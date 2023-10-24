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
package de.lb.cpx.sap.properties;

import de.lb.cpx.sap.container.AbteilungMapContainer;
import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author niemeier
 */
public class SapProperties {

    private static final CpxSystemPropertiesInterface CPX_PROPS = CpxSystemProperties.getInstance();

    private static final Logger LOG = Logger.getLogger(SapProperties.class.getName());

    public static final String SERVER_RESOURCE_PATH = "sap_import//";
    protected static final String SERVER_RESOURCE_NAME = SERVER_RESOURCE_PATH + "bapi_rename";
    protected static final String MAP301_RESOURCE_NAME = SERVER_RESOURCE_PATH + "bapi_map301";
    public static final String PROPERTIES_RESOURCE_NAME = SERVER_RESOURCE_PATH + "bapi_properties";

    public static final String PROPKEY_STATE_CLOSE = "STATE_CLOSE";
    public static final String PROPKEY_STATE_CORRECT = "STATE_CORRECT";
    public static final String PROPKEY_STATE_TRIALS = "STATE_TRIALS";
    public static final String PROPKEY_STORNO_REASON = "STORNO_REASON";
    public static final String PROPKEY_LOKALISATION_L = "LOKALISATION_L";
    public static final String PROPKEY_LOKALISATION_R = "LOKALISATION_R";
    public static final String PROPKEY_LOKALISATION_B = "LOKALISATION_B";
    public static final String PROPKEY_CASE_STATE = "GET_CASE_STATE";
    public static final String PROPKEY_CASE_REFERENCES = "GET_CASE_REFERENCES";
    public static final String PROPKEY_CASE_ENTG = "GET_CASE_ENTG";
    public static final String PROPKEY_CASE_NLEI = "GET_CASE_NLEI";
    public static final String PROPKEY_CASE_OPS_OE_NLEI = "GET_OPS_OE_NLEI";
    public static final String PROPKEY_LOAD_BAPI_CASES = "LOAD_BAPI_CASES";
    public static final String PROPKEY_LOAD_BAPI_CASES_SUFF = "LBAPI";
    public static final String PROPKEY_LAST_UPDATE_DATE = "LAST_UPDATE_DATE";
    public static final String PROPKEY_GWHOST = "GWHOST";
    public static final String PROPKEY_GWSERV = "GWSERV";
    public static final String PROPKEY_MSHOST = "MSHOST";
    public static final String PROPKEY_R3NAME = "R3NAME";
    public static final String PROPKEY_R3GROUP = "R3GROUP";
    public static final String PROPKEY_R3URL = "R3URL";
    public static final String PROPKEY_LOGINTYPE = "LOGINTYPE";
    public static final String PROPKEY_CASE_FI = "GET_CASE_FI";
    public static final String PROPKEY_CASE_RESETLIST = "CASE_RESETLIST";
    public static final String PROPKEY_CASE_RESETLIST_SUFF = "RBAPI";
    public static final String PROPKEY_CHECK_PRIMSEK = "CHECK_PRIMSEK";
    public static final String PROPKEY_IMPORT_STAT = "IMPORT_STAT";
    public static final String PROPKEY_IMPORT_TEILSTAT = "IMPORT_TEILSTAT";
    public static final String PROPKEY_DEBUGMODE = "DebugMode";
    public static final String PROPKEY_SSTVERSION = "SSTVERSION";
    public static final String PROPKEY_FALLTYP_MDK = "FALLTYP_MDK";
    public static final String PROPKEY_RESET_COUNT = "RESET_COUNT";
    public static final String PROPKEY_COMPUTE_WA = "COMPUTE_WA";
    public static final String PROPKEY_LOAD_CASE_BY_UPDATE_DATE = "LOAD_CASE_BY_UPDATE_DATE";
    public static final String PROPKEY_DRGCHECK = "CHECK_SAPDRG";

    public static final String PROPKEY_GET_LABOR = "LABOR_IMPORT";
    public static final String PROPKEY_LABOR_DOCTYPE = "LABOR_DOKAR";
    public static final String PROPKEY_LABOR_UPDATEKEY = "LABOR_UPDATEKEY";
    public static final String PROPKEY_LABOR_VALSTATUS = "LABOR_VALSTATUS";
    public static final String PROPKEY_LABOR_VALTYPE = "LABOR_VALTYPE";

    public static final String PROPKEY_GET_KAIN = "KAIN_IMPORT";

    public static final String PROPKEY_IKZ_METHOD = "IKZ_METHOD";

    public static final String PROPKEY_AUTH_CONTROL = "AUTH_CONTROL";
    public static final String PROPKEY_AUTH_NBEW_AKT = "AUTH_NBEW_AKT";
    public static final String PROPKEY_AUTH_NDIA_ALL = "AUTH_NDIA_ALL";
    public static final String PROPKEY_AUTH_NDIA_ORG = "AUTH_NDIA_ORG";

    public static final String PROPKEY_CHECK_ENDDAT = "CHECK_ENDDAT";
    public static final String PROPKEY_CHECK_ENDDAT_DELAY = "CHECK_ENDDAT_DELAY";

    public static final String PROPKEY_IMPORT_AMBULANT_OP = "IMPORT_AMB_OP";
    public static final String PROPKEY_IMPORT_AMBULANT_CANCELS = "IMPORT_AMB_CANCEL";
    public static final String PROPERTY_ONLY_READ_CASENUMBERS = "ONLY_READ_CASENUMBERS";
    public static final String PROPERTY_ONLY_CASENUMBER_DEPARTMENT = "ONLY_CASENUMBER_DEPARTMENT";
    public static final String PROPERTY_MAX_BLOCK_SIZE = "MAX_BLOCK_SIZE";
    protected String mCloseState = null;
    protected String mCorrectState = null;
    protected String mStornoGrund = null;

