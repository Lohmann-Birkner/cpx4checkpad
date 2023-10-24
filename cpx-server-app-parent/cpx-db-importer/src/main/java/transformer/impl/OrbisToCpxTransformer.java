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
import de.lb.cpx.db.importer.utils.RmcCaseAdminMgr_rm;
import de.lb.cpx.model.enums.AdmissionCauseEn;
import de.lb.cpx.model.enums.AdmissionReasonEn;
import de.lb.cpx.model.enums.CaseDetailsCancelReasonEn;
import de.lb.cpx.model.enums.IcdcTypeEn;
import dto.impl.CancelCase;
import dto.impl.Case;
import dto.impl.Department;
import dto.impl.Diagnose;
import dto.impl.Hauptdiagnose;
import dto.impl.Lab;
import dto.impl.Nebendiagnose;
import dto.impl.Patient;
import dto.impl.Procedure;
import dto.impl.Ward;
import dto.types.Erbringungsart;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import module.Orbis;
import module.impl.ImportConfig;
import util.UtlDateTimeConverter;

/**
 *
 * @author Dirk Niemeier
 */
public class OrbisToCpxTransformer extends DbToCpxTransformer<Orbis, Integer, Integer, Integer, Integer> {

    private static final Logger LOG = Logger.getLogger(OrbisToCpxTransformer.class.getName());

    public static final String TABLESPACE = "OS_KERN.";
    public static final String TABLESPACE_CW = "OS_CW.";
    public static final String DATE_OF_OPEN_ENDING = "31.12.4000 00:00";
    private final List<String> mStornos = new ArrayList<>();

    public OrbisToCpxTransformer(final ImportConfig<Orbis> pImportConfig)
            throws InstantiationException, IllegalAccessException, IOException,
            NoSuchFieldException, InterruptedException, NoSuchAlgorithmException,
            NoSuchMethodException, InvocationTargetException, SQLException {
        super(pImportConfig);
    }
//
//    @Override
//    public TransformResult start() throws InstantiationException, IllegalAccessException,
//            IOException, NoSuchFieldException, InterruptedException, NoSuchAlgorithmException,
//            NoSuchMethodException, InvocationTargetException {
        //create readers for files in mInputDirectory, for example...
        //try (final FileManager fileManager = new FileManager(pFile.getAbsolutePath())) {
        //    try (BufferedReader br = fileManager.getBufferedReader()) {
        //        while ((line = br.readLine()) != null) {
        //            //do something with line, e.g. String[] sa = AbstractLine.splitLine(line, ";");
        //        }
        //    }
        //}

        //try (final CpxWriter cpxMgr = CpxWriter.getInstance(getOutputDirectory().getAbsolutePath())) {
        //read file content and write information to file
        //Example for some dummy hospital cases from the scratch
//        Random rand = new SecureRandom();
//        for (int i = 1; i <= 100; i++) {
//            final String patientNumber = "Patient No. " + (rand.nextInt(49) + 1);
//            final String caseNumber = "Case No. " + i;
//            if (!patientKeyExists(patientNumber)) {
//                Patient patient = new Patient();
//                patient.setPatNr(patientNumber);
//                mCpxMgr.write(patient);
//                patientCounter.incrementAndGet();
//            }
//            Case cs = new Case(patientNumber);
//            cs.setIkz("260510461");
//            cs.setFallNr(caseNumber);
//            cs.setFallart("DRG");
//            cs.setAufnahmedatum(new Date());
//            cs.setAufnahmeanlass("E"); //N
//            cs.setAufnahmegrund1("01");
//            cs.setAufnahmegrund2("01");
//            cs.setAlterInJahren(66);
//            cs.setAlterInTagen(0);
//            cs.setUrlaubstage(0);
//            cs.setVwd(0);
//            cs.setVwdSimuliert(0);
//            mCpxMgr.write(cs);
//
//            Department dep = new Department(cs);
//            dep.setCode("0100");
//            dep.setErbringungsart(Erbringungsart.HA);
//            dep.setVerlegungsdatum(new Date());
//            dep.setEntlassungsdatum(new Date());
//            mCpxMgr.write(dep);
//
//            Hauptdiagnose primIcd = new Hauptdiagnose(dep);
//            primIcd.setCode("F10.2");
//            mCpxMgr.write(primIcd);
//
//            Nebendiagnose sekIcd = new Nebendiagnose(dep);
//            sekIcd.setCode("I49.3");
//            sekIcd.setRefIcd(primIcd, RefIcdType.Plus);
//            mCpxMgr.write(sekIcd);
//
//            Fee fee1 = new Fee(cs);
//            fee1.setAnzahl(3);
//            fee1.setEntgeltschluessel("0815");
//            mCpxMgr.write(fee1);
//
//            Fee fee2 = new Fee(cs);
//            fee2.setAnzahl(7);
//            fee2.setEntgeltschluessel("0816");
//            mCpxMgr.write(fee2);
//
//            caseCounter.incrementAndGet();
//        }
        //}
//        return new TransformResult(patientCounter.get(), caseCounter.get(), exceptions);
//    }

    @Override
    public Map<Integer, Case> getFaelle() throws SQLException, IOException {

        final String ikz = getDefaultHosIdent();
        final Map<Integer, Case> fallMap = new HashMap<>();
        executeStatement(getFallPatientQuery(ikz), rs -> {
            while (rs.next()) {
                int fallId = rs.getInt(1);
                String fallNr = rs.getString(2);
                String persnr = rs.getString(13);

                //final Case cs = new Case(persnr);
                final Case f = new Case(persnr);
                f.setFallNr(fallNr);
                f.setIkz(ikz);
                Date aDatum = rs.getDate(3);
                f.setAufnahmedatum(aDatum);
                f.setEntlassungsdatum(rs.getDate(4));
//                f.setAufnahmeDatum(aDatum);
//                f.setEntlassungsDatum(rs.getDate(4));
                createDefaultBewegung(f, aDatum, f.getEntlassungsdatum());
                if (mProperties.isGwiMandant()) {
                    String iknr;
                    if (mProperties.getGwiMandanten() == null) {
                        iknr = rs.getString(21);
                    } else {
                        iknr = getMandant(fallNr, ikz);
                    }
                    if (iknr != null) {
                        f.setVersichertennr(iknr);
                    } else {
                        iknr = ikz.split(",")[0];
                        f.setVersichertennr(iknr);
                    }
                }
                String kkIk = rs.getString(16); // KK IK
                if (kkIk != null) {
                    f.setVersichertennr(kkIk);
                }
                String kkGruppe = rs.getString(17); // KK Gruppe
                f.setString1(kkGruppe);
                f.setVwdIntensiv(getVwdIntensiv(fallId));
                Date gebDatum = rs.getDate(5);
                f.setAlterInJahren(getAgeInYears(aDatum, gebDatum));
                f.setAlterInTagen(getAgeInDays(aDatum, gebDatum));
                f.setGewicht(rs.getInt(7));
                f.setEntlassungsgrund12(getEntlassungsgrund12(rs.getInt(8)));
                f.setEntlassungsgrund3(getEntlassungsgrund3(rs.getInt(12)));
                String aufnahmeanlassOrbis = getAufnahmeanlass(rs.getInt(24));
                Integer aufnahmeanlassCp = getAufnahmeanlass4CP(aufnahmeanlassOrbis);
                f.setAufnahmeanlass(aufnahmeanlassCp);
                f.setAufnahmegrund1(getAufnahmegrund12(rs.getInt(11)));
                f.setAufnahmegrund2(getAufnahmegrund34(rs.getInt(10)));
                f.setFallstatus(getKisFallStatus(rs.getInt(20)));
//                if (f.getFallstatus() == 200) {
//                    LOG.log(Level.SEVERE, "Unbekannter GWI-Status bei Fall " + fallID + ", Status : " + rs.getString(20));
//                }
//                if (f.getFallstatus() < 0) {
//                    LOG.log(Level.SEVERE, "Unbekannter Status bei Fall " + fallID + ", Status : " + rs.getString(20));
//                }
                //Vidierstufe setzen
                Vidierstufe vidierung = getVidierstufe(fallId);
                if (vidierung != null) {
                    Vidierstufe v = vidierung;
                    if (v.getMinFaVid() != null) {
                        f.setString3("FAB_VID:" + v.getMinFaVid());
                    } else {
                        f.setString3("FAB_VID:NEIN");
                    }
                    if (v.getFallVid() != null) {
                        f.setString3(f.getString3() + "/FALL_VID:" + v.getFallVid());
                    } else {
                        f.setString3(f.getString3() + "/FALL_VID:NEIN");
                    }
                    if (v.getDrgVid() != null) {
                        f.setString2(v.getDrgVid() + "");
                    } else {
                        f.setString2("NEIN");
                    }
                }
                //Faktfreigabe
                //f.setFaktfrei(rs.getDate(22));
                if (!patientKeyExists(persnr)) {
                    Patient pat = new Patient();
                    pat.setPatNr(persnr);
                    pat.setGeburtsdatum(gebDatum);
                    pat.setGeschlecht(rs.getString(6));
                    pat.setPlz(getEinzugsgebiet(rs.getInt(9))); // einzugsgebiet
                    pat.setNachname(rs.getString(14));
                    pat.setVorname(rs.getString(15));
                    pat.setVersichertennr(rs.getString(23));
                    mCpxMgr.write(pat);
                }
                fallMap.put(fallId, f);
//                mCpxMgr.write(cs);
            }
        });
//        } else {
////            pI.message(AppResources.getResource(AppResourceBundle.MSG_ORBIS_CASES_NOT_FOUND));
//            //"Keine Fälle in der ORBIS Datenbank gefunden!");
//        }
//        m_vwdIntensivMap.clear();
        return fallMap;
    }

    @Override
    public void getUrlaub() throws SQLException, IOException {

        //"Urlaubsdaten in der ORBIS Datenbank zusammenstellen...");
        executeStatement(getUrlaubQuery(), rs -> {
            Case fall;
            while (rs.next()) {
                int fallId = rs.getInt(1);
                fall = getFall(fallId);
                if (fall == null) {
                    continue;
                }
                Date von = rs.getDate(2);
                Date bis = rs.getDate(3);
                if (von != null && bis != null) {
                    long diff = bis.getTime() - von.getTime();
                    diff = (diff / (1000 * 60 * 60 * 24));
                    fall.setUrlaubstage(fall.addUrlaubstage((int) diff));
                }
            }
        });
    }

    @Override
    public void getEntgelte() throws SQLException, IOException {
        ObjectProperty<Case> fallProp = new SimpleObjectProperty<>();
        executeStatement(getDrgIcdQuery(), rs -> {
            Case fall = null;
//        pI.message(AppResources.getResource(AppResourceBundle.WAITING_ORBIS_FEE_DIAG_LOAD));
//"Entgelte für Diagnosen in der ORBIS Datenbank laden...");
//        pI.percent(0);
            int lastFallId = 0;
            while (rs.next()) {
                int fallId = rs.getInt(1); // FALLID
                if (fall == null || fallId != lastFallId) {
                    fall = getFall(fallId);
                    if (fall != null) {
                        fall.setFallart("DRG"); // .setIsDrg(1);
                    }
                }
//                if (fall != null) {
//                    String s = rs.getString(3);
//                    String code = rs.getString(2);
//                    if (s != null && "J".equals(s)) {
//                        fall.setPicd(code);
//                    }
//                    fall.getIcds().add(code);
//                }
                lastFallId = fallId;
            }
            fallProp.set(fall);
        });

        //"Entgelte für Prozeduren in der ORBIS Datenbank zusammenstellen...");
        executeStatement(getDrgOpsQuery(), rs -> {
            //        pI.message(AppResources.getResource(AppResourceBundle.WAITING_ORBIS_FEE_PROC_LOAD));
            //"Entgelte für Prozeduren in der ORBIS Datenbank laden...");
            int lastFallId = 0;
            while (rs.next()) {
                int fallId = rs.getInt(1); // FALLID
                if (fallProp.get() == null || fallId != lastFallId) {
                    Case fall2 = getFall(fallId);
                    fallProp.set(fall2);
                    if (fallProp.get() != null) {
                        //fallProp.get().getOps().clear();
                        fallProp.get().removeProcedures();
                    }
                }
//                if (fallProp.get() != null) {
//                    //fallProp.get().getOps().add(rs.getString(2));
//                    fallProp.get().getOps().add(rs.getString(2));
//                }
                lastFallId = fallId;
            }
        });

//        return result;
    }

