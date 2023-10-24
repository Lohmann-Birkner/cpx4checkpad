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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.wm.model.enums;

import de.lb.cpx.model.enums.AbstractCpxEnumMap;
import de.lb.cpx.model.enums.CpxEnumInterface;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author niemeier
 */
public enum Tp301Key30En implements CpxEnumInterface<String> {

    //Einleitung des Prüfverfahrens (§4 PrüfvV) – nur KAIN
    pf000("PF000", Lang.TP_301_INFORMATION_KEY_30_PF000, Values.KAIN), //primäre Fehlbelegung (nur KAIN)
    sf000("SF000", Lang.TP_301_INFORMATION_KEY_30_SF000, Values.KAIN), //sekundäre Fehlbelegung (nur KAIN)
    kp000("KP000", Lang.TP_301_INFORMATION_KEY_30_KP000, Values.KAIN), //Kodierprüfung (nur KAIN)
    fv000("FV000", Lang.TP_301_INFORMATION_KEY_30_FV000, Values.KAIN), //Fragen zur Voraussetzung bestimmter Maßnahmen (nur KAIN)
    kl000("KL000", Lang.TP_301_INFORMATION_KEY_30_KL000, Values.KAIN), //Freitext bei Nennung anderer/weiterer Prüfgegenstände (nur KAIN)

    //Vorverfahren (§5 PrüfvV)– nur KAIN
    fdk01("FDK01", Lang.TP_301_INFORMATION_KEY_30_FDK01, Values.KAIN), //Aufforderung zum Falldialog (nur KAIN)
    fdk02("FDK02", Lang.TP_301_INFORMATION_KEY_30_FDK02, Values.KAIN), //Annahme der Aufforderung zum Falldialog (nur KAIN)
    fdk03("FDK03", Lang.TP_301_INFORMATION_KEY_30_FDK03, Values.KAIN), //Ablehnung eines Falldialoges (nur KAIN)
    fdk11("FDK11", Lang.TP_301_INFORMATION_KEY_30_FDK11, Values.KAIN), //Aufforderung zur Verlängerung des Falldialogs (nur KAIN)
    fdk12("FDK12", Lang.TP_301_INFORMATION_KEY_30_FDK12, Values.KAIN), //Annahme der Aufforderung zur Verlängerung des Falldialogs (nur KAIN)
    fdk13("FDK13", Lang.TP_301_INFORMATION_KEY_30_FDK13, Values.KAIN), //Ablehnung der Verlängerung des Falldialoges (nur KAIN)
    bek00("BEK00", Lang.TP_301_INFORMATION_KEY_30_BEK00, Values.KAIN, 0, 2021), //Beendigung des Prüfverfahrens aufgrund Datenkorrektur (nur bei vorheriger Einleitung eines Falldialoges FK01 zu verwenden)– MD nicht notwendig (nur KAIN)
    bek10("BEK10", Lang.TP_301_INFORMATION_KEY_30_BEK10, Values.KAIN, 0, 2021), //Im Vorverfahren erfolgte keine Datenkorrektur/Ergänzung und infolge der maximal zulässigen Prüfquote, erfolgt keine Prüfanzeige durch den MD (nur KAIN)
    bek11("BEK11", Lang.TP_301_INFORMATION_KEY_30_BEK11, Values.KAIN, 2022), // Vorverfahren wurde kein Falldialog durchgeführt und infolge der maximal zulässigen 

    bek20("BEK20", Lang.TP_301_INFORMATION_KEY_30_BEK20, Values.KAIN), //Im Vorverfahren wurde ein Falldialog durchgeführt, jedoch keine Einigung erzielt und infolge der maximal zulässigen Prüfquote, erfolgt keine Prüfanzeige durch den MD (nur KAIN)
    bef00("BEF00", Lang.TP_301_INFORMATION_KEY_30_BEF00, Values.KAIN), //Beendigung des Prüfverfahrens aufgrund Einigung im Falldialog – ohne Datenkorrektur (nur KAIN)

