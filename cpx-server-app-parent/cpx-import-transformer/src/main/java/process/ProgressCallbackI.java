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
 *    2017  niemeier - initial API and implementation and/or initial documentation
 */
package process;

/**
 * Progress handler
 *
 * @author niemeier
 */
public interface ProgressCallbackI {

    /**
     * Can be used to send progress messages
     *
     * @param pPhase process items in main phase
     * @param pMaxPhases all items in main phase
     * @param pSubphase process items in sub phase
     * @param pMaxSubphases all items in sub phase
     * @param pText message text
     */
    void execute(final int pPhase, final int pMaxPhases, final int pSubphase, final int pMaxSubphases, final String pText);

}
