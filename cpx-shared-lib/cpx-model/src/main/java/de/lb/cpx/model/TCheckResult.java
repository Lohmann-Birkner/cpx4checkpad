/* 
 * Copyright (c) 2015 Lohmann & Birkner.
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
 *    2016  Gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.model;

import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.util.HashSet;
import java.util.Set;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * <p style="font-size:1em; color:green;"> "T_CHECK_RESULT"Tabelle der
 * Ergebnisse der Regelanwendung /Diese Ergebnisse werden nicht archiviert,
 * beziehen sich immer auf den aktuellen Zustand des Falles/ Sie werden an den
 * Grouperresult angehängt, der zu Hauptdiagnose des Falles gehört.</p>
 *
 * @author gerschmann
 */
@Entity
@Table(name = "T_CHECK_RESULT",
        indexes = {
            @Index(name = "IDX_CHECK_RESULT4GRPRES_ID ", columnList = "T_GROUPING_RESULTS_ID")})
@SuppressWarnings("serial")
public class TCheckResult extends AbstractEntity {

    private static final long serialVersionUID = 1L;
    /*
  public void setR2rChkMaxDcwPos(TRole2Result r2rChkMinDcwNeg) {
  this.r2rChkMaxDcwPos = r2rChkMinDcwNeg;
  }
  
  public void setR2rChkMinDcwNeg(TRole2Result chkR2RMinDcwNeg) {
  this.r2rChkMinDcwNeg = chkR2RMinDcwNeg;
  }
  
  public void setR2rChkMaxDfeePos(TRole2Result chkR2RMaxDfeePos) {
  this.r2rChkMaxDfeePos = chkR2RMaxDfeePos;
  }
  
  public void setR2rChkMinDfeeNeg(TRole2Result chkR2RMinDfeeNeg) {
  this.r2rChkMinDfeeNeg = chkR2RMinDfeeNeg;
  }
  
  /*  @Override
  public int hashCode() {
  int hash = 3;
  hash = 89 * hash + (int) (this.id ^ (this.id >>> 32));
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
  final TCheckResult other = (TCheckResult) obj;
  if (this.id != other.id) {
  return false;
  }
  return true;
  }
     */

//    private long id;
    private TGroupingResults groupingResults;
    private long ruleid;
    private double chkCwSimulDiff;
    private double chkCwCareSimulDiff;
    private double chkFeeSimulDiff;
    private String chkDrg;
    private String chkReferences;
    private Set<TRole2Check> role2check = new HashSet<>();
    private Double chkRskAuditVal;
    private Double chkRskWasteVal;
    private Integer chkRskAuditPercentVal;
    private Integer chkRskWastePercentVal;
    private Double chkRskDefaultWasteVal;
    

