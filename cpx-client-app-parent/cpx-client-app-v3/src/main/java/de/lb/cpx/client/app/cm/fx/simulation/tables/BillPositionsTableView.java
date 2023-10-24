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
import de.lb.cpx.model.TSapFiBillposition;
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
 * TableView for Bill Positions
 *
 * @author nandola
 */
public class BillPositionsTableView extends TableView<TSapFiBillposition> {

    public BillPositionsTableView() {
        super();
        setUpColumns();
//        setFixedCellSize(35);

        setRowFactory(new Callback<TableView<TSapFiBillposition>, TableRow<TSapFiBillposition>>() {
            @Override
            public TableRow<TSapFiBillposition> call(TableView<TSapFiBillposition> param) {
                final TableRow<TSapFiBillposition> row = new TableRow<>();
                final ContextMenu contextMenu = new CtrlContextMenu<>();

                MenuItem menuItemExportExcel = new MenuItem("Tabelle als XLS exportieren");
                menuItemExportExcel.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        final ExcelCsvFileManager mgr = new ExcelCsvFileManager(ExportTypeEn.EXCEL, "RechnungPositionen", BillPositionsTableView.this);
                        mgr.openDialog(getScene().getWindow());
                    }
                });

                MenuItem menuItemExportCsv = new MenuItem("Tabelle als TXT (CSV) exportieren");
                menuItemExportCsv.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        final ExcelCsvFileManager mgr = new ExcelCsvFileManager(ExportTypeEn.CSV, "RechnungPositionen", BillPositionsTableView.this);
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
        getColumns().addAll(new BillNoColumn(), new PositionNoColumn(), new BillPositionTextColumn(),
                new PositionsReferenceColumn(), new BillPositionsAmountColumn(),
                new PositionsNetAmountColumn(), new PositionsBaseAmountColumn());
    }

    private class BillNoColumn extends StringColumn<TSapFiBillposition> {

        public BillNoColumn() {
            super(Lang.getBillPositionBillNo(), true);
            setMinWidth(140.0d);
//            setMaxWidth(150.0d);
//            setResizable(false);
        }

        @Override
        public String extractValue(TSapFiBillposition pValue) {
            return pValue.getInvoice();
        }
    }

    private class PositionNoColumn extends TableColumn<TSapFiBillposition, String> {

        private ObjectProperty<String> valueProperty = new SimpleObjectProperty<>("");

        PositionNoColumn() {
            super(Lang.getBillPositionPositionNo());
            setMinWidth(140.0d);
//            setMaxWidth(150.0d);
//            setResizable(false);
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TSapFiBillposition, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<TSapFiBillposition, String> param) {
                    valueProperty.set(param.getValue().getPositionNumber());
                    return valueProperty;
                }
            });
        }
    }

    private class BillPositionTextColumn extends StringColumn<TSapFiBillposition> {

        public BillPositionTextColumn() {
            //don't show OverrunHelperButton
//            super(Lang.getBillPositionText(), false);
            super(Lang.getBillPositionText(), OverrunStyleEn.Tooltip);
            setMinWidth(360d);
//            setMaxWidth(450d);
//            setMaxWidth(Double.MAX_VALUE);
//            setSortable(true);
//            setResizable(false);
        }

        @Override
        public String extractValue(TSapFiBillposition pValue) {
            return pValue.getText();
        }

    }

    private class PositionsReferenceColumn extends TableColumn<TSapFiBillposition, String> {

        private ObjectProperty<String> valueProperty = new SimpleObjectProperty<>("");

        PositionsReferenceColumn() {
            super(Lang.getBillPositionReference());
            setMinWidth(140.0d);
//            setMaxWidth(150.0d);
//            setResizable(false);
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TSapFiBillposition, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<TSapFiBillposition, String> param) {
                    valueProperty.set(param.getValue().getReferenceId());
                    return valueProperty;
                }
            });
        }
    }

    private class BillPositionsAmountColumn extends TableColumn<TSapFiBillposition, String> {

        private ObjectProperty<String> valueProperty = new SimpleObjectProperty<>("");

        BillPositionsAmountColumn() {
            super(Lang.getBillPositionAmount());
            setMinWidth(120.0d);
//            setMaxWidth(130.0d);
//            setResizable(false);
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TSapFiBillposition, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<TSapFiBillposition, String> param) {
                    valueProperty.set(String.valueOf(param.getValue().getAmount()));
                    return valueProperty;
                }
            });
        }

    }

//    private class PositionsNetAmountColumn extends CurrencyColumn<TSapFiBillposition> {
    private class PositionsNetAmountColumn extends DoubleColumn<TSapFiBillposition> {

        public PositionsNetAmountColumn() {
//            super("Lang.getBillPositionNetAmount()");
            super(Lang.getBillPositionNetAmount(), 2);
            setMinWidth(120.0d);
//            setMaxWidth(130.0d);
//            setResizable(false);
        }

        @Override
        public Double extractValue(TSapFiBillposition pValue) {
            return Objects.requireNonNullElse(pValue.getNetValue(), 0D);
        }

    }

//    private class PositionsBaseAmountColumn extends CurrencyColumn<TSapFiBillposition> {
    private class PositionsBaseAmountColumn extends DoubleColumn<TSapFiBillposition> {

        public PositionsBaseAmountColumn() {
            super(Lang.getBillPositionBaseAmount(), 2);
            setMinWidth(120.0d);
//            setMaxWidth(130.0d);
//            setResizable(false);
        }

        @Override
        public Double extractValue(TSapFiBillposition pValue) {
            return Objects.requireNonNullElse(pValue.getBaseValue(), 0D);
        }

    }

}
