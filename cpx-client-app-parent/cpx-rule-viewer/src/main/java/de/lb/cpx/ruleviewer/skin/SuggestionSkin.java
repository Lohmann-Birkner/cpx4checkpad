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
 *    2018  Your Organisation - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.skin;

import de.lb.cpx.client.core.model.fx.tooltip.BasicTooltip;
import de.lb.cpx.client.core.model.fx.tooltip.CpxTooltip;
import de.lb.cpx.rule.criteria.CriteriaHelper;
import de.lb.cpx.rule.criteria.model.CriterionTree.Supergroup.Group.Criterion;
import de.lb.cpx.rule.criteria.model.TypesAndOperations;
import de.lb.cpx.ruleviewer.cache.RuleMetaDataCache;
import de.lb.cpx.ruleviewer.enums.ViewMode;
import de.lb.cpx.ruleviewer.layout.RuleView;
import de.lb.cpx.ruleviewer.model.RuleMessageIndicator;
import de.lb.cpx.ruleviewer.model.Suggestion;
import de.lb.cpx.ruleviewer.model.ruletable.dialog.TableInfoDialog;
import de.lb.cpx.ruleviewer.util.TooltipHelper;
import de.lb.cpx.shared.json.RuleMessage;
import de.lb.cpx.shared.lang.Lang;
import java.io.IOException;
import java.util.Objects;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * Skinclass for the suggestion
 *
 * @author wilde
 */
public class SuggestionSkin extends SelectableControlSkin<Suggestion> {

    public static final String CONDITION_TEXT = "wenn";
    private Label lblActionId;
    private Label lblCriterion;
    private Label lblCriterionOperator;
//    private Label lblCriterionValue;
    private Label lblConditionText;
    private Label lblConditionCriterion;
    private Label lblConditionOperator;
//    private Label lblConditionValue;
    private HBox boxCondition;
    private Hyperlink hlCriterionValue;
    private Hyperlink hlConditionValue;
    private HBox boxContent;
    private RuleMessageIndicator messageIndicator;

    public SuggestionSkin(Suggestion pSkinnable) throws IOException {
        super(pSkinnable);

        //hide conditionpart
        pSkinnable.conditionActiveProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                if (getSkinnable().getConditionOperator() != null) {
                    showConditionArea(newValue);
//                }
            }
        });
        showConditionArea(pSkinnable.isConditionActive());

        pSkinnable.actionIdProperty().addListener(new ChangeListener<TypesAndOperations.SuggActions.SuggAction>() {
            @Override
            public void changed(ObservableValue<? extends TypesAndOperations.SuggActions.SuggAction> observable, TypesAndOperations.SuggActions.SuggAction oldValue, TypesAndOperations.SuggActions.SuggAction newValue) {
                updateActionId(newValue);
            }
        });
        lblActionId.fontProperty().bind(getSkinnable().fontProperty());
        updateActionId(pSkinnable.getActionId());
        pSkinnable.criterionProperty().addListener(new ChangeListener<Criterion>() {
            @Override
            public void changed(ObservableValue<? extends Criterion> observable, Criterion oldValue, Criterion newValue) {
                updateCriterion(newValue);
            }
        });
        updateCriterion(pSkinnable.getCriterion());
        lblCriterion.fontProperty().bind(getSkinnable().fontProperty());
        getSkinnable().criterionOperatorProperty().addListener(new ChangeListener<TypesAndOperations.OperationGroups.OperationGroup.Operation>() {
            @Override
            public void changed(ObservableValue<? extends TypesAndOperations.OperationGroups.OperationGroup.Operation> observable, TypesAndOperations.OperationGroups.OperationGroup.Operation oldValue, TypesAndOperations.OperationGroups.OperationGroup.Operation newValue) {
                updateCriterionOperator(newValue);
            }
        });
        updateCriterionOperator(getSkinnable().getCriterionOperator());

        pSkinnable.criterionValueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updateCriterionValue(newValue);
            }
        });
        updateCriterionValue(pSkinnable.getCriterionValue());
