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
package de.lb.cpx.client.app.wm.fx.process.section.details;

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.process.section.operations.WmProcessFinalisationOperations;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.server.commonDB.model.CMdkAuditreason;
import de.lb.cpx.server.commonDB.model.CWmListProcessResult;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmProcessHospital;
import de.lb.cpx.wm.model.TWmProcessHospitalFinalisation;
import java.util.List;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

/**
 * Detailclass to expose detail pane to the history, avoid doublicate source
 * code
 *
 * @author wilde
 */
public class WmProcessFinalisationDetails extends WmDetails<TWmProcessHospitalFinalisation> {

    public WmProcessFinalisationDetails(ProcessServiceFacade pFacade, TWmProcessHospitalFinalisation pItem) {
        super(pFacade, pItem);
    }

    @Override
    public String getDetailTitle() {
        return Lang.getEventNameProcessFinalisation(); //Vorgangsabschluss
    }

//    @Override
//    protected Button createMenuItem() {
//        //NOT IMPLEMENTED YET
//        return null;
//    }
    @Override
    protected Parent getDetailNode() {
        TWmProcessHospitalFinalisation result = ((TWmProcessHospital) facade.getCurrentProcess()).getProcessHospitalFinalisation();

        TitledPane tpGeneralInfos = new TitledPane();
        tpGeneralInfos.setText(Lang.getGeneral());

        GridPane gpInfos = new GridPane();
        gpInfos.setVgap(10.0);
        gpInfos.setHgap(10.0);

        Label lbClosedDateText = new Label("abgeschlossen am:");
        lbClosedDateText.getStyleClass().add("cpx-detail-label");
        lbClosedDateText.setWrapText(true);
        GridPane.setValignment(lbClosedDateText, VPos.TOP);
        Label lbClosedDate = new Label(Lang.toDate(result.getClosingDate()));
        lbClosedDate.setWrapText(true);
        GridPane.setValignment(lbClosedDate, VPos.TOP);

        Label lbMainReasonText = new Label(Lang.getAuditAuditReasons());
        lbMainReasonText.getStyleClass().add("cpx-detail-label");
        lbMainReasonText.setWrapText(true);
        GridPane.setValignment(lbMainReasonText, VPos.TOP);
        List<Long> mainAuditReasonList = result.getMainAuditReasonList();
        StringBuilder sbMainAuditReason = new StringBuilder();
        mainAuditReasonList.forEach((Long auditReason) -> {
            CMdkAuditreason mainAuditReason = MenuCache.getMenuCacheAuditReasons().get(auditReason);
            if (mainAuditReason != null) {
                sbMainAuditReason.append(mainAuditReason.getMdkArName()).append(", ");
            }
        });
        Label lbMainReason = new Label(sbMainAuditReason.length() > 0 ? sbMainAuditReason.substring(0, sbMainAuditReason.length() - 2) : "");
        lbMainReason.setWrapText(true);
        GridPane.setValignment(lbMainReason, VPos.TOP);

        Label lbFurtherReasonsText = new Label("Erweiterter Prüfgrunde");
        lbFurtherReasonsText.getStyleClass().add("cpx-detail-label");
        lbFurtherReasonsText.setWrapText(true);
        GridPane.setValignment(lbFurtherReasonsText, VPos.TOP);
        List<Long> auditReasonsExtendedList = result.getAuditReasonsExtendedList();
        StringBuilder sb = new StringBuilder();
        auditReasonsExtendedList.forEach((Long auditReason) -> {
            CMdkAuditreason mdkAuditreason1 = MenuCache.getMenuCacheAuditReasons().get(auditReason);
            if (mdkAuditreason1 != null) {
                sb.append(mdkAuditreason1.getMdkArName()).append(", ");
            }
        });
        Label lbFurtherReasons = new Label(sb.length() > 0 ? sb.substring(0, sb.length() - 2) : "");
        lbFurtherReasons.setWrapText(true);
        GridPane.setValignment(lbFurtherReasons, VPos.TOP);

        Label lbResultText = new Label("Ergebnis");
        lbResultText.getStyleClass().add("cpx-detail-label");
        lbResultText.setWrapText(true);
        GridPane.setValignment(lbResultText, VPos.TOP);
        long closingResult = result.getClosingResult();
        CWmListProcessResult processResult = MenuCache.getMenuCacheProcessResults().get(closingResult);
        Label lbResult = new Label(processResult != null ? processResult.getWmPrName() : "");
        lbResult.setWrapText(true);
        GridPane.setValignment(lbResult, VPos.TOP);

        Label lbDRGInitFinText = new Label("DRG initial/final");
        lbDRGInitFinText.getStyleClass().add("cpx-detail-label");
        lbDRGInitFinText.setWrapText(true);
        GridPane.setValignment(lbDRGInitFinText, VPos.TOP);
        Label lbDRGInitFin = new Label((result.getDrgInitial() != null ? result.getDrgInitial() : "") + " / " + (result.getDrgFinal() != null ? result.getDrgFinal() : ""));
        lbDRGInitFin.setWrapText(true);
        GridPane.setValignment(lbDRGInitFin, VPos.TOP);

        Label lbCWInitFinText = new Label("CW initial/final");
        lbCWInitFinText.getStyleClass().add("cpx-detail-label");
        lbCWInitFinText.setWrapText(true);
        GridPane.setValignment(lbCWInitFinText, VPos.TOP);

        Label lbCWInitFin = new Label(Lang.toDecimal(result.getCwInitial(), 3) + " / " + Lang.toDecimal(result.getCwFinal(), 3));   // up to 3 decimal places
        lbCWInitFin.setWrapText(true);
        GridPane.setValignment(lbCWInitFin, VPos.TOP);
        
        Label lbCareCWInitFinText = new Label("PflegeCW initial/final");
        lbCareCWInitFinText.getStyleClass().add("cpx-detail-label");
        lbCareCWInitFinText.setWrapText(true);
        GridPane.setValignment(lbCareCWInitFinText, VPos.TOP);

        Label lbCareCWInitFin = new Label(Lang.toDecimal(result.getCwCareInitial(), 4) + " / " + Lang.toDecimal(result.getCwCareFinal(), 4));   // up to 3 decimal places
        lbCareCWInitFin.setWrapText(true);
        GridPane.setValignment(lbCareCWInitFin, VPos.TOP);
        
        Label lbZEInitFinText = new Label("ZE-Betrag initial/final");
        lbZEInitFinText.getStyleClass().add("cpx-detail-label");
        lbZEInitFinText.setWrapText(true);
        GridPane.setValignment(lbZEInitFinText, VPos.TOP);
        Label lbZEInitFin = new Label(Lang.toDecimal(result.getInitialSupplementaryFee(), 2) + " " + Lang.getCurrencySymbol() + " / " + Lang.toDecimal(result.getFinalSupplementaryFee(), 2) + " " + Lang.getCurrencySymbol());
        lbZEInitFin.setWrapText(true);
        GridPane.setValignment(lbZEInitFin, VPos.TOP);

        Label lbVWDInitFinText = new Label("VWD initial/final");
        lbVWDInitFinText.getStyleClass().add("cpx-detail-label");
        lbVWDInitFinText.setWrapText(true);
        GridPane.setValignment(lbVWDInitFinText, VPos.TOP);
        Label lbVWDInitFin = new Label(result.getLosInitial() + " / " + result.getLosFinal());
        lbVWDInitFin.setWrapText(true);
        GridPane.setValignment(lbVWDInitFin, VPos.TOP);

        Label lbAvailsInitFinText = new Label("Erlös initial/final");
        lbAvailsInitFinText.getStyleClass().add("cpx-detail-label");
        lbAvailsInitFinText.setWrapText(true);
        GridPane.setValignment(lbAvailsInitFinText, VPos.TOP);
        Label lbAvailsInitFin = new Label(Lang.toDecimal(result.getRevenueInitial(), 2) + " " + Lang.getCurrencySymbol() + " / " + Lang.toDecimal(result.getRevenueFinal(), 2) + " " + Lang.getCurrencySymbol());
        lbAvailsInitFin.setWrapText(true);
        GridPane.setValignment(lbAvailsInitFin, VPos.TOP);

        Label lbDiffAvailsText = new Label("Differenz Erlös");
        lbDiffAvailsText.getStyleClass().add("cpx-detail-label");
        lbDiffAvailsText.setWrapText(true);
        GridPane.setValignment(lbDiffAvailsText, VPos.TOP);
        Label lbDiffAvails = new Label(Lang.toDecimal(result.getRevenueDiff(), 2) + " " + Lang.getCurrencySymbol());
        lbDiffAvails.setWrapText(true);
        GridPane.setValignment(lbDiffAvails, VPos.TOP);

        Label taCommentText = new Label(Lang.getComment());
        taCommentText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(taCommentText, VPos.TOP);

        Label taComment = new Label(result.getResultComment() != null ? result.getResultComment() : "");
        taComment.setWrapText(true);
        Pane spacePane = new Pane();
        HBox.setHgrow(spacePane, Priority.ALWAYS);

        gpInfos.add(lbClosedDateText, 0, 0);
        gpInfos.add(lbClosedDate, 1, 0);
        gpInfos.add(lbMainReasonText, 0, 1);
        gpInfos.add(lbMainReason, 1, 1);
        gpInfos.add(lbFurtherReasonsText, 0, 2);
        gpInfos.add(lbFurtherReasons, 1, 2);
        gpInfos.add(lbResultText, 0, 3);
        gpInfos.add(lbResult, 1, 3);
        gpInfos.add(lbDRGInitFinText, 0, 4);
        gpInfos.add(lbDRGInitFin, 1, 4);
        gpInfos.add(lbCWInitFinText, 0, 5);
        gpInfos.add(lbCWInitFin, 1, 5);
        gpInfos.add(lbCareCWInitFinText, 0, 6);
        gpInfos.add(lbCareCWInitFin, 1, 6);
        gpInfos.add(lbZEInitFinText, 0, 7);
        gpInfos.add(lbZEInitFin, 1, 7);
        gpInfos.add(lbVWDInitFinText, 0, 8);
        gpInfos.add(lbVWDInitFin, 1, 8);
        gpInfos.add(lbAvailsInitFinText, 0, 9);
        gpInfos.add(lbAvailsInitFin, 1, 9);
        gpInfos.add(lbDiffAvailsText, 0, 10);
        gpInfos.add(lbDiffAvails, 1, 10);
        gpInfos.add(taCommentText, 0, 11);
        gpInfos.add(taComment, 1, 11);

        ColumnConstraints columnConstraintHalf = new ColumnConstraints();
        columnConstraintHalf.setPercentWidth(50);

        //add row contrains to grab all space thats left for last row
        gpInfos.getColumnConstraints().add(columnConstraintHalf);
        gpInfos.getRowConstraints().add(new RowConstraints());

        tpGeneralInfos.setContent(gpInfos);

        return tpGeneralInfos;
    }

    @Override
    public WmProcessFinalisationOperations getOperations() {
        return new WmProcessFinalisationOperations(facade);
    }

}
