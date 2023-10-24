/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.menu.fx.filterlists.cases;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.client.app.menu.fx.table_master_detail.FilterListScene;
import de.lb.cpx.client.app.wm.util.SceneLoader;
import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.model.fx.filter_manager.FilterManager;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.enums.CaseDetailsCancelReasonEn;
import de.lb.cpx.model.enums.SearchListTypeEn;
import static de.lb.cpx.model.enums.SearchListTypeEn.QUOTA;
import static de.lb.cpx.model.enums.SearchListTypeEn.RULE;
import static de.lb.cpx.model.enums.SearchListTypeEn.WORKING;
import de.lb.cpx.service.ejb.CaseMergingServiceBeanRemote;
import de.lb.cpx.service.ejb.LockService;
import de.lb.cpx.service.ejb.ProcessServiceBeanRemote;
import de.lb.cpx.service.ejb.QuotaListStatelessEJBRemote;
import de.lb.cpx.service.ejb.RuleListStatelessEJBRemote;
import de.lb.cpx.service.ejb.SearchResult;
import de.lb.cpx.service.ejb.SingleCaseEJBRemote;
import de.lb.cpx.service.ejb.WorkingListStatelessEJBRemote;
import de.lb.cpx.shared.dto.LockException;
import de.lb.cpx.shared.dto.WorkingListItemDTO;
import de.lb.cpx.shared.filter.ColumnOption;
import de.lb.cpx.shared.filter.FilterOption;
import de.lb.cpx.shared.filter.enums.QuotaListAttributes;
import de.lb.cpx.shared.filter.enums.RuleListAttributes;
import de.lb.cpx.shared.filter.enums.WorkingListAttributes;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmProcess;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.naming.NamingException;

/**
 * Scene of the working list
 *
 * WorkignList specific implementation of the FilterListScene
 *
 * @author wilde
 */
public class WorkingListScene extends FilterListScene<WorkingListItemDTO> {

    private final String DEFAULT_TITLE = Lang.getWorkingList();
    private WorkingListStatelessEJBRemote workingListServiceBean;
    private RuleListStatelessEJBRemote ruleListServiceBean;
    private QuotaListStatelessEJBRemote quotaListServiceBean;
    private LockService lockServiceBean;
    private ProcessServiceBeanRemote processServiceBean;
    private SingleCaseEJBRemote singleCaseServiceBean;
    private CaseMergingServiceBeanRemote caseMergingServiceBeanRemote;

    /**
     * constructs new instance with workingListFXMLController
     *
     * @throws IOException if mismatch happen in fxml (FilterListFXML.fxml)
     * @throws javax.naming.NamingException thrown if beans can not be found
     */
    public WorkingListScene() throws IOException, NamingException {
        super(new WorkingListFXMLController()/*,new FilterManager(SearchListTypeEn.WORKING, WorkingListAttributes.instance())*/);
        updateFilterManager(SearchListTypeEn.WORKING);
        setSceneTitle(DEFAULT_TITLE);
        setMaxItems(CpxClientConfig.instance().getSearchListFetchSize());
        getController().afterInitialisingScene();
    }

    @Override
    public Integer getMaxItems() {
        return CpxClientConfig.instance().getSearchListFetchSize();
    }

    @Override
    public SearchResult<? extends WorkingListItemDTO> loadItems(Integer pStart, Integer pEnd) {
        GDRGModel grouperModel = CpxClientConfig.instance().getSelectedGrouper();
        //Whacky interface methodes
        //TODO: REFACTOR!
        boolean isLocal = Session.instance().isCaseLocal();
        int page = pStart / getMaxItems();
        LOG.info("try to load from index " + pStart + " to index " + pEnd + " - page " + pStart + " max items " + getMaxItems());
        SearchResult<? extends WorkingListItemDTO> result = getSearchResult(isLocal, grouperModel, page, getMaxItems(), new HashMap<>(getFilterManager().getFilterOptionMap()));
        return result;
    }

    private static final Logger LOG = Logger.getLogger(WorkingListScene.class.getName());

    @Override
    public void connectServiceBeans() throws NamingException {
        workingListServiceBean = Session.instance().getEjbConnector().connectWorkingListBean().getWithEx();
        ruleListServiceBean = Session.instance().getEjbConnector().connectRuleListBean().getWithEx();
        quotaListServiceBean = Session.instance().getEjbConnector().connectQuotaListBean().getWithEx();
        lockServiceBean = Session.instance().getEjbConnector().connectLockServiceBean().getWithEx();
        processServiceBean = Session.instance().getEjbConnector().connectProcessServiceBean().getWithEx();
        singleCaseServiceBean = Session.instance().getEjbConnector().connectSingleCaseBean().getWithEx();
        caseMergingServiceBeanRemote = Session.instance().getEjbConnector().connectCaseMergingServiceBean().getWithEx();
    }

    public TCase getCaseForCaseDetail(long pCaseDetailId) {
        return workingListServiceBean.getCaseForDetail(pCaseDetailId);
    }

    public boolean isRuleList() {
        return getFilterManager().getListType() == RULE;
    }

    public boolean isQuotaList() {
        return getFilterManager().getListType() == QUOTA;
    }

    public boolean isWorkingList() {
        return getFilterManager().getListType() == WORKING;
    }

