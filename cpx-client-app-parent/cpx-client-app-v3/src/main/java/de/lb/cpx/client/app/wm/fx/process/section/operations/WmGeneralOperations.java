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
package de.lb.cpx.client.app.wm.fx.process.section.operations;

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmProcessHospital;

/**
 *
 * @author niemeier
 */
public class WmGeneralOperations extends WmOperations<TWmProcessHospital> {

    public WmGeneralOperations(ProcessServiceFacade pFacade) {
        super(pFacade);
    }

    @Override
    public String getItemName() {
        return Lang.getEventNameProcess();
    }

}
