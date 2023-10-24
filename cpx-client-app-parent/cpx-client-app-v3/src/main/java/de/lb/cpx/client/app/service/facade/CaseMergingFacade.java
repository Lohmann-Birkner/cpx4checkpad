/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.service.facade;

import de.checkpoint.drg.GDRGModel;
import de.checkpoint.ruleGrouper.CRGRule;
import de.lb.cpx.client.app.job.fx.casemerging.model.CaseMergeContent;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog;
import de.lb.cpx.client.core.model.catalog.CpxBaserateCatalog;
import de.lb.cpx.client.core.model.catalog.CpxHospital;
import de.lb.cpx.client.core.model.catalog.CpxHospitalCatalog;
import de.lb.cpx.client.core.model.catalog.CpxInsuranceCompany;
import de.lb.cpx.client.core.model.catalog.CpxInsuranceCompanyCatalog;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.grouper.model.dto.IcdOverviewDTO;
import de.lb.cpx.grouper.model.dto.OpsOverviewDTO;
import de.lb.cpx.grouper.model.util.IcdDtoHelper;
import de.lb.cpx.grouper.model.util.OpsDtoHelper;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseDrg;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseMergeMapping;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.TInsurance;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.model.enums.AdmissionByLawEn;
import de.lb.cpx.model.enums.AdmissionCauseEn;
import de.lb.cpx.model.enums.AdmissionReason2En;
import de.lb.cpx.model.enums.AdmissionReasonEn;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.DrgCorrTypeEn;
import de.lb.cpx.model.enums.LocalisationEn;
import de.lb.cpx.model.enums.SupplFeeTypeEn;
import de.lb.cpx.server.catalog.service.ejb.RuleServiceBeanRemote;
import de.lb.cpx.service.ejb.CaseMergingServiceBeanRemote;
import de.lb.cpx.service.ejb.ReadmissionServiceEJBRemote;
import de.lb.cpx.service.ejb.SingleCaseEJBRemote;
import de.lb.cpx.service.ejb.SingleCaseGroupingEJBRemote;
import de.lb.cpx.shared.dto.rules.CpxSimpleRuleDTO;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * CaseMerging Facade to bundle server access and provide test data
 *
 * @author wilde
 */
public class CaseMergingFacade {

    public void setCurrentPatient(TPatient pCurrentPatient) {
        currentPatient = pCurrentPatient;
        if(currentPatient != null){
            emptyMergeCaseList();
        }
    }


    public enum MERGELIST_TYPE{ALL, PATIENT};
    
    private static final Logger LOG = Logger.getLogger(CaseMergingFacade.class.getName());

    private boolean testMode = false;
    private ObservableList<TCaseMergeMapping> mergeCases = FXCollections.observableArrayList();
//    private ObservableList<TCaseMergeMapping> mergeCases_PEPP = FXCollections.observableArrayList();
    private List<TCaseIcd> testIcds;
    private List<TCaseOps> testOpses;
    private CpxHospitalCatalog hospitalCatalog;
    private CpxInsuranceCompanyCatalog insuranceCatalog;
    private final EjbProxy<SingleCaseEJBRemote> caseBean;
    private final EjbProxy<CaseMergingServiceBeanRemote> mergeBean;
    private final EjbProxy<ReadmissionServiceEJBRemote>readmissionBean;
    private final EjbProxy<RuleServiceBeanRemote> ruleBean;
    private final EjbProxy<SingleCaseGroupingEJBRemote> grouperServiceBean;
    private final CpxBaserateCatalog baserateCatalog;
    private final Map<Long, TPatient> patientCacheMap = new HashMap<>();
    private CaseTypeEn grpresType;
    private TCase mergedCase;
    private MERGELIST_TYPE mergeListType;
    private TPatient currentPatient;


    /**
     * creates new instance testmode false
     *
     * @param pCaseType case type, drg pepp
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException illegal argument
     */
    public CaseMergingFacade(CaseTypeEn pCaseType) throws CpxIllegalArgumentException {
        this(false, pCaseType, MERGELIST_TYPE.ALL);
    }
    
        public CaseMergingFacade(CaseTypeEn pCaseType, MERGELIST_TYPE pType) throws CpxIllegalArgumentException {
             this(false, pCaseType, pType);
        }
    /**
     * create new instance with test mode
     *
     * @param pTestMode indicator if testmode true false
     * @param pCaseType case type
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException illegal argument
     */
    public CaseMergingFacade(boolean pTestMode, CaseTypeEn pCaseType, MERGELIST_TYPE pType) throws CpxIllegalArgumentException {
        testMode = pTestMode;
        grpresType = pCaseType;
        mergeListType = pType;
        if (pTestMode) {
            setUpMergingList();
        }

        caseBean = Session.instance().getEjbConnector().connectSingleCaseBean();
        mergeBean = Session.instance().getEjbConnector().connectCaseMergingServiceBean();
        ruleBean = Session.instance().getEjbConnector().connectToRuleServiceBean();
        grouperServiceBean = Session.instance().getEjbConnector().connectSingleCaseGroupingBean();
        readmissionBean = Session.instance().getEjbConnector().connectReadmissionServiceBean();

        hospitalCatalog = CpxHospitalCatalog.instance();
        insuranceCatalog = CpxInsuranceCompanyCatalog.instance();
        baserateCatalog = CpxBaserateCatalog.instance();
    }

    /**
     * @return check if facade is in testmode
     */
    public boolean isTestMode() {
        return testMode;
    }

    /**
     * @return type (dr,pepp,etc) that facade is initialized with
     */
    public CaseTypeEn getGrpresType() {
        return grpresType;
    }

