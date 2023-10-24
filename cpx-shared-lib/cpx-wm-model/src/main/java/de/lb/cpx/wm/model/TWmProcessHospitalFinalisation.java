/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2017 Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.wm.model;

import de.lb.cpx.server.commons.dao.AbstractVersionEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Entity to manage process finalisation results stores values in the moment
 * when the process finalisation is completed therefore redundant informations
 * must be stored
 *
 * @author Dirk Niemeier
 */
@Entity
@Table(name = "T_WM_PROCESS_HOSPITAL_FINALIS",
        indexes = {
            @Index(name = "IDX_PROC_HOS_FINAL4PROC_HOS_ID", columnList = "T_WM_PROCESS_HOSPITAL_ID", unique = false)})
public class TWmProcessHospitalFinalisation extends AbstractVersionEntity {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(TWmProcessHospitalFinalisation.class.getName());

    private TWmProcessHospital processHospital;
    private String drgInitial; //DrgInitial, DRG_INITIAL VARCHAR2(10)
    private String drgFinal; //DrgFinal, DRG_FINAL VARCHAR2(10)
    private Double cwInitial; //CostWeightInitial, CW_INITIAL FLOAT precision = 15, scale = 4
    private Double cwFinal; //CostWeightFinal, CW_FINAL FLOAT precision = 15, scale = 4
    private Double cwDiff; //CostWeightDifference, CW_DIFF FLOAT precision = 15, scale = 4
    private Integer losInitial; //LosInitial, LOS_INITIAL NUMBER(10,0) precision = 15, scale = 0
    private Integer losFinal; //LosFinal, LOS_FINAL NUMBER(10,0) precision = 15, scale = 0
    private Integer losDiff; //LosDiff, LOS_DIFF NUMBER(10,0) precision = 15, scale = 0
    private Integer unbilledDays; //DaysUnbilled, UNBILLED_DAYS NUMBER(10,0) precision = 15, scale = 0
    private Double revenueInitial; //RevenueInitial, REVENUE_INITIAL FLOAT precision = 15, scale = 4
    private Double revenueFinal; //RevenueFinal, REVENUE_FINAL FLOAT precision = 15, scale = 4
    private double revenueDiff; //RevenueDifference, REVENUE_DIFF FLOAT precision = 15, scale = 4
//    private long mainAuditReason; //MainAutditReason (id of one mdk_audit_reason), AUDIT_REASON
    private Set<TWmMdkAuditReasons> auditReasons = new HashSet<>(0); //Liste of  AuditReasons (Extended ,main)
    private Date closingDate; //Closing Date, CLOSING_DATE
    private long closingResult; //Closing Result, CLOSING_RESULT
    private int savedDays; //Saved Days, SAVED_DAYS
    private double savedMoney; //Saved Money, SAVED_MONEY
    private long initialVersion; //intial Version(db id of tcasedetails), INITIAL_VERSION
    private long initialVersionNumber; //initial Version number (version number in tcasedetails), INITIAL_VERSION_NUMBER
    private String initialVersionComment; //initial Version comment (version comment in tcasedetails), INITIAL_VERSION_COMMENT
    private long finalVersion; //final version (db id of tcasedetails, FINAL_VERSION
    private long finalVersionNumber; //final version number(version number in tcasedetails), FINAL_VERSION_NUMBER
    private String finalVersionComment; //final version comment (version comment in tcasedetials), FINAL_VERSION_COMMENT
    private String resultComment; //result comment, RESULT_COMMENT
    private Double initialSupplementaryFee; // supplementary fee value of the initial value (computed) ,INITIAL_SUPPLEMENTARY_FEE
    private Double finalSupplementaryFee; // supplementary fee value of the final value (computed) ,FINAL_SUPPLEMENTARY_FEE
    private Double diffSupplementaryFee; // supplementary fee value of the initial value (computed) ,INITIAL_SUPPLEMENTARY_FEE
    public static final String REASON_DELIMITER = ",";
    private Double risk;
    private Double resultDelta;
    private Double cwCareInitial;
    private Double cwCareFinal;
    private Double cwCareDiff;
    private Double penaltyFee;
//    private Set<TWmRisk> risks = new HashSet<>(0);
    private TWmFinalisationRisk finalisRisk;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "processHospitalFinalisation", orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public TWmFinalisationRisk getRisks() {
        return this.finalisRisk;
    }

