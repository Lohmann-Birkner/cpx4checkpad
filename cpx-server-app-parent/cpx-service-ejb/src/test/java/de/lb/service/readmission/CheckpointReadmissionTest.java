/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.service.readmission;

import de.checkpoint.server.rmServer.caseManager.RmcPeppWiederaufnahme;
import de.checkpoint.server.rmServer.caseManager.RmcWiederaufnahme;
import de.checkpoint.server.rmServer.caseManager.RmcWiederaufnahmeIF;
import de.lb.cpx.service.readmission.CheckReadmissionsService;
import de.lb.service.readmission.utils.TestCaseBuilder;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * Test der Einbinung der Checkpoint Klassen für die Anwendung der
 * Wiederaufnahmeregelung
 *
 * @author gerschmann
 */
public class CheckpointReadmissionTest {

    private static final Logger LOG = Logger.getLogger(CheckpointReadmissionTest.class.getName());
    CheckReadmissionsService checker;

    @Before
    public void createReadmissionsChecker() {
        checker = new CheckReadmissionsService();
    }

    /**
     * prüft die Anwendung der Wiederaufnahmeregelung für DRG - Fälle
     */
    @Test
    public void checkReadmissionsDrgTest() {
        ArrayList<RmcWiederaufnahmeIF> caseLst = new ArrayList<>();
        ArrayList<TestCaseBuilder.TestReadmissionCase> input = getCandidates(caseLst, false);
        checker.performDoRegeln(caseLst, caseLst.size() - 1, false);
        assertEquals(true, true); //to prevent issue in SonarQube
        checkResults(caseLst, input, false);
    }

    /**
     * Prüft die Anwendung der Wiederaufnahmeregelung für Peppfälle
     */
    @Test
    public void checkReadmissionsPeppTest() {
        ArrayList<RmcWiederaufnahmeIF> caseLst = new ArrayList<>();
        ArrayList<TestCaseBuilder.TestReadmissionCase> input = getCandidates(caseLst, true);
        checker.performDoRegeln(caseLst, caseLst.size() - 1, true);
        assertEquals(true, true); //to prevent issue in SonarQube
        checkResults(caseLst, input, true);
    }

    private ArrayList<TestCaseBuilder.TestReadmissionCase> getCandidates(ArrayList<RmcWiederaufnahmeIF> caseLst, boolean isPepp) {

        ArrayList<TestCaseBuilder.TestReadmissionCase> input = TestCaseBuilder.getReadmissionTestList(isPepp);

        for (TestCaseBuilder.TestReadmissionCase oneCase : input) {
            caseLst.add(oneCase.getInputData(isPepp));
        }
        return input;
    }