//        lblCriterionValue.fontProperty().bind(getSkinnable().fontProperty());
        hlCriterionValue.fontProperty().bind(getSkinnable().fontProperty());
        lblConditionCriterion.textProperty().bind(lblCriterion.textProperty());
        lblConditionCriterion.fontProperty().bind(getSkinnable().fontProperty());

        getSkinnable().conditionOperatorProperty().addListener(new ChangeListener<TypesAndOperations.OperationGroups.OperationGroup.Operation>() {
            @Override
            public void changed(ObservableValue<? extends TypesAndOperations.OperationGroups.OperationGroup.Operation> observable, TypesAndOperations.OperationGroups.OperationGroup.Operation oldValue, TypesAndOperations.OperationGroups.OperationGroup.Operation newValue) {
                updateConditionOperator(newValue);
            }
        });
        updateConditionOperator(getSkinnable().getConditionOperator());

        lblConditionOperator.fontProperty().bind(getSkinnable().fontProperty());

        pSkinnable.conditionValueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updateConditionValue(newValue);
            }
        });
        updateConditionValue(pSkinnable.getConditionValue());

        hlConditionValue.fontProperty().bind(getSkinnable().fontProperty());
        lblConditionText.setText(CONDITION_TEXT);
        lblConditionText.fontProperty().bind(getSkinnable().fontProperty());
        pSkinnable.criterionProperty().addListener(new ChangeListener<Criterion>() {
            @Override
            public void changed(ObservableValue<? extends Criterion> observable, Criterion oldValue, Criterion newValue) {
                updateCriterionTooltip(newValue);
            }
        });
        updateCriterionTooltip(getSkinnable().getCriterion());
        
        pSkinnable.messageProperty().addListener(new ChangeListener<RuleMessage>() {
            @Override
            public void changed(ObservableValue<? extends RuleMessage> ov, RuleMessage t, RuleMessage t1) {
                updateMessageIndicator();
            }

        });
        updateMessageIndicator();
    }

    private void updateCriterionOperator(TypesAndOperations.OperationGroups.OperationGroup.Operation pOperator) {
        if (pOperator != null) {
            lblCriterionOperator.setText(pOperator.toString());
            return;
        }
        lblCriterionOperator.setText(" ");
    }

    private void updateConditionOperator(TypesAndOperations.OperationGroups.OperationGroup.Operation pOperator) {
        if (pOperator != null) {
            if (pOperator.getName() != null && (!pOperator.getName().isEmpty())) {
                lblConditionOperator.setText(pOperator.toString());
                showConditionArea(true);
                return;
            }
        }
        lblConditionOperator.setText(" ");
        showConditionArea(false);
    }

    private void updateCriterionTooltip(Criterion newValue) {
        if (newValue == null) {
            setCriterionTooltip(null);
        } else {
            BasicTooltip tip = new BasicTooltip(CriteriaHelper.getDisplayName(newValue) + ":", TooltipHelper.getCriteriaTooltipText(newValue.getTooltip()));
            tip.setPrefWidth(200);
            tip.getScene().getStylesheets();
            setCriterionTooltip(tip);
        }
    }
    
    private void updateMessageIndicator() {
        if(messageIndicator == null){
            messageIndicator = new RuleMessageIndicator();
            messageIndicator.setPadding(new Insets(0, 8, 0, 0));
        }
        if(getSkinnable().getMessage() == null || (getSkinnable().getMessage().getDescription() == null || getSkinnable().getMessage().getDescription().isEmpty())){
            boxContent.getChildren().remove(messageIndicator);
            Tooltip.install(messageIndicator, null);
        }else{
            if(!boxContent.getChildren().contains(messageIndicator)){
                boxContent.getChildren().add(0,messageIndicator);
            }
            Tooltip.install(messageIndicator, new CpxTooltip(getSkinnable().getMessage().getDescription(),100,5000,100,true));
        }
    }
    
    private void setCriterionTooltip(Tooltip pTip) {
        lblCriterion.setTooltip(pTip);
        lblConditionCriterion.setTooltip(pTip);
    }

    private void updateActionId(TypesAndOperations.SuggActions.SuggAction newValue) {
        if (newValue == null) {
            lblActionId.setText("----");
            return;
        }
        lblActionId.setText(getTranslation(newValue.getDisplayName()));
    }

    private void updateCriterion(Criterion pCrit) {
        if (pCrit == null) {
            lblCriterion.setText("----");
            return;
        }
        lblCriterion.setText(Lang.get(pCrit.getCpname()).getValue());
    }
    private void showConditionArea(boolean pShow) {
        if (pShow) {
            if (!getRoot().getChildren().contains(boxCondition)) {
                getRoot().getChildren().add(boxCondition);
            }
        } else {
            getRoot().getChildren().remove(boxCondition);
        }
    }

    @Override
    protected Pane loadRoot() throws IOException {
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/Suggestion1.fxml"));
        lblActionId = (Label) root.lookup("#lblActionId");
        lblCriterion = (Label) root.lookup("#lblCriterion");
        lblCriterionOperator = (Label) root.lookup("#lblCriterionOperator");
//        lblCriterionValue = (Label) root.lookup("#lblCriterionValue");
        hlCriterionValue = (Hyperlink) root.lookup("#hlCriterionValue");
        lblConditionText = (Label) root.lookup("#lblConditionText");
        lblConditionCriterion = (Label) root.lookup("#lblConditionCriterion");
        lblConditionOperator = (Label) root.lookup("#lblConditionOperator");
        hlConditionValue = (Hyperlink) root.lookup("#hlConditionValue");
//        lblConditionValue = (Label) root.lookup("#lblConditionValue");
        boxCondition = (HBox) root.lookup("#boxCondition");
        boxContent = (HBox) root.lookup("#boxContent");
        if (root instanceof FlowPane) {
            ((FlowPane) root).prefWrapLengthProperty().bind(root.widthProperty());
        }
        return root;
    }

    public static String getTranslation(String pKey) {
        switch (pKey) {
            case "Rules.txt.list.add":
                return Lang.getRulesTxtListAdd();
            case "Rules.txt.list.delete":
                return Lang.getRulesTxtListDelete();
            case "Rules.txt.list.change":
                return Lang.getRulesTxtListChange();
            case "Rules.txt.list.do.nothing":
                return Lang.getRulesTxtListDoNothing();
            default:
                return Lang.get(pKey).getValue();
        }
    }
