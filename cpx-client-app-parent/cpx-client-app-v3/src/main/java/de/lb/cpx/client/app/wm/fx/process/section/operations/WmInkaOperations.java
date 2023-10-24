/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.process.section.operations;

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.dialog.AddInkaMessageDialog;
import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.model.fx.alert.NotificationsFactory;
import de.lb.cpx.model.TP301Inka;
import de.lb.cpx.model.TP301KainInka;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;
import javax.ejb.EJBException;
import org.controlsfx.control.Notifications;
import org.controlsfx.glyphfont.FontAwesome;

/**
 *
 * @author niemeier
 */
public class WmInkaOperations extends WmKainInkaOperations<TP301Inka> {

    private static final Logger LOG = Logger.getLogger(WmInkaOperations.class.getName());

    public WmInkaOperations(ProcessServiceFacade pFacade) {
        super(pFacade);
    }

    @Override
    protected List<ItemEventHandler> getOtherOperations(final TP301Inka pItem) {
        final List<ItemEventHandler> result = new ArrayList<>();
//        EventHandler<Event> addInka = addInka(pItem);
//        if (addInka != null) {
//            result.add(new ItemEventHandler(addInka, ResourceLoader.getGlyph(FontAwesome.Glyph.PLUS), "Inka-Nachricht anlegen"));
//        }
        ItemEventHandler sendInka = sendInka(pItem);
        if (sendInka != null) {
            result.add(sendInka);
        }
        ItemEventHandler cancelInka = cancelInka(pItem);
        if (cancelInka != null) {
            result.add(cancelInka);
        }
        return result;
    }

    public ItemEventHandler sendInka(final TP301Inka pItem) {
        if (pItem == null || !pItem.isInka()) {
            return null;
        }
//        final TP301Inka inka = (TP301Inka) pItem;
        if (pItem.getIsCancelledFl()) {
            return null;
        }
        if (pItem.getIsSendedFl()) {
            return null;
        }
        return new ItemEventHandler('\uf05e' /* uf05e = TELEGRAM */, "Senden", '\uf05e' /* uf05e = TELEGRAM */, "INKA-Nachricht senden", false) {
            @Override
            public void handle(ActionEvent evt) {
                AlertDialog dlg = AlertDialog.createConfirmationDialog("Möchten Sie die Inka-Nachricht wirklich schicken?");
                dlg.initOwner(MainApp.getWindow());
                dlg.showAndWait().ifPresent((t) -> {
                    if (t.equals(ButtonType.OK)) {
                        //call a method for the sending
                        try {
                            TP301KainInka sendInkaMessage = facade.sendInkaMessage(pItem);
                            Notifications notifications = NotificationsFactory.instance().createInformationNotification();
                            notifications.title("Inka-Nachricht wurde erfolgreich gesendet.")
                                    .text("Inka Nachricht mit IK des Krankenhauses " + pItem.getHospitalIdentifier() + "und IK der Krankenkasse " + (pItem.getInsuranceIdentifier()) + " wurde erfolgreich gesendet.").onAction((event2) -> {
//                                                    event.consume();
                            }).show();
                            // only after successful sending, set sending flag and sending date
                            ((TP301Inka) sendInkaMessage).setIsSendedFl(true);
                            ((TP301Inka) sendInkaMessage).setSendingDate(new Date());
//                                        ((TP301Inka) sendInkaMessage).setProcessingRef("75");  // 75 ---> to indicate that Inka message is sent
                            facade.updateKainInka(sendInkaMessage, false); // save but don't create new Event.
                            LOG.log(Level.INFO, "Inka message was sucessfully sent..");
                        } catch (EJBException ex) {
                            LOG.log(Level.SEVERE, "Was not able to send Inka Message", ex);
                            Window window = dlg.getDialogPane() == null || dlg.getDialogPane().getScene() == null ? null : dlg.getDialogPane().getScene().getWindow();
                            MainApp.showErrorMessageDialog("Die INKA-Nachricht konnte nicht verschickt werden", ex.getCause().getLocalizedMessage(), window);
                        }
                    }
                });
            }
        };
    }

