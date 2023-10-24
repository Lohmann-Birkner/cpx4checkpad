/* 
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.util.bookmarksmapping;

import de.checkpoint.drg.GDRGModel;
import de.checkpoint.utils.UtlDateTimeConverter;
import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog;
import de.lb.cpx.client.core.model.catalog.CpxBaserateCatalog;
import de.lb.cpx.client.core.model.catalog.CpxDepartmentCatalog;
import de.lb.cpx.client.core.model.catalog.CpxHospital;
import de.lb.cpx.client.core.model.catalog.CpxHospitalCatalog;
import de.lb.cpx.client.core.model.catalog.CpxInsuranceCompany;
import de.lb.cpx.client.core.model.catalog.CpxInsuranceCompanyCatalog;
import de.lb.cpx.client.core.model.catalog.CpxMdkCatalog;
import de.lb.cpx.connector.EjbConnector;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseDrg;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.TCasePepp;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.model.TPatientDetails;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.GenderEn;
import de.lb.cpx.model.enums.SupplFeeTypeEn;
import de.lb.cpx.server.commonDB.model.CBookmarksCustomer;
import de.lb.cpx.server.commonDB.model.CMdk;
import de.lb.cpx.server.commonDB.model.CWmListProcessTopic;
import de.lb.cpx.server.commonDB.model.CdbUsers;
import de.lb.cpx.service.ejb.ProcessServiceBeanRemote;
import de.lb.cpx.service.ejb.SingleCaseEJBRemote;
import de.lb.cpx.service.ejb.TemplateServiceBeanRemote;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmAction;
import de.lb.cpx.wm.model.TWmProcess;
import de.lb.cpx.wm.model.TWmProcessCase;
import de.lb.cpx.wm.model.TWmProcessHospital;
import de.lb.cpx.wm.model.TWmRequest;
import de.lb.cpx.wm.model.TWmRequestAudit;
import de.lb.cpx.wm.model.TWmRequestBege;
import de.lb.cpx.wm.model.TWmRequestInsurance;
import de.lb.cpx.wm.model.TWmRequestMdk;
import de.lb.cpx.wm.model.TWmRequestOther;
import de.lb.cpx.wm.model.TWmRequestReview;
import de.lb.cpx.wm.model.enums.WmRequestTypeEn;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * creation of the hashmap to use common Bookmarks in MS word templates and text
 * templates as well.
 *
 * @author nandola
 */
public class CreateBookmarksHashMap {

    private static final Logger LOG = Logger.getLogger(CreateBookmarksHashMap.class.getName());
    private final EjbProxy<TemplateServiceBeanRemote> templateServiceBean;
    private final EjbProxy<ProcessServiceBeanRemote> processServiceBean;
    private final EjbProxy<SingleCaseEJBRemote> caseEJB;
    private final CpxBaserateCatalog baserateCatalog;

    public CreateBookmarksHashMap() {
        Session session = Session.instance();
        EjbConnector connector = session.getEjbConnector();
        templateServiceBean = connector.connectTemplateServiceBean();
        processServiceBean = connector.connectProcessServiceBean();
        caseEJB = connector.connectSingleCaseBean();
        baserateCatalog = CpxBaserateCatalog.instance();
    }

    private static TCase getCaseFromProcess(final TWmProcess pProcess) {
        if (pProcess == null) {
            return null;
        }
        TWmProcessCase processCase = pProcess.getMainProcessCase();
        if (processCase == null) {
            return null;
        }
        TCase cs = processCase.getHosCase();
        if (cs == null) {
            return null;
        }
        return cs;
    }

    public Map<String, String> fillHashMap(final ProcessServiceFacade pFacade) {
        return fillHashMap(pFacade, null);
    }

    public Map<String, String> fillHashMap(final ProcessServiceFacade pFacade, final TCase pCase) {
        // add key-value pairs to this Hash-Map
        final long startTime = System.currentTimeMillis();
        final Map<String, String> map = new HashMap<>();
        TWmProcess process = pFacade.getCurrentProcess();
        final TCase cs;
        if (pCase == null) {
            cs = getCaseFromProcess(process);
        } else {
            cs = pCase;
        }
        fillHashMapCurrentUser(map);
        fillHashMapPatient(pFacade, map, cs == null ? null : cs.getPatient());
        fillHashMapCase(map, cs);
        fillHashMapProcess(pFacade, map, process);
        fillHashMapLatestRequests(pFacade, map);
        fillHashMapLatestAction(pFacade, map);
        fillHashMapCustomerBookmarks(map);
        //after filling a Map, show all key-value pairs.
        showAllHashMapKeyvalues(map);
        LOG.log(Level.INFO, "bookmarks filled in {0} ms", System.currentTimeMillis() - startTime);
        return map;
    }

    public Map<String, String> fillHashMap(final TCase pCase) {
        // add key-value pairs to this Hash-Map
        final Map<String, String> map = new HashMap<>();
        fillHashMapCurrentUser(map);
        fillHashMapPatient(null, map, pCase == null ? null : pCase.getPatient());
        fillHashMapCase(map, pCase);
        fillHashMapCustomerBookmarks(map);
        //after filling a Map, show all key-value pairs.
        showAllHashMapKeyvalues(map);
        return map;
    }

    private void fillHashMapPatient(final ProcessServiceFacade pFacade, final Map<String, String> map, final TPatient patient) {
        if (patient == null) {
            //LOG.log(Level.SEVERE, "There is no Patient for the CaseNo: {0}", cs.getCsCaseNumber());
            LOG.log(Level.SEVERE, "Patient is null!");
            return;
        }

        // patient information
        addParamValues(map, "patientnr", patient.getPatNumber());
        addParamValues(map, "title", patient.getPatTitle());
        addParamValues(map, "name", patient.getPatSecName());
        addParamValues(map, "name_kopie", patient.getPatSecName());
        addParamValues(map, "vorname", patient.getPatFirstName());
        addParamValues(map, "vorname_kopie", patient.getPatFirstName());
        addParamValues(map, "geburtsdatum", patient.getPatDateOfBirth());
        addParamValues(map, "geschlecht", patient.getPatGenderEn());
        addParamValues(map, "anrede", getSalutation(patient.getPatGenderEn()));
        TPatientDetails patDetails = null;
        if (pFacade != null) {

            // detailed information about patient
            pFacade.findActualPatientDetails(patient.getId());
        } else {
            patDetails = patient.getCurrentDetail();
        }
        if (patDetails != null) {
            addParamValues(map, "patstrasse", patDetails.getPatdAddress());
            addParamValues(map, "patplz", patDetails.getPatdZipcode());
            addParamValues(map, "patort", patDetails.getPatdCity());
            addParamValues(map, "patphone", patDetails.getPatPhoneNumber());
            addParamValues(map, "patmobile", patDetails.getPatCellNumber());
        } else {
            LOG.log(Level.WARNING, "There is no Patient Details for the Patient No: {0}", patient.getPatNumber());
        }

    }

