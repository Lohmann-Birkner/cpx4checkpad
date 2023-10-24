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
package de.lb.cpx.service.ejb;

import de.checkpoint.drg.GDRGModel;
import de.checkpoint.server.db.OSObject;
import de.checkpoint.server.rmServer.caseManager.RmcPeppWiederaufnahme;
import de.checkpoint.server.rmServer.caseManager.RmcWiederaufnahme;
import de.checkpoint.server.rmServer.caseManager.RmcWiederaufnahmeIF;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseDrg;
import de.lb.cpx.model.TCaseMergeMapping;
import de.lb.cpx.model.TCaseMergeMappingDrg;
import de.lb.cpx.model.TCaseMergeMappingPepp;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.Rmc2CpxReadmRulesFieldsEn;
import de.lb.cpx.server.dao.TCaseMergeMappingDao;
import de.lb.cpx.server.dao.TPatientDao;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author gerschmann
 */
@Stateless
public class ReadmissionServiceEJB implements ReadmissionServiceEJBRemote{

   private static final Logger LOG = Logger.getLogger(ReadmissionServiceBean.class.getName());
    @EJB
    private TPatientDao patientDao;
    @EJB
    private TCaseMergeMappingDao mappingDao;
    
    @Inject
    ReadmissionService readmissionService;



    @Override
    public int checkReadmissions4Patient(Long patientId, CaseTypeEn type, boolean isLocal, GDRGModel model, boolean isAuto) {
        
        TPatient patient = patientDao.findById(patientId);
        if (patient == null) {
            return -1;
        }
        Set<TCase> cases = patient.getCases();
        // es gibt nichts zu überprüfen
        if (cases == null || cases.isEmpty()) {
            return 0;
        }
        // we get all mappings for Patient and grouperModel
//        List<Long> mapping4Patient = mappingDao.findMappedAndMergedCases4Patient(model, patientId);
//        List<TCaseMergeMapping> mappings4patient = mappingDao.findNotMergingCasesForType(null,
//            model, 
//            patientId, 
//            null);
        HashMap<Integer, Mapping2Result> caseId2RmcCase = new HashMap<>();
        ArrayList<RmcWiederaufnahmeIF> candidates = new ArrayList<>();
        int i = 0;// temporäre ID für Rmc Objekte
        for (TCase cs : cases) {
            if (cs.getCsCaseTypeEn().equals(type)) {
                //AWi-20171127:
                //ignore canceled cases
                if (cs.getCsCancellationReasonEn()) {
                    continue;
                }
                if(cs.getCsCaseNumber().endsWith("_m")){
                    continue;
                }
                // entfernen evt. Mapping - Eintrag, der diesen Fall referenziert
                // TODO: hier muss geprüft werden, on der fall ein member der zusammenführung ist. Wenn ja, soll ignoriert werden
//                if(checkAlreadyMerged(cs, mappings4patient)){ 
//                    continue;
//                }
                if(checkAlreadyMerged(cs, model, isAuto)){
                    continue;
                }
                Mapping2Result mRmcCs = createRmcCase(type, cs, isLocal, model, isAuto);
                if (mRmcCs == null) {
                    continue;
                }
                RmcWiederaufnahmeIF rmcCs = mRmcCs.rmcCase;
                mRmcCs.id = i;
                ((OSObject) rmcCs).id = i;
                caseId2RmcCase.put(i, mRmcCs);
                i++;
                candidates.add(rmcCs);

            }
        }
        if (candidates.size() < 2) {
            return i;
        }
        Collections.sort(candidates, new RmcWiederaufnahmeComparator());
        ArrayList<RmcWiederaufnahmeIF> results = new ArrayList<>();
        ArrayList<RmcWiederaufnahmeIF> list4ik = new ArrayList<>();
        String ikNr = candidates.get(0).getIkNr();
        for(RmcWiederaufnahmeIF candidate: candidates){
            if(getString(ikNr).equals(getString(candidate.getIkNr()))){
                list4ik.add(candidate);
            }else{
                if(!list4ik.isEmpty()){
                    if(list4ik.size() > 1){
                    list4ik = readmissionService.checkReadmissions(list4ik, type.equals(CaseTypeEn.PEPP));
                        results.addAll(list4ik);
                    }
                    list4ik = new ArrayList<>();
                    ikNr = candidate.getIkNr();

                    list4ik.add(candidate);
                }
            }
        }
         if(list4ik.size() > 1){
            list4ik = readmissionService.checkReadmissions(list4ik, type.equals(CaseTypeEn.PEPP));
            results.addAll(list4ik);
         }
        setMergeInfos2Cases(caseId2RmcCase, results, type);
        return i;
    }

