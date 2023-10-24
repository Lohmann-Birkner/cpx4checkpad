/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.p21_export;

import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.util.CpxFXMLLoader;
import java.io.IOException;

/**
 *
 * @author wilde
 */
public class P21ExportScene extends CpxScene {

    public P21ExportScene() throws IOException {
        //super(CpxFXMLLoader.getLoader("/fxml/EasyCoderDialogFXML.fxml", new EasyCoderDialogFXMLController()));//CpxFXMLLoader.getLoader(EasyCoderDialogFXMLController.class));
        super(CpxFXMLLoader.getLoader(P21ExportFXMLController.class));
    }

    @Override
    public P21ExportFXMLController getController() {
        return (P21ExportFXMLController) super.getController();
    }

//    @Override
//    public boolean close() {
//        return super.close(); //To change body of generated methods, choose Tools | Templates.
//    }
}
