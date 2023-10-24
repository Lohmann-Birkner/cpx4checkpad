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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.export;

import de.lb.cpx.model.enums.AdmissionCauseEn;
import de.lb.cpx.model.enums.AdmissionReason2En;
import de.lb.cpx.model.enums.AdmissionReasonEn;
import de.lb.cpx.model.enums.CaseStatusEn;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.CpxEnumInterface;
import de.lb.cpx.model.enums.DischargeReasonEn;
import de.lb.cpx.model.enums.GenderEn;
import de.lb.cpx.model.enums.GrouperMdcOrSkEn;
import de.lb.cpx.model.enums.PlaceOfRegEn;
import de.lb.cpx.model.enums.RuleTypeEn;
import de.lb.cpx.model.enums.SearchListTypeEn;
import de.lb.cpx.model.enums.WeekdayEn;
import de.lb.cpx.server.commonDB.dao.CDepartmentDao;
import de.lb.cpx.server.commonDB.dao.CInsuranceCompanyDao;
import de.lb.cpx.server.commonDB.dao.CMdkAuditreasonDao;
import de.lb.cpx.server.commonDB.dao.CMdkDao;
import de.lb.cpx.server.commonDB.dao.CWmListActionSubjectDao;
import de.lb.cpx.server.commonDB.dao.CWmListMdkStateDao;
import de.lb.cpx.server.commonDB.dao.CWmListProcessResultDao;
import de.lb.cpx.server.commonDB.dao.CWmListProcessTopicDao;
import de.lb.cpx.server.commonDB.dao.CWmListReminderSubjectDao;
import de.lb.cpx.server.commonDB.dao.CdbUsersDao;
import de.lb.cpx.server.commonDB.model.CMdk;
import de.lb.cpx.server.commonDB.model.CMdkAuditreason;
import de.lb.cpx.server.commonDB.model.CWmListActionSubject;
import de.lb.cpx.server.commonDB.model.CWmListMdkState;
import de.lb.cpx.server.commonDB.model.CWmListProcessResult;
import de.lb.cpx.server.commonDB.model.CWmListProcessTopic;
import de.lb.cpx.server.commonDB.model.CWmListReminderSubject;
import de.lb.cpx.shared.dto.QuotaListItemDTO;
import de.lb.cpx.shared.dto.RuleListItemDTO;
import de.lb.cpx.shared.dto.SearchItemDTO;
import de.lb.cpx.shared.dto.UserDTO;
import de.lb.cpx.shared.dto.WorkflowListItemDTO;
import de.lb.cpx.shared.dto.WorkingListItemDTO;
import de.lb.cpx.shared.filter.ColumnOption;
import de.lb.cpx.shared.filter.enums.QuotaListAttributes;
import de.lb.cpx.shared.filter.enums.RuleListAttributes;
import de.lb.cpx.shared.filter.enums.SearchListAttributes;
import de.lb.cpx.shared.filter.enums.WorkflowListAttributes;
import de.lb.cpx.shared.filter.enums.WorkingListAttributes;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;
import de.lb.cpx.wm.model.enums.WmRequestTypeEn;
import de.lb.cpx.wm.model.enums.WmWorkflowStateEn;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author niemeier
 */
@Stateless
public class SearchItemDTOMapper {

    private static final Logger LOG = Logger.getLogger(SearchItemDTOMapper.class.getName());

//    public static final String DELIMITER = ";";
//    public static final String DELIMITER_REPLACEMENT = ",";
    @Inject
    private CdbUsersDao usersDao;
    @Inject
    private CWmListReminderSubjectDao reminderSubjectDao;
    @Inject
    private CMdkDao mdkDao;
    @Inject
    private CWmListProcessTopicDao processTopicDao;
    @Inject
    private CWmListProcessResultDao processResultDao;
    @Inject
    private CWmListMdkStateDao mdkStateDao;
    @Inject
    private CInsuranceCompanyDao insuranceCompanyDao;
    @Inject
    private CDepartmentDao departmentDao;
    @Inject
    private CMdkAuditreasonDao mdkAutidReasonDao;
    @Inject
    private CWmListActionSubjectDao actionSubjectDao;
//    @EJB
//    private ProcessServiceBeanRemote processServiceBean;
    private Map<Long, UserDTO> users;
    private Map<Long, CWmListReminderSubject> reminderSubjects;
    private Map<Long, CMdk> mdks;
    private Map<Long, CWmListProcessTopic> processTopics;
    private Map<Long, CWmListProcessResult> processResults;
    private Map<Long, CWmListMdkState> mdkStates;
    private Map<Long, CMdkAuditreason> mdkAuditReason;
    private Map<String, String> insuranceCompanies;
    private Map<String, String> departments;
    private Map<String, String> insuranceCompaniesShort;
    private Map<Long, CWmListActionSubject> actionSubject;

