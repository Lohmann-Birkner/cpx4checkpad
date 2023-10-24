/*
 * Copyright (c) 2021 Lohmann & Birkner.
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
 *    2021  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.cm.fx.department.model;

import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.TCaseWard;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import static java.util.Objects.requireNonNull;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.hibernate.Hibernate;

/**
 *
 * @author wilde
 * @param <T> details Object
 */
public class DepartmentWardDetails<T extends AbstractEntity> extends Control{

    private static final Logger LOG = Logger.getLogger(DepartmentWardDetails.class.getName());
    
    public DepartmentWardDetails(T pItem){
        super();
        setItem(pItem);
    }
    @Override
    protected Skin<?> createDefaultSkin() {
        return new DepartmentWardDetailsSkin<>(this);
    }
    private ObjectProperty<T> itemProperty;
    public ObjectProperty<T> itemProperty(){
        if(itemProperty == null){
            itemProperty = new SimpleObjectProperty<>();
        }
        return itemProperty;
    }
    public final void setItem(T pDepartment){
        itemProperty().set(pDepartment);
    }
    public T getItem(){
        return itemProperty().get();
    }
    
    private StringProperty titleProperty;
    public StringProperty titleProperty(){
        if(titleProperty == null){
            titleProperty = new SimpleStringProperty("----");
        }
        return titleProperty;
    }
    public String getTitle(){
        return titleProperty().get();
    }
    public void setTitle(String pTitle){
        titleProperty().set(pTitle);
    }
    
    private StringProperty noItemTextProperty;
    public StringProperty noItemTextProperty(){
        if(noItemTextProperty == null){
            noItemTextProperty = new SimpleStringProperty("----");
        }
        return noItemTextProperty;
    }
    public String getNoItemText(){
        return noItemTextProperty().get();
    }
    public void setNoItemText(String pNoItemText){
       noItemTextProperty().set(pNoItemText);
    }
    
    private final StringConverter<T> DEFAULT_ITEM_CONVERTER = new StringConverter<T>() {
        @Override
        public String toString(T t) {
            return "NO CONVERTER SET FOR :" + t.getClassName();
        }

        @Override
        public T fromString(String string) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    };
    private StringConverter<T> itemConverter = DEFAULT_ITEM_CONVERTER;
    public void setItemConverter(StringConverter<T> pConverter){
        itemConverter = Objects.requireNonNullElse(pConverter, DEFAULT_ITEM_CONVERTER);
    }
    public StringConverter<T> getItemConverter(){
        return itemConverter;
    }
    
    private final StringConverter<TCaseIcd> DEFAULT_ICD_CONVERTER = new StringConverter<TCaseIcd>() {
        @Override
        public String toString(TCaseIcd t) {
            return "NO CONVERTER SET FOR :" + t.getIcdcCode();
        }

        @Override
        public TCaseIcd fromString(String string) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    };
    private StringConverter<TCaseIcd> icdConverter = DEFAULT_ICD_CONVERTER;
    public void setIcdConverter(StringConverter<TCaseIcd> pConverter){
        icdConverter = Objects.requireNonNullElse(pConverter, DEFAULT_ICD_CONVERTER);
    }
    public StringConverter<TCaseIcd> getIcdConverter(){
        return icdConverter;
    }
    
    private final StringConverter<TCaseOps> DEFAULT_OPS_CONVERTER = new StringConverter<TCaseOps>() {
        @Override
        public String toString(TCaseOps t) {
            if(t == null){
                return null;
            }
            return t.getOpscCode();
        }

        @Override
        public TCaseOps fromString(String string) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    };
    private StringConverter<TCaseOps> opsConverter = DEFAULT_OPS_CONVERTER;
    public void setOpsConverter(StringConverter<TCaseOps> pConverter){
        opsConverter = Objects.requireNonNullElse(pConverter, DEFAULT_OPS_CONVERTER);
    }
    public StringConverter<TCaseOps> getOpsConverter(){
        return opsConverter;
    }
    
    private StringProperty hdbCodeProperty;
    public StringProperty hdbCodeProperty(){
        if(hdbCodeProperty == null){
            hdbCodeProperty = new SimpleStringProperty("----");
        }
        return hdbCodeProperty;
    }
    public void setHdbCode(String pCode){
        hdbCodeProperty().set(pCode);
    }
    public String getHdbCode(){
        return hdbCodeProperty().get();
    }
    
