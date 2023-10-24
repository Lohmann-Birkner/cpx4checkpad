/* 
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.sap.results;

import com.sap.conn.jco.JCoStructure;

/**
 *
 * @author niemeier
 */
public class SapAdmissionSearchResult extends SapChargeSearchResult {

    private String movemntEnddate = "";
    private String movemntEndtime = "";
    private String treatmCategory = "";
    private String treatmCtgryStext = "";
    private String treatmCtgryText = "";
    private String clazz = "";
    private String classStext = "";
    private String classText = "";
    private String movemntSpec = "";
    private String movemntSpecStext = "";
    private String admittDept = "";
    private String admittDeptStext = "";
    private String department = "";
    private String departmentStext = "";
    private String nursTreatOu = "";
    private String nursTreatOuStext = "";
    private String room = "";
    private String roomIdentifier = "";
    private String bed = "";
    private String bedIdentifier = "";
    private String roomStatusInd = "";
    private String roomStatusIndText = "";
    private String phoneNo = "";
    private String tv = "";
    private String lengthOfStay = "";
    private String waitlistPrio = "";
    private String waitlistPrioText = "";
    private String latestAdm = "";
    private String waitlistType = "";
    private String waitlistTypeText = "";
    private String waitlistDelDat = "";
    private String waitlistDelReas = "";
    private String waitlistDelReasText = "";
    private String waitlistStatus = "";
    private String waitlistStatusText = "";
    private String waitlistHosp = "";
    private String waitlistInclon = "";
    private String treatCode = "";
    private String treatCodeText = "";
    private String emergCase = "";
    private String accident = "";
    private String accidentText = "";
    private String accidentDate = "";
    private String accidentTime = "";
    private String accidentNo = "";
    private String accidentLoc = "";
    private String accidentEmsvce = "";
    private String accident3Rdpty = "";
    private String accidentEmstyp = "";
    private String accidentEmstypText = "";
    private String arrivalMode = "";
    private String arrivalModeText = "";

    /**
     *
     */
    public SapAdmissionSearchResult() {
    }

    /**
     * @return the movemntEnddate
     */
    public String getMovemntEnddate() {
        return movemntEnddate;
    }

    /**
     * @param movemntEnddate the movemntEnddate to set
     */
    public void setMovemntEnddate(String movemntEnddate) {
        this.movemntEnddate = movemntEnddate;
    }

    /**
     * @return the movemntEndtime
     */
    public String getMovemntEndtime() {
        return movemntEndtime;
    }

    /**
     * @param movemntEndtime the movemntEndtime to set
     */
    public void setMovemntEndtime(String movemntEndtime) {
        this.movemntEndtime = movemntEndtime;
    }

    /**
     * @return the treatmCategory
     */
    public String getTreatmCategory() {
        return treatmCategory;
    }

    /**
     * @param treatmCategory the treatmCategory to set
     */
    public void setTreatmCategory(String treatmCategory) {
        this.treatmCategory = treatmCategory;
    }

    /**
     * @return the treatmCtgryStext
     */
    public String getTreatmCtgryStext() {
        return treatmCtgryStext;
    }

    /**
     * @param treatmCtgryStext the treatmCtgryStext to set
     */
    public void setTreatmCtgryStext(String treatmCtgryStext) {
        this.treatmCtgryStext = treatmCtgryStext;
    }

    /**
     * @return the treatmCtgryText
     */
    public String getTreatmCtgryText() {
        return treatmCtgryText;
    }

    /**
     * @param treatmCtgryText the treatmCtgryText to set
     */
    public void setTreatmCtgryText(String treatmCtgryText) {
        this.treatmCtgryText = treatmCtgryText;
    }

    /**
     * @return the clazz
     */
    public String getClazz() {
        return clazz;
    }

    /**
     * @param clazz the clazz to set
     */
    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    /**
     * @return the classStext
     */
    public String getClassStext() {
        return classStext;
    }

    /**
     * @param classStext the classStext to set
     */
    public void setClassStext(String classStext) {
        this.classStext = classStext;
    }

    /**
     * @return the classText
     */
    public String getClassText() {
        return classText;
    }

    /**
     * @param classText the classText to set
     */
    public void setClassText(String classText) {
        this.classText = classText;
    }

    /**
     * @return the movemntSpec
     */
    public String getMovemntSpec() {
        return movemntSpec;
    }

    /**
     * @param movemntSpec the movemntSpec to set
     */
    public void setMovemntSpec(String movemntSpec) {
        this.movemntSpec = movemntSpec;
    }

