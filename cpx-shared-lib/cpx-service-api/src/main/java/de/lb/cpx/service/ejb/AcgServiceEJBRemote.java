/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.lb.cpx.shared.dto.acg.AcgPatientData;
import de.lb.cpx.shared.dto.acg.IcdFarbeOrgan;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author niemeier
 */
@Remote
public interface AcgServiceEJBRemote {

    Map<Integer, AcgPatientData> getAcgData(final String pPatientNumber);

    List<IcdFarbeOrgan> getIcdData(long pCaseId);

}