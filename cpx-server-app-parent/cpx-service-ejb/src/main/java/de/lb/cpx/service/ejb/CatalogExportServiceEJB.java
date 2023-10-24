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
 *    2018 Anna Gerschmann - Export of the DRG/PEPP/Supplementary fees Catalogs into drgm files in WD_SERVER\catalog
 */
package de.lb.cpx.service.ejb;

import de.checkpoint.drg.RuleCodeMgr;
import de.lb.cpx.server.commonDB.dao.CDrgCatalogDao;
import de.lb.cpx.server.commonDB.dao.CPeppCatalogDao;
import de.lb.cpx.server.commonDB.dao.CSupplementaryFeeDao;
import de.lb.cpx.server.commons.dao.AbstractDrgmCatalogEntity;
import de.lb.cpx.service.drgmexport.DrgmExportService;
import de.lb.cpx.service.information.CatalogTypeEn;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * @author gerschmann
 */
@Stateless
public class CatalogExportServiceEJB implements CatalogExportServiceEJBRemote {

    private static final Logger LOG = Logger.getLogger(CatalogExportServiceEJB.class.getName());

    @EJB
    private CDrgCatalogDao drgCatalogDao;
    @EJB
    private CPeppCatalogDao peppCatalogDao;
    @EJB
    private CSupplementaryFeeDao suppFeeDao;

//    DrgmExportService drgmExportService = new DrgmExportService();
    @Override
    public boolean checkDrgmFileExists(final CatalogTypeEn pCatalogType, final int pYear) {

        String fileName = getDrgmExportService().getFileName2CatalogTypeYear(pCatalogType, pYear);
        if (fileName == null) {
            return false;
        }
        String catalogPath = RuleCodeMgr.getCatalogPath();
        File pFile = new File(catalogPath + fileName);
        if (pFile.exists() && pFile.isFile()) {
            LOG.log(Level.INFO, " file " + catalogPath + fileName + " is found");
            return true;
        }
        return false;
    }

    @Override
    public boolean exportCatalog(final CatalogTypeEn pCatalogType, final int pYear, final String pCountryEn) {
        if (pCatalogType == null) {
            return false;
        }
        DrgmExportService drgmExportService = getDrgmExportService();
        String fileName = drgmExportService.getFileName2CatalogTypeYear(pCatalogType, pYear);
        if (fileName == null) {
            return false;
        }
        List<? extends AbstractDrgmCatalogEntity> result;
        switch (pCatalogType) {
            case DRG:
                result = drgCatalogDao.getEntries(pYear, pCountryEn);
                break;
            case PEPP:
                result = peppCatalogDao.getEntries(pYear, pCountryEn);
                break;
            case ET:
            case ZP:
            case ZE:
                result = suppFeeDao.getEntries(pYear, pCountryEn, pCatalogType.name());
                break;
            default: {
                LOG.log(Level.WARNING, "unknown catalog type:" + pCatalogType.toString());
                return false;
            }
        }

        if (result != null) {
            return drgmExportService.saveDrgmFile(result, fileName);
        }
        return false;
    }

    private DrgmExportService getDrgmExportService() {
        return new DrgmExportService();
    }

}
