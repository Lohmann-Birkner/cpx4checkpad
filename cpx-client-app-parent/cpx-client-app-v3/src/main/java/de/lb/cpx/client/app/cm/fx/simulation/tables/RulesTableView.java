/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.cm.fx.simulation.tables;

import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.fx.dialog.UnattachedDialog;
import de.lb.cpx.client.core.model.fx.tableview.column.rules.RuleCategoryColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.rules.RuleDcwColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.rules.RuleDrgCareCwColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.rules.RuleDrgRevenueColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.rules.RuleDrgSimColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.rules.RuleErrorTypeColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.rules.RuleNumberColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.rules.RulePeppSimColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.rules.RuleReferenceColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.rules.RuleRiskAreasColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.rules.RuleRiskWastePercentValueColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.rules.RuleRiskAuditPercentValueColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.rules.RuleSelectColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.rules.RuleSuggestionColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.rules.RuleTextColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.rules.RuleTypeColumn;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.ruleviewer.CrgRuleView;
import de.lb.cpx.ruleviewer.cache.RuleMetaDataCache;
import de.lb.cpx.shared.dto.rules.CpxSimpleRuleDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.util.Callback;

/**
 * New TableView implementation for displaying rules in case simulation TODO:
 * make some basic class to handle different case types?
 *
 * @author wilde
 */
public class RulesTableView extends TableView<CpxSimpleRuleDTO> {

    private static final double CONTEXT_MENU_MOUSE_SPACING = 12.0;
    private static final Logger LOG = Logger.getLogger(RulesTableView.class.getName());
    private final ObjectProperty<CaseTypeEn> caseTypeProperty = new SimpleObjectProperty<>();
    private final RuleRowContextMenu contextMenu = new RuleRowContextMenu();
    private boolean showRelevantRules =  CpxClientConfig.instance().getShowRelevantRules();
    private BooleanProperty rulesSelectProperty = new SimpleBooleanProperty(false);
    private Callback<CpxSimpleRuleDTO, Boolean> saveSelection4Rule;

    public RulesTableView() {
        setPrefHeight(200);
        setColumnResizePolicy(UNCONSTRAINED_RESIZE_POLICY);
        caseTypeProperty.addListener(new ChangeListener<CaseTypeEn>() {
            @Override
            public void changed(ObservableValue<? extends CaseTypeEn> observable, CaseTypeEn oldValue, CaseTypeEn newValue) {
                setUpColumns();
            }
        });
        setRowFactory(new Callback<TableView<CpxSimpleRuleDTO>, TableRow<CpxSimpleRuleDTO>>() {
            @Override
            public TableRow<CpxSimpleRuleDTO> call(TableView<CpxSimpleRuleDTO> param) {
                TableRow<CpxSimpleRuleDTO> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && (!row.isEmpty())) {
                        CpxSimpleRuleDTO rowData = row.getItem();
                        showRule(rowData);
                        event.consume();
                    }
                    if (MouseButton.SECONDARY.equals(event.getButton())) {
                        if (!row.isEmpty()) {
                            CpxSimpleRuleDTO rowData = row.getItem();
                            contextMenu.show(rowData, row, event.getScreenX() + CONTEXT_MENU_MOUSE_SPACING, event.getScreenY() + CONTEXT_MENU_MOUSE_SPACING);
                        }
                    }
                });
                return row;
            }
        });
    }

    public void setCaseType(CaseTypeEn pType) {
        caseTypeProperty.set(pType);
    }

    public CaseTypeEn getCaseType() {
        return caseTypeProperty.get();
    }

    private List<TableColumn<CpxSimpleRuleDTO, ?>> setUpCaseTypeColumns(CaseTypeEn pType) {
        switch (pType) {
            case DRG:
                return setUpDrgColumns();
            case PEPP:
                return setUpPeppColumns();
            default:
                LOG.log(Level.WARNING, "Unknown CaseType set for TableView! Case Type was {0}", pType.toString());
                return new ArrayList<>();
        }

    }

    private List<TableColumn<CpxSimpleRuleDTO, ?>> setUpDrgColumns() {
        List<TableColumn<CpxSimpleRuleDTO, ?>> cols = new ArrayList<>();
        RuleDrgSimColumn drgCol = new RuleDrgSimColumn();
        RuleDrgCareCwColumn careCwCol = new RuleDrgCareCwColumn();
        RuleDrgRevenueColumn revCol = new RuleDrgRevenueColumn();
        cols.add(careCwCol);
        cols.add(revCol);
        cols.add(drgCol);
        return cols;
//        getColumns().add(drgCol);
//        numberBinding = Bindings.add(drgCol.widthProperty(), 0);
//        numberBinding.add(drgCol.widthProperty());
//        w.add(drgCol.widthProperty());
//        setUpCommonColumns();
    }

    private List<TableColumn<CpxSimpleRuleDTO, ?>> setUpPeppColumns() {
        List<TableColumn<CpxSimpleRuleDTO, ?>> cols = new ArrayList<>();
        RulePeppSimColumn peppCol = new RulePeppSimColumn();
        cols.add(peppCol);
        return cols;
//        getColumns().add(peppCol);
//        numberBinding = Bindings.add(peppCol.widthProperty(), 0);
//        numberBinding.add(peppCol.widthProperty());
//        w.add(peppCol.widthProperty());
//        setUpCommonColumns();
    }

    @SuppressWarnings("unchecked")
    private void setUpColumns() {
        getColumns().clear();
        RuleTypeColumn typeCol = new RuleTypeColumn();
        RuleSelectColumn selColumn = new RuleSelectColumn();
        RuleErrorTypeColumn errorCol = new RuleErrorTypeColumn();
        RuleNumberColumn numbCol = new RuleNumberColumn();
        RuleTextColumn txtCol = new RuleTextColumn();
        RuleCategoryColumn catCol = new RuleCategoryColumn();
        RuleSuggestionColumn suggCol = new RuleSuggestionColumn();
        RuleReferenceColumn refCol = new RuleReferenceColumn();
        RuleDcwColumn dcwCol = new RuleDcwColumn();
        RuleRiskAreasColumn areasCol = new RuleRiskAreasColumn();
        RuleRiskAuditPercentValueColumn auditPercCol = new RuleRiskAuditPercentValueColumn();
        RuleRiskWastePercentValueColumn wastePercCol = new RuleRiskWastePercentValueColumn();

        getColumns().addAll(typeCol, //selColumn, 
                errorCol, numbCol, txtCol, catCol, suggCol, refCol, dcwCol, areasCol, auditPercCol, wastePercCol);

        //create binding to compute the actual size of each row with fixed sizes
        //so that other rows could compute its sizes
        NumberBinding numberBinding = Bindings.add(typeCol.widthProperty(), 0)
                .add(errorCol.widthProperty())
//               .add(selColumn.widthProperty())
                .add(numbCol.widthProperty())
                .add(refCol.widthProperty())
                .add(dcwCol.widthProperty())
                .add(catCol.widthProperty())
                .add(areasCol.widthProperty())
                .add(auditPercCol.widthProperty())
                .add(wastePercCol.widthProperty())
                //add for scrollbar
                .add(15);
        if(showRelevantRules){
            getColumns().add(1, selColumn);
            numberBinding.add(selColumn.widthProperty());

            rulesSelectProperty.addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    selColumn.setRulesSelect(newValue);
                }
            });
            
            Callback<CpxSimpleRuleDTO, Boolean> onSelectRule = new Callback<CpxSimpleRuleDTO, Boolean>(){
                public Boolean call(CpxSimpleRuleDTO rule){
                        if(saveSelection4Rule !=null && rule != null){
                            return saveSelection4Rule.call(rule);
                        }
                    return true;
                }
            };
            selColumn.setOnSelectRule(onSelectRule);
        }
        
        List<TableColumn<CpxSimpleRuleDTO, ?>> caseTypeCols = setUpCaseTypeColumns(getCaseType());
        for (TableColumn<CpxSimpleRuleDTO, ?> col : caseTypeCols) {
            numberBinding = Bindings.add(numberBinding, col.widthProperty());
            getColumns().add(col);
        }
        LOG.log(Level.FINEST, "after creation value {0}", numberBinding.getValue().doubleValue());
        txtCol.prefWidthProperty().bind(widthProperty().subtract(numberBinding).divide(2));
        suggCol.prefWidthProperty().bind(widthProperty().subtract(numberBinding).divide(2));
        txtCol.setResizable(false);
        suggCol.setResizable(false);

