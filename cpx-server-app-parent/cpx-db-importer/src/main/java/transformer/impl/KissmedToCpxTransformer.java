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
import de.lb.cpx.db.container.TarifeGueltigContainer;
import de.lb.cpx.db.container.Vidierstufe;
import de.lb.cpx.db.container.ZusatzContainer;
import de.lb.cpx.model.enums.AdmissionCauseEn;
import de.lb.cpx.model.enums.AdmissionReasonEn;
import de.lb.cpx.model.enums.IcdcTypeEn;
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
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import module.Kissmed;
import module.impl.ImportConfig;

/**
 *
 * @author Dirk Niemeier
 */
public class KissmedToCpxTransformer extends DbToCpxTransformer<Kissmed, String, String, String, Integer> {

    public static final String TABLESPACE = "KISSMED.";
    public static final String KEINE_VWD_KEY = "DRGKeineVWDAbteilung";

    public static final int PAT_ZUSATZ_ABRFREIGABE_KEY = 35;
    public static final int PAT_ZUSATZ_FALLFREIGABE_KEY = 36;
    public static final int PAT_ZUSATZ_BEM_ABRFREIGABE_KEY = 37;
    public static final int PAT_ZUSATZ_MC_ANFRAGE_KEY = 38;
    public static final int PAT_ZUSATZ_BEM_FALLFREIGABE_KEY = 1318;
    public static final int PAT_ZUSATZ_BEM_MC_ANFRAGE_KEY = 1319;

    public static final int PAT_ZUSATZ_BEATMUNGS_KEY = 410;
    public static final int PAT_ZUSATZ_GEBURTSGEWICHT_KEY = 30;

//    private final List<BigDecimal> m_disziplinInt = new ArrayList<>();
    public KissmedToCpxTransformer(final ImportConfig<Kissmed> pImportConfig) throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException, InterruptedException, NoSuchAlgorithmException, NoSuchMethodException, InvocationTargetException, SQLException {
        super(pImportConfig);
//        m_disziplinInt.add(new BigDecimal(101));
//        m_disziplinInt.add(new BigDecimal(151));
    }

    @Override
    public TransformResult start() throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException, InterruptedException, NoSuchAlgorithmException, NoSuchMethodException, InvocationTargetException {
        return new TransformResult(patientCounter.get(), caseCounter.get(), exceptions);
    }

    @Override
    protected String getUrlaubQuery() {
        String where = "WHERE U.P_NR=P.P_NR AND "
                + getDateWhere() + getAufnahmeArten() + getAufnahmegruendeSQL();
//            Object o = connection.getSingleSQLResult("SELECT COUNT(*) FROM " + TABLESPACE + "PATIENT P, " + TABLESPACE + "PAT_URLAUB U " + where);
//            if (o != null && o instanceof Number) {
//                results = ((Number) o).intValue();
//            }
        return "SELECT P.P_NR, U.TAGE, U.DATUM_VON, U.DATUM_BIS FROM " + TABLESPACE + "PATIENT P, " + TABLESPACE + "PAT_URLAUB U " + where;
    }

    @Override
    public void getUrlaub() throws SQLException, IOException {
        //"Urlaubsdaten in der KISSMED Datenbank zusammenstellen...");
        executeStatement(getUrlaubQuery(), rs -> {
            int count = 0;
            //"Urlaubsdaten aus der KISSMED Datenbank lesen...");
            while (rs.next()) {
                count++;
                String fallNr = rs.getString(1);
                if (fallNr != null) {
                    Case fall = getFall(fallNr);
                    if (fall != null) {
                        Integer tage = rs.getInt(2);
                        Date von = rs.getDate(3);
                        Date bis = rs.getDate(4);
                        fall.addUrlaubstage(tage);
                        if (von != null && bis != null) {
                            Calendar tempCalendar = new GregorianCalendar(TimeZone.getTimeZone("ECT"));
                            tempCalendar.setTime(von);
                            tempCalendar.set(Calendar.HOUR_OF_DAY, 0);
                            tempCalendar.set(Calendar.MINUTE, 0);
                            tempCalendar.set(Calendar.SECOND, 0);
                            tempCalendar.set(Calendar.MILLISECOND, 0);
                            von = tempCalendar.getTime();
                            tempCalendar.setTime(bis);
                            tempCalendar.set(Calendar.HOUR_OF_DAY, 0);
                            tempCalendar.set(Calendar.MINUTE, 0);
                            tempCalendar.set(Calendar.SECOND, 0);
                            tempCalendar.set(Calendar.MILLISECOND, 0);
                            bis = tempCalendar.getTime();
                            fall.setUrlaubVon((int) (von.getTime() / (1000 * 60 * 60 * 24)));
                            fall.setUrlaubBis((int) (bis.getTime() / (1000 * 60 * 60 * 24)));
                        }
                    }
                }
            }
        });
    }

    @Override
    protected String getEntgelteQuery() {
        String fromWhere
                = "FROM " + TABLESPACE + " PATIENT P, "
                + TABLESPACE + " abrechnung A "
                + "WHERE P.p_nr=A.p_nr"
                + " AND A.storno = 0"
                + " AND A.abrechnungstyp='S'"
                + " AND A.tage_abr != 0"
                + " AND A.p301entgelt is not null "
                + " AND A.typ != 14 "
                + " AND A.betrag != 0 "
                + " AND "
                + getDateWhere() + getAufnahmeArten() + getAufnahmegruendeSQL();
//            Object o = connection.getSingleSQLResult("SELECT COUNT(*) "
//                    + fromWhere);
//            if (o != null && o instanceof Number) {
//                results = ((Number) o).intValue();
//            }
        String query
                = "SELECT  P.p_nr"
                + ", A.kostentraeger_id"
                + ", A.p301entgelt"
                + ", A.betrag"
                + ", 1"
                + ", A.vondatum"
                + ", A.bisdatum "
                + fromWhere;
//   System.out.println(query);
        return query;
    }

