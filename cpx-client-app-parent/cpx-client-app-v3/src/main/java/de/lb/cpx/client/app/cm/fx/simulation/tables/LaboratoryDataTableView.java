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
 *    2019  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.cm.fx.simulation.tables;

import de.lb.cpx.client.app.cm.fx.simulation.filtermanager.LaboratoryDataListColumn;
import de.lb.cpx.client.app.cm.fx.simulation.filtermanager.LaboratoryDataListFilterManager;
import de.lb.cpx.client.core.model.filter.FilterBasicItem;
import de.lb.cpx.client.core.model.fx.tableview.FilterBaseTableView;
import de.lb.cpx.client.core.model.fx.tableview.column.FilterColumn;
import de.lb.cpx.client.core.util.ExcelCsvFileManager;
import de.lb.cpx.model.TLab;
import de.lb.cpx.model.enums.ExportTypeEn;
import de.lb.cpx.ruleviewer.event.RefreshEvent;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.service.searchlist.SearchListResult;
import de.lb.cpx.shared.filter.ColumnOption;
import de.lb.cpx.shared.filter.FilterOption;
import de.lb.cpx.shared.filter.enums.LaboratoryDataListAttributes;
import de.lb.cpx.shared.filter.enums.SearchListAttribute;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Skin;
import javafx.scene.control.TableColumn;
import static javafx.scene.control.TableView.UNCONSTRAINED_RESIZE_POLICY;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javax.validation.constraints.NotNull;

/**
 * TableView for the Laboratory Data..
 *
 * @author nandola
 */
public class LaboratoryDataTableView extends FilterBaseTableView<TLab> {

    private static final Logger LOG = Logger.getLogger(LaboratoryDataTableView.class.getName());
    protected List<TLab> laboratoryDataList = new ArrayList<>();
//    protected List<TLab> laboratoryDataList;
    protected int lastSelected;
    private final ListChangeListener<TableColumn<TLab, ?>> sortOrderListener = (ListChangeListener.Change<? extends TableColumn<TLab, ?>> change) -> {

        LOG.info("sort order changed, save filter list");
        Platform.runLater(() -> {
            //TODO: implement Filtermanager, temporary workaround
            updateColumnSort(getFilter());
            updateFilter(getFilter(), false);
        });
    };
    private Callback<List<CrgRules>, Boolean> onDelete = getItems()::removeAll;
    private ObjectProperty<LaboratoryDataListFilterManager> filterManagerProperty;

    public LaboratoryDataTableView() {
        super();

        setItems(FXCollections.observableArrayList(laboratoryDataList));
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setShowMenu(true); // needs to show update (refresh) button?
        setTableMenuButtonVisible(false);
        getStyleClass().add("stay-selected-table-view");
        setColumnResizePolicy(UNCONSTRAINED_RESIZE_POLICY);

        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setOnShowing((WindowEvent t) -> {
            contextMenu.getItems().clear();
            contextMenu.getItems().addAll(createContextMenu().getItems());
        });

        setRowContextMenu(contextMenu);
    }

    public List<TLab> getLaboratoryDataList() {
        return laboratoryDataList;
    }

    public void setLaboratoryDataList(List<TLab> labDataList) {
        laboratoryDataList = labDataList;
    }

    @Override
    public void reload() {
        if (laboratoryDataList.isEmpty()) {
            super.reload();
        } else {
            getColumns().clear();
            getFilterItems().clear();
            updateColumns();
        }
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        Skin<?> sk = super.createDefaultSkin();
        for (Node item : getMenuItems()) {
            if (item.getId() != null && item.getId().equals("reloadButton")) {
                ((ButtonBase) item).setOnAction((ActionEvent t) -> {
                    LaboratoryDataTableView.super.reload();
                });
            }
        }
        return sk;
    }

    @Override
    public void beforeTask() {
        lastSelected = getSelectionModel().getSelectedIndex();
//        laboratoryDataList.clear();
        super.beforeTask();
    }

    @Override
    public void afterTask(Worker.State pState) {
        super.afterTask(pState);
        getSelectionModel().select(lastSelected);
        scrollTo(lastSelected);
        callRefresh();
    }

