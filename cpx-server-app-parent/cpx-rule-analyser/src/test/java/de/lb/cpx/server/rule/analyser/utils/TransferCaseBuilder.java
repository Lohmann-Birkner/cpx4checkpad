/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.rule.analyser.utils;

import de.lb.cpx.grouper.model.transfer.TransferCase;
import de.lb.cpx.grouper.model.transfer.TransferDepartment;
import de.lb.cpx.grouper.model.transfer.TransferIcd;
import de.lb.cpx.grouper.model.transfer.TransferOps;
import de.lb.cpx.model.enums.CaseTypeEn;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gerschmann
 */
public class TransferCaseBuilder {

// Positions of case attributes in the input string. 
// Input string has the Inek - Input grouper format. 
// In this format the drg - cases do not have departments.
// As we need any department to which we assign the icds and ops we use one department 1500
// with admission date = case admission date 
// and transfer date = case discharge date    
    public static final int KH_POSITION = 0;
    public static final int CASE_NUMBER_POSITION = 1;
    public static final int ADM_DATE_POSITION = 2;
    public static final int DIS_DATE_POSITION = 3;
    public static final int ADM_REASON_POSITION = 4;
    public static final int ADM_MODE12_POSITION = 5;
    public static final int DIS_MODE12_POSITION = 6;
    public static final int BIRTH_DATE_POSITION = 7;
    public static final int ADM_WEIGHT_POSITION = 8;
    public static final int AGE_IN_YEARS_POSITION = 9;
    public static final int AGE_IN_DAYS_POSITION = 10;
    public static final int SEX_POSITION = 11;
    public static final int BREATHING_HMV_POSITION = 12;
    public static final int DIAGNOSIS_POSITION = 13;
    public static final int PROCEDURES_POSITION = 14;
    public static final int DEPARTMENTS_POSITION = 15;
    public static final int LENGTH_OF_STAY_LOS_POSITION = 16;
    public static final int LEAVE_DAYS_POSITION = 17;
// results now do not use
    public static final int DRG_PEPP_POSITION = 18;
    public static final int MDC_POSITION = 19;
    public static final int PCCL_POSITION = 20;
    public static final int GST_POSITION = 21;
    public static final int GPDX_POSITION = 22;

    private static final SimpleDateFormat m_simpleDateFormatWithTime = new SimpleDateFormat("yyyyMMddHHmm");
    private static final SimpleDateFormat m_simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

    private static final String DRG_CASES_PATH = "transferCases/drgcases.txt";
    private static final String DRG_HISTORY_CASES_PATH = "transferCases/history_cases.txt";
    private static final Logger LOG = Logger.getLogger(TransferCaseBuilder.class.getName());

    private TransferCaseBuilder() {

    }

    public static TransferCaseBuilder getInstance() {
        return new TransferCaseBuilder();
    }

    /**
     *
     * @return
     */
    public List<TransferCase> readDrgCases() {
        return readDrgCasesFromPath(DRG_CASES_PATH);
    }

    public List<TransferCase> readDrgHistoryCases() {
        return readDrgCasesFromPath(DRG_HISTORY_CASES_PATH);
    }

