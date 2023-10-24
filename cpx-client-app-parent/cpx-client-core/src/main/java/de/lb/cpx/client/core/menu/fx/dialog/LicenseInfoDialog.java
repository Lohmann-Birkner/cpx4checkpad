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
package de.lb.cpx.client.core.menu.fx.dialog;

import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.config.Session;
import static de.lb.cpx.client.core.menu.fx.ToolbarMenuFXMLController.infoLabel;
import static de.lb.cpx.client.core.menu.fx.ToolbarMenuFXMLController.keyLabel;
import de.lb.cpx.client.core.model.fx.dialog.TitledDialog;
import de.lb.cpx.cpx.license.crypter.License;
import de.lb.cpx.shared.lang.Lang;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 *
 * @author niemeier
 */
public class LicenseInfoDialog extends TitledDialog {

    private static final Logger LOG = Logger.getLogger(LicenseInfoDialog.class.getName());
    private static LicenseInfoDialog dialogInstance = null;

    public static synchronized void showDialog() {
        if (dialogInstance == null) {
            dialogInstance = new LicenseInfoDialog();
        } else {
            dialogInstance.getDialogSkin().centerWindow();
        }
        dialogInstance.show();
    }

    /**
     * construct new instance
     *
     */
    private LicenseInfoDialog() {
        super("Lizenz", MainApp.getWindow(), false);
        //setModality(Modality.NONE);
        License lic = Session.instance().getLicense();
        GridPane gpLic = new GridPane();
        if (lic == null) {
            Label noLicense = infoLabel("Keine Lizenz gefunden", null);
            gpLic.add(noLicense, 0, 0);
        } else {
            final String delimiter = ", ";
            final String placeholder = "k.A.";

            final String modules = String.join(delimiter, lic.getModuleList());
            final String hospitals = lic.isAllowAllHospitals() ? "Alle Krankenhäuser" : String.join(delimiter, lic.getHospList());
            final String departments = lic.isAllowAllDepartments() ? "Alle Fachabteilungen" : String.join(delimiter, lic.getDeptList());
            final String insurances = lic.isAllowAllInsurances() ? "Alle Versicherungen" : String.join(delimiter, lic.getInsCompList());
            
            int row = 0;
            
            if (lic.getCustomer() != null) {
                gpLic.add(keyLabel("Kunde"), 0, ++row);
                gpLic.add(infoLabel(lic.getCustomer().getShortName() + " (" + lic.getCustomerType().getShortName() + ")", null), 1, row);
            }

            gpLic.add(keyLabel("Titel"), 0, ++row);
            gpLic.add(infoLabel(lic.getCustName(), null), 1, row);
            gpLic.add(keyLabel("Ablaufdatum "), 0, ++row);
            gpLic.add(infoLabel(Lang.toDate(lic.getValidDate()), null), 1, row);
            gpLic.add(keyLabel("Max. Anzahl Fälle "), 0, ++row);
            gpLic.add(infoLabel(lic.getCaseLimit() == null ? "n.V." : lic.getCaseLimit() + "", null), 1, row);
            gpLic.add(keyLabel("Module"), 0, ++row);
            gpLic.add(infoLabel(modules.isEmpty() ? placeholder : modules, null), 1, row);
            gpLic.add(keyLabel("Krankenhäuser"), 0, ++row);
            gpLic.add(infoLabel(hospitals.isEmpty() ? placeholder : hospitals, null), 1, row);
            gpLic.add(keyLabel("Fachabteilungen"), 0, ++row);
            gpLic.add(infoLabel(departments.isEmpty() ? placeholder : departments, null), 1, row);
            gpLic.add(keyLabel("Versicherungen"), 0, ++row);
            gpLic.add(infoLabel(insurances.isEmpty() ? placeholder : insurances, null), 1, row);
        }
        gpLic.setVgap(10);
        getDialogPane().setContent(gpLic);
        getDialogSkin().setButtonTypes(ButtonType.OK);
//            MainApp.showInfoMessageDialog("Lizenz", gpLic);

        showingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    LOG.log(Level.INFO, "close license info dialog");
                    dialogInstance = null;
                }
            }
        });
    }

}
