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
 *    2017  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package dto.impl;

import static de.lb.cpx.str.utils.StrUtils.*;
import dto.AbstractDto;
import dto.DtoI;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import line.AbstractLine;
import line.LineI;
import line.impl.SapFiOpenItemsLine;

/**
 *
 * @author Dirk Niemeier
 */
public class SapFiOpenItems extends AbstractDto<SapFiOpenItems> {

    private static final long serialVersionUID = 1L;

    private String mCompanyCode = "";
    private String mCurrencyKey = "";
    private String mCustomerNumber = "";
    private String mDebitCreditKey = "";
    private Integer mFiscalYear = null;
    private String mKindOfReceipt = "";
    private Double mNetValue = null;
    private String mNumberReceipt = "";
    private Date mOrderDateReceipt = null;
    private String mPostingKey = "";
    private Date mReceiptDateReceipt = null;
    private Date mRecordingDateReceipt = null;
    private String mRefNumber = "";
    private String mRefNumberReceipt = "";
    private String mText = "";
    private Double mValue = null;

    public final String mIkz;
    public final String mFallNr;
    public final String mPatNr;
    public final Case mCase;

    public SapFiOpenItems(final Case pCase) {
        if (pCase == null) {
            throw new IllegalArgumentException("Case is null! SAP open item must be assigned to a case!");
        }
        mCase = pCase;
        mIkz = pCase.getIkz();
        mFallNr = pCase.getFallNr();
        mPatNr = pCase.getPatNr();
    }

//    public SapFiBillposition(final String pIkz, final String pFallNr) {
//        this(pIkz, pFallNr, "");
//    }
    public SapFiOpenItems(final String pIkz, final String pFallNr, final String pPatNr) {
        String ikz = pIkz == null ? "" : pIkz.trim();
        String fallNr = pFallNr == null ? "" : pFallNr.trim();
        String patNr = pPatNr == null ? "" : pPatNr.trim();
        //String rechnungsNr = pRechnungsNr == null ? "" : pRechnungsNr.trim();
        if (ikz.isEmpty()) {
            throw new IllegalArgumentException("IKZ is empty!");
        }
        if (patNr.isEmpty()) {
            throw new IllegalArgumentException("Patnr. is empty!");
        }
        if (fallNr.isEmpty()) {
            throw new IllegalArgumentException("Fallnr. is empty!");
        }
        mIkz = ikz;
        mFallNr = fallNr;
        mCase = null;
        mPatNr = patNr;
    }

    @Override
    public LineI toLine() {
        SapFiOpenItemsLine line = (SapFiOpenItemsLine) AbstractDto.toLine(this);
        line.setValueObj(SapFiOpenItemsLine.COMPANY_CODE, getCompanyCode());
        line.setValueObj(SapFiOpenItemsLine.CURRENCY_KEY, getCurrencyKey());
        line.setValueObj(SapFiOpenItemsLine.CUSTOMER_NUMBER, getCustomerNumber());
        line.setValueObj(SapFiOpenItemsLine.DEBIT_CREDIT_KEY, getDebitCreditKey());
        line.setValueObj(SapFiOpenItemsLine.FISCAL_YEAR, getFiscalYear());
        line.setValueObj(SapFiOpenItemsLine.KIND_OF_RECEIPT, getKindOfReceipt());
        line.setValueObj(SapFiOpenItemsLine.NET_VALUE, getNetValue());
        line.setValueObj(SapFiOpenItemsLine.NUMBER_RECEIPT, getNumberReceipt());
        line.setValueObj(SapFiOpenItemsLine.ORDERDATE_RECEIPT, getOrderDateReceipt());
        line.setValueObj(SapFiOpenItemsLine.POSTING_KEY, getPostingKey());
        line.setValueObj(SapFiOpenItemsLine.RECEIPTDATE_RECEIPT, getReceiptDateReceipt());
        line.setValueObj(SapFiOpenItemsLine.RECORDINGDATE_RECEIPT, getRecordingDateReceipt());
        line.setValueObj(SapFiOpenItemsLine.REF_NUMBER, getRefNumber());
        line.setValueObj(SapFiOpenItemsLine.REFNUMBER_RECEIPT, getRefNumberReceipt());
        line.setValueObj(SapFiOpenItemsLine.TEXT, getText());
        line.setValueObj(SapFiOpenItemsLine.VALUE, getValue());
        return line;
    }

    @Override
    public Class<? extends AbstractLine> getLineClass() {
        return SapFiOpenItemsLine.class;
    }

    @Override
    public Set<DtoI> getDtos() {
        Set<DtoI> dtoSet = new LinkedHashSet<>();
        dtoSet.add(this);
        return dtoSet;
    }

    /**
     * @return the CompanyCode
     */
    public String getCompanyCode() {
        return mCompanyCode;
    }

    /**
     * @param pCompanyCode the CompanyCode to set
     */
    public void setCompanyCode(final String pCompanyCode) {
        this.mCompanyCode = toStr(pCompanyCode);
    }

    /**
     * @return the CurrencyKey
     */
    public String getCurrencyKey() {
        return mCurrencyKey;
    }

    /**
     * @param pCurrencyKey the CurrencyKey to set
     */
    public void setCurrencyKey(final String pCurrencyKey) {
        this.mCurrencyKey = toStr(pCurrencyKey);
    }