    @Override
    protected void updateColumns() {

        getColumnToSearchItemMap().clear();

        List<TableColumn<TLab, ?>> columns = new ArrayList<>();
        getColumns().removeListener(sortOrderListener);
        getSortOrder().clear();

        List<FilterOption> filterSortList = new ArrayList<>();
        Map<Integer, TableColumn<TLab, ?>> sortOrder = new HashMap<>();
        List<ColumnOption> columnOptions = getFilter().getColumnsOrdered();

        columnOptions.forEach(new Consumer<ColumnOption>() {
            @Override
            public void accept(ColumnOption columnOpt) {

                LaboratoryDataListAttributes atts = LaboratoryDataListAttributes.instance();

                SearchListAttribute att = atts.get(columnOpt.attributeName);

                LaboratoryDataListColumn col = new LaboratoryDataListColumn(att);

                col.setPrefWidth(columnOpt.getColumnSize());
                col.setEditable(true);
                col.setResizable(true);
                col.setSortType(null);

                //apply sorting and column options
                if (columnOpt.getSortType() != null) {
                    //check column sort
                    if (columnOpt.getSortNumber() != null) {
                        if (columnOpt.getSortType() != null || columnOpt.getSortType().isEmpty()) {
                            sortOrder.put(columnOpt.getSortNumber(), col);
                            col.setSortTypeAsString(columnOpt.getSortType());
                        }
                    }
                }

                List<FilterOption> listOfFilterOptions = getFilterManager().getFilterOptions(columnOpt);//getFilter().getFilter(columnOpt.attributeName);

                if (listOfFilterOptions != null) {
                    for (FilterOption filterOption : listOfFilterOptions) {
                        if (filterOption.getValue() != null && !filterOption.getValue().isEmpty()) {
                            //due to this nested relation in searchList, there could be childs that needs to fetch
                            //identified by database field, if it is contained return it and create item and set in ui
                            SearchListAttribute child = fetchChild(att, filterOption.field);

                            FilterBasicItem filterItem = col.setFilterItem(child,
                                    filterOption.getValue());

                            addFilterItemInUi(col, filterItem);

                            filterSortList.add(filterOption);
                        }
                    }
                }

                col.setOnSortEdit((TableColumn.SortType param) -> {
                    Platform.runLater(() -> {
                        updateColumnSort(getFilter());
                        updateFilter(getFilter(), false);
                    });
                    return null;
                });
                col.setOnFilterEdit((FilterBasicItem[] param) -> editFilter(param, columnOpt));

                col.setOnFilterDelete((String param) -> clearFilter(param));

                columns.add(col);

            }
        });

        filterLaboratoryDataResultList(filterSortList);

        //apply sortOrder to tableview
        sortOrder.keySet().stream().sorted().forEach((Integer t) -> {
            if (t != null) {
                getSortOrder().add(sortOrder.get(t));
            }
        });

        sortOrder.clear();

        getColumns().addAll(columns);
        getColumns().addListener(sortOrderListener);
    }

    @Override
    public List<TLab> loadItems(int pStartIndex, int pEndIndex) {
        LOG.warning("empty list returned on reload, maybe override load items to fetch content from anywhere!");
        return new ArrayList<>();
    }

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

//    private Boolean editFilter(FilterBasicItem param, ColumnOption columnOpt) {
//        return editFilter(param.getDataKey(), param.getValue(), columnOpt);
//    }
    private Boolean editFilter(FilterBasicItem[] params, ColumnOption columnOpt) {
        if (params == null || params.length == 0) {
            return false;
        }
        for (FilterBasicItem param : params) {
            setFilterValues(columnOpt, param.getDataKey(), param.getValue());
        }
        updateCurrentFilter();
        return true;
    }
//    private Boolean editFilter(String pDataKey, String pValue, ColumnOption columnOpt) {
//        setFilterValues(columnOpt, pDataKey, pValue);
//        updateCurrentFilter();
//        return true;
//    }