    //get searchResult speficic for list type
    //rule or working list
    private SearchResult<? extends WorkingListItemDTO> getSearchResult(boolean isLocal, GDRGModel grouperModel, int page, int pMaxItems, Map<ColumnOption, List<FilterOption>> pFilterOptions) {
        switch (getFilterManager().getListType()) {
            case WORKING:
                return workingListServiceBean.findWorkingList(isLocal, grouperModel, page, pMaxItems, pFilterOptions);
            case RULE:
                //CPX-2108: AGe and GKr stated that rule list should respect user selection in the same way  as regular working list
                return ruleListServiceBean.findRuleList(isLocal, grouperModel, page, pMaxItems, pFilterOptions);
            case QUOTA:
                return quotaListServiceBean.findQuotaList(isLocal, grouperModel, page, pMaxItems, pFilterOptions);
            default:
                //TODO:Throw exception?
                LOG.warning("Unknown ListType!: " + getFilterManager().getListType().name() + "\nCan not fetch List, return null");
                return null;
        }
    }
//    private List<WorkingListItemDTO> getTestList(){
//        List<WorkingListItemDTO> items = new ArrayList<>();
//        WorkingListItemDTO dto = new WorkingListItemDTO();
//        dto.setCsCaseNumber("1");
//        dto.setCsdAdmCauseEn("E");
//        dto.setCsdAdmissionDate(new Date());
//        dto.setCsStatusEn("NEW");
//        
//        items.add(dto);
//        return items;
//    }

    @Override
    public void updateFilterManager(SearchListTypeEn newValue) {
        switch (newValue) {
            case RULE:
                setFilterManager(new FilterManager(SearchListTypeEn.RULE, RuleListAttributes.instance()));
                break;
            case QUOTA:
                setFilterManager(new FilterManager(SearchListTypeEn.QUOTA, QuotaListAttributes.instance()));
                break;
            case WORKING:
                setFilterManager(new FilterManager(SearchListTypeEn.WORKING, WorkingListAttributes.instance()));
                break;
            default:
                //TODO exception?
                LOG.warning("Unknown ListType: " + newValue.name() + "\nCan not change Filtermanager");
        }
    }

    @Override
    public List<SearchListTypeEn> getSupportedLists() {
        List<SearchListTypeEn> list = super.getSupportedLists();
        list.add(SearchListTypeEn.WORKING);
        list.add(SearchListTypeEn.RULE);
        list.add(SearchListTypeEn.QUOTA);
        return list;
    }

    @Override
    public CpxScene openItem(long pId) {
        return SceneLoader.getInstance().loadCaseMangementScene(pId);
    }

    @Override
    public boolean isItemLocked(long id) {
        return lockServiceBean.isCaseLocked(id);
    }

    @Override
    public boolean isItemCanceled(long id) {
        return singleCaseServiceBean.findSingleCaseForId(id).getCsCancellationReasonEn();
    }

    @Override
    public boolean unlockItem(long id) {
        return lockServiceBean.unlockCase(id, true);
    }

    @Override
    public int unlockAllItems() {
        return lockServiceBean.removeAllCaseLocks();
    }

    @Override
    public int unlockItems(long[] pIds) {
        return lockServiceBean.unlockCase(pIds, true);
    }

    @Override
    public void checkLocked(long pId) throws LockException {
        lockServiceBean.checkCaseLock(pId);
    }

    /**
     * @param caseId database id of the case
     * @return getList of all Processes for a case
     */
    List<TWmProcess> getProcessesOfCase(long caseId) {
        return processServiceBean.getProcessesOfCase(caseId);
    }

    /**
     * @param caseId database id of the case
     * @param pIncludeCanceled include canceled processes in result
     * @return getList of all Processes for a case
     */
    List<TWmProcess> getProcessesOfCase(long caseId, final boolean pIncludeCanceled) {
        return processServiceBean.getProcessesOfCase(caseId, pIncludeCanceled);
    }

    @Override
    public void deleteItem(long caseId) throws CpxIllegalArgumentException {
        singleCaseServiceBean.deleteCase(caseId);
    }

    @Override
    public void cancelItem(long caseId) throws CpxIllegalArgumentException {
        singleCaseServiceBean.cancelCase(caseId);
    }

    @Override
    public void unCancelItem(long caseId) throws CpxIllegalArgumentException {
        try {

            singleCaseServiceBean.unCancelCase(caseId);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(WorkingListScene.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public CaseDetailsCancelReasonEn getCancelReason4Case(long caseId) throws CpxIllegalArgumentException {
         try {

             return singleCaseServiceBean.getCancelReason(caseId);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(WorkingListScene.class.getName()).log(Level.SEVERE, null, ex);
        } 
       return null;
    }
    

    @Override
    public WorkingListFXMLController getController() {
        return (WorkingListFXMLController) controller;
    }

    @Override
    public boolean checkExists(long pId) {
        return singleCaseServiceBean.caseExists(pId);
    }
    
    @Override
    public String getCaseNumbers4CancelledCaseByMerge(long pCaseId) {

        List<String> scNumbers = caseMergingServiceBeanRemote.getCaseNumbers4CanceledCase4Merge(pCaseId); // first number is the number of the merged case
        String joinedString = scNumbers.stream().map(t->t.indexOf("_c") > 0?t.substring(0, t.indexOf("_c")):t)
        .collect(Collectors.joining(", "));

        return joinedString;
    }
    @Override
    public void unmergeCases4CancelledCase(long caseId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public boolean isCreateCaseAnonymized(){
        return workingListServiceBean.isCaseCreateAnonymized();
    }

    public void checkGrouperModel() {
       getController().checkGrouperModel();
    }

}
