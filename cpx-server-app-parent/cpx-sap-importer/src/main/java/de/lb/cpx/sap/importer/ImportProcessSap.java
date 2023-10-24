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
package de.lb.cpx.sap.importer;

import com.sap.conn.jco.JCoException;
import de.lb.cpx.sap.container.BewegungContainer;
import de.lb.cpx.sap.container.DiagnosenContainer;
import de.lb.cpx.sap.container.FallContainer;
import de.lb.cpx.sap.container.ProzedurenContainer;
import de.lb.cpx.sap.dto.CaseEntg;
import de.lb.cpx.sap.dto.RmlLabor;
import de.lb.cpx.sap.dto.RmlLaborDocument;
import de.lb.cpx.sap.dto.SapCase;
import de.lb.cpx.sap.dto.SapFiFactura;
import de.lb.cpx.sap.dto.SapFiOpenItem;
import de.lb.cpx.sap.dto.SapFiPosition;
import de.lb.cpx.sap.importer.utils.JsonDumpFileReader;
import static de.lb.cpx.sap.importer.utils.SapStrUtils.*;
import de.lb.cpx.sap.kain_inka.KainInkaMessage;
import de.lb.cpx.sap.kain_inka.PvtResultIf;
import de.lb.cpx.sap.results.SapKainDetailSearchResult;
import de.lb.cpx.sap.results.SapKainPvvSearchResult;
import de.lb.cpx.shared.dto.job.config.other.SapJob;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.str.utils.StrUtils;
import dto.impl.Case;
import dto.impl.Department;
import dto.impl.Diagnose;
import dto.impl.Fee;
import dto.impl.Hauptdiagnose;
import dto.impl.Kain;
import dto.impl.KainInkaPvt;
import dto.impl.KainInkaPvv;
import dto.impl.Lab;
import dto.impl.Nebendiagnose;
import dto.impl.Patient;
import dto.impl.Procedure;
import dto.impl.SapFiBill;
import dto.impl.SapFiBillposition;
import dto.impl.SapFiOpenItems;
import dto.impl.Ward;
import dto.types.Erbringungsart;
import dto.types.Geschlecht;
import dto.types.Lokalisation;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.constraints.NotNull;
import module.impl.ImportConfig;
import process.impl.ImportProcess;
import transformer.AbstractCpxTransformer;
import transformer.CpxTransformerI;
import transformer.impl.TransformResult;
import util.CpxWriter;

/**
 * Is responsible to get a list of hospital case data from SAP
 *
 * @author niemeier
 */
public class ImportProcessSap extends ImportProcess<Sap> {