    @PostConstruct
    private void init() {
        users = usersDao.getUsers();
        reminderSubjects = reminderSubjectDao.getReminderSubjects();    // uses Internal-Id of the reminder
        mdks = mdkDao.getAllMdks();    // uses Internal-Id of the mdk
//        reminderSubjects = reminderSubjectDao.getReminderSubjectsBasedOnId();    // uses Id of the reminder (WorkflowListItemDTO is set with reminder id: ---->  dto.setReminderId(toLong((Number) pItems.get("REMINDER_ID")));) in the WorkflowListSearchService.java
        processTopics = processTopicDao.getProcessTopics();
        mdkStates = mdkStateDao.getMdkStates();
        insuranceCompanies = insuranceCompanyDao.getAllInsuranceCompanies();
        insuranceCompaniesShort = insuranceCompanyDao.getAllInsuranceCompaniesShort();
        departments = departmentDao.getAllDepartments();
        processResults = processResultDao.getProcessResults();
        mdkAuditReason = mdkAutidReasonDao.getMdkAuditReasons();
        actionSubject = actionSubjectDao.getActionSubjects();
    }

//    private final String delimiter;
//    private final String delimiterReplacement;
//
//    public SearchItemDTOMapper(final String pDelimiter, final String pDelimiterReplacement) {
//        delimiter = pDelimiter == null ? "" : pDelimiter;
//        delimiterReplacement = pDelimiterReplacement == null ? "" : pDelimiterReplacement;
//    }
//    
//    public String getDelimiter() {
//        return delimiter;
//    }
//    
//    public String getDelimiterReplacement() {
//        return delimiterReplacement;
//    }
    public List<String> getWorkingListTitles(final Set<ColumnOption> pColumnOptions) {
        return getTitles(pColumnOptions, SearchListTypeEn.WORKING);
    }

    public List<String> getRuleListTitles(final Set<ColumnOption> pColumnOptions) {
        return getTitles(pColumnOptions, SearchListTypeEn.RULE);
    }
    
    public List<String> getQuotaListTitles(final Set<ColumnOption> pColumnOptions) {
        return getTitles(pColumnOptions, SearchListTypeEn.QUOTA);
    }

    public List<String> getWorkflowListTitles(final Set<ColumnOption> pColumnOptions) {
        return getTitles(pColumnOptions, SearchListTypeEn.WORKFLOW);
    }

    public List<String> getTitles(final Set<ColumnOption> pColumnOptions, final SearchListTypeEn pListTypeEn) {
        List<String> values = new ArrayList<>();
        if (pColumnOptions == null) {
            return values;
        }
        for (ColumnOption columnOption : pColumnOptions) {
//            if (columnOption.attributeName.equals(SearchListAttributes.lock)) {
//                values.add(columnOption.getDisplayName());
//                continue;
//            }
            final String value;
            Translation trans = columnOption.getTranslation(pListTypeEn);
            if (trans == null) {
                value = columnOption.getDisplayName();
            } else {
                if (trans.hasAbbreviation()) {
                    value = trans.getAbbreviation();
                } else {
                    value = trans.getValue();
                }
            }
//            String value = columnOption.getDisplayName(pListTypeEn); //.replace(DELIMITER, DELIMITER_REPLACEMENT);
//            if (pAttributes != null) {
//                SearchListAttribute attribute = pAttributes.get(columnOption.attributeName);
//                if (attribute != null) {
//                    Translation trans = Lang.get(attribute.getLanguageKey());
//                    if (trans != null) {
//                        if (trans.hasAbbreviation) {
//                            value = trans.getAbbreviation();
//                        } else {
//                            value = trans.getValue();
//                        }
//                    }
//                }
//            }
            values.add(value);
        }
        return values;
    }

