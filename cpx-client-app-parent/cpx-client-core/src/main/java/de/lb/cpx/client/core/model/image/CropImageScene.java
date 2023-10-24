/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
package de.lb.cpx.client.core.model.image;

import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.util.CpxFXMLLoader;
import java.io.IOException;

/**
 *
 * @author adameck
 */
public class CropImageScene extends CpxScene {

    public CropImageScene() throws IOException {
        super(CpxFXMLLoader.getLoader(CropImageFXMLController.class));
        if (controller != null) {
            controller.afterShow();
        }
    }
}
