/* 
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.model.enums;

import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;

/**
 * Enumeration für die Keime in Labordaten
 *
 * @author niemeier
 */
public enum AntibioticEn implements CpxEnumInterface<String> {

    PEN("Penicillin", Lang.ANTIBIOTIC_PENICILLIN),
    CLI("Clindamycin", Lang.ANTIBIOTIC_CLINDAMYCIN),
    OXA_FLU("Oxacillin/Flucloxacill.", Lang.ANTIBIOTIC_OXACILLIN_FLUCLOXACILL),
    VAN("Vancomycin", Lang.ANTIBIOTIC_VANCOMYCIN),
    AMP_AMX("Ampicillin/Amoxicillin", Lang.ANTIBIOTIC_AMPICILLIN_AMOXICILLIN),
    LZD("Linezolid", Lang.ANTIBIOTIC_LINEZOLID),
    SAM("Ampicillin+Sulbactam", Lang.ANTIBIOTIC_AMPICILLIN_SULBACTAM),
    FOF("Fosfomycin+", Lang.ANTIBIOTIC_FOSFOMYCIN),
    PIP("Piperacillin", Lang.ANTIBIOTIC_PIPERACILLIN),
    RIF("Rifampicin+", Lang.ANTIBIOTIC_RIFAMPICIN),
    TZP("Piperacillin+Tazo.", Lang.ANTIBIOTIC_PIPERACILLIN_TAZO),
    FUC("Fucidine+", Lang.ANTIBIOTIC_FUCIDINE),
    CFZ("Cefazolin", Lang.ANTIBIOTIC_CEFAZOLIN),
    CXM_CEF("Cefuroxim/Cefotiam", Lang.ANTIBIOTIC_CEFUROXIM_CEFOTIAM),
    CPD("Cefpodoxim", Lang.ANTIBIOTIC_CEFPODOXIM),
    CTX_CRO("Cefotaxim/Ceftriaxon", Lang.ANTIBIOTIC_CEFOTAXIM_CEFTRIAXON),
    CAZ("Ceftazidim", Lang.ANTIBIOTIC_CEFTAZIDIM),
    FEP("Cefepim", Lang.ANTIBIOTIC_CEFEPIM),
    IPM("Imipenem", Lang.ANTIBIOTIC_IMIPENEM),
    MEM("Meropenem", Lang.ANTIBIOTIC_MEROPENEM),
    ATM("Aztreonam", Lang.ANTIBIOTIC_AZTREONAM),
    CIP("Ciprofloxacin", Lang.ANTIBIOTIC_CIPROFLOXACIN),
    LVX("Levofloxacin", Lang.ANTIBIOTIC_LEVOFLOXACIN),
    MXF("Moxifloxacin", Lang.ANTIBIOTIC_MOXIFLOXACIN),
    GEN("Gentamicin", Lang.ANTIBIOTIC_GENTAMICIN),
    TOB("Tobramycin", Lang.ANTIBIOTIC_TOBRAMYCIN),
    AMK("Amikacin", Lang.ANTIBIOTIC_AMIKACIN),
    TET_DOX("Tetracyclin/Doxycyclin", Lang.ANTIBIOTIC_TETRACYCLIN_DOXYCYCLIN),
    TGC("Tigecyclin", Lang.ANTIBIOTIC_TIGECYCLIN),
    TMP_SUL("Trimethoprim+Sulfonamid", Lang.ANTIBIOTIC_TRIMETHOPRIM_SULFONAMID),
    ERY("Erythromycin", Lang.ANTIBIOTIC_ERYTHROMYCIN),
    DAP("Daptomycin", Lang.ANTIBIOTIC_DAPTOMYCIN),
    CEC("Cefaclor", Lang.ANTIBIOTIC_CEFACLOR),
    MTZ("Metronidazol", Lang.ANTIBIOTIC_METRONIDAZOL);

    private final String id;
    private final String langKey;

    private AntibioticEn(final String id, final String langKey) {
        this.id = id;
        this.langKey = langKey;
    }

