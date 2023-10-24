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
package de.lb.cpx.db.container;

//import java.util.*;
//
//public class KisFallContainer {
//
//    private static Date largeValidDate = new GregorianCalendar(2080, 1, 1, 0, 0, 0).getTime();
//
//    private int id = -1;
//    private int idIntern = -1;
//    private int idExtern = -1;
//    private int patientid = -1;
//    private int urlaub = 0;
//    private int isDrg = 0;
//    private int nalos = 0;
//    private int dischargeReason = 1;
//    private String sendBuffer1 = "";
//    private String sendBuffer2 = "";
//    private String sendBuffer3 = "";
//    private List<KisBewegungContainer> bewegungen = new ArrayList<>();
//    private List<KisHosWardContainer> stationen = new ArrayList<>();
//    private Date aufnahmeDatum;
//    private Date entlassungsDatum;
//    private int urlaubVon = 0;   // ich brauche Urlaub nur auf Tage genau
//    private int urlaubBis = 0;
//    private int fallStatus = 0;
//    private String picd = null;
//    private List<String> icds = new ArrayList<>();
//    private List<String> ops = new ArrayList<>();
//    private String feld1 = "";
//    private String feld2 = "";
//    private String feld3 = "";
//    private String feld4 = "";
//    private String feld5 = "";
//    private int numeric1 = 0;
//    private int numeric2 = 0;
//    private int numeric3 = 0;
//    private int numeric4 = 0;
//    private int numeric5 = 0;
//    private Date faktfrei;
//    private int tob = 0;
//    private String fallNr;
//    private String ikz;
//
//    public KisFallContainer() {
//    }
//
//    private static final Comparator<KisBewegungContainer> DATE_COMP = new Comparator<>() {
//        @Override
//        public int compare(KisBewegungContainer o1, KisBewegungContainer o2) {
//            if (o1 instanceof KisBewegungContainer && o2 instanceof KisBewegungContainer) {
//                Date d1 = o1.getAnfang();
//                Date d2 = o2.getAnfang();
//                if (d1 == null) {
//                    d1 = getLargeValidDate();
//                }
//                if (d2 == null) {
//                    d2 = getLargeValidDate();
//                }
//                if (d1 != null && d2 != null) {
//                    return d1.compareTo(d2);
//                } else {
//                    return -1;
//                }
//            } else {
//                return -1;
//            }
//        }
//    };
//
//    private static final Comparator<KisHosWardContainer> HOS_WARD_DATE_COMP = new Comparator<>() {
//        @Override
//        public int compare(KisHosWardContainer o1, KisHosWardContainer o2) {
//            if (o1 instanceof KisHosWardContainer && o2 instanceof KisHosWardContainer) {
//                Date d1 = o1.getAnfang();
//                Date d2 = o2.getAnfang();
//                if (d1 == null) {
//                    d1 = getLargeValidDate();
//                }
//                if (d2 == null) {
//                    d2 = getLargeValidDate();
//                }
//                if (d1 != null && d2 != null) {
//                    return d1.compareTo(d2);
//                } else {
//                    return -1;
//                }
//            } else {
//                return -1;
//            }
//        }
//    };
//
//    public void createDefaultBewegung(Date startDate, Date endDate) {
//        KisBewegungContainer b = new KisBewegungContainer();
//        if (startDate != null && startDate instanceof java.util.Date) {
//            b.setAnfang(startDate);
//        }
//        if (endDate != null && endDate instanceof java.util.Date) {
//            b.setEnde(endDate);
//        }
//        getBewegungen().clear();
//        getBewegungen().add(b);
//    }
//
//    private KisBewegungContainer tryToMergeORAdd(KisBewegungContainer lastBew, KisBewegungContainer newBew) {
//        if (lastBew == null || lastBew.getEnde() == null || lastBew.getAnfang() == null) {
//            return null;
//        }
//        int t1 = (int) (lastBew.getEnde().getTime() / 1000 / 60);
//        int t2 = (int) (newBew.getAnfang().getTime() / 1000 / 60);
//        int diff = t2 > t1 ? (t2 - t1) : (t1 - t2);
//        if (lastBew.getAbteilung().equals(newBew.getAbteilung()) && diff < 5) {
//            lastBew.setEnde(newBew.getEnde());
//            lastBew.addVwdIntensiv(newBew.getVwdInt());
//            lastBew.addVwdIntensiv(newBew.getVwdIntDouble());
//            return lastBew;
//        } else {
//            getBewegungen().add(newBew);
//            return newBew;
//        }
//    }
//
//    public KisBewegungContainer addBewegung(KisBewegungContainer b) {
//        return addBewegung(b, true);
//    }
//
//    public KisBewegungContainer addBewegung(KisBewegungContainer b, boolean tryToMerge) {
//        int size = getBewegungen().size();
//        if (size == 1) {
//            KisBewegungContainer bb = getBewegungen().get(0);
//            if (bb.getIsDefault() != 0) {
//                getBewegungen().clear(); // defaultbewegung löschen
//                getBewegungen().add(b);
//            } else {
//                if (tryToMerge) {
//                    return tryToMergeORAdd(bb, b);
//                } else {
//                    getBewegungen().add(b);
//                }
//            }
//        } else if (size > 0) {
//            if (tryToMerge) {
//                return tryToMergeORAdd(getBewegungen().get(size - 1), b);
//            } else {
//                getBewegungen().add(b);
//            }
//        } else {
//            getBewegungen().add(b);
//        }
//        return b;
//    }
//
//    public int getBewegungsID(Object dateOrAbtID, KisFallContainer fall) {
//        KisBewegungContainer b = getBewegungs(dateOrAbtID, fall);
//        if (b != null) {
//            return b.getId();
//        } else {
//            return 0;
//        }
//    }
//
//    public KisHosWardContainer addStation(KisHosWardContainer station, boolean tryToMerge) {
//        int size = getStationen().size();
//        if (size == 1) {
//            KisHosWardContainer bb = getStationen().get(0);
//            if (tryToMerge) {
//                return tryToMergeORAddHosWard(bb, station);
//            } else {
//                getStationen().add(station);
//            }
//        } else if (size > 0) {
//            if (tryToMerge) {
//                return tryToMergeORAddHosWard(getStationen().get(size - 1), station);
//            } else {
//                getStationen().add(station);
//            }
//        } else {
//            getStationen().add(station);
//        }
//        return station;
//    }
//
//    public int getStationsID(Object dateObj) {
//        if (dateObj != null && dateObj instanceof Date) {
//            Date date = (Date) dateObj;
//            KisHosWardContainer obj = null;
//            KisHosWardContainer station = null;
//            for (int i = 0, n = getStationen().size(); i < n; i++) {
//                obj = getStationen().get(i);
//                if (obj != null && obj instanceof KisHosWardContainer) {
//                    station = obj;
//                    if (station.getAnfang() != null && station.getEnde() != null
//                            && date.compareTo(station.getAnfang()) >= 0 && date.compareTo(station.getEnde()) <= 0) {
//                        return station.getId();
//                    }
//                }
//                //wenn letzte Station ohne Entlassungsdatum
//                if (station != null && station.getAnfang() != null && date.compareTo(station.getAnfang()) >= 0) {
//                    return station.getId();
//                }
//            }
//            if (!stationen.isEmpty()) {
//                return getStationen().get(getStationen().size() - 1).getId();
//            }
//        }
//        return -1;
//    }
//
//    public int getNexusStationsID(Date dateObj) {
//
//        int size = getStationen().size();
//
//        if (dateObj != null && dateObj instanceof Date) {
//            //Date date = dateObj;
//            //Object obj = null;
//            KisHosWardContainer station = null;
//            for (int i = size - 1; i >= 0; i--) {
//                station = getStationen().get(i);
//                if (station.getAnfang() != null && station.getEnde() != null
//                        && (station.getAnfang().before(dateObj) && station.getEnde().after(dateObj)
//                        || station.getAnfang().equals(dateObj) || station.getEnde().equals(dateObj))) {
//                    return station.getId();
//                } else if (station.getAnfang() != null && (station.getAnfang().before(dateObj) || station.getAnfang().equals(dateObj))) {
//                    return station.getId();
//                }
//            }
//            for (int i = 0; i < size; i++) {
//                station = getStationen().get(i);
//                if (station.getEnde() != null && (station.getEnde().after(dateObj) || station.getEnde().equals(dateObj))) {
//                    return station.getId();
//                }
//            }
//
//            station = getStationen().get(size - 1);
//            return station.getId();
//        }
//        return -1;
//    }
//
//    public int getBewegungsTypschl(Object dateOrAbtID, KisFallContainer fall) {
//        KisBewegungContainer b = getBewegungs(dateOrAbtID, fall);
//        if (b != null) {
//            return b.getTypschluessel();
//        } else {
//            return 1;
//        }
//    }
//
//    public String getBewegungsTyp(Object dateOrAbtID, KisFallContainer fall) {
//        KisBewegungContainer b = getBewegungs(dateOrAbtID, fall);
//        if (b != null) {
//            return b.getTyp();
//        } else {
//            return "HA";
//        }
//    }
//
//    public KisBewegungContainer getBewegungs(Object dateOrAbtID, KisFallContainer fall) {
//        int size = getBewegungen().size();
//        KisBewegungContainer b;
//
//        if (dateOrAbtID != null) {
//            List<KisBewegungContainer> bewegungen = fall.getBewegungen();
//            if (size > 1) {
//                if (dateOrAbtID instanceof String) {
//                    for (int i = size - 1; i >= 0; i--) {
//                        b = bewegungen.get(i);
//                        if (b.getAbtId().equals(dateOrAbtID)) {
//                            return b;
//                        }
//                    }
//                } else {
//                    for (int i = size - 1; i >= 0; i--) {
//                        b = bewegungen.get(i);
//                        if (b.getAnfang() != null && b.getEnde() != null
//                                && (b.getAnfang().before((Date) dateOrAbtID) && b.getEnde().after((Date) dateOrAbtID)
//                                || b.getAnfang().equals(dateOrAbtID) || b.getEnde().equals(dateOrAbtID))) {
//                            return b;
//                        } else if (b.getAnfang() != null && (b.getAnfang().before((Date) dateOrAbtID) || b.getAnfang().equals(dateOrAbtID))) {
//                            return b;
//                        }
//                    }
//                    for (int i = 0; i < size; i++) {
//                        b = bewegungen.get(i);
//                        if (b.getEnde() != null && (b.getEnde().after((Date) dateOrAbtID) || b.getEnde().equals(dateOrAbtID))) {
//                            return b;
//                        }
//                    }
//                }
//                b = bewegungen.get(size - 1);
//                return b;
//            }
//        }
//        if (size >= 1) {
//            b = getBewegungen().get(size - 1);
//            return b;
//        }
//        return null;
//    }
//
//    public void calcBewegungenFlagsNummerieren() {
//        if (getBewegungen().size() > 1) {
//            Collections.sort(getBewegungen(), DATE_COMP);
//            long dauer = 0;
//            int index = -1;
//            KisBewegungContainer beh = null;
//            KisBewegungContainer b;
//            for (int i = 0, n = getBewegungen().size() - 1; i <= n; i++) {
//                b = getBewegungen().get(i);
//                b.setBehandelnde((byte) 0);
//                b.setEntlassende((byte) 0);
//                //Fehler aufnehmende immer auf 1 TDu 03.01.2008
//                b.setAufnehmende((byte) 0);
//                b.setNummer(i + 1);
//                if (i == 0) {
//                    b.setAufnehmende((byte) 1);
//                } else if (i == n) {
//                    b.setEntlassende((byte) 1);
//                }
//                if (b.getAnfang() != null) {
//                    if (b.getEnde() != null) {
//                        long diff = b.getEnde().getTime() - b.getAnfang().getTime();
//                        if (diff > dauer) {
//                            dauer = diff;
//                            index = i;
//                        }
//                    } else {
//                        index = i;
//                    }
//                }
//            }
//            if (index >= 0) {
//                b = getBewegungen().get(index);
//                b.setBehandelnde((byte) 1);
//            }
//        }
//    }
//
//    public void calcHosWardFlagsNummerieren() {
//        if (getStationen().size() > 1) {
//            Collections.sort(getStationen(), HOS_WARD_DATE_COMP);
//            KisHosWardContainer b;
//            for (int i = 0, n = getStationen().size() - 1; i <= n; i++) {
//                b = getStationen().get(i);
//                if (b != null) {
//                    b.setTyp(0);
//                    if (i == 0) {
//                        b.setTyp(1);
//                    } else if (i == n) {
//                        b.setTyp(3);
//                    } else if (i > 0 && i < n) {
//                        b.setTyp(2);
//                    }
//                    if (n == 0) {
//                        b.setTyp(4);
//                    }
//                }
//            }
//        } else if (getStationen().size() == 1) {
//            KisHosWardContainer b;
//            b = getStationen().get(0);
//            if (b != null) {
//                b.setTyp(4);
//            }
//        }
//    }
//
//    public int containsICD(String icd) {
//        if (getIsDrg() == 0) {
//            return 1;
//        }
//        if (this.getIcds().contains(icd)) {
//            getIcds().remove(icd);
//            return 1;
//        } else {
//            return 0;
//        }
//    }
//
//    public int containsOPS(String icpm) {
//        if (getIsDrg() == 0) {
//            return 1;
//        }
//        if (this.getOps().contains(icpm)) {
//            return 1;
//        } else {
//            return 0;
//        }
//
//    }
//
//    private KisHosWardContainer tryToMergeORAddHosWard(KisHosWardContainer lastWard, KisHosWardContainer newWard) {
//        if (lastWard == null || lastWard.getEnde() == null || lastWard.getAnfang() == null) {
//            return null;
//        }
//        int t1 = (int) (lastWard.getEnde().getTime() / 1000 / 60);
//        int t2 = (int) (newWard.getAnfang().getTime() / 1000 / 60);
//        int diff = t2 > t1 ? (t2 - t1) : (t1 - t2);
//        if (lastWard.getWard().equals(newWard.getWard()) && diff < 5) {
//            lastWard.setEnde(newWard.getEnde());
//            lastWard.addVWDIntensiv(newWard.getBeatmungsdauer());
//            return lastWard;
//        } else {
//            getStationen().add(newWard);
//            return newWard;
//        }
//    }
//
//    /**
//     * @return the largeValidDate
//     */
//    public static Date getLargeValidDate() {
//        return largeValidDate == null ? null : new Date(largeValidDate.getTime());
//    }
//
//    /**
//     * @return the id
//     */
//    public int getId() {
//        return id;
//    }
//
//    /**
//     * @return the idIntern
//     */
//    public int getIdIntern() {
//        return idIntern;
//    }
//
//    /**
//     * @return the idExtern
//     */
//    public int getIdExtern() {
//        return idExtern;
//    }
//
//    /**
//     * @return the patientid
//     */
//    public int getPatientid() {
//        return patientid;
//    }
//
//    /**
//     * @return the urlaub
//     */
//    public int getUrlaub() {
//        return urlaub;
//    }
//
//    /**
//     * @return the isDrg
//     */
//    public int getIsDrg() {
//        return isDrg;
//    }
//
//    /**
//     * @return the nalos
//     */
//    public int getNalos() {
//        return nalos;
//    }
//
//    /**
//     * @return the dischargeReason
//     */
//    public int getDischargeReason() {
//        return dischargeReason;
//    }
//
//    /**
//     * @return the sendBuffer1
//     */
//    public String getSendBuffer1() {
//        return sendBuffer1;
//    }
//
//    /**
//     * @return the sendBuffer2
//     */
//    public String getSendBuffer2() {
//        return sendBuffer2;
//    }
//
//    /**
//     * @return the sendBuffer3
//     */
//    public String getSendBuffer3() {
//        return sendBuffer3;
//    }
//
//    /**
//     * @return the bewegungen
//     */
//    public List<KisBewegungContainer> getBewegungen() {
//        return bewegungen == null ? null : new ArrayList<>(bewegungen);
//    }
//
//    /**
//     * @return the stationen
//     */
//    public List<KisHosWardContainer> getStationen() {
//        return stationen == null ? null : new ArrayList<>(stationen);
//    }
//
//    /**
//     * @return the aufnahmeDatum
//     */
//    public Date getAufnahmeDatum() {
//        return aufnahmeDatum == null ? null : new Date(aufnahmeDatum.getTime());
//    }
//
//    /**
//     * @return the entlassungsDatum
//     */
//    public Date getEntlassungsDatum() {
//        return entlassungsDatum == null ? null : new Date(entlassungsDatum.getTime());
//    }
//
//    /**
//     * @return the urlaubVon
//     */
//    public int getUrlaubVon() {
//        return urlaubVon;
//    }
//
//    /**
//     * @return the urlaubBis
//     */
//    public int getUrlaubBis() {
//        return urlaubBis;
//    }
//
//    /**
//     * @return the fallStatus
//     */
//    public int getFallStatus() {
//        return fallStatus;
//    }
//
//    /**
//     * @return the picd
//     */
//    public String getPicd() {
//        return picd;
//    }
//
//    /**
//     * @return the icds
//     */
//    public List<String> getIcds() {
//        return icds == null ? null : new ArrayList<>(icds);
//    }
//
//    /**
//     * @return the ops
//     */
//    public List<String> getOps() {
//        return ops == null ? null : new ArrayList<>(ops);
//    }
//
//    /**
//     * @return the feld1
//     */
//    public String getFeld1() {
//        return feld1;
//    }
//
//    /**
//     * @return the feld2
//     */
//    public String getFeld2() {
//        return feld2;
//    }
//
//    /**
//     * @return the feld3
//     */
//    public String getFeld3() {
//        return feld3;
//    }
//
//    /**
//     * @return the feld4
//     */
//    public String getFeld4() {
//        return feld4;
//    }
//
//    /**
//     * @return the feld5
//     */
//    public String getFeld5() {
//        return feld5;
//    }
//
//    /**
//     * @return the numeric1
//     */
//    public int getNumeric1() {
//        return numeric1;
//    }
//
//    /**
//     * @return the numeric2
//     */
//    public int getNumeric2() {
//        return numeric2;
//    }
//
//    /**
//     * @return the numeric3
//     */
//    public int getNumeric3() {
//        return numeric3;
//    }
//
//    /**
//     * @return the numeric4
//     */
//    public int getNumeric4() {
//        return numeric4;
//    }
//
//    /**
//     * @return the numeric5
//     */
//    public int getNumeric5() {
//        return numeric5;
//    }
//
//    /**
//     * @return the faktfrei
//     */
//    public Date getFaktfrei() {
//        return faktfrei == null ? null : new Date(faktfrei.getTime());
//    }
//
//    /**
//     * @return the tob
//     */
//    public int getTob() {
//        return tob;
//    }
//
//    /**
//     * @param aLargeValidDate the largeValidDate to set
//     */
//    public static void setLargeValidDate(Date aLargeValidDate) {
//        largeValidDate = aLargeValidDate == null ? null : new Date(aLargeValidDate.getTime());
//    }
//
//    /**
//     * @param id the id to set
//     */
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    /**
//     * @param idIntern the idIntern to set
//     */
//    public void setIdIntern(int idIntern) {
//        this.idIntern = idIntern;
//    }
//
//    /**
//     * @param idExtern the idExtern to set
//     */
//    public void setIdExtern(int idExtern) {
//        this.idExtern = idExtern;
//    }
//
//    /**
//     * @param patientid the patientid to set
//     */
//    public void setPatientid(int patientid) {
//        this.patientid = patientid;
//    }
//
//    /**
//     * @param urlaub the urlaub to set
//     */
//    public void setUrlaub(int urlaub) {
//        this.urlaub = urlaub;
//    }
//
//    /**
//     * @param isDrg the isDrg to set
//     */
//    public void setIsDrg(int isDrg) {
//        this.isDrg = isDrg;
//    }
//
//    /**
//     * @param nalos the nalos to set
//     */
//    public void setNalos(int nalos) {
//        this.nalos = nalos;
//    }
//
//    /**
//     * @param dischargeReason the dischargeReason to set
//     */
//    public void setDischargeReason(int dischargeReason) {
//        this.dischargeReason = dischargeReason;
//    }
//
//    /**
//     * @param sendBuffer1 the sendBuffer1 to set
//     */
//    public void setSendBuffer1(String sendBuffer1) {
//        this.sendBuffer1 = sendBuffer1;
//    }
//
//    /**
//     * @param sendBuffer2 the sendBuffer2 to set
//     */
//    public void setSendBuffer2(String sendBuffer2) {
//        this.sendBuffer2 = sendBuffer2;
//    }
//
//    /**
//     * @param sendBuffer3 the sendBuffer3 to set
//     */
//    public void setSendBuffer3(String sendBuffer3) {
//        this.sendBuffer3 = sendBuffer3;
//    }
//
//    /**
//     * @param bewegungen the bewegungen to set
//     */
//    public void setBewegungen(List<KisBewegungContainer> bewegungen) {
//        this.bewegungen = bewegungen == null ? null : new ArrayList<>(bewegungen);
//    }
//
//    /**
//     * @param stationen the stationen to set
//     */
//    public void setStationen(List<KisHosWardContainer> stationen) {
//        this.stationen = stationen == null ? null : new ArrayList<>(stationen);
//    }
//
//    /**
//     * @param aufnahmeDatum the aufnahmeDatum to set
//     */
//    public void setAufnahmeDatum(Date aufnahmeDatum) {
//        this.aufnahmeDatum = aufnahmeDatum == null ? null : new Date(aufnahmeDatum.getTime());
//    }
//
//    /**
//     * @param entlassungsDatum the entlassungsDatum to set
//     */
//    public void setEntlassungsDatum(Date entlassungsDatum) {
//        this.entlassungsDatum = entlassungsDatum == null ? null : new Date(entlassungsDatum.getTime());
//    }
//
//    /**
//     * @param urlaubVon the urlaubVon to set
//     */
//    public void setUrlaubVon(int urlaubVon) {
//        this.urlaubVon = urlaubVon;
//    }
//
//    /**
//     * @param urlaubBis the urlaubBis to set
//     */
//    public void setUrlaubBis(int urlaubBis) {
//        this.urlaubBis = urlaubBis;
//    }
//
//    /**
//     * @param fallStatus the fallStatus to set
//     */
//    public void setFallStatus(int fallStatus) {
//        this.fallStatus = fallStatus;
//    }
//
//    /**
//     * @param picd the picd to set
//     */
//    public void setPicd(String picd) {
//        this.picd = picd;
//    }
//
//    /**
//     * @param icds the icds to set
//     */
//    public void setIcds(List<String> icds) {
//        this.icds = icds == null ? null : new ArrayList<>(icds);
//    }
//
//    /**
//     * @param ops the ops to set
//     */
//    public void setOps(List<String> ops) {
//        this.ops = ops == null ? null : new ArrayList<>(ops);
//    }
//
//    /**
//     * @param feld1 the feld1 to set
//     */
//    public void setFeld1(String feld1) {
//        this.feld1 = feld1;
//    }
//
//    /**
//     * @param feld2 the feld2 to set
//     */
//    public void setFeld2(String feld2) {
//        this.feld2 = feld2;
//    }
//
//    /**
//     * @param feld3 the feld3 to set
//     */
//    public void setFeld3(String feld3) {
//        this.feld3 = feld3;
//    }
//
//    /**
//     * @param feld4 the feld4 to set
//     */
//    public void setFeld4(String feld4) {
//        this.feld4 = feld4;
//    }
//
//    /**
//     * @param feld5 the feld5 to set
//     */
//    public void setFeld5(String feld5) {
//        this.feld5 = feld5;
//    }
//
//    /**
//     * @param numeric1 the numeric1 to set
//     */
//    public void setNumeric1(int numeric1) {
//        this.numeric1 = numeric1;
//    }
//
//    /**
//     * @param numeric2 the numeric2 to set
//     */
//    public void setNumeric2(int numeric2) {
//        this.numeric2 = numeric2;
//    }
//
//    /**
//     * @param numeric3 the numeric3 to set
//     */
//    public void setNumeric3(int numeric3) {
//        this.numeric3 = numeric3;
//    }
//
//    /**
//     * @param numeric4 the numeric4 to set
//     */
//    public void setNumeric4(int numeric4) {
//        this.numeric4 = numeric4;
//    }
//
//    /**
//     * @param numeric5 the numeric5 to set
//     */
//    public void setNumeric5(int numeric5) {
//        this.numeric5 = numeric5;
//    }
//
//    /**
//     * @param faktfrei the faktfrei to set
//     */
//    public void setFaktfrei(Date faktfrei) {
//        this.faktfrei = faktfrei == null ? null : new Date(faktfrei.getTime());
//    }
//
//    /**
//     * @param tob the tob to set
//     */
//    public void setTob(int tob) {
//        this.tob = tob;
//    }
//
//    /**
//     * @return the fallNr
//     */
//    public String getFallNr() {
//        return fallNr;
//    }
//
//    /**
//     * @return the ikz
//     */
//    public String getIkz() {
//        return ikz;
//    }
//
//    /**
//     * @param fallNr the fallNr to set
//     */
//    public void setFallNr(String fallNr) {
//        this.fallNr = fallNr;
//    }
//
//    /**
//     * @param ikz the ikz to set
//     */
//    public void setIkz(String ikz) {
//        this.ikz = ikz;
//    }
//}
