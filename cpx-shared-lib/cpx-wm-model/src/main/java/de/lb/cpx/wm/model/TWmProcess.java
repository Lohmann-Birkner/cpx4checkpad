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
 *    2016  husser - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.wm.model;

import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.model.util.ModelUtil;
import de.lb.cpx.server.commons.dao.AbstractVersionEntity;
import de.lb.cpx.wm.converter.WorkflowStateConverter;
import de.lb.cpx.wm.model.enums.WmProcessTypeEn;
import de.lb.cpx.wm.model.enums.WmWorkflowStateEn;
import de.lb.cpx.wm.model.enums.WmWorkflowTypeEn;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * <p style="font-size:1em; color:green;">Tabelle der Ereignis . </p>
 *
 * @author husser
 */
@Entity
@Table(name = "T_WM_PROCESS", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"WORKFLOW_NUMBER"}, name = "Uni_WorkflowNumber")
}, indexes = {
    @Index(name = "IDX_WM_PROCESS4PATIENT_ID", columnList = "T_PATIENT_ID", unique = false)})
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "PROCESS_TYPE",
        discriminatorType = DiscriminatorType.STRING)
public abstract class TWmProcess extends AbstractVersionEntity {

    private static final long serialVersionUID = 1L;

    private long workflowNumber;

    private long processModificationUser;

    private WmWorkflowTypeEn workflowType;

    //private Set<TWmProcessCase> cases;
    private Set<String> keywords = new HashSet<>();

    private WmWorkflowStateEn workflowState;

    private String comment;

//    private String subject;
    private Set<TWmReminder> reminders = new HashSet<>();

    private TPatient patient;

    private Set<TWmDocument> documents = new HashSet<>();

    private Set<TWmAction> actions = new HashSet<>();

//    private Set<TWmRequestMdk> mdkRequests = new HashSet<>();
//    public Set<TWmRequest> requests = new HashSet<>();
//    private long actualModifier;
    private Set<TWmProcessCase> cases = new HashSet<>(0);

    private Set<TWmEvent> events = new HashSet<>(0);

    private Long assignedUser;
    private Date lastModificationDate;
    private String lawerFileNumber;
    private String courtFileNumber;
    private boolean processCancellation = false;

    @Transient
    public String getProcessType() {
        if (this.getClass() != TWmProcess.class) {
            String val = this.getClass().getAnnotation(DiscriminatorValue.class).value();
            if (val == null || val.trim().isEmpty()) {
                return null;
            }
            return String.valueOf(val);
        }
        return null;
    }

    @Transient
    public WmProcessTypeEn getProcessTypeEnum() {
        return WmProcessTypeEn.findById(getProcessType());
    }

    @Transient
    public boolean isProcessHospital() {
        return this instanceof TWmProcessHospital;
    }

