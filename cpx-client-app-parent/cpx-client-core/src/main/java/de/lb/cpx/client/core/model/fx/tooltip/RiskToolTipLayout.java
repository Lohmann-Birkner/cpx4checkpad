/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.tooltip;

import de.lb.cpx.shared.dto.rules.CpxSimpleRisk;
import de.lb.cpx.shared.dto.rules.CpxSimpleRuleDTO;
import de.lb.cpx.shared.lang.Lang;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 *
 * @author gerschmann
 */
public class RiskToolTipLayout extends BorderPane {

    private static final Logger LOG = Logger.getLogger(RiskToolTipLayout.class.getName());
    GridPane layout;
    Label noRule;

    public RiskToolTipLayout() {
        super();
        createLayout();
        createNoRuleLayout();
    }

    private Parent createLayout() {
        try {
            layout = FXMLLoader.load(getClass().getResource("/fxml/RiskToolTip.fxml"));
            Label lbl1 = (Label) layout.lookup("#RuleResultId");
            lbl1.setText(Lang.getRuleResult());
            Label lbl2 = (Label) layout.lookup("#RiskAreaId");
            lbl2.setText(Lang.getRiskArea());
            Label lbl3 = (Label) layout.lookup("#dRevenueId");
            lbl3.setText(Lang.getRuleDeltaRevenue());
            Label lbl4 = (Label) layout.lookup("#AuditRiskId");
            lbl4.setText(Lang.getRuleEstimated() + " " + Lang.getRiskEditorRiskAuditValue());
            Label lbl5 = (Label) layout.lookup("#WasteId");
            lbl5.setText(Lang.getRuleEstimated() + " " + Lang.getRiskEditorRiskWasteValue());
            Label lbl6 = (Label) layout.lookup("#CalculatedLineId");
            lbl6.setText(Lang.getRuleCalculation());

            Label lbl7 = (Label) layout.lookup("#WasteSumId");
            lbl7.setText(Lang.getRuleEstimated() + " " + Lang.getRiskEditorRiskWasteValue());
            Label lbl8 = (Label) layout.lookup("#RiskId");
            lbl8.setText(Lang.getRuleEstimated() + " " + Lang.getIcdTypeRisk());

            return layout;
        } catch (IOException ex) {

            LOG.log(Level.SEVERE, "could not find resource", ex);
        }
        return new Pane();
    }

    void setValues(CpxSimpleRuleDTO rule, boolean is4billing) {
        if (rule == null || rule.getRisks() == null) {
            this.getChildren().remove(layout);
            this.setCenter(noRule);
        } else {
            this.getChildren().remove(noRule);
            fillLayoutFromRule(rule, is4billing);
            this.setCenter(layout);
        }

    }

    private void createNoRuleLayout() {
        noRule = new Label("Die Regel konnte nicht ermittelt werden\noder bei der Regel wurde kein Risikowert eingestellt");
    }

    private void fillLayoutFromRule(CpxSimpleRuleDTO rule, boolean is4billing) {
        if (layout == null) {
            createLayout();
        }
        CpxSimpleRisk risk = rule.getRisks();
        Label lbl1 = (Label) layout.lookup("#RuleNameId");
        lbl1.setText(rule.getRuleNumber());
        Label lbl2 = (Label) layout.lookup("#RiskAreaValueId");
        lbl2.setText(risk == null ? "" : risk.getRiskAreaTranslation());
        Label lbl3 = (Label) layout.lookup("#dRevenueValueId");
        String res = Lang.toDecimal(rule.getChkFeeSimulDiff(), 2) + Lang.getCurrencySymbol();
        lbl3.setText(res);
        Label lbl4 = (Label) layout.lookup("#AuditRiskValueId");
        lbl4.setText(risk == null ? "0" : risk.getRiskAuditPercentValue() + "%");
        Label lbl5 = (Label) layout.lookup("#WasteValueId");
        lbl5.setText(risk == null ? "0" : risk.getRiskWastePercentValue() + "%");
        Label lbl6 = (Label) layout.lookup("#CalcLineValueId");
        lbl6.setText(risk == null ? "" : risk.getComputeString(rule.getChkFeeSimulDiff(), is4billing));

        Label lbl7 = (Label) layout.lookup("#WasteSumValueId");
        lbl7.setText(risk == null ? "0" : risk.getForBillingComputeValue(rule.getChkFeeSimulDiff(), is4billing));
        Label lbl8 = (Label) layout.lookup("#RiskValueId");
        String computedBillingValue = risk!=null?risk.getComputedForBillingValue():"0";
        String computedWasteValue = risk!=null?risk.getComputedWasteValue():"0";
        lbl8.setText(is4billing ? computedBillingValue: computedWasteValue);
    }

}
