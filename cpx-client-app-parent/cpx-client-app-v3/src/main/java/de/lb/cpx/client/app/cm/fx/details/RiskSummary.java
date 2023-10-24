/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.app.cm.fx.details;

import de.lb.cpx.client.core.model.fx.textfield.CurrencyTextField;
import de.lb.cpx.client.core.model.fx.textfield.PercentTextField;
import de.lb.cpx.model.enums.VersionRiskTypeEn;
import de.lb.cpx.shared.lang.Lang;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

/**
 *
 * @author gerschmann
 */
public class RiskSummary extends BorderPane {

    private static final Logger LOG = Logger.getLogger(RiskSummary.class.getName());

    private GridPane grid;
    private Label auditPercentSugg;
    private Label wastePercentSugg;
    private Label wasteValueSugg;
    private Label calcWastePercentSugg;
    private Label dFee;
    private PercentTextField auditRiskPercent;
    private PercentTextField wasteRiskPercent;
//    private CurrencyTextField wasteValue;
    private Label wasteValue;
    private CurrencyTextField dFeeNotCalculated;
    private Label calcWastePercent;
    private Label lblChkAudit;
    private Label lblChkFinal;
    private CheckBox chkAudit;
    private CheckBox chkFinal;
    private Label lblWasteFullPercent;
    private Label lblAuditFullPercent;

    public RiskSummary() {
        super();
        createLayout();
    }

    public final Parent createLayout() {
        try {
            grid = FXMLLoader.load(getClass().getResource("/fxml/RiskSummaryFXML.fxml"));
            this.setCenter(grid);
            Label lblDFee = (Label) grid.lookup("#lblDFeeId");
            // text from Lang
            Label lblFullAudit = (Label) grid.lookup("#lblFullAuditId");
            //text from Lang
            Label lblFullWaste = (Label) grid.lookup("#lblFullWasteId");
            //text from lang
            Label lblFullWasteValue = (Label) grid.lookup("#lblFullWasteValueId");
            // text from Lang

            auditPercentSugg = (Label) grid.lookup("#fullAuditPercentSuggId");
            wastePercentSugg = (Label) grid.lookup("#fullWastePercentSuggId");
            wasteValueSugg = (Label) grid.lookup("#fullWasteValueSuggId");
            calcWastePercentSugg = (Label) grid.lookup("#calcPercentSuggId");

            dFee = (Label) grid.lookup("#dFeeId");
            auditRiskPercent = (PercentTextField) grid.lookup("#fullAuditRiskPercentId");
            wasteRiskPercent = (PercentTextField) grid.lookup("#fullWasteRiskPercentId");
            wasteValue = (Label) grid.lookup(("#fullWasteValueId"));
            calcWastePercent = (Label) grid.lookup("#calcPercentId");
            dFeeNotCalculated  = (CurrencyTextField)grid.lookup("#dFeeNotCalculatedId");

            auditPercentSugg.prefHeightProperty().bind(auditRiskPercent.heightProperty());
            auditPercentSugg.prefWidthProperty().bind(auditRiskPercent.widthProperty());
            wastePercentSugg.prefHeightProperty().bind(wasteRiskPercent.heightProperty());
            wastePercentSugg.prefWidthProperty().bind(wasteRiskPercent.widthProperty());
            wasteValue.prefHeightProperty().bind(wasteRiskPercent.heightProperty());
            wasteValue.prefWidthProperty().bind(wasteRiskPercent.widthProperty());
            wasteValueSugg.prefHeightProperty().bind(wasteValue.heightProperty());
            wasteValueSugg.prefWidthProperty().bind(wasteValue.widthProperty());
            calcWastePercent.prefWidthProperty().bind(wasteRiskPercent.widthProperty());
            calcWastePercent.prefHeightProperty().bind(wasteRiskPercent.heightProperty());

            calcWastePercentSugg.prefWidthProperty().bind(calcWastePercent.widthProperty());
            calcWastePercentSugg.prefHeightProperty().bind(calcWastePercent.heightProperty());
            dFee.prefWidthProperty().bind(calcWastePercent.widthProperty());
            dFee.prefHeightProperty().bind(calcWastePercent.heightProperty());

            lblWasteFullPercent = (Label)grid.lookup("#lblWasteFullPercentId");
            lblAuditFullPercent = (Label)grid.lookup("#lblAuditFullPercentId");
            lblWasteFullPercent.setVisible(false);
            lblAuditFullPercent.setVisible(false);
            lblAuditFullPercent.prefHeightProperty().bind(auditRiskPercent.heightProperty());
            lblAuditFullPercent.prefWidthProperty().bind(auditRiskPercent.widthProperty());
            lblWasteFullPercent.prefHeightProperty().bind(wasteRiskPercent.heightProperty());
            lblWasteFullPercent.prefWidthProperty().bind(wasteRiskPercent.widthProperty());
            auditRiskPercent.setAdditionalFunctionCallback(new Callback<Void, Void>(){
                @Override
                public Void call(Void p) {
                    changeCalculatedValues(true);
                    return null;
                }
                
            });

            wasteRiskPercent.setAdditionalFunctionCallback(new Callback<Void, Void>(){
                @Override
                public Void call(Void p) {
                    changeCalculatedValues(true);
                    return null;
                }
                
            });
            
            auditRiskPercent.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (!newValue) {
                        //focus lost -> store value
                        changeCalculatedValues(true);
                    }
                }
            });

            wasteRiskPercent.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (!newValue) {
                        //focus lost -> store value
                        //calculate
                        changeCalculatedValues(true);
                    }
                }
            });

