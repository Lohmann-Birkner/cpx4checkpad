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

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.process.section.operations.WmServiceOverviewOperations;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.catalog.CpxDepartmentCatalog;
import de.lb.cpx.client.core.model.catalog.CpxHospital;
import de.lb.cpx.client.core.util.ExtendedInfoHelper;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.enums.SupplFeeTypeEn;
import de.lb.cpx.service.ejb.SingleCaseGroupingEJBRemote;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmProcessCase;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;

/**
 * Detailclass to expose detail pane to the history, avoid doublicate source
 * code
 *
 * @author wilde
 */
public class WmServiceOverviewDetails extends WmDetails<TWmProcessCase> {

    private static final Logger LOG = Logger.getLogger(WmServiceOverviewDetails.class.getName());

    public WmServiceOverviewDetails(ProcessServiceFacade pFacade, TWmProcessCase pItem) {
        super(pFacade, pItem);
    }

    public WmServiceOverviewDetails(ProcessServiceFacade pFacade, TCase pItem) {
        super(pFacade, createFakeProcessCase(pItem));
    }

    public static TWmProcessCase createFakeProcessCase(TCase pItem) {
        TWmProcessCase pc = new TWmProcessCase();
        pc.setHosCase(pItem);
        return pc;
    }

//    public WmServiceOverviewDetails(ProcessServiceFacade pFacade) {
//        super(pFacade, null);
//    }
    @Override
    public String getDetailTitle() {
        return Lang.getEventNameCase() + ": " + item.getHosCase().getCsCaseNumber();
    }

