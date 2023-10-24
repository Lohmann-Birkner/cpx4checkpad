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
 *    2017  adameck - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.settings;

import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.util.CpxFXMLLoader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author adameck
 */
public class SettingsDialogScene extends CpxScene {

    public SettingsDialogScene() throws IOException {
        super(CpxFXMLLoader.getLoader(SettingsFXMLController.class));
        getController().afterInitialisingScene();
    }

    @Override
    public SettingsFXMLController getController() {
        return (SettingsFXMLController) super.getController();
    }

    /**
     * @return list of additional categorys displayed in Dialog scene
     */
    protected List<SettingsCategory> getAdditionalCategorys() {
        return new ArrayList<>();
    }

    public void selectCategory(String pCategory) {
        getController().selectCategory(pCategory);
    }
    
}
