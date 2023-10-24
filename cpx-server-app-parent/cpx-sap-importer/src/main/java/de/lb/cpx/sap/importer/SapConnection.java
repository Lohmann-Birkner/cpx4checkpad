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

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoFunctionTemplate;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;
import com.sap.conn.jco.ext.DestinationDataProvider;
import de.lb.cpx.cpx.license.crypter.License;
import de.lb.cpx.model.TP301Inka;
import de.lb.cpx.model.TP301KainInkaPvt;
import de.lb.cpx.model.TP301KainInkaPvv;
import de.lb.cpx.sap.container.FallContainer;
import de.lb.cpx.sap.container.Sap301MessageInfo;
import de.lb.cpx.sap.dto.CaseEntg;
import de.lb.cpx.sap.dto.Insurance;
import de.lb.cpx.sap.dto.ReferenzCase;
import de.lb.cpx.sap.dto.RmlLabor;
import de.lb.cpx.sap.dto.RmlLaborDocument;
import de.lb.cpx.sap.dto.SapCase;
import de.lb.cpx.sap.dto.SapExportResult;
import de.lb.cpx.sap.dto.SapFiFactura;
import de.lb.cpx.sap.dto.SapFiOpenItem;
import de.lb.cpx.sap.dto.SapFiPosition;
import de.lb.cpx.sap.dto.SapImExManager;
//import static de.lb.cpx.sap.importer.ImportProcessSap.ANONYMOUSIZE_DATA;
import de.lb.cpx.sap.importer.utils.JsonDumpFileWriter;
import static de.lb.cpx.sap.importer.utils.SapStrUtils.*;
import de.lb.cpx.sap.properties.SapProperties;
import de.lb.cpx.sap.results.SapDiagnosisSearchResult;
import de.lb.cpx.sap.results.SapKainDetailSearchResult;
import de.lb.cpx.sap.results.SapKainPvtSearchResult;
import de.lb.cpx.sap.results.SapKainPvvSearchResult;
import de.lb.cpx.sap.results.SapKisStateSearchResult;
import de.lb.cpx.sap.results.SapMovementSearchResult;
import de.lb.cpx.sap.results.SapPatientDetailResult;
import de.lb.cpx.sap.results.SapPatientSearchResult;
import de.lb.cpx.sap.results.SapProcedureSearchResult;
import de.lb.cpx.shared.dto.job.config.other.SapJob;
import de.lb.cpx.str.utils.StrUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import module.impl.ImportConfig;
import static sun.misc.Signal.handle;

/**
 * Offers some methods to gets hospital case data from SAP and to anaylize and
 * translate them to a format than CPX can handle
 *
 * @author niemeier
 */
public class SapConnection implements AutoCloseable {

    public static final int MESSAGE_STATE_FINISHED = 2;
    public static final String FORMAT_DATE = "yyyyMMdd";

    public static final String MAP_KEY_FI_CASE_STATUS = "FI_CASE_STATUS";
    public static final String MAP_KEY_FI_OPEN_ITEM_STATUS = "FI_OPEN_ITEM_STATUS";

    private static final Logger LOG = Logger.getLogger(SapConnection.class.getName());
    //protected static final String MAP301_RESOURCE_NAME = "bapi_map301";
    public static final String DESTINATION_NAME1 = "ABAP_AS_WITHOUT_POOL";
    public static final String DESTINATION_NAME2 = "ABAP_AS_WITH_POOL";
    public static final String SAP_TABLE_NVVF = "NVVF";
    public static final String SAP_TABLE_NVVP = "NVVP";
    public static final String SAP_TABLE_NCIR = "NCIR";
    public static final String SAP_TABLE_NPIR = "NPIR";
    public final SapProperties sapProperties;

    //protected static final String BAPI_COMMIT = "BAPI_TRANSACTION_COMMIT";
    //protected static final String BAPI_TRANSACTION_COMMIT = BAPI_COMMIT;
    protected static final String BAPI_TRANSACTION_COMMIT = "BAPI_TRANSACTION_COMMIT";

    protected static final String RFC_INKA_CREATE = "ZLBH_INKA_CREATE";
    protected static final String Z_RFC_INKA_CREATE = RFC_INKA_CREATE;

    protected static final String RFC_SET_CP_REQUEST_STATUS = "ZLBH_ADD_CP_STATUS";
    protected static final String Z_RFC_SET_CP_REQUEST_STATUS = RFC_SET_CP_REQUEST_STATUS;

    private final JsonDumpFileWriter mWriter;

    public final String mServer;
    public final String mSysNr;
    public final String mMandant;
    public final String mInstitution;
    public final String mUser;
    public final String mPassword;
    public final String mLanguage;
    public final boolean mUsePool;
    public final License mLicense;
    private final JCoDestination mDestination;
    private final JCoRepository mRepository;

    private SapImExManager mSapMgr;
    private Map<String, String> mHmLabTarif;
    private Map<String, String> mHmLabGroup;
    private boolean doAnonymize = false;
    private String mJsonPath = "";
    //public static final int MESSAGE_STATE_NO_STATIONAER = 4;
    protected Comparator<SapMovementSearchResult> mMovementDateComp = new Comparator<>() {
        @Override
        public int compare(final SapMovementSearchResult movLhs, final SapMovementSearchResult movRhs) {
            if (movLhs != null && movRhs != null) {
                if (movLhs.getBwidt().equals(movRhs.getBwidt())) {
                    if (movLhs.getBwizt().equals(movRhs.getBwizt())) {
                        return 0;
                    } else if (movLhs.getBwizt().before(movRhs.getBwizt())) {
                        return -1;
                    } else {
                        return 1;
                    }
                } else if (movLhs.getBwidt().before(movRhs.getBwidt())) {
                    return -1;
                } else {
                    return 1;
                }
            }
            return 0;
        }
    };

//    /**
//     * Creates a SAP Connection.Establish a single connection to a SAP
// destination and to a SAP repository to request meta information.
//     *
//     * @param pSapConfigName SAP Config name in cpx_server_config.xml
//     * @param pUsePool Use pooling?
//     * @param pLicense CPX license
//     * @throws IllegalArgumentException Illegal Argument passed
//     * @throws JCoException Exception
//     */
//    public SapConnection(final String pSapConfigName /*, final String pHosIdent */, final boolean pUsePool, final License pLicense) throws JCoException {
//        this(new CpxServerConfig().getSapConfig(pSapConfigName) /*, pHosIdent */, pUsePool, pLicense);
//    }
    /**
     * Creates a SAP Connection.Establish a single connection to a SAP
     * destination and to a SAP repository to request meta information. pUsePool
     * is automatically false!
     *
     * @param pSapConfig SAP Config in cpx_server_config.xml
     * @throws IllegalArgumentException Illegal Argument passed
     * @throws JCoException Exception
     */
    public SapConnection(final ImportConfig<Sap> pSapConfig /*, final String pHosIdent */) throws JCoException {
        this(pSapConfig /*, pHosIdent */, false);
    }