    private static final Logger LOG = Logger.getLogger(ImportProcessSap.class.getName());
//    public static final boolean USE_JSON_DUMP = false;
//    public static final boolean WRITE_JSON_DUMP = false;
//    public static final boolean ANONYMOUSIZE_DATA = false;
    public static final int MESSAGE_STATE_WK_CASE = 9;
    public static final int MESSAGE_STATE_CASE_NOT_EXISIT = 7;
//    private final Set<String> mPatienten = new HashSet<>();
//    private SapProperties mProperties;
//    private License mLicense;

//    /**
//     * Start SAP Import with these connection parameters.
//     *
//     * @param pServer Server
//     * @param pSysNr System number
//     * @param pMandant Mandant
//     * @param pUser User
//     * @param pPassword Password
//     * @param pInstitution Institution (Einrichtung)
//     * @param pHospitalIdentifier Hospital IKZ
//     * @param pChangeDate Change date (cases must be more recent than this date)
//     * @param pUsePool Use pooling?
//     * @param pLicense CPX license
//     * @param pCaseNumbersToImport use this case numbers for import
//     * @return A set of SAP Cases filled with data
//     * @throws JCoException Exception
//     * @throws Throwable Throwable
//     */
//    public FallContainer doSapImport(final String pServer, final String pSysNr, final String pMandant, final String pUser, final String pPassword, final String pInstitution, final String pHospitalIdentifier, final Date pChangeDate, final boolean pUsePool, final License pLicense, final Collection<String> pCaseNumbersToImport) throws JCoException, Throwable {
//        LOG.log(Level.INFO, "Institution: " + pInstitution);
//        LOG.log(Level.INFO, "Change date: " + getDate(pChangeDate));
//        LOG.log(Level.INFO, "Hospital identifier: " + pHospitalIdentifier);
//
//        final FallContainer fallContainer;
//        if (USE_JSON_DUMP) {
//            fallContainer = loadCaseData("E:\\fallContainer.json.zip");
//        } else {
//            fallContainer = ImportProcessSap.this.loadCaseData(pServer, pSysNr, pMandant, pUser, pPassword, pInstitution, pHospitalIdentifier, pChangeDate, pUsePool, pLicense, pCaseNumbersToImport);
//        }
//
//        LOG.log(Level.INFO, "Amount of hospital cases loaded: " + (fallContainer == null ? "fallContainerSet is null!" : fallContainer.getSapCases().size()));
//
//        return fallContainer;
//    }
    /**
     * Start SAP Import with these connection parameters.
     *
     * @param pSapConfig SAP configuration
     * @param pChangeDate Change date (cases must be more recent than this date)
     * @param pUsePool Use pooling?
     * @return A set of SAP Cases filled with data
     * @throws JCoException Exception
     * @throws Throwable Throwable
     */
    public FallContainer doSapImport(final ImportConfig<Sap> pSapConfig, /* final String pHospitalIdentifier, */ final Date pChangeDate, final boolean pUsePool) throws JCoException, Throwable {
        SapJob sapConfig = pSapConfig.getModule().getInputConfig();
        LOG.log(Level.INFO, "Institution: {0}", sapConfig.getInstitution());
        LOG.log(Level.INFO, "Change date: {0}", getDate(pChangeDate));
        LOG.log(Level.INFO, "Hospital identifier: {0}", sapConfig.getDefaultHosIdent());
        LOG.log(Level.INFO, sapConfig.toString());
        final FallContainer fallContainer;
        if (sapConfig.isUseJsonDump()) {
            fallContainer = loadCaseData((sapConfig.getmJsonPath() == null?"":(sapConfig.getmJsonPath() + File.separator)) +"fallContainer.json");
        } else {
            fallContainer = ImportProcessSap.this.loadCaseData(pSapConfig /*, pHospitalIdentifier */, pChangeDate, pUsePool);
        }

        LOG.log(Level.INFO, "Amount of hospital cases loaded: {0}", fallContainer == null ? "fallContainerSet is null!" : fallContainer.getSapCases().size());

        return fallContainer;
    }

//    /**
//     *
//     * @param pPatKey patient key
//     * @return patient exists?
//     */
//    protected synchronized boolean patientExists(final String pPatKey) {
//        return !mPatienten.add(pPatKey);
//    }
    /**
     *
     * @param pImportConfig import configuration
     * @return transformer instance
     * @throws java.lang.reflect.InvocationTargetException error
     * @throws java.security.NoSuchAlgorithmException error
     * @throws java.lang.InterruptedException error
     * @throws java.lang.NoSuchFieldException error
     * @throws java.lang.NoSuchMethodException error
     * @throws java.lang.IllegalAccessException error
     * @throws java.sql.SQLException error
     * @throws java.io.IOException error
     * @throws java.lang.InstantiationException error
     */
    @Override
    public CpxTransformerI<Sap> getTransformer(final ImportConfig<Sap> pImportConfig) throws InvocationTargetException, NoSuchAlgorithmException, InterruptedException, NoSuchFieldException, IOException, IllegalAccessException, InstantiationException, SQLException, NoSuchMethodException {
        //Set<Exception> exceptions = new LinkedHashSet<>();
        return new AbstractCpxTransformer<>(pImportConfig) {
            @Override
            public TransformResult start() throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchMethodException, InvocationTargetException, SQLException {
                final Set<SapCase> sapCaseSet = pImportConfig.getModule().getContainer().getSapCases();
                final Map<String, List<SapKainDetailSearchResult>> kainMessagesSet = pImportConfig.getModule().getContainer().getKainMessages();
                final String outputDirectory = pImportConfig.getOutputDirectory();
                final boolean importWard = pImportConfig.getModule().getInputConfig().getImportWard();
                int patientCount = 0;
                int caseCount = 0;
                try (final CpxWriter cpxMgr = CpxWriter.getInstance(outputDirectory)) {
                    Iterator<SapCase> it = sapCaseSet.iterator();
                    while (it.hasNext()) {
                        SapCase sapCase = it.next();
                        final String caseKey = sapCase.getCaseKey();
                        LOG.log(Level.INFO, caseKey);
                        final String thirdPartySystem = "SAP EINRI " + sapCase.getInstitution();

                        final String versichertenNr = sapCase.getInsurance() == null ? "" : sapCase.getInsurance().getVernr();
                        final String kasse = sapCase.getKasse();
                        //final String patNr = sapCase.patientnr.trim().isEmpty()?versichertenNr:sapCase.patientnr;
                        final String patNr = sapCase.getPatientnr().trim();

                        if (patNr.isEmpty()) {
                            LOG.log(Level.WARNING, "This patient has no patient no, so I will ignore this case: {0}", caseKey);
                            continue; //I think this is not the adequate way to handle this problem! But, yeah... what else can I do?
                        }
                        if (sapCase.getAufnahmedatum() == null) {
                            LOG.log(Level.WARNING, "This case has no admission date, so I will ignore it: {0}", caseKey);
                            continue;
                        }

                        final List<String> warnings = new ArrayList<>();
                        for (Iterator<BewegungContainer> itDep = sapCase.getWards().iterator(); itDep.hasNext();) {
                            BewegungContainer b = itDep.next();
                            //2020-01-20 DNi - Ticket CPX-2140
                            if (b.getP301() == null || b.getP301().trim().isEmpty()) {
                                warnings.add("This case has at least one department without P301 key (department 301 code), so I will ignore it: " + caseKey);
                                break;
                            }
                        }

                        if (!warnings.isEmpty()) {
                            for (String warning : warnings) {
                                LOG.log(Level.WARNING, warning);
                            }
                            continue; //skip this case
                        }
//                        if (sapCase.getEntlassungdatum() == null) {
//                            LOG.log(Level.WARNING, "This case has no discharge date, so I will ignore it: " + caseKey);
//                            continue;
//                        }

                        final String noCaseType = "???";
                        final String caseType;
                        switch (sapCase.getFalltyp()) {
                            case 1:
                                caseType = "DRG";
                                break;
                            case 2:
                                caseType = "PEPP";
                                break;
                            default:
                                caseType = noCaseType;
                        }

                        if (noCaseType.equals(caseType)) {
                            LOG.log(Level.WARNING, "This case has no valid case type (''{0}''), so I will ignore it: {1}", new Object[]{sapCase.getFalltyp(), caseKey});
                            continue;
                        }

                        final String patKey = patNr;
                        if (patientKeyExists(patKey)) {
                            //DO NOTHING, PATIENT ALREADY EXISTS
                        } else {
                            Patient pat = new Patient();
                            pat.setGeburtsdatum(sapCase.getGeburtsdatum());
                            pat.setPatNr(sapCase.getPatientnr());
                            pat.setNachname(sapCase.getNachname());
                            pat.setVorname(sapCase.getVorname());
                            pat.setTitel(sapCase.getTitel());
                            pat.setGeschlecht(Geschlecht.findByValue(sapCase.getGeschlecht()));
                            pat.setMobil(sapCase.getMobiltelefon());
                            pat.setTelefon(sapCase.getTelefon());
                            pat.setKasse(kasse);
                            pat.setVersichertennr(versichertenNr);
                            pat.setLand(sapCase.getLand());
                            pat.setOrt(sapCase.getOrt());
                            pat.setAdresse(sapCase.getAdresse());
                            pat.setBundesland(sapCase.getBundesland());
                            pat.setPlz(sapCase.getPlz());
                            pat.setTpSource(thirdPartySystem);
                            patientCount++;

                            cpxMgr.write(pat);
                        }

                        Case cs = new Case(patKey);
                        cs.setAlterInJahren(sapCase.getAlterInJahren());
                        cs.setAlterInTagen(sapCase.getAlterInTagen());
                        cs.setAufnahmeanlass(sapCase.getAufnahmeanlass());
                        cs.setAufnahmedatum(sapCase.getAufnahmedatum());
                        cs.setGeschlecht(Geschlecht.findByValue(sapCase.getGeschlecht()));
                        int aufnahmegrund12 = Integer.parseInt(toStr(sapCase.getAufnahmegrund12()).isEmpty() ? "0" : sapCase.getAufnahmegrund12());
                        if (aufnahmegrund12 == 99) {
                            aufnahmegrund12 = 1; //What is admission reason 99?!
                        }
                        cs.setAufnahmegrund1(String.valueOf(aufnahmegrund12));
                        cs.setAufnahmegrund2(sapCase.getAufnahmegrund34());
                        cs.setBeatmungsstunden(Integer.valueOf(sapCase.getBeatmungsstunden()));
                        //cs.setEntgeltbereich(fallContainer.);
                        cs.setEntlassungsdatum(sapCase.getEntlassungdatum());
                        int entlassungsgrund12 = Integer.parseInt(toStr(sapCase.getEntlassungsgrund12()).isEmpty() ? "0" : sapCase.getEntlassungsgrund12());
                        cs.setEntlassungsgrund12(String.valueOf(entlassungsgrund12));
                        cs.setEntlassungsgrund3(sapCase.getEntlassungsgrund3());
                        cs.setFallNr(sapCase.getFallnr());
                        cs.setFallart(caseType);
                        //cs.setGesetzlPsychstatus(1);
                        cs.setGewicht(sapCase.getAufnahmegewicht());
                        cs.setIkz(sapCase.getIkz());
                        cs.setKasse(kasse);
//                        if (cs.getKasse().isEmpty()) {
//                            cs.setKasse("k.A.");
//                        }
                        cs.setStorniert(!sapCase.isValid());
                        cs.setUrlaubstage(sapCase.getTob());
                        cs.setVersichertennr(versichertenNr);
                        cs.setVwd(sapCase.getUrlaub());
                        cs.setVwdIntensiv(sapCase.getVwdIntensiv());
                        cs.setVwdSimuliert(0);
                        cs.setTpSource(thirdPartySystem);
                        cs.setMDTob(sapCase.getMDTob());
                        cs.setBillingDate(sapCase.getRechnungsdatum());
                        caseCount++;

                        final Map<Long, Long> diagMap = new HashMap<>();

                        Iterator<BewegungContainer> itWard = sapCase.getWards().iterator();
                        Department prevDep = null;
                        BewegungContainer prevBew = null;
                         Ward ward = null;
                        while (itWard.hasNext()) {
                            BewegungContainer b = itWard.next();
                            Department dep;
                           int gap = prevBew == null?0:Lang.toDaysBetween( Lang.setTimeTo0000(prevBew.getEnde()),Lang.setTimeTo0000(b.getStart()), 0);
                            if (prevBew == null || (!prevBew.getSpecKey().equals(b.getSpecKey()))
 //here TODO: check prevBew.getEnde() == b.getStart() - because of readmission in the same department we can calculate leave or los_alteration   
                                 || ( gap > 0)   
                                    ) {
                                if(prevDep != null){
                                    cpxMgr.write(prevDep);
                                    if(ward != null){
                                        cpxMgr.write(ward);

                                    }
                                }
                                dep = new Department(cs);
                                dep.setAufnehmendeIk("");
                                dep.setCode(b.getP301());
                                dep.setCodeIntern(b.getAbteilung());
                                dep.setVerlegungsdatum(b.getStart());
                                dep.setEntlassungsdatum(b.getEnde());
                                if (dep.getEntlassungsdatum() == null) {
                                    dep.setEntlassungsdatum(Department.getDummyEntlassungsdatum());
                                }
                                dep.setErbringungsart(Erbringungsart.findByNumber(b.getTypSchluessel()));
                                dep.setTpSource(thirdPartySystem);
                                if(gap > 0){
                                 // TOB   
                                 cs.addUrlaubstage(gap);
                                }    
                                if (importWard) {
                                    ward = new Ward(dep);
                                    ward.setCode(b.getStation());
                                    ward.setVerlegungsdatum(b.getStart());
                                    ward.setEntlassungsdatum(b.getEnde());
                                    ward.setTpSource(thirdPartySystem);
                                }
                            } else {

                                dep = prevDep;
                                dep.setEntlassungsdatum(b.getEnde());// move discharge date of the department to the end date of the next 
                                if(importWard){ 
                                    if(ward != null){
                                        if(ward.getCode().equals(b.getStation())){
                                            ward.setEntlassungsdatum(b.getEnde());
                                        }else{
                                            cpxMgr.write(ward);
                                            ward = new Ward(dep);
                                            ward.setCode(b.getStation());
                                            ward.setVerlegungsdatum(b.getStart());
                                            ward.setEntlassungsdatum(b.getEnde());
                                            ward.setTpSource(thirdPartySystem);
                                        }
                                    }
                                    
                                }
                            }

// LUr 07.01.2021:
// Import der Stationen wird über die Eigenschaft "import_ward" in der cpx_server_config.xml gesteuert.
// Ist der Wert 'true', werden die Stationen mit importiert, bei 'false' oder nicht vorhanden nicht.
//                            Ward ward = null;
//                            if (importWard) {
//                                ward = new Ward(dep);
//                                ward.setCode(b.getStation());
//                                ward.setVerlegungsdatum(b.getStart());
//                                ward.setEntlassungsdatum(b.getEnde());
//                                ward.setTpSource(thirdPartySystem);
//                            }

                            Map<Long, Diagnose<?>> diagnosen = new HashMap<>();
                            Iterator<DiagnosenContainer> itIcd = b.getDiagList().iterator();
                            while (itIcd.hasNext()) {
                                DiagnosenContainer d = itIcd.next();
                                if (d.getCode().isEmpty()) {
                                    //ICD without a code? Ignore it!
                                    continue;
                                }
                                Diagnose<?> diagnose;
                                if (d.getIsCasePdx() == 1) { //isCasePdx = Hauptdiagnose des Falles, isDeptPdx = Bewegungshauptdiagnose??
                                    diagnose = new Hauptdiagnose(dep, ward);
                                } else {
                                    diagnose = new Nebendiagnose(dep, ward);
                                }
                                diagnose.setCode(d.getCode());
                                diagnose.setLokalisation(Lokalisation.findByValue(d.getLok()));
                                //diagnose.setStation(b.spec_key); //oder b.station??
                                //diagnose.setToGroup(d.grouped == 1); //fast immer false??
                                //diagnose.setToGroup(true);
                                diagnose.setToGroup(d.getIsDrg() == 1);
                                diagnose.setTpSource(thirdPartySystem);
                                diagnose.setTpId(String.valueOf(d.getKisKey()) + ";" + d.getRefType());
                                if (d.getRefKisKey() > 0) {
                                    diagMap.put(d.getKisKey(), d.getRefKisKey()); //Merken du Honk!
                                }
                                diagnosen.put(d.getKisKey(), diagnose);
                            }

                            Iterator<Map.Entry<Long, Diagnose<?>>> diagIt = diagnosen.entrySet().iterator();
                            while (diagIt.hasNext()) {
                                Map.Entry<Long, Diagnose<?>> entry = diagIt.next();
                                Diagnose<?> diagnose = entry.getValue();
                                String[] tmp = diagnose.getTpId().split(";");
                                Long kis_key = Long.parseLong(tmp[0]);
                                int refType = tmp.length > 1 ? Integer.parseInt(tmp[1]) : 0;
                                Long ref_kis_key = diagMap.get(kis_key);
                                Diagnose<?> refDiagnose = diagnosen.get(ref_kis_key);
//                                if (refType == 1) {
//                                    diagnose.setIcdType(refType);
                                        
//                                }
                                if(refType != 0){
                                    diagnose.setRefIcdType(refType);

                                }
                                if (refDiagnose != null) {
                                    if (Nebendiagnose.class.equals(diagnose.getClass())) {
                                        Nebendiagnose nebendiagnose = (Nebendiagnose) diagnose;
                                        nebendiagnose.setRefIcd(refDiagnose);
                                        nebendiagnose.setRefIcdType(refType);
// TODO:  primary icd to secondary icd: if there are many secodary icds to one primary icd, this primary icd has to be cloned                                       
//                                        if(refType == 3){
//                                            refDiagnose.setRefIcdType(4);
//                                        }
                                    } else {
                                        LOG.log(Level.WARNING, "Expected secondary diagnoses to be reference of type ''{0}'', but it seems to be a primary diagnoses in this case: {1}", new Object[]{refType, caseKey});
                                    }
                                }
                                cpxMgr.write(diagnose);
                            }

                            Iterator<ProzedurenContainer> itProc = b.getProcList().iterator();
                            while (itProc.hasNext()) {
                                ProzedurenContainer p = itProc.next();
                                Procedure procedure = new Procedure(dep, ward);
                                procedure.setCode(p.getCode());
                                procedure.setDatum(p.getBeginDate());
                                procedure.setLokalisation(Lokalisation.findByValue(p.getLok()));
                                //procedure.setStation(b.spec_key); //oder b.station??
                                //procedure.setToGroup(true);
                                procedure.setToGroup(p.getIsDrg() == 1);
                                procedure.setTpSource(thirdPartySystem);
                                cpxMgr.write(procedure);
                            }

//                            if(importWard) {
//                                cpxMgr.write(ward);
//                            }
                            prevDep = dep;
                            prevBew = b;
                            if(!itWard.hasNext()){
                                cpxMgr.write(dep);
                                if(ward != null){
                                    cpxMgr.write(ward);
                                }
                            }

                        }

                        Iterator<CaseEntg> itFee = sapCase.getFees().iterator();
                        while (itFee.hasNext()) {
                            CaseEntg e = itFee.next();
                            Fee fee = new Fee(cs);
                            fee.setAnzahl(Integer.parseInt(e.getAnzahl()));
                            fee.setBetrag(Float.valueOf(e.getBetrag()));
                            fee.setVon(null);
                            fee.setBis(null);
                            fee.setEntgeltschluessel(e.getEntga());
                            fee.setKasse(sapCase.getKasse());
                            fee.setTob(0);
                            fee.setTpSource(thirdPartySystem);
                            cpxMgr.write(fee);
                        }

                        Iterator<Map.Entry<String, RmlLaborDocument>> itLab = sapCase.getLabordaten().entrySet().iterator();
                        while (itLab.hasNext()) {
                            Map.Entry<String, RmlLaborDocument> e = itLab.next();
                            final RmlLaborDocument labDoc = e.getValue();
                            final RmlLabor labData = labDoc.getLab();
                            if (labData == null) {
                                continue;
                            }
                            Lab lab = new Lab(cs);
                            //labDoc.getLabd_docdate();
                            //labDoc.getLabd_key();
                            //labDoc.getLabd_update();
                            lab.setAnalysis(labData.getLabvAnalysis());
                            lab.setBenchmark(labData.getLabvBenchmark());
                            lab.setComment(labData.getLabvComment());
                            lab.setDescription(labData.getLabvDescr());
                            lab.setGroup(labData.getLabvGroup());
                            lab.setAnalysisDate(labData.getLabvAnalysisdtm());
                            lab.setCategory(labData.getLabvCategory());
                            lab.setDate(labData.getLabvDate());
                            lab.setKisExternKey(labData.getKisExternKey());
                            //lab.setLockdel(???);
                            lab.setMaxLimit(labData.getLabvMaxLimit());
                            lab.setMinLimit(labData.getLabvMinlimit());
                            lab.setPosition(labData.getLabvPosition());
                            lab.setValue(labData.getLabvValue());
                            lab.setValue2(labData.getLabvValue2());
                            lab.setMethod(labData.getLabvMethode());
                            lab.setRange(labData.getLabvRange());
                            lab.setText(labData.getLabvText());
                            lab.setUnit(labData.getLabvUnit());
                            lab.setTpSource(thirdPartySystem);
                            cpxMgr.write(lab);
                        }

                        Iterator<SapFiFactura> itFiBill = sapCase.getFiDaten().getFacturas().iterator();
                        while (itFiBill.hasNext()) {
                            SapFiFactura fac = itFiBill.next();
                            SapFiBill fiBill = new SapFiBill(cs.getIkz(), cs.getFallNr(), patNr);
                            fiBill.setInvoice(fac.getVbeln());
                            fiBill.setFiscalYear(fac.getGjahr());
                            fiBill.setInvoiceDate(fac.getFkdat());
                            fiBill.setInvoiceKind(fac.getFkart());
                            fiBill.setInvoiceType(fac.getFktyp());
                            fiBill.setNetValue(fac.getNetwr());
                            fiBill.setReceiverRef(fac.getKunrg());
                            fiBill.setReferenceCurrency(fac.getWaerk());
                            fiBill.setReferenceType(fac.getVbtyp());
                            fiBill.setState(fac.getRfbsk());
                            fiBill.setStornoRef(fac.getFksto());
                            cpxMgr.write(fiBill);

                            Iterator<SapFiPosition> itBillposition = fac.getPositions().iterator();
                            while (itBillposition.hasNext()) {
                                SapFiPosition billposition = itBillposition.next();
                                SapFiBillposition fiBillposition = new SapFiBillposition(fiBill);
                                fiBillposition.setAmount(billposition.getFkimg());
                                fiBillposition.setBaseValue(billposition.getIshgprs());
                                fiBillposition.setInvoice(billposition.getVbeln());
                                fiBillposition.setNetValue(billposition.getNetwr());
                                fiBillposition.setPositionNumber(billposition.getPosnr());
                                fiBillposition.setReferenceId(billposition.getIshablst());
                                fiBillposition.setText(billposition.getArktx());
                                cpxMgr.write(fiBillposition);
                            }
                        }

                        Iterator<SapFiOpenItem> itFiOpenItems = sapCase.getFiOpenCaseStatus().getOpenItems().iterator();
                        while (itFiOpenItems.hasNext()) {
                            SapFiOpenItem fiOpenItemTmp = itFiOpenItems.next();
                            SapFiOpenItems fiOpenItem = new SapFiOpenItems(cs);
                            fiOpenItem.setCompanyCode(fiOpenItemTmp.getBukrs());
                            fiOpenItem.setCurrencyKey(fiOpenItemTmp.getWaers());
                            fiOpenItem.setCustomerNumber(fiOpenItemTmp.getKunnr());
                            fiOpenItem.setDebitCreditKey(fiOpenItemTmp.getShkzg());
                            fiOpenItem.setFiscalYear(fiOpenItemTmp.getGjahr());
                            fiOpenItem.setKindOfReceipt(fiOpenItemTmp.getBlart()); //fiOpenItemTmp.blart?
                            fiOpenItem.setNetValue(fiOpenItemTmp.getNetwr());
                            fiOpenItem.setNumberReceipt(fiOpenItemTmp.getBelnr()); //fiOpenItemTmp.belnr?
                            fiOpenItem.setOrderDateReceipt(fiOpenItemTmp.getCpudt()); //fiOpenItemTmp.cpudt?
                            fiOpenItem.setPostingKey(fiOpenItemTmp.getBschl());
                            fiOpenItem.setReceiptDateReceipt(fiOpenItemTmp.getBudat()); //fiOpenItemTmp.budat?
                            fiOpenItem.setRecordingDateReceipt(fiOpenItemTmp.getBldat()); //fiOpenItemTmp.bldat?
                            fiOpenItem.setRefNumber(fiOpenItemTmp.getXblnr());
                            fiOpenItem.setRefNumberReceipt(fiOpenItemTmp.getDzuonr()); //fiOpenItemTmp.dzuonr?
                            fiOpenItem.setText(fiOpenItemTmp.getSgtxt());
                            cpxMgr.write(fiOpenItem);
                        }
// write number1 - number10 and string1-string10
                        cs.setNumeric3(sapCase.getVorschlag()); // fall.vorschlag in t_case gibt es kein feld vorschlag, numeric3 ist nicht besetzt
                        cs.setNumeric1(sapCase.getStasp());//   private int stasp; /staatbürgerkennzeichen 
                        cs.setNumeric2(sapCase.getForei()); ////Ausländerkennzeichen
                        cs.setNumeric4(sapCase.getResid()); //kein Staatsbürger 
                        cs.setString1(sapCase.getBekat());//patDetails.BEKAT
                        cs.setString2(sapCase.getCaseState());//patDetails.CASESTATE
                        cs.setString3(sapCase.getRefCase());//m_curWKCase != null ? m_curWKCase.refCase : "") ,  
                        cs.setString4(sapCase.getRefCaseType());//m_curWKCase != null ? m_curWKCase.refCaseType : "")  
                        cs.setString5(sapCase.getAufnahmeanlass());//m_curWKCase != null ? m_curWKCase.aufnahmeanlass : "")
                        cs.setString6(sapCase.getWKCasesText());
                        // es gibt kein spalten für kisstate_first_date und kisstate_second_date
                        //(m_curWKCase != null ? getDateTimeString(m_curWKCase.kisstate_first_date, dbConnection.getIsOracle()) : "null") , 
                        //(m_curWKCase != null ? getDateTimeString(m_curWKCase.kisstate_second_date, dbConnection.getIsOracle()) : "null")
                        cpxMgr.write(cs);
                    }

                    for (Map.Entry<String, List<SapKainDetailSearchResult>> entry : kainMessagesSet.entrySet()) {
                        Iterator<SapKainDetailSearchResult> itKain = entry.getValue().iterator();
                        while (itKain.hasNext()) {
                            SapKainDetailSearchResult kainDetails = itKain.next();
                            final String thirdPartySystem = "SAP EINRI " + kainDetails.getEinri();
                            //final String patNr = "";
                            final String ikz = kainDetails.getIkz();
                            final String fallNr = kainDetails.getFalnr();
                            final Date kainReceivingDate = kainDetails.getReceivingDate();

                            KainInkaMessage kainMsg = kainDetails.createKAINMessage();
                            Kain kain = new Kain(ikz, fallNr, "");
                            kain.setContractReference(kainMsg.getContractIdent());
                            kain.setCostUnitSap(kainDetails.getKostr());
                            kain.setCurrentTransactionNr(kainMsg.getTransactionNumber());
                            kain.setHospitalIdentifier(kainMsg.getReceptorIdent());
                            kain.setHospitalNumberPatient(fallNr);
                            kain.setInsuranceCaseNumber(kainMsg.getCaseNumberInsurance());
                            kain.setInsuranceIdentifier(kainMsg.getSenderIdent()); //kainMsg.getInsuranceNumber()
                            kain.setInsuranceRefNumber(kainMsg.getRefNumberInsurance());
                            kain.setProcessingRef(kainMsg.getProcessIdent());
                            kain.setTpSource(thirdPartySystem);
                            kain.setReceivingDate(kainReceivingDate == null ? new Date() : kainReceivingDate);
                            kain.setTpId(kainDetails.getLf301());
                            kain.setCpxExternalMsgNr(kainDetails.getLf301());
                            //Fill kainInka
                            Iterator<SapKainPvvSearchResult> itKainPvv = kainDetails.getAlPVVs().iterator();
                            while (itKainPvv.hasNext()) {
                                SapKainPvvSearchResult kainPvv = itKainPvv.next();
                                KainInkaPvv kainInkaPvv = new KainInkaPvv(kain);
                                kainInkaPvv.setBillDate(kainPvv.getBillDate());
                                kainInkaPvv.setBillNr(kainPvv.getBillNumber());
                                kainInkaPvv.setInformationKey30(kainPvv.getInformation());
                                kainInkaPvv.setTpSource(thirdPartySystem);
                                //Fill kainInkaPvv
                                Iterator<PvtResultIf> itKainPvt = kainPvv.getAlPVTs().iterator();
                                while (itKainPvt.hasNext()) {
                                    PvtResultIf kainPvt = itKainPvt.next();
                                    KainInkaPvt kainInkaPvt = new KainInkaPvt(kainInkaPvv);
                                    kainInkaPvt.setMainDiagIcd(kainPvt.getPVTPrincipalDiagCode());
                                    kainInkaPvt.setMainDiagLoc(Lokalisation.findByValue(kainPvt.getPVTPrincipalDiagLoc()));
                                    kainInkaPvt.setMainDiagSecondaryIcd(kainPvt.getPVTPrincipalDiagSecondaryCode());
                                    kainInkaPvt.setMainDiagSecondaryLoc(Lokalisation.findByValue(kainPvt.getPVTPrincipalDiagSecondaryLoc()));
                                    kainInkaPvt.setOpsCode(kainPvt.getPVTProcedureCode());
                                    kainInkaPvt.setOpsLocalisation(Lokalisation.findByValue(kainPvt.getPVTProcedureLoc()));
                                    kainInkaPvt.setSecondaryDiagIcd(kainPvt.getPVTAuxDiagCode());
                                    kainInkaPvt.setSecondaryDiagLoc(Lokalisation.findByValue(kainPvt.getPVTAuxDiagLoc()));
                                    kainInkaPvt.setSecondarySecondDiagIcd(kainPvt.getPVTAuxDiagSecondaryCode());
                                    kainInkaPvt.setSecondarySecondDiagLoc(Lokalisation.findByValue(kainPvt.getPVTAuxDiagSecondaryLoc()));
                                    kainInkaPvt.setText(kainPvt.getText());
                                    kainInkaPvt.setTpSource(thirdPartySystem);
                                    //Fill kainInkaPvt
                                    cpxMgr.write(kainInkaPvt);
                                }
                                cpxMgr.write(kainInkaPvv);
                            }
                            cpxMgr.write(kain);
                        }
                    }
                }
                return new TransformResult(patientCount, caseCount, exceptions);
            }
        };
    }

