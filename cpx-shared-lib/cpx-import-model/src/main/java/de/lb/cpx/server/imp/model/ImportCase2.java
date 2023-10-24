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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.imp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author husser
 */
public class ImportCase2 implements Serializable {

    private static final long serialVersionUID = 1L;

    private ImportCaseKey importCaseKey;

    private String[] caseRow;

    private List<String[]> icdList = new ArrayList<>();
    private List<String[]> opsList = new ArrayList<>();
    private List<String[]> departmentList = new ArrayList<>();
    private List<String[]> feeList = new ArrayList<>();

    public ImportCase2(ImportCaseKey importCaseKey) {
        this.importCaseKey = importCaseKey;
    }

    public String[] getCaseRow() {
        return caseRow == null ? null : Arrays.copyOf(caseRow, caseRow.length);
    }

    public void setCaseRow(String[] fallRow) {
        this.caseRow = fallRow == null ? null : Arrays.copyOf(fallRow, fallRow.length);
    }

    public List<String[]> getIcdList() {
        return icdList == null ? new ArrayList<>() : icdList;
    }

    public void setIcdList(List<String[]> icdList) {
        this.icdList = new ArrayList<>(icdList);
    }

    public List<String[]> getOpsList() {
        return opsList == null ? new ArrayList<>() : opsList;
    }

    public void setOpsList(List<String[]> opsList) {
        this.opsList = opsList == null ? null : new ArrayList<>(opsList);
    }

    public List<String[]> getDepartmentList() {
        return departmentList == null ? new ArrayList<>() : departmentList;
    }

    public void setDepartmentList(List<String[]> departmentList) {
        this.departmentList = departmentList == null ? null : new ArrayList<>(departmentList);
    }

    public void addDepartment(String[] csvRow) {
        String[] copy = csvRow == null ? null : Arrays.copyOf(csvRow, csvRow.length);
        this.departmentList.add(copy);
    }

    public ImportCaseKey getImportCaseKey() {
        return importCaseKey;
    }

    public void setImportCaseKey(ImportCaseKey importCaseKey) {
        this.importCaseKey = importCaseKey;
    }

    public List<String[]> getFeeList() {
        return feeList == null ? new ArrayList<>() : feeList;
    }

    public void setFeeList(List<String[]> feeList) {
        this.feeList = feeList == null ? null : new ArrayList<>(feeList);
    }

    public void addFee(String[] csvRow) {
        String[] copy = csvRow == null ? null : Arrays.copyOf(csvRow, csvRow.length);
        this.feeList.add(copy);
    }

    public void addOps(String[] csvRow) {
        String[] copy = csvRow == null ? null : Arrays.copyOf(csvRow, csvRow.length);
        this.opsList.add(copy);
    }

    public void addIcd(String[] csvRow) {
        String[] copy = csvRow == null ? null : Arrays.copyOf(csvRow, csvRow.length);
        this.icdList.add(copy);
    }

    @Override
    public String toString() {
        return this.importCaseKey.toString();
    }

}
