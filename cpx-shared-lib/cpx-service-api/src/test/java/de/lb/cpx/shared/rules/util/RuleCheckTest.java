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
package de.lb.cpx.shared.rules.util;

import de.checkpoint.drg.RulerInputObjectNull;
import de.checkpoint.ruleGrouper.CRGInputOutput;
import de.checkpoint.ruleGrouper.CRGRule;
import de.checkpoint.ruleGrouper.CheckpointRuleGrouper;
import de.checkpoint.ruleGrouper.cpx.CPXRuleGrouper;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Element;

/**
 *
 * @author gerschmann
 */
@Ignore
public class RuleCheckTest {

    private static final String testIcdEqualsTerm
            = "<rule>"
            + "<rules_element>"
            + "<rules_value  kriterium=\"Diagnose\"  operator=\"==\"  wert=\"'A%'\"/>"
            + "</rules_element>"
            + "</rule>";

    private static final String testLocInTableTerm
            = "<rule>"
            + "<rules_element>"
            + "<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"DiagnoseLokalisation\" method=\"\" not=\"false\" operator=\"@\" parameter=\"\" wert=\"'DIAGNOSE_LOKAL'\"/>"
            + "</rules_element>"
            + "</rule>";
    private static final String testIcdInTableTerm
            = "<rule>"
            + "<rules_element>"
            + "<rules_value  kriterium=\"Diagnose\" method=\"\" operator=\"@\"  wert=\"'IcdTable'\"/>"
            + "</rules_element>"
            + "</rule>";
    private static final String testVwdWithUgvdTerm
            = "<rule>"
            + "<rules_element>"
            + "<rules_value  kriterium=\"Verweildauer\" />"
            + "<rules_operator op_type=\"&amp;lt;\"/>"
            + "<rules_value  kriterium=\"untere_Grenzverweildauer\" />"
            + "</rules_element>"
            + "</rule>";

    private static final String testTermWithBrackets
            = //            "<rule caption=\"\" entgelt=\"true\" errror_type=\"Sonstige\" feegroup=\"\" from=\"01.01.2019\" hasinterval=\"false\" interval_from=\"\" interval_to=\"\" massnumber=\"0\" medtype=\"S\" number=\"\" profit=\"0.0\" rid=\"1002153\" role=\"1\" rules_notice=\"\" rules_number=\"2019_1002153_20190926150143\" rules_year=\"2019\" text=\"\" to=\"31.12.2019\" typ=\"error\" unchange=\"0\" used=\"true\" visible=\"\">\n" +
            //"<rules_element nested=\"false\">\n" +
            //"<rules_element nested=\"true\">\n" +
            //"<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"Verweildauer\" method=\"\" not=\"false\" operator=\"\" parameter=\"\" wert=\"\"/>\n" +
            //"<rules_operator op_type=\"&amp;lt;\"/>\n" +
            //"<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"untere_Grenzverweildauer\" method=\"\" not=\"false\" operator=\"\" parameter=\"\" wert=\"\"/></rules_element>\n" +
            //"<rules_operator op_type=\"&amp;&amp;\"/>\n" +
            //"<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"DRG\" method=\"\" not=\"false\" operator=\"==\" parameter=\"\" wert=\"'960Z'\"/>\n" +
            //"<rules_operator op_type=\"||\"/>\n" +
            //"<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"Hauptdiagnose\" method=\"\" not=\"false\" operator=\"==\" parameter=\"\" wert=\"'A%'\"/>\n" +
            //"<rules_operator op_type=\"&amp;&amp;\"/>\n" +
            //"<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"Nebendiagnose\" method=\"\" not=\"false\" operator=\"==\" parameter=\"\" wert=\"'M%'\"/></rules_element>\n" +
            //"<suggestions suggtext=\"\"/></rule>";
            "<rule>"
            + "<rules_element>"
            + "<rules_element nested=\"true\">"
            + "<rules_value hasinterval=\"false\"  kriterium=\"Verweildauer\"/>"
            + "<rules_operator op_type=\"&amp;lt;\"/>"
            + "<rules_value kriterium=\"untere_Grenzverweildauer\"/>"
            + "</rules_element>"
            + "<rules_operator op_type=\"||\"/>"
            + "<rules_value kriterium=\"DRG\" method=\"\" not=\"false\" operator=\"==\" wert=\"'960Z'\"/>"
            + "</rules_element>"
            + "</rule>";

    private static final String icdTable = "A01.01, A02.02";
    private static final String DIAGNOSE_LOKAL = "2,3";

    private static final Logger LOG = Logger.getLogger(RuleCheckTest.class.getName());
    private CheckpointRuleGrouper ruleGrouper;
    private CRGInputOutput inout;
    private CpxRuleManagerIF ruleMgr;

