/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.job.fx.casemerging.tab.sections;

import de.lb.cpx.client.core.model.fx.sectiontitle.SectionTitle;
import de.lb.cpx.model.TCaseMergeMapping;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * Section for Detected Reasons for Merge Overview Tab
 *
 * @author wilde
 */
public class MrgOvDetectedReasonsSection extends SectionTitle {

    private static final Logger LOG = Logger.getLogger(MrgOvDetectedReasonsSection.class.getName());

    private final TCaseMergeMapping mergeMapping;

    public MrgOvDetectedReasonsSection(TCaseMergeMapping pMapping) {
        super(Lang.getCaseMergeTabSectionDetectedReasons() + " (" + Lang.getCaseMergingIdObj().getAbbreviation() + ": " + pMapping.getMrgMergeIdent() + ")");
        mergeMapping = pMapping;

        setValues();
    }

    @Override
    public void setMenu() {
    }

    @Override
    protected Parent createContent() {
        GridPane gpInfos = new GridPane();
        gpInfos.setPadding(new Insets(12, 0, 0, 0));
        gpInfos.getStyleClass().add("default-grid");
        gpInfos.getStyleClass().add("default-distances");
        gpInfos.setVgap(12.0);
        gpInfos.setHgap(12.0);
        gpInfos.setPrefHeight(GridPane.USE_COMPUTED_SIZE);
        setContentProperty(gpInfos);
        return gpInfos;
    }

    @Override
    public Parent getDetailContent() {
        return new HBox();
    }

    private List<Label> getCondtionList(TCaseMergeMapping pMapping) {
        switch (pMapping.getGrpresType()) {
            case DRG:
                return getCondtionList_DRG(pMapping);
            case PEPP:
                return getCondtionList_PEPP(pMapping);
            default:
                return new ArrayList<>();
        }
    }

    private List<Label> getCondtionList_DRG(TCaseMergeMapping pMapping) {
        ArrayList<Label> conditionList = new ArrayList<>();
        if (pMapping.getMrgCondition1() == 1) {
            conditionList.add(getConditionItemDRG(1));
        }
        if (pMapping.getMrgCondition2() == 1) {
            conditionList.add(getConditionItemDRG(2));
        }
        if (pMapping.getMrgCondition3() == 1) {
            conditionList.add(getConditionItemDRG(3));
        }
        if (pMapping.getMrgCondition4() == 1) {
            conditionList.add(getConditionItemDRG(4));
        }
        if (pMapping.getMrgCondition5() == 1) {
            conditionList.add(getConditionItemDRG(5));
        }
        if (pMapping.getMrgCondition6() == 1) {
            conditionList.add(getConditionItemDRG(6));
        }
        if (pMapping.getMrgCondition7() == 1) {
            conditionList.add(getConditionItemDRG(7));
        }
        if (pMapping.getMrgCondition8() == 1) {
            conditionList.add(getConditionItemDRG(8));
        }
        if (pMapping.getMrgCondition9() == 1) {
            conditionList.add(getConditionItemDRG(9));
        }
        if (pMapping.getMrgCondition10() == 1) {
            conditionList.add(getConditionItemDRG(10));
        }
        return conditionList;
    }

    private List<Label> getCondtionList_PEPP(TCaseMergeMapping pMapping) {
        ArrayList<Label> conditionList = new ArrayList<>();
        if (pMapping.getMrgCondition1() == 1) {
            conditionList.add(getConditionItemPEPP(1));
        }
        if (pMapping.getMrgCondition2() == 1) {
            conditionList.add(getConditionItemPEPP(2));
        }
        if (pMapping.getMrgCondition3() == 1) {
            conditionList.add(getConditionItemPEPP(3));
        }
        if (pMapping.getMrgCondition4() == 1) {
            conditionList.add(getConditionItemPEPP(4));
        }
        if (pMapping.getMrgCondition5() == 1) {
            conditionList.add(getConditionItemPEPP(5));
        }
        if (pMapping.getMrgCondition6() == 1) {
            conditionList.add(getConditionItemPEPP(6));
        }
        if (pMapping.getMrgCondition7() == 1) {
            conditionList.add(getConditionItemPEPP(7));
        }
        if (pMapping.getMrgCondition8() == 1) {
            conditionList.add(getConditionItemPEPP(8));
        }
        if (pMapping.getMrgCondition9() == 1) {
            conditionList.add(getConditionItemPEPP(9));
        }
        if (pMapping.getMrgCondition10() == 1) {
            conditionList.add(getConditionItemPEPP(10));
        }
        return conditionList;
    }

    private Label getConditionItemDRG(Integer pCondition) {
        switch (pCondition) {
            case 1:
                return createSectionLabel(Lang.getCaseMergingConditionOneDrg());
            case 2:
                return createSectionLabel(Lang.getCaseMergingConditionTwoDrg());
            case 3:
                return createSectionLabel(Lang.getCaseMergingConditionThreeDrg());
            case 4:
                return createSectionLabel(Lang.getCaseMergingConditionFourDrg());
            case 5:
                return createSectionLabel(Lang.getCaseMergingConditionFiveDrg());
            case 6:
                return createSectionLabel(Lang.getCaseMergingConditionSixDrg());
            case 7:
                return createSectionLabel(Lang.getCaseMergingConditionSevenDrg());
            case 8:
                return createSectionLabel(Lang.getCaseMergingConditionEightDrg());
            case 9:
                return createSectionLabel(Lang.getCaseMergingConditionNineDrg());
            case 10:
                return createSectionLabel(Lang.getCaseMergingConditionTenDrg());
            default:
                return null;
        }
    }

    private Label getConditionItemPEPP(Integer pCondition) {
        switch (pCondition) {
            case 1:
                return createSectionLabel(Lang.getCaseMergingConditionOnePepp());
            case 2:
                return createSectionLabel(Lang.getCaseMergingConditionTwoPepp());
            case 3:
                return createSectionLabel(Lang.getCaseMergingConditionThreePepp());
            case 4:
                return createSectionLabel(Lang.getCaseMergingConditionFourPepp());
            case 5:
                return createSectionLabel(Lang.getCaseMergingConditionFivePepp());
            case 6:
                return createSectionLabel(Lang.getCaseMergingConditionSixPepp());
            case 7:
                return createSectionLabel(Lang.getCaseMergingConditionSevenPepp());
            case 8:
                return createSectionLabel(Lang.getCaseMergingConditionEightPepp());
            case 9:
                return createSectionLabel(Lang.getCaseMergingConditionNinePepp());
            case 10:
                return createSectionLabel(Lang.getCaseMergingConditionTenPepp());
            default:
                return null;
        }
    }

    private Label createSectionLabel(String pText) {
        Label label = new Label(pText);
        return label;
    }

    private void setValues() {
        if (getContent() == null) {
            LOG.warning("Can not set Values in content, no content was set!");
            return;
        }
        Parent content =  getContent();
        GridPane gp = (GridPane) getContentProperty();
        int cnt = 0;
        for (Label lbl : getCondtionList(mergeMapping)) {
            addToGrid(gp, 0, cnt, lbl);
            cnt++;
        }
    }

    private void addToGrid(GridPane pGrid, int pColIndex, int pRowIndex, Node pItem) {
        GridPane.setMargin(pItem, new Insets(0, 0, 0, 8));
        pGrid.add(pItem, pColIndex, pRowIndex);
    }
}
