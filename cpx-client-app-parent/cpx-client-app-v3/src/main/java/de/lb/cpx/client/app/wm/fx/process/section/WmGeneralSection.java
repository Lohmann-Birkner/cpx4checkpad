/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2017  adameck - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.process.section;

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.process.section.details.WmGeneralDetails;
import de.lb.cpx.client.app.wm.fx.process.section.operations.WmGeneralOperations;
import de.lb.cpx.client.app.wm.util.DisplayHelper;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.model.TCase;
import de.lb.cpx.server.commonDB.model.CWmListProcessTopic;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmProcessHospital;
import de.lb.cpx.wm.model.TWmRequest;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author adameck, nandola
 */
public class WmGeneralSection extends WmSection<TWmProcessHospital> {

    private static final Logger LOG = Logger.getLogger(WmGeneralSection.class.getName());

    private final TWmProcessHospital process;
    private Label caseNo;
    private Label processStatus;
    private Label requestType;
    private Label processTopicLabel;
    private Label auditReasons;
    private GridPane gpDeadlines;
    private Tooltip auditReasonsTT;
    private Tooltip deadlinesTT;

    public WmGeneralSection(final ProcessServiceFacade pServiceFacade) {
        super(pServiceFacade);
        setTitle(Lang.getGeneral());
        process = (TWmProcessHospital) pServiceFacade.getCurrentProcess();
        setData();
    }

