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
package module;

import de.lb.cpx.shared.dto.job.config.database.MedicoJob;

/**
 *
 * @author Dirk Niemeier
 */
public class Medico extends AbstractImportDbModule<MedicoJob> {

    private static final long serialVersionUID = 1L;
    private final static String NAME = "medico";

    public Medico(MedicoJob pInputConfig, String pOutputDirectory) {
        super(NAME, pInputConfig, pOutputDirectory);
    }

    public Medico(MedicoJob pInputConfig) {
        super(NAME, pInputConfig);
    }

}
