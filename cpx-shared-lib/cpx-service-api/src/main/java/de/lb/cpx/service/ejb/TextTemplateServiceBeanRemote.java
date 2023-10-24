/* 
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.lb.cpx.model.enums.TextTemplateTypeEn;
import de.lb.cpx.server.commonDB.model.CTextTemplate;
import de.lb.cpx.server.commonDB.model.CTextTemplate2Context;
import de.lb.cpx.server.commonDB.model.CTextTemplate2UserRole;
import de.lb.cpx.server.commonDB.model.CdbUser2Role;
import de.lb.cpx.server.commonDB.model.CdbUserRoles;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author nandola
 */
@Remote
public interface TextTemplateServiceBeanRemote {

    List<CTextTemplate> getAllTextTemplates();

    List<CTextTemplate> getAllTextTemplateEntries();

    List<CTextTemplate> getAllTextTemplatesForContext(TextTemplateTypeEn templateType);

    List<Long> getAllTextTemplateIds();

    List<String> getAllTextTemplateContent();

    String getTextTemplateContent(long Id);

    boolean removeTextTemplateById(Long id);

    CTextTemplate getTextTemplateById(Long cTextTemplateId);

    long addTextTemplate(CTextTemplate cTextTemplate);

    boolean updateTextTemplate(CTextTemplate cTextTemplate);

    List<CdbUser2Role> getUser2RoleByUserId(Long userId);

    CdbUserRoles findRoleById(long roleId);

    List<CTextTemplate2Context> getAllTextTemplate2ContextBasedOnContext(TextTemplateTypeEn templateType);

    List<CTextTemplate2UserRole> getAllTextTemplate2UserRoleBasedOnRole(CdbUserRoles cdbUserRole);

}
