/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  sklarow - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.lb.cpx.model.enums.CategoryEn;
import de.lb.cpx.model.enums.CountryEn;
import de.lb.cpx.model.enums.SupplFeeTypeEn;
import de.lb.cpx.server.commonDB.dao.CBaserateDao;
import de.lb.cpx.server.commonDB.dao.CBookmarksCustomerDao;
import de.lb.cpx.server.commonDB.dao.CBookmarksDao;
import de.lb.cpx.server.commonDB.dao.CDeadlineDao;
import de.lb.cpx.server.commonDB.dao.CDraftsDao;
import de.lb.cpx.server.commonDB.dao.CDrgCatalogDao;
import de.lb.cpx.server.commonDB.dao.CHospitalDao;
import de.lb.cpx.server.commonDB.dao.CInsuranceCompanyDao;
import de.lb.cpx.server.commonDB.dao.CMdkAuditquotaDao;
import de.lb.cpx.server.commonDB.dao.CMdkAuditreasonDao;
import de.lb.cpx.server.commonDB.dao.CMdkDao;
import de.lb.cpx.server.commonDB.dao.CMdkNegotiableAuditquotaDao;
import de.lb.cpx.server.commonDB.dao.CPeppCatalogDao;
import de.lb.cpx.server.commonDB.dao.CSupplementaryFeeDao;
import de.lb.cpx.server.commonDB.dao.CTextTemplate2ContextDao;
import de.lb.cpx.server.commonDB.dao.CTextTemplate2UserRoleDao;
import de.lb.cpx.server.commonDB.dao.CTextTemplateDao;
import de.lb.cpx.server.commonDB.dao.CWmListActionSubjectDao;
import de.lb.cpx.server.commonDB.dao.CWmListDocumentTypeDao;
import de.lb.cpx.server.commonDB.dao.CWmListDraftTypeDao;
import de.lb.cpx.server.commonDB.dao.CWmListMdkStateDao;
import de.lb.cpx.server.commonDB.dao.CWmListProcessResultDao;
import de.lb.cpx.server.commonDB.dao.CWmListProcessTopicDao;
import de.lb.cpx.server.commonDB.dao.CWmListReminderSubjectDao;
import de.lb.cpx.server.commonDB.dao.CdbUserRolesDao;
import de.lb.cpx.server.commonDB.model.CBaserate;
import de.lb.cpx.server.commonDB.model.CBookmarks;
import de.lb.cpx.server.commonDB.model.CBookmarksCustomer;
import de.lb.cpx.server.commonDB.model.CCatalogIF;
import de.lb.cpx.server.commonDB.model.CDeadline;
import de.lb.cpx.server.commonDB.model.CDrafts;
import de.lb.cpx.server.commonDB.model.CDrgCatalog;
import de.lb.cpx.server.commonDB.model.CHospital;
import de.lb.cpx.server.commonDB.model.CMdk;
import de.lb.cpx.server.commonDB.model.CMdkAuditquota;
import de.lb.cpx.server.commonDB.model.CMdkAuditreason;
import de.lb.cpx.server.commonDB.model.CMdkNegotiableAuditquota;
import de.lb.cpx.server.commonDB.model.CPeppCatalog;
import de.lb.cpx.server.commonDB.model.CSupplementaryFee;
import de.lb.cpx.server.commonDB.model.CTextTemplate;
import de.lb.cpx.server.commonDB.model.CWmListActionSubject;
import de.lb.cpx.server.commonDB.model.CWmListDocumentType;
import de.lb.cpx.server.commonDB.model.CWmListDraftType;
import de.lb.cpx.server.commonDB.model.CWmListMdkState;
import de.lb.cpx.server.commonDB.model.CWmListProcessResult;
import de.lb.cpx.server.commonDB.model.CWmListProcessTopic;
import de.lb.cpx.server.commonDB.model.CWmListReminderSubject;
import de.lb.cpx.server.commonDB.model.CdbUserRoles;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author sklarow
 */
@Stateless
public class MasterDataBeanService implements MasterDataBeanServiceRemote {

    @Inject
    private CBaserateDao cBaserateDao;

    @Inject
    private CMdkAuditreasonDao cMdkAuditreasonDao;

    @Inject
    private CDeadlineDao cDeadlineDao;

    @Inject
    private CMdkDao cMdkDao;

    @Inject
    private CDraftsDao cDraftsDao;

    @Inject
    private CWmListActionSubjectDao cWmListActionSubjectDao;

    @Inject
    private CWmListMdkStateDao cCWmListMdkStateDao;

    @Inject
    private CWmListDocumentTypeDao cWmListDocumentTypeDao;

    @Inject
    private CWmListProcessTopicDao cWmListProcessTopicDao;

    @Inject
    private CWmListReminderSubjectDao cWmListReminderSubjectDao;

    @Inject
    private CWmListProcessResultDao cWmListProcessResultDao;

    @Inject
    private CDrgCatalogDao cDrgCatalogDao;

    @Inject
    private CHospitalDao cHospitalDao;

    @Inject
    private CSupplementaryFeeDao cSupplementaryFeeDao;

    @Inject
    private CPeppCatalogDao cPeppCatalogDao;

    @Inject
    private CdbUserRolesDao cdbUserRolesDao;

//    @EJB
//    private CrgRulePoolsDao crgRulePoolsDao;
    @Inject
    private CBookmarksCustomerDao cBookmarksCustomerDao;

    @Inject
    private CWmListDraftTypeDao cWmListDraftTypeDao;

    @Inject
    private CTextTemplateDao cTextTemplateDao;

    @Inject
    private CBookmarksDao cBookmarksDao;

    @Inject
    private CTextTemplate2ContextDao cTextTemplate2contextDao;

    @Inject
    private CTextTemplate2UserRoleDao cTextTemplate2UserRoleDao;
    
    @Inject
    private CMdkAuditquotaDao cMdkAuditquotaDao;
    
    @Inject
    private CMdkNegotiableAuditquotaDao cMdkNegotiableAuditquotaDao;
    
    @Inject
    private CInsuranceCompanyDao cInsuranceCompanyDao;

    @Override
    public List<CBaserate> getBaseratesCatalog(String pCountryEn) {
        return cBaserateDao.getEntries(pCountryEn);
    }

    /**
     * Get negotiated default DRG list
     *
     * @param pCountryEn country En
     * @param pStartDate start date for baserates
     * @return default list of DRGs
     */
    @Override
    public List<CBaserate> getFullBaseratesCatalog(String pCountryEn, Date pStartDate) {
        return cBaserateDao.getFullBaseratesCatalog(pCountryEn, pStartDate);
    }

    @Override
    public List<CBaserate> getBaseratesByBaseHoIdent(String pBaseHoIdent) {
        return cBaserateDao.getBaseratesByBaseHoIdent(pBaseHoIdent);
    }

    /**
     * Update the baserate data
     *
     * @param pCBaserate baserate
     * @return state of update process
     */
    @Override
    public boolean updateBaserate(CBaserate pCBaserate) {
        //return cBaserateDao.updateBaserate(pCBaserate);
        return cBaserateDao.updateItem(pCBaserate);
    }

    /**
     * Delete baserate from database
     *
     * @param id baserate id
     * @return boolean value for delete state
     */
    @Override
    public boolean removeBaserateById(Long id) {
        //return cBaserateDao.removeBaserateById(id);
        return cBaserateDao.deleteById(id);
    }

    /**
     * Add new baserate for hospital
     *
     * @param pCBaserate baserate
     * @return id for new baserate
     */
    @Override
    public long addNewBaserate(CBaserate pCBaserate) {
        //return cBaserateDao.addNewBaserate(pCBaserate);
        return cBaserateDao.addNewItem(pCBaserate);
    }

    /**
     * Get baserate by id
     *
     * @param baserateId baserate id
     * @return searched baserate
     */
    @Override
    public CBaserate getBaserateById(Long baserateId) {
        //return cBaserateDao.getBaserateById(baserateId);
        return cBaserateDao.findById(baserateId);
    }

    /**
     * Delete all entries in the table
     *
     * @return deleted rows of table
     */
    @Override
    public int dropAllBaseratesEntries() {
        return cBaserateDao.dropAllEntries();
    }

    /**
     * Import new baserates entries for hospital
     *
     * @param pCBaserateList list of baserates
     * @return sum of importedbaserates
     */
    @Override
    public int importBaserates(List<CBaserate> pCBaserateList) {
        //return cBaserateDao.importBaserate(pCBaserateList);
        return cBaserateDao.importItemList(pCBaserateList);
    }

    /**
     * Get all entries from table of MDK audit reasons
     *
     * @return entries list
     */
    @Override
    public List<CMdkAuditreason> getMdkAuditReasonCatalog() {
        return cMdkAuditreasonDao.getEntries();
    }

    /**
     * Add new mdk audit reason
     *
     * @param pCMdkAuditreason mdk audit reason
     * @return id for new mdk audit reason
     */
    @Override
    public long addNewMdkAuditReason(CMdkAuditreason pCMdkAuditreason) {
        //return cMdkAuditreasonDao.addNewMdkAuditReason(pCMdkAuditreason);
        return cMdkAuditreasonDao.addNewItem(pCMdkAuditreason);
    }

