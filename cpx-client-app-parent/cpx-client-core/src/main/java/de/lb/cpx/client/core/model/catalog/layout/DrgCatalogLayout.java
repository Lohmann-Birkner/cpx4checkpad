/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.catalog.layout;

import de.lb.cpx.client.core.model.catalog.CpxDrg;
import de.lb.cpx.model.TCaseDrg;
import de.lb.cpx.model.enums.AdmissionModeEn;
import de.lb.cpx.model.enums.DrgCorrTypeEn;
import de.lb.cpx.model.enums.GrouperMdcOrSkEn;
import de.lb.cpx.shared.lang.Lang;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

/**
 * Ui Layout for displaying drg catalog data VBox unnecessary for now, could be
 * removed on the other hand, do not know if additional info will be added in
 * future
 *
 * @author wilde
 */
public class DrgCatalogLayout extends VBox {
    private final static String KEY_STYLE = "-fx-text-fill:-black05";
    private final static String VALUE_STYLE = "-fx-text-fill:-text-main";
    private final CpxDrg catalogItem;
    private final AdmissionModeEn admMode;

    private Integer rowCount = 0;
    private final String pCorrDays;
    private final String pCorrCw;
    private final DrgCorrTypeEn pCorrType;
    private final GrouperMdcOrSkEn pMdc;
    
        /**
     * creates new layout instance as vBox
     *
     * @param pCatalogItem catalog item to display
     * @param pMode admission mode to show correct values for surchage or
     * deduction
     * @param pCorrDays correction days
     * @param pCorrCw corrected cw value
     * @param pCorrType correction type
     */
    public DrgCatalogLayout(CpxDrg pCatalogItem, AdmissionModeEn pMode, String pCorrDays, String pCorrCw, DrgCorrTypeEn pCorrType){
        this(pCatalogItem, pMode, pCorrDays, pCorrCw, pCorrType, null);
    }
    /**
     * creates new layout instance as vBox
     *
     * @param pCatalogItem catalog item to display
     * @param pMode admission mode to show correct values for surchage or
     * deduction
     * @param pCorrDays correction days
     * @param pCorrCw corrected cw value
     * @param pCorrType correction type
     * @param pMdc mdc from grouping results
     */
    public DrgCatalogLayout(CpxDrg pCatalogItem, AdmissionModeEn pMode, String pCorrDays, String pCorrCw, DrgCorrTypeEn pCorrType, GrouperMdcOrSkEn pMdc) {
        super();
        catalogItem = pCatalogItem;
        admMode = pMode;
        this.pCorrDays = pCorrDays;
        this.pCorrCw = pCorrCw;
        this.pCorrType = pCorrType;
        this.pMdc = pMdc;
//        setPrefWidth(500);
//        setPrefHeight(USE_COMPUTED_SIZE);
        //add to children
        getChildren().add(createContent());
        //Workaround, add padding.. sometimes for some reason popover do not render top and bottom padding declared in css
        setPadding(new Insets(1));
    }

