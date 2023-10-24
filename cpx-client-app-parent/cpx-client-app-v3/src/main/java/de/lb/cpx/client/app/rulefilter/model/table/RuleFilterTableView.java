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
 *    2019  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.rulefilter.model.table;

import de.lb.cpx.client.core.model.fx.contextmenu.CtrlContextMenu;
import de.lb.cpx.client.core.model.fx.tableview.column.StringColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.enums.OverrunStyleEn;
import de.lb.cpx.client.core.util.ExcelCsvFileManager;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.model.enums.ExportTypeEn;
import de.lb.cpx.model.enums.RuleTypeEn;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.shared.lang.Lang;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.util.Callback;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

/**
 *
 * @author nandola
 */
public class RuleFilterTableView extends TableView<CrgRules> {

    public RuleFilterTableView() {
        super();
        setUpColumns();

        setRowFactory((TableView<CrgRules> param) -> {

            final TableRow<CrgRules> row = new TableRow<>();

            final ContextMenu contextMenu = new CtrlContextMenu<>();

            MenuItem menuItemExportExcel = new MenuItem("Tabelle als XLS exportieren");
            menuItemExportExcel.setOnAction((ActionEvent event) -> {
                final ExcelCsvFileManager mgr = new ExcelCsvFileManager(ExportTypeEn.EXCEL, "Regeln", this);
                mgr.openDialog(getScene().getWindow());
            });

            MenuItem menuItemExportCsv = new MenuItem("Tabelle als TXT (CSV) exportieren");
            menuItemExportCsv.setOnAction((ActionEvent event) -> {
                final ExcelCsvFileManager mgr = new ExcelCsvFileManager(ExportTypeEn.CSV, "Regeln", this);
                mgr.openDialog(getScene().getWindow());
            });
            contextMenu.getItems().addAll(menuItemExportExcel, menuItemExportCsv);

            // Set context menu on row, but use a binding to make it only show for non-empty rows:
            row.contextMenuProperty().bind(Bindings.when(row.emptyProperty())
                    .then((ContextMenu) null).otherwise(contextMenu));

            return row;
        });
    }

    @SuppressWarnings("unchecked")
    private void setUpColumns() {
        getColumns().addAll(new CheckBoxColumn(),
                //                new RuleIdColumn(),
                new RuleNumberColumn(),
                new RuleIdentNumberColumn(),
                new RuleDescriptionColumn(),
                new RuleSuggestionColumn(),
                new RuleErrorTypeColumn(),
                new RuleCategoryColumn(),
                new RuleTypeColumn(),
                new RuleYearColumn()
        );
    }

    private class CheckBoxColumn extends TableColumn<CrgRules, Boolean> {

        private BooleanProperty booleanProperty = new SimpleBooleanProperty(false);

        public CheckBoxColumn() {
            super("S");
            setMinWidth(40.0d);
            setMaxWidth(40.0d);
//            setResizable(false);

            // to determine how cells are displayed.
            setCellFactory(new Callback<TableColumn<CrgRules, Boolean>, TableCell<CrgRules, Boolean>>() {
                @Override
                public TableCell<CrgRules, Boolean> call(TableColumn<CrgRules, Boolean> p) {
                    return new CreateCheckBoxTableCell();
                }
            });

            // determines the values represented by the cell.
            setCellValueFactory(new Callback<CellDataFeatures<CrgRules, Boolean>, ObservableValue<Boolean>>() {
                @Override
                public ObservableValue<Boolean> call(CellDataFeatures<CrgRules, Boolean> p) {
                    return booleanProperty;
                }
            });

            setEditable(true);
            editableProperty().setValue(Boolean.TRUE);
        }

    }

//    private class RuleDescriptionColumn extends TableColumn<CrgRules, String> {
    private class RuleDescriptionColumn extends StringColumn<CrgRules> {

