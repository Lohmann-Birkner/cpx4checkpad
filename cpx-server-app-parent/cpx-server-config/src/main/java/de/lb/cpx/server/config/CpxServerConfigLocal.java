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
package de.lb.cpx.server.config;

import de.lb.cpx.config.CpxConfigLocal;
import de.lb.cpx.config.ExtendedXMLConfiguration;
import de.lb.cpx.cpx.license.crypter.License;
import de.lb.cpx.server.commons.enums.RuleImportCheckFlags;
import de.lb.cpx.server.commons.enums.RuleOverrideFlags;
import de.lb.cpx.service.information.ConnectionString;
import de.lb.cpx.service.information.DatabaseInfo;
import de.lb.cpx.shared.dto.job.config.CpxDatabaseBasedImportJob;
import de.lb.cpx.shared.dto.job.config.CpxFileBasedImportJob;
import de.lb.cpx.shared.dto.job.config.CpxJobConfig;
import de.lb.cpx.shared.dto.job.config.database.KissmedJob;
import de.lb.cpx.shared.dto.job.config.database.MedicoJob;
import de.lb.cpx.shared.dto.job.config.database.NexusJob;
import de.lb.cpx.shared.dto.job.config.database.OrbisJob;
import de.lb.cpx.shared.dto.job.config.file.FdseJob;
import de.lb.cpx.shared.dto.job.config.file.P21Job;
import de.lb.cpx.shared.dto.job.config.file.SampleJob;
import de.lb.cpx.shared.dto.job.config.other.SapJob;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import javax.ejb.Local;

/**
 *
 * @author Dirk Niemeier
 */
@Local
//@SecurityDomain("cpx")

public interface CpxServerConfigLocal extends CpxConfigLocal {

    final String COMMONDB = "dbsys_common:cpx_common";

    Connection getJdbcConnection(final String pConnectionString) throws SQLException;

    File getXmlConfigFile();

    ExtendedXMLConfiguration getXmlConfig();

//    
//    DatabaseConfiguration getDbConfig();

    /* Server Properties */
    String getEdition();

    void setEdition(final String pEdition);

    String getCustomer();

    void setCustomer(final String pCustomer);

    String getFontFamily();

    void setFontFamily(final String pFontFamily);

    String getFontSize();

    void setFontSize(final String pFontSize);

    String getLanguage();

    void setLanguage(final String pLanguage);

    boolean getWorkingListLocal();

    void setWorkingListLocal(final boolean pIsLocal);

    boolean getShowHistoryEventDetails();

    RuleOverrideFlags getRuleOverrideFlag();

    void setRuleOverrideFlag(final RuleOverrideFlags pRuleOverrideFlag);

    boolean getDocumentImportOfficeEnabled();

    void setDocumentImportOfficeEnabled(final boolean pDocumentImportOfficeEnabled);

    boolean getDocumentImportShowPdfEnabled();

    void setDocumentImportShowPdfEnabled(final boolean pDocumentImportShowPdfEnabled);

    RuleImportCheckFlags getRuleImportCheckFlag();

    void setRuleImportCheckFlag(final RuleImportCheckFlags pRuleImportCheckFlag);

    void setShowHistoryEventDetails(final boolean pShowHistoryEventDetails);

    boolean getShowHistoryDeleted();

    void setShowHistoryDeleted(final boolean pShowHistoryDeleted);
    
    boolean getUseGdv();
    void setUseGdv(final boolean pUseGdv);
    boolean getSendGdvResponce();
    void setSendGdvResponce(final boolean pSendGdvResponce);
    String getGdvResponcePath();
    void setGdvResponcePath(final String pPath);

    /*
    
    boolean getDocumentsStoreInDatabase();

    
    void setDocumentsStoreInDatabase(final boolean config);

    
    boolean getDocumentsStoreInFilesystem();

    
    void setDocumentsStoreInFilesystem(final boolean config);
    
    
    String getServerRootFolder();

    
    void setServerRootFolder(final String path);
     */
    int getDocumentsSizeMax();

    void setDocumentsSizeMax(final Integer pDocSizeMaxKb);

    String getDocumentsStorageType();

    void setDocumentsStorageType(final String docStorageType);

    String getDocumentsFileSystemPath();

    void setDocumentsFileSystemPath(final String fileSystemPath);

    boolean getDocumentsNotArchivate();

    void setDocumentsNotArchivate(final boolean notArchivateDocs);
    
    boolean getSapBillDisplayTab();

    void setSapBillDisplayTab(final boolean config);

    boolean getLaboratoryDataDisplayTab();

    void setLaboratoryDataDisplayTab(final boolean config);
    boolean getDoMergeSave();
    void setDoMergeSave(final boolean config);
    
    void setCreateCaseAnonymize(final boolean pFlag);
    boolean getCreateCaseAnonymize();
    /*
    
    String getPdfReportType();

    
    void setPdfReportType(final String type);
     */
    boolean isPdfReportAllowedToUse();

    void setPdfReportAllowedToUse(final boolean allowedToUse);

    String getPdfReportXslFilePath();

    void setPdfReportXslFilePath(final String xslFilePath);

    String getPdfReportImageFilePath();

    void setPdfReportImageFilePath(final String imageFilePath);

