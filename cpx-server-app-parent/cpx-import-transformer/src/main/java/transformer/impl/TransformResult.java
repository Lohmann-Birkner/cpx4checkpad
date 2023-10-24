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
 *    2017  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package transformer.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 *
 * @author Dirk Niemeier
 */
public class TransformResult {

    private static final Logger LOG = Logger.getLogger(TransformResult.class.getName());
    public final int mCaseCount;
    public final int mPatientCount;
    protected int mNumberOfProcessedItems = 0;
    public final Set<Exception> mExceptions;

    public TransformResult(final int pPatientCount, final int pCaseCount, final Set<Exception> pExceptions) {
        mCaseCount = pCaseCount;
        mPatientCount = pPatientCount;
        mExceptions = pExceptions == null ? new LinkedHashSet<>() : new LinkedHashSet<>(pExceptions);
    }

    public int getCaseCount() {
        return mCaseCount;
    }

    public int getPatientCount() {
        return mPatientCount;
    }

    public int getNumberOfAllItems() {
        return mPatientCount + mCaseCount;
    }

    public int incNumberOfProcessedItems() {
        return ++mNumberOfProcessedItems;
    }

    public int getNumberOfProcessedItems() {
        return mNumberOfProcessedItems;
    }

    public Set<Exception> getExceptions() {
        return mExceptions == null ? new LinkedHashSet<>() : new LinkedHashSet<>(mExceptions);
    }

    public boolean hasExceptions() {
        return (mExceptions != null && !mExceptions.isEmpty());
    }

    public String[] getExceptionStackTraces() {
        List<String> traces = new ArrayList<>();
        int i = 0;
        if (mExceptions != null && !mExceptions.isEmpty()) {
            int c = mExceptions.size();
            Iterator<Exception> it = mExceptions.iterator();
            while (it.hasNext()) {
                i++;
                Exception ex = it.next();
                if (ex == null) {
                    continue;
                }
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                ex.printStackTrace(pw);
                String sStackTrace = sw.toString();
                String head = "------ Exception " + i + " of " + c + " ------";
                traces.add(head + "\r\n" + sStackTrace);
            }
        }
        String[] t = new String[traces.size()];
        traces.toArray(t);
        return t;
    }

}
