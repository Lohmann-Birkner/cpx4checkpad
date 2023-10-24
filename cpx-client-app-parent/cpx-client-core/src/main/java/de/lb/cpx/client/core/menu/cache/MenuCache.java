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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.menu.cache;

import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.server.commonDB.model.CMdkAuditreason;
import de.lb.cpx.server.commonDB.model.CWmListActionSubject;
import de.lb.cpx.server.commonDB.model.CWmListReminderSubject;
import de.lb.cpx.server.commonDB.model.CdbUserRoles;
import de.lb.cpx.shared.dto.UserDTO;
import de.lb.cpx.shared.menucache.MenuCacheEntryEn;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.naming.NamingException;

/**
 * Menu cache to store relevant infos regarding user informations, process
 * related stuff etc.
 *
 * NOTE: THIS should be considert as a temporary solution! Caching these values
 * should be handled otherwise.
 *
 * TODO: Store catalog data?
 *
 * @author wilde
 */
public class MenuCache {

    private static final Logger LOG = Logger.getLogger(MenuCache.class.getName());

    private static MenuCache instance;

    private final MenuCacheActionSubjects actionSubjects = new MenuCacheActionSubjects();
    private final MenuCacheDeadlines deadlines = new MenuCacheDeadlines();
    private final MenuCacheDocumentTypes documentTypes = new MenuCacheDocumentTypes();
    private final MenuCacheDraftTypes draftTypes = new MenuCacheDraftTypes();
    private final MenuCacheInsurances insurances = new MenuCacheInsurances(); // Map <ikz , insurance name>
    private final MenuCacheInsShort insShort = new MenuCacheInsShort(); // Map <ikz , ins short>
    private final MenuCacheAuditReasons auditReasons = new MenuCacheAuditReasons();
    private final MenuCacheRequestStates requestStates = new MenuCacheRequestStates();
    private final MenuCacheProcessResults processResults = new MenuCacheProcessResults();
    private final MenuCacheProcessTopics processTopics = new MenuCacheProcessTopics();
    private final MenuCacheReminderSubjects reminderSubjects = new MenuCacheReminderSubjects();
    private final MenuCacheRoles roles = new MenuCacheRoles();
    private final MenuCacheRules rules = new MenuCacheRules();
    private final MenuCacheSearchLists searchLists = new MenuCacheSearchLists();
    private final MenuCacheUsers users = new MenuCacheUsers();

    private final Map<MenuCacheEntryEn, MenuCacheEntry<? extends Serializable, ? extends Serializable>> menuCacheEntries;

    private MenuCache() {
        Map<MenuCacheEntryEn, MenuCacheEntry<? extends Serializable, ? extends Serializable>> m = new HashMap<>();
        m.put(MenuCacheEntryEn.ACTION_SUBJECTS, actionSubjects);
        m.put(MenuCacheEntryEn.DEADLINES, deadlines);
        m.put(MenuCacheEntryEn.DOCUMENT_TYPES, documentTypes);
        m.put(MenuCacheEntryEn.DRAFT_TYPES, draftTypes);
        m.put(MenuCacheEntryEn.INSURANCES, insurances);
        m.put(MenuCacheEntryEn.AUDIT_REASONS, auditReasons);
//        m.put(MenuCacheEntryEn.MDK_AUDIT_QUOTAS, mdkAuditQuoatas);
        m.put(MenuCacheEntryEn.REQUEST_STATES, requestStates);
        m.put(MenuCacheEntryEn.PROCESS_RESULTS, processResults);
        m.put(MenuCacheEntryEn.PROCESS_TOPICS, processTopics);
        m.put(MenuCacheEntryEn.REMINDER_SUBJECTS, reminderSubjects);
        m.put(MenuCacheEntryEn.ROLES, roles);
        m.put(MenuCacheEntryEn.RULES, rules);
        m.put(MenuCacheEntryEn.SEARCHLISTS, searchLists);
        m.put(MenuCacheEntryEn.USERS, users);
        menuCacheEntries = Collections.unmodifiableMap(m);
    }

    public static MenuCacheActionSubjects getMenuCacheActionSubjects() {
        return instance().actionSubjects;
    }

    public static MenuCacheDeadlines getMenuCacheDeadlines() {
        return instance().deadlines;
    }

