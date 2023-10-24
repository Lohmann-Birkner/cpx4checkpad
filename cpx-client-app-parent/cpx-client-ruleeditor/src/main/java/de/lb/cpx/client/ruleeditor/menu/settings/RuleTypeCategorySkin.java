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
package de.lb.cpx.client.ruleeditor.menu.settings;

import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.model.fx.button.AddButton;
import de.lb.cpx.client.core.model.fx.button.DeleteButton;
import de.lb.cpx.client.core.model.fx.button.GlyphIconButton;
import de.lb.cpx.client.core.model.fx.button.SaveButton;
import de.lb.cpx.client.core.model.fx.masterdetail.TableViewMasterDetailPane;
import de.lb.cpx.client.core.model.fx.section.SectionHeader;
import de.lb.cpx.client.core.model.fx.tableview.AsyncTableView;
import de.lb.cpx.client.core.model.fx.tableview.column.StringColumn;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.client.core.util.TextLimiter;
import de.lb.cpx.client.ruleeditor.events.UpdateRuleTypeEvent;
import de.lb.cpx.ruleviewer.cache.RuleMetaDataCache;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTypes;
import de.lb.cpx.server.commonDB.model.rules.PoolTypeEn;
import de.lb.cpx.server.rule.services.RuleEditorProcessException;
import de.lb.cpx.shared.lang.Lang;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.skin.ScrollPaneSkin;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javax.ejb.AsyncResult;
import javax.validation.constraints.NotNull;
import org.controlsfx.glyphfont.FontAwesome;

/**
 *
 * @author wilde
 */
public class RuleTypeCategorySkin extends SkinBase<RuleTypeCategory> {

    private static final Logger LOG = Logger.getLogger(RuleTypeCategorySkin.class.getName());
    private RuleTypeTableView tableView;

    public RuleTypeCategorySkin(RuleTypeCategory pSkinnable) throws IOException {
        super(pSkinnable);
        getChildren().add(initLayout());
    }

    private Parent initLayout() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/RuleTypeCategory.fxml"));
        root.getChildrenUnmodifiable();

        ScrollPane spPane = (ScrollPane) root.lookup("#spPane");
        spPane.setSkin(new ScrollPaneSkin(spPane));

        VBox content = (VBox) root.lookup("#boxContent");

