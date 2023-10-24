/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.process.listview.entries.subject;

import de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog;
import de.lb.cpx.client.core.model.catalog.CpxInsuranceCompanyCatalog;
import de.lb.cpx.client.core.model.catalog.CpxMdk;
import de.lb.cpx.client.core.model.catalog.CpxMdkCatalog;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmEvent;
import de.lb.cpx.wm.model.TWmRequest;
import de.lb.cpx.wm.model.TWmRequestAudit;
import de.lb.cpx.wm.model.TWmRequestBege;
import de.lb.cpx.wm.model.TWmRequestInsurance;
import de.lb.cpx.wm.model.TWmRequestMdk;
import de.lb.cpx.wm.model.TWmRequestOther;
import de.lb.cpx.wm.model.TWmRequestReview;
import de.lb.cpx.wm.model.enums.WmEventTypeEn;

/**
 * Simple Pojo to store values for ListView
 *
 * @author niemeier
 */
public class RequestEventSubject extends EventSubject<TWmRequest> {

    public RequestEventSubject(final WmEventTypeEn pType, final TWmRequest pItem) {
        super(pType, pItem);
    }

    public RequestEventSubject(TWmEvent pEvent) {
        super(pEvent);
    }

    @Override
    public Object[] getTextParameters() {
        return new Object[]{
            getRequestType(),
            getRequestRecipient(),
            getMdkRequestQuotaExceededValue()
        };
    }

    private String getRequestType() {
//        if (pEvent == null || pEvent.getRequest() == null) {
//            return null;
//        }
        if (item.getRequestTypeEnum() == null) {
            return PLACEHOLDER; //"----";
        }
        return item.getRequestTypeEnum().getTranslation().getValue();
    }

    private String getRequestRecipient() {
//        if (pEvent == null || pEvent.getRequest() == null) {
//            return null;
//        }
        if (item.getRequestTypeEnum() == null) {
            return PLACEHOLDER; //"----";
        }
        switch (item.getRequestTypeEnum()) {
            case bege:
                TWmRequestBege begeReq = (TWmRequestBege) item;
                return getInsuranceName(begeReq.getInsuranceIdentifier());
            case mdk:
                TWmRequestMdk mdkReq = (TWmRequestMdk) item;
                return getMdkName(mdkReq.getMdkInternalId());
            case audit:
                TWmRequestAudit auditReq = (TWmRequestAudit) item;
                return getInsuranceName(auditReq.getInsuranceIdentifier());
            case insurance:
                TWmRequestInsurance insReq = (TWmRequestInsurance) item;
                return getInsuranceName(insReq.getInsuranceIdentifier());
            case other:
                TWmRequestOther otherReq = (TWmRequestOther) item;
                return otherReq.getInstitutionName();
            case review:
                TWmRequestReview review = (TWmRequestReview)item;
                return getInsuranceName(review.getInsIdentifier());
            default:
                return PLACEHOLDER; //"----";
        }
    }

    private String getMdkName(Long pIdentifier) {
        return getMdkName(pIdentifier, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

    private String getMdkName(Long pIdentifier, String pCountry) {
        if (pIdentifier == null || pCountry == null) {
            return PLACEHOLDER; //"----";
        }
        CpxMdk mdk = CpxMdkCatalog.instance().getByInternalId(pIdentifier, pCountry);
        if (mdk == null) {
            return PLACEHOLDER; //"----";
        }
        String name = mdk.getMdkName();
        if (name == null || name.isEmpty()) {
            return PLACEHOLDER; //"----";
        }
        return name;
    }

    private String getInsuranceName(String pIdentifier) {
        return getInsuranceName(pIdentifier, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

    private String getInsuranceName(String pIdentifier, String pCountry) {
        if (pIdentifier == null || pCountry == null) {
            return PLACEHOLDER; //"----";
        }
        String name = CpxInsuranceCompanyCatalog.instance().findInsNameByInsuranceNumber(pIdentifier, pCountry);
        if (name == null || name.isEmpty()) {
            return PLACEHOLDER; //"----";
        }
        return name;
    }

    @Override
    public String getRemovedDescription() {
        return super.getRemovedDescription(Lang.getEventNameRequest()); //Anfrage wurde gelöscht
    }

    private String getMdkRequestQuotaExceededValue() {
        if (item.getRequestTypeEnum() == null) {
            return ""; //"----";
        }
        switch (item.getRequestTypeEnum()) {
            case mdk:
                TWmRequestMdk mdkReq = (TWmRequestMdk) item;
                return mdkReq.getMdkRequestQuotaExceededFl() == TWmRequestMdk.QUOTA_EXCEEDED ? Lang.getAuditQuotaResultExceededExaminationQuota() : "";
            default:
                return "";
        }
    }

}
