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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

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
import de.lb.cpx.shared.dto.UserDTO;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author wilde
 */
@Remote
public interface MenuCacheBeanRemote {

    /**
     * Returns a map of Document Types
     *
     * @return all document types
     */
    Map<Long, CWmListDocumentType> getDocumentTypes();

    /**
     * Returns a map of Action Subjects
     *
     * @return all action subjects
     */
    Map<Long, CWmListActionSubject> getActionSubjects();

    /**
     * Returns a map of Process Topics
     *
     * @return all Process Topics
     */
    Map<Long, CWmListProcessTopic> getProcessTopics();

    /**
     *
     * @return all Process Results
     */
    Map<Long, CWmListProcessResult> getProcessResults();

    Map<Long, CWmListReminderSubject> getReminderSubjects();

    Map<Long, CWmListDraftType> getDraftTypes();

    Map<Long, CDeadline> getDeadlines();

    /**
     *
     * @return all Insurance related cases in the database
     */
    List<String> getInsuranceCompany();
    
    Map<String, String> getInsShortNames();

    /**
     *
     * @return return just Vaild AuditReasons
     */
    Map<Long, CMdkAuditreason> getMdkAuditReasons();

    Map<Long, CWmListMdkState> getMdkStates();

    Map<Long, CdbUserRoles> getUserRoles();

    Map<Long, UserDTO> getUsers();
    
//    Map<String, CMdkAuditquota> getMdkAuditQuotas();

}
