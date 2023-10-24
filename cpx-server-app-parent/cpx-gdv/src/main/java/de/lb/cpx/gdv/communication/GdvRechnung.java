/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.gdv.communication;

import de.lb.cpx.gdv.messages.BeschreibungLogischeEinheit;
import de.lb.cpx.gdv.messages.GDV;
import de.lb.cpx.gdv.messages.Partnerdaten;
import de.lb.cpx.gdv.messages.Rechnung;
import de.lb.cpx.gdv.messages.RechnungSV;
import de.lb.cpx.gdv.messages.Sachverstaendigenrechnung;
import java.util.List;

/**
 *
 * @author gerschmann
 */
public class GdvRechnung extends ItemsCreater{
    
    public GdvRechnung(String pSchadenNummer, List<String> pAttachments){
        super(pSchadenNummer, pAttachments);

    }
    
   @Override
    public void fillGdvList(GDV gdv) {
        List<Object> list = gdv.getAuftragWerkstattvermittlungOrBehebungsbeauftragungOrFahrzeugbewertung();
        list.add(createSachverstaendigenrechnung());
    }
    
    private Sachverstaendigenrechnung createSachverstaendigenrechnung(){
        Sachverstaendigenrechnung rechnung = GdvGenerator.getInstance().createSachverstaendigenrechnung();
        rechnung.setHeader(createHeader());
        rechnung.setBeschreibungLogischeEinheit(createBeschreibungLogischeEinheit());
        rechnung.getPartnerdatenBlock().add(createPartnerdatenBlock());
        rechnung.setRechnungSV(createRechnungSV());
        createAnhaenge(rechnung.getAnhang(), "03");
        rechnung.setNachrichtentyp("017"); 
        return rechnung;
    }
    
    private Sachverstaendigenrechnung.PartnerdatenBlock createPartnerdatenBlock(){
        Sachverstaendigenrechnung.PartnerdatenBlock partnerblock = GdvGenerator.getInstance().createSachverstaendigenrechnungPartnerdatenBlock();
        Partnerdaten pDaten = createPartnerdaten();
        setLBPartnerdaten(pDaten);
        partnerblock.setPartnerdaten(pDaten);
        partnerblock.getKommentar().add(createKommentar());
        return partnerblock;
    }
    
   
    @Override
    protected BeschreibungLogischeEinheit createBeschreibungLogischeEinheit() {
        BeschreibungLogischeEinheit einheit = super.createBeschreibungLogischeEinheit();
        einheit.getNrLogischeEinheit().setValue("017");
        return einheit;
    }

}
