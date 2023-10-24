/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package transformer.impl;

import de.lb.cpx.db.container.KisAbteilungContainer;
import de.lb.cpx.db.container.KisInsuranceContainer;
import de.lb.cpx.db.container.KisPatientContainer;
import de.lb.cpx.db.container.TarifeContainer;
import de.lb.cpx.db.container.Vidierstufe;
import de.lb.cpx.db.container.ZusatzContainer;
import de.lb.cpx.model.enums.AdmissionCauseEn;
import de.lb.cpx.model.enums.AdmissionReasonEn;
import de.lb.cpx.model.enums.IcdcTypeEn;
import dto.RmeDiagnose;
import dto.impl.Case;
import dto.impl.Department;
import dto.impl.Diagnose;
import dto.impl.Fee;
import dto.impl.Hauptdiagnose;
import dto.impl.Lab;
import dto.impl.Nebendiagnose;
import dto.impl.Patient;
import dto.impl.Procedure;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import module.Medico;
import module.impl.ImportConfig;

/**
 *
 * @author Dirk Niemeier
 */
public class MedicoToCpxTransformer extends DbToCpxTransformer<Medico, String, String, String, Integer> {

    public static final String KEY_BELEGAEZTLICHE_KOMBINATION = "1112";
    public static final String DEFAULT_SCHEMA = "DPS.";
    protected MathContext mRoundDecimalContext = new MathContext(5, java.math.RoundingMode.HALF_UP);
    private static final Logger LOG = Logger.getLogger(MedicoToCpxTransformer.class.getName());

    private static final String[] ADMISSION_CAUSE = new String[]{
        "01 - Krankenhausbehandlung, vollstationär",
        "02 - Krankenhausbehandlung, vollstationär mit voraus. vorstat. Behandlung",
        "03 - Krankenhausbehandlung teilstationär",
        "04 - Krankenhausbehandlung ohne anschl. stationäre Behandlung",
        "05 - stationäre Entbindung",
        "06 - Geburt",
        "07 - Wiederaufnahme wg. Komplikation (Fallpauschale)",
        "08 - Stationäre Aufnahme zur Organentnahme"
    };

    public MedicoToCpxTransformer(final ImportConfig<Medico> pImportConfig) throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException, InterruptedException, NoSuchAlgorithmException, NoSuchMethodException, InvocationTargetException, SQLException {
        super(pImportConfig);
    }

    @Override
    public TransformResult start() throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException, InterruptedException, NoSuchAlgorithmException, NoSuchMethodException, InvocationTargetException {
        return new TransformResult(patientCounter.get(), caseCounter.get(), exceptions);
    }

    @Override
    public void getUrlaub() {
        //
    }

