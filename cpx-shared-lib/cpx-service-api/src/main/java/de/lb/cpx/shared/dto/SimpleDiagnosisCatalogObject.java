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
public class SimpleDiagnosisCatalogObject implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long id;
    private String diagnosisDescription;
    private String diagnosisCode;
    private String diagnosisNote;
    private boolean isCompleted;
    private final long catalogId;

    public SimpleDiagnosisCatalogObject(Long id, long catalogId, String diagnosisCode, String diagnosisDescription, String note, boolean isCompleted) {
        this.id = id;
        this.diagnosisCode = diagnosisCode;
        this.diagnosisDescription = diagnosisDescription;
        this.diagnosisNote = note;
        this.isCompleted = isCompleted;
        this.catalogId = catalogId;
    }

    public Long getId() {
        return id;
    }

    public String getDiagnosisDescription() {
        return diagnosisDescription;
    }

    public String getDiagnosisCode() {
        return diagnosisCode;
    }

    public String getDiagnosisNote() {
        return diagnosisNote;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setDiagnosisDescription(String diagnosisDescription) {
        this.diagnosisDescription = diagnosisDescription;
    }

    public void setDiagnosisCode(String diagnosisCode) {
        this.diagnosisCode = diagnosisCode;
    }

    public void setDiagnosisNote(String diagnosisNote) {
        this.diagnosisNote = diagnosisNote;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public long getCatalogId() {
        return catalogId;
    }

}
