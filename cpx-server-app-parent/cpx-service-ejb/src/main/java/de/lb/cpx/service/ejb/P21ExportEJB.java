/* 
 * Copyright (c) 2019 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner.
 * http://www.lohmann-birkner.de/de/index.php
 *
 * Contributors:
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.FileUtils;
import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.server.dao.TCaseDao;
import de.lb.cpx.service.jms.producer.P21MessageProducer;
import de.lb.cpx.shared.dto.MessageDTO;
import de.lb.cpx.shared.filter.ColumnOption;
import de.lb.cpx.shared.filter.FilterOption;
import de.lb.cpx.shared.filter.enums.WorkingListAttributes;
import de.lb.cpx.shared.p21util.P21ExportSettings;
import de.lb.cpx.shared.p21util.P21Version;
import de.lb.cpx.str.utils.StrUtils;
import de.lb.cpx.system.properties.CpxSystemProperties;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import javax.batch.runtime.BatchStatus;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.enterprise.concurrent.ManagedThreadFactory;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSContext;
import org.jboss.ejb3.annotation.SecurityDomain;

/**
 *
 * @author niemeier
 */
@Stateful
@SecurityDomain("cpx")
public class P21ExportEJB {

    private static final Logger LOG = Logger.getLogger(P21ExportEJB.class.getName());

    @EJB
    private WorkingListSearchService searchService;

    @Inject
    private JMSContext jmsContext;

    @Resource(lookup = "java:jboss/exported/jms/queue/CpxBatchImportStatusQueue")
    private Destination destination;

    @Resource(name = "java:comp/DefaultManagedThreadFactory")
    private ManagedThreadFactory tf;

    @EJB
    private TCaseDao caseDao;

    @EJB
    private P21MessageProducer producer;

    private BatchStatus batchstatus = BatchStatus.ABANDONED;

    public static final int MAX_PHASES = 16;

    public int getMaxPhases() {
        return MAX_PHASES;
    }

    protected void process(final long executionId, final boolean pIsLocal, final GDRGModel pGrouperModel, final P21ExportSettings pP21ExportSettings, final long[] pSelectedIds, java.util.Map<ColumnOption, List<FilterOption>> pOptions) {
        final int page = 0;
        final int fetchSize = -1; //-1 -> get all hospital cases that match the filter (paging ignored)!
        //CpxSystemPropertiesInterface props = CpxSystemProperties.getInstance();

        //final Path tmpPath = Files.createTempDirectory("cpx_p21_export");
        //final String tempFolder = System.getProperty("java.io.tmpdir") + "\\" + pToken;
        final String tempFolder = System.getProperty("java.io.tmpdir") + "\\p21_export_" + executionId + "\\";
        LOG.log(Level.INFO, "Temp folder for P21 export: " + tempFolder);
        final File tmp = new File(tempFolder);
        if (!tmp.exists()) {
            tmp.mkdirs();
        }

        final P21Version pVersion = pP21ExportSettings.getVersionEn();
        final int p21Version = pVersion == null ? 0 : pVersion.getYear();
        
        final AtomicInteger phase = new AtomicInteger(0);

        final Calendar currentDate = Calendar.getInstance();
        //final int currentYear = currentDate.get(Calendar.YEAR);
        final P21DateFormat dfFull = new P21DateFormat("yyyyMMddHHmm");
        final P21DateFormat dfDate = new P21DateFormat("yyyyMMdd");
        //final P21DateFormat dfTime = new P21DateFormat("HHmm");
        final P21DecimalFormat df = new P21DecimalFormat();
        final P21DecimalFormat mf = new P21DecimalFormat(2);

        //final int blockSize = 3;
        final String delimiter = ";";

        //Phase 1: Collecting data! We're only interested in fetching case ids!
        phase.incrementAndGet();
        sendStatusJobMessage(executionId, phase.get(), 0, 0, batchstatus, "Ermittle zu exportierende Fälle");
        Iterator<Map.Entry<ColumnOption, List<FilterOption>>> it = pOptions.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<ColumnOption, List<FilterOption>> entry = it.next();
            ColumnOption colOption = entry.getKey();
            //if (!colOption.attributeName.equals(WorkingListAttributes.id)) {
            colOption.setShouldShow(false);
            if (pSelectedIds != null && pSelectedIds.length > 0 && colOption.attributeName.equals(WorkingListAttributes.id)) {
                StringBuilder sb = new StringBuilder();
                for (long id : pSelectedIds) {
                    sb.append(id + ",");
                }
                if (!entry.getValue().isEmpty()) {
                    FilterOption option = entry.getValue().iterator().next();
                    option.setValue(sb.toString());
                }
//                        for (FilterOption option : entry.getValue()) {
//                            option.setValue(sb.toString());
//                            break;
//                        }
                //entry.getValue().add(new FilterOption("", WorkingListAttributes.id, sb.toString()));
            }
            //it.remove();
            //}
        }
        //pOptions.put(new ColumnOption WorkingListAttributes.patId, new ArrayList<>());
        //pOptions.put(WorkingListAttributes.patdId, new ArrayList<>());
        //pOptions.put(WorkingListAttributes.patNumber, new ArrayList<>());
        //pOptions.entrySet().add(WorkingListAttributes.patNumber);
        final String newLine = "\n";
        final int fetchRows = 5000; //very relevant for performance!
        final String query = searchService.getQuery(pIsLocal, false, pGrouperModel, page, fetchSize, pOptions);
        final String tmpTable = "P21_EXPORT_" + executionId;
        final String tmpSeq = "P21_EXPORT_" + executionId + "_SQ";
        LOG.log(Level.INFO, "Write temporary case ids to table {0}", tmpTable);
        final boolean isOracle = caseDao.isOracle();
        final String newQuery = (isOracle ? "CREATE TABLE " + tmpTable + " NOLOGGING PARALLEL 5 AS " : "")
                + "SELECT " + nextSqVal(isOracle, tmpSeq) + " ID, CS.CS_HOSPITAL_IDENT, CS.CS_CASE_NUMBER, TMP2.* "
                + (isOracle ? "" : " INTO " + tmpTable)
                + " FROM ("
                + "SELECT DISTINCT ID_7126 T_CASE_ID, ID_1927 T_CASE_DETAILS_ID, T_PATIENT_ID, T_PATIENT_DETAILS_ID "
                // + (isOracle ? "" : " INTO " + tmpTable)
                + "FROM (" + query + ") TMP "
                + ") TMP2 "
                + "INNER JOIN T_CASE CS ON CS.ID = TMP2.T_CASE_ID";

        final String caseTable = tmpTable + "_CS";
        final String icdTable = tmpTable + "_ICD";
        final String opsTable = tmpTable + "_OPS";
        final String feeTable = tmpTable + "_FEE";
        final String depTable = tmpTable + "_DEP";
        File zipFile = null;

