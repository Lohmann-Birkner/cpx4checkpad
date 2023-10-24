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

import de.lb.cpx.server.commons.enums.RuleImportCheckFlags;
import de.lb.cpx.server.commons.enums.RuleOverrideFlags;
import de.lb.cpx.service.properties.UserProperties;
import de.lb.cpx.shared.dto.HistoryFilter;
import de.lb.cpx.shared.dto.RuleFilterDTO;
import de.lb.cpx.shared.p21util.P21ExportSettings;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Dirk Niemeier
 */
@Remote
//@SecurityDomain("cpx")
public interface ConfigurationServiceEJBRemote {

    /**
     * CPX Program Directory on server readonly!
     *
     * @return JNDI Property of CPX_HOME in standalone*.xml
     */
    String getCpxHome();

    String getFontSize();

    void setFontSize(final String pFontSize);

    String getFont();

    void setFont(final String pFont);

    String getLanguage();

    void setLanguage(final String pLanguage);

    Integer getSearchListFetchSize();

    void setSearchListFetchSize(final Integer pSearchListFetchSize);

    boolean getWorkingListLocal();

    void setWorkingListLocal(final boolean pIsLocal);

    Integer getUserSearchListFetchSize(final Long pUserId);

    boolean setUserSearchListFetchSize(final Long pUserId, final Integer pSearchListFetchSize);

    boolean getUserWorkingListLocal(final Long pUserId);

//    @PermitAll
//    boolean getDocumentDatabaseConfig();
//
//    @PermitAll
//    void setDocumentDatabaseConfig(final boolean inDB);
//
//    @PermitAll
//    boolean getDocumentFileSystemConfig();
//
//    @PermitAll
//    void setDocumentFileSystemConfig(final boolean inFS);
//
//    @PermitAll
//    String getServerRootFolder();
//
//    @PermitAll
//    void setServerRootFolder(final String path);
    boolean setUserWorkingListLocal(final Long pUserId, final boolean pIsLocal);

    boolean getUserShowHistoryEventDetails(final Long pUserId);

    boolean setUserShowHistoryEventDetails(final Long pUserId, final boolean pShowHistoryEventDetails);

    boolean getUserShowHistoryDeleted(final Long pUserId);

    boolean setUserShowHistoryDeleted(final Long pUserId, final boolean pShowHistoryDeleted);

    HistoryFilter getUserHistoryFilter(final Long pUserId);

    boolean setUserHistoryFilter(final Long pUserId, final HistoryFilter pHistoryFilter);

    P21ExportSettings getUserP21ExportSettings(final Long pUserId);

    boolean setUserP21ExportSettings(final Long pUserId, final P21ExportSettings pP21ExportSettings);

    RuleFilterDTO getRuleFilterCaseListForUser(final Long pUserId);

    boolean setRuleFilterCaseListForUser(final Long pUserId, final RuleFilterDTO pRuleFilterDTO);

    RuleFilterDTO getRuleFilterBatchAdmForUser(final Long pUserId);

    boolean setRuleFilterBatchAdmForUser(final Long pUserId, final RuleFilterDTO pRuleFilterDTO);

    String getUserLanguage(final Long pUserId);

    boolean setUserLanguage(final Long pUserId, final String pLanguage);

    boolean setUserSelectedDraftCategories(final Long pUserId, final long[] pSelectedDraftCategories);

    long[] getUserSelectedDraftCategories(final Long pUserId);

    Long getUserRecentMdkInternalId(final Long pUserId);

    boolean setUserRecentMdkInternalId(final Long pUserId, final Long pRecentMdkInternalId);

    String getUserRecentClientScene(final Long pUserId);

    boolean setUserRecentClientScene(final Long pUserId, final String pRecentClientScene);

    String getRecentRuleListTab(final Long pUserId);

    boolean setRecentRuleListTab(final Long pUserId, final String pRecentRuleListTab);

    List<String> getLocales();