    @Transient
    public boolean isProcessInsurance() {
        return this instanceof TWmProcessInsurance;
    }

//    public abstract void setRequests(Set<TWmRequest> requests);
//    public abstract Set<TWmRequest> getRequests();
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_WM_PROCESS_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "process")//"hosCase")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Set<TWmProcessCase> getProcessCases() {
        return this.cases;
    }

    public void setProcessCases(final Set<TWmProcessCase> pProcessCases) {
        this.cases = pProcessCases;
    }

    /**
     * retrieve main case for process
     *
     * @return MainProcessCase
     */
    @Transient
    public TWmProcessCase getMainProcessCase() {
        Iterator<TWmProcessCase> it = this.cases.iterator();
        while (it.hasNext()) {
            TWmProcessCase processCase = it.next();
            if (processCase == null) {
                continue;
            }
            if (!processCase.getMainCase()) {
                continue;
            }
            return processCase;
        }
        return null;
    }

    /**
     * set main case for process
     *
     * @param pProcessCase process case to be set
     */
    public void setMainProcessCase(final TWmProcessCase pProcessCase) {
        if (pProcessCase == null) {
            return;
        }
        pProcessCase.setMainCase(true);
        addProcessCase(pProcessCase);
    }

    /**
     * add hospital case to process
     *
     * @param pProcessCase process case to be added
     */
    public void addProcessCase(final TWmProcessCase pProcessCase) {
        if (pProcessCase == null) {
            return;
        }
        pProcessCase.setProcess(this);
        this.cases.remove(pProcessCase);
        if (pProcessCase.getMainCase()) {
            TWmProcessCase processCase = getMainProcessCase();
            if (processCase != null && processCase != pProcessCase) {
                processCase.setMainCase(false);
            }
        }
        this.cases.add(pProcessCase);
    }

    public TWmProcessCase addCase(final TCase cs, final boolean pIsMainFl) {
        if (cs == null) {
            return null;
        }
        TWmProcessCase processCase = new TWmProcessCase();
        processCase.setHosCase(cs);
        processCase.setKisDetails(cs.getCurrentExtern());
        processCase.setMainCase(pIsMainFl);
        addProcessCase(processCase);
        return processCase;
    }

    public TWmProcessCase getProcessCase(final TCase cs) {
        if (cs == null) {
            return null;
        }
        return getProcessCase(cs.id);
    }

    public TWmProcessCase getProcessCase(final Long pCaseId) {
        if (pCaseId == null) {
            return null;
        }
        for (TWmProcessCase processCase : new HashSet<>(this.cases)) {
            if (processCase.getHosCase().id == pCaseId) {
                return processCase;
            }
        }
        return null;
    }

    /**
     * For your convenience: retrieve main case
     *
     * @return current CaseEntity set as main case
     */
    @Transient
    public TCase getMainCase() {
        TWmProcessCase processCase = getMainProcessCase();
        if (processCase == null) {
            return null;
        }
        return processCase.getHosCase();
    }

    /**
     * For your convenience: set main case
     *
     * @param pCase case to be setted as main case
     */
    public void setMainCase(final TCase pCase) {
        if (pCase == null) {
            return;
        }
        Iterator<TWmProcessCase> it = this.cases.iterator();
        while (it.hasNext()) {
            TWmProcessCase processCase = it.next();
            if (processCase == null) {
                continue;
            }
            TCase cs = processCase.getHosCase();
            if (cs != null && cs.getId() == pCase.getId()) {
                setMainProcessCase(processCase);
                return;
            }
        }
        TWmProcessCase processCase = new TWmProcessCase();
        processCase.setHosCase(pCase);
        processCase.setKisDetails(pCase.getCurrentExtern());
        setMainProcessCase(processCase);
    }

    /**
     * Gibt Nummer des Vorgangs zurück.
     *
     * @return workflowNumber
     */
    @Column(name = "WORKFLOW_NUMBER", nullable = false)
//    @GenericGenerator(name = "sequence_workflow_number", strategy = "de.lb.cpx.wm.generator.WorkflowNumberGenerator")
//    @GeneratedValue(generator = "sequence_workflow_number")  
    public long getWorkflowNumber() {
        return workflowNumber;
    }

    /**
     *
     * @return lawerFileNumber
     */
    @Column(name = "LAWER_FILE_NUMBER", length = 100, nullable = true)
    public String getLawerFileNumber() {
        return this.lawerFileNumber;
    }

    /**
     *
     * @return courtFileNumber
     */
    @Column(name = "COURT_FILE_NUMBER", length = 100, nullable = true)
    public String getCourtFileNumber() {
        return this.courtFileNumber;
    }

    /**
     * Gibt die Storno Flag
     *
     * @return processCancellation
     */
    @Column(name = "CANCEL_FL", precision = 1, scale = 0, nullable = false)
    @ColumnDefault("0")
    public boolean getProcessCancellation() {
        return this.processCancellation;
    }

    /**
     * The user who modifies the process
     *
     * @return modificationUserId
     */
    @Column(name = "PROCESS_MODIFICATION_USER", nullable = false)
    public long getProcessModificationUser() {
        return processModificationUser;
    }

    /**
     * Gibt Ereignistyp zurück, d.h. statKH, keinFall.
     *
     * @return workflowType
     */
    @Column(nullable = false, name = "WM_TYPE")
    @Enumerated(EnumType.STRING)
    public WmWorkflowTypeEn getWorkflowType() {
        return workflowType;
    }

    @ElementCollection
    @Column(name = "KEYWORD")
    @CollectionTable(name = "T_WM_KEYWORDS",
            joinColumns = @JoinColumn(name = "T_WM_PROCESS_ID"))
    public Set<String> getKeywords() {
        return keywords;
    }

    /**
     * Gibt Status des Ereignis (offen,...) zurück.
     *
     * @return workflowState
     */
    @Column(nullable = false, name = "WM_STATE")
