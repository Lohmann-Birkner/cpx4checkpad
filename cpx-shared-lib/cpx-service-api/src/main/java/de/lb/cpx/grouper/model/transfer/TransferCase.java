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
 *    2016  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.grouper.model.transfer;

import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.CountryEn;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author gerschmann
 */
public class TransferCase implements Serializable, Comparable<TransferCase> {

    private static final long serialVersionUID = 1L;

    private String m_pathToRule = "";
    private String m_pathToCatalog = "";
    private String m_ikz;
    //private double m_baseRate;
    private String m_kasse;
    private String m_caseNumber;
    private int m_ageInYears;
    private int m_ageInDays;
    private int m_sex;
    private Date m_dateOfBirth;
    private String m_city;
    private String m_zipCode;
    private int m_insuranceState; // versichertenstatus
    private Date m_dateOfAdmission;
    private int m_admissionCause;
    private int m_admissionReason12;
    private int m_admissionReason34;
    private double m_weight;
    private Date m_dateOfDischarge;
    private int m_intensiveDepStay;
    private int m_nalos;
    private int m_leaveOfAbsence;
    private int m_departmentType;
    private int m_respirationLenght;
    private int m_involuntary;
    private int m_dischargeReason12;
    private int m_dischargeReason3;
    private boolean m_isTransfer;
    private final List<TransferFee> m_fees = new ArrayList<>();
    private List<TransferBaseRate> m_baserates4case = new ArrayList<>();
//    private double m_feeSum;
    private String m_admissionSpeciality;
    private final List<TransferDepartment> m_departments = new ArrayList<>();
    private String m_treatmenSpeciality;
    private String m_dischargeSpeciality;
    private boolean m_doGroupeForCase;
    private boolean m_DoGroup;

    private int m_modelId = 0;//GrouperInterfaceBasic.AUTOMATIC;
    private boolean m_isAuto = true;
    private int m_caseType;
    private String m_doctorIdent;
    private long m_losAlteration;
    private int m_losMdAlteration;
    private boolean m_isLocal = true;
    private int m_lengthOfStay;
    private ArrayList<TransferIcd> m_admissionIcd = null;
    private final Map<Long, TransferOps> m_ops2id = new HashMap<>();
    private final Map<Long, TransferIcd> m_icd2id = new HashMap<>();
    private TransferIcd m_principalIcd = null;
//    private List<String> m_wards = new ArrayList<>();
    private TransferGroupResult m_groupResult2principalNull;
// grouper options
    private boolean m_doSimulate = false; // default option for single grouping
    private boolean m_doSupplementaryFees = true;// default option for single grouping
    private boolean m_doSimulateRules = true; // default option for single grouping
    private boolean m_doMedAndRemedies = false; // use Medicines and Remidies
    private boolean m_doCareData = false; // use CareData
    private boolean m_doLabor = false;
    private boolean m_doDepartmentGrouping = false;
    private boolean m_doStationsgrouping = false;
    private boolean m_doRules = false;
    private int m_aux9count = 0;
    private long m_caseId = 0;
    private long m_caseDetailsId = 0;
    private ArrayList<Long> m_roleIds = null;
    private ArrayList<TransferLabor> m_labor = null;
    private final Map<Integer, String> m_csStrings = new HashMap<>();
    private final Map<Integer, Integer> m_csNumerics = new HashMap<>();
    private boolean hasTransferFlagByMerge = false;
    //private final CountryEn m_country = null;
    public TransferCase() {

    }

    public long getCaseId() {
        return m_caseId;
    }

    public void setCaseId(long caseId) {
        this.m_caseId = caseId;
    }

    public long getCaseDetailsId() {
        return m_caseDetailsId;
    }

    public void setCaseDetailsId(long caseDetailsId) {
        this.m_caseDetailsId = caseDetailsId;
    }

    public String getPathToRule() {
        return m_pathToRule;
    }

    public String getPathToCatalog() {
        return m_pathToCatalog;
    }

    public String getIkz() {
        return m_ikz;
    }

