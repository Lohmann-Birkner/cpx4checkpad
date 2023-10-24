/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.dialog.editor;

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.enums.CpxErrorTypeEn;
import de.lb.cpx.model.TCase;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmReminder;
import de.lb.cpx.wm.model.TWmRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import org.controlsfx.validation.ValidationSupport;

/**
 * Basic class for all request editors
 *
 * @author wilde
 * @param <T> request type to edit
 */
public class BasicRequestEditor<T extends TWmRequest> extends Control {
    
    public static final String UPDATE_REQUEST_DATA = "update_request_data";
    private final ProcessServiceFacade facade;
    private final TCase cs;
//    private final TCaseDetails externDetails;
    private CatalogValidationResult catalogValidationResult;

    public BasicRequestEditor(T pRequest, final ProcessServiceFacade pFacade, final TCase pCase) {
        super();
        setRequest(pRequest);
        facade = pFacade;
        cs = pCase;
//        externDetails = pExternDetails;
    }

    private ObjectProperty<T> requestProperty;

    public ObjectProperty<T> requestProperty() {
        if (requestProperty == null) {
            requestProperty = new SimpleObjectProperty<>();
        }
        return requestProperty;
    }

    public T getRequest() {
        return requestProperty().get();
    }

    public final void setRequest(T pRequest) {
        requestProperty().set(pRequest);
    }
    private ObjectProperty<Date> processDateProperty;

    public ObjectProperty<Date> processDateProperty() {
        if (processDateProperty == null) {
            processDateProperty = new SimpleObjectProperty<>(new Date());
        }
        return processDateProperty;
    }

    public Date getProcessDate() {
        return processDateProperty().get();
    }

    public final void setProcessDate(Date pProcessDate) {
        processDateProperty().set(pProcessDate);
    }
    private ObjectProperty<ValidationSupport> validationSupportProperty;

    public ObjectProperty<ValidationSupport> validationSupportProperty() {
        if (validationSupportProperty == null) {
            validationSupportProperty = new SimpleObjectProperty<>();
        }
        return validationSupportProperty;
    }

    public ValidationSupport getValidationSupport() {
        return validationSupportProperty().get();
    }

    public void setValidationSupport(ValidationSupport pSupport) {
        validationSupportProperty().set(pSupport);
    }

    protected void updateRequest() {
        getProperties().put(UPDATE_REQUEST_DATA, null);
    }

    public T getUpdatedRequest() {
        updateRequest();
        return getRequest();
    }

    public ProcessServiceFacade getProcessServiceFacade() {
        return facade;
    }

    public TCase getCase() {
        return cs;
    }
//
//    public TCaseDetails getExternDetails() {
//        return externDetails;
//    }

    public Date getCaseAdmissionDate() {
        return cs != null ? facade.getCurrentExtern(cs.getId()).getCsdAdmissionDate() : null;
    }
    
    /**
     * Optional Methode can be overriden
     * @return get Catalog Result for this request editor
     */
    public CatalogValidationResult getCatalogValidationResult(){
        if(catalogValidationResult == null){
            catalogValidationResult = new CatalogValidationResult();
        }
        return catalogValidationResult;
    }
    
    public void initCatalogValidation(){
        getCatalogValidationResult().replaceIfExists(CpxErrorTypeEn.WARNING, Lang.getMsgNoRightOtherBlank(Lang.getEventNameRequest(),"bearbeiten"), new Predicate<Void>() {
            @Override
            public boolean test(Void t) {
                return !canUserModifyOtherRequest();
            }
        });
        getCatalogValidationResult().replaceIfExists(CpxErrorTypeEn.WARNING, Lang.getMsgNoRightBlank(Lang.getEventNameRequest(),"bearbeiten"), new Predicate<Void>() {
            @Override
            public boolean test(Void t) {
                return !canUserModifyRequest();
            }
        });
    }
    
    public final boolean canUserModifyRequest(){
        return true; //TODO: Implement this user-permission
    }
    public final boolean canUserModifyOtherRequest(){
        if(getRequest().getCreationUser() == null){
            return true; // not saved yet .. you can maybe edit
        }
        if(!(getRequest().getCreationUser().equals(Session.instance().getCpxUserId())) && !Session.instance().getRoleProperties().isEditRequestOfOtherUserAllowed()){
            return false;
        }
        return true;
    }
    public final boolean isEditable(){
        if(!canUserModifyRequest()){
            return false;
        }
        if(!canUserModifyOtherRequest()){
            return false;
        }
        return true;
    }
    
    private List<TWmReminder> reminders;
    
     public void setReminders(List<TWmReminder> pMdkReminders) {
        List<TWmReminder> copyMdkReminders = new ArrayList<>(pMdkReminders);
        this.reminders = copyMdkReminders;
    }

    public List<TWmReminder> getReminders() {
        if (reminders != null) {
            return new ArrayList<>(reminders);
        } else {
            return new ArrayList<>();
        }
    }
   
}
