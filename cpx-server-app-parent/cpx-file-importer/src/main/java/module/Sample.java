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

import de.lb.cpx.shared.dto.job.config.file.SampleJob;

/**
 * This demo module is just for your demonstration how to implement
 *
 * @author Dirk Niemeier
 */
public class Sample extends AbstractImportFileModule<SampleJob> {

    private static final long serialVersionUID = 1L;
    private static final String NAME = "Sample";

    public Sample(SampleJob pInputConfig, String pOutputDirectory) {
        super(NAME, pInputConfig, pOutputDirectory);
    }

    public Sample(SampleJob pInputConfig) {
        super(NAME, pInputConfig);
    }

}