        public RuleDescriptionColumn() {
            super(Lang.getRuleFilterDialogRuleDescription(), OverrunStyleEn.Tooltip);
            setMinWidth(100.0d);
//            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CrgRules, String>, ObservableValue<String>>() {
//                @Override
//                public ObservableValue<String> call(TableColumn.CellDataFeatures<CrgRules, String> param) {
//                    valueProperty.set(param.getValue().getCrgrCaption());
//                    return valueProperty;
//                }
//            });
        }

        @Override
        public String extractValue(CrgRules pValue) {
            return pValue.getCrgrCaption();
        }
    }

//    private class RuleNumberColumn extends TableColumn<CrgRules, String> {
    private class RuleNumberColumn extends StringColumn<CrgRules> {

        public RuleNumberColumn() {
            super(Lang.getRuleFilterDialogRuleNumber(), OverrunStyleEn.Tooltip);
            setMinWidth(60.0);
//            setMaxWidth(60.0);

//            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CrgRules, String>, ObservableValue<String>>() {
//                @Override
//                public ObservableValue<String> call(TableColumn.CellDataFeatures<CrgRules, String> param) {
//                    valueProperty.set(param.getValue().getCrgrNumber());
//                    return valueProperty;
//                }
//            });
        }

        @Override
        public String extractValue(CrgRules pValue) {
            return pValue.getCrgrNumber();
        }
    }

//    private class RuleSuggestionColumn extends TableColumn<CrgRules, String> {
    private class RuleSuggestionColumn extends StringColumn<CrgRules> {

        public RuleSuggestionColumn() {
            super(Lang.getRuleFilterDialogRuleSuggestion(), OverrunStyleEn.Tooltip);
            setMinWidth(100.0d);
//            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CrgRules, String>, ObservableValue<String>>() {
//                @Override
//                public ObservableValue<String> call(TableColumn.CellDataFeatures<CrgRules, String> param) {
//                    valueProperty.set(param.getValue().getCrgrSuggText());
//                    return valueProperty;
//                }
//            });
        }

        @Override
        public String extractValue(CrgRules pValue) {
            return pValue.getCrgrSuggText();
        }
    }

    private class RuleErrorTypeColumn extends TableColumn<CrgRules, RuleTypeEn> {

//        private ObjectProperty<String> valueProperty = new SimpleObjectProperty<>("");
        public RuleErrorTypeColumn() {
            super(Lang.getRuleFilterDialogRuleErrorStatus());
            setMinWidth(50.0d);
//            setMaxWidth(50.0d);
//            setResizable(false);

            setCellValueFactory(new Callback<CellDataFeatures<CrgRules, RuleTypeEn>, ObservableValue<RuleTypeEn>>() {
                @Override
                public ObservableValue<RuleTypeEn> call(CellDataFeatures<CrgRules, RuleTypeEn> p) {
                    return new SimpleObjectProperty<>(p.getValue().getCrgrRuleErrorType());
                }
            });
//            setComparator(new Comparator<RuleTypeEn>() {
//                @Override
//                public int compare(RuleTypeEn o1, RuleTypeEn o2) {
//                }
//            });
            setCellFactory(new Callback<TableColumn<CrgRules, RuleTypeEn>, TableCell<CrgRules, RuleTypeEn>>() {
                @Override
                public TableCell<CrgRules, RuleTypeEn> call(TableColumn<CrgRules, RuleTypeEn> p) {
                    return new RuleErrorTypeCell();
                }
            });
//            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CrgRules, String>, ObservableValue<String>>() {
//                @Override
//                public ObservableValue<String> call(TableColumn.CellDataFeatures<CrgRules, String> param) {
//                    valueProperty.set(param.getValue().getCrgrRuleErrorType().name());
////                    valueProperty.set(param.getValue().getCrgRuleTypes().getCrgtDisplayText()); // gives Lazy initialization exception
////                    valueProperty.set("");
//                    return valueProperty;
//                }
//            });
        }

