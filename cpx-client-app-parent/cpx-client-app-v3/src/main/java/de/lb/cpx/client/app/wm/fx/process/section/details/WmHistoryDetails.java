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
import de.lb.cpx.client.app.wm.fx.process.listview.entries.HistoryEntry;
import de.lb.cpx.model.TP301Inka;
import de.lb.cpx.model.TP301Kain;
import de.lb.cpx.model.TP301KainInka;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * to create detailed view (detailed information) of the particular event of the
 * History panel.
 *
 * @author wilde
 */
public class WmHistoryDetails {

    private static final Logger LOG = Logger.getLogger(WmHistoryDetails.class.getName());

    private final ProcessServiceFacade facade;
    private final HistoryEntry<? extends AbstractEntity> item;

    public WmHistoryDetails(ProcessServiceFacade pFacade, HistoryEntry<? extends AbstractEntity> pItem) {
        facade = pFacade;
        item = pItem;
    }

    /**
     * to create detailed view (detailed information) of the particular event of
     * the History panel.
     *
     * @return parent
     */
    public WmDetailSection getDetailSection() {
//        long startTime = System.currentTimeMillis();
//        ListItem selected_event = historyListView.getSelectionModel().getSelectedItem();    // take one selected listitem 
        if (item == null) {
            return new WmDetailSection();
        }

        GridPane gridpane = new GridPane();
        gridpane.setVgap(10.0);
        gridpane.setHgap(10.0);

        Label eventType = new Label(item.toString());

        gridpane.add(eventType, 1, 0);

//        detailSection.setTitle(Lang.getDetailedViewMainLabel());
//        detailSection.createMenuItems(item.getEvent(), facade);
//        Parent detPane = null;
        final WmDetails<?> details;
        if (item.getEvent().isOrphaned()) {
            //deleted event content
            LOG.log(Level.FINEST, "selected event of type {0} in workflow number {1} has no content", new Object[]{item.getEvent().getEventType(), facade.getCurrentProcessNumber()});
//            WmDetailSection section = new WmDetailSection();
//            section.setTitle(item.getEvent().getSubject());
//            return section;
            details = null;
        } else if (item.getEvent().getDocument() != null && item.getEvent().getEventType().isDocumentRelated()) {
            //LOG.log(Level.INFO, "getDocument");
            details = new WmDocumentDetails(facade, item.getEvent().getDocument());
//            detPane = test.getDetailNode();
            //detPane = test.getDetailNode(pHistoryEvent.getEvent().getDocument());
//            historyListView.refresh();
//            historyListView.reload();
        } else if (item.getEvent().getAction() != null && item.getEvent().getEventType().isActionRelated()) {
            //LOG.log(Level.INFO, "getAction");
            //final long startTime2 = System.currentTimeMillis();
//            detPane = createActionDetailPane(item.getEvent().getAction());
            details = new WmActionDetails(facade, item.getEvent().getAction());
            //LOG.log(Level.INFO, "getAction took " + (System.currentTimeMillis() - startTime2));
        } else if (item.getEvent().getReminder() != null && item.getEvent().getEventType().isReminderRelated()) {
            //LOG.log(Level.INFO, "getReminder");
            details = new WmReminderDetails(facade, item.getEvent().getReminder());
//            detPane = test.getDetailNode();
            //detPane = test.getDetailNode(pHistoryEvent.getEvent().getReminder());
        } else if (item.getEvent().getRequest() != null && item.getEvent().getEventType().isRequestRelated()) {
            //LOG.log(Level.INFO, "getRequest");
//            detPane = createRequestDetailPane(item.getEvent().getRequest());
            details = new WmRequestDetails(facade, item.getEvent().getRequest());
        } else if (item.getEvent().getHosCase() != null && item.getEvent().getEventType().isHosCaseRelated()) {
            //LOG.log(Level.INFO, "getHosCase");
            //final TWmProcessCase fakeProcessCase = new TWmProcessCase();
            //fakeProcessCase.setHosCase(item.getEvent().getHosCase());
            details = new WmServiceOverviewDetails(facade, item.getEvent().getHosCase());
//            detPane = test.getDetailNode();
            //detPane = test.getDetailNode(pHistoryEvent.getEvent().getHosCase());
        } else if (item.getEvent().getKainInka() != null && item.getEvent().getEventType().isKainInkaRelated()) {
            //LOG.log(Level.INFO, "getKainInka");
            //detPane = createKainAndInkaMsgDetailPane(item.getEvent().getKainInka());
            TP301KainInka kainInka = item.getEvent().getKainInka();
            if (kainInka.isInka()) {
                details = new WmInkaDetails(facade, (TP301Inka) item.getEvent().getKainInka());
            } else {
                details = new WmKainDetails(facade, (TP301Kain) item.getEvent().getKainInka());
            }
            //} else if (item.getEvent().getEventType().equals(WmEventTypeEn.processClosed) && item.getEvent().getProcess().isProcessHospital() && ((TWmProcessHospital) facade.getCurrentProcess()).getProcessHospitalFinalisation() != null) {
        } else if (item.getEvent().getEventType().isProcessFinalisationRelated()) {
            //TWmProcessHospitalFinalisation result = ((TWmProcessHospital) facade.getProcess()).getProcessHospitalFinalisation();
            //detPane = createProcessClosedDetailPane(facade);
            details = new WmProcessFinalisationDetails(facade, facade.getCurrentProcessFinalisation());
        } else {
            LOG.log(Level.WARNING, "cannot create detail view, found unhandled event type for workflow number {0}: {1}", new Object[]{facade.getCurrentProcessNumber(), item.getEvent().getEventType()});
            details = null;
        }

//        if (details != null) {
//            Parent detPane = details.getDetailNode();
//            //LOG.log(Level.INFO, "setContent");
//            //final long startTime2 = System.currentTimeMillis();
//            detailSection.setContent(detPane);
//            //LOG.log(Level.INFO, "setContent took " + (System.currentTimeMillis() - startTime2));
//        }
//        LOG.log(Level.FINE, "detail content for workflow number {0} loaded in {1} ms", new Object[]{(facade == null ? "null" : facade.getCurrentProcessNumber()), (System.currentTimeMillis() - startTime)});
        return details != null ? details.getDetailSection() : new WmDetailSection();
    }

//    /**
//     * creates new Inka Message and Event Item, adds Event to the observable
//     * list.
//     */
////    private void createNewInkaMsgEvent() {
////    }
//    /**
//     * to create details about action type of events.
//     *
//     * @param action action to create detail for
//     * @return detail node as parent
//     */
//    public Parent createActionDetailPane(TWmAction action) {
//
//        TitledPane tpGenerelInfos = new TitledPane();
//        tpGenerelInfos.setText(Lang.getGeneral());
//
//        GridPane gpInfos = new GridPane();
//        gpInfos.setVgap(10.0);
//        gpInfos.setHgap(10.0);
//
//        Label labelUserText = new Label(Lang.getLoginUser());
//        labelUserText.getStyleClass().add("cpx-detail-label");
//        GridPane.setValignment(labelUserText, VPos.TOP);
//        Label labelUser = new Label(facade.getUserLabel(action.getCreationUser()) != null ? facade.getUserLabel(action.getCreationUser()) : ""); //2017-04-26 DNi - CPX-489: Show readable name
//        labelUser.setWrapText(true);
//
//        Label labelCaseAddedDateText = new Label(Lang.getActionCreationDate());
//        labelCaseAddedDateText.getStyleClass().add("cpx-detail-label");
//        labelCaseAddedDateText.setWrapText(true);
//        GridPane.setValignment(labelCaseAddedDateText, VPos.TOP);
//        Label labelCaseAddedDate = new Label(Lang.toDate(action.getCreationDate()));
//        GridPane.setValignment(labelCaseAddedDate, VPos.TOP);
//
//        Label labelActionCommentText = new Label(Lang.getComment());
//        labelActionCommentText.getStyleClass().add("cpx-detail-label");
//        GridPane.setValignment(labelActionCommentText, VPos.TOP);
//
//        Label labelActionTypeText = new Label(Lang.getActionType());
//        labelActionTypeText.getStyleClass().add("cpx-detail-label");
//
//        Label labelActionType = new Label(MenuCache.instance().getActionSubjectName(action.getActionType()));
//        Label labelActionComment = new Label(action.getComment() != null ? String.valueOf(action.getComment()) : "");
//        labelActionComment.setWrapText(true);
//
//        ScrollPane spActionComment = new ScrollPane(labelActionComment);
//        spActionComment.getStyleClass().add("history-detail-scroll-pane");
//        spActionComment.setFitToWidth(true);
//        spActionComment.setMaxHeight(Double.MAX_VALUE);
//
//        Button btnSendMail = new Button();
//        btnSendMail.setMaxHeight(Double.MAX_VALUE);
//        btnSendMail.setAlignment(Pos.CENTER);
//        btnSendMail.setMinWidth(30);
//        btnSendMail.setTooltip(new Tooltip("versenden"));
//        btnSendMail.getStyleClass().add("cpx-icon-button");
//        btnSendMail.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.ENVELOPE));
//        btnSendMail.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                MailCreator mailCreator = new MailCreator();
//                mailCreator.sendMail(facade.getCurrentProcess(), action);
//            }
//        });
//
//        gpInfos.add(labelUserText, 0, 0);
//        gpInfos.add(labelUser, 1, 0);
//        gpInfos.add(labelCaseAddedDateText, 0, 1);
//        gpInfos.add(labelCaseAddedDate, 1, 1);
//        gpInfos.add(labelActionTypeText, 0, 2);
//        HBox hbox = new HBox();
//        hbox.setAlignment(Pos.CENTER_LEFT);
//        hbox.setFillHeight(true);
//        hbox.setSpacing(2);
////        hbox.getChildren().addAll(cbActionType, btnSendMail);
//        hbox.getChildren().addAll(labelActionType, btnSendMail);
//        gpInfos.add(hbox, 1, 2);
//
//        gpInfos.add(labelActionCommentText, 0, 3);
//        gpInfos.add(spActionComment, 0, 4);
//        GridPane.setColumnSpan(spActionComment, 2);
//        GridPane.setVgrow(spActionComment, Priority.ALWAYS);
//        ColumnConstraints columnConstraintHalf = new ColumnConstraints();
//        columnConstraintHalf.setPercentWidth(50);
//        //Awi-20180323-CPX889
//        //add row contrains to grab all space thats left for last row
//        gpInfos.getColumnConstraints().add(columnConstraintHalf);
//        tpGenerelInfos.setContent(gpInfos);
//        tpGenerelInfos.disableProperty().setValue(!Session.instance().getRoleProperties().isEditActionAllowed());
//
//        return tpGenerelInfos;
//    }
//    /**
//     * to create details about Request type of events.
//     */
//    private Parent createRequestDetailPane(TWmRequest request) {
//        VBox content = new VBox();
//        content.setSpacing(12);
//
//        TitledPane tpGenerelInfos = new TitledPane();
//        tpGenerelInfos.setText(Lang.getGeneral());
//
//        GridPane gpInfos = new GridPane();
//        gpInfos.setVgap(10.0);
//        gpInfos.setHgap(10.0);
//
//        Label labelUserText = new Label(Lang.getLoginUser());
//        labelUserText.getStyleClass().add("cpx-detail-label");
//        GridPane.setValignment(labelUserText, VPos.TOP);
//        Label labelUser = new Label(facade.getUserLabel(request.getCreationUser()) != null ? facade.getUserLabel(request.getCreationUser()) : ""); //2017-04-26 DNi - CPX-489: Show readable name
//        labelUser.setWrapText(true);
//
//        Label labelCreationDateText = new Label(Lang.getRequestCreationDate());
//        labelCreationDateText.getStyleClass().add("cpx-detail-label");
//        labelCreationDateText.setWrapText(true);
//        Label labelCreationDate = new Label(Lang.toDate(request.getCreationDate()));
//        GridPane.setValignment(labelCreationDate, VPos.TOP);
//
//        Label labelRequestTypeText = new Label(Lang.getWmRequesttype());
//        labelRequestTypeText.getStyleClass().add("cpx-detail-label");
//
//        Label lblRequestType = new Label("-");
//        if (request.getRequestTypeEnum() != null) {
//            lblRequestType.setText(request.getRequestTypeEnum().getTranslation().getValue());
//        }
//
//        LabeledTextArea taComment = new LabeledTextArea(Lang.getComment(), 255);
//        taComment.setPadding(new Insets(0, 10, 0, 10));
//        taComment.setText(request.getComment() != null ? request.getComment() : "");
//        Label title = (Label) taComment.lookup("#title");
//        if (title != null) {
//            title.getStyleClass().add("cpx-detail-label");
//            title.setStyle("-fx-font-size: 15px;");
//        }
//        taComment.setEditable(true); //2017-04-26 DNi - CPX-489: Changed to true
//        taComment.setWrapText(true);
//        taComment.getControl().focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//            if (newValue) {
//                //gain focus = ignore
//                return;
//            }
//            if (taComment.getText().equals(request.getComment() != null ? request.getComment() : "")) {
//                return;
//            }
//            if (facade != null) {
//                //simple validattion if text changed
//                if (taComment.getText().equals(request.getComment())) {
//                    //comment did not change.. ignore
//                    return;
//                }
//                request.setComment(taComment.getText());
//                //historyListView.refresh(); //2019-11-07 DNi -> TODO?!
//                facade.updateRequest(request);
//            }
//        });
//
//        VBox.setVgrow(taComment, Priority.ALWAYS);
//        gpInfos.add(labelUserText, 0, 0);
//        gpInfos.add(labelUser, 1, 0);
//        gpInfos.add(labelCreationDateText, 0, 1);
//        gpInfos.add(labelCreationDate, 1, 1);
//        gpInfos.add(labelRequestTypeText, 0, 2);
//        gpInfos.add(lblRequestType, 1, 2);
//
//        ColumnConstraints columnConstraintHalf = new ColumnConstraints();
//        columnConstraintHalf.setPercentWidth(50);
//
//        gpInfos.getColumnConstraints().add(columnConstraintHalf);
//        //Awi-20180323-CPX889
//        //add row contrains to grab all space thats left for last row
//        gpInfos.getRowConstraints().add(new RowConstraints());
//        gpInfos.getRowConstraints().add(new RowConstraints());
//        gpInfos.getRowConstraints().add(new RowConstraints());
//        gpInfos.getRowConstraints().add(new RowConstraints());
//        gpInfos.getRowConstraints().add(new RowConstraints(GridPane.USE_PREF_SIZE, GridPane.USE_COMPUTED_SIZE, Double.MAX_VALUE, Priority.ALWAYS, VPos.CENTER, true));
//
//        tpGenerelInfos.setContent(gpInfos);
//
//        // new titlepane to show details of a request
//        TitledPane tpDetailsInfos = new TitledPane();
//        tpDetailsInfos.setText("Anfrage Details");
//
//        GridPane gpDetailsInfos = new GridPane();
//        gpDetailsInfos.setVgap(10.0);
//        gpDetailsInfos.setHgap(10.0);
//        gpDetailsInfos.getColumnConstraints().add(columnConstraintHalf);
//
//        final CpxInsuranceCompanyCatalog insuranceCatalog = CpxInsuranceCompanyCatalog.instance();
//
//        if (request.getRequestType() != null) {
//            switch (request.getRequestTypeEnum()) {
//                case bege:
//                    TWmRequestBege begeReq = (TWmRequestBege) request;
//
//                    String InsCompName = insuranceCatalog.findInsNameByInsuranceNumber(begeReq.getInsuranceIdentifier(), AbstractCpxCatalog.DEFAULT_COUNTRY);
//
//                    Label labelInsCompText = new Label("BG-Name");
//                    labelInsCompText.getStyleClass().add("cpx-detail-label");
//                    GridPane.setValignment(labelInsCompText, VPos.TOP);
//                    Label labelInsComp = new Label(InsCompName);
//                    labelInsComp.setWrapText(true);
//
//                    Label labelIKText = new Label("IK-Nummer");
//                    labelIKText.getStyleClass().add("cpx-detail-label");
//                    GridPane.setValignment(labelIKText, VPos.TOP);
//                    Label labelIK = new Label(begeReq.getInsuranceIdentifier());
//                    labelIK.setWrapText(true);
//
//                    CpxInsuranceCompany insComp = insuranceCatalog.getByCode(begeReq.getInsuranceIdentifier(), AbstractCpxCatalog.DEFAULT_COUNTRY);
//                    Label fullBgText = new Label();
//                    fullBgText.setWrapText(true);
//                    fullBgText.setText("Anschrift: " + (insComp.getInscAddress() != null ? insComp.getInscAddress() : "") + "\n"
//                            + "Stadt: " + (insComp.getInscZipCode() != null ? (insComp.getInscZipCode() + " ") : "") + (insComp.getInscCity() != null ? insComp.getInscCity() : ""));
//                    Pane graph = ExtendedInfoHelper.addInfoPane(labelIK, fullBgText, PopOver.ArrowLocation.TOP_CENTER);
//                    graph.setMaxWidth(100);
//
//                    Label labelEditorText = new Label("Ansprechpartner");
//                    labelEditorText.getStyleClass().add("cpx-detail-label");
//                    GridPane.setValignment(labelEditorText, VPos.TOP);
//                    Label labelEditor = new Label(begeReq.getBegeEditor());
//                    labelEditor.setWrapText(true);
//
//                    Label labelTelNoText = new Label("Telefon Nummer");
//                    labelTelNoText.getStyleClass().add("cpx-detail-label");
//                    GridPane.setValignment(labelTelNoText, VPos.TOP);
//                    Label labelTelNo = new Label(begeReq.getDirectPhone());
//                    labelTelNo.setWrapText(true);
//
//                    Label labelFaxNoText = new Label("FAX-Nummer");
//                    labelFaxNoText.getStyleClass().add("cpx-detail-label");
//                    GridPane.setValignment(labelFaxNoText, VPos.TOP);
//                    Label labelFaxNo = new Label(begeReq.getDirectFax());
//                    labelFaxNo.setWrapText(true);
//
//                    gpDetailsInfos.add(labelInsCompText, 0, 0);
//                    gpDetailsInfos.add(labelInsComp, 1, 0);
//                    gpDetailsInfos.add(labelIKText, 0, 1);
////                    gpDetailsInfos.add(labelIK, 1, 1);
//                    gpDetailsInfos.add(graph, 1, 1);
//                    gpDetailsInfos.add(labelEditorText, 0, 2);
//                    gpDetailsInfos.add(labelEditor, 1, 2);
//                    gpDetailsInfos.add(labelTelNoText, 0, 3);
//                    gpDetailsInfos.add(labelTelNo, 1, 3);
//                    gpDetailsInfos.add(labelFaxNoText, 0, 4);
//                    gpDetailsInfos.add(labelFaxNo, 1, 4);
////                    gpDetailsInfos.add(comment, 0, 5);
//
////                    gpDetailsInfos.getColumnConstraints().add(columnConstraintHalf);
//                    tpDetailsInfos.setContent(gpDetailsInfos);
//
//                    content.getChildren().addAll(tpGenerelInfos, tpDetailsInfos, taComment);//comment);
//
//                    break;
//                case mdk:
//                    content.getChildren().addAll(tpGenerelInfos, taComment);//comment);
//                    break;
//                case audit:
//                    content.getChildren().addAll(tpGenerelInfos, taComment);//comment);
//                    break;
//                case insurance:
//                    tpDetailsInfos.setContent(fillRequestInsuranceData(gpDetailsInfos, (TWmRequestInsurance) request));
////                    content.getChildren().addAll(tpGenerelInfos, tpDetailsInfos, comment);
//                    content.getChildren().addAll(tpGenerelInfos, tpDetailsInfos, taComment);//comment);
//                    break;
//                case other:
////                    tpDetailsInfos.setContent(fillRequestOtherData(gpDetailsInfos, (TWmRequestOther) request));
////                    content.getChildren().addAll(tpGenerelInfos, tpDetailsInfos, comment);
//                    tpDetailsInfos.setContent(fillRequestOtherData(gpDetailsInfos, (TWmRequestOther) request));
//                    content.getChildren().addAll(tpGenerelInfos, tpDetailsInfos, taComment);//comment);
//                    break;
//
//            }
//        }
//
////        content.getChildren().addAll(tpGenerelInfos, tpDetailsInfos, comment);
////        return tpGenerelInfos;
//        return content;
//    }
//    private Parent createKainAndInkaMsgDetailPane(TP301KainInka kainInka) {
//        List<TP301KainInkaPvv> kainInkaPvvs = kainInka.getKainInkaPvvs();
////        Set<TP301KainInkaPvv> kainInkaPvvs = kainInka.getKainInkaPvvs();
//
//        VBox vbox = new VBox();
//        VBox.setVgrow(vbox, Priority.ALWAYS);
//
//        //Pna: 13.12.18, putting VBox inside a scrollPane. With this approach, we don't have to worry about setting the prefHeight and prefWidth attributes.
//        ScrollPane sc = new ScrollPane(vbox);
//        //disabled showing the horizontal ScrollBar
//        sc.setFitToWidth(true);
//        SimpleDateFormat formatter;
//        formatter = new SimpleDateFormat(Lang.getProcessListDateFormat());
//
//        kainInkaPvvs.forEach((TP301KainInkaPvv pvv) -> {
//            Label fullPvtText = new Label();
//            //                fullPvtText.getStyleClass().add("cpx-detail-label");
//            fullPvtText.setPadding(new Insets(1.0, 1.0, 0, 0));
//            fullPvtText.setPrefWidth(700);
//            fullPvtText.setWrapText(true);
//            TitledPane tpGenerelInfos = new TitledPane();
//            tpGenerelInfos.setMinWidth(0);
//            StringBuilder sb = new StringBuilder();
//            if (sb.length() > 0) {
//                sb.append("\r\n");
//            }
//            String key30 = pvv.getInformationKey30();
//            if (key30 == null || key30.trim().isEmpty()) {
//                LOG.log(Level.SEVERE, "TP301 Key 30 is empty!");
//            }
//            Tp301Key30En key30En = (Tp301Key30En) CpxEnumInterface.findEnum(Tp301Key30En.values(), key30);
//            if (key30En == null) {
//                LOG.log(Level.SEVERE, "This TP301 Key 30 seems to be invalid: {0}", key30);
//            }
//            final String key30Desc = key30En == null ? "Unknown key" : key30En.getTranslation().getValue();
//            sb.append(key30 + " - " + key30Desc);
//            Label lb = new Label("PVV: " + sb);
//            HBox.setHgrow(lb, Priority.ALWAYS);
//            tpGenerelInfos.setText(lb.getText());
////                tpGenerelInfos.autosize();
//// to hide the expansion of the titledpane
////                tpGenerelInfos.expandedProperty().setValue(Boolean.FALSE);
//            HBox.setHgrow(tpGenerelInfos, Priority.ALWAYS);
//            tpGenerelInfos.setWrapText(true);
//            Tooltip tooltip = new Tooltip(sb.toString());
//            tooltip.setStyle("-fx-font-size: 14px");
//            tooltip.setWrapText(true);
//            tooltip.setMaxWidth(600);
////                tpGenerelInfos.setTooltip(tooltip);
//            GridPane gpInfos = new GridPane();
////                gpInfos.getStyleClass().add("default-grid");
//            gpInfos.setVgap(5.0);
//            gpInfos.setHgap(10.0);
//            Label labelBillNo = new Label("Rechnungsnummer:");
//            labelBillNo.getStyleClass().add("cpx-detail-label");
//            GridPane.setValignment(labelBillNo, VPos.TOP);
//            Label labelBillNoValue = new Label(pvv.getBillNr());
//            labelBillNoValue.setWrapText(true);
//            Label labelBillDate = new Label("Rechnungsdatum:");
//            labelBillDate.getStyleClass().add("cpx-detail-label");
//            GridPane.setValignment(labelBillDate, VPos.TOP);
//            Label labelBillDateValue = new Label(formatter.format(pvv.getBillDate()).substring(0, 10));
//            labelBillDateValue.setWrapText(true);
//            gpInfos.add(labelBillNo, 0, 0);
//            gpInfos.add(labelBillNoValue, 1, 0);
////                gpInfos.add(labelBillDate, 0, 1);
////                gpInfos.add(labelBillDateValue, 1, 1);
//// get all PVT segments of PVV
//            List<TP301KainInkaPvt> allPvts = new ArrayList<>();
//            if (pvv.getId() > 0L) {
//                EjbProxy<ProcessServiceBeanRemote> processServiceBean = Session.instance().getEjbConnector().connectProcessServiceBean();
//                allPvts = processServiceBean.get().getAllPvtsForPvv(pvv.getId());
//            } else {
//                allPvts = pvv.getKainInkaPvts();    //Pna:18.12.2018
//            }
////                List<TP301KainInkaPvt> allPvts = pvv.getKainInkaPvts();
//            StringBuilder sb2 = new StringBuilder();
//            allPvts.forEach((TP301KainInkaPvt pvt) -> {
//                sb2.append(pvt.getText()).append(" ");
//            });
//            fullPvtText.setText(sb2.toString());
////                if (kainInka.isKain()) {
//            VBox vboxPvtsTT = new VBox();
//            vboxPvtsTT.setSpacing(5);
//            VBox vboxPvtsDV = new VBox();
//            vboxPvtsDV.setSpacing(5);
//
//            allPvts.forEach(new Consumer<TP301KainInkaPvt>() {
//                private int i = 0;
//
//                @Override
//                public void accept(TP301KainInkaPvt pvt) {
//                    if (pvt != null) {
//
//                        i = i + 1;
//
//                        Label lbPvt = new Label("PVT " + String.valueOf(i));
//                        lbPvt.getStyleClass().add("cpx-detail-label");
//
//                        Label pvtText = new Label(pvt.getText().isEmpty() ? "" : pvt.getText());
//                        pvtText.setWrapText(true);
//                        GridPane.setValignment(pvtText, VPos.CENTER);
//
//                        if (isPvtHasIcdsAndOrOpses(pvt)) {
//                            GridPane gpWithIcdsAndOrOpses = createGpWithIcdsAndOrOpses(pvt);
//                            vboxPvtsTT.getChildren().addAll(lbPvt, gpWithIcdsAndOrOpses, pvtText);
//                        } else {
//                            vboxPvtsTT.getChildren().addAll(lbPvt, pvtText);
//                        }
//                    } else {
//                        LOG.log(Level.WARNING, "PVT segment is null..");
//                    }
//
//                }
//            });
//            allPvts.forEach((TP301KainInkaPvt pvt) -> {
//                if (pvt != null) {
//                    Label pvtText = new Label(pvt.getText().isEmpty() ? "" : pvt.getText());
//                    pvtText.setWrapText(true);
//                    GridPane.setValignment(pvtText, VPos.CENTER);
//
//                    if (isPvtHasIcdsAndOrOpses(pvt)) {
//                        GridPane gpWithIcdsAndOrOpses = createGpWithIcdsAndOrOpses(pvt);
//                        vboxPvtsDV.getChildren().addAll(gpWithIcdsAndOrOpses, pvtText);
//                    } else {
//                        vboxPvtsDV.getChildren().addAll(pvtText);
//                    }
//                } else {
//                    LOG.log(Level.WARNING, "PVT segment is null..");
//                }
//            });
//            ColumnConstraints columnConstraintHalf = new ColumnConstraints();
//            gpInfos.getColumnConstraints().add(columnConstraintHalf);
//
//            Label labelFullDetails = new Label("PVTs Details");
//            labelFullDetails.getStyleClass().add("cpx-detail-label");
//            labelFullDetails.setWrapText(true);
//            ScrollPane scPvts = new ScrollPane(vboxPvtsTT);
////disabled the horizontal ScrollBar
//            scPvts.setFitToWidth(true);
//            scPvts.setMaxWidth(700);
//            scPvts.setMaxHeight(800);
////
//            Pane graph = ExtendedInfoHelper.addInfoPane(labelFullDetails, scPvts, PopOver.ArrowLocation.RIGHT_CENTER);
//            graph.setMaxWidth(100);
//
//            VBox vb;
//            if (!allPvts.isEmpty()) {
//                vb = new VBox(gpInfos, graph, vboxPvtsDV);
////                    vb = new VBox(gpInfos, graph, graphFullText, vboxPvtsDV);
//            } else {
//                vb = new VBox(gpInfos);
//            }
//            vb.setSpacing(12);
//            tpGenerelInfos.setContent(vb);
//            vbox.getChildren().addAll(tpGenerelInfos);
//        }//            private int i = 1;
//        //            private StringBuilder sb2 = new StringBuilder();
//        );
//        return sc;
//    }
//    private Parent createProcessClosedDetailPane(ProcessServiceFacade facade) {
//        TWmProcessHospitalFinalisation result = ((TWmProcessHospital) facade.getProcess()).getProcessHospitalFinalisation();
//
//        TitledPane tpGeneralInfos = new TitledPane();
//        tpGeneralInfos.setText(Lang.getGeneral());
//
//        GridPane gpInfos = new GridPane();
//        gpInfos.setVgap(10.0);
//        gpInfos.setHgap(10.0);
//
//        Label lbClosedDateText = new Label("abgeschlossen am:");
//        lbClosedDateText.getStyleClass().add("cpx-detail-label");
//        lbClosedDateText.setWrapText(true);
//        GridPane.setValignment(lbClosedDateText, VPos.TOP);
//        Label lbClosedDate = new Label(Lang.toDate(result.getClosingDate()));
//        lbClosedDate.setWrapText(true);
//        GridPane.setValignment(lbClosedDate, VPos.TOP);
//
//        Label lbMainReasonText = new Label(Lang.getAuditAuditReasons());
//        lbMainReasonText.getStyleClass().add("cpx-detail-label");
//        lbMainReasonText.setWrapText(true);
//        GridPane.setValignment(lbMainReasonText, VPos.TOP);
//        List<Long> mainAuditReasonList = result.getMainAuditReasonList();
//        StringBuilder sbMainAuditReason = new StringBuilder();
//        mainAuditReasonList.forEach((Long auditReason) -> {
//            CMdkAuditreason mainAuditReason = MenuCache.getMenuCacheMdkAuditReasons().get(auditReason);
//            if (mainAuditReason != null) {
//                sbMainAuditReason.append(mainAuditReason.getMdkArName()).append(", ");
//            }
//        });
//        Label lbMainReason = new Label(sbMainAuditReason.length() > 0 ? sbMainAuditReason.substring(0, sbMainAuditReason.length() - 2) : "");
//        lbMainReason.setWrapText(true);
//        GridPane.setValignment(lbMainReason, VPos.TOP);
//
//        Label lbFurtherReasonsText = new Label("Erweiterter Prüfgrunde");
//        lbFurtherReasonsText.getStyleClass().add("cpx-detail-label");
//        lbFurtherReasonsText.setWrapText(true);
//        GridPane.setValignment(lbFurtherReasonsText, VPos.TOP);
//        List<Long> auditReasonsExtendedList = result.getAuditReasonsExtendedList();
//        StringBuilder sb = new StringBuilder();
//        auditReasonsExtendedList.forEach((Long auditReason) -> {
//            CMdkAuditreason mdkAuditreason1 = MenuCache.getMenuCacheMdkAuditReasons().get(auditReason);
//            if (mdkAuditreason1 != null) {
//                sb.append(mdkAuditreason1.getMdkArName()).append(", ");
//            }
//        });
//        Label lbFurtherReasons = new Label(sb.length() > 0 ? sb.substring(0, sb.length() - 2) : "");
//        lbFurtherReasons.setWrapText(true);
//        GridPane.setValignment(lbFurtherReasons, VPos.TOP);
//
//        Label lbResultText = new Label("Ergebnis");
//        lbResultText.getStyleClass().add("cpx-detail-label");
//        lbResultText.setWrapText(true);
//        GridPane.setValignment(lbResultText, VPos.TOP);
//        long closingResult = result.getClosingResult();
//        CWmListProcessResult processResult = MenuCache.getMenuCacheProcessResults().get(closingResult);
//        Label lbResult = new Label(processResult != null ? processResult.getWmPrName() : "");
//        lbResult.setWrapText(true);
//        GridPane.setValignment(lbResult, VPos.TOP);
//
//        Label lbDRGInitFinText = new Label("DRG initial/final");
//        lbDRGInitFinText.getStyleClass().add("cpx-detail-label");
//        lbDRGInitFinText.setWrapText(true);
//        GridPane.setValignment(lbDRGInitFinText, VPos.TOP);
//        Label lbDRGInitFin = new Label((result.getDrgInitial() != null ? result.getDrgInitial() : "") + " / " + (result.getDrgFinal() != null ? result.getDrgFinal() : ""));
//        lbDRGInitFin.setWrapText(true);
//        GridPane.setValignment(lbDRGInitFin, VPos.TOP);
//
//        Label lbCWInitFinText = new Label("CW initial/final");
//        lbCWInitFinText.getStyleClass().add("cpx-detail-label");
//        lbCWInitFinText.setWrapText(true);
//        GridPane.setValignment(lbCWInitFinText, VPos.TOP);
//
//        Label lbCWInitFin = new Label(Lang.toDecimal(result.getCwInitial(), 3) + " / " + Lang.toDecimal(result.getCwFinal(), 3));   // up to 3 decimal places
//        lbCWInitFin.setWrapText(true);
//        GridPane.setValignment(lbCWInitFin, VPos.TOP);
//
//        Label lbZEInitFinText = new Label("ZE-Betrag initial/final");
//        lbZEInitFinText.getStyleClass().add("cpx-detail-label");
//        lbZEInitFinText.setWrapText(true);
//        GridPane.setValignment(lbZEInitFinText, VPos.TOP);
//        Label lbZEInitFin = new Label(Lang.toDecimal(result.getInitialSupplementaryFee(), 2) + " " + Lang.getCurrencySymbol() + " / " + Lang.toDecimal(result.getFinalSupplementaryFee(), 2) + " " + Lang.getCurrencySymbol());
//        lbZEInitFin.setWrapText(true);
//        GridPane.setValignment(lbZEInitFin, VPos.TOP);
//
//        Label lbVWDInitFinText = new Label("VWD initial/final");
//        lbVWDInitFinText.getStyleClass().add("cpx-detail-label");
//        lbVWDInitFinText.setWrapText(true);
//        GridPane.setValignment(lbVWDInitFinText, VPos.TOP);
//        Label lbVWDInitFin = new Label(String.valueOf(result.getLosInitial()) + " / " + String.valueOf(result.getLosFinal()));
//        lbVWDInitFin.setWrapText(true);
//        GridPane.setValignment(lbVWDInitFin, VPos.TOP);
//
//        Label lbAvailsInitFinText = new Label("Erlös initial/final");
//        lbAvailsInitFinText.getStyleClass().add("cpx-detail-label");
//        lbAvailsInitFinText.setWrapText(true);
//        GridPane.setValignment(lbAvailsInitFinText, VPos.TOP);
//        Label lbAvailsInitFin = new Label(Lang.toDecimal(result.getRevenueInitial(), 2) + " " + Lang.getCurrencySymbol() + " / " + Lang.toDecimal(result.getRevenueFinal(), 2) + " " + Lang.getCurrencySymbol());
//        lbAvailsInitFin.setWrapText(true);
//        GridPane.setValignment(lbAvailsInitFin, VPos.TOP);
//
//        Label lbDiffAvailsText = new Label("Differenz Erlös");
//        lbDiffAvailsText.getStyleClass().add("cpx-detail-label");
//        lbDiffAvailsText.setWrapText(true);
//        GridPane.setValignment(lbDiffAvailsText, VPos.TOP);
//        Label lbDiffAvails = new Label(Lang.toDecimal(result.getRevenueDiff(), 2) + " " + Lang.getCurrencySymbol());
//        lbDiffAvails.setWrapText(true);
//        GridPane.setValignment(lbDiffAvails, VPos.TOP);
//
//        Label taCommentText = new Label(Lang.getComment());
//        taCommentText.getStyleClass().add("cpx-detail-label");
//        GridPane.setValignment(taCommentText, VPos.TOP);
//
//        Label taComment = new Label(result.getResultComment() != null ? result.getResultComment() : "");
//        taComment.setWrapText(true);
//        Pane spacePane = new Pane();
//        HBox.setHgrow(spacePane, Priority.ALWAYS);
//
//        gpInfos.add(lbClosedDateText, 0, 0);
//        gpInfos.add(lbClosedDate, 1, 0);
//        gpInfos.add(lbMainReasonText, 0, 1);
//        gpInfos.add(lbMainReason, 1, 1);
//        gpInfos.add(lbFurtherReasonsText, 0, 2);
//        gpInfos.add(lbFurtherReasons, 1, 2);
//        gpInfos.add(lbResultText, 0, 3);
//        gpInfos.add(lbResult, 1, 3);
//        gpInfos.add(lbDRGInitFinText, 0, 4);
//        gpInfos.add(lbDRGInitFin, 1, 4);
//        gpInfos.add(lbCWInitFinText, 0, 5);
//        gpInfos.add(lbCWInitFin, 1, 5);
//        gpInfos.add(lbZEInitFinText, 0, 6);
//        gpInfos.add(lbZEInitFin, 1, 6);
//        gpInfos.add(lbVWDInitFinText, 0, 7);
//        gpInfos.add(lbVWDInitFin, 1, 7);
//        gpInfos.add(lbAvailsInitFinText, 0, 8);
//        gpInfos.add(lbAvailsInitFin, 1, 8);
//        gpInfos.add(lbDiffAvailsText, 0, 9);
//        gpInfos.add(lbDiffAvails, 1, 9);
//        gpInfos.add(taCommentText, 0, 10);
//        gpInfos.add(taComment, 1, 10);
//
//        ColumnConstraints columnConstraintHalf = new ColumnConstraints();
//        columnConstraintHalf.setPercentWidth(50);
//
//        //add row contrains to grab all space thats left for last row
//        gpInfos.getColumnConstraints().add(columnConstraintHalf);
//        gpInfos.getRowConstraints().add(new RowConstraints());
//
//        tpGeneralInfos.setContent(gpInfos);
//
//        return tpGeneralInfos;
//    }
}
