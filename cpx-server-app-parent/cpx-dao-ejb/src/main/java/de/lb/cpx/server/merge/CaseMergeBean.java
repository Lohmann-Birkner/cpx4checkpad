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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.merge;

import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseWard;
import de.lb.cpx.model.enums.AdmissionCauseEn;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.DischargeReasonEn;
import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import org.hibernate.Hibernate;

/**
 * Case merging bean provides service to merge cases by readmission or
 * backtransfer
 *
 * @author wilde Note: Simple class does NOT do validity checks, must be done
 * before execution of the methode Keep in Mind, that references of the md in
 * merged case are not set in current local! it needs to be stored after saving
 * the case, otherwise is id of the icd 0 and you will find nothing with that
 * reference, otherwise datamodel should store object reference
 *
 */
@Stateless
public class CaseMergeBean {

    private static final Logger LOG = Logger.getLogger(CaseMergeBean.class.getName());

    private final LinkedHashMap<String, MdHelper> diagnosisMap = new LinkedHashMap<>();

    public static List<TCase> sortCases4Merge(List<TCase> pCases){
        List<TCase> cases = new ArrayList<>(pCases);
        cases.sort(new Comparator<TCase>() {
            @Override
            public int compare(TCase t, TCase t1) {
                if(t1.getCsHospitalIdent().equals(t.getCsHospitalIdent())){
                    TCaseDetails local1 = t.getCurrentLocal();
                    TCaseDetails local2 = t1.getCurrentLocal();
                    return local1.getCsdAdmissionDate().compareTo(local2.getCsdAdmissionDate());
                }
                else return t.getCsHospitalIdent().compareTo(t1.getCsHospitalIdent());
            }
        });
        return cases;
    }
    /**
     * merge cases by readmission
     *
     * @param pCases list of cases to merge
     * @return merged case
     * @throws java.lang.CloneNotSupportedException thrown if clone is not
     * supported
     */
    public TCase getMergedCaseByReadmission(List<TCase> pCases, boolean isSimul) throws CloneNotSupportedException {
        //risky .. need to be refactored
        diagnosisMap.clear();
        //sort cases due to admission date
        List<TCase> cases =  sortCases4Merge(pCases);
//        cases.sort(new Comparator<TCase>() {
//            @Override
//            public int compare(TCase t, TCase t1) {
//                TCaseDetails local1 = t.getCurrentLocal();
//                TCaseDetails local2 = t1.getCurrentLocal();
//                return local1.getCsdAdmissionDate().compareTo(local2.getCsdAdmissionDate());
//            }
//        });
        //clone and remove 0 element 
        TCase base = cases.get(0);
        Hibernate.initialize(base.getPatient().getInsurances());
        Hibernate.initialize(base.getPatient().getPatientDetailList());
//        TCase mergeCase = isSimul?(TCase) base.cloneWithoutIds(ClientManager.getCurrentCpxUserId()):(TCase) base;
        TCase mergeCase = (TCase) base.cloneWithoutIds(ClientManager.getCurrentCpxUserId());
        CaseTypeEn type = mergeCase.getCsCaseTypeEn();
        //create new local version from current local 
        TCaseDetails currentLocal = mergeCase.getCurrentLocal();
        TCaseDetails mergedVersion = currentLocal.cloneWithoutIds(ClientManager.getCurrentCpxUserId());
        currentLocal.setCsdIsActualFl(false);
        mergedVersion.setCsdVersion(currentLocal.getCsdVersion() + 1);
        mergedVersion.setHospitalCase(mergeCase);
        mergedVersion.setComment(Lang.toDate(new Date()) + ";" + ClientManager.getCurrentCpxUserName() + "; Fallzusammenführung ////");
        mergeCase.setCurrentLocal(mergedVersion);
        checkMD(mergeCase.getCurrentLocal());
        mergeCase.getCurrentLocal().setCsdHmv(Objects.requireNonNullElse(mergeCase.getCurrentLocal().getCsdHmv(), 0));
        mergeCase.getCurrentLocal().setCsdAdmissionWeight(Objects.requireNonNullElse(mergeCase.getCurrentLocal().getCsdAdmissionWeight(), 0));
        cases.remove(0);

        //get current locals for other cases
        List<TCaseDetails> locals = new ArrayList<>();
        for (TCase cse : cases) {
            locals.add(cse.getCurrentLocal());
        }
        //copy departments/icd/ops sort by admission date
        Iterator<TCaseDetails> it = locals.stream().sorted((TCaseDetails t, TCaseDetails t1) -> t.getCsdAdmissionDate().compareTo(t1.getCsdAdmissionDate())).iterator();
        boolean hasTransfer = AdmissionCauseEn.isTransfer(mergeCase.getCurrentLocal().getCsdAdmCauseEn()) 
                || DischargeReasonEn.isTransfer(mergeCase.getCurrentLocal().getCsdDisReason12En()); 
        while (it.hasNext()) {
            TCaseDetails next = it.next();

            Integer mergeHmv = mergeCase.getCurrentLocal().getCsdHmv();
            Integer nextHmv = next.getCsdHmv();
            hasTransfer = hasTransfer || AdmissionCauseEn.isTransfer(next.getCsdAdmCauseEn()) 
                || DischargeReasonEn.isTransfer(next.getCsdDisReason12En()); 
            mergeCase.getCurrentLocal().setCsdHmv((mergeHmv != null ? mergeHmv : 0) + (nextHmv != null ? nextHmv : 0));
            Integer mergeLeave = mergeCase.getCurrentLocal().getCsdLeave();
            Integer nextLeave = next.getCsdLeave();
            mergeCase.getCurrentLocal().setCsdLeave((mergeLeave != null ? mergeLeave : 0) + (nextLeave != null ? nextLeave : 0));

            Integer mergeLosintensiv = mergeCase.getCurrentLocal().getCsdLosIntensiv();
            Integer nextLosIntensiv = next.getCsdLosIntensiv();
            mergeCase.getCurrentLocal().setCsdLosIntensiv((mergeLosintensiv != null ? mergeLosintensiv : 0) + (nextLosIntensiv != null ? nextLosIntensiv : 0));
//            copyFees
            mergeCase.getCurrentLocal().getCaseFees().addAll(next.getCaseFees());
            copyDepartmentData(mergeCase.getCurrentLocal(), next, type);
            //when last, then set discharge date and reasons from last case
            if (!it.hasNext()) {
                mergeCase.getCurrentLocal().setCsdDisReason12En(next.getCsdDisReason12En());
                mergeCase.getCurrentLocal().setCsdDisReason3En(next.getCsdDisReason3En());
            }

        }
        mergeCase.getCurrentLocal().setHasTransferFlag4Merge(hasTransfer);
        mergeCase.getCurrentLocal().setDepartmentFlags();
//        computeLeave(mergeCase.getCurrentLocal());
        TCaseIcd md = checkForMD(diagnosisMap, type);
        updateMd(md, mergeCase.getCurrentLocal());
        removeDoubledIcds(mergeCase.getCurrentLocal());
        //AWi-20171127:
        //not the best way, but do not want to mess with clone methode for icds
        removeRefFromIcds(mergeCase.getCurrentLocal());
        return mergeCase;
    }

//    private void computeLeave(TCaseDetails pDetails) {
//        int leave = 0;
////        long los = 0;
//        for (TCaseDepartment dep : pDetails.getCaseDepartments()) {
//            if (dep.isPseudo()) {
//                leave = Lang.toDaysBetween(dep.getDepcAdmDate(), dep.getDepcDisDate(), 1) + leave;
////            }else{
////                los = Lang.toDaysBetween(dep.getDepcAdmDate(), dep.getDepcDisDate(),1) + los;
//            }
//        }
//        pDetails.setCsdLeave(leave);
////        pDetails.setCsdLos(los);
//    }

