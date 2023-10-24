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

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.app.crypter.PasswordDecrypter;
import de.lb.cpx.config.AbstractCpxConfig;
import de.lb.cpx.config.ExtendedXMLConfiguration;
import de.lb.cpx.cpx.license.crypter.License;
import de.lb.cpx.cpx.license.crypter.LicenseWriter;
import de.lb.cpx.model.enums.AdmissionCauseEn;
import de.lb.cpx.model.enums.AdmissionReasonEn;
import de.lb.cpx.model.enums.IcdcTypeEn;
import de.lb.cpx.server.commons.enums.DbDriverEn;
import de.lb.cpx.server.commons.enums.RuleImportCheckFlags;
import de.lb.cpx.server.commons.enums.RuleOverrideFlags;
import de.lb.cpx.service.information.ConnectionString;
import de.lb.cpx.service.information.DatabaseInfo;
import de.lb.cpx.shared.dto.job.config.CpxDatabaseBasedImportJob;
import de.lb.cpx.shared.dto.job.config.CpxExternalSystemBasedJobImportConfig;
import de.lb.cpx.shared.dto.job.config.CpxFileBasedImportJob;
import de.lb.cpx.shared.dto.job.config.CpxJobConfig;
import de.lb.cpx.shared.dto.job.config.CpxJobConstraints;
import de.lb.cpx.shared.dto.job.config.CpxJobImportConfig;
import de.lb.cpx.shared.dto.job.config.ImportMode;
import de.lb.cpx.shared.dto.job.config.database.KissmedJob;
import de.lb.cpx.shared.dto.job.config.database.MedicoJob;
import de.lb.cpx.shared.dto.job.config.database.NexusJob;
import de.lb.cpx.shared.dto.job.config.database.OrbisJob;
import de.lb.cpx.shared.dto.job.config.file.FdseJob;
import de.lb.cpx.shared.dto.job.config.file.P21Job;
import de.lb.cpx.shared.dto.job.config.file.SampleJob;
import de.lb.cpx.shared.dto.job.config.other.BatchgroupingJob;
import de.lb.cpx.shared.dto.job.config.other.BillJob;
import de.lb.cpx.shared.dto.job.config.other.GdvImportDocumentJob;
import de.lb.cpx.shared.dto.job.config.other.SapJob;
import de.lb.cpx.shared.lang.Lang;
import static de.lb.cpx.str.utils.StrUtils.*;
import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.ConfigurationBuilderEvent;
import org.apache.commons.configuration2.builder.ReloadingFileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.event.EventListener;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.tree.xpath.XPathExpressionEngine;

/**
 *
 * @author Dirk Niemeier
 */
@Singleton
@TransactionManagement(TransactionManagementType.BEAN) //<- is neccessary for DbConfig, otherwise there will be a commit error
@Lock(LockType.READ)
//@SecurityDomain("cpx")
public class CpxServerConfig extends AbstractCpxConfig implements CpxServerConfigLocal {

    private static final Logger LOG = Logger.getLogger(CpxServerConfig.class.getName());
    private ReloadingFileBasedConfigurationBuilder<ExtendedXMLConfiguration> xmlBuilder = null;
//    private DatabaseConfiguration dbConfig;

    public Connection getJdbcConnectionToCommonDb() throws SQLException {
        return getJdbcConnection(CpxServerConfigLocal.COMMONDB);
    }

    @Override
    public synchronized Connection getJdbcConnection(final String pConnectionString) throws SQLException {
        String connectionString = (pConnectionString == null) ? "" : pConnectionString.trim();
        if (connectionString.isEmpty()) {
            LOG.log(Level.SEVERE, "Cannot fetch jdbc connection, user has no database selected!");
            return null;
        }

        //Format of Database Parameter: PersistenceUnit:Database - so we have to split it now!
        ConnectionString connString = new ConnectionString(connectionString);

        final String persistenceUnit = connString.getPersistenceUnit(); //dbsys1, dbsys2...
        final String database = connString.getDatabase(); //CPX_DEV, CPX_DEV1...

        //CpxServerConfigLocal cpxServerConfig = ClientManager.lookup(CpxServerConfigLocal.class);
        // Falls nötig, Map-Eintrag für URL erstellen
        //factoryMap.computeIfAbsent(key, u -> {
        Map<String, String> props = this.getDatabaseProperties(persistenceUnit, database);
        if (props == null) {
            LOG.log(Level.SEVERE, "No connection properties found for connection string ''{0}''. Is this persistence unit really defined in your cpx_server_config.xml?", pConnectionString);
            return null;
        }

        String url = props.get("javax.persistence.jdbc.url");
        String user = props.get("javax.persistence.jdbc.user");
        String password = props.get("javax.persistence.jdbc.password");

        LOG.log(Level.INFO, "Try to establish jdbc connection with this url: {0}", url);
        if (url == null || url.trim().isEmpty()) {
            LOG.log(Level.SEVERE, "No connection url found for connection string ''{0}''. Is this persistence unit really defined in your cpx_server_config.xml?", pConnectionString);
        }

        Connection connection = DriverManager.getConnection(url, user, password);

        return connection;
    }

    //private final static Logger LOG = Logger.getLogger(CpxServerConfig.class.getName());
    //private ExtendedXMLConfiguration xmlConfig = null;
    public static File getPlaintextPasswordFile(final String pPersistenceUnit) {
        String persistenceUnit = (pPersistenceUnit == null ? "" : pPersistenceUnit.trim());
        CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();
        File plaintextPasswordFile = new File(cpxProps.getCpxHome() + "." + persistenceUnit);
        return plaintextPasswordFile;
    }

    public static boolean isPasswordEncrypted(final String pPasswordCandidate) {
        return PasswordDecrypter.getInstance().isEncrypted(pPasswordCandidate);
//        final String candidate = (pPasswordCandidate == null) ? "" : pPasswordCandidate.trim();
//        String decodedCandidate = decodePassword(candidate);
//        return !(candidate != null && candidate.equalsIgnoreCase(decodedCandidate));
    }

    public static String encryptPassword(final String pPlainPassword) {
//        String password = (pPlainPassword == null ? "" : pPlainPassword.trim());
        return PasswordDecrypter.getInstance().encrypt2(pPlainPassword);
    }

    public static String decryptPassword(final String pEncodedPassword) {
        return PasswordDecrypter.getInstance().decrypt2(pEncodedPassword);
//        String password = (pEncodedPassword == null ? "" : pEncodedPassword.trim());
//        try {
//            return AppDecrypter.getInstance().decrypt(password);
//        } catch (SecurityException ex) {
//            //If password is already plaintext then this exception branch will be executed
//            LOG.log(Level.FINER, "Was not able to decode password", ex);
//            return password;
//        }
    }

//    @Inject
//    @CommonEntityManagerDs
//    private DataSource dataSource;
    //@Resource(lookup = "java:jboss/datasources/CpXCommonDB")
    //private DataSource dataSource;
    @Override
    public File getXmlConfigFile() {
        CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();
        String configFileName = cpxProps.getCpxServerConfigFile();
        File configFile = new File(configFileName);
        if (!configFile.exists()) {
            LOG.log(Level.SEVERE, "XmlConfigFile does not exist: {0}", configFile.getAbsolutePath());
        } else if (!configFile.isFile()) {
            LOG.log(Level.SEVERE, "XmlConfigFile is not a file: {0}", configFile.getAbsolutePath());
        } else if (!configFile.canRead()) {
            LOG.log(Level.SEVERE, "XmlConfigFile is not accessible (no permissions to read this file): {0}", configFile.getAbsolutePath());
        }
        return configFile;
    }

