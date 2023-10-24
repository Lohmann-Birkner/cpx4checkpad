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
package de.lb.cpx.ruleviewer.util;

import de.lb.cpx.model.enums.RuleTypeEn;
import de.lb.cpx.rule.element.CaseRuleManager;
import de.lb.cpx.server.commonDB.model.CdbUserRoles;
import de.lb.cpx.server.commonDB.model.rules.CrgRule2Role;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Test Rule to have a set of default data to layout
 *
 * @author wilde
 */
public class TestRule extends CrgRules {

    private static final String TEST_RULE_1
            = "<?xml version=\"1.0\" encoding=\"UTF-16\"?>"
            + //            "<rule caption=\"DKR 1107: Dehydratation bei Gastroenteritis\" entgelt=\"true\" errror_type=\"DKR\" feegroup=\"1\" from=\"01.01.2018\" hasinterval=\"false\" interval_from=\"\" interval_to=\"\" massnumber=\"\" medtype=\"\" number=\"001 \" profit=\"0.0\" rid=\"1002053\" role=\"1\" rules_notice=\"DKR 1107 Dehydratation bei Gastroenteritis&amp;#013&amp;#013Bei stationärer Aufnahme zur Behandlung einer Gastroenteritis mit Dehydratation wird die Gastroenteritis als Hauptdiagnose und „Dehydratation” (E86 Volumenmangel) als Nebendiagnose&amp;#013angegeben.&amp;#013&amp;#013&amp;#013&amp;#013\" rules_number=\"2018_1002635_20129999000000\" rules_year=\"2018\" text=\"DKR 1107\" to=\"31.12.2018\" typ=\"error\" unchange=\"0\" used=\"true\" visible=\"\">" +
            //                "<rules_element nested=\"false\">" +
            //                    "<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"Hauptdiagnose\" method=\"\" not=\"false\" operator=\"==\" parameter=\"\" wert=\"'E86'\"/>" +
            //                    "<rules_operator op_type=\"&amp;&amp;\"/>" +
            //                    "<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"Nebendiagnose\" method=\"\" not=\"false\" operator=\"@\" parameter=\"\" wert=\"'GASTROENTERITIS'\"/>" +
            //                "</rules_element>" +
            //                "<suggestions suggtext=\"Gastroenteritis ist die korrekte Hauptdiagnose (+HD A09)\">" +
            //                    "<sugg actionid=\"2\" condition_op=\"\" condition_value=\"\" crit=\"Hauptdiagnose\" op=\"==\" value=\"A09.0\"/>" +
            //                "</suggestions>" +
            //            "</rule>"+
            "<rule caption=\"Neurologische Komplexbehandlung, VWD &amp;lt;73h, aber &amp;gt;23h\" entgelt=\"true\" errror_type=\"OPS\" feegroup=\"1\" from=\"01.01.2018\" hasinterval=\"false\" interval_from=\"\" interval_to=\"\" massnumber=\"\" medtype=\"\" number=\"004a\" profit=\"0.0\" rid=\"1002057\" role=\"1\" rules_notice=\"\" rules_number=\"2018_1002639_20129999000000\" rules_year=\"2018\" text=\"OPS\" to=\"31.12.2018\" typ=\"error\" unchange=\"0\" used=\"true\" visible=\"\">\n"
            + "	<rules_element nested=\"false\">\n"
            + "		<rules_element nested=\"true\">\n"
            + "			<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"Prozedur\" method=\"\" not=\"false\" operator=\"==\" parameter=\"\" wert=\"'8-981.1'\"/>\n"
            + "			<rules_operator op_type=\"||\"/>\n"
            + "			<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"Prozedur\" method=\"\" not=\"false\" operator=\"==\" parameter=\"\" wert=\"'8-98B.1%'\"/>\n"
            + "		</rules_element>\n"
            + "		<rules_operator op_type=\"&amp;&amp;\"/>\n"
            + "		<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"VerweildauerInStunden\" method=\"\" not=\"false\" operator=\"&amp;lt;\" parameter=\"\" wert=\"73\"/>\n"
            + "		<rules_operator op_type=\"&amp;&amp;\"/>\n"
            + "		<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"VerweildauerInStunden\" method=\"\" not=\"false\" operator=\"&amp;gt;=\" parameter=\"\" wert=\"23\"/>\n"
            + "	</rules_element>\n"
            + "	<suggestions suggtext=\"Neurologische Komplexbehandlungszeiten passen nicht zur VWD.\">\n"
            + "		<sugg actionid=\"2\" condition_op=\"==\" condition_value=\"8-981.1\" crit=\"Prozedur\" op=\"==\" value=\"8-981.0\"/>\n"
            + "		<sugg actionid=\"2\" condition_op=\"==\" condition_value=\"8-98B.10\" crit=\"Prozedur\" op=\"==\" value=\"8-98B.00\"/>\n"
            + "		<sugg actionid=\"2\" condition_op=\"==\" condition_value=\"8-98B.11\" crit=\"Prozedur\" op=\"==\" value=\"8-98B.01\"/>\n"
            + "	</suggestions>\n"
            + "</rule>"
            + //        "</caseRules>"
            "";
    private static final String TEST_RULE_2 = "<rule caption=\"PAVK oder Gangrän und Diabetes mellitus\" entgelt=\"false\" errror_type=\"DKR\" feegroup=\"1\" from=\"01.01.2017\" hasinterval=\"false\" interval_from=\"\" interval_to=\"\" massnumber=\"\" medtype=\"\" number=\"040 \" profit=\"0.0\" rid=\"1000048\" role=\"1\" rules_notice=\"\" rules_number=\"2017_1005193_20129999000000\" rules_year=\"2017\" text=\"DKR 0401\" to=\"31.12.2017\" typ=\"warning\" unchange=\"0\" used=\"true\" visible=\"\">\n"
            + "<rules_element nested=\"false\">\n"
            + "<rules_element nested=\"true\">\n"
            + "<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"Hauptdiagnose\" method=\"\" not=\"false\" operator=\"==\" parameter=\"\" wert=\"'R02'\"/>\n"
            + "<rules_operator op_type=\"||\"/>\n"
            + "<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"Hauptdiagnose\" method=\"\" not=\"false\" operator=\"==\" parameter=\"\" wert=\"'I70.2%'\"/>"
            + "</rules_element>\n"
            + "<rules_operator op_type=\"&amp;&amp;\"/>\n"
            + "<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"Nebendiagnose\" method=\"\" not=\"false\" operator=\"@\" parameter=\"\" wert=\"'DIABETES'\"/>"
            + "</rules_element>\n"
            + "<suggestions suggtext=\"Gangrän / pAVK durch  Diabetes mellitus? Dann bitte mit E10- E14, vierte + fünfte Stelle *50 oder *51 als HD verschlüsseln (+HD E11.50, z.B. +I70.24, -R02).\">\n"
            + "<sugg actionid=\"2\" condition_op=\"\" condition_value=\"\" crit=\"Hauptdiagnose\" op=\"==\" value=\"E11.50\"/>\n"
            + "<sugg actionid=\"1\" condition_op=\"\" condition_value=\"\" crit=\"Nebendiagnose\" op=\"==\" value=\"I79.2\"/>\n"
            + "<sugg actionid=\"1\" condition_op=\"\" condition_value=\"\" crit=\"Nebendiagnose\" op=\"==\" value=\"I70.24\"/>\n"
            + "<sugg actionid=\"2\" condition_op=\"\" condition_value=\"\" crit=\"Hauptdiagnose\" op=\"==\" value=\"E11.50\"/></suggestions></rule>";

