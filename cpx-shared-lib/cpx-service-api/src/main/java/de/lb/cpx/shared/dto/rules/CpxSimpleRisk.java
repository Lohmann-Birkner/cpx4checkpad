/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.dto.rules;

import de.lb.cpx.server.commonDB.model.enums.RiskAreaEn;
import de.lb.cpx.shared.lang.Lang;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gerschmann
 */
public class CpxSimpleRisk  implements Serializable {

    private static final Logger LOG = Logger.getLogger(CpxSimpleRisk.class.getName());
    
    
    private String mRiskAreas;
    private String mRiskComment;
    private String mRiskDefaultWasteValue;
    private String mRiskWastePercentValue;
    private String mRiskAuditPercentValue;

    private double mForBillingKoeff;
    private double mAuditKoeff;
    private boolean mUseDefault = false;
    private double mWasteKoeff;
    
    public CpxSimpleRisk(String pRiskAreas, 
            String pRiskComment, 
            String pRiskDefaultWasteValue, 
            String pRiskWastePercentValue, 
            String pRiskAuditPercentValue,
            boolean useDefault){
        mRiskAreas = pRiskAreas;
        mRiskComment = pRiskComment;
        mRiskDefaultWasteValue = pRiskDefaultWasteValue;
        mRiskWastePercentValue = pRiskWastePercentValue;
        mRiskAuditPercentValue = pRiskAuditPercentValue;
        mUseDefault = useDefault;
        mForBillingKoeff = Lang.round(0.0001 * getRiskWastePercentValueAsNumber() * getRiskAuditPercentValueAsNumber(), 4);
        mAuditKoeff = Lang.round(0.01 * getRiskAuditPercentValueAsNumber(), 2);
        mWasteKoeff = Lang.round(0.01 * getRiskWastePercentValueAsNumber(), 2);
    }

    public String getRiskAreas() {
        return mRiskAreas;
    }
    
    public String getRiskAreaTranslation(){
        return RiskAreaEn.getTranslation2Value(mRiskAreas); 
    }

    public void setRiskAreas(String mRiskAreas) {
        this.mRiskAreas = mRiskAreas;
    }

    public String getRiskComment() {
        return mRiskComment;
    }

    public void getRiskComment(String pRiskComment) {
        this.mRiskComment = pRiskComment;
    }

    public String getRiskDefaultWasteValue() {
        return mRiskDefaultWasteValue;
    }

    public void getRiskDefaultWasteValue(String pRiskDefaultWasteValue) {
        this.mRiskDefaultWasteValue = pRiskDefaultWasteValue;
    }

    public String getRiskWastePercentValue() {
        return mRiskWastePercentValue;
    }

    public void setRiskWastePercentValue(String pRiskWastePercentValue) {
        this.mRiskWastePercentValue = pRiskWastePercentValue;
    }
    
    public RiskAreaEn getRiskAreasAsEn(){

        
        if(mRiskAreas!= null && !mRiskAreas.isEmpty()){
             return RiskAreaEn.valueOf(mRiskAreas);

        }
        return null;
    }
    
    public int getRiskWastePercentValueAsNumber(){
        try{
            if(mRiskWastePercentValue == null || mRiskWastePercentValue.isEmpty()){
                return 0;
            }
            return Integer.parseInt(mRiskWastePercentValue);
        }catch(NumberFormatException ex){
            LOG.log(Level.SEVERE, "could not convert percent value " + mRiskWastePercentValue, ex);
            return 0;
        }
    }
    
    
    public int getRiskAuditPercentValueAsNumber(){
        try{
            if(mRiskAuditPercentValue == null || mRiskAuditPercentValue.isEmpty()){
                return 0;
            }
            return Integer.parseInt(mRiskAuditPercentValue);
        }catch(NumberFormatException ex){
            LOG.log(Level.SEVERE, "could not convert percent value " + this.mRiskAuditPercentValue, ex);
            return 0;
        }
    }

    public double getRiskDefaultWasteValueAsNumber(){
        try{
            if(mRiskDefaultWasteValue == null || mRiskDefaultWasteValue.isEmpty()){
                return 0;
            }
            return Double.parseDouble(mRiskDefaultWasteValue);
        }catch(NumberFormatException ex){
            LOG.log(Level.SEVERE, "could not convert percent value " + mRiskDefaultWasteValue, ex);
            return 0;
        }
        
    }

    public String getRiskAuditPercentValue() {
        return mRiskAuditPercentValue;
    }

    public void setRiskAuditPercentValue(String pRiskAuditPercentValue) {
        mRiskAuditPercentValue = pRiskAuditPercentValue;
    }

    public double getForBillingKoeff() {
        return mForBillingKoeff;
    }

    public double getAuditKoeff() {
        return mAuditKoeff;
    }

    public String getComputeString(Double simRevenue, boolean is4billing) {
        return ((simRevenue == null || simRevenue.doubleValue() == 0)?(mRiskDefaultWasteValue != null?mRiskDefaultWasteValue:"0"):
                Lang.toDecimal(Math.abs(simRevenue), 2)) +
                Lang.getCurrencySymbol() +
                
                " * " + mRiskWastePercentValue + "%" +
                (is4billing?(
                " * " + mRiskAuditPercentValue + "%"):"");
    } 

    public String getForBillingComputeValue(Double simRevenue, boolean is4billing) {
        double checkValue = getRiskDefaultWasteValueAsNumber();
        if(simRevenue != null && simRevenue.doubleValue() != 0) {
            checkValue = simRevenue.doubleValue();
        }
        return Lang.toDecimal(Math.abs(Lang.round(checkValue* (is4billing?mForBillingKoeff:mWasteKoeff), 2) ), 2)   + Lang.getCurrencySymbol();
    }
    
    public String getComputedForBillingValue(){
        return  Lang.toDecimal( mForBillingKoeff * 100, 0) + "%";
    }
    
    
    public String getComputedWasteValue(){
        return  Lang.toDecimal( this.mWasteKoeff * 100, 0) + "%";
    }
    
    
    public boolean doUseDefault(){
        return mUseDefault;
    }   

    public double getWasteKoeff() {
        return mWasteKoeff;
    }

}