    protected String mLokR = null;
    protected String mLokL = null;
    protected String mLokB = null;

    private Map<String, String> mOpsVersions;
    private Map<String, String> mIcdVersions;
    private Map<String, String> mDrgVersions;
    private Map<String, String> mKisStates;
    private Map<String, String> mKtartTypes;
    private Map<String, String> mDivCaseMsg;
    private Map<String, String> mLoadBAPICase;
    private Map<String, Boolean> mResetBAPICase;
    private int mWithSAPState;
    private int mWithSAPCaseReferenz;
    private int mWithSAPFI;
    private int mResetList;
    private int mWithEntg;
    private int mWithNLEI;
    private int mOpsOeNlei;
    private int mCheckPrimSek;
    private String mBabiCaseHolder;
    private String mLastUpdateDate;
    private String mGwHost;
    private String mGwServ;
    private String mR3Group;
    private int mLoginType;
    private int mImportStationaer;
    private int mImportTeilStationaer;
    private int mDebugMode;
    protected int mStateTrials = -1;
    protected boolean mHasFalltyp_MDK = false;
    protected int mCheckSAPDRG = -1;
    private int mSstVersion;
    private String mFalltypMDK;
    private int mResetCount;
    private int mComputeWA;
    private int mCasenrByUpdateDate;
    private int mWithLab;
    private String mLabDocType;
    private String mLabUpdateKey;
    private String mLabValStatus;
    private String mLabValType;
    private int mWithKAIN;
    private int mAuthControl;
    private String mAuthNbewAkt;
    private String mAuthNdiaAll;
    private String mAuthNdiaOrg;
    private int mCheckEndDat;
    private int mCheckEndDatDelay;
    private boolean mIkzMethodUsed;
    private String mIkzMethod;
    private String mCaseNumber4Department;
    private int mMaxBlockSize;

    protected int mAmbulantOPs = -1;
    protected int mAmbulantCancels = -1;
    protected int mOnlyReadCaseNumbers = -1;

    private Map<String, String> mP301AufnahmeartMap = null;
    private Map<String, String> mOpBewegungsartMap = null;
    private Map<String, AbteilungMapContainer> mP301AbteilungenMap = null;

    public final String hosIdent;
//    public final License license;
//    private static boolean libraryLoaded = false;

    /**
     *
     * @param pHosIdent hospital identifier
     */
    public SapProperties(final String pHosIdent) {
        hosIdent = pHosIdent == null ? "" : pHosIdent.trim();
        if (hosIdent.isEmpty()) {
            throw new IllegalArgumentException("No hospital ident passed!");
        }
//        license = pLicense;
//        final String licensePathTmp = cpxProps.getCpxServerLicenseDir() + "\\" + LicenseWriter.DEFAULT_LICENSE_FILENAME;
//        LOG.info("Load license from " + licensePathTmp + "...");
//        final String licenseFilename = licensePathTmp;
//        //LOG.info("Will import to database: " + caseDb);
//        license = License.loadFromLicenseFile(licenseFilename);
//        LOG.info("License found: " + String.valueOf(license));
//        if (!license.isValid()) {
//            throw new IllegalArgumentException("This license file is invalid: " + pLicense.);
//        }
//        if (!license.isHospitalAllowed(hosIdent)) {
//            throw new IllegalArgumentException("With this license you cannot import hospital cases for the hospital identifier '" + hosIdent + "'. You have to purchase an upgrade for CPX or choose another license!");
//        }
//        loadLibrary();
//        unloadLibrary();
//        final String sapJarFilename = "sapjco3.jar";
//        final String sapJarPathTmp = cpxProps.getCpxServerLibraryDir();
//        final String sapJarPath = sapJarPathTmp + sapJarFilename;
//        LOG.info("Load sapjco3.jar " + sapJarPath + "...");
//        try {
//            ClassLoader classLoader = new URLClassLoader(new URL[]{new File(sapJarPath).toURI().toURL()});
//            LOG.info("sapjco3.jar loaded!");
//        } catch (MalformedURLException ex) {
//            LOG.log(Level.SEVERE, "Failed to load sapjco3.jar", ex);
//        }

        loadSapProperties();
        loadSapParams();
    }

    /*
    private static synchronized void unloadLibrary() {
        final String sapDllFilename = "sapjco3.dll";
        NativeLibrary lib = NativeLibrary.getInstance(sapDllFilename);
        lib.dispose();
    }
     */

 /*
    private static synchronized void loadLibrary() {
        if (libraryLoaded) {
            LOG.log(Level.FINEST, "Library is already loaded");
        } else {
            final String sapDllFilename = "sapjco3.dll";
            final String sapDllPathTmp = CPX_PROPS.getCpxServerLibraryDir();
            final String sapDllPath = sapDllPathTmp + sapDllFilename;
            LOG.log(Level.INFO, "Load Library " + sapDllPath + "...");
            try {
                System.load(sapDllPath);
                LOG.log(Level.INFO, "Library successfully loaded!");
            } catch (UnsatisfiedLinkError ex) {
                if (ex.getMessage() != null && ex.getMessage().contains("already loaded in another classloader")) {
                    LOG.log(Level.INFO, "libary is already loaded: {0}", sapDllFilename);
                    LOG.log(Level.FINEST, null, ex);
                } else {
                    throw ex;
                }
            }
            libraryLoaded = true;
        }
    }
     */
    /**
     * @return the mLabValStatus
     */
    public String getLabValStatus() {
        return mLabValStatus;
    }

