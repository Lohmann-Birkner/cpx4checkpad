/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.cpx.license.crypter;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nandola
 */
public class LicenseWriter {

    public static final String DEFAULT_LICENSE_FILENAME = "cpx.license";
    public static final String DEFAULT_ENCODING = "Cp1252";
//    private static final String DEFAULT_LICENSE_PATH = "License Key.txt";
    private static final Logger LOG = Logger.getLogger(LicenseWriter.class.getSimpleName());

    public LicenseWriter() {
        //
    }

//    private void addModule(final String pModule) {
////        if (getModuleList() == null) {
////            moduleList = new ArrayList<>();
////        }
//        if (pModule == null || pModule.trim().isEmpty()) {
//            return;
//        }
//        final String module = pModule.trim();
//        //getModuleList().add(pModule);
//        moduleList.add(module);
//        if (module.equalsIgnoreCase(ModuleManager.PARAMETER_DRG)) {
//            drgModule = true;
//        }
//        if (module.equalsIgnoreCase(ModuleManager.PARAMETER_FM)) {
//            fmModule = true;
//        }
//        if (module.equalsIgnoreCase(ModuleManager.PARAMETER_DATA)) {
//            dataModule = true;
//        }
//        if (module.equalsIgnoreCase(ModuleManager.PARAMETER_BUDGET)) {
//            budgetModule = true;
//        }
//        if (module.equalsIgnoreCase(ModuleManager.PARAMETER_GK)) {
//            gkModule = true;
//        }
//        if (module.equalsIgnoreCase(ModuleManager.PARAMETER_MRSA)) {
//            morbiRsaModule = true;
//        }
//        if (module.equalsIgnoreCase(ModuleManager.PARAMETER_GKVM)) {
//            gkvmModule = true;
//        }
//        if (module.equalsIgnoreCase(ModuleManager.PARAMETER_PEPP)) {
//            peppModule = true;
//        }
//    }
//    public void addInsCompany(String insComp) {
//        if (insComp == null) {
//            return;
//        }
//        if (getInsCompList() == null) {
////            insCompList = new Vector<String>();
//            setInsCompList(new ArrayList<>());
//        }
//        getInsCompList().add(insComp);
//    }
//    public void addHospital(String hospital) {
//        if (hospital == null) {
//            return;
//        }
//        if (getHospList() == null) {
////            hospList = new Vector<String>();
//            setHospList(new ArrayList<>());
//        }
//        getHospList().add(hospital);
//    }

