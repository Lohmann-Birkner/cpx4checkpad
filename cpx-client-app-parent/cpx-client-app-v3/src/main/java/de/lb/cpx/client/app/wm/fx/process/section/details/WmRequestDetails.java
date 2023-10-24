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
import de.lb.cpx.client.app.wm.fx.process.completion.risk.SaveOrUpdateRiskEvent;
import de.lb.cpx.client.app.wm.fx.process.model.FixedTable;
import de.lb.cpx.client.app.wm.fx.process.model.RiskFixedTable;
import de.lb.cpx.client.app.wm.fx.process.model.RiskTableItem;
import de.lb.cpx.client.app.wm.fx.process.section.operations.WmRequestOperations;
import de.lb.cpx.client.app.wm.util.DisplayHelper;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog;
import de.lb.cpx.client.core.model.catalog.CpxInsuranceCompany;
import de.lb.cpx.client.core.model.catalog.CpxInsuranceCompanyCatalog;
import de.lb.cpx.client.core.model.catalog.CpxMdk;
import de.lb.cpx.client.core.model.catalog.CpxMdkCatalog;
import de.lb.cpx.client.core.model.fx.button.AddButton;
import de.lb.cpx.client.core.model.fx.section.SectionHeader;
import de.lb.cpx.client.core.util.ExtendedInfoHelper;
import de.lb.cpx.client.core.util.OverrunHelper;
import de.lb.cpx.model.enums.PlaceOfRegEn;
import de.lb.cpx.model.enums.VersionRiskTypeEn;
import de.lb.cpx.server.commonDB.model.CMdkAuditreason;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmMdkAuditReasons;
import de.lb.cpx.wm.model.TWmRequest;
import de.lb.cpx.wm.model.TWmRequestAudit;
import de.lb.cpx.wm.model.TWmRequestBege;
import de.lb.cpx.wm.model.TWmRequestInsurance;
import de.lb.cpx.wm.model.TWmRequestMdk;
import de.lb.cpx.wm.model.TWmRequestOther;
import de.lb.cpx.wm.model.TWmRequestReview;
import de.lb.cpx.wm.model.TWmRisk;
import de.lb.cpx.wm.model.TWmRiskDetails;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.beans.binding.Bindings;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.controlsfx.control.PopOver;

/**
 * Detailclass to expose detail pane to the history, avoid doublicate source
 * code
 *
 * @author wilde
 */
public class WmRequestDetails extends WmDetails<TWmRequest> {

    public WmRequestDetails(ProcessServiceFacade pFacade, TWmRequest pItem) {
        super(pFacade, pItem);
    }

    @Override
    public String getDetailTitle() {
        return Lang.getEventNameRequest();
    }

    @Override
    protected Parent getDetailNode() {

        VBox content = new VBox();
        content.setSpacing(12);

        TitledPane tpGenerelInfos = new TitledPane();
        tpGenerelInfos.setText(Lang.getGeneral());

        GridPane gpInfos = new GridPane();
        gpInfos.setVgap(10.0);
        gpInfos.setHgap(10.0);

        Label labelUserText = new Label(Lang.getLoginUser());
        labelUserText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(labelUserText, VPos.TOP);
        Label labelUser = new Label(facade.getUserLabel(item.getCreationUser()) != null ? facade.getUserLabel(item.getCreationUser()) : ""); //2017-04-26 DNi - CPX-489: Show readable name
        labelUser.setWrapText(true);

//        Label labelCreationDateText = new Label(Lang.getRequestCreationDate());
//        labelCreationDateText.getStyleClass().add("cpx-detail-label");
//        labelCreationDateText.setWrapText(true);
//        Label labelCreationDate = new Label(Lang.toDate(item.getCreationDate()));
//        GridPane.setValignment(labelCreationDate, VPos.TOP);

        Label labelCreationDateText = new Label(Lang.getAuditProcessCreationDate());
        labelCreationDateText.getStyleClass().add("cpx-detail-label");
        labelCreationDateText.setWrapText(true);
        Label labelCreationDate = new Label(Lang.toDate(item.getStartAudit()));
        GridPane.setValignment(labelCreationDate, VPos.TOP);

        Label lblStatusText = new Label(Lang.getAuditStatus());
        lblStatusText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblStatusText, VPos.TOP);
        Label lblStatus = new Label(getRequestStatus(item));
        lblStatus.setWrapText(true);

        Label labelRequestTypeText = new Label(Lang.getWmRequesttype());
        labelRequestTypeText.getStyleClass().add("cpx-detail-label");

        Label lblRequestType = new Label("-");
        if (item.getRequestTypeEnum() != null) {
            lblRequestType.setText(item.getRequestTypeEnum().getTranslation().getValue());
        }

        Label labelCommentText = new Label(Lang.getComment());
        labelCommentText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(labelUserText, VPos.TOP);
        Label labelComment = new Label(item.getComment() != null ? item.getComment() : "");
        labelComment.setWrapText(true);

        gpInfos.add(labelUserText, 0, 0);
        gpInfos.add(labelUser, 1, 0);
        gpInfos.add(labelCreationDateText, 0, 1);
        gpInfos.add(labelCreationDate, 1, 1);
        gpInfos.add(labelRequestTypeText, 0, 2);
        gpInfos.add(lblRequestType, 1, 2);
        gpInfos.add(lblStatusText, 0, 3);
        gpInfos.add(lblStatus, 1, 3);
        gpInfos.add(labelCommentText, 0, 4);
        gpInfos.add(labelComment, 0, 5);
        GridPane.setColumnSpan(labelComment, 2);
        GridPane.setVgrow(labelComment, Priority.ALWAYS);
        ColumnConstraints columnConstraintHalf = new ColumnConstraints();
        columnConstraintHalf.setPercentWidth(50);

        gpInfos.getColumnConstraints().add(columnConstraintHalf);
        //Awi-20180323-CPX889
        //add row contrains to grab all space thats left for last row
        gpInfos.getRowConstraints().add(new RowConstraints());
        gpInfos.getRowConstraints().add(new RowConstraints());
        gpInfos.getRowConstraints().add(new RowConstraints());
        gpInfos.getRowConstraints().add(new RowConstraints());
        gpInfos.getRowConstraints().add(new RowConstraints(GridPane.USE_PREF_SIZE, GridPane.USE_COMPUTED_SIZE, Double.MAX_VALUE, Priority.ALWAYS, VPos.CENTER, true));

        tpGenerelInfos.setContent(gpInfos);

        // new titlepane to show details of a request
        TitledPane tpDetailsInfos = new TitledPane();
        tpDetailsInfos.setText("Anfrage Details");

        GridPane gpDetailsInfos = new GridPane();
        gpDetailsInfos.setVgap(10.0);
        gpDetailsInfos.setHgap(10.0);
        gpDetailsInfos.getColumnConstraints().add(columnConstraintHalf);

        final CpxInsuranceCompanyCatalog insuranceCatalog = CpxInsuranceCompanyCatalog.instance();

