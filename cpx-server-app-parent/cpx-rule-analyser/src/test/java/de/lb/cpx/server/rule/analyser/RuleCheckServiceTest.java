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
package de.lb.cpx.server.rule.analyser;

import de.lb.cpx.grouper.model.transfer.Transfer4RuleAnalyse;
import de.lb.cpx.grouper.model.transfer.TransferCase;
import de.lb.cpx.grouper.model.transfer.TransferRuleAnalyseResult;
import de.lb.cpx.grouper.model.transfer.TransferRuleResult;
import de.lb.cpx.server.rule.analyser.utils.TransferCaseBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author gerschmann
 */
@Ignore
public class RuleCheckServiceTest {

    private static final Logger LOG = Logger.getLogger(RuleCheckServiceTest.class.getName());
//
    private final RuleCheckServiceLocal ruleCheckService = new RuleCheckServiceLocal();

//    public static final String testTermWithBrackets = 
//            "<rule caption=\"Alter in Jahren mit dem Operator &quot;plus&quot;\" entgelt=\"true\" errror_type=\"patient\" feegroup=\"1\" from=\"01.01.2013\" hasinterval=\"false\" interval_from=\"\" interval_to=\"\" massnumber=\"\" medtype=\"\" number=\"R.1.8.1.PL\" profit=\"0.0\" role=\"1,10,100,101,102,103,108,109,1000,1001,1002,1003,1007,1008,1009\" rules_notice=\"Alter des Patienten plus 44 Jahre muss mindestens 100 ergeben\" rules_number=\"2013_1009859_20070202051141\" rules_year=\"2013\" text=\"Patientenkriterien\" to=\"31.12.2013\" typ=\"error\" unchange=\"0\" used=\"true\" visible=\"\">\n" +
//"<rules_element nested=\"false\">\n" +
//"<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"AlterInJahren\" method=\"\" not=\"false\" operator=\"+\" parameter=\"\" wert=\"44\"/>\n" +
//"<rules_operator op_type=\"&amp;gt;=\"/>\n" +
//"<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"\" method=\"\" not=\"false\" operator=\"\" parameter=\"\" wert=\"100\"/>\n" +
//"<rules_operator op_type=\"&amp;&amp;\"/>\n" +
//"<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"Fallnummer\" method=\"\" not=\"false\" operator=\"==\" parameter=\"\" wert=\"'FR.1.8.1.PL'\"/>" +
//            "</rules_element>\n" +
//"<suggestions suggtext=\"\"/></rule>";
//    public static final String testTermWithBrackets
//            = "<rule>"
//            + "<rules_element >"
//            + "<rules_element nested=\"true\" >"
//            + "<rules_value kriterium=\"Verweildauer\" />"
//            + "<rules_operator op_type=\"&amp;lt;\" />"
//            + "<rules_value kriterium=\"untere_Grenzverweildauer\" />"
//            + "</rules_element>"
//            + "<rules_operator op_type=\"&amp;&amp;\" />"
//            + "<rules_value kriterium=\"DRG\" operator=\"==\" wert=\"'960Z'\" />"
//            + "<rules_operator op_type=\"||\" />"
//            + "<rules_value kriterium=\"Hauptdiagnose\" operator=\"@\"  wert=\"'ABC'\" />"
//            + "<rules_operator op_type=\"&amp;&amp;\" />"
//            + "<rules_value kriterium=\"Nebendiagnose\" operator=\"@\" wert=\"'NDX'\" />"
//            + "</rules_element>"
//            + "</rule>";
    public static final String testTermWithBrackets
            = "<rule caption=\"Aufnahmeanlass mit Operation &quot;in&quot;\" entgelt=\"true\" errror_type=\"Fall\" feegroup=\"1\" from=\"01.01.2013\" hasinterval=\"false\" interval_from=\"\" interval_to=\"\" massnumber=\"\" medtype=\"\" number=\"R1.3.1.IN\" profit=\"0.0\" role=\"1,10,100,101,102,103,108,109,1000,1001,1002,1003,1007,1008,1009\" rules_notice=\"\" rules_number=\"2013_1008967_20080120105311\" rules_year=\"2013\" text=\"Fall\" to=\"31.12.2013\" typ=\"error\" unchange=\"0\" used=\"true\" visible=\"\">\n"
            + "<rules_element nested=\"false\">"
            + "<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"Aufnahmeanlass\" method=\"\" not=\"false\" operator=\"IN\" parameter=\"\" wert=\"'V, K, A'\"/>\n"
            + "<rules_operator op_type=\"&amp;&amp;\"/>\n"
            + "<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"Fallnummer\" method=\"\" not=\"false\" operator=\"==\" parameter=\"\" wert=\"'FR.1.3.1.IN'\"/></rules_element>\n"
            + "<suggestions suggtext=\"\"/></rule>";
    public static final String testHistoryDrgNoInterval =
            "<rule caption=\"no interval\" entgelt=\"true\" errror_type=\"Sonstige\" feegroup=\"1\" from=\"01.01.2020\" hasinterval=\"false\" interval_from=\"\" interval_to=\"\" massnumber=\"0\" medtype=\"S\" number=\"\" profit=\"0.0\" rid=\"1002359\" role=\"1\" rules_notice=\"\" rules_number=\"2020_1002359_20191016152847\" rules_year=\"2020\" text=\"\" to=\"31.12.2020\" typ=\"warning\" unchange=\"0\" used=\"true\" visible=\"\">\n" +
            "<rules_element nested=\"false\">\n" +
            "<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"DRG\" method=\"\" not=\"false\" operator=\"\" parameter=\"\" wert=\"\"/>\n" +
            "<rules_operator op_type=\"==\"/>\n" +
            "<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"Klinik_DRG\" method=\"\" not=\"false\" operator=\"\" parameter=\"\" wert=\"\"/></rules_element>\n" +
            "<suggestions suggtext=\"\"/></rule>";
        public static final String testHistoryDrgMonthInterval =
                "<rule caption=\"Plus ein monat\" entgelt=\"true\" errror_type=\"Sonstige\" feegroup=\"1\" from=\"01.01.2020\" hasinterval=\"false\" interval_from=\"\" interval_to=\"\" massnumber=\"0\" medtype=\"S\" number=\"1\" profit=\"0.0\" rid=\"1002360\" role=\"1\" rules_notice=\"\" rules_number=\"2020_1002360_20191016152847\" rules_year=\"2020\" text=\"\" to=\"31.12.2020\" typ=\"warning\" unchange=\"0\" used=\"true\" visible=\"\">\n" +
                "<rules_element nested=\"false\">\n" +
                "<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"DRG\" method=\"\" not=\"false\" operator=\"\" parameter=\"\" wert=\"\"/>\n" +
                "<rules_operator op_type=\"==\"/>\n" +
                "<rules_value hasinterval=\"true\" interval_from=\"timeStamp2\" interval_to=\"months:1\" kriterium=\"Klinik_DRG\" method=\"\" not=\"false\" operator=\"\" parameter=\"\" wert=\"\"/></rules_element>\n" +
                "<suggestions suggtext=\"\"/></rule>";
        public static final String testHistoryDrgQuartalInterval =
                "<rule caption=\"Plus ein quartal\" entgelt=\"true\" errror_type=\"Sonstige\" feegroup=\"1\" from=\"01.01.2020\" hasinterval=\"false\" interval_from=\"\" interval_to=\"\" massnumber=\"0\" medtype=\"S\" number=\"2\" profit=\"0.0\" rid=\"1002361\" role=\"1\" rules_notice=\"\" rules_number=\"2020_1002361_20191016152847\" rules_year=\"2020\" text=\"\" to=\"31.12.2020\" typ=\"warning\" unchange=\"0\" used=\"true\" visible=\"\">\n" +
                "<rules_element nested=\"false\">\n" +
                "<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"DRG\" method=\"\" not=\"false\" operator=\"\" parameter=\"\" wert=\"\"/>\n" +
                "<rules_operator op_type=\"==\"/>\n" +
                "<rules_value hasinterval=\"true\" interval_from=\"timeStamp2\" interval_to=\"quater:1\" kriterium=\"Klinik_DRG\" method=\"\" not=\"false\" operator=\"\" parameter=\"\" wert=\"\"/></rules_element>\n" +
                "<suggestions suggtext=\"\"/></rule>";
    public static String[] tableABC = {"'ABC'", "S%, M%"};
    public static String[] tableNDX = {"'NDX'", "R%, A%"};
    public static Map<String, Boolean> testTermWithBrackets2case0 = new HashMap<>() {
        {
            put("0", true);
            put("1", true);
            put("2", false);
            put("4", false);
            put("6", false);
            put("7", false);
            put("8", true);
            put("9", true);
            put("10", true);
            put("11", true);

        }
    };

