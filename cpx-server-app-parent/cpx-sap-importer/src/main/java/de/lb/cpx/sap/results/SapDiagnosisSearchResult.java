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

import com.sap.conn.jco.JCoTable;
import java.util.Date;

/**
 *
 * @author niemeier
 */
public class SapDiagnosisSearchResult {

    private String client = "";
    private String institution = "";
    private String patCaseId = "";
    private String diagSeqno = "";
    private String extDiagno = "";
    private String movemntSeqno = "";
//	public String iverDocno = "";
    private String diagCatalog1 = "";
    private String diagKey1 = "";
    private String diagCatalog2 = "";
    private String diagKey2 = "";
//	public String diagRefCat = "";
//	public String diagRefKey = "";
    private String diagText = "";
//	public String diagLongText = "";
    private Date diagCreatDate = null;
    private Date diagCreatTime = null;
    private String diagPerson = "";
    private String noSurgeries = "";
    private String referralDia = "";
    private String treatmentDia = "";
    private String admissionDia = "";
    private String dischargeDia = "";
    private String deptMainDia = "";
    private String hospMainDia = "";
    private String surgeryDia = "";
    private String blockingInd = "";
    private String shortText = "";
    private String longText = "";
    private String certLevel = "";
    private Date creationDate = null;
    private Date creationTime = null;
    private String creationUser = "";
    private Date updateDate = null;
    private String updateUser = "";
    private String cancelInd = "";
    private String cancelUser = "";
    private Date cancelDate = null;
    private String causeOfDeath = "";
    private String extDiagRefKey = "";
    private String drgDiaSeqno = "";
    private String drgCategory = "";
    private String drgCc = "";
    private String drgRelevant = "";
    private String diagTyp1 = "";
    private String diagTyp2 = "";
    private String diagRefTyp = "";
    private String alternDiaTxt = "";
    private String diaLink = "";
    private String workDiagInd = "";
    private String preopDiagInd = "";
    private String diagReference = "";
    private String diagPriority = "";
    private String diagCertainty = "";
    private String diagAddition = "";
    private String diagLocation = "";
    private String diagCcl = "";