    /**
     * update the mdk audit reason
     *
     * @param pCMdkAuditreason mdk audit reason to update
     * @return state of update process
     */
    @Override
    public boolean updateMdkAuditReason(CMdkAuditreason pCMdkAuditreason) {
        //return cMdkAuditreasonDao.updateMdkAuditReason(pCMdkAuditreason);
        return cMdkAuditreasonDao.updateItem(pCMdkAuditreason);
    }

    /**
     * update the audit reason list
     *
     * @param pCMdkAuditreasonList audit reason list to update
     * @return state of update process
     */
    @Override
    public boolean updateMdkAuditReasonList(List<CMdkAuditreason> pCMdkAuditreasonList) {
        //return cMdkAuditreasonDao.updateMdkAuditReasonList(pCMdkAuditreasonList);
        return cMdkAuditreasonDao.updateItemList(pCMdkAuditreasonList);
    }

    /**
     * Get mdk audit reason item by id
     *
     * @param pAuditreasonId mdk audit reason id
     * @return searched mdk audit reason
     */
    @Override
    public CMdkAuditreason getMdkAuditreasonById(Long pAuditreasonId) {
        //return cMdkAuditreasonDao.getMdkAuditreasonById(pAuditreasonId);
        return cMdkAuditreasonDao.findById(pAuditreasonId);
    }

    /**
     * Get mdk audit reason item by AR id
     *
     * @param pAuditreasonNumber mdk audit reason AR id
     * @return searched mdk audit reason
     */
    @Override
    public CMdkAuditreason getMdkAuditreasonByNumber(long pAuditreasonNumber) {
        //return cMdkAuditreasonDao.getMdkAuditreasonByNumber(pAuditreasonNumber);
        return cMdkAuditreasonDao.findByInternalId(pAuditreasonNumber);
    }

    /**
     * Get sequence for internal id of audit reason
     *
     * @return sequence id
     */
    @Override
    public long getNextMdkAuditreasonInternalId() {
        //return cMdkAuditreasonDao.getNextMdkAuditreasonInternalId();
        return cMdkAuditreasonDao.getNextInternalId();
    }

    /**
     * Inactivate audit reason by id
     *
     * @param pCMdkAuditreason audit reason to inactivate
     * @return boolean value for inactivate state
     */
    @Override
    public boolean inactivateMdkAuditreason(CMdkAuditreason pCMdkAuditreason) {
        return cMdkAuditreasonDao.inactivateMdkAuditreason(pCMdkAuditreason);
    }

    /**
     * Get all entries from table of deadlines
     *
     * @return entries list
     */
    @Override
    public List<CDeadline> getDeadlinesCatalog() {
        return cDeadlineDao.getEntries();
    }

    /**
     * Get reminder type for deadlines
     *
     * @return entries list
     */
    @Override
    public List<CWmListReminderSubject> getReminderTypeList() {
        return cWmListReminderSubjectDao.getReminderTypeList();
    }

    /**
     * Add new deadline
     *
     * @param pCDeadline deadline
     * @return id for new deadlines
     */
    @Override
    public long addNewDeadline(CDeadline pCDeadline) {
        //return cDeadlineDao.addNewDeadline(pCDeadline);
        return cDeadlineDao.addNewItem(pCDeadline);
    }

    /**
     * update the deadline
     *
     * @param pCDeadline deadline to update
     * @return state of update process
     */
    @Override
    public boolean updateDeadline(CDeadline pCDeadline) {
        //return cDeadlineDao.updateDeadline(pCDeadline);
        return cDeadlineDao.updateItem(pCDeadline);
    }

    /**
     * Get deadline item by id
     *
     * @param pDeadlineId deadline id
     * @return searched deadline
     */
    @Override
    public CDeadline getDeadlineById(Long pDeadlineId) {
        //return cDeadlineDao.getDeadlineById(pDeadlineId);
        return cDeadlineDao.findById(pDeadlineId);
    }

    /**
     * Delete deadline from database
     *
     * @param id deadline id
     * @return boolean value for delete state
     */
    @Override
    public boolean removeDeadlineById(Long id) {
        //return cDeadlineDao.removeDeadlineById(id);
        return cDeadlineDao.deleteById(id);
    }

    /**
     * Get all entries of MDK master data
     *
     * @param pCountryEn name of country
     * @return list of MDK master data
     */
    @Override
    public List<CMdk> getMdkMasterDataCatalog(String pCountryEn) {
        return cMdkDao.getEntries(pCountryEn);
    }

    /**
     * Get all entries of MDK master data
     *
     * @param pCountryEn name of country
     * @return list of MDK master data
     */
    @Override
    public List<CMdk> getMdkDataCatalog(String pCountryEn) {
        return cMdkDao.getMdkEntries(pCountryEn);
    }

//    @Override
//    public List<CBaserate> getBaseratesByBaseHoIdent(String pBaseHoIdent) {
//        return cBaserateDao.getBaseratesByBaseHoIdent(pBaseHoIdent);
//    }
    /**
     * Update the MDK master data
     *
     * @param pCMdk mdk master data item
     * @return state of update process
     */
    @Override
    public boolean updateMdkItem(CMdk pCMdk) {
        //return cMdkDao.updateMdkItem(pCMdk);
        return cMdkDao.updateItem(pCMdk);
    }

    /**
     * Delete MDK master data item by id
     *
     * @param id mdk item id
     * @return boolean value for delete state
     */
    @Override
    public boolean removeMdkItemById(Long id) {
        //return cMdkDao.removeMdkItemById(id);
        return cMdkDao.deleteById(id);
    }

    /**
     * Add new MDK master data item
     *
     * @param pCMdk master data item
     * @return id for new mdk item
     */
    @Override
    public long addNewMdkItem(CMdk pCMdk) {
        //return cMdkDao.addNewMdkItem(pCMdk);
        return cMdkDao.addNewItem(pCMdk);
    }

    /**
     * Get MDK master data item by id
     *
     * @param pMdkItemId mdk item id
     * @return searched mdk item
     */
    @Override
    public CMdk getMdkItemById(Long pMdkItemId) {
        //return cMdkDao.getMdkItemById(pMdkItemId);
        return cMdkDao.findById(pMdkItemId);
    }

    /**
     * Get sequence for internal id of mdk
     *
     * @return sequence id
     */
    @Override
    public long getNextMdkInternalId() {
        //return cMdkDao.getNextMdkInternalId();
        return cMdkDao.getNextInternalId();
    }

    /**
     * Delete all entries in the MDK master data table
     *
     * @param pCountryEn name of country
     * @return deleted rows of table
     */
    @Override
    public int dropAllMdkMasterDataEntries(CountryEn pCountryEn) {
        return cMdkDao.dropEntries(pCountryEn);
    }

    /**
     * Import new MDK master data entries
     *
     * @param pCMdk list of mdk master data
     * @return sum of imported mdk items
     */
    @Override
    public int importMdkMasterData(List<CMdk> pCMdk) {
        //return cMdkDao.importMdkMasterData(pCMdk);
        return cMdkDao.importItemList(pCMdk);
    }

    /**
     * Import new MDKs entries in database
     *
     * @param pCMdkList list of MDKs
     * @return number of new MDKs
     */
    @Override
    public int importMdkList(List<CMdk> pCMdkList) {
        //return cMdkDao.importMdkList(pCMdkList);
        final boolean result = cMdkDao.updateItemList(pCMdkList);
        return pCMdkList.size();
        //return cMdkDao.updateItemList(pCMdkList);
    }

    /**
     * Delete all centrally based MDKs in the table
     *
     * @return deleted rows
     */
    @Override
    public int dropCentrallyBasedMdks() {
        return cMdkDao.dropCentrallyBasedMdks();
    }

    /**
     * Get last mdk ident of user definded mdk
     *
     * @return ident of user definded mdk
     */
    @Override
    public Long getLastUserDefinedMDK() {
        return cMdkDao.getLastUserDefinedMDK();
    }

    /**
     * Get all entries from C_DRAFTS table
     *
     * @return List of drafts
     */
    @Override
    public List<CDrafts> getAllDraftsEntries() {
        return cDraftsDao.getAllDraftsEntries();
    }

    /**
     * Update the ms word template
     *
     * @param pCDrafts ms word template
     * @return state of update process
     */
    @Override
    public boolean updateWordTemplate(CDrafts pCDrafts) {
        //return cDraftsDao.updateWordTemplate(pCDrafts);
        return cDraftsDao.updateItem(pCDrafts);
    }

    /**
     * Delete ms word template by id
     *
     * @param id ms word template id
     * @return boolean value for delete state
     */
    @Override
    public boolean removeWordTemplateById(Long id) {
        //return cDraftsDao.removeWordTemplateById(id);
        return cDraftsDao.deleteById(id);
    }