    public static MenuCacheDocumentTypes getMenuCacheDocumentTypes() {
        return instance().documentTypes;
    }

    public static MenuCacheDraftTypes getMenuCacheDraftTypes() {
        return instance().draftTypes;
    }

    public static MenuCacheInsurances getMenuCacheInsurances() {
        return instance().insurances;
    }

    public static MenuCacheInsShort getMenuCacheInsShort() {
        return instance().insShort;
    }

    public static MenuCacheAuditReasons getMenuCacheAuditReasons() {
        return instance().auditReasons;
    }

    public static MenuCacheRequestStates getMenuCacheRequestStates() {
        return instance().requestStates;
    }

    public static MenuCacheProcessResults getMenuCacheProcessResults() {
        return instance().processResults;
    }

    public static MenuCacheProcessTopics getMenuCacheProcessTopics() {
        return instance().processTopics;
    }

    public static MenuCacheReminderSubjects getMenuCacheReminderSubjects() {
        return instance().reminderSubjects;
    }

    public static MenuCacheRoles getMenuCacheRoles() {
        return instance().roles;
    }

    public static MenuCacheSearchLists getMenuCacheSearchLists() {
        return instance().searchLists;
    }

    public static MenuCacheRules getMenuCacheRules() {
        return instance().rules;
    }

    public static MenuCacheUsers getMenuCacheUsers() {
        return instance().users;
    }

    public static Map<MenuCacheEntryEn, MenuCacheEntry<? extends Serializable, ? extends Serializable>> getMenuCacheEntries() {
        MenuCache currInst = instance;
        Map<MenuCacheEntryEn, MenuCacheEntry<? extends Serializable, ? extends Serializable>> result = new HashMap<>();
        if (currInst == null) {
            return result;
        }
        result.putAll(currInst.menuCacheEntries);
        return result;
    }

    public static Map<MenuCacheEntryEn, MenuCacheEntry<? extends Serializable, ? extends Serializable>> getMenuCacheEntriesCopy() {
        MenuCache currInst = instance;
        Map<MenuCacheEntryEn, MenuCacheEntry<? extends Serializable, ? extends Serializable>> result = new HashMap<>();
        if (currInst == null) {
            return result;
        }
        Iterator<Entry<MenuCacheEntryEn, MenuCacheEntry<? extends Serializable, ? extends Serializable>>> it = currInst.menuCacheEntries.entrySet().iterator();
        while (it.hasNext()) {
            Entry<MenuCacheEntryEn, MenuCacheEntry<? extends Serializable, ? extends Serializable>> entry = it.next();
            result.put(entry.getKey(), entry.getValue().getCopy());
        }
        return result;
    }

    //don't expose this to other classes (it's NOT a copy of MenuCacheEntry!)
    private static synchronized MenuCacheEntry<? extends Serializable, ? extends Serializable> getMenuCacheEntry(MenuCacheEntryEn pEntryEn) {
        if (pEntryEn == null) {
            return null;
        }
        MenuCache currInst = instance;
        if (currInst == null) {
            return null;
        }
        return currInst.menuCacheEntries.get(pEntryEn);
    }

    public static synchronized int size(MenuCacheEntryEn pEntryEn) {
        MenuCacheEntry<? extends Serializable, ? extends Serializable> entry = getMenuCacheEntry(pEntryEn);
        if (entry == null) {
            return 0;
        }
        return entry.size();
    }

    public static synchronized boolean uninitialize(MenuCacheEntryEn pEntryEn) {
        LOG.log(Level.FINEST, "uninitialize MenuCache of type {0}...", pEntryEn);
        MenuCacheEntry<? extends Serializable, ? extends Serializable> entry = getMenuCacheEntry(pEntryEn);
        if (entry == null) {
            return false;
        }
        return entry.uninitialize();
    }

    public static MenuCacheEntry<? extends Serializable, ? extends Serializable> get(MenuCacheEntryEn pEntryEn) {
        return getMenuCacheEntry(pEntryEn);
    }

