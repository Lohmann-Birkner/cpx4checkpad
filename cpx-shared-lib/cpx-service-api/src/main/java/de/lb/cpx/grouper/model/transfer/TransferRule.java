/* 
 * Copyright (c) 2015 Lohmann & Birkner.
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
 *    2015  Wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.grouper.model.transfer;

import de.checkpoint.drg.RulesRefOut;
import de.checkpoint.ruleGrouper.CRGRisk;
import de.lb.cpx.model.enums.RuleTypeEn;
import de.lb.cpx.shared.dto.rules.CpxSimpleRisk;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Klasse zur Darstellung der Grouperrelevanten Informationen zu einer
 * angeschlagenen Regel
 *
 * @author Wilde
 */
public class TransferRule implements Serializable {

    private static final long serialVersionUID = 1L;

    private long mId;
    private double mDCw;
    private double mDCareCw;
    private String mDrg;
    private double mDFee;
    private long[] mRoles = null;
    private RuleTypeEn mRuleType;
    private String mRuleErrType;
    private ArrayList<Long> mUsedRoles = null;
    private RulesRefOut mRuleRefs;
    private CpxSimpleRisk mRisks;

    public String getRuleErrType() {
        return mRuleErrType;
    }

    public void setRuleErrType(String ruleErrType) {
        this.mRuleErrType = ruleErrType;
    }

    /**
     * Rule Id rid attribute in the xml
     *
     * @param rid id
     */
    public void setId(long rid) {
        mId = rid;
    }

    public long getId() {
        return mId;
    }

    public double getDcw() {
        return mDCw;
    }

    public void setDcw(double dCw) {
        this.mDCw = dCw;
    }

    public void setDfee(double dfee) {
        this.mDFee = dfee;
    }

    public String getDrg() {
        return mDrg;
    }

    public void setDrg(String drg) {
        this.mDrg = drg;
    }

    public double getDfee() {
        return mDFee;
    }

    public long[] getRoles() {
        return mRoles == null ? null : Arrays.copyOf(mRoles, mRoles.length);
    }

    public void setRoles(long[] roles) {
        this.mRoles = roles == null ? null : Arrays.copyOf(roles, roles.length);
    }

    public RuleTypeEn getRuleType() {
        return mRuleType;
    }

    public void setRuleType(RuleTypeEn type) {
        this.mRuleType = type;
    }

    public void setUsedRoles(List<Long> usedRoles) {
        mUsedRoles = usedRoles == null ? null : new ArrayList<>(usedRoles);
    }

    public List<Long> getUsedRoles() {
        return mUsedRoles == null ? null : new ArrayList<>(mUsedRoles);
    }

    public void setRuleReferences(RulesRefOut ref) {
        mRuleRefs = ref;
    }

    public RulesRefOut getRuleReferences() {
        return mRuleRefs;
    }

    public double getDCareCw() {
        return mDCareCw;
    }

    public void setDCareCw(double dCareCw) {
        this.mDCareCw = dCareCw;
    }
    
    public void setRuleRisks(List<CRGRisk> cpRisks, boolean useWasteValue){
       if(cpRisks != null && !cpRisks.isEmpty()){
            CRGRisk cpRisk = cpRisks.get(0);
                mRisks = new CpxSimpleRisk(cpRisk.getRiskName(), 
                        cpRisk.getRiskComment(),
                        cpRisk.getRiskDefaultWasteValue(),
                        cpRisk.getRiskWastePercentValue(), 
                        cpRisk.getRiskAuditPercentValue(),
                useWasteValue);
                   
            }
        
        
        
    }

    public CpxSimpleRisk getRuleRisks(){
        return mRisks;
    }
    /**
     * @return list of all references seperated by comma
     */
    @SuppressWarnings("unchecked")
    public String getReferences() {
//        String seperator = ",";
        String references = "";
        //AWi-20170802-CPX-528
        //added to avoid nullpointer when no ruleRefs are set
        if (mRuleRefs == null) {
            return references;
        }
        return mRuleRefs.toString();
    }
}
