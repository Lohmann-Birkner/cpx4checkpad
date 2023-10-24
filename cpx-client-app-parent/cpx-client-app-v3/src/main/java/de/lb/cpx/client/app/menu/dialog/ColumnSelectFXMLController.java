/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2017  Alexander Bohm - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.menu.dialog;

import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.model.fx.filter_manager.FilterManager;
import de.lb.cpx.client.core.model.fx.labeled.LabeledListView;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextField;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.cpx.license.crypter.License;
import de.lb.cpx.model.enums.SearchListTypeEn;
import de.lb.cpx.service.searchlist.SearchListResult;
import de.lb.cpx.shared.filter.ColumnOption;
import de.lb.cpx.shared.filter.FilterOption;
import de.lb.cpx.shared.filter.enums.QuotaListAttributes;
import de.lb.cpx.shared.filter.enums.SearchListAttribute;
import de.lb.cpx.shared.filter.enums.WorkflowListAttributes;
import de.lb.cpx.shared.filter.enums.WorkingListAttributes;
import de.lb.cpx.shared.lang.Lang;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.stream.Collectors;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.FontWeight;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

/**
 * FXML Controller class for the ColumnSelect Dialog
 *
 * @author Bohm
 */
public class ColumnSelectFXMLController extends Controller<CpxScene> {

    @FXML
    private Button btn_unselect_one;
    @FXML
    private Button btn_unselect_all;
    @FXML
    private Button btn_select_all;
    @FXML
    private Button btn_select_one;
    @FXML
    private Button btn_to_top;
    @FXML
    private Button btn_one_up;
    @FXML
    private Button btn_one_down;
    @FXML
    private Button btn_to_bottom;
    @FXML
    private LabeledListView<ColumnOption> listview_not_selected;
    @FXML
    private LabeledListView<ColumnOption> listview_selected;
    private SearchListResult list;
    private FilterManager filter;
    private boolean shouldSetAsLast = true;
    @FXML
    private ScrollPane explanation;
    @FXML
    private AnchorPane pane;

    @FXML
    private Label explanation_label;
    @FXML
    private LabeledTextField searchInput;

    private List<ColumnOption> unmodifiableNotSelected;
    @FXML
    private LabeledTextField searchSelected;
    @FXML
    private LabeledTextField lblListName;

