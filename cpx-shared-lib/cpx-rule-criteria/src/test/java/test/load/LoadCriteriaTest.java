/* 
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package test.load;

import de.lb.cpx.rule.criteria.CriteriaManager;
import de.lb.cpx.rule.criteria.TypesAndOperationsManager;
import de.lb.cpx.rule.criteria.model.Criteria;
import de.lb.cpx.rule.criteria.model.CriterionTree;
import de.lb.cpx.rule.criteria.model.CriterionTree.Supergroup.Group.Criterion;
import de.lb.cpx.rule.criteria.model.TypesAndOperations;
import de.lb.cpx.rule.element.CaseRuleManager;
import de.lb.cpx.rule.element.model.Rule;
import de.lb.cpx.rule.element.model.RulesValue;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author wilde
 */
public class LoadCriteriaTest {

    private static CriteriaManager criteriaInstance;
    private TypesAndOperationsManager typesAndOperationsInstance;

    private static final String TEST_RULE_1
            = "<?xml version=\"1.0\" encoding=\"UTF-16\"?>"
            + //        "<caseRules>" +
            "<rule caption=\"DKR 1107: Dehydratation bei Gastroenteritis\" entgelt=\"true\" errror_type=\"DKR\" feegroup=\"1\" from=\"01.01.2018\" hasinterval=\"false\" interval_from=\"\" interval_to=\"\" massnumber=\"\" medtype=\"\" number=\"001 \" profit=\"0.0\" rid=\"1002053\" role=\"1\" rules_notice=\"DKR 1107 Dehydratation bei Gastroenteritis&amp;#013&amp;#013Bei stationärer Aufnahme zur Behandlung einer Gastroenteritis mit Dehydratation wird die Gastroenteritis als Hauptdiagnose und „Dehydratation” (E86 Volumenmangel) als Nebendiagnose&amp;#013angegeben.&amp;#013&amp;#013&amp;#013&amp;#013\" rules_number=\"2018_1002635_20129999000000\" rules_year=\"2018\" text=\"DKR 1107\" to=\"31.12.2018\" typ=\"error\" unchange=\"0\" used=\"true\" visible=\"\">"
            + "<rules_element nested=\"false\">"
            + "<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"case_number\" method=\"\" not=\"false\" operator=\"==\" parameter=\"\" wert=\"'E86'\"/>"
            + "<rules_operator op_type=\"&amp;&amp;\"/>"
            + "<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"insurance_number\" method=\"\" not=\"false\" operator=\"@\" parameter=\"\" wert=\"'GASTROENTERITIS'\"/>"
            + "</rules_element>"
            + "<suggestions/>"
/*            + "<suggestions suggtext=\"Gastroenteritis ist die korrekte Hauptdiagnose (+HD A09)\">"
            + "<sugg actionid=\"2\" condition_op=\"\" condition_value=\"\" crit=\"Hauptdiagnose\" op=\"==\" value=\"A09.0\"/>"
            + "</suggestions>"*/
            + "</rule>";
//            "<rule caption=\"Neurologische Komplexbehandlung, VWD &amp;lt;73h, aber &amp;gt;23h\" entgelt=\"true\" errror_type=\"OPS\" feegroup=\"1\" from=\"01.01.2018\" hasinterval=\"false\" interval_from=\"\" interval_to=\"\" massnumber=\"\" medtype=\"\" number=\"004a\" profit=\"0.0\" rid=\"1002057\" role=\"1\" rules_notice=\"\" rules_number=\"2018_1002639_20129999000000\" rules_year=\"2018\" text=\"OPS\" to=\"31.12.2018\" typ=\"error\" unchange=\"0\" used=\"true\" visible=\"\">\n" +
//"	<rules_element nested=\"false\">\n" +
//"		<rules_element nested=\"true\">\n" +
//"			<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"Prozedur\" method=\"\" not=\"false\" operator=\"==\" parameter=\"\" wert=\"'8-981.1'\"/>\n" +
//"			<rules_operator op_type=\"||\"/>\n" +
//"			<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"Prozedur\" method=\"\" not=\"false\" operator=\"==\" parameter=\"\" wert=\"'8-98B.1%'\"/>\n" +
//"		</rules_element>\n" +
//"		<rules_operator op_type=\"&amp;&amp;\"/>\n" +
//"		<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"VerweildauerInStunden\" method=\"\" not=\"false\" operator=\"&amp;lt;\" parameter=\"\" wert=\"73\"/>\n" +
//"		<rules_operator op_type=\"&amp;&amp;\"/>\n" +
//"		<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"VerweildauerInStunden\" method=\"\" not=\"false\" operator=\"&amp;gt;\" parameter=\"\" wert=\"23\"/>\n" +
//"	</rules_element>\n" +
//"	<suggestions suggtext=\"Neurologische Komplexbehandlungszeiten passen nicht zur VWD.\">\n" +
//"		<sugg actionid=\"2\" condition_op=\"==\" condition_value=\"8-981.1\" crit=\"Prozedur\" op=\"\" value=\"8-981.0\"/>\n" +
//"		<sugg actionid=\"2\" condition_op=\"==\" condition_value=\"8-98B.10\" crit=\"Prozedur\" op=\"==\" value=\"8-98B.00\"/>\n" +
//"		<sugg actionid=\"2\" condition_op=\"==\" condition_value=\"8-98B.11\" crit=\"Prozedur\" op=\"==\" value=\"8-98B.01\"/>\n" +
//"	</suggestions>\n" +
//"</rule>"+
//        "</caseRules>"
//        ;
    private static final String TEST_RULE_WITH_RISK = "<?xml version=\"1.0\" encoding=\"UTF-16\"?>"
            + "<rule caption=\"\" entgelt=\"true\" errror_type=\"falluebrgr\" feegroup=\"1\" from=\"01.01.2019\" hasinterval=\"false\" interval_from=\"\" interval_to=\"\" massnumber=\"0\" medtype=\"S\" number=\"adrg\" profit=\"0.0\" role=\"1\" rules_notice=\"&amp;lt;&amp;gt;\" rules_number=\"user1_15075_1577457773907\" rules_year=\"0\" text=\"\" to=\"31.12.2019\" typ=\"warning\" unchange=\"0\" used=\"true\" visible=\"\">"
            + "<rules_element nested=\"false\">"
            + "<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"ADRG\" method=\"\" not=\"false\" operator=\"@\" parameter=\"\" wert=\"'ABORT_TOTGEBURT'\"/>"
            + "<rules_operator op_type=\"&amp;&amp;\"/>"
            + "<rules_value hasinterval=\"true\" interval_from=\"timeStamp2\" interval_to=\"days:3\" kriterium=\"Klinik_ADRG\" method=\"\" not=\"false\" operator=\"==\" parameter=\"\" wert=\"O6%\"/></rules_element>"
            + "<suggestions suggtext=\"\"/>"
            + "<risk>"
            + "<risk_area risk_area_name=\"DRG_PEPP\" risk_percent_value=\"20\" risk_default_waist_value=\"200\" risk_comment=\"erä gi 4ü ü45ptz9 e#r+ütop \"/>"
            + "<risk_area risk_area_name=\"MD\" risk_percent_value=\"20\" risk_default_waist_value=\"200\" risk_comment=\"erä gi 4ü ü45ptz9 e#r+ütop \"/>"
            + "<risk_area risk_area_name=\"SD\" risk_percent_value=\"20\" risk_default_waist_value=\"200\" risk_comment=\"erä gi 4ü ü45ptz9 e#r+ütop \"/>"
            + "<risk_area risk_area_name=\"PROCS\" risk_percent_value=\"20\" risk_default_waist_value=\"200\" risk_comment=\"erä gi 4ü ü45ptz9 e#r+ütop \"/>"
            + "<risk_area risk_area_name=\"PRI_MISALO\" risk_percent_value=\"20\" risk_default_waist_value=\"200\" risk_comment=\"erä gi 4ü ü45ptz9 e#r+ütop \"/>"
            + "<risk_area risk_area_name=\"SEC_MISALO\" risk_percent_value=\"20\" risk_default_waist_value=\"200\" risk_comment=\"erä gi 4ü ü45ptz9 e#r+ütop \"/>"
            + "<risk_area risk_area_name=\"CASE_MERGE\" risk_percent_value=\"20\" risk_default_waist_value=\"200\" risk_comment=\"erä gi 4ü ü45ptz9 e#r+ütop \"/>"
            + "<risk_area risk_area_name=\"OT_FEE_SUR\" risk_percent_value=\"20\" risk_default_waist_value=\"200\" risk_comment=\"erä gi 4ü ü45ptz9 e#r+ütop \"/>"
            + "<risk_area risk_area_name=\"OTHER\" risk_percent_value=\"20\" risk_default_waist_value=\"200\" risk_comment=\"erä gi 4ü ü45ptz9 e#r+ütop \"/>"
            + "</risk>"
            + "</rule>";
    
    
    private static final String STRING_TEST_RULE = TEST_RULE_1;
    private static final byte[] BYTES_TEST_RULE = STRING_TEST_RULE.getBytes();

