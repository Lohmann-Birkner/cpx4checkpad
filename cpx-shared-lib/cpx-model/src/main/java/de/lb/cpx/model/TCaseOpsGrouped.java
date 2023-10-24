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

import de.lb.cpx.server.commons.dao.AbstractEntity;
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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

/**
 * TCaseOpsGrouped initially generated at 21.01.2016 17:07:59 by Hibernate Tools
 * 3.2.2.GA
 * <p style="font-size:1em; color:green;">Die Tabelle "T_CASE_OPS_GROUPED"
 * speichert die Prozeduren , die mit T_CASE_OPS und T_GROUPING_RESULTS
 * referenziert wird  </p>
 */
@Entity
@Table(name = "T_CASE_OPS_GROUPED",
        indexes = {
            @Index(name = "IDX_CASE_OPS_GROUPED_GRPRES_ID", columnList = "T_GROUPING_RESULTS_ID"),
            @Index(name = "IDX_CASE_OPS_GROUPED4OPSC_ID", columnList = "T_CASE_OPS_ID")})
@SuppressWarnings("serial")
public class TCaseOpsGrouped extends AbstractEntity {

    private static final long serialVersionUID = 1L;
    /*    @Override
  public int hashCode() {
  int hash = 7;
  hash = 29 * hash + (int) (this.id ^ (this.id >>> 32));
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
  final TCaseOpsGrouped other = (TCaseOpsGrouped) obj;
  if (this.id != other.id) {
  return false;
  }
  return true;
  }*/

    // private long id;
    private TCaseOps caseOps;
    private TGroupingResults groupingResults;
    private boolean opsResU4gFl = false;
    private int opsResValidEn;// Enum  ResValidEn ,ResValidEnConverter
    private String opsResTypeEn; //  Enum  OpsResTypeEn
    private TCaseSupplFee caseSupplFees;

    /**
     * Gibt Verweis auf die Tablle T_CASE_OPS zurück.
     *
     * @return caseOps
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "T_CASE_OPS_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_OPS_GR4T_CASE_OPS_ID"))
    public TCaseOps getCaseOps() {
        return this.caseOps;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "caseOpsGrouped")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public TCaseSupplFee getCaseSupplFees() {
        return this.caseSupplFees;
    }

    /**
     * Gibt Verweis auf die Tablle T_GROUPING_RESULTS zurück.
     *
     * @return groupingResults
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "T_GROUPING_RESULTS_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_OPS_GR4GROUPING_RESULTS_ID"))
    public TGroupingResults getGroupingResults() {
        return this.groupingResults;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_CASE_OPS_GROUPED_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    /**
     * Gibt Prozedurentyp zurück, wie der Grupper ermittelt hat.
     *
     * @return opsResTypeEn
     */
    @Column(name = "OPS_RES_TYPE_EN", length = 2)
    public String getOpsResTypeEn() {
        return this.opsResTypeEn;
    }

    /**
     * Gibt 0/1 Flag zurück,der für die Gruppierung verwendet Wird.
     *
     * @return opsResU4gFl
     */
    @Column(name = "OPS_RES_U4G_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean getOpsResU4gFl() {
        return this.opsResU4gFl;
    }

    /**
     * Gibt Gültigekeit der OPS zurück.
     *
     * @return opsResValidEn
     */
    @Column(name = "OPS_RES_VALID_EN", precision = 1, scale = 0)
    public int getOpsResValidEn() {
        return this.opsResValidEn;
    }

    /**
     *
     * @param caseOps Column OPSC_ID:Verweis auf die Tablle T_CASE_OPS.
     */
    public void setCaseOps(final TCaseOps caseOps) {
        this.caseOps = caseOps;
    }

    public void setCaseSupplFees(final TCaseSupplFee caseSupplFees) {
        this.caseSupplFees = caseSupplFees;
    }

    /**
     *
     * @param groupingResults Column T_GROUPING_RESULTS_ID: Verweis auf die
     * Tablle T_GROUPING_RESULTS.
     */
    public void setGroupingResults(final TGroupingResults groupingResults) {
        this.groupingResults = groupingResults;
    }

    public void setId(final long id) {
        this.id = id;
    }

    /**
     *
     * @param opsResTypeEn Column OPS_RES_TYPE_EN: Prozedurentyp, wie der
     * Grupper ermittelt hat, .
     */
    public void setOpsResTypeEn(final String opsResTypeEn) {
        this.opsResTypeEn = opsResTypeEn;
    }

    /**
     *
     * @param opsResU4gFl Column OPS_RES_U4G_FL: Flag 0/1, Wird für die
     * Gruppierung verwendet.
     */
    public void setOpsResU4gFl(final boolean opsResU4gFl) {
        this.opsResU4gFl = opsResU4gFl;
    }

    /**
     *
     * @param opsResValidEn Column OPS_RES_VALID_EN: Gültigekeit der OPS .
     */
    public void setOpsResValidEn(final int opsResValidEn) {
        this.opsResValidEn = opsResValidEn;
    }

}