    public RuleCheckServiceTest() {
    }

    @Before
    public void setUp() {
        ruleCheckService.init();
    }

    @Test
    public void checkRule4CaseTest() throws Exception {
        List<TransferCase> cases = TransferCaseBuilder.getInstance().readDrgCases();
        if (cases != null && !cases.isEmpty()) {
            TransferCase testCase = cases.get(0);
            testCase.setAgeY(70);
            Transfer4RuleAnalyse testRequest = new Transfer4RuleAnalyse(testCase, testTermWithBrackets);
            testRequest.addRuleTable(tableABC[0], tableABC[1]);
            testRequest.addRuleTable(tableNDX[0], tableNDX[1]);
            TransferRuleAnalyseResult result = ruleCheckService.checkRule4Case(testRequest);
            assertNotNull("result is null", result);
            LOG.log(Level.INFO, result.toString());
            assertTrue("Ergebnis der Regel: " + result.getRuleResult().toString(), checkTransferResult(result.getRuleResult(), testTermWithBrackets2case0));
        }
    }

    public static boolean checkTransferResult(TransferRuleResult result, Map<String, Boolean> testTermResult) {
        if (testTermResult == null || testTermResult.isEmpty()) {
            return false;
        }
        if (result.getMark() != null && !result.getMark().isEmpty()) {
            if (testTermResult.get(result.getMark()) == null) {
                return false;
            } else {
                if (testTermResult.get(result.getMark()) != result.isResult()) {
                    return false;
                } else {
                    List<TransferRuleResult> children = result.getChildren();
                    if (children == null || children.isEmpty()) {
                        return true;
                    } else {
                        for (TransferRuleResult child : children) {
                            if (!checkTransferResult(child, testTermResult)) {
                                return false;
                            }
                        }
                        return true;
                    }
                }
            }
        }
        return true;
    }
    