    /**
     * Initializes the controller class.
     *
     * @param url url
     * @param rb resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {


        //explanation.setId("bordered-list-view");
        explanation.setId("column-select-explanation");
        searchSelected.setTitle(Lang.getWorkingListColumnsSelected());
        searchSelected.applyFontWeightToTitle(FontWeight.BOLD);
        searchInput.setTitle(Lang.getWorkingListColumnsAvailable());
        searchInput.applyFontWeightToTitle(FontWeight.BOLD);
        searchSelected.setPromptText("Text eingeben um zu filtern");
        searchInput.setPromptText("Text eingeben um zu filtern");
        explanation_label.setText("");
        
        lblListName.setTitle("Name der Suchliste");
        lblListName.applyFontWeightToTitle(FontWeight.BOLD);
        btn_select_one.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.ANGLE_RIGHT));
        btn_select_one.setStyle("-fx-background-color: #ffffff;");
        btn_select_one.getGraphic().setStyle("-fx-text-fill: #22533b;");
        btn_select_one.setScaleY(1.5);
        btn_select_one.setScaleX(1.5);
        // btn_select_one.getGraphic().setStyle("-fx-text-fill: #ffffff;");
        btn_select_all.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.ANGLE_DOUBLE_RIGHT));
        btn_unselect_one.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.ANGLE_LEFT));
        btn_unselect_one.setStyle("-fx-background-color: #ffffff;");
        btn_unselect_one.getGraphic().setStyle("-fx-text-fill: #22533b;");
        btn_unselect_one.setScaleY(1.5);
        btn_unselect_one.setScaleX(1.5);
        btn_unselect_all.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.ANGLE_DOUBLE_LEFT));
        btn_unselect_all.setVisible(true);
        btn_select_all.setVisible(true);
        btn_unselect_all.setStyle("-fx-background-color: #ffffff;");
        btn_unselect_all.getGraphic().setStyle("-fx-text-fill: #22533b;");
        btn_unselect_all.setScaleY(1.5);
        btn_unselect_all.setScaleX(1.5);
        btn_select_all.setStyle("-fx-background-color: #ffffff;");
        btn_select_all.getGraphic().setStyle("-fx-text-fill: #22533b;");
        btn_select_all.setScaleY(1.5);
        btn_select_all.setScaleX(1.5);
        btn_one_up.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.ANGLE_UP));
        btn_one_up.setStyle("-fx-background-color: #ffffff;");
        btn_one_up.getGraphic().setStyle("-fx-text-fill: #22533b;");
        btn_one_up.setScaleY(1.5);
        btn_one_up.setScaleX(1.5);
        btn_to_top.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.ANGLE_DOUBLE_UP));
        btn_to_top.setStyle("-fx-background-color: #ffffff;");
        btn_to_top.getGraphic().setStyle("-fx-text-fill: #22533b;");
        btn_to_top.setScaleY(1.5);
        btn_to_top.setScaleX(1.5);
        btn_one_down.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.ANGLE_DOWN));
        btn_one_down.setStyle("-fx-background-color: #ffffff;");
        btn_one_down.getGraphic().setStyle("-fx-text-fill: #22533b;");
        btn_one_down.setScaleY(1.5);
        btn_one_down.setScaleX(1.5);
        btn_to_bottom.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.ANGLE_DOUBLE_DOWN));
        btn_to_bottom.setStyle("-fx-background-color: #ffffff;");
        btn_to_bottom.getGraphic().setStyle("-fx-text-fill: #22533b;");
        btn_to_bottom.setScaleY(1.5);
        btn_to_bottom.setScaleX(1.5);
        listview_selected.getListView().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listview_not_selected.getListView().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listview_selected.getListView().setCellFactory((ListView<ColumnOption> param) -> new TooltipCell());
//        listview_selected.setId("stay-selected-list-view");
        listview_not_selected.getListView().setCellFactory((ListView<ColumnOption> param) -> new TooltipCell());
//        listview_not_selected.setId("stay-selected-list-view");
//        listview_not_selected.getListView().setId("");

        listview_not_selected.getListView().setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2) {
                btn_select_one.fire();
            }
            ObservableList<ColumnOption> selectedItems = listview_not_selected.getListView().getSelectionModel().getSelectedItems();
            if (!selectedItems.isEmpty()) {
                ColumnOption Colopt = selectedItems.get(selectedItems.size() - 1);
                if (Colopt != null) {
                    explanation.setContent(new Label(Colopt.getToolTipText()));
                }
            }
        });
        listview_selected.getListView().setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2) {
                btn_unselect_one.fire();
            }
            ObservableList<ColumnOption> selectedItems = listview_selected.getListView().getSelectionModel().getSelectedItems();
            if (!selectedItems.isEmpty()) {
                ColumnOption Colopt = selectedItems.get(selectedItems.size() - 1);
                if (Colopt != null) {
                    explanation.setContent(new Label(Colopt.getToolTipText()));
                }
            }
        });
        listview_not_selected.getListView().setOnKeyReleased((KeyEvent event) -> {
            if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.UP) {
                ObservableList<ColumnOption> selectedItems = listview_not_selected.getListView().getSelectionModel().getSelectedItems();
                if (!selectedItems.isEmpty()) {
                    ColumnOption o = selectedItems.get(selectedItems.size() - 1);
                    explanation.setContent(new Label(o.getToolTipText()));
                }
            }
        });
        listview_selected.getListView().setOnKeyReleased((KeyEvent event) -> {
            if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.UP) {
                ObservableList<ColumnOption> selectedItems = listview_selected.getListView().getSelectionModel().getSelectedItems();
                if (!selectedItems.isEmpty()) {
                    ColumnOption o = selectedItems.get(selectedItems.size() - 1);
                    explanation.setContent(new Label(o.getToolTipText()));
                }
            }
        });
        //listview_selected.setTitle(Lang.getWorkingListColumnsSelected());

        //listview_not_selected.setTitle(Lang.getWorkingListColumnsAvailable());
        btn_select_one.setOnAction((ActionEvent event) -> {
            List<ColumnOption> to_move = listview_not_selected.getListView().getSelectionModel().getSelectedItems();
            shouldSetAsLast = !to_move.isEmpty();
            List<ColumnOption> tmp = new ArrayList<>(unmodifiableNotSelected);
            ColumnOption o2 = null;

            for (ColumnOption o : to_move) {
                if (WorkflowListAttributes.getDeadlines().contains(o.attributeName)) {
                    String days;
                    days = o.attributeName.substring(0, o.attributeName.length() - 4);
                    o2 = filter.getColumnOption(days);
                    tmp.remove(o2);
                    ColumnOption c = list.getColumn(o2.attributeName);
                    if (c != null) {
                        c.setShouldShow(true);
                    } else {
                        list.addColumn(o2);
                    }
                    //Bug fix CPX-977
                    if (c != null) {
                        list.addFilter(filter.addToFilter(c.attributeName));
                    }
                }
                tmp.remove(o);
                ColumnOption c = list.getColumn(o.attributeName);
                o.setShouldShow(true);
                if (c != null) {
                    list.getColumn(o.attributeName).setShouldShow(true);
                } else {
                    list.addColumn(o);
                }
                list.addFilter(filter.addToFilter(list.getColumn(o.attributeName).attributeName));
                if (o2 != null) {
                    //   listview_not_selected.getListView().getSelectionModel().select(listview_not_selected.getListView().getItems().indexOf(o2));
                }
                explanation.setContent(new Label(""));
            }

            unmodifiableNotSelected = Collections.unmodifiableList(tmp);
            listview_selected.getListView().getItems().addListener(new ListChangeListener<ColumnOption>() {
                @Override
                public void onChanged(ListChangeListener.Change<? extends ColumnOption> c) {
                    c.next();
                    if (c.wasAdded() && shouldSetAsLast) {
                        shouldSetAsLast = false;
                        if (!c.getAddedSubList().isEmpty()) {
                            setItemsAsLast(c.getAddedSubList().get(0));
                        }
                    }
                }
            });
            listview_selected.getListView().getItems().addAll(to_move);
            listview_not_selected.getListView().getItems().removeAll(to_move);
            //CPX-1380 
            if (!to_move.isEmpty()) {
                setItemsAsLast(to_move.get(0));
            }
            listview_not_selected.getListView().getSelectionModel().clearAndSelect(-1);

        });
        btn_unselect_one.setOnAction((ActionEvent event) -> {
            ObservableList<ColumnOption> to_move = listview_selected.getListView().getSelectionModel().getSelectedItems();
            List<ColumnOption> tmp = new ArrayList<>(unmodifiableNotSelected);
            List<FilterOption> filters_to_remove = new ArrayList<>();
            List<ColumnOption> columns_to_remove = new ArrayList<>();
            for (ColumnOption o : to_move) {
                if (o.attributeName.equals(WorkflowListAttributes.csCaseNumber) || o.attributeName.equals(WorkflowListAttributes.csHospitalIdent) || o.attributeName.equals(WorkingListAttributes.csCaseNumber)
                        || o.attributeName.equals(WorkingListAttributes.csHospitalIdent)
                        || o.attributeName.equals(WorkflowListAttributes.workflowNumber)) {
                    continue;
                } else if (WorkflowListAttributes.getDeadlines2().contains(o.attributeName)) {
                    String days;
                    days = o.attributeName + "Days";
                    ColumnOption o2 = filter.getColumnOption(days);
                    filter.getColumnOption(days).setShouldShow(false);
                    o2.setShouldShow(false);
                    if (list.getColumn(o2.attributeName) != null) {
                        list.getColumn(o2.attributeName).setShouldShow(false);
                    }
                    filters_to_remove.addAll(list.getFilter(o2.attributeName));
                    columns_to_remove.add(o2);
                    tmp.add(o2);
                }
                filter.getColumnOption(o.attributeName).setShouldShow(false);
                o.setShouldShow(false);
                list.getColumn(o.attributeName).setShouldShow(false);
                filters_to_remove.addAll(list.getFilter(o.attributeName));
                columns_to_remove.add(o);
                tmp.add(o);
            }
            Iterator<FilterOption> it = list.getFilters().iterator();
            while (it.hasNext()) {
                FilterOption o = it.next();
                if (filters_to_remove.contains(o)) {
                    it.remove();
                }
            }
            unmodifiableNotSelected = Collections.unmodifiableList(tmp);
            listview_not_selected.getListView().getItems().addAll(columns_to_remove);
            listview_selected.getListView().getItems().removeAll(columns_to_remove);
            listview_selected.getListView().getSelectionModel().clearAndSelect(-1);
            explanation.setContent(new Label(""));
        });

        btn_select_all.setOnAction((ActionEvent event) -> {
            listview_selected.getListView().getItems().addAll(listview_not_selected.getListView().getItems());
            int number = 0;
            for (ColumnOption o : listview_selected.getListView().getItems()) {
                o.setShouldShow(true);
                o.setNumber(++number);
                FilterOption f = filter.addToFilter(o.attributeName);
                list.addFilter(f);
            }
            refreshList();
            addItem();
            explanation.setContent(new Label(""));

        });

        btn_unselect_all.setOnAction((ActionEvent event) -> {
            listview_not_selected.getListView().getItems().addAll(listview_selected.getListView().getItems());
            for (ColumnOption o : listview_not_selected.getListView().getItems()) {
                if (o.attributeName.equals(WorkflowListAttributes.csCaseNumber) || o.attributeName.equals(WorkflowListAttributes.csHospitalIdent) || o.attributeName.equals(WorkingListAttributes.csCaseNumber)
                        || o.attributeName.equals(WorkingListAttributes.csHospitalIdent)
                        || o.attributeName.equals(WorkflowListAttributes.workflowNumber)) {
                    o.setShouldShow(true);
                    continue;
                }
                o.setShouldShow(false);
            }
            Iterator<FilterOption> it = list.getFilters().iterator();
            while (it.hasNext()) {
                it.next();
                it.remove();
            }
            refreshList();
            addItem();
            explanation.setContent(new Label(""));
        });

        btn_one_up.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ColumnOption[] to_move_array = listview_selected.getListView().getSelectionModel().getSelectedItems().toArray(new ColumnOption[0]);
                if (to_move_array.length > 0) {
                    moveItemUp(listview_selected.getListView().getSelectionModel().getSelectedIndices());
                    refreshList();
                    listview_selected.getListView().getSelectionModel().clearAndSelect(-1);
                    focusSelected(to_move_array);
                }
            }
        });

        btn_to_top.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!listview_selected.getListView().getSelectionModel().getSelectedItems().isEmpty()) {
                    int index = listview_selected.getListView().getSelectionModel().getSelectedIndex();
                    while (index != 0) {
                        moveItemUp(listview_selected.getListView().getSelectionModel().getSelectedIndices());
                        listview_selected.getListView().getSelectionModel().clearAndSelect(-1);
                        index--;
                        listview_selected.getListView().getSelectionModel().select(index);
                    }
                }
                listview_selected.getListView().getSelectionModel().clearAndSelect(-1);
                refreshList();
            }
        });

        btn_one_down.setOnAction((ActionEvent event) -> {
            ColumnOption[] to_move_array = listview_selected.getListView().getSelectionModel().getSelectedItems().toArray(new ColumnOption[0]);
            if (!listview_selected.getListView().getSelectionModel().getSelectedItems().isEmpty()) {
                ObservableList<Integer> indices = listview_selected.getListView().getSelectionModel().getSelectedIndices();
                moveItemDown(indices);
                refreshList();
                listview_selected.getListView().getSelectionModel().clearAndSelect(-1);
                focusSelected(to_move_array);
            }
        });

        btn_to_bottom.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!listview_selected.getListView().getSelectionModel().getSelectedItems().isEmpty()) {
                    int index = listview_selected.getListView().getSelectionModel().getSelectedIndex();
                    while (index != listview_selected.getListView().getItems().size() - 1) {
                        moveItemDown(listview_selected.getListView().getSelectionModel().getSelectedIndices());
                        listview_selected.getListView().getSelectionModel().clearAndSelect(-1);
                        index++;
                        listview_selected.getListView().getSelectionModel().select(index);
                    }
                }
                listview_selected.getListView().getSelectionModel().clearAndSelect(-1);
                refreshList();
            }
        });

        searchInput.getTextProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!observable.getValue().isEmpty()) {
                    if (oldValue.length() < newValue.length()) {
                        List<ColumnOption> tmp = Collections.unmodifiableList(listview_not_selected.getListView().getItems());
                        ObservableList<ColumnOption> filtered = FXCollections.observableArrayList(
                                tmp.stream()
                                        .filter(o -> o.getDisplayName().toLowerCase().contains(observable.getValue().toLowerCase()))
                                        .collect(Collectors.toList()));
                        listview_not_selected.getListView().getItems().clear();
                        listview_not_selected.getListView().setItems(filtered);
                    } else {
                        ObservableList<ColumnOption> filtered = FXCollections.observableArrayList(
                                unmodifiableNotSelected.stream()
                                        .filter(o -> o.getDisplayName().toLowerCase().contains(observable.getValue().toLowerCase()))
                                        .collect(Collectors.toList()));
                        listview_not_selected.getListView().getItems().clear();
                        listview_not_selected.getListView().setItems(filtered);
                    }
                } else {
                    listview_not_selected.getListView().setItems(FXCollections.observableArrayList(unmodifiableNotSelected));
                }
            }

        });

        searchSelected.getTextProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!observable.getValue().isEmpty()) {
                    if (oldValue.length() < newValue.length()) {
                        List<ColumnOption> tmp = Collections.unmodifiableList(listview_selected.getListView().getItems());
                        ObservableList<ColumnOption> filtered = FXCollections.observableArrayList(
                                tmp.stream()
                                        .filter(o -> o.getDisplayName().toLowerCase().contains(observable.getValue().toLowerCase()))
                                        .collect(Collectors.toList()));
                        listview_selected.getListView().getItems().clear();
                        listview_selected.getListView().setItems(filtered);
                    } else {
                        ObservableList<ColumnOption> filtered = FXCollections.observableArrayList(
                                unmodifiableNotSelected.stream()
                                        .filter(o -> o.getDisplayName().toLowerCase().contains(observable.getValue().toLowerCase()))
                                        .collect(Collectors.toList()));
                        listview_selected.getListView().getItems().clear();
                        listview_selected.getListView().setItems(filtered);
                    }
                } else {
                    //CPX-1214
                    btn_unselect_all.fire();
                }
            }

        });

    }

    /**
     * Keeps the selected Elements Focused when another Control was klicked
     *
     * @param to_move_array
     */
    private void focusSelected(ColumnOption[] to_move_array) {
        if (to_move_array.length > 1) {
            int from = listview_selected.getListView().getItems().indexOf(to_move_array[0]);
            int to = listview_selected.getListView().getItems().indexOf(to_move_array[to_move_array.length - 1]) + 1;
            listview_selected.getListView().getSelectionModel().selectRange(from, to);
        } else {
            listview_selected.getListView().getSelectionModel().select(to_move_array[0]);
        }
        //listview_selected.scrollTo(listview_selected.getSelectionModel().getSelectedItem());
        listview_selected.requestFocus();
    }

