/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.rule.services.util;

import de.lb.cpx.model.enums.RuleTypeEn;
import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.server.commonDB.model.rules.PoolTypeEn;
import de.lb.cpx.shared.dto.LockException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toList;

/**
 * Data to mockup server services
 *
 * @author wilde
 */
public class MockUpData {

    private static MockUpData INSTANCE;
    private List<CrgRulePools> devPools;
    private List<CrgRulePools> prodPools;
    private Map<PoolTypeEn, Map<Long, List<CrgRules>>> mapPoolToRules;
    private long counterRid = 1;
    private final Map<Long, List<Long>> lockMap = new HashMap<>();
    Map<PoolTypeEn, Map<Long, List<CrgRuleTables>>> mapRuleTables;

    private MockUpData() {
        //
    }

    public static MockUpData instance() {
        if (INSTANCE == null) {
            INSTANCE = new MockUpData();
        }
        return INSTANCE;
    }

    public List<CrgRulePools> getPools(PoolTypeEn pType) {
        switch (pType) {
            case DEV:
                return getDevPools();
            case PROD:
                return getProdPools();
            default:
                return new ArrayList<>();
        }
    }

    public List<CrgRulePools> getDevPools() {
        if (devPools == null) {
            devPools = getTestPools(false);
        }
        return devPools;
    }

    public List<CrgRulePools> getProdPools() {
        if (prodPools == null) {
            prodPools = getTestPools(true);
        }
        return prodPools;
    }

    private List<CrgRulePools> getTestPools(boolean pActive) {
        List<CrgRulePools> list = new ArrayList<>();
        CrgRulePools pool2019 = new CrgRulePools();
        pool2019.setCrgplFrom(new Calendar.Builder().setDate(2019, 0, 1).build().getTime());//new Date(2019, 1, 1));
        pool2019.setCrgplTo(new Calendar.Builder().setDate(2019, 11, 31).build().getTime());//new Date(2019, 12, 31));
        pool2019.setCrgplIsActive(pActive);
        pool2019.setCrgplIdentifier("2019");
        pool2019.setCrgplPoolYear(2019);

        CrgRulePools pool2018 = new CrgRulePools();
        pool2018.setCrgplFrom(new Calendar.Builder().setDate(2018, 0, 1).build().getTime());
        pool2018.setCrgplTo(new Calendar.Builder().setDate(2018, 11, 31).build().getTime());
        pool2018.setCrgplIsActive(pActive);
        pool2018.setCrgplIdentifier("2018");
        pool2018.setCrgplPoolYear(2018);

        CrgRulePools pool2017 = new CrgRulePools();
        pool2017.setCrgplFrom(new Calendar.Builder().setDate(2017, 0, 1).build().getTime());
        pool2017.setCrgplTo(new Calendar.Builder().setDate(2017, 11, 31).build().getTime());
        pool2017.setCrgplIsActive(pActive);
        pool2017.setCrgplIdentifier("2017");
        pool2017.setCrgplPoolYear(2017);

        CrgRulePools pool2016 = new CrgRulePools();
        pool2016.setCrgplFrom(new Calendar.Builder().setDate(2016, 0, 1).build().getTime());
        pool2016.setCrgplTo(new Calendar.Builder().setDate(2016, 11, 31).build().getTime());
        pool2016.setCrgplIsActive(pActive);
        pool2016.setCrgplIdentifier("2016");
        pool2016.setCrgplPoolYear(2016);

        list.add(pool2019);
        pool2019.setId(list.size() + 1L);
        list.add(pool2018);
        pool2018.setId(list.size() + 1L);
        list.add(pool2017);
        pool2017.setId(list.size() + 1L);
        list.add(pool2016);
        pool2016.setId(list.size() + 1L);
        return list;
    }

    public boolean savePool(CrgRulePools pPool, PoolTypeEn pType) {
        switch (pType) {
            case DEV:
                pPool.setId(getDevPools().size() + 1L);
                pPool.setCreationUser(ClientManager.getCurrentCpxUserId());
                return getDevPools().add(pPool);
            case PROD:
                pPool.setId(getProdPools().size() + 1L);
                pPool.setCrgplIsActive(true);
                pPool.setCreationUser(ClientManager.getCurrentCpxUserId());
                return getProdPools().add(pPool);
            default:
                return false;
        }
    }

    public void updatePool(CrgRulePools pPool, PoolTypeEn pType) {
        switch (pType) {
            case DEV:
                replace(getDevPools(), pPool);
                break;
            case PROD:
                replace(getProdPools(), pPool);
                break;
        }
    }

    private void replace(List<CrgRulePools> pPools, CrgRulePools pPool) {
        int index = pPools.indexOf(findById(pPool.getId(), pPools));
        pPools.set(index, pPool);
    }

