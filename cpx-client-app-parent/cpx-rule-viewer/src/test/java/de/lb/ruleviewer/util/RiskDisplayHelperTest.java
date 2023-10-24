/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.ruleviewer.util;

import de.lb.cpx.rule.element.model.Risk;
import de.lb.cpx.rule.element.model.RiskArea;
import de.lb.cpx.ruleviewer.util.RiskDisplayHelper;
import java.util.logging.Logger;
import static junit.framework.TestCase.assertEquals;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author wilde
 */
public class RiskDisplayHelperTest {
    private static final Logger LOG = Logger.getLogger(RiskDisplayHelperTest.class.getName());
    /*<risk>
    <risk_area risk_area_name="DRG_PEPP" risk_percent_value="20" risk_default_waist_value="200" risk_comment="erä gi 4ü ü45ptz9 e#r+ütop "/>
    <risk_area risk_area_name="MD" risk_percent_value="20" risk_default_waist_value="200" risk_comment="erä gi 4ü ü45ptz9 e#r+ütop "/>
    <risk_area risk_area_name="SD" risk_percent_value="20" risk_default_waist_value="200" risk_comment="erä gi 4ü ü45ptz9 e#r+ütop "/>
    <risk_area risk_area_name="PROCS" risk_percent_value="20" risk_default_waist_value="200" risk_comment="erä gi 4ü ü45ptz9 e#r+ütop "/>
    <risk_area risk_area_name="PRI_MISALO" risk_percent_value="20" risk_default_waist_value="200" risk_comment="erä gi 4ü ü45ptz9 e#r+ütop "/>
    <risk_area risk_area_name="SEC_MISALO" risk_percent_value="20" risk_default_waist_value="200" risk_comment="erä gi 4ü ü45ptz9 e#r+ütop "/>
    <risk_area risk_area_name="CASE_MERGE" risk_percent_value="20" risk_default_waist_value="200" risk_comment="erä gi 4ü ü45ptz9 e#r+ütop "/>
    <risk_area risk_area_name="OT_FEE_SUR" risk_percent_value="20" risk_default_waist_value="200" risk_comment="erä gi 4ü ü45ptz9 e#r+ütop "/>
    <risk_area risk_area_name="OTHER" risk_percent_value="20" risk_default_waist_value="200" risk_comment="erä gi 4ü ü45ptz9 e#r+ütop "/>
    </risk>*/
    private Risk risk_multiple_entries;
    private Risk risk_null;
    private Risk risk_invalid;
    private Risk risk_single_entry;
    private Risk risk_single_entry_missing_risk;
    private Risk risk_single_entry_missing_waste;

    @Before
    public void setUp() throws IllegalAccessException {
        RiskArea area1 = new RiskArea();
        area1.setRiskAreaName("MD");
        area1.setRiskWastePercentValue("50");
        area1.setRiskAuditPercentValue("20");
        area1.setRiskDefaultWasteValue("200");
        
        RiskArea area1_missing_risk = new RiskArea();
        area1_missing_risk.setRiskAreaName("MD");
        area1_missing_risk.setRiskWastePercentValue("50");
        area1_missing_risk.setRiskAuditPercentValue("");
        area1_missing_risk.setRiskDefaultWasteValue("200.00");
        
        RiskArea area1_missing_waste = new RiskArea();
        area1_missing_waste.setRiskAreaName("MD");
        area1_missing_waste.setRiskWastePercentValue("50");
        area1_missing_waste.setRiskAuditPercentValue("20");
        
        RiskArea area2 = new RiskArea();
        area2.setRiskAreaName("PROCS");
        area2.setRiskWastePercentValue("20");
        area2.setRiskDefaultWasteValue("200");
        RiskArea area3 = new RiskArea();
        area3.setRiskAreaName("PRI_MISALO");
        area3.setRiskWastePercentValue("20");
        area3.setRiskDefaultWasteValue("200");
        
        risk_multiple_entries = new Risk();
        risk_multiple_entries.getRiskAreas().add(area1);
        risk_multiple_entries.getRiskAreas().add(area2);
        risk_multiple_entries.getRiskAreas().add(area3);
        
        risk_single_entry = new Risk();
        risk_single_entry.getRiskAreas().add(area1);
        
        risk_single_entry_missing_risk = new Risk();
        risk_single_entry_missing_risk.getRiskAreas().add(area1_missing_risk);
        
        risk_single_entry_missing_waste = new Risk();
        risk_single_entry_missing_waste.getRiskAreas().add(area1_missing_waste);
        
        risk_null = null;
        
        risk_invalid = new Risk();
    }
    @After
    public void tearDown() {
        risk_multiple_entries = null;
    }
    @Test
    @Ignore //irrelevant, risk is only one single instance
    public void testMultiRiskAreaText(){
        String displayResult = "DRG/PEPP, Hauptdiagnose, Nebendiagnosen, Prozeduren, Primäre Fehlbelegung";
        String displaySingleText = RiskDisplayHelper.getTranslatedRiskAreas(risk_multiple_entries);
        assertEquals(displayResult,displaySingleText);
    }
    @Test
    public void testSingleRiskAreaText(){
        String displayResult = "Hauptdiagnose";
        String displaySingleText = RiskDisplayHelper.getTranslatedRiskAreas(risk_single_entry);
        assertEquals(displayResult,displaySingleText);
    }
    @Test
    public void testInvalidDisplayText(){
        String displayResult = "Risiko: Nicht angegeben";
        String displayNullText = RiskDisplayHelper.getRiskDisplayText(risk_null);
        String displayInvalidText = RiskDisplayHelper.getRiskDisplayText(risk_invalid);
        assertEquals(displayResult,displayNullText);
        assertEquals(displayResult,displayInvalidText);
        
    }
    @Test
    public void testSingleDisplayText(){
        String displayResult = "Risiko: Prüfbr. - Hauptdiagnose, Anfr.-R. - 20 %, Verl.-R. - 50 %, Verl.-W. - 200,00 €";
        String displaySingleText = RiskDisplayHelper.getRiskDisplayText(risk_single_entry);
        assertEquals(displayResult,displaySingleText);
        
        String displayResult_missing_risk = "Risiko: Prüfbr. - Hauptdiagnose, Anfr.-R. - n.A., Verl.-R. - 50 %, Verl.-W. - 200,00 €";
        String displaySingleText_missing_risk = RiskDisplayHelper.getRiskDisplayText(risk_single_entry_missing_risk);
        assertEquals(displayResult_missing_risk,displaySingleText_missing_risk);
        
        String displayResult_missing_waist = "Risiko: Prüfbr. - Hauptdiagnose, Anfr.-R. - 20 %, Verl.-R. - 50 %, Verl.-W. - n.A.";
        String displaySingleText_missing_waist = RiskDisplayHelper.getRiskDisplayText(risk_single_entry_missing_waste);
        assertEquals(displayResult_missing_waist,displaySingleText_missing_waist);
        
    }
    @Test
    @Ignore //irrelevant, risk is only one single instance
    public void testMultiDisplayText(){
        String displayResult = "Risiko: Prüfbereich - DRG/PEPP, Hauptdiagnose, Nebendiagnosen, Prozeduren, Primäre Fehlbelegung, Risiko - 20 %, Verlustwert - 200,00 €";
        String displayMultiText = RiskDisplayHelper.getRiskDisplayText(risk_multiple_entries);
        assertEquals(displayResult,displayMultiText);
    }
}