    @Override
    public void getEntgelte() throws SQLException, IOException {
//        if (!m_importParam.contains(POSSIBLE_ENTGELT)) {
//            return;
//        }
        //"Entgelte in der Medico Datenbank zusammenstellen...");
        executeStatement(getEntgelteQuery(), rs -> {
            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                String fallNr = rs.getString(1);
                String ikKasse = rs.getString(2); //IK Kasse
                String entgeltart = rs.getString(3); //Entgeltart
                Double entgeltbetrag = rs.getDouble(4); //Entgeltbetrag
                Integer entgeltanzahl = rs.getInt(6); //Entgeltanzahl
                Date datumVon = rs.getDate(7); //von Datum
                Date datumBis = rs.getDate(8); //bis Datum
                String ikz = getDefaultHosIdent();
                Fee fee = new Fee(ikz, fallNr);
                getCpxMgr().write(fee);
            }
        });
    }

    @Override
    protected String getEntgelteQuery() {
        String s = getDateWhere() + getAufnahmeArten() + getAufnahmegruendeSQL();
        if (s.length() > 0) {
            s = " AND " + s + getMandant();
        }
        String from = "FROM " + getDbSchema() + "X1100PAT P "
                + "INNER JOIN " + getDbSchema() + "X1211SES  LF ON (P.PAT=LF.PAT) "
                + "INNER JOIN " + getDbSchema() + "X8001DEB  D ON (LF.DEB=D.DEB) "
                + "LEFT OUTER JOIN " + getDbSchema() + "X1131PDR FZF ON (P.PAT=FZF.PAT) ";
        String where = "WHERE LF.NUMSTORNO IS NOT NULL AND LF.SETLOCK=1 " + s;
//        Object o = connection.getSingleSQLResult("SELECT COUNT(*) " + from + where);
//        if (o != null && o instanceof Number) {
//            results = ((Number) o).intValue();
//        }
        String query = "SELECT LF.PAT, d.DEBOFFNO, lf.sermatext, LF.PRTOT, LF.PRSIN, LF.FACTOR, "
                + "LF.SERDF, LF.SERDT "
                + from + where + " ORDER BY P.PAT";
        return query;
    }

    @Override
    protected String getBewegungenQuery() {
        String s = getDateWhere() + getAufnahmeArten() + getAufnahmegruendeSQL();
        if (s.length() > 0) {
            s = " AND " + s;
        }
        String where = "WHERE 1=1 " + s;
        String from = "FROM " + getDbSchema() + "X1100PAT P "
                + "INNER JOIN " + getDbSchema() + "X1102STA V ON (P.PAT=V.PAT) "
                + "LEFT OUTER JOIN " + getDbSchema() + "X1121DUR BB ON (P.PAT=BB.PAT AND BB.DATF BETWEEN V.DATF AND V.DATT AND BB.DEP=V.DEP) "
                + "LEFT OUTER JOIN " + getDbSchema() + "X1131PDR FZF ON (P.PAT=FZF.PAT) ";
        String bewBeat = "SUM(BB.DATT-BB.DATF)*24 BEWBA ";
        if (isIngres()) {
            bewBeat = "0 ";
        }
//            Object o = connection.getSingleSQLResult("SELECT COUNT(*) " + from + where);
//            if (o != null && o instanceof Number) {
//                results = ((Number) o).intValue();
//            }
        return "SELECT P.PAT, V.DATF, V.DATT, V.WDS, V.DEP, V.TRA, V.CHRGDAYS, " + bewBeat + ", MIN(BB.DATF), MAX(BB.DATT) " + from
                + where + " GROUP BY P.PAT, V.DATF, V.DATT, V.WDS, V.DEP, V.TRA, V.CHRGDAYS ORDER BY P.PAT, V.DATF";
    }

    @Override
    public void getBewegungen() throws SQLException, IOException {
        //"Bewegungen in der medico//s Datenbank zusammenstellen...");
        executeStatement(getBewegungenQuery(), rs -> {
            //"Bewegungen aus der medico//s Datenbank lesen...");
            while (rs.next()) {
                String fallNr = rs.getString(1);
                if (fallNr != null) {
                    Case fall = getFall(fallNr);
                    ZusatzContainer<Integer> c = getZusatzContainer(fallNr);
                    if (fall != null) {
                        String art = rs.getString(6);
                        Department b = new Department(fall);
                        b.setVerlegungsdatum(rs.getDate(2));
                        b.setEntlassungsdatum(rs.getDate(3));
                        //b.isDefault = 0;
                        if (art != null) {
                            Integer chrgDays = rs.getInt(7);
                            int days;
                            if (chrgDays != null) {
                                days = chrgDays;
                            } else {
                                days = getVwd(b.getVerlegungsdatum(), b.getEntlassungsdatum());
                            }
                            if (art.equals("U") || art.equals("URB")) {
                                fall.addUrlaubstage(days);
//                                fall.setUrlaubstage(fall.getUrlaubstage() + days);
                                //fall.urlaub += days;
                            }
                            if (art.equals("F") || art.equals("VG")) {
                                fall.addUrlaubstage(days);
//                                fall.setUrlaubstage(fall.getUrlaubstage() + days);
                                //fall.nalos += days;
                            }
                        }
                        //Hinzufügen der Bewegungsbeatmung (nur wenn mit Fallbeatmung übereinstimmt)
                        Integer beatmung = rs.getInt(8);
//                        try {
                        if (beatmung != null) {
                            if (c != null && c.getBeatmungsdauer() > 0 && c.getBewegungsBeatmungsDauer() > 0) {
                                BigDecimal beaDiff = new BigDecimal(c.getBeatmungsdauer() - c.getBewegungsBeatmungsDauer());
                                if (beaDiff.abs().intValue() <= 1) {
                                    Date beatmFrom = rs.getDate(9);
                                    Date beatmTo = rs.getDate(10);
                                    if (beatmFrom != null && beatmTo != null) {
                                        if (beatmTo.after(b.getEntlassungsdatum())) {
                                            beatmTo = b.getEntlassungsdatum();
                                        }
                                        long timediff = beatmTo.getTime() - beatmFrom.getTime();
                                        beaDiff = new BigDecimal(timediff);
                                        BigDecimal hour = new BigDecimal(1000 * 60 * 60);
                                        beaDiff = beaDiff.divide(hour, mRoundDecimalContext);
                                        if (beaDiff.doubleValue() > 0.001) {
                                            //b.addVwdIntensiv(((Number) beatmung).doubleValue());
                                        }
                                    }
                                }
                            }
                        }
//                        } catch (Exception ex1) {
//                            LOG.log(Level.SEVERE, "Fehler bei Berechnung der Bewegungsbeatmungen", ex1);
//                        }
                        //b.abtId = rs.getInt(4);
                        b.setTpId(rs.getString(5));
                        KisAbteilungContainer<String> a = getAbteilung(rs.getString(5)); // MedicoAbteilungContainer) abteilungenHash.get(b.abtId);
                        if (a != null) {
                            //b.abteilung = a.name;
                            //b.abteilung301 = a.p301;
                            b.setCode(a.getP301());
                            b.setCodeIntern(a.getName());
//                            String dep;
//                            if (getProperties().getMedicoAbrech() != null) {
//                                dep = fall.getString3().substring(fall.getString3().indexOf("$") + 1);
//                            } else {
//                                dep = fall.getString3();
//                            }
//                            if (dep != null) {
//                                try {
//                                    int tt = Integer.parseInt(dep.trim());
//                                    if (tt > 0) {
//                                        if (tt <= 2) {
//                                            b.typ = "HA";
//                                            b.typschluessel = (byte) tt;
//                                        } else {
//                                            b.typ = "BA";
//                                            b.typschluessel = (byte) tt;
//                                        }
//                                    }
//                                } catch (NumberFormatException ex) {
//                                    b.typ = "HA";
//                                    b.typschluessel = 1;
//                                }
//                            }
                        }
//                        fall.addBewegung(b);
                    }
                }
            }
        });
    }

    @Override
    public void getMultiFaelle() throws SQLException, IOException {
        //"Multi-Fälle in der medico//s Datenbank zusammenstellen...");
        executeStatement(getNachfolgerQuery(), rs -> {

//            KISFallContainer fall, succ;
//            int count = 0, vwd;
            //"Multi-Fälle zusammenfassen...");
            List<String> removedCase = new ArrayList<>();
            Map<String, String> hmFalnr = new HashMap<>();
            while (rs.next()) {
//                count++;
                String succNr = rs.getString(1);
                String fallNr = rs.getString(2);
                if (fallNr != null && succNr != null) {
//                    if (fallNr.equals("616908")
//                            || fallNr.equals("619174")) {
//                        System.out.println("XX");
//                    }
                    String tempNr = hmFalnr.get(fallNr);
                    if (tempNr != null) {
                        fallNr = tempNr;
                    }
                    Case fall = getFall(fallNr);
                    Case succ = getFall(succNr);
                    hmFalnr.put(succNr, fallNr);
                    if (fall != null && succ != null) {
                        int dauer = getBeatmungsdauer(succNr);
                        if (dauer > 0) {
                            ZusatzContainer<Integer> c = getZusatzContainer(fallNr);
                            if (c != null) {
                                int dauerNeu = c.getBeatmungsdauer() + dauer;
                                setBeatmungsdauer(fallNr, dauerNeu);
                            }
                        }
                        for (Department dep : succ.getDepartments()) {
                            Department newDep = new Department(fall);
                            newDep.set(dep);
                        }
//                        fall.bewegungen.addAll(succ.bewegungen);
                        int vwd = getVwd(fall.getEntlassungsdatum(), succ.getAufnahmedatum());
                        if (vwd > 0 && checkSameDay(fall.getAufnahmedatum(), fall.getEntlassungsdatum())) {
                            vwd = vwd - 1;
                        }
                        if ("06".equals(fall.getEntlassungsgrund12()) || "08".equals(fall.getEntlassungsgrund12())
                                || "12".equals(fall.getEntlassungsgrund12()) || "13".equals(fall.getEntlassungsgrund12())
                                || "17".equals(fall.getEntlassungsgrund12()) || "18".equals(fall.getEntlassungsgrund12())
                                || "19".equals(fall.getEntlassungsgrund12()) || "20".equals(fall.getEntlassungsgrund12())
                                || "21".equals(fall.getEntlassungsgrund12())) {
//                        if (fall.dischargeReason == 6 || fall.dischargeReason == 8 || fall.dischargeReason == 12
//                                || fall.dischargeReason == 13 || fall.dischargeReason == 16 || fall.dischargeReason == 17 || fall.dischargeReason == 18
//                                || fall.dischargeReason == 19 || fall.dischargeReason == 20 || fall.dischargeReason == 21) {
//                            fall.setUrlaubstage(fall.getUrlaubstage() + succ.getUrlaubstage());
                            fall.setUrlaubstage(fall.addUrlaubstage(succ.getUrlaubstage()));
//                            fall.urlaub += succ.urlaub;
//                            fall.nalos += succ.nalos + vwd;
                        } else {
//                            fall.setUrlaubstage(fall.getUrlaubstage() + succ.getVwd());
                            fall.setUrlaubstage(fall.addUrlaubstage(succ.getVwd()));
//                            fall.urlaub += succ.urlaub + vwd;
//                            fall.nalos += succ.nalos;
                        }
                        if (!succ.getEntlassungsgrund12().isEmpty()) {
                            fall.setEntlassungsgrund12(succ.getEntlassungsgrund12());
                        }
                        fall.setEntlassungsdatum(succ.getEntlassungsdatum());
                        //fallMap.remove(succNr);
                        removedCase.add(succNr);
                        addTranslation(succNr, fallNr);
                    }
                }
            }
            for (int i = 0, n = removedCase.size(); i < n; i++) {
                String succNr = removedCase.get(i);
                removeFall(succNr);
            }
            //LUr: Zeile wurde auskommentiert, damit Zeiträume ausserhalb des KH wegen Fallzusammen-
            //führung nur noch über Belegungstage ausserhalb abgebildet werden und nicht noch
            //zusatzlich über Urlaub
//   calculateMulticaseLeaveDays(translationMap.values());
        });
    }

    @Override
    public void getStationen() {
        //
    }

    @Override
    public void getDiagnosen() throws SQLException, IOException {
        //"Diagnosen in der medico//s Datenbank zusammenstellen...");
        executeStatement(getDiagnosenQuery(), rs -> {
//            int i, iGrp, iHd;
            //"Diagnosen einlesen...");
            while (rs.next()) {
                String fallNr = rs.getString(1);
                if (fallNr != null) {
                    Case fall = getFall(fallNr);
                    if (fall == null) {
                        /*if (fallNr.equals("511107") || fallNr.equals("512584") || fallNr.equals("514152"))
       System.out.println("Mergen");*/
                        String succ;
                        while ((succ = getTranslation(fallNr)) != null) {
                            if (succ != null) {
                                fall = getFall(succ);
                            }
                            fallNr = succ;
                        }
                    }
                    if (fall != null) {
                        String md = fall.getString1();
                        String icdCode = rs.getString(2);
                        Boolean hd = rs.getBoolean(3);
                        Boolean group = rs.getBoolean(4);
//                        String depCode = rs.getString(6);
//                        iHd = 0;
                        String diagArt = rs.getString(8);
                        Integer art = getDiagnoseart(diagArt);

                        Department dep = getBewegung(fall.getAufnahmedatum(), fall);
//                        for(Department d: fall.getDepartments()) {
//                            if (d.getTpId().equalsIgnoreCase(depCode)) {
//                                dep = d;
//                                break;
//                            }
//                        }

//                        if (hd != null && hd > 0) {
//                            iHd = 1;
//                        }
//                        iGrp = 0;
//                        if (group != null && group instanceof Number && ((Number) group).intValue() == 0) {
//                            if (m_medico_groupedDiag != null && m_medico_groupedDiag.contains(diagArt)) {
//                                iGrp = 1;
//                            } else {
//                                iGrp = 1;
//                            }
//                        }
                        String lokalisation = getLokalisation(rs.getString(6));   // lokalisation
                        Integer diagID = rs.getInt(9);
                        String ref = rs.getString(10);

                        Diagnose<?> diagnose;
                        if (hd) {
                            diagnose = new Hauptdiagnose(dep);
                        } else {
                            diagnose = new Nebendiagnose(dep);
                            Diagnose<?> refIcd = null;
                            for (Diagnose<?> diag : fall.getDiagnosis()) {
                                if (diag.getCode().equalsIgnoreCase(ref)) {
                                    refIcd = diag;
                                }
                            }
                            ((Nebendiagnose) diagnose).setRefIcd(refIcd);
                        }

                        diagnose.setLokalisation(lokalisation);
                        diagnose.setIcdType(art);
//                        if (diagID != null && md != null && md.equals(diagID.toString())) {
//                            sb.append(1);
//                        } else {
//                            sb.append((md != null && art == 4 && iHd != 0) ? 1 : 0);       // Hauptdiagnose
//                        }
                        //iHd != 0 ? 1 : 0;       // Hauptdiagnose Bewegung
                        //art == 2 ? 0 : iGrp;                   // mit groupen
                        //sb.append(getICDVersion(fall.getAufnahmedatum()));
                        //                        i = fall.getBewegungsID(rs.getInt(6), fall);
                        //                        sb.append(i);                       // bewegungsid
//                        art; // diagnoseart
                        int refType = getDiagZusatzKey(ref);//ref_type
                        diagnose.setRefIcdType(refType);
                        getCpxMgr().write(diagnose);
                    }
                }
            }
        });

        if (getProperties().getMedicoVorNachStatDiag() != null) { // || m_importParam.contains(this.POSSIBLE_SVSN_DIAGPROC)
            executeStatement(getSVSNDiagnosenQuery(), rs -> {

                while (rs.next()) {
                    String fallNr = rs.getString(1);
                    if (fallNr != null) {
                        Case fall = getFall(fallNr);
                        if (fall == null) {
                            /*if (fallNr.equals("511107") || fallNr.equals("512584") || fallNr.equals("514152"))
        System.out.println("Mergen");*/
                            String succ;
                            while ((succ = getTranslation(fallNr)) != null) {
                                if (succ != null) {
                                    fall = getFall(succ);
                                }
                                fallNr = succ;
                            }
                        }
                        if (fall != null) {
                            String icdCode = rs.getString(2);
                            Boolean hd = rs.getBoolean(3);
                            Boolean group = rs.getBoolean(4);
                            String typ = rs.getString(10);
                            String ref = rs.getString(11);
                            boolean iHd = false;
                            Integer art = getDiagnoseart(typ);
                            boolean iGrp = false;
                            String lokalisation = getLokalisation(rs.getString(6)); // lokalisation
                            //sb.append(getICDVersion(fall.getAufnahmedatum(), rs.getString(4)));
                            Department dep;
                            if (typ != null && typ.equals("SV")) {
                                dep = getBewegung(fall.getAufnahmedatum(), fall);
//                                i = fall.getBewegungsId(fall.getAufnahmedatum(), fall);
                            } else {
                                dep = getBewegung(fall.getEntlassungsdatum(), fall);
//                                i = fall.getBewegungsId(fall.getEntlassungsdatum(), fall);
                            }
                            int zusatKey = getDiagZusatzKey(ref); //ref_type

                            Diagnose<?> diagnose;
                            if (hd) {
                                diagnose = new Hauptdiagnose(dep);
                            } else {
                                diagnose = new Nebendiagnose(dep);
                                Diagnose<?> refIcd = null;
                                for (Diagnose<?> diag : fall.getDiagnosis()) {
                                    if (diag.getCode().equalsIgnoreCase(ref)) {
                                        refIcd = diag;
                                    }
                                }
                                ((Nebendiagnose) diagnose).setRefIcd(refIcd);
                            }
                            diagnose.setIcdType(art);

                            getCpxMgr().write(diagnose);
                        }
                    }
                }
            });
        }
    }

    @Override
    protected String getDiagnosenQuery() {
        String s = getDateWhere() + getAufnahmeArten() + getAufnahmegruendeSQL() + getDiagnoseartenWhereSQL();
        if (s.length() > 0) {
            s = " AND " + s + getMandant();
        }
        String where = "WHERE 1=1 " + s;
        String from = "FROM " + getDbSchema() + "X1100PAT P "
                + "INNER JOIN " + getDbSchema() + "X1280DIA D ON (P.PAT=D.PAT) "
                + "LEFT OUTER JOIN " + getDbSchema() + "X1131PDR FZF ON (P.PAT=FZF.PAT) ";
//            Object o = connection.getSingleSQLResult("SELECT COUNT(*) " + from + where);
//            if (o != null && o instanceof Number) {
//                results = ((Number) o).intValue();
//            }
        String query = "SELECT P.PAT, D.DDC, D.MAINDRG, D.DIALOCKED, D.DCAOFF, D.DIAGLOCOFF, D.DEP, D.DIT, D.DIA, D.KZKRST "
                + from + where + " ORDER BY P.PAT, D.DIT, D.POSNO";
//   System.out.println(query);
        return query;
    }

    @Override
    public void getProzeduren() throws SQLException, IOException {
        //"Prozeduren in der medico//s Datenbank zusammenstellen...");#
        executeStatement(getProzedurenQuery(), rs -> {
            //"Prozeduren einlesen...");
            while (rs.next()) {
                String fallNr = rs.getString(1);
                if (fallNr != null) {
                    Case fall = getFall(fallNr);
                    if (fall == null) {
                        /*if (fallNr.equals("511107") || fallNr.equals("512584") || fallNr.equals("514152"))
       System.out.println("Mergen");*/
                        String succ;
                        while ((succ = getTranslation(fallNr)) != null) {
                            if (succ != null) {
                                fall = getFall(succ);
                            }
                            fallNr = succ;
                        }
                    }
                    if (fall != null) {
                        String opsCode = rs.getString(2);
                        Boolean group = rs.getBoolean(3);
                        String version = rs.getString(4);
                        Date datum = rs.getDate(7);
//                        typschl = fall.getBewegungsTypschl(datum, fall);
//                        if (group != null && group instanceof Number && ((Number) group).intValue() == 0) {
//                            iGrp = 1;
//                        }
                        boolean iGrp = true;
                        Department dep = getBewegung(datum, fall);
//                        int typschl = fall.getBewegungsTypschl(datum, fall);
//                        if (typschl >= 3) {
//                            sb.append('1'); // belegoperateur
//                        } else {
//                            sb.append('0'); // belegoperateur
//                        }
//                        if (typschl == 4 || typschl == 6) {
//                            sb.append('1'); // beleganaesthesist
//                        } else {
//                            sb.append('0'); // beleganaesthesist
//                        }
//                        if (typschl == 2 || typschl == 6) {
//                            sb.append('1'); // beleghebamme
//                        } else {
//                            sb.append('0'); // beleghebamme
//                        }
//                        sb.append(getDatumZeit(datum)); // datum
                        //sb.append(getOPSVersion(fall.getAufnahmedatum())); // opsversion
//                        if ("635812".equals(fallNr)) {
//                            System.out.println("XX");
//                        }
                        int beatmungsdauer = getBeatmungsdauer(fallNr);         // beatmungsstunden
//                        i = fall.getBewegungsId(datum, fall);
                        String lokalisation = MedicoToCpxTransformer.getLokalisation(rs.getString(5));                               // lokalisation
//                        sb.append(getOfficialCode(opsCode, datum, getOPSVersion(fall.getAufnahmedatum())));

                        Procedure proc = new Procedure(dep);
                        proc.setDatum(datum);
                        proc.setCode(opsCode);
                        proc.setLokalisation(lokalisation);
                        proc.setToGroup(iGrp);

                        getCpxMgr().write(proc);
                    }
                }
            }
        });

        if (getProperties().getMedicoVorNachStatDiag() != null) { // || m_importParam.contains(this.POSSIBLE_SVSN_DIAGPROC)
            executeStatement(getSVSNProzedurenQuery(), rs -> {
                while (rs.next()) {
                    String fallNr = rs.getString(1);
                    if (fallNr != null) {
                        Case fall = getFall(fallNr);
                        if (fall == null) {
                            /*if (fallNr.equals("511107") || fallNr.equals("512584") || fallNr.equals("514152"))
        System.out.println("Mergen");*/
                            String succ;
                            while ((succ = getTranslation(fallNr)) != null) {
                                if (succ != null) {
                                    fall = getFall(succ);
                                }
                                fallNr = succ;
                            }
                        }
                        if (fall != null) {
                            String opsCode = rs.getString(2);
                            Boolean group = rs.getBoolean(3);
                            String version = rs.getString(4);
                            Date datum = rs.getDate(7);
                            String typ = rs.getString(8);
                            //Boolean iGrp = false;
                            //if(group != null && group instanceof Number && ((Number)group).intValue() == 0)
                            // iGrp = 1;
//                            sb.append(getDatumZeit(datum)); // datum
                            //sb.append(getOPSVersion(fall.getAufnahmedatum(), version)); // opsversion
                            Department dep;
                            if (typ != null && typ.equals("SV")) {
                                dep = getBewegung(fall.getAufnahmedatum(), fall);
                            } else {
                                dep = getBewegung(fall.getEntlassungsdatum(), fall);
                            }
                            String lokalisation = getLokalisation(rs.getString(5)); // lokalisation
//                            sb.append(getOfficialCode(opsCode, datum, getOPSVersion(fall.getAufnahmedatum())));
                            Procedure proc = new Procedure(dep);
                            proc.setDatum(datum);
                            proc.setCode(opsCode);
                            proc.setLokalisation(lokalisation);
                            proc.setToGroup(group);

                            getCpxMgr().write(proc);
                        }
                    }
                }
            });
        }
    }

    @Override
    protected String getProzedurenQuery() {
        String s = getDateWhere() + getAufnahmeArten() + getAufnahmegruendeSQL();
        if (s.length() > 0) {
            s = " AND " + s + getMandant();
        }
        //String where = "WHERE D.DIT='OP'" + s;
        //CP-136 Erweiterung um PROZ
        String where = "WHERE D.DIT='OP' OR D.DIT='PROZ' " + s;
        String from = "FROM " + getDbSchema() + "X1100PAT P "
                + "INNER JOIN " + getDbSchema() + "X1280DIA D ON (P.PAT=D.PAT) "
                + "LEFT OUTER JOIN " + getDbSchema() + "X1131PDR FZF ON (P.PAT=FZF.PAT) ";
//            Object o = connection.getSingleSQLResult("SELECT COUNT(*) " + from + where);
//            if (o != null && o instanceof Number) {
//                results = ((Number) o).intValue();
//            }
        return "SELECT P.PAT, D.DDC, D.DIALOCKED, D.DCAOFF, D.DIAGLOCOFF, D.DEP, D.OPDAT "
                + from + where + " ORDER BY P.PAT, D.POSNO";
    }

    private int loadDrgStatus() throws SQLException, IOException {
        int results = 0;
        String s = getDateWhere() + getAufnahmeArten() + getAufnahmegruendeSQL();
        if (s.length() > 0) {
            s = " AND " + s + getMandant();
        }
        String where = "WHERE S.MKPRED=0 " + s;
        String from = " FROM " + getDbSchema() + "X1100PAT P "
                + "INNER JOIN " + getDbSchema() + "X1131PDR S ON(P.PAT=S.PAT) "
                + "INNER JOIN " + getDbSchema() + "X1132DRG D ON(S.DRG=D.DRG) "
                + "INNER JOIN " + getDbSchema() + "X1926DRG DS ON(S.DRG=DS.DRG) "
                + "LEFT OUTER JOIN " + getDbSchema() + "X1131PDR FZF ON (P.PAT=FZF.PAT) ";
//        Object o = connection.getSingleSQLResult("SELECT COUNT(*) " + from + where);
//        if (o != null && o instanceof Number) {
//            results = ((Number) o).intValue();
//        }
        final String query = "SELECT P.PAT, D.STATUS, D.maindrgdia, DS.mkprovide " + from + where;
        executeStatement(query, rs -> {
            while (rs.next()) {
                String pnr = rs.getString(1);
                Integer status = rs.getInt(2);
                String beleg = rs.getString(4);
                if (pnr != null) {
                    Case fall = getFall(pnr);
                    if (fall != null) {
                        fall.setFallstatus(mapFallstatus(status));
                        String status2 = rs.getString(3);
                        if (status2 != null) {
                            fall.setString2(status2);
                        }
                        if (getProperties().getMedicoAbrech() != null) {
                            fall.setString3(fall.getString3() + "$" + beleg);
                        } else {
                            fall.setString3(beleg);
                        }
                    }
                }
            }
        });
        return results;
    }