    @Override
    public void getEntgelte() throws SQLException, IOException {
        //"Entgelte in der KISSMED Datenbank zusammenstellen...");
        executeStatement(getEntgelteQuery(), rs -> {
            while (rs.next()) {
                String fallNr = rs.getString(1);
                if (fallNr != null) {
                    Case fall = getFall(fallNr);
                    if (fall != null) {
                        String kasse = rs.getString(2);      //IK Kasse
                        String kik = getKostentraeger(kasse);
                        String entgeltart = rs.getString(3);     //Entgeltart
                        Double entgeltbetrag = rs.getDouble(4);    //Entgeltbetrag
                        Integer entgeltanzahl = rs.getInt(5);    //Entgeltanzahl
                        Date datumVon = rs.getDate(6);   //von Datum
                        Date datumBis = rs.getDate(7);   //bis Datum

                        Fee fee = new Fee(fall);
                        fee.setAnzahl(entgeltanzahl);
                        fee.setKasse(kik != null ? kik : "000000000");
                        fee.setBetrag(entgeltbetrag);
                        fee.setVon(datumVon);
                        fee.setBis(datumBis);
                        fee.setEntgeltschluessel(entgeltart);

                        getCpxMgr().write(fee);
                    }
                }
            }
        });
    }

    @Override
    public void getBewegungen() throws SQLException, IOException {
        //"Abteilungen301 aus der KISSMED Datenbank lesen...");
        //"Tarife aus der KISSMED Datenbank lesen...");
        //"Bewegungen in der KISSMED Datenbank zusammenstellen...");
        executeStatement(getBewegungenQuery(), rs -> {

            //"Bewegungen aus der KISSMED Datenbank lesen...");
            while (rs.next()) {
                String fallNr = rs.getString(1);
                if (fallNr != null) {
                    Case fall = getFall(fallNr);
                    if (fall != null) {
                        Department b = new Department(fall);
                        b.setVerlegungsdatum(rs.getDate(2));
                        b.setEntlassungsdatum(rs.getDate(3));
//                        b.isDefault = 0;
//                        b.abteilung = name4AbteilungID(rs.get(4));
//                        if (b.abteilung != null && b.abteilung.length() > 49) {
//                            b.abteilung = b.abteilung.substring(0, 49);
//                        }
//                        b.station = this.name4AbteilungID(rs.get(3));
                        String tarifId = rs.getString(6);
                        TarifeGueltigContainer tg = getGueltigenTarif(rs.getDate(2), tarifId);
                        int days = getVwd(b.getVerlegungsdatum(), b.getEntlassungsdatum());
                        if (tg != null) {
                            KisAbteilungContainer<String> abt = getAbteilung(tg.getP301());
                            b.setCode(tg.getP301());
                            b.setCodeIntern(getAbteilungName(rs.getString(4)));
                            if (tg.isIsDrg()) {
                                fall.setFallart("DRG");
                            }
                            if (abt.isNalos()) {
                                if (!isUrlaub(fall, b.getVerlegungsdatum(), b.getEntlassungsdatum())) {
                                    fall.addUrlaubstage(days);
//                                    fall.nalos += days;
                                }
                            }
                        }
//                        b = fall.addBewegung(b);
                        BigDecimal dispNr = rs.getBigDecimal(7);
//                        if (dispNr != null && m_disziplinInt.contains(dispNr)) {
//                            b.addVwdIntensiv(days);
//                        }
                    }
                }
            }
        });
    }

    @Override
    protected String getBewegungenQuery() {
        String where = "WHERE P.P_NR=V.P_NR AND V.STATION_ID=S.STATION_ID(+) AND V.STATION_ID<>'-1' AND "
                + getDateWhere() + getAufnahmeArten() + getAufnahmegruendeSQL();
//            Object o = connection.getSingleSQLResult("SELECT COUNT(*) FROM " + TABLESPACE + "PATIENT P, " + TABLESPACE + "PAT_VERLEGUNG V, " + TABLESPACE + "ATTR_STATION S "
//                    + where);
//            if (o != null && o instanceof Number) {
//                results = ((Number) o).intValue();
//            }
        return "SELECT P.P_NR, V.ZEIT_AB, V.ZEIT_BIS, V.STATION_ID, V.KLINIK_ID, V.TARIF, S.PPR_DISZIPLIN "
                + "FROM " + TABLESPACE + "PATIENT P, " + TABLESPACE + "PAT_VERLEGUNG V, " + TABLESPACE + "ATTR_STATION S " + where + " ORDER BY P.P_NR, V.LFD_NR";
    }

    @Override
    public void getMultiFaelle() {
        //
    }

    @Override
    public void getStationen() {
        //
    }

    @Override
    public void getDiagnosen() throws SQLException, IOException {
        //"Diagnosen in der KISSMED Datenbank zusammenstellen...");
        executeStatement(getDiagnosenQuery(), rs -> {
            //"Diagnosen einlesen...");
            while (rs.next()) {
                String fallNr = rs.getString(1);
                if (fallNr != null) {
                    Case fall = getFall(fallNr);
                    if (fall != null) {
                        String icdCode = rs.getString(2);
                        Boolean hd = rs.getBoolean(3);
                        Date datum = rs.getDate(4);
                        Integer art = rs.getInt(5);
                        String seite = rs.getString(6);
                        Boolean intern = rs.getBoolean(7);
//                        sb.append(icdCode != null ? icdCode : "");
//                        sb.append(); // lokalisation
//                        sb.append((iArt == 6 && iHd != 0) ? 1 : 0);  // Hauptdiagnose
//                        sb.append(iHd != 0 ? 1 : 0);       // Hauptdiagnose bewegung
//                        sb.append((iArt == 6 && isIntern == 0) ? 1 : 0);      // nur DRG Diagnosen mit groupen
////                        sb.append(getICDVersion(fall.aufnahmeDatum));
//                        i = fall.getBewegungsID(datum, fall);
                        Department dep = getBewegung(datum, fall);
//                        if (i > 0) {
//                            sb.append(i);                   // bewegungsid
//                        } else {
//                            sb.append('0');
//                        }
//                        sb.append(iArt);                    // diagnoseart
                        Diagnose<?> diagnose;
                        if (hd) {
                            diagnose = new Hauptdiagnose(dep);
                        } else {
                            diagnose = new Nebendiagnose(dep);
                        }
                        diagnose.setCode(icdCode);
                        diagnose.setLokalisation(getLokalisation(seite));
                        diagnose.setIcdType(art);

                        getCpxMgr().write(diagnose);
                    }
                }
            }
        });
    }

