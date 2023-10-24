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

import de.lb.cpx.client.app.service.facade.CaseMergingFacade;
import de.lb.cpx.client.core.model.fx.sectiontitle.SectionTitle;
import de.lb.cpx.client.core.model.fx.tooltip.DrgRevenueLayout;
import de.lb.cpx.client.core.model.fx.tooltip.PeppRevenueLayout;
import de.lb.cpx.client.core.model.fx.tooltip.RevenueDeltaLayout;
import de.lb.cpx.client.core.util.ExtendedInfoHelper;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseMergeMapping;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.layout.VBox;

/**
 * class to handle section for case data overview
 *
 * @author wilde
 */
public class MrgOvCaseDataSection extends SectionTitle {

    private final TCase mergedCase;
    private final TGroupingResults mergedGrResult;
    private final List<TCaseMergeMapping> mergedMappings;
    private final CaseMergingFacade facade;

    /**
     * creates new instance
     *
     * @param pMergeId merge id
     * @param pMergedCase merged case
     * @param pGrResults grouping result of merged case
     * @param pMappings list of mergeMapping for merge id
     * @param pFacade facade for data access
     */
    public MrgOvCaseDataSection(long pMergeId, TCase pMergedCase, TGroupingResults pGrResults, List<TCaseMergeMapping> pMappings, CaseMergingFacade pFacade) {
        super(Lang.getCaseMergeTabSectionCaseData() + " (" + Lang.getCaseMergingIdObj().getAbbreviation() + ": " + pMergeId + ")");
        mergedCase = pMergedCase;
        mergedGrResult = pGrResults;
        mergedMappings = pMappings == null ? null : new ArrayList<>(pMappings);
        facade = pFacade;
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
//        gpInfos.getStyleClass().add("default-distances");
        gpInfos.setVgap(10.0);
//        gpInfos.setHgap(12.0);
        gpInfos.setMaxHeight(USE_COMPUTED_SIZE);
        setContentProperty(gpInfos);
        return gpInfos;
    }

    @Override
    public Parent getDetailContent() {
        return new HBox();
    }

    private void setValues() {
        GridPane gp = (GridPane) getContentProperty();
        int cnt = 0;
        List<CaseDataItem> items = getCaseItems();
        for (CaseDataItem item : items) {
            addToGrid(gp, 0, cnt, item);
            cnt++;
        }
        addToGrid(gp, 0, cnt++, new RevenueDeltaItem(items));
    }

    private List<CaseDataItem> getCaseItems() {
        List<CaseDataItem> caseItems = new ArrayList<>();
        caseItems.add(new CaseDataItem(mergedCase, mergedGrResult, facade.getCaseBaseRateFeeValue(mergedCase), facade.getCareBaseRateFeeValue(mergedCase), 0));
        int cnt = 1;
        for (TCaseMergeMapping map : mergedMappings) {
            caseItems.add(new CaseDataItem(map.getCaseByMergeMemberCaseId(), map.getGrpresId(), facade.getCaseBaseRateFeeValue(map.getCaseByMergeMemberCaseId()), facade.getCareBaseRateFeeValue(map.getCaseByMergeMemberCaseId()), cnt));
            cnt++;
        }
        return caseItems;
    }

    private void addToGrid(GridPane pGrid, int pColIndex, int pRowIndex, Region pItem) {
        pItem.setMaxWidth(Double.MAX_VALUE);
        GridPane.setMargin(pItem, new Insets(0, 0, 0, 8));
        pGrid.add(pItem, pColIndex, pRowIndex);
    }

    private class CaseDataItem extends VBox {

        private final TCase hcase;
        private final TGroupingResults result;
        private final Label lblCaseNumber = new Label();
        private final Label lblDrg = new Label();
        private final Label lblRevenue = new Label();
        private final Label lblMD = new Label();
        private final Double baserate;
        private Double revenue = 0.0;
        private boolean isMerged = false;
        private Label lblDrgText = new Label();
        private int index = -1;
        private GridPane gpInfos;
        private Double careBaserate;

        CaseDataItem(TCase pCase, TGroupingResults pResult, Double pBaserate, Double pCareBaserate) {
//            hcase = pCase;
//            result = pResult;
//            baserate = pBaserate;
//            isMerged = pCase.getId()==0;
//            createLayout();
//            setValues();
            this(pCase, pResult, pBaserate, pCareBaserate, -1);
        }

        CaseDataItem(TCase pCase, TGroupingResults pResult, Double pBaserate, Double pCareBaserate, int pIndex) {
            hcase = pCase;
            result = pResult;
            baserate = pBaserate;
            careBaserate = pCareBaserate;
            isMerged = pCase.getId() == 0;
            index = pIndex;
            createLayout();
            setValues();
        }

        public double getRevenue() {
            return revenue;
        }

        public boolean isMerged() {
            return isMerged;
        }