    /**
     * @return the movemntSpecStext
     */
    public String getMovemntSpecStext() {
        return movemntSpecStext;
    }

    /**
     * @param movemntSpecStext the movemntSpecStext to set
     */
    public void setMovemntSpecStext(String movemntSpecStext) {
        this.movemntSpecStext = movemntSpecStext;
    }

    /**
     * @return the admittDept
     */
    public String getAdmittDept() {
        return admittDept;
    }

    /**
     * @param admittDept the admittDept to set
     */
    public void setAdmittDept(String admittDept) {
        this.admittDept = admittDept;
    }

    /**
     * @return the admittDeptStext
     */
    public String getAdmittDeptStext() {
        return admittDeptStext;
    }

    /**
     * @param admittDeptStext the admittDeptStext to set
     */
    public void setAdmittDeptStext(String admittDeptStext) {
        this.admittDeptStext = admittDeptStext;
    }

    /**
     * @return the department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * @return the departmentStext
     */
    public String getDepartmentStext() {
        return departmentStext;
    }

    /**
     * @param departmentStext the departmentStext to set
     */
    public void setDepartmentStext(String departmentStext) {
        this.departmentStext = departmentStext;
    }

    /**
     * @return the nursTreatOu
     */
    public String getNursTreatOu() {
        return nursTreatOu;
    }

    /**
     * @param nursTreatOu the nursTreatOu to set
     */
    public void setNursTreatOu(String nursTreatOu) {
        this.nursTreatOu = nursTreatOu;
    }

    /**
     * @return the nursTreatOuStext
     */
    public String getNursTreatOuStext() {
        return nursTreatOuStext;
    }

    /**
     * @param nursTreatOuStext the nursTreatOuStext to set
     */
    public void setNursTreatOuStext(String nursTreatOuStext) {
        this.nursTreatOuStext = nursTreatOuStext;
    }

    /**
     * @return the room
     */
    public String getRoom() {
        return room;
    }

    /**
     * @param room the room to set
     */
    public void setRoom(String room) {
        this.room = room;
    }

    /**
     * @return the roomIdentifier
     */
    public String getRoomIdentifier() {
        return roomIdentifier;
    }

    /**
     * @param roomIdentifier the roomIdentifier to set
     */
    public void setRoomIdentifier(String roomIdentifier) {
        this.roomIdentifier = roomIdentifier;
    }

    /**
     * @return the bed
     */
    public String getBed() {
        return bed;
    }

    /**
     * @param bed the bed to set
     */
    public void setBed(String bed) {
        this.bed = bed;
    }

    /**
     * @return the bedIdentifier
     */
    public String getBedIdentifier() {
        return bedIdentifier;
    }

    /**
     * @param bedIdentifier the bedIdentifier to set
     */
    public void setBedIdentifier(String bedIdentifier) {
        this.bedIdentifier = bedIdentifier;
    }

    /**
     * @return the roomStatusInd
     */
    public String getRoomStatusInd() {
        return roomStatusInd;
    }

    /**
     * @param roomStatusInd the roomStatusInd to set
     */
    public void setRoomStatusInd(String roomStatusInd) {
        this.roomStatusInd = roomStatusInd;
    }

    /**
     * @return the roomStatusIndText
     */
    public String getRoomStatusIndText() {
        return roomStatusIndText;
    }

    /**
     * @param roomStatusIndText the roomStatusIndText to set
     */
    public void setRoomStatusIndText(String roomStatusIndText) {
        this.roomStatusIndText = roomStatusIndText;
    }

    /**
     * @return the phoneNo
     */
    public String getPhoneNo() {
        return phoneNo;
    }

    /**
     * @param phoneNo the phoneNo to set
     */
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    /**
     * @return the tv
     */
    public String getTv() {
        return tv;
    }

    /**
     * @param tv the tv to set
     */
    public void setTv(String tv) {
        this.tv = tv;
    }

    /**
     * @return the lengthOfStay
     */
    public String getLengthOfStay() {
        return lengthOfStay;
    }

    /**
     * @param lengthOfStay the lengthOfStay to set
     */
    public void setLengthOfStay(String lengthOfStay) {
        this.lengthOfStay = lengthOfStay;
    }

    /**
     * @return the waitlistPrio
     */
    public String getWaitlistPrio() {
        return waitlistPrio;
    }

    /**
     * @param waitlistPrio the waitlistPrio to set
     */
    public void setWaitlistPrio(String waitlistPrio) {
        this.waitlistPrio = waitlistPrio;
    }

