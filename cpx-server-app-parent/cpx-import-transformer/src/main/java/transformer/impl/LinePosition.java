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

import java.util.logging.Logger;

/**
 *
 * @author Dirk Niemeier
 */
public final class LinePosition {

    private static final Logger LOG = Logger.getLogger(LinePosition.class.getName());
    public String mCaseKey = "";
    public String mLine = null;
    public int mLineCount;

    public LinePosition(String pCaseKey, String pLine, int pLineCount) {
        update(pCaseKey, pLine, pLineCount);
        //mCaseKey = pCaseKey == null?"":pCaseKey;
        //mLine = pLine == null?"":pLine;
        //mLineCount = pLineCount;
    }

    public String getCaseKey() {
        return mCaseKey;
    }

    public String getLine() {
        return mLine;
    }

    public int getLineCount() {
        return mLineCount;
    }

    public LinePosition update(final String pCaseKey, final String pLine, final int pLineCount) {
        setCaseKey(pCaseKey);
        setLine(pLine);
        setLineCount(pLineCount);
        return this;
    }

    public LinePosition setCaseKey(final String pCaseKey) {
        mCaseKey = pCaseKey == null ? "" : pCaseKey;
        return this;
    }

    public LinePosition setLine(final String pLine) {
        mLine = pLine == null ? "" : pLine;
        return this;
    }

    public LinePosition setLineCount(final int pLineCount) {
        mLineCount = pLineCount;
        return this;
    }

}