    /*    private TRole2Result r2rChkMaxDcwPos;// ändern auf double
    private TRole2Result r2rChkMinDcwNeg;
    private TRole2Result r2rChkMaxDfeePos;
    private TRole2Result r2rChkMinDfeeNeg;*/
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_CHECK_RESULTS_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return id;
    }

    /**
     * Gibt Verweis auf die Tablle T_GROUPING_RESULTS zurück .
     *
     * @return groupingResults
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "T_GROUPING_RESULTS_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_CHECK_RESULT4GRPRES_ID"))
    public TGroupingResults getGroupingResults() {
        return groupingResults;
    }

    /**
     * Gibt Verweis auf die cpx_common.CRG_RULES zurück.d.h. RID Attrubut in der
     * Regelxml - Datei
     *
     * @return ruleid
     */
    @Column(name = "CRGR_ID")
    public long getRuleid() {
        return ruleid;
    }

    /**
     * Gibt CW-Änderung bei der Ausführung des Regelvorschlags zurück.
     *
     * @return chkCwSimulDiff
     */
    @Column(name = "CHK_CW_SIMUL_DIFF", precision = 4, scale = 0)
    public double getChkCwSimulDiff() {
        return chkCwSimulDiff;
    }

    /**
     * Gibt CW-Pflege - Änderung bei der Ausführung des Regelvorschlags zurück.
     *
     * @return chkCwSimulDiff
     */
    @Column(name = "CHK_CW_CARE_SIMUL_DIFF", precision = 4, scale = 0)
    public double getChkCwCareSimulDiff() {
        return chkCwCareSimulDiff;
    }

    /**
     * Gibt Entgeltänderung in Euro bei der Ausführung des Regelvorschlags
     * zurück.
     *
     * @return chkFeeSimulDiff
     */
    @Column(name = "CHK_FEE_SIMUL_DIFF",  precision = 10, scale = 2)
    public double getChkFeeSimulDiff() {
        return chkFeeSimulDiff;
    }

    /**
     * Gibt DRG/PEPP Wert der Simulation für diese Regel zurück.
     *
     * @return chkDrg
     */
    @Column(name = "CHK_SIMUL_DRG", length = 10)
    public String getChkDrg() {
        return chkDrg;
    }

    /**
     * @return chkDrg
     */
    @Column(name = "CHK_SIMUL_REFERENCES", length = 800)
    public String getChkReferences() {
        return chkReferences;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "checkResult"/*, orphanRemoval = true*/)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Set<TRole2Check> getRole2Check() {
        return role2check;
    }

    /*                                                                         
  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "r2rChkMaxDcwPos")
  @OnDelete(action=OnDeleteAction.CASCADE)
    public TRole2Result getR2rChkMaxDcwPos() {
        return r2rChkMaxDcwPos;
    }

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "r2rChkMinDcwNeg")
  @OnDelete(action=OnDeleteAction.CASCADE)
    public TRole2Result getR2rChkMinDcwNeg() {
        return r2rChkMinDcwNeg;
    }

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "r2rChkMaxDfeePos")
  @OnDelete(action=OnDeleteAction.CASCADE)
    public TRole2Result getR2rChkMaxDfeePos() {
        return r2rChkMaxDfeePos;
    }

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "r2rChkMinDfeeNeg")
  @OnDelete(action=OnDeleteAction.CASCADE)
    public TRole2Result getR2rChkMinDfeeNeg() {
        return r2rChkMinDfeeNeg;
    }

     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @param groupingResults Column T_GROUPING_RESULTS_ID :Verweis auf die
     * Tablle T_GROUPING_RESULTS .
     */
    public void setGroupingResults(TGroupingResults groupingResults) {
        this.groupingResults = groupingResults;
    }

    /**
     *
     * @param ruleid Column CRGR_ID :Verweis auf die cpx_common.CRG_RULES / RID
     * Attrubut in der Regelxml - Datei .
     */
    public void setRuleid(long ruleid) {
        this.ruleid = ruleid;
    }

    /**
     *
     * @param chkCwSimulDiff Column CHK_CW_SIMUL_DIFF : CW-Änderung bei der
     * Ausführung des Regelvorschlags.
     */
    public void setChkCwSimulDiff(double chkCwSimulDiff) {
        this.chkCwSimulDiff = chkCwSimulDiff;
    }

    /**
     *
     * @param chkCwSimulDiff Column CHK_CW_CARE_SIMUL_DIFF : CW-Pflege- Änderung bei der
     * Ausführung des Regelvorschlags.
     */
    public void setChkCwCareSimulDiff(double chkCwSimulDiff) {
        this.chkCwCareSimulDiff = chkCwSimulDiff;
    }

    /**
     *
     * @param chkFeeSimulDiff Column CHK_FEE_SIMUL_DIFF :Entgeltänderung in Euro
     * bei der Ausführung des Regelvorschlags.
     */
    public void setChkFeeSimulDiff(double chkFeeSimulDiff) {
        this.chkFeeSimulDiff = chkFeeSimulDiff;
    }

    /**
     *
     * @param chkDrg Column CHK_SIMUL_DRG : DRG/PEPP Wert der Simulation für
     * diese Regel.
     */
    public void setChkDrg(String chkDrg) {
        this.chkDrg = chkDrg;
    }

    public void setRole2Check(Set<TRole2Check> role2check) {
        this.role2check = role2check;
    }

    /**
     * @param chkReferences references for the rule
     */
    public void setChkReferences(String chkReferences) {
        this.chkReferences = chkReferences;
    }

    /**
     * calculated Risk for CASE_DETAILS configuration and this detected rule
     * in case of audit
     * @return chkRskAuditVal
     */
    @Column(name = "CHK_RSK_AUDIT_VAL",  precision = 10, scale = 2)
    public Double getChkRskAuditVal() {
        return chkRskAuditVal;
    }

    public void setChkRskAuditVal(Double chkRskAuditVal) {
        this.chkRskAuditVal = chkRskAuditVal;
    }

    /**
     * calculated Risk for CASE_DETAILS configuration and this detected rule
     * for billing of this  case version
     * @return chkRskWasteVal
     */
    @Column(name = "CHK_RSK_WASTE_VAL",  precision = 10, scale = 2)
    public Double getChkRskWasteVal() {
        return chkRskWasteVal;
    }

    public void setChkRskWasteVal(Double chkRskWasteVal) {
        this.chkRskWasteVal = chkRskWasteVal;
    }

    /**
     * applied percent value for calculating ofRisk 
     * for CASE_DETAILS configuration and this detected rule
     * in case of audit
     * @return chkRskAuditPercentVal
     */
    @Column(name = "CHK_RSK_AUDIT_PERCENT_VAL", precision = 2, scale = 0)
    public Integer getChkRskAuditPercentVal() {
        return chkRskAuditPercentVal;
    }

    public void setChkRskAuditPercentVal(Integer chkRskAuditPercentVal) {
        this.chkRskAuditPercentVal = chkRskAuditPercentVal;
    }


    /**
     * applied percent value for calculating ofRisk 
     * for CASE_DETAILS configuration and this detected rule
     * for billing
     * @return chkRskAuditPercentVal
     */
    @Column(name = "CHK_RSK_WASTE_PERCENT_VAL", precision = 2, scale = 0)
    public Integer getChkRskWastePercentVal() {
        return chkRskWastePercentVal;
    }

    public void setChkRskPercentVal(Integer chkRskPercentVal) {
        this.chkRskWastePercentVal = chkRskPercentVal;
    }

    @Column(name = "CHK_RSK_DEFAULT_WASTE_VAL",  precision = 10, scale = 2, nullable = true)
    public Double getChkRskDefaultWasteVal() {
        return chkRskDefaultWasteVal;
    }

    public void setChkRskDefaultWasteVal(Double chkRskDefaultWasteVal) {
        this.chkRskDefaultWasteVal = chkRskDefaultWasteVal;
    }

}