    /**
     * Creates a SAP Connection.Establish a single connection to a SAP
     * destination and to a SAP repository to request meta information.
     *
     * @param pSapConfig SAP Config in cpx_server_config.xml
     * @param pUsePool Use pooling?
     * @throws IllegalArgumentException Illegal Argument passed
     * @throws JCoException Exception
     */
    public SapConnection(final ImportConfig<Sap> pSapConfig /*, final String pHosIdent */, final boolean pUsePool) throws JCoException {
        if (pSapConfig == null) {
            throw new IllegalArgumentException("No SAP Config passed!");
        }
        final SapJob sapConfig = pSapConfig.getModule().getInputConfig();
        sapProperties = new SapProperties(sapConfig.getDefaultHosIdent());

        mServer = sapConfig.getServer() == null ? "" : sapConfig.getServer().trim();
        mSysNr = sapConfig.getSysNr() == null ? "" : sapConfig.getSysNr().trim();
        mMandant = sapConfig.getMandant() == null ? "" : sapConfig.getMandant().trim();
        mInstitution = sapConfig.getInstitution() == null ? "" : sapConfig.getInstitution().trim();
        mUser = sapConfig.getUser() == null ? "" : sapConfig.getUser().trim();
        mPassword = sapConfig.getPassword() == null ? "" : sapConfig.getPassword().trim();
        mLanguage = "de";
        mUsePool = pUsePool;
        mLicense = pSapConfig.getLicense();
        doAnonymize = sapConfig.isDoAnonymize();
        mJsonPath = sapConfig.getmJsonPath();
        LOG.log(Level.INFO, "Try to establish connection to SAP with the following settings: "
                + "  SAP Config Name: " + sapConfig.getName()
                + "  Server: " + mServer
                + "  SysNr: " + mSysNr
                + "  Mandant: " + mMandant
                + "  Institution: " + mInstitution
                + "  User: " + mUser
                + "  Password: " + "(hidden)"
                + "  Language: " + mLanguage
                + "  Use Pool: " + (mUsePool ? "yes" : "no")
        );

        if (mServer.trim().isEmpty()) {
            throw new IllegalArgumentException("No valid Host was given!");
        }
        if (mSysNr.trim().isEmpty()) {
            throw new IllegalArgumentException("No valid SysNr was given!");
        }
        if (mMandant.trim().isEmpty()) {
            throw new IllegalArgumentException("No valid Client was given!");
        }
        if (mUser.trim().isEmpty()) {
            throw new IllegalArgumentException("No valid User was given!");
        }
        if (mPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("No valid Password was given!");
        }
        if (mLanguage.trim().isEmpty()) {
            throw new IllegalArgumentException("No valid Language was given!");
        }
        if (mLicense == null) {
            throw new IllegalArgumentException("No CPX License was given!");
        }
        if (!mLicense.isValid()) {
            throw new IllegalArgumentException("Invalid CPX License was given!");
        }

        mDestination = createSapDestination();
        mRepository = createSapRepository();
        mWriter = new JsonDumpFileWriter((mJsonPath == null?"":(mJsonPath + File.separator)) +"fallContainer.json");
    }

//    /**
//     * Creates a SAP Connection.Establish a single connection to a SAP
//     * destination and to a SAP repository to request meta information.
//     *
//     * @param pServer Server
//     * @param pSysNr System number
//     * @param pMandant Mandant
//     * @param pInstitution Institution
//     * @param pUser User
//     * @param pPassword Password
//     * @param pHosIdent hospital identifier
//     * @param pUsePool Use pooling?
//     * @throws IllegalArgumentException Illegal Argument passed
//     * @throws JCoException Exception
//     */
//    public SapConnection(final String pServer, final String pSysNr, final String pMandant, final String pInstitution, 
//                         final String pUser, final String pPassword, final String pHosIdent, final boolean pUsePool, 
//                         final License pLicense) throws IllegalArgumentException, JCoException {
//        mServer = pServer == null ? "" : pServer.trim();
//        mSysNr = pSysNr == null ? "" : pSysNr.trim();
//        mMandant = pMandant == null ? "" : pMandant.trim();
//        mInstitution = pInstitution == null ? "" : pInstitution.trim();
//        mUser = pUser == null ? "" : pUser.trim();
//        mPassword = pPassword == null ? "" : pPassword.trim();
//        mLanguage = "de";
//        mUsePool = pUsePool;
//        mLicense = pLicense;
//
//        sapProperties = new SapProperties(pHosIdent);
//
//        LOG.log(Level.INFO, "Try to establish connection to SAP with the following settings: "
//                + "  Server: " + mServer
//                + "  SysNr: " + mSysNr
//                + "  Mandant: " + mMandant
//                + "  Institution: " + mInstitution
//                + "  User: " + mUser
//                + "  Password: " + "(hidden)"
//                + "  Language: " + mLanguage
//                + "  Use Pool: " + (mUsePool ? "yes" : "no")
//        );
//
//        if (mServer.trim().isEmpty()) {
//            throw new IllegalArgumentException("No valid Host was given!");
//        }
//        if (mSysNr.trim().isEmpty()) {
//            throw new IllegalArgumentException("No valid SysNr was given!");
//        }
//        if (mMandant.trim().isEmpty()) {
//            throw new IllegalArgumentException("No valid Client was given!");
//        }
//        if (mInstitution.trim().isEmpty()) {
//            throw new IllegalArgumentException("No valid Institution was given!");
//        }
//        if (mUser.trim().isEmpty()) {
//            throw new IllegalArgumentException("No valid User was given!");
//        }
//        if (mPassword.trim().isEmpty()) {
//            throw new IllegalArgumentException("No valid Password was given!");
//        }
//        if (mLanguage.trim().isEmpty()) {
//            throw new IllegalArgumentException("No valid Language was given!");
//        }
//        if (mLicense == null) {
//            throw new IllegalArgumentException("No CPX License was given!");
//        }
//        if (!mLicense.isValid()) {
//            throw new IllegalArgumentException("Invalid CPX License was given!");
//        }
//
//        mDestination = createSapDestination();
//        mRepository = createSapRepository();
//        mWriter = new JsonDumpFileWriter("fallContainer.json");
//    }
    private static void createDestinationDataFile(final String destinationName, final Properties connectProperties) {
        File destCfg = new File(destinationName + ".jcoDestination");
        try {
            try (FileOutputStream fos = new FileOutputStream(destCfg, false)) {
                connectProperties.store(fos, "for tests only !");
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to create the destination files", e);
        }
    }

    /**
     * Transform SAP admission cause to CP admission cause
     *
     * @param pAufnahmeanlass Admission cause
     * @return admission cause in Checkpoint
     */
    public static String getCpAufnahmeanlass(final String pAufnahmeanlass) {
        String newAufnahmeanlass = "1";
        final String aufnahmeAnlass = pAufnahmeanlass == null ? "" : pAufnahmeanlass.trim();
        if (!aufnahmeAnlass.isEmpty()) {
//            try {
            switch (aufnahmeAnlass.charAt(0)) {
                case 'E':
                case 'e':
                    newAufnahmeanlass = "1";
                    break;
                case 'Z':
                case 'z':
                    newAufnahmeanlass = "2";
                    break;
                case 'N':
                case 'n':
                    newAufnahmeanlass = "3";
                    break;
                case 'R':
                case 'r':
                    newAufnahmeanlass = "4";
                    break;
                case 'V':
                case 'v':
                    newAufnahmeanlass = "5";
                    break;
                case 'K':
                case 'k':
                    newAufnahmeanlass = "6";
                    break;
                case 'G':
                case 'g':
                    newAufnahmeanlass = "7";
                    break;
                case 'B':
                case 'b':
                    newAufnahmeanlass = "8";
                    break;
                case 'A':
                case 'a':
                    newAufnahmeanlass = "9";
                    break;
                default:
                    newAufnahmeanlass = "1";
                    break;
            }
        }

        switch (newAufnahmeanlass) {
            case "1":
                return "E";
            case "2":
                return "Z";
            case "3":
                return "N";
            case "4":
                return "R";
            case "5":
                return "V";
            case "6":
                return "K";
            case "7":
                return "G";
            case "8":
                return "B";
            default:
                return "E";
        }
        //return "1";
        //return "E";
    }

    /**
     * Get SAP Manager
     *
     * @return SAP Import/Export Manager
     */
    public SapImExManager getSAPManager() {
        if (mSapMgr == null) {
            mSapMgr = new SapImExManager();
        }
        return mSapMgr;
    }

    /**
     * Write SAP case to JSON file
     *
     * @param pFallContainer SAP Case
     * @throws IOException Exception
     */
    public void writeFallContainer(FallContainer pFallContainer) throws IOException {
        mWriter.writeFile(pFallContainer);
    }

    private Properties getProperties() {
        final Properties connectProperties = new Properties();
        final String language = "de";

        final int loginType = sapProperties.getLoginType();
        switch (loginType) {
            case 1:
                connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, mMandant);
                connectProperties.setProperty(DestinationDataProvider.JCO_USER, mUser);
                connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, mPassword);
                connectProperties.setProperty(DestinationDataProvider.JCO_LANG, language);
                connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR, mSysNr);
                connectProperties.setProperty(DestinationDataProvider.JCO_GWHOST, sapProperties.getGWHost());
                connectProperties.setProperty(DestinationDataProvider.JCO_GWSERV, sapProperties.getGWServ());
                break;
            case 2:
                connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, mMandant);
                connectProperties.setProperty(DestinationDataProvider.JCO_USER, mUser);
                connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, mPassword);
                connectProperties.setProperty(DestinationDataProvider.JCO_LANG, language);
                connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, mServer);
                connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR, mSysNr);
                connectProperties.setProperty(DestinationDataProvider.JCO_R3NAME, sapProperties.getR3Group());
                break;
            case 3:
                connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, mMandant);
                connectProperties.setProperty(DestinationDataProvider.JCO_USER, mUser);
                connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, mPassword);
                connectProperties.setProperty(DestinationDataProvider.JCO_LANG, language);
                connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, mServer);
                break;
            default:
                connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, mMandant);
                connectProperties.setProperty(DestinationDataProvider.JCO_USER, mUser);
                connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, mPassword);
                connectProperties.setProperty(DestinationDataProvider.JCO_LANG, language);
                connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, mServer);
                connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR, mSysNr);
                break;
        }

        if (mUsePool) {
            connectProperties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY, "3");
            connectProperties.setProperty(DestinationDataProvider.JCO_PEAK_LIMIT, "10");
        }

        //createDestinationDataFile(DESTINATION_NAME1, connectProperties);
        return connectProperties;
    }

    /**
     * SAP Destination
     *
     * @return SAP Destination
     */
    public JCoDestination getSapDestination() {
        return mDestination;
    }

    private JCoDestination createSapDestination() throws JCoException {
        Properties connectProperties = getProperties();
        final String DESTINATION_NAME = mUsePool ? DESTINATION_NAME2 : DESTINATION_NAME1;
        createDestinationDataFile(DESTINATION_NAME, connectProperties);
        JCoDestination destination = JCoDestinationManager.getDestination(DESTINATION_NAME);
        return destination;
    }

    /**
     * Gives you the instance to the SAP Repository
     *
     * @return SAP Repository
     */
    public JCoRepository getSapRepository() {
        return mRepository;
    }

    private JCoRepository createSapRepository() throws JCoException {
        JCoDestination destination = getSapDestination();
        if (destination == null) {
            throw new IllegalArgumentException("Destination is null!");
        }
        return destination.getRepository();
    }

    /**
     * Get list of SAP cases that have to be imported ("prüfbereite Fälle")
     *
     * @param pInstitution Einrichtung
     * @param pChangeDate Get only cases that are more recent than the change
     * date
     * @param pKainCaseNumbers case numbers
     * @return List of SAP case numbers
     * @throws JCoException Exception
     */
    public List<SapCase> getImportableCaseNumbers(final String pInstitution, final Date pChangeDate, final Map<String, String> pKainCaseNumbers) throws JCoException {
        List<SapCase> importableCaseNumbersLst = new ArrayList<>();
        final String functionName = sapProperties.getBAPICaseMethod("");
        JCoFunction function = createFunction(functionName); //"ZLBH_CASELIST_GET_01"
        if (function == null) {
            LOG.log(Level.INFO, "Method to get list of importable case numbers is not available");
            return new ArrayList<>();
        }
        JCoParameterList parameter = function.getImportParameterList();
        parameter.setValue("I_EINRI", pInstitution); //12345678
        parameter.setValue("I_TIMESTAMP", getDate(pChangeDate)); //now

        String setFalAr = "";
        if (sapProperties.importStationaer()) {
            if (setFalAr.length() != 0) {
                setFalAr += ";";
            }
            setFalAr += "1";
        }
        if (sapProperties.importTeilStationaer()) {
            if (setFalAr.length() != 0) {
                setFalAr += ";";
            }
            setFalAr += "3";
        }
        if (sapProperties.importAmbulantOP() || sapProperties.importAmbulantCancels()) {
            if (setFalAr.length() != 0) {
                setFalAr += ";";
            }
            setFalAr += "2";
        }

        if (setFalAr.length() != 0) {
            parameter.setValue("I_FALARS", setFalAr);
        }

        if (sapProperties.getSSTVersion() >= 7) {
            String strDepartmentsForImport = sapProperties.getCaseNumbers4Department();
            LOG.log(Level.INFO, "Beginn Übergabe Fachabteilungsbezeichnungen an SAP.");
            JCoTable tblDepValues = function.getTableParameterList().getTable("I_ORGFILTER");
            if (tblDepValues != null) {
                if (strDepartmentsForImport != null && !strDepartmentsForImport.isEmpty()) {
                    String[] aryDepImport = strDepartmentsForImport.split(";");
//                    if (aryDepImport != null) {
                    for (int i = 0, n = aryDepImport.length; i < n; i++) {
                        LOG.log(Level.INFO, "Liste der aufgeteilten OEs {0}: {1}", new Object[]{i, aryDepImport[i]});
                    }
//                    } else {
//                        LOG.log(Level.SEVERE, "Liste der OEs konnte nicht aufgeteilt werden!");
//                    }
                    for (int i = 0, n = aryDepImport.length; i < n; i++) {
                        String strEnc = aryDepImport[i];
                        if (strEnc != null) {
                            tblDepValues.appendRow();
                            tblDepValues.setValue("ORGFA", strEnc.trim());
                            tblDepValues.setValue("ORGPF", "*");
                            LOG.log(Level.INFO, "Suche nach Fallnummern folgender OEs: {0}", strEnc.trim());
                        } else {
                            LOG.log(Level.SEVERE, "Gelesenes Element der Fachabteilungsliste ist null.");
                        }
                    }
                } else {
                    tblDepValues.appendRow();
                    tblDepValues.setValue("ORGFA", "*");
                    tblDepValues.setValue("ORGPF", "*");
                    LOG.log(Level.INFO, "Keine Fachabteilungs- bzw.Stationsliste an SAP übergeben.");
                    LOG.log(Level.INFO, "ORGFA: *");
                    LOG.log(Level.INFO, "ORGPF: *");
                }
                LOG.log(Level.INFO, "Ende Übergabe Fachabteilungsbezeichnungen an SAP.");
            } else {
                LOG.log(Level.SEVERE, "Fehler beim Holen der Tabelle I_ORGFILTER aus SAP-Funktion.");
            }
        }

        callFunction(function);

        JCoTable caseTab = function.getTableParameterList().getTable("E_CASETAB");
        Set<String> pCaseNumbersFromKAIN = new HashSet<>();
        LOG.log(Level.FINE, "caseTab.getNumRows {0}", caseTab.getNumRows());
        LOG.log(Level.FINE, "caseTab.getNumColumns {0}", caseTab.getNumColumns());
        for (int i = 0, n = caseTab.getNumRows(); i < n; i++) {
            caseTab.setRow(i);
            final String fallNr = checkSapFallNr(caseTab.getString("FALNR"));
            if (pKainCaseNumbers != null) {
                pCaseNumbersFromKAIN = pKainCaseNumbers.keySet();
                if (pCaseNumbersFromKAIN != null && !pCaseNumbersFromKAIN.isEmpty() && fallNr != null) {
                    pKainCaseNumbers.remove(fallNr);
                }
            }
            final SapCase sapCase = new SapCase();
            sapCase.setInstitution(pInstitution);
            sapCase.setFallnr(fallNr);
            importableCaseNumbersLst.add(sapCase);
        }
        LOG.log(Level.FINE, "Importable hospital cases for KAIN-Messages after correction ''{0}'': {1}", new Object[]{mInstitution, pCaseNumbersFromKAIN == null ? null : pCaseNumbersFromKAIN.size()});

        if (pKainCaseNumbers != null && !pKainCaseNumbers.isEmpty()) {
            pCaseNumbersFromKAIN = pKainCaseNumbers.keySet();
            if(pCaseNumbersFromKAIN != null && !pCaseNumbersFromKAIN.isEmpty()) {
                Iterator<String> itFallNr = pCaseNumbersFromKAIN.iterator();
                while (itFallNr.hasNext()) {
                    final String kainFallNr = itFallNr.next();
                    if (kainFallNr != null) {
                        final SapCase sapCase = new SapCase();
                        sapCase.setInstitution(pInstitution);
                        sapCase.setFallnr(kainFallNr);
                        importableCaseNumbersLst.add(sapCase);
                    }
                }
            }
        }
        return importableCaseNumbersLst;
    }

    /**
     * Load hospital case status
     *
     * @param pSapCase Wk Case
     * @return case found?
     * @throws JCoException Exception
     */
    public boolean loadCaseStatus(final SapCase pSapCase) throws JCoException {
        if (pSapCase == null) {
            throw new IllegalArgumentException("SapCase is null!");
        }
        JCoFunction function = createFunction("ZLBH_GET_WKSTATUS");
        if (function == null) {
            LOG.log(Level.INFO, "Method to get case status is not available");
            return false;
        }
        JCoParameterList parameter = function.getImportParameterList();
        parameter.setValue("EINRI", pSapCase.getInstitution());
        parameter.setValue("FALNR", pSapCase.getFallnr());
        if (!callFunction(function)) {
            LOG.log(Level.WARNING, "Hospital case not found: {0}", pSapCase.getCaseKey());
            return false;
        }

        JCoParameterList params = function.getExportParameterList();
        //XLEADINGCASE
        String val = params.getString("XLEADINGCASE");
        if (val != null && "X".equals(val)) {
            pSapCase.setState(SapCase.getSTATE_LEADING());
            pSapCase.setIsLeading(true);
        }
        //XRETURNCASE
        val = params.getString("XRETURNCASE");
        if (val != null && "X".equals(val)) {
            pSapCase.setState(SapCase.getSTATE_WK());
            pSapCase.setIsWK(true);
        }
        //LEADINGCASENR
        val = params.getString("LEADINGCASENR");
        pSapCase.setFallnrLeading(checkSapFallNr(val));
        //Fallnummern

        JCoTable aTable = function.getTableParameterList().getTable("FAELLE");
        if (aTable != null) {
            for (int i = 0, n = aTable.getNumRows(); i < n; i++) {
                aTable.setRow(i);
                val = aTable.getString(2);
                final String l = aTable.getString(4);
                if (!"X".equals(l)) {
                    pSapCase.addWkCase(val);
                }
            }
        }
        return true;
        //return pWkCase;
    }

    private String getIkz(final String pInstitution, final String pFallNr) throws JCoException {
        final String methodName = sapProperties.getIKZMethod();
        if (methodName == null || methodName.trim().isEmpty()) {
            LOG.log(Level.WARNING, "IKZ_METHOD is not defined in properties file!");
            return "";
        }
        final JCoFunction function = createFunction(methodName);
        //LOG.log(Level.INFO, "UseIKZMethod: " + sapProperties.useIKZMethod());
        //LOG.log(Level.INFO, "IKZMethod: " + sapProperties.getIKZMethod());
        if (function == null) {
            LOG.log(Level.SEVERE, "Funktion {0} konnte nicht ermittelt werden", sapProperties.getIKZMethod());
            return "";
        }
        LOG.log(Level.FINER, "IKZFunction: {0}", sapProperties.getIKZMethod());
        JCoParameterList parameter = function.getImportParameterList();
        parameter.setValue("SS_EINRI", pInstitution);
        parameter.setValue("SS_FALNR", pFallNr);
        if (!callFunction(function)) {
            LOG.log(Level.SEVERE, "Ausführung der Methode {0} war nicht erfolgreich", sapProperties.getIKZMethod());
            return "";
        }
        final String ikz = function.getExportParameterList().getString("SS_IKNR");
        LOG.log(Level.FINER, "Gelesene IKZ mittels IKZ_METHOD: {0}", ikz);

        return toStr(ikz);
    }

    /**
     * Load hospital case data from SAP
     *
     * @param pSapCase SAP Case
     * @param pKainCaseKeys whatever, some keys
     * @return SAP Case
     * @throws JCoException Exception
     * @throws NoSuchAlgorithmException Exception
     */
    public boolean loadCaseData(final SapCase pSapCase, final Set<String> pKainCaseKeys) throws JCoException, NoSuchAlgorithmException {
        if (pSapCase == null) {
            throw new IllegalArgumentException("SapCase is null!");
        }
        //Map<String, String> moveFA, movePF;
        //Map<String, Date> moveDt, moveZt;

        JCoFunction function = createFunction("ZLBH_GET_WKDATEN");
        if (function == null) {
            LOG.log(Level.INFO, "Method to get case data is not available");
            return false;
        }
        JCoParameterList parameter = function.getImportParameterList();
        parameter.setValue("EINRI", pSapCase.getInstitution());
        parameter.setValue("FALNR", pSapCase.getFallnr());
        if (!callFunction(function)) {
            LOG.log(Level.WARNING, "Hospital case not found: {0}", pSapCase.getCaseKey());
            return false;
        }

        String ikz;
        if (sapProperties.useIKZMethod()) {
            ikz = getIkz(pSapCase.getInstitution(), pSapCase.getFallnr());
        } else {
            LOG.log(Level.FINER, "IKZFunction: E_INSTNR");

            ikz = toStr(function.getExportParameterList().getString("E_INSTNR"));
            if (!ikz.isEmpty()) {
                LOG.log(Level.FINER, "Gelesene IKZ mittels E_INSTNR: {0}", ikz);
                char c;
                while ((c = ikz.charAt(0)) == '0') {
                    ikz = ikz.substring(1);
                }
            }
        }
        ikz = toStr(ikz);
        if (!ikz.isEmpty()) {
            LOG.log(Level.FINER, "Ermittelte IKZ für den Fall {0}: {1}", new Object[]{pSapCase.getCaseKey(), ikz});
            pSapCase.setIkz(ikz);
        } else {
            LOG.log(Level.FINER, "IKZ konnte nicht ermittelt werden. Benutze Standard-IKZ für den Fall {0}: {1}", new Object[]{pSapCase.getCaseKey(), pSapCase.getIkz()});
        }

        pSapCase.setErbringungsart(0);

        //Erbringungsart
        if (sapProperties.getSSTVersion() >= 4) {
            try {
                LOG.log(Level.FINER, "ZUSKEY-Function: E_NZUSKEY");
                final int erbringungsart = toErbringungsart(function.getExportParameterList().getValue("E_NZUSKEY"));
                pSapCase.setErbringungsart(erbringungsart);
            } catch (Exception ex) {
                pSapCase.setErbringungsart(0);
                LOG.log(Level.SEVERE, "Zusatz-Key Erbringungsart nicht vorhanden", ex);
            }
        }

        //Rechnungsdatum
        final java.sql.Date billingDate = readSAPBillingDateFrom301(function, pSapCase.getFallnr(), pSapCase.getIkz());
        if(billingDate !=  null)
        {
            pSapCase.setRechnungsdatum(billingDate);
        }

        //Patientendaten
        JCoStructure structPat = function.getExportParameterList().getStructure("E_NPAT");
        SapPatientSearchResult pat = new SapPatientSearchResult();
        pat.readFromStructure(structPat);

        //Falldaten
        JCoStructure structCase = function.getExportParameterList().getStructure("E_NFAL");
        pSapCase.readFromCaseStructur(structCase);
        SapPatientDetailResult patDetails = new SapPatientDetailResult();
        patDetails.readFromStructure(structCase);
        //pWkCase.patDetails = patDetails;

        //Aufnahmeanlass
        String aufnahmeanlass = function.getExportParameterList().getString("E_ADM_OCCASION");
        pSapCase.setAusnahmeanlassSap(aufnahmeanlass);
        pSapCase.setAufnahmeanlass(getCpAufnahmeanlass(aufnahmeanlass));
        //pWkCase.m_sapdrg = getDRG(pWkCase.mIkz, pWkCase.mFallNr, true, null, mConnection);

        //Fallreferenzen
        JCoTable tabRefs = function.getTableParameterList().getTable("E_NFFZ_TAB");
        JCoTable tabRefTexts = null;
        //String fnr1, fnr2;
        //ReferenzCase refCase;
        for (int i = 0, n = tabRefs.getNumRows(); i < n; i++) {
            tabRefs.setRow(i);
            ReferenzCase refCase = new ReferenzCase(pSapCase.getFallnr());
            String fnr1 = tabRefs.getString("FALN1");
            String fnr2 = tabRefs.getString("FALN2");
            if (fnr2.endsWith(pSapCase.getFallnr())) {
                refCase.setRefFallnr(fnr1);
                refCase.setRefType(tabRefs.getString("REFA1"));
                refCase.setCaseType(tabRefs.getString("REFA2"));
            } else if (fnr1.endsWith(pSapCase.getFallnr())) {
                refCase.setRefFallnr(fnr2);
                refCase.setRefType(tabRefs.getString("REFA2"));
                refCase.setCaseType(tabRefs.getString("REFA1"));
            } else {
                refCase.setRefFallnr(fnr2);
                refCase.setRefType("-U-");
                refCase.setCaseType("-U-");
            }
            if (refCase.getRefType().length() > 0) {
                if (tabRefTexts == null) {
                    tabRefTexts = function.getTableParameterList().getTable("E_TN15U_TAB");
                }
                String rft;
                for (int j = 0, m = tabRefTexts.getNumRows(); j < m; j++) {
                    tabRefTexts.setRow(j);
                    rft = tabRefTexts.getString("REFTA");
                    if (rft.equals(refCase.getRefType())) {
                        refCase.setRefTypeText(tabRefTexts.getString("REFTX"));
                    }
                    if (rft.equals(refCase.getCaseType())) {
                        refCase.setCaseTypeText(tabRefTexts.getString("REFTX"));
                    }
                }
            }
            pSapCase.addRefCase(refCase);
            pSapCase.setRefCase(refCase.getRefFallnr());
            pSapCase.setRefCaseType(refCase.getCaseType());
        }

        //Fallart; Entscheidung, ob DRG oder PSY-Fall
        checkForPsychCase(pSapCase);
        getSAPDateForStateChange(pSapCase);

        //getSAPDateForStateChange(pWkCase);
        //Entgelte
        JCoTable tabEntg = function.getTableParameterList().getTable("E_P21_ENT");
        int n = tabEntg.getNumRows();
        //pSapCase.getFees() = new ArrayList<>((n >= 0) ? n : 0);
        for (int i = 0; i < n; i++) {
            tabEntg.setRow(i);
            CaseEntg entg = new CaseEntg(pSapCase.getFallnr());
            entg.setEntga(tabEntg.getString("ENTGA"));
            entg.setBetrag(tabEntg.getString("BETRAG"));
            if (entg.getBetrag() != null) {
                entg.setBetrag(entg.getBetrag().replace(",", "."));
            } else {
                entg.setBetrag("0");
            }
            entg.setAnzahl(tabEntg.getString("ANZAHL"));
            if (entg.getAnzahl() != null) {
                entg.setAnzahl(entg.getAnzahl().replace(",", "."));
            } else {
                entg.setAnzahl("0");
            }
            pSapCase.addEntgelt(entg);
        }

        //Bewegungen
        JCoTable tabMov = function.getTableParameterList().getTable("E_NBEW_TAB");
        ArrayList<SapMovementSearchResult> movementList = new ArrayList<>();
        int movSz = tabMov.getNumRows();
        final HashMap<String, String> moveFA = new HashMap<>(movSz);
        final HashMap<String, String> movePF = new HashMap<>(movSz);
        final HashMap<String, Date> moveDt = new HashMap<>(movSz);
        final HashMap<String, Date> moveZt = new HashMap<>(movSz);
        for (int i = 0, m = movSz; i < m; i++) {
            SapMovementSearchResult move = new SapMovementSearchResult();
            tabMov.setRow(i);
            move.readFromStructurTable(tabMov);
           /*
            * LUr 27.01.2021: Zeile 907-909 auskommentiert, da der Aufnahmeanlass mit einem falschen Wert belegt wird,
            * der eigentlich für das Feld Srting5 in der Tabelle TCase gedacht ist.
            */
//            if ("1".equals(move.getBewty())) {
//                pSapCase.setAufnahmeanlass(move.getBwart());
//            }
            movementList.add(move);
            moveFA.put(move.getLfdnr(), move.getOrgfa());
            movePF.put(move.getLfdnr(), move.getOrgpf());
            moveDt.put(move.getLfdnr(), move.getBwidt());
            moveZt.put(move.getLfdnr(), move.getBwizt());
        }
        Collections.sort(movementList, mMovementDateComp);

        //Diagnosen
        JCoTable diagnosen = function.getTableParameterList().getTable("E_NDIA_TAB");
        ArrayList<SapDiagnosisSearchResult> diagnosisList = new ArrayList<>();
        for (int i = 0, m = diagnosen.getNumRows(); i < m; i++) {
            SapDiagnosisSearchResult diag = new SapDiagnosisSearchResult();
            diagnosen.setRow(i);
            diag.readFromStructurTable(diagnosen);
            diagnosisList.add(diag);
            pSapCase.addPrimSekDiag(diag);
        }

        //Prozeduren
        JCoTable prozeduren = function.getTableParameterList().getTable("E_NICP_TAB");
        ArrayList<SapProcedureSearchResult> procedureList = new ArrayList<>();
        //Object movValFA, movValPF;
        for (int i = 0, m = prozeduren.getNumRows(); i < m; i++) {
            SapProcedureSearchResult proc = new SapProcedureSearchResult();
            prozeduren.setRow(i);
            proc.readFromStructurTable(prozeduren);
            procedureList.add(proc);
            if (proc.getDeptou() == null || proc.getDeptou().length() <= 1) {
                String movValFA = moveFA.get(proc.getMovemntSeqno());
                if (movValFA != null) {
                    proc.setDeptou(movValFA);
                }
            }
            if (proc.getPerfou() == null || proc.getPerfou().length() <= 1) {
                String movValPF = movePF.get(proc.getMovemntSeqno());
                if (movValPF != null) {
                    proc.setPerfou(movValPF);
                }
            }
            if (proc.getBegindate() == null) {
                Date dt;
                dt = moveDt.get(proc.getMovemntSeqno());
                if (dt != null) {
                    proc.setBegindate(dt);
                }
                dt = moveZt.get(proc.getMovemntSeqno());
                if (dt != null) {
                    proc.setBegintime(dt);
                }
            }
        }

        //List<SapNleiSearchResult> nleis;
        //nleis = getSAPCaseNLEIs(pSapCase.fallnr, pSapCase.institution);
        //LOGGER.info(serialize(pWkCase));
        pSapCase.setData(this, pat, patDetails, movementList, diagnosisList, procedureList, pKainCaseKeys /*, nleis */);

        final Map<String, SapExportResult> fiDataMap = getFiData(pSapCase.getInstitution(), pSapCase.getFallnr());
        pSapCase.setFiDaten(fiDataMap.get(MAP_KEY_FI_CASE_STATUS));
        pSapCase.setFiOpenCaseStatus(fiDataMap.get(MAP_KEY_FI_OPEN_ITEM_STATUS));
        //pSapCase.setFi_daten(getFICaseStatus(pSapCase.getInstitution(), pSapCase.getFallnr()));
        //pSapCase.setFi_open_case_status(getFIOpenItemStatus(pSapCase.getInstitution(), pSapCase.getFallnr()));
        pSapCase.setLabordaten(getLabordaten(pSapCase.getInstitution(), pSapCase.getIkz(), pSapCase.getFallnr()));

        return true;
    }

    /**
     * FI Case Status
     *
     * @param pInstitution Einrichtung
     * @param pFallnr Fallnr.
     * @return Results
     * @throws JCoException Exception
     */
    public SapExportResult getFICaseStatus(final String pInstitution, final String pFallnr) throws JCoException {
        if (!sapProperties.importSAPCaseFI()) {
            LOG.log(Level.INFO, "FI module is disabled in the settings");
            return new SapExportResult();
        }
        SapExportResult res = new SapExportResult();
        res = getFIStatus(pInstitution, pFallnr, res);
        return res;
    }

