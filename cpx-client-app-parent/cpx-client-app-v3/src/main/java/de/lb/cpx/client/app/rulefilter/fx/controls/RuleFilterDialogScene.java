/*
 * Copyright (c) 2019 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner Health Care Consulting GmbH and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner Health Care Consulting GmbH
 * http://www.lohmann-birkner.de
 *
 * Contributors:
 *    2019  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.rulefilter.fx.controls;

import de.lb.cpx.client.app.rulefilter.dialog.RuleFilterApplicationUsage;
import de.lb.cpx.client.core.model.CpxScreen;
import de.lb.cpx.client.core.util.CpxFXMLLoader;
import java.io.IOException;

/**
 *
 * @author nandola
 */
//public class RuleFilterDialogScene{
public class RuleFilterDialogScene extends CpxScreen {

    private final RuleFilterApplicationUsage appUsage;

    /**
     * @param appUsage context for rule filter usage
     * @throws IOException thrown if fxml was not found or erroneous
     */
    public RuleFilterDialogScene(RuleFilterApplicationUsage appUsage) throws IOException {
        super(CpxFXMLLoader.getLoader(RuleFilterDialogFXMLController.class));
        this.appUsage = appUsage;
//        FXMLLoader.load(getClass().getResource("/fxml/RuleFilterDialogFXML.fxml"));
        getController().afterInitialisingScene();
//        getController().init();
    }

    @Override
    public RuleFilterDialogFXMLController getController() {
        return (RuleFilterDialogFXMLController) super.getController();
    }

    public RuleFilterApplicationUsage getAppUsage() {
        return appUsage;
    }

}
