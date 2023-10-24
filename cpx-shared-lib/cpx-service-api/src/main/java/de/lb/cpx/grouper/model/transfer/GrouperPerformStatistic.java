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
package de.lb.cpx.grouper.model.transfer;

import java.io.Serializable;

/**
 *
 * @author gerschmann
 */
public class GrouperPerformStatistic implements Serializable {

    private static final long serialVersionUID = 1L;

    private long time4allWithId = 0;
    private int caseCount = 0;
    private int groupCount = 0;

    private long time4getCaseFromDB = 0;

    private long time4fillInput = 0;
    private long time4fillOutput = 0;
    private long time4group = 0;
    private long time4merge = 0;
    private long time4FillInOutCreator = 0;
    private long time4groupAndCalculateAdditionalFees = 0;
    private long time4performCheckRef = 0;
    private long time4performGroupAndCheck = 0;

    private long time4simulateRules = 0;
    private long time4getAllValidRoleIds = 0;

    public long getTime4allWithId() {
        return time4allWithId;
    }

    public void addTime4allWithId(long time4allWithId) {
        this.time4allWithId += time4allWithId;
    }

    public int getCaseCount() {
        return caseCount;
    }

    public void setCaseCount(int caseCount) {
        this.caseCount = caseCount;
    }

    public long getTime4fillInput() {
        return time4fillInput;
    }

    public void addTime4fillInput(long time4fillInput) {
        this.time4fillInput += time4fillInput;
    }

    public long getTime4fillOutput() {
        return time4fillOutput;
    }

    public void addTime4fillOutput(long time4fillOutput) {
        this.time4fillOutput += time4fillOutput;
    }

    public long getTime4group() {
        return time4group;
    }

    public void addTime4group(long time4group) {
        this.time4group += time4group;
    }

    public int getGroupCount() {
        return groupCount;
    }

    public void increaseGroupCount() {
        this.groupCount++;
    }

    public long getTime4getCaseFromDB() {
        return time4getCaseFromDB;
    }

    public void addTime4getCaseFromDB(long time4getCaseFromDB) {
        this.time4getCaseFromDB += time4getCaseFromDB;
    }

    @Override
    public String toString() {
        if (groupCount > 0) {

            return "Grouped " + caseCount + " cases<br> with method groupCaseLocal(Long hospitalCaseId,Long userId,Long actualRoleId)  total time " + time4allWithId + "; average = " + time4allWithId / groupCount
                    + "<br> time for reading from DB: " + time4getCaseFromDB + " average = " + time4getCaseFromDB / groupCount
                    + "<br> time for filling the transer object: " + time4fillInput + " average = " + time4fillInput / groupCount
                    + "<br> time for grouping: " + time4group + " average = " + time4group / groupCount
                    + "<br> time for getAllValidRoleIds: " + time4getAllValidRoleIds + " average = " + time4getAllValidRoleIds / groupCount
                    + "<br> time for performGroupAndCheck: " + time4performGroupAndCheck + " average = " + time4performGroupAndCheck / groupCount
                    + "<br> time for filling the output object: " + time4fillOutput + " average = " + time4fillOutput / groupCount
                    + "<br> time for merge: " + time4merge + " average = " + time4merge / groupCount
                    + "<br> time for filling InOutObject for grouping: " + time4FillInOutCreator + " average = " + time4FillInOutCreator / groupCount
                    + "<br> time for grouping ang calculation of affitional fees: " + time4groupAndCalculateAdditionalFees + " average = " + time4groupAndCalculateAdditionalFees / groupCount
                    + "<br> time  for rules: " + time4performCheckRef + " average = " + time4performCheckRef / groupCount
                    + "<br> time for simulating of rules and filling responce object with rule results: " + time4simulateRules + " average = " + time4simulateRules / groupCount;
        } else {
            return "no grouped cases ";
        }
    }

    public void addTime4Merge(long l) {
        time4merge += l;
    }

    public void addTime4FillInOutCreator(long l) {
        time4FillInOutCreator += l;
    }

    public void addTime4groupAndCalculateAdditionalFees(long l) {
        time4groupAndCalculateAdditionalFees += l;
    }

    public void addTime4performCheckRef(long l) {
        time4performCheckRef += l;
    }

    public void addTime4simulateRules(long l) {
        time4simulateRules += l;
    }

    public void addTime4performGroupAndCheck(long l) {
        time4performGroupAndCheck += l;
    }

    public void addTime4getAllValidRoleIds(long l) {
        time4getAllValidRoleIds += l;
    }

}