    /**
     * @param mLabValStatus the mLabValStatus to set
     */
    public void setLabValStatus(String mLabValStatus) {
        this.mLabValStatus = mLabValStatus;
    }

    /**
     * @return the mLabValType
     */
    public String getLabValType() {
        return mLabValType;
    }

    /**
     * @param mLabValType the mLabValType to set
     */
    public void setLabValType(String mLabValType) {
        this.mLabValType = mLabValType;
    }

    /**
     * @return the mP301AufnahmeartMap
     */
    public Map<String, String> getP301AufnahmeartMap() {
        return mP301AufnahmeartMap;
    }

    /**
     * @param mP301AufnahmeartMap the mP301AufnahmeartMap to set
     */
    public void setP301AufnahmeartMap(Map<String, String> mP301AufnahmeartMap) {
        this.mP301AufnahmeartMap = mP301AufnahmeartMap;
    }

    /**
     * @return the mOpBewegungsartMap
     */
    public Map<String, String> getOpBewegungsartMap() {
        return mOpBewegungsartMap;
    }

    /**
     * @param mOpBewegungsartMap the mOpBewegungsartMap to set
     */
    public void setOpBewegungsartMap(Map<String, String> mOpBewegungsartMap) {
        this.mOpBewegungsartMap = mOpBewegungsartMap;
    }

    /**
     * @return the mP301AbteilungenMap
     */
    public Map<String, AbteilungMapContainer> getmP301AbteilungenMap() {
        return mP301AbteilungenMap;
    }

    /**
     * @param mP301AbteilungenMap the mP301AbteilungenMap to set
     */
    public void setP301AbteilungenMap(Map<String, AbteilungMapContainer> mP301AbteilungenMap) {
        this.mP301AbteilungenMap = mP301AbteilungenMap;
    }

    private File getResourceFile(final String pFilename) {
        String filename = pFilename == null ? "" : pFilename.trim();
        if (filename.isEmpty()) {
            throw new IllegalArgumentException("Filename is null or empty!");
        }
        String ext = ".properties";
        if (!filename.endsWith(ext)) {
            filename += ext;
        }

        File file = new File(CPX_PROPS.getCpxHome() + filename);
        if (!file.exists()) {
            LOG.log(Level.SEVERE, "Resource file does not exist: {0}", file.getAbsolutePath());
        } else {
            if (!file.isFile()) {
                LOG.log(Level.SEVERE, "Resource is not a file: {0}", file.getAbsolutePath());
            } else {
                if (!file.canRead()) {
                    LOG.log(Level.SEVERE, "Cannot open resource file (no permissions to read): {0}", file.getAbsolutePath());
                }
            }
        }
        return file;
    }

    private void loadSapParams() {
        File file = getResourceFile(MAP301_RESOURCE_NAME);
        LOG.log(Level.INFO, "Properties werden ausgelesen: {0}", file.getAbsolutePath());
        final ResourceBundle res;
        try (FileInputStream fis = new FileInputStream(file)) {
            res = new PropertyResourceBundle(fis);
            //res = ResourceBundle.getBundle(MAP301_RESOURCE_NAME, Locale.getDefault());
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Fehler beim Lesen der properties-Datei: " + e.getMessage(), e);
            return;
        }
        if (getmP301AbteilungenMap() == null || getP301AufnahmeartMap() == null || getOpBewegungsartMap() == null) {
            setP301AbteilungenMap(new HashMap<>());
            setP301AufnahmeartMap(new HashMap<>());
            setOpBewegungsartMap(new HashMap<>());
            Enumeration<String> enumer = res.getKeys();
            while (enumer.hasMoreElements()) {
                String key = enumer.nextElement();
                String value = res.getString(key);
                LOG.log(Level.INFO, "\tKey: {0}, Value: {1}", new Object[]{key, value});
//                try {
                if (key.startsWith("AufnahmeAnlass")) {
                    key = key.substring(key.indexOf('.') + 1);
                    getP301AufnahmeartMap().put(key, value);
                } else if (key.startsWith("OPBewegungsArten")) {
                    String[] vals = value.split("[\\s]*,[\\s]*");
                    for (String val : vals) {
                        getOpBewegungsartMap().put(val, "1");
                    }
                } else {
                    String[] vals = value.split("[\\s]*,[\\s]*");
                    int lg = vals.length;
                    AbteilungMapContainer a = new AbteilungMapContainer();
                    if (lg >= 1) {
                        a.setP301(vals[0]);
                    } else {
                        a.setP301("");
                    }
                    if (lg >= 2) {
                        a.setTyp(vals[1]);
                    } else {
                        a.setTyp("");
                    }
                    if (lg >= 3) {
                        a.setTypSchluessel(vals[2]);
                    } else {
                        a.setTypSchluessel("0");
                    }
                    getmP301AbteilungenMap().put(key, a);
                    //if (this.isDebugMode() && this.myLogger != null) {
                    //  myLogger.info("Abteilunghash: Key=" + key + ", Val=" + a.p301);
                    //}
                }
//                } catch (Exception e) {
//                    //e.printStackTrace();
//                    LOG.log(Level.SEVERE, "This should not happen...", e);
//                }
            }
        }
    }

