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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.startup_ejb;

import de.lb.cpx.cpx.license.crypter.License;
import de.lb.cpx.db.importer.ImportProcessDb; 
import de.lb.cpx.gdv.gdvimport.GdvImportDocumentProcess;
import de.lb.cpx.sap.container.FallContainer;
import de.lb.cpx.sap.importer.ImportProcessSap;
import de.lb.cpx.sap.importer.Sap;
import de.lb.cpx.server.config.CpxServerConfigLocal;
import de.lb.cpx.service.ejb.CpxP21ImportBean;
import de.lb.cpx.service.ejb.LoginServiceEJB;
import de.lb.cpx.service.information.DatabaseInfo;
import de.lb.cpx.shared.dto.job.config.CpxDatabaseBasedImportJob;
import de.lb.cpx.shared.dto.job.config.CpxExternalSystemBasedJobImportConfig;
import de.lb.cpx.shared.dto.job.config.CpxJobConfig;
import de.lb.cpx.shared.dto.job.config.CpxJobImportConfig;
import de.lb.cpx.shared.dto.job.config.database.KissmedJob;
import de.lb.cpx.shared.dto.job.config.database.MedicoJob;
import de.lb.cpx.shared.dto.job.config.database.NexusJob;
import de.lb.cpx.shared.dto.job.config.database.OrbisJob;
import de.lb.cpx.shared.dto.job.config.file.FdseJob;
import de.lb.cpx.shared.dto.job.config.file.P21Job;
import de.lb.cpx.shared.dto.job.config.other.BatchgroupingJob;
import de.lb.cpx.shared.dto.job.config.other.BillJob;
import de.lb.cpx.shared.dto.job.config.other.GdvImportDocumentJob;
import de.lb.cpx.shared.dto.job.config.other.SapJob;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.system.properties.CpxSystemProperties;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import module.Fdse;
import module.Kissmed;
import module.Medico;
import module.Nexus;
import module.Orbis;
import module.P21;
import module.impl.ImportConfig;
import org.jboss.ejb3.annotation.TransactionTimeout;
import process.impl.ImportProcessFile;

@Singleton
@Startup
@DependsOn({"LicenseReadBean", /* "UpdateDbBean", */ "CatalogImportBean"})
@Lock(LockType.READ)
@TransactionManagement(TransactionManagementType.BEAN)
public class JobSchedulerBean {

    private static final Logger LOG = Logger.getLogger(JobSchedulerBean.class.getName());
    @EJB(name = "CpxServerConfig")
    private CpxServerConfigLocal cpxServerConfig;

    @Resource
    TimerService timerService;

    @EJB
    private LicenseReadBean licenseBean;

    @Inject
    private CpxP21ImportBean importBean;

//    @EJB
//    private LockServiceBean dBLockService;
    @Inject
    private LoginServiceEJB loginService;

//    private final List<Timer> timers = new ArrayList<>();
    @PostConstruct
    private void initSchedules() {
        if (CpxSystemProperties.getInstance().getCpxWebAppFile()) {
            LOG.log(Level.INFO, "don't initialize scheduler jobs in WebApp");
            return;
        }
        LOG.log(Level.INFO, "initialize scheduling jobs...");
        Map<Integer, CpxJobConfig> result = cpxServerConfig.getJobConfigs();
        for (Map.Entry<Integer, CpxJobConfig> entry : result.entrySet()) {
            CpxJobConfig config = entry.getValue();
            initJob(config);
        }
    }

