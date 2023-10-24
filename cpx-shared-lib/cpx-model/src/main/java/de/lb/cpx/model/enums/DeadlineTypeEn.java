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
package de.lb.cpx.model.enums;

import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dirk Niemeier
 */
public enum DeadlineTypeEn implements CpxEnumInterface<Integer> {
    /* mdkAuditCompletionDeadline */ DL1(1, Lang.WORKFLOW_DEADLINE_MDK_AUDIT_COMPLETION_DEADLINE, Lang.WORKFLOW_DEADLINE_MDK_AUDIT_COMPLETION_DEADLINE_COMMENT, ChronoUnit.MONTHS, 11, null, null),
    /* billCorrectionDeadline */ DL2(2, Lang.WORKFLOW_DEADLINE_BILL_CORRECTION_DEADLINE, Lang.WORKFLOW_DEADLINE_BILL_CORRECTION_DEADLINE_COMMENT, ChronoUnit.MONTHS, 5, null, null),
    /* mdkDocumentDeliverDeadline */ DL3(3, Lang.WORKFLOW_DEADLINE_MDK_DOCUMENT_DELIVER_DEADLINE, Lang.WORKFLOW_DEADLINE_MDK_DOCUMENT_DELIVER_DEADLINE_COMMENT, ChronoUnit.WEEKS, 8, null, null),
    /* preliminaryProceedingsClosedDeadline */ DL4(4, Lang.WORKFLOW_DEADLINE_PRELIMINARY_PROCEEDINGS_CLOSED_DEADLINE, Lang.WORKFLOW_DEADLINE_PRELIMINARY_PROCEEDINGS_CLOSED_DEADLINE_COMMENT, ChronoUnit.MONTHS, 3, null, null),
    /* continuationFeeDeadline */ DL5(5, Lang.WORKFLOW_DEADLINE_CONTINUATION_FEE_DEADLINE, Lang.WORKFLOW_DEADLINE_CONTINUATION_FEE_DEADLINE_COMMENT, ChronoUnit.WEEKS, 6, null, null),
    /* dataRecordCorrectionDeadline */ DL6(6, Lang.WORKFLOW_DEADLINE_DATA_RECORD_CORRECTION_DEADLINE, Lang.WORKFLOW_DEADLINE_DATA_RECORD_CORRECTION_DEADLINE_COMMENT, ChronoUnit.WEEKS, 6, null, null),
    /* preliminaryProceedingsAnswerDeadline */ DL7(7, Lang.WORKFLOW_DEADLINE_ANSWER_DEADLINE, Lang.WORKFLOW_DEADLINE_ANSWER_DEADLINE_COMMENT, ChronoUnit.WEEKS, 2, null, null),
    /* proposalSubsequentProceedingsDeadline */ DL8(8, Lang.WORKFLOW_DEADLINE_PROPOSAL_SUBSEQUENT_PROCEEDINGS_DEADLINE, Lang.WORKFLOW_DEADLINE_PROPOSAL_SUBSEQUENT_PROCEEDINGS_DEADLINE_COMMENT, ChronoUnit.WEEKS, 6, null, null),
    /* 6WeeksDeadline */ DL9(9, Lang.WORKFLOW_DEADLINE_6_WEEKS_DEADLINE, Lang.WORKFLOW_DEADLINE_6_WEEKS_DEADLINE_COMMENT, ChronoUnit.WEEKS, 6, null, null),
    /* CaseDialogBillCorrectionDeadline */ DL10(10, Lang.WORKFLOW_DEADLINE_CASE_DIALOG_BILL_CORRECTION_DEADLINE, Lang.WORKFLOW_DEADLINE_CASE_DIALOG_BILL_CORRECTION_DEADLINE_COMMENT, ChronoUnit.WEEKS, 4, null, null),
    /* ReviewDeadlineStart */ DL11(11, Lang.WORKFLOW_DEADLINE_REVIEW_START, Lang.WORKFLOW_DEADLINE_REVIEW_START_COMMENT, ChronoUnit.WEEKS, 6, null, null),
    /* ReviewDeadlineInsuranceAnswer */ DL12(12, Lang.WORKFLOW_DEADLINE_REVIEW_INSURANCE_ANSWER, Lang.WORKFLOW_DEADLINE_REVIEW_INSURANCE_ANSWER_COMMENT, ChronoUnit.WEEKS, 6, null, null),
    /* ReviewDeadlineDocumentsSendOn */ DL13(13, Lang.WORKFLOW_DEADLINE_REVIEW_DOCUMENTS_SEND_ON, Lang.WORKFLOW_DEADLINE_REVIEW_DOCUMENTS_SEND_ON_COMMENT, ChronoUnit.WEEKS, 4, null, null),
    /* ReviewDeadlineCompletion */ DL14(14, Lang.WORKFLOW_DEADLINE_REVIEW_COMPLETION, Lang.WORKFLOW_DEADLINE_REVIEW_COMPLETION_COMMENT, ChronoUnit.WEEKS, 12, null, null);

