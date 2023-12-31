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
package de.lb.cpx.server.commonDB.model;

//
// This file was generated by the JPA Modeler
//
import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name = "VW_CATALOG_OVERVIEW")
@SuppressWarnings("serial")
public class VwCatalogOverview extends AbstractEntity {

    private String catalog;
    private String countryEn;
    private int year;
    private int cnt;
    private long minId;
    private long maxId;
    private Date cdate;

    public VwCatalogOverview() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "default_gen")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    @Basic
    @SequenceGenerator(name = "default_gen", sequenceName = "VW_CATALOG_OVERVIEW_SQ", allocationSize = 1)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "CATALOG")
    public String getCatalog() {
        return this.catalog;
    }

    public void setCatalog(String pCatalog) {
        this.catalog = pCatalog;
    }

    @Column(name = "COUNTRY_EN")
    public String getCountryEn() {
        return this.countryEn;
    }

    public void setCountryEn(String pCountryEn) {
        this.countryEn = pCountryEn;
    }

    @Column(name = "CYEAR")
    public int getYear() {
        return this.year;
    }

    public void setYear(int pYear) {
        this.year = pYear;
    }

    @Column(name = "CNT")
    public int getCnt() {
        return this.cnt;
    }

    public void setCnt(int pCnt) {
        this.cnt = pCnt;
    }

    @Column(name = "MIN_ID")
    public long getMinId() {
        return this.minId;
    }

    public void setMinId(long pMinId) {
        this.minId = pMinId;
    }

    @Column(name = "MAX_ID")
    public long getMaxId() {
        return this.maxId;
    }

    public void setMaxId(long pMaxId) {
        this.maxId = pMaxId;
    }

    @Column(name = "CDATE")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Override
    public Date getDate() {
        return cdate == null ? null : new Date(cdate.getTime());
    }

    public void setDate(Date pDate) {
        this.cdate = pDate == null ? null : new Date(pDate.getTime());
    }

}
