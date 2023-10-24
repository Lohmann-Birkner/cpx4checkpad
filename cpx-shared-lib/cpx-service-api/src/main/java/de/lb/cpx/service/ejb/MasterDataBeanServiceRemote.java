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
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author sklarow
 */
@Remote
public interface MasterDataBeanServiceRemote {

    /**
     * Get the catalog with base rates data.
     *
     * @param pCountryEn Country of user
     * @return Return list with base rates data.
     */
    List<CBaserate> getBaseratesCatalog(String pCountryEn);

    /**
     * Get negotiated default DRG list
     *
     * @param pCountryEn country En
     * @param pStartDate start date for baserates
     * @return default list of DRGs
     */
    List<CBaserate> getFullBaseratesCatalog(String pCountryEn, Date pStartDate);

    /**
     * Get base rates for hospital by hospital identifier
     *
     * @param pBaseHosIdent hospital identifier
     * @return List of baserates
     */
    List<CBaserate> getBaseratesByBaseHoIdent(String pBaseHosIdent);

    /**
     * update the baserate data
     *
     * @param pCBaserate baserate
     * @return state of update process
     */
    boolean updateBaserate(CBaserate pCBaserate);

    /**
     * Delete baserate from database
     *
     * @param id baserate id
     * @return boolean value for delete state
     */
    boolean removeBaserateById(Long id);

    /**
     * Add new baserate for hospital
     *
     * @param pCBaserate baserate
     * @return id for new baserate
     */
    long addNewBaserate(CBaserate pCBaserate);

    /**
     * Get baserate by id
     *
     * @param baserateId baserate id
     * @return searched baserate
     */
    CBaserate getBaserateById(Long baserateId);

    /**
     * Delete all entries in the table
     *
     * @return deleted rows of table
     */
    int dropAllBaseratesEntries();

    /**
     * Import new baserate for hospital in database
     *
     * @param pCBaserateList list of baserates
     * @return id for new baserate
     */
    int importBaserates(List<CBaserate> pCBaserateList);

    /**
     * Get all entries from table of MDK audit reasons
     *
     * @return entries list
     */
    List<CMdkAuditreason> getMdkAuditReasonCatalog();

    /**
     * Add new mdk audit reason
     *
     * @param pCMdkAuditreason mdk audit reason
     * @return id for new mdk audit reason
     */
    long addNewMdkAuditReason(CMdkAuditreason pCMdkAuditreason);

    /**
     * Get mdk audit reason item by id
     *
     * @param pAuditreasonId mdk audit reason id
     * @return searched mdk audit reason
     */
    CMdkAuditreason getMdkAuditreasonById(Long pAuditreasonId);

    /**
     *
     * @param pAuditreasonNumber mdk audit reason AR id
     * @return searched mdk audit reason
     */
    CMdkAuditreason getMdkAuditreasonByNumber(long pAuditreasonNumber);

    /**
     * update the mdk audit reason
     *
     * @param pCMdkAuditreason mdk audit reason to update
     * @return state of update process
     */
    boolean updateMdkAuditReason(CMdkAuditreason pCMdkAuditreason);

    /**
     * update the audit reason list
     *
     * @param pCMdkAuditreasonList audit reason list to update
     * @return state of update process
     */
    boolean updateMdkAuditReasonList(List<CMdkAuditreason> pCMdkAuditreasonList);

    /**
     * Inactivate audit reason by id
     *
     * @param pCMdkAuditreason audit reason to inactivate
     * @return boolean value for inactivate state
     */
    boolean inactivateMdkAuditreason(CMdkAuditreason pCMdkAuditreason);

    /**
     * Get sequence for internal id of audit reason
     *
     * @return sequence id
     */
    long getNextMdkAuditreasonInternalId();

    /**
     * Get all entries from table of deadlines
     *
     * @return entries list
     */
    List<CDeadline> getDeadlinesCatalog();

    /**
     * Get reminder type for deadlines
     *
     *
     * @return entries list
     */
    List<CWmListReminderSubject> getReminderTypeList();

    /**
     * Add new deadline
     *
     * @param pCDeadline deadline
     * @return id for new deadlines
     */
    long addNewDeadline(CDeadline pCDeadline);

    /**
     * update the deadline
     *
     * @param pCDeadline deadline to update
     * @return state of update process
     */
    boolean updateDeadline(CDeadline pCDeadline);

    /**
     * Get deadline item by id
     *
     * @param pDeadlineId deadline id
     * @return searched deadline
     */
    CDeadline getDeadlineById(Long pDeadlineId);

    /**
     * Delete deadline from database
     *
     * @param id deadline id
     * @return boolean value for delete state
     */
    boolean removeDeadlineById(Long id);

    /**
     * Get all entries of MDK master data
     *
     * @param pCountryEn name of country
     * @return list of MDK master data
     */
    List<CMdk> getMdkMasterDataCatalog(String pCountryEn);

    /**
     * Get all entries of MDK master data
     *
     * @param pCountryEn name of country
     * @return list of MDK master data
     */
    List<CMdk> getMdkDataCatalog(String pCountryEn);

