/*
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

import de.checkpoint.drg.GrouperInterfaceBasic;
import de.lb.cpx.model.converter.GrouperMdcOrSkConverter;
import de.lb.cpx.model.enums.CountryEn;
import de.lb.cpx.model.enums.GrouperMdcOrSkEn;
import de.lb.cpx.server.commons.dao.AbstractDrgmCatalogEntity;
import static de.lb.cpx.service.information.CatalogTypeEn.PEPP;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.annotations.Type;
import org.jboss.ejb3.annotation.SecurityDomain;

/**
 * CPeppCatalog initially generated at 03.02.2016 10:32:45 by Hibernate Tools
 * 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;">C_PEPP_CATALOG: Kataloge der
 * PEPPs (Pauschalierendes Entgeltsystem Psychiatrie und Psychosomatik)
 */
@Entity
@Table(name = "C_PEPP_CATALOG")
@SuppressWarnings("serial")
@SecurityDomain("cpx")
public class CPeppCatalog extends AbstractDrgmCatalogEntity {

    private static final long serialVersionUID = 1L;

//     private long id;
    private CountryEn countryEn;

    private String peppHosIdent; // for negotiated pepps only
    private int peppYear;
    private String peppPepp;
    private String peppDescription;
    private boolean peppHasClassesFl;// for catalogs after 2013 = 1
    private GrouperMdcOrSkEn mdcSkEn;
    private boolean peppIsNegotiatedFl;
    private boolean peppIsDayCareFl;
    private Date peppValidFrom;
    private Date peppValidTo;
    private int peppRelationNumber;//Nummer der Vergütungsstufe(<2014)/Vergütungsklasse(>=2014)Default = 1, für Vergütungsklasse = default
    private int peppRelationFrom;//Für die Vergütungsstufen erste Tag der Stufe, für die Vergütungsklasse - Nummer der Vergütungsklasse
    private Integer peppRelationTo;//Für Vergütungsstufen - letzte Tag  oder 0, für Vergütungsklassen = PEPP_RELATION_FROM oder 0 für die letzte Klasse
    private BigDecimal peppRelationCostWeight;//cost weight of one Relation

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_PEPP_CATALOG_SQ", allocationSize = 1)
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

    /**
     *
     * @return peppYear:Katalogjahr
     */
    @Column(name = "PEPP_YEAR", nullable = false, length = 4)
    public int getPeppYear() {
        return peppYear;
    }

    public void setPeppYear(final int year) {
        this.peppYear = year;
    }

    /**
     *
     * @return peppHosIdent : Nur für ausgehandelte Pepps
     */
    @Column(name = "PEPP_HOS_IDENT", nullable = true, length = 10)
    public String getPeppHosIdent() {
        return this.peppHosIdent;
    }

    /**
     *
     * @param peppHosIdent Column PEPP_HOS_IDENT: Nur für ausgehandelte Pepps
     */
    public void setPeppHosIdent(String peppHosIdent) {
        this.peppHosIdent = peppHosIdent;
    }

    /**
     *
     * @return peppPepp : PEPP - Code
     */
    @Column(name = "PEPP_PEPP", nullable = false, length = 10)
    public String getPeppPepp() {
        return this.peppPepp;
    }

    /**
     *
     * @param peppPepp Column PEPP_PEPP : PEPP - Code
     */
    public void setPeppPepp(String peppPepp) {
        this.peppPepp = peppPepp;
    }

    /**
     *
     * @return peppHasClassesFl : Ein Flag, =1, für die Kataloge, die
     * Vergütungsklassen haben ( &gt; 2014)
     */
    @Column(name = "PEPP_HAS_CLASSES_FL", nullable = false, precision = 1, scale = 0)
    public boolean getPeppHasClassesFl() {
        return this.peppHasClassesFl;
    }

    /**
     *
     * @param peppHasClassesFl Column PEPP_HAS_CLASSES_FL: Ein Flag, =1, für die
     * Kataloge, die Vergütungsklassen haben (&gt; 2014)
     */
    public void setPeppHasClassesFl(boolean peppHasClassesFl) {
        this.peppHasClassesFl = peppHasClassesFl;
    }