    @Override
    public void getBewegungen() throws SQLException, IOException {
        //"Bewegungen in der ORBIS Datenbank zusammenstellen...");
        executeStatement(getBewegungenQuery(), rs -> {
            Case fall = null;
            int lastAbtId = 0;
//            pI.message(AppResources.getResource(AppResourceBundle.WAITING_ORBIS_MOVEMENT_READ));
//"Bewegungen aus der ORBIS Datenbank lesen...");
            int lastId = 0;
            while (rs.next()) {
//                count++;
                int fallId = rs.getInt(1);
//if (fallID.equals(new java.math.BigDecimal(5872)))
//System.out.println("...");
//                if (fallID != null) {
                if (lastId == 0 || lastId != fallId) {
                    fall = getFall(fallId);
                    lastAbtId = 0;
                }
                if (fall != null) {
                    boolean isBew = true;
//                    final Department dep = new Department(fall.getIkz(), fall.getFallNr());
//                        pI.percent((int) ((count * 100) / (long) size));
                    Department b = new Department(fall);
//                    b.setDepNr(dep.getNr());
//                    b.setAnfang(rs.getDate(2));
                    b.setVerlegungsdatum(rs.getDate(2));
//                    b.setEnde(rs.getDate(3));
                    b.setEntlassungsdatum(rs.getDate(3));
//                    b.setIsDefault((byte) 0);
                    if ((new UtlDateTimeConverter()).formatToGermanDate(b.getEntlassungsdatum(), true).equals(DATE_OF_OPEN_ENDING)
                            && fall.getEntlassungsdatum() != null) {
                        continue;
                    }
                    Integer fabid = rs.getInt(4);
                    Integer abtid = rs.getInt(5);
                    b.setTpId(fabid);
                    KisAbteilungContainer<Integer> abt = getAbteilung(abtid);
                    if (abt != null) {
//                            Ward ward = new Ward(dep);
                        if (abt.getId().equals(-300) || abt.getId().equals(-100)) {
                            //fall.setNalos(fall.getNalos() + getVwd(b.getVerlegungsdatum(), b.getEntlassungsdatum()));
                            fall.setUrlaubstage(fall.addUrlaubstage(getVwd(b.getVerlegungsdatum(), b.getEntlassungsdatum())));
                            isBew = false;
                        } else {
//                                ward.setCode(abt.getP301());
                            //b.setAbteilung(abt.getName());
                            //b.setAbteilung301(abt.getP301());
                            b.setCode(abt.getP301());
                            b.setCodeIntern(abt.getName());
                            if (mProperties.isGwiDrgBelegtyp()) {
                                Integer belegTyp = getDrgBeleg(fallId);
                                if (belegTyp != null) {
                                    if (belegTyp.byteValue() >= 3) {
                                        b.setErbringungsart(Erbringungsart.BoBa);
                                        //b.setTyp("BA");
                                    } else {
                                        b.setErbringungsart(Erbringungsart.HA);
                                        //b.setTyp("HA");
                                    }
//                                    b.setTypschluessel(belegTyp.byteValue());
                                } else {
//                                    b.setTyp("HA");
                                    b.setErbringungsart(Erbringungsart.HA);
//                                    b.setTypschluessel((byte) 1);
                                }
                            } else {
                                if (abt.isGwiBeleg()) {
                                    b.setErbringungsart(Erbringungsart.BoBa);
//                                    b.setTyp("BA");
//                                    b.setTypschluessel((byte) 3);
                                } else {
//                                    b.setTyp("HA");
                                    b.setErbringungsart(Erbringungsart.HA);
//                                    b.setTypschluessel((byte) 1);
                                }
                            }
                        }
//                            mCpxMgr.write(ward);
                    }
                    if (isBew) {
//                            b.id = index++;
                        Integer st = rs.getInt(5);
//                        if (st != null) {
//                            b.setStation(st.toString());
//                        }
                        if (abt != null && !abt.getId().equals(-400)) {
                            Department newDep = new Department(fall);
//                            fall.addBewegung(b, (abt.getTpId().equals(String.valueOf(lastAbtId))));
                            lastAbtId = abt.getId();
                        } else {
                            if (abtid == -400) {
                                //fall.addBewegung(b);
                                b = null;
                                lastAbtId = 0;
                            }
                        }
                    } else {
                        lastAbtId = 0;
                    }
                    if (b != null) {
                        mCpxMgr.write(b);
                    }
                    lastId = fallId;
                }
//                }
            }
//            result = true;
        });
//        rem_bewindex = index;
//        return result;
    }

    @Override
    public void getMultiFaelle() throws SQLException, IOException {
        Map<Integer, Integer> statiMap = getMultiFaelleStatus();
        executeStatement(getNachfolgerQuery(), rs -> {
            Case fall;
            Case succ;
            int vwd = 0;
            while (rs.next()) {
                Integer fallNr = rs.getInt(1);
                Integer succNr = rs.getInt(2);
                if (fallNr != null && succNr != null) {
                    fall = getFall(fallNr);
                    succ = getFall(succNr);
                    String fall2nr = rs.getString(4);
                    if (fall != null) {
                        if (statiMap.get(fallNr) != null) {
                            fall.setFallstatus(getKisFallStatus(statiMap.get(fallNr)));
                            LOG.log(Level.INFO, "Setzen {0} , {1}", new Object[]{fallNr, fall.getFallstatus()});
                        }
                    } else {
                        LOG.log(Level.INFO, "ID eines fehlerhaften Falles (logger): {0}", fallNr);
                    }
                    if (fall != null && succ != null) {
                        int dauer = getBeatmungsdauer(succNr);
                        if (dauer > 0D) {
                            dauer += getBeatmungsdauer(fallNr);
                            setBeatmungsdauer(fallNr, dauer);
                        }
                        for (Department dep : succ.getDepartments()) {
                            Department newDep = new Department(fall);
                            newDep.set(dep);
//                            newDep.setAufnehmendeIk(dep.getAufnehmendeIk());
//                            newDep.setBedIntensiv(dep.isBedIntensiv());
//                            newDep.setCode(dep.getCode());
//                            newDep.setEntlassungsdatum(dep.getEntlassungsdatum());
//                            newDep.setVerlegungsdatum(dep.getVerlegungsdatum());
//                            newDep.setErbringungsart(dep.getErbringungsart());
                        }
//                        fall.getBewegungen().addAll(succ.getBewegungen());
                        vwd = getVwd(fall.getEntlassungsdatum(), succ.getAufnahmedatum());
                        if (vwd > 0 && checkSameDay(fall.getAufnahmedatum(), fall.getEntlassungsdatum())) {
                            vwd = vwd - 1;
                        }
                        if ("06".equals(fall.getEntlassungsgrund12()) || "08".equals(fall.getEntlassungsgrund12())
                                || "12".equals(fall.getEntlassungsgrund12()) || "13".equals(fall.getEntlassungsgrund12())
                                || "17".equals(fall.getEntlassungsgrund12()) || "18".equals(fall.getEntlassungsgrund12())
                                || "19".equals(fall.getEntlassungsgrund12()) || "20".equals(fall.getEntlassungsgrund12())
                                || "21".equals(fall.getEntlassungsgrund12())) {
//                            fall.setUrlaubstage(fall.getUrlaubstage() + succ.getUrlaubstage());
                            fall.setUrlaubstage(fall.addUrlaubstage(succ.getUrlaubstage()));
                            //fall.setNalos(fall.getNalos() + succ.getNalos() + vwd);
                        } else {
//                            fall.setUrlaubstage(fall.getUrlaubstage() + succ.getUrlaubstage() + vwd);
                            fall.setUrlaubstage(fall.addUrlaubstage(succ.getUrlaubstage() + vwd));
                            //fall.setNalos(fall.getNalos() + succ.getNalos());
                        }
                        fall.setEntlassungsgrund12(succ.getEntlassungsgrund12());
                        fall.setEntlassungsdatum(succ.getEntlassungsdatum());
                        //kisStatus des letzten falles uebernehmen TDu 2007-12-20 wg. WOB
                        //fall.fallStatus = succ.fallStatus;
                        removeFall(succNr);
                        //mFallMap.remove(succNr);
                        addTranslation(fallNr, succNr);
//                        mTranslationMap.put(succNr, fallNr);
                        if (fall2nr != null) {
                            // AGe write into casecancel file
//                            mStornos.add(fall2nr);
                              CancelCase clCase = new CancelCase(); 
                              clCase.setFallNr(succ.getFallNr());
                              clCase.setIkz(succ.getIkz());
                              clCase.setCancelReason(CaseDetailsCancelReasonEn.MERGE.getIdStr());
                              mCpxMgr.write(clCase);
                        }
                    }
                }
            }
            calculateMulticaseLeaveDays(getTranslationMap().values());
        });
    }

    private Map<Integer, Integer> getMultiFaelleStatus() throws SQLException, IOException {
        Map<Integer, Integer> statiMap = new HashMap<>();
//        List<Object> row = new ArrayList<>();
        String nullFkt = "nvl";
//        int size = 0;
        if (isSqlsrv()) {
            nullFkt = "isnull";
        }
        String where = "WHERE " + getCasesWhere() + getCaseNumberWhere();
        String fromWhere = "FROM " + TABLESPACE + "FALL f "
                + "INNER JOIN " + TABLESPACE + "FALL_FALL ff ON ff.FALL2ID=f.FALLID "
                + "INNER JOIN " + TABLESPACE + "FALL fn ON ff.FALL1ID=fn.FALLID "
                + where + " and fn.fallnr is null and " + nullFkt + "(fn.abrechstat,0)<>"
                + nullFkt + "(f.abrechstat,0) and fn.storno_datum is null";
//        String query = "SELECT count(*) " + fromWhere;
//        Object o = connection.getSingleSQLResult(query);
//        if (o != null && o instanceof Number) {
//            size = ((Number) o).intValue();
//        }
        final String query = "SELECT f.fallid, " + nullFkt + "(fn.abrechstat,0) " + fromWhere;
//   System.out.println(query);
        executeStatement(query, rs -> {
            while (rs.next()) {
                Integer fallID = rs.getInt(1);
                Integer stat = rs.getInt(2);
                statiMap.put(fallID, stat);
                LOG.info("Zusammenstellung " + rs.getInt(1) + " , " + rs.getInt(2));
            }
        });
        return statiMap;
    }

    @Override
    protected String getNachfolgerQuery() {
//        int results = 0;
//        connect();
//        if (connection != null) {
        String where = "WHERE " + getCasesWhere()
                + " AND (ff.VERWEISARTID=4 OR ff.VERWEISARTID=7 OR ff.VERWEISARTID=8 OR ff.VERWEISARTID=13 OR ff.VERWEISARTID=14) "
                + " AND ff.stornodat is null " + getCaseNumberWhere();
//        String fromWhere = "FROM " + TABLESPACE + "FALL f "
//                + "INNER JOIN " + TABLESPACE + "FALL_FALL ff ON ff.FALL1ID=f.FALLID "
//                + "INNER JOIN " + TABLESPACE + "FALL f2 ON ff.FALL2ID=f2.FALLID "
//                + where;
//            Number o = (Number) connection.getSingleSQLResult("SELECT COUNT(*) " + fromWhere);
//            if (o != null && o instanceof Number) {
//                results = o.intValue();
//            }
        final String query = "SELECT ff.FALL1ID, "
                + //0
                "ff.FALL2ID, "
                + //1
                "ff.VERWEISARTID, "
                + //2
                "f2.fallnr "
                + //3
                "FROM " + TABLESPACE + "FALL f "
                + "INNER JOIN " + TABLESPACE + "FALL_FALL ff ON ff.FALL1ID=f.FALLID  "
                + "INNER JOIN " + TABLESPACE + "FALL f2 ON ff.FALL2ID=f2.FALLID "
                + where
                + " ORDER BY f.FALLNR";
//   System.out.println(query);
        return query;
//        }
//        return results;
    }

    @Override
    public void getStationen() throws SQLException, IOException {
        //"Stationen in der ORBIS Datenbank zusammenstellen...");
        executeStatement(getStationenQuery(), rs -> {
            int lastFallId = 0;
            Case fall = null;
            while (rs.next()) {
                int fallId = rs.getInt(1); // FALLID
                if (fall == null || fallId != lastFallId) {
                    fall = getFall(fallId);
                    if (fall == null) {
                        Integer fallId2 = getTranslation(fallId);
                        if (fallId2 != null) {
                            fall = getFall(fallId2);
                        }
                    }
                }
                if (fall != null) {
                    Date dateAdm = rs.getDate(2);
                    Department bew = OrbisToCpxTransformer.getBewegung(dateAdm, fall);
                    Date dateDis = rs.getDate(3);
                    String schotDesc = rs.getString(4);
                    String station = rs.getString(5);
                    Integer bewNr = rs.getInt(6);

                    Ward hosWard = new Ward(bew);
//                        hosWard.id = id;
                    hosWard.setVerlegungsdatum(dateAdm);
                    hosWard.setEntlassungsdatum(dateDis);
                    hosWard.setCode(schotDesc.replace(",", "")); // Kurzbez
                    //hosWard.setDescription(station.replace(",", "")); // Name
//                    if (bewNr != null) {
//                        hosWard.setBewWardNr(bewNr);
//                    }
//                    fall.addStation(hosWard, true);
//                        id++;
                }
                lastFallId = fallId;
            }
        });
    }

