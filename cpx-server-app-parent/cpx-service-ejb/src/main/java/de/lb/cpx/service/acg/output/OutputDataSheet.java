/*
 * Copyright (c) 2018 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner Health Care Consulting GmbH and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner Health Care Consulting GmbH
 * http://www.lohmann-birkner.de
 *
 * Contributors:
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.acg.output;

import de.lb.cpx.file.reader.CpxFileReader;
import de.lb.cpx.service.acg.catalog.CatalogAcg;
import de.lb.cpx.shared.dto.acg.AcgPatientData;
import de.lb.cpx.shared.dto.acg.AcgPatientDataComparator;
import de.lb.cpx.shared.dto.acg.IcdFarbeOrgan;
import de.lb.cpx.system.properties.CpxSystemProperties;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author niemeier
 */
public class OutputDataSheet implements AutoCloseable {

    private static final Logger LOG = Logger.getLogger(OutputDataSheet.class.getName());
    private final File acgOutputFile;
    private final CatalogAcg catalogAcg;

    public static final String COLUMN_JAHR = "Jahr";
    public static final String COLUMN_DATENJAHR = "Datenjahr";
    public static final String COLUMN_PATIENTENNUMMER = "Patientennummer";
    public static final String COLUMN_GEBURTSDATUM = "Geburtsdatum";
    public static final String COLUMN_ALTER = "Alter";
    public static final String COLUMN_GESCHLECHT = "Geschlecht";
    public static final String COLUMN_ICD_CODE = "ICD Code";
    public static final String COLUMN_CHRONISCHE_ERKRANKUNGEN_PATIENT = "Chronische Erkrankungen Patient";
    public static final String COLUMN_CHRONISCHE_ERKRANKUNGEN_ALTERSGRUPPE = "Chronische Erkrankungen Altersgruppe";
    public static final String COLUMN_ALTERSBEDINGTE_MAKULADEDEGENERATION = "Altersbedingte Makuladegeneration";
    public static final String COLUMN_BIPOLARE_STOERUNG = "Bipolare Störung";
    public static final String COLUMN_HERZINSUFFIZIENZ = "Herzinsuffizienz";
    public static final String COLUMN_DEPRESSION = "Depression";
    public static final String COLUMN_DIABETES = "Diabetes";
    public static final String COLUMN_GLAUKOM = "Glaukom";
    public static final String COLUMN_HIV = "HIV";
    public static final String COLUMN_FETTSTOFFWECHSELSTOERUNG = "Fettstoffwechselstörung";
    public static final String COLUMN_BLUTHOCHDRUCK = "Bluthochdruck";
    public static final String COLUMN_SCHILDDRUESENUNTERFUNKTION = "Schilddrüsenunterfunktion";
    public static final String COLUMN_IMMUNSUPPRESSION = "Immunsuppression";
    public static final String COLUMN_KORONARE_HERZERKRANKUNG = "Koronare Herzerkrankung";
    public static final String COLUMN_OSTEOPOROSE = "Osteoporose";
    public static final String COLUMN_MORBUS_PARKINSON = "Morbus Parkinson";
    public static final String COLUMN_ASTHMA = "Asthma";
    public static final String COLUMN_RHEUMATOIDE_ARTHRITIS = "Rheumatoide Arthritis";
    public static final String COLUMN_SCHIZOPHRENIE = "Schizophrenie";
    public static final String COLUMN_ADIPOSITAS = "Adipositas";
    public static final String COLUMN_COPD = "COPD";
    public static final String COLUMN_NIERENVERSAGEN = "Nierenversagen";
    public static final String COLUMN_RUECKENSCHMERZ = "Rückenschmerz";
    public static final String COLUMN_RELATIVGEWICHT_PATIENT = "Relativgewicht Patient";
    public static final String COLUMN_RELATIVGEWICHT_ALTERSGRUPPE = "Relativgewicht Altersgruppe";
    public static final String COLUMN_RESSOURCNEVERBRAUCHSGRUPPE_PATIENT = "Ressourcenverbrauchsgruppe Patient";
    public static final String COLUMN_RESSOURCNEVERBRAUCHSGRUPPE_ALTERSGRUPPE = "Ressourcenverbrauchsgruppe Altersgruppe";
    public static final String COLUMN_GEBRECHLICHKEIT = "Gebrechlichkeit";

