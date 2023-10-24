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
 *    2016  Husser - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.wm.model;

import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.server.commons.dao.AbstractVersionEntity;
import de.lb.cpx.wm.model.enums.WmRequestTypeEn;
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
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * <p style="font-size:1em; color:green;"> Tabelle der Anfrage. </p>
 *
 * @author Husser
 */
@Entity
@Table(name = "T_WM_REQUEST",
        indexes = {
            @Index(name = "IDX_T_WM_REQ4PROCESS_HOSP_ID", columnList = "T_WM_PROCESS_HOSPITAL_ID", unique = false)})
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "REQUEST_TYPE", discriminatorType = DiscriminatorType.INTEGER)
public abstract class TWmRequest extends AbstractVersionEntity {

    private static final Logger LOG = Logger.getLogger(TWmRequest.class.getName());

    public static final String REASON_DELIMITER = ",";
    private static final long serialVersionUID = 1L;

    //private Integer requestType;
    private TWmProcessHospital processHospital;
    
    private TCaseDetails kisCaseDetails;

    private Set<TWmMdkAuditReasons> auditReasons = new HashSet<>(0);
    private Set<TWmRisk> risks = new HashSet<>(0);

//    private long modificationUserId;
//    private String ikNumber;
    private String comment;
    
    private Long requestState;
    
    private Date reportDate;// Gutachten
    
    private Date startAudit; // einleitung prüfverfahren

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "request", orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Set<TWmRisk> getRisks() {
        return this.risks;
    }

    public void setRisks(final Set<TWmRisk> risks) {
        this.risks = risks;
    }

