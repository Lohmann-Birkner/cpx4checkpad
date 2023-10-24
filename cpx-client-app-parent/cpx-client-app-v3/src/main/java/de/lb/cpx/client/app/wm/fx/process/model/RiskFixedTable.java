/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.process.model;

import de.lb.cpx.client.app.wm.fx.process.completion.risk.SaveOrUpdateRiskEvent;
import de.lb.cpx.client.core.model.fx.button.DeleteButton;
import de.lb.cpx.client.core.model.fx.button.EditButton;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextArea;
import de.lb.cpx.client.core.model.fx.popover.AutoFitPopOver;
import de.lb.cpx.client.core.model.fx.textfield.CurrencyTextField;
import de.lb.cpx.client.core.model.fx.textfield.PercentTextField;
import de.lb.cpx.client.core.util.OverrunHelper;
import de.lb.cpx.server.commonDB.model.enums.RiskAreaEn;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmRisk;
import de.lb.cpx.wm.model.TWmRiskDetails;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.controlsfx.control.PopOver;

/**
 *
 * @author wilde
 */
public class RiskFixedTable extends FixedTable<RiskTableItem> {

    private static final Logger LOG = Logger.getLogger(RiskFixedTable.class.getName());
    private static final String DEFAULT_LABEL_STYLE_CLASS = "fixed-table-label";
    private static final String DEFAULT_TEXTFIELD_STYLE_CLASS = "fixed-table-text-field";
    private static final String DEFAULT_COMBOBOX_STYLE_CLASS = "risk-combo-box";
    
    public RiskFixedTable() {
        super();
        sumEntry().addListener(new ChangeListener<RiskTableItem>() {
            @Override
            public void changed(ObservableValue<? extends RiskTableItem> observable, RiskTableItem oldValue, RiskTableItem newValue) {
                getFixedItems().setAll(newValue);
            }
        });
        getColumns().addAll(getColumnList());
        getFixedColumns().addAll(getColumnList());
        syncColumns(getColumns(), getFixedColumns());
        
    }

    private List<TableColumn<RiskTableItem, ?>> getColumnList() {
        List<TableColumn<RiskTableItem, ?>> cols = new ArrayList<>();
        AreaColumn areaCol = new AreaColumn();
        cols.add(areaCol);
        RiskColumn riskCol = new RiskColumn();
        cols.add(riskCol);
        RiskValueColumn riskValCol = new RiskValueColumn();
        cols.add(riskValCol);
        CommentColumn comCol = new CommentColumn();
        cols.add(comCol);
        DeleteColumn delCol = new DeleteColumn();
        cols.add(delCol);

        comCol.prefWidthProperty().bind(widthProperty()
                .subtract(areaCol.prefWidthProperty())
                .subtract(riskCol.prefWidthProperty())
                .subtract(riskValCol.prefWidthProperty())
                .subtract(delCol.prefWidthProperty())
                .subtract(Bindings.when(vBarShowingProperty()).then(12).otherwise(0)));

        return cols;
    }

    private void syncColumns(List<TableColumn<RiskTableItem, ?>> mainColumns, List<TableColumn<RiskTableItem, ?>> fixedColumns) {
        mainColumns = Objects.requireNonNullElse(mainColumns, new ArrayList<>());
        fixedColumns = Objects.requireNonNullElse(fixedColumns, new ArrayList<>());
        if (mainColumns.size() != fixedColumns.size()) {
            return;
        }
        for (int i = 0; i < mainColumns.size(); i++) {
            TableColumn<RiskTableItem, ?> col1 = mainColumns.get(i);
            TableColumn<RiskTableItem, ?> col2 = fixedColumns.get(i);

            col2.prefWidthProperty().bind(col1.widthProperty());
            col2.visibleProperty().bind(col1.visibleProperty());
        }
    }
    private static final Comparator<RiskTableItem> DEFAULT_COMPARATOR = Comparator.comparing(RiskTableItem::getRiskArea, Comparator.nullsFirst(Comparator.naturalOrder()));
    private Comparator<RiskTableItem> comparator;