    /**
     * Sets the SearchList wich the Dialog uses to show available Columns
     *
     * @param type WorkingList or WorkflowList
     * @param list The SearchList to use
     */
    public void setSearchList(SearchListTypeEn type, SearchListResult list) {
        //this.listType = type;
        this.list = list;
        lblListName.setText(list.getName());
        if (type == SearchListTypeEn.WORKING) {
            lblListName.setTitle("Name der Arbeitsliste");
            filter = new FilterManager(type, WorkingListAttributes.instance());
            filter.setUpFilterForSearchList(list);
        } else if (type == SearchListTypeEn.RULE) {
            LOG.log(Level.WARNING, "rule list is not configurable");
        } else if (type == SearchListTypeEn.QUOTA) {
            lblListName.setTitle("Name der Prüfquotenliste");
            filter = new FilterManager(type, QuotaListAttributes.instance());
            filter.setUpFilterForSearchList(list);
        } else if (type == SearchListTypeEn.WORKFLOW) {
            lblListName.setTitle("Name der Vorgangsliste");
            filter = new FilterManager(type, WorkflowListAttributes.instance());
            filter.setUpFilterForSearchList(list);
        } else {
            LOG.log(Level.WARNING, "search list of type {0} cannot be configured. This is maybe intended or someone forget to implement this!", type);
        }
        addItem();
    }

