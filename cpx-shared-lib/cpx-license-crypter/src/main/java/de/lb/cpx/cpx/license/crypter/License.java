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
 *    2018  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.cpx.license.crypter;

import static de.lb.cpx.cpx.license.crypter.LicenseWriter.DEFAULT_ENCODING;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author niemeier
 */
public final class License implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = Logger.getLogger(License.class.getSimpleName());

    private final LicenseCustomerEn customer;
    private final String custName;
    private final Date validDate;
    private final Long caseLimit;

    private final Set<String> hospList;
    private final Set<String> insCompList;
    private final Set<String> deptList;
    private Set<String> moduleList;
    private final boolean allowAllHospitals;

    private boolean drgModule = false;
    private boolean dataModule = false;
    private boolean fmModule = false;
    private boolean gkModule = false;
    private boolean budgetModule = false;
    private boolean morbiRsaModule = false;
    private boolean gkvmModule = false;
    private boolean peppModule = false;
    private boolean acgModule = false;

    private License(final Date pValidDate, final LicenseCustomerEn pCustomer,
            final String pCustomerName,
            final Long pCaseLimit,
            final Set<String> pHospitals, final Set<String> pInsurances,
            final Set<String> pDepartments, final Set<String> pModules,
            final boolean pAllowAllHospitals) {
        validDate = pValidDate == null ? null : new Date(pValidDate.getTime());
        customer = pCustomer;
        custName = pCustomerName;
        caseLimit = pCaseLimit;
        hospList = Collections.unmodifiableSet(pHospitals);
        insCompList = Collections.unmodifiableSet(pInsurances);
        deptList = Collections.unmodifiableSet(pDepartments);
        moduleList = Collections.unmodifiableSet(pModules);
        allowAllHospitals = pAllowAllHospitals;

        drgModule = (moduleList.contains(ModuleManager.PARAMETER_DRG));
        fmModule = moduleList.contains(ModuleManager.PARAMETER_FM);
        dataModule = moduleList.contains(ModuleManager.PARAMETER_DATA);
        budgetModule = moduleList.contains(ModuleManager.PARAMETER_BUDGET);
        gkModule = moduleList.contains(ModuleManager.PARAMETER_GK);
        morbiRsaModule = moduleList.contains(ModuleManager.PARAMETER_MRSA);
        gkvmModule = moduleList.contains(ModuleManager.PARAMETER_GKVM); 
        peppModule = moduleList.contains(ModuleManager.PARAMETER_PEPP);
        acgModule = moduleList.contains(ModuleManager.PARAMETER_ACG);
        if (!isValid()) {
            Date vdt = getValidDate();
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            LOG.log(Level.WARNING, "License is invalid, because validation date is " + (vdt == null ? "NULL" : sdf.format(vdt)));
        }
    }

    public static License loadFromLicenseFile(final String pLicFilename) {
        if (pLicFilename == null || pLicFilename.trim().isEmpty()) {
            throw new IllegalArgumentException("No license file path passed");
        }
        File f = new File(pLicFilename);
        return loadFromLicenseFile(f);
    }

    public static License loadFromLicenseFile(final File pLicFile) {
        try {
            String[] licData = readLicenseFile(pLicFile);
            return loadFromLicenseData(licData);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "License file seems to be corrupted: " + pLicFile.getAbsolutePath(), ex);
            return null;
        }
    }

    public static String[] readLicenseFile(final File pLicFile) throws IOException {
        if (pLicFile == null) {
            throw new IllegalArgumentException("License file is null!");
        }
        LOG.log(Level.INFO, "Read license from file: " + pLicFile.getAbsolutePath());
        if (!pLicFile.exists()) {
            throw new IllegalArgumentException("No license file found in: " + pLicFile.getAbsolutePath());
        }
        if (!pLicFile.isFile()) {
            throw new IllegalArgumentException("This is not a license file: " + pLicFile.getAbsolutePath());
        }
        if (!pLicFile.canRead()) {
            throw new IllegalArgumentException("This license file is not readable: " + pLicFile.getAbsolutePath());
        }
        List<String> list = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(pLicFile), DEFAULT_ENCODING))) {
            //sb = new StringBuffer();
            String line;
            while ((line = in.readLine()) != null) {
                //String tt = new String(LicenseDecrypter.toByteArray(line), "Cp1252");
                //String tt = new String(LicenseDecrypter.toByteArray(line), "Cp1252");
                //sb.append(line.trim());
                list.add(line.trim());
            }
        }
        String[] lines = new String[list.size()];
        list.toArray(lines);
        return lines;
    }

    public static License loadFromLicenseData(final String pLicData) throws IOException {
        if (pLicData == null || pLicData.trim().isEmpty()) {
            throw new IllegalArgumentException("No license data passed!");
        }
        String[] licData = pLicData.split("\\r?\\n");
        return loadFromLicenseData(licData);
    }

    public static License loadFromLicenseData(final String[] pLicData) throws IOException {
        if (pLicData == null || pLicData.length == 0) {
            throw new IllegalArgumentException("No license data passed!");
        }
        for (int i = 0; i < pLicData.length; i++) {
            pLicData[i] = new String(LicenseDecrypter.toByteArray(pLicData[i]), "Cp1252");
        }
        //ReadWriteLicenseFile lic = new ReadWriteLicenseFile();
        LicenseDecrypter decrypter = LicenseDecrypter.getInstance();

        Date validDate = null;
        String customerName = "";
        LicenseCustomerEn customer = null;
        Long caseLimit = null;
        Set<String> modules = new TreeSet<>();
        Set<String> hospitals = new TreeSet<>();
        Set<String> insurances = new TreeSet<>();
        Set<String> departments = new TreeSet<>();
        boolean allowAllHospitals = false;

        try (BufferedReader in = new BufferedReader(new StringReader(String.join("", pLicData)))) {
            String line = readLine(in, decrypter);
            if (line != null) {
                String[] dtm = line.split("-"); // dd-MM-yyyy
                if (dtm.length >= 3) {
                    int dd = Integer.parseInt(dtm[0]);
                    int mm = Integer.parseInt(dtm[1]) - 1;
                    int yr = Integer.parseInt(dtm[2]);
                    GregorianCalendar calc = new GregorianCalendar();
                    calc.setLenient(false);
                    calc.set(Calendar.DAY_OF_MONTH, dd);
                    calc.set(Calendar.MONTH, mm);
                    calc.set(Calendar.YEAR, yr);
                    calc.set(Calendar.HOUR_OF_DAY, 23);
                    calc.set(Calendar.MINUTE, 59);
                    calc.set(Calendar.SECOND, 59);
                    calc.set(Calendar.MILLISECOND, 999);
                    validDate = calc.getTime();
                }
//                hospList = new Vector<>();
                //setHospList(new LinkedHashSet<>());
                line = readLine(in, decrypter);
                if (line != null && line.equals("k alle")) {
                    allowAllHospitals = true;
                    line = readLine(in, decrypter);
                }
                int state = 0;
                while (line != null) {
                    state = getState(line);
                    String val = line.substring(1, line.length()).trim();  //remove first character
                    if (val.isEmpty()) {
                        line = readLine(in, decrypter);
                        continue;
                    }
                    switch (state) {
                        case 1: {
                            //lic.addHospital(val);
                            hospitals.add(val);
                            break;
                        }
                        case 2: {
                            //lic.addInsCompany(val);
                            insurances.add(val);
                            break;
                        }
                        case 3: {
                            //lic.custName = val;
                            customerName = val;
                            break;
                        }
                        case 4: {
                            //lic.addModule(val);
                            modules.add(val);
                            break;
                        }
                        case 5: {
                            //lic.addDepartment(val);
                            departments.add(val);
                            break;
                        }
                        case 6: {
                            //lic.addDepartment(val);
                            for (LicenseCustomerEn c : LicenseCustomerEn.values()) {
                                if (c.name().equalsIgnoreCase(val)) {
                                    customer = c;
                                    break;
                                }
                            }
                            if (customer == null) {
                                LOG.log(Level.SEVERE, "Illegal customer found: " + val);
                            }
                            break;
                        }
                        case 7: {
                            try {
                                caseLimit = Long.parseLong(val);
                            } catch (NumberFormatException ex) {
                                LOG.log(Level.WARNING, MessageFormat.format("invalid case limit found in license: {0}", val), ex);
                            }
                            break;
                        }
                        default:
                            LOG.log(Level.WARNING, "Unknown state found: {0}", state);
                    }
                    line = readLine(in, decrypter);
                }
            }
        }
        return new License(validDate, customer, customerName, caseLimit, hospitals, insurances, departments, modules, allowAllHospitals);
    }

    private static int getState(final String line) {
        int error = -1;
        if (line == null || line.trim().isEmpty()) {
            return error;
        }
        char c = line.trim().charAt(0);
        switch (c) {
            case 'k':
                return 1;
            case 'v':
                return 2;
            case '+':
                return 3;
            case 'l':
                return 4;
            case 'd':
                return 5;
            case 'c':
                return 6;
            case 's':
                return 7;
            default:
                return error; //-1
        }
    }

    private static String readLine(final BufferedReader in, final LicenseDecrypter decrypter) {
        try {
            String line = in.readLine();
            if (line != null) {
                line = line.replaceAll(":::new_line:::", "\n");
                line = line.replaceAll(":::char_return:::", "\r");
                String decrypted = decrypter.decrypt(line);
                return decrypted;
            }
        } catch (IOException | SecurityException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
//            throw new RemoteException("", ex);
        }
        return null;
    }

    /**
     * Is this license valid or is it expired?
     *
     * @return license valid?
     */
    public final boolean isValid() {
        Date validDt = getValidDate();
        if (validDt == null) {
            return false;
        }
        final Date currentDate = getCurrentDate();
        return validDt.getTime() >= currentDate.getTime();
    }

    private Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * License expiration date
     *
     * @return the validDate license if valid until this date
     */
    public final Date getValidDate() {
        return validDate == null ? null : new Date(validDate.getTime());
    }

