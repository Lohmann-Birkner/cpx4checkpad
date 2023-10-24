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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import de.lb.cpx.model.converter.IcdcRefTypeConverter;
import de.lb.cpx.model.converter.IcdcTypeConverter;
import de.lb.cpx.model.enums.IcdcRefTypeEn;
import de.lb.cpx.model.enums.IcdcTypeEn;
import de.lb.cpx.model.enums.LocalisationEn;
import de.lb.cpx.server.commons.dao.AbstractCaseEntity;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.CascadeType;
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
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.Hibernate;
import org.hibernate.LazyInitializationException;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

/**
 * TCaseIcd initially generated at 21.01.2016 17:07:59 by Hibernate Tools
 * 3.2.2.GA
 * <p style="font-size:1em; color:green;">"T_CASE_ICD" ist Tabelle der
 * Diagnosen. </p>
 */
@Entity
@Table(name = "T_CASE_ICD",
        indexes = {
            @Index(name = "IDX_ICD_DEPC_ID", columnList = "T_CASE_DEPARTMENT_ID", unique = false),
            @Index(name = "IDX_CASE_ICD4ICDC_REF_ID", columnList = "T_CASE_ICD_ID", unique = false),
            @Index(name = "IDX_CASE_ICD4WARDC_ID", columnList = "T_CASE_WARD_ID", unique = false)})
@SuppressWarnings("serial")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id_intern")
public class TCaseIcd extends AbstractCaseEntity {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(TCaseIcd.class.getName());

//    private long id;
    private TCaseWard caseWard;
    private TCaseIcd refIcd;
    private TCaseDepartment caseDepartment;
    private String icdcCode;
    private boolean icdcIsHdxFl = false;
    private boolean icdcIsHdbFl = false;
    private IcdcTypeEn icdcTypeEn;
    private IcdcRefTypeEn icdcReftypeEn;
    private LocalisationEn icdcLocEn = LocalisationEn.E;
    private boolean icdcIsToGroupFl = true;

    private Set<TCaseIcd> refIcds = new HashSet<>(0);
    private Set<TGroupingResults> groupingResultses = new HashSet<>(0);
    private Set<TCaseIcdGrouped> caseIcdGroupeds = new HashSet<>(0);

    public TCaseIcd() {
    
        setIgnoreFields();
    }
    
    public TCaseIcd(Long pCurrentUser){
        super(pCurrentUser);
        setIgnoreFields();
    }
    

    @Override
    protected final void setIgnoreFields() {
        ignoredFields = new String[]{
            "caseWard",
            "caseDepartment",
            "refIcd",
            "refIcds",
            "groupingResultses",
            "caseIcdGroupeds",
            "icdcReftypeEn"
        };
    }
    /**
     * Gibt Verweis auf die Tabelle T_CASE_DEPARTMENT zurück , wenn die Diagnose
     * dieser Abteilung zugeordnet wurde.
     *
     * @return caseDepartment
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "T_CASE_DEPARTMENT_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_CASE_ICD4CASE_DEPARTMENT_ID"))
    @JsonBackReference(value = "icd")
    public TCaseDepartment getCaseDepartment() {
        return this.caseDepartment;
    }

    /**
     * Gibt zurück.
     *
     * @return refIcd: Verweis auf die Tabelle T_CASE_ICD .
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "T_CASE_ICD_ID", nullable = true, foreignKey = @ForeignKey(name = "FK_CASE_ICD4ICDC_REF_ID"))
    @JsonBackReference
    public TCaseIcd getRefIcd() {
        return this.refIcd;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "caseIcd")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Set<TCaseIcdGrouped> getCaseIcdGroupeds() {
        return this.caseIcdGroupeds;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "refIcd")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JsonManagedReference
    public Set<TCaseIcd> getRefIcds() {
        return this.refIcds;
    }

    /**
     * Gibt Verweis auf die Tabelle T_CASE_WARD zurück, wenn die Diagnose dieser
     * Station zugeordnet wurde.
     *
     * @return caseWard
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "T_CASE_WARD_ID", nullable = true, foreignKey = @ForeignKey(name = "FK_CASE_ICD4T_CASE_WARD_ID"))
    public TCaseWard getCaseWard() {
        return this.caseWard;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "caseIcd")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Set<TGroupingResults> getGroupingResultses() {
        return this.groupingResultses;
    }

    /**
     * Gibt icdcCode zurück, wie es in der Tabelle C_ICD_CATALOG steht .
     *
     * @return icdcCode
     */
    @Column(name = "ICDC_CODE", nullable = false, length = 10)
    public String getIcdcCode() {
        return this.icdcCode;
    }

