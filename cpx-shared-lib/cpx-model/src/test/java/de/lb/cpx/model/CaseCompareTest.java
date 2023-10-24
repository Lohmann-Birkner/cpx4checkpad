/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.model;

import de.lb.cpx.model.enums.IcdcTypeEn;
import de.lb.cpx.model.enums.LocalisationEn;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class CaseCompareTest {

    private static final Calendar m_calendar = Calendar.getInstance();
    private static final Logger LOG = Logger.getLogger(CaseCompareTest.class.getName());

    @Test
    public void testCompareIcdsEquals() {
        Date dat = new Date();
        TCaseIcd icd1 = EntitiesCreatorTest.createIcd("M16.7", true, true, IcdcTypeEn.Behandlung, LocalisationEn.B, true);
        icd1.setCreationDate(dat);
        TCaseIcd icd2 = EntitiesCreatorTest.createIcd("M16.7", true, true, IcdcTypeEn.Behandlung, LocalisationEn.B, true);
        icd2.setCreationDate(new Date(dat.getTime() + 10000));
        boolean b = icd1.versionEquals(icd2);
        assertTrue("compare icds equals", b);

    }

    @Test
    public void testCompareNotIcdsEquals() {
        // different codes

        TCaseIcd icd1 = EntitiesCreatorTest.createIcd("M16.7", true, true, IcdcTypeEn.Behandlung, LocalisationEn.B, true);

        TCaseIcd icd2 = EntitiesCreatorTest.createIcd("M16.1", true, true, IcdcTypeEn.Behandlung, LocalisationEn.B, true);

        boolean b = icd1.versionEquals(icd2);
        // different ICD type
        assertTrue("compare icds not equals, different codes", !b);
        icd1 = EntitiesCreatorTest.createIcd("M16.7", true, true, IcdcTypeEn.Behandlung, LocalisationEn.B, true);
        icd2 = EntitiesCreatorTest.createIcd("M16.7", true, true, IcdcTypeEn.Aufnahme, LocalisationEn.B, true);
        b = icd1.versionEquals(icd2);
        assertTrue("compare icds not equals, different icd type", !b);
        // different localisation
        icd1 = EntitiesCreatorTest.createIcd("M16.7", true, true, IcdcTypeEn.Behandlung, LocalisationEn.B, true);
        icd2 = EntitiesCreatorTest.createIcd("M16.7", true, true, IcdcTypeEn.Behandlung, null, true);
        b = icd1.versionEquals(icd2);
        assertTrue("compare icds not equals, different localisation", !b);
        // different to group flag
        icd1 = EntitiesCreatorTest.createIcd("M16.7", true, true, IcdcTypeEn.Behandlung, LocalisationEn.B, true);
        icd2 = EntitiesCreatorTest.createIcd("M16.7", true, true, IcdcTypeEn.Behandlung, LocalisationEn.B, false);
        b = icd1.versionEquals(icd2);
        assertTrue("compare icds not equals, different group flags", !b);
        // different hdx flag
        icd1 = EntitiesCreatorTest.createIcd("M16.7", false, true, IcdcTypeEn.Behandlung, LocalisationEn.B, true);
        icd2 = EntitiesCreatorTest.createIcd("M16.7", true, true, IcdcTypeEn.Behandlung, LocalisationEn.B, true);
        b = icd1.versionEquals(icd2);
        assertTrue("compare icds not equals, different hdx flag", !b);
        // different different hdb flag
        icd1 = EntitiesCreatorTest.createIcd("M16.7", true, false, IcdcTypeEn.Behandlung, LocalisationEn.B, true);
        icd2 = EntitiesCreatorTest.createIcd("M16.7", true, true, IcdcTypeEn.Behandlung, LocalisationEn.B, true);
        b = icd1.versionEquals(icd2);
        assertTrue("compare icds not equals, different hdb flag", !b);

    }

    @Test
    public void testCompareOpsEquals() {

        m_calendar.set(2016, 1, 10, 10, 10, 10);
        TCaseOps ops1 = EntitiesCreatorTest.createOps("5-820.00", m_calendar.getTime(), LocalisationEn.B, true);
        TCaseOps ops2 = EntitiesCreatorTest.createOps("5-820.00", m_calendar.getTime(), LocalisationEn.B, true);
        boolean b = ops1.versionEquals(ops2);
        assertTrue("compare ops equals", b);

    }

    @Test
    public void testCompareOpsNotEquals() {

        m_calendar.set(2016, 1, 10, 10, 10, 10);
        Date date = m_calendar.getTime();
// localisation        
        TCaseOps ops1 = EntitiesCreatorTest.createOps("5-820.00", date, LocalisationEn.B, true);
        TCaseOps ops2 = EntitiesCreatorTest.createOps("5-820.00", date, LocalisationEn.L, true);
        boolean b = ops1.versionEquals(ops2);
        assertTrue("compare ops not equals, different localisation", !b);
// code
        ops1 = EntitiesCreatorTest.createOps("5-820.00", date, LocalisationEn.B, true);
        ops2 = EntitiesCreatorTest.createOps("5-820.01", date, LocalisationEn.B, true);
        b = ops1.versionEquals(ops2);
        assertTrue("compare ops not equals, different code", !b);

// groupflag
        ops1 = EntitiesCreatorTest.createOps("5-820.00", date, LocalisationEn.B, false);
        ops2 = EntitiesCreatorTest.createOps("5-820.00", date, LocalisationEn.B, true);
        b = ops1.versionEquals(ops2);
        assertTrue("compare ops not equals, different group flag", !b);

// date
        m_calendar.add(Calendar.HOUR, 2);
        ops1 = EntitiesCreatorTest.createOps("5-820.00", date, LocalisationEn.B, true);
        ops2 = EntitiesCreatorTest.createOps("5-820.00", m_calendar.getTime(), LocalisationEn.B, true);
        b = ops1.versionEquals(ops2);
        assertTrue("compare ops not equals, different timestamp", !b);

    }

    @Test
    public void testWardCompareEquals() {
// no ops, no icds
        m_calendar.set(2016, 1, 10, 10, 10, 10);
        Date adm = m_calendar.getTime();
        m_calendar.add(Calendar.DAY_OF_YEAR, 2);
        Date dis = m_calendar.getTime();
        TCaseWard ward1 = EntitiesCreatorTest.createWard("stat1", adm, dis, null, null);
        TCaseWard ward2 = EntitiesCreatorTest.createWard("stat1", adm, dis, null, null);
        boolean b = ward1.versionEquals(ward2);
// empty or null        
        assertTrue("compare wards equals, no ops and no icd", b);
        ward1 = EntitiesCreatorTest.createWard("stat1", adm, dis, new HashSet<>(), null);
        ward2 = EntitiesCreatorTest.createWard("stat1", adm, dis, null, new HashSet<>());
        b = ward1.versionEquals(ward2);
        assertTrue("compare wards equals", b);
// icds
        Set<TCaseIcd> icds1 = EntitiesCreatorTest.getPrimaryIcdTestList();
        Set<TCaseIcd> icds2 = EntitiesCreatorTest.getPrimaryIcdTestList();
        ward1 = EntitiesCreatorTest.createWard("stat1", adm, dis, icds1, null);
        ward2 = EntitiesCreatorTest.createWard("stat1", adm, dis, icds2, null);
        b = ward1.versionEquals(ward2);
        assertTrue("compare wards equals", b);
// ops
        Set<TCaseOps> opss1 = EntitiesCreatorTest.getPrimaryOpsTestList(adm, dis);
        Set<TCaseOps> opss2 = EntitiesCreatorTest.getPrimaryOpsTestList(adm, dis);
        ward1 = EntitiesCreatorTest.createWard("stat1", adm, dis, new HashSet<>(), opss1);
        ward2 = EntitiesCreatorTest.createWard("stat1", adm, dis, new HashSet<>(), opss2);
        b = ward1.versionEquals(ward2);
        assertTrue("compare wards equals", b);
//ops and icd        
        ward1 = EntitiesCreatorTest.createWard("stat1", adm, dis, icds1, opss1);
        ward2 = EntitiesCreatorTest.createWard("stat1", adm, dis, icds2, opss2);
        b = ward1.versionEquals(ward2);
        assertTrue("compare wards equals", b);
    }

    @Test
    public void testWardCompareNotEquals() {
// no ops, no icds
        m_calendar.set(2016, 1, 10, 10, 10, 10);
        Date adm = m_calendar.getTime();
        m_calendar.add(Calendar.DAY_OF_YEAR, 2);
        Date dis = m_calendar.getTime();

        Set<TCaseOps> opss1 = EntitiesCreatorTest.getPrimaryOpsTestList(adm, dis);

        Set<TCaseOps> opss2 = EntitiesCreatorTest.getPrimaryOpsTestList(adm, dis);

        Set<TCaseIcd> icds1 = EntitiesCreatorTest.getPrimaryIcdTestList();

        Set<TCaseIcd> icds2 = EntitiesCreatorTest.getPrimaryIcdTestList();
// different ident        
        TCaseWard ward1 = EntitiesCreatorTest.createWard("stat1", adm, dis, icds1, opss1);
        TCaseWard ward2 = EntitiesCreatorTest.createWard("stat2", adm, dis, icds2, opss2);
        boolean b = ward1.versionEquals(ward2);
        assertTrue("compare wards not equals, ident", !b);
// different admission date        
        ward1 = EntitiesCreatorTest.createWard("stat1", adm, dis, icds1, opss1);
        ward2 = EntitiesCreatorTest.createWard("stat1", dis, dis, icds2, opss2);
        b = ward1.versionEquals(ward2);
        assertTrue("compare wards not equals, admission date", !b);
// different discharge date        
        ward1 = EntitiesCreatorTest.createWard("stat1", adm, dis, icds1, opss1);
        ward2 = EntitiesCreatorTest.createWard("stat1", adm, adm, icds2, opss2);
        b = ward1.versionEquals(ward2);
        assertTrue("compare wards not equals, discharge date", !b);
// different icd - lists
        icds1.add(EntitiesCreatorTest.createIcd("R12", false, false, IcdcTypeEn.Aufnahme, null, true));
        ward1 = EntitiesCreatorTest.createWard("stat1", adm, dis, icds1, opss1);
        icds2.add(EntitiesCreatorTest.createIcd("R12", false, false, IcdcTypeEn.Behandlung, null, true));
        ward2 = EntitiesCreatorTest.createWard("stat1", adm, dis, icds2, opss2);
        b = ward1.versionEquals(ward2);
        assertTrue("compare wards not equals, different icd lists", !b);
// different ops - lists
        opss1.add(EntitiesCreatorTest.createOps("5-820.00", dis, null, false));
        ward1 = EntitiesCreatorTest.createWard("stat1", adm, dis, icds1, opss1);
        ward2 = EntitiesCreatorTest.createWard("stat1", adm, dis, EntitiesCreatorTest.getPrimaryIcdTestList(), opss2);
        b = ward1.versionEquals(ward2);
        assertTrue("compare wards not equals, different ops lists", !b);
// different ops and icd - lists
        opss2.add(EntitiesCreatorTest.createOps("5-820.10", dis, null, false));
        ward1 = EntitiesCreatorTest.createWard("stat1", adm, dis, icds1, opss1);
        ward2 = EntitiesCreatorTest.createWard("stat1", adm, dis, icds2, opss2);
        b = ward1.versionEquals(ward2);
        assertTrue("compare wards not equals, different icd and ops lists", !b);
    }

    @Test
    public void testDepartmentCompareEquals() {
// no ops, no icds, no Wards (Empty or null
        m_calendar.set(2016, 1, 10, 10, 10, 10);
        Date adm = m_calendar.getTime();
        m_calendar.add(Calendar.DAY_OF_YEAR, 2);
        Date dis = m_calendar.getTime();
        TCaseDepartment dep1 = EntitiesCreatorTest.createDepartment("2900", adm, dis, new HashSet<>(), null, new HashSet<>());
        TCaseDepartment dep2 = EntitiesCreatorTest.createDepartment("2900", adm, dis, new HashSet<>(), null, new HashSet<>());
        boolean b = dep1.versionEquals(dep2);
        assertTrue("compare departments equals", b);
// icds
        Set<TCaseIcd> icds1 = EntitiesCreatorTest.getPrimaryIcdTestList();
        Set<TCaseIcd> icds2 = EntitiesCreatorTest.getPrimaryIcdTestList();
        dep1 = EntitiesCreatorTest.createDepartment("2900", adm, dis, null, icds1, null);
        dep2 = EntitiesCreatorTest.createDepartment("2900", adm, dis, null, icds2, null);
        b = dep1.versionEquals(dep2);
        assertTrue("compare departments equals", b);
// ops
        Set<TCaseOps> opss1 = EntitiesCreatorTest.getPrimaryOpsTestList(adm, dis);
        Set<TCaseOps> opss2 = EntitiesCreatorTest.getPrimaryOpsTestList(adm, dis);
        dep1 = EntitiesCreatorTest.createDepartment("2900", adm, dis, null, null, opss1);
        dep2 = EntitiesCreatorTest.createDepartment("2900", adm, dis, null, null, opss2);
        b = dep1.versionEquals(dep2);
        assertTrue("compare departments equals", b);
//ops and icd        
        dep1 = EntitiesCreatorTest.createDepartment("2900", adm, dis, null, icds1, opss1);
        dep2 = EntitiesCreatorTest.createDepartment("2900", adm, dis, null, icds2, opss2);
        b = dep1.versionEquals(dep2);
        assertTrue("compare departments equals", b);

// wards
        Set<TCaseWard> ward1 = EntitiesCreatorTest.getPrimaryWardTestList(adm, dis);
        Set<TCaseWard> ward2 = EntitiesCreatorTest.getPrimaryWardTestList(adm, dis);
        dep1 = EntitiesCreatorTest.createDepartment("2900", adm, dis, ward1, icds1, opss1);
        dep2 = EntitiesCreatorTest.createDepartment("2900", adm, dis, ward2, icds2, opss2);
        b = dep1.versionEquals(dep2);
        assertTrue("compare departments equals", b);
    }

    @Test
    public void testDepartmentCompareNotEquals() {
        m_calendar.set(2016, 1, 10, 10, 10, 10);
        Date adm = m_calendar.getTime();
        m_calendar.add(Calendar.DAY_OF_YEAR, 2);
        Date dis = m_calendar.getTime();
        Set<TCaseWard> ward1 = EntitiesCreatorTest.getPrimaryWardTestList(adm, dis);
        ward1.add(EntitiesCreatorTest.createWard("stat3", dis, dis, EntitiesCreatorTest.getPrimaryIcdTestList(), EntitiesCreatorTest.getPrimaryOpsTestList(dis, dis)));
        Set<TCaseWard> ward2 = EntitiesCreatorTest.getPrimaryWardTestList(adm, dis);
        Set<TCaseOps> ops = EntitiesCreatorTest.getPrimaryOpsTestList(dis, dis);
        ops.add(EntitiesCreatorTest.createOps("5-820.00", dis, LocalisationEn.B, true));
        ward2.add(EntitiesCreatorTest.createWard("stat3", dis, dis,
                EntitiesCreatorTest.getPrimaryIcdTestList(),
                ops));
        TCaseDepartment dep1 = EntitiesCreatorTest.createDepartment("2900", adm, dis, ward1, new HashSet<>(), null);
        TCaseDepartment dep2 = EntitiesCreatorTest.createDepartment("2900", adm, dis, ward2, null, new HashSet<>());
        boolean b = dep1.versionEquals(dep2);
        assertTrue("compare departments not equals", b);
    }
}