    /**
     *
     */
    public SapDiagnosisSearchResult() {
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
     * @return the diagSeqno
     */
    public String getDiagSeqno() {
        return diagSeqno;
    }

    /**
     * @param diagSeqno the diagSeqno to set
     */
    public void setDiagSeqno(String diagSeqno) {
        this.diagSeqno = diagSeqno;
    }

    /**
     * @return the extDiagno
     */
    public String getExtDiagno() {
        return extDiagno;
    }

    /**
     * @param extDiagno the extDiagno to set
     */
    public void setExtDiagno(String extDiagno) {
        this.extDiagno = extDiagno;
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
     * @return the diagCatalog1
     */
    public String getDiagCatalog1() {
        return diagCatalog1;
    }

    /**
     * @param diagCatalog1 the diagCatalog1 to set
     */
    public void setDiagCatalog1(String diagCatalog1) {
        this.diagCatalog1 = diagCatalog1;
    }

    /**
     * @return the diagKey1
     */
    public String getDiagKey1() {
        return diagKey1;
    }

    /**
     * @param diagKey1 the diagKey1 to set
     */
    public void setDiagKey1(String diagKey1) {
        this.diagKey1 = diagKey1;
    }

    /**
     * @return the diagCatalog2
     */
    public String getDiagCatalog2() {
        return diagCatalog2;
    }

    /**
     * @param diagCatalog2 the diagCatalog2 to set
     */
    public void setDiagCatalog2(String diagCatalog2) {
        this.diagCatalog2 = diagCatalog2;
    }

    /**
     * @return the diagKey2
     */
    public String getDiagKey2() {
        return diagKey2;
    }

    /**
     * @param diagKey2 the diagKey2 to set
     */
    public void setDiagKey2(String diagKey2) {
        this.diagKey2 = diagKey2;
    }

    /**
     * @return the diagText
     */
    public String getDiagText() {
        return diagText;
    }

    /**
     * @param diagText the diagText to set
     */
    public void setDiagText(String diagText) {
        this.diagText = diagText;
    }

    /**
     * @return the diagCreatDate
     */
    public Date getDiagCreatDate() {
        return diagCreatDate == null ? null : new Date(diagCreatDate.getTime());
    }

    /**
     * @param diagCreatDate the diagCreatDate to set
     */
    public void setDiagCreatDate(Date diagCreatDate) {
        this.diagCreatDate = diagCreatDate == null ? null : new Date(diagCreatDate.getTime());
    }

    /**
     * @return the diagCreatTime
     */
    public Date getDiagCreatTime() {
        return diagCreatTime == null ? null : new Date(diagCreatTime.getTime());
    }

    /**
     * @param diagCreatTime the diagCreatTime to set
     */
    public void setDiagCreatTime(Date diagCreatTime) {
        this.diagCreatTime = diagCreatTime == null ? null : new Date(diagCreatTime.getTime());
    }

    /**
     * @return the diagPerson
     */
    public String getDiagPerson() {
        return diagPerson;
    }

    /**
     * @param diagPerson the diagPerson to set
     */
    public void setDiagPerson(String diagPerson) {
        this.diagPerson = diagPerson;
    }

    /**
     * @return the noSurgeries
     */
    public String getNoSurgeries() {
        return noSurgeries;
    }

    /**
     * @param noSurgeries the noSurgeries to set
     */
    public void setNoSurgeries(String noSurgeries) {
        this.noSurgeries = noSurgeries;
    }

    /**
     * @return the referralDia
     */
    public String getReferralDia() {
        return referralDia;
    }

    /**
     * @param referralDia the referralDia to set
     */
    public void setReferralDia(String referralDia) {
        this.referralDia = referralDia;
    }

    /**
     * @return the treatmentDia
     */
    public String getTreatmentDia() {
        return treatmentDia;
    }

    /**
     * @param treatmentDia the treatmentDia to set
     */
    public void setTreatmentDia(String treatmentDia) {
        this.treatmentDia = treatmentDia;
    }

    /**
     * @return the admissionDia
     */
    public String getAdmissionDia() {
        return admissionDia;
    }

    /**
     * @param admissionDia the admissionDia to set
     */
    public void setAdmissionDia(String admissionDia) {
        this.admissionDia = admissionDia;
    }

    /**
     * @return the dischargeDia
     */
    public String getDischargeDia() {
        return dischargeDia;
    }

    /**
     * @param dischargeDia the dischargeDia to set
     */
    public void setDischargeDia(String dischargeDia) {
        this.dischargeDia = dischargeDia;
    }

    /**
     * @return the deptMainDia
     */
    public String getDeptMainDia() {
        return deptMainDia;
    }

    /**
     * @param deptMainDia the deptMainDia to set
     */
    public void setDeptMainDia(String deptMainDia) {
        this.deptMainDia = deptMainDia;
    }

    /**
     * @return the hospMainDia
     */
    public String getHospMainDia() {
        return hospMainDia;
    }

    /**
     * @param hospMainDia the hospMainDia to set
     */
    public void setHospMainDia(String hospMainDia) {
        this.hospMainDia = hospMainDia;
    }

    /**
     * @return the surgeryDia
     */
    public String getSurgeryDia() {
        return surgeryDia;
    }

    /**
     * @param surgeryDia the surgeryDia to set
     */
    public void setSurgeryDia(String surgeryDia) {
        this.surgeryDia = surgeryDia;
    }

    /**
     * @return the blockingInd
     */
    public String getBlockingInd() {
        return blockingInd;
    }

    /**
     * @param blockingInd the blockingInd to set
     */
    public void setBlockingInd(String blockingInd) {
        this.blockingInd = blockingInd;
    }

    /**
     * @return the shortText
     */
    public String getShortText() {
        return shortText;
    }

    /**
     * @param shortText the shortText to set
     */
    public void setShortText(String shortText) {
        this.shortText = shortText;
    }

    /**
     * @return the longText
     */
    public String getLongText() {
        return longText;
    }

    /**
     * @param longText the longText to set
     */
    public void setLongText(String longText) {
        this.longText = longText;
    }

    /**
     * @return the certLevel
     */
    public String getCertLevel() {
        return certLevel;
    }

    /**
     * @param certLevel the certLevel to set
     */
    public void setCertLevel(String certLevel) {
        this.certLevel = certLevel;
    }

    /**
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate == null ? null : new Date(creationDate.getTime());
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate == null ? null : new Date(creationDate.getTime());
    }

    /**
     * @return the creationTime
     */
    public Date getCreationTime() {
        return creationTime == null ? null : new Date(creationTime.getTime());
    }

    /**
     * @param creationTime the creationTime to set
     */
    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime == null ? null : new Date(creationTime.getTime());
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
    public Date getUpdateDate() {
        return updateDate == null ? null : new Date(updateDate.getTime());
    }

    /**
     * @param updateDate the updateDate to set
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate == null ? null : new Date(updateDate.getTime());
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
     * @return the cancelDate
     */
    public Date getCancelDate() {
        return cancelDate == null ? null : new Date(cancelDate.getTime());
    }

    /**
     * @param cancelDate the cancelDate to set
     */
    public void setCancelDate(Date cancelDate) {
        this.cancelDate = cancelDate == null ? null : new Date(cancelDate.getTime());
    }

    /**
     * @return the causeOfDeath
     */
    public String getCauseOfDeath() {
        return causeOfDeath;
    }

    /**
     * @param causeOfDeath the causeOfDeath to set
     */
    public void setCauseOfDeath(String causeOfDeath) {
        this.causeOfDeath = causeOfDeath;
    }

    /**
     * @return the extDiagRefKey
     */
    public String getExtDiagRefKey() {
        return extDiagRefKey;
    }

    /**
     * @param extDiagRefKey the extDiagRefKey to set
     */
    public void setExtDiagRefKey(String extDiagRefKey) {
        this.extDiagRefKey = extDiagRefKey;
    }

    /**
     * @return the drgDiaSeqno
     */
    public String getDrgDiaSeqno() {
        return drgDiaSeqno;
    }

    /**
     * @param drgDiaSeqno the drgDiaSeqno to set
     */
    public void setDrgDiaSeqno(String drgDiaSeqno) {
        this.drgDiaSeqno = drgDiaSeqno;
    }

    /**
     * @return the drgCategory
     */
    public String getDrgCategory() {
        return drgCategory;
    }

    /**
     * @param drgCategory the drgCategory to set
     */
    public void setDrgCategory(String drgCategory) {
        this.drgCategory = drgCategory;
    }

    /**
     * @return the drgCc
     */
    public String getDrgCc() {
        return drgCc;
    }

    /**
     * @param drgCc the drgCc to set
     */
    public void setDrgCc(String drgCc) {
        this.drgCc = drgCc;
    }

    /**
     * @return the drgRelevant
     */
    public String getDrgRelevant() {
        return drgRelevant;
    }

    /**
     * @param drgRelevant the drgRelevant to set
     */
    public void setDrgRelevant(String drgRelevant) {
        this.drgRelevant = drgRelevant;
    }

    /**
     * @return the diagTyp1
     */
    public String getDiagTyp1() {
        return diagTyp1;
    }

    /**
     * @param diagTyp1 the diagTyp1 to set
     */
    public void setDiagTyp1(String diagTyp1) {
        this.diagTyp1 = diagTyp1;
    }

    /**
     * @return the diagTyp2
     */
    public String getDiagTyp2() {
        return diagTyp2;
    }

    /**
     * @param diagTyp2 the diagTyp2 to set
     */
    public void setDiagTyp2(String diagTyp2) {
        this.diagTyp2 = diagTyp2;
    }

    /**
     * @return the diagRefTyp
     */
    public String getDiagRefTyp() {
        return diagRefTyp;
    }

    /**
     * @param diagRefTyp the diagRefTyp to set
     */
    public void setDiagRefTyp(String diagRefTyp) {
        this.diagRefTyp = diagRefTyp;
    }

    /**
     * @return the alternDiaTxt
     */
    public String getAlternDiaTxt() {
        return alternDiaTxt;
    }

    /**
     * @param alternDiaTxt the alternDiaTxt to set
     */
    public void setAlternDiaTxt(String alternDiaTxt) {
        this.alternDiaTxt = alternDiaTxt;
    }

    /**
     * @return the diaLink
     */
    public String getDiaLink() {
        return diaLink;
    }

    /**
     * @param diaLink the diaLink to set
     */
    public void setDiaLink(String diaLink) {
        this.diaLink = diaLink;
    }

    /**
     * @return the workDiagInd
     */
    public String getWorkDiagInd() {
        return workDiagInd;
    }

    /**
     * @param workDiagInd the workDiagInd to set
     */
    public void setWorkDiagInd(String workDiagInd) {
        this.workDiagInd = workDiagInd;
    }

    /**
     * @return the preopDiagInd
     */
    public String getPreopDiagInd() {
        return preopDiagInd;
    }

    /**
     * @param preopDiagInd the preopDiagInd to set
     */
    public void setPreopDiagInd(String preopDiagInd) {
        this.preopDiagInd = preopDiagInd;
    }

    /**
     * @return the diagReference
     */
    public String getDiagReference() {
        return diagReference;
    }

    /**
     * @param diagReference the diagReference to set
     */
    public void setDiagReference(String diagReference) {
        this.diagReference = diagReference;
    }

    /**
     * @return the diagPriority
     */
    public String getDiagPriority() {
        return diagPriority;
    }

    /**
     * @param diagPriority the diagPriority to set
     */
    public void setDiagPriority(String diagPriority) {
        this.diagPriority = diagPriority;
    }

    /**
     * @return the diagCertainty
     */
    public String getDiagCertainty() {
        return diagCertainty;
    }

    /**
     * @param diagCertainty the diagCertainty to set
     */
    public void setDiagCertainty(String diagCertainty) {
        this.diagCertainty = diagCertainty;
    }

    /**
     * @return the diagAddition
     */
    public String getDiagAddition() {
        return diagAddition;
    }

    /**
     * @param diagAddition the diagAddition to set
     */
    public void setDiagAddition(String diagAddition) {
        this.diagAddition = diagAddition;
    }

    /**
     * @return the diagLocation
     */
    public String getDiagLocation() {
        return diagLocation;
    }

    /**
     * @param diagLocation the diagLocation to set
     */
    public void setDiagLocation(String diagLocation) {
        this.diagLocation = diagLocation;
    }

    /**
     * @return the diagCcl
     */
    public String getDiagCcl() {
        return diagCcl;
    }

    /**
     * @param diagCcl the diagCcl to set
     */
    public void setDiagCcl(String diagCcl) {
        this.diagCcl = diagCcl;
    }

    /**
     *
     * @param aTable JCO table
     */
    public void readFromTable(JCoTable aTable) {
        setClient(aTable.getString(0));
        setInstitution(aTable.getString(1));
        setPatCaseId(aTable.getString(2));
        setDiagSeqno(aTable.getString(3));
        setExtDiagno(aTable.getString(4));
        setMovemntSeqno(aTable.getString(5));
//		IVER_DOCNO = aTable.getString(6);
        setDiagCatalog1(aTable.getString(7));
        setDiagKey1(aTable.getString(8));
        setDiagCatalog2(aTable.getString(9));
        setDiagKey2(aTable.getString(10));
//		DIAG_REF_CAT = aTable.getString(11);
//		DIAG_REF_KEY = aTable.getString(12);
        setDiagText(aTable.getString(13));
//		DIAG_LONGTEXT = aTable.getString(14);
        setDiagCreatDate(aTable.getDate(15));
        setDiagCreatTime(aTable.getTime(16));
        setDiagPerson(aTable.getString(17));
        setNoSurgeries(aTable.getString(18));
        setReferralDia(aTable.getString(19));
        setTreatmentDia(aTable.getString(20));
        setAdmissionDia(aTable.getString(21));
        setDischargeDia(aTable.getString(22));
        setDeptMainDia(aTable.getString(23));
        setHospMainDia(aTable.getString(24));
        setSurgeryDia(aTable.getString(25));
        setBlockingInd(aTable.getString(26));
        setShortText(aTable.getString(27));
        setLongText(aTable.getString(28));
        setCertLevel(aTable.getString(29));
        setCreationDate(aTable.getDate(30));
        setCreationTime(aTable.getTime(31));
        setCreationUser(aTable.getString(32));
        setUpdateDate(aTable.getDate(33));
        setUpdateUser(aTable.getString(34));
        setCancelInd(aTable.getString(35).trim());
        setCancelUser(aTable.getString(36));
        setCancelDate(aTable.getDate(37));
        setCauseOfDeath(aTable.getString(38));
        setExtDiagRefKey(aTable.getString(39));
        setDrgDiaSeqno(aTable.getString(40));
        setDrgCategory(aTable.getString(41));
        setDrgCc(aTable.getString(42));
        setDrgRelevant(aTable.getString(43));
        setDiagTyp1(aTable.getString(44));
        setDiagTyp2(aTable.getString(45));
        setDiagRefTyp(aTable.getString(46));
        setAlternDiaTxt(aTable.getString(47));
        setDiaLink(aTable.getString(48));
        setWorkDiagInd(aTable.getString(49));
        setPreopDiagInd(aTable.getString(50));
        setDiagReference(aTable.getString(51));
        setDiagPriority(aTable.getString(52));
        setDiagCertainty(aTable.getString(53));
        setDiagAddition(aTable.getString(54));
        setDiagLocation(aTable.getString(55));
        setDiagCcl(aTable.getString(56));
    }

    /**
     *
     * @param aTable JCO table
     */
    public void readFromStructurTable(final JCoTable aTable) {
        setClient(aTable.getString("MANDT"));                               //MANDT
        setInstitution(aTable.getString("EINRI"));                          //EINRI
        setPatCaseId(aTable.getString("FALNR"));                            //FALNR
        setDiagSeqno(aTable.getString("LFDNR"));                           //LFDNR
        setMovemntSeqno(aTable.getString("LFDBEW"));                       //LFDBEW
        setDiagCatalog1(aTable.getString("DKAT1"));                        //DKAT1
        setDiagKey1(aTable.getString("DKEY1"));                            //DKEY1
        setDiagCatalog2(aTable.getString("DKAT2"));                        //DKAT2
        setDiagKey2(aTable.getString("DKEY2"));                            //DKEY2
        setDiagText(aTable.getString("DITXT"));                           //DITXT
        setReferralDia(aTable.getString("EWDIA"));                        //EWDIA
        setTreatmentDia(aTable.getString("BHDIA"));                       //BHDIA
        setAdmissionDia(aTable.getString("AFDIA"));                       //AFDIA
        setDischargeDia(aTable.getString("ENDIA"));                       //ENDIA
        setDeptMainDia(aTable.getString("FHDIA"));                       //FHDIA
        setHospMainDia(aTable.getString("KHDIA"));                       //KHDIA
        setSurgeryDia(aTable.getString("OPDIA"));                         //OPDIA
        setCreationDate(aTable.getDate("ERDAT"));                         //ERDAT
        setCreationTime(aTable.getTime("ERTIM"));                         //ERTIM
        setCreationUser(aTable.getString("ERUSR"));                       //ERUSR
        setUpdateDate(aTable.getDate("UPDAT"));                           //UPDAT
        setUpdateUser(aTable.getString("UPUSR"));                         //UPUSR
        setCancelInd(aTable.getString("STORN").trim());                   //STORN
        setDrgDiaSeqno(aTable.getString("DRG_DIA_SEQNO"));               //DRG_DIA_SEQNO
        setDrgCategory(aTable.getString("DRG_CATEGORY"));                 //DRG_CATEGORY
        setDrgRelevant(aTable.getString("DRG_RELVANT"));                   //DRG_RELVANT
        setDiagTyp1(aTable.getString("DTYP1"));                           //DTYP1
        setDiagTyp2(aTable.getString("DTYP2"));                           //DTYP2
        setDiagRefTyp(aTable.getString("DTYP_REF"));                     //DTYP_REF
        setDiaLink(aTable.getString("DIA_LINK"));                            //DIA_LINK
        setWorkDiagInd(aTable.getString("ARDIA"));                       //ARDIA
        setPreopDiagInd(aTable.getString("PODIA"));                      //PODIA
        setDiagAddition(aTable.getString("DIAZS"));                       //DIAZS
        setDiagLocation(aTable.getString("DIALO"));                       //DIALO
//		blockingInd = aTable.getString("SPERR");                        //SPERR
//		shortText = aTable.getString("KZTXT");                          //KZTXT
//		longText = aTable.getString("LGTXT");                           //LGTXT
//		certLevel = aTable.getString("DIASI");                          //DIASI
//		cancelUser = aTable.getString("STUSR");                         //STUSR
//		cancelDate = aTable.getDate("STDAT");                           //STDAT
//		causeOfDeath = aTable.getString("TUDIA");                      //TUDIA
//		extDiagRefKey = aTable.getString("REFKY");                     //REFKY
//		drgCc = aTable.getString("drgCc");                             //DRG_CC
//		alternDiaTxt = aTable.getString("alternDiaTxt");               //ALTERN_DIATXT
//		diagCcl = "";                                              //nv, nb
//		extDiagno = aTable.getString("EXTID");                           //EXTID
//		IVER_DOCNO = "";                                                  //nv, nb
//		DIAG_REF_CAT = aTable.getString("DKAT_REF");                      //DKAT_REF
//		DIAG_REF_KEY = aTable.getString("DKEY_REF");                      //DKEY_REF
//		DIAG_LONGTEXT = aTable.getString("DILTX");                       //DILTX
//		diagCreatDate = aTable.getDate("DIADT");                       //DIADT
//		diagCreatTime = aTable.getTime("DIAZT");                       //DIAZT
//		diagPerson = aTable.getString("DIAPE");                         //DIAPE
//		noSurgeries = aTable.getString("ANZOP");                        //ANZOP
//		diagReference = aTable.getString("DIABZ");                      //DIABZ
//		diagPriority = aTable.getString("DIAPR");                       //DIAPR
//		diagCertainty = aTable.getString("DIAGW");                      //DIAGW

    }

    @Override
    public String toString() {
        return "Mandant >" + getClient() + "<\n"
                + "Einrichtung >" + getInstitution() + "<\n"
                + "Fallnummer >" + getPatCaseId() + "<\n"
                + "Laufende Nummer Diagnose >" + getDiagSeqno() + "<\n"
                + "*MED: Interne Kennung des Diagnosecodierungstools >" + getExtDiagno() + "<\n"
                + "Laufende Nummer einer Bewegung >" + getMovemntSeqno() + "<\n"
                + //			   "Belegnummer der Kostenübernahme/des Scheines >" + IVER_DOCNO + "<\n" +
                "Identifikationskürzel für Diagnosekatalog >" + getDiagCatalog1() + "<\n"
                + "Schlüsselung einer Diagnose >" + getDiagKey1() + "<\n"
                + "Identifikationskürzel für Diagnosekatalog >" + getDiagCatalog2() + "<\n"
                + "Schlüsselung einer Diagnose >" + getDiagKey2() + "<\n"
                + //			   "Katalog-ID des Referenzkatalogs für Statistiken >" + DIAG_REF_CAT + "<\n" +
                //			   "Schlüsselung einer Referenzdiagnose für Statistiken >" + DIAG_REF_KEY + "<\n" +
                "Freitext einer Diagnose >" + getDiagText() + "<\n"
                + //			   "Kennzeichen, ob Langtext vorhanden ist >" + DIAG_LONGTEXT + "<\n" +
                "Datum, an dem die Diagnose erstellt wurde >" + getDiagCreatDate() + "<\n"
                + "Uhrzeit der Diagnoseerstellung >" + getDiagCreatTime() + "<\n"
                + "Identifikation diagnostizierende Person >" + getDiagPerson() + "<\n"
                + "Anzahl Operationen wegen Hauptdiagnose >" + getNoSurgeries() + "<\n"
                + "Kennzeichen Einweisungs- bzw. Überweisungsdiagnose >" + getReferralDia() + "<\n"
                + "Kennzeichen Behandlungsdiagnose >" + getTreatmentDia() + "<\n"
                + "Kennzeichen Aufnahmediagnose >" + getAdmissionDia() + "<\n"
                + "Kennzeichen Entlassungsdiagnose >" + getDischargeDia() + "<\n"
                + "Kennzeichen Fachabteilungshauptdiagnose >" + getDeptMainDia() + "<\n"
                + "Kennzeichen Krankenhaus-Hauptdiagnose >" + getHospMainDia() + "<\n"
                + "Kennzeichen Operationsdiagnose >" + getSurgeryDia() + "<\n"
                + "Sperrkennzeichen >" + getBlockingInd() + "<\n"
                + "Bemerkung zur Diagnose >" + getShortText() + "<\n"
                + "Kennzeichen, ob Langtext vorhanden ist >" + getLongText() + "<\n"
                + "Grad der Sicherheit der Diagnose >" + getCertLevel() + "<\n"
                + "Datum, an dem der Satz hinzugefügt wurde >" + getCreationDate() + "<\n"
                + "Uhrzeit, zu der der Satz hinzugefügt wurde >" + getCreationTime() + "<\n"
                + "Name des Sachbearbeiters, der den Satz hinzugefügt hat >" + getCreationUser() + "<\n"
                + "Änderungsdatum >" + getUpdateDate() + "<\n"
                + "Name des ändernden Sachbearbeiters >" + getUpdateUser() + "<\n"
                + "Stornokennzeichen >" + getCancelInd() + "<\n"
                + "Name des stornierenden Sachbearbeiters >" + getCancelUser() + "<\n"
                + "Stornierungsdatum >" + getCancelDate() + "<\n"
                + "Kennzeichen Todesursache >" + getCauseOfDeath() + "<\n"
                + "Referenzschlüssel Diagnose aus externem System >" + getExtDiagRefKey() + "<\n"
                + "Fortlaufende Numerierung einer DRG-Diagnose >" + getDrgDiaSeqno() + "<\n"
                + "Kategorie einer DRG-Diagnose (Haupt-, Neben-) >" + getDrgCategory() + "<\n"
                + "Begleiterkrankungen u. Komplikationen (DRG-Diagnose) >" + getDrgCc() + "<\n"
                + "Kennz. für DRG-Ermittlung verwendet >" + getDrgRelevant() + "<\n"
                + "Typ für ICD-10-Diagnosen >" + getDiagTyp1() + "<\n"
                + "Typ für ICD-10-Diagnosen >" + getDiagTyp2() + "<\n"
                + "Typ für ICD-10-Diagnosen >" + getDiagRefTyp() + "<\n"
                + "Alternativer Diagnosetext (aus externem System) >" + getAlternDiaTxt() + "<\n"
                + "Verweis auf eine andere Diagnose >" + getDiaLink() + "<\n"
                + "*MED: Kennzeichen Arbeitsdiagnose >" + getWorkDiagInd() + "<\n"
                + "*Med: Kennzeichen Präoperative Diagnose >" + getPreopDiagInd() + "<\n"
                + "*MED: Diagnosenbezug >" + getDiagReference() + "<\n"
                + "*MED: Diagnosenpriorität >" + getDiagPriority() + "<\n"
                + "Diagnostische Gewißheit >" + getDiagCertainty() + "<\n"
                + "*MED: Diagnosenzusatz >" + getDiagAddition() + "<\n"
                + "Lokalisation einer Diagnose >" + getDiagLocation() + "<\n"
                + "Komplikationslevel der Diagnosen (für DRGs) >" + getDiagCcl() + "<";
    }

}