    private void fillHashMapCase(final Map<String, String> map, final TCase cs) {
        if (cs == null) {
            LOG.log(Level.SEVERE, "Case is null!");
            return;
        }
        GDRGModel selectedGrouper = CpxClientConfig.instance().getSelectedGrouper();
        CpxInsuranceCompanyCatalog catalog = CpxInsuranceCompanyCatalog.instance();
        if (cs.getInsuranceNumberPatient() != null) {
            // both are the same Bookmarks (cpx-1402)
            addParamValues(map, "patversnr", cs.getInsuranceNumberPatient());//pCase.getPatient().getPatInsuranceActual().getInsNumber() == null ? "" : pCase.getPatient().getPatInsuranceActual().getInsNumber());
            addParamValues(map, "versnr", cs.getInsuranceNumberPatient());
        } else {
            LOG.log(Level.FINE, "There is no InsuranceNumberPatient for the CaseNo: {0}", cs.getCsCaseNumber());
        }
        if (cs.getInsuranceIdentifier() != null) {
            addParamValues(map, "versik", cs.getInsuranceIdentifier());
            CpxInsuranceCompany ins = catalog.getByCode(cs.getInsuranceIdentifier(), AbstractCpxCatalog.DEFAULT_COUNTRY);
            // insurance details of patient
            if (ins.getId() != 0L) {
                addParamValues(map, "versname", ins.getInscShort());
                addParamValues(map, "versname_kopie", ins.getInscShort());
                addParamValues(map, "versname_lang", ins.getInscName());
                addParamValues(map, "versname_lang_kopie", ins.getInscName());
                addParamValues(map, "versstrasse", ins.getInscAddress());
                addParamValues(map, "verslandeskz", ins.getCountryEn());
                addParamValues(map, "versplz", ins.getInscZipCode());
                addParamValues(map, "versort", ins.getInscCity());
                addParamValues(map, "verstelefonvorwahl", ins.getInscPhonePrefix());
                addParamValues(map, "verstelefon", ins.getInscPhone());
                addParamValues(map, "verstelefax", ins.getInscFax());
            } else {
                LOG.log(Level.WARNING, "There is no InsuranceCompany for the Insurance Identifier: {0}", cs.getInsuranceIdentifier() + "so, Insurance company related "
                        + "Bookmarks doesn't filled...");
            }
        } else {
            LOG.log(Level.FINE, "There is no InsuranceIdentifier for the CaseNo: {0}", cs.getCsCaseNumber());
        }

        // get hospital catalog to get related data.
        CpxHospital hos = CpxHospitalCatalog.instance().getByCode(cs.getCsHospitalIdent(), AbstractCpxCatalog.DEFAULT_COUNTRY);
        addParamValues(map, "hospital_ik", cs.getCsHospitalIdent());
        if (hos != null && hos.getId() != 0L) {
            addParamValues(map, "hospital_name", hos.getHosName());
            addParamValues(map, "hospital_ort", hos.getHosCity());
            addParamValues(map, "hospital_plz", hos.getHosZipCode());
            addParamValues(map, "hospital_strasse", hos.getHosAddress());
        } else {
            LOG.log(Level.SEVERE, "There is no corresponding Hospital for the HospitalIdent: {0},"
                    + " so Hospital specific Bookmarks doesn't filled...", cs.getCsHospitalIdent());
        }

        addParamValues(map, "fall_fallnummer", cs.getCsCaseNumber());  // case number
        addParamValues(map, "fall_fallnummer_kopie", cs.getCsCaseNumber());
        addParamValues(map, "fall_aufnahmenr", cs.getCsCaseNumber()); // not used

        TCaseDetails localDetails = cs.getCurrentLocal();    // current cpx-version (Local version)
        TCaseDetails externDetails = cs.getCurrentExtern();  // current kis-version (Extern version)

        if (localDetails != null) {
            addParamValues(map, "fall_aufnahmedtm", localDetails.getCsdAdmissionDate());
            addParamValues(map, "fall_entlassungdtm", localDetails.getCsdDischargeDate());
            addParamValues(map, "fall_beatmungsst", localDetails.getCsdHmv());  // if local_fl is set and actual_fl is set.
        } else {
            LOG.log(Level.SEVERE, "There is no actual current local caseDetails for the caseNo: {0}, "
                    + "so local caseDetails related Bookmarks doesn't filled...", cs.getCsCaseNumber());
        }
        // DRG fall
        // tGroupingResultsDao.findGroupingResult_nativ_lazy(case_details.getId(), selectedGrouper);
        TGroupingResults groupingResultsLocal = templateServiceBean.get().getResultsLocal(localDetails!=null?localDetails.getId():-1L, selectedGrouper);
        TGroupingResults groupingResultsExtern = templateServiceBean.get().getResultsExtern(externDetails!=null?externDetails.getId():-1L, selectedGrouper);

        if (groupingResultsLocal != null && groupingResultsExtern != null) {
//                TGroupingResults caseDrg = groupingResultsLocal;
//                TGroupingResults caseKis = groupingResultsExtern;
                addParamValues(map, "fall_drg_kis", groupingResultsExtern.getGrpresCode());
                addParamValues(map, "fall_hauptdiag_kis", (groupingResultsExtern.getCaseIcd() != null && groupingResultsExtern.getCaseIcd().getIcdcIsHdxFl()) ? groupingResultsExtern.getCaseIcd().getIcdcCode() : "");

                List<TCaseIcd> kisCaseDetailsIcds = externDetails == null ? new ArrayList<>() : caseEJB.get().findIcdsForCaseDetailId(externDetails.getId(), selectedGrouper);
                if (kisCaseDetailsIcds != null && !kisCaseDetailsIcds.isEmpty()) {
                    addParamValues(map, "fall_nebendiag_kis",
                            kisCaseDetailsIcds.stream().filter(new Predicate<TCaseIcd>() {
                                @Override
                                public boolean test(TCaseIcd t) {
                                    return !t.getIcdcIsHdxFl();
                                }
                            }).map(item -> item.getIcdcCode()).collect(Collectors.joining(",")));
                } else {
                    LOG.log(Level.WARNING, "List of Icds For the Extern CaseDetail are null or empty..");
                    addParamValues(map, "fall_nebendiag_kis", "");
                }

                List<TCaseOps> kisCaseDetailsOpses = externDetails == null ? new ArrayList<>() : caseEJB.get().findOpsForCaseDetailsId(externDetails.getId());
                if (kisCaseDetailsOpses != null) {
                    addParamValues(map, "fall_prozeduren_kis",
                            kisCaseDetailsOpses.stream()
                                    .map(item -> item.getOpscCode()).collect(Collectors.joining(",")));
                } else {
                    LOG.log(Level.WARNING, "List of Opses For the Extern CaseDetail are null or empty..");
                    addParamValues(map, "fall_prozeduren_kis", "");
                }

                addParamValues(map, "fall_drg_drg", groupingResultsLocal.getGrpresCode());
                addParamValues(map, "fall_hauptdiag_drg", (groupingResultsLocal.getCaseIcd() != null && groupingResultsLocal.getCaseIcd().getIcdcIsHdxFl()) ? groupingResultsLocal.getCaseIcd().getIcdcCode() : "");

                List<TCaseIcd> drgCaseDetailsIcds = localDetails == null ? new ArrayList<>() : caseEJB.get().findIcdsForCaseDetailId(localDetails.getId(), selectedGrouper);
                if (drgCaseDetailsIcds != null) {
                    addParamValues(map, "fall_nebendiag_drg",
                            drgCaseDetailsIcds.stream().filter(new Predicate<TCaseIcd>() {
                                @Override
                                public boolean test(TCaseIcd t) {
                                    return !t.getIcdcIsHdxFl();
                                }
                            }).map(item -> item.getIcdcCode()).collect(Collectors.joining(",")));
                } else {
                    LOG.log(Level.WARNING, "List of Icds For the Local CaseDetail are null or empty..");
                    addParamValues(map, "fall_nebendiag_drg", "");
                }

                List<TCaseOps> drgCaseDetailsOpses = localDetails == null ? new ArrayList<>() : caseEJB.get().findOpsForCaseDetailsId(localDetails.getId());
                if (drgCaseDetailsOpses != null) {
                    addParamValues(map, "fall_prozeduren_drg",
                            drgCaseDetailsOpses.stream()
                                    .map(item -> item.getOpscCode()).collect(Collectors.joining(",")));
                } else {
                    LOG.log(Level.WARNING, "List of Opses For the Local CaseDetail are null or empty..");
                    addParamValues(map, "fall_prozeduren_drg", "");
                }

                //SANA BB (New Bookmarks)
                //from grouping results?
                // not sure if "ze_dif" and "erloes_dif" are correct.

//                Double findDrgBaserateFeeValueLocal = localDetails == null ? null : templateServiceBean.get().findDrgBaserateFeeValue(cs.getCsHospitalIdent(), localDetails.getCsdAdmissionDate(), AbstractCpxCatalog.DEFAULT_COUNTRY);
//                Double findDrgBaserateFeeValueKis = externDetails == null ? null : templateServiceBean.get().findDrgBaserateFeeValue(cs.getCsHospitalIdent(), externDetails.getCsdAdmissionDate(), AbstractCpxCatalog.DEFAULT_COUNTRY);
//                
//                Double findCareBaserateFeeValueLocal = localDetails == null ? null : templateServiceBean.get().findCareBaserateFeeValue(cs.getCsHospitalIdent(), localDetails.getCsdAdmissionDate(), AbstractCpxCatalog.DEFAULT_COUNTRY);
//                Double findCareBaserateFeeValueKis = externDetails == null ? null : templateServiceBean.get().findCareBaserateFeeValue(cs.getCsHospitalIdent(), externDetails.getCsdAdmissionDate(), AbstractCpxCatalog.DEFAULT_COUNTRY);
                
            if (CaseTypeEn.DRG.equals(cs.getCsCaseTypeEn())) {
                TCaseDrg caseDrg = (TCaseDrg) groupingResultsLocal; // groupingResultsLocal.getCaseDrg();
                TCaseDrg caseKis = (TCaseDrg) groupingResultsExtern;
                GDRGModel grouper = CpxClientConfig.instance().getSelectedGrouper();
                Double supplFeeFrmLocalGrpRes = localDetails != null ? processServiceBean.get().findSupplementaryFee(grouper, localDetails.id, SupplFeeTypeEn.ZE) : 0.0;
                Double supplFeeFrmExternGrpRes = externDetails != null ? processServiceBean.get().findSupplementaryFee(grouper, externDetails.id, SupplFeeTypeEn.ZE) : 0.0;
                addParamValues(map, "ze_dif", Lang.toDecimal((supplFeeFrmExternGrpRes - supplFeeFrmLocalGrpRes), 2) + " " + Lang.getCurrencySymbol());
		addParamValues(map, "ze_drg", Lang.toDecimal( supplFeeFrmLocalGrpRes, 2) + " " + Lang.getCurrencySymbol());
                addParamValues(map, "ze_kis", Lang.toDecimal( supplFeeFrmExternGrpRes, 2) + " " + Lang.getCurrencySymbol());
                addParamValues(map, "fall_drgcw_kis", Lang.toDecimal(caseKis.getDrgcCwEffectiv(), 3));
                addParamValues(map, "fall_drgcw_drg", Lang.toDecimal(caseDrg.getDrgcCwEffectiv(), 3));
                addParamValues(map, "fall_pflegecw_kis", Lang.toDecimal(caseKis.getDrgcCareCw(), 4));
                addParamValues(map, "fall_pflegecw_cpx", Lang.toDecimal(caseDrg.getDrgcCareCw(), 4));
                Double findDrgBaserateFeeValueLocal = localDetails == null ? null : baserateCatalog.findDrgBaserateFeeValue(cs.getCsHospitalIdent(), localDetails.getCsdAdmissionDate(), AbstractCpxCatalog.DEFAULT_COUNTRY);
                Double findDrgBaserateFeeValueKis = externDetails == null ? null : baserateCatalog.findDrgBaserateFeeValue(cs.getCsHospitalIdent(), externDetails.getCsdAdmissionDate(), AbstractCpxCatalog.DEFAULT_COUNTRY);
                
                Double findCareBaserateFeeValueLocal = localDetails == null ? null : baserateCatalog.findCareBaserateFeeValue(cs.getCsHospitalIdent(), localDetails.getCsdAdmissionDate(), AbstractCpxCatalog.DEFAULT_COUNTRY);
                Double findCareBaserateFeeValueKis = externDetails == null ? null : baserateCatalog.findCareBaserateFeeValue(cs.getCsHospitalIdent(), externDetails.getCsdAdmissionDate(), AbstractCpxCatalog.DEFAULT_COUNTRY);

                if (findDrgBaserateFeeValueLocal != null && findDrgBaserateFeeValueKis != null) {
                    // full revenue
                    Double erloesValueLocal = caseDrg.getRevenue(findDrgBaserateFeeValueLocal,findCareBaserateFeeValueLocal);
                    Double erloesValueKis = caseKis.getRevenue(findDrgBaserateFeeValueKis,findCareBaserateFeeValueKis);
                    addParamValues(map, "erloes_dif", Lang.toDecimal((erloesValueKis - erloesValueLocal), 2) + " " + Lang.getCurrencySymbol());   // two decimal precision
                    addParamValues(map, "erloes_drg", Lang.toDecimal( erloesValueLocal, 2) + " " + Lang.getCurrencySymbol());   // two decimal precision
                    addParamValues(map, "erloes_kis", Lang.toDecimal( erloesValueKis, 2) + " " + Lang.getCurrencySymbol());   // two decimal precision
                    // drg revenue
                    erloesValueLocal = caseDrg.getDrgRevenue(findDrgBaserateFeeValueLocal);
                    erloesValueKis = caseKis.getDrgRevenue(findDrgBaserateFeeValueKis);
                    addParamValues(map, "erloes_drg_dif", Lang.toDecimal((erloesValueKis - erloesValueLocal), 2) + " " + Lang.getCurrencySymbol());   // two decimal precision
                    addParamValues(map, "erloes_drg_cpx", Lang.toDecimal( erloesValueLocal, 2) + " " + Lang.getCurrencySymbol());   // two decimal precision
                    addParamValues(map, "erloes_drg_kis", Lang.toDecimal( erloesValueKis, 2) + " " + Lang.getCurrencySymbol());   // two decimal precision
                    // care revenue
                    erloesValueLocal = caseDrg.getCareRevenue(findDrgBaserateFeeValueLocal);
                    erloesValueKis = caseKis.getCareRevenue(findCareBaserateFeeValueKis);
                    addParamValues(map, "erloes_pflege_dif", Lang.toDecimal((erloesValueKis - erloesValueLocal), 2) + " " + Lang.getCurrencySymbol());   // two decimal precision
                    addParamValues(map, "erloes_pflege_cpx", Lang.toDecimal( erloesValueLocal, 2) + " " + Lang.getCurrencySymbol());   // two decimal precision
                    addParamValues(map, "erloes_pflege_kis", Lang.toDecimal( erloesValueKis, 2) + " " + Lang.getCurrencySymbol());   // two decimal precision

                } else {
                    LOG.log(Level.INFO, "DrgBaserateFeevalues (Local and/or Kis) are null..");
                }
                
            } else {
            // PEPP fall 
                TCasePepp caseCpx = groupingResultsLocal.getCasePepp(); // groupingResultsLocal.getCaseDrg();
                TCasePepp caseKis =  groupingResultsExtern.getCasePepp();
                GDRGModel grouper = CpxClientConfig.instance().getSelectedGrouper();
                Double valueCpx = localDetails != null ? processServiceBean.get().findSupplementaryFee(grouper, localDetails.id, SupplFeeTypeEn.ZP) : 0.0;
                Double valueKis = externDetails != null ? processServiceBean.get().findSupplementaryFee(grouper, externDetails.id, SupplFeeTypeEn.ZP) : 0.0;
                addParamValues(map, "zp_dif", Lang.toDecimal((valueKis - valueCpx), 2) + " " + Lang.getCurrencySymbol());
		addParamValues(map, "zp_cpx", Lang.toDecimal(valueCpx, 2) + " " + Lang.getCurrencySymbol());
                addParamValues(map, "zp_kis", Lang.toDecimal(valueKis, 2) + " " + Lang.getCurrencySymbol());
                valueCpx = localDetails != null ? processServiceBean.get().findSupplementaryFee(grouper, localDetails.id, SupplFeeTypeEn.ET) : 0.0;
                valueKis = externDetails != null ? processServiceBean.get().findSupplementaryFee(grouper, externDetails.id, SupplFeeTypeEn.ET) : 0.0;
                addParamValues(map, "et_dif", Lang.toDecimal((valueKis - valueCpx), 2) + " " + Lang.getCurrencySymbol());
		addParamValues(map, "et_cpx", Lang.toDecimal(valueCpx, 2) + " " + Lang.getCurrencySymbol());
                addParamValues(map, "et_kis", Lang.toDecimal(valueKis, 2) + " " + Lang.getCurrencySymbol());
                
                valueCpx = caseCpx.getPeppcCwEffectiv();
                valueKis = caseKis.getPeppcCwEffectiv();
                addParamValues(map, "fall_peppcw_kis", Lang.toDecimal(valueKis, 4));
                addParamValues(map, "fall_peppcw_cpx", Lang.toDecimal(valueCpx, 4));
                addParamValues(map, "fall_peppcw_dif", Lang.toDecimal(valueKis - valueCpx, 4));
               
                // days intensiv
                int cpx = caseCpx.getPeppcDaysIntensiv() == null?0:caseCpx.getPeppcDaysIntensiv();
                int kis = caseKis.getPeppcDaysIntensiv() == null?0:caseKis.getPeppcDaysIntensiv();
                addParamValues(map, "pepp_tage_intensiv_cpx", cpx);
                addParamValues(map, "pepp_tage_intensiv_kis", kis);
                addParamValues(map, "pepp_tage_intensiv_dif", kis - cpx);

                // personal care adults
                cpx = caseCpx.getPeppcDaysPerscareAdult()== null?0:caseCpx.getPeppcDaysPerscareAdult();
                kis = caseKis.getPeppcDaysPerscareAdult() == null?0:caseKis.getPeppcDaysPerscareAdult();
                addParamValues(map, "pepp_tage_einszueins_erv_cpx", cpx);
                addParamValues(map, "pepp_tage_einszueins_erv_kis", kis);
                addParamValues(map, "pepp_tage_einszueins_erv_dif", kis - cpx);
                
                cpx = caseCpx.getPeppcDaysPerscareInf() == null?0:caseCpx.getPeppcDaysPerscareInf();
                kis = caseKis.getPeppcDaysPerscareInf() == null?0:caseKis.getPeppcDaysPerscareInf();
                addParamValues(map, "pepp_tage_einszueins_kind_cpx", cpx);
                addParamValues(map, "pepp_tage_einszueins_kind_kis", kis);
                addParamValues(map, "pepp_tage_einszueins_kind_dif", kis - cpx);

                addParamValues(map, "pepp_verg_klasse_cpx", caseCpx.getPeppcPayClass() == null?0:caseCpx.getPeppcPayClass());
                addParamValues(map, "pepp_verg_klasse_kis", caseKis.getPeppcPayClass() == null?0:caseKis.getPeppcPayClass());
                
                // revenue
                valueCpx = caseCpx.getRevenue();
                valueKis = caseKis.getRevenue();
                addParamValues(map, "pepp_erloes_cpx", Lang.toDecimal(valueCpx, 2)+ " " + Lang.getCurrencySymbol());
                addParamValues(map, "pepp_erloes_kis", Lang.toDecimal(valueKis, 2) + " " + Lang.getCurrencySymbol());
                addParamValues(map, "pepp_erloes_dif", Lang.toDecimal((valueKis - valueCpx), 2) + " " + Lang.getCurrencySymbol());
            }

        } else {
            LOG.log(Level.INFO, "There are no Grouping Results (Local and/or Extern) for the caseNo: {0} and selected Grouper: {1} "
                    + "so, related Bookmarks doesn't filled...", new Object[]{cs.getCsCaseNumber(), selectedGrouper.name()});
        }


        addParamValues(map, "fall_beatmungsst_kis", externDetails == null ? null : externDetails.getCsdHmv());    // if local_fl is not set and actual_fl is set
        addParamValues(map, "fall_beatmungsst_drg", localDetails == null ? null : localDetails.getCsdHmv());  // if local_fl is set and actual_fl is set.
        addParamValues(map, "fall_prueferg_kis", groupingResultsExtern == null ? null : groupingResultsExtern.getGrpresCode()); // if local_fl is not set and actual_fl is set
        addParamValues(map, "fall_prueferg_drg", groupingResultsLocal == null ? null : groupingResultsLocal.getGrpresCode()); // if (both) local_fl & actual_fl are set.

        // fill Departments related Bookmarks with checkpoint (DRG) version.
        if (localDetails != null) {
            List<TCaseDepartment> caseDepartments = caseEJB.get().findTCaseDepartmentsForCaseDetailId(localDetails.id);
            if (caseDepartments != null && !caseDepartments.isEmpty()) {
                // which dept should consider, in case of more than one ??
                for (TCaseDepartment dep : caseDepartments) {
                    if (dep != null) {
                        String depShortName = dep.getDepKey301();
                        String depName = CpxDepartmentCatalog.instance().findDepNameByDepKey301(depShortName, AbstractCpxCatalog.DEFAULT_COUNTRY);
                        if (dep.getDepcIsAdmissionFl()) {
                            addParamValues(map, "fab_aufn_301", depShortName);
                            addParamValues(map, "fab_aufn", depName == null ? "" : depName);
                        }
                        if (dep.getDepcIsTreatingFl()) {
                            addParamValues(map, "fab_behandl_301", depShortName);
                            addParamValues(map, "fab_behandl", depName == null ? "" : depName);
                        }
                        if (dep.getDepcIsDischargeFl()) {
                            addParamValues(map, "fab_entl_301", depShortName);
                            addParamValues(map, "fab_entl_301_kopie", depShortName);//not found in Bookmark-wepapp
                            addParamValues(map, "fab_entl", depName == null ? "" : depName);
                            addParamValues(map, "fab_entl_kopie", depName == null ? "" : depName);//not found in Bookmark-wepapp
                        }
                    }
                }
            } else {
                LOG.log(Level.WARNING, "There is no any Department for the current local version. so, Departments related Bookmarks doesn't filled...");
            }
        }
    }

