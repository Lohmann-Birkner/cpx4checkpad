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
 *    2016  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.grouper.model.transfer;

import java.io.Serializable;

/**
 *
 * @author gerschmann
 */
public class TransferCode implements Serializable {

    private static final long serialVersionUID = 1L;

    private long m_id = -1;

    private String m_code;
    private int m_localisation;

    public TransferCode(long id, String code, int loc) {
        this(code, loc);
        m_id = id;

    }

    public TransferCode(String code, int loc) {

        m_code = code;
        m_localisation = loc;
    }

    public int getLocalisation() {
        return m_localisation;
    }

    public String getCode() {
        return m_code;
    }

    public long getId() {
        return m_id;
    }

}
