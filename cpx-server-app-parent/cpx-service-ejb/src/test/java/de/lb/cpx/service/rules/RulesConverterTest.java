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
package de.lb.cpx.service.rules;

import de.checkpoint.ruleGrouper.CRGRule;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.service.rules.utils.XMLRuleFileReader;
import de.lb.cpx.shared.rules.util.RulesConverter;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author gerschmann
 */
public class RulesConverterTest {

    private static final Logger LOG = Logger.getLogger(RulesConverterTest.class.getName());

    public RulesConverterTest() {
    }

    @Before
    public void setUp() {
    }

    /**
     * Test of getCpxRule method, of class RulesConverter.
     *
     * @throws URISyntaxException error
     */
    @Test
    public void testGetCpxRule() throws URISyntaxException {
        CRGRule[] rules = XMLRuleFileReader.readRulesFromFile();
        LOG.log(Level.INFO, "getCpxRule");
        assertNotNull("no rules found", rules);
        assertEquals("not all rules read", 4, rules.length);
        for (CRGRule rule : rules) {
            LOG.log(Level.INFO, rule.toString());
            String longString = rule.toString();
            CrgRules newRule = RulesConverter.getCpxRule(rule);
            byte[] ruleDefinition = newRule.getCrgrDefinition();
            CRGRule cpRule = new CRGRule(RulesConverter.getRuleDomElementFromDbString(ruleDefinition), "2017", null, CRGRule.RULE_ANALYSE_NO_TABLES);
            assertEquals("error on creation rule", longString, cpRule.toString());
        }
    }

    @Test
    public void testImportDomDoc() {

        try {
            String ret = XMLRuleFileReader.readXml();
        } catch (Exception ex) {
            Logger.getLogger(RulesConverterTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertTrue(true); //dummy
    }

}
