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
package de.lb.cpx.client.app.wm.fx.dialog;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.core.model.fx.dialog.FormularDialog;
import de.lb.cpx.client.core.model.fx.labeled.LabeledComboBox;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmReminder;
import de.lb.cpx.wm.model.TWmRequest;
import de.lb.cpx.wm.model.enums.WmRequestTypeEn;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Window;
import org.controlsfx.validation.ValidationSupport;

/**
 * create and show simple Request creation dialog, handles all necessary
 * functions to create a new request entity
 * 
 * TODO: Make the structure of editing TWmRequest same as TWmAction
 * @author wilde, nandola
 */
public class AddRequestDialog extends FormularDialog<TWmRequest> {

    private static final Logger LOG = Logger.getLogger(AddRequestDialog.class.getName());

    protected BooleanProperty addRequestProperty = new SimpleBooleanProperty(false);
    private final LabeledComboBox<WmRequestTypeEn> cbRequestType;
    private Node currentControlBox;
    private TWmRequest request;
    private final RequestLayout requestLayout;
//    private long caseId;
//    private final ObjectProperty<TWmRequest> requestProperty = new SimpleObjectProperty<>();
    private boolean save = true;
    private boolean manualCreate_Request = false;
//    private final ProcessServiceFacade facade;

    /**
     * creates a new Dialog to add a request to a existing process
     *
     * @param pFacade facade to access process services
     * @param hCaseId caseId to create process and request
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException case does not
     * exist
     */
    public AddRequestDialog(ProcessServiceFacade pFacade, long hCaseId) throws CpxIllegalArgumentException {
        this(pFacade, hCaseId, null);
        manualCreate_Request = true;
    }

    /**
     * creates a new Dialog to add a request to a existing process
     *
     * @param pFacade facade to access process services
     * @param hCaseId caseId to create process and request
     * @param pPreselectedRequestType selected request type
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException case does not
     * exist
     */
    public AddRequestDialog(ProcessServiceFacade pFacade, long hCaseId, final WmRequestTypeEn pPreselectedRequestType) throws CpxIllegalArgumentException {
        super(MainApp.getStage(), Modality.APPLICATION_MODAL, Lang.getMdkCreateRequestWindow());
        
        //for adding requests prevent autoresizing when messageNode is shown or hidden
        //otherwise dialog will attempt resizing when swithcing request types
        //this will result in an strange ui behavior
        setAutoResizeForMessageNode(false);

        WmRequestTypeEn preselectedRequestType = pPreselectedRequestType == null ? WmRequestTypeEn.bege : pPreselectedRequestType;
        validationSupport.initInitialDecoration();

        requestLayout = new RequestLayout(pFacade, request, hCaseId);
        messageTypeProperty().bind(requestLayout.messageTypeProperty());
        messageTextProperty().bind(requestLayout.messageTextProperty());
        requestLayout.setValidationSupport(validationSupport);

        final Window window = getDialogPane().getContent().getScene().getWindow();
        requestLayout.setOwnerWindow(window);

//        taComment = new LabeledTextArea(Lang.getComment());
        cbRequestType = new LabeledComboBox<>(Lang.getWmRequesttype());
//        cbRequestType.setMaxWidth(Double.MAX_VALUE);
        if(requestLayout.useReview()){
            cbRequestType.setItems(WmRequestTypeEn.valuesWithReview());
        }else{
            cbRequestType.setItems(WmRequestTypeEn.valuesNoReview());
        }
//        cbRequestType.getControl().showingProperty().addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                LOG.log(Level.INFO, "showing property changed from {0} to {1}", new Object[]{oldValue, newValue});
//            }
//        });

        cbRequestType.getSelectedItemProperty().addListener(new ChangeListener<WmRequestTypeEn>() {
            @Override
            public void changed(ObservableValue<? extends WmRequestTypeEn> observable, WmRequestTypeEn oldValue, WmRequestTypeEn newValue) {
                if (newValue == null) {
                    return;
                }
                if (currentControlBox != null) {
//                    requestLayout.getValidationSupport().getRegisteredControls().clear();
                    // remove the pane from the control
                    removeControlGroup(currentControlBox);
                }
                currentControlBox = setUpLayoutForRequestType(newValue);
//                getDialogPane().sceneProperty().get().getWindow().setWidth(getDialogSkin().getStage().getScene().getWidth() / 0.7);
                addControlGroup(currentControlBox);
                //restrict dialog in height
                getDialogPane().setMaxHeight(getOwner().getHeight() - 70);

                final Window window = getDialogPane().getContent().getScene().getWindow();
                requestLayout.setOwnerWindow(window);
                RequestLayout.resizeAndAlignWindow(getDialogPane().sceneProperty().get().getWindow());
            }
        });
        addControls(cbRequestType);

        cbRequestType.select(preselectedRequestType);

//        validationSupport.registerValidator();
//        cbRequestType.select(WmRequestType.mdk);
//        requestProperty.set(request);
//        getDialogSkin().getSaveButtonDisableProperty().bind(requestProperty.isNull());
        manualCreate_Request = false;
    }

//    public void setRequestType(WmRequestTypeEn pRequestType) {
//        cbRequestType.select(pRequestType);
//    }
    public void setSave(final boolean pSave) {
        save = pSave;
    }