    /**
     * Get ms word template by id
     *
     * @param pCDraftsId mdk item id
     * @return searched mdk item
     */
    @Override
    public CDrafts getWordTemplateById(Long pCDraftsId) {
        //return cDraftsDao.getWordTemplateById(pCDraftsId);
        return cDraftsDao.findById(pCDraftsId);
    }

    /**
     * Add new ms word template
     *
     * @param pCDrafts new ms word template
     * @return id for new new ms word template
     */
    @Override
    public long addNewWordTemplate(CDrafts pCDrafts) {
        //return cDraftsDao.addNewWordTemplate(pCDrafts);
        return cDraftsDao.addNewItem(pCDrafts);
    }

    /**
     * Get all entries from table C_WM_LIST_ACTION_SUBJECT
     *
     * @return entries list
     */
    @Override
    public List<CWmListActionSubject> getWmListActionSubjectCatalog() {
        return cWmListActionSubjectDao.getWmListActionSubjectCatalog();
    }

    /**
     * Add new action subject
     *
     * @param pCWmListActionSubject action subject
     * @return id for new action subject
     */
    @Override
    public long addNewActionSubject(CWmListActionSubject pCWmListActionSubject) {
        //return cWmListActionSubjectDao.addNewActionSubject(pCWmListActionSubject);
        return cWmListActionSubjectDao.addNewItem(pCWmListActionSubject);
    }

    /**
     * update the action subject
     *
     * @param pCWmListActionSubject action subject to update
     * @return state of update process
     */
    @Override
    public boolean updateActionSubject(CWmListActionSubject pCWmListActionSubject) {
        //return cWmListActionSubjectDao.updateActionSubject(pCWmListActionSubject);
        return cWmListActionSubjectDao.updateItem(pCWmListActionSubject);
    }

    /**
     * update the action subject list
     *
     * @param pCWmListActionSubjectList action subject list to update
     * @return state of update process
     */
    @Override
    public boolean updateActionSubjectList(List<CWmListActionSubject> pCWmListActionSubjectList) {
        //return cWmListActionSubjectDao.updateActionSubjectList(pCWmListActionSubjectList);
        return cWmListActionSubjectDao.updateItemList(pCWmListActionSubjectList);
    }

    /**
     * Get action subject item by id
     *
     * @param pCWmListActionSubjectId action subject id
     * @return searched action subject
     */
    @Override
    public CWmListActionSubject getActionSubjectById(Long pCWmListActionSubjectId) {
        //return cWmListActionSubjectDao.getActionSubjectById(pCWmListActionSubjectId);
        return cWmListActionSubjectDao.findById(pCWmListActionSubjectId);
    }

    /**
     * Inactivate action subject by id
     *
     * @param pCWmListActionSubject action subject to inactivate
     * @return boolean value for inactivate state
     */
    @Override
    public boolean inactivateActionSubject(CWmListActionSubject pCWmListActionSubject) {
        return cWmListActionSubjectDao.inactivateActionSubject(pCWmListActionSubject);
    }

    /**
     * Get sequence for internal id of action subject
     *
     * @return sequence id
     */
    @Override
    public long getNextActionSubjectInternalId() {
        //return cWmListActionSubjectDao.getNextActionSubjectInternalId();
        return cWmListActionSubjectDao.getNextInternalId();
    }

    /**
     * Get all entries from table C_WM_LIST_MDK_STATE
     *
     * @return entries list
     */
    @Override
    public List<CWmListMdkState> getWmListMdkStateCatalog() {
        return cCWmListMdkStateDao.getWmListMdkStateCatalog();
    }

    /**
     * Add new MDK state
     *
     * @param pCWmListMdkState MDK state
     * @return id for new MDK state
     */
    @Override
    public long addNewMdkState(CWmListMdkState pCWmListMdkState) {
        //return cCWmListMdkStateDao.addNewMdkState(pCWmListMdkState);
        return cCWmListMdkStateDao.addNewItem(pCWmListMdkState);
    }

    /**
     * update the MDK state
     *
     * @param pCWmListMdkState MDK state to update
     * @return state of update process
     */
    @Override
    public boolean updateMdkState(CWmListMdkState pCWmListMdkState) {
        //return cCWmListMdkStateDao.updateMdkState(pCWmListMdkState);
        return cCWmListMdkStateDao.updateItem(pCWmListMdkState);
    }

    /**
     * update the MDK state list
     *
     * @param pCWmListMdkStateList MDK state list to update
     * @return state of update process
     */
    @Override
    public boolean updateMdkStateList(List<CWmListMdkState> pCWmListMdkStateList) {
        //return cCWmListMdkStateDao.updateMdkStateList(pCWmListMdkStateList);
        return cCWmListMdkStateDao.updateItemList(pCWmListMdkStateList);
    }

    /**
     * Get MDK state item by id
     *
     * @param pCWmListMdkStateId MDK state id
     * @return searched MDK state
     */
    @Override
    public CWmListMdkState getMdkStateById(Long pCWmListMdkStateId) {
        //return cCWmListMdkStateDao.getMdkStateById(pCWmListMdkStateId);
        return cCWmListMdkStateDao.findById(pCWmListMdkStateId);
    }

    /**
     * Inactivate MDK state by id
     *
     * @param pCWmListMdkState MDK state to inactivate
     * @return boolean value for inactivate state
     */
    @Override
    public boolean inactivateMdkState(CWmListMdkState pCWmListMdkState) {
        return cCWmListMdkStateDao.inactivateMdkState(pCWmListMdkState);
    }

    /**
     * Get sequence for internal id of MDK state
     *
     * @return sequence id
     */
    @Override
    public long getNextMdkStateInternalId() {
        //return cCWmListMdkStateDao.getNextMdkStateInternalId();
        return cCWmListMdkStateDao.getNextInternalId();
    }

    /**
     * Get all entries from table C_WM_LIST_DOCUMENT_TYPE
     *
     * @return entries list
     */
    @Override

    public List<CWmListDocumentType> getWmListDocumentTypeCatalog() {
        return cWmListDocumentTypeDao.getWmListDocumentTypeCatalog();
    }

    /**
     * Add new document type
     *
     * @param pCWmListDocumentType document type
     * @return id for new document type
     */
    @Override
    public long addNewDocumentType(CWmListDocumentType pCWmListDocumentType) {
        //return cWmListDocumentTypeDao.addNewDocumentType(pCWmListDocumentType);
        return cWmListDocumentTypeDao.addNewItem(pCWmListDocumentType);
    }

    /**
     * update the document type
     *
     * @param pCWmListDocumentType document type to update
     * @return state of update process
     */
    @Override
    public boolean updateDocumentType(CWmListDocumentType pCWmListDocumentType) {
        //return cWmListDocumentTypeDao.updateDocumentType(pCWmListDocumentType);
        return cWmListDocumentTypeDao.updateItem(pCWmListDocumentType);
    }

    /**
     * update the document type list
     *
     * @param pCWmListDocumentTypeList document type list to update
     * @return state of update process
     */
    @Override
    public boolean updateDocumentTypeList(List<CWmListDocumentType> pCWmListDocumentTypeList) {
        //return cWmListDocumentTypeDao.updateDocumentTypeList(pCWmListDocumentTypeList);
        return cWmListDocumentTypeDao.updateItemList(pCWmListDocumentTypeList);
    }

    /**
     * Get document type item by id
     *
     * @param pCWmListDocumentTypeId document type id
     * @return searched document type
     */
    @Override
    public CWmListDocumentType getDocumentTypeById(Long pCWmListDocumentTypeId) {
        //return cWmListDocumentTypeDao.getDocumentTypeById(pCWmListDocumentTypeId);
        return cWmListDocumentTypeDao.findById(pCWmListDocumentTypeId);
    }

    /**
     * Inactivate document type by id
     *
     * @param pCWmListDocumentType document type to inactivate
     * @return boolean value for inactivate state
     */
    @Override
    public boolean inactivateDocumentType(CWmListDocumentType pCWmListDocumentType) {
        return cWmListDocumentTypeDao.inactivateDocumentType(pCWmListDocumentType);
    }

    /**
     * Get sequence for internal id of document type
     *
     * @return sequence id
     */
    @Override
    public long getNextDocumentTypeInternalId() {
        //return cWmListDocumentTypeDao.getNextDocumentTypeInternalId();
        return cWmListDocumentTypeDao.getNextInternalId();
    }

    /**
     * Get all entries from table C_WM_LIST_PROCESS_TOPIC
     *
     * @return entries list
     */
    @Override
    public List<CWmListProcessTopic> getWmListProcessTopicCatalog() {
        return cWmListProcessTopicDao.getWmListProcessTopicCatalog();
    }

    /**
     * Add new process topic
     *
     * @param pCWmListProcessTopic process topic
     * @return id for new process topic
     */
    @Override
    public long addNewProcessTopic(CWmListProcessTopic pCWmListProcessTopic) {
        //return cWmListProcessTopicDao.addNewProcessTopic(pCWmListProcessTopic);
        return cWmListProcessTopicDao.addNewItem(pCWmListProcessTopic);
    }

