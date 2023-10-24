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

import de.lb.cpx.grouper.model.enums.GrouperEvalResultEn;
import java.io.Serializable;

/**
 *
 * @author gerschmann
 */
public class EvaluationCaseResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private GrouperEvalResultEn result = GrouperEvalResultEn.OK;
    private String info = "";

    private long time4Case = 0;
    private long time4findCase = 0;
    private long time4evalCase = 0;
    private long minTime4Case = 0;
    private long minTime4findCase = 0;
    private long minTime4evalCase = 0;
    private String minTimeCase = "";
    private String minTimeFindCase = "";
    private String minTimeEvalCase = "";
    private long maxTime4Case = 0;
    private long maxTime4FindCase = 0;
    private long maxTime4EvalCase = 0;
    private String maxTimeCase = "";
    private String maxTimeFindCase = "";
    private String maxTimeEvalCase = "";
    private long time4CaseFull = 0;

    private int goodCount = 0;
    private int badCount = 0;
    private int errCount = 0;
    private int fullCount = 0;

    public void setResult(GrouperEvalResultEn result) {
        this.result = result;
    }

    public GrouperEvalResultEn getResult() {
        return result;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info += info.isEmpty() ? "" : "\r\n" + info;
    }

    public String getOneResult() {
        return "\r\n" + String.valueOf(fullCount + ": ") + result.name() + ": " + info;
    }

    public void setTime4FindCase(String fallnr, long l) {
        time4findCase += l;
        if (minTime4findCase > l || minTime4findCase == 0) {
            minTime4findCase = l;
            minTimeFindCase = fallnr;
        }
        if (maxTime4FindCase < l) {
            maxTime4FindCase = l;
            maxTimeFindCase = fallnr;
        }
    }

    public void setTime4EvalCaseCase(String fallnr, long l) {
        time4evalCase += l;
        if (minTime4evalCase > l || minTime4evalCase == 0) {
            minTime4evalCase = l;
            minTimeEvalCase = fallnr;
        }
        if (maxTime4EvalCase < l) {
            maxTime4EvalCase = l;
            maxTimeEvalCase = fallnr;
        }
    }

    public void setTimeCase(String fallnr, long l) {
        time4Case += l;
        if (minTime4Case > l || minTime4Case == 0) {
            minTime4Case = l;
            minTimeCase = fallnr;
        }
        if (maxTime4Case < l) {
            maxTime4Case = l;
            maxTimeCase = fallnr;
        }
    }

    public long getTime4Case() {
        return time4Case;
    }

    public long getTime4findCase() {
        return time4findCase;
    }

    public long getTime4evalCase() {
        return time4evalCase;
    }

    public long getMinTime4Case() {
        return minTime4Case;
    }

    public long getMinTime4findCase() {
        return minTime4findCase;
    }

    public long getMinTime4evalCase() {
        return minTime4evalCase;
    }

    public String getMinTimeCase() {
        return minTimeCase;
    }

    public String getMinTimeFindCase() {
        return minTimeFindCase;
    }

    public String getMinTimeEvalCase() {
        return minTimeEvalCase;
    }

    public long getMaxTime4Case() {
        return maxTime4Case;
    }

    public long getMaxTime4FindCase() {
        return maxTime4FindCase;
    }

    public long getMaxTime4EvalCase() {
        return maxTime4EvalCase;
    }

    public String getMaxTimeCase() {
        return maxTimeCase;
    }

    public String getMaxTimeFindCase() {
        return maxTimeFindCase;
    }

    public String getMaxTimeEvalCase() {
        return maxTimeEvalCase;
    }

    public void reset() {
        result = GrouperEvalResultEn.OK;
        info = "";

    }

    public int getGoodCount() {
        return goodCount;
    }

    public void setGoodCount() {
        this.goodCount++;
    }

    public int getBadCount() {
        return badCount;
    }

    public void setBadCount() {
        this.badCount++;
    }

    public int getErrCount() {
        return errCount;
    }

    public void setErrCount() {
        this.errCount++;
    }

    public int getFullCount() {
        return fullCount;
    }

    public void setFullCount() {
        this.fullCount++;
    }

    @Override
    public String toString() {
        return "full evaluation time = " + time4CaseFull + " for " + fullCount + " average  " + String.valueOf(time4CaseFull / fullCount) + "\r\n"
                + "full time = " + time4Case + " for " + fullCount + " average  " + String.valueOf(time4Case / fullCount) + "\r\n"
                + "time for find case in DB = " + time4findCase + " for " + fullCount + " average  " + String.valueOf(time4findCase / fullCount) + "\r\n"
                + "time for evaluate case = " + time4evalCase + " for " + fullCount + " average  " + String.valueOf(time4evalCase / fullCount) + "\r\n"
                + "min time for case = " + minTime4Case + " for " + minTimeCase + "\r\n"
                + "min time for find case = " + minTime4findCase + " for " + minTimeFindCase + "\r\n"
                + "min time for evaluate case = " + minTime4evalCase + " for " + minTimeEvalCase + "\r\n"
                + "max time for case = " + maxTime4Case + " for " + maxTimeCase + "\r\n"
                + "max time for find case = " + maxTime4FindCase + " for " + maxTimeFindCase + "\r\n"
                + "max time for evaluate case = " + maxTime4EvalCase + " for " + maxTimeEvalCase + "\r\n\r\n"
                + " full count = " + fullCount + "\r\n"
                + " cases with differences = " + badCount + "\r\n"
                + " errors = " + errCount + "\r\n";
    }

    public String getXmlString() {
        return toString().replaceAll("\r\n", "<br>");
    }

    public long getTime4CaseFull() {
        return time4CaseFull;
    }

    public void setTime4CaseFull(long time4CaseFull) {
        this.time4CaseFull += time4CaseFull;
    }

}
