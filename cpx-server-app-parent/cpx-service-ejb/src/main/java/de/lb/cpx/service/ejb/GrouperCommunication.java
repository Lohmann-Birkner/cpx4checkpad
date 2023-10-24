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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.grouper.model.dto.GrouperResponseObject;
import de.lb.cpx.grouper.model.transfer.BatchGroupResult;
import de.lb.cpx.grouper.model.transfer.TransferCase;
import de.lb.cpx.grouper.model.transfer.TransferDepartment;
import de.lb.cpx.grouper.model.transfer.TransferDrg;
import de.lb.cpx.grouper.model.transfer.TransferDrgCare; 
import de.lb.cpx.grouper.model.transfer.TransferGroupResult;
import de.lb.cpx.grouper.model.transfer.TransferIcd;
import de.lb.cpx.grouper.model.transfer.TransferIcdResult;
import de.lb.cpx.grouper.model.transfer.TransferLabor;
import de.lb.cpx.grouper.model.transfer.TransferOps;
import de.lb.cpx.grouper.model.transfer.TransferOpsResult;
import de.lb.cpx.grouper.model.transfer.TransferPepp;
import de.lb.cpx.grouper.model.transfer.TransferPeppGrade;
import de.lb.cpx.grouper.model.transfer.TransferRule;
import de.lb.cpx.grouper.model.transfer.TransferSupplementaryFee;
import de.lb.cpx.model.TBatchCheckResult;
import de.lb.cpx.model.TBatchResult;
import de.lb.cpx.model.TBatchResult2Role;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseDrg;
import de.lb.cpx.model.TCaseDrgCareGrades;
import de.lb.cpx.model.TCaseFee;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseIcdGrouped;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.TCaseOpsGrouped;
import de.lb.cpx.model.TCasePepp;
import de.lb.cpx.model.TCasePeppGrades;
import de.lb.cpx.model.TCaseSupplFee;
import de.lb.cpx.model.TCaseWard;
import de.lb.cpx.model.TCheckResult;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.TInsurance;
import de.lb.cpx.model.TLab;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.model.TPatientDetails;
import de.lb.cpx.model.TRole2Check;
import de.lb.cpx.model.TRole2Result;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.DrgCorrTypeEn;
import de.lb.cpx.model.enums.DrgPartitionEn;
import de.lb.cpx.model.enums.GroupResultPdxEn;
import de.lb.cpx.model.enums.GrouperMdcOrSkEn;
import de.lb.cpx.model.enums.GrouperStatusEn;
import de.lb.cpx.model.enums.IcdcRefTypeEn;
import de.lb.cpx.model.enums.IcdcTypeEn;
import de.lb.cpx.model.enums.PeppPayTypeEn;
import de.lb.cpx.model.enums.SupplFeeTypeEn;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import de.lb.cpx.server.config.CpxServerConfigLocal;
import de.lb.cpx.server.dao.TCaseMergeMappingDao;
import de.lb.cpx.server.dao.TGroupingResultsDao;
import de.lb.cpx.shared.dto.rules.CpxSimpleRisk;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.rules.enums.CaseValidationGroupErrList;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Übertragen der Daten aus Fall - Entities in den TransferCase - Transport
 * Objekt für Grouperanwendung und Resultaten des Groupens in die Fall -
 * Entities
 *
 * @author gerschmann
 */
@Stateless
public class GrouperCommunication {

    private static final Logger LOG = Logger.getLogger(GrouperCommunication.class.getName());

    @EJB
    private TGroupingResultsDao groupingResultsDao;
    
    @EJB
    private TCaseMergeMappingDao caseMergeMappingDao;

    @EJB(name = "CpxServerConfig")
    private CpxServerConfigLocal cpxServerConfig;
//    @EJB
//    private TCaseDetailsDao detailsDao;

//    /**
//     * fills grouper request object with case values
//     *
//     * @param hospitalCase a case to group
//     * @param grouperRequest request - transfer object
//     * @param isLocal a flag which determins whether local or external case
//     * details are to use
//     * @return was successful?
//     */
//    public boolean fillGrouperRequest(TCase hospitalCase, TransferCase grouperRequest, boolean isLocal) {
//        return fillGrouperRequest(hospitalCase, grouperRequest, isLocal, 0);
//    }
    public boolean fillGrouperRequest(TCase hospitalCase, TransferCase grouperRequest, boolean isLocal, int modelId) {
        return fillGrouperRequest(hospitalCase, grouperRequest, isLocal, modelId, null, null);
    }

    /**
     * interface to group the case details currently set to actual, distinguish
     * the details to group by isLocal Flag
     *
     * @param hospitalCase hospitalCase to group
     * @param grouperRequest the request object for the grouper
     * @param isLocal is local indicator to decide which details to group extern
     * or local copy
     * @param modelId grouper model to use for grouping
     * @param icdList Diagnosis
     * @param opsList Procedures
     * @return indicator if process was successful
     */
    public boolean fillGrouperRequest(TCase hospitalCase, TransferCase grouperRequest,
            Boolean isLocal, int modelId, Map<TCaseIcd, Long> icdList, Map<TCaseOps, Long> opsList) {
        //AWi-20170906-Note for Awi:
        //do not replace this methode .. normal grouper use this, also temp grouper .. will result grouper fail
        return fillGrouperRequest(hospitalCase,
                isLocal ? hospitalCase.getCurrentLocal() : hospitalCase.getCurrentExtern(),
                grouperRequest, modelId, icdList, opsList);
    }

    public boolean fillGrouperRequest2(TCase hospitalCase, TransferCase grouperRequest, boolean isLocal, int modelId) {
//        return fillGrouperRequest(hospitalCase, isLocal?hospitalCase.getCurrentLocal():hospitalCase.getCurrentExtern(), grouperRequest, modelId);
        //TCaseDetails details = detailsDao.findCurrentDetails(hospitalCase, isLocal);
        TCaseDetails details = isLocal ? hospitalCase.getCurrentLocal() : hospitalCase.getCurrentExtern();
        if (isLocal) {
            hospitalCase.setLocalCopyOfCurrentLocal(details);
        } else {
            hospitalCase.setLocalCopyOfCurrentExtern(details);
        }
        return fillGrouperRequest(hospitalCase, details, grouperRequest, modelId, null, null);
    }

    public boolean fillGrouperRequest(TCase hospitalCase, TCaseDetails csd, TransferCase grouperRequest, int modelId) {
        return fillGrouperRequest(hospitalCase, csd, grouperRequest, modelId,
                null, null);
    }

    /**
     * added new interface tries to solve issue that only case details set to
     * isActual can be grouped
     *
     * @param hospitalCase hospital to group
     * @param csd details amoung previous hospital case to use to group
     * @param grouperRequest grouper request object for the grouper
     * @param modelId grouper model to use
     * @param icdList saves
     * @param opsList List of procedures
     * @return indicator if process was successful
     */
    public boolean fillGrouperRequest(TCase hospitalCase,
            TCaseDetails csd,
            TransferCase grouperRequest,
            int modelId,
            Map<TCaseIcd, Long> icdList,
            Map<TCaseOps, Long> opsList) {
        List<CaseValidationGroupErrList> errorList = new ArrayList<>();

        errorList = fillGrouperRequest(hospitalCase,
                csd,
                grouperRequest,
                modelId,
                icdList,
                opsList,
                errorList);
        return !CaseValidationGroupErrList.hasSevere(errorList);
    }

