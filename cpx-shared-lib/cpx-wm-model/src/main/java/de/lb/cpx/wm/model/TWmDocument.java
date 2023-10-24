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
 * Contributors:
 *    2016  Husser - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.wm.model;

import de.lb.cpx.server.commons.dao.AbstractVersionEntity;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.annotations.ColumnDefault;

/**
 * <p style="font-size:1em; color:green;"> Tabelle der Dokument. </p>
 *
 * @author Husser
 */
@Entity
@Table(name = "T_WM_DOCUMENT",
        indexes = {
            @Index(name = "IDX_WM_DOCUMENT4PROCESS_ID", columnList = "T_WM_PROCESS_ID", unique = false)})
//        @Index(name = "FK_WM_DOCUMENT4CASE_ID", columnList = "T_CASE_ID", unique = false)}

public class TWmDocument extends AbstractVersionEntity {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(TWmDocument.class.getName());

    private TWmProcess process;

    //    private TCase hosCase;
    private String name;

    private String filePath;

//    private WmDocumentType documentType;
    private Long documentType;
    private Date documentDate;

//    private long modificationUserId;
    private byte[] content;

    private long fileSize;

    public TWmDocument() {

    }

    public TWmDocument(long id, TWmProcess process, String name, String filePath, Long documentType /*, long modificationUserId */,
            long fileSize, Date creationDate, Long creationUser, Date modificationDate, Long modificationUser,Date pDocumentDate, long version) {
        this.id = id;
        this.process = process;
        this.name = name;
        this.filePath = filePath;
        this.documentType = documentType;
//        this.modificationUserId = modificationUserId;
        this.fileSize = fileSize;
        this.creationDate = creationDate;
        this.creationUser = creationUser;
        this.modificationDate = modificationDate;
        this.modificationUser = modificationUser;
        this.documentDate = pDocumentDate;
        this.version = version;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_WM_DOCUMENT_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    /**
     * Gibt Verweis auf ID der Tabelle WF_PROCESS. zurück.
     *
     * @return process
     */
    @ManyToOne
//    @JoinColumn(name = "PROCESS_ID", nullable = false)
    @JoinColumn(name = "T_WM_PROCESS_ID", foreignKey = @ForeignKey(name = "FK_WM_DOCUMENT4PROCESS_ID"))
    public TWmProcess getProcess() {
        return process;
    }

    /**
     * Gibt Name des Dokumentes zurück.
     *
     * @return name
     */
    @Column(name = "NAME", length = 100, nullable = false)
    public String getName() {
        return name;
    }

    @Transient
    public String getExtension() {
        String fn = getName();
        if (fn == null) {
            return null;
        }
        int pos = fn.lastIndexOf('.');
        if (pos > 0) {
            return fn.substring(pos + 1).trim();
        }
        return "";
    }

    /**
     * Gibt Relativer Pfad vom CPX-server zu dem Dokument auf dem Filesystem
     * zurück.
     *
     * @return filePath
     */
    @Column(name = "FILEPATH", length = 2000)
    public String getFilePath() {
        return filePath;
    }

    /**
     * Gibt die Dateigröße in Byte zurück
     *
     * @return fileSize
     */
    @Column(name = "FILESIZE", precision = 10, scale = 0, insertable = true, updatable = false, nullable = false)
    @ColumnDefault("0")
    public long getFileSize() {
        return fileSize;
    }

    /**
     * Gibt Art des DOCUMENTS zurück.z.b.MDK-Gutachten.
     *
     * @return documentType
     */
    @Column(name = "DOCUMENT_TYPE")
//    @Enumerated(EnumType.STRING)
//    public WmDocumentType getDocumentType() {
//        return documentType;
//    }
    public Long getDocumentType() {
        return documentType;
    }

//    @Column(name = "MODIFICATION_USER_ID")
//    public long getModificationUserId() {
//        return modificationUserId;
//    }
    /**
     * Gibt Dokumenten-Inhalt zurück.
     *
     * @return content
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(insertable = true, updatable = false)
    public byte[] getContent() {
        return content == null ? null : content.clone();
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @param process Column T_WM_PROCESS_ID :Verweis auf ID der Tabelle
     * WF_PROCESS.
     */
    public void setProcess(TWmProcess process) {
        this.process = process;
    }

    /**
     *
     * @param name Column NAME : Name des Dokumentes.
     */
    public void setName(String name) {
        String n = name;
        final int MAX_LENGTH = 100;
        if (n != null && n.length() > MAX_LENGTH) {
            int pos = n.lastIndexOf('.');
            String ext = "";
            if (pos > -1) {
                ext = n.substring(pos);
            }
            int cutLength = n.length() - MAX_LENGTH;
            n = n.substring(0, pos - cutLength) + ext;
        }
        this.name = n;
    }

    /**
     *
     * @param fileSize Gibt die Dateigröße in Byte zurück
     */
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
    /**
     *
     * @param filePath Column FILEPATH :Relativer Pfad vom CPX-server zu dem
     * Dokument auf dem Filesystem .
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     *
     * @param documentType Column DOCUMENT_TYPE :Art des DOCUMENTS
     * (MDK-Gutachten). Why the hell is document type a string?!
     */
//    public void setDocumentType(WmDocumentType documentType) {
//        this.documentType = documentType;
//    }
    public void setDocumentType(Long documentType) {
        this.documentType = documentType;
    }

    /**
     *
     * @return documentDate
     */
    @Column(name = "DOCUMENT_DATE")
    @Temporal(TemporalType.DATE)
    public Date getDocumentDate() {
        return documentDate;
    }

    /**
     *
     * @param documentDate documentDate
     */
    public void setDocumentDate(Date documentDate) {
        this.documentDate = documentDate;
    }

//    public void setModificationUserId(long modificationUserId) {
//        this.modificationUserId = modificationUserId;
//    }
    /**
     *
     * @param content Column CONTENT: Dokumenten-Inhalt .
     */
    public void setContent(byte[] content) {
        this.content = content == null ? null : content.clone();
        fileSize = content == null ? 0L : content.length;
    }

    @Transient
    public boolean isDatabaseContentDocument() {
        final boolean result = getFilePath() == null || getFilePath().trim().isEmpty();
        //Don't check content, because it can be null when passed to client (for better performance)
//        if (result) {
//            if (getContent() == null) {
//                LOG.log(Level.SEVERE, "Database document with id " + id + " has database content that is null!");
//            } else if (getContent().length == 0) {
//                LOG.log(Level.SEVERE, "Database document with id " + id + " has empty database content!");
//            }
//        }
        return result;
    }

    @Transient
    public boolean isFileContentDocument() {
        final boolean result = !isDatabaseContentDocument();
        if (result) {
            if (getContent() != null) {
                LOG.log(Level.SEVERE, "File system document with id " + id + " is ambigious! It specifies a file path but has database content as well!");
            }
        }
        return result;
    }

//    /**
//     * Gibt Verweis auf ID der Tablle T_CASE zurück.
//     *
//     * @return hosCase
//     */
//    @ManyToOne
//    @JoinColumn(name = "T_CASE_ID", nullable = true,foreignKey = @ForeignKey(name = "FK_WM_DOCUMENT4CASE_ID"))
//    public TCase getHosCase() {
//        return hosCase;
//    }
//
//    /**
//     * @param hosCase Column T_CASE_ID: Verweis auf ID der Tablle T_CASE.
//     */
//    public void setHosCase(TCase hosCase) {
//        this.hosCase = hosCase;
//    }
    @Override
    public boolean versionEquals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (!(object instanceof TWmDocument)) {
            return false;
        }
        final TWmDocument other = (TWmDocument) object;
        //if (!Objects.equals(this.content, other.content)) {
        if (!(this.content == other.content)) {
            return false;
        }
        if (!Objects.equals(this.filePath, other.filePath)) {
            return false;
        }
//        if (!Objects.equals(this.modificationUserId, other.modificationUserId)) {
//            return false;
//        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.process, other.process)) {
            return false;
        }
        if (!Objects.equals(this.documentType, other.documentType)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String documentId = String.valueOf(this.id);
        final String hash = Integer.toHexString(this.hashCode());
        return "doc_" + name + ";id_" + documentId + "@" + hash;
    }

}
