/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.rule.element;

import de.lb.cpx.rule.element.model.Rule;
import de.lb.cpx.rule.element.model.RulesElement;
import de.lb.cpx.rule.util.XMLHandler;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import org.apache.commons.text.StringEscapeUtils;

/**
 * Case Rule Manager to transform rules from string to object and vice versa
 *
 * @author wilde
 */
public class CaseRuleManager {

    private CaseRuleManager() {
        //utility class needs no public constructor
    }

    public static byte[] getDefaultRuleContent() throws UnsupportedEncodingException {
        Rule rule = new Rule();
        rule.setRulesNotice("");
        rule.setRulesElement(new RulesElement());
        String xml = transformObject(rule);
        return transformObject(rule, "UTF-16");
    }

    public static Rule transfromRule(String pRule) {
        try {
            return (Rule) XMLHandler.unmarshalXML(pRule, Rule.class);
        } catch (JAXBException ex) {
            Logger.getLogger(CaseRuleManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static Rule transformRuleInUTF8(byte[] pRule) {
        try {
            return transfromRule(pRule, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CaseRuleManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static Rule transformRuleInUTF16(byte[] pRule) {
        try {
            return transfromRule(pRule, "UTF-16");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CaseRuleManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static Rule transfromRule(byte[] pRule) throws UnsupportedEncodingException {
        return transfromRule(pRule, null);
    }

    public static Rule transfromRule(byte[] pRule, String pEncoding) throws UnsupportedEncodingException {
        try {
            String rule = byteToString(pRule, pEncoding);//pEncoding!=null?new String(pRule,pEncoding):new String(pRule);
            return (Rule) XMLHandler.unmarshalXML(rule, Rule.class);
        } catch (JAXBException ex) {
            Logger.getLogger(CaseRuleManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String transformObject(Rule pRule) {
        try {
            return XMLHandler.marshalXML(pRule, Rule.class);
        } catch (JAXBException ex) {
            Logger.getLogger(CaseRuleManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static byte[] transformObject(Rule pRule, String pEncoding) throws UnsupportedEncodingException {

        String xmlString = transformObject(pRule);
        if (xmlString != null) {
            return xmlString.getBytes(pEncoding);
        }
        return null;
    }

    public static String getXMLText(String text) {
        if (text == null) {
            return "";
        }
        text = text.replaceAll("<", "&lt;");
        text = text.replaceAll(">", "&gt;");
        return text.replaceAll("\n", "&#013");
    }

    public static String getDisplayText(String text) {
        if (text == null) {
            return "";
        }
        text = StringEscapeUtils.unescapeHtml4(text);
        text = text.replaceAll("&lt;", "<");
        text = text.replaceAll("&gt;", ">");
        if (text.length() >= 2) {
            if ((text.charAt(0) == '>' || text.charAt(0) == '<')
                    && text.charAt(1) == '&') {
                text = text.replaceAll("&", "");
            }
        }
        return text.replaceAll("&#013", "\n");
    }

    public static String byteToString(byte[] pRule, String pEncoding) throws UnsupportedEncodingException {
        return pEncoding != null ? new String(pRule, pEncoding) : new String(pRule);
    }

    public static String byteToStringUTF8(byte[] pRule) throws UnsupportedEncodingException {
        return byteToString(pRule, "UTF-8");
    }

    public static String byteToStringUTF16(byte[] pRule) throws UnsupportedEncodingException {
        return byteToString(pRule, "UTF-16");
    }

}
