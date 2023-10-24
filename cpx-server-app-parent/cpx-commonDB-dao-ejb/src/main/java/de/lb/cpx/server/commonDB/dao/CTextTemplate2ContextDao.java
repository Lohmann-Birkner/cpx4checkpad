/* 
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.commonDB.dao;

import de.lb.cpx.model.enums.TextTemplateTypeEn;
import de.lb.cpx.server.commonDB.model.CTextTemplate2Context;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author nandola
 */
@Stateless
@SuppressWarnings("unchecked")
public class CTextTemplate2ContextDao extends AbstractCommonDao<CTextTemplate2Context> {

    public CTextTemplate2ContextDao() {
        super(CTextTemplate2Context.class);
    }

    /**
     * Get all entries from C_TEXT_TEMPLATE_2_CONTEXT table (sorted based on
     * text template)
     *
     * @return list
     */
    public List<CTextTemplate2Context> getAllTextTemplate2ContextEntries() {
        List<CTextTemplate2Context> list = null;
        Query query = getEntityManager().createQuery("from " + CTextTemplate2Context.class.getSimpleName() + " a order by a.textTemplate");
        list = query.getResultList();
        return list;
    }

    /**
     * @param templateType the type of text template
     * @return sorted list of matched entries.
     */
    public List<CTextTemplate2Context> getAllTextTemplate2ContextBasedOnContext(TextTemplateTypeEn templateType) {
        List<CTextTemplate2Context> lResults = null;
        String queryName = "select u from " + CTextTemplate2Context.class.getSimpleName() + " u where textTemplateType =:idNbr order by u.textTemplate";
        Query query = getEntityManager().createQuery(queryName);
        query.setParameter("idNbr", templateType);
        lResults = query.getResultList();
        if (lResults == null || lResults.isEmpty()) {
            return null;
        }
        return lResults;
    }

}
