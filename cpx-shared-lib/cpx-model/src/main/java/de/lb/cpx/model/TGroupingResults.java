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

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.GroupResultPdxEn;
import de.lb.cpx.model.enums.GrouperMdcOrSkEn;
import de.lb.cpx.model.enums.GrouperStatusEn;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
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
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

/**
 * TGroupingResults initially generated at 21.01.2016 17:07:59 by Hibernate
 * Tools 3.2.2.GA
 * <p style="font-size:1em; color:green;"> Die Tabelle "T_GROUPING_RESULTS"
 * speichert die statistischen Ergebnisse des Grouping.</p>
 */
@Entity
@Table(name = "T_GROUPING_RESULTS",
        indexes = {
            @Index(name = "IDX_GRP_RES4TCASE_DETAIL_ID", columnList = "T_CASE_DETAILS_ID", unique = false),
            @Index(name = "IDX_GROUPING_RESULTS4ICDC_ID", columnList = "T_CASE_ICD_ID", unique = false),
            @Index(name = "IDX_GROUPING_RESULTS4MODEL_ID", columnList = "MODEL_ID_EN", unique = false)})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "GRPRES_TYPE_EN", discriminatorType = DiscriminatorType.STRING)
@SuppressWarnings("serial")

@NamedEntityGraph(
        name = "groupingResultGraph",
        attributeNodes = {
            @NamedAttributeNode(value = "caseIcdGroupeds", subgraph = "icdGraph"),
            @NamedAttributeNode(value = "caseOpsGroupeds", subgraph = "opsGraph"),
            @NamedAttributeNode(value = "checkResults"),
            @NamedAttributeNode(value = "caseIcd")
        },
        subgraphs = {
            @NamedSubgraph(name = "icdGraph", attributeNodes = {
        @NamedAttributeNode("caseIcd")
    }),
            @NamedSubgraph(name = "opsGraph", attributeNodes = {
        @NamedAttributeNode("caseOps")
    })
        }
)

public class TGroupingResults extends AbstractEntity {

    private static final long serialVersionUID = 1L;

//  private long id;
    private CaseTypeEn grpresType;
    private TCaseIcd caseIcd;
    private TCaseDetails caseDetails;
    private GDRGModel modelIdEn;
    private int grpresPccl;
    private GrouperStatusEn grpresGst;
    private GroupResultPdxEn grpresGpdx;
    private String grpresCode;
    private GrouperMdcOrSkEn grpresGroup;
    private boolean grpresIsAutoFl;
    private boolean grpresIsNegotiatedFl = false;
    private boolean grpresIsDayCareFl = false;
    private Set<TCaseIcdGrouped> caseIcdGroupeds = new HashSet<>(0);
    private Set<TCaseOpsGrouped> caseOpsGroupeds = new HashSet<>(0);
    private Set<TCheckResult> checkResults = new HashSet<>(0);
    private Set<TRole2Result> role2Result = new HashSet<>(0);
//  private TCaseDrg caseDrg;
//  private TCasePepp casePepp;
    private long calculatedLengthOfStay;
    private long calculatedLeave;
//  private TCaseMergeMapping caseMergeMapping;

    @Transient
    public String getDiscrimantorType() {
        if (this.getClass() != TGroupingResults.class) {
            String val = this.getClass().getAnnotation(DiscriminatorValue.class).value();
            if (val == null || val.trim().isEmpty()) {
                return null;
            }
            //return String.valueOf(val);
            return val;
        }
        return null;
    }

    @Transient
    public boolean isDiscrimnatorPepp() {
        return "PEPP".equalsIgnoreCase(getDiscrimantorType());
    }

    @Transient
    public boolean isDiscrimnatorDrg() {
        return "DRG".equalsIgnoreCase(getDiscrimantorType());
    }

    @Transient
    public TCaseDrg getCaseDrg() {
        if (isDiscrimnatorDrg()) {
            return (TCaseDrg) this;
        }
        return null;
    }

    @Transient
    public TCasePepp getCasePepp() {
        if (isDiscrimnatorPepp()) {
            return (TCasePepp) this;
        }
        return null;
    }

