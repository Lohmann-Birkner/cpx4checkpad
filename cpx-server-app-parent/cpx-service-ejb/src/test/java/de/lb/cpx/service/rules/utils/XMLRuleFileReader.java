/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.rules.utils;

import de.checkpoint.ruleGrouper.CRGRule;
import de.checkpoint.server.data.caseRules.DatCaseRuleAttributes;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author gerschmann
 */
public class XMLRuleFileReader {

    private static final String RULE_FILE_NAME = "rules/2017csrules.xml";
    private static final Logger LOG = Logger.getLogger(XMLRuleFileReader.class.getName());

    public static XMLRuleFileReader getInstance() {
        return new XMLRuleFileReader();
    }

    public static CRGRule[] readRulesFromFile() throws URISyntaxException {
        ClassLoader classLoader = XMLRuleFileReader.class.getClassLoader();
        File xmlRuleFile = new File(new File(classLoader.getResource(RULE_FILE_NAME).toURI()).getAbsolutePath().replaceAll("%20", " "));
        LOG.log(Level.INFO, "XML rule file: " + xmlRuleFile.getAbsolutePath());
        CRGRule[] result = null;
        try {
            Document doc = getXMLDocument(xmlRuleFile);
            if (doc != null) {
                NodeList nl = doc.getElementsByTagName(DatCaseRuleAttributes.ELEMENT_RULES);
                int nlCount = nl != null ? nl.getLength() : 0;
                result = new CRGRule[nlCount];
                for (int i = 0; i < nlCount; i++) {
                    Element ele = (Element) nl.item(i);
                    CRGRule rule = new CRGRule(ele, null, null, CRGRule.RULE_ANALYSE_NO_TABLES);
                    result[i] = rule;
                }
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "error on reading rule - xml file", ex);
        }
        return result;
    }

    private static Document getXMLDocument(File file) throws Exception {
        if (file != null) {

            System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
                    "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            FileInputStream fin = new FileInputStream(file);
            Document doc = db.parse(fin);
            return doc;

        }
        return null;
    }

    public static String readXml() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document impDoc = null;
        ClassLoader classLoader = XMLRuleFileReader.class.getClassLoader();
        File xmlRuleFile = new File(new File(classLoader.getResource(RULE_FILE_NAME).toURI()).getAbsolutePath().replaceAll("%20", " "));
        LOG.log(Level.INFO, "XML rule file: " + xmlRuleFile.getAbsolutePath());
        byte[] encoded = Files.readAllBytes(xmlRuleFile.toPath());
        String testStr = new String(encoded);
        impDoc = db.parse(new ByteArrayInputStream(testStr.getBytes()));
        return "";

    }
}