    /**
     * @return the CustomerNumber
     */
    public String getCustomerNumber() {
        return mCustomerNumber;
    }

    /**
     * @param pCustomerNumber the CustomerNumber to set
     */
    public void setCustomerNumber(final String pCustomerNumber) {
        this.mCustomerNumber = toStr(pCustomerNumber);
    }

    /**
     * @return the DebitCreditKey
     */
    public String getDebitCreditKey() {
        return mDebitCreditKey;
    }

    /**
     * @param pDebitCreditKey the DebitCreditKey to set
     */
    public void setDebitCreditKey(final String pDebitCreditKey) {
        this.mDebitCreditKey = toStr(pDebitCreditKey);
    }

    /**
     * @return the FiscalYear
     */
    public Integer getFiscalYear() {
        return mFiscalYear;
    }

    /**
     * @param pFiscalYear the FiscalYear to set
     */
    public void setFiscalYear(final Integer pFiscalYear) {
        this.mFiscalYear = pFiscalYear;
    }

    /**
     * @return the KindOfReceipt
     */
    public String getKindOfReceipt() {
        return mKindOfReceipt;
    }

    /**
     * @param pKindOfReceipt the KindOfReceipt to set
     */
    public void setKindOfReceipt(final String pKindOfReceipt) {
        this.mKindOfReceipt = toStr(pKindOfReceipt);
    }

    /**
     * @return the NetValue
     */
    public Double getNetValue() {
        return mNetValue;
    }

    /**
     * @param pNetValue the NetValue to set
     */
    public void setNetValue(final Double pNetValue) {
        this.mNetValue = pNetValue;
    }

    /**
     * @return the NumberReceipt
     */
    public String getNumberReceipt() {
        return mNumberReceipt;
    }

    /**
     * @param pNumberReceipt the NumberReceipt to set
     */
    public void setNumberReceipt(final String pNumberReceipt) {
        this.mNumberReceipt = toStr(pNumberReceipt);
    }

    /**
     * @return the OrderDateReceipt
     */
    public Date getOrderDateReceipt() {
        return mOrderDateReceipt == null ? null : new Date(mOrderDateReceipt.getTime());
    }

    /**
     * @param pOrderDateReceipt the OrderDateReceipt to set
     */
    public void setOrderDateReceipt(final Date pOrderDateReceipt) {
        this.mOrderDateReceipt = pOrderDateReceipt == null ? null : new Date(pOrderDateReceipt.getTime());
    }

    /**
     * @return the PostingKey
     */
    public String getPostingKey() {
        return mPostingKey;
    }

    /**
     * @param pPostingKey the PostingKey to set
     */
    public void setPostingKey(final String pPostingKey) {
        this.mPostingKey = toStr(pPostingKey);
    }

    /**
     * @return the ReceiptDateReceipt
     */
    public Date getReceiptDateReceipt() {
        return mReceiptDateReceipt == null ? null : new Date(mReceiptDateReceipt.getTime());
    }

    /**
     * @param pReceiptDateReceipt the ReceiptDateReceipt to set
     */
    public void setReceiptDateReceipt(final Date pReceiptDateReceipt) {
        this.mReceiptDateReceipt = pReceiptDateReceipt == null ? null : new Date(pReceiptDateReceipt.getTime());
    }

    /**
     * @return the RecordingDateReceipt
     */
    public Date getRecordingDateReceipt() {
        return mRecordingDateReceipt == null ? null : new Date(mRecordingDateReceipt.getTime());
    }

    /**
     * @param pRecordingDateReceipt the RecordingDateReceipt to set
     */
    public void setRecordingDateReceipt(final Date pRecordingDateReceipt) {
        this.mRecordingDateReceipt = pRecordingDateReceipt == null ? null : new Date(pRecordingDateReceipt.getTime());
    }

    /**
     * @return the RefNumber
     */
    public String getRefNumber() {
        return mRefNumber;
    }

    /**
     * @param pRefNumber the RefNumber to set
     */
    public void setRefNumber(final String pRefNumber) {
        this.mRefNumber = toStr(pRefNumber);
    }

    /**
     * @return the RefNumberReceipt
     */
    public String getRefNumberReceipt() {
        return mRefNumberReceipt;
    }

    /**
     * @param pRefNumberReceipt the RefNumberReceipt to set
     */
    public void setRefNumberReceipt(final String pRefNumberReceipt) {
        this.mRefNumberReceipt = toStr(pRefNumberReceipt);
    }

    /**
     * @return the Text
     */
    public String getText() {
        return mText;
    }

    /**
     * @param pText the Text to set
     */
    public void setText(final String pText) {
        this.mText = toStr(pText);
    }

    /**
     * @return the Value
     */
    public Double getValue() {
        return mValue;
    }

    /**
     * @param pValue the Value to set
     */
    public void setValue(final Double pValue) {
        this.mValue = pValue;
    }

    @Override
    public Case getCase() {
        return mCase;
    }

    /**
     * @return the ikz REMOVING THIS METHODS RESULTS IN AN STACK OVERFLOW!
     */
    @Override
    public String getIkz() {
        return mIkz;
    }

    /**
     * @return the fallNr REMOVING THIS METHODS RESULTS IN AN STACK OVERFLOW!
     */
    @Override
    public String getFallNr() {
        return mFallNr;
    }

    @Override
    public String getPatNr() {
        return mPatNr;
    }

    @Override
    public void set(SapFiOpenItems pOther) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