    public Comparator<RiskTableItem> getComparator() {
        return comparator == null ? DEFAULT_COMPARATOR : comparator;
    }

    private static final Callback<TWmRiskDetails, Boolean> DEFAULT_CHECK_DETAIL_DELETABLE = new Callback<TWmRiskDetails, Boolean>() {
        @Override
        public Boolean call(TWmRiskDetails param) {
            return true;
        }
    };
    private Callback<TWmRiskDetails, Boolean> checkDetailDeletable;

    public void setCheckDetailDeletable(Callback<TWmRiskDetails, Boolean> pCallback) {
        checkDetailDeletable = pCallback;
    }

    public Callback<TWmRiskDetails, Boolean> getCheckDetailDeletable() {
        return checkDetailDeletable == null ? DEFAULT_CHECK_DETAIL_DELETABLE : checkDetailDeletable;
    }

    public void setComparator(Comparator<RiskTableItem> pComparator) {
        comparator = pComparator;
    }

    public void addItems(List<RiskTableItem> pItems) {
        getItems().addAll(pItems);
        FXCollections.sort(getItems(), getComparator());
    }

    public void setItems(List<RiskTableItem> pItems) {
        getItems().setAll(pItems);
        FXCollections.sort(getItems(), getComparator());
    }
    private ObjectProperty<RiskTableItem> sumEntry;

    public final ObjectProperty<RiskTableItem> sumEntry() {
        if (sumEntry == null) {
            sumEntry = new SimpleObjectProperty<>();
        }
        return sumEntry;
    }

    public RiskTableItem getSumEnty() {
        return sumEntry().get();
    }

    public void setSumEntry(RiskTableItem pEntry) {
        sumEntry().set(pEntry);
    }

    public void addItem(RiskTableItem pItem) {
        getItems().add(pItem);
        FXCollections.sort(getItems(), getComparator());
    }
    
    private class AreaColumn extends TableColumn<RiskTableItem, RiskTableItem> {

