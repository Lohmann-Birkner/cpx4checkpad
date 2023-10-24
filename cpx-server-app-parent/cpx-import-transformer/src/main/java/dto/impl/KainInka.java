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
 *    2017  niemeier - initial API and implementation and/or initial documentation
 */
package dto.impl;

import static de.lb.cpx.str.utils.StrUtils.*;
import dto.AbstractDto;
import dto.DtoI;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import line.AbstractLine;
import line.LineI;
import line.impl.KainInkaLine;

/**
 *
 * @author niemeier
 */
public abstract class KainInka extends AbstractDto<KainInka> {

    private static final long serialVersionUID = 1L;

    private String mContractReference = "";
    private String mCostUnitSap = "";
    private String mCurrentTransactionNr = "";
    private String mHospitalIdentifier = "";
    private String mHospitalNumberPatient = "";
    private String mInsuranceCaseNumber = "";
    private String mInsuranceIdentifier = "";
    private String mInsuranceRefNumber = "";
    private String mProcessingRef = "";
    private String mCpxExternalMsgNr = "";

    public final String mIkz;
    public final String mFallNr;
    public final String mPatNr;
    public final Case mCase;
    private final Set<KainInkaPvv> kainInkaPvvList = new LinkedHashSet<>();

    public KainInka(final Case pCase, final Patient pPatient) {
        if (pCase == null) {
            throw new IllegalArgumentException("Case is null!");
        }
        if (pPatient == null) {
            throw new IllegalArgumentException("Patient is null!");
        }
        mCase = pCase;
        mIkz = pCase.getIkz();
        mFallNr = pCase.getFallNr();
        mPatNr = pPatient.getPatNr();
    }

    public KainInka(final String pIkz, final String pFallNr, final String pPatNr) {
        String ikz = pIkz == null ? "" : pIkz.trim();
        String fallNr = pFallNr == null ? "" : pFallNr.trim();
        String patNr = pPatNr == null ? "" : pPatNr.trim();
        if (ikz.isEmpty()) {
            throw new IllegalArgumentException("IKZ is empty!");
        }
        if (fallNr.isEmpty()) {
            throw new IllegalArgumentException("Fallnr. is empty!");
        }
//        if (patNr.isEmpty()) {
//            throw new IllegalArgumentException("Patnr. is empty!");
//        }
        mCase = null;
        mIkz = ikz;
        mFallNr = fallNr;
        mPatNr = patNr;
    }

    @Override
    public LineI toLine() {
        KainInkaLine line = (KainInkaLine) AbstractDto.toLine(this);
        //KAIN&INKA
        line.setValueObj(KainInkaLine.CONTRACT_REFERENCE, getContractReference());
        line.setValueObj(KainInkaLine.COST_UNIT_SAP, getCostUnitSap());
        line.setValueObj(KainInkaLine.CURRENT_TRANSACTION_NR, getCurrentTransactionNr());
        line.setValueObj(KainInkaLine.HOSPITAL_IDENTIFIER, getHospitalIdentifier());
        line.setValueObj(KainInkaLine.HOSPITAL_NUMBER_PATIENT, getHospitalNumberPatient());
        line.setValueObj(KainInkaLine.INSURANCE_CASE_NUMBER, getInsuranceCaseNumber());
        line.setValueObj(KainInkaLine.INSURANCE_IDENTIFIER, getInsuranceIdentifier());
        line.setValueObj(KainInkaLine.INSURANCE_REF_NUMBER, getInsuranceRefNumber());
        line.setValueObj(KainInkaLine.MESSAGE_TYPE, getMessageType());
        line.setValueObj(KainInkaLine.PROCESSING_REF, getProcessingRef());
        line.setValueObj(KainInkaLine.CPX_EXTERNAL_MSG_NR, getCpxExternalMsgNr());
        //line.setValueObj(DiagnoseLine.REF_ICD_NR, (mRefIcd == null?"":mRefIcd.getNr()));
        //line.setValueObj(DiagnoseLine.REF_ICD_TYPE, getRefIcdType());
        return line;
    }

    public abstract String getMessageType();

    /**
     * @return the mContractReference
     */
    public String getContractReference() {
        return mContractReference;
    }

    /**
     * @param pContractReference the mContractReference to set
     */
    public void setContractReference(final String pContractReference) {
        this.mContractReference = toStr(pContractReference);
    }

    /**
     * @return the mCostUnitSap
     */
    public String getCostUnitSap() {
        return mCostUnitSap;
    }