        private void createLayout() {
            setSpacing(1.0);
            getChildren().add(0, lblCaseNumber);
            setFillWidth(true);
            gpInfos = new GridPane();
//            gpInfos.setPadding(new Insets(12, 0, 0, 0));
            gpInfos.getStyleClass().add("default-grid");
            gpInfos.getStyleClass().add("default-distances");
            gpInfos.setVgap(12.0);
//            gpInfos.setHgap(12.0);
            gpInfos.setPrefHeight(GridPane.USE_COMPUTED_SIZE);

            getChildren().add(1, gpInfos);

            Label lblMdText = new Label(Lang.getICDCodeObj().getAbbreviation());
            lblMdText.getStyleClass().add("cpx-detail-label");

//            labelDrgText = new Label();
            lblDrgText.getStyleClass().add("cpx-detail-label");

            Label labelRevenueText = new Label(Lang.getRevenue());
            labelRevenueText.getStyleClass().add("cpx-detail-label");

            gpInfos.add(lblMdText, 0, 0);
            gpInfos.add(lblMD, 1, 0);

            gpInfos.add(lblDrgText, 2, 0);
            gpInfos.add(lblDrg, 3, 0);

            gpInfos.add(labelRevenueText, 4, 0);

            gpInfos.add(lblRevenue, 5, 0);
//            lblRevenue.setTextAlignment(TextAlignment.RIGHT);
            GridPane.setHalignment(lblRevenue, HPos.RIGHT);
            GridPane.setHgrow(lblRevenue, Priority.ALWAYS);
            ColumnConstraints lastCol = new ColumnConstraints();
            lastCol.setPrefWidth(80);
            gpInfos.getColumnConstraints().addAll(new ColumnConstraints(),
                    new ColumnConstraints(50),
                    new ColumnConstraints(),
                    new ColumnConstraints(50),
                    new ColumnConstraints(),
                    lastCol);
//            gpInfos.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, 28, USE_PREF_SIZE, Priority.NEVER, VPos.TOP, true));
//            lblRevenue.setStyle("-fx-background-color:white");
        }

        private void setValues() {
            lblCaseNumber.setText(hcase.getCsCaseNumber() + addAdditionalDescriptionString(index));
            lblMD.setText(hcase.getCurrentLocal().getHdIcdCode());
            if (result != null) {
                lblDrgText.setText(result.getGrpresType().name());
                lblDrg.setText(result.getGrpresCode());
                revenue = getRevenue(result);
            }
            lblRevenue.setText(Lang.toDecimal(revenue, 2) + Lang.getCurrencySymbol());
        }

        private Double getRevenue(TGroupingResults result) {
            Pane pane;
            switch (result.getGrpresType()) {
                case DRG:
                    Double revDrg = result.getCaseDrg().getRevenue(baserate, careBaserate);//BaserateHelper.computeDrgRevenue(result.getCaseDrg(), baserate, careBaserate);
                    DrgRevenueLayout tipDrg = new DrgRevenueLayout(revDrg, baserate, careBaserate, result.getCaseDrg());
                    pane = ExtendedInfoHelper.addInfoPane(lblRevenue, tipDrg, null);
                    gpInfos.getChildren().remove(lblRevenue);
                    gpInfos.add(pane, 5, 0);
                    return revDrg;
                case PEPP:
                    Double revPepp = result.getCasePepp().getRevenue();//BaserateHelper.computePeppRevenue(result.getCasePepp());
                    PeppRevenueLayout tipPepp = new PeppRevenueLayout(revPepp, result.getCasePepp().getPeppcGrades(), Objects.requireNonNullElse(result.getCasePepp().getPeppcPayClass(), 0), Lang.toYear(hcase.getCurrentLocal().getCsdAdmissionDate()));
                    pane = ExtendedInfoHelper.addInfoPane(lblRevenue, tipPepp, null);
                    gpInfos.getChildren().remove(lblRevenue);
                    gpInfos.add(pane, 5, 0);
                    return revPepp;
                default:
                    return 0.0;
            }
        }

        private String addAdditionalDescriptionString(int index) {
            switch (index) {
                case -1:
                    return "";
                case 0:
                    return " (" + Lang.getCaseMergingCase() + ")";
                case 1:
                    return " (" + Lang.getCaseMergingCaseLeading() + ")";
                default:
                    return " (" + Lang.getCaseMergingCaseOrginal() + ")";
            }
        }
    }

    private class RevenueDeltaItem extends GridPane {

        private final Label lblRevenueDelta = new Label();
        private final List<CaseDataItem> caseItems;

        RevenueDeltaItem(List<CaseDataItem> pItems) {
            caseItems = pItems == null ? null : new ArrayList<>(pItems);
            createLayout();
            setValues();
//            setStyle("-fx-background-color:red");
        }

        private void createLayout() {

            getStyleClass().add("default-grid");
            getStyleClass().add("default-distances");
//            setVgap(12.0);
            setHgap(12.0);
            setPrefHeight(GridPane.USE_COMPUTED_SIZE);

//            getChildren().add(1, gpInfos);
            Label lblRevenueDeltaText = new Label(Lang.getRevenue() + " Delta");
            lblRevenueDeltaText.getStyleClass().add("cpx-detail-label");

            add(lblRevenueDeltaText, 0, 0);
//            gpInfos.setMargin(labelDrgText, new Insets(0, 0, 0, 8));
            add(lblRevenueDelta, 1, 0);
            GridPane.setHalignment(lblRevenueDelta, HPos.RIGHT);
            ColumnConstraints colCons1 = new ColumnConstraints();
            colCons1.setHgrow(Priority.ALWAYS);
            ColumnConstraints colCons2 = new ColumnConstraints();
            colCons2.setPrefWidth(80);
            getColumnConstraints().addAll(colCons1, colCons2);
        }

        private void setValues() {
            Double revenueMerged = 0.0;
            Double revenueSum = 0.0;
            for (CaseDataItem item : caseItems) {
                if (item.isMerged()) {
                    revenueMerged = item.getRevenue();
                    continue;
                }
                revenueSum = revenueSum + item.getRevenue();
            }
            lblRevenueDelta.setText(Lang.toDecimal(revenueMerged - revenueSum, 2) + Lang.getCurrencySymbol());
            Pane tip = new RevenueDeltaLayout(revenueMerged, revenueSum);
            Pane pane = ExtendedInfoHelper.addInfoPane(lblRevenueDelta, tip, null);
            getChildren().remove(lblRevenueDelta);
            add(pane, 1, 0);
            GridPane.setHalignment(pane, HPos.RIGHT);
        }
    }
}
