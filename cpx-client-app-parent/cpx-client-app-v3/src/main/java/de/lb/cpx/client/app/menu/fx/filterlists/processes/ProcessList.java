/* 
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.menu.fx.filterlists.processes;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.menu.fx.filter.tableview.FilterTableView;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.model.fx.tableview.column.FilterColumn;
import de.lb.cpx.model.enums.SearchListTypeEn;
import de.lb.cpx.service.ejb.SearchResult;
import de.lb.cpx.service.searchlist.SearchListResult;
import de.lb.cpx.shared.dto.WorkflowListItemDTO;
import de.lb.cpx.shared.filter.ColumnOption;
import de.lb.cpx.shared.filter.FilterOption;
import de.lb.cpx.shared.filter.enums.SearchListAttribute;
import de.lb.cpx.shared.filter.enums.SearchListAttributes;
import de.lb.cpx.shared.filter.enums.WorkflowListAttributes;
import java.util.List;
import java.util.stream.Stream;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

/**
 *
 * @author niemeier
 */
//case specific filter tableview
public class ProcessList extends FilterTableView<WorkflowListItemDTO, WorkflowListFXMLController> {
//    private ChangeListener<Number> updateTableInfosListener;
    
    public ProcessList(WorkflowListFXMLController pWorkflowList) {
        super(pWorkflowList);
//        currentCountProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                getFilterListFXMLController().updateTvInfos();
//                if (updateTableInfosListener == null) {
//                    updateTableInfosListener = (ObservableValue<? extends Number> ov, Number t, Number t1) -> {
//                        getFilterListFXMLController().updateTvInfos();
//                    };
//                    getFilterListFXMLController().numberOfFilteredIdsProperty().addListener(updateTableInfosListener);
//                }
//            }
//        });
        setShowMenu(true);
        setFilterColumnFactory(new Callback<SearchListAttribute, FilterColumn<WorkflowListItemDTO, ? extends Object>>() {
            @Override
            public FilterColumn<WorkflowListItemDTO, ? extends Object> call(SearchListAttribute param) {
                WorkflowListColumn col = new WorkflowListColumn(param);
                if(WorkflowListAttributes.remFinished.equals(param.getKey())){
                    if(!Session.instance().isShowAllRemindersConfig()){
                        col.setActionAllowed(SearchListAttribute.ACTIONS_ALLOWED.DO_NOTHING);
                    }
                }
                return col;
            }
        });
        maxItemsProperty().bind(getFilterListFXMLController().getScene().maxItemProperty());
        setRowContextMenu(new ProcessContextMenu(getFilterListFXMLController()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<WorkflowListItemDTO> loadItems(int pStartIndex, int pEndIndex) {
        SearchResult<? extends WorkflowListItemDTO> result = getFilterListFXMLController().getScene().loadItems(pStartIndex, pEndIndex);
        setStopLoading(result.hasMoreResults);
        getFilterListFXMLController().setNumberOfFilteredIds(result.totalResultCount);
        return (List<WorkflowListItemDTO>) result.resultList;
        //return transfromResult(result);
    }

    @Override
    public SearchListTypeEn getListType() {
        return SearchListTypeEn.WORKFLOW;
    }

    @Override
    public SearchListResult updateFilter(SearchListResult pFilter, boolean pAutoReload) {
//            saveFilter(pFilter.getName(), pFilter);
        getFilterListFXMLController().updateFilterInManager(this, pFilter, pAutoReload);
        return MenuCache.getMenuCacheSearchLists().saveSearchList(pFilter, false);
    }

    @Override
    public Stream<ColumnOption> getColumnOptions() {
        return getFilterListFXMLController().getSortedColumnOptions();
    }

    @Override
    public SearchListAttributes getSearchListAttributes() {
        return getFilterListFXMLController().getScene().getFilterManager().getAttributes();
    }

    @Override
    public List<FilterOption> getFilterOptions(ColumnOption pOption) {
        return getFilterListFXMLController().getScene().getFilterManager().getFilterOptions(pOption);
    }

    @Override
    public String getFilterName() {
        return getFilterListFXMLController().getScene().getFilterManager().getListName();
    }

    @Override
    public SearchListResult getFilter() {
        return getFilterListFXMLController().getSearchList();
    }

    @Override
    public boolean clearFilter(String pKey) {
//            getSearchList().getFilter(pKey).clear();
        getFilterListFXMLController().getSearchList().removeFilter(pKey);
        return true;
    }

    @Override
    public boolean changeFilter(SearchListResult pFilter) {
        MenuCache.getMenuCacheSearchLists().setSelectedSearchList(pFilter);
        //return getFilterListFXMLController().updateFilterInManager(this, CpxClientConfig.instance().getSearchList(pFilter.getType(), pFilter.getId()), true);
        return getFilterListFXMLController().updateFilterInManager(this, MenuCache.getMenuCacheSearchLists().get(pFilter.getId(), pFilter.getType()), true);
    }
//        @Override
//        public List<SearchListAttribute> getDefaultColumns() {
//            return WorkflowListAttributes.instance().createDefaultColumns();
//        }
//

    @Override
    public SearchListResult saveFilter(SearchListResult pFilter) {
        return MenuCache.getMenuCacheSearchLists().saveSearchList(pFilter, false);
    }
//

    @Override
    public boolean deleteFilter(SearchListResult pFilter) {
        return MenuCache.getMenuCacheSearchLists().deleteSearchList(pFilter);
    }

    @Override
    public boolean shortcutControlCCopy(KeyEvent pEvent) {
        if (MainApp.getToolbarMenuScene().isWorkflowListShown()) {
            getFilterListFXMLController().copyProcessNumber();
            return true;
        }
        return false;
    }

    @Override
    public boolean shortcutEnterExecute(KeyEvent pEvent) {
        if (MainApp.getToolbarMenuScene().isWorkflowListShown()) {
            getFilterListFXMLController().openProcess();
            return true;
        }
        return false;
    }

    @Override
    public boolean shortcutF5Refresh(KeyEvent pEvent) {
        if (MainApp.getToolbarMenuScene().isWorkflowListShown()) {
            getFilterListFXMLController().reload();
        } else {
            MainApp.getToolbarMenuScene().reopenProcess();
        }
        return true;
    }

    @Override
    public boolean shortcutF4Close(KeyEvent pEvent) {
        if (MainApp.getToolbarMenuScene().isInWorkflowList()) {
            recentProcessId = MainApp.getToolbarMenuScene().getDisplayedProcessId();
            if (recentProcessId != 0L) {
                MainApp.getToolbarMenuScene().closeProcess(recentProcessId);
            }
            return true;
        }
        return false;
    }

//        @Override
//        public boolean shortcutF2New(KeyEvent pEvent) {
//            MainApp.getToolbarMenuScene().getWorkingList().getController().createProcessDialog(null);
//            return true;
//        }
    @Override
    public boolean shortcutF3Find(KeyEvent pEvent) {
        return shortcutAltLeftBack(pEvent);
    }

    @Override
    public boolean shortcutDelRemove(KeyEvent pEvent) {
        if (MainApp.getToolbarMenuScene().isWorkflowListShown()) {
            getFilterListFXMLController().deleteProcess();
            return true;
        }
        return false;
    }

    @Override
    public boolean shortcutShiftDelRemove(KeyEvent pEvent) {
        getFilterListFXMLController().deleteProcess();
        return true;
    }

    private long recentProcessId;

    @Override
    public boolean shortcutAltLeftBack(KeyEvent pEvent) {
        long processId = MainApp.getToolbarMenuScene().getDisplayedProcessId();
        if (processId != 0L) {
            recentProcessId = processId;
        }
        MainApp.getToolbarMenuScene().showWorkflowList();
        return true;
    }

    @Override
    public boolean shortcutAltRightForward(KeyEvent pEvent) {
        if (recentProcessId != 0L) {
            MainApp.getToolbarMenuScene().openProcess(recentProcessId);
        }
        return true;
    }

}
