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

import de.lb.cpx.connector.EjbConnector;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.cpx.license.crypter.License;
import de.lb.cpx.model.enums.SearchListTypeEn;
import de.lb.cpx.server.commonDB.model.CdbUsers;
import de.lb.cpx.server.commons.enums.RuleImportCheckFlags;
import de.lb.cpx.server.commons.enums.RuleOverrideFlags;
import de.lb.cpx.service.ejb.AuthServiceEJBRemote;
import de.lb.cpx.service.information.ConnectionString;
import de.lb.cpx.service.information.CpxDatabase;
import de.lb.cpx.service.information.DatabaseInfo;
import de.lb.cpx.service.properties.RoleProperties;
import de.lb.cpx.shared.dto.HistoryFilter;
import de.lb.cpx.shared.dto.RuleFilterDTO;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Dirk Niemeier
 */
public class Session {

    private static Session instance = null;
    private static final Logger LOG = Logger.getLogger(Session.class.getName());

    //public AuthServiceEJBRemote authServiceBean;
    private final Date createdAt = new Date();
    private final Map<Long, String> cpxRoleNames = new HashMap<>();
    private long cpxActualRoleId = 0L;
    private String cpxUserName = "";
    private long cpxUserId = 0L;
    private final Map<SearchListTypeEn, Long> selectedSearchList = new HashMap<>();
    private String cpxDatabase = "";
    public final StringProperty cpxLocale = new SimpleStringProperty("");
    //public CpxLanguage cpxLanguage = null;
    private boolean autoGrouping = true;
    private Boolean caseLocal = null;
    private Boolean allReminders = null;
    private Boolean isShowFilterListDetailsOverview = null;
    private Boolean documentImportOfficeEnabled = null;
    private Boolean documentImportShowPdfEnabled = null;
    private RuleImportCheckFlags ruleImportCheckFlag = null;
    private RuleOverrideFlags ruleOverrideFlag = null;
    private Boolean showHistoryEventDetails = null;
    private Boolean showHistoryDeleted = null;
    private EjbConnector ejbConnector = null;
    private RoleProperties roleProperties = null;
    public final SimpleIntegerProperty caseCount = new SimpleIntegerProperty(-1);
    public final SimpleIntegerProperty processCount = new SimpleIntegerProperty(-1);
    public final SimpleIntegerProperty canceledCaseCount = new SimpleIntegerProperty(-1);
    public final SimpleIntegerProperty canceledProcessCount = new SimpleIntegerProperty(-1);
    public final SimpleIntegerProperty patientCount = new SimpleIntegerProperty(-1);
    private DatabaseInfo databaseInfo = null;
    private DatabaseInfo databaseInfoCommon = null;
    private CdbUsers cdbUser = null;
    private HistoryFilter historyFilter = null;
    private RuleFilterDTO ruleFilterCaseList = null;
    private RuleFilterDTO ruleFilterBatchAdm = null;
    private License license = null;
    private Integer searchListFetchSize = null;
    private String recentClientScene = "";
    private Long recentMdkInternalId = null;
    private long[] selectedDraftCategories = null;
    private Boolean isWmMainFrameSubject = null;
    private Boolean isWmMainFrameState = null;
    private Boolean isWmMainFrameWVNumber = null;
    private Boolean isWmMainFrameFNLawer = null;
    private Boolean isWmMainFrameFNCourt = null;
    private Boolean isWmMainFrameWvUser = null;
    private Integer documentMaxSize = null;
    private Boolean showAlwaysInfoForExaminateQuota = null;

    private Session() {
    }

    /**
     * Gets the current session instance (always creates a new session if not
     * exists)
     *
     * @return session
     */
    public static Session instance() {
        return instance(true);
    }

    /**
     * Gets the current session instance
     *
     * @param bAutoCreateNewIfNull creates a new session if you want
     * @return session
     */
    public static Session instance(boolean bAutoCreateNewIfNull) {
        if (bAutoCreateNewIfNull) {
            if (instance == null) {
                create();
            }
        }
        return instance;
    }

    /**
     * Creates a new session
     */
    private static synchronized void create() {
        //destroy();
        instance = new Session();
    }

    /**
     * Destroys session
     */
    public static synchronized void destroy() {
        LOG.log(Level.FINE, "Destroy session...");
        closeContexts();
        instance = null;
        LOG.log(Level.FINE, "Session destroyed!");
    }

