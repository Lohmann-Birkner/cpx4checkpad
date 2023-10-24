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

import de.lb.cpx.server.commonDB.dao.CdbUsersDao;
import de.lb.cpx.server.commonDB.model.CdbUsers;
import de.lb.cpx.server.commons.enums.RuleImportCheckFlags;
import de.lb.cpx.server.commons.enums.RuleOverrideFlags;
import de.lb.cpx.server.config.CpxServerConfigLocal;
import de.lb.cpx.service.properties.UserProperties;
import de.lb.cpx.shared.dto.HistoryFilter;
import de.lb.cpx.shared.dto.RuleFilterDTO;
import de.lb.cpx.shared.dto.job.config.other.SapJob;
import de.lb.cpx.shared.p21util.P21ExportSettings;
import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.inject.Inject;
import org.jboss.ejb3.annotation.SecurityDomain;

/**
 *
 * @author Dirk Niemeier
 */
@Singleton
@SecurityDomain("cpx")
public class ConfigurationServiceEJB implements ConfigurationServiceEJBRemote {
    
    @EJB(name = "CpxServerConfig")
    private CpxServerConfigLocal cpxServerConfig;
    
    @Inject
    private CdbUsersDao cdbUsersDao;
    
    @Override
    public String getCpxHome() {
        return CpxSystemProperties.getInstance().getCpxHome();
    }
    
    @Override
    public String getFontSize() {
        return cpxServerConfig.getFontSize();
    }
    
    @Override
    public void setFontSize(final String pFontSize) {
        cpxServerConfig.setFontSize(pFontSize);
    }
    
    @Override
    public String getFont() {
        return cpxServerConfig.getFontFamily();
    }
    
    @Override
    public void setFont(final String pFont) {
        cpxServerConfig.setFontFamily(pFont);
    }
    
    @Override
    public String getLanguage() {
        return cpxServerConfig.getLanguage();
    }
    
    @Override
    public void setLanguage(final String pLanguage) {
        cpxServerConfig.setLanguage(pLanguage);
    }
    
    @Override
    public Integer getSearchListFetchSize() {
        return cpxServerConfig.getSearchListFetchSize();
    }
    
    @Override
    public void setSearchListFetchSize(final Integer pSearchListFetchSize) {
        cpxServerConfig.setSearchListFetchSize(pSearchListFetchSize);
    }
    
    @Override
    public Integer getUserSearchListFetchSize(final Long pUserId) {
        UserProperties props = getUserProperties(pUserId);
        Integer searchListFetchSize = props.common.getSearchListFetchSize();
        if (searchListFetchSize == null || searchListFetchSize.equals(0)) {
            searchListFetchSize = cpxServerConfig.getSearchListFetchSize();
        }
        return searchListFetchSize;
    }
    
    @Override
    public boolean setUserSearchListFetchSize(final Long pUserId, final Integer pSearchListFetchSize) {
        UserProperties props = getUserProperties(pUserId);
        if (pSearchListFetchSize.equals(cpxServerConfig.getSearchListFetchSize())) {
            props.common.setSearchListFetchSize(null);
        } else {
            props.common.setSearchListFetchSize(pSearchListFetchSize);
        }
        return setUserProperties(pUserId, props);
    }
    
    @Override
    public boolean getWorkingListLocal() {
        return cpxServerConfig.getWorkingListLocal();
    }
    
    @Override
    public void setWorkingListLocal(final boolean pIsLocal) {
        cpxServerConfig.setWorkingListLocal(pIsLocal);
    }

//    @Override
//    @PermitAll
//    public boolean getDocumentDatabaseConfig() {
//        return cpxServerConfig.getDocumentDatabaseConfig();
//    }
//
//    @Override
//    @PermitAll
//    public void setDocumentDatabaseConfig(final boolean inDB) {
//        cpxServerConfig.setDocumentDatabaseConfig(inDB);
//    }
//
//    @Override
//    @PermitAll
//    public boolean getDocumentFileSystemConfig() {
//        return cpxServerConfig.getDocumentFileSystemConfig();
//    }
//
//    @Override
//    @PermitAll
//    public void setDocumentFileSystemConfig(final boolean inFS) {
//        cpxServerConfig.setDocumentFileSystemConfig(inFS);
//    }
//
//    @Override
//    @PermitAll
//    public void setServerRootFolder(final String path) {
//        cpxServerConfig.setServerRootFolder(path);
//    }
//
//    @Override
//    @PermitAll
//    public String getServerRootFolder() {
//        return cpxServerConfig.getServerRootFolder();
//    }
    @Override
    public boolean getUserWorkingListLocal(final Long pUserId) {
        UserProperties props = getUserProperties(pUserId);
        Boolean workingListLocal = props.common.getWorkingListLocal();
        if (workingListLocal == null) {
            workingListLocal = cpxServerConfig.getWorkingListLocal();
        }
        return workingListLocal;
    }
    
    @Override
    public boolean setUserWorkingListLocal(final Long pUserId, final boolean pIsLocal) {
        UserProperties props = getUserProperties(pUserId);
        Boolean tmp = cpxServerConfig.getWorkingListLocal();
        if (tmp.equals(pIsLocal)) {
            props.common.setWorkingListLocal(null);
        } else {
            props.common.setWorkingListLocal(pIsLocal);
        }
        return setUserProperties(pUserId, props);
    }
    
