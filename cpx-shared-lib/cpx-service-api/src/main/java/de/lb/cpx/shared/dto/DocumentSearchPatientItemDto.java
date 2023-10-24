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
public class DocumentSearchPatientItemDto extends DocumentSearchItemDto {

    private static final long serialVersionUID = 1L;

    protected LinkedHashSet<DocumentSearchCaseItemDto> cases = new LinkedHashSet<>();

//    public Long processId;
//    public Long processNumber;
    protected LinkedHashSet<DocumentSearchProcessItemDto> processes = new LinkedHashSet<>();

    public DocumentSearchPatientItemDto() {

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

//    public void addProcess(final Long pProcessId,
//            final Long pWorkflowNumber,
//            final boolean pIsMainFl) {
//        addProcess(
//                new DocumentSearchProcess(pProcessId, pWorkflowNumber, pIsMainFl)
//        );
//    }
    public void addProcess(final DocumentSearchProcessItemDto pProcess) {
        if (pProcess != null) {
            processes.add(pProcess);
        }
    }

//    public Set<DocumentSearchProcess> getMainProcesses() {
//        LinkedHashSet<DocumentSearchProcess> result = new LinkedHashSet<>();
//        for (DocumentSearchProcessItemDto process : getProcesses()) {
//            if (process .isMainFl) {
//                result.add(process);
//            }
//        }
//        return result;
//    }
//
//    public DocumentSearchProcess getFirstMainProcess() {
//        Set<DocumentSearchProcess> result = getMainProcesses();
//        if (result.isEmpty()) {
//            return null;
//        }
//        return result.iterator().next();
//    }
//
//    public DocumentSearchProcess getFirstProcess() {
//        DocumentSearchProcess process = getFirstMainProcess();
//        if (process != null) {
//            return process;
//        }
//        return getFirstSecondaryProcess();
//    }
//
//    public DocumentSearchProcess getFirstSecondaryProcess() {
//        Set<DocumentSearchProcess> result = getSecondaryProcesses();
//        if (result.isEmpty()) {
//            return null;
//        }
//        return result.iterator().next();
//    }
//
//    public Set<DocumentSearchProcess> getSecondaryProcesses() {
//        LinkedHashSet<DocumentSearchProcess> result = new LinkedHashSet<>();
//        for (DocumentSearchProcess process : getProcesses()) {
//            if (!process.isMainFl) {
//                result.add(process);
//            }
//        }
//        return result;
//    }
    public Set<DocumentSearchCaseItemDto> getCases() {
        return new LinkedHashSet<>(cases);
    }

    public void addCase(final Long pCaseId,
            final CaseStatusEn pCaseStatus,
            final String pHospitalIdent,
            final String pCaseNumber,
            final Date pAdmissionDate,
            final Date pDischargeDate,
            final boolean pIsMainFl) {
        addCase(
                new DocumentSearchCaseItemDto(pCaseId, pCaseStatus, pHospitalIdent, pCaseNumber, pAdmissionDate, pDischargeDate, pIsMainFl)
        );
    }

    public void addCase(final DocumentSearchCaseItemDto pCase) {
        if (pCase != null) {
            cases.add(pCase);
        }
    }

    public DocumentSearchCaseItemDto getCaseById(final Long pCaseId) {
        for (DocumentSearchCaseItemDto cs : getCases()) {
            if (cs.getCaseId().equals(pCaseId)) {
                return cs;
            }
        }
        return null;
    }

//    public DocumentSearchCase getMainCase() {
//        for (DocumentSearchCase cs : getCases()) {
//            if (cs.isMainFl) {
//                return cs;
//            }
//        }
//        return null;
//    }
    @Override
    public String[] toArray() {
        //final DocumentSearchCase mainCase = getFirstMainCase();
        return new String[]{
            //caseId == null ? "" : String.valueOf(caseId),
            //caseStatusEn == null ? "" : caseStatusEn.getTranslation().getValue(),
            getPatientNumber() == null ? "" : getPatientNumber().trim(),
            getPatientSecName() == null ? "" : getPatientSecName().trim(),
            getPatientFirstName() == null ? "" : getPatientFirstName().trim(),
            AbstractLang.toDate(getPatientDateOfBirth()),
            getCaseNumber() == null ? "" : getCaseNumber().trim() + (cases.size() > 1 ? "... (" + cases.size() + ")" : ""),
            getWorkflowNumber() == null ? "" : String.valueOf(getWorkflowNumber()) + (processes.size() > 1 ? "... (" + processes.size() + ")" : ""), //AbstractLang.toDate(admissionDate),
        //AbstractLang.toDate(dischargeDate),
        //processes.isEmpty() ? "" : String.valueOf(processes.size()),
        //cases.isEmpty() ? "" : String.valueOf(cases.size())
        //processId == null ? "" : String.valueOf(processId),
        //workflowNumber == null ? "" : String.valueOf(workflowNumber)
        };
    }

    public String getCaseKey() {
        return getHospitalIdent() + "/" + getCaseNumber();
    }

}
