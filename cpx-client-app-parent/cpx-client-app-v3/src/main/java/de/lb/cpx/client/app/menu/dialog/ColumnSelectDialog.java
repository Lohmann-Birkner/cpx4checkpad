/*
 * Copyright (c) 2017 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner Health Care Consulting GmbH and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner Health Care Consulting GmbH
 * http://www.lohmann-birkner.de
 *
 * Contributors:
 *    2017  Bohm - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.menu.dialog;

import de.lb.cpx.client.core.model.fx.dialog.DialogSkin;
import de.lb.cpx.client.core.model.fx.dialog.TitledDialog;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

/**
 *
 * @author Alexander Bohm
 */
public class ColumnSelectDialog extends TitledDialog {

    private static final Logger LOG = Logger.getLogger(ColumnSelectDialog.class.getName());

    private ColumnSelectScene scene;

    protected ValidationSupport validationSupport;
    
    private Callback<String, Boolean> onIsFilterNameExistCallback;
    public ColumnSelectDialog(Window pOwner, Modality pModality, String pTitle) {
        super(pTitle, pOwner, pModality, true);
        try {
        validationSupport = new ValidationSupport();
        validationSupport.initInitialDecoration();

            scene = new ColumnSelectScene();
            ScrollPane pane = new ScrollPane(scene.getRoot());
            pane.setFitToHeight(true);
            pane.setFitToWidth(true);
            getDialogPane().setContent(pane);
//            getDialogPane().minHeightProperty().bind(scene.heightProperty().subtract(80));
            Stage s = (Stage) this.getDialogPane().getScene().getWindow();
//            s.setMinHeight(500);
//            s.setMinWidth(750);
//            s.setMaxWidth(750);
            setDialogSkin(new DialogSkin<>(this));
        } catch (IOException ex) {
            Logger.getLogger(ColumnSelectDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ColumnSelectScene getScene() {
        return scene;
    }
    

    public void registerValidator(){
        ColumnSelectFXMLController controller = (ColumnSelectFXMLController) getScene().getController();
        TextField nameField = controller.getNameItem();
        validationSupport.registerValidator(nameField, new Validator<String>(){
          @Override
            public ValidationResult apply(Control t, String u) {
                ValidationResult res = new ValidationResult();
                
                res.addErrorIf(t, "Filter mit dem Namen schon vorhanden ", onIsFilterNameExistCallback.call(u));
                getDialogSkin().getButton(ButtonType.OK).disableProperty().setValue(onIsFilterNameExistCallback.call(u));
                
                LOG.log(Level.FINEST, "getDialogSkin().getButton(ButtonType.OK).disableProperty() " + getDialogSkin().getButton(ButtonType.OK).disableProperty().getValue());
                LOG.log(Level.FINEST, "validationSupport.isInvalid() " + validationSupport.isInvalid());
                return res;
            }
            
        });
    }
    


    public void setIsFilterNameExistCallback(Callback<String, Boolean> callback) {
        onIsFilterNameExistCallback = callback;
    }
}