    @Test
    public void checkRule4HistoryTest() throws Exception{
        List<TransferCase> cases = TransferCaseBuilder.getInstance().readDrgHistoryCases();
        TransferCase main = cases.remove(0);
        Transfer4RuleAnalyse objToAnalyse = new Transfer4RuleAnalyse(main, testHistoryDrgNoInterval, new HashMap<>(), new ArrayList<>(), cases);
        TransferRuleAnalyseResult res = ruleCheckService.checkRule4Case(objToAnalyse);
        if(res != null && res.getRuleResult() != null){
            String resRule = res.getRuleResult().getReferences();
            LOG.info(resRule);
            assertTrue("Ergebnis der Regel: " + res.getRuleResult().toString(), res.getRuleResult().isResult());
        }
 // check month interval
        objToAnalyse = new Transfer4RuleAnalyse(main, testHistoryDrgMonthInterval, new HashMap<>(), new ArrayList<>(), cases);
        res = ruleCheckService.checkRule4Case(objToAnalyse);
        if(res != null && res.getRuleResult() != null){
            String resRule = res.getRuleResult().getReferences();
            LOG.info(resRule);
            assertFalse("Ergebnis der Regel monal Intervall: " + res.getRuleResult().toString(), !res.getRuleResult().isResult());
        }
 // check quartal interval
        objToAnalyse = new Transfer4RuleAnalyse(main, testHistoryDrgQuartalInterval, new HashMap<>(), new ArrayList<>(), cases);
        res = ruleCheckService.checkRule4Case(objToAnalyse);
        if(res != null && res.getRuleResult() != null){
            String resRule = res.getRuleResult().getReferences();
            LOG.info(resRule);
            assertTrue("Ergebnis der Regel quartal Intervall: " + res.getRuleResult().toString(), res.getRuleResult().isResult());
        }
    }
    

}
