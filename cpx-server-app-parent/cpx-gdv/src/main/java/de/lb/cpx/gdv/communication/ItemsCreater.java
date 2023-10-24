/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.gdv.communication;

import de.lb.cpx.gdv.communication.utils.GdvUtils;
import de.lb.cpx.gdv.messages.Anhang;
import de.lb.cpx.gdv.messages.Anhang.Inhalt;
import de.lb.cpx.gdv.messages.BeschreibungLogischeEinheit;
import de.lb.cpx.gdv.messages.GDV;
import de.lb.cpx.gdv.messages.Header;
import de.lb.cpx.gdv.messages.Kommentar;
import de.lb.cpx.gdv.messages.Partnerdaten;
import de.lb.cpx.gdv.messages.RechnungSV;
import de.lb.cpx.gdv.messages.Vorsatz;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author gerschmann
 */
public abstract class ItemsCreater {

    private static final Logger LOG = Logger.getLogger(ItemsCreater.class.getName());
    public static final String[] SUFFIX = {"pdf", "jpeg", "tif", "jpg" };
    
    private final ObjectProperty<String> schadenNr = new SimpleObjectProperty<>();
    private final ObjectProperty<String> versNr = new SimpleObjectProperty<>();
    
    private final ObjectProperty<List<String>> attachments = new SimpleObjectProperty<>(new ArrayList<>());
    protected GDV gdv;
    
    public ItemsCreater(String pSchadensNr, List<String> pAttachments){
        schadenNr.set(pSchadensNr);
        attachments.set(pAttachments);
        versNr.set(GdvUtils.createVersNrFromSchadensNr(pSchadensNr));
        createGdv();
        
    }
    private GDV createGdv(){
        gdv = GdvGenerator.getInstance().createGDV();
        
        gdv.setVorsatz(createVorsatz());
        fillGdvList(gdv);

        return gdv;
    }

    public GDV getGdv() {
        return gdv;
    }
    public abstract void fillGdvList( GDV gdv);
    
    // Vorsatz
    protected Vorsatz createVorsatz(){
        Vorsatz vorsatz = GdvGenerator.getInstance().createVorsatz();
        //TODO fill
        Vorsatz.VSNR vsnr = GdvGenerator.getInstance().createVorsatzVSNR();
        vsnr.setValue(versNr.get());
        vorsatz.setVSNR(vsnr);
        vorsatz.setSatzart("4001");
        vorsatz.setVersionsnummer("004");
        vorsatz.setReleaseNummer(BigInteger.valueOf(2013));
        vorsatz.setAbsender(createAbsender());
        vorsatz.setEmpfaenger(createEmpfaenger());
        vorsatz.setReferenzierungAbsender(createRefernzierungAbsender());
        String[] time = GdvUtils.getDocCreationDateWithTime();
        vorsatz.setErstellungsdatum(time[0]);
        vorsatz.setErstellungsuhrzeit(time[1]);
        return vorsatz;
    }
    private Vorsatz.Absender createAbsender() {
        Vorsatz.Absender absender = GdvGenerator.getInstance().createVorsatzAbsender();
        // fill
        return absender;
    }
 
    public void fillDataFromBeauftragung(GDV pGdv, String pPatientNumber) {
        // fill vorsatz
        Vorsatz ov = gdv.getVorsatz();
        Vorsatz iv = pGdv.getVorsatz();
        // Fill
        ov.setVSNR(iv.getVSNR());
        ov.setVUReferenz(iv.getVUReferenz());
        fillReferezAbsenderLB(ov.getReferenzierungAbsender());
        fillAbsenderLB(ov);
        fillEmpfengerHUK(ov, iv);

    }
    
    private void fillReferezAbsenderLB(Vorsatz.ReferenzierungAbsender pRef){
        pRef.setAdressKennzeichen("21");
    }
    private void fillEmpfengerHUK(Vorsatz pLb, Vorsatz pHuk){
        Vorsatz.Absender hukAbsender = pHuk.getAbsender();
        Vorsatz.Empfaenger lubEmpfaenger = pLb.getEmpfaenger();
        lubEmpfaenger.setEmpfDLNR(hukAbsender.getAbsDLNR() == null?"not set":hukAbsender.getAbsDLNR());
        lubEmpfaenger.setEmpfDLPNR(hukAbsender.getAbsDLPNR() == null?"not set":hukAbsender.getAbsDLPNR());
        lubEmpfaenger.setEmpfOrdNrDLP(hukAbsender.getAbsOrdNrDLP() == null?"not set":hukAbsender.getAbsOrdNrDLP());
    }


