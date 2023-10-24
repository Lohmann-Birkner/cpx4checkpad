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
package de.lb.cpx.shared.dto;

import java.io.Serializable;

/**
 *
 * @author wilde
 */
public class VersionDetailsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String admReason;
    private String drgPeppCode;
    private Number numberOfOps;
    private Number numberOfIcd;
    private String comment;
    private String principalDiagnosis;

    public VersionDetailsDTO() {

    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAdmReason() {
        return admReason;
    }

    public void setAdmReason(String admReason) {
        this.admReason = admReason;
    }

    public String getDrgPeppCode() {
        return drgPeppCode;
    }

    public void setDrgPeppCode(String drgPeppCode) {
        this.drgPeppCode = drgPeppCode;
    }

    public Number getNumberOfOps() {
        return numberOfOps;
    }

    public void setNumberOfOps(Number numberOfOps) {
        this.numberOfOps = numberOfOps;
    }

    public Number getNumberOfIcd() {
        return numberOfIcd;
    }

    public void setNumberOfIcd(Number numberOfIcd) {
        this.numberOfIcd = numberOfIcd;
    }

    public String getPrincipalDiagnosis() {
        return principalDiagnosis;
    }

    public void setPrincipalDiagnosis(String principalDiagnosis) {
        this.principalDiagnosis = principalDiagnosis;
    }

    @Override
    public String toString() {
        return "admReason:" + admReason + "\n drgPeppCode=" + drgPeppCode + "\n numberOfOps=" + numberOfOps + "\n numberOfIcd=" + numberOfIcd + "\n comment=" + comment + "\n principalDiagnosis=" + principalDiagnosis;
    }

}