    /**
     * @return the waitlistPrioText
     */
    public String getWaitlistPrioText() {
        return waitlistPrioText;
    }

    /**
     * @param waitlistPrioText the waitlistPrioText to set
     */
    public void setWaitlistPrioText(String waitlistPrioText) {
        this.waitlistPrioText = waitlistPrioText;
    }

    /**
     * @return the latestAdm
     */
    public String getLatestAdm() {
        return latestAdm;
    }

    /**
     * @param latestAdm the latestAdm to set
     */
    public void setLatestAdm(String latestAdm) {
        this.latestAdm = latestAdm;
    }

    /**
     * @return the waitlistType
     */
    public String getWaitlistType() {
        return waitlistType;
    }

    /**
     * @param waitlistType the waitlistType to set
     */
    public void setWaitlistType(String waitlistType) {
        this.waitlistType = waitlistType;
    }

    /**
     * @return the waitlistTypeText
     */
    public String getWaitlistTypeText() {
        return waitlistTypeText;
    }

    /**
     * @param waitlistTypeText the waitlistTypeText to set
     */
    public void setWaitlistTypeText(String waitlistTypeText) {
        this.waitlistTypeText = waitlistTypeText;
    }

    /**
     * @return the waitlistDelDat
     */
    public String getWaitlistDelDat() {
        return waitlistDelDat;
    }

    /**
     * @param waitlistDelDat the waitlistDelDat to set
     */
    public void setWaitlistDelDat(String waitlistDelDat) {
        this.waitlistDelDat = waitlistDelDat;
    }

    /**
     * @return the waitlistDelReas
     */
    public String getWaitlistDelReas() {
        return waitlistDelReas;
    }

    /**
     * @param waitlistDelReas the waitlistDelReas to set
     */
    public void setWaitlistDelReas(String waitlistDelReas) {
        this.waitlistDelReas = waitlistDelReas;
    }

    /**
     * @return the waitlistDelReasText
     */
    public String getWaitlistDelReasText() {
        return waitlistDelReasText;
    }

    /**
     * @param waitlistDelReasText the waitlistDelReasText to set
     */
    public void setWaitlistDelReasText(String waitlistDelReasText) {
        this.waitlistDelReasText = waitlistDelReasText;
    }

    /**
     * @return the waitlistStatus
     */
    public String getWaitlistStatus() {
        return waitlistStatus;
    }

    /**
     * @param waitlistStatus the waitlistStatus to set
     */
    public void setWaitlistStatus(String waitlistStatus) {
        this.waitlistStatus = waitlistStatus;
    }

    /**
     * @return the waitlistStatusText
     */
    public String getWaitlistStatusText() {
        return waitlistStatusText;
    }

    /**
     * @param waitlistStatusText the waitlistStatusText to set
     */
    public void setWaitlistStatusText(String waitlistStatusText) {
        this.waitlistStatusText = waitlistStatusText;
    }

    /**
     * @return the waitlistHosp
     */
    public String getWaitlistHosp() {
        return waitlistHosp;
    }

    /**
     * @param waitlistHosp the waitlistHosp to set
     */
    public void setWaitlistHosp(String waitlistHosp) {
        this.waitlistHosp = waitlistHosp;
    }

    /**
     * @return the waitlistInclon
     */
    public String getWaitlistInclon() {
        return waitlistInclon;
    }

    /**
     * @param waitlistInclon the waitlistInclon to set
     */
    public void setWaitlistInclon(String waitlistInclon) {
        this.waitlistInclon = waitlistInclon;
    }

    /**
     * @return the treatCode
     */
    public String getTreatCode() {
        return treatCode;
    }

    /**
     * @param treatCode the treatCode to set
     */
    public void setTreatCode(String treatCode) {
        this.treatCode = treatCode;
    }

    /**
     * @return the treatCodeText
     */
    public String getTreatCodeText() {
        return treatCodeText;
    }

    /**
     * @param treatCodeText the treatCodeText to set
     */
    public void setTreatCodeText(String treatCodeText) {
        this.treatCodeText = treatCodeText;
    }

    /**
     * @return the emergCase
     */
    public String getEmergCase() {
        return emergCase;
    }

    /**
     * @param emergCase the emergCase to set
     */
    public void setEmergCase(String emergCase) {
        this.emergCase = emergCase;
    }

    /**
     * @return the accident
     */
    public String getAccident() {
        return accident;
    }

    /**
     * @param accident the accident to set
     */
    public void setAccident(String accident) {
        this.accident = accident;
    }