        if (item.getRequestType() != null) {
            switch (item.getRequestTypeEnum()) {
                case bege:
                    TWmRequestBege begeReq = (TWmRequestBege) item;

                    String insCompName = insuranceCatalog.findInsNameByInsuranceNumber(begeReq.getInsuranceIdentifier(), AbstractCpxCatalog.DEFAULT_COUNTRY);

                    Label labelInsCompText = new Label("BG-Name");
                    labelInsCompText.getStyleClass().add("cpx-detail-label");
                    GridPane.setValignment(labelInsCompText, VPos.TOP);
                    Label labelInsComp = new Label(insCompName);
                    labelInsComp.setWrapText(true);

                    Label labelIKText = new Label("IK-Nummer");
                    labelIKText.getStyleClass().add("cpx-detail-label");
                    GridPane.setValignment(labelIKText, VPos.TOP);
                    Label labelIK = new Label(begeReq.getInsuranceIdentifier());
                    labelIK.setWrapText(true);

                    Pane graph = ExtendedInfoHelper.addInfoPane(labelIK, getBegeDataGrid(begeReq), PopOver.ArrowLocation.TOP_CENTER);
                    graph.setMaxWidth(100);
                    
                    Label lblAuditReasonText = new Label(Lang.getAuditAuditReasons());
                    lblAuditReasonText.getStyleClass().add("cpx-detail-label");
                    GridPane.setValignment(lblAuditReasonText, VPos.TOP);
                    Label lblAuditReason = createAuditReasonLabel(begeReq,false);
//                    Label lblAuditReason = new Label();//
//                    List<Long> ids = new ArrayList<>();
//                    for (TWmMdkAuditReasons reason : item.getAuditReasons()) {
//                        ids.add(reason.getAuditReasonNumber());
//                    }
//                    lblAuditReason.setText(ids.stream().map(elem -> MenuCache.instance().getAuditReasonForNumber(elem)).collect(Collectors.joining(",\n")));
//                    lblAuditReason.setWrapText(true);
                            
                    Label lblReportStartDateText = new Label(Lang.getMdkReportCreationDate());
                    lblReportStartDateText.getStyleClass().add("cpx-detail-label");
                    GridPane.setValignment(lblReportStartDateText, VPos.TOP);
                    Label lblReportStartDate = new Label(Lang.toDate(item.getReportDate()));
                    lblReportStartDate.setWrapText(true);

                    Label labelEditorText = new Label("Ansprechpartner");
                    labelEditorText.getStyleClass().add("cpx-detail-label");
                    GridPane.setValignment(labelEditorText, VPos.TOP);
                    Label labelEditor = new Label(begeReq.getBegeEditor());
                    labelEditor.setWrapText(true);

                    Label labelTelNoText = new Label("Telefon Nummer");
                    labelTelNoText.getStyleClass().add("cpx-detail-label");
                    GridPane.setValignment(labelTelNoText, VPos.TOP);
                    Label labelTelNo = new Label(begeReq.getDirectPhone());
                    labelTelNo.setWrapText(true);

                    Label labelFaxNoText = new Label("FAX-Nummer");
                    labelFaxNoText.getStyleClass().add("cpx-detail-label");
                    GridPane.setValignment(labelFaxNoText, VPos.TOP);
                    Label labelFaxNo = new Label(begeReq.getDirectFax());
                    labelFaxNo.setWrapText(true);

                    gpDetailsInfos.add(labelInsCompText, 0, 0);
                    gpDetailsInfos.add(labelInsComp, 1, 0);
                    gpDetailsInfos.add(labelIKText, 0, 1);
//                    gpDetailsInfos.add(labelIK, 1, 1);
                    gpDetailsInfos.add(graph, 1, 1);
                    gpDetailsInfos.add(lblAuditReasonText, 0, 2);
                    gpDetailsInfos.add(lblAuditReason, 1, 2);
                    gpDetailsInfos.add(lblReportStartDateText, 0, 3);
                    gpDetailsInfos.add(lblReportStartDate, 1, 3);
                    gpDetailsInfos.add(labelEditorText, 0, 4);
                    gpDetailsInfos.add(labelEditor, 1, 4);
                    gpDetailsInfos.add(labelTelNoText, 0, 5);
                    gpDetailsInfos.add(labelTelNo, 1, 5);
                    gpDetailsInfos.add(labelFaxNoText, 0, 6);
                    gpDetailsInfos.add(labelFaxNo, 1, 6);

                    tpDetailsInfos.setContent(gpDetailsInfos);

                    content.getChildren().addAll(tpGenerelInfos, tpDetailsInfos);//comment);

                    break;
                case mdk:
                    tpDetailsInfos.setContent(fillRequestMdkData(gpDetailsInfos, (TWmRequestMdk) item));
                    Node node = fillRequestRiskData((TWmRequestMdk) item,true);
                    content.getChildren().addAll(tpGenerelInfos, tpDetailsInfos, node);
                    break;

                case audit:
                    tpDetailsInfos.setContent(fillRequestAuditData(gpDetailsInfos, (TWmRequestAudit) item));
                    content.getChildren().addAll(tpGenerelInfos, tpDetailsInfos);
                    break;
                case insurance:
                    tpDetailsInfos.setContent(fillRequestInsuranceData(gpDetailsInfos, (TWmRequestInsurance) item));
                    content.getChildren().addAll(tpGenerelInfos, tpDetailsInfos);
                    break;
                case other:
                    tpDetailsInfos.setContent(fillRequestOtherData(gpDetailsInfos, (TWmRequestOther) item));
                    content.getChildren().addAll(tpGenerelInfos, tpDetailsInfos);
                    break;
                case review:
                    tpDetailsInfos.setContent(fillRequestReviewData(gpDetailsInfos, (TWmRequestReview) item));
                    content.getChildren().addAll(tpGenerelInfos, tpDetailsInfos);
                    break;

            }
        }

        return content;
    }

    private GridPane fillRequestInsuranceData(GridPane pPane, TWmRequestInsurance pRequest) {

        final CpxInsuranceCompanyCatalog insuranceCatalog = CpxInsuranceCompanyCatalog.instance();
        String insCompName = insuranceCatalog.findInsNameByInsuranceNumber(pRequest.getInsuranceIdentifier(), AbstractCpxCatalog.DEFAULT_COUNTRY);

        Label labelInsCompText = new Label(Lang.getInsuranceName());
        labelInsCompText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(labelInsCompText, VPos.TOP);
        Label labelInsComp = new Label(insCompName);
        labelInsComp.setWrapText(true);

        Label labelIKText = new Label(Lang.getInsuranceIdent());
        labelIKText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(labelIKText, VPos.TOP);
        Label labelIK = new Label(pRequest.getInsuranceIdentifier());
        labelIK.setWrapText(true);
        Pane graph = ExtendedInfoHelper.addInfoPane(labelIK, getInsuranceDataGrid(pRequest), PopOver.ArrowLocation.TOP_CENTER);
        graph.setMaxWidth(100);
        
        Label lblAuditReasonText = new Label(Lang.getAuditAuditReasons());
        lblAuditReasonText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblAuditReasonText, VPos.TOP);
        Label lblAuditReason = createAuditReasonLabel(pRequest,false);
        
        Label labelEditorText = new Label(Lang.getAuditEditor());
        labelEditorText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(labelEditorText, VPos.TOP);
        Label labelEditor = new Label(pRequest.getEditor());
        labelEditor.setWrapText(true);

        Label lblInsuranceTypeText = new Label("Art der Versicherung");
        lblInsuranceTypeText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblInsuranceTypeText, VPos.TOP);
        Label lblInsuranceType = new Label(pRequest.isPublicInsured() ? "Gesetzlich" : "Privat");
        lblInsuranceType.setWrapText(true);

//        Label lblAuditDateText = new Label(Lang.getAuditProcessCreationDate());
//        lblAuditDateText.getStyleClass().add("cpx-detail-label");
//        GridPane.setValignment(lblAuditDateText, VPos.TOP);
//        Label lblAuditDate = new Label(Lang.toDate(pRequest.getStartAudit()));
//        lblAuditDate.setWrapText(true);

        Label lblAuditTypeText = new Label("Prüfart");
        lblAuditTypeText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblAuditTypeText, VPos.TOP);
        Label lblAuditType = new Label(pRequest.getAuditType().getTranslation().getValue());
        lblAuditType.setWrapText(true);

        Label lblReportStartDateText = new Label(Lang.getMdkReportCreationDate());
        lblReportStartDateText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblReportStartDateText, VPos.TOP);
        Label lblReportStartDate = new Label(Lang.toDate(pRequest.getReportDate()));
        lblReportStartDate.setWrapText(true);

//        Label lblStatusText = new Label(Lang.getAuditStatus());
//        lblStatusText.getStyleClass().add("cpx-detail-label");
//        GridPane.setValignment(lblStatusText, VPos.TOP);
//        Label lblStatus = new Label(getRequestStatus(pRequest));
//        lblStatus.setWrapText(true);

        Label lblResultText = new Label(Lang.getResult());
        lblResultText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblResultText, VPos.TOP);
        Label lblResult = new Label(pRequest.getResultComment());
        lblResult.setWrapText(true);

        pPane.add(labelInsCompText, 0, 0);
        pPane.add(labelInsComp, 1, 0);
        pPane.add(labelIKText, 0, 1);
        pPane.add(graph, 1, 1);
        pPane.add(lblAuditReasonText, 0, 2);
        pPane.add(lblAuditReason, 1, 2);
        pPane.add(labelEditorText, 0, 3);
        pPane.add(labelEditor, 1, 3);
        pPane.add(lblInsuranceTypeText, 0, 4);
        pPane.add(lblInsuranceType, 1, 4);
