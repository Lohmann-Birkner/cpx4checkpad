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
import dto.impl.Case;
import dto.impl.Department;
import dto.impl.Diagnose;
import dto.impl.Hauptdiagnose;
import dto.impl.Lab;
import dto.impl.Nebendiagnose;
import dto.impl.Patient;
import dto.impl.Procedure;
import dto.impl.Ward;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import module.Nexus;
import module.impl.ImportConfig;
import util.UtlDateTimeConverter;

/**
 *
 * @author Dirk Niemeier
 */
public class NexusToCpxTransformer extends DbToCpxTransformer<Nexus, String, String, String, Date[]> {

    private static final Logger LOG = Logger.getLogger(NexusToCpxTransformer.class.getName());

    public static final String DEFAULT_SCHEMA = "KIS.";

    public NexusToCpxTransformer(final ImportConfig<Nexus> pImportConfig) throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException, InterruptedException, NoSuchAlgorithmException, NoSuchMethodException, InvocationTargetException, SQLException {
        super(pImportConfig);
//        m_disziplinInt.add(new BigDecimal(101));
//        m_disziplinInt.add(new BigDecimal(151));
    }

    @Override
    public TransformResult start() throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException, InterruptedException, NoSuchAlgorithmException, NoSuchMethodException, InvocationTargetException, SQLException {
        return new TransformResult(patientCounter.get(), caseCounter.get(), exceptions);
    }

    @Override
    public Map<String, Case> getFaelle() throws SQLException, IOException {
        Map<String, Case> result = new HashMap<>();
        //loadEntbindungsdaten(aufVonDatum, aufBisDatum, entVonDatum, entBisDatum, aufnahmeGruende, aufnahmeArten);

        //GKr 25.01.2017 neue Methode wegen Versicherungsdaten
        executeStatement(getFaelleQuery(), rs -> {
            while (rs.next()) {
                String fallNr = rs.getString(1);
                String aufnNr = rs.getString(2);
                if (fallNr != null) {
                    Case f = new Case(fallNr); //fallNr = patNr?!
                    String ikz = getDefaultHosIdent();
                    //aDatum = getNexusDate(rs.getString(2), rs.getString(3));
                    //eDatum = getNexusDate(rs.getString(4), rs.getString(5));
                    Date aDatum = rs.getDate(3);
                    Date eDatum = rs.getDate(5);
                    f.setAufnahmedatum(aDatum);
                    f.setIkz(ikz);
                    f.setFallNr(aufnNr);
                    createDefaultBewegung(f, aDatum, eDatum);

                    LOG.log(Level.FINEST, "DBG->getFaelle(): Es wird KassenIK geladen-> für EpisodenNr: " + fallNr.trim());
                    //s = rs.getString(10); //IKZ Krankenkasse - ppvpat_kvk_kass_nr
                    KisInsuranceContainer tmpNexusCasePatientInsurance = getInsurance(fallNr.trim());
                    String kasseNr = "";
                    if (tmpNexusCasePatientInsurance != null) {
                        kasseNr = tmpNexusCasePatientInsurance.getKasseNr();
                        LOG.log(Level.FINEST, "DBG->getFaelle(): Kasse_Nr aus m_nexusCaseInsuranceHMap geladen");
                    } else {
                        LOG.log(Level.FINEST, "DBG->getFaelle(): EpisodenNr fuer Kasse_Nr NICHT in m_nexusCaseInsuranceHMap gefunden: " + fallNr.trim());
//                        s = rs.getString(10); //IKZ Krankenkasse - ppvpat_kvk_kass_nr
//                        if (m_debugprint_forSST_aktiv) {
//                            logger.info("DBG->getFaelle(): Kasse_Nr aus openFallCursor geladen geladen");
//                        }
                    }
                    kasseNr = pruefeIKZ9stellig(kasseNr);
                    // TODO Krankenkasse aus Hashmap o. direkt holen //krankenkasse
                    f.setEntlassungsdatum(eDatum);
                    f.setVwdIntensiv(getVwdIntensiv(fallNr));
                    Integer alterInJahren = getAgeInYears(aDatum, getNexusDate(rs.getString(7)));
                    Integer alterInTagen = getAgeInDays(aDatum, getNexusDate(rs.getString(7)));
                    f.setAlterInJahren(alterInJahren);
                    f.setAlterInTagen(alterInTagen);
                    //char geschlecht = getGeschlecht(rs.getString(8)); // geschlecht
                    String enlassungsgrund12 = getEntlassungsgrund12(rs.getString(13));// entlassungsgrund12
                    String enlassungsgrund3 = rs.getString(14);// entlassungsgrund12
                    if (enlassungsgrund3 == null && f.getEntlassungsdatum() != null) {
                        enlassungsgrund3 = "1";
                    }
                    //String plz = rs.getString(9); // plz
                    //sb.append(getYearMonth(this.getNexusDate(rs.getString(8)))); // geburtsjahr, geburtsmonat
                    // TODO Verlegungskrankenhaus holen
                    String einwKKH = "";
                    if (rs.getString(15) != null) {
                        einwKKH = rs.getString(15).trim();
                    }
                    String aufnahmegrund12 = getAufnahmegrund12(rs.getString(11));
                    String aufnahmegrund34 = getAufnahmegrund34(rs.getString(12));
                    String transferHospLess24H = getTransferHospLess24Hours(fallNr);
                    String aufnahmeanlass = getAufnahmeanlass(rs.getString(21), einwKKH, aufnahmegrund34, transferHospLess24H); // aufnahmeanlass
                    f.setAufnahmeanlass(aufnahmeanlass);
                    f.setAufnahmegrund1(aufnahmegrund12);
                    f.setAufnahmegrund2(aufnahmegrund34);
                    // TODO isDRG setzen//s = drgAbrechungsArtenMap.get(rs.getString(13));
                    //f.isDrg = (s != null) ? 1 : 0;
                    // TODO Fallstatus setzen//
                    f.setFallstatus(getKisFallStatus(rs.getString(19), rs.getInt(20)));
                    //Beatmungsstunden seten
                    Integer beatmungsstunden = rs.getInt(18);
                    if (beatmungsstunden != null) {
                        setBeatmungsdauer(fallNr, beatmungsstunden);
//                        ZusatzContainer c = getZusatzContainer(fallNr);
//                        if (c == null) {
//                            c = new ZusatzContainer();
//                            setBeatmungsdauer(fallNr, beatmungsstunden);
//                            zusatzHash.put(fallNr, c);
//                        }
//                        c.beatmungsDauer = ((Number) s).intValue();
                    }
                    Integer urlaub = super.getUrlaub(fallNr);
                    f.setUrlaubstage(urlaub);
//                    valToB = m_ToBMap.get(fallNr); //Ermittlung ToB
//                    if (valToB != null && valToB instanceof Integer) {
//                        f.tob = ((Integer) valToB).intValue();
//                    }

//                    valUrlaub = m_UrlaubMap.get(fallNr); //Ermittlung Urlaub
//                    if (valUrlaub != null && valUrlaub instanceof Integer) {
//                        f.urlaub = ((Integer) valUrlaub).intValue();
//                    }
                    result.put(fallNr, f);
                }
            }
        });
        return result;
    }

    @Override
    public void getUrlaub() throws SQLException, IOException {
//        Map<String, Integer> result = new HashMap<>();
//
//        String s = getDateWhere() + getAufnahmeArten() + getAufnahmeGruende();
//        if (s.length() > 0) {
//            s = " AND " + s;
//        }
//        String where = "WHERE e.ppveps_epsnr=ai.ppveps_epsnr and f.ppveps_epsnr=e.ppveps_epsnr " + s;
//        String from = "FROM " + getDbSchema() + "p_pveps_episode e, "
//                + getDbSchema() + "p_pvbzt_behzt f, "
//                + getDbSchema() + "p_pvabi_abr_info ai ";
//
//        String query = "SELECT e.ppveps_epsnr, ai.ppvabi_tage_ohne_berechnung "
//                + from + where + " ORDER BY e.ppveps_epsnr ";
//
//        executeStatement(query, rs -> {
//            while (rs.next()) {
//                String fallid = rs.getString(1);
//                Integer valUrlaub = rs.getInt(2);
//                if (fallid != null && valUrlaub != null) {
//                    result.put(fallid, valUrlaub);
//                }
//            }
//        });
//        return result;
    }

    @Override
    public Map<Integer, Integer> loadDrgBeleg() throws SQLException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void getPatienten() throws SQLException, IOException {
        executeStatement(getPatientenQuery(), rs -> {
            String lastPatnr = "";
            while (rs.next()) {
                String fallNr = rs.getString(1);
                if (fallNr != null) {
                    Case fall = getFall(fallNr);
                    if (fall != null) {
                        String name = rs.getString(3);
                        String vorname = rs.getString(4);
                        //CP-232 Leerzeichen vor der patnr
                        //patnr = rs.getString(2).toString();
                        String patnr = rs.getString(2).trim();

                        //versNr = rs.getString(7);
                        if (patnr.equalsIgnoreCase(lastPatnr)) {
                            continue;
                        }
                        Patient pat = new Patient();
                        pat.setPatNr(patnr);
                        pat.setNachname(name);
                        pat.setVorname(vorname);
                        pat.setGeburtsdatum(getNexusDate(rs.getString(5)));
                        pat.setGeschlecht(getGeschlecht(rs.getString(6)));
                        //GKr Versicherungsnummer des Patienten
                        LOG.log(Level.FINEST, "DBG->getPatienten(): Es soll die VersicherungsNr geladen werden-> für EpisodenNr: " + fallNr.trim());
                        KisInsuranceContainer tmpNexusCaseInsurance = getInsurance(fallNr.trim());
                        String versNr = "";
                        if (tmpNexusCaseInsurance != null) {
                            versNr = tmpNexusCaseInsurance.getMitglNr();
                            LOG.log(Level.FINEST, "DBG->getPatienten(): VersicherungsNr aus m_nexusCaseInsuranceHMap geladen: versNr=" + versNr);
                        } else {
                            LOG.log(Level.FINEST, "DBG->getPatienten(): EpisodenNr NICHT in m_nexusCaseInsuranceHMap gefunden: " + fallNr.trim());
                        }

                        if (versNr == null || (versNr != null && versNr.isEmpty())) {
//                                versNr = rs.getString(7);
//                                if (m_debugprint_forSST_aktiv) {
//                                    logger.info("DBG->getPatienten(): VersicherungsNr aus openPatientCursor geladen");
//                                }
                            LOG.log(Level.FINEST, "DBG->getPatienten(): VersicherungsNr existiert noch nicht. Suche in m_nexusPatientInsuranceHMap -> ppvpat_patid: " + patnr);

                            KisInsuranceContainer tmpNexusPatientInsurance = getInsurance(patnr);
                            if (tmpNexusPatientInsurance != null) {
                                versNr = tmpNexusPatientInsurance.getMitglNr();
                                LOG.log(Level.FINEST, "DBG->getPatienten(): VersicherungsNr aus m_nexusPatientInsuranceHMap geladen: versNr=" + versNr);
                            } else {
                                LOG.log(Level.FINEST, "DBG->getPatienten(): ppvpat_patid NICHT in m_nexusPatientInsuranceHMap gefunden: " + patnr);
                            }
                        }

                        pat.setVersichertennr(versNr);

                        /*if(!lastPatnr.equals(patnr)){
                             if(!uploadClient.sendLine(sb.toString())) {
                             result = false;
                             break;
                             }
                             id++;
                             }
                             lastPatnr = patnr;*/
                        lastPatnr = patnr;

                        getCpxMgr().write(pat);
                    }
                }
            }
        });
    }