    /**
     *
     * @return the currently used SearchList with all changes made in the Dialog
     */
    public SearchListResult getSearchList() {
        return this.list;
    }
    
    public String getSearchListName(){
        return lblListName.getText();
    }
    /**
     * Sets the Listview Entries based on FilterManager
     */
    public void addItem() {
//        listview_selected.getListView().setItems(FXCollections.observableArrayList(
//                filter.getSortedStreamOfColumns().filter(new Predicate<ColumnOption>() {
//                    @Override
//                    public boolean test(ColumnOption o) {
//                        SearchListAttribute att = filter.getAttributes().getByKey(o.attributeName);
//                        return o.isShouldShow() && !o.attributeName.equals("caseFees") && !att.isNoColumn() && att.isVisible();
//                    }
//                }).collect(Collectors.toList())));
        listview_selected.getListView().setItems(FXCollections.observableArrayList(
                filter.getSortedStreamOfColumns().filter(new Predicate<ColumnOption>() {
                    @Override
                    public boolean test(ColumnOption o) {
                        SearchListAttribute att = filter.getAttributes().getByKey(o.attributeName);
                        return o.isShouldShow() && !o.attributeName.equals("caseFees") && !att.isNoColumn() && att.isVisible();
                    }
                }).collect(Collectors.toList())));

        listview_not_selected.getListView().setItems(FXCollections.observableArrayList(
                filter.getSortedStreamOfColumns().filter(new Predicate<ColumnOption>() {
                    @Override
                    public boolean test(ColumnOption o) {
                        SearchListAttribute att = filter.getAttributes().getByKey(o.attributeName);
                        return !o.isShouldShow() && !att.isNoColumn() && att.isVisible();
                    }
                }).collect(Collectors.toList())));

        sortListAlphabetically();

        listview_selected.getListView().getItems().sorted((ColumnOption o1, ColumnOption o2) -> o1.getNumber().compareTo(o2.getNumber()));
    }