    /**
     * @return get the cases that can be merged In Database all Entries that are
     * stored in T_CASE_MERGE_MAPPING and where HOSC_ID is null and grpres type
     * is as initialized
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException invalid argument
     */
    public List<TCaseMergeMapping> findMergingCases() throws CpxIllegalArgumentException { 

        if(currentPatient == null ){
            if(mergeListType.equals(MERGELIST_TYPE.ALL)){
                return findAllMergingCases(null);
            }
            return new ArrayList<>();
        }else{
            return findAllCases4PatientAndType(currentPatient);
        }
    }
    
    
    private List<TCaseMergeMapping> findAllCases4PatientAndType(TPatient pPatient)  throws CpxIllegalArgumentException {
        if(pPatient == null){
            return new ArrayList<>();
        }
        List<TCaseMergeMapping> newMapping = findAllMergingCases(pPatient.getId());
        List<Long> csIds  = new ArrayList<>();
        for(TCaseMergeMapping mp: newMapping){
            csIds.add(mp.getCaseByMergeMemberCaseId().getId());
        }
        // get all patient cases
        List<TCaseMergeMapping> notMergedCases = new ArrayList<>();
        if(pPatient.getCases().size() > csIds.size()){
//            CpxClientConfig conf = CpxClientConfig.instance();
//            GDRGModel grouperModel = conf.getSelectedGrouper();
//            for(TCase cs: currentPatient.getCases()){
//                if(csIds.contains(cs.getId()) || !cs.getCsCaseTypeEn().equals(grpresType)){
//                    continue;
//                }
//                TGroupingResults gr = cs.getCurrentLocal().getGroupingResult2Model(grouperModel, grouperModel.equals(GDRGModel.AUTOMATIC));
//                if(gr == null){
//                    continue;
//                }
//                TCaseMergeMapping tmpMap = new TCaseMergeMapping();
//                tmpMap.setCaseByMergeMemberCaseId(cs);
//                tmpMap.setGrpresId(gr);
//                notMergedCases.add(tmpMap);
//
//            }
//            notMergedCases.sort((TCaseMergeMapping o1, TCaseMergeMapping o2) ->
//                    o1.getCaseByMergeMemberCaseId().getCurrentLocal().getCsdAdmissionDate()
//                    .compareTo(o2.getCaseByMergeMemberCaseId().getCurrentLocal().getCsdAdmissionDate()));
//
            notMergedCases = findNotMergingCases(pPatient.getId(), csIds);
        }
        notMergedCases.addAll(newMapping);

        return notMergedCases;
    }

    
    private List<TCaseMergeMapping> findAllMergingCases(Long pPatientId) throws CpxIllegalArgumentException {
        List<TCaseMergeMapping> newMapping = new ArrayList<>();
         CpxClientConfig conf = CpxClientConfig.instance();
         GDRGModel grouperModel = conf.getSelectedGrouper();
        if (testMode) {
            newMapping.addAll(setUpMergingList());
        } else {
//                newMapping.addAll(mergeBean.findAllViableCasesToMerge());
            newMapping.addAll(mergeBean.get().findAllViableCasesForType(grpresType, grouperModel, pPatientId));
        }
        newMapping.sort(Comparator.comparing(TCaseMergeMapping::getMrgMergeIdent)
                .thenComparing((TCaseMergeMapping o1, TCaseMergeMapping o2) -> {
                    Date adm1 = o1.getCaseByMergeMemberCaseId().getCurrentLocal().getCsdAdmissionDate();
                    Date adm2 = o2.getCaseByMergeMemberCaseId().getCurrentLocal().getCsdAdmissionDate();
                    return adm1.compareTo(adm2);
                }));
//            newMapping.sort(new Comparator<TCaseMergeMapping>() {
//            @Override
//            public int compare(TCaseMergeMapping o1, TCaseMergeMapping o2) {
//                return o1.getMrgMergeIdent().compareTo(o2.getMrgMergeIdent());
//            }
//        });

        return newMapping;
    }
    
    private List<TCaseMergeMapping> findNotMergingCases(Long pPatientId, List<Long> csIds) throws CpxIllegalArgumentException {
        List<TCaseMergeMapping> notMergingCases = new ArrayList<>();
         CpxClientConfig conf = CpxClientConfig.instance();
         GDRGModel grouperModel = conf.getSelectedGrouper(); 
          notMergingCases.addAll(mergeBean.get().findNotMergingCasesForType(grpresType, grouperModel, pPatientId, csIds));
          notMergingCases.sort((TCaseMergeMapping o1, TCaseMergeMapping o2) ->
                    o1.getCaseByMergeMemberCaseId().getCurrentLocal().getCsdAdmissionDate()
                    .compareTo(o2.getCaseByMergeMemberCaseId().getCurrentLocal().getCsdAdmissionDate()));
          
        return notMergingCases;
    }

    /**
     * reload list and returns updated values
     *
     * @return new list
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException illegal argument
     */
    public ObservableList<TCaseMergeMapping> reloadMergeCaseList() throws CpxIllegalArgumentException {
        mergeCases.setAll(findMergingCases());
        return mergeCases;
    }

    public ObservableList<TCaseMergeMapping> emptyMergeCaseList() {
        mergeCases.setAll(new ArrayList<>());
        return mergeCases;
    }

    /**
     * @return get observable list of current case merge mapping list
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException illegal argument
     */
    public ObservableList<TCaseMergeMapping> getObservableMergingCases() throws CpxIllegalArgumentException {
        if (mergeCases.isEmpty()) {
            reloadMergeCaseList();
        }
        return mergeCases;
    }

