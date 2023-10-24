/* 
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.commonDB.model;

import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.io.File;
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
import javax.persistence.Transient;

/**
 * This entity is used to create C_Drafts table in a database, which can be used
 * to deal with the template generation mechanism.
 *
 * @author nandola
 */
@Entity
@Table(name = "C_DRAFTS")
@SuppressWarnings("serial")
public class CDrafts extends AbstractEntity {

    private static final long serialVersionUID = 1L;

//    private String perm_name;
    private String draftName;
    private String draftDir;
    private String draftDescription;
    private byte[] draftContent;
    private Long category1;
    private Long category2;
    private Long category3;
    private String filenameProPosal;//FILENAME_PROPOSAL 

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_Drafts_SQ", allocationSize = 1)

    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @return filenameProPosal Filename Wildcard , can contains %v%
     * workflownummer , %f% casenumber , %p% Insured number of the patient , %i%
     * IKZ of insurance
     */
    @Column(name = "FILENAME_PROPOSAL", length = 100)
    public String getFilenameProPosal() {
        return this.filenameProPosal;
    }

    /**
     *
     * @param filenameProPosal set filenameProPosal
     */
    public void setFilenameProPosal(String filenameProPosal) {
        this.filenameProPosal = filenameProPosal;
    }

    @Column(name = "DRAFT_NAME", length = 100)
    //   @Enumerated(EnumType.STRING)
    public String getDraftName() {
        return this.draftName;
    }

    public void setDraftName(String draft_name) {
        this.draftName = draft_name;
    }

    @Column(name = "DRAFT_DIR", nullable = false, length = 200)
//    @Enumerated(EnumType.STRING)
    public String getDraftDir() {
        return this.draftDir;
    }

    public void setDraftDir(String draft_dir) {
        this.draftDir = draft_dir;
    }

    @Column(name = "DRAFT_DESCRIPTION", length = 500)
//    @Enumerated(EnumType.STRING)
    public String getDraftDescription() {
        return this.draftDescription;
    }

    /**
     * gives the pure file name (draft directory but without path)
     *
     * @return draft file name
     */
    @Transient
    public String getDraftFile() {
        String dir = this.draftDir;
        if (dir == null || dir.trim().isEmpty()) {
            return "";
        }
        File file = new File(dir);
        return file.getName();
    }

    public void setDraftDescription(String draft_description) {
        this.draftDescription = draft_description;
    }

    /*
    // Indicates whether this template is marked as primary. (0 = not primary, 1 = primary)
    @Column(name = "DRAFT_ISPRIMARY", length = 3)
    public Integer getDraftIsPrimary() {
        return draftIsPrimary;
    }

    public void setDraftIsPrimary(Integer draftIsPrimary) {
        this.draftIsPrimary = (draftIsPrimary != null && draftIsPrimary.equals(0)) ? null : draftIsPrimary;
    }
     */
    @Lob
    @Column(name = "DRAFT_CONTENT")
    @Basic(fetch = FetchType.LAZY)
    public byte[] getDraftContent() {
        return draftContent == null ? null : draftContent.clone();
    }

    public void setDraftContent(byte[] draft_content) {
        this.draftContent = draft_content == null ? null : draft_content.clone();
    }

    @Column(name = "CATEGORY1", nullable = true)
    public Long getCategory1() {
        return this.category1;
    }

    public void setCategory1(Long pCategory1) {
        this.category1 = pCategory1;
    }

    @Column(name = "CATEGORY2", nullable = true)
    public Long getCategory2() {
        return this.category2;
    }

    public void setCategory2(Long pCategory) {
        this.category2 = pCategory;
    }

    @Column(name = "CATEGORY3", nullable = true)
    public Long getCategory3() {
        return this.category3;
    }

    public void setCategory3(Long pCategory) {
        this.category3 = pCategory;
    }
}
