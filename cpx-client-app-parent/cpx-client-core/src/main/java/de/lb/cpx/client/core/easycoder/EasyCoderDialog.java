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
package de.lb.cpx.client.core.easycoder;

import de.lb.cpx.client.core.model.fx.dialog.TitledDialog;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseOps;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogEvent;
import javafx.stage.Modality;
import javafx.stage.Window;

/**
 *
 * @author wilde
 */
public class EasyCoderDialog extends TitledDialog {

    private static final Logger LOG = Logger.getLogger(EasyCoderDialog.class.getName());
    private static final Double PREF_DIALOG_HEIGHT = 800.0d;
    private EasyCoderDialogScene contentScene;

    public EasyCoderDialog(Window pOwner, Modality pModality, String pTitle, final Date pAdmissionDate) {
        this(pOwner, pModality, pTitle, getYearFromDate(pAdmissionDate));
    }

    public EasyCoderDialog(Window pOwner, Modality pModality, String pTitle, final int pYear) {
        super(pTitle + " / " + pYear, pOwner, pModality);
//        super();
//        initOwner(pOwner);
//        initModality(pModality);
//        setHeaderText(pTitle);
        try {
//            setDialogSkin(new DialogSkin(this));
            contentScene = new EasyCoderDialogScene();
            contentScene.getController().init(pYear);
//            setDialogSkin(new DialogSkin(this));
            getDialogPane().setContent(contentScene.getRoot());
//            getDialogPane().heightProperty().addListener(new ChangeListener<Number>() {
//                @Override
//                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                    getDialogPane().getScene().getWindow().sizeToScene();
//                }
//            });
//            getDialogPane().prefHeightProperty().bind(getDialogPane().getScene().heightProperty());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        dialogSkinProperty.get().addButtonTypes(ButtonType.OK);
        //getDialogSkin().addButtonTypes(ButtonType.OK);

        //calculate height remaining on window that dialog does not exceed bounds 
        setOnShowing(new EventHandler<DialogEvent>() {
            @Override
            public void handle(DialogEvent t) {
                if (getDialogSkin().getCurrentScreen().getBounds().getHeight() < PREF_DIALOG_HEIGHT) {
                    getDialogPane().setPrefHeight(getDialogSkin().getCurrentScreen().getBounds().getHeight() - 100);
                } else {
                    getDialogPane().setPrefHeight(PREF_DIALOG_HEIGHT);
                }
            }
        });
    }

    public EasyCoderDialog(Window pOwner, final Date pAdmissionDate) {
        this(pOwner, getYearFromDate(pAdmissionDate));
    }

    public EasyCoderDialog(Window pOwner, final int pYear) {
        this(pOwner, Modality.APPLICATION_MODAL, "ICD-, OPS-Bearbeitung", pYear);
    }

    private static int getYearFromDate(final Date pDate) {
        final int year;
        if (pDate == null) {
            year = 0;
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(pDate);
            year = cal.get(Calendar.YEAR);
        }
        return year;
    }

    public void setIcdList(ObservableList<TCaseIcd> observableList) {
        contentScene.getController().setIcdList(observableList);
    }

    public void setOpsList(ObservableList<TCaseOps> observableList) {
        contentScene.getController().setOpsList(observableList);
    }

    public ObservableList<TCaseOps> getSelectedTCaseOps() {
        return contentScene.getController().selectedTCaseOps;
    }

    public ObservableList<TCaseIcd> getSelectedTCaseIcd() {
        return contentScene.getController().selectedTCaseIcd;
    }

    public void setDiagnosenSearch(String icd) {
        contentScene.getController().setDiagnosenSearch(icd);
    }

    public void setProzudureSearch(String ops) {
        contentScene.getController().setProcedureSearch(ops);

    }

    public void closeEasyCoder() {
        contentScene.getController().close();
    }

}