    /**
     * Opens a connection the MS Access DB for ACG mapping information and to
     * the CSV file Beispieldatensatz_Visualisierung.csv
     *
     * @throws IOException ACG files are missing or they are opened in another
     * application
     * @throws SQLException cannot open MS Access Database for ACG mapping data
     */
    public OutputDataSheet() throws IOException, SQLException {
        this(
                CpxSystemProperties.getInstance().getCpxServerAcgOutputFile(),
                CpxSystemProperties.getInstance().getCpxServerCatalogAcgFile()
        );
    }

    /**
     * Opens a connection the MS Access DB for ACG mapping information and to
     * the CSV file Beispieldatensatz_Visualisierung.csv
     *
     * @param pAcgOutputPath use this explicit path to
     * Beispieldatensatz_Visualisierung.csv or another CSV file with this
     * structure
     * @param pDatabasePath use this explicit path to steuerung_acg.mdb or
     * another MS Access DB with this structure
     * @throws IOException ACG files are missing or they are opened in another
     * application
     * @throws SQLException cannot open MS Access Database for ACG mapping data
     */
    public OutputDataSheet(final String pAcgOutputPath, final String pDatabasePath) throws IOException, SQLException {
        final String acgOutputPath = pAcgOutputPath == null ? "" : pAcgOutputPath.trim();
        if (acgOutputPath.isEmpty()) {
            throw new IllegalArgumentException("CSV path is null or empty!");
        }
        final File file = new File(acgOutputPath);
        LOG.log(Level.INFO, "Try to open to this ACG CSV file: " + file.getAbsolutePath());
        checkFile(file);
        acgOutputFile = file;
        catalogAcg = new CatalogAcg(pDatabasePath);
    }

    private void checkFile(final File pAcgOutputFile) {
        if (!pAcgOutputFile.exists()) {
            throw new IllegalArgumentException("This ACG CSV path does not exist: " + pAcgOutputFile.getAbsolutePath());
        }
        if (!pAcgOutputFile.isFile()) {
            throw new IllegalArgumentException("This ACG CSV path is not a file: " + pAcgOutputFile.getAbsolutePath());
        }
        if (!pAcgOutputFile.canRead()) {
            throw new IllegalArgumentException("This ACG CSV file is not readable: " + pAcgOutputFile.getAbsolutePath());
        }
    }

    /**
     * Returns all entries in the Beispieldatensatz_Visualisierung.csv that
     * match to the passed patient insurance number
     *
     * @param pPatientId patient id
     * @return list of rows where the patient insurance number is identical to
     * the parameter
     * @throws IOException ACG file is missing or it is opened in another
     * application
     */
    public List<OutputDataRow> findRowByPatientId(final String pPatientId) throws IOException {
        List<OutputDataRow> resultRows = new ArrayList<>();
        String patientId = pPatientId == null ? "" : pPatientId.trim();
        if (patientId.isEmpty()) {
            throw new IllegalArgumentException("Patient number is null or empty!");
        }
        final Map<String, Integer> mappingColumnNameToIndex;
        final File file = acgOutputFile;
        checkFile(file);
        int lineNumber = 0;
        try (CpxFileReader fileReader = new CpxFileReader(file)) {
            mappingColumnNameToIndex = fileReader.getHeaderMappings();
            Map<Integer, String> mappingIndexToColumnName = new LinkedHashMap<>();
            for (Map.Entry<String, Integer> entry : mappingColumnNameToIndex.entrySet()) {
                mappingIndexToColumnName.put(entry.getValue(), entry.getKey());
            }
            int idxPatientId = mappingColumnNameToIndex.get(CpxFileReader.normalizeKey(COLUMN_PATIENTENNUMMER));

            while (!fileReader.eof()) {
                lineNumber++;
                String[] sa = fileReader.readLineAsArray();
                if (lineNumber == 1) {
                    //Ignore head line
                    continue;
                }
                final String patientIdRow = sa.length - 1 < idxPatientId ? "" : sa[idxPatientId];
                if (patientId.equalsIgnoreCase(patientIdRow)) {
                    Map<String, OutputDataCell> columnResults = new LinkedHashMap<>();
                    for (int columnIndex = 0; columnIndex < sa.length; columnIndex++) {
                        String columnName = mappingIndexToColumnName.get(columnIndex);
                        if (columnName == null) {
                            LOG.log(Level.FINE, "Column header name is empty! I'll ignore this column");
                            continue;
                        }
                        String columnValue = sa[columnIndex];
                        OutputDataCell columnResult = new OutputDataCell(columnIndex, columnName, columnValue);
                        columnResults.put(columnName, columnResult);
                    }
                    //Result[] results = new Result[columnResults.size()];
                    //columnResults.toArray(results);
                    //return new OutputDataRow(columnResults, lineNumber);
                    final OutputDataRow outputDataRow = new OutputDataRow(columnResults, lineNumber);
                    resultRows.add(outputDataRow);
                }
            }
        }
        if (resultRows.isEmpty()) {
            LOG.log(Level.INFO, "No row found with this patient id: " + patientId);
        }
        return resultRows;
    }

