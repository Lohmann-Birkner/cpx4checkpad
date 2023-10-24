/**
 * Copyright (c) 2016 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner Health Care Consulting GmbH and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner Health Care Consulting GmbH
 * http://www.lohmann-birkner.de
 *
 */
package de.lb.cpx.server.commonDB.model;

import de.lb.cpx.server.commons.dao.AbstractCatalogEntity;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.jboss.ejb3.annotation.SecurityDomain;

/**
 * CCountry initially generated at 03.02.2016 10:32:45 by Hibernate Tools
 * 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;">C_COUNTRY : Tabelle der
 * Staates.</p>
 *
 */
@NamedQueries({
    @NamedQuery(
            name = "findCountryByShortName",
            query = "from CCountry c where c.shortName = :short_name"
    )
})
@Entity
@Table(name = "C_COUNTRY")
@SuppressWarnings("serial")
@SecurityDomain("cpx")
public class CCountry extends AbstractCatalogEntity {

    private static final long serialVersionUID = 1L;
    /*@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="CCountry")
  public Set<CInsuranceCompany> getCInsuranceCompanies() {
  return this.CInsuranceCompanies;
  }
  
  public void setCInsuranceCompanies(Set<CInsuranceCompany> CInsuranceCompanies) {
  this.CInsuranceCompanies = CInsuranceCompanies;
  }
  @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="CCountry")
  public Set<CDepartment> getCDepartments() {
  return this.CDepartments;
  }
  
  public void setCDepartments(Set<CDepartment> CDepartments) {
  this.CDepartments = CDepartments;
  }
  @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="CCountry")
  public Set<CState> getCStates() {
  return this.CStates;
  }
  
  public void setCStates(Set<CState> CStates) {
  this.CStates = CStates;
  }
  @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="CCountry")
  public Set<CDoctor> getCDoctors() {
  return this.CDoctors;
  }
  
  public void setCDoctors(Set<CDoctor> CDoctors) {
  this.CDoctors = CDoctors;
  }
  
     */

//     private long id;
    private String shortName;
    private String name;
    private String resourceBundle;
    private Set<CCatalog> CCatalogs = new HashSet<>(0);
//     private Set<CInsuranceCompany> CInsuranceCompanies = new HashSet<>(0);
//     private Set<CDepartment> CDepartments = new HashSet<>(0);
    //    private Set<CState> CStates = new HashSet<>(0);
//     private Set<CDoctor> CDoctors = new HashSet<>(0);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_COUNTRY_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return shortName :Kurzer Name des Staates .
     */
    @Column(name = "SHORT_NAME", length = 10)
    public String getShortName() {
        return this.shortName;
    }

    /**
     * @param shortName Column SHORT_NAME :Kurzer Name des Staates .
     */
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    /**
     *
     * @return name: Name des Staates.
     */
    @Column(name = "NAME", length = 50)
    public String getName() {
        return this.name;
    }

    /**
     * @param name Column NAME: Name des Staates.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return resourceBundle für diesen Staat
     */
    @Column(name = "RESOURCE_BUNDLE", length = 4000)
    public String getResourceBundle() {
        return this.resourceBundle;
    }

    /**
     * @param resourceBundle Column RESOURCE_BUNDLE: RESOURCE_BUNDLE für diesen
     * Staat
     */
    public void setResourceBundle(String resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "CCountry")
    public Set<CCatalog> getCCatalogs() {
        return this.CCatalogs;
    }

    public void setCCatalogs(Set<CCatalog> CCatalogs) {
        this.CCatalogs = CCatalogs;
    }

}
