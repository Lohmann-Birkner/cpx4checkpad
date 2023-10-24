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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author gerschmann
 */
public class Property extends KainInkaElement {

    private static final Logger LOG = Logger.getLogger(Property.class.getName());

    private String dataType = "String";
    private String value;
    private String format;
    private int length = 0;
    private int maxlength = 0;
    private String pEnum;
    private Object resultValue = null;

    private Property() {

    }

    /**
     *
     * @param elem element
     */
    public Property(Element elem) {
        super(elem);
        dataType = elem.getAttribute(KainInkaStatics.ATTRIBUTE_DATA_TYPE);
        value = elem.getAttribute(KainInkaStatics.ATTRIBUTE_VALUE);
        format = elem.getAttribute(KainInkaStatics.ATTRIBUTE_FORMAT);
        length = getIntValue(elem.getAttribute(KainInkaStatics.ATTRIBUTE_LENGTH));
        maxlength = getIntValue(elem.getAttribute(KainInkaStatics.ATTRIBUTE_MAXLENGTH));
        pEnum = elem.getAttribute(KainInkaStatics.ATTRIBUTE_ENUM);
        setResultValue();
    }

    private void setResultValue() {
        switch (dataType) {
            case "Integer":
                resultValue = getIntValue(value);
                break;
            case "Date":
                resultValue = getDateValue(value, format);
                break;
            default:
                resultValue = value;
        }
    }

    /**
     *
     * @return check result
     */
    @Override
    public boolean check() {
        if (length > 0 && value != null && value.length() != length) { // fixed length
            return false;
        }
        if (maxlength > 0 && value != null && value.length() > maxlength) { // got max length
            return false;
        }
        if (value == null) {
            LOG.log(Level.WARNING, "value is null!");
        }
        if (pEnum != null && !pEnum.isEmpty()) { // member enumeration(Only defined values)
            String[] parts = pEnum.split(",");
            boolean found = false;
            String val = (value == null ? "" : value.trim());
            for (String part : parts) {
                if (part.trim().equals(val)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        if (!nullable && resultValue == null) {  // is must field
            return false;
        }
        return true;
    }

    /**
     *
     * @param builder builder
     */
    @Override
    protected void getSimpleFileds(StringBuilder builder) {
        super.getSimpleFileds(builder);
        builder.append("data_type = ").append(dataType == null ? "" : dataType).append("\r\n");
        builder.append("value = ").append(value == null ? "" : value).append("\r\n");
        builder.append("format = ").append(format == null ? "" : format).append("\r\n");
        builder.append("length =  ").append(length).append("\r\n");
        builder.append("maxlength =  ").append(maxlength).append("\r\n");
        builder.append("pEnum = ").append(pEnum == null ? "" : pEnum).append("\r\n");
    }

    /**
     *
     * @param val value
     */
    @Override
    protected void setValue(Object val) {
        resultValue = null;
        value = "";
        switch (dataType) {
            case "Integer":
                if (val instanceof Integer) {
                    value = String.valueOf(val);
                    resultValue = val;
                }
                break;
            case "Date":
                if (val instanceof Date && format != null && !format.isEmpty()) {// sql.Date or util.Date
                    SimpleDateFormat fmt = new SimpleDateFormat(format);
                    value = fmt.format((Date) val);
                    resultValue = val;
                }
                break;
            case "String":
            case "":
                if (val instanceof String) {
                    value = (String) val;
                    resultValue = val;
                }
                break;
            default:
                LOG.log(Level.WARNING, "I cannot handle this value: {0}", String.valueOf(val));
                break;
        }

    }

    /**
     *
     * @return result value
     */
    @Override
    protected Object getResultValue() {
        return resultValue;
    }

    /**
     *
     * @param doc document
     * @param elem element
     */
    @Override
    protected void addXMLElement(Document doc, Element elem) {
        addXMLElement(doc, elem, KainInkaStatics.ELEMENT_PROPERTY);
    }

    /**
     *
     * @param elem element
     */
    @Override
    protected void fillAttributes(Element elem) {
        super.fillAttributes(elem);
        if (dataType != null) {
            elem.setAttribute(KainInkaStatics.ATTRIBUTE_DATA_TYPE, dataType);
        }
        elem.setAttribute(KainInkaStatics.ATTRIBUTE_VALUE, value == null ? "" : value);
        if (format != null) {
            elem.setAttribute(KainInkaStatics.ATTRIBUTE_FORMAT, format);
        }
        if (length > 0) {
            elem.setAttribute(KainInkaStatics.ATTRIBUTE_LENGTH, String.valueOf(length));
        }
        if (maxlength > 0) {
            elem.setAttribute(KainInkaStatics.ATTRIBUTE_MAXLENGTH, String.valueOf(maxlength));
        }
        if (pEnum != null && !pEnum.isEmpty()) {
            elem.setAttribute(KainInkaStatics.ATTRIBUTE_ENUM, pEnum);
        }
    }

}