    public List<TransferBaseRate> getBaseRate4case() {
// ermittlung der default baserates is in die baserateDao übertaragen worden        
//        if (m_baserates4case.isEmpty()) {
//            double value = 3000.0;
//            if (m_caseType == CsCaseType.PEPP.getId()) {
//                value = 250.0;
//            }
//            TransferBaseRate br = new TransferBaseRate(value, this.m_dateOfAdmission, this.m_dateOfDischarge);
//            m_baserates4case.add(br);
//        }
//        return new ArrayList<>(m_baserates4case);
        return m_baserates4case;
    }

    public String getKasse() {
        return m_kasse;
    }

    public String getCaseNumber() {
        return m_caseNumber;
    }

    public int getAgeInYears() {
        return m_ageInYears;
    }

    public int getAgeInDays() {
        return m_ageInDays;
    }

    public int getSex() {
        return m_sex;
    }

    public Date getDateOfBirth() {
        return m_dateOfBirth == null ? null : new Date(m_dateOfBirth.getTime());
    }

    public String getCity() {
        return m_city;
    }

    public String getZipCode() {
        return m_zipCode;
    }

    public Date getDateOfAdmission() {
        return m_dateOfAdmission == null ? null : new Date(m_dateOfAdmission.getTime());
    }

    public int getAdmissionCause() {
        return m_admissionCause;
    }

    public int getAdmissionReason12() {
        return m_admissionReason12;
    }

    public int getAdmissionReason34() {
        return m_admissionReason34;
    }

    public double getWeight() {
        return m_weight;
    }

    public Date getDateOfDischarge() {
        return m_dateOfDischarge == null ? null : new Date(m_dateOfDischarge.getTime());
    }

    public int getIntensiveDepStay() {
        return m_intensiveDepStay;
    }

    public int getNalos() {
        return m_nalos;
    }

    public int getLeaveOfAbsence() {
        return m_leaveOfAbsence;
    }

    public int getDepartmentType() {
        return m_departmentType;
    }

    public int getRespirationLenght() {
        return m_respirationLenght;
    }

    public int getInvoluntary() {
        return m_involuntary;
    }

    public int getDischargeReason12() {
        return m_dischargeReason12;
    }

    public int getDischargeReason3() {
        return m_dischargeReason3;
    }

    public boolean isTransfer() {
        return m_isTransfer;
    }

    public List<TransferFee> getFees() {
        return new ArrayList<>(m_fees);
    }

    /*    public double getFeeSum() {
        return m_feeSum;
    }
     */
    public String getAdmissionSpeciality() {
        return m_admissionSpeciality;
    }

    public List<TransferDepartment> getDepartments() {
        return new ArrayList<>(m_departments);
    }

    public void addDepartment(final TransferDepartment pDep) {
        m_departments.add(pDep);
    }

    public String getTreatmenSpeciality() {
        return m_treatmenSpeciality;
    }

    public String getDischargeSpeciality() {
        return m_dischargeSpeciality;
    }

    public boolean isDoGroupForCase() {
        return m_doGroupeForCase;
    }

    public void setPathToRule(String pathToRule) {
        m_pathToRule = pathToRule;
    }

    public void setPathToCatalog(String pathToCatalog) {
        m_pathToCatalog = pathToCatalog;
    }

    public void setIkz(String ikz) {
        m_ikz = ikz;
    }

    public void setBaseRate(List<TransferBaseRate> baseRates) {
        m_baserates4case = baseRates == null ? null : new ArrayList<>(baseRates);
    }

    public void setKasse(String kasse) {
        m_kasse = kasse;
    }

    public void setCaseNumber(String caseNumber) {
        m_caseNumber = caseNumber;
    }

    public void setAgeY(int ageInYears) {
        m_ageInYears = ageInYears;
    }

    public void setAgeD(int ageInDays) {
        m_ageInDays = ageInDays;
    }

    public void setSex(int sex) {
        m_sex = sex;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        m_dateOfBirth = dateOfBirth == null ? null : new Date(dateOfBirth.getTime());
    }

    public void setCity(String city) {
        m_city = city;
    }

    public void setZipCode(String zipCode) {
        m_zipCode = zipCode;
    }

    public void setAdmissionDate(Date dateOfAdmission) {
        m_dateOfAdmission = dateOfAdmission == null ? null : new Date(dateOfAdmission.getTime());
    }

    public void setAdmissionCause(int admissionCause) {
        m_admissionCause = admissionCause;
    }

