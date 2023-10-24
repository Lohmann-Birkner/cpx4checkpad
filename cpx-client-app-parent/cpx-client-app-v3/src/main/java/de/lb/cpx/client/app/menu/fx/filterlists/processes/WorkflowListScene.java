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
package de.lb.cpx.client.app.menu.fx.filterlists.processes;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.menu.fx.table_master_detail.FilterListScene;
import de.lb.cpx.client.app.wm.util.SceneLoader;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.model.fx.filter_manager.FilterManager;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.model.enums.SearchListTypeEn;
import de.lb.cpx.service.ejb.LockService;
import de.lb.cpx.service.ejb.ProcessServiceBeanRemote;
import de.lb.cpx.service.ejb.SearchResult;
import de.lb.cpx.service.ejb.WorkflowListStatelessEJBRemote;
import de.lb.cpx.shared.dto.LockException;
import de.lb.cpx.shared.dto.WorkflowListItemDTO;
import de.lb.cpx.shared.filter.ColumnOption;
import de.lb.cpx.shared.filter.FilterOption;
import de.lb.cpx.shared.filter.enums.WorkflowListAttributes;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmProcess;
import de.lb.cpx.wm.model.TWmProcessCase;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.naming.NamingException;

/**
 * Scene for the WorkflowList handles Db and Filter access and manipulation
 *
 * @author wilde
 */
public class WorkflowListScene extends FilterListScene<WorkflowListItemDTO> {

    private static final String DEFAULT_TITLE = Lang.getWorkflowList();
    private WorkflowListStatelessEJBRemote workflowListServiceBean;
    private LockService lockServiceBean;
    private ProcessServiceBeanRemote processServiceBean;

    /**
     * constructs new instance with workflowListFXMLController
     *
     * @throws IOException if mismatch happen in fxml (FilterListFXML.fxml)
     * @throws javax.naming.NamingException thrown if beans can not be found
     */
    public WorkflowListScene() throws IOException, NamingException {
        super(new WorkflowListFXMLController());
        updateFilterManager(SearchListTypeEn.WORKFLOW);
        setSceneTitle(DEFAULT_TITLE);
        //TODO FIX ME:
        //uses same value as workflow list, should have other name or should be different value
        setMaxItems(CpxClientConfig.instance().getSearchListFetchSize());
        getController().afterInitialisingScene();
               }

    @Override
    public Integer getMaxItems() {
        return CpxClientConfig.instance().getSearchListFetchSize();
    }

    @Override
    public SearchResult<WorkflowListItemDTO> loadItems(Integer pStart, Integer pEnd) {
        GDRGModel grouperModel = CpxClientConfig.instance().getSelectedGrouper();
        //Whacky interface methodes
        //TODO: REFACTOR!
        boolean isLocal = Session.instance().isCaseLocal();
        boolean isShowAllReminders = Session.instance().isShowAllRemindersConfig();
        int page = pStart / getMaxItems();
        LOG.info("try to load from index " + pStart + " to index " + pEnd + " - page " + pStart + " max items " + getMaxItems());
        SearchResult<WorkflowListItemDTO> result = getSearchResult(isLocal, isShowAllReminders, grouperModel, page, getMaxItems(), new HashMap<>(getFilterManager().getFilterOptionMap()));
        return result;
    }

    private static final Logger LOG = Logger.getLogger(WorkflowListScene.class.getName());

    @Override
    public void connectServiceBeans() throws NamingException {
        workflowListServiceBean = Session.instance().getEjbConnector().connectWorkflowListBean().getWithEx();
        lockServiceBean = Session.instance().getEjbConnector().connectLockServiceBean().getWithEx();
        processServiceBean = Session.instance().getEjbConnector().connectProcessServiceBean().getWithEx();
    }

    //get searchResult speficic for list type
    //rule or workflow list
    private SearchResult<WorkflowListItemDTO> getSearchResult(boolean isLocal, boolean isShowAllReminders, GDRGModel grouperModel, int page, int pMaxItems, Map<ColumnOption, List<FilterOption>> pFilterOptions) {
        switch (getFilterManager().getListType()) {
            case WORKFLOW:
                return workflowListServiceBean.findWorkflowList(isLocal, isShowAllReminders, grouperModel, page, pMaxItems, pFilterOptions);
            default:
                //TODO:Throw exception?
                LOG.warning("Unknown ListType!: " + getFilterManager().getListType().name() + "\nCan not fetch List, return null");
                return null;
        }
    }

    @Override
    public void updateFilterManager(SearchListTypeEn newValue) {
        switch (newValue) {
            case WORKFLOW:
                setFilterManager(new FilterManager(SearchListTypeEn.WORKFLOW, WorkflowListAttributes.instance()));
                break;
            default:
                //TODO exception?
                LOG.warning("Unknown ListType: " + newValue.name() + "\nCan not change Filtermanager");
        }
    }

    @Override
    public List<SearchListTypeEn> getSupportedLists() {
        List<SearchListTypeEn> list = super.getSupportedLists();
        list.add(SearchListTypeEn.WORKFLOW);
        return list;
    }

    @Override
    public CpxScene openItem(long pId) {
        if(!processServiceBean.processExists(pId)){
            MainApp.showInfoMessageDialog("Der Vorgang konnte nicht geöffnet werden\n"+Lang.getProcessDoesNotExistWithReason(String.valueOf(pId)));
            return null;
        }
        return SceneLoader.getInstance().loadWmMainFrame(pId);
    }

    @Override
    public boolean isItemLocked(long pId) {
        return lockServiceBean.isProcessLocked(pId);
    }

    @Override
    public boolean isItemCanceled(long pId) {
        TWmProcess process = processServiceBean.findSingleProcessForId(pId);
        if(process == null){
            return false;
        }
        return process.getProcessCancellation();
    }

    @Override
    public boolean unlockItem(long pId) {
        return lockServiceBean.unlockProcess(pId, true);
    }

    @Override
    public int unlockAllItems() {
        return lockServiceBean.removeAllProcessLocks();
    }

    @Override
    public int unlockItems(long[] pIds) {
        return lockServiceBean.unlockProcess(pIds, true);
    }

    @Override
    public void checkLocked(long pId) throws LockException {
        lockServiceBean.checkProcessLock(pId);
    }

    @Override
    public void deleteItem(long pId) throws CpxIllegalArgumentException {
        processServiceBean.deleteProcess(pId);
    }

    @Override
    public void cancelItem(long processId) throws CpxIllegalArgumentException {
        processServiceBean.cancelProcess(processId);

    }

    @Override
    public void unCancelItem(long processId) throws CpxIllegalArgumentException {
        processServiceBean.unCancelProcess(processId);
    }

    public List<TWmProcessCase> getProcessCases(long processId) {
        return processServiceBean.getProcessCases(processId);
    }

    @Override
    public WorkflowListFXMLController getController() {
        return (WorkflowListFXMLController) controller;
    }

    public TWmProcess findProcessById(long itemId) {
        return processServiceBean.findProcessById(itemId);
    }

    @Override
    public boolean checkExists(long pId) {
        return processServiceBean.processExists(pId);
    }

}