    /**
     * Returns information about a patient from
     * Beispieldatensatz_Visualisierung.csv.
     *
     * @param pPatientNr filter list with the help of this patient insurance
     * number
     * @return Each datenjahr has its own entry in the returned map.
     * @throws IOException ACG files are missing or they are opened in another
     * application
     * @throws SQLException cannot open MS Access Database for ACG mapping data
     * @throws Exception error occured when closing resources (JDBC connection
     * to MS Access DB)
     */
    public Map<Integer, AcgPatientData> findByPatientId(final String pPatientNr) throws IOException, SQLException, Exception {
        final boolean fillGaps = true;
        return findByPatientId(pPatientNr, fillGaps);
    }

    /**
     * Returns information about a patient from
     * Beispieldatensatz_Visualisierung.csv.
     *
     * @param pPatientNr filter list with the help of this patient insurance
     * number
     * @param pFillGaps fills gaps when Datenjahre are missing
     * @return Each datenjahr has its own entry in the returned map.
     * @throws IOException ACG files are missing or they are opened in another
     * application
     * @throws SQLException cannot open MS Access Database for ACG mapping data
     * @throws Exception error occured when closing resources (JDBC connection
     * to MS Access DB)
     */
    public Map<Integer, AcgPatientData> findByPatientId(final String pPatientNr, final boolean pFillGaps) throws IOException, SQLException, Exception {
        final int MIN_DATENJAHR = -5;
        final int MAX_DATENJAHR = 0;
        //final Map<Integer, AcgPatientData> acgPatientDataSet = new LinkedHashMap<>();
        //Sort descending by datenjahr
        final Map<Integer, AcgPatientData> acgPatientDataMap = new TreeMap<>(new AcgPatientDataComparator());
        final List<OutputDataRow> outputDataRows = findRowByPatientId(pPatientNr);
        if (outputDataRows.isEmpty()) {
            return acgPatientDataMap;
        }

        if (pFillGaps) {
            for (int jahr = MAX_DATENJAHR; jahr >= MIN_DATENJAHR; jahr--) {
                //if (!acgPatientDataSet.keySet().contains(jahr)) {
                acgPatientDataMap.put(jahr, null);
                //}
            }
        }

        Collections.sort(outputDataRows, new Comparator<OutputDataRow>() {
            @Override
            public int compare(final OutputDataRow lhs, final OutputDataRow rhs) {
                int c;
                c = Integer.compare(lhs.getResultIndex(COLUMN_DATENJAHR), rhs.getResultIndex(COLUMN_DATENJAHR));
                return c;
            }
        });
        final Iterator<OutputDataRow> it = outputDataRows.iterator();
        //StringBuilder sb = new StringBuilder();
        //String datenjahr = "";
        //String datenjahrPrev = "";
        //AcgPatientData acgPatientDataPrev = null;
        //AcgPatientData acgPatientData = null;
        while (it.hasNext()) {
            final OutputDataRow outputDataRow = it.next();
//            datenjahr = outputDataRow.getResultValue(COLUMN_DATENJAHR);
//            boolean neuesDatenjahr = false;
//            if (acgPatientDataPrev != null || (!datenjahrPrev.isEmpty() && !datenjahr.equalsIgnoreCase(datenjahrPrev))) {
//                neuesDatenjahr = true;
//                if (acgPatientData != null) {
//                    acgPatientDataSet.put(acgPatientData.datenjahr, acgPatientData);
//                    icds = new ArrayList<>();
//                }
//            }
//            String icd = outputDataRow.getResultValue(COLUMN_ICD_CODE);
//            List<IcdFarbeOrgan> tmpIcdList = new ArrayList<>();
//            String[] sa = icd.split(" ");
//            for(String s: sa) {
//                if (s == null || s.trim().isEmpty()) {
//                    continue;
//                }
//                IcdFarbeOrgan icdFarbeOrgan = catalogAcg.getIcdFarbeOrganByIcd(s);
//                tmpIcdList.add(icdFarbeOrgan);
//            }
//
//            datenjahrPrev = datenjahr;
//            acgPatientDataPrev = acgPatientData;
            //icds.add(outputDataRow.getResultValue(COLUMN_ICD_CODE));
//            if (!neuesDatenjahr) {
//                icds.addAll(tmpIcdList);
//            }

            Integer datenjahr = AcgPatientData.toInt(outputDataRow.getResultValue(COLUMN_DATENJAHR));
            if (datenjahr == null) {
                LOG.log(Level.SEVERE, "No datenjahr found in line number " + outputDataRow.lineNumber + ", I'll ignore this row in ACG file!");
                continue;
            }
            if (!AcgPatientData.isDatenjahrValidRange(datenjahr)) {
                LOG.log(Level.SEVERE, "Datenjahr in line number " + outputDataRow.lineNumber + " is out of range (found: " + datenjahr + ", I'll ignore this row in ACG file!");
                continue;
            }

            final AcgPatientData acgPatientDataPrev = acgPatientDataMap.get(datenjahr);

            List<IcdFarbeOrgan> icds = new ArrayList<>();
            List<Integer> lineNumbers = new ArrayList<>();
            if (acgPatientDataPrev != null) {
                icds.addAll(acgPatientDataPrev.icd_code);
                lineNumbers.addAll(acgPatientDataPrev.lineNumbers);
            }
            String icd = outputDataRow.getResultValue(COLUMN_ICD_CODE);
            String[] sa = icd.split(" ");
            for (String s : sa) {
                if (s == null || s.trim().isEmpty()) {
                    continue;
                }
                IcdFarbeOrgan icdFarbeOrgan = catalogAcg.getIcdFarbeOrganByIcd(s);
                //tmpIcdList.add(icdFarbeOrgan);
                icds.add(icdFarbeOrgan);
            }
            lineNumbers.add(outputDataRow.lineNumber);
            final int period = 1;
            final String label = "";

            AcgPatientData acgPatientData = new AcgPatientData(
                    lineNumbers,
                    outputDataRow.getResultValue(COLUMN_PATIENTENNUMMER),
                    outputDataRow.getResultValue(COLUMN_JAHR),
                    period + "",
                    outputDataRow.getResultValue(COLUMN_DATENJAHR),
                    label,
                    outputDataRow.getResultValue(COLUMN_GEBURTSDATUM),
                    outputDataRow.getResultValue(COLUMN_ALTER),
                    outputDataRow.getResultValue(COLUMN_GESCHLECHT),
                    icds,
                    outputDataRow.getResultValue(COLUMN_CHRONISCHE_ERKRANKUNGEN_PATIENT),
                    outputDataRow.getResultValue(COLUMN_CHRONISCHE_ERKRANKUNGEN_ALTERSGRUPPE),
                    outputDataRow.getResultValue(COLUMN_ALTERSBEDINGTE_MAKULADEDEGENERATION),
                    outputDataRow.getResultValue(COLUMN_BIPOLARE_STOERUNG),
                    outputDataRow.getResultValue(COLUMN_HERZINSUFFIZIENZ),
                    outputDataRow.getResultValue(COLUMN_DEPRESSION),
                    outputDataRow.getResultValue(COLUMN_DIABETES),
                    outputDataRow.getResultValue(COLUMN_GLAUKOM),
                    outputDataRow.getResultValue(COLUMN_HIV),
                    outputDataRow.getResultValue(COLUMN_FETTSTOFFWECHSELSTOERUNG),
                    outputDataRow.getResultValue(COLUMN_BLUTHOCHDRUCK),
                    outputDataRow.getResultValue(COLUMN_SCHILDDRUESENUNTERFUNKTION),
                    outputDataRow.getResultValue(COLUMN_IMMUNSUPPRESSION),
                    outputDataRow.getResultValue(COLUMN_KORONARE_HERZERKRANKUNG),
                    outputDataRow.getResultValue(COLUMN_OSTEOPOROSE),
                    outputDataRow.getResultValue(COLUMN_MORBUS_PARKINSON),
                    outputDataRow.getResultValue(COLUMN_ASTHMA),
                    outputDataRow.getResultValue(COLUMN_RHEUMATOIDE_ARTHRITIS),
                    outputDataRow.getResultValue(COLUMN_SCHIZOPHRENIE),
                    outputDataRow.getResultValue(COLUMN_ADIPOSITAS),
                    outputDataRow.getResultValue(COLUMN_COPD),
                    outputDataRow.getResultValue(COLUMN_NIERENVERSAGEN),
                    outputDataRow.getResultValue(COLUMN_RUECKENSCHMERZ),
                    outputDataRow.getResultValue(COLUMN_RELATIVGEWICHT_PATIENT),
                    outputDataRow.getResultValue(COLUMN_RELATIVGEWICHT_ALTERSGRUPPE),
                    outputDataRow.getResultValue(COLUMN_RESSOURCNEVERBRAUCHSGRUPPE_PATIENT),
                    outputDataRow.getResultValue(COLUMN_RESSOURCNEVERBRAUCHSGRUPPE_ALTERSGRUPPE),
                    outputDataRow.getResultValue(COLUMN_GEBRECHLICHKEIT)
            );

            acgPatientDataMap.put(acgPatientData.datenjahr, acgPatientData);

//            if (neuesDatenjahr && acgPatientDataPrev != null) {
//                acgPatientDataSet.put(acgPatientDataPrev.datenjahr, acgPatientDataPrev);
//                icds = new ArrayList<>();
//                icds.addAll(tmpIcdList);
//            }
//            if (!acgPatientDataSet.add(acgPatientData)) {
//                LOG.log(Level.WARNING, "This rows was defined multiple times. There is more than 1 row with this patient insurance number and datenjahr");
//            }
        }
//        if (acgPatientData != null) {
//            acgPatientDataSet.put(acgPatientData.datenjahr, acgPatientData);
//        }

        return acgPatientDataMap;
    }