    public void setAdmissionReason12(int admissionReason12) {
        m_admissionReason12 = admissionReason12;
    }

    public void setAdmissionReason34(int admissionReason34) {
        m_admissionReason34 = admissionReason34;
    }

    public void setWeight(double weight) {
        m_weight = weight;
    }

    public void setDischargeDate(Date dateOfDischarge) {
        m_dateOfDischarge = dateOfDischarge == null ? null : new Date(dateOfDischarge.getTime());
    }

    public void setIntensiveDepStay(int intensiveDepStay) {
        m_intensiveDepStay = intensiveDepStay;
    }

    public void setNALOS(int nalos) {
        m_nalos = nalos;
    }

    public void setLeaveOfAbsence(int leaveOfAbsence) {
        m_leaveOfAbsence = leaveOfAbsence;
    }

    public void setDepartmentType(int departmentType) {
        m_departmentType = departmentType;
    }

    public void setRespirationLength(int respirationLenght) {
        m_respirationLenght = respirationLenght;
    }

    public void setInvoluntary(int involuntary) {
        m_involuntary = involuntary;
    }

    public void setDiscargeReason12(int dischargeReason12) {
        m_dischargeReason12 = dischargeReason12;
    }

    public void setDiscargeReason3(int dischargeReason3) {
        m_dischargeReason3 = dischargeReason3;
    }

    public void setIsTransfer(boolean isTransfer) {
        m_isTransfer = isTransfer;
    }

    public void addFeeRecord(String feeType, double value, Date calcFrom, Date calcTo, int count, int daysNotConsidered) {
        m_fees.add(new TransferFee(feeType, value, calcFrom, calcTo, count, daysNotConsidered));
    }

    /*
    public void setFeeSum(double feeSum) {
        m_feeSum = feeSum;
    }
     */
    public void setAdmissionSpeciality(String admissionSpeciality) {
        m_admissionSpeciality = admissionSpeciality;
    }

    public void addDepartment(String department, Date transferDate, Date adm, boolean isAdm, boolean isDis, boolean isTreat) {
        m_departments.add(new TransferDepartment(department, transferDate, adm, isAdm, isDis, isTreat));
    }

    public void setTreatmenSpeciality(String treatmenSpeciality) {
        m_treatmenSpeciality = treatmenSpeciality;
    }

    public void setDischargeSpeciality(String dischargeSpeciality) {
        m_dischargeSpeciality = dischargeSpeciality;
    }

    public void setDoGroupForCase(boolean doGroupForCase) {
        m_doGroupeForCase = doGroupForCase;
    }

    public boolean isDoGroup() {
        return m_DoGroup;
    }

    public void setDoGroup(boolean isDoGroup) {
        this.m_DoGroup = isDoGroup;
    }

    public void setGrouperModelId(int modelid) {
        m_modelId = modelid;
        this.setIsAuto(m_modelId == 0);
    }

    public int getGrouperModelId() {
        return m_modelId;
    }

    public void setCaseType(int type) {
        m_caseType = type;
    }

    public int getCaseType() {
        return m_caseType;
    }

    public void setDoctorIdent(String csDoctorIdent) {
        m_doctorIdent = csDoctorIdent;
    }

    public String getDoctorIdent() {
        return m_doctorIdent;
    }

    public void setLosAlteration(long csdLosAlteration) {
        m_losAlteration = csdLosAlteration;
    }

    public long getLosAlteration() {
        return m_losAlteration;
    }

    /**
     * flag der definiert, ob das Groupen fuer lokale oder KIS Daten
     * durchgefuehrt wird
     *
     * @param local locale or external
     *
     */
    public void setIsLocal(boolean local) {
        m_isLocal = local;
    }

    public boolean getIsLocal() {
        return m_isLocal;
    }

    public void setIsAuto(boolean isAuto) {
        m_isAuto = isAuto;
    }

    public boolean getIsAuto() {
        return this.m_isAuto;
    }

    public int getLengthOfStay() {
        return m_lengthOfStay;
    }

    public void setLengthOfStay(int m_lengthOfStay) {
        this.m_lengthOfStay = m_lengthOfStay;
    }

    public void addAdmissionDiagnose(long id, String icdcCode, int refType, int loc) {
        if (m_admissionIcd == null) {
            m_admissionIcd = new ArrayList<>();
        }
        m_admissionIcd.add(new TransferIcd(id, icdcCode, refType, loc, false, true));
    }