    public boolean initJob(CpxJobConfig config) {
        if (config == null) {
            LOG.log(Level.FINEST, "job config cannot be null!");
            return false;
        }
        cancelTimer(config);
//        if (!config.isActive()) {
//            LOG.log(Level.FINEST, "job is inactive: " + config.getClassName() + "/" + config.getName());
//            return false;
//        }
        final Date date = new Date();
//        if (config.getBeginDate() != null && config.getBeginDate().after(date)) {
//            LOG.log(Level.FINEST, "job has to be started in future (" + config.getBeginDate() + "): " + config.getClassName() + "/" + config.getName());
//            return false;
//        }
//        if (config.getEndDate() != null && config.getEndDate().before(date)) {
//            LOG.log(Level.FINEST, "job was finished in the past (" + config.getEndDate() + "): " + config.getClassName() + "/" + config.getName());
//            return false;
//        }
//        if (config.getTimePeriodUnit() == null) {
//            LOG.log(Level.SEVERE, "job has no time period unit or given time period unit is invalid. Cannot initialize job: " + config.getClassName() + "/" + config.getName());
//            return false;
//        }
        //Timer timer = timerService.createTimer(0, 1000, config);
        TimerConfig timerConfig = new TimerConfig();
        timerConfig.setInfo(config);
        timerConfig.setPersistent(false);
        //ScheduleExpression schedule = new ScheduleExpression();
//            if (config.getBeginDate() != null) {
//                schedule.start(config.getBeginDate());
//            }
//            if (config.getBeginDate() != null) {
//                schedule.end(config.getEndDate());
//            }
        //Date initialExpiration = config.getBeginDate();
        long initialDuration = 0L;
        long intervalDuration = config.getTimePeriodUnit().getDuration().toMillis() * config.getTimePeriodValue();
        if (config.getBeginDate() != null) {
            LocalDateTime startTime = Lang.toLocalDateTime(config.getBeginDate());
            LocalDateTime now = Lang.toLocalDateTime(date);
            while (startTime.isBefore(now)) {
                for (int i = 1; i <= config.getTimePeriodValue(); i++) {
                    startTime = startTime.plus(config.getTimePeriodUnit().getDuration());
                }
            }
            initialDuration = now.until(startTime, ChronoUnit.MILLIS);
            final Date diff = new Date(initialDuration);
            //initialDuration = startTime.getLong(ChronoField.MICRO_OF_DAY) - now.getLong(ChronoField.MICRO_OF_DAY);
            LOG.log(Level.INFO, "first start of job config ''{0}'' (id {1}) will be on {2} (current time: {3}, time left: {4})", new Object[]{config.getName(), config.getId(), Lang.toDateTime(startTime), Lang.toTime(date), Lang.toTime(diff)});
        }
        Timer timer = timerService.createIntervalTimer(initialDuration, intervalDuration, timerConfig);
        //schedule.hour("*").minute("*").second("13,34,57");
        //timerService.createCalendarTimer(schedule, timerConfig);
//            Timer timer = timerService.createCalendarTimer(new ScheduleExpression(), config);
        //timers.add(timer);
        return true;
    }

//    private Collection<Timer> getAllTimers() {
//        return timerService.getAllTimers();
//        //return new ArrayList<>(timers.);
//    }
    private Collection<Timer> getTimers() {
        return timerService.getTimers();
        //return new ArrayList<>(timers.);
    }

    private void cancelTimers() {
        for (Timer timer : getTimers()) {
            timer.cancel();
        }
    }

    public boolean cancelTimer(CpxJobConfig config) {
        if (config == null) {
            return true;
        }
        return cancelTimer(config.getId());
    }

    public boolean cancelTimer(final long pJobId) {
        for (Timer timer : getTimers()) {
            CpxJobConfig config = (CpxJobConfig) timer.getInfo();
            if (config != null && config.getId() == pJobId) {
                timer.cancel();
                return true;
            }
        }
        return false;
    }