    public List<CaseValidationGroupErrList> fillGrouperRequest(TCase hospitalCase,
            TCaseDetails csd,
            TransferCase grouperRequest,
            int modelId,
            Map<TCaseIcd, Long> icdList,
            Map<TCaseOps, Long> opsList, List<CaseValidationGroupErrList> errorList) {

//        try{
//            TransferCaseHelper.fillTransferCaseForGrouper(hospitalCase, csd, grouperRequest, modelId, icdList, opsList);
//        }catch(IllegalArgumentException ex1){
//            LOG.warning(ex1.getMessage());
//            return false;
//        }catch(NullPointerException ex2){
//            LOG.severe(ex2.getMessage());
//            return false;
//        }
//        return true;
        //Fehlt
//pflegestatus
//entgeltgruppe
// medikamente, heil- und hilfsmittel
//vwd intensiv
//verlegendes Krankenhaus
//fall.vorschlag flag
//        long startTotal = System.currentTimeMillis();
//        long start = System.currentTimeMillis();
        //2017-11-24 DNi: Make some checks to prevent explosions in grouper component
        final String caseKey = (hospitalCase == null ? "NULL" : hospitalCase.getCaseKey());
        final String caseId = (hospitalCase == null ? "NULL" : String.valueOf(hospitalCase.getId()));
        final String csdId = (csd == null ? "NULL" : String.valueOf(csd.getId()));
        final String caseInfo = caseKey + " (case id " + caseId + ", details id " + csdId + ")";

        if (hospitalCase == null) {
            LOG.log(Level.WARNING, "Hospital case is null!");
            errorList.add(CaseValidationGroupErrList.NO_HOSPITAL_CASE);
            return errorList;
        }
        if (hospitalCase.getCsCaseTypeEn() == null || !CaseTypeEn.DRG.equals(hospitalCase.getCsCaseTypeEn())
                && !CaseTypeEn.PEPP.equals(hospitalCase.getCsCaseTypeEn()) //                && !CaseTypeEn.PIA.equals(hospitalCase.getCsCaseTypeEn())
                ) {
            LOG.log(Level.WARNING, "Case type is ''{0}''. This is neither ''{1}'' nor ''{2}'' , so I cannot group this case: {3}", new Object[]{hospitalCase.getCsCaseTypeEn(), CaseTypeEn.DRG, CaseTypeEn.PEPP, caseInfo});
            errorList.add(CaseValidationGroupErrList.NOT_DEFINED_CASE_TYPE);

        }
// CASE_DETAILS        
//        TCaseDetails csd = isLocal?hospitalCase.getCurrentLocal():hospitalCase.getCurrentExtern();
        if (csd == null) {
            LOG.log(Level.WARNING, "No case details found, so I cannot group this case: {0}", caseInfo);
            errorList.add(CaseValidationGroupErrList.NO_CASE_DETAILS_FOUND);
            return errorList;
        }
//        if (csd != null) {
        if (csd.getCsdAdmissionDate() == null) {
            LOG.log(Level.WARNING, "No admission date found, so I cannot group this case: {0}", caseInfo);
            errorList.add(CaseValidationGroupErrList.NO_ADMISSION_DATE_FOUND);

        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(csd.getCsdAdmissionDate());
            if (cal.get(Calendar.YEAR) < GDRGModel.getMinModelYear() && modelId == 0) {
                LOG.log(Level.WARNING, "Admission date is older than 2013, so I cannot group this case: {0}", caseInfo);
                errorList.add(CaseValidationGroupErrList.ADMISSION_DATE_FOR_2013);
            }

        }
        if (csd.getCsdDischargeDate() == null) {
            LOG.log(Level.WARNING, "No discharge date found, so I cannot group this case: {0}", caseInfo);
            errorList.add(CaseValidationGroupErrList.NO_DISCHARGE_DATE_FOUND);
        } else {
            if (csd.getCsdAdmissionDate() != null && csd.getCsdDischargeDate().before(csd.getCsdAdmissionDate())) {
                LOG.log(Level.WARNING, "Discharge date is set for admission date: {0}", caseInfo);
                errorList.add(CaseValidationGroupErrList.DISCHARGE_DATE_BEFORE_ADMISSION_DATE);
            }
        }
        if (csd.getCsdDisReason12En() == null) {
            LOG.log(Level.WARNING, "No discharge reason 12 found, so I cannot group this case properly: {0}", caseInfo);
            errorList.add(CaseValidationGroupErrList.NO_DISCHARGE_REASON_FOUND);
        }
        if (csd.getCsdDisReason3En() == null) {
            LOG.log(Level.WARNING, "No discharge reason 3 found, so I cannot group this case properly: {0}", caseInfo);
            errorList.add(CaseValidationGroupErrList.NO_DISCHARGE_REASON3_FOUND);
        }
        if (csd.getCsdAdmReason12En() == null) {
            LOG.log(Level.WARNING, "No admission reason 12 found, so I cannot group this case properly: {0}", caseInfo);
            errorList.add(CaseValidationGroupErrList.NO_ADMISSION_REASON_FOUND);
        }
        if (csd.getCsdAdmReason34En() == null) {
            LOG.log(Level.WARNING, "No admission reason 34 found, so I cannot group this case properly: {0}", caseInfo);
            errorList.add(CaseValidationGroupErrList.NO_ADMISSION_REASON34_FOUND);
        }
        if (CaseTypeEn.DRG.equals(hospitalCase.getCsCaseTypeEn()) && csd.getCsdAdmodEn() == null) {
            LOG.log(Level.WARNING, "No admission mode found, so I cannot group this case: {0}", caseInfo);
            errorList.add(CaseValidationGroupErrList.NO_ADMISSION_MODE_FOUND);
        }
        if (csd.getCsdAdmCauseEn() == null) {
            LOG.log(Level.WARNING, "No admission cause found, so I cannot group this case properly: {0}", caseInfo);
            errorList.add(CaseValidationGroupErrList.NO_ADMISSION_CAUSE_FOUND);

        }
        if (csd.getCaseDepartments() != null) {
            Iterator<TCaseDepartment> it = csd.getCaseDepartments().iterator();
            while (it.hasNext()) {
                TCaseDepartment dep = it.next();
                if (dep.getDepcAdmDate() == null) {
                    if (!dep.getDepcIsAdmissionFl()) {
                        LOG.log(Level.WARNING, "Admission date of department id {0} is null, so I cannot group this case: {1}", new Object[]{dep.getId(), caseInfo});
                        errorList.add(CaseValidationGroupErrList.NO_DEPARTMENT_ADMISSION_DATE);
                        return errorList;
                    } else {
                        dep.setDepcAdmDate(csd.getCsdAdmissionDate());
                    }
                }
                if (dep.getDepcDisDate() == null && !dep.getDepcIsDischargeFl()) {
                    LOG.log(Level.WARNING, "Discharge date of department id {0} is null, so I cannot group this case: {1}", new Object[]{dep.getId(), caseInfo});
                    errorList.add(CaseValidationGroupErrList.NO_DEPARTMENT_DISCHARGE_DATE);
//                        return errorList;
                }
                if (dep.getDepKey301() == null || dep.getDepKey301().isEmpty()) {
                    LOG.log(Level.WARNING, "Department key {0} is null, so I cannot group this case: {1}", new Object[]{dep.getId(), caseInfo});
                    errorList.add(CaseValidationGroupErrList.NO_DEPARTMENT_DISCHARGE_DATE);
//                        return errorList;
                }
            }
        } else {
            LOG.log(Level.WARNING, " There are no departments for case coded {0}", caseInfo);
            errorList.add(CaseValidationGroupErrList.NO_DEPARTMENT_CODED);
        }
//        }

        LOG.log(Level.FINE, "I will group this case: {0} now...", caseInfo);

        //Falldaten
        grouperRequest.setGrouperModelId(modelId);
        grouperRequest.setIkz(hospitalCase.getCsHospitalIdent() == null ? "" : hospitalCase.getCsHospitalIdent());
        grouperRequest.setCaseNumber(hospitalCase.getCsCaseNumber() == null ? "" : hospitalCase.getCsCaseNumber());
        grouperRequest.setCaseId(hospitalCase.getId());
//isDRGCase        

        grouperRequest.setCaseType(hospitalCase.getCsCaseTypeEn() == null ? 0 : hospitalCase.getCsCaseTypeEn().getId()); //DRG/PEPP und sw.
        grouperRequest.setDoctorIdent(hospitalCase.getCsDoctorIdent());

        TPatient patient = hospitalCase.getPatient();
        grouperRequest.setSex(csd.getCsdGenderEn() == null ? 0 : csd.getCsdGenderEn().getId());
// versichertenstatus
        TInsurance insurance = patient.getCurrentInsurance();
        if (insurance != null && insurance.getInsStatusEn() != null) {
            grouperRequest.setInsuranceState(insurance.getInsStatusEn().getIdInt());
        }
// Patientendaten   
        TPatientDetails patDetails = patient.getPatDetailsActual();
        if (patDetails != null) {
// einzugsgebiet
            if (patDetails.getPatdZipcode() != null && !patDetails.getPatdZipcode().isEmpty()) {
                grouperRequest.setZipCode(patDetails.getPatdZipcode());
            }
//wohnort
            if (patDetails.getPatdCity() != null && !patDetails.getPatdCity().isEmpty()) {
                grouperRequest.setCity(patDetails.getPatdCity().toUpperCase());
            }
        }
        grouperRequest.setIsLocal(csd.getCsdIsLocalFl());
        //2019-01-30 DNi - Ticket #CPX-1378: Don't rely on patient's birth date! 
        //Try to use age in years/days before date of birth is used, because some 
        //interfaces like P21 give use only fuzzy information about the patient.
//AGE 20190802:     
// If the date of birth is defined it will de used by grouper to check the age, because it needs to know the exact age at the admission date 

// and uses the age value only if the date of birth was not set
        if (patient.getPatDateOfBirth() != null //                || ((csd.getCsdAgeYears() == null || csd.getCsdAgeYears() == 0) && (csd.getCsdAgeDays() == null || csd.getCsdAgeDays() == 0)))
                ) {
//            if (csd == null) {
//                LOG.log(Level.WARNING, "Age in years/days is not available because there are no case details, so I use patient birth date to group this case: " + caseInfo);
//            } else {
//                LOG.log(Level.WARNING, "Age in years is " + csd.getCsdAgeYears() + " and age in days is " + csd.getCsdAgeDays() + ", so I use patient birth date to group this case: " + caseInfo);
//            }
            grouperRequest.setDateOfBirth(patient.getPatDateOfBirth());
        }
// Kasse
        //grouperRequest.setKasse(csd.getCsdInsCompany());
//Alter        
        grouperRequest.setAgeD(csd.getCsdAgeDays() == null ? 0 : csd.getCsdAgeDays());
        grouperRequest.setAgeY(csd.getCsdAgeYears() == null ? 0 : csd.getCsdAgeYears());
// Aufnahmedatum        
        grouperRequest.setAdmissionDate(csd.getCsdAdmissionDate());
// Entlassungsdatum        
        grouperRequest.setDischargeDate(csd.getCsdDischargeDate());
//Aufnahmeanlass        
        grouperRequest.setAdmissionCause(csd.getCsdAdmCauseEn() == null ? 0 : csd.getCsdAdmCauseEn().getId());
//Aufnahmegrund12        
        grouperRequest.setAdmissionReason12(csd.getCsdAdmReason12En() == null ? 0 : csd.getCsdAdmReason12En().getIdInt());
//Aufnahmegrund34        
        grouperRequest.setAdmissionReason34(csd.getCsdAdmReason34En() == null ? 0 : csd.getCsdAdmReason34En().getIdInt());
// aufenthalt ausserhalb
        grouperRequest.setNALOS(csd.getCsdLeave() == null ? 0 : csd.getCsdLeave());
// Aufnahmegewicht
        grouperRequest.setWeight(csd.getCsdAdmissionWeight() == null ? 0d : csd.getCsdAdmissionWeight());
// Simulationsänderung an dem VWD
        grouperRequest.setLosAlteration(csd.getCsdLosAlteration() == null ? 0L : csd.getCsdLosAlteration());
// Simulation of change from MD, caes does not change
        grouperRequest.setLosMdAlteration(csd.getCsdLosMdAlteration() == null?0: csd.getCsdLosMdAlteration());
// Erbringungsart

        grouperRequest.setDepartmentType(csd.getCsdAdmodEn() == null ? 0 : csd.getCsdAdmodEn().getId());

//Beatmungsstunden  
        grouperRequest.setRespirationLength(csd.getCsdHmv() == null ? 0 : csd.getCsdHmv());
//Art der Behandlung
        grouperRequest.setInvoluntary(csd.getCsdAdmLawEn() == null ? 0 : csd.getCsdAdmLawEn().getId());
//Entlassungsgrund12
        grouperRequest.setDiscargeReason12(csd.getCsdDisReason12En() == null ? 0 : csd.getCsdDisReason12En().getIdInt());
//Entlassungsgrund3
        grouperRequest.setDiscargeReason3(csd.getCsdDisReason3En() == null ? 0 : csd.getCsdDisReason3En().getIdInt());
//        Logger.getLogger(GrouperCommunication.class.getName()).log(Level.INFO, "set case data " + String.valueOf(System.currentTimeMillis() - start));
//        start = System.currentTimeMillis();
//Abteilungen        
        List<TCaseDepartment> departments = csd.getSortedDepartments();
//        Logger.getLogger(GrouperCommunication.class.getName()).log(Level.INFO, "sort departments " + String.valueOf(System.currentTimeMillis() - start));
//        start = System.currentTimeMillis();

        for (TCaseDepartment dep : departments) {
            Set<TCaseOps> opses = dep.getCaseOpses();// == null?new HashSet<>():dep.getAllOps();
            
            Set<TCaseIcd> icds = dep.getCaseIcds();// == null?new HashSet<>():dep.getAllIcds();
            Set<TCaseWard> wards = dep.getCaseWards();
            TransferDepartment tDep = new TransferDepartment(dep.getDepKey301(), dep.getDepcDisDate(), dep.getDepcAdmDate(),
                    dep.getDepcIsAdmissionFl(), dep.getDepcIsDischargeFl(), dep.getDepcIsTreatingFl());
            grouperRequest.addDepartment(tDep);
// Wards
            if (wards != null && !wards.isEmpty()) {
                for (TCaseWard ward : wards) {
                    tDep.addWard(ward.getWardcIdent());
                }
            }

//Prozeduren            
            for (TCaseOps ops : opses) {
                if (!ops.getOpsIsToGroupFl()) {
                    continue;
                }
// Korrektur des OPS - Datums, wenn OPS zu Abteilung gehoert und kein Datum oder kein Zeitstaempel hat, dann
// OPSDatum = AdmDatum der Abteilung, nur für PEPP Fälle relevant

//                Date opsDate = ops.getOpscDatum();
//                if (hospitalCase.getCsCaseTypeEn() != null && hospitalCase.getCsCaseTypeEn().equals(CaseTypeEn.PEPP)) {
//                    if (opsDate == null || opsDate.before(dep.getDepcAdmDate())) {
//                        opsDate = dep.getDepcAdmDate();
//                    } else if (opsDate != null) {
//                        if (dep.getDepcDisDate() != null && opsDate.after(dep.getDepcDisDate())) {
//                            opsDate = dep.getDepcDisDate();
//                        }
//                    }
//                }

                TransferOps tOps = new TransferOps(opsList == null ? ops.getId() : checkId(opsList, ops), ops.getOpscCode(),
                        ops.getOpscLocEn() == null ? 0 : ops.getOpscLocEn().getId(), ops.getOpscDatum());
                tDep.addOps(tOps);
                grouperRequest.addOps(tOps);
            }
//Diagnosen            
            for (TCaseIcd icd : icds) {
                if (!icd.getIcdIsToGroupFl()) {
                    //AWi 07.04.2016, in Db TCaseIcd have no default Type, it returns null -> grouper explodes
                    if (icd.getIcdcTypeEn() != null) {
                        if (icd.getIcdcTypeEn().equals(IcdcTypeEn.Aufnahme)) {
                            // Aufnahmediagnose wird an GRouper übergeben wegen dem Regelkriterium

                            grouperRequest.addAdmissionDiagnose(icd.getId(), icd.getIcdcCode(),
                                    icd.getIcdcReftypeEn() == null ? 0 : icd.getIcdcReftypeEn().getId(),
                                    icd.getIcdcLocEn() == null ? 0 : icd.getIcdcLocEn().ordinal());
                        }
                    }
                    continue;
                }
                TransferIcd tIcd = new TransferIcd(icdList == null ? icd.getId() : checkId(icdList, icd),
                        icd.getIcdcCode(),
                        icd.getIcdcReftypeEn() == null ? 0 : icd.getIcdcReftypeEn().getId(),
                        icd.getIcdcLocEn() == null ? 0 : icd.getIcdcLocEn().ordinal(),
                        icd.getIcdcIsHdxFl(),
                        icd.getIcdcTypeEn() == null ? false : icd.getIcdcTypeEn().equals(IcdcTypeEn.Aufnahme));
                if (icd.getIcdcIsHdxFl()) {
                    grouperRequest.setPrincipalIcd(tIcd);
                }
                tDep.addIcd(tIcd);
                grouperRequest.addIcd(tIcd);
                if (icd.getIcdcReftypeEn() != null) {
                    if (icd.getIcdcReftypeEn().equals(IcdcRefTypeEn.Kreuz) || icd.getIcdcReftypeEn().equals(IcdcRefTypeEn.Zusatz)) {
                        TCaseIcd icdPrim = icd.getRefIcd();
                        if (icdPrim != null) {
                            try {
                                tIcd.setPrimIcd(icdPrim.getIcdcCode());
                            } catch (Exception ex) {
                                LOG.log(Level.SEVERE, "Yeah, there's a problem in this case: " + String.valueOf(hospitalCase), ex);
                            }
                        }
                    }
                }
            }
//Stationen      
//            if (wards != null && !wards.isEmpty()) {
//                for (TCaseWard ward : wards) {
//                    tDep.addWard(ward.getWardcIdent());
//                }
//            }
        }
//Labor     
// islabor allowed?
        if (cpxServerConfig.getLaboratoryDataDisplayTab()) {
            grouperRequest.setDoLabor(true);
            Set<TLab> labor = hospitalCase.getCaseLabor();
            if (labor != null) {
                for (TLab lab : labor) {
                    grouperRequest.addLabor(new TransferLabor(lab.getLabValue(), lab.getLabText(), lab.getLabUnit(), lab.getLabDescription(), lab.getLabAnalysisDate()));
                }
            }
        }
// entgelte
        Set<TCaseFee> caseFees = csd.getCaseFees();
        if (caseFees != null && !caseFees.isEmpty()) {
            for (TCaseFee fee : caseFees) {
                //(String feeType, double value, Date calcFrom, Date calcTo, int count, int daysNotConsidered) 
                grouperRequest.addFeeRecord(fee.getFeecFeekey(), fee.getFeecValue(), fee.getFeecFrom(), fee.getFeecTo(), fee.getFeecCount(), fee.getFeecUnbilledDays());
            }
        }
// String and Numeric values from KIS-Inteface     
        grouperRequest.setKisInterfaceValues(1, hospitalCase.getString1(), hospitalCase.getNumeric1());
        grouperRequest.setKisInterfaceValues(2, hospitalCase.getString2(), hospitalCase.getNumeric2());
        grouperRequest.setKisInterfaceValues(3, hospitalCase.getString3(), hospitalCase.getNumeric3());
        grouperRequest.setKisInterfaceValues(4, hospitalCase.getString4(), hospitalCase.getNumeric4());
        grouperRequest.setKisInterfaceValues(5, hospitalCase.getString5(), hospitalCase.getNumeric5());
        grouperRequest.setKisInterfaceValues(6, hospitalCase.getString6(), hospitalCase.getNumeric6());
        grouperRequest.setKisInterfaceValues(7, hospitalCase.getString7(), hospitalCase.getNumeric7());
        grouperRequest.setKisInterfaceValues(8, hospitalCase.getString8(), hospitalCase.getNumeric8());
        grouperRequest.setKisInterfaceValues(9, hospitalCase.getString9(), hospitalCase.getNumeric9());
        grouperRequest.setKisInterfaceValues(10, hospitalCase.getString10(), hospitalCase.getNumeric10());
// transfer flag from case merging: HAS_TRANSFER_4_MERGE_FL
        grouperRequest.setHasTransferFlagByMerge( csd.getHasTransferFlag4Merge()== null?false:csd.getHasTransferFlag4Merge());
        return errorList;
    }