    public List<TransferIcd> getAdmissionDiagnose() {
        return m_admissionIcd;
    }

    public void addOps(TransferOps tOps) {
        m_ops2id.put(tOps.getId(), tOps);

    }

    public void addIcd(TransferIcd tIcd) {
        m_icd2id.put(tIcd.getId(), tIcd);
        checkAux9(tIcd.getCode());

    }

    public TransferIcd getIcd2id(long id) {
        return m_icd2id.get(id);
    }

    public Map<Long, TransferIcd> getIcd2id() {
        return new HashMap<>(m_icd2id);
    }

    public TransferOps getOps2Id(long id) {
        return m_ops2id.get(id);
    }

    public void setPrincipalIcd(TransferIcd tIcd) {
        m_principalIcd = tIcd;
    }

    public TransferIcd getPrincipalIcd() {
        return m_principalIcd;
    }

    public void setSupplementaryFee2Ops(int procId, TransferSupplementaryFee addFee) {

        if (m_principalIcd != null) {
            m_principalIcd.setSupplementartyFee2OpsResult(procId, addFee);
        } else {
            if (m_groupResult2principalNull != null) {
                m_groupResult2principalNull.setSupplementaryFee((long) procId, addFee);
            }
        }
    }

    public boolean doSimulate() {
        return m_doSimulate;
    }

    /**
     * flag, which will be set, when the drgs/pepps for all case icds is on/off
     *
     * @param doSimulate simulate
     */
    public void doSimulate(boolean doSimulate) {
        this.m_doSimulate = doSimulate;
    }

    public boolean doSupplementaryFees() {
        return m_doSupplementaryFees;
    }

    /**
     * flag, which will be set, when the calculation of supplementary fees is
     * on/off
     *
     * @param doSupplementaryFees supplementary fees
     */
    public void doSupplementaryFees(boolean doSupplementaryFees) {
        this.m_doSupplementaryFees = doSupplementaryFees;
    }

    public boolean doSimulateRules() {
        return m_doSimulateRules;
    }

    public void doSimulateRules(boolean doSimulateRules) {
        this.m_doSimulateRules = doSimulateRules;
    }

    public void setBatchGrouperParameter(BatchGroupParameter batchGroupParameter) {
        if (batchGroupParameter != null) {
            m_doSimulate = batchGroupParameter.isDoSimulate();// simulate DRG/PEPP
            m_doSupplementaryFees = batchGroupParameter.isDoSupplementaryFees(); // supplementary Fees
            m_doMedAndRemedies = batchGroupParameter.isDoMedAndRemedies(); // use Medicines and Remidies
            m_doCareData = batchGroupParameter.isDoCareData(); // use CareData
            m_doLabor = batchGroupParameter.isDoLabor();
            m_doDepartmentGrouping = batchGroupParameter.isDoDepartmentGrouping();
            m_doStationsgrouping = batchGroupParameter.isDoStationsgrouping();
            m_doRules = batchGroupParameter.isDoRules();
            m_doSimulateRules = batchGroupParameter.isDoRulesSimulate();
            m_modelId = batchGroupParameter.getModelId();

            List<Long> roleIds = batchGroupParameter.getRoleIds();
            m_roleIds = roleIds == null ? null : new ArrayList<>(roleIds); // default null use rules for all roles
        }
    }

    public List<Long> getRoleIds() {
        return m_roleIds == null ? null : new ArrayList<>(m_roleIds);
    }

    public long[] getRoleIdsArray() {
        if (m_roleIds != null) {
            int len = m_roleIds.size();
            long[] arr = new long[len];
            for (int i = 0; i < len; i++) {
                arr[i] = m_roleIds.get(i);
            }
            return arr;
        }
        return null; //2017-12-01 DNi: Return new Long[0] instead?!
    }

    public void setRoleIds(List<Long> roleIds) {
        this.m_roleIds = roleIds == null ? null : new ArrayList<>(roleIds);
    }

    public void addRoleId(Long roleId) {
        if (m_roleIds == null) {
            m_roleIds = new ArrayList<>();
        }
        m_roleIds.add(roleId);
    }