//    protected void checkErbringungsArt(List<Department> bewegungen) {
//        for (int i = 0, n = bewegungen.size(); i < n; i++) {
//            Department b = bewegungen.get(i);
//            if (b != null && b.behandelnde != 0) {
//                MedicoAbteilungContainer a = (MedicoAbteilungContainer) abteilungenHash.get(b.abtId);
//                if (a != null && a.belegart != 0) {
//                    b.typ = "BA";
//                    b.typschluessel = a.typschluessel;
//                }
//            }
//        }
//    }
    @Override
    public void getLabordaten() throws SQLException, IOException {
//        if (!m_importParam.contains(this.POSSIBLE_LABOR)) {
//            return true;
//        }
//        KISFallContainer fall;
        //"Labordaten aus der Medico Datenbank zusammenstellen..."
        executeStatement(getLabordatenQuery(), rs -> {
            //String lastLine = null, currLine = null, lastUploadedLine = "", lastFallLaborWert = "", currFallLaborWert = "";
            while (rs.next()) {
                String fallNr = rs.getString(1);
                Case fall = getFall(fallNr);
                if (fall == null) {
                    fallNr = getTranslation(fallNr);
                    if (fallNr != null) {
                        fall = getFall(fallNr);
                    }
                }
                if (fall != null) {
                    String o = rs.getString(2);
                    String textwert = "";
                    if (o != null) {
                        Float val = null;
                        o = o.replaceAll(",", ".");
                        try {
                            val = Float.parseFloat(o);
                        } catch (NumberFormatException ex) {
                            LOG.log(Level.WARNING, "Cannot parse '" + o + "' as float", ex);
                            textwert = o + " - ";
                        }
//                        if (val != null) {
//                            sb.append(val.toString()); //Wert1
//                        }
                    }
                    Double wert2 = rs.getDouble(3);
                    Date datum = rs.getDate(4); //Datum
//                    if (o != null) {
//                        sb.append(getDatumZeit(o));
//                    }
                    String bereich = rs.getString(5); //Bereich
                    //if(o != null)
                    //sb.append("'" +row.get(4)+"'"); //Bereich
                    String bewertung = rs.getString(6); //Bewertung
//                    if (o != null) {
//                        sb.append(rs.getString(5)); //Bewertung
//                    }
                    Double untergrenze = rs.getDouble(7); //Untergrenze
//                    if (o != null) {
//                        sb.append(o.toString());
//                    }
//                    sb.append(';');
                    Double obergrenze = rs.getDouble(8); //Obergrenze
//                    if (o != null) {
//                        sb.append(o.toString());
//                    }
                    textwert += rs.getString(9);
//                    if (o != null) {
//                        textwert = o.toString();
//                        sb.append(textwert); //Textwert
//                    }
                    String kommentar = rs.getString(10); //Kommentar
//                    if (o != null) {
//                        String tt = o.toString();
//                        tt = tt.replace(";", ",");
//                        sb.append(tt); //Kommentar
//                    }
                    Date analyseDatum = rs.getDate(11); //Analyse-Datum
//                    if (o != null) {
//                        sb.append(getDatum(o));
//                    }
                    String analyse = rs.getString(13); //Analyse
//                    if (o != null) {
//                        sb.append(o); //Analyse
//                    }
                    String bezeichnung = rs.getString(14); //Bezeichnung
//                    if (o != null) {
//                        bezeichnung = o.toString();
//                        if (bezeichnung.contains(";")) {
//                            bezeichnung = bezeichnung.replace(";", ",");
//                        }
//                        sb.append(o.toString()); //Bezeichnung
//                    }
                    String einheit = rs.getString(15); //Einheit
//                    if (o != null) {
//                        sb.append(o); //Einheit
//                    }
                    String methode = rs.getString(16); //Methode
//                    if (o != null) {
//                        methode = o.toString();
//                        if (methode.length() > 40) {
//                            methode = methode.substring(0, 40);
//                        }
//                        if (methode.contains(";")) {
//                            methode = methode.replace(";", ",");
//                        }
//                        sb.append(methode); //Methode
//                    }
                    Integer kategorie = rs.getInt(17); //Kategorie
//                    if (o != null) {
//                        kategorie = o.toString();
//                        if (kategorie.contains(";")) {
//                            kategorie = kategorie.replace(";", ",");
//                        }
//                        sb.append(kategorie); //Kategorie
//                    }
                    String gruppe = rs.getString(18); //Gruppe
//                    if (o != null) {
//                        sb.append(o); //Gruppe
//                    }
//                    boolean uploaded = false;
//                    if (uploadable) {
//                        currFallLaborWert = fallNr + bezeichnung;
//                    }
                    Lab lab = new Lab(fall);
                    lab.setAnalysis(analyse);
                    lab.setAnalysisDate(analyseDatum);
                    lab.setMethod(methode);
                    lab.setGroup(gruppe);
                    lab.setCategory(kategorie);
                    lab.setUnit(einheit);
                    lab.setText(textwert);
                    lab.setComment(kommentar);
                    lab.setDescription(bezeichnung);
                    lab.setMinLimit(untergrenze);
                    lab.setMaxLimit(obergrenze);
                    lab.setRange(bereich);
                    lab.setBenchmark(bewertung);
//                    lab.setValue(wert);
                    lab.setValue2(wert2);

                    getCpxMgr().write(lab);
                }
            }
        });
    }

    @Override
    protected String getLabordatenQuery() {
        String s = getDateWhere() + getAufnahmeArten() + getAufnahmegruendeSQL();
        if (s.length() > 0) {
            s = " AND " + s + getMandant();
        }
        String from = "FROM " + getDbSchema() + "X1100PAT P "
                + "INNER JOIN " + getDbSchema() + "X1580RPS l on (l.PAT=P.PAT) "
                + "INNER JOIN " + getDbSchema() + "X6500tar lc on (l.id=lc.id) "
                + "INNER JOIN " + getDbSchema() + "w6508lid lid on (l.id=lid.id) "
                + "INNER JOIN " + getDbSchema() + "w6507lgr lgr on (lid.lgr=lgr.lgr) "
                + "LEFT OUTER JOIN " + getDbSchema() + "X1583rpt lt on(l.res=lt.res AND l.id=lt.id) "
                + "LEFT OUTER JOIN " + getDbSchema() + "X1131PDR FZF ON (P.PAT=FZF.PAT) ";
        String where = "WHERE l.val is not null " + s;
//        Object o = connection.getSingleSQLResult("SELECT COUNT(*) " + from + where);
//        if (o != null && o instanceof Number) {
//            results = ((Number) o).intValue();
//        }
        return "SELECT P.PAT "
                + ", l.val "
                + //Wert
                ", NULL "
                + //kein 2. Wert enthalten
                ", l.crd "
                + //Datum
                ", l.rpsrange "
                + //Normwerte
                ", l.flags "
                + //Bewertung (zu hoch, zu niedrig)
                ", NULL "
                + ", NULL "
                + ", '' "
                + ", '' "
                + ", NULL "
                +//10
                ", NULL "
                + ", '' "
                + ", lc.des "
                + ", l.uni "
                + ", lt.text  "
                + ", 0 "
                + ", lgr.des  "
                +//17
                from + where + " ORDER BY P.PAT, LC.DES, L.VAL ";
    }

    @Override
    public void getDrg() {
        //
    }

    @Override
    public Map<Integer, Integer> loadDrgBeleg() throws SQLException {
        return new HashMap<>();
    }

    @Override
    protected Map<String, String> loadAufnahmegrund12() throws SQLException, IOException {
        Map<String, String> result = new HashMap<>();
        executeStatement(getAufnahmeGruende1SQL(), rs -> {
            while (rs.next()) {
                String a = rs.getString(1);
                if (a != null) {
                    String admissionsCause = textForAdmissionCause1(a);
                    result.put(a, admissionsCause);
                }
            }
        });
        return result;
    }

    @Override
    protected Map<String, String> loadAufnahmegrund34() throws SQLException, IOException {
        Map<String, String> result = new HashMap<>();
        executeStatement(getAufnahmeGruende2SQL(), rs -> {
            while (rs.next()) {
                String a = rs.getString(1);
                if (a != null) {
                    //String admissionsCause = textForAdmissionCause1(a.toString());
                    result.put(a, "");
                }
            }
        });
        return result;
    }

    @Override
    protected Map<String, String> loadEntlassungsgrund12() throws SQLException, IOException {
        Map<String, String> result = new HashMap<>();
        executeStatement(getEntlassungsGruende1SQL(), rs -> {
            while (rs.next()) {
                String a = rs.getString(1);
                if (a != null) {
                    //String admissionsCause = textForAdmissionCause1(a.toString());
                    result.put(a, "");
                }
            }
        });
        return result;
    }

    @Override
    protected Map<String, String> loadEntlassungsgrund3() throws SQLException, IOException {
        Map<String, String> result = new HashMap<>();
        executeStatement(getEntlassungsGruende2SQL(), rs -> {
            while (rs.next()) {
                String a = rs.getString(1);
                if (a != null) {
                    //String admissionsCause = textForAdmissionCause1(a.toString());
                    result.put(a, "");
                }
            }
        });
        return result;
    }

    @Override
    protected Map<String, String> loadAufnahmeanlass() throws SQLException, IOException {
        Map<String, String> result = new HashMap<>();
        executeStatement(getAufnahmeArtenSQL(), rs -> {
            while (rs.next()) {
                String a = rs.getString(1);
                if (a != null) {
                    final String admissionCause;
                    if (a.equalsIgnoreCase("A")) { //txt.admission.type.1
                        admissionCause = "A - Ambulant"; //txt.admission.type.1.long
                    } else if (a.equalsIgnoreCase("S")) { //txt.admission.type.3
                        admissionCause = "S - Stationär"; //txt.admission.type.3.long
                    } else if (a.equalsIgnoreCase("SN")) { //txt.admission.type.6
                        admissionCause = "SN - Nachstationnär"; //txt.admission.type.6.long
                    } else if (a.equalsIgnoreCase("SV")) { //txt.admission.type.7
                        admissionCause = "SV - Vorstationnär"; //txt.admission.type.7.long
                    } else if (a.equalsIgnoreCase("ST")) { //txt.admission.type.8
                        admissionCause = "ST - Teilstationnär"; //txt.admission.type.8.long
                    } else if (a.equalsIgnoreCase("U")) { //txt.admission.type.9
                        admissionCause = "U - Unbekannt ?"; //txt.admission.type.9.long
                    } else {
                        admissionCause = a + " - Unbekannt ?";  // + AppResources.getResource(AppResourceBundle.CHECKRESULT_TYP_UNKNOW));
                    }
                    result.put(a, admissionCause);
                }
            }
        });
        return result;
    }

    @Override
    protected Map<String, Integer> loadDiagnosearten() throws SQLException, IOException {
        Map<String, Integer> result = new HashMap<>();
        result.put("AUF", 2);
        result.put("ARB", 12);
        result.put("EIN", 1);
        result.put("POP", 7);
        result.put("PRP", 8);
        result.put("RIS", 9);
        result.put("PFL", 10);
        result.put("ENT", 4);
        result.put("ENTA", 11);
        result.put("SV", 17);
        result.put("SN", 18);
        return result;

//        Map<String, String> result = new HashMap<>();
//        executeStatement(getDiagnoseArtenSQL(), rs -> {
//            while (rs.next()) {
//                String a = rs.getString(0);
//                if (a != null) { // && !a.equals(AppResources.getResource(AppResourceBundle.CASE_OP))) {  // OP Diagnosen sind Prozeduren !
//                    final String diagnoseArt;
//                    /*
//       if(a.equals(AppResources.getResource(AppResourceBundle.TXT_DIAG_TYPE_7)))
//        row.add(AppResources.getResource(AppResourceBundle.TXT_DIAG_TYPE_7_LONG));
//       else if(a.equals(AppResources.getResource(AppResourceBundle.TXT_DIAG_TYPE_8)))
//        row.add(AppResources.getResource(AppResourceBundle.TXT_DIAG_TYPE_8_LONG));
//       else if(a.equals(AppResources.getResource(AppResourceBundle.TXT_DIAG_TYPE_9)))
//        row.add(AppResources.getResource(AppResourceBundle.TXT_DIAG_TYPE_9_LONG));
//       else if(a.equals(AppResources.getResource(AppResourceBundle.TXT_DIAG_TYPE_10)))
//        row.add(AppResources.getResource(AppResourceBundle.TXT_DIAG_TYPE_10_LONG));
//       else if(a.equals(AppResources.getResource(AppResourceBundle.TXT_DIAG_TYPE_11)))
//        row.add(AppResources.getResource(AppResourceBundle.TXT_DIAG_TYPE_11_LONG));
//       else if(a.equals(AppResources.getResource(AppResourceBundle.TXT_DIAG_TYPE_12)))
//        row.add(AppResources.getResource(AppResourceBundle.TXT_DIAG_TYPE_12_LONG));
//       else if(a.equals(AppResources.getResource(AppResourceBundle.TXT_DIAG_TYPE_13)))
//        row.add(AppResources.getResource(AppResourceBundle.TXT_DIAG_TYPE_13_LONG));
//       else if(a.equals(AppResources.getResource(AppResourceBundle.TXT_DIAG_TYPE_14)))
//        row.add(AppResources.getResource(AppResourceBundle.TXT_DIAG_TYPE_14_LONG));
//       else if(a.equals(AppResources.getResource(AppResourceBundle.TXT_DIAG_TYPE_15)))
//        row.add(AppResources.getResource(AppResourceBundle.TXT_DIAG_TYPE_15_LONG));
//                     */
//                    if (a.equalsIgnoreCase("AUF")) { //txt.diag.type.7
//                        diagnoseArt = "AUF - Aufnahmediagnose"; //txt.diag.type.7.long
//                    } else if (a.equalsIgnoreCase("ARB")) { //txt.diag.type.8
//                        diagnoseArt = "ARB - Arbeitsdiagnose"; //txt.diag.type.8.long
//                    } else if (a.equalsIgnoreCase("EIN")) { //txt.diag.type.9
//                        diagnoseArt = "EIN - Einweisungsdiagnose"; //txt.diag.type.9.long
//                    } else if (a.equalsIgnoreCase("POP")) { //txt.diag.type.10
//                        diagnoseArt = "POP - Postoperative Diagnose"; //txt.diag.type.10.long
//                    } else if (a.equalsIgnoreCase("PRP")) { //txt.diag.type.11
//                        diagnoseArt = "PRP - Präoperative Diagnose"; //txt.diag.type.11.long
//                    } else if (a.equalsIgnoreCase("RIS")) { //txt.diag.type.12
//                        diagnoseArt = "RIS - Risikofaktoren"; //txt.diag.type.12.long
//                    } else if (a.equalsIgnoreCase("PFL")) { //txt.diag.type.13
//                        diagnoseArt = "PFL - Pflegediagnose"; //txt.diag.type.13.long
//                    } else if (a.equalsIgnoreCase("ENT")) { //txt.diag.type.14
//                        diagnoseArt = "ENT - Entlassungsdiagnose"; //txt.diag.type.14.long
//                    } else if (a.equalsIgnoreCase("ENTA")) { //txt.diag.type.15
//                        diagnoseArt = "ENTA - Entlassungsdiagnose (ambulant)"; //txt.diag.type.15.long
//                    } else {
//                        diagnoseArt = a + " - Unbekannt ?";  // + AppResources.getResource(AppResourceBundle.CHECKRESULT_TYP_UNKNOW));
//                    }
//                    result.put(a, diagnoseArt);
//                }
//            }
//        });
//        return result;
    }

    @Override
    public Map<String, KisPatientContainer> loadPatienten() throws IOException, SQLException {
        final Map<String, KisPatientContainer> result = new HashMap<>();
        String s = getDateWhere() + getAufnahmeArten() + getAufnahmegruendeSQL();
        if (s.length() > 0) {
            s = " AND " + s;
        }

        final String query = "SELECT D.NAME || D.NAME2, D.DEBOFFNO, C.PAT, C.INSNO FROM " + getDbSchema() + "X8001DEB D "
                + "INNER JOIN " + getDbSchema() + "X1150COG C ON (C.DEB=D.DEB) "
                + "INNER JOIN " + getDbSchema() + "X1100PAT P ON (P.PAT=C.PAT) "
                + "LEFT OUTER JOIN " + getDbSchema() + "X1131PDR FZF ON (P.PAT=FZF.PAT) "
                + "WHERE D.DEBOFFNO IS NOT NULL " + s;
        executeStatement(query, rs -> {
            while (rs.next()) {
                String deb = rs.getString(1);
                String iknr = rs.getString(2);
                if (iknr != null) {
                    String ikz = iknr.trim();
                    if (ikz.length() == 9 && (ikz.startsWith("10") || ikz.startsWith("12") || ikz.startsWith("5"))) {
                        deb = ikz;
                    }
                }
                String pat = rs.getString(3);
                String versnr = rs.getString(4);
                if (pat != null && deb != null) {
//                debMap.put(pat, deb);
//                if (versnr != null) {
//                    versNrMap.put(pat, versnr);
//                }
                    KisPatientContainer p = new KisPatientContainer(pat, versnr, deb);
                    result.put(pat, p);
                }
            }
        });
        return result;
    }

    @Override
    public void getPatienten() throws SQLException, IOException {
        //"Patientendaten in der medico//s DB zusammenstellen...");
        executeStatement(getPatientenQuery(), rs -> {
            StringBuilder sb = new StringBuilder();

            Map<String, String> htPats = new HashMap<>();
            while (rs.next()) {
                String fallNr = rs.getString(6);
                String persnr = rs.getString(1);
                if (fallNr != null) {
                    Case fall = getFall(fallNr);
                    if (fall != null) {
                        String oldPersID = htPats.get(persnr);
                        if (oldPersID == null) {
                            String patNr = rs.getString(1);
                            String name = rs.getString(2);
                            String vorname = rs.getString(3);
                            Date geburtsdatum = rs.getDate(4);
                            char geschlecht = getGeschlecht(rs.getString(5));
                            String versNr = getPatientVersNr(fallNr); //why fallNr instead of patientNr?
                            if (versNr != null) {
                                sb.append(versNr);
                            }

                            Patient pat = new Patient();
                            pat.setPatNr(patNr);
                            pat.setNachname(name);
                            pat.setVorname(vorname);
                            pat.setGeburtsdatum(geburtsdatum);
                            pat.setGeschlecht(geschlecht);
                            pat.setVersichertennr(versNr);

                            getCpxMgr().write(pat);

                            htPats.put(persnr, pat.getPatNr());
                        }
//                        else {
//                            String patID = oldPersID;
////                            fall.patientid = patID; //Patienten-ID
//                        }
                    }
                }
            }
            //"Patientendaten in der medico//s DB zusammengestellt");
        });
    }

    @Override
    protected String getPatientenQuery() {
        String s = getDateWhere() + getAufnahmeArten() + getAufnahmegruendeSQL();
        if (s.length() > 0) {
            s = " AND " + s + getMandant();
        }
        String where = "WHERE P.ADMERRORD IS NULL AND P.RESERVMK=0 " + s;
        String from = "FROM " + getDbSchema() + "X1100PAT P "
                + "INNER JOIN " + getDbSchema() + "X1000PER PER ON (P.PER=PER.PER) "
                + "LEFT OUTER JOIN " + getDbSchema() + "X1131PDR FZF ON (P.PAT=FZF.PAT) ";
        String fromWhere = from + where;
//            Object o = connection.getSingleSQLResult("SELECT COUNT(*) " + fromWhere);
//            if (o != null && o instanceof Number) {
//                results = ((Number) o).intValue();
//            }
        return "SELECT DISTINCT PER.PER, PER.NAME, PER.CHR, PER.BIRTHD, PER.SEX, P.PAT ";
    }

    @Override
    protected Map<Integer, String> loadEinzugsgebiet() throws SQLException {
        return new HashMap<>();
    }

    @Override
    protected Map<String, ZusatzContainer<Integer>> loadZusatzContainer() throws SQLException, IOException {
        String s = getDateWhere()
                + getAufnahmeArten() + getAufnahmegruendeSQL();
        String bewBeat = "SUM(BB.DATT-BB.DATF)*24 BEWBA ";
        if (isIngres()) {
            bewBeat = "Z.DURRESP ";
        }
        if (s.length() > 0) {
            s = s + getMandant();
        }
        final Map<String, ZusatzContainer<Integer>> zusatzHash = new HashMap<>();
        String where = s;
        final String query = "SELECT P.PAT, Z.WADM, Z.DURRESP, " + bewBeat
                + "FROM " + getDbSchema() + "X1100PAT P "
                + "LEFT OUTER JOIN " + getDbSchema() + "X1120PAD Z ON (P.PAT=Z.PAT) "
                + "LEFT OUTER JOIN " + getDbSchema() + "X1131PDR FZF ON (P.PAT=FZF.PAT) "
                + "LEFT OUTER JOIN  " + getDbSchema() + "X1121DUR BB ON (P.PAT=BB.PAT) "
                + "WHERE " + where + " GROUP BY P.PAT, Z.WADM, Z.DURRESP";
        executeStatement(query, rs -> {

            while (rs.next()) {
                String pnr = rs.getString(1);
                Integer admW = rs.getInt(2);
                Integer respD = rs.getInt(3);
                Integer respDBew = rs.getInt(4);
                if (pnr != null) {
                    /*if (pnr.equals("635812"))
      System.out.println("XX");*/
                    ZusatzContainer<Integer> c = zusatzHash.get(pnr);
                    if (c == null) {
                        c = new ZusatzContainer<>();
                        zusatzHash.put(pnr, c);
                    }
                    c.setGeburtsgewicht(admW);
                    c.setBeatmungsdauer(respD);
                    c.setBewegungsBeatmungsDauer(respDBew);
                }
            }
        });
        return zusatzHash;
    }

    @Override
    protected Map<String, Integer> loadEscort() throws SQLException {
        return new HashMap<>();
    }

    @Override
    protected Map<String, BigDecimal> loadVwdIntensivdauer() throws SQLException {
        return new HashMap<>();
    }

    @Override
    protected Map<Integer, Vidierstufe> loadVidierstufen() throws SQLException {
        return new HashMap<>();
    }

    @Override
    protected Map<String, KisAbteilungContainer<String>> loadAbteilungen() throws SQLException, IOException {
        Map<String, KisAbteilungContainer<String>> result = new HashMap<>();
        executeStatement("SELECT DEP, DES, OFFCODE2, DET FROM " + getDbSchema() + "X8102DEP ORDER BY DATF", rs -> {
            while (rs.next()) {
                //MedicoAbteilungContainer a = new MedicoAbteilungContainer();
                KisAbteilungContainer<String> a = new KisAbteilungContainer<>();
                String oid = rs.getString(1);
                String oname = rs.getString(2);
                String cd301 = rs.getString(3);
                String det = rs.getString(4);
                if (oid != null && oname != null) {
                    a.setId(oid);
                    a.setName(oname.replace(",", ""));
                    a.setP301(cd301);
                    a.setBelegart((byte) (det.equals(KEY_BELEGAEZTLICHE_KOMBINATION) ? 1 : 0));
                    result.put(oid, a);
                }
            }
        });
        return result;
    }

    @Override
    public Map<String, Case> getFaelle() throws SQLException, IOException {
        Map<String, Case> result = new HashMap<>();
        //"Krankenkassen in der medico//s Datenbank zusammenstellen...");
        //"Patientenzusatze in der medico//s Datenbank zusammenstellen...");
        //"Fälle in der medico//s Datenbank zusammenstellen...");
        //HashMap drgAbrechungsArtenMap = getDrgAbrechnungsarten();
        executeStatement(getFaelleQuery(), rs -> {
//            fallMap.clear();
            int count;
            //"Fälle aus der medico//s Datenbank lesen...");
            count = 0;
            while (rs.next()) {
                count++;
                String fallNr = rs.getString(1);
                if (fallNr != null) {
                    final String ikz = getDefaultHosIdent();
                    String patNr = rs.getString(1);
                    Case f = new Case(patNr);
                    f.setIkz(ikz);
                    f.setFallNr(fallNr);
                    Date aDatum = rs.getDate(2);
                    Date eDatum = rs.getDate(3);
                    f.setAufnahmedatum(aDatum);
                    createDefaultBewegung(f, aDatum, eDatum);
                    Date geburtsdatum = rs.getDate(4);
                    //s = debMap.get(fallNr);                     // krankenkasse
                    final String krankenkasse = checkKasseStringLength(getPatientDeb(fallNr));  // krankenkasse //GKr 26.11.2013 ->  debMap.get(fallNr) not debMap.get(patNr)??
//                    sb.append(getDatumZeit(aDatum));            // aufnahmedatum,
                    f.setEntlassungsdatum(eDatum);
                    f.setVwdIntensiv(getVwdIntensiv(fallNr));
//                    sb.append(";" + getZusatzContainer(fallNr) + ";");// vwdintensiv
                    Integer alterInJahren = getAgeInYears(eDatum, geburtsdatum); //alterj
                    Integer alterInTagen = getAgeInDays(eDatum, geburtsdatum); //altert
                    f.setAlterInJahren(alterInJahren);
                    f.setAlterInTagen(alterInTagen);
                    char geschlecht = getGeschlecht(rs.getString(5));       // geschlecht
                    int geburtsgewicht = getGeburtsgewicht(fallNr);       // gewicht *******************************
                    String entlassungsgrund12 = getEntlassungsgrund12(rs.getString(6));
                    int index = entlassungsgrund12.indexOf(';');
                    if (index > 0) {
                        f.setEntlassungsgrund12(entlassungsgrund12.substring(0, index));
                        if (f.getEntlassungsgrund12().equalsIgnoreCase("16")) {
                            f.setEntlassungsgrund12("17");
                            entlassungsgrund12 = entlassungsgrund12.replaceAll("16", "17");
                        }
                    }
                    f.setEntlassungsgrund12(entlassungsgrund12);
//                    sb.append(entlassungsgrund12);                               // entlassungsgrund12, entlassungsgrund3
                    String plz = rs.getString(7);                              // plz
//                    Date geburtsdatum = rs.getDate(3);
//                    sb.append(getYearMonth(geburtsdatum));         // geburtsjahr, geburtsmonat
                    String einwKKH = rs.getString(10);
                    String aufnahmeanlass = rs.getString(8);
                    String aufnahmeAnlassEinwKkh = getAufnahmeanlassEinwKKH(einwKKH);  // aufnahmeanlass
                    String aufnahmegrund12 = getAufnahmegrund12(rs.getString(9));          // aufnahmegrund1
                    String aufnahmegrund34 = getAufnahmegrund34(rs.getString(9));          // aufnahmegrund2
                    String ikverlegungskh = rs.getString(11);                            // ikverlegungskh
                    f.setAufnahmeanlass(aufnahmeanlass);
                    f.setAufnahmegrund1(aufnahmegrund12);
                    f.setAufnahmegrund2(aufnahmegrund34);
//                    if (ikverlegungskh != null) {
//                        sb.append(ikverlegungskh);
//                    } else if (einwKKH != null && einwKKH instanceof String) {
//                        sb.append(ikverlegungskh);
//                    }
                    Integer ikverlegungskh2 = getDrgAbrechnungsart(rs.getString(13));
                    f.setFallart((ikverlegungskh != null) ? "DRG" : "PEPP");
//                    f.isDrg = (ikverlegungskh != null) ? 1 : 0;
                    result.put(fallNr, f);
                    /*if (fallNr.equals("635812"))
      System.out.println("XX");*/
                    f.setString2(String.valueOf(getZusatzContainer(fallNr)));         // beatmungsstunden
                    if (getProperties().getMedicoAbrech() != null) {
                        f.setString3(rs.getString(13)); //P.CHA Abrechnungsart;
                    }

                    getCpxMgr().write(f);
                }
            }
//            result = true;
            //"DRG Status zu den Fällen aus der medico//s Datenbank lesen...");
            loadDrgStatus();
//        } else {
//            //"Keine Fälle in der medico//s Datenbank gefunden!");
//        }
        });
        return result;
    }

    @Override
    protected String getFaelleQuery() {
        String where = getDateWhere() + getAufnahmeArten() + getAufnahmegruendeSQL() + getMandant();
        String from = "FROM " + getDbSchema() + "X1100PAT P "
                + "INNER JOIN " + getDbSchema() + "X1000PER PER ON (P.PER=PER.PER) "
                + "LEFT OUTER JOIN " + getDbSchema() + "X1131PDR FZF ON (P.PAT=FZF.PAT) ";
        if (where.length() <= 0) {
            where = "1=1";
        }
//            Object o = connection.getSingleSQLResult("SELECT COUNT(*) " + from + " WHERE P.ADMERRORD IS NULL AND P.RESERVMK=0 AND" + where);
//            if (o != null && o instanceof Number) {
//                results = ((Number) o).intValue();
//            }
        return "SELECT P.PAT, P.ADMD, P.DISD, PER.BIRTHD, PER.SEX, "
                + "P.DIR, P.ZIP, P.ADM, P.ADR, P.AINS, P.DINS, P.PER, P.CHA " + from
                + "WHERE P.ADMERRORD IS NULL AND P.RESERVMK=0 AND " + where + " ORDER BY P.PAT";
    }

    private String getDbSchema() {
        return getProperties().getDbSchema2(DEFAULT_SCHEMA);
//        //String schema = getProperties().getDbSchemas().get(this.m_srcName);
//        String schema = getProperties().getDbSchemas().entrySet().iterator().next().getValue();
//        if (schema != null) {
//            schema = schema + ".";
//        } else {
//            schema = DEFAULT_SCHEMA;
//        }
//        return schema;
    }