    /**
     *
     * @return peppDescription: PEPP - Definition
     */
    @Column(name = "PEPP_DESCRIPTION", length = 1000)
    public String getPeppDescription() {
        return this.peppDescription;
    }

    /**
     *
     * @param peppDescription Column PEPP_DESCRIPTION :PEPP - Definition
     */
    public void setPeppDescription(String peppDescription) {
        this.peppDescription = peppDescription;
    }

    /**
     *
     * @return mdcSkEn:Enumeration GrouperMdcOrSk
     */
    @Column(name = "MDC_SK_EN", length = 25)
    //@Enumerated(EnumType.STRING)
    @Convert(converter = GrouperMdcOrSkConverter.class)
    public GrouperMdcOrSkEn getCMdcSkCatalog() {
        return this.mdcSkEn;
    }

    public void setCMdcSkCatalog(GrouperMdcOrSkEn sk) {
        this.mdcSkEn = sk;
    }

    /**
     *
     * @return peppIsNegotiatedFl : Verhandelbar Ja/Nein 1/0
     */
    @Column(name = "PEPP_IS_NEGOTIATED_FL", precision = 1, scale = 0)
    @Type(type = "numeric_boolean")
    public boolean getPeppIsNegotiatedFl() {
        return this.peppIsNegotiatedFl;
    }

    public void setPeppIsNegotiatedFl(final boolean isNg) {
        this.peppIsNegotiatedFl = isNg;
    }

    /**
     *
     * @return peppIsDayCareFl: Teilstationär Ja/Nein: 1/0
     */
    @Column(name = "PEPP_IS_DAY_CARE_FL", precision = 1, scale = 0)
    @Type(type = "numeric_boolean")
    public boolean getPeppIsDayCareFl() {
        return this.peppIsDayCareFl;
    }

    public void setPeppIsDayCareFl(final boolean dayCare) {
        this.peppIsDayCareFl = dayCare;
    }

    /**
     *
     * @return peppValidFrom : Anfang der Gültigkeit
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PEPP_VALID_FROM", length = 7)
    public Date getPeppValidFrom() {
        return peppValidFrom == null ? null : new Date(peppValidFrom.getTime());
    }

    public void setPeppValidFrom(final Date validFrom) {
        peppValidFrom = validFrom == null ? null : new Date(validFrom.getTime());
    }

    /**
     *
     * @return PEPP_VALID_TO: Ende der Gültigkeit
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PEPP_VALID_TO", length = 7)
    public Date getPeppValidTo() {
        return peppValidTo == null ? null : new Date(peppValidTo.getTime());
    }

    public void setPeppValidTo(final Date validTo) {
        peppValidTo = validTo == null ? null : new Date(validTo.getTime());
    }

    /**
     *
     * @return peppRelationNumber: Nummer der Vergütungsstufe(&lt;
     * 2014)/Vergütungsklasse( &gt;= 2014)Default = 1, für Vergütungsklasse =
     * default
     */
    @Column(name = "PEPP_RELATION_NUMBER", nullable = false, precision = 5, scale = 0)
    public int getPeppRelationNumber() {
        return this.peppRelationNumber;
    }

    /**
     *
     * @param peppRelationNumber Column PEPP_RELATION_NUMBER :Nummer der
     * Vergütungsstufe(&lt; 2014)/Vergütungsklasse(&gt; =2014)Default = 1, für
     * Vergütungsklasse = default
     */
    public void setPeppRelationNumber(int peppRelationNumber) {
        this.peppRelationNumber = peppRelationNumber;
    }

    /**
     *
     * @return peppRelationFrom:Für die Vergütungsstufen erste Tag der Stufe,
     * für die Vergütungsklasse - Nummer der Vergütungsklasse
     */
    @Column(name = "PEPP_RELATION_FROM", nullable = false, precision = 5, scale = 0)
    public int getPeppRelationFrom() {
        return this.peppRelationFrom;
    }

