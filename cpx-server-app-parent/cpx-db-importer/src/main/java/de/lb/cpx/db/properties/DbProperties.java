/*
 * Copyright (c) 2019 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner Health Care Consulting GmbH and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner Health Care Consulting GmbH
 * http://www.lohmann-birkner.de
 *
 * Contributors:
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.db.properties;

import de.lb.cpx.db.properties.reader.DbImportConfig;
import de.lb.cpx.str.utils.StrUtils;
import static de.lb.cpx.str.utils.StrUtils.*;
import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author niemeier
 */
public class DbProperties {

    private static final CpxSystemPropertiesInterface CPX_PROPS = CpxSystemProperties.getInstance();
    
    public static final String PROPERTIES_SST_NAME = "dbImport\\sst";
    private static final Logger LOG = Logger.getLogger(DbProperties.class.getName());

    private final boolean mCharType; // = -1;
    private final int mBelegSignType; // = 0;
    private final String mBelegSign; // = "";
    private final String mMandant; // = null;
    private final String mMedicoAbrech; // = null;
    private final Map<String, String> mDbSchemas;
    private final Map<Long, String> mFdMandanten;
    private final boolean mGwiMandant; // = null;
    private final Map<String, String> mGwiMandanten;
    private final List<String> mVecMandanten;
    private final boolean mGwiCostunitIdent; // = null;
    private final String mGwiIntensivtypIdent; // = null;
    private final boolean mGwiRealPatientIdent; // = null;
    private final String mClinicomFabBez; // = null;
    private final boolean mClinicomBelegErm; // = false;
    private final List<String> mClinicomBelegIdent;
    private final List<String> mMedicoGroupedDiag;
    private final String mMedicoVorNachStatDiag; // = null;
    private boolean mMedicoUserCaseState; // = false;
    private boolean mMedicoTobDef; // = false; //GKr
//    protected final List<String> m_medico_tob_ident; //GKr

    private final boolean mImpLaborOnlyMinMax; // = true;
    private final boolean mGwiImpLaborgruppeRestriction; // = false;
    private final Integer mGwiImpLaborForLastDays; // = null;
    private final boolean mGwiDrgBelegtyp; // = false;
    private final List<String> mGwiImpLaborgruppe;

    private final List<String> mNexusIntensivStationenDefList;
    private final List<String[]> mNexusBelegStationenDefList;
    private final List<String[]> mNexusBelegFABDefList;
////    public final boolean m_debugprint_forSST_aktiv; // = false;
//
    //private final List<String[]> mNexusKisStatusDefHash;
    private final Map<String, String> mNexusKisStatusDefHash;
    
    private DbImportConfig mImportConfig = new DbImportConfig();