    /**
     * Gibt Flg 1/0 zurück,indem Hauptdiagnose der Bewegung mit 1 Markiert
     * wurde.
     *
     * @return icdcIsHdbFl
     */
    @Column(name = "MAIN_DIAG_DEP_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean getIcdcIsHdbFl() {
        return this.icdcIsHdbFl;
    }

    /**
     * Gibt Flg 1/0 zurück , indem Hauptdiagnose des Falles mit 1 Markiert
     * wurde.
     *
     * @return icdcIsHdxFl
     */
    @Column(name = "MAIN_DIAG_CASE_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean getIcdcIsHdxFl() {
        return this.icdcIsHdxFl;
    }

    /**
     * Gibt Diagnosenreferenztyp 1-4 zurück,z.b. Kreuz,Stern,Zusatz,ZusatzZu
     *
     * @return icdcReftypeEn
     */
    @Column(name = "ICD_REFERENCE_EN", length = 20)
    @Convert(converter = IcdcRefTypeConverter.class)
    public IcdcRefTypeEn getIcdcReftypeEn() {
        return this.icdcReftypeEn;
    }

    /**
     * Gibt Diagnosentyp 01-18 zurück.z.b. Einweisung,Aufnahme,Verlegung,..
     *
     * @return icdcTypeEn
     */
    @Column(name = "ICDC_TYPE_EN", length = 2)
    @Convert(converter = IcdcTypeConverter.class)
    public IcdcTypeEn getIcdcTypeEn() {
        return this.icdcTypeEn;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_CASE_ICD_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    /**
     * Gibt Lokalisation 0-3 zurück. z.b. E,R,L,Z
     *
     * @return icdcLocEn:
     */
    @Column(name = "ICDC_LOC_EN", length = 1, nullable = false)
    @Enumerated(EnumType.STRING)
    public LocalisationEn getIcdcLocEn() {
        return icdcLocEn;
    }

    /**
     * Gibt Flag 0/1 zurück, indem Diagnose als für Groupen zu verwenden
     * markiert wurde.
     *
     * @return icdcIsToGroupFl
     */
    @Column(name = "TO_GROUP_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean getIcdIsToGroupFl() {
        return icdcIsToGroupFl;
    }

    public void setIcdIsToGroupFl(final boolean b) {
        icdcIsToGroupFl = b;
    }

    /**
     *
     * @param icdcLocEn Column ICDC_LOC_EN: Lokalisation 0-3(E,R,L,B)
     */
    public void setIcdcLocEn(LocalisationEn icdcLocEn) {
        this.icdcLocEn = icdcLocEn;
    }

    /**
     *
     * @param caseDepartment Column T_CASE_DEPARTMENT_ID: Verweis auf die
     * Tabelle T_CASE_DEPARTMENT, wenn die Diagnose dieser Abteilung zugeordnet
     * wurde.
     */
    public void setCaseDepartment(final TCaseDepartment caseDepartment) {
        this.caseDepartment = caseDepartment;
    }

    /**
     *
     * @param caseIcd Column T_CASE_ICD_ID: Verweis auf die Tabelle T_CASE_ICD.
     */
    public void setRefIcd(final TCaseIcd caseIcd) {
        this.refIcd = caseIcd;
    }

    public void setCaseIcdGroupeds(final Set<TCaseIcdGrouped> caseIcdGroupeds) {
        this.caseIcdGroupeds = caseIcdGroupeds;
    }

    public void setRefIcds(final Set<TCaseIcd> caseIcds) {
        this.refIcds = caseIcds;
    }

    /**
     *
     * @param caseWard Column T_CASE_WARD_ID: Verweis auf die Tabelle
     * T_CASE_WARD, wenn die Diagnose dieser Station zugeordnet wurde.
     */
    public void setCaseWard(final TCaseWard caseWard) {
        this.caseWard = caseWard;
    }

    public void setGroupingResultses(final Set<TGroupingResults> groupingResultses) {
        this.groupingResultses = groupingResultses;
    }

    /**
     *
     * @param icdcCode Column ICDC_CODE: wie es in der Tabelle
     * cpx-common.C_ICD_CATALOG steht .
     */
    public void setIcdcCode(final String icdcCode) {
        this.icdcCode = icdcCode;
    }

    /**
     *
     * @param icdcIsHdbFl Column MAIN_DIAG_CASE_FL: Markiert Hauptdiagnose der
     * Bewegung mit 1.
     */
    public void setIcdcIsHdbFl(final boolean icdcIsHdbFl) {
        this.icdcIsHdbFl = icdcIsHdbFl;
    }

    /**
     *
     * @param icdcIsHdxFl :Markiert Hauptdiagnose des Falles mit 1.
     */
    public void setIcdcIsHdxFl(final boolean icdcIsHdxFl) {
        this.icdcIsHdxFl = icdcIsHdxFl;
        if (caseDepartment != null) {
            TCaseDetails caseDetails = caseDepartment.getCaseDetails();
            if (caseDetails != null) {
                if (this.icdcIsHdxFl && Hibernate.isInitialized(caseDetails)) {
//                    caseDetails.setID_ICD_HDX(getId());
                    try {
                        caseDetails.setHdIcdCode(getIcdcCode());
                    } catch (LazyInitializationException ex) {
                        LOG.log(Level.INFO, "Was not able to set main icd '" + getIcdcCode() + "' with id " + id + " in case details", ex);
                    }
                }
            }
        }

    }

    /**
     *
     * @param icdcReftypeEn Column ICD_REFERENCE_EN: Diagnosenreferenztyp 1-4
     * (Kreuz,Stern,Zusatz,ZusatzZu).
     */
    public void setIcdcReftypeEn(final IcdcRefTypeEn icdcReftypeEn) {
        this.icdcReftypeEn = icdcReftypeEn;
    }

    /**
     *
     * @param icdcTypeEn COlumn ICDC_TYPE_EN: Diagnosentyp
     * 01-18(Einweisung,Aufnahme,Verlegung,..)
     */
    public void setIcdcTypeEn(final IcdcTypeEn icdcTypeEn) {
        this.icdcTypeEn = icdcTypeEn;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {

        TCaseIcd cloned = (TCaseIcd) super.clone();
        return cloned;
    }

    @Override
    public Object cloneWithoutIds(Long currentCpxUserId) throws CloneNotSupportedException {

        TCaseIcd cloned = (TCaseIcd) super.cloneWithoutIds(currentCpxUserId);
        cloned.setId(0);
        cloned.setCaseIcdGroupeds(new HashSet<>(0));
        cloned.setGroupingResultses(new HashSet<>(0));
        cloned.setRefIcds(new HashSet<>(0));
        return cloned;
    }

    @Override
    public boolean versionEquals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TCaseIcd)) {
            return false;
        }
        final TCaseIcd other = (TCaseIcd) obj;
        if (this.icdcIsHdxFl != other.getIcdcIsHdxFl()) {
            return false;
        }
        if (this.icdcIsHdbFl != other.getIcdcIsHdbFl()) {
            return false;
        }
        if (this.icdcIsToGroupFl != other.getIcdIsToGroupFl()) {
            return false;
        }
        if (!isStringEquals(this.icdcCode, other.getIcdcCode())) {
            return false;
        }
        if (!Objects.equals(this.icdcTypeEn, other.getIcdcTypeEn())) {
            return false;
        }
        if (!Objects.equals(this.icdcLocEn, other.getIcdcLocEn())) {
            return false;
        }
        return true;
    }

    /**
     * tests equality for simulation in client equals in this case means equal
     * in code and locEn
     *
     * @param obj to test
     * @return indicator if is equals
     */
    public boolean simulationEquals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TCaseIcd)) {
            return false;
        }
        final TCaseIcd other = (TCaseIcd) obj;
        if (!isStringEquals(this.icdcCode, other.getIcdcCode())) {
            return false;
        }
        if (!Objects.equals(this.icdcLocEn, other.getIcdcLocEn())) {
            return false;
        }
        return true;
    }

    /**
     * Diese Methode wird aufgerufen, bevor das Entity gelöscht wird. um
     * Selbstreferenz auf andere Diagnosen aufzuheben ( dagger diagnose,
     * asterisk diagnosen)
     *
     */
    //Resolve self-reference to other diagnoses (dagger diagnosis, asterisk diagnoses)
    @PreRemove
    public void removeIcd() {
        //AWi 20170526: clean up references to enable removing via entity manager
        //without cleanup hibernate could not delete the entity 
        setCaseWard(null);
        setGroupingResultses(null);
        setCaseIcdGroupeds(null);

//        getCaseDepartment().getCaseIcds().removeIf(new Predicate<TCaseIcd>() {
//            @Override
//            public boolean test(TCaseIcd t) {
//                return t.equals(TCaseIcd.this);
//            }
//        });
        LOG.log(Level.INFO, "PreRemove in Icd: {0}", getIcdcCode());
//        Iterator<TCaseIcd> itDep = getCaseDepartment().getCaseIcds().iterator();
//        while (itDep.hasNext()) {
//            TCaseIcd next = itDep.next();
//            if (next.equals(this)) {
//                LOG.log(Level.INFO, "Remove icd:{0} from Department: {1}", new Object[]{getIcdcCode(), getCaseDepartment().getDepShortName()});
//                itDep.remove();
//            }
//        }
        Iterator<TCaseIcd> it = getRefIcds().iterator();
        while (it.hasNext()) {
            TCaseIcd icd = it.next();
            if (icd == null) {
                continue;
            }
            icd.setRefIcd(null);
        }
        TCaseDetails caseDetails = caseDepartment.getCaseDetails();
        Integer sum_sd = caseDetails.getSumOfIcd();
        if (sum_sd == null) {
            sum_sd = 0;
        }
        caseDetails.setSumOfIcd((sum_sd - 1));
    }

    @PrePersist
    @Override
    public void prePersist() {
        TCaseDetails caseDetails = caseDepartment.getCaseDetails();
        if (!this.icdcIsHdxFl && caseDetails.getCsdIsActualFl()) {

            Integer sum_sd = caseDetails.getSumOfIcd();
            if (sum_sd == null) {
                caseDetails.setSumOfIcd(0);
                sum_sd = 0;
            }
            caseDetails.setSumOfIcd((sum_sd + 1));
        }
    }

    @PreUpdate
    @Override
    public void preUpdate() {
        if (caseDepartment != null) {
            TCaseDetails caseDetails = caseDepartment.getCaseDetails();
            if (caseDetails != null) {
                if (this.icdcIsHdxFl) {
//                    caseDetails.setID_ICD_HDX(getId());
                    caseDetails.setHdIcdCode(getIcdcCode());
                }
            }
        }
    }
}
