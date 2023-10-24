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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * reads inka/kain messages from xml in the KainInka objects and writes XML from
 * KainInkaObjects
 *
 * @author gerschmann
 */
public abstract class KainInkaProcessor {

    /**
     *
     */
    public static final String ENCODING = "Cp1252";

    private static final Logger LOG = Logger.getLogger(KainInkaProcessor.class.getName());

    /**
     *
     */
    public KainInkaProcessor() {
    }

    /**
     * reads the pattern xml from site/kainInka/kainInka_vorlage.xml
     *
     * @return KainInkaMessage
     */
    public static KainInkaMessage createOriginalMessage() {
        KainInkaMessage originalMessage = null;
        try {
            //String origXml = AppServer.getApplicationServer().getKainInkaOriginal();
            String origXml = getKainInkaOriginal();

            originalMessage = readXML(origXml, LOG);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "error on reading the pattern xml for kain/inka messages");
            LOG.log(Level.SEVERE, null, e);
        }
        return originalMessage;
    }

    /**
     *
     * @return kain/inka xml content
     * @throws Exception exception
     */
    public static String getKainInkaOriginal() throws Exception {
        //String path = AppServer.getWebServerPathProperty() + File.separator + XML_PATTERN_PATH;
        //File file = new File(path);
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String content = "";
        try (InputStream is = classloader.getResourceAsStream("KainInka_vorlage.xml")) {
            content = convertStreamToString(is);
        }

        return content;
    }

    /**
     *
     * @param is input stream
     * @return string
     */
    public static String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is, ENCODING).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    /**
     * data recieved as string
     *
     * @param content Content
     * @param logger Logger
     * @return Kain/Inka Message
     * @throws java.lang.Exception Exception
     */
    public static KainInkaMessage readXML(String content, Logger logger) throws Exception {
        if (content == null) {
            return null;
        }
        KainInkaMessage msg = null;
        Document doc = getXMLDocument(new ByteArrayInputStream(content.getBytes(ENCODING)));
        if (doc != null) {
            Element root = doc.getDocumentElement();
            msg = new KainInkaMessage(root);
            //System.out.println(msg.toString());
        }
        return msg;
    }

    /**
     *
     * @return document
     * @throws Exception exception
     */
    protected static Document getXMLDocument() throws Exception {
        return getXMLDocument(null);
    }

    /**
     *
     * @param in input stream
     * @return document
     * @throws Exception exception
     */
    protected static Document getXMLDocument(InputStream in) throws Exception {
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
                "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc;
        if (in != null) {
            doc = db.parse(in);
        } else {
            doc = db.newDocument();
        }
        return doc;
    }

    /**
     * liefert einen String aus Datum dem Typ der DB Verbindung entsprechend
     * Wird in den abgeleiteten Klassen überschrieben
     *
     * @param date Date
     * @return Formatted date
     */
    protected abstract String getDateFormated4Insert(Date date);

    /**
     *
     * @param strMsg message
     * @return ?
     */
    protected String getMsgFormated4Insert(String strMsg) {
        return "?";

    }

    /**
     * returns a create statement for one imex fm table
     *
     * @param tableName Table name
     * @return Query?
     */
    protected abstract String getCreateImexFmTableStatement(String tableName);

}
