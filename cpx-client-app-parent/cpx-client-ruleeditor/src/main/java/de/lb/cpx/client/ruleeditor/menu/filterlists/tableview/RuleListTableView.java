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
package de.lb.cpx.client.ruleeditor.menu.filterlists.tableview;

import de.lb.cpx.client.core.model.filter.FilterBasicItem;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.model.fx.tableview.FilterBaseTableView;
import de.lb.cpx.client.core.model.fx.tableview.column.FilterColumn;
import de.lb.cpx.client.ruleeditor.menu.filterlists.RuleListColumn;
import de.lb.cpx.client.ruleeditor.menu.filterlists.RuleListFilterManager;
import de.lb.cpx.ruleviewer.event.RefreshEvent;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.server.commonDB.model.rules.PoolTypeEn;
import de.lb.cpx.service.searchlist.SearchListResult;
import de.lb.cpx.shared.filter.ColumnOption;
import de.lb.cpx.shared.filter.FilterOption;
import de.lb.cpx.shared.filter.enums.SearchListAttribute;
import de.lb.cpx.shared.filter.enums.SearchListRuleAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Skin;
import javafx.scene.control.TableColumn;
import static javafx.scene.control.TableView.UNCONSTRAINED_RESIZE_POLICY;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javax.validation.constraints.NotNull;

/**
 * RuleList Tableview to show filterable rules
 *
 * @author wilde
 */
public class RuleListTableView extends FilterBaseTableView<CrgRules> {

    private static final Logger LOG = Logger.getLogger(RuleListTableView.class.getName());
    protected final List<CrgRules> databaseList = new ArrayList<>();

    protected int lastSelected;
    //TODO: implement Filtermanager 
//        RuleListFilterManager filtermanager;

