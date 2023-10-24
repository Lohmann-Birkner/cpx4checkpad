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
package de.lb.cpx.server.commonDB.dao;

import de.lb.cpx.model.enums.TextTemplateTypeEn;
import de.lb.cpx.server.commonDB.model.CTextTemplate;
import de.lb.cpx.server.commonDB.model.CTextTemplate_;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * DAO for the CTextTemplate.
 *
 * @author nandola
 */
@Stateless
@SuppressWarnings("unchecked")
public class CTextTemplateDao extends AbstractCommonDao<CTextTemplate> {

    public CTextTemplateDao() {
        super(CTextTemplate.class);
    }

//    /**
//     * @return list of all Text templates.
//     */
//    public List<CTextTemplate> getAllTextTemplates() {
//        final TypedQuery<CTextTemplate> query = getEntityManager().createQuery("from CTextTemplate", CTextTemplate.class);
//        return query.getResultList();
//    }
    /**
     * Get all entries from C_TEXT_TEMPLATE table (sorted based on text template
     * name)
     *
     * @return list
     */
    public List<CTextTemplate> getAllTextTemplateEntries() {
        List<CTextTemplate> list = null;
        Query query = getEntityManager().createQuery("from " + CTextTemplate.class.getSimpleName() + " a order by a.templateName");
        list = query.getResultList();
        return list;
    }

    /**
     * @param templateType the type of text template
     * @return sorted list of matching context Text templates.
     */
    public List<CTextTemplate> getAllTextTemplatesForContext(TextTemplateTypeEn templateType) {
        List<CTextTemplate> lResults = null;
        String queryName = "select u from " + CTextTemplate.class.getSimpleName() + " u where templateContextType =:idNbr order by u.templateSort";
        Query query = getEntityManager().createQuery(queryName);
        query.setParameter("idNbr", templateType);
        lResults = query.getResultList();
        if (lResults == null || lResults.isEmpty()) {
            return null;
        }
        return lResults;
    }

//    /**
//     * returns list of text template ids.
//     *
//     * @return IDs
//     */
//    public List<Long> getAllTextTemplateIds() {
//        final TypedQuery<Long> query = getEntityManager().createQuery("select id from CTextTemplate c", Long.class);
//        return query.getResultList();
//    }
    /**
     * returns list of text template objects (contents).
     *
     * @return CLOB
     */
    public List<String> getAllTextTemplateContent() {
        final TypedQuery<String> query = getEntityManager().createQuery("select t.templateContent from CTextTemplate t", String.class);
        return query.getResultList();
    }

    /**
     * fetch the content of a specific text template
     *
     * @param Id id of the text template
     * @return byte array of the stored content
     */
//    public byte[] getTextTemplateContent(long Id) {
//        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
//        CriteriaQuery<byte[]> query = criteriaBuilder.createQuery(byte[].class);
//        Root<CTextTemplate> from = query.from(CTextTemplate.class);
//        query.select(from.get(CTextTemplate_.templateContent));
//        query.where(criteriaBuilder.equal(from.get(CTextTemplate_.id), Id));
//        TypedQuery<byte[]> criteriaQuery = getEntityManager().createQuery(query);
//        List<byte[]> result = criteriaQuery.getResultList();
//        return result.size() >= 1 ? result.get(0) : null;
//    }
    public String getTextTemplateContent(long Id) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<String> query = criteriaBuilder.createQuery(String.class);
        Root<CTextTemplate> from = query.from(CTextTemplate.class);
        query.select(from.get(CTextTemplate_.templateContent));
        query.where(criteriaBuilder.equal(from.get(CTextTemplate_.id), Id));
        TypedQuery<String> criteriaQuery = getEntityManager().createQuery(query);
        List<String> result = criteriaQuery.getResultList();
        return !result.isEmpty() ? result.get(0) : null;
    }

}