        private class RuleErrorTypeCell extends TableCell<CrgRules, RuleTypeEn> {

            @Override
            protected void updateItem(RuleTypeEn pItem, boolean pEmpty) {
                super.updateItem(pItem, pEmpty); //To change body of generated methods, choose Tools | Templates.
                if (pItem == null || pEmpty) {
                    setGraphic(null);
                    return;
                }
                setGraphic(getIcon(pItem));
            }

            private Glyph getIcon(RuleTypeEn pItem) {
                switch (pItem) {
                    case STATE_ERROR:
                        setTooltip(new Tooltip("Fehler"));
                        Glyph error = ResourceLoader.getGlyph(FontAwesome.Glyph.EXCLAMATION_CIRCLE);
                        error.setStyle("-fx-text-fill:red;");
                        return error;
                    case STATE_WARNING:
                        setTooltip(new Tooltip("Warnung"));
                        Glyph warning = ResourceLoader.getGlyph(FontAwesome.Glyph.WARNING);
                        warning.setStyle("-fx-text-fill:orange;");
                        return warning;
                    case STATE_SUGG:
                        setTooltip(new Tooltip("Information"));
                        Glyph sugg = ResourceLoader.getGlyph(FontAwesome.Glyph.INFO_CIRCLE);
                        sugg.setStyle("-fx-text-fill:lightblue;");
                        return sugg;
                    default:
                        setTooltip(new Tooltip("Unbestimmt"));
                        return ResourceLoader.getGlyph(FontAwesome.Glyph.QUESTION_CIRCLE);
                }
            }

        }
    }

//    private class RuleCategoryColumn extends TableColumn<CrgRules, String> {
    private class RuleCategoryColumn extends StringColumn<CrgRules> {

        public RuleCategoryColumn() {
            super(Lang.getRuleFilterDialogRuleCategory(), OverrunStyleEn.Tooltip);
            setMinWidth(100.0d);
//            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CrgRules, String>, ObservableValue<String>>() {
//                @Override
//                public ObservableValue<String> call(TableColumn.CellDataFeatures<CrgRules, String> param) {
//                    valueProperty.set(param.getValue().getCrgrCategory());
//                    return valueProperty;
//                }
//            });
        }

        @Override
        public String extractValue(CrgRules pValue) {
            return pValue.getCrgrCategory();
        }
    }

//    private class RuleIdentNumberColumn extends TableColumn<CrgRules, String> {
    private class RuleIdentNumberColumn extends StringColumn<CrgRules> {

        public RuleIdentNumberColumn() {
            super(Lang.getRuleFilterDialogRuleIdent(), OverrunStyleEn.Tooltip);
            setMinWidth(100.0d);
//            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CrgRules, String>, ObservableValue<String>>() {
//                @Override
//                public ObservableValue<String> call(TableColumn.CellDataFeatures<CrgRules, String> param) {
//                    valueProperty.set(param.getValue().getCrgrIdentifier());
//                    return valueProperty;
//                }
//            });
        }

        @Override
        public String extractValue(CrgRules pValue) {
            return pValue.getCrgrIdentifier();
        }
    }

    private class RuleTypeColumn extends StringColumn<CrgRules> {

        public RuleTypeColumn() {
            super(Lang.getRuleFilterDialogRuleType(), OverrunStyleEn.Tooltip);
            setMinWidth(100.0);
        }

        @Override
        public String extractValue(CrgRules pValue) {
            return pValue.getCrgRuleTypes().getCrgtDisplayText();
        }
    }

    private class RuleYearColumn extends StringColumn<CrgRules> {

        public RuleYearColumn() {
            super(Lang.getRuleFilterDialogPoolYear(), OverrunStyleEn.Tooltip);
            setMinWidth(48.0);
//            setMaxWidth(48.0);
        }

        @Override
        public String extractValue(CrgRules pValue) {
            return String.valueOf(pValue.getCrgRulePools().getCrgplPoolYear());
        }
    }
}