    /**
     * update the process topic
     *
     * @param pCWmListProcessTopic process topic to update
     * @return state of update process
     */
    @Override
    public boolean updateProcessTopic(CWmListProcessTopic pCWmListProcessTopic) {
        //return cWmListProcessTopicDao.updateProcessTopic(pCWmListProcessTopic);
        return cWmListProcessTopicDao.updateItem(pCWmListProcessTopic);
    }

    /**
     * update the process topic list
     *
     * @param pCWmListProcessTopicList process topic list to update
     * @return state of update process
     */
    @Override
    public boolean updateProcessTopicList(List<CWmListProcessTopic> pCWmListProcessTopicList) {
        //return cWmListProcessTopicDao.updateProcessTopicList(pCWmListProcessTopicList);
        return cWmListProcessTopicDao.updateItemList(pCWmListProcessTopicList);
    }

    /**
     * Get process topic item by id
     *
     * @param pCWmListProcessTopicId process topic id
     * @return searched process topic
     */
    @Override
    public CWmListProcessTopic getProcessTopicById(Long pCWmListProcessTopicId) {
        //return cWmListProcessTopicDao.getProcessTopicById(pCWmListProcessTopicId);
        return cWmListProcessTopicDao.findById(pCWmListProcessTopicId);
    }

    @Override
    public CWmListProcessTopic getProcessTopicByIdent(Long pIdent) {
        //return cWmListProcessTopicDao.findByIdent(pIdent);
        return cWmListProcessTopicDao.findByInternalId(pIdent);
    }

    /**
     * Inactivate process topic by id
     *
     * @param pCWmListProcessTopic process topic to inactivate
     * @return boolean value for inactivate state
     */
    @Override
    public boolean inactivateProcessTopic(CWmListProcessTopic pCWmListProcessTopic) {
        return cWmListProcessTopicDao.inactivateProcessTopic(pCWmListProcessTopic);
    }

    /**
     * Get sequence for internal id of process topi
     *
     * @return sequence id
     */
    @Override
    public long getNextProcessTopicInternalId() {
        //return cWmListProcessTopicDao.getNextProcessTopicInternalId();
        return cWmListProcessTopicDao.getNextInternalId();
    }

    /**
     * Get all entries from table C_WM_LIST_REMINDER_SUBJECT
     *
     * @return entries list
     */
    @Override
    public List<CWmListReminderSubject> getWmListReminderSubjectCatalog() {
        return cWmListReminderSubjectDao.getWmListReminderSubjectCatalog();
    }

    /**
     * Add new reminder subject
     *
     * @param pCWmListReminderSubject reminder subject
     * @return id for new reminder subject
     */
    @Override
    public long addNewReminderSubject(CWmListReminderSubject pCWmListReminderSubject) {
        //return cWmListReminderSubjectDao.addNewReminderSubject(pCWmListReminderSubject);
        return cWmListReminderSubjectDao.addNewItem(pCWmListReminderSubject);
    }

    /**
     * update the reminder subject
     *
     * @param pCWmListReminderSubject reminder subject to update
     * @return state of update process
     */
    @Override
    public boolean updateReminderSubject(CWmListReminderSubject pCWmListReminderSubject) {
        //return cWmListReminderSubjectDao.updateReminderSubject(pCWmListReminderSubject);
        return cWmListReminderSubjectDao.updateItem(pCWmListReminderSubject);
    }

    /**
     * update the reminder subject list
     *
     * @param pCWmListReminderSubjectList reminder subject list to update
     * @return state of update process
     */
    @Override
    public boolean updateReminderSubjectList(List<CWmListReminderSubject> pCWmListReminderSubjectList) {
        //return cWmListReminderSubjectDao.updateReminderSubjectList(pCWmListReminderSubjectList);
        return cWmListReminderSubjectDao.updateItemList(pCWmListReminderSubjectList);
    }

    /**
     * Get reminder subject item by id
     *
     * @param pCWmListReminderSubjectId reminder subject id
     * @return searched reminder subject
     */
    @Override
    public CWmListReminderSubject getReminderSubjectById(Long pCWmListReminderSubjectId) {
        //return cWmListReminderSubjectDao.getReminderSubjectById(pCWmListReminderSubjectId);
        return cWmListReminderSubjectDao.findById(pCWmListReminderSubjectId);
    }

    /**
     * Inactivate reminder subject by id
     *
     * @param pCWmListReminderSubject reminder subject to inactivate
     * @return boolean value for inactivate state
     */
    @Override
    public boolean inactivateReminderSubject(CWmListReminderSubject pCWmListReminderSubject) {
        return cWmListReminderSubjectDao.inactivateReminderSubject(pCWmListReminderSubject);
    }

    /**
     * Get sequence for internal id of reminder subject
     *
     * @return sequence id
     */
    @Override
    public long getNextReminderSubjectInternalId() {
        //return cWmListReminderSubjectDao.getNextReminderSubjectInternalId();
        return cWmListReminderSubjectDao.getNextInternalId();
    }

    /**
     * Get all entries from table C_WM_LIST_PROCESS_RESULT
     *
     * @return entries list
     */
    @Override
    public List<CWmListProcessResult> getWmListProcessResultCatalog() {
        return cWmListProcessResultDao.getWmListProcessResultCatalog();
    }

    /**
     * Add new process result
     *
     * @param pCWmListProcessResult process result
     * @return id for new process result
     */
    @Override
    public long addNewProcessResult(CWmListProcessResult pCWmListProcessResult) {
        //return cWmListProcessResultDao.addNewProcessResult(pCWmListProcessResult);
        return cWmListProcessResultDao.addNewItem(pCWmListProcessResult);
    }

    /**
     * update the process result
     *
     * @param pCWmListProcessResult process result to update
     * @return state of update process
     */
    @Override
    public boolean updateProcessResult(CWmListProcessResult pCWmListProcessResult) {
        //return cWmListProcessResultDao.updateProcessResult(pCWmListProcessResult);
        return cWmListProcessResultDao.updateItem(pCWmListProcessResult);
    }

    /**
     * update the process result list
     *
     * @param pCWmListProcessResultList process result list to update
     * @return state of update process
     */
    @Override
    public boolean updateProcessResultList(List<CWmListProcessResult> pCWmListProcessResultList) {
        //return cWmListProcessResultDao.updateProcessResultList(pCWmListProcessResultList);
        return cWmListProcessResultDao.updateItemList(pCWmListProcessResultList);
    }

    /**
     * Get process result item by id
     *
     * @param pCWmListProcessResultId process result id
     * @return searched process result
     */
    @Override
    public CWmListProcessResult getProcessResultById(Long pCWmListProcessResultId) {
        //return cWmListProcessResultDao.getProcessResultById(pCWmListProcessResultId);
        return cWmListProcessResultDao.findById(pCWmListProcessResultId);
    }

    /**
     * Inactivate process result by id
     *
     * @param pCWmListProcessResult process result to inactivate
     * @return boolean value for inactivate state
     */
    @Override
    public boolean inactivateProcessResult(CWmListProcessResult pCWmListProcessResult) {
        return cWmListProcessResultDao.inactivateProcessResult(pCWmListProcessResult);
    }

    /**
     * Get sequence for internal id of process result
     *
     * @return sequence id
     */
    @Override
    public long getNextProcessResultInternalId() {
        //return cWmListProcessResultDao.getNextProcessResultInternalId();
        return cWmListProcessResultDao.getNextInternalId();
    }

    /**
     * Get all distinct years of DRG catalog
     *
     * @param pCountryEn "de"
     * @return list of years of DRG catalog
     */
    @Override
    public List<Integer> getDrgYearList(CountryEn pCountryEn) {
        return cDrgCatalogDao.getDrgYearList(pCountryEn);
    }

    /**
     * Get all hospital ident numbers (IKZ)
     *
     * @param pCountryEn "de"
     * @return map of hospital ident numbers (IKZ)
     */
    @Override
    public List<String> getHosIdentList(CountryEn pCountryEn) {
        return cHospitalDao.getHosIdentList(pCountryEn);
    }

    /**
     * Get already entered hospital ident numbers (IKZ) in DRG catalog
     *
     * @param pCountryEn "de"
     * @param pSupplYear selected year
     * @return map of hospital ident numbers (IKZ)
     */
    @Override
    public List<String> getExistingHosIdentDrgList(CountryEn pCountryEn, int pSupplYear) {
        return cDrgCatalogDao.getExistingHosIdentDrgList(pCountryEn, pSupplYear);
    }

    /**
     * Get negotiated DRG catalog
     *
     * @param pDrgYear selected yaer
     * @param pHosIdent selected IKZ
     * @param pCountryEn "de"
     * @return DRG catalog for selected year and IKZ
     */
    @Override
    public List<CDrgCatalog> getDrgCatalogByYearAndIkz(int pDrgYear, String pHosIdent, CountryEn pCountryEn) {
        return cDrgCatalogDao.getDrgCatalogByYearAndIkz(pDrgYear, pHosIdent, pCountryEn);
    }