    /**
     * Update the MDK master data
     *
     * @param pCMdk mdk master data item
     * @return state of update process
     */
    boolean updateMdkItem(CMdk pCMdk);

    /**
     * Delete MDK master data item by id
     *
     * @param id mdk item id
     * @return boolean value for delete state
     */
    boolean removeMdkItemById(Long id);

    /**
     * Add new MDK master data item
     *
     * @param pCMdk master data item
     * @return id for new mdk item
     */
    long addNewMdkItem(CMdk pCMdk);

    /**
     * Get MDK master data item by id
     *
     * @param pMdkItemId mdk item id
     * @return searched mdk item
     */
    CMdk getMdkItemById(Long pMdkItemId);

    /**
     * Get sequence for internal id of mdk
     *
     * @return sequence id
     */
    long getNextMdkInternalId();

    /**
     * Delete all entries in the MDK master data table
     *
     * @param pCountryEn name of country
     * @return deleted rows of table
     */
    int dropAllMdkMasterDataEntries(CountryEn pCountryEn);

    /**
     * Delete all centrally based MDKs in the table
     *
     * @return deleted rows
     */
    int dropCentrallyBasedMdks();

    /**
     * Import new MDK master data entries
     *
     * @param pCMdk list of mdk master data
     * @return id for new baserate
     */
    int importMdkMasterData(List<CMdk> pCMdk);

    /**
     * Import new MDKs entries in database
     *
     * @param pCMdkList list of MDKs
     * @return number of new MDKs
     */
    int importMdkList(List<CMdk> pCMdkList);

    /**
     * Get last mdk ident of user definded mdk
     *
     * @return ident of user definded mdk
     */
    Long getLastUserDefinedMDK();

    /**
     * Get all entries from C_DRAFTS table
     *
     * @return drafts
     */
    List<CDrafts> getAllDraftsEntries();

    /**
     * Update the ms word template
     *
     * @param pCDrafts ms word template
     * @return state of update process
     */
    boolean updateWordTemplate(CDrafts pCDrafts);

    /**
     * Delete ms word template by id
     *
     * @param id ms word template id
     * @return boolean value for delete state
     */
    boolean removeWordTemplateById(Long id);

    /**
     * Get ms word template by id
     *
     * @param pCDraftsId mdk item id
     * @return searched mdk item
     */
    CDrafts getWordTemplateById(Long pCDraftsId);

    /**
     * Add new ms word template
     *
     * @param pCDrafts new ms word template
     * @return id for new new ms word template
     */
    long addNewWordTemplate(CDrafts pCDrafts);

    /**
     * Get all entries from table C_WM_LIST_ACTION_SUBJECT
     *
     * @return entries list
     */
    List<CWmListActionSubject> getWmListActionSubjectCatalog();

    /**
     * Add new action subject
     *
     * @param pCWmListActionSubject action subject
     * @return id for new action subject
     */
    long addNewActionSubject(CWmListActionSubject pCWmListActionSubject);

    /**
     * update the action subject
     *
     * @param pCWmListActionSubject action subject to update
     * @return state of update process
     */
    boolean updateActionSubject(CWmListActionSubject pCWmListActionSubject);

    /**
     * update the action subject list
     *
     * @param pCWmListActionSubjectList action subject list to update
     * @return state of update process
     */
    boolean updateActionSubjectList(List<CWmListActionSubject> pCWmListActionSubjectList);

    /**
     * Get action subject item by id
     *
     * @param pCWmListActionSubjectId action subject id
     * @return searched action subject
     */
    CWmListActionSubject getActionSubjectById(Long pCWmListActionSubjectId);

    /**
     * Inactivate action subject by id
     *
     * @param pCWmListActionSubject action subject to inactivate
     * @return boolean value for inactivate state
     */
    boolean inactivateActionSubject(CWmListActionSubject pCWmListActionSubject);

    /**
     * Get sequence for internal id of action subject
     *
     * @return sequence id
     */
    long getNextActionSubjectInternalId();

    /**
     * Get all entries from table C_WM_LIST_MDK_STATE
     *
     * @return entries list
     */
    List<CWmListMdkState> getWmListMdkStateCatalog();

    /**
     * Add new MDK state
     *
     * @param pCWmListMdkState MDK state
     * @return id for new MDK state
     */
    long addNewMdkState(CWmListMdkState pCWmListMdkState);

    /**
     * update the MDK state
     *
     * @param pCWmListMdkState MDK state to update
     * @return state of update process
     */
    boolean updateMdkState(CWmListMdkState pCWmListMdkState);

    /**
     * update the MDK state list
     *
     * @param pCWmListMdkStateList MDK state list to update
     * @return state of update process
     */
    boolean updateMdkStateList(List<CWmListMdkState> pCWmListMdkStateList);

    /**
     * Get MDK state item by id
     *
     * @param pCWmListMdkStateId MDK state id
     * @return searched MDK state
     */
    CWmListMdkState getMdkStateById(Long pCWmListMdkStateId);

