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
package de.lb.cpx.client.app.wm.fx.process.completion;

import de.lb.cpx.client.core.model.CpxScreen;
import de.lb.cpx.client.core.util.CpxFXMLLoader;
import java.io.IOException;

/**
 * Implements the screen of the process completion TODO: Make more generic to
 * handle different process types As of today ONLY process hospital with drg is
 * supported! AWi:20170407
 *
 * @author wilde
 */
public class ProcessCompletionScreen extends CpxScreen {

    /**
     * contstruct new screen
     *
     * @throws IOException thrown when no fxml is found
     */
    public ProcessCompletionScreen() throws IOException {
        super(CpxFXMLLoader.getLoader(ProcessCompletionFXMLController.class));
    }

    @Override
    public ProcessCompletionFXMLController getController() {
        return (ProcessCompletionFXMLController) super.getController();
    }
}