    /**
     * fills case with grouping results
     *
     * @param hospitalCase the case which was grouped
     * @param grouperResponse results of grouping
     * @return List the list of old grouping results of this case which are to
     * delete from the Database
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException Exception
     */
    public Map<String, List<TGroupingResults>> fillGrouperResults(TCase hospitalCase,
            GrouperResponseObject grouperResponse) throws CpxIllegalArgumentException {
//        long start = System.currentTimeMillis();
        if (grouperResponse == null) {
            return new HashMap<>();
        }
        long start = System.currentTimeMillis();
        TCaseDetails details = grouperResponse.getResult().getIsLocal() ? hospitalCase.getCurrentLocal() : hospitalCase.getCurrentExtern();
//        Logger.getLogger(GrouperCommunication.class.getName()).log(Level.INFO, "get Details in " + String.valueOf(System.currentTimeMillis() - start));
        Map<String, List<TGroupingResults>> result = fillGrouperResults(hospitalCase, details, grouperResponse);
        Logger.getLogger(GrouperCommunication.class.getName()).log(Level.INFO, "fill result in " + String.valueOf(System.currentTimeMillis() - start));
        return result;
    }

    public Map<String, List<TGroupingResults>> fillGrouperResults(TCase hospitalCase,
            TCaseDetails pDetails,
            GrouperResponseObject grouperResponse) throws CpxIllegalArgumentException {
//        long start = System.currentTimeMillis();
        Map<String, List<TGroupingResults>> retHash = new HashMap<>();
        List<TGroupingResults> toRemove = new ArrayList<>();
        List<TGroupingResults> newResults = new ArrayList<>();
        retHash.put("toRemove", toRemove);
        retHash.put("newResults", newResults);
        if (grouperResponse == null) {
            return retHash;
        }
        TransferCase result = grouperResponse.getResult();
        if (result == null) {
            return retHash;
        }
        Map<Long, TCaseOps> opsAll = new HashMap<>();
        Map<Long, TCaseIcd> icdAll = new HashMap<>();
        TGroupingResults principal = null;
        TransferGroupResult tPrincipal = null;
//        long timestamp = System.currentTimeMillis();
//        TCaseDetails caseDetails = result.getIsLocal()?hospitalCase.getCurrentLocal():hospitalCase.getCurrentExtern();

        pDetails.setCsdLos((long) result.getLengthOfStay());
        pDetails.setCsdLeave(result.getLeaveOfAbsence());
// löschen alte Groupingergebnisse zu diesen CaseDetails und dieser GrouperModel      
//        timestamp = System.currentTimeMillis();
        Set<TGroupingResults> groupingResults = pDetails.getGroupingResultses();
        Set<TGroupingResults> newGroupingResults = new HashSet<>();
//        timestamp = System.currentTimeMillis();

//        ArrayList<Long> removeIds = new ArrayList<>();
        Iterator<TGroupingResults> itr = groupingResults.iterator();

        while (itr.hasNext()) {
            TGroupingResults grRes = itr.next();
            if (grRes.getGrpresIsAutoFl() == result.getIsAuto()
                    && (result.getIsAuto()
                    || !result.getIsAuto() && grRes.getModelIdEn().equals(GDRGModel.getModel(result.getGrouperModelId())))) {
                toRemove.add(grRes);
//if there is any mapping in t_CASE_MERGE_MAPPING for thie result, delete all lines with this mapping ident
                caseMergeMappingDao.deleteMappings4GrpresId(grRes.getId());
                groupingResultsDao.deleteById(grRes.getId());
//                grRes.setCaseDetails(null);
//                removeIds.add(grRes.getId());
            } else {
                newGroupingResults.add(grRes);
            }
        }
//        groupingResults.removeAll(toRemove);
        pDetails.getGroupingResultses().clear();
        pDetails.getGroupingResultses().addAll(newGroupingResults);
//        Logger.getLogger(GrouperCommunication.class.getName()).log(Level.INFO, "remove old grouping results:  caseNumber = " + hospitalCase.getCsCaseNumber() 
//                + " count = " + groupingResults.size()
//                + " time = " + String.valueOf(System.currentTimeMillis() - timestamp));
//        timestamp = System.currentTimeMillis();

//Abteilungen      
// alle  Listen müssen gleiche Länge haben, da die Transferlisten aus den T - Listen nach dem gleichen Algorithmus gebaut wurden, wie sie jetzt gelesen werden
        List<TCaseDepartment> departments = pDetails.getSortedDepartments();

        for (TCaseDepartment dep : departments) {
//            timestamp = System.currentTimeMillis();
            Set<TCaseOps> opses = dep.getAllOps();
            Set<TCaseIcd> icds = dep.getAllIcds();

//Prozeduren            
            for (TCaseOps ops : opses) {
                if (!ops.getOpsIsToGroupFl()) {
                    continue;
                }
                opsAll.put(ops.getId(), ops);
            }
//Diagnosen       

            for (TCaseIcd icd : icds) {
                if (!icd.getIcdIsToGroupFl()) {

                    continue;
                }
                icdAll.put(icd.getId(), icd);
                TransferIcd tIcd = result.getIcd2id(icd.getId());
                if (tIcd == null) {
                    //Kein Transferobjekt für die Icd id = " + icd.getId() + " gefunden";
                    continue;

                }

                TransferGroupResult tRes = tIcd.getGroupResult();
                TGroupingResults grouperResult = createOneGroupingResult(tRes, hospitalCase.getCsCaseTypeEn());
                if (tRes != null) {
                    newResults.add(grouperResult);
                    grouperResult.setCaseDetails(pDetails);
                    
                    grouperResult.setCaseIcd(icd);

                    if (icd.getIcdcIsHdxFl()) {
                        principal = grouperResult;
                        pDetails.getGroupingResultses().add(grouperResult);
                        tPrincipal = tRes;
                    }
                }
            }
            if (principal == null) {
                TransferGroupResult tRes = result.getGroupResult2principalNull();
                if (tRes != null) {
                    principal = createOneGroupingResult(tRes, hospitalCase.getCsCaseTypeEn());
                    newResults.add(principal);
                    principal.setCaseDetails(pDetails);
                    pDetails.getGroupingResultses().add(principal);
                    tPrincipal = tRes;
                }
            }
        }
//        timestamp = System.currentTimeMillis();
// Z.z. haben wir die Simulationseinträge nur für Hauptdiagnose des Falles            
        if (principal != null && tPrincipal != null) {
            List<TransferIcdResult> tResIcd = tPrincipal.getIcdRes();
            for (TransferIcdResult tIcd : tResIcd) {
                TCaseIcdGrouped icdGr = new TCaseIcdGrouped();
                icdGr.setCaseIcd(icdAll.get(tIcd.getId()));
                icdGr.setGroupingResults(principal);
                principal.getCaseIcdGroupeds().add(icdGr);
                icdGr.setIcdResCcl(tIcd.getCCL());
                icdGr.setIcdResU4gFl(tIcd.isUsed4grouping());
                icdGr.setIcdResValidEn(tIcd.getValid());
            }
            Map<Long, TransferOpsResult> hTResOps = tPrincipal.getOpsRes();
            Collection<TransferOpsResult> vals = hTResOps.values();
            Iterator<TransferOpsResult> tResOps = vals.iterator();
            while (tResOps.hasNext()) {
                TransferOpsResult tOps = tResOps.next();
                if (tOps == null) {
                    continue;

                }
                TCaseOpsGrouped opsGr = new TCaseOpsGrouped();
                opsGr.setCaseOps(opsAll.get(tOps.getId()));
                opsGr.setGroupingResults(principal);
                principal.getCaseOpsGroupeds().add(opsGr);
                opsGr.setOpsResU4gFl(tOps.isUsed4grouping());
                opsGr.setOpsResValidEn(tOps.getValid());
                TransferSupplementaryFee tFee = tOps.getSupplementaryFee();
                if (tFee != null) {
                    // supplementary fee
                    TCaseSupplFee sFee = new TCaseSupplFee();
                    sFee.setCaseOpsGrouped(opsGr);
                    opsGr.setCaseSupplFees(sFee);
                    sFee.setCsuplCount(tFee.getCount());
                    sFee.setCsuplfeeCode(tFee.getCode());
                    sFee.setCsuplTypeEn(SupplFeeTypeEn.get2Id(tFee.getTypeid()));
                    sFee.setCsuplFrom(tFee.getFrom());
                    sFee.setCsuplTo(tFee.getTo());
                    sFee.setCsuplCwValue(tFee.getCw());
                    sFee.setCsuplValue(tFee.getValue());
                }
            }

             addSimulatedAndRuleResults(principal, tPrincipal, grouperResponse, result);

        }
//        Logger.getLogger(GrouperCommunication.class.getName()).log(Level.INFO, "fill result methode " + String.valueOf(System.currentTimeMillis() - start));
        return retHash;
    }

