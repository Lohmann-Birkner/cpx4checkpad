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

import de.lb.cpx.client.app.addcasewizard.fx.control.InsuranceSearchFXMLController;
import de.lb.cpx.client.core.model.CpxScreen;
import de.lb.cpx.client.core.model.catalog.CpxInsuranceCompany;
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
public class InsuranceSearchDialog extends TitledDialog {

    private static final Logger LOG = Logger.getLogger(InsuranceSearchDialog.class.getName());

    //private AddCaseServiceFacade serviceFacade = new AddCaseServiceFacade();
    private InsuranceSearchScreen contentScene;

    public InsuranceSearchDialog(String pTitle, Window pOwner) {

        super(pTitle, pOwner, Modality.APPLICATION_MODAL);
        try {
            contentScene = new InsuranceSearchScreen();
            setContent(contentScene.getRoot());
        } catch (IOException ex) {
            LOG.log(Level.WARNING, "Can't load InsurancesearchScreen: " + ex.getMessage(), ex);
        }
    }

    /**
     *
     * @return CpxInsuranceCompany InsuranceKatalog
     */
    public CpxInsuranceCompany getInsurance() {
        return contentScene.getInsurance();
    }

    public class InsuranceSearchScreen extends CpxScreen {

        private CpxInsuranceCompany insurance;

        public InsuranceSearchScreen() throws IOException {
            super(CpxFXMLLoader.getLoader(InsuranceSearchFXMLController.class));
        }

        /**
         *
         * @return CpxInsuranceCompany InsuranceKatalog
         */
        public CpxInsuranceCompany getInsurance() {
            return insurance;
        }

        /**
         *
         * @param pInsurance CpxInsuranceCompany
         */
        public void setInsurance(CpxInsuranceCompany pInsurance) {
            this.insurance = pInsurance;
        }

    }

}
