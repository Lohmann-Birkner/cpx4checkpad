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
package de.lb.cpx.shared.dto;

import java.util.Date;

/**
 *
 * @author Wilde
 */
public class WorkingListItemDTO extends SearchItemDTO {

    private static final long serialVersionUID = 1L;

    /*
    public static Attribute<Long> getCaseIdAttributes() {
      return new Attribute<>();
    }
     */
    private Long csdId; //CaseDetailsId

    //private String grpresCode; //feegroup drg
    protected String csCaseNumber; ///Fallnummer
    private Date csCreationDate; //Anlegedatum

    protected Date csdAdmissionDate; //Aufnahmedatum
    private Integer csdAdmissionDateWeekday; //Aufnahmedatum Wochentag
    protected String csdAdmReason12En; //Aufnahmegrund
    private String csdAdmReason34En; //Aufnahmegrund2
    private String csdAdmCauseEn; //Aufnahmegrund
//    private String admissionWD; //AufnahmeWT

    protected Date csdDischargeDate; // Entl. Datum
    private Integer csdDischargeDateWeekday; //Entlassungsdatum Wochentag
    private String csdDisReason12En; //Entlassungsgrund
//    private String dischargeWD; //Entl. WT

    private String csCaseTypeEn; //fallart
    protected String csHospitalIdent;
//    private Integer caseStatus;

    //private String ikz; //ikz
    private String grpresGroup; //mdc

    private Integer csdLeave; //Urlaubstage

//    private Integer transferDaysAdmission; 
//    private Integer transferDaysDischarge;
    private Long csdLos; //vwd
    private Long csdLosAlteration; //vwd?
//    private String vwdIntensiv;
//    private Date wvDate;
//    private String wvCaseworker;
    private String patNumber; //Pat.-Nummer
    private Integer csdAgeYears; //Alter in Jahren
    private Integer csdAgeDays; //Alter in Tagen
    private String patGenderEn; //Geschlecht
    private Integer csdAdmissionWeight; //Aufnahmegewicht
    private Integer csdHmv; //Beatmung
    private String patdZipcode; //Einzuggebiet

    //INSURANCE
    private String insInsCompany;
    private String insInsCompanyName; //Versicherungsnamen
    private String insInsCompanyShortName; //Versicherungs-/Krankenkassengruppen
    private String icdCode; //Haupt-ICD

    //CALCULATEDDATA
//    private Double cw;
//    private Double cwPlus;
//    private Double cwMinus;
//    private Double cwCatalog;
//    private Double proceedsPlus; 
//    private Double proceedsMinus; 
//    private Integer oGVDDays;
//    private Integer uGVDDays;
//    private Integer opWT;
    private Integer pccl; //pccl
//    private String checkResult;
//    private String statusCheckpoint;
    private Integer countSd; //Summe ND
    private Integer countProc; //Summe Proz.
    private String csStatusEn;

    //Customer extension
    private String string1;
    private String string2;
    private String string3;
    private String string4;
    private String string5;
    private String string6;
    private String string7;
    private String string8;
    private String string9;
    private String string10;
    private int numeric1;
    private int numeric2;
    private int numeric3;
    private int numeric4;
    private int numeric5;
    private int numeric6;
    private int numeric7;
    private int numeric8;
    private int numeric9;
    private int numeric10;
    private Double csdDouble1;
    private Double csdDouble2;
    private Double csdDouble3;
    private Double csdDouble4;
    private Double csdDouble5;
    private Date csdDate1;
    private Date csdDate2;
    private Date csdDate3;
    private Date csdDate4;
    private Date csdDate5;

    private Double cwPositive;
    private Double cwNegative;
    private Double cwEffective;
    private Double cwCatalog;
    private String csDrg;
    private String caseFees;
    private String caseSupplFees;
    private Double dCWmin;
    private Double dCWmax;
    private String rule;
    private boolean isCancel;

    private Long csdVersionNumber;
    
    private Double dCareCWmin;
    private Double dCareCWmax;
    
    private Double dRevenueMin;
    private Double dRevenueMax;
    
    private Double careCw;
    private Date csBillingDate;
    