    public void setFilterValues(ColumnOption pOption, String pKey, String pFilterItem) {

        Set<FilterOption> filters = getFilter().getFilters();

        //if item is null(filter removed) delete from list and store
        if (pFilterItem == null) {
            filters.removeIf((FilterOption t) -> t.name.equals(pOption.attributeName));
            return;
        }

        //if filter is not null(value updated) set the new value in filter
        for (FilterOption opt : filters) {
            if (opt.field.equals(pKey)) {
                opt.setValue(pFilterItem);
                return;
            }
        }

        //if filter not exists, create a new one
        FilterOption opt = new FilterOption("", pKey, pFilterItem);

        getFilter().addFilter(opt);

    }

    public void updateCurrentFilter() {
        SearchListResult filter = getFilter();
        if (filter != null) {
            LOG.log(Level.INFO, "update current Filter: {0}", filter.getName());
            updateColumnSort(filter);
            updateFilter(filter, true);
        }
    }

    //update the sort of the column
    //updates all values in the given filter
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

    public ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem menuItemExportExcel = new MenuItem("Tabelle als XLS exportieren");
        menuItemExportExcel.setOnAction((ActionEvent event) -> {
            final ExcelCsvFileManager mgr = new ExcelCsvFileManager(ExportTypeEn.EXCEL, "Labordaten", this);
            mgr.openDialog(getScene().getWindow());
        });
        MenuItem menuItemExportCsv = new MenuItem("Tabelle als TXT (CSV) exportieren");
        menuItemExportCsv.setOnAction((ActionEvent event) -> {
            final ExcelCsvFileManager mgr = new ExcelCsvFileManager(ExportTypeEn.CSV, "Labordaten", this);
            mgr.openDialog(getScene().getWindow());
        });

        contextMenu.getItems().addAll(menuItemExportExcel, menuItemExportCsv);

        return contextMenu;
    }

