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
import de.lb.cpx.client.core.util.ExcelCsvFileManager;
import de.lb.cpx.model.TSapFiBill;
import de.lb.cpx.model.enums.ExportTypeEn;
import de.lb.cpx.shared.lang.Lang;
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
 * TableView for SAP Bills
 *
 * @author nandola
 */
public class BillsTableView extends TableView<TSapFiBill> {

    public BillsTableView() {
        super();
        setUpColumns();

        setRowFactory(new Callback<TableView<TSapFiBill>, TableRow<TSapFiBill>>() {
            @Override
            public TableRow<TSapFiBill> call(TableView<TSapFiBill> param) {
                final TableRow<TSapFiBill> row = new TableRow<>();
                final ContextMenu contextMenu = new CtrlContextMenu<>();

                MenuItem menuItemExportExcel = new MenuItem("Tabelle als XLS exportieren");
                menuItemExportExcel.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        final ExcelCsvFileManager mgr = new ExcelCsvFileManager(ExportTypeEn.EXCEL, "Rechnungen", BillsTableView.this);
                        mgr.openDialog(getScene().getWindow());
                    }
                });

                MenuItem menuItemExportCsv = new MenuItem("Tabelle als TXT (CSV) exportieren");
                menuItemExportCsv.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        final ExcelCsvFileManager mgr = new ExcelCsvFileManager(ExportTypeEn.CSV, "Rechnungen", BillsTableView.this);
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
        getColumns().addAll(new BillRefTypeColumn(), new BillTypeColumn(), new BillNoColumn(), new RefCurrencyColumn(),
                new BillDateColumn(), new BillFiscalYearColumn(), new BillKindColumn(),
                new BillStatusColumn(), new BillNetValueColumn(), new BillRecipientColumn());
    }

    private class BillRefTypeColumn extends TableColumn<TSapFiBill, String> {

        private ObjectProperty<String> valueProperty = new SimpleObjectProperty<>("");

        BillRefTypeColumn() {
            super(Lang.getBillRefType());
            setMinWidth(100.0d);
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TSapFiBill, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<TSapFiBill, String> param) {
                    valueProperty.set(param.getValue().getReferenceType().getTranslation().getValue());
                    return valueProperty;
                }
            });
        }
    }

    private class BillTypeColumn extends TableColumn<TSapFiBill, String> {

        private ObjectProperty<String> valueProperty = new SimpleObjectProperty<>("");

        BillTypeColumn() {
            super(Lang.getBillType());
            setMinWidth(80.0d);
//            setLabel(Lang.getInsuranceIdent()); //set column label
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TSapFiBill, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<TSapFiBill, String> param) {
                    valueProperty.set(param.getValue().getInvoiceType());
                    return valueProperty;
                }
            });
        }
    }

    private class BillNoColumn extends TableColumn<TSapFiBill, String> {

        private ObjectProperty<String> valueProperty = new SimpleObjectProperty<>("");

        BillNoColumn() {
            super(Lang.getBillNo());
            setMinWidth(140.0d);
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TSapFiBill, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<TSapFiBill, String> param) {
                    valueProperty.set(param.getValue().getInvoice());
                    return valueProperty;
                }
            });
        }

    }

    private class RefCurrencyColumn extends TableColumn<TSapFiBill, String> {

        private ObjectProperty<String> valueProperty = new SimpleObjectProperty<>("");

        RefCurrencyColumn() {
            super(Lang.getBillRefCurrency());
            setMinWidth(80.0d);
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TSapFiBill, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<TSapFiBill, String> param) {
                    valueProperty.set(param.getValue().getReferenceCurrency());
                    return valueProperty;
                }
            });
        }

    }

    private class BillDateColumn extends TableColumn<TSapFiBill, String> {

        private ObjectProperty<String> valueProperty = new SimpleObjectProperty<>("");

        BillDateColumn() {
            super(Lang.getBillDate());
            setMinWidth(120.0d);
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TSapFiBill, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<TSapFiBill, String> param) {
                    valueProperty.set(Lang.toDate(param.getValue().getInvoiceDate()));
                    return valueProperty;
                }
            });
        }

    }

    private class BillFiscalYearColumn extends TableColumn<TSapFiBill, String> {

        private ObjectProperty<String> valueProperty = new SimpleObjectProperty<>("");

        BillFiscalYearColumn() {
            super(Lang.getBillFiscalYear());
            setMinWidth(60.0d);
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TSapFiBill, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<TSapFiBill, String> param) {
                    valueProperty.set(String.valueOf(param.getValue().getFiscalYear()));
                    return valueProperty;
                }
            });
        }

    }

    private class BillKindColumn extends TableColumn<TSapFiBill, String> {

        private ObjectProperty<String> valueProperty = new SimpleObjectProperty<>("");

        BillKindColumn() {
            super(Lang.getBillKind());
            setMinWidth(80.0d);
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TSapFiBill, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<TSapFiBill, String> param) {
                    valueProperty.set(param.getValue().getInvoiceKind());
                    return valueProperty;
                }
            });
        }

    }

    private class BillStatusColumn extends TableColumn<TSapFiBill, String> {

        private ObjectProperty<String> valueProperty = new SimpleObjectProperty<>("");

        BillStatusColumn() {
            super(Lang.getBillStatus());
            setMinWidth(80.0d);
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TSapFiBill, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<TSapFiBill, String> param) {
                    valueProperty.set(param.getValue().getState());
                    return valueProperty;
                }
            });
        }

    }

//    private class BillNetValueColumn extends CurrencyColumn<TSapFiBill>{
    private class BillNetValueColumn extends DoubleColumn<TSapFiBill> {

        public BillNetValueColumn() {
//            super(Lang.getBillNetvalue());
            super(Lang.getBillNetvalue(), 2);
            setMinWidth(120.0d);
        }

        @Override
        public Double extractValue(TSapFiBill pValue) {
            return pValue.getNetValue();
        }

    }

    private class BillRecipientColumn extends StringColumn<TSapFiBill> {

        public BillRecipientColumn() {
            super(Lang.getBillRecipient(), true);
            setMinWidth(150.0d);
        }

        @Override
        public String extractValue(TSapFiBill pValue) {
            return pValue.getReceiverRef();
        }

    }

}