    private static final String TEST_RULE_3 = "<rule caption=\"Es wurde ein Kode aus dem Geltungsbereich § 17b für den Qualifizierten Entzug verschlüsselt. \" entgelt=\"true\" errror_type=\"OPS\" feegroup=\"2\" from=\"01.01.2017\" hasinterval=\"false\" interval_from=\"\" interval_to=\"\" massnumber=\"\" medtype=\"\" number=\"PEPP035a\" profit=\"0.0\" rid=\"1000479\" role=\"1\" rules_notice=\"Ein Kode aus diesen Bereichen ist nur für Leistungen anzugeben, die in Einrichtungen im Geltungsbereich des § 17b KHG erbracht wurden. Ein Kode aus diesen Bereichen darf demnach nicht in Fachabteilungen der Psychiatrie, Psychosomatik oder KJP verschlüsselt werden.&amp;#013&amp;#013Für den Qualifizierten Entzug in der Psychiatrie ist ein Kode aus 9-647* zu verwenden.&amp;#013&amp;#013Ersatz nach folgenden Endsteller-Entsprechungen:&amp;#013&amp;#0138-985 | Tage        | 9-647  | Tage&amp;#013_____|_________|______|_____&amp;#013.0       |  bis 6        | .5        | 6&amp;#013.1       |  bis 13      | .c        | 13&amp;#013.2       | bis 20       | .k        | 20&amp;#013.3       | &amp;gt;= 21      | .u        | 28\" rules_number=\"2017_1005634_20121026025738\" rules_year=\"2017\" text=\"OPS\" to=\"31.12.2017\" typ=\"error\" unchange=\"0\" used=\"true\" visible=\"\">\n"
            + "<rules_element nested=\"false\">\n"
            + "<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"Prozedur\" method=\"\" not=\"false\" operator=\"==\" parameter=\"\" wert=\"'8-985%'\"/>"
            + "</rules_element>\n"
            + "<suggestions suggtext=\"Kodieren Sie die Leistung mit Kodes für Einrichtungen im Geltungsbereich des § 17d KHG.\">\n"
            + "<sugg actionid=\"2\" condition_op=\"==\" condition_value=\"8-985.0\" crit=\"Prozedur\" op=\"==\" value=\"9-647.5\"/>\n"
            + "<sugg actionid=\"2\" condition_op=\"==\" condition_value=\"8-985.1\" crit=\"Prozedur\" op=\"==\" value=\"9-647.C\"/>\n"
            + "<sugg actionid=\"2\" condition_op=\"==\" condition_value=\"8-985.2\" crit=\"Prozedur\" op=\"==\" value=\"9-647.K\"/>\n"
            + "<sugg actionid=\"2\" condition_op=\"==\" condition_value=\"8-985.3\" crit=\"Prozedur\" op=\"==\" value=\"9-647.U\"/></suggestions></rule>";