    //Vorverfahren (§5 PrüfvV)– nur INKA
    fdi01("FDI01", Lang.TP_301_INFORMATION_KEY_30_FDI01, Values.INKA), //Aufforderung zum Falldialog (nur INKA)
    fdi02("FDI02", Lang.TP_301_INFORMATION_KEY_30_FDI02, Values.INKA), //Annahme der Aufforderung zum Falldialog (nur INKA)
    fdi03("FDI03", Lang.TP_301_INFORMATION_KEY_30_FDI03, Values.INKA), //Ablehnung eines Falldialoges (nur INKA)
    fdi11("FDI11", Lang.TP_301_INFORMATION_KEY_30_FDI11, Values.INKA), //Aufforderung zur Verlängerung des Falldialogs (nur INKA)
    fdi12("FDI12", Lang.TP_301_INFORMATION_KEY_30_FDI12, Values.INKA), //Annahme der Aufforderung zur Verlängerung des Falldialogs (nur INKA)
    fdi13("FDI13", Lang.TP_301_INFORMATION_KEY_30_FDI13, Values.INKA), //Ablehnung der Verlängerung des Falldialoges (nur INKA)

    //Vorverfahren (§5 PrüfvV)– nur KAIN und INKA
    fdk20("FDK20", Lang.TP_301_INFORMATION_KEY_30_FDK20, Values.KAIN), //Erklärung über die Beendigung des Falldialogs (nur KAIN)
    fdi20("FDI20", Lang.TP_301_INFORMATION_KEY_30_FDI20, Values.INKA), //Erklärung über die Beendigung des Falldialogs (nur INKA)

    //Durchführung MDK-Prüfung (§7 Abs. 2 PrüfvV)– nur KAIN
    mdk10("MDK10", Lang.TP_301_INFORMATION_KEY_30_MDK10, Values.KAIN, 0, 2021), //Keine oder nicht vollständige Unterlagen an MDK mit Folge der Aufrechnung (KAIN)
    mdk30("MDK30", Lang.TP_301_INFORMATION_KEY_30_MDK30, Values.KAIN, 2022), //Keine fristgemäße Übermittlung der Unterlagen. Die Krankenhausabrechnung gilt als erörtert gemäß § 7 Abs. 2 Satz 10 PrüfvV (KAIN) 
    //Entscheidung der Krankenkasse nach MD-Gutachten (§8 PrüfvV) – nur KAIN
    mdk01("MDK01", Lang.TP_301_INFORMATION_KEY_30_MDK01, Values.KAIN), //Leistungsrechtliche Entscheidung hat keine Beanstandung der Abrechnung (ohne Minderung des Abrechnungsbetrages) als Ergebnis (nur KAIN)
    mdk02("MDK02", Lang.TP_301_INFORMATION_KEY_30_MDK02, Values.KAIN), //Leistungsrechtliche Entscheidung hat Beanstandung der Abrechnung (mit Minderung des Abrechnungsbetrages) als Ergebnis (nur KAIN)
    mdk03("MDK03", Lang.TP_301_INFORMATION_KEY_30_MDK03, Values.KAIN, 0, 2021), //Leistungsrechtliche Entscheidung hat aufgrund Datensatzkorrektur (§7 Abs. 5 PrüfvV) im MD-Verfahren, ohne MD Abbruch,keine Beanstandung (ohne Minderung des Abrechnungsbetrages) der Abrechnung als Ergebnis (nur KAIN)
    mdk04("MDK04", Lang.TP_301_INFORMATION_KEY_30_MDK04, Values.KAIN), //Festlegung des Aufschlages gemäß § 275c Abs. 3 SGB V durch die Krankenkasse (nur KAIN)
    mdk06("MDK06", Lang.TP_301_INFORMATION_KEY_30_MDK06, Values.KAIN, 2023), //Reaktion zu evtl. Einwänden des Krankenhauses (KAIN) 
    mdk07("MDK07",  Lang.TP_301_INFORMATION_KEY_30_MDK07, Values.KAIN, 2023), // Mitteilung über einen unveränderten Aufschlagsbetrag (KAIN) 
    mdk08("MDK08",  Lang.TP_301_INFORMATION_KEY_30_MDK08, Values.KAIN, 2023), // Mitteilung über einen veränderten Aufschlagsbetrag (KAIN) 
    mdk09("MDK09",  Lang.TP_301_INFORMATION_KEY_30_MDK09, Values.KAIN, 2023), // Mitteilung des Aufschlages mit Grundlage der Berechnung (KAIN) 