    /**
     * Inactivate MDK state by id
     *
     * @param pCWmListMdkState MDK state to inactivate
     * @return boolean value for inactivate state
     */
    boolean inactivateMdkState(CWmListMdkState pCWmListMdkState);

    /**
     * Get sequence for internal id of MDK state
     *
     * @return sequence id
     */
    long getNextMdkStateInternalId();

    /**
     * Get all entries from table C_WM_LIST_DOCUMENT_TYPE
     *
     * @return entries list
     */
    List<CWmListDocumentType> getWmListDocumentTypeCatalog();

    /**
     * Add new document type
     *
     * @param pCWmListDocumentType document type
     * @return id for new document type
     */
    long addNewDocumentType(CWmListDocumentType pCWmListDocumentType);

    /**
     * update the document type
     *
     * @param pCWmListDocumentType document type to update
     * @return state of update process
     */
    boolean updateDocumentType(CWmListDocumentType pCWmListDocumentType);

    /**
     * update the document type list
     *
     * @param pCWmListDocumentTypeList document type list to update
     * @return state of update process
     */
    boolean updateDocumentTypeList(List<CWmListDocumentType> pCWmListDocumentTypeList);

    /**
     * Get document type item by id
     *
     * @param pCWmListDocumentTypeId document type id
     * @return searched document type
     */
    CWmListDocumentType getDocumentTypeById(Long pCWmListDocumentTypeId);

    /**
     * Inactivate document type by id
     *
     * @param pCWmListDocumentType document type to inactivate
     * @return boolean value for inactivate state
     */
    boolean inactivateDocumentType(CWmListDocumentType pCWmListDocumentType);

    /**
     * Get sequence for internal id of document type
     *
     * @return sequence id
     */
    long getNextDocumentTypeInternalId();

    /**
     * Get all entries from table C_WM_LIST_PROCESS_TOPIC
     *
     * @return entries list
     */
    List<CWmListProcessTopic> getWmListProcessTopicCatalog();

    /**
     * Add new process topic
     *
     * @param pCWmListProcessTopic process topic
     * @return id for new process topic
     */
    long addNewProcessTopic(CWmListProcessTopic pCWmListProcessTopic);

    /**
     * update the process topic
     *
     * @param pCWmListProcessTopic process topic to update
     * @return state of update process
     */
    boolean updateProcessTopic(CWmListProcessTopic pCWmListProcessTopic);

    /**
     * update the process topic list
     *
     * @param pCWmListProcessTopicList process topic list to update
     * @return state of update process
     */
    boolean updateProcessTopicList(List<CWmListProcessTopic> pCWmListProcessTopicList);

    /**
     * Get process topic item by id
     *
     * @param pCWmListProcessTopicId process topic id
     * @return searched process topic
     */
    CWmListProcessTopic getProcessTopicById(Long pCWmListProcessTopicId);

    /**
     * @param pIdent internal id
     * @return process Topic for internal ident
     */
    CWmListProcessTopic getProcessTopicByIdent(Long pIdent);

    /**
     * Inactivate process topic by id
     *
     * @param pCWmListProcessTopic process topic to inactivate
     * @return boolean value for inactivate state
     */
    boolean inactivateProcessTopic(CWmListProcessTopic pCWmListProcessTopic);

    /**
     * Get sequence for internal id of process topi
     *
     * @return sequence id
     */
    long getNextProcessTopicInternalId();

    /**
     * Get all entries from table C_WM_LIST_REMINDER_SUBJECT
     *
     * @return entries list
     */
    List<CWmListReminderSubject> getWmListReminderSubjectCatalog();

    /**
     * Add new reminder subject
     *
     * @param pCWmListReminderSubject reminder subject
     * @return id for new reminder subject
     */
    long addNewReminderSubject(CWmListReminderSubject pCWmListReminderSubject);

    /**
     * update the reminder subject
     *
     * @param pCWmListReminderSubject reminder subject to update
     * @return state of update process
     */
    boolean updateReminderSubject(CWmListReminderSubject pCWmListReminderSubject);

    /**
     * update the reminder subject list
     *
     * @param pCWmListReminderSubjectList reminder subject list to update
     * @return state of update process
     */
    boolean updateReminderSubjectList(List<CWmListReminderSubject> pCWmListReminderSubjectList);

    /**
     * Get reminder subject item by id
     *
     * @param pCWmListReminderSubjectId reminder subject id
     * @return searched reminder subject
     */
    CWmListReminderSubject getReminderSubjectById(Long pCWmListReminderSubjectId);

    /**
     * Inactivate reminder subject by id
     *
     * @param pCWmListReminderSubject reminder subject to inactivate
     * @return boolean value for inactivate state
     */
    boolean inactivateReminderSubject(CWmListReminderSubject pCWmListReminderSubject);

    /**
     * Get sequence for internal id of reminder subject
     *
     * @return sequence id
     */
    long getNextReminderSubjectInternalId();

    /**
     * Get all entries from table C_WM_LIST_PROCESS_RESULT
     *
     * @return entries list
     */
    List<CWmListProcessResult> getWmListProcessResultCatalog();