    public static synchronized boolean uninitialize() {
        LOG.log(Level.FINEST, "uninitialize MenuCache (all entries)...");
        MenuCache currInst = instance;
        if (currInst == null) {
            return false;
        }
        Iterator<Entry<MenuCacheEntryEn, MenuCacheEntry<? extends Serializable, ? extends Serializable>>> it = currInst.menuCacheEntries.entrySet().iterator();
        boolean uninitialized = false;
        while (it.hasNext()) {
            Entry<MenuCacheEntryEn, MenuCacheEntry<? extends Serializable, ? extends Serializable>> entry = it.next();
            uninitialized = entry.getValue().uninitialize() || uninitialized;
        }
        return uninitialized;
    }

    public static synchronized MenuCache instance() {
        if (instance == null) {
            instance = new MenuCache();
        }
        return instance;
    }

    /**
     * destroy the cache, and clearAndRemoveMap maps
     */
    public static synchronized void destroy() {
        MenuCache oldInstance = instance;
        instance = null;
        if (oldInstance != null) {
            LOG.log(Level.FINE, "Cache destroyed!");
        }
    }
    
    /**
     * @return map of entries for all role names, convince methode for
     * CheckComboBoxes in Menu creates new Map.Entry Objects by hand to not
     * break implementation
     */
    public Set<Map.Entry<Long, CdbUserRoles>> getRoleMapEntries() {
        return roles.entrySet();
    }

    public List<CdbUserRoles> getRoles() {
        return roles.values();
    }

    /**
     * @param pUserId id of the user
     * @return user name, UName value in CDBUsers
     */
    public String getUserNameForId(long pUserId) {
        return users.getUserNameForId(pUserId);
    }

    /**
     * @param pUserId id of the user
     * @return user full name value in CDBUsers
     */
    public String getUserFullNameForId(long pUserId) {
        return users.getUserFullNameForId(pUserId);
    }

    /**
     * @return map of entries for all user names, convince methode for
     * CheckComboBoxes in Menu creates new Map.Entry Objects by hand to not
     * break implementation
     */
    public Set<Map.Entry<Long, UserDTO>> getUserMapEntries() {
        return users.entrySet();
    }

    /**
     * @return map of entries for all valid user names, convince methode for
     * CheckComboBoxes in Menu creates new Map.Entry Objects by hand to not
     * break implementation
     */
    public Set<Map.Entry<Long, UserDTO>> getValidUserMapEntries() {
        return users.getValidUserMapEntries();
    }

    public UserDTO getUserMapEntryForId(Long pUserId) {
        return users.getUserMapEntryForId(pUserId);
    }

    /**
     * @return map of entries for all user names, convince methode for
     * CheckComboBoxes in Menu creates new Map.Entry Objects by hand to not
     * break implementation
     */
    public Set<Map.Entry<Long, String>> getUserNamesEntries() {
        return users.getUserNamesEntries();
    }

    /**
     * @param pName name of the user
     * @return user id by user name, null if name is not found in map
     * @throws NamingException naming exception (ejb corrupted?!)
     */
    public Long getUserId(String pName) throws NamingException {
        return users.getUserId(pName);
    }

    /**
     * @param pReminderInternalId id of the reminder
     * @return ReminderName
     */
    public String getReminderSubjectsForInternalId(long pReminderInternalId) {
        return reminderSubjects.getName(pReminderInternalId);
    }

    /**
     * @param pReminderInternalId id of the reminder (uncached!)
     * @return Reminder
     */
    public CWmListReminderSubject getReminderForInternalId(long pReminderInternalId) {
        return reminderSubjects.getReminderForInternalId(pReminderInternalId);
    }

    /**
     * @return set of map entries for all reminder subjects, convince methode
     * for CheckComboBoxes in Menu
     */
    public Set<Map.Entry<Long, String>> getReminderSubjectEntries() {
        return reminderSubjects.nameEntrySet();
    }

    /**
     * @param pDocumentTypeId id of the document type
     * @return DocumentTypeName
     */
    public String getDocumentTypeName(long pDocumentTypeId) {
        return documentTypes.getName(pDocumentTypeId);
    }

    /**
     * @param pProcessResultId id of the process result
     * @return ProcessResultName
     */
    public String getProcessResultName(long pProcessResultId) {
        return processResults.getName(pProcessResultId);
    }

    /**
     * @param pActionId id of the action
     * @return ActionName
     */
    public String getActionSubjectName(long pActionId) {
        return actionSubjects.getName(pActionId);
    }

    public CWmListActionSubject getActionSubjectForId(long pType) {
        return actionSubjects.get(pType);
    }