    public RuleListTableView(RuleListFilterManager pManager) {
        super();
        setFilterManager(pManager);
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setShowMenu(true);
        getStyleClass().add("stay-selected-table-view");
        setColumnResizePolicy(UNCONSTRAINED_RESIZE_POLICY);

        ContextMenu menu = new ContextMenu();
        setRowContextMenu(menu);
        menu.setOnShowing(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                menu.getItems().clear();
                menu.getItems().addAll(createContextMenu().getItems());
            }
        });
        getSortOrder().addListener(new ListChangeListener<TableColumn<CrgRules, ?>>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends TableColumn<CrgRules, ?>> change) {
                LOG.info("sortorder list size " + change.getList().size());
                if (change.getList().isEmpty()) {
                    updateColumnSort(getFilter());
                }
            }
        });
    }

    public List<Long> getSelectedRuleIds() {
        List<Long> ids = getSelectionModel().getSelectedItems().stream().map(n -> n.getId()).collect(Collectors.toList());
        return ids;
    }

    public List<Long> getRuleIds(List<CrgRules> pRules) {
        List<Long> ids = pRules.stream().map(n -> n.getId()).collect(Collectors.toList());
        return ids;
    }

    @Override
    public void reload() {
        if (databaseList.isEmpty()) {
            super.reload();
        } else {
            getColumns().clear();
            getFilterItems().clear();
            updateColumns();
        }
//        super.reload(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        Skin<?> sk = super.createDefaultSkin(); //To change body of generated methods, choose Tools | Templates.
        for (Node item : getMenuItems()) {
            if (item.getId() != null && item.getId().equals("reloadButton")) {
                ((ButtonBase) item).setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        RuleListTableView.super.reload();
                    }
                });
            }
        }
        return sk;
    }

    @Override
    public void beforeTask() {
        lastSelected = getSelectionModel().getSelectedIndex();
        databaseList.clear();
        super.beforeTask(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void afterTask(Worker.State pState) {
        super.afterTask(pState); //To change body of generated methods, choose Tools | Templates.
        getSelectionModel().select(lastSelected);
        scrollTo(lastSelected);
        callRefresh();
    }

    @Override
    protected void updateColumns() {
        //clear column list to force redraw of searchitem objects 
        //otherwise only last added item will be shown
        //TODO:FIX ME
        getColumnToSearchItemMap().clear();
        List<TableColumn<CrgRules, ?>> columns = new ArrayList<>();
        getColumns().removeListener(sortOrderListener);
        getSortOrder().clear();
        List<FilterOption> filterSortList = new ArrayList<>();
        Map<Integer, TableColumn<CrgRules, ?>> sortOrder = new HashMap<>();
        List<ColumnOption> columnOptions = getFilter().getColumnsOrdered();
        PoolTypeEn pType = getFilterManager().getPoolType();
        columnOptions.forEach(new Consumer<ColumnOption>() {
            @Override
            public void accept(ColumnOption columnOpt) {
                SearchListRuleAttributes atts = SearchListRuleAttributes.instance();
                SearchListAttribute att = atts.get(columnOpt.attributeName);
                RuleListColumn col = new RuleListColumn(att, pType);

                col.setEditable(true);
                col.setResizable(true);
                col.setPrefWidth(columnOpt.getColumnSize());
                if(SearchListRuleAttributes.ruleMessage.equals(columnOpt.attributeName)){
                    col.setMinWidth(columnOpt.getColumnSize());
                    col.setMaxWidth(columnOpt.getColumnSize());
                    col.setResizable(false);
                    col.setStyle("-fx-alignment: CENTER_RIGHT;");
                }
//                col.setPrefWidth(columnOpt.getColumnSize());


                col.setSortType(null);
                //apply sorting and column options
                if (columnOpt.getSortType() != null) {
                    //check column sort
                    //if not 0 or null than add  
                    if (columnOpt.getSortNumber() != null) {
                        if (columnOpt.getSortType() != null || columnOpt.getSortType().isEmpty()) {
                            //add to sort order if item has sortnumber and sortvalue
                            //due to bug in old list, there are items with sortNumber that have no sort type
                            sortOrder.put(columnOpt.getSortNumber(), col);
                            col.setSortTypeAsString(columnOpt.getSortType());
                        }
                    }
                }

                //List<FilterOption> fopts = filter.getFilter(columnOpt.attributeName);
                List<FilterOption> fopts = getFilter().getFilter(columnOpt.attributeName);
                //List<FilterOption> fopts = RuleListFXMLController.this.getScene().getFilter().getFilter(columnOpt.attributeName);
                if (fopts != null) {
                    for (FilterOption fopt : fopts) {
                        if (fopt.getValue() != null && !fopt.getValue().isEmpty()) {
                            //due to this nested relation in searchList, there could be childs that needs to fetch
                            //identified by database field, if it is contained return it and create item and set in ui
                            SearchListAttribute child = fetchChild(att, fopt.field);
                            FilterBasicItem filterItem = col.setFilterItem(child,
                                    fopt.getValue());
                            addFilterItemInUi(col, filterItem);
                            filterSortList.add(fopt);
                        }
                    }
                }
                col.setOnSortEdit(new Callback<TableColumn.SortType, Void>() {
                    @Override
                    public Void call(TableColumn.SortType param) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                updateColumnSort(getFilter());
                                updateFilter(getFilter(), false);
//                                    saveCurrentFilter();
//                                    if (!att.isClientSide()) {
//                                        updateCurrentFilter();
////                                        changeFilter(filterMenu.getSelectedItem());//getSelectedFilter());
//                                    } else if (TableColumn.SortType.ASCENDING.equals(param)) {
//                                        getItems().sort(col.getComparator());
//                                    } else {
//                                        getItems().sort(col.getComparator().reversed());
//                                    }
                            }
                        });
                        return null;
                    }
                });
                col.setOnFilterEdit(new Callback<FilterBasicItem[], Boolean>() {
                    @Override
                    public Boolean call(FilterBasicItem[] param) {
                        return editFilter(param[0], columnOpt);
                    }
                });

                col.setOnFilterDelete(new Callback<String, Boolean>() {
                    @Override
                    public Boolean call(String param) {
                        return clearFilter(param);
                    }
                });

                columns.add(col);

            }
        });
        filterResultList(filterSortList);
//            System.out.println("ColumnToSearchItemMap Size: "+getColumnToSearchItemMap().size());
//            System.out.println("getFilterItems Size: "+getFilterItems().size());

