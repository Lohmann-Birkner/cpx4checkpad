/*
 * Copyright (c) 2022 Lohmann & Birkner.
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
 *    2022  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.model;

import de.lb.cpx.model.enums.DetailsFilterEn;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
//import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

/**
 *
 * @author gerschmann
 */
@Entity
@Table(name = "T_BATCH_GROUP_PARAMETER")
@SuppressWarnings("serial")
public class TBatchGroupParameter  extends AbstractEntity {
    
  private static final long serialVersionUID = 1L;
    //private static final Logger LOG = Logger.getLogger(BatchGroupParameter.class.getName());

    private boolean doSimulate;// simulate DRG/PEPP
    private boolean doSupplementaryFees; // supplementary Fees
    private boolean doMedAndRemedies; // use Medicines and Remidies
    private boolean doCareData; // use CareData
    private boolean doLabor;
    private boolean doDepartmentGrouping;
    private boolean doStationsgrouping;
    private boolean doRules;
    private boolean doRulesSimulate;
    private boolean do4actualRoleOnly;
    private boolean doHistoryCases;
//    private int mModelId = GDRGModel.AUTOMATIC.getGDRGVersion();
//    private ArrayList<Long> mRuleIds = null; // default null - use all rules
//    private ArrayList<Long> mCaseIds = null; // default null - group all cases
//    private ArrayList<Long> mRoleIds = null; // default null - group for all roles
//    private boolean doInclHis = true;
    private DetailsFilterEn detailsFilter;

    private boolean doUseAllRules;
    private Date admissionDateFrom ;
    private Date admissionDateUntil;
    private Date dischargeDateFrom;
    private Date dischargeDateUntil;
    private boolean grouped;
    private TBatchResult batchResult;        


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_BATCH_GROUP_PARAMETER_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @Column(name = "DO_SIMULATE_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean isDoSimulate() {
        return doSimulate;
    }

    public void setDoSimulate(boolean doSimulate) {
        this.doSimulate = doSimulate;
    }

    @Column(name = "DO_SUPPL_FFEES_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean isDoSupplementaryFees() {
        return doSupplementaryFees;
    }

    public void setDoSupplementaryFees(boolean doSupplementaryFees) {
        this.doSupplementaryFees = doSupplementaryFees;
    }

    @Column(name = "DO_MED_AND_REMEDIES_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean isDoMedAndRemedies() {
        return doMedAndRemedies;
    }

    public void setDoMedAndRemedies(boolean doMedAndRemedies) {
        this.doMedAndRemedies = doMedAndRemedies;
    }

     @Column(name = "DO_CARE_DATA_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean isDoCareData() {
        return doCareData;
    }

    public void setDoCareData(boolean doCareData) {
        this.doCareData = doCareData;
    }

    @Column(name = "DO_LABOR_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean isDoLabor() {
        return doLabor;
    }

    public void setDoLabor(boolean doLabor) {
        this.doLabor = doLabor;
    }

    @Column(name = "DO_DEPARTMENT_GR_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean isDoDepartmentGrouping() {
        return doDepartmentGrouping;
    }

    public void setDoDepartmentGrouping(boolean doDepartmentGrouping) {
        this.doDepartmentGrouping = doDepartmentGrouping;
    }

    @Column(name = "DO_WARD_GR_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean isDoStationsgrouping() {
        return doStationsgrouping;
    }

    public void setDoStationsgrouping(boolean doStationsgrouping) {
        this.doStationsgrouping = doStationsgrouping;
    }

    @Column(name = "DO_RULES_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean isDoRules() {
        return doRules;
    }

    public void setDoRules(boolean doRules) {
        this.doRules = doRules;
    }

    @Column(name = "DO_RULES_SIMUL_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean isDoRulesSimulate() {
        return doRulesSimulate;
    }

    public void setDoRulesSimulate(boolean doRulesSimulate) {
        this.doRulesSimulate = doRulesSimulate;
    }

    @Column(name = "DO_ACTUAL_ROLE_ONLY_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean isDo4actualRoleOnly() {
        return do4actualRoleOnly;
    }

    public void setDo4actualRoleOnly(boolean do4actualRoleOnly) {
        this.do4actualRoleOnly = do4actualRoleOnly;
    }

    @Column(name = "DO_HISTORY_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean isDoHistoryCases() {
        return doHistoryCases;
    }

    public void setDoHistoryCases(boolean doHistoryCases) {
        this.doHistoryCases = doHistoryCases;
    }

    @Column(name = "DETAILS_FILTER_EN", length = 25)
    @Enumerated(EnumType.STRING)
    public DetailsFilterEn getDetailsFilter() {
        return detailsFilter;
    }

    public void setDetailsFilter(DetailsFilterEn detailsFilter) {
        this.detailsFilter = detailsFilter;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ADMISSION_DATE_FROM", length = 7)
    public Date getAdmissionDateFrom() {
        return admissionDateFrom;
    }

    public void setAdmissionDateFrom(Date admissionDateFrom) {
        this.admissionDateFrom = admissionDateFrom;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ADMISSION_DATE_TO", length = 7)
    public Date getAdmissionDateUntil() {
        return admissionDateUntil;
    }

    public void setAdmissionDateUntil(Date admissionDateUntil) {
        this.admissionDateUntil = admissionDateUntil;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DISCHARGE_DATE_FROM", length = 7)
    public Date getDischargeDateFrom() {
        return dischargeDateFrom;
    }

    public void setDischargeDateFrom(Date dischargeDateFrom) {
        this.dischargeDateFrom = dischargeDateFrom;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DISCHARGE_DATE_TO", length = 7)
    public Date getDischargeDateUntil() {
        return dischargeDateUntil;
    }

    public void setDischargeDateUntil(Date dischargeDateUntil) {
        this.dischargeDateUntil = dischargeDateUntil;
    }

    @Column(name = "GROUPED_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean isGrouped() {
        return grouped;
    }

    public void setGrouped(boolean grouped) {
        this.grouped = grouped;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "T_BATCH_RESULT_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_GROUP_PARAM_2_BATCH_RES_ID"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    public TBatchResult getBatchResult() {
        return batchResult;
    }

    public void setBatchResult(TBatchResult batchResult) {
        this.batchResult = batchResult;
    }

    @Column(name = "DO_USE_ALL_RULES_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean isDoUseAllRules() {
        return doUseAllRules;
    }

    public void setDoUseAllRules(boolean doUseAllRules) {
        this.doUseAllRules = doUseAllRules;
    }

}