    public RuleCheckTest() {
    }

    @Before
    public void setup() {
        //create checkpointRuleGrouper    

        ruleGrouper = new CPXRuleGrouper();
        inout = new CRGInputOutput(new RulerInputObjectNull());
        ruleGrouper.setInout(inout);
        ruleMgr = new CPXTestRulesManager();
        ((CPXTestRulesManager) ruleMgr).addRuleTable("IcdTable", null, 0, icdTable);
        ((CPXTestRulesManager) ruleMgr).addRuleTable("DIAGNOSE_LOKAL", null, 0, DIAGNOSE_LOKAL);

    }

    /**
     * test term Diagnose=='A%'
     */
    @Test
    public void diagnoseEqualsTest() {
        try {
            byte[] check = testIcdEqualsTerm.getBytes("UTF-16");
            Element e = RulesConverter.getRuleDomElementFromDbString(check);
            CRGRule rule = new CRGRule(e, null, null, CRGRule.RULE_ANALYSE_FULL); // hier muss isServer = true gesetzt werden, sonst rule.getRuleElement() = null
            inout.setPrincipalDiagnosis("A01.01", "A01.01", 0);
            LOG.log(Level.INFO, "rule:" + rule.toString() + " HDX = A01.01");
            boolean res = ruleGrouper.checkTerm4Rule(rule.getRuleElement(), inout);
            LOG.log(Level.INFO, "rule:" + rule.toString() + " HDX = A01.01, res = " + Boolean.toString(res));
            assertTrue("rule:" + rule.toString() + " HDX = A01.01", res);
            inout.newCase();
            inout.setPrincipalDiagnosis("B01.01", "B01.01", 0);
            res = ruleGrouper.checkTerm4Rule(rule.getRuleElement(), inout);
            LOG.log(Level.INFO, "rule:" + rule.toString() + " HDX = B01.01, res = " + Boolean.toString(res));
            assertFalse("rule:" + rule.toString() + " HDX = B01.01", res);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error", ex);
        }
    }

    /**
     * test Term Diagnose @ 'IcdTable'
     */
    @Test
    public void diagnoseInTableTest() {
        try {
            byte[] check = testIcdInTableTerm.getBytes("UTF-16");
            Element e = RulesConverter.getRuleDomElementFromDbString(check);
            CRGRule rule = new CpxCRGRule(ruleMgr, e, null, null, CRGRule.RULE_ANALYSE_FULL); // hier muss isServer = true gesetzt werden, sonst rule.getRuleElement() = null
            inout.setPrincipalDiagnosis("A01.01", "A01.01", 0);
            boolean res = ruleGrouper.checkTerm4Rule(rule.getRuleElement(), inout);
            LOG.log(Level.INFO, "rule:" + rule.toString() + " HDX = A01.01, res = " + Boolean.toString(res));
            assertTrue(res);
            inout.newCase();
            inout.setPrincipalDiagnosis("B01.01", "B01.01", 0);
            res = ruleGrouper.checkTerm4Rule(rule.getRuleElement(), inout);
            LOG.log(Level.INFO, "rule:" + rule.toString() + " HDX = B01.01, res = " + Boolean.toString(res));
            assertFalse(res);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error", ex);
        }

    }

    @Test
    public void LocalisationInTableTest() {
        try {
            byte[] check = testLocInTableTerm.getBytes("UTF-16");
            Element e = RulesConverter.getRuleDomElementFromDbString(check);
            CRGRule rule = new CpxCRGRule(ruleMgr, e, null, null, CRGRule.RULE_ANALYSE_FULL); // hier muss isServer = true gesetzt werden, sonst rule.getRuleElement() = null
            inout.setLOS(5);
            inout.addAuxiliaryDiagnosis("A01.01", "A01.01", 0, 3);
            boolean res = ruleGrouper.checkTerm4Rule(rule.getRuleElement(), inout);
            LOG.log(Level.INFO, "rule:" + rule.toString() + " HDX = A01.01, res = " + Boolean.toString(res));
            assertTrue(res);
            inout.newCase();
            inout.setPrincipalDiagnosis("B01.01", "B01.01", 0);
            inout.setLOS(5);
            inout.addAuxiliaryDiagnosis("A01.01", "A01.01", 0, 1);
            res = ruleGrouper.checkTerm4Rule(rule.getRuleElement(), inout);
            LOG.log(Level.INFO, "rule:" + rule.toString() + " HDX = B01.01, res = " + Boolean.toString(res));
            assertFalse(res);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error", ex);
        }

    }