//        typeCol.setSortType(TableColumn.SortType.DESCENDING);
//        getSortOrder().add(typeCol);
//        sort();
    }

    //show rule, here it should be implemented if the read only view is started, or the rule editor
    //depends on requirements, such as: 
    //-the user has the right to open the rule editor 
    //-the rule editor is installed on the pc/enviroment, that it could be displayed
    //-the licence is valid for rule editor
    //-etc.
    private void showRule(CpxSimpleRuleDTO pRule) {
        UnattachedDialog dialog = new UnattachedDialog(pRule.getRuleNumber() + " - " + pRule.getCaption());
        RuleMetaDataCache.instance().clearMapPoolToTable();
        RuleMetaDataCache.instance().clearRuleTypes();
        CrgRuleView pane = new CrgRuleView();
        pane.setRules(Session.instance().getEjbConnector().connectToRuleServiceBean().get().getRule(Integer.parseInt(pRule.getRuleId())));
        Session.instance().getEjbConnector().connectToRuleServiceBean().get().printStatistic();
        dialog.setContent(pane);
        dialog.show();
    }

    public void setRulesSelect(boolean b) {
        rulesSelectProperty.set(b);
    }
    
    public boolean isRuleSelect(){
        return rulesSelectProperty.get();
    }

    public void setSaveSelection4Rule(Callback<CpxSimpleRuleDTO, Boolean> pSaveSelection4Rule) {
       saveSelection4Rule = pSaveSelection4Rule;
    }

    private class RuleRowContextMenu extends ContextMenu {

        private ObjectProperty<CpxSimpleRuleDTO> ruleProperty;

        public RuleRowContextMenu() {
            super();
            MenuItem showRule = new MenuItem("Regel öffnen");
            showRule.setStyle("-fx-text-fill:black;");
            showRule.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    showRule(getRule());
                }
            });
            getItems().add(showRule);
        }

        public ObjectProperty<CpxSimpleRuleDTO> ruleProperty() {
            if (ruleProperty == null) {
                ruleProperty = new SimpleObjectProperty<>();
            }
            return ruleProperty;
        }

        public CpxSimpleRuleDTO getRule() {
            return ruleProperty().get();
        }

        public void setRule(CpxSimpleRuleDTO pRule) {
            ruleProperty().set(pRule);
        }

        public void show(CpxSimpleRuleDTO pRule, Node pAnchor, Double pX, Double pY) {
            setRule(pRule);
            if (!isShowing()) {
                show(pAnchor, pX, pY);
            } else {
                hide();
            }
        }
    }
}