//    private String getDBSchema() {
//        if (m_dbSchema == null) {
////            if (this.m_dbSchemas == null) {
////                this.loadProperties();
////            }
//            String schema = (String) this.m_dbSchemas.get(this.m_srcName);
//            if (schema != null) {
//                m_dbSchema = schema + ".";
//            } else {
//                m_dbSchema = DEFAULT_SCHEMA;
//            }
//        }
//        return m_dbSchema;
//    }
    protected String getAufnahmeGruende1SQL() {
        return "SELECT DISTINCT SUBSTR(ADR,1,2) FROM " + getDbSchema() + "X1100PAT "
                + "ORDER BY SUBSTR(ADR,1,2)";
    }

    protected String getAufnahmeGruende2SQL() {
        return "SELECT DISTINCT SUBSTR(ADR,3,2) FROM " + getDbSchema() + "X1100PAT "
                + "ORDER BY SUBSTR(ADR,3,2)";
    }

    protected String getEntlassungsGruende1SQL() {
        return "SELECT DISTINCT SUBSTR(DIR,1,2) FROM " + getDbSchema() + "X1100PAT "
                + "WHERE DIR<> 'DIR' ORDER BY SUBSTR(DIR,1,2)";
    }

    protected String getEntlassungsGruende2SQL() {
        return "SELECT DISTINCT SUBSTR(DIR,3,1) FROM " + getDbSchema() + "X1100PAT "
                + "WHERE DIR<> 'DIR' ORDER BY SUBSTR(DIR,3,1)";
    }

    protected String getAufnahmeArtenSQL() {
        return "SELECT DISTINCT TYP FROM " + getDbSchema() + "X1100PAT "
                + "ORDER BY TYP";
    }

    protected String getDiagnoseArtenSQL() {
        return "SELECT DISTINCT DIT FROM " + getDbSchema() + "X1280DIA";
    }