    private void fillHashMapProcess(final ProcessServiceFacade pFacade, final Map<String, String> map, final TWmProcess process) {
        if (process == null) {
            LOG.log(Level.SEVERE, "Process is null!");
            return;
        }

        // workflow (process number)
        if (process.getWorkflowNumber() != 0L) {
            addParamValues(map, "vorgangnr", process.getWorkflowNumber());
        } else {
            LOG.log(Level.SEVERE, "This Process has no Workflow Number (is either equals 0 or null)...");
        }

        if (process.getAssignedUser() != null) {
            CdbUsers processAssignedUser = pFacade.getUser(process.getAssignedUser());
            // Process informations
            if (processAssignedUser != null) {
                addParamValues(map, "bearbeitername", processAssignedUser.getULastName());
                addParamValues(map, "bearbeitername_unterschrift", processAssignedUser.getULastName());
                addParamValues(map, "bearbeitername_kopie", processAssignedUser.getULastName());
                addParamValues(map, "bearbeiter_titel", processAssignedUser.getUTitle());
                addParamValues(map, "bearbeiter_titel_unterschrift", processAssignedUser.getUTitle());
                addParamValues(map, "bearbeiter_titel_kopie", processAssignedUser.getUTitle());
                addParamValues(map, "bearbeitervorname", processAssignedUser.getUFirstName());
                addParamValues(map, "bearbeitervorname_unterschrift", processAssignedUser.getUFirstName());
                addParamValues(map, "bearbeitervorname_kopie", processAssignedUser.getUFirstName());
                addParamValues(map, "bearbeiter_telefon", processAssignedUser.getUPhoneNumber());
                addParamValues(map, "bearbeiter_telefon_kopie", processAssignedUser.getUPhoneNumber());
                addParamValues(map, "bearbeiter_email", processAssignedUser.getUEmailAddresse());
                addParamValues(map, "bearbeiter_email_kopie", processAssignedUser.getUEmailAddresse());
                
            } else {
                LOG.log(Level.WARNING, "Assigned User (Bearbeiter) is invalid: {0}", process.getAssignedUser());
            }
        } else {
            LOG.log(Level.WARNING, "There is no Assigned User (Bearbeiter) for this process, so related Bookmarks doesn't filled...");
        }
        addParamValues(map, "anfrage_dtm", process.getCreationDate());
        addParamValues(map, "anfrage_art", process.getWorkflowType());
        if (process.isProcessHospital()) {
            TWmProcessHospital processHospital = (TWmProcessHospital) process;
            if (processHospital.getProcessTopic() != null) {
                CWmListProcessTopic processTopic = MenuCache.getMenuCacheProcessTopics().get(processHospital.getProcessTopic());
                if (processTopic != null) {
                    addParamValues(map, "betreff_vorgang", processTopic.getWmPtName() != null ? processTopic.getWmPtName() : "");
                } else {
                    LOG.log(Level.WARNING, "There is no corresponding Process Topic for the ProcessTopicID: {0}", processHospital.getProcessTopic());
                }
            } else {
                LOG.log(Level.FINE, "There is no ProcessTopicID for this Process (of type Hospital)... ");
            }
        } else {
            LOG.log(Level.FINER, "This Process isn't an instance of ProcessHospital ...");
        }
        // Details about Leistungen (Case)
        // All processCases of a process? 
        if (process.getProcessCases() != null && !process.getProcessCases().isEmpty()) {
            String processCases = process.getProcessCases().stream().map(item -> item.getHosCase().getCsCaseNumber()).collect(Collectors.joining(","));
            addParamValues(map, "leistungen", processCases);
        }
    }

