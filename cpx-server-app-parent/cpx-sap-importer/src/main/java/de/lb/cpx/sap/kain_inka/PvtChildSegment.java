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
package de.lb.cpx.sap.kain_inka;

import org.w3c.dom.Element;

/**
 *
 * @author gerschmann
 */
public class PvtChildSegment extends Segment implements PvtResultIf {

    /**
     *
     * @param elem element
     */
    public PvtChildSegment(Element elem) {
        super(elem);
        children = getChildList(elem.getChildNodes(), localName2element);
    }

    /**
     *
     * @param pvt pvt
     */
    public void setValue(PvtResultIf pvt) {
        setText(pvt.getText());
        setPVTPrincipalDiag(pvt.getPVTPrincipalDiagCode(), pvt.getPVTPrincipalDiagLoc());
        setPVTPrincipalSecondaryDiag(pvt.getPVTPrincipalDiagSecondaryCode(), pvt.getPVTPrincipalDiagSecondaryLoc());
        setPVTAuxDiag(pvt.getPVTAuxDiagCode(), pvt.getPVTAuxDiagLoc());
        setPVTSecondaryAuxDiag(pvt.getPVTAuxDiagSecondaryCode(), pvt.getPVTAuxDiagSecondaryLoc());
        setPVTProcedure(pvt.getPVTProcedureCode(), pvt.getPVTProcedureLoc());
    }

    /**
     *
     * @param text text
     */
    @Override
    public void setText(String text) {
        setFieldValue(KainInkaStatics.NAME_PROPERTY_PVT_TEXT, localName2element, text);
    }

    /**
     * Set principal diagnosis
     *
     * @param code Code
     * @param loc localisation
     */
    @Override
    public void setPVTPrincipalDiag(String code, String loc) {
        CodeElement elem = (CodeElement) localName2element.get(KainInkaStatics.NAME_CODE_ELEMENT_PRINCIPAL_DIAGNOSIS);
        elem.setCodeAndLoc(code, loc);
    }

    /**
     *
     * @param code code
     * @param loc localisation
     */
    @Override
    public void setPVTPrincipalSecondaryDiag(String code, String loc) {
        CodeElement elem = (CodeElement) localName2element.get(KainInkaStatics.NAME_CODE_ELEMENT_SECONDARY_2_PRINCIPAL_DIAGNOSIS);
        elem.setCodeAndLoc(code, loc);
    }

    /**
     *
     * @param code code
     * @param loc localisation
     */
    @Override
    public void setPVTAuxDiag(String code, String loc) {
        CodeElement elem = (CodeElement) localName2element.get(KainInkaStatics.NAME_CODE_ELEMENT_AUX_DIAGNOSIS);
        elem.setCodeAndLoc(code, loc);
    }

    /**
     *
     * @param code code
     * @param loc localisation
     */
    @Override
    public void setPVTSecondaryAuxDiag(String code, String loc) {
        CodeElement elem = (CodeElement) localName2element.get(KainInkaStatics.NAME_CODE_ELEMENT_SECONDARY_AUX_DIAGNOSIS);
        elem.setCodeAndLoc(code, loc);
    }

    /**
     *
     * @param code code
     * @param loc localisation
     */
    @Override
    public void setPVTProcedure(String code, String loc) {
        CodeElement elem = (CodeElement) localName2element.get(KainInkaStatics.NAME_CODE_ELEMENT_PROCEDURE);
        elem.setCodeAndLoc(code, loc);
    }

    /**
     *
     * @return icd code
     */
    @Override
    public String getPVTPrincipalDiagCode() {
        CodeElement elem = (CodeElement) localName2element.get(KainInkaStatics.NAME_CODE_ELEMENT_PRINCIPAL_DIAGNOSIS);
        return elem.getCode();
    }

    /**
     *
     * @return icd localisation
     */
    @Override
    public String getPVTPrincipalDiagLoc() {
        CodeElement elem = (CodeElement) localName2element.get(KainInkaStatics.NAME_CODE_ELEMENT_PRINCIPAL_DIAGNOSIS);
        return elem.getLocalisation();
    }

    /**
     *
     * @return icd secondary code
     */
    @Override
    public String getPVTPrincipalDiagSecondaryCode() {
        CodeElement elem = (CodeElement) localName2element.get(KainInkaStatics.NAME_CODE_ELEMENT_SECONDARY_2_PRINCIPAL_DIAGNOSIS);
        return elem.getCode();
    }

    /**
     *
     * @return icd secondary localisation
     */
    @Override
    public String getPVTPrincipalDiagSecondaryLoc() {
        CodeElement elem = (CodeElement) localName2element.get(KainInkaStatics.NAME_CODE_ELEMENT_SECONDARY_2_PRINCIPAL_DIAGNOSIS);
        return elem.getLocalisation();
    }

    /**
     *
     * @return auxiliary icd code
     */
    @Override
    public String getPVTAuxDiagCode() {
        CodeElement elem = (CodeElement) localName2element.get(KainInkaStatics.NAME_CODE_ELEMENT_AUX_DIAGNOSIS);
        return elem.getCode();
    }

    /**
     *
     * @return auxiliary icd localisation
     */
    @Override
    public String getPVTAuxDiagLoc() {
        CodeElement elem = (CodeElement) localName2element.get(KainInkaStatics.NAME_CODE_ELEMENT_AUX_DIAGNOSIS);
        return elem.getLocalisation();
    }

    /**
     *
     * @return auxiliary icd secondary code
     */
    @Override
    public String getPVTAuxDiagSecondaryCode() {
        CodeElement elem = (CodeElement) localName2element.get(KainInkaStatics.NAME_CODE_ELEMENT_SECONDARY_AUX_DIAGNOSIS);
        return elem.getCode();
    }

    /**
     *
     * @return auxiliary icd secondary localisation
     */
    @Override
    public String getPVTAuxDiagSecondaryLoc() {
        CodeElement elem = (CodeElement) localName2element.get(KainInkaStatics.NAME_CODE_ELEMENT_SECONDARY_AUX_DIAGNOSIS);
        return elem.getLocalisation();
    }

    /**
     *
     * @return ops code
     */
    @Override
    public String getPVTProcedureCode() {
        CodeElement elem = (CodeElement) localName2element.get(KainInkaStatics.NAME_CODE_ELEMENT_PROCEDURE);
        return elem.getCode();
    }

    /**
     *
     * @return ops localisation
     */
    @Override
    public String getPVTProcedureLoc() {
        CodeElement elem = (CodeElement) localName2element.get(KainInkaStatics.NAME_CODE_ELEMENT_PROCEDURE);
        return elem.getLocalisation();
    }

    /**
     *
     * @return text
     */
    @Override
    public String getText() {
        return (String) getFieldValue(KainInkaStatics.NAME_PROPERTY_PVT_TEXT, localName2element);
    }

}