    /**
     * Add new process result
     *
     * @param pCWmListProcessResult process result
     * @return id for new process result
     */
    long addNewProcessResult(CWmListProcessResult pCWmListProcessResult);

    /**
     * update the process result
     *
     * @param pCWmListProcessResult process result to update
     * @return state of update process
     */
    boolean updateProcessResult(CWmListProcessResult pCWmListProcessResult);

    /**
     * update the process result list
     *
     * @param pCWmListProcessResultList process result list to update
     * @return state of update process
     */
    boolean updateProcessResultList(List<CWmListProcessResult> pCWmListProcessResultList);

    /**
     * Get process result item by id
     *
     * @param pCWmListProcessResultId process result id
     * @return searched process result
     */
    CWmListProcessResult getProcessResultById(Long pCWmListProcessResultId);

    /**
     * Inactivate process result by id
     *
     * @param pCWmListProcessResult process result to inactivate
     * @return boolean value for inactivate state
     */
    boolean inactivateProcessResult(CWmListProcessResult pCWmListProcessResult);

    /**
     * Get sequence for internal id of process result
     *
     * @return sequence id
     */
    long getNextProcessResultInternalId();

    /**
     * Get all distinct years of DRG catalog
     *
     * @param pCountryEn "de"
     * @return list of years of DRG catalog
     */
    List<Integer> getDrgYearList(CountryEn pCountryEn);

    /**
     * Get all hospital ident numbers (IKZ)
     *
     * @param pCountryEn "de"
     * @return map of hospital ident numbers (IKZ)
     */
    List<String> getHosIdentList(CountryEn pCountryEn);

    /**
     * Get already entered hospital ident numbers (IKZ) in DRG catalog
     *
     * @param pCountryEn "de"
     * @param pSupplYear selected year
     * @return map of hospital ident numbers (IKZ)
     */
    List<String> getExistingHosIdentDrgList(CountryEn pCountryEn, int pSupplYear);

    /**
     * Get negotiated DRG catalog
     *
     * @param pDrgYear selected yaer
     * @param pHosIdent selected IKZ
     * @param pCountryEn "de"
     * @return DRG catalog for selected year and IKZ
     */
    List<CDrgCatalog> getDrgCatalogByYearAndIkz(int pDrgYear, String pHosIdent, CountryEn pCountryEn);

    /**
     * Get DRG catalog
     *
     * @param pDrgYear selected yaer
     * @param pHosIdent selected IKZ
     * @param pCountryEn "de"
     * @return DRG catalog for selected year and IKZ
     */
    List<CDrgCatalog> getAllDrgCatalogByYearAndIkz(int pDrgYear, String pHosIdent, CountryEn pCountryEn);

    /**
     * Add new negotiated DRG item
     *
     * @param pCDrgCatalog negotiated DRG item
     * @return new negotiated DRG item
     */
    CDrgCatalog addNegotiatedDrg(CDrgCatalog pCDrgCatalog);

    /**
     * update negotiated DRG item
     *
     * @param pCDrgCatalog negotiated DRG item
     * @return state of update process
     */
    boolean updateNegotiatedDrg(CDrgCatalog pCDrgCatalog);

    /**
     * Delete DRG by ID
     *
     * @param id item id
     * @return boolean value for delete state
     */
    boolean removeDrgById(Long id);

    /**
     * Get negotiated default DRG list
     *
     * @param pCountryEn country
     * @param pDrgYear year
     * @return list of drg catalog
     */
    List<CCatalogIF> getDefaultNegotiatedDrgList(CountryEn pCountryEn, int pDrgYear);

   List<CDrgCatalog> getDefaultNegoDrgList(CountryEn pCountryEn, int pDrgYear);

    /**
     * Get all default DRG list
     *
     * @param pCountryEn country
     * @param pDrgYear year
     * @return list of drg catalog
     */
    List<CDrgCatalog> getDefaultAllDrgList(CountryEn pCountryEn, int pDrgYear);

    /**
     * Get all distinct years of supplementary fee catalog
     *
     * @param pCountryEn "de"
     * @return list of years of supplementary fee
     */
    List<Integer> getFeeYearList(CountryEn pCountryEn);

    /**
     * Get negotiated supplementary fee catalog
     *
     * @param pFeeYear selected yaer
     * @param pHosIdent selected IKZ
     * @param pCountryEn "de"
     * @param pSupplTypeEn catalog type
     * @return fee catalog for selected year, type and IKZ
     */
    List<CSupplementaryFee> getFeeCatalogByYearIkzAndType(int pFeeYear, String pHosIdent, CountryEn pCountryEn, SupplFeeTypeEn pSupplTypeEn);

    /**
     * Get all supplementary fee catalog
     *
     * @param pFeeYear selected yaer
     * @param pHosIdent selected IKZ
     * @param pCountryEn "de"
     * @param pSupplTypeEn catalog type
     * @return fee catalog for selected year, type and IKZ
     */
    List<CSupplementaryFee> getAllFeeCatalogByYearIkzAndType(int pFeeYear, String pHosIdent, CountryEn pCountryEn, SupplFeeTypeEn pSupplTypeEn);