    Integer getSearchListFetchSize();

    void setSearchListFetchSize(final Integer pSearchListFetchSize);

    String getDatabaseUrl(final String pPersistenceUnit);

    String getDatabaseUser(final String pPersistenceUnit);

    String getDatabasePassword(final String pPersistenceUnit);

    /**
     * Calculated value with the help of Database URL!
     *
     * @param pPersistenceUnit Persistence unit
     * @return Drivername
     */
    String getDatabaseDriver(final String pPersistenceUnit);

    /**
     * Calculated value with the help of Database URL!
     *
     * @param pPersistenceUnit Persistence unit
     * @return Dialect
     */
    String getDatabaseDialect(final String pPersistenceUnit);

    DatabaseInfo getDatabaseInfo(final String pConnectionString);

    Map<String, String> getDatabaseProperties(final ConnectionString pConnectionString);

    Map<String, String> getDatabaseProperties(final String pConnectionString);

    Map<String, String> getDatabaseProperties(final String pPersistenceUnit, final String pDatabase);

    String getDbIdentifier(final String pConnectionString);

    boolean getRulesDatabaseConfig();

    void setRulesDatabaseConfig(final boolean config);
    
    boolean getShowRelevantRules();

    void setShowRelevantRules(final boolean config);

    boolean getWorkflowListAllReminder();

    void setWorkflowListAllReminder(final boolean pAllReminder);

    boolean getFilterListDetailsOverview();

    void setFilterListDetailsOverview(final boolean isShowFilterListDetailsOverview);

    /**
     * Should the patient's health status be rendered?
     *
     * @return is this feature enabled/disabled?
     */
    boolean getCommonHealthStatusVisualization();

    boolean setDatabasePassword(final String pPersistenceUnit, final String pPlaintextPassword);

    <T extends CpxJobConfig> Map<Integer, T> getActiveJobConfigs(final Class<T> pJobConfigClass);

    <T extends CpxJobConfig> Map<Integer, T> getJobConfigs(final Class<T> pJobConfigClass);

    <T extends CpxJobConfig> Map<Integer, T> getJobConfigs(final Class<T> pJobConfigClass, final String pName);

    Map<Integer, CpxJobConfig> getJobConfigs(final long pId);

    <T extends CpxJobConfig> Map<Integer, T> getJobConfigs(final Class<T> pJobConfigClass, final long pId);

    NexusJob getNexusConfig(final String pName);

    Map<Integer, NexusJob> getNexusConfigs();

    KissmedJob getKissmedConfig(final String pName);

    Map<Integer, KissmedJob> getKissmedConfigs();

    MedicoJob getMedicoConfig(final String pName);

    Map<Integer, MedicoJob> getMedicoConfigs();

    OrbisJob getOrbisConfig(final String pName);

    Map<Integer, OrbisJob> getOrbisConfigs();

    SampleJob getSampleConfig(final String pName);

    Map<Integer, SampleJob> getSampleConfigs();

    P21Job getP21Config(final String pName);

    Map<Integer, P21Job> getP21Configs();

    FdseJob getFdseConfig(final String pName);

    Map<Integer, FdseJob> getFdseConfigs();

    CpxDatabaseBasedImportJob getDbConfig(final String pName);

    Map<Integer, CpxDatabaseBasedImportJob> getDbConfigs();

    CpxFileBasedImportJob getFileConfig(final String pName);

    Map<Integer, CpxFileBasedImportJob> getFileConfigs();

    SapJob getSapConfig(final String pName);

    Map<Integer, SapJob> getSapConfigs();

    <T extends CpxJobConfig> T getJobConfig(final Class<T> pJobConfigClass, final long pId);

    <T extends CpxJobConfig> T getJobConfig(final Class<T> pJobConfigClass, final String pName);

    Map<Integer, CpxJobConfig> getActiveJobConfigs();

    Map<Integer, CpxJobConfig> getJobConfigs();

    Map<Integer, CpxJobConfig> getJobConfig(final String pName);

    Map<Integer, CpxJobConfig> getJobConfig(final long pId);

    CpxJobConfig getJobConfig(final int pIndex);

    <T extends CpxJobConfig> boolean removeJobConfig(final T pJobConfig);

    <T extends CpxJobConfig> boolean removeJobConfig(final Class<T> pJobConfigClass, final String pName);

    <T extends CpxJobConfig> boolean saveJobConfig(final T pJobConfig);

    /**
     * Should the patient's health status be rendered?
     *
     * @param pHealthStatusVisulization enable/disable this feature
     */
    void setCommonHealthStatusVisualization(final boolean pHealthStatusVisulization);

    boolean isRuleEditorClient();

    void setRuleEditorClient(final boolean pRuleEditorClient);

    public License readLicense();

    public Date getSapChangeDate(final String pCaseDb) throws SQLException;

    public boolean useHistoryCases4Group();
    
    public void setUseHistoryCases4Group(final boolean pUseIt);
    
    public String getPdfReportPeppXslFilePath();
    
    public void setPdfReportPeppXslFilePath(String xslFilePath);

    public boolean getSapSendMDStateToSAP();
    
    public void setSapSendMDStateToSAP(boolean pSendMDStateToSAP);
}