    /**
     * Closes JNDI contexts
     */
    public static synchronized void closeContexts() {
        Session session = instance(false);
        if (session != null) {
            if (session.ejbConnector != null) {
                session.ejbConnector.closeContexts();
            }
        }
    }

    /**
     * @return Client ID (JNDI)
     */
    public String getClientId() {
        return getEjbConnector().getClientId();
    }

    /**
     * Sets EJB Connector
     *
     * @param pEjbConnector new ejb connector
     * @return old ejb connector
     */
    public EjbConnector setEjbConnector(final EjbConnector pEjbConnector) {
        EjbConnector old = ejbConnector;
        ejbConnector = pEjbConnector;
        return old;
    }

    /**
     * Gets EJB Connector (creates a new one if not exists)
     *
     * @return (new) ejb connector
     */
    public synchronized EjbConnector getEjbConnector() {
        if (ejbConnector == null) {
            ejbConnector = new EjbConnector();
        }
        return ejbConnector;
    }

    //can be removed/replaced!
    public String getFontFamily() {
        return CpxClientConfig.instance().getUserFontFamily();
    }

    //can be removed/replaced!
    public String getFontSize() {
        return CpxClientConfig.instance().getUserFontSize();
    }

    //can be removed/replaced!
    public void setFontFamily(final String pFontFamily) {
        CpxClientConfig.instance().setUserFontFamily(pFontFamily);
    }

    //can be removed/replaced!
    public void setFontSize(final String pFontSize) {
        CpxClientConfig.instance().setUserFontSize(pFontSize);
    }

    public void setHistoryFilter(final HistoryFilter pHistoryFilter) {
        historyFilter = new HistoryFilter(pHistoryFilter); //creates a copy
        CpxClientConfig.instance().setUserHistoryFilter(pHistoryFilter);
    }

    public HistoryFilter getHistoryFilter() {
        if (historyFilter == null) {
            historyFilter = CpxClientConfig.instance().getUserHistoryFilter();
        }
        return historyFilter;
    }

    public void setRuleFilterCaseListForUser(final RuleFilterDTO pRuleFilterDTO) {
        ruleFilterCaseList = new RuleFilterDTO(pRuleFilterDTO);  // copy constructor
        CpxClientConfig.instance().setRuleFilterCaseListForUser(pRuleFilterDTO);
    }

    public RuleFilterDTO getRuleFilterCaseListForUser() {
        if (ruleFilterCaseList == null) {
            ruleFilterCaseList = CpxClientConfig.instance().getRuleFilterCaseListForUser();
        }
        return ruleFilterCaseList;
    }

    public void setRuleFilterBatchAdmForUser(final RuleFilterDTO pRuleFilterDTO) {
        ruleFilterBatchAdm = new RuleFilterDTO(pRuleFilterDTO);  // copy constructor
        CpxClientConfig.instance().setRuleFilterBatchAdmForUser(pRuleFilterDTO);
    }

    public RuleFilterDTO getRuleFilterBatchAdmForUser() {
        if (ruleFilterBatchAdm == null) {
            ruleFilterBatchAdm = CpxClientConfig.instance().getRuleFilterBatchAdmForUser();
        }
        return ruleFilterBatchAdm;
    }

    public void setSelectedDraftCategories(final long[] pSelectedDraftCategories) {
        if (pSelectedDraftCategories == null || pSelectedDraftCategories.length == 0) {
            this.selectedDraftCategories = new long[0];
        } else {
            this.selectedDraftCategories = new long[pSelectedDraftCategories.length];
            System.arraycopy(pSelectedDraftCategories, 0, this.selectedDraftCategories, 0, pSelectedDraftCategories.length);
        }
        //selectedDraftCategories = pSelectedDraftCategories == null ? new long[0] : pSelectedDraftCategories;
        CpxClientConfig.instance().setUserSelectedDraftCategories(pSelectedDraftCategories);
    }

    public long[] getSelectedDraftCategories() {
        if (selectedDraftCategories == null) {
            selectedDraftCategories = CpxClientConfig.instance().getUserSelectedDraftCategories();
        }
        long[] tmp = new long[selectedDraftCategories.length];
        System.arraycopy(selectedDraftCategories, 0, tmp, 0, selectedDraftCategories.length);
        return tmp;
    }

    public void setShowHistoryEventDetails(final boolean pShowHistoryEventDetails) {
        showHistoryEventDetails = pShowHistoryEventDetails;
        CpxClientConfig.instance().setUserShowHistoryEventDetails(pShowHistoryEventDetails);
    }