    public TCase getCase() {
        return item.getHosCase();
    }

//    @Override
//    protected Button createMenuItem() {
//        //NOT IMPLEMENTED YET
//        return null;
//    }
    @Override
    protected Parent getDetailNode() {
        VBox detailContent = new VBox();
        final TCase cs = item.getHosCase();
        TCaseDetails currentLocal = facade.getCurrentLocal(cs.getId());//hCase.getCurrentLocal();
        TitledPane tpGenerelInfos = new TitledPane();
        tpGenerelInfos.setText(Lang.getCommonCaseData());
        GridPane gpInfos = new GridPane();
        gpInfos.setVgap(10.0);
        gpInfos.setHgap(10.0);

        Label caseSolvedText = new Label(Lang.getCaseResolved());
        caseSolvedText.getStyleClass().add("cpx-detail-label");
        caseSolvedText.setWrapText(true);
        GridPane.setValignment(caseSolvedText, VPos.TOP);
        Label caseSolved = new Label(cs.getCsStatusEn().isClosed() ? Lang.getConfirmationYes() : Lang.getConfirmationNo());
        GridPane.setValignment(caseSolved, VPos.TOP);

        Label caseCancelText = new Label(Lang.getSapReferenceTypeCancellation());
        caseCancelText.getStyleClass().add("cpx-detail-label");
        caseCancelText.setWrapText(true);
        GridPane.setValignment(caseCancelText, VPos.TOP);
        Label caseCancel = new Label(cs.getCsCancellationReasonEn() ? Lang.getConfirmationYes() : Lang.getConfirmationNo());
        if (cs.getCsCancellationReasonEn()) {
            caseCancel.getStyleClass().add("red-colored-label");
        }
        GridPane.setValignment(caseCancel, VPos.TOP);

        Label hospitalNameText = new Label(Lang.getHospitalName());
        hospitalNameText.getStyleClass().add("cpx-detail-label");
        hospitalNameText.setWrapText(true);
        GridPane.setValignment(hospitalNameText, VPos.TOP);
        CpxHospital hospital = facade.findHospitalByIdent(cs.getCsHospitalIdent());
        Label hospitalName = new Label(hospital.getHosName());
        hospitalName.setWrapText(true);
        GridPane.setValignment(hospitalName, VPos.TOP);

        Label hospitalIdentText = new Label(Lang.getHospitalIdent());
        hospitalIdentText.getStyleClass().add("cpx-detail-label");
        hospitalIdentText.setWrapText(true);
        GridPane.setValignment(hospitalIdentText, VPos.TOP);
        Label hospitalIdent = new Label(cs.getCsHospitalIdent());
        hospitalIdent.setWrapText(true);
        GridPane.setValignment(hospitalIdent, VPos.TOP);
        Label fullHospText = new Label();
//        fullHospText.setText(Lang.getHospitalIdentifier() + ": " + cs.getCsHospitalIdent() + "\n"
//                + Lang.getHospitalName() + ": " + (hospital.getHosName() != null ? hospital.getHosName() : "") + "\n"
//                + Lang.getAddress() + ": " + (hospital.getHosAddress() != null ? hospital.getHosAddress() : "") + "\n"
//                + Lang.getAddressCity() + ": " + (hospital.getHosZipCode() != null ? hospital.getHosZipCode() : "") + " " + (hospital.getHosCity() != null ? hospital.getHosCity() : ""));
////                fullHospText.setText(hospital.toString());
//        fullHospText.setWrapText(true);
        Pane graph = ExtendedInfoHelper.addInfoPane(hospitalIdent, getHospitalInfoGrid(cs.getCsHospitalIdent(), hospital), PopOver.ArrowLocation.TOP_RIGHT);
        graph.setMaxWidth(100);

        Label admDateText = new Label(Lang.getAdmissionDate());
        admDateText.getStyleClass().add("cpx-detail-label");
        Label admDate = new Label(currentLocal != null && currentLocal.getCsdAdmissionDate() != null ? Lang.toDate(currentLocal.getCsdAdmissionDate()) : "");

        Label admReasonText = new Label(Lang.getAdmissionReason());
        admReasonText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(admReasonText, VPos.TOP);
        Label admReason = new Label(currentLocal != null && currentLocal.getCsdAdmReason12En() != null ? currentLocal.getCsdAdmReason12En().toString() : "");
        admReason.setWrapText(true);

        Label admDepText = new Label(Lang.getAdmissionDepartmentShort());
        admDepText.getStyleClass().add("cpx-detail-label");
        TCaseDepartment admDepObj = currentLocal == null ? null : currentLocal.getCaseDepartmentAdmission();
        Label admDep = new Label(admDepObj != null ? admDepObj.getDepCodes() + " - " + CpxDepartmentCatalog.instance().getByCode(admDepObj.getDepKey301()).getDepDescription301() : "");
//                admDep.setTooltip(admDepObj != null
//                        ? new Tooltip(CpxDepartmentCatalog.instance().getByCode(admDepObj.getDepKey301()).getDepDescription301())
//                        : null);

        Label disDateText = new Label(Lang.getDischargeDate());
        disDateText.getStyleClass().add("cpx-detail-label");
        Label disDate = new Label(currentLocal != null && currentLocal.getCsdDischargeDate() != null ? Lang.toDate(currentLocal.getCsdDischargeDate()) : "");

        Label disReasonText = new Label(Lang.getDischargeReason());
        disReasonText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(disReasonText, VPos.TOP);
        Label disReason = new Label(currentLocal != null && currentLocal.getCsdDisReason12En() != null ? currentLocal.getCsdDisReason12En().toString() : "");
        disReason.setWrapText(true);

        Label disDepText = new Label(Lang.getDischargeDepartmentShort());
        disDepText.getStyleClass().add("cpx-detail-label");
        TCaseDepartment disDepObj = currentLocal == null ? null : currentLocal.getCaseDepartmentDischarge();
        Label disDep = new Label(disDepObj != null ? disDepObj.getDepCodes() + " - " + CpxDepartmentCatalog.instance().getByCode(disDepObj.getDepKey301()).getDepDescription301() : "");
//                disDep.setTooltip(disDepObj != null
//                        ? new Tooltip(CpxDepartmentCatalog.instance().getByCode(disDepObj.getDepKey301()).getDepDescription301())
//                        : null);

        TCaseIcd md = facade.getMainDiagnosisForVersion(currentLocal == null ? 0L : currentLocal.getId());
        Label mainDiagnosisText = new Label(Lang.getICDCode());
        mainDiagnosisText.getStyleClass().add("cpx-detail-label");
        Label mainDiagnosis = new Label(md != null ? md.getIcdcCode() : "");
        final Tooltip tooltip;
        if (md == null) {
            tooltip = null;
        } else {
            Date date = currentLocal != null && currentLocal.getCsdAdmissionDate() != null ? currentLocal.getCsdAdmissionDate() : new Date();
            tooltip = new Tooltip(facade.findIcdCatalogData(md.getIcdcCode(), Lang.toYear(date)).getDescription());
        }
        mainDiagnosis.setTooltip(tooltip);

        Label countSecDiagnosisText = new Label(Lang.getCountSD());
        countSecDiagnosisText.getStyleClass().add("cpx-detail-label");
        countSecDiagnosisText.setWrapText(true);
        GridPane.setValignment(countSecDiagnosisText, VPos.TOP);
        Label countSecDiagnosis = new Label(currentLocal != null ? String.valueOf(facade.getCountSecondaryDiagnosis(currentLocal.getId())) : "");
        GridPane.setValignment(countSecDiagnosis, VPos.TOP);

        Label countProcText = new Label(Lang.getCountProc());
        countProcText.getStyleClass().add("cpx-detail-label");
        countProcText.setWrapText(true);
        GridPane.setValignment(countProcText, VPos.TOP);
        Label countProc = new Label(currentLocal != null ? String.valueOf(facade.getCountProcedures(currentLocal.getId())) : "");
        GridPane.setValignment(countProc, VPos.TOP);

        int row = -1;

        gpInfos.add(caseSolvedText, 0, ++row);
        gpInfos.add(caseSolved, 1, row);

        gpInfos.add(caseCancelText, 0, ++row);
        gpInfos.add(caseCancel, 1, row);

        gpInfos.add(hospitalNameText, 0, ++row);
        gpInfos.add(hospitalName, 1, row);

        gpInfos.add(hospitalIdentText, 0, ++row);
        gpInfos.add(graph, 1, row);

        gpInfos.add(admDateText, 0, ++row);
        gpInfos.add(admDate, 1, row);

        gpInfos.add(admReasonText, 0, ++row);
        gpInfos.add(admReason, 1, row);

        gpInfos.add(admDepText, 0, ++row);
        gpInfos.add(admDep, 1, row);

        gpInfos.add(disDateText, 0, ++row);
        gpInfos.add(disDate, 1, row);

        gpInfos.add(disReasonText, 0, ++row);
        gpInfos.add(disReason, 1, row);

        gpInfos.add(disDepText, 0, ++row);
        gpInfos.add(disDep, 1, row);

        gpInfos.add(mainDiagnosisText, 0, ++row);
        gpInfos.add(mainDiagnosis, 1, row);

        gpInfos.add(countSecDiagnosisText, 0, ++row);
        gpInfos.add(countSecDiagnosis, 1, row);

        gpInfos.add(countProcText, 0, ++row);
        gpInfos.add(countProc, 1, row);

        tpGenerelInfos.setContent(gpInfos);

        TitledPane tpDrgInfos = new TitledPane();
        tpDrgInfos.setText(Lang.getDetailCaseData());
        TGroupingResults drgResults = currentLocal == null ? null : facade.getDrgResult(currentLocal.getId(), CpxClientConfig.instance().getSelectedGrouper());//md!=null?facade.getDrgResult(currentLocal.getId(),CpxClientConfig.instance().getSelectedGrouper(),md.getId()):null;

        GridPane gpDrg = getGrPresPane(drgResults, currentLocal);

        tpDrgInfos.setContent(gpDrg);

        ColumnConstraints columnConstraintHalf = new ColumnConstraints();
        columnConstraintHalf.setPercentWidth(50);

        gpInfos.getColumnConstraints().add(columnConstraintHalf);
        gpDrg.getColumnConstraints().add(columnConstraintHalf);

        detailContent.getChildren().addAll(tpGenerelInfos, tpDrgInfos);

        ScrollPane content = new ScrollPane(detailContent);

        detailContent.prefWidthProperty().bind(content.widthProperty().subtract(2));

        content.setFitToHeight(true);

        return content;
    }
    
