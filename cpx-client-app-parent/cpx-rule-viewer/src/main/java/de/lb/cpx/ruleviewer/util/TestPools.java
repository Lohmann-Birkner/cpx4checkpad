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
package de.lb.cpx.ruleviewer.util;

import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author wilde
 */
public class TestPools {

    private static TestPools INSTANCE;

    private TestPools() {

    }

    public static TestPools instance() {
        if (INSTANCE == null) {
            INSTANCE = new TestPools();
        }
        return INSTANCE;
    }
    private List<CrgRuleTables> tables;

    public List<CrgRuleTables> getAllRuleTables() {
        if (tables == null) {
            tables = new ArrayList<>();
            CrgRuleTables table1 = new CrgRuleTables();
            table1.setCreationUser(1L);
            table1.setCrgtContent("test1,"
                    + "test2,"
                    + "test3,"
                    + "test4");
            table1.setCrgtTableName("DIABETES");
            CrgRuleTables table2 = new CrgRuleTables();
            table2.setCreationUser(3L);
            table2.setCrgtTableName("Ops-Tabelle");
            table2.setCrgtContent("1-20a,"
                    + "1-220,"
                    + "1-212.0,"
                    + "1-279.51,"
                    + "5-785.0q,"
                    + "5-812.kq,"
                    + "6-005.ge,"
                    + "9-984.7");
            CrgRuleTables table3 = new CrgRuleTables();
            table3.setCrgtTableName("Icd-Tabelle");
            table3.setCreationUser(0L);
            table3.setCrgtContent("Z99.4,"
                    + "U04.9,"
                    + "L90.0,"
                    + "L89.91,"
                    + "C44.9,"
                    + "B96.0,"
                    + "A02.0");
            CrgRuleTables table4 = new CrgRuleTables();
            table4.setCrgtTableName("CCL");
            table4.setCrgtContent("A02.0,"
                    + "A02.1,"
                    + "A04.3,"
                    + "A04.7%,"
                    + "A04.9,"
                    + "A07.1,"
                    + "A07.3,"
                    + "A08.0,"
                    + "A08.1,"
                    + "A09.0,"
                    + "A15.0,"
                    + "A15.1,"
                    + "A15.2,"
                    + "A15.3,"
                    + "A15.4,"
                    + "A15.5,"
                    + "A15.6,"
                    + "A15.7,"
                    + "A15.8,"
                    + "A15.9,"
                    + "A16.0,"
                    + "A16.1,"
                    + "A16.2,"
                    + "A16.3,"
                    + "A16.4,"
                    + "A16.5,"
                    + "A16.7,"
                    + "A16.8,"
                    + "A16.9,"
                    + "A17.0,"
                    + "A17.1,"
                    + "A17.8,"
                    + "A17.9,"
                    + "A18.0,"
                    + "A18.1,"
                    + "A18.2,"
                    + "A18.3,"
                    + "A18.5,"
                    + "A18.6,"
                    + "A18.7,"
                    + "A18.8,"
                    + "A19.0,"
                    + "A19.1,"
                    + "A19.2,"
                    + "A19.8,"
                    + "A19.9,"
                    + "A31.0,"
                    + "A31.80,"
                    + "A32.1,"
                    + "A32.7,"
                    + "A34,"
                    + "A35,"
                    + "A39.0,"
                    + "A39.1,"
                    + "A39.2,"
                    + "A39.3,"
                    + "A39.4,"
                    + "A39.5,"
                    + "A39.8,"
                    + "A39.9,"
                    + "A40.0,"
                    + "A40.1,"
                    + "A40.2,"
                    + "A40.3,"
                    + "A40.8,"
                    + "A40.9,"
                    + "A41.0,"
                    + "A41.1,"
                    + "A41.2,"
                    + "A41.3,"
                    + "A41.4,"
                    + "A41.51,"
                    + "A41.52,"
                    + "A41.58,"
                    + "A41.8,"
                    + "A41.9,"
                    + "A42.0,"
                    + "A42.1,"
                    + "A42.7,"
                    + "A43.0,"
                    + "A48.0,"
                    + "A48.1,"
                    + "A48.2,"
                    + "A48.3,"
                    + "A49.0,"
                    + "A49.1,"
                    + "A49.2,"
                    + "A50.4,"
                    + "A52.0");//getCCLTestValues());
            table4.setCreationUser(2L);

            tables.add(table1);
            tables.add(table2);
            tables.add(table3);
            tables.add(table4);
        }
        return tables;
    }

    public CrgRuleTables findRuleTables(String pName) {
        pName = pName.replace("'", "");
        for (CrgRuleTables table : getAllRuleTables()) {
            if (pName.toLowerCase().equals(table.getCrgtTableName().toLowerCase())) {
                return table;
            }
        }
        return null;
    }

}
