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
public abstract class SapChargeSearchResult {

    protected String client = "";
    protected String institution = "";
    protected String instStext = "";
    protected String patCaseId = "";
    protected String chkDigitCase = "";
    protected String caseType = "";
    protected String caseTypeExt = "";
    protected String caseTypeStext = "";
    protected String patientId = "";
    protected String billingStatus = "";
    protected String billStext = "";
    protected String restricted = "";
    protected String geogrArea = "";
    protected String geogrAreaText = "";
    protected String caseComment = "";
    protected String caseStatus = "";
    protected String caseStatStext = "";
    protected String emergAdm = "";
    protected String quickAdm = "";
    protected String startDate = "";
    protected String endDate = "";
    protected String billBlock = "";
    protected String billBlockStext = "";
    protected String prevTreatDays = "";
    protected String statStcBlock = "";
    protected String ppaRelev = "";
    protected String recOrder = "";
    protected String previousDays = "";
    protected String objectNo = "";
    protected String nonResident = "";
    protected String children = "";
    protected String foreignCase = "";
    protected String caseCategory = "";
    protected String caseCategoryStext = "";
    protected String employeeType = "";
    protected String employeeTypeStext = "";
    protected String cantonTariff = "";
    protected String cantonTariffStext = "";
    protected String cantonConvtn = "";
    protected String cantonConvtnStext = "";
    protected String srvGenTo = "";
    protected String healedDate = "";
    protected String applStatus = "";
    protected String applStatusStext = "";
    protected String specialty = "";
    protected String specialtyStext = "";
    protected String caseEndType = "";
    protected String caseEndTypeStext = "";
    protected String docType = "";
    protected String docTypeText = "";
    protected String docNo = "";
    protected String choiceCl = "";
    protected String choiceClText = "";
    protected String patWeight = "";
    protected String weightUnit = "";
    protected String weightUnitIso = "";
    protected String patHeight = "";
    protected String heightUnit = "";
    protected String heightUnitIso = "";
    protected String movemntSeqno = "";
    protected String movemntCtgry = "";
    protected String movemntCtgryText = "";
    protected String movemntType = "";
    protected String movemntTypeText = "";
    protected String movemntDate = "";
    protected String movemntTime = "";
    protected String movemntReas1 = "";
    protected String movemntReas1Text = "";
    protected String movemntReas2 = "";
    protected String movemntReas2Text = "";
    protected String statusInd = "";
    protected String statusIndText = "";
    protected String refPstTrtType = "";
    protected String refPstTrtTypeText = "";
    protected String refHospital = "";
    protected String dischrgDisp = "";
    protected String dischrgDispText = "";
    protected String workIncapacity = "";
    protected String creationDate = "";
    protected String creationUser = "";
    protected String updateDate = "";
    protected String updateUser = "";
    protected String cancelInd = "";
    protected String cancelDate = "";
    protected String cancelUser = "";
    protected String cancelReason = "";
    protected String cancelReasonText = "";
    protected String creationTime = "";
    protected String respiration = "";

    public SapChargeSearchResult() {
        //
    }

    /**
     * @return the client
     */
    public String getClient() {
        return client;
    }

    /**
     * @param client the client to set
     */
    public void setClient(String client) {
        this.client = client;
    }

    /**
     * @return the institution
     */
    public String getInstitution() {
        return institution;
    }

    /**
     * @param institution the institution to set
     */
    public void setInstitution(String institution) {
        this.institution = institution;
    }

    /**
     * @return the instStext
     */
    public String getInstStext() {
        return instStext;
    }

    /**
     * @param instStext the instStext to set
     */
    public void setInstStext(String instStext) {
        this.instStext = instStext;
    }

    /**
     * @return the patCaseId
     */
    public String getPatCaseId() {
        return patCaseId;
    }

    /**
     * @param patCaseId the patCaseId to set
     */
    public void setPatCaseId(String patCaseId) {
        this.patCaseId = patCaseId;
    }

    /**
     * @return the chkDigitCase
     */
    public String getChkDigitCase() {
        return chkDigitCase;
    }

    /**
     * @param chkDigitCase the chkDigitCase to set
     */
    public void setChkDigitCase(String chkDigitCase) {
        this.chkDigitCase = chkDigitCase;
    }

    /**
     * @return the caseType
     */
    public String getCaseType() {
        return caseType;
    }

