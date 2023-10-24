/* 
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.util.cpx_handler;

import de.lb.cpx.service.properties.SearchListProperties;
import de.lb.cpx.service.searchlist.SearchListResult;

/**
 *
 * @author niemeier
 */
public class DummyCpxHandleManager extends BasicCpxHandleManager {

    @Override
    protected void performProtocolCaseHandler(final CpxHandleMessage pMessage) {
        throw new UnsupportedOperationException("Performing case protocol handler is not supported in CPX Core.");
    }

    @Override
    protected void performProtocolWorkflowHandler(final CpxHandleMessage pMessage) {
        throw new UnsupportedOperationException("Performing workflow protocol handler is not supported in CPX Core.");
    }

    @Override
    protected boolean performProtocolUnknownHandler(final CpxHandleMessage pMessage) {
        throw new UnsupportedOperationException("Performing unknown protocol handler is not supported in CPX Core.");
    }

    @Override
    protected void importFilter(final SearchListResult pExistingSearchList, final SearchListProperties pSearchList) {
        throw new UnsupportedOperationException("Import of filters is not supported in CPX Core.");
    }

}
