/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018 nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.dialog;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.core.model.fx.dialog.FormularDialog;
import de.lb.cpx.model.TP301KainInka;
import javafx.event.ActionEvent;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import org.controlsfx.validation.ValidationSupport;

/**
 * Create new INKA Message Dialog with required controls and data.
 *
 * @author nandola
 */
public class AddInkaMessageDialog extends FormularDialog<TP301KainInka> {

    private final InkaMessageLayout inkaMessageLayout;

    public AddInkaMessageDialog(ProcessServiceFacade pServiceFacade) {
        this(pServiceFacade, null);
    }

    public AddInkaMessageDialog(ProcessServiceFacade pServiceFacade, final TP301KainInka kainInka) {
        super(MainApp.getStage(), Modality.APPLICATION_MODAL, "INKA-Nachricht");

        validationSupport.initInitialDecoration();

        getDialogPane().sceneProperty().get().getWindow().centerOnScreen();
        ButtonType sendBtnType = new ButtonType("Senden");
//        ButtonType cancelBtnType = new ButtonType("Stornierung");

        getDialogPane().getButtonTypes().addAll(sendBtnType);
        getDialogSkin().getButton(ButtonType.OK).setText("Speichern");

//        getDialogSkin().getButton(ButtonType.OK).disableProperty().bind(Bindings.isEmpty(listOfPvv));
//        getDialogSkin().getButton(sendBtnType).disableProperty().bind(Bindings.isEmpty(listOfPvv));
        getDialogSkin().getButton(sendBtnType).addEventFilter(
                ActionEvent.ACTION,
                evt -> {
                    // to prevent the dialog to close
//                    evt.consume();
//                    final TP301KainInka reqObj = onSave(false);
//                    final EjbProxy<ProcessServiceBeanRemote> processServiceBean = Session.instance().getEjbConnector().connectProcessServiceBean();
//                    processServiceBean.get().sendInkaMessage(reqObj);
                    onSend();
                }
        );

        inkaMessageLayout = new InkaMessageLayout(pServiceFacade, kainInka);
        inkaMessageLayout.setValidationSupport(validationSupport);

//        getDialogSkin().getButton(ButtonType.OK).disableProperty().unbind();
//        getDialogSkin().getButton(ButtonType.OK).disableProperty().bind(inkaMessageLayout.getValidationSupport().invalidProperty());
        getDialogSkin().getButton(sendBtnType).disableProperty().unbind();
        getDialogSkin().getButton(sendBtnType).disableProperty().bind(inkaMessageLayout.getValidationSupport().invalidProperty());

        // here, kainInka object is always of type "Kain", so no need to check extra conditions
//        if (kainInka.isInka()) {
//            getDialogSkin().getButton(sendBtnType).disableProperty().bind(inkaMessageLayout.getValidationSupport().invalidProperty().and(new SimpleBooleanProperty(((TP301Inka) kainInka).getIsSendedFl())));
//        } else {
//            getDialogSkin().getButton(sendBtnType).disableProperty().bind(inkaMessageLayout.getValidationSupport().invalidProperty());
//        }
//        getDialogSkin().getButton(cancelBtnType).disableProperty().unbind();
//        getDialogSkin().getButton(cancelBtnType).setDisable(true);    // always disabled while creating new Inka msg 
        Pane setUpLayoutForInkaMsg = inkaMessageLayout.setUpLayoutForInkaMsg(pServiceFacade, kainInka);

        addControlGroup(setUpLayoutForInkaMsg);
    }

    public ValidationSupport getValidationSupport() {
        return validationSupport;
    }

    @Override
    public TP301KainInka onSave() {
        return onSave(true, true);
    }

    public TP301KainInka onSave(final boolean pStore, final boolean isCreateNewEvent) {
        TP301KainInka kainInka = inkaMessageLayout.onSave(pStore, isCreateNewEvent);
        return kainInka;
    }

    //first save and then send
    public TP301KainInka onSend() {
        final TP301KainInka kainInka = onSave(true, false); // save but don't create new Event
//        final EjbProxy<ProcessServiceBeanRemote> processServiceBean = Session.instance().getEjbConnector().connectProcessServiceBean();
//        processServiceBean.get().sendInkaMessage(reqObj);
        TP301KainInka kainInkaObj = inkaMessageLayout.onSend(kainInka);
        return kainInkaObj;
    }

    //first save and then send (if not already sent)
//    public TP301KainInka onSend() {
//        final TP301KainInka kainInka = onSave(true, false); // save but don't create new Event
//        if (!((TP301Inka) kainInka).getIsSendedFl()) {
//            TP301KainInka kainInkaObj = inkaMessageLayout.onSend(kainInka);
//            return kainInkaObj;
//        }
//        return kainInka;
//    }
//    public void onCancel() {
//        TP301KainInka kainInka = onSave(false); // true or false?
//
//        // if it's of type INKA and already sent, then allow to cancel it.
//        if (kainInka.isInka() && ((TP301Inka) kainInka).getIsSendedFl()) {
//            inkaMessageLayout.onCancel(kainInka);
//        } else {
//            LOG.log(Level.WARNING, "can't cancel an Inka Message because it didn't sent successfully before.");
//        }
//    }
}
