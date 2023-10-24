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
package de.lb.cpx.client.ruleeditor.menu.filterlists.tabs;

import de.lb.cpx.client.core.model.fx.button.SaveButton;
import de.lb.cpx.client.core.model.fx.button.SearchToggleButton;
import de.lb.cpx.client.core.model.fx.section.SectionHeader;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.client.ruleeditor.events.OpenRuleEvent;
import de.lb.cpx.client.ruleeditor.events.UpdateRoleEvent;
import de.lb.cpx.client.ruleeditor.menu.filterlists.model.CdbUserRolesItem;
import de.lb.cpx.client.ruleeditor.menu.filterlists.tabs.UserRolesDetail.SortMode;
import de.lb.cpx.client.ruleeditor.model.dnd.DndRulesToRole;
import de.lb.cpx.ruleviewer.event.RefreshEvent;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.shared.lang.Lang;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.skin.TreeCellSkin;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javax.validation.constraints.NotNull;
import org.controlsfx.glyphfont.FontAwesome;

/**
 *
 * @author wilde
 */
public class UserRolesDetailSkin extends SkinBase<UserRolesDetail> {

    private static final Logger LOG = Logger.getLogger(UserRolesDetailSkin.class.getName());

    private Label lblNameValue;
    private Label lblValidFromValue;
    private Label lblValidToValue;
    private TreeView<RuleDisplayItem> tvActualRules;
    private TreeView<RuleDisplayItem> tvAvailableRules;
    private TextField tfAvailable;
    private TextField tfActive;

    public UserRolesDetailSkin(UserRolesDetail pSkinnable) throws IOException {
        super(pSkinnable);
        getChildren().add(initLayout());
        pSkinnable.userRolesItemProperty().addListener(new ChangeListener<CdbUserRolesItem>() {
            @Override
            public void changed(ObservableValue<? extends CdbUserRolesItem> ov, CdbUserRolesItem t, CdbUserRolesItem t1) {
                setUpRoleValues(t1);
            }
        });
        setUpRoleValues(pSkinnable.getUserRolesItem());
    }

    private Parent initLayout() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/UserRolesDetail.fxml"));

        SectionHeader commonData = (SectionHeader) root.lookup("#shCommonData");
        SaveButton btnSave = new SaveButton();
        btnSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
//                    getSkinnable().getOnSaveRequested().handle(t);
                ArrayList<CrgRules> newItems = getSkinnable().getUserRolesItem().getStateManager().getCurrentStateItem();
                ArrayList<CrgRules> oldItems = getSkinnable().getUserRolesItem().getStateManager().getOriginalStateItem();
                List<Long> changeset = getSkinnable().getUserRolesItem().getRuleChangeSet(oldItems, newItems);
                Event.fireEvent(getSkinnable(), new UpdateRoleEvent(UpdateRoleEvent.updateRoleEvent(), getSkinnable().getPool(), getSkinnable().getUserRolesItem().getRole().getId(), changeset));
                getSkinnable().getUserRolesItem().getStateManager().save();
            }

