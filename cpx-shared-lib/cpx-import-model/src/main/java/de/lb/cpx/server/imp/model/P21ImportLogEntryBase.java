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

import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Wilde
 */
@Entity
@Table(name = "T_IMPORT_LOG_ENTRY")
@SuppressWarnings("serial")
@DiscriminatorColumn(name = "LOG_ENTRY_TYPE")
public abstract class P21ImportLogEntryBase extends AbstractEntity implements Serializable {

    private String importId;
    private String text;
    private P21ImportLogLevel logLevel;

    public String buildCsvString() {
        final StringBuilder data = new StringBuilder();
        data.append(getLogLevel());
        data.append(";");
        data.append(getText());
        return data.toString();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_IMPORT_LOG_ENTRY_SQ")
    public long getId() {
        return this.id;
    }

    @Column(name = "IMPORT_ID", nullable = false)
    public String getImportId() {
        return importId;
    }

    @Column(name = "LOG_LEVEL", nullable = false)
    @Enumerated(EnumType.STRING)
    public P21ImportLogLevel getLogLevel() {
        return logLevel;
    }

    @Column(name = "TEXT", nullable = false)
    public String getText() {
        return text;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public final void setImportId(final String importId) {
        this.importId = importId;
    }

    public final void setLogLevel(final P21ImportLogLevel logLevel) {
        this.logLevel = logLevel;
    }

    public final void setText(final String text) {
        this.text = text;
    }

}