        tableView = new RuleTypeTableView(PoolTypeEn.DEV);
        TableViewMasterDetailPane<CrgRuleTypes> masterDetail = new TableViewMasterDetailPane<>(tableView);
        masterDetail.getSelectedItemProperty().addListener(new ChangeListener<CrgRuleTypes>() {
            @Override
            public void changed(ObservableValue<? extends CrgRuleTypes> ov, CrgRuleTypes t, CrgRuleTypes t1) {
                masterDetail.setDetail(createDetail(t1));
            }
        });
        masterDetail.setDividerDefaultPosition(1.0);
        VBox.setVgrow(masterDetail, Priority.ALWAYS);
//        Tab tab = new Tab("Arbeitspools", masterDetail);
//        tp.getTabs().add(tab);
        content.getChildren().add(masterDetail);
        masterDetail.setDetail(createDetail(null));
        return root;
    }

    private Parent createDetail(CrgRuleTypes t1) {
        if (t1 == null) {
            HBox box = new HBox(new Label("Sie haben nichts ausgewählt!"));
            box.setMinHeight(120.0);
            box.setAlignment(Pos.CENTER);
            return box;
        }
        return new RuleTypeDetail(t1);
    }

    private class RuleTypeDetail extends VBox {

        private final CrgRuleTypes ruleType;
        private final Double spacing = 5.0d;
        private SectionHeader header;

        public RuleTypeDetail(@NotNull CrgRuleTypes pType) {
            super();
            Objects.requireNonNull(pType, "RuleType must not be null");
            ruleType = pType;
            setSpacing(spacing);
            setFillWidth(true);
            GridPane content = createContent();
            VBox.setVgrow(content, Priority.ALWAYS);
            getChildren().add(createHeader());
            getChildren().add(content);
        }

        private SectionHeader createHeader() {
            header = new SectionHeader(ruleType.getCrgtShortText());
            SaveButton btnSave = new SaveButton();
            btnSave.setTooltip(new Tooltip("Regeltabelle speichern"));
            btnSave.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    long start = System.currentTimeMillis();
                    ruleType.setCrgtShortText(getShortText());
                    ruleType.setCrgtDisplayText(getDisplayText());
                    LOG.info("setValues in object " + ruleType.getCrgtShortText() + " in " + (System.currentTimeMillis() - start) + " ms");
                    start = System.currentTimeMillis();
                    getSkinnable().updateRuleType(ruleType);
                    LOG.info("update RuleType in database " + ruleType.getCrgtShortText() + " in " + (System.currentTimeMillis() - start) + " ms");
                    start = System.currentTimeMillis();
                    RuleMetaDataCache.instance().updateRuleType(ruleType, getSkinnable().getType());
                    UpdateRuleTypeEvent event = new UpdateRuleTypeEvent(UpdateRuleTypeEvent.updateRuleTypeEvent(), ruleType);
                    Event.fireEvent(getSkinnable(), event);
                    header.setTitle(getShortText());
                    tableView.refresh();
                    LOG.info("save rule type " + ruleType.getCrgtShortText() + " in " + (System.currentTimeMillis() - start) + " ms");
                }
            });
            header.addMenuItems(btnSave);
            return header;
        }

        private GridPane createContent() {
            GridPane primary = createNewGrid();
            Label lblCreator = new Label("Erstellt von:");
            Label lblCreatorValue = new Label(ruleType.getCreationUser() != null ? MenuCache.instance().getUserNameForId(ruleType.getCreationUser()) : "----");
            Label lblCreationDate = new Label("Erstellt am:");
            Label lblCreationDateValue = new Label(ruleType.getCreationDate() != null ? Lang.toDate(ruleType.getCreationDate()) : "----");
            Label lblShortText = new Label("Kurzname:");
            Label lblDisplayText = new Label("Bezeichnung:");

            GridPane secondary = createNewGrid();
            secondary.addRow(0, lblCreatorValue, lblCreationDate, lblCreationDateValue);

            secondary.getColumnConstraints().add(new ColumnConstraints(10, 100, GridPane.USE_COMPUTED_SIZE, Priority.ALWAYS, HPos.LEFT, true));

            primary.addRow(0, lblCreator, secondary);
            primary.addRow(1, lblShortText, createShortTextValueNode());
            primary.addRow(2, lblDisplayText, createDisplayTextValueNode());

            primary.getColumnConstraints().add(new ColumnConstraints());
            primary.getColumnConstraints().add(new ColumnConstraints(10,
                    100,
                    GridPane.USE_COMPUTED_SIZE,
                    Priority.SOMETIMES,
                    HPos.LEFT,
                    true));

            return primary;
        }

        private GridPane createNewGrid() {
            GridPane pane = new GridPane();
            pane.setHgap(spacing);
            pane.setVgap(spacing);
            return pane;
        }

        private Node createShortTextValueNode() {
            if (isReadOnly(ruleType)) {
                return new Label(getShortText());
            }
            TextField field = new TextField(getShortText());
            new TextLimiter().setMaxSize(50).setTextInputControl(field).buildAndApply();
            field.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                    setShortText(t1);
                }
            });
            return field;
        }

        private Node createDisplayTextValueNode() {
            if (isReadOnly(ruleType)) {
                return new Label(getDisplayText());
            }
            TextField field = new TextField(getDisplayText());
            new TextLimiter().setMaxSize(255).setTextInputControl(field).buildAndApply();
            field.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                    setDisplayText(t1);
                }
            });
            return field;
        }
        private StringProperty shortTextProperty;

        public StringProperty shortTextProperty() {
            if (shortTextProperty == null) {
                shortTextProperty = new SimpleStringProperty(ruleType != null ? ruleType.getCrgtShortText() : null);
            }
            return shortTextProperty;
        }

        public void setShortText(String pShortText) {
            shortTextProperty().set(pShortText);
        }

        public String getShortText() {
            return shortTextProperty().get();
        }

        private StringProperty displayTextProperty;

        public StringProperty displayTextProperty() {
            if (displayTextProperty == null) {
                displayTextProperty = new SimpleStringProperty(ruleType != null ? ruleType.getCrgtDisplayText() : null);
            }
            return displayTextProperty;
        }

        public void setDisplayText(String pDisplayText) {
            displayTextProperty().set(pDisplayText);
        }

        public String getDisplayText() {
            return displayTextProperty().get();
        }
    }

    private boolean isReadOnly(CrgRuleTypes pType) {
        if (!getSkinnable().isEditable()) {
            return true;
        }
        return pType.isCrgtReadonly();
    }

    private class RuleTypeTableView extends AsyncTableView<CrgRuleTypes> {

        public RuleTypeTableView(PoolTypeEn pType) {
            super();
            getStyleClass().add("stay-selected-table-view");
            setColumnResizePolicy(UNCONSTRAINED_RESIZE_POLICY);

            LockColumn lock = new LockColumn();
            NameColumn name = new NameColumn();
            DescriptionColumn desc = new DescriptionColumn();
            getColumns().addAll(lock, name, desc);

            NumberBinding numberBinding = Bindings.add(lock.widthProperty(), 0)
                    .add(12);
            name.prefWidthProperty().bind(widthProperty().subtract(numberBinding).multiply(0.3));
            desc.prefWidthProperty().bind(widthProperty().subtract(numberBinding).multiply(0.7));
            name.setResizable(false);
            desc.setResizable(false);

            ComboBox<PoolTypeEn> cbTypeSelection = new ComboBox<>(FXCollections.observableArrayList(PoolTypeEn.values()));
            cbTypeSelection.getStyleClass().add("combo-box-menu-pane");
            getSkinnable().typeProperty().bind(cbTypeSelection.valueProperty());
            cbTypeSelection.setConverter(new StringConverter<PoolTypeEn>() {
                @Override
                public String toString(PoolTypeEn t) {
                    return t.getTitle();
                }

                @Override
                public PoolTypeEn fromString(String string) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
            getMenuItems().add(cbTypeSelection);
            GlyphIconButton btnAdd = new AddButton().setOnActionEvent(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    try {
                        CrgRuleTypes created = getSkinnable().addRuleType();
                        RuleMetaDataCache.instance().addRuleType(created, getSkinnable().getType());
                        getItems().add(created);
                        getSelectionModel().select(created);
                        RuleMetaDataCache.instance().sort(getItems());
                        scrollTo(created);
                        UpdateRuleTypeEvent event = new UpdateRuleTypeEvent(UpdateRuleTypeEvent.updateRuleTypeEvent(), created);
                        Event.fireEvent(getSkinnable(), event);
                    } catch (RuleEditorProcessException ex) {
                        Logger.getLogger(RuleTypeCategorySkin.class.getName()).log(Level.SEVERE, null, ex);
                        MainApp.showErrorMessageDialog(ex.getMessage(), getSkinnable().getScene().getWindow());
                    }
                }
            });
            getMenuItems().add(btnAdd);
            btnAdd.disableProperty().bind(Bindings.when(cbTypeSelection.getSelectionModel().selectedItemProperty().isEqualTo(PoolTypeEn.PROD)).then(true).otherwise(false));

            GlyphIconButton btnDelete = new DeleteButton().setOnActionEvent(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    CrgRuleTypes selected = getSelectionModel().getSelectedItem();
                    int selectedIndex = getSelectionModel().getSelectedIndex();
                    if (selected == null) {
                        LOG.warning("SelectedItem is null!");
                        return;
                    }
                    long sizeOfRelations = getSkinnable().getSizeOfTypeRuleRelation(selected);
                    if (sizeOfRelations > 0) {
                        AlertDialog dialog = AlertDialog.createWarningDialog("Der Regeltyp: " + selected.getCrgtShortText() + " kann nicht gelöscht werden."
                                + "\nAnzahl der Regel(n), die den Regeltyp: '" + selected.getCrgtShortText() + "' referenzieren:  " + sizeOfRelations,
                                getSkinnable().getScene().getWindow(), ButtonType.OK);
                        dialog.showAndWait();
                        return;
                    }
                    AlertDialog dialog = AlertDialog.createConfirmationDialog("Wollen Sie den Regeltyp: " + selected.getCrgtShortText() + " wirklich löschen?", getSkinnable().getScene().getWindow());
                    dialog.showAndWait().ifPresent(new Consumer<ButtonType>() {
                        @Override
                        public void accept(ButtonType t) {
                            if (ButtonType.OK.equals(t)) {
                                try {
                                    getSkinnable().deleteRuleType(selected);
                                    RuleMetaDataCache.instance().removeRuleType(selected, getSkinnable().getType());
                                    getItems().remove(selected);
                                    UpdateRuleTypeEvent event = new UpdateRuleTypeEvent(UpdateRuleTypeEvent.updateRuleTypeEvent(), selected);
                                    Event.fireEvent(getSkinnable(), event);
                                    if (selectedIndex < getItems().size()) {
                                        getSelectionModel().select(selectedIndex);
                                    } else {
                                        getSelectionModel().selectLast();
                                    }
                                } catch (RuleEditorProcessException ex) {
                                    Logger.getLogger(RuleTypeCategorySkin.class.getName()).log(Level.SEVERE, null, ex);
                                    MainApp.showErrorMessageDialog(ex.getMessage(), getSkinnable().getScene().getWindow());
                                }
                            }
                        }
                    });
                }
            });
            if (getSkinnable().isEditable()) {
                btnDelete.disableProperty().bind(getSelectionModel().selectedItemProperty().isNull().or(Bindings.when(cbTypeSelection.getSelectionModel().selectedItemProperty().isEqualTo(PoolTypeEn.PROD)).then(true).otherwise(false)));
            } else {
                btnDelete.setDisable(true);
            }
            getMenuItems().add(btnDelete);
            setShowMenu(true);

            getSkinnable().typeProperty().addListener(new ChangeListener<PoolTypeEn>() {
                @Override
                public void changed(ObservableValue<? extends PoolTypeEn> ov, PoolTypeEn t, PoolTypeEn t1) {
                    getSelectionModel().select(null);
                    reload();
                }
            });
            cbTypeSelection.getSelectionModel().select(pType);
        }

        @Override
        public Future<List<CrgRuleTypes>> getFuture() {
            return new AsyncResult<>(getSkinnable().fetchRuleTypes(getSkinnable().getType()));
        }

        private class DescriptionColumn extends StringColumn<CrgRuleTypes> {

            public DescriptionColumn() {
                super("Bezeichnung");
            }

            @Override
            public String extractValue(CrgRuleTypes pValue) {
                return pValue.getCrgtDisplayText();
            }

        }

        private class NameColumn extends StringColumn<CrgRuleTypes> {

            public NameColumn() {
                super("Kurzname");
            }

            @Override
            public String extractValue(CrgRuleTypes pValue) {
                return pValue.getCrgtShortText();
            }

        }

        private class LockColumn extends TableColumn<CrgRuleTypes, Boolean> {

            public LockColumn() {
                super("Gesperrt");
                setMinWidth(55.0);
                setMaxWidth(55.0);
                setResizable(false);
                setCellValueFactory(new Callback<CellDataFeatures<CrgRuleTypes, Boolean>, ObservableValue<Boolean>>() {
                    @Override
                    public ObservableValue<Boolean> call(CellDataFeatures<CrgRuleTypes, Boolean> p) {
                        return new SimpleBooleanProperty(isReadOnly(p.getValue()));//p.getValue().isCrgtReadonly());
                    }
                });
                setCellFactory(new Callback<TableColumn<CrgRuleTypes, Boolean>, TableCell<CrgRuleTypes, Boolean>>() {
                    @Override
                    public TableCell<CrgRuleTypes, Boolean> call(TableColumn<CrgRuleTypes, Boolean> p) {
                        return new TableCell<CrgRuleTypes, Boolean>() {
                            @Override
                            protected void updateItem(Boolean t, boolean bln) {
                                super.updateItem(t, bln); //To change body of generated methods, choose Tools | Templates.
                                if (t == null || bln) {
                                    setGraphic(null);
                                    return;
                                }
                                Label lbl = new Label();
                                lbl.setGraphic(t ? ResourceLoader.getGlyph(FontAwesome.Glyph.LOCK) : null);
                                setGraphic(lbl);
                            }

                        };
                    }
                });
            }

        }
    }
}