    public static void  addSimulatedAndRuleResults(TGroupingResults principal,
            TransferGroupResult tPrincipal,
            GrouperResponseObject grouperResponse,
            TransferCase result
    ) {
        //detected rules, rules applied to principal diagnosis configuration only
        if (principal != null && tPrincipal != null) {
            List<TransferRule> rules = grouperResponse.getDetectedRules();
            List<Long> roleIds = result.getRoleIds(); // if roleIds == null we do it for all roles
            Map<Long, TRole2Result> r2rs = new HashMap<>();
            Set<TRole2Result> r2rPrincipal = new HashSet<>();
            principal.setRole2Results(r2rPrincipal);

            
            for (TransferRule rule : rules) {
                TCheckResult res = new TCheckResult();
                res.setChkReferences(rule.getReferences());
                res.setGroupingResults(principal);
                principal.getCheckResults().add(res);
                res.setChkDrg(rule.getDrg());
                res.setChkCwSimulDiff(rule.getDcw());
                res.setChkCwCareSimulDiff(rule.getDCareCw());
                res.setChkFeeSimulDiff(rule.getDfee());
                res.setRuleid(rule.getId());
//fill risk values
                CpxSimpleRisk risk = rule.getRuleRisks();
                if(risk != null){
                    res.setChkRskAuditPercentVal(risk.getRiskAuditPercentValueAsNumber());
                    res.setChkRskPercentVal(risk.getRiskWastePercentValueAsNumber());
                    res.setChkRskDefaultWasteVal(risk.getRiskDefaultWasteValueAsNumber());
                    res.setChkRskWasteVal(Math.abs(Lang.round((risk.doUseDefault()?risk.getRiskDefaultWasteValueAsNumber():rule.getDfee()) * risk.getForBillingKoeff(), 2)));
                    res.setChkRskAuditVal(Math.abs(Lang.round((risk.doUseDefault()?risk.getRiskDefaultWasteValueAsNumber():rule.getDfee()) * risk.getRiskAuditPercentValueAsNumber(), 2)));
                }
                // fill TRole2Check and TRole2Result
                long[] roles = rule.getRoles();
                if (roles != null && roles.length > 0) {
                    for (long role : roles) {
                        if (roleIds == null || roleIds.contains(role)) {
                            TRole2Check role2check = new TRole2Check();
                            role2check.setCheckResult(res);
                            res.getRole2Check().add(role2check);
                            role2check.setRoleId(role);
                            TRole2Result r2r = r2rs.get(role);
                            if (r2r == null) {
                                r2r = new TRole2Result();
                                r2rs.put(role, r2r);
                                r2r.setGroupingResults(principal);
                                r2r.setMaxDcwPosRef(res);
                                r2rPrincipal.add(r2r);
                                r2r.setRoleId(role);
                                r2r.setMaxDcwPositive(0D);
                                r2r.setMaxDfeePositive(0D);
                                r2r.setMinDcwNegative(0D);
                                r2r.setMinDfeeNegative(0D);
                                r2r.setR2rAdwiseCount(0);
                                r2r.setR2rErrorCount(0);
                                r2r.getR2rWarningCount();
                            }
                            r2r.checkCheckResult(res, rule.getRuleType());
                        }
                    }
                }
            }           
            
        }
        
    }

