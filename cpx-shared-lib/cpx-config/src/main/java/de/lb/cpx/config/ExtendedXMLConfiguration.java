/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.config;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 *
 * @author Dirk Niemeier
 * http://stackoverflow.com/questions/5761610/format-xml-output-with-apache-commons-configuration-xmlconfiguration
 */
public class ExtendedXMLConfiguration extends XMLConfiguration {

    public ExtendedXMLConfiguration() throws ConfigurationException {
        super();
    }

    @Override
    protected Transformer createTransformer() {
        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(ExtendedXMLConfiguration.class.getName()).log(Level.SEVERE, "cannot set attribute FEATURE_SECURE_PROCESSING = true", ex);
        }
//        try {
//            transformerFactory.setAttribute("indent-number", 2);
//        } catch (IllegalArgumentException ex) {
//            LOG.log(Level.SEVERE, "Cannot set Indent-Number", ex);
//        }
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(ExtendedXMLConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (transformer != null) {
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        }
        return transformer;
    }

}
