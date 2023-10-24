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
package de.lb.cpx.service.ejb;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseComment;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseMergeMapping;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.enums.CaseDetailsCancelReasonEn;
import de.lb.cpx.model.enums.CaseStatusEn;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.server.config.CpxServerConfigLocal;
import de.lb.cpx.server.dao.TCaseDao;
import de.lb.cpx.server.dao.TCaseDetailsDao;
import de.lb.cpx.server.dao.TCaseMergeMappingDao;
import de.lb.cpx.server.dao.TGroupingResultsDao;
import de.lb.cpx.server.dao.TPatientDao;
import de.lb.cpx.server.merge.CaseMergeBean;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.FlushModeType;

/**
 * implementation of the case merging bean provides methodes to start merging
 * process for case merging due readmission and back transfer
 * todo:backtransfer,grouping and setting grouping results .. should be dto to
 * transfer grouping result and merged case? Performance?
 *
 * provides server service and does simple validity checks
 *
 * @author wilde
 */
@Stateless
public class CaseMergingServiceBean implements CaseMergingServiceBeanRemote {

    private static final Logger LOG = Logger.getLogger(CaseMergingServiceBean.class.getName());
    @EJB
    private CaseMergeBean mergeBean;
    @EJB
    private TCaseDao caseDao;
    @EJB
    private TCaseDetailsDao caseDetailsDao;
    @EJB
    private SingleCaseGroupingEJBRemote groupingBean;
    @EJB
    private TCaseMergeMappingDao mergeDao;
    @EJB
    private TGroupingResultsDao resDao;
    @EJB
    private TPatientDao patDao;
    @EJB(name = "CpxServerConfig")
    private CpxServerConfigLocal cpxServerConfig;


