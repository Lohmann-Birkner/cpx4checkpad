/* 
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.grouper.model.transfer;

import de.checkpoint.drg.GrouperInterfaceBasic;
import de.checkpoint.ruleGrouper.CRGInputOutput;
import de.checkpoint.ruleGrouper.CRGRisk;
import de.checkpoint.ruleGrouper.CRGRule;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.DrgCorrTypeEn;
import de.lb.cpx.model.enums.GroupResultPdxEn;
import de.lb.cpx.model.enums.GrouperStatusEn;
import de.lb.cpx.model.enums.RuleTypeEn;
import de.lb.cpx.service.information.BaserateTypeEn;
import de.lb.cpx.shared.rules.enums.CaseValidationGroupErrList;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.codehaus.plexus.util.StringUtils;

/**
 * Standardinstanz zum erstellen des Inout-Objektes fuer den Grouper
 *
 * @author Wilde
 */
public class InOutCreator {
    private static final Pattern[] mNegativeFeeKeysPatterns = 
    {
        // info is from iskvimpoport - project, class EK_ENT
        // and widen through info from the document in CPX-1850 
        Pattern.compile("472....."),       
        Pattern.compile("492....."),       
        Pattern.compile("604....."),      
        Pattern.compile("62......"),       
        Pattern.compile("72......"),       
        Pattern.compile("73......"),       
        Pattern.compile("752....."),       
        Pattern.compile("82......"),       
        Pattern.compile("83......"),       
        Pattern.compile("88......"),       
        Pattern.compile("8830...."),       
        Pattern.compile("89......"),       
        Pattern.compile("92......"),       
        Pattern.compile("93......"),       
        Pattern.compile("A3......"),       
        Pattern.compile("B3......"),       
        Pattern.compile("C3......"),       
        Pattern.compile("A7......"),       
        Pattern.compile("B7......"),       
        Pattern.compile("C7......"),       
        Pattern.compile("AA0....K"),       
        Pattern.compile("BA0....K"),       
        Pattern.compile("CA0....K"),       
        Pattern.compile("CA9....."),       
        Pattern.compile("AD100002"),       
        Pattern.compile("BD100002"),       
        Pattern.compile("AF2....."),       
        Pattern.compile("BF2....."),       
        Pattern.compile("CF2....."),       
        Pattern.compile("29000000"),       
        Pattern.compile("29100000"),       
        Pattern.compile("39000000"),       
        Pattern.compile("39100000"),       
        Pattern.compile("49000000"),       
        Pattern.compile("49100000"),       
        Pattern.compile("59000000"),       
        Pattern.compile("59100000"),       
        Pattern.compile("D7300008"),
    };

    private static final Logger LOG = Logger.getLogger(InOutCreator.class.getName());

    /**
     * erstellen des InOut-Objekts fuer den Grouper und befüllen mit hilfe der
     * Daten aus dem Request Methode unterscheidet nach Entgeltgruppenart bei
     * der Befüllung des Objekt, wird für fallübergreifende Prüfung verwendet
     *
     * @param inout InOut Object
     * @param request Patient DTO
     * @return Grouper Case Object
     */
    public static synchronized CRGInputOutput createInOut(CRGInputOutput inout, TransferPatient request) throws Exception{
        if(request.getMainCase() != null){
            createInOut(inout, request.getMainCase() );
        }
// fill history
        if(request.getGroupingResult4HistoryCase()!= null  ){
            List<CRGInputOutput>  hisList = request.getGroupingResult4HistoryCase();
            for(int i = 0, len = hisList.size(); i < len ; i++){
                CRGInputOutput tmpInout = hisList.get(i);
	
                inout.setHistoryValues(tmpInout, i);

            }
            inout.sortHistoryCases();
            
        }
        return inout;
    }
    