    /**
     * Gets a list of hospital cases from JSON file
     *
     * @param pJsonFilename Path of JSON file
     * @return List of SAP Cases
     * @throws IllegalArgumentException path for JSON file is wrong
     * @throws Throwable Cannot unserialize JSON file
     */
    public FallContainer loadCaseData(final String pJsonFilename) throws IllegalArgumentException, Throwable {
        File file = new File(pJsonFilename);
        if (!file.exists() || !file.isFile()) {
             // try with zip 
             file = new  File(pJsonFilename + ".zip");
              if (!file.exists() || !file.isFile()){
                throw new IllegalArgumentException("JSON file does not exist: " + file.getAbsolutePath());
              }
        }
       
        if (file.getAbsolutePath().toLowerCase().endsWith(".zip")) {
            String newFilename = uncompressFile(file);
            file = new File(newFilename);
        }
        JsonDumpFileReader lJsonReader = new JsonDumpFileReader(file.getAbsolutePath());
        FallContainer fallContainer = lJsonReader.readFile();
//        FallContainer fallContainer = null;
//        FallContainer fallContainerTmp;
//        while ((fallContainerTmp = lJsonReader.readFile()) != null) {
//            fallContainer = fallContainerTmp;
//            break;
//        }
        return fallContainer;
    }