    @Override
    public String getLangKey() {
        return langKey;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getIdStr() {
        return id;
    }

    @Override
    public int getIdInt() {
        throw new UnsupportedOperationException("id as integer is not supported for " + getClass().getSimpleName());
    }

    /*
  @Override
  public String toString(final CpxLanguageInterface cpxLanguage) {
    return this.getViewId() + " - " + cpxLanguage.get(langKey);
  }
     */
    @Override
    public String toString() {
        return CpxEnumInterface.toStaticString(getTranslation().getAbbreviation(), getLangKey());
    }

    @Override
    public String getViewId() {
        //return String.valueOf(id);
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
//            Logger.getLogger(WeekdayEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
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

    public static AntibioticEn findById(final String pId) {
        return AntibioticEnMap.getInstance().get(pId);
    }

    public static AntibioticEn findByName(final String pName) {
        if (pName == null || pName.trim().isEmpty()) {
            return null;
        }
        final String name = pName.trim();
        for (AntibioticEn val : values()) {
            if (val != null && val.name().equalsIgnoreCase(name)) {
                return val;
            }
        }
        return null;
    }

    public static AntibioticEn detectAntibiotic(final String pAntibiotikum) {
        final String val = pAntibiotikum == null ? "" : pAntibiotikum.trim();
        if (val.isEmpty()) {
            return null;
        }
        final AntibioticEn antibiotic = findById(val);
        return antibiotic;
//        final AntibioticEn antibiotic;
//        switch (val) {
//            case "Penicillin":
//                antibiotic = AntibioticEn.PEN;
//                break;
//            case "Daptomycin":
//                antibiotic = AntibioticEn.DAP;
//                break;
//            case "Cefaclor":
//                antibiotic = AntibioticEn.CEC;
//                break;
//            case "Metronidazol":
//                antibiotic = AntibioticEn.MTZ;
//                break;
//            case "Aztreonam":
//                antibiotic = AntibioticEn.ATM;
//                break;
//            case "Ciprofloxacin":
//                antibiotic = AntibioticEn.CIP;
//                break;
//            case "Amikacin":
//                antibiotic = AntibioticEn.AMK;
//                break;
//            case "Oxacillin/Flucloxacill.":
//                antibiotic = AntibioticEn.OXA_FLU;
//                break;
//            case "Ampicillin/Amoxicillin":
//                antibiotic = AntibioticEn.AMP_AMX;
//                break;
//            case "Ampicillin+Sulbactam":
//                antibiotic = AntibioticEn.SAM;
//                break;
//            case "Piperacillin":
//                antibiotic = AntibioticEn.PIP;
//                break;
//            case "Piperacillin+Tazo.":
//                antibiotic = AntibioticEn.TZP;
//                break;
//            case "Cefazolin":
//                antibiotic = AntibioticEn.CFZ;
//                break;
//            case "Ceftazidim":
//                antibiotic = AntibioticEn.CAZ;
//                break;
//            case "Cefepim":
//                antibiotic = AntibioticEn.FEP;
//                break;
//            case "Cefuroxim/Cefotiam":
//                antibiotic = AntibioticEn.CXM_CEF;
//                break;
//            case "Cefpodoxim":
//                antibiotic = AntibioticEn.CPD;
//                break;
//            case "Cefotaxim/Ceftriaxon":
//                antibiotic = AntibioticEn.CTX_CRO;
//                break;
//            case "Imipenem":
//                antibiotic = AntibioticEn.IPM;
//                break;
//            case "Meropenem":
//                antibiotic = AntibioticEn.MEM;
//                break;
//            case "Levofloxacin":
//                antibiotic = AntibioticEn.LVX;
//                break;
//            case "Moxifloxacin":
//                antibiotic = AntibioticEn.MXF;
//                break;
//            case "Gentamicin":
//                antibiotic = AntibioticEn.GEN;
//                break;
//            case "Tetracyclin/Doxycyclin":
//                antibiotic = AntibioticEn.TET_DOX;
//                break;
//            case "Tigecyclin":
//                antibiotic = AntibioticEn.TGC;
//                break;
//            case "Tobramycin":
//                antibiotic = AntibioticEn.TOB;
//                break;
//            case "Trimethoprim+Sulfonamid":
//                antibiotic = AntibioticEn.TMP_SUL;
//                break;
//            case "Erythromycin":
//                antibiotic = AntibioticEn.ERY;
//                break;
//            case "Clindamycin":
//                antibiotic = AntibioticEn.CLI;
//                break;
//            case "Vancomycin":
//                antibiotic = AntibioticEn.VAN;
//                break;
//            case "Linezolid":
//                antibiotic = AntibioticEn.LZD;
//                break;
//            case "Fosfomycin+":
//                antibiotic = AntibioticEn.FOF;
//                break;
//            case "Rifampicin+":
//                antibiotic = AntibioticEn.RIF;
//                break;
//            case "Fucidine+":
//                antibiotic = AntibioticEn.FUC;
//                break;
//            default:
//                LOG.log(Level.WARNING, "cannot detect antibiotic: {0}", val);
//                antibiotic = null;
//                break;
//        }
//        return antibiotic;
    }

}

final class AntibioticEnMap extends AbstractCpxEnumMap<AntibioticEn, String> {

    private static final AntibioticEnMap INSTANCE;

    static {
        INSTANCE = new AntibioticEnMap();
    }

    protected AntibioticEnMap() {
        super(AntibioticEn.class);
    }

    public static AntibioticEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public AntibioticEn[] getValues() {
        return AntibioticEn.values();
    }

}
