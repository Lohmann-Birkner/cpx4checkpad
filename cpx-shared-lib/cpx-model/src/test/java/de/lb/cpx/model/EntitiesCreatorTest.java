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

import de.lb.cpx.model.enums.AdmissionModeEn;
import de.lb.cpx.model.enums.IcdcTypeEn;
import de.lb.cpx.model.enums.LocalisationEn;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 *
 * @author gerschmann
 */
public class EntitiesCreatorTest {

    private static final Logger LOG = Logger.getLogger(EntitiesCreatorTest.class.getName());

    /**
     * creates Icd
     *
     * @param code code
     * @param isHdx is case main icd
     * @param isHdb is department main icd
     * @param itype icd reference type
     * @param loc location
     * @param used use for grouping?
     * @return icd
     */
    public static TCaseIcd createIcd(String code, boolean isHdx, boolean isHdb, IcdcTypeEn itype, LocalisationEn loc, boolean used) {
        TCaseIcd icd = new TCaseIcd();
        icd.setIcdcCode(code);
        icd.setIcdcIsHdxFl(isHdx);
        icd.setIcdcIsHdbFl(isHdb);
        icd.setIcdcTypeEn(itype);
        icd.setIcdcLocEn(loc);
        icd.setIcdIsToGroupFl(used);
        return icd;
    }

    public static TCaseOps createOps(String code, Date date, LocalisationEn loc, boolean used) {
        TCaseOps ops = new TCaseOps();

        ops.setOpscCode(code);
        ops.setOpscDatum(date);
        ops.setOpscLocEn(loc);
        ops.setOpsIsToGroupFl(used);

        return ops;
    }

    public static TCaseWard createWard(String ident, Date admDate, Date disDate, final Set<TCaseIcd> icds, final Set<TCaseOps> opses) {
        TCaseWard ward = new TCaseWard();
        ward.setWardcIdent(ident);
        ward.setWardcAdmdate(admDate);
        ward.setWardcDisdate(disDate);
        ward.setCaseIcds(icds);
        ward.setCaseOpses(opses);
        return ward;
    }

    public static TCaseDepartment createDepartment(String ident, Date admDate, Date disDate,
            final Set<TCaseWard> wards, final Set<TCaseIcd> icds, final Set<TCaseOps> opses) {
        TCaseDepartment department = new TCaseDepartment();
        department.setDepShortName(ident);
        department.setDepKey301(ident);
        department.setDepcAdmDate(admDate);
        department.setDepcDisDate(disDate);
        department.setCaseWards(wards);
        department.setCaseIcds(icds);
        department.setCaseOpses(opses);

        return department;
    }

    public static TCaseDepartment createDepartment(String ident, Date admDate, Date disDate,
            final AdmissionModeEn admMode) {
        TCaseDepartment department = createDepartment(ident, admDate, disDate, null, null, null);
        department.setDepcAdmodEn(admMode);

        return department;
    }

    public static TCaseFee createFee(String key, double value, int count, Date from, Date to) {
        TCaseFee fee = new TCaseFee();
        fee.setFeecFeekey(key);
        fee.setFeecFrom(from);
        fee.setFeecTo(to);
        fee.setFeecValue(value);
        return fee;

    }

    public static TCaseBill createBill() {
        TCaseBill bill = new TCaseBill();

        return bill;

    }

    public static TCaseDetails createCaseDetails() {
        TCaseDetails details = new TCaseDetails();

        return details;
    }

    public static TCase createCase() {
        TCase cs = new TCase();

        return cs;
    }

    public static TPatient createPatient() {
        TPatient pat = new TPatient();

        return pat;
    }

    public static Set<TCaseIcd> getPrimaryIcdTestList() {
        Set<TCaseIcd> icds = new HashSet<>();
        icds.add(EntitiesCreatorTest.createIcd("M16.7", true, true, IcdcTypeEn.Behandlung, LocalisationEn.B, true));
        icds.add(EntitiesCreatorTest.createIcd("R12", false, false, IcdcTypeEn.Behandlung, null, true));
        return icds;
    }

    public static Set<TCaseOps> getPrimaryOpsTestList(Date dat1, Date dat2) {
        Set<TCaseOps> opss = new HashSet<>();
        opss.add(EntitiesCreatorTest.createOps("5-820.00", dat1, LocalisationEn.B, true));
        opss.add(EntitiesCreatorTest.createOps("5-820.10", dat2, LocalisationEn.B, true));
        return opss;

    }

    public static Set<TCaseWard> getPrimaryWardTestList(Date dat1, Date dat2) {
        Set<TCaseWard> wards = new HashSet<>();
        wards.add(createWard("stat1", dat1, dat1, getPrimaryIcdTestList(), getPrimaryOpsTestList(dat1, dat1)));
        wards.add(createWard("stat2", dat1, dat2, getPrimaryIcdTestList(), getPrimaryOpsTestList(dat1, dat2)));
        wards.add(createWard("stat3", dat2, dat2, getPrimaryIcdTestList(), getPrimaryOpsTestList(dat2, dat2)));
        return wards;
    }
}
