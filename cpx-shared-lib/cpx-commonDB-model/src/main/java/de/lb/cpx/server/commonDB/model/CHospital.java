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

import de.lb.cpx.model.converter.IdentClassConverter;
import de.lb.cpx.model.converter.StateConverter;
import de.lb.cpx.model.enums.CountryEn;
import de.lb.cpx.model.enums.CpxEnumInterface;
import de.lb.cpx.model.enums.IdentClassEn;
import de.lb.cpx.model.enums.StateEn;
import de.lb.cpx.server.commons.dao.AbstractCatalogEntity;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * CHospital initially generated at 03.02.2016 10:32:45 by Hibernate Tools
 * 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;">C_HOSPITAL : Tabelle der
 * Krankenhäuser .</p>
 *
 */
@Entity
@Table(name = "C_HOSPITAL")
@SuppressWarnings("serial")
public class CHospital extends AbstractCatalogEntity {

    private static final long serialVersionUID = 1L;

//     private long id;
    private CountryEn countryEn;
    private StateEn stateEn;
    private String hosIdent;
    private String hosName;
    private String hosZipCode;
    private String hosCity;
    private String hosAddress;
    private Set<CWard> CWards = new HashSet<>(0);
    private IdentClassEn hosIdentClass;
    private String hosComment;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_HOSPITAL_SQ", allocationSize = 1)
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
     * @return pStateEn: Enumeration für Bundesland 01-16.
     */
    @Column(name = "STATE_EN", length = 25)
    @Convert(converter = StateConverter.class)
    public StateEn getStateEn() {
        return stateEn;
    }

    /**
     *
     * @param pStateEn Column STATE_EN: Enumeration für Bundesland 01-16.
     */
    public void setStateEn(final String pStateEn) {
        String state = (pStateEn == null) ? "" : pStateEn.trim();
        if (state.length() == 1) {
            state = "0" + state;
        }
        StateEn enumeration;
        if (state.isEmpty()) {
            enumeration = null;
        } else if ("21".equals(state) || "25".equals(state)) {
            //Don't know what means 21/25 in the identifier
            enumeration = null;
        } else if ("97".equals(state) || "98".equals(state) || "99".equals(state)) {
            //classification 97 to 99 is reserved for internal use if there is no permanently identifier assigned yet:
            //3.5 Fehlen eines IK
            //Für Zahlungsempfänger, die noch kein IK erhalten haben, ist die Vergabe zu veranlassen.
            //Der zahlungspflichtige Träger kann verwaltungsintern ein Kennzeichen unter den Klassifikationen
            //97 bis 99 verwenden. Kennzeichen mit den Klassifikationen 97 bis 99 dürfen jedoch
            //im Verhältnis zum Zahlungsempfänger nicht als IK bezeichnet und im Verhältnis zu anderen
            //Institutionen nicht angegeben werden.
            enumeration = null;
        } else {
            enumeration = (StateEn) CpxEnumInterface.findEnum(StateEn.values(), state);
        }
        setStateEn(enumeration);
    }

    /**
     *
     * @param stateEn Column STATE_EN: Enumeration für Bundesland 01-16.
     */
    public void setStateEn(StateEn stateEn) {
        this.stateEn = stateEn;
    }

    /**
     *
     * @return hosIdent: Ident Nummer des Krankenhauses.
     */
    @Column(name = "HOS_IDENT", nullable = false, length = 10)
    public String getHosIdent() {
        return this.hosIdent;
    }

    /**
     *
     * @param hosIdent Column HOS_IDENT: Ident Nummer des Krankenhauses.
     */
    public void setHosIdent(String hosIdent) {
        this.hosIdent = hosIdent;
    }

    @Column(name = "HOS_COMMENT",  length = 255)
    public String getHosComment() {
        return hosComment;
    }

    public void setHosComment(String hosComment) {
        this.hosComment = hosComment;
    }

    /**
     *
     * @return hosName: Name des Krankenhauses.
     */
    @Column(name = "HOS_NAME")
    public String getHosName() {
        return this.hosName;
    }

    /**
     *
     * @param hosName Column HOS_NAME: Name des Krankenhauses.
     */
    public void setHosName(String hosName) {
        this.hosName = hosName;
    }

    /**
     *
     * @return hosZipCode : Postleitzahl der Krankenhauses.
     */
    @Column(name = "HOS_ZIP_CODE", length = 5)
    public String getHosZipCode() {
        return this.hosZipCode;
    }

    /**
     *
     * @param hosZipCode Column HOS_ZIP_CODE : Postleitzahl der Krankenhauses.
     */
    public void setHosZipCode(String hosZipCode) {
        this.hosZipCode = hosZipCode;
    }

    /**
     *
     * @return hosCity: Stadt des Krankenhauses.
     */
    @Column(name = "HOS_CITY", nullable = true, length = 50)
    public String getHosCity() {
        return hosCity;
    }

    /**
     * @param hosCity Column HOS_CITY: Stadt des Krankenhauses.
     */
    public void setHosCity(String hosCity) {
        this.hosCity = hosCity;
    }

    /**
     *
     * @return hosAddress: Adresse des Krankenhauses.
     */
    @Column(name = "HOS_ADDRESS")
    public String getHosAddress() {
        return this.hosAddress;
    }

    /**
     *
     * @return identClass :Enumeration für Class der versicherungsträger 10-97
     * (Kranken,Renten,Unfall,Sozial,...).
     */
    @Column(name = "HOS_IDENT_CLASS", nullable = false, length = 25)
    //@Enumerated(EnumType.STRING)
    @Convert(converter = IdentClassConverter.class)
    public IdentClassEn getHosIdentClassEn() {
        return hosIdentClass;
    }

    /**
     *
     * @param identClass Column HOS_IDENT_CLASS: Enumeration für Class der
     * versicherungsträger 10-97 (Kranken,Renten,Unfall,Sozial,...).
     */
    public void setHosIdentClassEn(IdentClassEn identClass) {
        this.hosIdentClass = identClass;
    }

    /**
     *
     * @param hosAddress Column HOS_ADDRESS: Adresse des Krankenhauses.
     */
    public void setHosAddress(String hosAddress) {
        this.hosAddress = hosAddress;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "CHospital")
    public Set<CWard> getCWards() {
        return this.CWards;
    }

    public void setCWards(Set<CWard> CWards) {
        this.CWards = CWards;
    }

}
