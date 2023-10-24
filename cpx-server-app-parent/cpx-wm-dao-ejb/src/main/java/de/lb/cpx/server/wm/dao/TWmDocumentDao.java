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
 *    2016  Husser - initial API and implementation and/or initial documentation
 *    2016  Wilde - extends service implementation
 */
package de.lb.cpx.server.wm.dao;

import de.lb.cpx.server.dao.AbstractCpxDao;
import de.lb.cpx.wm.model.TWmDocument;
import de.lb.cpx.wm.model.TWmDocument_;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

/**
 * Object to Access T_WM_DOCUMENT Table
 *
 * @author Husser
 */
@Stateless
@SuppressWarnings("unchecked")
public class TWmDocumentDao extends AbstractCpxDao<TWmDocument> {

    private static final Logger LOG = Logger.getLogger(TWmDocumentDao.class.getName());

    public TWmDocumentDao() {
        super(TWmDocument.class);
    }

    /**
     * fetch the content of a specific file
     *
     * @param documentId id of the document
     * @return byte array of the stored content
     */
    public byte[] getContent(long documentId) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<byte[]> query = criteriaBuilder.createQuery(byte[].class);

        Root<TWmDocument> from = query.from(TWmDocument.class);
        query.select(from.get(TWmDocument_.content));
        query.where(criteriaBuilder.equal(from.get(TWmDocument_.id), documentId));
        TypedQuery<byte[]> criteriaQuery = getEntityManager().createQuery(query);
        List<byte[]> result = criteriaQuery.getResultList();
        return !result.isEmpty() ? result.get(0) : null;
    }

    /**
     * checks if a document exists with that file name in the database for a
     * specific process
     *
     * @param fileName name of the file
     * @param processId process id
     * @return exists or not
     */
    public boolean docExists(String fileName, long processId) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);

        Root<TWmDocument> from = query.from(TWmDocument.class);
        query.select(criteriaBuilder.countDistinct(from));
        query.where(criteriaBuilder.equal(from.get(TWmDocument_.name), fileName), criteriaBuilder.equal(from.get(TWmDocument_.process), processId));

        TypedQuery<Long> criteriaQuery = getEntityManager().createQuery(query);

        return criteriaQuery.getSingleResult() >= 1;
    }

//    public void addDocument(byte[] doc_file) {
////        TWmDocument_.
////        getEntityManager().getTransaction().begin();
//        getEntityManager().createNativeQuery("INSERT INTO TWmDocument (content, name, filepath, version, process)"
//                + "VALUES(doc_file, 'generatedDoc', ‪'C:\\Users\\nandola\\Desktop\\Templates\\Generated_Document.docx', 1,1)")
//              //  .setParameter(1, doc_file)
//                .executeUpdate();
////        getEntityManager().getTransaction().commit();
//    }
//        public void addDocument() {
////        TWmDocument_.
////        getEntityManager().getTransaction().begin();
//        getEntityManager().createNativeQuery("INSERT INTO TWmDocument (name, filepath, version, process)"
//                + "VALUES('generatedDoc', ‪'C:\\Users\\nandola\\Desktop\\Templates\\Generated_Document.docx', 1,1)")
//              //  .setParameter(1, doc_file)
//                .executeUpdate();
////        getEntityManager().getTransaction().commit();
//persist(transientInstance);
//    }
    public List<TWmDocument> findAllForProcess(long pProcessId) {
        long startTime = System.currentTimeMillis();
        LOG.log(Level.INFO, "Find all documents for process with id " + pProcessId);
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TWmDocument> query = cb.createQuery(getEntityClass());
        Root<TWmDocument> from = query.from(getEntityClass());
        query.where(cb.equal(from.get(TWmDocument_.process), pProcessId));
        query.orderBy(cb.desc(from.get(TWmDocument_.creationDate)));
        query.select(cb.construct(TWmDocument.class, from.get(TWmDocument_.id),
                from.get(TWmDocument_.process),
                from.get(TWmDocument_.name),
                from.get(TWmDocument_.filePath),
                from.get(TWmDocument_.documentType),
                //from.get(TWmDocument_.modificationUserId),
                from.get(TWmDocument_.fileSize),
                from.get(TWmDocument_.creationDate),
                from.get(TWmDocument_.creationUser),
                from.get(TWmDocument_.modificationDate),
                from.get(TWmDocument_.modificationUser),
                from.get(TWmDocument_.documentDate),
                from.get(TWmDocument_.version)
        //from.get(TWmDocument_.content) //never ever do this or you will die in agony!
        ));

        TypedQuery<TWmDocument> criteriaQuery = getEntityManager().createQuery(query);
        List<TWmDocument> result = criteriaQuery.getResultList();
        LOG.log(Level.FINER, result.size() + " documents found for process id " + pProcessId + " in " + (System.currentTimeMillis() - startTime) + " ms");

        return result;
    }
