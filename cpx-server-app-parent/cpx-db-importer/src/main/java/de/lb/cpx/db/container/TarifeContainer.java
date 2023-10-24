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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.db.container;

import de.lb.cpx.db.importer.utils.Constants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class TarifeContainer implements Serializable {

    private static final long serialVersionUID = 1L;
    private String nr = "xyz";
    private String name = "<" + Constants.CHECKRESULT_TYP_UNKNOWN + ">";
    private final ArrayList<TarifeGueltigContainer> t = new ArrayList<>();

    public TarifeGueltigContainer getGueltigenTarifContainer(Date date) {
        if (date != null) {
            for (int i = t.size() - 1; i >= 0; i--) {
                TarifeGueltigContainer tg = t.get(i);
                if (tg.getVon() != null && tg.getBis() != null
                        && (tg.getVon().before(date) && tg.getBis().after(date)
                        || tg.getVon().equals(date) || tg.getBis().equals(date))) {
                    return tg;
                } else if (tg.getBis() != null && (tg.getBis().after(date) || tg.getBis().equals(date))) {
                    return tg;
                } else if (tg.getVon() != null && (tg.getVon().before(date) || tg.getVon().equals(date))) {
                    return tg;
                }
            }
            if (t.size() == 1) {
                TarifeGueltigContainer tg = t.get(0);
                if (tg.getVon() == null && tg.getBis() == null) {
                    return tg;
                }
            }
        }
        return null;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the nr
     */
    public String getNr() {
        return nr;
    }

    /**
     * @param nr the nr to set
     */
    public void setNr(String nr) {
        this.nr = nr;
    }

    public void addTarif(TarifeGueltigContainer tg) {
        if (tg == null) {
            return;
        }
        t.add(tg);
    }
}