    private void fillHashMapCurrentUser(final Map<String, String> map) {
        /* --------------- Gather all required key-value pair and store it into the hashMap. ----------------------*/
        // General details
        Date currentDate = Calendar.getInstance().getTime();
        addParamValues(map, "current_date", currentDate);
        //Loggedin user (active session) information
        CdbUsers currentUserName = Session.instance().getCdbUser();
        if (currentUserName == null) {
            LOG.log(Level.SEVERE, "There is no user for the active client session, so CdbUsers related Bookmarks doesn't filled...");
            return;
        }
        addParamValues(map, "currentuser_name", currentUserName.getULastName());
        addParamValues(map, "currentuser_firstname", currentUserName.getUFirstName());
        addParamValues(map, "currentuser_email", currentUserName.getUEmailAddresse());
        addParamValues(map, "currentuser_telephone", currentUserName.getUPhoneNumber());
        addParamValues(map, "currentuser_fax", currentUserName.getUFaxNumber());
    }

    private void fillHashMapCustomerBookmarks(final Map<String, String> map) {
        // Add all customer-specific-bookmarks to the HashMap.
        List<CBookmarksCustomer> allBookmarksCustomerEntries = templateServiceBean.get().getAllBookmarksCustomerEntries();
        if (allBookmarksCustomerEntries == null || allBookmarksCustomerEntries.isEmpty()) {
            LOG.log(Level.WARNING, "There is no any Customer specific Bookmarks. so, BookmarksCustomer related Bookmarks doesn't filled...");
            return;
        }
        allBookmarksCustomerEntries.forEach((CBookmarksCustomer bc) -> {
            if (bc != null && bc.getId() != 0L) {
                addParamValues(map, bc.getBookmarkName(), bc.getBookmarkText());
            }
        });
    }