    private void copyDepartmentData(TCaseDetails mergeDetails, TCaseDetails toCopy, CaseTypeEn type) throws CloneNotSupportedException {
        List<TCaseDepartment> copyDepartments = new ArrayList<>();
        // by pepp schould always add pseudo: for 2020 there was a different usage of gaps between departments to to calculate the departmentand ops validity  in grouper
        // exception only when there is no time gap between two cases
        if (type.equals(CaseTypeEn.PEPP) && mergeDetails.getCsdDischargeDate().before(toCopy.getCsdAdmissionDate()) 
                || shouldAddPseudo(mergeDetails.getCsdDischargeDate(), toCopy.getCsdAdmissionDate())) {
            TCaseDepartment pseudo = createPseudo(mergeDetails.getCsdDischargeDate(), toCopy.getCsdAdmissionDate());
            pseudo.setCaseDetails(mergeDetails);
            copyDepartments.add(pseudo);
            // add leave from pseudo - duration
            
            Integer leave = mergeDetails.getCsdLeave();
            Integer gap = calculatePseudoDuration(type, mergeDetails.getCsdDischargeDate(), toCopy.getCsdAdmissionDate());
            if(type.equals(CaseTypeEn.DRG) 
                    && Lang.setTimeTo0000(mergeDetails.getCsdAdmissionDate()).equals(Lang.setTimeTo0000(mergeDetails.getCsdDischargeDate()))
                    && gap > 0){
                gap--;
            }
            mergeDetails.setCsdLeave((leave != null ? leave : 0) + gap);
            // correcture for the first drg case when admDate = disdate
            
        }
        TCaseDetails copy = toCopy.cloneWithoutIds(ClientManager.getCurrentCpxUserId());
        //check and set md for current case version
        checkMD(copy);
        Iterator<TCaseDepartment> it = copy.getCaseDepartments().iterator();
        while (it.hasNext()) {
            TCaseDepartment department = it.next();
            department.setCaseDetails(mergeDetails);
            copyDepartments.add(department);
        }
        mergeDetails.getCaseDepartments().addAll(copyDepartments);
        mergeDetails.setCsdDischargeDate(toCopy.getCsdDischargeDate());

    }

