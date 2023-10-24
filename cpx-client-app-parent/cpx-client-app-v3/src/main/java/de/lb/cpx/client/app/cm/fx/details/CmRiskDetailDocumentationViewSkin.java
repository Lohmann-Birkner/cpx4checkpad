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
package de.lb.cpx.client.app.cm.fx.details;

import de.lb.cpx.client.core.model.fx.labeled.LabeledTextArea;
import de.lb.cpx.model.enums.VersionRiskTypeEn;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmRiskDetails;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SkinBase;
import javafx.scene.control.skin.ScrollPaneSkin;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author wilde
 */
public class CmRiskDetailDocumentationViewSkin extends SkinBase<CmRiskDetailDocumentationView>{

    private static final Logger LOG = Logger.getLogger(CmRiskDetailDocumentationViewSkin.class.getName());
    
    private RiskSummary riskSummary;
    private LabeledTextArea riskComment;
    private ScrollPane spContent;
    public CmRiskDetailDocumentationViewSkin(CmRiskDetailDocumentationView pSkinnable) throws IOException {
        super(pSkinnable);
        getChildren().add(createRoot());
    }
    
    private Parent createRoot() throws IOException{
        AnchorPane layout = FXMLLoader.load(getClass().getResource("/fxml/CmRiskDetailDocumentationView.fxml"));
        spContent  = (ScrollPane) layout.lookup("#spContent");
        spContent.setSkin(new ScrollPaneSkin(spContent));
        riskSummary = (RiskSummary) layout.lookup("#selectedRiskSummaryId");
//        riskSummary.enableFields2VersionType(getSkinnable().getVersionType());

        riskSummary.changeCalculatedValuesProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if (t1) {
                    riskSummary.changeCalculatedValues(false);
                    getSkinnable().getRiskDetails().setRiskAuditPercent(riskSummary.getAuditPercentValue());
                    getSkinnable().getRiskDetails().setRiskWastePercent(riskSummary.getWastePercentValue());
                    getSkinnable().getRiskDetails().setRiskNotCalculatedFee(BigDecimal.valueOf(riskSummary.getNotCalculatedFee()));

                    riskSummary.setWasteValue(getSkinnable().calculateWaste4RiskDetails(getSkinnable().getRiskDetails()));
                    riskSummary.setCalcWasteValues(getSkinnable().getRiskDetails().getRiskCalcPercent(), getSkinnable().getRiskDetails().getRiskPercent());
                    updateRiskDetail(getSkinnable().getRiskDetails());
                }
            }
        });

//        riskSummary.changeWasteValueProperty().addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
//                if (t1) {
//                    riskSummary.changeWasteValue(false);
//                    getSkinnable().getRiskDetails().setRiskValue(BigDecimal.valueOf((riskSummary.getWasteValue())));
//                    updateRiskDetail(getSkinnable().getRiskDetails());
//                }
//            }
//        });
        riskSummary.changeAuditFlagProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {

                    if(getSkinnable().getRiskDetails().getRiskUsedForAuditFl() != riskSummary.changeAuditFlag()){
                        getSkinnable().getRiskDetails().setRiskUsedForAuditFl(riskSummary.changeAuditFlag());
                        updateRiskDetail(getSkinnable().getRiskDetails());
                    
                }
            }
        });
        riskSummary.changeFinalFlagProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
               
                    if(getSkinnable().getRiskDetails().getRiskUsedForFinalFl() != riskSummary.changeFinalFlag()){
                       getSkinnable().getRiskDetails().setRiskUsedForFinalFl(riskSummary.changeFinalFlag());
                       updateRiskDetail(getSkinnable().getRiskDetails());
                    
                }
            }
        });

        riskComment = (LabeledTextArea) layout.lookup("#riskCommentId");
        riskComment.setTitle(Lang.getComment());
        riskComment.setMaxSize(LabeledTextArea.RISK_COMMENT_SIZE);
//        riskComment.setText(getSkinnable().getRiskDetails() == null || getSkinnable().getRiskDetails().getRiskComment() == null ? "" : getSkinnable().getRiskDetails().getRiskComment());
//        fullRiskSummary.getControl().prefWidthProperty().bind(txtFullRiskComment.widthProperty());
//        fillFullRiskSummary();
        riskComment.getControl().focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    //focus lost -> store value