    private StringProperty hdbCodeDescriptionProperty;
    public StringProperty hdbCodeDescriptionProperty(){
        if(hdbCodeDescriptionProperty == null){
            hdbCodeDescriptionProperty = new SimpleStringProperty("----");
        }
        return hdbCodeDescriptionProperty;
    }
    public void setHdbCodeDescription(String pCodeDescription){
        hdbCodeDescriptionProperty().set(pCodeDescription);
    }
    public String getHdbCodeDescription(){
        return hdbCodeDescriptionProperty().get();
    }
    
    private final Callback<TCaseIcd,Boolean> DEFAULT_SAVE_ICD_CALLBACK = new Callback<>() {
        @Override
        public Boolean call(TCaseIcd param) {
            if(param == null){
                return false;
            }
            LOG.log(Level.WARNING, "NO SAVE CALLBACK REGISTERED! CAN NOT SAVE ICD: {0}", param.getIcdcCode());
            return false;
        }
    };
    
    private Callback<TCaseIcd,Boolean> saveIcdCallback = DEFAULT_SAVE_ICD_CALLBACK;
    public Callback<TCaseIcd,Boolean> getSaveIcdCallback(){
        return saveIcdCallback;
    }
    public void setSaveIcdCallback(Callback<TCaseIcd,Boolean> pCallback){
        saveIcdCallback = Objects.requireNonNullElse(pCallback, DEFAULT_SAVE_ICD_CALLBACK);
    }
    
    private final Callback<T,List<TCaseIcd>> DEFAULT_FIND_ICDS_CALLBACK = new Callback<>() {
        @Override
        public List<TCaseIcd> call(T param) {
            LOG.log(Level.WARNING, "NO SAVE CALLBACK REGISTERED! CAN NOT FETCH ANY ICDS!");
            return new ArrayList<>();
        }
    };
    
    private Callback<T,List<TCaseIcd>> findIcdsCallback = DEFAULT_FIND_ICDS_CALLBACK;
    public Callback<T,List<TCaseIcd>> getFindIcdsCallback(){
        return findIcdsCallback;
    }
    public void setFindIcdsCallback(Callback<T,List<TCaseIcd>> pCallback){
        findIcdsCallback = Objects.requireNonNullElse(pCallback, DEFAULT_FIND_ICDS_CALLBACK);
    }
    
    private final Callback<T,List<TCaseOps>> DEFAULT_FIND_OPSES_CALLBACK = new Callback<>() {
        @Override
        public List<TCaseOps> call(T param) {
            LOG.log(Level.WARNING, "NO SAVE CALLBACK REGISTERED! CAN NOT FETCH ANY OPSES!");
            return new ArrayList<>();
        }
    };
    
    private Callback<T,List<TCaseOps>> findOpsesCallback = DEFAULT_FIND_OPSES_CALLBACK;
    public Callback<T,List<TCaseOps>> getFindOpsesCallback(){
        return findOpsesCallback;
    }
    public void setFindOpsesCallback(Callback<T,List<TCaseOps>> pCallback){
        findOpsesCallback = Objects.requireNonNullElse(pCallback, DEFAULT_FIND_OPSES_CALLBACK);
    }
    
    public Set<TCaseIcd> getIcdsFromItem(T pItem){
        if(pItem instanceof TCaseDepartment){
            return requireInitializedElse(((TCaseDepartment)pItem).getCaseIcds(),new HashSet<>());
        }
        if(pItem instanceof TCaseWard){
            return requireInitializedElse(((TCaseWard)pItem).getCaseIcds(),new HashSet<>());
        }
        return new HashSet<>();
    }
    public Set<TCaseOps> getOpsesFromItem(T pItem){
        if(pItem instanceof TCaseDepartment){
            return requireInitializedElse(((TCaseDepartment)pItem).getCaseOpses(),new HashSet<>());
        }
        if(pItem instanceof TCaseWard){
            return requireInitializedElse(((TCaseWard) pItem).getCaseOpses(), new HashSet<>());
        }
        return new HashSet<>();
    }
    
    private <Z> Z requireInitializedElse(Z obj, Z defaultObj) {
        if(Hibernate.isInitialized(obj)){
            return obj;
        }
        return defaultObj;
//        return (obj != null) ? obj : requireNonNull(defaultObj, "defaultObj");
    }
}