    /**
     * @param caseType the caseType to set
     */
    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    /**
     * @return the caseTypeExt
     */
    public String getCaseTypeExt() {
        return caseTypeExt;
    }

    /**
     * @param caseTypeExt the caseTypeExt to set
     */
    public void setCaseTypeExt(String caseTypeExt) {
        this.caseTypeExt = caseTypeExt;
    }

    /**
     * @return the caseTypeStext
     */
    public String getCaseTypeStext() {
        return caseTypeStext;
    }

    /**
     * @param caseTypeStext the caseTypeStext to set
     */
    public void setCaseTypeStext(String caseTypeStext) {
        this.caseTypeStext = caseTypeStext;
    }

    /**
     * @return the patientId
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * @param patientId the patientId to set
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * @return the billingStatus
     */
    public String getBillingStatus() {
        return billingStatus;
    }

    /**
     * @param billingStatus the billingStatus to set
     */
    public void setBillingStatus(String billingStatus) {
        this.billingStatus = billingStatus;
    }

    /**
     * @return the billStext
     */
    public String getBillStext() {
        return billStext;
    }

    /**
     * @param billStext the billStext to set
     */
    public void setBillStext(String billStext) {
        this.billStext = billStext;
    }

    /**
     * @return the restricted
     */
    public String getRestricted() {
        return restricted;
    }

    /**
     * @param restricted the restricted to set
     */
    public void setRestricted(String restricted) {
        this.restricted = restricted;
    }

    /**
     * @return the geogrArea
     */
    public String getGeogrArea() {
        return geogrArea;
    }

    /**
     * @param geogrArea the geogrArea to set
     */
    public void setGeogrArea(String geogrArea) {
        this.geogrArea = geogrArea;
    }

    /**
     * @return the geogrAreaText
     */
    public String getGeogrAreaText() {
        return geogrAreaText;
    }

    /**
     * @param geogrAreaText the geogrAreaText to set
     */
    public void setGeogrAreaText(String geogrAreaText) {
        this.geogrAreaText = geogrAreaText;
    }

    /**
     * @return the caseComment
     */
    public String getCaseComment() {
        return caseComment;
    }

    /**
     * @param caseComment the caseComment to set
     */
    public void setCaseComment(String caseComment) {
        this.caseComment = caseComment;
    }

    /**
     * @return the caseStatus
     */
    public String getCaseStatus() {
        return caseStatus;
    }