    public ItemEventHandler cancelInka(final TP301Inka pItem) {
        if (pItem == null || !pItem.isInka()) {
            return null;
        }
//        final TP301Inka inka = (TP301Inka) pItem;
        if (pItem.getIsCancelledFl()) {
            return null;
        }
        if (!pItem.getIsSendedFl()) {
            return null;
        }
        return new ItemEventHandler(FontAwesome.Glyph.BAN, "Stornieren", FontAwesome.Glyph.BAN, "INKA-Nachricht stornieren", false) {
            @Override
            public void handle(ActionEvent evt) {
                AlertDialog dlg = AlertDialog.createConfirmationDialog("Möchten Sie die Inka-Nachricht wirklich stornieren?");
                dlg.initOwner(MainApp.getWindow());
                dlg.showAndWait().ifPresent((t) -> {
                    if (t.equals(ButtonType.OK)) {
                        try {
                            //                                            TP301KainInka kainInka = inka;
                            //call a method for cancellation
                            TP301KainInka cancelInkaMessage = facade.cancelInkaMessage(pItem);
                            Notifications notifications = NotificationsFactory.instance().createInformationNotification();
                            notifications.title("Inka-Nachricht wurde erfolgreich storniert.")
                                    .text("Inka Nachricht mit IK des Krankenhauses " + pItem.getHospitalIdentifier() + "und IK der Krankenkasse " + (pItem.getInsuranceIdentifier()) + " wurde erfolgreich storniert.").onAction((event2) -> {
//                                                    event.consume();
                            }).show();
                            ((TP301Inka) cancelInkaMessage).setIsCancelledFl(true);
                            cancelInkaMessage.setProcessingRef("76");  // 76 ---> to indicate Inka message is removed
                            facade.updateKainInka(cancelInkaMessage, false);
                            LOG.log(Level.INFO, "Inka message was sucessfully cancelled..");
                        } catch (EJBException ex) {
                            LOG.log(Level.SEVERE, "Was not able to cancel an Inka Message", ex);
                            Window window = dlg.getDialogPane() == null || dlg.getDialogPane().getScene() == null ? null : dlg.getDialogPane().getScene().getWindow();
                            MainApp.showErrorMessageDialog("Die INKA-Nachricht konnte nicht storniert werden", ex.getCause().getLocalizedMessage(), window);
                        }
                    }
                });
            }
        };
    }