//    @Enumerated(EnumType.STRING)
    @Convert(converter = WorkflowStateConverter.class)
    public WmWorkflowStateEn getWorkflowState() {
        return workflowState;
    }

    /**
     * Gibt Text der in diesem Eintrag steht zurück.
     *
     * @return comment
     */
    @Column(name = "PROCESS_COMMENT")
    public String getComment() {
        return comment;
    }

    @OneToMany(mappedBy = "process", cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Set<TWmReminder> getReminders() {
        return reminders;
    }

    @OneToMany(mappedBy = "process", cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Set<TWmEvent> getEvents() {
        return events;
    }

//    /**
//     * Gibt Betreff des Events zurück.
//     *
//     * @return subject
//     */
//    @Column(name = "SUBJECT", length = 128)
//    public String getSubject() {
//        return subject;
//    }
    /**
     * Gibt Verweis auf ID der Tablle T_PATIENT zurück.
     *
     * @return patient
     */
    @ManyToOne
    @JoinColumn(name = "T_PATIENT_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_WM_PROCESS4PATIENT_ID"))
    public TPatient getPatient() {
        return patient;
    }

    /*
    @OneToMany
    public Set<TWmProcessCase> getHosCase() {
        return cases;
    }
     */
    @OneToMany(mappedBy = "process")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Set<TWmDocument> getDocuments() {
        return documents;
    }

    @OneToMany(mappedBy = "process", cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Set<TWmAction> getActions() {
        return actions;
    }

//    @OneToMany(mappedBy = "process", cascade = CascadeType.ALL)
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    public Set<TWmRequestMdk> getMdkRequests() {
//        return mdkRequests;
//    }
//    @OneToMany(mappedBy = "process", cascade = CascadeType.ALL)
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    public Set<TWmRequest> getRequests() {
//        return requests;
//    }
//    @Transient
//    public long getActualModifier() {
//        return actualModifier;
//    }
    @Transient
    public Set<TWmReminder> getOpenReminders() {
        Set<TWmReminder> openReminders = new HashSet<>();
        this.getReminders().forEach((rem) -> {
            if (!rem.isFinished()) {
                openReminders.add(rem);
            }
        });
        return openReminders;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @param workflowNumber Column WORKFLOW_NUMBER:Nummer des Vorgangs .
     */
    public void setWorkflowNumber(long workflowNumber) {
        this.workflowNumber = workflowNumber;
    }

    /**
     *
     * @param courtFileNumber Column COURT_FILE_NUMBER Aktenzeichen (Gericht)
     */
    public void setCourtFileNumber(String courtFileNumber) {
        this.courtFileNumber = courtFileNumber;
    }

    /**
     *
     * @param lawerFileNumber Column LAWER_FILE_NUMBER Aktenzeichen (RA)
     * Rechtsanwalt
     */
    public void setLawerFileNumber(String lawerFileNumber) {
        this.lawerFileNumber = lawerFileNumber;
    }

    /**
     *
     * @param processCancellation Column CANCEL_FL , process Storno Flag
     */
    public void setProcessCancellation(final boolean processCancellation) {
        this.processCancellation = processCancellation;
    }

    public void setProcessModificationUser(long processModificationUser) {
        this.processModificationUser = processModificationUser;
    }

    /**
     *
     * @param type Column WM_TYPE :Ereignistyp (statKH, keinFall).
     */
    public void setWorkflowType(WmWorkflowTypeEn type) {
        this.workflowType = type;
    }

    public void setKeywords(Set<String> keywords) {
        this.keywords = keywords;
    }

    /**
     *
     * @param state Column WM_STATE: Status des Ereignis (offen,.) .
     */
    public void setWorkflowState(WmWorkflowStateEn state) {
        this.workflowState = state;
    }

    /**
     *
     * @param comment Column PROCESS_COMMENT :Text der in diesem Eintrag steht.
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

//    /**
//     *
//     * @param subject Column SUBJECT: Betreff des Events .
//     */
//    public void setSubject(String subject) {
//        this.subject = subject;
//    }
//    public void setActualModifier(long actualModifier) {
//        this.actualModifier = actualModifier;
//    }
    public void setReminders(Set<TWmReminder> reminders) {
        this.reminders = reminders;
    }

    /**
     *
     * @param patient Column T_PATIENT_ID :Verweis auf ID der Tablle T_PATIENT.
     */
    public void setPatient(TPatient patient) {
        this.patient = patient;
    }

    /*
    public void setHosCase(Set<TWmProcessCase> cases) {
        this.cases = cases;
    }
     */
    public void setDocuments(Set<TWmDocument> documents) {
        this.documents = documents;
    }

    public void setActions(Set<TWmAction> actions) {
        this.actions = actions;
    }

//    public void setMdkRequests(Set<TWmRequestMdk> mdkRequests) {
//        this.mdkRequests = mdkRequests;
//    }
//    public void setRequests(Set<TWmRequest> requests) {
//        this.requests = requests;
//    }
    public void setEvents(Set<TWmEvent> events) {
        this.events = events;
    }

    /**
     * @return get the currently assigned user to the process, is id of
     * cdb_users - due to old db values user can be null!
     */
    @Column(name = "ASSIGNED_USER", scale = 0)
    public Long getAssignedUser() {
        return assignedUser;
    }

    /**
     * @param assignedUser sets a new assigned user, should be id of cdb_users
     */
    public void setAssignedUser(Long assignedUser) {
        this.assignedUser = assignedUser;
    }

    /**
     * The latest date when process is modified with any process element
     * creation, updation or deletion.
     *
     * @return the lastModificationDate
     */
    @Column(name = "LAST_PROCESS_MODIFICATION")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getLastModificationDate() {
        return lastModificationDate == null ? null : new Date(lastModificationDate.getTime());
    }

    /**
     * @param lastModificationDate the lastModificationDate to set
     */
    public void setLastModificationDate(Date lastModificationDate) {
        this.lastModificationDate = lastModificationDate == null ? null : new Date(lastModificationDate.getTime());
    }

    @Override
    public boolean versionEquals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (!(object instanceof TWmProcess)) {
            return false;
        }
        final TWmProcess other = (TWmProcess) object;
//        if (!Objects.equals(this.processModificationUser, other.processModificationUser)) {
//            return false;
//        }
        if (!Objects.equals(this.comment, other.comment)) {
            return false;
        }
//        if (!Objects.equals(this.subject, other.subject)) {
//            return false;
//        }
        if (!Objects.equals(this.workflowNumber, other.workflowNumber)) {
            return false;
        }
        if (!Objects.equals(this.lawerFileNumber, other.lawerFileNumber)) {
            return false;
        }
        if (!Objects.equals(this.courtFileNumber, other.courtFileNumber)) {
            return false;
        }
        if (!Objects.equals(this.patient, other.patient)) {
            return false;
        }
        if (this.workflowState != other.workflowState) {
            return false;
        }
        if (this.workflowType != other.workflowType) {
            return false;
        }
        if (!Objects.equals(this.keywords, other.keywords)) {
            return false;
        }
//        if (!ModelUtil.versionSetEquals(this.keywords, other.keywords)) {
//            return false;
//        }
        /*
        if (!ModelUtil.versionSetEquals(this.cases, other.cases)) {
            return false;
        }
         */
        if (!ModelUtil.versionSetEquals(this.reminders, other.reminders)) {
            return false;
        }
        if (!ModelUtil.versionSetEquals(this.documents, other.documents)) {
            return false;
        }
        if (!ModelUtil.versionSetEquals(this.actions, other.actions)) {
            return false;
        }
//        if (!ModelUtil.versionSetEquals(this.mdkRequests, other.mdkRequests)) {
//            return false;
//        }
//        if (!ModelUtil.versionSetEquals(this.requests, other.requests)) {
//            return false;
//        }
        return true;
    }

    @Transient
    public String getWorkflowKey() {
        String workflowKey = String.valueOf(workflowNumber);
        return workflowKey;
    }

    @Transient
    public String getWorkflowSignature() {
        String workflowKey = getWorkflowKey();
        String workflowId = String.valueOf(this.id);
        return "proc_" + workflowKey + ";id_" + workflowId;
    }

    @Override
    public String toString() {
        final String hash = Integer.toHexString(this.hashCode());
        final String signature = getWorkflowSignature();
        return signature + "@" + hash;
    }

    @PrePersist
    @Override
    public void prePersist() {
        super.prePersist();
        if (id == 0 && lastModificationDate == null) {
            lastModificationDate = new Date();
        }
    }

    @PreUpdate
    @Override
    public void preUpdate() {
        super.preUpdate();
        lastModificationDate = new Date();
    }

}