    @Override
    public void getDiagnosen() throws SQLException, IOException {
        executeStatement(getDiagnosenQuery(), rs -> {
            String version = null;
            String sekICD = null;
            int bewID = 0;
            int hosWardID = 0;
            StringBuilder sb = new StringBuilder();
            Case fall = null;
            Diagnose<?> code = null;
            int lastFallID = 0;
            int lastId = 0;
//            int cid = 0;
            boolean isSucc = false;
            while (rs.next()) {
                int fallId = rs.getInt(3); // FALLID
                if (fallId != lastFallID) {
//                    if (fall != null) {
//                        cid = writeDrgCodeValue(fall, fall.getIcds(), sb, /* uploadClient, */ cid, bewID, version);
//                        //CP-222 wenn dieser Fall der fuehrende Fall einer Fallzusammfuehrung ist und die icd-Liste der entgelte
//                        //weiterhin diagnosen enthaelt, wird der Folgefall die hier vorhanden diagnosen in Checkpoint immer wieder
//                        //eintragen (Problem in Heilbron), daher werden nach dem erstamligen einschrieben alle Listenwerte geloescht
//                        if (fall.getIcds() != null && !fall.getIcds().isEmpty()) {
//                            LOG.info("getDiagnosen()->writeDRGCodeValue->leere Fall-Icds-Liste mit: "
//                                    + fall.getIcds().size() + " Eintraegen, FallId: " + fall.getId()
//                                    + " FallIdExtern: " + fall.getIdExtern() + "FallIdIntern: " + fall.getIdIntern());
//                            fall.getIcds().clear();
//                        }
//                    }
                    fall = getFall(fallId);
                    if (fall == null) {
                        Integer fallId2 = getTranslation(fallId);
                        if (fallId2 != null) {
                            fall = getFall(fallId2);
                            fallId = Integer.valueOf(fall.getTpId());
                        }
                        isSucc = true;
                    } else {
                        isSucc = false;
                    }
                }
                if (fall != null) {
                    int id = rs.getInt(1); //FALL_DIAGNOSENID
                    String icd = rs.getString(2);  //DIAGKURZ
                    Integer fab = rs.getInt(9);  //OEBENEID
                    String hd = rs.getString(11);
                    Department b = getBewegung(fallId, fab);
                    Ward w = getStation(b.getVerlegungsdatum(), fall);
                    final Diagnose<?> diagnose;
                    if ("H".equalsIgnoreCase(hd)) {
                        diagnose = new Hauptdiagnose(b, w);
                    } else {
                        diagnose = new Nebendiagnose(b, w);
                    }
                    if (id != lastId) {
//                        if (code != null) {
//                            writeCodeValue(sb, code, version, sekICD /*, uploadClient */);
//                        } else {
//                            code = new KisCodeContainer();
//                        }
//                        cid++;
//                        code.setId(cid);                                     // ID
//                        code.setIdIntern(fall.getIdIntern());                       // fall_lokalid
//                        code.setIdExtern(fall.getIdExtern());                       // fall_externid
                        Department bew = getBewegung(String.valueOf(rs.getInt(10)), fall);
                        Ward ward = getStation(bew.getVerlegungsdatum(), fall);
                        code = new Nebendiagnose(bew, ward);
//                        Ward w = new Ward(bew);
                        code.setCode(icd);                // Code
//                        hosWardID = fall.getStationsID(rs.getInt(10));
//                        code.setBewId(bewID);  // BewegungsID
//                        code.setHosWardId(hosWardID);
//                        code.setLokalisation(getLokalisation(rs.getString(5)));     // LOKALISATION;
//                        code.setHd(0);                                         //HD Flag zurücksetzen
//                        code.setHdb(0);                                         //HDB Flag zurücksetzen
//                        code.setIsGr(0);                                        //grouping Flag zurücksetzen
//                        code.setDiagArt(0);
//                        version = getIcdVersion(rs.getDate(12));
                        sekICD = rs.getString(13);
                    }
                    Integer s = rs.getInt(8);
                    if (s != null && code != null) {
                        int ctype = s;
                        code.setIcdType(getDiagnoseartId(ctype));
                        //code.setDiagArt(getDiagnoseArt(ctype));
//                        if (!isSucc && code.getHd() == 0) {
//                            int hdx = getHDFlag(ctype, hd, fall.getPicd(), code.getCode()); //Hauptdiagnose
//                            code.setHd(hdx);
//                            if (hdx == 1) {
//                                code.setIsGr(1);
//                                fall.setPicd("X");
//                                fall.getIcds().remove(code.getCode());
//                            }
//                        }
//                        if (code.getHdb() == 0) {
//                            code.setHdb(getHDBFlag(ctype, hd)); //Hauptdiagnose
//                        }
//                        if (code.getIsGr() == 0) {
//                            code.setIsGr(fall.containsICD(code.getCode()));                     // mit groupen
//                        }
                    }
                    lastId = id;
                    lastFallID = fallId;
                }
            }
//            if (result) {
//                if (code != null) {
//                    writeCodeValue(sb, code, version, sekICD /*, uploadClient */);
//                }
//                result = uploadClient.finishLineUpload();
//            }
//            pI.percent(100);
        });
//        return result;
    }

    @Override
    public void getProzeduren() throws SQLException, IOException {
//        List<Object> row = new ArrayList<>();
        final List<Integer> lstBelegOP = new ArrayList<>();
        final List<Integer> lstBelegAna = new ArrayList<>();
        final List<Integer> lstBelegHeb = new ArrayList<>();
//        pI.percent(0);
//        pI.message(AppResources.getResource(AppResourceBundle.WAITING_ORBIS_PROC_CRIT_GET));
        //"Prozeduren-Belegkriterien in der ORBIS Datenbank zusammenstellen...");
        executeStatement(getProzedurenKritQuery(), rs -> {
            int kritVal = 0;
            Integer icpmid = null;
            BigDecimal kriID = null;
            String krit = null;
            while (rs.next()) {
                icpmid = rs.getInt(1);
                kriID = rs.getBigDecimal(2);
                krit = rs.getString(3);
                if (kriID != null && krit != null && "J".equals(krit)) {
                    kritVal = kriID.intValue();
                    switch (kritVal) {
                        case 1:
                            lstBelegOP.add(icpmid);
                            break;
                        case 2:
                            lstBelegAna.add(icpmid);
                            break;
                        case 3:
                            lstBelegHeb.add(icpmid);
                            break;
                        default:
                            break;
                    }
                }
            }
        });
//        pI.message(AppResources.getResource(AppResourceBundle.WAITING_ORBIS_PROC_GET));
        //"Prozeduren in der ORBIS Datenbank zusammenstellen...");
        executeStatement(getProzedurenQuery(), rs -> {
            /* boolean result = false, isOp, */
            boolean isOp;
            boolean isAna;
            boolean isHeb;
            Integer icpmid = null;
            int hosWardID = 0;
//            uploadClient.setUploadSize(size * 60); // Schätzung !
//            uploadClient.setTargetTableName(MultiFileImporter.imexTableNames[MultiFileImporter.imexOPS]);
//            if (m_useOracle) {
//                uploadClient.setTargetTableDef(MultiFileImporter.tableDefinitionsOra[MultiFileImporter.imexOPS]);
//            } else {
//                uploadClient.setTargetTableDef(MultiFileImporter.tableDefinitions[MultiFileImporter.imexOPS]);
//            }
//            Integer startID = uploadClient.getNextID(MultiFileImporter.targetTableNames[MultiFileImporter.imexOPS]);
//            uploadClient.startUpload(isCharTrans());
//            row = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
//            int i; //, id = (startID != null) ? startID : 1;
            int fallId;
            int lastFallId = 0;
            Date datum;
            String opsCode;
//            pI.message(AppResources.getResource(AppResourceBundle.WAITING_PROC_READ));
//"Prozeduren einlesen...");
//            result = true;
            Case fall = null;
            while (rs.next()) {
                icpmid = rs.getInt(1);
                fallId = rs.getInt(2);
                if (fallId != 0) {
                    if (fall == null || fallId != lastFallId) {
                        fall = getFall(fallId);
                        if (fall == null) {
                            Integer fallIdTmp = getTranslation(fallId);
                            if (fallIdTmp != null) {
                                fall = getFall(fallIdTmp);
                            }
                        }
                    }
                    if (fall != null) {
                        datum = rs.getDate(5);
                        Department dep = OrbisToCpxTransformer.getBewegung(datum, fall);
                        Ward ward = getStation(datum, fall);
                        Procedure proc = new Procedure(dep, ward);
                        opsCode = rs.getString(3);
                        proc.setDatum(datum);
                        proc.setCode(opsCode);
//                        sb.setLength(0);
//                        sb.append(id++);
//                        sb.append(';');
//                        sb.append(fall.getIdIntern());
//                        sb.append(';');
//                        sb.append(fall.getIdExtern());
//                        sb.append(";1;"); // doInsert
//                        sb.append(opsCode != null ? opsCode : "");
//                        sb.append(';');
//                        sb.append(fall.containsOPS(opsCode)); // toGroup
//                        sb.append(';');
                        if (lstBelegOP.contains(icpmid)) {
//                            sb.append('1'); // belegoperateur
                            isOp = true;
                        } else {
//                            sb.append('0'); // belegoperateur
                            isOp = false;
                        }
//                        sb.append(';');
                        if (lstBelegAna.contains(icpmid)) {
//                            sb.append('1'); // beleganaesthesist
                            isAna = true;
                        } else {
//                            sb.append('0'); // beleganaesthesist
                            isAna = false;
                        }
//                        sb.append(';');
                        if (lstBelegHeb.contains(icpmid)) {
//                            sb.append('1'); // beleghebamme
                            isHeb = true;
                        } else {
//                            sb.append('0'); // beleghebamme
                            isHeb = false;
                        }
//                        sb.append(';');
//                        sb.append(getDatumZeit(datum)); // datum
//                        sb.append(';');
                        sb.append(getOpsVersion(fall.getAufnahmedatum())); // opsversion
                        sb.append(';');
                        sb.append(getZusatzContainer(fallId));         // beatmungsstunden
                        sb.append(';');
//                        Department kbc = fall.getBewegung(datum, fall);
//i = fall.getBewegungsId(datum, fall);
//                        if (dep != null) {
//                            sb.append(dep.getId()); // bewegungsid
//                            if (!mProperties.isGwiDrgBelegtyp()) {
//                                if (dep.getTypschluessel() == 1 && isHeb) {
//                                    dep.setTypschluessel((byte) 2);
//                                } else if (dep.getTypschluessel() >= 3) {
//                                    if (isHeb && isAna && dep.getTypschluessel() < 6) {
//                                        dep.setTypschluessel((byte) 6);
//                                    } else if (isHeb && dep.getTypschluessel() < 5) {
//                                        dep.setTypschluessel((byte) 5);
//                                    } else if (isAna && dep.getTypschluessel() < 4) {
//                                        dep.setTypschluessel((byte) 4);
//                                    }
//                                }
//                            }
//                        }
//                        sb.append(';');
                        proc.setLokalisation(rs.getString(7));
                        //sb.append(getLokalisation(rs.getString(7)));  // lokalisation
//sb.append(getOfficialCode(opsCode, datum, getOpsVersion(fall.aufnahmeDatum)));
                        //sb.append(opsCode);
                        //sb.append(';');
//hosWardID
//                        hosWardID = fall.getStationsID(datum);
//                        sb.append(hosWardID);
//                        if (!uploadClient.sendLine(sb.toString())) {
//                            result = false;
//                            break;
//                        }

                    }
                    lastFallId = fallId;
                }
            }
//            if (result) {
//                result = uploadClient.finishLineUpload();
//            }
//            pI.percent(100);
        });
//        return result;
    }