    private static final Logger LOG = Logger.getLogger(DeadlineTypeEn.class.getName());

    private final int id;
    private final String langKey;
    private final String comment;
    private final ChronoUnit timeUnit;
    private final int timeQuantity;
    private final ChronoUnit timeUnitIntern;
    private final Integer timeQuantityIntern;

    private DeadlineTypeEn(int id, String langKey, String comment, ChronoUnit timeUnit, int timeQuantity, ChronoUnit timeUnitIntern, Integer timeQuantityIntern) {
        this.id = id;
        this.langKey = langKey;
        this.comment = comment;
        this.timeUnit = timeUnit;
        this.timeQuantity = timeQuantity;
        this.timeUnitIntern = timeUnitIntern;
        this.timeQuantityIntern = timeQuantityIntern;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getIdStr() {
        return String.valueOf(id);
    }

    @Override
    public int getIdInt() {
        return id;
    }

    @Override
    public String getLangKey() {
        return langKey;
    }

    public String getCommentKey() {
        return comment;
    }

    public Translation getCommentTranslation() {
        return getCommentTranslation((Object[]) null);
    }

    public Translation getCommentTranslation(Object... pParams) {
        return Lang.get(getCommentKey(), pParams);
    }

    public String getComment() {
        return CpxEnumInterface.toStaticString(getViewId(), this.getCommentKey());
    }

    public ChronoUnit getTimeUnit() {
        return timeUnit;
    }

    public int getTimeQuantity() {
        return timeQuantity;
    }

    public ChronoUnit getTimeUnitIntern() {
        return timeUnitIntern;
    }

    public Integer getTimeQuantityIntern() {
        return timeQuantityIntern;
    }

    /**
     * AuditCompletionDeadline (DL1)
     *
     * @return AuditCompletionDeadline (DL1)
     */
    public boolean isAcd() {
        return this.equals(DL1);
    }

    /**
     * BillCorrectionDeadline (DL2)
     *
     * @return BillCorrectionDeadline (DL2)
     */
    public boolean isBcd() {
        return this.equals(DL2);
    }

    /**
     * DocumentDeliverDeadline (DL3)
     *
     * @return DocumentDeliverDeadline (DL3)
     */
    public boolean isDdd() {
        return this.equals(DL3);
    }

    /**
     * PreliminaryProceedingsClosedDeadline (DL4)
     *
     * @return PreliminaryProceedingsClosedDeadline (DL4)
     */
    public boolean isPpcd() {
        return this.equals(DL4);
    }

    /**
     * ContinuationFeeDeadline (DL5)
     *
     * @return ContinuationFeeDeadline (DL5)
     */
    public boolean isCfd() {
        return this.equals(DL5);
    }

    /**
     * RecordCorrectionDeadline (DL6)
     *
     * @return RecordCorrectionDeadline (DL6)
     */
    public boolean isDrcd() {
        return this.equals(DL6);
    }

    /**
     * PreliminaryProceedingsAnswerDeadline (DL7)
     *
     * @return PreliminaryProceedingsAnswerDeadline (DL7)
     */
    public boolean isPpad() {
        return this.equals(DL7);
    }

    /**
     * ProposalSubsequentProceedingsDeadline (DL8)
     *
     * @return ProposalSubsequentProceedingsDeadline (DL8)
     */
    public boolean isPspd() {
        return this.equals(DL8);
    }

    /**
     * 6WeeksDeadline (DL9)
     *
     * @return 6WeeksDeadline (DL9)
     */
    public boolean is6Wd() {
        return this.equals(DL9);
    }

    /**
     * CaseDialogBillCorrectionDeadline (DL10)
     *
     * @return CaseDialogBillCorrectionDeadline (DL10)
     */
    public boolean isCbcd() {
        return this.equals(DL10);
    }
    
    public  boolean isReviewDeadlineStart() {
        return this.equals(DL11);
    }

    public boolean ReviewDeadlineInsuranceAnswer() {
        return this.equals(DL12);
    }

    public boolean ReviewDeadlineDocumentsSendOn() {
        return this.equals(DL13);
    }

    public boolean ReviewDeadlineCompletion() {
        return this.equals(DL14);
    }


    /**
     * AuditCompletionDeadline (DL1)
     *
     * @return AuditCompletionDeadline (DL1)
     */
    public static DeadlineTypeEn getAcd() {
        return DeadlineTypeEn.DL1;
    }

    /**
     * BillCorrectionDeadline (DL2)
     *
     * @return BillCorrectionDeadline (DL2)
     */
    public static DeadlineTypeEn getBcd() {
        return DeadlineTypeEn.DL2;
    }

    /**
     * DocumentDeliverDeadline (DL3)
     *
     * @return DocumentDeliverDeadline (DL3)
     */
    public static DeadlineTypeEn getDdd() {
        return DeadlineTypeEn.DL3;
    }

    /**
     * PreliminaryProceedingsClosedDeadline (DL4)
     *
     * @return PreliminaryProceedingsClosedDeadline (DL4)
     */
    public static DeadlineTypeEn getPpcd() {
        return DeadlineTypeEn.DL4;
    }

    /**
     * ContinuationFeeDeadline (DL5)
     *
     * @return ContinuationFeeDeadline (DL5)
     */
    public static DeadlineTypeEn getCfd() {
        return DeadlineTypeEn.DL5;
    }

    /**
     * DataRecordCorrectionDeadline (DL6)
     *
     * @return DataRecordCorrectionDeadline (DL6)
     */
    public static DeadlineTypeEn getDrcd() {
        return DeadlineTypeEn.DL6;
    }

    /**
     * PreliminaryProceedingsAnswerDeadline (DL7)
     *
     * @return PreliminaryProceedingsAnswerDeadline (DL7)
     */
    public static DeadlineTypeEn getPpad() {
        return DeadlineTypeEn.DL7;
    }

    /**
     * ProposalSubsequentProceedingsDeadline (DL8)
     *
     * @return ProposalSubsequentProceedingsDeadline (DL8)
     */
    public static DeadlineTypeEn getPspd() {
        return DeadlineTypeEn.DL8;
    }

    /**
     * 6WeeksDeadline (DL9)
     *
     * @return 6WeeksDeadline (DL9)
     */
    public static DeadlineTypeEn get6Wd() {
        return DeadlineTypeEn.DL9;
    }

    /**
     * CaseDialogBillCorrectionDeadline (DL10)
     *
     * @return CaseDialogBillCorrectionDeadline (DL10)
     */
    public static DeadlineTypeEn getCbcd() {
        return DeadlineTypeEn.DL10;
    }

    public static DeadlineTypeEn getReviewDeadlineStart() {
        return DL11;
    }

    public static DeadlineTypeEn getReviewDeadlineInsuranceAnswer() {
        return DL12;
    }

    public static DeadlineTypeEn getReviewDeadlineDocumentsSendOn() {
        return DL13;
    }

    public static DeadlineTypeEn getReviewDeadlineCompletion() {
        return DL14;
    }

    @Override
    public String toString() {
        return CpxEnumInterface.toStaticString(getViewId(), getLangKey());
    }

    @Override
    public String getViewId() {
        return String.valueOf(id);
    }

    @Override
    public boolean isViewRelevant() {
        return true;
    }

//    @Override
//    public CpxEnumInterface getEnum(String value) {
//        try {
//            return CpxEnumInterface.findEnum(values(), value);
//        } catch (IllegalArgumentException ex) {
//            Logger.getLogger(DeadlineTypeEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
//        }
//        return null;
//    }
    @Override
    public Translation getTranslation() {
        return getTranslation((Object[]) null);
    }

    @Override
    public Translation getTranslation(Object... pParams) {
        return Lang.get(getLangKey(), pParams);
    }

    public static DeadlineTypeEn findById(final Integer pId) {
        return DeadlineTypeEnMap.getInstance().get(pId);
    }

    public static DeadlineTypeEn findById(final String pId) {
        final String id = pId == null ? "" : pId.trim();
        if (id.isEmpty()) {
            return null;
        }
        try {
            return findById(Integer.valueOf(id));
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, "Cannot find DeadlineTypeEn, because this is not a valid integer: " + id + " (maybe invalid enum value is stored in database?)", ex);
            return null;
        }
    }

}

final class DeadlineTypeEnMap extends AbstractCpxEnumMap<DeadlineTypeEn, Integer> {

    private static final DeadlineTypeEnMap INSTANCE;

    static {
        INSTANCE = new DeadlineTypeEnMap();
    }

    protected DeadlineTypeEnMap() {
        super(DeadlineTypeEn.class);
    }

    public static DeadlineTypeEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public DeadlineTypeEn[] getValues() {
        return DeadlineTypeEn.values();
    }

}