    boolean setUserProperties(final Long pUserId, final UserProperties pUserProperties);

    UserProperties getUserProperties(final Long pUserId);

    Boolean getAutoGroupingProperty(final Long pUserId);

    boolean setAutoGroupingProperty(final Long pUserId, final Boolean pValue);

    Boolean getPinnedSidebarProperty(final Long pUserId);

    boolean setPinnedSidebarProperty(final Long pUserId, final Boolean pValue);

    boolean getRulesDatabaseConfig();

    void setRulesDatabaseConfig(final boolean inDB);
    
    boolean getShowRelevantRules();

    void setShowRelevantRules(final boolean config);


//    boolean getWorkflowListAllReminder();
//
//    void setWorkflowListAllReminder(final boolean pWv);
    boolean getFilterListDetailsOverview(final Long pUserId);

    boolean setFilterListDetailsOverview(final Long pUserId, final boolean isShowFilterListDetailsOverview);

    boolean getWorkflowListAllReminder(final Long pUserId);

    boolean setWorkflowListAllReminder(final Long userId, final boolean pAllReminder);

    boolean setWmMainFrameSubject(final Long pUserId, boolean isWmMainFrameSubject);

    boolean setWmMainFrameState(final Long pUserId, boolean isWmMainFrameState);

    boolean setWmMainFrameWVNumber(final Long pUserId, boolean isWmMainFrameWVNumber);

    boolean setWmMainFrameFNLawer(final Long pUserId, boolean isWmMainFrameFNLawer);

    boolean setWmMainFrameFNCourt(final Long pUserId, boolean isWmMainFrameFNCourt);

    boolean setWmMainFrameWvUser(final Long pUserId, boolean isWmMainFrameWvUser);

    boolean setAlwaysInfoForExaminateQuota(final Long pUserId, boolean showAlwaysInfoForExaminateQuota);

    boolean getWmMainFrameFNCourt(final Long pUserId);

    boolean getWmMainFrameFNLawer(final Long pUserId);

    boolean getAlwaysInfoForExaminateQuota(final Long pUserId);

    boolean getWmMainFrameState(final Long pUserId);

    boolean getWmMainFrameSubject(final Long pUserId);

    boolean getWmMainFrameWVNumber(final Long pUserId);

    boolean getWmMainFrameWvUser(final Long pUserId);

    boolean getDocumentImportOfficeEnabled(final Long pUserId);

    boolean setDocumentImportOfficeEnabled(Long pUserId, Boolean pDocumentImportDetection);

    boolean getDocumentImportShowPdfEnabled(final Long pUserId);

    boolean setDocumentImportShowPdfEnabled(Long pUserId, Boolean pDocumentImportShowPdf);

    RuleOverrideFlags getRuleOverrideFlag(Long pUserId);

    boolean setRuleOverrideFlag(Long pUserId, RuleOverrideFlags pRuleOverrideFlag);

    RuleImportCheckFlags getRuleImportCheckFlag(Long pUserId);

    boolean setRuleImportCheckFlag(Long pUserId, RuleImportCheckFlags pRuleImportCheckFlag);

    /**
     * Should the patient's health status be rendered?
     *
     * @return is this feature enabled/disabled?
     */
    boolean getCommonHealthStatusVisualization();

    boolean isRuleEditorClient();

    void setRuleEditorClient(final boolean pRuleEditorClient);

    int getDocumentsSizeMax();

    void setDocumentsSizeMax(final Integer pDocSizeMaxKb);

    boolean useHistoryCases4Group() ;
    
    void setUseHistoryCases4Group(final boolean pUseIt);
    
    public boolean hasActiveSapJobs();

    boolean getUseGdv();
    void setUseGdv(final boolean pUseGdv);
    boolean getSendGdvResponce();
    void setSendGdvResponce(final boolean pSendGdvResponce);
    String getGdvResponcePath();
    void setGdvResponcePath(final String pPath);
}