    /*
    public String getGrpresCode() {
        return grpresCode;
    }

    public void setGrpresCode(String grpresCode) {
        this.grpresCode = grpresCode;
    }
     */
    public String getCaseFees() {
        return caseFees;
    }

    public void setCaseFees(String caseFees) {
        this.caseFees = caseFees;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getCsCaseNumber() {
        return csCaseNumber;
    }

    public void setCsCaseNumber(String csCaseNumber) {
        this.csCaseNumber = csCaseNumber;
    }

    public void setCsCaseTypeEn(String csCaseTypeEn) {
        this.csCaseTypeEn = csCaseTypeEn;
    }

    public Date getCsCreationDate() {
        return csCreationDate == null ? null : new Date(csCreationDate.getTime());
    }

    public void setCsCreationDate(Date creationDate) {
        this.csCreationDate = creationDate == null ? null : new Date(creationDate.getTime());
    }

    public Date getCsdAdmissionDate() {
        return csdAdmissionDate == null ? null : new Date(csdAdmissionDate.getTime());
    }

    public void setCsdAdmissionDate(Date csdAdmissionDate) {
        this.csdAdmissionDate = csdAdmissionDate == null ? null : new Date(csdAdmissionDate.getTime());
    }

    public String getCsdAdmReason12En() {
        return csdAdmReason12En;
    }

    public void setCsdAdmReason12En(String csdAdmReason12En) {
        this.csdAdmReason12En = csdAdmReason12En;
    }

    public String getCsdAdmReason34En() {
        return csdAdmReason34En;
    }

    public void setCsdAdmReason34En(String csdAdmReason34En) {
        this.csdAdmReason34En = csdAdmReason34En;
    }

    public String getCsdAdmCauseEn() {
        return csdAdmCauseEn;
    }

    public void setCsdAdmCauseEn(String csdAdmCauseEn) {
        this.csdAdmCauseEn = csdAdmCauseEn;
    }

    public Date getCsdDischargeDate() {
        return csdDischargeDate == null ? null : new Date(csdDischargeDate.getTime());
    }

    public void setCsdDischargeDate(Date csdDischargeDate) {
        this.csdDischargeDate = csdDischargeDate == null ? null : new Date(csdDischargeDate.getTime());
    }

    public String getCsdDisReason12En() {
        return csdDisReason12En;
    }

    public void setCsdDisReason12En(String csdDisReason12En) {
        this.csdDisReason12En = csdDisReason12En;
    }

    public String getCsCaseTypeEn() {
        return csCaseTypeEn;
    }

    /*
    public String getGrpresGroup() {
        return grpresGroup;
    }

    public void setGrpresGroup(String grpresGroup) {
        this.grpresGroup = grpresGroup;
    }
     */
    public Integer getCsdLeave() {
        return csdLeave;
    }

    public void setCsdLeave(Integer csdLeave) {
        this.csdLeave = csdLeave;
    }

    public Long getCsdLos() {
        return csdLos;
    }

    public Long getCsdLosAlteration() {
        return csdLosAlteration;
    }

    public void setCsdLos(Long csdLos) {
        this.csdLos = csdLos;
    }

    public void setCsdLosAlteration(Long csdLosAlteration) {
        this.csdLosAlteration = csdLosAlteration;
    }

    @Override
    public String getPatNumber() {
        return patNumber;
    }

    public void setPatNumber(String patNumber) {
        this.patNumber = patNumber;
    }

    public void setIcdCode(String icdCode) {
        this.icdCode = icdCode;
    }

    public String getIcdCode() {
        return icdCode;
    }

    public Integer getCsdAgeYears() {
        return csdAgeYears;
    }

    public void setCsdAgeYears(Integer csdAgeYears) {
        this.csdAgeYears = csdAgeYears;
    }

    public Integer getCsdAgeDays() {
        return csdAgeDays;
    }

    public void setCsdAgeDays(Integer csdAgeDays) {
        this.csdAgeDays = csdAgeDays;
    }

    public String getPatGenderEn() {
        return patGenderEn;
    }

    public void setPatGenderEn(String patGenderEn) {
        this.patGenderEn = patGenderEn;
    }

    public Integer getCsdAdmissionWeight() {
        return csdAdmissionWeight;
    }

    public void setCsdAdmissionWeight(Integer csdAdmissionWeight) {
        this.csdAdmissionWeight = csdAdmissionWeight;
    }

    public Integer getCsdHmv() {
        return csdHmv;
    }

    public void setCsdHmv(Integer csdHmv) {
        this.csdHmv = csdHmv;
    }

    public String getPatdZipcode() {
        return patdZipcode;
    }

    public void setPatdZipcode(String patdZipcode) {
        this.patdZipcode = patdZipcode;
    }

    public String getInsInsCompany() {
        return insInsCompany;
    }

    public void setInsInsCompany(String insInsCompany) {
        this.insInsCompany = insInsCompany;
    }

    /**
     * @return the insInsCompanyShortName
     */
    public String getInsInsCompanyShortName() {
        return insInsCompanyShortName;
    }

    /**
     * @param insInsCompanyShortName the insInsCompanyShortName to set
     */
    public void setInsInsCompanyShortName(String insInsCompanyShortName) {
        this.insInsCompanyShortName = insInsCompanyShortName;
    }

    
    public int getPccl() {
        
        return pccl == null?0:pccl;
    }

    public void setPccl(Integer pccl) {
        this.pccl = pccl;
    }
     
    public Integer getCountProc() {
        return countProc;
    }

    public void setCountProc(Integer countProc) {
        this.countProc = countProc;
    }

    public Integer getCountSd() {
        return countSd;
    }

    public void setCountSd(Integer countSd) {
        this.countSd = countSd;
    }

    public String getCsStatusEn() {
        return csStatusEn;
    }

    public void setCsStatusEn(String csStatusEn) {
        this.csStatusEn = csStatusEn;
    }

    public String getCsHospitalIdent() {
        return csHospitalIdent;
    }

    public void setCsHospitalIdent(String csHospitalIdent) {
        this.csHospitalIdent = csHospitalIdent;
    }

    //Customer extensions:
    public String getString1() {
        return string1;
    }

    public void setString1(final String pString1) {
        this.string1 = pString1;
    }

    public String getString2() {
        return string2;
    }

    public void setString2(final String pString2) {
        this.string2 = pString2;
    }

    public String getString3() {
        return string3;
    }

    public void setString3(final String pString3) {
        this.string3 = pString3;
    }

    public String getString4() {
        return string4;
    }

    public void setString4(final String pString4) {
        this.string4 = pString4;
    }

    public String getString5() {
        return string5;
    }

    public void setString5(final String pString5) {
        this.string5 = pString5;
    }

    public String getString6() {
        return string6;
    }

    public void setString6(final String pString6) {
        this.string6 = pString6;
    }

    public String getString7() {
        return string7;
    }

    public void setString7(final String pString7) {
        this.string7 = pString7;
    }

    public String getString8() {
        return string8;
    }

    public void setString8(final String pString8) {
        this.string8 = pString8;
    }

    public String getString9() {
        return string9;
    }

    public void setString9(final String pString9) {
        this.string9 = pString9;
    }

    public String getString10() {
        return string10;
    }

    public void setString10(final String pString10) {
        this.string10 = pString10;
    }

    public int getNumeric1() {
        return numeric1;
    }

    public void setNumeric1(final int pNmeric1) {
        this.numeric1 = pNmeric1;
    }

    public int getNumeric2() {
        return numeric2;
    }

    public void setNumeric2(final int pNmeric2) {
        this.numeric2 = pNmeric2;
    }

    public int getNumeric3() {
        return numeric3;
    }

    public void setNumeric3(final int pNmeric3) {
        this.numeric3 = pNmeric3;
    }

    public int getNumeric4() {
        return numeric4;
    }

    public void setNumeric4(final int pNmeric4) {
        this.numeric4 = pNmeric4;
    }

    public int getNumeric5() {
        return numeric5;
    }

    public void setNumeric5(final int pNmeric5) {
        this.numeric5 = pNmeric5;
    }

    public int getNumeric6() {
        return numeric6;
    }

    public void setNumeric6(final int pNmeric6) {
        this.numeric6 = pNmeric6;
    }

    public int getNumeric7() {
        return numeric7;
    }

    public void setNumeric7(final int pNmeric7) {
        this.numeric7 = pNmeric7;
    }

    public int getNumeric8() {
        return numeric8;
    }

    public void setNumeric8(final int pNmeric8) {
        this.numeric8 = pNmeric8;
    }

    public int getNumeric9() {
        return numeric9;
    }

    public void setNumeric9(final int pNmeric9) {
        this.numeric9 = pNmeric9;
    }

    public int getNumeric10() {
        return numeric10;
    }

    public void setNumeric10(final int pNmeric10) {
        this.numeric10 = pNmeric10;
    }

    public Double getCsdDouble1() {
        return csdDouble1;
    }

    public void setCsdDouble1(final Double pCsdDouble1) {
        this.csdDouble1 = pCsdDouble1;
    }

    public Double getCsdDouble2() {
        return csdDouble2;
    }

    public void setCsdDouble2(final Double pCsdDouble2) {
        this.csdDouble2 = pCsdDouble2;
    }

    public Double getCsdDouble3() {
        return csdDouble3;
    }

    public void setCsdDouble3(final Double pCsdDouble3) {
        this.csdDouble3 = pCsdDouble3;
    }

    public Double getCsdDouble4() {
        return csdDouble4;
    }

    public void setCsdDouble4(final Double pCsdDouble4) {
        this.csdDouble4 = pCsdDouble4;
    }

    public Double getCsdDouble5() {
        return csdDouble5;
    }

    public void setCsdDouble5(final Double pCsdDouble5) {
        this.csdDouble5 = pCsdDouble5;
    }

    public Date getCsdDate1() {
        return csdDate1 == null ? null : new Date(csdDate1.getTime());
    }

    public void setCsdDate1(final Date pCsdDate1) {
        this.csdDate1 = pCsdDate1 == null ? null : new Date(pCsdDate1.getTime());
    }

    public Date getCsdDate2() {
        return csdDate2 == null ? null : new Date(csdDate2.getTime());
    }

    public void setCsdDate2(final Date pCsdDate2) {
        this.csdDate2 = pCsdDate2 == null ? null : new Date(pCsdDate2.getTime());
    }

    public Date getCsdDate3() {
        return csdDate3 == null ? null : new Date(csdDate3.getTime());
    }

    public void setCsdDate3(final Date pCsdDate3) {
        this.csdDate3 = pCsdDate3 == null ? null : new Date(pCsdDate3.getTime());
    }

    public Date getCsdDate4() {
        return csdDate4 == null ? null : new Date(csdDate4.getTime());
    }

    public void setCsdDate4(final Date pCsdDate4) {
        this.csdDate4 = pCsdDate4 == null ? null : new Date(pCsdDate4.getTime());
    }

    public Date getCsdDate5() {
        return csdDate5 == null ? null : new Date(csdDate5.getTime());
    }

    public void setCsdDate5(final Date pCsdDate5) {
        this.csdDate5 = pCsdDate5 == null ? null : new Date(pCsdDate5.getTime());
    }

//    public String getAdmissionWT() {
//        return admissionWD;
//    }
//
//    public void setAdmissionWT(String admissionWT) {
//        this.admissionWD = admissionWT;
//    }
//    public String getDischargeWT() {
//        return dischargeWD;
//    }
//
//    public void setDischargeWT(String dischargeWT) {
//        this.dischargeWD = dischargeWT;
//    }
//    public Integer getCaseStatus() {
//        return caseStatus;
//    }
//
//    public void setCaseStatus(Integer caseStatus) {
//        this.caseStatus = caseStatus;
//    }
//    public Integer getTransferDaysAdmission() {
//        return transferDaysAdmission;
//    }
//
//    public void setTransferDaysAdmission(Integer transferDaysAdmission) {
//        this.transferDaysAdmission = transferDaysAdmission;
//    }
//
//    public Integer getTransferDaysDischarge() {
//        return transferDaysDischarge;
//    }
//
//    public void setTransferDaysDischarge(Integer transferDaysDischarge) {
//        this.transferDaysDischarge = transferDaysDischarge;
//    }
//    public String getVwdIntensiv() {
//        return vwdIntensiv;
//    }
//
//    public void setVwdIntensiv(String vwdIntensiv) {
//        this.vwdIntensiv = vwdIntensiv;
//    }
//
//    public Date getWvDate() {
//        return wvDate;
//    }
//
//    public void setWvDate(Date wvDate) {
//        this.wvDate = wvDate;
//    }
//
//    public String getWvCaseworker() {
//        return wvCaseworker;
//    }
//
//    public void setWvCaseworker(String wvCaseworker) {
//        this.wvCaseworker = wvCaseworker;
//    }
//    public Double getCwPlus() {
//        return cwPlus;
//    }
//
//    public void setCwPlus(Double cwPlus) {
//        this.cwPlus = cwPlus;
//    }
//
//    public Double getCwMinus() {
//        return cwMinus;
//    }
//
//    public void setCwMinus(Double cwMinus) {
//        this.cwMinus = cwMinus;
//    }
//
//    public Double getCwCatalog() {
//        return cwCatalog;
//    }
//
//    public void setCwCatalog(Double cwCatalog) {
//        this.cwCatalog = cwCatalog;
//    }
//
//    public Double getProceedsPlus() {
//        return proceedsPlus;
//    }
//
//    public void setProceedsPlus(Double proceedsPlus) {
//        this.proceedsPlus = proceedsPlus;
//    }
//
//    public Double getProceedsMinus() {
//        return proceedsMinus;
//    }
//
//    public void setProceedsMinus(Double proceedsMinus) {
//        this.proceedsMinus = proceedsMinus;
//    }
//
//    public Integer getoGVDDays() {
//        return oGVDDays;
//    }
//
//    public void setoGVDDays(Integer oGVDDays) {
//        this.oGVDDays = oGVDDays;
//    }
//
//    public Integer getuGVDDays() {
//        return uGVDDays;
//    }
//
//    public void setuGVDDays(Integer uGVDDays) {
//        this.uGVDDays = uGVDDays;
//    }
//
//    public Integer getOpWT() {
//        return opWT;
//    }
//
//    public void setOpWT(Integer opWT) {
//        this.opWT = opWT;
//    }
//    public String getCheckResult() {
//        return checkResult;
//    }
//
//    public void setCheckResult(String checkResult) {
//        this.checkResult = checkResult;
//    }
//
//    public String getStatusCheckpoint() {
//        return statusCheckpoint;
//    }
//
//    public void setStatusCheckpoint(String statusCheckpoint) {
//        this.statusCheckpoint = statusCheckpoint;
//    }
    @Override
    public String toString() {
        return "WorkingListDTO : " + "\n"
                // +"grpresCode " + grpresCode + "\n"//drg
                + "csCaseNumber " + csCaseNumber + "\n" ///Fallnummer
                + "creationDate " + csCreationDate + "\n" //Anlegedatum
                + "csdAdmissionDate " + csdAdmissionDate + "\n" //Aufnahmedatum
                + "csdAdmReason12En " + csdAdmReason12En + "\n" //Aufnahmegrund
                + "csdAdmReason34En " + csdAdmReason34En + "\n" //Aufnahmegrund2
                + "csdAdmCauseEn " + csdAdmCauseEn + "\n" //Aufnahmegrund
                //                +"admissionWD " + admissionWD + "\n" //AufnahmeWT
                + "csdDischargeDate " + csdDischargeDate + "\n" // Entl. Datum
                + "csdDisReason12En " + csdDisReason12En + "\n" //Entlassungsgrund
                //                +"dischargeWD " + dischargeWD + "\n" //Entl. WT
                + "csCaseTypeEn " + csCaseTypeEn + "\n" //fallart
                //              +"caseStatus " + caseStatus + "\n"
                // +"ikz " + ikz + "\n" //ikz
                //                +"grpresGroup " + grpresGroup + "\n" //mdc
                + "csdLeave " + csdLeave + "\n" //Urlaubstage
                //               +"transferDaysAdmission " + transferDaysAdmission + "\n" 
                //               +"transferDaysDischarge " + transferDaysDischarge + "\n"
                + "csdLos " + csdLos + "\n" //vwd
                //              +"vwdIntensiv " + vwdIntensiv + "\n"
                //              +"wvDate" + wvDate + "\n"
                //              +"wvCaseworker" + wvCaseworker + "\n"

                //PATIENTDATA
                + "patName " + getPatName() + "\n" //Name
                + "patNumber " + patNumber + "\n" //Pat.-Nummer
                + "patDateOfBirth " + getPatDateOfBirth() + "\n" //Geburtsdatum
                + "csdAgeYears " + csdAgeYears + "\n" //Alter in Jahren
                + "csdAgeDays " + csdAgeDays + "\n" //Alter in Tagen
                + "patGenderEn " + patGenderEn + "\n" //Geschlecht
                + "csdAdmissionWeight " + csdAdmissionWeight + "\n" //Aufnahmegewicht
                + "csdHmv " + csdHmv + "\n" //Beatmung
                + "patdZipcode " + patdZipcode + "\n" //Einzuggebiet

                //CALCULATEDDATA
                //    cw;
                //    cwPlus;
                //    cwMinus;
                //    cwCatalog;

                //    proceedsPlus; 
                //    proceedsMinus; 

                //    oGVDDays;
                //    uGVDDays;
                //    opWT;

                //                +"pccl " + pccl + "\n" //pccl
                //  checkResult;

                //    private String statusCheckpoint;
                + "sumND " + countSd + "\n" //Summe ND
                + "sumProc " + countProc + "\n" //Summe Proz.
                + "csStatusEn " + csStatusEn + "\n";
    }

    @Override
    public String getHospitalIdent() {
        return getCsHospitalIdent();
    }

    @Override
    public String getCaseNumber() {
        return getCsCaseNumber();
    }

    /**
     * @return the csdAdmissionDateWeekday
     */
    public Integer getCsdAdmissionDateWeekday() {
        return csdAdmissionDateWeekday;
    }

    /**
     * @param csdAdmissionDateWeekday the csdAdmissionDateWeekday to set
     */
    public void setCsdAdmissionDateWeekday(final Integer csdAdmissionDateWeekday) {
        this.csdAdmissionDateWeekday = csdAdmissionDateWeekday;
    }

    /**
     * @return the csdDischargeDateWeekday
     */
    public Integer getCsdDischargeDateWeekday() {
        return csdDischargeDateWeekday;
    }

    /**
     * @param csdDischargeDateWeekday the csdDischargeDateWeekday to set
     */
    public void setCsdDischargeDateWeekday(Integer csdDischargeDateWeekday) {
        this.csdDischargeDateWeekday = csdDischargeDateWeekday;
    }

    /**
     * @return the cwPositiv
     */
    public Double getCwPositive() {
        return cwPositive;
    }

    /**
     * @param cwPositiv the cwPositiv to set
     */
    public void setCwPositive(Double cwPositiv) {
        this.cwPositive = cwPositiv;
    }

    /**
     * @return the cwNegative
     */
    public Double getCwNegative() {
        return cwNegative;
    }

    /**
     * @param cwNegative the cwNegative to set
     */
    public void setCwNegative(Double cwNegative) {
        this.cwNegative = cwNegative;
    }

    /**
     * @return the cwEffective
     */
    public Double getCwEffective() {
        return cwEffective;
    }

    /**
     * @param cwEffective the cwEffective to set
     */
    public void setCwEffective(Double cwEffective) {
        this.cwEffective = cwEffective;
    }

    /**
     * @return the cwCatalog
     */
    public Double getCwCatalog() {
        return cwCatalog;
    }

    /**
     * @param cwCatalog the cwCatalog to set
     */
    public void setCwCatalog(Double cwCatalog) {
        this.cwCatalog = cwCatalog;
    }

    /**
     * @return the csDrg
     */
    public String getCsDrg() {
        return csDrg;
    }

    /**
     * @param csDrg the csDrg to set
     */
    public void setCsDrg(String csDrg) {
        this.csDrg = csDrg;
    }

    /**
     * @return the grpresGroup
     */
    public String getGrpresGroup() {
        return grpresGroup;
    }

    /**
     * @param grpresGroup the grpresGroup to set
     */
    public void setGrpresGroup(String grpresGroup) {
        this.grpresGroup = grpresGroup;
    }

    /**
     * @return the csdId
     */
    public Long getCsdId() {
        return csdId;
    }

    /**
     * @param csdId the csdId to set
     */
    public void setCsdId(final Long csdId) {
        this.csdId = csdId;
    }

    /**
     * @return the dCWmin
     */
    public Double getDCWmin() {
        return dCWmin;
    }

    /**
     * @param dCWmin the dCWmin to set
     */
    public void setDCWmin(final Double dCWmin) {
        this.dCWmin = dCWmin;
    }

    /**
     * @return the dCWmax
     */
    public Double getDCWmax() {
        return dCWmax;
    }

    /**
     * @param dCWmax the dCWmax to set
     */
    public void setDCWmax(final Double dCWmax) {
        this.dCWmax = dCWmax;
    }

    /**
     *
     * @param isCancel Cancel_Fl to set
     */
    public void setIsCancel(boolean isCancel) {
        this.isCancel = isCancel;
    }

    /**
     *
     * @return the Cancel_Fl
     */
    public boolean isIsCancel() {
        return isCancel;
    }

    /**
     * @return the insInsCompanyName
     */
    public String getInsInsCompanyName() {
        return insInsCompanyName;
    }

    /**
     * @param insInsCompanyName the insInsCompanyName to set
     */
    public void setInsInsCompanyName(String insInsCompanyName) {
        this.insInsCompanyName = insInsCompanyName;
    }

    /**
     *
     * @return caseSupplFees
     */
    public String getCaseSupplFees() {
        return caseSupplFees;
    }

    /**
     *
     * @param caseSupplFees the caseSupplFees to set
     */
    public void setCaseSupplFees(String caseSupplFees) {
        this.caseSupplFees = caseSupplFees;
    }

    /**
     * @return the csdVersionNumber
     */
    public Long getCsdVersionNumber() {
        return csdVersionNumber;
    }

    /**
     * @param csdVersionNumber the csdVersionNumber to set
     */
    public void setCsdVersionNumber(Long csdVersionNumber) {
        this.csdVersionNumber = csdVersionNumber;
    }
    
    public Double getDCareCWmin() {
        return dCareCWmin;
    }

    public void setDCareCWmin(Double dCareCWmin) {
        this.dCareCWmin = dCareCWmin;
    }

    public Double getDCareCWmax() {
        return dCareCWmax;
    }

    public void setDCareCWmax(Double dCareCWmax) {
        this.dCareCWmax = dCareCWmax;
    }
    
    public Double getDRevenueMin() {
        return dRevenueMin;
    }

    public void setDRevenueMin(Double dRevenueMin) {
        this.dRevenueMin = dRevenueMin;
    }

    public Double getDRevenueMax() {
        return dRevenueMax;
    }

    public void setDRevenueMax(Double dRevenueMax) {
        this.dRevenueMax = dRevenueMax;
    }
    
    public Double getCareCw() {
        return careCw;
    }

    public void setCareCw(Double careCw) {
        this.careCw = careCw;
    }
    
    
//    public Map<String, String> createWorkingListAtrrHashMap() {
//        final Map<String, String> h = new HashMap<>();
//
//        addParamValues(h, "csdId", csdId);
//        addParamValues(h, "csCaseNumber", csCaseNumber);
//        addParamValues(h, "csCreationDate", csCreationDate);
//        addParamValues(h, "csdAdmissionDate", csdAdmissionDate);
//        addParamValues(h, "csdAdmissionDateWeekday", csdAdmissionDateWeekday);
//        addParamValues(h, "csdAdmReason12En", csdAdmReason12En);
//        addParamValues(h, "csdAdmReason34En", csdAdmReason34En);
//        addParamValues(h, "csdAdmCauseEn", csdAdmCauseEn);
//        addParamValues(h, "csdDischargeDate", csdDischargeDate);
//        addParamValues(h, "csdDischargeDateWeekday", csdDischargeDateWeekday);
//        addParamValues(h, "csdDisReason12En", csdDisReason12En);
//        addParamValues(h, "csCaseTypeEn", csCaseTypeEn);
//        addParamValues(h, "csHospitalIdent", csHospitalIdent);
//        addParamValues(h, "grpresGroup", grpresGroup);
//        addParamValues(h, "csdLeave", csdLeave);
//        addParamValues(h, "csdLos", csdLos);
//        addParamValues(h, "csdLosAlteration", csdLosAlteration);
//        addParamValues(h, "patNumber", patNumber);
//        addParamValues(h, "csdAgeYears", csdAgeYears);
//        addParamValues(h, "csdAgeDays", csdAgeDays);
//        addParamValues(h, "patGenderEn", patGenderEn);
//        addParamValues(h, "csdAdmissionWeight", csdAdmissionWeight);
//        addParamValues(h, "csdHmv", csdHmv);
//        addParamValues(h, "patdZipcode", patdZipcode);
//        addParamValues(h, "insInsCompany", insInsCompany);
//        addParamValues(h, "insInsCompanyName", insInsCompanyName);
//        addParamValues(h, "icdCode", icdCode);
//        addParamValues(h, "countSd", countSd);
//        addParamValues(h, "countProc", countProc);
//        addParamValues(h, "csStatusEn", csStatusEn);
//        addParamValues(h, "string1", string1);
//        addParamValues(h, "string2", string2);
//        addParamValues(h, "string3", string3);
//        addParamValues(h, "string4", string4);
//        addParamValues(h, "string5", string5);
//        addParamValues(h, "string6", string6);
//        addParamValues(h, "string7", string7);
//        addParamValues(h, "string8", string8);
//        addParamValues(h, "string9", string9);
//        addParamValues(h, "string10", string10);
//        addParamValues(h, "numeric1", numeric1);
//        addParamValues(h, "numeric2", numeric2);
//        addParamValues(h, "numeric3", numeric3);
//        addParamValues(h, "numeric4", numeric4);
//        addParamValues(h, "numeric5", numeric5);
//        addParamValues(h, "numeric6", numeric6);
//        addParamValues(h, "numeric7", numeric7);
//        addParamValues(h, "numeric8", numeric8);
//        addParamValues(h, "numeric9", numeric9);
//        addParamValues(h, "numeric10", numeric10);
//        addParamValues(h, "csdDouble1", csdDouble1);
//        addParamValues(h, "csdDouble2", csdDouble2);
//        addParamValues(h, "csdDouble3", csdDouble3);
//        addParamValues(h, "csdDouble4", csdDouble4);
//        addParamValues(h, "csdDouble5", csdDouble5);
//        addParamValues(h, "csdDate1", csdDate1);
//        addParamValues(h, "csdDate2", csdDate2);
//        addParamValues(h, "csdDate3", csdDate3);
//        addParamValues(h, "csdDate4", csdDate4);
//        addParamValues(h, "csdDate5", csdDate5);
//        addParamValues(h, "cwPositive", cwPositive);
//        addParamValues(h, "cwNegative", cwNegative);
//        addParamValues(h, "cwEffective", cwEffective);
//        addParamValues(h, "cwCatalog", cwCatalog);
//        addParamValues(h, "csDrg", csDrg);
//        addParamValues(h, "caseFees", caseFees);
//        addParamValues(h, "caseSupplFees", caseSupplFees);
//        addParamValues(h, "dCWmin", dCWmin);
//        addParamValues(h, "dCWmax", dCWmax);
//        addParamValues(h, "rule", rule);
//        addParamValues(h, "isCancel", isCancel);
//        addParamValues(h, "csdVersionNumber", csdVersionNumber);
//
//        addParamValues(h, "patDateOfBirth", getPatDateOfBirth());
//        addParamValues(h, "patName", getPatName());
//        addParamValues(h, "insNumber", getInsNumber());
//        addParamValues(h, "lock", getLock());
//        addParamValues(h, "rowNum", getRowNum());
//
//        return h;
//    }

    public Date getCsBillingDate() {
        return csBillingDate == null ? null : new Date(csBillingDate.getTime());

    }

    public void setCsBillingDate(Date csBillingDate) {
        this.csBillingDate = csBillingDate == null ? null : new Date(csBillingDate.getTime());
    }
}