    /**
     * @return set of map entries for all action subjects, convince methode for
     * CheckComboBoxes in Menu
     */
    public Set<Map.Entry<Long, String>> getActionSubjectEntries() {
        return actionSubjects.nameEntrySet();
    }

    public List<CWmListActionSubject> getActionSubjects(Date pDate) {
        return actionSubjects.getActionSubjects(pDate);
    }

    /**
     * @param pProcessTopicId process topic id
     * @return process Topic Name
     */
    public String getProcessTopicForId(long pProcessTopicId) {
        return processTopics.getName(pProcessTopicId);
    }

    /**
     * @return set of all entries of the map, convince methode for
     * CheckComboBoxes in Menu
     */
    public Set<Map.Entry<Long, String>> getProcessTopicsEntries() {
        return processTopics.nameEntrySet();
    }

    /**
     *
     * @param pProcessResultId process result id
     * @return process Result Name
     */
    public String getProcessResultForId(long pProcessResultId) {
        return processResults.getName(pProcessResultId);
    }

    /**
     *
     * @return set of all entries of the map, convince methode for
     * CheckComboBoxes in Menu
     */
    public Set<Map.Entry<Long, String>> getProcessResultEntries() {
        return processResults.nameEntrySet();
    }

    /**
     *
     * @param pAuditReasonNumber reason id
     * @param pOption MenucacheOption to filter Objects
     * @return reason name
     */
    public String getAuditReasonForNumber(long pAuditReasonNumber,MenuCacheOptionsEn pOption) {
        return auditReasons.getName(pAuditReasonNumber,pOption);
    }
    
    public String getAuditReasonForNumber(long pAuditReasonNumber) {
        return getAuditReasonForNumber(pAuditReasonNumber,null);
    }
    
    public Set<Map.Entry<Long, String>> getAuditReasonsEntries() {
        return auditReasons.nameEntrySet();
    }
    public Set<Map.Entry<Long, String>> getAuditReasonsEntries(CaseTypeEn pType) {
        return getAuditReasonsEntries(null, pType);
    }
    public Set<Map.Entry<Long, String>> getAuditReasonsEntries(MenuCacheOptionsEn pOptions, CaseTypeEn pType) {
        if(pType == null){
            return MenuCache.this.getAuditReasonsEntries();
        }
        if(!CaseTypeEn.DRG.equals(pType) && !CaseTypeEn.PEPP.equals(pType)){
            return MenuCache.this.getAuditReasonsEntries();
        }
        Map<Long, CMdkAuditreason> m = auditReasons.map(pOptions);
        Map<Long, CMdkAuditreason> filtered = m.entrySet()
                .stream()
                .filter((t) -> {
                    if(t.getValue() == null){
                        return false;
                    }
                    return pType.equals(t.getValue().getMdkArCaseType());
                })
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
        return auditReasons.nameEntrySet(filtered);
    }

    public String getRequestStatesForInternalId(Long pRequestStatesId) {
        return requestStates.getName(pRequestStatesId);
    }

    public Set<Map.Entry<Long, String>> getRequestStatesEntries() {
        return requestStates.nameEntrySet();
    }

    /**
     * @param ikz insurance ident
     * @return insurance name
     */
    public String getInsuranceForIkz(String ikz) {
        return insurances.get(ikz);
    }

    /**
     * @param ikz insurance ident
     * @return insurance short name
     */
    public String getInsuranceShortForIkz(String ikz) {
        return insurances.getInsuranceShortForIkz(ikz);
    }

    /**
     * @return set of all entries of the map, convince methode for
     * CheckComboBoxes in Menu
     */
    public Set<Map.Entry<String, String>> getInsuranceEntries() {
        return insurances.entrySet();
    }

    /**
     * @return set of all entries of the map, convince methode for
     * CheckComboBoxes in Menu
     */
    public Set<Map.Entry<String, String>> getInsShortEntries() {
        return insShort.entrySet();
    }

    /**
     *
     * @param pDraftTypeInternalId InternalId for template
     * @return template type
     */
    public String getDraftTypesForInternalId(long pDraftTypeInternalId) {
        return draftTypes.getName(pDraftTypeInternalId);
    }

    public String getRuleNumber(long ruleId) {
        return rules.getRuleNumber(ruleId);
    }

}