    @Override
    public boolean getUserShowHistoryEventDetails(final Long pUserId) {
        UserProperties props = getUserProperties(pUserId);
        Boolean showHistoryEventDetails = props.modules.processView.getShowHistoryEventDetails();
        if (showHistoryEventDetails == null) {
            showHistoryEventDetails = cpxServerConfig.getShowHistoryEventDetails();
        }
        return showHistoryEventDetails;
    }
    
    @Override
    public boolean setUserShowHistoryEventDetails(final Long pUserId, final boolean pShowHistoryEventDetails) {
        UserProperties props = getUserProperties(pUserId);
        Boolean tmp = cpxServerConfig.getShowHistoryEventDetails();
        if (tmp.equals(pShowHistoryEventDetails)) {
            props.modules.processView.setShowHistoryEventDetails(null);
        } else {
            props.modules.processView.setShowHistoryEventDetails(pShowHistoryEventDetails);
        }
        return setUserProperties(pUserId, props);
    }
    
    @Override
    public boolean getUserShowHistoryDeleted(final Long pUserId) {
        UserProperties props = getUserProperties(pUserId);
        Boolean showHistoryDeleted = props.modules.processView.getShowHistoryDeleted();
        if (showHistoryDeleted == null) {
            showHistoryDeleted = cpxServerConfig.getShowHistoryDeleted();
        }
        return showHistoryDeleted;
    }
    
    @Override
    public boolean setUserShowHistoryDeleted(final Long pUserId, final boolean pShowHistoryDeleted) {
        UserProperties props = getUserProperties(pUserId);
        Boolean tmp = cpxServerConfig.getShowHistoryDeleted();
        if (tmp.equals(pShowHistoryDeleted)) {
            props.modules.processView.setShowHistoryDeleted(null);
        } else {
            props.modules.processView.setShowHistoryDeleted(pShowHistoryDeleted);
        }
        return setUserProperties(pUserId, props);
    }
    
    @Override
    public HistoryFilter getUserHistoryFilter(final Long pUserId) {
        UserProperties props = getUserProperties(pUserId);
        HistoryFilter historyFilter = props.modules.processView.getHistoryFilter();
        if (historyFilter == null) {
            historyFilter = new HistoryFilter();
        }
        return historyFilter;
    }
    
    @Override
    public boolean setUserHistoryFilter(final Long pUserId, final HistoryFilter pHistoryFilter) {
        UserProperties props = getUserProperties(pUserId);
        props.modules.processView.setHistoryFilter(pHistoryFilter);
        return setUserProperties(pUserId, props);
    }
    
    @Override
    public P21ExportSettings getUserP21ExportSettings(final Long pUserId) {
        UserProperties props = getUserProperties(pUserId);
        P21ExportSettings p21ExportSettings = props.modules.workingList.getP21ExportSettings();
        if (p21ExportSettings == null) {
            p21ExportSettings = new P21ExportSettings();
        }
        return p21ExportSettings;
    }
    
    @Override
    public boolean setUserP21ExportSettings(final Long pUserId, final P21ExportSettings pP21ExportSettings) {
        UserProperties props = getUserProperties(pUserId);
        props.modules.workingList.setP21ExportSettings(pP21ExportSettings);
        return setUserProperties(pUserId, props);
    }
    
    @Override
    public RuleFilterDTO getRuleFilterCaseListForUser(final Long pUserId) {
        UserProperties props = getUserProperties(pUserId);
        RuleFilterDTO ruleFilterDTO = props.modules.ruleFilter.getRuleFilterCaseList();
        if (ruleFilterDTO == null) {
            ruleFilterDTO = new RuleFilterDTO();
        }
        return ruleFilterDTO;
    }
    
    @Override
    public boolean setRuleFilterCaseListForUser(final Long pUserId, final RuleFilterDTO pRuleFilterDTO) {
        UserProperties props = getUserProperties(pUserId);
        props.modules.ruleFilter.setRuleFilterCaseList(pRuleFilterDTO);
        return setUserProperties(pUserId, props);
    }
    
    @Override
    public RuleFilterDTO getRuleFilterBatchAdmForUser(final Long pUserId) {
        UserProperties props = getUserProperties(pUserId);
        RuleFilterDTO ruleFilterDTO = props.modules.ruleFilter.getRuleFilterBatchAdm();
        if (ruleFilterDTO == null) {
            ruleFilterDTO = new RuleFilterDTO();
        }
        return ruleFilterDTO;
    }
    