    /**
     * Gets a list of hospital cases and KAIN messages from SAP.Establishes a
     * connection SAP with the passed parameters.The data which will be loaded
     * depends on the date range (change date till now).
     *
     * @param pSapConfig SAP configuration
     * @param pChangeDate Change date
     * @param pUsePool Use pooling?
     * @return List of SAP Cases (cannot be null)
     * @throws JCoException Exception
     * @throws NoSuchAlgorithmException Algorithm for anonymization not found
     */
    @NotNull
    public FallContainer loadCaseData(final ImportConfig<Sap> pSapConfig /*, final String pHosIdent */, 
            final Date pChangeDate, final boolean pUsePool) throws JCoException, NoSuchAlgorithmException {
        //Set<SapCase> faelle;
        final SapJob sapConfig = pSapConfig.getModule().getInputConfig();
//        final FallContainer fallContainer = new FallContainer();
        final FallContainer fallContainer = pSapConfig.getModule().getContainer();
        try (SapConnection sapConnection = new SapConnection(pSapConfig /*, pHosIdent */, pUsePool)) {

//            mProperties = sapConnection.sapProperties;
            final List<SapCase> importableCaseNumbers;
            Map<String, String> kainCaseNumbers = null;
            final HashSet<String> kainCaseKeys = new HashSet<>(0);
            //1st Step: Retrieve a list of hospital cases that have changed since last import (single case import happens here!)
            if (!pSapConfig.getCaseNumbers().isEmpty()) {
                LOG.log(Level.INFO, "Use passed list of case numbers for institution ''{0}''...", sapConnection.mInstitution);
                final List<SapCase> tmp = new ArrayList<>();
                for (final String caseNumber : pSapConfig.getCaseNumbers()) {
                    if (caseNumber == null || caseNumber.trim().isEmpty()) {
                        continue;
                    }
                    final SapCase sapCase = new SapCase();
                    sapCase.setInstitution(sapConnection.mInstitution);
                    sapCase.setFallnr(caseNumber.trim());
                    tmp.add(sapCase);
                }
                importableCaseNumbers = tmp;
            } else {
                LOG.log(Level.INFO, "Get the list of importable cases for institution ''{0}''...", sapConnection.mInstitution);
                kainCaseNumbers = sapConnection.getNewKainCaseNumbers(sapConnection.mInstitution, pChangeDate);
                LOG.log(Level.FINE, "Importable hospital cases for KAIN-Messages in SAP for institution ''{0}'' were found: {1}", new Object[]{sapConnection.mInstitution, kainCaseNumbers.size()});

                importableCaseNumbers = sapConnection.getImportableCaseNumbers(sapConnection.mInstitution, pChangeDate, kainCaseNumbers);
            }

            LOG.log(Level.INFO, "Importable hospital cases in SAP for institution ''{0}'' were found: {1}", new Object[]{sapConnection.mInstitution, importableCaseNumbers.size()});

            //2nd Step: Retrieve the case status for importable hospital cases
            LOG.log(Level.INFO, "Retrieve hospital case status...");
            int errors = 0;
            Iterator<SapCase> itStatus = importableCaseNumbers.iterator();
            int size = importableCaseNumbers.size();
            int i = 0;
            int notFoundCaseCount = 0;
            if (kainCaseNumbers != null) {
                kainCaseKeys.addAll(kainCaseNumbers.values());
            }
            while (itStatus.hasNext()) {
                i++;
                SapCase sapCase = itStatus.next();
                sapCase.setIkz(pSapConfig.getModule().getInputConfig().getDefaultHosIdent());
                final String prozent = DoubleToStr(StrUtils.getKaufmaennischGerundet((i / ((double) size)) * 100, 2));
                LOG.log(Level.INFO, "{0}/{1} {2}%: Load hospital case status for {3}...", new Object[]{i, size, prozent, sapCase.getCaseKey()});
                try {
                    if (!sapConnection.loadCaseStatus(sapCase)) {
                        itStatus.remove();
                        notFoundCaseCount++;
                        continue;
                    }
                } catch (JCoException ex) {
                    errors++;
                    LOG.log(Level.SEVERE, "Was not able get SAP status of hospital case '" + sapCase.getCaseKey() + "'. I will ignore this case!", ex);
                    itStatus.remove();
                }
                LOG.log(Level.FINE, "Status result of {0}: state = {1}, isLeading = {2}, isWK = {3}, fallnrLeading = {4}", new Object[]{sapCase.getCaseKey(), sapCase.getState(), sapCase.isIsLeading(), sapCase.isIsWK(), sapCase.getFallnrLeading()});
            }
            LOG.log(Level.INFO, "All hospital case statuses were retrieved ({0} errors)!", errors);

            //3th Step: Retrieve the case data for importable hospital cases
            LOG.log(Level.INFO, "Retrieve case data...");
            Iterator<SapCase> itDaten = importableCaseNumbers.iterator();
            int ignoredCaseCount = 0;
            i = 0;
            errors = 0;
            while (itDaten.hasNext()) {
                i++;
                final SapCase sapCase = itDaten.next();
                LOG.log(Level.FINE, "Load data for hospital case {0}", sapCase.getCaseKey());
                final String prozent = DoubleToStr(getKaufmaennischGerundet((i / ((double) size)) * 100, 2));
                LOG.log(Level.INFO, "{0}/{1} {2}%: Load hospital case data for {3}...", new Object[]{i, size, prozent, sapCase.getCaseKey()});
                if (sapCase.getState() == SapCase.getSTATE_WK()) {
                    LOG.log(Level.INFO, "Skip case {0} with return code {1}", new Object[]{sapCase.getCaseKey(), MESSAGE_STATE_WK_CASE});
                    itDaten.remove();
                    ignoredCaseCount++;
                    continue;
                }
                if (sapCase.getState() == SapCase.getSTATE_NOTEXISTS()) {
                    LOG.log(Level.INFO, "Skip case {0} with return code {1}", new Object[]{sapCase.getCaseKey(), MESSAGE_STATE_CASE_NOT_EXISIT});
                    itDaten.remove();
                    ignoredCaseCount++;
                    continue;
                }

                try {
                    if (!sapConnection.loadCaseData(sapCase, kainCaseKeys)) {
                        //Hospital case not found
                        itDaten.remove();
                        notFoundCaseCount++;
                        continue;
                    }
                } catch (JCoException ex) {
                    errors++;
                    LOG.log(Level.SEVERE, "Was not able get SAP data of hospital case '" + sapCase.getCaseKey() + "'. I will ignore this case!", ex);
                    itDaten.remove();
                    //ignoredCaseCount++;
                    continue;
                }

                LOG.log(Level.FINE, "Hospital case data was retrieved successfully: {0}", String.valueOf(sapCase));
                LOG.log(Level.FINE, "Data result of {0}: aufnahmedatum = {1}, aufnahmeanlass = {2}, aufnahmegrund12 = {3}, aufnahmegrund34 = {4}, entlassungdatum = {5}, entlassungsgrund12 = {6}, entlassungsgrund3 = {7}, fees = {8}, wards = {9}", new Object[]{sapCase.getCaseKey(), sapCase.getAufnahmedatum(), sapCase.getAufnahmeanlass(), sapCase.getAufnahmegrund12(), sapCase.getAufnahmegrund34(), sapCase.getEntlassungdatum(), sapCase.getEntlassungsgrund12(), sapCase.getEntlassungsgrund3(), sapCase.getFees(), sapCase.getWards()});

                fallContainer.addSapCase(sapCase);
            }
            LOG.log(Level.INFO, "All hospital case data was retrieved ({0} cases not found, {1} cases were ignored, {2} errors)!", new Object[]{notFoundCaseCount, ignoredCaseCount, errors});

            //4th Step: Retrieve the KAIN data for a given date range (change date till today)
//            final Set<String> caseNumbers;
//            if (!pSapConfig.getCaseNumbers().isEmpty()) {
//                //retrieve KAIN data only for passed case numbers
//                caseNumbers = pSapConfig.getCaseNumbers();
//            } else {
            //retrieve KAIN data for all cases in specific data range
//            final Set<String> caseNumbers = sapConnection.getNewKainCaseNumbers(sapConnection.mInstitution, pChangeDate, pSapConfig.getCaseNumbers());
//            }
            LOG.log(Level.FINE, "Importable cases_Keys for KAIN-Messages in SAP for institution ''{0}'' were found: {1}", new Object[]{sapConnection.mInstitution, kainCaseKeys.size()});
            final Map<String, List<SapKainDetailSearchResult>> kainMessages = performKAINForCaseResults(sapConnection, sapConnection.mInstitution, kainCaseKeys, sapConfig.getDefaultHosIdent());
            fallContainer.setKainMessages(kainMessages);
            //5th Step: Write to JSON file
            if (sapConfig.isWriteJsonDump()) {
                try {
                    LOG.log(Level.INFO, "Write to JSON file...");
                    sapConnection.writeFallContainer(fallContainer);
                    LOG.log(Level.INFO, "Successfully written to JSON file...");
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, "Writing to JSON file failed!", ex);
                }
            }
        }