    /**
     * Add new supplementary fee item
     *
     * @param pCSupplementaryFee negotiated fee item
     * @return new negotiated fee item
     */
    CSupplementaryFee addNegotiatedSuplFee(CSupplementaryFee pCSupplementaryFee);

    /**
     * update negotiated supplementary fee item
     *
     * @param pCSupplementaryFee negotiated fee item
     * @return id for updated negotiated fee item
     */
    boolean updateNegotiatedFee(CSupplementaryFee pCSupplementaryFee);

    /**
     * Delete supl fee by ID
     *
     * @param id item id
     * @return boolean value for delete state
     */
    boolean removeSupplFeeById(Long id);

    /**
     * Get negotiated default supplementary fee list
     *
     * @param pCountryEn catalog language
     * @param pSupplYear catalog year
     * @param pSupplTypeEn catalog type
     * @return fee catalog for selected year, type and IKZ
     */
    List<CSupplementaryFee> getDefaultNegotiatedSuplFeeList(CountryEn pCountryEn, int pSupplYear, SupplFeeTypeEn pSupplTypeEn);
    
    List<CCatalogIF> getDefaultNegoSuplFeeList(CountryEn pCountryEn, int pSupplYear, SupplFeeTypeEn pSupplTypeEn);

    /**
     * Get all default supplementary fee list
     *
     * @param pCountryEn catalog language
     * @param pSupplYear catalog year
     * @param pSupplTypeEn catalog type
     * @return fee catalog for selected year, type and IKZ
     */
    List<CSupplementaryFee> getDefaultAllSuplFeeList(CountryEn pCountryEn, int pSupplYear, SupplFeeTypeEn pSupplTypeEn);

    /**
     * Get already entered hospital ident numbers (IKZ) in supplementary fee
     * catalog
     *
     * @param pCountryEn catalog language
     * @param pSupplYear selected year
     * @param pSupplTypeEn catalog typegetExistingHosIdentList
     * @return map of hospital ident numbers (IKZ)
     */
    List<String> getExistingHosIdentFeeList(CountryEn pCountryEn, int pSupplYear, SupplFeeTypeEn pSupplTypeEn);

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
    List<String> getExistingHosIdentSupplFeePeppList(CountryEn pCountryEn, int pSupplYear, SupplFeeTypeEn pSupplTypeEnDf, SupplFeeTypeEn pSupplTypeEnSp);

    /**
     * Get all distinct years of PEPP catalog
     *
     * @param pCountryEn "de"
     * @return list of years of PEPP catalog
     */
    List<Integer> getPeppYearList(CountryEn pCountryEn);

    /**
     * Get negotiated PEPP catalog
     *
     * @param pPeppYear selected yaer
     * @param pHosIdent selected IKZ
     * @param pCountryEn "de"
     * @return PEPP catalog for selected year and IKZ
     */
    List<CPeppCatalog> getPeppCatalogByYearAndIkz(int pPeppYear, String pHosIdent, CountryEn pCountryEn);

    /**
     * Get all PEPP catalog
     *
     * @param pPeppYear selected yaer
     * @param pHosIdent selected IKZ
     * @param pCountryEn "de"
     * @return PEPP catalog for selected year and IKZ
     */
    List<CPeppCatalog> getAllPeppCatalogByYearAndIkz(int pPeppYear, String pHosIdent, CountryEn pCountryEn);

    /**
     * Add new negotiated PEPP item
     *
     * @param pCPeppCatalog negotiated PEPP item
     * @return new negotiated PEPP item
     */
    CPeppCatalog addNegotiatedPepp(CPeppCatalog pCPeppCatalog);

    /**
     * update negotiated PEPP item
     *
     * @param pCPeppCatalog negotiated PEPP item
     * @return id for updated negotiated PEPP item
     */
    boolean updateNegotiatedPepp(CPeppCatalog pCPeppCatalog);

    /**
     * Get negotiated default PEPP list
     *
     * @param pCountryEn catalog language
     * @param pPeppYear catalog year
     * @return list of pepp catalog
     */
    List<CPeppCatalog> getDefaultNegotiatedPeppList(CountryEn pCountryEn, int pPeppYear);

    /**
     * Get default PEPP list
     *
     * @param pCountryEn catalog language
     * @param pPeppYear catalog year
     * @return list of pepp catalog
     */
    List<CPeppCatalog> getDefaultAllPeppList(CountryEn pCountryEn, int pPeppYear);

    /**
     * Get already entered hospital ident numbers (IKZ) in PEPP catalog
     *
     * @param pCountryEn "de"
     * @param pPeppYear selected year
     * @return map of hospital ident numbers (IKZ)
     */
    List<String> getExistingHosIdentPeppList(CountryEn pCountryEn, int pPeppYear);

