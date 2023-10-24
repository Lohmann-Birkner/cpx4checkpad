/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.dto;

import de.lb.cpx.model.enums.CaseStatusEn;
import de.lb.cpx.shared.lang.AbstractLang;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author niemeier
 */
public class DocumentSearchCaseItemDto extends DocumentSearchItemDto {

    private static final long serialVersionUID = 1L;

    protected LinkedHashSet<DocumentSearchProcessItemDto> processes = new LinkedHashSet<>();

    public DocumentSearchCaseItemDto() {

    }

    public DocumentSearchCaseItemDto(final Long pCaseId,
            final CaseStatusEn pCaseStatus, final String pHospitalIdent,
            final String pCaseNumber, final Date pAdmissionDate,
            final Date pDischargeDate, final boolean pIsMainFl) {
        setCaseId(pCaseId);
        setCaseStatusEn(pCaseStatus);
        setHospitalIdent(pHospitalIdent);
        setCaseNumber(pCaseNumber);
        setAdmissionDate(pAdmissionDate);
        setDischargeDate(pDischargeDate);
        setMainFl(pIsMainFl);

    }

    public Set<DocumentSearchProcessItemDto> getProcesses() {
        return new LinkedHashSet<>(processes);
    }

    public DocumentSearchProcessItemDto getProcessById(final Long pProcessId) {
        for (DocumentSearchProcessItemDto process : getProcesses()) {
            if (process.getProcessId().equals(pProcessId)) {
                return process;
            }
        }
        return null;
    }

    public void addProcess(final Long pProcessId,
            final Long pWorkflowNumber,
            final boolean pIsMainFl) {
        addProcess(
                new DocumentSearchProcessItemDto(pProcessId, pWorkflowNumber, pIsMainFl)
        );
    }

    public void addProcess(final DocumentSearchProcessItemDto pProcess) {
        if (pProcess != null) {
            processes.add(pProcess);
        }
    }

    public Set<DocumentSearchProcessItemDto> getMainProcesses() {
        LinkedHashSet<DocumentSearchProcessItemDto> result = new LinkedHashSet<>();
        for (DocumentSearchProcessItemDto process : getProcesses()) {
            if (process.isMainFl()) {
                result.add(process);
            }
        }
        return result;
    }

    public DocumentSearchProcessItemDto getFirstMainProcess() {
        Set<DocumentSearchProcessItemDto> result = getMainProcesses();
        if (result.isEmpty()) {
            return null;
        }
        return result.iterator().next();
    }

    public DocumentSearchProcessItemDto getFirstProcess() {
        DocumentSearchProcessItemDto process = getFirstMainProcess();
        if (process != null) {
            return process;
        }
        return getFirstSecondaryProcess();
    }

    public DocumentSearchProcessItemDto getFirstSecondaryProcess() {
        Set<DocumentSearchProcessItemDto> result = getSecondaryProcesses();
        if (result.isEmpty()) {
            return null;
        }
        return result.iterator().next();
    }

    public Set<DocumentSearchProcessItemDto> getSecondaryProcesses() {
        LinkedHashSet<DocumentSearchProcessItemDto> result = new LinkedHashSet<>();
        for (DocumentSearchProcessItemDto process : getProcesses()) {
            if (!process.isMainFl()) {
                result.add(process);
            }
        }
        return result;
    }

    @Override
    public String[] toArray() {
        return new String[]{
            //caseId == null ? "" : String.valueOf(caseId),
            getCaseNumber() == null ? "" : getCaseNumber().trim(),
            getCaseStatusEn() == null ? "" : getCaseStatusEn().getTranslation().getValue(),
            getPatientNumber() == null ? "" : getPatientNumber().trim(),
            getPatientSecName() == null ? "" : getPatientSecName().trim(),
            getPatientFirstName() == null ? "" : getPatientFirstName().trim(),
            AbstractLang.toDate(getPatientDateOfBirth()),
            AbstractLang.toDate(getAdmissionDate()),
            AbstractLang.toDate(getDischargeDate()),
            processes.isEmpty() ? "" : getFirstProcess().getWorkflowNumber() + (processes.size() <= 1 ? "" : "... (" + processes.size() + ")")
        //processId == null ? "" : String.valueOf(processId),
        //workflowNumber == null ? "" : String.valueOf(workflowNumber)
        };
    }

    public String getCaseKey() {
        return getHospitalIdent() + "/" + getCaseNumber();
    }

}
