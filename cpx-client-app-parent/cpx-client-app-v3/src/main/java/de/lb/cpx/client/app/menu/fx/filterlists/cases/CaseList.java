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
package de.lb.cpx.client.app.menu.fx.filterlists.cases;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.menu.fx.filter.tableview.FilterTableView;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.model.fx.tableview.column.FilterColumn;
import de.lb.cpx.model.enums.SearchListTypeEn;
import de.lb.cpx.service.ejb.SearchResult;
import de.lb.cpx.service.searchlist.SearchListResult;
import de.lb.cpx.shared.dto.WorkingListItemDTO;
import de.lb.cpx.shared.filter.ColumnOption;
import de.lb.cpx.shared.filter.FilterOption;
import de.lb.cpx.shared.filter.enums.SearchListAttribute;
import de.lb.cpx.shared.filter.enums.SearchListAttributes;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

/**
 * case specific filter tableview
 *
 * @author niemeier
 */
public class CaseList extends FilterTableView<WorkingListItemDTO, WorkingListFXMLController> {

    private static final Logger LOG = Logger.getLogger(CaseList.class.getName());
//    private ChangeListener<Number> updateTableInfosListener;
    
    public CaseList(WorkingListFXMLController pWorkingList) {
        super(pWorkingList);
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
//        getFilterListFXMLController().numberOfFilteredIdsProperty().addListener(updateTableInfosListener);
        setShowMenu(true);
        setFilterColumnFactory(new Callback<SearchListAttribute, FilterColumn<WorkingListItemDTO, ? extends Object>>() {
            @Override
            public FilterColumn<WorkingListItemDTO, ? extends Object> call(SearchListAttribute param) {
                WorkingListColumn col = new WorkingListColumn(param);
                return col;
            }
        });
        maxItemsProperty().bind(getFilterListFXMLController().getScene().maxItemProperty());
        setRowContextMenu(new CaseContextMenu(getFilterListFXMLController()));
        addMenuItem(getFilterListFXMLController().createAddCaseButton());
        addMenuItem(getFilterListFXMLController().createRuleFilterButton());
    }

    @Override
    public void dispose() {
//        getFilterListFXMLController().numberOfFilteredIdsProperty().removeListener(updateTableInfosListener);
        super.dispose(); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<WorkingListItemDTO> loadItems(int pStartIndex, int pEndIndex) {
        long start = System.currentTimeMillis();
        SearchResult<? extends WorkingListItemDTO> result = getFilterListFXMLController().getScene().loadItems(pStartIndex, pEndIndex);
        setStopLoading(result.hasMoreResults);
        getFilterListFXMLController().setNumberOfFilteredIds(result.totalResultCount);
        LOG.log(Level.INFO, "time to load items({0}): {1}", new Object[]{result.resultList.size(), +(System.currentTimeMillis() - start)});
        return (List<WorkingListItemDTO>) result.resultList;
        //return transfromResult(result);
    }

//        @Override
//        public void beforeTask() {
//            getItems().clear();
//            super.beforeTask(); //To change body of generated methods, choose Tools | Templates.
//        }
    @Override
    public SearchListTypeEn getListType() {
        return SearchListTypeEn.WORKING;
    }

    @Override
    public SearchListResult updateFilter(SearchListResult pFilter, boolean pAutoReload) {
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
        getFilterListFXMLController().getSearchList().removeFilter(pKey);
        return true;
    }

    @Override
    public boolean changeFilter(SearchListResult pFilter) {
        if (pFilter == null) {
            return false;
        }
        MenuCache.getMenuCacheSearchLists().setSelectedSearchList(pFilter);
        //return getFilterListFXMLController().updateFilterInManager(this, CpxClientConfig.instance().getSearchList(pFilter.getType(), pFilter.getId()), true);
        return getFilterListFXMLController().updateFilterInManager(this, MenuCache.getMenuCacheSearchLists().get(pFilter.getId(), pFilter.getType()), true);
    }

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
        if (MainApp.getToolbarMenuScene().isWorkingListShown()) {
            getFilterListFXMLController().copyCaseNumber();
            return true;
        }
        return false;
    }

    @Override
    public boolean shortcutEnterExecute(KeyEvent pEvent) {
        if (MainApp.getToolbarMenuScene().isWorkingListShown()) {
            getFilterListFXMLController().openCase();
            return true;
        }
        return false;
    }

    @Override
    public boolean shortcutF5Refresh(KeyEvent pEvent) {
        if (MainApp.getToolbarMenuScene().isWorkingListShown()) {
            getFilterListFXMLController().reload();
        } else {
            MainApp.getToolbarMenuScene().reopenCase();
        }
        return true;
    }

    @Override
    public boolean shortcutF2New(KeyEvent pEvent) {
        getFilterListFXMLController().createProcessDialog();
        return true;
    }

    @Override
    public boolean shortcutF3Find(KeyEvent pEvent) {
        return shortcutAltLeftBack(pEvent);
    }

    @Override
    public boolean shortcutF4Close(KeyEvent pEvent) {
        if (MainApp.getToolbarMenuScene().isInWorkingList()) {
            recentCaseId = MainApp.getToolbarMenuScene().getDisplayedCaseId();
            if (recentCaseId != 0L) {
                MainApp.getToolbarMenuScene().closeCase(recentCaseId);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean shortcutDelRemove(KeyEvent pEvent) {
        if (MainApp.getToolbarMenuScene().isWorkingListShown()) {
            getFilterListFXMLController().deleteCase();
            return true;
        }
        return false;
    }

    @Override
    public boolean shortcutShiftDelRemove(KeyEvent pEvent) {
        getFilterListFXMLController().deleteCase();
        return true;
    }

    private long recentCaseId;

    @Override
    public boolean shortcutAltLeftBack(KeyEvent pEvent) {
        long caseId = MainApp.getToolbarMenuScene().getDisplayedCaseId();
        if (caseId != 0L) {
            recentCaseId = caseId;
        }
        MainApp.getToolbarMenuScene().showWorkingList();
        return true;
    }

    @Override
    public boolean shortcutAltRightForward(KeyEvent pEvent) {
        if (recentCaseId != 0L) {
            MainApp.getToolbarMenuScene().openCase(recentCaseId);
        }
        return true;
    }

}
