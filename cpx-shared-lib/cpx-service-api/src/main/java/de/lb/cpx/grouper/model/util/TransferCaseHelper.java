/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.grouper.model.util;

import de.lb.cpx.grouper.model.transfer.TransferCase;
import de.lb.cpx.grouper.model.transfer.TransferDepartment;
import de.lb.cpx.grouper.model.transfer.TransferIcd;
import de.lb.cpx.grouper.model.transfer.TransferOps;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.TCaseWard;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.CpxEnumInterface;
import de.lb.cpx.model.enums.IcdcRefTypeEn;
import de.lb.cpx.model.enums.IcdcTypeEn;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.constraints.NotNull;

/**
 * Helper class to handle transfer cases there needs to be conversions from db
 * objects to transfercase objects for grouping and other analyzing tasks
 *
 * @author wilde
 */
public class TransferCaseHelper {

    private static final Logger LOG = Logger.getLogger(TransferCaseHelper.class.getName());

    public static TransferCase transformTCaseToTransferCase(@NotNull TCase pCase, boolean pLocal, int pGrouperModelId) throws IllegalArgumentException {
        Objects.requireNonNull(pCase, "Hospital case is null");

        TCaseDetails currentDetails = pLocal ? pCase.getCurrentLocal() : pCase.getCurrentExtern();
        TransferCase transferCase = new TransferCase();

        fillTransferCase(pCase, currentDetails, transferCase, pGrouperModelId);
        return transferCase;
    }