    private void fillHashMapLatestAction(final ProcessServiceFacade pFacade, final Map<String, String> map) {
        TWmAction latestAction = pFacade.getLastObsAction();

        if (latestAction == null) {
            LOG.log(Level.FINE, "There is no any Action for this Process ...");
            return;
        }
        // details about actions
        // Pna: 09.11.18 (fill Bookmark values with the latest Action)
        String actionSubjectName = MenuCache.instance().getActionSubjectName(latestAction.getActionType());
        addParamValues(map, "aufgaben", actionSubjectName == null ? "" : actionSubjectName);
        addParamValues(map, "aktion_typ", actionSubjectName == null ? "" : actionSubjectName);
        if (latestAction.getComment() != null && latestAction.getComment().length > 0) {
            String commentText = String.valueOf(latestAction.getComment());
            addParamValues(map, "aufgabe_text", commentText);
            addParamValues(map, "aktion_text", commentText);//flasch in wep-app
        } else {
            LOG.log(Level.FINE, "The latest Action with an ID: {0} has no Comment.. ", latestAction.getId());
        }
    }

    private void fillHashMapLatestRequests(final ProcessServiceFacade pFacade, final Map<String, String> map) {
        TWmRequest latestRequest = pFacade.getLastObsRequest();
        if (latestRequest == null) {
            LOG.log(Level.FINE, "There is no any Request for this Process ...");
            return;
        }
        switch (latestRequest.getRequestTypeEnum()) {
            case bege:
                TWmRequestBege bege = (TWmRequestBege) latestRequest;
                addParamValues(map, "adressat", bege.getBegeEditor());
                addParamValues(map, "adressat_kopie", bege.getBegeEditor());
                break;
            case mdk:
                TWmRequestMdk mdk = (TWmRequestMdk) latestRequest;
                addParamValues(map, "adressat", mdk.getMdkEditor());
                addParamValues(map, "adressat_kopie", mdk.getMdkEditor());
                break;
            case audit:
                TWmRequestAudit audit = (TWmRequestAudit) latestRequest;
                addParamValues(map, "adressat", audit.getEditor());
                addParamValues(map, "adressat_kopie", audit.getEditor());
                break;
            case insurance:
                TWmRequestInsurance insurance = (TWmRequestInsurance) latestRequest;
                addParamValues(map, "adressat", insurance.getEditor());
                addParamValues(map, "adressat_kopie", insurance.getEditor());
                break;
            case other:
                TWmRequestOther other = (TWmRequestOther) latestRequest;
                addParamValues(map, "adressat", other.getEditor());
                addParamValues(map, "adressat_kopie", other.getEditor());
                break;
            case review:
                TWmRequestReview review = (TWmRequestReview) latestRequest;
                addParamValues(map, "adressat", review.getInsEditor());
                addParamValues(map, "adressat_kopie", review.getInsEditor());
                break;
            default:
                LOG.log(Level.WARNING, "unknown request type: " + latestRequest.getRequestTypeEnum());
        }

        TWmRequestMdk latestMdkRequest = pFacade.getLastObsRequest(WmRequestTypeEn.mdk);
        TWmRequestBege latestBegeRequest = pFacade.getLastObsRequest(WmRequestTypeEn.bege);
        TWmRequestAudit latestAuditRequest = pFacade.getLastObsRequest(WmRequestTypeEn.audit);
        TWmRequestReview latestReviewRequest = pFacade.getLastObsRequest(WmRequestTypeEn.review);
        if (latestMdkRequest != null) {
            if (latestMdkRequest.getMdkInternalId() != null) {
                fillMdkInfos ( map, latestMdkRequest.getMdkInternalId(), "");
//                CMdk mdk = CpxMdkCatalog.instance().getByInternalId(latestMdkRequest.getMdkInternalId(), AbstractCpxCatalog.DEFAULT_COUNTRY);
//                if (mdk != null && mdk.getId() != 0L) {
//                    addParamValues(map, "anfrage_ik_mdk", mdk.getMdkIdent() != null ? mdk.getMdkIdent() : ""); // All mdk doesn't have IK and it's name
//                    addParamValues(map, "anfrage_ik_name_mdk", mdk.getMdkName());
//                    addParamValues(map, "anfrage_dienststelle_mdk", mdk.getMdkDepartment());
//                    addParamValues(map, "anfrage_telefon_mdk", mdk.getMdkPhone());
//                    addParamValues(map, "anfrage_strasse_mdk", mdk.getMdkStreet());
//                    addParamValues(map, "anfrage_ort_mdk", mdk.getMdkCity());
//                    addParamValues(map, "anfrage_vorwahl_mdk", mdk.getMdkPhonePrefix());
//                    addParamValues(map, "anfrage_fax_mdk", mdk.getMdkFax());
//                    addParamValues(map, "anfrage_plz_mdk", mdk.getMdkZipCode());
//                } else {
//                    LOG.log(Level.SEVERE, "There is no corresponding CMdk for the MDKInternalId: {0},"
//                            + " so MDK specific Bookmarks doesn't filled...", latestMdkRequest.getMdkInternalId());
//                }
            } else {
                LOG.log(Level.WARNING, "The latest MDK Request has either null or a negative MDKInternalId...");
            }
            addParamValues(map, "anfrage_art_mdk", WmRequestTypeEn.mdk); // kind of request
            addParamValues(map, "anfrage_aufnahme_mdk", latestMdkRequest.getMdkStartAudit());
            addParamValues(map, "anfrage_bearbeiter_mdk", latestMdkRequest.getMdkEditor());
            addParamValues(map, "anfrage_bemerkung_mdk", latestMdkRequest.getMdkComment());
            addParamValues(map, "anfrage_gutachten_mdk", latestMdkRequest.getReportDate());
            addParamValues(map, "anfrage_abschluss_mdk", latestMdkRequest.getMdkAuditCompletionDeadline());
            addParamValues(map, "anfrage_pruefgruende_mdk", latestMdkRequest.getMdkAuditReasons());
        } else {
            LOG.log(Level.INFO, "There is no any (latest) MDK Request for this Process, so MDK Request related Bookmarks"
                    + " doesn't filled...");
        }

        if (latestBegeRequest != null) {
            if (latestBegeRequest.getInsuranceIdentifier() != null) {
                 fillInsuranceCompanyInfos( map, latestBegeRequest.getInsuranceIdentifier(), "_bg");
//                CpxInsuranceCompany insComp = CpxInsuranceCompanyCatalog.instance().getByIdent(latestBegeRequest.getInsuranceIdentifier(), AbstractCpxCatalog.DEFAULT_COUNTRY);
//                if (insComp != null && insComp.getId() != 0L) {
//                    addParamValues(map, "anfrage_ik_bg", insComp.getInscIdent() != null ? insComp.getInscIdent() : "");
//                    addParamValues(map, "anfrage_ik_name_bg", insComp.getInscName());
//                    addParamValues(map, "anfrage_telefon_bg", insComp.getInscPhone());
//                    addParamValues(map, "anfrage_strasse_bg", insComp.getInscAddress());
//                    addParamValues(map, "anfrage_ort_bg", insComp.getInscCity());
//                    addParamValues(map, "anfrage_vorwahl_bg", insComp.getInscPhonePrefix());
//                    addParamValues(map, "anfrage_fax_bg", insComp.getInscFax());
//                    addParamValues(map, "anfrage_plz_bg", insComp.getInscZipCode());
//                } else {
//                    LOG.log(Level.SEVERE, "There is no corresponding InsuranceCompany for the InsuranceIdentifier: {0},"
//                            + " so InsuranceCompany specific Bookmarks doesn't filled...", latestBegeRequest.getInsuranceIdentifier());
//                }
            } else {
                LOG.log(Level.WARNING, "The latest BG Request has either null or a negative InsuranceIdentifier...");
            }
            addParamValues(map, "anfrage_art_bg", WmRequestTypeEn.bege); // kind of request
            addParamValues(map, "anfrage_bearbeiter_bg", latestBegeRequest.getBegeEditor());
            addParamValues(map, "anfrage_bemerkung_bg", latestBegeRequest.getBegeComment());
        } else {
            LOG.log(Level.INFO, "There is no any (latest) BG Request for this Process, so BG Request related Bookmarks"
                    + " doesn't filled...");
        }

        // remove/add additional Bookmarks related to Audit Req type
        if (latestAuditRequest != null) {
            if (latestAuditRequest.getInsuranceIdentifier() != null) {
                fillInsuranceCompanyInfos( map, latestAuditRequest.getInsuranceIdentifier(), "_pv");
//                CpxInsuranceCompany insComp = CpxInsuranceCompanyCatalog.instance().getByIdent(latestAuditRequest.getInsuranceIdentifier(), AbstractCpxCatalog.DEFAULT_COUNTRY);
//                if (insComp != null && insComp.getId() != 0L) {
//                    addParamValues(map, "anfrage_ik_pv", insComp.getInscIdent() != null ? insComp.getInscIdent() : ""); // All mdk doesn't have IK and it's name
//                    addParamValues(map, "anfrage_ik_name_pv", insComp.getInscName());
//                    addParamValues(map, "anfrage_telefon_pv", insComp.getInscPhone());
//                    addParamValues(map, "anfrage_strasse_pv", insComp.getInscAddress());
//                    addParamValues(map, "anfrage_ort_pv", insComp.getInscCity());
//                    addParamValues(map, "anfrage_vorwahl_pv", insComp.getInscPhonePrefix());
//                    addParamValues(map, "anfrage_fax_pv", insComp.getInscFax());
//                    addParamValues(map, "anfrage_plz_pv", insComp.getInscZipCode());
//                } else {
//                    LOG.log(Level.SEVERE, "There is no corresponding InsuranceCompany for the InsuranceIdentifier: {0},"
//                            + " so InsuranceCompany specific Bookmarks doesn't filled...", latestAuditRequest.getInsuranceIdentifier());
//                }
            } else {
                LOG.log(Level.WARNING, "The latest Audit Request has either null or a negative InsuranceIdentifier...");
            }
            addParamValues(map, "anfrage_art_pv", WmRequestTypeEn.audit); // kind of request
            addParamValues(map, "anfrage_bearbeiter_pv", latestAuditRequest.getEditor());
            addParamValues(map, "anfrage_bemerkung_pv", latestAuditRequest.getAuditComment());
            addParamValues(map, "anfrage_abschluss_pv", latestAuditRequest.getPreTrialEnd());
            addParamValues(map, "anfrage_pruefgruende_pv", latestAuditRequest.getMdkAuditReasons());

        } else {
            LOG.log(Level.INFO, "There is no any (latest) Audit Request for this Process, so Audit Request related Bookmarks"
                    + " doesn't filled...");
        }
        if(latestReviewRequest != null){

            if (latestReviewRequest.getInsIdentifier()!= null) {
                fillInsuranceCompanyInfos( map, latestReviewRequest.getInsIdentifier(), "_ev");
            } else {
                LOG.log(Level.WARNING, "The latest Review Request has no InsuranceIdentifier...");
            }
            if (latestReviewRequest.getMdInternalId() != null) {
                fillMdkInfos ( map, latestReviewRequest.getMdInternalId(), "_ev");
            }else {
                LOG.log(Level.WARNING, "The latest Review Request has no MD - Identifier...");
            }
            addParamValues(map, "anfrage_art_ev", WmRequestTypeEn.review); // kind of request
            addParamValues(map, "anfrage_pruefgruende_ev", latestReviewRequest.getMdkAuditReasons());
             addParamValues(map, "anfrage_bearbeiter_md_ev", latestReviewRequest.getMdEditor());
            addParamValues(map, "anfrage_bearbeiter_vers_ev", latestReviewRequest.getInsEditor());
            addParamValues(map, "anfrage_start_kasse_ev", latestReviewRequest.getStartAudit());
            addParamValues(map, "anfrage_start_md_ev", latestReviewRequest.getMdStartAudit());
            addParamValues(map, "anfrage_gutachten_datum_ev", latestReviewRequest.getReportDate());
            addParamValues(map, "anfrage_eingang_gutachten_ev", latestReviewRequest.getReportReceiveDate());
            addParamValues(map, "anfrage_bemerkung_ev", latestReviewRequest.getComment());
            addParamValues(map, "anfrage_frist_ev", latestReviewRequest.getReviewDeadline());
            addParamValues(map, "anfrage_frist_verlaengerung_ev", latestReviewRequest.getRenewalDeadline());
            addParamValues(map, "anfrage_eingeleitet_ev", latestReviewRequest.getReviewStart());
            addParamValues(map, "anfrage_antwort_frist_kk_ev", latestReviewRequest.getInsReplyDeadline());
            addParamValues(map, "anfrage_kk_antwort_ev", latestReviewRequest.getInsReplyDate());
            addParamValues(map, "anfrage_dok_nachsendefrist_ev", latestReviewRequest.getReplySendDocDeadline());
            addParamValues(map, "anfrage_dok_nachgesendet_ev", latestReviewRequest.getReplySendDocDate());
            addParamValues(map, "anfrage_abschlussfrist_ev", latestReviewRequest.getCompletionDeadline());
            addParamValues(map, "anfrage_abgeschossen_ev", latestReviewRequest.getCompletedDate());
        } else {
            LOG.log(Level.INFO, "There is no any (latest) Audit Request for this Process, so Audit Request related Bookmarks"
                    + " doesn't filled...");
        }
        
    }
    
