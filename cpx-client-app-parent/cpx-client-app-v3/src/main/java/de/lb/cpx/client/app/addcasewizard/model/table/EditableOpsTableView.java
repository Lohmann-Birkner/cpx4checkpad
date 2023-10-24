/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.addcasewizard.model.table;

import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.model.fx.contextmenu.CtrlContextMenu;
import de.lb.cpx.client.core.model.fx.labeled.LabeledDatePicker;
import de.lb.cpx.client.core.model.fx.popover.AutoFitPopOver;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.enums.LocalisationEn;
import de.lb.cpx.shared.lang.Lang;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

/**
 * Implements editable OpsTable, it allows to manipulate Localisation and Date
 * of the Ops Object
 *
 * @author wilde
 */
public class EditableOpsTableView extends OpsTableView {

    private ValidationSupport validationSupport;
    private Validator<Object> val;
    private Collection<TCaseOps> opsList;

    /**
     * Creates new Editable Ops TableView, layout is equals the standard
     * OpsTableView, except the "used for Grouping" Column is hidden.
     * Supportsvalidation, for its Date Column
     */
    public EditableOpsTableView() {
        super();
//        setArmedPropertyValue(true);
        validationSupport = new ValidationSupport();
        validationSupport.initInitialDecoration();
        setUpColumns();
        setUpRows();
    }

    /**
     * Creates new OpsTableView, which can be editable or not, layout is equals
     * the standard OpsTableView, except the "used for Grouping" Column is
     * hidden. Supportsvalidation, for its Date Column
     *
     * @param editable if tableview Entries should be editable or not
     */
    public EditableOpsTableView(boolean editable) {
        this();
//        setArmedPropertyValue(editable);
    }

    /**
     * returns ValidationSupport Object, to check if the content in the
     * TableView is valid
     *
     * @return validation support
     */
    public ValidationSupport getValidationSupport() {
        return validationSupport;
    }

    /**
     * @param item remove item from lists
     */
    public void remove(TCaseOps item) {
        getItems().remove(item);
        opsList.remove(item);
        refresh();
    }
//

    public void add(TCaseOps item) {
        getItems().add(item);
        setItemSet(getItems());
        opsList.add(item);
        refresh();
    }

    public void removeBlankRows(Collection<TCaseOps> objCollection) {
        Iterator<TCaseOps> it = new ArrayList<>(objCollection).iterator();
        while (it.hasNext()) {
            TCaseOps next = it.next();
            if (next.getOpscCode() == null) {
                remove(next);
            }
        }
        refresh();
    }

