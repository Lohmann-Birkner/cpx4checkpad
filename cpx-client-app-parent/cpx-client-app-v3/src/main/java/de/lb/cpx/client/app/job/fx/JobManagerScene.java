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
package de.lb.cpx.client.app.job.fx;

import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.util.CpxFXMLLoader;
import java.io.IOException;

/**
 * Creates the JobManager scene to handel all jobs that can be started by the
 * client
 *
 * @author wilde
 */
public class JobManagerScene extends CpxScene {

    public JobManagerScene() throws IOException {
        super(CpxFXMLLoader.getLoader(JobManagerFXMLController.class));
    }

    @Override
    public JobManagerFXMLController getController() {
        return (JobManagerFXMLController) super.getController();
    }

    public void checkGrouperModelAndRefresh() {
        getController().checkGrouperModelAndRefresh();
    }

}