//        //apply sortOrder to tableview
//        sortOrder.keySet().stream().sorted().forEach(new Consumer<Integer>() {
//            @Override
//            public void accept(Integer t) {
//                if (t != null) {
//                    TableColumn<CrgRules, ?> col = sortOrder.get(t);
//                    getSortOrder().add(col);
//                    col.setSortable(false);
//                    col.setSortable(true);
//                }
//            }
//        });
//        sortOrder.clear();
        getColumns().addAll(columns);
        //apply sortOrder to tableview
        sortOrder.keySet().stream().sorted().forEach(new Consumer<Integer>() {
            @Override
            public void accept(Integer t) {
                if (t != null) {
                    TableColumn<CrgRules, ?> col = sortOrder.get(t);
                    getSortOrder().add(col);
                    col.setSortable(true);
                }
            }
        });
        sortOrder.clear();
        getSortOrder().addListener(new ListChangeListener<TableColumn<CrgRules, ?>>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends TableColumn<CrgRules, ?>> change) {
                LOG.info("test");
            }
        });
        getColumns().addListener(sortOrderListener);
    }
    //get children for given attribute

    private SearchListAttribute fetchChild(SearchListAttribute pAttribute, String pKey) {
        if (pAttribute.getChildren() == null) {
            return pAttribute;
        }
        for (SearchListAttribute att : pAttribute.getChildren()) {
            if (att.key.equals(pKey)) {
                return att;
            }
        }
        return pAttribute;
    }

    public boolean clearFilter(String pKey) {
        getFilter().removeFilter(pKey);
        return true;
    }

    private Boolean editFilter(FilterBasicItem param, ColumnOption columnOpt) {
        return editFilter(param.getDataKey(), param.getValue(), columnOpt);
    }

    private Boolean editFilter(String pDataKey, String pValue, ColumnOption columnOpt) {
        setFilterValues(columnOpt, pDataKey, pValue);
        updateCurrentFilter();
        return true;
    }

    public void setFilterValues(ColumnOption pOption, String pKey,/*FilterBasicItem*/ String pFilterItem) {

        Set<FilterOption> filters = getFilter().getFilters();
        //Set<FilterOption> filters = RuleListFXMLController.this.getScene().getFilter().getFilters();
        //if item is null(filter removed) delete from list and store
        if (pFilterItem == null) {
            filters.removeIf(new Predicate<FilterOption>() {
                @Override
                public boolean test(FilterOption t) {
                    return t.name.equals(pOption.attributeName);
                }
            });
            return;
        }
        //if filter is not null(value updated) set the new value in filter
        for (FilterOption opt : filters) {
            if (opt.field.equals(pKey)) {
                opt.setValue(pFilterItem)/*.getValue()*/;
                return;
            }
        }
        //if filter not exists, create new one
        FilterOption opt = new FilterOption("", pKey, pFilterItem/*.getValue()*/);
        //getFilter().addFilter(opt);
        //filter.addFilter(opt);
        getFilter().addFilter(opt);
        //RuleListFXMLController.this.getScene().getFilter().addFilter(opt);
    }

    public void updateCurrentFilter() {
        //SearchList filter = getFilter();
        //SearchList filter = getLocalFilter();
        SearchListResult filter = getFilter();
        //SearchList filter = RuleListFXMLController.this.getScene().getFilter();
        if (filter != null) {
            LOG.info("update current Filter: " + filter.getName());
            updateColumnSort(filter);
            updateFilter(filter, true);
        }
    }

    //update the sort of the column
    //updates all values in given filter
    public void updateColumnSort(SearchListResult filter) {
        for (ColumnOption opt : filter.getColumns()) {
            FilterColumn<?, ?> col = getColumnForKey(opt.attributeName);
            //if columnOption is not represented in tableview
            if (col == null) {
                continue;
            }
            int sortIndex = getSortOrder().indexOf(col);
            int index = getColumns().indexOf(col);
            String sort = col.getSortType(col.getSortType());

            if (sortIndex >= 0) {
                opt.setSortNumber(sortIndex);
                opt.setSortType(sort);
            } else {
                opt.setSortNumber(null);
                opt.setSortType(null);
            }
            filter.getColumn(col.getDataKey()).setSize((int) col.getWidth());
            opt.setNumber(index);
        }
    }

    //get Filter column for the datakey
    private FilterColumn<?, ?> getColumnForKey(String pKey) {
        if (pKey == null) {
            return null;
        }
        for (TableColumn<?, ?> col : getColumns()) {
            if (col instanceof FilterColumn) {
                if (pKey.equals(((FilterColumn) col).getDataKey())) {
                    return (FilterColumn<?, ?>) col;
                }
            }
        }
        return null;
    }