    public DbProperties() {
        try{
            mImportConfig.getXmlConfigFile();
        }catch(Exception ex){
            LOG.log(Level.SEVERE, " couldnot open import config file, try to create new one", ex);
            try{
                mImportConfig.createConfigFile();
            }catch(FileSystemException ex1){
                LOG.log(Level.SEVERE, "could not create properties file", ex1);
            }
            
        }
        mCharType = mImportConfig.getCharType();
        mBelegSignType = mImportConfig.getBelegSigType();
        mBelegSign = mImportConfig.getBelegSign();
        mDbSchemas = mImportConfig.getDbSchema();
        mMandant = mImportConfig.getMandant();
        mMedicoAbrech = mImportConfig.getMedicoAbrech();
        mFdMandanten = mImportConfig.getFdMandant();
        mGwiMandant = mImportConfig.getGwiMandant();
        mGwiMandanten = mImportConfig.getGwiMandanten();
       
        mVecMandanten = mImportConfig.getVecMandanten();
        mGwiCostunitIdent = mImportConfig.getGwiConsunitIdent();
        mGwiIntensivtypIdent = mImportConfig.getGwiIntensivTypIdent();
        mGwiRealPatientIdent = mImportConfig.getGwiRealPatientIdent();
        mMedicoGroupedDiag = mImportConfig.getMedicoGroupedDiag();
        mMedicoVorNachStatDiag = mImportConfig.getMedicoVorNachStatDiag();
        mMedicoUserCaseState = mImportConfig.getMedicoUserCaseState();
        mClinicomFabBez = mImportConfig.getClinicomFabBez();

        mGwiImpLaborgruppeRestriction = mImportConfig.getGwiImpLaborgruppeRestriction();
        mGwiImpLaborgruppe = mImportConfig.getGwiImpLaborgruppe();
//        mGwiImpLaborgruppe = getPropertiesMultiStringValue(m_resourceBundle, "GWI_IMP_LABORGRUPPE");
        mImpLaborOnlyMinMax = mImportConfig.getImpLaborOnlyMinMax();
        mGwiImpLaborForLastDays = mImportConfig.getGwiImpLaborForLastDays();
        mGwiDrgBelegtyp = mImportConfig.getmGwiDrgBelegtyp();
        mClinicomBelegIdent = mImportConfig.getClinicomBelegIdent();
        mClinicomBelegErm = mImportConfig.getClinicomBelegErm();
        mNexusIntensivStationenDefList = mImportConfig.getNexusIntensivStationenDefList();
//        mNexusKisStatusDefHash = getPropertiesSplittedStringValue(m_resourceBundle, "NEXUS_KIS_STATI", ',');
        mNexusBelegStationenDefList = mImportConfig.getNexusBelegStationenDefList();
        mNexusBelegFABDefList = mImportConfig.getNexusBelegFABDefList();
        mNexusKisStatusDefHash = mImportConfig.getNexusKisStatusDefHash();

//        final Enumeration<String> enumer = m_resourceBundle.getKeys();
//        while (enumer.hasMoreElements()) {
//            String key = enumer.nextElement();
//            String sfName = m_resourceBundle.getString(key);
//
//            if (key.equals("CHAR_TRANS")) {
//                if (sfName != null && sfName.equals("true")) {
//                    mCharType = 1;
//                } else {
//                    mCharType = 0;
//                }
//            }
//        if (key.equals("BELEG_SIGN_TYPE")) {
//            try {
//                mBelegSignType = Integer.parseInt(sfName);
//            } catch (NumberFormatException ex) {
//                mBelegSignType = -1;
//            }
//        }
//        if (key.equals("BELEG_SIGN")) {
//            mBelegSign = sfName;
//            if (mBelegSign == null) {
//                mBelegSign = "";
//            }
//        }
//        if (key.startsWith("DBSCHEMA")) {
//            String[] sb = key.split("_");
//            if (sb.length >= 2) {
//                mDbSchemas.put(sb[1], sfName);
//            }
//        }
//        if (key.equals("MANDANT")) {
//            mMandant = sfName;
//            if (mMandant == null) {
//                mMandant = "";
//            }
//        }
//        if (key.equals("MEDICO_ABRECH")) {
//            mMedicoAbrech = sfName;
//        }
//        if (key.startsWith("FDMANDANT")) {
//            String[] sb = key.split("_");
////                        if (mFdMandanten == null) {
////                            mFdMandanten = new HashMap<>();
////                        }
//            if (sb.length >= 2) {
//                Long value = null;
//                final String tmp = sb[1];
//                try {
//                    value = Long.valueOf(tmp);
//                } catch (NumberFormatException ex) {
//                    LOG.log(Level.FINEST, "cannot convert string to long: " + tmp, ex);
//                }
//                mFdMandanten.put(value, sfName);
//            }
//        }
//        if (key.equals("GWIMANDANT")) {
//            mGwiMandant = sfName;
//            if (mGwiMandant == null) {
//                mGwiMandant = "";
//            }
//        }
//        if (key.startsWith("GWIMANDANTPRAEFIX_")) {
//            String[] sb = key.split("_");
////                        if (mGwiMandanten == null) {
////                            mGwiMandanten = new HashMap<>();
////                        }
//            if (sb.length >= 2) {
//                mGwiMandanten.put(sb[1], sfName);
//            }
//        }
//        if (key.equals("GWI_USE_COSTUNITIDENT")) {
//            mGwiCostunitIdent = sfName;
//            if (mGwiCostunitIdent == null) {
//                mGwiCostunitIdent = "";
//            }
//        }
//        if (key.equals("GWI_INTENSIVTYP_IDENT")) {
//            mGwiIntensivtypIdent = sfName;
//            if (mGwiIntensivtypIdent == null) {
//                mGwiIntensivtypIdent = "";
//            }
//        }
//        if (key.equals("GWI_REAL_PATIENT_IDENT")) {
//            mGwiRealPatientIdent = sfName;
//            if (mGwiRealPatientIdent == null) {
//                mGwiRealPatientIdent = "";
//            }
//        }
//        if (key.startsWith("MEDICO_GROUPDIAG")) {
//            mMedicoGroupedDiag.addAll(StrUtils.split(sfName, ','));
////                        if (mMedicoGroupedDiag == null) {
////                            mMedicoGroupedDiag = this.split(sfName, ',');
////                        }
//        }
//        if (key.startsWith("MEDICO_SV_SN_DIAG")) {
//            if (mMedicoVorNachStatDiag == null) {
//                mMedicoVorNachStatDiag = sfName;
//            }
//        }
//        if (key.equals("MEDICO_USER_CASESTATE")) {
//            if (sfName.toUpperCase().equals("TRUE")) {
//                mMedicoUserCaseState = true;
//            }
//        }
        /*                                     if (key.equals("MEDICO_TOB_FEK")){
                                            if(sfName.trim().length()>0){
							this.mMedicoTobDef=true;
							this.m_medico_tob_ident=new ArrayList<String>();
							String[] tobTypes = sfName.split(",");
							for(int i=0, n=tobTypes.length;i<n;i++){
								m_medico_tob_ident.add(tobTypes[i].trim());
							}
						}
                                        }
         */
//        if (key.equals("CLINICOM_FABBEZ")) {
//            mClinicomFabBez = sfName;
//            if (mClinicomFabBez == null) {
//                mClinicomFabBez = "";
//            }
//        }
//        if (key.startsWith("GWI_IMP_LABORGRUPPE_")) {
//            mGwiImpLaborgruppeRestriction = true;
//            if (sfName.toLowerCase().equals("true")) {
//                String laborgruppe = key.replace("GWI_IMP_LABORGRUPPE_", "");
//                mGwiImpLaborgruppe.add(laborgruppe);
//            }
//        }
//        if (key.equals("GWI_IMP_LABORMINMAX")) {
//            if (sfName.toLowerCase().equals("false")) {
//                mImpLaborOnlyMinMax = false;
//            }
//        }
//        if (key.equals("GWI_IMP_LABORFORLASTDAYS")) {
//            try {
//                mGwiImpLaborForLastDays = Integer.valueOf(sfName);
//            } catch (NumberFormatException ex) {
//                LOG.log(Level.FINEST, "cannot convert value from string to int: " + sfName, ex);
//                mGwiImpLaborForLastDays = null;
//            }
//        }
//        if (key.equals("GWI_DRG_BELEGTYP")) {
//            if (sfName.toLowerCase().equals("true")) {
//                mGwiDrgBelegtyp = true;
//            }
//        }
//        if (key.equals("CLINICOM_TARIF_BELEGTYP")) {
//            if (sfName.trim().length() > 0) {
//                this.mClinicomBelegErm = true;
//                //m_clinicom_beleg_ident = new ArrayList<String>();
//                String[] belTypes = sfName.split(",");
//                for (int i = 0, n = belTypes.length; i < n; i++) {
//                    mClinicomBelegIdent.add(belTypes[i].trim());
//                }
//            }
//        }
//        //GKr ->
//        if (key.equals("NEXUS_INTENSIV_STATIONEN")) {
//            if (sfName.trim().length() > 0) {
////                            if (this.m_nexusIntensivStationenDefList == null) {
////                                this.m_nexusIntensivStationenDefList = new ArrayList<String>();
////                            }
//                String[] intensivStationen = sfName.split(",");
//                for (int i = 0, n = intensivStationen.length; i < n; i++) {
//                    this.m_nexusIntensivStationenDefList.add(intensivStationen[i].trim());
//                }
//            }
//        }
//
//        if (key.equals("NEXUS_KIS_STATI")) {
//            if (sfName.trim().length() > 0) {
////                            if (this.m_nexusKisStatusDefHash == null) {
////                                this.m_nexusKisStatusDefHash = new HashMap<>();
////                            }
//                String[] kisStati = sfName.split(",");
//                for (int i = 0, n = kisStati.length; i < n; i++) {
//                    String[] statiMap = kisStati[i].trim().split("_");
//                    if (statiMap.length == 2) {
//                        this.m_nexusKisStatusDefHash.put(statiMap[0].trim(), statiMap[1].trim());
//                    }
//                }
//            }
//        }
//
//        if (key.equals("NEXUS_BELEG_STATIONEN")) {
//            if (sfName.trim().length() > 0) {
////                            if (this.m_nexusBelegStationenDefList == null) {
////                                this.m_nexusBelegStationenDefList = new ArrayList<String[]>();
////                            }
//                String[] belegStationen = sfName.split(",");
//                if (belegStationen != null) {
//                    for (int i = 0, n = belegStationen.length; i < n; i++) {
//                        String belegStat = belegStationen[i].trim();
//                        if (belegStat != null && !belegStat.isEmpty()) {
//                            String[] bStats = belegStat.split("_");
//                            if (bStats != null && bStats.length == 2) {
//                                this.m_nexusBelegStationenDefList.add(bStats);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        if (key.equals("NEXUS_BELEG_FAB")) {
//            if (sfName.trim().length() > 0) {
////                            if (this.m_nexusBelegFABDefList == null) {
////                                this.m_nexusBelegFABDefList = new ArrayList<String[]>();
////                            }
//                String[] belegFABs = sfName.split(",");
//                if (belegFABs != null) {
//                    for (int i = 0, n = belegFABs.length; i < n; i++) {
//                        String belegFAB = belegFABs[i].trim();
//                        if (belegFAB != null && !belegFAB.isEmpty()) {
//                            String[] bFABs = belegFAB.split("_");
//                            if (bFABs != null && bFABs.length == 2) {
//                                this.m_nexusBelegFABDefList.add(bFABs);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        if (key.equals("DEBUG_PRINT_SST")) {
//            if (sfName.toLowerCase().equals("true")) {
//                this.m_debugprint_forSST_aktiv = true;
//            }
//        }
        //-> GKr
//        }
    }

