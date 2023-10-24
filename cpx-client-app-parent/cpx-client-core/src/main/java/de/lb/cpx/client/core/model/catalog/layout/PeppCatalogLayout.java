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

import de.lb.cpx.client.core.model.catalog.CpxPepp;
import de.lb.cpx.model.TCasePepp;
import de.lb.cpx.model.TCasePeppGrades;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.enums.GrouperMdcOrSkEn;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
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
public class PeppCatalogLayout extends VBox {
    private final static String KEY_STYLE = "-fx-text-fill:-black05";
    private final static String VALUE_STYLE = "-fx-text-fill:-text-main";
    private final CpxPepp catalogItem;
//    private final AdmissionModeEn admMode;
    private final GrouperMdcOrSkEn grouperSk;
    private final long los;
    private TGroupingResults peppResult;

    /**
     * creates new layout instance as vBox
     *
     * @param pCatalogItem catalog item to display
     * @param pGrouperSk GrouperMdcOrSkEn
     * @param pLos length of stay
     */
    public PeppCatalogLayout(CpxPepp pCatalogItem, GrouperMdcOrSkEn pGrouperSk, Long pLos) {
        super();
        catalogItem = pCatalogItem;
//        admMode = pMode;
        grouperSk = pGrouperSk;
        los = pLos;
        setPrefWidth(USE_COMPUTED_SIZE);
        setPrefHeight(USE_COMPUTED_SIZE);
        //add to children
        getChildren().add(createContent());
        //Workaround, add padding.. sometimes for some reason popover do not render top and bottom padding declared in css
        setPadding(new Insets(1));
    }

    public PeppCatalogLayout(CpxPepp pCatalogItem, TGroupingResults pResult, Long pLos) {
        super();
        catalogItem = pCatalogItem;
        los = pLos;
        peppResult = pResult;
        grouperSk = pResult.getGrpresGroup();
        setPrefWidth(USE_COMPUTED_SIZE);
        setPrefHeight(USE_COMPUTED_SIZE);
        //add to children
        getChildren().add(createContent());
        //Workaround, add padding.. sometimes for some reason popover do not render top and bottom padding declared in css
        setPadding(new Insets(1));
    }

    //creates node
    private Node createContent() {
        GridPane pane = new GridPane();
        pane.getStyleClass().add("default-grid");
//        CpxPepp item = catalogItem;

//        Label admModeText = new Label("Admission Mode:");
//        Label admModeValue = new Label(admMode.getTranslation().getValue());
//        Label first = new Label(Lang.getCatalogDrgFirstDaySurcharge());
//        first.setStyle("-fx-text-fill:-text-main");
//        Label firstValue = new Label();
//        firstValue.setStyle("-fx-text-fill:-text-main");
//        Label second = new Label(Lang.getCatalogDrgLos());
//        second.setStyle("-fx-text-fill:-text-main");
//        Label secondValue = new Label();
//        secondValue.setStyle("-fx-text-fill:-text-main");
//        Label third = new Label(Lang.getCatalogDrgFirstDayDeduction());
//        third.setStyle("-fx-text-fill:-text-main");
//        Label thirdValue = new Label();
//        thirdValue.setStyle("-fx-text-fill:-text-main");
        if (grouperSk != null) {
            Label lbgrouperSk = new Label(Lang.getCatalogPeppSc(grouperSk.getId()));
            lbgrouperSk.setStyle(KEY_STYLE);
            Label lbgrouperSkInfo = new Label(grouperSk.equals(GrouperMdcOrSkEn.pre)?Lang.getCatalogSkPre():grouperSk.getTranslation().toString());
            lbgrouperSkInfo.setWrapText(true);
            lbgrouperSkInfo.setStyle(VALUE_STYLE);
//        pane.addRow(1, second,secondValue);
//        pane.addRow(2, third,thirdValue);
            pane.addRow(0, lbgrouperSk, lbgrouperSkInfo);
        }
// berechnungstage        
        Label lbLos = new Label(Lang.getDaysStayPepp());
        lbLos.setStyle(KEY_STYLE);
        Label lbLosInfo = new Label(String.valueOf(los));
        lbLosInfo.setStyle(VALUE_STYLE);
        pane.addRow(1, lbLos, lbLosInfo);
        if (catalogItem != null && peppResult != null && peppResult instanceof TCasePepp) {
            Label firstLabel = new Label();
            firstLabel.setStyle(KEY_STYLE);
            Label secondLabel = new Label();
            secondLabel.setStyle(VALUE_STYLE);
            int rowNr = 2;
            String[] line = new String[2];
            List<String[]> lines = getGradesList(peppResult.getCasePepp().getPeppcGrades(), catalogItem.getPeppHasClassesFl());
            if (!lines.isEmpty()) {
                line = lines.remove(0);
            }
            if(line[1] != null){
                if (catalogItem.getPeppHasClassesFl()) {// Vergütungsklassen > 2013
                    firstLabel.setText(Lang.getPayClass() + " " + peppResult.getCasePepp().getPeppcPayClass());
                } else {
                    firstLabel.setText(Lang.getPayGrade() + " " + line[0]);
                }
                secondLabel.setText(line[1]);

                pane.addRow(rowNr, firstLabel, secondLabel);

                for (String[] ln : lines) {
                    firstLabel = catalogItem.getPeppHasClassesFl() ? new Label() : new Label(Lang.getPayGrade() + " " + ln[0]);
                    firstLabel.setStyle(KEY_STYLE);
                    secondLabel = new Label(ln[1]);
                    secondLabel.setStyle(VALUE_STYLE);
                    pane.addRow(++rowNr, firstLabel, secondLabel);

                }
            }

        }
        pane.getColumnConstraints().add(new ColumnConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.ALWAYS, HPos.LEFT, true));