//        pPane.add(lblAuditDateText, 0, 4);
//        pPane.add(lblAuditDate, 1, 4);

        pPane.add(lblAuditTypeText, 0, 5);
        pPane.add(lblAuditType, 1, 5);
        pPane.add(lblReportStartDateText, 0, 6);
        pPane.add(lblReportStartDate, 1, 6);
//        pPane.add(lblStatusText, 0, 7);
//        pPane.add(lblStatus, 1, 7);
        pPane.add(lblResultText, 0, 7);
        pPane.add(lblResult, 0, 8);
        GridPane.setColumnSpan(lblResult, 2);
        GridPane.setVgrow(lblResult, Priority.ALWAYS);
        return pPane;
    }

    private GridPane fillRequestOtherData(GridPane pPane, TWmRequestOther pRequest) {
        Label labelRequestNameText = new Label("Anfrage-Name");
        labelRequestNameText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(labelRequestNameText, VPos.TOP);
        Label labelRequestName = new Label(pRequest.getRequestName());
        labelRequestName.setWrapText(true);

        String insCompName = pRequest.getInstitutionName();
        Label labelInsCompText = new Label("Anfrage-Stelle");
        labelInsCompText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(labelInsCompText, VPos.TOP);
        Label labelInsComp = new Label(insCompName);
        labelInsComp.setWrapText(true);

        Pane graph = ExtendedInfoHelper.addInfoPane(labelInsComp, getOtherDataGrid(pRequest), PopOver.ArrowLocation.TOP_CENTER);
        graph.setMaxWidth(100);

        Label labelEditorText = new Label(Lang.getAuditEditor());
        labelEditorText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(labelEditorText, VPos.TOP);
        Label labelEditor = new Label(pRequest.getEditor());
        labelEditor.setWrapText(true);

//        Label lblAuditDateText = new Label(Lang.getAuditProcessCreationDate());
//        lblAuditDateText.getStyleClass().add("cpx-detail-label");
//        GridPane.setValignment(lblAuditDateText, VPos.TOP);
//        Label lblAuditDate = new Label(Lang.toDate(pRequest.getStartAudit()));
//        lblAuditDate.setWrapText(true);

        Label lblAuditTypeText = new Label("Prüfart");
        lblAuditTypeText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblAuditTypeText, VPos.TOP);
        Label lblAuditType = new Label(pRequest.getAuditType().getTranslation().getValue());
        lblAuditType.setWrapText(true);

        Label lblAuditReasonText = new Label(Lang.getAuditAuditReasons());
        lblAuditReasonText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblAuditReasonText, VPos.TOP);
        Label lblAuditReason = createAuditReasonLabel(pRequest,false);

        Label lblReportStartDateText = new Label(Lang.getMdkReportCreationDate());
        lblReportStartDateText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblReportStartDateText, VPos.TOP);
        Label lblReportStartDate = new Label(Lang.toDate(pRequest.getReportDate()));
        lblReportStartDate.setWrapText(true);

//        Label lblStatusText = new Label(Lang.getAuditStatus());
//        lblStatusText.getStyleClass().add("cpx-detail-label");
//        GridPane.setValignment(lblStatusText, VPos.TOP);
//        Label lblStatus = new Label(getRequestStatus(pRequest));
//        lblStatus.setWrapText(true);

        Label lblResultText = new Label(Lang.getResult());
        lblResultText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblResultText, VPos.TOP);
        Label lblResult = new Label(pRequest.getResultComment());
        lblResult.setWrapText(true);

        pPane.add(labelRequestNameText, 0, 0);
        pPane.add(labelRequestName, 1, 0);

        pPane.add(labelInsCompText, 0, 1);
        pPane.add(graph, 1, 1);
        pPane.add(labelEditorText, 0, 2);
        pPane.add(labelEditor, 1, 2);
//        pPane.add(lblAuditDateText, 0, 3);
//        pPane.add(lblAuditDate, 1, 3);
        pPane.add(lblAuditTypeText, 0, 3);
        pPane.add(lblAuditType, 1, 3);
        pPane.add(lblAuditReasonText, 0, 4);
        pPane.add(lblAuditReason, 1, 4);
        pPane.add(lblReportStartDateText, 0, 5);
        pPane.add(lblReportStartDate, 1, 5);
//        pPane.add(lblStatusText, 0, 7);
//        pPane.add(lblStatus, 1, 7);
        pPane.add(lblResultText, 0, 6);
        pPane.add(lblResult, 0, 7);
        GridPane.setColumnSpan(lblResult, 2);
        GridPane.setVgrow(lblResult, Priority.ALWAYS);

        return pPane;
    }

    private String getRequestStatus(TWmRequest pRequest){
        if(pRequest == null){
            return "-";
        }
        Long internalId = pRequest.getRequestState();
        return Objects.requireNonNullElse(MenuCache.instance().getRequestStatesForInternalId(internalId!=null?internalId:0),"-");
    }
    
    private Label createAuditReasonLabel(TWmRequest pRequest, Boolean pExtended){
//        if(pRequest == null){
//            return new Label("-");
//        }
//        if(pRequest.getAuditReasons().isEmpty()){
//            return new Label("-");
//        }
//        Label lblAuditReason = new Label();//
//        List<Long> ids = new ArrayList<>();
//        for (TWmMdkAuditReasons reason : pRequest.getAuditReasons()) {
//            ids.add(reason.getAuditReasonNumber());
//        }
//        lblAuditReason.setText(ids.stream().map(elem -> MenuCache.instance().getAuditReasonForNumber(elem)).collect(Collectors.joining(",\n")));
//        lblAuditReason.setWrapText(true);
//        return lblAuditReason;
        return DisplayHelper.createAuditReasonLabel(pRequest, ",\n" ,pExtended);
    }
    
    private Node fillRequestMdkData(GridPane pPane, TWmRequestMdk pRequest) {
        final CpxMdk mdkCatalog = CpxMdkCatalog.instance().getByInternalId(pRequest.getMdkInternalId(), AbstractCpxCatalog.DEFAULT_COUNTRY);

        Label lblMdkNameText = new Label(Lang.getMdkName());
        lblMdkNameText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblMdkNameText, VPos.TOP);
        //AWi: remove wrap text to allow more readable text overrun computation for javafx texts
        //otherwise text in label appear as WORD_ELLIPSIS
        Label lblMdkName = new Label(mdkCatalog.getMdkName());
        Label fullMdkFull = new Label();
        fullMdkFull.setWrapText(true);
        Pane graph = ExtendedInfoHelper.addInfoPane(lblMdkName, getMdkDataGrid(mdkCatalog), PopOver.ArrowLocation.TOP_CENTER);
        Label lblMdkStateText = new Label(Lang.getMdkStatus());
        lblMdkStateText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblMdkStateText, VPos.TOP);
        
        Label lblMdkState = new Label(MenuCache.getMenuCacheRequestStates().getName(pRequest.getRequestState()));

        Label lblMdkResultText = new Label(Lang.getMdkComment());
        lblMdkResultText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblMdkResultText, VPos.TOP);
        Label lblMdkResult = new Label(pRequest.getUserComment());
        lblMdkResult.setWrapText(true);

        Label lblMdkAuditReasonsText = createLabel4Text(Lang.getMdkAuditReasons());