    private void loadSapProperties() {
        mOpsVersions = new HashMap<>();
        mIcdVersions = new HashMap<>();
        mDrgVersions = new HashMap<>();
        mKisStates = new HashMap<>();
        mKtartTypes = new HashMap<>();
        mDivCaseMsg = new HashMap<>();
        mLoadBAPICase = new HashMap<>();
        mResetBAPICase = new HashMap<>();
        mWithSAPState = 0;
        mWithSAPCaseReferenz = 0;
        mWithSAPFI = 0;
        mResetList = 0;
        mWithEntg = 0;
        mWithNLEI = 0;
        mOpsOeNlei = 0;
        mCheckPrimSek = 0;
        mBabiCaseHolder = "";
        mLastUpdateDate = "";
        mGwHost = null;
        mGwServ = null;
        mR3Group = null;
        mLoginType = 0;
        this.mImportStationaer = 1;
        this.mImportTeilStationaer = 1;
        mDebugMode = 0;
        mSstVersion = 1;
        mFalltypMDK = "";
        mResetCount = 1000;
        mComputeWA = 0;
        mCasenrByUpdateDate = -1;
        mWithLab = 0;
        mLabDocType = "LAB";
        mLabUpdateKey = "yyyyMMddhhmmss";
        setLabValStatus("");
        setLabValType("");
        mWithKAIN = 0;
        mAuthControl = 0;
        mAuthNbewAkt = "";
        mAuthNdiaAll = "";
        mAuthNdiaOrg = "";
        mCheckEndDat = 0;
        mCheckEndDatDelay = 0;
        mIkzMethodUsed = false;
        mIkzMethod = "";
        mOnlyReadCaseNumbers = 0;
        mCaseNumber4Department = "";
        mMaxBlockSize = 0;

        final ResourceBundle resour;

        File file = getResourceFile(PROPERTIES_RESOURCE_NAME);
        LOG.log(Level.INFO, "Properties werden ausgelesen: {0}", file.getAbsolutePath());
        try (FileInputStream fis = new FileInputStream(file)) {
            resour = new PropertyResourceBundle(fis);
            //res = ResourceBundle.getBundle(MAP301_RESOURCE_NAME, Locale.getDefault());
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Fehler beim Lesen der properties-Datei: " + e.getMessage(), e);
            return;
        }

//    try {
//      resour = ResourceBundle.getBundle(SAPClass.PROPERTIES_RESOURCE_NAME, Locale.getDefault());
//    } catch (Exception e) {
//      mCloseState = "ABGE";
//      mCorrectState = "ZUPR";
//      if (this.myLogger != null) {
//        myLogger.fatal("Fehler beim Lesen der properties-Datei", e);
//      }
//      return;
//    }
//        try {
        String val = getSAPPropertiesKeyValue(resour, PROPKEY_STATE_CLOSE);
        if (!val.isEmpty()) {
            mCloseState = val;
        } else {
            mCloseState = "ABGE";
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_STORNO_REASON);
        if (!val.isEmpty()) {
            mStornoGrund = val;
        } else {
            mStornoGrund = "";
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_LOGINTYPE);
        if (!val.isEmpty()) {
            try {
                this.mLoginType = Integer.parseInt(val);
            } catch (NumberFormatException ex) {
//                if (LOG != null) {
                LOG.log(Level.SEVERE, "Fehler beim Lesen der properties-Datei (Wert: " + val + "): " + ex.getMessage(), ex);
//                }
            }
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_STATE_TRIALS);
        if (!val.isEmpty()) {
            try {
                this.mStateTrials = Integer.parseInt(val);
            } catch (NumberFormatException ex) {
//                if (LOG != null) {
                LOG.log(Level.SEVERE, "Fehler beim Lesen der properties-Datei Wert STATE-Trials (Wert: " + val + "): " + ex.getMessage(), ex);
//                }
            }
        } else {
            mStateTrials = 0;
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_GWHOST);
        if (!val.isEmpty()) {
            mGwHost = val;
        } else {
            mGwHost = null;
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_GWSERV);
        if (!val.isEmpty()) {
            mGwServ = val;
        } else {
            mGwServ = null;
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_R3GROUP);
        if (!val.isEmpty()) {
            mR3Group = val;
        } else {
            mR3Group = null;
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_DEBUGMODE);
        if ("true".equalsIgnoreCase(val)) {
            mDebugMode = 1;
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_SSTVERSION);
        if (!val.isEmpty() && !"1".equals(val)) {
            try {
                mSstVersion = Integer.parseInt(val);
            } catch (NumberFormatException ex) {
//                if (LOG != null) {
                LOG.log(Level.SEVERE, "cannot parse value '" + val + "' as integer: " + ex.getMessage(), ex);
//                } else {
//                    //ex.printStackTrace();
//                    LOG.log(Level.SEVERE, "This should not happen...", ex);
//                }
            }
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_FALLTYP_MDK);
        if (!val.isEmpty()) {
            mFalltypMDK = val;
            mHasFalltyp_MDK = true;
        } else {
            mFalltypMDK = "";
            mHasFalltyp_MDK = false;
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_RESET_COUNT);
        if (!val.isEmpty()) {
            try {
                mResetCount = Integer.parseInt(val);
            } catch (NumberFormatException ex) {
                mResetCount = 1000;
//                if (LOG != null) {
                LOG.log(Level.SEVERE, "cannot parse value '" + val + "' as integer: " + ex.getMessage(), ex);
//                } else {
//                    //ex.printStackTrace();
//                    LOG.log(Level.SEVERE, "This should not happen...", ex);
//                }
            }
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_COMPUTE_WA);
        if ("true".equalsIgnoreCase(val)) {
            mComputeWA = 1;
        }
        if (mResetCount <= 0) {
            mResetCount = 1000;
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_STATE_CORRECT);
        if (!val.isEmpty()) {
            mCorrectState = val;
        } else {
            mCorrectState = "ZUPR";
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_LOKALISATION_L);
        if (!val.isEmpty()) {
            mLokL = val;
        } else {
            mLokL = "L";
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_LOKALISATION_R);
        if (!val.isEmpty()) {
            mLokR = val;
        } else {
            mLokR = "R";
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_LOKALISATION_B);
        if (!val.isEmpty()) {
            mLokB = val;
        } else {
            mLokB = "B";
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_CASE_STATE);
        if ("true".equalsIgnoreCase(val)) {
            mWithSAPState = 1;
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_CASE_REFERENCES);
        if ("true".equalsIgnoreCase(val)) {
            mWithSAPCaseReferenz = 1;
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_CASE_FI);
        if ("true".equalsIgnoreCase(val)) {
            mWithSAPFI = 1;
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_CASE_RESETLIST);
        if ("true".equalsIgnoreCase(val)) {
            mResetList = 1;
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_CASE_ENTG);
        if ("true".equalsIgnoreCase(val)) {
            mWithEntg = 1;
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_CASE_NLEI);
        if ("true".equalsIgnoreCase(val)) {
            mWithNLEI = 1;
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_CASE_OPS_OE_NLEI);
        if ("true".equalsIgnoreCase(val)) {
            mOpsOeNlei = 1;
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_CHECK_PRIMSEK);
        if ("true".equalsIgnoreCase(val)) {
            mCheckPrimSek = 1;
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_LOAD_BAPI_CASES);
        if (!val.isEmpty()) {
            mBabiCaseHolder = val;
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_LAST_UPDATE_DATE);
        if (!val.isEmpty()) {
            mLastUpdateDate = val;
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_IMPORT_STAT);
        if ("false".equalsIgnoreCase(val)) {
            mImportStationaer = 0;
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_IMPORT_TEILSTAT);
        if ("false".equalsIgnoreCase(val)) {
            mImportTeilStationaer = 0;
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_GET_LABOR);
        if ("true".equalsIgnoreCase(val)) {
            mWithLab = 1;
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_LABOR_DOCTYPE);
        if (!val.isEmpty()) {
            mLabDocType = val;
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_LABOR_UPDATEKEY);
        if (!val.isEmpty()) {
            mLabUpdateKey = val;
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_LABOR_VALSTATUS);
        if (!val.isEmpty()) {
            setLabValStatus(val);
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_LABOR_VALTYPE);
        if (!val.isEmpty()) {
            setLabValType(val);
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_IKZ_METHOD);
        if (!val.isEmpty()) {
            mIkzMethodUsed = true;
            mIkzMethod = val;
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_AUTH_CONTROL);
        if ("true".equalsIgnoreCase(val)) {
            mAuthControl = 1;
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_AUTH_NBEW_AKT);
        if (!val.isEmpty()) {
            mAuthNbewAkt = val;
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_AUTH_NDIA_ALL);
        if (!val.isEmpty()) {
            mAuthNdiaAll = val;
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_AUTH_NDIA_ORG);
        if (!val.isEmpty()) {
            mAuthNdiaOrg = val;
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_CHECK_ENDDAT);
        if ("true".equalsIgnoreCase(val)) {
            mCheckEndDat = 1;
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_CHECK_ENDDAT_DELAY);
        if (!val.isEmpty()) {
            try {
                mCheckEndDatDelay = Integer.parseInt(val);
            } catch (NumberFormatException ex) {
                mCheckEndDatDelay = 0;
//                if (LOG != null) {
                LOG.log(Level.SEVERE, "cannot parse value '" + val + "' as integer: " + ex.getMessage(), ex);
//                } else {
//                    LOG.log(Level.SEVERE, "This should not happen...", ex);
//                    //ex.printStackTrace();
//                }
            }
        }

        val = getSAPPropertiesKeyValue(resour, PROPKEY_IMPORT_AMBULANT_OP);
        if (!val.isEmpty() && "true".equalsIgnoreCase(val)) {
            mAmbulantOPs = 1;
        }

        val = getSAPPropertiesKeyValue(resour, PROPKEY_IMPORT_AMBULANT_CANCELS);
        if (!val.isEmpty() && "true".equalsIgnoreCase(val)) {
            mAmbulantCancels = 1;
        }

        val = getSAPPropertiesKeyValue(resour, PROPERTY_MAX_BLOCK_SIZE);
        if (!val.isEmpty()) {
            try {
                mMaxBlockSize = Integer.parseInt(val);
            } catch (NumberFormatException ex) {
                mMaxBlockSize = 0;
//                if (LOG != null) {
                LOG.log(Level.SEVERE, "cannot parse value '" + val + "' as integer: " + ex.getMessage(), ex);
//                } else {
//                    //ex.printStackTrace();
//                    LOG.log(Level.SEVERE, "This should not happen...", ex);
//                }
            }
        }

        val = getSAPPropertiesKeyValue(resour, PROPKEY_GET_KAIN);
        if (!val.isEmpty() && "true".equalsIgnoreCase(val)) {
            mWithKAIN = 1;
        }

        String key;
        String keyVal;
        String[] vals;
        for (Enumeration<String> en = resour.getKeys(); en.hasMoreElements();) {
            key = en.nextElement();
            if (key != null) {
                keyVal = key;
                if (keyVal.startsWith("OPSVERSION")) {
                    vals = keyVal.split("_");
                    if (vals.length > 1) {
                        mOpsVersions.put(vals[1], getSAPPropertiesKeyValue(resour, keyVal));
                    }
                } else if (keyVal.startsWith("ICDVERSION")) {
                    vals = keyVal.split("_");
                    if (vals.length > 1) {
                        mIcdVersions.put(vals[1], getSAPPropertiesKeyValue(resour, keyVal));
                    }
                } else if (keyVal.startsWith("DRGVERSION")) {
                    vals = keyVal.split("_");
                    if (vals.length > 1) {
                        mDrgVersions.put(vals[1], getSAPPropertiesKeyValue(resour, keyVal));
                    }
                } else if (keyVal.startsWith("CASESTATE")) {
                    vals = keyVal.split("_");
                    if (vals.length > 1) {
                        mKisStates.put(vals[1], getSAPPropertiesKeyValue(resour, keyVal));
                    }
                } else if (keyVal.startsWith("TN18")) {
                    vals = keyVal.split("_");
                    if (vals.length > 1) {
                        mKtartTypes.put(getSAPPropertiesKeyValue(resour, keyVal), vals[1]);
                    }
                } else if (keyVal.startsWith("DIVMSG")) {
                    vals = keyVal.split("_", 2);
                    if (vals.length > 1) {
                        mDivCaseMsg.put(vals[1], getSAPPropertiesKeyValue(resour, keyVal));
                    }
                } else if (keyVal.startsWith(PROPKEY_LOAD_BAPI_CASES_SUFF)) {
                    vals = keyVal.split("_", 2);
                    if (vals.length > 1) {
                        mLoadBAPICase.put(vals[1], getSAPPropertiesKeyValue(resour, keyVal));
                    }
                }
            }
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_LOAD_CASE_BY_UPDATE_DATE);
        if ("true".equalsIgnoreCase(val)) {
            mCasenrByUpdateDate = 1;
        } else {
            mCasenrByUpdateDate = 0;
        }
        val = getSAPPropertiesKeyValue(resour, PROPKEY_DRGCHECK);
        if (!val.isEmpty() && "FALSE".equalsIgnoreCase(val)) {
            mCheckSAPDRG = 0;
        }

        val = getSAPPropertiesKeyValue(resour, PROPERTY_ONLY_READ_CASENUMBERS);
        if (!val.isEmpty() && "true".equalsIgnoreCase(val)) {
            mOnlyReadCaseNumbers = 1;
        }
        val = getSAPPropertiesKeyValue(resour, PROPERTY_ONLY_CASENUMBER_DEPARTMENT);
        if (!val.isEmpty()) {
            mCaseNumber4Department = val;
        }
    }