    public Boolean isShowHistoryEventDetails() {
        if (showHistoryEventDetails == null) {
            showHistoryEventDetails = CpxClientConfig.instance().getUserShowHistoryEventDetails();
        }
        return showHistoryEventDetails;
    }

    public void setShowHistoryDeleted(final boolean pShowHistoryDeleted) {
        showHistoryDeleted = pShowHistoryDeleted;
        CpxClientConfig.instance().setUserShowHistoryDeleted(pShowHistoryDeleted);
    }

    public Boolean isShowHistoryDeleted() {
        if (showHistoryDeleted == null) {
            showHistoryDeleted = CpxClientConfig.instance().getUserShowHistoryDeleted();
        }
        return showHistoryDeleted;
    }

    public void setSearchListFetchSize(final int pSearchListFetchSize) {
        if (CpxClientConfig.instance().setSearchListFetchSize(pSearchListFetchSize)) {
            searchListFetchSize = pSearchListFetchSize;
        }
    }

    public int getSearchListFetchSize() {
        if (searchListFetchSize == null) {
            searchListFetchSize = CpxClientConfig.instance().getSearchListFetchSize();
        }
        return searchListFetchSize;
    }

    /**
     * runs through all roles and returns key of actualRole String Todo: There
     * should be a better way ...
     *
     * @return actual RoleId For actualRole String, returns null if no key was
     * found
     */
    /*
  public Long getActualRoleId(){
      Iterator<Long> it = getCpxRoleNames().keySet().iterator();
      while(it.hasNext()){
          Long key = it.next();
          if(getCpxRoleNames().get(key).equals(getCpxActualRoleId())){
              return key;
          }
      }
      return null;
    }
     */
    /**
     * @return the cpxRoleNames
     */
    /*
  public Map<Long, String> getCpxRoleNames() {
    return cpxRoleNames;
  }
     */
    /**
     * @param pCpxRoleNames the Role Names to set
     */
    /*
  public void setCpxRoleNames(final Map<Long, String> pCpxRoleNames) {
    this.cpxRoleNames = pCpxRoleNames;
  }
     */
    /**
     * @return the cpxActualRole
     */
    public long getCpxActualRoleId() {
        return cpxActualRoleId;
    }

    /**
     * @param pRoleId the Actual Role Name to set
     */
    public void setCpxActualRoleId(final Long pRoleId) {
        this.cpxActualRoleId = (pRoleId == null) ? 0L : pRoleId;
    }

    /**
     * @return the cpxUserName
     */
    public String getCpxUserName() {
        return cpxUserName;
    }

    /**
     * @param pUserName the User Name to set
     */
    public void setCpxUserName(final String pUserName) {
        this.cpxUserName = (pUserName == null) ? "" : pUserName.trim();
    }

    /**
     * @return the cpxUserId
     */
    public long getCpxUserId() {
        return cpxUserId;
    }

    /**
     * @param pCpxUserId the User ID to set
     */
    public void setCpxUserId(final Long pCpxUserId) {
        this.cpxUserId = (pCpxUserId == null) ? 0L : pCpxUserId;
    }

    /**
     * @param pList list of options
     * @return the selectedWorkingList
     */
    public Long getSelectedSearchList(final SearchListTypeEn pList) {
        Long tmp = selectedSearchList.get(pList);
        return tmp;
    }

    /**
     * @param pList list of options
     * @param pSelectedSearchListId the Selected Working List to set
     */
    public void setSelectedSearchList(final SearchListTypeEn pList, final Long pSelectedSearchListId) {
        //String tmp = (pSelectedWorkingList == null) ? "" : pSelectedWorkingList.trim();
        selectedSearchList.put(pList, pSelectedSearchListId);
    }

    public DatabaseInfo getCpxDatabaseInfo() {
        return getCpxDatabaseInfo(false);
    }

    /**
     * @param pForceReload get recent count from server?
     * @return the databaseInfo
     */
    public DatabaseInfo getCpxDatabaseInfo(final boolean pForceReload) {
//        if (!MainApp.getNeedsDatabase() || !getEjbConnector().isJndiUserContextInitialized()) {
//            return null;
//        }
        if (getCpxDatabase().isEmpty()) {
            return null;
        }
        if (pForceReload || databaseInfo == null) {
//            try{
            setDatabaseInfo(getEjbConnector().connectWorkingListBean().get().getDatabaseInfo());
//            }catch(javax.ejb.EJBException ex){
//                LOG.warning("Request of DatabaseInfo failed, reason:\n"+ex.getMessage());
//                return databaseInfo;
//            }
        }
        return databaseInfo;
    }

