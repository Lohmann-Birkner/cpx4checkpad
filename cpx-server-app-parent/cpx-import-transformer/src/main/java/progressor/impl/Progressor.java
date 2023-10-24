/* 
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package progressor.impl;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Callback;
import process.FailureCallbackI;
import process.ProgressCallbackI;
import process.SuccessCallbackI;
import process.impl.UpdateImportsEntryDto;
import progressor.ProgressorI;

/**
 *
 * @author niemeier
 */
public class Progressor implements ProgressorI {

    private static final Logger LOG = Logger.getLogger(Progressor.class.getName());

    private static final long serialVersionUID = 1L;

    private final AtomicInteger mPhase = new AtomicInteger(0);
    private final AtomicInteger mSubphase = new AtomicInteger(0);
    private final AtomicInteger mMaxPhases = new AtomicInteger(0);
    private final AtomicInteger mMaxSubphases = new AtomicInteger(0);
//    public static final int MAX_SUBPHASES = 7;
    private ProgressCallbackI mProgressCallback;
    private SuccessCallbackI mSuccessCallback;
    private FailureCallbackI mFailureCallback;
    private final Callback<UpdateImportsEntryDto, Integer> mUpdateImportsEntryCb;

    public Progressor(final Callback<UpdateImportsEntryDto, Integer> pUpdateImportsEntryCb) {
        this.mUpdateImportsEntryCb = pUpdateImportsEntryCb;
        if (mUpdateImportsEntryCb == null) {
            throw new IllegalArgumentException("update imports callback cannot be null!");
        }
    }

    @Override
    public void setProgressCallback(final ProgressCallbackI pProgressCallback) {
        mProgressCallback = pProgressCallback;
    }

    @Override
    public void setFailureCallback(final FailureCallbackI pFailureCallback) {
        mFailureCallback = pFailureCallback;
    }

    @Override
    public ProgressCallbackI getProgressCallback() {
        return mProgressCallback;
    }

    @Override
    public FailureCallbackI getFailureCallback() {
        return mFailureCallback;
    }

    @Override
    public void sendProgress(final String pComment) {
        final int phase = getPhase();
        final int maxPhases = getMaxPhases();
        final int newSubphase = getIncSubphase();
        final int maxSubphases = getMaxSubphases();
        final String message = "Phase " + phase + "/" + maxPhases + ", Schritt " + newSubphase + "/" + maxSubphases + ": " + pComment;
        LOG.log(Level.INFO, message);
        mUpdateImportsEntryCb.call(new UpdateImportsEntryDto(message));
        if (mProgressCallback == null) {
            return;
        }
        mProgressCallback.execute(phase, maxPhases, newSubphase, maxSubphases, pComment);
    }

    @Override
    public void sendFailure(final String pReasonForFailure, final Exception pException) {
        final String message = "Fehler: " + pReasonForFailure;
        LOG.log(Level.INFO, message, pException);
        mUpdateImportsEntryCb.call(new UpdateImportsEntryDto(message));
        if (mFailureCallback == null) {
            return;
        }
        mFailureCallback.execute(pReasonForFailure, pException);
    }

    @Override
    public void sendSuccess(String pComment) {
        final String message = "Erfolgreich abgeschlossen: " + pComment;
        LOG.log(Level.INFO, message);
        mUpdateImportsEntryCb.call(new UpdateImportsEntryDto(message));
        if (mSuccessCallback == null) {
            return;
        }
//        final int phase = getPhase();
//        final int maxPhases = getMaxPhases();
//        final int newSubphase = mSubphase.incrementAndGet();
//        final int maxSubphases = getMaxSubphases();
        mSuccessCallback.execute(pComment);
    }

    @Override
    public void setSuccessCallback(SuccessCallbackI pSuccessCallback) {
        mSuccessCallback = pSuccessCallback;
    }

    @Override
    public SuccessCallbackI getSuccessCallback() {
        return mSuccessCallback;
    }

    @Override
    public int getPhase() {
        return mPhase.get();
    }

    @Override
    public int incrementPhase() {
        int newPhase = mPhase.incrementAndGet();
        mSubphase.set(0);
        LOG.log(Level.INFO, "We're now in main phase " + newPhase + "/" + getMaxPhases());
        return newPhase;
    }

    @Override
    public void setMaxPhases(int pMaxPhases) {
        mMaxPhases.set(pMaxPhases);
    }

    @Override
    public void setMaxSubPhases(int pMaxSubphases) {
        mMaxSubphases.set(pMaxSubphases);
    }

    @Override
    public int getSubphase() {
        return mSubphase.get();
    }

    @Override
    public int getMaxPhases() {
        return mMaxPhases.get();
    }

    @Override
    public int getMaxSubphases() {
        return mMaxSubphases.get();
    }

    /**
     * increment sub phase
     *
     * @return incremented sub phase
     */
    public int getIncSubphase() {
        int newSubphase = mSubphase.incrementAndGet();
        LOG.log(Level.FINEST, "We're now in sub phase " + newSubphase + "/" + getMaxSubphases());
        return newSubphase;
    }

}
