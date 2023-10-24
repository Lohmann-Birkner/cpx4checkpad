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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author gerschmann
 */
public class CodeElement extends KainInkaElement {

    private CodeElement() {
    }

    /**
     *
     * @param elem element
     */
    public CodeElement(Element elem) {
        super(elem);
        children = getChildList(elem.getChildNodes(), this.localName2element);
    }

    /**
     *
     * @return is always true
     */
    @Override
    public boolean check() {
        return true;
    }

    /**
     *
     * @param value value
     */
    @Override
    protected void setValue(Object value) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @return is always null
     */
    @Override
    protected Object getResultValue() {
        return null;
    }

    /**
     *
     * @param doc document
     * @param elem element
     */
    @Override
    protected void addXMLElement(Document doc, Element elem) {
        addXMLElement(doc, elem, KainInkaStatics.ELEMENT_CODE_ELEMENT);
    }

    public void setCodeAndLoc(String code, String loc) {
        this.setFieldValue(KainInkaStatics.NAME_PROPERTY_CODE, localName2element, code);
        this.setFieldValue(KainInkaStatics.NAME_PROPERTY_LOCALISATION, localName2element, loc);
    }

    /**
     *
     * @return code
     */
    public String getCode() {
        return (String) getFieldValue(KainInkaStatics.NAME_PROPERTY_CODE, localName2element);
    }

    /**
     *
     * @return localisation
     */
    public String getLocalisation() {
        return (String) getFieldValue(KainInkaStatics.NAME_PROPERTY_LOCALISATION, localName2element);
    }

}
