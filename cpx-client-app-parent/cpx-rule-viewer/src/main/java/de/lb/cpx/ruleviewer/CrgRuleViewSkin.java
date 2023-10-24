/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer;

import de.lb.cpx.client.core.model.fx.pane.AsyncPane;
import de.lb.cpx.client.core.model.fx.tooltip.BasicTooltip;
import de.lb.cpx.client.core.model.fx.tooltip.CpxTooltip;
import de.lb.cpx.client.core.util.OverrunHelper;
import de.lb.cpx.model.enums.RuleTypeEn;
import de.lb.cpx.rule.element.CaseRuleManager;
import de.lb.cpx.rule.element.model.Risk;
import de.lb.cpx.rule.element.model.Rule;
import de.lb.cpx.ruleviewer.layout.RuleView;
import de.lb.cpx.ruleviewer.layout.SuggestionView;
import de.lb.cpx.ruleviewer.util.RiskDisplayHelper;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.shared.lang.Lang;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SkinBase;
import javafx.scene.control.SplitPane;
import javafx.scene.control.skin.ScrollPaneSkin;
import javafx.scene.control.skin.SplitPaneSkin;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * Skin class for rule view handles layout definied in CrgRuleView.fxml
 *
 * @author wilde
 */
public class CrgRuleViewSkin extends SkinBase<CrgRuleView> {

    private static final Logger LOG = Logger.getLogger(CrgRuleViewSkin.class.getName());

    private Label lblNumberValue;
    private Label lblNameValue;
    private Label lblTypeValue;
    private Label lblCategoryValue;
    private Label lblRolesValue;
    private Label lblFromValue;
    private Label lblToValue;
    private Label lblNoticeValue;
    private Label lblSuggTextValue;
    private AsyncPane<RuleView> rulePane;
    private AsyncPane<SuggestionView> suggestionPane;
    private Label lblRiskAreasValue;
    private Label lblRiskAuditPercentValue;
    private Label lblRiskWastePercentValue;
    private Label lblRuleTypeValue;

    /**
     * contruct new instance, loads fxml and displayes values based in
     * CrgRuleView
     *
     * @param pSkinnable control to display data for
     * @throws IOException thrown if fxml could not be loaded
     */
    public CrgRuleViewSkin(CrgRuleView pSkinnable) throws IOException {
        super(pSkinnable);
        updateUi();
        pSkinnable.ruleProperty().addListener(new ChangeListener<CrgRules>() {
            @Override
            public void changed(ObservableValue<? extends CrgRules> observable, CrgRules oldValue, CrgRules newValue) {
                updateRulesValues(newValue);
            }
        });
    }

    /**
     * @return loads the layout from fxml and initialize ui components
     * @throws IOException cannot load fxml
     */
    protected final Parent fetchLayout() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/CrgRuleView.fxml"));
        //load and set lables
        //descriptions
        Label lblNumber = (Label) root.lookup("#lblNumber");
        lblNumber.setText("Nummer");
        Label lblName = (Label) root.lookup("#lblName");
        lblName.setText("Bezeichnung");
        Label lblType = (Label) root.lookup("#lblType");
        lblType.setText("Fehlerart");
        Label lblCategory = (Label) root.lookup("#lblCategory");
        lblCategory.setText("Kategorie");
        Label lblRoles = (Label) root.lookup("#lblRoles");
        lblRoles.setText("Rollen");
        Label lblFrom = (Label) root.lookup("#lblFrom");
        lblFrom.setText("Gültig von");
        Label lblTo = (Label) root.lookup("#lblTo");
        lblTo.setText("Gültig bis");
        Label lblRiskAuditPercent = (Label) root.lookup("#lblRiskAuditPercent");
        lblRiskAuditPercent.setText(new StringBuilder(Lang.getRiskEditorRiskAuditValue()).toString());
        Label lblRiskWastePercent = (Label) root.lookup("#lblRiskWastePercent");
        lblRiskWastePercent.setText(new StringBuilder(Lang.getRiskEditorRiskWasteValue()).toString());
        Label lblRiskAreas = (Label) root.lookup("#lblRiskAreas");
        lblRiskAreas.setText(new StringBuilder(Lang.getRiskArea()).toString());
        Label lblRuleType = (Label) root.lookup("#lblRuleType");
        lblRuleType.setText(new StringBuilder("Regeltyp").toString());
        
        //editable values
        lblNumberValue = (Label) root.lookup("#lblNumberValue");
        lblNameValue = (Label) root.lookup("#lblNameValue");
        lblTypeValue = (Label) root.lookup("#lblTypeValue");
        lblCategoryValue = (Label) root.lookup("#lblCategoryValue");
        lblRolesValue = (Label) root.lookup("#lblRolesValue");
        lblFromValue = (Label) root.lookup("#lblFromValue");
        lblToValue = (Label) root.lookup("#lblToValue");
        lblRiskAuditPercentValue = (Label) root.lookup("#lblRiskAuditPercentValue");
        lblRiskWastePercentValue = (Label) root.lookup("#lblRiskWastePercentValue");
        lblRiskAreasValue = (Label) root.lookup("#lblRiskAreasValue");
        lblRuleTypeValue = (Label) root.lookup("#lblRuleTypeValue");
        
        ScrollPane spNotice = (ScrollPane) root.lookup("#spNotice");
        spNotice.setSkin(new ScrollPaneSkin(spNotice));
        lblNoticeValue = (Label) root.lookup("#lblNoticeValue");