    @Override
    public synchronized ExtendedXMLConfiguration getXmlConfig() {
        if (xmlBuilder == null) {
            //CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();
            //File configFile = new File(cpxProps.getCpxServerConfigFile());      
            File configFile = getXmlConfigFile();
//      try {
//        createXmlConfigFile(configFile.getAbsolutePath());
//      } catch (FileSystemException ex) {
//        Logger.getLogger(CpxServerConfig.class.getName()).log(Level.SEVERE, null, ex);
//        return null;
//      }
            Parameters params = new Parameters();
            final ReloadingFileBasedConfigurationBuilder<ExtendedXMLConfiguration> builder
                    = new ReloadingFileBasedConfigurationBuilder<>(ExtendedXMLConfiguration.class)
                            .configure(params.xml()
                                    .setThrowExceptionOnMissing(false)
                                    .setValidating(false)
                                    .setEncoding("UTF-8")
                                    .setFile(configFile)
                                    .setExpressionEngine(new XPathExpressionEngine()));
            builder.setAutoSave(true);

            /*
      PeriodicReloadingTrigger trigger = new PeriodicReloadingTrigger(builder.getReloadingController(),
          null, 1, TimeUnit.MINUTES);
      trigger.start();
             */
            // Register an event listener for triggering reloading checks
            builder.addEventListener(
                    ConfigurationBuilderEvent.CONFIGURATION_REQUEST,
                    new EventListener<ConfigurationBuilderEvent>() {
                @Override
                public void onEvent(ConfigurationBuilderEvent event) {
                    builder.getReloadingController().checkForReloading(null);
                }
            });

            /*
      // Register an event listener for triggering reloading checks
      builder.addEventListener(ConfigurationEvent.SET_PROPERTY,
        new EventListener() {
          @Override
          public void onEvent(Event event) {
            System.out.println("CHANGED!");
          }
        });
             */
            xmlBuilder = builder;
        }
        try {
            return xmlBuilder.getConfiguration();
        } catch (ConfigurationException ex) {
            String msg = "Server configuration file seems to be corrupted: " + (xmlBuilder.getFileHandler() == null || xmlBuilder.getFileHandler().getFile() == null ? "null" : xmlBuilder.getFileHandler().getFile().getAbsolutePath());
            //LOG.log(Level.SEVERE, msg, ex);
            throw new IllegalStateException(msg, ex);
        }
    }

//    /**
//     * example how to use: serverConfig.getDbConfig().getBoolean("iskv");
//     * @return database configuration
//     */
//    
//    public synchronized DatabaseConfiguration getDbConfig() {
//        if (dbConfig == null) {
//            DatabaseConfiguration conf = new DatabaseConfiguration();
//            conf.setDataSource(new DataSource() {
//                @Override
//                public Connection getConnection() throws SQLException {
//                    return getJdbcConnection(CpxServerConfigLocal.COMMONDB);
//                }
//
//                @Override
//                public Connection getConnection(String username, String password) throws SQLException {
//                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                }
//
//                @Override
//                public PrintWriter getLogWriter() throws SQLException {
//                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                }
//
//                @Override
//                public void setLogWriter(PrintWriter out) throws SQLException {
//                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                }
//
//                @Override
//                public void setLoginTimeout(int seconds) throws SQLException {
//                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                }
//
//                @Override
//                public int getLoginTimeout() throws SQLException {
//                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                }
//
//                @Override
//                public Logger getParentLogger() throws SQLFeatureNotSupportedException {
//                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                }
//
//                @Override
//                public <T> T unwrap(Class<T> iface) throws SQLException {
//                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                }
//
//                @Override
//                public boolean isWrapperFor(Class<?> iface) throws SQLException {
//                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                }
//                
//            });
//            conf.setTable("C_CONFIGURATION");
//            conf.setKeyColumn("KEY");
//            conf.setValueColumn("VALUE");
//            conf.setAutoCommit(false); //cannot be true!
//            dbConfig = conf;
//        }
//        return dbConfig;
//    }
    @Override
    public synchronized List<Configuration> getConfigs() {
        List<Configuration> configs = new ArrayList<>();
        configs.add(getXmlConfig());
//        configs.add(getDbConfig());
        return configs;
    }

    /* Server Properties */
    @Override
    public String getEdition() {
        return getString(getXmlConfig(), "common/edition");
    }

    @Override
    public void setEdition(final String pEdition) {
        setString(getXmlConfig(), "common/edition", pEdition);
    }

    @Override
    public String getCustomer() {
        return getString(getXmlConfig(), "common/customer");
    }

    @Override
    public void setCustomer(final String pCustomer) {
        setString(getXmlConfig(), "common/customer", pCustomer);
    }

    @Override
    public String getFontFamily() {
        return getString(getXmlConfig(), "common/font/family", "Segoe UI Light");
    }

    @Override
    public void setFontFamily(final String pFontFamily) {
        setString(getXmlConfig(), "common/font/family", pFontFamily);
    }

    @Override
    public String getFontSize() {
        return getString(getXmlConfig(), "common/font/size", "12");
    }

    @Override
    public void setFontSize(final String pFontSize) {
        setString(getXmlConfig(), "common/font/size", pFontSize);
    }

    @Override
    public String getLanguage() {
        return getString(getXmlConfig(), "common/language", "de");
    }

    @Override
    public void setLanguage(final String pLanguage) {
        setString(getXmlConfig(), "common/language", pLanguage);
    }

    @Override
    public boolean getWorkingListLocal() {
        return getBoolean(getXmlConfig(), "common/working_list_local", true);
    }

    @Override
    public void setWorkingListLocal(final boolean pIsLocal) {
        setBoolean(getXmlConfig(), "common/working_list_local", pIsLocal);
    }

    @Override
    public boolean getShowHistoryEventDetails() {
        return getBoolean(getXmlConfig(), "common/show_history_event_details", false);
    }

    @Override
    public void setShowHistoryEventDetails(final boolean pShowHistoryEventDetails) {
        setBoolean(getXmlConfig(), "common/show_history_event_details", pShowHistoryEventDetails);
    }

    @Override
    public boolean getDocumentImportOfficeEnabled() {
        return getBoolean(getXmlConfig(), "document_import/office_enabled", true);
    }

    @Override
    public void setDocumentImportOfficeEnabled(final boolean pDocumentImportOfficeEnabled) {
        setBoolean(getXmlConfig(), "document_import/office_enabled", pDocumentImportOfficeEnabled);
    }

    @Override
    public boolean getDocumentImportShowPdfEnabled() {
        return getBoolean(getXmlConfig(), "document_import/show_pdf", true);
    }

    @Override
    public void setDocumentImportShowPdfEnabled(final boolean pDocumentImportShowPdfEnabled) {
        setBoolean(getXmlConfig(), "document_import/show_pdf", pDocumentImportShowPdfEnabled);
    }

    @Override
    public RuleImportCheckFlags getRuleImportCheckFlag() {
        return getEnum(getXmlConfig(), "rule_editor/rule_import_check_flag", RuleImportCheckFlags.values(), RuleImportCheckFlags.CHECK_4_COLLISIONS);
    }

    @Override
    public void setRuleImportCheckFlag(final RuleImportCheckFlags pRuleImportCheckFlag) {
        setEnum(getXmlConfig(), "rule_editor/rule_import_check_flag", pRuleImportCheckFlag);
    }

    @Override
    public RuleOverrideFlags getRuleOverrideFlag() {
        return getEnum(getXmlConfig(), "rule_editor/rule_override_flag", RuleOverrideFlags.values(), RuleOverrideFlags.SAVE_BOTH);
    }

    @Override
    public void setRuleOverrideFlag(final RuleOverrideFlags pRuleOverrideFlag) {
        setEnum(getXmlConfig(), "rule_editor/rule_override_flag", pRuleOverrideFlag);
    }

    @Override
    public boolean getShowHistoryDeleted() {
        return getBoolean(getXmlConfig(), "common/show_history_deleted", false);
    }

    @Override
    public void setShowHistoryDeleted(final boolean pShowHistoryDeleted) {
        setBoolean(getXmlConfig(), "common/show_history_deleted", pShowHistoryDeleted);
    }

    //RSH: 19042018 CPX-857
    @Override
    public boolean getWorkflowListAllReminder() {
        return getBoolean(getXmlConfig(), "common/workflow_list_all_reminder", true);
    }

    @Override
    public void setWorkflowListAllReminder(final boolean pAllReminder) {
        setBoolean(getXmlConfig(), "common/workflow_list_all_reminder", pAllReminder);
    }

    @Override
    public boolean getFilterListDetailsOverview() {
        return getBoolean(getXmlConfig(), "common/show_filterlists_details_overview", true);
    }

    @Override
    public void setFilterListDetailsOverview(final boolean isShowFilterListDetailsOverview) {
        setBoolean(getXmlConfig(), "common/show_filterlists_details_overview", isShowFilterListDetailsOverview);
    }

    @Override
    public Integer getSearchListFetchSize() {
        return getInt(getXmlConfig(), "common/search_list_fetch_size", 1000);
    }

    @Override
    public void setSearchListFetchSize(Integer pSearchListFetchSize) {
        setInt(getXmlConfig(), "common/search_list_fetch_size", pSearchListFetchSize);
    }

    /* 
    @Override
    
    public boolean getDocumentsStoreInDatabase() {
        return getBoolean(getXmlConfig(), "documents/store_in_database", false);
    }

    @Override
    
    public void setDocumentsStoreInDatabase(final boolean pStoreInDatabase) {
        setBoolean(getXmlConfig(), "documents/store_in_database", pStoreInDatabase);
    }

    @Override
    
    public boolean getDocumentsStoreInFilesystem() {
        return getBoolean(getXmlConfig(), "documents/store_in_filesystem", true);
    }

    @Override
    
    public void setDocumentsStoreInFilesystem(final boolean pStoreInFilesystem) {
        setBoolean(getXmlConfig(), "documents/store_in_filesystem", pStoreInFilesystem);
    }
    
        
    @Override
    
    public String getServerRootFolder() {
        return getString(getXmlConfig(), "documents/store_doc_in_serverpath", "");
    }

    @Override
    
    public void setServerRootFolder(final String path) {
        setString(getXmlConfig(), "documents/store_doc_in_serverpath", path);
    }
     */
    @Override
    public int getDocumentsSizeMax() {
//        return getString(getXmlConfig(), "documents/storage[@type]", "");
        return getInt(getXmlConfig(), "documents/size_max", 30720); //default is 30 MB
    }

    @Override
    public void setDocumentsSizeMax(final Integer docSizeMaxKb) {
//        setString(getXmlConfig(), "documents/storage[@type]", docStorageType);
        setInt(getXmlConfig(), "documents/size_max", docSizeMaxKb);
    }