    /**
     * get the addRequestProperty to inform a observer that a new request could
     * be obtained
     *
     * @return boolean property
     */
    public BooleanProperty getAddRequestProperty() {
        return addRequestProperty;
    }

    /**
     * to set up a layout based on request type
     *
     * @param pRequestType type of request
     * @return Pane in form of VBox
     */
    public Node setUpLayoutForRequestType(WmRequestTypeEn pRequestType) {

//        removeControls(labelInsurance, tfInsuranceName, tfInsuranceIdent, tfInsuranceZipCode, tfInsuranceCity,
//                labelAddData, labelAddress, labelTelephoneNumber, labelFax,
//                taComment);
//        removeControls(MdkName, Bearbeiter, EinleitungPrüfverfahrenKasse, Prüfgründe, EinleitungErweiterteMDKPrüfung,
//                ErweitertePrüfgründe, EinleitungMDKPrüfung, FristEinmaligeRechnungskorrektur, Gutachtendatum,
//                FristAbschlussMDKPrüfung, FortsetzungpauschaleGezahlt, EingangGutachten, Unterlagen,
//                angefordertAm_c, angefordertAm, gesendetAm_c, gesendetAm, AbgabefristBis, MdkStatus,
//                MDK_Ergebnis_Bemerkung, EigeneNotizen, InformationenMDK, MDKDienststelle, Postleitzahl,
//                Anschrift, MailAdresse, Vorwahl, Telefon, Fax);
        if (pRequestType != null) {
            switch (pRequestType) {
                case bege:
                    resetValidator();
                    //                validationSupport.getValidationResult().combine(requestLayout.getValidationSupport().getValidationResult());
                    //                requestLayout.setValidationSupport(validationSupport);
                    //                getDialogSkin().getMaxWidthProperty().divide(2); 
                    return requestLayout.getBegeRequestLayout(pRequestType);
                case mdk:
                    resetValidator();
                    //                requestLayout.setValidationSupport(validationSupport);
                    //                return requestLayout.getMDKLayout(pRequestType);
                    return requestLayout.getMDKLayout(pRequestType);
                case audit:
                    resetValidator();
                    //                requestLayout.setValidationSupport(validationSupport);
                    return requestLayout.getAuditRequestLayout(pRequestType);
                case insurance:
                    resetValidator();
                    return requestLayout.getInsuranceRequestLayout(pRequestType);
                case other:
                    resetValidator();
                    return requestLayout.getOtherRequestLayout(pRequestType);
                case review:
                    resetValidator();
                    return requestLayout.getReviewLayout(pRequestType);
                default:
                    LOG.log(Level.SEVERE, "Can not setUp Layout, unknown RequestType found {0}", pRequestType.toString());
            }
        }
        return new Pane();
    }

    private void resetValidator() {
        if (validationSupport.getRegisteredControls().isEmpty()) {
            return;
        }
        //dirty override, should lookout for a better way to reset the validation support
        validationSupport = new ValidationSupport();
        validationSupport.initInitialDecoration();

        requestLayout.setValidationSupport(validationSupport);

        getDialogSkin().getButton(ButtonType.OK).disableProperty().unbind();
        getDialogSkin().getButton(ButtonType.OK).disableProperty().bind(requestLayout.getValidationSupport().invalidProperty());
    }

    public void addControls(Pane setUpLayoutForRequestType) {
        getDialogSkin().addControls(setUpLayoutForRequestType);
    }

    public ValidationSupport getValidationSupport() {
        return validationSupport;
    }

    /**
     * This method will call with the OK button to save the request.
     *
     * @return request object
     */
    @Override
    public TWmRequest onSave() {
//        if (requestProperty.get() != null) {
        requestLayout.setCheckQuota(manualCreate_Request);
        request = requestLayout.onSave(save);
        return request;
//        }
//        return null;
    }

    /**
     * This method will call with the OK button to save the request.
     *
     * @return request object
     */
    public TWmRequest onSave(boolean checkQuota) {
//        if (requestProperty.get() != null) {
        requestLayout.setCheckQuota(checkQuota);
        request = requestLayout.onSave(save);
        return request;
//        }
//        return null;
    }

    
    public List<TWmReminder> getMdkReminders() {
        List<TWmReminder> rem = requestLayout.getMdkRequestReminders();
        return rem;
    }

    public List<TWmReminder> getAuditReminders() {
        List<TWmReminder> rem = requestLayout.getAuditRequestReminders();
        return rem;
    }
}