    /**
     * @param caseStatus the caseStatus to set
     */
    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
    }

    /**
     * @return the caseStatStext
     */
    public String getCaseStatStext() {
        return caseStatStext;
    }

    /**
     * @param caseStatStext the caseStatStext to set
     */
    public void setCaseStatStext(String caseStatStext) {
        this.caseStatStext = caseStatStext;
    }

    /**
     * @return the emergAdm
     */
    public String getEmergAdm() {
        return emergAdm;
    }

    /**
     * @param emergAdm the emergAdm to set
     */
    public void setEmergAdm(String emergAdm) {
        this.emergAdm = emergAdm;
    }

    /**
     * @return the quickAdm
     */
    public String getQuickAdm() {
        return quickAdm;
    }

    /**
     * @param quickAdm the quickAdm to set
     */
    public void setQuickAdm(String quickAdm) {
        this.quickAdm = quickAdm;
    }

    /**
     * @return the startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the billBlock
     */
    public String getBillBlock() {
        return billBlock;
    }

    /**
     * @param billBlock the billBlock to set
     */
    public void setBillBlock(String billBlock) {
        this.billBlock = billBlock;
    }

    /**
     * @return the billBlockStext
     */
    public String getBillBlockStext() {
        return billBlockStext;
    }

    /**
     * @param billBlockStext the billBlockStext to set
     */
    public void setBillBlockStext(String billBlockStext) {
        this.billBlockStext = billBlockStext;
    }

    /**
     * @return the prevTreatDays
     */
    public String getPrevTreatDays() {
        return prevTreatDays;
    }

    /**
     * @param prevTreatDays the prevTreatDays to set
     */
    public void setPrevTreatDays(String prevTreatDays) {
        this.prevTreatDays = prevTreatDays;
    }

    /**
     * @return the statStcBlock
     */
    public String getStatStcBlock() {
        return statStcBlock;
    }

    /**
     * @param statStcBlock the statStcBlock to set
     */
    public void setStatStcBlock(String statStcBlock) {
        this.statStcBlock = statStcBlock;
    }

    /**
     * @return the ppaRelev
     */
    public String getPpaRelev() {
        return ppaRelev;
    }

    /**
     * @param ppaRelev the ppaRelev to set
     */
    public void setPpaRelev(String ppaRelev) {
        this.ppaRelev = ppaRelev;
    }

    /**
     * @return the recOrder
     */
    public String getRecOrder() {
        return recOrder;
    }

    /**
     * @param recOrder the recOrder to set
     */
    public void setRecOrder(String recOrder) {
        this.recOrder = recOrder;
    }

    /**
     * @return the previousDays
     */
    public String getPreviousDays() {
        return previousDays;
    }

    /**
     * @param previousDays the previousDays to set
     */
    public void setPreviousDays(String previousDays) {
        this.previousDays = previousDays;
    }

    /**
     * @return the objectNo
     */
    public String getObjectNo() {
        return objectNo;
    }

    /**
     * @param objectNo the objectNo to set
     */
    public void setObjectNo(String objectNo) {
        this.objectNo = objectNo;
    }

    /**
     * @return the nonResident
     */
    public String getNonResident() {
        return nonResident;
    }

    /**
     * @param nonResident the nonResident to set
     */
    public void setNonResident(String nonResident) {
        this.nonResident = nonResident;
    }

    /**
     * @return the children
     */
    public String getChildren() {
        return children;
    }

    /**
     * @param children the children to set
     */
    public void setChildren(String children) {
        this.children = children;
    }

    /**
     * @return the foreignCase
     */
    public String getForeignCase() {
        return foreignCase;
    }

    /**
     * @param foreignCase the foreignCase to set
     */
    public void setForeignCase(String foreignCase) {
        this.foreignCase = foreignCase;
    }

    /**
     * @return the caseCategory
     */
    public String getCaseCategory() {
        return caseCategory;
    }

    /**
     * @param caseCategory the caseCategory to set
     */
    public void setCaseCategory(String caseCategory) {
        this.caseCategory = caseCategory;
    }

    /**
     * @return the caseCategoryStext
     */
    public String getCaseCategoryStext() {
        return caseCategoryStext;
    }

    /**
     * @param caseCategoryStext the caseCategoryStext to set
     */
    public void setCaseCategoryStext(String caseCategoryStext) {
        this.caseCategoryStext = caseCategoryStext;
    }

    /**
     * @return the employeeType
     */
    public String getEmployeeType() {
        return employeeType;
    }

    /**
     * @param employeeType the employeeType to set
     */
    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    /**
     * @return the employeeTypeStext
     */
    public String getEmployeeTypeStext() {
        return employeeTypeStext;
    }

    /**
     * @param employeeTypeStext the employeeTypeStext to set
     */
    public void setEmployeeTypeStext(String employeeTypeStext) {
        this.employeeTypeStext = employeeTypeStext;
    }

    /**
     * @return the cantonTariff
     */
    public String getCantonTariff() {
        return cantonTariff;
    }

    /**
     * @param cantonTariff the cantonTariff to set
     */
    public void setCantonTariff(String cantonTariff) {
        this.cantonTariff = cantonTariff;
    }

    /**
     * @return the cantonTariffStext
     */
    public String getCantonTariffStext() {
        return cantonTariffStext;
    }

    /**
     * @param cantonTariffStext the cantonTariffStext to set
     */
    public void setCantonTariffStext(String cantonTariffStext) {
        this.cantonTariffStext = cantonTariffStext;
    }

    /**
     * @return the cantonConvtn
     */
    public String getCantonConvtn() {
        return cantonConvtn;
    }

    /**
     * @param cantonConvtn the cantonConvtn to set
     */
    public void setCantonConvtn(String cantonConvtn) {
        this.cantonConvtn = cantonConvtn;
    }

    /**
     * @return the cantonConvtnStext
     */
    public String getCantonConvtnStext() {
        return cantonConvtnStext;
    }

    /**
     * @param cantonConvtnStext the cantonConvtnStext to set
     */
    public void setCantonConvtnStext(String cantonConvtnStext) {
        this.cantonConvtnStext = cantonConvtnStext;
    }

    /**
     * @return the srvGenTo
     */
    public String getSrvGenTo() {
        return srvGenTo;
    }

    /**
     * @param srvGenTo the srvGenTo to set
     */
    public void setSrvGenTo(String srvGenTo) {
        this.srvGenTo = srvGenTo;
    }

    /**
     * @return the healedDate
     */
    public String getHealedDate() {
        return healedDate;
    }

    /**
     * @param healedDate the healedDate to set
     */
    public void setHealedDate(String healedDate) {
        this.healedDate = healedDate;
    }

    /**
     * @return the applStatus
     */
    public String getApplStatus() {
        return applStatus;
    }

    /**
     * @param applStatus the applStatus to set
     */
    public void setApplStatus(String applStatus) {
        this.applStatus = applStatus;
    }

    /**
     * @return the applStatusStext
     */
    public String getApplStatusStext() {
        return applStatusStext;
    }

    /**
     * @param applStatusStext the applStatusStext to set
     */
    public void setApplStatusStext(String applStatusStext) {
        this.applStatusStext = applStatusStext;
    }

    /**
     * @return the specialty
     */
    public String getSpecialty() {
        return specialty;
    }

    /**
     * @param specialty the specialty to set
     */
    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    /**
     * @return the specialtyStext
     */
    public String getSpecialtyStext() {
        return specialtyStext;
    }

    /**
     * @param specialtyStext the specialtyStext to set
     */
    public void setSpecialtyStext(String specialtyStext) {
        this.specialtyStext = specialtyStext;
    }

    /**
     * @return the caseEndType
     */
    public String getCaseEndType() {
        return caseEndType;
    }

    /**
     * @param caseEndType the caseEndType to set
     */
    public void setCaseEndType(String caseEndType) {
        this.caseEndType = caseEndType;
    }

    /**
     * @return the caseEndTypeStext
     */
    public String getCaseEndTypeStext() {
        return caseEndTypeStext;
    }

    /**
     * @param caseEndTypeStext the caseEndTypeStext to set
     */
    public void setCaseEndTypeStext(String caseEndTypeStext) {
        this.caseEndTypeStext = caseEndTypeStext;
    }

    /**
     * @return the docType
     */
    public String getDocType() {
        return docType;
    }

    /**
     * @param docType the docType to set
     */
    public void setDocType(String docType) {
        this.docType = docType;
    }

    /**
     * @return the docTypeText
     */
    public String getDocTypeText() {
        return docTypeText;
    }

    /**
     * @param docTypeText the docTypeText to set
     */
    public void setDocTypeText(String docTypeText) {
        this.docTypeText = docTypeText;
    }

    /**
     * @return the docNo
     */
    public String getDocNo() {
        return docNo;
    }

    /**
     * @param docNo the docNo to set
     */
    public void setDocNo(String docNo) {
        this.docNo = docNo;
    }

    /**
     * @return the choiceCl
     */
    public String getChoiceCl() {
        return choiceCl;
    }

    /**
     * @param choiceCl the choiceCl to set
     */
    public void setChoiceCl(String choiceCl) {
        this.choiceCl = choiceCl;
    }

    /**
     * @return the choiceClText
     */
    public String getChoiceClText() {
        return choiceClText;
    }

    /**
     * @param choiceClText the choiceClText to set
     */
    public void setChoiceClText(String choiceClText) {
        this.choiceClText = choiceClText;
    }

    /**
     * @return the patWeight
     */
    public String getPatWeight() {
        return patWeight;
    }

    /**
     * @param patWeight the patWeight to set
     */
    public void setPatWeight(String patWeight) {
        this.patWeight = patWeight;
    }

    /**
     * @return the weightUnit
     */
    public String getWeightUnit() {
        return weightUnit;
    }

    /**
     * @param weightUnit the weightUnit to set
     */
    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }

    /**
     * @return the weightUnitIso
     */
    public String getWeightUnitIso() {
        return weightUnitIso;
    }

    /**
     * @param weightUnitIso the weightUnitIso to set
     */
    public void setWeightUnitIso(String weightUnitIso) {
        this.weightUnitIso = weightUnitIso;
    }

    /**
     * @return the patHeight
     */
    public String getPatHeight() {
        return patHeight;
    }

    /**
     * @param patHeight the patHeight to set
     */
    public void setPatHeight(String patHeight) {
        this.patHeight = patHeight;
    }

    /**
     * @return the heightUnit
     */
    public String getHeightUnit() {
        return heightUnit;
    }

    /**
     * @param heightUnit the heightUnit to set
     */
    public void setHeightUnit(String heightUnit) {
        this.heightUnit = heightUnit;
    }

    /**
     * @return the heightUnitIso
     */
    public String getHeightUnitIso() {
        return heightUnitIso;
    }

    /**
     * @param heightUnitIso the heightUnitIso to set
     */
    public void setHeightUnitIso(String heightUnitIso) {
        this.heightUnitIso = heightUnitIso;
    }

    /**
     * @return the movemntSeqno
     */
    public String getMovemntSeqno() {
        return movemntSeqno;
    }

    /**
     * @param movemntSeqno the movemntSeqno to set
     */
    public void setMovemntSeqno(String movemntSeqno) {
        this.movemntSeqno = movemntSeqno;
    }

    /**
     * @return the movemntCtgry
     */
    public String getMovemntCtgry() {
        return movemntCtgry;
    }

    /**
     * @param movemntCtgry the movemntCtgry to set
     */
    public void setMovemntCtgry(String movemntCtgry) {
        this.movemntCtgry = movemntCtgry;
    }

    /**
     * @return the movemntCtgryText
     */
    public String getMovemntCtgryText() {
        return movemntCtgryText;
    }

    /**
     * @param movemntCtgryText the movemntCtgryText to set
     */
    public void setMovemntCtgryText(String movemntCtgryText) {
        this.movemntCtgryText = movemntCtgryText;
    }

    /**
     * @return the movemntType
     */
    public String getMovemntType() {
        return movemntType;
    }

    /**
     * @param movemntType the movemntType to set
     */
    public void setMovemntType(String movemntType) {
        this.movemntType = movemntType;
    }

    /**
     * @return the movemntTypeText
     */
    public String getMovemntTypeText() {
        return movemntTypeText;
    }

    /**
     * @param movemntTypeText the movemntTypeText to set
     */
    public void setMovemntTypeText(String movemntTypeText) {
        this.movemntTypeText = movemntTypeText;
    }

    /**
     * @return the movemntDate
     */
    public String getMovemntDate() {
        return movemntDate;
    }

    /**
     * @param movemntDate the movemntDate to set
     */
    public void setMovemntDate(String movemntDate) {
        this.movemntDate = movemntDate;
    }

    /**
     * @return the movemntTime
     */
    public String getMovemntTime() {
        return movemntTime;
    }

    /**
     * @param movemntTime the movemntTime to set
     */
    public void setMovemntTime(String movemntTime) {
        this.movemntTime = movemntTime;
    }

    /**
     * @return the movemntReas1
     */
    public String getMovemntReas1() {
        return movemntReas1;
    }

    /**
     * @param movemntReas1 the movemntReas1 to set
     */
    public void setMovemntReas1(String movemntReas1) {
        this.movemntReas1 = movemntReas1;
    }

    /**
     * @return the movemntReas1Text
     */
    public String getMovemntReas1Text() {
        return movemntReas1Text;
    }

    /**
     * @param movemntReas1Text the movemntReas1Text to set
     */
    public void setMovemntReas1Text(String movemntReas1Text) {
        this.movemntReas1Text = movemntReas1Text;
    }

    /**
     * @return the movemntReas2
     */
    public String getMovemntReas2() {
        return movemntReas2;
    }

    /**
     * @param movemntReas2 the movemntReas2 to set
     */
    public void setMovemntReas2(String movemntReas2) {
        this.movemntReas2 = movemntReas2;
    }

    /**
     * @return the movemntReas2Text
     */
    public String getMovemntReas2Text() {
        return movemntReas2Text;
    }

    /**
     * @param movemntReas2Text the movemntReas2Text to set
     */
    public void setMovemntReas2Text(String movemntReas2Text) {
        this.movemntReas2Text = movemntReas2Text;
    }

    /**
     * @return the statusInd
     */
    public String getStatusInd() {
        return statusInd;
    }

    /**
     * @param statusInd the statusInd to set
     */
    public void setStatusInd(String statusInd) {
        this.statusInd = statusInd;
    }

    /**
     * @return the statusIndText
     */
    public String getStatusIndText() {
        return statusIndText;
    }

    /**
     * @param statusIndText the statusIndText to set
     */
    public void setStatusIndText(String statusIndText) {
        this.statusIndText = statusIndText;
    }

    /**
     * @return the refPstTrtType
     */
    public String getRefPstTrtType() {
        return refPstTrtType;
    }

    /**
     * @param refPstTrtType the refPstTrtType to set
     */
    public void setRefPstTrtType(String refPstTrtType) {
        this.refPstTrtType = refPstTrtType;
    }

    /**
     * @return the refPstTrtTypeText
     */
    public String getRefPstTrtTypeText() {
        return refPstTrtTypeText;
    }

    /**
     * @param refPstTrtTypeText the refPstTrtTypeText to set
     */
    public void setRefPstTrtTypeText(String refPstTrtTypeText) {
        this.refPstTrtTypeText = refPstTrtTypeText;
    }

    /**
     * @return the refHospital
     */
    public String getRefHospital() {
        return refHospital;
    }

    /**
     * @param refHospital the refHospital to set
     */
    public void setRefHospital(String refHospital) {
        this.refHospital = refHospital;
    }

    /**
     * @return the dischrgDisp
     */
    public String getDischrgDisp() {
        return dischrgDisp;
    }

    /**
     * @param dischrgDisp the dischrgDisp to set
     */
    public void setDischrgDisp(String dischrgDisp) {
        this.dischrgDisp = dischrgDisp;
    }

    /**
     * @return the dischrgDispText
     */
    public String getDischrgDispText() {
        return dischrgDispText;
    }

    /**
     * @param dischrgDispText the dischrgDispText to set
     */
    public void setDischrgDispText(String dischrgDispText) {
        this.dischrgDispText = dischrgDispText;
    }

    /**
     * @return the workIncapacity
     */
    public String getWorkIncapacity() {
        return workIncapacity;
    }

    /**
     * @param workIncapacity the workIncapacity to set
     */
    public void setWorkIncapacity(String workIncapacity) {
        this.workIncapacity = workIncapacity;
    }

    /**
     * @return the creationDate
     */
    public String getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * @return the creationUser
     */
    public String getCreationUser() {
        return creationUser;
    }

    /**
     * @param creationUser the creationUser to set
     */
    public void setCreationUser(String creationUser) {
        this.creationUser = creationUser;
    }

    /**
     * @return the updateDate
     */
    public String getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate the updateDate to set
     */
    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * @return the updateUser
     */
    public String getUpdateUser() {
        return updateUser;
    }

    /**
     * @param updateUser the updateUser to set
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * @return the cancelInd
     */
    public String getCancelInd() {
        return cancelInd;
    }

    /**
     * @param cancelInd the cancelInd to set
     */
    public void setCancelInd(String cancelInd) {
        this.cancelInd = cancelInd;
    }

    /**
     * @return the cancelDate
     */
    public String getCancelDate() {
        return cancelDate;
    }

    /**
     * @param cancelDate the cancelDate to set
     */
    public void setCancelDate(String cancelDate) {
        this.cancelDate = cancelDate;
    }

    /**
     * @return the cancelUser
     */
    public String getCancelUser() {
        return cancelUser;
    }

    /**
     * @param cancelUser the cancelUser to set
     */
    public void setCancelUser(String cancelUser) {
        this.cancelUser = cancelUser;
    }

    /**
     * @return the cancelReason
     */
    public String getCancelReason() {
        return cancelReason;
    }

    /**
     * @param cancelReason the cancelReason to set
     */
    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    /**
     * @return the cancelReasonText
     */
    public String getCancelReasonText() {
        return cancelReasonText;
    }

    /**
     * @param cancelReasonText the cancelReasonText to set
     */
    public void setCancelReasonText(String cancelReasonText) {
        this.cancelReasonText = cancelReasonText;
    }

    /**
     * @return the creationTime
     */
    public String getCreationTime() {
        return creationTime;
    }

    /**
     * @param creationTime the creationTime to set
     */
    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    /**
     * @return the respiration
     */
    public String getRespiration() {
        return respiration;
    }

    /**
     * @param respiration the respiration to set
     */
    public void setRespiration(String respiration) {
        this.respiration = respiration;
    }

    public abstract void readFromStructure(final JCoStructure aTable);

}
