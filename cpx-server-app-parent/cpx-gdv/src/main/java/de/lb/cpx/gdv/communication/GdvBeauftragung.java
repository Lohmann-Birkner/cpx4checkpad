/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.gdv.communication;

import de.lb.cpx.gdv.messages.AllgemeineSchadendaten;
import de.lb.cpx.gdv.messages.BeauftragungSV;
import de.lb.cpx.gdv.messages.GDV;
import de.lb.cpx.gdv.messages.Kalkulation;
import de.lb.cpx.gdv.messages.SachSVBeauftragung;
import de.lb.cpx.gdv.messages.Sachverstaendigenbeauftragung;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gerschmann
 */
public class GdvBeauftragung extends ItemsCreater{
    
    public GdvBeauftragung(String pSchadenNr, List<String> pPaths){
        super(pSchadenNr, pPaths);
    }
    public GdvBeauftragung(){
        this("", new ArrayList<>());

    }
    public GdvBeauftragung(String pSchadenNr){
        this(pSchadenNr, new ArrayList<>());
    }

    @Override
    public void fillGdvList(GDV gdv) {
        List<Object> list = gdv.getAuftragWerkstattvermittlungOrBehebungsbeauftragungOrFahrzeugbewertung();
        list.add(createSachverstaendigenBeauftragung());
    }

    private Sachverstaendigenbeauftragung createSachverstaendigenBeauftragung() {
        Sachverstaendigenbeauftragung svBeauftr = GdvGenerator.getInstance().createSachverstaendigenbeauftragung();
        // TODO: fill
        svBeauftr.setHeader(createHeader());
        svBeauftr.setBeschreibungLogischeEinheit(createBeschreibungLogischeEinheit());
        svBeauftr.setBeauftragungSV(createBeauftragungSv());
        svBeauftr.setKalkulation(createKalkulation());
        svBeauftr.setAllgemeineSchadendaten(createAllgemeineSchadendaten());
        svBeauftr.getPartnerdatenBlock().add(createPartnerdatenBlock());
        createAnhaenge(svBeauftr.getAnhang(), "07");
////        svBeauftr.getAnhang().add(createAnhang());
        svBeauftr.setNachrichtentyp("007");
        return svBeauftr;
    }

    private AllgemeineSchadendaten createAllgemeineSchadendaten() {
        AllgemeineSchadendaten asDaten = GdvGenerator.getInstance().createAllgemeineSchadendaten();
        //TODO: Fill
        asDaten.setSatzart("4200");
        asDaten.setVersionsnummer("004");
        return asDaten;
    }

    private Sachverstaendigenbeauftragung.PartnerdatenBlock createPartnerdatenBlock() {
        Sachverstaendigenbeauftragung.PartnerdatenBlock pBlock = GdvGenerator.getInstance().createSachverstaendigenbeauftragungPartnerdatenBlock();
        pBlock.setPartnerdaten(createPartnerdaten());
        //TODO: Fill
        return pBlock;
    }

    private BeauftragungSV createBeauftragungSv() {
        BeauftragungSV beauftr = GdvGenerator.getInstance().createBeauftragungSV();
        //TODO: fill
        beauftr.setSatzart("4810");
        beauftr.setVersionsnummer("003");
        return beauftr;
    }

    private Kalkulation createKalkulation() {
        Kalkulation calc = GdvGenerator.getInstance().createKalkulation();
        // TODO: fill
        calc.setSatzart("4300");
        calc.setVersionsnummer("004");
        return calc;
    }
    
}