//    /**
//     * Retrieves a list of hospital cases with KAIN messages within in the given
//     * change date.
//     *
//     * @param pInstitution Institution
//     * @param pChangeDate Last change date
//     * @return List of case numbers
//     * @throws JCoException Exception
//     */
//    public Set<String> getNewKainCaseNumbers(final String pInstitution, final Date pChangeDate) throws JCoException {
//        return getNewKainCaseNumbers(pInstitution, pChangeDate, new HashSet<>());
//    }    
    /**
     * Retrieves a list of hospital cases with KAIN messages within in the given
     * change date.
     *
     * @param pInstitution IerFilter case numbers that have to be considered
     * here
     * @param pChangeDate change date
     * @return List of case numbers
     * @throws JCoException Exception
     */
    public Map<String, String> getNewKainCaseNumbers(final String pInstitution, final Date pChangeDate) throws JCoException {
        final Map<String, String> results = new HashMap<>();
        if (!sapProperties.importKAIN()) {
            LOG.log(Level.INFO, "KAIN module is disabled in the settings");
            return results;
        }
        final String methodName = "ZLBH_P301_GETDELTA";
        JCoFunction function = createFunction(methodName);
        if (function == null) {
            LOG.log(Level.INFO, "Method to get KAIN messages is not available");
            return results;
        }
        JCoParameterList parameter = function.getImportParameterList();
        parameter.setValue("I_EINRI", pInstitution);
        parameter.setValue("I_VONDATUM", getDatumOhneUhrzeit(pChangeDate));
        parameter.setValue("I_VONZEIT", "000000");
        callFunction(function);
        JCoTable tabRefs = function.getTableParameterList().getTable("ET_NC301S");
        int n = tabRefs.getNumRows();

        //String fnr, einri, kostr, strCaseP301;
        for (int i = 0; i < n; i++) {
            tabRefs.setRow(i);
            String fnr = toStr(tabRefs.getString("FALNR"));
            String einri = toStr(tabRefs.getString("EINRI"));
            String kostr = toStr(tabRefs.getString("KOSTR"));
            //extRefNumber = tabRefs.getString("LF301");
            if (fnr != null && einri != null && kostr != null) {
                fnr = checkSapFallNr(fnr);
                String strCaseP301 = fnr.trim() + "_" + einri.trim() + "_" + kostr.trim();
                results.put(fnr.trim(), strCaseP301);
            }
        }
        return results;
    }

    /**
     * Gets KAIN message for a hospital case
     *
     * @param pInstitution Institution
     * @param pFallnr Case number
     * @param pInsurIK Insurance
     * @param pIkz Hospital identifier
     * @return List of KAIN details
     * @throws JCoException Exception
     */
    public List<SapKainDetailSearchResult> getKainMessagesForCase(final String pInstitution, final String pFallnr,
            final String pInsurIK, final String pIkz) throws JCoException {
        if (!sapProperties.importKAIN()) {
            LOG.log(Level.INFO, "KAIN module is disabled in the settings");
            return new ArrayList<>();
        }
        LOG.log(Level.FINE, "Import KAIN-Nachrichten f�r Fallnummer {0} beginnt", pFallnr);
        List<SapKainDetailSearchResult> kainDetailsList = new ArrayList<>();
        Map<String, SapKainDetailSearchResult> kainMessages = new HashMap<>();
        List<Sap301MessageInfo> lstP301KainMsgInfo = getAllKainMessagesForCase(pInstitution, pFallnr, pInsurIK);
        if (lstP301KainMsgInfo != null && !lstP301KainMsgInfo.isEmpty()) {
            LOG.log(Level.FINE, "alNumberKAINMessages.size() = {0}", lstP301KainMsgInfo.size());
            for (int i = 0, n = lstP301KainMsgInfo.size(); i < n; i++) {
                Sap301MessageInfo kainMsgInfo = lstP301KainMsgInfo.get(i);
                if (kainMsgInfo != null) {
                    String messageNumber = kainMsgInfo.getExtRefNumber();
                    Date kainReceivingDate = kainMsgInfo.getKainImportDate();
                    LOG.log(Level.FINE, "messageNumber = {0}", messageNumber);
                    if (messageNumber != null) {
                        SapKainDetailSearchResult objKAINDetails = getContentOfKainMessage(messageNumber, pFallnr, pInstitution, pInsurIK);
                        //objKAINDetails.setIkz(pIkz);
                        objKAINDetails.setIkz(objKAINDetails.getReceiver());
                        objKAINDetails.setReceivingDate(kainReceivingDate);
                        kainMessages.put(messageNumber, objKAINDetails);
                    }
                }
            }
        }
        if (!kainMessages.isEmpty()) {
            LOG.log(Level.FINE, "kainMessages.size() = {0}", kainMessages.size());
            List<SapKainDetailSearchResult> alKainM = new ArrayList<>(kainMessages.values());
            if (!alKainM.isEmpty()) {
                LOG.log(Level.FINE, "alKainM.size() = {0}", alKainM.size());
                for (int i = 0, n = alKainM.size(); i < n; i++) {
                    SapKainDetailSearchResult objKainM = alKainM.get(i);
                    LOG.log(Level.FINE, "Lf301 = {0}", objKainM.getLf301());
                    kainDetailsList.add(objKainM);
                }
            }
        }
        return kainDetailsList;
    }

    /**
     * read FI data for specific hospital case
     *
     * @param pInstitution String aus dem Feld Institution.
     * @param pFallNr String aus dem Feld SAP- Fall-Nr.
     * @return List of FI data
     * @throws com.sap.conn.jco.JCoException Exception
     */
    public Map<String, SapExportResult> getFiData(final String pInstitution, final String pFallNr) throws JCoException {
        final Map<String, SapExportResult> res = new HashMap<>();
        res.put(MAP_KEY_FI_CASE_STATUS, getFICaseStatus(pInstitution, pFallNr));
        res.put(MAP_KEY_FI_OPEN_ITEM_STATUS, getFIOpenItemStatus(pInstitution, pFallNr));
        return res;
    }

    /**
     * getAllKAINMessagesForCase liest die KAIN-Nachrichten zu einem Fall
     * ein<br>
     *
     * @param pInstitution String aus dem Feld Institution.
     * @param pFallNr String aus dem Feld SAP- Fall-Nr.
     * @param pInsKOSTR Kostenträger des Versicherten
     * @return Liste mit KAIN-Nachrichten eines Falles
     * @throws com.sap.conn.jco.JCoException Exception
     */
    public List<Sap301MessageInfo> getAllKainMessagesForCase(final String pInstitution, final String pFallNr, final String pInsKOSTR) throws JCoException {
        if (!sapProperties.importKAIN()) {
            LOG.log(Level.INFO, "KAIN module is disabled in the settings");
            return new ArrayList<>();
        }
        LOG.log(Level.FINE, "pInstitution={0}, pFallNr={1}, pInsKOSTR={2}", new Object[]{pInstitution, pFallNr, pInsKOSTR});

        List<Sap301MessageInfo> alKAINResults = null;
        String lFallnr = pFallNr;

        //Vervollständigung der Fallnummer auf 10 Stellen
        lFallnr = fillFallnr(lFallnr);
//        if (lFallnr.length() < 10) {
//            for (int i = lFallnr.length(); i < 10; i++) {
//                lFallnr = "0" + lFallnr;
//            }
//        }

        final String methodName = "ZLBH_KAIN_GETLIST";
        JCoFunction function = createFunction(methodName);
        if (function == null) {
            LOG.log(Level.INFO, "Method to get KAIN messages is not available");
            return new ArrayList<>();
        }

        JCoParameterList parameter = function.getImportParameterList();
        parameter.setValue("I_EINRI", pInstitution);
        parameter.setValue("I_FALNR", lFallnr);
        parameter.setValue("I_KOSTR", pInsKOSTR);
        LOG.log(Level.FINE, "call method {0} with params I_EINRI = {1}, I_FALNR = {2}, I_KOSTR = {3}", new Object[]{methodName, pInstitution, lFallnr, pInsKOSTR});
        callFunction(function);
        JCoTable tblNC301S = function.getTableParameterList().getTable("E_KAIN_TAB");
        if (tblNC301S != null && !tblNC301S.isEmpty()) {
            int n = tblNC301S.getNumRows();
            LOG.log(Level.FINE, "tblNC301S.getNumRows() = {0}", n);
            alKAINResults = new ArrayList<>(n);
            //String fnr, einri, insik, extRefNumber;
            for (int i = 0; i < n; i++) {
                tblNC301S.setRow(i);
                String fnr = tblNC301S.getString("FALNR");
                String einri = tblNC301S.getString("EINRI");
                String insik = tblNC301S.getString("KOSTR");
                String extRefNumber = tblNC301S.getString("LF301");
                Date kainImportDate = combineDate(tblNC301S.getDate("ERDAT"), tblNC301S.getDate("ERTIM"));
                Sap301MessageInfo kainMsgInfo = new Sap301MessageInfo(extRefNumber, kainImportDate);
                alKAINResults.add(kainMsgInfo);
            }
        }

        return alKAINResults;
    }

    /**
     * Determines the current insurance for a hospital case
     *
     * @param pInstitution Institution
     * @param pFallNr Case number
     * @return Insurance
     * @throws JCoException Exception
     */
    public Insurance getCurrentInsurance(final String pInstitution, final String pFallNr) throws JCoException {
        List<Insurance> insurances = getInsurances(pInstitution, pFallNr);
        Insurance ins = getCurrentInsurance(insurances);
        return ins;
    }

    private static Insurance getCurrentInsurance(final List<Insurance> pInsurances) {
        if (pInsurances == null) {
            LOG.log(Level.FINE, "Keine Versicherung vorhanden!");
            return null;
        }
        Collections.sort(pInsurances, new Comparator<Insurance>() {
            @Override
            public int compare(Insurance insuranceLhs, Insurance insuranceRhs) {
                return insuranceLhs.getRangf().compareTo(insuranceRhs.getRangf());
            }
        });
        int lg = pInsurances.size();
        if (lg > 0) {
            Insurance in = pInsurances.get(0);
            if (in.getRangf() != null && !"00".equals(in.getRangf())) {
                return in;
            } else if (lg > 1) {
                return pInsurances.get(1);
            }
        }
        return null;
    }

    /**
     * Ließt die Versicherungsinformationen aus dem SAP
     *
     * @param pEinrichtung String
     * @param pFallnr String
     * @return List of Insurances
     * @throws com.sap.conn.jco.JCoException Exception
     */
    private List<Insurance> getInsurances(final String pEinrichtung,
            final String pFallnr) throws JCoException {
        List<Insurance> v = getInsuranceDate(pEinrichtung, pFallnr, SAP_TABLE_NCIR, SAP_TABLE_NPIR);
        v.addAll(getInsuranceDate(pEinrichtung, pFallnr, SAP_TABLE_NVVF, SAP_TABLE_NVVP));
        return v;
    }

    private List<Insurance> getInsuranceDate(
            final String pEinrichtung,
            final String pFallnr,
            final String pTableIns,
            final String pTableDetails) throws JCoException {
        List<Insurance> inces = new ArrayList<>();
        final String methodName = "RFC_READ_TABLE";
        JCoFunction function = createFunction(methodName);
        if (function == null) {
            LOG.log(Level.INFO, "Method to get insurance data is not available");
            return new ArrayList<>();
        }

        JCoParameterList parameter = function.getImportParameterList();
        parameter.setValue("QUERY_TABLE", pTableIns);
        JCoTable tblOptions = function.getTableParameterList().getTable("OPTIONS");
        String where = getInsuranceWhere(pEinrichtung, pFallnr);
        if (where != null) {
            tblOptions.appendRow();
            tblOptions.setValue("TEXT", where);
            JCoTable tblFields = function.getTableParameterList().getTable("FIELDS");
            tblFields.appendRow();
            tblFields.setValue("FIELDNAME", "EINRI");
            tblFields.appendRow();
            tblFields.setValue("FIELDNAME", "FALNR");
            tblFields.appendRow();
            tblFields.setValue("FIELDNAME", "LFDNR");
            tblFields.appendRow();
            tblFields.setValue("FIELDNAME", "KOSTR");
            tblFields.appendRow();
            tblFields.setValue("FIELDNAME", "RANGF");
            tblFields.appendRow();
            tblFields.setValue("FIELDNAME", "VERNR");
            tblFields.appendRow();
            tblFields.setValue("FIELDNAME", "VBGDT");
            callFunction(function);
            JCoTable returnStructure = function.getTableParameterList().getTable("DATA");
            int n = returnStructure.getNumRows();
            if (n > 0) {
                for (int i = 0; i < n; i++) {
                    returnStructure.setRow(i);
                    String res = toStr(returnStructure.getString("WA"));
                    Insurance ins = new Insurance();
                    ins.setEinri(res.substring(0, 4));
                    ins.setFalnr(res.substring(4, 14));
                    ins.setLfdnr(res.substring(14, 17));
                    ins.setKostr(res.substring(17, 27).trim());
                    ins.setRangf(res.substring(27, 29));
                    ins.setVernr(res.substring(29, 49));
                    ins.setVbgdt(res.substring(49, 57));
                    if (doAnonymize) {
                        ins.setVernr("ANO_" + getHash(ins.getVernr(), 16));
                    }
                    ins.setVernr(checkQuotes(ins.getVernr().trim()));
                    //getInsuranceDetails(ins, pat.PATIENTID, ins.lfdnr, mConnection, tableDetails);
                    searchInsuranceData(ins, ins.getKostr());
                    inces.add(ins);
                }
            } else {
                LOG.log(Level.FINE, "Keine Versicherungsdaten NVVF zum Fall {0}, Einrichtung {1} gefunden!", new Object[]{pFallnr, pEinrichtung});
            }
        } else {
            LOG.log(Level.INFO, "WHERE-Klausel in Versicherung ist null");
        }
        return inces;
    }

    private static String getInsuranceWhere(final String pEinrichtung, final String pFallnr) {
        int lg;
        if (pEinrichtung == null) {
            LOG.log(Level.SEVERE, "Einrichtung im Insurance-Where ist NULL");
            return null;
        }
        if (pFallnr == null) {
            LOG.log(Level.SEVERE, "Fallnummer im Insurance-Where ist NULL");
            return null;
        }
        String einrichtung = pEinrichtung;
        lg = einrichtung.length();
        if (lg < 4) {
            for (int i = lg; i < 4; i++) {
                einrichtung = "0" + einrichtung;
            }
        } else if (lg > 4) {
            LOG.log(Level.WARNING, "Einrichtung-String zu lang: {0}", einrichtung);
            LOG.log(Level.WARNING, "Einrichtung-String zu lang: {0}", einrichtung.length());
            return null;
        }
        String fallnr = pFallnr;
        lg = fallnr.length();
        if (lg < 10) {
            fallnr = fillFallnr(fallnr);
//            for (int i = lg; i < 10; i++) {
//                fallnr = "0" + fallnr;
//            }
        } else if (lg > 10) {
            LOG.log(Level.WARNING, "Fallnummern-String zu lang: {0}", fallnr);
            LOG.log(Level.WARNING, "Fallnummern-String zu lang: {0}", fallnr.length());
            return null;
        }
        return "EINRI = '" + einrichtung + "' AND FALNR = '" + fallnr + "' AND STORN = ''";
    }

    private void searchInsuranceData(final Insurance pInsurance, final String pKostentraeger) throws JCoException {
        final String methodName = "BAPI_BUSPARTNER_GETDETAIL";
        JCoFunction function = createFunction(methodName);
        if (function == null) {
            LOG.log(Level.INFO, "Method to find insurance data is not available");
            return;
        }

        JCoParameterList parameter = function.getImportParameterList();
        parameter.setValue("BPARTNERID", pKostentraeger);
        callFunction(function);
        JCoTable returnStructure = function.getTableParameterList().getTable("RETURN");
        if (returnStructure.getNumRows() > 0
                && !(returnStructure.getString("TYPE").isEmpty()
                || "S".equals(returnStructure.getString("TYPE"))
                || "W".equals(returnStructure.getString("TYPE")))) {
            for (int i = 0, n = returnStructure.getNumRows(); i < n; i++) {
                returnStructure.nextRow();
            }
        }
        JCoStructure expStructur = function.getExportParameterList().getStructure("BPARTNER_DATA");
        pInsurance.setInstituteInd(expStructur.getString("INSTITUTE_IND"));
        if (pInsurance.getInstituteInd() != null) {
            int lg = pInsurance.getInstituteInd().length();
            if (lg > 9) {
                pInsurance.setInstituteInd(pInsurance.getInstituteInd().substring(lg - 9, lg));
            }
        }
        pInsurance.setLastNameBp(expStructur.getString("LAST_NAME_BP"));
    }

    /**
     * getKAINMeassegesForCase liest den INhalt der KAIN-Nachrichten zu einem
     * Fall aus.<br>
     *
     * @param pStMessageNumber String Nachrichtennummer, deren Details aus SAP
     * abgerufen werden soll.
     * @param pFallnr String Fallnummer, auf die sich diese Nachricht beszieht.
     * @param pInstitution String Einrichtungsnummer zu dem Fall, auf den sich
     * diese Nachricht bezieht.
     * @param pKostrNumber Cost unit number
     * @return Objekt vom Typ SAPKAINDetailSearchResult mit der KAIN-Nachricht
     * @throws com.sap.conn.jco.JCoException Exception
     */
    protected SapKainDetailSearchResult getContentOfKainMessage(final String pStMessageNumber, final String pFallnr, final String pInstitution,
            final String pKostrNumber) throws JCoException {
        SapKainDetailSearchResult skdKAINResult = null;
        SapKainPvvSearchResult skpPVVResult;
        SapKainPvtSearchResult skpPVTResult;
        List<SapKainPvvSearchResult> alPVVResults = new ArrayList<>(0);

        final String methodName = "ZLBH_KAIN_GETDETAIL";
        JCoFunction function = createFunction(methodName);
        if (function == null) {
            LOG.log(Level.INFO, "Method to get KAIN details is not available");
            return new SapKainDetailSearchResult();
        }

        JCoParameterList parameter = function.getImportParameterList();
        parameter.setValue("I_LF301", pStMessageNumber);
        callFunction(function);
        JCoParameterList plKAINDetails = function.getExportParameterList();
        if (plKAINDetails != null) {
            skdKAINResult = new SapKainDetailSearchResult(doAnonymize);
            skdKAINResult.setCaseIndicator(pFallnr, pInstitution, pStMessageNumber, pKostrNumber);
            skdKAINResult.readFromParameterList(plKAINDetails);
            JCoTable taPVVDetails = function.getTableParameterList().getTable("ET_PVV");
            if (taPVVDetails != null && !taPVVDetails.isEmpty()) {
                int n = taPVVDetails.getNumRows();

                for (int i = 0; i < n; i++) {
                    taPVVDetails.setRow(i);
                    skpPVVResult = new SapKainPvvSearchResult();
                    skpPVVResult.readFromTable(taPVVDetails);
                    alPVVResults.add(skpPVVResult);
                }
            }
            JCoTable taPVTDetails = function.getTableParameterList().getTable("ET_PVT");
            if (taPVTDetails != null && !taPVTDetails.isEmpty()) {
                int n = taPVTDetails.getNumRows();

                for (int i = 0; i < n; i++) {
                    taPVTDetails.setRow(i);
                    skpPVTResult = new SapKainPvtSearchResult();
                    skpPVTResult.readFromTable(taPVTDetails);
                    for (int j = 0, m = alPVVResults.size(); j < m; j++) {
                        SapKainPvvSearchResult tmpPVV = alPVVResults.get(j);
                        if (tmpPVV != null && tmpPVV.getPvvrowid().equals(skpPVTResult.getPvvrowid())) {
                            tmpPVV.addPVTElement(skpPVTResult);
                        }
                    }
                }
            }
        }
        if (skdKAINResult != null) {
            skdKAINResult.addPVVElement(alPVVResults);
        }

        return skdKAINResult;
    }

    /**
     * Import der Laborwerte zu einem Fall aus dem SAP
     *
     * @param pInstitution Einrichtung
     * @param pIkz IKZ
     * @param pFallnr Fallnr.
     * @return Map with Labor documents
     * @throws com.sap.conn.jco.JCoException Exception
     */
    public Map<String, RmlLaborDocument> getLabordaten(final String pInstitution, final String pIkz, String pFallnr) throws JCoException {
        if (!sapProperties.importLabor()) {
            LOG.log(Level.INFO, "Labor module is disabled in the settings");
            return new HashMap<>();
        }
        if (pFallnr.startsWith("0")) {
            pFallnr = "" + Integer.parseInt(pFallnr);
        }
        final String methodName = "ZLBH_GET_LABORDATEN";
        JCoFunction function = createFunction(methodName);
        if (function == null) {
            LOG.log(Level.INFO, "Method to get labor data is not available");
            return new HashMap<>();
        }
        JCoParameterList parameter = function.getImportParameterList();
        parameter.setValue("EINRI", pInstitution);
        parameter.setValue("FALNR", pFallnr);
        if (getLaborValueStatusKey().length() != 0 && getLaborValueTypeKey().length() != 0) {
            parameter.setValue(getLaborValueStatusKey(), "I_N2VSTATUS");
            parameter.setValue(getLaborValueTypeKey(), "I_N2VALUETYPEN");
        }
        if (!callFunction(function)) {
            LOG.log(Level.INFO, "Labor module is not available (function not found)");
            return new HashMap<>();
        }
        Map<String, RmlLaborDocument> hm = readSAPLaborDocuments(function, pFallnr, pIkz);
        readSAPLaborValues(function, pInstitution, pFallnr, pIkz, hm);
        return hm;
    }

    /**
     * Gibt den Status von Laborwerten zurück, der bei der Abfrage im SAP
     * berücksichtigt werden soll
     *
     * @return String
     */
    public String getLaborValueStatusKey() {
        return sapProperties.getLabValStatus();
    }

    /**
     * Gibt den Typ von Laborwerten zurück, der bei der Abfrage im SAP
     * berücksichtigt werden soll
     *
     * @return String
     */
    public String getLaborValueTypeKey() {
        return sapProperties.getLabValType();
    }

    /**
     * Ermittelt die Liste der zu importierenden Labordokumente
     *
     * @param pFunction JCO Function
     * @param pFallnr Fall-Nr.
     * @param fallid Fall-ID
     * @param pIkz IKZ
     * @return Map with Labor documents
     */
    private static Map<String, RmlLaborDocument> readSAPLaborDocuments(final JCoFunction pFunction, final String pFallnr, final String pIkz) {
        final Map<String, RmlLaborDocument> hm = new HashMap<>();
        JCoTable tabRefs = pFunction.getTableParameterList().getTable("E_NDOC");
        int n = tabRefs.getNumRows();
        LOG.log(Level.INFO, "Anzahl der Ergebnisse Labor-Dokumente für Fallnr {0}, IK {1}: {2}", new Object[]{pFallnr, pIkz, n});
        //SQLConnection c = dbConnection.getSQLConnection();
        //ArrayList<RmlLaborDocument> oldRes = getSAPLaborOldDocuments(c);
        //String mandt, dokar, doknr, dokvr, doktl, key;
        //Date dat, tim, dtm, docDate;
        //RmlLaborDocument labd;
        for (int i = 0; i < n; i++) {
            tabRefs.setRow(i);
            String mandt = tabRefs.getString("MANDT");
            String dokar = tabRefs.getString("DOKAR");
            String doknr = tabRefs.getString("DOKNR");
            String dokvr = tabRefs.getString("DOKVR");
            String doktl = tabRefs.getString("DOKTL");
            String key = mandt + "_" + dokar + "_" + doknr + "_" + dokvr + "_" + doktl;
            Date dat = tabRefs.getDate("UPDAT");
            Date tim = tabRefs.getTime("UPTIM");
            Date dtm = combineDate(dat, tim);
            dat = tabRefs.getDate("DODAT");
            tim = tabRefs.getTime("DOTIM");
            Date docDate = combineDate(dat, tim);
//      labd = findSAPLaborDocument(oldRes, key, dtm, docDate);
            RmlLaborDocument labd = new RmlLaborDocument();

            labd.setLabdKey(key);
            //labd.labd_value = val;
            labd.setLabdUpdate(dtm);
            labd.setLabdDocDate(docDate);

            hm.put(key, labd);
        }
        return hm;
    }

    /**
     * Liest die Laborwerte zu den SAP Labordokumenten aus und speichert diese
     * in der Datenbank.
     *
     * @param pFunction JCO Function
     * @param pFallnr Fallnr.
     * @param fallid Fall-ID
     * @param pIkz IKZ
     * @param userName Username
     * @param pHm Hashmap with Labor documents
     * @return Labor values
     */
    private int readSAPLaborValues(final JCoFunction pFunction, final String pEinrichtung,
            final String pFallnr, final String pIkz, final Map<String, RmlLaborDocument> pHm) throws JCoException {
        JCoTable tabRefs = pFunction.getTableParameterList().getTable("E_N2LABOR001");
        RmlLabor lab;
        int n = tabRefs.getNumRows();
        //Date dt, dtm;
        RmlLaborDocument labd = null;
        //String mandt, dokar, doknr, dokvr, doktl, key, tarif, talst, lastKey = null;
        String lastKey = null;
        for (int i = 0; i < n; i++) {
            tabRefs.setRow(i);
            String mandt = tabRefs.getString("MANDT");
            String dokar = tabRefs.getString("DOKAR");
            String doknr = tabRefs.getString("DOKNR");
            String dokvr = tabRefs.getString("DOKVR");
            String doktl = tabRefs.getString("DOKTL");
            String key = mandt + "_" + dokar + "_" + doknr + "_" + dokvr + "_" + doktl;

            if (lastKey == null || !lastKey.equals(key)) {
                labd = pHm.get(key);
                lastKey = key;
            }
            if (labd != null) {
                lab = new RmlLabor();
                //lab.fallid = fallid;
                lab.setLabvText(tabRefs.getString("N2VALUE"));
                try {
                    lab.setLabvValue(Double.parseDouble(lab.getLabvText()));
                } catch (NumberFormatException ex) {
                    lab.setLabvValue(0D);
                }
                lab.setLabvDate(labd.getLabdDocDate());
                lab.setLabvRange(tabRefs.getString("N2NORMAL"));
                Date dt = tabRefs.getDate("N2DATE");
                Date dtm = tabRefs.getTime("N2TIME");
                dtm = combineDate(dt, dtm);
                if (dtm != null) {
                    lab.setLabvAnalysisdtm(new java.sql.Date(dtm.getTime()));
                }
                lab.setLabvAnalysis(tabRefs.getString("N2ABNORMAL"));
                lab.setLabvDescr(tabRefs.getString("N2KATTEXT"));
                lab.setLabvUnit(tabRefs.getString("N2UNIT"));
                String tarif = tabRefs.getString("N2KATID");
                String talst = tabRefs.getString("N2LEISTID");
                lab.setLabvGroup(this.getSAPLaborTarifGruppe(mandt, pEinrichtung, tarif, talst));
                lab.setKisExternKey(key);
                labd.setLab(lab);
            } else {
                LOG.log(Level.WARNING, "Labor-Dokument nicht gefunden {0}", key);
            }
        }
        return MESSAGE_STATE_FINISHED;
    }

    /**
     * Gibt zu einer Leistung die Laborgruppe zurück
     *
     * @param pMandant Mandant
     * @param pEinrichtung Institution
     * @param pTarif Tarif
     * @param pTalst Talst
     * @return Tarifgruppe
     */
    private String getSAPLaborTarifGruppe(final String pMandant, final String pEinrichtung, final String pTarif, final String pTalst) throws JCoException {
        final Map<String, String> hm = getSAPLaborTarifs();
        final String key = pMandant + "_" + pEinrichtung + "_" + pTarif + "_" + pTalst;
        String val = hm.get(key);
        if (val == null) {
            LOG.log(Level.SEVERE, "Keine Labor-Leistung gefunden zu {0}", key);
            val = "";
        } else {
            val = getSAPLaborGruppe(pMandant, pEinrichtung, val);
        }
        return val;
    }

    /**
     * HashMap mit allen SAP Leistungen
     *
     * @return Labor-Tarife
     */
    private Map<String, String> getSAPLaborTarifs() throws JCoException {
        if (mHmLabTarif == null) {
            initSAPLaborCustomizing();
        }
        return mHmLabTarif;
    }

    /**
     * Lädt die Labor Customizing Werte aus SAP
     *
     */
    private void initSAPLaborCustomizing() throws JCoException {
//      JCO.Function function = null;
//      JCO.Table tabRefs;
//      IFunctionTemplate ft = mRepository.getFunctionTemplate(Z_RFC_LABOR_GET_LABORCUSTOMIZING);
        final String methodName = "ZLBH_GET_LABORCUSTOMIZING";
        JCoFunction function = createFunction(methodName);
        if (function == null) {
            LOG.log(Level.INFO, "Method to get labor data is not available");
            return;
        }

        callFunction(function);
        JCoTable tabRefs = function.getTableParameterList().getTable("E_TN2KUM02");
        int n = tabRefs.getNumRows();
        mHmLabTarif = new HashMap<>(n);
        //String mandt, einri, tarif, talst, groupid, key, text;
        for (int i = 0; i < n; i++) {
            tabRefs.setRow(i);
            String mandt = tabRefs.getString("MANDT");
            String einri = tabRefs.getString("EINRI");
            String tarif = tabRefs.getString("TARIF");
            String talst = tabRefs.getString("TALST");
            String groupid = tabRefs.getString("GROUPID");
            String key = mandt + "_" + einri + "_" + tarif + "_" + talst;
            mHmLabTarif.put(key, groupid);
        }
        tabRefs = function.getTableParameterList().getTable("E_TN2KUM01");
        n = tabRefs.getNumRows();
        mHmLabGroup = new HashMap<>(n);
        for (int i = 0; i < n; i++) {
            tabRefs.setRow(i);
            String mandt = tabRefs.getString("MANDT");
            String einri = tabRefs.getString("EINRI");
            String groupid = tabRefs.getString("GROUPID");
            String text = tabRefs.getString("GRUPPE");
            String key = mandt + "_" + einri + "_" + groupid;
            mHmLabGroup.put(key, text);
        }
    }

    /**
     * Gibt die SAP Laborgruppe zurück
     *
     * @param pMandant Mandant
     * @param pEinrichtung Institution
     * @param pGroupId GroupId
     * @return Laborgruppe
     */
    private String getSAPLaborGruppe(String pMandant, String pEinrichtung, String pGroupId) throws JCoException {
        final Map<String, String> hm = getSAPLaborGroups();
        final String key = pMandant + "_" + pEinrichtung + "_" + pGroupId;
        String val = hm.get(key);
        if (val == null) {
            LOG.log(Level.SEVERE, "Keine Labor-Gruppe gefunden zu {0}", key);
            val = "";
        }
        return val;
    }

    /**
     * HasMap mit SAP Laborgruppen
     *
     * @return Laborgruppen
     */
    private Map<String, String> getSAPLaborGroups() throws JCoException {
        if (mHmLabGroup == null) {
            initSAPLaborCustomizing();
        }
        return mHmLabGroup;
    }

