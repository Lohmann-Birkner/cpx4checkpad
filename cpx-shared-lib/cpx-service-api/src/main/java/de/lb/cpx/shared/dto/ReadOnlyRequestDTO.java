/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.dto;

import de.lb.cpx.wm.model.TWmRequest;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author wilde
 */
public class ReadOnlyRequestDTO implements Serializable{

    private final TWmRequest request;
    private final Date processDate;

    public ReadOnlyRequestDTO(TWmRequest pRequest, Date pProcessDate) {
        request = pRequest;
        processDate = pProcessDate;
    }

    public TWmRequest getRequest() {
        return request;
    }

    public Date getProcessDate() {
        return processDate;
    }
    
}