    @TransactionTimeout(value = 360, unit = TimeUnit.MINUTES)
    @Timeout
    public void execute(final Timer pTimer) {
        CpxJobConfig config = (CpxJobConfig) pTimer.getInfo();
        if (config == null) {
            LOG.log(Level.SEVERE, "timer has no job config!");
            return;
        }
        if (!config.isActive()) {
            LOG.log(Level.INFO, "job is inactive: " + config.getClassName() + "/" + config.getName());
            return;
        }
        if (config.getTimePeriodUnit() == null) {
            LOG.log(Level.SEVERE, "job has no time period unit or given time period unit is invalid. Cannot initialize job: {0}/{1}", new Object[]{config.getClassName(), config.getName()});
            return;
        }
        final Date date = new Date();
        if (config.getBeginDate() != null && config.getBeginDate().after(date)) {
            LOG.log(Level.INFO, "job has to be started in future ({0}): {1}/{2}", new Object[]{config.getBeginDate(), config.getClassName(), config.getName()});
            return;
        }
        if (config.getEndDate() != null && config.getEndDate().before(date)) {
            LOG.log(Level.INFO, "job was finished in the past ({0}): {1}/{2}", new Object[]{config.getEndDate(), config.getClassName(), config.getName()});
            pTimer.cancel();
            return;
        }
        if (!config.isActive(date)) {
            LOG.log(Level.INFO, "job is inactive for some other reason: {0}/{1}", new Object[]{config.getClassName(), config.getName()});
            pTimer.cancel();
            return;
        }
//        if (pTimer.isPersistent()) {
//            pTimer.cancel();
//            return;
//        }
        final License license = licenseBean.getLicense();
        try (final Connection commonDbConnection = cpxServerConfig.getJdbcConnection(CpxServerConfigLocal.COMMONDB); final Connection caseDbConnection = cpxServerConfig.getJdbcConnection(config.getTargetDatabase())) {
            final String identifier = cpxServerConfig.getDbIdentifier(config.getTargetDatabase());
            if (config.isImport()) {
                final CpxJobImportConfig importConfig = (CpxJobImportConfig) config;
                try {
                    startJob(importConfig, commonDbConnection, caseDbConnection, identifier, license);
                } catch (Throwable ex) {
                    LOG.log(Level.SEVERE, "failed to start import job", ex);
                }
            } else if (config.isBatchgrouping()) {
                final BatchgroupingJob batchgroupingJob = (BatchgroupingJob) config;
                startBatchgrouping(batchgroupingJob);
//                    } finally {
//                        dBLockService.unlockDatabase();
//                    }
//                LOG.log(Level.INFO, "Batchgrouping is not implemented yet!");
            } else if (config.isBill()) {
                final BillJob billJob = (BillJob) config;
                startBill(billJob, cpxServerConfig.getDatabaseInfo(config.getTargetDatabase()));
//                    } finally {
//                        dBLockService.unlockDatabase();
//                    }
//                LOG.log(Level.INFO, "Batchgrouping is not implemented yet!");
            } else if(config.isGdvImportDocumentJob()){
                final GdvImportDocumentJob gdvImportDocumentJob = (GdvImportDocumentJob)config;
                startGdvImportDocumentJob(gdvImportDocumentJob, caseDbConnection);
            }
            else {
                LOG.log(Level.WARNING, "unknown job: {0}", config.getClassName());
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "cannot execute job because database connection failed: " + config.getClassName() + "/" + config.getName(), ex);
        }
    }

    public boolean startJob(final CpxJobImportConfig pImportConfig, final Connection pCommonDbConnection, final Connection pCaseDbConnection, final String pIdentifier, final License pLicense) throws Throwable {
        //final AbstractImportModule module;
        //ImportProcess<? extends AbstractImportModule> p = null;

        boolean importExecuted = startImport(pImportConfig, pCommonDbConnection, pCaseDbConnection, pIdentifier, pLicense);

        if (importExecuted) {
            BatchgroupingJob batchgroupingJob = pImportConfig.getBatchgroupingJob();
            if (batchgroupingJob != null) {
                LOG.log(Level.INFO, "Batchgrouping job with model {0} starts after import...", batchgroupingJob.getGrouperModel().name());
                startBatchgrouping(batchgroupingJob);
            }
        }

        return false;
    }

