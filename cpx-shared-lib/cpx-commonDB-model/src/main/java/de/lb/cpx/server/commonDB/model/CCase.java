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

import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * CAtc initially generated at 03.02.2016 10:32:45 by Hibernate Tools 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;"> C_ATC: Tabelle der ATC
 * (Anatomisch-Therapeutisch-Chemisches Klassifikationssystem).</p>
 *
 *
 */
@Entity
@Table(name = "C_CASE")
@SuppressWarnings("serial")
public class CCase extends AbstractEntity {

    private static final long serialVersionUID = 1L;

//     private long id;
    private long caseId;
    private String csHospitalIdent;
    private String csCaseNumber;
    private String db;
    private byte[] content;
    private String name;
    private String category;

    public CCase(long id,
            long caseId,
            String csHospitalIdent,
            String csCaseNumber,
            String db,
            long creationUser,
            Date creationDate,
            String name,
            String category) {
        this();
        this.id = id;
//        this.crgRuleTypes = type;
        this.caseId = caseId;
        this.csHospitalIdent = csHospitalIdent;
        this.csCaseNumber = csCaseNumber;
        this.db = db;
        setCreationUser(creationUser);
        setCreationDate(creationDate);
        this.name = name;
        this.category = category;
    }

    public CCase() {
        super();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_CASE_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gibt Dokumenten-Inhalt zurück.
     *
     * @return content
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    public byte[] getContent() {
        return content == null ? null : content.clone();
    }

    /**
     *
     * @param content Column CONTENT: Dokumenten-Inhalt .
     */
    public void setContent(byte[] content) {
        this.content = content == null ? null : content.clone();
    }

    /**
     * @return the caseId
     */
    @Column(name = "T_CASE_ID", nullable = false)
    public long getCaseId() {
        return caseId;
    }

    /**
     * @param caseId the caseId to set
     */
    public void setCaseId(long caseId) {
        this.caseId = caseId;
    }

    /**
     * @return the db
     */
    @Column(name = "DB", length = 100, nullable = false)
    public String getDb() {
        return db;
    }

    /**
     * @param db the db to set
     */
    public void setDb(String db) {
        this.db = db;
    }

    /**
     * Gibt Identifikationsnumme des Krankenhauses, das den Fall bearbeitet
     * zurück.
     *
     * @return csHospitalIdent Weitere Informationen in der Tabelle
     * CPX_common.C_HOSPITAL (HOS_IDENT)
     */
    @Column(name = "CS_HOSPITAL_IDENT", length = 10, nullable = false)
    public String getCsHospitalIdent() {
        return this.csHospitalIdent;
    }

    /**
     *
     * @param csHospitalIdent Column CS_HOSPITAL_IDENT:Identifikationsnumme des
     * Krankenhauses, das den Fall bearbeitet.
     */
    public void setCsHospitalIdent(final String csHospitalIdent) {
        this.csHospitalIdent = csHospitalIdent;
    }

    /**
     * Gibt Fallnummer in Bezug auf das Krankenhaus (Krankenhausinterne
     * Fallnummer) zurück. if casenumber ends with _c than the case is indicated
     * as canceled
     *
     * @return csCaseNumber
     */
    @Column(name = "CS_CASE_NUMBER", length = 25, nullable = false)
    public String getCsCaseNumber() {
        return this.csCaseNumber;
    }

    /**
     *
     * @param csCaseNumber Column CS_CASE_NUMBER :Krankenhausinterne Fallnummer.
     */
    public void setCsCaseNumber(final String csCaseNumber) {
        this.csCaseNumber = csCaseNumber;
    }

    @Column(name = "NAME", length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "CATEGORY", length = 50)
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