    @Override
    public TCase getMergedCaseByReadmssion(List<TCase> pCases, boolean isSimul) throws CpxIllegalArgumentException {
        long start = System.currentTimeMillis();
        caseDao.changeFlushMode(FlushModeType.COMMIT); //formerly (< WF14/Hibernate 5.3): FlushMode.MANUAL
        if (!validedCases(pCases)) {
            throw new CpxIllegalArgumentException("Validation failed! Either list is null, contains not enough elements(at least 2), case types are not drg/pepp or cases do not have the same case type");
        }
        TCase merge = null;
        List<TCase> merged = new ArrayList<>();
        for (TCase cse : pCases) {
            merged.add(caseDao.merge(cse));
        }
        try {
            merge = mergeBean.getMergedCaseByReadmission(merged, isSimul);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(CaseMergingServiceBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        LOG.info("merge case in " + (System.currentTimeMillis() - start));
        return merge;
    }

    private boolean validedCases(List<TCase> pCases) {
        if (pCases == null) {
            return false;
        }
        if (pCases.size() < 2) {
            return false;
        }
        //get the first type of the list and check if all match
        //redundant check in object of index 0
        CaseTypeEn firstType = pCases.get(0).getCsCaseTypeEn();
        for (TCase cse : pCases) {
            if (!cse.getCsCaseTypeEn().equals(firstType)) {
                return false;
            }
        }
        Iterator<TCase> it = pCases.iterator();
        CaseTypeEn type = null;
        while (it.hasNext()) {
            TCase next = it.next();
            if (pCases.indexOf(next) == 0) {
                type = next.getCsCaseTypeEn();
                if (!isCaseTypeSupported(type)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public TGroupingResults performTempGrouping(TCase pCase, GDRGModel pModel) throws CpxIllegalArgumentException {
        return groupingBean.getTempGroupingResultsLocal(pCase, pModel);
    }
    
    @Override
    public TGroupingResults performTempGrouping4merge(TCase pCase, GDRGModel pModel) throws CpxIllegalArgumentException{
         return groupingBean.getTempGroupingResults4merge(pCase, pModel);   
    }

//
//    @Override
//    public List<TCaseMergeMapping> findAllViableCasesToMerge() {
//        return mergeDao.findAllViableEntries();
//    }
//
//    @Override
//    public Boolean checkDatabaseRequirements(GDRGModel pModel) {
//        Integer caseCount = caseDao.getCount();
//        Integer drgCount = resDao.getCountForModel(pModel, true);
//        return drgCount.equals(caseCount);
//    }
    
    @Override
    public Boolean checkDatabaseRequirements(GDRGModel pModel, CaseTypeEn pType) {
        Integer caseCount = caseDao.getCount4Type(pType);
        Integer caseNoHdxCount = caseDao.getCountCasesWithNoGrpRes4Type(Boolean.TRUE, pType);
        Integer grpResCount = resDao.getCountForModelAndType(pModel, true, pType);
        return grpResCount.equals(caseCount - caseNoHdxCount);
    }
    
    @Override
    public Boolean checkDatabaseRequirements4Patient(GDRGModel pModel, CaseTypeEn pType, Long pPatientId) {
        Integer caseCount = caseDao.getPatientCasesCount(pType, pPatientId);
        Integer caseNoHdxCount = caseDao.getCountCasesWithNoGrpRes4Type(Boolean.TRUE, pType, pPatientId);
        Integer grpResCount = resDao.getCountForModelAndType(pModel, true, pType, pPatientId);
        return grpResCount.equals(caseCount - caseNoHdxCount);
    }
    

    @Override
    public TCase persistMergedCase(Integer pMergeId, TCase pCase) {
        TCaseIcd md = mergeBean.getMd(pCase.getCurrentLocal());
//        pCase.getCurrentLocal().getAllIcds();
        if (md == null) {
            LOG.severe("Can not persist merge case, no md was set");
            return null;
        }
        TCaseDetails local = pCase.getCurrentLocal();
        caseDao.saveOrUpdate(pCase);
        md = mergeBean.getMd(local);
        local.setHdIcdCode(md.getIcdcCode());
        return pCase;
    }
    
//    
//    @Override
//    public Boolean cancelCases(List<TCase> pCases){
//        return cancelCases(pCases, null);
//    }
//

    private Boolean cancelCases(List<TCase> pCases, Long pBaseId) {
        for (TCase cse : pCases) {
            if(pBaseId != null &&  cse.getId() == pBaseId){
                continue;
            }
            cse.setCsCancellationReasonEn(true);
            //marke name as canceled due to unique constraint on caseNumber and hospitalIdent
            cse.setCsCaseNumber(cse.getCsCaseNumber() + "_c");
            caseDao.saveOrUpdate(cse);
            cse.getCurrentLocal().setCsdCancelReasonEn(CaseDetailsCancelReasonEn.MERGE);
        }
//        caseDao.flush();
        return true;
    }

    @Override
    public Long persistAndMergeById(Integer pIdent) { 
        List<TCase> cseList = mergeDao.findCasesByIdent(pIdent);
        if (cseList.size() < 2) {
            return null;
        }
        TCase merged = null;
        try {
            merged = getMergedCaseByReadmssion(cseList, false);
        } catch (CpxIllegalArgumentException ex) {
            Logger.getLogger(CaseMergingServiceBean.class.getName()).log(Level.SEVERE, "Merging case by readmission failed", ex);
        }
        if (merged == null) {
            return null;
        }
        LOG.info("merging complete, save merged case!");
        return updateMergedCase(merged, pIdent);

    }

    private Boolean cancel(Integer pMergeId, TCase pCase) {
        for (TCaseMergeMapping map : mergeDao.findByMergeId(pMergeId)) {
            map.setCaseByHoscId(pCase);
            mergeDao.saveOrUpdate(map);
        }
        return true;
    }

    private String removeLastOccurance(String pTarget, String pToRemove) {
        int indexOfLast = pTarget.lastIndexOf(pToRemove);
        String newStr = pTarget;
        if (indexOfLast >= 0) {
            newStr = pTarget.substring(0, indexOfLast);
        }
        return newStr;
    }

    @Override
    public Long saveMergedCase(TCase pMergedCase, Integer pIdent) {
        caseDao.changeFlushMode(FlushModeType.AUTO); //formerly (< WF14/Hibernate 5.3): FlushMode.ALWAYS
        pMergedCase.setCsCaseNumber(pMergedCase.getCsCaseNumber() + "_m");
//        cancelCases(mergeDao.findCasesByIdent(pIdent), pMergedCase.getId());
//        LOG.info("cases canceled");
//        TPatient pat = patDao.merge(pMergedCase.getPatient());
//        pMergedCase.setPatient(pat);
        TCase saved = persistMergedCase(pIdent, pMergedCase);
        LOG.info("merged case saved");
//        caseDao.flush();
//        saved.setCsCaseNumber(removeLastOccurance(saved.getCsCaseNumber(), "_m"));
        cancel(pIdent, saved);
        caseDao.saveOrUpdate(saved);
        LOG.info("clean up, merged case has id " + saved.getId());
        return saved.getId();
    }
    
    public Long updateMergedCase(TCase pMergedCase, Integer pIdent){
        // check, whether there is already any merged case with casenumber_m
        TCase oldMerge = caseDao.findCaseByNumber(pMergedCase.getCsCaseNumber() + "_m");
        if(oldMerge == null){
           return saveMergedCase(pMergedCase, pIdent);
       }
        TCaseIcd md = mergeBean.getMd(pMergedCase.getCurrentLocal());
        caseDao.changeFlushMode(FlushModeType.AUTO);
        // local case details are to attach to the existent case
        TCaseDetails newLocal = pMergedCase.getCurrentLocal();
        TCaseDetails oldLocal = oldMerge.getCurrentLocal();
        if(oldLocal != null){
            // set to not active
            oldLocal.setCsdIsActualFl(false);
            caseDetailsDao.flush();
        }
        newLocal.setCsdVersion(oldLocal.getCsdVersion() + 1);
        newLocal.setCaseDetailsByCsdParentId(oldLocal);
        newLocal.setCaseDetailsByCsdExternId(oldLocal.getCaseDetailsByCsdExternId());
        newLocal.setHospitalCase(oldMerge);
        oldMerge.setCurrentLocal(newLocal);
        oldMerge.setCsStatusEn(CaseStatusEn.NEW_VERS);//merged case?
        md = mergeBean.getMd(newLocal);
        newLocal.setHdIcdCode(md.getIcdcCode());
        cancel(pIdent, oldMerge);
        caseDao.saveOrUpdate(oldMerge);
        return oldMerge.getId();
    }
    
     @Override 
    public String checkHasMerged(Integer pIdent){
         List<TCase> cseList = mergeDao.findCasesByIdent(pIdent);
        if (cseList.size() < 2) {
            return null;
        }
       cseList = CaseMergeBean.sortCases4Merge(cseList);
       String mergeNr = cseList.get(0).getCsCaseNumber();
       if(caseDao.findCaseByNumber(mergeNr + "_m") != null){
           return mergeNr;
       }
       return null;
    }

    @Override
    public Boolean isCaseTypeSupported(CaseTypeEn pCsType) {
        switch (pCsType) {
            case DRG:
                return true;
            case PEPP:
                return true;
            default:
                return false;
        }
    }


    @Override
    public List<TCaseMergeMapping> findAllViableCasesForType(CaseTypeEn pGrpresType,  GDRGModel pGrouperModel, Long pPatientId) {
        return mergeDao.findViableEntriesForGrpresType(pGrpresType, pGrouperModel, pPatientId);
    }

    @Override
    public List<TCaseMergeMapping> findNotMergingCasesForType(CaseTypeEn grpresType, GDRGModel grouperModel, Long pPatientId, List<Long> csIds) {
        return mergeDao.findNotMergingCasesForType(grpresType, grouperModel, pPatientId, csIds);
    }
    
    @Override
    public boolean isSaveCaseMergingAllowed(){
        return cpxServerConfig.getDoMergeSave();
    }
    
    public List<String> getCaseNumbers4CanceledCase4Merge(long pCaseId){
         return mergeDao.getCaseNumbers4CanceledCase4Merge(pCaseId);
    }

}
