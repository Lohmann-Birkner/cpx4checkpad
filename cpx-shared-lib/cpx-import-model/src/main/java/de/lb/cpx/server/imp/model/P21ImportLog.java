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
package de.lb.cpx.server.imp.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 *
 * @author Wilde
 */
@Entity
@Table(name = "T_IMPORT_LOG")
@SuppressWarnings("serial")
public class P21ImportLog implements Serializable {

    private String content;
    private String identifier;
    private String importId;
    private long id;
    private long version;
    private P21UploadDataType type;

    @Column(name = "content", nullable = false)
    public String getContent() {
        return content;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_IMPORT_LOG_SQ")
    public long getId() {
        return this.id;
    }

    @Column(name = "identifier", nullable = false)
    public String getIdentifier() {
        return identifier;
    }

    @Column(name = "import_id", nullable = false)
    public String getImportId() {
        return importId;
    }

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    public P21UploadDataType getType() {
        return type;
    }

    @Column(name = "version", unique = false, nullable = false)
    @Version
    public long getVersion() {
        return version;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public void setIdentifier(final String identifier) {
        this.identifier = identifier;
    }

    public void setImportId(final String importId) {
        this.importId = importId;
    }

    public void setType(final P21UploadDataType type) {
        this.type = type;
    }

    public void setVersion(final long version) {
        this.version = version;
    }

}