    private void fillAbsenderLB(Vorsatz ov) {
        Vorsatz.Absender absender = ov.getAbsender();
        absender.setAbsDLNR("0098");
        absender.setAbsDLPNR("L&B");//26 positions constant
        absender.setAbsOrdNrDLP("vorgang");//cpx-workflow number
    }

    private Vorsatz.Empfaenger createEmpfaenger() {
        Vorsatz.Empfaenger empfaenger = GdvGenerator.getInstance().createVorsatzEmpfaenger();
        // fill
        return empfaenger;
    }
    

    private Vorsatz.ReferenzierungAbsender createRefernzierungAbsender() {
        Vorsatz.ReferenzierungAbsender refAbs = GdvGenerator.getInstance().createVorsatzReferenzierungAbsender();
        // fill
        return refAbs;
    }

// end Vorsatz    
    
// BeschreibungLogischeEinheit
    protected BeschreibungLogischeEinheit createBeschreibungLogischeEinheit() {
       BeschreibungLogischeEinheit beschreibungLogischeEinheit = GdvGenerator.getInstance().createBeschreibungLogischeEinheit();
       beschreibungLogischeEinheit.setSatzart("4052");
       beschreibungLogischeEinheit.setVersionsnummer("001");
       //TODO: fill
       beschreibungLogischeEinheit.setNrLogischeEinheit(createNrLogischeEinheit());
       return beschreibungLogischeEinheit;
    }

    private BeschreibungLogischeEinheit.NrLogischeEinheit createNrLogischeEinheit() {
        BeschreibungLogischeEinheit.NrLogischeEinheit nr = GdvGenerator.getInstance().createBeschreibungLogischeEinheitNrLogischeEinheit();
        // fill 
        return nr;
    }

// end BeschreibungLogischeEinheit
    
    protected Header createHeader() {
       Header header = GdvGenerator.getInstance().createHeader();
       //TODO: fill
       header.setSchadenNr(schadenNr.get());
       header.setVersicherungsscheinNr(versNr.get());
       return header;
    }
//Partnerdaten start
    protected Partnerdaten createPartnerdaten() {
        Partnerdaten partnerdaten = GdvGenerator.getInstance().createPartnerdaten();
        partnerdaten.setSatzart("4100");
        partnerdaten.setVersionsnummer("004");
        // TODO: fill
        partnerdaten.setAdresse(createAdresse());
        partnerdaten.setIdentifikationPartner(createIdentifikationPartner());
        partnerdaten.setAbteilung(createAbteilung());
        partnerdaten.setBeruf(createBeruf());
        partnerdaten.setKommunikation(createKommunikation());
        partnerdaten.setHausnummer(createHausnummer());
        partnerdaten.setPersonendaten(createPersonendaten());
        partnerdaten.setZahlungsdaten(createZahlungsdaten());
        return partnerdaten;
    }
    
    protected void setLBPartnerdaten(Partnerdaten pDaten){
        pDaten.getAdresse().setAnredeschluessel("3");
        pDaten.getAdresse().setName1("Lohmann & Birkner");
        pDaten.getAdresse().setPLZ("13407");
        pDaten.getAdresse().setOrt("Berlin");
        pDaten.getAdresse().setStrasse("Alt-Reinickendorf 25");
        pDaten.getIdentifikationPartner().setAdresskennzeichen("AG");
        pDaten.getKommunikation().setTyp("10");
        pDaten.getKommunikation().setNummer("+49(30)409985100");
        pDaten.getKommunikation().setKOMMTYP2("50");
        pDaten.getKommunikation().setKOMMNR2("+49(30)409985109");

    }
    
    protected Kommentar createKommentar(){
        Kommentar komment = GdvGenerator.getInstance().createKommentar();
        // fill
        return komment;
        
    }

    
    protected Partnerdaten.Adresse createAdresse(){
        Partnerdaten.Adresse addr = GdvGenerator.getInstance().createPartnerdatenAdresse();
        // fill
        return addr;
    }
    
    protected Partnerdaten.IdentifikationPartner createIdentifikationPartner(){
        Partnerdaten.IdentifikationPartner ident = GdvGenerator.getInstance().createPartnerdatenIdentifikationPartner();
        // fill
        return ident;
    }
    
    protected Partnerdaten.Abteilung createAbteilung(){
        Partnerdaten.Abteilung abt = GdvGenerator.getInstance().createPartnerdatenAbteilung();
        //fill
        return abt;
    }
    
    protected Partnerdaten.Beruf createBeruf(){
        Partnerdaten.Beruf beruf = GdvGenerator.getInstance().createPartnerdatenBeruf();
        // fill
        return beruf;
    }
    