    @Override
    public String getDocumentsStorageType() {
//        return getString(getXmlConfig(), "documents/storage[@type]", "");
        return getString(getXmlConfig(), "documents/storage/@type", "");
    }

    @Override
    public void setDocumentsStorageType(final String docStorageType) {
//        setString(getXmlConfig(), "documents/storage[@type]", docStorageType);
        setString(getXmlConfig(), "documents/storage/@type", docStorageType);
    }

    @Override
    public String getDocumentsFileSystemPath() {
        return getString(getXmlConfig(), "documents/storage/path", "");
    }

    @Override
    public void setDocumentsFileSystemPath(final String fileSystemPath) {
        setString(getXmlConfig(), "documents/storage/path", fileSystemPath);
    }

    @Override
    public boolean getDocumentsNotArchivate() {
        return getBoolean(getXmlConfig(), "documents/storage/not_archivate_docs", false);
    }

    @Override
    public void setDocumentsNotArchivate(final boolean notArchivateDocs) {
        setBoolean(getXmlConfig(), "documents/storage/not_archivate_docs", notArchivateDocs);
    }

    @Override
    public boolean getSapBillDisplayTab() {
        return getBoolean(getXmlConfig(), "sap/bills/display_tab", false);
    }

    @Override
    public void setSapBillDisplayTab(boolean pDisplayTab) {
        setBoolean(getXmlConfig(), "sap/bills/display_tab", pDisplayTab);
    }

    @Override
    public boolean getLaboratoryDataDisplayTab() {
        return getBoolean(getXmlConfig(), "labor_data/display_tab", false);
    }

    @Override
    public void setLaboratoryDataDisplayTab(boolean pDisplayTab) {
        setBoolean(getXmlConfig(), "labor_data/display_tab", pDisplayTab);
    }


    @Override 
    public boolean getDoMergeSave() {
        return getBoolean(getXmlConfig(), "merge_cases/doSaveMerging", false);
    }

    @Override  
    public void setDoMergeSave(boolean pDisplayTab) {  
        setBoolean(getXmlConfig(), "merge_cases/doSaveMerging", pDisplayTab);
    }

    @Override
    public  void setCreateCaseAnonymize(final boolean pFlag){
        setBoolean(getXmlConfig(), "create_case/anonymize", pFlag);
        
    }
    @Override
    public boolean getCreateCaseAnonymize(){
        return getBoolean(getXmlConfig(), "create_case/anonymize", true);
       
    }
    /*  
    @Override
    public String getPdfReportType() {
        return getString(getXmlConfig(), "pdf_reports/type", "NONE");
    }

    @Override
    public void setPdfReportType(final String type) {
        setString(getXmlConfig(), "pdf_reports/type", type);
    }
     */
    @Override
    public boolean isPdfReportAllowedToUse() {
        return getBoolean(getXmlConfig(), "pdf_report/use_report", false);
    }

    @Override
    public void setPdfReportAllowedToUse(boolean allowedToUse) {
        setBoolean(getXmlConfig(), "pdf_report/use_report", allowedToUse);
    }

    @Override
    public String getPdfReportXslFilePath() {
        return getString(getXmlConfig(), "pdf_report/report_xsl_file", "");
    }

    @Override
    public void setPdfReportXslFilePath(String xslFilePath) {
        setString(getXmlConfig(), "pdf_report/report_xsl_file", xslFilePath);
    }

    @Override
    public String getPdfReportImageFilePath() {
        return getString(getXmlConfig(), "pdf_report/report_image_file", "");
    }

    @Override
    public void setPdfReportImageFilePath(String imageFilePath) {
        setString(getXmlConfig(), "pdf_report/report_image_file", imageFilePath);
    }

    @Override
    public String getDatabaseUrl(final String pPersistenceUnit) {
        String name = (pPersistenceUnit == null) ? "" : pPersistenceUnit.trim().toLowerCase();
        return getString(getXmlConfig(), "database/" + name + "/url", "");
    }

    @Override
    public String getDatabaseUser(final String pPersistenceUnit) {
        String name = (pPersistenceUnit == null) ? "" : pPersistenceUnit.trim().toLowerCase();
        return getString(getXmlConfig(), "database/" + name + "/user", "");
    }

    @Override
    public String getDatabasePassword(final String pPersistenceUnit) {
        String name = (pPersistenceUnit == null) ? "" : pPersistenceUnit.trim().toLowerCase();
        return getString(getXmlConfig(), "database/" + name + "/password", "");
    }

    @Override
    public boolean setDatabasePassword(final String pPersistenceUnit, final String pPlaintextPassword) {
        String name = (pPersistenceUnit == null) ? "" : pPersistenceUnit.trim().toLowerCase();
        String plaintextPassword = (pPlaintextPassword == null ? "" : pPlaintextPassword.trim());
        if (!plaintextPassword.isEmpty()) {
            final String encryptedPassword;
            if (isPasswordEncrypted(plaintextPassword)) {
                encryptedPassword = plaintextPassword;
            } else {
                encryptedPassword = encryptPassword(plaintextPassword);
            }
            setString(getXmlConfig(), "database/" + name + "/password", encryptedPassword);
            return true;
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends CpxJobConfig> Map<Integer, T> getActiveJobConfigs(final Class<T> pJobConfigClass) {
        final Map<Integer, CpxJobConfig> list = getActiveJobConfigs();
        final Map<Integer, T> result = new HashMap<>();
        if (pJobConfigClass != null) {
            Iterator<Map.Entry<Integer, CpxJobConfig>> it = list.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Integer, CpxJobConfig> entry = it.next();
                CpxJobConfig eif = entry.getValue();
                if (!pJobConfigClass.isAssignableFrom(eif.getClass())) {
                    //job (!eif.getClass().equals(pJobConfigClass)) {
                    it.remove();
                } else {
                    result.put(entry.getKey(), (T) eif);
                }
            }
        }
        return result;
    }

    @Override
    public Map<Integer, CpxJobConfig> getActiveJobConfigs() {
        final Map<Integer, CpxJobConfig> list = getJobConfigs();
        final Iterator<Map.Entry<Integer, CpxJobConfig>> it = list.entrySet().iterator();
        final Date date = new Date();
        while (it.hasNext()) {
            Map.Entry<Integer, CpxJobConfig> entry = it.next();
            if (entry.getValue() == null || !entry.getValue().isActive(date)) {
                it.remove();
            }
        }
        return list;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends CpxJobConfig> Map<Integer, T> getJobConfigs(final Class<T> pJobConfigClass) {
        final Map<Integer, CpxJobConfig> list = getJobConfigs();
        final Map<Integer, T> result = new HashMap<>();
        if (pJobConfigClass != null) {
            Iterator<Map.Entry<Integer, CpxJobConfig>> it = list.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Integer, CpxJobConfig> entry = it.next();
                CpxJobConfig eif = entry.getValue();
                if (!pJobConfigClass.isAssignableFrom(eif.getClass())) {
                    //job (!eif.getClass().equals(pJobConfigClass)) {
                    it.remove();
                } else {
                    result.put(entry.getKey(), (T) eif);
                }
            }
        }
        return result;
    }

    @Override
    public <T extends CpxJobConfig> Map<Integer, T> getJobConfigs(final Class<T> pJobConfigClass, final String pName) {
        final String name = pName == null ? "" : pName.trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("No name for external interface was passed!");
        }

        final Map<Integer, T> list = getJobConfigs(pJobConfigClass);
        if (!name.isEmpty()) {
            Iterator<Map.Entry<Integer, T>> it = list.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Integer, T> entry = it.next();
                CpxJobConfig eif = entry.getValue();
                if (!eif.getName().equalsIgnoreCase(name)) {
                    it.remove();
                }
            }
        }
        return list;
    }

    @Override
    public <T extends CpxJobConfig> Map<Integer, T> getJobConfigs(final Class<T> pJobConfigClass, final long pId) {
        final Map<Integer, T> list = getJobConfigs(pJobConfigClass);
        Iterator<Map.Entry<Integer, T>> it = list.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, T> entry = it.next();
            CpxJobConfig eif = entry.getValue();
            if (eif.getId() != pId) {
                it.remove();
            }
        }
        return list;
    }

    @Override
    public Map<Integer, CpxJobConfig> getJobConfigs(final long pId) {
        final Map<Integer, CpxJobConfig> list = getJobConfigs();
        Iterator<Map.Entry<Integer, CpxJobConfig>> it = list.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, CpxJobConfig> entry = it.next();
            CpxJobConfig eif = entry.getValue();
            if (eif.getId() != pId) {
                it.remove();
            }
        }
        return list;
    }

    /**
     * Shortcut to get a Nexus configuration
     *
     * @param pName name of Nexus configuration
     * @return Nexus configuration
     */
    @Override
    public NexusJob getNexusConfig(final String pName) {
        return getJobConfig(NexusJob.class, pName);
    }

    /**
     * Shortcut to get a all Nexus configurations
     *
     * @return Nexus configurations
     */
    @Override
    public Map<Integer, NexusJob> getNexusConfigs() {
        return getJobConfigs(NexusJob.class);
    }

    /**
     * Shortcut to get a KISSMED configuration
     *
     * @param pName name of KISSMED configuration
     * @return KISSMED configuration
     */
    @Override
    public KissmedJob getKissmedConfig(final String pName) {
        return getJobConfig(KissmedJob.class, pName);
    }

    /**
     * Shortcut to get a all KISSMED configurations
     *
     * @return KISSMED configurations
     */
    @Override
    public Map<Integer, KissmedJob> getKissmedConfigs() {
        return getJobConfigs(KissmedJob.class);
    }

    /**
     * Shortcut to get a Medico configuration
     *
     * @param pName name of Medico configuration
     * @return Medico configuration
     */
    @Override
    public MedicoJob getMedicoConfig(final String pName) {
        return getJobConfig(MedicoJob.class, pName);
    }

    /**
     * Shortcut to get a all Medico configurations
     *
     * @return Medico configurations
     */
    @Override
    public Map<Integer, MedicoJob> getMedicoConfigs() {
        return getJobConfigs(MedicoJob.class);
    }

    /**
     * Shortcut to get an Orbis configuration
     *
     * @param pName name of Orbis configuration
     * @return Orbis configuration
     */
    @Override
    public OrbisJob getOrbisConfig(final String pName) {
        return getJobConfig(OrbisJob.class, pName);
    }

    /**
     * Shortcut to get a all Orbis configurations
     *
     * @return Orbis configurations
     */
    @Override
    public Map<Integer, OrbisJob> getOrbisConfigs() {
        return getJobConfigs(OrbisJob.class);
    }

    /**
     * Shortcut to get a Sample configuration
     *
     * @param pName name of Sample configuration
     * @return Sample configuration
     */
    @Override
    public SampleJob getSampleConfig(final String pName) {
        return getJobConfig(SampleJob.class, pName);
    }

    /**
     * Shortcut to get a all Sample configurations
     *
     * @return Sample configurations
     */
    @Override
    public Map<Integer, SampleJob> getSampleConfigs() {
        return getJobConfigs(SampleJob.class);
    }

    /**
     * Shortcut to get a P21 configuration
     *
     * @param pName name of P21 configuration
     * @return P21 configuration
     */
    @Override
    public P21Job getP21Config(final String pName) {
        return getJobConfig(P21Job.class, pName);
    }

    /**
     * Shortcut to get a all P21 configurations
     *
     * @return P21 configurations
     */
    @Override
    public Map<Integer, P21Job> getP21Configs() {
        return getJobConfigs(P21Job.class);
    }

    /**
     * Shortcut to get a Fdse configuration
     *
     * @param pName name of Fdse configuration
     * @return Fdse configuration
     */
    @Override
    public FdseJob getFdseConfig(final String pName) {
        return getJobConfig(FdseJob.class, pName);
    }

    /**
     * Shortcut to get a all Fdse configurations
     *
     * @return Fdse configurations
     */
    @Override
    public Map<Integer, FdseJob> getFdseConfigs() {
        return getJobConfigs(FdseJob.class);
    }