//    public List<TWmDocument> findAllForProcess(long pProcessId) {
//        long startTime = System.currentTimeMillis();
//        LOG.log(Level.INFO, "Find all documents for process with id " + pProcessId);
//        Criteria criteriaQuery = getSession().createCriteria(TWmDocument.class)
//                .add(Restrictions.eq(TWmDocument_.process.getName() + ".id", pProcessId))
//                .addOrder(Order.desc(TWmDocument_.creationDate.getName()))
//                .setProjection(Projections.projectionList()
//                        .add(Projections.property(TWmDocument_.id.getName()), TWmDocument_.id.getName())
//                        .add(Projections.property(TWmDocument_.filePath.getName()), TWmDocument_.filePath.getName())
//                        .add(Projections.property(TWmDocument_.fileSize.getName()), TWmDocument_.fileSize.getName())
//                        .add(Projections.property(TWmDocument_.documentType.getName()), TWmDocument_.documentType.getName())
//                        .add(Projections.property(TWmDocument_.creationDate.getName()), TWmDocument_.creationDate.getName())
//                        .add(Projections.property(TWmDocument_.creationUser.getName()), TWmDocument_.creationUser.getName())
//                        .add(Projections.property(TWmDocument_.modificationDate.getName()), TWmDocument_.modificationDate.getName())
//                        .add(Projections.property(TWmDocument_.modificationUser.getName()), TWmDocument_.modificationUser.getName())
//                        .add(Projections.property(TWmDocument_.modificationUserId.getName()), TWmDocument_.modificationUserId.getName())
//                        .add(Projections.property(TWmDocument_.name.getName()), TWmDocument_.name.getName())
//                        .add(Projections.property(TWmDocument_.process.getName()), TWmDocument_.process.getName())
//                        .add(Projections.property(TWmDocument_.version.getName()), TWmDocument_.version.getName()))
//                //.add(Projections.property(TWmDocument_.content.getName())) //never ever do this or you will die in agony!
//                .setResultTransformer(Transformers.aliasToBean(TWmDocument.class));
//
//        List<TWmDocument> result = criteriaQuery.list();
//        LOG.log(Level.FINER, result.size() + " documents found for process id " + pProcessId + " in " + (System.currentTimeMillis() - startTime) + " ms");
//
//        return result;
////        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
////        CriteriaQuery<TWmDocument> query = criteriaBuilder.createQuery(TWmDocument.class);
////        query.Root<TWmDocument> from = query.from(TWmDocument.class);
////        //add two Where-Conditions like csCaseNamber and equals hospitalIdent 
////        query.where(criteriaBuilder.equal(from.get(TWmDocument_.process), pProcessId));
////
////        TypedQuery<TWmDocument> criteriaQuery = getEntityManager().createQuery(query);
////        return criteriaQuery.getResultList();
//    }

    /* The CriteriaUpdate interface can be used to implement bulk update operations.
      But be careful, these operations are directly mapped to database update operations.
    Therefore the persistence context is not synchronized with the result and there is no optimistic locking of the involved entities.*/
    public void updateDocumentName(TWmDocument doc, String docNameToUpdate) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        // create update
        CriteriaUpdate<TWmDocument> update = criteriaBuilder.createCriteriaUpdate(TWmDocument.class);
        // set the root class
        final Root<TWmDocument> root = update.from(TWmDocument.class);
        final Path<TWmDocument> lPath = root.get("id");
        // set update and where clause
        update.set(TWmDocument_.name, docNameToUpdate);
        update.where(criteriaBuilder.equal(lPath, doc.getId()));
//        update.where(criteriaBuilder.equals(TWmDocument_.id,doc.id));

        // perform update
        getEntityManager().createQuery(update).executeUpdate();

    }
}