//  public SapExportResult getFICaseStatus(final String pFallnr, String pUsername, String pEinrichtung) throws JCoException {
//    SapExportResult res = new SapExportResult();
//    res = getFIStatus(pEinrichtung, pFallnr, res);
//    List<SapFiFactura> v = res.getFacturas();
////    if (v != null) {
////      SapFiFactura fact;
////      for (int i = 0, n = v.size(); i < n; i++) {
////        fact = (SapFiFactura) v.get(i);
////        if (!fact.hasChanged()) {
////          if (isDebugMode() && myLogger != null) {
////            myLogger.info("Facts speichern: " + fact.vbeln);
////          }
////          saveFactura(fallid, fact, userName, conn);
////        }
////        fact.setChanged(false);
////      }
////    }
////    res = getFIOpenItemStatus(mConnection, einrichtung, fallnr, res);
////    v = res.getOpenItems();
////    if (v != null) {
////      SAPFIOpenItem item;
////      String query;
////      for (int i = 0, n = v.size(); i < n; i++) {
////        item = (SAPFIOpenItem) v.get(i);
////        if (isDebugMode() && myLogger != null) {
////          myLogger.info("Facts speichern: " + item.toString());
////        }
////        item.fall_id = fallid;
////        if (item.id <= 0) {
////          item.id = conn.getNewSerial(SAPFIOpenItem.TABLE_NAME, 1);
////          query = item.getInsertStatement(false, userName, conn.getIsOracle());
////        } else {
////          query = item.getUpdateStatement(false, userName, conn.getIsOracle());
////        }
////        conn.getSQLConnection().performSql(query);
////        item.resetFields();
////      }
////    }
//    return res;
//  }
    /**
     * FI-Status
     *
     * @param pInstitution Einrichtung
     * @param pFallnr Fall-Nr.
     * @param res Result
     * @return Result
     * @throws JCoException Exception
     */
    public SapExportResult getFIStatus(final String pInstitution, String pFallnr, SapExportResult res) throws JCoException {
        if (!sapProperties.importSAPCaseFI()) {
            LOG.log(Level.INFO, "FI module is disabled in the settings");
            return new SapExportResult();
        }
        if (res == null) {
            res = new SapExportResult();
        }
        //use Z_ME_CASE_FI_DATA_GET_02 to retrieve a subset of Z_ME_CASE_FI_DATA_GET_01
        final String methodName2 = "ZLBH_CASE_FI_DATA_GET_02"; //Z_ME_CASE_FI_DATA_GET_01 or Z_ME_CASE_FI_DATA_GET_02?
        boolean methodName2Valid;
        JCoFunction function = null;
        try {
            //Try lightweight method Z_ME_CASE_FI_DATA_GET_02 first...
            function = createFunction(methodName2);
            methodName2Valid = function != null;
        } catch (IllegalArgumentException ex) {
            LOG.log(Level.FINEST, methodName2 + " is not available (this is just a warning)", ex);
            methodName2Valid = false;
        }
        if (!methodName2Valid) {
            //Z_ME_CASE_FI_DATA_GET_02 is not available, use fallback to Z_ME_CASE_FI_DATA_GET_01
            final String methodName1 = "ZLBH_CASE_FI_DATA_GET_01"; //Z_ME_CASE_FI_DATA_GET_01 or Z_ME_CASE_FI_DATA_GET_02?
            LOG.log(Level.FINE, "Method '" + methodName2 + "' is not available, try to use '" + methodName1 + "' instead");
            function = createFunction(methodName1);
        }

        if (function == null) {
            LOG.log(Level.INFO, "FI module is not available");
            return new SapExportResult();
        }

//      if (function == null && !Z_RFC_FI_DATA.equals(RFC_FI_DATA)) {
//        function = createFunction(RFC_FI_DATA);
//      }
        JCoParameterList parameter = function.getImportParameterList();
        parameter.setValue("EINRI", pInstitution);
        parameter.setValue("FALNR", pFallnr);
        callFunction(function);
        SapImExManager mgr = this.getSAPManager();
        //FI-Faktura
        JCoTable aTable = function.getTableParameterList().getTable("E_VBRK");
        readFacturaResults(aTable, res, mgr, false);
        //FI-Faktura (Storno)
        aTable = function.getTableParameterList().getTable("E_VBRK_STORN");
        readFacturaResults(aTable, res, mgr, true);
        //FI-Positionen
        aTable = function.getTableParameterList().getTable("E_VBRP");
        readPositionResults(aTable, res, mgr, false);
        //FI-Positionen (Storno)
        aTable = function.getTableParameterList().getTable("E_VBRP_STORN");
        readPositionResults(aTable, res, mgr, true);

        return res;
    }

    private static void readPositionResults(final JCoTable aTable, final SapExportResult res, SapImExManager mgr, final boolean isStorno) {
        if (aTable != null) {
            SapFiFactura fact = null;
            for (int i = 0, n = aTable.getNumRows(); i < n; i++) {
                aTable.setRow(i);
                String belegnr = aTable.getString("VBELN");
                if (fact == null || !fact.getVbeln().equals(belegnr)) {
                    fact = res.getFacturaForNr(belegnr);
                }
                if (fact != null) {
                    //what about pos? It is not used!
                    SapFiPosition pos = fact.addPosition(mgr.readFIPositionFromSAPTable(aTable));
                } else {
                    res.getText().append("Faktura für Position nicht gefunden: ").append(belegnr);
                }
            }
        }
    }

    private static void readFacturaResults(final JCoTable aTable, final SapExportResult res, SapImExManager mgr, final boolean isStorno) {
        if (aTable != null) {
            SapFiFactura fact;
            for (int i = 0, n = aTable.getNumRows(); i < n; i++) {
                aTable.setRow(i);
                String belegnr = aTable.getString("VBELN");
                fact = res.getFacturaForNr(belegnr);
                if (fact == null) {
                    fact = res.addFactura(mgr.readFacturaFromSAPTable(aTable));
                    /* 3.9.5 2015-09-01 DNi: #FINDBUGS - Potenzielle NP-Exception, weil fact != null nicht abgefragt wird.  */
                    if (isStorno) {
                        fact.setVbtyp("Storno");
                    } else {
                        fact.setVbtyp("Rechnung");
                    }
                }
            }
        }
    }

    /**
     * FI Open Item Status
     *
     * @param pInstitution Einrichtung
     * @param pFallnr Fall-Nr.
     * @return Result
     * @throws JCoException Exception
     */
    public SapExportResult getFIOpenItemStatus(final String pInstitution, final String pFallnr) throws JCoException {
        if (!sapProperties.importSAPCaseFI()) {
            LOG.log(Level.INFO, "FI module is disabled in the settings");
            return new SapExportResult();
        }
        SapExportResult res = new SapExportResult();
        res = getFIOpenItemStatus(pInstitution, pFallnr, res);
        return res;
    }

    /**
     * FI Open Item Status
     *
     * @param pInstitution Einrichtung
     * @param pFallnr Fall-Nr.
     * @param res Result
     * @return Result
     * @throws JCoException Exception
     */
    public SapExportResult getFIOpenItemStatus(final String pInstitution, final String pFallnr, SapExportResult res) throws JCoException {
        if (!sapProperties.importSAPCaseFI()) {
            LOG.log(Level.INFO, "FI module is disabled in the settings");
            return new SapExportResult();
        }
        if (res == null) {
            res = new SapExportResult();
        }
        final String methodName = "ZLBH_CASE_OPENITEMS_GET_01";
        JCoFunction function = createFunction(methodName);
        if (function == null) {
            LOG.log(Level.INFO, "FI module is not available");
            return new SapExportResult();
        }
//      if (function == null && !Z_RFC_OPENITEMS.equals(RFC_OPENITEMS)) {
//        function = createFunction(RFC_OPENITEMS);
//      }
        JCoParameterList parameter = function.getImportParameterList();
        parameter.setValue("EINRI", pInstitution);
        parameter.setValue("FALNR", pFallnr);
        callFunction(function);
        JCoParameterList exportparameter = function.getExportParameterList();
        String val = exportparameter.getString("TOTALOPENITEMS");
        double sumVal = 0;
        if (val != null) {
            try {
                String valStr = val, korrVal = "";
                //myLogger.info("CP-Val temp: " + valStr);
                char c;
                for (int i = 0; i < valStr.length(); i++) {
                    c = valStr.charAt(i);
                    switch (c) {
                        case ',':
                            korrVal += ".";
                            break;
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                        case '0':
                            korrVal += c;
                            break;
                        default:
//                            korrVal = korrVal; //yeah, that makes sense... NOT!
                    }
                }
                //myLogger.info("CP-Val korr: " + korrVal);
                sumVal = Double.parseDouble(korrVal.trim());
            } catch (NumberFormatException ex) {
                LOG.log(Level.SEVERE, "cannot parse value as double: " + val, ex.getMessage());
            }
        }
        SapFiOpenItem item;
        //FI-Faktura
        JCoTable aTable = function.getTableParameterList().getTable("BSID");
        SapImExManager mgr = this.getSAPManager();
        if (aTable != null) {
            for (int i = 0, n = aTable.getNumRows(); i < n; i++) {
                aTable.setRow(i);
                item = mgr.readFIOpenItemFromSAPTable(aTable);
                res.addOpenItem(item);
            }
        }

        return res;
    }
    //  private void checkOldFacturas(long fallid, Vector oldFacts, SapExportResult res, String userName,
    //          DbSQLConnection conn) throws RemoteException {
    //    SAPFIFactura oldFact, fact;
    //    for (int i = 0, n = oldFacts.size(); i < n; i++) {
    //      oldFact = (SAPFIFactura) oldFacts.get(i);
    //      fact = res.getFacturaForNr(oldFact.vbeln);
    //      SQLConnection c = conn.getSQLConnection();
    //      if (fact == null) {
    //        c.performSql("DELETE FROM " + SAPFIFactura.TABLE_NAME + " WHERE "
    //                + SAPFIFactura.ATT_ID + "=" + oldFact.id);
    //        c.performSql("DELETE FROM " + SAPFIPosition.TABLE_NAME + " WHERE "
    //                + SAPFIPosition.ATT_FAKTURA_ID + "=" + oldFact.id);
    //      } else {
    //        fact.id = oldFact.id;
    //        fact.creation_date = oldFact.creation_date;
    //        fact.creation_user = oldFact.creation_user;
    //        Vector v = oldFact.getFIPositions();
    //        int m = v != null ? v.size() : 0;
    //        for (int j = 0; j < m; j++) {
    //          SAPFIPosition oldPos = (SAPFIPosition) v.get(j);
    //          SAPFIPosition pos = fact.getSAPPositionByNr(oldPos.posnr);
    //          if (pos != null) {
    //            pos.id = oldPos.id;
    //            pos.creation_date = oldPos.creation_date;
    //            pos.creation_user = oldPos.creation_user;
    //          } else {
    //            c.performSql("DELETE FROM " + SAPFIPosition.TABLE_NAME + " WHERE "
    //                    + SAPFIPosition.ATT_ID + "=" + oldPos.id);
    //            //dbMgr.deleteObject(oldPos, userName);
    //          }
    //        }
    //        saveFactura(fallid, fact, userName, conn);
    //        fact.setChanged(true);
    //      }
    //    }
    //  }

    /**
     * checkForPsychCase dient zur Überprüfung, ob es sich bei einem Fall um
     * einen<br>
     * PEPP- oder um einen DRG FALL handelt.<br>
     * Die Methode wird ab sstVersion 6 verwendet.<br>
     *
     * @param pWkCase Case
     * @throws com.sap.conn.jco.JCoException Exception
     */
    protected void checkForPsychCase(final SapCase pWkCase) throws JCoException {
        //Vervollständigung der Fallnummer auf 10 Stellen
        final String fallNr = fillFallnr(pWkCase.getFallnr());
//        if (pWkCase.getFallnr().length() < 10) {
//            for (int i = pWkCase.getFallnr().length(); i < 10; i++) {
//                pWkCase.setFallnr("0" + pWkCase.getFallnr());
//            }
//        }

        final String methodName = "ZLBH_PSYDRG_CASE_CHECK_01";
        JCoFunction function = createFunction(methodName);
        if (function == null) {
            LOG.log(Level.INFO, "Method to get psych data is not available");
            return;
        }

        JCoParameterList parameter = function.getImportParameterList();
        parameter.setValue("P_EINRI", pWkCase.getInstitution());
        parameter.setValue("P_FALNR", fallNr);
        callFunction(function);
        String isPSY = function.getExportParameterList().getString("E_PSYDRGCASE");
        String billMode = function.getExportParameterList().getString("E_BILLMODE");
        if (isPSY != null) {
            pWkCase.setFalltyp("X".equals(isPSY) ? 2 : 1);
        }
    }

    /**
     * getSAPDateForStateChange liest die gewünschten Datumswerte, die in der
     * <br>
     * bapi_properites.properties hinterlegt sind, aus dem SAP aus.<br>
     * Die Methode wird ab sstVersion 6 verwendet.<br>
     *
     * @param pWkCase Case
     * @throws com.sap.conn.jco.JCoException Exception
     */
    protected void getSAPDateForStateChange(final SapCase pWkCase) throws JCoException {
        List<SapKisStateSearchResult> results = null;
        final String methodName = "ZLBH_CASE_STATUS_GET_02";
        JCoFunction function = createFunction(methodName);
        if (function == null) {
            LOG.log(Level.INFO, "Method to get state change is not available");
            return;
        }

        JCoParameterList parameter = function.getImportParameterList();
        parameter.setValue("P_EINRI", pWkCase.getInstitution());
        parameter.setValue("P_FALNR", pWkCase.getFallnr());
        callFunction(function);

        JCoTable tblKisStates = function.getTableParameterList().getTable("ET_JCDS");
        SapKisStateSearchResult kisStateSR;
        if (tblKisStates != null && !tblKisStates.isEmpty()) {
            int n = tblKisStates.getNumRows();
            results = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                tblKisStates.setRow(i);
                kisStateSR = new SapKisStateSearchResult();
                kisStateSR.readFromTable(tblKisStates);
                results.add(kisStateSR);
            }
        }

        if (results != null && !results.isEmpty()) {
            for (int i = 0, cnt = results.size(); i < cnt; i++) {
                Object obj = results.get(i);
                if (obj != null && obj instanceof SapKisStateSearchResult) {
                    SapKisStateSearchResult stateResult = (SapKisStateSearchResult) obj;
                    // Auslesen des Datums für Statuts E0001 = 'in Bearbeitung'
                    if ("E0001".equalsIgnoreCase(stateResult.getStat())) {
                        pWkCase.setKisstateFirstDate(combineDate(stateResult.getUdate(), stateResult.getUtime()));
                        continue;
                    }
                    // Auslesen des Datums für Statuts E0003 = 'in med. Freigabe'
                    if ("E0003".equalsIgnoreCase(stateResult.getStat())) {
                        pWkCase.setKisstateSecondDate(combineDate(stateResult.getUdate(), stateResult.getUtime()));
                    }
                }
            }
        }
    }

    /**
     * Kopiert die ausgelesenen SAP-Daten in die Struktur für den DB-Import
     *
     * @param pWkCase
     * @param pInstitution String
     * @param pIkz String
     * @param pFallnr String
     * @param pPatient SAPPatientSearchResult
     * @param pMovementList List
     * @param pErbringungsart
     * @param pPatientDetails
     * @param pDiagnosisList List
     * @param pProcedureList List
     * @param pNleis
     * @return boolean
     * @throws com.sap.conn.jco.JCoException
     */
