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
package de.lb.cpx.client.core.config;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.config.AbstractCpxConfig;
import de.lb.cpx.config.ExtendedXMLConfiguration;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.grouper.model.transfer.BatchGroupParameter;
import de.lb.cpx.model.enums.DetailsFilterEn;
import de.lb.cpx.model.enums.SearchListTypeEn;
import de.lb.cpx.server.commons.enums.RuleImportCheckFlags;
import de.lb.cpx.server.commons.enums.RuleOverrideFlags;
import de.lb.cpx.service.ejb.ConfigurationServiceEJBRemote;
import de.lb.cpx.service.properties.SearchListProperties;
import de.lb.cpx.shared.dto.HistoryFilter;
import de.lb.cpx.shared.dto.RuleFilterDTO;
import de.lb.cpx.shared.filter.enums.SearchListTypeFactory;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.p21util.P21ExportSettings;
import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import java.io.File;
import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javax.batch.runtime.BatchStatus;
import javax.ejb.Singleton;
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
public class CpxClientConfig extends AbstractCpxConfig /* implements CpxClientConfigLocal */ {

    private static CpxClientConfig instance = null;
    private static final Logger LOG = Logger.getLogger(CpxClientConfig.class.getSimpleName());

    private EjbProxy<ConfigurationServiceEJBRemote> configurationService = null;
    private ReloadingFileBasedConfigurationBuilder<ExtendedXMLConfiguration> xmlBuilder = null;
    private ReloadingFileBasedConfigurationBuilder<ExtendedXMLConfiguration> xmlUserBuilder = null;
    private final ReadOnlyObjectWrapper<GDRGModel> selectedGrouperProperty = new ReadOnlyObjectWrapper<>();

    private CpxClientConfig() {

    }

    public static synchronized CpxClientConfig instance() {
        if (instance == null) {
            instance = new CpxClientConfig();
        }
        return instance;
    }

    public void reset() {
        configurationService = Session.instance().getEjbConnector().connectConfigurationServiceBean();
    }

    public File getXmlConfigFile() {
        CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();
        File configFile = new File(cpxProps.getCpxClientConfigFile());
        return configFile;
    }

    public synchronized ExtendedXMLConfiguration getXmlConfig() {
        if (xmlBuilder == null) {
            //CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();
            //File configFile = new File(cpxProps.getCpxClientConfigFile());
            File configFile = getXmlConfigFile();
//            try {
//                createXmlConfigFile(configFile.getAbsolutePath());
//            } catch (FileSystemException ex) {
//                final String msg = "Was not able to open or to create config file '" + configFile.getAbsolutePath() + "'";
//                //LOG.log(Level.SEVERE, msg, ex);
//                throw new IllegalStateException(msg, ex);
//            }
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

            // Register an event listener for triggering reloading checks
            builder.addEventListener(ConfigurationBuilderEvent.CONFIGURATION_REQUEST,
                    new EventListener<ConfigurationBuilderEvent>() {

                @Override
                public void onEvent(ConfigurationBuilderEvent event) {
                    builder.getReloadingController().checkForReloading(null);
                }
            });

            xmlBuilder = builder;
        }
        try {
            return xmlBuilder.getConfiguration();
        } catch (ConfigurationException ex) {
            String msg = "Client configuration file seems to be corrupted: " + (xmlBuilder.getFileHandler() == null || xmlBuilder.getFileHandler().getFile() == null ? "null" : xmlBuilder.getFileHandler().getFile().getAbsolutePath());
            throw new IllegalStateException(msg, ex);
        }
    }

