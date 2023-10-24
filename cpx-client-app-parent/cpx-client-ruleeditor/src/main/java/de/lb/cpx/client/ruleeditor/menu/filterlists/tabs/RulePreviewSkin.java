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
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.ruleeditor.menu.filterlists.tabs;

import de.lb.cpx.client.core.model.fx.pane.AsyncPane;
import de.lb.cpx.client.core.model.fx.section.SectionHeader;
import de.lb.cpx.client.core.model.fx.tooltip.BasicTooltip;
import de.lb.cpx.client.core.model.fx.tooltip.CpxTooltip;
import de.lb.cpx.client.core.util.OverrunHelper;
import de.lb.cpx.model.enums.RuleTypeEn;
import de.lb.cpx.rule.criteria.TypesAndOperationsManager;
import de.lb.cpx.rule.criteria.model.TypesAndOperations;
import de.lb.cpx.rule.element.CaseRuleManager;
import de.lb.cpx.rule.element.model.Risk;
import de.lb.cpx.rule.element.model.Rule;
import de.lb.cpx.ruleviewer.layout.RuleView;
import de.lb.cpx.ruleviewer.layout.SuggestionView;
import de.lb.cpx.ruleviewer.model.RuleMessageIndicator;
import de.lb.cpx.ruleviewer.util.RiskDisplayHelper;
import de.lb.cpx.server.commonDB.model.CdbUserRoles;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.shared.json.RuleMessageReader;
import de.lb.cpx.shared.lang.Lang;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
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
import javafx.scene.control.Tooltip;
import javafx.scene.control.skin.ScrollPaneSkin;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * Skin View for RulePreview
 *
 * @author wilde
 */
public class RulePreviewSkin extends SkinBase<RulePreview> {

    private static final Logger LOG = Logger.getLogger(RulePreviewSkin.class.getName());

    private Label lblNumberValue;
    private Label lblNameValue;
    private Label lblTypeValue;
    private Label lblCategoryValue;
    private Label lblRolesValue;
    private Label lblFromValue;
    private Label lblToValue;
    private Label lblNoticeValue;
    private AsyncPane<RuleView> rulePane;
    private Label lblSuggTextValue;
    private AsyncPane<SuggestionView> suggestionPane;
    private Label lblRuleTypeValue;
    private Label lblFeeGroupValue;

    protected static final String FEE_GROUP = "hos_normal_fee";
    private Label lblRiskWaistValue;
    private Label lblRiskAreasValue;
    private Label lblRiskAuditPercentValue;
    private Label lblRiskWastePercentValue;
    private SectionHeader shTitle;

    public RulePreviewSkin(RulePreview pSkinnable) throws IOException {
        super(pSkinnable);
        getChildren().add(createRoot());
        pSkinnable.ruleProperty().addListener(new ChangeListener<CrgRules>() {
            @Override
            public void changed(ObservableValue<? extends CrgRules> ov, CrgRules t, CrgRules t1) {
                updateRuleData(t1);
            }
        });
        updateRuleData(pSkinnable.getRule());
    }

    private void updateRuleData(CrgRules pRule) {
        if (pRule == null) {
            return;
        }
        long startTotal = System.currentTimeMillis();
        
        if(pRule.getCrgrMessage()!=null){
            RuleMessageIndicator indicator = new RuleMessageIndicator();
            String msg;
            try {
                msg = new RuleMessageReader().read(pRule.getCrgrMessage(), "UTF-8").stream().map((t) -> {
                    return t.getDescription();
                }).collect(Collectors.joining("\n"));
            } catch (IOException ex) {
                Logger.getLogger(RulePreviewSkin.class.getName()).log(Level.SEVERE, null, ex);
                msg = new StringBuilder("Reading of JSON failed! Reason:\n").append(ex.getMessage()).toString();
            }
            indicator.setTooltip(new CpxTooltip(msg, 100, 5000, 200, true));
            shTitle.setTitleGraphic(indicator);
        }
        
        lblNumberValue.setText(pRule.getCrgrNumber());
        lblNameValue.setText(pRule.getCrgrCaption());
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
        lblCategoryValue.setText(pRule.getCrgrCategory());
        lblRolesValue.setText(getUserRoles(pRule).stream().map(item -> item.getCdburName()).collect(Collectors.joining(", ")));
        OverrunHelper.addInfoTooltip(lblRolesValue);
        lblFromValue.setText(Lang.toDate(pRule.getCrgrValidFrom()));
        lblToValue.setText(Lang.toDate(pRule.getCrgrValidTo()));
        lblRuleTypeValue.setText(getSkinnable().getRuleTypeName(pRule));
        lblRuleTypeValue.setTooltip(new Tooltip(getSkinnable().getRuleTypeDisplayName(pRule)));
        lblNoticeValue.setText(CaseRuleManager.getDisplayText(pRule.getCrgrNote()));
        lblFeeGroupValue.setText(getFeeGroups(pRule));
        rulePane.reload();
        suggestionPane.reload();
//
        lblSuggTextValue.setText(pRule.getCrgrSuggText());
        lblSuggTextValue.setTooltip(new BasicTooltip("Beschreibung", lblSuggTextValue.getText()));

        LOG.info("total time to update ruleValues " + (System.currentTimeMillis() - startTotal));
    }

