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

import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author gerschmann
 */
public class Segment extends KainInkaElement {

    private int maxCount;
    private int lfdNummer = 0;

    /**
     *
     */
    public Segment() {
    }

    /**
     *
     * @param elem element
     */
    public Segment(Element elem) {
        super(elem);
    }

    /**
     *
     * @param elem element
     * @param n2o n2o
     */
    public Segment(Element elem, Map<String, KainInkaElement> n2o) {
        super(elem);
        maxCount = getIntValue(elem.getAttribute(KainInkaStatics.ATTRIBUTE_MAX_COUNT));
        // damit hashmap nict vermüllt wird
        children = getChildList(elem.getChildNodes(), n2o);
    }

    /**
     *
     * @param i incrementing number
     */
    public void setLfdNummer(int i) {
        lfdNummer = i;
    }

    /**
     *
     * @return incrementing number
     */
    public int getLfdNummer() {
        return lfdNummer;
    }

    /**
     *
     * @return check result
     */
    @Override
    public boolean check() {
        if (maxCount > 0 && getChildSegmentCount() > maxCount) {
            return false;
        }
        return super.check();
    }

    /**
     *
     * @param builder builder
     */
    @Override
    protected void getSimpleFileds(StringBuilder builder) {
        super.getSimpleFileds(builder);
        builder.append("maxCount = ").append(maxCount).append("\r\n");
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
     * @return result value
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
        addXMLElement(doc, elem, KainInkaStatics.ELEMENT_SEGMENT);
    }

    /**
     *
     * @param elem element
     */
    @Override
    protected void fillAttributes(Element elem) {
        super.fillAttributes(elem);
        if (maxCount > 0) {
            elem.setAttribute(KainInkaStatics.ATTRIBUTE_MAX_COUNT, String.valueOf(maxCount));
        }
    }

    private int getChildSegmentCount() {
        int ret = 0;
        if (maxCount > 0) {
            for (KainInkaElement child : children) {
                if (child instanceof Segment) {
                    ret++;
                }
            }
        }
        return ret;
    }

}