    /**
     * Delete PEPP item from database by id
     *
     * @param id PEPP item id
     * @return boolean value for delete state
     */
    boolean removePeppById(Long id);

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
    List<CPeppCatalog> getPeppByKeyAndIkz(String peppPepp, Date pValidFrom, Date pValidTo, int pPeppYear, String pHosIdent, CountryEn pCountryEn);

    /**
     * Get negotiated PEPPs by key and year
     *
     * @param peppPepp selected PEPP
     * @param pPeppYear selected yaer
     * @param pCountryEn "de"
     * @return PEPP catalog for selected year and IKZ
     */
    List<CPeppCatalog> getPeppByKeyAndYearDef(String peppPepp, int pPeppYear, CountryEn pCountryEn);

    /**
     * Get fee keys and description by year
     *
     * @param pCountryEn language
     * @param pYear year
     * @param pSupplFeeType fee type
     * @return map of fee keys and description
     */
    Map<String, String> getFeeKeyByYear(CountryEn pCountryEn, int pYear, String pSupplFeeType);

    /**
     * Get PEPP keys and description by year
     *
     * @param pCountryEn language
     * @param pYear year
     * @return map of PEPP keys and description
     */
    Map<String, String> getPeppKeyByYear(CountryEn pCountryEn, int pYear);

    /**
     * Get DRG keys and description by year
     *
     * @param pCountryEn language
     * @param pYear year
     * @return map of DRG keys and description
     */
    Map<String, String> getDrgKeyByYear(CountryEn pCountryEn, int pYear);

    /**
     * Import new PEPP catalog items
     *
     * @param pCPeppCatalog list of items
     * @return number of added PEPP catalog items
     */
    int importPeppCatalog(List<CPeppCatalog> pCPeppCatalog);

    /**
     * Import new DRG catalog items
     *
     * @param pCDrgCatalog list of items
     * @return number of added DRG catalog items
     */
    int importDrgCatalog(List<CDrgCatalog> pCDrgCatalog);

    /**
     * Drop DRGs custom entries by year
     *
     * @param pYear year
     * @param pCountryEn country
     * @return count of dropped entries
     */
    int dropDrgEntriesByYear(final int pYear, final CountryEn pCountryEn);

    /**
     * Drop DRGs custom entries by year
     *
     * @param pYear year
     * @param pCountryEn country
     * @param pHosIdent hospital ident nr
     * @return count of dropped entries
     */
    int dropDrgEntriesByYearAndHospIdent(final int pYear, final CountryEn pCountryEn, String pHosIdent);

    /**
     * Import new suppl. fee catalog items
     *
     * @param pCSupplementaryFee list of items
     * @return number of added suppl fee catalog items
     */
    int importSupplFeeCatalog(List<CSupplementaryFee> pCSupplementaryFee);

    /**
     * Drop suppl. fee custom entries by year
     *
     * @param pYear selected year
     * @param pCountryEn de
     * @param pSupplFeeType suppl fee type
     * @return number of deleted items
     */
    int dropSupplFeeEntriesByYear(int pYear, CountryEn pCountryEn, SupplFeeTypeEn pSupplFeeType);

    /**
     * Drop suppl.fee custom entries by year
     *
     * @param pYear selected year
     * @param pCountryEn de
     * @param pSupplFeeType suppl fee type
     * @param pHosIdent hospital ident
     * @return number of deleted items
     */
    int dropSupplFeeEntriesByYearAndHosIdent(int pYear, CountryEn pCountryEn, SupplFeeTypeEn pSupplFeeType, String pHosIdent);

    /**
     * Drop PEPPs custom entries by year
     *
     * @param pYear selected year
     * @param pCountryEn de
     * @return number of deleted items
     */
    int dropPeppEntriesByYear(int pYear, CountryEn pCountryEn);

    /**
     * Drop PEPPs custom entries by year
     *
     * @param pYear selected year
     * @param pCountryEn de
     * @param pHosIdent
     * @return number of deleted items
     */
    int dropPeppEntriesByYearAndHosIdent(int pYear, CountryEn pCountryEn, String pHosIdent);

    /**
     * Create map of role id and role name
     *
     * @return map of roles
     */
    Map<Long, String> createRoleMap();

    public List<CdbUserRoles> getAllUserRoles();

    /**
     * Create map of rule pools id and rule pools name
     *
     * @param pYear year
     * @return map of rule pools
     */
    Map<Long, String> createRulePoolsMap(int pYear);

    /**
     * Get DRG by ID
     *
     * @param id id of items
     * @return DRG item
     */
    CDrgCatalog getDrgById(Long id);

    /**
     * Get PEPP by ID
     *
     * @param id id of PEPP
     * @return PEPP item
     */
    CPeppCatalog getPeppById(Long id);

    /**
     * Get supplementary fee by ID
     *
     * @param id id of supplementary fee
     * @return supplementary fee item
     */
    CSupplementaryFee getSupplFeeById(Long id);

    /**
     * Drop DRGs daily fee custom entries by year
     *
     * @param pYear selected year
     * @param pCountryEn de
     * @return number of deleted items
     */
    int dropDrgDailyFeeByYear(int pYear, CountryEn pCountryEn);