    private Mapping2Result createRmcCase(CaseTypeEn type, TCase cs, boolean isLocal, GDRGModel model, boolean isAuto) {
        switch (type) {
            case PEPP:
                return createRmcPeppCase(cs, isLocal, model, isAuto);
            case DRG:
                return creatRmcDrgCase(cs, isLocal, model, isAuto);
            default:
                LOG.log(Level.WARNING, "Unknown case type: " + type);
        }
        return null;
    }

    /**
     * erzeugt und legt die TCaseMergeMapping - objekte aus den
     * RmcWiederaufnahmeIF objekten an
     *
     * @param caseId2RmcCase
     * @param candidates
     */
    private void setMergeInfos2Cases(HashMap<Integer, Mapping2Result> caseId2RmcCase, ArrayList<RmcWiederaufnahmeIF> candidates, CaseTypeEn type) {
        Class<?>[] param = new Class<?>[]{int.class};
        Object[] args = new Object[1];
        int ident = 0;
        int nextIdent = 0;
        for (RmcWiederaufnahmeIF candidate : candidates) {
//            printCandidateString(candidate);
            if (candidate.getMergeId() <= 0) {
                continue;
            }

            TCaseMergeMapping mapping = null;
            if (type.equals(CaseTypeEn.PEPP)) {
                mapping = new TCaseMergeMappingPepp();
            } else if (type.equals(CaseTypeEn.DRG)) {
                mapping = new TCaseMergeMappingDrg();
            }
            if (mapping == null) {
                continue;
            }
            if (ident != candidate.getMergeId()) {
                ident = candidate.getMergeId();
                nextIdent = mappingDao.getNextMergeIdent();

            }
            mapping.setMrgMergeIdent(/*candidate.getMergeId() + */nextIdent);
            int g = (int) (((OSObject) candidate).id);
            Mapping2Result res = caseId2RmcCase.get(g);
            mapping.setCaseByMergeMemberCaseId(res.cs);
            mapping.setGrpresId(res.grpres);
            mapping.setGrpresType(type);
            for (Rmc2CpxReadmRulesFieldsEn en : Rmc2CpxReadmRulesFieldsEn.values()) {
                try {
                    args[0] = candidate.getClass().getField(en.getValue()).get(candidate);
                    Method method = mapping.getClass().getMethod(en.name(), param);
                    method.invoke(mapping, args);
                } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
                    LOG.log(Level.SEVERE, "Error on creating of TCaseMergeMapping objects", e);
                }
            }

            mappingDao.persist(mapping);
        }
    }

    /**
     * liefert RmcWiederaufnahme Objekt zu TCase
     *
     * @param cs TCase objekt
     * @return Ergebnisobjekt
     */
    private Mapping2Result createRmcPeppCase(TCase cs, boolean isLocal, GDRGModel model, boolean isAuto) {
        RmcPeppWiederaufnahme rmc = new RmcPeppWiederaufnahme();
        Mapping2Result ret = fillCommonFields(rmc, cs, isLocal, model, isAuto);

        if (ret == null) {
            return null;
        }
        rmc.m_teilstationaer = ret.grpres.isGrpresIsDayCareFl() ? 1 : 0;
        rmc.m_verhandelbar = ret.grpres.isGrpresIsNegotiatedFl() ? 1 : 0;
        return ret;
    }

    /**
     * liefert RmcPeppWiederaufnahme Objekt zu TCase
     *
     * @param cs TCase objekt
     * @return Ergebnisobjekt
     */
    private Mapping2Result creatRmcDrgCase(TCase cs, boolean isLocal, GDRGModel model, boolean isAuto) {
        RmcWiederaufnahme rmc = new RmcWiederaufnahme();
        Mapping2Result ret = fillCommonFields(rmc, cs, isLocal, model, isAuto);

        if (ret == null) {
            return null;
        }
        if (rmc.aufnahmegrund == 3 || rmc.aufnahmegrund == 4) {
            return null;
        }
        rmc.m_aufnahmeWgKomplikation = rmc.aufnahmegrund == 7;
        TCaseDrg drg = ret.grpres.getCaseDrg();
        if (drg == null) {
            return null;
        }

        rmc.m_partition = (drg.getDrgcPartitionEn() == null || drg.getDrgcPartitionEn().name() == null) ? ' ' : drg.getDrgcPartitionEn().name().charAt(0);
        rmc.m_drgKatalogWAAusnahme = drg.getIsDrgcIsExceptionFl();
        rmc.m_oGVD = drg.getDrgcHtp();
        rmc.m_adrg = rmc.drg == null || rmc.drg.length() < 4 ? rmc.drg : rmc.drg.substring(0, 3);
        return ret;
    }

    /**
     * die Felder, die über schnisstellengetter/setter Methoden gefüllt werden
     * können, die für PEPP und DRG gleich sind
     *
     * @param rmc RmcWiederaufnahmeIF - interface
     * @param cs Fall aus CPX - DB
     * @param isLocal bestimmt, ob lokale oder KIS Fälle analysiert werden
     * @param model Model, relevant nur wenn es kein automatisches Groupen ist
     * @param isAuto Flag, ob es um automatischen Gropuen geht
     * @return liefert ein Objekt der klasse Mapping2Result oder null
     */
    private Mapping2Result fillCommonFields(RmcWiederaufnahmeIF rmc, TCase cs, boolean isLocal, GDRGModel model, boolean isAuto) {
        rmc.setIknr(cs.getCsHospitalIdent());
        rmc.setCaseNr(cs.getCsCaseNumber());
        TCaseDetails csDetails = isLocal ? cs.getCurrentLocal() : cs.getCurrentExtern();
        if (csDetails == null || csDetails.getCsdAdmissionDate() == null || csDetails.getCsdDischargeDate() == null) {
            return null;
        }
        rmc.setAufnahmedatum(new java.sql.Date(csDetails.getCsdAdmissionDate().getTime()));
        rmc.setEntlassungsdatum(new java.sql.Date(csDetails.getCsdDischargeDate().getTime()));
        rmc.setAufnahmegrund(csDetails.getCsdAdmReason12En().getIdInt());

        TGroupingResults grpRes = csDetails.getGroupingResult2Model(model, isAuto);
        if (grpRes == null) {
            return null;
        }
        rmc.setDrgOrPepp(grpRes.getGrpresCode());
        //AWi-20171018:
        //if error drg set empty string - suggestion from gerschmann
        rmc.setMdcOrSk(grpRes.getGrpresGroup() != null ? grpRes.getGrpresGroup().getId() : "");
        return new Mapping2Result(grpRes, cs, rmc);

    }

    /**
     * prüft und entfernt die Ergebnisse der früheren WA-Regeleanwendung für
     * nicht zusammengeführen Fälle
     *
     * @param model - Groupermodel, für die die WA überprüft wird
     * @param isAuto - flag des Automatischen Groupens
     * @param cs - aktueller Fall
     */
