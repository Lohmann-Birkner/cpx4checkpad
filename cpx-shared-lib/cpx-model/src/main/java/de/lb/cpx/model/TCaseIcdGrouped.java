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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.Type;

/**
 * TCaseIcdGrouped initially generated at 21.01.2016 17:07:59 by Hibernate Tools
 * 3.2.2.GA
 * <p style="font-size:1em; color:green;"> Die Tabelle "T_CASE_ICD_GROUPED"
 * speichert berechnete CCL der Diagnose, die mit T_CASE_ICD und
 * T_GROUPING_RESULTS referenziert wird. </p>
 */
@Entity
@Table(name = "T_CASE_ICD_GROUPED",
        indexes = {
            @Index(name = "IDX_CASE_ICD_GROUPED_GRPRES_ID", columnList = "T_GROUPING_RESULTS_ID"),
            @Index(name = "IDX_CASE_ICD_GRD4CASE_ICD_ID", columnList = "T_CASE_ICD_ID")})
@SuppressWarnings("serial")
public class TCaseIcdGrouped extends AbstractEntity {

    private static final long serialVersionUID = 1L;
    /*    @Override
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
  final TCaseIcdGrouped other = (TCaseIcdGrouped) obj;
  if (this.id != other.id) {
  return false;
  }
  return true;
  }*/

//  private long id;
    private TCaseIcd caseIcd;
    private TGroupingResults groupingResults;
    private boolean icdResU4gFl = false;
    private int icdResValidEn; // Enum  ResValidEn ,ResValidEnConverter
    private int icdResCcl;

    /**
     * Gibt Verweis auf die Tabelle T_Case_ICD zurück.
     *
     * @return caseIcd
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "T_CASE_ICD_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_CASE_ICD_GR4T_CASE_ICD_ID"))
    public TCaseIcd getCaseIcd() {
        return this.caseIcd;
    }

    /**
     * Gibt Verweis auf die Tabelle T_GROUPING_RESULTS zurück.
     *
     * @return groupingResults
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "T_GROUPING_RESULTS_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_CASE_ICD_GR4T_GROUP_RES_ID"))
    public TGroupingResults getGroupingResults() {
        return this.groupingResults;
    }

    /**
     * Gibt Berechnete CCL der Diagnose zurück , die mit T_CASE_ICD_ID
     * referenziert wird für den Groupingergebnis, der mit T_GROUPING_RESULTS_ID
     * referenziert wird .
     *
     * @return icdResCcl
     */
    @Column(name = "ICD_RES_CCL", precision = 1, scale = 0)
    public int getIcdResCcl() {
        return this.icdResCcl;
    }

    /**
     * Gibt grouping Flag 0/1 zurück .
     *
     * @return icdResU4gFl
     */
    @Column(name = "ICD_RES_U4G_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean getIcdResU4gFl() {
        return this.icdResU4gFl;
    }

    /**
     * Gibt Gültigekeit der ICD zurück .
     *
     * @return icdResValidEn
     */
    @Column(name = "ICD_RES_VALID_EN", precision = 1, scale = 0)
    public int getIcdResValidEn() {
        return this.icdResValidEn;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_CASE_ICD_GROUPED_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    /**
     *
     * @param caseIcd Column T_CASE_ICD_ID: Verweis auf die Tabelle T_Case_ICD.
     */
    public void setCaseIcd(final TCaseIcd caseIcd) {
        this.caseIcd = caseIcd;
    }

    /**
     *
     * @param groupingResults Column T_GROUPING_RESULTS_ID: Verweis auf die
     * Tabelle T_GROUPING_RESULTS .
     */
    public void setGroupingResults(final TGroupingResults groupingResults) {
        this.groupingResults = groupingResults;
    }

    /**
     *
     * @param icdResCcl Column ICD_RES_CCL: Berechnete CCL der Diagnose, die mit
     * T_CASE_ICD_ID referenziert wird für den Groupingergebnis, der mit
     * T_GROUPING_RESULTS_ID referenziert wird .
     */
    public void setIcdResCcl(final int icdResCcl) {
        this.icdResCcl = icdResCcl;
    }

    /**
     *
     * @param icdResU4gFl Column ICD_RES_U4G_FL: Used for grouping Flag 0/1 .
     */
    public void setIcdResU4gFl(final boolean icdResU4gFl) {
        this.icdResU4gFl = icdResU4gFl;
    }

    /**
     *
     * @param icdResValidEn Column ICD_RES_VALID_EN: Gültigekeit der ICD .
     */
    public void setIcdResValidEn(final int icdResValidEn) {
        this.icdResValidEn = icdResValidEn;
    }

    public void setId(final long id) {
        this.id = id;
    }

}
