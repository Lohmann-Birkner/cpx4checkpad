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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.process.section.details;

import de.lb.cpx.client.core.model.fx.sectiontitle.SectionTitle;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;

/**
 * Implements the DetailSection in the Process GUI Details section is an ui
 * dummy to represent an empty details section
 *
 * @author wilde
 */
public class WmDetailSection extends SectionTitle {

    public WmDetailSection() {
        super("");
    }

    @Override
    public void setMenu() {
        //NOT IMPLEMENTED IN THIS DUMMY
    }

//    public void createMenuItems(final TWmEvent pEvent, final ProcessServiceFacade pFacade) {
//        if (pEvent == null) {
//            return;
//        }
//        if (pEvent.getAction() != null) {
//            final Button btnMenu = new Button();
//            btnMenu.setText("");
//            btnMenu.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.ELLIPSIS_V));
//            btnMenu.getStyleClass().add("cpx-icon-button");
//            btnMenu.setOnAction((ActionEvent event) -> {
//                final PopOver menu = new AutoFitPopOver();
//                final Button btnDeleteAction = new Button();
//                btnDeleteAction.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.TRASH));
//                btnDeleteAction.setMaxWidth(Double.MAX_VALUE);
//                btnDeleteAction.setText("Aktion löschen");
//                btnDeleteAction.setOnAction(new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(ActionEvent event) {
//                        AlertDialog dlg = AlertDialog.createConfirmationDialog("Möchten Sie die Aktion wirklich löschen?");
//                        dlg.initOwner(MainApp.getWindow());
//                        dlg.showAndWait().ifPresent(new Consumer<ButtonType>() {
//                            @Override
//                            public void accept(ButtonType t) {
//                                if (t.equals(ButtonType.OK)) {
//                                    //addCaseDialog();
//                                    pFacade.removeAction(pEvent.getAction());
////                                    LOG.log(Level.INFO, "Delete action");
////                                    pProcessServiceBean.get().removeAction(pEvent.getAction());
////                                    TWmEvent removedEvent = new TWmEvent();
////                                    removedEvent.setEventType(WmEventTypeEn.actionRemoved);
////                                    removedEvent.setProcess(pEvent.getProcess());
////                                    pProcessServiceBean.get().storeEvent(removedEvent);
////                                    pHistoryListView.reload();
//                                }
//                            }
//                        });
//                    }
//                });
//                final Button btnChangeAction = new Button();
//                btnChangeAction.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.PENCIL));
//                btnChangeAction.setMaxWidth(Double.MAX_VALUE);
//                btnChangeAction.setText("Aktion ändern");
//                btnChangeAction.setOnAction(new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(ActionEvent event) {
//                        UpdateActionDialog dialog = new UpdateActionDialog(pEvent.getAction(), pFacade);
//                        dialog.showAndWait();
//                    }
//                });
//                VBox menuContent = new VBox(btnChangeAction, btnDeleteAction);
//                menu.setContentNode(menuContent);
//                menu.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
//                menu.show(btnMenu);
//            });
//            getSkin().setMenu(btnMenu);
//            return;
//        }
//        if (pEvent.getReminder() != null) {
//            //NOT IMPLEMENTED YET
//        }
//        if (pEvent.getRequest() != null) {
//            final Button btnMenu = new Button();
//            btnMenu.setText("");
//            btnMenu.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.ELLIPSIS_V));
//            btnMenu.getStyleClass().add("cpx-icon-button");
//            btnMenu.setOnAction((ActionEvent event) -> {
//                final PopOver menu = new AutoFitPopOver();
//                final Button btnDeleteRequest = new Button();
//                btnDeleteRequest.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.TRASH));
//                btnDeleteRequest.setMaxWidth(Double.MAX_VALUE);
//                btnDeleteRequest.setText("Anfrage löschen");
//                btnDeleteRequest.setOnAction(new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(ActionEvent event) {
//                        AlertDialog dlg = AlertDialog.createConfirmationDialog("Möchten Sie die Anfrage wirklich löschen?");
//                        dlg.initOwner(MainApp.getWindow());
//                        dlg.showAndWait().ifPresent(new Consumer<ButtonType>() {
//                            @Override
//                            public void accept(ButtonType t) {
//                                if (t.equals(ButtonType.OK)) {
//                                    pFacade.removeRequest(pEvent.getRequest());
////                                    LOG.log(Level.INFO, "Delete Request");
////                                    pHistoryListView.reload();
//                                }
//                            }
//                        });
//                    }
//                });
//                VBox menuContent = new VBox(btnDeleteRequest);
//                menu.setContentNode(menuContent);
//                menu.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
//                menu.show(btnMenu);
//            });
//            getSkin().setMenu(btnMenu);
//            return;
//        }
//        if (pEvent.getHosCase() != null) {
//            //NOT IMPLEMENTED YET
//        }
//        if (pEvent.getKainInka() != null && pEvent.getKainInka().isKain()) {
//            //NOT IMPLEMENTED YET
//        }
//        if (pEvent.getKainInka() != null && pEvent.getKainInka().isInka() && (pEvent.getEventType().equals(WmEventTypeEn.inkaStored) || pEvent.getEventType().equals(WmEventTypeEn.inkaUpdated))
//                && !((TP301Inka) pEvent.getKainInka()).getIsSendedFl()) {
//        if (pEvent.getKainInka() != null && pEvent.getKainInka().isInka() && (pEvent.getEventType().equals(WmEventTypeEn.inkaStored) || pEvent.getEventType().equals(WmEventTypeEn.inkaUpdated))) {
//        if (pEvent.getKainInka() != null && pEvent.getKainInka().isInka() && !((TP301Inka) pEvent.getKainInka()).getIsSendedFl() && !((TP301Inka) pEvent.getKainInka()).getIsCancelledFl()) {
//            final Button btnMenu = new Button();
//            btnMenu.setText("");
//            btnMenu.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.ELLIPSIS_V));
//            btnMenu.getStyleClass().add("cpx-icon-button");
//            btnMenu.setOnAction((ActionEvent event) -> {
//                final PopOver menu = new AutoFitPopOver();
//                final Button btnSendInkaMsg = new Button();
//                btnSendInkaMsg.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.SEND));
//                btnSendInkaMsg.setMaxWidth(Double.MAX_VALUE);
//                btnSendInkaMsg.setText("Inka-Nachricht senden");
//                btnSendInkaMsg.setOnAction(new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(ActionEvent event) {
//                        AlertDialog dlg = AlertDialog.createConfirmationDialog("Möchten Sie die Inka-Nachricht wirklich schicken?");
//                        dlg.initOwner(MainApp.getWindow());
//                        dlg.showAndWait().ifPresent(new Consumer<ButtonType>() {
//                            @Override
//                            public void accept(ButtonType t) {
//                                if (t.equals(ButtonType.OK)) {
//                                    //call a method for the sending
//                                    try {
//                                        TP301KainInka sendInkaMessage = pFacade.sendInkaMessage(pEvent.getKainInka());
//
//                                        Notifications notifications = NotificationsFactory.instance().createInformationNotification();
//                                        notifications.title("Inka-Nachricht wurde erfolgreich gesendet.")
//                                                .text("Inka Nachricht mit IK des Krankenhauses " + pEvent.getKainInka().getHospitalIdentifier() + "und IK der Krankenkasse " + (pEvent.getKainInka().getInsuranceIdentifier()) + " wurde erfolgreich gesendet.")
//                                                .onAction((event) -> {
////                                                    event.consume();
//                                                }).show();
//
//                                        // only after successful sending, set sending flag and sending date
//                                        ((TP301Inka) sendInkaMessage).setIsSendedFl(true);
//                                        ((TP301Inka) sendInkaMessage).setSendingDate(new Date());
////                                        ((TP301Inka) sendInkaMessage).setProcessingRef("75");  // 75 ---> to indicate that Inka message is sent
//
//                                        pFacade.updateKainInka(sendInkaMessage, false); // save but don't create new Event.
//
//                                        LOG.log(Level.INFO, "Inka message was sucessfully sent..");
//
//                                    } catch (EJBException ex) {
//                                        LOG.log(Level.SEVERE, "Was not able to send Inka Message", ex);
//                                        Stage stage = (Stage) getSkin().getWindow();
//                                        MainApp.showErrorMessageDialog("Die INKA-Nachricht konnte nicht verschickt werden", ex.getCause().getLocalizedMessage(), stage);
//                                    }
//                                }
//                            }
//                        });
//                    }
//                });
//                VBox menuContent = new VBox(btnSendInkaMsg);
//                menu.setContentNode(menuContent);
//                menu.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
//                menu.show(btnMenu);
//            });
//            getSkin().setMenu(btnMenu);
//            return;
//        }
//        if (pEvent.getKainInka() != null && pEvent.getKainInka().isInka() && pEvent.getEventType().equals(WmEventTypeEn.inkaSent)
//                && ((TP301Inka) pEvent.getKainInka()).getIsSendedFl()) {
//        if (pEvent.getKainInka() != null && pEvent.getKainInka().isInka() && pEvent.getEventType().equals(WmEventTypeEn.inkaSent)) {
//        if (pEvent.getKainInka() != null && pEvent.getKainInka().isInka() && ((TP301Inka) pEvent.getKainInka()).getIsSendedFl() && !((TP301Inka) pEvent.getKainInka()).getIsCancelledFl()) {
//            final Button btnMenu = new Button();
//            btnMenu.setText("");
//            btnMenu.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.ELLIPSIS_V));
//            btnMenu.getStyleClass().add("cpx-icon-button");
//            btnMenu.setOnAction((ActionEvent event) -> {
//                final PopOver menu = new AutoFitPopOver();
//                final Button btnCancelInkaMsg = new Button();
//                btnCancelInkaMsg.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.REMOVE));
//                btnCancelInkaMsg.setMaxWidth(Double.MAX_VALUE);
//                btnCancelInkaMsg.setText("Inka-Nachricht stornieren");
//                btnCancelInkaMsg.setOnAction(new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(ActionEvent event) {
//                        AlertDialog dlg = AlertDialog.createConfirmationDialog("Möchten Sie die Inka-Nachricht wirklich stornieren?");
//                        dlg.initOwner(MainApp.getWindow());
//                        dlg.showAndWait().ifPresent(new Consumer<ButtonType>() {
//                            @Override
//                            public void accept(ButtonType t) {
//                                if (t.equals(ButtonType.OK)) {
//                                    try {
//                                        TP301KainInka kainInka = pEvent.getKainInka();
//                                        //call a method for cancellation
//                                        TP301KainInka cancelInkaMessage = pFacade.cancelInkaMessage(kainInka);
//
//                                        Notifications notifications = NotificationsFactory.instance().createInformationNotification();
//                                        notifications.title("Inka-Nachricht wurde erfolgreich storniert.")
//                                                .text("Inka Nachricht mit IK des Krankenhauses " + kainInka.getHospitalIdentifier() + "und IK der Krankenkasse " + (kainInka.getInsuranceIdentifier()) + " wurde erfolgreich storniert.")
//                                                .onAction((event) -> {
////                                                    event.consume();
//                                                }).show();
//
//                                        ((TP301Inka) cancelInkaMessage).setIsCancelledFl(true);
//                                        cancelInkaMessage.setProcessingRef("76");  // 76 ---> to indicate Inka message is removed
//
//                                        pFacade.updateKainInka(cancelInkaMessage, false);
//
//                                        LOG.log(Level.INFO, "Inka message was sucessfully cancelled..");
//
//                                    } catch (EJBException ex) {
//                                        LOG.log(Level.SEVERE, "Was not able to cancel an Inka Message", ex);
//                                        Stage stage = (Stage) getSkin().getWindow();
//                                        MainApp.showErrorMessageDialog("Die INKA-Nachricht konnte nicht storniert werden", ex.getCause().getLocalizedMessage(), stage);
//                                    }
//
//                                }
//                            }
//                        });
//                    }
//                });
//                VBox menuContent = new VBox(btnCancelInkaMsg);
//                menu.setContentNode(menuContent);
//                menu.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
//                menu.show(btnMenu);
//            });
//            getSkin().setMenu(btnMenu);
//            return;
//        }
//    }
    @Override
    public Parent getDetailContent() {
        return null;
    }

    @Override
    protected Parent createContent() {
        return new HBox();
    }

}