    private String getSAPPropertiesKeyValue(final ResourceBundle pResource, final String pKey) {
        try {
            final String result = pResource.getString(pKey);
            return result == null ? "" : result.trim();
        } catch (MissingResourceException ex) {
            LOG.log(Level.WARNING, "Cannot get resource property for key ''{0}''. Key does not exist!", pKey);
            LOG.log(Level.FINEST, MessageFormat.format("Cannot get resource property for key ''{0}''. Key does not exist!", pKey), ex);
            return "";
        }
    }

    /**
     * Gibt den SAP-Key für die Lokalisation links zurück
     *
     * @return String
     */
    protected String getSAPLokalisationLeft() {
        if (mLokL == null) {
            this.loadSapProperties();
        }
        return mLokL;
    }

    /**
     * Gibt den SAP-Key für die Lokalisation rechts zurück
     *
     * @return String
     */
    protected String getSAPLokalisationRight() {
        if (mLokR == null) {
            this.loadSapProperties();
        }
        return mLokR;
    }

    /**
     * Gibt den SAP-Key für die Lokalisation beidseitig zurück
     *
     * @return String
     */
    protected String getSAPLokalisationBoth() {
        if (mLokB == null) {
            this.loadSapProperties();
        }
        return mLokB;
    }

    /**
     * Gibt zum CP ICD Schlüssel den SAP Key zurück
     *
     * @param cpversion String
     * @return String
     */
    public String getSAPICDVersion(String cpversion) {
        if (mIcdVersions == null) {
            this.loadSapProperties();
        }
        return this.mIcdVersions.get(cpversion);
    }