    public CrgRulePools findById(long pId, List<CrgRulePools> pPools) {
        for (CrgRulePools pool : pPools) {
            if (pool.getId() == pId) {
                return pool;
            }
        }
        return null;
    }

    public CrgRulePools findById(long pId, PoolTypeEn pType) {
        switch (pType) {
            case DEV:
                return findById(pId, getDevPools());
            case PROD:
                return findById(pId, getProdPools());

        }
        return null;
    }

    public void deletePool(CrgRulePools pPool, PoolTypeEn pType) {
        switch (pType) {
            case DEV:
                delete(getDevPools(), pPool);
                break;
            case PROD:
                delete(getProdPools(), pPool);
                break;
        }
    }

    private void delete(List<CrgRulePools> prodPools, CrgRulePools pPool) {
        prodPools.remove(pPool);
    }

    public List<CrgRules> findRulesForPool(long pPoolId, PoolTypeEn pType) {
        if (mapPoolToRules == null) {
            mapPoolToRules = new HashMap<>();
            mapPoolToRules.put(PoolTypeEn.DEV, new HashMap<>());
            mapPoolToRules.put(PoolTypeEn.PROD, new HashMap<>());
        }
        if (!mapPoolToRules.get(pType).containsKey(pPoolId)) {
            List<CrgRules> newData = createNewMockUpRules(pPoolId, pType);
            mapPoolToRules.get(pType).put(pPoolId, newData);
        }
        return mapPoolToRules.get(pType).get(pPoolId);

    }

    private long generateRid() {
        counterRid++;
        return counterRid;
    }

    private String generateRidAsString() {
        return String.valueOf(generateRid());
    }