    private int calculatePseudoDuration(CaseTypeEn type, Date pStartDate, Date pEndDate) {
        long between = Lang.toDaysBetween(Lang.setTimeTo0000(pStartDate), Lang.setTimeTo0000(pEndDate));
        if(CaseTypeEn.PEPP.equals(type)){
// for pepp cases days of start and enddate of pseudo department are not  included          
            between -= 2;
        }
        if(between < 0){
            between = 0;
        }
        return (int)between;
    }
    

    private boolean shouldAddPseudo( Date pStartDate, Date pEndDate) {
         long between = Lang.toDaysBetween(Lang.setTimeTo0000(pStartDate), Lang.setTimeTo0000(pEndDate));
        if (between >= 1) {
            return true;
        }
        return false;
    }
    //todo:check correct computation of the pseudo department

    private TCaseDepartment createPseudo(Date pAdmDate, Date pDisDate) {
        TCaseDepartment pseudo = new TCaseDepartment();
        pseudo.setDepShortName("0002");
        pseudo.setDepKey301("0002");
        pseudo.setDepcAdmDate(pAdmDate);
        pseudo.setDepcDisDate(pDisDate);
        return pseudo;
    }

    public TCaseIcd checkForMD(Map<String, MdHelper> map, CaseTypeEn type) {
        if (map.isEmpty()) {
            return null;
        }
        switch (type) {
            case DRG:
                return checkMDForDrg(map);
            case PEPP:
                return checkMDForPepp(map);
            default:
                LOG.warning("Can not detect Md for unknown casetype: " + type.name() + "! Remove md!");
                return deleteMd(map);
        }
    }

    private void checkMD(TCaseDetails pDetails) {
        long losCase = Lang.toDaysBetween(pDetails.getCsdAdmissionDate(), pDetails.getCsdDischargeDate());
        TCaseIcd mdCase = getMd(pDetails);
        if (mdCase == null) {
            return;
        }
        if (!diagnosisMap.containsKey(generateKey(mdCase))) {
            diagnosisMap.put(generateKey(mdCase), new MdHelper(mdCase, losCase));
            mdCase.setIcdcIsHdxFl(false);
        } else {
            losCase = losCase + diagnosisMap.get(generateKey(mdCase)).getLos();
            mdCase.setIcdcIsHdxFl(false);
            diagnosisMap.get(generateKey(mdCase)).setLos(losCase);
        }
    }