    private String getPropertiesKeyValue(final ResourceBundle pResource, final String pKey) {
        try {
            final String result = pResource.getString(pKey);
            return result == null ? "" : result.trim();
        } catch (MissingResourceException ex) {
            LOG.log(Level.WARNING, "Cannot get resource property for key '" + pKey + "'");
            LOG.log(Level.FINEST, "Cannot get resource property for key '" + pKey + "'", ex);
            return null;
        }
    }

    private String getPropertiesStringValue(final ResourceBundle pResource, final String pKey) {
        return getPropertiesKeyValue(pResource, pKey);
    }

    private boolean getPropertiesBoolValue(final ResourceBundle pResource, final String pKey) {
        return StrUtils.toBool(getPropertiesKeyValue(pResource, pKey));
    }

    private int getPropertiesIntValue(final ResourceBundle pResource, final String pKey) {
        return StrUtils.toInt(getPropertiesKeyValue(pResource, pKey));
    }

    private Map<String, String> getPropertiesMapValue(final ResourceBundle pResource, final String pKey) {
        Enumeration<String> enumer = pResource.getKeys();
        final Map<String, String> result = new HashMap<>();
        while (enumer.hasMoreElements()) {
            String key = toStr(enumer.nextElement());
            String sfName = toStr(pResource.getString(key));
            if (sfName.isEmpty()) {
                continue;
            }
            if (key.startsWith(pKey)) {
                String[] sb = key.split("_");
                if (sb.length >= 2) {
                    result.put(toStr(sb[1]), sfName);
                }
            }
        }
        return result;
    }

