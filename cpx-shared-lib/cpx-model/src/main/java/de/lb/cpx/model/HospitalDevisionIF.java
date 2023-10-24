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
package de.lb.cpx.model;

import java.util.Date;
import java.util.Set;

/**
 *
 * @author gerschmann
 */
public interface HospitalDevisionIF {

    void setCaseIcds(final Set<TCaseIcd> caseIcds);

    void setCaseOpses(final Set<TCaseOps> caseOpses);
    public Set<TCaseOps> getCaseOpses();
    public Set<TCaseIcd> getCaseIcds();
    public Date getStartDate();
    public Date getEndDate();
}
