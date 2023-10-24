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
package de.lb.cpx.client.app.menu.fx.filter.tableview;

import de.lb.cpx.client.app.menu.fx.filter.menu.FilterSelectionMenu;
import de.lb.cpx.client.app.menu.fx.filterlists.processes.WorkflowListColumn;
import de.lb.cpx.client.app.menu.fx.table_master_detail.FilterListFXMLController;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.menu.fx.filterlists.SearchListColumn;
import de.lb.cpx.client.core.model.filter.FilterBasicItem;
import de.lb.cpx.client.core.model.fx.adapter.WeakPropertyAdapter;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextField;
import de.lb.cpx.client.core.model.fx.pane.AsyncPane;
import de.lb.cpx.client.core.model.fx.popover.AutoFitPopOver;
import de.lb.cpx.client.core.model.fx.tableview.FilterBaseTableView;
import de.lb.cpx.client.core.model.fx.tableview.column.FilterColumn;
import de.lb.cpx.client.core.model.fx.tableview.searchitem.SearchItem;
import de.lb.cpx.client.core.model.fx.textfield.FilterTextField;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.client.core.util.shortcut.ShortcutHandler;
import de.lb.cpx.model.enums.CpxEnumInterface;
import de.lb.cpx.model.enums.SearchListTypeEn;
import de.lb.cpx.service.searchlist.SearchListResult;
import de.lb.cpx.shared.dto.SearchItemDTO;
import de.lb.cpx.shared.filter.ColumnOption;
import de.lb.cpx.shared.filter.FilterOption;
import de.lb.cpx.shared.filter.enums.SearchListAttribute;
import de.lb.cpx.shared.filter.enums.SearchListAttributes;
import de.lb.cpx.shared.filter.enums.WorkingListAttributes;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;
import de.lb.cpx.wm.model.enums.WmEventTypeEn;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SortEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Callback;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.glyphfont.FontAwesome;

/**
 * @author wilde
 *
 * TableView that is filterable handles advanced stuff with filteroption and
 * columnoption separeted from basic interaction due to reusability
 * @param <T> Content of the TableView
 *
 * TODO:Unify Behavior to add SearchListItems when columns are processed
 * @param <F> type
 */
public abstract class FilterTableView<T extends SearchItemDTO, F extends FilterListFXMLController<T>> extends FilterBaseTableView<T> implements ShortcutHandler {

    private static final Logger LOG = Logger.getLogger(FilterTableView.class.getName());
    public static final String ADD_SEARCH_ITEM = "add_search_item";
    private ChangeListener<Number> updateTableInfosListener;