//                    getSkinnable().getOnUpdateFullRiskComment().call(txtFullRiskComment.getText());
                    getSkinnable().getRiskDetails().setRiskComment(riskComment.getText());
                    updateRiskDetail(getSkinnable().getRiskDetails());
                }
            }
        });
        getSkinnable().riskDetailsProperty().addListener(new ChangeListener<TWmRiskDetails>() {
            @Override
            public void changed(ObservableValue<? extends TWmRiskDetails> ov, TWmRiskDetails oldVal, TWmRiskDetails newVal) {
//                if (newVal != null) {
//                    if(!getSkinnable().getUpdateRiskDetail().call(newVal)){
//                        LOG.severe("Update on riskDetail changed failed!");
//                        return;
//                    }
//                    LOG.finest("update on riskDetail!");
//                }
                handleRiskDetailsChange(newVal);
            }
        });
        
        getSkinnable().readonlyContent().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean oldVal, Boolean newVal) {
                riskSummary.adjustLayout(newVal);
            }
        });
        handleRiskDetailsChange(getSkinnable().getRiskDetails());
        riskSummary.adjustLayout(getSkinnable().getReadonlyContent());
        if(!getSkinnable().getReadonlyContent()){
            showVersionBoxes();
//        riskComment.prefHeightProperty().bind(riskSummary.getControl().heightProperty());
//            riskComment.getControl().prefHeightProperty().bind(riskSummary.getCheckFinal().heightProperty().multiply(7));
       }else{
            riskSummary.removeCheckBoxes();
//            riskComment.getControl().prefHeightProperty().bind(riskSummary.getCheckFinal().heightProperty().multiply(6));
        riskComment.prefHeightProperty().bind(riskSummary.getControl().heightProperty()
                .subtract(riskSummary.getCheckFinal().heightProperty())
                .subtract(riskSummary.getCheckFinal().heightProperty())
                .subtract(riskSummary.getCheckFinal().heightProperty())
        );        
        }

        riskComment.getControl().setEditable(!getSkinnable().getReadonlyContent());
         if(getSkinnable().getReadonlyContent()){
              riskComment.getControl().getStyleClass().add("text-area-disabled-background");
//              riskComment.getControl().setStyle("-fx-background-color: grey;");
        }
        return layout;
    }
    private void updateRiskDetail(TWmRiskDetails pDetail){
        if(!getSkinnable().getUpdateRiskDetail().call(pDetail)){
            LOG.severe("updating RiskDetail failed!");
        }
    }

    private void handleRiskDetailsChange(TWmRiskDetails newVal) {
        if (newVal == null) {
            LOG.finer("RiskDetails is null, can not update view!");
            return;
        }
        riskComment.setText(newVal.getRiskComment() == null ? "" : newVal.getRiskComment());
        //        if (newVal != null) {
        riskSummary.setValues(
                newVal.getRiskAuditPercentSugg(),
                newVal.getRiskAuditPercent(),
                newVal.getRiskWastePercentSugg(),
                newVal.getRiskWastePercent(),
                newVal.getRiskCalcValue() == null ? 0 : newVal.getRiskCalcValue().doubleValue(),
                newVal.getRiskValue() == null ? 0 : newVal.getRiskValue().doubleValue(),
                newVal.getRiskCalcPercent(),
                newVal.getRiskPercent() );
        riskSummary.setChecks(newVal.getRiskUsedForAuditFl(), newVal.getRiskUsedForFinalFl());
        riskSummary.setDFee(newVal.getRiskBaseFee() == null ? 0 : newVal.getRiskBaseFee().doubleValue());
        riskSummary.setNotCalculatedFee(newVal.getRiskNotCalculatedFee()== null ? 0 : newVal.getRiskNotCalculatedFee().doubleValue());
        riskSummary.changeCalculatedValues(false);
//        riskSummary.changeWasteValue(false);

//        addToolTips2RiskDetailsFields(getSkinnable().getRule2area(newVal.getRiskArea()));
    }

    private void showVersionBoxes() {
        VersionRiskTypeEn versType = getSkinnable().getVersionType();

        if(versType == null ){
            riskSummary.setVisibleCheckBoxes(false);
        }else{
            riskSummary.enableFields2VersionType(versType);
            if(versType.equals(VersionRiskTypeEn.CASE_FINALISATION)){
                riskSummary.setVisibleCheckBoxes(true);
            }else{
                if(versType.equals(VersionRiskTypeEn.AUDIT_MD) || versType.equals(VersionRiskTypeEn.AUDIT_CASE_DIALOG)){
                    riskSummary.setVisibleAuditCheck(true);
                }else{
                    riskSummary.setVisibleCheckBoxes(false);
                }
            }
        }
    }
    

}