    public Map<String, List<TGroupingResults>> fillGrouperResults3(TCase hospitalCase, TCaseDetails pDetails,
            GrouperResponseObject grouperResponse) throws CpxIllegalArgumentException {
//        long start = System.currentTimeMillis();
        Map<String, List<TGroupingResults>> retHash = new HashMap<>();
        List<TGroupingResults> toRemove = new ArrayList<>();
        List<TGroupingResults> newResults = new ArrayList<>();
        retHash.put("toRemove", toRemove);
        retHash.put("newResults", newResults);
        if (grouperResponse == null) {

            return retHash;
        }
        TransferCase result = grouperResponse.getResult();
        if (result == null) {
            return retHash;
        }
        Map<Long, TCaseOps> opsAll = new HashMap<>();
        Map<Long, TCaseIcd> icdAll = new HashMap<>();
        TGroupingResults principal = null;
        TransferGroupResult tPrincipal = null;
//        long timestamp = System.currentTimeMillis();
//        TCaseDetails caseDetails = result.getIsLocal()?hospitalCase.getCurrentLocal():hospitalCase.getCurrentExtern();

        pDetails.setCsdLos((long) result.getLengthOfStay());
        pDetails.setCsdLeave(result.getLeaveOfAbsence());
// löschen alte Groupingergebnisse zu diesen CaseDetails und dieser GrouperModel      
//        timestamp = System.currentTimeMillis();
        pDetails.getGroupingResultses().iterator();
        Set<TGroupingResults> groupingResults = pDetails.getGroupingResultses();
//        Set<TGroupingResults> newGroupingResults = new HashSet<>();
//        timestamp = System.currentTimeMillis();
//        ArrayList<Long> removeIds = new ArrayList<>();
//        Iterator<TGroupingResults> it = groupingResults.iterator();
//        while (it.hasNext()) {
//            TGroupingResults grRes = it.next();
//            if( grRes.getGrpresIsAutoFl() == result.getIsAuto()
//                && (result.getIsAuto()
//                || !result.getIsAuto() && grRes.getModelIdEn().equals(GDRGModel.getModel(result.getGrouperModelId())))
//                ){
//                 pDetails.getGroupingResultses().remove(grRes);
//            }
//        }
        Iterator<TGroupingResults> itr = groupingResults.iterator();
        TGroupingResults grouperResult = null;
        while (itr.hasNext()) {
            TGroupingResults grRes = itr.next();
            if (grRes.getGrpresIsAutoFl() == result.getIsAuto()
                    && (result.getIsAuto()
                    || !result.getIsAuto() && grRes.getModelIdEn().equals(GDRGModel.getModel(result.getGrouperModelId())))) {
                toRemove.add(grRes);

            }
        }
//        groupingResults.removeAll(toRemove);
//        pDetails.getGroupingResultses().clear();
//        pDetails.getGroupingResultses().addAll(newGroupingResults);
//        Logger.getLogger(GrouperCommunication.class.getName()).log(Level.INFO, "remove old grouping results:  caseNumber = " + hospitalCase.getCsCaseNumber() 
//                + " count = " + groupingResults.size()
//                + " time = " + String.valueOf(System.currentTimeMillis() - timestamp));
//        timestamp = System.currentTimeMillis();

//Abteilungen      
// alle  Listen müssen gleiche Länge haben, da die Transferlisten aus den T - Listen nach dem gleichen Algorithmus gebaut wurden, wie sie jetzt gelesen werden
        List<TCaseDepartment> departments = pDetails.getSortedDepartments();

        for (TCaseDepartment dep : departments) {
//            timestamp = System.currentTimeMillis();
            Set<TCaseOps> opses = dep.getCaseOpses();
            Set<TCaseIcd> icds = dep.getCaseIcds();

//Prozeduren            
            for (TCaseOps ops : opses) {
                if (!ops.getOpsIsToGroupFl()) {
                    continue;
                }
                opsAll.put(ops.getId(), ops);
            }
//Diagnosen       

            for (TCaseIcd icd : icds) {
                if (!icd.getIcdIsToGroupFl()) {

                    continue;
                }
                icdAll.put(icd.getId(), icd);
                TransferIcd tIcd = result.getIcd2id(icd.getId());
                if (tIcd == null) {
                    continue;

                }

                TransferGroupResult tRes = tIcd.getGroupResult();
                if (tRes != null) {
                    if (grouperResult == null) {
                        grouperResult = createOneGroupingResult(tRes, hospitalCase.getCsCaseTypeEn());
                        grouperResult.setCreationDate(new Date());
                    }
                    newResults.add(grouperResult);
                    grouperResult.setCaseDetails(pDetails);
//                    pDetails.getGroupingResultses().add(grouperResult);
                    grouperResult.setCaseIcd(icd);

                    if (icd.getIcdcIsHdxFl()) {
                        principal = grouperResult;
                        tPrincipal = tRes;
                    }
                }
            }
            if (principal == null) {
                TransferGroupResult tRes = result.getGroupResult2principalNull();
                if (tRes != null) {
                    if (grouperResult == null) {
                        principal = createOneGroupingResult(tRes, hospitalCase.getCsCaseTypeEn());
                        principal.setCreationDate(new Date());
                    } else {
                        principal = grouperResult;
                    }
                    newResults.add(principal);
                    principal.setCaseDetails(pDetails);
//                    pDetails.getGroupingResultses().add(principal);
                    tPrincipal = tRes;
                }
            }
        }
//        timestamp = System.currentTimeMillis();
// Z.z. haben wir die Simulationseinträge nur für Hauptdiagnose des Falles            
        if (principal != null && tPrincipal != null) {
            List<TransferIcdResult> tResIcd = tPrincipal.getIcdRes();
            for (TransferIcdResult tIcd : tResIcd) {
                TCaseIcdGrouped icdGr = getIcdGrouped(tIcd, principal);
                icdGr.setCaseIcd(icdAll.get(tIcd.getId()));
                icdGr.setGroupingResults(principal);
                principal.getCaseIcdGroupeds().add(icdGr);
                icdGr.setIcdResCcl(tIcd.getCCL());
                icdGr.setIcdResU4gFl(tIcd.isUsed4grouping());
                icdGr.setIcdResValidEn(tIcd.getValid());
            }
            Map<Long, TransferOpsResult> hTResOps = tPrincipal.getOpsRes();
            Collection<TransferOpsResult> vals = hTResOps.values();
            Iterator<TransferOpsResult> tResOps = vals.iterator();
            while (tResOps.hasNext()) {
                TransferOpsResult tOps = tResOps.next();
                if (tOps == null) {
                    continue;

                }
                TCaseOpsGrouped opsGr = getOpsGrouped(tOps, principal);
                opsGr.setCaseOps(opsAll.get(tOps.getId()));
                opsGr.setGroupingResults(principal);
                principal.getCaseOpsGroupeds().add(opsGr);
                opsGr.setOpsResU4gFl(tOps.isUsed4grouping());
                opsGr.setOpsResValidEn(tOps.getValid());
                TransferSupplementaryFee tFee = tOps.getSupplementaryFee();
                if (tFee != null) {
                    // supplementary fee
                    if (opsGr.getCaseSupplFees() == null) {
                        opsGr.setCaseSupplFees(new TCaseSupplFee());
                    }
                    TCaseSupplFee sFee = opsGr.getCaseSupplFees();
                    sFee.setCaseOpsGrouped(opsGr);
//                    opsGr.setCaseSupplFees(sFee);
                    sFee.setCsuplCount(tFee.getCount());
                    sFee.setCsuplfeeCode(tFee.getCode());
                    sFee.setCsuplTypeEn(SupplFeeTypeEn.get2Id(tFee.getTypeid()));
                    sFee.setCsuplFrom(tFee.getFrom());
                    sFee.setCsuplTo(tFee.getTo());
                    sFee.setCsuplCwValue(tFee.getCw());
                    sFee.setCsuplValue(tFee.getValue());
                }
            }
            //detected rules, rules applied to principal diagnosis configuration only
            List<TransferRule> rules = grouperResponse.getDetectedRules();
            List<Long> roleIds = result.getRoleIds(); // if roleIds == null we do it for all roles
            Map<Long, TRole2Result> r2rs = new HashMap<>();
            Set<TRole2Result> r2rPrincipal = new HashSet<>();
//            principal.clearRules();
            principal.setRole2Results(r2rPrincipal);
            for (TransferRule rule : rules) {
                TCheckResult res = new TCheckResult();
                res.setChkReferences(rule.getReferences());
                res.setGroupingResults(principal);
                principal.getCheckResults().add(res);
                res.setChkDrg(rule.getDrg());
                res.setChkCwSimulDiff(rule.getDcw());
                res.setChkFeeSimulDiff(rule.getDfee());
                res.setRuleid(rule.getId());
                // fill TRole2Check and TRole2Result
                long[] roles = rule.getRoles();
                if (roles != null && roles.length > 0) {
                    for (long role : roles) {
                        if (roleIds == null || roleIds.contains(role)) {
                            TRole2Check role2check = new TRole2Check();
                            role2check.setCheckResult(res);
                            res.getRole2Check().add(role2check);
                            role2check.setRoleId(role);
                            TRole2Result r2r = r2rs.get(role);
                            if (r2r == null) {
                                r2r = new TRole2Result();
                                r2rs.put(role, r2r);
                                r2r.setGroupingResults(principal);
//                                principal.getRole2Results().add(r2r);
                                r2rPrincipal.add(r2r);
                                r2r.setRoleId(role);
                                r2r.setR2rAdwiseCount(0);
                                r2r.setR2rErrorCount(0);
                                r2r.getR2rWarningCount();
                            }
                            r2r.checkCheckResult(res, rule.getRuleType());
                        }
                    }
                }
            }

        }
        return retHash;
    }

