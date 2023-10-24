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

import de.lb.cpx.client.core.model.fx.contextmenu.CtrlContextMenu;
import de.lb.cpx.client.core.model.fx.tableview.column.DoubleColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.StringColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.enums.OverrunStyleEn;
import de.lb.cpx.client.core.util.ExcelCsvFileManager;
import de.lb.cpx.model.TSapFiOpenItems;
import de.lb.cpx.model.enums.ExportTypeEn;
import de.lb.cpx.shared.lang.Lang;
import java.util.Objects;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;

/**
 * TableView for Open bill items
 *
 * @author nandola
 */
public class BillOpenItemsTableView extends TableView<TSapFiOpenItems> {

    public BillOpenItemsTableView() {
        super();
        setUpColumns();
//        setFixedCellSize(35);

        setRowFactory(new Callback<TableView<TSapFiOpenItems>, TableRow<TSapFiOpenItems>>() {
            @Override
            public TableRow<TSapFiOpenItems> call(TableView<TSapFiOpenItems> param) {
                final TableRow<TSapFiOpenItems> row = new TableRow<>();
                final ContextMenu contextMenu = new CtrlContextMenu<>();

                MenuItem menuItemExportExcel = new MenuItem("Tabelle als XLS exportieren");
                menuItemExportExcel.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        final ExcelCsvFileManager mgr = new ExcelCsvFileManager(ExportTypeEn.EXCEL, "Offenen Positionen", BillOpenItemsTableView.this);
                        mgr.openDialog(getScene().getWindow());
                    }
                });

                MenuItem menuItemExportCsv = new MenuItem("Tabelle als TXT (CSV) exportieren");
                menuItemExportCsv.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        final ExcelCsvFileManager mgr = new ExcelCsvFileManager(ExportTypeEn.CSV, "Offenen Positionen", BillOpenItemsTableView.this);
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
    private void setUpColumns() {
        getColumns().addAll(new CompanyCodeColumn(), new AccountsReceivableNoColumn(), new refNumberColumn(),
                new FiscalYearColumn(), new NumberReceiptColumn(), new ReceiptDateReceiptColumn(),
                new CurrencyKeyColumn(), new DebitCreditKeyColumn(), new AmountInLocalCurrColumn(),
                new TextDescColumn(), new NetValueColumn());
    }

    private class CompanyCodeColumn extends TableColumn<TSapFiOpenItems, String> {

        private ObjectProperty<String> valueProperty = new SimpleObjectProperty<>("");

        CompanyCodeColumn() {
            super(Lang.getBillOpenItemCompanyCode());
            setMinWidth(100.0d);
//            setMaxWidth(110.0d);
//            setResizable(false);
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TSapFiOpenItems, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<TSapFiOpenItems, String> param) {
                    valueProperty.set(param.getValue().getCompanyCode());
                    return valueProperty;
                }
            });
        }
    }

    private class AccountsReceivableNoColumn extends StringColumn<TSapFiOpenItems> {

        public AccountsReceivableNoColumn() {
            super(Lang.getBillOpenItemAccountsReceivableNo(), true);
            setMinWidth(120.0d);
//            setMaxWidth(130.0d);
//            setResizable(false);
        }

        @Override
        public String extractValue(TSapFiOpenItems pValue) {
            return pValue.getCustomerNumber();
        }
    }

    private class refNumberColumn extends StringColumn<TSapFiOpenItems> {

        public refNumberColumn() {
            super(Lang.getBillOpenItemRefNo(), true);
            setMinWidth(120.0d);
//            setMaxWidth(130.0d);
//            setResizable(false);
        }

        @Override
        public String extractValue(TSapFiOpenItems pValue) {
            return pValue.getRefNumber();
        }
    }

    private class FiscalYearColumn extends TableColumn<TSapFiOpenItems, String> {

        private ObjectProperty<String> valueProperty = new SimpleObjectProperty<>("");

        FiscalYearColumn() {
            super(Lang.getBillOpenItemFiscalYear());
            setMinWidth(50.0d);
//            setMaxWidth(60.0d);
//            setResizable(false);
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TSapFiOpenItems, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<TSapFiOpenItems, String> param) {
                    valueProperty.set(String.valueOf(param.getValue().getFiscalYear()));
                    return valueProperty;
                }
            });
        }
    }

    private class NumberReceiptColumn extends StringColumn<TSapFiOpenItems> {

        public NumberReceiptColumn() {
            super(Lang.getBillOpenItemNumberReceipt(), true);
            setMinWidth(120.0d);
//            setMaxWidth(130.0d);
//            setResizable(false);
        }

        @Override
        public String extractValue(TSapFiOpenItems pValue) {
            return pValue.getNumberReceipt();
        }
    }

    private class ReceiptDateReceiptColumn extends TableColumn<TSapFiOpenItems, String> {

        private ObjectProperty<String> valueProperty = new SimpleObjectProperty<>("");

        ReceiptDateReceiptColumn() {
            super(Lang.getBillOpenItemReceiptDateReceipt());
            setMinWidth(120.0d);
//            setMaxWidth(130.0d);
//            setResizable(false);
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TSapFiOpenItems, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<TSapFiOpenItems, String> param) {
                    valueProperty.set(Lang.toDate(param.getValue().getReceiptDateReceipt()));
                    return valueProperty;
                }
            });
        }
    }

    private class CurrencyKeyColumn extends TableColumn<TSapFiOpenItems, String> {

        private ObjectProperty<String> valueProperty = new SimpleObjectProperty<>("");

        CurrencyKeyColumn() {
            super(Lang.getBillOpenItemCurrencyKey());
            setMinWidth(70.0d);
//            setMaxWidth(80.0d);
//            setResizable(false);
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TSapFiOpenItems, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<TSapFiOpenItems, String> param) {
                    valueProperty.set(param.getValue().getCurrencyKey());
                    return valueProperty;
                }
            });
        }
    }

    private class DebitCreditKeyColumn extends TableColumn<TSapFiOpenItems, String> {

        private ObjectProperty<String> valueProperty = new SimpleObjectProperty<>("");

        DebitCreditKeyColumn() {
            super(Lang.getBillOpenItemDebitCreditKey());
            setMinWidth(40.0d);
//            setMaxWidth(50.0d);
//            setResizable(false);
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TSapFiOpenItems, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<TSapFiOpenItems, String> param) {
                    valueProperty.set(param.getValue().getDebitCreditKey());
                    return valueProperty;
                }
            });
        }
    }

