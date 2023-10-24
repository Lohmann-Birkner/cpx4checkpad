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

import de.lb.cpx.model.enums.PeppPayTypeEn;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * TCasePepp initially generated at 21.01.2016 17:07:59 by Hibernate Tools
 * 3.2.2.GA.
 */
@Entity
//@Table(name = "T_CASE_PEPP")
//@SuppressWarnings("serial")
@DiscriminatorValue(value = "PEPP")
public class TCasePepp extends TGroupingResults {

    private static final long serialVersionUID = 1L;

//  private long id;
//  private TGroupingResults groupingResults;
    private PeppPayTypeEn peppcTypeEn; // Enumeration
    private double peppcCwEffectiv;
    private Integer peppcDaysPerscareAdult;
    private Integer peppcDaysPerscareInf;
    private Integer peppcDaysIntensiv;
    private Integer peppcPersentageIntens;
    private Integer peppcPayClass;
    private Double peppPayClassCwDay;
    private Set<TCasePeppGrades> peppcGrades = new HashSet<>();

    /*  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "GRPRES_ID", nullable = false)
  public TGroupingResults getGroupingResults() {
    return this.groupingResults;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
  @SequenceGenerator(name = "default_gen", sequenceName = "T_CASE_PEPP_SQ")
  @Column(name = "ID", unique = true, nullable = false, scale = 0)
  public long getId() {
    return this.id;
  }
     */
    @Column(name = "PEPPC_CW_EFFECTIV", precision = 10, scale = 4)
    public double getPeppcCwEffectiv() {
        return this.peppcCwEffectiv;
    }

    @Column(name = "PEPP_PAY_CLASS_CW_DAY", precision = 10, scale = 4)
    public Double getPeppPayClassCwDay() {
        return this.peppPayClassCwDay;
    }

    @Column(name = "PEPPC_DAYS_INTENSIV", precision = 5, scale = 0)
    public Integer getPeppcDaysIntensiv() {
        return this.peppcDaysIntensiv;
    }

    @Column(name = "PEPPC_DAYS_PERSCARE_ADULT", precision = 5, scale = 0)
    public Integer getPeppcDaysPerscareAdult() {
        return this.peppcDaysPerscareAdult;
    }

    @Column(name = "PEPPC_DAYS_PERSCARE_INF", precision = 5, scale = 0)
    public Integer getPeppcDaysPerscareInf() {
        return this.peppcDaysPerscareInf;
    }

    @Column(name = "PEPPC_PERSENTAGE_INTENS", precision = 10)
    public Integer getPeppcPersentageIntens() {
        return this.peppcPersentageIntens;
    }

    @Column(name = "PEPPC_TYPE_EN", length = 20)
    @Enumerated(EnumType.STRING)
    public PeppPayTypeEn getPeppcType() {
        return peppcTypeEn;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "casePepp")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Set<TCasePeppGrades> getPeppcGrades() {
        return peppcGrades;
    }

    @Column(name = "PEPPC_PAY_CLASS", precision = 10)
    public Integer getPeppcPayClass() {
        return this.peppcPayClass;
    }


    /*  
  public void setGroupingResults(final TGroupingResults groupingResults) {
    this.groupingResults = groupingResults;
  }

  public void setId(final long id) {
    this.id = id;
  }
     */
    public void setPeppcCwEffectiv(final double peppcCwEffectiv) {
        this.peppcCwEffectiv = peppcCwEffectiv;
    }

    public void setPeppcDaysIntensiv(final Integer peppcDaysIntensiv) {
        this.peppcDaysIntensiv = peppcDaysIntensiv;
    }

    public void setPeppcDaysPerscareAdult(final Integer peppcDaysPerscareAdult) {
        this.peppcDaysPerscareAdult = peppcDaysPerscareAdult;
    }

    public void setPeppcDaysPerscareInf(final Integer peppcDaysPerscareInf) {
        this.peppcDaysPerscareInf = peppcDaysPerscareInf;
    }

    public void setPeppcPersentageIntens(final Integer peppcPersentageIntens) {
        this.peppcPersentageIntens = peppcPersentageIntens;
    }

    public void setPeppcPayClass(final Integer peppcPayClass) {
        this.peppcPayClass = peppcPayClass;
    }

    public void setPeppcGrades(Set<TCasePeppGrades> peppcGrades) {
        this.peppcGrades = peppcGrades;
    }

    public void setPeppcType(PeppPayTypeEn peppcType) {
        this.peppcTypeEn = peppcType;
    }

    @Transient
    public double getRevenue() {
        double rev = 0.0;
        double revOneDay;
        for (TCasePeppGrades grade : getPeppcGrades()) {
            //CPX-1137 
            // Erlös pro Tag =cw pro Tag * baserate pro tage  mit zwei stelle nach Komma 
            revOneDay = Math.round((grade.getPeppcgrCw() * grade.getPeppcgrBaserate() / grade.getPeppcgrDays()) * 100.0) / 100.0;
            //Erlös=Erlös pro Tag * Anzahl der Tage für diese Klasse/Stufe
            rev += revOneDay * grade.getPeppcgrDays();
        }
        return rev;
    }

    // don't use for computing of revenue!!! for simulate tooltip only
    public void setPeppPayClassCwDay(double peppCw4Class) {
        this.peppPayClassCwDay = peppCw4Class;
    }

}