    public DatabaseInfo getCpxDatabaseInfoCommon() {
//        if (!getEjbConnector().isJndiContextInitialized()) {
//            return null;
//        }
        return getCpxDatabaseInfoCommon(false);
    }

    /**
     * @param pForceReload get recent count from server?
     * @return the databaseInfoCommon
     */
    public DatabaseInfo getCpxDatabaseInfoCommon(final boolean pForceReload) {
        if (pForceReload || databaseInfoCommon == null) {
//            try{
            setDatabaseInfoCommon(getEjbConnector().connectAuthServiceBean().get().getDatabaseInfoCommon());
//            }catch(javax.ejb.EJBException ex){
//                LOG.warning("Request of DatabaseInfo failed, reason:\n"+ex.getMessage());
//                return databaseInfo;
//            }
        }
        return databaseInfoCommon;
    }

    /**
     * @param pDatabaseInfo database info
     */
    public void setDatabaseInfo(final DatabaseInfo pDatabaseInfo) {
        this.databaseInfo = pDatabaseInfo;
    }

    /**
     * @param pDatabaseInfoCommon database info
     */
    public void setDatabaseInfoCommon(final DatabaseInfo pDatabaseInfoCommon) {
        this.databaseInfoCommon = pDatabaseInfoCommon;
    }

    /**
     * @return the cpxDatabase
     */
    public String getCpxDatabase() {
        return cpxDatabase;
    }

    public ConnectionString getConnectionString() {
        return new ConnectionString(cpxDatabase);
    }

    public String getCpxPersistenceUnit() {
        return getConnectionString().getPersistenceUnit();
    }

    /**
     * @param pCpxDatabase the Database to set
     */
    public void setCpxDatabase(final String pCpxDatabase) {
        this.cpxDatabase = (pCpxDatabase == null) ? "" : pCpxDatabase.trim();
        databaseInfo = null;
    }

    /**
     * @return the recentClientScene
     */
    public String getRecentClientScene() {
        if (recentClientScene == null || recentClientScene.trim().isEmpty()) {
            recentClientScene = CpxClientConfig.instance().getUserRecentClientScene();
        }
        return recentClientScene;
    }

    /**
     * @param pRecentClientScene the recent client scene to set
     */
    public void setRecentClientScene(final String pRecentClientScene) {
        this.recentClientScene = (pRecentClientScene == null) ? "" : pRecentClientScene.trim();
        CpxClientConfig.instance().setUserRecentClientScene(pRecentClientScene);
    }

    /**
     * @return tab selected in ruleeditor ruleList
     */
    public String getRecentRuleListTab() {
        if (recentClientScene == null || recentClientScene.trim().isEmpty()) {
            recentClientScene = CpxClientConfig.instance().getRecentRuleListTab();
        }
        return recentClientScene;
    }

    /**
     * @param pRecentRuleListTab current selected tab in ruleeditor ruleList
     */
    public void setRecentRuleListTab(final String pRecentRuleListTab) {
        this.recentClientScene = (pRecentRuleListTab == null) ? "" : pRecentRuleListTab.trim();
        CpxClientConfig.instance().setRecentRuleListTab(pRecentRuleListTab);
    }

    /**
     * @return the recentMdk
     */
    public Long getRecentMdkInternalId() {
        if (recentMdkInternalId == null) {
            recentMdkInternalId = CpxClientConfig.instance().getUserRecentMdkInternalId();
        }
        return recentMdkInternalId;
    }

    /**
     * @param pRecentMdkInternalId the recent used internal MDK id
     */
    public void setRecentMdkInternalId(final Long pRecentMdkInternalId) {
        this.recentMdkInternalId = pRecentMdkInternalId;
        CpxClientConfig.instance().setUserRecentMdkInternalId(pRecentMdkInternalId);
    }

    /**
     * @return the cpxLocale
     */
    public String getCpxLocale() {
        return cpxLocale.get();
    }

    /**
     * @param pLocale the Locale to set
     */
    public void setCpxLocale(final String pLocale) {
        this.cpxLocale.set((pLocale == null) ? "" : pLocale.trim().toLowerCase());
    }