//            wasteValue.focusedProperty().addListener(new ChangeListener<Boolean>() {
//                @Override
//                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                    if (!newValue) {
//                        //focus lost -> store value
//                        //calculate
//                        changeWasteValue(true);
//                    }
//                }
//            });
            dFeeNotCalculated.setAdditionalFunctionCallback(new Callback<Void, Void>(){
                @Override
                public Void call(Void p) {
                    changeCalculatedValues(true);
                    return null;
                }
                
            });
            
            dFeeNotCalculated.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (!newValue) {
                        //focus lost -> store value
                        changeCalculatedValues(true);
                    }
                }
            });

            // add checkBoxes
            lblChkAudit = (Label) grid.lookup("#lblAuditCheckId");
            lblChkFinal = (Label) grid.lookup("#lblFinalChkId");
            chkAudit = (CheckBox) grid.lookup("#chkAuditId");
            chkAudit.setDisable(false);
            chkFinal = (CheckBox) grid.lookup("#chkFinalId");

            chkAudit.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                    if (!Objects.equals(oldValue, newValue)) {
                        changeAuditFlag(newValue);
                    }
                }
            });
            chkFinal.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                    if (!Objects.equals(oldValue, newValue)) {
                        changeFinalFlag(newValue);
                    }

                }
            });
            setVisibleCheckBoxes(false);

            return grid;
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "could not find resource", ex);
        }
        return new Pane();
    }

    public GridPane getControl() {
        return grid;
    }

    public void setDFee(double fee) {
        dFee.setText(" " + dFeeNotCalculated.checkTextDigits(dFeeNotCalculated.getConverter().toString(Lang.round(fee, 2))));
        setFeeValue(fee);
    }

    public double getDFee() {
        return feeProperty().get();
    }

    public void setNotCalculatedFee(double fee) {
        dFeeNotCalculated.setValue(fee);

    }

    public double getNotCalculatedFee() {
        return dFeeNotCalculated.getValue().doubleValue();
    }

    public void setWasteValue(double waste) {
        //AWI:Add additional space to format the String that the values in gridPane are vertically aligned the same way
        //maybe aligning by these spaces is not the best way? maybe we should use here the padding properties
        wasteValue.setText("  " + dFeeNotCalculated.checkTextDigits(dFeeNotCalculated.getConverter().toString(Lang.round(waste, 2))));
        setWasteDoubleValue(waste);
    }

    public double getWasteValue() {
        return wasteProperty().get();
    }

    public void setAuditPercent(int val) {
        auditRiskPercent.setValue(val);
        lblAuditFullPercent.setText(" " + auditRiskPercent.checkTextDigits(auditRiskPercent.getConverter().toString( val)));
    }

    public int getAuditPercentValue() {
        return auditRiskPercent.getValue().intValue();
    }

    public void setWastePercent(int val) {
        wasteRiskPercent.setValue(val);
        lblWasteFullPercent.setText(" " + wasteRiskPercent.checkTextDigits(wasteRiskPercent.getConverter().toString( val)));
    }

    public int getWastePercentValue() {
        return wasteRiskPercent.getValue().intValue();
    }

    public void setWasteValueSugg(Double val) {
        wasteValueSugg.setText(" " + dFeeNotCalculated.checkTextDigits(dFeeNotCalculated.getConverter().toString(val == null ? 0 : Lang.round(val, 2))));
    }

    public void setAuditPercentSugg(Integer val) {
        auditPercentSugg.setText(" " + auditRiskPercent.checkTextDigits(auditRiskPercent.getConverter().toString(val == null ? 0 : val)));
    }

    public void setWastePercentSugg(Integer val) {
        wastePercentSugg.setText(" " + wasteRiskPercent.checkTextDigits(wasteRiskPercent.getConverter().toString(val == null ? 0 : val)));
    }

    public void setCalcWasteValues(Integer calcWastePercSugg, Integer calcWastePerc) {
        calcWastePercentSugg.setText(" " + wasteRiskPercent.checkTextDigits(wasteRiskPercent.getConverter().toString(calcWastePercSugg == null ? 0 : calcWastePercSugg)));
        calcWastePercent.setText("  " + wasteRiskPercent.checkTextDigits(wasteRiskPercent.getConverter().toString(calcWastePerc == null ? 0 : calcWastePerc)));
    }

    public void setValues(Integer auditPercentSugg,
            Integer auditPercent, 
             Integer wastePercentSugg, Integer wastePercent,
            Double wasteSugg, Double waste,
            Integer calcWastePercSugg, Integer calcWastePerc) {
        this.setAuditPercentSugg(auditPercentSugg);
        this.setWastePercentSugg(wastePercentSugg);
        this.setAuditPercent(auditPercent);
        this.setWastePercent(wastePercent);
        this.setWasteValueSugg(wasteSugg);
        this.setWasteValue(waste);
        this.setCalcWasteValues(calcWastePercSugg, calcWastePerc);
    }

    public void enableFields2VersionType(VersionRiskTypeEn versionType) {
        if(!versionType.equals(VersionRiskTypeEn.BEFORE_BILLING) && !versionType.equals(VersionRiskTypeEn.NOT_SET)){
//            auditRiskPercent.setEditable(versionType.equals(VersionRiskTypeEn.BEFORE_BILLING) || versionType.equals(VersionRiskTypeEn.NOT_SET));    
              auditRiskPercent.setVisible(false);
              this.lblAuditFullPercent.setVisible(true);
        }
        if(versionType.equals(VersionRiskTypeEn.CASE_FINALISATION)){
//            wasteRiskPercent.setEditable(!versionType.equals(VersionRiskTypeEn.CASE_FINALISATION));
              wasteRiskPercent.setVisible(false);
              this.lblWasteFullPercent.setVisible(true);
        }
    }

    private SimpleBooleanProperty changeCalculatedValuesProperty;

    public SimpleBooleanProperty changeCalculatedValuesProperty() {
        if (changeCalculatedValuesProperty == null) {
            changeCalculatedValuesProperty = new SimpleBooleanProperty();
        }
        return changeCalculatedValuesProperty;

    }

    public boolean changeCalculatedValues() {
        return changeCalculatedValuesProperty().get();
    }

    public void changeCalculatedValues(boolean doIt) {
        changeCalculatedValuesProperty().set(doIt);
    }