    @Override
    public boolean setRuleFilterBatchAdmForUser(final Long pUserId, final RuleFilterDTO pRuleFilterDTO) {
        UserProperties props = getUserProperties(pUserId);
        props.modules.ruleFilter.setRuleFilterBatchAdm(pRuleFilterDTO);
        return setUserProperties(pUserId, props);
    }

////RSH: 19042018 CPX-857
//    @Override
//    public boolean getWorkflowListAllReminder() {
//        return cpxServerConfig.getWorkflowListAllReminder();
//    }
//
//    @Override
//    public void setWorkflowListAllReminder(final boolean pAllReminder) {
//        cpxServerConfig.setWorkflowListAllReminder(pAllReminder);
//    }
    @Override
    public boolean getFilterListDetailsOverview(final Long pUserId) {
        UserProperties props = getUserProperties(pUserId);
        Boolean isShowFilterListDetailsOverview = props.common.getFilterListDetailsOverview();
        if (isShowFilterListDetailsOverview == null) {
            isShowFilterListDetailsOverview = cpxServerConfig.getFilterListDetailsOverview();
        }
        return isShowFilterListDetailsOverview;
    }
    
    @Override
    public boolean setFilterListDetailsOverview(final Long pUserId, final boolean isShowFilterListDetailsOverview) {
//        UserProperties props = getUserProperties(pUserId);
//        Boolean tmp = cpxServerConfig.getFilterListDetailsOverview();
//        if (tmp != null && tmp.equals(isShowFilterListDetailsOverview)) {
//            props.common.setFilterListDetailsOverview(null);
//        } else {
//            props.common.setFilterListDetailsOverview(isShowFilterListDetailsOverview);
//        }
//        return setUserProperties(pUserId, props);
        UserProperties props = getUserProperties(pUserId);
        props.common.setFilterListDetailsOverview(isShowFilterListDetailsOverview);
        return setUserProperties(pUserId, props);
    }
    
    @Override
    public boolean setWorkflowListAllReminder(final Long pUserId, final boolean pAllReminder) {
        UserProperties props = getUserProperties(pUserId);
        props.modules.workflowList.setWorkflowListAllReminder(pAllReminder);
        return setUserProperties(pUserId, props);
    }
    
    @Override
    public boolean getWorkflowListAllReminder(final Long pUserId) {
        UserProperties props = getUserProperties(pUserId);
        Boolean isShowWorkflowListAllReminder = props.modules.workflowList.getWorkflowListAllReminder();
        if (isShowWorkflowListAllReminder == null) {
            isShowWorkflowListAllReminder = cpxServerConfig.getWorkflowListAllReminder();
        }
        return isShowWorkflowListAllReminder;
    }
    
    @Override
    public boolean setWmMainFrameSubject(final Long pUserId, boolean isWmMainFrameSubject) {
        UserProperties props = getUserProperties(pUserId);
        props.modules.processView.setWmMainFrameSubject(isWmMainFrameSubject);
        return setUserProperties(pUserId, props);
        
    }
    
    @Override
    public boolean setWmMainFrameState(final Long pUserId, boolean isWmMainFrameState) {
        UserProperties props = getUserProperties(pUserId);
        props.modules.processView.setWmMainFrameState(isWmMainFrameState);
        return setUserProperties(pUserId, props);
    }
    
    @Override
    public boolean setWmMainFrameWVNumber(final Long pUserId, boolean isWmMainFrameWVNumber) {
        UserProperties props = getUserProperties(pUserId);
        props.modules.processView.setWmMainFrameWVNumber(isWmMainFrameWVNumber);
        return setUserProperties(pUserId, props);
    }
    
    @Override
    public boolean setWmMainFrameFNLawer(final Long pUserId, boolean isWmMainFrameFNLawer) {
        UserProperties props = getUserProperties(pUserId);
        props.modules.processView.setWmMainFrameFNLawer(isWmMainFrameFNLawer);
        return setUserProperties(pUserId, props);
    }
    
    @Override
    public boolean setWmMainFrameFNCourt(final Long pUserId, boolean isWmMainFrameFNCourt) {
        UserProperties props = getUserProperties(pUserId);
        props.modules.processView.setWmMainFrameFNCourt(isWmMainFrameFNCourt);
        return setUserProperties(pUserId, props);
    }
    
    @Override
    public boolean setWmMainFrameWvUser(final Long pUserId, boolean isWmMainFrameWvUser) {
        UserProperties props = getUserProperties(pUserId);
        props.modules.processView.setWmMainFrameWvUser(isWmMainFrameWvUser);
        return setUserProperties(pUserId, props);
    }
    
    @Override
    public boolean setAlwaysInfoForExaminateQuota(final Long pUserId, boolean showAlwaysInfoForExaminateQuota) {
        UserProperties props = getUserProperties(pUserId);
        props.modules.processView.setAlwaysInfoForExaminateQuota(showAlwaysInfoForExaminateQuota);
        return setUserProperties(pUserId, props);
    }
    
    @Override
    public boolean getWmMainFrameFNCourt(final Long pUserId) {
        UserProperties props = getUserProperties(pUserId);
        Boolean isWmMainFrameFNCourt = props.modules.processView.getWmMainFrameFNCourt();
        if (isWmMainFrameFNCourt == null) {
            return false;
        }
//            isWmMainFrameFNCourt = cpxServerConfig.getWmMainFrameFNCourt();
//        }
        return isWmMainFrameFNCourt;
    }
    