    mdk20("MDK20", Lang.TP_301_INFORMATION_KEY_30_MDK20, Values.KAIN, 2023), //Datum der leistungsrechtlichen Entscheidung 
    mdk11("MDK11",  Lang.TP_301_INFORMATION_KEY_30_MDK11, Values.KAIN, 2023), // Rücknahme des Aufschlages, keine weitere Geltendmachung (KAIN) 
    mdk14("MDK14",  Lang.TP_301_INFORMATION_KEY_30_MDK14, Values.KAIN, 2023), // Endgültige Anpassung eines bereits geltend gemachten Aufschlages (KAIN    
    mdk12("MDK12", Lang.TP_301_INFORMATION_KEY_30_MDK12, Values.KAIN, 0, 2021), //Information durch die Krankenkasse über MD Abbruchwegen Datenkorrektur –mit Minderung des Abrechnungsbetrages, Prüfverfahren beendet (nur KAIN)
    mdk13("MDK13", Lang.TP_301_INFORMATION_KEY_30_MDK13, Values.KAIN, 0, 2021), //Information durch die Krankenkasse überMD Abbruchwegen Datenkorrekturohne Minderung des Abrechnungsbetrages, Prüfverfahren beendet (nur KAIN)
    mdk31("MDK31", Lang.TP_301_INFORMATION_KEY_30_MDK31, Values.KAIN, 2022), //Diese vom MD konkret benannten angeforderten Unterlagen wurden nicht fristgemäß vorgelegt und können nicht im Erörterungsverfahren vorgelegt werden gemäß § 7 Abs. 2 Satz 11 PrüfvV (KAIN)

    //Entscheidung der Krankenkasse nach MD-Gutachten (§ 8 Sätze 4 und 5 PrüfvV) – nur INKA
    mdi04("MDI04",  Lang.TP_301_INFORMATION_KEY_30_MDI04, Values.INKA, 2023), // Einwände gegen Grund oder Höhe des Aufschlages durch das Krankenhaus (INKA) 
    mdi05("MDI05",  Lang.TP_301_INFORMATION_KEY_30_MDI05, Values.INKA, 2022),
    //Nachverfahren (§9 PrüfvV) – nur INKA
    nvi01("NVI01", Lang.TP_301_INFORMATION_KEY_30_NVI01, Values.INKA, 0, 2021), //Vorschlag eines Nachverfahrens, begündete Stellungnahme (INKA)//Vorschlag eines Nachverfahrens, begündete Stellungnahme (INKA)