    public final String getDrgPeppColumnHeaderName() {
        License license = Session.instance().getLicense();
        String str = "";
        if (license.isDrgModule() && !license.isPeppModule()) {
            str = "DRG";
        } else if (license.isPeppModule() && !license.isDrgModule()) {
            str = "PEPP";
        } else if (license.isDrgModule() && license.isPeppModule()) {
            str = "DRG/PEPP";
        }
        return str;
    }
 /*   
    public final String getDrgPeppColumnHeaderTooltip(){
        License license = Session.instance().getLicense();
        String str = "";
        if (license.isDrgModule() && !license.isPeppModule()) {
            str = "DRG " + Lang.getRulesTxtCritDrgTooltip1() ;
        } else if (license.isPeppModule() && !license.isDrgModule()) {
            str = "PEPP" + Lang.getRulesTxtCritPeppTooltip1();
        } else if (license.isDrgModule() && license.isPeppModule()) {
            str = "DRG " + Lang.getRulesTxtCritDrgTooltip1() + "\n" + "PEPP" + Lang.getRulesTxtCritPeppTooltip1();
        }
        return str;
        
    }
*/
    public final String getMdcSkColumnHeaderName() {
        License license = Session.instance().getLicense();
        String str = "";
        if (license.isDrgModule() && !license.isPeppModule()) {
            str = "MDC";
        } else if (license.isPeppModule() && !license.isDrgModule()) {
            str = "SK";
        } else if (license.isDrgModule() && license.isPeppModule()) {
            str = "MDC/SK";
        }
        return str;
    }