//    public CpxDatabaseBasedImportJob getDbConfig(final String pModuleName, final String pName) {
//        if (pModuleName == null || pModuleName.trim().isEmpty()) {
//            throw new IllegalArgumentException("Cannot fetch db config with name '" + pName + "', because module name cannot be null or empty!");
//        }
//    }

    /**
     * Shortcut to get a specific Database configuration
     *
     * @param pName name of Database configuration
     * @return Database configuration
     */
    @Override
    public CpxDatabaseBasedImportJob getDbConfig(final String pName) {
        return getJobConfig(CpxDatabaseBasedImportJob.class, pName);
    }

    /**
     * Shortcut to get a all Database configurations
     *
     * @return Database configurations
     */
    @Override
    public Map<Integer, CpxDatabaseBasedImportJob> getDbConfigs() {
        return getJobConfigs(CpxDatabaseBasedImportJob.class);
    }

    /**
     * Shortcut to get a specific File configuration
     *
     * @param pName name of File configuration
     * @return File configuration
     */
    @Override
    public CpxFileBasedImportJob getFileConfig(final String pName) {
        return getJobConfig(CpxFileBasedImportJob.class, pName);
    }

    /**
     * Shortcut to get a all File configurations
     *
     * @return File configurations
     */
    @Override
    public Map<Integer, CpxFileBasedImportJob> getFileConfigs() {
        return getJobConfigs(CpxFileBasedImportJob.class);
    }

    /**
     * Shortcut to get a specific SAP configuration
     *
     * @param pName name of SAP configuration
     * @return SAP configuration
     */
    @Override
    public SapJob getSapConfig(final String pName) {
        return getJobConfig(SapJob.class, pName);
    }

    /**
     * Shortcut to get a all SAP configurations
     *
     * @return SAP configurations
     */
    @Override
    public Map<Integer, SapJob> getSapConfigs() {
        return getJobConfigs(SapJob.class);
//        Map<Integer, SapJob> list = getJobConfigs(SapJob.class);
//        Map<Integer, SapJob> result = new LinkedHashMap<>();
//        if (!list.isEmpty()) {
//            Iterator<Map.Entry<Integer, SapJob>> it = list.entrySet().iterator();
//            while (it.hasNext()) {
//                Map.Entry<Integer, SapJob> entry = it.next();
//                result.put(entry.getKey(), entry.getValue());
//            }
//        }
//        return result;
    }

    @Override
    public <T extends CpxJobConfig> T getJobConfig(final Class<T> pJobConfigClass, final long pId) {
        final Map<Integer, T> result = getJobConfigs(pJobConfigClass, pId);
        if (result.isEmpty()) {
            return null;
        }
        if (result.size() > 1) {
            LOG.log(Level.WARNING, "Okay, that''s strange: There are multiple entries in cpx_server_config.xml for the same interface class and with the same interface id -> {0}/{1}. Maybe you should check that! Meanwhile I''ll always take the first entry.", new Object[]{pJobConfigClass.getName(), pId});
        }
        final T eif = result.entrySet().iterator().next().getValue();
        return eif;
    }

    @Override
    public <T extends CpxJobConfig> T getJobConfig(final Class<T> pJobConfigClass, final String pName) {
        final Map<Integer, T> result = getJobConfigs(pJobConfigClass, pName);
        if (result.isEmpty()) {
            return null;
        }
        if (result.size() > 1) {
            LOG.log(Level.WARNING, "Okay, that''s strange: There are multiple entries in cpx_server_config.xml for the same interface class and with the same interface name -> {0}/{1}. Maybe you should check that! Meanwhile I''ll always take the first entry.", new Object[]{pJobConfigClass.getName(), pName});
        }
        final T eif = result.entrySet().iterator().next().getValue();
        return eif;
    }

    @Override
    public Map<Integer, CpxJobConfig> getJobConfigs() {
        final Map<Integer, CpxJobConfig> result = new LinkedHashMap<>();
        //int lastIndex = getJobConfigLastIndex();
        //for (int idx = 1; idx <= lastIndex; idx++) {
        for (int idx = 1;; idx++) {
            try {
                CpxJobConfig eif = getJobConfig(idx);
                if (eif == null) {
                    //continue;
                    break;
                }
                result.put(idx, eif);
            } catch (IllegalStateException ex) {
                LOG.log(Level.SEVERE, "external interface seems to be broken, will ignore it", ex);
            }
        }
        return result;
    }

    @Override
    public Map<Integer, CpxJobConfig> getJobConfig(final long pId) {
        final Map<Integer, CpxJobConfig> list = getJobConfigs();
        if (pId > 0L) {
            Iterator<Map.Entry<Integer, CpxJobConfig>> it = list.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Integer, CpxJobConfig> entry = it.next();
                CpxJobConfig eif = entry.getValue();
                if (eif.getId() != pId) {
                    it.remove();
                }
            }
        }
        return list;
    }

    @Override
    public Map<Integer, CpxJobConfig> getJobConfig(final String pName) {
        final String name = pName == null ? "" : pName.trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("No name for external interface was passed!");
        }

        final Map<Integer, CpxJobConfig> list = getJobConfigs();
        if (!name.isEmpty()) {
            Iterator<Map.Entry<Integer, CpxJobConfig>> it = list.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Integer, CpxJobConfig> entry = it.next();
                CpxJobConfig eif = entry.getValue();
                if (!eif.getName().equalsIgnoreCase(name)) {
                    it.remove();
                }
            }
        }
        return list;
    }

    @Override
    public CpxJobConfig getJobConfig(final int pIndex) {
        if (pIndex <= 0) {
            return null;
        }
        final String key = "jobs/job[" + pIndex + "]/";
        final String clazzName = getString(getXmlConfig(), key + "@class");
        if (clazzName.isEmpty()) {
            return null;
        }
        final long id = getLong(getXmlConfig(), key + "@id");
        final Class<?> clazz;
        try {
            clazz = Class.forName(clazzName);
//        final CpxJobConfig eif;
//        try {
//            eif = (CpxJobConfig) Class.forName(clazz).getConstructor().newInstance();
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
//            throw new IllegalStateException("This is not a valid class for an external interface: " + clazz, ex);
//            //return null;
//        }
//
//        try {
//            Field idField = CpxJobConfig.class.getDeclaredField("id");
//            idField.setAccessible(true);
//            idField.set(eif, id);
//        } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException ex) {
//            throw new IllegalStateException("Was not able to set id " + id + ": " + clazz, ex);
//        }
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException("This is not a valid class for an external interface: " + clazzName, ex);
        }

        final String name = getString(getXmlConfig(), key + "@name");
        final String targetDatabase = getString(getXmlConfig(), key + "target_database");
        final Date beginDate = Lang.fromIsoDateTime(getString(getXmlConfig(), key + "begin_date"));
        final Date endDate = Lang.fromIsoDateTime(getString(getXmlConfig(), key + "end_date"));
        final long timePeriodValue = getLong(getXmlConfig(), key + "time_period_value");
        final ChronoUnit timePeriodUnit = getEnum(getXmlConfig(), key + "time_period_unit", ChronoUnit.values());
        final boolean active = getBoolean(getXmlConfig(), key + "active", true);

        final Date admissionDateFrom = Lang.fromIsoDate(getString(getXmlConfig(), key + "constraints/admission_date_from"));
        final Date admissionDateTo = Lang.fromIsoDate(getString(getXmlConfig(), key + "constraints/admission_date_to"));
        final Date dischargeDateFrom = Lang.fromIsoDate(getString(getXmlConfig(), key + "constraints/discharge_date_from"));
        final Date dischargeDateTo = Lang.fromIsoDate(getString(getXmlConfig(), key + "constraints/discharge_date_to"));
        final Collection<AdmissionCauseEn> admissionCauses = AdmissionCauseEn.findByNames(getString(getXmlConfig(), key + "constraints/admission_causes"));
        final Collection<AdmissionReasonEn> admissionReasons = AdmissionReasonEn.findByIds(getString(getXmlConfig(), key + "constraints/admission_reasons"));
        final Collection<IcdcTypeEn> icdTypes = IcdcTypeEn.findByIds(getString(getXmlConfig(), key + "constraints/icd_types"));
        final String caseNumbers = getString(getXmlConfig(), key + "constraints/case_numbers");
        CpxJobConstraints c;
        try {
            c = new CpxJobConstraints(admissionDateFrom, admissionDateTo, dischargeDateFrom, dischargeDateTo, admissionCauses, admissionReasons, icdTypes, caseNumbers);
        } catch (IOException ex) {
            LOG.log(Level.WARNING, "cannot read case numbers from file: " + caseNumbers, ex);
            c = new CpxJobConstraints(admissionDateFrom, admissionDateTo, dischargeDateFrom, dischargeDateTo, admissionCauses, admissionReasons, icdTypes);
        }
        final CpxJobConstraints constraints = c;

        //load common settings for configurations
//        eif.setName(name);
//        eif.setTargetDatabase(targetDatabase);
//        eif.setBeginDate(beginDate);
//        eif.setEndDate(endDate);
//        eif.setTimePeriodValue(timePeriodValue);
//        eif.setTimePeriodUnit(timePeriodUnit);
//        eif.setActive(active);
//        eif.setConstraints(constraints);
//        try {
//            eif.setConstraints(new CpxJobConstraints(admissionDateFrom, admissionDateTo, dischargeDateFrom, dischargeDateTo, admissionCauses, admissionReasons, icdTypes, caseNumbers));
//        } catch (IOException ex) {
//            LOG.log(Level.WARNING, "cannot read case numbers from file: " + caseNumbers, ex);
//            eif.setConstraints(new CpxJobConstraints(admissionDateFrom, admissionDateTo, dischargeDateFrom, dischargeDateTo, admissionCauses, admissionReasons, icdTypes));
//        }
        //load specific settings for configurations like SAP
        if (CpxJobConfig.isImportStatic(clazz)) {
            //final CpxJobImportConfig imp = (CpxJobImportConfig) eif;
            final ImportMode importMode = getEnum(getXmlConfig(), key + "import_mode", ImportMode.values(), ImportMode.Version);
            final boolean rebuildIndexes = getBoolean(getXmlConfig(), key + "rebuild_indexes", true);
            final GDRGModel grouperModel = getEnum(getXmlConfig(), key + "grouper_model", GDRGModel.values());
            final String whatGroup = getString( getXmlConfig(), key + "case_version", "local");
//            imp.setImportMode(importMode);
//            imp.setRebuildIndexes(rebuildIndexes);
//            imp.setGrouperModel(grouperModel);
            if (CpxJobConfig.isFileImportStatic(clazz)) {
                //final CpxFileBasedImportJob file = (CpxFileBasedImportJob) imp;
                final String inputDirectory = getString(getXmlConfig(), key + "input_directory");
//                file.setInputDirectory(inputDirectory);
                if (CpxJobConfig.isP21ImportStatic(clazz)) {
                    return setId(new P21Job(name, targetDatabase, importMode, inputDirectory, rebuildIndexes, grouperModel, constraints, timePeriodValue, timePeriodUnit, beginDate, endDate, active, whatGroup), id);
                }
                if (CpxJobConfig.isFdseImportStatic(clazz)) {
                    return setId(new FdseJob(name, targetDatabase, importMode, inputDirectory, rebuildIndexes, grouperModel, constraints, timePeriodValue, timePeriodUnit, beginDate, endDate, active, whatGroup), id);
                }
                if (CpxJobConfig.isSampleImportStatic(clazz)) {
                    return setId(new SampleJob(name, targetDatabase, importMode, inputDirectory, rebuildIndexes, grouperModel, constraints, timePeriodValue, timePeriodUnit, beginDate, endDate, active, whatGroup), id);
                }
                throw new IllegalStateException("unknown file based import job: " + clazzName);
            } else if (CpxJobConfig.isExternalImportStatic(clazz)) {
                //final CpxExternalSystemBasedJobImportConfig external = (CpxExternalSystemBasedJobImportConfig) imp;
                final String defaultHosIdent = getString(getXmlConfig(), key + "default_hos_ident");
                final String server = getString(getXmlConfig(), key + "server");
                final int port = getInt(getXmlConfig(), key + "port");
                final String user = getString(getXmlConfig(), key + "user");
                final String password = getString(getXmlConfig(), key + "password");
//                external.setDefaultHosIdent(defaultHosIdent);
//                external.setServer(server);
//                external.setPort(port);
//                external.setUser(user);
//                external.setPassword(password);
                if (CpxJobConfig.isSapImportStatic(clazz)) {
                    //final SapJob sap = (SapJob) external;
                    final String sysNr = getString(getXmlConfig(), key + "sys_nr");
                    final String mandant = getString(getXmlConfig(), key + "mandant");
                    final String institution = getString(getXmlConfig(), key + "institution");
                    final boolean importWard = getBoolean(getXmlConfig(), key + "import_ward", false);
                    final boolean writeJsonDump = getBoolean(getXmlConfig(), key + "write_json", false);
                    final boolean readJsopDump = getBoolean(getXmlConfig(), key + "use_json", false);
                    final boolean doAnonymize = getBoolean(getXmlConfig(), key + "anonymize", false);
                    final String jsonPath = getString(getXmlConfig(), key + "json_path", "");
                    return setId(new SapJob(name, targetDatabase, importMode, server, port, user, password, 
                            defaultHosIdent, sysNr, mandant, institution, rebuildIndexes, grouperModel, 
                            constraints, timePeriodValue, timePeriodUnit, beginDate, endDate, active,
                            importWard, readJsopDump, writeJsonDump, doAnonymize, jsonPath, whatGroup
                    ), id);
//                    sap.setSysNr(sysNr);
//                    sap.setMandant(mandant);
//                    sap.setInstitution(institution);
                }
                if (CpxJobConfig.isDbImportStatic(clazz)) {
                    //final CpxDatabaseBasedImportJob db = (CpxDatabaseBasedImportJob) external;
                    final DbDriverEn driver = getEnum(getXmlConfig(), key + "driver", DbDriverEn.values());
                    final String sourceDatabase = getString(getXmlConfig(), key + "database");
                    final String backupPath = getString(getXmlConfig(), key + "backup_path");
                    final boolean useBackup = getBoolean(getXmlConfig(), key + "use_backup");
                    final boolean doBackup = getBoolean(getXmlConfig(), key + "do_backup");
                    if (CpxJobConfig.isKissmedImportStatic(clazz)) {
                        return setId(new KissmedJob(name, targetDatabase, importMode, driver, sourceDatabase, server, port, user, 
                                password, defaultHosIdent, rebuildIndexes, grouperModel, constraints, timePeriodValue, timePeriodUnit, beginDate, endDate, active, backupPath, useBackup, doBackup, whatGroup), id);
                    }
                    if (CpxJobConfig.isNexusImportStatic(clazz)) {
                        return setId(new NexusJob(name, targetDatabase, importMode, driver, sourceDatabase, server, port, user, password, 
                                defaultHosIdent, rebuildIndexes, grouperModel, constraints, timePeriodValue, timePeriodUnit, beginDate, endDate, active, backupPath, useBackup, doBackup, whatGroup), id);
                    }
                    if (CpxJobConfig.isOrbisImportStatic(clazz)) {
                        return setId(new OrbisJob(name, targetDatabase, importMode, driver, sourceDatabase, server, port, user, password, defaultHosIdent, 
                                rebuildIndexes, grouperModel, constraints, timePeriodValue, timePeriodUnit, beginDate, endDate, active, backupPath, useBackup, doBackup, whatGroup), id);
                    }
                    if (CpxJobConfig.isMedicoImportStatic(clazz)) {
                        return setId(new MedicoJob(name, targetDatabase, importMode, driver, sourceDatabase, server, port, user, password, 
                                defaultHosIdent, rebuildIndexes, grouperModel, constraints, timePeriodValue, timePeriodUnit, beginDate, endDate, active, backupPath, useBackup, doBackup, whatGroup), id);
                    }
                    throw new IllegalStateException("unknown database based import job: " + clazzName);
//                    db.setDriver();
//                    db.setDatabase(sourceDatabase);
                }
                throw new IllegalStateException("unknown external based import: " + clazzName);
            }
        } else if (CpxJobConfig.isBatchgroupingStatic(clazz)) {
             final String whatGroup = getString( getXmlConfig(), key + "case_version", "local");
//            final BatchgroupingJob grp = (BatchgroupingJob) eif;
            final GDRGModel grouperModel = getEnum(getXmlConfig(), key + "grouper_model", GDRGModel.values(), GDRGModel.AUTOMATIC);
            return setId(new BatchgroupingJob(name, targetDatabase, grouperModel, constraints, timePeriodValue, timePeriodUnit, beginDate, endDate, active, whatGroup), id);
            //getString(getXmlConfig(), key + "grouper_model");
            //grp.setGrouperModel(grouperModel);
        } else if (CpxJobConfig.isBillStatic(clazz)) {
            final String inputDirectory = getString(getXmlConfig(), key + "input_directory");
            return setId(new BillJob(name, targetDatabase, inputDirectory, constraints, timePeriodValue, timePeriodUnit, beginDate, endDate, active), id);
            //getString(getXmlConfig(), key + "grouper_model");
            //grp.setGrouperModel(grouperModel);
        }else if(CpxJobConfig.isGdvImportDocumentJobStatic(clazz)){
            final String gdvDirectory = getString(getXmlConfig(), key + "gdv_directory");
            final String archivDirectory = getString(getXmlConfig(), key + "archiv_directory");
            final String targetDir = getString(getXmlConfig(), key + "target_directory");
            final String emailFrom = getString(getXmlConfig(), key + "email_from");
            final String emailPassword = getString(getXmlConfig(), key + "email_password");
            final String emailTo = getString(getXmlConfig(), key + "email_to");
            final String emailPort = getString(getXmlConfig(), key + "email_port");
            final String emailHost = getString(getXmlConfig(), key + "email_host");
            final String emailDebug = getString(getXmlConfig(), key + "email_debug");
            return setId(new GdvImportDocumentJob(name, targetDatabase, constraints, timePeriodValue, 
                    timePeriodUnit, beginDate, endDate, active,
                    gdvDirectory, targetDir, archivDirectory, 
                    emailFrom, emailPassword, emailTo, emailPort, emailHost, emailDebug), id); 
        }
        throw new IllegalStateException("unknown cpx job: " + clazzName);
        
//        return eif;
    }

    private static CpxJobConfig setId(final CpxJobConfig pJob, final long pId) {
        if (pJob == null) {
            return pJob;
        }
//        try {
//            eif = (CpxJobConfig) Class.forName(clazz).getConstructor().newInstance();
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
//            throw new IllegalStateException("This is not a valid class for an external interface: " + clazz, ex);
//            //return null;
//        }

        try {
            Field idField = CpxJobConfig.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(pJob, pId);
        } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException ex) {
            throw new IllegalStateException("Was not able to set id " + pId + ": " + pJob.getClassName(), ex);
        }
        return pJob;
    }

