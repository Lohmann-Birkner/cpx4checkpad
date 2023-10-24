/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.process.section.operations;

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.wm.model.TWmProcess;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author wilde
 */
public class WmProcessContinuedOperations extends WmProcessOperations{
    public WmProcessContinuedOperations(ProcessServiceFacade pFacade) {
        super(pFacade);
    }

    @Override
    public List<ItemEventHandler> getDefaultOperations(TWmProcess pItem) {
        return new ArrayList<>();
    }
}
