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
package de.lb.cpx.shared.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.lb.cpx.shared.json.enums.MessageReasonEn;
import java.io.Serializable;

/**
 * Basic Error Message on json format
 * @author wilde
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonErrorMessage implements Cloneable, Serializable{
    
    private String type;
    private String description;
    private String severity;
    private MessageReasonEn reason;
    private int srcYear;
    private int destYear;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public MessageReasonEn getReason() {
        return reason;
    }

    public void setReason(MessageReasonEn reason) {
        this.reason = reason;
    }

    public int getSrcYear() {
        return srcYear;
    }

    public void setSrcYear(int srcYear) {
        this.srcYear = srcYear;
    }

    public int getDestYear() {
        return destYear;
    }

    public void setDestYear(int destYear) {
        this.destYear = destYear;
    }
    
    
}
