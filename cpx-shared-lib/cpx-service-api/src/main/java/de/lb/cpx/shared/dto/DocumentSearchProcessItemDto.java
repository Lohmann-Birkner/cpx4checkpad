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
public class DocumentSearchProcessItemDto extends DocumentSearchItemDto {

    private static final long serialVersionUID = 1L;

    protected LinkedHashSet<DocumentSearchCaseItemDto> cases = new LinkedHashSet<>();

    public DocumentSearchProcessItemDto() {

    }

    public DocumentSearchProcessItemDto(final Long pProcessId, final Long pWorkflowNumber, final boolean pIsMainFl) {
        setProcessId(pProcessId);
        setWorkflowNumber(pWorkflowNumber);
        setMainFl(pIsMainFl);
    }

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

//    public Set<DocumentSearchCase> getMainCases() {
//        LinkedHashSet<DocumentSearchCase> result = new LinkedHashSet<>();
//        for (DocumentSearchCase cs : getCases()) {
//            if (cs.isMainFl) {
//                result.add(cs);
//            }
//        }
//        return result;
//    }
    public DocumentSearchCaseItemDto getMainCase() {
        for (DocumentSearchCaseItemDto cs : getCases()) {
            if (cs.isMainFl()) {
                return cs;
            }
        }
        return null;
    }

    public DocumentSearchCaseItemDto getCaseById(final Long pCaseId) {
        for (DocumentSearchCaseItemDto cs : getCases()) {
            if (cs.getCaseId().equals(pCaseId)) {
                return cs;
            }
        }
        return null;
    }

//    public DocumentSearchCase getFirstCase() {
//        DocumentSearchCase cs = getMainCase();
//        if (cs != null) {
//            return cs;
//        }
//        return getFirstSecondaryCase();
//    }
//
//    public DocumentSearchCase getFirstSecondaryCase() {
//        Set<DocumentSearchCase> result = getSecondaryCases();
//        if (result.isEmpty()) {
//            return null;
//        }
//        return result.iterator().next();
//    }
//
//    public Set<DocumentSearchCase> getSecondaryCases() {
//        LinkedHashSet<DocumentSearchCase> result = new LinkedHashSet<>();
//        for (DocumentSearchCase cs : getCases()) {
//            if (!cs.isMainFl) {
//                result.add(cs);
//            }
//        }
//        return result;
//    }
    @Override
    public String[] toArray() {
        //final DocumentSearchCase mainCase = getFirstMainCase();
        return new String[]{
            //caseId == null ? "" : String.valueOf(caseId),
            //caseStatusEn == null ? "" : caseStatusEn.getTranslation().getValue(),
            String.valueOf(getWorkflowNumber()),
            getPatientNumber() == null ? "" : getPatientNumber().trim(),
            getPatientSecName() == null ? "" : getPatientSecName().trim(),
            getPatientFirstName() == null ? "" : getPatientFirstName().trim(),
            AbstractLang.toDate(getPatientDateOfBirth()),
            getCaseNumber() == null ? "" : getCaseNumber().trim(),
            AbstractLang.toDate(getAdmissionDate()),
            AbstractLang.toDate(getDischargeDate()),
            cases.isEmpty() ? "" : String.valueOf(cases.size())
        //processId == null ? "" : String.valueOf(processId),
        //workflowNumber == null ? "" : String.valueOf(workflowNumber)
        };
    }

    public String getCaseKey() {
        return getHospitalIdent() + "/" + getCaseNumber();
    }

}