    /**
     * Gibt zum CP OPS Schlüssel den SAP Key zurück
     *
     * @param cpversion String
     * @return Object
     */
    public Object getSAPOPSVersion(String cpversion) {
        if (mOpsVersions == null) {
            this.loadSapProperties();
        }
        return this.mOpsVersions.get(cpversion);
    }

    /**
     * Gibt zum CP DRG Schlüssel den SAP Key zurück
     *
     * @param cpversion String
     * @return Object
     */
    public Object getSAPDRGVersion(String cpversion) {
        if (mDrgVersions == null) {
            this.loadSapProperties();
        }
        return this.mDrgVersions.get(cpversion);
    }

    /**
     * Gibt zum CP Key zum SAP Status zurück
     *
     * @param sapString String
     * @return String
     */
    public String getSAPKISState(String sapString) {
        if (mKisStates == null) {
            this.loadSapProperties();
        }
        Object obj = mKisStates == null ? null : mKisStates.get(sapString);
        if (obj != null) {
            return obj.toString();
        } else {
            return "0";
        }
    }

    /**
     *
     * @return map of cost unit types
     */
    public Map<String, String> getSAPKTArtTypes() {
        if (this.mKtartTypes == null) {
            this.loadSapProperties();
        }
        return mKtartTypes;
    }

