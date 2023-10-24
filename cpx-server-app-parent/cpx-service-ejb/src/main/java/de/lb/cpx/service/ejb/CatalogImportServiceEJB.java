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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.server.commonDB.dao.CAtcDao;
import de.lb.cpx.server.commonDB.dao.CBaserateDao;
import de.lb.cpx.server.commonDB.dao.CDepartmentDao;
import de.lb.cpx.server.commonDB.dao.CDoctorDao;
import de.lb.cpx.server.commonDB.dao.CDrgCatalogDao;
import de.lb.cpx.server.commonDB.dao.CHospitalDao;
import de.lb.cpx.server.commonDB.dao.CIcdCatalogDao;
import de.lb.cpx.server.commonDB.dao.CIcdThesaurusDao;
import de.lb.cpx.server.commonDB.dao.CInsuranceCompanyDao;
import de.lb.cpx.server.commonDB.dao.CMdkDao;
import de.lb.cpx.server.commonDB.dao.COpsCatalogDao;
import de.lb.cpx.server.commonDB.dao.COpsAopDao;
import de.lb.cpx.server.commonDB.dao.COpsThesaurusDao;
import de.lb.cpx.server.commonDB.dao.CPeppCatalogDao;
import de.lb.cpx.server.commonDB.dao.CPznDao;
import de.lb.cpx.server.commonDB.dao.CSupplementaryFeeDao;
import de.lb.cpx.server.commonDB.dao.VwCatalogOverviewDao;
import de.lb.cpx.server.commonDB.model.VwCatalogOverview;
import de.lb.cpx.server.commons.dao.AbstractCatalogEntity;
import de.lb.cpx.service.information.CatalogTodoEn;
import de.lb.cpx.service.information.CatalogTypeEn;
import de.lb.cpx.service.information.CpxCatalogOverview;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRES_NEW;
import javax.enterprise.context.ApplicationScoped;
import org.jboss.ejb3.annotation.TransactionTimeout;

/**
 *
 * @author Dirk Niemeier
 */
@Stateful
@ApplicationScoped
public class CatalogImportServiceEJB implements CatalogImportServiceEJBRemote {

    private static final Logger LOG = Logger.getLogger(CatalogImportServiceEJB.class.getName());

    @EJB
    private CDrgCatalogDao drgCatalogDao;
    @EJB
    private CPeppCatalogDao peppCatalogDao;
    @EJB
    private CIcdCatalogDao icdCatalogDao;
    @EJB
    private COpsCatalogDao opsCatalogDao;
    @EJB
    private COpsAopDao opsAopDao;
    @EJB
    private CHospitalDao hospitalDao;
    @EJB
    private CInsuranceCompanyDao insuranceCompanyDao;
    @EJB
    private CDepartmentDao departmentDao;
    @EJB
    private CDoctorDao doctorDao;
    @EJB
    private CAtcDao atcDao;
    @EJB
    private CPznDao pznDao;
    @EJB
    private CMdkDao mdkDao;
    @EJB
    private CSupplementaryFeeDao suppFeeDao;
    @EJB
    private CBaserateDao baserateDao;
    @EJB
    private CIcdThesaurusDao icdThesaurusDao;
    @EJB
    private COpsThesaurusDao opsThesaurusDao;
    @EJB
    private VwCatalogOverviewDao catalogOverviewDao;

    @Override
    public List<CpxCatalogOverview> checkCatalogOverview(final List<CpxCatalogOverview> pCatalogOverviewList) {
        return checkCatalogOverview(pCatalogOverviewList, false);
    }

