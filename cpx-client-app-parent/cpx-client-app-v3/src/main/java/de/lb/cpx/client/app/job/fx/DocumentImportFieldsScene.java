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
 * Case Merging Scene StartScene for the CaseMerging, contains: TabView
 * (Overview)
 *
 * @author wilde
 */
public class DocumentImportFieldsScene extends CpxScene {

    /**
     * construct new instance
     *
     * @throws IOException thrown if fxml is not found
     */
    public DocumentImportFieldsScene() throws IOException {
        super(CpxFXMLLoader.getLoader(DocumentImportFieldsFXMLController.class));
    }

    @Override
    public DocumentImportFieldsFXMLController getController() {
        return (DocumentImportFieldsFXMLController) super.getController(); //To change body of generated methods, choose Tools | Templates.
    }

}