//  public SapCase copyCase(final SapCase pWkCase, final String pInstitution, final String pIkz, final String pFallnr,
//          final SapPatientSearchResult pPatient, 
//          final int pErbringungsart, 
//          final SapPatientDetailResult pPatientDetails,
//          final List<SapMovementSearchResult> pMovementList,
//          final List<SapDiagnosisSearchResult> pDiagnosisList,
//          final List<SapProcedureSearchResult> pProcedureList,
//          final List<SapNleiSearchResult> pNleis) throws JCoException {
//    //int curFallID = -1;
//    //long externID = -1, lokalid = -1;
//    //SQLConnection c = dbConnection.getSQLConnection();
//    //pWkCase.patient = pat;
//    //pWkCase.patient_details = patDetails;
//
//    List insurances = getInsurances(pInstitution, pPatient, pFallnr);
//    Insurance ins = getCurrentInsurance(insurances);
//
//    pWkCase.insurance = ins;
//
//    pWkCase.insKOSTR = (ins != null ? ins.kostr : "");
//    if (pWkCase.insKOSTR != null) {
//      pWkCase.insKOSTR = pWkCase.insKOSTR.trim();
//    }
//
//    FallContainer fallContainer = new FallContainer(this, pWkCase, pPatient, pPatientDetails, pInstitution,
//            pFallnr, pErbringungsart, pMovementList, pDiagnosisList,
//            pProcedureList, pPatient.kasse);
//
//    fallContainer.plz = pPatientDetails.PCD;
//    fallContainer.tob = pPatientDetails.TOB;
//    fallContainer.kasse = (ins != null ? ins.institute_ind : "");
//
////    if (!fallContainer.wards.isEmpty()) {
////      int type = 0;
////      for (int i = 0, n = fallContainer.wards.size(); i < n; i++) {
////        if (n == 1) {
////          type = 4;
////        } else {
////          if (i == 0) {
////            type = 1;
////          } else if (i == (n - 1)) {
////            type = 3;
////          } else {
////            type = 2;
////          }
////        }
////        BewegungContainer b = (BewegungContainer) fallContainer.wards.get(i);
////        BewegungContainer prevBew = null, spec = null;
////        boolean inEx = false, inLo = false;
////        if (prevBew == null || (prevBew != null && !prevBew.spec_key.equals(b.spec_key))) {
////          //Speichern
//////          if (spec != null) {
//////            execUpdateMovements(spec, fall_lokalID, fall_externID, bewLoId, bewExId, inLo,
//////                    inEx, dbConnection);
//////          }
////          inEx = false;
////          inLo = false;
////          //bewLoId = getBewegungenLoExID(c, "fall_lokalid", fall_lokalID, b.bewegungnr);
////          //bewExId = getBewegungenLoExID(c, "fall_externid", fall_externID, b.bewegungnr);
//////          if (bewLoId == -1) {
//////            try {
//////              bewLoId = (int) dbConnection.getNewSerial("bewegungen", 1, c);
//////              inLo = true;
//////              if (bewExId == -1) {
//////                bewExId = bewLoId;
//////                inEx = true;
//////              }
//////            } catch (Exception e) {
//////              myLogger.fatal("Exception beim Erzeugen einer neuen bewegungen.id", e);
//////              return false;
//////            }
//////          } else {
//////            deleteMovementResults(bewLoId, c);
//////          }
//////          if (bewExId == -1) {
//////            try {
//////              bewExId = (int) dbConnection.getNewSerial("bewegungen", 1, c);
//////              inEx = true;
//////            } catch (Exception e) {
//////              myLogger.fatal("Exception beim Erzeugen einer neuen bewegungen.id", e);
//////              return false;
//////            }
//////          } else if (!inEx) {
//////            deleteMovementResults(bewExId, c);
//////          }
////          spec = new BewegungContainer(b);
////          myLogger.info("Neue Bewegung: " + spec.spec_description + ", " + spec.spec_key);
////        } else {
////          if (b.aufnehmende != null && b.aufnehmende.equals("1")) {
////            spec.aufnehmende = b.aufnehmende;
////          }
////          if (b.behandelnde != null && b.behandelnde.equals("1")) {
////            spec.behandelnde = b.behandelnde;
////          }
////          if (b.entlassende != null && b.entlassende.equals("1")) {
////            spec.entlassende = b.entlassende;
////          }
////          spec.beatmungsdauer += b.beatmungsdauer;
////          spec.ende = b.ende;
////        }
////        prevBew = b;
////      }
////    }
//    return fallContainer;
//
////    if (patID >= 0) {
////      m_curFallID = insertUpdateFall(institution, ikz, fallnr, patID, dbConnection, patDetails);
////      if (m_curFallID >= 0) {
////        m_curCase = new FallContainer(mConnection, dbConnection, mandant, pat, institution,
////                fallnr, erbringungsart, m_curFallID, movementList, diagnosisList,
////                procedureList, pat.kasse);
////        m_curCase.plz = patDetails.PCD;
////        m_curCase.tob = patDetails.TOB;
////        int errid = MESSAGE_STATE_FINISHED;
////        SAPCaseDRG sapdrg = (m_curWKCase != null ? m_curWKCase.m_sapdrg : null);
////        if (!m_curCase.wards.isEmpty()) {
////          if (newCase || isInPatient(m_curFallID, c)) {
////            int[] fids = insertUpdateCase(m_curCase, dbConnection, 1, 1);
////            if (fids != null && fids.length >= 2) {
////              externID = fids[0];
////              lokalid = fids[1];
////            }
////            if (externID >= 0) {
////              c.commitTransaction();
////              c.beginTransaction();
////              errid = groupCase(m_curFallID, externID, lokalid, m_curCase.aufnahme, dbConnection, roleID, sapdrg);
////            }
////          } else {
////            externID = getFallLoExID(c, "fall_extern", m_curCase.id);
////            if (testNewCaseVersion(dbConnection, "fall_extern", m_curCase,
////                    externID)) {
////              Object versObj
////                      = c.singleSqlResult("SELECT MAX(version) FROM fall_extern WHERE fallid="
////                              + m_curFallID);
////              int versionEx = 0;
////              int versionLo = 0;
////              if (versObj != null) {
////                if (versObj instanceof Integer) {
////                  versionEx = ((Integer) versObj).intValue();
////                } else if (versObj instanceof BigDecimal) {
////                  versionEx = ((BigDecimal) versObj).intValue();
////                }
////              }
////
////              versObj
////                      = c.singleSqlResult("SELECT MAX(version) FROM fall_lokal WHERE fallid=" + m_curFallID);
////              if (versObj != null) {
////                if (versObj instanceof Integer) {
////                  versionLo
////                          = ((Integer) versObj).intValue();
////                } else if (versObj instanceof BigDecimal) {
////                  versionLo
////                          = ((BigDecimal) versObj).intValue();
////                }
////              }
////
////              c.performSql("UPDATE fall_extern SET aktuell=0 WHERE fallid=" + m_curFallID);
////              c.performSql("UPDATE fall_lokal SET aktuell=0 WHERE fallid=" + m_curFallID);
////              int[] fids
////                      = insertUpdateCase(m_curCase, dbConnection, versionEx
////                              + 1, versionLo + 1);
////              if (fids != null && fids.length >= 2) {
////                externID = fids[0];
////                lokalid = fids[1];
////              }
////              if (externID >= 0) {
////                c.commitTransaction();
////                c.beginTransaction();
////                errid
////                        = groupCase(m_curFallID, externID, lokalid, m_curCase.aufnahme, dbConnection, roleID, sapdrg);
////
////              }
////              c.performSql("UPDATE fall SET status=3 WHERE id="
////                      + m_curFallID);
////            }
////          }
////        }
////        insertUpdateEntgelte(newCase, externID, lokalid,
////                ((m_curWKCase != null) ? m_curWKCase.m_entg : null),
////                m_curCase.kasse, dbConnection, m_curCase.id);
////        insertUpdateReferenceCases(newCase, m_curFallID, m_curWKCase.m_refCases,
////                dbConnection);
////        if (this.importSAPCaseFI()) {
////          Vector oldFacts;
////          if (!newCase) {
////            if (this.isDebugMode()) {
////              myLogger.info("Alte Facturas auslesen");
////            }
////            oldFacts
////                    = getOldFacturas(m_curFallID, dbConnection.getSQLConnection());
////          } else {
////            oldFacts = null;
////          }
////          SapExportResult res
////                  = this.getFICaseStatus(m_curFallID, fallnr, oldFacts,
////                          "sapimport", mConnection, institution,
////                          dbConnection);
////          if (res.m_text != null && res.m_text.length() > 0) {
////            myLogger.fatal(res.m_text);
////          }
////          if (dbConnection.getSQLConnection().getErrorStatus() > 0) {
////            myLogger.fatal(dbConnection.getSQLConnection().getErrorString());
////            myLogger.fatal(dbConnection.getSQLConnection().getMessageString());
////            dbConnection.getSQLConnection().resetErrorStatus();
////          }
////        }
////        if (nleis != null) {
////          insertUpdateNLEI(c, fallnr, nleis);
////        }
////        if (errid != MESSAGE_STATE_FINISHED) {
////          return errid;
////        }
////        if (c.getErrorStatus() == 0) {
////          return MESSAGE_STATE_FINISHED;
////        }
////      } else {
////        myLogger.fatal(ERROR_PREFIX + "Fall " + fallnr + " in Institution "
////                + institution + " nicht gefunden !\n");
////      }
////    } else {
////      myLogger.fatal(ERROR_PREFIX + "Patient " + pat.PATIENTID + "nicht gefunden !\n");
////    }
////
////    return MESSAGE_STATE_ERROR;
//  }
//  /**
//   * Ließt zu dem Patienten die Versicherungsdetails aus
//   *
//   * @param ins Insurance
//   * @param patID String
//   * @param lfdnr String
//   * @param mConnection Client
//   */
//  private void getInsuranceDetails(Insurance ins, String patID, String lfdnr, String tableName) throws JCoException {
//    final String methodName = "RFC_READ_TABLE";
//    JCoFunction function = createFunction(methodName);
//    JCoParameterList parameter = function.getImportParameterList();
//    parameter.setValue("QUERY_TABLE", tableName);
//    String where = getInsurancePatWhere(patID, lfdnr);
//    if (where != null) {
//      JCoTable tblOptions = function.getTableParameterList().getTable("OPTIONS");
//      tblOptions.appendRow();
//      tblOptions.setValue("TEXT", where);
//      JCoTable tblFields = function.getTableParameterList().getTable("FIELDS");
//      tblFields.appendRow();
//      tblFields.setValue("FIELDNAME", "PATNR");
//      /*tblFields.appendRow();
//					   tblFields.setValue("VERAB", "FIELDNAME");
//					   tblFields.appendRow();
//					   tblFields.setValue("VERBI", "FIELDNAME");*/
//      callFunction(function);
//      JCoTable returnStructure = function.getTableParameterList().getTable("DATA");
//      int n = returnStructure.getNumRows();
//      if (n > 0) {
//        String res = returnStructure.getString("WA");
//        ins.verab = res.substring(0, 8);
//        ins.verbi = res.substring(8, 16);
//      }
//    }
//  }
//  private String getInsurancePatWhere(String patID, String lfdnr) {
//    int lg = 0;
//    if (lfdnr == null) {
//      LOGGER.severe("Einrichtung im Insurance-Where ist NULL");
//      return null;
//    }
//    lg = lfdnr.length();
//    if (lg < 3) {
//      for (int i = lg; i < 3; i++) {
//        lfdnr = "0" + lfdnr;
//      }
//    } else if (lg > 3) {
//      LOGGER.severe("LFDNR-String zu lang: " + lfdnr);
//      return null;
//    }
//    lg = patID.length();
//    if (lg < 10) {
//      for (int i = lg; i < 10; i++) {
//        patID = "0" + patID;
//      }
//    } else if (lg > 10) {
//      LOGGER.severe("PAtient-String zu lang: " + patID);
//      return null;
//    }
//    return "PATNR = '" + patID + "' AND LFDNR='" + lfdnr + "'";
//  }
//  public static String serialize(final Object pObject) {
//    if (pObject == null) {
//      throw new IllegalArgumentException("Object is null!");
//    }
//    String output = "";
//    try {
//      ByteArrayOutputStream bo = new ByteArrayOutputStream();
//      ObjectOutputStream so = new ObjectOutputStream(bo);
//      so.writeObject(pObject);
//      so.flush();
//      output = new String(Base64.getEncoder().encode(bo.toByteArray()));
//    } catch (IOException e) {
//      LOGGER.log(Level.SEVERE, "Was not able to serialize object", e);
//    }
//    return output;
//  }
//
//  public static Object deserialize(final String pString) {
//    final String string = (pString == null) ? "" : pString.trim();
//    if (string.isEmpty()) {
//      throw new IllegalArgumentException("String is null or empty!");
//    }
//    Object obj = null;
//    try {
//      byte b[] = Base64.getDecoder().decode(string.getBytes());
//      ByteArrayInputStream bi = new ByteArrayInputStream(b);
//      ObjectInputStream si = new ObjectInputStream(bi);
//      obj = si.readObject();
//    } catch (IOException | ClassNotFoundException e) {
//      LOGGER.log(Level.SEVERE, "Was not able to deserialize string", e);
//    }
//    return obj;
//  }
    /**
     * Prüft, ob es in der Bewegungsliste mindestens EINE stationäre Bewegung
     * gibt Zusätzlich wird geprüft, ob bei aktivierter Option ambulante OPs
     * oder vorstationäre Aufenthalte übernommen werden
     *
     * @param movementList List
     * @param pWkCase Case
     * @return boolean
     */
