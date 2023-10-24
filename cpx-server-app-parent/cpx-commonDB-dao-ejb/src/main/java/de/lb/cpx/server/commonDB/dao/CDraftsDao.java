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
 *    2016  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.commonDB.dao;

import de.lb.cpx.server.commonDB.model.CDrafts;
import de.lb.cpx.server.commonDB.model.CDrafts_;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author nandola, sklarow
 */
@Stateless
@SuppressWarnings("unchecked")
public class CDraftsDao extends AbstractCommonDao<CDrafts> {

    public CDraftsDao() {
        super(CDrafts.class);
    }

//    /**
//     * @return list of all templates.
//     */
//    public List<CDrafts> getAllTemplates() {
//        final TypedQuery<CDrafts> query = getEntityManager().createQuery("from CDrafts", CDrafts.class);
//        return query.getResultList();
//    }
    /**
     *
     * @param cat1Ids list of Internal ID Category 1
     * @param cat2Ids list of Internal ID Category 2
     * @param cat3Ids list of Internal ID Category 3
     * @return list of all templates in Category 1,Category 2,Category 3
     */
    public List<CDrafts> findAllTemplatesByCategoryInternalId(List<Long> cat1Ids, List<Long> cat2Ids, List<Long> cat3Ids) {

        String queryName = "select u from CDrafts u ";
        if (!cat1Ids.isEmpty()) {
            if (queryName.contains("where")) {
                queryName += " and category1 in :id1Nbr";
            } else {
                queryName += " where category1 in :id1Nbr";
            }

        }
        if (!cat2Ids.isEmpty()) {
            if (queryName.contains("where")) {
                queryName += " and category2 in :id2Nbr";
            } else {
                queryName += " where category2 in :id2Nbr";
            }

        }
        if (!cat3Ids.isEmpty()) {
            if (queryName.contains("where")) {
                queryName += " and category3 in :id3Nbr";
            } else {
                queryName += " where category3 in :id3Nbr";
            }

        }
        final TypedQuery<CDrafts> query = getEntityManager().createQuery(queryName, CDrafts.class);

        if (!cat1Ids.isEmpty()) {
            query.setParameter("id1Nbr", cat1Ids);
        }
        if (!cat2Ids.isEmpty()) {
            query.setParameter("id2Nbr", cat2Ids);
        }
        if (!cat3Ids.isEmpty()) {
            query.setParameter("id3Nbr", cat3Ids);
        }
        List<CDrafts> lResults = query.getResultList();
        if (lResults.isEmpty()) {
            return null;
        }
        return lResults;
    }

//    /**
//     * returns list of template ids.
//     *
//     * @return IDs
//     */
//    public List<Long> getAllTemplateIds() {
//        final TypedQuery<Long> query = getEntityManager().createQuery("select id from CDrafts c", Long.class);
//        return query.getResultList();
//    }
    /**
     * returns list of template content.
     *
     * @return BLOB
     */
    public List<byte[]> getAllTemplateContent() {
        final TypedQuery<byte[]> query = getEntityManager().createQuery("select c.draftContent from CDrafts c", byte[].class);
//        ResultSet.getBinaryStream()
        return query.getResultList();
    }

//    public List<Lob> getAllTemplateContent() {
//        final TypedQuery<Lob> query = getEntityManager().createQuery("select c.draftContent from CDrafts c", Lob.class);
////        ResultSet.getBinaryStream()
//        return query.getResultList();
//    }
    /**
     * fetch the content of a specific template
     *
     * @param Id id of the template
     * @return byte array of the stored content
     */
    public byte[] getTemplateContent(long Id) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<byte[]> query = criteriaBuilder.createQuery(byte[].class);
        Root<CDrafts> from = query.from(CDrafts.class);
        query.select(from.get(CDrafts_.draftContent));
        query.where(criteriaBuilder.equal(from.get(CDrafts_.id), Id));
        TypedQuery<byte[]> criteriaQuery = getEntityManager().createQuery(query);
        List<byte[]> result = criteriaQuery.getResultList();
        return !result.isEmpty() ? result.get(0) : null;
    }

    /**
     * returns list of template directory.
     *
     * @return String
     */
    public List<String> getAllTemplateDirectory() {
        final TypedQuery<String> query = getEntityManager().createQuery("select c.draftDir from CDrafts c", String.class);
        return query.getResultList();
    }

