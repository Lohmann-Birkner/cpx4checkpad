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
import de.lb.cpx.model.TP301Inka;
import de.lb.cpx.model.TP301KainInka;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import org.controlsfx.validation.ValidationSupport;

/**
 * To Update (and/or send) an INKA Message, Create INKA Message update dialog
 *
 * @author nandola
 */
public class UpdateInkaMessageDialog extends FormularDialog<TP301KainInka> {

    private static final Logger LOG = Logger.getLogger(UpdateInkaMessageDialog.class.getName());
    private InkaMessageLayout inkaMessageLayout;

    public UpdateInkaMessageDialog(ProcessServiceFacade facade) {
        this(facade, null);
    }

    public UpdateInkaMessageDialog(ProcessServiceFacade pServiceFacade, final TP301KainInka kainInka) {
        super(MainApp.getStage(), Modality.APPLICATION_MODAL, "Gespeicherte INKA-Nachricht");

        validationSupport.initInitialDecoration();

        getDialogPane().sceneProperty().get().getWindow().centerOnScreen();
        ButtonType sendBtnType = new ButtonType("Senden");
        ButtonType cancelBtnType = new ButtonType("Stornieren");
//        ButtonType saveBtnType = new ButtonType("Speichern");
        getDialogPane().getButtonTypes().addAll(cancelBtnType, sendBtnType);
        getDialogSkin().getButton(ButtonType.OK).setText("Speichern");

        Button sendBtn = getDialogSkin().getButton(sendBtnType);
        Button cancelbtn = getDialogSkin().getButton(cancelBtnType);

        getDialogSkin().getButton(sendBtnType).addEventFilter(
                ActionEvent.ACTION,
                evt -> {
                    // to prevent the dialog to close
//                    evt.consume();
                    onSend();
                }
        );

        getDialogSkin().getButton(cancelBtnType).addEventFilter(
                ActionEvent.ACTION,
                evt -> {
                    // to prevent the dialog to close
//                    evt.consume();
                    onCancel();
                }
        );

        inkaMessageLayout = new InkaMessageLayout(pServiceFacade, kainInka);
        inkaMessageLayout.setValidationSupport(validationSupport);

        getDialogSkin().getButton(sendBtnType).disableProperty().unbind();
        if (kainInka.isInka()) {
            getDialogSkin().getButton(sendBtnType).disableProperty().bind(inkaMessageLayout.getValidationSupport().invalidProperty().or(new SimpleBooleanProperty(((TP301Inka) kainInka).getIsSendedFl())));
        }

        getDialogSkin().getButton(cancelBtnType).disableProperty().unbind();
        if (kainInka.isInka()) {
//            getDialogSkin().getButton(cancelBtnType).disableProperty().bind(inkaMessageLayout.getValidationSupport().invalidProperty().or(new SimpleBooleanProperty(!((TP301Inka) kainInka).getIsSendedFl())));
            getDialogSkin().getButton(cancelBtnType).disableProperty().bind(inkaMessageLayout.getValidationSupport().invalidProperty().or(new SimpleBooleanProperty(!((TP301Inka) kainInka).getIsSendedFl())).or(new SimpleBooleanProperty(((TP301Inka) kainInka).getIsCancelledFl())));
        }

        Pane setUpLayoutForInkaMsg = inkaMessageLayout.setUpLayoutForInkaMsg(pServiceFacade, kainInka);
        inkaMessageLayout.setDataFromCreatedInkaMsg();

        addControlGroup(setUpLayoutForInkaMsg);

    }

    public ValidationSupport getValidationSupport() {
        return validationSupport;
    }

//    public void addControls(Pane setUpLayoutForInkaMsg) {
//        getDialogSkin().addControls(setUpLayoutForInkaMsg);
//    }
    @Override
    public TP301KainInka onSave() {
        return onSave(true, true);  // save and create new Event
    }

    public TP301KainInka onSave(final boolean pStore, final boolean isCreateNewEvent) {
        TP301KainInka kainInka = inkaMessageLayout.onSave(pStore, isCreateNewEvent);
        return kainInka;
    }

    //first save and then send
    public TP301KainInka onSend() {
        final TP301KainInka kainInka = onSave(true, false); // save but don't create new Event
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
    // no need to save. cancel it, if already sent and not already cancelled.
    public TP301KainInka onCancel() {
        TP301KainInka kainInka = onSave(false, false); // don't save and don't create new Event

        // if it's of type INKA and is already sent, then allow to cancel it.
        if (kainInka.isInka() && ((TP301Inka) kainInka).getIsSendedFl() && !((TP301Inka) kainInka).getIsCancelledFl()) {
            return inkaMessageLayout.onCancel(kainInka);
        } else {
            LOG.log(Level.WARNING, "can't cancel an Inka Message because it didn't sent successfully before.");
            return null;
        }
    }

}