    private Map<String, String> getPropertiesStringMapValue(final ResourceBundle pResource, final String pKey) {
        return getPropertiesMapValue(pResource, pKey);
    }

    private Map<Long, String> getPropertiesLongMapValue(final ResourceBundle pResource, final String pKey) {
        Map<Long, String> result = new HashMap<>();
        Map<String, String> tmp = getPropertiesMapValue(pResource, pKey);
        for (Map.Entry<String, String> entry : tmp.entrySet()) {
            final String key = entry.getKey();
            final String value = entry.getValue();

            Long longKey = null;
            try {
                longKey = Long.valueOf(key);
                getFdMandanten().put(longKey, value);
            } catch (NumberFormatException ex) {
                LOG.log(Level.FINEST, "cannot convert string to long: " + tmp, ex);
            }
        }
        return result;
    }

    private List<String[]> getPropertiesSplittedStringValue(final ResourceBundle pResource, final String pKey, final char pChar) {
        final List<String[]> result = new ArrayList<>();
        final String tmp = getPropertiesStringValue(pResource, pKey);
        if (tmp == null) {
            return result;
        }
        String[] arr = tmp.split(",");
        if (arr != null) {
            for (int i = 0, n = arr.length; i < n; i++) {
                String val = arr[i].trim();
                if (val != null && !val.isEmpty()) {
                    String[] a = val.split("_");
                    if (a != null && a.length == 2) {
                        result.add(a);
                    }
                }
            }
        }
        return result;
    }

//    private List<String> getPropertiesMultiStringValue(final ResourceBundle pResource, final String pKey) {
//        Enumeration<String> enumer = pResource.getKeys();
//        final Map<String, String> result = new HashMap<>();
//        while (enumer.hasMoreElements()) {
//            String key = toStr(enumer.nextElement());
//            String sfName = toStr(pResource.getString(key));
//            if (sfName.isEmpty()) {
//                continue;
//            }
//            if (key.startsWith(pKey)) {
//                final String keyTmp = key.replace(pKey + "_", "");
//                result.add(keyTmp, sfName);
//            }
//        }
//        return result;
//    }
    /**
     * @return the mCharType
     */
    public boolean isCharType() {
        return mCharType;
    }