    @Override
    public void getProzeduren() throws SQLException, IOException {
        //"Prozeduren in der KISSMED Datenbank zusammenstellen...");
        executeStatement(getProzedurenQuery(), rs -> {
            //"Prozeduren einlesen...");
            while (rs.next()) {
                String fallNr = rs.getString(1);
                if (fallNr != null) {
                    Case fall = getFall(fallNr);
                    /* 3.9.5 2015-09-02 DNi: #FINDBUGS - Potenzielle NP-Exception, weil fall != null nicht abgefragt wird. */
//                    checkErbringungsArt(bewegungen);
                    if (fall != null) {
                        //List<Department> bewegungen = new ArrayList<>(fall.getDepartments());
                        Date datum = rs.getDate(2);
                        String opsCode = rs.getString(3);
                        String seite = rs.getString(4);
                        Boolean intern = rs.getBoolean(5);
                        //typ = fall.getBewegungsTyp(datum, fall);
                        //isIntern = (intern != null && intern instanceof Number) ? ((Number) intern).intValue() : 0;
                        Department dep = getBewegung(datum, fall);
                        Procedure proc = new Procedure(dep);
                        proc.setCode(opsCode);
                        proc.setLokalisation(getLokalisation(seite));
                        proc.setToGroup(true);
                        proc.setDatum(datum);

                        getCpxMgr().write(proc);

//                        sb.append(getDatumZeit(datum)); // datum
//                        sb.append(';');
//                        sb.append(getOPSVersion(fall.aufnahmeDatum)); // opsversion
//                        sb.append(';');
//                        sb.append(getBeatmungsDauer(fallNr));         // beatmungsstunden
//                        sb.append(';');
//                        i = getBewegungsID(datum, fall, row.get(5));
//                        if (i > 0) {
//                            sb.append(i);                             // bewegungsid
//                        } else {
//                            sb.append('0');
//                        }
//                        sb.append(';');
//                        sb.append();            // lokalisation
//                        sb.append(getOfficialCode(opsCode, datum, getOPSVersion(fall.aufnahmeDatum)));
//                        if (!uploadClient.sendLine(sb.toString())) {
//                            result = false;
//                            break;
//                        }
                    }
                }
            }
        });
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
    protected Map<Integer, String> loadEinzugsgebiet() throws SQLException {
        return new HashMap<>();
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
    public Map<String, Case> getFaelle() throws SQLException, IOException {
        //"VWD intensiv für Fälle in der KISSMED DB zusammenstellen...");
        Map<String, Integer> hm = new HashMap<>();
        executeStatement(getFallVwdQuery(), rs -> {
            while (rs.next()) {
                String fallNr = rs.getString(1);
                Integer vwd = rs.getInt(2);
                if (fallNr != null && vwd != null) {
                    hm.put(fallNr, vwd);
                }
            }
        });
        //"Fälle in der KISSMED DB zusammenstellen...");
        Map<String, Case> result = new HashMap<>();
        executeStatement(getFaelleQuery(), rs -> {
            //"Fälle aus der KISSMED Datenbank lesen...");
            while (rs.next()) {
                String fallNr = rs.getString(2);
                if (fallNr != null) {
                    String ikz = getDefaultHosIdent();
                    String patNr = rs.getString(2);
                    Case f = new Case(patNr);
//                    KISFallContainer f = new KISFallContainer();
                    Date aufnahmedatum = rs.getDate(3);
                    f.setAufnahmedatum(aufnahmedatum);
                    f.setFallNr(fallNr);
                    f.setIkz(ikz);
                    Date entlassungsdatum = rs.getDate(4);
                    createDefaultBewegung(f, aufnahmedatum, entlassungsdatum);
                    String krankenkasse = rs.getString(1);                             // krankenkasse
                    String kik = getKostentraeger(krankenkasse);
                    f.setVersichertennr(kik);
                    f.setEntlassungsdatum(entlassungsdatum);
                    Integer vwd = hm.get(fallNr);
                    Integer vwdIntensiv = vwd != null ? vwd : 0;
                    f.setVwd(vwd);
                    f.setVwdIntensiv(vwdIntensiv);
                    Date geburtsdatum = rs.getDate(5);
                    Integer alterInTagen = getAgeInDays(aufnahmedatum, geburtsdatum);
                    Integer alterInJahren = getAgeInYears(aufnahmedatum, geburtsdatum);
                    f.setAlterInTagen(alterInTagen);
                    f.setAlterInJahren(alterInJahren);
                    f.setGewicht(getGeburtsgewicht(fallNr));
//                    sb.append(getGeschlecht(rs.get(6)));       // geschlecht
                    f.setEntlassungsgrund12(getEntlassungsgrund12(rs.getString(7))); // entlassungsgrund12, entlassungsgrund3
                    f.setEntlassungsgrund3(getEntlassungsgrund3(rs.getString(7))); // entlassungsgrund12, entlassungsgrund3
//                    String plz = rs.getString(8);                             // plz
                    String einwKKH = rs.getString(14);
                    String aufnahmeart = rs.getString(9); //not in use?
                    f.setAufnahmeanlass(getAufnahmeanlass(einwKKH));
                    f.setAufnahmegrund1(rs.getString(10));
                    f.setAufnahmegrund2(rs.getString(11));
                    String ikverlegungskh = rs.getString(13);                            // ikverlegungskh
                    f.setFallstatus(getKisFallStatus(fallNr));
                    result.put(fallNr, f);
                }
            }
        });
        return result;
    }

    @Override
    protected String getFaelleQuery() {
        String where = "WHERE " + getDateWhere() + getAufnahmeArten() + getAufnahmegruendeSQL();
//        Object o = connection.getSingleSQLResult("SELECT COUNT(*) FROM " + TABLESPACE + "PATIENT P " + where);
//        if (o != null && o instanceof Number) {
//            results = ((Number) o).intValue();
//        }
        String query
                = "SELECT P.kostentraeger_id, P.P_NR, P.AUFN_ZEIT, P.ENTL_ZEIT, P.P_GEB_DATUM, P.GESCHLECHT, "
                + "P.ENTL_GRUND, P.V_PLZ, P.AUFNAHMEART, P.AUFNGRUND301_01, P.AUFNGRUND301_02, "
                + "P.PID, P.ENTL_IK, P.EINW_KKH_ID "
                + "FROM " + TABLESPACE + "PATIENT P "
                + where
                + "ORDER BY P.P_NR";
        return query;
    }

    @Override
    protected String getPatientenQuery() {
        String where = "WHERE O.OPID_ID=ORE.OPID_ID AND ORE.P_NR=P.P_NR AND "
                + getDateWhere() + getAufnahmeArten() + getAufnahmegruendeSQL();

//   Object cnt=connection.getSingleSQLResult("SELECT COUNT(*) FROM PATIENT P, OPID_REF OR, OPID O "+
//                                     where);
//   results = (cnt != null) ? ((Number)cnt).intValue() : 0;
//   if(results > 0) {
        return "SELECT DISTINCT O.OPID, P.P_NAME, P.P_VORNAME, P.P_GEB_DATUM, P.GESCHLECHT, P.P_NR "
                + "FROM " + TABLESPACE + "PATIENT P, " + TABLESPACE + "OPID_REF ORE, " + TABLESPACE + "OPID O " + where;
    }

    @Override
    public void getPatienten() throws SQLException, IOException {
        //"Patientendaten in der KISSMED DB zusammenstellen...");
        executeStatement(getPatientenQuery(), rs -> {
            Set<Integer> patIDset = new HashSet<>();

            while (rs.next()) {
                Integer patOPID = rs.getInt(1);
                if (patOPID != null) {
                    if (!patIDset.add(patOPID)) {
                        continue;
                    }
                }
                /*fallNr = row.get(5);
      if(fallNr != null) {
       fall = (KISFallContainer)fallMap.get(fallNr);
       if(fall != null) {
        fall.patientid = id;*/
                String nachname = rs.getString(2);
                String vorname = rs.getString(3);
                Date geburtsdatum = rs.getDate(4);
                String geschlecht = rs.getString(5);
                String patNr = rs.getString(6);

                Patient pat = new Patient();
                pat.setPatNr(patNr);
                pat.setNachname(nachname);
                pat.setVorname(vorname);
                pat.setGeburtsdatum(geburtsdatum);
                pat.setGeschlecht(geschlecht);

                getCpxMgr().write(pat);
            }
//            Iterator<Map.Entry<String, Case>> it = getFaelle().entrySet().iterator();
//            while (it.hasNext()) {
//                Map.Entry<String, Case> entry = it.next();
//                String fallNr = entry.getKey();
//                Case fall = entry.getValue();
//                if (fall != null) {
//                    patOPID = opidHash.get(fallNr);
//                    if (patOPID != null) {
//                        patID = patIDmap.get(patOPID);
//                        if (patID != null) {
//                            fall.patientid = ((Number) patID).intValue();
//                        }
//                    }
//                }
//            }
        });
    }

    @Override
    public void getLabordaten() throws SQLException, IOException {
//        if (!m_importParam.contains(this.POSSIBLE_LABOR)) {
//            return true;
//        }
        //"Labordaten aus der KISSMED Datenbank zusammenstellen...");
        executeStatement(getLabordatenQuery(), rs -> {
            //"Labordaten aus der KISSMED Datenbank lesen...");
            while (rs.next()) {
                String fallNr = rs.getString(1);
                Case fall = getFall(fallNr);
                if (fall != null) {
                    Double wert1 = rs.getDouble(2);
                    Double wert2 = rs.getDouble(3);
                    Date datum = rs.getDate(4); //Datum
                    String bereich = rs.getString(5);
                    //if(o != null)
                    //sb.append("'" +row.get(5)+"'"); //Bereich
                    String bewertung = rs.getString(6);
                    Double untergrenze = rs.getDouble(7); //Untergrenze
                    Double obergrenze = rs.getDouble(8); //Obergrenze
                    String textwert = rs.getString(9);
                    String kommentar = rs.getString(10);
                    if (kommentar != null) {
                        kommentar = kommentar.replace(";", ",");
                    }
                    Date analyseDatum = rs.getDate(11); //Analyse-Datum
                    Integer position = rs.getInt(12);
                    String analyse = rs.getString(13);
                    String bezeichnung = rs.getString(14); //Bezeichnung
                    String einheit = rs.getString(15);
                    String methode = rs.getString(16);
                    Integer kategorie = rs.getInt(17);
                    String gruppe = rs.getString(18);

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
        String fromWhere = "FROM " + TABLESPACE + "PATIENT P, " + TABLESPACE + "PAT_LABORWERTE  L, LABORWERTE LS "
                + "WHERE L.P_NR=P.P_NR AND L.W_NR=LS.W_NR AND "
                + getDateWhere() + getAufnahmeArten() + getAufnahmegruendeSQL();
        //" AND P.P_NR='468689' ";
//        Object o = connection.getSingleSQLResult("SELECT COUNT(*) " + fromWhere);
//        if (o != null && o instanceof Number) {
//            results = ((Number) o).intValue();
//        }
        return "SELECT P.P_NR "
                + ", L.WERT1 "
                + ", L.WERT2 "
                + ", L.DATUM "
                + ", L.BEREICH "
                + ", L.BEWERTUNG "
                + ", L.UNTGR "
                + ", L.OBGR "
                + ", L.TEXTWERT "
                + ", L.KOMMENTAR "
                + ", L.DATUM_ANALYSE "
                +//10
                ", L.BEF_POS "
                + ", L.ANALYSE "
                + ", LS.BEZEICHNUNG "
                + ", LS.EINHEIT "
                + ", LS.METHODE  "
                + ", LS.KATEGORIE "
                + ", LS.GRUPPE  "
                +//17
                fromWhere;
    }

    @Override
    protected Map<String, Integer> loadDiagnosearten() throws SQLException, IOException {
        Map<String, Integer> result = new HashMap<>();
        result.put("1", 1); // - Einweisung
        result.put("2", 2); // - Aufnahme
        result.put("3", 3); // - Verlegung
        result.put("4", 4); // - Entlassung
        result.put("5", 5); // - Behandlung
        result.put("6", 6); // - DRG/DKR
        /*
  list.add(newRow("1", AppResources.getResource(AppResourceBundle.TXT_DIAG_TYPE_1_LONG)));
  list.add(newRow("2", AppResources.getResource(AppResourceBundle.TXT_DIAG_TYPE_2_LONG)));
  list.add(newRow("3", AppResources.getResource(AppResourceBundle.TXT_DIAG_TYPE_3_LONG)));
  list.add(newRow("4", AppResources.getResource(AppResourceBundle.TXT_DIAG_TYPE_4_LONG)));
  list.add(newRow("5", AppResources.getResource(AppResourceBundle.TXT_DIAG_TYPE_5_LONG)));
  list.add(newRow("6", AppResources.getResource(AppResourceBundle.TXT_DIAG_TYPE_6_LONG)));
         */
        return result;
    }

    @Override
    protected Map<String, ZusatzContainer<Integer>> loadZusatzContainer() throws SQLException, IOException {
        Map<String, ZusatzContainer<Integer>> result = new HashMap<>();
        String where = "WHERE P.P_NR=Z.P_NR AND (Z.FELD_ID=" + PAT_ZUSATZ_GEBURTSGEWICHT_KEY + " OR Z.FELD_ID="
                + PAT_ZUSATZ_BEATMUNGS_KEY + " OR Z.FELD_ID=" + PAT_ZUSATZ_ABRFREIGABE_KEY + " OR Z.FELD_ID="
                + PAT_ZUSATZ_FALLFREIGABE_KEY + " OR Z.FELD_ID=" + PAT_ZUSATZ_MC_ANFRAGE_KEY
                + ") AND " + getDateWhere() + getAufnahmeArten() + getAufnahmegruendeSQL();
        final String query = "SELECT Z.P_NR, Z.FELD_ID, Z.WERT_NUMBER, Z.WERT_STRING FROM " + TABLESPACE + "PATIENT P, " + TABLESPACE + "PAT_ZUSATZFELD Z "
                + where;
        executeStatement(query, rs -> {
            while (rs.next()) {
                String pnr = rs.getString(1);
                Integer feldID = rs.getInt(2);
                Integer wertNumber = rs.getInt(3);
                String wertString = rs.getString(4);
                if (pnr != null && feldID != null) {
                    ZusatzContainer<Integer> c = result.get(pnr);
                    if (c == null) {
                        c = new ZusatzContainer<>();
                        result.put(pnr, c);
                    }
                    if (feldID != null) {
                        switch (feldID) {
                            case PAT_ZUSATZ_GEBURTSGEWICHT_KEY:
                                c.setGeburtsgewicht(wertNumber);
                                break;
                            case PAT_ZUSATZ_BEATMUNGS_KEY:
                                c.setBeatmungsdauer(wertNumber);
                                break;
                            case PAT_ZUSATZ_ABRFREIGABE_KEY:
                                c.setAbrechnungsFreigabe(wertNumber == 1);
                                break;
                            case PAT_ZUSATZ_FALLFREIGABE_KEY:
                                c.setFallFreigabe(wertNumber == 1);
                                break;
                            case PAT_ZUSATZ_MC_ANFRAGE_KEY:
                                c.setMcAnfrage(wertNumber == 1);
                                break;
                            case PAT_ZUSATZ_BEM_ABRFREIGABE_KEY:
                                c.setBemAbrechnungsFreigabe(wertString);
                                break;
                            case PAT_ZUSATZ_BEM_FALLFREIGABE_KEY:
                                c.setBemFallFreigabe(wertString);
                                break;
                            case PAT_ZUSATZ_BEM_MC_ANFRAGE_KEY:
                                c.setBemMcAnfrage(wertString);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        });
        return result;
    }

    @Override
    protected Map<String, KisPatientContainer> loadPatienten() throws SQLException, IOException {
        return new HashMap<>();
    }

    @Override
    protected Map<String, Integer> loadDrgAbrechnungsarten() throws SQLException, IOException {
        return new HashMap<>();
    }

    @Override
    protected Map<String, KisAbteilungContainer<String>> loadAbteilungen() throws SQLException, IOException {
        Map<String, KisAbteilungContainer<String>> result = new HashMap<>();

        List<String> noVWD = new ArrayList<>();
        executeStatement("SELECT WERT_STRING FROM " + TABLESPACE + "P301_CONFIG WHERE BEZEICHNUNG='" + KEINE_VWD_KEY + "'", rs -> {
            while (rs.next()) {
                String str = rs.getString(1);
                if (str != null) {
                    String[] vals = str.split(",\\s*");
                    for (int i = 0; i < vals.length; i++) {
                        noVWD.add(vals[i].replaceAll("'", "").trim());
                    }
                }
            }
        });

        final String query = "SELECT SCHL301, BEZEICHNUNG, BELEG_ART FROM " + TABLESPACE + "PAT_SCHL301 "
                + "WHERE EBENE='1' AND GRUPPE='FACHABTEILUNGEN' AND (INAKTIV IS NULL OR INAKTIV='')";
        executeStatement(query, rs -> {
            while (rs.next()) {
                String oschl301 = rs.getString(1);
                String oname = rs.getString(2);
                int obeleg = rs.getInt(3);

//                String key = rs.getString(2);
//                String code = rs.getString(3);
//                int belegart = rs.getInt(4);
                String bezeichnung = rs.getString(5);
                KisAbteilungContainer<String> fab = new KisAbteilungContainer<>();
//                fab.setId(key);
                fab.setName(bezeichnung);
                fab.setBelegart(obeleg);
                fab.setP301(oschl301);
                if (noVWD.contains(fab.getP301())) {
                    fab.setNalos(true);
                }
                result.put(oschl301, fab);
            }
        });
        return result;
    }

    @Override
    protected String getProzedurenQuery() {
        String where = "WHERE P.P_NR=OP.P_NR AND OP.OP_NR=OS.OP_NR AND "
                + getDateWhere() + getAufnahmeArten() + getAufnahmegruendeSQL();
//            Object o = connection.getSingleSQLResult("SELECT COUNT(*) FROM " + TABLESPACE + "PATIENT P, " + TABLESPACE + "OP_DOKU OP, " + TABLESPACE + "OP_DOKU_SCHL OS "
//                    + where);
//            if (o != null && o instanceof Number) {
//                results = ((Number) o).intValue();
//            }
        return "SELECT P.P_NR, OP.DATUM, OS.ICPM, OS.KOERPERSEITE, OS.INTERN, OP.p301disziplin "
                + "FROM " + TABLESPACE + "PATIENT P, " + TABLESPACE + "OP_DOKU OP, " + TABLESPACE + "OP_DOKU_SCHL OS " + where + " ORDER BY P.P_NR, OS.LFD_NR";
    }

    @Override
    protected String getDiagnosenQuery() {
        String where = "WHERE P.P_NR=D.P_NR AND " + getDateWhere()
                + getAufnahmeArten() + getAufnahmegruendeSQL() + getDiagnoseartenSQL();
//            Object o = connection.getSingleSQLResult("SELECT COUNT(*) FROM " + TABLESPACE + "PATIENT P, " + TABLESPACE + "PAT_DIAGNOSE D "
//                    + where);
//            if (o != null && o instanceof Number) {
//                results = ((Number) o).intValue();
//            }
        return "SELECT P.P_NR, D.ICD, D.HAUPTDIAGNOSE, D.DATUM, D.ART, D.SEITE, D.INTERN "
                + "FROM " + TABLESPACE + "PATIENT P, " + TABLESPACE + "PAT_DIAGNOSE D " + where + " ORDER BY P.P_NR, D.LFD_NR";
    }

    @Override
    public Map<String, String> loadAufnahmegrund12() throws SQLException, IOException {
        Map<String, String> result = new HashMap<>();
        final String query = "SELECT SCHL301, SCHL301 || ' - ' || BEZEICHNUNG FROM " + TABLESPACE + "PAT_SCHL301 "
                + "WHERE EBENE='1' AND GRUPPE='AUFNAHMEGRUND' AND (INAKTIV IS NULL OR INAKTIV='') "
                + "ORDER BY SCHL301";
        executeStatement(query, rs -> {
            while (rs.next()) {
                String key = rs.getString(1);
                String description = rs.getString(2);
                result.put(key, description);
            }
        });
        return result;
    }

    @Override
    public Map<String, String> loadAufnahmegrund34() throws SQLException, IOException {
        Map<String, String> result = new HashMap<>();
        final String query = "SELECT SCHL301, SCHL301 || ' - ' || BEZEICHNUNG FROM " + TABLESPACE + "PAT_SCHL301 "
                + "WHERE EBENE='2' AND GRUPPE='AUFNAHMEGRUND' AND (INAKTIV IS NULL OR INAKTIV='') "
                + "ORDER BY SCHL301";
        executeStatement(query, rs -> {
            while (rs.next()) {
                String key = rs.getString(1);
                String description = rs.getString(2);
                result.put(key, description);
            }
        });
        return result;
    }

    @Override
    public Map<String, String> loadEntlassungsgrund12() throws SQLException, IOException {
        Map<String, String> result = new HashMap<>();
        final String query = "SELECT SCHL301, SCHL301 || ' - ' || BEZEICHNUNG FROM " + TABLESPACE + "PAT_SCHL301 "
                + "WHERE EBENE='1' AND GRUPPE='ENTLASSUNGSGRUND' AND (INAKTIV IS NULL OR INAKTIV='') "
                + "ORDER BY SCHL301";
        executeStatement(query, rs -> {
            while (rs.next()) {
                String key = rs.getString(1);
                String description = rs.getString(2);
                result.put(key, description);
            }
        });
        return result;
    }

    @Override
    public Map<String, String> loadEntlassungsgrund3() throws SQLException, IOException {
        Map<String, String> result = new HashMap<>();
        final String query = "SELECT SCHL301, SCHL301 || ' - ' || BEZEICHNUNG FROM " + TABLESPACE + "PAT_SCHL301 "
                + "WHERE EBENE='2' AND GRUPPE='ENTLASSUNGSGRUND' AND (INAKTIV IS NULL OR INAKTIV='') "
                + "ORDER BY SCHL301";
        executeStatement(query, rs -> {
            while (rs.next()) {
                String key = rs.getString(1);
                String description = rs.getString(2);
                result.put(key, description);
            }
        });
        return result;
    }

    @Override
    protected Map<String, String> loadAufnahmeanlass() throws SQLException, IOException {
        Map<String, String> result = new HashMap<>();
        result.put("A", "A - Ambulant");
        result.put("O", "O - OP");
        result.put("S", "S - Stationär");
        result.put("V", "V - Verlegung");
        result.put("X", "X - Storniert");
        /*
  list.add(newRow(AppResources.getResource(AppResourceBundle.TXT_ADMISSION_TYPE_1), AppResources.getResource(AppResourceBundle.TXT_ADMISSION_TYPE_1_LONG)));
  list.add(newRow(AppResources.getResource(AppResourceBundle.TXT_ADMISSION_TYPE_2), AppResources.getResource(AppResourceBundle.TXT_ADMISSION_TYPE_2_LONG)));
  list.add(newRow(AppResources.getResource(AppResourceBundle.TXT_ADMISSION_TYPE_3), AppResources.getResource(AppResourceBundle.TXT_ADMISSION_TYPE_3_LONG)));
  list.add(newRow(AppResources.getResource(AppResourceBundle.TXT_ADMISSION_TYPE_4), AppResources.getResource(AppResourceBundle.TXT_ADMISSION_TYPE_4_LONG)));
  list.add(newRow(AppResources.getResource(AppResourceBundle.TXT_ADMISSION_TYPE_5), AppResources.getResource(AppResourceBundle.TXT_ADMISSION_TYPE_5_LONG)));
         */
        return result;
    }

    private String getDateWhere() {
        String aufVonDatum = getAufnahmedatumVon();
        String aufBisDatum = getAufnahmedatumBis();
        String entVonDatum = getEntlassungsdatumVon();
        String entBisDatum = getEntlassungsdatumBis();
        String date = "";
        if (aufVonDatum != null && aufVonDatum.length() > 0) { // && !aufVonDatum.toUpperCase().equals(AppResources.getResource(AppResourceBundle.MNEMONIC_ALL).toUpperCase())) {
            date = "P.AUFN_DATUM >= TO_DATE('" + aufVonDatum + " 00:00:00', 'DD.MM.YYYY HH24:MI:SS')";
        }
        if (aufBisDatum != null && aufBisDatum.length() > 0) { // && !aufBisDatum.toUpperCase().equals(AppResources.getResource(AppResourceBundle.MNEMONIC_ALL).toUpperCase())) {
            if (!date.isEmpty()) {
                date += " AND ";
            }
            date += "P.AUFN_DATUM <= TO_DATE('" + aufBisDatum + " 23:59:59', 'DD.MM.YYYY HH24:MI:SS')";
        }

        if (entVonDatum != null && entVonDatum.length() > 0) { // && !entVonDatum.toUpperCase().equals(AppResources.getResource(AppResourceBundle.MNEMONIC_ALL).toUpperCase())) {
            if (!date.isEmpty()) {
                date += " AND ";
            }
            date += "P.ENTL_DATUM >= TO_DATE('" + entVonDatum + " 00:00:00', 'DD.MM.YYYY HH24:MI:SS')";
        }
        if (entBisDatum != null && entBisDatum.length() > 0) { // && !entBisDatum.toUpperCase().equals(AppResources.getResource(AppResourceBundle.MNEMONIC_ALL).toUpperCase())) {
            if (!date.isEmpty()) {
                date += " AND ";
            }
            date += "P.ENTL_DATUM <= TO_DATE('" + entBisDatum + " 23:59:59', 'DD.MM.YYYY HH24:MI:SS')";
        }
        if (date.length() == 0) {
            return "1=1";
        } else {
            return date;
        }
    }

    @Override
    public void removeStorno() throws SQLException, IOException {
        //"stornierte Fälle in der KISSMED Datenbank zusammenstellen...");
        executeStatement(getStornoQuery(), rs -> {
            while (rs.next()) {
                String fallNr = rs.getString(1);
//                String ikz = getDefaultHosIdent();
                if (fallNr != null) {
                    removeFall(fallNr);
//                    Case fall = getFall(fallNr);
//                    if (fall != null) {
//                        fallMap.remove(fallNr);
//                    }
                }
            }
        });
    }

    private static boolean isUrlaub(Case fall, Date anfang, Date ende) {
        if ((fall.getUrlaubVon() != null && fall.getUrlaubVon() != 0)
                && (fall.getUrlaubBis() != null && fall.getUrlaubBis() != 0)
                && anfang != null && ende != null) {
            Calendar tempCalendar = new GregorianCalendar(TimeZone.getTimeZone("ECT"));
            tempCalendar.setTime(anfang);
            tempCalendar.set(Calendar.HOUR_OF_DAY, 0);
            tempCalendar.set(Calendar.MINUTE, 0);
            tempCalendar.set(Calendar.SECOND, 0);
            tempCalendar.set(Calendar.MILLISECOND, 0);
            anfang = tempCalendar.getTime();
            tempCalendar.setTime(ende);
            tempCalendar.set(Calendar.HOUR_OF_DAY, 0);
            tempCalendar.set(Calendar.MINUTE, 0);
            tempCalendar.set(Calendar.SECOND, 0);
            tempCalendar.set(Calendar.MILLISECOND, 0);
            ende = tempCalendar.getTime();
            int bis = (int) (ende.getTime() / (1000 * 60 * 60 * 24));
            int von = (int) (anfang.getTime() / (1000 * 60 * 60 * 24));
            return fall.getUrlaubVon() == von && fall.getUrlaubBis() == bis;
        }
        return false;
    }

//    private String patientNummer4Pid(Object pid) {
//        Object s = opidHash.get(pid);
//        if (s == null) {
//            return "";
//        } else {
//            return (String) s;
//        }
//    }
    @Override
    protected Map<String, TarifeContainer> loadTarife() throws SQLException, IOException {
        Map<String, TarifeContainer> result = new HashMap<>();
        final String query = "SELECT TARIF_NR, BEZEICHNUNG, GUELTIG_VON, GUELTIG_BIS, SCHL301_NR, "
                + "DRG_ABR_MOEGLICH FROM " + TABLESPACE + "TARIFE WHERE DELETED=0 ORDER BY TARIF_NR, GUELTIG_VON";
        executeStatement(query, rs -> {
            while (rs.next()) {
                TarifeContainer lastTarif = null;
//                KissmedAbteilungContainer defaultAbt = new KissmedAbteilungContainer();
//                while (connection.fetchNext(row)) {
                String onr = rs.getString(1);
                String oname = rs.getString(2);
                Date ogvon = rs.getDate(3);
                Date ogbis = rs.getDate(4);
                String oschl = rs.getString(5);
                Integer moeglich = rs.getInt(6);
                if (onr != null && oschl != null) {
//                        KissmedAbteilungContainer a = (KissmedAbteilungContainer) abteilungen301Hash.get(oschl);
//                        if (a == null) {
//                            a = defaultAbt;
//                        }
                    TarifeGueltigContainer t = new TarifeGueltigContainer();
//                        t.beleg = a.belegart != 0;
                    t.setVon(ogvon);
                    t.setBis(ogbis);
                    t.setIsDrg(moeglich == null || moeglich > 0);
                    t.setP301(oschl); // hier kann noch gemappt werden !!!!!!!!!!
                    if (lastTarif == null || !onr.equals(lastTarif.getNr())) {
                        lastTarif = new TarifeContainer();
                        lastTarif.setNr(onr);
                        lastTarif.setName(oname);
                        lastTarif.addTarif(t);
                        result.put(onr, lastTarif);
                    } else {
                        lastTarif.addTarif(t);
                    }
                }
            }
        });
        return result;
    }

    private String getAufnahmeArten() {
        String result = "";
        Set<AdmissionCauseEn> arten = getAufnahmeanlaesse();
        if (arten != null && !arten.isEmpty()) { // && !aufnahmeArten.equals(AppResources.getResource(AppResourceBundle.MNEMONIC_ALL).toUpperCase())) {
//            aufnahmeArten = aufnahmeArten.replaceAll("[\\(\\)]", "");
//            if (aufnahmeArten.toUpperCase().indexOf(AppResources.getResource(AppResourceBundle.MNEMONIC_ALL).toUpperCase()) < 0) {
//                String a[] = aufnahmeArten.split(",[\\s*]");
            String aufnahmeArten = "";
            Iterator<AdmissionCauseEn> it = arten.iterator();
            while (it.hasNext()) {
                AdmissionCauseEn art = it.next();
                if (!aufnahmeArten.isEmpty()) {
                    aufnahmeArten += ",";
                }
                aufnahmeArten += "'" + art.name() + "'";
            }
            result = " AND P.AUFNAHMEART IN (" + aufnahmeArten + ")";
//            }
        }
        return result;
    }

    private String getAufnahmegruendeSQL() {
        String result = "";
        Set<AdmissionReasonEn> gruende = getAufnahmegruende();
        if (gruende != null && !gruende.isEmpty()) { // && !aufnahmeArten.equals(AppResources.getResource(AppResourceBundle.MNEMONIC_ALL).toUpperCase())) {
//            aufnahmeArten = aufnahmeArten.replaceAll("[\\(\\)]", "");
//            if (aufnahmeArten.toUpperCase().indexOf(AppResources.getResource(AppResourceBundle.MNEMONIC_ALL).toUpperCase()) < 0) {
//                String a[] = aufnahmeArten.split(",[\\s*]");
            String aufnahmeGruende = "";
            Iterator<AdmissionReasonEn> it = gruende.iterator();
            while (it.hasNext()) {
                AdmissionReasonEn art = it.next();
                if (!aufnahmeGruende.isEmpty()) {
                    aufnahmeGruende += ",";
                }
                aufnahmeGruende += "'" + art.name() + "'";
            }
            result = " AND P.AUFNGRUND301_01 IN (" + aufnahmeGruende + ")";
//            }
        }
        return result;
    }

    private String getDiagnoseartenSQL() {
        String result = "";
        Set<IcdcTypeEn> arten = getDiagnosearten();
        if (arten != null && !arten.isEmpty()) { // && !aufnahmeArten.equals(AppResources.getResource(AppResourceBundle.MNEMONIC_ALL).toUpperCase())) {
//            aufnahmeArten = aufnahmeArten.replaceAll("[\\(\\)]", "");
//            if (aufnahmeArten.toUpperCase().indexOf(AppResources.getResource(AppResourceBundle.MNEMONIC_ALL).toUpperCase()) < 0) {
//                String a[] = aufnahmeArten.split(",[\\s*]");
            String diagnoseArten = "";
            Iterator<IcdcTypeEn> it = arten.iterator();
            while (it.hasNext()) {
                IcdcTypeEn art = it.next();
                if (!diagnoseArten.isEmpty()) {
                    diagnoseArten += ",";
                }
                diagnoseArten += "'" + art.name() + "'";
            }
            result = " AND D.ART IN (" + diagnoseArten + ")";
//            }
        }
        return result;
    }

    @Override
    protected String getStornoQuery() {
        String where = "WHERE " + getDateWhere()
                + " AND P.aufnahmeart='X' ";
//            Object o = connection.getSingleSQLResult("SELECT COUNT(*) FROM " + TABLESPACE + "PATIENT P " + where);
//            if (o != null && o instanceof Number) {
//                results = ((Number) o).intValue();
//            }
        return "SELECT P.P_NR FROM " + TABLESPACE + "PATIENT P " + where;
    }

    private String getFallVwdQuery() {
        String where = "WHERE PV.P_NR=P.P_NR AND PV.station_id=S.station_id AND "
                + getDateWhere() + getAufnahmeArten() + getAufnahmegruendeSQL()
                + " AND S.ppr_disziplin in (101, 151) ";
        String from = "FROM " + TABLESPACE + "PATIENT P, " + TABLESPACE + "PAT_VERLEGUNG PV, " + TABLESPACE + "ATTR_STATION S ";
//            Object o = connection.getSingleSQLResult("SELECT COUNT(*) " + from + where);
//            if (o != null && o instanceof Number) {
//                results = ((Number) o).intValue();
//            }
        String query
                = "SELECT P.P_NR, DECODE (sum(nvl(PV.datum_bis, trunc(SYSDATE)) - PV.datum_ab), NULL, 0, 0, "
                + "DECODE(sum(P.entl_datum-P.aufn_datum), 0, 1, 0), "
                + "sum(nvl(PV.datum_bis, trunc(SYSDATE))-PV.datum_ab)) "
                + from
                + where
                + " GROUP BY P.P_NR "
                + " ORDER BY P.P_NR";
        return query;
    }

    private String getKostentraegerQuery() {
        String where = " WHERE P.kostentraeger_id=K.knr AND "
                + getDateWhere() + getAufnahmeArten() + getAufnahmegruendeSQL()
                + " AND K.ik IS NOT NULL ";
        String from = " FROM " + TABLESPACE + "PATIENT P, " + TABLESPACE + " kostentraeger K ";
//            Object o = connection.getSingleSQLResult("SELECT COUNT(*) " + from + where);
//            if (o != null && o instanceof Number) {
//                results = ((Number) o).intValue();
//            }
        String query
                = "SELECT K.knr, K.ik "
                + from + where;
        return query;
    }

//    private Map<String, String> loadPatientIDHash() throws SQLException, IOException {
//        Map<String, String> result = new HashMap<>();
//        String where = "WHERE O.OPID_ID=ORE.OPID_ID AND ORE.P_NR=P.P_NR AND "
//                + getDateWhere() + getAufnahmeArten() + getAufnahmeGruende();
//        String query = "SELECT P.P_NR, O.OPID FROM " + TABLESPACE + "PATIENT P, " + TABLESPACE + "OPID_REF ORE, " + TABLESPACE + "OPID O " + where;
//        executeStatement(query, rs -> {
//            while (rs.next()) {
//                result.put(rs.getString(1), rs.getString(2));
//            }
//        });
//        return result;
//    }
//    private Map<Integer, KisAbteilungContainer<Integer>> loadAbteilungenHash() throws SQLException, IOException {
//        Map<Integer, KisAbteilungContainer<Integer>> result = new HashMap<>();
//        String query = "SELECT OBJ_ID, OBJ_NAME FROM " + TABLESPACE + "OBJEKTE WHERE "
//                + "(OBJ_TYPE=4 OR OBJ_TYPE=8 OR OBJ_TYPE=4096)";
//        executeStatement(query, rs -> {
//            while (rs.next()) {
//                KisAbteilungContainer<Integer> a = new KisAbteilungContainer<>();
//                Integer oid = rs.getInt(1);
//                String oname = rs.getString(2);
//                if (oid != null && oname != null) {
//                    a.setId(oid);
//                    a.setName(oname.replace(",", ""));
//                    result.put(oid, a);
//                }
//            }
//        });
//        return result;
//    }
    @Override
    protected Map<String, String> loadKostentraeger() throws SQLException, IOException {
        Map<String, String> result = new HashMap<>();
        //"Kostenträger für Fälle in der KISSMED DB zusammenstellen...");
        executeStatement(getKostentraegerQuery(), rs -> {
            while (rs.next()) {
                String kid = rs.getString(1);
                String kik = rs.getString(2);
                if (kid != null && kik != null) {
                    result.put(kid, kik);
                }
            }
        });
        return result;
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
    protected String getNachfolgerQuery() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
