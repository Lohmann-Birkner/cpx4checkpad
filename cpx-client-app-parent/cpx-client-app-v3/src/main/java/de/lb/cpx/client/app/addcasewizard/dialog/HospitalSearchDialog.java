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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.addcasewizard.dialog;

import de.lb.cpx.client.app.addcasewizard.fx.control.HospitalSearchFXMLController;
import de.lb.cpx.client.core.model.CpxScreen;
import de.lb.cpx.client.core.model.catalog.CpxHospital;
import de.lb.cpx.client.core.model.fx.dialog.TitledDialog;
import de.lb.cpx.client.core.util.CpxFXMLLoader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.Modality;
import javafx.stage.Window;

/**
 *
 * @author shahin
 */
public class HospitalSearchDialog extends TitledDialog {

    //private AddCaseServiceFacade serviceFacade = new AddCaseServiceFacade();
    private HospitalSearchScreen contentScene;

    public HospitalSearchDialog(String pTitle, Window pOwner) {

        super(pTitle, pOwner, Modality.APPLICATION_MODAL);
        try {
            contentScene = new HospitalSearchScreen();
            setContent(contentScene.getRoot());
        } catch (IOException ex) {
            Logger.getLogger(HospitalSearchDialog.class.getSimpleName()).log(Level.WARNING, "Can't load HospitalsearchScreen: " + ex.getMessage(), ex);
        }
    }

    /**
     *
     * @return CpxHospital HospitalKatalog
     */
    public CpxHospital getHopital() {
        return contentScene.getHospital();
    }

    public class HospitalSearchScreen extends CpxScreen {

        private CpxHospital hospital;

        public HospitalSearchScreen() throws IOException {
            super(CpxFXMLLoader.getLoader(HospitalSearchFXMLController.class));
        }

        /**
         *
         * @return CpxHospital HospitalKatalog
         */
        public CpxHospital getHospital() {
            return hospital;
        }

        /**
         *
         * @param pHospital CpxHospital
         */
        public void setHospital(CpxHospital pHospital) {
            this.hospital = pHospital;
        }

    }

}
