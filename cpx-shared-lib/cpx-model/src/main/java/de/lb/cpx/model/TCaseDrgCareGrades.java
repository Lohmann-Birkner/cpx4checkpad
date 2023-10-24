/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.model;

import de.lb.cpx.server.commons.dao.AbstractEntity;
import de.lb.cpx.shared.lang.Lang;
import java.util.Date;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author gerschmann
 */
@Entity
@Table(name = "T_CASE_DRG_CARE_GRADES", indexes = {
    @Index(name = "IDX_CASE4DRG_ID", columnList = "T_CASE_DRG_ID", unique = false)})
public class TCaseDrgCareGrades extends AbstractEntity implements Comparable <TCaseDrgCareGrades>{
    
    private static final long serialVersionUID = 1L;


    private TCaseDrg caseDrg;
    private int drgCareSortInd;
    private int drgCareDays;
    private double drgCareCwDay;
    private double drgCareBaserate;
    private Date drgCareFrom;
    private Date drgCareTo;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_CASE_DRG_CARE_GRADES_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "T_CASE_DRG_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_DRG_CARE4T_CASE_DRG_ID"))   
    public TCaseDrg getCaseDrg() {
        return caseDrg;
    }

    public void setCaseDrg(TCaseDrg caseDrg) {
        this.caseDrg = caseDrg;
    }

    @Column(name = "SORT_INDEX", precision = 5, scale = 0, nullable = false)    
    public int getDrgCareSortInd() {
        return drgCareSortInd;
    }

    public void setDrgCareSortInd(int drgCareSortInd) {
        this.drgCareSortInd = drgCareSortInd;
    }

    @Column(name = "CARE_DAYS", precision = 5, scale = 0, nullable = false)
    public int getDrgCareDays() {
        return drgCareDays;
    }

    public void setDrgCareDays(int drgCareDays) {
        this.drgCareDays = drgCareDays;
    }

     @Column(name = "CARE_CW_DAY", precision = 10, scale = 4, nullable = false)
   public double getDrgCareCwDay() {
        return drgCareCwDay;
    }

    public void setDrgCareCwDay(double drgCareCwDay) {
        this.drgCareCwDay = drgCareCwDay;
    }

    @Column(name = "CARE_BASERATE", precision = 10, scale = 2, nullable = false)
    public double getDrgCareBaserate() {
        return drgCareBaserate;
    }

    public void setDrgCareBaserate(double drgCareBaserate) {
        this.drgCareBaserate = drgCareBaserate;
    }


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ACCOUNTED_FROM", nullable = false)
    public Date getDrgCareFrom() {
        return drgCareFrom;
    }

    public void setDrgCareFrom(Date drgCareFrom) {
        this.drgCareFrom = drgCareFrom;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ACCOUNTED_TO", nullable = false)
    public Date getDrgCareTo() {
        return drgCareTo;
    }

    public void setDrgCareTo(Date drgCareTo) {
        this.drgCareTo = drgCareTo;
    }

    @Override
    public int compareTo(TCaseDrgCareGrades o) {
        if(getDrgCareSortInd()< o.getDrgCareSortInd()){
            return -1;
        }
        if(getDrgCareSortInd()== o.getDrgCareSortInd()){
            return 0;
        }

        return 1;
        
                
    }
    
    @Transient
    public double getGradeRevenue(){
        double revCareDay = this.getDrgCareCwDay()* this.getDrgCareBaserate();
        double roundedRevCare = Lang.round(revCareDay, 2);
        return roundedRevCare * this.getDrgCareDays();        
    }


}