    public TransferGroupResult getGroupResult2principalNull() {
        return m_groupResult2principalNull;
    }

    public void setGroupResult2principalNull(TransferGroupResult m_groupResult2principalNull) {
        this.m_groupResult2principalNull = m_groupResult2principalNull;
    }

    /**
     * icd code ends with 9(nonspeciffic code)
     *
     * @param cd
     */
    private void checkAux9(String cd) {
        if (cd == null) {
            return;
        }
        if (cd.indexOf('.') >= 1 && cd.endsWith("9")) {
            m_aux9count++;
        }
        /*
        Code in Checkpoint. Is it faster?
    len = cd.length();
    if(len == 5 && cd.charAt(3) == '.' && cd.charAt(4) == '9') {
            m_result.diag9++;
    } else if(len == 6 && cd.charAt(3) == '.' && cd.charAt(5) == '9') {
            m_result.diag9++;
    }
        
         */
    }

    public int getAux9count() {
        return m_aux9count;
    }

    public String getCountry() {

        //String country = m_country == null ? CountryEn.de.name() : m_country.name();
        //return country;
        return CountryEn.de.name();
//        if(m_country != null){
//            return m_country.name();
//        }else{
//            return CountryEn.de.name();
//        }
    }

    public boolean isDrg() {
        return m_caseType == CaseTypeEn.DRG.getId();
    }

    public boolean isPepp() {
        return m_caseType == CaseTypeEn.PEPP.getId();
    }

    public boolean isPia() {
        return m_caseType == CaseTypeEn.PIA.getId();
    }

    public void addLabor(TransferLabor transferLabor) {
        getLabor().add(transferLabor);
    }

    public ArrayList<TransferLabor> getLabor() {
        m_labor = (m_labor == null ? new ArrayList<>() : m_labor);
        return m_labor;
    }

    public boolean isDoLabor() {
        return m_doLabor;
    }

    public void setDoLabor(boolean m_doLabor) {
        this.m_doLabor = m_doLabor;
    }

    public void setCsStringValue(int pIndex, String pString) {
        m_csStrings.put(pIndex, pString);
    }

    public void setCsNumericValue(int pIndex, int pNumeric) {
        m_csNumerics.put(pIndex, pNumeric);
    }

    public Map<Integer, String> getCsStringValues() {
        return m_csStrings;
    }

    public Map<Integer, Integer> getCsNumericValues() {
        return m_csNumerics;
    }

    public void setKisInterfaceValues(int pIndex, String csString, Integer csNumeric) {
        if (csString != null) {
            setCsStringValue(pIndex, csString);
        }
        if (csNumeric != null) {
            setCsNumericValue(pIndex, csNumeric);
        }

    }

    public int getInsuranceState() {
        return m_insuranceState;
    }

    public void setInsuranceState(int pInsuranceState) {
        m_insuranceState = pInsuranceState;
    }

    public int getAdmissionYear() {
        if (this.m_dateOfAdmission != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(m_dateOfAdmission);
            return cal.get(Calendar.YEAR);
        } else {
            return 0;
        }
    }

    public boolean isHasTransferFlagByMerge() {
        return hasTransferFlagByMerge;
    }

    public void setHasTransferFlagByMerge(boolean hasTransferFlagByMerge) {
        this.hasTransferFlagByMerge = hasTransferFlagByMerge;
    }

    public void resetAllFlags() {
        m_doSimulate = false; // default option for single grouping
         m_doSupplementaryFees = false;// default option for single grouping
         m_doSimulateRules = false; // default option for single grouping
         m_doMedAndRemedies = false; // use Medicines and Remidies
         m_doCareData = false; // use CareData
         m_doLabor = false;
         m_doDepartmentGrouping = false;
         m_doStationsgrouping = false;
         m_doRules = false;
    }

    public int getLosMdAlteration() {
        return m_losMdAlteration;
    }

    public void setLosMdAlteration(int pLosMdAlteration) {
        this.m_losMdAlteration = pLosMdAlteration;
    }

    @Override
    public int compareTo(TransferCase o) {
        if(getCaseNumber().equals(o.getCaseNumber())){
            return getIkz().compareTo(o.getIkz());
        }
        return getCaseNumber().compareTo(o.getCaseNumber()) ;
    }
   

}