    public DrgCatalogLayout(CpxDrg pCatalogItem,
            AdmissionModeEn pMode,
            TCaseDrg pResult,
            String pCorrDays,
            String pCorrCw,
            DrgCorrTypeEn pCorrType) {
        this(pCatalogItem, pMode, pCorrDays, pCorrCw, pCorrType, pResult.getGrpresGroup() );
        
        if(pResult.isNegotiatedDayFee()){
            showNegotiatedValues(pResult.getDrgcNegoFee2Day(),
                    pResult.getDrgcNegoFeeDays());
        }else{
            showDrgCwValues(pResult.getDrgcCwCatalog(),
                pResult.getDrgcCwEffectiv());
        }
        showCareCwValues(pResult.getDrgcCareDays(),pResult.getCareCwDays());
    }

//    public DrgCatalogLayout(CpxDrg pCatalogItem, AdmissionModeEn pMode, TCaseDrg pResult, double pBaserate, double unkCw) {
//        this(pCatalogItem, pMode, String.valueOf(pResult.getDrgcDaysCorr()), Lang.toDecimal(pResult.getDrgcCwCorr(), 3), pResult.getDrgcTypeOfCorrEn(), pBaserate,pResult.getDrgcCwCatalog(), unkCw, pResult.getDrgcCwEffectiv());
//    }
    public DrgCatalogLayout(CpxDrg pCatalogItem, AdmissionModeEn pMode, TCaseDrg pResult){
        this(pCatalogItem,
                pMode,
                pResult,
                String.valueOf(pResult.getDrgcDaysCorr()),
                Lang.toDecimal(pResult.getDrgcCwCorr(), 3),
                pResult.getDrgcTypeOfCorrEn());
    }
    private void showDrgCwValues(double pCwCatalog, double pEffCw) {
        GridPane pane = (GridPane) getChildren().get(0);

        Label first = new Label(Lang.getCatalogDrgCostweight());
        first.setStyle(KEY_STYLE);
        Label firstValue = new Label();
        firstValue.setStyle(VALUE_STYLE);
        Label secondValue = new Label();
        secondValue.setStyle(VALUE_STYLE);
//        Label thirdValue = new Label();
//        thirdValue.setStyle("-fx-text-fill:-text-main");

        firstValue.setText(Lang.getCatalogDrgUnkCw(Lang.toDecimal(pCwCatalog, 3)));
        secondValue.setText(Lang.getCatalogDrgEffCw(Lang.toDecimal(pEffCw, 3)));
//        thirdValue.setText("PflegeCW "+Lang.toDecimal(pCareCwPerDay,4)+" ("+Lang.toDecimal(pRevenueCare,2)+"€)");

        pane.addRow(incrementRow(), first, firstValue);
        pane.addRow(incrementRow(), new Label(), secondValue);
//        pane.addRow(incrementRow(), new Label(), thirdValue);
        
        pane.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.NEVER, VPos.TOP, true));
        pane.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.NEVER, VPos.TOP, true));
//        pane.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.NEVER, VPos.TOP, true));
    }
    
    private void showNegotiatedValues(double pNegoFeePerDay, int pNegoFeeDays) {
        GridPane pane = (GridPane) getChildren().get(0);

        Label first = new Label(Lang.getCatalogDrgNegoTitle());
        first.setStyle(KEY_STYLE);
        Label firstValue = new Label();
        firstValue.setStyle(VALUE_STYLE);
//        Label secondValue = new Label();
//        secondValue.setStyle("-fx-text-fill:-text-main");
        
        firstValue.setText(Lang.getCatalogDrgNegoDesc(Lang.toDecimal(pNegoFeePerDay, 2), Lang.getCurrencySymbol(), pNegoFeeDays));
//        secondValue.setText("PflegeCW pro Tag "+Lang.toDecimal(pCareCwPerDay,2)+"€, "+pCareDays+" Tag(e) ("+Lang.toDecimal(pRevenueCare,2)+"€)");
        
        pane.addRow(incrementRow(), first, firstValue);
//        pane.addRow(incrementRow(), new Label(), secondValue);

        pane.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.NEVER, VPos.TOP, true));
//        pane.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.NEVER, VPos.TOP, true));
    }
    
    private void showCareCwValues(int pCareDays, double pCareCw){
        GridPane pane = (GridPane) getChildren().get(0);

        Label first = new Label(Lang.getCatalogDrgCareTitle());
        first.setStyle(KEY_STYLE);
        Label firstValue = new Label();
        firstValue.setStyle(VALUE_STYLE);
//        Label secondValue = new Label();
//        secondValue.setStyle("-fx-text-fill:-text-main");
        
        firstValue.setText(Lang.getCatalogDrgCareDesc(pCareDays, Lang.toDecimal(pCareCw, 4)));
//        secondValue.setText("PflegeCW pro Tag "+Lang.toDecimal(pCareCwPerDay,2)+"€, "+pCareDays+" Tag(e) ("+Lang.toDecimal(pRevenueCare,2)+"€)");
        
        pane.addRow(incrementRow(), first, firstValue);
//        pane.addRow(incrementRow(), new Label(), secondValue);

        pane.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.NEVER, VPos.TOP, true));
    }
    
    private Integer incrementRow() {
        return rowCount++;
    }