    /**
     * @return the mBelegSignType
     */
    public int getBelegSignType() {
        return mBelegSignType;
    }

    /**
     * @return the mBelegSign
     */
    public String getBelegSign() {
        return mBelegSign;
    }

    /**
     * @return the mMandant
     */
    public String getMandant() {
        return mMandant;
    }

    /**
     * @return the mMedicoAbrech
     */
    public String getMedicoAbrech() {
        return mMedicoAbrech;
    }

    /**
     * @return the mDbSchemas
     */
    public Map<String, String> getDbSchemas() {
        return Collections.unmodifiableMap(mDbSchemas);
    }

    public String getDbSchema2(final String pDefaultSchema) {
        //String schema = getProperties().getDbSchemas().get(this.m_srcName);
        Map<String, String> schemas = getDbSchemas();
        if (schemas.isEmpty()) {
            return "";
        }
        String schema = schemas.entrySet().iterator().next().getValue();
        if (schema != null && !schema.isEmpty()) {
            schema = schema + ".";
        } else {
            schema = pDefaultSchema;
        }
        return schema;
    }

    /**
     * @param pSrcName source name
     * @return db schema
     */
    public String getDbSchema(final String pSrcName) {
        final String srcName = pSrcName == null ? "" : pSrcName.trim();
        Map<String, String> map = getDbSchemas();
        if (map.isEmpty()) {
            return "";
        }
        String value = map.get(srcName);
        return value == null ? "" : value.trim();
    }

    /**
     * @return the mFdMandanten
     */
    public Map<Long, String> getFdMandanten() {
        return Collections.unmodifiableMap(mFdMandanten);
    }