    private static final String TEST_RULE_4 = "<rule caption=\"Es wurde ein Kode aus dem Geltungsbereich § 17b für den Qualifizierten Entzug verschlüsselt. \" entgelt=\"true\" errror_type=\"OPS\" feegroup=\"2\" from=\"01.01.2017\" hasinterval=\"false\" interval_from=\"\" interval_to=\"\" massnumber=\"\" medtype=\"\" number=\"PEPP035a\" profit=\"0.0\" rid=\"1000479\" role=\"1\" rules_notice=\"Ein Kode aus diesen Bereichen ist nur für Leistungen anzugeben, die in Einrichtungen im Geltungsbereich des § 17b KHG erbracht wurden. Ein Kode aus diesen Bereichen darf demnach nicht in Fachabteilungen der Psychiatrie, Psychosomatik oder KJP verschlüsselt werden.&amp;#013&amp;#013Für den Qualifizierten Entzug in der Psychiatrie ist ein Kode aus 9-647* zu verwenden.&amp;#013&amp;#013Ersatz nach folgenden Endsteller-Entsprechungen:&amp;#013&amp;#0138-985 | Tage        | 9-647  | Tage&amp;#013_____|_________|______|_____&amp;#013.0       |  bis 6        | .5        | 6&amp;#013.1       |  bis 13      | .c        | 13&amp;#013.2       | bis 20       | .k        | 20&amp;#013.3       | &amp;gt;= 21      | .u        | 28\" rules_number=\"2017_1005634_20121026025738\" rules_year=\"2017\" text=\"OPS\" to=\"31.12.2017\" typ=\"error\" unchange=\"0\" used=\"true\" visible=\"\">\n"
            + "<rules_element nested=\"false\">\n"
            + "<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"Aufnahmegrund2\" method=\"\" not=\"false\" operator=\"==\" parameter=\"\" wert=\"22\"/>"
            + "</rules_element>\n"
            + "<suggestions suggtext=\"Kodieren Sie die Leistung mit Kodes für Einrichtungen im Geltungsbereich des § 17d KHG.\">\n"
            + "<sugg actionid=\"2\" condition_op=\"==\" condition_value=\"8-985.0\" crit=\"Prozedur\" op=\"==\" value=\"9-647.5\"/>\n"
            + "<sugg actionid=\"2\" condition_op=\"==\" condition_value=\"8-985.1\" crit=\"Prozedur\" op=\"==\" value=\"9-647.C\"/>\n"
            + "<sugg actionid=\"2\" condition_op=\"==\" condition_value=\"8-985.2\" crit=\"Prozedur\" op=\"==\" value=\"9-647.K\"/>\n"
            + "<sugg actionid=\"2\" condition_op=\"==\" condition_value=\"8-985.3\" crit=\"Prozedur\" op=\"==\" value=\"9-647.U\"/></suggestions></rule>";
    private static final long serialVersionUID = 1L;

