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
import de.lb.cpx.model.enums.AntibiogramResultEn;
import de.lb.cpx.model.enums.AntibioticEn;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * TCase initially generated at 21.01.2016 17:07:59 by Hibernate Tools 3.2.2.GA
 * <p style="font-size:1em; color:green;"> Die Tabelle "T_CASE" speichert alle
 * Krankenhausfälle, die in die Datenbank eingelesen wurden. </p>
 */
@Entity
@Table(name = "T_MIBI_ANTIBIOGRAM")
@SuppressWarnings("serial")
public class TMibiAntibiogram extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    private TMibi mibi;
    private TMibiAppraisal mbanAppraisal;
    private AntibioticEn mbanAntibiotic;
    private AntibiogramResultEn mbanResult;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_MIBI_ANTIBIOGRAM_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "T_MIBI_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_MBAN4MB"))
    //@JsonBackReference(value = "labor")
    public TMibi getMibi() {
        return this.mibi;
    }

    public void setMibi(final TMibi mibi) {
        this.mibi = mibi;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "T_MIBI_APPRAISAL_ID", foreignKey = @ForeignKey(name = "FK_T_MIBI_AN4T_MINI_AP_ID"), nullable = false)
    public TMibiAppraisal getMbanAppraisal() {
        return this.mbanAppraisal;
    }

    public void setMbanAppraisal(final TMibiAppraisal mbanAppraisal) {
        this.mbanAppraisal = mbanAppraisal;
    }

    /**
     * @return the mbanAntibiotic
     */
    @Column(name = "MBAN_ANTIBIOTIC", length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    public AntibioticEn getMbanAntibiotic() {
        return mbanAntibiotic;
    }

    /**
     * @param mbanAntibiotic the mbanAntibiotic to set
     */
    public void setMbanAntibiotic(AntibioticEn mbanAntibiotic) {
        this.mbanAntibiotic = mbanAntibiotic;
    }

    /**
     * @return the mbarResult
     */
    @Column(name = "MBAN_RESULT", length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    public AntibiogramResultEn getMbanResult() {
        return mbanResult;
    }

    /**
     * @param mbanResult the mbanResult to set
     */
    public void setMbanResult(AntibiogramResultEn mbanResult) {
        this.mbanResult = mbanResult;
    }

//    @Override
//    public boolean versionEquals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        if (obj == null) {
//            return false;
//        }
//        if (!(obj instanceof TMibiAntibiogram)) {
//            return false;
//        }
//        final TMibiAntibiogram other = (TMibiAntibiogram) obj;
//////        if (!isStringEquals(this.csdInsCompany, other.getCsdInsCompany())) {
//////            return false;
//////        }
//        if (!Objects.equals(this.mbanResult, other.getMbanResult())) {
//            return false;
//        }
//
//        return true;
//    }
}