    /**
     * load patient by id, results are cached
     *
     * @param pPatientId patient id
     * @return Patient entity
     */
    public TPatient loadPatient(Long pPatientId) {
        if (!patientCacheMap.containsKey(pPatientId)) {
            patientCacheMap.put(pPatientId, caseBean.get().findPatientForId(pPatientId));
        }
        return patientCacheMap.get(pPatientId);
    }

    /**
     * clear facade cache
     */
    public void clearCache() {
        patientCacheMap.clear();
    }

    /**
     * @return indicator if mergeCases are present in the client
     */
    public boolean mergeCasesExists() {
        if (testMode) {
            return true;
        }
        return mergeCases.isEmpty();
    }

    /**
     * @param pItems remove items from mergeCases observable list
     */
    public void removeFromMergeCases(TCaseMergeMapping... pItems) {
        mergeCases.removeAll(pItems);
    }

    /**
     * @param pItems add items to mergeCases observable list
     */
    public void addMergeCases(TCaseMergeMapping... pItems) {
        mergeCases.addAll(pItems);
    }

    /**
     * merge cases by merge id
     *
     * @param pMergeId merge case to merge
     * @return merged case entity
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException invalid argument
     * @throws IllegalArgumentException thrown if mergings failed, Reasons can
     * be insufficient number of cases for merge id (at least 2), cases have
     * different case type, etc. Warning: All ids are set to 0L
     */
    public TCase mergeById(Integer pMergeId) throws CpxIllegalArgumentException {
        Long start = System.currentTimeMillis();
//        if(testMode){
        List<TCase> caseList = new ArrayList<>();

        //todo: maybe stream api?
        for (TCaseMergeMapping mapping : mergeCases) {
            if (mapping.getMrgMergeIdent().equals(pMergeId)) {
                caseList.add(mapping.getCaseByMergeMemberCaseId());
            }
        }
        if (caseList.size() < 2) {
            return null;
        }

        TCase merged = mergeBean.get().getMergedCaseByReadmssion(caseList, true);
        LOG.info("time to merge " + (System.currentTimeMillis() - start));
        mergedCase = merged;
        return merged;
    }

    /**
     * dummy methode to store ops entity - does nothing
     *
     * @param pOps ops entity
     */
    public void saveOpsEntity(TCaseOps pOps) {
        //TODO
//        if(!testMode){
//            caseBean.saveTCaseOps(pOps);
//        }
    }

    /**
     * dummy methode to store icd entity - does nothing
     *
     * @param pIcd icd entity
     */
    public void saveIcdEntity(TCaseIcd pIcd) {

    }

    /**
     * @param pMergeId merge id of the TCaseMergeMapping items
     * @return List of all TCaseMergeMapping Entries in mergeCases Observable
     * List
     */
    public List<TCaseMergeMapping> getCaseMergeByMergeId(int pMergeId) {
        return mergeCases.stream().filter(new Predicate<TCaseMergeMapping>() {
            @Override
            public boolean test(TCaseMergeMapping t) {
                return t.getMrgMergeIdent() == pMergeId;
            }
        }).collect(Collectors.toList());
    }

    /**
     * @param pCase case object to use as content
     * @return CaseMergeContent Object for case
     */
    public CaseMergeContent createMergeContent(TCase pCase) {
        return new CaseMergeContent(pCase, this);
    }

    /**
     * @param pCase case object to use as content
     * @param pGrpResult grouping result of merged case
     * @return CaseMergeContent Object for case
     */
    public CaseMergeContent createMergeContent(TCase pCase, TGroupingResults pGrpResult) {
        return new CaseMergeContent(pCase, pGrpResult, this);
    }

    /**
     * perform Tempgrouping for case with selected Groupermodel
     *
     * @param pCase case to group
     * @return Temp Grouping Result, not stored in database
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException invalid argument
     */
    public TGroupingResults getGrouperResult(TCase pCase) throws CpxIllegalArgumentException {
        if (testMode) {
            return getTestGroupingResult();
        }
        return mergeBean.get().performTempGrouping4merge(pCase, CpxClientConfig.instance().getSelectedGrouper());
    }
//    public TGroupingResults getGrouperResult(Long pCaseId) throws CpxIllegalArgumentException {
//        if (testMode) {
//            return getTestGroupingResult();
//        }
//        return mergeBean.performTempGrouping(pCaseId, CpxClientConfig.instance().getSelectedGrouper());
//    }

    /**
     * find the detected rules for the grouper result and admissionDate
     *
     * @param pAdmDate admissiondate of the case
     * @param pGroupingResults grouping results object
     * @return DTO list for all detected rules with catalog data for
     * admissionDate
     */
    public List<CpxSimpleRuleDTO> findDetectedRules(Date pAdmDate, TGroupingResults pGroupingResults) {
//        if (testMode) {
//            return getTestDetectedRules();
//        }
        if (pGroupingResults.getId() != 0) {
            return ruleBean.get().findRulesAdmissionDateAndGroupingId(pAdmDate, pGroupingResults.getId());
        }
        return ruleBean.get().findRulesAsList(Lang.toYear(pAdmDate), new ArrayList<>(pGroupingResults.getCheckResults()));
    }

    /**
     * find the detected rules for the grouper result and admissionDate
     *
     * @param pAdmDate admissiondate of the case
     * @param pGroupingResults grouping results object
     * @return DTO list for all detected rules with catalog data for
     * admissionDate
     */
    public List<CpxSimpleRuleDTO> findSortedDetectedRules(Date pAdmDate, TGroupingResults pGroupingResults) {
        List<CpxSimpleRuleDTO> dtos = findDetectedRules(pAdmDate, pGroupingResults);
        if (dtos != null) {
            dtos.sort(Comparator.comparingInt(CpxSimpleRuleDTO::getRuleTypeSeverity).reversed().thenComparing(CpxSimpleRuleDTO::getErrorTyp));
        }
        return dtos;
    }