    @Override
    public boolean getWmMainFrameFNLawer(final Long pUserId) {
        UserProperties props = getUserProperties(pUserId);
        Boolean isWmMainFrameFNLawer = props.modules.processView.getWmMainFrameFNLawer();
        if (isWmMainFrameFNLawer == null) {
            return false;
        }
//            isWmMainFrameFNCourt = cpxServerConfig.getWmMainFrameFNCourt();
//        }
        return isWmMainFrameFNLawer;
    }
    
    @Override
    public boolean getAlwaysInfoForExaminateQuota(final Long pUserId) {
        UserProperties props = getUserProperties(pUserId);
        Boolean showAlwaysInfoForExaminateQuota = props.modules.processView.getAlwaysInfoForExaminateQuota();
        if (showAlwaysInfoForExaminateQuota == null) {
            return false;
        }
        return showAlwaysInfoForExaminateQuota;
    }
    
    @Override
    public boolean getWmMainFrameState(final Long pUserId) {
        UserProperties props = getUserProperties(pUserId);
        Boolean isWmMainFrameState = props.modules.processView.getWmMainFrameState();
        if (isWmMainFrameState == null) {
            return false;
        }
//            isWmMainFrameFNCourt = cpxServerConfig.getWmMainFrameFNCourt();
//        }
        return isWmMainFrameState;
    }
    
    @Override
    public boolean getWmMainFrameSubject(final Long pUserId) {
        UserProperties props = getUserProperties(pUserId);
        Boolean isWmMainFrameSubject = props.modules.processView.getWmMainFrameSubject();
        if (isWmMainFrameSubject == null) {
            return false;
        }
//            isWmMainFrameFNCourt = cpxServerConfig.getWmMainFrameFNCourt();
//        }
        return isWmMainFrameSubject;
    }
    
    @Override
    public boolean getWmMainFrameWVNumber(final Long pUserId) {
        UserProperties props = getUserProperties(pUserId);
        Boolean isWmMainFrameWVNumber = props.modules.processView.getWmMainFrameWVNumber();
        if (isWmMainFrameWVNumber == null) {
            return false;
        }
//            isWmMainFrameFNCourt = cpxServerConfig.getWmMainFrameFNCourt();
//        }
        return isWmMainFrameWVNumber;
    }
    
    @Override
    public boolean getWmMainFrameWvUser(final Long pUserId) {
        UserProperties props = getUserProperties(pUserId);
        Boolean isWmMainFrameWVUser = props.modules.processView.getWmMainFrameWVUser();
        if (isWmMainFrameWVUser == null) {
            return false;
        }
//            isWmMainFrameFNCourt = cpxServerConfig.getWmMainFrameFNCourt();
//        }
        return isWmMainFrameWVUser;
    }
    
    @Override
    public boolean getDocumentImportOfficeEnabled(Long pUserId) {
        UserProperties props = getUserProperties(pUserId);
        Boolean documentImportOfficeEnabled = props.modules.documentImport.getDocumentImportOfficeEnabled();
        if (documentImportOfficeEnabled == null) {
            documentImportOfficeEnabled = cpxServerConfig.getDocumentImportOfficeEnabled();
        }
        return documentImportOfficeEnabled;
    }
    
    @Override
    public boolean setDocumentImportOfficeEnabled(Long pUserId, Boolean pDocumentImportOfficeEnabled) {
        UserProperties props = getUserProperties(pUserId);
        Boolean tmp = cpxServerConfig.getDocumentImportOfficeEnabled();
        if (tmp.equals(pDocumentImportOfficeEnabled)) {
            props.modules.documentImport.setDocumentImportOfficeEnabled(null);
        } else {
            props.modules.documentImport.setDocumentImportOfficeEnabled(pDocumentImportOfficeEnabled);
        }
        //props.setDocumentImportOfficeEnabled(pDocumentImportDetection);
        return setUserProperties(pUserId, props);
    }
    
    @Override
    public boolean getDocumentImportShowPdfEnabled(Long pUserId) {
        UserProperties props = getUserProperties(pUserId);
        Boolean documentImportOfficeEnabled = props.modules.documentImport.getDocumentImportShowPdfEnabled();
        if (documentImportOfficeEnabled == null) {
            documentImportOfficeEnabled = cpxServerConfig.getDocumentImportShowPdfEnabled();
        }
        return documentImportOfficeEnabled;
    }
    
    @Override
    public boolean setDocumentImportShowPdfEnabled(Long pUserId, Boolean pDocumentImportShowPdfEnabled) {
        UserProperties props = getUserProperties(pUserId);
        Boolean tmp = cpxServerConfig.getDocumentImportShowPdfEnabled();
        if (tmp.equals(pDocumentImportShowPdfEnabled)) {
            props.modules.documentImport.setDocumentImportShowPdfEnabled(null);
        } else {
            props.modules.documentImport.setDocumentImportShowPdfEnabled(pDocumentImportShowPdfEnabled);
        }
        //props.setDocumentImportShowPdfEnabled(pDocumentImportDetection);
        return setUserProperties(pUserId, props);
    }
    
    @Override
    public RuleOverrideFlags getRuleOverrideFlag(Long pUserId) {
        UserProperties props = getUserProperties(pUserId);
        RuleOverrideFlags ruleOverrideFlag = props.modules.ruleEditor.getRuleOverrideFlag();
        if (ruleOverrideFlag == null) {
            ruleOverrideFlag = cpxServerConfig.getRuleOverrideFlag();
        }
        return ruleOverrideFlag;
    }
    