//    public int getJobConfigLastIndex() {
//        HierarchicalConfiguration<ImmutableNode> nodes = getXmlConfig().configurationAt("jobs");
//        nodes.getList("node()");
//        nodes.getList("job");
//        nodes.getKeys()
//        return 0;
//        //int lastIndex = getXmlConfig().getStringArray("jobs/job");
//        //return lastIndex;
////        int lastIndex = 0;
////        for (int i = 1;; i++) {
////            final String key = "jobs/job[" + i + "]/.";
////            final Object eif = getXmlConfig().getProperty("jobs/job");
////            //final Object eif = getXmlConfig().getProperty("jobs/job/child::node()"); // getXmlConfig().getProperty("jobs/*"); //[" + i + "]/node()");
////            //final Object eif = getXmlConfig().getProperty("jobs/job[" + i + "]/node()");
////            //final Object eif = getXmlConfig().getProperty("jobs/job[" + i + "]/@*");
////            if (eif == null) {
////                break;
////            } else {
////                lastIndex = i;
////            }
////        }
////        return lastIndex;
//    }
    @Override
    public synchronized <T extends CpxJobConfig> boolean removeJobConfig(
            final T pJobConfig
    ) {
        if (pJobConfig == null) {
            return true;
        }
        return removeJobConfig(pJobConfig.getClass(), pJobConfig.getName());
    }

    @Override
    public synchronized <T extends CpxJobConfig> boolean removeJobConfig(
            final Class<T> pJobConfigClass, final String pName
    ) {
        if (pJobConfigClass == null) {
            return false;
        }
        final Map<Integer, T> result = getJobConfigs(pJobConfigClass, pName);
        if (result.isEmpty()) {
            return false;
        }
        if (result.size() > 1) {
            LOG.log(Level.WARNING, "Okay, that''s strange: There are multiple entries in cpx_server_config.xml for the same interface class and with the same interface name -> {0}/{1}. Maybe you should check that! Meanwhile I''ll always take the first entry.", new Object[]{pJobConfigClass.getName(), pName});
        }
        int index = result.entrySet().iterator().next().getKey();
        final String key = "jobs/job[" + index + "]";
        getXmlConfig().clearTree(key);
        return true;
    }

    @Override
    public synchronized <T extends CpxJobConfig> boolean saveJobConfig(
            final T pJobConfig
    ) {
        if (pJobConfig == null) {
            return false;
        }
        final String name = pJobConfig.getName() == null ? "" : pJobConfig.getName().trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("No name for external interface was passed!");
        }

        @SuppressWarnings("unchecked")
        final Map<Integer, T> result = getJobConfigs((Class<T>) pJobConfig.getClass(), pJobConfig.getName());
        final String key;
        if (result.isEmpty()) {
            key = "jobs/job[last()]/";

            //ensure that parent node exists
            setString(getXmlConfig(), "jobs", null);
            getXmlConfig().addProperty("jobs/job", "");
        } else {
            if (result.size() > 1) {
                LOG.log(Level.WARNING, "Okay, that''s strange: There are multiple entries in cpx_server_config.xml for the same interface class and with the same interface name -> {0}/{1}. Maybe you should check that! Meanwhile I''ll always take the first entry.", new Object[]{pJobConfig.getClassName(), pJobConfig.getName()});
            }
            int index = result.entrySet().iterator().next().getKey();
            key = "jobs/job[" + index + "]/";
        }

        //save common settings for configurations
        setLong(getXmlConfig(), key + "@id", pJobConfig.getId());
        setString(getXmlConfig(), key + "@name", pJobConfig.getName());
        setString(getXmlConfig(), key + "@class", pJobConfig.getClassName());
        setString(getXmlConfig(), key + "target_database", pJobConfig.getTargetDatabase());
        setString(getXmlConfig(), key + "begin_date", Lang.toIsoDateTime(pJobConfig.getBeginDate()));
        setString(getXmlConfig(), key + "end_date", Lang.toIsoDateTime(pJobConfig.getEndDate()));
        setLong(getXmlConfig(), key + "time_period_value", pJobConfig.getTimePeriodValue());
        setString(getXmlConfig(), key + "time_period_unit", (pJobConfig.getTimePeriodUnit() == null ? null : pJobConfig.getTimePeriodUnit().name()));
        setBoolean(getXmlConfig(), key + "active", pJobConfig.isActive());
        setString(getXmlConfig(), key + "constraints/admission_date_from", Lang.toIsoDate(pJobConfig.getConstraints().getAdmissionDateFrom()));
        setString(getXmlConfig(), key + "constraints/admission_date_to", Lang.toIsoDate(pJobConfig.getConstraints().getAdmissionDateTo()));
        setString(getXmlConfig(), key + "constraints/discharge_date_from", Lang.toIsoDate(pJobConfig.getConstraints().getDischargeDateFrom()));
        setString(getXmlConfig(), key + "constraints/discharge_date_to", Lang.toIsoDate(pJobConfig.getConstraints().getDischargeDateTo()));
        setString(getXmlConfig(), key + "constraints/admission_causes", String.join(",", pJobConfig.getConstraints().getAdmissionCauseValues()));
        setString(getXmlConfig(), key + "constraints/admission_reasons", String.join(",", pJobConfig.getConstraints().getAdmissionReasonValues()));
        setString(getXmlConfig(), key + "constraints/icd_types", String.join(",", pJobConfig.getConstraints().getIcdTypeValues()));
        setString(getXmlConfig(), key + "constraints/case_numbers", String.join(",", pJobConfig.getConstraints().getCaseNumberValues()));

        //save specific settings for configurations like SAP
        if (pJobConfig.isImport()) {
            final CpxJobImportConfig imp = (CpxJobImportConfig) pJobConfig;
            setString(getXmlConfig(), key + "import_mode", imp.getImportMode().name());
            setBoolean(getXmlConfig(), key + "rebuild_indexes", imp.isRebuildIndexes());
            setString(getXmlConfig(), key + "grouper_model", (imp.getGrouperModel() == null ? null : imp.getGrouperModel().name()));
            if (imp.isFileImport()) {
                final CpxFileBasedImportJob file = (CpxFileBasedImportJob) imp;
                setString(getXmlConfig(), key + "input_directory", file.getInputDirectory());
            } else if (imp.isExternalImport()) {
                final CpxExternalSystemBasedJobImportConfig external = (CpxExternalSystemBasedJobImportConfig) imp;
                setString(getXmlConfig(), key + "default_hos_ident", external.getDefaultHosIdent());
                setString(getXmlConfig(), key + "server", external.getServer());
                setInt(getXmlConfig(), key + "port", external.getPort());
                setString(getXmlConfig(), key + "user", external.getUser());
                setString(getXmlConfig(), key + "password", external.getPassword());
                if (pJobConfig.isSapImport()) {
                    final SapJob sap = (SapJob) imp;
                    setString(getXmlConfig(), key + "sys_nr", sap.getSysNr());
                    setString(getXmlConfig(), key + "mandant", sap.getMandant());
                    setString(getXmlConfig(), key + "institution", sap.getInstitution());
                    setBoolean(getXmlConfig(), key + "import_ward", sap.getImportWard());
                } else if (pJobConfig.isDbImport()) {
                    final CpxDatabaseBasedImportJob db = (CpxDatabaseBasedImportJob) imp;
                    final DbDriverEn driver = db.getDriver();
                    setString(getXmlConfig(), key + "driver", driver == null ? null : driver.name());
                    setString(getXmlConfig(), key + "source_database", db.getSourceDatabase());
                }
            }
        } else if (pJobConfig.isBatchgrouping()) {
            final BatchgroupingJob grp = (BatchgroupingJob) pJobConfig;
            setString(getXmlConfig(), key + "grouper_model", grp.getGrouperModel().name());
        } else if (pJobConfig.isBill()) {
            final BillJob bill = (BillJob) pJobConfig;
            setString(getXmlConfig(), key + "input_directory", bill.getInputDirectory());
        }

        return true;
    }