//    private SimpleBooleanProperty changeWasteValueProperty;
//
//    public SimpleBooleanProperty changeWasteValueProperty() {
//        if (changeWasteValueProperty == null) {
//            changeWasteValueProperty = new SimpleBooleanProperty();
//        }
//        return changeWasteValueProperty;
//
//    }
//
//    public boolean changeWasteValue() {
//        return changeWasteValueProperty().get();
//    }
//
//    public void changeWasteValue(boolean doIt) {
//        changeWasteValueProperty().set(doIt);
//    }
//
    private SimpleBooleanProperty changeAuditFlagProperty;

    public SimpleBooleanProperty changeAuditFlagProperty() {
        if (changeAuditFlagProperty == null) {
            changeAuditFlagProperty = new SimpleBooleanProperty();
        }
        return changeAuditFlagProperty;

    }

    public boolean changeAuditFlag() {
        return changeAuditFlagProperty().get();
    }

    public void changeAuditFlag(boolean doIt) {
        changeAuditFlagProperty().set(doIt);
    }

    private SimpleBooleanProperty changeFinalFlagProperty;

    public SimpleBooleanProperty changeFinalFlagProperty() {
        if (changeFinalFlagProperty == null) {
            changeFinalFlagProperty = new SimpleBooleanProperty();
        }
        return changeFinalFlagProperty;

    }

    public boolean changeFinalFlag() {
        return changeFinalFlagProperty().get();
    }

    public void changeFinalFlag(boolean doIt) {
        changeFinalFlagProperty().set(doIt);
    }

    void enableAll(boolean en) {
        auditRiskPercent.setEditable(en);
        wasteRiskPercent.setEditable(en);

    }

    public void setVisibleCheckBoxes(boolean b) {
        lblChkFinal.setVisible(b);
        chkFinal.setVisible(b);
        setVisibleAuditCheck(b);
    }

    public void setVisibleAuditCheck(boolean b) {
        lblChkAudit.setVisible(b);
        chkAudit.setVisible(b);
    }

    void setChecks(boolean riskUsedForAuditFl, boolean riskUsedForFinalFl) {
//        if (chkAudit.isVisible()) {
            chkAudit.setSelected(riskUsedForAuditFl);
//        }
//        if (chkFinal.isVisible()) {
            chkFinal.setSelected(riskUsedForFinalFl);
//        }
    }

    void adjustLayout(Boolean isReadonly) {
        if (isReadonly) {
            auditPercentSugg.prefWidthProperty().unbind();
            wastePercentSugg.prefWidthProperty().unbind();
            wasteValueSugg.prefWidthProperty().unbind();
            calcWastePercentSugg.prefWidthProperty().unbind();
            auditPercentSugg.prefWidthProperty().bind(dFee.widthProperty());
            wastePercentSugg.prefWidthProperty().bind(auditPercentSugg.widthProperty());
            wasteValueSugg.prefWidthProperty().bind(auditPercentSugg.widthProperty());
            calcWastePercentSugg.prefWidthProperty().bind(auditPercentSugg.widthProperty());

            auditRiskPercent.setVisible(false);
            wasteRiskPercent.setVisible(false);
            wasteValue.setVisible(false);
            calcWastePercent.setVisible(false);
            dFeeNotCalculated.setVisible(false);

        }
    }

    private SimpleDoubleProperty feeProperty;

    private SimpleDoubleProperty feeProperty() {
        if (feeProperty == null) {
            feeProperty = new SimpleDoubleProperty();
        }
        return feeProperty;
    }

    private void setFeeValue(double fee) {
        feeProperty().set(fee);
    }

    private SimpleDoubleProperty wasteProperty;

    private SimpleDoubleProperty wasteProperty() {
        if (wasteProperty == null) {
            wasteProperty = new SimpleDoubleProperty();
        }
        return wasteProperty;
    }

    private void setWasteDoubleValue(double fee) {
        wasteProperty().set(fee);
    }

    public Control getCheckFinal() {
        return this.chkFinal;
    }

    public void removeCheckBoxes() {
//    //        lblChkAudit.prefHeight(0);
//    //        lblChkFinal.prefHeight(0);
//    //        chkAudit.prefHeight(0);
//    //        chkFinal.prefHeight(0);
//    //        lblChkAudit.maxHeight(0);
//    //        lblChkFinal.maxHeight(0);
//    //        chkAudit.maxHeight(0);
//    //        chkFinal.maxHeight(0);
        grid.getChildren().remove(lblChkAudit);
        grid.getChildren().remove(lblChkFinal);
        grid.getChildren().remove(chkAudit);
        grid.getChildren().remove(chkFinal);
        grid.getRowConstraints().remove(6);
        grid.getRowConstraints().remove(5);

     }

}
