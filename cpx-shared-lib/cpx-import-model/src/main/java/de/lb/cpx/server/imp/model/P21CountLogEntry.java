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
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 *
 * @author Wilde
 */
@Entity
@DiscriminatorValue(value = "COUNT_LOG")
@SuppressWarnings("serial")
public class P21CountLogEntry extends P21ImportLogEntryBase implements Serializable {

    private Long count;

    private Long measuredTime;

    protected P21CountLogEntry() {
    }

    public P21CountLogEntry(final P21ImportLogLevel logLevel, final String importId, final String text,
            final long measuredTime, final long count) {
        setLogLevel(logLevel);
        setText(text);
        setImportId(importId);
        setMeasuredTime(measuredTime);
        setCount(count);
    }

    @Column(name = "COUNT", nullable = true)
    public Long getCount() {
        return count;
    }

    @Column(name = "MEASURED_TIME", nullable = true)
    public Long getMeasuredTime() {
        return measuredTime;
    }

    public final void setCount(final Long count) {
        this.count = count;
    }

    public final void setMeasuredTime(final Long measuredTime) {
        this.measuredTime = measuredTime;
    }

}