//    /**
//     * @param validDate the validDate to set
//     */
//    public void setValidDate(final Date validDate) {
//        this.validDate = validDate == null ? null : new Date(validDate.getTime());
//    }
    /**
     * List of granted hospital identifiers (can never be null!)
     *
     * @return hospital identifiers
     */
    public final Set<String> getHospList() {
        return Collections.unmodifiableSet(hospList);
    }

//    /**
//     * @param hospList the hospList to set
//     */
//    private void setHospList(final Set<String> hospList) {
//        this.hospList = hospList == null ? new LinkedHashSet<>() : new LinkedHashSet<>(hospList);
//    }
//    /**
//     * @param pHosp hospital
//     */
//    private void addHospital(final String pHosp) {
//        if (pHosp == null || pHosp.trim().isEmpty()) {
//            return;
//        }
//        this.hospList.add(pHosp.trim());
//    }
    /**
     * List of granted insurance identifiers (can never be null!)
     *
     * @return insurance identifiers
     */
    public final Set<String> getInsCompList() {
        return Collections.unmodifiableSet(insCompList);
    }

//    /**
//     * @param insCompList the insCompList to set
//     */
//    private void setInsCompList(final Set<String> insCompList) {
//        this.insCompList = insCompList == null ? new LinkedHashSet<>() : new LinkedHashSet<>(insCompList);
//    }
//    /**
//     * @param pIns insurance
//     */
//    private void addInsCompany(final String pIns) {
//        if (pIns == null || pIns.trim().isEmpty()) {
//            return;
//        }
//        this.insCompList.add(pIns.trim());
//    }
    /**
     * List of granted department identifiers (can never be null!)
     *
     * @return department identifiers
     */
    public final Set<String> getDeptList() {
        return Collections.unmodifiableSet(deptList);
    }

