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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.sap.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author niemeier
 */
public class SapExportResult {

    private int state = 0;
    private StringBuilder mText = new StringBuilder();
    private StringBuilder mWarnings = new StringBuilder();

    private Map<Long, Long> newDiags = null;
    private Map<Long, Long> newProcs = null;

    private final List<String> closedCaseNumbers = new ArrayList<>();
    private final List<String> notClosedCaseNumbers = new ArrayList<>();

    private final List<SapFiFactura> mFactura = new ArrayList<>();
    private final List<SapFiOpenItem> mOpenItems = new ArrayList<>();

    private String mCurrentState = "";

    /**
     *
     * @param text text
     */
    public SapExportResult(String text) {
        state = 1;
        mText.append(text);
    }

    /**
     *
     */
    public SapExportResult() {
    }

    /**
     * @return the state
     */
    public int getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(int state) {
        this.state = state;
    }

    /**
     * @return the mText
     */
    public StringBuilder getText() {
        return mText;
    }

    /**
     * @param mText the mText to set
     */
    public void setText(StringBuilder mText) {
        this.mText = mText;
    }

    /**
     * @return the mWarnings
     */
    public StringBuilder getWarnings() {
        return mWarnings;
    }

    /**
     * @param mWarnings the mWarnings to set
     */
    public void setWarnings(StringBuilder mWarnings) {
        this.mWarnings = mWarnings;
    }

    /**
     * @return the newDiags
     */
    public Map<Long, Long> getNewDiags() {
        return newDiags;
    }

    /**
     * @param newDiags the newDiags to set
     */
    public void setNewDiags(Map<Long, Long> newDiags) {
        this.newDiags = newDiags;
    }

    /**
     * @return the newProcs
     */
    public Map<Long, Long> getNewProcs() {
        return newProcs;
    }

    /**
     * @param newProcs the newProcs to set
     */
    public void setNewProcs(Map<Long, Long> newProcs) {
        this.newProcs = newProcs;
    }

//    /**
//     * @param closedCaseNumbers the closedCaseNumbers to set
//     */
//    public void setClosedCaseNumbers(List<String> closedCaseNumbers) {
//        this.closedCaseNumbers = closedCaseNumbers;
//    }
//    /**
//     * @return the notClosedCaseNumbers
//     */
//    public List<String> getNotClosedCaseNumbers() {
//        return notClosedCaseNumbers;
//    }
//    /**
//     * @param notClosedCaseNumbers the notClosedCaseNumbers to set
//     */
//    public void setNotClosedCaseNumbers(List<String> notClosedCaseNumbers) {
//        this.notClosedCaseNumbers = notClosedCaseNumbers;
//    }
//    /**
//     * @return the m_factura
//     */
//    public List<SapFiFactura> getM_factura() {
//        return m_factura;
//    }
//
//    /**
//     * @param m_factura the m_factura to set
//     */
//    public void setM_factura(List<SapFiFactura> m_factura) {
//        this.m_factura = m_factura;
//    }
//    /**
//     * @return the m_openitems
//     */
//    public List<SapFiOpenItem> getM_openitems() {
//        return m_openitems;
//    }
//
//    /**
//     * @param m_openitems the m_openitems to set
//     */
//    public void setM_openitems(List<SapFiOpenItem> m_openitems) {
//        this.m_openitems = m_openitems;
//    }
    /**
     * @return the mCurrentState
     */
    public String getCurrentState() {
        return mCurrentState;
    }

    /**
     * @param mCurrentState the mCurrentState to set
     */
    public void setCurrentState(String mCurrentState) {
        this.mCurrentState = mCurrentState;
    }

    /**
     *
     * @param id id
     * @param key icd key
     */
    public void addNewDiag(long id, long key) {
        if (getNewDiags() == null) {
            setNewDiags(new HashMap<>());
        }
        getNewDiags().put(id, key);
    }

    /**
     *
     * @param id id
     * @param key ops key
     */
    public void addNewProc(long id, long key) {
        if (getNewProcs() == null) {
            setNewProcs(new HashMap<>());
        }
        getNewProcs().put(id, key);
    }

    /**
     *
     * @param id id
     * @return SAP icd key
     */
    public long getKISSDiagKey(long id) {
        if (getNewDiags() != null) {
            Long obj = getNewDiags().get(id);
            return obj;
        }
        return -1;
    }

    /**
     *
     * @param id id
     * @return SAP ops key
     */
    public long getKISSProcKey(long id) {
        if (getNewProcs() != null) {
            Long obj = getNewProcs().get(id);
            return obj;
        }
        return -1;
    }

    /**
     *
     * @param fact factura
     * @return factura
     */
    public SapFiFactura addFactura(SapFiFactura fact) {
        if (fact == null) {
            return fact;
        }
        mFactura.add(fact);
        return fact;
    }

    /**
     *
     * @param belegnr bill number
     * @return factura
     */
    public SapFiFactura getFacturaForNr(String belegnr) {
        if (belegnr == null) {
            return null;
        }
        for (int i = 0, n = mFactura.size(); i < n; i++) {
            SapFiFactura fact = mFactura.get(i);
            if (fact.getVbeln().equals(belegnr)) {
                return fact;
            }
        }
        return null;
    }

    /**
     *
     * @return list of SAP facturas
     */
    public List<SapFiFactura> getFacturas() {
        return new ArrayList<>(mFactura);
    }

//    /**
//     *
//     * @param v list of SAP factura
//     */
//    public void setFacturas(final List<SapFiFactura> v) {
//        setM_factura(v == null ? null : new ArrayList<>(v));
//    }
    /**
     *
     * @param item SAP open FI item
     * @return SAP open FI item
     */
    public SapFiOpenItem addOpenItem(SapFiOpenItem item) {
        if (item == null) {
            return item;
        }
        mOpenItems.add(item);
        return item;
    }

    /**
     *
     * @return list of open SAP FI items
     */
    public List<SapFiOpenItem> getOpenItems() {
        return new ArrayList<>(mOpenItems);
    }

    /**
     *
     * @param fnr case number
     */
    public void addClosedCaseNumber(String fnr) {
        closedCaseNumbers.add(fnr);
    }

//    /**
//     *
//     * @return list of case numbers
//     */
//    public List<String> getClosedCaseNumbers() {
//        return closedCaseNumbers == null ? null : new ArrayList<>(getClosedCaseNumbers());
//    }
    /**
     *
     * @param fnr case number
     */
    public void addNotClosedCaseNumber(String fnr) {
        notClosedCaseNumbers.add(fnr);
    }

}
