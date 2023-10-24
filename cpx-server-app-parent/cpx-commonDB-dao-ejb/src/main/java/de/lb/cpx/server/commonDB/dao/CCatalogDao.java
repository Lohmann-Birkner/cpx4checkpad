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
 */
package de.lb.cpx.server.commonDB.dao;

import de.lb.cpx.server.commonDB.model.CCatalog;
import java.util.List;
import javax.ejb.Stateless;
import org.jboss.ejb3.annotation.SecurityDomain;

/**
 * Data access object for domain model class CCatalog. Initially generated at
 * 02.02.2016 17:27:35 by Hibernate Tools 3.2.2.GA.
 *
 * @author Hibernate Tools
 */
@Stateless
@SecurityDomain("cpx")
public class CCatalogDao extends AbstractCommonDao<CCatalog> {

    /**
     * Creates a new instance.
     */
    public CCatalogDao() {
        super(CCatalog.class);
    }

    public CCatalog getCatalog(final String pModelId, final String pCountryShortName) {
        String countryShortName = (pCountryShortName == null) ? "" : pCountryShortName.trim().toLowerCase();
        List<CCatalog> lResults = getEntityManager().createNamedQuery("findCatalogByModelId", CCatalog.class)
                .setParameter("model_id", pModelId)
                .setParameter("country", countryShortName)
                .getResultList();

        if (lResults.isEmpty()) {
            return null;
        }
        return lResults.get(0);
    }

}