    @Override
    protected String getProzedurenQuery() {
//        int results = 0;
//        connect();
//        if (connection != null) {
        String where = "WHERE f.FALLID=p.FALLID AND "
                + " pi.ICPMID=p.ICPMID AND "
                + getCasesWhere()
                + " AND p.STORNO_DATUM is NULL " + getCaseNumberWhere();
        String fromWhere = "FROM "
                + TABLESPACE + "FALL f, "
                + TABLESPACE + "FALL_ICPM p, "
                + TABLESPACE + "ICPM pi "
                + where;
//            Object o = connection.getSingleSQLResult("SELECT COUNT(*) " + fromWhere);
//            if (o != null && o instanceof Number) {
//                results = ((Number) o).intValue();
//            }

        final String query = "SELECT "
                + "p.FALLICPMID, "
                + //0
                "p.FALLID, "
                + //1
                "pi.ICPM, "
                + //2
                "p.OEBENEID, "
                + //3
                "p.DURCHF_DATUM, "
                + //4
                "p.OP_ID, "
                + //5
                "p.LOKALISATION "
                + //6
                "FROM " + TABLESPACE + "FALL f, "
                + TABLESPACE + "FALL_ICPM p, "
                + TABLESPACE + "ICPM pi "
                + where
                + " ORDER BY p.FALLID, p.FALLICPMID, p.DURCHF_DATUM";
        return query;
//        }
//        return results;
    }

    private String getProzedurenKritQuery() {
        String where = "WHERE f.FALLID=p.FALLID AND "
                + " p.fallicpmid=k.fallicpmid AND "
                + getCasesWhere()
                + " AND p.STORNO_DATUM is NULL " + getCaseNumberWhere();
        String fromWhere = "FROM "
                + TABLESPACE + "FALL f, "
                + TABLESPACE + "FALL_ICPM p, "
                + TABLESPACE + "fall_icpmkrit k "
                + where;
//            Object o = connection.getSingleSQLResult("SELECT COUNT(*) " + fromWhere);
//            if (o != null && o instanceof Number) {
//                results = ((Number) o).intValue();
//            }

        final String query = "SELECT "
                + "p.FALLICPMID, "
                + //0
                "k.ICPMKRITID, "
                + //1
                "k.ICPMKRITWERT "
                + //2
                fromWhere;
        return query;
//        }
//        return results;
    }

    @Override
    public void getDrg() {
        //not used
    }

    @Override
    protected Map<Integer, String> loadEntlassungsgrund12() throws SQLException, IOException {
//        if (m_importCasenumber != null && enlassung12 != null) {
//            return;
//        }
//        if (enlassung12 == null) {
//            enlassung12 = new HashMap<>();
//        }
//        List<Object> row = new ArrayList<>();
        final Map<Integer, String> enlassung12 = new HashMap<>();
        final String query = "SELECT ENTLASSARTID, P301_CD2 FROM " + TABLESPACE + "ENTLASSART";
        executeStatement(query, rs -> {
            while (rs.next()) {
                Integer key = rs.getInt(1);
                String val = getIntValue(rs.getString(2));
                enlassung12.put(key, val);
            }
        });
        return enlassung12;
    }

    @Override
    protected Map<Integer, String> loadEntlassungsgrund3() throws SQLException, IOException {
//        if (m_importCasenumber != null && enlassung3 != null) {
//            return;
//        }
//        if (enlassung3 == null) {
//            enlassung3 = new HashMap<>();
//        }
        final Map<Integer, String> enlassung3 = new HashMap<>();
        final String query = "SELECT ENTLFAEHIGID, P301_CD1 FROM " + TABLESPACE + "ENTLASSUNGSFAEHIG";
//        this.enlassung3.clear();
        executeStatement(query, rs -> {
            while (rs.next()) {
                Integer key = rs.getInt(1);
                String val = getIntValue(rs.getString(2));
                enlassung3.put(key, val);
            }
        });
        return enlassung3;
    }

    @Override
    protected Map<Integer, String> loadAufnahmegrund12() throws SQLException, IOException {
//        if (m_importCasenumber != null && aufnahme12 != null) {
//            return;
//        }
//        if (aufnahme12 == null) {
//            aufnahme12 = new HashMap<>();
//        }
        //aufnahme12.clear();
        final Map<Integer, String> aufnahme12 = new HashMap<>();
        final String query = "SELECT AUFNAHMEGRUNDID, P301_CD2 FROM " + TABLESPACE + "AUFNAHMEGRUND";
        executeStatement(query, rs -> {
            while (rs.next()) {
                Integer key = rs.getInt(1);
                String val = getIntValue(rs.getString(2));
                aufnahme12.put(key, val);
            }
        });
        return aufnahme12;
    }

    @Override
    protected Map<Integer, String> loadAufnahmegrund34() throws SQLException, IOException {
//        if (m_importCasenumber != null && aufnahme34 != null) {
//            return;
//        }
//        if (aufnahme34 == null) {
//            aufnahme34 = new HashMap<>();
//        }
        //this.aufnahme34.clear();
        final Map<Integer, String> aufnahme34 = new HashMap<>();
        final String query = "SELECT AUFNAHMEARTID, P301_CD2 FROM " + TABLESPACE + "AUFNAHMEART";
        executeStatement(query, rs -> {
            while (rs.next()) {
                Integer key = rs.getInt(1);
                String val = getIntValue(rs.getString(2));
                aufnahme34.put(key, val);
            }
        });
        return aufnahme34;
    }

    protected static String getIntValue(String pValue) {
        if (pValue == null) {
            return "";
        }
        int i = 0;
        try {
            i = Integer.parseInt(pValue);   // nachsehen, ob das ein gültiger int ist !
            return String.valueOf(i);
        } catch (NumberFormatException ex) {
            LOG.log(Level.FINEST, "cannot convert value from string to int: " + pValue, ex);
        }
        return "";
    }

    @Override
    protected Map<Integer, String> loadAufnahmeanlass() throws SQLException, IOException {
//        if (hasCaseNumbers() && aufnahmeAnlass != null) {
//            return;
//        }
//        if (aufnahmeAnlass == null) {
//            aufnahmeAnlass = new HashMap<>();
//        }
        //this.aufnahmeAnlass.clear();
        final Map<Integer, String> aufnahmeAnlass = new HashMap<>();
        String getAdmissionCauseDefinitonSql = "select dbuid " //, lid, cc validfrom,validto "
                + "from OS_SYS.CATALOGDEF "
                + "where lid='ADMISSIONCAUSE' and (cc='DE' or cc is null) and sysdate >= validfrom "
                + "and sysdate <= validto and rownum =1";
        Integer tmpDefinition = getSingleInt(getAdmissionCauseDefinitonSql);
        Integer parentId = null;
        if (tmpDefinition != null) {
            parentId = tmpDefinition;
            LOG.info("loadAufnahmeanlass->parentId fuer Aufnahmeanlass gesetzt: " + parentId.toString());
        } else {
            LOG.info("loadAufnahmeanlass->parentId fuer Aufnahmeanlass konnte NICHT bestimmt werden.");
        }

        if (parentId != null) {
            String admissionCausesSql = "select cat.dbuid, cat.language, cat.shorttext, cat.longtext "
                    + "from OS_SYS.CATALOGTEXT cat where cat.dbuid in ("
                    + "select dbuid from OS_SYS.CATALOGDEF where parentid=" + parentId + ")";
            executeStatement(admissionCausesSql, rs -> {
                while (rs.next()) {
                    Integer key = rs.getInt(1);
                    String val = rs.getString(3);
                    aufnahmeAnlass.put(key, val);
                    LOG.log(Level.INFO, "loadAufnahmeanlass->hinzugefuegt-> key: " + rs.getString(1)
                            + ", val: " + rs.getString(3) + ", language: " + rs.getString(2) + ", longtext: " + rs.getString(4)
                    );
                }
            });
        }
        return aufnahmeAnlass;
    }

    @Override
    protected Map<Integer, String> loadEinzugsgebiet() throws SQLException, IOException {
//        if (m_importCasenumber != null && einzugsgebiet != null) {
//            return;
//        }
//        if (einzugsgebiet == null) {
//            einzugsgebiet = new HashMap<>();
//        }
        //List<Object> row = new ArrayList<>();
//        this.einzugsgebiet.clear();
        final Map<Integer, String> einzugsgebiet = new HashMap<>();
        final String query = "SELECT EINZUGSID, EINZUGSGEBIET FROM " + TABLESPACE + "EINZUGSGEBIET";
        executeStatement(query, rs -> {
            while (rs.next()) {
                Integer key = rs.getInt(1);
                String val = getIntValue(rs.getString(2));
                einzugsgebiet.put(key, val);
            }
        });
        return einzugsgebiet;
    }

//    protected int getZusatzContainer(final String pPatientenNr) {
//        Object c = mZusatzHash.get(pPatientenNr);
//        int result = 0;
//        if (c != null && c instanceof Double) {
//            double exact = 0d;
//            exact = ((Number) c).doubleValue();
//            result = ((Number) c).intValue();
//            if ((exact - result) > 0.00000000001) {
//                result = result + 1;
//            }
//            return result;
//        } else {
//            return 0;
//        }
//    }
//    private void loadMandanten() {
//        if (mProperties.mGwiMandanten != null) {
//            mProperties.mVecMandanten = new ArrayList<>(mProperties.mGwiMandanten.keySet());
//            Collections.sort(mProperties.mVecMandanten);
//        }
//    }
    @Override
    public int getKisFallStatus(final Integer pFallId) {
        final int KIS_NOT_ACCOUNTED = 0;
        final int KIS_PART_ACCOUNTED = 1;
        final int KIS_COMPL_ACCOUNTED = 2;

//        int sid = 0;
//        String fallNr = pFallId;
//        //nicht abgerechnete Status ist im GWI als null in Fall.abrechstat gespeichert
//        if (fallNr
//                == null) {
//            fallNr = "0";
//        }
//        if (fallNr != null) {
//        try {
//            sid = Integer.parseInt(fallNr);
//        } catch (NumberFormatException ex) {
//            LOG.log(Level.WARNING, "Cannot interpret case number as integer: " + fallNr);
////                if (fallNr == null) {
////                    sid = 0;
////                }
//        }
        switch (pFallId) {
            case KIS_NOT_ACCOUNTED:
                return RmcCaseAdminMgr_rm.GWI_STATUS_OFFSET + RmcCaseAdminMgr_rm.KIS_GWI_NOT_ACCOUNTED;
            case KIS_PART_ACCOUNTED:
                return RmcCaseAdminMgr_rm.GWI_STATUS_OFFSET + RmcCaseAdminMgr_rm.KIS_GWI_PART_ACCOUNTED;
            case KIS_COMPL_ACCOUNTED:
                return RmcCaseAdminMgr_rm.GWI_STATUS_OFFSET + RmcCaseAdminMgr_rm.KIS_GWI_COMPL_ACCOUNTED;
            /*case KIS_NOT_ACCOUNTED:
     return RmcCaseAdminMgr_rm.GWI_STATUS_OFFSET + RmcCaseAdminMgr_rm.KIS_GWI_NOT_ACCOUNTED;
    case KIS_ACCOUNTED:
     return RmcCaseAdminMgr_rm.GWI_STATUS_OFFSET + RmcCaseAdminMgr_rm.KIS_GWI_ACCOUNTED;
    case KIS_CANCELED:
     return RmcCaseAdminMgr_rm.GWI_STATUS_OFFSET + RmcCaseAdminMgr_rm.KIS_GWI_CANCELED;
    case KIS_PART_ACCOUNTED:
     return RmcCaseAdminMgr_rm.GWI_STATUS_OFFSET + RmcCaseAdminMgr_rm.KIS_GWI_PART_ACCOUNTED;
    case KIS_COMPL_ACCOUNTED:
     return RmcCaseAdminMgr_rm.GWI_STATUS_OFFSET + RmcCaseAdminMgr_rm.KIS_GWI_COMPL_ACCOUNTED;
    case KIS_COLLECT_ACCOUNT:
     return RmcCaseAdminMgr_rm.GWI_STATUS_OFFSET + RmcCaseAdminMgr_rm.KIS_GWI_COLLECT_ACCOUNT;
    case KIS_TRANSFERED:
     return RmcCaseAdminMgr_rm.GWI_STATUS_OFFSET + RmcCaseAdminMgr_rm.KIS_GWI_TRANSFERED;
    case KIS_PLANED:
     return RmcCaseAdminMgr_rm.GWI_STATUS_OFFSET + RmcCaseAdminMgr_rm.KIS_GWI_PLANED;
    case KIS_PLANED_CANCEL:
     return RmcCaseAdminMgr_rm.GWI_STATUS_OFFSET + RmcCaseAdminMgr_rm.KIS_GWI_PLANED_CANCEL;
    case KIS_PAD:
     return RmcCaseAdminMgr_rm.GWI_STATUS_OFFSET + RmcCaseAdminMgr_rm.KIS_GWI_PAD;
    case KIS_TRANSFERED_END:
     return RmcCaseAdminMgr_rm.GWI_STATUS_OFFSET + RmcCaseAdminMgr_rm.KIS_GWI_TRANSFERED_END;
    case KIS_COMPARE:
     return RmcCaseAdminMgr_rm.GWI_STATUS_OFFSET + RmcCaseAdminMgr_rm.KIS_GWI_COMPARE;
    case KIS_NOT_ACCOUNTABLE:
     return RmcCaseAdminMgr_rm.GWI_STATUS_OFFSET + RmcCaseAdminMgr_rm.KIS_GWI_NOT_ACCOUNTABLE;
    case KIS_PAYMENT:
     return RmcCaseAdminMgr_rm.GWI_STATUS_OFFSET + RmcCaseAdminMgr_rm.KIS_GWI_PAYMENT;
    case KIS_DISK:
     return RmcCaseAdminMgr_rm.GWI_STATUS_OFFSET + RmcCaseAdminMgr_rm.KIS_GWI_DISK;*/
            default:
                return RmcCaseAdminMgr_rm.GWI_STATUS_OFFSET + RmcCaseAdminMgr_rm.KIS_STATUS_UNBEKANNT;
        }
//        }
//        return RmcCaseAdminMgr_rm.KIS_STATUS_UNBEKANNT;
    }