    private List<CrgRules> createNewMockUpRules(long pPoolId, PoolTypeEn pPoolType) {
        List<CrgRules> rules = new ArrayList<>();
        CrgRulePools pool = findById(pPoolId, pPoolType);
        if (pool == null) {
            return new ArrayList<>();
        }
        CrgRules r1 = new CrgRules();
        r1.setCreationDate(new Date());
        try {
            String rule = "<rule caption=\"Neurologische Komplexbehandlung, VWD &amp;lt;73h, aber &amp;gt;23h\" entgelt=\"true\" errror_type=\"OPS\" feegroup=\"1\" from=\"01.01.2018\" hasinterval=\"false\" interval_from=\"\" interval_to=\"\" massnumber=\"\" medtype=\"\" number=\"004a\" profit=\"0.0\" rid=\"1002057\" role=\"1\" rules_notice=\"\" rules_number=\"2018_1002639_20129999000000\" rules_year=\"2018\" text=\"OPS\" to=\"31.12.2018\" typ=\"error\" unchange=\"0\" used=\"true\" visible=\"\">\n"
                    + "	<rules_element nested=\"false\">\n"
                    + "		<rules_element nested=\"true\">\n"
                    + "			<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"Prozedur\" method=\"\" not=\"false\" operator=\"==\" parameter=\"\" wert=\"'8-981.1'\"/>\n"
                    + "			<rules_operator op_type=\"||\"/>\n"
                    + "			<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"Prozedur\" method=\"\" not=\"false\" operator=\"==\" parameter=\"\" wert=\"'8-98B.1%'\"/>\n"
                    + "		</rules_element>\n"
                    + "		<rules_operator op_type=\"&amp;&amp;\"/>\n"
                    + "		<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"VerweildauerInStunden\" method=\"\" not=\"false\" operator=\"&amp;lt;\" parameter=\"\" wert=\"73\"/>\n"
                    + "		<rules_operator op_type=\"&amp;&amp;\"/>\n"
                    + "		<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"VerweildauerInStunden\" method=\"\" not=\"false\" operator=\"&amp;gt;\" parameter=\"\" wert=\"23\"/>\n"
                    + "	</rules_element>\n"
                    + "	<suggestions suggtext=\"Neurologische Komplexbehandlungszeiten passen nicht zur VWD.\">\n"
                    + "		<sugg actionid=\"2\" condition_op=\"==\" condition_value=\"8-981.1\" crit=\"Prozedur\" op=\"==\" value=\"8-981.0\"/>\n"
                    + "		<sugg actionid=\"2\" condition_op=\"==\" condition_value=\"8-98B.10\" crit=\"Prozedur\" op=\"==\" value=\"8-98B.00\"/>\n"
                    + "		<sugg actionid=\"2\" condition_op=\"==\" condition_value=\"8-98B.11\" crit=\"Prozedur\" op=\"==\" value=\"8-98B.01\"/>\n"
                    + "	</suggestions>\n"
                    + "</rule>";
            r1.setCrgrDefinition(rule.getBytes("UTF-16"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MockUpData.class.getName()).log(Level.SEVERE, null, ex);
        }
        r1.setCrgrValidFrom(pool.getCrgplFrom());
        r1.setCrgrValidTo(pool.getCrgplFrom());
        r1.setCrgrCaption("Neurologische Komplexbehandlung, VWD >73h, aber <23h");
        r1.setCrgrRid(generateRidAsString());//"1002057");
        r1.setCrgrNumber("004a");
        r1.setCrgrRuleErrorType(RuleTypeEn.STATE_ERROR);
        r1.setCrgrIdentifier(pool.getCrgplPoolYear() + "_" + r1.getCrgrRid());
        r1.setCrgrCategory("OPS");
        r1.setCrgrSuggText("Neurologische Komplexbehandlungszeiten passen nicht zur VWD.");
//        rules.add(r1);

        CrgRules r2 = new CrgRules();
        r2.setCreationDate(new Date());
        try {
            String rule = "<rule caption=\"Bakteriämie als ND und Sepsis  in einem Datensatz\" entgelt=\"true\" errror_type=\"DKR\" feegroup=\"1\" from=\"01.01.2018\" hasinterval=\"false\" interval_from=\"\" interval_to=\"\" massnumber=\"\" medtype=\"\" number=\"205a\" profit=\"0.0\" rid=\"1002182\" role=\"1\" rules_notice=\"DKR 0103&amp;#013Bakteriämie, Sepsis, SIRS und Neutropenie Bakteriämie&amp;#013&amp;#013Eine Bakteriämie ist mit einem Kode aus&amp;#013A49.− Bakterielle Infektion, nicht näher bezeichneter Lokalisation oder einem anderen Kode, der spezifisch den Erreger benennt z.B. A54.9 Gonokokkeninfektion, nicht näher bezeichnet zu kodieren. &amp;#013Sie ist nicht mit einem Sepsis-Kode (siehe Tabelle 1) zu verschlüsseln.&amp;#013Eine Ausnahme hiervon stellt die Meningokokken-Bakteriämie dar, die mit&amp;#013A39.4 Meningokokkensepsis, nicht näher bezeichnet zu verschlüsseln ist.&amp;#013&amp;#013Sepsis (Septikämie)&amp;#013Im Gegensatz dazu wird eine Sepsis mit einem passenden Sepsis-Kode z.B. aus Tabelle 1 kodiert. Dies trifft auch auf eine klinisch manifeste Urosepsis zu.\" rules_number=\"2018_1002764_20129999000000\" rules_year=\"2018\" text=\"DKR 0103\" to=\"31.12.2018\" typ=\"warning\" unchange=\"0\" used=\"true\" visible=\"\">\n"
                    + "<rules_element nested=\"false\">\n"
                    + "<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"Diagnose\" method=\"\" not=\"false\" operator=\"@\" parameter=\"\" wert=\"'BAKTERIAEMIE2'\"/>\n"
                    + "<rules_operator op_type=\"&amp;&amp;\"/>\n"
                    + "<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"Nebendiagnose\" method=\"\" not=\"false\" operator=\"@\" parameter=\"\" wert=\"'BAKTERIAEMIE1'\"/>\n"
                    + "<rules_operator op_type=\"&amp;&amp;\"/>\n"
                    + "<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"Hauptdiagnose\" method=\"\" not=\"true\" operator=\"@\" parameter=\"\" wert=\"'BAKTERIAEMIE1'\"/></rules_element>\n"
                    + "<suggestions suggtext=\"Sepsis + Bakteriämie bitte analog der DKR kodieren (-Bakteriämiekode).\">\n"
                    + "<sugg actionid=\"0\" condition_op=\"\" condition_value=\"\" crit=\"Diagnose\" op=\"@\" value=\"BAKTERIAEMIE1\"/></suggestions></rule>";
            r2.setCrgrDefinition(rule.getBytes("UTF-16"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MockUpData.class.getName()).log(Level.SEVERE, null, ex);
        }
        r2.setCrgrValidFrom(pool.getCrgplFrom());
        r2.setCrgrValidTo(pool.getCrgplFrom());
        r2.setCrgrCaption("Bakteriämie als ND und Sepsis  in einem Datensatz");
        r2.setCrgrRid(generateRidAsString());//"1002182");
        r2.setCrgrNumber("205a");
        r2.setCrgrRuleErrorType(RuleTypeEn.STATE_WARNING);
        r2.setCrgrIdentifier(pool.getCrgplPoolYear() + "_" + r2.getCrgrRid());
        r2.setCrgrCategory("DKR 0103");
        r2.setCrgrSuggText("Sepsis + Bakteriämie bitte analog der DKR kodieren (-Bakteriämiekode).");
//        rules.add(r2);

        CrgRules r3 = new CrgRules();
        r3.setCreationDate(new Date());
        try {
            String rule = "<rule caption=\"Beatmung &amp;gt;24h ohne Prozedurdokumentation bei Neugeborenen.\" entgelt=\"true\" errror_type=\"DKR\" feegroup=\"1\" from=\"01.01.2018\" hasinterval=\"false\" interval_from=\"\" interval_to=\"\" massnumber=\"\" medtype=\"\" number=\"213b \" profit=\"0.0\" rid=\"1002185\" role=\"1\" rules_notice=\"DKR 1001&amp;#013Beatmung&amp;#013&amp;#013Kodierung&amp;#013Wenn eine maschinelle Beatmung die Definition der DKR&amp;#013erfüllt, ist&amp;#013&amp;#0131) zunächst die Dauer der künstlichen Beatmung zu &amp;#013erfassen. ...&amp;#013&amp;#0132) Dann ist zusätzlich:&amp;#013&amp;#0132a) einer der folgenden Kodes&amp;#013 8-701 Einfache endotracheale Intubation&amp;#013 8-704 Intubation mit Doppellumentubus&amp;#013 8-706 Anlegen einer Maske zur maschinellen Beatmung&amp;#013und/oder&amp;#013&amp;#0132b) der zutreffende Kode aus&amp;#013 5-311 Temporäre Tracheostomie oder&amp;#013 5-312 Permanente Tracheostomie&amp;#013anzugeben, wenn zur Durchführung der künstlichen &amp;#013Beatmung ein Tracheostoma angelegt wurde.&amp;#013&amp;#0133) Bei Neugeborenen und Säuglingen ist zusätzlich ein &amp;#013Kode aus&amp;#013 8-711 Maschinelle Beatmung bei Neugeborenen und    Säuglingen&amp;#013anzugeben.\" rules_number=\"2018_1002767_20129999000000\" rules_year=\"2018\" text=\"DKR 1001\" to=\"31.12.2018\" typ=\"warning\" unchange=\"0\" used=\"true\" visible=\"\">\n"
                    + "<rules_element nested=\"false\">\n"
                    + "<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"Beatmungsdauer\" method=\"\" not=\"false\" operator=\"&amp;gt;=\" parameter=\"\" wert=\"24\"/>\n"
                    + "<rules_operator op_type=\"&amp;&amp;\"/>\n"
                    + "<rules_element nested=\"true\">\n"
                    + "<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"Prozedur\" method=\"\" not=\"true\" operator=\"@\" parameter=\"\" wert=\"'BEATMUNG'\"/>\n"
                    + "<rules_operator op_type=\"||\"/>\n"
                    + "<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"Prozedur\" method=\"\" not=\"true\" operator=\"==\" parameter=\"\" wert=\"'8-711%'\"/></rules_element>\n"
                    + "<rules_operator op_type=\"&amp;&amp;\"/>\n"
                    + "<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"DRG\" method=\"\" not=\"false\" operator=\"==\" parameter=\"\" wert=\"'P%'\"/></rules_element>\n"
                    + "<suggestions suggtext=\"Beatmung &amp;gt;24h erfordert zusätzliche OPS (z.B. +8-701, +8-711.2).\">\n"
                    + "<sugg actionid=\"1\" condition_op=\"\" condition_value=\"\" crit=\"Prozedur\" op=\"==\" value=\"8-701\"/>\n"
                    + "<sugg actionid=\"1\" condition_op=\"\" condition_value=\"\" crit=\"Prozedur\" op=\"==\" value=\"8-711.2\"/></suggestions></rule>";
            r3.setCrgrDefinition(rule.getBytes("UTF-16"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MockUpData.class.getName()).log(Level.SEVERE, null, ex);
        }
        r3.setCrgrValidFrom(pool.getCrgplFrom());
        r3.setCrgrValidTo(pool.getCrgplFrom());
        r3.setCrgrCaption("Beatmung >24h ohne Prozedurdokumentation bei Neugeborenen.");
        r3.setCrgrRid(generateRidAsString());//"1002185");
        r3.setCrgrNumber("213b");
        r3.setCrgrRuleErrorType(RuleTypeEn.STATE_WARNING);
        r3.setCrgrIdentifier(pool.getCrgplPoolYear() + "_" + r3.getCrgrRid());
        r3.setCrgrCategory("DKR 1001");
        r3.setCrgrSuggText("Beatmung >24h erfordert zusätzliche OPS (z.B. +8-701, +8-711.2).");
//        rules.add(r3);

        CrgRules r4 = new CrgRules();
        r4.setCreationDate(new Date());
        try {
            String rule = "<rule caption=\"Aspirationssyndrom  beim Neugeborenen als ND und nicht als HD, ohne OPS für Beatmung / Sauerstoff\" entgelt=\"true\" errror_type=\"DKR\" feegroup=\"1\" from=\"01.01.2018\" hasinterval=\"false\" interval_from=\"\" interval_to=\"\" massnumber=\"\" medtype=\"\" number=\"066c\" profit=\"0.0\" rid=\"1002187\" role=\"1\" rules_notice=\"DKR 1605&amp;#013Massives Aspirationssyndrom und transitorische Tachypnoe&amp;#013beim Neugeborenen&amp;#013&amp;#013Die Kategorie &quot;P24.– Aspirationssyndrome beim Neugeborenen&quot; ist zu verwenden, wenn die Atemstörung – bedingt durch das Aspirationssyndrom – eine Sauerstoffzufuhr von über 24 Stunden Dauer erforderte.&amp;#013Der Kode &quot;P22.1 Transitorische Tachypnoe beim Neugeborenen&quot; ist bei folgenden Diagnosen zu verwenden:&amp;#013• transitorische Tachypnoe beim Neugeborenen (ungeachtet der Dauer der Sauerstofftherapie)&amp;#013oder&amp;#013• Aspirationssyndrom beim Neugeborenen, wenn die Atemstörung eine Sauerstoffzufuhr von weniger als 24 Stunden Dauer erforderte.&amp;#013\" rules_number=\"2018_1002769_20129999000000\" rules_year=\"2018\" text=\"DKR 1605\" to=\"31.12.2018\" typ=\"warning\" unchange=\"0\" used=\"true\" visible=\"\">\n"
                    + "<rules_element nested=\"false\">\n"
                    + "<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"Nebendiagnose\" method=\"\" not=\"false\" operator=\"==\" parameter=\"\" wert=\"'P24%'\"/>\n"
                    + "<rules_operator op_type=\"&amp;&amp;\"/>\n"
                    + "<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"Prozedur\" method=\"\" not=\"true\" operator=\"@\" parameter=\"\" wert=\"'OPSNEUGEBORENBEATMUNG'\"/>\n"
                    + "<rules_operator op_type=\"&amp;&amp;\"/>\n"
                    + "<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"Hauptdiagnose\" method=\"\" not=\"true\" operator=\"==\" parameter=\"\" wert=\"'P24%'\"/></rules_element>\n"
                    + "<suggestions suggtext=\"Aspirationssyndrom wird nur kodiert, wenn zumindest eine Sauerstoffzufuhr von &amp;gt;24 Stunden erforderlich wurde (-P24*).\">\n"
                    + "<sugg actionid=\"0\" condition_op=\"\" condition_value=\"\" crit=\"Diagnose\" op=\"==\" value=\"P24*\"/>\n"
                    + "<sugg actionid=\"1\" condition_op=\"\" condition_value=\"\" crit=\"Nebendiagnose\" op=\"==\" value=\"P22.1\"/></suggestions></rule>";
            r4.setCrgrDefinition(rule.getBytes("UTF-16"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MockUpData.class.getName()).log(Level.SEVERE, null, ex);
        }
        r4.setCrgrValidFrom(pool.getCrgplFrom());
        r4.setCrgrValidTo(pool.getCrgplFrom());
        r4.setCrgrCaption("Aspirationssyndrom  beim Neugeborenen als ND und nicht als HD, ohne OPS für Beatmung / Sauerstoff");
        r4.setCrgrRid(generateRidAsString());//"1002187");
        r4.setCrgrNumber("066c");
        r4.setCrgrRuleErrorType(RuleTypeEn.STATE_WARNING);
        r4.setCrgrIdentifier(pool.getCrgplPoolYear() + "_" + r4.getCrgrRid());
        r4.setCrgrCategory("DKR 1605");
        r4.setCrgrSuggText("Aspirationssyndrom wird nur kodiert, wenn zumindest eine Sauerstoffzufuhr von >24 Stunden erforderlich wurde (-P24*).");
        r4.setCrgrNote("DKR 1605&amp;#013Massives Aspirationssyndrom und transitorische Tachypnoe&amp;#013beim Neugeborenen&amp;#013&amp;#013Die Kategorie &quot;P24.– Aspirationssyndrome beim Neugeborenen&quot; ist zu verwenden, wenn die Atemstörung – bedingt durch das Aspirationssyndrom – eine Sauerstoffzufuhr von über 24 Stunden Dauer erforderte.&amp;#013Der Kode &quot;P22.1 Transitorische Tachypnoe beim Neugeborenen&quot; ist bei folgenden Diagnosen zu verwenden:&amp;#013• transitorische Tachypnoe beim Neugeborenen (ungeachtet der Dauer der Sauerstofftherapie)&amp;#013oder&amp;#013• Aspirationssyndrom beim Neugeborenen, wenn die Atemstörung eine Sauerstoffzufuhr von weniger als 24 Stunden Dauer erforderte.&amp;#013");
//        rules.add(r4);

        CrgRules r5 = new CrgRules();
        r5.setCreationDate(new Date());
        try {
            String rule = "<rule caption=\"Linksherzinsuffizienz, nicht näher bezeichnet\" entgelt=\"true\" errror_type=\"ICD\" feegroup=\"1\" from=\"01.01.2018\" hasinterval=\"false\" interval_from=\"\" interval_to=\"\" massnumber=\"\" medtype=\"\" number=\"270 \" profit=\"0.0\" rid=\"1002222\" role=\"1\" rules_notice=\"I50.1- Linksherzinsuffizienz&amp;#013Asthma cardiale&amp;#013Linksherzversagen&amp;#013Lungenödem (akut) mit Angabe einer nicht näher bezeichneten Herzkrankheit oder einer Herzinsuffizienz&amp;#013&amp;#013I50.11 Ohne Beschwerden&amp;#013NYHA-Stadium I&amp;#013&amp;#013I50.12 Mit Beschwerden bei stärkerer Belastung&amp;#013NYHA-Stadium II&amp;#013&amp;#013I50.13 Mit Beschwerden bei leichterer Belastung&amp;#013NYHA-Stadium III&amp;#013&amp;#013I50.14 Mit Beschwerden in Ruhe&amp;#013NYHA-Stadium IV&amp;#013&amp;#013I50.19 Nicht näher bezeichnet&amp;#013&amp;#013I50.9 Herzinsuffizienz, nicht näher bezeichnet&amp;#013Herz- oder Myokardinsuffizienz o.n.A. \" rules_number=\"2018_1002804_20129999000000\" rules_year=\"2018\" text=\"ICD\" to=\"31.12.2018\" typ=\"warning\" unchange=\"0\" used=\"true\" visible=\"\">\n"
                    + "<rules_element nested=\"false\">\n"
                    + "<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"Nebendiagnose\" method=\"\" not=\"false\" operator=\"@\" parameter=\"\" wert=\"'HERZINSUFF'\"/>\n"
                    + "<rules_operator op_type=\"&amp;&amp;\"/>\n"
                    + "<rules_value hasinterval=\"false\" interval_from=\"\" interval_to=\"\" kriterium=\"Diagnose\" method=\"\" not=\"true\" operator=\"@\" parameter=\"\" wert=\"'HERZINSUFF_SPEZ'\"/></rules_element>\n"
                    + "<suggestions suggtext=\"Linksherzinsuffizienz, falls möglich spezifizieren (+I50.13).\">\n"
                    + "<sugg actionid=\"1\" condition_op=\"\" condition_value=\"\" crit=\"Nebendiagnose\" op=\"==\" value=\"I50.13\"/></suggestions></rule>";
            r5.setCrgrDefinition(rule.getBytes("UTF-16"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MockUpData.class.getName()).log(Level.SEVERE, null, ex);
        }
        r5.setCrgrValidFrom(pool.getCrgplFrom());
        r5.setCrgrValidTo(pool.getCrgplFrom());
        r5.setCrgrCaption("Linksherzinsuffizienz, nicht näher bezeichnet");
        r5.setCrgrRid(generateRidAsString());//"1000596");
        r5.setCrgrNumber("270");
        r5.setCrgrRuleErrorType(RuleTypeEn.STATE_WARNING);
        r5.setCrgrIdentifier(pool.getCrgplPoolYear() + "_" + r5.getCrgrRid());
        r5.setCrgrCategory("ICD");
        r5.setCrgrSuggText("Linksherzinsuffizienz, falls möglich spezifizieren (+I50.13).");
        r5.setCrgrNote("I50.1- Linksherzinsuffizienz&amp;#013Asthma cardiale&amp;#013Linksherzversagen&amp;#013Lungenödem (akut) mit Angabe einer nicht näher bezeichneten Herzkrankheit oder einer Herzinsuffizienz&amp;#013&amp;#013I50.11 Ohne Beschwerden&amp;#013NYHA-Stadium I&amp;#013&amp;#013I50.12 Mit Beschwerden bei stärkerer Belastung&amp;#013NYHA-Stadium II&amp;#013&amp;#013I50.13 Mit Beschwerden bei leichterer Belastung&amp;#013NYHA-Stadium III&amp;#013&amp;#013I50.14 Mit Beschwerden in Ruhe&amp;#013NYHA-Stadium IV&amp;#013&amp;#013I50.19 Nicht näher bezeichnet&amp;#013&amp;#013I50.9 Herzinsuffizienz, nicht näher bezeichnet&amp;#013Herz- oder Myokardinsuffizienz o.n.A. ");

        rules.add(r1);
        r1.setId(rules.size());
        rules.add(r2);
        r2.setId(rules.size());
        rules.add(r3);
        r3.setId(rules.size());
        rules.add(r4);
        r4.setId(rules.size());
        rules.add(r5);
        r5.setId(rules.size());

        return rules;
    }

    public int getRulesSize(long pPoolId, PoolTypeEn pType) {
        return findRulesForPool(pPoolId, pType).size();
    }

    public String saveRule(CrgRules rule, long id, PoolTypeEn poolType) {
        List<CrgRules> list = findRulesForPool(id, poolType);
        if (list != null) {
            rule.setId(list.size() + 1L);
            rule.setCrgrRid(generateRidAsString());
            list.add(rule);
            return rule.getCrgrRid();
        }
        return null;
    }

    public void copyRule(CrgRules pRule, long pPoolId, PoolTypeEn pPoolType) {
        List<CrgRules> list = findRulesForPool(pPoolId, pPoolType);
        if (list != null) {
            CrgRules copy = copyRule(pRule);
            copy.setId(list.size() + 1L);
            list.add(copy);
        }

    }

    private CrgRules copyRule(CrgRules pRule) {
        CrgRules copy = new CrgRules();
        copy.setCrgRule2Roles(pRule.getCrgRule2Roles());
//        copy.setCrgRule2Tables(pRule.getCrgRule2Tables());
//        copy.setCrgRulePools(pRule.getCrgRulePools());
//        copy.setCrgRuleTypes(pRule.getCrgRuleTypes());
        copy.setCrgrCaption(pRule.getCrgrCaption());
        copy.setCrgrCategory(pRule.getCrgrCategory());
        copy.setCrgrDefinition(pRule.getCrgrDefinition());
        copy.setCrgrIdentifier(pRule.getCrgrIdentifier());
        copy.setCrgrNote(pRule.getCrgrNote());
        copy.setCrgrNumber(pRule.getCrgrNumber());
        copy.setCrgrRid(generateRidAsString());//pRule.getCrgrRid());
        copy.setCrgrRuleErrorType(pRule.getCrgrRuleErrorType());
        copy.setCrgrSuggText(pRule.getCrgrSuggText());
        copy.setCrgrSuggestion(pRule.getCrgrSuggestion());
        copy.setCrgrValidFrom(pRule.getCrgrValidFrom());
        copy.setCrgrValidTo(pRule.getCrgrValidTo());
        return copy;
    }

    public void deleteRule(CrgRules pRule, long pPoolId, PoolTypeEn pPoolType) {
        List<CrgRules> list = findRulesForPool(pPoolId, pPoolType);
        if (list != null) {
            list.remove(pRule);
        }
    }

    public void clearLockMap() {
        lockMap.clear();
    }

    public void lockRule(long pPoolId, long pRuleId) throws LockException {
        if (hasLockRule(pPoolId, pRuleId)) {
            //thorw db lock, interface has no other message? 
            //Implement a more common approache to this matter? Should talk to dirk??
            throw LockException.createDbLockException("Rule with id: " + pRuleId + " on Pool with id: " + pPoolId + " is already locked!");
        }
        if (!lockMap.containsKey(pPoolId)) {
            lockMap.put(pPoolId, new ArrayList<>());
        }
        List<Long> lockedRules = lockMap.get(pPoolId);
        if (!lockedRules.contains(pRuleId)) {
            lockedRules.add(pRuleId);
        }
    }

    public void unLockRule(long pPoolId, long pRuleId) {
        if (!lockMap.containsKey(pPoolId)) {
            return;
        }
        List<Long> lockedRules = lockMap.get(pPoolId);
        lockedRules.remove(pRuleId);
    }

    public boolean hasLockRule(long pPoolId, long pRuleId) {
        if (!lockMap.containsKey(pPoolId)) {
            return false;
        }
        List<Long> lockedRules = lockMap.get(pPoolId);
        return lockedRules.contains(pRuleId);
    }

    public void clearLockRuleForPool(long pPoolId) {
        if (lockMap.containsKey(pPoolId)) {
            lockMap.get(pPoolId).clear();
        }
    }

    public List<CrgRuleTables> getRuleTables(PoolTypeEn pType, long pPoolId) {
        initMapRulesTables();
        if (!mapRuleTables.get(pType).containsKey(pPoolId)) {
            mapRuleTables.get(pType).put(pPoolId, createMockUpRuleTables(findById(pPoolId, pType)));
        }
        return mapRuleTables.get(pType).get(pPoolId);
    }

    public List<CrgRuleTables> getAllRuleTables() {
        List<CrgRuleTables> tables = new ArrayList<>();
        tables.addAll(getAllRuleTables(PoolTypeEn.PROD));
        tables.addAll(getAllRuleTables(PoolTypeEn.DEV));
        return tables;
    }

    public List<CrgRuleTables> getAllRuleTables(PoolTypeEn pType) {
        List<CrgRuleTables> tables = new ArrayList<>();
        Iterator<Long> it = mapRuleTables.get(pType).keySet().iterator();
        while (it.hasNext()) {
            Long next = it.next();
            tables.addAll(mapRuleTables.get(pType).get(next));
        }
        return tables;
    }

    private void initMapRulesTables() {
        if (mapRuleTables == null) {
            mapRuleTables = new HashMap<>();
            mapRuleTables.put(PoolTypeEn.DEV, new HashMap<>());
            mapRuleTables.put(PoolTypeEn.PROD, new HashMap<>());
        }
    }

    private List<CrgRuleTables> createMockUpRuleTables(CrgRulePools pPool) {
        List<CrgRuleTables> tables = new ArrayList<>();
        CrgRuleTables t1 = new CrgRuleTables();
        t1.setCrgtTableName("BEATMUNG");
        t1.setCrgtContent("5-311%,"
                + "5-312%,"
                + "8-701,"
                + "8-704,"
                + "8-706");
        t1.setCrgRulePools(pPool);

        CrgRuleTables t2 = new CrgRuleTables();
        t2.setCrgtTableName("BAKTERIAEMIE1");
        t2.setCrgtContent("A20.9,"
                + "A22.9,"
                + "A24.4,"
                + "A26.9,"
                + "A32.9,"
                + "A39.9,"
                + "A42.9,"
                + "A49%,"
                + "A54.9,"
                + "B37.9,"
                + "P39.9");
        t2.setCrgRulePools(pPool);

        CrgRuleTables t3 = new CrgRuleTables();
        t3.setCrgtTableName("BAKTERIAEMIE2");
        t3.setCrgtContent("A02.1,"
                + "A32.7,"
                + "A39.2,"
                + "A39.3,"
                + "A39.4,"
                + "A40.%,"
                + "A41.%,"
                + "B37.7,"
                + "P36.%");
        t3.setCrgRulePools(pPool);

        CrgRuleTables t4 = new CrgRuleTables();
        t4.setCrgtTableName("GESTATIONSHYPERTONIE");
        t4.setCrgtContent("O13,"
                + "O14%");
        t4.setCrgRulePools(pPool);

        CrgRuleTables t5 = new CrgRuleTables();
        t5.setCrgtTableName("OPSNEUGEBORENBEATMUNG");
        t5.setCrgtContent("8-711%,"
                + "8-720");
        t5.setCrgRulePools(pPool);

        CrgRuleTables t6 = new CrgRuleTables();
        t6.setCrgtTableName("HERZINSUFF");
        t6.setCrgtContent("I50.19,"
                + "I50.9");
        t6.setCrgRulePools(pPool);

        CrgRuleTables t7 = new CrgRuleTables();
        t7.setCrgtTableName("HERZINSUFF_SPEZ");
        t7.setCrgtContent("I50.11,"
                + "I50.12,"
                + "I50.13,"
                + "I50.14");
        t7.setCrgRulePools(pPool);

        tables.add(t1);
        t1.setId(tables.size());

        tables.add(t2);
        t2.setId(tables.size());

        tables.add(t3);
        t3.setId(tables.size());

        tables.add(t4);
        t4.setId(tables.size());

        tables.add(t5);
        t5.setId(tables.size());

        tables.add(t6);
        t6.setId(tables.size());

        tables.add(t7);
        t7.setId(tables.size());
        return tables;
    }

    public CrgRuleTables findRuleTable(String pName, PoolTypeEn poolType, long pPoolId) {
        pName = pName.replace("'", "");
        List<CrgRuleTables> tables = getRuleTables(poolType, pPoolId);
        for (CrgRuleTables table : tables) {
            if (table.getCrgtTableName().equalsIgnoreCase(pName)) {
                return table;
            }
        }
        return null;
    }

    public void saveRuleTable(long pPoolId, PoolTypeEn pPoolType, CrgRuleTables pTable) {
//        CrgRulePools pool = findById(pPoolId, pPoolType);
        List<CrgRuleTables> tables = getRuleTables(pPoolType, pPoolId);
        tables.add(pTable);
//        pool.getCrgRuleTableses().add(pTable);
    }

    public void updateRule(CrgRules pRule, long pPoolId, PoolTypeEn pPoolType) {
//        CrgRulePools pool = findById(pPoolId, pPoolType);
        List<CrgRules> pool = findRulesForPool(pPoolId, pPoolType);
        List<CrgRules> newItems = pool.stream()
                .map(o -> o.getId() == pRule.getId() ? pRule : o)
                .collect(toList());
        pool.clear();
        pool.addAll(newItems);
    }

    public void updateRuleTable(long pPoolId, PoolTypeEn pPoolType, CrgRuleTables pTable) {
        List<CrgRuleTables> pool = getRuleTables(pPoolType, pPoolId);
        List<CrgRuleTables> newItems = pool.stream()
                .map(o -> (o.getCrgtTableName() == null ? pTable.getCrgtTableName() == null : o.getCrgtTableName().equals(pTable.getCrgtTableName())) ? pTable : o)
                .collect(toList());
        pool.clear();
        pool.addAll(newItems);
    }

}
