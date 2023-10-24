/*
 * Copyright (c) 2021 Lohmann & Birkner.
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
 *    2021  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import de.lb.cpx.server.commons.dao.AbstractEntity;
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

/**
 *
 * @author gerschmann
 */
@Entity
@Table(name = "T_CASE_2_RULE_SELECTION",
        indexes = {
            @Index(name = "IDX_CASE_2_RULE_SELECTION_ID", columnList = "T_CASE_ID", unique = false)})
@SuppressWarnings("serial")
public class TCase2RuleSelection extends  AbstractEntity {


    private static final long serialVersionUID = 1L;

//    private long id;
    private TCase hospitalCase;
    private String ruleid;
//    private TCaseDetails firstKisVersion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "T_CASE_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_CASE_RULE_SELECTION_CASE_ID"))
    public TCase getHospitalCase() {
        return hospitalCase;
    }

    public void setHospitalCase(TCase hospitalCase) {
        this.hospitalCase = hospitalCase;
    }

    @Column(name = "CRGR_ID", length = 25, nullable = false)
    public String getRuleid() {
        return ruleid;
    }

    public void setRuleid(String ruleid) {
        this.ruleid = ruleid;
    }
//
//    @Column(name = "FIRST_KIS_VERS_ID", nullable = false)
//    public TCaseDetails getFirstKisVersion() {
//        return firstKisVersion;
//    }
//
//    public void setFirstKisVersion(TCaseDetails firstKisVersion) {
//        this.firstKisVersion = firstKisVersion;
//    }

   @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_CASE_2_RULE_SELECTION_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }
    
    public void setId(long pId){
        id = pId;
    }

}