    /**
     *
     * @return map of case messages
     */
    protected Map<String, String> getDivCaseMsg() {
        if (mDivCaseMsg == null) {
            this.loadSapProperties();
        }
        return mDivCaseMsg;
    }

    /**
     *
     * @param mandant client
     * @param institut institution
     * @return div case
     */
    public String getDivCaseDatabase(String mandant, String institut) {
        Map<String, String> hm = getDivCaseMsg();
        Object obj = hm.get(mandant + "_" + institut);
        if (obj != null) {
            return obj.toString();
        } else {
            return null;
        }
    }

    /**
     * Gibt an, ob der SAP-Fallstatus übetragen werden soll
     *
     * @return boolean
     */
    public boolean importSAPState() {
        if (mWithSAPState < 0) {
            this.loadSapProperties();
        }
        return (mWithSAPState == 1);
    }

    /**
     * Fallreferenzen übertragen
     *
     * @return boolean
     */
    public boolean importSAPCaseReferences() {
        if (mWithSAPCaseReferenz < 0) {
            this.loadSapProperties();
        }
        return (mWithSAPCaseReferenz == 1);
    }

    /**
     * FI Daten übernehmen
     *
     * @return boolean
     */
    public boolean importSAPCaseFI() {
        if (mWithSAPFI < 0) {
            this.loadSapProperties();
        }
        return (mWithSAPFI == 1);
    }

    /**
     *
     * @param dbName database name
     * @return resetted
     */
    public boolean resetSAPCaseList(String dbName) {
        if (mResetList < 0) {
            this.loadSapProperties();
        }
        if (mResetBAPICase != null) {
            Boolean b = mResetBAPICase.get(dbName);
            if (b != null) {
                return b;
            } else {
                return (mResetList == 1);
            }
        } else {
            return (mResetList == 1);
        }
    }

    /**
     * Entgelte nach §301 auslesen
     *
     * @return boolean
     */
    public boolean importSAPCaseEntg() {
        if (mWithEntg < 0) {
            this.loadSapProperties();
        }
        return (mWithEntg == 1);
    }

    /**
     *
     * @return with nlei?
     */
    public boolean importSAPCaseNLEI() {
        if (mWithNLEI < 0) {
            this.loadSapProperties();
        }
        return (mWithNLEI == 1);
    }

    /**
     *
     * @return ops nlei?
     */
    public boolean importSAP_OPS_OEFromNLEI() {
        if (mOpsOeNlei < 0) {
            this.loadSapProperties();
        }
        return (mOpsOeNlei == 1);
    }

    /**
     * Laborwerte aus SAP auslesen
     *
     * @return with labor data?
     */
    public boolean importLabor() {
        if (mWithLab < 0) {
            this.loadSapProperties();
        }
        return (mWithLab == 1);
    }

    /**
     * Gibt den Labortyp zurück (ALUE(P_DOKAR) TYPE NDOC-DOKAR DEFAULT 'LAB')
     *
     * @return boolean
     */
    public String getLaborDocType() {
        if (mLabDocType == null) {
            this.loadSapProperties();
        }
        return mLabDocType;
    }

    /**
     *
     * @return labor update key
     */
    public String getLaborUpdateKey() {
        if (mLabUpdateKey == null) {
            this.loadSapProperties();
        }
        return mLabUpdateKey;
    }

    /**
     * Gibt den Status von Laborwerten zurück, der bei der Abfrage im SAP
     * berücksichtigt werden soll
     *
     * @return String
     */
    public String getLaborValueStatusKey() {
        if (getLabValStatus() == null) {
            this.loadSapProperties();
        }
        return getLabValStatus();
    }

    /**
     * Gibt den Typ von Laborwerten zurück, der bei der Abfrage im SAP
     * berücksichtigt werden soll
     *
     * @return String
     */
    public String getLaborValueTypeKey() {
        if (getLabValType() == null) {
            this.loadSapProperties();
        }
        return getLabValType();
    }

    /**
     * KAIN-Nachrichten aus SAP auslesen
     *
     * @return boolean
     */
    public boolean importKAIN() {
        if (mWithKAIN < 0) {
            this.loadSapProperties();
        }
        return (mWithKAIN == 1);
    }

    /**
     *
     * @return use method to retrieve ikz from SAP?
     */
    public boolean useIKZMethod() {
        if (this.mIkzMethod == null) {
            this.loadSapProperties();
        }
        return mIkzMethodUsed;
    }

    /**
     *
     * @return use method to retrieve ikz from SAP?
     */
    public String getIKZMethod() {
        if (this.mIkzMethod == null) {
            this.loadSapProperties();
        }
        return mIkzMethod;
    }

    /**
     *
     * @return check secondary diagnosis?
     */
    public boolean checkPrimSekDiags() {
        if (mCheckPrimSek < 0) {
            this.loadSapProperties();
        }
        return (mCheckPrimSek == 1);
    }

    /**
     * Sofern die Fallnummern über eine BAPI geholt werden sollen
     *
     * @param dbName database
     * @return String
     */
    public String getBAPICaseMethod(String dbName) {
        if (mBabiCaseHolder == null) {
            this.loadSapProperties();
        }
        if (mBabiCaseHolder != null && mBabiCaseHolder.isEmpty() && mLoadBAPICase != null) {
            return mLoadBAPICase.get(dbName);
        } else {
            return mBabiCaseHolder;
        }
    }