//        Label lblMdkAuditReasons = new Label();
//
////        List<Long> auditReasonsInternalIds = new ArrayList<>();
////        for (TWmMdkAuditReasons reason : pRequest.getAuditReasons(false)) {
////            auditReasonsInternalIds.add(reason.getAuditReasonNumber());
////        }
////        String text = auditReasonsInternalIds.stream().map(elem -> MenuCache.instance().getAuditReasonForNumber(elem)).collect(Collectors.joining(", "));
////        String text4Overrun = auditReasonsInternalIds.stream().map(elem -> MenuCache.instance().getAuditReasonForNumber(elem)).collect(Collectors.joining("\n"));
//        lblMdkAuditReasons.setText(DisplayHelper.createAuditReasonInitialText(pRequest, ", "));
//        HBox helpOverrun = new HBox();
//        helpOverrun.getChildren().add(lblMdkAuditReasons);
//        OverrunHelper.addOverrunInfoButton(lblMdkAuditReasons, DisplayHelper.createAuditReasonInitialText(pRequest, "\n"), true);
        //AWi: add condition to bind graph width not to zero (or near zero) value when no audit reason is set by user
        //should not occure but  when it seems like a bug
        //graph.prefWidthProperty().bind(helpOverrun.widthProperty());
        HBox helpOverrun = createAuditReasonsHbox(pRequest);
        graph.prefWidthProperty().bind(Bindings.when(helpOverrun.widthProperty().lessThanOrEqualTo(10.0)).then(Label.USE_COMPUTED_SIZE).otherwise(helpOverrun.widthProperty()));

        Label lblMdkExtendedAuditReasonsText = new Label(Lang.getMdkExtendedAuditReasons());
        lblMdkExtendedAuditReasonsText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblMdkExtendedAuditReasonsText, VPos.TOP);
        Label lblMdkExtendedAuditReasons = new Label();