        try ( Statement stmt = caseDao.getConnection().createStatement()) {
            try {
                //stmt = caseDao.getConnection().createStatement();

                //Phase: clean old tables
                phase.incrementAndGet();
                sendStatusJobMessage(executionId, phase.get(), 0, 0, batchstatus, "Lösche veraltete temporäre Tabellen");
                dropOldTables(stmt, isOracle);

                if (BatchStatus.STOPPING.equals(batchstatus)) {
                    return;
                }
                //Phase 2: create temporary table for collected case ids
                phase.incrementAndGet();
                sendStatusJobMessage(executionId, phase.get(), 0, 0, batchstatus, "Erstelle temporäre ID-Tabelle");

                dropTable(stmt, tmpTable);
                dropSequence(stmt, tmpSeq);
                stmt.execute("CREATE SEQUENCE " + tmpSeq);
                stmt.execute(newQuery);

                if (BatchStatus.STOPPING.equals(batchstatus)) {
                    return;
                }

                //Phase 3: create indizes
                phase.incrementAndGet();
                sendStatusJobMessage(executionId, phase.get(), 0, 0, batchstatus, "Erzeuge Indizes");
                stmt.execute("ALTER TABLE " + tmpTable + " ADD CONSTRAINT " + tmpTable + "_PK PRIMARY KEY (ID)");
                stmt.execute("CREATE INDEX I_" + tmpTable + "_CS ON " + tmpTable + " (T_CASE_ID)");
                stmt.execute("CREATE INDEX I_" + tmpTable + "_CSD ON " + tmpTable + " (T_CASE_DETAILS_ID)");
                stmt.execute("CREATE INDEX I_" + tmpTable + "_PT ON " + tmpTable + " (T_PATIENT_ID)");
                stmt.execute("CREATE INDEX I_" + tmpTable + "_PTD ON " + tmpTable + " (T_PATIENT_DETAILS_ID)");

                if (BatchStatus.STOPPING.equals(batchstatus)) {
                    return;
                }

                //Phase 4: get number of hospital cases
                phase.incrementAndGet();
                sendStatusJobMessage(executionId, phase.get(), 0, 0, batchstatus, "Ermittle die Anzahl der Fälle");
                int caseCount = -1;
                try ( ResultSet rs = stmt.executeQuery("SELECT COUNT(*) CNT FROM " + tmpTable)) {
                    while (rs.next()) {
                        caseCount = rs.getInt("CNT");
                    }
                }
                LOG.log(Level.INFO, "Found {0} cases to export in P21 format", caseCount);

                if (BatchStatus.STOPPING.equals(batchstatus)) {
                    return;
                }

                //Phase 6: create temporary case table
                phase.incrementAndGet();
                sendStatusJobMessage(executionId, phase.get(), 0, 0, batchstatus, "Erzeuge temporäre Falltabelle");
                dropTable(stmt, caseTable);
                stmt.execute((isOracle ? "CREATE TABLE " + caseTable + " NOLOGGING PARALLEL 5 AS " : "")
                        + "SELECT "
                        + "TMP.ID TMP_ID, "
                        + "CS.CS_HOSPITAL_IDENT, "
                        + "CS.CS_CASE_NUMBER, "
                        + "CS.CS_CASE_TYPE_EN, "
                        + "CS.INSURANCE_NUMBER_PATIENT, "
                        + "CS.INSURANCE_IDENTIFIER, "
                        + "PAT.PAT_DATE_OF_BIRTH, "
                        + "CSD.GENDER_EN, "
                        + "PATD.PATD_ZIPCODE, "
                        + "PATD.PATD_STATE, "
                        + "CSD.ADMISSION_DATE, "
                        + "CSD.ADMISSION_CAUSE_EN, "
                        + "CSD.ADMISSION_REASON_12_EN, "
                        + "CSD.ADMISSION_REASON_34_EN, "
                        + "CSD.ADMISSION_WEIGHT, "
                        + "CSD.DISCHARGE_DATE, "
                        + "CSD.DISCHARGE_REASON_12_EN, "
                        + "CSD.DISCHARGE_REASON_3_EN, "
                        + "CSD.AGE_DAYS, "
                        + "CSD.AGE_YEARS, "
                        + "PAT.PAT_NUMBER, "
                        + "CSD.HMV, "
                        + "CSD.LOS_INTENSIV "
                        + (isOracle ? "" : " INTO " + caseTable)
                        + " FROM T_CASE CS "
                        + "INNER JOIN " + tmpTable + " TMP ON CS.ID = TMP.T_CASE_ID "
                        + "LEFT JOIN T_CASE_DETAILS CSD ON CSD.ID = TMP.T_CASE_DETAILS_ID "
                        + "LEFT JOIN T_PATIENT PAT ON PAT.ID = TMP.T_PATIENT_ID "
                        + "LEFT JOIN T_PATIENT_DETAILS PATD ON PATD.ID = TMP.T_PATIENT_DETAILS_ID ");
                // + "ORDER BY TMP.ID");

                if (BatchStatus.STOPPING.equals(batchstatus)) {
                    return;
                }

                //Phase 7: create temporary icd table
                phase.incrementAndGet();
                sendStatusJobMessage(executionId, phase.get(), 0, 0, batchstatus, "Erzeuge temporäre ICD-Tabelle");
                dropTable(stmt, icdTable);
                stmt.execute((isOracle ? "CREATE TABLE " + icdTable + " NOLOGGING PARALLEL 5 AS " : "")
                        + "SELECT "
                        + "TMP.ID TMP_ID, "
                        // + "TMP.CS_CASE_NUMBER, "
                        // + "TMP.CS_HOSPITAL_IDENT, "
                        + "ICD.MAIN_DIAG_CASE_FL, "
                        + "ICD.ICDC_CODE, "
                        + "ICD.ICDC_LOC_EN, "
                        // + "ICD.ICD_REFERENCE_EN, "
                        + "ICD_REF.MAIN_DIAG_CASE_FL MAIN_DIAG_CASE_FL_REF, "
                        + "ICD_REF.ICDC_CODE ICDC_CODE_REF, "
                        + "ICD_REF.ICDC_LOC_EN ICDC_LOC_EN_REF "
                        + (isOracle ? "" : " INTO " + icdTable)
                        + " FROM T_CASE_ICD ICD "
                        + "INNER JOIN T_CASE_DEPARTMENT DEP ON DEP.ID = ICD.T_CASE_DEPARTMENT_ID "
                        + "INNER JOIN " + tmpTable + " TMP ON DEP.T_CASE_DETAILS_ID = TMP.T_CASE_DETAILS_ID "
                        //+ "INNER JOIN T_CASE CS ON CS.ID = TMP.T_CASE_ID "
                        + "LEFT JOIN T_CASE_ICD ICD_REF ON ICD_REF.ID = ICD.T_CASE_ICD_ID "
                        + "WHERE ICD.T_CASE_ICD_ID IS NOT NULL OR NOT EXISTS (SELECT 1 FROM T_CASE_ICD ICD2 WHERE ICD2.T_CASE_ICD_ID = ICD.ID) ");
                //+ "ORDER BY TMP.ID");

                if (BatchStatus.STOPPING.equals(batchstatus)) {
                    return;
                }

                //Phase 8: create temporary ops table
                phase.incrementAndGet();
                sendStatusJobMessage(executionId, phase.get(), 0, 0, batchstatus, "Erzeuge temporäre OPS-Tabelle");
                dropTable(stmt, opsTable);
                stmt.execute((isOracle ? "CREATE TABLE " + opsTable + " NOLOGGING PARALLEL 5 AS " : "")
                        + "SELECT "
                        + "TMP.ID TMP_ID, "
                        // + "TMP.CS_CASE_NUMBER, "
                        // + "TMP.CS_HOSPITAL_IDENT, "
                        + "OPS.OPSC_CODE, "
                        + "OPS.OPSC_LOC_EN, "
                        + "OPS.OPSC_DATUM "
                        + (isOracle ? "" : " INTO " + opsTable)
                        + " FROM T_CASE_OPS OPS "
                        + "INNER JOIN T_CASE_DEPARTMENT DEP ON DEP.ID = OPS.T_CASE_DEPARTMENT_ID "
                        + "INNER JOIN " + tmpTable + " TMP ON DEP.T_CASE_DETAILS_ID = TMP.T_CASE_DETAILS_ID ");
                //+ "INNER JOIN T_CASE CS ON CS.ID = TMP.T_CASE_ID "
                // + "ORDER BY TMP.ID, OPS.OPSC_DATUM");

                if (BatchStatus.STOPPING.equals(batchstatus)) {
                    return;
                }

                //Phase 9: create temporary fee table
                phase.incrementAndGet();
                sendStatusJobMessage(executionId, phase.get(), 0, 0, batchstatus, "Erzeuge temporäre Entgelte-Tabelle");
                dropTable(stmt, feeTable);
                stmt.execute((isOracle ? "CREATE TABLE " + feeTable + " NOLOGGING PARALLEL 5 AS " : "")
                        + "SELECT "
                        + "TMP.ID TMP_ID, "
                        // + "TMP.CS_CASE_NUMBER, "
                        // + "TMP.CS_HOSPITAL_IDENT, "
                        + "FEE.FEEC_INSURANCE, "
                        + "FEE.FEEC_FEEKEY, "
                        + "FEE.FEEC_VALUE, "
                        + "FEE.FEEC_FROM, "
                        + "FEE.FEEC_TO, "
                        + "FEE.FEEC_COUNT, "
                        + "FEE.FEEC_UNBILLED_DAYS "
                        + (isOracle ? "" : " INTO " + feeTable)
                        + " FROM T_CASE_FEE FEE "
                        + "INNER JOIN " + tmpTable + " TMP ON FEE.T_CASE_DETAILS_ID = TMP.T_CASE_DETAILS_ID ");
                //+ "INNER JOIN T_CASE CS ON CS.ID = TMP.T_CASE_ID "
                // + "ORDER BY TMP.ID, FEE.FEEC_FROM, FEE.FEEC_TO");

                if (BatchStatus.STOPPING.equals(batchstatus)) {
                    return;
                }

                //Phase 9: create temporary department table
                phase.incrementAndGet();
                sendStatusJobMessage(executionId, phase.get(), 0, 0, batchstatus, "Erzeuge temporäre Abteilungen-Tabelle");
                dropTable(stmt, depTable);
                stmt.execute((isOracle ? "CREATE TABLE " + depTable + " NOLOGGING PARALLEL 5 AS " : "")
                        + "SELECT "
                        + "TMP.ID TMP_ID, "
                        // + "TMP.CS_CASE_NUMBER, "
                        // + "TMP.CS_HOSPITAL_IDENT, "
                        + "DEP.DEPC_ADMOD_EN, "
                        + "DEP.DEP_KEY_301, "
                        + "DEP.DEPC_ADM_DATE, "
                        + "DEP.DEPC_DIS_DATE, "
                        + "DEP.DEPC_IS_BED_INTENSIV_FL "
                        + (isOracle ? "" : " INTO " + depTable)
                        + " FROM T_CASE_DEPARTMENT DEP "
                        + "INNER JOIN " + tmpTable + " TMP ON DEP.T_CASE_DETAILS_ID = TMP.T_CASE_DETAILS_ID ");
                //+ "INNER JOIN T_CASE CS ON CS.ID = TMP.T_CASE_ID "
                // + "ORDER BY TMP.ID, DEP.DEPC_ADM_DATE, DEP.DEPC_DIS_DATE");

                if (BatchStatus.STOPPING.equals(batchstatus)) {
                    return;
                }

                //Phase 10: load data from database
                phase.incrementAndGet();
                sendStatusJobMessage(executionId, phase.get(), 0, 0, batchstatus, "Lade die Daten für den Export");
                try (
                        final ResultSet rsCs = caseDao.createStatement(fetchRows).executeQuery("SELECT * FROM " + caseTable + " ORDER BY TMP_ID"); final ResultSet rsIcd = caseDao.createStatement(fetchRows).executeQuery("SELECT * FROM " + icdTable + " ORDER BY TMP_ID"); //TMP_ID, CASE WHEN ICDC_CODE_REF IS NULL THEN ICDC_CODE ELSE ICDC_CODE_REF END, ICDC_CODE_REF
                         final ResultSet rsOps = caseDao.createStatement(fetchRows).executeQuery("SELECT * FROM " + opsTable + " ORDER BY TMP_ID"); //TMP_ID, OPSC_DATUM, OPSC_CODE
                         final ResultSet rsFee = caseDao.createStatement(fetchRows).executeQuery("SELECT * FROM " + feeTable + " ORDER BY TMP_ID"); //TMP_ID, FEEC_FROM, FEEC_TO, FEEC_FEEKEY
                         final ResultSet rsDep = caseDao.createStatement(fetchRows).executeQuery("SELECT * FROM " + depTable + " ORDER BY TMP_ID"); //TMP_ID, DEPC_ADM_DATE, DEPC_DIS_DATE, DEP_SHORT_NAME
                        ) {
                    //set cursors on first entry
                    final boolean depEmpty = !rsDep.next();
                    final boolean feeEmpty = !rsFee.next();
                    final boolean opsEmpty = !rsOps.next();
                    final boolean icdEmpty = !rsIcd.next();

                    //Phase 5: write basic csv files
                    phase.incrementAndGet();
                    sendStatusJobMessage(executionId, phase.get(), 0, 0, batchstatus, "Erstelle die CSV-Dateien");

                    final String charset = CpxSystemProperties.DEFAULT_ENCODING;
                    try (final Writer fallWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFolder + "FALL.csv"), charset)); final Writer entWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFolder + "Entgelte.csv"), charset)); final Writer icdWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFolder + "ICD.csv"), charset)); final Writer opsWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFolder + "OPS.csv"), charset)); final Writer fabWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFolder + "FAB.csv"), charset)); final Writer infoWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFolder + "INFO.csv"), charset)); final Writer hosWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFolder + "Krankenhaus.csv"), charset))) {
                        final Writer[] writers = new Writer[]{fallWriter, entWriter, icdWriter, opsWriter, fabWriter, infoWriter, hosWriter};

                        phase.incrementAndGet();
                        sendStatusJobMessage(executionId, phase.get(), 0, 0, batchstatus, "Schreibe Daten in die CSV-Dateien");

                        fallWriter.write("IK;Entlassender-Standort;Entgeltbereich;KH-internes-Kennzeichen;Versicherten-ID;Vertragskennzeichen-64b-Modellvorhaben;IK-der-Krankenkasse;Geburtsjahr;Geburtsmonat;Geschlecht;PLZ;Wohnort;Aufnahmedatum;Aufnahmeanlass;Aufnahmegrund;Fallzusammenführung;Fallzusammenführungsgrund;Aufnahmegewicht;Entlassungsdatum;Entlassungsgrund;Alter-in-Tagen-am-Aufnahmetag;Alter-in-Jahren-am-Aufnahmetag;Patientennummer;interkurrente-Dialysen;Beatmungsstunden;Behandlungsbeginn-vorstationär;Behandlungstage-vorstationär;Behandlungsende-nachstationär;Behandlungstage-nachstationär;IK-Verlegungs-KH;Belegungstage-in-anderem-Entgeltbereich;Beurlaubungstage-PSY;Kennung-Besonderer-Fall-Modellvorhaben;Verweildauer Intensiv" + newLine);
                        icdWriter.write("IK;Entlassender-Standort;Entgeltbereich;KH-internes-Kennzeichen;Diagnoseart;ICD-Version;ICD-Kode;Lokalisation;Diagnosensicherheit;Sekundär-Kode;Lokalisation2;Diagnosensicherheit2" + newLine);
                        opsWriter.write("IK;Entlassender-Standort;Entgeltbereich;KH-internes-Kennzeichen;OPS-Version;OPS-Kode;Lokalisation;OPS-Datum;Belegoperateur;Beleganästhesist;Beleghebamme" + newLine);
                        entWriter.write("IK;Entlassender-Standort;Entgeltbereich;KH-internes-Kennzeichen;IK-Krankenkasse;Entgeltart;Entgeltbetrag;Abrechnung-von;Abrechnung-bis;Entgeltanzahl;Tage-ohne-Berechnung-Behandlung;Tag-der-Behandlung" + newLine);
                        if(p21Version < 2020) {
                            fabWriter.write("IK;Entlassender-Standort;Entgeltbereich;KH-internes-Kennzeichen;FAB;FAB-Aufnahmedatum;FAB-Entlassungsdatum;Kennung Intensivbett" + newLine);
                        } else {
                            fabWriter.write("IK;Entlassender-Standort;Entgeltbereich;KH-internes-Kennzeichen;Standortnummer-Behandlungsort;Fachabteilung;FAB-Aufnahmedatum;FAB-Entlassungsdatum;Kennung Intensivbett" + newLine);
                        }
                        infoWriter.write("IK;Datenerhebung;Datum-der-Erstellung;E-Mail-Adresse;DRG-Grouper;Softwarelösung;Versionskennung;E-Mail-Adresse2" + newLine);
                        if(p21Version < 2020) {
                            hosWriter.write("IK;KH-Name;KH-Art;KH-Träger;Betten-DRG;Betten-PSY;Merkmal-Zu-Abschläge;Regionale-Versorgungsverpflichtung" + newLine);
                        } else {
                            hosWriter.write("IK;KH-Name;KH-Art;KH-Träger;Betten-DRG;Intensivbetten-DRG;Betten-PSY;Intensivbetten-PSY;Merkmal-Zu-Abschläge;Regionale-Versorgungsverpflichtung" + newLine);
                        }

                        //write info.csv
                        infoWriter.write(
                                (pP21ExportSettings.getIdent() == null ? "" : anonymizeHospitalIdent(pP21ExportSettings, pP21ExportSettings.getIdent())) + delimiter //IK
                                + pP21ExportSettings.getVersionEn().getLabel() + delimiter //Datenerhebung
                                + dfFull.format(currentDate.getTime()) + delimiter //Datum-der-Erstellung
                                + (pP21ExportSettings.getMail() == null ? "" : pP21ExportSettings.getMail()) + delimiter //E-Mail-Adresse
                                + (pGrouperModel == null ? "" : "Grouper " + pGrouperModel.toString()) + delimiter //DRG-Grouper
                                + "L&B CheckpointX;" //Softwarelösung
                                + pP21ExportSettings.getVersionEn().getVersionIdentifier() + delimiter //Versionskennung
                                + (pP21ExportSettings.getMail2() == null ? "" : pP21ExportSettings.getMail2()) //E-Mail-Adresse2
                                + newLine
                        );

                        String pBettDrgIntensiv = "";
                        String pBettPeppIntensiv = "";
                        String pEntlassStandort = "";
                        String pStandortNrBehandlung = "";
                        if(p21Version >= 2020) {
                            pBettDrgIntensiv = (pP21ExportSettings.getBedsDrgIntensiv() == null ? "0" : pP21ExportSettings.getBedsDrgIntensiv()) + delimiter;
                            pBettPeppIntensiv = (pP21ExportSettings.getBedsPeppIntensiv() == null ? "0" : pP21ExportSettings.getBedsPeppIntensiv()) + delimiter;
                            pStandortNrBehandlung = "0" + delimiter;
                            pEntlassStandort = "0";
                        }

                        //write krankenhaus.csv
                        hosWriter.write(
                                (pP21ExportSettings.getIdent() == null ? "" : anonymizeHospitalIdent(pP21ExportSettings, pP21ExportSettings.getIdent())) + delimiter //IK
                                + (pP21ExportSettings.getHosName() == null ? "" : pP21ExportSettings.getHosName()) + delimiter //KH-Name
                                + (pP21ExportSettings.getHosType() == null ? "" : pP21ExportSettings.getHosType()) + delimiter //KH-Art
                                + (pP21ExportSettings.getHosCostUnit() == null ? "" : pP21ExportSettings.getHosCostUnit()) + delimiter //KH-Träger
                                + (pP21ExportSettings.getBedsDrg() == null ? "0" : pP21ExportSettings.getBedsDrg()) + delimiter //Betten-DRG
                                +  pBettDrgIntensiv //Betten-DRG-Intesiv
                                + (pP21ExportSettings.getBedsPepp() == null ? "0" : pP21ExportSettings.getBedsPepp()) + delimiter //Betten-PSY
                                +  pBettPeppIntensiv //Betten-PSY-Intensiv    
                                + (pP21ExportSettings.getSurcharges() == null ? "" : pP21ExportSettings.getSurcharges()) + delimiter //Merkmal-Zu-Abschläge
                                + (pP21ExportSettings.isRegionalPensionObligation() ? "1" : "0") //Regionale-Versorgungsverpflichtung
                                + newLine
                        );

                        if (BatchStatus.STOPPING.equals(batchstatus)) {
                            return;
                        }

                        int caseNo = 0;
                        //String caseKey = "";
                        while (rsCs.next()) {
                            caseNo++;
                            //final String newCaseKey = rsCs.getString("CS_HOSPITAL_IDENT") + "_" + rsCs.getString("CS_CASE_NUMBER");
                            final long tmpId = rsCs.getLong(1);
                            final String hosIdent = anonymizeHospitalIdent(pP21ExportSettings, rsCs.getString(2)); //CS_HOSPITAL_IDENT
                            final String caseNumber = anonymizeCaseNumber(pP21ExportSettings, rsCs.getString(3)); //CS_CASE_NUMBER
                            //LOG.log(Level.FINEST, "TMP_ID: " + tmpId + ", Case Key: " + hosIdent + "_" + caseNumber);
                            final String caseType = formatCaseType(rsCs.getString(4)); //CS_CASE_TYPE_EN
                            final Date admissionDate = rsCs.getDate(11);
                            final Integer admissionYear = extractYear(admissionDate);

                            //write fall.csv
                            fallWriter.write(hosIdent + delimiter); //IK, CS_HOSPITAL_IDENT
                            fallWriter.write(pEntlassStandort + delimiter); //Entlassender-Standort
                            fallWriter.write(caseType + delimiter); //Entgeltbereich
                            fallWriter.write(caseNumber + delimiter); //KH-internes-Kennzeichen, CS_CASE_NUMBER
                            fallWriter.write(anonymizeInsuranceNumberPatient(pP21ExportSettings, rsCs.getString(5)) + delimiter); //Versicherten-ID, INSURANCE_NUMBER_PATIENT
                            fallWriter.write("" + delimiter); //Vertragskennzeichen-64b-Modellvorhaben
                            fallWriter.write(anonymizeInsuranceIdentifier(pP21ExportSettings, rsCs.getString(6)) + delimiter); //IK-der-Krankenkasse, INSURANCE_IDENTIFIER
                            final Date dateOfBirth = rsCs.getDate(7);
                            fallWriter.write(getYear(dateOfBirth) + delimiter); //Geburtsjahr, PAT_DATE_OF_BIRTH
                            fallWriter.write(getMonth(dateOfBirth) + delimiter); //Geburtsmonat, PAT_DATE_OF_BIRTH
                            fallWriter.write(rsCs.getString(8) + delimiter); //Geschlecht, PAT_GENDER_EN
                            final String zipCode = format(rsCs.getString(9));
                            final String state = format(rsCs.getString(10));
                            fallWriter.write(zipCode + delimiter); //PLZ, PATD_ZIPCODE
                            fallWriter.write(state + delimiter); //Wohnort, PATD_STATE
                            fallWriter.write(dfFull.format(admissionDate) + delimiter); //Aufnahmedatum, ADMISSION_DATE
                            fallWriter.write(format(rsCs.getString(12)) + delimiter); //Aufnahmeanlass, ADMISSION_CAUSE_EN
                            fallWriter.write(format(rsCs.getString(13)) + format(rsCs.getString(14)) + delimiter); //Aufnahmegrund, ADMISSION_REASON_12_EN + ADMISSION_REASON_34_EN
                            fallWriter.write("N" + delimiter); //Fallzusammenführung
                            fallWriter.write("" + delimiter); //Fallzusammenführungsgrund
                            fallWriter.write(format(rsCs.getString(15)) + delimiter); //Aufnahmegewicht, ADMISSION_WEIGHT
                            fallWriter.write(dfFull.format(rsCs.getDate(16)) + delimiter); //Entlassungsdatum, DISCHARGE_DATE
                            fallWriter.write(format(rsCs.getString(17)) + format(rsCs.getString(18)) + delimiter); //Entlassungsgrund, DISCHARGE_REASON_12_EN + DISCHARGE_REASON_3_EN
                            fallWriter.write(format(rsCs.getString(19)) + delimiter); //Alter-in-Tagen-am-Aufnahmetag, AGE_DAYS
                            fallWriter.write(format(rsCs.getString(20)) + delimiter); //Alter-in-Jahren-am-Aufnahmetag, AGE_YEARS
                            fallWriter.write(anonymizePatientNumber(pP21ExportSettings, rsCs.getString(21)) + delimiter); //Patientennummer, PAT_NUMBER
                            fallWriter.write("" + delimiter); //interkurrente-Dialysen
                            fallWriter.write(format(rsCs.getString(22)) + delimiter); //Beatmungsstunden, HMV
                            fallWriter.write("" + delimiter); //Behandlungsbeginn-vorstationär
                            fallWriter.write("" + delimiter); //Behandlungstage-vorstationär
                            fallWriter.write("" + delimiter); //Behandlungsende-nachstationär
                            fallWriter.write("" + delimiter); //Behandlungstage-nachstationär
                            fallWriter.write("" + delimiter); //IK-Verlegungs-KH
                            fallWriter.write("" + delimiter); //Belegungstage-in-anderem-Entgeltbereich
                            fallWriter.write("" + delimiter); //Beurlaubungstage-PSY
                            fallWriter.write("" + delimiter); //Kennung-Besonderer-Fall-Modellvorhaben
                            fallWriter.write(format(rsCs.getString(23))); //Verweildauer Intensiv, LOS_INTENSIV
                            fallWriter.write(newLine);
                            if (!icdEmpty) {
                                while (true) {
                                    //!rsIcd.isClosed() || rsIcd.next() && hosIdent.equals(rsIcd.getString("CS_HOSPITAL_IDENT")) && caseNumber.equals(rsIcd.getString("CS_CASE_NUMBER"))
//                                        if (rsIcd.getRow() == 0) {
//                                            rsIcd.next();
//                                        }
                                    if (rsIcd.isAfterLast()) {
                                        break;
                                    }
                                    if (tmpId != rsIcd.getLong(1)) {
                                        break;
                                    }

                                    final String mainDiagFl = formatIcdType(rsIcd.getBoolean(2));
                                    final String icdCode = format(rsIcd.getString(3));
                                    final String icdLoc = formatLoc(rsIcd.getString(4));

                                    final String mainDiagFlRef = formatIcdType(rsIcd.getBoolean(5));
                                    final String icdRefCode = format(rsIcd.getString(6));
                                    final String icdRefLoc = formatLoc(rsIcd.getString(7));

                                    //write icd.csv
                                    icdWriter.write(hosIdent + delimiter); //IK, CS_HOSPITAL_IDENT
                                    icdWriter.write(pEntlassStandort + delimiter); //Entlassender-Standort
                                    icdWriter.write(caseType + delimiter); //Entgeltbereich
                                    icdWriter.write(caseNumber + delimiter); //KH-internes-Kennzeichen, CS_CASE_NUMBER
                                    icdWriter.write((icdRefCode.isEmpty() ? mainDiagFl : mainDiagFlRef) + delimiter); //Diagnoseart, MAIN_DIAG_CASE_FL
                                    icdWriter.write((admissionYear == null ? "" : admissionYear) + delimiter); //ICD-Version
                                    icdWriter.write((icdRefCode.isEmpty() ? icdCode : icdRefCode) + delimiter); //ICD-Kode, ICDC_CODE
                                    icdWriter.write((icdRefLoc.isEmpty() ? icdLoc : icdRefLoc) + delimiter); //Lokalisation, ICDC_LOC_EN
                                    icdWriter.write("" + delimiter); //Diagnosensicherheit
                                    icdWriter.write((icdRefCode.isEmpty() ? "" : icdCode) + delimiter); //Sekundär-Kode, ICDC_CODE_REF
                                    icdWriter.write((icdRefLoc.isEmpty() ? "" : icdLoc) + delimiter); //Lokalisation2, ICDC_LOC_EN_REF
                                    icdWriter.write(""); //Diagnosensicherheit2                                            
                                    icdWriter.write(newLine);
                                    if (!rsIcd.next()) {
                                        break;
                                    }
                                }
                            }
                            if (!opsEmpty) {
                                while (true) { //rsOps.next() && hosIdent.equals(rsOps.getString("CS_HOSPITAL_IDENT")) && caseNumber.equals(rsOps.getString("CS_CASE_NUMBER"))) {
//                                        if (rsOps.getRow() == 0) {
//                                            rsOps.next();
//                                        }
                                    if (rsOps.isAfterLast()) {
                                        break;
                                    }
                                    if (tmpId != rsOps.getLong(1)) {
                                        break;
                                    }

                                    //write ops.csv
                                    opsWriter.write(hosIdent + delimiter); //IK, CS_HOSPITAL_IDENT
                                    opsWriter.write(pEntlassStandort + delimiter); //Entlassender-Standort
                                    opsWriter.write(caseType + delimiter); //Entgeltbereich
                                    opsWriter.write(caseNumber + delimiter); //KH-internes-Kennzeichen, CS_CASE_NUMBER
                                    opsWriter.write(admissionYear + delimiter); //OPS-Version
                                    opsWriter.write(rsOps.getString(2) + delimiter); //OPS-Kode, OPSC_CODE
                                    opsWriter.write(formatLoc(rsOps.getString(3)) + delimiter); //Lokalisation, OPSC_LOC_EN
                                    opsWriter.write(dfFull.format(rsOps.getDate(4)) + delimiter); //OPS-Datum, OPSC_DATUM
                                    opsWriter.write("N" + delimiter); //Belegoperateur
                                    opsWriter.write("N" + delimiter); //Beleganästhesist
                                    opsWriter.write("N"); //Beleghebamme                                            
                                    opsWriter.write(newLine);
                                    if (!rsOps.next()) {
                                        break;
                                    }
                                }
                            }
                            if (!feeEmpty) {
                                while (true) { //rsOps.next() && hosIdent.equals(rsOps.getString("CS_HOSPITAL_IDENT")) && caseNumber.equals(rsOps.getString("CS_CASE_NUMBER"))) {
//                                        if (rsFee.getRow() == 0) {
//                                            rsFee.next();
//                                        }
                                    if (rsFee.isAfterLast()) {
                                        break;
                                    }
                                    if (tmpId != rsFee.getLong(1)) {
                                        break;
                                    }

                                    //write entgelte.csv
                                    entWriter.write(hosIdent + delimiter); //IK, CS_HOSPITAL_IDENT
                                    entWriter.write(pEntlassStandort + delimiter); //Entlassender-Standort
                                    entWriter.write(caseType + delimiter); //Entgeltbereich
                                    entWriter.write(caseNumber + delimiter); //KH-internes-Kennzeichen, CS_CASE_NUMBER
                                    entWriter.write(anonymizeFeeKeyInsurance(pP21ExportSettings, rsFee.getString(2)) + delimiter); //IK-Krankenkasse, FEEC_INSURANCE
                                    entWriter.write(rsFee.getString(3) + delimiter); //Entgeltart, FEEC_FEEKEY
                                    entWriter.write(mf.format(rsFee.getDouble(4)) + delimiter); //Entgeltbetrag, FEEC_VALUE
                                    entWriter.write(dfDate.format(rsFee.getDate(5)) + delimiter); //Abrechnung-von, FEEC_FROM
                                    entWriter.write(dfDate.format(rsFee.getDate(6)) + delimiter); //Abrechnung-bis, FEEC_TO
                                    entWriter.write(df.format(rsFee.getDouble(7)) + delimiter); //Entgeltanzahl, FEEC_COUNT
                                    entWriter.write(rsFee.getString(8) + delimiter); //Tage-ohne-Berechnung-Behandlung, FEEC_UNBILLED_DAYS
                                    entWriter.write(""); //Tag-der-Behandlung                            
                                    entWriter.write(newLine);
                                    if (!rsFee.next()) {
                                        break;
                                    }
                                }
                            }
                            if (!depEmpty) {
                                while (true) { //rsOps.next() && hosIdent.equals(rsOps.getString("CS_HOSPITAL_IDENT")) && caseNumber.equals(rsOps.getString("CS_CASE_NUMBER"))) {
//                                        if (rsDep.getRow() == 0) {
//                                            rsDep.next();
//                                        }
                                    if (rsDep.isAfterLast()) {
                                        break;
                                    }
                                    if (tmpId != rsDep.getLong(1)) {
                                        break;
                                    }

                                    //write fab.csv
                                    fabWriter.write(hosIdent + delimiter); //IK, CS_HOSPITAL_IDENT
                                    fabWriter.write(pEntlassStandort + delimiter); //Entlassender-Standort
                                    fabWriter.write(caseType + delimiter); //Entgeltbereich
                                    fabWriter.write(caseNumber + delimiter); //KH-internes-Kennzeichen, CS_CASE_NUMBER
                                    fabWriter.write(pStandortNrBehandlung); //Standortnummer Behandlungsort
                                    fabWriter.write(formatAdmod(rsDep.getString(2)) + format(rsDep.getString(3)) + delimiter); //FAB, DEPC_ADMOD_EN + DEP_SHORT_NAME
                                    fabWriter.write(dfFull.format(rsDep.getDate(4)) + delimiter); //FAB-Aufnahmedatum, DEPC_ADM_DATE
                                    fabWriter.write(dfFull.format(rsDep.getDate(5)) + delimiter); //FAB-Entlassungsdatum, DEPC_DIS_DATE
                                    fabWriter.write(rsDep.getString(6)); //Kennung Intensivbett, DEPC_IS_BED_INTENSIV_FL
                                    fabWriter.write(newLine);
                                    if (!rsDep.next()) {
                                        break;
                                    }
                                }
                            }
                            if (BatchStatus.STOPPING.equals(batchstatus)) {
                                return;
                                //batchstatus = BatchStatus.ABANDONED;
                                //break;
                            }
                            //don't send progress message for each case
                            if (caseNo % 1000 == 0 || caseNo == caseCount) {
                                LOG.log(Level.INFO, "Written {0}/{1} cases to csv files", new Object[]{caseNo, caseCount});
                                sendStatusJobMessage(executionId, phase.get(), caseNo /* number of files written */, caseCount /* of total number of files */, batchstatus, "Schreibe Fall " + String.format(java.util.Locale.GERMAN, "%,d", caseNo) + "/" + String.format(java.util.Locale.GERMAN, "%,d", caseCount) + " in die CSV-Dateien...");
                            }
                        }
                        //Phase 12: Close writers
                        phase.incrementAndGet();
                        sendStatusJobMessage(executionId, phase.get(), 0, 0, batchstatus, "Finalisiere P21-Dateien");
                        for (Writer writer : writers) {
                            writer.flush();
                            writer.close();
                        }
                        File[] files = tmp.listFiles();
                        if (!BatchStatus.STOPPED.equals(batchstatus)) {
                            //Phase 13: Compress files to ZIP for upload
                            zipFile = new File(tmp.getAbsolutePath() + "\\" + tmp.getName() + ".zip");
                            LOG.log(Level.INFO, "Compress P21 files to " + zipFile.getAbsolutePath() + "...");
                            phase.incrementAndGet();
                            sendStatusJobMessage(executionId, phase.get(), 0, 0, batchstatus, "Packe die P21-Dateien für den Download");
                            FileUtils.zipFiles(files, zipFile);
                            if (zipFile.exists()) {
                                zipFile.deleteOnExit(); //zip file can be deleted later
                            }
                            //deleteFile(zipFile, false); //zip file can be deleted later
                            LOG.log(Level.INFO, "Compressing P21 files finished!");
                        }

//                                LOG.log(Level.INFO, "Delete csv files...");
//                                phase.incrementAndGet();
//                                sendStatusJobMessage(executionId, phase.get(), 0, 0, batchstatus, "Lösche CSV-Dateien auf dem Server");
//                                for (File file : files) {
//                                    deleteFile(file, true);
//                                }
                        if (!BatchStatus.STOPPED.equals(batchstatus)) {
                            //Phase 14: finishing export
                            LOG.log(Level.INFO, "Finishing P21 export...");
                            batchstatus = BatchStatus.COMPLETED;
                            phase.incrementAndGet();
                            sendStatusJobMessage(executionId, phase.get(), 0, 0, batchstatus, "P21-Export auf dem Server beendet");
                            LOG.log(Level.INFO, "Finished P21 export, files were written to {0}", tempFolder);
                        }
                        batchstatus = BatchStatus.ABANDONED;
                    } catch (IOException ex) {
                        batchstatus = BatchStatus.FAILED;
                        sendStatusFailureJobMessage(executionId, "Eine CSV-Datei konnte nicht zum Schreiben geöffnet werden", ex);
                        LOG.log(Level.SEVERE, "Cannot open file writer", ex);
                    }
                }
                //batchstatus = BatchStatus.COMPLETED;
                //sendStatusJobMessage(executionId, 2 /* phase */, caseCount, caseCount, batchstatus);
            } catch (SQLException ex) {
                batchstatus = BatchStatus.FAILED;
                sendStatusFailureJobMessage(executionId, "Es ist ein SQL-Fehler aufgetreten", ex);
                //LOG.log(Level.SEVERE, "Grouping of case id " + cs + " failed (blocking queue is probably full and writer thread died)", ex);
                LOG.log(Level.SEVERE, "Error in sql statement", ex);
            } finally {
                LOG.log(Level.INFO, "Delete csv files");
                for (File file : tmp.listFiles()) {
                    if (zipFile != null && !zipFile.equals(file)) {
                        deleteFile(file, true);
                    }
                }
                if (BatchStatus.STOPPING.equals(batchstatus)) {
                    LOG.log(Level.INFO, "Stopping P21 export...");
                    batchstatus = BatchStatus.STOPPED;
                    sendStatusJobMessage(executionId, phase.get(), 0, 0, batchstatus, "Export abgebrochen");
                    LOG.log(Level.INFO, "P21 export stopped!");
                    deleteFile(tmp, true);
                    //batchstatus = BatchStatus.ABANDONED;
                    //break;
                }

                dropTable(stmt, tmpTable);
                dropSequence(stmt, tmpSeq);
                dropTable(stmt, caseTable);
                dropTable(stmt, icdTable);
                dropTable(stmt, opsTable);
                dropTable(stmt, feeTable);
                dropTable(stmt, depTable);
                batchstatus = BatchStatus.ABANDONED;
            }
        } catch (SQLException ex) {
            batchstatus = BatchStatus.FAILED;
            sendStatusFailureJobMessage(executionId, "Es ist ein SQL-Fehler aufgetreten", ex);
            //LOG.log(Level.SEVERE, "Grouping of case id " + cs + " failed (blocking queue is probably full and writer thread died)", ex);
            LOG.log(Level.SEVERE, "Error in sql statement", ex);
        }
    }

    //@TransactionAttribute(TransactionAttributeType.MANDATORY)
    public long prepareExport(final boolean pIsLocal, final GDRGModel pGrouperModel, final P21ExportSettings pP21ExportSettings, final long[] pSelectedIds, final Map<ColumnOption, List<FilterOption>> pOptions) {
        final long executionId = System.currentTimeMillis();

        tf.newThread(new Runnable() {
            @Override
            //@TransactionAttribute(TransactionAttributeType.MANDATORY)
            public void run() {
                if (!BatchStatus.ABANDONED.equals(batchstatus)) {
                    return;
                }
                batchstatus = BatchStatus.STARTED;

                process(executionId, pIsLocal, pGrouperModel, pP21ExportSettings, pSelectedIds, pOptions);
            }
        }).start();

        return executionId;
    }
    //    private void deleteFile(final String pTempFolder, final boolean pDeleteImmediatly) {
    //        deleteFile(new File(pTempFolder), pDeleteImmediatly);
    //    }
    //    private void deleteFile(final File[] pFile, final boolean pDeleteImmediatly) {
    //        for (File file : pFile) {
    //            deleteFile(file, pDeleteImmediatly);
    //        }
    //    }

    public static void deleteFile(final File pFile, final boolean pDeleteImmediatly) {
        if (pFile == null) {
            return;
        }
        LOG.log(Level.INFO, "Delete file/folder " + pFile.getAbsolutePath() + "...");
        //File file = new File(pTempFolder);
        if (!pFile.exists()) {
            return;
        }
        if (pFile.isDirectory()) {
            for (File file : pFile.listFiles()) {
                deleteFile(file, pDeleteImmediatly);
            }
        }
        try {
            Files.delete(pFile.toPath());
            LOG.log(Level.FINEST, "deleted file: {0}", pFile.getAbsolutePath());
        } catch (IOException ex) {
            LOG.log(Level.WARNING, "was not able to delete file: " + pFile.getAbsolutePath(), ex);
            if (!pDeleteImmediatly) {
                pFile.deleteOnExit();
            }
        }
//        if (!pDeleteImmediatly || !pFile.delete()) {
//            pFile.deleteOnExit();
//        }
    }

    private static boolean dropTable(final Statement pStmt, final String pTableName) {
        final String query = "DROP TABLE " + pTableName;
        try {
            pStmt.execute(query);
            return true;
        } catch (SQLException ex) {
            LOG.log(Level.FINEST, "Cannot drop table: " + query, ex);
            return false;
        }
    }

    private static boolean dropSequence(final Statement pStmt, final String pSequenceName) {
        final String query = "DROP SEQUENCE " + pSequenceName;
        try {
            pStmt.execute(query);
            return true;
        } catch (SQLException ex) {
            LOG.log(Level.FINEST, "Cannot drop sequence: " + query, ex);
            return false;
        }
    }

    public boolean stopExport(final long pToken) {
        if (BatchStatus.ABANDONED.equals(batchstatus)) {
            return true;
        }
        batchstatus = BatchStatus.STOPPING;
        return false;
    }

    private void sendStatusJobMessage(final long pExcecutionId, final int pPhase, final int pSubphase, final int pMaxSubphases, final BatchStatus pBatchStatus, final String pComment) {
//        if (Integer.valueOf(pNumberOfProcessedItems).equals(pNumberOfAllItems)) {
//            //mLogger.log(Level.INFO, "JmsProducer send message: {0}+ Batch-Status:{1} + JOB_Batch-Status:{2}", new Object[]{pNumberOfProcessedItems, BatchStatus.COMPLETED});
//            LOG.log(Level.INFO, "P21ExportEJB:sendStatusJobMessage:if: ExecutionID: {0}", pExcecutionId);
//            sendObjectMessage(pExcecutionId, new P21ExportMessageDTO(pPhase, pNumberOfProcessedItems, pNumberOfAllItems, BatchStatus.COMPLETED, null, 0, null), "importStatusMessage");
//        } else {
        //mLogger.log(Level.INFO, "JmsProducer send message: {0}+ Batch-Status:{1} + JOB_Batch-Status:{2}", new Object[]{pNumberOfProcessedItems, BatchStatus.STARTED});
        LOG.log(Level.FINEST, "P21ExportEJB:sendStatusJobMessage:: ExecutionID: {0}", pExcecutionId);
        sendObjectMessage(pExcecutionId, new MessageDTO(pPhase, MAX_PHASES, pSubphase, pMaxSubphases, pBatchStatus, null, 0, null, pComment), "importStatusMessage");
        //messagesSent++;
//        }
    }

    private void sendStatusFailureJobMessage(final long pExecutionId, String pReasonForFailure, final Exception pException) {
//        try {
        LOG.log(Level.SEVERE, "JmsProducer send message. Import failure: {0}", pReasonForFailure);
        final int phase = 0;
        final int maxPhases = 0;
        final int subphase = 0;
        final int maxSubphases = 0;
        sendObjectMessage(pExecutionId, new MessageDTO(phase /* phase */, maxPhases, subphase, maxSubphases, BatchStatus.FAILED, pException, 0, pReasonForFailure, ""), "importStatusMessage");
//        } catch (NumberFormatException | JMSException ex) {
//            LOG.log(Level.SEVERE, "Can not send message", ex);
//        }
    }

    public void sendObjectMessage(final long pExecutionId, MessageDTO messageDTO, String type) {
        producer.sendObjectMessage(pExecutionId, messageDTO, type);
    }

    private String getMonth(final Date pDate) {
        Integer month = extractMonth(pDate);
        if (month == null) {
            return "";
        } else {
            return month + "";
        }
    }

    private String getYear(final Date pDate) {
        Integer year = extractYear(pDate);
        if (year == null) {
            return "";
        } else {
            return year + "";
        }
    }

    private Integer extractMonth(final Date pDate) {
        if (pDate == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(pDate);
        int month = cal.get(Calendar.MONTH) + 1; //0 = January!
        return month;
    }

    private Integer extractYear(final Date pDate) {
        if (pDate == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(pDate);
        int year = cal.get(Calendar.YEAR);
        return year;
    }

    protected String nextSqVal(final boolean isOracle, final String pSequenceName) {
        if (isOracle) {
            return pSequenceName + ".nextval";
        } else {
            return "NEXT VALUE FOR " + pSequenceName;
        }
    }

    private static int dropOldTables(final Statement pStmt, final boolean pIsOracle) throws SQLException {
        //ORA: SELECT table_name FROM user_tables where table_name like 'IMEX_%' and table_name not like '%_TMP';
        String query;
        if (pIsOracle) {
            query = "SELECT table_name name FROM user_tables where table_name like 'P21_EXPORT_%'";
        } else {
            query = "SELECT TABLE_NAME name FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME LIKE 'P21_EXPORT_%'";
        }

        int deletedTables = 0;
        try ( ResultSet rs = pStmt.executeQuery(query)) {
            List<String> tableNames = new ArrayList<>();
            final Pattern pattern = Pattern.compile("^P21\\_EXPORT\\_(\\d{13})(\\_.*)?$");
            while (rs.next()) {
                String tblName = rs.getString("name");
                tblName = (tblName == null) ? "" : tblName.trim().toUpperCase();
                if (!tblName.isEmpty()) {
                    tableNames.add(tblName);
                }
            }
            //final Calendar today = Calendar.getInstance();
            //today.setTimeInMillis(System.currentTimeMillis());
            final long now = System.currentTimeMillis();
            for (String tableName : tableNames) {
                //pQry.execute("DROP VIEW " + tableName);
                Matcher m = pattern.matcher(tableName);
                while (m.find()) {
                    String number = m.group(1);
                    long executionId = 0;
                    try {
                        executionId = Long.valueOf(number);
                    } catch (NumberFormatException ex) {
                        LOG.log(Level.FINEST, "This table has an invalid execution id: " + tableName, ex);
                    }
                    if (executionId > 0) {
                        //Calendar cal = Calendar.getInstance();
                        //cal.setTimeInMillis(executionId);
                        //long diff = today.getTime() - cal.getTime();
                        long diff = now - executionId;
                        long hoursDiff = TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS);
                        if (hoursDiff > 24) {
                            if (dropTable(pStmt, tableName)) {
                                deletedTables++;
                            }
                            dropSequence(pStmt, tableName + "_SQ");
                        }
                    }
                }
            }
            LOG.log(Level.INFO, "Deleted " + deletedTables + " old temporary tables");
        }
        return deletedTables;
    }

    private class P21DateFormat {

        private final DateFormat df;

        public P21DateFormat(String pattern) {
            df = new SimpleDateFormat(pattern);
        }

        public String format(final Object pObject) {
            if (pObject == null) {
                return "";
            }
            return df.format(pObject);
        }

//        public String format(final Date pDate) {
//            if (pDate == null) {
//                return "";
//            }
//            return df.format(pDate);
//        }
    }

    private static String formatLoc(final String pValue) {
        if (pValue == null) {
            return "";
        }
        final String val = pValue.trim();
        if (val.equalsIgnoreCase("E")) {
            return "";
        }
        return val;
    }

    private static String format(final String pValue) {
        if (pValue == null) {
            return "";
        }
        final String val = pValue.trim();
        return val;
    }

    private static String formatAdmod(final String pValue) {
        if (pValue == null) {
            return "";
        }
        final String val = pValue.trim().toUpperCase();
        if (val.startsWith("BA")) {
            return "BA";
        }
        return "HA";
    }

    private static String formatIcdType(final Boolean pValue) {
        if (pValue == null) {
            return "";
        }
        return pValue ? "HD" : "ND";
    }

    private static String formatCaseType(final String pValue) {
        if (pValue == null) {
            return "DRG";
        }
        final String val = pValue.trim();
        if (val.equalsIgnoreCase("PEPP")) {
            return "PSY";
        }
        return val;
    }

//    private static String formatBool(final String pValue) {
//        if (pValue == null) {
//            return "";
//        }
//        final String val = pValue.trim();
//        if (val.equalsIgnoreCase("1")) {
//            return "J";
//        } else {
//            return "N";
//        }
//    }
//    private class P21Localisation {
//        
//        public P21Localisation() {
//            
//        }
//        
//        public String format(final String pValue) {
//            if (pValue == null) {
//                return "";
//            }
//            if (pValue.trim().equalsIgnoreCase("E")) {
//                return "";
//            }
//            return pValue;
//        }
//        
//    }
    private class P21DecimalFormat {

        private final DecimalFormat df;

        public P21DecimalFormat() {
            this(null);
        }

        public P21DecimalFormat(final Integer pDigits) {
            df = new DecimalFormat();
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(',');
            if (pDigits != null) {
                df.setMaximumFractionDigits(pDigits);
                df.setMinimumFractionDigits(pDigits);
            }
            //symbols.setGroupingSeparator(' ');
            df.setGroupingUsed(false);
            df.setDecimalFormatSymbols(symbols);
        }

        public String format(final Object pNumber) {
            if (pNumber == null) {
                return "";
            }
            return df.format(pNumber);
        }

        public String format(final Double pNumber) {
            if (pNumber == null) {
                return "";
            }
            return df.format(pNumber.doubleValue());
        }

        public String format(final Float pNumber) {
            if (pNumber == null) {
                return "";
            }
            return df.format(pNumber.floatValue());
        }

    }

    private static String anonymizeInsuranceIdentifier(final P21ExportSettings pSettings, final String pValue) {
        return anonymize(pSettings.isAnonymizePatient(), "INS_", pValue, 20);
    }

    private static String anonymizeInsuranceNumberPatient(final P21ExportSettings pSettings, final String pValue) {
        return anonymize(pSettings.isAnonymizePatient(), "INSP_", pValue, 20);
    }

    private static String anonymizeFeeKeyInsurance(final P21ExportSettings pSettings, final String pValue) {
        return anonymize(pSettings.isAnonymizePatient(), "IF_", pValue, 12);
    }

    private static String anonymizePatientNumber(final P21ExportSettings pSettings, final String pValue) {
        return anonymize(pSettings.isAnonymizePatient(), "PAT_", pValue, 50);
    }

    private static String anonymizeCaseNumber(final P21ExportSettings pSettings, final String pValue) {
        return anonymize(pSettings.isAnonymizeCase(), "FALL_", pValue, 25);
    }

    private static String anonymizeHospitalIdent(final P21ExportSettings pSettings, final String pValue) {
        return anonymize(pSettings.isAnonymizeHospital(), "KH_", pValue, 10);
    }

    private static String anonymize(final boolean pDecisionFlag, final String pPrefix, final String pValue, final int pLength) {
        final String value;
        if (pDecisionFlag && pValue != null) {
            String val = new StringBuilder(pValue).reverse().toString(); //reverse string
            val = StrUtils.getHash(val); //StrUtils.getHash(pNumber, pLength);
            value = StrUtils.trunc(pPrefix + val, pLength);
        } else {
            value = pValue;
        }
        return format(value);
    }

}
