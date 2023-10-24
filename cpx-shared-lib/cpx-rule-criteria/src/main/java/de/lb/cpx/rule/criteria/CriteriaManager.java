/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.rule.criteria;

import de.lb.cpx.rule.criteria.model.CaseCriteria;
import de.lb.cpx.rule.criteria.model.Criteria;
import de.lb.cpx.rule.criteria.model.CriterionTree;
import de.lb.cpx.rule.criteria.model.CriterionTree.Supergroup.Group;
import de.lb.cpx.rule.criteria.model.CriterionTree.Supergroup.Group.Criterion;
import de.lb.cpx.rule.util.XMLHandler;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;

/**
 * Criteria Manager Loads and handles criteria groups in memory
 *
 * TODO: add licence handling! some of the criteria groups are only valid if
 * special licence is purchased
 *
 * @author wilde
 */
public class CriteriaManager {
//    private static final String BASIC_CRITERIA = "criterionTree.xml";

    private static final String RESOURCE_FILE = "/xml/";
//    private static final String BASIC_CRITERIA_RESOURCE = RESOURCE_FILE+BASIC_CRITERIA;
/*
    private static final String ACG_CRITERIA_NAME = "rules.supergroup.acg.tooltip";
    private static final String ACG_CRITERIA = "rules.supergroup.acg.tooltip.xml";
    private static final String ACG_CRITERIA_RESOURCE = RESOURCE_FILE + ACG_CRITERIA;

    private static final String AMBU_CRITERIA_NAME = "rules.supergroup.ambu.data.tooltip";
    private static final String AMBU_CRITERIA = "rules.supergroup.ambu.data.tooltip.xml";
    private static final String AMBU_CRITERIA_RESOURCE = RESOURCE_FILE + AMBU_CRITERIA;

    private static final String HOSPITAL_CRITERIA_NAME = "rules.supergroup.data.case.tooltip";
    private static final String HOSPITAL_CRITERIA = "rules.supergroup.data.case.tooltip.xml";
    private static final String HOSPITAL_CRITERIA_RESOURCE = RESOURCE_FILE + HOSPITAL_CRITERIA;

    private static final String GKRSA_CRITERIA_NAME = "rules.supergroup.gk.rsa.dis";
    private static final String GKRSA_CRITERIA = "rules.supergroup.gk.rsa.dis.xml";
    private static final String GKRSA_CRITERIA_RESOURCE = RESOURCE_FILE + GKRSA_CRITERIA;

    private static final String CASE_CRITERIA_NAME = "rules.supergroup.hosp.case.tooltip";
    private static final String CASE_CRITERIA = "rules.supergroup.hosp.case.tooltip.xml";
    private static final String CASE_CRITERIA_RESOURCE = RESOURCE_FILE + CASE_CRITERIA;

    private static final String PATIENT_CRITERIA_NAME = "rules.txt.group.patient.master.data.dis";
    private static final String PATIENT_CRITERIA = "rules.txt.group.patient.master.data.dis.xml";
    private static final String PATIENT_CRITERIA_RESOURCE = RESOURCE_FILE + PATIENT_CRITERIA;

    private static final String VPS_CRITERIA_NAME = "";
    private static final String VPS_CRITERIA = "rules.vps.crit.group.xml";
//    private static final String VPS_CRITERIA_RESOURCE = RESOURCE_FILE + VPS_CRITERIA;
    private static final String VPS_CRITERIA_RESOURCE = "D:\\Labor\\cpx_xml\\" + VPS_CRITERIA;
*/
    private static final String criteriaPath = "D:\\Projekte\\rule_processor_parent\\workspace\\Kriterien.xml";
    private static final String USE_RULE_ONLY = "CRIT_RULE_ONLY";
    private static final String USE_RULE_SUGG = "CRIT_RULE_AND_SUGG";
    private static CriteriaManager instance;

/*    private Criteria acgCriteria;

    private Criteria ambuCriteria;

    private Criteria hospitalCriteria;

    private Criteria gkrsaCriteria;

    private Criteria caseCriteria;

    private Criteria patientCriteria;

    private Criteria vpsCriteria;*/
    
    private Criteria usedCriteria;

    private CriteriaManager() {
        
    }

    public static synchronized CriteriaManager instance() {
        if (instance == null) {
            instance = new CriteriaManager();
        }
        return instance;
    }

    public static synchronized void destroy() {
        if (instance != null) {
            instance.clear();
            instance = null;
        }
    }

