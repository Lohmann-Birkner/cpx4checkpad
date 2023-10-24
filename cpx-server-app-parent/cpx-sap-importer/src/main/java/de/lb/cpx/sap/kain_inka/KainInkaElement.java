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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author gerschmann
 */
public abstract class KainInkaElement {

    private static final Logger LOG = Logger.getLogger(KainInkaElement.class.getName());
    protected Element xmlElement = null;
    private String name;
    private String comment;
    protected boolean nullable;
    protected List<KainInkaElement> children = null;
    protected HashMap<String, KainInkaElement> localName2element = new HashMap<>();

    /**
     *
     */
    public KainInkaElement() {
    }

    /**
     *
     * @param elem element
     */
    public KainInkaElement(Element elem) {
        xmlElement = elem;
        name = elem.getAttribute(KainInkaStatics.ATTRIBUTE_NAME);
        comment = elem.getAttribute(KainInkaStatics.ATTRIBUTE_COMMENT);
        nullable = getBooleanValue(elem.getAttribute(KainInkaStatics.ATTRIBUTE_NULLABLE));
    }

    /**
     *
     * @return name name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return check
     */
    public boolean check() {
        for (KainInkaElement child : children) {
            if (!child.check()) {
                return false;
            }
        }
        return true;

    }

    /**
     *
     * @param l numeric value
     * @return integer
     */
    protected final int getIntValue(String l) {
        try {
            if (l != null && !l.isEmpty()) {
                return Integer.parseInt(l);
            }
            return 0;
        } catch (NumberFormatException e) {
            LOG.log(Level.SEVERE, "Error on parsing in value from " + l, e);
            return 0;
        }
    }

    /**
     *
     * @param attribute boolean value
     * @return boolean value
     */
    protected final boolean getBooleanValue(String attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return true;

        }
        return "true".equalsIgnoreCase(attribute);
    }

    /**
     *
     * @param value value
     * @param format format
     * @return object
     */
    protected final Object getDateValue(String value, String format) {
        if (value == null || value.isEmpty() || format == null || format.isEmpty()) {
            return null;
        }
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        try {
            return fmt.parse(value);
        } catch (ParseException e) {
            LOG.log(Level.SEVERE, "Error on parsing date value from " + value, e);
            return null;
        }
    }

    /**
     *
     * @param fieldName field name
     * @param name2element name2element
     * @return object
     */
    protected Object getFieldValue(String fieldName, Map<String, KainInkaElement> name2element) {
        KainInkaElement elem = name2element.get(fieldName);
        return elem.getResultValue();

    }

    /**
     *
     * @param nodeList node list
     * @param name2element name2element
     * @return list of kain/inka elements
     */
    protected final List<KainInkaElement> getChildList(NodeList nodeList, Map<String, KainInkaElement> name2element) {
        List<KainInkaElement> retList = new ArrayList<>();
        if (nodeList == null) {
            return retList;
        }
        int count = nodeList.getLength();
        for (int i = 0; i < count; i++) {
            Node node = nodeList.item(i);
            KainInkaElement ki = null;
            switch (node.getNodeName()) {
                case KainInkaStatics.ELEMENT_SEGMENT:
                    String nm = ((Element) node).getAttribute(KainInkaStatics.ATTRIBUTE_NAME);
                    if (nm.equalsIgnoreCase(KainInkaStatics.NAME_SEGMENT_PVT_CHILD)) {
                        ki = new PvtChildSegment((Element) node);
                    } else if (nm.equalsIgnoreCase(KainInkaStatics.NAME_SEGMENT_PVV_CHILD)) {
                        ki = new PvvChildSegment((Element) node);
                    } else {
                        ki = new Segment((Element) node, name2element);
                    }

                    break;
                case KainInkaStatics.ELEMENT_PROPERTY:
                    ki = new Property((Element) node);

                    break;
                case KainInkaStatics.ELEMENT_CODE_ELEMENT:
                    ki = new CodeElement((Element) node);

                    break;
                default:
                    break;
            }
            if (ki != null) {
                retList.add(ki);
                name2element.put(ki.getName(), ki);
            }
        }
        return retList;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        getSimpleFileds(builder);
        if (children != null) {
            for (KainInkaElement child : children) {
                builder.append(child.toString());
            }
        }
        return builder.toString();
    }

    /**
     *
     * @param builder builder
     */
    protected void getSimpleFileds(StringBuilder builder) {
        builder.append("name = ").append(name == null ? "" : name).append("\r\n");
        builder.append("comment = ").append(comment == null ? "" : comment).append("\r\n");
        builder.append("nullable = ").append(String.valueOf(nullable)).append("\r\n");
    }

    /**
     *
     * @param value value
     */
    protected void setValue(Object value) {
    }

    /**
     *
     * @param fieldName field name
     * @param name2element name2element
     * @param value value
     */
    protected void setFieldValue(String fieldName, Map<String, KainInkaElement> name2element, Object value) {
        KainInkaElement elem = name2element.get(fieldName);
        elem.setValue(value);
    }

    /**
     *
     * @return object
     */
    protected abstract Object getResultValue();

    /**
     *
     * @param doc document
     * @param parent parent
     * @param tag tag
     */
    protected void addXMLElement(Document doc, Element parent, String tag) {
        Element elem = doc.createElement(tag);
        if (parent == null) {
            doc.appendChild(elem);
        } else {
            parent.appendChild(elem);
        }
        fillAttributes(elem);
        if (children != null) {
            for (KainInkaElement ki : children) {
                ki.addXMLElement(doc, elem);
            }
        }

    }

    /**
     *
     * @param doc document
     * @param elem element
     */
    protected abstract void addXMLElement(Document doc, Element elem);

    /**
     *
     * @param elem element
     */
    protected void fillAttributes(Element elem) {
        elem.setAttribute(KainInkaStatics.ATTRIBUTE_NAME, name);
        elem.setAttribute(KainInkaStatics.ATTRIBUTE_COMMENT, comment);
        elem.setAttribute(KainInkaStatics.ATTRIBUTE_NULLABLE, String.valueOf(nullable));

    }

    /**
     *
     * @return list of kain/inka elements
     */
    public List<KainInkaElement> getChildren() {
        return children;
    }

    /**
     * alle child - Knoten ausser Property werden entfernt. Wird benutzt um die
     * neuen PVV/PVT Segmente zuzufügen/ersetzen
     *
     */
    protected void removeNotPropertes() {
        ArrayList<KainInkaElement> toRemove = new ArrayList<>();
        for (KainInkaElement child : children) {
            if (child instanceof Property) {
                continue;
            }
            toRemove.add(child);
        }
        children.removeAll(toRemove);
    }

}