    /**
     * Demonstration Code
     *
     * @param args arguments
     * @throws IOException ACG files are missing or they are opened in another
     * application
     * @throws SQLException cannot open MS Access Database for ACG mapping data
     * @throws Exception error occured when closing resources (JDBC connection
     * to MS Access DB)
     */
    public static void main(String[] args) throws IOException, SQLException, Exception {
        //CpxSystemProperties.setCpxHome("C:\\workspace\\CheckpointX_Repository\\trunk\\WD_CPX_Server\\");
        final String cpxHome = getCpxHome();
        final String acgOutputPath = cpxHome + "\\catalog\\Beispieldatensatz_Visualisierung.csv";
        final String acgDatabasePath = cpxHome + "\\catalog\\steuerung_acg.mdb";

        long startTime = System.currentTimeMillis();
        try (final OutputDataSheet acg = new OutputDataSheet(acgOutputPath, acgDatabasePath)) {
            final String versichertenNummer = "608962PER";
            //        final AcgPatientData acgPatient = acg.findByPatientId(patientId);
            //        LOG.log(Level.INFO, "Patient found for patient id '" + patientId + "': " + String.valueOf(acgPatient));
            final Map<Integer, AcgPatientData> acgPatientData = acg.findByPatientId(versichertenNummer, true);
            LOG.log(Level.INFO, "It took " + (System.currentTimeMillis() - startTime) + " ms to retrieve ACG data for patient number " + versichertenNummer);
            if (acgPatientData.isEmpty()) {
                LOG.log(Level.INFO, "No data found for patient insurance number id '" + versichertenNummer + "'");
            } else {
                int i = 0;
                LOG.log(Level.INFO, "Data found for patient insurance number id '" + versichertenNummer + "': ");
                for (Map.Entry<Integer, AcgPatientData> entry : acgPatientData.entrySet()) {
                    i++;
                    int datenjahr = entry.getKey();
                    AcgPatientData data = entry.getValue();
                    if (data == null) {
                        LOG.log(Level.INFO, "No data available for datenjahr " + datenjahr);
                        continue;
                    }
                    LOG.log(Level.INFO, "#" + i + " in line number(s) " + data.lineNumbers + ": " + String.valueOf(data));
                }
            }
        }
    }

    private static String getCpxHome() {
        final String cpxHome = System.getenv("CPX_HOME");
        final String baseDir;
        if (cpxHome.isEmpty()) {
            LOG.log(Level.WARNING, "THERE'S NO CPX_HOME ON THIS SYSTEMS ENV! I TRY A BEST GUESS STRATEGY TO FIND SERVER DIRECTORY (WD_CPX_SERVER?)");
            Properties props = System.getProperties();
            String userDir = props.getProperty("user.dir");
            int pos = userDir.indexOf("cpx-server-app-parent");
            if (pos >= 0) {
                userDir = userDir.substring(0, pos);
            }
            baseDir = userDir + "WD_CPX_Server";
        } else {
            LOG.log(Level.INFO, "Using CPX_HOME from Systems Environment: " + cpxHome);
            baseDir = cpxHome;
        }
        return baseDir;
    }

    @Override
    public void close() throws Exception {
        if (catalogAcg != null) {
            catalogAcg.close();
        }
    }

}