    /**
     * adds result of grouping of one case to the batch result
     *
     * @param batchCaseResult Object of TBatchResult which gathers the Results
     * of Batchgrouping
     * @param batchGroupResult Results of grouping of one case
     * @param rules list of rules for this case
     * @param roleIds role ids for which will be gegrouped
     */
//    public void addResult4OneCase(TBatchResult batchCaseResult, BatchGroupResult batchGroupResult, List<TransferRule> rules) {
//        addResult4OneCase(batchCaseResult, batchGroupResult, rules, null);
//    }
//    
//    
    public static void addResult4OneCase(TBatchResult batchCaseResult, BatchGroupResult batchGroupResult,
            List<TransferRule> rules, List<Long> roleIds) {
        if (batchGroupResult != null) {
            if (batchGroupResult.isHtp()) {
                batchCaseResult.addBatchresHtpCount();
            }
            if (batchGroupResult.isLtp()) {
                batchCaseResult.addBatchresLtpCount();
            }

            if (batchGroupResult.isTransfer()) {
                batchCaseResult.addBatchresTransfCount();
            }

            batchCaseResult.addBatchresPcclSum(batchGroupResult.getPccl());
            batchCaseResult.addBatchresCwEffectivSum(batchGroupResult.getCwEff());
            batchCaseResult.addBatchresCwCatalogSum(batchGroupResult.getCwCatalog());
            if (batchGroupResult.isDead()) {
                batchCaseResult.addBatchresDeadCount();
            }
            if (batchGroupResult.isDayCare()) {
                batchCaseResult.addBatchresDayCareCount();
            }
            batchCaseResult.addBatchresCareDaysSum(batchGroupResult.getCareDays());
            batchCaseResult.addBatchresAuxdCount(batchGroupResult.getAuxIcdCount());
            batchCaseResult.addBatchresLosIntensivSum(batchGroupResult.getLosIntensiv());
            if (batchGroupResult.isIntensiv()) {
                batchCaseResult.addBatchresCaseIntensivCount();
            }
            if (batchGroupResult.isGrouped()) {
                batchCaseResult.addBatchresGroupedCount();
            }
            if (batchGroupResult.isErrDrg()) {
                batchCaseResult.addBatchresErrDrgCount();
            }
            batchCaseResult.addBatchresCaseCount();
            batchCaseResult.addBatchresAux9Count(batchGroupResult.getAux9count());
            batchCaseResult.addBatchresNalosSum(batchGroupResult.getNalos());

            Map<Long, TBatchResult2Role> role2RuleResult = batchCaseResult.getResult2Role();

            TBatchResult2Role allRoles = role2RuleResult.get(0L);
            if (allRoles == null) {
                allRoles = new TBatchResult2Role();
                allRoles.setRoleId(0L);
                allRoles.setBatchResult(batchCaseResult);
                batchCaseResult.getBatchres2role().add(allRoles);
                role2RuleResult.put(0L, allRoles);
            }
            if (roleIds != null) {
                for (Long id : roleIds) {
                    TBatchResult2Role r2r = role2RuleResult.get(id);
                    if (r2r == null) {
                        r2r = new TBatchResult2Role();
                        r2r.setRoleId(id);
                        r2r.setBatchResult(batchCaseResult);
                        batchCaseResult.getBatchres2role().add(r2r);
                        role2RuleResult.put(id, r2r);
                    }

                }
            }
            if (rules != null) {
                 for (TransferRule rule : rules) {
                    // allRoles - add rule results
                    long[] roles = rule.getRoles();
                    if (roles != null) {
                        for (long role : roles) {
                            if (rule.getUsedRoles() == null || rule.getUsedRoles().contains(role)) {
                                TBatchResult2Role res = role2RuleResult.get(role);
                                if (res == null) {
                                    res = new TBatchResult2Role();
                                    res.setRoleId(role);
                                    role2RuleResult.put(role, res);
                                    res.setBatchResult(batchCaseResult);
                                    batchCaseResult.getBatchres2role().add(res);
                                }
                                // add rule results for role
                                addRuleData(res, rule);
                            }
                        }
                    }
                    // add rule results for allRoles
                    addRuleData(allRoles, rule);
                }
            }

        }

    }