    /**
     * Get DRG catalog
     *
     * @param pDrgYear selected yaer
     * @param pHosIdent selected IKZgetDefaultAllSuplFeeList
     * @param pCountryEn "de"
     * @return DRG catalog for selected year and IKZ
     */
    @Override
    public List<CDrgCatalog> getAllDrgCatalogByYearAndIkz(int pDrgYear, String pHosIdent, CountryEn pCountryEn) {
        return cDrgCatalogDao.getAllDrgCatalogByYearAndIkz(pDrgYear, pHosIdent, pCountryEn);
    }

    /**
     * Add new negotiated DRG item
     *
     * @param pCDrgCatalog negotiated DRG item
     * @return new negotiated DRG item
     */
    @Override
    public CDrgCatalog addNegotiatedDrg(CDrgCatalog pCDrgCatalog) {
        //return cDrgCatalogDao.addNegotiatedDrg(pCDrgCatalog);
        cDrgCatalogDao.addNewItem(pCDrgCatalog);
        return pCDrgCatalog;
    }

    /**
     * update negotiated DRG item
     *
     * @param pCDrgCatalog negotiated DRG item
     * @return state of update process
     */
    @Override
    public boolean updateNegotiatedDrg(CDrgCatalog pCDrgCatalog) {
        //return cDrgCatalogDao.updateNegotiatedDrg(pCDrgCatalog);
        return cDrgCatalogDao.updateItem(pCDrgCatalog);
    }

    /**
     * Delete DRG by ID
     *
     * @param id item id
     * @return boolean value for delete state
     */
    @Override
    public boolean removeDrgById(Long id) {
        //return cDrgCatalogDao.removeDrgById(id);
        return cDrgCatalogDao.deleteById(id);
    }

    /**
     * Get negotiated default DRG list
     *
     * @param pCountryEn catalog language
     * @param pDrgYear catalog year
     * @return List of drgs from catalog
     */
    @Override
    public List<CCatalogIF> getDefaultNegotiatedDrgList(CountryEn pCountryEn, int pDrgYear) {
        return cDrgCatalogDao.getDefaultNegotiatedDrgList(pCountryEn, pDrgYear);
    }

    /**
     * Get all default DRG list
     *
     * @param pCountryEn country
     * @param pDrgYear year
     * @return list of drg catalogs
     */
    @Override
    public List<CDrgCatalog> getDefaultAllDrgList(CountryEn pCountryEn, int pDrgYear) {
        return cDrgCatalogDao.getDefaultAllDrgList(pCountryEn, pDrgYear);
    }

    /**
     * Get all distinct years of supplementary fee catalog
     *
     * @param pCountryEn "de"
     * @return list of years of supplementary fee
     */
    @Override
    public List<Integer> getFeeYearList(CountryEn pCountryEn) {
        return cSupplementaryFeeDao.getFeeYearList(pCountryEn);
    }

    /**
     * Get negotiated supplementary fee catalog
     *
     * @param pFeeYear selected yaer
     * @param pHosIdent selected IKZ
     * @param pCountryEn "de"
     * @param pSupplTypeEn catalog type
     * @return fee catalog for selected year, type and IKZ
     */
    @Override
    public List<CSupplementaryFee> getFeeCatalogByYearIkzAndType(int pFeeYear, String pHosIdent, CountryEn pCountryEn, SupplFeeTypeEn pSupplTypeEn) {
        return cSupplementaryFeeDao.getFeeCatalogByYearIkzAndType(pFeeYear, pHosIdent, pCountryEn, pSupplTypeEn);
    }

    /**
     * Get all supplementary fee catalog
     *
     * @param pFeeYear selected yaer
     * @param pHosIdent selected IKZ
     * @param pCountryEn "de"
     * @param pSupplTypeEn catalog type
     * @return fee catalog for selected year, type and IKZ
     */
    @Override
    public List<CSupplementaryFee> getAllFeeCatalogByYearIkzAndType(int pFeeYear, String pHosIdent, CountryEn pCountryEn, SupplFeeTypeEn pSupplTypeEn) {
        return cSupplementaryFeeDao.getAllFeeCatalogByYearIkzAndType(pFeeYear, pHosIdent, pCountryEn, pSupplTypeEn);
    }

    /**
     * Add new supplementary fee item
     *
     * @param pCSupplementaryFee negotiated fee item
     * @return new negotiated fee item
     */
    @Override
    public CSupplementaryFee addNegotiatedSuplFee(CSupplementaryFee pCSupplementaryFee) {
        return cSupplementaryFeeDao.addNegotiatedSuplFee(pCSupplementaryFee);
    }

    /**
     * update negotiated supplementary fee item
     *
     * @param pCSupplementaryFee negotiated fee item
     * @return id for updated negotiated fee item
     */
    @Override
    public boolean updateNegotiatedFee(CSupplementaryFee pCSupplementaryFee) {
        //return cSupplementaryFeeDao.updateNegotiatedFee(pCSupplementaryFee);
        return cSupplementaryFeeDao.updateItem(pCSupplementaryFee);
    }

    /**
     * Get negotiated default supplementary fee list
     *
     * @param pCountryEn catalog language
     * @param pSupplYear catalog year
     * @param pSupplTypeEn catalog type
     * @return fee catalog for selected year, type and IKZ
     */
    @Override
    public List<CSupplementaryFee> getDefaultNegotiatedSuplFeeList(CountryEn pCountryEn, int pSupplYear, SupplFeeTypeEn pSupplTypeEn) {
        return cSupplementaryFeeDao.getDefaultNegotiatedSuplFeeList(pCountryEn, pSupplYear, pSupplTypeEn);
    }
    
    public List<CCatalogIF> getDefaultNegoSuplFeeList(CountryEn pCountryEn, int pSupplYear, SupplFeeTypeEn pSupplTypeEn){
        return cSupplementaryFeeDao.getDefaultNegoSuplFeeList(pCountryEn, pSupplYear, pSupplTypeEn);
    }

    /**
     * Get all default supplementary fee list
     *
     * @param pCountryEn catalog language
     * @param pSupplYear catalog year
     * @param pSupplTypeEn catalog type
     * @return fee catalog for selected year, type and IKZ
     */
    @Override
    public List<CSupplementaryFee> getDefaultAllSuplFeeList(CountryEn pCountryEn, int pSupplYear, SupplFeeTypeEn pSupplTypeEn) {
        return cSupplementaryFeeDao.getDefaultAllSuplFeeList(pCountryEn, pSupplYear, pSupplTypeEn);
    }

    /**
     * Get already entered hospital ident numbers (IKZ) in supplementary fee
     * catalog
     *
     * @param pCountryEn catalog language
     * @param pSupplYear selected year
     * @param pSupplTypeEn catalog type
     * @return map of hospital ident numbers (IKZ)
     */
    @Override
    public List<String> getExistingHosIdentFeeList(CountryEn pCountryEn, int pSupplYear, SupplFeeTypeEn pSupplTypeEn) {
        return cSupplementaryFeeDao.getExistingHosIdentFeeList(pCountryEn, pSupplYear, pSupplTypeEn);
    }

    /**
     * Get already entered hospital ident numbers (IKZ) in supplementary fee for
     * PEPP catalog
     *
     * @param pCountryEn catalog language
     * @param pSupplYear selected year
     * @param pSupplTypeEnDf catalog type day fee
     * @param pSupplTypeEnSp catalog type suplementary fee for PEPP
     * @return map of hospital ident numbers (IKZ)
     */
    @Override
    public List<String> getExistingHosIdentSupplFeePeppList(CountryEn pCountryEn, int pSupplYear, SupplFeeTypeEn pSupplTypeEnDf, SupplFeeTypeEn pSupplTypeEnSp) {
        return cSupplementaryFeeDao.getExistingHosIdentSupplFeePeppList(pCountryEn, pSupplYear, pSupplTypeEnDf, pSupplTypeEnSp);
    }

    /**
     * Delete supl fee by ID
     *
     * @param id item id
     * @return boolean value for delete state
     */
    @Override
    public boolean removeSupplFeeById(Long id) {
        //return cSupplementaryFeeDao.removeSupplFeeById(id);
        return cSupplementaryFeeDao.deleteById(id);
    }

    /**
     * Get all distinct years of PEPP catalog
     *
     * @param pCountryEn "de"
     * @return list of years of PEPP catalog
     */
    @Override
    public List<Integer> getPeppYearList(CountryEn pCountryEn) {
        return cPeppCatalogDao.getPeppYearList(pCountryEn);
    }

    /**
     * Get negotiated PEPP catalog
     *
     * @param pPeppYear selected yaer
     * @param pHosIdent selected IKZ
     * @param pCountryEn "de"
     * @return PEPP catalog for selected year and IKZ
     */
    @Override
    public List<CPeppCatalog> getPeppCatalogByYearAndIkz(int pPeppYear, String pHosIdent, CountryEn pCountryEn) {
        return cPeppCatalogDao.getPeppCatalogByYearAndIkz(pPeppYear, pHosIdent, pCountryEn);
    }