//    @Override
//    public Integer getDiagnoseart(String dit) {
//        if (dit == null) {
//            return 0;
//        }
//        switch (dit) {
//            case "AUF":
//                return 2;
//            case "ARB":
//                return 12;
//            case "EIN":
//                return 1;
//            case "POP":
//                return 7;
//            case "PRP":
//                return 8;
//            case "RIS":
//                return 9;
//            case "PFL":
//                return 10;
//            case "ENT":
//                return 4;
//            case "ENTA":
//                return 11;
//            case "SV":
//                return 17;
//            case "SN":
//                return 18;
//            default:
//                break;
//        }
//        return 0;
//    }
    private String getMandant() {
//        if (getProperties().getMandantStatement() == null) {
        if (getProperties().getMandant() != null && !getProperties().getMandant().isEmpty()) {
            return " AND P.MAN = '" + getProperties().getMandant() + "' ";
        } else {
            return "";
        }
//        return m_mandantStatement;
    }

    @Override
    protected Map<String, Integer> loadDrgAbrechnungsarten() throws SQLException, IOException {
        Map<String, Integer> result = new HashMap<>();
        final String query = "SELECT CHA, PERMI FROM " + getDbSchema() + "X8301CHA ORDER BY PERMI";
        executeStatement(query, rs -> {
            while (rs.next()) {
                String cha = rs.getString(1);
                Integer permi = rs.getInt(2);
                if (permi != null && cha != null) {
                    int m = permi;
                    if (m != 0) {
                        result.put(cha, permi);
                    }
                }
            }
        });
        return result;
    }

    /**
     * getDatum todo: Diese de.checkpoint.ImportExport.KISAdapter-Methode
     * implementieren
     *
     * @param o Object
     * @return String
     */
    @Override
    protected String getDatum(Date o) {
        if (isIngres()) {
            Calendar tempCalendar = new GregorianCalendar(TimeZone.getTimeZone("ECT"));
            final TimeZone timezone = tempCalendar.getTimeZone();
            if (o != null && o instanceof Date) {
                if (timezone.inDaylightTime(o)) {
                    o.setTime(o.getTime() - 1000 * 60 * 60);
                }
            }
        }
        return super.getDatum(o);
    }

    /**
     * getDatumZeit todo: Diese de.checkpoint.ImportExport.KISAdapter-Methode
     * implementieren
     *
     * @param o Object
     * @return String
     */
    @Override
    protected String getDatumZeit(Date o) {
        if (isIngres()) {
            Calendar tempCalendar = new GregorianCalendar(TimeZone.getTimeZone("ECT"));
            final TimeZone timezone = tempCalendar.getTimeZone();
            if (o != null && o instanceof Date) {
                if (timezone.inDaylightTime(o)) {
                    o.setTime(o.getTime() - 1000 * 60 * 60);
                }
            }
        }
        return super.getDatumZeit(o);
    }

    protected String getAufnahmeArten() {
        String result = "";
        String nullFkt = getNullFunction();
        final Set<AdmissionCauseEn> anlaesse = getAufnahmeanlaesse();
        if (anlaesse != null && !anlaesse.isEmpty()) {
            String aufnahmeArten = "";
            Iterator<AdmissionCauseEn> it = anlaesse.iterator();
            while (it.hasNext()) {
                AdmissionCauseEn anlass = it.next();
                if (!aufnahmeArten.isEmpty()) {
                    aufnahmeArten += ",";
                }
                aufnahmeArten += "'" + anlass.name() + "'";
            }
            String[] b = null;
            String abrechArten = null;
            if (getProperties().getMedicoAbrech() != null) {
                abrechArten = "";
                b = getProperties().getMedicoAbrech().split(",");
                for (int i = 0; i < b.length; i++) {
                    abrechArten += "'" + b[i] + "'";
                    if (i < (b.length - 1)) {
                        abrechArten += ",";
                    }
                }
            }
            result = " AND P.TYP IN (" + aufnahmeArten + ")";
            if (abrechArten == null) {
                result += " AND P.CHA IN ('S', 'SGL', 'IV')";
            } else {
                result += " AND P.CHA IN (" + abrechArten + ")";
            }
            result += " AND " + nullFkt + "(P.DIS,'0')<>'KRS' ";
            //logger.info(result);
        }
        return result;
    }

    protected String getAufnahmeGruendeSQLWhere(String aufnahmeGruende) {
        return " AND SUBSTR(P.ADR,1,2) IN (" + aufnahmeGruende + ")";
    }

    public static String textForAdmissionCause1(String cause1) {
        String s;
        for (int i = 0, n = ADMISSION_CAUSE.length; i < n; i++) {
            s = ADMISSION_CAUSE[i];
            if (s != null && s.startsWith(cause1)) {
                return s;
            }
        }
        return cause1 + " - <unbekannt>";
    }

    protected String getDateWhere() {
        String aufVonDatum = getAufnahmedatumVon();
        String aufBisDatum = getAufnahmedatumBis();
        String entVonDatum = getEntlassungsdatumVon();
        String entBisDatum = getEntlassungsdatumBis();
        String date = "";
        if (isIngres()) {
            if (aufVonDatum != null && aufVonDatum.length() > 0) {
                date = "((P.ADMD >= " + getIngresDateString(aufVonDatum, false) + " AND FZF.ADMD IS NULL) OR "
                        + "(FZF.ADMD >= " + getIngresDateString(aufVonDatum, true) + "))";
            }
            if (aufBisDatum != null && aufBisDatum.length() > 0) {
                if (!date.isEmpty()) {
                    date += " AND ";
                }
                date += "((P.ADMD <= " + getIngresDateString(aufBisDatum, false) + " AND FZF.ADMD IS NULL) OR "
                        + "(FZF.ADMD <= " + getIngresDateString(aufBisDatum, true) + "))";
            }
            if (entVonDatum != null && entVonDatum.length() > 0) {
                if (!date.isEmpty()) {
                    date += " AND ";
                }
                date += "((P.DISD >= " + getIngresDateString(entVonDatum, false) + " AND FZF.DISD IS NULL) OR "
                        + "(FZF.DISD >= " + getIngresDateString(entVonDatum, true) + "))";
            }
            if (entBisDatum != null && entBisDatum.length() > 0) {
                if (!date.isEmpty()) {
                    date += " AND ";
                }
                date += "((P.DISD <= " + getIngresDateString(entBisDatum, false) + " AND FZF.DISD IS NULL) OR "
                        + "(FZF.DISD <= " + getIngresDateString(entBisDatum, true) + "))";
            }
        } else {
            if (aufVonDatum != null && aufVonDatum.length() > 0) {
                date = "((P.ADMD >= TO_DATE('" + aufVonDatum + " 00:00:00', 'DD.MM.YYYY HH24:MI:SS') AND FZF.ADMD IS NULL) OR "
                        + "FZF.ADMD >= TO_DATE('" + aufVonDatum + " 00:00:00', 'DD.MM.YYYY HH24:MI:SS'))";
            }
            if (aufBisDatum != null && aufBisDatum.length() > 0) {
                if (!date.isEmpty()) {
                    date += " AND ";
                }
                date += "((P.ADMD <= TO_DATE('" + aufBisDatum + " 23:59:59', 'DD.MM.YYYY HH24:MI:SS') AND FZF.ADMD IS NULL) OR "
                        + "FZF.ADMD <= TO_DATE('" + aufBisDatum + " 23:59:59', 'DD.MM.YYYY HH24:MI:SS'))";
            }
            if (entVonDatum != null && entVonDatum.length() > 0) {
                if (!date.isEmpty()) {
                    date += " AND ";
                }
                date += "((P.DISD >= TO_DATE('" + entVonDatum + " 00:00:00', 'DD.MM.YYYY HH24:MI:SS') AND FZF.DISD IS NULL) OR "
                        + "FZF.DISD >= TO_DATE('" + entVonDatum + " 00:00:00', 'DD.MM.YYYY HH24:MI:SS'))";
            }
            if (entBisDatum != null && entBisDatum.length() > 0) {
                if (!date.isEmpty()) {
                    date += " AND ";
                }
                date += "((P.DISD <= TO_DATE('" + entBisDatum + " 23:59:59', 'DD.MM.YYYY HH24:MI:SS') AND FZF.DISD IS NULL) OR "
                        + "FZF.DISD <= TO_DATE('" + entBisDatum + " 23:59:59', 'DD.MM.YYYY HH24:MI:SS'))";
            }
        }
        if (date.length() == 0) {
            return "1=1";
        } else {
            return date;
        }
    }

    private static String getIngresDateString(String date, boolean dayend) {
        String timestamp = "";
        String time = "00:00:00";
        date = date.replace(".", ";");
        if (dayend) {
            time = "23:59:59";
        }
        String[] dateParts = date.split(";");
        if (dateParts != null && dateParts.length == 3) {
            timestamp = "(TIMESTAMP '" + dateParts[2] + "-" + dateParts[1] + "-" + dateParts[0] + " " + time + "')";
        }
        return timestamp;
    }

    protected String getAufnahmegruendeSQL() {
        String result = "";
        Set<AdmissionReasonEn> aufnahmegruende = super.getAufnahmegruende();
        if (aufnahmegruende != null && !aufnahmegruende.isEmpty()) {
//            aufnahmeGruende = aufnahmeGruende.replaceAll("[\\(\\)]", "");
//            if (aufnahmeGruende.toUpperCase().indexOf(AppResources.getResource(AppResourceBundle.MNEMONIC_ALL).toUpperCase()) < 0) {
//                String a[] = aufnahmeGruende.split(",[\\s*]");
            String aufnahmeGruende = "";
            Iterator<AdmissionReasonEn> it = aufnahmegruende.iterator();
            while (it.hasNext()) {
                AdmissionReasonEn reason = it.next();
                if (!aufnahmeGruende.isEmpty()) {
                    aufnahmeGruende += ",";
                }
                aufnahmeGruende += "'" + reason.getId() + "'";
            }
            result = getAufnahmeGruendeSQLWhere(aufnahmeGruende);
//            }
        }
        return result;
    }

    private String getDiagnoseartenWhereSQL() {
        String result = "";
        final Set<IcdcTypeEn> diagnosearten = super.getDiagnosearten();
        if (diagnosearten != null) {
//            diagnoseArten = diagnoseArten.replaceAll("[\\(\\)]", "");
            if (diagnosearten.isEmpty()) {
                result = " AND D.DIT<>'OP'";         // OP 'Diagnosen' sind Prozeduren !
            } else {
//                String a[] = diagnoseArten.split(",[\\s*]");
                String diagnoseArten = "";
                Iterator<IcdcTypeEn> it = diagnosearten.iterator();
                while (it.hasNext()) {
                    IcdcTypeEn art = it.next();
                    if (!diagnoseArten.isEmpty()) {
                        diagnoseArten += ",";
                    }
                    diagnoseArten += "'" + art.name() + "'";
                }
                result = " AND D.DIT IN (" + diagnoseArten + ")";
            }
        }
        return result;
    }

    @Override
    protected String getStornoQuery() {
        String where = getDateWhere() + getAufnahmeArten() + getAufnahmegruendeSQL() + getMandant();
        String from = "FROM " + getDbSchema() + "X1100PAT P "
                + "LEFT OUTER JOIN " + getDbSchema() + "X1131PDR FZF ON (P.PAT=FZF.PAT) ";
        if (where.length() <= 0) {
            where = "1=1";
        }
//        Object o = connection.getSingleSQLResult("SELECT COUNT(*) " + from
//                + "WHERE P.ADMERRORD IS NOT NULL AND P.RESERVMK=0 AND " + where);
//        if (o != null && o instanceof Number) {
//            results = ((Number) o).intValue();
//        }
        return "SELECT P.PAT " + from + "WHERE P.ADMERRORD IS NOT NULL  AND P.RESERVMK=0 AND " + where + " ORDER BY P.PAT";
    }

    private int mapFallstatus(Integer status) {
        if (status != null) {
            int st = status;
            if (st > 0) {
                if (getProperties().isMedicoUserCaseState()) {
                    return 400 + st;
                } else {
                    return 100 + st; // RmcCaseAdminMgr_rm.MEDICO_STATUS_OFFSET + st
                }
            }
        }
        return 0;
    }

    private static int getDiagZusatzKey(String zusatz) {
        int key = 0;
        if (zusatz != null) {
            if (zusatz.equals("*")) {
                key = RmeDiagnose.REF_TYPE_STERN;
            }
            if (zusatz.equals("#")) {
                key = RmeDiagnose.REF_TYPE_KREUZ;
            }
            if (zusatz.equals("!")) {
                key = RmeDiagnose.REF_TYPE_ADDITIONAL;
            }
        }
        return key;
    }

    private String getSVSNProzedurenQuery() {
        String s = getDateWhere() + getAufnahmeArten() + getAufnahmegruendeSQL();
        if (s.length() > 0) {
            s = " AND " + s + getMandant();
        }
        //String where = "WHERE D.DIT='OP'" + s;
        //CP-136 Erweiterung um PROZ
        String where = "WHERE D.DIT='OP' OR D.DIT='PROZ' " + s;
        String from = "FROM " + getDbSchema() + "X1100PAT P "
                + "INNER JOIN " + getDbSchema() + "X1924S21 SG ON (P.PAT=SG.PAT) "
                + "INNER JOIN " + getDbSchema() + "X1100PAT SVN ON (P.PER=SVN.PER AND "
                + "((SVN.TYP='SV' AND SVN.ADMD BETWEEN SG.SVDATF AND P.ADMD) OR "
                + "(SVN.TYP='SN' AND SVN.DISD BETWEEN P.DISD AND SG.SNDATT))) "
                + "INNER JOIN " + getDbSchema() + "X1280DIA D ON (SVN.PAT=D.PAT) "
                + "LEFT OUTER JOIN " + getDbSchema() + "X1131PDR FZF ON (P.PAT=FZF.PAT) ";
//            Object o = connection.getSingleSQLResult("SELECT COUNT(*) " + from + where);
//            if (o != null && o instanceof Number) {
//                results = ((Number) o).intValue();
//            }
        return "SELECT P.PAT, D.DDC, D.DIALOCKED, D.DCAOFF, D.DIAGLOCOFF, D.DEP, D.OPDAT, SVN.TYP "
                + from + where + " ORDER BY P.PAT, D.POSNO";
    }

    private String getSVSNDiagnosenQuery() {
        String s = getDateWhere() + getAufnahmeArten() + getAufnahmegruendeSQL();
        if (s.length() > 0) {
            s = " AND " + s + getMandant();
        }
        String where = "WHERE D.DIT<>'OP'" + s;
        String from = "FROM " + getDbSchema() + "X1100PAT P "
                + "INNER JOIN " + getDbSchema() + "X1924S21 SG ON (P.PAT=SG.PAT) "
                + "INNER JOIN " + getDbSchema() + "X1100PAT SVN ON (P.PER=SVN.PER AND "
                + "((SVN.TYP='SV' AND SVN.ADMD BETWEEN SG.SVDATF AND P.ADMD) OR "
                + "(SVN.TYP='SN' AND SVN.DISD BETWEEN P.DISD AND SG.SNDATT))) "
                + "INNER JOIN " + getDbSchema() + "X1280DIA D ON (SVN.PAT=D.PAT) "
                + "LEFT OUTER JOIN " + getDbSchema() + "X1131PDR FZF ON (P.PAT=FZF.PAT) ";
//            Object o = connection.getSingleSQLResult("SELECT COUNT(*) " + from + where);
//            if (o != null && o instanceof Number) {
//                results = ((Number) o).intValue();
//            }
        return "SELECT P.PAT, D.DDC, D.MAINDRG, D.DIALOCKED, D.DCAOFF, D.DIAGLOCOFF, D.DEP, D.DIT, D.DIA, SVN.TYP, D.KZKRST  "
                + from + where + " ORDER BY P.PAT, D.POSNO";
    }

    @Override
    protected String getNachfolgerQuery() {
        String s = getDateWhereSucc();
        if (s.length() > 0) {
            s = " AND " + s + getMandant();
        }
        String where = "WHERE P.PAT=S.PAT AND S.PATPRED IS NOT NULL" + s;
//            Object o = connection.getSingleSQLResult("SELECT COUNT(*) FROM " + getDBSchema() + "X1100PAT P, " + getDBSchema() + "X1131PDR S " + where);
//            if (o != null && o instanceof Number) {
//                results = ((Number) o).intValue();
//            }
        String query = "SELECT P.PAT, S.PATPRED FROM " + getDbSchema() + "X1100PAT P, " + getDbSchema() + "X1131PDR S "
                + where + " ORDER BY P.PAT";
//    System.out.println(query);
        return query;
    }

    protected String getDateWhereSucc() {
        String aufVonDatum = getAufnahmedatumVon();
        String aufBisDatum = getAufnahmedatumBis();
        String entVonDatum = getEntlassungsdatumVon();
        String entBisDatum = getEntlassungsdatumBis();
        String date = "";
        if (isIngres()) {
            if (aufVonDatum != null && aufVonDatum.length() > 0) {
                date = "S.ADMD >= " + getIngresDateString(aufVonDatum, false);
            }
            if (aufBisDatum != null && aufBisDatum.length() > 0) {
                if (!date.isEmpty()) {
                    date += " AND ";
                }
                date += "S.ADMD <= " + getIngresDateString(aufBisDatum, true);
            }
            if (entVonDatum != null && entVonDatum.length() > 0) {
                if (!date.isEmpty()) {
                    date += " AND ";
                }
                date += "S.DISD >= " + getIngresDateString(entVonDatum, false);
            }
            if (entBisDatum != null && entBisDatum.length() > 0) {
                if (!date.isEmpty()) {
                    date += " AND ";
                }
                date += "S.DISD <= " + getIngresDateString(entBisDatum, true);
            }
        } else {
            if (aufVonDatum != null && aufVonDatum.length() > 0) {
                date = "S.ADMD >= TO_DATE('" + aufVonDatum + " 00:00:00', 'DD.MM.YYYY HH24:MI:SS')";
            }
            if (aufBisDatum != null && aufBisDatum.length() > 0) {
                if (!date.isEmpty()) {
                    date += " AND ";
                }
                date += "S.ADMD <= TO_DATE('" + aufBisDatum + " 23:59:59', 'DD.MM.YYYY HH24:MI:SS')";
            }
            if (entVonDatum != null && entVonDatum.length() > 0) {
                if (!date.isEmpty()) {
                    date += " AND ";
                }
                date += "S.DISD >= TO_DATE('" + entVonDatum + " 00:00:00', 'DD.MM.YYYY HH24:MI:SS')";
            }
            if (entBisDatum != null && entBisDatum.length() > 0) {
                if (!date.isEmpty()) {
                    date += " AND ";
                }
                date += "S.DISD <= TO_DATE('" + entBisDatum + " 23:59:59', 'DD.MM.YYYY HH24:MI:SS')";
            }
        }
        if (date.length() == 0) {
            return "1=1";
        } else {
            return date;
        }
    }

    @Override
    public void removeStorno() {
        //
    }

    @Override
    protected Map<String, TarifeContainer> loadTarife() throws SQLException, IOException {
        return new HashMap<>();
    }

    @Override
    protected String getUrlaubQuery() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Map<String, String> loadKostentraeger() throws SQLException, IOException {
        return new HashMap<>();
    }

    @Override
    protected Map<String, Date> loadEntbindungsdaten() throws SQLException, IOException {
        return new HashMap<>();
    }

    @Override
    protected String getStationenQuery() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Map<String, String> loadStationen() {
        return new HashMap<>();
    }

    @Override
    protected Map<String, Integer> loadUrlaub() throws SQLException, IOException {
        return new HashMap<>();
    }

    @Override
    protected Map<String, String> loadTransferHospLess24Hours() throws SQLException, IOException {
        return new HashMap<>();
    }

    @Override
    protected Map<String, KisInsuranceContainer> loadInsurances() throws SQLException, IOException {
        return new HashMap<>();
    }

}
