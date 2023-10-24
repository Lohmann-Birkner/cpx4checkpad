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

import de.lb.cpx.model.enums.DrgCorrTypeEn;
import de.lb.cpx.model.enums.DrgPartitionEn;
import de.lb.cpx.shared.lang.Lang;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.hibernate.annotations.Type;

/**
 * TCaseDrg initially generated at 21.01.2016 17:07:59 by Hibernate Tools
 * 3.2.2.GA
 *
 */
@Entity
//@Table(name = "T_CASE_DRG")
@DiscriminatorValue(value = "DRG")
//@SuppressWarnings("serial")
public class TCaseDrg extends TGroupingResults {

    private static final Logger LOG = Logger.getLogger(TCaseDrg.class.getName());

    private static final long serialVersionUID = 1L;

//  private long id;
    //private TGroupingResults groupingResults;
    private double drgcCwEffectiv;
    private Short drgcDaysCorr;
    private double drgcCwCorr;
    private DrgCorrTypeEn drgcTypeOfCorrEn;
    private DrgPartitionEn drgcPartitionEn; // for Analysis of merging of drg cases from DRG - Catalog
    private boolean drgcIsExceptionFl; // for Analysis of merging of drg cases from DRG - Catalog
    private int drgcHtp; // OGVD der DRG // for Analysis of merging of drg cases from DRG - Catalog
    private double drgcNegoFee2Day; // 
    private int drgcNegoFeeDays;
    private int drgcLtp;//ugvd
    private double drgcAlos;// average length of stay
    private double drgcCwCatalog;
    private double drgcCwCorrDay;
    private double drgcCareCwDay;
    private int drgcCareDays;
    private double drgcCareCw;
    private Set<TCaseDrgCareGrades>drgCareGrades =  new HashSet<>();
    
    @Column(name = "DRGC_CW_CORR", precision = 10, scale = 3)
    public double getDrgcCwCorr() {
        return this.drgcCwCorr;
    }

    @Column(name = "DRGC_CW_EFFECTIV", precision = 10, scale = 3)
    public double getDrgcCwEffectiv() {
        return this.drgcCwEffectiv;
    }

    @Column(name = "DRGC_DAYS_CORR", precision = 3, scale = 0)
    public Short getDrgcDaysCorr() {
        return this.drgcDaysCorr;
    }

    @Column(name = "DRGC_TYPE_OF_CORR_EN", length = 25)
    @Enumerated(EnumType.STRING)
    public DrgCorrTypeEn getDrgcTypeOfCorrEn() {
        return this.drgcTypeOfCorrEn;
    }

    @Column(name = "DRGC_PARTITION_EN", length = 25)
    @Enumerated(EnumType.STRING)
    public DrgPartitionEn getDrgcPartitionEn() {
        return drgcPartitionEn;
    }

    public void setDrgcPartitionEn(DrgPartitionEn drgcPartitionEn) {
        this.drgcPartitionEn = drgcPartitionEn;
    }

    @Column(name = "EXCEPTION_DRG_FL", precision = 1, scale = 0)
    @Type(type = "numeric_boolean")
    public boolean getIsDrgcIsExceptionFl() {
        return drgcIsExceptionFl;
    }

    public void setIsDrgcIsExceptionFl(boolean drgcIsExceptionFl) {
        this.drgcIsExceptionFl = drgcIsExceptionFl;
    }

    @Column(name = "DRGC_HTP", precision = 3, scale = 0)
    public int getDrgcHtp() {
        return drgcHtp;
    }

    @Column(name = "DRGC_NEGO_FEE_2_DAY", precision = 10, scale = 2)
    public double getDrgcNegoFee2Day() {
        return drgcNegoFee2Day;
    }

    @Column(name = "DRGC_NEGO_FEE_DAYS", precision = 5, scale = 0)
    public int getDrgcNegoFeeDays() {
        return drgcNegoFeeDays;
    }

    @Column(name = "DRGC_LTP", precision = 5, scale = 0)
    public int getDrgcLtp() {
        return drgcLtp;
    }

    @Column(name = "DRGC_ALOS", precision = 10, scale = 1)
    public double getDrgcAlos() {
        return drgcAlos;
    }

    @Column(name = "DRGC_CW_CATALOG", precision = 10, scale = 3)
    public double getDrgcCwCatalog() {
        return drgcCwCatalog;
    }

    @Column(name = "DRGC_CW_CORR_DAY", precision = 10, scale = 3)
    public double getDrgcCwCorrDay() {
        return drgcCwCorrDay;
    }

    @Column(name = "DRGC_CARE_CW_DAY", precision = 10, scale = 4)
    public double getDrgcCareCwDay() {
        return drgcCareCwDay;
    }

    @Column(name = "DRGC_CARE_DAYS", precision = 10, scale = 0)
    public int getDrgcCareDays() {
        return drgcCareDays;
    }