    private void fillMdkInfos (final Map<String, String> map, Long pMdkId, String suffix){
                CMdk mdk = CpxMdkCatalog.instance().getByInternalId(pMdkId, AbstractCpxCatalog.DEFAULT_COUNTRY);
                if (mdk != null && mdk.getId() != 0L) {
                    addParamValues(map, "anfrage_ik_mdk" + suffix, mdk.getMdkIdent() != null ? mdk.getMdkIdent() : ""); // All mdk doesn't have IK and it's name
                    addParamValues(map, "anfrage_ik_name_mdk" + suffix, mdk.getMdkName());
                    addParamValues(map, "anfrage_dienststelle_mdk" + suffix, mdk.getMdkDepartment());
                    addParamValues(map, "anfrage_telefon_mdk" + suffix, mdk.getMdkPhone());
                    addParamValues(map, "anfrage_strasse_mdk" + suffix, mdk.getMdkStreet());
                    addParamValues(map, "anfrage_ort_mdk" + suffix, mdk.getMdkCity());
                    addParamValues(map, "anfrage_vorwahl_mdk" + suffix, mdk.getMdkPhonePrefix());
                    addParamValues(map, "anfrage_fax_mdk" + suffix, mdk.getMdkFax());
                    addParamValues(map, "anfrage_plz_mdk" + suffix, mdk.getMdkZipCode());
                } else {
                    LOG.log(Level.SEVERE, "There is no corresponding CMdk for the MDKInternalId: {0},"
                            + " so MDK specific Bookmarks doesn't filled...", pMdkId);
                }
       
    }
    