//    private ObservableList<Node> getAllMenuItems() {
//        ObservableList<Node> allMenuItems = FXCollections.observableArrayList();
//        Button btn = new Button("+");
//        btn.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent t) {
//            }
//        });
//
//        allMenuItems.addAll(btn);
//
//        return allMenuItems;
//    }
    public void callRefresh() {
        if (getScene() != null) {
            Platform.runLater(() -> {
                RefreshEvent event = new RefreshEvent(RefreshEvent.refreshEvent(), LaboratoryDataTableView.this);
                Event.fireEvent(LaboratoryDataTableView.this, event);
            });
        }
    }

    private void filterLaboratoryDataResultList(List<FilterOption> listOfFilterOptions) {
        ObservableList<TLab> labDataList = FXCollections.observableArrayList(laboratoryDataList);
        listOfFilterOptions = Objects.requireNonNullElse(listOfFilterOptions, new ArrayList<>());
        if (listOfFilterOptions.isEmpty()) {
            //if empty do nothing
            LOG.finer("do not filter entries, list of filter options is empty");
            this.setItems(labDataList);
            return;
        }
        List<TLab> labDataToDeleteFromList = new ArrayList<>();

        for (TLab lab : labDataList) {
            boolean deleteFromList = false;
            String labDataValue = "";

            for (FilterOption filterOption : listOfFilterOptions) {
                boolean isValueFound = false;
                String optFieldValue = filterOption.getValue();

                if (filterOption.getValue() != null && !filterOption.getValue().isEmpty()) {

//                    if (filterOption.field.equals(LaboratoryDataListAttributes.position)) {
                    if (filterOption.field.equalsIgnoreCase(LaboratoryDataListAttributes.position)) {
                        labDataValue = Objects.requireNonNullElse(lab.getLabPosition().toString(), "");
                        isValueFound = checkString(labDataValue, optFieldValue);
                    } else if (filterOption.field.equalsIgnoreCase(LaboratoryDataListAttributes.category)) {
                        labDataValue = Objects.requireNonNullElse(lab.getLabCategory().toString(), "");
                        isValueFound = checkString(labDataValue, optFieldValue);
                    } else if (filterOption.field.equalsIgnoreCase(LaboratoryDataListAttributes.lockDel)) {
                        labDataValue = Objects.requireNonNullElse(lab.getLabLockdel().toString(), "");
                        isValueFound = checkString(labDataValue, optFieldValue);
                    } else if (filterOption.field.equalsIgnoreCase(LaboratoryDataListAttributes.date)
                            || filterOption.field.equalsIgnoreCase(LaboratoryDataListAttributes.dateEqual)
                            || filterOption.field.equalsIgnoreCase(LaboratoryDataListAttributes.dateFrom)
                            || filterOption.field.equalsIgnoreCase(LaboratoryDataListAttributes.dateTo)) {
                        labDataValue = Objects.requireNonNullElse(Lang.toIsoDate(lab.getLabDate()), "");
                        isValueFound = checkString(labDataValue, optFieldValue);
                    } else if (filterOption.field.equalsIgnoreCase(LaboratoryDataListAttributes.analysisDate)
                            || filterOption.field.equalsIgnoreCase(LaboratoryDataListAttributes.analysisDateEqual)
                            || filterOption.field.equalsIgnoreCase(LaboratoryDataListAttributes.analysisDateFrom)
                            || filterOption.field.equalsIgnoreCase(LaboratoryDataListAttributes.analysisDateTo)) {
                        labDataValue = Objects.requireNonNullElse(Lang.toIsoDate(lab.getLabAnalysisDate()), "");
                        isValueFound = checkString(labDataValue, optFieldValue);
                    } else if (filterOption.field.equalsIgnoreCase(LaboratoryDataListAttributes.valueOrText)) {
                        labDataValue = Objects.requireNonNullElse(lab.getLabText(), "");
                        isValueFound = checkString(labDataValue, optFieldValue);
                    } else if (filterOption.field.equalsIgnoreCase(LaboratoryDataListAttributes.description)) {
                        labDataValue = Objects.requireNonNullElse(lab.getLabDescription(), "");
                        isValueFound = checkString(labDataValue, optFieldValue);
                    } else if (filterOption.field.equalsIgnoreCase(LaboratoryDataListAttributes.group)) {
                        labDataValue = Objects.requireNonNullElse(lab.getLabGroup(), "");
                        isValueFound = checkString(labDataValue, optFieldValue);
                    } else if (filterOption.field.equalsIgnoreCase(LaboratoryDataListAttributes.kisExtern)) {
                        labDataValue = Objects.requireNonNullElse(lab.getLabKisExternKey(), "");
                        isValueFound = checkString(labDataValue, optFieldValue);
                    } else if (filterOption.field.equalsIgnoreCase(LaboratoryDataListAttributes.comment)) {
                        labDataValue = Objects.requireNonNullElse(lab.getLabComment(), "");
                        isValueFound = checkString(labDataValue, optFieldValue);
                    } else if (filterOption.field.equalsIgnoreCase(LaboratoryDataListAttributes.range)) {
                        labDataValue = Objects.requireNonNullElse(lab.getLabRange(), "");
                        isValueFound = checkString(labDataValue, optFieldValue);
                    } else if (filterOption.field.equalsIgnoreCase(LaboratoryDataListAttributes.area)) {
                        labDataValue = Objects.requireNonNullElse(lab.getLabBenchmark(), "");
                        isValueFound = checkString(labDataValue, optFieldValue);
                    } else if (filterOption.field.equalsIgnoreCase(LaboratoryDataListAttributes.analysis)) {
                        labDataValue = Objects.requireNonNullElse(lab.getLabAnalysis(), "");
                        isValueFound = checkString(labDataValue, optFieldValue);
                    } else if (filterOption.field.equalsIgnoreCase(LaboratoryDataListAttributes.unit)) {
                        labDataValue = Objects.requireNonNullElse(lab.getLabUnit(), "");
                        isValueFound = checkString(labDataValue, optFieldValue);
                    } else if (filterOption.field.equalsIgnoreCase(LaboratoryDataListAttributes.method)) {
                        labDataValue = Objects.requireNonNullElse(lab.getLabMethod(), "");
                        isValueFound = checkString(labDataValue, optFieldValue);
                    } else if (filterOption.field.equalsIgnoreCase(LaboratoryDataListAttributes.value1)) {
                        labDataValue = Objects.requireNonNullElse(lab.getLabValue().toString(), "");
                        isValueFound = checkString(labDataValue, optFieldValue);
                    } else if (filterOption.field.equalsIgnoreCase(LaboratoryDataListAttributes.value2)) {
                        labDataValue = Objects.requireNonNullElse(lab.getLabValue2().toString(), "");
                        isValueFound = checkString(labDataValue, optFieldValue);
                    } else if (filterOption.field.equalsIgnoreCase(LaboratoryDataListAttributes.minLimit)) {
                        labDataValue = Objects.requireNonNullElse(lab.getLabMinLimit().toString(), "");
                        isValueFound = checkString(labDataValue, optFieldValue);
                    } else if (filterOption.field.equalsIgnoreCase(LaboratoryDataListAttributes.maxLimit)) {
                        labDataValue = Objects.requireNonNullElse(lab.getLabMaxLimit().toString(), "");
                        isValueFound = checkString(labDataValue, optFieldValue);
                    }

                }

                // if value is not found, add it to the delete List.
                if (!isValueFound) {
                    deleteFromList = true;
                }
            }

            if (deleteFromList) {
                labDataToDeleteFromList.add(lab);
            }
        }

        for (TLab lab : labDataToDeleteFromList) {
            labDataList.remove(lab);
        }

        this.setItems(labDataList);

    }

    private boolean checkString(String labDataValue, String filterOptValue) {
        labDataValue = labDataValue.toLowerCase();
        filterOptValue = filterOptValue.toLowerCase();

        boolean valueFound = false;
        if (filterOptValue.contains("*")) {
            filterOptValue = filterOptValue.replace("*", "");
            if (labDataValue.contains(filterOptValue)) {
                valueFound = true;
            }
        } else if (filterOptValue.contains(",")) {
            String[] splitted = filterOptValue.split(",");
            for (String str : splitted) {
                if (str.contains("*")) {
                    String value = str.replace("*", "");
                    if (labDataValue.contains(value)) {
                        valueFound = true;
                    }
                } else {
                    if (labDataValue.contains(str)) {
                        valueFound = true;
                    }
                }
            }
        } else if (labDataValue.contains(filterOptValue)) {
            valueFound = true;
        } else {
        }
        return valueFound;
    }

    protected boolean updateFilterInManager(LaboratoryDataTableView pView, SearchListResult pFilter, boolean pAutoReload) {
//        updateFilter();
        getFilterManager().setUpFilterForSearchList(pFilter);
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

    public Callback<List<CrgRules>, Boolean> getOnDelete() {
        return onDelete;
    }

    public final void setOnDelete(@NotNull Callback<List<CrgRules>, Boolean> pOnDelete) {
        Objects.requireNonNull(pOnDelete, "OnDelete Callback should not be null");
        onDelete = pOnDelete;
    }

    public ObjectProperty<LaboratoryDataListFilterManager> filterManagerProperty() {
        if (filterManagerProperty == null) {
            filterManagerProperty = new SimpleObjectProperty<>();
        }
        return filterManagerProperty;
    }

    public LaboratoryDataListFilterManager getFilterManager() {
        return filterManagerProperty().get();
    }

    public void setFilterManager(@NotNull LaboratoryDataListFilterManager pManager) {
        Objects.requireNonNull(pManager, "Manager can not be null");
        filterManagerProperty().set(pManager);
    }

    //TODO: implement Filtermanager, temporary workaround 
    public SearchListResult getFilter() {
        if (getFilterManager() != null) {
            return getFilterManager().getSearchList();
        }
        return null;
    }

    public void removeLaboratoryData(List<CrgRules> pRules) {
        getOnDelete().call(pRules);
    }

    public boolean updateLaboratoryData(TLab lab) {
        if (containedInDataset(lab)) {
            int idx = laboratoryDataList.indexOf(lab);
            if (idx == -1) {
                return false;
            }
            laboratoryDataList.set(idx, lab);
            return true;
        }
        return false;
    }

    protected final void clearDataset() {
        laboratoryDataList.clear();
    }

    protected boolean containedInDataset(TLab labData) {
        for (TLab lab : laboratoryDataList) {
            if (lab.getId() == labData.getId()) {
                return true;
            }
        }
        return false;
    }

}