    /**
     * Get all PEPP catalog
     *
     * @param pPeppYear selected yaer
     * @param pHosIdent selected IKZ
     * @param pCountryEn "de"
     * @return PEPP catalog for selected year and IKZ
     */
    @Override
    public List<CPeppCatalog> getAllPeppCatalogByYearAndIkz(int pPeppYear, String pHosIdent, CountryEn pCountryEn) {
        return cPeppCatalogDao.getAllPeppCatalogByYearAndIkz(pPeppYear, pHosIdent, pCountryEn);
    }

    /**
     * Add new negotiated PEPP item
     *
     * @param pCPeppCatalog negotiated PEPP item
     * @return new negotiated PEPP item
     */
    @Override
    public CPeppCatalog addNegotiatedPepp(CPeppCatalog pCPeppCatalog) {
        return cPeppCatalogDao.addNegotiatedPepp(pCPeppCatalog);
    }

    /**
     * update negotiated PEPP item
     *
     * @param pCPeppCatalog negotiated PEPP item
     * @return id for updated negotiated PEPP item
     */
    @Override
    public boolean updateNegotiatedPepp(CPeppCatalog pCPeppCatalog) {
        return cPeppCatalogDao.updateNegotiatedPepp(pCPeppCatalog);
    }

    /**
     * Get negotiated default PEPP list
     *
     * @param pCountryEn catalog language
     * @param pPeppYear catalog year
     * @return List of pepps from catalog
     */
    @Override
    public List<CPeppCatalog> getDefaultNegotiatedPeppList(CountryEn pCountryEn, int pPeppYear) {
        return cPeppCatalogDao.getDefaultNegotiatedPeppList(pCountryEn, pPeppYear);
    }

    /**
     * Get default PEPP list
     *
     * @param pCountryEn catalog language
     * @param pPeppYear catalog year
     * @return list of pepp catalogs
     */
    @Override
    public List<CPeppCatalog> getDefaultAllPeppList(CountryEn pCountryEn, int pPeppYear) {
        return cPeppCatalogDao.getDefaultAllPeppList(pCountryEn, pPeppYear);
    }

    /**
     * Get already entered hospital ident numbers (IKZ) in PEPP catalog
     *
     * @param pCountryEn "de"
     * @param pPeppYear selected year
     * @return map of hospital ident numbers (IKZ)
     */
    @Override
    public List<String> getExistingHosIdentPeppList(CountryEn pCountryEn, int pPeppYear) {
        return cPeppCatalogDao.getExistingHosIdentPeppList(pCountryEn, pPeppYear);
    }

    /**
     * Delete PEPP item from database by id
     *
     * @param id PEPP item id
     * @return boolean value for delete state
     */
    @Override
    public boolean removePeppById(Long id) {
        //return cPeppCatalogDao.removePeppById(id);
        return cPeppCatalogDao.deleteById(id);
    }

    /**
     * Get negotiated PEPPs by key, year and IKZ
     *
     * @param peppPepp selected PEPP
     * @param pValidFrom PEPP valid from
     * @param pValidTo PEPP valid to
     * @param pPeppYear selected yaer
     * @param pHosIdent selected IKZ
     * @param pCountryEn "de"
     * @return PEPP catalog for selected year and IKZ
     */
    @Override
    public List<CPeppCatalog> getPeppByKeyAndIkz(String peppPepp, Date pValidFrom, Date pValidTo, int pPeppYear, String pHosIdent, CountryEn pCountryEn) {
        return cPeppCatalogDao.getPeppByKeyAndIkz(peppPepp, pValidFrom, pValidTo, pPeppYear, pHosIdent, pCountryEn);
    }

    /**
     * Get negotiated PEPPs by key and year
     *
     * @param peppPepp selected PEPP
     * @param pPeppYear selected yaer
     * @param pCountryEn "de"
     * @return PEPP catalog for selected year and IKZ
     */
    @Override
    public List<CPeppCatalog> getPeppByKeyAndYearDef(String peppPepp, int pPeppYear, CountryEn pCountryEn) {
        return cPeppCatalogDao.getPeppByKeyAndYearDef(peppPepp, pPeppYear, pCountryEn);
    }

    /**
     * Get fee keys and description by year
     *
     * @param pCountryEn language
     * @param pYear year
     * @param pSupplFeeType fee type
     * @return map of fee keys and description
     */
    @Override
    public Map<String, String> getFeeKeyByYear(CountryEn pCountryEn, int pYear, String pSupplFeeType) {
        return cSupplementaryFeeDao.getFeeKeyByYear(pCountryEn, pYear, pSupplFeeType);
    }

    /**
     * Get PEPP keys and description by year
     *
     * @param pCountryEn language
     * @param pYear year
     * @return map of PEPP keys and description
     */
    @Override
    public Map<String, String> getPeppKeyByYear(CountryEn pCountryEn, int pYear) {
        return cPeppCatalogDao.getPeppKeyByYear(pCountryEn, pYear);
    }

    /**
     * Get DRG keys and description by year
     *
     * @param pCountryEn language
     * @param pYear year
     * @return map of DRG keys and description
     */
    @Override
    public Map<String, String> getDrgKeyByYear(CountryEn pCountryEn, int pYear) {
        return cDrgCatalogDao.getDrgKeyByYear(pCountryEn, pYear);
    }

    /**
     * Import new PEPP catalog items
     *
     * @param pCPeppCatalog list of items
     * @return number of added PEPP catalog items
     */
    @Override
    public int importPeppCatalog(List<CPeppCatalog> pCPeppCatalog) {
        //return cPeppCatalogDao.importPeppCatalog(pCPeppCatalog);
        return cPeppCatalogDao.importItemList(pCPeppCatalog);
    }

    /**
     * Import new DRG catalog items
     *
     * @param pCDrgCatalog list of items
     * @return number of added DRG catalog items
     */
    @Override
    public int importDrgCatalog(List<CDrgCatalog> pCDrgCatalog) {
        //return cDrgCatalogDao.importDrgCatalog(pCDrgCatalog);
        return cDrgCatalogDao.importItemList(pCDrgCatalog);
    }

    /**
     * Get DRG by ID
     *
     * @param id id of items
     * @return DRG item
     */
    @Override
    public CDrgCatalog getDrgById(Long id) {
        //return cDrgCatalogDao.getDrgById(id);
        return cDrgCatalogDao.findById(id);
    }

    /**
     * Get PEPP by ID
     *
     * @param id id of PEPP
     * @return PEPP item
     */
    @Override
    public CPeppCatalog getPeppById(Long id) {
        //return cPeppCatalogDao.getPeppById(id);
        return cPeppCatalogDao.findById(id);
    }

    /**
     * Drop DRGs custom entries by year
     *
     * @param pYear year
     * @param pCountryEn country
     * @return count of dropped entries
     */
    @Override
    public int dropDrgEntriesByYear(int pYear, CountryEn pCountryEn) {
        return cDrgCatalogDao.dropDrgEntriesByYear(pYear, pCountryEn);
    }

    @Override
   public int dropDrgEntriesByYearAndHospIdentList(int pYear, CountryEn pCountryEn, List<String> pIkzList){
       return cDrgCatalogDao.dropDrgEntriesByYearAndHospIdentList(pYear, pCountryEn, pIkzList);
   }

    @Override   
   public int dropDrgNegoEntriesByYearAndHospIdentList(int pYear, CountryEn pCountryEn, List<String> pIkzList, List<String> pDrgList){
       return cDrgCatalogDao.dropDrgNegoEntriesByYearAndHospIdentList(pYear, pCountryEn, pIkzList, pDrgList);
   }


    /**
     * Import new suppl. fee catalog items
     *
     * @param pCSupplementaryFee list of items
     * @return number of added suppl fee catalog items
     */
    @Override
    public int importSupplFeeCatalog(List<CSupplementaryFee> pCSupplementaryFee) {
        //return cSupplementaryFeeDao.importSupplFeeCatalog(pCSupplementaryFee);
        return cSupplementaryFeeDao.importItemList(pCSupplementaryFee);
    }

    /**
     * Drop suppl. fee custom entries by year
     *
     * @param pYear selected year
     * @param pCountryEn de
     * @param pSupplFeeType suppl fee type
     * @return number of deleted items
     */
    @Override
    public int dropSupplFeeEntriesByYear(int pYear, CountryEn pCountryEn, SupplFeeTypeEn pSupplFeeType) {
        return cSupplementaryFeeDao.dropSupplFeeEntriesByYear(pYear, pCountryEn, pSupplFeeType);
    }

    /**
     * Drop PEPPs custom entries by year
     *
     * @param pYear selected year
     * @param pCountryEn de
     * @return number of deleted items
     */
    @Override
    public int dropPeppEntriesByYear(int pYear, CountryEn pCountryEn) {
        return cPeppCatalogDao.dropPeppEntriesByYear(pYear, pCountryEn);
    }

    /**
     * Create map of role id and role name
     *
     * @return map of roles
     */
    @Override
    public Map<Long, String> createRoleMap() {
        return cdbUserRolesDao.createRoleMap();
    }