    //Erörterungsverfahren nach MD-Prüfung (§ 9 PrüfvV) – nur INKA 
    ekh01("EKH01", Lang.TP_301_INFORMATION_KEY_30_EKH01, Values.INKA, 2022), // Inhaltliche begründetes Bestreiten der Entscheidung der Krankenkasse gemäß § 9 Abs. 1 Satz 1 und 2 PrüfvV ohne Einleitung des EV (INKA) 
    ekh02("EKH02", Lang.TP_301_INFORMATION_KEY_30_EKH02, Values.INKA, 2022), // Inhaltliche begründetes Bestreiten der Entscheidung der Krankenkasse gemäß § 9 Abs. 1 Satz 1 bis 3 PrüfvV mit gleichzeitiger Einleitung des EV (INKA) 
    ekh30("EKH30", Lang.TP_301_INFORMATION_KEY_30_EKH30, Values.INKA, 2022), // Aufforderung zur Verlängerung des Erörterungsverfahrens (INKA) 
    ekh31("EKH31", Lang.TP_301_INFORMATION_KEY_30_EKH31, Values.INKA, 2022), // Annahme der Aufforderung zur Verlängerung des Erörterungsverfahrens (INKA) 
    ekh32("EKH32", Lang.TP_301_INFORMATION_KEY_30_EKH32, Values.INKA, 2022), //  Ablehnung der Verlängerung des Erörterungsverfahrens (INKA) 
    ekh33("EKH33", Lang.TP_301_INFORMATION_KEY_30_EKH33, Values.INKA, 2022), // Aufforderung mündliches Erörterungsverfahren (INKA) 
    ekh34("EKH34", Lang.TP_301_INFORMATION_KEY_30_EKH34, Values.INKA, 2022), // Zustimmung mündliches Erörterungsverfahren (INKA) 
    ekh35("EKH35", Lang.TP_301_INFORMATION_KEY_30_EKH35 , Values.INKA, 2022), // Ablehnung mündliches Erörterungsverfahren (INKA) 
    ekh41("EKH41", Lang.TP_301_INFORMATION_KEY_30_EKH41, Values.INKA, 2022), // Anzeige für eine ausnahmsweise Zulassung nicht fristgerecht geltend gemachter Einwendungen oder Tatsachenvortrag unter Angabe der besonderen Gründe gemäß § 9 Abs. 7 PrüfvV (INKA) 
    ekh42("EKH42", Lang.TP_301_INFORMATION_KEY_30_EKH42 , Values.INKA, 2022), // Ablehnung nicht fristgerecht geltend gemachter Einwendungen oder Tatsachenvortrag unter Angabe der besonderen Gründe gemäß § 9 Abs. 7 PrüfvV (INKA) 
    ekh43("EKH43", Lang.TP_301_INFORMATION_KEY_30_EKH43 , Values.INKA, 2022), // Annahme nicht fristgerecht geltend gemachter Einwendungen oder Tatsachenvortrag gemäß § 9 Abs. 7 PrüfvV (INKA) 
    ekh50("EKH50", Lang.TP_301_INFORMATION_KEY_30_EKH50 , Values.INKA, 2022), // Einigung im Erörterungsverfahren und Beendigung gemäß § 9 Abs. 8 PrüfvV (INKA) 
    ekh51("EKH51", Lang.TP_301_INFORMATION_KEY_30_EKH51 , Values.INKA, 2022), // Einvernehmliche Beendigung des Erörterungsverfahren gemäß § 9 Abs. 10 PrüfvV (INKA) 
    ekh60("EKH60", Lang.TP_301_INFORMATION_KEY_30_EKH60 , Values.INKA, 2022), //  gilt infolge fehlender Erörterung oder fehlender Mitwirkung als erörtert gemäß § 9 Abs. 11 PrüfvV (INKA) 
    ekh61("EKH61", Lang.TP_301_INFORMATION_KEY_30_EKH61 , Values.INKA, 2022), //  wurde keine Einigung erzielt und das Erörterungsverfahren ist gemäß § 9 Abs. 12 PrüfvV beendet (INKA) 

//Erörterungsverfahren nach MD-Prüfung (§ 9 PrüfvV) – nur KAIN)  
    ekk01("EKK01", Lang.TP_301_INFORMATION_KEY_30_EKK01, Values.KAIN, 2022), //  Krankenkasse schließt sich gemäß §9 Abs. 3 Satz 1 PrüfvV der Begründung des Krankenhauses an. Es erfolgt kein Erörterungsverfahren. (KAIN)  
    ekk02("EKK02", Lang.TP_301_INFORMATION_KEY_30_EKK02, Values.KAIN, 2022), // Krankenkasse schließt sich der Begründung des Krankenhauses gemäß §9 Abs. 3 Satz 1 PrüfvV an. Das durch das Krankenhaus bereits eingeleitete EV ist beendet. (KAIN)  
    ekk03("EKK03", Lang.TP_301_INFORMATION_KEY_30_EKK03, Values.KAIN, 2022), // Krankenkasse schließt sich nicht der Begründung des Krankenhauses gemäß §9 Abs. 4 PrüfvV an, das KH hat das EV bereits eingeleitet. (KAIN) 
    ekk04("EKK04", Lang.TP_301_INFORMATION_KEY_30_EKK04, Values.KAIN, 2022), // Krankenkasse schließt sich nicht der Begründung des Krankenhauses gemäß §9 Abs. 4 Satz 1 PrüfvV an und leitet das EV ein. (KAIN) 
    ekk10("EKK10", Lang.TP_301_INFORMATION_KEY_30_EKK10, Values.KAIN, 2022), // Entscheidung der Krankenkasse wurde nicht fristgemäß bestritten und gilt als erörtert gemäß § 9 Abs. 2 PrüfvV mit Folge der Aufrechnung (KAIN)  
    ekk30("EKK30", Lang.TP_301_INFORMATION_KEY_30_EKK30, Values.KAIN, 2022), // Aufforderung zur Verlängerung des Erörterungsverfahrens (KAIN) 
    ekk31("EKK31", Lang.TP_301_INFORMATION_KEY_30_EKK31, Values.KAIN, 2022), // Annahme der Aufforderung zur Verlängerung des Erörterungsverfahrens (KAIN) 
    ekk32("EKK32", Lang.TP_301_INFORMATION_KEY_30_EKK32, Values.KAIN, 2022), // Ablehnung der Verlängerung des Erörterungsverfahrens (KAIN) 
    ekk33("EKK33", Lang.TP_301_INFORMATION_KEY_30_EKK33, Values.KAIN, 2022), // Aufforderung mündliches Erörterungsverfahren (KAIN) 
    ekk34("EKK34", Lang.TP_301_INFORMATION_KEY_30_EKK34, Values.KAIN, 2022), // Zustimmung mündliches Erörterungsverfahren (KAIN) 
    ekk35("EKK35", Lang.TP_301_INFORMATION_KEY_30_EKK35, Values.KAIN, 2022), // Ablehnung mündliches Erörterungsverfahren (KAIN) 
    ekk41("EKK41", Lang.TP_301_INFORMATION_KEY_30_EKK41, Values.KAIN, 2022), // Anzeige einer Ausnahmsweise Zulassung nicht fristgerecht geltend gemachter Einwende oder Tatsachenvorträge unter Angabe der besonderen Gründe gemäß § 9 Abs. 7 PrüfvV (KAIN) 
    ekk42("EKK42", Lang.TP_301_INFORMATION_KEY_30_EKK42, Values.KAIN, 2022), // Ablehnung Zulassung nicht fristgerecht geltend gemachter Einwende oder Tatsachenvorträge unter Angabe der besonderen Gründe gemäß § 9 Abs. 7 PrüfvV (KAIN) 
    ekk43("EKK43", Lang.TP_301_INFORMATION_KEY_30_EKK43, Values.KAIN, 2022), // Annahme Zulassung nicht fristgerecht geltend gemachter Einwende oder Tatsachenvorträge unter Angabe der besonderen Gründe gemäß § 9 Abs. 7 PrüfvV (KAIN) 
    ekk50("EKK50", Lang.TP_301_INFORMATION_KEY_30_EKK50, Values.KAIN, 2022), // Einigung im Erörterungsverfahren und Beendigung gemäß § 9 Abs. 8 PrüfvV (KAIN) Tp301.InformationKey30.EKK51 Einvernehmliche Beendigung des Erörterungsverfahren gemäß § 9 Abs. 10 PrüfvV (KAIN) 
    ekk51("EKK51", Lang.TP_301_INFORMATION_KEY_30_EKK51, Values.KAIN, 2022), // Einvernehmliche Beendigung des Erörterungsverfahren gemäß § 9 Abs. 10 PrüfvV (KAIN)
    ekk60("EKK60", Lang.TP_301_INFORMATION_KEY_30_EKK60, Values.KAIN, 2022), // Abrechnungsstreitigkeit gilt infolge fehlender Erörterung oder fehlender Mitwirkung als erörtert gemäß § 9 Abs. 11 PrüfvV (KAIN) 
    ekk61("EKK61", Lang.TP_301_INFORMATION_KEY_30_EKK61, Values.KAIN, 2022), // Es wurde keine Einigung erzielt und das Erörterungsverfahren ist gemäß § 9 Abs. 12 PrüfvV beendet (KAIN) 
    
;     
    private static final Logger LOG = Logger.getLogger(Tp301Key30En.class.getName());


 
    public static class Values {