//    // remove particular template based on ID. 
//    // @Override
//    public void deleteById(final long id) {
//        try {
//            Query query = getEntityManager()
//                    .createNativeQuery("delete from CDrafts c where id = " + String.valueOf(id));
//
//            query.executeUpdate();
//
//            LOG.log(Level.INFO, "template is removed");
//
//        } catch (final RuntimeException re) {
//            LOG.log(Level.INFO, "remove failed", re);
//            throw re;
//        }
//    }
    /*
    public CDrafts findTemplateByName(String templateName) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<CDrafts> query = criteriaBuilder.createQuery(CDrafts.class);
        Root<CDrafts> from = query.from(CDrafts.class);
        query.where(criteriaBuilder.like(criteriaBuilder.lower(from.get(CDrafts_.draftName)), templateName.toLowerCase()));
        List<CDrafts> results = addEntityGraph(getEntityManager().createQuery(query)).getResultList();
        if (results.size() > 1) {
            LOG.warning("Methode: findCaseByNumber return resultset > 1, only 1 result is expected! Size of results: " + results.size());
        }
        return results.get(0);
    }

    private Object addEntityGraph(TypedQuery<CDrafts> createQuery) {
        EntityGraph<CDrafts> toFetch = getEntityManager().createEntityGraph(CDrafts.class);
        toFetch.addSubgraph(CDrafts_.draftContent);
        toFetch.addSubgraph(CDrafts_.draftName);
        createQuery.setHint(EntityGraphType.getLoadGraphType(), toFetch);
        return createQuery;
    }
     */
    /**
     * Get all entries from C_DRAFTS table
     *
     * @return list
     */
    public List<CDrafts> getAllDraftsEntries() {
        List<CDrafts> list = null;
        Query query = getEntityManager().createQuery("from " + CDrafts.class.getSimpleName() + " a order by a.draftName");
        list = query.getResultList();
        return list;
    }

//    /**
//     * Update the ms word template
//     *
//     * @param pCDrafts ms word template
//     * @return state of update process
//     */
//    public boolean updateWordTemplate(CDrafts pCDrafts) {
//        boolean checkUpdateState = false;
//        if (pCDrafts.getId() <= 0L) {
//            checkUpdateState = false;
//        } else {
//            merge(pCDrafts);
//            checkUpdateState = true;
//        }
//        return checkUpdateState;
//    }
//
//    /**
//     * Delete ms word template by id
//     *
//     * @param id ms word template id
//     * @return boolean value for delete state
//     */
//    public boolean removeWordTemplateById(Long id) {
//        deleteById(id);
//        boolean isDeleted = true;
//        return isDeleted;
//    }
//    /**
//     * Get ms word template by id
//     *
//     * @param pCDraftsId mdk item id
//     * @return searched mdk item
//     */
//    public CDrafts getWordTemplateById(Long pCDraftsId) {
//        List<CDrafts> lResults = null;
//        String queryName = "select u from " + CDrafts.class.getSimpleName() + " u where id =:idNbr";
//        Query query = getEntityManager().createQuery(queryName);
//        query.setParameter("idNbr", pCDraftsId);
//        lResults = query.getResultList();
//        if (lResults == null || lResults.isEmpty()) {
//            return null;
//        }
//        return lResults.get(0);
//    }
//
//    /**
//     * Add new ms word template
//     *
//     * @param pCDrafts new ms word template
//     * @return id for new new ms word template
//     */
//    public long addNewWordTemplate(CDrafts pCDrafts) {
//        return addNewItem(pCDrafts);
////        persist(pCDrafts);
////        flush();
////        long id = pCDrafts.getId();
////        return id;
//    }
    public byte[] getDraftContent(long draftId) {
        CriteriaBuilder qb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<byte[]> cq = qb.createQuery(byte[].class);
        Root<CDrafts> from = cq.from(getEntityClass());
        cq.select(from.get(CDrafts_.DRAFT_CONTENT));
        cq.where(qb.equal(from.get("id"), draftId));
        return getEntityManager().createQuery(cq).getSingleResult();
    }

}
