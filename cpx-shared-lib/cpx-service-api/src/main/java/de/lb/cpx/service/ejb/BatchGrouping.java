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
 *    2016  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.grouper.model.transfer.BatchGroupParameter;
import de.lb.cpx.model.TCase;
import javax.ejb.Local;

/**
 *
 * @author gerschmann
 */
@Local
public interface BatchGrouping {

    Object processGrouperRequest(TCase hospitalCase, BatchGroupParameter batchGroupParameter) throws CpxIllegalArgumentException;

    Object[] processRuleGrouperRequest(TCase hospitalCase, BatchGroupParameter batchGroupParameter) throws CpxIllegalArgumentException;

//    Object[] processRuleGrouperRequest2(TCase hospitalCase, BatchGroupParameter batchGroupParameter, boolean pIsLocal) throws CpxIllegalArgumentException;
}