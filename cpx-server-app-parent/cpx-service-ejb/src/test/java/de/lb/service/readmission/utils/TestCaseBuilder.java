/*
 * Copyright (c) 2017 Lohmann & Birkner.
 * NOTICE:  All information contained herein is; and remains
 * the property of Lohmann & Birkner and its suppliers;
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
package de.lb.service.readmission.utils;

import de.checkpoint.server.rmServer.caseManager.RmcPeppWiederaufnahme;
import de.checkpoint.server.rmServer.caseManager.RmcWiederaufnahme;
import de.checkpoint.server.rmServer.caseManager.RmcWiederaufnahmeIF;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * In den Dateien drg.txt und pepp.txt befinden sich die Daten für Test der
 * Faellen die mit Checkpoint berechnet wurden. Ziel des Tests zu überprüfen, ob
 * die gleiche Ergebnisse rauskommen. In der ersten Zeile jeder Datei steht die
 * erwartete Dateistruktur
 *
 * @author gerschmann
 */
public class TestCaseBuilder {

    private static final SimpleDateFormat m_dateFormatEnglDet = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
    private static final Logger LOG = Logger.getLogger(TestCaseBuilder.class.getName());

    public static ArrayList<TestReadmissionCase> getReadmissionTestList(boolean isPepp) {
        if (isPepp) {
            return getPeppReadmissionTestList();
        } else {
            return getDrgReadmissionTestList();
        }

    }

    private static ArrayList<TestReadmissionCase> getDrgReadmissionTestList() {
        return getReadmissionTestList("file/drg.txt");
    }

    private static ArrayList<TestReadmissionCase> getPeppReadmissionTestList() {

        return getReadmissionTestList("file/pepp.txt");

    }

    /**
     * liest die vorgegebenen Beispieldaten aus der Resources Datei
     *
     * @param fileName name der Datei
     * @return
     */
    private static ArrayList<TestReadmissionCase> getReadmissionTestList(String fileName) {
        ArrayList<TestReadmissionCase> drgList = new ArrayList<>();
        ClassLoader classLoader = TestCaseBuilder.class.getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        int i = 0;

        try ( Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Logger.getLogger(TestCaseBuilder.class.getName()).log(Level.INFO, line);
                if (i == 0) {
                    i++; // skip header
                    continue;
                }
                String[] parts = line.split(";");
                drgList.add(new TestReadmissionCase(
                        i,
                        parts[1], // ikz
                        parts[3], // fallnr
                        parts[2], // patNr
                        parseDate(parts[4]), // aufn. datum
                        parseDate(parts[5]), // entl. Datum
                        parts[6], //drg
                        parts[7], // mdc
                        parts[8], // partition
                        parseInt(parts[9]), // ogvd        
                        parseBoolean(parts[10]), // is catalog Exception
                        parseBoolean(parts[11]), // aufnahme wegen Komplikation
                        parseInt(parts[12]), // mergeId
                        parseInt(parts[13]), // 1
                        parseInt(parts[14]), // 2
                        parseInt(parts[15]), // 3
                        parseInt(parts[16]), // 4
                        parseInt(parts[17]), // 5
                        parseInt(parts[18]), // 6
                        parseInt(parts[19]), // 7
                        parseInt(parts[20]), // 8
                        parseInt(parts[21]), // 9
                        parseInt(parts[22]) // 10
                ));
                i++;
            }

            scanner.close();

        } catch (IOException e) {
            Logger.getLogger(TestCaseBuilder.class.getName()).log(Level.WARNING, e.getMessage());
        }