    public List<Object> getWorkingListValues(final Set<ColumnOption> pColumnOptions, final WorkingListItemDTO pDto) {
        List<Object> values = new ArrayList<>();
        if (pColumnOptions == null || pDto == null) {
            return values;
        }
        for (ColumnOption columnOption : pColumnOptions) {
//                pOptions.forEach((columnOption, filterOption) -> {
//                    if (pVisibleColumns.contains(columnOption.attributeName)) {
            if (columnOption.attributeName.equals(WorkingListAttributes.csDrg)) {
                values.add(pDto.getCsDrg());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.csCreationDate)) {
                //Anlegedatum
                values.add(pDto.getCsCreationDate());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.csCaseNumber)) {
                //Fallnummer
                values.add(pDto.getCsCaseNumber());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.isCancel)) {
                //CancelFl
                values.add(pDto.isIsCancel());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.csCaseTypeEn)) {
                //Fallart
                CaseTypeEn caseType = CaseTypeEn.findByName(pDto.getCsCaseTypeEn());
                values.add(getEnumValue(caseType));
            } else if (columnOption.attributeName.equals(WorkingListAttributes.csStatusEn)) {
                //FS (Fallstatus)
                CaseStatusEn caseStatus = CaseStatusEn.findByName(pDto.getCsStatusEn());
                values.add(getEnumValue(caseStatus));
            } else if (columnOption.attributeName.equals(WorkingListAttributes.csdAdmissionDate)) {
                //Aufnahmedatum
                values.add(pDto.getCsdAdmissionDate());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.csdAdmissionDateWeekday)) {
                //Aufnahmedatum Wochentag
                //WeekdayEn weekDay = WeekdayEn.findById(pDto.getCsdAdmissionDateWeekday());
                //values.add(weekDay == null ? "" : weekDay.getTranslation().getAbbreviation());
                values.add(getWeekDay(pDto.getCsdAdmissionDateWeekday()));
            } else if (columnOption.attributeName.equals(WorkingListAttributes.csdAdmissionWeight)) {
                //Aufnahmegewicht
                values.add(pDto.getCsdAdmissionWeight());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.csdDischargeDate)) {
                //Entlassungsdatum
                values.add(pDto.getCsdDischargeDate());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.csdDischargeDateWeekday)) {
                //Entlassungsdatum Wochentag
                //WeekdayEn weekDay = WeekdayEn.findById(pDto.getCsdDischargeDateWeekday());
                //values.add(weekDay == null ? "" : weekDay.getTranslation().getAbbreviation());
                values.add(getWeekDay(pDto.getCsdDischargeDateWeekday()));
            } else if (columnOption.attributeName.equals(WorkingListAttributes.csdAgeYears)) {
                //Alter Jahre
                values.add(pDto.getCsdAgeYears());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.csdAgeDays)) {
                //Alter Tage
                values.add(pDto.getCsdAgeDays());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.csdHmv)) {
                //Beatmung
                values.add(pDto.getCsdHmv());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.csdLos)) {
                //VWD (Verweildauer)           
                values.add(pDto.getCsdLos());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.csdLeave)) {
                //Urlaubstage
                values.add(pDto.getCsdLeave());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.csdLosAlteration)) {
                //Length of Stay
                values.add(pDto.getCsdLosAlteration());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.csdAdmReason12En)) {
                //Aufnahmegrund
                AdmissionReasonEn admissionReason12 = AdmissionReasonEn.findById(pDto.getCsdAdmReason12En());
                values.add(getEnumValue(admissionReason12));
            } else if (columnOption.attributeName.equals(WorkingListAttributes.csdAdmReason34En)) {
                //Aufnahmegrund 02
                AdmissionReason2En admissionReason34 = AdmissionReason2En.findById(pDto.getCsdAdmReason34En());
                values.add(getEnumValue(admissionReason34));
            } else if (columnOption.attributeName.equals(WorkingListAttributes.csdAdmCauseEn)) {
                //Aufnahmeanlass
                AdmissionCauseEn admissionCause = AdmissionCauseEn.findByName(pDto.getCsdAdmCauseEn());
                values.add(getEnumValue(admissionCause));
            } else if (columnOption.attributeName.equals(WorkingListAttributes.csdDisReason12En)) {
                //Entlassungsgrund
                DischargeReasonEn dischargeReason12 = DischargeReasonEn.findById(pDto.getCsdDisReason12En());
                values.add(getEnumValue(dischargeReason12));
            } else if (columnOption.attributeName.equals(WorkingListAttributes.csHospitalIdent)) {
                //IKZ
                values.add(pDto.getCsHospitalIdent());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.patNumber)) {
                //Pat.-Nummer
                values.add(pDto.getPatNumber());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.icdCode)) { //ICDC_CODE
                //Haupt-ICD
                values.add(pDto.getIcdCode());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.patGenderEn)) {
                //Geschlecht
                GenderEn gender = GenderEn.findByName(pDto.getPatGenderEn());
                values.add(getEnumValue(gender));
            } else if (columnOption.attributeName.equals(WorkingListAttributes.patdZipcode)) {
                //Einzugsgebiet
                values.add(pDto.getPatdZipcode());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.insInsCompany)) {
                //?
                values.add(pDto.getInsInsCompany());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.insInsCompanyName)) {
                values.add(getInsuranceCompanyName(pDto.getInsInsCompanyName()));
            } else if (columnOption.attributeName.equals(WorkingListAttributes.insInsCompanyShortName)) {
                values.add(getInsuranceCompanyShortName(pDto.getInsInsCompanyShortName()));
            } else if (columnOption.attributeName.equals(WorkingListAttributes.countSd)) {
                //Summe ND
                values.add(pDto.getCountSd());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.countProc)) {
                //Summe Proz.
                values.add(pDto.getCountProc());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.string1)) {
                values.add(pDto.getString1());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.string2)) {
                values.add(pDto.getString2());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.string3)) {
                values.add(pDto.getString3());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.string4)) {
                values.add(pDto.getString4());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.string5)) {
                values.add(pDto.getString5());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.string6)) {
                values.add(pDto.getString6());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.string7)) {
                values.add(pDto.getString7());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.string8)) {
                values.add(pDto.getString8());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.string9)) {
                values.add(pDto.getString9());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.string10)) {
                values.add(pDto.getString10());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.numeric1)) {
                values.add(pDto.getNumeric1());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.numeric2)) {
                values.add(pDto.getNumeric2());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.numeric3)) {
                values.add(pDto.getNumeric3());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.numeric4)) {
                values.add(pDto.getNumeric4());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.numeric5)) {
                values.add(pDto.getNumeric5());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.numeric6)) {
                values.add(pDto.getNumeric6());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.numeric7)) {
                values.add(pDto.getNumeric7());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.numeric8)) {
                values.add(pDto.getNumeric8());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.numeric9)) {
                values.add(pDto.getNumeric9());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.numeric10)) {
                values.add(pDto.getNumeric10());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.cwPositive)) {
                values.add(pDto.getCwPositive());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.cwNegative)) {
                values.add(pDto.getCwNegative());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.cwEffective)) {
                values.add(pDto.getCwEffective());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.cwCatalog)) {
                values.add(pDto.getCwCatalog());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.grpresGroup)) {
                values.add(getMdc(pDto.getGrpresGroup()));
            } else if (columnOption.attributeName.equals(WorkingListAttributes.dCWmin)) {
                values.add(pDto.getDCWmin());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.dCWmax)) {
                values.add(pDto.getDCWmax());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.csdVersionNumber)) {
                values.add(pDto.getCsdVersionNumber());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.dCareCWmin)) {
                values.add(pDto.getDCareCWmin());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.dCareCWmax)) {
                values.add(pDto.getDCareCWmax());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.dRevenueMin)) {
                values.add(pDto.getDRevenueMin());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.dRevenueMax)) {
                values.add(pDto.getDRevenueMax());
            } else if (columnOption.attributeName.equals(WorkingListAttributes.careCw)) {
                values.add(pDto.getCareCw());
            }else if (columnOption.attributeName.equals(WorkingListAttributes.pccl)) {
                values.add(pDto.getPccl());
            }else if (columnOption.attributeName.equals(WorkingListAttributes.csBillingDate)) {
                values.add(pDto.getCsBillingDate());
            } else {
                if (!setCommonValues(values, columnOption, pDto)) {
                    LOG.log(Level.WARNING, "Unknown working list column option: {0}", columnOption.attributeName);
                    values.add("MISSING COLUMN: " + columnOption.attributeName);
                }
            }
        }
        return values;
    }

    public List<Object> getRuleListValues(final Set<ColumnOption> pColumnOptions, final RuleListItemDTO pDto) {
        List<Object> values = new ArrayList<>();
        if (pColumnOptions == null || pDto == null) {
            return values;
        }
        for (ColumnOption columnOption : pColumnOptions) {
//                pOptions.forEach((columnOption, filterOption) -> {
//                    if (pVisibleColumns.contains(columnOption.attributeName)) {
            if (columnOption.attributeName.equals(WorkingListAttributes.csDrg)) {
                values.add(pDto.getCsDrg());
            } else if (columnOption.attributeName.equals(RuleListAttributes.csCaseNumber)) {
                //Fallnummer
                values.add(pDto.getCsCaseNumber());
            } else if (columnOption.attributeName.equals(RuleListAttributes.chkCwSimulDiff)) {
                //Aufnahmedatum
                values.add(pDto.getChkCwSimulDiff());
            } else if (columnOption.attributeName.equals(RuleListAttributes.cwEffective)) {
                //Entlassungsdatum
                values.add(pDto.getCwEffective());
            } else if (columnOption.attributeName.equals(RuleListAttributes.csdLos)) {
                //Rechnungsdatum
                values.add(pDto.getCsdLos());
            } else if (columnOption.attributeName.equals(RuleListAttributes.csdHmv)) {
                //Rechnungsquartal
                values.add(pDto.getCsdHmv());
            } else if (columnOption.attributeName.equals(RuleListAttributes.csdAgeYears)) {
                values.add(pDto.getCsdAgeYears());
            } else if (columnOption.attributeName.equals(RuleListAttributes.csStatusEn)) {
                CaseStatusEn caseStatus = CaseStatusEn.findByName(pDto.getCsStatusEn());
                values.add(getEnumValue(caseStatus));
            } else if (columnOption.attributeName.equals(RuleListAttributes.csdAdmissionDate)) {
                values.add(pDto.getCsdAdmissionDate());
            } else if (columnOption.attributeName.equals(RuleListAttributes.ruleDescription)) {
                values.add(pDto.getRuleDescription());
            } else if (columnOption.attributeName.equals(RuleListAttributes.crgrCategory)) {
                values.add(pDto.getCrgrCategory());
            } else if (columnOption.attributeName.equals(RuleListAttributes.xRuleNumber)) {
                values.add(pDto.getXRuleNumber());
            } else if (columnOption.attributeName.equals(RuleListAttributes.ruleSuggestion)) { 
                values.add(pDto.getRuleSuggestion());
            } else if (columnOption.attributeName.equals(RuleListAttributes.typeText)) {
                RuleTypeEn ruleType = RuleTypeEn.findByInternalKey(pDto.getTypeText());
                values.add(getEnumValue(ruleType));
            } else if (columnOption.attributeName.equals(RuleListAttributes.ruleSelected)) {
                
                values.add(pDto.getRuleSelected());
            } else {
                if (!setCommonValues(values, columnOption, pDto)) {
                    LOG.log(Level.WARNING, "Unknown rule list column option: {0}", columnOption.attributeName);
                    values.add("MISSING COLUMN: " + columnOption.attributeName);
                }
            }
        }
        return values;
    }

    public List<Object> getQuotaListValues(final Set<ColumnOption> pColumnOptions, final QuotaListItemDTO pDto) {
        List<Object> values = new ArrayList<>();
        if (pColumnOptions == null || pDto == null) {
            return values;
        }
        for (ColumnOption columnOption : pColumnOptions) {
//                pOptions.forEach((columnOption, filterOption) -> {
//                    if (pVisibleColumns.contains(columnOption.attributeName)) {
            if (columnOption.attributeName.equals(WorkingListAttributes.csDrg)) {
                values.add(pDto.getCsDrg());
            } else if (columnOption.attributeName.equals(QuotaListAttributes.csCaseNumber)) {
                //Fallnummer
                values.add(pDto.getCsCaseNumber());
            } else if (columnOption.attributeName.equals(QuotaListAttributes.csdAdmissionDate)) {
                //Aufnahmedatum
                values.add(pDto.getCsdAdmissionDate());
            } else if (columnOption.attributeName.equals(QuotaListAttributes.csdDischargeDate)) {
                //Entlassungsdatum
                values.add(pDto.getCsdDischargeDate());
            } else if (columnOption.attributeName.equals(QuotaListAttributes.csBillingDate)) {
                //Rechnungsdatum
                values.add(pDto.getCsBillingDate());
            } else if (columnOption.attributeName.equals(QuotaListAttributes.csBillingQuarter)) {
                //Rechnungsquartal
                values.add(pDto.getCsBillingQuarter());
            } else if (columnOption.attributeName.equals(QuotaListAttributes.riskPercentTotal)) {
                //Risiko in %
                values.add(pDto.getRiskPercentTotal());
            } else if (columnOption.attributeName.equals(QuotaListAttributes.riskValueTotal)) {
                //Risiko in EUR
                values.add(pDto.getRiskValueTotal());
            } else if (columnOption.attributeName.equals(QuotaListAttributes.riskPlaceOfReg)) {
                //Ort der (Risiko-)Erfassung
                PlaceOfRegEn placeOfReg = PlaceOfRegEn.findByName(pDto.getRiskPlaceOfReg());
                values.add(getEnumValue(placeOfReg));
            } else if (columnOption.attributeName.equals(QuotaListAttributes.csdAdmReason12En)) {
                //Aufnahmegrund
                AdmissionReasonEn admissionReason12 = AdmissionReasonEn.findById(pDto.getCsdAdmReason12En());
                values.add(getEnumValue(admissionReason12));
            } else if (columnOption.attributeName.equals(QuotaListAttributes.csHospitalIdent)) {
                //IKZ
                values.add(pDto.getCsHospitalIdent());
            } else if (columnOption.attributeName.equals(QuotaListAttributes.patNumber)) {
                //Pat.-Nummer
                values.add(pDto.getPatNumber());
            } else if (columnOption.attributeName.equals(QuotaListAttributes.icdCode)) { //ICDC_CODE
                //Haupt-ICD
                values.add(pDto.getIcdCode());
            } else if (columnOption.attributeName.equals(QuotaListAttributes.insInsCompany)) {
                //?
                values.add(pDto.getInsInsCompany());
            } else if (columnOption.attributeName.equals(QuotaListAttributes.insInsCompanyName)) {
                values.add(getInsuranceCompanyName(pDto.getInsInsCompanyName()));
            } else if (columnOption.attributeName.equals(QuotaListAttributes.insInsCompanyShortName)) {
                values.add(getInsuranceCompanyShortName(pDto.getInsInsCompanyShortName()));
            } else if (columnOption.attributeName.equals(QuotaListAttributes.wmState)) {
                WmWorkflowStateEn workflowState = WmWorkflowStateEn.findById(pDto.getWmState());
                values.add(getEnumValue(workflowState));
            } else if (columnOption.attributeName.equals(QuotaListAttributes.processTopic)) {
                values.add(getProcessTopic(pDto.getProcessTopic()));
            } else if (columnOption.attributeName.equals(QuotaListAttributes.processResult)) {
                String processResult = getProcessResult(pDto.getProcessResult());
                values.add(processResult);
            } else if (columnOption.attributeName.equals(QuotaListAttributes.startAudit)) {
                values.add(pDto.getStartAudit());
            } else if (columnOption.attributeName.equals(QuotaListAttributes.mdkStartAuditExtendedDate)) {
                values.add(pDto.getMdkStartAuditExtendedDate());
            } else if (columnOption.attributeName.equals(QuotaListAttributes.mdkStartAuditQuarter)) {
                values.add(pDto.getMdkStartAuditQuarter());
            } else if (columnOption.attributeName.equals(QuotaListAttributes.requestState)) {
                values.add(getMdkState(pDto.getRequestState()));
            } else if (columnOption.attributeName.equals(QuotaListAttributes.reportDate)) {
                values.add(pDto.getReportDate());
            } else if (columnOption.attributeName.equals(QuotaListAttributes.mdkReportCreationQuarter)) {
                values.add(pDto.getMdkReportCreationQuarter());
            } else if (columnOption.attributeName.equals(QuotaListAttributes.workflowNumber)) {
                values.add(pDto.getWorkflowNumber());
            } else if (columnOption.attributeName.equals(QuotaListAttributes.mdk)) {
                String mdk = getMdk(pDto.getMdk());
                values.add(mdk);
            } else if (columnOption.attributeName.equals(QuotaListAttributes.insuranceRecivedBillDate)) {
                values.add(pDto.getInsuranceRecivedBillDate());
            } else {
                if (!setCommonValues(values, columnOption, pDto)) {
                    LOG.log(Level.WARNING, "Unknown working list column option: {0}", columnOption.attributeName);
                    values.add("MISSING COLUMN: " + columnOption.attributeName);
                }
            }
        }
        return values;
    }

    public List<Object> getWorkflowListValues(final Set<ColumnOption> pColumnOptions, final WorkflowListItemDTO pDto) {
        List<Object> values = new ArrayList<>();
        for (ColumnOption columnOption : pColumnOptions) {
            if (columnOption.attributeName.equals(WorkflowListAttributes.creationDate)) {
                //Anlegedatum
                values.add(pDto.getCreationDate());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.workflowNumber)) {
                values.add(pDto.getWorkflowNumber());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.wmState)) {
                //WmWorkflowStateEn wmState = WmWorkflowStateEn.findByName(pDto.getWmState());
                //values.add(getEnumValue(wmState));
                WmWorkflowStateEn workflowState = WmWorkflowStateEn.findById(pDto.getWmState());
                values.add(getEnumValue(workflowState));
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.patNumber)) {
                //Pat.-Nummer
                values.add(pDto.getPatNumber());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.csHospitalIdent)) {
                //IKZ
                values.add(pDto.getCsHospitalIdent());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.csCaseNumber)) {
                //Fallnummer
                values.add(pDto.getCsCaseNumber());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.csdAdmissionDate)) {
                //Aufnahmedatum
                values.add(pDto.getCsdAdmissionDate());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.csdDischargeDate)) {
                //Entlassungsdatum
                values.add(pDto.getCsdDischargeDate());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.assSubject)) {
                //values.add(pDto.getAssSubject(toLong(number));
                values.add(getReminderSubject(pDto.getAssSubject()));
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.assSender)) {
                //Wiedervorlage (Ersteller)           
                values.add(getUserName(pDto.getAssSender()));
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.assReceiver)) {
                //Wiedervorlage (Empfänger)
                values.add(getUserName(pDto.getAssReceiver()));
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.assLastModificationDate)) {
                values.add(pDto.getAssLastModificationDate());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.insInsCompany)) {
                values.add(pDto.getInsInsCompany());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.insInsCompanyName)) {
                values.add(getInsuranceCompanyName(pDto.getInsInsCompanyName()));
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.insInsCompanyShortName)) {
                values.add(getInsuranceCompanyShortName(pDto.getInsInsCompanyShortName()));
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.wvPrio)) {
                values.add(pDto.getWvPrio());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.requestStartAudit)) {
                values.add(pDto.getRequestStartAudit());
            }else if (columnOption.attributeName.equals(WorkflowListAttributes.requestReportDate)) {
                values.add(pDto.getRequestReportDate());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.remLatestCreationDate)) {
                values.add(pDto.getRemLatestCreationDate());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.vmModUser)) {
                values.add(getUserName(pDto.getVmModUser()));
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.assUser)) {
                values.add(getUserName(pDto.getAssUser()));
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.remFinished)) {
                values.add(getReminderStatus(pDto.getRemFinished()));
//                values.add(pDto.getRemFinished());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.csdAdmCauseEn)) {
                AdmissionCauseEn admissionCause = AdmissionCauseEn.findByName(pDto.getCsdAdmCauseEn());
                values.add(getEnumValue(admissionCause));
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.billCorrectionDeadline)) {
                values.add(pDto.getBillCorrectionDeadline());
                //values.add(pDto.getBillCorrectionDeadlineDays()));
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.mdkAuditCompletionDeadline)) {
                values.add(pDto.getMdkAuditCompletionDeadline());
                //values.add(pDto.getMdkAuditCompletionDeadlineDays()));
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.mdkDocumentDeliverDeadline)) {
                values.add(pDto.getMdkDocumentDeliverDeadline());
                //values.add(pDto.getMdkDocumentDeliverDeadlineDays()));
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.preliminaryProceedingAnswerDeadline)) {
                values.add(pDto.getPreliminaryProceedingAnswerDeadline());
                //values.add(pDto.getPreliminaryProceedingAnswerDeadlineDays()));
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.preliminaryProceedingsClosedDeadline)) {
                values.add(pDto.getPreliminaryProceedingsClosedDeadline());
                //values.add(pDto.getPreliminaryProceedingsClosedDeadlineDays()));
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.dataRecordCorrectionDeadline)) {
                values.add(pDto.getDataRecordCorrectionDeadline());
                //values.add(pDto.getDataRecordCorrectionDeadlineDays()));
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.billCorrectionDeadlineDays)) {
                //values.add(pDto.getBillCorrectionDeadline()));
                values.add(pDto.getBillCorrectionDeadlineDays());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.mdkAuditCompletionDeadlineDays)) {
                //values.add(pDto.getMdkAuditCompletionDeadline()));
                values.add(pDto.getMdkAuditCompletionDeadlineDays());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.mdkDocumentDeliverDeadlineDays)) {
                //values.add(pDto.getMdkDocumentDeliverDeadline()));
                values.add(pDto.getMdkDocumentDeliverDeadlineDays());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.preliminaryProceedingAnswerDeadlineDays)) {
                //values.add(pDto.getPreliminaryProceedingAnswerDeadline()));
                values.add(pDto.getPreliminaryProceedingAnswerDeadlineDays());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.preliminaryProceedingsClosedDeadlineDays)) {
                //values.add(pDto.getPreliminaryProceedingsClosedDeadline()));
                values.add(pDto.getPreliminaryProceedingsClosedDeadlineDays());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.dataRecordCorrectionDeadlineDays)) {
                //values.add(pDto.getDataRecordCorrectionDeadline()));
                values.add(pDto.getDataRecordCorrectionDeadlineDays());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.processTopic)) {
                values.add(getProcessTopic(pDto.getProcessTopic()));
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.processResult)) {
                String processResult = getProcessResult(pDto.getProcessResult());
                values.add(processResult);
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.closingDate)) {
                values.add(pDto.getClosingDate());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.requestType)) {
                WmRequestTypeEn requestType = WmRequestTypeEn.findById(pDto.getRequestType());
                values.add(getEnumValue(requestType));
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.wvComment)) {
                values.add(pDto.getWvComment());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.actionComment)) {
                values.add(pDto.getActionComment()); //.replace(DELIMITER, DELIMITER_REPLACEMENT)
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.drgFinal)) {
                values.add(pDto.getDrgFinal());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.drgInitial)) {
                values.add(pDto.getDrgInitial());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.cwInitial)) {
                values.add(pDto.getCwInitial());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.cwDiff)) {
                values.add(pDto.getCwDiff());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.cwFinal)) {
                values.add(pDto.getCwFinal());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.supFeeInitial)) {
                values.add(pDto.getSupFeeInitial());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.supFeeDiff)) {
                values.add(pDto.getSupFeeDiff());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.supFeeFinal)) {
                values.add(pDto.getSupFeeFinal());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.losInitial)) {
                values.add(pDto.getLosInitial());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.losDiff)) {
                values.add(pDto.getLosDiff());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.losFinal)) {
                values.add(pDto.getLosFinal());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.caseType)) {
                //Abrechnungsart 
                CaseTypeEn caseType = CaseTypeEn.findByName(pDto.getCaseType());
                values.add(getEnumValue(caseType));
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.revenueDiff)) {
                values.add(pDto.getRevenueDiff());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.requestState)) {
                values.add(getMdkState(pDto.getMdkState()));
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.mdkSubseqProcDate)) {
                values.add(pDto.getMdkSubseqProcDate());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.courtFileNumber)) {
                values.add(pDto.getCourtFileNumber());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.lawerFileNumber)) {
                values.add(pDto.getLawerFileNumber());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.insuranceRecivedBillDate)) {
                values.add(pDto.getInsuranceRecivedBillDate());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.auditNames)) {
                values.add(getAuditReasinSubjects(pDto.getAuditNames()));
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.isCancel)) {
                values.add(pDto.isIsCancel());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.resultDelta)) {
                values.add(pDto.getResultDelta());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.savedMoney)) {
                values.add(pDto.getSavedMoney());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.cwCareInitial)) {
                values.add(pDto.getCwCareInitial());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.cwCareFinal)) {
                values.add(pDto.getCwCareFinal());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.cwCareDiff)) {
                values.add(pDto.getCwCareDiff());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.latestActionSubject)) {
                values.add(getActionSubject(pDto.getLatestActionSubject()));
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.revenueInit)) {
                values.add(pDto.getRevenueInit());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.revenueFinal)) {
                values.add(pDto.getRevenueFinal());
                
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.reviewDeadline)) {
                values.add(pDto.getReviewDeadline());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.reviewRenewalDeadline)) {
                values.add(pDto.getReviewRenewalDeadline());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.reviewInsReplyDeadline)) {
                values.add(pDto.getReviewInsReplyDeadline());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.reviewReplySendDocDeadline)) {
                values.add(pDto.getReviewReplySendDocDeadline());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.reviewCompletionDeadline)) {
                values.add(pDto.getReviewCompletionDeadline());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.reviewDeadlineDays)) {
                values.add(pDto.getReviewDeadlineDays());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.reviewRenewalDeadlineDays)) {
                values.add(pDto.getReviewRenewalDeadlineDays());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.reviewInsReplyDeadlineDays)) {
                values.add(pDto.getReviewInsReplyDeadlineDays());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.reviewReplySendDocDeadlineDays)) {
                values.add(pDto.getReviewReplySendDocDeadlineDays());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.reviewCompletionDeadlineDays)) {
                values.add(pDto.getReviewCompletionDeadlineDays());
            } else if (columnOption.attributeName.equals(WorkflowListAttributes.penaltyFee)) {
                values.add(pDto.getPenaltyFee());
            }else  if (columnOption.attributeName.equals(WorkflowListAttributes.kainKeyEn)) {
                values.add(pDto.getKainKeyEn());
            }else  if (columnOption.attributeName.equals(WorkflowListAttributes.inkaKeyEn)) {
                values.add(pDto.getInkaKeyEn());
            } else if(columnOption.attributeName.equals(WorkflowListAttributes.dateOfBenefitLawDecision)){
                values.add(pDto.getDateOfBenefitLawDecision());
            }else if(columnOption.attributeName.equals(WorkflowListAttributes.mdHosStartAudit)){
                values.add(pDto.getMdHosStartAudit());
            }
            else {
                if (!setCommonValues(values, columnOption, pDto)) {
                    LOG.log(Level.WARNING, "Unknown workflow list column option: {0}", columnOption.attributeName);
                    values.add("MISSING COLUMN: " + columnOption.attributeName);
                }
            }
        }
        return values;
    }

    protected boolean setCommonValues(List<Object> values, final ColumnOption columnOption, final SearchItemDTO pDto) {
        if (columnOption.attributeName.equals(WorkingListAttributes.rowNum)) {
            values.add(pDto.getRowNum());
            return true;
        } else if (columnOption.attributeName.equals(WorkingListAttributes.patDateOfBirth)) {
            //Geburtsdatum
            values.add(pDto.getPatDateOfBirth());
            return true;
        } else if (columnOption.attributeName.equals(SearchListAttributes.patName)) {
            //Pat.-Nummer
            values.add(pDto.getPatName());
            return true;
        } else if (columnOption.attributeName.equals(SearchListAttributes.insNumber)) {
            values.add(pDto.getInsNumber());
            return true;
        } else if (columnOption.attributeName.equals(SearchListAttributes.lock)) {
//            values.add(pDto.getLockSince()));
//            values.add(pDto.getLockExpires()));
//            values.add(pDto.getLockUserName()));
            values.add(pDto.getLock());
            return true;
        } else if (columnOption.attributeName.equals(SearchListAttributes.depDischarge)) {
            values.add(pDto.getDepDischarge());
            return true;
        } else if (columnOption.attributeName.equals(SearchListAttributes.depDischarge301)) {
            values.add(pDto.getDepDischarge301());
            return true;
        } //            else if (columnOption.attributeName.equals(SearchListAttributes.depDischarge3Name)) {
        //                values.add(getDepartmentName(pDto.getdepDischarge3Name()));
        //                return true;
        //            } 
        else if (columnOption.attributeName.equals(SearchListAttributes.depAdmission)) {
            values.add(pDto.getDepAdmission());
            return true;
        } else if (columnOption.attributeName.equals(SearchListAttributes.depAdmission301)) {
            values.add(pDto.getDepAdmission301());
            return true;
        } else if (columnOption.attributeName.equals(SearchListAttributes.depTreating)) {
            values.add(pDto.getDepTreating());
            return true;
        } else if (columnOption.attributeName.equals(SearchListAttributes.depTreating301)) {
            values.add(pDto.getDepTreating301());
            return true;
        }
        return false;
    }

    protected static String getWeekDay(final Integer pWeekDay) {
        if (pWeekDay == null) {
            return "";
        }
        WeekdayEn weekDay = WeekdayEn.findById(pWeekDay);
        if (weekDay == null) {
            return pWeekDay + "";
        }
        return weekDay.getTranslation().getAbbreviation();
    }

    protected static String getMdc(final String pMdc) {
        if (pMdc == null) {
            return "";
        }
        GrouperMdcOrSkEn grpresGroup = GrouperMdcOrSkEn.findByName(pMdc);
        if (grpresGroup == null) {
            return pMdc;
        }
        return grpresGroup.getTranslation().getAbbreviation();
    }

    protected String getProcessResult(final Long pResultId) {
        if (pResultId == null) {
            return "";
        }
        CWmListProcessResult obj = processResults.get(pResultId);
        String name = obj == null ? null : obj.getName();
        if (name == null) {
            return pResultId + "";
        }
        return name; //.replace(DELIMITER, DELIMITER_REPLACEMENT);
    }

    protected String getMdk(final Long pMdkId) {
        if (pMdkId == null) {
            return "";
        }
        CMdk obj = mdks.get(pMdkId);
        String name = obj == null ? null : obj.getMdkName();
        if (name == null) {
            return pMdkId + "";
        }
        return name; //.replace(DELIMITER, DELIMITER_REPLACEMENT);
    }

    protected String getInsuranceCompanyName(final String pIdent) {
        if (pIdent == null) {
            return "";
        }
        String name = insuranceCompanies.get(pIdent);
        if (name == null) {
            return pIdent;
        }
        return name; //.replace(DELIMITER, DELIMITER_REPLACEMENT);
    }

    protected String getInsuranceCompanyShortName(final String pIdent) {
        if (pIdent == null) {
            return "";
        }
        String obj = insuranceCompaniesShort.get(pIdent);
        return obj;
//        String name = obj == null ? null : obj.getId();
//        if (name == null) {
//            return pIdent;
//        }
//        return name; //.replace(DELIMITER, DELIMITER_REPLACEMENT);
    }

    protected String getDepartmentName(final String pDepKey301) {
        if (pDepKey301 == null) {
            return "";
        }
        String name = departments.get(pDepKey301);
        if (name == null) {
            return pDepKey301;
        }
        return name; //.replace(DELIMITER, DELIMITER_REPLACEMENT);
    }