    private void fillInsuranceCompanyInfos(final Map<String, String> map, String pInsuranceIdent, String suffix){
                        CpxInsuranceCompany insComp = CpxInsuranceCompanyCatalog.instance().getByIdent(pInsuranceIdent, AbstractCpxCatalog.DEFAULT_COUNTRY);
                if (insComp != null && insComp.getId() != 0L) {
                    addParamValues(map, "anfrage_ik" + suffix, insComp.getInscIdent() != null ? insComp.getInscIdent() : ""); // All mdk doesn't have IK and it's name
                    addParamValues(map, "anfrage_ik_name" + suffix, insComp.getInscName());
                    addParamValues(map, "anfrage_telefon" + suffix, insComp.getInscPhone());
                    addParamValues(map, "anfrage_strasse" + suffix, insComp.getInscAddress());
                    addParamValues(map, "anfrage_ort" + suffix, insComp.getInscCity());
                    addParamValues(map, "anfrage_vorwahl" + suffix, insComp.getInscPhonePrefix());
                    addParamValues(map, "anfrage_fax" + suffix, insComp.getInscFax());
                    addParamValues(map, "anfrage_plz" + suffix, insComp.getInscZipCode());
                } else {
                    LOG.log(Level.SEVERE, "There is no corresponding InsuranceCompany for the InsuranceIdentifier: {0},"
                            + " so InsuranceCompany specific Bookmarks doesn't filled...", pInsuranceIdent);
                }

    }