    public ItemEventHandler addInka(final TP301KainInka pItem) {
        if (pItem == null) {
            return null;
        }
        if (!pItem.isKain()) {
            return null;
        }
        return new ItemEventHandler(FontAwesome.Glyph.PLUS, Lang.getEventOperationCreate(), FontAwesome.Glyph.PLUS, Lang.getEventOperationCreateItem(getItemName()), false) {
            @Override
            public void handle(ActionEvent evt) {
                AddInkaMessageDialog inkaMessageDialog = new AddInkaMessageDialog(facade, pItem);
                inkaMessageDialog.showAndWait();
            }
        };
    }

//    @Override
//    public EventHandler<Event> editItem(TP301Inka pItem) {
////        TP301KainInka kainInka = pEvent.getKainInka();
//        if (pItem == null) {
//            return null;
//        }
//        if (!pItem.isInka()) {
//            return null;
//        }
//        return new EventHandler<Event>() {
//            @Override
//            public void handle(Event evt) {
//                UpdateInkaMessageDialog updateInkaMessageDialog = new UpdateInkaMessageDialog(facade, pItem);
//                updateInkaMessageDialog.getResultsProperty().addListener((ObservableValue<? extends TP301KainInka> observable, TP301KainInka oldValue, TP301KainInka newValue) -> {
////                    pEvent.setKainInka(newValue);
//                });
//                updateInkaMessageDialog.showAndWait();
//            }
//        };
//    }
//    @Override
//    protected Button createMenuItem() {
//        TP301Inka inka = getInka();
//        if (inka != null && !inka.getIsCancelledFl()) {
//            final Button btnMenu = new Button();
//            btnMenu.setText("");
//            btnMenu.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.ELLIPSIS_V));
//            btnMenu.getStyleClass().add("cpx-icon-button");
//            final Button btn = new Button();
//            btn.setMaxWidth(Double.MAX_VALUE);
//
//            if (!inka.getIsSendedFl()) {
//                //INKA message was not sent yet
//                btnMenu.setOnAction((ActionEvent event) -> {
//                    btn.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.SEND));
//                    btn.setText("Inka-Nachricht senden");
//                    btn.setOnAction((ActionEvent event1) -> {
//                        AlertDialog dlg = AlertDialog.createConfirmationDialog("Möchten Sie die Inka-Nachricht wirklich schicken?");
//                        dlg.initOwner(MainApp.getWindow());
//                        dlg.showAndWait().ifPresent((t) -> {
//                            if (t.equals(ButtonType.OK)) {
//                                //call a method for the sending
//                                try {
//                                    TP301KainInka sendInkaMessage = facade.sendInkaMessage(inka);
//                                    Notifications notifications = NotificationsFactory.instance().createInformationNotification();
//                                    notifications.title("Inka-Nachricht wurde erfolgreich gesendet.")
//                                            .text("Inka Nachricht mit IK des Krankenhauses " + inka.getHospitalIdentifier() + "und IK der Krankenkasse " + (inka.getInsuranceIdentifier()) + " wurde erfolgreich gesendet.").onAction((event2) -> {
////                                                    event.consume();
//                                    }).show();
//                                    // only after successful sending, set sending flag and sending date
//                                    ((TP301Inka) sendInkaMessage).setIsSendedFl(true);
//                                    ((TP301Inka) sendInkaMessage).setSendingDate(new Date());
////                                        ((TP301Inka) sendInkaMessage).setProcessingRef("75");  // 75 ---> to indicate that Inka message is sent
//                                    facade.updateKainInka(sendInkaMessage, false); // save but don't create new Event.
//                                    LOG.log(Level.INFO, "Inka message was sucessfully sent..");
//                                } catch (EJBException ex) {
//                                    LOG.log(Level.SEVERE, "Was not able to send Inka Message", ex);
//                                    Window window = dlg.getDialogPane() == null || dlg.getDialogPane().getScene() == null ? null : dlg.getDialogPane().getScene().getWindow();
//                                    MainApp.showErrorMessageDialog("Die INKA-Nachricht konnte nicht verschickt werden", ex.getCause().getLocalizedMessage(), window);
//                                }
//                            }
//                        });
//                    });
//                });
//            } else { //if (inka.getIsSendedFl()) {...}
//                //INKA message was already sent
//                btnMenu.setOnAction((ActionEvent event) -> {
//                    btn.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.REMOVE));
//                    btn.setText("Inka-Nachricht stornieren");
//                    btn.setOnAction((ActionEvent event1) -> {
//                        AlertDialog dlg = AlertDialog.createConfirmationDialog("Möchten Sie die Inka-Nachricht wirklich stornieren?");
//                        dlg.initOwner(MainApp.getWindow());
//                        dlg.showAndWait().ifPresent((t) -> {
//                            if (t.equals(ButtonType.OK)) {
//                                try {
//                                    //                                            TP301KainInka kainInka = inka;
//                                    //call a method for cancellation
//                                    TP301KainInka cancelInkaMessage = facade.cancelInkaMessage(inka);
//                                    Notifications notifications = NotificationsFactory.instance().createInformationNotification();
//                                    notifications.title("Inka-Nachricht wurde erfolgreich storniert.")
//                                            .text("Inka Nachricht mit IK des Krankenhauses " + inka.getHospitalIdentifier() + "und IK der Krankenkasse " + (inka.getInsuranceIdentifier()) + " wurde erfolgreich storniert.").onAction((event2) -> {
////                                                    event.consume();
//                                    }).show();
//                                    ((TP301Inka) cancelInkaMessage).setIsCancelledFl(true);
//                                    cancelInkaMessage.setProcessingRef("76");  // 76 ---> to indicate Inka message is removed
//                                    facade.updateKainInka(cancelInkaMessage, false);
//                                    LOG.log(Level.INFO, "Inka message was sucessfully cancelled..");
//                                } catch (EJBException ex) {
//                                    LOG.log(Level.SEVERE, "Was not able to cancel an Inka Message", ex);
//                                    Window window = dlg.getDialogPane() == null || dlg.getDialogPane().getScene() == null ? null : dlg.getDialogPane().getScene().getWindow();
//                                    MainApp.showErrorMessageDialog("Die INKA-Nachricht konnte nicht storniert werden", ex.getCause().getLocalizedMessage(), window);
//                                }
//                            }
//                        });
//                    });
//                });
//            }
//            VBox menuContent = new VBox(btn);
//            final PopOver menu = new AutoFitPopOver();
//            menu.setContentNode(menuContent);
//            menu.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
//            menu.show(btnMenu);
//            return btnMenu;
//        }
//        return null;
//    }
    @Override
    public String getItemName() {
        return Lang.getEventNameInka();
    }

}