    public void clear() {
/*        basicCriteria = null;
        acgCriteria = null;
        ambuCriteria = null;
        hospitalCriteria = null;
        gkrsaCriteria = null;
        caseCriteria = null;
        patientCriteria = null;
        vpsCriteria = null;*/
        usedCriteria = null;
    }

/*    public Criteria getAcgCriteria() {
        if (acgCriteria == null) {
            try {
                acgCriteria = initCriteria(getClass().getResource(ACG_CRITERIA_RESOURCE));
            } catch (JAXBException ex) {
                Logger.getLogger(CriteriaManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return acgCriteria;
    }

    public Criteria getAmbuCriteria() {
        if (ambuCriteria == null) {
            try {
                ambuCriteria = initCriteria(getClass().getResource(AMBU_CRITERIA_RESOURCE));
            } catch (JAXBException ex) {
                Logger.getLogger(CriteriaManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ambuCriteria;
    }

    public Criteria getHospitalCriteria() {
        if (hospitalCriteria == null) {
            try {
                hospitalCriteria = initCriteria(getClass().getResource(HOSPITAL_CRITERIA_RESOURCE));
            } catch (JAXBException ex) {
                Logger.getLogger(CriteriaManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return hospitalCriteria;
    }

    public Criteria getGkrsaCriteria() {
        if (gkrsaCriteria == null) {
            try {
                gkrsaCriteria = initCriteria(getClass().getResource(GKRSA_CRITERIA_RESOURCE));
            } catch (JAXBException ex) {
                Logger.getLogger(CriteriaManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return gkrsaCriteria;
    }

    public Criteria getCaseCriteria() {
        if (caseCriteria == null) {
            try {
                caseCriteria = initCriteria(getClass().getResource(CASE_CRITERIA_RESOURCE), CaseCriteria.class);
            } catch (JAXBException ex) {
                Logger.getLogger(CriteriaManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return caseCriteria;
    }

    public Criteria getPatientCriteria() {
        if (patientCriteria == null) {
            try {
                patientCriteria = initCriteria(getClass().getResource(PATIENT_CRITERIA_RESOURCE));
            } catch (JAXBException ex) {
                Logger.getLogger(CriteriaManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return patientCriteria;
    }

    public Criteria getVpsCriteria()  {
        if (vpsCriteria == null) {
            try {
                File file = new File(VPS_CRITERIA_RESOURCE);
                URI uri = file.toURI();
                vpsCriteria = initCriteria(uri.toURL(), Criteria.class);
            } catch (JAXBException | MalformedURLException ex) {
                Logger.getLogger(CriteriaManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return vpsCriteria;
    }
*/
    public Criteria getUsedCriteria()  {
        if (usedCriteria == null) {
            try {
                File file = new File(criteriaPath);
                URI uri = file.toURI();
                usedCriteria = initCriteria(uri.toURL(), Criteria.class);
            } catch (JAXBException | MalformedURLException ex) {
                Logger.getLogger(CriteriaManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return usedCriteria;
    }

    private Criteria initCriteria(URL pURL) throws JAXBException {
        return initCriteria(pURL, Criteria.class);
    }

    private Criteria initCriteria(URL pURL, Class<?> pClazz) throws JAXBException {
        return (Criteria) XMLHandler.unmarshalXML(pURL, pClazz, new CriterionTreeObjectFactory(pClazz));
    }

    public Criterion findFirstCriterion(String pName) {
 /*       if (getAcgCriteria().hasCriterion(pName)) {
            return getAcgCriteria().getCriterion(pName);
        }
        if (getAmbuCriteria().hasCriterion(pName)) {
            return getAmbuCriteria().getCriterion(pName);
        }
        if (getHospitalCriteria().hasCriterion(pName)) {
            return getHospitalCriteria().getCriterion(pName);
        }
        if (getGkrsaCriteria().hasCriterion(pName)) {
            return getGkrsaCriteria().getCriterion(pName);
        }
        if (getCaseCriteria().hasCriterion(pName)) {
            return getCaseCriteria().getCriterion(pName);
        }
        if (getPatientCriteria().hasCriterion(pName)) {
            return getPatientCriteria().getCriterion(pName);
        }
        if (getVpsCriteria().hasCriterion(pName)) {
            return getVpsCriteria().getCriterion(pName);
        }*/
        if (getUsedCriteria().hasCriterion(pName)) {
            return getUsedCriteria().getCriterion(pName);
        }        return null;
    }

    public Criterion getFirstCriterionFromMap(String pName) {
 /*       if (getAcgCriteria().containsCriterion(pName)) {
            return getAcgCriteria().getCriterionFromMap(pName);
        }
        if (getAmbuCriteria().containsCriterion(pName)) {
            return getAmbuCriteria().getCriterionFromMap(pName);
        }
        if (getHospitalCriteria().containsCriterion(pName)) {
            return getHospitalCriteria().getCriterionFromMap(pName);
        }
        if (getGkrsaCriteria().containsCriterion(pName)) {
            return getGkrsaCriteria().getCriterionFromMap(pName);
        }
        if (getCaseCriteria().containsCriterion(pName)) {
            return getCaseCriteria().getCriterionFromMap(pName);
        }
        if (getPatientCriteria().containsCriterion(pName)) {
            return getPatientCriteria().getCriterionFromMap(pName);
        }
        if (getVpsCriteria().containsCriterion(pName)) {
            return getVpsCriteria().getCriterionFromMap(pName);
        }*/
        if (getUsedCriteria().containsCriterion(pName)) {
            return getUsedCriteria().getCriterionFromMap(pName);
        }
        return null;
    }

    public Group getFirstParentGroup(String pCriterionName) {
 /*       if (getAcgCriteria().containsCriterion(pCriterionName)) {
            return getAcgCriteria().getParentGroup(pCriterionName);
        }
        if (getAmbuCriteria().containsCriterion(pCriterionName)) {
            return getAmbuCriteria().getParentGroup(pCriterionName);
        }
        if (getHospitalCriteria().containsCriterion(pCriterionName)) {
            return getHospitalCriteria().getParentGroup(pCriterionName);
        }
        if (getGkrsaCriteria().containsCriterion(pCriterionName)) {
            return getGkrsaCriteria().getParentGroup(pCriterionName);
        }
        if (getCaseCriteria().containsCriterion(pCriterionName)) {
            return getCaseCriteria().getParentGroup(pCriterionName);
        }
        if (getPatientCriteria().containsCriterion(pCriterionName)) {
            return getPatientCriteria().getParentGroup(pCriterionName);
        }
        if (getVpsCriteria().containsCriterion(pCriterionName)) {
            return getVpsCriteria().getParentGroup(pCriterionName);
        }*/
        if (getUsedCriteria().containsCriterion(pCriterionName)) {
            return getUsedCriteria().getParentGroup(pCriterionName);
        }
        return null;
    }

    public List<Criterion> getAllCriterions() {
        List<Criterion> crits = new ArrayList<>();
/*        crits.addAll(getAcgCriteria().criterionMap().values());
        crits.addAll(getAmbuCriteria().criterionMap().values());
        crits.addAll(getHospitalCriteria().criterionMap().values());
        crits.addAll(getGkrsaCriteria().criterionMap().values());
        crits.addAll(getCaseCriteria().criterionMap().values());
        crits.addAll(getPatientCriteria().criterionMap().values());
        crits.addAll(getVpsCriteria().criterionMap().values());*/
        crits.addAll(getUsedCriteria().criterionMap().values());
        crits.sort(new Comparator<Criterion>() {
            @Override
            public int compare(Criterion o1, Criterion o2) {
                return o1.getCpname().compareTo(o2.getCpname());
            }
        });
        return crits;
    }

    /**
     * @return list of criterion, which usage is NOT CRIT_RULE_ONLY
     */
    public List<Criterion> getAllSuggestionCriterions() {
        List<Criterion> crits = new ArrayList<>();
        getAllCriterions().stream().filter((crit) -> !(USE_RULE_ONLY.equals(crit.getUsage()))).forEachOrdered((crit) -> {
            crits.add(crit);
        });
        return crits;
    }
//    private Criteria basicCriteria;
//    public Criteria getBasicCriteria(){
//        if(basicCriteria == null){
//            try {
//                basicCriteria = initCriteria(getClass().getResource(BASIC_CRITERIA_RESOURCE));
//            } catch (JAXBException ex) {
//                Logger.getLogger(CriteriaManager.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        return basicCriteria;
//    }

    public List<Criteria> getAllAvailableCriteria() {
        List<Criteria> crits = new ArrayList<>();
//        crits.add(getAcgCriteria());
//        crits.add(getAmbuCriteria());
//        crits.add(getCaseCriteria());
//        crits.add(getGkrsaCriteria());
//        crits.add(getHospitalCriteria());
//        crits.add(getPatientCriteria());
//        crits.add(getVpsCriteria());
     crits.add(getUsedCriteria());
        return crits;
    }

    public List<Criteria> filterCriteria(List<Criteria> pCriteriaList, List<Criterion> pCriterions) {
        if (pCriterions.isEmpty()) {
            return new ArrayList<>();
        }
//        Criteria[] copy = Arrays.copyOf(pCriteriaList.toArray(new Criteria[pCriteriaList.size()]),pCriteriaList.size());
//        List<Criteria> filtered = pCriteriaList.stream().collect(Collectors.toList());//new ArrayList<>(Arrays.asList(copy));//new ArrayList<>(copy);
        List<Criteria> filtered = new ArrayList<>();
        for (Criteria orgCrit : pCriteriaList) {
            try {
                filtered.add(initCriteria(getClass().getResource(RESOURCE_FILE + (orgCrit.getSupergroup().getName() + ".xml"))));
            } catch (JAXBException ex) {
                Logger.getLogger(CriteriaManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Iterator<Criteria> it = filtered.iterator();
        while (it.hasNext()) {
            Criteria next = it.next();
            for (Criterion crit : pCriterions) {
                checkCriterion(next, crit);
            }
            if (next.getSupergroup().getGroup().isEmpty()) {
                it.remove();
            }
        }

        return filtered;
    }

    public List<Criteria> filterCriteria(List<Criteria> pCriteriaList, String pCpName) {
        if (pCpName.isEmpty()) {
            return pCriteriaList;
        }
//        Criteria[] copy = Arrays.copyOf(pCriteriaList.toArray(new Criteria[pCriteriaList.size()]),pCriteriaList.size());
//        List<Criteria> filtered = pCriteriaList.stream().collect(Collectors.toList());//new ArrayList<>(Arrays.asList(copy));//new ArrayList<>(copy);
        List<Criteria> filtered = new ArrayList<>();
        for (Criteria orgCrit : pCriteriaList) {
            try {
                filtered.add(initCriteria(getClass().getResource(RESOURCE_FILE + (orgCrit.getSupergroup().getName() + ".xml"))));
            } catch (JAXBException ex) {
                Logger.getLogger(CriteriaManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Iterator<Criteria> it = filtered.iterator();
        while (it.hasNext()) {
            Criteria next = it.next();
            filterCriterion(next, pCpName);
//            for(Criterion crit : pCriterions){
//                checkCriterion(next, crit);
//            }
            if (next.getSupergroup().getGroup().isEmpty()) {
                it.remove();
            }
        }

        return filtered;
    }

    public List<Criteria> filterSuggCriteria(List<Criterion> pCriterions) {
        return filterCriteria(getAllSuggAvailableCriteria(), pCriterions);
    }

    public List<Criteria> filterAllCriteria(List<Criterion> pCriterions) {
        return filterCriteria(getAllAvailableCriteria(), pCriterions);
    }

    public List<Criteria> getAllSuggAvailableCriteria() {
        List<Criteria> crits = new ArrayList<>();
//        try {
//            Criteria acg = removeCriterionType(initCriteria(getClass().getResource(ACG_CRITERIA_RESOURCE)), USE_RULE_ONLY);
//            Criteria ambu = removeCriterionType(initCriteria(getClass().getResource(AMBU_CRITERIA_RESOURCE)), USE_RULE_ONLY);
//            Criteria hospital = removeCriterionType(initCriteria(getClass().getResource(HOSPITAL_CRITERIA_RESOURCE)), USE_RULE_ONLY);
//            Criteria gkrsa = removeCriterionType(initCriteria(getClass().getResource(GKRSA_CRITERIA_RESOURCE)), USE_RULE_ONLY);
//            Criteria caseCrit = removeCriterionType(initCriteria(getClass().getResource(CASE_CRITERIA_RESOURCE)), USE_RULE_ONLY);
//            Criteria patient = removeCriterionType(initCriteria(getClass().getResource(PATIENT_CRITERIA_RESOURCE)), USE_RULE_ONLY);
//            Criteria vps = removeCriterionType(initCriteria(getClass().getResource(VPS_CRITERIA_RESOURCE)), USE_RULE_ONLY);
//            if (acg != null) {
//                crits.add(acg);
//            }
//            if (ambu != null) {
//                crits.add(ambu);
//            }
//            if (hospital != null) {
//                crits.add(hospital);
//            }
//            if (gkrsa != null) {
//                crits.add(gkrsa);
//            }
//            if (caseCrit != null) {
//                crits.add(caseCrit);
//            }
//            if (patient != null) {
//                crits.add(patient);
//            }
//            if (vps != null) {
//                crits.add(vps);
//            }
//        } catch (JAXBException ex) {
//            Logger.getLogger(CriteriaManager.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return crits;
    }

    public Boolean hasInterval(Criterion pCriterion) {
        if (pCriterion == null) {
            return false;
        }
        Group group = getGroup(pCriterion);
        if (group == null) {
            return false;
        }
        return Objects.requireNonNullElse(group.isHasInterval(), false);
    }

    public Group getGroup(Criterion pCriterion) {
        if (pCriterion == null) {
            return null;
        }

        return getFirstParentGroup(pCriterion.getCpname());
    }

    private Criteria removeCriterionType(Criteria criteria, String pType) {
        Iterator<Group> it = criteria.getSupergroup().getGroup().iterator();
        while (it.hasNext()) {
            Group next = it.next();
            Iterator<Criterion> critIt = next.getCriterion().iterator();
            while (critIt.hasNext()) {
                Criterion crit = critIt.next();
                if (crit.getUsage().equals(pType)) {
                    critIt.remove();
                }
            }
            if (next.getCriterion().isEmpty()) {
                it.remove();
            }
        }
        if (criteria.getSupergroup().getGroup().isEmpty()) {
            return null;
        }
        return criteria;
    }

    private boolean checkCriterion(Criteria criteria, Criterion pCriterion) {
        if (criteria.hasCriterion(pCriterion.getCpname())) {
            return false;
        }
        Iterator<Group> it = criteria.getSupergroup().getGroup().iterator();
        while (it.hasNext()) {
            Group next = it.next();
            if (next.getCriterion().contains(pCriterion)) {
                Iterator<Criterion> critIt = next.getCriterion().iterator();
                while (critIt.hasNext()) {
                    Criterion crit = critIt.next();
                    if (!crit.getCpname().equals(pCriterion.getCpname())) {
                        critIt.remove();
                        //                    return true;
                    }
                }
            }
            if (next.getCriterion().isEmpty()) {
                it.remove();
//                return false;
            }
        }
        return false;
    }
//    return checkEditorText(((Criterion) item).getCpname())||checkEditorText(getDisplayName((Criterion)item));
//                });
//            }
//        }
//        if (!cbCriteria.isShowing()) {
////                        if (oldValue != null && !oldValue.isEmpty()) {
//            if (popover == null || !popover.isShowing()) {
//                cbCriteria.show();
//            }
//        }
//    }

    private boolean checkCriteriaText(String pText, String pCriteriaText) {
        if (pText == null) {
            return false;
        }
        return pCriteriaText.trim().toUpperCase().contains(pText.trim().toUpperCase());
    }

    private String getDisplayName(Criterion pCriterion) {
        return CriteriaHelper.getDisplayName(pCriterion);
    }

    private boolean filterCriterion(Criteria criteria, String pText) {
//        if(criteria.hasCriterion(pCriterion.getCpname())){
//            return false;
//        }
        Iterator<Group> it = criteria.getSupergroup().getGroup().iterator();
        while (it.hasNext()) {
            Group next = it.next();
//            if(next.getCriterion().contains(pCriterion)){
            Iterator<Criterion> critIt = next.getCriterion().iterator();
            while (critIt.hasNext()) {
                Criterion crit = critIt.next();
//                    if((!crit.getCpname().toUpperCase().contains(pCpName.toUpperCase())) || (!getDisplayName(crit).toUpperCase().contains(pCpName.toUpperCase()))){
//                    if(!crit.getCpname().toUpperCase().contains(pCpName.toUpperCase())||!getDisplayName(crit).toUpperCase().contains(pCpName.toUpperCase())){
                if ((!checkCriteriaText(pText, crit.getCpname()))) {
                    if ((!checkCriteriaText(pText, getDisplayName(crit)))) {
                        critIt.remove();
                    }
                    //                    return true;
                }
            }
//            }
            if (next.getCriterion().isEmpty()) {
                it.remove();
//                return false;
            }
        }
        return false;
    }

    public class CriterionTreeObjectFactory {

        private final Class<?> clazz;

        public CriterionTreeObjectFactory(Class<?> pClazz) {
            clazz = pClazz;
        }

        public CriterionTree createCriterionTree() {
            if (clazz == CaseCriteria.class) {
                return new CaseCriteria();
            }
            return new Criteria();
        }
    }
}
