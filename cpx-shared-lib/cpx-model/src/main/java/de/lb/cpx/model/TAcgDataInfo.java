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
import com.fasterxml.jackson.annotation.JsonManagedReference;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * TCase initially generated at 21.01.2016 17:07:59 by Hibernate Tools 3.2.2.GA
 * <p style="font-size:1em; color:green;"> Die Tabelle "T_CASE" speichert alle
 * Krankenhausfälle, die in die Datenbank eingelesen wurden. </p>
 */
@Entity
@Table(name = "T_ACG_DATA_INFO")
@SuppressWarnings("serial")
public class TAcgDataInfo extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    private String acgName;
    private String acgDescription;
    private int acgPeriod;
    private int acgYear;

    private Set<TAcgData> acgData = new HashSet<>(0);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_ACG_DATA_INFO_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @Column(name = "ACG_NAME", length = 100, nullable = false)
    public String getAcgName() {
        return acgName;
    }

    public void setAcgName(String acgName) {
        this.acgName = acgName;
    }

    @Column(name = "ACG_DESCRIPTION", length = 255, nullable = true)
    public String getAcgDescription() {
        return acgDescription;
    }

    public void setAcgDescription(String acgDescription) {
        this.acgDescription = acgDescription;
    }
       

    @Column(name = "ACG_PERIOD", nullable = false)
    public int getAcgPeriod() {
        return acgPeriod;
    }

    public void setAcgPeriod(int acgPeriod) {
        this.acgPeriod = acgPeriod;
    }

    @Column(name = "ACG_YEAR", nullable = false)
    public int getAcgYear() {
        return acgYear;
    }

    public void setAcgYear(int acgYear) {
        this.acgYear = acgYear;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "acgDataInfo", orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference(value = "acgDataInfo")
    public Set<TAcgData> getAcgData() {
        return this.acgData;
    }

    public void setAcgData(final Set<TAcgData> acgData) {
        this.acgData = acgData;
    }

}
