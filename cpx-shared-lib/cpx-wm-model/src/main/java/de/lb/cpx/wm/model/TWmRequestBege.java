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
package de.lb.cpx.wm.model;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author nandola
 */
@Entity
@DiscriminatorValue(value = "1") //enum value in WmRequestType
@Table(name = "T_WM_REQUEST_BEGE")
public class TWmRequestBege extends TWmRequest {

    private static final long serialVersionUID = 1L;

//    private Long begeInternalId;
    private String begeComment;
    private String begeEditor;
    private String directPhone;
    private String directFax;
    private String insuranceIdentifier;

    /**
     * @return the begeId
     */
//    @Column(name = "BEGE_INTERNAL_ID", nullable = false)
//    public Long getBegeInternalId() {
//        return begeInternalId;
//    }
//    /**
//     * @param begeInternalId: the begeId to set
//     */
//    public void setBegeInternalId(Long begeInternalId) {
//        this.begeInternalId = begeInternalId;
//    }
    /**
     * @return the begeComment
     */
    @Column(name = "BEGE_COMMENT", length = 255)
//    @Override
    public String getBegeComment() {
        return begeComment;
    }

    /**
     * @param begeComment: the Comment to set
     */
    public void setBegeComment(String begeComment) {
        this.begeComment = begeComment;
    }

    /**
     * @return the begeEditor
     */
    @Column(name = "BEGE_EDITOR", length = 50)
    public String getBegeEditor() {
        return begeEditor;
    }

    /**
     * @param begeEditor: the Editor to set
     */
    public void setBegeEditor(String begeEditor) {
        this.begeEditor = begeEditor;
    }

    /**
     * @return the directPhone
     */
    @Column(name = "DIRECT_PHONE", length = 20)
    public String getDirectPhone() {
        return directPhone;
    }

    /**
     * @param directPhone: the directPhone to set
     */
    public void setDirectPhone(String directPhone) {
        this.directPhone = directPhone;
    }

    /**
     * @return the directFax
     */
    @Column(name = "DIRECT_FAX", length = 20)
    public String getDirectFax() {
        return directFax;
    }

    /**
     * @param directFax: the directFax to set
     */
    public void setDirectFax(String directFax) {
        this.directFax = directFax;
    }

    /**
     * @return the insuranceIdentifier
     */
    @Column(name = "INSURANCE_IDENTIFIER", length = 20, nullable = false)
    public String getInsuranceIdentifier() {
        return insuranceIdentifier;
    }

    /**
     * @param insuranceIdentifier: IKZ der Berufsgenossenschaft von der die
     * Anfrage versendet wurde to set
     */
    public void setInsuranceIdentifier(String insuranceIdentifier) {
        this.insuranceIdentifier = insuranceIdentifier;
    }

    @Override
    public boolean versionEquals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (!(object instanceof TWmRequestBege)) {
            return false;
        }
        final TWmRequestBege other = (TWmRequestBege) object;
        if (!Objects.equals(this.insuranceIdentifier, other.insuranceIdentifier)) {
            return false;
        }
//        if (!Objects.equals(this.begeInternalId, other.begeInternalId)) {
//            return false;
//        }
        if (!Objects.equals(this.begeComment, other.begeComment)) {
            return false;
        }
        if (!Objects.equals(this.begeEditor, other.begeEditor)) {
            return false;
        }
        if (!Objects.equals(this.directPhone, other.directPhone)) {
            return false;
        }
        if (!Objects.equals(this.directFax, other.directFax)) {
            return false;
        }
        return true;
    }

}