//    public synchronized boolean addJobConfig(final CpxExternalSystemBasedJobImportConfig pJobConfig) {
//        return setJobConfig(pJobConfig, 0);
//    }
    @Override
    public String getDatabaseDriver(final String pPersistenceUnit
    ) {
//        SapJob eif1 = new SapJob();
//        eif1.setName("SAP Test 1");
//        eif1.setServer("SAP Server 1");
//        eif1.setUser("SAP User 1");
//        eif1.setPassword("SAP Password 1");
//        saveJobConfig(eif1);
//
//        SapJob eif2 = new SapJob();
//        eif2.setName("SAP Test 2");
//        eif2.setServer("SAP Server 2");
//        eif2.setUser("SAP User 2");
//        eif2.setPassword("SAP Password 2");
//        eif2.setSysNr("SAP SysNr 2");
//        eif2.setMandant("SAP Mandant 2");
//        saveJobConfig(eif2);
//        eif2.setMandant("SAP Mandant -2- -> 3 :D");
//        saveJobConfig(eif2);
//
//        CpxExternalSystemBasedJobImportConfig eif = getJobConfig(2);
//        Map<Integer, CpxExternalSystemBasedJobImportConfig> eifs = getJobConfigs();
//
//        getJobConfigs(SapJob.class, "SAP Test 2");
//        
//        removeJobConfig(SapJob.class, "SAP Test 1");

        final String url = getDatabaseUrl(pPersistenceUnit);
        String driver = "";
        if (isOracle(url)) {
            driver = "oracle.jdbc.driver.OracleDriver";
        } else if (isSqlSrv(url)) {
            driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        }
        return driver;
    }

    @Override
    public String getDatabaseDialect(final String pPersistenceUnit
    ) {
        final String url = getDatabaseUrl(pPersistenceUnit);
        String dialect = "";
        if (isOracle(url)) {
            //dialect = "org.hibernate.dialect.Oracle10gDialect";
            dialect = "de.lb.hibernate.dialect.Oracle10gDialectExtended";
        } else if (isSqlSrv(url)) {
            dialect = "org.hibernate.dialect.SQLServer2012Dialect";
        }
        return dialect;
    }

