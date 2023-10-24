/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package progressor;

import java.io.Serializable;
import process.FailureCallbackI;
import process.ProgressCallbackI;
import process.SuccessCallbackI;

/**
 *
 * @author Dirk Niemeier
 */
public interface ProgressorI extends Serializable {

    /**
     * Will be executed when import progresses (JMS Message can be send here)
     *
     * @param pComment comment
     */
    void sendProgress(final String pComment /*, final int pNumberOfAllItems */);

    /**
     * Will be executed when import successes (JMS Message can be send here)
     *
     * @param pComment comment
     */
    void sendSuccess(final String pComment /*, final int pNumberOfAllItems */);

    /**
     * Will be executed when import fails (JMS Message can be send here)
     *
     * @param pReasonForFailure reason
     * @param pException exception
     */
    void sendFailure(final String pReasonForFailure, final Exception pException);

    /**
     * Sets the process handler
     *
     * @param pProgressCallback progress handler
     */
    void setProgressCallback(final ProgressCallbackI pProgressCallback);

    /**
     * Sets the success handler
     *
     * @param pSuccessCallback success handler
     */
    void setSuccessCallback(final SuccessCallbackI pSuccessCallback);

    /**
     * Sets the failure handler
     *
     * @param pFailureCallback failure handler
     */
    void setFailureCallback(final FailureCallbackI pFailureCallback);

    /**
     * Gets the progress handler
     *
     * @return progress handler
     */
    ProgressCallbackI getProgressCallback();

    /**
     * Gets the success handler
     *
     * @return success handler
     */
    SuccessCallbackI getSuccessCallback();

    /**
     * Gets the failure handler
     *
     * @return failure handler
     */
    FailureCallbackI getFailureCallback();

    /**
     * get recent main phase
     *
     * @return main phase
     */
    int getPhase();

    /**
     * increment main phase
     *
     * @return incremented main phase
     */
    int incrementPhase();

    /**
     * set number of main phases
     *
     * @param pMaxPhases number of main phases
     */
    void setMaxPhases(final int pMaxPhases);

    /**
     * set number of sub phases
     *
     * @param pMaxSubphases number of sub phases
     */
    void setMaxSubPhases(final int pMaxSubphases);

    /**
     * get recent sub phase
     *
     * @return sub phase
     */
    int getSubphase();

//    /**
//     * get recent main phase
//     *
//     * @return new main phase
//     */
//    public int getIncSubphase();
    /**
     * get number of main phases
     *
     * @return number of main phases
     */
    int getMaxPhases();

    /**
     * get number of sub phases
     *
     * @return number of sub phases
     */
    int getMaxSubphases();

}