    //private WmRequestTypeEn requestType;
    //private Integer reqType;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "request", orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Set<TWmMdkAuditReasons> getAuditReasons() {
        return this.auditReasons;
    }

    public void setAuditReasons(final Set<TWmMdkAuditReasons> auditReasons) {
        this.auditReasons = auditReasons;
    }

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
     * @return the state
     */
    @Column(name = "REQUEST_STATE")
    public Long getRequestState() {
        return requestState;
    }

    /**
     * @param state: the state to set
     */
    public void setRequestState(Long state) {
        this.requestState = state;
    }

    @Column(name = "START_AUDIT")
    @Temporal(TemporalType.DATE)
    public Date getStartAudit() {
        return startAudit == null ? null : new Date(startAudit.getTime());
    }

    /**
     * @param startAudit: the startAudit to set
     */
    public void setStartAudit(Date startAudit) {
        this.startAudit = startAudit == null ? null : new Date(startAudit.getTime());
    }

    @Column(name = "REPORT_DATE")
    @Temporal(TemporalType.DATE)
    public Date getReportDate() {
        return reportDate == null ? null : new Date(reportDate.getTime());
    }

    /**
     * @param reportDate: the reportDate to set
     */
    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate == null ? null : new Date(reportDate.getTime());
    }

    
    public boolean containsAuditReason(Long pKey) {
        for (TWmMdkAuditReasons reason : getAuditReasons()) {
            if (pKey.equals(reason.getAuditReasonNumber())) {
                return true;
            }
        }
        return false;
    }

    public boolean containsAuditReasonExtended(Long pKey) {
        for (TWmMdkAuditReasons reason : getAuditReasons(true)) {
            if (pKey.equals(reason.getAuditReasonNumber())) {
                return true;
            }
        }
        return false;
    }

    public boolean addAuditReason(final Long pAuditReasonNumber, final boolean pExtended) {
        if (pAuditReasonNumber == null || pAuditReasonNumber.equals(0L)) {
            return false;
        }

        TWmMdkAuditReasons auditReason = new TWmMdkAuditReasons();
        auditReason.setRequest(this);
        auditReason.setAuditReasonNumber(pAuditReasonNumber);
        auditReason.setExtended(pExtended);

        return auditReasons.add(auditReason);
    }

    public List<TWmMdkAuditReasons> getAuditReasons(final Long pAuditReasonNumber, final boolean pExtended) {
        if (pAuditReasonNumber == null || pAuditReasonNumber.equals(0L)) {
            return null;
        }

        List<TWmMdkAuditReasons> result = new ArrayList<>();
        Iterator<TWmMdkAuditReasons> it = getAuditReasons(pExtended).iterator();
        while (it.hasNext()) {
            TWmMdkAuditReasons auditReason = it.next();
            if (auditReason.getAuditReasonNumber() == pAuditReasonNumber) {
                result.add(auditReason);
            }
        }
        return result;
    }

    /**
     * @param auditReasonNumbers the mdkAuditReasons to set
     */
    public void addAuditReasons(final Long[] auditReasonNumbers) {
        if (auditReasonNumbers == null) {
            return;
        }
        for (Long auditReasonNumber : auditReasonNumbers) {
            addAuditReason(auditReasonNumber, false);
        }
    }

    /**
     * @param auditReasonNumbersExtended some discription
     */
    public void addAuditReasonsExtended(final Long[] auditReasonNumbersExtended) {
        if (auditReasonNumbersExtended == null) {
            return;
        }
        for (Long auditReasonNumber : auditReasonNumbersExtended) {
            addAuditReason(auditReasonNumber, true);
        }
    }

    public int removeAuditReason(final Long auditReasonNumber) {
        Iterator<TWmMdkAuditReasons> it = getAuditReasons(auditReasonNumber, false).iterator();
        int count = 0;
        while (it.hasNext()) {
            TWmMdkAuditReasons reason = it.next();
            if (auditReasons.remove(reason)) {
                count++;
            }
        }
        return count;
    }

    public int removeAuditReasonExtended(final Long auditReasonNumberExtended) {
        Iterator<TWmMdkAuditReasons> it = getAuditReasons(auditReasonNumberExtended, true).iterator();
        int count = 0;
        while (it.hasNext()) {
            TWmMdkAuditReasons reason = it.next();
            if (auditReasons.remove(reason)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns a list of audit reason numbers Attention: There is a duplicate of
     * this code in TWmRequestAudit!
     *
     * @return audit reason numbers
     */
    @Transient
    public long[] getAuditReasonsSplitted() {
        List<Long> auditReasonNumbers = new ArrayList<>();
        Iterator<TWmMdkAuditReasons> it = new ArrayList<>(auditReasons).iterator();
        while (it.hasNext()) {
            TWmMdkAuditReasons auditReason = it.next();
            if (!auditReason.getExtended()) {
                auditReasonNumbers.add(auditReason.getAuditReasonNumber());
            }
        }
        long[] numbers = new long[auditReasonNumbers.size()];
        int i = 0;
        for (Long auditReasonNumber : auditReasonNumbers) {
            numbers[i++] = auditReasonNumber;
        }
        return numbers;
    }

    /**
     * Returns a list of audit reason numbers Attention: There is a duplicate of
     * this code in TWmRequestAudit!
     *
     * @return audit reason numbers
     */
    @Transient
    public long[] getAuditReasonsExtendedSplitted() {
        List<Long> auditReasonNumbers = new ArrayList<>();
        Iterator<TWmMdkAuditReasons> it = new ArrayList<>(auditReasons).iterator();
        while (it.hasNext()) {
            TWmMdkAuditReasons auditReason = it.next();
            if (auditReason.getExtended()) {
                auditReasonNumbers.add(auditReason.getAuditReasonNumber());
            }
        }
        long[] numbers = new long[auditReasonNumbers.size()];
        int i = 0;
        for (Long auditReasonNumber : auditReasonNumbers) {
            numbers[i++] = auditReasonNumber;
        }
        return numbers;
    }

    /**
     * @return the mdkAuditReasons
     */
    @Transient
    public String getMdkAuditReasons() {
        final StringBuilder sb = new StringBuilder();
        for (long auditReasonNumber : getAuditReasonsSplitted()) {
            if (sb.length() > 0) {
                sb.append(REASON_DELIMITER);
            }
            sb.append(auditReasonNumber);
        }
        return sb.toString();
    }

    /**
     * @return the mdkAuditReasonsExtended
     */
    @Transient
    public String getMdkAuditReasonsExtended() {
        final StringBuilder sb = new StringBuilder();
        for (long auditReasonNumber : getAuditReasonsExtendedSplitted()) {
            if (sb.length() > 0) {
                sb.append(REASON_DELIMITER);
            }
            sb.append(auditReasonNumber);
        }
        return sb.toString();
    }

    /**
     * @param mdkAuditReasonsExtended the mdkAuditReasonsExtended to set
     */
    public void setAuditReasonsExtended(final String mdkAuditReasonsExtended) {
        if (mdkAuditReasonsExtended == null) {
            return;
        }
        //clearMdkAuditReasonsExtended();
        String[] tmp = mdkAuditReasonsExtended.split(REASON_DELIMITER);
        List<Long> reasonNumbers = new ArrayList<>();
        for (String separateReasonNumber : tmp) {
            separateReasonNumber = separateReasonNumber.trim();
            if (separateReasonNumber.trim().isEmpty()) {
                LOG.log(Level.WARNING, "MDK audit reason number is empty");
                continue;
            }
            long reasonNumber = 0L;
            try {
                reasonNumber = Long.parseLong(separateReasonNumber.trim());
            } catch (NumberFormatException ex) {
                LOG.log(Level.SEVERE, "This is not a valid MDK audit reason number: '" + separateReasonNumber + "'", ex);
                continue;
            }
            if (reasonNumber == 0L) {
                LOG.log(Level.WARNING, "MDK audit reason number is equal to 0");
                continue;
            }
            //addAuditReason(reasonNumber, true);
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
     * @param mdkAuditReasons the mdkAuditReasons to set
     */
    public void setAuditReasons(final String mdkAuditReasons) {
        if (mdkAuditReasons == null) {
            return;
        }
        //clearMdkAuditReasons();
        String[] tmp = mdkAuditReasons.split(REASON_DELIMITER);
        List<Long> reasonNumbers = new ArrayList<>();
        for (String separateReasonNumber : tmp) {
            separateReasonNumber = separateReasonNumber.trim();
            if (separateReasonNumber.trim().isEmpty()) {
                LOG.log(Level.WARNING, "MDK audit reason number is empty");
                continue;
            }
            long reasonNumber = 0L;
            try {
                reasonNumber = Long.parseLong(separateReasonNumber.trim());
            } catch (NumberFormatException ex) {
                LOG.log(Level.SEVERE, "This is not a valid MDK audit reason number: '" + separateReasonNumber + "'", ex);
                continue;
            }
            if (reasonNumber == 0L) {
                LOG.log(Level.WARNING, "MDK audit reason number is equal to 0");
                continue;
            }
            //addAuditReason(reasonNumber, false);
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

    public void clearMdkAuditReasonsExtended() {
        Iterator<TWmMdkAuditReasons> it = auditReasons.iterator();
        while (it.hasNext()) {
            TWmMdkAuditReasons auditReason = it.next();
            if (auditReason.getExtended()) {
                it.remove();
            }
        }
    }

    public void clearMdkAuditReasons() {
        Iterator<TWmMdkAuditReasons> it = auditReasons.iterator();
        while (it.hasNext()) {
            TWmMdkAuditReasons auditReason = it.next();
            if (!auditReason.getExtended()) {
                it.remove();
            }
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_WM_REQUEST_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    @Transient
    public Integer getRequestType() {
        if (this.getClass() != TWmRequest.class) {
            String val = this.getClass().getAnnotation(DiscriminatorValue.class).value();
            if (val == null || val.trim().isEmpty()) {
                return null;
            }
            return Integer.valueOf(val);
        }
        return null;
    }

    @Transient
    public WmRequestTypeEn getRequestTypeEnum() {
        return WmRequestTypeEn.findById(getRequestType());
    }

    @Transient
    public boolean isRequestMdk() {
        return this instanceof TWmRequestMdk;
    }

    @Transient
    public boolean isRequestAudit() {
        return this instanceof TWmRequestAudit;
    }

    @Transient
    public boolean isRequestBege() {
        return this instanceof TWmRequestBege;
    }
    
    @Transient
    public boolean isRequestReview(){
        return this instanceof TWmRequestReview;
    }

//    /**
//     * get request enum type.
//     *
//     * @return WmRequestType
//     */
//    @Column(name = "REQUEST_ENUM_TYPE", nullable = false)
//    @Enumerated(EnumType.STRING)
//    public WmRequestTypeEn getRequestEnumType() {
//        return requestType;
//    }
//
//    public void setRequestEnumType(WmRequestTypeEn requestType) {
//        this.requestType = requestType;
//    }
//    @Transient
//    public void setRequestType(WmRequestType reqType) {
//        if (reqType != null) {
//            this.requestType = reqType;
//        }
//    }
//    @Transient
//    public void setRequestType(Integer reqType) {
//        if (reqType != null) {
//            this.reqType = reqType;
//        }
//    }
    /**
     * Gibt Verweis auf T_WM_PROCESS. zurück.
     *
     * @return process
     */
    @ManyToOne
    @JoinColumn(name = "T_WM_PROCESS_HOSPITAL_ID", foreignKey = @ForeignKey(name = "FK_T_WM_REQ4PROCESS_HOSP_ID"))
    public TWmProcessHospital getProcessHospital() {
        return processHospital;
    }

//    @Column(name = "MODIFICATION_USER_ID")
//    public long getModificationUserId() {
//        return modificationUserId;
//    }
    /**
     * Gibt IK-Nummer des Krankenhauses zurück.
     *
     * @return ikNumber
     */
//    @Column(name = "IK_NUMBER", length = 10, nullable = false)
//    public String getIkNumber() {
//        return ikNumber;
//    }
    /**
     * Gibt Text zurück ,der in diesem Request steht.
     *
     * @return Comment
     */
    @Column(name = "REQUEST_COMMENT")
    public String getComment() {
        return comment;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gibt Verweis auf ID der Tablle T_CASE_DETAILS zurück.
     *
     * @return kisCaseDetails
     */
    @ManyToOne
    @JoinColumn(name = "T_CASE_DETAILS_ID", nullable = true, foreignKey = @ForeignKey(name = "FK_REQUEST_CASE4CASEDET_ID"))
    public TCaseDetails getKisDetails() {
        return kisCaseDetails;
    }

    /**
     * @param kisCaseDetails Column T_CASE_DETAILS_ID :Verweis auf ID der Tabelle T_CASE_DETAILS, LOCAL_FL = 0.
     */
    public void setKisDetails(TCaseDetails kisCaseDetails) {
        this.kisCaseDetails = kisCaseDetails;
    }

//    public void setRequestType(Integer requestType) {
//        this.requestType = requestType;
//    }
//
//    public void setRequestType(WmRequestType requestType) {
//        setRequestType(requestType==null?null:requestType.getId());
//    }
    /**
     *
     * @param processHospital Column T_WM_PROCESS_HOSPITAL_ID :Verweis auf
     * T_WM_PROCESS_HOSPITAL.
     */
    public void setProcessHospital(TWmProcessHospital processHospital) {
        this.processHospital = processHospital;
    }

//    public void setModificationUserId(long modificationUserId) {
//        this.modificationUserId = modificationUserId;
//    }
//    /**
//     *
//     * @param ikNumber Column IK_NUMBER :IK-Nummer des Krankenhauses .
//     */
//    public void setIkNumber(String ikNumber) {
//        this.ikNumber = ikNumber;
//    }
    /**
     *
     * @param comment Column REQUEST_COMMENT :Text ,der in diesem Request steht.
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean versionEquals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (!(object instanceof TWmRequest)) {
            return false;
        }
        final TWmRequest other = (TWmRequest) object;
        if (!Objects.equals(this.comment, other.comment)) {
            return false;
        }
//        if (!Objects.equals(this.ikNumber, other.ikNumber)) {
//            return false;
//        }
//        if (!Objects.equals(this.modificationUserId, other.modificationUserId)) {
//            return false;
//        }
        if (!Objects.equals(this.processHospital, other.processHospital)) {
            return false;
        }
       if (!Objects.equals(this.kisCaseDetails, other.kisCaseDetails)) {
            return false;
        }
              if (!Objects.equals(this.requestState, other.requestState)) {
            return false;
        }
       if (!Objects.equals(this.reportDate, other.reportDate)) {
            return false;
        }
       if (!Objects.equals(this.startAudit, other.startAudit)) {
            return false;
        }

        /*
        if (!Objects.equals(this.requestType, other.requestType)) {
            return false;
        }
         */
        return true;
    }

}
