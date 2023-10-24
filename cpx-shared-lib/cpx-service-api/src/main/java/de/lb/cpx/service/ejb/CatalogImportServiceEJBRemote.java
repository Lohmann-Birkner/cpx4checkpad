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
package de.lb.cpx.service.ejb;

import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.server.commons.dao.AbstractCatalogEntity;
import de.lb.cpx.service.information.CpxCatalogOverview;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Dirk Niemeier
 */
@Remote
public interface CatalogImportServiceEJBRemote {

    List<CpxCatalogOverview> checkCatalogOverview(final List<CpxCatalogOverview> pCatalogOverviewList);

    List<CpxCatalogOverview> checkCatalogOverview(final List<CpxCatalogOverview> pCatalogOverviewList, final boolean pSingleCatalog);

    List<? extends AbstractCatalogEntity> getCatalog(final CpxCatalogOverview pCatalogOverview) throws CpxIllegalArgumentException;

}