    //filter selection menu to get the currently selected filter by the user
    private final FilterSelectionMenu<SearchListResult> filterMenu;
    private final Button btnAdditionalFilter;
    private AdditionalFilterPopOver popOverAdditionalFilter;
    private ListChangeListener<TableColumn<T, ?>> sortOrderListener = new ListChangeListener<>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends TableColumn<T, ?>> c) {
            LOG.info("sort order changed, save filter list");
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    updateFilter(getFilter(), false);
                }
            });
        }
    };

    private Map<SearchListAttribute, ColumnOption> mapAdditionalFilter = new HashMap<>();

    private Map<String, String> mapAdditionalFilterValue = new HashMap<>();
    //DEFAULT filterColumn Factory
    //loads searchlist attribute and build simple filter Column instance for filter option
    //TODO build new default one
    private Callback<SearchListAttribute, FilterColumn<T, ? extends Object>> filterColumnFactory = new Callback<>() {
        @Override
        public FilterColumn<T, ? extends Object> call(SearchListAttribute param) {
            //return new FilterColumn(param.getLanguageKey(),param.getKey(),param.getDataType());
            return new SearchListColumn<>(param);
        }
    };
    private boolean showTooltip = true;
    private final F filterListFXMLController;
    private final WeakPropertyAdapter propAdapter;

    @SuppressWarnings("rawtypes")
    public FilterTableView(F pFilterListFXMLController) {
        super();
        propAdapter = new WeakPropertyAdapter();
        //define filtermenu
        filterListFXMLController = pFilterListFXMLController;
        filterMenu = new FilterSelectionMenu<>();
        filterMenu.setListTyp(getListType());
        filterMenu.setFont(Font.font(15));
        filterMenu.setText("Filter: " + getFilterName());
        filterMenu.setTooltip(new Tooltip("Filtereinstellungen"));
        //filterMenu.setSelectedItem(CpxClientConfig.instance().getSelectedSearchListDTO(getListType()));//(getFilter());
        filterMenu.setSelectedItem(MenuCache.getMenuCacheSearchLists().getSelectedSearchList(getListType()));//(getFilter());
        //TODO:Refactor, prevent if list type rule the creation and editing of filters
        if (SearchListTypeEn.RULE.equals(getListType())) {
            filterMenu.setDisableFilterCreation(true);
            filterMenu.setDisableFilterEdit(true);
        }
        filterMenu.setOnCreateCallback(new Callback<String, SearchListResult>() {
            @Override
            public SearchListResult call(String param) {
                //creates new filter based on default columns
                SearchListResult filter = createFilter(param);
                saveFilter(filter);
                return filter;
            }
        });
        filterMenu.setOnPersistCallback(new Callback<SearchListResult, SearchListResult>() {
            @Override
            public SearchListResult call(SearchListResult param) {
                if (param == null) {
                    return null;
                }
//                SearchList filter = getFilter();
//                updateColumnSort(filter);
                return null;//saveFilter(filter);
            }
        });
        filterMenu.setOnDeleteCallback(new Callback<SearchListResult, Boolean>() {
            @Override
            public Boolean call(SearchListResult param) {
                return deleteFilter(param);
            }
        });
        filterMenu.setOnCopyCallback(new Callback<SearchListResult, SearchListResult>() {
            @Override
            public SearchListResult call(SearchListResult param) {
                SearchListResult filter = copyFilter(param);
                saveFilter(filter);
                return filter;
            }
        });

        filterMenu.setOnUpdateCallback(new Callback<SearchListResult, SearchListResult>() {
            @Override
            public SearchListResult call(SearchListResult param) {
                //do not autoreload, otherwise list will most likely be loaded multiple times!
                return updateFilter(param, false);
            }
        });
        filterMenu.setOnLoadCallback(new Callback<Void, List<SearchListResult>>() {
            @Override
            public List<SearchListResult> call(Void param) {
                //return CpxClientConfig.instance().getSearchListDTOs(getListType()).stream().collect(Collectors.toList());
                List<SearchListResult> list = MenuCache.getMenuCacheSearchLists().values(getListType());
                Collections.sort(list);
                return list;
            }
        });
        //load items on main thread to avoid async loading, not neccessary anymore
        //makes only problems due to import of filter files, popover must be shown otherwise with current implemenation
        //TODO:FIX-ME do refactor!
        //filterMenu.setItems(CpxClientConfig.instance().getSearchListDTOs(getListType()).stream().collect(Collectors.toList()));
        filterMenu.setSelectedItem(filterMenu.getActive());
        filterMenu.setOnChangeCallback(new Callback<SearchListResult, Boolean>() {
            @Override
            public Boolean call(SearchListResult param) {
                //TODO:FIX ME, try to update here maxCount to react to non static computation of max items
//                setMaxCount(0);
                return changeFilter(param);
            }
        });
        //add properties listener to get indented refresh/reload of the tableview
        filterMenu.getProperties().addListener(new MapChangeListener<Object, Object>() {
            @Override
            public void onChanged(MapChangeListener.Change<? extends Object, ? extends Object> change) {
                if (change.wasAdded()) {
                    if (com.sun.javafx.scene.control.Properties.REFRESH.equals(change.getKey())) {
                        reload();
                        filterMenu.getProperties().remove(com.sun.javafx.scene.control.Properties.REFRESH);
                    }
                }
            }
        });
        addMenuItem(0, filterMenu);
        //interrupt sort events on the tableview, to sort on serverside
        addEventHandler(SortEvent.ANY, new EventHandler<SortEvent>() {
            @Override
            public void handle(SortEvent event) {
                event.consume();
            }
        });

        //additional Filter Menu
        //is not added due to optinal Menu, if there is a SearchListAttribute marked as no_column
        btnAdditionalFilter = new Button();
        btnAdditionalFilter.getStyleClass().add("cpx-icon-button");
        btnAdditionalFilter.setTooltip(new Tooltip("Zusätzliche Filter"));
        btnAdditionalFilter.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.FILTER));
        btnAdditionalFilter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                if (popOverAdditionalFilter == null) {
                popOverAdditionalFilter = new AdditionalFilterPopOver();
//                }
//                popOverAdditionalFilter = new AdditionalFilterPopOver();
                if (!popOverAdditionalFilter.isShowing()) {
                    popOverAdditionalFilter.show(btnAdditionalFilter);
                    //AWi - Waiting task as workaround
                    //there is now(20200612) for me no easy way to detect when popover is finished displaying
                    //setOnShown is not called on popover and WindowEvent.SHOWN is called multiple times
                    //set async waiting to ensure that ui work of displaying popover(stage) is not tried to
                    //be executed at the same time as adding components to the ui
                    //resulting in lagging ui - waiting task should ensure that these task are executed in the
                    //correct order
                    Task<Void> waitToShow = new Task() {
                        @Override
                        protected Void call() throws Exception {
                            try {
                                Thread.sleep(25);
                            } catch (InterruptedException e) {
                                LOG.log(Level.WARNING, "Interrupted WaitingThread!", e);
                                // Restore interrupted state...
                                Thread.currentThread().interrupt();
                            }
                            return null;
                        }

                    };
                    waitToShow.setOnFailed(new EventHandler() {
                        @Override
                        public void handle(Event t) {
                            //this should not happen at all
                            LOG.severe("Somehow waiting Task failed in showing AdditionalFilter!!!! This should really not happend!");
                            LOG.log(Level.SEVERE, "Exception: {0}", waitToShow.getException() == null ? "null" : waitToShow.getException().getMessage());
                        }
                    });
                    waitToShow.setOnSucceeded(new EventHandler() {
                        @Override
                        public void handle(Event t) {
                            //if task is finished load content
                            popOverAdditionalFilter.loadContent();
                        }
                    });
                    new Thread(waitToShow).start();
                }
            }
        });
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        propAdapter.addChangeListener(currentCountProperty(),new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                getFilterListFXMLController().updateTvInfos();
                if (updateTableInfosListener == null) {
                    updateTableInfosListener = (ObservableValue<? extends Number> ov, Number t, Number t1) -> {
                        getFilterListFXMLController().updateTvInfos();
                    };
                    propAdapter.addChangeListener(getFilterListFXMLController().numberOfFilteredIdsProperty(),updateTableInfosListener);
                }
            }
        });
    }
    

    @Override
    public void dispose() {
        propAdapter.dispose();
        super.dispose();
    }
    
    @Override
    public void reload() {
        super.reload(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void beforeTask() {
        cleanFilterItems();
        super.beforeTask(); //To change body of generated methods, choose Tools | Templates.
    }

    public SearchListResult getSelectedFilter() {
        SearchListResult filter = filterMenu.getSelectedItem();
        //return CpxClientConfig.instance().getSearchList(filter.getType(), filter.getId());//filterMenu.getSelectedItem();
        return MenuCache.getMenuCacheSearchLists().get(filter.getId(), filter.getType());//filterMenu.getSelectedItem();
    }

    //doubious code from old implementation 
    //TODO: check
    private SearchListResult createFilter(String pFilterName) {
        return MenuCache.getMenuCacheSearchLists().createNewSearchList(getListType(), pFilterName, true);
    }

    private SearchListResult copyFilter(@NotNull SearchListResult pToCopy) {
        Objects.requireNonNull(pToCopy, "search list to copy can not be null");
        return MenuCache.getMenuCacheSearchLists().copySearchList(pToCopy, true);
    }

    /**
     * load all columns from filtermanager uses FilterColumn factory to fetch
     * result Factory decides what to show as column and what not
     */
    @Override
    protected void updateColumns() {
        if (getColumnOptions() == null) {
            LOG.warning("do not update Columns, no ColumnOptions set!");
            return;
        }
        getColumns().removeListener(sortOrderListener);
        Stream<ColumnOption> filterOptions = getColumnOptions();
        //build sortorder map to apply sorted order to tableview
        //and to avoid searching the list twice
        Map<Integer, TableColumn<T, ?>> sortOrder = new HashMap<>();
        List<TableColumn<T, ?>> colList = new ArrayList<>();
        filterOptions.forEach(new Consumer<ColumnOption>() {
            @Override
            public void accept(ColumnOption columnOpt) {
                SearchListAttributes attributes = getSearchListAttributes();
                if (attributes == null) {
                    return;
                }
                SearchListAttribute att = attributes.get(columnOpt.attributeName);
                if (att == null) {
                    return;
                }
                //process no column filter
                //TODO: if there is the need to show differnt controls here other than textfield
                //transformFilterNodes must be somehow extracted from filterColumn by some extra class?
                if (att.isNoColumn()) {
                    addToAdditionalFilter(att, columnOpt);
                    return;
                }
                if (!att.isVisible()) {
                    return;
                }
                if (!columnOpt.isShouldShow()) {
                    return;
                }
                FilterColumn<T, ? extends Object> col = getFilterColumnFactory().call(att);
                if (col != null) {
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
                    col.setPrefWidth(columnOpt.getColumnSize());
                    //apply filterOptions
                    List<FilterOption> fopts = getFilterOptions(columnOpt);
                    for (FilterOption fopt : fopts) {
                        if (fopt.getValue() != null && !fopt.getValue().isEmpty()) {
                            //due to this nested relation in searchList, there could be childs that needs to fetch
                            //identified by database field, if it is contained return it and create item and set in ui
                            SearchListAttribute child = fetchChild(att, fopt.field);
                            FilterBasicItem filterItem = col.setFilterItem(child,
                                    fopt.getValue());
                            addFilterItemInUi(col, filterItem);
                        }
                    }
                    if (!att.isSortable()) {
                        //don't sort row number column for example!
                        col.setSortable(false);
                    } else {
                        col.setOnSortEdit(new Callback<TableColumn.SortType, Void>() {
                            @Override
                            public Void call(TableColumn.SortType param) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    @SuppressWarnings("unchecked")
                                    public void run() {
//                                    saveCurrentFilter();
                                        if (!att.isClientSide()) {
                                            updateCurrentFilter();
//                                        changeFilter(filterMenu.getSelectedItem());//getSelectedFilter());
                                        } else if (TableColumn.SortType.ASCENDING.equals(param)) {
                                            //client side sorting (ascending)
                                            getItems().sort((Comparator<T>) col.getComparator());
                                        } else {
                                            //client side sorting (descending)
                                            getItems().sort((Comparator<T>) col.getComparator().reversed());
                                        }
                                    }
                                });
                                return null;
                            }
                        });
                    }
                    col.setOnFilterEdit(new Callback<FilterBasicItem[], Boolean>() {
                        @Override
                        public Boolean call(FilterBasicItem[] param) {
                            return editFilter(param, columnOpt);
                        }
                    });
                    col.setOnFilterDelete(new Callback<String, Boolean>() {
                        @Override
                        public Boolean call(String param) {
                            return clearFilter(param);
                        }
                    });

                    //register columns and set listeners for filters
                    //do not do it in methode, performance will suffer if
                    //getColumns.add() is called
                    colList.add(col);
                }
            }

        });
        //apply sortOrder to tableview
        sortOrder.keySet().stream().sorted().forEach(new Consumer<Integer>() {
            @Override
            public void accept(Integer t) {
                if (t != null) {
                    getSortOrder().add(sortOrder.get(t));
                }
            }
        });
        sortOrder.clear();
