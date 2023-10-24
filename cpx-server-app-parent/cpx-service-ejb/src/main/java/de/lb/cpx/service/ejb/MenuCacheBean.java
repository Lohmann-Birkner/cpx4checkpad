/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
package de.lb.cpx.service.ejb;

import de.lb.cpx.server.commonDB.dao.CDeadlineDao;
import de.lb.cpx.server.commonDB.dao.CMdkAuditreasonDao;
import de.lb.cpx.server.commonDB.dao.CWmListActionSubjectDao;
import de.lb.cpx.server.commonDB.dao.CWmListDocumentTypeDao;
import de.lb.cpx.server.commonDB.dao.CWmListDraftTypeDao;
import de.lb.cpx.server.commonDB.dao.CWmListMdkStateDao;
import de.lb.cpx.server.commonDB.dao.CWmListProcessResultDao;
import de.lb.cpx.server.commonDB.dao.CWmListProcessTopicDao;
import de.lb.cpx.server.commonDB.dao.CWmListReminderSubjectDao;
import de.lb.cpx.server.commonDB.dao.CdbUserRolesDao;
import de.lb.cpx.server.commonDB.dao.CdbUsersDao;
import de.lb.cpx.server.commonDB.model.CDeadline;
import de.lb.cpx.server.commonDB.model.CMdkAuditreason;
import de.lb.cpx.server.commonDB.model.CWmListActionSubject;
import de.lb.cpx.server.commonDB.model.CWmListDocumentType;
import de.lb.cpx.server.commonDB.model.CWmListDraftType;
import de.lb.cpx.server.commonDB.model.CWmListMdkState;
import de.lb.cpx.server.commonDB.model.CWmListProcessResult;
import de.lb.cpx.server.commonDB.model.CWmListProcessTopic;
import de.lb.cpx.server.commonDB.model.CWmListReminderSubject;
import de.lb.cpx.server.commonDB.model.CdbUserRoles;
import de.lb.cpx.server.dao.TCaseDao;
import de.lb.cpx.shared.dto.UserDTO;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Bean to access process related services e.g. storing and loading process
 * related entities
 *
 * @author niemeier
 */
@Stateless
public class MenuCacheBean implements MenuCacheBeanRemote {

    @Inject
    private CMdkAuditreasonDao mdkAuditreasonDao;
    @Inject
    private CDeadlineDao deadlineDao;
    @Inject
    private TCaseDao caseDao;
    @Inject
    private CWmListProcessResultDao cWmListProcessResultDao;
    @Inject
    private CWmListProcessTopicDao cWmListProcessTopicDao;
    @Inject
    private CWmListReminderSubjectDao reminderSubjectDao;
    @Inject
    private CWmListDraftTypeDao draftTypesDao;
    @Inject
    private CWmListDocumentTypeDao documentTypeDao;
    @Inject
    private CWmListActionSubjectDao actionSubjectDao;
    @Inject
    private CdbUsersDao cdbUsersDao;
    @Inject
    private CWmListMdkStateDao mdkStateDao;
    @Inject
    private CdbUserRolesDao userRolesDao;
//    @Inject
//    private CMdkAuditquotaDao mdkAuditquotaDao;

    @Override
    public Map<Long, CWmListActionSubject> getActionSubjects() {
        return actionSubjectDao.getMenuCacheItems();
    }

    @Override
    public Map<Long, CWmListDocumentType> getDocumentTypes() {
        return documentTypeDao.getMenuCacheItems();
    }

    @Override
    public Map<Long, CWmListProcessTopic> getProcessTopics() {
        return cWmListProcessTopicDao.getMenuCacheItems();
    }

    @Override
    public Map<Long, CWmListProcessResult> getProcessResults() {
        return cWmListProcessResultDao.getMenuCacheItems();
    }

    @Override
    public Map<Long, CdbUserRoles> getUserRoles() {
        return userRolesDao.getMenuCacheItems();
//        List<CdbUserRoles> result = loginService.getRoles();
//        Map<Long, CdbUserRoles> map = new TreeMap<>();
//        for (CdbUserRoles role : result) {
//            map.put(role.id, role);
//        }
//        return map;
    }

    @Override
    public Map<Long, UserDTO> getUsers() {
        return cdbUsersDao.getMenuCacheItems();
    }

    @Override
    public Map<Long, CDeadline> getDeadlines() {
        return deadlineDao.getMenuCacheItems();
    }

    @Override
    public Map<Long, CWmListReminderSubject> getReminderSubjects() {
        return reminderSubjectDao.getMenuCacheItems();
    }

    @Override
    public Map<Long, CWmListDraftType> getDraftTypes() {
        return draftTypesDao.getMenuCacheItems();
    }

    @Override
    public Map<Long, CWmListMdkState> getMdkStates() {
        return mdkStateDao.getMenuCacheItems();
    }

    @Override
    public Map<Long, CMdkAuditreason> getMdkAuditReasons() {
        return mdkAuditreasonDao.getMenuCacheItems();
    }

    //CPX-995 RSH 20180815
    @Override
    public List<String> getInsuranceCompany() {
        //get insurance liste von Tcase
        List<String> insuranceListe = caseDao.getAllInsurance();
        return insuranceListe;
    }

    @Override
    public Map<String, String> getInsShortNames() {
        return caseDao.getAllInsShortNames();
    }

//    @Override
//    public Map<String, CMdkAuditquota> getMdkAuditQuotas() {
//        return mdkAuditquotaDao.getMdkQuoatas();
//    }
}