//        List<Long> extendesAuditReasonsInternalIds = new ArrayList<>();
//        for (TWmMdkAuditReasons reason : pRequest.getAuditReasons(true)) {
//            extendesAuditReasonsInternalIds.add(reason.getAuditReasonNumber());
//        }
//        text = extendesAuditReasonsInternalIds.stream().map(elem -> MenuCache.instance().getAuditReasonForNumber(elem)).collect(Collectors.joining(", "));
//        text4Overrun = extendesAuditReasonsInternalIds.stream().map(elem -> MenuCache.instance().getAuditReasonForNumber(elem)).collect(Collectors.joining("\n"));
        lblMdkExtendedAuditReasons.setText(DisplayHelper.createAuditReasonExtendedText(pRequest, ", "));
        HBox helpOverrun1 = new HBox();
        helpOverrun1.getChildren().add(lblMdkExtendedAuditReasons);
        helpOverrun1.prefWidthProperty().bind(helpOverrun.widthProperty());
        OverrunHelper.addOverrunInfoButton(lblMdkExtendedAuditReasons, DisplayHelper.createAuditReasonInitialText(pRequest, "\n"), true);


        Label lblMdkStartInsuranceText = new Label(Lang.getMdkProcessStartDate());
        lblMdkStartInsuranceText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblMdkStartInsuranceText, VPos.TOP);
        Label lblMdkStartInshuranceAudit = new Label(Lang.toDate(pRequest.getStartAudit()));

        Label lblMdkStartAuditText = new Label(Lang.getMdkStartAudit());
        lblMdkStartAuditText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblMdkStartAuditText, VPos.TOP);
        Label lblMdkStartAudit = new Label(Lang.toDate(pRequest.getMdkStartAudit()));

        Label lblMdkBillCorrectionDeadlineText = new Label(Lang.getMdkBillCorrectionDeadline());
        lblMdkBillCorrectionDeadlineText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblMdkBillCorrectionDeadlineText, VPos.TOP);
        Label lblMdkBillCorrectionDeadline = new Label(Lang.toDate(pRequest.getBillCorrectionDeadline()));

        Label lblMdkAuditCompletionDeadlineText = new Label(Lang.getMdkAuditCompletionDeadline());
        lblMdkAuditCompletionDeadlineText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblMdkAuditCompletionDeadlineText, VPos.TOP);
        Label lblMdkAuditCompletionDeadline = new Label(Lang.toDate(pRequest.getMdkAuditCompletionDeadline()));

        Label lblQuotaCheckExceeded = new Label(Lang.getAuditQuotaResultExceededExaminationQuota());
        lblQuotaCheckExceeded.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblQuotaCheckExceeded, VPos.TOP);
        final Label lblQuotaCheckExceededValue;
        switch (pRequest.getMdkRequestQuotaExceededFl()) {
            case TWmRequestMdk.QUOTA_NOT_RELEVANT:
                lblQuotaCheckExceededValue = new Label(Lang.getAdmissionModeNotRelevant());
                break;
            case TWmRequestMdk.QUOTA_NOT_EXCEEDED:
                lblQuotaCheckExceededValue = new Label(Lang.getConfirmationNo());
                break;
            case TWmRequestMdk.QUOTA_EXCEEDED:
                lblQuotaCheckExceededValue = new Label(Lang.getConfirmationYes());
                lblQuotaCheckExceededValue.pseudoClassStateChanged(PseudoClass.getPseudoClass("error"), true);
                break;
            default:
                lblQuotaCheckExceededValue = new Label("illegal value found: " + pRequest.getMdkRequestQuotaExceededFl());
                break;
        }
        Pane graphQuota = ExtendedInfoHelper.addInfoPane(lblQuotaCheckExceededValue, getGridPaneForMDKCheckQuota(pRequest), PopOver.ArrowLocation.TOP_CENTER);
        graphQuota.prefWidthProperty().bind(helpOverrun.widthProperty());
        
        int line = 0;
        pPane.add(lblMdkNameText, 0, line);
        pPane.add(graph, 1, line++);
        
        pPane.add(lblQuotaCheckExceeded, 0, line);
        if (pRequest.getMdkRequestQuotaExceededFl() == TWmRequestMdk.QUOTA_NOT_RELEVANT) {
            pPane.add(lblQuotaCheckExceededValue, 1, line++);
        } else {
            pPane.add(graphQuota, 1, line++);
        }
        
        pPane.add(lblMdkStateText, 0, line);
        pPane.add(lblMdkState, 1, line++);

        pPane.add(lblMdkAuditReasonsText, 0, line);
        pPane.add(helpOverrun, 1, line++);

        pPane.add(lblMdkExtendedAuditReasonsText, 0, line);
        pPane.add(helpOverrun1, 1, line++);

        pPane.add(lblMdkStartInsuranceText, 0, line);
        pPane.add(lblMdkStartInshuranceAudit, 1, line++);
        
        pPane.add(lblMdkStartAuditText, 0, line);
        pPane.add(lblMdkStartAudit, 1, line++);

        pPane.add(lblMdkBillCorrectionDeadlineText, 0, line);
        pPane.add(lblMdkBillCorrectionDeadline, 1, line++);

        pPane.add(lblMdkAuditCompletionDeadlineText, 0, line);
        pPane.add(lblMdkAuditCompletionDeadline, 1, line++);

        pPane.add(lblMdkResultText, 0, line++);
        //pPane.add(lblMdkResult, 1, 7);
        pPane.add(lblMdkResult, 0, line++);

        GridPane.setColumnSpan(lblMdkResult, 2);
        GridPane.setVgrow(lblMdkResult, Priority.ALWAYS);

        ScrollPane sp = new ScrollPane(pPane);
        sp.setPadding(new Insets(12));
        sp.setFitToHeight(true);
        sp.setFitToWidth(true);
        return sp;
    }
    
    private GridPane getMdkDataGrid(CpxMdk mdkCatalog){
//        final CpxMdk mdkCatalog = CpxMdkCatalog.instance().getByInternalId(pRequest.getMdkInternalId(), AbstractCpxCatalog.DEFAULT_COUNTRY);

        GridPane grid = new GridPane();
        if(mdkCatalog == null){
            return grid;
        }
        ColumnConstraints columnConstraintHalf = new ColumnConstraints();
        columnConstraintHalf.setPercentWidth(50);

        grid.setVgap(10.0);
        grid.setHgap(10.0);
        grid.getColumnConstraints().add(columnConstraintHalf);
        grid.setPrefWidth(400);  
        int line = 0;        
        Label lblmdName = new Label(Lang.getMdkName());
        lblmdName.getStyleClass().add("cpx-detail-label");
//        lblHospitalIdent.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblmdName, VPos.TOP);
        Label lblMdNameValue = new Label(mdkCatalog.getMdkName());
        lblMdNameValue.setAlignment(Pos.CENTER_RIGHT);

        grid.add(lblmdName, 0, line);
        grid.add(lblMdNameValue, 1, line++);
        
        Label lblMdDepartment = new Label(Lang.getMdkDepartment());
        lblMdDepartment.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblMdDepartment, VPos.TOP);
        Label lblMdDepartmentValue = new Label(mdkCatalog.getMdkDepartment());
        lblMdDepartmentValue.setAlignment(Pos.CENTER_RIGHT);
        
        grid.add(lblMdDepartment, 0, line);
        grid.add(lblMdDepartmentValue, 1, line++);
        
        Label lblMdAdress = new Label(Lang.getMdkAddress());
        lblMdAdress.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblMdAdress, VPos.TOP);
        Label lblMdAdressValue = new Label(mdkCatalog.getMdkStreet());
        lblMdAdressValue.setAlignment(Pos.CENTER_RIGHT);
        
        grid.add(lblMdAdress, 0, line);
        grid.add(lblMdAdressValue, 1, line++);

        Label lblMdAreaCode = new Label(Lang.getMdkAreaCode());
        lblMdAreaCode.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblMdAreaCode, VPos.TOP);
        Label lblMdAreaCodeValue = new Label(mdkCatalog.getMdkZipCode());
        lblMdAreaCodeValue.setAlignment(Pos.CENTER_RIGHT);
        
        grid.add(lblMdAreaCode, 0, line);
        grid.add(lblMdAreaCodeValue, 1, line++);

        Label lblMdPhone = new Label(Lang.getMdkTelephone());
        lblMdPhone.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblMdPhone, VPos.TOP);
        Label lblMdPhoneValue = new Label(mdkCatalog.getMdkPhonePrefix() + mdkCatalog.getMdkPhone());
        lblMdPhoneValue.setAlignment(Pos.CENTER_RIGHT);
        
        grid.add(lblMdPhone, 0, line);
        grid.add(lblMdPhoneValue, 1, line++);

        Label lblMdFax = new Label(Lang.getMdkFax());
        lblMdFax.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblMdFax, VPos.TOP);
        Label lblMdFaxValue = new Label(mdkCatalog.getMdkFax());
        lblMdFaxValue.setAlignment(Pos.CENTER_RIGHT);
        
        grid.add(lblMdFax, 0, line);
        grid.add(lblMdFaxValue, 1, line++);

        Label lblMdMail = new Label(Lang.getMdkEmail() );
        lblMdMail.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblMdMail, VPos.TOP);
        Label lblMdMailValue = new Label(mdkCatalog.getMdkEmail());
        lblMdMailValue.setAlignment(Pos.CENTER_RIGHT);
        
        grid.add(lblMdMail, 0, line);
        grid.add(lblMdMailValue, 1, line);

        
        return grid;
    }

    private GridPane getInsuranceDataGrid(TWmRequestInsurance pRequest){

        final CpxInsuranceCompanyCatalog insuranceCatalog = CpxInsuranceCompanyCatalog.instance();
        final CpxInsuranceCompany insComp = insuranceCatalog.getByCode(pRequest.getInsuranceIdentifier(), AbstractCpxCatalog.DEFAULT_COUNTRY);

        GridPane grid = new GridPane();
        if(insComp == null){
            return grid;
        }
        ColumnConstraints columnConstraintHalf = new ColumnConstraints();
        columnConstraintHalf.setPercentWidth(50);

        grid.setVgap(10.0);
        grid.setHgap(10.0);
        grid.getColumnConstraints().add(columnConstraintHalf);
        grid.setPrefWidth(400);  
        int line = 0;        
        Label lblInsIdent = new Label(Lang.getInsuranceIdent());
        lblInsIdent.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblInsIdent, VPos.TOP);
        Label lblInsIdentValue = new Label(insComp.getInscIdent());
        lblInsIdentValue.setAlignment(Pos.CENTER_RIGHT);

        grid.add(lblInsIdent, 0, line);
        grid.add(lblInsIdentValue, 1, line++);
        
        Label lblInscName = new Label(Lang.getInsuranceName());
        lblInscName.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblInscName, VPos.TOP);
        Label lblInscNameValue = new Label(insComp.getInscName());
        lblInscNameValue.setAlignment(Pos.CENTER_RIGHT);
        lblInscNameValue.setWrapText(true);
        
        grid.add(lblInscName, 0, line);
        grid.add(lblInscNameValue, 1, line++);
        
        Label lblInscKind = new Label(Lang.getInsuranceType());
        lblInscKind.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblInscKind, VPos.TOP);
        Label lblInscKindValue = new Label(insComp.getInscShort() != null ? insComp.getInscShort() : "-");
        lblInscKind.setAlignment(Pos.CENTER_RIGHT);
        
        grid.add(lblInscKind, 0, line);
        grid.add(lblInscKindValue, 1, line++);

        Label lblInscAdress = new Label(Lang.getAddress());
        lblInscAdress.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblInscAdress, VPos.TOP);
        Label lblInscAdressValue = new Label(insComp.getInscAddress());
        lblInscAdressValue.setAlignment(Pos.CENTER_RIGHT);
        
        grid.add(lblInscAdress, 0, line);
        grid.add(lblInscAdressValue, 1, line++);

        Label lblInscAdressCity = new Label(Lang.getAddressCity());
        lblInscAdressCity.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblInscAdressCity, VPos.TOP);
        String adressCity = (insComp.getInscZipCode()!=null?(insComp.getInscZipCode()+ " "):"") + insComp.getInscCity();
        Label lblInscAdressCityValue = new Label(adressCity);
        lblInscAdressCityValue.setAlignment(Pos.CENTER_RIGHT);
        
        grid.add(lblInscAdressCity, 0, line);
        grid.add(lblInscAdressCityValue, 1, line++);

        return grid;
    }

    private GridPane getAuditDataGrid(CpxInsuranceCompany auditComp){
//
//        final CpxInsuranceCompanyCatalog insuranceCatalog = CpxInsuranceCompanyCatalog.instance();
//        final CpxInsuranceCompany auditComp = insuranceCatalog.getByCode(pRequest.getInsuranceIdentifier(), AbstractCpxCatalog.DEFAULT_COUNTRY);

        GridPane grid = new GridPane();
        if(auditComp == null){
            return grid;
        }
        ColumnConstraints columnConstraintHalf = new ColumnConstraints();
        columnConstraintHalf.setPercentWidth(50);

        grid.setVgap(10.0);
        grid.setHgap(10.0);
        grid.getColumnConstraints().add(columnConstraintHalf);
        grid.setPrefWidth(400);  
        int line = 0;        
        Label lblAuditIdent = new Label(Lang.getInsuranceIdent());
        lblAuditIdent.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblAuditIdent, VPos.TOP);
        Label lblAuditIdentValue = new Label(auditComp.getInscIdent());
        lblAuditIdentValue.setAlignment(Pos.CENTER_RIGHT);

        grid.add(lblAuditIdent, 0, line);
        grid.add(lblAuditIdentValue, 1, line++);
        
        Label lblAuditName = new Label(Lang.getInsuranceName());
        lblAuditName.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblAuditName, VPos.TOP);
        Label lblAuditNameValue = new Label(auditComp.getInscName());
        lblAuditNameValue.setAlignment(Pos.CENTER_RIGHT);
        lblAuditNameValue.setWrapText(true);
        
        grid.add(lblAuditName, 0, line);
        grid.add(lblAuditNameValue, 1, line++);
        
        Label lblAuditKind = new Label(Lang.getInsuranceType());
        lblAuditKind.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblAuditKind, VPos.TOP);
        Label lblAuditKindValue = new Label(auditComp.getInscShort() != null ? auditComp.getInscShort() : "-");
        lblAuditKind.setAlignment(Pos.CENTER_RIGHT);
        
        grid.add(lblAuditKind, 0, line);
        grid.add(lblAuditKindValue, 1, line++);

        Label lblAuditAdress = new Label(Lang.getAddress());
        lblAuditAdress.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblAuditAdress, VPos.TOP);
        Label lblAuditAdressValue = new Label(auditComp.getInscAddress());
        lblAuditAdressValue.setAlignment(Pos.CENTER_RIGHT);
        lblAuditAdressValue.setWrapText(true);
        
        grid.add(lblAuditAdress, 0, line);
        grid.add(lblAuditAdressValue, 1, line++);

        Label lblAuditAdressCity = new Label(Lang.getAddressCity());
        lblAuditAdressCity.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblAuditAdressCity, VPos.TOP);
        String adressCity = (auditComp.getInscZipCode()!=null?(auditComp.getInscZipCode()+ " "):"") + auditComp.getInscCity();
        Label lblAuditAdressCityValue = new Label(adressCity);
        lblAuditAdressCityValue.setAlignment(Pos.CENTER_RIGHT);
        
        grid.add(lblAuditAdressCity, 0, line);
        grid.add(lblAuditAdressCityValue, 1, line++);

        return grid;
    }

    private GridPane getBegeDataGrid(TWmRequestBege pRequest){

        final CpxInsuranceCompanyCatalog insuranceCatalog = CpxInsuranceCompanyCatalog.instance();
        final CpxInsuranceCompany begeComp = insuranceCatalog.getByCode(pRequest.getInsuranceIdentifier(), AbstractCpxCatalog.DEFAULT_COUNTRY);

        GridPane grid = new GridPane();
        if(begeComp == null){
            return grid;
        }
        ColumnConstraints columnConstraintHalf = new ColumnConstraints();
        columnConstraintHalf.setPercentWidth(50);

        grid.setVgap(10.0);
        grid.setHgap(10.0);
        grid.getColumnConstraints().add(columnConstraintHalf);
        grid.setPrefWidth(400);  
        int line = 0;        

        Label lblBegeAdress = new Label(Lang.getAddress());
        lblBegeAdress.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblBegeAdress, VPos.TOP);
        Label lblBegeAdressValue = new Label(begeComp.getInscAddress());
        lblBegeAdressValue.setAlignment(Pos.CENTER_RIGHT);
        lblBegeAdressValue.setWrapText(true);
        
        grid.add(lblBegeAdress, 0, line);
        grid.add(lblBegeAdressValue, 1, line++);

        Label lblBegeAdressCity = new Label(Lang.getAddressCity());
        lblBegeAdressCity.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblBegeAdressCity, VPos.TOP);
        String adressCity = (begeComp.getInscZipCode()!=null?(begeComp.getInscZipCode()+ " "):"") + begeComp.getInscCity();
        Label lblBegeAdressCityValue = new Label(adressCity);
        lblBegeAdressCityValue.setAlignment(Pos.CENTER_RIGHT);
        
        grid.add(lblBegeAdressCity, 0, line);
        grid.add(lblBegeAdressCityValue, 1, line++);

        return grid;
    }

    private GridPane getOtherDataGrid(TWmRequestOther pRequest){

        GridPane grid = new GridPane();
        ColumnConstraints columnConstraintHalf = new ColumnConstraints();
        columnConstraintHalf.setPercentWidth(50);

        grid.setVgap(10.0);
        grid.setHgap(10.0);
        grid.getColumnConstraints().add(columnConstraintHalf);
        grid.setPrefWidth(400);  
        int line = 0;        
        Label lblOtherName = new Label("Anfrage-Stelle");
        lblOtherName.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblOtherName, VPos.TOP);
        Label lblOtherNameValue = new Label(pRequest.getInstitutionName());
        lblOtherNameValue.setAlignment(Pos.CENTER_RIGHT);

        grid.add(lblOtherName, 0, line);
        grid.add(lblOtherNameValue, 1, line++);
        
        Label lblOtherPhone = new Label(Lang.getAddressPhoneNumber());
        lblOtherPhone.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblOtherPhone, VPos.TOP);
