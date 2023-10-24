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
 *    nandola
 */
package de.lb.cpx.client.app.wm.fx.dialog;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.core.model.fx.dialog.FormularDialog;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.wm.model.TWmRequest;
import de.lb.cpx.wm.model.TWmRequestAudit;
import de.lb.cpx.wm.model.TWmRequestBege;
import de.lb.cpx.wm.model.TWmRequestInsurance;
import de.lb.cpx.wm.model.TWmRequestMdk;
import de.lb.cpx.wm.model.TWmRequestOther;
import de.lb.cpx.wm.model.TWmRequestReview;
import de.lb.cpx.wm.model.enums.WmRequestTypeEn;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Window;
import org.controlsfx.validation.ValidationSupport;

/**
 * create and show Request update dialog, handles all necessary functions to
 * update a new request entity
 *
 * TODO: Make the structure of editing TWmRequest same as TWmAction
 * @author nandola
 */
public class UpdateRequestDialog extends FormularDialog<TWmRequest> {

    private static final Logger LOG = Logger.getLogger(UpdateRequestDialog.class.getSimpleName());
    protected BooleanProperty addRequestProperty = new SimpleBooleanProperty(false);
//    private final TWmRequest request;
    private TWmRequestMdk mdkReq;
    private TWmRequestBege begeReq;
    private TWmRequestAudit auditReq;

    private RequestLayout requestLayout;
    private long hCaseId;
    private TWmRequestInsurance auditInsurance;
    private TWmRequestOther auditOther;
    private TWmRequestReview reviewReq;

    /**
     * creates a new Dialog to update a request to a existing process
     *
     * @param pFacade facade to access server services
     * @param pRequest request object
     * @param headerName header name depends on request type
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException case does not
     * exist
     */
    public UpdateRequestDialog(ProcessServiceFacade pFacade, TWmRequest pRequest, String headerName) throws CpxIllegalArgumentException {
        super(MainApp.getStage(), Modality.APPLICATION_MODAL, headerName);
        validationSupport.initInitialDecoration();
        if (pRequest == null) {
            LOG.log(Level.WARNING, "passed request object is null!");
            return;
        }
        LOG.log(Level.INFO, "open update dialog for request with id {0}", pRequest.id);
//        this.request = request;

        if (pRequest.getRequestType() != null) {
            switch (pRequest.getRequestTypeEnum()) {
                case bege:
                    this.begeReq = (TWmRequestBege) pRequest;
                    requestLayout = new RequestLayout(pFacade, begeReq, hCaseId);
                    requestLayout.setValidationSupport(validationSupport);
                    break;
                case mdk:
                    this.mdkReq = (TWmRequestMdk) pRequest;
                    requestLayout = new RequestLayout(pFacade, mdkReq, hCaseId);
                    requestLayout.setValidationSupport(validationSupport);
                    break;
                case audit:
                    this.auditReq = (TWmRequestAudit) pRequest;
                    requestLayout = new RequestLayout(pFacade, auditReq, hCaseId);
                    requestLayout.setValidationSupport(validationSupport);
                    break;
                case insurance:
                    this.auditInsurance = (TWmRequestInsurance) pRequest;
                    requestLayout = new RequestLayout(pFacade, auditInsurance, hCaseId);
                    requestLayout.setValidationSupport(validationSupport);
                    break;
                case other:
                    this.auditOther = (TWmRequestOther) pRequest;
                    requestLayout = new RequestLayout(pFacade, auditOther, hCaseId);
                    requestLayout.setValidationSupport(validationSupport);
                    break;
                case review:
                    this.reviewReq = (TWmRequestReview) pRequest;
                    requestLayout = new RequestLayout(pFacade, reviewReq, hCaseId);
                    requestLayout.setValidationSupport(validationSupport);
                    break;
                default:
                    LOG.log(Level.SEVERE, "Unknown RequestType {0}/{1}", new Object[]{pRequest.getRequestType(), pRequest.getRequestTypeEnum()});
                    break;
            }
        }
        messageTypeProperty().bind(requestLayout.messageTypeProperty());
        messageTextProperty().bind(requestLayout.messageTextProperty());
        final Window window = getDialogPane().getContent().getScene().getWindow();
        requestLayout.setOwnerWindow(window);
        RequestLayout.resizeAndAlignWindow(getDialogPane().sceneProperty().get().getWindow());
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
//        switch (request.getRequestEnumType()) {
        switch (pRequestType) {
            case bege:
                resetValidator();
                return requestLayout.getBegeRequestLayout(pRequestType);
            case mdk:
                resetValidator();
                return requestLayout.getMDKLayout(pRequestType);
            case audit:
                resetValidator();
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
        return new Pane();
    }

    private void resetValidator() {
        if (validationSupport.getRegisteredControls().isEmpty()) {
            return;
        }
        //dirty override, should lookout for a better way to reset the validation support
        validationSupport = new ValidationSupport();
        validationSupport.initInitialDecoration();
    }

    public ValidationSupport getValidationSupport() {
        return validationSupport;
    }

    public void addControls(Node setUpLayoutForRequestType) {
        getDialogSkin().addControlGroup(setUpLayoutForRequestType);
    }

    /**
     * This method will call with the OK button to update the request and/or
     * process.
     *
     * @return request object
     */
    @Override
    public TWmRequest onSave() {
        TWmRequest reqObj = requestLayout.onSave();
        return reqObj;
    }

}
