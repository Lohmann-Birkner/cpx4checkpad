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
package de.lb.cpx.sap.kain_inka;

import java.util.Date;
import java.util.List;

/**
 *
 * @author niemeier
 */
public interface PvvResultIf {

    /**
     *
     * @param info info
     */
    void setInformation(String info);

    /**
     *
     * @param nr bill number
     */
    void setBillNumber(String nr);

    /**
     *
     * @param date bill date
     */
    void setBillDate(Date date);

    /**
     *
     * @param pvt pvt
     */
    void addPVT(PvtResultIf pvt);

    /**
     *
     * @return information
     */
    String getInformation();

    /**
     *
     * @return bill number
     */
    String getBillNumber();

    /**
     *
     * @return bill date
     */
    Date getBillDate();

    /**
     *
     * @return list of pvt results
     */
    List<PvtResultIf> getPVTResultList();

}
