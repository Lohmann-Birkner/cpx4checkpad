/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.gdv.model;

import de.lb.cpx.server.commons.dao.AbstractVersionEntity;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 *
 * @author gerschmann
 */
@Entity
@Table(name = "T_GDV_IN_DOCUMENT")
public class TGdvInDocument  extends AbstractVersionEntity{

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(TGdvInDocument.class.getName());
    private String fileName;
    private String filePath;
    private long fileSize;
    private byte[] fileContent;
    
    private Set<TGdvAttachment> attchments = new HashSet<>(0);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_GDV_IN_DOCUMENT_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    
    @Column(name = "FILE_NAME", length = 100, nullable = false)
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Column(name = "FILE_PATH", length = 255, nullable = false)
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Column(name = "FILE_SIZE", precision = 10, scale = 0, insertable = true, updatable = false, nullable = false)
    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name="FILE_CONTENT", insertable = true, updatable = false)
    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "gdvInDocument", orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Set<TGdvAttachment> getAttchments() {
        return attchments;
    }

    public void setAttchments(Set<TGdvAttachment> attchments) {
        this.attchments = attchments;
    }

    
    @Override
    public boolean versionEquals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (!(object instanceof TGdvInDocument)) {
            return false;
        }
        final TGdvInDocument other = (TGdvInDocument) object;
        if (!(this.fileContent == other.fileContent)) {
            return false;
        }
        if (!Objects.equals(this.filePath, other.filePath)) {
            return false;
        }
        if (!Objects.equals(this.fileName, other.fileName)) {
            return false;
        }
        return Objects.equals(this.fileSize, other.fileSize);
    }
    
}