    private final Parent createRoot() throws IOException {
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/RulePreviewFXML.fxml"));

        Label lblNumber = (Label) root.lookup("#lblNumber");
        lblNumber.setText("Nummer:");
        Label lblName = (Label) root.lookup("#lblName");
        lblName.setText("Bezeichnung:");
        Label lblType = (Label) root.lookup("#lblType");
        lblType.setText("Fehlerart:");
        Label lblCategory = (Label) root.lookup("#lblCategory");
        lblCategory.setText("Kategorie:");
        Label lblRoles = (Label) root.lookup("#lblRoles");
        lblRoles.setText("Rollen:");
        Label lblFrom = (Label) root.lookup("#lblFrom");
        lblFrom.setText("von:");
        Label lblTo = (Label) root.lookup("#lblTo");
        lblTo.setText("bis:");
        Label lblRuleType = (Label) root.lookup("#lblRuleType");
        lblRuleType.setText("Regeltyp:");
        Label lblFeeGroup = (Label) root.lookup("#lblFeeGroup");
        lblFeeGroup.setText("Entgeltgruppe:");
        
//        ColumnConstraints cons = new ColumnConstraints(GridPane.USE_PREF_SIZE, GridPane.USE_COMPUTED_SIZE, GridPane.USE_PREF_SIZE, Priority.SOMETIMES, HPos.LEFT, true);
        
        
        Label lblRiskAuditPercent = (Label) root.lookup("#lblRiskAuditPercent");
        lblRiskAuditPercent.setText(new StringBuilder(Lang.getRiskEditorRiskAuditValue()).append(":").toString());
        Label lblRiskWastePercent = (Label) root.lookup("#lblRiskWastePercent");
        lblRiskWastePercent.setText(new StringBuilder(Lang.getRiskEditorRiskWasteValue()).append(":").toString()); 
        Label lblRiskAreas = (Label) root.lookup("#lblRiskAreas");
        lblRiskAreas.setText(new StringBuilder(Lang.getRiskArea()).append(":").toString());
        Label lblRiskWaist = (Label) root.lookup("#lblRiskWaist");
        lblRiskWaist.setText(new StringBuilder(Lang.getRiskEditorRiskWasteDefaultValue()).append(":").toString()); 

        shTitle = (SectionHeader) root.lookup("#shTitle");
        //editable values
        lblNumberValue = (Label) root.lookup("#lblNumberValue");
        lblNameValue = (Label) root.lookup("#lblNameValue");
        lblTypeValue = (Label) root.lookup("#lblTypeValue");
        lblCategoryValue = (Label) root.lookup("#lblCategoryValue");
        lblRolesValue = (Label) root.lookup("#lblRolesValue");
        lblFromValue = (Label) root.lookup("#lblFromValue");
        lblToValue = (Label) root.lookup("#lblToValue");
        lblFeeGroupValue = (Label) root.lookup("#lblFeeGroupValue");
        lblRuleTypeValue = (Label) root.lookup("#lblRuleTypeValue");
        ScrollPane spNotice = (ScrollPane) root.lookup("#spNotice");
        spNotice.setSkin(new ScrollPaneSkin(spNotice));
        lblNoticeValue = (Label) root.lookup("#lblNoticeValue");
        lblRiskAuditPercentValue = (Label) root.lookup("#lblRiskAuditPercentValue");
        lblRiskWastePercentValue = (Label) root.lookup("#lblRiskWastePercentValue");
        lblRiskAreasValue = (Label) root.lookup("#lblRiskAreasValue");
        lblRiskWaistValue = (Label) root.lookup("#lblRiskWaistValue");

        rulePane = new AsyncPane<>() {
            @Override
            public RuleView loadContent() {
                long start = System.currentTimeMillis();
                //loads new rule view instance!
                //set font and layout variables
                RuleView ruleView = new RuleView(getSkinnable().getPool(), getSkinnable().getRule());
                ruleView.setFont(Font.font("Courier New", 15));
                ruleView.setMaxHeight(Double.MAX_VALUE);
                //fetch rule from defintion
                long parse = System.currentTimeMillis();
                byte[] definition = getRuleDefinition(getSkinnable().getRule());
                if (definition != null) {
                    Rule ruleXml = CaseRuleManager.transformRuleInUTF16(definition);//getSkinnable().getRule().getCrgrDefinition());
                    ruleView.setRule(ruleXml);
                    updateRiskValues(ruleXml.getRisks());
                    long startRuleMessage = System.currentTimeMillis();
                    ruleView.setRuleMessages(new RuleMessageReader().read(getSkinnable().getRule()));
                    LOG.log(Level.INFO, "Set RuleMessage in {0} ms for ruleView", System.currentTimeMillis()-startRuleMessage);
                }
//                Rule ruleXml = getSkinnable().getRule().getCrgrDefinition()!=null?CaseRuleManager.transformRuleInUTF16(getSkinnable().getRule().getCrgrDefinition()):null;
//                LOG.finer("parse xml in " + (System.currentTimeMillis() - parse));
//                ruleView.setCrgRules(getSkinnable().getRule());
//                ruleView.setRule(ruleXml);
                LOG.finer("load ruleview in " + (System.currentTimeMillis() - start));
//                ruleView.setRuleMessage("Prozedur @ AOP_2021 - Testtooltip");
                return ruleView;
            }

        };
        VBox boxRule = (VBox) root.lookup("#boxRule");
        VBox.setVgrow(rulePane, Priority.ALWAYS);
        boxRule.getChildren().add(rulePane);

        Label lblSuggText = (Label) root.lookup("#lblSuggText");
        lblSuggText.setText("Beschreibung:");
        lblSuggTextValue = (Label) root.lookup("#lblSuggTextValue");
        VBox boxSuggestion = (VBox) root.lookup("#boxSuggestion");
        suggestionPane = new AsyncPane<>() {
            @Override
            public SuggestionView loadContent() {
                SuggestionView suggestionView = new SuggestionView();
                suggestionView.setFont(Font.font("Courier New", 15));
                suggestionView.setPadding(new Insets(0, 0, 0, 6));
                byte[] definition = getRuleDefinition(getSkinnable().getRule());
                if (definition != null) {
                    Rule ruleXml = CaseRuleManager.transformRuleInUTF16(definition);//getSkinnable().getRule().getCrgrDefinition());
                    suggestionView.setRule(ruleXml);
                }
                long startRuleMessage = System.currentTimeMillis();
                suggestionView.setRuleMessages(new RuleMessageReader().read(getSkinnable().getRule()));
                LOG.log(Level.INFO, "Set RuleMessage in {0} ms for suggestionView", System.currentTimeMillis()-startRuleMessage);
                return suggestionView;
            }
        };
        VBox.setVgrow(suggestionPane, Priority.ALWAYS);
        boxSuggestion.getChildren().add(suggestionPane);

        return root;
    }
    private void updateRiskValues(Risk risk) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
//                if (risk == null || risk.getRiskAreas().isEmpty()) {
//                    lblRiskAreasValue.setText("");
//                    lblRiskPercentValue.setText("");
//                    lblRiskWaistValue.setText("");
//                    return;
//                }
                lblRiskAreasValue.setText(RiskDisplayHelper.getTranslatedRiskAreas(risk));
                lblRiskAuditPercentValue.setText(RiskDisplayHelper.getTranslatedRiskAuditPercent(risk));
                lblRiskWastePercentValue.setText(RiskDisplayHelper.getTranslatedRiskWastePercent(risk));
                lblRiskWaistValue.setText(RiskDisplayHelper.getTranslatedRiskDefaultWaste(risk));
            }
        });
    }
    //make loading blocking task, so that server is not asked multiple times for definition
    //see ruleeditor view implementation in ruleviewer
    private byte[] getRuleDefinition(CrgRules pRule) {
        if (pRule.getCrgrDefinition() != null) {
            return pRule.getCrgrDefinition();
        }
        return getSkinnable().getRuleDefinition(pRule);
    }

    private List<CdbUserRoles> getUserRoles(CrgRules pRule) {
        return getSkinnable().getUserRoles4Rule(pRule);
    }

    private String getFeeGroups(CrgRules pRule) {
        if (pRule == null) {
            return "";
        }
        String feeGroups = Objects.requireNonNullElse(pRule.getCrgrFeeGroup(), "");
        return List.of(feeGroups.split(",")).stream().map((t) -> {
            TypesAndOperations.FeeGroups.FeeGroup.FeeType fee = TypesAndOperationsManager.instance().getFeeType(FEE_GROUP, t);
            if (fee == null) {
                return "";
            }
            return fee.getName();
        }).collect(Collectors.joining(", "));

    }
}
