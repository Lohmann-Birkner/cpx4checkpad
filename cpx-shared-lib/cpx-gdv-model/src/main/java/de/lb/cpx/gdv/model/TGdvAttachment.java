/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.gdv.model;

import de.lb.cpx.server.commons.dao.AbstractVersionEntity;
import java.util.Objects;
import java.util.logging.Logger;
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
@Table(name = "T_GDV_ATTACHMENT",
        indexes = {
            @Index(name = "IDX_GDV_ATTACH4_IN_DOC_ID", columnList = "T_GDV_IN_DOCUMENT_ID", unique = false)})

public class TGdvAttachment extends AbstractVersionEntity{

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(TGdvAttachment.class.getName());
    private String damageReportNumber;
    private String attachmentPath;
    private String attachmentName;
    private TGdvInDocument gdvInDocument;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_GDV_ATTACHMENT_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "DAMAGE_REPORT_NUMBER", length = 20, nullable = false)    
    public String getDamageReportNumber() {
        return damageReportNumber;
    }

    public void setDamageReportNumber(String damageReportNumber) {
        this.damageReportNumber = damageReportNumber;
    }

    @Column(name = "ATTACHMENT_PATH", length = 255, nullable = false)
    public String getAttachmentPath() {
        return attachmentPath;
    }

    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    @Column(name = "ATTACHMENT_NAME", length = 100, nullable = false)
    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "T_GDV_IN_DOCUMENT_ID", nullable = true, foreignKey = @ForeignKey(name = "FK_ATTACHMENT_2_IN_DOC_ID"))
    public TGdvInDocument getGdvInDocument() {
        return gdvInDocument;
    }

    public void setGdvInDocument(TGdvInDocument gdvInDocument) {
        this.gdvInDocument = gdvInDocument;
    }


    @Override
    public boolean versionEquals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (!(object instanceof TGdvAttachment)) {
            return false;
        }
        final TGdvAttachment other = (TGdvAttachment) object;
        if (!Objects.equals(this.damageReportNumber, other.damageReportNumber)) {
            return false;
        }
        if (!Objects.equals(this.attachmentName, other.attachmentName)) {
            return false;
        }
        if (!Objects.equals(this.attachmentPath, other.attachmentPath)) {
            return false;
        }
        return this.gdvInDocument.versionEquals (other.gdvInDocument);

    }
    
}