    /**
     *
     * @param peppRelationFrom Column PEPP_RELATION_FROM :Für die
     * Vergütungsstufen erste Tag der Stufe, für die Vergütungsklasse - Nummer
     * der Vergütungsklasse
     */
    public void setPeppRelationFrom(int peppRelationFrom) {
        this.peppRelationFrom = peppRelationFrom;
    }

    /**
     *
     * @return peppRelationTo: Für Vergütungsstufen - letzte Tag oder 0, für
     * Vergütungsklassen = PEPP_RELATION_FROM oder 0 für die letzte Klasse
     */
    @Column(name = "PEPP_RELATION_TO", precision = 5, scale = 0)
    public Integer getPeppRelationTo() {
        return this.peppRelationTo;
    }

    /**
     *
     * @param peppRelationTo Column PEPP_RELATION_TO : Für Vergütungsstufen -
     * letzte Tag oder 0, für Vergütungsklassen = PEPP_RELATION_FROM oder 0 für
     * die letzte Klasse
     */
    public void setPeppRelationTo(Integer peppRelationTo) {
        this.peppRelationTo = peppRelationTo;
    }

    /**
     *
     * @return peppRelationCostWeight:CW der Relation
     */
    @Column(name = "PEPP_RELATION_COST_WEIGHT", precision = 10, scale = 4)
    public BigDecimal getPeppRelationCostWeight() {
        return this.peppRelationCostWeight;
    }

    /**
     *
     * @param peppRelationCostWeight Column PEPP_RELATION_COST_WEIGHT :CW der
     * Relation
     */
    public void setPeppRelationCostWeight(BigDecimal peppRelationCostWeight) {
        this.peppRelationCostWeight = peppRelationCostWeight;
    }

    /**
     * Liefert der String für die Ausgabe in die DRGM - Datei. Da es mehrere
     * Objekte der Klasse zu einem Eintrag in der DRGM - Datei hinzugezogen
     * werden können(für die Groupermodelle vor 2016, wird hier nur die erste
     * Relation für den führenden Objekt mitausgegeben. Für zufügen der
     * weirteren Relationen muss die methode getRelation2DrgmMapping benutzt
     * werden
     *
     * @return ein DRGM - String
     */
    @Transient
    @Override
    public String get2DrgmMapping() {
//        try {
        StringBuilder br = new StringBuilder();
        br.append(getId());
        br.append(";");
        br.append(getPeppHosIdent() == null ? "" : getPeppHosIdent());
        br.append(";");//ikz
        br.append(String.valueOf(GrouperInterfaceBasic.getModelIdByYear(getPeppYear())));
        br.append(";\"");
        br.append(checkDate4Mapping(this.getPeppValidFrom()));
        br.append("\";\"");
        br.append(checkDate4Mapping(this.getPeppValidTo()));
        br.append("\";");
        br.append(this.getPeppPepp());
        br.append(";");
        br.append(this.getPeppIsNegotiatedFl() ? '1' : '0');
        br.append(';');
        br.append(this.getPeppIsDayCareFl() ? '1' : '0');
        br.append(';');
        br.append(getRelation2DrgmMapping());
        return br.toString();
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, "Error on creating of the DRGM String for pepp: " + ex.getMessage(), ex);
//        }
//        return null;
    }

    /**
     * liefert eine Relationsdarstellung als String für ausgabe in die drgm -
     * Datei
     *
     * @return Relation als String
     */
    @Transient
    public String getRelation2DrgmMapping() {
        StringBuilder br = new StringBuilder();
        br.append(String.valueOf(this.getPeppRelationFrom()));
        br.append(';');
        br.append(String.valueOf(this.getPeppRelationTo()));
        br.append(';');
        br.append(String.valueOf(checkFloat4Mapping(PEPP, getPeppRelationCostWeight())));
        br.append(';');
        return br.toString();
    }

    @Override
    protected boolean showDrgmDate() {
        return getPeppIsNegotiatedFl() && getPeppHosIdent() != null && !getPeppHosIdent().isEmpty();
    }
}