    @Test
    public void VwdWithUgvdTermTest() {
        try {
            byte[] check = testVwdWithUgvdTerm.getBytes("UTF-16");
            Element e = RulesConverter.getRuleDomElementFromDbString(check);
            CRGRule rule = new CpxCRGRule(ruleMgr, e, null, null, CRGRule.RULE_ANALYSE_FULL); // hier muss isServer = true gesetzt werden, sonst rule.getRuleElement() = null
            inout.setLOS(5);
            inout.setUGVD(3);
            boolean res = ruleGrouper.checkTerm4Rule(rule.getRuleElement(), inout);
            LOG.log(Level.INFO, "rule:" + rule.toString() + " LOS = 5, Ugvd = 3, res = " + Boolean.toString(res));
            assertFalse(res);
            inout.newCase();
            inout.setLOS(5);
            inout.setUGVD(7);
            res = ruleGrouper.checkTerm4Rule(rule.getRuleElement(), inout);
            LOG.log(Level.INFO, "rule:" + rule.toString() + " LOS = 5, Ugvd = 7, res = " + Boolean.toString(res));
            assertTrue(res);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error", ex);
        }

    }

    @Test
    public void TermWithBracketsTest() {
        try {
            byte[] check = testTermWithBrackets.getBytes("UTF-16");
            Element e = RulesConverter.getRuleDomElementFromDbString(check);
            CRGRule rule = new CpxCRGRule(ruleMgr, e, null, null, CRGRule.RULE_ANALYSE_FULL); // hier muss isServer = true gesetzt werden, sonst rule.getRuleElement() = null
            inout.setLOS(5);
            inout.setUGVD(7);
            inout.setDRG("960Z");
            boolean res = ruleGrouper.checkTerm4Rule(rule.getRuleElement(), inout);
            LOG.log(Level.INFO, "rule:" + rule.toString() + " LOS = 5, Ugvd = 7, DRG = 960Z, res = " + Boolean.toString(res));
            assertTrue(res);
            inout.newCase();
            inout.setLOS(5);
            inout.setUGVD(7);
            inout.setDRG("B64Z");
            res = ruleGrouper.checkTerm4Rule(rule.getRuleElement(), inout);
            LOG.log(Level.INFO, "rule:" + rule.toString() + " LOS = 5, Ugvd = 7, DRG = B64Z, res = " + Boolean.toString(res));
            assertTrue(res);
            inout.newCase();
            inout.setLOS(5);
            inout.setUGVD(3);
            inout.setDRG("960Z");
            res = ruleGrouper.checkTerm4Rule(rule.getRuleElement(), inout);
            LOG.log(Level.INFO, "rule:" + rule.toString() + " LOS = 5, Ugvd = 3, DRG = 960Z, res = " + Boolean.toString(res));
            assertTrue(res);
            inout.newCase();
            inout.setLOS(5);
            inout.setUGVD(7);
            inout.setDRG("960Z");
            res = ruleGrouper.checkTerm4Rule(rule.getRuleElement(), inout);
            LOG.log(Level.INFO, "rule:" + rule.toString() + " LOS = 5, Ugvd = 7, DRG = 960Z, res = " + Boolean.toString(res));
            assertTrue(res);
            inout.newCase();
            inout.setLOS(5);
            inout.setUGVD(3);
            inout.setDRG("B64Z");
            res = ruleGrouper.checkTerm4Rule(rule.getRuleElement(), inout);
            LOG.log(Level.INFO, "rule:" + rule.toString() + " LOS = 5, Ugvd = 3, DRG = B64Z, res = " + Boolean.toString(res));
            assertFalse(res);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error", ex);
        }
    }

    @Test
    public void RuleNeedGroupCaseTest() {
        try {
            byte[] check = testVwdWithUgvdTerm.getBytes("UTF-16");
            Element e = RulesConverter.getRuleDomElementFromDbString(check);
            CRGRule rule = new CpxCRGRule(ruleMgr, e, null, null, CRGRule.RULE_ANALYSE_FULL);
            assertTrue(" The Rule has ugvd criterium, must be grouped", rule.isNeedGroupResults());
            check = testIcdInTableTerm.getBytes("UTF-16");
            e = RulesConverter.getRuleDomElementFromDbString(check);
            rule = new CpxCRGRule(ruleMgr, e, null, null, CRGRule.RULE_ANALYSE_FULL); // hier muss isServer = true gesetzt werden, sonst rule.getRuleElement() = null
            assertFalse(" The Rule has ICd criterium only, must not be grouped", rule.isNeedGroupResults());
            check = testTermWithBrackets.getBytes("UTF-16");
            e = RulesConverter.getRuleDomElementFromDbString(check);
            rule = new CpxCRGRule(ruleMgr, e, null, null, CRGRule.RULE_ANALYSE_FULL); // hier muss isServer = true gesetzt werden, sonst rule.getRuleElement() = null
            assertTrue(" The Rule has ugvd and DRG criterium, must be grouped", rule.isNeedGroupResults());
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error", ex);
        }

    }
}
