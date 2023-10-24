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
 *    2018  Shahin - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.dao;

//import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseComment;
import de.lb.cpx.model.TCaseComment_;
import de.lb.cpx.model.enums.CommentTypeEn;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Shahin
 */
@Stateless
public class TCaseCommentDao extends AbstractCpxDao<TCaseComment> {

    public TCaseCommentDao() {
        super(TCaseComment.class);
    }

    public List<TCaseComment> findAllForCase(long pCaseId) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TCaseComment> query = criteriaBuilder.createQuery(TCaseComment.class);

        Root<TCaseComment> from = query.from(TCaseComment.class);

        query.where(criteriaBuilder.equal(from.get(TCaseComment_.TCaseId), pCaseId));

        TypedQuery<TCaseComment> criteriaQuery = getEntityManager().createQuery(query);
        return criteriaQuery.getResultList();
    }

    public List<TCaseComment> findAllForCaseAndType(long pCaseId, CommentTypeEn pType) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TCaseComment> query = criteriaBuilder.createQuery(TCaseComment.class);

        Root<TCaseComment> from = query.from(TCaseComment.class);

        query.where(criteriaBuilder.equal(from.get(TCaseComment_.TCaseId), pCaseId), criteriaBuilder.equal(from.get(TCaseComment_.typeEn), pType));

        TypedQuery<TCaseComment> criteriaQuery = getEntityManager().createQuery(query);
        return criteriaQuery.getResultList();
    }

    public TCaseComment findActiveComment(long pCaseId, CommentTypeEn pType) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TCaseComment> query = criteriaBuilder.createQuery(TCaseComment.class);

        Root<TCaseComment> from = query.from(TCaseComment.class);

        query.where(criteriaBuilder.equal(from.get(TCaseComment_.TCaseId), pCaseId),
                criteriaBuilder.equal(from.get(TCaseComment_.typeEn), pType),
                criteriaBuilder.equal(from.get(TCaseComment_.active), Boolean.TRUE));

        TypedQuery<TCaseComment> criteriaQuery = getEntityManager().createQuery(query);
        return getSingleResultOrNull(criteriaQuery);
    }

    /*
    public TCaseComment findActiveFlagComment(long caseId, CommentType commentType) {
        TCaseComment activeComment;
//        String query = "SELECT FROM T_CASE_COMMENT"
//                + " WHERE T_CASE_ID = :caseId "
//                + " AND TYPE_EN = :commentType "
//                + " AND IS_ACTIVE_FL = :isActive ";
//
//        Query nativeQuery = getEntityManager().createNativeQuery(query, TCaseComment.class);
//        nativeQuery.setParameter("caseId", caseId);
//        nativeQuery.setParameter("commentType", 1);
//        nativeQuery.setParameter("isActive", 0);
//
//        if (nativeQuery.getResultList().isEmpty()) {
//            return null;
//        }
//        return getSingleResultOrNull(nativeQuery);
//    }

        final TypedQuery<TCaseComment> query = getEntityManager().createQuery("select cc from " + TCaseComment.class.getSimpleName() + " cc WHERE T_CASE_ID = :caseId AND TYPE_EN = :commentType AND IS_ACTIVE_FL = :isActive", TCaseComment.class);
        query.setParameter("caseId", caseId);
        query.setParameter("commentType", commentType);
        query.setParameter("isActive", Boolean.TRUE);

        if (query.getResultList() != null) {
            activeComment = query.getSingleResult();
            return activeComment;
        }
        return null;

    }
     */
}