    protected Partnerdaten.Kommunikation createKommunikation(){
        Partnerdaten.Kommunikation komm = GdvGenerator.getInstance().createPartnerdatenKommunikation();
        // fill
        return komm;
    }
    
    protected Partnerdaten.Hausnummer createHausnummer(){
        Partnerdaten.Hausnummer nr = GdvGenerator.getInstance().createPartnerdatenHausnummer();
        // fill
        return nr;
    }
    
    protected Partnerdaten.Personendaten createPersonendaten(){
        Partnerdaten.Personendaten pers = GdvGenerator.getInstance().createPartnerdatenPersonendaten();
        // fill
        return pers;
    }
    
    protected Partnerdaten.Zahlungsdaten createZahlungsdaten(){
        Partnerdaten.Zahlungsdaten zd = GdvGenerator.getInstance().createPartnerdatenZahlungsdaten();
        // fill
        return zd;
    }
// end Partnerdaten      
//rechnungSV    
    protected RechnungSV createRechnungSV() {
        RechnungSV rech =  GdvGenerator.getInstance().createRechnungSV();
        // fill
        rech.setZahlungsempfaenger(createZahlungsempfaenger());
        rech.setAuftraggeber(createAuftraggeber());
        rech.setGutachterkostenBrutto(createGutachtenkostenBrutto());
        rech.setProzentsatzMWSt(createProzentsatzMWSt());
        rech.setGutachterkostenNetto(createGutachterkostenNetto());
        rech.setBearbeitungsanteilNetto(createBearbeitungsanteilNetto());
        rech.setFahrtkostenanteilNetto(createFahrtkostenanteilNetto());
        rech.setAnzahlGefahreneKm(createAnzahlGefahreneKm());
        rech.setKostenProKm(createKostenProKm());
        rech.setBuerokostenanteilNetto(createBuerokostenanteilNetto());
        rech.setFotokostenanteilNetto(createFotokostenanteilNetto());
        rech.setAnzahlBilder(createAnzahlBilder());
        rech.setSonstigeKostenNetto(createSonstigeKostenNetto());
        rech.setEDVKostenNetto(createEDVKostenNetto());
        
        rech.setLeistungsart("04");//Rechnungsprüfung
        rech.setWaehrungsschluessel("EUR");
        rech.setSatzart("4310");
        rech.setVersionsnummer("003");
        return rech;
    }


    private RechnungSV.Zahlungsempfaenger createZahlungsempfaenger() {
        RechnungSV.Zahlungsempfaenger empf = GdvGenerator.getInstance().createRechnungSVZahlungsempfaenger();
        empf.setAdressKennzeichen("21"); // Sachverständiger
        return empf;
    }

    private RechnungSV.Auftraggeber createAuftraggeber() {
         RechnungSV.Auftraggeber auftr = GdvGenerator.getInstance().createRechnungSVAuftraggeber();
         // fill
         return auftr;
    }
    
    private RechnungSV.GutachterkostenBrutto createGutachtenkostenBrutto() {
         RechnungSV.GutachterkostenBrutto gBr = GdvGenerator.getInstance().createRechnungSVGutachterkostenBrutto();
         // fill
         gBr.setIndikator("0");
         return gBr;
    }

    private RechnungSV.ProzentsatzMWSt createProzentsatzMWSt() {
         RechnungSV.ProzentsatzMWSt prS = GdvGenerator.getInstance().createRechnungSVProzentsatzMWSt();
         // fill
         prS.setIndikator("0");
         return prS;
    }

    private RechnungSV.GutachterkostenNetto createGutachterkostenNetto() {
        RechnungSV.GutachterkostenNetto gN = GdvGenerator.getInstance().createRechnungSVGutachterkostenNetto();
        // fill
        gN.setIndikator("0");
        return gN;
    }

    private RechnungSV.BearbeitungsanteilNetto createBearbeitungsanteilNetto() {
        RechnungSV.BearbeitungsanteilNetto bN = GdvGenerator.getInstance().createRechnungSVBearbeitungsanteilNetto();
        // fill
        bN.setIndikator("0");
        return bN;
    }

    private RechnungSV.FahrtkostenanteilNetto createFahrtkostenanteilNetto() {
        RechnungSV.FahrtkostenanteilNetto fN = GdvGenerator.getInstance().createRechnungSVFahrtkostenanteilNetto();
        // fill;
        fN.setIndikator("0");
        return fN;
    }

    private RechnungSV.AnzahlGefahreneKm createAnzahlGefahreneKm() {
        RechnungSV.AnzahlGefahreneKm aKm = GdvGenerator.getInstance().createRechnungSVAnzahlGefahreneKm();
        // fill
        aKm.setIndikator("0");
        return aKm;
    }