    private String getSalutation(GenderEn genderEn) {
        if (genderEn == null) {
            return null;
        }
        return genderEn.getSalutationTranslation().getValue();

    }

    public static void addParamValues(Map<String, String> map, String key, String value) {
        if (value == null) {
            return;
        }
        if (!value.isEmpty()) {
            map.put(key, value);
        }
    }

    public static void addParamValues(Map<String, String> map, String key, Date value) {
        if (value == null) {
            return;
        }
        map.put(key, UtlDateTimeConverter.converter().formatToGermanDate(value, false));
    }

    public static void addParamValues(Map<String, String> map, String key, Number value) {
        if (value == null) {
            return;
        }
        map.put(key, String.valueOf(value));
    }

    public static void addParamValues(Map<String, String> map, String key, Enum<?> value) {
        if (value == null) {
            return;
        }
        map.put(key, String.valueOf(value));
    }

    public void showAllHashMapKeyvalues(Map<String, String> h) {
        if (h != null && !h.isEmpty()) {
            LOG.log(Level.FINEST, "\n \n ------- MAP IS FILLED WITH FOLLOWING KEYS AND VALUES-------- \n");
            h.forEach((String key, String value) -> {
                LOG.log(Level.FINEST, "{0} = {1}", new Object[]{key, value});
            });
        } else {
            LOG.log(Level.FINEST, "\n ----- MAP IS NULL OR EMPTY (CAN'T FILL ANY BOOKMARKS) ------ \n");
        }
    }

}
