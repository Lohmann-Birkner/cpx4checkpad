/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.catalog;

import de.lb.cpx.server.commonDB.model.CHospital;
import de.lb.cpx.shared.lang.Lang;

/**
 *
 * @author Dirk Niemeier
 */
public class CpxHospital extends CHospital {
    
    private static final long serialVersionUID = 1L;
    
    
    public String getStateDescription() {
        if (this.getStateEn() == null) {
            return "";
        }
        //return CpxLanguage.instance().get(getStateEn().getLangKey());
        return Lang.get(getStateEn().getLangKey()).value;
    }

    @Override
    public String toString() {
        return Lang.getHospitalIdentifier() + ": " + getTextRequireNotNull(getHosIdent()) + "\n"
                + Lang.getHospitalName() + ": " + getTextRequireNotNull(getHosName()) + "\n"
                //                + Lang.getAddressPhoneNumber()+": ".concat(getInscPhonePrefix()!=null?getInscPhonePrefix():"")
                //                + "".concat(getInscPhone()!=null?getInscPhone():"")+ "\n"
                //                + Lang.getInsuranceType()+": " + getInscShort() + "\n"
                + Lang.getAddress() + ": " + getTextRequireNotNull(getHosAddress()) + "\n"
                + Lang.getAddressCity() + ": " + (getHosZipCode()!=null?(getHosZipCode()+ " "):"") + getTextRequireNotNull(getHosCity()) + "\n";
    }

    /**
     * @return hospital short Description withput hospitalname
     */
    public String toShortDescription() {
        return Lang.getHospitalIdentifier() + ": " + getTextRequireNotNull(getHosIdent()) + "\n"
                //                + Lang.getHospitalName() + ": " + getHosName() + "\n"
                //                + Lang.getAddressPhoneNumber()+": ".concat(getInscPhonePrefix()!=null?getInscPhonePrefix():"")
                //                + "".concat(getInscPhone()!=null?getInscPhone():"")+ "\n"
                //                + Lang.getInsuranceType()+": " + getInscShort() + "\n"
                + Lang.getAddress() + ": " + getTextRequireNotNull(getHosAddress()) + "\n"
                + Lang.getAddressCity() + ": " + (getHosZipCode()!=null?(getHosZipCode()+ " "):"") + getTextRequireNotNull(getHosCity()) + "\n";
    }
}