    @Override
    protected Map<Integer, KisAbteilungContainer<Integer>> loadAbteilungen() throws SQLException, IOException {
//        if (m_importCasenumber != null && abteilungenHash != null) {
//            return;
//        }
//        if (abteilungenHash == null) {
//            abteilungenHash = new HashMap<>();
//        }
//        connect();
        final Map<Integer, KisAbteilungContainer<Integer>> abteilungen = new HashMap<>();
//        if (connection != null) {
//            abteilungen.clear();
        String query = "SELECT ft.OEBENEID, ft.OEBENE_IDBEZ, fa.OEBENENAME, t.OTYP_ID "
                + "FROM OS_KERN.OEBENE_IDS ft "
                + "INNER JOIN OS_KERN.ORGAEBENE fa ON ft.OEBENEID = fa.OEBENEID "
                + "LEFT OUTER JOIN OS_KERN.ORGA_OTYP ot ON ot.OEBENEID=  fa.OEBENEID "
                + "LEFT OUTER JOIN OS_KERN.ORGA_TYP t ON t.OTYP_ID=ot.OTYP_ID "
                + "WHERE ft.OEBENE_IDTYPID=1 "
                + "ORDER BY ft.OEBENEID";
        executeStatement(query, rs -> {
            //            List<Object> row = new ArrayList<>();
            //Object oid, pname, oname;
            while (rs.next()) {
                Integer oid = rs.getInt(1);
                KisAbteilungContainer<Integer> a = abteilungen.get(oid);
                if (a == null) {
                    a = new KisAbteilungContainer<>();
                    String pname = rs.getString(2);
                    String oname = rs.getString(3);
                    if (oid != null && pname != null) {
                        a.setId(oid);
                        a.setP301(pname);
                        a.setName(oname);
                        if (a.getName() != null) {
                            a.setName(a.getName().replace(",", ""));
                        }
                        a.setBelegart(-1);
                    }
                    abteilungen.put(oid, a);
                }
                a.addOrgType(new BigDecimal(rs.getInt(4)));
            }
        });
//        }
        return abteilungen;
    }

    /**
     * Query über die Fälle
     */
    private String getFallPatientQuery(final String pIknr) {
        //int results = 0;
        String khJoin = "";
        String ikField = ", '' ";
//        String ikWhere = "";
        String costunitIdent = "";
        String patnrField = "";
        String patnrTable = "";
        StringBuilder ikWhere = new StringBuilder();
        if (mProperties.isGwiMandant()) {
            khJoin = "INNER JOIN " + TABLESPACE + "PM_MANDANT m ON f.MANDANTID=m.PERSNR "
                    + "INNER JOIN " + TABLESPACE + "GESELLSCHAFT kh ON kh.PERSNR=m.KH_PERSNR ";
            ikField = ",kh.IK_NR ";
            String[] iks = pIknr.split(",");
            ikWhere.append(" and kh.IK_NR IN (");
            for (int i = 0, n = iks.length; i < n; i++) {
                if (iks[i] != null) {
                    ikWhere.append("'" + iks[i].trim() + "'");
                    if (i < n - 1) {
                        ikWhere.append(",");
                    }
                }
            }
            ikWhere.append(")");
        }
        if (mProperties.isGwiCostunitIdent()) {
            costunitIdent = "AND K.IDENT='H' AND K.STORNODAT IS NULL ";
        }

        if (mProperties.isGwiRealPatientIdent()) {
            patnrField = "pf.FREMD_PERSNR, ";
            patnrTable = "LEFT OUTER JOIN OS_KERN.PERSON_FREMD pf ON p.PERSNR=pf.PERSNR ";
        } else {
            patnrField = "p.PERSNR, ";
            patnrTable = "";
        }

//        connect();
//        if (connection != null) {
        String where = "WHERE " + getCasesWhere() + ikWhere.toString() + getCaseNumberWhere();
        String fromWhere = "FROM " + TABLESPACE + "FALL f "
                + "INNER JOIN " + TABLESPACE + "NATPERSON p ON p.PERSNR=f.PERSNR "
                + "LEFT OUTER JOIN OS_KERN.FALL_KOTRAEG k ON k.FALLID=f.FALLID " + costunitIdent
                + "LEFT OUTER JOIN OS_KERN.GESELLSCHAFT g ON g.PERSNR=k.PERSNR "
                + "LEFT OUTER JOIN OS_KERN.ABZUR_LST l ON l.FALLID=f.FALLID "
                + "LEFT OUTER JOIN OS_KERN.PERS_KOTRAE pk ON k.PERS_KOTRAEID=pk.PERS_KOTRAEID "
                + patnrTable + khJoin
                + where;
//            Object o = connection.getSingleSQLResult("SELECT COUNT(*) " + fromWhere);
//            if (o != null && o instanceof Number) {
//                results = ((Number) o).intValue();
//            }
        String query = "SELECT DISTINCT f.FALLID, "
                + //0
                "f.FALLNR, "
                + //1
                "f.AUFNDAT, "
                + //2
                "f.ENTLDAT, "
                + //3
                "p.GEBDAT, "
                + //4
                "p.GESCHLECHT, "
                + //5
                "f.AUFNAHMEGEWICHT, "
                + //6
                "f.ENTLASSARTID, "
                + //7
                "f.EINZUGSID, "
                + //8
                "f.AUFNAHMEARTID, "
                + //9
                "f.AUFNAHMEGRUNDID, "
                + //10
                "f.ENTLFAEHIGID, "
                + //11
                patnrField
                + //12
                "p.NAME, "
                + //13
                "p.VORNAME, "
                + //14
                "g.IK_NR, "
                + //15
                "k.KOTGRUPID, "
                + //16
                "f.NEUGEBORENES, "
                + //17
                "l.STATUSID, "
                + //18
                "f.ABRECHSTAT "
                +//19
                ikField + ", "
                +//20
                "f.FAKTFREI, "
                +//21
                "pk.VERSNR, "
                +//22
                //CP-159 Aufnahmeanlass fehlte bisher
                "f.ADMISSIONCAUSE "
                +//23
                fromWhere
                + " ORDER BY f.FALLNR";
        return query;
//        }
        //return results;
    }

    private String getCasesWhere() {
        String where = getDateWhere() + " AND (f.FALLTYP<>0 AND f.FALLTYP<>3 AND f.FALLTYP<>4 AND f.FALLTYP<>7) "
                + getAufnahmeArten() + getAufnahmegruendeSQL();
        return where;
    }

    private String getAufnahmeArten() {
//        String result = "";
//        if (hasAdmissionCauses()) {
//            pAufnahmeArten = pAufnahmeArten.replaceAll("[\\(\\)]", "");
//            if (pAufnahmeArten.indexOf(AppResources.getResource(AppResourceBundle.TXT_ALL_UPPER)) < 0) {
//                String a[] = pAufnahmeArten.split(",[\\s*]");
//                pAufnahmeArten = "";
//                for (int i = 0; i < a.length; i++) {
//                    pAufnahmeArten += "'" + a[i] + "'";
//                    if (i < (a.length - 1)) {
//                        pAufnahmeArten += ",";
//                    }
//                }
//                result = " AND f.AKTSTATNR IN (" + pAufnahmeArten + ")";
//            }
        if (!hasAdmissionCauses()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (AdmissionCauseEn value : getAdmissionCauses()) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(value.getIdStr());
        }
        return " AND f.AKTSTATNR IN (" + sb.toString() + ")";
//        }
//        return result;
    }

    private String getDiagnoseartenSQL() {
        if (!hasIcdTypes()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (IcdcTypeEn value : getIcdTypes()) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(value.getIdStr());
        }
        return " AND da.DIAGARTID IN (" + sb.toString() + ")";
//        String result = "";
//        if (pDiagArten != null && pDiagArten.length() > 0 && !pDiagArten.equals(AppResources.getResource(AppResourceBundle.TXT_ALL_UPPER))) {
//            pDiagArten = pDiagArten.replaceAll("[\\(\\)]", "");
//            if (pDiagArten.indexOf(AppResources.getResource(AppResourceBundle.TXT_ALL_UPPER)) < 0) {
//                String a[] = pDiagArten.split(",[\\s*]");
//                pDiagArten = "";
//                for (int i = 0; i < a.length; i++) {
//                    pDiagArten += "'" + a[i] + "'";
//                    if (i < (a.length - 1)) {
//                        pDiagArten += ",";
//                    }
//                }
//                result = " AND da.DIAGARTID IN (" + pDiagArten + ")";
//            }
//        }
//        return result;
    }

    private String getAufnahmegruendeSQL() {
//        String result = "";
//        if (pAufnahmeArten != null && pAufnahmeArten.length() > 0 && !pAufnahmeArten.equals(AppResources.getResource(AppResourceBundle.TXT_ALL_UPPER))) {
//            pAufnahmeArten = pAufnahmeArten.replaceAll("[\\(\\)]", "");
//            if (pAufnahmeArten.indexOf(AppResources.getResource(AppResourceBundle.TXT_ALL_UPPER)) < 0) {
//                String a[] = pAufnahmeArten.split(",[\\s*]");
//                pAufnahmeArten = "";
//                for (int i = 0; i < a.length; i++) {
//                    pAufnahmeArten += "'" + a[i] + "'";
//                    if (i < (a.length - 1)) {
//                        pAufnahmeArten += ",";
//                    }
//                }
//                result = " AND f.AUFNAHMEGRUNDID IN (" + pAufnahmeArten + ")";
//            }
//        }
//        return result;
        if (!hasAdmissionReasons()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (AdmissionReasonEn value : getAdmissionReasons()) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(value.getIdStr());
        }
        return " AND f.AUFNAHMEGRUNDID IN (" + sb.toString() + ")";
    }

    private String getDateWhere() {
        String date = "";
        if (!mAufnahmedatumVon.isEmpty()) {
            date = "f.AUFNDAT >= " + mAufnahmedatumVon;
        }
        if (!mAufnahmedatumBis.isEmpty()) {
            if (!date.isEmpty()) {
                date += " AND ";
            }
            date += "f.AUFNDAT < " + mAufnahmedatumBis;
        }
        if (!mEntlassungsdatumVon.isEmpty()) {
            if (!date.isEmpty()) {
                date += " AND ";
            }
            date += "f.ENTLDAT > " + mEntlassungsdatumVon;
        }
        if (!mEntlassungsdatumBis.isEmpty()) {
            if (!date.isEmpty()) {
                date += " AND ";
            }
            date += "f.ENTLDAT < " + mEntlassungsdatumBis;
        }
        if (date.length() == 0) {
            return "1=1";
        } else {
            return date;
        }
    }

    private String getCaseNumberWhere() {
        if (!hasCaseNumbers()) {
            return "";
        }
        StringBuilder sb = new StringBuilder(" AND (");
        for (String caseNumber : getCaseNumbers()) {
            if (sb.length() > 0) {
                sb.append(" OR ");
            }
            sb.append("F.FALLNR = '" + caseNumber + "' ");
        }
        sb.append(")");
        //        if (m_importCasenumber != null) {
        //            return " AND F.FALLNR='" + m_importCasenumber + "' ";
        //        }
        return sb.toString();
    }

    @Override
    protected String getUrlaubQuery() {
//        int results = 0;
//        connect();
//        if (connection != null) {
        String where = "FROM " + TABLESPACE + "FALL_URLAUB u "
                + "INNER JOIN " + TABLESPACE + "FALL f "
                + " ON f.FALLID=u.FALLID "
                + "WHERE "
                + getCasesWhere() + getCaseNumberWhere();
//            String query = "SELECT COUNT(*) "
//                    + where;
//            Object o = connection.getSingleSQLResult(query);
//            if (o != null && o instanceof Number) {
//                results = ((Number) o).intValue();
//            }

        final String query = "SELECT "
                + "u.FALLID, "
                + //0
                "u.URLAUB_AB, "
                + //1
                "u.URLAUB_BIS "
                + //2
                where
                + " ORDER BY u.FALLID";
        return query;
//        }
//        return results;

    }