//            private List<Long> getRuleChangeSet(ArrayList<CrgRules> oldItems, ArrayList<CrgRules> newItems) {
//                List<Long> diff = new ArrayList<>();
//                for(CrgRules rule : oldItems){
//                    if(!newItems.contains(rule)){
//                        if(!diff.contains(rule.getId())){
//                            diff.add(rule.getId());
//                        }
//                    }
//                }
//                for(CrgRules rule : newItems){
//                    if(!oldItems.contains(rule)){
//                        if(!diff.contains(rule.getId())){
//                            diff.add(rule.getId());
//                        }
//                    }
//                }
//                return diff;
//            }
        });
        if (getSkinnable().isEditable()) {
            commonData.addMenuItems(btnSave);
        }
        Label lblName = (Label) root.lookup("#lblName");
        lblNameValue = (Label) root.lookup("#lblNameValue");

        Label lblValidFrom = (Label) root.lookup("#lblValidFrom");
        lblValidFromValue = (Label) root.lookup("#lblValidFromValue");

        Label lblValidTo = (Label) root.lookup("#lblValidTo");
        lblValidToValue = (Label) root.lookup("#lblValidToValue");

        Button btnAllRight = (Button) root.lookup("#btnAllRight");
        btnAllRight.setScaleY(1.5);
        btnAllRight.setScaleX(1.5);
        btnAllRight.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.ANGLE_DOUBLE_RIGHT));
        btnAllRight.setDisable(!getSkinnable().isEditable());
        btnAllRight.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                moveAllToAvailable();
            }
        });
        Button btnSelectionRight = (Button) root.lookup("#btnSelectionRight");
        btnSelectionRight.setScaleY(1.5);
        btnSelectionRight.setScaleX(1.5);
        btnSelectionRight.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.ANGLE_RIGHT));
        btnSelectionRight.setDisable(!getSkinnable().isEditable());
        btnSelectionRight.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                moveSelectionToAvailable();
            }
        });
        Button btnAllLeft = (Button) root.lookup("#btnAllLeft");
        btnAllLeft.setScaleY(1.5);
        btnAllLeft.setScaleX(1.5);
        btnAllLeft.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.ANGLE_DOUBLE_LEFT));
        btnAllLeft.setDisable(!getSkinnable().isEditable());
        btnAllLeft.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                moveAllToActive();
            }
        });
        Button btnSelectionLeft = (Button) root.lookup("#btnSelectionLeft");
        btnSelectionLeft.setScaleY(1.5);
        btnSelectionLeft.setScaleX(1.5);
        btnSelectionLeft.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.ANGLE_LEFT));
        btnSelectionLeft.setDisable(!getSkinnable().isEditable());
        btnSelectionLeft.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                moveSelectionToActive();
            }
        });

        lblName.setText("Rolle:");
        lblValidFrom.setText("Gültig von:");
        lblValidTo.setText("Gültig bis:");

        CategorySort sortActiveRules = new CategorySort();
        sortActiveRules.getComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<SortMode>() {
            @Override
            public void changed(ObservableValue<? extends SortMode> ov, SortMode t, SortMode t1) {
                getSkinnable().setSortMode(t1);
                setUpRoleValues(getSkinnable().getUserRolesItem());
            }
        });
        CategorySort sortAvailableRules = new CategorySort();
        sortAvailableRules.getComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<SortMode>() {
            @Override
            public void changed(ObservableValue<? extends SortMode> ov, SortMode t, SortMode t1) {
                getSkinnable().setSortMode(t1);
                setUpRoleValues(getSkinnable().getUserRolesItem());
            }
        });

        getSkinnable().sortModeProperty().addListener(new ChangeListener<SortMode>() {
            @Override
            public void changed(ObservableValue<? extends SortMode> ov, SortMode t, SortMode t1) {
                sortActiveRules.getComboBox().getSelectionModel().select(t1);
                sortAvailableRules.getComboBox().getSelectionModel().select(t1);
            }
        });

        VBox vbActiveRules = (VBox) root.lookup("#vbActiveRules");
        VBox vbAvailableRules = (VBox) root.lookup("#vbAvailableRules");

        SectionHeader shAvailableRules = (SectionHeader) root.lookup("#shAvailableRules");
        SectionHeader shActiveRules = (SectionHeader) root.lookup("#shActiveRules");

        SearchToggleButton btnSearchActive = new SearchToggleButton();
        btnSearchActive.setTooltip(new Tooltip("Aktive Regeln durchsuchen"));
        tfActive = new TextField();
        tfActive.setPromptText("Regeln durchsuchen");
        tfActive.setFont(Font.font(tfActive.getFont().getFamily(), 15));
        HBox.setHgrow(tfActive, Priority.ALWAYS);
        tfActive.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                setUpActiveRules(t1);
            }
        });
        btnSearchActive.selectedProperty().addListener(new ChangeListener<Boolean>() {
            private final HBox box = new HBox(5, getIcon(FontAwesome.Glyph.SEARCH), tfActive);

            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if (t1) {
                    if (!vbActiveRules.getChildren().contains(box)) {
                        vbActiveRules.getChildren().add(1, box);
                    }
                } else {
                    if (vbActiveRules.getChildren().contains(box)) {
                        vbActiveRules.getChildren().remove(box);
                        tfActive.setText("");
                    }
                }
            }
        });
        shActiveRules.addMenuItems(btnSearchActive);

        SearchToggleButton btnSearchAvailable = new SearchToggleButton();
        btnSearchAvailable.setTooltip(new Tooltip("Verfügbare Regeln durchsuchen"));
        tfAvailable = new TextField();
        tfAvailable.setPromptText("Regeln durchsuchen");
        tfAvailable.setFont(Font.font(tfAvailable.getFont().getFamily(), 15));
        HBox.setHgrow(tfAvailable, Priority.ALWAYS);
        tfAvailable.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                setUpAvailableRules(t1);
            }
        });
        btnSearchAvailable.selectedProperty().addListener(new ChangeListener<Boolean>() {
            private final HBox box = new HBox(5, getIcon(FontAwesome.Glyph.SEARCH), tfAvailable);

            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if (t1) {
                    if (!vbAvailableRules.getChildren().contains(box)) {
                        vbAvailableRules.getChildren().add(1, box);
                    }
                } else {
                    if (vbAvailableRules.getChildren().contains(box)) {
                        vbAvailableRules.getChildren().remove(box);
                        tfAvailable.setText("");
                    }
                }

            }
        });
        shAvailableRules.addMenuItems(btnSearchAvailable);

        vbActiveRules.getChildren().add(0, sortActiveRules);
        vbAvailableRules.getChildren().add(0, sortAvailableRules);

        tvActualRules = (TreeView) root.lookup("#tvActualRules");
        tvActualRules.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tvActualRules.setRoot(new TreeItem<>());
        tvActualRules.setCellFactory(new RuleCellFactory());
        workaroundRemoveIndent(tvActualRules);
        registerDragDropHandler(tvActualRules);
        tvAvailableRules = (TreeView) root.lookup("#tvAvailableRules");
        tvAvailableRules.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tvAvailableRules.setRoot(new TreeItem<>());
        tvAvailableRules.setCellFactory(new RuleCellFactory());
        workaroundRemoveIndent(tvAvailableRules);
        registerDragDropHandler(tvAvailableRules);
        return root;
    }

    private void setUpRoleValues(@NotNull CdbUserRolesItem pItem) {
        pItem = Objects.requireNonNull(pItem, "RoleItem can not be null");

        lblNameValue.setText(pItem.getRole().getCdburName());
        lblValidFromValue.setText(pItem.getRole().getCdburValidFrom() != null ? Lang.toDate(pItem.getRole().getCdburValidFrom()) : "----");
        lblValidToValue.setText(pItem.getRole().getCdburValidTo() != null ? Lang.toDate(pItem.getRole().getCdburValidTo()) : "----");

        setUpAvailableRules("");
        setUpActiveRules("");
    }

    private void setUpAvailableRules(String pFilter) {
        pFilter = Objects.requireNonNullElse(pFilter, "");
        tvAvailableRules.getRoot().getChildren().clear();
        tvAvailableRules.getRoot().getChildren().addAll(createTreeItems(getSkinnable().getUserRolesItem().filterAvailableItems(pFilter), !pFilter.isEmpty()));
    }

    private void setUpActiveRules(String pFilter) {
        pFilter = Objects.requireNonNullElse(pFilter, "");
        tvActualRules.getRoot().getChildren().clear();
        tvActualRules.getRoot().getChildren().addAll(createTreeItems(getSkinnable().getUserRolesItem().filterActiveItems(pFilter), !pFilter.isEmpty()));
    }

    private List<TreeItem<RuleDisplayItem>> createTreeItems(List<CrgRules> pRules, boolean pExpand) {
        Map<String, TreeItem<RuleDisplayItem>> itemMap = createEntryMap(getSkinnable().getSortMode(), pRules);
        List<TreeItem<RuleDisplayItem>> values = itemMap.values().stream().collect(Collectors.toList());
        values.sort((TreeItem<RuleDisplayItem> o1, TreeItem<RuleDisplayItem> o2) -> o1.getValue().getDisplayText().compareTo(o2.getValue().getDisplayText()));
        //for better performance on large lists maybe move this in specific create Methodes!
        for (TreeItem<RuleDisplayItem> value : values) {
            if (!value.isLeaf()) {
                value.setExpanded(pExpand);
            }
        }
        return values;
    }

    private Map<String, TreeItem<RuleDisplayItem>> createEntryMap(SortMode pMode, List<CrgRules> pRules) {
        switch (pMode) {
            case FIRST_LETTER:
                return createFirstLetterMap(pRules);
            case RULE_TYPE:
                return createErrorTypeMap(pRules);
            case TYPE:
                return createRuleTypeMap(pRules);
            default:
                LOG.warning("Type can not be displayed!Type was " + pMode.name());
                return new HashMap<>();
        }
    }

    private Map<String, TreeItem<RuleDisplayItem>> createFirstLetterMap(List<CrgRules> pRules) {
        Map<String, TreeItem<RuleDisplayItem>> itemMap = new HashMap<>();
//        pRules.sort(Comparator.comparing(CrgRules::String.valueOf(getCrgrNumber().charAt(0))));
        for (CrgRules rule : pRules) {
            String firstLetter = String.valueOf(rule.getCrgrNumber().charAt(0));
            if (itemMap.containsKey(firstLetter)) {
                itemMap.get(firstLetter).getChildren().add(new SingleRuleDisplayItem(rule));
            } else {
                RuleDisplayItem newItem = new RuleDisplayItem(firstLetter);
                newItem.getChildren().add(new SingleRuleDisplayItem(rule));
                itemMap.put(firstLetter, newItem);
            }
        }
        return itemMap;
    }

    private Map<String, TreeItem<RuleDisplayItem>> createErrorTypeMap(List<CrgRules> pRules) {
        Map<String, TreeItem<RuleDisplayItem>> itemMap = new HashMap<>();
//        pRules.sort(Comparator.comparing(CrgRules::String.valueOf(getCrgrNumber().charAt(0))));
        for (CrgRules rule : pRules) {
            String firstLetter = rule.getCrgrRuleErrorType().name();//String.valueOf(rule.getCrgrNumber().charAt(0));
            if (itemMap.containsKey(firstLetter)) {
                itemMap.get(firstLetter).getChildren().add(new SingleRuleDisplayItem(rule));
            } else {
                RuleDisplayItem newItem = new RuleDisplayItem(rule.getCrgrRuleErrorType().getTranslation().getValue());
                newItem.getChildren().add(new SingleRuleDisplayItem(rule));
                itemMap.put(firstLetter, newItem);
            }
        }
        return itemMap;
    }

    private Map<String, TreeItem<RuleDisplayItem>> createRuleTypeMap(List<CrgRules> pRules) {
        Map<String, TreeItem<RuleDisplayItem>> itemMap = new HashMap<>();
//        pRules.sort(Comparator.comparing(CrgRules::String.valueOf(getCrgrNumber().charAt(0))));
        for (CrgRules rule : pRules) {
            String firstLetter = rule.getCrgRuleTypes() != null ? rule.getCrgRuleTypes().getCrgtShortText() : "----";//String.valueOf(rule.getCrgrNumber().charAt(0));
            if (itemMap.containsKey(firstLetter)) {
                itemMap.get(firstLetter).getChildren().add(new SingleRuleDisplayItem(rule));
            } else {
                RuleDisplayItem newItem = new RuleDisplayItem(firstLetter);
                newItem.getChildren().add(new SingleRuleDisplayItem(rule));
                itemMap.put(firstLetter, newItem);
            }
        }
        return itemMap;
    }

    private void moveAllToActive() {
        getSkinnable().getUserRolesItem().setAllActive();
        refresh();
    }

    private void moveAllToAvailable() {
        getSkinnable().getUserRolesItem().setAllAvailable();
        refresh();
    }

    private void moveToActive(List<CrgRules> pRules) {
        getSkinnable().getUserRolesItem().setItemsToActive(pRules);
        refresh();
        selectRules(tvActualRules, pRules);
    }

    private void moveSelectionToActive() {
        moveToActive(getSelectedRules(tvAvailableRules));
    }

    private void moveToAvailable(List<CrgRules> pRules) {
        getSkinnable().getUserRolesItem().setItemsToAvailable(pRules);
        refresh();
        selectRules(tvAvailableRules, pRules);
    }

    private void moveSelectionToAvailable() {
        moveToAvailable(getSelectedRules(tvActualRules));
    }

    private void refresh() {
        setUpActiveRules(tfActive.getText());
        setUpAvailableRules(tfAvailable.getText());
        RefreshEvent event2 = new RefreshEvent(RefreshEvent.refreshEvent(), getSkinnable());
        Event.fireEvent(getSkinnable(), event2);
    }

    private List<CrgRules> getSelectedRules(TreeView<RuleDisplayItem> pTreeView) {
        List<CrgRules> rules = new ArrayList<>();
        for (TreeItem<RuleDisplayItem> selected : pTreeView.getSelectionModel().getSelectedItems()) {
            if (!selected.isLeaf()) {
                for (TreeItem<RuleDisplayItem> child : selected.getChildren()) {
                    if (child.getValue() instanceof SingleRuleDisplayItem) {
                        if (!rules.contains(((SingleRuleDisplayItem) child.getValue()).getRule())) {
                            rules.add(((SingleRuleDisplayItem) child.getValue()).getRule());
                        }
                    }
                }
            } else {
                if (selected.getValue() instanceof SingleRuleDisplayItem) {
                    if (!rules.contains(((SingleRuleDisplayItem) selected.getValue()).getRule())) {
                        rules.add(((SingleRuleDisplayItem) selected.getValue()).getRule());
                    }
                }
            }
        }
        return rules;
    }

    private void registerDragDropHandler(TreeView<RuleDisplayItem> pTreeView) {
        pTreeView.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (pTreeView.getSelectionModel().isEmpty()) {
                    t.consume();
                    return;
                }
                if (!getSkinnable().isEditable()) {
                    t.consume();
                    return;
                }
                Dragboard db = pTreeView.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.put(DndRulesToRole.DATA_FORMAT, new DndRulesToRole(getSelectedRules(pTreeView)));
                db.setContent(content);
                t.consume();

            }
        });
        pTreeView.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent t) {
                DndRulesToRole content = (DndRulesToRole) t.getDragboard().getContent(DndRulesToRole.DATA_FORMAT);
                if (content == null) {
                    t.consume();
                    return;
                }
                if (pTreeView == tvActualRules) {
                    moveToActive(content.getRules());
                }
                if (pTreeView == tvAvailableRules) {
                    moveToAvailable(content.getRules());
                }
                t.setDropCompleted(true);
                t.consume();
                refresh();
                selectRules(pTreeView, content.getRules());
            }

        });
        pTreeView.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent t) {
                if (!(t.getGestureSource() != pTreeView)) {
                    return;
                }
                t.acceptTransferModes(TransferMode.ANY);
            }
        });
    }

    private void selectRules(TreeView<RuleDisplayItem> pTreeView, List<CrgRules> rules) {
        for (TreeItem<RuleDisplayItem> selected : pTreeView.getRoot().getChildren()) {
            if (!selected.isLeaf()) {
                for (TreeItem<RuleDisplayItem> child : selected.getChildren()) {
                    if (child.getValue() instanceof SingleRuleDisplayItem) {
                        if (rules.contains(((SingleRuleDisplayItem) child.getValue()).getRule())) {
                            selected.setExpanded(true);
                            pTreeView.getSelectionModel().select(child);
                        }
                    }
                }
            }
        }
    }

    private class CategorySort extends HBox {

        private final ComboBox<SortMode> combobox;

        public CategorySort() {
            super(5.0);
            combobox = new ComboBox<>(FXCollections.observableArrayList(SortMode.values()));
            combobox.getSelectionModel().select(SortMode.TYPE);
            combobox.setConverter(new StringConverter<SortMode>() {
                @Override
                public String toString(SortMode t) {
                    if (t == null) {
                        return null;
                    }
                    return t.getDescription();
                }

                @Override
                public SortMode fromString(String string) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
            combobox.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(combobox, Priority.ALWAYS);
            Label icon = getIcon(FontAwesome.Glyph.SORT_AMOUNT_ASC);
            getChildren().addAll(icon, combobox);
            setAlignment(Pos.CENTER_RIGHT);
        }

        public ComboBox<SortMode> getComboBox() {
            return combobox;
        }
    }

    private Label getIcon(FontAwesome.Glyph pGlyph) {
        Label icon = new Label();
        icon.setGraphic(ResourceLoader.getGlyph(pGlyph));
        icon.getStyleClass().add("icon");
        return icon;
    }

    private class RuleCellFactory implements Callback<TreeView<RuleDisplayItem>, TreeCell<RuleDisplayItem>> {

        @Override
        public TreeCell<RuleDisplayItem> call(TreeView<RuleDisplayItem> p) {
            TreeCell<RuleDisplayItem> cell = new TreeCell<RuleDisplayItem>() {

                @Override
                protected void updateItem(RuleDisplayItem pItem, boolean pEmpty) {
                    super.updateItem(pItem, pEmpty); //To change body of generated methods, choose Tools | Templates.
                    if (pItem == null || pEmpty) {
                        setText("");
                        setGraphic(null);
                        return;
                    }
                    Text label1 = new Text(pItem.getDisplayText());
                    label1.getStyleClass().add("selectable-text");
//                    label1.setStyle("-fx-wrap-text: true;");
                    TextFlow flow = new TextFlow(label1);
//                    label.setWrapText(true);
//                    TextFlow flow = new TextFlow(label);
                    VBox vbox = new VBox();
                    vbox.getChildren().add(flow);
//                            Tooltip tooltip = new Tooltip(icdItem.getDescription());
//                            Tooltip.install(flow, tooltip);
                    vbox.setPrefWidth(p.getWidth() - 40 - ((1) * 15));
                    vbox.setPrefHeight(flow.prefHeight(vbox.getPrefWidth()) + 4);
                    Platform.runLater(() -> {
                        if ((vbox.getHeight() / flow.getHeight()) > 1.6 || (vbox.getHeight() / flow.getHeight()) < -1.6) {
                            vbox.setPrefHeight(flow.getHeight());
                        }
                    });
                    widthProperty().addListener((obs, oldVal, newVal) -> {
                        vbox.setPrefWidth(p.getWidth() - 40 - ((1) * 15));
                        vbox.setPrefHeight(flow.prefHeight(vbox.getPrefWidth()) + 4);
                        Platform.runLater(() -> {
                            if ((vbox.getHeight() / flow.getHeight()) > 1.6 || (vbox.getHeight() / flow.getHeight()) < -1.6) {
                                vbox.setPrefHeight(flow.getHeight());
                            }
                        });
                    });
                    setGraphic(vbox);
//                    setText(pItem.getDisplayText());
                }
            };
            cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    if (t.getClickCount() >= 2 && MouseButton.PRIMARY.equals(t.getButton()) && cell.getItem() != null) {
                        RuleDisplayItem item = cell.getItem().getValue();
                        if (item instanceof SingleRuleDisplayItem) {
                            CrgRules rule = ((SingleRuleDisplayItem) item).getRule();
                            if (rule.getCrgrDefinition() == null) {
                                rule.setCrgrDefinition(getSkinnable().fetchRuleContent(rule));
                            }
                            OpenRuleEvent event = new OpenRuleEvent(OpenRuleEvent.openRuleEvent(),
                                    getSkinnable().getPool(),
                                    rule);
                            Event.fireEvent(cell.getTreeView(), event);
                        }
                    }
                }
            });
//            cell.setStyle("-fx-font-size:15px;");
            return cell;
        }
    }

    private class RuleDisplayItem extends TreeItem<RuleDisplayItem> {

        private final String displayText;

        public RuleDisplayItem(String pDisplayText) {
            displayText = pDisplayText;
            setValue(this);
        }

        public String getDisplayText() {
            return displayText;
        }
    }

    private class SingleRuleDisplayItem extends RuleDisplayItem {

        private final CrgRules rule;

        public SingleRuleDisplayItem(CrgRules pRule) {
            super(pRule.getCrgrNumber() + " - " + pRule.getCrgrCaption());
            rule = pRule;
//            setValue(pRule);
        }

        public CrgRules getRule() {
            return rule;
        }

    }

    /**
     * workaround to remove ident from the treeview maybe not needed in future
     * releases of javafx normaly ident is modified by setIdent in TreeCellSkin
     * See: https://bugs.openjdk.java.net/browse/JDK-8180646
     *
     * @param treeView treeview to remove
     */
    public static void workaroundRemoveIndent(TreeView<?> treeView) {
        try {
            Field maxDisclosureWidthMapField = TreeCellSkin.class.getDeclaredField("maxDisclosureWidthMap"); //$NON-NLS-1$ 
            maxDisclosureWidthMapField.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<TreeView<?>, Double> maxDisclosureWidthMap = (Map<TreeView<?>, Double>) maxDisclosureWidthMapField
                    .get(null);

            maxDisclosureWidthMap.put(treeView, 0.0);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
            // If TreeCellSkin has not the expected structure, we cannot know 
            // how anything works - so we silently give up. 
            LOG.log(Level.FINEST, null, e);
        }
    }
}