    @Column(name = "DRGC_CARE_CW", precision = 10, scale = 4)
    public double getDrgcCareCw() {
        return drgcCareCw;
    }


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "caseDrg")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Set<TCaseDrgCareGrades> getDrgCareGrades() {
        return drgCareGrades;
    }
  
    
    public void setDrgcHtp(int drgHtp) {
        this.drgcHtp = drgHtp;
    }

    /*  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "GRPRES_ID", nullable = false)
  public TGroupingResults getGroupingResults() {
    return this.groupingResults;
  }
     */
 /*  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
  @SequenceGenerator(name = "default_gen", sequenceName = "T_CASE_DRG_SQ")
  @Column(name = "ID", unique = true, nullable = false, scale = 0)
  public long getId() {
    return this.id;
  }
     */
    public void setDrgcCwCorr(final double drgcCwCorr) {
        this.drgcCwCorr = drgcCwCorr;
    }

    public void setDrgcCwEffectiv(final double drgcCwEffectiv) {
        this.drgcCwEffectiv = drgcCwEffectiv;
    }

    public void setDrgcDaysCorr(final Short drgcDaysCorr) {
        this.drgcDaysCorr = drgcDaysCorr;
    }

    public void setDrgcTypeOfCorrEn(final DrgCorrTypeEn drgcTypeOfCorrEn) {
        this.drgcTypeOfCorrEn = drgcTypeOfCorrEn;
    }

    /*
  public void setGroupingResults(final TGroupingResults groupingResults) {
    this.groupingResults = groupingResults;
  }

  public void setId(final long id) {
    this.id = id;
  }
     */
    public void setAdrg(String adrg) {

    }

    public void setDrgcNegoFee2Day(final double tge2Day) {
        drgcNegoFee2Day = tge2Day;
    }

    public void setDrgcNegoFeeDays(final int tgeDays) {
        drgcNegoFeeDays = tgeDays;
    }

    public void setDrgcLtp(final int drgcLtp) {
        this.drgcLtp = drgcLtp;
    }

    public void setDrgcAlos(final double drgcAlos) {
        this.drgcAlos = drgcAlos;
    }

    public void setDrgcCwCatalog(final double drgcCwCatalog) {
        this.drgcCwCatalog = drgcCwCatalog;
    }

    public void setDrgcCwCorrDay(final double drgcCwCorrDay) {
        this.drgcCwCorrDay = drgcCwCorrDay;
    }

    public void setDrgcCareCwDay(double drgcCareCwDay) {
        this.drgcCareCwDay = drgcCareCwDay;
    }

    public void setDrgcCareCw(double drgcCareCw) {
        this.drgcCareCw = drgcCareCw;
    }

    public void setDrgcCareDays(int drgcCareDays) {
        this.drgcCareDays = drgcCareDays;
    }

    public void setDrgCareGrades(Set<TCaseDrgCareGrades> drgCareGrades) {
        this.drgCareGrades = drgCareGrades;
    }

    public double getCorrectedCwRevenue(Double pBaserate) {
        if (pBaserate == null) {
            LOG.warning("Can not compute catalog cw, baserate was null!");
            return 0.0d;
        }
        double revCatalog = getCatalogCwRevenue(pBaserate);
        switch (getDrgcTypeOfCorrEn()) {
            case DeductionTransferDis:
            case DeductionTransferAdm:
            case DeductionTransfer:
            case Deduction:
                return revCatalog - getRevenueCatalogCorrected(pBaserate);
            case Surcharge:
                return revCatalog + getRevenueCatalogCorrected(pBaserate);
            case no:
                return revCatalog;
            default:
                LOG.log(Level.WARNING, "unknown correctionType: {0} return only Revenue of Catalog: {1}", new Object[]{getDrgcTypeOfCorrEn().name(), revCatalog});
                return revCatalog;
        }
    }

    public double getCatalogCwRevenue(Double pBaserate) {
        if (pBaserate == null) {
            return 0.0d;
        }
        double revCatalog = getDrgcCwCatalog() * pBaserate;
        return Lang.round(revCatalog, 2);
    }

    @Transient
    public boolean isNegotiatedDayFee() {
        return //isGrpresIsNegotiatedFl() && this flag is not set by grouper and is not used anywhere AGe
                getDrgcNegoFee2Day() > 0 && getDrgcNegoFeeDays() > 0;
    }

    @Transient
    public double getNegotiatedFeeRevenue() {
        double negotiated = getDrgcNegoFee2Day() * getDrgcNegoFeeDays();
        return Lang.round(negotiated, 2);
    }

    public double getRevenueCatalogCorrected(double pBaserate) {
        double revCorrDays = getDrgcCwCorrDay() * pBaserate;
        double roundedRevCorr = Lang.round(revCorrDays, 2);
        return roundedRevCorr * getDrgcDaysCorr();
    }

    public double getCareRevenue(double pBaserate) {
        if(getDrgCareGrades() != null || !getDrgCareGrades().isEmpty()){


            double revenue = 0;
            for(TCaseDrgCareGrades grade: getDrgCareGrades()){
                revenue += grade.getGradeRevenue(); 
            }
            if(revenue != 0){
                return revenue;
            }
        }
        double revCareDay = getDrgcCareCwDay() * pBaserate;
        double roundedRevCare = Lang.round(revCareDay, 2);
        return roundedRevCare * getDrgcCareDays();
    }
    
    public double getRevenue(Double pDrgBaserate, Double pCareBaserate){
        pDrgBaserate = Objects.requireNonNullElse(pDrgBaserate, 0.0);
        pCareBaserate = Objects.requireNonNullElse(pCareBaserate, 0.0);
        
        return getDrgRevenue(pDrgBaserate)+getCareRevenue(pCareBaserate);
    }
    public double getDrgRevenue(Double pDrgBaserate){
        pDrgBaserate = Objects.requireNonNullElse(pDrgBaserate, 0.0);
        return isNegotiatedDayFee()?getNegotiatedFeeRevenue():getCorrectedCwRevenue(pDrgBaserate);
    }
    
    @Transient
    public double getCareCwDays(){
        return getDrgcCareCwDay() * getDrgcCareDays();
    }
}
