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
package de.lb.cpx.client.app.cm.fx.details;

import de.lb.cpx.model.enums.VersionRiskTypeEn;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmRiskDetails;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.util.Callback;

/**
 *
 * @author wilde
 */
public class CmRiskDetailDocumentationView extends Control{

    @Override
    protected Skin<?> createDefaultSkin() {
        try {
            return new CmRiskDetailDocumentationViewSkin(this);
        } catch (IOException ex) {
            Logger.getLogger(CmRiskDetailDocumentationView.class.getName()).log(Level.SEVERE, null, ex);
        }
        return super.createDefaultSkin();
    }
    
    public CmRiskDetailDocumentationView(boolean isReadonly){
        super();
        setReadonlyContent(isReadonly);
    }
    private ObjectProperty<TWmRiskDetails> riskDetailProperty;
    public ObjectProperty<TWmRiskDetails> riskDetailsProperty(){
        if(riskDetailProperty == null){
            riskDetailProperty = new SimpleObjectProperty<>();
        }
        return riskDetailProperty;
    }
    public void setRiskDetail(TWmRiskDetails pDetail){
        riskDetailsProperty().set(pDetail);
    }
    public TWmRiskDetails getRiskDetails(){
        return riskDetailsProperty().get();
    }
    
    private static final Callback<TWmRiskDetails,Boolean> DEFAULT_UPDATE_CALLBACK = new Callback<TWmRiskDetails, Boolean>() {
        @Override
        public Boolean call(TWmRiskDetails param) {
            return false;
        }
    };
    private Callback<TWmRiskDetails,Boolean> updateRiskDetailCallback = DEFAULT_UPDATE_CALLBACK;
    public Callback<TWmRiskDetails,Boolean> getUpdateRiskDetail(){
        return updateRiskDetailCallback;
    } 
    public void setUpdateRiskDetailCalback(Callback<TWmRiskDetails,Boolean> pCallback){
        updateRiskDetailCallback = Objects.requireNonNullElse(pCallback, DEFAULT_UPDATE_CALLBACK);
    }
    public double calculateWaste4RiskDetails(TWmRiskDetails pDetails) {
        pDetails.setRiskPercent(pDetails.getRiskAuditPercent() * pDetails.getRiskWastePercent()/100);
        pDetails.setRiskValue(BigDecimal.valueOf(Lang.round(pDetails.getRiskNotCalculatedFee().doubleValue() /100 * pDetails.getRiskPercent(), 2)));
//        this.getOnUpdateRisk().call( pDetails);
        return  pDetails.getRiskValue().doubleValue();
    }        

    private ObjectProperty<VersionRiskTypeEn> versionTypeProperty;
    
    public ObjectProperty<VersionRiskTypeEn> versionTypeProperty(){
        if(versionTypeProperty == null){
            versionTypeProperty = new SimpleObjectProperty<VersionRiskTypeEn>();
        }
        return versionTypeProperty;
    }
    
    public void setVersionType(VersionRiskTypeEn versionType) {
        versionTypeProperty().set(versionType);
    }
    
    public VersionRiskTypeEn getVersionType(){
        return versionTypeProperty().get();
    }

    private BooleanProperty readonlyContent;
    
    public BooleanProperty readonlyContent(){
        if(readonlyContent == null){
            readonlyContent = new SimpleBooleanProperty();
            readonlyContent.set(false);
        }
        return readonlyContent;
    }
    
    public void setReadonlyContent(boolean b) {
        readonlyContent().set(b);
    }
    public boolean getReadonlyContent(){
       return readonlyContent().get();
    }


}