//    public void rewriteConfigFile(final String pPersistenceUnit) {
//        File file = getPlaintextPasswordFile(pPersistenceUnit);
//        if (!file.exists()) {
//            return;
//        }
//        if (file.isDirectory()) {
//            return;
//        }
//        String plaintextPassword = null;
//        try ( CpxFileReader cpxFr = new CpxFileReader(file)) {
//            while (!cpxFr.eof()) {
//                plaintextPassword = cpxFr.readLine();
//                break;
//            }
//        } catch (IllegalArgumentException | IOException ex) {
//            LOG.log(Level.SEVERE, null, ex);
//        }
//        if (setDatabasePassword(pPersistenceUnit, plaintextPassword)) {
//            try {
//                Files.delete(file.toPath());
//                LOG.log(Level.FINEST, "deleted file: {0}", file.getAbsolutePath());
//            } catch (IOException ex) {
//                LOG.log(Level.WARNING, "was not able to delete file: " + file.getAbsolutePath(), ex);
//            }
////            boolean deleted = file.delete();
////            if (!deleted) {
////                LOG.log(Level.WARNING, "Was not able to deleted file " + file.getAbsolutePath());
////            }
//        }
//    }
    @Override
    public Map<String, String> getDatabaseProperties(final ConnectionString pConnectionString) {
        if (pConnectionString == null) {
            return new HashMap<>();
        }
        final String persistenceUnit = pConnectionString.getPersistenceUnit(); //dbsys1, dbsys2...
        final String database = pConnectionString.getDatabase(); //CPX_DEV, CPX_DEV1...
        return getDatabaseProperties(persistenceUnit, database);
    }

    @Override
    public Map<String, String> getDatabaseProperties(final String pConnectionString) {
        //Format of Database Parameter: PersistenceUnit:Database - so we have to split it now!
        ConnectionString connString = new ConnectionString(pConnectionString);

        final String persistenceUnit = connString.getPersistenceUnit(); //dbsys1, dbsys2...
        final String database = connString.getDatabase(); //CPX_DEV, CPX_DEV1...
        return getDatabaseProperties(persistenceUnit, database);
    }

    @Override
    public DatabaseInfo getDatabaseInfo(final String pConnectionString) {
        ConnectionString connString = new ConnectionString(pConnectionString);
        final Map<String, String> props = getDatabaseProperties(connString);
        if (props.isEmpty()) {
            return new DatabaseInfo("", "", "", "");
        }
        String url = props.get("javax.persistence.jdbc.url");
        return new DatabaseInfo(connString.connectionString, url, "", "");
    }

    @Override
    public String getDbIdentifier(final String pConnectionString) {
        DatabaseInfo dbInfo = getDatabaseInfo(pConnectionString);
        return dbInfo.getIdentifier();
        //String user = props.get("javax.persistence.jdbc.user");
        //String password = props.get("javax.persistence.jdbc.password");
    }

    @Override
    public Map<String, String> getDatabaseProperties(final String pPersistenceUnit, final String pDatabase) {
        //rewriteConfigFile(pPersistenceUnit);
        final String pw = getDatabasePassword(pPersistenceUnit);
        final String database = (pDatabase == null) ? "" : pDatabase.trim();
        final String url = getDatabaseUrl(pPersistenceUnit);
        final String user = getDatabaseUser(pPersistenceUnit);
        final String password = decryptPassword(pw);
        final String driver = getDatabaseDriver(pPersistenceUnit);
        final String dialect = getDatabaseDialect(pPersistenceUnit);

        if (pw != null && pw.equals(password)) {
            //password is in plain text, replace this in cpx_server_config.xml with more secure encrypted password
            LOG.log(Level.INFO, "your current database password for persistence unit {0} is in plain text. To improve security I will encrypt it now in your CPX Server Config...", pPersistenceUnit);
            final String encodedPw = encryptPassword(password);
            if (setDatabasePassword(pPersistenceUnit, encodedPw)) {
                LOG.log(Level.FINEST, "password encryption done, enjoy your gain of security with this -> {0}", encodedPw);
            }
        }

        // Falls nötig, Map-Eintrag für URL erstellen
        //factoryMap.computeIfAbsent(key, u -> {
        Map<String, String> props = new HashMap<>();

        props.put("hibernate.transaction.jta.platform", "org.hibernate.service.jta.platform.internal.JBossAppServerJtaPlatform");
        props.put("hibernate.order_updates", "true");
        props.put("hibernate.order_inserts", "true");
        props.put("hibernate.integration.envers.enabled", "false");
        props.put("hibernate.default_batch_fetch_size", "25");
        props.put("hibernate.jdbc.batch_size", "25");
        props.put("hibernate.show_sql", "false");
        props.put("hibernate.format_sql", "true");
        props.put("javax.persistence.jdbc.url", url);
        props.put("javax.persistence.jdbc.user", user);
        props.put("javax.persistence.jdbc.password", password);
        props.put("javax.persistence.jdbc.driver", driver);
        //props.put("hibernate.current_session_context_class", "thread");
        props.put("hibernate.dialect", dialect);
        props.put("hibernate.event.merge.entity_copy_observer", "allow");
        props.put("hibernate.enable_lazy_load_no_trans", "false");
        props.put("hibernate.hbm2ddl.jdbc_metadata_extraction_strategy", "individually");
        props.put("hibernate.jpa.compliance.global_id_generators", "false");
        props.put("hibernate.enhancer.enableLazyInitialization", "true");
//        props.put("hibernate.show_sql", "true");
// props.put("hibernate.show_sql", "true");   
        if (!database.isEmpty()) {
            props.put("hibernate.hbm2ddl.auto", "update"); //validate | update | create | create-drop | none
            //props.put("hibernate.hbm2ddl.auto", "create");
            if (isOracle(url)) {
                props.put("javax.persistence.jdbc.user", database);
//                if (url.equals("jdbc:oracle:thin:@LB498:1521:ORCL")) {
//                    //               props.put("hibernate.hbm2ddl.auto", "create");                
//                    props.put("hibernate.show_sql", "true");
//                }
                //props.put("hibernate.show_sql", "true");
            } else if (isSqlSrv(url)) {
                props.put("javax.persistence.jdbc.url", url + ";DATABASE=" + database + ";sendStringParametersAsUnicode=false;loginTimeout=120");
//                if(url.equals("jdbc:sqlserver://lb498:1433")){
//                    props.put("hibernate.show_sql", "true");
//                }
            }
            if (props.get("hibernate.hbm2ddl.auto") != null && !props.get("hibernate.hbm2ddl.auto").trim().equalsIgnoreCase("validate")) {
                LOG.log(Level.WARNING, "hibernate.hbm2ddl.auto IS SET TO ''{0}'' INSTEAD OF ''validate''. POSSIBLE LOSS OF DATA! WILL PROBABLY DROP DATABASE!", props.get("hibernate.hbm2ddl.auto"));
            }
        } else {
            //When database is empty, then default value from persistence.xml will be used
            props.put("hibernate.hbm2ddl.auto", "");
        }

        return props;
    }

    @Override
    //@AccessTimeout(value = 60000, unit = java.util.concurrent.TimeUnit.MILLISECONDS)
    public boolean getRulesDatabaseConfig() {
        return getBoolean(getXmlConfig(), "rules/store_in_database", false);
    }
    
    @Override
     public boolean getShowRelevantRules() {
        return getBoolean(getXmlConfig(), "rules/show_relevant_rules", false);
    }

    @Override
    public void setRulesDatabaseConfig(final boolean inDB) {
        setBoolean(getXmlConfig(), "rules/store_in_database", inDB);
    }

    @Override
    public void setShowRelevantRules(final boolean config){
        setBoolean(getXmlConfig(), "rules/show_relevant_rules", config);
    }
    @Override
    public void setCommonHealthStatusVisualization(final boolean pEnableHealthStatusVisualization) {
        setBoolean(getXmlConfig(), "common/health_status_visualization", pEnableHealthStatusVisualization);
    }

    @Override
    public boolean getCommonHealthStatusVisualization() {
        return getBoolean(getXmlConfig(), "common/health_status_visualization", false);
    }

    @Override
    public boolean isRuleEditorClient() {
        return getBoolean(getXmlConfig(), "rules_editor/editor_enabled", false);
    }

    @Override
    public void setRuleEditorClient(boolean pRuleEditorClient) {
        setBoolean(getXmlConfig(), "rules_editor/editor_enabled", pRuleEditorClient);
    }

    @Override
    public License readLicense() {
        final CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();
        final String licensePathTmp = cpxProps.getCpxServerLicenseDir() + cpxProps.getFileSeparator() + LicenseWriter.DEFAULT_LICENSE_FILENAME;
        LOG.log(Level.INFO, "Load license from {0}...", licensePathTmp);
        final String licenseFilename = licensePathTmp;
        //LOG.info("Will import to database: " + caseDb);
        License license = License.loadFromLicenseFile(licenseFilename);
        LOG.log(Level.INFO, "License found: {0}", String.valueOf(license));
        if (!license.isValid()) {
            LOG.log(Level.WARNING, "This license file is invalid: {0}", licenseFilename);
        }
        return license;
    }

    @Override
    public Date getSapChangeDate(final String pCaseDb) throws SQLException {
        Date chDate = null;
        final String identifier = getDbIdentifier(pCaseDb);
        try (final Connection commonDbConnection = getJdbcConnection(CpxServerConfig.COMMONDB)) {
            LOG.log(Level.INFO, "Hurray! Connection to common database '" + CpxServerConfig.COMMONDB + "' established!");
            //delete old entries?
            //final String connectionUrl = getConnectionUrl(commonDbConnection);
            //final boolean isOracle = isOracle(connectionUrl);
            //try (Statement stmt = commonDbConnection.createStatement()) {
            //    final String query = "DELETE FROM FROM C_JOB_LOG WHERE ...";
            //    stmt.execute(query);
            //}
            final String query = "SELECT MAX(END_DATE) END_DATE FROM C_JOB_LOG WHERE LOWER(DB) = LOWER(?)";
            try (PreparedStatement pstmt = commonDbConnection.prepareStatement(query)) {
                //pstmt.setString(1, caseDb);
                pstmt.setString(1, identifier);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        chDate = rs.getDate("END_DATE");
                        if (chDate != null) {
                            LOG.log(Level.INFO, "Found end date of previous import in C_JOB_LOG: {0}", chDate);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(chDate);
                            final int days = 2;
                            cal.add(Calendar.DAY_OF_YEAR, -days);
                            cal.set(Calendar.HOUR_OF_DAY, 0);
                            cal.set(Calendar.MINUTE, 0);
                            cal.set(Calendar.SECOND, 0);
                            cal.set(Calendar.MILLISECOND, 0);
                            chDate = cal.getTime();
                        }
                    }
                }
            }
        }