    /**
     * create IcdOverviewDTO objects for the merging
     *
     * @param pDetails merged caseDetails
     * @param param list of all case versions displayed
     * @return list of computed overview TODO: Refactor to reflect to be
     * discussed behavior
     */
    public List<IcdOverviewDTO> getAllIcdCodes(TCaseDetails pDetails, List<Long> param) {
        if (testMode) {
            //TODO - general idea .. get list from server for cases, merge list with current List stored for merged case
            List<IcdOverviewDTO> caseIcds = getTestIcdOverviewDTOs();
            List<TCaseIcd> mergedCaseIcds = new ArrayList<>(getTestIcds());
            for (IcdOverviewDTO dto : caseIcds) {
                Iterator<TCaseIcd> itMc = mergedCaseIcds.iterator();
                while (itMc.hasNext()) {
                    TCaseIcd next = itMc.next();
                    if (dto.getIcdCode().equals(next.getIcdcCode())) {
                        dto.addIcdForVersion("0", next);
                        dto.getOccurance().add(0L);
                        itMc.remove();
                    }
                }
            }
            for (TCaseIcd icd : mergedCaseIcds) {
                IcdOverviewDTO dto = new IcdOverviewDTO(icd.getIcdcCode(), icd.getIcdcLocEn(), "");
                dto.addIcdForVersion(String.valueOf(0), icd);
                dto.getOccurance().add(0L);
                caseIcds.add(dto);
            }
            return caseIcds;
        }
//
//        param.remove(0);
//        List<IcdOverviewDTO> dtos = caseBean.findAllIcdCodesForVersions(param, CpxClientConfig.instance().getSelectedGrouper());
//        ArrayList<TCaseIcd> icds = new ArrayList<>(pDetails.getAllIcds());
//        icds.sort(Comparator.comparing(TCaseIcd::getIcdcCode));
//        icdloop:
//        for(TCaseIcd icd : icds){
//            for(IcdOverviewDTO dto : dtos){
//                if(icd.getIcdcCode().equals(dto.getIcdCode())){
//                    if(!dto.isOccuringIn(pDetails.getId())){
//                        dto.addIcdForVersion(String.valueOf(pDetails.getId()), icd);
//                        dto.getOccurance().add(pDetails.getId());
//                        continue icdloop;
//                    }
//                }
//            }
//            IcdOverviewDTO newDto = new IcdOverviewDTO(icd.getIcdcCode(), "");
//            newDto.addIcdForVersion(String.valueOf(pDetails.getId()), icd);
//            newDto.getOccurance().add(pDetails.getId());
//            dtos.add(newDto);
//        }
        HashMap<Long, List<TCaseIcd>> versionOpsMap = new HashMap<>();
        for (long id : param) {
            List<TCaseIcd> icds = new ArrayList<>();
            if (id == 0) {
                icds.addAll(pDetails.getAllIcds());
            } else {
                icds.addAll(caseBean.get().findAllIcdCodes(id, CpxClientConfig.instance().getSelectedGrouper()));
            }
//            icds.sort(Comparator.comparing(TCaseIcd::getIcdcCode));
            versionOpsMap.put(id, icds);
        }
        List<IcdOverviewDTO> dtos = new IcdDtoHelper().computeDtoList(versionOpsMap);
        return dtos;
    }

    /**
     * @param pDetails case version
     * @return list of all icds for that case version
     */
    public List<TCaseIcd> getIcdsForVersion(TCaseDetails pDetails) {
        //TODO
        if (testMode) {
            return getTestIcds();
        }
        return caseBean.get().findIcdsForCaseDetailId(pDetails.getId(), CpxClientConfig.instance().getSelectedGrouper());
    }

    /**
     * delete icd from case version
     *
     * @param pCaseDetails case version
     * @param pIcd icd to delete
     */
    public void deleteIcd(TCaseDetails pCaseDetails, TCaseIcd pIcd) {
        if (testMode && testIcds != null) {
            testIcds.remove(pIcd);
            return;
        }
        for (TCaseDepartment dep : pCaseDetails.getCaseDepartments()) {
            Iterator<TCaseIcd> it = dep.getCaseIcds().iterator();
            while (it.hasNext()) {
                TCaseIcd next = it.next();
                if (next.equals2object(pIcd)) {
                    it.remove();
                }
            }
        }

    }