//        
//        public abstract boolean clearFilter(String pKey);
//        
//        public abstract SearchListDTO updateFilter(SearchList pFilter, boolean pAutoReload);    
    private final ListChangeListener<TableColumn<CrgRules, ?>> sortOrderListener = new ListChangeListener<>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends TableColumn<CrgRules, ?>> c) {
            LOG.info("sort order changed, save filter list");

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    //TODO: implement Filtermanager , temporary workaround 
                    updateColumnSort(getFilter());
                    updateFilter(getFilter(), false);
                }
            });
        }
    };

    @Override
    public List<CrgRules> loadItems(int pStartIndex, int pEndIndex) {
        LOG.warning("empty list returned on reload, maybe override load items to fetch content from anywhere!");
        return new ArrayList<>();
    }

    protected void onDelete(@NotNull List<CrgRules> pRules) {
        Objects.requireNonNull(pRules, "Rule must not be null");
        AlertDialog dialog = AlertDialog.createWarningDialog("Die ausgewählten Regel(n) (" + pRules.size() + ")\nWirklich löschen?", ButtonType.OK, ButtonType.CANCEL);
        dialog.showAndWait().ifPresent(new Consumer<ButtonType>() {
            @Override
            public void accept(ButtonType t) {
                if (ButtonType.OK.equals(t)) {
                    if (getOnDelete().call(pRules)) {
                        callRefresh();
                        //success!
                    }
//                    RuleListFXMLController.this.getScene().deleteRules(pRules);
//                    reload();
                }
            }
        ;
    }

    );
    }
    public ContextMenu createContextMenu() {
        ContextMenu menu = new ContextMenu();
        return menu;
    }

    public void callRefresh() {
        if (getScene() != null) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    RefreshEvent event = new RefreshEvent(RefreshEvent.refreshEvent(), RuleListTableView.this);
                    Event.fireEvent(RuleListTableView.this, event);
                }
            });