    public void setRisks(final TWmFinalisationRisk risks) {
        this.finalisRisk = risks;
    }

    /**
     * @return the process
     */
    @OneToOne
    @JoinColumn(name = "T_WM_PROCESS_HOSPITAL_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_PROC_HOS_FINAL4PROC_HOS_ID"))
    public TWmProcessHospital getProcessHospital() {
        return processHospital;
    }

    /**
     * @param processHospital the process to set
     */
    public void setProcessHospital(TWmProcessHospital processHospital) {
        this.processHospital = processHospital;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_WM_PROCESS_HOSPITAL_DRG_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "process", orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Set<TWmMdkAuditReasons> getAuditReasons() {
        return this.auditReasons;
    }

    /**
     * @param auditReasons sets extended reasons
     */
    public void setAuditReasons(final Set<TWmMdkAuditReasons> auditReasons) {
        this.auditReasons = auditReasons;
    }

    public void setMainAuditReasons(final String listAuditReasonsNumber) {

        if (listAuditReasonsNumber == null) {
            return;
        }
        String[] tmp = listAuditReasonsNumber.split(REASON_DELIMITER);
        List<Long> reasonNumbers = new ArrayList<>();
        for (String separateReasonNumber : tmp) {
            separateReasonNumber = separateReasonNumber.trim();
            if (separateReasonNumber.trim().isEmpty()) {
                LOG.log(Level.WARNING, " audit reason number is empty");
                continue;
            }
            long reasonNumber = 0L;
            try {
                reasonNumber = Long.parseLong(separateReasonNumber.trim());
            } catch (NumberFormatException ex) {
                LOG.log(Level.SEVERE, "This is not a valid  audit reason number: '" + separateReasonNumber + "'", ex);
                continue;
            }
            if (reasonNumber == 0) {
                LOG.log(Level.WARNING, "audit reason number is equal to 0");
                continue;
            }
            addAuditReason(reasonNumber, false);
            reasonNumbers.add(reasonNumber);
        }
        final Iterator<TWmMdkAuditReasons> it = getAuditReasons(false).iterator();
        while (it.hasNext()) {
            TWmMdkAuditReasons auditReason = it.next();
            boolean remain = false;
            for (Long reasonNumber : reasonNumbers) {
                if (reasonNumber == auditReason.getAuditReasonNumber()) {
                    remain = true;
                    break;
                }
            }
            if (!remain) {
                auditReasons.remove(auditReason);
            }
        }
        for (Long reasonNumber : reasonNumbers) {
            addAuditReason(reasonNumber, false);
        }
    }

    /**
     *
     * @param listeAuditReasonsExtended extended reasons
     */
    public void setAuditReasonsExtended(final String listeAuditReasonsExtended) {
        if (listeAuditReasonsExtended == null) {
            return;
        }
        String[] tmp = listeAuditReasonsExtended.split(REASON_DELIMITER);
        List<Long> reasonNumbers = new ArrayList<>();
        for (String separateReasonNumber : tmp) {
            separateReasonNumber = separateReasonNumber.trim();
            if (separateReasonNumber.trim().isEmpty()) {
                LOG.log(Level.WARNING, " audit reason number is empty");
                continue;
            }
            long reasonNumber = 0L;
            try {
                reasonNumber = Long.parseLong(separateReasonNumber.trim());
            } catch (NumberFormatException ex) {
                LOG.log(Level.SEVERE, "This is not a valid  audit reason number: '" + separateReasonNumber + "'", ex);
                continue;
            }
            if (reasonNumber == 0) {
                LOG.log(Level.WARNING, "audit reason number is equal to 0");
                continue;
            }
            addAuditReason(reasonNumber, true);
            reasonNumbers.add(reasonNumber);
        }
        final Iterator<TWmMdkAuditReasons> it = getAuditReasons(true).iterator();
        while (it.hasNext()) {
            TWmMdkAuditReasons auditReason = it.next();
            boolean remain = false;
            for (Long reasonNumber : reasonNumbers) {
                if (reasonNumber == auditReason.getAuditReasonNumber()) {
                    remain = true;
                    break;
                }
            }
            if (!remain) {
                auditReasons.remove(auditReason);
            }
        }
        for (Long reasonNumber : reasonNumbers) {
            addAuditReason(reasonNumber, true);
        }
    }

    /**
     *
     * @param pExtended true wenn Extended
     * @return set of audit reasons
     */
    public Set<TWmMdkAuditReasons> getAuditReasons(final boolean pExtended) {
        Set<TWmMdkAuditReasons> auditReasons = new HashSet<>();
        Iterator<TWmMdkAuditReasons> it = new HashSet<>(this.auditReasons).iterator();
        while (it.hasNext()) {
            TWmMdkAuditReasons auditReason = it.next();
            if (auditReason.getExtended() && pExtended
                    || !auditReason.getExtended() && !pExtended) {
                auditReasons.add(auditReason);
            }
        }
        return auditReasons;
    }

    /**
     *
     * @param pAuditReasonNumber AuditReason InternalNumber
     * @param pExtended true wenn Extended
     * @return was added?
     */
    public boolean addAuditReason(final Long pAuditReasonNumber, final boolean pExtended) {
        if (pAuditReasonNumber == null || pAuditReasonNumber.equals(0L)) {
            return false;
        }

        TWmMdkAuditReasons auditReason = new TWmMdkAuditReasons();
        auditReason.setProcess(this);
        auditReason.setAuditReasonNumber(pAuditReasonNumber);
        auditReason.setExtended(pExtended);
        if (!auditReasons.contains(auditReason)) {
            auditReasons.add(auditReason);
        }
        return true;
    }

    /**
     * Removes the AuditReason from this set if it is present
     *
     * @param auditReasonNumber AuditReason InternalNumber
     * @return true wenn removed
     */
    public boolean removeAuditReason(final Long auditReasonNumber) {
        TWmMdkAuditReasons auditReason = getAuditReasons(auditReasonNumber, false);
        if (auditReason == null) {
            return true;
        }
        return auditReasons.remove(auditReason);
    }

    /**
     * Removes the AuditReason from this set if it is present
     *
     * @param auditReasonNumberExtended AuditReason InternalNumber
     * @return true when removed
     */
    public boolean removeAuditReasonExtended(final Long auditReasonNumberExtended) {
        TWmMdkAuditReasons auditReason = getAuditReasons(auditReasonNumberExtended, true);
        if (auditReason == null) {
            return true;
        }
        return auditReasons.remove(auditReason);
    }

    /**
     *
     * @param pAuditReasonNumber AuditReason Number
     * @param pExtended true wenn Extended
     * @return audit reason
     */
    public TWmMdkAuditReasons getAuditReasons(final Long pAuditReasonNumber, final boolean pExtended) {
        if (pAuditReasonNumber == null || pAuditReasonNumber.equals(0L)) {
            return null;
        }

        Iterator<TWmMdkAuditReasons> it = getAuditReasons(pExtended).iterator();
        while (it.hasNext()) {
            TWmMdkAuditReasons auditReason = it.next();
            if (auditReason.getAuditReasonNumber() == pAuditReasonNumber) {
                return auditReason;
            }
        }
        return null;
    }

    public void clearAuditReasonsExtended() {
        Iterator<TWmMdkAuditReasons> it = auditReasons.iterator();
        while (it.hasNext()) {
            TWmMdkAuditReasons auditReason = it.next();
            if (auditReason.getExtended()) {
                it.remove();
            }
        }
    }

    public void clearMainAuditReason() {
        Iterator<TWmMdkAuditReasons> it = auditReasons.iterator();
        while (it.hasNext()) {
            TWmMdkAuditReasons auditReason = it.next();
            if (!auditReason.getExtended()) {
                it.remove();
            }
        }

    }

    /**
     * @return the drgInitial
     */
    @Column(name = "DRG_INITIAL", length = 10, nullable = true)
    public String getDrgInitial() {
        return drgInitial;
    }

    /**
     * @param drgInitial the drgInitial to set
     */
    public void setDrgInitial(String drgInitial) {
        this.drgInitial = drgInitial;
    }

    /**
     * @return the drgFinal
     */
    @Column(name = "DRG_FINAL", length = 10, nullable = true)
    public String getDrgFinal() {
        return drgFinal;
    }

    /**
     * @param drgFinal the drgFinal to set
     */
    public void setDrgFinal(String drgFinal) {
        this.drgFinal = drgFinal;
    }

    /**
     * @return the cwInitial
     */
    @Column(name = "CW_INITIAL", precision = 15, scale = 4, nullable = true)
    public Double getCwInitial() {
        return cwInitial;
    }

    /**
     * @param cwInitial the cwInitial to set
     */
    public void setCwInitial(Double cwInitial) {
        this.cwInitial = cwInitial;
    }

    /**
     * @return the cwFinal
     */
    @Column(name = "CW_FINAL", precision = 15, scale = 4, nullable = true)
    public Double getCwFinal() {
        return cwFinal;
    }

    /**
     * @param cwFinal the cwFinal to set
     */
    public void setCwFinal(Double cwFinal) {
        this.cwFinal = cwFinal;
    }

    /**
     * @return the cwDiff
     */
    @Column(name = "CW_DIFF", precision = 15, scale = 4, nullable = true)
    public Double getCwDiff() {
        return cwDiff;
    }

    /**
     * @param cwDiff the cwDiff to set
     */
    public void setCwDiff(Double cwDiff) {
        this.cwDiff = cwDiff;
    }

    /**
     * @return the cwCareInitial
     */
    @Column(name = "CW_CARE_INITIAL", precision = 15, scale = 4, nullable = true)
    public Double getCwCareInitial() {
        return cwCareInitial;
    }

    /**
     * @param cwInitial the cwCareInitial to set
     */
    public void setCwCareInitial(Double cwInitial) {
        this.cwCareInitial = cwInitial;
    }

    /**
     * @return the cwCareFinal
     */
    @Column(name = "CW_CARE_FINAL", precision = 15, scale = 4, nullable = true)
    public Double getCwCareFinal() {
        return cwCareFinal;
    }

    /**
     * @param cwFinal the cwCareFinal to set
     */
    public void setCwCareFinal(Double cwFinal) {
        this.cwCareFinal = cwFinal;
    }

    /**
     * @return the cwCareDiff
     */
    @Column(name = "CW_CARE_DIFF", precision = 15, scale = 4, nullable = true)
    public Double getCwCareDiff() {
        return cwCareDiff;
    }

    /**
     * @param cwCareDiff the cwCareDiff to set
     */
    public void setCwCareDiff(Double cwCareDiff) {
        this.cwCareDiff = cwCareDiff;
    }

    /**
     * @return the losInitial
     */
    @Column(name = "LOS_INITIAL", precision = 15, scale = 0, nullable = true)
    public Integer getLosInitial() {
        return losInitial;
    }

    /**
     * @param losInitial the losInitial to set
     */
    public void setLosInitial(Integer losInitial) {
        this.losInitial = losInitial;
    }

    /**
     * @return the losFinal
     */
    @Column(name = "LOS_FINAL", precision = 15, scale = 0, nullable = true)
    public Integer getLosFinal() {
        return losFinal;
    }

    /**
     * @param losFinal the losFinal to set
     */
    public void setLosFinal(Integer losFinal) {
        this.losFinal = losFinal;
    }

    /**
     * @return the losDiff
     */
    @Column(name = "LOS_DIFF", precision = 15, scale = 0, nullable = true)
    public Integer getLosDiff() {
        return losDiff;
    }

    /**
     * @param losDiff the losDiff to set
     */
    public void setLosDiff(Integer losDiff) {
        this.losDiff = losDiff;
    }

    /**
     * @return the unbilledDays
     */
    @Column(name = "FEEC_UNBILLED_DAYS", precision = 3, scale = 0, nullable = true)
    public Integer getUnbilledDays() {
        return unbilledDays;
    }

    /**
     * @param unbilledDays the unbilledDays to set
     */
    public void setUnbilledDays(Integer unbilledDays) {
        this.unbilledDays = unbilledDays;
    }

    /**
     * @return the revenueInitial
     */
    @Column(name = "REVENUE_INITIAL", precision = 15, scale = 4, nullable = true)
    public Double getRevenueInitial() {
        return revenueInitial;
    }

    /**
     * @param revenueInitial the revenueInitial to set
     */
    public void setRevenueInitial(Double revenueInitial) {
        this.revenueInitial = revenueInitial;
    }

    /**
     * @return the revenueFinal
     */
    @Column(name = "REVENUE_FINAL", precision = 15, scale = 4, nullable = true)
    public Double getRevenueFinal() {
        return revenueFinal;
    }

    /**
     * @param revenueFinal the revenueFinal to set
     */
    public void setRevenueFinal(Double revenueFinal) {
        this.revenueFinal = revenueFinal;
    }

    /**
     * @return the revenueDiff
     */
    @Column(name = "REVENUE_DIFF", precision = 15, scale = 4, nullable = true)
    public Double getRevenueDiff() {
        return revenueDiff;
    }

    /**
     * @param revenueDiff the revenueDiff to set
     */
    public void setRevenueDiff(Double revenueDiff) {
        this.revenueDiff = revenueDiff;
    }

//    /**
//     * @return get the main audit reason, id of mdk_audit_reason
//     */
//    @Column(name = "AUDIT_REASON", nullable = false, scale = 0)
//    public long getMainAuditReason() {
//        return mainAuditReason;
//            }
//  
//    /**
//     * @param mainAuditReason sets a new main audit reason, should be id of
//     * mdk_audit_reason
//     */
//    public void setMainAuditReason(long mainAuditReason) {
//        this.mainAuditReason = mainAuditReason;
//    }
    /**
     * @param pExtended extended audit reason?
     * @return list of mdk_audit_reasons ids seperated by comma
     */
//    @Column(name = "AUDIT_REASON_EXTENDED", length = 255)
    public Set<TWmMdkAuditReasons> getAuditReasonsExtended(final boolean pExtended) {
        Set<TWmMdkAuditReasons> auditReasons = new HashSet<>();
        Iterator<TWmMdkAuditReasons> it = new HashSet<>(this.auditReasons).iterator();
        while (it.hasNext()) {
            TWmMdkAuditReasons auditReason = it.next();
            if (auditReason.getExtended() && pExtended
                    || !auditReason.getExtended() && !pExtended) {
                auditReasons.add(auditReason);
            }
        }
        return auditReasons;
    }

    /**
     * @return list of extended audit reasons
     * @throws NumberFormatException if content can not be cast to long
     */
    @Transient
    public List<Long> getAuditReasonsExtendedList() {
        List<Long> listOfNumbers = new ArrayList<>();
        if (getAuditReasons() == null || getAuditReasons().isEmpty()) {
            return listOfNumbers;
        }
        for (Iterator<TWmMdkAuditReasons> it = auditReasons.iterator(); it.hasNext();) {
            TWmMdkAuditReasons strId = it.next();
            if (strId.getExtended()) {
                listOfNumbers.add(strId.getAuditReasonNumber());
            }
        }

        return listOfNumbers;
    }

    @Transient
    public List<Long> getMainAuditReasonList() {
        List<Long> listOfNumbers = new ArrayList<>();
        if (getAuditReasons() == null || getAuditReasons().isEmpty()) {
            return listOfNumbers;
        }
        for (Iterator<TWmMdkAuditReasons> it = auditReasons.iterator(); it.hasNext();) {
            TWmMdkAuditReasons strId = it.next();
            if (!strId.getExtended()) {
                listOfNumbers.add(strId.getAuditReasonNumber());
            }
        }

        return listOfNumbers;
    }

    /**
     * @return gets the closing date of the process
     */
    @Column(name = "CLOSING_DATE_PROCESS", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getClosingDate() {
        return closingDate == null ? null : new Date(closingDate.getTime());
    }

    /**
     * @param closingDate sets a new closing date of the process, cant be null
     */
    public void setClosingDate(Date closingDate) {
        this.closingDate = closingDate == null ? null : new Date(closingDate.getTime());
    }

    /**
     * @return get the closing result of the process, id of
     * c_wm_list_process_result
     */
    @Column(name = "CLOSING_RESULT", scale = 0, nullable = false)
    public long getClosingResult() {
        return closingResult;
    }

    /**
     * @param closingResult set new closing result, cant be null
     */
    public void setClosingResult(long closingResult) {
        this.closingResult = closingResult;
    }

    /**
     * @return saved days / difference days
     */
    @Column(name = "SAVED_DAYS", precision = 3, scale = 0)
    public int getSavedDays() {
        return savedDays;
    }

    /**
     * @param savedDays sets new saved days, precision is 3
     */
    public void setSavedDays(int savedDays) {
        this.savedDays = savedDays;
    }

    /**
     * @return saved money / diff money
     */
    @Column(name = "SAVED_MONEY", precision = 3, scale = 0)
    public double getSavedMoney() {
        return savedMoney;
    }

    /**
     * @param savedMoney sets new saved money , precision is 3
     */
    public void setSavedMoney(double savedMoney) {
        this.savedMoney = savedMoney;
    }

    /**
     * @return gets the initial version id (id of t_case_details), with which
     * the process was closed
     */
    @Column(name = "INITIAL_VERSION", scale = 0, nullable = false)
    public long getInitialVersion() {
        return initialVersion;
    }

    /**
     * @param initialVersion sets new initial version, should if of
     * t_case_details
     */
    public void setInitialVersion(long initialVersion) {
        this.initialVersion = initialVersion;
    }

    /**
     * @return version number of the initial version (version in t_case_details)
     */
    @Column(name = "INITIAL_VERSION_NUMBER", scale = 0)
    public long getInitialVersionNumber() {
        return initialVersionNumber;
    }

    /**
     * @param initialVersionNumber sets new versionnumber of the inital version
     */
    public void setInitialVersionNumber(long initialVersionNumber) {
        this.initialVersionNumber = initialVersionNumber;
    }

    /**
     * @return comment of the initial version , comment in t_case_details
     */
    @Column(name = "INITIAL_VERSION_COMMENT", length = 800)
    public String getInitialVersionComment() {
        return initialVersionComment;
    }

    /**
     * @param initialVersionComment sets new comment for the initial version
     */
    public void setInitialVersionComment(String initialVersionComment) {
        this.initialVersionComment = initialVersionComment;
    }

    /**
     * @return gets the final version id (id of t_case_details), with which the
     * process was closed
     */
    @Column(name = "FINAL_VERSION", scale = 0, nullable = false)
    public long getFinalVersion() {
        return finalVersion;
    }

    /**
     * @param finalVersion sets new initial version, should if of t_case_details
     */
    public void setFinalVersion(long finalVersion) {
        this.finalVersion = finalVersion;
    }

    /**
     * @return version number of the final version (version in t_case_details)
     */
    @Column(name = "FINAL_VERSION_NUMBER", scale = 0)
    public long getFinalVersionNumber() {
        return finalVersionNumber;
    }

    /**
     * @param finalVersionNumber sets new versionnumber of the final version
     */
    public void setFinalVersionNumber(long finalVersionNumber) {
        this.finalVersionNumber = finalVersionNumber;
    }

    /**
     * @return comment of the final version , comment in t_case_details
     */
    @Column(name = "FINAL_VERSION_COMMENT", length = 800)
    public String getFinalVersionComment() {
        return finalVersionComment;
    }

    /**
     * @param finalVersionComment sets new comment for the initial version
     */
    public void setFinalVersionComment(String finalVersionComment) {
        this.finalVersionComment = finalVersionComment;
    }

    /**
     * @return comment of the process completion
     */
    @Column(name = "RESULT_COMMENT", length = 800)
    public String getResultComment() {
        return resultComment;
    }

    /**
     * @param resultComment set new comment for the process completion, lenght
     * is 800
     */
    public void setResultComment(String resultComment) {
        this.resultComment = resultComment;
    }

    /**
     * @return supplementary fee value of the inital version
     */
    @Column(name = "INITIAL_SUPPLEMENTARY_FEE", precision = 10, scale = 2)
    public Double getInitialSupplementaryFee() {
        return initialSupplementaryFee;
    }

    /**
     * @param initialSupplementaryFee new initial supplementary fee value
     */
    public void setInitialSupplementaryFee(Double initialSupplementaryFee) {
        this.initialSupplementaryFee = initialSupplementaryFee;
    }

    /**
     * @return supplementary fee value of the final version
     */
    @Column(name = "FINAL_SUPPLEMENTARY_FEE", precision = 10, scale = 2)
    public Double getFinalSupplementaryFee() {
        return finalSupplementaryFee;
    }

    /**
     * @param finalSupplementaryFee new final supplementary fee value
     */
    public void setFinalSupplementaryFee(Double finalSupplementaryFee) {
        this.finalSupplementaryFee = finalSupplementaryFee;
    }

    /**
     * @return difference of the initial and final supplementary fee value
     */
    @Column(name = "DIFF_SUPPLEMENTARY_FEE", precision = 10, scale = 2)
    public Double getDiffSupplementaryFee() {
        return diffSupplementaryFee;
    }

    /**
     * @param diffSupplementaryFee new difference of the intial and final
     * supplementary fee value
     */
    public void setDiffSupplementaryFee(Double diffSupplementaryFee) {
        this.diffSupplementaryFee = diffSupplementaryFee;
    }

    /**
     *
     * @return risk risk of the Process completion
     */
    @Column(name = "RISK", precision = 10, scale = 2)
    public double getRisk() {
        return risk;
    }

    /**
     *
     * @return resultDelta resultDelta of the Process completion
     */
    @Column(name = "RESULT_DELTA", precision = 10, scale = 2)
    public double getResultDelta() {
        return resultDelta;
    }

    /**
     *
     * @return penaltyFee of the Process completion
     */
    @Column(name = "PENALTY_FEE", precision = 10, scale = 2)
    public Double getPenaltyFee() {
        return penaltyFee;
    }

    
    /**
     *
     * @param risk risk of the Process completion
     */
    public void setRisk(double risk) {
        this.risk = risk;
    }

    /**
     *
     * @param resultDelta resultDelta of the Process completion
     */
    public void setResultDelta(double resultDelta) {
        this.resultDelta = resultDelta;
    }
    /**
     *
     * @param penaltyFee resultDelta of the Process completion
     */
    public void setPenaltyFee(Double penaltyFee) {
        this.penaltyFee = penaltyFee;
    }

    @Override
    public boolean versionEquals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (!(object instanceof TWmProcessHospitalFinalisation)) {
            return false;
        }
        final TWmProcessHospitalFinalisation other = (TWmProcessHospitalFinalisation) object;
        if (!Objects.equals(this.processHospital, other.processHospital)) {
            return false;
        }
        if (!Objects.equals(this.drgInitial, other.drgInitial)) {
            return false;
        }
        if (!Objects.equals(this.drgFinal, other.drgFinal)) {
            return false;
        }
        if (!Objects.equals(this.cwInitial, other.cwInitial)) {
            return false;
        }
        if (!Objects.equals(this.cwFinal, other.cwFinal)) {
            return false;
        }
        if (!Objects.equals(this.cwDiff, other.cwDiff)) {
            return false;
        }
        if (!Objects.equals(this.losInitial, other.losInitial)) {
            return false;
        }
        if (!Objects.equals(this.losFinal, other.losFinal)) {
            return false;
        }
        if (!Objects.equals(this.losDiff, other.losDiff)) {
            return false;
        }
        if (!Objects.equals(this.unbilledDays, other.unbilledDays)) {
            return false;
        }
        if (!Objects.equals(this.revenueInitial, other.revenueInitial)) {
            return false;
        }
        if (!Objects.equals(this.revenueFinal, other.revenueFinal)) {
            return false;
        }
        if (!Objects.equals(this.revenueDiff, other.revenueDiff)) {
            return false;
        }
        if (!Objects.equals(this.risk, other.risk)) {
            return false;
        }
        if (!Objects.equals(this.resultDelta, other.resultDelta)) {
            return false;
        }
        if (!Objects.equals(this.savedDays, other.savedDays)) {
            return false;
        }
        if (!Objects.equals(this.savedMoney, other.savedMoney)) {
            return false;
        }
        return true;
    }

}