//    private void updateCriterionValue(String pCriterionValue) {
//        if(getSkinnable().isRuleTable(getSkinnable().getCriterionOperator())){
//            lblCriterionValue.setText(RuleMetaDataCache.instance().getTableNameInPool(RuleView.getFacade().getPool(), pCriterionValue));
//        }else{
//            lblCriterionValue.setText(pCriterionValue);
//        }
//    }

    private void updateCriterionValue(String pNewText) {

        if (getSkinnable().isRuleTable(getSkinnable().getCriterionOperator())) {
            hlCriterionValue.setText(RuleMetaDataCache.instance().getTableNameInPool(RuleView.getFacade().getPool(), pNewText));
            hlCriterionValue.setDisable(false);
        } else {
            pNewText = Objects.requireNonNullElse(pNewText, "");
            if (getSkinnable().isDouble(pNewText)) {
                pNewText = pNewText.replace(".", ",");
            }
            pNewText = pNewText.replace("%", "*");
            hlCriterionValue.setText(pNewText);
            hlCriterionValue.setDisable(true);
        }

        if (pNewText == null || pNewText.isEmpty()) {
            hlCriterionValue.setDisable(true);
            return;
        }

        if (ViewMode.READ_WRITE.equals(getSkinnable().getViewMode())) {
            hlCriterionValue.setDisable(true);
            return;
        }

        hlCriterionValue.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (getSkinnable().isRuleTable(getSkinnable().getCriterionOperator())) {
//                    AsyncPane<FlowPane> pane = new ReadOnlyRuleTableContentPane(getSkinnable().getCriterionValue());
//                    pane.setPrefHeight(100);
//                    ScrollPane scPane = new ScrollPane(pane);
//                    scPane.setFitToWidth(true);
//                    scPane.setFitToHeight(true);
//                    scPane.setMaxHeight(200);
//                    scPane.setPrefWidth(80);
//                    TitledDialog dialog = new TitledDialog(hlCriterionValue.getText(), getSkinnable().getScene().getWindow()) {
//                    };
//                    dialog.setContent(scPane);
                       TableInfoDialog dialog = new TableInfoDialog(hlCriterionValue.getText(), getSkinnable().getScene().getWindow());                    
                       dialog.showAndWait();
                    return;

                }
            }
        });

    }

    private void updateConditionValue(String pNewText) {

        if (getSkinnable().isRuleTable(getSkinnable().getConditionOperator())) {
            hlConditionValue.setText(RuleMetaDataCache.instance().getTableNameInPool(RuleView.getFacade().getPool(), pNewText));
            hlConditionValue.setDisable(false);
        } else {
            pNewText = Objects.requireNonNullElse(pNewText, "");
            pNewText = pNewText.replace("%", "*");
            hlConditionValue.setText(pNewText);
            hlConditionValue.setDisable(true);
        }

        if (pNewText == null || pNewText.isEmpty()) {
            hlConditionValue.setDisable(true);
            return;
        }

        if (ViewMode.READ_WRITE.equals(getSkinnable().getViewMode())) {
            hlConditionValue.setDisable(true);
            return;
        }

        hlConditionValue.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (getSkinnable().isRuleTable(getSkinnable().getConditionOperator())) {
//                    AsyncPane<FlowPane> pane = new ReadOnlyRuleTableContentPane(getSkinnable().getConditionValue());
//                    pane.setPrefHeight(100);
//                    ScrollPane scPane = new ScrollPane(pane);
//                    scPane.setFitToWidth(true);
//                    scPane.setFitToHeight(true);
//                    scPane.setMaxHeight(200);
//                    scPane.setPrefWidth(80);
//                    TitledDialog dialog = new TitledDialog(hlConditionValue.getText(), getSkinnable().getScene().getWindow()) {
//                    };
//                    dialog.setContent(scPane);
                    TableInfoDialog dialog = new TableInfoDialog(hlConditionValue.getText(), getSkinnable().getScene().getWindow());
                    dialog.showAndWait();
                    return;

                }
            }
        });
    }
//    private void updateConditionValue(String pConditionValue) {
//        if(getSkinnable().isRuleTable(getSkinnable().getConditionOperator())){
//            lblConditionValue.setText(RuleMetaDataCache.instance().getTableNameInPool(RuleView.getFacade().getPool(), pConditionValue));
//        }else{
//            lblConditionValue.setText(pConditionValue);
//        }
//    }
}
