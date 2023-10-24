/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.cm.fx.simulation.tables;

import de.lb.cpx.client.core.model.fx.contextmenu.CtrlContextMenu;
import de.lb.cpx.client.core.util.ExcelCsvFileManager;
import de.lb.cpx.model.TCaseFee;
import de.lb.cpx.model.enums.ExportTypeEn;
import de.lb.cpx.shared.lang.Lang;
import java.util.Date;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;

/**
 * Simple TableView to display Fees for a case
 *
 * @author wilde
 */
public class CaseFeeTableView extends TableView<TCaseFee> {
       protected final TableColumn<TCaseFee, String>  insuranceColumn = new InsuranceColumn();
       protected final TableColumn<TCaseFee, String>  feeTypeColumn = new FeeTypeColumn();
       protected final TableColumn<TCaseFee, String>  feeValueColumn = new FeeValueColumn();
       protected final TableColumn<TCaseFee, String>  feeCountColumn = new FeeCountColumn();
       protected final TableColumn<TCaseFee, String>  feeValueSumColumn = new FeeValueSumColumn();
       protected final TableColumn<TCaseFee, Date>  feeFromColumn = new FeeFromColumn();
       protected final TableColumn<TCaseFee, Date>  feeToColumn = new FeeToColumn();
       protected final TableColumn<TCaseFee, String>  feeDaysWithoutColumn = new FeeDaysWithoutColumn();
    private static final Logger LOG = Logger.getLogger(CaseFeeTableView.class.getName());

       

    public CaseFeeTableView() {
        super();
        setUpColumns();
        setRowFactory();
    }
    
    protected void setRowFactory(){
        setRowFactory(new Callback<TableView<TCaseFee>, TableRow<TCaseFee>>() {
            @Override
            public TableRow<TCaseFee> call(TableView<TCaseFee> param) {
                final TableRow<TCaseFee> row = new TableRow<>();
                final ContextMenu contextMenu = new CtrlContextMenu<>();

                MenuItem menuItemExportExcel = new MenuItem("Tabelle als XLS exportieren");
                menuItemExportExcel.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        final ExcelCsvFileManager mgr = new ExcelCsvFileManager(ExportTypeEn.EXCEL, "Entgelte", CaseFeeTableView.this);
                        mgr.openDialog(getScene().getWindow());

                    }
                });

                MenuItem menuItemExportCsv = new MenuItem("Tabelle als TXT (CSV) exportieren");
                menuItemExportCsv.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        final ExcelCsvFileManager mgr = new ExcelCsvFileManager(ExportTypeEn.CSV, "Entgelte", CaseFeeTableView.this);
                        mgr.openDialog(getScene().getWindow());
                    }
                });

                contextMenu.getItems().addAll(menuItemExportExcel, menuItemExportCsv);

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

    @SuppressWarnings("unchecked")
    protected void setUpColumns() {
//        insuranceColumn = new InsuranceColumn();
//        feeTypeColumn = new FeeTypeColumn();
//        feeValueColumn = new FeeValueColumn();
//        feeCountColumn = new FeeCountColumn();
//        feeValueSumColumn = new FeeValueSumColumn();
//        feeFromColumn =new FeeFromColumn();
//        feeToColumn = new FeeToColumn();
//        feeDaysWithoutColumn = new FeeDaysWithoutColumn();
        
        getColumns().addAll(insuranceColumn, feeTypeColumn, feeValueColumn,
                feeCountColumn, feeValueSumColumn, feeFromColumn, feeToColumn, feeDaysWithoutColumn);
        resizeColumns();
    }
    
    private void resizeColumns(){
        feeFromColumn.setMinWidth(100);
        feeFromColumn.setMaxWidth(150);
        feeToColumn.setMinWidth(100);
        feeToColumn.setMaxWidth(150);
        feeValueColumn.setMinWidth(100);
        feeValueColumn.setMaxWidth(150);
        feeCountColumn.setMinWidth(100);
        feeCountColumn.setMaxWidth(100);
//        insuranceColumn.setMinWidth(150);
        insuranceColumn.setMaxWidth(150);
        feeTypeColumn.setMinWidth(100);
        feeTypeColumn.setMaxWidth(150);
        feeValueSumColumn.setMinWidth(100);
        feeValueSumColumn.setMaxWidth(200);
//        feeDaysWithoutColumn.setMinWidth(150);
        feeDaysWithoutColumn.setMaxWidth(150);
    }

    private class InsuranceColumn extends TableColumn<TCaseFee, String> {

        private ObjectProperty<String> valueProperty = new SimpleObjectProperty<>("");

        InsuranceColumn() {
            super(Lang.getInsuranceIdent());
//            setLabel(Lang.getInsuranceIdent()); //set column label
            setCellValueFactory(new Callback<CellDataFeatures<TCaseFee, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(CellDataFeatures<TCaseFee, String> param) {
                    valueProperty.set(param.getValue().getFeecInsurance());
                    return valueProperty;
                }
            });
        }
    }

    private class FeeTypeColumn extends TableColumn<TCaseFee, String> {

        private ObjectProperty<String> valueProperty = new SimpleObjectProperty<>("");

        FeeTypeColumn() {
            super(Lang.getCasefeeKind());
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TCaseFee, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<TCaseFee, String> param) {
                    valueProperty.set(param.getValue().getFeecFeekey());
                    return valueProperty;
                }
            });
        }
    }

    private class FeeValueColumn extends TableColumn<TCaseFee, String> {

        private ObjectProperty<String> valueProperty = new SimpleObjectProperty<>("");

        FeeValueColumn() {
            super(Lang.getCasefeeValue());
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TCaseFee, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<TCaseFee, String> param) {
                    valueProperty.set(Lang.toDecimal(param.getValue().getFeecValue(), 2) + " " + Lang.getCurrencySymbol());
                    return valueProperty;
                }
            });
        }
    }

    private class FeeCountColumn extends TableColumn<TCaseFee, String> {

        private ObjectProperty<String> valueProperty = new SimpleObjectProperty<>("");

        FeeCountColumn() {
            super(Lang.getCasefeeCount());
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TCaseFee, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<TCaseFee, String> param) {
                    valueProperty.set(String.valueOf(param.getValue().getFeecCount()));
                    return valueProperty;
                }
            });
        }
    }

    private class FeeValueSumColumn extends TableColumn<TCaseFee, String> {

        private ObjectProperty<String> valueProperty = new SimpleObjectProperty<>("");

        FeeValueSumColumn() {
            super(Lang.getCasefeeSum());
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TCaseFee, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<TCaseFee, String> param) {
                    valueProperty.set(Lang.toDecimal(param.getValue().getFeecCount() * param.getValue().getFeecValue()) + " " + Lang.getCurrencySymbol());
                    return valueProperty;
                }
            });
        }
    }

    private class FeeFromColumn extends TableColumn<TCaseFee, Date> {

        private ObjectProperty<Date> valueProperty = new SimpleObjectProperty<>();

        FeeFromColumn() {
            super(Lang.getCasefeeFrom());
//            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TCaseFee, Date>, ObservableValue<Date>>() {
//                @Override
//                public ObservableValue<Date> call(TableColumn.CellDataFeatures<TCaseFee, Date> param) {
////                    valueProperty.set(Lang.toDate(param.getValue().getFeecFrom()));
//                    valueProperty.set(param.getValue().getFeecFrom());
//                    return valueProperty;
//                }
//            });
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TCaseFee, Date>, ObservableValue<Date>>() {
                @Override
                public ObservableValue<Date> call(TableColumn.CellDataFeatures<TCaseFee, Date> param) {
//                    valueProperty.set(Lang.toDate(param.getValue().getFeecFrom()));
                    valueProperty.set(param.getValue().getFeecFrom());
                    return valueProperty;
                }
            });
            setCellFactory(getDateCellCallBack());
        }
    }
    