    @Override
    public boolean setRuleOverrideFlag(Long pUserId, RuleOverrideFlags pRuleOverrideFlag) {
        UserProperties props = getUserProperties(pUserId);
        RuleOverrideFlags tmp = cpxServerConfig.getRuleOverrideFlag();
        if (tmp != null && tmp.equals(pRuleOverrideFlag)) {
            props.modules.ruleEditor.setRuleOverrideFlag(null);
        } else {
            props.modules.ruleEditor.setRuleOverrideFlag(pRuleOverrideFlag);
        }
        //props.setRuleOverrideFlag(pRuleOverrideFlag);
        return setUserProperties(pUserId, props);
    }
    
    @Override
    public RuleImportCheckFlags getRuleImportCheckFlag(Long pUserId) {
        UserProperties props = getUserProperties(pUserId);
        RuleImportCheckFlags ruleImportCheckFlag = props.modules.ruleEditor.getRuleImportCheckFlag();
        if (ruleImportCheckFlag == null) {
            ruleImportCheckFlag = cpxServerConfig.getRuleImportCheckFlag();
        }
        return ruleImportCheckFlag;
    }
    
    @Override
    public boolean setRuleImportCheckFlag(Long pUserId, RuleImportCheckFlags pRuleImportCheckFlag) {
        UserProperties props = getUserProperties(pUserId);
        RuleImportCheckFlags tmp = cpxServerConfig.getRuleImportCheckFlag();
        if (tmp != null && tmp.equals(pRuleImportCheckFlag)) {
            props.modules.ruleEditor.setRuleImportCheckFlag(null);
        } else {
            props.modules.ruleEditor.setRuleImportCheckFlag(pRuleImportCheckFlag);
        }
        //props.setRuleImportCheckFlag(pRuleImportCheckFlag);
        return setUserProperties(pUserId, props);
    }
    
    @Override
    public String getUserLanguage(final Long pUserId) {
        UserProperties props = getUserProperties(pUserId);
        String language = props.common.getLanguage();
        if (language == null || language.trim().isEmpty()) {
            language = cpxServerConfig.getLanguage();
        }
        return language;
    }
    
    @Override
    public boolean setUserLanguage(final Long pUserId, final String pLanguage) {
        UserProperties props = getUserProperties(pUserId);
        String language = (pLanguage == null) ? null : pLanguage.trim().toLowerCase();
        if (language != null && language.equalsIgnoreCase(cpxServerConfig.getLanguage())) {
            props.common.setLanguage(null);
        } else {
            props.common.setLanguage(language);
        }
        return setUserProperties(pUserId, props);
    }
    
    @Override
    public long[] getUserSelectedDraftCategories(final Long pUserId) {
        UserProperties props = getUserProperties(pUserId);
        long[] selectedDraftCategories = props.modules.processView.getSelectedDraftCategories();
        return selectedDraftCategories == null ? new long[0] : selectedDraftCategories;
//        if (selectedDraftCategories == null || selectedDraftCategories .trim().isEmpty()) {
//            selectedDraftCategories = cpxServerConfig.getSelectedDraftCategories();
//        }
//        return selectedDraftCategories;
    }
    
    @Override
    public boolean setUserSelectedDraftCategories(final Long pUserId, final long[] pSelectedDraftCategories) {
        UserProperties props = getUserProperties(pUserId);
        props.modules.processView.setSelectedDraftCategories(pSelectedDraftCategories);
//        String selectedDraft = (pSelectedDraftCategories == null) ? null : pSelectedDraftCategories.trim().toLowerCase();
//        if (selectedDraft != null && selectedDraft.equalsIgnoreCase(cpxServerConfig.getSelectedDraftCategories())) {
//            props.setSelectedDraftCategories(null);
//        } else {
//            props.setSelectedDraftCategories(selectedDraft);
//        }
        return setUserProperties(pUserId, props);
    }
    
    @Override
    public Long getUserRecentMdkInternalId(final Long pUserId) {
        UserProperties props = getUserProperties(pUserId);
        Long recentMdk = props.modules.processView.getRecentMdkInternalId();
//        if (recentClientView == null || recentClientView.trim().isEmpty()) {
//            recentClientView = cpxServerConfig.getRecentClientView(); or something static like 'WORKING_LIST'
//        }
        return recentMdk;
    }
    
    @Override
    public boolean setUserRecentMdkInternalId(final Long pUserId, final Long pRecentMdkInternalId) {
        UserProperties props = getUserProperties(pUserId);
//        String recentMdk = (pRecentMdkInternalId == null) ? null : pRecentMdkInternalId.trim();
//        if (recentClientView.equalsIgnoreCase(cpxServerConfig.getLanguage())) {
//            props.setLanguage(null);
//        } else {
//            props.setLanguage(pRecentClientView);
//        }
        props.modules.processView.setRecentMdkInternalId(pRecentMdkInternalId);
        return setUserProperties(pUserId, props);
    }
    