        /**
     * erstellen des InOut-Objekts fuer den Grouper und befüllen mit hilfe der
     * Daten aus dem Request Methode unterscheidet nach Entgeltgruppenart bei
     * der Befüllung des Objekts
     *
     * @param inout InOut Object
     * @param request Case DTO
     * @return Grouper Case Object
     */public static synchronized CRGInputOutput createInOut(CRGInputOutput inout, TransferCase request) {


        try {
            //          Ein- und Ausgabe-Objekt holen
            //          CRGInputOutput inout = m_ruleCRG.getInout();
            inout.newCase();
            inout.setIsDrgCase(request.getCaseType());
            //in this case can be both times same id
            inout.setID((int) request.getCaseId(), (int) request.getCaseId());
            /*
                Fall-Stammdaten
             */
            //IK des Krankenhauses
            inout.setIkz(request.getIkz());
            //IK der Krankenkasse
            inout.setKasse(request.getKasse());
            //Fallnummer
            inout.setCaseNumber(request.getCaseNumber());

            /*
                Patientendaten
             */
            //Alter
            inout.setAgeY(request.getAgeInYears());
            inout.setAgeD(request.getAgeInDays());
            //Geschlecht
            inout.setSex(request.getSex());
            //Geburtsdatum
            inout.setDateOfBirth(request.getDateOfBirth());
            //Wohnort
            inout.setCity(request.getCity());
            inout.setZipCode(request.getZipCode());
// versichertenstatus
            inout.setInsuranceState(request.getInsuranceState());
            /*
                Aufnahmedaten
             */
            //Aufnahmedatum
            inout.setAdmissionDate(request.getDateOfAdmission());
            //Aufnahmeanlass
            inout.setAdmissionCause(request.getAdmissionCause());
            //Aufnahmegrund 1./2. Stelle
            inout.setAdmissionReason12(request.getAdmissionReason12());
            //Aufnahmegrund 3./4. Stelle
            inout.setAdmissionReason34(request.getAdmissionReason34());
            inout.setWeight(request.getWeight());

            /*
                Entlassungsdaten
             */
            //Entlassungsdatum
            inout.setDischargeDate(request.getDateOfDischarge());
            //Verweildauer (length of stay)
            int vwd = CRGInputOutput.performVWD(request.getDateOfAdmission(),
                    request.getDateOfDischarge(),
                    request.getNalos(), 0, request.getAdmissionReason12() != 3);
            inout.setLOS(vwd, 0);
            //Verweildauer intensiv
            inout.setIntensiveDepStay(request.getIntensiveDepStay());
            //Belegtage außerhalb
            inout.setNALOS(request.getNalos());
            //Urlaubstage
            inout.setLeaveOfAbsence(request.getLeaveOfAbsence());
            //Erbringungsart
            inout.setDepartmentType(request.getDepartmentType());
            //Beatmungsdauer
            //            inout.setRespirationLength(335);
            inout.setRespirationLength(request.getRespirationLenght());
            //Art der Behandlung
            inout.setInvoluntary(request.getInvoluntary());
            //Entlassungsgrund
            inout.setDiscargeReason12(request.getDischargeReason12());
            inout.setDiscargeReason3(request.getDischargeReason3());
            // this flags are always true wenn not ESKA. They switch of grouping for some defined feeGroups in ESKA
            inout.setDoGroup(true);
            inout.setDoGroupForCase(true);

            //Verlegungen-Fall 
            // CPX-561 : 18.07.2017
            inout.setIsTransfer(request.isHasTransferFlagByMerge()?request.isHasTransferFlagByMerge():GrouperInterfaceBasic.checkAdmDisFlag(request.getAdmissionCause(), request.getDischargeReason12()));
            List<TransferBaseRate> baserates = request.getBaseRate4case();
            if (!baserates.isEmpty()) {
                inout.setBaseRate(baserates.get(0).getValue());
//                if(baserates.size() > 1){
//// look for drg - care baserate
//    
//                    for(TransferBaseRate care:baserates){;
//                        if(care.getBaserateType().equals(BaserateTypeEn.DRG_CARE)){
//                            inout.setCareBaseRate(care.getValue());
//                        }
//                    }
//                }
                inout.setCheckBaserates(1);
                for (TransferBaseRate br : baserates) {
                    if (br.getValidFrom() == null) {
                        LOG.log(Level.WARNING, "Baserate has ot valid from: " + br.getValue());
                        continue;
                    }
                    if (br.getValidTo() == null) {
                        LOG.log(Level.WARNING, "Baserate has ot valid to: " + br.getValue());
                        continue;
                    }
                    if(br.getBaserateType().equals(BaserateTypeEn.DRG_CARE)){
                         inout.addOneCareBaserate(br.getValidFrom(), br.getValidTo(), br.getValue());
                    }else{
                        inout.addOneBaserate(br.getValidFrom(), br.getValidTo(), br.getValue());
                    }
                }
            }
            List<TransferIcd> admList = request.getAdmissionDiagnose();
            if (admList != null) {
                // ungünstige Methodenname: werden die Daten in die Liste zugefügt
                // hier werden die nicht für groupen verfendete aufnahmediagnosen übergeben
                for (TransferIcd adm : admList) {
                    inout.setAdmissionDiagnosis(adm.getCode(), adm.getCode(), adm.getRefType(), adm.getLocalisation());
                }
            }
            List<TransferDepartment> departs = request.getDepartments();

            // die Abteilunden wurden bei der uebergabe aus T_CASE_DETAILS sortiert nach Aufnahmedatum                
            for (TransferDepartment dep : departs) {
                Date start = dep.getAdm();
                Date end = dep.getTransferDate();
                if (dep.isAdmission()) {
                    inout.setReceptDepartment(dep.getDepartment());
                    start = request.getDateOfAdmission();
                }
                if (dep.isTreating()) {
                    inout.setCombatDepartment(dep.getDepartment());
                }
                if (dep.isDischarge()) {
                    inout.setReleaseDepartment(dep.getDepartment());
                    end = request.getDateOfDischarge() != null ? request.getDateOfDischarge() : end;
                }
                inout.addDepartment(dep.getDepartment(), end, start);
                List<TransferIcd> icds = dep.getIcds();
                //Diagnosen                    
                for (TransferIcd icd : icds) {
                    if (icd.isHdx()) {
                        inout.setPrincipalDiagnosis(icd.getCode(), icd.getCode(), icd.getRefType(), icd.getLocalisation());
                    } else {
                        inout.addAuxiliaryDiagnosis(icd.getCode(), icd.getCode(), icd.getRefType(), icd.getLocalisation());
                    }
                    if (icd.isAdm()) {
                        // hier werden die für groupen verwendete aufnahmediagnosen zugefügt
                        inout.setAdmissionDiagnosis(icd.getCode(), icd.getCode(), icd.getRefType());
                    }
                    if (!StringUtils.isEmpty(icd.getPrim())) {
                        inout.addSecondaryDiagnosis(icd.getCode(), icd.getCode(),
                                icd.getRefType(), icd.getLocalisation(), icd.getPrim(), icd.getPrim());

                    }
                }
                // Prozeduren
                List<TransferOps> opses = dep.getOps();
                for (TransferOps ops : opses) {
                    
                
                    Date opsDate = ops.getOpsDate();
                    //AGe:  we do not correct the ops date, it has to be es it was set by user
//                    if (request.getCaseType() == CaseTypeEn.PEPP.getId()) {
//                        if (opsDate == null || opsDate.before(start)) {
//                            opsDate = start;
//                        } else if (opsDate != null) {
//                            if (end!= null && opsDate.after(end)) {
//                                opsDate = end;
//                            }
//                        }
//                    }
                    inout.addProcedure((int) ops.getId(), ops.getCode(), ops.getLocalisation(), opsDate, ops.getCode());
                }
                // Stationen
                List<TransferWard> wards = dep.getWards();
                if (wards != null && !wards.isEmpty()) {
                    for (TransferWard ward : wards) {
                        inout.addStation(ward.getIdent());
                    }
                }
            }
            inout.setProcedureEqualCount();
            CRGInputOutput.performVWD4DepartmentsLOSPepp(inout);
            // wenn keine Hauptdiagnose gesetzt wurde wird ein Platzhalter zugefügt                
            if (inout.getPrincipalDiagnosis() == null || inout.getPrincipalDiagnosis().trim().length() == 0) {
                inout.setPrincipalDiagnosis("", "", 0);
            }
            // Entgelte
            List<TransferFee> fees = request.getFees();
            double sum = 0;
            for (TransferFee fee : fees) {
                if (!Objects.equals(fee.getFeeType(), "00000000")) {
                    double fvalue = Math.round(fee.getValue() * 100) / (double) 100;
                    // by p21 import the deduction is imported as positive value
                    if(isDeduction(fee.getFeeType()) && fvalue > 0){
                        fvalue = (-1)*fvalue;
                    }
                    inout.addFeeRecord(fee.getFeeType(), fvalue, fee.getCalcFrom(),
                            fee.getCalcTo(), fee.getCount(), fee.getDaysNotConsidered());
                    sum += fvalue * fee.getCount();
                }
            }
            inout.setFeeSum(sum);
            // VWD - simulation   
            inout.setVwdSim((int) request.getLosAlteration());
            // Los from md
            
            inout.setVwdMdSim(request.getLosMdAlteration());
            // Labor
            if (request.isDoLabor()) {
                List<TransferLabor> labor = request.getLabor();
                for (TransferLabor lab : labor) {
                    inout.addLabor(lab.getLabValue(), lab.getLabText() == null ? "" : lab.getLabText(),
                            lab.getLabUnit() == null ? "" : lab.getLabUnit(),
                            lab.getLabDescr() == null ? "" : lab.getLabDescr(), lab.getLabAnalysedate());
                }
            }
// benutzerdefinierte Werte String1-String10, Numeric1 -numeric10            
            Map<Integer, String> csStrings = request.getCsStringValues();
            Set<Integer> indSet = csStrings.keySet();
            for (int ind : indSet) {
                inout.setCaseString(ind, csStrings.get(ind));

            }

            Map<Integer, Integer> csNumerics = request.getCsNumericValues();
            indSet = csNumerics.keySet();
            for (int ind : indSet) {
                inout.setCaseNumeric(ind, csNumerics.get(ind));

            }

            return inout;
        } catch (Exception ex) {
            Logger.getLogger(InOutCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * fills the transfer object from CRGInputOutput after grouping
     *
     * @param inout CRGInputOutput interface to Grouper
     * @param tCase - Transfer Object
     * @return TransferCase Object
     */
    public static synchronized TransferCase createResult4Case(CRGInputOutput inout, TransferCase tCase) {

        // simulation der Diagnosen übernahmen
        List<TransferDepartment> departments = tCase.getDepartments();
        try {
//            LOG.log(Level.INFO, inout.dumpRecord().toString());
            String[] drgs = inout.getSimulatedDRGsForDiagnosis(); /// TODO: die folge entspricht der Folge  der Diagnosen in TransferCase
            /* ccls = getSimulateCCL, 
				cws = getSimulateCW()used müssen in Inout noch überarbeitet werden, da sie als Strings gespeichert werden   
             */
            int posDrg = 0;
            String[] usedIcds = inout.getUsesGroupingDiagnosis();
            int posUsedIcds = 0;
            String[] usedOps = inout.getUsedGroupingProcedures();
            int posUsedOps = 0;
            String[] ccls = inout.getSimulateCCL();
            int posCCLs = 0;
            String[] cws = inout.getSimulateCW();
            int posCws = 0;
            String [] uncorrCws = inout.getSimulateUnkorrCW();
            int posUncorr = 0;
            String [] careCws = inout.getSimulateCareCW();
            int posCare = 0;
            
            String [] peppCw4Class = inout.getSimulateOnePappClassCW();
            int posPeppCw4Class = 0;
            
            String [] peppClass = inout.getSimulateOnePappClass();
            int posPeppClass = 0;
            
            List<TransferIcdResult> icdRes = new ArrayList<>();  // wird z.Z. nur an Hauptdiagnose angehängt, gibt es keine andere Infos
            HashMap<Long, TransferOpsResult> opsRes = new HashMap<>();
            TransferGroupResult hdxResult = null;
            for (TransferDepartment dep : departments) {
                List<TransferIcd> icds = dep.getIcds();
                for (TransferIcd icd : icds) {
//                    if (icd.isHdx()) {

                    TransferGroupResult result =( tCase.doSimulate() || icd.isHdx())?
                            createGroupResults(tCase.getCaseType(), inout, icd.isHdx(), tCase.isHasTransferFlagByMerge()):null;
                    if(result != null){
                        result.setModelId(inout.getDRGVersion());
                        result.setIsAuto(tCase.getIsAuto());
                        if (icd.isHdx()) {
                            result.setCode(checkString4Empty(inout.getDrg()));
                            result.setPccl(inout.getPccl());
                            result.setGst(checkString4Empty(inout.getGST()));
                            result.setGpdx(checkString4Empty(inout.getGPDX()));
                            result.setGroup(checkString4Empty(inout.getMdc()));
                            result.setCwEffectiv(inout.getEffectiveCostWeight());
                            result.setIsNegotiated(inout.isVerhandelbar() == 1);
                            hdxResult = result;
                            icd.setGroupResult(result);

                        } else {
                            boolean ignore = false;
                            if(result != null && tCase.doSimulate()){
                                if (drgs != null && posDrg < drgs.length) {
                                    ignore = ignoreDrgWithAdmReason(drgs[posDrg], inout.getAdmissionReason12(), result instanceof TransferDrg);
                                    if(ignore){
                                        if(inout.getAdmissionReason12() == 3) {
                                            result.setCode("----");
                                        }else{
                                            result.setCode(" ");
                                        }
                                        
                                    }else{
                                        result.setCode(drgs[posDrg]);
                                    }
                                    posDrg++;
                                }

                                if (cws != null && posCws < cws.length) {
                                    double cw = 0;
                                    if(!ignore){
                                        try {
                                            cw = Double.valueOf(cws[posCws]);
                                        } catch (NumberFormatException e) {
                                            LOG.log(Level.WARNING, "Fehler beim Parsen des CW Wertes " + cws[posCws], e);
                                        }
                                    }
                                    result.setCwEffectiv(cw);
                                    posCws++;
                                }
                                if (uncorrCws != null && posUncorr < uncorrCws.length) {
                                    double cw = 0;
                                    if(!ignore){
                                        try {
                                            cw = Double.valueOf(uncorrCws[posUncorr]);
                                        } catch (NumberFormatException e) {
                                            LOG.log(Level.WARNING, "Fehler beim Parsen des CW Wertes " + cws[posCws], e);
                                        }
                                    }
                                    result.setCwCatalog(cw);
                                    posUncorr++;
                                }
                                if (careCws != null && posCare < careCws.length) {
                                    double cw = 0;
                                    if(!ignore){
                                        try {
                                            cw = Double.valueOf(careCws[posCare]);
                                        } catch (NumberFormatException e) {
                                            LOG.log(Level.WARNING, "Fehler beim Parsen des CW Wertes " + cws[posCws], e);
                                        }
                                    }
                                    result.setCareCwDay(cw);
                                    posCare++;
                                }
                                if(result instanceof TransferPepp){
                                if (peppCw4Class != null && posPeppCw4Class < peppCw4Class.length) {
                                    double cw = 0;
                                    try {
                                        cw = Double.valueOf(peppCw4Class[posPeppCw4Class]);
                                    } catch (NumberFormatException e) {
                                        LOG.log(Level.WARNING, "Fehler beim Parsen des CW Wertes " + cws[posCws], e);
                                    }
                                    ((TransferPepp)result).setPeppCw4Class(cw);
                                    posPeppCw4Class++;
                                }
                                if (peppClass != null && posPeppClass < peppClass.length) {
                                    int cl = 0;
                                    try {
                                        cl = Integer.valueOf(peppClass[posPeppClass]);
                                    } catch (NumberFormatException e) {
                                        LOG.log(Level.WARNING, "Fehler beim Parsen des class Wertes " +peppClass[posPeppClass], e);
                                    }

                                    ((TransferPepp)result).setPayClass(cl);
                                    posPeppClass++;
                                }

                                }
                                icd.setGroupResult(result);
                            }
                        }
                    }
                    TransferIcdResult res = new TransferIcdResult(icd.getId());
                    icdRes.add(res);
                    if (usedIcds != null && posUsedIcds < usedIcds.length) {

                        res.setUsed4grouping(Objects.equals(usedIcds[posUsedIcds], "1"));
                        posUsedIcds++;
                    }
                    if (ccls != null && posCCLs < ccls.length) {
                        int ccl = 0;
                        try {
                            ccl = Integer.valueOf(ccls[posCCLs]);
                        } catch (NumberFormatException e) {
                            LOG.log(Level.WARNING, "Fehler beim Parsen des CCLs Wertes " + ccls[posCCLs], e);
                        }
                        res.setCCL(ccl);
                        posCCLs++;
                    }

                }

                for (TransferOps ops : dep.getOps()) {
                    if (usedOps != null && posUsedOps < usedOps.length) {
                        TransferOpsResult tOpsRes = new TransferOpsResult(ops.getId());
                        opsRes.put(ops.getId(), tOpsRes);

                        tOpsRes.setUsed4grouping(Objects.equals(usedOps[posUsedOps], "1"));
                        posUsedOps++;
                    }
                }
            }
            if (hdxResult == null) {// there is no hdx in this case
                hdxResult = createGroupResults(tCase.getCaseType(), inout, true);
                hdxResult.setModelId(inout.getDRGVersion());
                hdxResult.setIsAuto(tCase.getIsAuto());

                hdxResult.setCode(inout.getDrg());
                hdxResult.setPccl(inout.getPccl());
                hdxResult.setGst(inout.getGST());
                hdxResult.setGpdx(inout.getGPDX());
                hdxResult.setGroup(inout.getMdc());
                hdxResult.setCwEffectiv(inout.getEffectiveCostWeight());
                hdxResult.setIsNegotiated(inout.isVerhandelbar() == 1);
                tCase.setGroupResult2principalNull(hdxResult);
            }
            hdxResult.setIcdRes(icdRes);
            hdxResult.setOpsRes(opsRes);
            // Verweildauer
            tCase.setLengthOfStay(inout.getLengthOfStay());

            tCase.setLeaveOfAbsence(inout.getCalcReduceLos());

        } catch (Exception e) {
            Logger.getLogger(InOutCreator.class.getName()).log(Level.SEVERE, "Fehler beim Setzen der Grouperergebnisse in den Transferobjekt", e);
        }
        return tCase;
    }
    
    private static boolean ignoreDrgWithAdmReason(String drg, int admReason, boolean isDrg){
        return isDrg && (admReason == 4 ||
                admReason == 3 && !(drg.startsWith("L90") || drg.startsWith("A90")));
    }

    /**
     * creates Grouping Result Object for Transfer
     *
     * @param caseType int of CaseTyp, 1 DRG 2 Pepp .. or something like that
     * @param inout Grouper InOut Object
     * @param isHdx boolean if Case contains MainDiagnosis
     * @return TansferCase Grouping Result
     * @throws Exception Exception thrown when something happend in Grouping
     * Process
     */
    private static TransferGroupResult createGroupResults(int caseType, CRGInputOutput inout, boolean isHdx) throws Exception {
        return createGroupResults( caseType,  inout,  isHdx, false);
    }
    
    private static TransferGroupResult createGroupResults(int caseType, CRGInputOutput inout, boolean isHdx, boolean isMergeTransfer) throws Exception {
        // setzen die eigenschaften, die bei DRG und PEPP unterschiedlich sind 
        if (caseType == CaseTypeEn.PEPP.getId()) {
            TransferPepp pepp = new TransferPepp();
            if (isHdx) {
                pepp.setAdultPersonalCare(inout.getAdultPersonalCare());
                pepp.setInfantsPersonalCare(inout.getInfantsPersonalCare());
                pepp.setDurationsIntensivCare(inout.getDurationsIntensivCare());
                pepp.setDurationsIntesivPersentage(inout.getDurationsIntesivPersentage());
                pepp.setPeppType(inout.getPeppPayClass() == 0);
                pepp.setCalculatedGrades(setCalculatedPeppValues(inout));
            }
            pepp.setPayClass(inout.getPeppPayClass());
            if(inout.getPeppPayClass() != 0){ // by payclass 
                pepp.setPeppCw4Class(inout.getCW4payClass());
            }
                
            
            return pepp;
        } else {
            TransferDrg drg = new TransferDrg();
            if (isHdx) {
                drg.setAdrg(inout.getAdrg());
                int typeOfCorr = DrgCorrTypeEn.no.getIdInt();
                int corrDays = 0;
                int reductionDays = inout.getAbschlagstage();
                int additionDays = inout.getZuschlagstage();
                double cwCorr = 0;
                double cwCorr2Day = 0;
                if (additionDays > 0) {
                    typeOfCorr = DrgCorrTypeEn.Surcharge.getIdInt(); // zuschlag
                    cwCorr = inout.getZuschlagscw();
                    corrDays = additionDays;
                    cwCorr2Day = inout.getZuschlagscwJeTag();
                }
                long admType = inout.getAdmissionCauseId();
                boolean aufnahmeVerlegung = GrouperInterfaceBasic.checkAdmissionTransferFlag(admType) && inout.getIsTransfer() == 1;
                int sep = inout.getDiscargeReason12();
                boolean entlassungVerlegung = GrouperInterfaceBasic.checkDischargeTransferType(sep) && inout.getIsTransfer() == 1;
                boolean verlegung = aufnahmeVerlegung || entlassungVerlegung;
                boolean checkVerlegung = reductionDays > 0;
//                if (verlegung && inout.getVerlegungsDrg() == 1 && reductionDays > 0) {
//                    reductionDays = 0;
//                    cwCorr = 0;
//                    cwCorr2Day = 0;
//
//                }
                if (checkVerlegung) {
                    if (!aufnahmeVerlegung && !entlassungVerlegung) {
                        typeOfCorr = DrgCorrTypeEn.Deduction.getIdInt();
                        cwCorr2Day = inout.getAbschlagscwJeTag();
                    } else {
                        // abschläge bei verlegungsdrgs
                        if (inout.getTransferDRG() == 1) {
                            typeOfCorr = DrgCorrTypeEn.DeductionTransfer.getIdInt();
                            cwCorr2Day = inout.getAbschlagscwJeTag();
                        }
                    }
                    if (inout.getTransferDRG() == 0) {
                        if (aufnahmeVerlegung && !entlassungVerlegung) {
                            typeOfCorr = DrgCorrTypeEn.DeductionTransferAdm.getIdInt();
                            cwCorr2Day = inout.getVerlegungcwJeTag();
                        }
                        if (entlassungVerlegung) {
                            typeOfCorr = DrgCorrTypeEn.DeductionTransferDis.getIdInt();
                            cwCorr2Day = inout.getVerlegungcwJeTag();
                        }
                        if(isMergeTransfer){
                            cwCorr2Day = inout.getVerlegungcwJeTag();
                        }
                        
                    }
//                    if (inout.getIsTransfer() != 0) {
//                        typeOfCorr = DrgCorrTypeEn.DeductionTransfer.getIdInt();
//                    } else {
//                        typeOfCorr = DrgCorrTypeEn.Deduction.getIdInt();
//                    }
                    cwCorr = inout.getAbschlagscw();
                    corrDays = reductionDays;
                }
                drg.seCwCorr2Day(cwCorr2Day);
                drg.setTypeOfCorr(typeOfCorr);
                drg.setCwCorr(cwCorr);
                drg.setCorrDays(corrDays);
                drg.setHtp(inout.getOGVD());
                drg.setLtp(inout.getUGVD());
                drg.setAdrg(checkString4Empty(inout.getAdrg()));
                drg.setAlos(inout.getAverageLengthOfStay());
                drg.setCwCatalog(inout.getCostWeight());
                drg.setDrgPartition(checkString4Empty(inout.getDrgPartition()));
                drg.setIsException(inout.getAusnahmeDrg());
                drg.setTge2Day(inout.getTge2Day(), inout.getLengthOfStay());
                drg.setCareCwDay(inout.getCareCwDay());
                drg.setCareDays(inout.getDrgCareDays());
                drg.setCareCw(inout.getCareCwDay() * inout.getDrgCareDays()); 
                if(inout.getDrgCareDays() > 0){
                    addCareValues(inout, drg);
                }
            }else{
                drg.setCareDays(inout.getDrgCareDays());
            }
            return drg;
        }
    }
    
        private static void addCareValues(CRGInputOutput inout, TransferDrg drg) throws Exception{
        
        int[] careSortInds = inout.getCareSortInds();

        int[] carePartDays = inout.getCarePartDays();

        double[] carePartsBaserates = inout.getPartCareBaserate() ;

        Date[] starts = inout.getPartCareStartDate();

        Date[] ends = inout.getPartCareEndDate();
        for(int i = 0; i < careSortInds.length; i++){
            drg.addOneCareGrade(careSortInds[i], carePartDays[i], inout.getCareCwDay(), carePartsBaserates[i], starts[i], ends[i]);

        }
    }

    private static List<TransferPeppGrade> setCalculatedPeppValues(CRGInputOutput inout) throws Exception {

        List<TransferPeppGrade> grades = new ArrayList<>();
        double[] gradeValues = inout.getPeppGradeCW();
        double[] baseRates = inout.getBaserates4Case();
        long[] starts2Baserate = inout.getStarts4Baserates();
        long[] ends2Baserate = inout.getEnds4Baserates();
        int[] baseRatesPositions = inout.getPeppUsedBaserateNumber();
        int[] gradePositons = inout.getPeppUsedGradeNumber();
        int[] lens = inout.getPeppGradeUsedDays();
        int lenCws = Math.min(gradeValues.length, lens.length);

        for (int i = 0; i < lenCws; i++) {
            TransferPeppGrade oneValue = new TransferPeppGrade();
            grades.add(oneValue);
            oneValue.setGrade(gradePositons.length > i ? (gradePositons[i] + 1) : 1);
            oneValue.setCw(gradeValues.length > i ? gradeValues[i] : 0);
            oneValue.setBaserate((baseRatesPositions.length > i && baseRates.length > baseRatesPositions[i]) ? baseRates[baseRatesPositions[i]] : 0);
            oneValue.setDuration(lens.length > i ? lens[i] : 0);
            oneValue.setBaserateFrom((baseRatesPositions.length > i && starts2Baserate.length > baseRatesPositions[i]) ? new Date(starts2Baserate[baseRatesPositions[i]]) : null);
            oneValue.setBaserateTo((baseRatesPositions.length > i && ends2Baserate.length > baseRatesPositions[i]) ? new Date(ends2Baserate[baseRatesPositions[i]]) : null);
        }
        return grades;
    }

    /**
     * creates a TranserRule object from Rule and simulated values
     *
     * @param crgRule CRGRule
     * @param cw simulated delta CW
     * @param simulDrg simulated DRG or PEPP
     * @param usedRoles User Role Ids
     * @return TransferCase Object
     */
//    public static TransferRule createNewDetectedRule(CRGRule crgRule, double cw, String simulDrg, List<Long> usedRoles, Map<String, String> ruleTypes2Ids) {
    public static TransferRule createNewDetectedRule(CRGRule crgRule, CRGInputOutput origInout, 
            CRGInputOutput simulInout, List<Long> usedRoles,
            Map<String, String> ruleTypes2Ids, 
            double pCaseSupplFeeSum,
            double pRuleSupplFeeSum) {
        TransferRule rule = new TransferRule();
        long rid = 0;
        try {
            rid = Long.parseLong(crgRule.m_rid);
        } catch (NumberFormatException e) {
            Logger.getLogger(InOutCreator.class.getName()).log(Level.WARNING, "error on identifiying the rule id from String value", e);
        }
        String ruleType = ruleTypes2Ids.get(crgRule.m_errorTypeText);

        rule.setId(rid);
        rule.setRoles(crgRule.m_roles);
        rule.setRuleType(RuleTypeEn.get2RuleType(crgRule.m_type));
        rule.setRuleErrType(ruleType == null ? crgRule.m_errorTypeText : ruleType);
        rule.setUsedRoles(usedRoles);
        if(origInout == null || simulInout == null){
            rule.setDcw(0);
            rule.setDrg("");
            rule.setDfee(0);
            rule.setDCareCw(0);

            return rule;
        }
        try{
            rule.setDcw(simulInout.getEffectiveCostWeight() - origInout.getEffectiveCostWeight());
            rule.setDrg(simulInout.getDrg());
            rule.setDfee(simulInout.getCaseFullRevenue() + pRuleSupplFeeSum - origInout.getCaseFullRevenue() - pCaseSupplFeeSum);
            rule.setDCareCw(simulInout.getCareCwDay() * simulInout.getDrgCareDays() - origInout.getCareCwDay() * origInout.getDrgCareDays());
        }catch(Exception ex){
            LOG.log(Level.SEVERE, " Error on creation simulation rule objects", ex);
        }
// risks for rule 
        List<CRGRisk> cpRisks = crgRule.getRuleRisks();
        rule.setRuleRisks(cpRisks, !crgRule.m_hasSuggestions);
        return rule;
    }

    /**
     * creates a BatchGroupResult object for grouping results of a single Case
     *
     * @param inout interface object of CheckpointRuleGrouper
     * @param aux9count aux9count
     * @return a batchresult object
     *
     */
    public static BatchGroupResult createBatchResult4Case(CRGInputOutput inout, int aux9count) {

        BatchGroupResult batchResult = new BatchGroupResult();
        try {
            boolean isTransfer = GrouperInterfaceBasic.checkAdmDisFlag(inout.getAdmissionCauseId(), inout.getDiscargeReason12());
            batchResult.isHtp(inout.getZuschlagstage() > 0);
            batchResult.isLtp(inout.getAbschlagstage() > 0 && !isTransfer);
            batchResult.isTransfer(isTransfer && inout.getAbschlagstage() > 0);
            batchResult.setPccl(inout.getPccl());
            batchResult.setCwEff(inout.getEffectiveCostWeight());
            batchResult.setCwCatalog(inout.getCostWeight());
            batchResult.isDead(inout.getDiscargeReason12() == 7); // TODO: change to enumeration
            batchResult.isDayCare(inout.getAdmissionReason12() == 3 || inout.getAdmissionReason12() == 4); // TODO: change to enumeration
            batchResult.setCareDays(inout.getLengthOfStay());
            batchResult.setAuxIcdCount(inout.getAuxiliaryDiagnosisCount());
            batchResult.setLosIntensiv(inout.getIntensiveDepartmentStay());
            batchResult.isGrouped(inout.getDrg() != null && !inout.getDrg().startsWith("-"));
            batchResult.isErrDrg(inout.getDrg() != null && (inout.getDrg().startsWith("9") || inout.getDrg().startsWith("PF")));
            batchResult.setNalos(inout.getLeaveOfAbsence());
            batchResult.setAux9count(aux9count);
        } catch (Exception ex) {
            Logger.getLogger(InOutCreator.class.getName()).log(Level.SEVERE, "Error on getting the batch results for case ", ex);
        }

        return batchResult;
    }

    public static void fillDefaults(CRGInputOutput inout) {
        try {
            if (inout.getIsDrgCase() == CaseTypeEn.PEPP.getId()) {
                inout.setDRG("----");
                inout.setPEPP("----");
                inout.setADRG(" ");
                inout.setMDC(" ");
                inout.setDRGVersion(0);

            } else {
                inout.setDRG("----");
                inout.setADRG(" ");
                inout.setMDC(" ");
                inout.setDRGPartition(" ");
                inout.setDRGVersion(0);

            }
        } catch (Exception ex) {
            Logger.getLogger(InOutCreator.class.getName()).log(Level.SEVERE, "Fehler beim Setzen der default - Grouperergebnisse in den Transferobjekt", ex);

        }
    }

    /**
     * prüft VWD auf 0
     *
     * @param inout inout
     * @return is los valid?
     */
    public static boolean checkVwdValid(CRGInputOutput inout) {

        try {
//            CRGInputOutput.performVWD4DepartmentsLOSPepp(inout);
            return inout.getLengthOfStay() + inout.getVwdSim() > 0;
        } catch (Exception ex) {
            Logger.getLogger(InOutCreator.class.getName()).log(Level.SEVERE, "Fehler beim Ermitteln der VWD", ex);

        }
        return true;
    }

    public static void createGroupAnalyseResult(TransferRuleAnalyseResult pRuleAnalyseResult, CRGInputOutput inout) throws Exception {
        if (pRuleAnalyseResult == null) {
            return;
        }
        CaseTypeEn caseType = CaseTypeEn.getValue2Id(inout.getIsDrgCase());
        pRuleAnalyseResult.setCaseType(caseType);
        pRuleAnalyseResult.setBaserate(inout.getBaseRate());
        pRuleAnalyseResult.setLengthOfStay(inout.getLengthOfStay());
        pRuleAnalyseResult.setGroupResult(createGroupResult(inout));
        if (inout.getAdmissionReason12() == 4) {
            pRuleAnalyseResult.getCaseValidationGroupErrList().add(CaseValidationGroupErrList.CASE_DAY_CARE);

        } else if (caseType != null && caseType.equals(CaseTypeEn.DRG)
                    && (inout.getAdmissionReason12() == 3 && inout.getDrg() != null && inout.getDrg().startsWith("-"))) {
                pRuleAnalyseResult.getCaseValidationGroupErrList().add(CaseValidationGroupErrList.DRG_CASE_DAY_CARE);
            
        }
        if (inout.getGST() != null) {
            GrouperStatusEn grpresGst = GrouperStatusEn.get2Id(inout.getGST());
            if (grpresGst != null && !grpresGst.equals(GrouperStatusEn.GST00)) {
                CaseValidationGroupErrList err = CaseValidationGroupErrList.find2EnumName(grpresGst.name());
                if (err != null) {
                    pRuleAnalyseResult.getCaseValidationGroupErrList().add(err);
                }
            }
        }
        if (inout.getGPDX() != null) {
            GroupResultPdxEn grpresGpdx = GroupResultPdxEn.getValue2Id(inout.getGPDX());

            if (grpresGpdx != null && !grpresGpdx.equals(GroupResultPdxEn.pdx0)) {
                CaseValidationGroupErrList err = CaseValidationGroupErrList.find2EnumName(grpresGpdx.name());
                if (err != null) {
                    pRuleAnalyseResult.getCaseValidationGroupErrList().add(err);
                }

            }
        }
    }

    private static TransferGroupResult createNullResult(CaseTypeEn caseType) {

        TransferGroupResult nullResult = null;
        switch (caseType) {
            case DRG:
                nullResult = new TransferDrg();
                break;
            case PEPP:
                nullResult = new TransferPepp();
                break;
            default:
                LOG.log(Level.WARNING, "unsupported case type found: {0}", caseType);
        }
        if (nullResult != null) {
            nullResult.setPccl(0);
        }
        return nullResult;
    }

    private static String checkString4Empty(String chkStr){
        if(chkStr == null ){
            return " ";
        }
        chkStr = chkStr.replaceAll("\"", "");
        if( chkStr.isEmpty()){
            return " ";
        }
        return chkStr;
    }

    public static TransferGroupResult createGroupResult(CRGInputOutput inout) throws Exception{
        CaseTypeEn caseType = CaseTypeEn.getValue2Id(inout.getIsDrgCase());
        if (isGroupResultNotValid(inout)){
                return createNullResult(caseType);
        } else {
            TransferGroupResult groupResult = createGroupResults(inout.getIsDrgCase(), inout, true);
            groupResult.setCode(inout.getDrg());
            groupResult.setPccl(inout.getPccl());
            groupResult.setGst(inout.getGST());
            groupResult.setGpdx(inout.getGPDX());
            groupResult.setGroup(inout.getMdc());
            groupResult.setCwEffectiv(inout.getEffectiveCostWeight());
            groupResult.setCaseYear(inout.getAdmissionYear());
            return groupResult;
        }
    }
    
    public static boolean isGroupResultNotValid(CRGInputOutput inout) throws Exception{
       CaseTypeEn caseType = CaseTypeEn.getValue2Id(inout.getIsDrgCase());
        return (inout.getAdmissionReason12() == 4 
                || caseType != null && caseType.equals(CaseTypeEn.DRG)
                    && (inout.getAdmissionReason12() == 3 && inout.getDrg() != null && inout.getDrg().startsWith("-")));

    }
    
    public static synchronized boolean isDeduction(final String pEgArtSl) {
        if(pEgArtSl == null || pEgArtSl.isEmpty()){
            return false;
        }
      for(Pattern patt: mNegativeFeeKeysPatterns) {
          if(patt.matcher(pEgArtSl).matches()){
              return true;
          }
      }
      return false;
    }
}