    @Override
    protected Parent createContent() {
        long startTime = System.currentTimeMillis();
        VBox content = new VBox();

        Tooltip tooltip = new Tooltip();
        tooltip.setStyle("-fx-font-size: 12px");
        tooltip.setWrapText(true);
        tooltip.setMaxWidth(500);

        GridPane gpInfos = new GridPane();
        gpInfos.getStyleClass().add("default-grid");
        Label caseLabel = keyLabel(Lang.getProcessPreviewCaseNo());
        caseLabel.setMinWidth(40);
        gpInfos.add(caseLabel, 0, 0);
        caseNo = new Label();
        caseNo.setMinWidth(40);
        gpInfos.add(caseNo, 1, 0);
        Label processStatusLabel = keyLabel(Lang.getProcessPreviewProcessStatus());
        processStatusLabel.setMinWidth(40);
        gpInfos.add(processStatusLabel, 2, 0);
        processStatus = new Label();
        processStatus.setMinWidth(40);
        gpInfos.add(processStatus, 3, 0);
        Label reqTypeLabel = keyLabel(Lang.getProcessPreviewRequestType());
        reqTypeLabel.setMinWidth(40);
        gpInfos.add(reqTypeLabel, 4, 0);
        requestType = new Label();
        requestType.setMinWidth(40);
        gpInfos.add(requestType, 5, 0);
        Label processTopicNameLabel = keyLabel(Lang.getProcessPreviewProcessTopic());
        processTopicNameLabel.setMinWidth(40);
        gpInfos.add(processTopicNameLabel, 6, 0);
        processTopicLabel = new Label();
        processTopicLabel.setMinWidth(40);
        gpInfos.add(processTopicLabel, 7, 0);

        gpInfos.getChildren().forEach(new Consumer<Node>() {
            @Override
            public void accept(Node t) {
                t.setOnMouseEntered(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        tooltip.show(t, event.getScreenX() + 10, event.getScreenY() + 10);
                        if (gpInfos.getChildren().get(0).equals(t)) {
                            tooltip.setText(caseLabel.getText());
                        } else if (gpInfos.getChildren().get(1).equals(t)) {
                            tooltip.setText(caseNo.getText());
                        } else if (gpInfos.getChildren().get(2).equals(t)) {
                            tooltip.setText(processStatusLabel.getText());
                        } else if (gpInfos.getChildren().get(3).equals(t)) {
                            tooltip.setText(processStatus.getText());
                        } else if (gpInfos.getChildren().get(4).equals(t)) {
                            tooltip.setText(reqTypeLabel.getText());
                        } else if (gpInfos.getChildren().get(5).equals(t)) {
                            tooltip.setText(requestType.getText());
                        } else if (gpInfos.getChildren().get(6).equals(t)) {
                            tooltip.setText(processTopicNameLabel.getText());
                        } else if (gpInfos.getChildren().get(7).equals(t)) {
                            tooltip.setText(processTopicLabel.getText());
                        }

                    }
                });

                t.setOnMouseExited(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        tooltip.hide();
                    }
                });
            }
        });

        HBox gpAuditReasons = new HBox();
        gpAuditReasons.getStyleClass().add("default-grid");
        Label mdkReasonText = keyLabel(Lang.getProcessPreviewAuditReasons());
        gpAuditReasons.setSpacing(8);
        auditReasons = new Label();
        AnchorPane anchorPane1 = new AnchorPane();
        anchorPane1.getChildren().addAll(mdkReasonText);
        gpAuditReasons.getChildren().addAll(anchorPane1, auditReasons);

        auditReasonsTT = new Tooltip();
        auditReasonsTT.setStyle("-fx-font-size: 12px");
        auditReasonsTT.setWrapText(true);
        auditReasonsTT.setMaxWidth(500);

        auditReasons.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                auditReasonsTT.show(auditReasons, event.getScreenX() + 10, event.getScreenY() + 10);
            }
        });
        auditReasons.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                auditReasonsTT.hide();
            }
        });

        gpDeadlines = new GridPane();
        gpDeadlines.getStyleClass().add("default-grid");
        Label deadlineLabel = new Label(Lang.getProcessPreviewDeadlines());
        deadlineLabel.getStyleClass().add("cpx-header-label");

        deadlinesTT = new Tooltip();
        deadlinesTT.setStyle("-fx-font-size: 12px");
        deadlinesTT.setWrapText(true);
        deadlinesTT.setMaxWidth(500);

        content.getChildren().addAll(gpInfos, gpAuditReasons, deadlineLabel, new Separator(Orientation.HORIZONTAL), gpDeadlines);

        VBox.setMargin(gpInfos, new Insets(8));
        VBox.setMargin(gpAuditReasons, new Insets(8));
        VBox.setMargin(gpDeadlines, new Insets(8));
        LOG.log(Level.FINE, "create content for workflow number " + (facade == null ? "null" : facade.getCurrentProcessNumber()) + " loaded in " + (System.currentTimeMillis() - startTime) + " ms");
        return content;
    }

    private Label keyLabel(String pTitle) {
        Label label = new Label(pTitle);
        label.getStyleClass().add("cpx-detail-label");
        return label;
    }

    @Override
    public TWmProcessHospital getSelectedItem() {
        return process;
    }

    @Override
    public WmGeneralDetails getDetails() {
        TWmProcessHospital selected = getSelectedItem();
        WmGeneralDetails details = new WmGeneralDetails(facade, selected);
        return details;
    }

    private void setData() {
        if(facade == null){
            LOG.warning("Facade is not yet initialized, can not setData!");
            return;
        }
        long startTime = System.currentTimeMillis();
        TCase mainCase = facade.getMainCase(process);
        caseNo.setText(mainCase != null ? mainCase.getCsCaseNumber() : "");//process.getMainCase().getCsCaseNumber());
        processStatus.setText(process.getWorkflowState() != null ? process.getWorkflowState().name() : "");
        //TODO:
        //in future not loading from server but from cached list in client?
        //CWmListProcessTopic processTopic = masterDataBean.get().getProcessTopicByIdent(process.getProcessTopic());
        CWmListProcessTopic processTopic = MenuCache.getMenuCacheProcessTopics().get(process.getProcessTopic());
        processTopicLabel.setText(processTopic != null ? processTopic.getWmPtName() : "");

        facade.getObsRequests();

        final TWmRequest latestRequest = facade.loadLatestRequest();
        if (latestRequest == null) {
            requestType.setText("");    // If there is a no request for this process.
            auditReasons.setText("");
        } else {
            requestType.setText(latestRequest.getRequestTypeEnum().toString());
            auditReasons.setText(DisplayHelper.createAuditReasonInitialText(latestRequest, ", "));
            auditReasonsTT.setText(DisplayHelper.createAuditReasonInitialText(latestRequest, "\n"));
            switch (latestRequest.getRequestTypeEnum()) {
                case mdk:
                    // set MDK Request Deadlines
                    if (process.getMdkBillCorrectionDeadline() != null) {
                        addDeadlineColumn(keyLabel(Lang.getProcessPreviewBillCorr()), new Label(Lang.toDate(process.getMdkBillCorrectionDeadline())));
                    }
                    if (process.getMdkDocDeliverDeadline() != null) {
                        addDeadlineColumn(keyLabel(Lang.getProcessPreviewDocDeliver()), new Label(Lang.toDate(process.getMdkDocDeliverDeadline())));
                    }
                    if (process.getMdkAuditCompletionDeadline() != null) {
                        addDeadlineColumn(keyLabel(Lang.getProcessPreviewAuditCompletion()), new Label(Lang.toDate(process.getMdkAuditCompletionDeadline())));
                    }
                    break;

                case audit:
                    // set Audit Request Deadlines
                    if (process.getAuditDataRecordCorrectionDeadline() != null) {
                        addDeadlineColumn(keyLabel(Lang.getProcessPreviewDataRecCorrection()), new Label(Lang.toDate(process.getAuditDataRecordCorrectionDeadline())));
                    }
                    if (process.getAuditPrelProcAnsDeadline() != null) {
                        addDeadlineColumn(keyLabel(Lang.getProcessPreviewAnswer()), new Label(Lang.toDate(process.getAuditPrelProcAnsDeadline())));
                    }
                    if (process.getAuditPrelProcClosedDeadline() != null) {
                        addDeadlineColumn(keyLabel(Lang.getProcessPreviewClosed()), new Label(Lang.toDate(process.getAuditPrelProcClosedDeadline())));
                    }
                    break;

                default:
                    LOG.log(Level.FINEST, "Unknown request type: " + latestRequest.getRequestTypeEnum());
            }

        }

        LOG.log(Level.FINER, "set data for workflow number {0} loaded in {1} ms", new Object[]{facade.getCurrentProcessNumber(), (System.currentTimeMillis() - startTime)});
    }

    public void addDeadlineColumn(Node... pElements) {
        //return if deadlines are null
        if (gpDeadlines == null) {
            return;
        }

        gpDeadlines.addColumn(gpDeadlines.getColumnCount(), pElements);
    }

    @Override
    public WmGeneralOperations getOperations() {
        return new WmGeneralOperations(facade);
    }

}