//        Label lblOtherPhoneValue = new Label((pRequest.getInstitutionAreaCode() != null ? pRequest.getInstitutionAreaCode() : "") 
//                                               + pRequest.getInstitutionTelefon() != null ? pRequest.getInstitutionTelefon() : "");
        Label lblOtherPhoneValue = new Label(new StringBuilder().append(Objects.requireNonNullElse(pRequest.getInstitutionAreaCode(), ""))
                .append(Objects.requireNonNullElse(pRequest.getInstitutionTelefon(), ""))
                .toString());
        lblOtherPhone.setAlignment(Pos.CENTER_RIGHT);
        lblOtherPhone.setWrapText(true);

        grid.add(lblOtherPhone, 0, line);
        grid.add(lblOtherPhoneValue, 1, line++);

        Label lblOtherAdress = new Label(Lang.getAddress());
        lblOtherAdress.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblOtherAdress, VPos.TOP);
        Label lblOtherAdressValue = new Label(pRequest.getInstitutionAddress() != null ? pRequest.getInstitutionAddress() : "");
        lblOtherAdressValue.setAlignment(Pos.CENTER_RIGHT);
        lblOtherAdressValue.setWrapText(true);

        grid.add(lblOtherAdress, 0, line);
        grid.add(lblOtherAdressValue, 1, line++);

        Label lblOtherAdressCity = new Label(Lang.getAddressCity());
        lblOtherAdressCity.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblOtherAdressCity, VPos.TOP);
