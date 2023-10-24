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
 *    2020  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.process.completion.risk;

import de.lb.cpx.client.core.model.fx.button.AddButton;
import de.lb.cpx.server.commonDB.model.enums.RiskAreaEn;
import de.lb.cpx.wm.model.TWmFinalisationRisk;
import de.lb.cpx.wm.model.TWmFinalisationRiskDetail;
import java.math.BigDecimal;
import java.util.Objects;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 *
 * @author wilde
 */
public class CompletionRiskCell extends RiskListCell<TWmFinalisationRiskDetail> {

    private final TWmFinalisationRisk risk;

    public CompletionRiskCell(TWmFinalisationRisk pRisk) {
        risk = pRisk;
    }

    @Override
    protected Node createLayout(TWmFinalisationRiskDetail pDetails) {
        if (pDetails == null) {
            HBox content = new HBox(5);
            content.setMinHeight(55.0);
            content.setPrefHeight(55.0);
            content.setMaxHeight(55.0);
            content.setAlignment(Pos.CENTER);
            if (isEditable()) {
                AddButton addButton = new AddButton();
                addButton.getGraphic().setStyle("-fx-text-fill:-root03;");
                addButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        addRiskDetails();
                    }
                });
                Label label = new Label("Prüfgrund hinzufügen");
                content.getChildren().addAll(addButton, label);
                content.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (MouseButton.PRIMARY.equals(event.getButton())) {
                            addRiskDetails();
                        }
                    }
                });
                content.getStyleClass().add("hover-hand");
            } else {
                Label label = new Label("Keine Angabe");
                content.getChildren().add(label);
            }
            Label title = new Label(getItem().getTranslation().getValue());
            VBox box = new VBox(5, title, content);
            box.setAlignment(Pos.TOP_LEFT);
            box.maxWidthProperty().bind(getListView().widthProperty().subtract(20));
            return box;
        }
        return super.createLayout(pDetails);
    }

    private void addRiskDetails() {
        TWmFinalisationRiskDetail detail = new TWmFinalisationRiskDetail();
        detail.setRiskArea(getItem());
        detail.setRiskComment("");
//        detail.setRiskPercent(0);
        detail.setRiskValue(getRiskDefaultValueCallback().call(getItem()));
        getAddCallback().call(detail);
    }
//    public abstract TWmRiskDetails getDetailForArea(RiskAreaEn pArea);
    @Override
    public TWmFinalisationRiskDetail getDetailForArea(RiskAreaEn pArea) {
        if (risk == null) {
            return null;
        }
        if (pArea == null) {
            return null;
        }
        for (TWmFinalisationRiskDetail detail : risk.getFinalisationRiskDetails()) {
            if (pArea.equals(detail.getRiskArea())) {
                return detail;
            }
        }
        return null;
    }

    @Override
    public Integer getRiskPercent(TWmFinalisationRiskDetail pDetails) {
//        return pDetails!=null?pDetails.getRiskPercent():null;
        return null;
    }

    @Override
    public void setRiskPercent(TWmFinalisationRiskDetail pDetails, int intValue) {
//        if(pDetails==null){
//            return;
//        }
//        pDetails.setRiskPercent(intValue);
    }

    @Override
    public BigDecimal getRiskValue(TWmFinalisationRiskDetail pDetails) {
        return pDetails!=null?pDetails.getRiskValue():null;
    }

    @Override
    public void setRiskValue(TWmFinalisationRiskDetail pDetails, BigDecimal valueOf) {
        if(pDetails==null){
            return;
        }
        pDetails.setRiskValue(valueOf);
    }

//    @Override
//    public String getRiskAreaName(TWmRiskDetails pDetails) {
//        
//    }

    @Override
    public String getRiskComment(TWmFinalisationRiskDetail pDetails) {
         return pDetails!=null?pDetails.getRiskComment():null;
    }
    private static final Callback<RiskAreaEn, BigDecimal> DEFAULT_RISK_VALUE_CALLBACK = new Callback<RiskAreaEn, BigDecimal>() {
        @Override
        public BigDecimal call(RiskAreaEn param) {
            return BigDecimal.ZERO;
        }
    };
    private Callback<RiskAreaEn, BigDecimal> riskValueCallback = DEFAULT_RISK_VALUE_CALLBACK;
    public Callback<RiskAreaEn, BigDecimal> getRiskDefaultValueCallback() {
        return riskValueCallback;
    }
    
    public void setRiskDefaultValueCallback(Callback<RiskAreaEn, BigDecimal> callback) {
        riskValueCallback = Objects.requireNonNullElse(callback, DEFAULT_RISK_VALUE_CALLBACK);
    }
}