    /**
     * create OpsOverviewDTO objects for the merging
     *
     * @param pDetails merged caseDetails
     * @param param list of all case versions displayed
     * @return list of computed overview TODO: Refactor to reflect to be
     * discussed behavior
     */
    public List<OpsOverviewDTO> getAllOpsCodes(TCaseDetails pDetails, List<Long> param) {
        //TODO -siehe getAllIcdsCodes
        if (testMode) {
            List<OpsOverviewDTO> caseOpses = getTestOpsOverviewDTO();
            List<TCaseOps> mergedCaseOpses = new ArrayList<>(getTestOpses());
            for (OpsOverviewDTO dto : caseOpses) {
                Iterator<TCaseOps> it = mergedCaseOpses.iterator();
                while (it.hasNext()) {
                    TCaseOps next = it.next();
                    if (dto.getOpsCode().equals(next.getOpscCode())) {
                        dto.addOpsForVersion("0", next);
                        dto.getOccurance().add(0L);
                        it.remove();
                    }
                }
            }
            for (TCaseOps ops : mergedCaseOpses) {
                OpsOverviewDTO dto = new OpsOverviewDTO(ops.getOpscCode(), "");
                dto.addOpsForVersion(String.valueOf(0), ops);
                dto.getOccurance().add(0L);
                caseOpses.add(dto);
            }
            return caseOpses;
        }

//        param.remove(0);
//        List<OpsOverviewDTO> dtos = caseBean.findAllOpsCodesForVersions(param);
//        ArrayList<TCaseOps> opses = new ArrayList<>(pDetails.getAllOpses());
//        opses.sort(Comparator.comparing(TCaseOps::getOpscCode));
//
//        opsloop:
//        for(TCaseOps ops : opses){
//            for(OpsOverviewDTO dto : dtos){
//                if(ops.getOpscCode().equals(dto.getOpsCode())){
//                    if(!dto.isOccuringIn(pDetails.getId())){
//                        dto.addOpsForVersion(String.valueOf(pDetails.getId()), ops);
//                        dto.getOccurance().add(pDetails.getId());
//                        continue opsloop;
//                    }
//                }
//            }
//            OpsOverviewDTO newDto = new OpsOverviewDTO(ops.getOpscCode(), "");
//            newDto.addOpsForVersion(String.valueOf(pDetails.getId()), ops);
//            newDto.getOccurance().add(pDetails.getId());
//            dtos.add(newDto);
//        }
        HashMap<Long, List<TCaseOps>> versionOpsMap = new HashMap<>();
        for (long id : param) {
            List<TCaseOps> opses = new ArrayList<>();
            if (id == 0) {
                opses.addAll(pDetails.getAllOpses());
            } else {
                opses.addAll(caseBean.get().findAllOpsCodes(id));
            }
//            opses.sort(Comparator.comparing(TCaseOps::getOpscCode));
            versionOpsMap.put(id, opses);
        }
        List<OpsOverviewDTO> dtos = new OpsDtoHelper().computeDtoMergeList(versionOpsMap);
        return dtos;
    }

    /**
     * get all ops for case version
     *
     * @param pCaseDetails case version
     * @return list of all opses for that case version
     */
    public List<TCaseOps> getOpsesForVersion(TCaseDetails pCaseDetails) {
        //TODO
        if (testMode) {
            return getTestOpses();
        }
        return caseBean.get().findOpsForCaseDetailsId(pCaseDetails.getId());
    }

    /**
     * delte Ops from case version
     *
     * @param pCaseDetails case version
     * @param pOps ops to delete
     */
    public void deleteOps(TCaseDetails pCaseDetails, TCaseOps pOps) {
        if (testMode && testOpses != null) {
            testOpses.remove(pOps);
            return;
        }
        for (TCaseDepartment dep : pCaseDetails.getCaseDepartments()) {
            Iterator<TCaseOps> it = dep.getCaseOpses().iterator();
            while (it.hasNext()) {
                TCaseOps next = it.next();
                if (next.equals2object(pOps)) {
                    it.remove();
                }
            }
        }
//        caseBean.removeOps(ops);
    }

    /**
     * @param pCaseDetails case version
     * @return list of all departments for case version
     */
    public List<TCaseDepartment> getDepartmentsForDetailsId(TCaseDetails pCaseDetails) {
        //TODO
        if (testMode) {
            return getTestDepartmentList();
        }
        return pCaseDetails.getSortedDepartments();
    }

    /**
     * save merge case in databse with merge ident
     *
     * @param pCase case to save
     * @param pMergeIdent merge ident, to set references
     * @return indicator if storing was successful
     */
    public boolean saveMergedCase(TCase pCase, Integer pMergeIdent) {
        LOG.info("save merged case");
        Long dbId = mergeBean.get().saveMergedCase(pCase, pMergeIdent);
        if (dbId == null || dbId == 0L) {
            return false;
        }
        pCase.setId(dbId);
        return true;
    }