        return fallContainer;
    }

    /**
     * Gets KAIN Messages for a passed list of case numbers
     *
     * @param pSapConnection Connection to SAP
     * @param pInstitution Institution
     * @param pCaseNumbers Hospital case numbers
     * @param pHosIdent Default hospital ident
     * @return KAIN messages
     * @throws JCoException Exception
     */
    public Map<String, List<SapKainDetailSearchResult>> performKAINForCaseResults(final SapConnection pSapConnection, final String pInstitution, final Set<String> pCaseNumbers, final String pHosIdent) throws JCoException {
        if (!pSapConnection.sapProperties.importKAIN()) {
            return new HashMap<>();
        }
        final Map<String, List<SapKainDetailSearchResult>> kainResults = new HashMap<>();
        final int size = pCaseNumbers.size();
        Iterator<String> it = pCaseNumbers.iterator();
        int i = 0;
        while (it.hasNext()) {
            i++;
            final String caseIdent = it.next();
            final String[] row = caseIdent.split("_");
            if (row.length >= 3) {
                String strFallNr = toStr(row[0]); //Case Number
                final String strFacility = toStr(row[1]); //sending Institution Einrichtung
                final String strKOSTR = toStr(row[2]); //Insurance identifier
                final String strIKNR = getIKZ(pSapConnection, strFacility);

                LOG.log(Level.FINE, "strFacility={0}, strKOSTR={1}, strIKNR={2}", new Object[]{strFacility, strKOSTR, strIKNR});

                final String prozent = DoubleToStr(getKaufmaennischGerundet((i / ((double) size)) * 100, 2));
                LOG.log(Level.INFO, "{0}/{1} {2}%: Load KAIN data for hospital identifier {3} case number {4}", new Object[]{i, size, prozent, strKOSTR, strFallNr});

                if (strFallNr != null && strFacility != null && strKOSTR != null) {
//                    try {
                    if (strFallNr.startsWith("0")) {
                        strFallNr = "" + Integer.parseInt(strFallNr);
                    }
                    List<SapKainDetailSearchResult> kainMessages = pSapConnection.getKainMessagesForCase(pInstitution, strFallNr, strKOSTR, pHosIdent);
                    LOG.log(Level.FINE, "KAIN message found for {0}: {1}", new Object[]{caseIdent, kainMessages.size()});
                    kainResults.put(caseIdent, kainMessages);
                } else {
                    LOG.log(Level.FINE, "Identifier for KAIN message not completed: Fallnr: {0}, Facility: {1}, strKOSTR: {2}", new Object[]{strFallNr, strFacility, strKOSTR});
                }
            } else {
                LOG.log(Level.SEVERE, "Case information to get KAIN messages for caseIdent ''{0}'' is incomplete!", caseIdent);
            }
        }
        return kainResults;
    }

    private String getIKZ(final SapConnection pSapConnection, String facility) {
        //TODO???
        //Checkpoint retrieves the IKZ from sapikzmapping table here!
        return facility;
    }

//    public SapProperties getProperties() {
//        return mProperties;
//    }
//    
//    public License getLicense() {
//        return mLicense;
//    }
}