//    private boolean checkAlreadyMerged(TCase cs, List<TCaseMergeMapping> pMappings) {
//       for(TCaseMergeMapping mapping: pMappings){
//           if(cs.getId() == mapping.getCaseByMergeMemberCaseId().getId()){
//               if(mapping.getCaseByHoscId() != null){
//                   return true;
//               }else{
//                   mappingDao.deleteById(mapping.getId());
//               }
//           }
//       }
//       return false; 
//    }
    private boolean checkAlreadyMerged(TCase cs, GDRGModel model, boolean isAuto) {
//        Set<TCaseMergeMapping> caseParts = cs.getCaseMergeMappingsForHoscId();
//        if (caseParts == null || caseParts.isEmpty()) {
            
            Set<TCaseMergeMapping> candidates = cs.getCaseMergeMappingsForMergeMemberCaseId();
            if(candidates == null|| candidates.isEmpty()){
                return false;
            }

            for (TCaseMergeMapping candidate : candidates) {
                // der Fall wurde nicht zusammengeführt, die Mappingeinträge der vorherigen Prüfung müssen gelöscht werden
                if(isAuto && candidate.getGrpresId().getGrpresIsAutoFl() || !isAuto &&  !candidate.getGrpresId().getGrpresIsAutoFl() && candidate.getGrpresId().getModelIdEn().equals(model)){
                    if(candidate.getCaseByHoscId() == null){
                        mappingDao.deleteById(candidate.getId());
                        return false;
                    }else{
                        return true;
                    }
                }
            }
            
            return false;
//        }
    }

    private void printCandidateString(RmcWiederaufnahmeIF candidate) {
        try {
            LOG.log(Level.INFO, "Fall:" + candidate.getFallNr() + " ikz: " + candidate.getIkz() + " DRG/pepp: " + candidate.getdrgOrPepp()
                    + " mergeID: " + candidate.getMergeId() + " rules: "
                    + candidate.getEins() + ", "
                    + candidate.getZwei() + ", "
                    + candidate.getDrei() + ", "
                    + candidate.getVier() + ", "
                    + candidate.getFuenf() + ", "
                    + candidate.getSechs() + ", "
                    + candidate.getSieben() + ", "
                    + candidate.getAcht() + ", "
                    + candidate.getNeun() + ", "
                    + candidate.getZehn());
        } catch (Exception e) {
            LOG.log(Level.INFO, "Fehler bei der IKnr - Ermittlung " + candidate.getFallNr(), e);
        }

    }
    
    private static String getString(String str){
            return str == null?"":str;
    }


    /**
     * Sortierung nach Aufnahmedatum
     */
    private static class RmcWiederaufnahmeComparator implements Comparator<RmcWiederaufnahmeIF>, Serializable {

        private static final long serialVersionUID = 1L;

        RmcWiederaufnahmeComparator() {
        }

    @Override
    public int compare(RmcWiederaufnahmeIF rmc1, RmcWiederaufnahmeIF rmc2) {
        if (rmc1 != null && rmc2 != null) {
            //RmcWiederaufnahmeIF rmc1 = (RmcWiederaufnahmeIF) o1;
            //RmcWiederaufnahmeIF rmc2 = (RmcWiederaufnahmeIF) o2;
            if(getString(rmc1.getIkNr()).equals(getString(rmc2.getIkNr() ))){
                int ret = rmc1.getAufnahmedatum().compareTo(rmc2.getAufnahmedatum());
                if (ret == 0) {
                    return rmc1.getCaseNumber4RefCase().compareTo(rmc2.getCaseNumber4RefCase());
                } else {
                    return ret;
                }
            }else{

               return getString(rmc1.getIkNr()).compareTo(getString(rmc2.getIkNr()));
            }
        }

        return 0;
    }
}
    /**
     * Temporäre Klasse
     */
    public class Mapping2Result {

        private TGroupingResults grpres;
        private int id;
        private TCase cs;
        private RmcWiederaufnahmeIF rmcCase;

        Mapping2Result(TGroupingResults grpres, TCase cs, RmcWiederaufnahmeIF rmcCase) {
            this.grpres = grpres;
            this.cs = cs;
            this.rmcCase = rmcCase;
        }

    }
    
}