    private List<TransferCase> readDrgCasesFromPath(String path) {
        List<TransferCase> retList = new ArrayList<>();
        ClassLoader classLoader = TransferCaseBuilder.class.getClassLoader();
        File file = new File(classLoader.getResource(path).getFile());
        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                LOG.log(Level.INFO, line);
                if (line.isEmpty() || line.startsWith("-")) {
                    continue;
                }
                retList.add(fillInoutFromInputString(line, CaseTypeEn.DRG));
            }
            scanner.close();

        } catch (Exception ex) {
            LOG.log(Level.WARNING, ex.getMessage());
        }
        return retList;
    }

    public TransferCase fillInoutFromInputString(String input, CaseTypeEn isDrg) throws Exception {
        TransferCase trCase = new TransferCase();
        TransferDepartment trDepartment = new TransferDepartment("1500");
        trCase.setCaseType(isDrg.getId());
        trCase.addDepartment(trDepartment);
        String parts[] = input.split("\\|");
        for (int i = 0; i < parts.length; i++) {
            try {
                switch (i) {
                    case KH_POSITION:
                        trCase.setIkz(parts[i]);
                        break;
                    case CASE_NUMBER_POSITION:
                        trCase.setCaseNumber(parts[i]);

                        break;
                    case ADM_DATE_POSITION:
                        trCase.setAdmissionDate(getDateFromString(parts[i]));

                        break;
                    case DIS_DATE_POSITION:
                        trCase.setDischargeDate(getDateFromString(parts[i]));
                        break;
                    case ADM_REASON_POSITION:
                        trCase.setAdmissionCause(getAdmCause(parts[i]));
                        break;
                    case ADM_MODE12_POSITION:
                        try {
                            trCase.setAdmissionReason12(Integer.parseInt(parts[i]));
                        } catch (NumberFormatException e) {
                            LOG.log(Level.INFO, parts[CASE_NUMBER_POSITION] + " Aufnahmegrund is not numeric: " + parts[ADM_MODE12_POSITION]);
                        }

                        break;

                    case DIS_MODE12_POSITION:
                        try {
                            trCase.setDiscargeReason12(Integer.parseInt(parts[i]));
                        } catch (NumberFormatException e) {
                            LOG.log(Level.INFO, parts[CASE_NUMBER_POSITION] + " Entlassungsgund is not numeric: " + parts[DIS_MODE12_POSITION]);
                        }

                        break;

                    case BIRTH_DATE_POSITION:
                        try {
                            trCase.setDateOfBirth(getDateFromString(parts[i]));
                        } catch (Exception e) {
                            LOG.log(Level.INFO, parts[CASE_NUMBER_POSITION] + " Fehler in Geburtsdatum: " + parts[BIRTH_DATE_POSITION]);
                        }

                        break;
                    case ADM_WEIGHT_POSITION:
                        trCase.setWeight(getDoubleFromString(parts[i], 0));
                        break;
                    case AGE_IN_YEARS_POSITION:
                        trCase.setAgeY(Integer.parseInt(parts[i]));
                        break;
                    case AGE_IN_DAYS_POSITION:
                        trCase.setAgeD(Integer.parseInt(parts[i]));
                        break;
                    case SEX_POSITION:
                        trCase.setSex(getSexValues(parts[i]));
                        break;
                    case BREATHING_HMV_POSITION:
                        int hmv = 0;
                        try {
                            hmv = Integer.parseInt(parts[i]);
                        } catch (NumberFormatException e) {

                        }
                        trCase.setRespirationLength(hmv);

                        break;

                    case DIAGNOSIS_POSITION:
                        setDiagnoses4Department(trCase, trDepartment, parts[i]);
                        break;
                    case PROCEDURES_POSITION:
                        setProcedures4Department(trCase, trDepartment, parts[i]);
                        break;
                    case DEPARTMENTS_POSITION:
                        break;
                    case LENGTH_OF_STAY_LOS_POSITION:

                        try {
                            trCase.setLengthOfStay(Integer.parseInt(parts[i]));
                        } catch (NumberFormatException e) {
                            LOG.log(Level.INFO, parts[CASE_NUMBER_POSITION] + " LOS is not numeric: " + parts[LENGTH_OF_STAY_LOS_POSITION]);

                        }
                        break;

                    case LEAVE_DAYS_POSITION:
                        trCase.setLeaveOfAbsence(Integer.parseInt(parts[i]));
                        break;

                }
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Fehler in der Spalte " + i, e);
            }
        }

        return trCase;
    }

    private static Date getDateFromString(String str) {
        try {
            if (str.length() == 8) {
                return m_simpleDateFormat.parse(str);
            }
            if (str.length() == 12) {
                return m_simpleDateFormatWithTime.parse(str);
            }
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private double getDoubleFromString(String str, int divider) {
        try {
            int i = Integer.parseInt(str);
            if (divider == 0) {
                return i;
            }
            double d = ((double) i) / divider;
            return d;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static void setDiagnoses4Department(TransferCase trCase, TransferDepartment trDepartment, String str) throws Exception {
        // addition to inek format:<code>&<isAdmissionIcd>&<localisation>
        if (str.trim().length() == 0) {
            return;
        }
        String parts[] = str.split("~");
        for (int i = 0; i < parts.length; i++) {
            String s = parts[i].trim();
            String parts1[] = s.split("&");
            if (parts1.length == 0) {
                continue;
            }
            String code = parts1[0];
            if (parts1[0].length() == 0) {
                continue;
            }
            boolean isAdm = false;//(0:1)
            int loc = 0;
            if (parts1.length >= 2) {
                isAdm = parts1[1].trim().equals("1");
            }
            if (parts1.length >= 3) {
                loc = getLocalisation(parts[2].trim());
            }
            TransferIcd trIcd = new TransferIcd(i, code, 0, loc, i == 0, isAdm);
            trDepartment.addIcd(trIcd);
            if (i == 0) {
                trCase.setPrincipalIcd(trIcd);
            }
            trCase.addIcd(trIcd);
        }
    }

    private void setProcedures4Department(TransferCase trCase, TransferDepartment trDepartment, String str) throws Exception {
        if (str.trim().length() == 0) {
            return;
        }
        String parts[] = str.split("~");

        for (int i = 0; i < parts.length; i++) {
            String parts1[] = parts[i].split("&");
            if (parts1[0].length() == 0) {
                continue;
            }
            String code = parts1[0];
            int loc = 0;
            Date date = null;

            if (parts1.length >= 2) {
                loc = getLocalisation(parts1[1]);

            }
            if (parts1.length == 3) {
                date = getDateFromString(parts1[2]);

            }
            TransferOps ops = new TransferOps(i, code, loc, date);
            trDepartment.addOps(ops);
            trCase.addOps(ops);
        }

    }

    private int getSexValues(String sex) throws Exception {

        switch (sex.toUpperCase()) {
            case "M":
            case "1":
                return 1;

            case "W":
            case "2":
                return 2;
            case "I":
            case "3":
                return 3;

            case "U":
            case "9":
                return 9;

            case "D":
            case "4":
                return 4;

            case "X":
            case "0":
                return 0;
            default: {
                return -1;
            }
        }
    }

    private static int getAdmCause(String cs) {

        switch (cs) {
            case "E":
                return 1;
            case "Z":
                return 2;
            case "N":
                return 3;
            case "R":
                return 4;
            case "V":
                return 5;
            case "K":
                return 6;
            case "G":
                return 7;
            case "B":
                return 8;
            case "A":
                return 9;
            default:
                return 1;
        }
    }

    private static int getLocalisation(String loc) {
        if (loc == null || loc.isEmpty()) {
            return 0;
        }
        loc = loc.toUpperCase();
        switch (loc) {
            case "R":
            case "1":
                return 1;
            case "L":
            case "2":
                return 2;
            case "B":
            case "3":
                return 3;
            default:
                return 0;
        }
    }
}