    /**
     * @return the autoGrouping
     */
    public boolean isAutoGrouping() {
        return autoGrouping;
    }

    /**
     * @param pAutoGrouping the Auto Grouping to set
     */
    public void setAutoGrouping(final boolean pAutoGrouping) {
        this.autoGrouping = pAutoGrouping;
    }

    /**
     * @return the caseLocal
     */
    public boolean isCaseLocal() {
        if (caseLocal == null) {
            caseLocal = CpxClientConfig.instance().getCaseLocalProperty();
        }
        return caseLocal;
    }

    /**
     * @param pCaseLocal the Case Local to set
     */
    public void setCaseLocal(final boolean pCaseLocal) {
        this.caseLocal = pCaseLocal;
        CpxClientConfig.instance().setCaseLocalProperty(pCaseLocal);
    }

    public void setShowAllRemindersConfig(boolean pAllReminders) {
        this.allReminders = pAllReminders;
        CpxClientConfig.instance().setWorkflowListAllReminder(pAllReminders);
    }

    /**
     * @return the allReminders
     */
    public boolean isShowAllRemindersConfig() {
        if (allReminders == null) {
            allReminders = CpxClientConfig.instance().getWorkflowListAllReminder();
        }
        return allReminders;
    }

    public void setFilterListDetailsOverview(boolean isShowFilterListDetailsOverview) {
        this.isShowFilterListDetailsOverview = isShowFilterListDetailsOverview;
        CpxClientConfig.instance().setUserFilterListDetailsOverview(isShowFilterListDetailsOverview);
    }

    /**
     * @return the isShowFilterListDetailsOverview
     */
    public boolean isShowFilterListDetailsOverview() {
        if (isShowFilterListDetailsOverview == null) {
            isShowFilterListDetailsOverview = CpxClientConfig.instance().getUserFilterListDetailsOverview();
        }
        return isShowFilterListDetailsOverview;
    }

    public boolean isWmMainFrameFNCourt() {
        if (isWmMainFrameFNCourt == null) {
            isWmMainFrameFNCourt = CpxClientConfig.instance().getWmMainFrameFNCourt();
        }
        return isWmMainFrameFNCourt;
    }

    public boolean isWmMainFrameFNLawer() {
        if (isWmMainFrameFNLawer == null) {
            isWmMainFrameFNLawer = CpxClientConfig.instance().getWmMainFrameFNLawer();
        }
        return isWmMainFrameFNLawer;
    }

    public boolean showAlwaysInfoForExaminateQuota() {
        if (showAlwaysInfoForExaminateQuota == null) {
            showAlwaysInfoForExaminateQuota = CpxClientConfig.instance().getAlwaysInfoForExaminateQuota();
        }
        return showAlwaysInfoForExaminateQuota;
    }

    public boolean isWmMainFrameState() {
        if (isWmMainFrameState == null) {
            isWmMainFrameState = CpxClientConfig.instance().getWmMainFrameState();
        }
        return isWmMainFrameState;
    }

    public boolean isWmMainFrameSubject() {
        if (isWmMainFrameSubject == null) {
            isWmMainFrameSubject = CpxClientConfig.instance().getWmMainFrameSubject();
        }
        return isWmMainFrameSubject;
    }

    public boolean isWmMainFrameWVNumber() {
        if (isWmMainFrameWVNumber == null) {
            isWmMainFrameWVNumber = CpxClientConfig.instance().getWmMainFrameWVNumber();
        }
        return isWmMainFrameWVNumber;
    }

    public boolean isWmMainFrameWvUser() {
        if (isWmMainFrameWvUser == null) {
            isWmMainFrameWvUser = CpxClientConfig.instance().getWmMainFrameWvUser();
        }
        return isWmMainFrameWvUser;
    }

    public void setDocumentImportOfficeEnabled(boolean pDocumentImportOfficeEnabled) {
        this.documentImportOfficeEnabled = pDocumentImportOfficeEnabled;
        CpxClientConfig.instance().setDocumentImportOfficeEnabled(pDocumentImportOfficeEnabled);
    }

    /**
     * @return the documentImportOfficeEnabled
     */
    public boolean isDocumentImportDetection() {
        if (documentImportOfficeEnabled == null) {
            documentImportOfficeEnabled = CpxClientConfig.instance().getDocumentImportOfficeEnabled();
        }
        return documentImportOfficeEnabled;
    }

