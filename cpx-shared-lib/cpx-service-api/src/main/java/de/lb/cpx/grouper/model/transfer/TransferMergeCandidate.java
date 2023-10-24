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
package de.lb.cpx.grouper.model.transfer;

import de.checkpoint.drg.GDRGModel;
import de.checkpoint.server.rmServer.caseManager.RmcPeppWiederaufnahme;
import de.checkpoint.server.rmServer.caseManager.RmcWiederaufnahme;
import de.checkpoint.server.rmServer.caseManager.RmcWiederaufnahmeIF;
import de.lb.cpx.model.enums.AdmissionReasonEn;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.DrgPartitionEn;
import de.lb.cpx.model.enums.GrouperMdcOrSkEn;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gerschmann
 */
public class TransferMergeCandidate {

    private static final Logger LOG = Logger.getLogger(TransferMergeCandidate.class.getName());
    
    private long caseId;
    private long grpResId;
    private CaseTypeEn grpresType;
    private RmcWiederaufnahmeIF rmcCase = null;
    private GDRGModel model = GDRGModel.AUTOMATIC;
    private boolean isAuto = true;

    public long getCaseId() {
        return caseId;
    }

    public void setCaseId(long id) {
        caseId = id;
    }

    public long getGrpResId() {
        return grpResId;
    }

    public void setGrpResId(long grpResId) {
        this.grpResId = grpResId;
    }

    public CaseTypeEn getGrpresType() {
        return grpresType;
    }

    public void setGrpresType(CaseTypeEn grpresType) {
        this.grpresType = grpresType;
    }

    public RmcWiederaufnahmeIF getRmcCase() {
        return rmcCase;
    }

    public void setRmcCase(RmcWiederaufnahmeIF rmcCase) {
        this.rmcCase = rmcCase;
    }

    public RmcWiederaufnahmeIF createRmcCase(String hospIdent, 
            String insuranceIdent,
            String caseNr,
            Date admDate, 
            Date disDate, 
            AdmissionReasonEn admissionReason12, 
            int los, 
            String grpResCode, 
            GrouperMdcOrSkEn grpResGroup, 
            DrgPartitionEn drgPartition, 
            boolean drgException, 
            int drgHtp, 
            boolean dayCare, 
            boolean negotiated) {
//        RmcWiederaufnahmeIF rmcCase = null;
           switch (grpresType) {
            case PEPP:
                rmcCase = new RmcPeppWiederaufnahme();
                break;
            case DRG:
                if(admissionReason12.equals(AdmissionReasonEn.ar03) || admissionReason12.equals(AdmissionReasonEn.ar04)){
                    return null;
                }
                rmcCase = new RmcWiederaufnahme();
                break;
            default:
                LOG.log(Level.WARNING, "Unknown case type: " + grpresType);
        }
        if(rmcCase != null){
            rmcCase.setIknr(hospIdent == null?"":hospIdent);
            rmcCase.setCaseNr(caseNr == null?"":caseNr);
            rmcCase.setAufnahmedatum(new java.sql.Date(admDate.getTime()));
            rmcCase.setEntlassungsdatum(new java.sql.Date(disDate.getTime()));
            rmcCase.setAufnahmegrund(admissionReason12.getIdInt());
            rmcCase.setDrgOrPepp(grpResCode);
            rmcCase.setMdcOrSk(grpResGroup != null ? grpResGroup.getId() : "");
        }
        switch(grpresType){
            case PEPP:

                ((RmcPeppWiederaufnahme)rmcCase).m_verhandelbar = negotiated ? 1 : 0;
                if(!negotiated && grpResCode.startsWith("PF")){
                    ((RmcPeppWiederaufnahme)rmcCase).m_verhandelbar = 1;
                }
                if(admissionReason12.equals(AdmissionReasonEn.ar03)) {
                    ((RmcPeppWiederaufnahme)rmcCase).m_teilstationaer = 1;
		} else if(admissionReason12.equals(AdmissionReasonEn.ar10)){
			((RmcPeppWiederaufnahme)rmcCase).m_teilstationaer = 2;
		} else {
			((RmcPeppWiederaufnahme)rmcCase).m_teilstationaer = 0;
		}

                return rmcCase;
            case DRG:
                ((RmcWiederaufnahme)rmcCase).m_partition = (drgPartition == null || drgPartition.name() == null) ? ' ' : drgPartition.name().charAt(0);
                ((RmcWiederaufnahme)rmcCase).m_drgKatalogWAAusnahme = drgException;
                ((RmcWiederaufnahme)rmcCase).m_oGVD = drgHtp;
                ((RmcWiederaufnahme)rmcCase).m_adrg = (grpResCode == null || grpResCode.length() < 4 )? grpResCode : grpResCode.substring(0, 3);
                return rmcCase;
        }

        
        return null;
}

    public static void printCandidateString(RmcWiederaufnahmeIF candidate) {
        try {
            LOG.log(Level.INFO, "Fall:{0} ikz: {1} DRG/pepp: {2} mergeID: {3} rules: \n"
                    + "eins: {4}, zwei: {5}, drei: {6}, fier: {7}, fuenf: {8}, sechs: {9}, sieben: {10}, acht: {11}, neun: {12}, zehn: {13}", 
                    new Object[]{candidate.getFallNr(), candidate.getIkz(), candidate.getdrgOrPepp(), candidate.getMergeId(), 
                        candidate.getEins(), candidate.getZwei(), candidate.getDrei(), candidate.getVier(), candidate.getFuenf(), 
                        candidate.getSechs(), candidate.getSieben(), candidate.getAcht(), candidate.getNeun(), candidate.getZehn()});
        } catch (Exception e) { 
            LOG.log(Level.INFO, "Fehler bei der IKnr - Ermittlung " + candidate.getFallNr(), e);
        }

    }
    

    @Override
    public boolean equals(Object other){
        if(other == null || !(other instanceof TransferMergeCandidate)){
            return false;
        }
        RmcWiederaufnahmeIF rmOther = ((TransferMergeCandidate)other).getRmcCase();

        return (this.getRmcCase() == null && rmOther == null) 
                || (this.getRmcCase() != null && rmOther != null && getRmcCase().equals(rmOther));
    }

    public Integer getMergeId() {
       return this.rmcCase == null?0:rmcCase.getMergeId();
    }
}