    private TCaseIcd checkMDForDrg(Map<String, MdHelper> map) {
        //get first item
        TCaseIcd md = map.get(map.keySet().iterator().next()).getIcd();
        md.setIcdcIsHdxFl(true);
        return md;
    }

    private TCaseIcd checkMDForPepp(Map<String, MdHelper> map) {
        MdHelper los = null;
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            MdHelper next = map.get(it.next());
            if (los == null) {
                los = next;
            } else {
                if (next.getLos() > los.getLos()) {
                    los = next;
                }
            }
        }
        if (los == null) {
            LOG.log(Level.WARNING, "los is null!");
            return null;
        }
        los.getIcd().setIcdcIsHdxFl(true);
        return los.getIcd();
    }

    private String generateKey(TCaseIcd pIcd) {
        return pIcd.getIcdcCode();
    }

    public TCaseIcd getMd(TCaseDetails pDetails) {
        for (TCaseDepartment dep : pDetails.getCaseDepartments()) {
            for (TCaseIcd icd : dep.getCaseIcds()) {
                if (icd.getIcdcIsHdxFl()) {
                    return icd;
                }
            }
        }
        return null;
    }

    private TCaseIcd deleteMd(Map<String, MdHelper> map) {
//        setMdData(merge, null);
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            MdHelper next = map.get(it.next());
            next.getIcd().setIcdcIsHdxFl(false);
        }
        return null;
    }

    private boolean updateMd(TCaseIcd md, TCaseDetails currentLocal) {
        md.setIcdcIsHdxFl(false);
        for (TCaseDepartment dep : currentLocal.getCaseDepartments()) {
            for (TCaseIcd icd : dep.getCaseIcds()) {
                if (icd.equals2object(md)) {
                    icd.setIcdcIsHdxFl(true);
                    return true;
                }
            }
        }
        return false;
    }

    private void removeDoubledIcds(TCaseDetails pDetails) {
        List<TCaseIcd> tmpIcds = new ArrayList<>();
        for (TCaseDepartment dep : pDetails.getCaseDepartments()) {
            //handle wards
            if (dep.getCaseWards().isEmpty()) {
                removeDouble(dep.getCaseIcds().iterator(), tmpIcds);
            } else {
                for (TCaseWard ward : dep.getCaseWards()) {
                    removeDouble(ward.getCaseIcds().iterator(), tmpIcds);
                }
            }
        }
//        pDetails.getCaseDepartments();
    }

    private void removeDouble(Iterator<TCaseIcd> it, List<TCaseIcd> tmpIcds) {
        while (it.hasNext()) {
            TCaseIcd next = it.next();
            //check if icd is stored already if not add otherwise remove from list of the department 
            if (!checkContainsIcd(tmpIcds, next)) {
                tmpIcds.add(next);
            } else {
                it.remove();
            }
        }
    }

    private void removeRefFromIcds(TCaseDetails pDetails) {
        for (TCaseDepartment dep : pDetails.getCaseDepartments()) {
            //handle wards
            if (dep.getCaseWards().isEmpty()) {
                removeRefs(dep.getCaseIcds().iterator());
            } else {
                for (TCaseWard ward : dep.getCaseWards()) {
                    removeRefs(ward.getCaseIcds().iterator());
                }
            }
        }
    }

    private void removeRefs(Iterator<TCaseIcd> it) {
        while (it.hasNext()) {
            TCaseIcd next = it.next();
            next.setIcdcReftypeEn(null);
            next.setRefIcd(null);
        }
    }

    private boolean checkContainsIcd(List<TCaseIcd> pIcds, TCaseIcd pIcd) {
        if (pIcd.getIcdcIsHdxFl()) {
            return false;
        }
        for (TCaseIcd icd : pIcds) {

            if (icd.simulationEquals(pIcd)) {
                return true;
            }
        }
        return false;
    }

}