    private RechnungSV.KostenProKm createKostenProKm() {
        RechnungSV.KostenProKm kKm = GdvGenerator.getInstance().createRechnungSVKostenProKm();
        // fill
        kKm.setIndikator("0");
        return kKm;
    }

    private RechnungSV.BuerokostenanteilNetto createBuerokostenanteilNetto() {
        RechnungSV.BuerokostenanteilNetto bN = GdvGenerator.getInstance().createRechnungSVBuerokostenanteilNetto();
        // fill
        bN.setIndikator("0");
        return bN;
    }

    private RechnungSV.FotokostenanteilNetto createFotokostenanteilNetto() {
        RechnungSV.FotokostenanteilNetto fN = GdvGenerator.getInstance().createRechnungSVFotokostenanteilNetto();
        // fill
        fN.setIndikator("0");
        return fN;
    }

    private RechnungSV.AnzahlBilder createAnzahlBilder() {
        RechnungSV.AnzahlBilder aB = GdvGenerator.getInstance().createRechnungSVAnzahlBilder();
        // fill
        aB.setIndikator("0");
        return aB;
    }

    private RechnungSV.SonstigeKostenNetto createSonstigeKostenNetto() {
        RechnungSV.SonstigeKostenNetto sN = GdvGenerator.getInstance().createRechnungSVSonstigeKostenNetto();
        // fill
        sN.setIndikator("0");
        return sN;
    }

    private RechnungSV.EDVKostenNetto createEDVKostenNetto() {
        RechnungSV.EDVKostenNetto edvN = GdvGenerator.getInstance().createRechnungSVEDVKostenNetto();
        // fill
        edvN.setIndikator(createIndikator());
        return edvN;
    }
    private RechnungSV.EDVKostenNetto.Indikator createIndikator() {
        RechnungSV.EDVKostenNetto.Indikator indikator = GdvGenerator.getInstance().createRechnungSVEDVKostenNettoIndikator();
        // fill
        indikator.setValue("0");
        return indikator;
    }
    
//end RechnungSV
    
//Anhaenge    
    protected void createAnhaenge(List<Anhang> pAnhaenge, String pType){
        List<String> paths = attachments.get();
        if(paths == null || paths.isEmpty()){
            return;
        }
        int count = 0;
        for(String path: paths){
            Anhang anh = createAnhang(count);
            fillAnhang(anh, path, pType);
            pAnhaenge.add(anh);
        }

    }

    protected Anhang createAnhang(int pSatzNr) {
        //d:\temp\report_123456789.pdf
        Anhang anhang = GdvGenerator.getInstance().createAnhang();
        anhang.setSatzart("4900");
        anhang.setVersionsnummer("004");
        anhang.setSatznummer(BigInteger.valueOf(pSatzNr));
//        fillAnhang(anhang);
        //TODO fill
        return anhang;
    }
    
    private void fillAnhang(Anhang pAnhang, String pPath, String pType){
       if(pPath == null || pPath.isEmpty()){
           return;
        }
        File file = new File(pPath);
        if(!file.exists() || !file.canRead()){
            LOG.log(Level.INFO, " cannot attach file: {0}", pPath);
            return;
        }
        String suffix = FilenameUtils.getExtension(pPath);
        List<String> lst = Arrays.asList(SUFFIX);
        if(!lst.contains(suffix.toLowerCase())){
            LOG.log(Level.INFO, " file type is not allowed {0}", suffix);
            return;
        }
        
        pAnhang.setAnhangsart(suffix);
        pAnhang.setAnhangstyp(pType);
        String name = file.getName();
        name = name.substring(0, name.length() - suffix.length() - 1);
        
        pAnhang.setDateiname(name);
        byte[] content = new byte[(int) file.length()];
        try{
           try (FileInputStream fin = new FileInputStream(file.getAbsolutePath())) {
               int numberOfBytes = fin.read(content);
               Anhang.Laenge len = GdvGenerator.getInstance().createAnhangLaenge();
               len.setWert(BigInteger.valueOf(numberOfBytes));
               pAnhang.setLaenge(len);
               Inhalt inhalt = GdvGenerator.getInstance().createAnhangInhalt();
               inhalt.setValue(content);
               pAnhang.setInhalt(inhalt);
           }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ItemsCreater.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ItemsCreater.class.getName()).log(Level.SEVERE, null, ex);
        }
            

        
    }
    
    public void addAttachment(String pAbsPath){
        attachments.get().add(pAbsPath);
    }
    
    protected void setAttachments(List<String> pPaths){
        attachments.get().addAll(pPaths);
    }

    protected void setSchadenNr(String pSchadenNr) {
        schadenNr.set(pSchadenNr);
    }



}
