/* 
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.model;

//import de.lb.cpx.model.converter.CsCaseTypeConverter;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * TCase initially generated at 21.01.2016 17:07:59 by Hibernate Tools 3.2.2.GA
 * <p style="font-size:1em; color:green;"> Die Tabelle "T_CASE" speichert alle
 * Krankenhausfälle, die in die Datenbank eingelesen wurden. </p>
 */
@Entity
@Table(name = "T_MIBI")
@SuppressWarnings("serial")
public class TMibi extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    private TCase hospitalCase;
    private Date mbDate;
    private String mbProvider;
    private String mbRequester;
    private String mbText;
    private String mbRequirement;
    private String mbMaterial;
    private String mbAppraisal;
    private String mbAntibiogram;

    private Set<TMibiAntibiogram> antibiogram = new HashSet<>(0);
    private Set<TMibiAppraisal> appraisal = new HashSet<>(0);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_MIBI_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    /**
     * Gibt Verweis auf die ID der Tabelle T_CASE zurück
     *
     * @return hospitalCase
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "T_CASE_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_MB4T_CASE_ID"))
    //@JsonBackReference(value = "labor")
    public TCase getHospitalCase() {
        return this.hospitalCase;
    }

    /**
     *
     * @param hospitalCase Column T_CASE_DETAILS_ID:Verweis auf die ID der
     * Tabelle T_CASE.
     */
    public void setHospitalCase(final TCase hospitalCase) {
        this.hospitalCase = hospitalCase;
    }

    /**
     * @return the mbDate
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MB_DATE", length = 7, nullable = false)
    public Date getMbDate() {
        return this.mbDate == null ? null : new Date(this.mbDate.getTime());
    }

    /**
     * @param mbDate the mbDate to set
     */
    public void setMbDate(Date mbDate) {
        this.mbDate = mbDate == null ? null : new Date(mbDate.getTime());
    }

    /**
     * @return the mbProvider
     */
    @Column(name = "MB_PROVIDER", length = 50, nullable = false)
    public String getMbProvider() {
        return mbProvider;
    }

    /**
     * @param mbProvider the mbProvider to set
     */
    public void setMbProvider(String mbProvider) {
        this.mbProvider = mbProvider;
    }

    /**
     * @return the mbRequester
     */
    @Column(name = "MB_REQUESTER", length = 50, nullable = true)
    public String getMbRequester() {
        return mbRequester;
    }

    /**
     * @param mbRequester the mbRequester to set
     */
    public void setMbRequester(String mbRequester) {
        this.mbRequester = mbRequester;
    }

    /**
     * @return the mbText
     */
    @Lob
    @Column(name = "MB_TEXT", length = 6000, nullable = false)
    //@Basic(fetch = FetchType.LAZY)
    public String getMbText() {
        return mbText;
    }

    /**
     * @param mbText the mbText to set
     */
    public void setMbText(String mbText) {
        this.mbText = mbText;
    }

    /**
     * @return the mbRequirement
     */
    @Lob
    @Column(name = "MB_REQUIREMENT", length = 6000, nullable = true)
    //@Basic(fetch = FetchType.LAZY)
    public String getMbRequirement() {
        return mbRequirement;
    }

    /**
     * @param mbRequirement the mbMaterial to set
     */
    public void setMbRequirement(String mbRequirement) {
        this.mbRequirement = mbRequirement;
    }

    /**
     * @return the mbMaterial
     */
    @Lob
    @Column(name = "MB_MATERIAL", length = 6000, nullable = false)
    //@Basic(fetch = FetchType.LAZY)
    public String getMbMaterial() {
        return mbMaterial;
    }

    /**
     * @param mbMaterial the mbMaterial to set
     */
    public void setMbMaterial(String mbMaterial) {
        this.mbMaterial = mbMaterial;
    }

    /**
     * @return the mbAppraisal
     */
    @Lob
    @Column(name = "MB_APPRAISAL", length = 6000, nullable = true)
    //@Basic(fetch = FetchType.LAZY)
    public String getMbAppraisal() {
        return mbAppraisal;
    }

    /**
     * @param mbAppraisal the mbAppraisal to set
     */
    public void setMbAppraisal(String mbAppraisal) {
        this.mbAppraisal = mbAppraisal;
    }

    /**
     * @return the mbAntibiogram
     */
    @Lob
    @Column(name = "MB_ANTIBIOGRAM", length = 6000, nullable = true)
    //@Basic(fetch = FetchType.LAZY)
    public String getMbAntibiogram() {
        return mbAntibiogram;
    }

    /**
     * @param mbAntibiogram the mbAntibiogram to set
     */
    public void setMbAntibiogram(String mbAntibiogram) {
        this.mbAntibiogram = mbAntibiogram;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mibi", orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    //@JsonManagedReference(value = "details")
    public Set<TMibiAntibiogram> getAntibiogram() {
        return this.antibiogram;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mibi", orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    //@JsonManagedReference(value = "details")
    public Set<TMibiAppraisal> getAppraisal() {
        return this.appraisal;
    }

    public void setAntibiogram(final Set<TMibiAntibiogram> antibiogram) {
        this.antibiogram = antibiogram;
    }

    public void setAppraisal(final Set<TMibiAppraisal> appraisal) {
        this.appraisal = appraisal;
    }

//    @Override
//    public boolean versionEquals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        if (obj == null) {
//            return false;
//        }
//        if (!(obj instanceof TMibi)) {
//            return false;
//        }
//        final TMibi other = (TMibi) obj;
////        if (!isStringEquals(this.csdInsCompany, other.getCsdInsCompany())) {
////            return false;
////        }
//        if (!Objects.equals(this.mbDate, other.getMbDate())) {
//            return false;
//        }
//        if (!Objects.equals(this.mbText, other.getMbText())) {
//            return false;
//        }
//        if (!Objects.equals(this.mbMaterial, other.getMbMaterial())) {
//            return false;
//        }
//        if (!Objects.equals(this.mbAppraisal, other.getMbAppraisal())) {
//            return false;
//        }
//        if (!Objects.equals(this.mbAntibiogram, other.getMbAntibiogram())) {
//            return false;
//        }
//
//        return true;
//    }
}