//  protected boolean stationaereBewegungen(List<SapMovementSearchResult> movementList, WkCase pWkCase) {
//    for (int i = 0, n = movementList.size(); i < n; i++) {
//      SapMovementSearchResult move = (SapMovementSearchResult) movementList.get(i);
//      if (move.STORN != null && move.STORN.length() == 0) {
//        if (move.BEWTY.equals("1") || move.BEWTY.equals("2") || move.BEWTY.equals("3")) {
//          return true;
//        }
//      }
//    }
//    if (importAmbulantOP() || importAmbulantCancels()) {
//      for (int i = 0, n = movementList.size(); i < n; i++) {
//        SapMovementSearchResult move = (SapMovementSearchResult) movementList.get(i);
//        if (move.STORN != null && move.STORN.length() == 0) {
//          if (importAmbulantOP() && move.BWART.equals("AO")) {
//            pWkCase.falltyp = 4;
//            return true;
//          } else if (importAmbulantCancels() && move.BWART.equals("VO")) {
//            pWkCase.falltyp = 5;
//            return true;
//          }
//        }
//      }
//    }
//    return false;
//  }
//  /**
//   * getSAPDateForStateChange liest die gewünschten Datumswerte, die in der
//   * <br>
//   * bapi_properites.properties hinterlegt sind, aus dem SAP aus.<br>
//   * Die Methode wird ab sstVersion 6 verwendet.<br>
//   *
//   * @param pWkCase Case
//   * @throws com.sap.conn.jco.JCoException
//   */
//  protected void getSAPDateForStateChange(final WkCase pWkCase) throws JCoException {
//    ArrayList<SapKisStateSearchResult> results = null;
//    //JCoDestination destination = createSapDestination();
//    JCoFunction function = createFunction("zlbh_case_status_get_02");
//    JCoParameterList parameter = function.getImportParameterList();
//    parameter.setValue("P_EINRI", pWkCase.mInstitution);
//    parameter.setValue("P_FALNR", pWkCase.mFallNr);
//    callFunction(function);
//
//    JCoTable tblKisStates = function.getTableParameterList().getTable("ET_JCDS");
//    SapKisStateSearchResult kisStateSR;
//    if (tblKisStates != null && !tblKisStates.isEmpty()) {
//      int n = tblKisStates.getNumRows();
//      results = new ArrayList<>(n);
//      for (int i = 0; i < n; i++) {
//        tblKisStates.setRow(i);
//        kisStateSR = new SapKisStateSearchResult();
//        kisStateSR.readFromTable(tblKisStates);
//        results.add(kisStateSR);
//      }
//    }
//
//    if (results != null && !results.isEmpty()) {
//      for (int i = 0, cnt = results.size(); i < cnt; i++) {
//        Object obj = results.get(i);
//        if (obj != null && obj instanceof SapKisStateSearchResult) {
//          SapKisStateSearchResult stateResult = (SapKisStateSearchResult) obj;
//          // Auslesen des Datums für Statuts E0001 = 'in Bearbeitung'
//          if (stateResult.STAT.equalsIgnoreCase("E0001")) {
//            pWkCase.kisstate_first_date = combineDate(stateResult.UDATE, stateResult.UTIME);
//            continue;
//          }
//          // Auslesen des Datums für Statuts E0003 = 'in med. Freigabe'
//          if (stateResult.STAT.equalsIgnoreCase("E0003")) {
//            pWkCase.kisstate_second_date = combineDate(stateResult.UDATE, stateResult.UTIME);
//          }
//        }
//      }
//    }
//  }
    /**
     *
     * @param pMethodName Method name
     * @return JCO Function
     * @throws JCoException Exception
     */
    public JCoFunction createFunction(final String pMethodName) throws JCoException {
        if (pMethodName == null || pMethodName.trim().isEmpty()) {
            throw new IllegalArgumentException("Method name is null or empty!");
        }
        LOG.log(Level.FINE, "Get function for ''{0}''...", pMethodName);
        JCoRepository repository = createSapRepository();
        JCoFunctionTemplate ft = repository.getFunctionTemplate(pMethodName);
        if (ft == null) {
            throw new IllegalArgumentException("No function template available for '" + pMethodName + "'!");
        }
        JCoFunction function;
        function = ft.getFunction();
        if (function == null) {
            LOG.log(Level.WARNING, "Cannot get function ''{0}'' (perhaps functionality is restricted)", pMethodName);
        }
        return function;
    }

    /**
     * Calls JCO Function
     *
     * @param pFunction JCO Function
     * @return successful?
     * @throws JCoException Exception
     */
    public boolean callFunction(final JCoFunction pFunction) throws JCoException {
        if (pFunction == null) {
            throw new IllegalArgumentException("Function is null!");
        }

        boolean result = false;
        JCoDestination destination = getSapDestination();
        LOG.log(Level.FINEST, "Call function {0}...", pFunction.getName());
        try {
            pFunction.execute(destination);
            result = true;
        } catch (JCoException ex) {
            if (ex.getMessage() != null && ex.getMessage().contains("NO_AUTHORITY")) {
                //don't show full stack trace in default mode if this exception occurs: ZLBH_GET_WKDATEN gescheitert: NO_AUTHORITY: com.sap.conn.jco.AbapException: (126) NO_AUTHORITY: NO_AUTHORITY Message 552 of class N8 type E
                LOG.log(Level.WARNING, "{0} gescheitert (weil VIP-Patient?!): {1}", new Object[]{pFunction.getName(), ex.getMessage()});
                LOG.log(Level.FINEST, null, ex);
            } else {
                LOG.log(Level.WARNING, pFunction.getName() + " gescheitert: " + ex.getMessage(), ex);
            }
        }
        return result;
    }

    /**
     * Close everything
     */
    @Override
    public void close() {
        if (mWriter != null) {
            mWriter.closeFile();
            compressFile(mWriter.getFile(), 512); //Nur mit ZIP komprimieren falls die Datei größer als 0,5 MB ist
        }
    }

    /**
     * Überträgt die erstellten INKA-Nachrichten ans SAP
     *
     * @param pInstitution String
     * @param pFallNr String
     * @param pHandle String
     * @param pKostr String
     * @param pInkaMessage SAPKAINDetailSearchResult
     * @throws JCoException something went wrong -.-
     * @return SapExportResult
     */
    public SapExportResult sendInkaMessage(final String pInstitution, final String pFallNr,
            final Long pHandle, final String pKostr, final TP301Inka pInkaMessage) throws JCoException {
        SapExportResult result = null;
        String fallNr = pFallNr == null ? "" : pFallNr.trim();
        if (pInkaMessage == null) {
            return new SapExportResult("KAIN / INKA - Message ist leer!");
        }
        if (pHandle == null || pHandle.equals(0L)) {
            return new SapExportResult("Handle ist leer oder 0!");
        }
        if (pInstitution == null || pInstitution.trim().isEmpty()) {
            LOG.log(Level.WARNING, "Institution ist leer!");
        }
        if (pKostr == null || pKostr.trim().isEmpty()) {
            LOG.log(Level.WARNING, "Kostenträger ist leer!");
        }
        if (pKostr == null || pKostr.trim().isEmpty()) {
            LOG.log(Level.WARNING, "Kostenträger ist leer!");
        }
        if (fallNr.isEmpty()) {
            LOG.log(Level.WARNING, "Fallnummer ist leer!");
        }
        final String handle = pHandle + "";
        try {
            //Vervollständigung der Fallnummer auf 10 Stellen
            if (fallNr.length() < 10) {
                fallNr = fillFallnr(fallNr);
//                for (int i = fallNr.length(); i < 10; i++) {
//                    fallNr = "0" + fallNr;
//                }
            }

            //SapKainDetailSearchResult sapINKADetails = new SapKainDetailSearchResult();
            //sapINKADetails.fillFromKainMessage(pInkaMessage);
            //Setzen der Werte für Hizufügen von Diagnosen
            final JCoFunction newINKAFunction = createFunction(Z_RFC_INKA_CREATE);
            JCoParameterList newParameter = newINKAFunction.getImportParameterList();
            newParameter.setValue("I_EINRI", pInstitution);
            newParameter.setValue("I_FALNR", fallNr);
            newParameter.setValue("I_KOSTR", pKostr);
            newParameter.setValue("I_HANDLE", handle);
            LOG.log(Level.FINEST, "I_EINRI = {0}", pInstitution);
            LOG.log(Level.FINEST, "I_FALNR = {0}", fallNr);
            LOG.log(Level.FINEST, "I_KOSTR = {0}", pKostr);
            LOG.log(Level.FINEST, "I_HANDLE = {0}", handle);
//            if (isDebugMode()) {
//                myLogger.info("I_EINRI: " + pInstitution);
//                myLogger.info("I_FALNR: " + pFallNr);
//                myLogger.info("I_KOSTR: " + pKostr);
//                myLogger.info("I_HANDLE: " + handle);
//            }
            JCoTable tblNewINKAPVV = newINKAFunction.getTableParameterList().getTable("IT_PVV");
            JCoTable tblNewINKAPVT = newINKAFunction.getTableParameterList().getTable("IT_PVT");
            if (pInkaMessage.getKainInkaPvvs() != null) {
                int n = pInkaMessage.getKainInkaPvvs().size();
                for (int i = 0; i < n; i++) {
                    TP301KainInkaPvv pvvSegm = pInkaMessage.getKainInkaPvvs().get(i);
                    if (pvvSegm != null) {
                        String pvvRowID = String.valueOf(i + 1);
                        setPvvValues(tblNewINKAPVV, tblNewINKAPVT,
                                pvvSegm, mMandant, handle, pvvRowID);
                    }
                }
            }

//            writePVVandPVTForExport(tblNewINKAPVV, tblNewINKAPVT);
            final boolean called = callFunction(newINKAFunction);

            if (!called) {
                Exception ex = newINKAFunction.getException("NOT_ACTIVE");
                if (ex != null) {
                    LOG.log(Level.SEVERE, "Failed to send INKA message", ex.getMessage());
                }
                return new SapExportResult("Der Aufruf der Methode zum Versenden einer INKA-Nachricht schlug fehl!");
            }
            if (commitSAP()) {
                LOG.log(Level.INFO, "Send INKA message successfully committed");
            } else {
                LOG.log(Level.SEVERE, "Send INKA message was not committed!");
            }

//            Exception ex1 = newINKAFunction.getException("RFC_OTHERS");
//            LOG.log(Level.INFO, "RFC_OTHERS = " + (ex1 == null ? "null" : ex1.getMessage()));
//            Exception ex2 = newINKAFunction.getException("CASE_DOES_NOT_EXIST");
//            LOG.log(Level.INFO, "CASE_DOES_NOT_EXIST = " + (ex2 == null ? "null" : ex2.getMessage()));
//            Exception ex3 = newINKAFunction.getException("NOT_ACTIVE_ISH_READ_NC301VER");
//            LOG.log(Level.INFO, "NOT_ACTIVE_ISH_READ_NC301VER = " + (ex3 == null ? "null" : ex3.getMessage()));
//            Exception ex4 = newINKAFunction.getException("NOT_ACTIVE_ASSIGNMENT_BY_DATE");
//            LOG.log(Level.INFO, "NOT_ACTIVE_ASSIGNMENT_BY_DATE = " + (ex4 == null ? "null" : ex4.getMessage()));
//            Exception ex5 = newINKAFunction.getException("NOT_ACTIVE_VERSION13");
//            LOG.log(Level.INFO, "NOT_ACTIVE_VERSION13 = " + (ex5 == null ? "null" : ex5.getMessage()));
//            Exception ex6 = newINKAFunction.getException("NOT_ACTIVE_DGUV");
//            LOG.log(Level.INFO, "NOT_ACTIVE_DGUV = " + (ex6 == null ? "null" : ex6.getMessage()));
//            Exception ex7 = newINKAFunction.getException("NOT_ACTIVE_INKA_CREATE");
//            LOG.log(Level.INFO, "NOT_ACTIVE_INKA_CREATE = " + (ex7 == null ? "null" : ex7.getMessage()));
        } catch (JCoException ex) {
            LOG.log(Level.SEVERE, "Was not able to export/send INKA message to SAP", ex);
            throw ex;
            //createErrorMessage(ex, 2);
//            return new SapExportResult(ex.getMessage());
        }
        return result;
    }

    private void writePVVandPVTForExport(JCoTable pvvTable, JCoTable pvtTable) {
        SapKainPvvSearchResult skpPVVResult;
        if (pvvTable != null && !pvvTable.isEmpty()) {
            int n = pvvTable.getNumRows();
            LOG.log(Level.INFO, "Anzahl der PVV-Segmente f\u00fcr Export: {0}", String.valueOf(n));
            for (int i = 0; i < n; i++) {
                pvvTable.setRow(i);
                skpPVVResult = new SapKainPvvSearchResult();
                skpPVVResult.readFromTable(pvvTable);
                LOG.log(Level.INFO, "PVV-Segment {0} für Export lautet:\n{1}", new Object[]{i, skpPVVResult});
            }
        } else {
            LOG.log(Level.SEVERE, "Keine PVV-Segmente für Export vorhanden!");
            if (pvvTable == null) {
                LOG.log(Level.INFO, "Geholte Liste der PVV-Segmente ist null.");
            } else {
                LOG.log(Level.INFO, "Geholte Liste der PVV-Segmente ist leer.");
            }
        }

        SapKainPvtSearchResult skpPVTResult;
        if (pvtTable != null && !pvtTable.isEmpty()) {
            int n = pvtTable.getNumRows();
            LOG.log(Level.INFO, "Anzahl der PVT-Segmente für Export: {0}", String.valueOf(n));

            for (int i = 0; i < n; i++) {
                pvtTable.setRow(i);
                skpPVTResult = new SapKainPvtSearchResult();
                skpPVTResult.readFromTableForExport(pvtTable);
                LOG.log(Level.INFO, "PVT-Segment {0} für Export lautet:\n{1}", new Object[]{i, skpPVTResult});
            }
        } else {
            LOG.log(Level.SEVERE, "Keine PVT-Segmente für Export vorhanden!");
            if (pvtTable == null) {
                LOG.log(Level.INFO, "Geholte Liste der PVT-Segmente ist null.");
            } else {
                LOG.log(Level.INFO, "Geholte Liste der PVT-Segmente ist leer.");
            }
        }
    }

    /**
     * Storniert die erstellten INKA-Nachrichten über SAP
     *
     * @param pInstitution String
     * @param pFallNr String
     * @param pHandle String
     * @return SapExportResult
     * @throws com.sap.conn.jco.JCoException error
     */
    public SapExportResult cancelInkaMessage(final String pInstitution, final String pFallNr, final Long pHandle) throws JCoException {
        SapExportResult result = null;
//        try {
        String fallNr = pFallNr == null ? "" : pFallNr.trim();
        if (pHandle == null) {
            return new SapExportResult("Handle ist leer!");
        }

        //Vervollständigung der Fallnummer auf 10 Stellen
        if (fallNr.length() < 10) {
            fallNr = fillFallnr(fallNr);
//            for (int i = fallNr.length(); i < 10; i++) {
//                fallNr = "0" + fallNr;
//            }
        }

        //Setzen der Werte für Hizufügen von Diagnosen
        final String methodName = "ZLBH_INKA_CANCEL";
        JCoFunction cancelINKAFunction = createFunction(methodName);
        JCoParameterList newParameter = cancelINKAFunction.getImportParameterList();
        newParameter.setValue("I_EINRI", pInstitution);
        newParameter.setValue("I_FALNR", fallNr);
        newParameter.setValue("I_HANDLE", pHandle);
        LOG.log(Level.FINEST, "I_EINRI = {0}", pInstitution);
        LOG.log(Level.FINEST, "I_FALNR = {0}", fallNr);
        LOG.log(Level.FINEST, "I_HANDLE = {0}", pHandle);
//            if (isDebugMode()) {
//                System.out.println("Parameter INKA-Storno I_EINRI: " + institution);
//                System.out.println("Parameter INKA-Storno I_FALNR: " + fallnr);
//                System.out.println("Parameter INKA-Storno I_HANDLE: " + extIdent);
//            }
        if (!callFunction(cancelINKAFunction)) {
            return new SapExportResult("Der Aufruf der Methode zum Stornieren einer INKA-Nachricht schlug fehl!");
        }
        commitSAP();
//        } catch (JCoException ex) {
//            //createErrorMessage(ex, 2);
//            result = new SapExportResult(ex.getMessage());
//        }
        return result;
    }

