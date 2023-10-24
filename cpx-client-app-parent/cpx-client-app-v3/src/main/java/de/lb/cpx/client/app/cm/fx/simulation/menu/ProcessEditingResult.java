/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.cm.fx.simulation.menu;

import de.lb.cpx.wm.model.TWmProcess;

/**
 *
 * @author niemeier
 */
public class ProcessEditingResult {

    private final TWmProcess process;
    private final boolean confirmBeforeOpen;

    public ProcessEditingResult(final TWmProcess pProcess, final boolean pConfirmBeforeOpen) {
        process = pProcess;
        confirmBeforeOpen = pConfirmBeforeOpen;
    }

    public TWmProcess getProcess() {
        return process;
    }

    public long getProcessId() {
        return process == null ? 0L : process.getId();
    }

    public long getWorkflowNumber() {
        return process == null ? 0L : process.getWorkflowNumber();
    }

    public boolean isConfirmBeforeOpen() {
        return confirmBeforeOpen;
    }

}