    /**
     * @return the filter
     */
    public FilterManager getFilter() {
        return filter;
    }

    private void refreshList() {
        list.getColumns().clear();
        list.getColumns().addAll(new HashSet<>(listview_selected.getListView().getItems()));
        list.getColumns().addAll(new HashSet<>(listview_not_selected.getListView().getItems()));
    }

    private void moveItemUp(ObservableList<Integer> indices) {

        for (int index : indices) {
            if (index == 0) {
                return;
            }
            ColumnOption tmp = listview_selected.getListView().getItems().get(index - 1);
            int tmp_number = listview_selected.getListView().getItems().get(index).getNumber();
            listview_selected.getListView().getItems().get(index).setNumber(tmp.getNumber());
            tmp.setNumber(tmp_number);
            listview_selected.getListView().getItems().set(index - 1, listview_selected.getListView().getItems().get(index));
            listview_selected.getListView().getItems().set(index, tmp);
        }
    }

    private void moveItemDown(ObservableList<Integer> indices) {
        for (int i = (indices.size() - 1); i >= 0; i--) {
            int index = indices.get(i);
            if (index == (listview_selected.getListView().getItems().size() - 1)) {
                return;
            }
            ColumnOption tmp = listview_selected.getListView().getItems().get(index + 1);
            int tmp_number = listview_selected.getListView().getItems().get(index).getNumber();
            listview_selected.getListView().getItems().get(index).setNumber(tmp.getNumber());
            tmp.setNumber(tmp_number);
            listview_selected.getListView().getItems().set(index + 1, listview_selected.getListView().getItems().get(index));
            listview_selected.getListView().getItems().set(index, tmp);
        }
    }

