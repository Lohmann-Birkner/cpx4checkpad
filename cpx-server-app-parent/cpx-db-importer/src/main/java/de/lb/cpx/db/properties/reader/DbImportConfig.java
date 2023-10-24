/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.db.properties.reader;

import de.lb.cpx.str.utils.StrUtils;
import java.io.File;
import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration2.Configuration;

/**
 *
 * @author gerschmann
 */
public class DbImportConfig extends ImportConfig{

    private static final Logger LOG = Logger.getLogger(DbImportConfig.class.getName());
    
    public static final String PROPERTIES_SST_NAME = "sst";

    
    public DbImportConfig(){
        
    }
    
    @Override
    public File getXmlConfigFile() {
        return super.getXmlConfigFile(PROPERTIES_SST_NAME);
    }

//        mCharType = getPropertiesBoolValue(m_resourceBundle, "CHAR_TRANS");
    public boolean getCharType(){
        return getBoolean(getXmlConfig(), "CHAR_TRANS");
    }
//        mBelegSignType = getPropertiesIntValue(m_resourceBundle, "BELEG_SIGN_TYPE");
    
    public int getBelegSigType(){
        return getInt(getXmlConfig(),"BELEG_SIGN_TYPE");
    }
//        mBelegSign = getPropertiesStringValue(m_resourceBundle, "BELEG_SIGN");
    public String getBelegSign(){
        return getString(getXmlConfig(),"BELEG_SIGN");
    }
    
//        mDbSchemas = getPropertiesStringMapValue(m_resourceBundle, "DBSCHEMA");
        public Map<String, String> getDbSchema(){
            return getPropertiesStringMapValue(getXmlConfig(), "DBSCHEMA");
        }
    
//        mMandant = getPropertiesStringValue(m_resourceBundle, "MANDANT");
        public String getMandant(){
            return getString(getXmlConfig(), "MANDANT");
        }
//        mMedicoAbrech = getPropertiesStringValue(m_resourceBundle, "MEDICO_ABRECH");
        public String getMedicoAbrech(){
            return getString(getXmlConfig(), "MEDICO_ABRECH");
        }
//        mFdMandanten = getPropertiesLongMapValue(m_resourceBundle, "FDMANDANT");
        public Map <Long, String> getFdMandant(){
            return getPropertiesLongMapValue(getXmlConfig(), "FDMANDANT");
        }
//        mGwiMandant = getPropertiesBoolValue(m_resourceBundle, "GWIMANDANT");
        public boolean getGwiMandant(){
            return getBoolean(getXmlConfig(), "GWIMANDANT");
        }
//        mGwiMandanten = getPropertiesStringMapValue(m_resourceBundle, "GWIMANDANTPRAEFIX");
        public Map<String, String> getGwiMandanten(){
            return getPropertiesStringMapValue(getXmlConfig(), "GWIMANDANTPRAEFIX");
        }
        
//        final ArrayList<String> mandanten = new ArrayList<>(getGwiMandanten().keySet());
//        Collections.sort(mandanten);
//        mVecMandanten = mandanten;
        public List<String> getVecMandanten(){

        final ArrayList<String> mandanten = new ArrayList<>(getGwiMandanten().keySet());
            Collections.sort(mandanten);
            return mandanten;
        }

//        mGwiCostunitIdent = getPropertiesBoolValue(m_resourceBundle, "GWI_USE_COSTUNITIDENT");
        public boolean getGwiConsunitIdent(){
            return getBoolean(getXmlConfig(), "GWI_USE_COSTUNITIDENT");
        }
//        mGwiIntensivtypIdent = getPropertiesStringValue(m_resourceBundle, "GWI_INTENSIVTYP_IDENT");
        public String getGwiIntensivTypIdent(){
            return getString(getXmlConfig(), "GWI_INTENSIVTYP_IDENT");
        }
//        mGwiRealPatientIdent = getPropertiesBoolValue(m_resourceBundle, "GWI_REAL_PATIENT_IDENT");
        public boolean getGwiRealPatientIdent(){
            return getBoolean(getXmlConfig(), "GWI_REAL_PATIENT_IDENT");
        }
//        mMedicoGroupedDiag = StrUtils.split(getPropertiesStringValue(m_resourceBundle, "MEDICO_GROUPDIAG"), ',');
        public List<String> getMedicoGroupedDiag(){
            return StrUtils.split( getString(getXmlConfig(), "MEDICO_GROUPDIAG"), ',');
        }
//        mMedicoVorNachStatDiag = getPropertiesStringValue(m_resourceBundle, "MEDICO_SV_SN_DIAG");
        public String getMedicoVorNachStatDiag(){
            return getString(getXmlConfig(), "MEDICO_SV_SN_DIAG");
        }
//        mMedicoUserCaseState = getPropertiesBoolValue(m_resourceBundle, "MEDICO_USER_CASESTATE");
        public boolean getMedicoUserCaseState(){
            return getBoolean(getXmlConfig(), "MEDICO_USER_CASESTATE");
        }
//        mClinicomFabBez = getPropertiesStringValue(m_resourceBundle, "CLINICOM_FABBEZ");
        public String getClinicomFabBez(){
            return getString(getXmlConfig(), "CLINICOM_FABBEZ");
        }
        
//        final Map<String, String> laborgruppe = getPropertiesStringMapValue(m_resourceBundle, "GWI_IMP_LABORGRUPPE");
        public Map<String, String> getLaborgruppe(){
            return getPropertiesStringMapValue(getXmlConfig(), "GWI_IMP_LABORGRUPPE");
        }
//        mGwiImpLaborgruppeRestriction = !laborgruppe.isEmpty() && toBool(laborgruppe.entrySet().iterator().next().getKey());
        public boolean getGwiImpLaborgruppeRestriction (){
            return  !getLaborgruppe().isEmpty() && toBool(getLaborgruppe().entrySet().iterator().next().getKey());
        }
        
//        mGwiImpLaborgruppe = new ArrayList<>(laborgruppe.keySet());
        public List<String> getGwiImpLaborgruppe(){
            return  new ArrayList<>(getLaborgruppe().keySet());
        }
////        mGwiImpLaborgruppe = getPropertiesMultiStringValue(m_resourceBundle, "GWI_IMP_LABORGRUPPE");
//        mImpLaborOnlyMinMax = getPropertiesBoolValue(m_resourceBundle, "GWI_IMP_LABORMINMAX");
        public boolean getImpLaborOnlyMinMax(){
            return getBoolean(getXmlConfig(), "GWI_IMP_LABORMINMAX");
        }
//        mGwiImpLaborForLastDays = getPropertiesIntValue(m_resourceBundle, "GWI_IMP_LABORFORLASTDAYS");
        public int getGwiImpLaborForLastDays(){
            return  getInt(getXmlConfig(), "GWI_IMP_LABORFORLASTDAYS");
        }
//        mGwiDrgBelegtyp = getPropertiesBoolValue(m_resourceBundle, "GWI_DRG_BELEGTYP");
        public boolean getmGwiDrgBelegtyp(){
            return getBoolean(getXmlConfig(), "GWI_DRG_BELEGTYP");
        }
//        mClinicomBelegIdent = StrUtils.split(getPropertiesStringValue(m_resourceBundle, "CLINICOM_TARIF_BELEGTYP"), ',');
        public List<String> getClinicomBelegIdent(){
            return StrUtils.split(getString(getXmlConfig(), "CLINICOM_TARIF_BELEGTYP"), ',');
        }
//        mClinicomBelegErm = !mClinicomBelegIdent.isEmpty();
        public boolean getClinicomBelegErm(){
            return  !getClinicomBelegIdent().isEmpty();
        }
//        mNexusIntensivStationenDefList = StrUtils.split(getPropertiesStringValue(m_resourceBundle, "NEXUS_INTENSIV_STATIONEN"), ',');
        public List<String> getNexusIntensivStationenDefList(){
            return StrUtils.split(getString(getXmlConfig(), "NEXUS_INTENSIV_STATIONEN"), ',');
        }
////        mNexusKisStatusDefHash = getPropertiesSplittedStringValue(m_resourceBundle, "NEXUS_KIS_STATI", ',');
//        mNexusBelegStationenDefList = getPropertiesSplittedStringValue(m_resourceBundle, "NEXUS_BELEG_STATIONEN", ',');
        public List<String[]> getNexusBelegStationenDefList(){
            return getPropertiesSplittedStringValue(getXmlConfig(), "NEXUS_BELEG_STATIONEN");
        }
//        mNexusBelegFABDefList = getPropertiesSplittedStringValue(m_resourceBundle, "NEXUS_BELEG_FAB", ',');
        public List<String[]> getNexusBelegFABDefList(){
            return getPropertiesSplittedStringValue(getXmlConfig(), "NEXUS_BELEG_FAB");
        }
//        mNexusKisStatusDefHash = getPropertiesStringMapValue(m_resourceBundle, "NEXUS_KIS_STATI");
        public Map<String, String> getNexusKisStatusDefHash(){
            return getPropertiesStringMapValue(getXmlConfig(), "NEXUS_KIS_STATI");
        }


   private Map<String, String> getPropertiesStringMapValue(final Configuration config, final String pKey) {

        final Map<String, String> result = new HashMap<>();
        Iterator<String>  itr =config.getKeys();
       while (itr.hasNext()) {
            String key = toStr(itr.next());
            String sfName = getString(config, key);
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

   private Map<Long, String> getPropertiesLongMapValue(final Configuration config, final String pKey) {
        Map<Long, String> result = new HashMap<>();
        Map<String, String> tmp = getPropertiesStringMapValue(config, pKey);
        for (Map.Entry<String, String> entry : tmp.entrySet()) {
            final String key = entry.getKey();
            final String value = entry.getValue();

            Long longKey = null;
            try {
                longKey = Long.valueOf(key);
                result.put(longKey, value);
            } catch (NumberFormatException ex) {
                LOG.log(Level.FINEST, "cannot convert string to long: " + tmp, ex);
            }
        }
        return result;
    }

    private List<String[]> getPropertiesSplittedStringValue(final Configuration config, final String pKey) {
        final List<String[]> result = new ArrayList<>();
        final String tmp = getString(config, pKey);
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

    public void createConfigFile() throws FileSystemException {
        createConfigFile(PROPERTIES_SST_NAME);
    }

}