    /**
     * @param csHospitalIdent unique hospital ident
     * @return hospital catalog data
     */
    public CpxHospital getHospitalData(String csHospitalIdent) {
        return hospitalCatalog.getByCode(csHospitalIdent, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

    /**
     * @param insNumber unique number of the insurance
     * @return insurance catalog data
     */
    public CpxInsuranceCompany getInsuranceData(String insNumber) {
        return insuranceCatalog.getByCode(insNumber, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

    public void saveCaseDetailsEntity(TCaseDetails pDetails) {
        //TODO
//        if(!testMode){
//            caseBean.saveCaseDetailsEntity(pDetails);
//        }
    }

    public TCase reloadContent(TCase pContent) throws CpxIllegalArgumentException {
        if (testMode) {
            return getTestCase(pContent.getId(), pContent.getId());
        }
        //TODO
        if (pContent.getId() != 0) {
            return caseBean.get().findSingleCaseForId(pContent.getId());
        }
        return pContent;
    }
    
        public int doCheckMerging4Patient(TPatient patient, CaseTypeEn grpresType) {
             GDRGModel grouper = CpxClientConfig.instance().getSelectedGrouper();
            int ret = readmissionBean.get().checkReadmissions4Patient(patient.getId(), grpresType, true,  grouper, grouper.equals(GDRGModel.AUTOMATIC));
            mergeCases.clear();
            return ret;
    }



    /**
     * @param pVersion case version
     * @param pType type of the supplementary fee
     * @return calculated supplementary fee value from the server for a case
     * version
     */
    public Double getSupplementaryFeeValue(TGroupingResults pVersion, SupplFeeTypeEn pType) {
        return grouperServiceBean.get().getSupplFeeValue(pVersion, pType);
    }

    /**
     * @param pVersion case version
     * @param pType type of the supplementary fee
     * @return calculated supplementary fee value from the server for a case
     * version
     */
    public Double getSupplementaryFeeValue(TCaseDetails pVersion, SupplFeeTypeEn pType) {
        GDRGModel grouper = CpxClientConfig.instance().getSelectedGrouper();
        return grouperServiceBean.get().getSupplFeeValue(grouper, pVersion.getId(), pType);
    }

    /**
     * @param pCase case to get baserate for, baserate is shown by hospital of
     * the case and die admission date of the current local version
     * @return fee value of the base rate from the catalog. if nothing is found
     * 0.0 is returned
     */
    public Double getCaseBaseRateFeeValue(TCase pCase) {
        return baserateCatalog.findDrgBaserateFeeValue(pCase.getCsHospitalIdent(), pCase.getCurrentLocal().getCsdAdmissionDate(), AbstractCpxCatalog.DEFAULT_COUNTRY);
    }
    /**
     * @param pCase case to get baserate for, baserate is shown by hospital of
     * the case and die admission date of the current local version
     * @return fee value of the base rate from the catalog. if nothing is found
     * 0.0 is returned
     */
    public Double getCareBaseRateFeeValue(TCase pCase) {
        return baserateCatalog.findCareBaserateFeeValue(pCase.getCsHospitalIdent(), pCase.getCurrentLocal().getCsdAdmissionDate(), AbstractCpxCatalog.DEFAULT_COUNTRY);
    }
    /**
     * find grouper result for case details id ( case version) and grouper model
     *
     * @param pCaseDetailsId case version id
     * @param pModel grouper model
     * @return result entity or null if database do not contain result for that
     * case version and grouper model
     */
    public TGroupingResults findGrouperResult(long pCaseDetailsId, GDRGModel pModel) {
        return caseBean.get().findGroupingResultForVersion(pCaseDetailsId, pModel);
    }

    /**
     * find grouper result for case details id ( case version) and grouper model
     *
     * @param pGroupingResult grouping results database id
     * @return result entity or null if database do not contain result for that
     * case version and grouper model
     */
    public TGroupingResults findGrouperResult(long pGroupingResult) {
        return caseBean.get().findGroupingResult(pGroupingResult);
    }

    /**
     * @param mapping merge mapping list
     * @return get list of all case entities in mapping list, referenced by
     * CaseByMergeMemberCaseId
     */
    public Set<TCase> getCases(List<TCaseMergeMapping> mapping) {
        Set<TCase> cases = new HashSet<>();
        if (mergedCase != null) {
            cases.add(mergedCase);
        }
        for (TCaseMergeMapping mrgCse : mapping) {
            cases.add(mrgCse.getCaseByMergeMemberCaseId());
        }

        return cases;
    }

    /**
     * @return get the current merged case stored in facade, null if nothing was
     * merged
     */
    public TCase getMergedCase() {
        return mergedCase;
    }

    /**
     * merge and persist by merge ident server merge case and store it
     *
     * @param pIdent merge ident
     * @return indicator if merge was successful
     */
    public boolean mergeAndPersistById(Integer pIdent) {
        Long mergedId = mergeBean.get().persistAndMergeById(pIdent);
        if(mergedId != null){
            // group result case
            grouperServiceBean.get().groupCaseLocal(mergedId, Session.instance().getCpxUserId() , Session.instance().getCpxActualRoleId(), CpxClientConfig.instance().getSelectedGrouper());
        }
        return mergedId != null;
    }
    
/**
 * by grouping of the current case with actual model we group all cases of the patient because of grouping with history cases
 * @param currentCase 
 */
    public void performGroup4CurrentCase(TCase currentCase) {
        grouperServiceBean.get().groupCaseLocal(currentCase, Session.instance().getCpxUserId() , Session.instance().getCpxActualRoleId(), CpxClientConfig.instance().getSelectedGrouper());
    }
    
    public String checkHasMerged(Integer ident) {
        return mergeBean.get().checkHasMerged(ident);
    }


    /**
     * @param ident remove all mergeCases with given ident NOTE: only removed in
     * client set for ui reasons, no database action is done
     */
    public void removeFromMergeCases(Integer ident) {
        mergeCases.removeIf(new Predicate<TCaseMergeMapping>() {
            @Override
            public boolean test(TCaseMergeMapping t) {
                return t.getMrgMergeIdent().equals(ident);
            }
        });
    }
    
    public boolean checkDatabaseRequirements(GDRGModel pModel, CaseTypeEn pType){
        return checkDatabaseRequirements(pModel, pType,null);
    }
    
    public boolean checkDatabaseRequirements(GDRGModel pModel, CaseTypeEn pType, Long pPatientId){
        if(pPatientId == null){
            return  mergeBean.get().checkDatabaseRequirements(pModel, pType);
        }else{
            return  mergeBean.get().checkDatabaseRequirements4Patient(pModel, pType, pPatientId);
        }
    }

    /*
    * private classes, init test data
     */
    //set case test data to show in merging detail
    private TCase createTestData(long pId) throws CpxIllegalArgumentException {
        TCase hcase = new TCase();
        hcase.setId(pId);
        hcase.setCsCaseNumber(String.valueOf(pId));
        hcase.setCsCaseTypeEn(CaseTypeEn.DRG);
        hcase.setCsHospitalIdent("1111111111");
        TCaseDetails local = new TCaseDetails();
        hcase.setCurrentLocal(local);

        local.setCsdAdmCauseEn(AdmissionCauseEn.E);
        local.setCsdAdmLawEn(AdmissionByLawEn.Freiwillig);
        local.setCsdAdmReason12En(AdmissionReasonEn.ar01);
        local.setCsdAdmReason34En(AdmissionReason2En.ar203);
//        local.setCsd

        return hcase;
    }

    private TCaseMergeMapping createMergingTestData(int pMergeId, long pCaseId) throws CpxIllegalArgumentException {
        TCaseMergeMapping mapping = new TCaseMergeMapping();
        mapping.setMrgMergeIdent(pMergeId);
        mapping.setGrpresType(CaseTypeEn.DRG);
        mapping.setMrgCondition1(1);
        mapping.setMrgCondition2(0);
        mapping.setMrgCondition3(0);
        mapping.setMrgCondition4(1);
        mapping.setMrgCondition5(1);
        mapping.setMrgCondition6(0);
        mapping.setMrgCondition7(1);
        mapping.setMrgCondition8(1);
        mapping.setMrgCondition9(0);
        mapping.setMrgCondition10(1);

        TCase hcase = getTestCase(pCaseId, pCaseId);

        TCaseDrg result = getTestGroupingResult();

        mapping.setCaseByMergeMemberCaseId(hcase);
        mapping.setGrpresId(result);
        return mapping;
    }

    private List<TCaseMergeMapping> setUpMergingList() throws CpxIllegalArgumentException {
        List<TCaseMergeMapping> test = new ArrayList<>();
        test.add(createMergingTestData(1, 1));
        test.add(createMergingTestData(1, 2));
        test.add(createMergingTestData(2, 3));
        test.add(createMergingTestData(2, 4));
        return test;
    }

    private TCase getTestCase(long pId, long pCaseId) throws CpxIllegalArgumentException {
        TCase hcase = new TCase();
        hcase.setId(pId);
        hcase.setCsHospitalIdent("1111111");
        hcase.setCsCaseNumber(String.valueOf(pCaseId));
        hcase.setCsCaseTypeEn(CaseTypeEn.DRG);

        TPatient patient = new TPatient();
        TInsurance ins = new TInsurance();
        ins.setInsInsuranceCompany("111111111");
        patient.setCurrentInsurance(ins);
        hcase.setPatient(patient);

        TCaseDetails details = new TCaseDetails();
        details.setCsdAdmCauseEn(AdmissionCauseEn.E);
        details.setCsdIsActualFl(true);
        details.setCsdIsLocalFl(true);
        details.setCsdAdmReason34En(AdmissionReason2En.ar203);
        details.setCsdAdmReason12En(AdmissionReasonEn.ar01);
        details.setCsdDischargeDate(new Date());
        details.setCsdAdmissionDate(new Date(details.getCsdDischargeDate().getTime() - 1));
        hcase.getCaseDetails().add(details);

        return hcase;
    }

    private TCaseDrg getTestGroupingResult() {
        TCaseDrg result = new TCaseDrg();
        result.setGrpresCode("A017");
        result.setDrgcCwCorr(10.0);
        result.setDrgcCwEffectiv(10.0);
        result.setGrpresPccl(2);
        result.setDrgcTypeOfCorrEn(DrgCorrTypeEn.Deduction);
        result.setGrpresType(CaseTypeEn.DRG);
        return result;
    }

    private List<CpxSimpleRuleDTO> getTestDetectedRules() {
        List<CpxSimpleRuleDTO> rules = new ArrayList<>();
        CRGRule currentRule = new CRGRule();
        currentRule.m_rid = "1";
        currentRule.m_number = "2016_1004310_20129999000000";
        currentRule.m_typeText = "error";
        currentRule.m_errorTypeText = "DKR";
        currentRule.m_text = "";
        currentRule.m_caption = "DKR 1107: Dehydratation bei Gastroenteritis";
        currentRule.m_notice = "DKR 1107 Dehydratation bei Gastroenteritis&amp;#013&amp;#013Bei stationärer Aufnahme zur Behandlung einer Gastroenteritis mit Dehydratation wird die Gastroenteritis als Hauptdiagnose und „Dehydratation” (E86 Volumenmangel) als Nebendiagnose&amp;#013angegeben.&amp;#013&amp;#013&amp;#013&amp;#013";
        currentRule.m_suggestion = "Gastroenteritis ist die korrekte Hauptdiagnose (+HD A09)";
        CpxSimpleRuleDTO dto1 = new CpxSimpleRuleDTO(currentRule);

        currentRule = new CRGRule();
        currentRule.m_rid = "2";
        currentRule.m_number =  "2016_1004311_20129999000000";
        currentRule.m_typeText = "error";
        currentRule.m_errorTypeText = "ICD";
        currentRule.m_text = "";
        currentRule.m_caption = "Peritonitis als Nebendiagnose mit anderer Peritonitis-Diagnose ";
        currentRule.m_notice = "K65 Peritonitis  &amp;#013  Exkl.:  &amp;#013Peritonitis: &amp;#013· aseptisch ( T81.6 ) &amp;#013· bei oder nach: &amp;#013· Abort, Extrauteringravidität oder Molenschwangerschaft ( O00-O07 , O08.0 ) &amp;#013· Appendizitis ( K35.- ) &amp;#013· Divertikulose des Darmes ( K57.- ) &amp;#013· beim Neugeborenen ( P78.0-P78.1 ) &amp;#013· benigne, paroxysmal ( E85.0 ) &amp;#013· durch chemische Substanzen ( T81.6 ) &amp;#013· durch Talkum oder sonstige Fremdsubstanzen ( T81.6 ) &amp;#013· periodisch, familiär ( E85.0 ) &amp;#013· puerperal ( O85 ) &amp;#013· weibliches Becken ( N73.3-N73.5 ) &amp;#013 &amp;#013";
        currentRule.m_suggestion = "Peritonitis: gleichzeitige Kodierung aus K65* und den dazugehörigen Exklusiva, bitte korrekte ND angeben";
        CpxSimpleRuleDTO dto2 = new CpxSimpleRuleDTO(currentRule);
        
        currentRule = new CRGRule();
        currentRule.m_rid = "3";
        currentRule.m_number = "2016_1004312_20129999000000";
        currentRule.m_typeText = "error";
        currentRule.m_errorTypeText = "ICD";
        currentRule.m_text = "";
        currentRule.m_caption = "Peritonitis als Hauptdiagnose mit anderer Peritonitis-Diagnose";
        currentRule.m_notice = "K65 Peritonitis  &amp;#013  Exkl.:  &amp;#013Peritonitis: &amp;#013· aseptisch ( T81.6 ) &amp;#013· bei oder nach: &amp;#013· Abort, Extrauteringravidität oder Molenschwangerschaft ( O00-O07 , O08.0 ) &amp;#013· Appendizitis ( K35.- ) &amp;#013· Divertikulose des Darmes ( K57.- ) &amp;#013· beim Neugeborenen ( P78.0-P78.1 ) &amp;#013· benigne, paroxysmal ( E85.0 ) &amp;#013· durch chemische Substanzen ( T81.6 ) &amp;#013· durch Talkum oder sonstige Fremdsubstanzen ( T81.6 ) &amp;#013· periodisch, familiär ( E85.0 ) &amp;#013· puerperal ( O85 ) &amp;#013· weibliches Becken ( N73.3-N73.5 ) &amp;#013 &amp;#013";
        currentRule.m_suggestion = "Peritonitis: HD aus K65* und ND aus den dazugehörigen Exklusiva, bitte prüfen";
        CpxSimpleRuleDTO dto3 = new CpxSimpleRuleDTO(currentRule);
        
        rules.add(dto1);
        rules.add(dto2);
        rules.add(dto3);
        return rules;
    }

    private List<IcdOverviewDTO> getTestIcdOverviewDTOs() {
        List<IcdOverviewDTO> dtos = new ArrayList<>();
        IcdOverviewDTO dto1 = new IcdOverviewDTO("A00.0", LocalisationEn.E, "1,2,3,4");
        dto1.addIcdForVersion("1", createTestIcd("A00.0", true));
        dto1.addIcdForVersion("2", createTestIcd("A00.0", true));
        dto1.addIcdForVersion("3", createTestIcd("A00.0", true));
        dto1.addIcdForVersion("4", createTestIcd("A00.0", true));

        IcdOverviewDTO dto2 = new IcdOverviewDTO("B00.0", LocalisationEn.E, "1,2,3,4");
        dto2.addIcdForVersion("1", createTestIcd("B00.0", false));
        dto2.addIcdForVersion("2", createTestIcd("B00.0", false));
        dto2.addIcdForVersion("3", createTestIcd("B00.0", false));
        dto2.addIcdForVersion("4", createTestIcd("B00.0", false));
        dtos.add(dto1);
        dtos.add(dto2);
        return dtos;
    }

    private TCaseIcd createTestIcd(String pIcdCode, boolean isHbxFlag) {
        TCaseIcd icd = new TCaseIcd();
        icd.setIcdcIsHdxFl(isHbxFlag);
        icd.setIcdcCode(pIcdCode);
        icd.setIcdIsToGroupFl(true);
        icd.setIcdcLocEn(LocalisationEn.R);
        return icd;
    }

    private List<TCaseIcd> getTestIcds() {
        if (testIcds == null) {
            testIcds = new LinkedList<>();
            testIcds.add(createTestIcd("A00.0", true));
            testIcds.add(createTestIcd("B00.0", false));
        }
        return testIcds == null ? null : new ArrayList<>(testIcds);
    }

    private List<OpsOverviewDTO> getTestOpsOverviewDTO() {
        List<OpsOverviewDTO> dtos = new ArrayList<>();
        OpsOverviewDTO dto1 = new OpsOverviewDTO("111.1", "1,2,3,4");
        dto1.addOpsForVersion("1", createTestOps("111.1"));
        dto1.addOpsForVersion("2", createTestOps("111.1"));
        dto1.addOpsForVersion("3", createTestOps("111.1"));
        dto1.addOpsForVersion("4", createTestOps("111.1"));

        OpsOverviewDTO dto2 = new OpsOverviewDTO("222.2", "1,2,3,4");
        dto2.addOpsForVersion("1", createTestOps("222.2"));
        dto2.addOpsForVersion("2", createTestOps("222.2"));
        dto2.addOpsForVersion("3", createTestOps("222.2"));
        dto2.addOpsForVersion("4", createTestOps("222.2"));
        dtos.add(dto1);
        dtos.add(dto2);
        return dtos;
    }

    private TCaseOps createTestOps(String pOpsCode) {
        TCaseOps ops = new TCaseOps();
        ops.setOpscCode(pOpsCode);
        ops.setOpscDatum(new Date());
        ops.setOpsIsToGroupFl(true);
        ops.setOpscLocEn(LocalisationEn.R);
        return ops;
    }

    private List<TCaseOps> getTestOpses() {
        if (testOpses == null) {
            testOpses = new LinkedList<>();
            testOpses.add(createTestOps("111.1"));
            testOpses.add(createTestOps("222.2"));
        }
        return testOpses == null ? null : new ArrayList<>(testOpses);
    }

    private List<TCaseDepartment> getTestDepartmentList() {
        List<TCaseDepartment> depList = new ArrayList<>();
        TCaseDepartment dep1 = new TCaseDepartment();
        dep1.setDepKey301("0000");
        dep1.setDepcDisDate(new Date());
        dep1.setDepcAdmDate(new Date((dep1.getDepcDisDate().getTime() - 1)));
        depList.add(dep1);
        return depList;
    }
    
    
    public boolean allIcdsGrouped(TCaseDetails caseDetails, GDRGModel pModel) {
        return caseBean.get().allIcdsGrouped(caseDetails.getId(), pModel);
    }

    public boolean isSaveCaseMergingAllowed(){
        return mergeBean.get().isSaveCaseMergingAllowed();
    }

}