    /**
     * Drop DRGs daily fee custom entries by year and hospital Ident
     *
     * @param pYear selected year
     * @param pCountryEn de
     * @param hosIdent hospital ident
     * @return number of deleted items
     */
    int dropDrgDailyFeeByYearAndHosIdent(int pYear, CountryEn pCountryEn, String hosIdent);

    /**
     * Get only DRG main and external department
     *
     * @param pCountryEn de
     * @param pYear selected year
     * @return list of DRG main and external department
     */
    List<CDrgCatalog> getMainAndExternalDepartmentList(CountryEn pCountryEn, int pYear);

    /**
     * Get only DRG daily fee
     *
     * @param pCountryEn de
     * @param pYear selected year
     * @return list of DRG daily fee
     */
    List<CCatalogIF> getDailyFeeList(CountryEn pCountryEn, int pYear);
    
    /**
     * gets individaul negotiated DRGs set for year
     * @param pCountryEn
     * @param pYear
     * @return 
     */
    List<CCatalogIF> getNegotiatedDrgsWithIKsList(CountryEn pCountryEn, int pYear) ;

    /**
     * Get all entries from table of bookmarks customer
     *
     * @return entries list
     */
    List<CBookmarksCustomer> getCustomerBookmarksCatalog();

    /**
     * Add new customer bookmark
     *
     * @param pCBookmarksCustomer bookmark
     * @return id for new bookmark
     */
    long addNewCustomerBookmark(CBookmarksCustomer pCBookmarksCustomer);

    /**
     * update the customer bookmark
     *
     * @param pCBookmarksCustomer bookmark to update
     * @return state of update process
     */
    boolean updateCustomerBookmark(CBookmarksCustomer pCBookmarksCustomer);

    /**
     * Get customer bookmark item by id
     *
     * @param pBookmarkId bookmark id
     * @return searched bookmark
     */
    CBookmarksCustomer getCustomerBookmarkById(Long pBookmarkId);

    /**
     * Delete customer bookmark from database
     *
     * @param id bookmark id
     * @return boolean value for delete state
     */
    boolean removeCustomerBookmarkById(Long id);

    /**
     * Get all entries from table C_WM_LIST_DRAFT_TYPE
     *
     * @return entries list
     */
    List<CWmListDraftType> getWmListDraftTypeCatalog();

    /**
     * Add new draft type
     *
     * @param pCWmListDraftType draft type
     * @return id for new draft type
     */
    long addNewDraftType(CWmListDraftType pCWmListDraftType);

    /**
     * Find draft type by id
     *
     * @param id draft type id
     * @return new draft type
     */
    CWmListDraftType findById(long id);

    /**
     * update the draft type
     *
     * @param pCWmListDraftType draft type to update
     * @return state of update process
     */
    boolean updateDraftType(CWmListDraftType pCWmListDraftType);

    /**
     * update the draft type list
     *
     * @param pCWmListDraftTypeList draft type list to update
     * @return state of update process
     */
    boolean updateDraftTypeList(List<CWmListDraftType> pCWmListDraftTypeList);

    /**
     * Get draft type item by id
     *
     * @param pCWmListDraftTypeId draft type id
     * @return searched draft type
     */
    CWmListDraftType getDraftTypeById(Long pCWmListDraftTypeId);

    /**
     * Inactivate draft type by id
     *
     * @param pCWmListDraftType draft type to inactivate
     * @return boolean value for inactivate state
     */
    boolean inactivateDraftType(CWmListDraftType pCWmListDraftType);

    /**
     * Get sequence for internal id of draft type
     *
     * @return sequence id
     */
    long getNextDraftTypeInternalId();

    /**
     * Get all valid and not deleted items from table C_WM_LIST_DRAFT_TYPE for
     * any category
     *
     * @param pCategoryEn template category
     * @return entries list
     */
    Map<Long, String> getDraftTypesCategoryMap(CategoryEn pCategoryEn);

    /**
     * Get all entries from table of textemplate
     *
     * @return entries list
     */
    List<CTextTemplate> getTextTemplateCatalog();

    /**
     * Add new TextTemplate
     *
     * @param pCTextTemplate text template
     * @return id for new TextTemplate
     */
    long addNewTextTemplate(CTextTemplate pCTextTemplate);

    /**
     * update the TextTemplate
     *
     * @param pCTextTemplateCustomer template customer
     * @return state of update process
     */
    boolean updateTextTemplate(CTextTemplate pCTextTemplateCustomer);

    /**
     * Get textTemplate item by id
     *
     * @param pTextTemplateId text template id
     * @return searched textTemplate
     */
    CTextTemplate getTextTemplateById(Long pTextTemplateId);

    /**
     * Delete textTemplate from database
     *
     * @param id textTemplate id
     * @return boolean value for delete state
     */
    boolean removeTextTemplateById(Long id);

    /**
     * Delete textTemplate from database
     *
     * @param textTemplate textTemplate
     */
    void removeTextTemplate(CTextTemplate textTemplate);

