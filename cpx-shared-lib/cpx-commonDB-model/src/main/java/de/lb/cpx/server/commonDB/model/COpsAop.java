/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.server.commonDB.model;

import de.lb.cpx.model.enums.CountryEn;
import de.lb.cpx.server.commons.dao.AbstractCatalogEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author gerschmann
 */
@Entity
@Table(name = "C_OPS_AOP")
@SuppressWarnings("serial")
public class COpsAop extends AbstractCatalogEntity {
    private CountryEn countryEn;
    private String opsCode;
    private String opsCategory;
    private int opsYear;
    private int catalogSheet;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_OPS_AOP_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }


    public void setId(long id) {
        this.id = id;
    }
    
    @Column(name = "COUNTRY_EN", length = 25)
    @Enumerated(EnumType.STRING)
    public CountryEn getCountryEn() {
        return countryEn;
    }

    public void setCountryEn(CountryEn countryEn) {
        this.countryEn = countryEn;
    }

    @Column(name = "OPS_CODE", length = 10, nullable = false)
    public String getOpsCode() {
        return opsCode;
    }

    public void setOpsCode(String opsCode) {
        this.opsCode = opsCode;
    }

    @Column(name = "OPS_CATEGORY", length = 255, nullable = true)
    public String getOpsCategory() {
        return opsCategory;
    }

    public void setOpsCategory(String opsCategory) {
        this.opsCategory = opsCategory;
    }

    @Column(name = "OPS_YEAR", nullable = false, length = 4)
    public int getOpsYear() {
        return opsYear;
    }

    public void setOpsYear(int opsYear) {
        this.opsYear = opsYear;
    }


    @Column(name = "AOP_CATALOG_SHEET", nullable = false, length = 4)
    public int getCatalogSheet() {
        return catalogSheet;
    }

    public void setCatalogSheet(int catalogSheet) {
        this.catalogSheet = catalogSheet;
    }


}