//    protected String getInsuranceCompanyName(final String pIdent) {
//        if (pIdent == null) {
//            return "";
//        }
//        String ident = pIdent.trim();
//        String name = insuranceCompanies.get(ident);
//        if (name == null) {
//            name = insuranceCompanyDao.getInsuCompName(ident, CountryEn.de.name());
//            if (name != null) {
//                insuranceCompanies.put(ident, name);
//            } else {
//                return ident;
//            }
//        }
//        return name;
//    }
    protected String getUserName(final Long pUserId) {
        if (pUserId == null) {
            return "";
        }
        UserDTO user = users.get(pUserId);
        if (user == null) {
            return pUserId + "";
        }
        return user.getUserName(); //.replace(DELIMITER, DELIMITER_REPLACEMENT);
    }

    protected String getMdkState(final Long pStateId) {
        if (pStateId == null) {
            return "";
        }
        CWmListMdkState obj = mdkStates.get(pStateId);
        String state = obj == null ? null : obj.getName();
        if (state == null) {
            return pStateId + "";
        }
        return state; //.replace(DELIMITER, DELIMITER_REPLACEMENT);
    }

    protected String getReminderStatus(final Boolean reminderStatus) {
        if (reminderStatus == null) {
            return "";
        } else if (reminderStatus) {
//                    return  "geschlossen";
            return Lang.getProcessStatusReminderClosed();
        } else {
//                    return "offen";
            return Lang.getProcessStatusReminderOpen();
        }
    }

    protected String getReminderSubject(final Long pReminderId) {
        if (pReminderId == null) {
            return "";
        }
        CWmListReminderSubject obj = reminderSubjects.get(pReminderId);
        String subject = obj == null ? null : obj.getName();
        if (subject == null) {
            return pReminderId + "";
        }
        return subject; //.replace(DELIMITER, DELIMITER_REPLACEMENT);
    }

    protected String getProcessTopic(final Long pTopicId) {
        if (pTopicId == null) {
            return "";
        }
        CWmListProcessTopic obj = processTopics.get(pTopicId);
        String topic = obj == null ? null : obj.getName();
        if (topic == null) {
            return pTopicId + "";
        }
        return topic; //.replace(DELIMITER, DELIMITER_REPLACEMENT);
    }

    protected static String getEnumValue(final CpxEnumInterface<?> pValue) {
        if (pValue == null) {
            return "";
        }
        return pValue.getTranslation().getValue();
    }

    protected String getAuditReasinSubjects(final String pAuditReasonIds) {
        String reAuditreasons = "";

        if (pAuditReasonIds == null) {
            return "";
        }
        
        String[]arAuditReasons = pAuditReasonIds.split(",");
        if (arAuditReasons != null && arAuditReasons.length > 0) {
            for (int i = 0, n = arAuditReasons.length; i < n; i++) {
                String auditReason =  arAuditReasons[i].trim();
                if(auditReason != null) {
                    Long auditreasonId = 0L;
                    try {
                        auditreasonId = Long.valueOf(auditReason);
                    } catch (NumberFormatException ex) {
                        LOG.log(Level.SEVERE, "Invalid ID found in AuditReasons auditReason: " + auditReason, ex);
                        continue;
                    }
                   if(auditreasonId != null) {
                       CMdkAuditreason obj = mdkAuditReason.get(auditreasonId);
                       String subject = obj == null ? null : obj.getName();
                       if(i > 0) {
                        reAuditreasons += ", ";
                       }
                       if (subject == null) {
                           reAuditreasons += auditreasonId + "";
                       } else {
                           reAuditreasons += subject;
                       }

                   }
               }
            }
        }
        return reAuditreasons; //.replace(DELIMITER, DELIMITER_REPLACEMENT);
    }

    protected String getActionSubject(final Long pSubjectId) {
        if (pSubjectId == null) {
            return "";
        }
        CWmListActionSubject obj = actionSubject.get(pSubjectId);
        String state = obj == null ? null : obj.getName();
        if (state == null) {
            return pSubjectId + "";
        }
        return state; //.replace(DELIMITER, DELIMITER_REPLACEMENT);
    }
    
}