    public void setDocumentImportShowOfficeEnabled(boolean pDocumentImportShowPdfEnabled) {
        this.documentImportShowPdfEnabled = pDocumentImportShowPdfEnabled;
        CpxClientConfig.instance().setDocumentImportShowPdfEnabled(pDocumentImportShowPdfEnabled);
    }

    /**
     * @return the documentImportShowPdfEnabled
     */
    public boolean isDocumentImportShowPdf() {
        if (documentImportShowPdfEnabled == null) {
            documentImportShowPdfEnabled = CpxClientConfig.instance().getDocumentImportShowPdfEnabled();
        }
        return documentImportShowPdfEnabled;
    }

    public void setRuleOverrideFlag(RuleOverrideFlags pRuleOverrideFlag) {
        this.ruleOverrideFlag = pRuleOverrideFlag;
        CpxClientConfig.instance().setRuleOverrideFlag(pRuleOverrideFlag);
    }

    /**
     * @return the ruleOverrideFlag
     */
    public RuleOverrideFlags getRuleOverrideFlag() {
        if (ruleOverrideFlag == null) {
            ruleOverrideFlag = CpxClientConfig.instance().getRuleOverrideFlag();
        }
        return ruleOverrideFlag;
    }

    public void setRuleImportCheckFlag(RuleImportCheckFlags pRuleImportCheckFlag) {
        this.ruleImportCheckFlag = pRuleImportCheckFlag;
        CpxClientConfig.instance().setRuleImportCheckFlag(pRuleImportCheckFlag);
    }

    /**
     * @return the ruleImportCheckFlag
     */
    public RuleImportCheckFlags getRuleImportCheckFlag() {
        if (ruleImportCheckFlag == null) {
            ruleImportCheckFlag = CpxClientConfig.instance().getRuleImportCheckFlag();
        }
        return ruleImportCheckFlag;
    }

    /**
     * @return Date/Time when session was started
     */
    public Date getCreatedAt() {
        return new Date(createdAt.getTime());
    }

    public void setRoleProperties(final RoleProperties pRoleProperties) {
        this.roleProperties = pRoleProperties;
    }

    public RoleProperties getRoleProperties() {
        return roleProperties;
    }

    public boolean isBatchgroupingAllowed() {
        return roleProperties.isBatchgroupingAllowed();

    }
    
    public boolean canSelectRules(){
        return roleProperties.canSetRelevanceFlag(); 
    }

    public boolean isFallzusammenführungAllowed() {
        return roleProperties.isCaseMergingAllowed();
    }

    public boolean isExportDataAllowed() {
        return roleProperties.isExportDataAllowed();
    }

    public boolean isEditActionAllowed() {
        return roleProperties.isEditActionAllowed();
    }

    /**
     * @return the caseCount
     */
    public Integer getCaseCount() {
        return getCaseCount(false);
    }

    /**
     * @param pForceReload get recent count from server?
     * @return the caseCount
     */
    public Integer getCaseCount(final boolean pForceReload) {
        if (caseCount.get() == -1 || pForceReload) {
            setCaseCount(getEjbConnector().connectWorkingListBean().get().getMaxCount());
        }
        return caseCount.get();
    }

    /**
     * @param pCaseCount case count to set
     */
    public void setCaseCount(final int pCaseCount) {
        this.caseCount.set(pCaseCount);
    }

    /**
     * @return the processCount
     */
    public Integer getProcessCount() {
        return getProcessCount(false);
    }

    /**
     * @param pForceReload get recent count from server?
     * @return the processCount
     */
    public Integer getProcessCount(final boolean pForceReload) {
        if (processCount.get() == -1 || pForceReload) {
            setProcessCount(getEjbConnector().connectWorkflowListBean().get().getMaxCount());
        }
        return processCount.get();
    }

    /**
     * @param pProcessCount process count to set
     */
    public void setProcessCount(final int pProcessCount) {
        this.processCount.set(pProcessCount);
    }

    /**
     * @return the patientCount
     */
    public Integer getPatientCount() {
        return getPatientCount(false);
    }

    /**
     * @param pForceReload get recent count from server?
     * @return the patientCount
     */
    public Integer getPatientCount(final boolean pForceReload) {
        if (patientCount.get() == -1 || pForceReload) {
            setPatientCount(getEjbConnector().connectWorkingListBean().get().getMaxPatientCount());
        }
        return patientCount.get();
    }

    /**
     * @param pPatientCount patient count to set
     */
    public void setPatientCount(final Integer pPatientCount) {
        this.patientCount.set(pPatientCount);
    }