    /*
    public LicHospitalDepartment addHospital(String hospital) {
        if (hospital == null) {
            return null;
        }
        String[] params = hospital.split("-");
        int count = params.length;
        if (count > 0) {
            String ik = params[0];
            LicHospitalDepartment ikz = getHospital(ik);
            if (ikz == null) {
                ikz = new LicHospitalDepartment(ik);
                ikz.hospital = ik;
                hospList.add(ikz);
            }
            if (count > 1) {
                ikz.addFab(params[1]);
            }
            return ikz;
        }
        return null;
    }

    public LicHospitalDepartment getHospital(String ik) {
        if (hospList == null) {
//            hospList = new Vector<LicHospitalDepartment>();
            hospList = new Vector<>();
            return null;
        } else {
            for (int i = 0, n = hospList.size(); i < n; i++) {
                LicHospitalDepartment lik = (LicHospitalDepartment) hospList.get(i);
                if (lik.hospital.equals(ik)) {
                    return lik;
                }
            }
        }
        return null;
    }
     */
    private boolean writeLine(final StringBuilder br, final String line, final LicenseDecrypter decriptor) throws UnsupportedEncodingException {
        try {
            String encrypted = decriptor.encrypt(line);
            encrypted = encrypted.replaceAll("\n", ":::new_line:::");
            encrypted = encrypted.replaceAll("\r", ":::char_return:::");
            encrypted += "\n";
            String tt = LicenseDecrypter.toHex(encrypted.getBytes("Cp1252"));
            br.append(tt);
            return true;
        } catch (SecurityException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
            return false;
        }
    }

//    public String writeFileLicense(final String fname, final String custName, final Date expiryDate,
//            final Set<String> hospitals, final Set<String> modules, final Set<String> insCompanies) throws IOException {
    public String writeFileLicense(final String fname, final LicenseCustomerEn customer, final String custName, final Date expiryDate,
            final Long caseLimit, final Set<String> hospitals, final Set<String> modules, final Set<String> insCompanies, final Set<String> departments) throws IOException {
//        if (fname == null) {
//            fname = DEFAULT_LICENSE_PATH;
//        }
        StringBuilder sb = new StringBuilder();
        String line = new SimpleDateFormat("dd-MM-yyyy").format(expiryDate);
        LicenseDecrypter decrypter = LicenseDecrypter.getInstance();

        boolean nextLine = writeLine(sb, line, decrypter);

        if (customer != null) {
            line = "c" + customer.name();
            nextLine = writeLine(sb, line, decrypter);
        }
        line = "+" + custName;
        nextLine = writeLine(sb, line, decrypter);

        if (nextLine) {
            if (modules != null) {
                for (String module : modules) {
                    line = "l" + module;
                    if (!writeLine(sb, line, decrypter)) {
                        return line;
                    }
                }
            }

            if (insCompanies != null) {
                for (String insComp : insCompanies) {
                    line = "v" + insComp;
                    if (!writeLine(sb, line, decrypter)) {
                        return line;
                    }
                }
            }

            if (departments != null) {
                for (String department : departments) {
                    line = "d" + department;
                    if (!writeLine(sb, line, decrypter)) {
                        return line;
                    }
                }
            }

            if (hospitals != null) {
                for (String hospital : hospitals) {
                    /*                    LicHospitalDepartment hospitalDept = (LicHospitalDepartment) hospitals.get(i);  //class cast exception
                        if (hospitalDept != null) {
                            Vector<?> deptVector = hospitalDept.m_fabs;
                            if (deptVector == null || deptVector.size() == 0) { // if department vector is null or it has zero elements
                                line = "k" + hospitalDept.hospital;
                                if (!writeLine(sb, line, decrypter)) {
                                    return line;
                                }
                            } else {    // else also append dept value 
                                for (int j = 0, n = deptVector.size(); j < n; j++) {
                                    String fa = (String) deptVector.get(j);
                                    line = "k" + hospitalDept.hospital + "-" + fa;
                                    if (!writeLine(sb, line, decrypter)) {
                                        return line;
                                    }
                                }
                            }
                        }   */

                    line = "k" + hospital;
                    if (!writeLine(sb, line, decrypter)) {
                        return line;
                    }
                }
            }

            if (caseLimit != null) {
                line = "s" + caseLimit;
                if (!writeLine(sb, line, decrypter)) {
                    return line;
                }
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fname), DEFAULT_ENCODING))) {
            String text = sb.toString();
            writeToWriter(text, bw);
            bw.flush();
        }
        return null;
    }

    private void writeToWriter(final String text, final BufferedWriter br) {
        int count = 0;
        String line = "";
        try {
            for (int i = 0, n = text.length(); i < n; i++) {
                char c = text.charAt(i);
                line += c;
                count++;
                if (count == 50) {
                    br.write(line);
                    br.newLine();
                    line = "";
                    count = 0;
                }
            }
            if (line.length() > 0) {
                br.write(line);
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

//    public String setLicenceCopyKey(final String fname) {
//        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fname), DEFAULT_ENCODING))) {
//            StringBuilder sb = new StringBuilder();
//            sb.append("********Bitte den folgenden Text (nach den Sternchen) in das vorgegebene Fenster in Checkpoint X kopieren*********\n");
//            String line;
//            while ((line = br.readLine()) != null) {
//                sb.append(line);
//                sb.append("\n");
//            }
//            sb.append("*********** Vor dieser Zeile befindet sich das Ende des Kopierbereiches**********");
//            return sb.toString();
//        } catch (IOException ex) {
//            LOG.log(Level.SEVERE, ex.getMessage(), ex);
//            return null;
//        }
//    }
}