//            Event.fireEvent(getScene().getRoot(), event);
        }
    }

    /**
     * local filter method for rule list
     *
     * @param list list of filteroptions for filter
     */
    private void filterResultList(List<FilterOption> list) {
        //not really needed to wrap in observable list
        ObservableList<CrgRules> ruleList = FXCollections.observableArrayList(databaseList);//this.getItems();
        List<CrgRules> rulesToDeleteFromList = new ArrayList<>();
        for (CrgRules cur_rule : ruleList) {
            boolean deleteFromList = false;
            String ruleValue = "";
            for (FilterOption cur_opt : list) {
                boolean valueFound = false;
                String optFieldValue = cur_opt.getValue();
                if (cur_opt.getValue() != null && !cur_opt.getValue().isEmpty()) {
                    if(cur_opt.field.equals(SearchListRuleAttributes.ruleMessage)){
                        ruleValue = cur_rule.getCrgrMessage()!=null?"1":"0";//Objects.requireNonNullElse(cur_rule.getCrgrNumber(), "");
                        valueFound = checkString(ruleValue, optFieldValue);
                    }
                    else if (cur_opt.field.equals(SearchListRuleAttributes.ruleNumber)) {
                        ruleValue = Objects.requireNonNullElse(cur_rule.getCrgrNumber(), "");
                        valueFound = checkString(ruleValue, optFieldValue);
                    } else if (cur_opt.field.equals(SearchListRuleAttributes.ruleCaption)) {
                        ruleValue = Objects.requireNonNullElse(cur_rule.getCrgrCaption(), "");
                        valueFound = checkString(ruleValue, optFieldValue);
                    } else if (cur_opt.field.equals(SearchListRuleAttributes.ruleCategory)) {
                        ruleValue = Objects.requireNonNullElse(cur_rule.getCrgrCategory(), "");
                        valueFound = checkString(ruleValue, optFieldValue);
                    } else if (cur_opt.field.equals(SearchListRuleAttributes.ruleIdent)) {
                        ruleValue = Objects.requireNonNullElse(cur_rule.getCrgrIdentifier(), "");
                        valueFound = checkString(ruleValue, optFieldValue);
                    } else if (cur_opt.field.equals(SearchListRuleAttributes.ruleSuggestion)) {
                        ruleValue = Objects.requireNonNullElse(cur_rule.getCrgrSuggText(), "");
                        valueFound = checkString(ruleValue, optFieldValue);
                    } else if (cur_opt.field.equals(SearchListRuleAttributes.ruleStatus)) {
                        ruleValue = String.valueOf(Objects.requireNonNullElse(cur_rule.getCrgrRuleErrorType().getId(), ""));
                        valueFound = checkString(ruleValue, optFieldValue);
                    } else if (cur_opt.field.equals(SearchListRuleAttributes.ruleType)) {
                        ruleValue = String.valueOf(Objects.requireNonNullElse(cur_rule.getCrgRuleTypes().getId(), ""));
                        valueFound = checkString(ruleValue, optFieldValue);

                    }
                }
                if (!valueFound) {
                    deleteFromList = true;
                }
            }
            if (deleteFromList) {
                rulesToDeleteFromList.add(cur_rule);

            }
        }

        for (CrgRules cur_rule : rulesToDeleteFromList) {
            ruleList.remove(cur_rule);
        }
//            System.out.println("Regelliste vor Filter: "+list.size());
//            System.out.println("Regelliste nach Filter: "+ruleList.size());
        this.setItems(ruleList);
    }

    /**
     * checks strings for filter
     *
     * @param ruleValue
     * @param filterOptValue
     * @return
     */
    private boolean checkString(String ruleValue, String filterOptValue) {
        boolean valueFound = false;
        if (filterOptValue.contains("*")) {
            filterOptValue = filterOptValue.replace("*", "");
            if (ruleValue.contains(filterOptValue)) {
                valueFound = true;
            }
        } else if (filterOptValue.contains(",")) {
            String[] splitted = filterOptValue.split(",");
            for (String str : splitted) {
                if (str.contains("*")) {
                    String value = str.replace("*", "");
                    if (ruleValue.contains(value)) {
                        valueFound = true;
                    }
                } else {
                    if (ruleValue.contains(str)) {
                        valueFound = true;
                    }
                }
            }
        } else if (ruleValue.contains(filterOptValue)) {
            valueFound = true;
        } else {
            // nichts
        }
        return valueFound;
    }

    public String extractValue(CrgRules pValue) {
        Integer year = pValue.getCrgrValidFromYear();
        return year == null ? "----" : year + "";
    }

    /**
     * update filter in manager and trigger reload of tableview TODO: check if
     * ok .. reloading is async setup after this is discouraged?
     *
     * @param pView tableview to reload
     * @param pFilter filter to update manager with
     * @param pAutoReload auto reload/update tableview
     * @return if filter setting was successful
     */
    protected boolean updateFilterInManager(RuleListTableView pView, SearchListResult pFilter, boolean pAutoReload) {
        if (pAutoReload) {
            pView.reload();
        }
        return true;
    }

    //TODO: implement Filtermanager , temporary workaround 
    public SearchListResult updateFilter(SearchListResult pFilter, boolean pAutoReload) {
        updateFilterInManager(this, pFilter, pAutoReload);
        return null;
    }
    private Callback<List<CrgRules>, Boolean> onDelete = getItems()::removeAll;

    public Callback<List<CrgRules>, Boolean> getOnDelete() {
        return onDelete;
    }

    public final void setOnDelete(@NotNull Callback<List<CrgRules>, Boolean> pOnDelete) {
        Objects.requireNonNull(pOnDelete, "OnDelete Callback should not be null");
        onDelete = pOnDelete;
    }
    private ObjectProperty<RuleListFilterManager> filterManagerProperty;

    public ObjectProperty<RuleListFilterManager> filterManagerProperty() {
        if (filterManagerProperty == null) {
            filterManagerProperty = new SimpleObjectProperty<>();
        }
        return filterManagerProperty;
    }

    public RuleListFilterManager getFilterManager() {
        return filterManagerProperty().get();
    }

    public void setFilterManager(@NotNull RuleListFilterManager pManager) {
        Objects.requireNonNull(pManager, "Manager can not be null");
        filterManagerProperty().set(pManager);
    }

    //TODO: implement Filtermanager , temporary workaround 
    public SearchListResult getFilter() {
        if (getFilterManager() != null) {
            return getFilterManager().getSearchList();
        }
        return null;
    }

    public void removeRules(List<CrgRules> pRules) {
        getOnDelete().call(pRules);
    }

    public boolean updateRule(CrgRules pRule) {
        if (containedInDataset(pRule)) {
            int idx = databaseList.indexOf(pRule);
            if (idx == -1) {
                return false;
            }
            databaseList.set(idx, pRule);
            return true;
        }
        return false;
    }

    protected final void clearDataset() {
        databaseList.clear();
    }

    protected boolean containedInDataset(CrgRules pRule) {
        for (CrgRules rule : databaseList) {
            if (rule.getId() == pRule.getId()) {
                return true;
            }
        }
        return false;
    }
}