    @Override
    public List<CdbUserRoles> getAllUserRoles() {
        return cdbUserRolesDao.findAll();
    }

    /**
     * Get supplementary fee by ID
     *
     * @param id id of supplementary fee
     * @return supplementary fee item
     */
    @Override
    public CSupplementaryFee getSupplFeeById(Long id) {
        //return cSupplementaryFeeDao.getSupplFeeById(id);
        return cSupplementaryFeeDao.findById(id);
    }

    /**
     * Drop DRGs daily fee custom entries by year
     *
     * @param pYear selected year
     * @param pCountryEn de
     * @return number of deleted items
     */
    @Override
    public int dropDrgDailyFeeByYear(int pYear, CountryEn pCountryEn) {
        return cDrgCatalogDao.dropDrgDailyFeeByYear(pYear, pCountryEn);
    }
    public int dropDrgDailyFeeByYearAndHospIdentList(int catalogYear, CountryEn countryEn, List<String> pIkzList){
        return cDrgCatalogDao.dropDrgDailyFeeByYearAndHospIdentList(catalogYear, countryEn, pIkzList);
    } 
    /**
     * Get only DRG main and external department
     *
     * @param pCountryEn de
     * @param pYear selected year
     * @return list of DRG main and external department
     */
    @Override
    public List<CDrgCatalog> getMainAndExternalDepartmentList(CountryEn pCountryEn, int pYear) {
        return cDrgCatalogDao.getMainAndExternalDepartmentList(pCountryEn, pYear);
    }

    /**
     * Get only DRG daily fee
     *
     * @param pCountryEn de
     * @param pYear selected year
     * @return list of DRG daily fee
     */
    @Override
    public List<CCatalogIF> getDailyFeeList(CountryEn pCountryEn, int pYear) {
        return cDrgCatalogDao.getDailyFeeList(pCountryEn, pYear); 
    }
    
    @Override
    public List<CCatalogIF> getNegotiatedDrgsWithIKsList(CountryEn pCountryEn, int pYear) {
        return cDrgCatalogDao.getNegotiatedDrgsWithIKsList(pCountryEn, pYear); 
    }

    /**
     * Create map of rule pools id and rule pools name
     *
     * @param pYear year
     * @return map of rule pools
     */
    @Override
    public Map<Long, String> createRulePoolsMap(int pYear) {
//        return crgRulePoolsDao.createRulePoolsMap(pYear);
        return new HashMap<>();
    }

    /**
     * Get all entries from table of bookmarks customer
     *
     * @return entries list
     */
    @Override
    public List<CBookmarksCustomer> getCustomerBookmarksCatalog() {
        return cBookmarksCustomerDao.getBookmarksCatalog();
    }

    /**
     * Add new customer bookmark
     *
     * @param pCBookmarksCustomer bookmark
     * @return id for new bookmark
     */
    @Override
    public long addNewCustomerBookmark(CBookmarksCustomer pCBookmarksCustomer) {
        //return cBookmarksCustomerDao.addNewBookmark(pCBookmarksCustomer);
        return cBookmarksCustomerDao.addNewItem(pCBookmarksCustomer);
    }

    /**
     * update the customer bookmark
     *
     * @param pCBookmarksCustomer bookmark to update
     * @return state of update process
     */
    @Override
    public boolean updateCustomerBookmark(CBookmarksCustomer pCBookmarksCustomer) {
        //return cBookmarksCustomerDao.updateBookmark(pCBookmarksCustomer);
        return cBookmarksCustomerDao.updateItem(pCBookmarksCustomer);
    }

    /**
     * Get bookmark item by id
     *
     * @param pBookmarkId bookmark id
     * @return searched bookmark
     */
    @Override
    public CBookmarksCustomer getCustomerBookmarkById(Long pBookmarkId) {
        //return cBookmarksCustomerDao.getBookmarkById(pBookmarkId);
        return cBookmarksCustomerDao.findById(pBookmarkId);
    }

    /**
     * Delete bookmark from database
     *
     * @param id bookmark id
     * @return boolean value for delete state
     */
    @Override
    public boolean removeCustomerBookmarkById(Long id) {
        //return cBookmarksCustomerDao.removeBookmarkById(id);
        return cBookmarksCustomerDao.deleteById(id);
    }

    /**
     * Get all entries from table C_WM_LIST_DRAFT_TYPE
     *
     * @return entries list
     */
    @Override
    public List<CWmListDraftType> getWmListDraftTypeCatalog() {
        return cWmListDraftTypeDao.getWmListDraftType();
    }

    /**
     * Add new draft type
     *
     * @param pCWmListDraftType draft type
     * @return id for new draft type
     */
    @Override
    public long addNewDraftType(CWmListDraftType pCWmListDraftType) {
        return cWmListDraftTypeDao.addNewItem(pCWmListDraftType);
    }

    /**
     * Find draft type by id
     *
     * @param id draft type id
     * @return new draft type
     */
    @Override
    public CWmListDraftType findById(long id) {
        return cWmListDraftTypeDao.findById(id);
    }

    /**
     * update the draft type
     *
     * @param pCWmListDraftType draft type to update
     * @return state of update process
     */
    @Override
    public boolean updateDraftType(CWmListDraftType pCWmListDraftType) {
        return cWmListDraftTypeDao.updateItem(pCWmListDraftType);
    }

    /**
     * update the draft type list
     *
     * @param pCWmListDraftTypeList draft type list to update
     * @return state of update process
     */
    @Override
    public boolean updateDraftTypeList(List<CWmListDraftType> pCWmListDraftTypeList) {
        return cWmListDraftTypeDao.updateItemList(pCWmListDraftTypeList);
    }

    /**
     * Get draft type item by id
     *
     * @param pCWmListDraftTypeId draft type id
     * @return searched draft type
     */
    @Override
    public CWmListDraftType getDraftTypeById(Long pCWmListDraftTypeId) {
        return cWmListDraftTypeDao.findById(pCWmListDraftTypeId);
    }

    /**
     * Inactivate draft type by id
     *
     * @param pCWmListDraftType draft type to inactivate
     * @return boolean value for inactivate state
     */
    @Override
    public boolean inactivateDraftType(CWmListDraftType pCWmListDraftType) {
        return cWmListDraftTypeDao.inactivateDraftType(pCWmListDraftType);
    }

    /**
     * Get sequence for internal id of draft type
     *
     * @return sequence id
     */
    @Override
    public long getNextDraftTypeInternalId() {
        return cWmListDraftTypeDao.getNextInternalId();
    }

    /**
     * Get all valid and not deleted items from table C_WM_LIST_DRAFT_TYPE for
     * any category
     *
     * @param pCategoryEn template category
     * @return entries list
     */
    @Override
    public Map<Long, String> getDraftTypesCategoryMap(CategoryEn pCategoryEn) {
        return cWmListDraftTypeDao.getDraftTypesCategoryMap(pCategoryEn);
    }

    /**
     * Get all entries from table of bookmarks
     *
     * @return entries list
     */
    @Override
    public List<CTextTemplate> getTextTemplateCatalog() {
        return cTextTemplateDao.getAllTextTemplateEntries();
    }

    /**
     * Add new bookmark
     *
     * @param pCBookmarksCustomer bookmark
     * @return id for new bookmark
     */
    @Override
    public long addNewTextTemplate(CTextTemplate pCBookmarksCustomer) {
        //return cBookmarksCustomerDao.addNewBookmark(pCBookmarksCustomer);
        return cTextTemplateDao.addNewItem(pCBookmarksCustomer);
    }

    /**
     * update the TextTemplate
     *
     * @param pCTextTemplate TextTemplate to update
     * @return state of update process
     */
    @Override
    public boolean updateTextTemplate(CTextTemplate pCTextTemplate) {
        return cTextTemplateDao.updateItem(pCTextTemplate);
    }

    /**
     * Get TextTemplate item by id
     *
     * @param pTextTemplateId TextTemplate id
     * @return searched TextTemplate
     */
    @Override
    public CTextTemplate getTextTemplateById(Long pTextTemplateId) {
        return cTextTemplateDao.findById(pTextTemplateId);
    }

    /**
     * Delete TextTemplate from database
     *
     * @param id TextTemplate id
     * @return boolean value for delete state
     */
    @Override
    public boolean removeTextTemplateById(Long id) {
        return cTextTemplateDao.deleteById(id);
    }

    @Override
    public void removeTextTemplate(CTextTemplate textTemplate) {
        cTextTemplateDao.attach(textTemplate);
        cTextTemplateDao.remove(textTemplate);
    }

    /**
     * Get all entries from table of bookmarks
     *
     * @return entries list
     */
    @Override
    public List<CBookmarks> getBookmarksCatalog() {
        return cBookmarksDao.getBookmarksCatalog();
    }

    @Override
    public void deleteTextTemplate2Context(long textTemplate2contextId) {
        cTextTemplate2contextDao.deleteById(textTemplate2contextId);
    }

    @Override
    public void deleteTextTemplate2UserRole(long textTemplate2UserRoleId) {
        cTextTemplate2UserRoleDao.deleteById(textTemplate2UserRoleId);
    }