    /**
     * @return the accidentText
     */
    public String getAccidentText() {
        return accidentText;
    }

    /**
     * @param accidentText the accidentText to set
     */
    public void setAccidentText(String accidentText) {
        this.accidentText = accidentText;
    }

    /**
     * @return the accidentDate
     */
    public String getAccidentDate() {
        return accidentDate;
    }

    /**
     * @param accidentDate the accidentDate to set
     */
    public void setAccidentDate(String accidentDate) {
        this.accidentDate = accidentDate;
    }

    /**
     * @return the accidentTime
     */
    public String getAccidentTime() {
        return accidentTime;
    }

    /**
     * @param accidentTime the accidentTime to set
     */
    public void setAccidentTime(String accidentTime) {
        this.accidentTime = accidentTime;
    }

    /**
     * @return the accidentNo
     */
    public String getAccidentNo() {
        return accidentNo;
    }

    /**
     * @param accidentNo the accidentNo to set
     */
    public void setAccidentNo(String accidentNo) {
        this.accidentNo = accidentNo;
    }

    /**
     * @return the accidentLoc
     */
    public String getAccidentLoc() {
        return accidentLoc;
    }

    /**
     * @param accidentLoc the accidentLoc to set
     */
    public void setAccidentLoc(String accidentLoc) {
        this.accidentLoc = accidentLoc;
    }

    /**
     * @return the accidentEmsvce
     */
    public String getAccidentEmsvce() {
        return accidentEmsvce;
    }

    /**
     * @param accidentEmsvce the accidentEmsvce to set
     */
    public void setAccidentEmsvce(String accidentEmsvce) {
        this.accidentEmsvce = accidentEmsvce;
    }

    /**
     * @return the accident3Rdpty
     */
    public String getAccident3Rdpty() {
        return accident3Rdpty;
    }

    /**
     * @param accident3Rdpty the accident3Rdpty to set
     */
    public void setAccident3Rdpty(String accident3Rdpty) {
        this.accident3Rdpty = accident3Rdpty;
    }

    /**
     * @return the accidentEmstyp
     */
    public String getAccidentEmstyp() {
        return accidentEmstyp;
    }

    /**
     * @param accidentEmstyp the accidentEmstyp to set
     */
    public void setAccidentEmstyp(String accidentEmstyp) {
        this.accidentEmstyp = accidentEmstyp;
    }

    /**
     * @return the accidentEmstypText
     */
    public String getAccidentEmstypText() {
        return accidentEmstypText;
    }

    /**
     * @param accidentEmstypText the accidentEmstypText to set
     */
    public void setAccidentEmstypText(String accidentEmstypText) {
        this.accidentEmstypText = accidentEmstypText;
    }

    /**
     * @return the arrivalMode
     */
    public String getArrivalMode() {
        return arrivalMode;
    }

    /**
     * @param arrivalMode the arrivalMode to set
     */
    public void setArrivalMode(String arrivalMode) {
        this.arrivalMode = arrivalMode;
    }

    /**
     * @return the arrivalModeText
     */
    public String getArrivalModeText() {
        return arrivalModeText;
    }

    /**
     * @param arrivalModeText the arrivalModeText to set
     */
    public void setArrivalModeText(String arrivalModeText) {
        this.arrivalModeText = arrivalModeText;
    }