//        if (getSelectionModel().getSelectionMode().equals(SelectionMode.MULTIPLE)) {
//            getSelectionModel().getSelectedItems().removeListener(selectionChangeListener);
//            getColumns().add(0, selectionColumn);
//            getSelectionModel().getSelectedItems().addListener(selectionChangeListener);
//        }
        getColumns().addAll(colList);
        getColumns().addListener(sortOrderListener);
        colList.clear();
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

    //add searchAttribute to additional filter popover
    //popover and button only available if there is one additonalFilter attribute set
    @SuppressWarnings("unchecked")
    private void addToAdditionalFilter(SearchListAttribute pAtt, ColumnOption pOption) {
        //don't display additionalFilter when quota-list is showing
        if (!SearchListTypeEn.QUOTA.equals(getListType())) {
            //add to menu when special filter exists
            if (!getMenuItems().contains(btnAdditionalFilter)) {
                getMenuItems().add(1, btnAdditionalFilter);
            }
            mapAdditionalFilter.put(pAtt, pOption);
        }
//set own searchItem Objects 
        List<FilterOption> fopts = getFilterOptions(pOption);
        for (FilterOption fopt : fopts) {
            if (fopt.getValue() != null && !fopt.getValue().isEmpty()) {
                Translation trans = Lang.get(pAtt.getLanguageKey());
//                LabeledTextField textField = popOverAdditionalFilter.getFieldForKey(pAtt.getKey());
//                String text = trans.getValue() + ":\n" + fopt.getValue();//textField.getText();
                String text = trans.getValue() + ":\n" + fopt.getValue();//textField.getText();
                if (fopt.field.equals(WorkingListAttributes.rules)) {
//                    text = Lang.getRuleColumnIdent() + ":\n" + Arrays.stream(fopt.getValue().split(",")).map((String item) -> {
                    text = Lang.getRuleColumnNumber() + " (" + Lang.getRuleFilterDialogPoolYear() + ")" + ":\n" + Arrays.stream(fopt.getValue().split(",")).map((String item) -> {
                        item = StringUtils.trimToEmpty(item);
                        if (item.isEmpty()) {
                            return "";
                        }
                        Long regelId;
                        String regelNummer;
                        try {
                            regelId = Long.valueOf(item);
                            regelNummer = MenuCache.instance().getRuleNumber(regelId);
                            if (regelNummer == null) {
                                LOG.log(Level.SEVERE, "Could not found  regelNummer  with id ''{0}''!", regelId);
                                return String.valueOf(regelId);
                            } else {
                                return regelNummer;
                            }
                        } catch (NumberFormatException ex) {
                            LOG.log(Level.SEVERE, "This is not a valid audit regelNummer: " + item, ex);
                            return item;
                        }
                    }).collect(Collectors.joining(","));
                }
                //CPX-1108 RSH  20180814 :MdkAuditReasons Filter
                if (pAtt.isMdkAuditReasonsMap()) {
                    text = trans.getValue() + ":\n" + Arrays.stream(fopt.getValue().split(",")).map((String item) -> {
                        item = StringUtils.trimToEmpty(item);
                        if (item.isEmpty()) {
                            return "";
                        }
                        Integer auditReasonNumber;
                        String auditReason;
                        try {
                            auditReasonNumber = Integer.valueOf(item);
                            auditReason = MenuCache.instance().getAuditReasonForNumber(auditReasonNumber);
                            if (auditReason == null) {
                                LOG.log(Level.SEVERE, "Could not found audit reason with number ''{0}''!", auditReasonNumber);
                                return String.valueOf(auditReasonNumber);
                            } else {
                                return auditReason;
                            }
                        } catch (NumberFormatException ex) {
                            LOG.log(Level.SEVERE, "This is not a valid audit reason number: " + item, ex);
                            return item;
                        }
                    }).collect(Collectors.joining(","));
                }
                if (pAtt.isMdkStatesMap()) {
                    text = trans.getValue() + ":\n" + Arrays.stream(fopt.getValue().split(",")).map((String item) -> {
                        item = StringUtils.trimToEmpty(item);
                        if (item.isEmpty()) {
                            return "";
                        }
                        Long stateNumber;
                        String state;
                        try {
                            stateNumber = Long.valueOf(item);
                            state = MenuCache.instance().getRequestStatesForInternalId(stateNumber);
                            if (state == null) {
                                LOG.log(Level.SEVERE, "Could not found mdk state with number ''{0}''!", stateNumber);
                                return String.valueOf(stateNumber);
                            } else {
                                return state;
                            }
                        } catch (NumberFormatException ex) {
                            LOG.log(Level.SEVERE, "This is not a valid mdk state number: " + item, ex);
                            return item;
                        }
                    }).collect(Collectors.joining(","));
                }
                // cpx-1734
                /*            if (pAtt.isDepartmentMap()) {
//                if (pAtt.key.equals(WorkingListAttributes.caseDepartments)) {
                    text = trans.getValue() + ":\n" + Arrays.stream(fopt.getValue().split(",")).map((String item) -> {
                        item = StringUtils.trimToEmpty(item);
                        if (item.isEmpty()) {
                            return "";
                        }
                        String deptShortName = null;
                        String deptKey301 = null;

                        deptShortName = item;
                        deptKey301 = MenuCache.instance().getDeptKey301FromDeptShortName(deptShortName);
                        if (deptKey301 == null) {
                            LOG.log(Level.SEVERE, "Could not found dept ShortKey with dept Short name ''{0}''!", deptShortName);
                            return deptShortName;
                        } else {
                            return deptShortName + " (" + deptKey301 + ")";
                        }

                    }).collect(Collectors.joining(","));
                }  */
                if (pAtt.isActionSubjectMap()) {
                    text = trans.getValue() + ":\n" + Arrays.stream(fopt.getValue().split(",")).map((String item) -> {
                        item = StringUtils.trimToEmpty(item);
                        if (item.isEmpty()) {
                            return "";
                        }
                        Long actionSubjectNumber;
                        String actionSubject;
                        try {
                            actionSubjectNumber = Long.valueOf(item);
                            actionSubject = MenuCache.instance().getActionSubjectName(actionSubjectNumber);
                            if (actionSubject == null) {
                                LOG.log(Level.SEVERE, "Could not found mdk state with number ''{0}''!", actionSubjectNumber);
                                return String.valueOf(actionSubjectNumber);
                            } else {
                                return actionSubject;
                            }
                        } catch (NumberFormatException ex) {
                            LOG.log(Level.SEVERE, "This is not a valid mdk state number: " + item, ex);
                            return item;
                        }
                    }).collect(Collectors.joining(","));
                }
                if (pAtt.isEnum()) {
                    text = trans.getValue() + ":\n" + Arrays.stream(fopt.getValue().split(",")).map((String item) -> {
                        item = StringUtils.trimToEmpty(item);
                        if (item.isEmpty()) {
                            return "";
                        }
                        //Long actionSubjectNumber = null;
                        CpxEnumInterface<?> subject;
                        try {
                            //actionSubjectNumber = Long.valueOf(item);
                            subject = CpxEnumInterface.findEnum(((Class<CpxEnumInterface<?>>) pAtt.getDataType()).getEnumConstants(), item);
                            if (subject == null) {
                                LOG.log(Level.SEVERE, "Could not found enum {0} with id ''{1}''!", new Object[]{String.valueOf(pAtt.getDataType()), item});
                                return String.valueOf(subject);
                            } else {
                                return subject.getTranslation().hasAbbreviation() ? subject.getTranslation().getAbbreviation() : subject.getTranslation().value;
                            }
                        } catch (NumberFormatException ex) {
                            LOG.log(Level.SEVERE, "This is not a valid mdk state number: " + item, ex);
                            return item;
                        }
                    }).collect(Collectors.joining(","));
                }
                if (!getColumnToSearchItemMap().containsKey(pAtt.getKey())) {
                    getColumnToSearchItemMap().put(pAtt.getKey(), new ArrayList<>());
                }
                if (getColumnToSearchItemMap().get(pAtt.getKey()).isEmpty()) {

                    SearchItem item = getAdditionalFilterSearchItemFactory().call(pAtt);//new SearchItem(pAtt.getKey(), text);
                    item.setText(text);
                    mapAdditionalFilterValue.put(pAtt.getKey(), fopt.getValue());
                    if (item.getOnMouseClicked() == null) {
                        //if factory was overwritten and no mouse click is set
                        item.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                if (popOverAdditionalFilter == null) {
                                    popOverAdditionalFilter = new AdditionalFilterPopOver();
                                    popOverAdditionalFilter.loadContent();
                                }
                                if (!popOverAdditionalFilter.isShowing()) {
                                    try {// give him some time to create  all fields in async pane
                                        Thread.sleep(25);
                                    } catch (InterruptedException e) {
                                        LOG.log(Level.WARNING, "Interrupted WaitingThread!", e);

                                    }

                                    popOverAdditionalFilter.show(btnAdditionalFilter);
                                    Control textField = popOverAdditionalFilter.getFieldForKey(pAtt.getKey());
                                    textField.requestFocus();
                                }
                            }
                        });
                    }
                    item.setOnCloseEvent(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            editFilter(pAtt.getKey(), "", pOption);
                            mapAdditionalFilterValue.remove(pAtt.getKey());
                        }
                    });
                    getColumnToSearchItemMap().get(pAtt.getKey()).add(item);
                    addFilterItem(item);
                }
                getColumnToSearchItemMap().get(pAtt.getKey()).get(0).setText(text);
            }
        }
    }

    public String getAdditionalFilterValue(SearchListAttribute pAttribute) {
        return mapAdditionalFilterValue.get(pAttribute.getKey());
    }
    private static final Callback<SearchListAttribute, SearchItem> DEFAULT_ADDITONAL_FILTER_SEARCHITEM_FACTORY = new Callback<SearchListAttribute, SearchItem>() {
        @Override
        public SearchItem call(SearchListAttribute p) {
            return new SearchItem(p.getKey(), "");
        }
    };
    private Callback<SearchListAttribute, SearchItem> additonalFilterSearchItemFactory = DEFAULT_ADDITONAL_FILTER_SEARCHITEM_FACTORY;

    public void setAdditionalFilterSearchItemFactory(@NotNull Callback<SearchListAttribute, SearchItem> pCallback) {
        additonalFilterSearchItemFactory = Objects.requireNonNullElse(pCallback, DEFAULT_ADDITONAL_FILTER_SEARCHITEM_FACTORY);
    }

    public Callback<SearchListAttribute, SearchItem> getAdditionalFilterSearchItemFactory() {
        return additonalFilterSearchItemFactory;
    }

    /**
     * @param pCallback sets callback to build specific column Keep in mind to
     * check SearchListAttribute for NoColumn Prop etc.
     */
    public void setFilterColumnFactory(Callback<SearchListAttribute, FilterColumn<T, ? extends Object>> pCallback) {
        filterColumnFactory = pCallback;
    }

    /**
     * @return current FilterColumnFactory
     */
    public Callback<SearchListAttribute, FilterColumn<T, ? extends Object>> getFilterColumnFactory() {
        return filterColumnFactory;
    }

    public void setFilterValues(ColumnOption pOption, String pKey,/*FilterBasicItem*/ String pFilterItem) {

        Set<FilterOption> filters = getFilter().getFilters();//getSelectedFilter().getFilters();//getFilterOptions(pOption);
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
        getFilter().addFilter(opt);
    }

    public void updateCurrentFilter() {
        SearchListResult filter = getFilter();//getSelectedFilter();
        if (filter != null) {
            LOG.log(Level.INFO, "update current Filter: {0}", filter.getName());
            updateColumnSort(filter);
            updateFilter(filter, true);
        }
    }

    //update the sort of the column
    //updates all values in given filter
    public void updateColumnSort(SearchListResult filter) {
        for (ColumnOption opt : filter.getColumns()) {
            FilterColumn<T, Object> col = getColumnForKey(opt.attributeName);
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
    @SuppressWarnings("unchecked")
    private FilterColumn<T, Object> getColumnForKey(String pKey) {
        if (pKey == null) {
            return null;
        }
        for (TableColumn<T, ?> col : getColumns()) {
            if (col instanceof FilterColumn) {
                if (pKey.equals(((FilterColumn<T, Object>) col).getDataKey())) {
                    return (FilterColumn<T, Object>) col;
                }
            }
        }
        return null;
    }

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
//    private Boolean editFilter(FilterBasicItem param, ColumnOption columnOpt) {
//        return editFilter(param.getDataKey(), param.getValue(), columnOpt);
//    }

    private Boolean editFilter(String pDataKey, String pValue, ColumnOption columnOpt) {
        setFilterValues(columnOpt, pDataKey, pValue);
        updateCurrentFilter();
        return true;
    }

    //TODO:
    //most of the methodes should be avoided to to give searchlist in constructor?
    public abstract SearchListTypeEn getListType();

    public abstract SearchListResult getFilter();

    public abstract SearchListResult updateFilter(SearchListResult pFilter, boolean pAutoReload);

    public abstract Stream<ColumnOption> getColumnOptions();

    public abstract SearchListAttributes getSearchListAttributes();

    public abstract List<FilterOption> getFilterOptions(ColumnOption pOption);

    public abstract String getFilterName();

    public abstract boolean clearFilter(String pKey);

    public abstract SearchListResult saveFilter(SearchListResult pFilter);

    public abstract boolean deleteFilter(SearchListResult pFilter);

    public abstract boolean changeFilter(SearchListResult pFilter);

    //TODO:FIX ME, refactor in own property handling?
    public void addSearchList(SearchListResult pItem) {
        int idx = filterMenu.getItems().indexOf(pItem);
        if (idx > -1) {
            //clear selection
            filterMenu.getItems().set(idx, pItem);
        } else {
            filterMenu.getItems().add(pItem);
        }
        filterMenu.getItems().sort(new Comparator<SearchListResult>() {
            @Override
            public int compare(SearchListResult o1, SearchListResult o2) {
                return (o1).getName().compareTo((o2).getName());
            }
        });
        filterMenu.setSelectedItem(pItem);
        LOG.log(Level.INFO, "add to items {0}", pItem.getName());
    }

    public void clearSelection() {
        filterMenu.setSelectedItem(null);
    }

    @Override
    public void setShowTooltip(boolean pShow) {
        showTooltip = pShow;
    }

    @Override
    public boolean isShowTooltip() {
        return showTooltip;
    }

//    public class SelectionColumn extends TableColumn<T, T> {
//
//        public SelectionColumn() {
//            super("");
//            setSortable(false);
//            setCellValueFactory(new SimpleCellValueFactory<>());
//            setCellFactory(new Callback<TableColumn<T, T>, TableCell<T, T>>() {
//                @Override
//                public TableCell<T, T> call(TableColumn<T, T> param) {
//                    return new TableCell<T, T>() {
//                        private CheckBox box;
//
//                        @Override
//                        protected void updateItem(T item, boolean empty) {
//                            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
//                            if (item == null || empty) {
//                                box = null;
//                                setGraphic(null);
//                                return;
//                            }
////                            getSelectionModel().getSelectedItems().removeListener(selectionChangeListener);
//                            if (box == null) {
//                                box = new CheckBox();
//                                box.setOnAction(new EventHandler<ActionEvent>() {
//                                    @Override
//                                    public void handle(ActionEvent event) {
//                                        if (box.isSelected()) {
//                                            getSelectionModel().select(item);
//                                        } else {
//                                            getSelectionModel().clearSelection(getIndex());
//                                        }
//                                    }
//                                });
////                                getSelectionModel().getSelectedItems().addListener(selectionChangeListener);
//                                setGraphic(box);
//                            }
//                            box.setSelected(getSelectionModel().getSelectedItems().contains(item));
//                        }
//                    };
//                }
//            });
//        }
//    }
//    public abstract FilterListFXMLController<? extends SearchItemDTO> getFilterListFXMLController();
    @Override
    public boolean shortcutF1Help(KeyEvent pEvent) {
        LOG.log(Level.FINEST, "Unhandled F1 shortcut in {0}. Override method to implement shortcut!", getClass().getSimpleName());
        return false;
    }

    @Override
    public boolean shortcutF2New(KeyEvent pEvent) {
        LOG.log(Level.FINEST, "Unhandled F2 shortcut in {0}. Override method to implement shortcut!", getClass().getSimpleName());
        return false;
    }

    @Override
    public boolean shortcutF3Find(KeyEvent pEvent) {
        LOG.log(Level.FINEST, "Unhandled F3 shortcut in {0}. Override method to implement shortcut!", getClass().getSimpleName());
        return false;
    }

    @Override
    public boolean shortcutF4Close(KeyEvent pEvent) {
        LOG.log(Level.FINEST, "Unhandled F4 shortcut in {0}. Override method to implement shortcut!", getClass().getSimpleName());
        return false;
    }

    @Override
    public boolean shortcutF5Refresh(KeyEvent pEvent) {
        LOG.log(Level.FINEST, "Unhandled F5 shortcut in {0}. Override method to implement shortcut!", getClass().getSimpleName());
        return false;
    }

    @Override
    public boolean shortcutEnterExecute(KeyEvent pEvent) {
        LOG.log(Level.FINEST, "Unhandled ENTER shortcut in {0}. Override method to implement shortcut!", getClass().getSimpleName());
        return false;
    }

    @Override
    public boolean shortcutControlPPrint(KeyEvent pEvent) {
        LOG.log(Level.FINEST, "Unhandled Ctrl+P shortcut in {0}. Override method to implement shortcut!", getClass().getSimpleName());
        return false;
    }

    @Override
    public boolean shortcutControlFFind(KeyEvent pEvent) {
        //LOG.log(Level.FINEST, "Unhandled Ctrl+F shortcut in " + getClass().getSimpleName() + ". Override method to implement shortcut!");
        return shortcutF3Find(pEvent);
    }

    @Override
    public boolean shortcutControlWClose(KeyEvent pEvent) {
        //LOG.log(Level.FINEST, "Unhandled Ctrl+W shortcut in " + getClass().getSimpleName() + ". Override method to implement shortcut!");
        return shortcutF4Close(pEvent);
    }

    @Override
    public boolean shortcutControlNNew(KeyEvent pEvent) {
        //LOG.log(Level.FINEST, "Unhandled Ctrl+N shortcut in " + getClass().getSimpleName() + ". Override method to implement shortcut!");
        return shortcutF2New(pEvent);
    }

    @Override
    public boolean shortcutControlRRefresh(KeyEvent pEvent) {
        //LOG.log(Level.FINEST, "Unhandled Ctrl+R shortcut in " + getClass().getSimpleName() + ". Override method to implement shortcut!");
        return shortcutF5Refresh(pEvent);
    }

    @Override
    public boolean shortcutControlSSave(KeyEvent pEvent) {
        LOG.log(Level.FINEST, "Unhandled Ctrl+S shortcut in {0}. Override method to implement shortcut!", getClass().getSimpleName());
        return false;
    }

    @Override
    public boolean shortcutAltLeftBack(KeyEvent pEvent) {
        LOG.log(Level.FINEST, "Unhandled Alt+Left shortcut in {0}. Override method to implement shortcut!", getClass().getSimpleName());
        return false;
    }

    @Override
    public boolean shortcutAltRightForward(KeyEvent pEvent) {
        LOG.log(Level.FINEST, "Unhandled Alt+Right shortcut in {0}. Override method to implement shortcut!", getClass().getSimpleName());
        return false;
    }

    @Override
    public boolean shortcutControlCCopy(KeyEvent pEvent) {
        LOG.log(Level.FINEST, "Unhandled Ctrl+C shortcut in {0}. Override method to implement shortcut!", getClass().getSimpleName());
        return false;
    }

    @Override
    public boolean shortcutControlVPaste(KeyEvent pEvent) {
        LOG.log(Level.FINEST, "Unhandled Ctrl+V shortcut in {0}. Override method to implement shortcut!", getClass().getSimpleName());
        return false;
    }

    @Override
    public boolean shortcutDelRemove(KeyEvent pEvent) {
        LOG.log(Level.FINEST, "Unhandled DEL shortcut in {0}. Override method to implement shortcut!", getClass().getSimpleName());
        return false;
    }

    @Override
    public boolean shortcutShiftDelRemove(KeyEvent pEvent) {
        LOG.log(Level.FINEST, "Unhandled SHIFT+DEL shortcut in {0}. Override method to implement shortcut!", getClass().getSimpleName());
        return false;
    }

//    @Override
//    public void shortcutUnhandled(KeyEvent pEvent) {
//        LOG.log(Level.FINEST, "Unhandled shortcut in " + getClass().getSimpleName() + ". Override method to implement shortcut!");
//    }
//
//    @Override
//    public void shortcutUnhandledCombo(KeyEvent pEvent) {
//        LOG.log(Level.FINEST, "Unhandled shortcut combination in " + getClass().getSimpleName() + ". Override method to implement shortcut!");
//    }
//    private static class SearchListAttributeComperator implements Comparator<SearchListAttribute>, Serializable {
//
//        private static final long serialVersionUID = 1L;
//
//        private final HashMap<String, Integer> GROUP = new HashMap<>();
//
//        SearchListAttributeComperator() {
//            super();
//            GROUP.put(WorkingListAttributes.caseIcds, 1);
//            GROUP.put(WorkingListAttributes.admDiagnosis, 1);
//            GROUP.put(WorkingListAttributes.hosDiagnosis, 1);
//            GROUP.put(WorkingListAttributes.departmentMd, 1);
//            GROUP.put(WorkingListAttributes.caseOpses, 2);
//            GROUP.put(WorkingListAttributes.caseFees, 3);
//            GROUP.put(WorkflowListAttributes.mdkAuditReasonsFilter, 1);
//            GROUP.put(WorkingListAttributes.caseDepartments, 1);
//            GROUP.put(WorkingListAttributes.caseDepartmentsKey301, 1);
////            GROUP.put(WorkingListAttributes.caseDepartmentsMap, 1);
//        }
//
//        @Override
//        public int compare(SearchListAttribute o1, SearchListAttribute o2) {
//            int group1, group2;
//            group1 = o1 == null ? Integer.MAX_VALUE : getGroup(o1.getKey());//o1.getPriority();
//            group2 = o2 == null ? Integer.MAX_VALUE : getGroup(o2.getKey());//o2.getPriority();
//            int group = group1 == group2 ? 0 : (group1 < group2 ? -1 : 1);
//            if (group1 != group2) {
//                return group;
//            }
//            if (o1 == null) {
//                LOG.log(Level.WARNING, "SearchListAttribute o1 is null!");
//                return -1;
//            }
//            if (o2 == null) {
//                LOG.log(Level.WARNING, "SearchListAttribute o2 is null!");
//                return -1;
//            }
//            //return o1.getLanguageKey().compareTo(o2.getLanguageKey());
//            Translation t1 = Lang.get(o1.getLanguageKey());
//            Translation t2 = Lang.get(o2.getLanguageKey());
//            return t1.getValue().compareToIgnoreCase(t2.getValue());
//        }
//
//        public int getGroup(String pGroup) {
//            return GROUP.containsKey(pGroup) ? GROUP.get(pGroup) : Integer.MAX_VALUE;
//        }
//    }
    private class AdditionalFilterPopOver extends AutoFitPopOver {

//        private final VBox boxContent = null;
        private final Map<String, Control> mapKeyToControl = new HashMap<>();
        //private final Map<String, CheckComboBox> mapKeyToControlCombobox = new HashMap<>();
        private final AsyncPane<Node> pane;

        public AdditionalFilterPopOver() {
            super();
            setFitOrientation(Orientation.HORIZONTAL);

            pane = new AsyncPane<Node>(false) {
                @Override
                public Node loadContent() {
                    VBox boxContent = new VBox();
                    boxContent.setPadding(new Insets(10));
                    boxContent.setSpacing(5);
                    Iterator<SearchListAttribute> it = mapAdditionalFilter.keySet().stream().sorted(new Comparator<SearchListAttribute>() {
                        @Override
                        public int compare(SearchListAttribute o1, SearchListAttribute o2) {
                            Translation t1 = Lang.get(o1.getLanguageKey());
                            Translation t2 = Lang.get(o2.getLanguageKey());
                            return t1.getValue().compareToIgnoreCase(t2.getValue());
                        }
                    }).collect(Collectors.toList()).iterator();
                    List<Node> nodes = new ArrayList<>();
                    while (it.hasNext()) {
                        SearchListAttribute next = it.next();
                        Node node = getAdditionalFilter(next, mapAdditionalFilter.get(next));
                        if (node != null) {
                            nodes.add(node);
                        }
                    }

                    boxContent.getChildren().addAll(nodes);
                    return boxContent;
                }

                @Override
                public void beforeTask() {
                    super.beforeTask(); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public void afterTask(Worker.State pState) {
                    super.afterTask(pState); //To change body of generated methods, choose Tools | Templates.
//                    setPrefHeight(USE_COMPUTED_SIZE);
                }

            };
//            pane.setMinSize(230, 150);
            pane.setMinHeight(150);
            pane.setPrefSize(230, USE_COMPUTED_SIZE);
            setContentNode(pane);
        }

        public void loadContent() {
            if (pane == null) {
                return;
            }
            pane.reload();
        }
//        private void addAdditionalFilter(SearchListAttribute pAtt, ColumnOption pOption) {
//            //TODO: Smarter cotnrol creation, is default textfield other formats are not considert
//            String filter = mapAdditionalFilterValue.get(pAtt.getKey());
//            if (pAtt.getDataType() == String.class) {
//                if (!pAtt.getKey().equals(WorkingListAttributes.rules)) {
//                    setFilterTextField(pAtt, filter, pOption);
//                }
//            }
//            //CPX-1108   RSH  20180814 :MdkAuditReasons Filter
//            WorkflowListColumn col = new WorkflowListColumn(pAtt);
//            if (pAtt.isMdkAuditReasonsMap()) {
//                setFilterCombobox(col.getMdkAuditReasonsComboBox(pAtt.getKey(), pAtt.getLanguageKey()), pAtt, filter, pOption);
//            }
//            //CPX 1220 VYe Mdk Status Filter
//            if (pAtt.isMdkStatesMap()) {
//                setFilterCombobox(col.getMdkStatesComboBox(pAtt.getKey(), pAtt.getLanguageKey()), pAtt, filter, pOption);
//            }
//            if (pAtt.isActionSubjectMap()) {
//                setFilterCombobox(col.getActionSubjectsComboBox(pAtt.getKey(), pAtt.getLanguageKey()), pAtt, filter, pOption);
//            }
//            if (pAtt.isEnum() && pAtt.getDataType() == WmEventTypeEn.class) {
//                setFilterCombobox(col.getEventTypeComboBox(pAtt.getKey(), pAtt.getLanguageKey()), pAtt, filter, pOption);
//            }
//        }

        private Node getAdditionalFilter(SearchListAttribute pAtt, ColumnOption pOption) {
            //TODO: Smarter cotnrol creation, is default textfield other formats are not considert
            String filter = mapAdditionalFilterValue.get(pAtt.getKey());
            if (pAtt.getDataType() == String.class) {
                if (!pAtt.getKey().equals(WorkingListAttributes.rules)) {
                    return getFilterTextField(pAtt, filter, pOption);
                }
            }
            //CPX-1108   RSH  20180814 :MdkAuditReasons Filter
            WorkflowListColumn col = new WorkflowListColumn(pAtt);
            if (pAtt.isMdkAuditReasonsMap()) {
                return getFilterCombobox(col.getMdkAuditReasonsComboBox(pAtt.getKey(), pAtt.getLanguageKey()), pAtt, filter, pOption);
            }
            //CPX 1220 VYe Mdk Status Filter
            if (pAtt.isMdkStatesMap()) {
                return getFilterCombobox(col.getMdkStatesComboBox(pAtt.getKey(), pAtt.getLanguageKey()), pAtt, filter, pOption);
            }
            if (pAtt.isActionSubjectMap()) {
                return getFilterCombobox(col.getActionSubjectsComboBox(pAtt.getKey(), pAtt.getLanguageKey()), pAtt, filter, pOption);
            }
            if (pAtt.isEnum() && pAtt.getDataType() == WmEventTypeEn.class) {
                return getFilterCombobox(col.getEventTypeComboBox(pAtt.getKey(), pAtt.getLanguageKey()), pAtt, filter, pOption);
            }
            return null;
        }

//        /**
//         *
//         * @param pAtt SearchListAttribute
//         * @param filter filter
//         * @param pOption ColumnOption
//         */
//        private void setFilterTextField(SearchListAttribute pAtt, String filter, ColumnOption pOption) {
//            LabeledTextField textField = new LabeledTextField(Lang.get(pAtt.getLanguageKey()).getValue(), new FilterTextField());
//            if (filter != null) {
//                textField.setText(filter);
//            }
//            ((FilterTextField) textField.getControl()).filterValueProperty().addListener(new ChangeListener<String>() {
//                @Override
//                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                    editFilter(pAtt.getKey(), newValue, pOption);
//                }
//            });
//            mapKeyToControl.put(pAtt.getKey(), textField);
//            boxContent.getChildren().add(textField);
//        }
        private Control getFilterTextField(SearchListAttribute pAtt, String filter, ColumnOption pOption) {
            LabeledTextField textField = new LabeledTextField(Lang.get(pAtt.getLanguageKey()).getValue(), new FilterTextField());
            if (filter != null) {
                textField.setText(filter);
            }
            ((FilterTextField) textField.getControl()).filterValueProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    editFilter(pAtt.getKey(), newValue, pOption);
                }
            });
            mapKeyToControl.put(pAtt.getKey(), textField);
            return textField;
//            boxContent.getChildren().add(textField);
        }
        private void preSelectValuesInCombobox(CheckComboBox<Map.Entry<Long, String>> pCheckCombobox, String pFilter){
            if(pFilter == null ||pCheckCombobox == null){
                return;
            }
            //2018-08-17 DNi: Preselect entries in combo box from selected filter
            String[] values = pFilter.split(",");
            for (String value : values) {
                if (value.trim().isEmpty()) {
                    continue;
                }
                Integer internalNumber;
                try {
                    internalNumber = Integer.valueOf(value);
                } catch (NumberFormatException ex) {
                    LOG.log(Level.SEVERE, "This is not a valid audit reason number: " + value, ex);
                    continue;
                }
                for (int i = 0; i < pCheckCombobox.getCheckModel().getItemCount(); i++) {
                    Map.Entry<Long, String> item = pCheckCombobox.getCheckModel().getItem(i);
                    if (item.getKey().equals(internalNumber.longValue())) {
                        pCheckCombobox.getCheckModel().check(item);
                    }
                }
            }
        }
        private Node getFilterCombobox(CheckComboBox<Map.Entry<Long, String>> pCheckCombobox, SearchListAttribute pAtt, String filter, ColumnOption pOption) {
            Label comboboxLabel = new Label(Lang.get(pAtt.getLanguageKey()).toString());
            preSelectValuesInCombobox(pCheckCombobox, filter);
//            if (filter != null) {
//                //2018-08-17 DNi: Preselect entries in combo box from selected filter
//                String[] values = filter.split(",");
//                for (String value : values) {
//                    if (value.trim().isEmpty()) {
//                        continue;
//                    }
//                    Integer internalNumber;
//                    try {
//                        internalNumber = Integer.valueOf(value);
//                    } catch (NumberFormatException ex) {
//                        LOG.log(Level.SEVERE, "This is not a valid audit reason number: " + value, ex);
//                        continue;
//                    }
//                    for (int i = 0; i < pCheckCombobox.getCheckModel().getItemCount(); i++) {
//                        Map.Entry<Long, String> item = pCheckCombobox.getCheckModel().getItem(i);
//                        if (item.getKey().equals(internalNumber.longValue())) {
//                            pCheckCombobox.getCheckModel().check(item);
//                        }
//                    }
//                }
//            }
            //CPX-1108 ,CPX-1126
            pCheckCombobox.setMaxWidth(250);
            pCheckCombobox.getStylesheets().add("/styles/cpx-default.css");
            pCheckCombobox.addEventHandler(ComboBox.ON_HIDDEN, event -> {
                if (filter == null && pCheckCombobox.getCheckModel().isEmpty()) {
                    //nothing previously selected and nothing selected now
                    //do nothing
                    return;
                }
                List<Map.Entry<Long, String>> added = pCheckCombobox.getCheckModel().getCheckedItems();

                String searchItem = null;
                if(!added.isEmpty()){
                    //help-set to filter entries if dublicates occure
                    Set<String> items = new HashSet<>();
                    searchItem = added.stream()
                            .filter((t) -> {
                                String search[] = t.toString().split("=");
                                return items.add(search[0].trim());
                            })
                            .map((t) -> {
                                String search[] = t.toString().split("=");
                                return search[0].trim();
                            })
                            .collect(Collectors.joining(","));
                }
                if (filter == null ? searchItem == null : filter.equals(searchItem)) {
                    //filter and search is equal? do nothing
                    return;
                }
                editFilter(pAtt.getKey(), searchItem == null ? "" : searchItem, pOption);
                if (searchItem == null) {
                    mapAdditionalFilterValue.remove(pAtt.getKey());
                }
            });
            mapKeyToControl.put(pAtt.getKey(), pCheckCombobox);
            return new VBox(comboboxLabel, pCheckCombobox);
        }

        public Control getFieldForKey(String pKey) {
            return mapKeyToControl.get(pKey);
        }

    }

    /**
     * @return the filterListFXMLController
     */
    public F getFilterListFXMLController() {
        return filterListFXMLController;
    }
}