    public static void fillTransferCaseForGrouper(@NotNull TCase pCase,
            @NotNull TCaseDetails pDetails,
            @NotNull TransferCase pTransferCase,
            int pGrouperModelId,
            Map<TCaseIcd, Long> pIcdList,
            Map<TCaseOps, Long> pOpsList) throws IllegalArgumentException {

        Objects.requireNonNull(pCase, "Hospital case is null");
        Objects.requireNonNull(pDetails, "Case Version is null");
        Objects.requireNonNull(pTransferCase, "TransferCase is null");

        //2017-11-24 DNi: Make some checks to prevent explosions in grouper component
        final String caseKey = pCase.getCaseKey();
        final String caseId = String.valueOf(pCase.getId());
        final String csdId = String.valueOf(pDetails.getId());
        final String caseInfo = caseKey + " (case id " + caseId + ", details id " + csdId + ")";

//        if (pCase == null) {
//            LOG.log(Level.WARNING, "Hospital case is null!");
//            return false;
//        }
        if (!CaseTypeEn.DRG.equals(pCase.getCsCaseTypeEn()) && !CaseTypeEn.PEPP.equals(pCase.getCsCaseTypeEn()) && !CaseTypeEn.PIA.equals(pCase.getCsCaseTypeEn())) {
            String msg = "Case type is '" + pCase.getCsCaseTypeEn() + "'. This is neither '" + CaseTypeEn.DRG + "' nor '" + CaseTypeEn.PEPP + "' or '" + CaseTypeEn.PIA + "', so I cannot group this case: " + caseInfo;
            throw new IllegalArgumentException(msg);
        }
        if (pDetails.getCsdAdmissionDate() == null) {
            String msg = "No admission date found, so I cannot group this case: " + caseInfo;
            throw new IllegalArgumentException(msg);
        }
        if (pDetails.getCsdDischargeDate() == null) {
            String msg = "No discharge date found, so I cannot group this case: " + caseInfo;
            throw new IllegalArgumentException(msg);
        }
        if (pDetails.getCsdDisReason12En() == null) {
            String msg = "No discharge reason 12 found, so I cannot group this case: " + caseInfo;
            throw new IllegalArgumentException(msg);
        }
        if (pDetails.getCsdAdmodEn() == null) {
            String msg = "No admission mode found, so I cannot group this case: " + caseInfo;
            throw new IllegalArgumentException(msg);
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(pDetails.getCsdAdmissionDate());
        if (cal.get(Calendar.YEAR) < GrouperConstant.GROUPER_MIN_YEAR && pGrouperModelId == 0) {
            String msg = "Admission date is older than 2013, so I cannot group this case: " + caseInfo;
            throw new IllegalArgumentException(msg);
        }
        if (pDetails.getCaseDepartments() != null) {
            Iterator<TCaseDepartment> it = pDetails.getCaseDepartments().iterator();
            while (it.hasNext()) {
                TCaseDepartment dep = it.next();
                if (dep.getDepcAdmDate() == null) {
                    String msg = "Admission date of department id " + dep.getId() + " is null, so I cannot group this case: " + caseInfo;
                    throw new IllegalArgumentException(msg);
                }
                if (dep.getDepcDisDate() == null) {
                    String msg = "Discharge date of department id " + dep.getId() + " is null, so I cannot group this case: " + caseInfo;
                    throw new IllegalArgumentException(msg);
                }
            }
        }
        fillTransferCase(pCase, pDetails, pTransferCase, pGrouperModelId, pIcdList, pOpsList);
    }

    /**
     * fill transfer case with case data NOTE:USED for Grouping
     *
     * @param pCase database case object (not null)
     * @param pDetails current case version to get data from (not null)
     * @param pTransferCase transfercase to be filled (not null)
     * @param pGrouperModelId grouper model
     * @throws IllegalArgumentException thrown if data is not consistent
     */
    public static void fillTransferCase(@NotNull TCase pCase,
            @NotNull TCaseDetails pDetails,
            @NotNull TransferCase pTransferCase,
            int pGrouperModelId) throws IllegalArgumentException {
        fillTransferCase(pCase, pDetails, pTransferCase, pGrouperModelId, null, null);
    }

    /**
     * fill transfer case with case data NOTE:USED for Grouping
     *
     * @param pCase database case object (not null)
     * @param pDetails current case version to get data from (not null)
     * @param pTransferCase transfercase to be filled (not null)
     * @param pGrouperModelId grouper model
     * @param pIcdList map of diagnosis
     * @param pOpsList map of processes
     * @throws IllegalArgumentException thrown if data is not consistent
     */
    public static void fillTransferCase(@NotNull TCase pCase,
            @NotNull TCaseDetails pDetails,
            @NotNull TransferCase pTransferCase,
            int pGrouperModelId,
            Map<TCaseIcd, Long> pIcdList,
            Map<TCaseOps, Long> pOpsList) throws IllegalArgumentException {
        Objects.requireNonNull(pCase, "Hospital case is null");
        Objects.requireNonNull(pDetails, "Case Version is null");
        Objects.requireNonNull(pTransferCase, "TransferCase is null");

        //Fehlt
        //baserate   
        // einzugsgebiet
        //pflegestatus
        //entgeltgruppe
        // Patientendaten   
        // versichertenstatus
        // medikamente, heil- und hilfsmittel
        // labor
        //vwd intensiv
        //verlegendes Krankenhaus
        // caseNumeric1-5, caseString1-5
        //fall.vorschlag flag
        // entgelte
//        long startTotal = System.currentTimeMillis();
//        long start = System.currentTimeMillis();
        //Falldaten
        pTransferCase.setGrouperModelId(pGrouperModelId);
        pTransferCase.setIkz(pCase.getCsHospitalIdent());
        pTransferCase.setCaseNumber(pCase.getCsCaseNumber());
        pTransferCase.setCaseId(pCase.getId());
        //isDRGCase        
        pTransferCase.setCaseType(getEnumId(pCase.getCsCaseTypeEn())); //DRG/PEPP und sw.
        pTransferCase.setDoctorIdent(pCase.getCsDoctorIdent());

        //Patient
        TPatient patient = pCase.getPatient();
        if (patient != null) {
//            pTransferCase.setSex(patient.getPatGenderEn() != null ? patient.getPatGenderEn().getId() : 0);
            //2019-01-30 DNi - Ticket #CPX-1378: Don't rely on patient's birth date! 
            //Try to use age in years/days before date of birth is used, because some 
            //interfaces like P21 give use only fuzzy information about the patient.
            if (patient.getPatDateOfBirth() != null && ((pDetails.getCsdAgeYears() == null || pDetails.getCsdAgeYears() == 0) && (pDetails.getCsdAgeDays() == null || pDetails.getCsdAgeDays() == 0))) {
                LOG.log(Level.WARNING, "Age in years is {0} and age in days is {1}, so I use patient birth date to group this case: {2} (case id {3}, details id {4})", new Object[]{pDetails.getCsdAgeYears(), pDetails.getCsdAgeDays(), pCase.getCaseKey(), pCase.getId(), pDetails.getId()});
                pTransferCase.setDateOfBirth(patient.getPatDateOfBirth());
            }
        }
        // CASE_DETAILS        
//        TCaseDetails csd = isLocal?hospitalCase.getCurrentLocal():hospitalCase.getCurrentExtern();
        pTransferCase.setSex(pDetails.getCsdGenderEn() != null ? pDetails.getCsdGenderEn().getId() : 0); //in Batchgrouping GrouperRequestLoader gender is 'U' is used if it is null! should be consistent!
        pTransferCase.setIsLocal(pDetails.getCsdIsLocalFl());
        // Kasse
        //grouperRequest.setKasse(csd.getCsdInsCompany());
        //Alter        
        pTransferCase.setAgeD(Objects.requireNonNullElse(pDetails.getCsdAgeDays(), 0));
        pTransferCase.setAgeY(Objects.requireNonNullElse(pDetails.getCsdAgeYears(), 0));
        // Aufnahmedatum        
        pTransferCase.setAdmissionDate(pDetails.getCsdAdmissionDate());
        // Entlassungsdatum        
        pTransferCase.setDischargeDate(pDetails.getCsdDischargeDate());
        //Aufnahmeanlass        
        pTransferCase.setAdmissionCause(getEnumId(pDetails.getCsdAdmCauseEn()));
        //Aufnahmegrund12        
        pTransferCase.setAdmissionReason12(getEnumId(pDetails.getCsdAdmReason12En()));//.getIdInt());
        //Aufnahmegrund34        
        pTransferCase.setAdmissionReason34(getEnumId(pDetails.getCsdAdmReason34En()));//.getIdInt());
        // aufenthalt ausserhalb
        pTransferCase.setNALOS(Objects.requireNonNullElse(pDetails.getCsdLeave(), 0));
        // Aufnahmegewicht
        pTransferCase.setWeight(Objects.requireNonNullElse(pDetails.getCsdAdmissionWeight(), 0));
        // Simulationsänderung an dem VWD
        pTransferCase.setLosAlteration(Objects.requireNonNullElse(pDetails.getCsdLosAlteration(), 0L));

        // Erbringungsart
        pTransferCase.setDepartmentType(getEnumId(pDetails.getCsdAdmodEn()));

        //Beatmungsstunden  
        pTransferCase.setRespirationLength(Objects.requireNonNullElse(pDetails.getCsdHmv(), 0));
        //Art der Behandlung
        pTransferCase.setInvoluntary(getEnumId(pDetails.getCsdAdmLawEn()));
        //Entlassungsgrund12
        pTransferCase.setDiscargeReason12(getEnumId(pDetails.getCsdDisReason12En()));
        //Entlassungsgrund3
        pTransferCase.setDiscargeReason3(getEnumId(pDetails.getCsdDisReason3En()));
//        Logger.getLogger(GrouperCommunication.class.getName()).log(Level.INFO, "set case data " + String.valueOf(System.currentTimeMillis() - start));
//        start = System.currentTimeMillis();
        //Abteilungen        
        List<TCaseDepartment> departments = pDetails.getSortedDepartments();
//        Logger.getLogger(GrouperCommunication.class.getName()).log(Level.INFO, "sort departments " + String.valueOf(System.currentTimeMillis() - start));
//        start = System.currentTimeMillis();

        for (TCaseDepartment dep : departments) {
            Set<TCaseOps> opses = dep.getCaseOpses();
            Set<TCaseIcd> icds = dep.getCaseIcds();
            Set<TCaseWard> wards = dep.getCaseWards();
            TransferDepartment tDep = new TransferDepartment(dep.getDepKey301(), dep.getDepcDisDate(), dep.getDepcAdmDate(), dep.getDepcIsAdmissionFl(), dep.getDepcIsDischargeFl(), dep.getDepcIsTreatingFl());
            pTransferCase.addDepartment(tDep);

            //Prozeduren            
            for (TCaseOps ops : opses) {
                if (!ops.getOpsIsToGroupFl()) {
                    continue;
                }
                // Korrektur des OPS - Datums, wenn OPS zu Abteilung gehoert und kein Datum oder kein Zeitstaempel hat, dann
                // OPSDatum = AdmDatum der Abteilung, nur für PEPP Fälle relevant

                Date opsDate = ops.getOpscDatum();
                if (pCase.getCsCaseTypeEn().equals(CaseTypeEn.PEPP)) {
                    if (opsDate == null || opsDate.before(dep.getDepcAdmDate())) {
                        opsDate = dep.getDepcAdmDate();
                    } else if (opsDate != null) {
                        if (dep.getDepcDisDate() != null && opsDate.after(dep.getDepcDisDate())) {
                            opsDate = dep.getDepcDisDate();
                        }
                    }
                }

                TransferOps tOps = new TransferOps(pOpsList == null ? ops.getId() : checkId(pOpsList, ops), ops.getOpscCode(), ops.getOpscLocEn() == null ? 0 : ops.getOpscLocEn().getId(),
                        opsDate);
                tDep.addOps(tOps);
                pTransferCase.addOps(tOps);
            }
            //Diagnosen            
            for (TCaseIcd icd : icds) {
                if (!icd.getIcdIsToGroupFl()) {
                    //AWi 07.04.2016, in Db TCaseIcd have no default Type, it returns null -> grouper explodes
                    if (icd.getIcdcTypeEn() != null) {
                        if (icd.getIcdcTypeEn().equals(IcdcTypeEn.Aufnahme)) {
                            // Aufnahmediagnose wird an GRouper übergeben wegen dem Regelkriterium

                            pTransferCase.addAdmissionDiagnose(icd.getId(), icd.getIcdcCode(), icd.getIcdcReftypeEn() == null ? 0 : icd.getIcdcReftypeEn().getId(), icd.getIcdcLocEn() == null ? 0 : icd.getIcdcLocEn().ordinal());
                        }
                    }
                    continue;
                }
                TransferIcd tIcd = new TransferIcd(pIcdList == null ? icd.getId() : checkId(pIcdList, icd),
                        icd.getIcdcCode(),
                        icd.getIcdcReftypeEn() == null ? 0 : icd.getIcdcReftypeEn().getId(),
                        icd.getIcdcLocEn() == null ? 0 : icd.getIcdcLocEn().ordinal(),
                        icd.getIcdcIsHdxFl(),
                        icd.getIcdcTypeEn() == null ? false : icd.getIcdcTypeEn().equals(IcdcTypeEn.Aufnahme));
                if (icd.getIcdcIsHdxFl()) {
                    pTransferCase.setPrincipalIcd(tIcd);
                }
                tDep.addIcd(tIcd);
                pTransferCase.addIcd(tIcd);
                if (icd.getIcdcReftypeEn() != null) {
                    if (icd.getIcdcReftypeEn().equals(IcdcRefTypeEn.Kreuz) || icd.getIcdcReftypeEn().equals(IcdcRefTypeEn.Zusatz)) {
                        TCaseIcd icdPrim = icd.getRefIcd();
                        if (icdPrim != null) {
                            try {
                                tIcd.setPrimIcd(icdPrim.getIcdcCode());
                            } catch (Exception ex) {
                                LOG.log(Level.SEVERE, "Yeah, there's a problem in this case: " + String.valueOf(pCase), ex);
                            }
                        }
                    }
                }
            }
            //Stationen            
            for (TCaseWard ward : wards) {
                tDep.addWard(ward.getWardcIdent());
            }
        }

//        Logger.getLogger(GrouperCommunication.class.getName()).log(Level.INFO, "set department data " + String.valueOf(System.currentTimeMillis() - start));
//        start = System.currentTimeMillis();
//        Logger.getLogger(GrouperCommunication.class.getName()).log(Level.INFO, "total fill grouper Request " + String.valueOf(System.currentTimeMillis() - startTotal));
    }

    private static <E extends AbstractEntity> long checkId(Map<E, Long> tmpList, E entity) {

        long retId = (long) tmpList.keySet().size() + 1;
        tmpList.put(entity, retId);
        return retId;
    }

    private static int getEnumId(CpxEnumInterface<?> pEnum) {
        return pEnum == null ? 0 : pEnum.getIdInt();
    }
}