    public TestRule() throws UnsupportedEncodingException {
        super();
        String rule = TEST_RULE_2;
        setCrgrCaption("Testbezeichnung");
        setCrgrCategory("Testkategorie");
        setCrgrDefinition(rule.getBytes("UTF-16"));
        setCrgrIdentifier("TestIdent");
        setCrgrNote("Kodierempfehlung Nr. 9&amp;#013    &amp;#013Schlagwort:  Diabetes, entgleist&amp;#013Stand:  &amp;#013Aktualisiert:  15.01.2015&amp;#013DRG:    &amp;#013ICD:    &amp;#013OPS:    &amp;#013&amp;#013Problem/Erläuterung:&amp;#013Eine allgemein gültige Definition des entgleisten Diabetes mellitus existiert in der Fachliteratur nicht. Es besteht die Notwendigkeit Kriterien zu entwickeln, die eine Differenzierung erlauben.&amp;#013&amp;#013Kodierempfehlung:&amp;#013Ein Diabetes mellitus gilt dann als entgleist, wenn mindestens einer der folgenden Punkte erfüllt ist:&amp;#013• Rezidivierende (an mehreren Tagen) Hypoglykämien unter 50 mg/dl (2,775 mmol/l) mit Symptomen mit mindestens dreimal täglichen BZ-Kontrollen und Therapieanpassung&amp;#013• Stark schwankende BZ-Werte (Unterschied mindestens 100 mg/dl (5,55 mmol/l)) mit mindestens dreimal täglichen BZ-Kontrollen und Therapieanpassung&amp;#013• Deutlich erhöhtes HbA1c (größer als 10 % bzw. 85 mmol/mol Hb) als Parameter der Stoffwechselsituation während der letzten 3 Monate mit entsprechender therapeutischer Würdigung während des stationären Aufenthaltes (mindestens 3x tägliche Kontrolle und Therapieanpassung)&amp;#013• Mindestens dreimal Werte &amp;gt;300 mg/dl (16,65 mmol/l) mit mehrfacher Therapieanpassung&amp;#013• Bei Werten unter 300 mg/dl (16,65 mmol/l): aufwändiges Management mit an mehreren Tagen mehr als dreimal tgl. Kontrollen und dokumentiertem Nachspritzen von Altinsulin oder kurzwirksamen Insulinanaloga&amp;#013Diese Empfehlung gilt nicht beim Therapiemanagement nach dem Basis-Bolus-Prinzip.");
        setCrgrNote(CaseRuleManager.getDisplayText(getCrgrNote()));
        setCrgrNumber("Testnummer");
        setCrgrRuleErrorType(RuleTypeEn.STATE_ERROR);
        setCrgrSuggText("Vorschlagstext");
        setCrgrValidFrom(new Date());
        setCrgrValidTo(new Date());
        setCrgRulePools(new TestPool());
        getCrgRule2Roles().add(new TestRole1());
        getCrgRule2Roles().add(new TestRole2());
    }

    private class TestPool extends CrgRulePools {

        private static final long serialVersionUID = 1L;

        public TestPool() {
            super();
            this.setCrgplIdentifier("Testpool");
            this.setCrgplPoolYear(2018);
            this.setCrgplTo(new Date());
            this.setCrgplFrom(new Date());
            this.setCrgplIsActive(true);
        }
    }

    private class TestRole1 extends CrgRule2Role {

        private static final long serialVersionUID = 1L;

        public TestRole1() {
            super();
            CdbUserRoles role = new CdbUserRoles();
            role.setCdburName("Admin");
            setCdbUserRoles(role);
        }
    }

    private class TestRole2 extends CrgRule2Role {

        private static final long serialVersionUID = 1L;

        public TestRole2() {
            super();
            CdbUserRoles role = new CdbUserRoles();
            role.setCdburName("Bearbeiter");
            setCdbUserRoles(role);
        }
    }
}