    private void checkResults(ArrayList<RmcWiederaufnahmeIF> caseLst, ArrayList<TestCaseBuilder.TestReadmissionCase> input, boolean isPepp) {

        for (int i = 0, n = input.size(); i < n; i++) {
            TestCaseBuilder.TestReadmissionCase oneCase = input.get(i);

            TestCaseBuilder.TestReadmissionCaseResult oneResult = oneCase.getOutputData();
            if (!isPepp) {
                RmcWiederaufnahme wa = (RmcWiederaufnahme) caseLst.get(i);
                logResult("berechnet: ", wa.fallnr, wa.mergeid, wa.eins, wa.zwei, wa.drei, wa.vier, wa.fuenf, wa.sechs, wa.sieben, wa.acht, wa.neun, wa.zehn);
                logResult("erwartet: ", oneCase.getInputData(false).getFallNr(), oneResult.mergeId, oneResult.cond1, oneResult.cond2, oneResult.cond3,
                        oneResult.cond4, oneResult.cond5, oneResult.cond6, oneResult.cond7, oneResult.cond8, oneResult.cond9, oneResult.cond10);
                assertTrue("Fallnr: " + wa.fallnr + " Fehler in mergeID: erwartet: " + oneResult.mergeId + " bekommen: " + wa.mergeid,
                        oneResult.mergeId != 0 && wa.mergeid != 0 || oneResult.mergeId == 0 && wa.mergeid == 0);
                assertEquals("Fallnr: " + wa.fallnr + " Fehler in der Bedignung 1, erwartet: " + oneResult.cond1 + " bekommen: " + wa.eins, wa.eins, oneResult.cond1);
                assertEquals("Fallnr: " + wa.fallnr + " Fehler in der Bedignung 2, erwartet: " + oneResult.cond2 + " bekommen: " + wa.zwei, wa.zwei, oneResult.cond2);
                assertEquals("Fallnr: " + wa.fallnr + " Fehler in der Bedignung 3, erwartet: " + oneResult.cond3 + " bekommen: " + wa.drei, wa.drei, oneResult.cond3);
                assertEquals("Fallnr: " + wa.fallnr + " Fehler in der Bedignung 4, erwartet: " + oneResult.cond4 + " bekommen: " + wa.vier, wa.vier, oneResult.cond4);
                assertEquals("Fallnr: " + wa.fallnr + " Fehler in der Bedignung 5, erwartet: " + oneResult.cond5 + " bekommen: " + wa.fuenf, wa.fuenf, oneResult.cond5);
                assertEquals("Fallnr: " + wa.fallnr + " Fehler in der Bedignung 6, erwartet: " + oneResult.cond6 + " bekommen: " + wa.sechs, wa.sechs, oneResult.cond6);
                assertEquals("Fallnr: " + wa.fallnr + " Fehler in der Bedignung 7, erwartet: " + oneResult.cond7 + " bekommen: " + wa.sieben, wa.sieben, oneResult.cond7);
                assertEquals("Fallnr: " + wa.fallnr + " Fehler in der Bedignung 8, erwartet: " + oneResult.cond8 + " bekommen: " + wa.acht, wa.acht, oneResult.cond8);
                assertEquals("Fallnr: " + wa.fallnr + " Fehler in der Bedignung 9, erwartet: " + oneResult.cond9 + " bekommen: " + wa.neun, wa.neun, oneResult.cond9);
                assertEquals("Fallnr: " + wa.fallnr + " Fehler in der Bedignung 10, erwartet: " + oneResult.cond10 + " bekommen: " + wa.zehn, wa.zehn, oneResult.cond10);
            } else {
                RmcPeppWiederaufnahme wa = (RmcPeppWiederaufnahme) caseLst.get(i);
                logResult("berechnet: ", wa.fallnr, wa.mergeid, wa.eins, wa.zwei, wa.drei, wa.vier, wa.fuenf, wa.sechs, wa.sieben, wa.acht, wa.neun, wa.zehn);
                logResult("erwartet: ", oneCase.getInputData(true).getFallNr(), oneResult.mergeId, oneResult.cond1, oneResult.cond2, oneResult.cond3,
                        oneResult.cond4, oneResult.cond5, oneResult.cond6, oneResult.cond7, oneResult.cond8, oneResult.cond9, oneResult.cond10);
                assertTrue("Fallnr: " + wa.fallnr + " Fehler in mergeID: erwartet: " + oneResult.mergeId + " bekommen: " + wa.mergeid,
                        oneResult.mergeId != 0 && wa.mergeid != 0 || oneResult.mergeId == 0 && wa.mergeid == 0);
                assertEquals("Fallnr: " + wa.fallnr + " Fehler in der Bedignung 1, erwartet: " + oneResult.cond1 + " bekommen: " + wa.eins, wa.eins, oneResult.cond1);
                assertEquals("Fallnr: " + wa.fallnr + " Fehler in der Bedignung 2, erwartet: " + oneResult.cond2 + " bekommen: " + wa.zwei, wa.zwei, oneResult.cond2);
                assertEquals("Fallnr: " + wa.fallnr + " Fehler in der Bedignung 3, erwartet: " + oneResult.cond3 + " bekommen: " + wa.drei, wa.drei, oneResult.cond3);
                assertEquals("Fallnr: " + wa.fallnr + " Fehler in der Bedignung 4, erwartet: " + oneResult.cond4 + " bekommen: " + wa.vier, wa.vier, oneResult.cond4);
                assertEquals("Fallnr: " + wa.fallnr + " Fehler in der Bedignung 5, erwartet: " + oneResult.cond5 + " bekommen: " + wa.fuenf, wa.fuenf, oneResult.cond5);
                assertEquals("Fallnr: " + wa.fallnr + " Fehler in der Bedignung 6, erwartet: " + oneResult.cond6 + " bekommen: " + wa.sechs, wa.sechs, oneResult.cond6);
                assertEquals("Fallnr: " + wa.fallnr + " Fehler in der Bedignung 7, erwartet: " + oneResult.cond7 + " bekommen: " + wa.sieben, wa.sieben, oneResult.cond7);
                assertEquals("Fallnr: " + wa.fallnr + " Fehler in der Bedignung 8, erwartet: " + oneResult.cond8 + " bekommen: " + wa.acht, wa.acht, oneResult.cond8);
                assertEquals("Fallnr: " + wa.fallnr + " Fehler in der Bedignung 9, erwartet: " + oneResult.cond9 + " bekommen: " + wa.neun, wa.neun, oneResult.cond9);
                assertEquals("Fallnr: " + wa.fallnr + " Fehler in der Bedignung 10, erwartet: " + oneResult.cond10 + " bekommen: " + wa.zehn, wa.zehn, oneResult.cond10);

            }

        }
    }

    private void logResult(String what, String fallnr, int mergeId, int eins, int zwei, int drei, int vier, int fuenf, int sechs, int sieben, int acht, int neun, int zehn) {
        Logger.getLogger(CheckpointReadmissionTest.class.getName()).log(Level.INFO,
                what
                + "fallnr: " + fallnr
                + " mergeId: " + mergeId
                + " eins: " + eins
                + " zwei: " + zwei
                + " drei: " + drei
                + " vier: " + vier
                + "  fuenf: " + fuenf
                + " sechs: " + sechs
                + " sieben: " + sieben
                + " acht: " + acht
                + " neun: " + neun
                + " zehn: " + zehn);
    }

}