    public synchronized ExtendedXMLConfiguration getXmlUserConfig() {
        if (xmlUserBuilder == null) {
            CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();
            File configFile = new File(cpxProps.getCpxClientUserConfigFile());
            try {
                createXmlConfigFile(configFile.getAbsolutePath());
            } catch (FileSystemException ex) {
                final String msg = "Was not able to open or to create config file '" + configFile.getAbsolutePath() + "'";
                //LOG.log(Level.SEVERE, msg, ex);
                throw new IllegalStateException(msg, ex);
            }
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

            // Register an event listener for triggering reloading checks
            builder.addEventListener(ConfigurationBuilderEvent.CONFIGURATION_REQUEST,
                    new EventListener<ConfigurationBuilderEvent>() {

                @Override
                public void onEvent(ConfigurationBuilderEvent event) {
                    builder.getReloadingController().checkForReloading(null);
                }
            });

            xmlUserBuilder = builder;
        }
        int max_tries = 5;
        for (int tr = 1; tr <= max_tries; tr++) {
            try {
                //when multiple clients log in at the same time a ConfigurationException 
                //can occur when they write to cpx_client_user_config at the same time!
                return xmlUserBuilder.getConfiguration();
            } catch (ConfigurationException ex) {
                if (tr == max_tries) {
                    //LOG.log(Level.SEVERE, msg, ex);
                    String msg = "Client user configuration file seems to be corrupted: " + (xmlUserBuilder.getFileHandler() == null || xmlUserBuilder.getFileHandler().getFile() == null ? "null" : xmlUserBuilder.getFileHandler().getFile().getAbsolutePath());
                    throw new IllegalStateException(msg, ex);
                } else {
                    String msg = "I'm temporarly not able to read from client user configuration file, maybe multiple clients are running: " + (xmlUserBuilder.getFileHandler() == null || xmlUserBuilder.getFileHandler().getFile() == null ? "null" : xmlUserBuilder.getFileHandler().getFile().getAbsolutePath());
                    LOG.log(Level.WARNING, "try #" + tr + ": " + msg);
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException ex1) {
                        LOG.log(Level.FINEST, null, ex1);
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
        return null;
    }

    @Override
    public synchronized List<Configuration> getConfigs() {
        getConfigurationService();
        List<Configuration> configs = new ArrayList<>();
        configs.add(getXmlConfig());
        return configs;
    }

    public synchronized EjbProxy<ConfigurationServiceEJBRemote> getConfigurationService() {
        if (configurationService == null) {
            configurationService = Session.instance().getEjbConnector().connectConfigurationServiceBean();
        }
        return configurationService;
    }

    /* Client Properties */
    public String getServerUser() {
        return getString(getXmlConfig(), "server/user"); //hasse
    }

    public void setServerUser(final String pServerUser) {
        setString(getXmlConfig(), "server/user", pServerUser);
    }

    public String getServerPassword() {
        return decryptPassword(getString(getXmlConfig(), "server/password")); //password
    }

    public void setServerPassword(final String pServerPassword) {
        String pw = (pServerPassword == null) ? "" : pServerPassword.trim();
        if (!isPasswordEncrypted(pw)) {
            pw = encryptPassword(pw);
        }
        setString(getXmlConfig(), "server/password", pw);
    }

    public int getServerPort() {
        return getInt(getXmlConfig(), "server/port"); //8085
    }

    public void setServerPort(final String pServerPort) {
        setString(getXmlConfig(), "server/port", pServerPort);
    }

    public String getServerHost() {
        return getString(getXmlConfig(), "server/host"); //localhost
    }

    public void setServerHost(final String pServerHost) {
        setString(getXmlConfig(), "server/host", pServerHost);
    }

    public String getLastSessionDatabase() {
        return getString(getXmlUserConfig(), "database");
    }

    public void setLastSessionDatabase(final String pDatabase) {
        setString(getXmlUserConfig(), "database", pDatabase);
    }

    /**
     * get selected Grouper Model from Database
     *
     * @return GrouperModel Object last selected and safed in config file
     */
    public GDRGModel getSelectedGrouper() {

        if (selectedGrouperProperty.getValue() == null) {
            GDRGModel model = GDRGModel.getModel2Name(getString(getXmlUserConfig(), "user_" + getUserId() + "/selected_grouper/@grouper"));
            if (model == null) {
                model = GDRGModel.AUTOMATIC;
            }
            setSelectedGrouper(model);
        }
        return selectedGrouperProperty.getValue();
    }
    public boolean isAutomaticGrouperSelected(){
        GDRGModel grouper = getSelectedGrouper();
        if(grouper == null){
            return false;
        }
        return GDRGModel.AUTOMATIC.equals(grouper);
    }
    /**
     * set slected GrouperModel in config File
     *
     * @param selectedGrouper selected Grouper Model in string representation
     */
    public void setSelectedGrouper(final GDRGModel selectedGrouper) {
        setString(getXmlUserConfig(), "user_" + getUserId() + "/selected_grouper", null); //creates an empty node (if it does not exist)
        setString(getXmlUserConfig(), "user_" + getUserId() + "/selected_grouper/@grouper", (selectedGrouper == null ? null : selectedGrouper.name()));
        selectedGrouperProperty.setValue(selectedGrouper);
    }

    /**
     * gets the current selected grouper property, should initialized as
     * automatic if non is selected in the config xml needs to be tested
     * AWI:20170103
     *
     * @return object property which contains the current selected grouper
     */
    public ReadOnlyObjectProperty<GDRGModel> getSelectedGrouperProperty() {
        return selectedGrouperProperty.getReadOnlyProperty();
    }

    /**
     * get Settings Value for BatchGrouping
     *
     * @return should results grouped only if there have not an DRG or PEPP
     */
    public boolean getBatchSettingsGrouped() {
        return Boolean.valueOf(getString(getXmlUserConfig(), "user_" + getUserId() + "/selected_grouper/grouped"));
    }

    /**
     * set Grouped Value in Settings
     *
     * @param grouped should results grouped only if there have not an DRG or
     * PEPP
     */
    public void setBatchSettingsGrouped(boolean grouped) {
        setString(getXmlUserConfig(), "user_" + getUserId() + "/selected_grouper/grouped", String.valueOf(grouped));
    }

    /**
     * get Settings Value for BatchGrouping
     *
     * @return should results grouped incl. His Cases
     */
    public DetailsFilterEn getBatchSettingsDetailsFilter() {
        final String value = getString(getXmlUserConfig(), "user_" + getUserId() + "/selected_grouper/details_filter");
        if (value.isEmpty()) {
            return DetailsFilterEn.ACTUAL_LOCAL;
        }
        return DetailsFilterEn.valueOf(value);
    }

    /**
     * set Grouped Value in Settings
     *
     * @param pDetailsFilter should results grouped only if there have not an
     * DRG or PEPP
     */
    public void setBatchSettingsDetailsFilter(DetailsFilterEn pDetailsFilter) {
        setString(getXmlUserConfig(), "user_" + getUserId() + "/selected_grouper/details_filter", pDetailsFilter == null ? null : pDetailsFilter.name());
    }

//    /**
//     * get Settings Value for BatchGrouping
//     *
//     * @return should results grouped incl. His Cases
//     */
//    public boolean getBatchSettingsExtern() {
//        return Boolean.valueOf(getString(getXmlUserConfig(), "user_" + getUserId() + "/selected_grouper/extern"));
//    }
//
//    /**
//     * set Grouped Value in Settings
//     *
//     * @param extern should results grouped only if there have not an DRG or
//     * PEPP
//     */
//    public void setBatchSettingsExtern(boolean extern) {
//        setString(getXmlUserConfig(), "user_" + getUserId() + "/selected_grouper/extern", String.valueOf(extern));
//    }
    /**
     * get Settins Value for BatchGrouping
     *
     * @return should results grouped with automatic Case check
     */
    public boolean getBatchSettingsDoRules() {
        return Boolean.valueOf(getString(getXmlUserConfig(), "user_" + getUserId() + "/selected_grouper/dorules"));
    }

    /**
     * set Grouped Value in Settings
     *
     * @param doRules should results grouped with automatic Case check
     */
    public void setBatchSettingsDoRules(boolean doRules) {
        setString(getXmlUserConfig(), "user_" + getUserId() + "/selected_grouper/dorules", String.valueOf(doRules));
    }

    /**
     * get Settins Value for BatchGrouping
     *
     * @return should results grouped with automatic Case correction
     */
    public boolean getBatchSettingsdoRulesSimulate() {
        return Boolean.valueOf(getString(getXmlUserConfig(), "user_" + getUserId() + "/selected_grouper/dorulessimulate"));
    }

    /**
     * set Grouped Value in Settings
     *
     * @param doRulesSimulate should results grouped with automatic Case
     * correction
     */
    public void setBatchSettingsDoRulesSimulate(boolean doRulesSimulate) {
        setString(getXmlUserConfig(), "user_" + getUserId() + "/selected_grouper/dorulessimulate", String.valueOf(doRulesSimulate));
    }

    /**
     * get Settins Value for BatchGrouping
     *
     * @return should results grouped with distribution of fees
     */
    public boolean getBatchSettingsSupplementaryFee() {
        return Boolean.valueOf(getString(getXmlUserConfig(), "user_" + getUserId() + "/selected_grouper/supplementaryfee"));
    }

    /**
     * set Grouped Value in Settings
     *
     * @param supplementaryFee should results grouped with distribution of fees
     */
    public void setBatchSettingsSupplementaryFee(boolean supplementaryFee) {
        setString(getXmlUserConfig(), "user_" + getUserId() + "/selected_grouper/supplementaryfee", String.valueOf(supplementaryFee));
    }

    /**
     * get Settins Value for BatchGrouping
     *
     * @return should results grouped with results saved for current role
     */
    public boolean getBatchSettingsDo4ActualRole() {
        return Boolean.valueOf(getString(getXmlUserConfig(), "user_" + getUserId() + "/selected_grouper/doforactualrole"));
    }

    /**
     * set Grouped Value in Settings
     *
     * @param do4ActualRole should results grouped with results saved for
     * current role
     */
    public void setBatchSettingsDo4ActualRole(boolean do4ActualRole) {
        setString(getXmlUserConfig(), "user_" + getUserId() + "/selected_grouper/doforactualrole", String.valueOf(do4ActualRole));
    }

    /**
     * get Settins Value for BatchGrouping
     *
     * @return should results grouped with gk check
     */
    public boolean getBatchSettingsMedAndRemedies() {
        return Boolean.valueOf(getString(getXmlUserConfig(), "user_" + getUserId() + "/selected_grouper/medandremedies"));
    }

    /**
     * set Grouped Value in Settings
     *
     * @param medAndRemedies should results grouped with gk check
     */
    public void setBatchSettingsMedsAndRemedies(boolean medAndRemedies) {
        setString(getXmlUserConfig(), "user_" + getUserId() + "/selected_grouper/medandremedies", String.valueOf(medAndRemedies));
    }

    /**
     * get Settins Value for BatchGrouping
     *
     * @return should results grouped with with DRG/PEPP Simulation
     */
    public boolean getBatchSettingsDoSimulate() {
        return Boolean.valueOf(getString(getXmlUserConfig(), "user_" + getUserId() + "/selected_grouper/dosimulate"));
    }

    /**
     * set Grouped Value in Settings
     *
     * @param doSimulate should results grouped with DRG/PEPP Simulation
     */
    public void setBatchSettingsDoSimulate(boolean doSimulate) {
        setString(getXmlUserConfig(), "user_" + getUserId() + "/selected_grouper/dosimulate", String.valueOf(doSimulate));
    }

    /**
     * get Settins Value for BatchGrouping
     *
     * @return should results grouped with with DRG/PEPP Simulation
     */
    public boolean getBatchSettingsDoHistoryCases() {
        return Boolean.valueOf(getString(getXmlUserConfig(), "user_" + getUserId() + "/selected_grouper/dohistorycases"));
    }

    /**
     * set Grouped Value in Settings
     *
     * @param doSimulate should results grouped with DRG/PEPP Simulation
     */
    public void setBatchSettingsDoHistoryCases(boolean doHistoryCases) {
        setString(getXmlUserConfig(), "user_" + getUserId() + "/selected_grouper/dohistorycases", String.valueOf(doHistoryCases));
    }

    public String getLastBatchJobId() {
        return getString(getXmlUserConfig(), "user_" + getUserId() + "/selected_grouper/lastbatchjob");
    }

    public void setLastBatchJobId(Long batchJobId) {
        setString(getXmlUserConfig(), "user_" + getUserId() + "/selected_grouper/lastbatchjob", String.valueOf(batchJobId));
    }

    public BatchStatus getLastBatchJobStatus() {
        String status = getString(getXmlUserConfig(), "user_" + getUserId() + "/selected_grouper/lastbatchjob/@status");
        if (status == null || status.isEmpty()) {
            return BatchStatus.FAILED;
        }
        return BatchStatus.valueOf(status);
    }

    public void setLastBatchJobStatus(String batchJobStatus) {
        setString(getXmlUserConfig(), "user_" + getUserId() + "/selected_grouper/lastbatchjob/@status", batchJobStatus);
    }

    /* user specific properties */
    public Long getUserId() {
        return Session.instance().getCpxUserId();
    }

    public void setUserName(final String pUserName) {
        setString(getXmlUserConfig(), "user_" + getUserId() + "/user_name", pUserName);
    }

    public String getUserRecentFileChooserPath() {
        return getUserRecentFileChooserPath(null);
    }

    public String getUserRecentFileChooserPath(final String pSection) {
        final String section = pSection == null ? "" : pSection.trim();
        return toStr(getString(getXmlUserConfig(),
                "user_" + getUserId() + "/file_chooser/recent_path/" + (section.isEmpty() ? "other" : section))
        );
    }

    public void setUserRecentFileChooserPath(final File pFile) {
        setUserRecentFileChooserPath(pFile, null);
    }

    public void setUserRecentFileChooserPath(final File pFile, final String pSection) {
        if (pFile == null) {
            LOG.log(Level.WARNING, "pFile is null!");
            return;
        }
        setUserRecentFileChooserPath(pFile.getAbsolutePath(), pSection);
    }

    public void setUserRecentFileChooserPath(final String pPath) {
        setUserRecentFileChooserPath(pPath, null);
    }

    public void setUserRecentFileChooserPath(final String pPath, final String pSection) {
        final String section = pSection == null ? "" : pSection.trim();
        String key = "user_" + getUserId() + "/file_chooser/recent_path/" + (section.isEmpty() ? "other" : section);
        String path = toStr(pPath);
        if (!path.isEmpty()) {
            //truncate file name and just store the pure path (because this might be a secret!)
            File file = new File(path);
            if (!file.exists()) {
                LOG.log(Level.FINE, "Using parent folder as recent file chooser path, because this folder does not exist: " + file.getAbsolutePath());
                file = file.getParentFile();
                path = file.getAbsolutePath();
            }
            if (!path.isEmpty() && !file.exists()) {
                LOG.log(Level.FINE, "Cannot set recent file chooser path, because this folder does not exist: " + file.getAbsolutePath());
                path = "";
            }
            if (!path.isEmpty() && !file.isDirectory()) {
                LOG.log(Level.FINE, "Using parent folder as recent file chooser path, because this path seems to be a file and not a folder: " + file.getAbsolutePath());
                file = file.getParentFile();
                path = file.getAbsolutePath();
            }
        }
        setString(getXmlUserConfig(), key, path);
    }

    public String getUserFontFamily() {
        return toStr(getString(getXmlUserConfig(),
                "user_" + getUserId() + "/font/family"),
                getFontFamily() //Fallback: Use server setting if empty
        );
    }

    public void setUserFontFamily(final String pFontFamily) {
        String key = "user_" + getUserId() + "/font/family";
        String fontFamily = toStr(pFontFamily);
        if (fontFamily.equalsIgnoreCase(getFontFamily())) {
            clearProperty(getXmlUserConfig(), key);
        } else {
            setString(getXmlUserConfig(), key, fontFamily);
        }
    }

    public String getUserFontSize() {
        return toStr(getString(getXmlUserConfig(),
                "user_" + getUserId() + "/font/size"),
                getFontSize() //Fallback: Use server setting if empty
        );
    }

    public void setUserFontSize(final String pFontSize) {
        String key = "user_" + getUserId() + "/font/size";
        String fontSize = toStr(pFontSize);
        if (fontSize.equalsIgnoreCase(getFontSize())) {
            clearProperty(getXmlUserConfig(), key);
        } else {
            setString(getXmlUserConfig(), key, fontSize);
        }
    }

    public HistoryFilter getUserHistoryFilter() {
        return configurationService.get().getUserHistoryFilter(getUserId());
    }

    public boolean setUserHistoryFilter(final HistoryFilter pHistoryFilter) {
        return configurationService.get().setUserHistoryFilter(getUserId(), pHistoryFilter);
    }

    public P21ExportSettings getUserP21ExportSettings() {
        return configurationService.get().getUserP21ExportSettings(getUserId());
    }

    public boolean setUserP21ExportSettings(final P21ExportSettings pP21ExportSettings) {
        return configurationService.get().setUserP21ExportSettings(getUserId(), pP21ExportSettings);
    }

    public RuleFilterDTO getRuleFilterCaseListForUser() {
        return configurationService.get().getRuleFilterCaseListForUser(getUserId());
    }

    public boolean setRuleFilterCaseListForUser(final RuleFilterDTO pRuleFilterDTO) {
        return configurationService.get().setRuleFilterCaseListForUser(getUserId(), pRuleFilterDTO);
    }

    public RuleFilterDTO getRuleFilterBatchAdmForUser() {
        return configurationService.get().getRuleFilterBatchAdmForUser(getUserId());
    }

    public boolean setRuleFilterBatchAdmForUser(final RuleFilterDTO pRuleFilterDTO) {
        return configurationService.get().setRuleFilterBatchAdmForUser(getUserId(), pRuleFilterDTO);
    }

    public String getUserLanguage() {
        return configurationService.get().getUserLanguage(getUserId());
    }

    public boolean setUserLanguage(final String pLanguage) {
        return configurationService.get().setUserLanguage(getUserId(), pLanguage);
    }

    public long[] getUserSelectedDraftCategories() {
        return configurationService.get().getUserSelectedDraftCategories(getUserId());
    }

    public boolean setUserSelectedDraftCategories(final long[] pSelectedDraftCategories) {
        return configurationService.get().setUserSelectedDraftCategories(getUserId(), pSelectedDraftCategories);
    }

    public Long getUserRecentMdkInternalId() {
        return configurationService.get().getUserRecentMdkInternalId(getUserId());
    }

    public boolean setUserRecentMdkInternalId(final Long pRecentMdkInternalId) {
        return configurationService.get().setUserRecentMdkInternalId(getUserId(), pRecentMdkInternalId);
    }

    public String getUserRecentClientScene() {
        return configurationService.get().getUserRecentClientScene(getUserId());
    }

    public boolean setUserRecentClientScene(final String pRecentClientScene) {
        return configurationService.get().setUserRecentClientScene(getUserId(), pRecentClientScene);
    }

    public String getRecentRuleListTab() {
        return configurationService.get().getRecentRuleListTab(getUserId());
    }

    public boolean setRecentRuleListTab(final String pRecentRuleListTab) {
        return configurationService.get().setRecentRuleListTab(getUserId(), pRecentRuleListTab);
    }

    public Integer getSearchListFetchSize() {
        return configurationService.get().getUserSearchListFetchSize(getUserId());
    }

    public boolean setSearchListFetchSize(final Integer pSearchListFetchSize) {
        return configurationService.get().setUserSearchListFetchSize(getUserId(), pSearchListFetchSize);
    }

    public boolean getWorkingListLocal() {
        return configurationService.get().getUserWorkingListLocal(getUserId());
    }

    public boolean setWorkingListLocal(final boolean pIsLocal) {
        return configurationService.get().setUserWorkingListLocal(getUserId(), pIsLocal);
    }

    public boolean getUserShowHistoryEventDetails() {
        return configurationService.get().getUserShowHistoryEventDetails(getUserId());
    }

    public boolean setUserShowHistoryEventDetails(final boolean pShowHistoryEventDetails) {
        return configurationService.get().setUserShowHistoryEventDetails(getUserId(), pShowHistoryEventDetails);
    }

    public boolean getUserShowHistoryDeleted() {
        return configurationService.get().getUserShowHistoryDeleted(getUserId());
    }

    public boolean setUserShowHistoryDeleted(final boolean pShowHistoryDeleted) {
        return configurationService.get().setUserShowHistoryDeleted(getUserId(), pShowHistoryDeleted);
    }

    public List<String> getLocales() {
        return configurationService.get().getLocales();
    }

    public SearchListProperties getNewSearchListProperties(final SearchListTypeEn pList) {
        if (pList == null) {
            throw new IllegalArgumentException("List type cannot be null!");
        }
        return SearchListTypeFactory.instance().getNewSearchListProperties(pList);
    }

//    public Long getSelectedSearchListId(final SearchListTypeEn pList) {
//        return configurationService.get().getSelectedSearchListId(getUserId(), pList);
//    }
//
//    public SearchListProperties getSearchListProperties(final Long pSearchListId) {
//        return configurationService.get().getSearchListProperties(pSearchListId);
////    }
//
//    public SearchListResult createNewSearchList(final SearchListTypeEn pList, final String pName, final boolean pIsSelected) {
////        final SearchListProperties searchListProperties = getNewSearchListProperties(pList);
//        //searchListProperties.setName(pName);
////        final CSearchList searchList = new CSearchList();
////        searchList.setSlName(pName);
////        searchList.setSlType(pList);
//        SearchListResult searchListResult = new SearchListResult(Session.instance().getCpxUserId(), pName, pList);
//        searchListResult = saveSearchList(searchListResult, pIsSelected);
//        return searchListResult;
//    }
//    
//    public SearchListResult createNewSearchList(final SearchListTypeEn pList, final String pName, final boolean pIsSelected) {
//        final SearchListProperties searchList = getNewSearchListProperties(pList);
//        searchList.setName(pName);
//        CSearchList dto = saveSearchList(searchList, true, pIsSelected);
//        return new SearchListResult(dto, searchList);
//    }
//    
//    public String getSelectedSearchListName(final SearchListTypeEn pList) {
//        try {
//            return configurationService.get().getSelectedSearchListName(getUserId(), pList);
//        } catch (IOException ex) {
//            LOG.log(Level.SEVERE, "Cannot get selected search list name", ex);
//        }
//        return "";
//    }
//    
//    public CSearchList getSelectedSearchListDTO(final SearchListTypeEn pList) {
//        try {
//            return configurationService.get().getSelectedSearchListDTO(getUserId(), pList);
//        } catch (IOException ex) {
//            LOG.log(Level.SEVERE, "Cannot get selected search list dto", ex);
//        }
//        return null;
//    }
//    
//    public SearchListResult getSelectedSearchList(final SearchListTypeEn pList, final boolean pLoadPropertiesEagerly) {
//        try {
//            return configurationService.get().getSelectedSearchList(getUserId(), pList, pLoadPropertiesEagerly);
//        } catch (IOException ex) {
//            LOG.log(Level.SEVERE, "Cannot get selected search list", ex);
//        }
//        return null;
//    }
//    
//    public Long getSelectedSearchListId(final SearchListTypeEn pList) {
//        try {
//            return configurationService.get().getSelectedSearchListId(getUserId(), pList);
//        } catch (IOException ex) {
//            LOG.log(Level.SEVERE, "Cannot get selected search list id", ex);
//        }
//        return null;
//    }
//
//    
//    public String getSelectedSearchListName(final SearchListTypeEn pList) {
//        try {
//            return configurationService.get().getSelectedSearchListName(getUserId(), pList);
//        } catch (IOException ex) {
//            LOG.log(Level.SEVERE, "Cannot get selected search list name", ex);
//        }
//        return null;
//    }
//
//    
//    public SearchListResult getSelectedSearchListResult(final SearchListTypeEn pList) {
//        return getSelectedSearchListResult(pList, true);
//    }
//
//    
//    public SearchListResult getSelectedSearchListResult(final SearchListTypeEn pList, final boolean pCreateNewIfEmpty) {
//        SearchListResult searchListResult = null;
//        try {
//            searchListResult = configurationService.get().getSelectedSearchListResult(getUserId(), pList);
//            if (pCreateNewIfEmpty && searchListResult == null) {
//                final boolean isSelected = true;
//                searchListResult = createNewSearchList(pList, "", isSelected);
//            }
//        } catch (IOException ex) {
//            LOG.log(Level.SEVERE, "Cannot get selected search list", ex);
//        }
//        return searchListResult;
//    }
//    public Map<Long, SearchListResult> getSearchLists() {
//        return configurationService.get().getSearchLists(getUserId());
//    }
//
//    public Map<Long, SearchListResult> getSearchLists(final Long[] pIds) {
//        return configurationService.get().getSearchLists(getUserId(), pIds);
//    }
//    
//    public SearchListResult getSearchList(final SearchListTypeEn pList, final Long pId) {
//        return configurationService.get().getSearchList(getUserId(), pList, pId);
//    }
//
//    
//    public SearchListResult getSearchList(final SearchListTypeEn pList, final String pName) {
//        return configurationService.get().getSearchList(getUserId(), pList, pName);
//    }
//    
//    public SearchList getSearchList(final SearchListTypeEn pList, final Long pId) {
//        try {
//            SearchList searchList = configurationService.get().getSearchList(getUserId(), pList, pId);
//            return searchList;
//        } catch (IOException ex) {
//            LOG.log(Level.SEVERE, "Cannot get search list", ex);
//        }
//        return null;
//    }
//
//    
//    public SearchList getSearchList(final SearchListDTO pSearchListDTO) {
//        try {
//            SearchList searchList = configurationService.get().getSearchList(getUserId(), pSearchListDTO);
//            return searchList;
//        } catch (IOException ex) {
//            LOG.log(Level.SEVERE, "Cannot get search list", ex);
//        }
//        return null;
//    }
//
//    
//    public SearchList getSearchList(final SearchListTypeEn pList, final String pName) {
//        try {
//            SearchList searchList = configurationService.get().getSearchList(getUserId(), pList, pName);
//            return searchList;
//        } catch (IOException ex) {
//            LOG.log(Level.SEVERE, "Cannot get search list", ex);
//        }
//        return null;
//    }
//    public boolean setSelectedSearchList(final CSearchList pSearchList) {
//        return configurationService.get().setSelectedSearchList(getUserId(), pSearchList);
//    }
//    
//    public boolean setSelectedSearchList(final SearchListDTO pSearchListDTO) {
//        return configurationService.get().setSelectedSearchList(getUserId(), pSearchListDTO);
//    }
//    
//    public boolean setSelectedSearchListName(final SearchListTypeEn pList, final String pName) {
//        return configurationService.get().setSelectedSearchListFileName(getUserId(), pList, pName);
//    }
//    
//    public Set<SearchListDTO> getSearchListDTOs(final SearchListTypeEn pList) {
//        return configurationService.get().getSearchListDTOs(getUserId(), pList);
//    }
//    
//    public SearchList getSearchListProperties(final SearchListTypeEn pList) {
//        try {
//            return configurationService.get().getSelectedSearchList(getUserId(), pList);
//            //return configurationService.get().getSearchListProperties(getUserId(), pList, pName);
//        } catch (IOException ex) {
//            LOG.log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//    
//    public String saveSearchList(final SearchListTypeEn pList, final SearchList pSearchList) {
//        try {
//            return configurationService.get().saveSearchList(getUserId(), pSearchList);
//            //return configurationService.get().setSearchListProperties(getUserId(), pList, pSearchList);
//        } catch (IOException ex) {
//            LOG.log(Level.SEVERE, "Cannot save search list", ex);
//        }
//        return "";
//    }
//    
//    public CSearchList saveSearchList(final SearchListProperties pSearchList, final boolean pCreateIfNotExists, final boolean pIsSelected) {
//        if (pSearchList == null || pSearchList.getName().isEmpty()) {
//            //DON'T SAVE EMPTY FILTER!
//            return null;
//        }
//        if (!pSearchList.isWriteable(Session.instance().getCpxUserId())) {
//            LOG.log(Level.FINER, "You're not the creator of this search list, so you're not allowed to store/overwrite it!");
//            return null;
//        }
//        try {
//            return configurationService.get().saveSearchList(getUserId(), pSearchList, pCreateIfNotExists, pIsSelected);
//            //return configurationService.get().setSearchListProperties(getUserId(), pList, pSearchList);
//        } catch (IOException ex) {
//            LOG.log(Level.SEVERE, "Cannot save search list and set it to default", ex);
//        }
//        return null;
//    }
//
//    
//    public SearchListResult saveSearchListResult(final SearchListResult pSearchListResult, final boolean pCreateIfNotExists, final boolean pIsSelected) {
//        if (pSearchListResult == null || pSearchListResult.getSearchList() == null || pSearchListResult.getSearchList().getName().isEmpty()) {
//            //DON'T SAVE EMPTY FILTER!
//            return null;
//        }
//        SearchListDTO dto = CpxClientConfig.instance().saveSearchList(pSearchListResult.getSearchList(), false, false);
//        return new SearchListResult(dto, pSearchListResult.getSearchList());
//    }
//
//
//    public boolean deleteSearchList(final CSearchList pSearchList) {
//        return configurationService.get().deleteSearchList(getUserId(), pSearchList);
//    }
    public boolean getAutoGroupingProperty() {
        Boolean value = configurationService.get().getAutoGroupingProperty(getUserId());
        if (value == null) {
            value = true;
        }
        return value;
    }

    public boolean setAutoGroupingProperty(final Boolean pValue) {
        return configurationService.get().setAutoGroupingProperty(getUserId(), pValue);
    }

    public boolean getCaseLocalProperty() {
        Boolean value = configurationService.get().getUserWorkingListLocal(getUserId());
        return value;
    }

    public boolean setCaseLocalProperty(final Boolean pValue) {
        return configurationService.get().setUserWorkingListLocal(getUserId(), pValue);
    }

    public boolean getPinnedSidebarProperty() {
        Boolean value = configurationService.get().getPinnedSidebarProperty(getUserId());
        if (value == null) {
            value = false;
        }
        return value;
    }

    public boolean setPinnedSidebarProperty(final Boolean pValue) {
        return configurationService.get().setPinnedSidebarProperty(getUserId(), pValue);
    }

    /* non-user specific properties */
    public String getCpxHome() {
        return getConfigurationService().get().getCpxHome();
    }

    public String getFontFamily() {
        return getConfigurationService().get().getFont();
    }

    public void setFontFamily(String pFontFamily) {
        getConfigurationService().get().setFont(pFontFamily);
    }

    public String getFontSize() {
        return getConfigurationService().get().getFontSize();
    }

    public void setFontSize(String pFontSize) {
        getConfigurationService().get().setFontSize(pFontSize);
    }

    public String getLanguage() {
        return getConfigurationService().get().getLanguage();
    }

    public void setLanguage(String pLanguage) {
        getConfigurationService().get().setLanguage(pLanguage);
    }

    public void setMenuHoverProperty(boolean pIsHover) {
        setString(getXmlUserConfig(), "user_" + getUserId() + "/menu_hover", String.valueOf(pIsHover));
    }

    public Boolean getMenuHoverProperty() {
        return Boolean.valueOf(getString(getXmlUserConfig(), "user_" + getUserId() + "/menu_hover"));
    }

    public String getServerSocket() {
        return getServerHost() + ":" + String.valueOf(getServerPort());
    }

    public void setExtendToolbar(boolean pExtendToolbar) {
        setString(getXmlUserConfig(), "user_" + getUserId() + "/toolbar_extend", String.valueOf(pExtendToolbar));
    }

    public Boolean getExtendToolbar() {
        return Boolean.valueOf(getString(getXmlUserConfig(), "user_" + getUserId() + "/toolbar_extend"));
    }

//    
//    public ReadOnlyObjectProperty<Boolean> getExtendToolbarProperty() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
    public boolean getWorkflowListAllReminder() {
        return configurationService.get().getWorkflowListAllReminder(getUserId());
    }

    public void setWorkflowListAllReminder(boolean pAllReminder) {
        configurationService.get().setWorkflowListAllReminder(getUserId(), pAllReminder);
    }

    public boolean getUserFilterListDetailsOverview() {
        return configurationService.get().getFilterListDetailsOverview(getUserId());
    }

    public void setUserFilterListDetailsOverview(boolean isShowFilterListDetailsOverview) {
        configurationService.get().setFilterListDetailsOverview(getUserId(), isShowFilterListDetailsOverview);
    }

    public void setWmMainFrameSubject(boolean isWmMainFrameSubject) {
        configurationService.get().setWmMainFrameSubject(getUserId(), isWmMainFrameSubject);
    }

    public void setWmMainFrameState(boolean isWmMainFrameState) {
        configurationService.get().setWmMainFrameState(getUserId(), isWmMainFrameState);

    }

    public void setWmMainFrameWVNumber(boolean isWmMainFrameWVNumber) {
        configurationService.get().setWmMainFrameWVNumber(getUserId(), isWmMainFrameWVNumber);

    }

    public void setWmMainFrameFNLawer(boolean isWmMainFrameFNLawer) {
        configurationService.get().setWmMainFrameFNLawer(getUserId(), isWmMainFrameFNLawer);

    }

    public void setWmMainFrameFNCourt(boolean isWmMainFrameFNCourt) {
        configurationService.get().setWmMainFrameFNCourt(getUserId(), isWmMainFrameFNCourt);

    }

    public void setWmMainFrameWvUser(boolean isWmMainFrameWvUser) {
        configurationService.get().setWmMainFrameWvUser(getUserId(), isWmMainFrameWvUser);

    }

    public void setAlwaysInfoForExaminateQuota(boolean showAlwaysInfoForExaminateQuota) {
        configurationService.get().setAlwaysInfoForExaminateQuota(getUserId(), showAlwaysInfoForExaminateQuota);

    }

    public boolean getWmMainFrameFNCourt() {
        return configurationService.get().getWmMainFrameFNCourt(getUserId());
    }

    public boolean getWmMainFrameFNLawer() {
        return configurationService.get().getWmMainFrameFNLawer(getUserId());
    }

    public boolean getAlwaysInfoForExaminateQuota() {
        return configurationService.get().getAlwaysInfoForExaminateQuota(getUserId());
    }

    public boolean getWmMainFrameState() {
        return configurationService.get().getWmMainFrameState(getUserId());
    }

    public boolean getWmMainFrameSubject() {
        return configurationService.get().getWmMainFrameSubject(getUserId());
    }

    public boolean getWmMainFrameWVNumber() {
        return configurationService.get().getWmMainFrameWVNumber(getUserId());
    }

    public boolean getWmMainFrameWvUser() {
        return configurationService.get().getWmMainFrameWvUser(getUserId());
    }

    public boolean getDocumentImportOfficeEnabled() {
        return configurationService.get().getDocumentImportOfficeEnabled(getUserId());
    }

    public void setDocumentImportOfficeEnabled(boolean pDocumentImportOfficeEnabled) {
        configurationService.get().setDocumentImportOfficeEnabled(getUserId(), pDocumentImportOfficeEnabled);
    }

    public boolean getDocumentImportShowPdfEnabled() {
        return configurationService.get().getDocumentImportShowPdfEnabled(getUserId());
    }

    public void setDocumentImportShowPdfEnabled(boolean pDocumentImportShowPdfEnabled) {
        configurationService.get().setDocumentImportShowPdfEnabled(getUserId(), pDocumentImportShowPdfEnabled);
    }

    public RuleImportCheckFlags getRuleImportCheckFlag() {
        return configurationService.get().getRuleImportCheckFlag(getUserId());
    }

    public void setRuleImportCheckFlag(RuleImportCheckFlags pRuleImportCheckFlag) {
        configurationService.get().setRuleImportCheckFlag(getUserId(), pRuleImportCheckFlag);
    }

    public RuleOverrideFlags getRuleOverrideFlag() {
        return configurationService.get().getRuleOverrideFlag(getUserId());
    }

    public void setRuleOverrideFlag(RuleOverrideFlags pRuleOverrideFlag) {
        configurationService.get().setRuleOverrideFlag(getUserId(), pRuleOverrideFlag);
    }
    

    public boolean getShowRelevantRules() {
        return configurationService.get().getShowRelevantRules();
    }

    public void setShowRelevantRules(final boolean config) {
        configurationService.get().setShowRelevantRules(config);
    }
   
    public boolean getUseGdv(){
        return configurationService.get().getUseGdv();
    }

    public void setUseGdv(final boolean pUseGdv){
        configurationService.get().setUseGdv(pUseGdv);
    }

    public boolean getSendGdvResponce(){
        return getUseGdv() && configurationService.get().getSendGdvResponce();
    }

    public void setSendGdvResponce(final boolean pSendGdvResponce){
        configurationService.get().setSendGdvResponce(pSendGdvResponce);
    }

    public String getGdvResponcePath(){
        return configurationService.get().getGdvResponcePath();
    }

    public void setGdvResponcePath(final String pPath){
        configurationService.get().setGdvResponcePath(pPath);
    }

//    
//    public void setCommonHealthStatusVisualization(final boolean pHealthStatusVisulization) {
//        configurationService.setCommonHealthStatusVisualization(pHealthStatusVisulization);
//    }
    public boolean getCommonHealthStatusVisualization() {
//        return configurationService.get().getCommonHealthStatusVisualization();
            return (Session.instance().getLicense().isAcgModule()) ;
    }
    
    

//    
//    public SearchListDTO getSearchListDTO(SearchListTypeEn pList, Long pId) throws IOException {
//        try {
//            SearchListDTO searchListDto = configurationService.get().getSearchListDTO(getUserId(), pList, pId);
//            return searchListDto;
//        } catch (IOException ex) {
//            LOG.log(Level.SEVERE, "Cannot get search list dto", ex);
//        }
//        return null;
//    }
//
//    
//    public SearchListDTO getSearchListDTO(SearchListTypeEn pList, String pName) throws IOException {
//        try {
//            SearchListDTO searchListDto = configurationService.get().getSearchListDTO(getUserId(), pList, pName);
//            return searchListDto;
//        } catch (IOException ex) {
//            LOG.log(Level.SEVERE, "Cannot get search list dto", ex);
//        }
//        return null;
//    }
//
//    
//    public SearchListResult getSearchListResult(SearchListTypeEn pList, Long pId) throws IOException {
//        try {
//            SearchListResult searchListResult = configurationService.get().getSearchListResult(getUserId(), pList, pId);
//            return searchListResult;
//        } catch (IOException ex) {
//            LOG.log(Level.SEVERE, "Cannot get search list result", ex);
//        }
//        return null;
//    }
//
//    
//    public SearchListResult getSearchListResult(SearchListTypeEn pList, String pName) throws IOException {
//        try {
//            SearchListResult searchListResult = configurationService.get().getSearchListResult(getUserId(), pList, pName);
//            return searchListResult;
//        } catch (IOException ex) {
//            LOG.log(Level.SEVERE, "Cannot get search list result", ex);
//        }
//        return null;
//    }
    public boolean isRuleEditorClient() {
        return configurationService.get().isRuleEditorClient();
    }

    public Date getBatchSettingsAdmissionDateFrom() {
        return Lang.fromIsoDate(getString(getXmlUserConfig(), "user_" + getUserId() + "/selected_grouper/admissiondatefrom"));
    }

    public Date getBatchSettingsAdmissionDateUntil() {
        return Lang.fromIsoDate(getString(getXmlUserConfig(), "user_" + getUserId() + "/selected_grouper/admissiondateuntil"));
    }

    public Date getBatchSettingsDischargeDateFrom() {
        return Lang.fromIsoDate(getString(getXmlUserConfig(), "user_" + getUserId() + "/selected_grouper/dischargedatefrom"));
    }

    public Date getBatchSettingsDischargeDateUntil() {
        return Lang.fromIsoDate(getString(getXmlUserConfig(), "user_" + getUserId() + "/selected_grouper/dischargedateuntil"));
    }

    public void setBatchSettingsAdmissionDateFrom(Date pDate) {
        setString(getXmlUserConfig(), "user_" + getUserId() + "/selected_grouper/admissiondatefrom", Lang.toIsoDate(pDate));
    }

    public void setBatchSettingsAdmissionDateUntil(Date pDate) {
        setString(getXmlUserConfig(), "user_" + getUserId() + "/selected_grouper/admissiondateuntil", Lang.toIsoDate(pDate));
    }

    public void setBatchSettingsDischargeDateFrom(Date pDate) {
        setString(getXmlUserConfig(), "user_" + getUserId() + "/selected_grouper/dischargedatefrom", Lang.toIsoDate(pDate));
    }

    public void setBatchSettingsDischargeDateUntil(Date pDate) {
        setString(getXmlUserConfig(), "user_" + getUserId() + "/selected_grouper/dischargedateuntil", Lang.toIsoDate(pDate));
    }

}
