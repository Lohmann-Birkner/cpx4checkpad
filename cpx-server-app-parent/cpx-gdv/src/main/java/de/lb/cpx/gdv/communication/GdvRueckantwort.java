/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.gdv.communication;


import de.lb.cpx.gdv.messages.BeschreibungLogischeEinheit;
import de.lb.cpx.gdv.messages.GDV;
import de.lb.cpx.gdv.messages.Partnerdaten;
import de.lb.cpx.gdv.messages.Rueckfrage;
import de.lb.cpx.gdv.messages.Vorsatz;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gerschmann
 */
public class GdvRueckantwort extends ItemsCreater{

    
    public GdvRueckantwort(){
        this("", new ArrayList<>());
    }
    
    public GdvRueckantwort(String pSchadenNummer){
        this(pSchadenNummer, new ArrayList<>());
    }
   
    public GdvRueckantwort(String pSchadenNummer, List<String> pAttachments){
        super(pSchadenNummer, pAttachments);

    }

    @Override
    public void fillGdvList(GDV gdv) {
        List<Object> list = gdv.getAuftragWerkstattvermittlungOrBehebungsbeauftragungOrFahrzeugbewertung();
        list.add(createRueckfrage());
    }
    
    private Rueckfrage createRueckfrage(){
        Rueckfrage rueckfrage = GdvGenerator.getInstance().createRueckfrage();
        //TODO fill 
        rueckfrage.setHeader(createHeader());
        rueckfrage.setBeschreibungLogischeEinheit(createBeschreibungLogischeEinheit());
        rueckfrage.getPartnerdatenBlock().add(createPartnerdatenBlock());
        createAnhaenge(rueckfrage.getAnhang(), "07");
        rueckfrage.setNachrichtentyp("013");
        return rueckfrage;
    }
    
    @Override
    protected BeschreibungLogischeEinheit createBeschreibungLogischeEinheit() {
        BeschreibungLogischeEinheit einheit = super.createBeschreibungLogischeEinheit();
        einheit.getNrLogischeEinheit().setValue("019");
        return einheit;
    }
    
    private Rueckfrage.PartnerdatenBlock createPartnerdatenBlock() {
        Rueckfrage.PartnerdatenBlock partnerdatenBlock = GdvGenerator.getInstance().createRueckfragePartnerdatenBlock();
        // TODO: fill
        Partnerdaten pDaten = createPartnerdaten();
        setLBPartnerdaten(pDaten);
        partnerdatenBlock.setPartnerdaten(pDaten);
        partnerdatenBlock.getKommentar().add(createKommentar());
        return partnerdatenBlock;
    }
    
    
}