    private void setItemsAsLast(ColumnOption o) {
        if (o != null) {
            listview_selected.getListView().getItems().stream().filter((co) -> (co.getNumber() > o.getNumber())).forEach((co) -> {
                int tmp = co.getNumber();
//                String tooltip = co.getToolTipText();
                co.setNumber(o.getNumber());
//                co.setToolTipText(o.getToolTipText());
                o.setNumber(tmp);
//                o.setToolTipText(tooltip);
            });
        }
    }

    private void sortListAlphabetically() {
        ObservableList<ColumnOption> items = listview_not_selected.getListView().getItems();
        items.sort((ColumnOption o1, ColumnOption o2) -> o1.getDisplayName().compareToIgnoreCase(o2.getDisplayName()));
        unmodifiableNotSelected = Collections.unmodifiableList(items);
        listview_not_selected.getListView().setItems(FXCollections.observableArrayList(unmodifiableNotSelected));
//        modifiable_not_selected = unmodifiable_not_selected;
    }

    public void updateSearchListName() {
        if(!getSearchList().getName().equals(lblListName.getText())){
            getSearchList().getSearchList().setSlName(lblListName.getText());
        }
    }
    
    public boolean isFilterNameChanged(){
        return !getSearchList().getName().equals(lblListName.getText());
    }

