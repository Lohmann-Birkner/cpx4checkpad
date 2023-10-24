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
package de.lb.cpx.model;

//import de.lb.cpx.model.converter.CsCaseTypeConverter;
import de.lb.cpx.model.converter.AcgAgeBandConverter;
import de.lb.cpx.model.enums.AcgAgeBandEn;
import de.lb.cpx.model.enums.AcgIndexTypeEn;
import de.lb.cpx.model.enums.GenderEn;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TCase initially generated at 21.01.2016 17:07:59 by Hibernate Tools 3.2.2.GA
 * <p style="font-size:1em; color:green;"> Die Tabelle "T_CASE" speichert alle
 * Krankenhausfälle, die in die Datenbank eingelesen wurden. </p>
 */
@Entity
@Table(name = "T_ACG_DATA")
@SuppressWarnings("serial")
public class TAcgData extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    private TAcgDataInfo acgDataInfo;
    private TPatient patient;
    private int age;
    private GenderEn sex;
    private String icdCode;
    private String lineOfBusiness;
    private String company;
    private String product;
    private String employerGroupId;
    private String employerGroupName;
    private String benefitPlan;
    private String healthSystem;
    private String pcpId;
    private String pcpName;
    private String pcpGroupId;
    private String pcpGroupName;
    private boolean pregnant;
    private boolean delivered;
    private boolean lowBirthweight;
    private String pharmacyCost;
    private String totalCost;
    private String inpatientHospCount;
    private String inpatientHospDays;
    private String emergencyVisitCount;
    private String outpatientVisitCount;
    private String dialysisService;
    private String nursingService;
    private String majorProcedure;
    private String cancerTreatment;
    private String careManagementProgram;
    private Date dateOfBirth;
    private String psychotherapyService;
    private String mechanicalVentilation;
    private String calSsa;
    private String allCauseInpatientHospCount;
    private String unplannedInpatientHospCount;
    private String readmission30DayCount;
    private String unplannedReadm30DayCount;
    private String observationStay;
    private String observationDayCount;
    private AcgAgeBandEn ageBand;
    private String untruncatedTotalCost;
    private String pharmacyCostBand;
    private String totalCostBand;
    private int acgCode;
    private int resourceUtilizationBand;
    private BigDecimal unscaledAcgConcurrentRisk;
    private BigDecimal rescaledAcgConcurrentRisk;
    private BigDecimal localAcgConcurrentRisk;
    private BigDecimal unscaledConcurrentRisk;
    private BigDecimal rescaledConcurrentRisk;
    private BigDecimal localAgeGendConcurrentRisk;
    private BigDecimal unscaledTotalCostPredRisk;
    private BigDecimal rescaledTotalCostPredRisk;
    private String predictedTotalCostRange;
    private BigDecimal rankProbHighTotalCost;
    private BigDecimal referenceProbHighTotalCost;
    private BigDecimal unscaledPharmCostPredRisk;
    private BigDecimal rescaledPharmCostPredRisk;
    private String predictedPharmacyCostRange;
    private BigDecimal rankProbHighPharmacyCost;
    private BigDecimal referenceProbHighPharmCost;
    private boolean highRiskUnexpPharmacyCost;
    private BigDecimal probUnexpPharmacyCost;
    private BigDecimal probPersistentHighUser;
    private int majorAdgCount;
    private int hospDominantMorbidityTypes;
    private Boolean pregnancyWithoutDelivery;
    private int chronicConditionCount;
    private int diagnosesUsed;
    private boolean frailtyFlag;
    private String frailtyConcepts;
    private int frailtyConceptCount;
    private String concomitantOpioidUser;
    private String chronicOpioidUser;
    private boolean opioidDependent;
    private String adgCodes;
    private String adgVector;
    private String edcCodes;
    private String medcCodes;
    private String rxmgCodes;
    private String medicalRxmgCodes;
    private String pharmacyRxmgCodes;
    private String majorRxmgCodes;
    private String labMarkers;
    private int activeIngredientCount;
    private BigDecimal probIpHosp;
    private BigDecimal probIpHosp6Mos;
    private BigDecimal probIcuHosp;
    private BigDecimal probInjuryHosp;
    private BigDecimal probExtendedHosp;
    private BigDecimal probUnplanned30DayReadm;
    private AcgIndexTypeEn ageRelMacularDegCondition;
    private AcgIndexTypeEn bipolarDisorderCondition;
    private String bipolarDisorderRxGaps;
    private String bipolarDisorderMpr;
    private String bipolarDisorderCsa;
    private String bipolarDisorderUntreatedRx;
    private AcgIndexTypeEn congestiveHeartFlrCondition;
    private String congestiveHeartFlrRxGaps;
    private String congestiveHeartFlrMpr;
    private String congestiveHeartFlrCsa;
    private String congestiveHeartFlrUntrRx;
    private AcgIndexTypeEn depressionCondition;
    private String depressionRxGaps;
    private String depressionMpr;
    private String depressionCsa;
    private String depressionUntreatedRx;
    private AcgIndexTypeEn diabetesCondition;
    private String diabetesRxGaps;
    private String diabetesMpr;
    private String diabetesCsa;
    private String diabetesUntreatedRx;
    private String diabetesLab;
    private AcgIndexTypeEn glaucomaCondition;
    private String glaucomaRxGaps;
    private String glaucomaMpr;
    private String glaucomaCsa;
    private String glaucomaUntreatedRx;
    private AcgIndexTypeEn humanImdefVirusCondition;
    private String humanImdefVirusRxGaps;
    private String humanImdefVirusMpr;
    private String humanImdefVirusCsa;
    private String humanImdefVirusUntreatedRx;
    private AcgIndexTypeEn disordersOfLipidMCondition;
    private String disordersOfLipidMRxGaps;
    private String disordersOfLipidMtblsmMpr;
    private String disordersOfLipidMtblsmCsa;
    private String disordersOfLipidMUntrRx;
    private String disordersOfLipidMtblsmLab;
    private AcgIndexTypeEn hypertensionCondition;
    private String hypertensionRxGaps;
    private String hypertensionMpr;
    private String hypertensionCsa;
    private String hypertensionUntreatedRx;
    private AcgIndexTypeEn hypothyroidismCondition;
    private String hypothyroidismRxGaps;
    private String hypothyroidismMpr;
    private String hypothyroidismCsa;
    private String hypothyroidismUntreatedRx;
    private AcgIndexTypeEn immunoSupprTransCondition;
    private String immunoSupprTransRxGaps;
    private String immunoSupprTransMpr;
    private String immunoSupprTransCsa;
    private String immunoSupprTransUntrRx;
    private AcgIndexTypeEn ischemicHeartDisCondition;
    private String ischemicHeartDiseaseRxGaps;
    private String ischemicHeartDiseaseMpr;
    private String ischemicHeartDiseaseCsa;
    private String ischemicHeartDisUntrRx;
    private AcgIndexTypeEn osteoporosisCondition;
    private String osteoporosisRxGaps;
    private String osteoporosisMpr;
    private String osteoporosisCsa;
    private String osteoporosisUntreatedRx;
    private AcgIndexTypeEn parkinsonsDiseaseCondition;
    private String parkinsonsDiseaseRxGaps;
    private String parkinsonsDiseaseMpr;
    private String parkinsonsDiseaseCsa;
    private String parkinsonsDisUntreatedRx;
    private AcgIndexTypeEn persistentAsthmaCondition;
    private String persistentAsthmaRxGaps;
    private String persistentAsthmaMpr;
    private String persistentAsthmaCsa;
    private String persistentAsthmaUntreatedRx;
    private AcgIndexTypeEn rheumatoidArthritisCondition;
    private String rheumatoidArthritisRxGaps;
    private String rheumatoidArthritisMpr;
    private String rheumatoidArthritisCsa;
    private String rheumatoidArthritisUntrRx;
    private AcgIndexTypeEn schizophreniaCondition;
    private String schizophreniaRxGaps;
    private String schizophreniaMpr;
    private String schizophreniaCsa;
    private String schizophreniaUntreatedRx;
    private AcgIndexTypeEn seizureDisordersCondition;
    private String seizureDisordersRxGaps;
    private String seizureDisordersMpr;
    private String seizureDisordersCsa;
    private String seizureDisordersUntreatedRx;
    private AcgIndexTypeEn chronicObstPulDisCondition;
    private AcgIndexTypeEn chronicRenalFlrCondition;
    private String chronicRenalFailureLab;
    private AcgIndexTypeEn lowBackPainCondition;
    private AcgIndexTypeEn deficiencyAnemiaCondition;
    private String deficiencyAnemiaLab;
    private String totalRxGaps;
    private BigDecimal majoritySrcOfCarePercent;
    private String majoritySrcOfCareProviders;
    private Integer uniqueProviderCount;
    private Integer specialtyCount;
    private Boolean generalistSeen;
    private Integer generalistVisitCount;
    private Integer managementVisitCount;
    private String coordinationRisk;
    private BigDecimal careDensityRatio;
    private String careDensityQuantile;
    private String careDensBottomQuartCutoff;
    private String careDensTopQuartCutoff;
    private String careDensityCostSavingRatio;
    private String careDensityCostSavingRange;
    private String hhsMarket;
    private String hhsRatingArea;
    private String hhsRiskPool;
    private String hhsMetalLevel;
    private String hhsCsrIndicator;
    private String hhsModel;
    private String hhsExcludeFromRiskAdjust;
    private String hhsCcCodes;
    private String hhsHccCodes;
    private String hhsRiskScore;
    private String hhsCsrAdjustedRiskScore;
    private String hhsMemberMonths;
    private String hhsBillableMemberMonths;
    private String hhsTotalMemberMonths;
    private String hhsTotalBillableMemMonths;
    private String cmsFactorType;
    private String cmsMedicaidEligible;
    private String cmsOrigRsnForEntitlement;
    private String cmsLtiStatus;
    private String cmsPaceStatus;
    private String cmsModel;
    private String cmsSubModel;
    private String cmsExcludeFromRiskAdjust;
    private String cmsCcCodes;
    private String cmsHccCodes;
    private String cmsRiskScore;
    private String cmsMemberMonths;
    private String cmsTotalMemberMonths;
    private String warningCodes;
    private BigDecimal referenceRescaledWeight; //reference_rescaled_weight(?!) ->   Relativgewicht Patient					
    private BigDecimal referenceRescaledWeightAgeGroup; //calulcated field ->  Relativgewicht Altersgruppe				
    private BigDecimal resourceUtilizationBandAgeGroup; //calulcated field ->  Ressourcenverbrauchsgruppe Altersgruppe
    private BigDecimal chronicConditionCountAgeGroup; //calculated value -> Chronische Erkrankungen Altersgruppe	 

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_ACG_DATA_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "T_ACG_DATA_INFO_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_ACG_DATA4ACG_DATA_INFO"))
    //@JsonBackReference(value = "labor")
    public TAcgDataInfo getAcgDataInfo() {
        return this.acgDataInfo;
    }

    public void setAcgDataInfo(final TAcgDataInfo acgDataInfo) {
        this.acgDataInfo = acgDataInfo;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "T_PATIENT_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_ACG_DATA4T_PATIENT_ID"))
    //@JsonBackReference(value = "labor")
    public TPatient getPatient() {
        return this.patient;
    }

    public void setPatient(final TPatient patient) {
        this.patient = patient;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_OF_BIRTH", length = 7)
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Column(name = "AGE", nullable = false)
    public int getAge() {
        return age;
    }

    public void setAge(int acgAge) {
        this.age = acgAge;
    }

    @Column(name = "SEX", length = 1, nullable = false)
    @Enumerated(EnumType.STRING)
    public GenderEn getSex() {
        return sex;
    }

    public void setSex(GenderEn sex) {
        this.sex = sex;
    }

    @Lob
    @Column(name = "ICD_CODE", length = 6000, nullable = true)
    public String getIcdCode() {
        return icdCode;
    }

    public void setIcdCode(String icdCode) {
        this.icdCode = icdCode;
    }

     @Column(name = "LINE_OF_BUSINESS", nullable = true)
    public String getLineOfBusiness() {
        return lineOfBusiness;
    }

    public void setLineOfBusiness(String lineOfBusiness) {
        this.lineOfBusiness = lineOfBusiness;
    }

    @Column(name = "COMPANY", nullable = true)
    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Column(name = "PRODUCT", nullable = true)
    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    @Column(name = "EMPLOYER_GROUP_ID", nullable = true)
    public String getEmployerGroupId() {
        return employerGroupId;
    }

    public void setEmployerGroupId(String employerGroupId) {
        this.employerGroupId = employerGroupId;
    }

    @Column(name = "EMPLOYER_GROUP_NAME", nullable = true)
    public String getEmployerGroupName() {
        return employerGroupName;
    }

    public void setEmployerGroupName(String employerGroupName) {
        this.employerGroupName = employerGroupName;
    }

    @Column(name = "BENEFIT_PLAN", nullable = true)
    public String getBenefitPlan() {
        return benefitPlan;
    }

    public void setBenefitPlan(String benefitPlan) {
        this.benefitPlan = benefitPlan;
    }

    @Column(name = "HEALTH_SYSTEM", nullable = true)
    public String getHealthSystem() {
        return healthSystem;
    }

    public void setHealthSystem(String healthSystem) {
        this.healthSystem = healthSystem;
    }

    @Column(name = "PCP_ID", nullable = true)
    public String getPcpId() {
        return pcpId;
    }

    public void setPcpId(String pcpId) {
        this.pcpId = pcpId;
    }

    @Column(name = "PCP_NAME", nullable = true)
    public String getPcpName() {
        return pcpName;
    }

    public void setPcpName(String pcpName) {
        this.pcpName = pcpName;
    }

    @Column(name = "PCP_GROUP_ID", nullable = true)
    public String getPcpGroupId() {
        return pcpGroupId;
    }

    public void setPcpGroupId(String pcpGroupId) {
        this.pcpGroupId = pcpGroupId;
    }

    @Column(name = "PCP_GROUP_NAME", nullable = true)
    public String getPcpGroupName() {
        return pcpGroupName;
    }

    public void setPcpGroupName(String pcpGroupName) {
        this.pcpGroupName = pcpGroupName;
    }

    @Column(name = "PREGNANT", nullable = false)
    public boolean getPregnant() {
        return pregnant;
    }

    public void setPregnant(boolean pregnant) {
        this.pregnant = pregnant;
    }

    @Column(name = "DELIVERED", nullable = false)
    public boolean getDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    @Column(name = "LOW_BIRTHWEIGHT", nullable = false)
    public boolean getLowBirthweight() {
        return lowBirthweight;
    }

    public void setLowBirthweight(boolean lowBirthweight) {
        this.lowBirthweight = lowBirthweight;
    }

    @Column(name = "PHARMACY_COST", nullable = true)
    public String getPharmacyCost() {
        return pharmacyCost;
    }

    public void setPharmacyCost(String pharmacyCost) {
        this.pharmacyCost = pharmacyCost;
    }

    @Column(name = "TOTAL_COST", nullable = true)
    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    @Column(name = "INPATIENT_HOSP_COUNT", nullable = true)
    public String getInpatientHospCount() {
        return inpatientHospCount;
    }

    public void setInpatientHospCount(String inpatientHospCount) {
        this.inpatientHospCount = inpatientHospCount;
    }

    @Column(name = "INPATIENT_HOSP_DAYS", nullable = true)
    public String getInpatientHospDays() {
        return inpatientHospDays;
    }

    public void setInpatientHospDays(String inpatientHospDays) {
        this.inpatientHospDays = inpatientHospDays;
    }

    @Column(name = "EMERGENCY_VISIT_COUNT", nullable = true)
    public String getEmergencyVisitCount() {
        return emergencyVisitCount;
    }

    public void setEmergencyVisitCount(String emergencyVisitCount) {
        this.emergencyVisitCount = emergencyVisitCount;
    }

    @Column(name = "OUTPATIENT_VISIT_COUNT", nullable = true)
    public String getOutpatientVisitCount() {
        return outpatientVisitCount;
    }

    public void setOutpatientVisitCount(String outpatientVisitCount) {
        this.outpatientVisitCount = outpatientVisitCount;
    }

    @Column(name = "DIALYSIS_SERVICE", nullable = true)
    public String getDialysisService() {
        return dialysisService;
    }

    public void setDialysisService(String dialysisService) {
        this.dialysisService = dialysisService;
    }

    @Column(name = "NURSING_SERVICE", nullable = true)
    public String getNursingService() {
        return nursingService;
    }

    public void setNursingService(String nursingService) {
        this.nursingService = nursingService;
    }

    @Column(name = "MAJOR_PROCEDURE", nullable = true)
    public String getMajorProcedure() {
        return majorProcedure;
    }

    public void setMajorProcedure(String majorProcedure) {
        this.majorProcedure = majorProcedure;
    }

    @Column(name = "CANCER_TREATMENT", nullable = true)
    public String getCancerTreatment() {
        return cancerTreatment;
    }

    public void setCancerTreatment(String cancerTreatment) {
        this.cancerTreatment = cancerTreatment;
    }

    @Column(name = "CARE_MANAGEMENT_PROGRAM", nullable = true)
    public String getCareManagementProgram() {
        return careManagementProgram;
    }

    public void setCareManagementProgram(String careManagementProgram) {
        this.careManagementProgram = careManagementProgram;
    }

    @Column(name = "PSYCHOTHERAPY_SERVICE", nullable = true)
    public String getPsychotherapyService() {
        return psychotherapyService;
    }

    public void setPsychotherapyService(String psychotherapyService) {
        this.psychotherapyService = psychotherapyService;
    }

    @Column(name = "MECHANICAL_VENTILATION", nullable = true)
    public String getMechanicalVentilation() {
        return mechanicalVentilation;
    }

    public void setMechanicalVentilation(String mechanicalVentilation) {
        this.mechanicalVentilation = mechanicalVentilation;
    }

    @Column(name = "CAL_SSA", nullable = true)
    public String getCalSsa() {
        return calSsa;
    }

    public void setCalSsa(String calSsa) {
        this.calSsa = calSsa;
    }

    @Column(name = "ALL_CAUSE_INPATIENT_HOSP_COUNT", nullable = true)
    public String getAllCauseInpatientHospCount() {
        return allCauseInpatientHospCount;
    }

    public void setAllCauseInpatientHospCount(String allCauseInpatientHospCount) {
        this.allCauseInpatientHospCount = allCauseInpatientHospCount;
    }

    @Column(name = "UNPLANNED_INPATIENT_HOSP_COUNT", nullable = true)
    public String getUnplannedInpatientHospCount() {
        return unplannedInpatientHospCount;
    }

    public void setUnplannedInpatientHospCount(String unplannedInpatientHospCount) {
        this.unplannedInpatientHospCount = unplannedInpatientHospCount;
    }

    @Column(name = "READMISSION_30_DAY_COUNT", nullable = true)
    public String getReadmission30DayCount() {
        return readmission30DayCount;
    }

    public void setReadmission30DayCount(String readmission30DayCount) {
        this.readmission30DayCount = readmission30DayCount;
    }

    @Column(name = "UNPLANNED_READM_30_DAY_COUNT", nullable = true)
    public String getUnplannedReadm30DayCount() {
        return unplannedReadm30DayCount;
    }

    public void setUnplannedReadm30DayCount(String unplannedReadm30DayCount) {
        this.unplannedReadm30DayCount = unplannedReadm30DayCount;
    }

    @Column(name = "OBSERVATION_STAY", nullable = true)
    public String getObservationStay() {
        return observationStay;
    }

    public void setObservationStay(String observationStay) {
        this.observationStay = observationStay;
    }

    @Column(name = "OBSERVATION_DAY_COUNT", nullable = true)
    public String getObservationDayCount() {
        return observationDayCount;
    }

    public void setObservationDayCount(String observationDayCount) {
        this.observationDayCount = observationDayCount;
    }

    @Column(name = "AGE_BAND", length = 6, nullable = false)
    @Convert(converter = AcgAgeBandConverter.class)
    public AcgAgeBandEn getAgeBand() {
        return ageBand;
    }

    public void setAgeBand(AcgAgeBandEn ageBand) {
        this.ageBand = ageBand;
    }

    @Column(name = "UNTRUNCATED_TOTAL_COST", nullable = true)
    public String getUntruncatedTotalCost() {
        return untruncatedTotalCost;
    }

    public void setUntruncatedTotalCost(String untruncatedTotalCost) {
        this.untruncatedTotalCost = untruncatedTotalCost;
    }

    @Column(name = "PHARMACY_COST_BAND", nullable = true)
    public String getPharmacyCostBand() {
        return pharmacyCostBand;
    }

    public void setPharmacyCostBand(String pharmacyCostBand) {
        this.pharmacyCostBand = pharmacyCostBand;
    }

    @Column(name = "TOTAL_COST_BAND", nullable = true)
    public String getTotalCostBand() {
        return totalCostBand;
    }

    public void setTotalCostBand(String totalCostBand) {
        this.totalCostBand = totalCostBand;
    }

    @Column(name = "ACG_CODE", nullable = false)
    public int getAcgCode() {
        return acgCode;
    }

    public void setAcgCode(int acgCode) {
        this.acgCode = acgCode;
    }

    @Column(name = "RESOURCE_UTILIZATION_BAND", nullable = false)
    public int getResourceUtilizationBand() {
        return resourceUtilizationBand;
    }

    public void setResourceUtilizationBand(int resourceUtilizationBand) {
        this.resourceUtilizationBand = resourceUtilizationBand;
    }

    @Column(name = "UNSCALED_ACG_CONCURRENT_RISK", precision = 10, scale = 5, nullable = false)
    public BigDecimal getUnscaledAcgConcurrentRisk() {
        return unscaledAcgConcurrentRisk;
    }

    public void setUnscaledAcgConcurrentRisk(BigDecimal unscaledAcgConcurrentRisk) {
        this.unscaledAcgConcurrentRisk = unscaledAcgConcurrentRisk;
    }

    @Column(name = "RESCALED_ACG_CONCURRENT_RISK", precision = 38, scale = 9, nullable = false)
    public BigDecimal getRescaledAcgConcurrentRisk() {
        return rescaledAcgConcurrentRisk;
    }

    public void setRescaledAcgConcurrentRisk(BigDecimal rescaledAcgConcurrentRisk) {
        this.rescaledAcgConcurrentRisk = rescaledAcgConcurrentRisk;
    }

    @Column(name = "LOCAL_ACG_CONCURRENT_RISK", precision = 38, scale = 9, nullable = false)
    public BigDecimal getLocalAcgConcurrentRisk() {
        return localAcgConcurrentRisk;
    }

    public void setLocalAcgConcurrentRisk(BigDecimal localAcgConcurrentRisk) {
        this.localAcgConcurrentRisk = localAcgConcurrentRisk;
    }

    @Column(name = "UNSCALED_CONCURRENT_RISK", precision = 38, scale = 9, nullable = false)
    public BigDecimal getUnscaledConcurrentRisk() {
        return unscaledConcurrentRisk;
    }

    public void setUnscaledConcurrentRisk(BigDecimal unscaledConcurrentRisk) {
        this.unscaledConcurrentRisk = unscaledConcurrentRisk;
    }

    @Column(name = "RESCALED_CONCURRENT_RISK", precision = 38, scale = 9, nullable = false)
    public BigDecimal getRescaledConcurrentRisk() {
        return rescaledConcurrentRisk;
    }

    public void setRescaledConcurrentRisk(BigDecimal rescaledConcurrentRisk) {
        this.rescaledConcurrentRisk = rescaledConcurrentRisk;
    }

    @Column(name = "LOCAL_AGE_GEND_CONCURRENT_RISK", precision = 38, scale = 9, nullable = false)
    public BigDecimal getLocalAgeGendConcurrentRisk() {
        return localAgeGendConcurrentRisk;
    }

    public void setLocalAgeGendConcurrentRisk(BigDecimal localAgeGendConcurrentRisk) {
        this.localAgeGendConcurrentRisk = localAgeGendConcurrentRisk;
    }

    @Column(name = "UNSCALED_TOTAL_COST_PRED_RISK", precision = 38, scale = 9, nullable = false)
    public BigDecimal getUnscaledTotalCostPredRisk() {
        return unscaledTotalCostPredRisk;
    }

    public void setUnscaledTotalCostPredRisk(BigDecimal unscaledTotalCostPredRisk) {
        this.unscaledTotalCostPredRisk = unscaledTotalCostPredRisk;
    }

    @Column(name = "RESCALED_TOTAL_COST_PRED_RISK", precision = 38, scale = 9, nullable = false)
    public BigDecimal getRescaledTotalCostPredRisk() {
        return rescaledTotalCostPredRisk;
    }

    public void setRescaledTotalCostPredRisk(BigDecimal rescaledTotalCostPredRisk) {
        this.rescaledTotalCostPredRisk = rescaledTotalCostPredRisk;
    }

    @Column(name = "PREDICTED_TOTAL_COST_RANGE", nullable = true)
    public String getPredictedTotalCostRange() {
        return predictedTotalCostRange;
    }

    public void setPredictedTotalCostRange(String predictedTotalCostRange) {
        this.predictedTotalCostRange = predictedTotalCostRange;
    }

    @Column(name = "RANK_PROB_HIGH_TOTAL_COST", precision = 3, scale = 2, nullable = false)
    public BigDecimal getRankProbHighTotalCost() {
        return rankProbHighTotalCost;
    }

    public void setRankProbHighTotalCost(BigDecimal rankProbHighTotalCost) {
        this.rankProbHighTotalCost = rankProbHighTotalCost;
    }

    @Column(name = "REFERENCE_PROB_HIGH_TOTAL_COST", precision = 38, scale = 9, nullable = false)
    public BigDecimal getReferenceProbHighTotalCost() {
        return referenceProbHighTotalCost;
    }

    public void setReferenceProbHighTotalCost(BigDecimal referenceProbHighTotalCost) {
        this.referenceProbHighTotalCost = referenceProbHighTotalCost;
    }

    @Column(name = "UNSCALED_PHARM_COST_PRED_RISK", precision = 38, scale = 9, nullable = false)
    public BigDecimal getUnscaledPharmCostPredRisk() {
        return unscaledPharmCostPredRisk;
    }

    public void setUnscaledPharmCostPredRisk(BigDecimal unscaledPharmCostPredRisk) {
        this.unscaledPharmCostPredRisk = unscaledPharmCostPredRisk;
    }

    @Column(name = "RESCALED_PHARM_COST_PRED_RISK", precision = 38, scale = 9, nullable = false)
    public BigDecimal getRescaledPharmCostPredRisk() {
        return rescaledPharmCostPredRisk;
    }

    public void setRescaledPharmCostPredRisk(BigDecimal rescaledPharmCostPredRisk) {
        this.rescaledPharmCostPredRisk = rescaledPharmCostPredRisk;
    }

    @Column(name = "PREDICTED_PHARMACY_COST_RANGE", nullable = true)
    public String getPredictedPharmacyCostRange() {
        return predictedPharmacyCostRange;
    }

    public void setPredictedPharmacyCostRange(String predictedPharmacyCostRange) {
        this.predictedPharmacyCostRange = predictedPharmacyCostRange;
    }

    @Column(name = "RANK_PROB_HIGH_PHARMACY_COST", precision = 3, scale = 2, nullable = false)
    public BigDecimal getRankProbHighPharmacyCost() {
        return rankProbHighPharmacyCost;
    }

    public void setRankProbHighPharmacyCost(BigDecimal rankProbHighPharmacyCost) {
        this.rankProbHighPharmacyCost = rankProbHighPharmacyCost;
    }

    @Column(name = "REFERENCE_PROB_HIGH_PHARM_COST", precision = 38, scale = 9, nullable = false)
    public BigDecimal getReferenceProbHighPharmCost() {
        return referenceProbHighPharmCost;
    }

    public void setReferenceProbHighPharmCost(BigDecimal referenceProbHighPharmCost) {
        this.referenceProbHighPharmCost = referenceProbHighPharmCost;
    }

    @Column(name = "HIGH_RISK_UNEXP_PHARMACY_COST", nullable = false)
    public boolean isHighRiskUnexpPharmacyCost() {
        return highRiskUnexpPharmacyCost;
    }

    public void setHighRiskUnexpPharmacyCost(boolean highRiskUnexpPharmacyCost) {
        this.highRiskUnexpPharmacyCost = highRiskUnexpPharmacyCost;
    }

    @Column(name = "PROB_UNEXP_PHARMACY_COST", precision = 38, scale = 9, nullable = false)
    public BigDecimal getProbUnexpPharmacyCost() {
        return probUnexpPharmacyCost;
    }

    public void setProbUnexpPharmacyCost(BigDecimal probUnexpPharmacyCost) {
        this.probUnexpPharmacyCost = probUnexpPharmacyCost;
    }

    @Column(name = "PROB_PERSISTENT_HIGH_USER", precision = 38, scale = 9, nullable = false)
    public BigDecimal getProbPersistentHighUser() {
        return probPersistentHighUser;
    }

    public void setProbPersistentHighUser(BigDecimal probPersistentHighUser) {
        this.probPersistentHighUser = probPersistentHighUser;
    }

    @Column(name = "MAJOR_ADG_COUNT", nullable = false)
    public int getMajorAdgCount() {
        return majorAdgCount;
    }

    public void setMajorAdgCount(int majorAdgCount) {
        this.majorAdgCount = majorAdgCount;
    }

    @Column(name = "HOSP_DOMINANT_MORBIDITY_TYPES", nullable = false)
    public int getHospDominantMorbidityTypes() {
        return hospDominantMorbidityTypes;
    }

    public void setHospDominantMorbidityTypes(int hospDominantMorbidityTypes) {
        this.hospDominantMorbidityTypes = hospDominantMorbidityTypes;
    }

    @Column(name = "PREGNANCY_WITHOUT_DELIVERY", nullable = false)
    public Boolean isPregnancyWithoutDelivery() {
        return pregnancyWithoutDelivery;
    }

    public void setPregnancyWithoutDelivery(boolean pregnancyWithoutDelivery) {
        this.pregnancyWithoutDelivery = pregnancyWithoutDelivery;
    }

    @Column(name = "CHRONIC_CONDITION_COUNT", nullable = false)
    public int getChronicConditionCount() {
        return chronicConditionCount;
    }

    public void setChronicConditionCount(int chronicConditionCount) {
        this.chronicConditionCount = chronicConditionCount;
    }

    @Column(name = "DIAGNOSES_USED", nullable = false)
    public int getDiagnosesUsed() {
        return diagnosesUsed;
    }

    public void setDiagnosesUsed(int diagnosesUsed) {
        this.diagnosesUsed = diagnosesUsed;
    }

    @Column(name = "FRAILTY_FLAG", nullable = false)
    public boolean isFrailtyFlag() {
        return frailtyFlag;
    }

    public void setFrailtyFlag(boolean frailtyFlag) {
        this.frailtyFlag = frailtyFlag;
    }

    @Column(name = "FRAILTY_CONCEPTS", nullable = true)
    public String getFrailtyConcepts() {
        return frailtyConcepts;
    }

    public void setFrailtyConcepts(String frailtyConcepts) {
        this.frailtyConcepts = frailtyConcepts;
    }

    @Column(name = "FRAILTY_CONCEPT_COUNT", nullable = false)
    public int getFrailtyConceptCount() {
        return frailtyConceptCount;
    }

    public void setFrailtyConceptCount(int frailtyConceptCount) {
        this.frailtyConceptCount = frailtyConceptCount;
    }

    @Column(name = "CONCOMITANT_OPIOID_USER", nullable = true)
    public String getConcomitantOpioidUser() {
        return concomitantOpioidUser;
    }

    public void setConcomitantOpioidUser(String concomitantOpioidUser) {
        this.concomitantOpioidUser = concomitantOpioidUser;
    }

    @Column(name = "CHRONIC_OPIOID_USER", nullable = true)
    public String getChronicOpioidUser() {
        return chronicOpioidUser;
    }

    public void setChronicOpioidUser(String chronicOpioidUser) {
        this.chronicOpioidUser = chronicOpioidUser;
    }

    @Column(name = "OPIOID_DEPENDENT", nullable = false)
    public boolean isOpioidDependent() {
        return opioidDependent;
    }

    public void setOpioidDependent(boolean opioidDependent) {
        this.opioidDependent = opioidDependent;
    }

    @Column(name = "ADG_CODES", nullable = true)
    public String getAdgCodes() {
        return adgCodes;
    }

    public void setAdgCodes(String adgCodes) {
        this.adgCodes = adgCodes;
    }

    @Column(name = "ADG_VECTOR", nullable = false)
    public String getAdgVector() {
        return adgVector;
    }

    public void setAdgVector(String adgVector) {
        this.adgVector = adgVector;
    }

    @Column(name = "EDC_CODES", length = 2000, nullable = true)
    public String getEdcCodes() {
        return edcCodes;
    }

    public void setEdcCodes(String edcCodes) {
        this.edcCodes = edcCodes;
    }

    @Column(name = "MEDC_CODES", length = 2000, nullable = true)
    public String getMedcCodes() {
        return medcCodes;
    }

    public void setMedcCodes(String medcCodes) {
        this.medcCodes = medcCodes;
    }

    @Column(name = "RXMG_CODES", length = 700,nullable = true)
    public String getRxmgCodes() {
        return rxmgCodes;
    }

    public void setRxmgCodes(String rxmgCodes) {
        this.rxmgCodes = rxmgCodes;
    }

    @Column(name = "MEDICAL_RXMG_CODES", length = 700,nullable = true)
    public String getMedicalRxmgCodes() {
        return medicalRxmgCodes;
    }

    public void setMedicalRxmgCodes(String medicalRxmgCodes) {
        this.medicalRxmgCodes = medicalRxmgCodes;
    }

    @Column(name = "PHARMACY_RXMG_CODES", length = 700, nullable = true)
    public String getPharmacyRxmgCodes() {
        return pharmacyRxmgCodes;
    }

    public void setPharmacyRxmgCodes(String pharmacyRxmgCodes) {
        this.pharmacyRxmgCodes = pharmacyRxmgCodes;
    }

    @Column(name = "MAJOR_RXMG_CODES", nullable = true)
    public String getMajorRxmgCodes() {
        return majorRxmgCodes;
    }

    public void setMajorRxmgCodes(String majorRxmgCodes) {
        this.majorRxmgCodes = majorRxmgCodes;
    }

    @Column(name = "LAB_MARKERS", nullable = true)
    public String getLabMarkers() {
        return labMarkers;
    }

    public void setLabMarkers(String labMarkers) {
        this.labMarkers = labMarkers;
    }

    @Column(name = "ACTIVE_INGREDIENT_COUNT", nullable = false)
    public int getActiveIngredientCount() {
        return activeIngredientCount;
    }

    public void setActiveIngredientCount(int activeIngredientCount) {
        this.activeIngredientCount = activeIngredientCount;
    }

    @Column(name = "PROB_IP_HOSP", nullable = true)
    public BigDecimal getProbIpHosp() {
        return probIpHosp;
    }

    public void setProbIpHosp(BigDecimal probIpHosp) {
        this.probIpHosp = probIpHosp;
    }

    @Column(name = "PROB_IP_HOSP_6MOS", nullable = true)
    public BigDecimal getProbIpHosp6Mos() {
        return probIpHosp6Mos;
    }

    public void setProbIpHosp6Mos(BigDecimal probIpHosp6Mos) {
        this.probIpHosp6Mos = probIpHosp6Mos;
    }

    @Column(name = "PROB_ICU_HOSP", nullable = true)
    public BigDecimal getProbIcuHosp() {
        return probIcuHosp;
    }

    public void setProbIcuHosp(BigDecimal probIcuHosp) {
        this.probIcuHosp = probIcuHosp;
    }

    @Column(name = "PROB_INJURY_HOSP", nullable = true)
    public BigDecimal getProbInjuryHosp() {
        return probInjuryHosp;
    }

    public void setProbInjuryHosp(BigDecimal probInjuryHosp) {
        this.probInjuryHosp = probInjuryHosp;
    }

    @Column(name = "PROB_EXTENDED_HOSP", nullable = true)
    public BigDecimal getProbExtendedHosp() {
        return probExtendedHosp;
    }

    public void setProbExtendedHosp(BigDecimal probExtendedHosp) {
        this.probExtendedHosp = probExtendedHosp;
    }

    @Column(name = "PROB_UNPLANNED_30_DAY_READM", nullable = true)
    public BigDecimal getProbUnplanned30DayReadm() {
        return probUnplanned30DayReadm;
    }

    public void setProbUnplanned30DayReadm(BigDecimal probUnplanned30DayReadm) {
        this.probUnplanned30DayReadm = probUnplanned30DayReadm;
    }

    @Column(name = "AGE_REL_MACULAR_DEG_CONDITION", length = 3, nullable = false)
    @Enumerated(EnumType.STRING)
    public AcgIndexTypeEn getAgeRelMacularDegCondition() {
        return ageRelMacularDegCondition;
    }

    public void setAgeRelMacularDegCondition(AcgIndexTypeEn ageRelMacularDegCondition) {
        this.ageRelMacularDegCondition = ageRelMacularDegCondition;
    }

    @Column(name = "BIPOLAR_DISORDER_CONDITION", length = 3, nullable = false)
    @Enumerated(EnumType.STRING)
    public AcgIndexTypeEn getBipolarDisorderCondition() {
        return bipolarDisorderCondition;
    }

    public void setBipolarDisorderCondition(AcgIndexTypeEn bipolarDisorderCondition) {
        this.bipolarDisorderCondition = bipolarDisorderCondition;
    }

    @Column(name = "BIPOLAR_DISORDER_RX_GAPS", nullable = true)
    public String getBipolarDisorderRxGaps() {
        return bipolarDisorderRxGaps;
    }

    public void setBipolarDisorderRxGaps(String bipolarDisorderRxGaps) {
        this.bipolarDisorderRxGaps = bipolarDisorderRxGaps;
    }

    @Column(name = "BIPOLAR_DISORDER_MPR", nullable = true)
    public String getBipolarDisorderMpr() {
        return bipolarDisorderMpr;
    }

    public void setBipolarDisorderMpr(String bipolarDisorderMpr) {
        this.bipolarDisorderMpr = bipolarDisorderMpr;
    }

    @Column(name = "BIPOLAR_DISORDER_CSA", nullable = true)
    public String getBipolarDisorderCsa() {
        return bipolarDisorderCsa;
    }

    public void setBipolarDisorderCsa(String bipolarDisorderCsa) {
        this.bipolarDisorderCsa = bipolarDisorderCsa;
    }

    @Column(name = "BIPOLAR_DISORDER_UNTREATED_RX", nullable = true)
    public String getBipolarDisorderUntreatedRx() {
        return bipolarDisorderUntreatedRx;
    }

    public void setBipolarDisorderUntreatedRx(String bipolarDisorderUntreatedRx) {
        this.bipolarDisorderUntreatedRx = bipolarDisorderUntreatedRx;
    }

    @Column(name = "CONGESTIVE_HEART_FLR_CONDITION", length = 3, nullable = false)
    @Enumerated(EnumType.STRING)
    public AcgIndexTypeEn getCongestiveHeartFlrCondition() {
        return congestiveHeartFlrCondition;
    }

    public void setCongestiveHeartFlrCondition(AcgIndexTypeEn congestiveHeartFlrCondition) {
        this.congestiveHeartFlrCondition = congestiveHeartFlrCondition;
    }

    @Column(name = "CONGESTIVE_HEART_FLR_RX_GAPS", nullable = true)
    public String getCongestiveHeartFlrRxGaps() {
        return congestiveHeartFlrRxGaps;
    }

    public void setCongestiveHeartFlrRxGaps(String congestiveHeartFlrRxGaps) {
        this.congestiveHeartFlrRxGaps = congestiveHeartFlrRxGaps;
    }

    @Column(name = "CONGESTIVE_HEART_FLR_MPR", nullable = true)
    public String getCongestiveHeartFlrMpr() {
        return congestiveHeartFlrMpr;
    }

    public void setCongestiveHeartFlrMpr(String congestiveHeartFlrMpr) {
        this.congestiveHeartFlrMpr = congestiveHeartFlrMpr;
    }

    @Column(name = "CONGESTIVE_HEART_FLR_CSA", nullable = true)
    public String getCongestiveHeartFlrCsa() {
        return congestiveHeartFlrCsa;
    }

    public void setCongestiveHeartFlrCsa(String congestiveHeartFlrCsa) {
        this.congestiveHeartFlrCsa = congestiveHeartFlrCsa;
    }

    @Column(name = "CONGESTIVE_HEART_FLR_UNTR_RX", nullable = true)
    public String getCongestiveHeartFlrUntrRx() {
        return congestiveHeartFlrUntrRx;
    }

    public void setCongestiveHeartFlrUntrRx(String congestiveHeartFlrUntrRx) {
        this.congestiveHeartFlrUntrRx = congestiveHeartFlrUntrRx;
    }

    @Column(name = "DEPRESSION_CONDITION", length = 3, nullable = false)
    @Enumerated(EnumType.STRING)
    public AcgIndexTypeEn getDepressionCondition() {
        return depressionCondition;
    }

    public void setDepressionCondition(AcgIndexTypeEn depressionCondition) {
        this.depressionCondition = depressionCondition;
    }

    @Column(name = "DEPRESSION_RX_GAPS", nullable = true)
    public String getDepressionRxGaps() {
        return depressionRxGaps;
    }

    public void setDepressionRxGaps(String depressionRxGaps) {
        this.depressionRxGaps = depressionRxGaps;
    }

    @Column(name = "DEPRESSION_MPR", nullable = true)
    public String getDepressionMpr() {
        return depressionMpr;
    }

    public void setDepressionMpr(String depressionMpr) {
        this.depressionMpr = depressionMpr;
    }

    @Column(name = "DEPRESSION_CSA", nullable = true)
    public String getDepressionCsa() {
        return depressionCsa;
    }

    public void setDepressionCsa(String depressionCsa) {
        this.depressionCsa = depressionCsa;
    }

    @Column(name = "DEPRESSION_UNTREATED_RX", nullable = true)
    public String getDepressionUntreatedRx() {
        return depressionUntreatedRx;
    }

    public void setDepressionUntreatedRx(String depressionUntreatedRx) {
        this.depressionUntreatedRx = depressionUntreatedRx;
    }

    @Column(name = "DIABETES_CONDITION", length = 3, nullable = false)
    @Enumerated(EnumType.STRING)
    public AcgIndexTypeEn getDiabetesCondition() {
        return diabetesCondition;
    }

    public void setDiabetesCondition(AcgIndexTypeEn diabetesCondition) {
        this.diabetesCondition = diabetesCondition;
    }

    @Column(name = "DIABETES_RX_GAPS", nullable = true)
    public String getDiabetesRxGaps() {
        return diabetesRxGaps;
    }

    public void setDiabetesRxGaps(String diabetesRxGaps) {
        this.diabetesRxGaps = diabetesRxGaps;
    }

    @Column(name = "DIABETES_MPR", nullable = true)
    public String getDiabetesMpr() {
        return diabetesMpr;
    }

    public void setDiabetesMpr(String diabetesMpr) {
        this.diabetesMpr = diabetesMpr;
    }

    @Column(name = "DIABETES_CSA", nullable = true)
    public String getDiabetesCsa() {
        return diabetesCsa;
    }

    public void setDiabetesCsa(String diabetesCsa) {
        this.diabetesCsa = diabetesCsa;
    }

    @Column(name = "DIABETES_UNTREATED_RX", nullable = true)
    public String getDiabetesUntreatedRx() {
        return diabetesUntreatedRx;
    }

    public void setDiabetesUntreatedRx(String diabetesUntreatedRx) {
        this.diabetesUntreatedRx = diabetesUntreatedRx;
    }

    @Column(name = "DIABETES_LAB", nullable = true)
    public String getDiabetesLab() {
        return diabetesLab;
    }

    public void setDiabetesLab(String diabetesLab) {
        this.diabetesLab = diabetesLab;
    }

    @Column(name = "GLAUCOMA_CONDITION", length = 3, nullable = false)
    @Enumerated(EnumType.STRING)
    public AcgIndexTypeEn getGlaucomaCondition() {
        return glaucomaCondition;
    }

    public void setGlaucomaCondition(AcgIndexTypeEn glaucomaCondition) {
        this.glaucomaCondition = glaucomaCondition;
    }

    @Column(name = "GLAUCOMA_RX_GAPS", nullable = true)
    public String getGlaucomaRxGaps() {
        return glaucomaRxGaps;
    }

    public void setGlaucomaRxGaps(String glaucomaRxGaps) {
        this.glaucomaRxGaps = glaucomaRxGaps;
    }

    @Column(name = "GLAUCOMA_MPR", nullable = true)
    public String getGlaucomaMpr() {
        return glaucomaMpr;
    }

    public void setGlaucomaMpr(String glaucomaMpr) {
        this.glaucomaMpr = glaucomaMpr;
    }

    @Column(name = "GLAUCOMA_CSA", nullable = true)
    public String getGlaucomaCsa() {
        return glaucomaCsa;
    }

    public void setGlaucomaCsa(String glaucomaCsa) {
        this.glaucomaCsa = glaucomaCsa;
    }

    @Column(name = "GLAUCOMA_UNTREATED_RX", nullable = true)
    public String getGlaucomaUntreatedRx() {
        return glaucomaUntreatedRx;
    }

    public void setGlaucomaUntreatedRx(String glaucomaUntreatedRx) {
        this.glaucomaUntreatedRx = glaucomaUntreatedRx;
    }

    @Column(name = "HUMAN_IMDEF_VIRUS_CONDITION", length = 3, nullable = false)
    @Enumerated(EnumType.STRING)
    public AcgIndexTypeEn getHumanImdefVirusCondition() {
        return humanImdefVirusCondition;
    }

    public void setHumanImdefVirusCondition(AcgIndexTypeEn humanImdefVirusCondition) {
        this.humanImdefVirusCondition = humanImdefVirusCondition;
    }

    @Column(name = "HUMAN_IMDEF_VIRUS_RX_GAPS", nullable = true)
    public String getHumanImdefVirusRxGaps() {
        return humanImdefVirusRxGaps;
    }

    public void setHumanImdefVirusRxGaps(String humanImdefVirusRxGaps) {
        this.humanImdefVirusRxGaps = humanImdefVirusRxGaps;
    }

    @Column(name = "HUMAN_IMDEF_VIRUS_MPR", nullable = true)
    public String getHumanImdefVirusMpr() {
        return humanImdefVirusMpr;
    }

    public void setHumanImdefVirusMpr(String humanImdefVirusMpr) {
        this.humanImdefVirusMpr = humanImdefVirusMpr;
    }

    @Column(name = "HUMAN_IMDEF_VIRUS_CSA", nullable = true)
    public String getHumanImdefVirusCsa() {
        return humanImdefVirusCsa;
    }

    public void setHumanImdefVirusCsa(String humanImdefVirusCsa) {
        this.humanImdefVirusCsa = humanImdefVirusCsa;
    }

    @Column(name = "HUMAN_IMDEF_VIRUS_UNTREATED_RX", nullable = true)
    public String getHumanImdefVirusUntreatedRx() {
        return humanImdefVirusUntreatedRx;
    }

    public void setHumanImdefVirusUntreatedRx(String humanImdefVirusUntreatedRx) {
        this.humanImdefVirusUntreatedRx = humanImdefVirusUntreatedRx;
    }

    @Column(name = "DISORDERS_OF_LIPID_M_CONDITION", length = 3, nullable = false)
    @Enumerated(EnumType.STRING)
    public AcgIndexTypeEn getDisordersOfLipidMCondition() {
        return disordersOfLipidMCondition;
    }

    public void setDisordersOfLipidMCondition(AcgIndexTypeEn disordersOfLipidMCondition) {
        this.disordersOfLipidMCondition = disordersOfLipidMCondition;
    }

    @Column(name = "DISORDERS_OF_LIPID_M_RX_GAPS", nullable = true)
    public String getDisordersOfLipidMRxGaps() {
        return disordersOfLipidMRxGaps;
    }

    public void setDisordersOfLipidMRxGaps(String disordersOfLipidMRxGaps) {
        this.disordersOfLipidMRxGaps = disordersOfLipidMRxGaps;
    }

    @Column(name = "DISORDERS_OF_LIPID_MTBLSM_MPR", nullable = true)
    public String getDisordersOfLipidMtblsmMpr() {
        return disordersOfLipidMtblsmMpr;
    }

    public void setDisordersOfLipidMtblsmMpr(String disordersOfLipidMtblsmMpr) {
        this.disordersOfLipidMtblsmMpr = disordersOfLipidMtblsmMpr;
    }

    @Column(name = "DISORDERS_OF_LIPID_MTBLSM_CSA", nullable = true)
    public String getDisordersOfLipidMtblsmCsa() {
        return disordersOfLipidMtblsmCsa;
    }

    public void setDisordersOfLipidMtblsmCsa(String disordersOfLipidMtblsmCsa) {
        this.disordersOfLipidMtblsmCsa = disordersOfLipidMtblsmCsa;
    }

    @Column(name = "DISORDERS_OF_LIPID_M_UNTR_RX", nullable = true)
    public String getDisordersOfLipidMUntrRx() {
        return disordersOfLipidMUntrRx;
    }

    public void setDisordersOfLipidMUntrRx(String disordersOfLipidMUntrRx) {
        this.disordersOfLipidMUntrRx = disordersOfLipidMUntrRx;
    }

    @Column(name = "DISORDERS_OF_LIPID_MTBLSM_LAB", nullable = true)
    public String getDisordersOfLipidMtblsmLab() {
        return disordersOfLipidMtblsmLab;
    }

    public void setDisordersOfLipidMtblsmLab(String disordersOfLipidMtblsmLab) {
        this.disordersOfLipidMtblsmLab = disordersOfLipidMtblsmLab;
    }

    @Column(name = "HYPERTENSION_CONDITION", length = 3, nullable = false)
    @Enumerated(EnumType.STRING)
    public AcgIndexTypeEn getHypertensionCondition() {
        return hypertensionCondition;
    }

    public void setHypertensionCondition(AcgIndexTypeEn hypertensionCondition) {
        this.hypertensionCondition = hypertensionCondition;
    }

    @Column(name = "HYPERTENSION_RX_GAPS", nullable = true)
    public String getHypertensionRxGaps() {
        return hypertensionRxGaps;
    }

    public void setHypertensionRxGaps(String hypertensionRxGaps) {
        this.hypertensionRxGaps = hypertensionRxGaps;
    }

    @Column(name = "HYPERTENSION_MPR", nullable = true)
    public String getHypertensionMpr() {
        return hypertensionMpr;
    }

    public void setHypertensionMpr(String hypertensionMpr) {
        this.hypertensionMpr = hypertensionMpr;
    }

    @Column(name = "HYPERTENSION_CSA", nullable = true)
    public String getHypertensionCsa() {
        return hypertensionCsa;
    }

    public void setHypertensionCsa(String hypertensionCsa) {
        this.hypertensionCsa = hypertensionCsa;
    }

    @Column(name = "HYPERTENSION_UNTREATED_RX", nullable = true)
    public String getHypertensionUntreatedRx() {
        return hypertensionUntreatedRx;
    }

    public void setHypertensionUntreatedRx(String hypertensionUntreatedRx) {
        this.hypertensionUntreatedRx = hypertensionUntreatedRx;
    }

    @Column(name = "HYPOTHYROIDISM_CONDITION", length = 3, nullable = false)
    @Enumerated(EnumType.STRING)
    public AcgIndexTypeEn getHypothyroidismCondition() {
        return hypothyroidismCondition;
    }

    public void setHypothyroidismCondition(AcgIndexTypeEn hypothyroidismCondition) {
        this.hypothyroidismCondition = hypothyroidismCondition;
    }

    @Column(name = "HYPOTHYROIDISM_RX_GAPS", nullable = true)
    public String getHypothyroidismRxGaps() {
        return hypothyroidismRxGaps;
    }

    public void setHypothyroidismRxGaps(String hypothyroidismRxGaps) {
        this.hypothyroidismRxGaps = hypothyroidismRxGaps;
    }

    @Column(name = "HYPOTHYROIDISM_MPR", nullable = true)
    public String getHypothyroidismMpr() {
        return hypothyroidismMpr;
    }

    public void setHypothyroidismMpr(String hypothyroidismMpr) {
        this.hypothyroidismMpr = hypothyroidismMpr;
    }

    @Column(name = "HYPOTHYROIDISM_CSA", nullable = true)
    public String getHypothyroidismCsa() {
        return hypothyroidismCsa;
    }

    public void setHypothyroidismCsa(String hypothyroidismCsa) {
        this.hypothyroidismCsa = hypothyroidismCsa;
    }

    @Column(name = "HYPOTHYROIDISM_UNTREATED_RX", nullable = true)
    public String getHypothyroidismUntreatedRx() {
        return hypothyroidismUntreatedRx;
    }

    public void setHypothyroidismUntreatedRx(String hypothyroidismUntreatedRx) {
        this.hypothyroidismUntreatedRx = hypothyroidismUntreatedRx;
    }

    @Column(name = "IMMUNO_SUPPR_TRANS_CONDITION", length = 3, nullable = false)
    @Enumerated(EnumType.STRING)
    public AcgIndexTypeEn getImmunoSupprTransCondition() {
        return immunoSupprTransCondition;
    }

    public void setImmunoSupprTransCondition(AcgIndexTypeEn immunoSupprTransCondition) {
        this.immunoSupprTransCondition = immunoSupprTransCondition;
    }

    @Column(name = "IMMUNO_SUPPR_TRANS_RX_GAPS", nullable = true)
    public String getImmunoSupprTransRxGaps() {
        return immunoSupprTransRxGaps;
    }

    public void setImmunoSupprTransRxGaps(String immunoSupprTransRxGaps) {
        this.immunoSupprTransRxGaps = immunoSupprTransRxGaps;
    }

    @Column(name = "IMMUNO_SUPPR_TRANS_MPR", nullable = true)
    public String getImmunoSupprTransMpr() {
        return immunoSupprTransMpr;
    }

    public void setImmunoSupprTransMpr(String immunoSupprTransMpr) {
        this.immunoSupprTransMpr = immunoSupprTransMpr;
    }

    @Column(name = "IMMUNO_SUPPR_TRANS_CSA", nullable = true)
    public String getImmunoSupprTransCsa() {
        return immunoSupprTransCsa;
    }

    public void setImmunoSupprTransCsa(String immunoSupprTransCsa) {
        this.immunoSupprTransCsa = immunoSupprTransCsa;
    }

    @Column(name = "IMMUNO_SUPPR_TRANS_UNTR_RX", nullable = true)
    public String getImmunoSupprTransUntrRx() {
        return immunoSupprTransUntrRx;
    }

    public void setImmunoSupprTransUntrRx(String immunoSupprTransUntrRx) {
        this.immunoSupprTransUntrRx = immunoSupprTransUntrRx;
    }

    @Column(name = "ISCHEMIC_HEART_DIS_CONDITION", length = 3, nullable = false)
    @Enumerated(EnumType.STRING)
    public AcgIndexTypeEn getIschemicHeartDisCondition() {
        return ischemicHeartDisCondition;
    }

    public void setIschemicHeartDisCondition(AcgIndexTypeEn ischemicHeartDisCondition) {
        this.ischemicHeartDisCondition = ischemicHeartDisCondition;
    }

    @Column(name = "ISCHEMIC_HEART_DISEASE_RX_GAPS", nullable = true)
    public String getIschemicHeartDiseaseRxGaps() {
        return ischemicHeartDiseaseRxGaps;
    }

    public void setIschemicHeartDiseaseRxGaps(String ischemicHeartDiseaseRxGaps) {
        this.ischemicHeartDiseaseRxGaps = ischemicHeartDiseaseRxGaps;
    }

    @Column(name = "ISCHEMIC_HEART_DISEASE_MPR", nullable = true)
    public String getIschemicHeartDiseaseMpr() {
        return ischemicHeartDiseaseMpr;
    }

    public void setIschemicHeartDiseaseMpr(String ischemicHeartDiseaseMpr) {
        this.ischemicHeartDiseaseMpr = ischemicHeartDiseaseMpr;
    }

    @Column(name = "ISCHEMIC_HEART_DISEASE_CSA", nullable = true)
    public String getIschemicHeartDiseaseCsa() {
        return ischemicHeartDiseaseCsa;
    }

    public void setIschemicHeartDiseaseCsa(String ischemicHeartDiseaseCsa) {
        this.ischemicHeartDiseaseCsa = ischemicHeartDiseaseCsa;
    }

    @Column(name = "ISCHEMIC_HEART_DIS_UNTR_RX", nullable = true)
    public String getIschemicHeartDisUntrRx() {
        return ischemicHeartDisUntrRx;
    }

    public void setIschemicHeartDisUntrRx(String ischemicHeartDisUntrRx) {
        this.ischemicHeartDisUntrRx = ischemicHeartDisUntrRx;
    }

    @Column(name = "OSTEOPOROSIS_CONDITION", length = 3, nullable = false)
    @Enumerated(EnumType.STRING)
    public AcgIndexTypeEn getOsteoporosisCondition() {
        return osteoporosisCondition;
    }

    public void setOsteoporosisCondition(AcgIndexTypeEn osteoporosisCondition) {
        this.osteoporosisCondition = osteoporosisCondition;
    }

    @Column(name = "OSTEOPOROSIS_RX_GAPS", nullable = true)
    public String getOsteoporosisRxGaps() {
        return osteoporosisRxGaps;
    }

    public void setOsteoporosisRxGaps(String osteoporosisRxGaps) {
        this.osteoporosisRxGaps = osteoporosisRxGaps;
    }

    @Column(name = "OSTEOPOROSIS_MPR", nullable = true)
    public String getOsteoporosisMpr() {
        return osteoporosisMpr;
    }

    public void setOsteoporosisMpr(String osteoporosisMpr) {
        this.osteoporosisMpr = osteoporosisMpr;
    }

    @Column(name = "OSTEOPOROSIS_CSA", nullable = true)
    public String getOsteoporosisCsa() {
        return osteoporosisCsa;
    }

    public void setOsteoporosisCsa(String osteoporosisCsa) {
        this.osteoporosisCsa = osteoporosisCsa;
    }

    @Column(name = "OSTEOPOROSIS_UNTREATED_RX", nullable = true)
    public String getOsteoporosisUntreatedRx() {
        return osteoporosisUntreatedRx;
    }

    public void setOsteoporosisUntreatedRx(String osteoporosisUntreatedRx) {
        this.osteoporosisUntreatedRx = osteoporosisUntreatedRx;
    }

    @Column(name = "PARKINSONS_DISEASE_CONDITION", length = 3, nullable = false)
    @Enumerated(EnumType.STRING)
    public AcgIndexTypeEn getParkinsonsDiseaseCondition() {
        return parkinsonsDiseaseCondition;
    }

    public void setParkinsonsDiseaseCondition(AcgIndexTypeEn parkinsonsDiseaseCondition) {
        this.parkinsonsDiseaseCondition = parkinsonsDiseaseCondition;
    }

    @Column(name = "PARKINSONS_DISEASE_RX_GAPS", nullable = true)
    public String getParkinsonsDiseaseRxGaps() {
        return parkinsonsDiseaseRxGaps;
    }

    public void setParkinsonsDiseaseRxGaps(String parkinsonsDiseaseRxGaps) {
        this.parkinsonsDiseaseRxGaps = parkinsonsDiseaseRxGaps;
    }

    @Column(name = "PARKINSONS_DISEASE_MPR", nullable = true)
    public String getParkinsonsDiseaseMpr() {
        return parkinsonsDiseaseMpr;
    }

    public void setParkinsonsDiseaseMpr(String parkinsonsDiseaseMpr) {
        this.parkinsonsDiseaseMpr = parkinsonsDiseaseMpr;
    }

    @Column(name = "PARKINSONS_DISEASE_CSA", nullable = true)
    public String getParkinsonsDiseaseCsa() {
        return parkinsonsDiseaseCsa;
    }

    public void setParkinsonsDiseaseCsa(String parkinsonsDiseaseCsa) {
        this.parkinsonsDiseaseCsa = parkinsonsDiseaseCsa;
    }

    @Column(name = "PARKINSONS_DIS_UNTREATED_RX", nullable = true)
    public String getParkinsonsDisUntreatedRx() {
        return parkinsonsDisUntreatedRx;
    }

    public void setParkinsonsDisUntreatedRx(String parkinsonsDisUntreatedRx) {
        this.parkinsonsDisUntreatedRx = parkinsonsDisUntreatedRx;
    }

    @Column(name = "PERSISTENT_ASTHMA_CONDITION", length = 3, nullable = false)
    @Enumerated(EnumType.STRING)
    public AcgIndexTypeEn getPersistentAsthmaCondition() {
        return persistentAsthmaCondition;
    }

    public void setPersistentAsthmaCondition(AcgIndexTypeEn persistentAsthmaCondition) {
        this.persistentAsthmaCondition = persistentAsthmaCondition;
    }

    @Column(name = "PERSISTENT_ASTHMA_RX_GAPS", nullable = true)
    public String getPersistentAsthmaRxGaps() {
        return persistentAsthmaRxGaps;
    }

    public void setPersistentAsthmaRxGaps(String persistentAsthmaRxGaps) {
        this.persistentAsthmaRxGaps = persistentAsthmaRxGaps;
    }

    @Column(name = "PERSISTENT_ASTHMA_MPR", nullable = true)
    public String getPersistentAsthmaMpr() {
        return persistentAsthmaMpr;
    }

    public void setPersistentAsthmaMpr(String persistentAsthmaMpr) {
        this.persistentAsthmaMpr = persistentAsthmaMpr;
    }

    @Column(name = "PERSISTENT_ASTHMA_CSA", nullable = true)
    public String getPersistentAsthmaCsa() {
        return persistentAsthmaCsa;
    }

    public void setPersistentAsthmaCsa(String persistentAsthmaCsa) {
        this.persistentAsthmaCsa = persistentAsthmaCsa;
    }

    @Column(name = "PERSISTENT_ASTHMA_UNTREATED_RX", nullable = true)
    public String getPersistentAsthmaUntreatedRx() {
        return persistentAsthmaUntreatedRx;
    }

    public void setPersistentAsthmaUntreatedRx(String persistentAsthmaUntreatedRx) {
        this.persistentAsthmaUntreatedRx = persistentAsthmaUntreatedRx;
    }

    @Column(name = "RHEUMATOID_ARTHRITIS_CONDITION", length = 3, nullable = false)
    @Enumerated(EnumType.STRING)
    public AcgIndexTypeEn getRheumatoidArthritisCondition() {
        return rheumatoidArthritisCondition;
    }

    public void setRheumatoidArthritisCondition(AcgIndexTypeEn rheumatoidArthritisCondition) {
        this.rheumatoidArthritisCondition = rheumatoidArthritisCondition;
    }

    @Column(name = "RHEUMATOID_ARTHRITIS_RX_GAPS", nullable = true)
    public String getRheumatoidArthritisRxGaps() {
        return rheumatoidArthritisRxGaps;
    }

    public void setRheumatoidArthritisRxGaps(String rheumatoidArthritisRxGaps) {
        this.rheumatoidArthritisRxGaps = rheumatoidArthritisRxGaps;
    }

    @Column(name = "RHEUMATOID_ARTHRITIS_MPR", nullable = true)
    public String getRheumatoidArthritisMpr() {
        return rheumatoidArthritisMpr;
    }

    public void setRheumatoidArthritisMpr(String rheumatoidArthritisMpr) {
        this.rheumatoidArthritisMpr = rheumatoidArthritisMpr;
    }

    @Column(name = "RHEUMATOID_ARTHRITIS_CSA", nullable = true)
    public String getRheumatoidArthritisCsa() {
        return rheumatoidArthritisCsa;
    }

    public void setRheumatoidArthritisCsa(String rheumatoidArthritisCsa) {
        this.rheumatoidArthritisCsa = rheumatoidArthritisCsa;
    }

    @Column(name = "RHEUMATOID_ARTHRITIS_UNTR_RX", nullable = true)
    public String getRheumatoidArthritisUntrRx() {
        return rheumatoidArthritisUntrRx;
    }

    public void setRheumatoidArthritisUntrRx(String rheumatoidArthritisUntrRx) {
        this.rheumatoidArthritisUntrRx = rheumatoidArthritisUntrRx;
    }

    @Column(name = "SCHIZOPHRENIA_CONDITION", length = 3, nullable = false)
    @Enumerated(EnumType.STRING)
    public AcgIndexTypeEn getSchizophreniaCondition() {
        return schizophreniaCondition;
    }

    public void setSchizophreniaCondition(AcgIndexTypeEn schizophreniaCondition) {
        this.schizophreniaCondition = schizophreniaCondition;
    }

    @Column(name = "SCHIZOPHRENIA_RX_GAPS", nullable = true)
    public String getSchizophreniaRxGaps() {
        return schizophreniaRxGaps;
    }

    public void setSchizophreniaRxGaps(String schizophreniaRxGaps) {
        this.schizophreniaRxGaps = schizophreniaRxGaps;
    }

    @Column(name = "SCHIZOPHRENIA_MPR", nullable = true)
    public String getSchizophreniaMpr() {
        return schizophreniaMpr;
    }

    public void setSchizophreniaMpr(String schizophreniaMpr) {
        this.schizophreniaMpr = schizophreniaMpr;
    }

    @Column(name = "SCHIZOPHRENIA_CSA", nullable = true)
    public String getSchizophreniaCsa() {
        return schizophreniaCsa;
    }

    public void setSchizophreniaCsa(String schizophreniaCsa) {
        this.schizophreniaCsa = schizophreniaCsa;
    }

    @Column(name = "SCHIZOPHRENIA_UNTREATED_RX", nullable = true)
    public String getSchizophreniaUntreatedRx() {
        return schizophreniaUntreatedRx;
    }

    public void setSchizophreniaUntreatedRx(String schizophreniaUntreatedRx) {
        this.schizophreniaUntreatedRx = schizophreniaUntreatedRx;
    }

    @Column(name = "SEIZURE_DISORDERS_CONDITION", length = 3, nullable = false)
    @Enumerated(EnumType.STRING)
    public AcgIndexTypeEn getSeizureDisordersCondition() {
        return seizureDisordersCondition;
    }

    public void setSeizureDisordersCondition(AcgIndexTypeEn seizureDisordersCondition) {
        this.seizureDisordersCondition = seizureDisordersCondition;
    }

    @Column(name = "SEIZURE_DISORDERS_RX_GAPS", nullable = true)
    public String getSeizureDisordersRxGaps() {
        return seizureDisordersRxGaps;
    }

    public void setSeizureDisordersRxGaps(String seizureDisordersRxGaps) {
        this.seizureDisordersRxGaps = seizureDisordersRxGaps;
    }

    @Column(name = "SEIZURE_DISORDERS_MPR", nullable = true)
    public String getSeizureDisordersMpr() {
        return seizureDisordersMpr;
    }

    public void setSeizureDisordersMpr(String seizureDisordersMpr) {
        this.seizureDisordersMpr = seizureDisordersMpr;
    }

    @Column(name = "SEIZURE_DISORDERS_CSA", nullable = true)
    public String getSeizureDisordersCsa() {
        return seizureDisordersCsa;
    }

    public void setSeizureDisordersCsa(String seizureDisordersCsa) {
        this.seizureDisordersCsa = seizureDisordersCsa;
    }

    @Column(name = "SEIZURE_DISORDERS_UNTREATED_RX", nullable = true)
    public String getSeizureDisordersUntreatedRx() {
        return seizureDisordersUntreatedRx;
    }

    public void setSeizureDisordersUntreatedRx(String seizureDisordersUntreatedRx) {
        this.seizureDisordersUntreatedRx = seizureDisordersUntreatedRx;
    }

    @Column(name = "CHRONIC_OBST_PUL_DIS_CONDITION", length = 3, nullable = false)
    @Enumerated(EnumType.STRING)
    public AcgIndexTypeEn getChronicObstPulDisCondition() {
        return chronicObstPulDisCondition;
    }

    public void setChronicObstPulDisCondition(AcgIndexTypeEn chronicObstPulDisCondition) {
        this.chronicObstPulDisCondition = chronicObstPulDisCondition;
    }

    @Column(name = "CHRONIC_RENAL_FLR_CONDITION", length = 3, nullable = false)
    @Enumerated(EnumType.STRING)
    public AcgIndexTypeEn getChronicRenalFlrCondition() {
        return chronicRenalFlrCondition;
    }

    public void setChronicRenalFlrCondition(AcgIndexTypeEn chronicRenalFlrCondition) {
        this.chronicRenalFlrCondition = chronicRenalFlrCondition;
    }

    @Column(name = "CHRONIC_RENAL_FAILURE_LAB", nullable = true)
    public String getChronicRenalFailureLab() {
        return chronicRenalFailureLab;
    }

    public void setChronicRenalFailureLab(String chronicRenalFailureLab) {
        this.chronicRenalFailureLab = chronicRenalFailureLab;
    }

    @Column(name = "LOW_BACK_PAIN_CONDITION", length = 3, nullable = false)
    @Enumerated(EnumType.STRING)
    public AcgIndexTypeEn getLowBackPainCondition() {
        return lowBackPainCondition;
    }

    public void setLowBackPainCondition(AcgIndexTypeEn lowBackPainCondition) {
        this.lowBackPainCondition = lowBackPainCondition;
    }

    @Column(name = "DEFICIENCY_ANEMIA_CONDITION", nullable = true)
    @Enumerated(EnumType.STRING)
    public AcgIndexTypeEn getDeficiencyAnemiaCondition() {
        return deficiencyAnemiaCondition;
    }

    public void setDeficiencyAnemiaCondition(AcgIndexTypeEn deficiencyAnemiaCondition) {
        this.deficiencyAnemiaCondition = deficiencyAnemiaCondition;
    }

    @Column(name = "DEFICIENCY_ANEMIA_LAB", nullable = true)
    public String getDeficiencyAnemiaLab() {
        return deficiencyAnemiaLab;
    }

    public void setDeficiencyAnemiaLab(String deficiencyAnemiaLab) {
        this.deficiencyAnemiaLab = deficiencyAnemiaLab;
    }

    @Column(name = "TOTAL_RX_GAPS", nullable = true)
    public String getTotalRxGaps() {
        return totalRxGaps;
    }

    public void setTotalRxGaps(String totalRxGaps) {
        this.totalRxGaps = totalRxGaps;
    }

    @Column(name = "MAJORITY_SRC_OF_CARE_PERCENT", nullable = true)
    public BigDecimal getMajoritySrcOfCarePercent() {
        return majoritySrcOfCarePercent;
    }

    public void setMajoritySrcOfCarePercent(BigDecimal majoritySrcOfCarePercent) {
        this.majoritySrcOfCarePercent = majoritySrcOfCarePercent;
    }

    @Column(name = "MAJORITY_SRC_OF_CARE_PROVIDERS", nullable = true)
    public String getMajoritySrcOfCareProviders() {
        return majoritySrcOfCareProviders;
    }

    public void setMajoritySrcOfCareProviders(String majoritySrcOfCareProviders) {
        this.majoritySrcOfCareProviders = majoritySrcOfCareProviders;
    }

    @Column(name = "UNIQUE_PROVIDER_COUNT", nullable = true)
    public Integer getUniqueProviderCount() {
        return uniqueProviderCount;
    }

    public void setUniqueProviderCount(Integer uniqueProviderCount) {
        this.uniqueProviderCount = uniqueProviderCount;
    }

    @Column(name = "SPECIALTY_COUNT", nullable = true)
    public Integer getSpecialtyCount() {
        return specialtyCount;
    }

    public void setSpecialtyCount(Integer specialtyCount) {
        this.specialtyCount = specialtyCount;
    }

    @Column(name = "GENERALIST_SEEN", nullable = true)
    public Boolean getGeneralistSeen() {
        return generalistSeen;
    }

    public void setGeneralistSeen(Boolean generalistSeen) {
        this.generalistSeen = generalistSeen;
    }

    @Column(name = "GENERALIST_VISIT_COUNT", nullable = true)
    public Integer getGeneralistVisitCount() {
        return generalistVisitCount;
    }

    public void setGeneralistVisitCount(Integer generalistVisitCount) {
        this.generalistVisitCount = generalistVisitCount;
    }

    @Column(name = "MANAGEMENT_VISIT_COUNT", nullable = true)
    public Integer getManagementVisitCount() {
        return managementVisitCount;
    }

    public void setManagementVisitCount(Integer managementVisitCount) {
        this.managementVisitCount = managementVisitCount;
    }

    @Column(name = "COORDINATION_RISK", nullable = true)
    public String getCoordinationRisk() {
        return coordinationRisk;
    }

    public void setCoordinationRisk(String coordinationRisk) {
        this.coordinationRisk = coordinationRisk;
    }

    @Column(name = "CARE_DENSITY_RATIO", nullable = true)
    public BigDecimal getCareDensityRatio() {
        return careDensityRatio;
    }

    public void setCareDensityRatio(BigDecimal careDensityRatio) {
        this.careDensityRatio = careDensityRatio;
    }

    @Column(name = "CARE_DENSITY_QUANTILE", nullable = true)
    public String getCareDensityQuantile() {
        return careDensityQuantile;
    }

    public void setCareDensityQuantile(String careDensityQuantile) {
        this.careDensityQuantile = careDensityQuantile;
    }

    @Column(name = "CARE_DENS_BOTTOM_QUART_CUTOFF", nullable = true)
    public String getCareDensBottomQuartCutoff() {
        return careDensBottomQuartCutoff;
    }

    public void setCareDensBottomQuartCutoff(String careDensBottomQuartCutoff) {
        this.careDensBottomQuartCutoff = careDensBottomQuartCutoff;
    }

    @Column(name = "CARE_DENS_TOP_QUART_CUTOFF", nullable = true)
    public String getCareDensTopQuartCutoff() {
        return careDensTopQuartCutoff;
    }

    public void setCareDensTopQuartCutoff(String careDensTopQuartCutoff) {
        this.careDensTopQuartCutoff = careDensTopQuartCutoff;
    }

    @Column(name = "CARE_DENSITY_COST_SAVING_RATIO", nullable = true)
    public String getCareDensityCostSavingRatio() {
        return careDensityCostSavingRatio;
    }

    public void setCareDensityCostSavingRatio(String careDensityCostSavingRatio) {
        this.careDensityCostSavingRatio = careDensityCostSavingRatio;
    }

    @Column(name = "CARE_DENSITY_COST_SAVING_RANGE", nullable = true)
    public String getCareDensityCostSavingRange() {
        return careDensityCostSavingRange;
    }

    public void setCareDensityCostSavingRange(String careDensityCostSavingRange) {
        this.careDensityCostSavingRange = careDensityCostSavingRange;
    }

    @Column(name = "HHS_MARKET", nullable = true)
    public String getHhsMarket() {
        return hhsMarket;
    }

    public void setHhsMarket(String hhsMarket) {
        this.hhsMarket = hhsMarket;
    }

    @Column(name = "HHS_RATING_AREA", nullable = true)
    public String getHhsRatingArea() {
        return hhsRatingArea;
    }

    public void setHhsRatingArea(String hhsRatingArea) {
        this.hhsRatingArea = hhsRatingArea;
    }

    @Column(name = "HHS_RISK_POOL", nullable = true)
    public String getHhsRiskPool() {
        return hhsRiskPool;
    }

    public void setHhsRiskPool(String hhsRiskPool) {
        this.hhsRiskPool = hhsRiskPool;
    }

    @Column(name = "HHS_METAL_LEVEL", nullable = true)
    public String getHhsMetalLevel() {
        return hhsMetalLevel;
    }

    public void setHhsMetalLevel(String hhsMetalLevel) {
        this.hhsMetalLevel = hhsMetalLevel;
    }

    @Column(name = "HHS_CSR_INDICATOR", nullable = true)
    public String getHhsCsrIndicator() {
        return hhsCsrIndicator;
    }

    public void setHhsCsrIndicator(String hhsCsrIndicator) {
        this.hhsCsrIndicator = hhsCsrIndicator;
    }

    @Column(name = "HHS_MODEL", nullable = true)
    public String getHhsModel() {
        return hhsModel;
    }

    public void setHhsModel(String hhsModel) {
        this.hhsModel = hhsModel;
    }

    @Column(name = "HHS_EXCLUDE_FROM_RISK_ADJUST", nullable = true)
    public String getHhsExcludeFromRiskAdjust() {
        return hhsExcludeFromRiskAdjust;
    }

    public void setHhsExcludeFromRiskAdjust(String hhsExcludeFromRiskAdjust) {
        this.hhsExcludeFromRiskAdjust = hhsExcludeFromRiskAdjust;
    }

    @Column(name = "HHS_CC_CODES", length = 700, nullable = true)
    public String getHhsCcCodes() {
        return hhsCcCodes;
    }

    public void setHhsCcCodes(String hhsCcCodes) {
        this.hhsCcCodes = hhsCcCodes;
    }

    @Column(name = "HHS_HCC_CODES", length = 700,nullable = true)
    public String getHhsHccCodes() {
        return hhsHccCodes;
    }

    public void setHhsHccCodes(String hhsHccCodes) {
        this.hhsHccCodes = hhsHccCodes;
    }

    @Column(name = "HHS_RISK_SCORE", nullable = true)
    public String getHhsRiskScore() {
        return hhsRiskScore;
    }

    public void setHhsRiskScore(String hhsRiskScore) {
        this.hhsRiskScore = hhsRiskScore;
    }

    @Column(name = "HHS_CSR_ADJUSTED_RISK_SCORE", nullable = true)
    public String getHhsCsrAdjustedRiskScore() {
        return hhsCsrAdjustedRiskScore;
    }

    public void setHhsCsrAdjustedRiskScore(String hhsCsrAdjustedRiskScore) {
        this.hhsCsrAdjustedRiskScore = hhsCsrAdjustedRiskScore;
    }

    @Column(name = "HHS_MEMBER_MONTHS", nullable = true)
    public String getHhsMemberMonths() {
        return hhsMemberMonths;
    }

    public void setHhsMemberMonths(String hhsMemberMonths) {
        this.hhsMemberMonths = hhsMemberMonths;
    }

    @Column(name = "HHS_BILLABLE_MEMBER_MONTHS", nullable = true)
    public String getHhsBillableMemberMonths() {
        return hhsBillableMemberMonths;
    }

    public void setHhsBillableMemberMonths(String hhsBillableMemberMonths) {
        this.hhsBillableMemberMonths = hhsBillableMemberMonths;
    }

    @Column(name = "HHS_TOTAL_MEMBER_MONTHS", nullable = true)
    public String getHhsTotalMemberMonths() {
        return hhsTotalMemberMonths;
    }

    public void setHhsTotalMemberMonths(String hhsTotalMemberMonths) {
        this.hhsTotalMemberMonths = hhsTotalMemberMonths;
    }

    @Column(name = "HHS_TOTAL_BILLABLE_MEM_MONTHS", nullable = true)
    public String getHhsTotalBillableMemMonths() {
        return hhsTotalBillableMemMonths;
    }

    public void setHhsTotalBillableMemMonths(String hhsTotalBillableMemMonths) {
        this.hhsTotalBillableMemMonths = hhsTotalBillableMemMonths;
    }

    @Column(name = "CMS_FACTOR_TYPE", nullable = true)
    public String getCmsFactorType() {
        return cmsFactorType;
    }

    public void setCmsFactorType(String cmsFactorType) {
        this.cmsFactorType = cmsFactorType;
    }

    @Column(name = "CMS_MEDICAID_ELIGIBLE", nullable = true)
    public String getCmsMedicaidEligible() {
        return cmsMedicaidEligible;
    }

    public void setCmsMedicaidEligible(String cmsMedicaidEligible) {
        this.cmsMedicaidEligible = cmsMedicaidEligible;
    }

    @Column(name = "CMS_ORIG_RSN_FOR_ENTITLEMENT", nullable = true)
    public String getCmsOrigRsnForEntitlement() {
        return cmsOrigRsnForEntitlement;
    }

    public void setCmsOrigRsnForEntitlement(String cmsOrigRsnForEntitlement) {
        this.cmsOrigRsnForEntitlement = cmsOrigRsnForEntitlement;
    }

    @Column(name = "CMS_LTI_STATUS", nullable = true)
    public String getCmsLtiStatus() {
        return cmsLtiStatus;
    }

    public void setCmsLtiStatus(String cmsLtiStatus) {
        this.cmsLtiStatus = cmsLtiStatus;
    }

    @Column(name = "CMS_PACE_STATUS", nullable = true)
    public String getCmsPaceStatus() {
        return cmsPaceStatus;
    }

    public void setCmsPaceStatus(String cmsPaceStatus) {
        this.cmsPaceStatus = cmsPaceStatus;
    }

    @Column(name = "CMS_MODEL", nullable = true)
    public String getCmsModel() {
        return cmsModel;
    }

    public void setCmsModel(String cmsModel) {
        this.cmsModel = cmsModel;
    }

    @Column(name = "CMS_SUB_MODEL", nullable = true)
    public String getCmsSubModel() {
        return cmsSubModel;
    }

    public void setCmsSubModel(String cmsSubModel) {
        this.cmsSubModel = cmsSubModel;
    }

    @Column(name = "CMS_EXCLUDE_FROM_RISK_ADJUST", nullable = true)
    public String getCmsExcludeFromRiskAdjust() {
        return cmsExcludeFromRiskAdjust;
    }

    public void setCmsExcludeFromRiskAdjust(String cmsExcludeFromRiskAdjust) {
        this.cmsExcludeFromRiskAdjust = cmsExcludeFromRiskAdjust;
    }

    @Column(name = "CMS_CC_CODES", length = 700, nullable = true)
    public String getCmsCcCodes() {
        return cmsCcCodes;
    }

    public void setCmsCcCodes(String cmsCcCodes) {
        this.cmsCcCodes = cmsCcCodes;
    }

    @Column(name = "CMS_HCC_CODES", nullable = true)
    public String getCmsHccCodes() {
        return cmsHccCodes;
    }

    public void setCmsHccCodes(String cmsHccCodes) {
        this.cmsHccCodes = cmsHccCodes;
    }

    @Column(name = "CMS_RISK_SCORE", nullable = true)
    public String getCmsRiskScore() {
        return cmsRiskScore;
    }

    public void setCmsRiskScore(String cmsRiskScore) {
        this.cmsRiskScore = cmsRiskScore;
    }

    @Column(name = "CMS_MEMBER_MONTHS", nullable = true)
    public String getCmsMemberMonths() {
        return cmsMemberMonths;
    }

    public void setCmsMemberMonths(String cmsMemberMonths) {
        this.cmsMemberMonths = cmsMemberMonths;
    }

    @Column(name = "CMS_TOTAL_MEMBER_MONTHS", nullable = true)
    public String getCmsTotalMemberMonths() {
        return cmsTotalMemberMonths;
    }

    public void setCmsTotalMemberMonths(String cmsTotalMemberMonths) {
        this.cmsTotalMemberMonths = cmsTotalMemberMonths;
    }

    @Column(name = "WARNING_CODES", nullable = true)
    public String getWarningCodes() {
        return warningCodes;
    }

    public void setWarningCodes(String warningCodes) {
        this.warningCodes = warningCodes;
    }
        
    @Column(name = "REFERENCE_RESCALE_WEIGHT", precision = 2, scale = 1, nullable = false)
    public BigDecimal getReferenceRescaledWeight() {
        return referenceRescaledWeight;
    }

    public void setReferenceRescaledWeight(BigDecimal referenceRescaledWeight) {
        this.referenceRescaledWeight = referenceRescaledWeight;
    }

    @Column(name = "REFERENCE_RESCALE_WEIGHT_GROUP", precision = 2, scale = 1, nullable = false)
    public BigDecimal getReferenceRescaledWeightAgeGroup() {
        return referenceRescaledWeightAgeGroup;
    }

    public void setReferenceRescaledWeightAgeGroup(BigDecimal referenceRescaledWeightAgeGroup) {
        this.referenceRescaledWeightAgeGroup = referenceRescaledWeightAgeGroup;
    }

    @Column(name = "RESOURCE_UTIL_BAND_GROUP", precision = 2, scale = 1, nullable = false)
    public BigDecimal getResourceUtilizationBandAgeGroup() {
        return resourceUtilizationBandAgeGroup;
    }

    public void setResourceUtilizationBandAgeGroup(BigDecimal resourceUtilizationBandAgeGroup) {
        this.resourceUtilizationBandAgeGroup = resourceUtilizationBandAgeGroup;
    }

    @Column(name = "CHRONIC_CONDITION_COUNT_GROUP", precision = 3, scale = 1, nullable = false)
    public BigDecimal getChronicConditionCountAgeGroup() {
        return chronicConditionCountAgeGroup;
    }

    public void setChronicConditionCountAgeGroup(BigDecimal chronicConditionCountAgeGroup) {
        this.chronicConditionCountAgeGroup = chronicConditionCountAgeGroup;
    }

}