//    private class AmountInLocalCurrColumn extends CurrencyColumn<TSapFiOpenItems> {
    private class AmountInLocalCurrColumn extends DoubleColumn<TSapFiOpenItems> {

        public AmountInLocalCurrColumn() {
            super(Lang.getBillOpenItemAmountInLocalCurr(), 2);
            setMinWidth(100.0d);
//            setMaxWidth(130.0d);
//            setResizable(false);
        }

        @Override
        public Double extractValue(TSapFiOpenItems pValue) {
            return Objects.requireNonNullElse(pValue.getValue(), 0D);
        }
    }

    private class TextDescColumn extends StringColumn<TSapFiOpenItems> {

        public TextDescColumn() {
            //don't show OverrunHelperButton (comment by Jörn)
//            super(Lang.getBillOpenItemTextDesc(), false);
            super(Lang.getBillOpenItemTextDesc(), OverrunStyleEn.Tooltip);
            setMinWidth(300d);
//            setMaxWidth(Double.MAX_VALUE);
        }

        @Override
        public String extractValue(TSapFiOpenItems pValue) {
            return pValue.getText();
        }
    }

//    private class NetValueColumn extends CurrencyColumn<TSapFiOpenItems> {
    private class NetValueColumn extends DoubleColumn<TSapFiOpenItems> {

        public NetValueColumn() {
            super(Lang.getBillOpenItemNetValue(), 2);
            setMinWidth(100.0d);
//            setMaxWidth(130.0d);
//            setResizable(false);
        }

        @Override
        public Double extractValue(TSapFiOpenItems pValue) {
            return Objects.requireNonNullElse(pValue.getNetValue(), 0D);
        }
    }

}
