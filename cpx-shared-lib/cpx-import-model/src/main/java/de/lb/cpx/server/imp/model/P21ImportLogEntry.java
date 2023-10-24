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
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 *
 * @author Wilde
 */
@Entity
@DiscriminatorValue(value = "LOG")
@SuppressWarnings("serial")
public class P21ImportLogEntry extends P21ImportLogEntryBase implements Serializable {

    protected P21ImportLogEntry() {
    }

    public P21ImportLogEntry(final P21ImportLogLevel level, final String importId,
            final String text) {
        setLogLevel(level);
        setText(text);
        setImportId(importId);
    }

}
