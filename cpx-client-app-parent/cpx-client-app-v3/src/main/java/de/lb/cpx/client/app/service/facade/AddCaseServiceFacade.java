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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.service.facade;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog;
import de.lb.cpx.client.core.model.catalog.CpxDepartmentCatalog;
import de.lb.cpx.client.core.model.catalog.CpxDoctorCatalog;
import de.lb.cpx.client.core.model.catalog.CpxDrg;
import de.lb.cpx.client.core.model.catalog.CpxDrgCatalog;
import de.lb.cpx.client.core.model.catalog.CpxHospital;
import de.lb.cpx.client.core.model.catalog.CpxHospitalCatalog;
import de.lb.cpx.client.core.model.catalog.CpxInsuranceCompany;
import de.lb.cpx.client.core.model.catalog.CpxInsuranceCompanyCatalog;
import de.lb.cpx.client.core.model.catalog.CpxPeppCatalog;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.service.ejb.AddCaseEJBRemote;
import de.lb.cpx.service.ejb.SingleCaseGroupingEJBRemote;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.validation.constraints.NotNull;

/**
 * ServiceFacade for all ServiceMethodes to manually case creation
 *
 * @author wilde
 */
public class AddCaseServiceFacade {

    private EjbProxy<AddCaseEJBRemote> addCaseEJB;
    private CpxInsuranceCompanyCatalog insuranceCatalog;
    private CpxDepartmentCatalog departmentCatalog;
    private CpxHospitalCatalog hospitalCatalog;
    private CpxDoctorCatalog doctorCatalog;
    private TPatient currentPatient;
    private TCase currentCase;
    private EjbProxy<SingleCaseGroupingEJBRemote> singleGroupingEJB;
    private CpxDrgCatalog drgCatalog;
    private CpxPeppCatalog peppCatalog;

    public AddCaseServiceFacade() {
        init();
    }

    private void init() {
        addCaseEJB = Session.instance().getEjbConnector().connectAddCaseBean();
        singleGroupingEJB = Session.instance().getEjbConnector().connectSingleCaseGroupingBean();
        insuranceCatalog = CpxInsuranceCompanyCatalog.instance();
        departmentCatalog = CpxDepartmentCatalog.instance();
        hospitalCatalog = CpxHospitalCatalog.instance();
        doctorCatalog = CpxDoctorCatalog.instance();
        drgCatalog = CpxDrgCatalog.instance();
        peppCatalog = CpxPeppCatalog.instance();
    }

    public List<String> getMatchesForPatientNumber(String number) {
        return addCaseEJB.get().getMatchForPatientNumber(number);
    }

    public TPatient loadPatient(String patientNumber) {
        return addCaseEJB.get().getPatient(patientNumber);
    }

