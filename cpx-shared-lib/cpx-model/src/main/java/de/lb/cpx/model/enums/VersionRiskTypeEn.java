/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.model.enums;

import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;

/**
 *
 * @author gerschmann
 */
public enum VersionRiskTypeEn implements CpxEnumInterface<Integer>{

//1.	Abrechnung
//2.	Anfrage: Falldialog
//3.	Anfrage: MD
//4.	Vorgangsabschluss
//5.    Gutachten: MD
    NOT_SET(0, Lang.getVersionRiskTypeNotSet()),
    BEFORE_BILLING(1, "Abrechnung"),
    AUDIT_CASE_DIALOG(2, Lang.getVersionRiskTypeAuditCaseDialog()),
    AUDIT_MD(3, Lang.getVersionRiskTypeAuditMD()),
    CASE_FINALISATION(4, "Ergebnis"/*Lang.getVersionRiskTypeCaseFinalisation()*/);
    
    private final int id;
    private final String langKey;
    
    private VersionRiskTypeEn(int id, String langKey){
        this.id = id;
        this.langKey = langKey;
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

    /*
  @Override
  public String toString(final CpxLanguageInterface cpxLanguage) {
    return this.getViewId() + " - " + cpxLanguage.get(String.valueOf(this.id));
  }
     */
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
//            Logger.getLogger(AdmissionByLawEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
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

    @Override
    public String getLangKey() {
       return langKey;
    }


    
}