    public void selectSearchListName() {
        lblListName.getControl().selectAll();
    }

    public  TextField getNameItem() {
        return lblListName.getControl();
    }

    /**
     * custom ListCell with a Tooltip
     */
    private class TooltipCell extends ListCell<ColumnOption> {

        //private final double heightPerItem = listview_selected.getHeight() / listview_selected.getListView().getItems().size() / 2;
        private final Glyph glyph;

        TooltipCell() {
            glyph = ResourceLoader.getGlyph(FontAwesome.Glyph.EXCHANGE);
            glyph.setRotate(-90);
            glyph.setStyle("-fx-text-fill: green;");
//CPX-1214 OnDrag disabled because it is not working properly
//            setOnMouseEntered((event) -> {
//                if (getItem() != null) {
//                    setGraphic(glyph);
//                }
//            });
//            setOnMouseExited((event) -> {
//                if (!event.isPrimaryButtonDown()) {
//                    setGraphic(null);
//                }
//            });
//            setOnDragDetected(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    ClipboardContent content = new ClipboardContent();
//                    if (getItem() != null) {
//                        content.put(DATAFORMAT, getItem());
//                        setGraphic(glyph);
//                        Dragboard db = startDragAndDrop(TransferMode.ANY);
//                        db.setContent(content);
//                    }
//                    event.consume();
//                }
//            });
//
//            setOnDragOver(new EventHandler<DragEvent>() {
//                @Override
//                public void handle(DragEvent event) {
//                    ColumnOption opt = (ColumnOption) event.getDragboard().getContent(DATAFORMAT);
//                    if (opt != null) {
//                        event.acceptTransferModes(TransferMode.ANY);
//                    }
//                    event.consume();
//                }
//            });
//
//            setOnDragDropped(new EventHandler<DragEvent>() {
//                @Override
//                public void handle(DragEvent event) {
//                    if (event.getGestureSource() == event.getGestureTarget()) {
//                        return;
//                    }
//                    TooltipCell target = (TooltipCell) event.getGestureTarget();
//                    TooltipCell source = (TooltipCell) event.getGestureSource();
//
//                    if (!(event.getY() >= heightPerItem)) {
//                        source.getItem().setNumber((Integer) (target.getItem().getNumber() - 1));
//                    } else {
//                        source.getItem().setNumber((Integer) (target.getItem().getNumber() + 1));
//                    }
//                    listview_selected.getListView().getItems().sort((o1, o2) -> {
//                        return o1.getNumber().compareTo(o2.getNumber());
//                    });
//                }
//            });

//            setOnDragDone((event) -> {
//                setGraphic(null);
//            });
        }

        @Override
        protected void updateItem(ColumnOption item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText("");
            } else {
//                setText(item.getDisplayName());

                // set tooltip as well
//                setTooltip(item.getToolTipText().isEmpty() ? null : new Tooltip(item.getToolTipText()));
                // PNa: CPX-1753 (Lizenzabhängige Spaltennamen in Arbeitsliste)
                // may be a dirty solution, do we have any better way?
                switch (item.attributeName) {
                    case WorkingListAttributes.csDrg:
                        setText(getDrgPeppColumnHeaderName());
                        setTooltip(new Tooltip(getDrgPeppColumnHeaderName()));
//                        setTooltip(new Tooltip(getDrgPeppColumnHeaderTooltip()));
                        break;
                    case WorkingListAttributes.grpresGroup:
                        setText(getMdcSkColumnHeaderName());
//                        setTooltip(new Tooltip(getMdcSkColumnHeaderName()));
                        setTooltip(item.getToolTipText().isEmpty() ? null : new Tooltip(item.getToolTipText()));
                        break;
                    default:
                        setText(item.getDisplayName());
                        setTooltip(item.getToolTipText().isEmpty() ? null : new Tooltip(item.getToolTipText()));
                        break;
                }
            }
        }

    }
}
