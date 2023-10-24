/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.dialog.editor;

import de.lb.cpx.client.core.model.enums.CpxErrorTypeEn;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.util.Callback;

/**
 *
 * @author wilde
 */
public class CatalogValidationResult {

    private static final Logger LOG = Logger.getLogger(CatalogValidationResult.class.getName());
    
    private final List<CatalogValidationMessage> validationItems;

    public CatalogValidationResult() {
        validationItems = new ArrayList<>();
    }
    public void replaceIfExists(CpxErrorTypeEn pType, String pMessage, Predicate<Void> pPredicate){
        replaceIfExists(new CatalogValidationMessage(pType, pMessage, pPredicate));
    }
    public void replaceIfExists(CatalogValidationMessage pItem){
        removeValidationMessage(pItem.getMessage());
        addValidationMessage(pItem);
    }
    public void add(CpxErrorTypeEn pType, String pMessage, Predicate<Void> pPredicate){
        addValidationMessage(new CatalogValidationMessage(pType, pMessage, pPredicate));
    }
    public void addValidationMessage(CatalogValidationMessage pItem){
        if(!validationItems.stream().anyMatch(ti -> (ti.getMessage() == null ? pItem.getMessage() == null : ti.getMessage().equals(pItem.getMessage())))){
            validationItems.add(pItem);
        }else{
            LOG.log(Level.FINE, "ValidationMessage with the same Text: {0} already exists - ignored!", pItem.getMessage());
        }
    }
    public void removeValidationMessage(String pMessage){
        Optional<CatalogValidationMessage> message = validationItems.stream().
                filter(p -> p.getMessage().equals(pMessage)).
                findFirst();
        removeValidationMessage(message.isPresent()?message.get():null);
    }
    public void removeValidationMessage(CatalogValidationMessage pMessage){
        if(pMessage == null){
            LOG.fine("Can not remove null Item from CatalogValidation");
            return;
        }
        validationItems.remove(pMessage);
    }
    public boolean hasMessages() {
        return !validationItems.isEmpty();
    }

    public String getValidationMessages() {
        if (!hasMessages()) {
            return null;
        }
        //get String separated
        return validationItems.stream().filter((CatalogValidationMessage t) -> {
            //filter if condition is true
            //as in addMessageIf
            return t.getPredicate().test(null);
        }).sorted(CatalogValidationMessage.COMPARATOR.reversed())
        .map((t) -> {
            //get Message stored for validationMessage
            return t.getMessage();
        }).filter((t) -> {
            //filter all where message is empty or null
            return !(t == null || t.isEmpty());
        }).collect(Collectors.joining("\n"));
    }

    public CpxErrorTypeEn getHighestErrorType() {
        Optional<CatalogValidationMessage> msg = validationItems.stream().filter((t) -> {
            //filter if condition is true
            //as in addMessageIf
            Predicate<Void> condition = t.getPredicate();
            return condition.test(null);
        }).max(CatalogValidationMessage.COMPARATOR);
        if (msg.isPresent()) {
            return msg.get().getType();
        }
        return null;
    }
    
    public void reset(){
        if(validationItems != null){
            validationItems.clear();
        }
    }

    public void invalidate(){
        getInvalidationCallback().call(null);
        
    }
    private final static Callback<Void,Void> DEFAULT_INVALIDATION_CALLBACK = new Callback<Void, Void>() {
        @Override
        public Void call(Void p) {
            LOG.fine("Result gets invalidated, but no callback is stored!");
            return null;
        }
    };
    private Callback<Void,Void> invalidationCallback = DEFAULT_INVALIDATION_CALLBACK;
    public Callback<Void,Void> getInvalidationCallback(){
        return invalidationCallback;
    }
    public void setInvalidationCallback(Callback<Void,Void> pCallback){
        invalidationCallback = Objects.requireNonNullElse(pCallback, DEFAULT_INVALIDATION_CALLBACK);
    }
}