    @Override
    public List<CpxCatalogOverview> checkCatalogOverview(final List<CpxCatalogOverview> pCatalogOverviewList, final boolean pSingleCatalog) {
        final List<VwCatalogOverview> list;
        if (pSingleCatalog) {
            CpxCatalogOverview catalogOverview = pCatalogOverviewList.isEmpty() ? null : pCatalogOverviewList.get(0);
            list = catalogOverview == null ? new ArrayList<>() : catalogOverviewDao.find(catalogOverview.countryEn, catalogOverview.catalog, catalogOverview.year);
        } else {
            list = catalogOverviewDao.findAll();
        }

        //At the beginning we suppose, that the client can delete all catalogs
        for (CpxCatalogOverview cpxCatalogOverview : pCatalogOverviewList) {
            cpxCatalogOverview.setTodo(CatalogTodoEn.DELETE);
        }

        //Now we'll investigate, if catalogs have to be imported or reimported
        for (VwCatalogOverview catalogOverview : list) {
            CpxCatalogOverview cpxCatalogOverview = null;
            //Okay, does the server catalog already exists on this client...?
            for (CpxCatalogOverview cpxCatalogOverviewTmp : pCatalogOverviewList) {
                if (cpxCatalogOverviewTmp.getCatalog().equalsIgnoreCase(catalogOverview.getCatalog())
                        && cpxCatalogOverviewTmp.getCountryEn().equalsIgnoreCase(catalogOverview.getCountryEn())
                        && cpxCatalogOverviewTmp.getYear() == catalogOverview.getYear()) {
                    cpxCatalogOverview = cpxCatalogOverviewTmp;
                    break;
                }
            }

            if (cpxCatalogOverview == null) {
                //No, catalog does not exist on this client. New catalog found, client has to download it!
                CpxCatalogOverview newCatalog = new CpxCatalogOverview(0,
                        catalogOverview.getCatalog(),
                        catalogOverview.getCountryEn(),
                        catalogOverview.getYear(),
                        0,
                        0L,
                        0L,
                        null);
                newCatalog.setTodo(CatalogTodoEn.IMPORT);
                newCatalog.setNewCount(catalogOverview.getCnt());
                newCatalog.setNewMinId(catalogOverview.getMinId());
                newCatalog.setNewMaxId(catalogOverview.getMaxId());
                newCatalog.setNewDate(catalogOverview.getDate());
                pCatalogOverviewList.add(newCatalog);
            } else {
                //Yes, catalog already exists on this client. We'll see, if the catalog has changed and if the catalog has to reimport it.
                cpxCatalogOverview.setTodo(CatalogTodoEn.NONE);
                boolean changed = false;
                if (!changed && cpxCatalogOverview.getCount() != catalogOverview.getCnt()) {
                    changed = true;
                }
                if (!changed && cpxCatalogOverview.getMinId() != catalogOverview.getMinId()) {
                    changed = true;
                }
                if (!changed && cpxCatalogOverview.getMaxId() != catalogOverview.getMaxId()) {
                    changed = true;
                }
                if (cpxCatalogOverview.getDate() == null && catalogOverview.getDate() == null) {
                    LOG.log(Level.WARNING, "CREATION_DATE seems to be null. You should set it to any date (it's okay to use GETDATE() or NOW here)!");
                }
                if (!changed && ((cpxCatalogOverview.getDate() == null && catalogOverview.getDate() != null)
                        || (cpxCatalogOverview.getDate() != null && catalogOverview.getDate() == null)
                        || (cpxCatalogOverview.getDate().compareTo(catalogOverview.getDate()) != 0))) {
                    changed = true;
                }
                if (changed) {
                    //Has obviously changed!
                    cpxCatalogOverview.setTodo(CatalogTodoEn.REIMPORT);
                    cpxCatalogOverview.setNewCount(catalogOverview.getCnt());
                    cpxCatalogOverview.setNewMinId(catalogOverview.getMinId());
                    cpxCatalogOverview.setNewMaxId(catalogOverview.getMaxId());
                    cpxCatalogOverview.setNewDate(catalogOverview.getDate());
                }
            }
        }
        Collections.sort(pCatalogOverviewList);
        return pCatalogOverviewList;
    }

    @Override
    @TransactionAttribute(REQUIRES_NEW)
    @TransactionTimeout(value = 60, unit = TimeUnit.MINUTES)
    //@Asynchronous
    public List<? extends AbstractCatalogEntity> getCatalog(CpxCatalogOverview pCatalogOverview) throws CpxIllegalArgumentException {
        List<? extends AbstractCatalogEntity> result = new ArrayList<>();
        if (pCatalogOverview == null) {
            return result;
        }
        CatalogTypeEn catalogType = pCatalogOverview.getCatalogType();
        //String catalog = pCatalogOverview.getCatalog();
        //catalog = (catalog == null)?"":catalog.trim().toUpperCase();
        if (catalogType != null) {
            switch (catalogType) {
                case DRG:
                    result = drgCatalogDao.getEntries(pCatalogOverview.getYear(), pCatalogOverview.getCountryEn());
                    break;
                case PEPP:
                    result = peppCatalogDao.getEntries(pCatalogOverview.getYear(), pCatalogOverview.getCountryEn());
                    break;
                case ICD:
                    result = icdCatalogDao.getEntries(pCatalogOverview.getYear(), pCatalogOverview.getCountryEn());
                    break;
                case OPS:
                    result = opsCatalogDao.getEntries(pCatalogOverview.getYear(), pCatalogOverview.getCountryEn());
                    break;
                case HOSPITAL:
                    result = hospitalDao.getEntries(pCatalogOverview.getCountryEn());
                    break;
                case INSURANCE_COMPANY:
                    result = insuranceCompanyDao.getEntries(pCatalogOverview.getCountryEn());
                    break;
                case DEPARTMENT:
                    result = departmentDao.getEntries(pCatalogOverview.getCountryEn());
                    break;
                case DOCTOR:
                    result = doctorDao.getEntries(pCatalogOverview.getCountryEn());
                    break;
                case ATC:
                    result = atcDao.getEntries(pCatalogOverview.getCountryEn());
                    break;
                case PZN:
                    result = pznDao.getEntries(pCatalogOverview.getCountryEn());
                    break;
                case MDK:
                    result = mdkDao.getEntries(pCatalogOverview.getCountryEn());
                    break;
                case BASERATE:
                    result = baserateDao.getEntries(pCatalogOverview.getCountryEn());
                    break;
                case ICD_THESAURUS:
                    result = icdThesaurusDao.getEntries(pCatalogOverview.getYear(), pCatalogOverview.getCountryEn());
                    break;
                case OPS_THESAURUS:
                    result = opsThesaurusDao.getEntries(pCatalogOverview.getYear(), pCatalogOverview.getCountryEn());
                    break;
                case ET:
                case ZP:
                case ZE:
                    result = suppFeeDao.getEntries(pCatalogOverview.getYear(), pCatalogOverview.getCountryEn(), catalogType.name());
                    break;
                case OPS_AOP:
                    result = opsAopDao.getEntries(pCatalogOverview.getYear(), pCatalogOverview.getCountryEn()); 
                    break;
                default:
                    throw new CpxIllegalArgumentException("Catalog '" + pCatalogOverview.getCatalog() + "' cannot be downloaded, this catalog is not a valid argument!");
            }
        }
        return result;
    }

}
