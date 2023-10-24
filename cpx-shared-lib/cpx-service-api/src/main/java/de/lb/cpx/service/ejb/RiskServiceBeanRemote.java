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
import de.lb.cpx.wm.model.TWmFinalisationRisk;
import de.lb.cpx.wm.model.TWmRisk;
import de.lb.cpx.wm.model.TWmRiskDetails;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author wilde
 */
@Remote
public interface RiskServiceBeanRemote{
    /**
     * finds all risks for case 
     * @param pCaseId case id
     * @return list of risks found
     */
    List<TWmRisk> findRisks4CaseVersion(long pCaseId);
    
    /**
     * finds risk for caseId and Place of Reg
     * @param pCaseId case id
     * @param pPlaceOfReg place of reg
     * @return returns ris or null
     */
    TWmRisk findRisk4CaseAndPlaceOrReg(long pCaseId, PlaceOfRegEn pPlaceOfReg);

    /**
     * saves Risk entity in DB
     * @param pRisk 
     * @return  returns saved entity
     */
    TWmRisk saveRiskEntity(TWmRisk pRisk);

    public void removeRiskDetails(List<Long>  pRemovedRisksIds);
    
    /**
     * @param pCaseId
     * @return completion Risk for this case
     */
    public TWmRisk getCompletionRisk(long pCaseId);
    /**
     * @param pCaseId case database id to identify risk
     * @return risk for the billing
     */
    public TWmRisk getBillingRisk(long pCaseId);
    /**
     * @param pRisk risk object to save or update
     * @return newly updated or stored risk
     */
    public TWmRisk saveOrUpdateRisk(TWmRisk pRisk);
    
    /**
     * @param pCaseId db caseId
     * @param pRequestId db requestId
     * @return risk for the request
     */
    public TWmRisk getRequestRisk(long pCaseId, long pRequestId);
    
    /**
     * @param pCaseId case database id
     * @return all risks for that case
     */
    public List<TWmRisk> findAllRisks(long pCaseId);

    public TWmRisk findActualRisk(long pCaseId,PlaceOfRegEn placeOfRegEn, VersionRiskTypeEn pType);

    public List<TWmRisk> findActualRequestRisks(long id, PlaceOfRegEn placeOfRegEn);

    public TWmFinalisationRisk saveOrUpdateFinalisationRisk(TWmFinalisationRisk pRisk);

    public TWmFinalisationRisk findFinalisationRisk(long id);

    public List<TWmRiskDetails> findRisksFromActualBillingVersion(long pCaseId);

    public List<TWmRiskDetails> findRisksFromActualAuditVersion(long pCaseId);

        /**
     * for all risks of the actual case with PlaceOfRegEn from pRisk the flag RISK_ACTUAL_4_REG will be resetet.the pRisk will not be included
     * @param pCaseId actual case
     * @param pRiskId risk to exclude

     * @param pLocal is local?
     * @return number of reseted objects
     */
    public int resetActual4OtherRisks(long pCaseId, long pRiskId, VersionRiskTypeEn pVersionRiskType, boolean pLocal);


}
