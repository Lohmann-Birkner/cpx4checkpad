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
import de.lb.cpx.client.app.wm.fx.process.section.operations.WmReminderOperations;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmReminder;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Detailclass to expose detail pane to the history, avoid doublicate source
 * code
 *
 * @author wilde
 */
public class WmReminderDetails extends WmDetails<TWmReminder> {

    public WmReminderDetails(ProcessServiceFacade pFacade, TWmReminder pItem) {
        super(pFacade, pItem);
    }

//    public WmReminderDetails(ProcessServiceFacade pFacade) {
//        super(pFacade, null);
//    }
//    public WmReminderDetails(final ProcessServiceFacade pFacade, final TWmReminder pReminder) {
//        facade = pFacade;
//        reminder = pReminder;
//    }
    @Override
    public String getDetailTitle() {
        return Lang.getEventNameReminder(); //Wiedervorlagen
    }

//    @Override
//    protected Button createMenuItem() {
//        //NOT IMPLEMENTED YET
//        return null;
//    }
    /**
     * create from the TWmReminder the representation of the Reminder in its
     * detailview
     *
     * @return Parent pane root of the reminder detail
     */
    @Override
    protected Parent getDetailNode() {
        VBox detailContent = new VBox();
        TitledPane tpGenerelInfos = new TitledPane();
        tpGenerelInfos.setText(Lang.getGeneral());

        GridPane gpInfos = new GridPane();
        gpInfos.setVgap(10.0);
        gpInfos.setHgap(10.0);

        Label reminderIssuedFromText = new Label(Lang.getReminderSender());
        reminderIssuedFromText.getStyleClass().add("cpx-detail-label");
        reminderIssuedFromText.setWrapText(true);
        GridPane.setValignment(reminderIssuedFromText, VPos.TOP);
        Label reminderIssuedFrom = new Label(facade.getUserLabel(item.getCreationUser()) != null ? facade.getUserLabel(item.getCreationUser()) : "");
        reminderIssuedFrom.setWrapText(true);
        GridPane.setValignment(reminderIssuedFrom, VPos.TOP);

        Label reminderAsignedUserText = new Label(Lang.getReminderReceiver());
        reminderAsignedUserText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(reminderAsignedUserText, VPos.TOP);
        reminderAsignedUserText.setWrapText(true);
        Label reminderAsignedUser = new Label(facade.getUserLabel(item.getAssignedUserId()) != null ? facade.getUserLabel(item.getAssignedUserId()) : "");
        GridPane.setValignment(reminderAsignedUser, VPos.TOP);
        reminderAsignedUser.setWrapText(true);

        Label reminderTypeText = new Label(Lang.getReminderType());
        reminderTypeText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(reminderTypeText, VPos.TOP);
        reminderTypeText.setWrapText(true);
        //Session session = Session.instance();
//                EjbConnector connector = session.getEjbConnector();
//CWmListReminderSubject CWmListReminderSubject = connector.connectProcessServiceBean().get().getReminderSubjectById(reminder.getSubject());
        String reminderSubjectName = MenuCache.instance().getReminderSubjectsForInternalId(item.getSubject());
        Label reminderType = new Label(reminderSubjectName != null ? reminderSubjectName : "");
        reminderType.setWrapText(true);

        Label reminderCommentText = new Label(Lang.getComment());
        reminderCommentText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(reminderCommentText, VPos.TOP);
        Label reminderComment = new Label(item.getComment());
        reminderComment.setWrapText(true);

//        ScrollPane spReminderComment = new ScrollPane(reminderComment);
//        spReminderComment.getStyleClass().add("history-detail-scroll-pane");
//        spReminderComment.setFitToWidth(true);
//        spReminderComment.setMaxHeight(Double.MAX_VALUE);
        Label reminderCreationDateText = new Label(Lang.getReminderCreationDate());
        reminderCreationDateText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(reminderCreationDateText, VPos.TOP);
        reminderCreationDateText.setWrapText(true);
        Label reminderCreationDate = new Label(Lang.toDate(item.getCreationDate()));
        GridPane.setValignment(reminderCreationDate, VPos.TOP);

        Label reminderDueDateText = new Label(Lang.getDurationTo());
        reminderDueDateText.getStyleClass().add("cpx-detail-label");
        Label reminderDueDate = new Label(Lang.toDate(item.getDueDate()));

        Label reminderFinishedText = new Label(Lang.getAuditStatus());
        reminderFinishedText.getStyleClass().add("cpx-detail-label");
        reminderFinishedText.setWrapText(true);
        GridPane.setValignment(reminderFinishedText, VPos.TOP);
        Label reminderFinished = new Label();
        reminderFinished.setText(item.isFinished() ? Lang.getReminderFinishedStatus() : Lang.getProcessStatusReminderOpen());
        reminderFinished.setWrapText(true);
        GridPane.setValignment(reminderFinished, VPos.TOP);

        Label reminderFinishedDateText = new Label(Lang.getReminderFinishedDate());
        reminderFinishedDateText.getStyleClass().add("cpx-detail-label");
        reminderFinishedDateText.setWrapText(true);
        GridPane.setValignment(reminderFinishedDateText, VPos.TOP);
        Label reminderFinishedDate = new Label();
        reminderFinishedDate.setText(Lang.toDate(item.getFinishedDate()));
        reminderFinishedDate.setWrapText(true);
        GridPane.setValignment(reminderFinishedDate, VPos.TOP);

        Label reminderHighPriorityText = new Label(Lang.getReminderHighPriority());
        reminderHighPriorityText.getStyleClass().add("cpx-detail-label");
        reminderHighPriorityText.setWrapText(true);
        GridPane.setValignment(reminderHighPriorityText, VPos.TOP);
        Label reminderHighPriority = new Label();
        reminderHighPriority.setText(item.isHighPrio() ? Lang.getConfirmationYes() : Lang.getConfirmationNo());
        reminderHighPriority.setWrapText(true);
        GridPane.setValignment(reminderHighPriority, VPos.TOP);

        gpInfos.add(reminderIssuedFromText, 0, 0);
        gpInfos.add(reminderIssuedFrom, 1, 0);
        gpInfos.add(reminderAsignedUserText, 0, 1);
        gpInfos.add(reminderAsignedUser, 1, 1);
        gpInfos.add(reminderTypeText, 0, 2);
        gpInfos.add(reminderType, 1, 2);
        gpInfos.add(reminderCreationDateText, 0, 3);
        gpInfos.add(reminderCreationDate, 1, 3);
        gpInfos.add(reminderDueDateText, 0, 4);
        gpInfos.add(reminderDueDate, 1, 4);
        gpInfos.add(reminderFinishedText, 0, 5);
        gpInfos.add(reminderFinished, 1, 5);
        gpInfos.add(reminderFinishedDateText, 0, 6);
        gpInfos.add(reminderFinishedDate, 1, 6);
        gpInfos.add(reminderHighPriorityText, 0, 7);
        gpInfos.add(reminderHighPriority, 1, 7);
        gpInfos.add(reminderCommentText, 0, 8);
        gpInfos.add(reminderComment, 0, 9);
        GridPane.setColumnSpan(reminderComment, 2);
        GridPane.setVgrow(reminderComment, Priority.ALWAYS);

        tpGenerelInfos.setContent(gpInfos);

        ColumnConstraints columnConstraintHalf = new ColumnConstraints();
        columnConstraintHalf.setPercentWidth(50);

        gpInfos.getColumnConstraints().add(columnConstraintHalf);

        detailContent.getChildren().addAll(tpGenerelInfos);

        ScrollPane content = new ScrollPane(detailContent);
        detailContent.maxWidthProperty().bind(content.widthProperty().subtract(2));
        content.setFitToHeight(true);

        return content;
    }

    @Override
    public WmReminderOperations getOperations() {
        return new WmReminderOperations(facade);
    }

}