        public AreaColumn() {
            super("Prüfbereich");
            setSortable(false);
            setPrefWidth(155);
            setCellValueFactory(new Callback<CellDataFeatures<RiskTableItem, RiskTableItem>, ObservableValue<RiskTableItem>>() {
                @Override
                public ObservableValue<RiskTableItem> call(CellDataFeatures<RiskTableItem, RiskTableItem> param) {
                    return new SimpleObjectProperty<>(param.getValue());
                }
            });
            setCellFactory(new Callback<TableColumn<RiskTableItem, RiskTableItem>, TableCell<RiskTableItem, RiskTableItem>>() {
                @Override
                public TableCell<RiskTableItem, RiskTableItem> call(TableColumn<RiskTableItem, RiskTableItem> param) {
                    return new TableCell<>() {
                        private ComboBox<RiskAreaEn> cbRiskArea;
                        private ChangeListener<RiskAreaEn> riskChangeListener = new ChangeListener<>() {
                            @Override
                            public void changed(ObservableValue<? extends RiskAreaEn> observable, RiskAreaEn oldValue, RiskAreaEn newValue) {
                                if (newValue == null) {
                                    cbRiskArea.getSelectionModel().select(oldValue);
                                    return;
                                }
                                if (oldValue == null && getItem().getRiskDetails().getRisk() == null) {
                                    TWmRisk risk = getSumEnty().getRisk();
                                    risk.getRiskDetails().add(getItem().getRiskDetails());
                                    getItem().getRiskDetails().setRisk(risk);
                                }
                                if (getItem().getRiskDetails() != null) {
                                    getItem().getRiskDetails().setRiskArea(newValue);
                                }
                                Event.fireEvent(RiskFixedTable.this, new SaveOrUpdateRiskEvent());
                            }
                        };

                        @Override
                        protected void updateItem(RiskTableItem item, boolean empty) {
                            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                            if (item == null || empty) {
                                setGraphic(null);
                                return;
                            }
                            if (RiskTableItem.Type.RISK.equals(item.getType())) {
                                Label lbl = new Label("Risiko (ges.)");
                                lbl.getStyleClass().add(DEFAULT_LABEL_STYLE_CLASS);
                                setGraphic(lbl);
                            } else {
                                if(DisplayMode.READ_ONLY.equals(getDisplayMode())){
                                    if(cbRiskArea != null){
                                        cbRiskArea.getSelectionModel().selectedItemProperty().removeListener(riskChangeListener);
                                        cbRiskArea = null;
                                    }
                                    Label lbl = new Label(item.getRiskDetails().getRiskArea()!=null?item.getRiskDetails().getRiskArea().getTranslation().getValue():"Nicht angegeben");
                                    lbl.getStyleClass().add(DEFAULT_LABEL_STYLE_CLASS);
                                    setGraphic(lbl);
                                    return;
                                }
                                if (cbRiskArea == null) {
                                    cbRiskArea = new ComboBox<>(FXCollections.observableArrayList(RiskAreaEn.values()));
                                    cbRiskArea.setCellFactory(new Callback<ListView<RiskAreaEn>, ListCell<RiskAreaEn>>() {
                                        @Override
                                        public ListCell<RiskAreaEn> call(ListView<RiskAreaEn> param) {
                                            return new ListCell<RiskAreaEn>() {
                                                @Override
                                                public void updateSelected(boolean selected) {
                                                    super.updateSelected(selected);
                                                    if (selected && isEmpty()) {
                                                        return;
                                                    }
                                                    updateItem(getItem(), isEmpty());
                                                }

                                                @Override
                                                protected void updateItem(RiskAreaEn item, boolean empty) {
                                                    super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                                                    if (item == null || empty) {
                                                        setGraphic(null);
                                                        setText("");
                                                        setDisable(false);
                                                        return;
                                                    }
                                                    setDisable(hasAreaAlready(item));
                                                    Label areaText = new Label(cbRiskArea.getConverter().toString(item));
                                                    setGraphic(areaText);
                                                }
                                            };
                                        }
                                    });
                                    cbRiskArea.getStyleClass().add(0,DEFAULT_COMBOBOX_STYLE_CLASS);
                                    cbRiskArea.setConverter(new StringConverter<RiskAreaEn>() {
                                        @Override
                                        public String toString(RiskAreaEn object) {
                                            if (object == null) {
                                                return null;
                                            }
                                            return object.getTranslation().getValue();
                                        }

                                        @Override
                                        public RiskAreaEn fromString(String string) {
                                            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                                        }
                                    });
                                }
                                cbRiskArea.getSelectionModel().selectedItemProperty().removeListener(riskChangeListener);
                                cbRiskArea.getSelectionModel().select(item.getRiskDetails().getRiskArea());
                                cbRiskArea.getSelectionModel().selectedItemProperty().addListener(riskChangeListener);
                                setText("");
                                setGraphic(cbRiskArea);
//                                setText(item.getRiskDetails() != null?item.getRiskDetails().getRiskArea().getTranslation().getValue():"----");
                            }
                        }
                    };
                }
            });
        }
        private boolean hasAreaAlready(RiskAreaEn pArea) {
            if (pArea == null) {
                return false;
            }
            for (RiskTableItem item : getItems()) {
                if (pArea.equals(item.getRiskArea())) {
                    return true;
                }
            }
            return false;
        }
    }

    private class DeleteColumn extends TableColumn<RiskTableItem, RiskTableItem> {