        pane.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.NEVER, VPos.TOP, true));
        pane.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.NEVER, VPos.TOP, true));
//        pane.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.NEVER, VPos.TOP, true));
//        pane.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.NEVER, VPos.TOP, true));

//        Mode mode = getModeForAdmission(admMode);
//        if (mode != null) {
//            switch(mode){
//                case EO:
//                    firstValue.setText(String.valueOf(item.getDrgEo1SurchargeDay()));
//                    secondValue.setText(String.valueOf(item.getDrgEoAlos()));
//                    thirdValue.setText(String.valueOf(item.getDrgEo1DeductionDay()));
//                    break;
//                case MD:
//                    firstValue.setText(String.valueOf(item.getDrgMd1SurchargeDay()));
//                    secondValue.setText(String.valueOf(item.getDrgMdAlos()));
//                    thirdValue.setText(String.valueOf(item.getDrgMd1DeductionDay()));
//                    break;
//            }
//            if (item.getCMdcSkCatalog() != null) {
//                four.setText(Lang.getCatalogPeppSc(String.valueOf(item.getCMdcSkCatalog().getId())));
//                fourValue.setText(item.getCMdcSkCatalog().getTranslation().getValue() + "\n");
//            }
//        }
        return pane;
    }

    private List<String[]> getGradesList(Set<TCasePeppGrades> pPeppGrades, boolean hasClasses) {

        List<String[]> objects = new ArrayList<>();
        if (pPeppGrades == null || pPeppGrades.isEmpty()) {
            return objects;
        }
        List<TCasePeppGrades> sortPeppGrades = new ArrayList<>(pPeppGrades);
        Collections.sort(sortPeppGrades);
        TCasePeppGrades firstGrade = sortPeppGrades.get(0);

        double cw = 0;
        int days = 0;
        String cwDay = Lang.toDecimal(firstGrade.getPeppcgrDays() > 0 ? firstGrade.getPeppcgrCw() / firstGrade.getPeppcgrDays() : 0, 4);

        int gradeNr = firstGrade.getPeppcgrNumber();
        String[] row = new String[2];

        row[0] = String.valueOf(gradeNr);
        for (TCasePeppGrades pGrade : sortPeppGrades) {
            if (gradeNr != pGrade.getPeppcgrNumber()) {
                row[1] = Lang.getPeppCatalogLayout(Lang.toDecimal(cw, 4), String.valueOf(days), cwDay);
                objects.add(row);
                cw = pGrade.getPeppcgrCw();
                days = firstGrade.getPeppcgrDays();

                gradeNr = pGrade.getPeppcgrNumber();
                cwDay = Lang.toDecimal(pGrade.getPeppcgrDays() > 0 ? pGrade.getPeppcgrCw() / pGrade.getPeppcgrDays() : 0, 4);
                row = new String[2];
                row[0] = String.valueOf(gradeNr);
            }
            cw += pGrade.getPeppcgrCw();
            days += pGrade.getPeppcgrDays();

        }

        row[1] = Lang.getPeppCatalogLayout(Lang.toDecimal(cw, 4), String.valueOf(days), cwDay);
        objects.add(row);
        return objects;
    }

//    private class GradesCompare implements Comparator<TCasePeppGrades>{
//
//        @Override
//        public int compare(TCasePeppGrades grade1, TCasePeppGrades grade2) {
//                if(grade1.getPeppcgrNumber() < grade2.getPeppcgrNumber()){
//                    return -1;
//                }
//                if(grade1.getPeppcgrNumber() > grade2.getPeppcgrNumber()){
//                    return 1;
//                }
//                return(grade1.getPeppcgrFrom().compareTo(grade2.getPeppcgrFrom()));
//        }
//
//    
//    }
//    private Mode getModeForAdmission(AdmissionModeEn pMode) {
//        switch (pMode) {
//            case Bo:
//            case BoBaBh:
//                return Mode.EO;
//            case HA:
//            case HaBh:
//            case HaBha:
//                return Mode.MD;
//            default:
//                return null;
//        }
//    }
//    private enum Mode {
//        MD, EO
//    }
}