    private String getDrgIcdQuery() {
//        int results = 0;
//        connect();
//        if (connection != null) {
        String fromWhere = "FROM " + TABLESPACE + "FALL_DRG d "
                + "INNER JOIN " + TABLESPACE + "FALL f ON f.FALLID=d.FALLID "
                + "INNER JOIN " + TABLESPACE + "DRG_ICD da ON da.FALL_DRGID=d.FALL_DRGID "
                + "INNER JOIN " + TABLESPACE + "DIAGNOSEN di ON di.DIAGNOSEID = da.DIAGNOSEID "
                + //"LEFT OUTER JOIN OS_KERN.MDC  M ON M.MDCID = D.MDCID  " +
                //"LEFT OUTER JOIN OS_CW.CW_FALLDRGVIDIERUNG CW ON CW.FALLID = f.FALLID " +
                "WHERE "
                + getCasesWhere() + getCaseNumberWhere()
                + " AND da.ICDVALIDITYFLAG<>3"
                + " AND D.DRGSTATUSID = (SELECT MAX(D2.DRGSTATUSID) FROM OS_KERN.FALL_DRG D2 WHERE D2.FALLID = f.FALLID) ";
//            String query = "SELECT COUNT(*) "
//                    + fromWhere;
//            Object o = connection.getSingleSQLResult(query);
//            if (o != null && o instanceof Number) {
//                results = ((Number) o).intValue();
//            }
        final String query = "SELECT d.FALLID, "
                + //0
                "di.DIAGKURZ, "
                + //1
                "da.PRINCIPAL "
                + //2
                fromWhere
                + " ORDER BY d.FALLID";
        return query;
//        }
//        return results;
    }

    private String getDrgOpsQuery() {
//        int results = 0;
//        connect();
//        if (connection != null) {
        String fromWhere = "FROM " + TABLESPACE + "FALL_DRG d "
                + "INNER JOIN " + TABLESPACE + "FALL f ON f.FALLID=d.FALLID "
                + "INNER JOIN " + TABLESPACE + "DRG_OPS da ON da.FALL_DRGID=d.FALL_DRGID "
                + "INNER JOIN " + TABLESPACE + "ICPM di ON di.ICPMID = da.ICPMID "
                + "WHERE "
                + getCasesWhere() + getCaseNumberWhere()
                + " AND D.DRGSTATUSID = (SELECT MAX(D2.DRGSTATUSID) FROM OS_KERN.FALL_DRG D2 WHERE D2.FALLID = f.FALLID) ";
//            String query = "SELECT COUNT(*) "
//                    + fromWhere;
//            Object o = connection.getSingleSQLResult(query);
//            if (o != null && o instanceof Number) {
//                results = ((Number) o).intValue();
//            }
        final String query = "SELECT d.FALLID, "
                + //0
                "di.ICPM "
                + //1
                fromWhere
                + " ORDER BY d.FALLID";
        return query;
//        }
//        return results;
    }

    @Override
    public Map<Integer, Integer> loadDrgBeleg() throws SQLException, IOException {
//        boolean result = true;
//        int count = 0;
        //List<Object> row = new ArrayList<>();
        final Map<Integer, Integer> drgBelegMap = new HashMap<>();
        if (!mProperties.isGwiDrgBelegtyp()) {
            return drgBelegMap;
        }

//        pI.percent(0);
//        pI.message(AppResources.getResource(AppResourceBundle.WAITING_ORBIS_FEE_DRG_GET,
//                "Belegungstypen für Bewegungen in der ORBIS Datenbank zusammenstellen..."));
        //"Entgelte für Diagnosen in der ORBIS Datenbank zusammenstellen...");
        executeStatement(getDrgBelegQuery(), rs -> {
            Integer fallID;
            Integer belegTyp = null;

//        pI.message(AppResources.getResource(AppResourceBundle.WAITING_ORBIS_FEE_DRG_LOAD,
//                "Belegungstypen für Bewegungen in der ORBIS Datenbank laden..."));
//"Entgelte für Diagnosen in der ORBIS Datenbank laden...");
//        pI.percent(0);
            while (rs.next()) {
//                count++;
//            pI.percent((int) ((count * 100) / (long) size));
                fallID = rs.getInt(1); // FALLID
                belegTyp = rs.getInt(2);
                if (fallID != null && belegTyp != null) {
                    drgBelegMap.put(fallID, belegTyp);
                }
            }
        });
        return drgBelegMap;
    }

    private String getDrgBelegQuery() {
//        int results = 0;
//        connect();
//        if (connection != null) {
        String fromWhere = "FROM " + TABLESPACE + "FALL_DRG d "
                + "INNER JOIN " + TABLESPACE + "FALL f ON f.FALLID=d.FALLID "
                + "WHERE "
                + getCasesWhere() + getCaseNumberWhere()
                + " AND D.DRGSTATUSID = (SELECT MAX(D2.DRGSTATUSID) FROM OS_KERN.FALL_DRG D2 WHERE D2.FALLID = f.FALLID) ";
//            String query = "SELECT COUNT(*) "
//                    + fromWhere;
//            Object o = connection.getSingleSQLResult(query);
//            if (o != null && o instanceof Number) {
//                results = ((Number) o).intValue();
//            }
        final String query = "SELECT d.FALLID, "
                + //0
                "d.DRGBELEGTYPID "
                + //1
                fromWhere
                + " ORDER BY d.FALLID";
        return query;
//        }
//        return results;
    }

    @Override
    protected String getBewegungenQuery() {
//        int results = 0;
//        connect();
//        if (connection != null) {
        String where = "WHERE f.FALLID=a.FALLID AND "
                + getCasesWhere() + getCaseNumberWhere();
        String fromWhere = "FROM " + TABLESPACE + "FALL f, "
                + TABLESPACE + "FALL_AUFENTHALT a "
                + where;
//            Object o = connection.getSingleSQLResult("SELECT COUNT(*) " + fromWhere);
//            if (o != null && o instanceof Number) {
//                results = ((Number) o).intValue();
//            }
        final String query
                = "SELECT a.FALLID, "
                + //0
                "a.BEGINN, "
                + //1
                "a.ENDE, "
                + //2
                "a.OEBENEID_DEPARTMENT, "
                + //3
                "a.OEBENEID_STATION, "
                + //4
                "a.STAYNR " //5
                + fromWhere
                + " ORDER BY a.FALLID, a.BEGINN";
        return query;
//        }
//        return results;
    }

    @Override
    protected String getStationenQuery() {
//        int results = 0;
//        connect();
//        if (connection != null) {
        String where = "WHERE f.FALLID=a.FALLID AND a.OEBENEID_STATION=s.OEBENEID AND "
                + getCasesWhere() + getCaseNumberWhere();
        String fromWhere = "FROM " + TABLESPACE + "FALL f, "
                + TABLESPACE + "FALL_AUFENTHALT a, "
                + TABLESPACE + "ORGAEBENE s "
                + where;
//            Object o = connection.getSingleSQLResult("SELECT COUNT(*) " + fromWhere);
//            if (o != null && o instanceof Number) {
//                results = ((Number) o).intValue();
//            }
        final String query
                = "SELECT a.FALLID, "
                + //0
                "a.BEGINN, "
                + //1
                "a.ENDE, "
                + //2
                "s.OEBENEKURZ, "
                + //3
                "s.OEBENENAME, "
                + //4
                "a.STAYNR " //5
                + fromWhere
                + " ORDER BY a.FALLID, a.BEGINN";
        return query;
//        }
//        return results;
    }

