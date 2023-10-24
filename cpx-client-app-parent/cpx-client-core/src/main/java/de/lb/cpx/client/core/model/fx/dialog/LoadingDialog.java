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
package de.lb.cpx.client.core.model.fx.dialog;

import de.lb.cpx.shared.lang.Lang;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

/**
 * Implements logic for the loading dialog Dialog contains an Progressbar, by
 * default set to indetermined And is styled by the DialogSkin Class
 *
 * loading dialog have no close button by default! AWI 20161215: Please remember
 * that you have to use closeDirtyAndForcefully to close the dialog As for now
 * JavaFx Dialogs only close if an close button with button data "CANCEL_CLOSE"
 * is present
 *
 * @author wilde
 */
public class LoadingDialog extends Dialog<Void> {

    private static final Logger LOG = Logger.getLogger(LoadingDialog.class.getName());

    private final DialogSkin<Void> skin;
    private final ProgressBar progressBar;

    /**
     * construct a new loading dialog with an indeterminded progress bar and
     * sets styling defined in DialogSkin
     */
    public LoadingDialog() {

        skin = new DialogSkin<>(this);
        skin.setTitle(Lang.getPleaseWait());
        setResultConverter(dialogButton -> null);
        VBox content = new VBox();
        content.setSpacing(10);
//        Label label = new Label(Lang.getPleaseWait());
        progressBar = new ProgressBar();
        //set indeterminate
        progressBar.setProgress(-1F);

        content.getChildren().addAll(progressBar);
        skin.getButtonTypes().clear();

        getDialogPane().setContent(content);

    }

    /**
     * binds the progress property of the progressbar to the progress of the
     * given task
     *
     * @param task running task for the loading dialog
     */
    public void bindPropressProperty(Task<?> task) {

        task.progressProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                LOG.warning("progress set to " + newValue + " from " + oldValue);
                setProgress(newValue.doubleValue());
            }
        });
    }

    /**
     * sets the progress from the outside
     *
     * @param pProgress progress to set
     */
    public synchronized void setProgress(double pProgress) {
        LOG.warning("progress set to " + pProgress + " from " + progressBar.getProgress());
        progressBar.setProgress(pProgress);
    }

    /**
     * Workaround: AWi 20161215 Closes the Dialog even when no close button is
     * set! In that case a close button is added and after close removed. Is to
     * be checked if javafx change closing behaviour to close a loading dialog
     * in a cleaner way
     */
    public void closeDirtyAndForcefully() {
        if (skin.containsCancelButton()) {
            close();
        }
        getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        close();
        getDialogPane().getButtonTypes().remove(ButtonType.CANCEL);
    }
}