        private Values() {
            //utility class needs no public constructor
        }

        public static final String KAIN = "KAIN";
        public static final String INKA = "INKA";
    }

    private final String id;
    private final String langKey;
    private final String typ;
    private final int firstYear; // first valid year. when admissionYear< firstYear, than ist the key not valid
    private final int lastYear; // last valid year. when admissionYear > lastYear, than ist the key not valid
    private static final List<Tp301Key30En>allKain = new ArrayList<>();
    private static final List<Tp301Key30En>allInka = new ArrayList<>();
    
    static {
        for(Tp301Key30En value : EnumSet.allOf(Tp301Key30En.class)){
            if(value.isInka()){
                allInka.add(value);
            }
            if(value.isKain()){
                allKain.add(value);
            }
        }
        allInka.sort(Comparator.comparing(Tp301Key30En::getId));
        allKain.sort(Comparator.comparing(Tp301Key30En::getId));
    }
    //used to show lists of kain or inka keys sorted on client in filters
    public static Tp301Key30En[] getValues(String pType){
        if(Values.INKA.equalsIgnoreCase(pType)){
            Tp301Key30En[] arr = new Tp301Key30En[allInka.size()];
            allInka.toArray(arr);
            return arr;
        }
        if(Values.KAIN.equalsIgnoreCase(pType)){
            Tp301Key30En[] arr = new Tp301Key30En[allKain.size()];
            allKain.toArray(arr);
            return arr;
        }
        return new Tp301Key30En[0];
    }