    /**
     *
     * @return last update
     */
    public String getLastUpdateDate() {
        if (mLastUpdateDate == null) {
            this.loadSapProperties();
        }
        return mLastUpdateDate;
    }

    /**
     *
     * @return is stationary?
     */
    public boolean importStationaer() {
        if (mImportStationaer == -1) {
            this.loadSapProperties();
        }
        return mImportStationaer == 1;
    }

    /**
     *
     * @return is day-care?
     */
    public boolean importTeilStationaer() {
        if (mImportTeilStationaer == -1) {
            this.loadSapProperties();
        }
        return mImportTeilStationaer == 1;
    }

    /**
     *
     * @return is ambulant surcharge?
     */
    public boolean importAmbulantOP() {
        if (mAmbulantOPs == -1) {
            this.loadSapProperties();
        }
        return mAmbulantOPs == 1;
    }

    /**
     *
     * @return is ambulant cancels?
     */
    public boolean importAmbulantCancels() {
        if (mAmbulantCancels == -1) {
            this.loadSapProperties();
        }
        return mAmbulantCancels == 1;
    }

    /**
     *
     * @return with authentification control?
     */
    public boolean withAuthControl() {
        if (mAuthControl == -1) {
            loadSapProperties();
        }
        return mAuthControl == 1;
    }

    /**
     *
     * @return authentification department ...?
     */
    public String getAuthNBewAkt() {
        if (mAuthNbewAkt == null) {
            loadSapProperties();
        }
        return mAuthNbewAkt;
    }

    /**
     *
     * @return authentification dia ...?
     */
    public String getAuthNDiaAll() {
        if (mAuthNdiaAll == null) {
            loadSapProperties();
        }
        return mAuthNdiaAll;
    }

    /**
     *
     * @return authentification dia ... org... ?
     */
    public String getAuthNDiaOrg() {
        if (mAuthNdiaOrg == null) {
            loadSapProperties();
        }
        return mAuthNdiaOrg;
    }

    /**
     *
     * @return end date
     */
    public boolean checkEndDate() {
        if (mCheckEndDat == -1) {
            loadSapProperties();
        }
        return mCheckEndDat == 1;
    }

    /**
     *
     * @return end date delay
     */
    public int getEndDateDelay() {
        if (mCheckEndDatDelay == -1) {
            loadSapProperties();
        }
        return mCheckEndDatDelay;
    }

    /**
     *
     * @return login type
     */
    public int getLoginType() {
        if (mLoginType < 0) {
            this.loadSapProperties();
        }
        return mLoginType;
    }

    /**
     *
     * @return host
     */
    public String getGWHost() {
        if (mGwHost == null) {
            this.loadSapProperties();
        }
        return mGwHost;
    }

    /**
     *
     * @return server
     */
    public String getGWServ() {
        if (mGwServ == null) {
            this.loadSapProperties();
        }
        return mGwServ;
    }

    /**
     *
     * @return group
     */
    public String getR3Group() {
        if (mR3Group == null) {
            this.loadSapProperties();
        }
        return mR3Group;
    }

    /**
     *
     * @return debug mode
     */
    protected boolean isDebugMode() {
        if (mDebugMode == -1) {
            this.loadSapProperties();
        }
        return mDebugMode == 1;
    }

    /**
     *
     * @return compute wa?
     */
    protected boolean computeWA() {
        if (mComputeWA == -1) {
            this.loadSapProperties();
        }
        return mComputeWA == 1;
    }

    /**
     *
     * @return version of SAP interface?
     */
    public int getSSTVersion() {
        if (mSstVersion == -1) {
            this.loadSapProperties();
        }
        return mSstVersion;
    }

    /**
     *
     * @return with case type mdk?
     */
    protected boolean hasFalltypMDK() {
        if (mFalltypMDK == null) {
            this.loadSapProperties();
        }
        return mHasFalltyp_MDK;
    }

    /**
     *
     * @return case type mdk
     */
    protected String getFalltypMDK() {
        if (mFalltypMDK == null) {
            this.loadSapProperties();
        }
        return mFalltypMDK;
    }

    /**
     *
     * @return count
     */
    public int getResetCount() {
        if (mResetCount <= 0) {
            this.loadSapProperties();
        }
        return mResetCount;
    }

    /**
     *
     * @return case by update date
     */
    protected int getCaseByUpdateDate() {
        if (mCasenrByUpdateDate == -1) {
            this.loadSapProperties();
        }
        return mCasenrByUpdateDate;
    }

    /**
     *
     * @return check SAP drg
     */
    protected boolean getCheckSAPDRG() {
        if (mCheckSAPDRG == -1) {
            this.loadSapProperties();
            if (mCheckSAPDRG == -1) {
                mCheckSAPDRG = 1;
            }
        }
        return mCheckSAPDRG != 0;
    }

    /**
     *
     * @return readonly case numbers
     */
    public boolean onlyReadCaseNumbers() {
        if (mOnlyReadCaseNumbers == -1) {
            this.loadSapProperties();
        }
        return mOnlyReadCaseNumbers == 1;
    }

    /**
     *
     * @return case numbers for department
     */
    public String getCaseNumbers4Department() {
        if (mCaseNumber4Department == null) {
            loadSapProperties();
        }
        return mCaseNumber4Department;
    }

    /**
     *
     * @return max block size
     */
    public int getMaxBlockSize() {
        if (mMaxBlockSize == -1) {
            loadSapProperties();
        }
        return mMaxBlockSize;
    }

}