    public LoadCriteriaTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        criteriaInstance = CriteriaManager.instance();
        typesAndOperationsInstance = TypesAndOperationsManager.instance();
    }

    @After
    public void tearDown() {
        CriteriaManager.destroy();
        criteriaInstance = null;
        typesAndOperationsInstance.destroy();
        typesAndOperationsInstance = null;
    }

    @Test
    public void testLoad() {
        long start = System.currentTimeMillis();
//        Criteria crit = instance.getBasicCriteria();
 /*       Criteria acgCrit = criteriaInstance.getAcgCriteria();
        LOG.log(Level.INFO, "time to unmarshal acgCriteria: {0} ms", System.currentTimeMillis() - start);
        assertTrue(acgCrit != null);
        String firstAcgGroupName = acgCrit.getSupergroup().getGroup().get(0).getCpname();
        assertEquals("Stammdaten", firstAcgGroupName);
        start = System.currentTimeMillis();

        Criteria ambuCrit = criteriaInstance.getAmbuCriteria();
        LOG.log(Level.INFO, "time to unmarshal ambuCriteria: {0} ms", System.currentTimeMillis() - start);
        assertTrue(ambuCrit != null);
        String firstAmbuGroupName = ambuCrit.getSupergroup().getGroup().get(0).getCpname();
        assertEquals("Heilmittel", firstAmbuGroupName);
        start = System.currentTimeMillis();

        Criteria hospitalCrit = criteriaInstance.getHospitalCriteria();
        LOG.log(Level.INFO, "time to unmarshal caseCriteria: {0} ms", System.currentTimeMillis() - start);
        assertTrue(hospitalCrit != null);
        String firstCaseGroupName = hospitalCrit.getSupergroup().getGroup().get(0).getCpname();
        assertEquals("Kodierung", firstCaseGroupName);
        start = System.currentTimeMillis();

        Criteria caseCrit = criteriaInstance.getCaseCriteria();
        LOG.log(Level.INFO, "time to unmarshal hoscCriteria: {0} ms", System.currentTimeMillis() - start);
        assertTrue(caseCrit != null);
        assertTrue(caseCrit.getSupergroup() != null);
        assertTrue(!caseCrit.getSupergroup().getGroup().isEmpty());
//        String firstHoscGroupName = caseCrit.getSupergroup().getGroup().get(0).getCpname();
//        assertEquals("Bewegungen", firstHoscGroupName);
        start = System.currentTimeMillis();

        Criteria gkrsaCrit = criteriaInstance.getGkrsaCriteria();
        LOG.log(Level.INFO, "time to unmarshal insuCriteria: {0} ms", System.currentTimeMillis() - start);
        assertTrue(gkrsaCrit != null);
        String firstInsuGroupName = gkrsaCrit.getSupergroup().getGroup().get(0).getCpname();
        assertEquals("Arzneimittel", firstInsuGroupName);
        start = System.currentTimeMillis();

        Criteria patientCrit = criteriaInstance.getPatientCriteria();
        LOG.log(Level.INFO, "time to unmarshal patientCriteria: {0} ms", System.currentTimeMillis() - start);
        assertTrue(patientCrit != null);
        String firstPatGroupName = patientCrit.getSupergroup().getGroup().get(0).getCpname();
        assertEquals("Versicherten Stammdaten", firstPatGroupName);
        start = System.currentTimeMillis();

        Criteria vpsCrit = criteriaInstance.getVpsCriteria();
        LOG.log(Level.INFO, "time to unmarshal vpsCriteria: {0} ms", System.currentTimeMillis() - start);
        assertTrue(vpsCrit != null);
        String firstGroupName = vpsCrit.getSupergroup().getGroup().get(0).getCpname();
        assertEquals("VPS Kriterien", firstGroupName);
        start = System.currentTimeMillis();
*/
    Criteria usedCrit = criteriaInstance.getUsedCriteria();
        LOG.log(Level.INFO, "time to unmarshal vpsCriteria: {0} ms", System.currentTimeMillis() - start);
        assertTrue(usedCrit != null);
        String firstGroupName = usedCrit.getSupergroup().getGroup().get(0).getCpname();
        assertEquals("patient", firstGroupName);
        start = System.currentTimeMillis();
        
        TypesAndOperations types = typesAndOperationsInstance.getTypesAndOperations();
        LOG.log(Level.INFO, "time to unmarshal typesAndOperations: {0} ms", System.currentTimeMillis() - start);
        assertTrue(types != null);
        start = System.currentTimeMillis();

        TypesAndOperations.OperationGroups.OperationGroup group = typesAndOperationsInstance.getOperationGroupByType("DATATYPE_ARRAY_DATE");
        LOG.log(Level.INFO, "time to find operationsGroup by type: {0} ms", System.currentTimeMillis() - start);
        assertNotNull(group);
        assertEquals(7, group.getOperation().size());
        start = System.currentTimeMillis();

        CriterionTree.Supergroup.Group.Criterion criterion = usedCrit.getSupergroup().getGroup().get(1).getCriterion().get(0);
        assertEquals("DATATYPE_STRING", criterion.getCriterionType());
        TypesAndOperations.OperationGroups.OperationGroup groupString = typesAndOperationsInstance.getOperationGroupByType(criterion.getCriterionType());
        assertEquals(7, groupString.getOperation().size());

        String gt = "&amp;gt;";
        TypesAndOperations.OperationGroups.OperationGroup.Operation op = new TypesAndOperations.OperationGroups.OperationGroup.Operation();
        op.setName(gt);
        assertEquals(">", op.getFormatedName());

//        List<CriterionTree.OperationGroups.OperationGroup.Operation> operations = crit.getOperations("opList_numeric");
//        LOG.log(Level.INFO, "time to load operations: {0} ms", System.currentTimeMillis() - start);
//        assertNotEquals(operations.size(), 0);
//        assertEquals(operations.size(), 15);
//        start = System.currentTimeMillis();
    }

    @Test
    public void testUnmarshal() {
        long start = System.currentTimeMillis();
        Rule ruleFromString = CaseRuleManager.transfromRule(STRING_TEST_RULE);
        LOG.info("Time to Unmarshal from String: " + (System.currentTimeMillis() - start) + " ms");
        Assert.assertNotNull(ruleFromString);
//        LOG.info(CaseRuleManager.getDisplayText(ruleFromString.getRule().get(0).getRulesNotice()));
        start = System.currentTimeMillis();

        Rule ruleFromBytes = null;
        try {
            ruleFromBytes = CaseRuleManager.transfromRule(BYTES_TEST_RULE);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(LoadCriteriaTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        LOG.info("Time to Unmarshal from Bytes: " + (System.currentTimeMillis() - start) + " ms");
        Assert.assertNotNull(ruleFromBytes);
//        LOG.info(CaseRuleManager.getDisplayText(ruleFromBytes.getRulesElementOrRule().get(0).getRulesNotice()));
        start = System.currentTimeMillis();

        RulesValue value = (RulesValue) ruleFromBytes.getRulesElement().getRulesValueOrRulesOperatorOrRulesElement().get(0);
        Criterion crit = criteriaInstance.findFirstCriterion(value.getKriterium());
        LOG.info("Time to fetch Criterion from rule: " + (System.currentTimeMillis() - start) + " ms");
        Assert.assertNotNull(crit);

        String ruleString = CaseRuleManager.transformObject(ruleFromBytes);
        LOG.info("Time to marshal from Bytes: " + (System.currentTimeMillis() - start) + " ms");
        Assert.assertNotNull(ruleFromString);
        LOG.info("input rule:\n" + STRING_TEST_RULE);
//        ruleString = ruleString.replaceAll("UTF-8", "UTF-16");
//        ruleString = ruleString.replaceAll(" standalone=\"yes\"", "");
        LOG.info("output rule:\n" + ruleString);
        LOG.info("\n");
        Assert.assertEquals(STRING_TEST_RULE, ruleString);
//        LOG.info(CaseRuleManager.getDisplayText(ruleFromBytes.getRule().get(0).getRulesNotice()));
        start = System.currentTimeMillis();

    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    private static final Logger LOG = Logger.getLogger(LoadCriteriaTest.class.getName());
}
