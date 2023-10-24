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
import de.lb.cpx.client.app.wm.fx.process.section.operations.ItemEventHandler;
import de.lb.cpx.client.app.wm.fx.process.section.operations.WmActionOperations;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmAction;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.controlsfx.glyphfont.FontAwesome;

/**
 * Detailclass to expose detail pane to the history, avoid doublicate source
 * code
 *
 * @author wilde
 */
public class WmActionDetails extends WmDetails<TWmAction> {

    public WmActionDetails(ProcessServiceFacade pFacade, TWmAction pItem) {
        super(pFacade, pItem);
    }

//    public WmActionDetails(ProcessServiceFacade pFacade) {
//        super(pFacade, null);
//    }
    @Override
    public String getDetailTitle() {
        return Lang.getEventNameAction();
    }

//    @Override
//    protected Button createMenuItem() {
//        final Button btnMenu = new Button();
//        btnMenu.setText("");
//        btnMenu.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.ELLIPSIS_V));
//        btnMenu.getStyleClass().add("cpx-icon-button");
//        btnMenu.setOnAction((ActionEvent event) -> {
//            final PopOver menu = new AutoFitPopOver();
//            final Button btnDeleteAction = new Button();
//            btnDeleteAction.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.TRASH));
//            btnDeleteAction.setMaxWidth(Double.MAX_VALUE);
//            btnDeleteAction.setText("Aktion löschen");
//            btnDeleteAction.setOnAction(new EventHandler<ActionEvent>() {
//                @Override
//                public void handle(ActionEvent event) {
//                    EventHandler<Event> eh = removeItem();
//                    if (eh != null) {
//                        eh.handle(event);
//                    }
//                }
//            });
//            final Button btnChangeAction = new Button();
//            btnChangeAction.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.PENCIL));
//            btnChangeAction.setMaxWidth(Double.MAX_VALUE);
//            btnChangeAction.setText("Aktion ändern");
//            btnChangeAction.setOnAction(new EventHandler<ActionEvent>() {
//                @Override
//                public void handle(ActionEvent event) {
//                    EventHandler<Event> eh = editItem();
//                    if (eh != null) {
//                        eh.handle(event);
//                    }
//                }
//            });
//            VBox menuContent = new VBox(btnChangeAction, btnDeleteAction);
//            menu.setContentNode(menuContent);
//            menu.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
//            menu.show(btnMenu);
//        });
//        return btnMenu;
////            getSkin().setMenu(btnMenu);
//    }
    @Override
    protected Parent getDetailNode() {
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

        Label labelCaseAddedDateText = new Label(Lang.getActionCreationDate());
        labelCaseAddedDateText.getStyleClass().add("cpx-detail-label");
        labelCaseAddedDateText.setWrapText(true);
        GridPane.setValignment(labelCaseAddedDateText, VPos.TOP);
        Label labelCaseAddedDate = new Label(Lang.toDate(item.getCreationDate()));
        GridPane.setValignment(labelCaseAddedDate, VPos.TOP);

        Label labelActionCommentText = new Label(Lang.getComment());
        labelActionCommentText.getStyleClass().add("cpx-detail-label");
        GridPane.setValignment(labelActionCommentText, VPos.TOP);

        Label labelActionTypeText = new Label(Lang.getActionType());
        labelActionTypeText.getStyleClass().add("cpx-detail-label");

        Label labelActionType = new Label(MenuCache.instance().getActionSubjectName(item.getActionType()));
        Label labelActionComment = new Label(item.getComment() != null ? String.valueOf(item.getComment()) : "");
        labelActionComment.setWrapText(true);

        ScrollPane spActionComment = new ScrollPane(labelActionComment);
        spActionComment.getStyleClass().add("history-detail-scroll-pane");
        spActionComment.setFitToWidth(true);
        spActionComment.setMaxHeight(Double.MAX_VALUE);

        Button btnSendMail = new Button();
        btnSendMail.setMaxHeight(Double.MAX_VALUE);
        btnSendMail.setAlignment(Pos.CENTER);
        btnSendMail.setMinWidth(30);
        btnSendMail.setTooltip(new Tooltip("versenden"));
        btnSendMail.getStyleClass().add("cpx-icon-button");
        btnSendMail.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.ENVELOPE));
        btnSendMail.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ItemEventHandler eh = getOperations().sendMailItem(item);
                if (eh != null) {
                    eh.handle(event);
                }
//                MailCreator mailCreator = new MailCreator();
//                mailCreator.sendMail(facade.getCurrentProcess(), item);
            }
        });

        gpInfos.add(labelUserText, 0, 0);
        gpInfos.add(labelUser, 1, 0);
        gpInfos.add(labelCaseAddedDateText, 0, 1);
        gpInfos.add(labelCaseAddedDate, 1, 1);
        gpInfos.add(labelActionTypeText, 0, 2);
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setFillHeight(true);
        hbox.setSpacing(2);
//        hbox.getChildren().addAll(cbActionType, btnSendMail);
        hbox.getChildren().addAll(labelActionType, btnSendMail);
        gpInfos.add(hbox, 1, 2);

        gpInfos.add(labelActionCommentText, 0, 3);
        gpInfos.add(spActionComment, 0, 4);
        GridPane.setColumnSpan(spActionComment, 2);
        GridPane.setVgrow(spActionComment, Priority.ALWAYS);
        ColumnConstraints columnConstraintHalf = new ColumnConstraints();
        columnConstraintHalf.setPercentWidth(50);
        //Awi-20180323-CPX889
        //add row contrains to grab all space thats left for last row
        gpInfos.getColumnConstraints().add(columnConstraintHalf);
        tpGenerelInfos.setContent(gpInfos);
        tpGenerelInfos.disableProperty().setValue(!Session.instance().getRoleProperties().isEditActionAllowed());

        return tpGenerelInfos;
    }

    @Override
    public WmActionOperations getOperations() {
        return new WmActionOperations(facade);
    }

}