    /**
     * @return the mGwiMandant
     */
    public boolean isGwiMandant() {
        return mGwiMandant;
    }

    /**
     * @return the mGwiMandanten
     */
    public Map<String, String> getGwiMandanten() {
        return Collections.unmodifiableMap(mGwiMandanten);
    }

    /**
     * @return the mVecMandanten
     */
    public List<String> getVecMandanten() {
        return mVecMandanten == null ? null : new ArrayList<>(mVecMandanten);
    }

    /**
     * @return the mGwiCostunitIdent
     */
    public boolean isGwiCostunitIdent() {
        return mGwiCostunitIdent;
    }

    /**
     * @return the mGwiIntensivtypIdent
     */
    public String getGwiIntensivtypIdent() {
        return mGwiIntensivtypIdent;
    }

    /**
     * @return the mGwiRealPatientIdent
     */
    public boolean isGwiRealPatientIdent() {
        return mGwiRealPatientIdent;
    }

    /**
     * @return the mClinicomFabBez
     */
    public String getClinicomFabBez() {
        return mClinicomFabBez;
    }

    /**
     * @return the mClinicomBelegErm
     */
    public boolean isClinicomBelegErm() {
        return mClinicomBelegErm;
    }

    /**
     * @return the mClinicomBelegIdent
     */
    public List<String> getClinicomBelegIdent() {
        return mClinicomBelegIdent == null ? null : new ArrayList<>(mClinicomBelegIdent);
    }

    /**
     * @return the mMedicoGroupedDiag
     */
    public List<String> getMedicoGroupedDiag() {
        return mMedicoGroupedDiag == null ? null : new ArrayList<>(mMedicoGroupedDiag);
    }

    /**
     * @return the mMedicoVorNachStatDiag
     */
    public String getMedicoVorNachStatDiag() {
        return mMedicoVorNachStatDiag;
    }

    /**
     * @return the mMedicoUserCaseState
     */
    public boolean isMedicoUserCaseState() {
        return mMedicoUserCaseState;
    }

    /**
     * @return the mMedicoTobDef
     */
    public boolean isMedicoTobDef() {
        return mMedicoTobDef;
    }

    /**
     * @return the mImpLaborOnlyMinMax
     */
    public boolean isImpLaborOnlyMinMax() {
        return mImpLaborOnlyMinMax;
    }

    /**
     * @return the mGwiImpLaborgruppeRestriction
     */
    public boolean isGwiImpLaborgruppeRestriction() {
        return mGwiImpLaborgruppeRestriction;
    }

    /**
     * @return the mGwiImpLaborForLastDays
     */
    public Integer getGwiImpLaborForLastDays() {
        return mGwiImpLaborForLastDays;
    }

    /**
     * @return the mGwiDrgBelegtyp
     */
    public boolean isGwiDrgBelegtyp() {
        return mGwiDrgBelegtyp;
    }

    /**
     * @return the mGwiImpLaborgruppe
     */
    public List<String> getGwiImpLaborgruppe() {
        return mGwiImpLaborgruppe == null ? null : new ArrayList<>(mGwiImpLaborgruppe);
    }

    /**
     * @return the mNexusKisStatusDefHash
     */
    public Map<String, String> getNexusKisStatusDefHash() {
        return Collections.unmodifiableMap(mNexusKisStatusDefHash);
    }

    /**
     * @return the mNexusIntensivStationenDefList
     */
    public List<String> getNexusIntensivStationenDefList() {
        return mNexusIntensivStationenDefList == null ? null : new ArrayList<>(mNexusIntensivStationenDefList);
    }

    /**
     * @return the mNexusBelegStationenDefList
     */
    public List<String[]> getNexusBelegStationenDefList() {
        return mNexusBelegStationenDefList == null ? null : new ArrayList<>(mNexusBelegStationenDefList);
    }

    /**
     * @return the mNexusBelegFABDefList
     */
    public List<String[]> getNexusBelegFABDefList() {
        return mNexusBelegFABDefList == null ? null : new ArrayList<>(mNexusBelegFABDefList);
    }
}