    /**
     * @param pCostUnitSap the mCostUnitSap to set
     */
    public void setCostUnitSap(final String pCostUnitSap) {
        this.mCostUnitSap = toStr(pCostUnitSap);
    }

    /**
     * @return the mCurrentTransactionNr
     */
    public String getCurrentTransactionNr() {
        return mCurrentTransactionNr;
    }

    /**
     * @param pCurrentTransactionNr the mCurrentTransactionNr to set
     */
    public void setCurrentTransactionNr(final String pCurrentTransactionNr) {
        this.mCurrentTransactionNr = toStr(pCurrentTransactionNr);
    }

    /**
     * @return the mHospitalIdentifier
     */
    public String getHospitalIdentifier() {
        return mHospitalIdentifier;
    }

    /**
     * @param pHospitalIdentifier the mHospitalIdentifier to set
     */
    public void setHospitalIdentifier(final String pHospitalIdentifier) {
        this.mHospitalIdentifier = toStr(pHospitalIdentifier);
    }

    /**
     * @return the mHospitalNumberPatient
     */
    public String getHospitalNumberPatient() {
        return mHospitalNumberPatient;
    }

    /**
     * @param pHospitalNumberPatient the mHospitalNumberPatient to set
     */
    public void setHospitalNumberPatient(final String pHospitalNumberPatient) {
        this.mHospitalNumberPatient = toStr(pHospitalNumberPatient);
    }

    /**
     * @return the mInsuranceCaseNumber
     */
    public String getInsuranceCaseNumber() {
        return mInsuranceCaseNumber;
    }

    /**
     * @param pInsuranceCaseNumber the mInsuranceCaseNumber to set
     */
    public void setInsuranceCaseNumber(final String pInsuranceCaseNumber) {
        this.mInsuranceCaseNumber = toStr(pInsuranceCaseNumber);
    }

    /**
     * @return the mInsuranceIdentifier
     */
    public String getInsuranceIdentifier() {
        return mInsuranceIdentifier;
    }

    /**
     * @param pInsuranceIdentifier the mInsuranceIdentifier to set
     */
    public void setInsuranceIdentifier(final String pInsuranceIdentifier) {
        this.mInsuranceIdentifier = toStr(pInsuranceIdentifier);
    }

    /**
     * @return the mInsuranceRefNumber
     */
    public String getInsuranceRefNumber() {
        return mInsuranceRefNumber;
    }

    /**
     * @param pInsuranceRefNumber the mInsuranceRefNumber to set
     */
    public void setInsuranceRefNumber(final String pInsuranceRefNumber) {
        this.mInsuranceRefNumber = toStr(pInsuranceRefNumber);
    }

    /**
     * @return the mProcessingRef
     */
    public String getProcessingRef() {
        return mProcessingRef;
    }

    /**
     * @return the mCpxExternalMsgNr
     */
    public String getCpxExternalMsgNr() {
        return mCpxExternalMsgNr;
    }

    /**
     * @param mCpxExternalMsgNr the mCpxExternalMsgNr to set
     */
    public void setCpxExternalMsgNr(String mCpxExternalMsgNr) {
        this.mCpxExternalMsgNr = toStr(mCpxExternalMsgNr);
    }

    /**
     * @param pProcessingRef the mProcessingRef to set
     */
    public void setProcessingRef(final String pProcessingRef) {
        this.mProcessingRef = toStr(pProcessingRef);
    }

    @Override
    public Class<? extends AbstractLine> getLineClass() {
        return KainInkaLine.class;
    }

    @Override
    public Set<DtoI> getDtos() {
        Set<DtoI> dtoSet = new LinkedHashSet<>();
        dtoSet.add(this);
        Iterator<KainInkaPvv> it = kainInkaPvvList.iterator();
        while (it.hasNext()) {
            KainInkaPvv kainInkaPvv = it.next();
            if (kainInkaPvv == null) {
                continue;
            }
            dtoSet.addAll(kainInkaPvv.getDtos());
        }
        return dtoSet;
    }

    protected boolean addKainInkaPvv(final KainInkaPvv pKainInkaPvv) {
        if (pKainInkaPvv == null) {
            throw new IllegalArgumentException("KainInkaPvv is null!");
        }
        return kainInkaPvvList.add(pKainInkaPvv);
    }

    @Override
    public String getIkz() {
        return mIkz;
    }

    @Override
    public String getFallNr() {
        return mFallNr;
    }

    @Override
    public String getPatNr() {
        return mPatNr;
    }

    @Override
    public Case getCase() {
        return mCase;
    }

    @Override
    public void set(KainInka pOther) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