    /**
     * distributes the rule results to the different roles
     */
    private static void addRuleData(TBatchResult2Role role2result, TransferRule rule) {
        if (role2result.getB2rMaxDcwPosSum() < rule.getDcw()) {
            role2result.setB2rMaxDcwPosSum(rule.getDcw());
        }
        if (role2result.getB2rMinDcwNegSum() > rule.getDcw()) {
            role2result.setB2rMinDcwNegSum(rule.getDcw());
        }
        if (role2result.getB2rMaxDfeePosSum() < rule.getDfee()) {
            role2result.setB2rMaxDfeePosSum(rule.getDfee());
        }
        if (role2result.getB2rMinDfeeNegSum() > rule.getDfee()) {
            role2result.setB2rMinDfeeNegSum(rule.getDfee());
        }

        Map<String, TBatchCheckResult> type2checkResult = role2result.getType2checkResult();

// TODO:  rule.getRuleErrType() is always null, to fill? from rule.getRuleDescrTypeID()  fill CRGRuleTypes csruletypes.xml?      
        TBatchCheckResult checkResult = type2checkResult.get(rule.getRuleErrType());
        if (checkResult == null) {
            checkResult = new TBatchCheckResult();
            checkResult.setBcheckresRuleType(rule.getRuleErrType());
            role2result.getBatchCheckResult().add(checkResult);
            checkResult.setBatchResult2Role(role2result);
            type2checkResult.put(rule.getRuleErrType(), checkResult);
        }
        switch (rule.getRuleType()) {
            case STATE_WARNING:
                checkResult.setBchechresWarnCount(checkResult.getBchechresWarnCount() + 1);
                break;
            case STATE_ERROR:
                checkResult.setBchechresErrCount(checkResult.getBchechresErrCount() + 1);
                break;
            case STATE_SUGG:
                checkResult.setBatchresAdviceCount(checkResult.getBatchresAdviceCount() + 1);
                break;
            default:
                LOG.log(Level.WARNING, "Unknown rule type: " + rule.getRuleType());
                break;
        }
    }

    /**
     * returns a temporary grouping result to the principal Icd from the
     * GrouperResponceObject it does not have any references to TCaseDetails,
     * TCaseIcd and TCaseOps entities the additional fees cannot be distributed
     * because the HashMap which is used for this distribution has opsId as a
     * key. So as we did not saved a new case in the db we don't have ids for
     * ops
     *
     * @param grouperResponse Grouper Response
     * @param allIcds Diagnosis
     * @param allOps Procedures
     * @param cmpIcds ICD Comparator
     * @param cmpOps OPS Comparator
     * @return Grouping Results
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException Exception
     */
    public static TGroupingResults getTempResult4Group(GrouperResponseObject grouperResponse,
            Map<TCaseIcd, Long> allIcds, Map<TCaseOps, Long> allOps,
            Comparator<TCaseIcd> cmpIcds, Comparator<TCaseOps> cmpOps) throws CpxIllegalArgumentException {
        return getTempResult4Group(grouperResponse,
             allIcds,  allOps,
            cmpIcds,  cmpOps, null);
    }
   public static TGroupingResults getTempResult4Group(GrouperResponseObject grouperResponse,
            Map<TCaseIcd, Long> allIcds, Map<TCaseOps, Long> allOps,
            Comparator<TCaseIcd> cmpIcds, Comparator<TCaseOps> cmpOps, TCaseDetails pCaseDetails) throws CpxIllegalArgumentException {
        if (grouperResponse == null) {
            return null;
        }
        Set <TGroupingResults> results = new HashSet<>();
        TransferCase result = grouperResponse.getResult();
        if (result == null) {
            return null;
        }
        TransferIcd hdx = null;
        TransferGroupResult tRes = result.getGroupResult2principalNull();// grouping result when hdx is not set
        if (tRes == null) {
            hdx = result.getPrincipalIcd();
            if (hdx != null) {
                tRes = hdx.getGroupResult();
            }

        }
        if (tRes == null) {
            LOG.log(Level.INFO, "could not find any grouping result for this case");
        }
        TGroupingResults grouperResult = createOneGroupingResult(tRes, CaseTypeEn.getValue2Id(result.getCaseType()));
         // transient values lengthOfStay and leave
        if (grouperResult == null) {
            LOG.log(Level.SEVERE, "grouperResult is null!");
            return grouperResult;
        } else {
            grouperResult.setCalculatedLeave(result.getLeaveOfAbsence());
            grouperResult.setCalculatedLengthOfStay(result.getLengthOfStay());
        }
        grouperResult.setCaseDetails(pCaseDetails);
        results.add(grouperResult);

        Map<Long, TransferIcd> icdMap = result.getIcd2id();
        if (allIcds != null && !allIcds.isEmpty() && hdx != null) {
            List<TCaseIcd> tIcdSort = getSortList(allIcds, cmpIcds);
           
            List<TransferIcdResult> tResIcd = tRes == null ? new ArrayList<>() : tRes.getIcdRes();
            for (TCaseIcd icd : tIcdSort) {
                long icdId = allIcds.get(icd);
                TransferIcdResult tIcd = null;
                for (TransferIcdResult toneIcd : tResIcd) {
                    if (toneIcd.getId() == icdId) {
                        tIcd = toneIcd;
                        break;
                    }
                }
                if (tIcd == null) {
                    continue;
                }
                TGroupingResults grResult = null;
                TransferIcd tIcd2Id = icdMap.get(icdId);
                    if(tIcd2Id != null && tIcd2Id.getGroupResult() != null){
                    // add simulated result
                    grResult = createOneGroupingResult(tIcd2Id.getGroupResult(), CaseTypeEn.getValue2Id(result.getCaseType()));
                    if(grResult != null){
                        grResult.setCaseIcd(icd);
                        Set <TGroupingResults> res4Icd = new HashSet<>();
                        res4Icd.add(grResult);
                        icd.setGroupingResultses(res4Icd);
                        grResult.setCaseDetails(pCaseDetails);
                        results.add(grResult);
                    }
                }

                TCaseIcdGrouped icdGr = getIcdGrouped(tIcd, grouperResult);
                icdGr.setCaseIcd(icd);
                icdGr.setGroupingResults(grouperResult);
                if(grouperResult != null){
                    grouperResult.getCaseIcdGroupeds().add(icdGr);
                }
                icdGr.setIcdResCcl(tIcd.getCCL());
                icdGr.setIcdResU4gFl(tIcd.isUsed4grouping());
                icdGr.setIcdResValidEn(tIcd.getValid());
                
            }
        }

        if (allOps != null && ! allOps.isEmpty()) {

            List<TCaseOps> tOpsSort = getSortList(allOps, cmpOps);
            Map<Long, TransferOpsResult> hTResOps = tRes == null ? new HashMap<>() : tRes.getOpsRes();
            for (TCaseOps ops : tOpsSort) {
                long opsId = allOps.get(ops);
                TransferOpsResult tOps = hTResOps.get(opsId);
                if (tOps == null) {
                    continue;
                }
                TCaseOpsGrouped opsGr = new TCaseOpsGrouped();
                opsGr.setGroupingResults(grouperResult);
                grouperResult.getCaseOpsGroupeds().add(opsGr);
                opsGr.setOpsResU4gFl(tOps.isUsed4grouping());
                opsGr.setOpsResValidEn(tOps.getValid());
                opsGr.setCaseOps(ops);
                TransferSupplementaryFee tFee = tOps.getSupplementaryFee();
                if (tFee != null) {
                    // supplementary fee
                    TCaseSupplFee sFee = new TCaseSupplFee();
                    sFee.setCaseOpsGrouped(opsGr);
                    opsGr.setCaseSupplFees(sFee);
                    sFee.setCsuplCount(tFee.getCount());
                    sFee.setCsuplfeeCode(tFee.getCode());
                    sFee.setCsuplTypeEn(SupplFeeTypeEn.get2Id(tFee.getTypeid()));
                    sFee.setCsuplFrom(tFee.getFrom());
                    sFee.setCsuplTo(tFee.getTo());
                    sFee.setCsuplCwValue(tFee.getCw());
                    sFee.setCsuplValue(tFee.getValue());
                }
            }
        }
        if(pCaseDetails != null){
            pCaseDetails.setGroupingResultses(results);
        }
        addSimulatedAndRuleResults(grouperResult, tRes, grouperResponse, result);
        return grouperResult;
    }