        return drgList;

    }

    private static Date parseDate(String dt) {
        try {
            return dt.length() == 0 ? null : new Date(m_dateFormatEnglDet.parse(dt).getTime());
        } catch (ParseException e) {
            Logger.getLogger(TestCaseBuilder.class.getName()).log(Level.WARNING, e.getMessage());
        }
        return null;
    }

    private static int parseInt(String i) {
        try {
            return i.length() == 0 ? 0
                    : Integer.parseInt(i);
        } catch (NumberFormatException e) {
            Logger.getLogger(TestCaseBuilder.class.getName()).log(Level.WARNING, e.getMessage());
        }
        return 0;
    }

    private static boolean parseBoolean(String b) {
        return b.trim().length() == 0 ? false : b.trim().equals("1");
    }

    public static class TestReadmissionCase {

        TestReadmissionCaseInput m_input;
        TestReadmissionCaseResult m_result;

        public TestReadmissionCase(int id, String ikz, String fallnr, String patNr,
                Date admDate, Date disDate, String drg, String mdc,
                String partition,
                int ogvd,
                boolean catException,
                boolean readmCompl,
                int mergeId,
                int cond1,
                int cond2,
                int cond3,
                int cond4,
                int cond5,
                int cond6,
                int cond7,
                int cond8,
                int cond9,
                int cond10) {
            m_input = new TestReadmissionCaseInput(id, ikz, fallnr, patNr, admDate, disDate, drg, mdc, partition, ogvd, catException, readmCompl);
            m_result = new TestReadmissionCaseResult(mergeId, cond1, cond2, cond3, cond4, cond5, cond6, cond7, cond8, cond9, cond10);

        }

        public RmcWiederaufnahmeIF getInputData(boolean isPepp) {
            if (!isPepp) {
                RmcWiederaufnahme ret = new RmcWiederaufnahme();
                ret.kasse = m_input.ikz;
                ret.fallnr = m_input.fallnr;
                ret.setDrgOrPepp(m_input.drg);
                ret.mdc = m_input.mdc;
                ret.m_oGVD = m_input.ogvd;
                ret.m_partition = m_input.partition.charAt(0);
                ret.m_drgKatalogWAAusnahme = m_input.catException;
                ret.m_aufnahmeWgKomplikation = m_input.readmCompl;
                ret.aufnahmedatum = m_input.admDate;
                ret.entlassungsdatum = m_input.disDate;
                return ret;

            } else {

                RmcPeppWiederaufnahme ret = new RmcPeppWiederaufnahme();
                ret.iknr = m_input.ikz;
                ret.fallnr = m_input.fallnr;
                ret.pepp = m_input.drg;
                ret.sk = m_input.mdc;

                ret.m_teilstationaer = m_input.catException ? 1 : 0;
                ret.m_verhandelbar = m_input.readmCompl ? 1 : 0;
                ret.aufnahmedatum = m_input.admDate;
                ret.entlassungsdatum = m_input.disDate;
                return ret;
            }

        }

        public TestReadmissionCaseResult getOutputData() {
            return m_result;
        }

    }

    public static class TestReadmissionCaseInput {

        int id;
        String ikz;
        String fallnr;
        String patNr;
        Date admDate;
        Date disDate;
        String drg;
        String mdc;
        String partition;
        int ogvd;
        boolean catException;
        boolean readmCompl;

        public TestReadmissionCaseInput(int id, String ikz, String fallnr, String patNr,
                Date admDate, Date disDate, String drg, String mdc,
                String partition,
                int ogvd,
                boolean catException,
                boolean readmCompl) {
            this.id = id;
            this.ikz = ikz;
            this.fallnr = fallnr;
            this.patNr = patNr;
            this.admDate = admDate;
            this.disDate = disDate;
            this.drg = drg;
            this.mdc = mdc;
            this.partition = partition;
            this.ogvd = ogvd;
            this.catException = catException;
            this.readmCompl = readmCompl;

        }

    }

    public static class TestReadmissionCaseResult {

        public int mergeId;
        public int cond1;
        public int cond2;
        public int cond3;
        public int cond4;
        public int cond5;
        public int cond6;
        public int cond7;
        public int cond8;
        public int cond9;
        public int cond10;

        public TestReadmissionCaseResult(int mergeId,
                int cond1,
                int cond2,
                int cond3,
                int cond4,
                int cond5,
                int cond6,
                int cond7,
                int cond8,
                int cond9,
                int cond10) {

            this.mergeId = mergeId;
            this.cond1 = cond1;
            this.cond2 = cond2;
            this.cond3 = cond3;
            this.cond4 = cond4;
            this.cond5 = cond5;
            this.cond6 = cond6;
            this.cond7 = cond7;
            this.cond8 = cond8;
            this.cond9 = cond9;
            this.cond10 = cond10;

        }

    }
}