    /**
     * Gibt Verweis auf die Tablle T_CASE_DETAILS zurück.
     *
     * @return caseDetails
     */
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "T_CASE_DETAILS_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_GRP_RES4TCASE_DETAIL_ID"))
    public TCaseDetails getCaseDetails() {
        return this.caseDetails;
    }

    /**
     *
     * @return caseIcd: Verweis auf die Tablle T_CASE_ICD, die als Hautduagnose
     * für diesen Groupingergebnis verwendet wurde.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "T_CASE_ICD_ID", nullable = true, foreignKey = @ForeignKey(name = "FK_GRP_RES4T_CASE_ICD_ID"))
    public TCaseIcd getCaseIcd() {
        return this.caseIcd;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "groupingResults"/*, orphanRemoval = true)*/)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Set<TCheckResult> getCheckResults() {
        return this.checkResults;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "groupingResults"/*, orphanRemoval = true)*/)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Set<TRole2Result> getRole2Results() {
        return this.role2Result;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "groupingResults"/*, orphanRemoval = true)*/)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Set<TCaseIcdGrouped> getCaseIcdGroupeds() {
        return this.caseIcdGroupeds;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "groupingResults"/*, orphanRemoval = true*/)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Set<TCaseOpsGrouped> getCaseOpsGroupeds() {
        return this.caseOpsGroupeds;
    }

    /**
     * Gibt DRG oder PEPP - Code zurück , der von Grouper ermittelt .
     *
     * @return grpresCode
     */
    @Column(name = "GRPRES_CODE", length = 10)
    public String getGrpresCode() {
        return this.grpresCode;
    }

    /**
     * Gibt Fehlerinfo für Hauptdiagnose 0-3 zurück. z.b pdx0:Gültige
     * Hauptdiagnose ,pdx1:Ungültiger ICD-10-GM-Kode,pdx2:V-,W-,X-,Y-Kode als
     * Hauptdiagnose,pdx3:Unzulässige Hauptdiagnose .
     *
     * @return grpresGpdx:
     */
    @Column(name = "GRPRES_GPDX", length = 25)
    @Enumerated(EnumType.STRING)
    public GroupResultPdxEn getGrpresGpdx() {
        return this.grpresGpdx;
    }

    /**
     * Gibt MDC oder SK zurück, die beim Groupen ermittelt .
     *
     * @return grpresGroup
     */
    @Column(name = "GRPRES_GROUP", length = 25)
    @Enumerated(EnumType.STRING)
    public GrouperMdcOrSkEn getGrpresGroup() {
        return this.grpresGroup;
    }

    /**
     * Gibt Grouperstatus 0-8 zurück,die beim Groupen ermittelt wird.d.h. (GST00
     * - GST08)
     *
     * @return grpresGst :
     */
    @Column(name = "GRPRES_GST", length = 25)
    @Enumerated(EnumType.STRING)
    public GrouperStatusEn getGrpresGst() {
        return this.grpresGst;
    }

    /**
     * Gibt Flag 1/0 zurück.Es ist 1, wenn automatische Grouper augewendet
     * wurde.
     *
     * @return grpresIsAutoFl
     */
    @Column(name = "GRPRES_IS_AUTO_FL", precision = 1, scale = 0)
    @Type(type = "numeric_boolean")
    public boolean getGrpresIsAutoFl() {
        return this.grpresIsAutoFl;
    }

    /**
     * Gibt PCCL des Falles zurück.
     *
     * @return grpresPccl
     */
    @Column(name = "GRPRES_PCCL", precision = 1, scale = 0)
    public int getGrpresPccl() {
        return this.grpresPccl;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_GROUPING_RESULTS_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    /**
     * Gibt Groupermodell zurück.z.b.AUTOMATIC, GDRG20112013,.,GDRG2017,..
     *
     * @return modelIdEn
     */
    @Column(name = "MODEL_ID_EN", nullable = false, length = 25)
    @Enumerated(EnumType.STRING)
    public GDRGModel getModelIdEn() {
        return modelIdEn;
    }

    @Column(name = "NEGOTIATED_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean isGrpresIsNegotiatedFl() {
        return grpresIsNegotiatedFl;
    }

    public void setGrpresIsNegotiatedFl(boolean flag) {
        grpresIsNegotiatedFl = flag;
    }

    @Column(name = "DAY_CARE_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean isGrpresIsDayCareFl() {
        return grpresIsDayCareFl;
    }

    public void setGrpresIsDayCareFl(boolean flag) {
        grpresIsDayCareFl = flag;
    }

    /*  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "groupingResults")
  public TCaseDrg getCaseDrg() {
    return caseDrg;
  }

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "groupingResults")
  public TCasePepp getCasePepp() {
    return this.casePepp;
  }
     */
    public void setCaseDetails(final TCaseDetails caseDetails) {
        this.caseDetails = caseDetails;
    }

    /*
  public void setCaseDrg(final TCaseDrg caseDrg) {
    this.caseDrg = caseDrg;
  }
     */
    /**
     *
     * @param caseIcd Column T_CASE_ICD_ID: Verweis auf die Tablle T_CASE_ICD
     */
    public void setCaseIcd(final TCaseIcd caseIcd) {
        this.caseIcd = caseIcd;
    }

    public void setCheckResults(final Set<TCheckResult> checkResults) {
        this.checkResults = checkResults;
    }

    public void setRole2Results(final Set<TRole2Result> role2Results) {
        this.role2Result = role2Results;
    }

    public void setCaseIcdGroupeds(final Set<TCaseIcdGrouped> caseIcdGroupeds) {
        this.caseIcdGroupeds = caseIcdGroupeds;
    }

    public void setCaseOpsGroupeds(final Set<TCaseOpsGrouped> caseOpsGroupeds) {
        this.caseOpsGroupeds = caseOpsGroupeds;
    }

    /*
  public void setCasePepp(final TCasePepp casePepp) {
    this.casePepp = casePepp;
  }
     */
    /**
     *
     * @param grpresCode Column GRPRES_CODE: DRG oder PEPP - Code, der von
     * Grouper ermittelt .
     */
    public void setGrpresCode(final String grpresCode) {
        this.grpresCode = grpresCode;
    }

    /**
     *
     * @param grpresGpdx Column GRPRES_GPDX : Enumeration für Fehlerinfo für
     * Hauptdiagnose
     */
    public void setGrpresGpdx(final GroupResultPdxEn grpresGpdx) {
        this.grpresGpdx = grpresGpdx;
    }

    /**
     *
     * @param grpresGroup Column GRPRES_GROUP :Enumeration für MDC oder SK,
     * ermittelt beim Groupen.
     */
    public void setGrpresGroup(final GrouperMdcOrSkEn grpresGroup) {
        this.grpresGroup = grpresGroup;
    }

    /**
     *
     * @param grpresGst Column GRPRES_GST : Enumeration für Grouperstatus 0-8
     * (GST00 - GST08),die beim Groupen ermittelt wird.
     */
    public void setGrpresGst(final GrouperStatusEn grpresGst) {
        this.grpresGst = grpresGst;
    }

    /**
     *
     * @param grpresIsAutoFl Column GRPRES_IS_AUTO_FL :Flag 1/0, =1, wenn
     * automatische Grouper augewendet wurde.
     */
    public void setGrpresIsAutoFl(final boolean grpresIsAutoFl) {
        this.grpresIsAutoFl = grpresIsAutoFl;
    }

    /**
     *
     * @param grpresPccl Column GRPRES_PCCL :PCCL des Falles.
     */
    public void setGrpresPccl(final int grpresPccl) {
        this.grpresPccl = grpresPccl;
    }

    public void setId(final long id) {
        this.id = id;
    }

    /**
     *
     * @param modelIdEn Column MODEL_ID_EN :Enumeration für Groupermodell
     * (AUTOMATIC, GDRG20112013,.,GDRG2017,..) .
     */
    public void setModelIdEn(final GDRGModel modelIdEn) {
        this.modelIdEn = modelIdEn;
    }

    public void setGrpresType(CaseTypeEn type) {
        this.grpresType = type;
    }

    /**
     *
     * @return grpresType: Enumeration für Art der Diagnose 0-5(OTHER, DRG,
     * PEPP, PSY, PIA ,AmbuOP ,vorstatAbbrecher ) .
     */
    @Column(name = "GRPRES_TYPE_EN", length = 25, insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    public CaseTypeEn getGrpresType() {
        return grpresType;
    }

//
//  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "grpres")
//  @OnDelete(action=OnDeleteAction.CASCADE)
//  public TCaseMergeMapping getCaseMergeMapping() {
//    return this.caseMergeMapping;
//  }
    /*
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TGroupingResults other = (TGroupingResults) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }*/
    @Transient
    public long getCalculatedLengthOfStay() {
        return calculatedLengthOfStay;
    }

    public void setCalculatedLengthOfStay(long calculatedLengthOfStay) {
        this.calculatedLengthOfStay = calculatedLengthOfStay;
    }

    @Transient
    public long getCalculatedLeave() {
        return calculatedLeave;
    }

    public void setCalculatedLeave(long calculatedLeave) {
        this.calculatedLeave = calculatedLeave;
    }

    /**
     * clear rules and remove link to grouping result to ensure deleting in
     * transaction should be used on server, may not effect automatic changes on
     * the server side = Old results might still live and new results will be
     * added in the list
     */
    public void clearRules() {
//        for(TRole2Result role : role2Result){
//            role.setGroupingResults(null);
//        }
        role2Result.clear();
        checkResults.clear();
        setRole2Results(new HashSet<>());
        setCheckResults(new HashSet<>());
////        for(TCheckResult result : checkResults){
////            result.setGroupingResults(null);
////        }
    }

}