//    private Callback<TableColumn.CellDataFeatures<TCaseFee, Date>, ObservableValue<Date>> getDateValueCallback(ObjectProperty<Date> valueProperty){
//        return new Callback<TableColumn.CellDataFeatures<TCaseFee, Date>, ObservableValue<Date>>() {
//                @Override
//                public ObservableValue<Date> call(TableColumn.CellDataFeatures<TCaseFee, Date> param) {
////                    valueProperty.set(Lang.toDate(param.getValue().getFeecFrom()));
//                    valueProperty.set(param.getValue().getFeecFrom());
//                    return valueProperty;
//                }
//            };
//    }
    
    private Callback<TableColumn<TCaseFee, Date>, TableCell<TCaseFee, Date>> getDateCellCallBack(){
        return new Callback<TableColumn<TCaseFee, Date>, TableCell<TCaseFee, Date>>() {
            @Override
            public TableCell<TCaseFee, Date> call(TableColumn<TCaseFee, Date> param) {

                TableCell<TCaseFee, Date> cell = new TableCell<TCaseFee, Date>() {
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
                    return cell;  
                };
            };
    }
    
    
    private class FeeToColumn extends TableColumn<TCaseFee, Date> {

        private ObjectProperty<Date> valueProperty = new SimpleObjectProperty<>();
         FeeToColumn() {
            super(Lang.getCasefeeTo());
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TCaseFee, Date>, ObservableValue<Date>>() {
                @Override
                public ObservableValue<Date> call(TableColumn.CellDataFeatures<TCaseFee, Date> param) {
//                    valueProperty.set(Lang.toDate(param.getValue().getFeecFrom()));
                    valueProperty.set(param.getValue().getFeecTo());
                    return valueProperty;
                }
            });
            setCellFactory(getDateCellCallBack());


//            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TCaseFee, String>, ObservableValue<String>>() {
//                @Override
//                public ObservableValue<String> call(TableColumn.CellDataFeatures<TCaseFee, String> param) {
//                    valueProperty.set(Lang.toDate(param.getValue().getFeecTo()));
//                    return valueProperty;
//                }
//            });
        }
    }

    private class FeeDaysWithoutColumn extends TableColumn<TCaseFee, String> {

        private ObjectProperty<String> valueProperty = new SimpleObjectProperty<>("");

        FeeDaysWithoutColumn() {
            super(Lang.getCasefeeDaysoff());
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TCaseFee, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<TCaseFee, String> param) {
                    valueProperty.set(String.valueOf(param.getValue().getFeecUnbilledDays()));
                    return valueProperty;
                }
            });
        }
    }
}