    private GridPane getHospitalInfoGrid(String hosIdent, CpxHospital hospital){
 
        GridPane grid = new GridPane();
        ColumnConstraints columnConstraint1 = new ColumnConstraints();
        columnConstraint1.setPercentWidth(35);
        ColumnConstraints columnConstraint2 = new ColumnConstraints();
        columnConstraint2.setPercentWidth(65);

        grid.setVgap(10.0);
        grid.setHgap(10.0);
        grid.getColumnConstraints().addAll(columnConstraint1, columnConstraint2);
        grid.setPrefWidth(400);  
        int line = 0;        
        
        Label lblHosId = new Label(Lang.getHospitalIdentifier() + ": ");
        lblHosId.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblHosId, VPos.TOP);
        Label lblHosIdValue = new Label(hosIdent);
        lblHosIdValue.setAlignment(Pos.CENTER_RIGHT);

        grid.add(lblHosId, 0, line);
        grid.add(lblHosIdValue, 1, line++);
        if(hospital == null){
            return grid;
        }
        Label lblHosName = new Label(Lang.getHospitalName() + ":");
        lblHosName.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblHosName, VPos.TOP);
        Label lblHosNameValue = new Label(hospital.getHosName()== null?"":hospital.getHosName());
        lblHosNameValue.setAlignment(Pos.CENTER_RIGHT);
        lblHosNameValue.setWrapText(true);

        grid.add(lblHosName, 0, line);
        grid.add(lblHosNameValue, 1, line++);

        Label lblHosAddress = new Label(Lang.getAddress() + ":");
        lblHosAddress.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblHosAddress, VPos.TOP);
        Label lblHosAddressValue = new Label((hospital.getHosAddress() != null ? hospital.getHosAddress() : ""));
        lblHosAddressValue.setWrapText(true);
        lblHosAddressValue.setAlignment(Pos.CENTER_RIGHT);

        grid.add(lblHosAddress, 0, line);
        grid.add(lblHosAddressValue, 1, line++);

        Label lblHosCity = new Label(Lang.getAddressCity() + ":");
        lblHosCity.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblHosCity, VPos.TOP);
        Label lblHosCityValue = new Label((hospital.getHosZipCode() != null ? hospital.getHosZipCode() : "") + " " + (hospital.getHosCity() != null ? hospital.getHosCity() : ""));
        lblHosCityValue.setAlignment(Pos.CENTER_RIGHT);
        lblHosCityValue.setWrapText(true);
        
        grid.add(lblHosCity, 0, line);
        grid.add(lblHosCityValue, 1, line++);

        Label lblComment = new Label(Lang.getComment() + ":");
        lblComment.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblComment, VPos.TOP);        
        Label comment = new Label();
        comment.setWrapText(true);
        comment.setText(hospital.getHosComment()==null?"":hospital.getHosComment());
        grid.add(lblComment, 0, line);
        grid.add(comment, 1, line++);
        return grid;
    }

    private GridPane getGrPresPane(TGroupingResults drgResults, TCaseDetails currentLocal) {
        GridPane gpDrg = new GridPane();
        EjbProxy<SingleCaseGroupingEJBRemote> groupingBean = Session.instance().getEjbConnector().connectSingleCaseGroupingBean();
        gpDrg.setVgap(10.0);
        gpDrg.setHgap(10.0);
        if (drgResults == null) {
            return gpDrg;
        }
        switch (drgResults.getGrpresType()) {
            case DRG:
                setDrgLayout(gpDrg, drgResults, currentLocal, groupingBean.get());
                break;
            case PEPP:
                setPeppLayout(gpDrg, drgResults, currentLocal, groupingBean.get());
                break;
            default:
                LOG.log(Level.WARNING, "unknown type: {0}", drgResults.getGrpresType().name());
        }

        return gpDrg;
    }

    private void setDrgLayout(GridPane gpDrg, TGroupingResults drgResults, TCaseDetails currentLocal, SingleCaseGroupingEJBRemote groupingBean) {
        Label drgCodeText = new Label(Lang.getDrgShortcut());
        drgCodeText.getStyleClass().add("cpx-detail-label");
        Label drgCode = new Label(drgResults != null ? drgResults.getGrpresCode() : "");

        Label drgText = new Label(Lang.getDrgText());
        drgText.getStyleClass().add("cpx-detail-label");
        drgText.setWrapText(true);
        GridPane.setValignment(drgText, VPos.TOP);
        Label drg = new Label("");
        drg.setText(drgResults == null ? "drgResults is null!" : facade.getDrgDescriptionText(drgResults.getGrpresCode(), Lang.toYear(currentLocal.getCsdAdmissionDate())));
        drg.setWrapText(true);
        GridPane.setValignment(drg, VPos.TOP);

        Label pcclText = new Label(Lang.getPCCL());
        pcclText.getStyleClass().add("cpx-detail-label");
        Label pccl = new Label(String.valueOf(drgResults != null ? drgResults.getGrpresPccl() : ""));

//            Label suppFeeText = new Label(Lang.getSupplementaryValue());
//            suppFeeText.getStyleClass().add("cpx-detail-label");
//            suppFeeText.setWrapText(true);
//            gpDrg.setValignment(suppFeeText, VPos.TOP);
//            Label suppFee = new Label(String.valueOf(drgResults != null ? Lang.toDecimal(groupingBean.getSupplementaryValueForGroupingResultId(drgResults.getCaseDetails().getId(), true), 2) + " " + Lang.getCurrencySymbol() : ""));
//            gpDrg.setValignment(suppFee, VPos.TOP);
        gpDrg.add(drgCodeText, 0, 0);
        gpDrg.add(drgCode, 1, 0);

        gpDrg.add(drgText, 0, 1);
        gpDrg.add(drg, 1, 1);

        gpDrg.add(pcclText, 0, 2);
        gpDrg.add(pccl, 1, 2);

        setDrgFeeValues(gpDrg, currentLocal.getId(), groupingBean);
//            gpDrg.add(suppFeeText, 0, 3);
//            gpDrg.add(suppFee, 1, 3);
    }

    private void setPeppLayout(GridPane gpDrg, TGroupingResults drgResults, TCaseDetails currentLocal, SingleCaseGroupingEJBRemote groupingBean) {
        Label peppCodeText = new Label(Lang.getPEPP());
        peppCodeText.getStyleClass().add("cpx-detail-label");
        Label peppCode = new Label(drgResults != null ? drgResults.getGrpresCode() : "");

        Label peppText = new Label(Lang.getPeppText());
        peppText.getStyleClass().add("cpx-detail-label");
        peppText.setWrapText(true);
        GridPane.setValignment(peppText, VPos.TOP);
        Label pepp = new Label("");
        pepp.setText(drgResults == null ? "drgResults is null!" : facade.getPeppDescriptionText(drgResults.getGrpresCode(), Lang.toYear(currentLocal.getCsdAdmissionDate())));
        pepp.setWrapText(true);
        GridPane.setValignment(pepp, VPos.TOP);

        Label pcclText = new Label(Lang.getPCCL());
        pcclText.getStyleClass().add("cpx-detail-label");
        Label pccl = new Label(String.valueOf(drgResults != null ? drgResults.getGrpresPccl() : ""));

//            Label suppFeeText = new Label(Lang.getSupplementaryValue());
//            suppFeeText.getStyleClass().add("cpx-detail-label");
//            suppFeeText.setWrapText(true);
//            gpDrg.setValignment(suppFeeText, VPos.TOP);
//            Label suppFee = new Label(String.valueOf(drgResults != null ? Lang.toDecimal(groupingBean.getSupplementaryValueForGroupingResultId(drgResults.getCaseDetails().getId(), true), 2) + " " + Lang.getCurrencySymbol() : ""));
//            gpDrg.setValignment(suppFee, VPos.TOP);
        gpDrg.add(peppCodeText, 0, 0);
        gpDrg.add(peppCode, 1, 0);

        gpDrg.add(peppText, 0, 1);
        gpDrg.add(pepp, 1, 1);

        gpDrg.add(pcclText, 0, 2);
        gpDrg.add(pccl, 1, 2);
        setPeppFeeValues(gpDrg, currentLocal.getId(), groupingBean);
//            gpDrg.add(suppFeeText, 0, 3);
//            gpDrg.add(suppFee, 1, 3);
    }

    private void setDrgFeeValues(GridPane gpInfos, long pDetailsId, SingleCaseGroupingEJBRemote groupingBean) {
        Label labelSuppFeeText = new Label(Lang.getSupplementaryValue());
        labelSuppFeeText.getStyleClass().add("cpx-detail-label");
        Label labelSuppFee = new Label(getSfValue(groupingBean, pDetailsId));
        gpInfos.add(labelSuppFeeText, 0, 3);
        gpInfos.add(labelSuppFee, 1, 3);
        GridPane.setValignment(labelSuppFeeText, VPos.TOP);
        GridPane.setValignment(labelSuppFee, VPos.TOP);
    }

    private void setPeppFeeValues(GridPane gpInfos, long pDetailsId, SingleCaseGroupingEJBRemote groupingBean) {
        Label labelSpText = new Label(Lang.getSupplFeeValuePEPP());
        labelSpText.getStyleClass().add("cpx-detail-label");
        Label labelSp = new Label(getSpValue(groupingBean, pDetailsId));
        gpInfos.add(labelSpText, 0, 3);
        gpInfos.add(labelSp, 1, 3);
        GridPane.setValignment(labelSpText, VPos.TOP);
        GridPane.setValignment(labelSp, VPos.TOP);
        Label labelDfText = new Label(Lang.getDailyFeeValue());
        labelDfText.getStyleClass().add("cpx-detail-label");
        Label labelDf = new Label(getDfValue(groupingBean, pDetailsId));
        gpInfos.add(labelDfText, 0, 4);
        gpInfos.add(labelDf, 1, 4);
        GridPane.setValignment(labelDfText, VPos.TOP);
        GridPane.setValignment(labelDf, VPos.TOP);
    }

    private String getSfValue(SingleCaseGroupingEJBRemote groupingBean, long pDetailsId) {
        GDRGModel grouper = CpxClientConfig.instance().getSelectedGrouper();
        Double suppFee = groupingBean.getSupplFeeValue(grouper, pDetailsId, SupplFeeTypeEn.ZE);
        return Lang.toDecimal(suppFee, 2) + " " + Lang.getCurrencySymbol();
    }

    private String getSpValue(SingleCaseGroupingEJBRemote groupingBean, long pDetailsId) {
        GDRGModel grouper = CpxClientConfig.instance().getSelectedGrouper();
        Double suppFee = groupingBean.getSupplFeeValue(grouper, pDetailsId, SupplFeeTypeEn.ZP);
        return Lang.toDecimal(suppFee, 2) + " " + Lang.getCurrencySymbol();
    }

    private String getDfValue(SingleCaseGroupingEJBRemote groupingBean, long pDetailsId) {
        GDRGModel grouper = CpxClientConfig.instance().getSelectedGrouper();
        Double suppFee = groupingBean.getSupplFeeValue(grouper, pDetailsId, SupplFeeTypeEn.ET);
        return Lang.toDecimal(suppFee, 2) + " " + Lang.getCurrencySymbol();
    }

    @Override
    public WmServiceOverviewOperations getOperations() {
        return new WmServiceOverviewOperations(facade);
    }

}