//        catch (SQLException ex) {
//            LOG.log(Level.SEVERE, "Was not able to establish connection to CPX Common Database: " + CpxServerConfig.COMMONDB, ex);
//            return null;
//        }

        final Date changeDate;
        if (chDate != null) {
            changeDate = chDate;
            LOG.log(Level.INFO, "Previous import found in C_JOB_LOG, will use {0} as change date", changeDate);
        } else {
            changeDate = getSapChangeDateFallback();
            LOG.log(Level.INFO, "No previous import found in C_JOB_LOG, will use {0} as change date", changeDate);
        }
        return changeDate;
    }

    public static Date getSapChangeDateFallback() {
        final Date today = new Date();
        final Calendar c = Calendar.getInstance();
        c.setTime(today);
        final int weeks = 2;
        c.add(Calendar.DAY_OF_YEAR, -weeks * 7);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    @Override
    public boolean useHistoryCases4Group() {
        return getBoolean(getXmlConfig(), "rules/use_history_cases", false);
    }

    @Override
    public void setUseHistoryCases4Group(final boolean pUseIt) {
        setBoolean(getXmlConfig(), "rules/use_history_cases", pUseIt);
    }
    
    @Override
    public String getPdfReportPeppXslFilePath() {
        return getString(getXmlConfig(), "pdf_report/report_pepp_xsl_file", "");
    }
    
    @Override
    public void setPdfReportPeppXslFilePath(String xslFilePath) {
        setString(getXmlConfig(), "pdf_report/report_pepp_xsl_file", xslFilePath);
    }

    @Override
    public boolean getSapSendMDStateToSAP() {
        return getBoolean(getXmlConfig(), "sap/send_md_state_to_sap", false);
    }

    @Override
    public void setSapSendMDStateToSAP(boolean pSendMDStateToSAP) {
        setBoolean(getXmlConfig(), "sap/send_md_state_to_sap", pSendMDStateToSAP);
    }

    @Override
    public boolean getUseGdv() {
        return getBoolean(getXmlConfig(), "gdv/use_gdv", false);
    }

    @Override
    public void setUseGdv(boolean pUseGdv) {
        setBoolean(getXmlConfig(), "gdv/use_gdv", pUseGdv);
    }

    @Override
    public boolean getSendGdvResponce() {
        return getBoolean(getXmlConfig(), "gdv/send_responce", false);
    }

    @Override
    public void setSendGdvResponce(boolean pSendGdvResponce) {
        setBoolean(getXmlConfig(), "gdv/send_responce", pSendGdvResponce);
    }

    @Override
    public String getGdvResponcePath() {
        return getString(getXmlConfig(), "gdv/responce_path", "");
    }

    @Override
    public void setGdvResponcePath(String pPath) {
        setString(getXmlConfig(), "gdv/responce_path", pPath);
    }
}