    @Override
    public void getEntgelte() throws SQLException, IOException {
        //not implemented in Nexus
    }

    @Override
    public void getBewegungen() throws SQLException, IOException {
        executeStatement(getBewegungenQuery(), rs -> {
            //String abtSchl = null, statSchl = null, abt301, station = null;
            boolean lastBewIsIntensiv = false;
            String lastFallNr = null;
            while (rs.next()) {
                String fallNr = rs.getString(1);
                ZusatzContainer<Date[]> c = getZusatzContainer(fallNr);
                if (fallNr != null) {
                    Case fall = getFall(fallNr);
                    if (fall != null) {
                        Department b = new Department(fall);
                        b.setVerlegungsdatum(getNexusDate(rs.getString(2), rs.getString(3)));
                        b.setEntlassungsdatum(getNexusDate(rs.getString(7), rs.getString(8)));
                        String abtSchl = rs.getString(4);
                        String statSchl = rs.getString(5);
                        // neu wegen CP-247
                        KisAbteilungContainer<String> abt = getAbteilung(abtSchl);
                        if (abt != null) {
                            //what to do exactly here?!?
                            //fall.isDrg = nexusDepartment.getIsDrgByAggitation();
//                            nexusDepartment = (NexusDepartment) m_nexusDepartmentMap.get(abtSchl);
//                            if (hasPeppLicense && fall.isDrg == 0 && fall.isDrg != nexusDepartment.getIsDrgByAggitation()) {
//                                fall.isDrg = nexusDepartment.getIsDrgByAggitation();
//                                //logger.info("DGR-PEPP-Zuordnung: fallId "+fall.id + " fallIdExtern "+fall.idExtern+ " fallIdIntern "+fall.idIntern+ " fallIsDrg "+fall.isDrg );
//                            }
                            //abt301 = abteilungen301Hash.get(abtSchl.trim()); //auskommetiert wegen CP-247
                            //abt301 = nexusDepartment.getP301departKey(); // neu wegen CP-247
                            b.setCode(abt.getP301());
                            b.setCodeIntern(abt.getName());

                            //what to do exactly here?!?
//                            //station = abteilungenHash.get(abtSchl.trim()); //auskommetiert wegen CP-247
//                            station = nexusDepartment.getDepartName(); // neu wegen CP-247
//                            if (station != null) {
//                                b.abteilung = station;
//                            } else {
//                                b.abteilung = statSchl.trim();
//                            }
                        }

                        //what to do exactly here?!?
//                        try {
//                            
//                            if (getProperties().getNexusBelegStationenDefList() != null && !getProperties().getNexusBelegStationenDefList().isEmpty()) {
//                                if (rs.getString(5) != null) {
//                                    for (int i = 0; i < getProperties().getNexusBelegStationenDefList().size(); i++) {
//                                        String[] belegStats = getProperties().getNexusBelegStationenDefList().get(i);
//                                        if (belegStats != null && belegStats.length == 2) {
//                                            if (belegStats[0].equals(rs.getString(5).toString().trim())) {
//                                                b.typschluessel = Byte.valueOf(belegStats[1]);
//                                                if (b.typschluessel >= 3) {
//                                                    b.typ = "BA";
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//
//                            if (getProperties().getNexusBelegFABDefList() != null && !getProperties().getNexusBelegFABDefList().isEmpty()) {
//                                if (rs.getString(4) != null) {
//                                    for (int i = 0; i < getProperties().getNexusBelegFABDefList().size(); i++) {
//                                        String[] belegFABs = getProperties().getNexusBelegFABDefList().get(i);
//                                        if (belegFABs != null && belegFABs.length == 2) {
//                                            if (belegFABs[0].equals(rs.getString(4).toString().trim())) {
//                                                b.typschluessel = Byte.valueOf(belegFABs[1]);
//                                                if (b.typschluessel >= 3) {
//                                                    b.typ = "BA";
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        } catch (Exception ex1) {
//                            logger.error("Fehler bei Ermittlung der Abrechnugsart");
//                        }
                        //GKr - VerweildauerIntensiv
//                        try {
                        if (getProperties().getNexusIntensivStationenDefList() != null && !getProperties().getNexusIntensivStationenDefList().isEmpty()) {
                            LOG.log(Level.FINEST, "GKr: NexusAdapter.getBewegungen: VerweildauerIntensiv? ->Stationspruefung für: " + b.getCode());
                            if (getProperties().getNexusIntensivStationenDefList().contains(rs.getString(4).trim())) {
                                if (c != null && c.getBeatmungszeiten() != null && !c.getBeatmungszeiten().isEmpty()) {
                                    for (int i = 0, n = c.getBeatmungszeiten().size(); i < n; i++) {
                                        Date[] beatmung = c.getBeatmungszeiten().get(i);
                                        if (beatmung != null && beatmung.length == 2) {
                                            Date beatmBeginn = beatmung[0];
                                            Date beatmEnde = beatmung[1];
                                            BigDecimal beaDiff = new BigDecimal(0);
                                            if (beatmBeginn != null && beatmBeginn instanceof Date && beatmEnde != null
                                                    && beatmEnde instanceof Date) {
                                                if (b.getVerlegungsdatum().before(beatmEnde) && b.getEntlassungsdatum().after(beatmBeginn)) {
                                                    if (beatmBeginn.before(b.getVerlegungsdatum())) {
                                                        if (lastBewIsIntensiv && (lastFallNr != null && lastFallNr.equals(fallNr))) {
                                                            beatmBeginn = b.getVerlegungsdatum();
                                                        }
                                                    }
                                                    if (beatmEnde.after(b.getEntlassungsdatum())) {
                                                        beatmEnde = b.getEntlassungsdatum();
                                                        lastBewIsIntensiv = true;
                                                    } else {
                                                        lastBewIsIntensiv = false;
                                                    }
                                                    long timediff = beatmEnde.getTime() - beatmBeginn.getTime();
                                                    beaDiff = new BigDecimal(timediff);
                                                    //if (m_debugprint_forSST_aktiv) {
                                                    LOG.log(Level.FINEST, "LUR-Beatmung-DIffernz in Milli_Sek. Fall: " + fallNr + " :" + beaDiff);
                                                    //}
                                                    BigDecimal hour = new BigDecimal(1000 * 60 * 60);
                                                    beaDiff = beaDiff.divide(hour, 2, RoundingMode.HALF_UP);
                                                    //beaDiff = beaDiff.divide(hour, 2, BigDecimal.ROUND_UP);
                                                    //if (m_debugprint_forSST_aktiv) {
                                                    LOG.log(Level.FINEST, "LUR-Beatmung-DIffernz in Stund Fall: " + fallNr + " :" + beaDiff);
                                                    //}
//                                                        if (beaDiff.doubleValue() > 0.001) {
//                                                            b.addVwdIntensiv(beaDiff.doubleValue());
//                                                        }
//                                                        if (m_debugprint_forSST_aktiv) {
                                                    LOG.log(Level.FINEST, "GKr: NexusAdapter.getBewegungen: VerweildauerIntensiv -> Station: "
                                                            + b.getCode() + " ist in sst.properties als Intensiv-Station definiert. "
                                                            + beaDiff.doubleValue() + " Intensivtage für FallNr: " + fallNr);
//                                                        }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                lastBewIsIntensiv = false;
                            }
                        }
//                        } catch (SQLException ex1) {
//                            LOG.log(Level.SEVERE, "Fehler bei Berechnung der Bewegungsbeatmungen", ex1);
//                        }
//                        b.isDefault = 0;
//                        b.id = index++;
//                        fall.addBewegung(b);
                        getCpxMgr().write(b);
                        lastFallNr = fallNr;
                    }
                }
            }
        });
    }

    @Override
    public void getMultiFaelle() throws SQLException, IOException {
        executeStatement(getNachfolgerQuery(), rs -> {
            List<String> removedCase = new ArrayList<>();
            Map<String, String> hmFalnr = new HashMap<>();
            while (rs.next()) {
                String fallNr = rs.getString(1);
                String succNr = rs.getString(2);
                if (fallNr != null && succNr != null) {
                    String tempNr = hmFalnr.get(fallNr);
                    if (tempNr != null) {
                        fallNr = tempNr;
                    }
                    Case fall = getFall(fallNr);
                    Case succ = getFall(succNr);
                    hmFalnr.put(succNr, fallNr);
                    if (fall != null && succ != null) {
                        int dauer = getBeatmungsdauer(succNr);
                        List<Date[]> beatZeiten = getBeatmungszeiten(succNr);
                        if (dauer > 0 || (beatZeiten != null && !beatZeiten.isEmpty())) {
                            ZusatzContainer<Date[]> c = getZusatzContainer(fallNr);
                            if (c == null) {
                                c = new ZusatzContainer<>();
                                setZusatzContainer(fallNr, c);
                            }
                            if (c != null) {
                                setBeatmungsdauer(fallNr, dauer);
                                setBeatmungszeiten(fallNr, beatZeiten);
//                                c.beatmungsDauer += dauer;
//                                c.beatmungszeiten.addAll(beatZeiten);
                            }
                        }
//                        int vwdIntensivSecondCase = getVwdIntensivSecondCase(succ.sendBuffer2);
//                        fall.sendBuffer2 = buildNewStringBuffer2VwdIntensiv(fall.sendBuffer2, vwdIntensivSecondCase);
                        //fall.bewegungen.addAll(succ.bewegungen);
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
                            LOG.log(Level.FINEST, "fall.urlaub += succ.urlaub; fall.nalos += succ.nalos + vwd;?");
                            fall.addUrlaubstage(succ.getUrlaubstage());
//                            fall.urlaub += succ.urlaub;
//                            fall.nalos += succ.nalos + vwd;
                        } else {
                            LOG.log(Level.FINEST, "fall.urlaub += succ.urlaub + vwd; fall.nalos += succ.nalos;?");
                            fall.addUrlaubstage(succ.getUrlaubstage());
//                            fall.urlaub += succ.urlaub + vwd;
//                            fall.nalos += succ.nalos;
                        }
                        if (!succ.getEntlassungsgrund12().isEmpty()) {
                            fall.setEntlassungsgrund12(succ.getEntlassungsgrund12());
                        }
                        fall.setEntlassungsdatum(succ.getEntlassungsdatum());
                        //fall.tob += succ.tob;
                        fall.addUrlaubstage(succ.getUrlaubstage());
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
            calculateMulticaseLeaveDays(getTranslationMap().values());
        });
    }

    @Override
    public void getStationen() throws SQLException, IOException {
//        if (!m_importParam.contains(POSSIBLE_HOSWARD)) {
//            return true;
//        }
        //"Stationen in der ORBIS Datenbank zusammenstellen...");
        executeStatement(getStationenQuery(), rs -> {
            String lastFallID = null;
            boolean lastBewIsIntensiv = false;
            while (rs.next()) {
                String fallID = rs.getString(1); // FALLID
                if (fallID != null) {
                    Case fall = getFall(fallID);
                    if (fall == null || !fallID.equals(lastFallID)) {
                        fall = getFall(fallID);
                        if (fall == null) {
                            fallID = getTranslation(fallID);
                            if (fallID != null) {
                                fall = getFall(fallID);
                            }
                        }
                    }
                    if (fall != null) {
                        ZusatzContainer<Date[]> c = getZusatzContainer(fallID);
                        String statSchl = rs.getString(5);
                        //hosWard = new KisHosWardContainer();
                        Date dateAdm = getNexusDate(rs.getString(2), rs.getString(3));
                        Date dateDis = getNexusDate(rs.getString(7), rs.getString(8));
                        Department dep = getBewegung(dateAdm, fall);
                        //bewID = fall.getBewegungsID(dateAdm, fall);
                        String schotDesc = rs.getString(4);
                        String station = rs.getString(9);
                        if (station != null) {
                            station = getStation(station.trim());
                        }
                        if (station == null) {
                            station = "unbekannt";
                        }

                        Ward ward = new Ward(dep);
                        ward.setCode(schotDesc);
                        ward.setVerlegungsdatum(dateAdm);
                        ward.setEntlassungsdatum(dateDis);

                        if (dateAdm != null && dateAdm instanceof Date) {
                            ward.setVerlegungsdatum(dateAdm);
                        }
                        if (dateDis != null && dateDis instanceof Date) {
                            ward.setEntlassungsdatum(dateDis);
                        }
                        if (statSchl != null) {
                            ward.setCode(statSchl.replace(",", "")); // Kurzbez
                        }
//                        if (station != null) {
//                            ward.description = station.toString().replace(",", ""); // Name
//                        }

                        //what is the purpose of this? We detect admission mode in distribution phase
//                        try {
//                            if (getProperties().getNexusBelegStationenDefList() != null && !getProperties().getNexusBelegStationenDefList().isEmpty()) {
//                                if (rs.getString(5) != null) {
//                                    for (int i = 0; i < getProperties().getNexusBelegStationenDefList().size(); i++) {
//                                        String[] belegStats = getProperties().getNexusBelegStationenDefList().get(i);
//                                        if (belegStats != null && belegStats.length == 2) {
//                                            if (belegStats[0].equals(rs.getString(5).toString().trim())) {
//                                                hosWard.erbringungsart = Byte.valueOf(belegStats[1]);
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//
//                            if (getProperties().getNexusBelegFABDefList() != null && !getProperties().getNexusBelegFABDefList().isEmpty()) {
//                                if (rs.getString(4) != null) {
//                                    for (int i = 0; i < getProperties().getNexusBelegFABDefList().size(); i++) {
//                                        String[] belegFABs = getProperties().getNexusBelegFABDefList().get(i);
//                                        if (belegFABs != null && belegFABs.length == 2) {
//                                            if (belegFABs[0].equals(rs.getString(4).toString().trim())) {
//                                                hosWard.erbringungsart = Byte.valueOf(belegFABs[1]);
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        } catch (NumberFormatException | SQLException ex1) {
//                            LOG.log(Level.SEVERE, "Fehler bei Ermittlung der Abrechnugsart", ex1);
//                        }
                        //GKr - VerweildauerIntensiv
//                        try {
                        if (getProperties().getNexusIntensivStationenDefList() != null && !getProperties().getNexusIntensivStationenDefList().isEmpty()) {
                            LOG.log(Level.FINEST, "GKr: NexusAdapter.getBewegungen: VerweildauerIntensiv? ->Stationspruefung für: " + statSchl);
                            if (getProperties().getNexusIntensivStationenDefList().contains(rs.getString(4).trim())) {
                                if (c != null && c.getBeatmungszeiten() != null && !c.getBeatmungszeiten().isEmpty()) {
                                    for (int i = 0, n = c.getBeatmungszeiten().size(); i < n; i++) {
                                        Date[] beatmung = c.getBeatmungszeiten().get(i);
                                        if (beatmung != null && beatmung.length == 2) {
                                            Date beatmBeginn = beatmung[0];
                                            Date beatmEnde = beatmung[1];
                                            BigDecimal beaDiff = new BigDecimal(0);
                                            if (beatmBeginn != null && beatmBeginn instanceof Date && beatmEnde != null
                                                    && beatmEnde instanceof Date) {
                                                if (ward.getVerlegungsdatum().before(beatmEnde) && ward.getEntlassungsdatum().after(beatmBeginn)) {
                                                    if (beatmBeginn.before(ward.getVerlegungsdatum())) {
                                                        if (lastBewIsIntensiv && (lastFallID != null && lastFallID.equals(fallID))) {
                                                            beatmBeginn = ward.getVerlegungsdatum();
                                                        }
                                                    }
                                                    if (beatmEnde.after(ward.getEntlassungsdatum())) {
                                                        beatmEnde = ward.getEntlassungsdatum();
                                                        lastBewIsIntensiv = true;
                                                    } else {
                                                        lastBewIsIntensiv = false;
                                                    }
                                                    long timediff = beatmEnde.getTime() - beatmBeginn.getTime();
                                                    beaDiff = new BigDecimal(timediff);
                                                    LOG.log(Level.FINEST, "LUR-Beatmung-DIffernz in Milli_Sek. Fall: " + fallID + " :" + beaDiff);
                                                    BigDecimal hour = new BigDecimal(1000 * 60 * 60);
                                                    beaDiff = beaDiff.divide(hour, 2, RoundingMode.HALF_UP);
                                                    //beaDiff = beaDiff.divide(hour, 2, BigDecimal.ROUND_UP);
                                                    LOG.log(Level.FINEST, "LUR-Beatmung-DIffernz in Stund Fall: " + fallID + " :" + beaDiff);
//                                                    if (beaDiff.doubleValue() > 0.001) {
//                                                        ward.beatmungsdauer += beaDiff.doubleValue();
//                                                    }
                                                    LOG.log(Level.FINEST, "GKr: NexusAdapter.getBewegungen: VerweildauerIntensiv -> Station: "
                                                            + statSchl + " ist in sst.properties als Intensiv-Station definiert. "
                                                            + beaDiff.doubleValue() + " Intensivtage für FallNr: " + fallID);
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                lastBewIsIntensiv = false;
                            }
                        }
//                        } catch (Exception ex1) {
//                            LOG.log(Level.SEVERE, "Fehler bei Berechnung der Bewegungsbeatmungen", ex1);
//                        }
                        getCpxMgr().write(ward);
//                        fall.addStation(hosWard, true);
//                        id++;
                        lastFallID = fallID;
                    }
                }
            }
        });
    }

    @Override
    public void getDiagnosen() throws SQLException, IOException {
        executeStatement(getDiagnosenQuery(), rs -> {
            String lastFallNr = "";
//            String lastID = null;
            Case fall = null;
            boolean isSucc = false;
            while (rs.next()) {
                String fallNr = rs.getString(1);
                if (fallNr != null) {
                    fall = getFall(fallNr);
                    if (fall == null || !fallNr.equals(lastFallNr)) {
                        if (fall != null) {
                            //cid = writeDRGCodeValue(fall, fall.icds, sb, uploadClient, cid, bewID, version);
                        }
                        if (fall == null) {
                            fallNr = getTranslation(fallNr);
                            if (fallNr != null) {
                                fall = getFall(fallNr);
                            }
                            isSucc = true;
                        } else {
                            isSucc = false;
                        }
                    }
                    if (fall != null) {
                        String id = rs.getString(11); //FALL_DIAGNOSENID
                        String icdCode = rs.getString(2);
                        Boolean hd = rs.getBoolean(6);
                        boolean iGrp = false;
                        Date diagDate = getNexusDate(rs.getString(4), rs.getString(5));
                        Date diagFADate = getNexusDate(rs.getString(8), rs.getString(9));
                        String diagArt = rs.getString(7);

                        Department dep;
                        if (diagFADate != null) {
                            dep = getBewegung(diagFADate, fall); // fall.getBewegungsID(diagFADate, fall);
                        } else {
                            dep = getBewegung(diagDate, fall); // fall.getBewegungsID(diagDate, fall);
                        }
//                        if (!lastID.equals(id)) {
//                            if (code != null) {
//                                writeCodeValue(sb, code, version, sekICD, sekLoc, uploadClient);
//                            } else {
//                                code = new KISCodeContainer();
//                            }

//                            if (icdCode == null || (icdCode != null && icdCode.isEmpty())) {
//                                code = null;
//                                LOG.log(Level.FINEST, "getDiagnosen()->IcdCode hat kein Inhalt->ignoriere diese Diagnose-> Diagnoseart: " + diagArt + " , FallId: " + fall.id + " FallIdExtern: " + fall.idExtern + " FallIdIntern: " + fall.idIntern + " FallNr: " + fallNr);
//                                continue;
//                            }
//                        code.code = icdCode.toString().trim();   // Code Trim wegen CP-232
//                        if (diagFADate != null) {
//                            i = fall.getBewegungsID(diagFADate, fall);
//                        } else {
//                            i = fall.getBewegungsID(diagDate, fall);
//                        }
//                        if (m_importParam.contains(POSSIBLE_HOSWARD)) {
                        Ward ward;
                        if (diagFADate != null) {
                            ward = getStation(diagFADate, fall);
                        } else {
                            ward = getStation(diagDate, fall);
                        }
//                        }

                        Diagnose<?> diagnose;
                        if (hd) {
                            diagnose = new Hauptdiagnose(dep, ward);
                        } else {
                            diagnose = new Nebendiagnose(dep, ward);
                        }

                        diagnose.setCode(icdCode);
                        diagnose.setLokalisation(getLokalisation(rs.getString(3)));
                        diagnose.setToGroup(iGrp);
                        diagnose.setIcdType(getDiagnoseart(diagArt));

//                        version = getICDVersion(fall.getAufnahmedatum());
                        String sekICD = rs.getString(12);

                        if (sekICD != null && !sekICD.isEmpty()) {
                            String sekLoc = getLokalisation(rs.getString(13));
                            Nebendiagnose nebendiagnose = new Nebendiagnose(dep, ward);
                            nebendiagnose.setCode(sekICD);
                            nebendiagnose.setLokalisation(sekLoc);
                        }

//                        }
//                        code.diagArt = art;
//                        if (!isSucc && !diagnose.isHdb()) {
//                            int hdx = this.getHDFlag(art, hd, fall.picd, code.code); //Hauptdiagnose
//                            code.hd = hdx;
//                            if (hdx == 1) {
//                                code.isGr = 1;
//                                fall.picd = "X";
//                                //logger.info("getDiagnosen()->setzeHauptdiagnose-> code.hd: "+code.hd +" code.diagArt: "+ code.diagArt+ " code.code: "+ code.code + " FallNr: "+fallNr);
//                            }
//                        }
//                        if (code.hdb == 0) {
//                            code.hdb = this.getHDBFlag(art, hd); //Hauptdiagnose
//                            if (code.hdb == 1) {
//                                code.isGr = 1;
//                            }
//                        }
                        //CP-69 Diesen Bereich auskommentiert
                        //Berückschtigt die drgRelevanz nur, wenn noch isGr noch nicht gesetzt ist.
                        //Die systemvorgabe wird hier ignoriert
//                        if (code.isGr==0) {
//                            drgRelevanz = rs.getString(10);
//                            if(drgRelevanz != null && drgRelevanz instanceof Number) {
//                                iGrp = ((Number)drgRelevanz).intValue();
//                                code.isGr = iGrp;                     // mit groupen
//                            }
//                        }
                        //CP-69 Korrektur, dass drgRelevanz aus Nexus immer berücksichtigt wird,
                        //sofern ein Zahlenwert enthalten ist
                        //Wenn es eine Hauptdiagnose ist, wird drgrelevanz nicht beruecksichtigt
//                        Integer drgRelevanz = rs.getInt(10);
//                        if (!diagnose.isHdb() && drgRelevanz != null) {
//                            iGrp = drgRelevanz;
//                            if (iGrp != code.isGr) {
//                                //logger.info("getDiagnosen()->drgRelevanz->isGr wird geaendert->von "+code.isGr+" auf "+iGrp+" -> code.hd: "+code.hd +" , code.hdb: "+ code.hdb+" code.diagArt: "+ code.diagArt+ " code.code: "+ code.code + " FallNr: "+fallNr);
//                                code.isGr = iGrp;                     // mit groupen
//                            }
//                        }
//                        fall.icds.add(code);
//                        lastID = id;
                        getCpxMgr().write(diagnose);

                        lastFallNr = fallNr;

                        /*         if(hd != null && hd.equals("HD"))
                         iHd = 1;
                         iGrp = 0;
                         if(art == 4){
                         iGrp = 1;
                         } else {
                         drgRelevanz = rs.getString(10);
                         if(drgRelevanz != null && drgRelevanz instanceof Number) {
                         iGrp = ((Number)drgRelevanz).intValue();
                         }
                         }*/

 /*      sb.setLength(0);
                         sb.append(cid++);
                         sb.append(';');
                         sb.append(fall.idIntern);
                         sb.append(';');
                         sb.append(fall.idExtern);
                         sb.append(";1;");                  // doInsert
                         sb.append(icdCode != null ? ((String)icdCode).trim() : "");
                         sb.append(';');
                         sb.append(getLokalisation(rs.getString(3)));   // lokalisation
                         sb.append(';');
                         if (iHd ==1 && art == 4)
                         sb.append(1);
                         else
                         sb.append(0);       // Hauptdiagnose
                         sb.append(';');
                         if(iHd == 1 && art == 15)
                         sb.append(1);       // Hauptdiagnose Bewegung
                         else
                         sb.append(0);
                         sb.append(';');
                         sb.append(iGrp);                   // mit groupen
                         sb.append(';');
                         version = getICDVersion(fall.aufnahmeDatum);
                         sb.append(";;;");                   // sekundaericd, sekundaerlokalisation
                         if(diagFADate != null) {
                         i = fall.getBewegungsID(diagFADate, fall);
                         } else {
                         i = fall.getBewegungsID(diagDate, fall);
                         }
                         sb.append(i);                       // bewegungsid
                         sb.append(';');
                         sb.append(art); // diagnoseart
                         sb.append(';');
                         if(m_importParam.contains(POSSIBLE_HOSWARD)) {
                         if(diagFADate != null) {
                         statID = fall.getNexusStationsID(diagFADate);
                         } else {
                         statID = fall.getNexusStationsID(diagDate);
                         }
                         sb.append(statID);
                         }
                         sb.append(';');//ref_type
                         if(! uploadClient.sendLine(sb.toString())) {
                         result = false;
                         break;
                         }*/
                    }
                }
            }
//            if (result) {
//                if (code != null) {
//                    writeCodeValue(sb, code, version, sekICD, sekLoc, uploadClient);
//                }
//            }
        });
    }

    @Override
    public void getProzeduren() throws SQLException, IOException {
        executeStatement(getProzedurenQuery(), rs -> {
            while (rs.next()) {
                String fallNr = rs.getString(1);
                if (fallNr != null) {
                    Case fall = getFall(fallNr);
                    if (fall == null) {
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
                        boolean group = true;
                        Date datum = getNexusDate(rs.getString(4), rs.getString(5));
                        Date faDatum = getNexusDate(rs.getString(6), rs.getString(7));
                        Date opsDate = getNexusDate(rs.getString(9), rs.getString(10)); //GKr 12.01.2017 Erweiterung PEPP
                        Boolean drgRelevanz = rs.getBoolean(8);
                        if (drgRelevanz != null) {
                            group = drgRelevanz;
                        } else {
                            group = true;
                        }
                        Department dep;
                        if (faDatum != null) {
                            dep = getBewegung(faDatum, fall);
                        } else {
                            dep = getBewegung(datum, fall);
                        }
                        Ward ward;
                        if (faDatum != null) {
                            ward = getStation(faDatum, fall);
                        } else {
                            ward = getStation(datum, fall);
                        }
                        Procedure proc = new Procedure(dep, ward);
                        proc.setCode(opsCode);
                        proc.setToGroup(group);
                        proc.setDatum(datum);
                        proc.setLokalisation(getLokalisation(rs.getString(3)));
//                        sb.append(getBeatmungsDauer(fallNr));         // beatmungsstunden
//                        sb.append(this.getLokalisation(rs.getString(3)));                               // lokalisation
//                        sb.append(getOfficialCode(opsCode, datum, getOPSVersion(fall.aufnahmeDatum)));
//                        sb.append(';');
//                        if (m_importParam.contains(POSSIBLE_HOSWARD)) {
//                            if (faDatum != null) {
//                                statID = fall.getNexusStationsID(faDatum);
//                            } else {
//                                statID = fall.getNexusStationsID(datum);
//                            }
//                            sb.append(statID);
//                        }
                        getCpxMgr().write(proc);
                    }
                }
            }
        });
    }

    @Override
    public void getDrg() throws SQLException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void getLabordaten() throws SQLException, IOException {
//        if (!m_importParam.contains(this.POSSIBLE_LABOR)) {
//            return true;
//        }
        executeStatement(getLabordatenQuery(), rs -> {

            /*
             ", l.untersuchungswert" +
             ", l.untersuchungseinhe" +
             ", l.normalwert" +
             ", l.idklartext"+
             ", l.erstelldatumzeit"+
             ", l.lastupdate"+
             ", lg.gruppe"+
             */
            while (rs.next()) {
                String textWert1 = "";
                String fallNr = rs.getString(1);
                Case fall = getFall(fallNr);

                if (fall != null) {
                    String o = rs.getString(2);
                    if (o != null) {
                        Float val = null;
                        try {
                            val = Float.parseFloat(o);
                        } catch (NumberFormatException ex) {
                            LOG.log(Level.WARNING, "Cannot parse value as float: " + o, ex);
                            textWert1 = o;
//                            if (textWert1.contains("++")) {
//                                uploadable = true;
//                            } else {
//                                uploadable = false;
//                            }
                        }
                    }

                    Date datum = rs.getDate(6);
                    String bereich = rs.getString(4);
                    String bewertung = rs.getString(9);
                    Double untergrenze = null;
                    Double obergrenze = null;

                    if (!textWert1.isEmpty() && textWert1.length() > 100) {
                        textWert1 = textWert1.substring(0, 100);
                    }
                    String anforderungsnr = rs.getString(10);
                    String kommentar = "";

                    Date analyseDatum = rs.getDate(7);
                    Integer position = null;
                    String analyse = "";
                    String bezeichnung = "";

                    Double wert1 = rs.getDouble(5);
//                    currAnfLabwert = anforderungsnr + "_" + bezeichnung;
//                    if (bezeichnung.length() > 40) {
//                        sb.append(bezeichnung.substring(0, 40));
//                    } else {
//                        sb.append(bezeichnung);
//                    }

                    Double wert2 = rs.getDouble(3);
                    String einheit = "";
                    String methode = "";
                    Integer kategorie = null;

                    //o = rs.getString(8);
                    String gruppe = getLaborProperties().get(bezeichnung);
                    if (o != null) {
                        gruppe = "Sonstige";
                    }

                    Lab lab = new Lab(fall);
                    lab.setAnalysis(analyse);
                    lab.setAnalysisDate(analyseDatum);
                    lab.setMethod(methode);
                    lab.setGroup(gruppe);
                    lab.setCategory(kategorie);
                    lab.setUnit(einheit);
                    lab.setText(textWert1);
                    lab.setComment(kommentar);
                    lab.setDescription(bezeichnung);
                    lab.setMinLimit(untergrenze);
                    lab.setMaxLimit(obergrenze);
                    lab.setRange(bereich);
                    lab.setBenchmark(bewertung);
//                    lab.setValue(wert);
                    lab.setValue2(wert2);

                    getCpxMgr().write(lab);
                } else {
                    LOG.log(Level.WARNING, "Labor: kein Fallcontainer vorhanden - " + fallNr);
                }
            }
        });
    }

    @Override
    protected String getStationenQuery() {
        String s = getDateWhere()
                + getAufnahmeArten() + getAufnahmegruendeSQL();
        if (s.length() > 0) {
            s = " AND " + s;
        }
        String where = "WHERE e.ppveps_epsnr=f.ppveps_epsnr and f.ppvbzt_behnr=eg.ppvbzt_behnr " + s;
        String from = "FROM " + getDbSchema() + "p_pveps_episode e, "
                + getDbSchema() + "p_pvbzt_behzt f, "
                + getDbSchema() + "p_pverk_ereigkette eg ";
//            Object o = connection.getSingleSQLResult("SELECT COUNT(*) " + from + where);
//            if (o != null && o instanceof Number) {
//                results = ((Number) o).intValue();
//            }
        String query = "SELECT e.ppveps_epsnr, eg.ppverg_datum, eg.ppverg_zeit, trim(eg.pstfab_fachbereich), trim(eg.pststa_station), eg.psterg_typ, "
                + "eg.ppverg_datum2, eg.ppverg_zeit2, trim(eg.pststa_station) "
                + from + where + " ORDER BY e.ppveps_epsnr, eg.ppverg_datum, eg.ppverg_zeit ";
        return query;
    }

    @Override
    protected Map<String, String> loadAufnahmegrund12() throws SQLException, IOException {
        Map<String, String> result = new HashMap<>();
        String query = "SELECT pstauf_art, pstauf_key_301 FROM " + getDbSchema()
                + "p_stauf_aufart";
        executeStatement(query, rs -> {
            while (rs.next()) {
                String agSchl = rs.getString(1);
                String ag_301 = rs.getString(2);
                result.put(agSchl, ag_301);
            }
        });
        return result;
    }

//    @Override
//    protected Map<String, String> loadAufnahmegrund12() throws SQLException, IOException {
//        Map<String, String> result = new HashMap<>();
//        String query = "select distinct trim(ag.pstaug_key), trim(ag.pstaug_bez) "
//                + "from " + getDbSchema() + "p_staug_aufngrund_301 ag where exists "
//                + "(select 1 from " + getDbSchema() + "p_pvbzt_behzt f "
//                + "where f.pstaug_key=ag.pstaug_key) " + getMandant() + " order by trim(ag.pstaug_bez)";
//        executeStatement(query, rs -> {
//            while (rs.next()) {
//                String a = rs.getString(1);
//                if (a != null) {
//                    String val = rs.getString(2);
//                    result.put(a, val);
//                }
//            }
//        });
//        return result;
//    }
    @Override
    protected Map<String, String> loadAufnahmegrund34() throws SQLException, IOException {
        Map<String, String> result = new HashMap<>();
        String query = "SELECT pstaug_key, pstaug_key_301 FROM " + getDbSchema()
                + "p_staug_aufngrund_301" + getMandantenWhere();
        executeStatement(query, rs -> {
            while (rs.next()) {
                String agSchl = rs.getString(1);
                String ag_301 = rs.getString(2);
                result.put(agSchl, ag_301);
            }
        });
        return result;
    }

    @Override
    protected Map<String, String> loadEntlassungsgrund12() throws SQLException, IOException {
        Map<String, String> result = new HashMap<>();
        String query = "SELECT pstent_art, pstent_key_301 FROM " + getDbSchema()
                + "p_stent_entlart" + getMandantenWhere();
        executeStatement(query, rs -> {
            while (rs.next()) {
                String entSchl = rs.getString(1);
                String ent_301 = rs.getString(2);
                result.put(entSchl, ent_301);
            }
        });
        return result;
    }

    @Override
    protected Map<String, String> loadEntlassungsgrund3() throws SQLException, IOException {
        return new HashMap<>();
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

    @Override
    protected Map<String, String> loadAufnahmeanlass() throws SQLException, IOException {
        Map<String, String> result = new HashMap<>();
        String query = "select distinct trim(aa.pstbha_bart), trim(ba.pstbha_bez) from "
                + getDbSchema() + "p_stauf_aufart aa, "
                + getDbSchema() + "p_stbha_bhndlgart ba "
                + "where aa.pstbha_bart = ba.pstbha_bart";
        executeStatement(query, rs -> {
            while (rs.next()) {
                String a = rs.getString(1);
                if (a != null) {
                    String val = rs.getString(2);
                    result.put(a, val);
                }
            }
        });
        return result;
    }

    @Override
    protected Map<String, Integer> loadDiagnosearten() throws SQLException, IOException {
        Map<String, Integer> result = new HashMap<>();
//        result.put("DIAG-AUFN", "Aufnahmediagnose");
//        result.put("DIAG-EINW", "Einweisungsdiagnose");
//        result.put("DIAG-FAKT", "Abrechnungsdiagnose");
//        result.put("DIAG-FABT", "Fachabteilungsdiagnose");
//        result.put("DIAG-ENTL", "Entlassungsdiagnose");
        return result;
    }

    @Override
    protected Map<String, TarifeContainer> loadTarife() throws SQLException, IOException {
        return new HashMap<>();
    }

    @Override
    protected Map<String, String> loadKostentraeger() throws SQLException, IOException {
        return new HashMap<>();
    }

    @Override
    protected Map<Integer, String> loadEinzugsgebiet() throws SQLException, IOException {
        return new HashMap<>();
    }

    @Override
    protected Map<String, ZusatzContainer<Date[]>> loadZusatzContainer() throws SQLException, IOException {
        Map<String, ZusatzContainer<Date[]>> result = new HashMap<>();
        String s = getDateWhere() + getAufnahmeArten() + getAufnahmegruendeSQL();
        if (s.length() > 0) {
            s = " AND " + s;
        }
        String where = "WHERE e.ppveps_epsnr=bea.ppveps_epsnr and f.ppveps_epsnr=e.ppveps_epsnr and "
                + "bea.ppvmdb_geloescht=0 AND ppvmdb_dname is null and ppvmdb_dzeit is null and "
                + "ppvmdb_stornoarztname is null and ppvmdb_stornoarztpraesenz is null" + s;
        String from = "FROM " + getDbSchema() + "p_pveps_episode e, "
                + getDbSchema() + "p_pvbzt_behzt f, "
                + getDbSchema() + "p_pvmdb_meddok_beatmungsdauer bea ";
        //Object o = connection.getSingleSQLResult("SELECT COUNT(*) " + from + where);
        String query = "SELECT e.ppveps_epsnr, bea.ppvmdb_beadt_von, bea.ppvmdb_beazt_von, bea.ppvmdb_beadt_bis, "
                + "bea.ppvmdb_beazt_bis "
                + from + where + " ORDER BY e.ppveps_epsnr, bea.ppvmdb_beadt_von, bea.ppvmdb_beazt_von ";

        executeStatement(query, rs -> {
            /* 3.9.5 2015-09-01 DNi: #FINDBUGS - Potenzielle NP-Exception, weil connection != null nicht abgefragt wird.  */
            while (rs.next()) {
                String fallNr = rs.getString(1);
                if (fallNr != null) {
                    Date beaBeginn = getNexusDate(rs.getString(2), rs.getString(3));
                    Date beaEnde = getNexusDate(rs.getString(4), rs.getString(5));
                    ZusatzContainer<Date[]> c = result.get(fallNr);
                    if (c == null) {
                        c = new ZusatzContainer<>();
                        result.put(fallNr, c);
                    }
//                    Date[] beatmZeit = new Date[2];
//                    beatmZeit[0] = beaBeginn;
//                    beatmZeit[1] = beaEnde;
                    //int dauer = getBeatmungsdauer(beaBeginn, beaEnde);
                    //c.setBeatmungsdauer(dauer);
                    //c.beatmungszeiten.add(beatmZeit);
                    Date[] beatmZeit = new Date[2];
                    beatmZeit[0] = beaBeginn;
                    beatmZeit[1] = beaEnde;
                    c.setBeatmungszeit(beatmZeit);
                }
            }
        });
        return result;
    }

    @Override
    protected Map<String, Integer> loadEscort() throws SQLException, IOException {
        return new HashMap<>();
    }

    @Override
    protected Map<String, KisPatientContainer> loadPatienten() throws SQLException, IOException {
        return new HashMap<>();
    }

    @Override
    protected Map<String, BigDecimal> loadVwdIntensivdauer() throws SQLException, IOException {
        Map<String, BigDecimal> result = new HashMap<>();
        if (getProperties().getNexusIntensivStationenDefList() != null && !getProperties().getNexusIntensivStationenDefList().isEmpty()) {
            String s = getDateWhere() + getAufnahmeArten() + getAufnahmegruendeSQL();
            if (s.length() > 0) {
                s = " AND " + s;
            }
            String where = "WHERE e.ppveps_epsnr=f.ppveps_epsnr and f.ppvbzt_behnr=eg.ppvbzt_behnr AND eg.ppverg_datum "
                    + "is not null " + getStringWhere("trim(eg.pstfab_fachbereich)", getProperties().getNexusIntensivStationenDefList()) + s;
            String from = "FROM " + getDbSchema() + "p_pveps_episode e, "
                    + getDbSchema() + "p_pvbzt_behzt f, "
                    + getDbSchema() + "p_pverk_ereigkette eg ";

            String query = "SELECT e.ppveps_epsnr, eg.ppverg_datum, eg.ppverg_zeit, trim(eg.pstfab_fachbereich), "
                    + "eg.ppverg_datum2, eg.ppverg_zeit2 "
                    + from + where + " ORDER BY e.ppveps_epsnr, eg.ppverg_datum, eg.ppverg_zeit ";

            executeStatement(query, rs -> {
                String fallid = rs.getString(1);
                if (fallid != null) {
                    Date intBeginn = getNexusDate(rs.getString(2), rs.getString(3));
                    Date intEnde = getNexusDate(rs.getString(5), rs.getString(6));
                    BigDecimal beaDiff;
                    if (intBeginn != null && intEnde != null) {
                        long timediff = intEnde.getTime() - intBeginn.getTime();
                        beaDiff = new BigDecimal(timediff);
                        BigDecimal hour = new BigDecimal(1000 * 60 * 60);
                        beaDiff = beaDiff.divide(hour, 0, RoundingMode.HALF_UP);
                        //beaDiff = beaDiff.divide(hour, 0, BigDecimal.ROUND_UP);
                        double dauer = beaDiff.doubleValue();
                        if (dauer > 0.001) {
                            BigDecimal c = result.get(fallid);
                            if (c != null) {
                                dauer = dauer + c.doubleValue();
                            }
                            result.put(fallid, BigDecimal.valueOf(dauer));
                        }
                    }
                }
            });
        }
        return result;
    }

    @Override
    protected Map<String, Integer> loadDrgAbrechnungsarten() throws SQLException, IOException {
        return new HashMap<>();
    }

    @Override
    protected Map<Integer, Vidierstufe> loadVidierstufen() throws SQLException, IOException {
        return new HashMap<>();
    }

    @Override
    protected Map<String, KisAbteilungContainer<String>> loadAbteilungen() throws SQLException, IOException {
        Map<String, KisAbteilungContainer<String>> result = new HashMap<>();
        String query = "SELECT trim(pstfab_fachbereich), pstfs3_fabtschl, pstfab_name, pstfab_drg_relevant, pstfab_pepp_relevant  FROM " + getDbSchema()
                + "p_stfab_fachbereich";
        executeStatement(query, rs -> {
            while (rs.next()) {
                String fabSchl = rs.getString(1);
                String p301Schl = rs.getString(2);
                String fabName = rs.getString(3);
                Boolean departDrgRelevant = rs.getBoolean(4);
                Boolean departPeppRelevant = rs.getBoolean(5);

                if (fabSchl != null && p301Schl != null) {
                    KisAbteilungContainer<String> abt = new KisAbteilungContainer<>();
                    abt.setId(fabSchl.trim());
                    abt.setP301(p301Schl);
                    abt.setName(fabName);
                    result.put(fabSchl.trim(), abt);
                    //abteilungen301Hash.put(fabSchl.trim(), p301Schl);
                    //abteilungenHash.put(fabSchl.trim(), fabName);
                }
            }
        });
        return result;
    }

    @Override
    protected String getProzedurenQuery() {
        String s = getDateWhere()
                + getAufnahmeArten() + getAufnahmegruendeSQL();
        if (s.length() > 0) {
            s = " AND " + s;
        }
        String where = "WHERE e.ppveps_epsnr=f.ppveps_epsnr and f.ppvbzt_behnr=d.ppvbzt_behnr and "
                + "d.ppvbzt_behnr=ter.ppvbzt_behnr and d.ppvter_nr_int=ter.ppvter_nr_int and " //GKr 12.01.2017 wegen Pepp sind korrekte Prozedur-Zeiten notwendig
                + "d.ppvafb_aufenthaltnr=a.ppvafb_aufenthaltnr(+) and d.ppvmda_storniertwann is null "
                + "and d.pstdty_dokutyp='OPERATION' " + s;
        String from = "FROM " + getDbSchema() + "p_pveps_episode e, "
                + getDbSchema() + "p_pvbzt_behzt f, "
                + getDbSchema() + "p_pvmda_meddok_aufenth d, "
                + getDbSchema() + "p_pvafb_aufenthalt a, "
                + getDbSchema() + "p_pvter_termin ter "; //GKr 12.01.2017 wegen Pepp sind korrekte Prozedur-Zeiten notwendig
//            Object o = connection.getSingleSQLResult("SELECT COUNT(*) " + from + where);
//            if (o != null && o instanceof Number) {
//                results = ((Number) o).intValue();
//            }
        String query = "SELECT e.ppveps_epsnr, d.pstdco_code_ges, d.pstdgl_lokalisation, "
                + "d.ppvmda_doku_datum, d.ppvmda_doku_zeit, a.ppvafb_datumab, a.ppvafb_zeitab, d.ppvmda_drgrelevant "
                + ",ter.ppvter_datum, ter.ppvter_zeit " //GKr 12.01.2017 wegen Pepp sind korrekte Prozedur-Zeiten notwendig
                + from + where + " ORDER BY e.ppveps_epsnr, a.ppvafb_datumab, a.ppvafb_zeitab";
        return query;
    }

    @Override
    protected String getDiagnosenQuery() {
        String s = getDateWhere()
                + getAufnahmeArten() + getAufnahmegruendeSQL()
                + getDiagnoseartenSQL();
        if (s.length() > 0) {
            s = " AND " + s;
        }
        String where = "WHERE e.ppveps_epsnr=f.ppveps_epsnr and f.ppvbzt_behnr=d.ppvbzt_behnr and "
                + "d.ppvafb_aufenthaltnr=a.ppvafb_aufenthaltnr(+) and d.ppvmda_storniertwann is null "
                + "and not d.pstdty_dokutyp='OPERATION' and d.ppvmda_doku_nr=dix.ppvmda_doku_nr_primaer(+) " + s;
        String from = "FROM " + getDbSchema() + "p_pveps_episode e, "
                + getDbSchema() + "p_pvbzt_behzt f, "
                + getDbSchema() + "p_pvmda_meddok_aufenth d, "
                + getDbSchema() + "p_pvafb_aufenthalt a, "
                + getDbSchema() + "p_pvmda_meddok_aufenth dix ";
//            Object o = connection.getSingleSQLResult("SELECT COUNT(*) " + from + where);
//            if (o != null && o instanceof Number) {
//                results = ((Number) o).intValue();
//            }
        String query = "SELECT e.ppveps_epsnr, d.pstdco_code_ges, d.pstdgl_lokalisation, d.ppvmda_doku_datum, "
                + "d.ppvmda_doku_zeit, d.ppvmda_typ, d.pstdty_dokutyp, a.ppvafb_datumab, a.ppvafb_zeitab, d.ppvmda_drgrelevant, "
                + "d.ppvmda_doku_nr, dix.pstdco_code_ges, dix.pstdgl_lokalisation "
                + from + where + " ORDER BY e.ppveps_epsnr, a.ppvafb_datumab, a.ppvafb_zeitab";
        return query;
    }

    @Override
    protected String getEntgelteQuery() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String getBewegungenQuery() {
        String s = getDateWhere() + getAufnahmeArten() + getAufnahmegruendeSQL();
        if (s.length() > 0) {
            s = " AND " + s;
        }
        String where = "WHERE e.ppveps_epsnr=f.ppveps_epsnr and f.ppvbzt_behnr=eg.ppvbzt_behnr " + s;
        String from = "FROM " + getDbSchema() + "p_pveps_episode e, "
                + getDbSchema() + "p_pvbzt_behzt f, "
                + getDbSchema() + "p_pverk_ereigkette eg ";
//            Object o = connection.getSingleSQLResult("SELECT COUNT(*) " + from + where);
//            if (o != null && o instanceof Number) {
//                results = ((Number) o).intValue();
//            }
        String query = "SELECT e.ppveps_epsnr, eg.ppverg_datum, eg.ppverg_zeit, trim(eg.pstfab_fachbereich), trim(eg.pststa_station), eg.psterg_typ, "
                + "eg.ppverg_datum2, eg.ppverg_zeit2 "
                + from + where + " ORDER BY e.ppveps_epsnr, eg.ppverg_datum, eg.ppverg_zeit ";
        return query;
    }

    @Override
    protected String getFaelleQuery() {
        String s = getDateWhere() + getAufnahmeArten() + getAufnahmegruendeSQL();
        String from = "FROM " + getDbSchema() + "p_pveps_episode e, "
                + getDbSchema() + "p_pvbzt_behzt f, "
                + getDbSchema() + "p_stauf_aufart aa, "
                + getDbSchema() + "p_pvpat_patient p";
        if (s.length() <= 0) {
            s = "1=1";
        }
        String where = " where f.ppveps_epsnr=e.ppveps_epsnr and "
                + "aa.pstauf_art=f.pstauf_art and aa.allmnd_mandant=f.allmnd_mandant and e.ppvpat_patnr=p.ppvpat_patnr and " + s;
//            Object o = connection.getSingleSQLResult("SELECT COUNT(*) " + from + where);
//            if (o != null && o instanceof Number) {
//                results = ((Number) o).intValue();
//            }
        String query = "SELECT e.ppveps_epsnr, e.ppveps_aufnr, "
                + "f.ppvbzt_aufn_dat, e.ppveps_zeitvon, f.ppvbzt_ent_dat, e.ppveps_zeitbis, "
                + "p.ppvpat_gdatum, f.astgsl_geschlecht, p.ppvpat_plz, p.ppvpat_kvk_kass_nr,"
                + "f.pstauf_art, f.pstaug_key, f.pstent_art, f.psteng_key, "
                + "f.ppvbzt_krhsvon, f.ppvbzt_krhszu, "
                + "e.ppveps_aufnahmegewicht, e.ppveps_beatmungsdauer, "
                + "e.ppveps_freigabe_kzn , e.ppveps_medfreigabe, f.pstbha_bart, f.ppvbzt_aufnzt " + from
                + where + " ORDER BY e.ppveps_epsnr, f.ppvbzt_aufn_dat, f.ppvbzt_aufnzt";
        return query;
    }

    @Override
    protected String getLabordatenQuery() {
        String dateExtension = " ";
        //GKr
        //PPVEPS_EPSNR ist die Episoden Nummer aus Fall
        /*
             select e.ppveps_epsnr, l.untersuchungswert, l.untersuchungseinhe, l.normalwert, l.idklartext, l.erstelldatumzeit,l.lastupdate, lg.gruppe
             from kis.p_pveps_episode e, kis.labbef_position l, kis.labor_verfahren_hf211 lv, kis.labor_gruppe_hf211 lg
             where trim(e.ppveps_aufnr)=trim(l.aufnahmenr) and
             l.untersuchungsid = lv.verfahrennr and
             lv.gruppenr = lg.gruppenr and
             e.ppveps_datvon>20101201
             order by e.ppveps_epsnr;

         */
        if (getProperties().getGwiImpLaborForLastDays() != null) {
            Date dt = new Date();
            /* 3.9.5 2015-09-01 DNi: #FINDBUGS - Integer-Bereich könnte überschritten werden.
                 * Korrektur: dt.setTime(dt.getTime()-(m_gwi_imp_labor_forLastDays.intValue()*1000L*60*60*24));
             */
            dt.setTime(dt.getTime() - (getProperties().getGwiImpLaborForLastDays() * 1000 * 60 * 60 * 24));
            SimpleDateFormat nexusSdfDate = new SimpleDateFormat("yyyyMMdd");
            dateExtension = " AND e.ppveps_datvon >= " + nexusSdfDate.format(dt) + " ";
        }
        String fromWhere = "FROM " + getDbSchema() + "p_pveps_episode e, "
                + getDbSchema() + "p_pvbzt_behzt f, "
                + getDbSchema() + "labbef_position l "
                + "where trim(e.ppveps_aufnr)=trim(l.aufnahmenr) "
                + "AND f.ppveps_epsnr=e.ppveps_epsnr "
                + "AND l.storniert='N' "
                + "AND (l.untersuchungswert is not null and l.untersuchungswert not like '!%') AND "
                + getDateWhere()
                + getAufnahmeArten() + getAufnahmegruendeSQL()
                + dateExtension;

        String orderBy = " order by e.ppveps_epsnr, l.anforderungsnr, l.idklartext, l.erstelldatumzeit";

//            Object o = connection.getSingleSQLResult("SELECT COUNT(*) " + fromWhere);
//            if (o != null && o instanceof Number) {
//                results = ((Number) o).intValue();
//            }
        String query = "SELECT e.ppveps_epsnr"
                + ", l.untersuchungswert"
                + ", l.untersuchungseinhe"
                + ", l.normalwert"
                + ", l.idklartext"
                + ", l.erstelldatumzeit"
                + ", l.lastupdate"
                + ", null "
                + ", nvl(l.bewertung,decode(l.pathologisch, 1, ' ', null))"
                + ", l.anforderungsnr "
                + fromWhere + orderBy;
        return query;
    }

    @Override
    protected Map<String, Date> loadEntbindungsdaten() throws SQLException, IOException {
        Map<String, Date> result = new HashMap<>();
        String s = getDateWhere();
        if (s.length() > 0) {
            s = " AND " + s;
        }
        String where = "WHERE e.ppveps_epsnr=enb.ppveps_epsnr_sgl " + s;
        String from = "FROM " + getDbSchema() + "p_pveps_episode e, "
                + getDbSchema() + "p_pvenb_entbindung enb ";
        String fromWhere = from + where;
        String query = "SELECT e.ppveps_epsnr, enb.ppvenb_szeit "
                + fromWhere;

        executeStatement(query, rs -> {
            while (rs.next()) {
                String sglEpisode = rs.getString(1);
                Date entbindungsDatum = rs.getDate(2);
                if (sglEpisode != null && entbindungsDatum != null) {
                    result.put(sglEpisode, entbindungsDatum);
                }
            }
        });
        return result;
    }

    @Override
    protected String getPatientenQuery() {
        String s = getDateWhere() + getAufnahmeArten() + getAufnahmegruendeSQL();
        if (s.length() > 0) {
            s = " AND " + s;
        }
        String where = "WHERE e.ppvpat_patnr=p.ppvpat_patnr and f.ppveps_epsnr=e.ppveps_epsnr " + s;
        String from = "FROM " + getDbSchema() + "p_pveps_episode e, "
                + getDbSchema() + "p_pvbzt_behzt f, "
                + getDbSchema() + "p_pvpat_patient p ";
        String fromWhere = from + where;
//            Object o = connection.getSingleSQLResult("SELECT COUNT(*) " + fromWhere);
//            if (o != null && o instanceof Number) {
//                results = ((Number) o).intValue();
//            }
        String query = "SELECT e.ppveps_epsnr, p.ppvpat_patid, "
                + "p.ppvpat_name, p.ppvpat_vname, p.ppvpat_gdatum, p.astgsl_geschlecht "
                + ", p.ppvpat_kvk_mitgnr " //GKr Versicherungsnummer (Mitgliedsnummer) des Patienten bei der Kasse
                + fromWhere + " ORDER BY p.ppvpat_patid, e.ppveps_epsnr ";
        return query;
    }

    @Override
    protected String getUrlaubQuery() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String getStornoQuery() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeStorno() throws SQLException, IOException {
        //TODO Storno Fälle ermitteln
    }

    private String getDateWhere() {
        String aufVonDatum = getAufnahmedatumVon();
        String aufBisDatum = getAufnahmedatumBis();
        String entVonDatum = getEntlassungsdatumVon();
        String entBisDatum = getEntlassungsdatumBis();
        String date = "";
        if (aufVonDatum != null && !aufVonDatum.isEmpty()) { //&& !aufVonDatum.toUpperCase().equals(AppResources.getResource(AppResourceBundle.TXT_ALL_UPPER).toUpperCase())) {
            date = "f.ppvbzt_aufn_dat >= TO_DATE('" + aufVonDatum + " 00:00:00', 'DD.MM.YYYY HH24:MI:SS')";
        }
        if (aufBisDatum != null && !aufBisDatum.isEmpty()) { //&& !aufBisDatum.toUpperCase().equals(AppResources.getResource(AppResourceBundle.TXT_ALL_UPPER).toUpperCase())) {
            if (!date.isEmpty()) {
                date += " AND ";
            }
            date += "f.ppvbzt_aufn_dat <= TO_DATE('" + aufBisDatum + " 23:59:59', 'DD.MM.YYYY HH24:MI:SS')";
        }
        if (entVonDatum != null && !entVonDatum.isEmpty()) { //&& !entVonDatum.toUpperCase().equals(AppResources.getResource(AppResourceBundle.TXT_ALL_UPPER).toUpperCase())) {
            if (!date.isEmpty()) {
                date += " AND ";
            }
            //GKr 09.09.2016 Entlassdatum hat Aufnahmedatum überschrieben. Für Quedlinburg eingesetzt um Nicht entlassene Patienten zu vermeiden
            date += "f.ppvbzt_ent_dat >= TO_DATE('" + entVonDatum + " 00:00:00', 'DD.MM.YYYY HH24:MI:SS')";
        }
        if (entBisDatum != null && !entBisDatum.isEmpty()) { //&& !entBisDatum.toUpperCase().equals(AppResources.getResource(AppResourceBundle.TXT_ALL_UPPER).toUpperCase())) {
            if (!date.isEmpty()) {
                date += " AND ";
            }
            date += "f.ppvbzt_ent_dat <= TO_DATE('" + entBisDatum + " 23:59:59', 'DD.MM.YYYY HH24:MI:SS')";
        }
        if (date.length() == 0) {
            return "1=1";
        } else {
            return date;
        }
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
            result = " AND f.pstbha_bart IN (" + aufnahmeArten + ")";
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
            result = " AND f.pstaug_key IN (" + aufnahmeGruende + ")";
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
            result = " AND trim(d.pstdty_dokutyp) IN (" + diagnoseArten + ")";
//            }
        }
        return result;
    }

//    private String getMandant() {
//        String mandantStatement;
//        if (getProperties().getMandant() != null && !getProperties().getMandant().isEmpty()) {
//            mandantStatement = " AND allmnd_mandant = '" + getProperties().getMandant() + "' ";
//        } else {
//            mandantStatement = "";
//        }
//        return mandantStatement;
//    }
    public int getKisFallStatus(String freigabeKZN, Integer medFreigabe) {
        int kisstaus = 0;
        if (freigabeKZN != null) {
            if (getProperties().getNexusKisStatusDefHash() != null) {
                String mapValue = "";
                mapValue = getProperties().getNexusKisStatusDefHash().get(freigabeKZN);
                if (mapValue != null && !mapValue.isEmpty()) {
                    kisstaus = Integer.parseInt(mapValue);
                } else {
                    LOG.log(Level.SEVERE, "Nexus KisStatus: '" + freigabeKZN + "' wurde nicht in der sst.properties definiert");
                }
            } else {
                if (freigabeKZN.equals("O")) {
                    kisstaus = 401;
                } else if (freigabeKZN.equals("D")) {
                    kisstaus = 402;
                } else if (freigabeKZN.equals("F")) {
                    kisstaus = 403;
                } else if (freigabeKZN.equals("A")) {
                    kisstaus = 404;
                }
            }
        }
        if (kisstaus > 0 && medFreigabe != null) {
            if (medFreigabe >= 0) {
                kisstaus = kisstaus + 10;
            }
        }
        return kisstaus;
    }

    @Override
    protected Map<String, String> loadStationen() throws SQLException, IOException {
        Map<String, String> result = new HashMap<>();
        String query = "SELECT trim(pststa_station), pststa_name  FROM " + getDbSchema()
                + "p_ststa_station" + getMandantenWhere();
        executeStatement(query, rs -> {
            while (rs.next()) {
                String staSchl = rs.getString(1);
                String staName = rs.getString(2);
                if (staSchl != null && staName != null) {
                    result.put(staSchl.trim(), staName);
                }
            }
        });
        return result;
    }

    private String getMandantenWhere() {
        String mandantWhereStatement;
        if (getProperties().getMandant() != null && !getProperties().getMandant().isEmpty()) {
            mandantWhereStatement = " WHERE allmnd_mandant = '" + getProperties().getMandant() + "' ";
        } else {
            mandantWhereStatement = "";
        }
        return mandantWhereStatement;
    }

    @Override
    protected Map<String, Integer> loadUrlaub() throws SQLException, IOException {
        Map<String, Integer> result = new HashMap<>();

        String s = getDateWhere() + getAufnahmeArten() + getAufnahmegruendeSQL();
        if (s.length() > 0) {
            s = " AND " + s;
        }
        String where = "WHERE e.ppveps_epsnr=ai.ppveps_epsnr and f.ppveps_epsnr=e.ppveps_epsnr "
                + " AND ai.ppvabi_entdt IS NOT NULL " + s;
        String from = "FROM " + getDbSchema() + "p_pveps_episode e, "
                + getDbSchema() + "p_pvbzt_behzt f, "
                + getDbSchema() + "p_pvabi_abr_info ai ";

        String query = "SELECT e.ppveps_epsnr, e.ppveps_datbis, ai.ppvabi_entdt "
                + from + where + " ORDER BY e.ppveps_epsnr ";

        executeStatement(query, rs -> {
            while (rs.next()) {
                String fallid = rs.getString(1);
                String objDisDate = rs.getString(2);
                String objDisDate_MDK = rs.getString(3);
                if (fallid != null && objDisDate != null && objDisDate_MDK != null) {
                    long tobValue = 0;
                    Date disDate = getNexusDate(objDisDate);
                    Date disDate_MDK = getNexusDate(objDisDate_MDK);
                    if (disDate != null && disDate_MDK != null) {
                        if (disDate.after(disDate_MDK)) {
                            tobValue = UtlDateTimeConverter.converter().daysBetween(disDate, disDate_MDK);
                        } else {
                            tobValue = UtlDateTimeConverter.converter().daysBetween(disDate_MDK, disDate);
                        }
                        if (tobValue > 0) {
                            result.put(fallid, (int) tobValue);
                        }
                    }
                }
            }
        });
        return result;
    }

    @Override
    public Integer getDiagnoseart(String dit) {
        if (dit == null || !(dit instanceof String)) {
            return 0;
        } else {
            dit = dit.trim();
        }
        if (dit.equalsIgnoreCase("DIAG-AUFN")) {
            return 2;
        }
        if (dit.equalsIgnoreCase("DIAG-EINW")) {
            return 1;
        }
        if (dit.equalsIgnoreCase("DIAG-FAKT")) {
            return 16;
        }
        if (dit.equalsIgnoreCase("DIAG-FABT")) {
            return 15;
        }
        if (dit.equalsIgnoreCase("DIAG-ENTL")) {
            return 4;
        }
        return 0;
    }

    @Override
    protected String getNachfolgerQuery() {
        String s = getDateWhere();
        if (s.length() > 0) {
            s = " AND " + s;
        }
        String from = "FROM " + getDbSchema() + "p_pvfkl_fallklammer fk1, " + getDbSchema() + "p_pvfkl_fallklammer fk2, "
                + getDbSchema() + "p_pveps_episode e, " + getDbSchema() + "p_pvbzt_behzt f ";
        String where = "WHERE fk1.ppvfkk_fallnummer=fk2.ppvfkk_fallnummer and "
                + "fk1.PPVEPS_EPSNR<>fk2.PPVEPS_EPSNR and "
                + "fk1.ppvfkl_fuehrender_fall='J' and "
                + "fk1.ppvfkl_freigabe_kzn='J' and " //In Quedlinburg
                + "fk1.PPVEPS_EPSNR=e.ppveps_epsnr and f.ppveps_epsnr=e.ppveps_epsnr " + s;
//            Object o = connection.getSingleSQLResult("SELECT COUNT(*) " + from + where);
//            if (o != null && o instanceof Number) {
//                results = ((Number) o).intValue();
//            }
        String query = "SELECT DISTINCT fk1.PPVEPS_EPSNR, fk2.PPVEPS_EPSNR " + from + where
                + " ORDER BY fk1.PPVEPS_EPSNR, fk2.PPVEPS_EPSNR";
        return query;
    }

    @Override
    protected Map<String, String> loadTransferHospLess24Hours() throws IOException, SQLException {
        Map<String, String> result = new HashMap<>();

        String s = getDateWhere() + getAufnahmeArten() + getAufnahmegruendeSQL();
        if (s.length() > 0) {
            s = " AND " + s;
        }
        String where = "WHERE e.ppveps_epsnr=ai.ppveps_epsnr and f.ppveps_epsnr=e.ppveps_epsnr "
                + " AND ai.ppvabi_max_24_std_and_kh='J' " + s;
        String from = "FROM " + getDbSchema() + "p_pveps_episode e, "
                + getDbSchema() + "p_pvbzt_behzt f, "
                + getDbSchema() + "p_pvabi_abr_info ai ";

        String query = "SELECT e.ppveps_epsnr, ai.ppvabi_max_24_std_and_kh "
                + from + where + " ORDER BY e.ppveps_epsnr ";

        executeStatement(query, rs -> {
            while (rs.next()) {
                String fallid = rs.getString(1);
                String objTransferLess24H = rs.getString(2);
                if (fallid != null && objTransferLess24H != null) {
                    result.put(fallid, objTransferLess24H);
                }
            }
        });
        return result;
    }

    @Override
    protected Map<String, KisInsuranceContainer> loadInsurances() throws IOException, SQLException {
        Map<String, KisInsuranceContainer> tmpResult = new HashMap<>();
        String s = getDateWhere() + getAufnahmeArten() + getAufnahmegruendeSQL();
        String from = "FROM " + getDbSchema() + "p_pveps_episode e, "
                + getDbSchema() + "p_pvbzt_behzt f, "
                + getDbSchema() + "p_stauf_aufart aa, "
                + getDbSchema() + "p_pvpat_patient p, "
                + getDbSchema() + "p_pvrep_re r";
        if (s.length() <= 0) {
            s = "1=1";
        }
        String where = " where f.ppveps_epsnr=e.ppveps_epsnr "
                + "and aa.pstauf_art=f.pstauf_art "
                + "and e.ppvpat_patnr=p.ppvpat_patnr "
                + "and e.ppveps_epsnr = r.ppveps_epsnr(+) "
                + "and 'J'=r.ppvrep_hre "
                + "and 'N'=r.ppvrep_av "
                + "and 'P'<> r.pstmit_art "
                + "and 'Z' <> r.pstmit_art "
                + "and " + s;

        String query = "SELECT DISTINCT "
                + "e.ppveps_epsnr, " //0
                + "f.ppvbzt_behnr, " //1
                + "p.ppvpat_patid, " //2
                + "p.ppvpat_patnr, " //3
                //Fallback wurd in Absprache mit Frau Duerre wieder entfernt
                //                    + "nvl(r.ppvrep_mitgnr,p.ppvpat_kvk_mitgnr) as mitgl_nr, "      //4
                //                    + "nvl(r.ppvrep_kvk_iknr,p.ppvpat_kvk_kass_nr) as kasse_nr, "   //5
                + "r.ppvrep_mitgnr, " //4
                + "r.ppvrep_kvk_iknr, " //5
                + "r.ppvrep_lfdnr, " //6
                + "r.pstpvk_pverskreis, " //7
                + "r.pstmit_art, " //8
                + "r.pstrem_ident, " //9
                + "r.ppvrep_hre, " //10
                + "r.ppvrep_av " //11
                + from
                + where + " ORDER BY e.ppveps_epsnr";

        executeStatement(query, rs -> {
            while (rs.next()) {
                String ppveps_epsnr = rs.getString(1);
                String ppvbzt_behnr = rs.getString(2);
                String ppvpat_patid = rs.getString(3);
                String ppvpat_patnr = rs.getString(4);
                String mitgl_nr = rs.getString(5);
                String kasse_nr = rs.getString(6);
                String ppvrep_lfdnr = rs.getString(7);
                String pstpvk_pverskreis = rs.getString(8);
                String pstmit_art = rs.getString(9);
                String pstrem_ident = rs.getString(10);
                String ppvrep_hre = rs.getString(11);
                String ppvrep_av = rs.getString(12);

                KisInsuranceContainer nexusCaseInsurance = new KisInsuranceContainer(
                        ppveps_epsnr, ppvbzt_behnr,
                        ppvpat_patid, ppvpat_patnr,
                        mitgl_nr, kasse_nr, ppvrep_lfdnr,
                        pstpvk_pverskreis, pstmit_art, pstrem_ident,
                        ppvrep_hre, ppvrep_av);

                KisInsuranceContainer entryFromCaseMap = tmpResult.get(nexusCaseInsurance.getPpvepsEpsnr());

                if (entryFromCaseMap != null) {
                    LOG.log(Level.FINEST, "DBG->loadCasePatientInsurance->EpisodenNr bereits vorhanden in nexusCasePatientInsuranceHMap: " + entryFromCaseMap.getPpvepsEpsnr());

                    if ((entryFromCaseMap.getMitglNr() == null || entryFromCaseMap.getMitglNr().isEmpty())
                            && nexusCaseInsurance.getMitglNr() != null && !nexusCaseInsurance.getMitglNr().isEmpty()) {
                        LOG.log(Level.FINEST, "DBG->loadCasePatientInsurance->MitglNr im CaseMapEntry ist nicht vorhanden, aber im aktuellen Objekt: " + nexusCaseInsurance.getMitglNr());
                        entryFromCaseMap.setMitglNr(nexusCaseInsurance.getMitglNr());
                    }

                    if ((entryFromCaseMap.getKasseNr() == null || entryFromCaseMap.getKasseNr().isEmpty())
                            && nexusCaseInsurance.getKasseNr() != null && !nexusCaseInsurance.getKasseNr().isEmpty()) {
                        LOG.log(Level.FINEST, "DBG->loadCasePatientInsurance->KasseNr im CaseMapEntry ist nicht vorhanden, aber im aktuellen Objekt: " + nexusCaseInsurance.getKasseNr());
                        entryFromCaseMap.setKasseNr(nexusCaseInsurance.getKasseNr());
                    }
                } else {
                    tmpResult.put(nexusCaseInsurance.getPpvepsEpsnr(), nexusCaseInsurance);
                }
            }
        });

        //Daten in m_nexusCaseInsuranceHMap geladen - Nun wird aus dieser HashMap die nächste HMap gefüllt
        Map<String, KisInsuranceContainer> result = new HashMap<>();
        Iterator<Entry<String, KisInsuranceContainer>> entryIterator = tmpResult.entrySet().iterator();
        while (entryIterator.hasNext()) {
            Entry<String, KisInsuranceContainer> entry = entryIterator.next();
            KisInsuranceContainer nexusPatientInsurance = entry.getValue();

            String patId = nexusPatientInsurance.getPpvpatPatid();
            KisInsuranceContainer entryFromPatientMap = result.get(patId);
            if (entryFromPatientMap != null) {
                if ((entryFromPatientMap.getMitglNr() == null || entryFromPatientMap.getMitglNr().isEmpty())
                        && nexusPatientInsurance.getMitglNr() != null && !nexusPatientInsurance.getMitglNr().isEmpty()) {
                    LOG.log(Level.FINEST, "DBG->loadCasePatientInsurance->MitglNr im PatientMapEntry ist nicht vorhanden, aber im aktuellen Objekt: " + nexusPatientInsurance.getMitglNr());
                    entryFromPatientMap.setMitglNr(nexusPatientInsurance.getMitglNr());

                }
                if ((entryFromPatientMap.getKasseNr() == null || entryFromPatientMap.getKasseNr().isEmpty())
                        && nexusPatientInsurance.getKasseNr() != null && !nexusPatientInsurance.getKasseNr().isEmpty()) {
                    LOG.log(Level.FINEST, "DBG->loadCasePatientInsurance->KasseNr im PatientMapEntry ist nicht vorhanden, aber im aktuellen Objekt: " + nexusPatientInsurance.getKasseNr());
                    entryFromPatientMap.setKasseNr(nexusPatientInsurance.getKasseNr());
                }
            } else {
                result.put(patId, nexusPatientInsurance);
            }
        }
        return result;
    }

    private static Date getNexusDate(String nexusDate) {
        Date date = null;
        if (nexusDate != null && nexusDate.length() == 8) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            try {
                date = sdf.parse(nexusDate);
            } catch (ParseException ex) {
                LOG.log(Level.WARNING, "Fehler beim Dateparsing: " + nexusDate, ex);
            }
        }
        return date;
    }

    private static Date getNexusDate(String nexusDate, String nexusTime) {
        Date date = null;
        if (nexusDate != null && (nexusTime == null || nexusTime.equals("0"))) {
            return getNexusDate(nexusDate);
        } else if (nexusDate != null && nexusTime != null) {
            String dateTime = "";
            if (nexusDate.length() == 8) {
                dateTime = nexusDate;
            }
            SimpleDateFormat nexusSdfTime = new SimpleDateFormat("yyyyMMddHHmm");
            if (nexusTime.length() == 4) {
                dateTime += nexusTime;
                try {
                    date = nexusSdfTime.parse(dateTime);
                } catch (ParseException ex) {
                    LOG.log(Level.WARNING, "Falsches Datumsformat: " + dateTime, ex);
                    return null;
                }
            } else if (nexusTime.length() < 4) {
                int len = nexusTime.length();
                for (int i = 0; i < 4 - len; i++) {
                    dateTime += "0";
                }
                dateTime += nexusTime;
                try {
                    date = nexusSdfTime.parse(dateTime);
                } catch (ParseException ex) {
                    LOG.log(Level.WARNING, "Falsches Datumsformat: " + dateTime, ex);
                    return null;
                }
            }
            /*if(nexusTime.toString().length()==4)
             dateTime += " "+nexusTime.toString().substring(0,1)
             + ":"+nexusTime.toString().substring(2,3)
             + ":00";
             else if(nexusTime.toString().length()==6)
             dateTime += " "+nexusTime.toString().substring(0,1)
             + ":"+nexusTime.toString().substring(2,3)
             + ":"+nexusTime.toString().substring(4,5);
             else{
             logger.info("Nexus falsche Datumsformat");
             return null;
             }
             try {
             date = sdfTime.parse(dateTime);
             } catch (ParseException e) {
             logger.info("Falsches Datumsformat: "+dateTime);
             return null;
             }*/
        }
        return date;
    }

    private String getStringWhere(String attrName, List<String> val) {
        String result = "";
        if (val != null && !val.isEmpty()) {
            Object[] specs = val.toArray();
            result = " AND ";
            result += "(";
            for (int i = 0; i < specs.length; i++) {
                result += attrName + " LIKE '" + specs[i].toString() + "' OR ";
            }
            result = result.substring(0, result.length() - 4) + ")";
        }
        return result;
    }

    /**
     * Im Zuge der Unterschiede innerhalb der NEXUS-DB's beim Kunden, muss fuer
     * die IKZ eine Pruefung auf 9-stellig gemacht werden. In einigen
     * Einrichtungen ist die ausgelesene IKZ zu kurz, in anderen Einrichtungen
     * stimmt die Laenge (Bsp Kiel und Quedlinburg). Im Rahmen unserer
     * bisherigen Erfahrung, wird vor 7-stelligen Nummern eine 10 vorangesetzt
     *
     * @param objIKZ
     * @return
     */
    private static String pruefeIKZ9stellig(String objIKZ) {
        String retVal = "";

        if (objIKZ != null && !objIKZ.trim().isEmpty()) {
            String tmp = objIKZ;
            retVal = tmp.trim();

            if (retVal.length() != 9) {
                if (retVal.length() == 7 && !retVal.equalsIgnoreCase("0000000")) { //Kiel
                    retVal = "10" + retVal;
                }
            }
        }
        return retVal;
    }

//    private int getVwdIntensivSecondCase(String strBuffer2) {
//        int vwdIntSecondCase = 0;
//
//        if (strBuffer2 != null) {
//            String[] bufferContent = strBuffer2.split(";");
//            if (bufferContent != null && bufferContent.length >= 2) {
//                String strVwdInt = bufferContent[1];
//                try {
//                    vwdIntSecondCase = Integer.parseInt(strVwdInt);
//                } catch (NumberFormatException ex) {
//                    LOG.log(Level.FINEST, "Cannot parse value as integer: " + strVwdInt, ex);
//                }
//            }
//        }
//        return vwdIntSecondCase;
//    }    
}
