/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.job.fx.batchgroup;

import de.lb.cpx.client.core.model.CpxScreen;
import de.lb.cpx.client.core.util.CpxFXMLLoader;
import java.io.IOException;

/**
 * implements the screen of the Batch grouping job
 *
 * @author wilde
 */
public class JobBatchGroupingScreen extends CpxScreen {

    public JobBatchGroupingScreen() throws IOException {
        super(CpxFXMLLoader.getLoader(JobBatchGroupingFXMLController.class));
    }

    /**
     * get the controller instance of the screen
     *
     * @return controller
     */
    @Override
    public JobBatchGroupingFXMLController getController() {
        return (JobBatchGroupingFXMLController) super.getController();
    }

}