    /**
     * @return the canceledProcessCount
     */
    public Integer getCanceledCaseCount() {
        return getCanceledCaseCount(false);
    }

    /**
     * @param pForceReload get recent count from server?
     * @return the canceledCaseCount
     */
    public Integer getCanceledCaseCount(final boolean pForceReload) {
        if (canceledCaseCount.get() == -1 || pForceReload) {
            setCanceledCaseCount(getEjbConnector().connectWorkingListBean().get().getCanceledCount());
        }
        return canceledCaseCount.get();
    }

    /**
     * @param pCanceledCaseCount canceledCase count to set
     */
    public void setCanceledCaseCount(final Integer pCanceledCaseCount) {
        this.canceledCaseCount.set(pCanceledCaseCount);
    }

    /**
     * @return the canceledProcessCount
     */
    public Integer getCanceledProcessCount() {
        return getCanceledProcessCount(false);
    }

    /**
     * @param pForceReload get recent count from server?
     * @return the canceledProcessCount
     */
    public Integer getCanceledProcessCount(final boolean pForceReload) {
        if (canceledProcessCount.get() == -1 || pForceReload) {
            setCanceledProcessCount(getEjbConnector().connectWorkflowListBean().get().getCanceledCount());
        }
        return canceledProcessCount.get();
    }

    /**
     * @param pCanceledProcessCount canceledProcess count to set
     */
    public void setCanceledProcessCount(final Integer pCanceledProcessCount) {
        this.canceledProcessCount.set(pCanceledProcessCount);
    }

    public void resetCaseCount() {
        caseCount.set(-1);
    }

    public void resetProcessCount() {
        processCount.set(-1);
    }

    public void resetPatientCount() {
        patientCount.set(-1);
    }

    public void resetCanceledCaseCount() {
        canceledCaseCount.set(-1);
    }

    public void resetCanceledProcessCount() {
        canceledProcessCount.set(-1);
    }

    /**
     * @return the user
     */
    public CdbUsers getCdbUser() {
        return cdbUser;
    }

    /**
     * @param pCdbUser the user
     */
    public void setCdbUser(final CdbUsers pCdbUser) {
        this.cdbUser = (pCdbUser == null) ? new CdbUsers() : pCdbUser;
    }