    private Tp301Key30En(String id, String langKey, String typ) {
        this(id, langKey, typ, 0);
    }
        
    private Tp301Key30En(String id, String langKey, String typ, int firstYear) {
        this(id, langKey, typ, firstYear, 2999);
    }
    
    private Tp301Key30En(String id, String langKey, String typ, int firstYear, int lastYear){
        this.id = id;
        this.langKey = langKey;
        this.typ = typ;
        this.firstYear = firstYear;
        this.lastYear = lastYear;
    }

    public String getTyp() {
        return typ;
    }
    
    public int getLastYear(){
        return lastYear;
    }
    
    public int getFirstYear(){
        return firstYear;
    }

    /**
     * KAIN = Krankenkasseninformation
     *
     * @return is KAIN?
     */
    public boolean isKain() {
        return Values.KAIN.equalsIgnoreCase(typ);
    }

    /**
     * INKA = Information der Krankenhäuser
     *
     * @return is INKA?
     */
    public boolean isInka() {
        return Values.INKA.equalsIgnoreCase(typ);
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

    @Override
    public String getLangKey() {
        return langKey;
    }

    /*
    @Override
    public String toString(final CpxLanguageInterface cpxLanguage) {
        return this.getViewId() + " - " + cpxLanguage.get(langKey);
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
//            LOG.log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
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

    public static Tp301Key30En findById(final String pId) {
        String id = (pId == null) ? null : pId.toUpperCase().trim();
        if (id == null) {
            return null;
        }
        return Tp301Key30EnMap.getInstance().get(id);
    }

}

final class Tp301Key30EnMap extends AbstractCpxEnumMap<Tp301Key30En, String> {

    private static final Tp301Key30EnMap INSTANCE;

    static {
        INSTANCE = new Tp301Key30EnMap();
    }

    protected Tp301Key30EnMap() {
        super(Tp301Key30En.class);
    }

    public static Tp301Key30EnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public Tp301Key30En[] getValues() {
        return Tp301Key30En.values();
    }

}
