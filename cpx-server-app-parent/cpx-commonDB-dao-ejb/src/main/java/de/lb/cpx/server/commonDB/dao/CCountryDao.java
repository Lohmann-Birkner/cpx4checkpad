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
package de.lb.cpx.server.commonDB.dao;

import de.lb.cpx.server.commonDB.model.CCountry;
import java.util.List;
import javax.ejb.Stateless;
import org.jboss.ejb3.annotation.SecurityDomain;

/**
 * Data access object for domain model class CCountry. Initially generated at
 * 02.02.2016 17:27:35 by Hibernate Tools 3.2.2.GA.
 *
 * @author Hibernate Tools
 */
@Stateless
@SecurityDomain("cpx")
public class CCountryDao extends AbstractCommonDao<CCountry> {

    /**
     * Creates a new instance.
     */
    public CCountryDao() {
        super(CCountry.class);
    }

    public CCountry getCountry(final String pShortName) {
        String shortName = (pShortName == null) ? "" : pShortName.trim().toLowerCase();
        List<CCountry> lResults = getEntityManager().createNamedQuery("findCountryByShortName", CCountry.class)
                .setParameter("short_name", shortName)
                .getResultList();

        if (lResults.isEmpty()) {
            return null;
        }
        return lResults.get(0);
    }

}