    @Override
    public String toString() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder roleNames = new StringBuilder();
        Iterator<Map.Entry<Long, String>> it = cpxRoleNames.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Long, String> entry = it.next();
            if (roleNames.length() > 0) {
                roleNames.append(", ");
            }
            roleNames.append(entry.getKey()).append("=").append(entry.getValue());
        }
        String o = "CPX Client Session:"
                + "\nCreated or started at: " + df.format(createdAt)
                + "\nRole names: " + roleNames.toString()
                + "\nActual role ID: " + cpxActualRoleId
                + "\nUser name: " + cpxUserName + " (ID " + cpxUserId + ")"
                + "\nSelected search lists: " + selectedSearchList
                + "\nDatabase: " + cpxDatabase
                + "\nLocale: " + cpxLocale.getValue()
                + "\nAutogrouping: " + (autoGrouping ? "activated" : "deactivated")
                + "\nUse local or external case details in lists: " + (caseLocal == null ? "not initialized yet" : caseLocal ? "local" : "extern")
                + "\nShow all reminders: " + (allReminders == null ? "not initialized yet" : allReminders ? "yes" : "no")
                + "\nShow Filterlist's details Overview: " + (isShowFilterListDetailsOverview == null ? "not initialized yet" : isShowFilterListDetailsOverview ? "yes" : "no")
                + "\nShow history event details: " + (showHistoryEventDetails == null ? "not initialized yet" : showHistoryEventDetails ? "yes" : "no")
                + "\nHistory filter: " + (historyFilter == null ? "not initialized yet" : historyFilter.hasValues() ? "is set" : "is not set")
                + "\nRule filter (Case List): " + (ruleFilterCaseList == null ? "not initialized yet" : ruleFilterCaseList.filterHasValues() ? "is set" : "is not set")
                + "\nRule filter (Batch Administration): " + (ruleFilterBatchAdm == null ? "not initialized yet" : ruleFilterBatchAdm.filterHasValues() ? "is set" : "is not set")
                + "\nPatient count: " + (patientCount)
                + "\nCase count: " + (caseCount)
                + "\nProcess count: " + (processCount)
                + "\nEJB Connector: " + (ejbConnector == null ? "not initialized yet" : "initialized")
                + "\nCdbUser: " + (cdbUser == null ? "not initialized yet" : "initialized")
                + "\nCPX Rights: " + (roleProperties == null ? "not initialized yet" : "initialized")
                + "\nLicense: " + (license == null ? "not initialized yet" : "initialized")
                + (roleProperties == null ? "" : "\n" + roleProperties.toString());
        return o;
    }

    protected void setLicense(License pLicense) {
        license = pLicense;
    }

    public License getLicense() {
        if (license == null) {
            EjbProxy<AuthServiceEJBRemote> authServiceBean = Session.instance().getEjbConnector().connectAuthServiceBean();
            if (authServiceBean.get() == null) {
                return null;
            }
            setLicense(authServiceBean.get().getLicense());
        }
        return license;
    }

    public  List<CpxDatabase> getAllowedCpxDatabases() {
        return getAllowedCpxDatabases4RoleId(cpxActualRoleId);
    }
    
    public List<CpxDatabase> getAllowedCpxDatabases4RoleId(Long pRoleId) {
        EjbProxy<AuthServiceEJBRemote> authServiceBean = Session.instance().getEjbConnector().connectAuthServiceBean();
        if (authServiceBean.get() == null) {
            return null;
        }
        List<CpxDatabase> databases = authServiceBean.get().getCpxDatabases();
        List<CpxDatabase> filterResult = new ArrayList<>();
        setRoleProperties(Session.instance().getEjbConnector().connectLoginServiceBean().get().getRoleProperties(pRoleId));
        databases.stream().filter((CpxDatabase database) -> getRoleProperties().isDatabaseAllowed(database.getConnectionString())).forEachOrdered((database) -> {
            filterResult.add(database);
        });
        Collections.sort(filterResult);
        return filterResult;
    }

    public void setWmMainFrameSubject(boolean isWmMainFrameSubject) {
        this.isWmMainFrameSubject = isWmMainFrameSubject;
        CpxClientConfig.instance().setWmMainFrameSubject(isWmMainFrameSubject);
    }

    public void setWmMainFrameState(boolean isWmMainFrameState) {
        this.isWmMainFrameState = isWmMainFrameState;
        CpxClientConfig.instance().setWmMainFrameState(isWmMainFrameState);
    }

    public void setWmMainFrameWVNumber(boolean isWmMainFrameWVNumber) {
        this.isWmMainFrameWVNumber = isWmMainFrameWVNumber;
        CpxClientConfig.instance().setWmMainFrameWVNumber(isWmMainFrameWVNumber);
    }

    public void setWmMainFrameFNLawer(boolean isWmMainFrameFNLawer) {
        this.isWmMainFrameFNLawer = isWmMainFrameFNLawer;
        CpxClientConfig.instance().setWmMainFrameFNLawer(isWmMainFrameFNLawer);
    }

    public void setWmMainFrameFNCourt(boolean isWmMainFrameFNCourt) {
        this.isWmMainFrameFNCourt = isWmMainFrameFNCourt;
        CpxClientConfig.instance().setWmMainFrameFNCourt(isWmMainFrameFNCourt);
    }

    public void setWmMainFrameWvUser(boolean isWmMainFrameWvUser) {
        this.isWmMainFrameWvUser = isWmMainFrameWvUser;
        CpxClientConfig.instance().setWmMainFrameWvUser(isWmMainFrameWvUser);
    }

    public void setAlwaysInfoForExaminateQuota(boolean showAlwaysInfoForExaminateQuota) {
        this.showAlwaysInfoForExaminateQuota = showAlwaysInfoForExaminateQuota;
        CpxClientConfig.instance().setAlwaysInfoForExaminateQuota(showAlwaysInfoForExaminateQuota);
    }

    public int getDocumentsSizeMax() {
        return getDocumentsSizeMax(false);
    }

    public int getDocumentsSizeMax(final boolean pForceReload) {
        if (documentMaxSize == null || pForceReload) {
            this.documentMaxSize = getEjbConnector().connectProcessServiceBean().get().getDocumentsSizeMax();
        }
        return documentMaxSize;
    }

//    public void setDocumentMaxSize(int pDocumentMaxSize) {
//        this.documentMaxSize = pDocumentMaxSize;
//        CpxClientConfig.instance().setWmMainFrameWvUser(pDocumentMaxSize);
//    }
}