    @Override
    public String getUserRecentClientScene(final Long pUserId) {
        UserProperties props = getUserProperties(pUserId);
        String recentClientScene = props.common.getRecentClientScene();
//        if (recentClientView == null || recentClientView.trim().isEmpty()) {
//            recentClientView = cpxServerConfig.getRecentClientView(); or something static like 'WORKING_LIST'
//        }
        return recentClientScene;
    }
    
    @Override
    public boolean setUserRecentClientScene(final Long pUserId, final String pRecentClientScene) {
        UserProperties props = getUserProperties(pUserId);
        String recentClientScene = (pRecentClientScene == null) ? null : pRecentClientScene.trim();
//        if (recentClientView.equalsIgnoreCase(cpxServerConfig.getLanguage())) {
//            props.setLanguage(null);
//        } else {
//            props.setLanguage(pRecentClientView);
//        }
        props.common.setRecentClientScene(recentClientScene);
        return setUserProperties(pUserId, props);
    }
    
    @Override
    public String getRecentRuleListTab(final Long pUserId) {
        UserProperties props = getUserProperties(pUserId);
        String recentRuleListTab = props.modules.ruleEditor.getRecentRuleListTab();
//        if (recentClientView == null || recentClientView.trim().isEmpty()) {
//            recentClientView = cpxServerConfig.getRecentClientView(); or something static like 'WORKING_LIST'
//        }
        return recentRuleListTab;
    }
    
    @Override
    public boolean setRecentRuleListTab(final Long pUserId, final String pRecentRuleListTab) {
        UserProperties props = getUserProperties(pUserId);
        String recentRuleListTab = (pRecentRuleListTab == null) ? null : pRecentRuleListTab.trim();
//        if (recentClientView.equalsIgnoreCase(cpxServerConfig.getLanguage())) {
//            props.setLanguage(null);
//        } else {
//            props.setLanguage(pRecentClientView);
//        }
        props.modules.ruleEditor.setRecentRuleListTab(recentRuleListTab);
        return setUserProperties(pUserId, props);
    }
    
    @Override
    public List<String> getLocales() {
        List<String> localeList = new LinkedList<>();
        CpxSystemPropertiesInterface props = CpxSystemProperties.getInstance();
        File folder = new File(props.getCpxServerLocaleDir());
        /*
    if (!folder.exists() || !folder.isDirectory()) {
      
    }
         */
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (!file.isFile()) {
                continue;
            }
            String name = file.getName();
            int pos = name.toLowerCase().indexOf(".properties");
            if (pos < 0) {
                //Is not a .properties file
                continue;
            }
            if (name.toLowerCase().startsWith("custom.")) {
                continue;
            }
            String locale = name.substring(0, pos).trim();
            localeList.add(locale);
        }
        Collections.sort(localeList);
        return localeList;
    }