//        String adressCity = pRequest.getInstitutionZipCode() != null ? pRequest.getInstitutionZipCode() : "" 
//                            + " " + pRequest.getInstitutionCity() != null ? pRequest.getInstitutionCity() : "";
        String adressCity = new StringBuilder().append(Objects.requireNonNullElse(pRequest.getInstitutionZipCode(), ""))
                .append(" ")
                .append(Objects.requireNonNullElse(pRequest.getInstitutionCity(), ""))
                .toString();
        
        Label lblOtherAdressCityValue = new Label(adressCity);
        lblOtherAdressCityValue.setAlignment(Pos.CENTER_RIGHT);
        
        grid.add(lblOtherAdressCity, 0, line);
        grid.add(lblOtherAdressCityValue, 1, line++);

        return grid;
    }

    private GridPane getGridPaneForMDKCheckQuota(TWmRequestMdk pRequest) {

        GridPane gpExaminationQuota = new GridPane();

        ColumnConstraints columnConstraintHalf = new ColumnConstraints();
        columnConstraintHalf.setPercentWidth(50);

        gpExaminationQuota.setVgap(10.0);
        gpExaminationQuota.setHgap(10.0);
        gpExaminationQuota.getColumnConstraints().add(columnConstraintHalf);
        gpExaminationQuota.setPrefWidth(400);

        Label lblHospitalIdent = new Label(Lang.getHospitalIdentObj().getAbbreviation());
        lblHospitalIdent.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblHospitalIdent, VPos.TOP);
        Label lblHospitalIdentValue = new Label(pRequest.getMdkQuotaHospitalIdent());
        lblHospitalIdentValue.setAlignment(Pos.CENTER_RIGHT);

        Label lblInsuranceIdent = new Label(Lang.getAuditIdentObj().getAbbreviation());
        lblInsuranceIdent.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblInsuranceIdent, VPos.TOP);
        Label lblInsuranceIdentValue = new Label(pRequest.getMdkQuotaInsuranceIdent());

        Label lblAuditQuotaResultQuarterYear = new Label(Lang.getAuditQuotaResultQuarterYearMDRequest());
        lblAuditQuotaResultQuarterYear.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblAuditQuotaResultQuarterYear, VPos.TOP);
        lblAuditQuotaResultQuarterYear.setWrapText(true);
        lblAuditQuotaResultQuarterYear.setPrefWidth(120);
        Label lblAuditQuotaResultQuarterYearValue = new Label(pRequest.getMdkQuotaQuarter() + " / " + pRequest.getMdkQuotaYear());

        Label lblAuditQuotaResultCountBilledInPatientCases = new Label(Lang.getAuditQuotaResultBilledCases());
        lblAuditQuotaResultCountBilledInPatientCases.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblAuditQuotaResultCountBilledInPatientCases, VPos.TOP);
        lblAuditQuotaResultCountBilledInPatientCases.setWrapText(true);
        Label lblAuditQuotaResultCountBilledInPatientCasesValue = new Label(pRequest.getMdkQuotaInPatientCaseCount() + "");

        Label lblAuditQuotaResultCountMDExaminatedInPatientCases = new Label(Lang.getAuditQuotaResultMDExaminations());
        lblAuditQuotaResultCountMDExaminatedInPatientCases.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblAuditQuotaResultCountMDExaminatedInPatientCases, VPos.TOP);
        lblAuditQuotaResultCountMDExaminatedInPatientCases.setWrapText(true);
        Label lblAuditQuotaResultCountMDExaminatedInPatientCasesValue = new Label(pRequest.getMdkQuotaActComplInpatCase() + "");

        Label lblAuditQuotaResultCountAllowedMDExaminations = new Label(Lang.getAuditQuotaResultMaxMDExaminations());
        lblAuditQuotaResultCountAllowedMDExaminations.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblAuditQuotaResultCountAllowedMDExaminations, VPos.TOP);
        lblAuditQuotaResultCountAllowedMDExaminations.setWrapText(true);
        Label lblAuditQuotaResultCountAllowedMDExaminationsValue = new Label(pRequest.getMdkMaxComplaintsForQuota() + "");

        Label lblAuditQuotaResultActualExaminationQuota = new Label(Lang.getAuditQuotaResultActualExaminationQuota());
        lblAuditQuotaResultActualExaminationQuota.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblAuditQuotaResultActualExaminationQuota, VPos.TOP);
        lblAuditQuotaResultActualExaminationQuota.setWrapText(true);
        Label lblAuditQuotaResultActualExaminationQuotaValue = new Label(Lang.toDecimal(pRequest.getMdkGivenQuotaForQuarter()) + " %");

        gpExaminationQuota.add(lblHospitalIdent, 0, 0);
        gpExaminationQuota.add(lblHospitalIdentValue, 1, 0);
        gpExaminationQuota.add(lblInsuranceIdent, 0, 1);
        gpExaminationQuota.add(lblInsuranceIdentValue, 1, 1);
        gpExaminationQuota.add(lblAuditQuotaResultQuarterYear, 0, 2);
        gpExaminationQuota.add(lblAuditQuotaResultQuarterYearValue, 1, 2);
        gpExaminationQuota.add(lblAuditQuotaResultCountBilledInPatientCases, 0, 3);
        gpExaminationQuota.add(lblAuditQuotaResultCountBilledInPatientCasesValue, 1, 3);
        gpExaminationQuota.add(lblAuditQuotaResultCountMDExaminatedInPatientCases, 0, 4);
        gpExaminationQuota.add(lblAuditQuotaResultCountMDExaminatedInPatientCasesValue, 1, 4);
        gpExaminationQuota.add(lblAuditQuotaResultCountAllowedMDExaminations, 0, 5);
        gpExaminationQuota.add(lblAuditQuotaResultCountAllowedMDExaminationsValue, 1, 5);
        gpExaminationQuota.add(lblAuditQuotaResultActualExaminationQuota, 0, 6);
        gpExaminationQuota.add(lblAuditQuotaResultActualExaminationQuotaValue, 1, 6);

        return gpExaminationQuota;
    }

    private Node fillRequestAuditData(GridPane pPane, TWmRequestAudit pRequest) {
        final CpxInsuranceCompanyCatalog insuranceCatalog = CpxInsuranceCompanyCatalog.instance();
//        String insCompName = insuranceCatalog.findInsNameByInsuranceNumber(pRequest.getInsuranceIdentifier(), AbstractCpxCatalog.DEFAULT_COUNTRY);
        final CpxInsuranceCompany auditComp = insuranceCatalog.getByCode(pRequest.getInsuranceIdentifier(), AbstractCpxCatalog.DEFAULT_COUNTRY);

        Label labelInsCompText = new Label(Lang.getInsuranceName());
        labelInsCompText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(labelInsCompText, VPos.TOP);
        Label labelInsComp = new Label(auditComp.getInscName());
        labelInsComp.setWrapText(true);

        Label labelIKText = new Label(Lang.getInsuranceIdent());
        labelIKText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(labelIKText, VPos.TOP);
        Label labelIK = new Label(pRequest.getInsuranceIdentifier());
        labelIK.setWrapText(true);

        Pane graph = ExtendedInfoHelper.addInfoPane(labelIK, getAuditDataGrid(auditComp), PopOver.ArrowLocation.TOP_CENTER);
        graph.setMaxWidth(100);

//        Label labelPreTrialStateText = new Label(Lang.getAuditPreTrial());
//        labelPreTrialStateText.getStyleClass().add("cpx-detail-label");
//        GridPane.setValignment(labelPreTrialStateText, VPos.TOP);
//        Label labelPreTrialState = new Label(getRequestStatus(pRequest));
//        labelPreTrialState.setWrapText(true);

//        Label labelEnsuranceStartAuditText = new Label(Lang.getProcessStartDate());
//        labelEnsuranceStartAuditText.getStyleClass().add("cpx-detail-label");
//        GridPane.setValignment(labelEnsuranceStartAuditText, VPos.TOP);
//        Label labelEnsuranceStartAudit = new Label(Lang.toDate(pRequest.getStartAudit()));
//        labelEnsuranceStartAudit.setWrapText(true);

        Label lblCommentText = new Label(Lang.getAuditComment());
        lblCommentText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblCommentText, VPos.TOP);
        Label lblComment = new Label(pRequest.getUserComment());
        lblComment.setWrapText(true);
        
        Label lblAuditReasonsText = new Label(Lang.getMdkAuditReasons());
        lblAuditReasonsText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblAuditReasonsText, VPos.TOP);
        Label lblAuditReasons = createAuditReasonLabel(pRequest,false);
        
        Label lblReportStartDateText = new Label(Lang.getMdkReportCreationDate());
        lblReportStartDateText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(lblReportStartDateText, VPos.TOP);
        Label lblReportStartDate = new Label(Lang.toDate(pRequest.getReportDate()));
        lblReportStartDate.setWrapText(true);
        
        pPane.add(labelInsCompText, 0, 0);
        pPane.add(labelInsComp, 1, 0);

        pPane.add(labelIKText, 0, 1);
        pPane.add(graph, 1, 1);