//    /**
//     * @param deptList the deptList to set
//     */
//    private void setDeptList(final Set<String> deptList) {
//        this.deptList = deptList == null ? new LinkedHashSet<>() : new LinkedHashSet<>(deptList);
//    }
//    /**
//     * @param pDep department
//     */
//    private void addDepartment(final String pDep) {
//        if (pDep == null || pDep.trim().isEmpty()) {
//            return;
//        }
//        this.deptList.add(pDep.trim());
//    }
    /**
     * List of granted modules (can never be null!)
     *
     * @return modules
     */
    public final Set<String> getModuleList() {
        return Collections.unmodifiableSet(moduleList);
    }

    /**
     * Are there any restrictions considering hospital identifiers?
     *
     * @return the allowAllHospitals
     */
    public final boolean isAllowAllHospitals() {
        return allowAllHospitals || hospList.isEmpty();
    }

    /**
     * Are there any restrictions considering insurance identifiers?
     *
     * @return the allowAllInsurances
     */
    public final boolean isAllowAllInsurances() {
        return insCompList.isEmpty();
    }

    /**
     * Are there any restrictions considering department codes?
     *
     * @return the allowAllDepartments
     */
    public final boolean isAllowAllDepartments() {
        return deptList.isEmpty();
    }

    /**
     * Is the access to hospital cases with this hospital identifier restricted?
     *
     * @param pHosIdent hospital identifier to check
     * @return access to hospital allowed?
     */
    public final boolean isHospitalAllowed(final String pHosIdent) {
        if (isAllowAllHospitals()) {
            return true;
        }
        final String hosIdent = pHosIdent == null ? "" : pHosIdent.trim();
//        if (hosIdent.isEmpty()) {
//            throw new IllegalArgumentException("Hospital identifier is null or empty!");
//        }
        return hospList.contains(hosIdent);
    }

    /**
     * Is the access to department with this code restricted?
     *
     * @param pDepCode department code
     * @return access to department allowed?
     */
    public final boolean isDepartmentAllowed(final String pDepCode) {
//        if (isAllowAllDepartments()) {
//            return true;
//        }
        if (isAllowAllDepartments()) {
            return true;
        }
        final String depCode = pDepCode == null ? "" : pDepCode.trim();
//        if (depCode.isEmpty()) {
//            throw new IllegalArgumentException("Department code is null or empty!");
//        }
        return deptList.contains(depCode);
    }

    /**
     * Is the access to insurance with this insurance identifier restricted?
     *
     * @param pInsIdent insurance identifier to check
     * @return access to insurance allowed?
     */
    public final boolean isInsuranceAllowed(final String pInsIdent) {
//        if (isAllowAllInsurances()) {
//            return true;
//        }
        if (isAllowAllInsurances()) {
            return true;
        }
        final String insIdent = pInsIdent == null ? "" : pInsIdent.trim();
//        if (hosIdent.isEmpty()) {
//            throw new IllegalArgumentException("Hospital identifier is null or empty!");
//        }
        return insCompList.contains(insIdent);
    }

    /**
     * @return the custName
     */
    public final String getCustName() {
        return custName;
    }

    /**
     * @return the drgModule
     */
    public final boolean isDrgModule() {
        return drgModule;
    }

    /**
     * @return the dataModule
     */
    public final boolean isDataModule() {
        return dataModule;
    }

    /**
     * @return the fmModule
     */
    public final boolean isFmModule() {
        return fmModule;
    }

    /**
     * @return the gkModule
     */
    public final boolean isGkModule() {
        return gkModule;
    }

    /**
     * @return the budgetModule
     */
    public final boolean isBudgetModule() {
        return budgetModule;
    }

    /**
     * @return the morbiRsaModule
     */
    public final boolean isMorbiRsaModule() {
        return morbiRsaModule;
    }

    /**
     * @return the gkvmModule
     */
    public final boolean isGkvmModule() {
        return gkvmModule;
    }

    /**
     * @return the peppModule
     */
    public final boolean isPeppModule() {
        return peppModule;
    }
    
    public boolean isAcgModule(){
        return acgModule;
    }

    /**
     * @return the customer
     */
    public LicenseCustomerEn getCustomer() {
        return customer;
    }

    /**
     * @return the caseLimit
     */
    public Long getCaseLimit() {
        return caseLimit;
    }

    /**
     * @return the customer type
     */
    public LicenseCustomerTypeEn getCustomerType() {
        return customer == null ? LicenseCustomerTypeEn.Other : customer.getType();
    }

    public void addModule(String module) {
        if (moduleList == null) {
            moduleList = new HashSet<>();
        }
        moduleList.add(module);
        if (module.equals(ModuleManager.PARAMETER_DRG)) {
            drgModule = true;
        }
        if (module.equals(ModuleManager.PARAMETER_FM)) {
            fmModule = true;
        }
        if (module.equals(ModuleManager.PARAMETER_DATA)) {
            dataModule = true;
        }
        if (module.equals(ModuleManager.PARAMETER_BUDGET)) {
            budgetModule = true;
        }
        if (module.equals(ModuleManager.PARAMETER_GK)) {
            gkModule = true;
        }
        if (module.equals(ModuleManager.PARAMETER_MRSA)) {
            morbiRsaModule = true;
        }
        if (module.equals(ModuleManager.PARAMETER_GKVM)) {
            gkvmModule = true;
        }
        if (module.equals(ModuleManager.PARAMETER_PEPP)) {
            peppModule = true;
        }
        if (module.equals(ModuleManager.PARAMETER_ACG)) {
            acgModule = true;
        }
    }

    /**
     * gets customer name directly from free form text field first. If it is
     * empty then use customer enum.
     *
     * @return customer name
     */
    public String getTitle() {
        if (custName != null && !custName.trim().isEmpty()) {
            return custName;
        }
        if (customer != null) {
            return customer.getShortName();
        }
        return "";
    }

    @Override
    public String toString() {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return "License {"
                + "\n  customer = " + getCustomer()
                + "\n  name = " + custName
                + "\n  expiration date = " + (validDate == null ? "null" : sdf.format(validDate)) + (isValid() ? "" : " (license is invalid!)")
                + "\n  case limit = " + (caseLimit == null ? "(none)" : caseLimit)
                + "\n  modules = " + moduleList
                + "\n  hospitals = " + hospList + (isAllowAllHospitals() ? " (all hospitals allowed)" : "")
                + "\n  insurances = " + insCompList + (isAllowAllInsurances() ? " (all insurances allowed)" : "")
                + "\n  departments = " + deptList + (isAllowAllDepartments() ? " (all departments allowed)" : "")
                //+ "\n  allowAllHospitals=" + allowAllHospitals
                //+ "\n  drgModule=" + drgModule
                //+ "\n  dataModule=" + dataModule
                //+ "\n  fmModule=" + fmModule
                //+ "\n  gkModule=" + gkModule
                //+ "\n  budgetModule=" + budgetModule
                //+ "\n  morbiRsaModule=" + morbiRsaModule
                //+ "\n  gkvmModule=" + gkvmModule
                //+ "\n  peppModule=" + peppModule
                + "\n}";
    }

}
