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

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.enums.ExportTypeEn;
import de.lb.cpx.model.enums.SearchListTypeEn;
import de.lb.cpx.server.commonDB.dao.CdbUsersDao;
import de.lb.cpx.server.config.CpxServerConfigLocal;
import de.lb.cpx.server.dao.TCaseDao;
import de.lb.cpx.server.dao.TPatientDao;
import de.lb.cpx.service.information.DatabaseInfo;
import de.lb.cpx.shared.dto.WorkingListItemDTO;
import de.lb.cpx.shared.filter.ColumnOption;
import de.lb.cpx.shared.filter.FilterOption;
import de.lb.cpx.shared.p21util.P21ExportSettings;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.jboss.ejb3.annotation.SecurityDomain;

/**
 *
 * @author Wilde
 */
@Stateless
@SecurityDomain("cpx")
public class WorkingListStatelessEJB implements WorkingListStatelessEJBRemote {

    private static final Logger LOG = Logger.getLogger(WorkingListStatelessEJB.class.getName());

    @Inject
    private WorkingListSearchService searchService;

    @Inject
    private TCaseDao caseDao;

    @Inject
    private CdbUsersDao userDao;

    @Inject
    private TPatientDao patientDao;

    @Inject
    private P21ExportEJB p21Export;

    @EJB
    private ListExportEJB listExport;

    @EJB(name = "CpxServerConfig")
    private CpxServerConfigLocal cpxServerConfig;

    @Override
    public Integer getMaxCount() {
        //return searchService.getMaxCount(isLocal);
        return caseDao.getCount();
//        ClientManager.updateCaseCount(count);
    }

    @Override
    public Integer getCanceledCount() {
        //return searchService.getMaxCount(isLocal);
        return caseDao.getCanceledCount();
    }

    @Override
    public Integer getMaxPatientCount() {
        int count = patientDao.getCount();
        return count;
    }

    @Override
    @Asynchronous
    public Future<SearchResult<WorkingListItemDTO>> find(final boolean pIsLocal, final GDRGModel pGrouperModel, final int pPage, final int pFetchSize, final Map<ColumnOption, List<FilterOption>> pOptions) {
        Set<ColumnOption> colOptions = pOptions.keySet();
        LOG.log(Level.INFO, "try to get List for ColumnSize of {0}", colOptions.size());
        SearchResult<WorkingListItemDTO> result;
        try {
            result = searchService.getAllWithCriteriaForFilter(pIsLocal, pGrouperModel, pPage, pFetchSize, pOptions);
        } catch (IOException | InterruptedException ex) {
            LOG.log(Level.SEVERE, null, ex);
            Thread.currentThread().interrupt();
            result = new SearchResult<>();
        }
        return new AsyncResult<>(result);
    }

    @Override
    public List<Long> findAllCaseIds() {
        return caseDao.getAllCasesIds();
    }

    @Override
    public List<TCase> findAllCases() {
        return caseDao.getAllCases();
    }

    @Override
    public List<TCase> findAllCasesWithPatient() {
        return caseDao.getAllCasesWithPatient();
    }

    @Override
    public DatabaseInfo getDatabaseInfo() {
        return caseDao.getDatabaseInfo();
//        return new DatabaseInfo(
//                caseDao.getConnectionDatabase(),
//                caseDao.getConnectionUrl(),
//                caseDao.getConnectionDatabaseVendor(),
//                //caseDao.isOracle(), caseDao.isSqlSrv(),
//                caseDao.getDatabaseVersion()
//        );
    }

    @Override
    public DatabaseInfo getDatabaseInfoCommon() {
        return userDao.getDatabaseInfo();
//        return new DatabaseInfo(
//                caseDao.getConnectionDatabase(),
//                caseDao.getConnectionUrl(),
//                caseDao.getConnectionDatabaseVendor(),
//                //caseDao.isOracle(), caseDao.isSqlSrv(),
//                caseDao.getDatabaseVersion()
//        );
    }

    @Override
    public TCase getCaseForDetail(long pid) {
        TCase hCase = caseDao.findById(pid);
        if (hCase == null) {
            LOG.log(Level.WARNING, "No hospital case found with this id: {0}", pid);
            return null;
        }
        hCase.getCaseDetails().iterator();
        return hCase;
    }

    @Override
    public SearchResult<WorkingListItemDTO> findWorkingList(final boolean pIsLocal, final GDRGModel pGrouperModel, final int pPage, final int pFetchSize, final java.util.Map<ColumnOption, List<FilterOption>> pOptions) {
        Set<ColumnOption> colOptions = pOptions.keySet();
        LOG.log(Level.INFO, "try to get List for ColumnSize of {0}", colOptions.size());
        SearchResult<WorkingListItemDTO> result;
        try {
            result = searchService.getAllWithCriteriaForFilter(pIsLocal, pGrouperModel, pPage, pFetchSize, pOptions);
        } catch (IOException | InterruptedException ex) {
            LOG.log(Level.SEVERE, null, ex);
            Thread.currentThread().interrupt();
            result = new SearchResult<>();
        }
        return result;
    }

    @Override
    public long prepareP21Export(final boolean pIsLocal, final GDRGModel pGrouperModel, final P21ExportSettings pP21ExportSettings, final long[] pSelectedIds, final Map<ColumnOption, List<FilterOption>> pOptions) throws IOException {
        return p21Export.prepareExport(pIsLocal, pGrouperModel, pP21ExportSettings, pSelectedIds, pOptions);
    }

    @Override
    public long prepareListExport(final boolean pIsLocal, final boolean pIsShowAllReminders, final GDRGModel pGrouperModel, final SearchListTypeEn pListType, final ExportTypeEn pExportType, final long[] pSelectedIds, final Map<ColumnOption, List<FilterOption>> pOptions) {
        return listExport.prepareExport(pIsLocal, pIsShowAllReminders, pGrouperModel, pListType, pExportType, pSelectedIds, pOptions);
    }

    @Override
    public int getMaxPhases() {
        return p21Export.getMaxPhases();
    }

    @Override
    public boolean stopExport(final long pExecutionId) {
        return p21Export.stopExport(pExecutionId);
    }

    @Override
    public boolean stopExport2(final long pExecutionId) {
        return listExport.stopExport(pExecutionId);
    }
    
    @Override
     public boolean isCaseCreateAnonymized(){
         return cpxServerConfig.getCreateCaseAnonymize();

     }

}
