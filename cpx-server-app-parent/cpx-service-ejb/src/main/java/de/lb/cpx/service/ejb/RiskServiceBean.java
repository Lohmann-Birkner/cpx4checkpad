/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.lb.cpx.model.enums.PlaceOfRegEn;
import de.lb.cpx.model.enums.VersionRiskTypeEn;
import de.lb.cpx.server.wm.dao.TWmFinalisationRiskDao;
import de.lb.cpx.server.wm.dao.TWmRiskDao;
import de.lb.cpx.server.wm.dao.TWmRiskDetailsDao;
import de.lb.cpx.wm.model.TWmFinalisationRisk; 
import de.lb.cpx.wm.model.TWmRisk;
import de.lb.cpx.wm.model.TWmRiskDetails;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.hibernate.Hibernate;

/**
 *
 * @author wilde
 */
@Stateless
public class RiskServiceBean implements RiskServiceBeanRemote {

    @Inject
    private TWmRiskDao riskDao;
    
    @Inject
    private TWmRiskDetailsDao riskDetailsDao;
    
    @Inject
    private TWmFinalisationRiskDao finalisationRiskDao;

    @Override
    public List<TWmRisk> findRisks4CaseVersion(long id) {
       return riskDao.findRisks4CaseVersion(id);
    }
    
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public TWmRisk findRisk4CaseAndPlaceOrReg(long pCaseId, PlaceOfRegEn pPlaceOfReg) {
        TWmRisk retRisk =  riskDao.findRisk4CaseAndPlaceOrReg(pCaseId, pPlaceOfReg);
        if(retRisk!= null && retRisk.getRiskDetails() != null){
            Hibernate.initialize(retRisk.getRiskDetails());
            for(TWmRiskDetails det : retRisk.getRiskDetails()){
                Hibernate.initialize(det);
            }
        }
        return retRisk;
    }

    @Override
    public TWmRisk saveRiskEntity(TWmRisk pRisk) {
        return riskDao.merge(pRisk);
    }

    @Override
    public void removeRiskDetails(List<Long> pRemovedRiskIds) {
        if(pRemovedRiskIds == null || pRemovedRiskIds.isEmpty()){
            return;
        }
        riskDetailsDao.remove4Ids(pRemovedRiskIds);
    }

    @Override
    public TWmRisk getCompletionRisk(long pCaseId) {
        return riskDao.findActualRisk(pCaseId, PlaceOfRegEn.REQUEST_FINALISATION, VersionRiskTypeEn.CASE_FINALISATION);//getSingleRiskForAreaOfReg(pCaseId,PlaceOfRegEn.REQUEST_FINALISATION);
    }

    @Override
    public TWmRisk getBillingRisk(long pCaseId) {
        return riskDao.getSingleRiskForAreaOfReg(pCaseId,PlaceOfRegEn.BEFORE_BILLING);
    }

    @Override
    public TWmRisk saveOrUpdateRisk(TWmRisk pRisk) {
        pRisk = riskDao.merge(pRisk);
        return riskDao.saveOrUpdate(pRisk);
    }

    @Override
    public TWmRisk getRequestRisk(long pCaseId, long pRequestId) {
        return riskDao.getRequestRisk(pCaseId, pRequestId);
    }

    @Override
    public List<TWmRisk> findAllRisks(long pCaseId) {
        return riskDao.findAllRisks(pCaseId);
    }

    @Override
    public TWmRisk findActualRisk(long pCaseId,PlaceOfRegEn placeOfRegEn, VersionRiskTypeEn pType) {
        return riskDao.findActualRisk(pCaseId,placeOfRegEn,pType);
    }

    @Override
    public List<TWmRisk> findActualRequestRisks(long pCaseId, PlaceOfRegEn placeOfRegEn) {
        return riskDao.findActualRequestRisks(pCaseId,placeOfRegEn);
    }
    @Override
    public TWmFinalisationRisk saveOrUpdateFinalisationRisk(TWmFinalisationRisk pRisk) {
        pRisk = finalisationRiskDao.merge(pRisk);
        return finalisationRiskDao.saveOrUpdate(pRisk);
    }

    @Override
    public TWmFinalisationRisk findFinalisationRisk(long pProcessCompletionId) {
        return finalisationRiskDao.findFinalisationRisk(pProcessCompletionId);
    }
    
    @Override 
    public List<TWmRiskDetails> findRisksFromActualBillingVersion(long pCaseId){
        return riskDetailsDao.findRisks4ActualVersionType(pCaseId, VersionRiskTypeEn.BEFORE_BILLING);
    }
    
    @Override
    public List<TWmRiskDetails> findRisksFromActualAuditVersion(long pCaseId){
        return riskDetailsDao.findRisks4ActualVersionType(pCaseId, VersionRiskTypeEn.AUDIT_MD);
    }

    @Override
    public int resetActual4OtherRisks(long pCaseId, long pRiskId, VersionRiskTypeEn pVersionRiskType, boolean pLocal){
        return riskDao.resetActual4OtherRisks( pCaseId,  pRiskId, pVersionRiskType, pLocal) ; 
    }
    
}