    /**
     * Get all entries from table of bookmarks
     *
     * @return entries list
     */
    List<CBookmarks> getBookmarksCatalog();

    /**
     * Delete TextTemplate2Context for given ID
     *
     * @param textTemplate2contextId id
     */
    void deleteTextTemplate2Context(long textTemplate2contextId);

    /**
     * Delete TextTemplate2UserRole for given ID
     *
     * @param textTemplate2UserRoleId id
     */
    void deleteTextTemplate2UserRole(long textTemplate2UserRoleId);
    
    /**
     * Get all entries from table of AuditQuota
     *
     * @return entries list
     */
    List<CMdkAuditquota> getAuditQuotaCatalog();

    /**
     * Add new Auditquota
     *
     * @param pCMdkAuditquota Auditquota
     * @return id for new Auditquota
     */
    long addNewAuditQuota(CMdkAuditquota pCMdkAuditquota);

    /**
     * update the Auditquota
     *
     * @param pCMdkAuditquota Auditquota to update
     * @return state of update process
     */
    boolean updateAuditquota(CMdkAuditquota pCMdkAuditquota);

    /**
     * Get Auditquota item by id
     *
     * @param pAuditquotaId Auditquota id
     * @return searched Auditquota
     */
    CMdkAuditquota getAuditquotaById(Long pAuditquotaId);

    /**
     * Delete Auditquota from database
     *
     * @param id Auditquota id
     * @return boolean value for delete state
     */
    boolean removeAuditquotaById(Long id);

    void removeAuditquota(CMdkAuditquota pCMdkAuditquota);
    
        /**
     * Get all entries from table of NegotiableAuditQuota
     *
     * @return entries list
     */
    List<CMdkNegotiableAuditquota> getNegotiableAuditQuotaCatalog();

    /**
     * Add new NegotiableAuditquota
     *
     * @param pCMdkNegotiableAuditquota Auditquota
     * @return id for new Auditquota
     */
    long addNewNegotiableAuditQuota(CMdkNegotiableAuditquota pCMdkNegotiableAuditquota);

    /**
     * update the NegotiableAuditquota
     *
     * @param pCMdkNegotiableAuditquota Auditquota to update
     * @return state of update process
     */
    boolean updateNegotiableAuditquota(CMdkNegotiableAuditquota pCMdkNegotiableAuditquota);

    /**
     * Get NegotiableAuditquota item by id
     *
     * @param pNegotiableAuditquotaId Auditquota id
     * @return searched Auditquota
     */
    CMdkNegotiableAuditquota getNegotiableAuditquotaById(Long pNegotiableAuditquotaId);

    /**
     * Delete NegotiableAuditquota from database
     *
     * @param id NegotiableAuditquota id
     * @return boolean value for delete state
     */
    boolean removeNegotiableAuditquotaById(Long id);

    void removeNegotiableAuditquota(CMdkNegotiableAuditquota pCMdkNegotiableAuditquota);
    
    List<String> getInscIdentList(CountryEn pCountryEn);
    
    void updateTextTemplatesList(List<CTextTemplate> textTemplatesCatalog);
    
    List<CHospital> getHospitalList(String pCountryEn);

    public CHospital updateHospitalData(CHospital pHospital);

    public boolean removeHospitalById(long id);



    public CHospital getHospital2Ident(String hosIdent, String userSystemLanguage);

    public int dropDrgEntriesByYearAndHospIdentList(int catalogYear, CountryEn countryEn, List<String> importIkzList);
    
    public int dropDrgNegoEntriesByYearAndHospIdentList(int pYear, CountryEn pCountryEn, List<String> pIkzList, List<String> pDrgList);

    public int dropDrgDailyFeeByYearAndHospIdentList(int catalogYear, CountryEn countryEn, List<String> importIkzList);

    public int dropSupplFeeEntriesByYearAndHospIdentList(int catalogYear, SupplFeeTypeEn pType, CountryEn countryEn, List<String> pIkzList);

    public int dropPeppEntriesByYearAndHospIdentList(int catalogYear, CountryEn countryEn, List<String> importIkzList);

    public List<CCatalogIF> getDefaultI68DrgList(CountryEn countryEn, int pSelectedYear);

    public List<CCatalogIF> getI68DrgList(CountryEn countryEn, int pSelectedYear);

    public int dropDrgI68EntriesByYearAndHospIdentList(int catalogYear, CountryEn countryEn, List<String> importIkzList, List<String> importDrgList);

    public int dropDrgNegoEntriesByYearAndHospIdentList(int catalogYear, CountryEn countryEn, Map<String, List<String>> importIkzList);

    public int dropDrgI68EntriesByYearAndHospIdentList(int catalogYear, CountryEn countryEn, Map<String, List<String>> importIkzList);

    public int dropSupplFeeEntriesByYearAndHospIdentList(int catalogYear, SupplFeeTypeEn pType, CountryEn countryEn, Map<String, List<String>> importDrg2Ik);

    public List<CCatalogIF> getNegoSupplWithIkFeeList(int pYear, SupplFeeTypeEn pType, CountryEn countryEn);
}
