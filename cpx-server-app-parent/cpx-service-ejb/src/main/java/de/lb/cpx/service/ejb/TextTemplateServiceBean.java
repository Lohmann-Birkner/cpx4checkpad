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
import de.lb.cpx.server.commonDB.dao.CTextTemplate2ContextDao;
import de.lb.cpx.server.commonDB.dao.CTextTemplate2UserRoleDao;
import de.lb.cpx.server.commonDB.dao.CTextTemplateDao;
import de.lb.cpx.server.commonDB.dao.CdbUser2RoleDao;
import de.lb.cpx.server.commonDB.dao.CdbUserRolesDao;
import de.lb.cpx.server.commonDB.model.CTextTemplate;
import de.lb.cpx.server.commonDB.model.CTextTemplate2Context;
import de.lb.cpx.server.commonDB.model.CTextTemplate2UserRole;
import de.lb.cpx.server.commonDB.model.CdbUser2Role;
import de.lb.cpx.server.commonDB.model.CdbUserRoles;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Service bean related to text template handling
 *
 * @author nandola
 */
@Stateless
public class TextTemplateServiceBean implements TextTemplateServiceBeanRemote {

    @Inject
    private CTextTemplateDao textTemplateDao;
    @Inject
    private CTextTemplate2ContextDao cTextTemplate2ContextDao;
    @Inject
    private CTextTemplate2UserRoleDao cTextTemplate2UserRoleDao;
    @Inject
    private CdbUser2RoleDao user2RoleDao;
    @Inject
    private CdbUserRolesDao userRolesDao;

    @Override
    public List<CTextTemplate> getAllTextTemplates() {
        //return textTemplateDao.getAllTextTemplates();
        return textTemplateDao.findAll();
    }

    @Override
    public List<CTextTemplate> getAllTextTemplateEntries() {
        return textTemplateDao.getAllTextTemplateEntries();
    }

    @Override
    public List<CTextTemplate> getAllTextTemplatesForContext(TextTemplateTypeEn templateType) {
        return textTemplateDao.getAllTextTemplatesForContext(templateType);
    }

    @Override
    public List<Long> getAllTextTemplateIds() {
        //return textTemplateDao.getAllTextTemplateIds();
        return textTemplateDao.getAllIds();
    }

    @Override
    public List<String> getAllTextTemplateContent() {
        return textTemplateDao.getAllTextTemplateContent();
    }

    @Override
    public String getTextTemplateContent(long Id) {
        return textTemplateDao.getTextTemplateContent(Id);
    }

    @Override
    public boolean removeTextTemplateById(Long id) {
        //return textTemplateDao.removeTextTemplateById(id);
        return textTemplateDao.deleteById(id);
    }

    @Override
    public CTextTemplate getTextTemplateById(Long cTextTemplateId) {
        //return textTemplateDao.getTextTemplateById(cTextTemplateId);
        return textTemplateDao.findById(cTextTemplateId);
    }

    @Override
    public long addTextTemplate(CTextTemplate cTextTemplate) {
        //return textTemplateDao.addTextTemplate(cTextTemplate);
        return textTemplateDao.addNewItem(cTextTemplate);
    }

    @Override
    public boolean updateTextTemplate(CTextTemplate cTextTemplate) {
        //return textTemplateDao.updateTextTemplate(cTextTemplate);
        return textTemplateDao.updateItem(cTextTemplate);
    }

    @Override
    public List<CdbUser2Role> getUser2RoleByUserId(Long userId) {
        return user2RoleDao.getUser2RoleByUserId(userId);
    }

    @Override
    public CdbUserRoles findRoleById(long roleId) {
        return userRolesDao.findRoleById(roleId);
    }

    @Override
    public List<CTextTemplate2Context> getAllTextTemplate2ContextBasedOnContext(TextTemplateTypeEn templateType) {
        return cTextTemplate2ContextDao.getAllTextTemplate2ContextBasedOnContext(templateType);
    }

    @Override
    public List<CTextTemplate2UserRole> getAllTextTemplate2UserRoleBasedOnRole(CdbUserRoles cdbUserRole) {
        return cTextTemplate2UserRoleDao.getAllTextTemplate2UserRoleBasedOnRole(cdbUserRole);
    }

}