    @Override
    public int dropDrgEntriesByYearAndHospIdent(int pYear, CountryEn pCountryEn, String pHosIdent) {
        return cDrgCatalogDao.dropDrgEntriesByYearAndHosIdent(pYear, pCountryEn, pHosIdent);
    }

    @Override
    public int dropSupplFeeEntriesByYearAndHosIdent(int pYear, CountryEn pCountryEn, SupplFeeTypeEn pSupplFeeType, String pHosIdent) {
        return cSupplementaryFeeDao.dropSupplFeeEntriesByYearAndHosIdent(pYear, pCountryEn, pSupplFeeType, pHosIdent);
    }

    @Override
    public int dropSupplFeeEntriesByYearAndHospIdentList(int pYear, SupplFeeTypeEn pType, CountryEn pCountryEn, List<String> pIkzList){
       return cSupplementaryFeeDao.dropSupplFeeEntriesByYearAndHosIdentList(pYear, pType, pCountryEn, pIkzList);      
    }
     
    @Override
    public int dropPeppEntriesByYearAndHosIdent(int pYear, CountryEn pCountryEn, String pHosIdent) {
        return cPeppCatalogDao.dropPeppEntriesByYearAndHosIdent(pYear, pCountryEn, pHosIdent);
    }
    
    @Override
    public int dropPeppEntriesByYearAndHospIdentList(int pYear, CountryEn pCountryEn, List<String> pIkzList){
        return cPeppCatalogDao.dropPeppEntriesByYearAndHosIdent(pYear, pCountryEn, pIkzList);
    }
    
    @Override
    public int dropDrgDailyFeeByYearAndHosIdent(int pYear, CountryEn pCountryEn, String hosIdent) {
        return cDrgCatalogDao.dropDrgDailyFeeByYearAndHosIdent(pYear, pCountryEn, hosIdent);
    }
    
     @Override
     public List<CCatalogIF> getDefaultI68DrgList(CountryEn countryEn, int pSelectedYear){
          return cDrgCatalogDao.getDefaultI68DrgList(countryEn, pSelectedYear); 
      }
    
        /**
     * Get all entries from table of CMdkAuditquota
     *
     * @return entries list
     */
    @Override
    public List<CMdkAuditquota> getAuditQuotaCatalog(){
        return cMdkAuditquotaDao.getAllMdkAuditQuotaEntries();
    }

    /**
     * Add new Auditquota
     *
     * @param pCMdkAuditquota Auditquota
     * @return id for new Auditquota
     */
    @Override
    public long addNewAuditQuota(CMdkAuditquota pCMdkAuditquota) {
        return cMdkAuditquotaDao.addNewItem(pCMdkAuditquota);
    }

    /**
     * update the Auditquota
     *
     * @param pCMdkAuditquota Auditquota to update
     * @return state of update process
     */
    @Override
    public boolean updateAuditquota(CMdkAuditquota pCMdkAuditquota) {
        return cMdkAuditquotaDao.updateItem(pCMdkAuditquota);
    }

    /**
     * Get Auditquota item by id
     *
     * @param pAuditquotaId Auditquota id
     * @return searched Auditquota
     */
    @Override
    public CMdkAuditquota getAuditquotaById(Long pAuditquotaId) {
        return cMdkAuditquotaDao.findById(pAuditquotaId);
    }

    /**
     * Delete Auditquota from database
     *
     * @param id Auditquota id
     * @return boolean value for delete state
     */
    @Override
    public boolean removeAuditquotaById(Long id) {
        return cMdkAuditquotaDao.deleteById(id);
    }

    @Override
    public void removeAuditquota(CMdkAuditquota pCMdkAuditquota) {
        cMdkAuditquotaDao.attach(pCMdkAuditquota);
        cMdkAuditquotaDao.remove(pCMdkAuditquota);
    }
    
    /**
     * Get all entries from table of CMdkNegotiableAuditquota
     *
     * @return entries list
     */
    @Override
    public List<CMdkNegotiableAuditquota> getNegotiableAuditQuotaCatalog(){
        return cMdkNegotiableAuditquotaDao.getAllMdkNegotiableAuditQuotaEntries();
    }

    /**
     * Add new NegotiableAuditquota
     *
     * @param pCMdkNegotiableAuditquota Auditquota
     * @return id for new Auditquota
     */
    @Override
    public long addNewNegotiableAuditQuota(CMdkNegotiableAuditquota pCMdkNegotiableAuditquota) {
        return cMdkNegotiableAuditquotaDao.addNewItem(pCMdkNegotiableAuditquota);
    }

    /**
     * update the NegotiableAuditquota
     *
     * @param pCMdkNegotiableAuditquota Auditquota to update
     * @return state of update process
     */
    @Override
    public boolean updateNegotiableAuditquota(CMdkNegotiableAuditquota pCMdkNegotiableAuditquota) {
        return cMdkNegotiableAuditquotaDao.updateItem(pCMdkNegotiableAuditquota);
    }

    /**
     * Get NegotiableAuditquota item by id
     *
     * @param pNegotiableAuditquotaId Auditquota id
     * @return searched Auditquota
     */
    @Override
    public CMdkNegotiableAuditquota getNegotiableAuditquotaById(Long pNegotiableAuditquotaId) {
        return cMdkNegotiableAuditquotaDao.findById(pNegotiableAuditquotaId);
    }

    /**
     * Delete NegotiableAuditquota from database
     *
     * @param id NegotiableAuditquota id
     * @return boolean value for delete state
     */
    @Override
    public boolean removeNegotiableAuditquotaById(Long id) {
        return cMdkNegotiableAuditquotaDao.deleteById(id);
    }

    @Override
    public void removeNegotiableAuditquota(CMdkNegotiableAuditquota pCMdkNegotiableAuditquota) {
        cMdkNegotiableAuditquotaDao.attach(pCMdkNegotiableAuditquota);
        cMdkNegotiableAuditquotaDao.remove(pCMdkNegotiableAuditquota);
    }
    
        /**
     * Get all Insurance ident numbers (IK)
     *
     * @param pCountryEn "de"
     * @return map of Insurance ident numbers (IK)
     */
    @Override
    public List<String> getInscIdentList(CountryEn pCountryEn) {
        return cInsuranceCompanyDao.getInscIdentList(pCountryEn);
    }

    @Override
    public void updateTextTemplatesList(List<CTextTemplate> textTemplatesCatalog) {
        cTextTemplateDao.updateItemList(textTemplatesCatalog);
    }
    
    @Override
    public List<CHospital> getHospitalList(String pCountryEn){
        return cHospitalDao.getEntries(pCountryEn);
    }
    
    @Override
    public CHospital updateHospitalData(CHospital pHospital){ 
        
        return cHospitalDao.merge(pHospital);
    }

    @Override
    public boolean removeHospitalById(long id){
        return cHospitalDao.deleteById(id);
    }

    @Override
      public CHospital getHospital2Ident(String hosIdent, String pCountry){
          return cHospitalDao.getHospital2Ident(hosIdent, pCountry);
      }
      
      
    @Override
    public List<CCatalogIF> getI68DrgList(CountryEn countryEn, int pSelectedYear){
        return cDrgCatalogDao.getI68DrgList(countryEn, pSelectedYear);
    }      

    @Override
    public int dropDrgI68EntriesByYearAndHospIdentList(int catalogYear, CountryEn countryEn, List<String> importIkzList, List<String> importDrgList) {
        return cDrgCatalogDao.dropDrgI68EntriesByYearAndHospIdentList(catalogYear, countryEn, importIkzList, importDrgList);
    }
    @Override
    public int dropDrgNegoEntriesByYearAndHospIdentList(int catalogYear, CountryEn countryEn, Map<String, List<String>> importIkzList){
        return cDrgCatalogDao.dropDrgNegoEntriesByYearAndHospIdentList(catalogYear, countryEn, importIkzList);
    }

    @Override
    public int dropDrgI68EntriesByYearAndHospIdentList(int catalogYear, CountryEn countryEn, Map<String, List<String>> importIkzList){
         return cDrgCatalogDao.dropDrgI68EntriesByYearAndHospIdentList(catalogYear, countryEn, importIkzList);
    }
    
     public int dropSupplFeeEntriesByYearAndHospIdentList(int catalogYear, SupplFeeTypeEn pType, CountryEn countryEn, Map<String, List<String>> importCode2Ik){
         return cSupplementaryFeeDao.dropSupplFeeEntriesByYearAndHospIdentList(catalogYear, pType, countryEn, importCode2Ik);
     }

    @Override
    public List<CDrgCatalog> getDefaultNegoDrgList(CountryEn pCountryEn, int pDrgYear) {
       return cDrgCatalogDao.getDefaultNegoDrgList(pCountryEn, pDrgYear);
    }
    
     public List<CCatalogIF> getNegoSupplWithIkFeeList(int pYear, SupplFeeTypeEn pType, CountryEn countryEn){
          return cSupplementaryFeeDao.getNegoSupplWithIkFeeList(pYear, pType, countryEn);
     }
}