//
//    private String computeCwValue(double pCw, double pBaseRate) {
//        return Lang.toDecimal(pCw * pBaseRate, 2);
//    }

    //creates node
    private Node createContent() {
        GridPane pane = new GridPane();
        pane.setMinWidth(200);
        pane.setMaxWidth(600);
        pane.getStyleClass().add("default-grid");
        CpxDrg item = catalogItem;
        Label admModeText = new Label(Lang.getModeOfAdmission());
        admModeText.setStyle(KEY_STYLE);
        Label admModeValue = new Label(admMode.getTranslation().getDescription());
        admModeValue.setStyle(VALUE_STYLE);
        Label first = new Label(Lang.getCatalogDrgFirstDaySurcharge());
        first.setStyle(KEY_STYLE);
        Label firstValue = new Label();
        firstValue.setStyle(VALUE_STYLE);
        Label second = new Label(Lang.getCatalogDrgLos());
        second.setStyle(KEY_STYLE);
        Label secondValue = new Label();
        secondValue.setStyle(VALUE_STYLE);
        Label third = new Label(Lang.getCatalogDrgFirstDayDeduction());
        third.setStyle(KEY_STYLE);
        Label thirdValue = new Label();
        thirdValue.setStyle(VALUE_STYLE);
        Label four = new Label(Lang.getCatalogDrgMdc("- "));
        four.setStyle(KEY_STYLE);
        Label fourValue = new Label("-");
        fourValue.setWrapText(true);
        fourValue.setStyle(VALUE_STYLE);
        Label five = new Label("Korrektur(-)");
        five.setStyle(KEY_STYLE);
        Label fiveValue = new Label("-");
        fiveValue.setWrapText(true);
        fiveValue.setStyle(VALUE_STYLE);

        pane.addRow(incrementRow(), admModeText,admModeValue);
        pane.addRow(incrementRow(), first, firstValue);
        pane.addRow(incrementRow(), second, secondValue);
        pane.addRow(incrementRow(), third, thirdValue);
        pane.addRow(incrementRow(), four, fourValue);
        if (!DrgCorrTypeEn.no.equals(pCorrType)) {
            pane.addRow(incrementRow(), five, fiveValue);
        }

        pane.getColumnConstraints().addAll(getColumnConstraints());

        pane.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.NEVER, VPos.TOP, true));
        pane.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.NEVER, VPos.TOP, true));
        pane.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.NEVER, VPos.TOP, true));
        pane.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.NEVER, VPos.TOP, true));
        pane.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.NEVER, VPos.TOP, true));
        if (!DrgCorrTypeEn.no.equals(pCorrType)) {
            pane.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.NEVER, VPos.TOP, true));
        }

        Mode mode = getModeForAdmission(admMode);
        if (mode != null) {
            switch (mode) {
                case EO:
                    setTextInLabel(firstValue, item.getDrgEo1SurchargeDay());
                    setTextInLabel(secondValue, item.getDrgEoAlos());
                    setTextInLabel(thirdValue, item.getDrgEo1DeductionDay());
                    break;
                case MD:
                    setTextInLabel(firstValue, item.getDrgMd1SurchargeDay());
                    setTextInLabel(secondValue, item.getDrgMdAlos());
                    setTextInLabel(thirdValue, item.getDrgMd1DeductionDay());
                    break;
            }
        }
        if (item.getCMdcSkCatalog() != null) {
// check catalog mdc with grouper mdc, they can be different by "PRE" from Catalog      
            if(pMdc == null || pMdc.equals(item.getCMdcSkCatalog() )){
                        four.setText(Lang.getCatalogDrgMdc(String.valueOf(item.getCMdcSkCatalog().getId())));
                        fourValue.setText(item.getCMdcSkCatalog().getTranslation().getValue() + "\n");
            }else{
                four.setText(Lang.getCatalogDrgMdc(pMdc.getId()));
                fourValue.setText(pMdc.getTranslation().getValue() + "\n"
                        +  (item.getCMdcSkCatalog().equals(GrouperMdcOrSkEn.pre)?Lang.getCatalogMdcPre():item.getCMdcSkCatalog().getId()) 
                        + " - "  
                +item.getCMdcSkCatalog().getTranslation().getValue() + "\n");
            }
        }
        if (!DrgCorrTypeEn.no.equals(pCorrType)) {
            five.setText("Korrektur (" + getCorrReasion(pCorrType) + "):");
            fiveValue.setText(getCorrModifier() + pCorrDays + " Tag(e), entspr. " + pCorrCw);
        }
        return pane;
    }
    protected List<ColumnConstraints> columnConstraints;

    protected List<ColumnConstraints> getColumnConstraints() {
        if (columnConstraints == null) {
            columnConstraints = new ArrayList<>();
            columnConstraints.add(new ColumnConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.ALWAYS, HPos.LEFT, true));
        }
        return columnConstraints;
    }

    private void setTextInLabel(Label pLabel, Object pText) {
        pLabel.setText(pText == null ? "" : String.valueOf(pText));
    }

    private Mode getModeForAdmission(AdmissionModeEn pMode) {
        switch (pMode) {
            case Bo:
            case BoBa:
            case BoBh:
            case BoBaBh:
                return Mode.EO;
            case HA:
            case HaBh:
            case HaBha:
                return Mode.MD;
            default:
                return null;
        }
    }

    public static final Double getUncCwForAdmission(CpxDrg pCatalogItem, AdmissionModeEn pMode) {
        switch (pMode) {
            case HA: //hauptabteilung
                return getDouble(pCatalogItem.getDrgMdCw());
            case HaBh: //hauptabteilung hebamme
                return getDouble(pCatalogItem.getDrgMdMCw());
            case HaBha: //hauptabteilung hno arzt
                return getDouble(pCatalogItem.getDrgMdCw()) * 0.8; // this doctor is not set in catalog, info from AGe use 80 percent of md
            case Bo: //b op
                return getDouble(pCatalogItem.getDrgEoCw());
            case BoBa: //b op anäst
                return getDouble(pCatalogItem.getDrgEoaCw());
            case BoBh: //b op hebamme
                return getDouble(pCatalogItem.getDrgEomCw());
            case BoBaBh: //b op anäst hebamme
                return getDouble(pCatalogItem.getDrgEoamCw());
            default:
                return 0.0;
        }
    }

    private static Double getDouble(BigDecimal pDecimal) {
        return pDecimal != null ? pDecimal.doubleValue() : 0.0;
    }

    private String getCorrReasion(DrgCorrTypeEn pCorrType) {
        switch (pCorrType) {
            case Deduction:
                return "UGVD";
            case Surcharge:
                return "OGVD";
            case DeductionTransfer:
                return "Verlegung";
            case DeductionTransferAdm:
                return "Aufnahme - Verlegung";
            case DeductionTransferDis:
                return "Entlassung - Verlegung";
            default:
                return "-";
        }
    }

    private String getCorrModifier() {
        switch (pCorrType) {
            case Surcharge:
                return "+";
            case Deduction:
            case DeductionTransfer:
            case DeductionTransferAdm:
            case DeductionTransferDis:
                return "-";
            default:
                return "";
        }
    }

    private enum Mode {
        MD, EO
    }
}