//    /**
//     * Überträgt die erstellten INKA-Nachrichten ans SAP
//     *
//     * @param pInstitution String
//     * @param pFallNr String
//     * @param pHandle String
//     * @param pKostr String
//     * @param pInkaMessage SAPKAINDetailSearchResult
//     * @throws JCoException something went wrong -.-
//     * @return SapExportResult
//     */
//    public SapExportResult sendInkaMessage(final String pInstitution, final String pFallNr,
//            final String pHandle, final String pKostr, final KainInkaMessage pInkaMessage) throws JCoException {
//        SapExportResult result = null;
//        String fallNr = pFallNr;
//        try {
//            if (pInkaMessage == null || pHandle == null) {
//                return new SapExportResult("KAIN / INKA - Message ist leer!");
//            }
//
//            //Vervollständigung der Fallnummer auf 10 Stellen
//            if (fallNr.length() < 10) {
//                for (int i = fallNr.length(); i < 10; i++) {
//                    fallNr = "0" + fallNr;
//                }
//            }
//
//            SapKainDetailSearchResult sapINKADetails = new SapKainDetailSearchResult();
//            sapINKADetails.fillFromKainMessage(pInkaMessage);
//            JCoFunction newINKAFunction = null;
//            //Setzen der Werte für Hizufügen von Diagnosen
//            if (newINKAFunction == null) {
//                newINKAFunction = createFunction(Z_RFC_INKA_CREATE);
//                JCoParameterList newParameter = newINKAFunction.getImportParameterList();
//                newParameter.setValue(pInstitution, "I_EINRI");
//                newParameter.setValue(fallNr, "I_FALNR");
//                newParameter.setValue(pKostr, "I_KOSTR");
//                newParameter.setValue(pHandle, "I_HANDLE");
//            }
////            if (isDebugMode()) {
////                myLogger.info("I_EINRI: " + pInstitution);
////                myLogger.info("I_FALNR: " + pFallNr);
////                myLogger.info("I_KOSTR: " + pKostr);
////                myLogger.info("I_HANDLE: " + pHandle);
////            }
//            JCoTable tblNewINKAPVV = newINKAFunction.getTableParameterList().getTable("IT_PVV");
//            JCoTable tblNewINKAPVT = newINKAFunction.getTableParameterList().getTable("IT_PVT");
//            if (sapINKADetails.alPVVs != null) {
//                int n = sapINKADetails.alPVVs.size();
//                for (int i = 0; i < n; i++) {
//                    SapKainPvvSearchResult pvvSegm = (SapKainPvvSearchResult) sapINKADetails.alPVVs.get(i);
//                    if (pvvSegm != null && result == null) {
//                        String pvvRowID = String.valueOf(i + 1);
//                        result = setPvvValues(tblNewINKAPVV, tblNewINKAPVT,
//                                pvvSegm, mMandant, pHandle, pvvRowID);
//                    }
//                }
//            }
////            if (isDebugMode()) {
////                writePVVandPVTForExport(tblNewINKAPVV, tblNewINKAPVT);
////            }
//
//            if (!callFunction(newINKAFunction)) {
//                Exception ex = newINKAFunction.getException("NOT_ACTIVE");
//                if (ex != null) {
//                    LOG.log(Level.SEVERE, ex.getMessage());
//                }
//                return new SapExportResult("Der Aufruf der Methode zum Versenden einer INKA-Nachricht schlug fehl!");
//            }
//            commitSAP();
//        } catch (JCoException ex) {
//            LOG.log(Level.SEVERE, "Was not able to export/send INKA message to SAP", ex);
//            throw ex;
//            //createErrorMessage(ex, 2);
////            return new SapExportResult(ex.getMessage());
//        }
//        return result;
//    }
    /**
     * Erstellung der PVV-Segmente einer INKA-Nachricht
     *
     * @param tblNewINKAPVV Table
     * @param tblNewINKAPVT Table
     * @param sapPVV SAPKAINPVVSearchResult
     * @param pvvMandant String
     * @param pvvHandle String
     * @return SapExportResult
     */
    private void setPvvValues(final JCoTable tblNewINKAPVV, final JCoTable tblNewINKAPVT, final TP301KainInkaPvv sapPVV,
            final String pvvMandant, final String pvvHandle, final String pvvRowID) {

//        SapExportResult result = null;
//        try {
        if (tblNewINKAPVV != null && tblNewINKAPVT != null && sapPVV != null) {
            tblNewINKAPVV.appendRow();
            tblNewINKAPVV.setValue("MANDT", pvvMandant);
            tblNewINKAPVV.setValue("HANDLE", pvvHandle);
            tblNewINKAPVV.setValue("PVVROWID", pvvRowID);
            tblNewINKAPVV.setValue("PVVINFO", sapPVV.getInformationKey30()); //sapPVV.PVVINFO
            tblNewINKAPVV.setValue("PVVRECNO", sapPVV.getBillNr()); //sapPVV.PVVRECNO
            String dtm = getSapDate(sapPVV.getBillDate()); //sapPVV.PVVRECDAT
            tblNewINKAPVV.setValue("PVVRECDAT", dtm);
            LOG.log(Level.FINEST, "MANDT = {0}", pvvMandant);
            LOG.log(Level.FINEST, "HANDLE = {0}", pvvHandle);
            LOG.log(Level.FINEST, "PVVROWID = {0}", pvvRowID);
            LOG.log(Level.FINEST, "PVVINFO = {0}", sapPVV.getInformationKey30());
            LOG.log(Level.FINEST, "PVVRECNO = {0}", sapPVV.getBillNr());
            LOG.log(Level.FINEST, "PVVRECDAT = {0}", dtm);

            if (sapPVV.getKainInkaPvts() != null) {
                int n = sapPVV.getKainInkaPvts().size();
                for (int i = 0; i < n; i++) {
                    TP301KainInkaPvt pvtSegm = sapPVV.getKainInkaPvts().get(i);
                    if (pvtSegm != null) {
                        String pvtRowID = String.valueOf(i + 1);
                        setPvtValues(tblNewINKAPVT, pvtSegm, i,
                                pvvMandant, pvvHandle, pvvRowID, pvtRowID);
                    }
                }
            }
        }
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, "Cannot set PVV values", ex);
//            //createErrorMessage(ex, 2);
//            throw ex;
//            //return new SapExportResult(ex.getMessage());
//        }
//        return result;
    }