    public Collection<String> getMatchesForInsuranceNumber(String partialInsuranceNumber) {
        return insuranceCatalog.findMatchesByInsuranceNumber(partialInsuranceNumber, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

    public Collection<String> getMatchesForIdent(String partialInsuranceNumber) {
        return insuranceCatalog.findMatchesByIdent(partialInsuranceNumber, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }
    //RSH 02112017  CPX-642

    public Collection<String> getMatchesForInsuranceName(String partialInsuranceName) {
        return insuranceCatalog.findMatchesByInsName(partialInsuranceName, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

    public Collection<String> getMatchesForInsuranceZipCode(String partialInsuranceZipCode) {
        return insuranceCatalog.findMatchesByInsZipCode(partialInsuranceZipCode, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

    public Collection<String> getMatchesForInsuranceCity(String partialInsuranceCity) {
        return insuranceCatalog.findMatchesByCity(partialInsuranceCity, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

    public Collection<String> getMatchesForInsuranceNumber(String partialInsuranceNumber, String insc_type) {
        if (insc_type == null) {
            insc_type = "%";
        }
        return insuranceCatalog.findMatchesByInsuranceNumber(partialInsuranceNumber, insc_type, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

    public CpxInsuranceCompany loadInsurance(String insuranceNumber) {
        return insuranceCatalog.getByCode(insuranceNumber, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

    public Collection<String> getInsuranceTypes() {
        return insuranceCatalog.getInsuranceTypes(AbstractCpxCatalog.DEFAULT_COUNTRY);
    }
    //RSH 02112017  CPX-642

    public CpxInsuranceCompany getInsuranceByIdent(String pInsIdent) {
        return insuranceCatalog.getByCode(pInsIdent, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

    public CpxInsuranceCompany getInsuranceDetailByIdent(String pInsIdent) {
        return insuranceCatalog.getInsuranceDetailByIdent(pInsIdent, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

    public CpxInsuranceCompany getInsuranceByName(String pInsName) {
        // str=INSC_NAME , INSC_IDENT",INSC_CITY
        String str[] = pInsName.split(",");
        return insuranceCatalog.getByCode(str[1].trim(), AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

    public CpxInsuranceCompany getInsuranceByZipCode(String zipInsName) {
        String str[] = zipInsName.split(",");

        return insuranceCatalog.getByZipInsNameIdent(str[0].trim(), str[1].trim(), str[2].trim(), AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

    public CpxInsuranceCompany getInsuranceByCity(String InsNameIdent) {
        String str[] = InsNameIdent.split(",");
        return insuranceCatalog.getByCityInsNameIdent(str[0].trim(), str[1].trim(), str[2].trim(), AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

    public List<TCaseIcd> get50Icds() {
        return addCaseEJB.get().getTestIcds(50);
    }

    public List<TCaseOps> get50Ops() {
        return addCaseEJB.get().getTestOps(50);
    }

    public Collection<String> getBestMatchesForDepartment(String userText, String country) {
        return departmentCatalog.getBestMatches(userText, country);
    }

    public String getDepartmentNameWithDesc(String depShortName, String country) {
        return departmentCatalog.getDepartmentNameWithDesc(depShortName, country);
    }

    public Collection<String> getBestMatchesForHospital(String userText) {
        return hospitalCatalog.getBestMatchForHospital(userText, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

    public Collection<String> getBestMatchesForHospitalIdent(String userText) {
        return hospitalCatalog.getBestMatchForHospitalIdent(userText, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }
    //RSH 07092017  CPX-628

    public Collection<String> getBestMatchesForHospitalName(String userText) {
        return hospitalCatalog.getBestMatchForHospitalName(userText, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }
    //RSH 22.09.2017 CPX-628

    public Collection<String> getBestMatchesForHospitalZipCode(String userText) {
        return hospitalCatalog.getBestMatchForHospitalZipCode(userText, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

    public Collection<String> getBestMatchesForHospitalCity(String userText) {
        return hospitalCatalog.getBestMatchForHospitalCity(userText, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

    public Collection<String> getBestMatchesForHospitalAddresse(String userText) {
        return hospitalCatalog.getBestMatchForHospitalAddresse(userText, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

    public Collection<String> getBestMatchesForDoctor(String userText) {
        return doctorCatalog.getBestMatchForDoctor(userText, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

    public CpxHospital getHospitalByIdent(String newValue) {
        return hospitalCatalog.getByCode(newValue, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }
    //RSH 07092017  CPX-628

    public CpxHospital getHospitalByName(String newValue) {
        return hospitalCatalog.getByName(newValue, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }
    //RSH 22.09.2017 CPX-628

    public CpxHospital getHospitalByZipCode(String newValue) {
        return hospitalCatalog.getByZipCode(newValue, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

    public CpxHospital getHospitalByCity(String newValue) {
        return hospitalCatalog.getByCity(newValue, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

    public CpxHospital getHospitalByAddresse(String newValue) {
        return hospitalCatalog.getByAddresse(newValue, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

    public CpxHospital getHospitalDetailsByCity(String newValue) {
        return hospitalCatalog.getBestMatchForHospitalDetailsByCity(newValue, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

    public CpxHospital getHospitalDetailsByZipCode(String newValue) {
        return hospitalCatalog.getBestMatchForHospitalDetailsByZipCode(newValue, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

    public CpxHospital getHospitaldetailsByName(String hosName) {
        return hospitalCatalog.getBestMatchForHospitalDetailsByName(hosName, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

    public CpxHospital getHospitaldetailsByIdent(String pHosIdent) {
        return hospitalCatalog.getBestMatchForHospitalDetailsByCode(pHosIdent, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

    public TPatient getCurrentPatient() {
        return currentPatient;
    }

    public void setCurrentPatient(TPatient currentPatient) {
        this.currentPatient = currentPatient;
    }

    public TCase getCurrentCase() {
        return currentCase;
    }

    public void setCurrentCase(TCase currentCase) {
        this.currentCase = currentCase;
    }

    public ObservableList<TCaseIcd> getIcdsForCurrentCase() {
        ObservableList<TCaseIcd> list = FXCollections.observableArrayList();

        if (this.currentCase.getCurrentExtern() != null) {
            this.currentCase.getCurrentExtern().getCaseDepartments().stream().forEach((department) -> {
                list.addAll(department.getCaseIcds());
            });
        }

        return list;
    }

    public ObservableList<TCaseOps> getOpsForCurrentCase() {
        ObservableList<TCaseOps> list = FXCollections.observableArrayList();

        if (this.currentCase.getCurrentExtern() != null) {
            this.currentCase.getCurrentExtern().getCaseDepartments().stream().forEach((department) -> {
                list.addAll(department.getCaseOpses());
            });
        }

        return list;
    }

    public TGroupingResults getTempGroupingResults(TCase currentCase, GDRGModel selectedGrouper) throws CpxIllegalArgumentException {
        return singleGroupingEJB.get().getTempGroupingResultsExtern(currentCase, selectedGrouper);
    }

    public boolean isHospitalExisting(String hospitalIdent) {
        return hospitalCatalog.hasEntry(hospitalIdent, AbstractCpxCatalog.DEFAULT_COUNTRY);

    }

    public boolean isHospitalNameExisting(String hospitalName) {
        return hospitalCatalog.hasEntryName(hospitalName, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

    public boolean isDepartmentExisting(String departmentShort) {
        return departmentCatalog.hasEntry(departmentShort, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

    public boolean hasMainDiagnosis() {
        if (currentCase == null) {
            return false;
        }
        if (currentCase.getCurrentExtern() == null) {
            return false;
        }
        for (TCaseIcd icd : getIcdsForCurrentCase()) {
            if (icd.getIcdcIsHdxFl()) {
                return true;
            }
        }
        return false;
    }

    public CpxDrg getDrgCatalogDescription(String grpresCode, Integer year) {
        return drgCatalog.getByCode(grpresCode, "de", year);
    }

    public String getDrgCatalogDescription(String grpresCode, Date admDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(admDate == null ? new Date() : admDate);
//        CpxDrg catalogEntry = drgCatalog.getByCode(grpresCode, "de", cal.get(Calendar.YEAR));
//        if(catalogEntry != null){
//            return catalogEntry.getDrgDescription()==null?"":catalogEntry.getDrgDescription();
//        }
        return drgCatalog.getDrgDescription(grpresCode, "de", cal.get(Calendar.YEAR));
    }

    public String getDrgCatalogDescriptionText(String grpresCode, Date admDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(admDate == null ? new Date() : admDate);

        return drgCatalog.getDrgDecriptionText(grpresCode, "de", cal.get(Calendar.YEAR));
    }

    public void savePatient(TPatient patient) {
        patient.setId(addCaseEJB.get().savePatient(patient));
    }

    public void saveCurrentPatient() {
        savePatient(currentPatient);
    }

    public void saveCase(TCase hCase) {
        hCase.setId(addCaseEJB.get().saveCase(hCase));
    }

    public void saveCurrentCase() {
        saveCase(currentCase);
    }

    /**
     * Check if Case allready exists in db with the same casenumber and
     * hosptailident
     *
     * @param caseNumber number if the case
     * @param hospitalIdent unique ident of the hospital
     * @return if a case with that data is found
     */
    public boolean checkIfCaseExists(String caseNumber, String hospitalIdent) {
        return addCaseEJB.get().checkIfCaseExists(caseNumber, hospitalIdent);
    }

    /**
     * Attempt to get the Lastdepartment from the set
     *
     * @return last department in the Set
     */
    public TCaseDepartment getLastDepartment() {
        if (!getCurrentCase().getCurrentExtern().getCaseDepartments().isEmpty()) {
            final Iterator<TCaseDepartment> itr = getCurrentCase().getCurrentExtern().getCaseDepartments().iterator();
            TCaseDepartment lastElement = itr.next();
            while (itr.hasNext()) {
                lastElement = itr.next();
            }
            return lastElement;
        }
        return null;
    }

    public boolean isInsuranceExisting(String insuranceIdent) {
        return insuranceCatalog.hasEntry(insuranceIdent, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

    public void dispose() {
        currentCase = null;
        currentPatient = null;
    }

    /**
     * merge case
     */
    public void updateCurrentCase() {
        updateCase(currentCase);
    }

    /**
     *
     * @param hCase TCase object
     */
    private void updateCase(TCase hCase) {
        addCaseEJB.get().updateCase(hCase);
    }

    /**
     *
     * @param hcase case nummer
     * @param hospitalIdent hospitalident
     * @return instance of Tcase
     */
    public TCase loadCase(String hcase, String hospitalIdent) {
        return addCaseEJB.get().findeCase(hcase, hospitalIdent);
    }

    /**
     * find existing case to add new version in case adding due to loading
     * existing case, case data will hold more data than if case was loaded with
     * loadCase()
     *
     * @param hcase caseNumber, to identify case
     * @param hospitalIdent hospital ident, to identify case
     * @return case with extended patient data
     */
    public TCase loadExistingCase(@NotNull String hcase, @NotNull String hospitalIdent) {
        Objects.requireNonNull(hcase);
        Objects.requireNonNull(hospitalIdent);
        return addCaseEJB.get().findExistingCase(hcase, hospitalIdent);
    }

    public String getPeppCatalogDescriptionText(String grpresCode, Date admDate) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(admDate == null ? new Date() : admDate);

        return peppCatalog.getPeppDecriptionText(grpresCode, "de", cal.get(Calendar.YEAR));
    }

    public String getPeppCatalogDescription(String grpresCode, Date admDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(admDate == null ? new Date() : admDate);

        return peppCatalog.getPeppDescription(grpresCode, "de", cal.get(Calendar.YEAR));
    }

    public boolean isDepartmentNameExisting(String departmentShort, String departmentName) {
        return departmentCatalog.hasEntryName(departmentShort, departmentName, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

}