//        pPane.add(labelPreTrialStateText, 0, 2);
//        pPane.add(labelPreTrialState, 1, 2);
//
//        pPane.add(labelEnsuranceStartAuditText, 0, 3);
//        pPane.add(labelEnsuranceStartAudit, 1, 3);

        pPane.add(lblAuditReasonsText, 0, 2);
        pPane.add(lblAuditReasons, 1, 2);
        
        pPane.add(lblReportStartDateText, 0, 3);
        pPane.add(lblReportStartDate, 1, 3);
        
        pPane.add(lblCommentText, 0, 4);
        pPane.add(lblComment, 1, 4);
        return pPane;

    }

    @Override
    public WmRequestOperations getOperations() {
        return new WmRequestOperations(facade);
    }

    private Node fillRequestRiskData(TWmRequestMdk tWmRequestMdk, boolean pIsReadOnly) {
        
        SectionHeader header = new SectionHeader("Risikobewertung: Anfrage");
        VBox layout = new VBox(header);
        layout.setMinHeight(300);
        VBox.setVgrow(layout, Priority.ALWAYS);
        TWmRisk risk = facade.getActualRiskForVersionRiskType(VersionRiskTypeEn.AUDIT_MD,PlaceOfRegEn.REQUEST);//facade.createOrGetRequestRisk(tWmRequestMdk);
        if(risk == null){
            Label lbl = new Label("Risiko wurde noch nicht ermittelt!");
            lbl.setMaxWidth(Double.MAX_VALUE);
            lbl.setMaxHeight(Double.MAX_VALUE);
            lbl.setAlignment(Pos.CENTER);
            layout.getChildren().add(lbl);
            VBox.setVgrow(lbl, Priority.ALWAYS);
            return layout;
        }
        RiskFixedTable risks = new RiskFixedTable();
        //AWi: add pref height to avoid gaining to much space and force titledPane request details to vertical scrolling
        risks.setPrefHeight(200);
        risks.setDisplayMode(pIsReadOnly?FixedTable.DisplayMode.READ_ONLY:FixedTable.DisplayMode.NORMAL);
        risks.setCheckDetailDeletable(new Callback<TWmRiskDetails, Boolean>() {
            @Override
            public Boolean call(TWmRiskDetails param) {
                if (param == null) {
                    return false;
                }
                for (TWmMdkAuditReasons auditReason : tWmRequestMdk.getAuditReasons()) {
                    CMdkAuditreason commonAudit = MenuCache.getMenuCacheAuditReasons().get(auditReason.getAuditReasonNumber());
                    if (commonAudit == null) {
                        continue;
                    }
                    if (param.getRiskArea().equals(commonAudit.getMdkArRiskArea())) {
                        return false;
                    }
                }
                return true;
            }
        });
        risks.addEventFilter(SaveOrUpdateRiskEvent.saveOrUpdateRiskEvent(), new EventHandler<SaveOrUpdateRiskEvent>() {
            @Override
            public void handle(SaveOrUpdateRiskEvent event) {
                    TWmRisk updatedRisk = facade.saveOrUpdateRisk(risks.getSumEnty().getRisk());
                    RiskTableItem updatedMainRisk = new RiskTableItem(updatedRisk);
                    risks.setSumEntry(updatedMainRisk);
                    risks.setItems(updatedMainRisk.getRiskDetailItems());
                }
        });

        RiskTableItem mainRisk = new RiskTableItem(risk);
        risks.setSumEntry(mainRisk);
        risks.addItems(mainRisk.getRiskDetailItems());
        if (!pIsReadOnly) {
            AddButton btnAdd = new AddButton();
            btnAdd.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    TWmRiskDetails newDetail = new TWmRiskDetails();
                    newDetail.setRiskComment("");
                    newDetail.setRiskPercent(0);
                    newDetail.setRiskValue(BigDecimal.ZERO);
                    risks.addItem(new RiskTableItem(newDetail));

                }
            });
            header.addMenuItems(btnAdd);
        }
        layout.getChildren().add(risks);
        VBox.setVgrow(risks, Priority.ALWAYS);
        return layout;

    }

    private Node fillRequestReviewData(GridPane pPane, TWmRequestReview pRequest) {
        final CpxMdk mdkCatalog = CpxMdkCatalog.instance().getByInternalId(pRequest.getMdInternalId(), AbstractCpxCatalog.DEFAULT_COUNTRY);

        Label lblMdkNameText =createLabel4Text(Lang.getMdkName());
        //AWi: remove wrap text to allow more readable text overrun computation for javafx texts
        //otherwise text in label appear as WORD_ELLIPSIS
        Label lblMdkName = new Label(mdkCatalog.getMdkName());
        Pane graph = ExtendedInfoHelper.addInfoPane(lblMdkName, getMdkDataGrid(mdkCatalog), PopOver.ArrowLocation.TOP_CENTER);

        final CpxInsuranceCompanyCatalog insuranceCatalog = CpxInsuranceCompanyCatalog.instance();
        final CpxInsuranceCompany insComp = insuranceCatalog.getByCode(pRequest.getInsIdentifier(), AbstractCpxCatalog.DEFAULT_COUNTRY);

        Label labelInsCompText = createLabel4Text(Lang.getInsuranceName());

        Label labelInsComp = new Label(insComp.getInscName());
        labelInsComp.setWrapText(true);

        Label labelIKText = createLabel4Text(Lang.getInsuranceIdent());

        Label labelIK = new Label(pRequest.getInsIdentifier());
        labelIK.setWrapText(true);

        Pane insGraph = ExtendedInfoHelper.addInfoPane(labelIK, getAuditDataGrid(insComp), PopOver.ArrowLocation.TOP_CENTER);
//        graph.setMaxWidth(100);
        List <Label> titleLabels = new ArrayList<>();
        List <Node> values = new ArrayList<>();
        titleLabels.add(createLabel4Text(Lang.getReviewExpertiseInDate()));
        values.add(createLabel4Text(Lang.toDate(pRequest.getReportReceiveDate())));
        titleLabels.add(createLabel4Text(Lang.getReviewRequestStatus()));
        values.add(createLabel4Text(MenuCache.getMenuCacheRequestStates().getName(pRequest.getRequestState())));
        titleLabels.add(createLabel4Text(Lang.getReviewAuditReasons()));
        values.add(createAuditReasonsHbox(pRequest));
        titleLabels.add(createLabel4Text(Lang.getReviewDeadlineDate()));
        values.add(createLabel4Text(Lang.toDate(pRequest.getReviewDeadline())));
        titleLabels.add(createLabel4Text(Lang.getReviewDeadlineExtendedDate()));
        values.add(createLabel4Text(Lang.toDate(pRequest.getRenewalDeadline())));
        titleLabels.add(createLabel4Text(Lang.getReviewStartDate()));
        values.add(createLabel4Text(Lang.toDate(pRequest.getReviewStart())));
        titleLabels.add(createLabel4Text(Lang.getReviewDeadlineAnswerInsuranceDate()));
        values.add(createLabel4Text(Lang.toDate((pRequest.getInsReplyDeadline()))));
        titleLabels.add(createLabel4Text(Lang.getReviewAnsweredDate()));
        values.add(createLabel4Text(Lang.toDate(pRequest.getInsReplyDate())));
        titleLabels.add(createLabel4Text(Lang.getReviewDeadlineSendOnDate()));
        values.add(createLabel4Text(Lang.toDate(pRequest.getReplySendDocDeadline())));
        titleLabels.add(createLabel4Text(Lang.getReviewSentDate()));
        values.add(createLabel4Text(Lang.toDate(pRequest.getReplySendDocDate())));
         titleLabels.add(createLabel4Text(Lang.getReviewCompletionDeadlineDate()));
        values.add(createLabel4Text(Lang.toDate(pRequest.getCompletionDeadline())));
        titleLabels.add(createLabel4Text(Lang.getReviewCompletedDate()));
        values.add(createLabel4Text(Lang.toDate(pRequest.getCompletedDate())));
//        titleLabels.add(createLabel4Text(Lang.getReviewAfterMd()));
//        values.add(createLabel4Text(""));
//        titleLabels.add( createLabel4Text(Lang.getReviewSendOnDocuments()));
//        values.add(createLabel4Text(""));
//        titleLabels.add(createLabel4Text(Lang.getReviewCompletion()));
//        values.add(createLabel4Text(""));
       int line = 0;
        pPane.add(lblMdkNameText, 0, line);
        pPane.add(graph, 1, line++);
        pPane.add(labelInsCompText, 0, line);
        pPane.add(labelInsComp, 1, line++);
        pPane.add(labelIKText, 0, line);
        pPane.add(insGraph, 1, line++);
        for(int i = 0; i < titleLabels.size(); i++){
            pPane.add(titleLabels.get(i), 0, line);
            pPane.add(values.get(i), 1, line++);

        }        
        return pPane;
    }
    
    private Label createLabel4Text(String pText){
        Label label = new Label(pText);
        label.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(label, VPos.TOP);
        return label;
    }
    
    private HBox createAuditReasonsHbox(TWmRequest pRequest){
                Label lblMdkAuditReasons = new Label();

//        List<Long> auditReasonsInternalIds = new ArrayList<>();
//        for (TWmMdkAuditReasons reason : pRequest.getAuditReasons(false)) {
//            auditReasonsInternalIds.add(reason.getAuditReasonNumber());
//        }
//        String text = auditReasonsInternalIds.stream().map(elem -> MenuCache.instance().getAuditReasonForNumber(elem)).collect(Collectors.joining(", "));
//        String text4Overrun = auditReasonsInternalIds.stream().map(elem -> MenuCache.instance().getAuditReasonForNumber(elem)).collect(Collectors.joining("\n"));
        lblMdkAuditReasons.setText(DisplayHelper.createAuditReasonInitialText(pRequest, ", "));
        HBox helpOverrun = new HBox();
        helpOverrun.getChildren().add(lblMdkAuditReasons);
        OverrunHelper.addOverrunInfoButton(lblMdkAuditReasons, DisplayHelper.createAuditReasonInitialText(pRequest, "\n"), true);
        return helpOverrun;
    }

}