//    /**
//     * Erstellung der PVV-Segmente einer INKA-Nachricht
//     *
//     * @param tblNewINKAPVV Table
//     * @param tblNewINKAPVT Table
//     * @param sapPVV SAPKAINPVVSearchResult
//     * @param pvvMandant String
//     * @param pvvHandle String
//     * @return SapExportResult
//     */
//    private SapExportResult setPvvValues(JCoTable tblNewINKAPVV, JCoTable tblNewINKAPVT, SapKainPvvSearchResult sapPVV,
//            String pvvMandant, String pvvHandle, String pvvRowID) {
//
//        SapExportResult result = null;
//        try {
//            if (tblNewINKAPVV != null && tblNewINKAPVT != null && sapPVV != null) {
//                tblNewINKAPVV.appendRow();
//                tblNewINKAPVV.setValue(pvvMandant, "MANDT");
//                tblNewINKAPVV.setValue(pvvHandle, "HANDLE");
//                tblNewINKAPVV.setValue(pvvRowID, "PVVROWID");
//                tblNewINKAPVV.setValue(sapPVV.PVVINFO, "PVVINFO");
//                tblNewINKAPVV.setValue(sapPVV.PVVRECNO, "PVVRECNO");
//                String dtm = getSapDate(sapPVV.PVVRECDAT);
//                tblNewINKAPVV.setValue(dtm, "PVVRECDAT");
//                if (sapPVV.alPVTs != null) {
//                    int n = sapPVV.alPVTs.size();
//                    for (int i = 0; i < n; i++) {
//                        SapKainPvtSearchResult pvtSegm = (SapKainPvtSearchResult) sapPVV.alPVTs.get(i);
//                        if (pvtSegm != null && result == null) {
//                            String pvtRowID = String.valueOf(i + 1);
//                            result = setPvtValues(tblNewINKAPVT, pvtSegm, i,
//                                    pvvMandant, pvvHandle, pvvRowID, pvtRowID);
//                        }
//                    }
//                }
//            }
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, "Cannot set PVV values", ex);
//            //createErrorMessage(ex, 2);
//            throw ex;
//            //return new SapExportResult(ex.getMessage());
//        }
//        return result;
//    }
    /**
     * Erstellung der PVT-Segmente einer INKA-Nachricht
     *
     * @param tblNewINKAPVT Table
     * @param pvtSegm SAPKAINPVTSearchResult
     * @param pvtCnt int
     * @param pvtMandant String
     * @param pvtExtIdent String
     * @return SapExportResult
     */
    private static void setPvtValues(final JCoTable tblNewINKAPVT, final TP301KainInkaPvt pvtSegm, final int pvtCnt,
            final String pvtMandant, final String pvtHandle, final String pvvRoeID, final String pvtRowID) {
//        try {
        if (tblNewINKAPVT != null && pvtSegm != null) {
            tblNewINKAPVT.appendRow();
            tblNewINKAPVT.setValue("MANDT", pvtMandant);
            tblNewINKAPVT.setValue("HANDLE", pvtHandle);
            tblNewINKAPVT.setValue("PVVROWID", pvvRoeID);
            tblNewINKAPVT.setValue("PVTROWID", pvtRowID);
            tblNewINKAPVT.setValue("PVTTEXT", pvtSegm.getText()); //pvtSegm.PVTTEXT
            LOG.log(Level.FINEST, "MANDT = {0}", pvtMandant);
            LOG.log(Level.FINEST, "HANDLE = {0}", pvtHandle);
            LOG.log(Level.FINEST, "PVVROWID = {0}", pvvRoeID);
            LOG.log(Level.FINEST, "PVTROWID = {0}", pvtRowID);
            LOG.log(Level.FINEST, "PVTTEXT = {0}", pvtSegm.getText());
        }
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, "Cannot set PVT values", ex);
//            //createErrorMessage(ex, 2);
//            throw ex;
//            //return new SapExportResult(ex.getMessage());
//        }
//        return null;
    }

    /**
     * Überträgt den Status einer MD-Anfrage ans SAP
     *
     * @param pInstitution String
     * @param pFallNr String
     * @param pHandle String
     * @param pKostr String
     * @param pInkaMessage SAPKAINDetailSearchResult
     * @throws JCoException something went wrong -.-
     * @return SapExportResult
     */
    public SapExportResult setMDStateToSAP(final String pInstitution, final String pFallNr,
            final Long pProcessNumber, final Long pRequestStateShort, final String pRequestStateText, final String pUser) throws JCoException {

        SapExportResult result = null;
        String fallNr = pFallNr == null ? "" : pFallNr.trim();
        String processNr = null;
        String stateShort = null;
        String stateText = null;
        String user = null;

        LOG.log(Level.FINEST, "Test_vorgangsnummer = {0}", String.valueOf(pProcessNumber));
        if (pProcessNumber == null || pProcessNumber.equals(0L)) {
            return new SapExportResult("Vorgangsnummer ist leer.");
        } else {
            processNr = String.valueOf(pProcessNumber); 
            if(processNr != null && processNr.length() > 20) {
                processNr = processNr.substring(0,20);
            }
        }
        if (pRequestStateShort == null || pRequestStateShort.equals(0L)) {
            return new SapExportResult("ID des Anfrage-Status ist leer.");
        } else {
            stateShort = pRequestStateShort.toString();
            if(stateShort != null && stateShort.length() > 4) {
                stateShort = stateShort.substring(0,4);
            }
        }
        if (pRequestStateText == null || pRequestStateText.trim().isEmpty()) {
            return new SapExportResult("Text des Anfrage-Status ist leer.");
        } else {
            stateText = pRequestStateText;
            if(stateText != null && stateText.length() > 50) {
                stateText = stateText.substring(0,50);
            }
        }
        if (pInstitution == null || pInstitution.trim().isEmpty()) {
            return new SapExportResult("Informatioh zur Einrichtung ist leer.");
        }
        if (pUser == null || pUser.trim().isEmpty()) {
            LOG.log(Level.WARNING, "User-Name ist leer.");
        } else {
            user = pUser;
            if(user != null && user.length() > 50) {
                user = user.substring(0,50);
            }
        }
        if (fallNr.isEmpty()) {
            LOG.log(Level.WARNING, "Fallnummer ist leer.");
        }

        try {
            //Vervollständigung der Fallnummer auf 10 Stellen
            if (fallNr.length() < 10) {
                fallNr = fillFallnr(fallNr);
            }

            
            final JCoFunction sendCPXMDStateFunction = createFunction(Z_RFC_SET_CP_REQUEST_STATUS);
            JCoParameterList newParameter = sendCPXMDStateFunction.getImportParameterList();
            newParameter.setValue("P_EINRI", pInstitution);
            newParameter.setValue("P_FALNR", fallNr);
            newParameter.setValue("P_CPVORGANG", processNr);
            newParameter.setValue("P_CPSTATUS", stateShort);
            newParameter.setValue("P_CPSTATUSTEXT", stateText);
            newParameter.setValue("P_CPERUSR", user);
            LOG.log(Level.FINEST, "P_EINRI = {0}", pInstitution);
            LOG.log(Level.FINEST, "P_FALNR = {0}", fallNr);
            LOG.log(Level.FINEST, "P_CPVORGANG = {0}", processNr);
            LOG.log(Level.FINEST, "P_CPSTATUS = {0}", stateShort);
            LOG.log(Level.FINEST, "P_CPSTATUSTEXT = {0}", stateText);
            LOG.log(Level.FINEST, "P_CPERUSR = {0}", user);

            if (callFunction(sendCPXMDStateFunction)) {
                JCoParameterList res = sendCPXMDStateFunction.getExportParameterList();
		Object dbUpdated = res.getValue("P_DBUPDATED");
		if (dbUpdated != null && dbUpdated.toString().equalsIgnoreCase("X")) {
                    result = new SapExportResult("Der Checkpoint MD-Status wurde für den Fall '"+ fallNr +"' erfolgreich nach SAP übertragen!");
                    result.setState(0);
                } else {
		    result = new SapExportResult("Es gab ein SAP-internes Problem beim Export des MD-Status für "
                            + "den Fall '"+ fallNr +"'.\n Bitte wenden Sie sich an den Support!");
		    result.setState(1);
                }
            } else {
                Exception ex = sendCPXMDStateFunction.getException("NOT_ACTIVE");
                if (ex != null) {
                    LOG.log(Level.SEVERE, "Failed to send MD-Status", ex.getMessage());
                }
                return new SapExportResult("Der Aufruf der Methode zum Übertragen des MD-Status schlug fehl!");
            }
        } catch (JCoException ex) {
            LOG.log(Level.SEVERE, "Was not able to export/send MD-Status to SAP", ex);
            throw ex;
        } 
        finally {
            if (commitSAP()) {
                LOG.log(Level.INFO, "Send MD-Status successfully committed");
            } else {
                LOG.log(Level.SEVERE, "Send MD-Status was not committed!");
            }
            
        }
        return result;
    }    
    
    public static String getSapDate(final Date pDate) {
        if (pDate == null) {
            return "";
        }
        final DateFormat m_dtfSAP = new SimpleDateFormat("yyyyMMdd");
        //try {
        return m_dtfSAP.format(pDate);
//        } catch (ParseException ex) {
//            LOG.log(Level.SEVERE, "Cannot format date: " + pDate, ex);
//            return "";
//        }
    }

    public boolean commitSAP() throws JCoException {
        JCoFunction function = createFunction(BAPI_TRANSACTION_COMMIT);
        if (function != null) {
            JCoParameterList parameter = function.getImportParameterList();
            parameter.setValue("WAIT", "X");
            return callFunction(function);
        }
        return true;
    }

    
    private static String fillFallnr(final String pFallnr) {
        return leftPad(pFallnr, 10, '0');
    }

//	/**
//	 * Schreibt den Fehlertext in das Ergebniss
//	 * @param ex Exception
//	 * @param state int
//	 */
//	private void createErrorMessage(Exception ex, int state){
//		ExcException.createException(ex);
//		if (m_result!=null){
//			m_result.m_text.append(ex.getMessage());
//			m_result.state = state;
//		}
//	}
//    /**
//     *
//     * @param pFallnr
//     * @param pEinreichtung
//     * @return
//     * @throws JCoException
//     */
//    protected List<SapNleiSearchResult> getSAPCaseNLEIs(final String pFallnr, final String pEinreichtung) throws JCoException {
//        List<SapNleiSearchResult> results;
//        JCoFunction function = createFunction("ZLBH_CASE_SERVICES_GET_01");
//        //if (function == null && !RFC_CASE_SERVICES_GET_01.equals(RFC_CASE_SERVICES_GET_01)) {
//        //  function = createFunction(RFC_CASE_SERVICES_GET_01);
//        //}
//        //if (function != null) {
//        JCoParameterList parameter = function.getImportParameterList();
//        parameter.setValue("P_EINRI", pEinreichtung);
//        parameter.setValue("P_FALNR", pFallnr);
//        //if (isDebugMode()) {
//        //  myLogger.info("NLEI Aufruf " + fallnr + " - " + einri);
//        //}
//        callFunction(function);
//        JCoTable tblServices = function.getTableParameterList().getTable("E_SERVICES");
//        SapNleiSearchResult nlei;
//        int n = tblServices.getNumRows();
//        results = new ArrayList<>(n);
//        for (int i = 0; i < n; i++) {
//            SapProcedureSearchResult proc = new SapProcedureSearchResult();
//            tblServices.setRow(i);
//            nlei = new SapNleiSearchResult();
//            nlei.readFromTable(tblServices);
//            results.add(nlei);
//        }
//        //}
//        //}
//        return results;
//    }
    private static int toErbringungsart(final Object pValue) {
        if (pValue == null) {
            return 0;
        }
        try {
            return Integer.parseInt(pValue.toString());
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, "Zusatz-Key Erbringungsart konnte nicht gelesen werden", ex);
            return 0;
        }
    }

    /**
     * Ermittelt das Rechnungsdatum der Schlussrechnung aus dem §301-Datensatz
     *
     * @param pFunction JCO Function
     * @param pFallnr Fall-Nr.
     * @param fallid Fall-ID
     * @param pIkz IKZ
     * @return java.sql.Date
     */
    private static java.sql.Date readSAPBillingDateFrom301(final JCoFunction pFunction, final String pFallnr, final String pIkz)
    {
        java.sql.Date sapBillingDate = null;
        ArrayList<String> billMessages = new ArrayList<String>();
        JCoTable tabRefs = pFunction.getTableParameterList().getTable("E_NC301M_TAB");
        int n = tabRefs.getNumRows();
        LOG.log(Level.INFO, "Anzahl der Ergebnisse §301-Rechnungsobjekte", new Object[]{pFallnr, pIkz, n});
        for (int i = 0; i < n; i++) {
            tabRefs.setRow(i);
            String message = tabRefs.getString("MAIL");
            if(message != null) {
                billMessages.add(message);
            }
        }
        if(billMessages.size() > 0) 
        {
            ArrayList<String> sapBillingSegments = extractBillingSegmentsFrom301(billMessages);
            if(sapBillingSegments.size() > 0) 
            {
                sapBillingDate = extractBillingDatesFromSegments(sapBillingSegments);
            }            
        }
        return sapBillingDate;
    }

    /**
     * Extrahiert das REC-Segment aus der §301-Nachricht
     *
     * @param pBillMessages §301-Nachrichten
     * @return ArrayList<String>
     */
    private static ArrayList<String> extractBillingSegmentsFrom301(final ArrayList<String> pBillMessages) {

        ArrayList<String> extractedBillingSegments = new ArrayList<String>();

        for(int i = 0, n = pBillMessages.size(); i < n; i++)
        {
            String billMessage = pBillMessages.get(i);
            if(billMessage != null)
            {
                String[] arySegments = billMessage.split("\'");
                if (arySegments != null && arySegments.length > 0)
                {
                    for(int j = 0, m = arySegments.length; j < m; j++)
                    {
                        String segment = arySegments[j];
                        if (segment != null && segment.startsWith("REC+")) {
                            extractedBillingSegments.add(segment);
                            break;
                        }
                    }
                }
            }
        }
        return extractedBillingSegments;
    }

    /**
     * Extrahiert das Rechnnungsdatum der REC-Segmente aus der §301-Nachricht der Schlussrechung
     *
     * @param pBillSegments Rechungssegmente der §301-Nachrichten
     * @param pFallnr Fall-Nr.
     * @param fallid Fall-ID
     * @param pIkz IKZ
     * @return java.sql.Date
     */
    private static java.sql.Date extractBillingDatesFromSegments(final ArrayList<String> pBillSegments)
    {
        java.sql.Date extractedBillingDate = null;
        String strBillDate = null;

        for(int i = 0, n = pBillSegments.size(); i < n; i++)
        {
            String billSegement = pBillSegments.get(i);
            if(billSegement != null)
            {
                String[] aryValues = billSegement.split("\\+");
                if (aryValues != null && aryValues.length >= 4)
                {
                    String billKeyNumber = aryValues[3];
                    if(billKeyNumber != null && (billKeyNumber.equals("02") || billKeyNumber.equals("52"))) {
                        strBillDate = aryValues[2];
                        break;
                    }
                }
            }
        }

        if(strBillDate != null)
        {
            try {
                extractedBillingDate = StrUtils.parseDate(strBillDate, FORMAT_DATE);
            } catch (ParseException e) {
                LOG.log(Level.SEVERE, "Error on parsing date value from " + strBillDate, e);
            }
        }
        return extractedBillingDate;
    }

        private String checkSapFallNr(String fnr) {
                char c;
                String tt = "";
                int n = fnr != null ? fnr.length() : 0;
                for (int i = 0; i < n; i++) {
                        c = fnr.charAt(i);
                        if (c != '0') {
                                tt = fnr.substring(i, n);
                                break;
                        }
                }
                return tt;
        }

    public boolean isDoAnonymize() {
        return doAnonymize;
    }
}