//    public SearchList createEmptyList(final SearchListTypeEn pList, final Long pUserId) throws IOException {
//        SearchListAttributes attributes = RuleListAttributes.instance();
//        SearchList ruleList = new SearchList(SearchListTypeEn.RULE, "Regel_DRG");
//        for (Map.Entry<String, SearchListAttribute> entry : attributes.getAll().entrySet()) {
//            SearchListAttribute att = entry.getValue();
//
//            if (!RuleListAttributes.getRuleColumns().contains(att.getKey())) {
//                continue;
//            }
//            if (!att.isVisible()) {
//                continue;
//            }
//
//            if (att.getParent() != null) {
//                continue;
//            }
//
//            List<SearchListAttribute> attributeList = new LinkedList<>();
//            if (att.hasChildren()) {
//                attributeList.addAll(att.getChildren());
//            } else {
//                attributeList.add(att);
//            }
//            boolean selected = false;
//            int size = att.getSize();
//            //ColumnOption colOption = workingList.getColumn(att.getKey().getName());
//            int sort = 0;
//            if (pList == SearchListTypeEn.WORKING) {
//                sort = WorkingListAttributes.getDefaultColumns().indexOf(att.getKey());
//            } else {
//                sort = WorkflowListAttributes.getDefaultColumns().indexOf(att.getKey());
//            }
//            sort++;
//            Integer sortNumber = sort;
//            String sortType = "";
//            Integer number = sort;
//            //workinList.addColumn(colOption);
//            //String displayName = lang.get(att.getName());
//            //ColumnOption option = new ColumnOption(att.getLanguageKey(), att.getKey().getName(), selected);
//            ColumnOption option = new ColumnOption(att.getLanguageKey(), att.getKey(), selected);
//            //option.setWorkingListAttribute(att);
//            option.size = size;
//            option.sortNumber = sortNumber;
//            option.sortType = sortType;
//            option.number = number;
//            option.shouldShow = true;
//
//            ruleList.addColumn(option);
//        }
//        ruleList.addFilter(new FilterOption("", RuleListAttributes.csCaseTypeEn, CaseTypeEn.DRG.name()));
//        saveSearchList(pUserId, ruleList, true, true);
//
//        ruleList = new SearchList(SearchListTypeEn.RULE, "Regel_PEPP");
//        for (Map.Entry<String, SearchListAttribute> entry : attributes.getAll().entrySet()) {
//            SearchListAttribute att = entry.getValue();
//
//            if (!RuleListAttributes.getRuleColumns().contains(att.getKey())) {
//                continue;
//            }
//            if (!att.isVisible()) {
//                continue;
//            }
//
//            if (att.getParent() != null) {
//                continue;
//            }
//
//            List<SearchListAttribute> attributeList = new LinkedList<>();
//            if (att.hasChildren()) {
//                attributeList.addAll(att.getChildren());
//            } else {
//                attributeList.add(att);
//            }
//            boolean selected = false;
//            int size = att.getSize();
//            //ColumnOption colOption = workingList.getColumn(att.getKey().getName());
//            int sort = 0;
//            if (pList == SearchListTypeEn.WORKING) {
//                sort = WorkingListAttributes.getDefaultColumns().indexOf(att.getKey());
//            } else {
//                sort = WorkflowListAttributes.getDefaultColumns().indexOf(att.getKey());
//            }
//            sort++;
//            Integer sortNumber = sort;
//            String sortType = "";
//            Integer number = sort;
//            //workinList.addColumn(colOption);
//            //String displayName = lang.get(att.getName());
//            //ColumnOption option = new ColumnOption(att.getLanguageKey(), att.getKey().getName(), selected);
//            ColumnOption option = new ColumnOption(att.getLanguageKey(), att.getKey(), selected);
//            //option.setWorkingListAttribute(att);
//            option.size = size;
//            option.sortNumber = sortNumber;
//            option.sortType = sortType;
//            option.number = number;
//            option.shouldShow = true;
//
//            ruleList.addColumn(option);
//        }
//        ruleList.addFilter(new FilterOption("", RuleListAttributes.csCaseTypeEn, CaseTypeEn.PEPP.name()));
//        return ruleList;
//    }
//    @Override
//    //@PermitAll
//    public SearchListResult getSelectedSearchList(final Long pUserId, final SearchListTypeEn pList, final boolean pLoadPropertiesEagerly) {
//        UserProperties props = getUserProperties(pUserId);
//        Long searchListId = props.common.getSelectedSearchList(pList);
//        return searchListDao.getSearchListProperties(searchListId);
////        SearchList workingList1 = null;
////        for (SearchList workingList : list) {
////            if (workingList == null) {
////                continue;
////            }
////            if (workingList.isSelected()) { // && pList.equals(workingList.getList())
////                workingList1 = workingList;
////            }
////        }
////
////        if (props.getSearchLists(LIST.RULE).isEmpty()) {
////            LOG.log(Level.INFO, "No Rule-Searchlist found! I will create one now!");
////            final SearchList ruleList = createEmptyList(pList, pUserId);
////            setSearchListProperties(pUserId, SearchListTypeEn.RULE, ruleList);
////        }
////        if (workingList1 != null) {
////            return workingList1;
////        }
////        SearchList searchList = new SearchList(pList, "empty");
////        SearchListAttributes attributes = pList == SearchListTypeEn.WORKING ? WorkingListAttributes.instance() : WorkflowListAttributes.instance();
////        for (Map.Entry<String, SearchListAttribute> entry : attributes.getAll().entrySet()) {
////            SearchListAttribute att = entry.getValue();
////
////            if (!att.isVisible()) {
////                continue;
////            }
////
////            if (att.getParent() != null) {
////                continue;
////            }
////
////            if (WorkingListAttributes.getDefaultColumns().contains(att.getKey()) || WorkflowListAttributes.getDefaultColumns().contains(att.getKey())) {
////
////                List<SearchListAttribute> attributeList = new LinkedList<>();
////                if (att.hasChildren()) {
////                    attributeList.addAll(att.getChildren());
////                } else {
////                    attributeList.add(att);
////                }
////                boolean selected = false;
////                int size = att.getSize();
////                //ColumnOption colOption = workingList.getColumn(att.getKey().getName());
////                int sort = 0;
////                if (pList == SearchListTypeEn.WORKING) {
////                    sort = WorkingListAttributes.getDefaultColumns().indexOf(att.getKey());
////                } else {
////                    sort = WorkflowListAttributes.getDefaultColumns().indexOf(att.getKey());
////                }
////                sort++;
////                Integer sortNumber = sort;
////                String sortType = "";
////                Integer number = sort;
////                //workinList.addColumn(colOption);
////                //String displayName = lang.get(att.getName());
////                //ColumnOption option = new ColumnOption(att.getLanguageKey(), att.getKey().getName(), selected);
////                ColumnOption option = new ColumnOption(att.getLanguageKey(), att.getKey(), selected);
////                //option.setWorkingListAttribute(att);
////                option.size = size;
////                option.sortNumber = sortNumber;
////                option.sortType = sortType;
////                option.number = number;
////                option.shouldShow = true;
////                searchList.addColumn(option);
////            }
////        }
////        return searchList;
//    }
//
//    @Override
//    @PermitAll
//    public boolean setSelectedSearchList(final Long pUserId, final SearchListTypeEn pList, final String pCurrentWorkingListName) {
//        String currentWorkingListName = (pCurrentWorkingListName == null) ? "" : pCurrentWorkingListName.trim();
//        UserProperties props = getUserProperties(pUserId);
//        for (SearchList workingList : props.getSearchLists(pList)) {
//            if (workingList == null) {
//                continue;
//            }
//            if (workingList.getName().equalsIgnoreCase(currentWorkingListName)) {
//                workingList.setSelected(true);
//            } else {
//                workingList.setSelected(false);
//            }
//        }
//        return setUserProperties(pUserId, props);
//    }
//
//    @Override
//    @PermitAll
//    public String getSelectedSearchListName(final Long pUserId, final SearchListTypeEn pList) {
//        return getSelectedSearchList(pUserId, pList).getName();
//    }

    /*
  @Override
  public boolean setCurrentWorkingListName(final Long pUserId, final String pCurrentWorkingListName) {
    UserProperties props = getUserProperties(pUserId);
    props.setCurrentWorkingListName(pCurrentWorkingListName);
    return setUserProperties(pUserId, props);
  }
     */
    @Override
    public UserProperties getUserProperties(final Long pUserId) {
        //return cpxServerConfig.getUserProperties(pUserId);
        CdbUsers user = cdbUsersDao.findById(pUserId);
        UserProperties props = null;
        if (user != null) {
            props = cdbUsersDao.getUserProperties(pUserId);
        }
        if (props == null) {
            props = new UserProperties();
        }
        return props;
    }
    
    @Override
    public boolean setUserProperties(final Long pUserId, final UserProperties pUserProperties) {
        return cdbUsersDao.setUserProperties(pUserId, pUserProperties);
    }
    
    @Override
    public Boolean getAutoGroupingProperty(Long pUserId) {
        UserProperties props = getUserProperties(pUserId);
        return props.modules.caseView.getAutoGrouping();
    }
    
    @Override
    public boolean setAutoGroupingProperty(Long pUserId, Boolean pValue) {
        UserProperties props = getUserProperties(pUserId);
        props.modules.caseView.setAutoGrouping(pValue);
        return setUserProperties(pUserId, props);
    }
    
    @Override
    public Boolean getPinnedSidebarProperty(Long pUserId) {
        UserProperties props = getUserProperties(pUserId);
        return props.common.getPinnedSidebar();
    }
    
    @Override
    public boolean setPinnedSidebarProperty(Long pUserId, Boolean pValue) {
        UserProperties props = getUserProperties(pUserId);
        props.common.setPinnedSidebar(pValue);
        return setUserProperties(pUserId, props);
    }
    
    @Override
    public boolean getRulesDatabaseConfig() {
        return cpxServerConfig.getRulesDatabaseConfig();
    }
    
    @Override
    public void setRulesDatabaseConfig(final boolean inDB) {
        cpxServerConfig.setRulesDatabaseConfig(inDB);
    }
    
    @Override
    public boolean getCommonHealthStatusVisualization() {
        return cpxServerConfig.getCommonHealthStatusVisualization();
    }
    
    @Override
    public boolean isRuleEditorClient() {
        return cpxServerConfig.isRuleEditorClient();
    }
    
    @Override
    public void setRuleEditorClient(boolean pRuleEditorClient) {
        cpxServerConfig.setRuleEditorClient(pRuleEditorClient);
    }
    
    @Override
    public int getDocumentsSizeMax() {
        return cpxServerConfig.getDocumentsSizeMax();
    }
    
    @Override
    public void setDocumentsSizeMax(final Integer pDocSizeMaxKb) {
        cpxServerConfig.setDocumentsSizeMax(pDocSizeMaxKb);
    }
    
    @Override
    public boolean useHistoryCases4Group() {        
        return cpxServerConfig.useHistoryCases4Group();
    }
    
    @Override
    public void setUseHistoryCases4Group(final boolean pUseIt) {
        cpxServerConfig.setRulesDatabaseConfig(pUseIt);
    }
    
    @Override
    public boolean getShowRelevantRules() {
        return cpxServerConfig.getShowRelevantRules();
    }
    
    @Override
    public void setShowRelevantRules(final boolean config) {
        cpxServerConfig.setShowRelevantRules(config);
    }
    
    @Override
    public boolean hasActiveSapJobs() {
        //TODO (maybe): you should additionally pass the database name and check if it equals to job configuration
        return !cpxServerConfig.getActiveJobConfigs(SapJob.class).isEmpty();
    }

    @Override
    public boolean getUseGdv() {
        return cpxServerConfig.getUseGdv();
    }

    @Override
    public void setUseGdv(boolean pUseGdv) {
        cpxServerConfig.setUseGdv(pUseGdv);
    }

    @Override
    public boolean getSendGdvResponce() {
        return cpxServerConfig.getSendGdvResponce();
    }

    @Override
    public void setSendGdvResponce(boolean pSendGdvResponce) {
        cpxServerConfig.setSendGdvResponce(pSendGdvResponce);
    }

    @Override
    public String getGdvResponcePath() {
       return  cpxServerConfig.getGdvResponcePath();
    }

    @Override
    public void setGdvResponcePath(String pPath) {
        cpxServerConfig.setGdvResponcePath(pPath);
    }
}