    /**
     *
     * @param aTable JCO structure table
     */
    @Override
    public void readFromStructure(final JCoStructure aTable) {
        setClient(aTable.getString(0));
        setInstitution(aTable.getString(1));
        setInstStext(aTable.getString(2));
        setPatCaseId(aTable.getString(3));
        setChkDigitCase(aTable.getString(4));
        setCaseType(aTable.getString(5));
        setCaseTypeExt(aTable.getString(6));
        setCaseTypeStext(aTable.getString(7));
        setPatientId(aTable.getString(8));
        setBillingStatus(aTable.getString(9));
        setBillStext(aTable.getString(10));
        setRestricted(aTable.getString(11));
        setGeogrArea(aTable.getString(12));
        setGeogrAreaText(aTable.getString(13));
        setCaseComment(aTable.getString(14));
        setCaseStatus(aTable.getString(15));
        setCaseStatStext(aTable.getString(16));
        setEmergAdm(aTable.getString(17));
        setQuickAdm(aTable.getString(18));
        setStartDate(aTable.getString(19));
        setEndDate(aTable.getString(20));
        setBillBlock(aTable.getString(21));
        setBillBlockStext(aTable.getString(22));
        setPrevTreatDays(aTable.getString(23));
        setStatStcBlock(aTable.getString(24));
        setPpaRelev(aTable.getString(25));
        setRecOrder(aTable.getString(26));
        setPreviousDays(aTable.getString(27));
        setObjectNo(aTable.getString(28));
        setNonResident(aTable.getString(29));
        setChildren(aTable.getString(30));
        setForeignCase(aTable.getString(31));
        setCaseCategory(aTable.getString(32));
        setCaseCategoryStext(aTable.getString(33));
        setEmployeeType(aTable.getString(34));
        setEmployeeTypeStext(aTable.getString(35));
        setCantonTariff(aTable.getString(36));
        setCantonTariffStext(aTable.getString(37));
        setCantonConvtn(aTable.getString(38));
        setCantonConvtnStext(aTable.getString(39));
        setSrvGenTo(aTable.getString(40));
        setHealedDate(aTable.getString(41));
        setApplStatus(aTable.getString(42));
        setApplStatusStext(aTable.getString(43));
        setSpecialty(aTable.getString(44));
        setSpecialtyStext(aTable.getString(45));
        setCaseEndType(aTable.getString(46));
        setCaseEndTypeStext(aTable.getString(47));
        setDocType(aTable.getString(48));
        setDocTypeText(aTable.getString(49));
        setDocNo(aTable.getString(50));
        setChoiceCl(aTable.getString(51));
        setChoiceClText(aTable.getString(52));
        setPatWeight(aTable.getString(53));
        setWeightUnit(aTable.getString(54));
        setWeightUnitIso(aTable.getString(55));
        setPatHeight(aTable.getString(56));
        setHeightUnit(aTable.getString(57));
        setHeightUnitIso(aTable.getString(58));
        setMovemntSeqno(aTable.getString(59));
        setMovemntCtgry(aTable.getString(60));
        setMovemntCtgryText(aTable.getString(61));
        setMovemntType(aTable.getString(62));
        setMovemntTypeText(aTable.getString(63));
        setMovemntReas1(aTable.getString(64));
        setMovemntReas1Text(aTable.getString(65));
        setMovemntReas2(aTable.getString(66));
        setMovemntReas2Text(aTable.getString(67));
        setMovemntDate(aTable.getString(68));
        setMovemntTime(aTable.getString(69));
        setStatusInd(aTable.getString(70));
        setStatusIndText(aTable.getString(71));
        setMovemntEnddate(aTable.getString(72));
        setMovemntEndtime(aTable.getString(73));
        setTreatmCategory(aTable.getString(74));
        setTreatmCtgryStext(aTable.getString(75));
        setTreatmCtgryText(aTable.getString(76));
        setClazz(aTable.getString(77));
        setClassStext(aTable.getString(78));
        setClassText(aTable.getString(79));
        setMovemntSpec(aTable.getString(80));
        setMovemntSpecStext(aTable.getString(81));
        setAdmittDept(aTable.getString(82));
        setAdmittDeptStext(aTable.getString(83));
        setDepartment(aTable.getString(84));
        setDepartmentStext(aTable.getString(85));
        setNursTreatOu(aTable.getString(86));
        setNursTreatOuStext(aTable.getString(87));
        setRoom(aTable.getString(88));
        setRoomIdentifier(aTable.getString(89));
        setBed(aTable.getString(90));
        setBedIdentifier(aTable.getString(91));
        setRoomStatusInd(aTable.getString(92));
        setRoomStatusIndText(aTable.getString(93));
        setPhoneNo(aTable.getString(94));
        setTv(aTable.getString(95));
        setLengthOfStay(aTable.getString(96));
        setRefPstTrtType(aTable.getString(97));
        setRefPstTrtTypeText(aTable.getString(98));
        setRefHospital(aTable.getString(99));
        setWaitlistPrio(aTable.getString(100));
        setWaitlistPrioText(aTable.getString(101));
        setLatestAdm(aTable.getString(102));
        setWaitlistType(aTable.getString(103));
        setWaitlistTypeText(aTable.getString(104));
        setWaitlistDelDat(aTable.getString(105));
        setWaitlistDelReas(aTable.getString(106));
        setWaitlistDelReasText(aTable.getString(107));
        setWaitlistStatus(aTable.getString(108));
        setWaitlistStatusText(aTable.getString(109));
        setWaitlistHosp(aTable.getString(110));
        setWaitlistInclon(aTable.getString(111));
        setTreatCode(aTable.getString(112));
        setTreatCodeText(aTable.getString(113));
        setEmergCase(aTable.getString(114));
        setAccident(aTable.getString(115));
        setAccidentText(aTable.getString(116));
        setAccidentDate(aTable.getString(117));
        setAccidentTime(aTable.getString(118));
        setAccidentNo(aTable.getString(119));
        setAccidentLoc(aTable.getString(120));
        setAccidentEmsvce(aTable.getString(121));
        setAccident3Rdpty(aTable.getString(122));
        setAccidentEmstyp(aTable.getString(123));
        setAccidentEmstypText(aTable.getString(124));
        setArrivalMode(aTable.getString(125));
        setArrivalModeText(aTable.getString(126));
        setCreationDate(aTable.getString(127));
        setCreationUser(aTable.getString(128));
        setUpdateDate(aTable.getString(129));
        setUpdateUser(aTable.getString(130));
        setCancelInd(aTable.getString(131));
        setCancelDate(aTable.getString(132));
        setCancelUser(aTable.getString(133));
        setCancelReason(aTable.getString(134));
        setCancelReasonText(aTable.getString(135));
        setCreationTime(aTable.getString(136));
        setRespiration(aTable.getString(137));
    }