    /**
     * creates one grouping result from the TransferGroupResult
     *
     * @param tRes transfer grouper result
     * @param csType case type
     * @return grouping results
     */
    public static TGroupingResults createOneGroupingResult(TransferGroupResult tRes, CaseTypeEn csType) throws CpxIllegalArgumentException {
        if (tRes == null) {
            return null;
        }
        TGroupingResults grouperResult;
        if (csType.equals(CaseTypeEn.DRG)) {
            grouperResult = new TCaseDrg();
        } else {
            grouperResult = new TCasePepp();
        }

        grouperResult = updateOneGroupingResult(tRes, grouperResult, csType);

        return grouperResult;
    }

    private static TGroupingResults updateOneGroupingResult(TransferGroupResult pTrRes,
            TGroupingResults pGrRes, CaseTypeEn pCsType) throws CpxIllegalArgumentException {
        pGrRes.setGrpresIsAutoFl(pTrRes.isAuto());

        pGrRes.setGrpresCode(pTrRes.getCode()); //DRG/PEPP
        pGrRes.setGrpresGroup(GrouperMdcOrSkEn.getValue2Id(pTrRes.getGroup())); //MDC/SK 
        pGrRes.setGrpresPccl(pTrRes.getPccl() );
        pGrRes.setGrpresGpdx(GroupResultPdxEn.getValue2Id(pTrRes.getGpdx()));
        pGrRes.setGrpresGst(GrouperStatusEn.get2Id(pTrRes.getGst()));
        pGrRes.setModelIdEn(GDRGModel.getModel(pTrRes.getModelId())); // Groupermodel, mit der gegroupt wurde
        pGrRes.setGrpresIsNegotiatedFl(pTrRes.isNegotiated());
        
        if (pCsType.equals(CaseTypeEn.DRG)) {
            pGrRes.setGrpresType(CaseTypeEn.DRG);
           
            TCaseDrg csDrg = (TCaseDrg) pGrRes;
            TransferDrg tDrg = (TransferDrg) pTrRes;
            csDrg.setDrgcCwEffectiv(pTrRes.getCwEffectiv());
            csDrg.setAdrg(tDrg.getAdrg());
            csDrg.setDrgcCwCorr(tDrg.getCorrectionValue());
            csDrg.setDrgcDaysCorr((short) tDrg.getCorrectionDays());
            csDrg.setDrgcTypeOfCorrEn(DrgCorrTypeEn.get2Id(tDrg.getTypeOfCorrection()));
            csDrg.setDrgcHtp(tDrg.getHtp());
            csDrg.setIsDrgcIsExceptionFl(tDrg.isException());

            csDrg.setDrgcPartitionEn(DrgPartitionEn.getValue2name(tDrg.getDrgPartition()));
            csDrg.setDrgcNegoFee2Day(tDrg.getTge2Day());
            csDrg.setDrgcNegoFeeDays(tDrg.getTgeDays());
            csDrg.setDrgcAlos(tDrg.getAlos());
            csDrg.setDrgcLtp(tDrg.getLtp());
            csDrg.setDrgcCwCatalog(tDrg.getCwCatalog());
            csDrg.setDrgcCwCorrDay(tDrg.getCwCorr2Day());
            csDrg.setDrgcCareCwDay(tDrg.getCareCwDay());
            csDrg.setDrgcCareDays(tDrg.getCareDays());
            csDrg.setDrgcCareCw(tDrg.getCareCw()); 
            if(!tDrg.getCareValues().isEmpty()){
                Set <TCaseDrgCareGrades>careGrades = new HashSet<>();
                for(TransferDrgCare care: tDrg.getCareValues()){
                    TCaseDrgCareGrades oneGrade = new TCaseDrgCareGrades();
                    oneGrade.setCaseDrg(csDrg);
                    oneGrade.setDrgCareBaserate(care.getBaserate());
                    oneGrade.setDrgCareCwDay(care.getCare_cw_day());
                    oneGrade.setDrgCareDays(care.getCare_days());
                    oneGrade.setDrgCareFrom(care.getStartBaserate());
                    oneGrade.setDrgCareTo(care.getEndBaserate());
                    oneGrade.setDrgCareSortInd(care.getSortInd());
                    careGrades.add(oneGrade);
                }
                csDrg.setDrgCareGrades(careGrades);
            }

        } else if (pCsType.equals(CaseTypeEn.PEPP)) {
            pGrRes.setGrpresType(CaseTypeEn.PEPP);
            TCasePepp csPepp = (TCasePepp) pGrRes;
            if (pTrRes instanceof TransferPepp) {
                TransferPepp tPepp = (TransferPepp) pTrRes;
                csPepp.setPeppcCwEffectiv(tPepp.getCwEffectiv());
                csPepp.setPeppcDaysPerscareAdult(tPepp.getAdultPersonalCare());
                csPepp.setPeppcDaysPerscareInf(tPepp.getInfantsPersonalCare());
                csPepp.setPeppcDaysIntensiv(tPepp.getDurationsIntensivCare());
                csPepp.setPeppcPersentageIntens(tPepp.getDurationsIntesivPersentage());
                if (tPepp.getHasGrades()) {
                    csPepp.setPeppcType(PeppPayTypeEn.PayGrade);
                } else {
                    csPepp.setPeppcType(PeppPayTypeEn.PayClass);
                }
                List<TransferPeppGrade> grades = tPepp.getGrades();
                Set<TCasePeppGrades> pappcGrades = new HashSet<>();
                csPepp.setPeppcGrades(pappcGrades);
                csPepp.setPeppcPayClass(tPepp.getPayClass());
                csPepp.setPeppPayClassCwDay(tPepp.getPeppCw4Class());
                if (grades != null) {
                    for (TransferPeppGrade tGrade : grades) {
                        TCasePeppGrades grade = new TCasePeppGrades();
                        grade.setCasePepp(csPepp);
                        grade.setPeppcgrNumber(tGrade.getGrade());
                        grade.setPeppcgrDays(tGrade.getDuration());
                        grade.setPeppcgrCw(tGrade.getCw());
                        grade.setPeppcgrFrom(tGrade.getFrom());
                        grade.setPeppcgrTo(tGrade.getTo());
                        grade.setPeppcgrBaserate(tGrade.getBaserate());
                        pappcGrades.add(grade);
                    }
                }
            }
        }
        return pGrRes;
    }
    //iterate through all icds of grouping result to detect if an grouping result for that case icd exists
    //if not new icdGrouped is created

    private static TCaseIcdGrouped getIcdGrouped(TransferIcdResult pTrIcd, TGroupingResults pGrResults) {
        for (TCaseIcdGrouped grIcd : pGrResults.getCaseIcdGroupeds()) {
            if (grIcd.getCaseIcd().getId() == pTrIcd.getId()) {
                return grIcd;
            }
        }
        return new TCaseIcdGrouped();
    }

    private static TCaseOpsGrouped getOpsGrouped(TransferOpsResult pTrOps, TGroupingResults pGrResults) {
        for (TCaseOpsGrouped grOps : pGrResults.getCaseOpsGroupeds()) {
            if (grOps.getCaseOps().getId() == pTrOps.getId()) {
                return grOps;
            }
        }
        return new TCaseOpsGrouped();
    }

    private static <E extends AbstractEntity> long checkId(Map<E, Long> tmpList, E entity) {
        long retId = (long) tmpList.keySet().size() + 1;
        tmpList.put(entity, retId);
        return retId;
    }

    private static <E extends AbstractEntity> E getEntity2TmpId(Map<E, Long> tmpList, long id) {
        if (tmpList == null || tmpList.isEmpty()) {
            return null;
        }
        Set<Entry<E, Long>> entries = tmpList.entrySet();
        for (Entry<E, Long> entry : entries) {
            if (entry.getValue().equals(id)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private static <E extends AbstractEntity> List<E> getDefaultSort(Map<E, Long> allEntities) {
        ArrayList<E> sortList = new ArrayList<>();
        if (allEntities == null || allEntities.isEmpty()) {
            return sortList;
        }
        ArrayList<Long> ids = new ArrayList<>(allEntities.values());
        Collections.sort(ids);
        for (Long id : ids) {
            E entity = getEntity2TmpId(allEntities, id);
            if (entity != null) {
                sortList.add(entity);
            }
        }
        return sortList;
    }

    private static <E extends AbstractEntity> List<E> getSortList(Map<E, Long> allEntities, Comparator<E> cmp) {
        ArrayList<E> sortList = new ArrayList<>();
        if (allEntities == null || allEntities.isEmpty()) {
            return sortList;
        }
        Set<E> entityList = allEntities.keySet();

        if (cmp != null) {
            sortList.addAll(entityList);
            Collections.sort(sortList, cmp);
        } else {
            return getDefaultSort(allEntities);
        }
        return sortList;
    }
}
