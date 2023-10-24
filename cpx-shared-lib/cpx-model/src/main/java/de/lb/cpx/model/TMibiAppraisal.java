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
import de.lb.cpx.model.converter.GermGrowthConverter;
import de.lb.cpx.model.enums.GermGrowthEn;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
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
@Table(name = "T_MIBI_APPRAISAL")
@SuppressWarnings("serial")
public class TMibiAppraisal extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    private TMibi mibi;
    private Integer mbapPosition;
    private String mbapGerm;
    private GermGrowthEn mbapGrowth;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_MIBI_APPRAISAL_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "T_MIBI_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_MBAP4MB"))
    //@JsonBackReference(value = "labor")
    public TMibi getMibi() {
        return this.mibi;
    }

    public void setMibi(final TMibi mibi) {
        this.mibi = mibi;
    }

    /**
     * @return the mbapPosition
     */
    @Column(name = "MBAP_POSITION", nullable = true)
    public Integer getMbapPosition() {
        return mbapPosition;
    }

    /**
     * @param mbapPosition the mbapPosition to set
     */
    public void setMbapPosition(Integer mbapPosition) {
        this.mbapPosition = mbapPosition;
    }

    /**
     * @return the mbapGerm
     */
    @Column(name = "MBAP_GERM", length = 255, nullable = false)
    public String getMbapGerm() {
        return mbapGerm;
    }

    /**
     * @param mbapGerm the mbapGerm to set
     */
    public void setMbapGerm(String mbapGerm) {
        this.mbapGerm = mbapGerm;
    }

    /**
     * @return the mbapGrowth
     */
    @Column(name = "MBAP_GROWTH", nullable = false)
    @Convert(converter = GermGrowthConverter.class)
    public GermGrowthEn getMbapGrowth() {
        return mbapGrowth;
    }

    /**
     * @param mbapGrowth the mbapGrowth to set
     */
    public void setMbapGrowth(GermGrowthEn mbapGrowth) {
        this.mbapGrowth = mbapGrowth;
    }

//    @Override
//    public boolean versionEquals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        if (obj == null) {
//            return false;
//        }
//        if (!(obj instanceof TMibiAppraisal)) {
//            return false;
//        }
//        final TMibiAppraisal other = (TMibiAppraisal) obj;
////        if (!isStringEquals(this.csdInsCompany, other.getCsdInsCompany())) {
////            return false;
////        }
//        if (!Objects.equals(this.mbapGerm, other.getMbapGerm())) {
//            return false;
//        }
//        if (!Objects.equals(this.mbapGrowth, other.getMbapGrowth())) {
//            return false;
//        }
//
//        return true;
//    }
}