    //sets up Columns
    private void setUpColumns() {
        val = new Validator<Object>() {
            @Override
            public ValidationResult apply(Control t, Object u) {
                ValidationResult res = new ValidationResult();
                res.addErrorIf(t, Lang.getValidationErrorOpsDate(), !allOpsContainsDate());
                res.addWarningIf(t, "not in range", !allOpsInDateRange());
                return res;
            }

            private boolean allOpsContainsDate() {
                for (TCaseOps ops : getItems()) {
                    if (ops.getOpscDatum() == null) {
                        return false;
                    }
                }
                return true;
            }

            private boolean allOpsInDateRange() {
                for (TCaseOps ops : getItems()) {
                    Date adm = ops.getCaseDepartment().getDepcAdmDate();
                    Date dis = ops.getCaseDepartment().getDepcDisDate();
                    if (adm == null || dis == null) {
                        continue;
                    }

                    if (ops.getOpscDatum() != null) {

                        if (outOfRange(ops.getOpscDatum(), adm, dis)) {
                            return false;
                        }
                    }
                }
                return true;
            }

            private boolean outOfRange(Date date, Date min, Date max) {

                if (date.equals(min) || date.equals(max)) {
                    return false;
                }

                return !(date.after(min) && date.before(max));
            }
        };
        useForGroupColumn.setVisible(false);
        opsDateColumn.setCellFactory(new Callback<TableColumn<TCaseOps, Date>, TableCell<TCaseOps, Date>>() {
            @Override
            public TableCell<TCaseOps, Date> call(TableColumn<TCaseOps, Date> param) {

                TableCell<TCaseOps, Date> cell = new TableCell<TCaseOps, Date>() {
                    @Override
                    protected void updateItem(Date item, boolean empty) {
                        super.updateItem(item, empty);
                        setText("");
                        if (empty || getItem() == null) {
                            return;
                        }
                        setText(Lang.toDate(item));
                    }
                };
                cell.itemProperty().addListener(new ChangeListener<Date>() {
                    @Override
                    public void changed(ObservableValue<? extends Date> observable, Date oldValue, Date newValue) {
                        if (cell.getTableRow() != null && newValue != null) {
                            cell.setText(Lang.toDate(newValue));
                            TCaseOps ops = getOpsFromRow(cell);
                            if (ops != null) {
                                ops.setOpscDatum(newValue);
                                refresh();
                            }
                        }
                    }
                });

                //creates Popover layout to manipulate Date-Object
                cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (cell.isEmpty()) {
                            return;
                        }
                        //CPX-642 RSH :08122017 Improved date input in the procedures    
                        TCaseOps ops = getOpsFromRow(cell);
                        if (ops != null && ops.getOpscCode() == null) {
                            AlertDialog.createErrorDialog(Lang.getOPSCodeValidation(), ButtonType.OK).showAndWait();
                            return;
                        }
                        HBox hBox = new HBox();
                        VBox vBox = new VBox();
                        Label label = new Label(Lang.getEdit());
                        vBox.getChildren().addAll(label, hBox);

                        LabeledDatePicker datePicker = new LabeledDatePicker();
                        hBox.getChildren().addAll(datePicker);
                        datePicker.getControl().valueProperty().addListener(new ChangeListener<LocalDate>() {
                            @Override
                            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                                //if (newValue instanceof LocalDate) {
                                validationSupport.registerValidator(EditableOpsTableView.this, val);

                                cell.setItem(java.sql.Date.valueOf(datePicker.getLocalDate()));
//                                        
                                //}
                            }
                        });

                        PopOver popover = new AutoFitPopOver();
                        popover.setHideOnEscape(true);
                        popover.setAutoHide(true);
                        popover.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
                        popover.setDetachable(false);
                        popover.setContentNode(vBox);
                        popover.show(cell);
                        popover.getContentNode().setOnKeyPressed(new EventHandler<KeyEvent>() {

                            @Override
                            public void handle(KeyEvent ke) {
                                if (ke.getCode().equals(KeyCode.ENTER)) {
                                    popover.hide(Duration.ZERO);

                                }
                            }
                        });
                        popover.ownerWindowProperty().get().setOnCloseRequest(new EventHandler<WindowEvent>() {
                            @Override
                            public void handle(WindowEvent event) {
                                popover.hide(Duration.ZERO);
                            }
                        });
                    }

                });
                return cell;
            }

            private TCaseOps getOpsFromRow(TableCell<TCaseOps, Date> pCell) {
                if (pCell.getTableRow().getItem() != null) {
                    return pCell.getTableRow().getItem();
                }
                return null;
            }
        });
        //override default CellFactory to edit DateValue