        //pseudo hack to get childeren stored in splitpane, otherwise lookup will fail!
        SplitPane spDefinition = (SplitPane) root.lookup("#spDefinition");
        spDefinition.setSkin(new SplitPaneSkin(spDefinition));

        //load rule async to avoid lag on big rules, due to heavy processing on the main task
        rulePane = new AsyncPane<>() {
            @Override
            public RuleView loadContent() {
                //loads new rule view instance!
                //set font and layout variables
                RuleView ruleView = new RuleView(getSkinnable().getPool(), getSkinnable().getRule());
                ruleView.setFont(Font.font("Courier New", 15));
                ruleView.setPadding(new Insets(0, 0, 0, 16));
                ruleView.setMaxHeight(Double.MAX_VALUE);
                //fetch rule from defintion
                Rule ruleXml = CaseRuleManager.transformRuleInUTF16(getSkinnable().getRule().getCrgrDefinition());
//                ruleView.setCrgRules(getSkinnable().getRule());
                ruleView.setRule(ruleXml);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        updateRuleRiskValues(ruleXml);
                    }
                });
                return ruleView;
            }
            
        };
        VBox boxRule = (VBox) root.lookup("#boxRule");
        VBox.setVgrow(rulePane, Priority.ALWAYS);
        boxRule.getChildren().add(rulePane);
        Label lblSuggText = (Label) root.lookup("#lblSuggText");
        lblSuggText.setText("Beschreibung");
        lblSuggTextValue = (Label) root.lookup("#lblSuggTextValue");
        VBox boxSuggestion = (VBox) root.lookup("#boxSuggestion");
        suggestionPane = new AsyncPane<>() {
            @Override
            public SuggestionView loadContent() {
                SuggestionView suggestionView = new SuggestionView();
                suggestionView.setFont(Font.font("Courier New", 15));
                suggestionView.setPadding(new Insets(0, 0, 0, 6));
                Rule ruleXml = CaseRuleManager.transformRuleInUTF16(getSkinnable().getRule().getCrgrDefinition());
                suggestionView.setRule(ruleXml);
                return suggestionView;
            }
        };
        VBox.setVgrow(suggestionPane, Priority.ALWAYS);
        boxSuggestion.getChildren().add(suggestionPane);
        return root;
    }
    
    private void updateRuleRiskValues(Rule ruleXml) {
//        if(ruleXml == null){
//            lblRiskAreasValue.setText("");
//            lblRiskPercentValue.setText("");
//            return;
//        }
        Risk risk = ruleXml != null?ruleXml.getRisks():null;
        lblRiskAreasValue.setText(RiskDisplayHelper.getTranslatedRiskAreas(risk));
        lblRiskAuditPercentValue.setText(RiskDisplayHelper.getTranslatedRiskAuditPercent(risk));
        lblRiskWastePercentValue.setText(RiskDisplayHelper.getTranslatedRiskWastePercent(risk));
    }
    private void updateRulesValues(CrgRules pRule) {
        if (pRule == null) {
            return;
        }
        long startTotal = System.currentTimeMillis();
        lblNumberValue.setText(pRule.getCrgrNumber());
        lblNameValue.setText(pRule.getCrgrCaption());
        Platform.runLater(() -> {
            lblTypeValue.setText(pRule.getCrgrRuleErrorType() != null ? Lang.get(pRule.getCrgrRuleErrorType().getLangKey()).getValue() : "");

            switch ((pRule.getCrgrRuleErrorType() != null) ? pRule.getCrgrRuleErrorType() : RuleTypeEn.STATE_NO) {
                case STATE_ERROR:
                    lblTypeValue.setStyle("-fx-text-fill:red;");
                    break;
                case STATE_WARNING:
                    lblTypeValue.setStyle("-fx-text-fill:gold;");
                    break;
                case STATE_SUGG:
                    lblTypeValue.setStyle("-fx-text-fill:skyblue;");
                    break;
                case STATE_NO:
                    lblTypeValue.setStyle("-fx-text-fill:black;");
                    break;
                default:
                    lblTypeValue.setStyle("-fx-text-fill:black;");
            }
        });
        lblCategoryValue.setText(pRule.getCrgrCategory());
        OverrunHelper.addInfoTooltip(lblCategoryValue);
        lblRolesValue.setText(pRule.getCrgRule2Roles().stream().map(item -> item.getCdbUserRoles().getCdburName()).collect(Collectors.joining(",")));
        lblFromValue.setText(Lang.toDate(pRule.getCrgrValidFrom()));
        lblToValue.setText(Lang.toDate(pRule.getCrgrValidTo()));
        lblNoticeValue.setText(pRule.getCrgrNote());
        lblRuleTypeValue.setText(pRule.getCrgRuleTypes().getCrgtShortText());
        lblRuleTypeValue.setTooltip(new CpxTooltip(pRule.getCrgRuleTypes().getCrgtDisplayText(), 0, 5000, 0, true));
        
        rulePane.reload();
//        ruleStructurePane.reload();
        suggestionPane.reload();
//                ruleView.setRule(ruleXml);
//                structureView.setRule(ruleXml);
//                suggestionView.setRule(ruleXml);

        lblSuggTextValue.setText(pRule.getCrgrSuggText());
        lblSuggTextValue.setTooltip(new BasicTooltip("Beschreibung", lblSuggTextValue.getText()));
        LOG.finer("total time to update ruleValues " + (System.currentTimeMillis() - startTotal));
    }

    public final void updateUi() throws IOException {
        getChildren().clear();
        getChildren().add(fetchLayout());
        updateRulesValues(getSkinnable().getRule());
    }
}