    public int getHDBFlag(final int pArt, final String pHd) {
        if (pHd != null) {
            if (pArt == 5 && "H".equals(pHd)) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public int getHDFlag(final int pArt, final String pHd, final String pFallHD, final String pCode) {
        if (pFallHD == null) {
            if (pHd != null) {
                if (pArt == 5 && "H".equals(pHd)) {
                    return 1;
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        } else {
            if (pCode.equals(pFallHD)) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    private static int getDiagnoseartId(final int pVal) {
        switch (pVal) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 13;
            case 4:
                return 14;
            case 5:
                return 4;
            case 6:
                return 8;
            case 7:
                return 7;
            case 9:
                return 15;
            case 10:
                return 5;
            case 11:
                return 6;
            case 12:
                return 16;
            default:
                return 5;
        }
    }

//    private static void writeCodeValue(final StringBuilder sb, final KisCodeContainer code,
//            final String version, final String sekICD /*, UploadClient uploadClient */) {
//        sb.setLength(0);
//        sb.append(code.getId());
//        sb.append(';');
//        sb.append(code.getIdIntern());
//        sb.append(';');
//        sb.append(code.getIdExtern());
//        sb.append(";1;");                  // doInsert
//        sb.append(code.getCode());
//        sb.append(';');
//        sb.append(code.getLokalisation());   // lokalisation
//        sb.append(';');
//        sb.append(code.getHd());       // Hauptdiagnose
//        sb.append(';');
//        sb.append(code.getHdb());       // Hauptdiagnose Bewegung
//        sb.append(';');
//        sb.append(code.getIsGr());   // mit groupen
//        sb.append(';');
//        sb.append(version);
//        sb.append(';');
//        // sekundaericd
//        if (sekICD != null) {
//            sb.append(sekICD);
//            sb.append(";0;");                   // sekundaerlokalisation
//        } else {
//            sb.append(";;");                   // sekundaerlokalisation
//        }
//        sb.append(code.getBewId());                   // bewegungsid
//        sb.append(';');
//        sb.append(code.getDiagArt()); // diagnoseart
//        sb.append(';');
//        sb.append(code.getHosWardId()); // hosWardID
//        sb.append(';');//ref_type
//        //return uploadClient.sendLine(sb.toString());
//    }
//    private static int writeDrgCodeValue(final Case fall, final List<String> icds,
//            final StringBuilder sb, /* UploadClient uploadClient, */ final int pCid,
//            final int bewID, final String version) {
//        int cid = pCid;
//        if (icds != null) {
//            int size = icds.size();
//            if (size > 0) {
//                KisCodeContainer code = new KisCodeContainer();
//                code.setIdIntern(fall.getIdIntern());                       // fall_lokalid
//                code.setIdExtern(fall.getIdExtern());                       // fall_externid
//                code.setBewId(bewID);  // BewegungsID
//                for (int i = 0; i < size; i++) {
//                    String icd = icds.get(i);
//                    cid++;
//                    code.setId(cid);                                     // ID
//                    code.setCode(icd);                // Code
//                    code.setLokalisation(getLokalisation(null));     // LOKALISATION;
//                    if (icd.equals(fall.getPicd())) {                       //HD Flag zurücksetzen
//                        code.setHd(1);
//                        fall.setPicd("X");
//                    } else {
//                        code.setHd(0);
//                    }
//                    code.setHdb(0);                                         //HDB Flag zurücksetzen
//                    code.setIsGr(1);                                        //grouping Flag zurücksetzen
//                    code.setDiagArt(6);     //Diagnoseart DRG
//                    writeCodeValue(sb, code, version, null /*, uploadClient */);
//                }
//            }
//        }
//        return cid;
//    }
    @Override
    protected String getDiagnosenQuery() {
//        int results = 0;
//        connect();
//        if (connection != null) {
        String where = "WHERE "
                + getCasesWhere()
                + " AND da.STORNO_DATUM is NULL "
                + getDiagnoseartenSQL() + getCaseNumberWhere();
        String fromWhere = "FROM "
                + TABLESPACE + "FALL f "
                + "INNER JOIN " + TABLESPACE + "FALL_DIAGNOSEN d ON f.FALLID=d.FALLID "
                + "INNER JOIN " + TABLESPACE + "FALL_DIAG_ARTEN da ON da.FALL_DIAGNOSENID=d.FALL_DIAGNOSENID "
                + "INNER JOIN " + TABLESPACE + "DIAGNOSEN DI ON DI.DIAGNOSEID = D.DIAGNOSEID "
                + "LEFT OUTER JOIN " + TABLESPACE + "DIAGNOSEN DIX ON DIX.DIAGNOSEID = D.SEKDIAGNOSEID "
                + where;
//            String query = "SELECT COUNT(*) " + fromWhere;
//            Object o = connection.getSingleSQLResult(query);
//            if (o != null && o instanceof Number) {
//                results = ((Number) o).intValue();
//            }

        final String query = "SELECT "
                + "d.FALL_DIAGNOSENID, "
                + //0
                "di.DIAGKURZ, "
                + //1
                "d.FALLID, "
                + //2
                "d.DIAGNOSEID, "
                + //3
                "d.LOKALISATION, "
                + //4
                "d.ZUORD, "
                + //5
                "d.SEKDIAGNOSEID, "
                + //6
                "da.DIAGARTID, "
                + //7
                "da.OEBENEID, "
                + //8
                "da.FESTSTDATUM, "
                + //9
                "da.HAUPTNEBEN, "
                + //10
                "f.AUFNDAT, "
                + //11
                "dix.DIAGKURZ "
                + //12
                fromWhere
                + " ORDER BY d.FALLID, d.FALL_DIAGNOSENID, da.FESTSTDATUM";
        return query;
//        }
//        return results;
    }

//    @Override
//    protected Map<Integer, Map<String, String>> loadPossibleParams() {
//        Map<Integer, Map<String, String>> params = new HashMap<>();
//        //GWI
//        addImportParam(ORBIS_KIS, POSSIBLE_HOSWARD, POSSIBLE_HOSWARD);
//        addImportParam(ORBIS_KIS, POSSIBLE_LABOR, POSSIBLE_LABOR);
//        //Medico
//        addImportParam(MEDICO_KIS, POSSIBLE_ENTGELT, POSSIBLE_ENTGELT);
//        addImportParam(MEDICO_KIS, POSSIBLE_SVSN_DIAGPROC, POSSIBLE_SVSN_DIAGPROC);
//        addImportParam(MEDICO_KIS, POSSIBLE_LABOR, POSSIBLE_LABOR);
//        //ISoft
//        addImportParam(ISOFT, POSSIBLE_ENTGELT, POSSIBLE_ENTGELT);
//        //Kismed
//        addImportParam(KISSMED_KIS, POSSIBLE_LABOR, POSSIBLE_LABOR);
//        //Nexus
//        addImportParam(NEXUS, POSSIBLE_HOSWARD, POSSIBLE_HOSWARD);
//        addImportParam(NEXUS, POSSIBLE_LABOR, POSSIBLE_LABOR);
//        return params;
//    }
//    protected double getBeatmungsDauer4MultiFaelle(final String pFallnr) {
//        Object c = zusatzHash.get(pFallnr);
//        if (c != null && c instanceof Double) {
//            return ((Number) c).doubleValue();
//        } else {
//            return 0d;
//        }
//    }
//    @Override
//    protected Map<Integer, ZusatzContainer> loadZusatzContainer() throws SQLException {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//    @Override
//    protected Map<String, Integer> loadEscort() throws SQLException {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    protected Map<Integer, BigDecimal> loadVwdIntensivdauer() throws SQLException {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    protected Map<Integer, Vidierstufe> loadVidierstufen() throws SQLException {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
    @Override
    protected Map<Integer, BigDecimal> loadVwdIntensivdauer() throws SQLException, IOException {
//        connect();
        final Map<Integer, BigDecimal> vwdIntensivMap = new HashMap<>();
        if (mProperties.getGwiIntensivtypIdent() != null && !mProperties.getGwiIntensivtypIdent().trim().isEmpty()) {
//            m_vwdIntensivMap.clear();
            String where = "WHERE " + getCasesWhere() + " AND t.otyp_id in (" + mProperties.getGwiIntensivtypIdent() + ") AND a.ende is not null "
                    + " AND a.beginn is not null " + getCaseNumberWhere();
            String select = "SELECT DISTINCT "
                    + "a.FALLID"
                    + //0
                    ", a.fall_aufenthaltid "
                    + //1
                    ", a.ende-a.beginn "
                    + //2
                    "FROM " + TABLESPACE + "FALL f "
                    + "INNER JOIN " + TABLESPACE + "FALL_AUFENTHALT a ON f.FALLID=a.FALLID "
                    + "INNER JOIN " + TABLESPACE + "ORGA_OTYP ot ON ot.oebeneid = a.oebeneid_station "
                    + "INNER JOIN " + TABLESPACE + "ORGA_TYP t ON t.otyp_id = ot.otyp_id "
                    + where
                    + " ORDER BY A.FALLID";
            executeStatement(select, rs -> {
                Integer fallid;
                Integer dauerObj; //, lastFallID;
                double dauer;
                while (rs.next()) {
                    fallid = rs.getInt(1);
                    if (fallid != null) {
                        dauerObj = rs.getInt(3);
                        if (dauerObj != null) {
                            dauer = dauerObj.doubleValue();
                            BigDecimal c = vwdIntensivMap.get(fallid);
                            if (c != null) {
                                dauer = dauer + c.doubleValue();
                            }
                            vwdIntensivMap.put(fallid, BigDecimal.valueOf(dauer));
                        }
                    }
                }
            });
//            connection.closeLastResultSet();
        }
        return vwdIntensivMap;
    }

    @Override
    protected Map<String, Integer> loadEscort() throws SQLException, IOException {
        //escortMap = new HashMap<>();
        final Map<String, Integer> escortMap = new HashMap<>();
        executeStatement(getEscortPersonQuery(), rs -> {
            //            List<Object> row = new ArrayList<>();
            while (rs.next()) {
                Integer fall1 = rs.getInt(1);
                String obj = rs.getString(2);
                if (obj != null) {
                    escortMap.put(obj, fall1);
                }
            }
//            connection.closeLastResultSet();
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, "Fehler beim Laden der Begleitpersonen");
        });
        return escortMap;
    }

    private String getEscortPersonQuery() {
//        int results = 0;
//        connect();
//        if (connection != null) {
        String where = "WHERE " + getCasesWhere()
                + " AND ff.VERWEISARTID=6 " + getCaseNumberWhere();
//        String fromWhere = "FROM " + TABLESPACE + "FALL f "
//                + "INNER JOIN " + TABLESPACE + "FALL_FALL ff ON ff.FALL1ID=f.FALLID "
//                + "INNER JOIN " + TABLESPACE + "FALL f2 ON ff.FALL2ID=f2.FALLID "
//                + where;
//            Object o = connection.getSingleSQLResult("SELECT COUNT(*) " + fromWhere);
//            if (o != null && o instanceof Number) {
//                results = ((Number) o).intValue();
//            }
        final String query = "SELECT ff.FALL1ID, "
                + //0
                "f2.FALLNR, "
                + //1
                "ff.VERWEISARTID "
                + //2
                "FROM " + TABLESPACE + "FALL f "
                + "INNER JOIN " + TABLESPACE + "FALL_FALL ff ON ff.FALL1ID=f.FALLID "
                + "INNER JOIN " + TABLESPACE + "FALL f2 ON ff.FALL2ID=f2.FALLID "
                + where
                + " ORDER BY f.FALLNR";
//   System.out.println(query);
        return query;
//        }
//        return results;
    }

    @Override
    protected Map<Integer, ZusatzContainer<Integer>> loadZusatzContainer() throws SQLException, IOException {
//        connect();
//        if (connection != null) {
        final Map<Integer, ZusatzContainer<Integer>> beatmungsdauerMap = new HashMap<>();
//        resetBeatmungsHash();
        String where = "WHERE " + getCasesWhere() + getCaseNumberWhere();
        final String select = "SELECT "
                + "b.FALLID"
                + //0
                ", b.DAUER "
                + //1
                ", b.DAUER_EINHEITID "
                + //2
                ", b.INTRAOP "
                + "FROM " + TABLESPACE + "FALL_BEATMUNG b "
                + "INNER JOIN " + TABLESPACE + "FALL f ON f.FALLID=b.FALLID "
                + where
                + " ORDER BY FALLID";
//        connection.openResultSet(select);
//        List<Object> row = new ArrayList<>();
        executeStatement(select, rs -> {
            Integer fallid;
            Integer dauerObj;
            Integer einheit;
            Integer intraop; //lastFallID,
            int dauer;

            while (rs.next()) {
                fallid = rs.getInt(1);
                if (fallid != null) {
                    dauerObj = rs.getInt(2);
                    intraop = rs.getInt(4);
                    if (dauerObj != null) {
                        dauer = dauerObj;
                        einheit = rs.getInt(3);
//                        try {
                        int ein = einheit;
                        if (ein == 20 && dauer > 0) {
                            dauer = dauer / 60;
                        }
//                        } catch (Exception ex) {
//                        }
                        if (intraop == null || !"J".equalsIgnoreCase(intraop.toString())
                                || ("J".equalsIgnoreCase(intraop.toString()) && dauer >= 24)) {
                            ZusatzContainer<Integer> c = beatmungsdauerMap.get(fallid);
                            if (c != null) {
                                dauer = dauer + c.getBeatmungsdauer();
                            }
                            c = new ZusatzContainer<>();
                            c.setBeatmungsdauer(dauer);
                            beatmungsdauerMap.put(fallid, c);
                        }
                    }
                }
            }
        });
        return beatmungsdauerMap;
//        connection.closeLastResultSet();
//        }
    }

    @Override
    protected Map<Integer, Vidierstufe> loadVidierstufen() throws SQLException, IOException {
        final Map<Integer, Vidierstufe> vidierstufenMap = new HashMap<>();
        executeStatement(getVidierQuery(), rs -> {
            //            pI.message("Lade Vidierungsstufen (" + size + ")");
//            pI.percent(0);
//            if (size > 0) {
//            int count = 0;
//                List<Object> row = new ArrayList<>();
            while (rs.next()) {
//                count++;
//                    pI.percent((int) ((count * 100) / (long) size));
                Vidierstufe v = new Vidierstufe();
                v.setFall(rs.getInt(1));
                v.setMinFaVid(rs.getInt(2));
                v.setFallVid(rs.getInt(3));
                v.setDrgVid(rs.getInt(4));
                vidierstufenMap.put(v.getFall(), v);
            }
//            }
//            connection.closeLastResultSet();
            executeStatement(getVidierFabQuery(), (ResultSet rs2) -> {
                //            pI.message("Lade Vidierungsstufen für Fachabteilungen (" + size + ")");
//            pI.percent(0);
//            if (size > 0) {
//                int count = 0;
//                List<Object> row = new ArrayList<>();
                Vidierstufe obj;
                Vidierstufe v;
                while (rs2.next()) {
//                    count++;
//                    pI.percent((int) ((count * 100) / (long) size));
                    Integer fall = rs2.getInt(1);
                    obj = vidierstufenMap.get(fall);
                    if (obj != null) {
                        v = obj;
                        v.setMinFaVid(null);
                    }
                }
            });
        });
        return vidierstufenMap;
//            connection.closeLastResultSet();
//        } catch (Exception ex) {
//            pI.message("Fehler beim Laden der Vidierstufen" + ex.getMessage());
//            LOG.log(Level.SEVERE, "Fehler beim Laden der Vidierstufen" + ex.getMessage());
//        }
    }

    private String getVidierQuery() {
//        int results = 0;
//        connect();
//        if (connection != null) {
        String where = "WHERE " + getCasesWhere() + getCaseNumberWhere();
        String from = "FROM " + TABLESPACE + "FALL f "
                + "INNER JOIN " + TABLESPACE + "FALL_FACHRICHTUNG FA ON (FA.FALLID=F.FALLID) "
                + "LEFT OUTER JOIN " + TABLESPACE_CW + "CW_FALLFACHRICHTVIDIERUNG FAV ON "
                + "(FAV.FALL_FACHRICHTID=FA.FALL_FACHRICHTID AND FAV.AKTIV=1 AND NVL(FAV.STORNO,0)=0) "
                + "LEFT OUTER JOIN " + TABLESPACE_CW + "CW_FALLVIDIERUNG FV ON (FV.FALLID=F.FALLID AND FV.AKTIV=1 AND NVL(FV.STORNO,0)=0) "
                + "LEFT OUTER JOIN " + TABLESPACE_CW + "CW_FALLDRGVIDIERUNG DV ON (DV.FALLID=F.FALLID AND DV.AKTIV=1 AND  NVL(DV.STORNO,0)=0) ";
        String group = "GROUP BY F.FALLID, FV.HISTVIDIERSTUFE, DV.HISTVIDIERSTUFE ";
        String query = "SELECT f.FALLID, "
                + //0
                "MIN(FAV.HISTVIDIERSTUFE), "
                + //1 minimale Fachabt Vidierung
                "FV.HISTVIDIERSTUFE, "
                +//2 Fallvidierung
                "DV.HISTVIDIERSTUFE "
                +//3 DRG Vidierung
                from + where + group;
//            Object o = connection.getSingleSQLResult("SELECT COUNT(*) FROM (" + query + ")");
//            if (o != null && o instanceof Number) {
//                results = ((Number) o).intValue();
//            }
        query += " ORDER BY f.FALLID, MIN(FAV.HISTVIDIERSTUFE)";
        return query;
//            connection.openResultSet(query, false);
//        }
//        return results;
    }

    private String getVidierFabQuery() {
//        int results = 0;
//        connect();
//        if (connection != null) {
        String where = "WHERE " + getCasesWhere() + getCaseNumberWhere();
        String whereExtension = " AND fav.FALL_FACHRICHTID is null ";
        String from = "FROM " + TABLESPACE + "FALL f "
                + "INNER JOIN " + TABLESPACE + "FALL_FACHRICHTUNG FA ON (FA.FALLID=F.FALLID) "
                + "LEFT OUTER JOIN " + TABLESPACE_CW + "CW_FALLFACHRICHTVIDIERUNG FAV ON "
                + "(FAV.FALL_FACHRICHTID=FA.FALL_FACHRICHTID AND FAV.AKTIV=1 AND NVL(FAV.STORNO,0)=0) ";
        String group = "GROUP BY F.FALLID, FA.OEBENEID, FAV.FALL_FACHRICHTID ";
        String query = "SELECT f.FALLID "
                + //0
                from + where + whereExtension + group;
//            Object o = connection.getSingleSQLResult("SELECT COUNT(*) FROM (" + query + ")");
//            if (o != null && o instanceof Number) {
//                results = ((Number) o).intValue();
//            }
        query += " ORDER BY f.FALLID";
        return query;
//            connection.openResultSet(query, false);
//        }
//        return results;
    }

    private Department getBewegung(Integer fallId, int fabId) {
        Case fall = getFall(fallId);
        if (fall == null) {
            return null;
        }
        Iterator<Department> it = fall.getDepartments().iterator();
        while (it.hasNext()) {
            Department b = it.next();
            if (b.getTpId().equalsIgnoreCase(String.valueOf(fabId))) {
                return b;
            }
//            if (b.getId() == fabId) {
//                return b;
//            }
        }
        return null;
    }

//    public static String getBewegungsId(Date dateOrAbtId, Case fall) {
//        Department b = OrbisToCpxTransformer.getBewegung(dateOrAbtId, fall);
//        if (b != null) {
//            return b.getTpId();
//        } else {
//            return "";
//        }
//    }
    public static Department getBewegung(String abtId, Case fall) {
        Department b;
        List<Department> bewegungen = new ArrayList<>(fall.getDepartments());
        int size = bewegungen.size();
        if (abtId != null) {
            if (size > 0) {
                for (int i = size - 1; i >= 0; i--) {
                    b = bewegungen.get(i);
                    if (b.getTpId().equals(abtId)) {
                        return b;
                    }
                }
            }
            b = bewegungen.get(size - 1);
            return b;
//            }
        }
        if (size >= 1) {
            b = bewegungen.get(size - 1);
            return b;
        }
        return null;
    }

//    private static Integer getInteger(ResultSet pResultSet, int pColumnIndex) throws SQLException {
//        int nValue = pResultSet.getInt(pColumnIndex);
//        return pResultSet.wasNull() ? null : nValue;
//    }
    @Override
    protected Map<Integer, Integer> loadDiagnosearten() throws SQLException, IOException {
        String query = "SELECT DIAGARTID, DIAGARTID || ' - ' || DIAGART FROM " + TABLESPACE + "DIAGNOSEART ORDER BY DIAGARTID";
        return new HashMap<>();
    }

    @Override
    public void getPatienten() throws SQLException, IOException {
        //AGe: don't need, see getFaelle,  lines 233-242
    }

    @Override
    public void getLabordaten() throws SQLException, IOException {
        executeStatement(getLabordatenQuery(), rs -> {
            while (rs.next()) {
                //Object fallNr, lastFallID = null, tage;
                //String labCode = "";
                //ArrayList row = new ArrayList();
                //labGroup = null;
                //upload = true;
                //count++;
//                Integer lastFallID = null;
                Integer fallNr = rs.getInt(1);
                String labCode = rs.getString(7).replace(" ", "_");
                Case fall = getFallMap().get(fallNr);
//                if (rs.get(6) != null) {
//                    labCode = row.get(6).toString();
//                    labCode = labCode.replace(" ", "_");
//                }
//                if (fallNr != null) {
//                    if (fall == null || !fallNr.equals(lastFallID)) {
//                        fall = getFallMap().get(fallNr); // KISFallContainer) fallMap.get(fallNr);
                if (fall == null) {
                    fallNr = getTranslationMap().get(fallNr); //translationMap.get(fallNr);
                    if (fallNr != null) {
                        fall = getFallMap().get(fallNr); // (KISFallContainer) fallMap.get(fallNr);
                    }
                }
                String labGroup = null;
//                    }
                if (getLaborProperties().getLaborGroupHash() != null) {
                    labGroup = getLaborProperties().getLaborGroupHash().get(labCode);
                    if (labGroup == null) {
                        continue;
                    }
                }
                if (fall != null) {
                    Lab lab = new Lab(fall);
                    lab.setValue(rs.getDouble(2));
                    lab.setDate(rs.getDate(4));
                    lab.setRange(rs.getString(8)); //Bereich?
                    lab.setDescription(fallNr + rs.getString(7));
                    lab.setUnit(rs.getString(3));
                    lab.setGroup(labGroup);
//                    sb.setLength(0);
//                    sb.append(id++);
//                    sb.append(';');
//                    sb.append(fall.id);
//                    sb.append(';');
//                    sb.append(fall.idIntern);
//                    sb.append(';');
//                    sb.append(fall.idExtern);
//                    sb.append(';');
//                    Object o = row.get(1);
//                    if (o != null) {
//                        sb.append(roundValue(o, 1000.0)); //Wert1
//                    }
//                    sb.append(';');
//                    sb.append(';');//Wert2
//                    o = row.get(3); //Datum
//                    if (o != null) {
//                        sb.append(getDatum(o));
//                    }
//                    sb.append(';');
//                    o = row.get(8);
//                    if (o != null) {
//                        sb.append(o.toString());
//                    }
//                    sb.append(';');//Bereich
//                    sb.append(';');//Bewertung
//                    sb.append(';');//Untergrenze
//                    sb.append(';');//Obergrenze
//                    /*o = row.get(4);
//						if(o != null){
//							String textwert = o.toString();
//							textwert = textwert.replace("\n"," ");
//							if(textwert.length()>100)
//								textwert = textwert.substring(0,100);
//							sb.append(textwert);
//						}*/
//                    sb.append(';');//Textwert
//                    sb.append(';');//Kommentar
//                    sb.append(';');//Analyse-Datum
//                    sb.append(';');//Befund-Position
//                    sb.append(';');//Analyse
//                    o = row.get(6);
//                    String bezeichnung = "";
//                    if (o != null) {
//                        bezeichnung = o.toString();
//                        currFallLaborWert = fallNr.toString() + bezeichnung;
//                    }
//                    sb.append(bezeichnung); //Bezeichnung
//                    sb.append(';');
//                    o = row.get(2);
//                    if (o != null) {
//                        sb.append(o); //Einheit
//                    }
//                    sb.append(';');
//                    sb.append(';');//Methode
//                    sb.append(';');//Kategorie
//                    if (labGroup == null) {
//                        o = row.get(7);
//                        if (o != null) {
//                            labGroup = o.toString();
//                        }
//                    }
//                    if (labGroup != null) {
//                        sb.append(labGroup); //Gruppe
//                    }
//                    boolean uploaded = false;
//                    if (!m_imp_labor_onlyMinMax && upload) {
//                        uploaded = uploadClient.sendLine(sb.toString());
//                    } else if (upload) {
//                        uploaded = true;
//                        currLine = sb.toString();
//                        if (lastFallLaborWert.equals(currFallLaborWert)) {
//                            upload = false;
//                        }
//                        if (upload) {
//                            if (lastLine != null && !lastLine.equals(lastUploadedLine)) {
//                                uploaded = uploadClient.sendLine(lastLine);
//                                lastUploadedLine = lastLine;
//                            }
//                            if (uploaded && !lastUploadedLine.equals(currLine)) {
//                                uploaded = uploadClient.sendLine(currLine);
//                                lastUploadedLine = currLine;
//                            }
//                        }
//                        lastFallLaborWert = currFallLaborWert;
//                        lastLine = currLine;
//                    }
//                    if (upload && !uploaded) {
//                        System.out.println(fallNr + ":" + sb.toString());
//                        result = false;
//                        break;
//                    }
                }
//                lastFallID = fallNr;
//                }
//                if (result && m_imp_labor_onlyMinMax && lastLine != null && !lastLine.equals(lastUploadedLine) && upload) {
//                    result = uploadClient.sendLine(lastLine);
//                }
            }
        });
    }

    @Override
    protected Map<String, KisPatientContainer> loadPatienten() throws SQLException, IOException {
        // muss bestimmt gefüllt werden
        return new HashMap<>();
    }

    @Override
    protected Map<String, Integer> loadDrgAbrechnungsarten() throws SQLException, IOException {
        return new HashMap<>();
    }

    @Override
    public void removeStorno() {
        //
    }

    @Override
    protected Map<Integer, TarifeContainer> loadTarife() throws SQLException, IOException {
        return new HashMap<>();
    }

    @Override
    protected String getEntgelteQuery() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected String getFaelleQuery() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected String getLabordatenQuery() {
        String whereLaborGruppen = "";
        if (!getProperties().getGwiImpLaborgruppe().isEmpty()) {
            whereLaborGruppen = "LGH.BEZEICHNUNG IN(";
            for (int i = 0, n = getProperties().getGwiImpLaborgruppe().size(); i < n; i++) {
                whereLaborGruppen += "'" + getProperties().getGwiImpLaborgruppe().get(i) + "'";
                if (i < n - 1) {
                    whereLaborGruppen += ",";
                }
            }
            whereLaborGruppen += ") AND ";
        } else if (getProperties().isGwiImpLaborgruppeRestriction() && getProperties().getGwiImpLaborgruppe().isEmpty()) {
            //return ""; //Alle Laborgruppen auf false gestellt
        }
        String dateExtension = "";
        if (getProperties().getGwiImpLaborForLastDays() != null) {
            dateExtension = " AND f.AUFNDAT>=sysdate-" + getProperties().getGwiImpLaborForLastDays() + " ";
        }
        String fromWhere = "FROM " + TABLESPACE + "FALL F, " + TABLESPACE_CW + "CW_LABORBEFUND LB, "
                + TABLESPACE_CW + "CW_LABORKATALOG LK, " + TABLESPACE_CW + "CW_LABORKATALOGHISTORY LKH, "
                + TABLESPACE_CW + "CW_LABORGRUPPE LG, " + TABLESPACE_CW + "CW_LABORGRUPPEHISTORY LGH "
                + "WHERE F.FALLID=LB.FALLNUMMER AND LB.BEFUNDNUMMER=LK.NUMMER AND "
                + //"LK.NUMMER=LKH.NUMMER AND LKH.GRUPPEHISTORY=LG.NUMMER AND "+
                "LB.BEFUNDNUMMER=LKH.KATALOGNUMMER AND LKH.ISTAKTUELL=1 AND LKH.GRUPPEHISTORY=LG.NUMMER AND "
                + //"LG.NUMMER=LGH.NUMMER AND LB.WERT IS NOT NULL AND "+
                "LG.NUMMER=LGH.GRUPPENUMMER AND LGH.ISTAKTUELL=1 AND LB.WERT IS NOT NULL AND "
                + whereLaborGruppen
                + getDateWhere() + getCaseNumberWhere()
                + dateExtension
                + getAufnahmeArten() + getAufnahmegruende();
        String orderBy = " order by F.FALLID, LGH.BEZEICHNUNG, LKH.BEZEICHNUNG, LB.WERT";
//        Object o = connection.getSingleSQLResult("SELECT COUNT(*) " + fromWhere);
//        if (o != null && o instanceof Number) {
//            results = ((Number) o).intValue();
//        }
        return "SELECT F.FALLID "
                + ", LB.WERT "
                + ", LB.EINHEIT "
                + ", LB.BEFUNDDATUM "
                + ", LB.TEXT "
                + ", LB.ABNORMITAET "
                + ", LKH.BEZEICHNUNG "
                + ", LGH.BEZEICHNUNG "
                + ", LB.NORMALWERTE "
                + ", LK.CODE "
                + fromWhere + orderBy;
    }

    @Override
    protected String getPatientenQuery() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String getStornoQuery() {
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
    protected Map<String, String> loadStationen() throws SQLException, IOException {
        // siehe openStationenCursor
        return new HashMap<>();
    }

    @Override
    protected Map<String, Integer> loadUrlaub() throws SQLException, IOException {
        // siehe openUrlaubCursor
        return new HashMap<>();
    }

    @Override
    protected Map<Integer, String> loadTransferHospLess24Hours() throws SQLException, IOException {
        return new HashMap<>();
    }

    @Override
    protected Map<Integer, KisInsuranceContainer> loadInsurances() throws SQLException, IOException {
        return new HashMap<>();
    }

}