        public DeleteColumn() {
            super("");
            setPrefWidth(23);
            setSortable(false);
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RiskTableItem, RiskTableItem>, ObservableValue<RiskTableItem>>() {
                @Override
                public ObservableValue<RiskTableItem> call(TableColumn.CellDataFeatures<RiskTableItem, RiskTableItem> param) {
                    return new SimpleObjectProperty<>(param.getValue());
                }
            });
            setCellFactory(new Callback<TableColumn<RiskTableItem, RiskTableItem>, TableCell<RiskTableItem, RiskTableItem>>() {
                @Override
                public TableCell<RiskTableItem, RiskTableItem> call(TableColumn<RiskTableItem, RiskTableItem> param) {
                    return new TableCell<>() {
                        private DeleteButton btnDelete;

                        @Override
                        protected void updateItem(RiskTableItem item, boolean empty) {
                            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                            if (item == null || empty) {
                                setGraphic(null);
                                setText("");
                                return;
                            }
                            setStyle("-fx-padding:0 0 0 0;");
                            setAlignment(Pos.CENTER);
                            if (RiskTableItem.Type.RISK.equals(item.getType())) {
                                setGraphic(null);
                            } else {
                                if(DisplayMode.READ_ONLY.equals(getDisplayMode())){
                                    if(btnDelete != null){
                                        btnDelete.setOnAction(null);
                                        btnDelete = null;
                                    }
                                    setGraphic(null);
                                    return;
                                }
                                if (btnDelete == null) {
                                    btnDelete = new DeleteButton();
                                }
                                btnDelete.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {
                                        if (removeRiskDetail(getItem())) {
                                            Event.fireEvent(RiskFixedTable.this, new SaveOrUpdateRiskEvent());
                                        }
                                    }
                                });
                                setText("");
                                setGraphic(btnDelete);
                            }
                        }
                    };
                }
            });
        }

        private Boolean removeRiskDetail(RiskTableItem pItem) {
            TWmRiskDetails pRiskDetail = pItem.getRiskDetails();
            TWmRisk risk = getSumEnty().getRisk();
            if (pRiskDetail == null) {
                LOG.warning("can not delete riskdetail, it is null");
                return false;
            }
            if (risk == null) {
                LOG.warning("can not delete riskdetails, risk is null");
                return false;
            }
            if (!risk.getRiskDetails().contains(pRiskDetail)) {
                LOG.warning("can not delete riskdetail, risk did not contain such detail");
                return false;
            }
            if (!getCheckDetailDeletable().call(pRiskDetail)) {
                LOG.warning("can not delete riskdetail, is marked as not deletable!");
                return false;
            }
            risk.getRiskDetails().remove(pRiskDetail);
            boolean result = getItems().remove(pItem);
            FXCollections.sort(getItems(), super.getComparator());
            return result;
        }
    }

    private class RiskColumn extends TableColumn<RiskTableItem, RiskTableItem> {

        public RiskColumn() {
            super("Risiko (%)");
            setSortable(false);
            setPrefWidth(65.0);
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RiskTableItem, RiskTableItem>, ObservableValue<RiskTableItem>>() {
                @Override
                public ObservableValue<RiskTableItem> call(TableColumn.CellDataFeatures<RiskTableItem, RiskTableItem> param) {
                    return new SimpleObjectProperty<>(param.getValue());
                }
            });
            setCellFactory(new Callback<TableColumn<RiskTableItem, RiskTableItem>, TableCell<RiskTableItem, RiskTableItem>>() {
                @Override
                public TableCell<RiskTableItem, RiskTableItem> call(TableColumn<RiskTableItem, RiskTableItem> param) {
                    return new TableCell<>() {
                        private PercentTextField tfRisk;
                        private final ChangeListener<Number> riskChangeListener = new ChangeListener<Number>() {
                            @Override
                            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                                if (RiskTableItem.Type.RISK.equals(getItem().getType())) {
                                    getItem().getRisk().setRiskPercentTotal(newValue.intValue());
                                } else {
                                    getItem().getRiskDetails().setRiskPercent(newValue.intValue());
                                }
                                Event.fireEvent(RiskFixedTable.this, new SaveOrUpdateRiskEvent());
                            }
                        };

                        @Override
                        protected void updateItem(RiskTableItem item, boolean empty) {
                            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                            if (item == null || empty) {
                                setGraphic(null);
                                return;
                            }
                            if(DisplayMode.READ_ONLY.equals(getDisplayMode())){
                                if(tfRisk != null){
                                    tfRisk.valueProperty().removeListener(riskChangeListener);
                                    tfRisk = null;
                                }
                                int val;
                                if (RiskTableItem.Type.RISK.equals(item.getType())) {
                                    val = item.getRisk().getRiskPercentTotal();
                                } else {
                                    val = item.getRiskDetails().getRiskPercent();
                                }
                                Label lbl = new Label(new StringBuilder().append(val).append("%").toString());
                                lbl.getStyleClass().add(DEFAULT_LABEL_STYLE_CLASS);
                                setGraphic(lbl);
                                return;
                            }
                            if (tfRisk == null) {
                                tfRisk = new PercentTextField();
                                tfRisk.getStyleClass().add(DEFAULT_TEXTFIELD_STYLE_CLASS);
                            }
                            tfRisk.valueProperty().removeListener(riskChangeListener);
                            int val;
                            if (RiskTableItem.Type.RISK.equals(item.getType())) {
                                val = item.getRisk().getRiskPercentTotal();
                            } else {
                                val = item.getRiskDetails().getRiskPercent();
                                tfRisk.setDisable(item.getRiskArea()==null);
                            }
                            tfRisk.setValue(val);
                            tfRisk.valueProperty().addListener(riskChangeListener);
                            setGraphic(tfRisk);
                        }
                    };
                }
            });
        }
    }

    private class RiskValueColumn extends TableColumn<RiskTableItem, RiskTableItem> {

        public RiskValueColumn() {
            super("Verlustvol. (€)");
            setSortable(false);
            setPrefWidth(95.0);
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RiskTableItem, RiskTableItem>, ObservableValue<RiskTableItem>>() {
                @Override
                public ObservableValue<RiskTableItem> call(TableColumn.CellDataFeatures<RiskTableItem, RiskTableItem> param) {
                    return new SimpleObjectProperty<>(param.getValue());
                }
            });
            setCellFactory(new Callback<TableColumn<RiskTableItem, RiskTableItem>, TableCell<RiskTableItem, RiskTableItem>>() {
                @Override
                public TableCell<RiskTableItem, RiskTableItem> call(TableColumn<RiskTableItem, RiskTableItem> param) {
                    return new TableCell<>() {
                        private CurrencyTextField tfRiskValue;
                        private ChangeListener<Number> riskValueChangeListener = new ChangeListener<Number>() {
                            @Override
                            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                                if (RiskTableItem.Type.RISK.equals(getItem().getType())) {
                                    getItem().getRisk().setRiskValueTotal(BigDecimal.valueOf(newValue.doubleValue()));
                                } else {
                                    getItem().getRiskDetails().setRiskValue(BigDecimal.valueOf(newValue.doubleValue()));
                                }
                                Event.fireEvent(RiskFixedTable.this, new SaveOrUpdateRiskEvent());
                            }
                        };

                        @Override
                        protected void updateItem(RiskTableItem item, boolean empty) {
                            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                            if (item == null || empty) {
                                setGraphic(null);
                                return;
                            }
                            if(DisplayMode.READ_ONLY.equals(getDisplayMode())){
                                if(tfRiskValue != null){
                                    tfRiskValue.valueProperty().removeListener(riskValueChangeListener);
                                    tfRiskValue = null;
                                }
                                double val;
                                if (RiskTableItem.Type.RISK.equals(item.getType())) {
                                    val = item.getRisk().getRiskValueTotal().doubleValue();
                                } else {
                                    val = item.getRiskDetails().getRiskValue().doubleValue();
                                }
                                Label lbl = new Label(new StringBuilder(Lang.toDecimal(val,2)).append(Lang.getCurrencySymbol()).toString());
                                lbl.getStyleClass().add(DEFAULT_LABEL_STYLE_CLASS);
                                setGraphic(lbl);
                                return;
                            }
                            if (tfRiskValue == null) {
                                tfRiskValue = new CurrencyTextField();
                                tfRiskValue.getStyleClass().add(DEFAULT_TEXTFIELD_STYLE_CLASS);
                            }
                            tfRiskValue.valueProperty().removeListener(riskValueChangeListener);
                            double val;
                            if (RiskTableItem.Type.RISK.equals(item.getType())) {
                                val = item.getRisk().getRiskValueTotal().doubleValue();
                            } else {
                                val = item.getRiskDetails().getRiskValue().doubleValue();
                                tfRiskValue.setDisable(item.getRiskArea()==null);
                            }
                            tfRiskValue.setValue(val);
                            tfRiskValue.valueProperty().addListener(riskValueChangeListener);
                            setGraphic(tfRiskValue);
                        }
                    };
                }
            });
        }
    }

    private class CommentColumn extends TableColumn<RiskTableItem, RiskTableItem> {

        public CommentColumn() {
            super("Kommentar");
            setSortable(false);
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RiskTableItem, RiskTableItem>, ObservableValue<RiskTableItem>>() {
                @Override
                public ObservableValue<RiskTableItem> call(TableColumn.CellDataFeatures<RiskTableItem, RiskTableItem> param) {
                    return new SimpleObjectProperty<>(param.getValue());
                }
            });
            setCellFactory(new Callback<TableColumn<RiskTableItem, RiskTableItem>, TableCell<RiskTableItem, RiskTableItem>>() {
                @Override
                public TableCell<RiskTableItem, RiskTableItem> call(TableColumn<RiskTableItem, RiskTableItem> param) {
                    return new TableCell<>() {
                        private EditButton btnEdit;
                        private AutoFitPopOver popover;
                        private LabeledTextArea taComment;
                        private final ChangeListener<Boolean> focusChangeListener = new ChangeListener<Boolean>() {
                            @Override
                            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                                if (!newValue) {
                                    getItem().setRiskComment(taComment.getText());
//                                    if (RiskTableItem.Type.RISK.equals(getItem().getType())) {
//                                        getItem().getRisk().setRiskComment(taComment.getText());
//                                    } else {
//                                        getItem().getRiskDetails().setRiskComment(taComment.getText());
//                                    }
                                    Event.fireEvent(RiskFixedTable.this, new SaveOrUpdateRiskEvent());
                                }
                            }
                        };

                        @Override
                        protected void updateItem(RiskTableItem item, boolean empty) {
                            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                            if (item == null || empty) {
                                setGraphic(null);
                                setText("");
                                return;
                            }
                            String comment = item.getRiskComment();
                            Label lblComment = new Label(comment);
                            lblComment.getStyleClass().add(DEFAULT_LABEL_STYLE_CLASS);
                            lblComment.setMaxWidth(Double.MAX_VALUE);
                            OverrunHelper.addInfoTooltip(lblComment);
                            HBox box = new HBox(lblComment);
                            HBox.setHgrow(lblComment, Priority.ALWAYS);
                            box.setAlignment(Pos.CENTER_LEFT);
                            setGraphic(box);
                            if(DisplayMode.READ_ONLY.equals(getDisplayMode())){
                                return;
                            }
                            initComponents();
                            if(RiskTableItem.Type.RISK_DETAIL.equals(item.getType())){
                                btnEdit.setDisable(item.getRiskArea()==null);
                            }
                            taComment.getControl().focusedProperty().removeListener(focusChangeListener);
                            taComment.setText(comment);
                            taComment.getControl().focusedProperty().addListener(focusChangeListener);
                            btnEdit.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    if (!popover.isShowing()) {
                                        popover.show(btnEdit);
                                    } else {
                                        popover.hide();
                                    }
                                }
                            });
                            box.getChildren().add(btnEdit);
                        }

                        private void initComponents() {
                            if (btnEdit == null) {
                                btnEdit = new EditButton();
                                taComment = new LabeledTextArea("", 500);
                                taComment.setTitle("Kommentar");
                                taComment.setPadding(new Insets(8));
                                taComment.setWrapText(true);
                                popover = new AutoFitPopOver(taComment);
                                popover.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
                            }
                        }
                    };
                }
            });
        }
    }
}
