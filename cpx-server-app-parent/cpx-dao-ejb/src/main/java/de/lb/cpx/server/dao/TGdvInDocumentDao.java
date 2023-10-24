/*
 * Copyright (c) 2021 Lohmann & Birkner.
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
 *    2021  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.dao;

import de.lb.cpx.gdv.model.TGdvAttachment;
import de.lb.cpx.gdv.model.TGdvAttachment_;
import de.lb.cpx.gdv.model.TGdvInDocument;
import de.lb.cpx.gdv.model.TGdvInDocument_;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;



/**
 *
 * @author gerschmann
 */
@Stateless
@SuppressWarnings("unchecked")
public class TGdvInDocumentDao extends AbstractCpxDao<TGdvInDocument>{
    
    public TGdvInDocumentDao(){
        super(TGdvInDocument.class);
    }

    public  List<TGdvInDocument>  findContent2DamageNr(String pDamageNr) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TGdvInDocument> query = cb.createQuery(TGdvInDocument.class);
        Root<TGdvInDocument> from = query.from(TGdvInDocument.class);
        Join<TGdvInDocument, TGdvAttachment> join = from.join(TGdvInDocument_.attchments);
        query.where(cb.equal(join.get(TGdvAttachment_.damageReportNumber), pDamageNr));
         TypedQuery<TGdvInDocument> criteriaQuery = getEntityManager().createQuery(query);
        return criteriaQuery.getResultList();
    }
    
}