    private boolean startImport(final CpxJobImportConfig pImportConfig, final Connection pCommonDbConnection, final Connection pCaseDbConnection, final String pIdentifier, final License pLicense) throws Throwable {
        if (pImportConfig == null) {
            throw new IllegalArgumentException("import config cannot be null!");
        }
        if (pImportConfig.isFileImport()) {
            //final CpxFileBasedImportJob fileImportConfig = (CpxFileBasedImportJob) pImportConfig;
            if (pImportConfig.isP21Import()) {
                LOG.log(Level.INFO, "start P21 import job...");
                P21 module = new P21((P21Job) pImportConfig);
                ImportProcessFile<P21> process = new ImportProcessFile<>();
                ImportConfig<P21> importConfig = new ImportConfig<>(pIdentifier, module, pLicense);
                process.start(importConfig, pCommonDbConnection, pCaseDbConnection);
                return true;
            } else if (pImportConfig.isFdseImport()) {
                LOG.log(Level.INFO, "start FDSE import job...");
                Fdse module = new Fdse((FdseJob) pImportConfig);
                ImportProcessFile<Fdse> process = new ImportProcessFile<>();
                ImportConfig<Fdse> importConfig = new ImportConfig<>(pIdentifier, module, pLicense);
                process.start(importConfig, pCommonDbConnection, pCaseDbConnection);
                return true;
            }
        } else if (pImportConfig.isDbImport()) {
            final CpxDatabaseBasedImportJob databaseImportConfig = (CpxDatabaseBasedImportJob) pImportConfig;
            if (pImportConfig.isKissmedImport()) {
                LOG.log(Level.INFO, "start KISSMED import job...");
                Kissmed module = new Kissmed((KissmedJob) databaseImportConfig);
                ImportProcessDb<Kissmed> process = new ImportProcessDb<>();
                ImportConfig<Kissmed> importConfig = new ImportConfig<>(pIdentifier, module, pLicense);
                process.start(importConfig, pCommonDbConnection, pCaseDbConnection);
                return true;
            } else if (pImportConfig.isMedicoImport()) {
                LOG.log(Level.INFO, "start Medico import job...");
                Medico module = new Medico((MedicoJob) databaseImportConfig);
                ImportProcessDb<Medico> process = new ImportProcessDb<>();
                ImportConfig<Medico> importConfig = new ImportConfig<>(pIdentifier, module, pLicense);
                process.start(importConfig, pCommonDbConnection, pCaseDbConnection);
                return true;
            } else if (pImportConfig.isOrbisImport()) {
                LOG.log(Level.INFO, "start Orbis import job...");
                Orbis module = new Orbis((OrbisJob) databaseImportConfig);
                ImportProcessDb<Orbis> process = new ImportProcessDb<>();
                ImportConfig<Orbis> importConfig = new ImportConfig<>(pIdentifier, module, pLicense);
                process.start(importConfig, pCommonDbConnection, pCaseDbConnection);
                return true;
            } else if (pImportConfig.isNexusImport()) {
                LOG.log(Level.INFO, "start Nexus import job...");
                Nexus module = new Nexus((NexusJob) databaseImportConfig);
                ImportProcessDb<Nexus> process = new ImportProcessDb<>();
                ImportConfig<Nexus> importConfig = new ImportConfig<>(pIdentifier, module, pLicense);
                process.start(importConfig, pCommonDbConnection, pCaseDbConnection);
                return true;
            }
        } else if (pImportConfig.isSapImport()) {
            LOG.log(Level.INFO, "start SAP import job...");
            final CpxExternalSystemBasedJobImportConfig externalImportConfig = (CpxExternalSystemBasedJobImportConfig) pImportConfig;
            final FallContainer fallContainer = new FallContainer();
            Sap module = new Sap((SapJob) externalImportConfig, fallContainer);
            ImportProcessSap process = new ImportProcessSap();
            ImportConfig<Sap> importConfig = new ImportConfig<>(pIdentifier, module, pLicense);
            final Date changeDate = cpxServerConfig.getSapChangeDate(externalImportConfig.getTargetDatabase());

            FallContainer fallContainer1 = process.doSapImport(importConfig, changeDate, false);
            if(((SapJob) externalImportConfig).isUseJsonDump()){
                Sap module1 = new Sap((SapJob) externalImportConfig, fallContainer1);
                final ImportConfig<Sap> importConfig1 = new ImportConfig<>(pIdentifier, module1, pLicense);

                ImportProcessSap process1 = new ImportProcessSap();
                process1.start(importConfig1, pCommonDbConnection, pCaseDbConnection);

            }else{
                process.start(importConfig, pCommonDbConnection, pCaseDbConnection);
            }
            return true;
        }
        LOG.log(Level.SEVERE, "cannot detect suitable import for this job config: " + pImportConfig.getClassName() + "/" + pImportConfig.getName(), pImportConfig.getConstraints());
        return false;
    }

    private void startBatchgrouping(final BatchgroupingJob pBatchgroupingJob) {
        LOG.log(Level.INFO, "start Batchgrouping job...");
        try{
        loginService.loginForExternalCall();
        }catch(Exception ex){
            LOG.log(Level.SEVERE, "Error on login by start of batchgroup job, we try to go on", ex);
        }
        //dBLockService.unlockDatabase();
        final Long executionId = importBean.prepareBatchgroupingWithoutLocking(pBatchgroupingJob.getTargetDatabase());
        importBean.startBatchGrouping(executionId, pBatchgroupingJob.getBatchParameter());
    }

    private void startBill(final BillJob pBillJob, final DatabaseInfo dbInfo) {
        LOG.log(Level.INFO, "start Bill job...");
        loginService.loginForExternalCall();
        //dBLockService.unlockDatabase();
        final Long executionId = importBean.prepareBatchgroupingWithoutLocking(pBillJob.getTargetDatabase());
        importBean.startBillImport(executionId, pBillJob.getInputDirectory(), dbInfo);
    }

    private void startGdvImportDocumentJob(GdvImportDocumentJob pGdvImportDocumentJob, final Connection pCaseDbConnection) {
        LOG.log(Level.INFO, "start GdvImportDocument job...");
       loginService.loginForExternalCall(); //do we need this?
        //dBLockService.unlockDatabase();
        final Long executionId = importBean.prepareBatchgroupingWithoutLocking(pGdvImportDocumentJob.getTargetDatabase());
        importBean.startGdvDocumentImport(executionId, pGdvImportDocumentJob);


    }

}