//        opsDateColumn.setCellFactory(new Callback<TableColumn<TCaseOps, Date>, TableCell<TCaseOps, Date>>() {
//            @Override
//            public TableCell<TCaseOps, Date> call(TableColumn<TCaseOps, Date> param) {
//
//                TableCell<TCaseOps, Date> cell = new TableCell<TCaseOps, Date>() {
//                    @Override
//                    protected void updateItem(Date item, boolean empty) {
//                        super.updateItem(item, empty);
//                        if (empty || getItem() == null) {
//                            return;
//                        }
////                        Date ops = ((TCaseOps) getItem()).getOpscDatum();
//                        setValues(Lang.toDate(item));
//                    }
//                };
//                cell.itemProperty().addListener(new ChangeListener() {
//                    @Override
//                    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
//                        if (cell.getTableRow() != null && newValue != null) {
//                            validationSupport.registerValidator(EditableOpsTableView.this, val);
//                            cell.setValues(Lang.toDate((Date) newValue));
//                            TCaseOps ops = getOpsFromRow(cell);
//                            if (ops != null) {
//                                ops.setOpscDatum((Date) newValue);
//                                EditableOpsTableView.this.refresh();
//                            }
//                        }
//                    }
//                });
//
//                //creates Popover layout to manipulate Date-Object
//                cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
//                    @Override
//                    public void handle(MouseEvent event) {
//                        if (cell.isEmpty()) {
//                            return;
//                        }
//                        HBox hBox = new HBox();
//                        VBox vBox = new VBox();
//                        Label label = new Label(Lang.getEdit());
//                        vBox.getChildren().addAll(label, hBox);
//
//                        DatePicker datePicker = new DatePicker();
//
////                        CpxTimeSpinner timeSpinner = new CpxTimeSpinner();
//                        if (cell.getItem() != null) {
//                            datePicker.setValue(Lang.toLocalDate(cell.getItem()));
////                            timeSpinner.getTimeProperty().setValue(Lang.toLocalTime(ops.getOpscDatum()));
//                        }
//                        datePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
//                            @Override
//                            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
//                                if (newValue != null) {
//                                    cell.setItem(java.sql.Date.valueOf(newValue));
//                                }
//                            }
//                        });
//                        datePicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
//                            @Override
//                            public DateCell call(DatePicker param) {
//                                return new DateCell() {
//                                    @Override
//                                    public void updateItem(LocalDate item, boolean empty) {
//                                        super.updateItem(item, empty);
//                                        if (item == null || empty) {
//                                            return;
//                                        }
//                                        TCaseOps ops = getOpsFromRow(cell);
//                                        if (ops == null) {
//                                            return;
//                                        }
//                                        TCaseDepartment department = ops.getCaseDepartment();
//                                        if ((department.getDepcAdmDate() != null && item.isBefore(Lang.toLocalDate(department.getDepcAdmDate())))
//                                                || (department.getDepcDisDate() != null && item.isAfter(Lang.toLocalDate(department.getDepcDisDate())))) {
//                                            Platform.runLater(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    setStyle("-fx-background-color: #ffc0cb; -fx-text-fill: darkgray;");
//                                                    setDisable(true);
//                                                }
//                                            });
//                                            addEventFilter(MouseEvent.MOUSE_CLICKED, e -> e.consume());
//                                        }
//                                    }
//                                };
//                            }
//                        });
//                        hBox.getChildren().addAll(datePicker);//,timeSpinner);
//                        PopOver popover = new AutoFitPopOver();
//                        popover.setHideOnEscape(true);
//                        popover.setAutoHide(true);
//                        popover.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
//                        popover.setDetachable(false);
//                        popover.setContentNode(vBox);
//                        popover.show(cell);
//                        popover.ownerWindowProperty().get().setOnCloseRequest(new EventHandler<WindowEvent>() {
//                            @Override
//                            public void handle(WindowEvent event) {
//                                popover.hide(Duration.ZERO);
//                            }
//                        });
//                    }
////                }
//                });
//
//                return cell;
//            }
//
//            private TCaseOps getOpsFromRow(TableCell pCell) {
//                if (pCell.getTableRow().getItem() != null) {
//                    return (TCaseOps) pCell.getTableRow().getItem();
//                }
//                return null;
//            }
//        });
        opsLocalisationColumn.setCellFactory(new Callback<TableColumn<TCaseOps, TCaseOps>, TableCell<TCaseOps, TCaseOps>>() {
            @Override
            public TableCell<TCaseOps, TCaseOps> call(TableColumn<TCaseOps, TCaseOps> param) {
                TableCell<TCaseOps, TCaseOps> cell = new TableCell<>() {
                    @Override
                    protected void updateItem(TCaseOps item, boolean empty) {
                        super.updateItem(item, empty);
                        setText("");
                        if (item == null) {
                            return;
                        }
                        TCaseOps ops = item;
                        setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                if (ops.getOpscCode() != null) {
                                    handleEditEvent(ops);
                                }

                            }

                        });
                        setText(ops.getOpscLocEn() == LocalisationEn.E ? "" : ops.getOpscLocEn().getViewId());
                        if (ops.getOpscLocEn() != LocalisationEn.E) {
                            setTooltip(new Tooltip(ops.getOpscLocEn().getTranslation().toString()));
                        }

                    }

                    private void handleEditEvent(TCaseOps ops) {
                        TextField opsLoc = new TextField(ops.getOpscLocEn() == LocalisationEn.E ? "" : ops.getOpscLocEn().getViewId());
                        opsLoc.setTooltip(new Tooltip("Gültige Werte : R (Rechts) ,L(Links),B(beidseitig)"));
                        opsLoc.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent e) {
                                refresh();
                            }
                        });
                        opsLoc.textProperty().addListener(new ChangeListener<String>() {
                            @Override
                            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                                if (opsLoc.getText() != null && opsLoc.getText().length() > 1) {
                                    opsLoc.setText(opsLoc.getText().substring(0, 1));
                                }
                            }
                        });
                        opsLoc.focusedProperty().addListener((arg0, oldValue, newValue) -> {
                            if (!newValue) {
                                setOpsLoc(opsLoc.getText(), ops);
                            }
                        });
                        opsLoc.setOnKeyPressed(new EventHandler<KeyEvent>() {
                            @Override
                            public void handle(KeyEvent ke) {
                                if (ke.getCode().equals(KeyCode.ENTER)) {
                                    setOpsLoc(opsLoc.getText(), ops);
                                    refresh();

                                }
                                if (ke.getCode().equals(KeyCode.ESCAPE)) {
                                    refresh();

                                }

                            }

                        });

                        opsLoc.setPrefWidth(80);
                        setGraphic(opsLoc);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                opsLoc.requestFocus();

                            }
                        });

                    }

                    private void setOpsLoc(String opsLoc, TCaseOps ops) {
                        if (opsLoc != null) {

                            String icdLocS = opsLoc.toUpperCase();
                            switch (icdLocS) {
                                case "R":
                                    ops.setOpscLocEn(LocalisationEn.R);
                                    break;
                                case "B":
                                    ops.setOpscLocEn(LocalisationEn.B);
                                    break;
                                case "L":
                                    ops.setOpscLocEn(LocalisationEn.L);
                                    break;
                                default:
                                    ops.setOpscLocEn(LocalisationEn.E);
                                    break;
                            }
                            refresh();
                        } else {
                            refresh();
                        }
                    }
                };
                return cell;
            }

        });

        super.resizeColumns();
    }

    private void setUpRows() {
        setRowFactory(new Callback<TableView<TCaseOps>, TableRow<TCaseOps>>() {
            @Override
            public TableRow<TCaseOps> call(TableView<TCaseOps> tableView) {
                final TableRow<TCaseOps> row = new TableRow<>();
                // row.getTableView().getColumns().get(1).getColumns().addListener(listener);

                setOnKeyPressed(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent ke) {

                        if (ke.getCode().equals(KeyCode.DELETE)) {
                            if (tableView.getSelectionModel().getSelectedItem() != null && tableView.getSelectionModel().getSelectedItem().getOpscCode() != null) {
                                remove(tableView.getSelectionModel().getSelectedItem());
                            }

                        }
                    }
                });
                final ContextMenu contextMenu = new CtrlContextMenu<>();
                final MenuItem removeMenuItem = new MenuItem(Lang.getDelete());
                removeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        TCaseOps item = row.getItem();
                        if (item.getOpscCode() != null) {
                            remove(item);
                        }
                    }
                });
                contextMenu.getItems().add(removeMenuItem);
                // Set context menu on row, but use a binding to make it only show for non-empty rows:  
                row.contextMenuProperty().bind(
                        Bindings.when(row.emptyProperty())
                                .then((ContextMenu) null)
                                .otherwise(contextMenu)
                );
                return row;
            }
        });
    }

    public void setItemSet(Collection<TCaseOps> opsList) {
        this.opsList = opsList == null ? null : new HashSet<>(opsList);
        setItems(FXCollections.observableArrayList(opsList));
        refresh();
    }

//    private void forceRedraw() {
//        ObservableList<TCaseOps> tmpItems = FXCollections.observableArrayList();
//        tmpItems.addAll(getItems());
//        getItems().clear();
////        setItems(tmpItems);
//    }
}