    @Override
    public String toString() {
        return "Mandant >" + getClient() + "<\n"
                + "Einrichtung >" + getInstitution() + "<\n"
                + "Kurzbezeichnung einer Einrichtung >" + getInstStext() + "<\n"
                + "Fallnummer >" + getPatCaseId() + "<\n"
                + "Prüfziffer Fall >" + getChkDigitCase() + "<\n"
                + "Fallart >" + getCaseType() + "<\n"
                + "Fallart - anwenderspezifisch >" + getCaseTypeExt() + "<\n"
                + "Fallartentext >" + getCaseTypeStext() + "<\n"
                + "Patientennummer >" + getPatientId() + "<\n"
                + "Abrechnungskennzeichen eines Falles >" + getBillingStatus() + "<\n"
                + "Abrechnungskennzeichen eines Falles - Beschreibung >" + getBillStext() + "<\n"
                + "Kennzeichen für Sicherheitsverwahrung >" + getRestricted() + "<\n"
                + "Einzugsgebiet >" + getGeogrArea() + "<\n"
                + "Bezeichnung des Einzugsgebietes >" + getGeogrAreaText() + "<\n"
                + "Bemerkung zum Fall >" + getCaseComment() + "<\n"
                + "Fallstatus >" + getCaseStatus() + "<\n"
                + "Fallstatus - Beschreibung >" + getCaseStatStext() + "<\n"
                + "Notaufnahmekennzeichen >" + getEmergAdm() + "<\n"
                + "Kurzaufnahmekennzeichen >" + getQuickAdm() + "<\n"
                + "Beginndatum des Falles >" + getStartDate() + "<\n"
                + "Endedatum des Falles >" + getEndDate() + "<\n"
                + "Fakturasperre eines Falles >" + getBillBlock() + "<\n"
                + "Fakturasperre eines Falles - Beschreibung >" + getBillBlockStext() + "<\n"
                + "Anzahl Vorpflegetage des Patienten im Kalenderjahr >" + getPrevTreatDays() + "<\n"
                + "Kennzeichen für die Aufnahmeart Statistiksperre >" + getStatStcBlock() + "<\n"
                + "Kennzeichen, daß für die KV-Abrechnung relevant >" + getPpaRelev() + "<\n"
                + "CO Empfänger (Auftrag) >" + getRecOrder() + "<\n"
                + "anzurechnende Vortage des Falles >" + getPreviousDays() + "<\n"
                + "Objektnummer >" + getObjectNo() + "<\n"
                + "Kennzeichen Patient kein Staatsbürger >" + getNonResident() + "<\n"
                + "Anzahl Kinder >" + getChildren() + "<\n"
                + "Auslandsfall >" + getForeignCase() + "<\n"
                + "Falltyp >" + getCaseCategory() + "<\n"
                + "Falltyp - Text >" + getCaseCategoryStext() + "<\n"
                + "Arbeitnehmertyp >" + getEmployeeType() + "<\n"
                + "Text zum Arbeitnehmertyp >" + getEmployeeTypeStext() + "<\n"
                + "CH: Kantonstarif >" + getCantonTariff() + "<\n"
                + "CH: Bezeichnung des Kantonstarifs >" + getCantonTariffStext() + "<\n"
                + "CH: Abkommen (Spital <-> Kanton) >" + getCantonConvtn() + "<\n"
                + "CH: Bezeichnung des Abkommens >" + getCantonConvtnStext() + "<\n"
                + "CH: Leistungsgenerierungsdatum bis >" + getSrvGenTo() + "<\n"
                + "Datum der Wundheilung >" + getHealedDate() + "<\n"
                + "Applikationsstatus >" + getApplStatus() + "<\n"
                + "Text des Applikationsstatus >" + getApplStatusStext() + "<\n"
                + "Fachrichtung für Organisationseinheit >" + getSpecialty() + "<\n"
                + "Bezeichnung der Fachrichtung >" + getSpecialtyStext() + "<\n"
                + "Art des Fallendes >" + getCaseEndType() + "<\n"
                + "Text zur Fallendeart >" + getCaseEndTypeStext() + "<\n"
                + "Typ des Ausweisdokuments >" + getDocType() + "<\n"
                + "Text für Ausweisdokumenttyp >" + getDocTypeText() + "<\n"
                + "Ausweisdokumentnummer (lang) >" + getDocNo() + "<\n"
                + "Wahl-Unterbringungskategorie >" + getChoiceCl() + "<\n"
                + "Text zur Wahl-Unterbringungskategorie >" + getChoiceClText() + "<\n"
                + "Gewicht des Patienten bei Aufnahme >" + getPatWeight() + "<\n"
                + "Maßeinheit Gewicht Patient >" + getWeightUnit() + "<\n"
                + "ISO-Maßeinheit Gewicht Patient >" + getWeightUnitIso() + "<\n"
                + "Größe des Patienten bei Aufnahme >" + getPatHeight() + "<\n"
                + "Maßeinheit Größe Patient >" + getHeightUnit() + "<\n"
                + "ISO-Maßeinheit Größe Patient >" + getHeightUnitIso() + "<\n"
                + "Laufende Nummer einer Bewegung >" + getMovemntSeqno() + "<\n"
                + "Bewegungstyp >" + getMovemntCtgry() + "<\n"
                + "Bewegungstypentext >" + getMovemntCtgryText() + "<\n"
                + "Bewegungsart >" + getMovemntType() + "<\n"
                + "Bewegungsartentext >" + getMovemntTypeText() + "<\n"
                + "Bewegungsgrund - 1. und 2. Stelle >" + getMovemntReas1() + "<\n"
                + "Bewegungsgrund - Text >" + getMovemntReas1Text() + "<\n"
                + "Bewegungsgrund - 3. und 4. Stelle >" + getMovemntReas2() + "<\n"
                + "Bewegungsgrund - Text >" + getMovemntReas2Text() + "<\n"
                + "Datum der Bewegung >" + getMovemntDate() + "<\n"
                + "Uhrzeit der Bewegung >" + getMovemntTime() + "<\n"
                + "Plankennzeichen Datum der Bewegung >" + getStatusInd() + "<\n"
                + "Plankennzeichen Datum der Bewegung - Beschreibung >" + getStatusIndText() + "<\n"
                + "Bewegungsendedatum, Beginndatum der Folgebewegung >" + getMovemntEnddate() + "<\n"
                + "Bewegungsendezeit, Beginnzeit der Folgebewegung >" + getMovemntEndtime() + "<\n"
                + "Behandlungskategorie >" + getTreatmCategory() + "<\n"
                + "Kurzbezeichnung der Behandlungskategorie >" + getTreatmCtgryStext() + "<\n"
                + "Bezeichnung der Behandlungskategorie >" + getTreatmCtgryText() + "<\n"
                + "Klasse des Patienten >" + getClazz() + "<\n"
                + "Kurzbezeichnung der Patientenklasse >" + getClassStext() + "<\n"
                + "Bezeichnung der Patientenklasse >" + getClassText() + "<\n"
                + "Fachrichtung der Ärzte >" + getMovemntSpec() + "<\n"
                + "Bezeichnung der Fachrichtung >" + getMovemntSpecStext() + "<\n"
                + "OrgId der Aufnahmestelle >" + getAdmittDept() + "<\n"
                + "Kurzbezeichnung einer Organisationseinheit >" + getAdmittDeptStext() + "<\n"
                + "OrgEinheit, die einem Fall fachl. zugewiesen wird >" + getDepartment() + "<\n"
                + "Kurzbezeichnung einer Organisationseinheit >" + getDepartmentStext() + "<\n"
                + "OrgEinheit, die einem Fall zugewiesen wird >" + getNursTreatOu() + "<\n"
                + "Kurzbezeichnung einer Organisationseinheit >" + getNursTreatOuStext() + "<\n"
                + "BauId eines Zimmers >" + getRoom() + "<\n"
                + "Kürzel einer baulichen Einheit >" + getRoomIdentifier() + "<\n"
                + "Bauid eines Bettenstellplatzes >" + getBed() + "<\n"
                + "Kürzel einer baulichen Einheit >" + getBedIdentifier() + "<\n"
                + "Plankennzeichen für räumliche Zuweisung >" + getRoomStatusInd() + "<\n"
                + "Plankennzeichen für räumliche Zuweisung - Text >" + getRoomStatusIndText() + "<\n"
                + "Telefonnummer d. Patienten in der Einrichtung >" + getPhoneNo() + "<\n"
                + "Kennzeichen für TV >" + getTv() + "<\n"
                + "Vorraussichtl. Aufenthalts-bzw. Behandlungsdauer >" + getLengthOfStay() + "<\n"
                + "Einweisungs-, Überweisungs-, Nachbehandlungsart >" + getRefPstTrtType() + "<\n"
                + "Text Einweisungs-, Überweisungs-, Nachbehandlungsart >" + getRefPstTrtTypeText() + "<\n"
                + "Identifikation eines externen Krankenhauses >" + getRefHospital() + "<\n"
                + "Priorität für die Vormerkungs-/Wartelistenverwaltung >" + getWaitlistPrio() + "<\n"
                + "Text zur Priorität >" + getWaitlistPrioText() + "<\n"
                + "spätestes Aufnahmedatum >" + getLatestAdm() + "<\n"
                + "Wartelisten >" + getWaitlistType() + "<\n"
                + "Text Warteliste >" + getWaitlistTypeText() + "<\n"
                + "Datum der Entfernungs von der Warteliste >" + getWaitlistDelDat() + "<\n"
                + "Grund der Entfernungs von der Warteliste >" + getWaitlistDelReas() + "<\n"
                + "Warteliste Entfernungsgrund Text >" + getWaitlistDelReasText() + "<\n"
                + "Vormerkungs-/Wartelistenstatus >" + getWaitlistStatus() + "<\n"
                + "Text Vormerkungstatus >" + getWaitlistStatusText() + "<\n"
                + "Warteliste weiterbehandelndes Krankenhaus >" + getWaitlistHosp() + "<\n"
                + "Aufnahmedatum auf Warteliste >" + getWaitlistInclon() + "<\n"
                + "Behandlungscode (medizinisches Programm) >" + getTreatCode() + "<\n"
                + "Text zum Behandlungscode >" + getTreatCodeText() + "<\n"
                + "Notfallkennzeichen >" + getEmergCase() + "<\n"
                + "Unfallart >" + getAccident() + "<\n"
                + "Unfallartentext >" + getAccidentText() + "<\n"
                + "Unfalldatum >" + getAccidentDate() + "<\n"
                + "Uhrzeit des Unfalls >" + getAccidentTime() + "<\n"
                + "Aktenzeichen / Bearbeitungsnummer für Unfall >" + getAccidentNo() + "<\n"
                + "Ort des Unfalls >" + getAccidentLoc() + "<\n"
                + "Nummer des Rettungsdienstes bei Unfall >" + getAccidentEmsvce() + "<\n"
                + "Fremdverschulden >" + getAccident3Rdpty() + "<\n"
                + "Rettungsdienst >" + getAccidentEmstyp() + "<\n"
                + "Text zu Rettungsdienst >" + getAccidentEmstypText() + "<\n"
                + "Einlieferungsart >" + getArrivalMode() + "<\n"
                + "Text zur Einlieferungsart >" + getArrivalModeText() + "<\n"
                + "Datum, an dem der Satz hinzugefügt wurde >" + getCreationDate() + "<\n"
                + "Name des Sachbearbeiters, der den Satz hinzugefügt hat >" + getCreationUser() + "<\n"
                + "Änderungsdatum >" + getUpdateDate() + "<\n"
                + "Name des ändernden Sachbearbeiters >" + getUpdateUser() + "<\n"
                + "Stornokennzeichen >" + getCancelInd() + "<\n"
                + "Stornierungsdatum >" + getCancelDate() + "<\n"
                + "Name des stornierenden Sachbearbeiters >" + getCancelUser() + "<\n"
                + "Stornogrund der Bewegung >" + getCancelReason() + "<\n"
                + "Bezeichnung des Stornogrundes >" + getCancelReasonText() + "<\n"
                + "Uhrzeit, zu der der Satz hinzugefügt wurde >" + getCreationTime() + "<\n"
                + "Beatmungsdauer in Stunden >" + getRespiration() + "<";
    }
}
