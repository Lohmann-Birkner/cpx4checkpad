/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package transformer.impl;

import de.lb.cpx.db.container.KisAbteilungContainer;
import de.lb.cpx.db.container.KisInsuranceContainer;
import de.lb.cpx.db.container.KisPatientContainer;
import de.lb.cpx.db.container.TarifeContainer;
import de.lb.cpx.db.container.TarifeGueltigContainer;
import de.lb.cpx.db.container.Vidierstufe;
import de.lb.cpx.db.container.ZusatzContainer;
import de.lb.cpx.db.importer.utils.Constants;
import de.lb.cpx.db.importer.utils.DbCallback;
import de.lb.cpx.db.properties.DbProperties;
import de.lb.cpx.db.properties.LaborProperties;
import de.lb.cpx.model.enums.AdmissionCauseEn;
import de.lb.cpx.model.enums.AdmissionReasonEn;
import de.lb.cpx.model.enums.IcdcTypeEn;
import de.lb.cpx.shared.dto.job.config.CpxDatabaseBasedImportJob;
import de.lb.cpx.str.utils.StrUtils;
import dto.impl.Case;
import dto.impl.Department;
import dto.impl.Ward;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import module.AbstractImportDbModule;
import module.impl.ImportConfig;
import transformer.AbstractCpxTransformer;
import util.UtlDateTimeConverter;

/**
 *
 * @author niemeier
 * @param <T> config type
 * @param <CASEKEYTYPE> type of hospital case id/key
 * @param <DEPKEYTYPE> type of department id/key
 * @param <COREDATATYPE> type of some core data (admission reason etc.)
 * @param <BEATMUNGSZEITTYPE> type of beatmungszeit
 */
public abstract class DbToCpxTransformer<T extends AbstractImportDbModule<? extends CpxDatabaseBasedImportJob>, CASEKEYTYPE extends Serializable, DEPKEYTYPE extends Serializable, COREDATATYPE extends Serializable, BEATMUNGSZEITTYPE extends Serializable> extends AbstractCpxTransformer<T> {

    private static final Logger LOG = Logger.getLogger(DbToCpxTransformer.class.getName());

    protected final CpxDatabaseBasedImportJob mInputConfig;
    protected final Connection mConnection;
    protected final DbProperties mProperties;
    protected final LaborProperties mLaborProperties;
    private final Map<CASEKEYTYPE, Case> mFallMap = new HashMap<>();
//    private final Map<caseKeyType, ZusatzContainer> mZusatzContainerMap = new HashMap<>();

//    private final Map<Integer, Map<String, String>> mPossibleParams = new HashMap<>();
    private final Map<COREDATATYPE, String> mEnlassungsgrund12Map = new HashMap<>();
    private final Map<COREDATATYPE, String> mEnlassungsgrund3Map = new HashMap<>();
    private final Map<COREDATATYPE, String> mAufnahmegrund12Map = new HashMap<>();
    private final Map<COREDATATYPE, String> mAufnahmegrund34Map = new HashMap<>();
    private final Map<String, Integer> mAufnahmeanlass4CpMap = new HashMap<>();
    private final Map<COREDATATYPE, String> mAufnahmeAnlassMap = new HashMap<>();
    private final Map<COREDATATYPE, Integer> mDiagnoseartenMap = new HashMap<>();
    private final Map<COREDATATYPE, TarifeContainer> mTarifeMap = new HashMap<>();
    private final Map<String, Date> mEntbindungsdatenMap = new HashMap<>();
    private final Map<String, String> mKostentraegerMap = new HashMap<>();
    private final Map<String, Integer> mUrlaubMap = new HashMap<>();
    private final Map<Integer, String> mEinzugsgebietMap = new HashMap<>();
    private final Map<CASEKEYTYPE, ZusatzContainer<BEATMUNGSZEITTYPE>> mZusatzContainerMap = new HashMap<>();
//    private final Map<String, Integer> mEscortMap = new HashMap<>();
    private final Map<CASEKEYTYPE, BigDecimal> mVwdIntensivMap = new HashMap<>();
    private final Map<String, Integer> mDrgAbrechnungsartenMap = new HashMap<>();
    private final Map<Integer, Vidierstufe> mVidierstufeMap = new HashMap<>();
    private final Map<DEPKEYTYPE, KisAbteilungContainer<DEPKEYTYPE>> mAbteilungenMap = new HashMap<>();
    private final Map<String, String> mStationenMap = new HashMap<>();
    private final Map<CASEKEYTYPE, String> mTransferHospLess24HoursMap = new HashMap<>();
    private final Map<CASEKEYTYPE, KisInsuranceContainer> mInsurancesMap = new HashMap<>();
    private final Map<Integer, Integer> mDrgBelegMap = new HashMap<>();
    protected final String mAufnahmedatumVon;
    protected final String mAufnahmedatumBis;
    protected final String mEntlassungsdatumVon;
    protected final String mEntlassungsdatumBis;
    private final Map<String, KisPatientContainer> mPatientenMap = new HashMap<>();
    private final Set<AdmissionCauseEn> mAufnahmeanlaesse;
    private final Set<AdmissionReasonEn> mAufnahmegruende;
    private final Set<IcdcTypeEn> mDiagnosearten;
    private final Map<CASEKEYTYPE, CASEKEYTYPE> mTranslationMap = new HashMap<>();

//    protected static Date firstValidDate = new GregorianCalendar(1880, 1, 1, 0, 0, 0).getTime();
//    protected static Date lastValidDate = new GregorianCalendar(2090, 1, 1, 0, 0, 0).getTime();
//    protected static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
    public DbToCpxTransformer(final ImportConfig<T> pImportConfig) throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException, InterruptedException, NoSuchAlgorithmException, NoSuchMethodException, InvocationTargetException, SQLException {
        super(pImportConfig);
        mInputConfig = checkInputConfig(pImportConfig.getModule().getInputConfig());
        mConnection = pImportConfig.getModule().getInputConfig().createConnection();
        mProperties = new DbProperties();
        mLaborProperties = new LaborProperties();
        initAufnahmeanlass4CP();
        String aufnahmeDatumVon = StrUtils.toStaticDate(getImportConstraint().getAdmissionDateFrom(), getInputConfig().isSqlsrv());
        String aufnahmeDatumBis = "";
        String entlassungsDatumVon = StrUtils.toStaticDate(getImportConstraint().getDischargeDateFrom(), getInputConfig().isSqlsrv());
        String entlassungsDatumBis = "";
        if (getImportConstraint().getAdmissionDateTo() != null) {
            //add 1 extra day to admission date to
            final Calendar cal = Calendar.getInstance();
            cal.setTime(getImportConstraint().getAdmissionDateTo());
            cal.add(Calendar.DATE, 1);
            aufnahmeDatumBis = StrUtils.toStaticDate(cal.getTime(), getInputConfig().isSqlsrv());
        }
        if (getImportConstraint().getDischargeDateTo() != null) {
            //add 1 extra day to admission date to
            final Calendar cal = Calendar.getInstance();
            cal.setTime(getImportConstraint().getDischargeDateTo());
            cal.add(Calendar.DATE, 1);
            entlassungsDatumBis = StrUtils.toStaticDate(cal.getTime(), getInputConfig().isSqlsrv());
        }
        mAufnahmedatumVon = aufnahmeDatumVon;
        mAufnahmedatumBis = aufnahmeDatumBis;
        mEntlassungsdatumVon = entlassungsDatumVon;
        mEntlassungsdatumBis = entlassungsDatumBis;
        mAufnahmeanlaesse = getImportConstraint().getAdmissionCauses();
        mAufnahmegruende = getImportConstraint().getAdmissionReasons();
        mDiagnosearten = getImportConstraint().getIcdTypes();
    }

    public DbProperties getProperties() {
        return mProperties;
    }

    public LaborProperties getLaborProperties() {
        return mLaborProperties;
    }

    @Override
    public void close() throws Exception {
        super.close();
        if (mConnection != null && !mConnection.isClosed()) {
            mConnection.close();
        }
    }

    public final CpxDatabaseBasedImportJob getInputConfig() {
        return mInputConfig;
    }

    public void executeStatement(final String pQuery, final DbCallback pCallback) throws SQLException, IOException {
        try (PreparedStatement pstmt = mConnection.prepareStatement(pQuery); ResultSet rs = pstmt.executeQuery()) {
            if (pCallback != null) {
                pCallback.call(rs);
            }
        }
    }

//    public void executeStatement(final String pQuery) throws SQLException {
//        mConnection.prepareStatement(pQuery).executeQuery();
//    }
    public String getSingleString(final String pQuery) throws SQLException {
        try (ResultSet rs = mConnection.prepareStatement(pQuery).executeQuery()) {
            while (rs.next()) {
                return rs.getString(1);
            }
        }
        return null;
    }

    public Integer getSingleInt(final String pQuery) throws SQLException {
        try (ResultSet rs = mConnection.prepareStatement(pQuery).executeQuery()) {
            while (rs.next()) {
                return rs.getInt(1);
            }
        }
        return null;
    }

    public static CpxDatabaseBasedImportJob checkInputConfig(final CpxDatabaseBasedImportJob pConfig) {
        if (pConfig == null) {
            throw new IllegalArgumentException("config is null!");
        }
//        try {
//            if (pConfig.isClosed()) {
//                throw new IllegalArgumentException("Connection is closed!");
//            }
//        } catch (SQLException ex) {
//            LOG.log(Level.SEVERE, "Cannot detect if connection is closed, error occured during check", ex);
//            throw new IllegalArgumentException("Cannot detect if connection is closed, error occured during check: " + ex.getMessage());
//        }
        /*
    if (dir.listFiles().length <= 0) {
      throw new CpxIllegalArgumentException("Directory has no input files: " + dir.getAbsolutePath());
    }
         */
        return pConfig;
    }

    @Override
    public TransformResult start() throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException, InterruptedException, NoSuchAlgorithmException, NoSuchMethodException, InvocationTargetException, SQLException {
//        try (final CpxWriter cpxMgr = CpxWriter.getInstance(getOutputDirectory().getAbsolutePath())) {
//            
//        }
//        mPossibleParams.putAll(loadPossibleParams());

        mAufnahmegrund12Map.putAll(loadAufnahmegrund12());
        mAufnahmegrund34Map.putAll(loadAufnahmegrund34());
        mEnlassungsgrund12Map.putAll(loadEntlassungsgrund12());
        mEnlassungsgrund3Map.putAll(loadEntlassungsgrund3());
        mAufnahmeAnlassMap.putAll(loadAufnahmeanlass());
        mDiagnoseartenMap.putAll(loadDiagnosearten());
        mTarifeMap.putAll(loadTarife());
        mKostentraegerMap.putAll(loadKostentraeger());
        mUrlaubMap.putAll(loadUrlaub());
        mEinzugsgebietMap.putAll(loadEinzugsgebiet());
        mZusatzContainerMap.putAll(loadZusatzContainer());
//        mEscortMap.putAll(loadEscort());
        mVwdIntensivMap.putAll(loadVwdIntensivdauer());
        mDrgAbrechnungsartenMap.putAll(loadDrgAbrechnungsarten());
//        mVidierstufeMap.putAll(loadVidierstufen());
        mAbteilungenMap.putAll(loadAbteilungen());
        mStationenMap.putAll(loadStationen());
        mTransferHospLess24HoursMap.putAll(loadTransferHospLess24Hours());
        mInsurancesMap.putAll(loadInsurances());
        mEntbindungsdatenMap.putAll(loadEntbindungsdaten());
        mDrgBelegMap.putAll(loadDrgBeleg());
        mPatientenMap.putAll(loadPatienten());

        mFallMap.putAll(getFaelle());
        getPatienten();
        getUrlaub();
//        getPatienten();
        getEntgelte();
        getBewegungen();
        getMultiFaelle();
        getStationen();
        getDiagnosen();
        getProzeduren();
        return new TransformResult(patientCounter.get(), caseCounter.get(), exceptions);
    }

    public abstract Map<CASEKEYTYPE, Case> getFaelle() throws SQLException, IOException;

    public abstract void getUrlaub() throws SQLException, IOException;

    protected abstract Map<Integer, Integer> loadDrgBeleg() throws SQLException, IOException;

    public abstract void getPatienten() throws SQLException, IOException;

    public abstract void getEntgelte() throws SQLException, IOException;

    public abstract void getBewegungen() throws SQLException, IOException;

    public abstract void getMultiFaelle() throws SQLException, IOException;

    //public abstract void removeStorno(CpxWriter pCpxMgr);
    public abstract void getStationen() throws SQLException, IOException;

    public abstract void getDiagnosen() throws SQLException, IOException;

    public abstract void getProzeduren() throws SQLException, IOException;

    public abstract void getDrg() throws SQLException, IOException;

    public abstract void getLabordaten() throws SQLException, IOException;

    protected abstract Map<COREDATATYPE, String> loadAufnahmegrund12() throws SQLException, IOException;

    protected abstract Map<COREDATATYPE, String> loadAufnahmegrund34() throws SQLException, IOException;

    protected abstract Map<COREDATATYPE, String> loadEntlassungsgrund12() throws SQLException, IOException;

    protected abstract Map<COREDATATYPE, String> loadEntlassungsgrund3() throws SQLException, IOException;

    protected abstract Map<COREDATATYPE, String> loadAufnahmeanlass() throws SQLException, IOException;

    protected abstract Map<COREDATATYPE, Integer> loadDiagnosearten() throws SQLException, IOException;

    protected abstract Map<COREDATATYPE, TarifeContainer> loadTarife() throws SQLException, IOException;

    protected abstract Map<String, String> loadKostentraeger() throws SQLException, IOException;

    protected abstract Map<Integer, String> loadEinzugsgebiet() throws SQLException, IOException;

//    public boolean startImport(String aufVonDatum, String aufBisDatum, String entVonDatum, String entBisDatum,
//            String aufnahmeGruende, String aufnahmeArten, String diagnoseArten) {
//        //String uploadServer, String uploadDatabase, int uploadPort,
//        rc = kisAdapter.getFaelle(aufVonDatum, aufBisDatum, entVonDatum, entBisDatum, aufnahmeGruende, aufnahmeArten, uploadClient, ikz, this);
//        if (rc) {
//            rc = kisAdapter.getUrlaub(aufVonDatum, aufBisDatum, entVonDatum, entBisDatum, aufnahmeGruende, aufnahmeArten, this);
//        }
//        if (rc) {
//            rc = kisAdapter.getPatienten(aufVonDatum, aufBisDatum, entVonDatum, entBisDatum, aufnahmeGruende, aufnahmeArten, uploadClient, this);
//        }
//        if (rc) {
//            rc = kisAdapter.getEntgelte(aufVonDatum, aufBisDatum, entVonDatum, entBisDatum, aufnahmeGruende, aufnahmeArten, uploadClient, this);
//        }
//        if (rc) {
//            rc = kisAdapter.getBewegungen(aufVonDatum, aufBisDatum, entVonDatum, entBisDatum, aufnahmeGruende, aufnahmeArten, uploadClient, this);
//        }
//        if (rc) {
//            rc = kisAdapter.getMultiFaelle(aufVonDatum, aufBisDatum, entVonDatum, entBisDatum, aufnahmeGruende, aufnahmeArten, this);
//        }
//        if (rc) {
//            kisAdapter.removeStorno(aufVonDatum, aufBisDatum, entVonDatum, entBisDatum, aufnahmeGruende, aufnahmeArten, uploadClient, ikz, this);
//        }
//        //nach unten verschben für Belegabteilungen Medico Schnittstelle
//        //if(rc)
//        // rc = kisAdapter.sendBewegungen(uploadClient, this);
//        if (rc) {
//            rc = kisAdapter.sendFaelle(uploadClient, this);
//        }
//        if (rc) {
//            rc = kisAdapter.getStationen(aufVonDatum, aufBisDatum, entVonDatum, entBisDatum, aufnahmeGruende, aufnahmeArten, uploadClient, this);
//        }
//        if (rc) {
//            rc = kisAdapter.getDiagnosen(aufVonDatum, aufBisDatum, entVonDatum, entBisDatum, aufnahmeGruende, aufnahmeArten, uploadClient,
//                    diagnoseArten, this);
//        }
//        if (rc) {
//            rc = kisAdapter.getProzeduren(aufVonDatum, aufBisDatum, entVonDatum, entBisDatum, aufnahmeGruende, aufnahmeArten, uploadClient, this);
//        }
//        //verschoben für Belegabteilungen Medico Schnittstelle
//        if (rc) {
//            rc = kisAdapter.sendBewegungen(uploadClient, this);
//        }
//        if (rc) {
//            rc = kisAdapter.sendHosWard(uploadClient, this);
//        }
//        if (rc) {
//            rc = kisAdapter.getDRG(aufVonDatum, aufBisDatum, entVonDatum, entBisDatum, aufnahmeGruende, aufnahmeArten, uploadClient, this);
//        }
//        if (rc) {
//            uploadClient.createSecondaryDiagnoses();
//        }
//        if (rc) {
//            rc = kisAdapter.loadLaborValues(aufVonDatum, aufBisDatum, entVonDatum, entBisDatum, aufnahmeGruende, aufnahmeArten, uploadClient, ikz, this);
//        }
//        if (rc) {
//            rc = uploadClient.createIndexes(database, MultiFileImporter.imexIndexNames);
//        }
//        if (rc) {
//            rc = uploadClient.distibuteData(database, distMode, deleteCases, aufVonDatum, aufBisDatum, entVonDatum, entBisDatum);
//        }
//
//    }
//    protected String getDatumZeit(Date pDate) {
//        if (pDate != null) {
//            if (pDate.after(firstValidDate) && pDate.before(lastValidDate)) {
//                return sdfTime.format(pDate);
//            }
//        }
//        return "";
//    }
//    public Map<caseKeyType, ZusatzContainer> getZusatzContainerMap() {
//        return new HashMap<>(mZusatzContainerMap);
//    }
    public ZusatzContainer<BEATMUNGSZEITTYPE> getZusatzContainer(CASEKEYTYPE pFallKey) {
        ZusatzContainer<BEATMUNGSZEITTYPE> c = mZusatzContainerMap.get(pFallKey);
        if (c != null) {
            return c; //.getZusatzContainer();
        } else {
            return null;
        }
    }

    public ZusatzContainer<BEATMUNGSZEITTYPE> setZusatzContainer(CASEKEYTYPE pFallKey, ZusatzContainer<BEATMUNGSZEITTYPE> pZusatzContainer) {
        if (pZusatzContainer == null) {
            return null;
        }
        return mZusatzContainerMap.put(pFallKey, pZusatzContainer);
    }

    public int getBeatmungsdauer(CASEKEYTYPE pFallKey) {
        ZusatzContainer<BEATMUNGSZEITTYPE> c = getZusatzContainer(pFallKey);
        return c == null ? 0 : c.getBeatmungsdauer();
    }

    public List<BEATMUNGSZEITTYPE> getBeatmungszeiten(CASEKEYTYPE pFallKey) {
        ZusatzContainer<BEATMUNGSZEITTYPE> c = getZusatzContainer(pFallKey);
        return c == null ? null : c.getBeatmungszeiten();
    }

    public int getGeburtsgewicht(CASEKEYTYPE pFallKey) {
        ZusatzContainer<BEATMUNGSZEITTYPE> c = getZusatzContainer(pFallKey);
        return c == null ? 0 : c.getGeburtsgewicht();
    }

    public Map<CASEKEYTYPE, ZusatzContainer<BEATMUNGSZEITTYPE>> getZusatzContainerMap() {
        return new HashMap<>(mZusatzContainerMap);
    }

    public String getAufnahmegrund12(final COREDATATYPE pKey) {
        String val = mAufnahmegrund12Map.get(pKey);
        if (val != null) {
            return val;
        } else {
            return "1";
        }
    }

    public Map<COREDATATYPE, String> getAufnahmegrund12Map() {
        return new HashMap<>(mAufnahmegrund12Map);
    }

    public String getEntlassungsgrund12(final COREDATATYPE pKey) {
        String val = mEnlassungsgrund12Map.get(pKey);
        if (val != null) {
            return val;
        } else {
            return "1";
        }
    }

    public Map<COREDATATYPE, String> getEntlassungsgrund12Map() {
        return new HashMap<>(mEnlassungsgrund12Map);
    }

    public String getEntlassungsgrund3(final COREDATATYPE pKey) {
        String val = mEnlassungsgrund3Map.get(pKey);
        if (val != null) {
            return val;
        } else {
            return "9";
        }
    }

    public Map<COREDATATYPE, String> getEntlassungsgrund3Map() {
        return new HashMap<>(mEnlassungsgrund3Map);
    }

    public String getEinzugsgebiet(final Integer pKey) {
        String val = mEinzugsgebietMap.get(pKey);
        if (val != null) {
            return val;
        } else {
            return "";
        }
    }

    public Map<COREDATATYPE, Integer> getDiagnoseartenMap() {
        return new HashMap<>(mDiagnoseartenMap);
    }

    public Integer getDiagnoseart(final COREDATATYPE pKey) {
        Integer val = mDiagnoseartenMap.get(pKey);
        if (val != null) {
            return val;
        } else {
            return 0;
        }
    }

    public Map<String, Integer> getUrlaubMap() {
        return new HashMap<>(mUrlaubMap);
    }

    public Integer getUrlaub(final String pKey) {
        return mUrlaubMap.get(pKey);
    }

    public Map<String, String> getKostentraegerMap() {
        return new HashMap<>(mKostentraegerMap);
    }

    public String getKostentraeger(final String pKey) {
        return mKostentraegerMap.get(pKey);
    }

    public Map<String, Date> getEntbindungsdatenMap() {
        return new HashMap<>(mEntbindungsdatenMap);
    }

    public Date getEntbindungsdaten(final String pKey) {
        return mEntbindungsdatenMap.get(pKey);
    }

    public Map<COREDATATYPE, TarifeContainer> getTarifeMap() {
        return new HashMap<>(mTarifeMap);
    }

    public TarifeContainer getTarif(final COREDATATYPE pKey) {
        return mTarifeMap.get(pKey);
    }

    public TarifeGueltigContainer getGueltigenTarif(Date date, COREDATATYPE tid) {
        TarifeContainer t = mTarifeMap.get(tid);
        if (t != null) {
            return t.getGueltigenTarifContainer(date);
        } else {
            return null;
        }
    }

    public Map<CASEKEYTYPE, CASEKEYTYPE> getTranslationMap() {
        return new HashMap<>(mTranslationMap);
    }

    public CASEKEYTYPE addTranslation(final CASEKEYTYPE pFallNr, final CASEKEYTYPE pSuccNr) {
        return mTranslationMap.put(pFallNr, pSuccNr);
    }

    public CASEKEYTYPE getTranslation(final CASEKEYTYPE pFallNr) {
        return mTranslationMap.get(pFallNr);
    }

    public Map<Integer, String> getEinzugsgebietMap() {
        return new HashMap<>(mEinzugsgebietMap);
    }

    public Case getFall(final CASEKEYTYPE pFallKey) {
        return mFallMap.get(pFallKey);
    }

    protected Case removeFall(CASEKEYTYPE pFallKey) {
        return mFallMap.remove(pFallKey);
    }

    public Map<CASEKEYTYPE, Case> getFallMap() {
        return new HashMap<>(mFallMap);
    }

    protected String getDatum(Date pDate) {
        if (pDate == null) {
            return "";
        }
        final Date firstValidDate = new GregorianCalendar(1880, 1, 1, 0, 0, 0).getTime();
        final Date lastValidDate = new GregorianCalendar(2090, 1, 1, 0, 0, 0).getTime();
        if (pDate.after(firstValidDate) && pDate.before(lastValidDate)) {
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            return sdf.format(pDate);
        }
        return "";
    }

    protected String getDatumZeit(Date pDate) {
        if (pDate == null) {
            return "";
        }
        final Date firstValidDate = new GregorianCalendar(1880, 1, 1, 0, 0, 0).getTime();
        final Date lastValidDate = new GregorianCalendar(2090, 1, 1, 0, 0, 0).getTime();
        if (pDate.after(firstValidDate) && pDate.before(lastValidDate)) {
            final SimpleDateFormat sdfTime = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
            return sdfTime.format(pDate);
        }
        return "";
    }

    protected abstract Map<String, Integer> loadUrlaub() throws SQLException, IOException;

    protected abstract Map<CASEKEYTYPE, ZusatzContainer<BEATMUNGSZEITTYPE>> loadZusatzContainer() throws SQLException, IOException;

    protected abstract Map<String, Integer> loadEscort() throws SQLException, IOException;

    protected abstract Map<String, KisPatientContainer> loadPatienten() throws SQLException, IOException;

    protected abstract Map<CASEKEYTYPE, BigDecimal> loadVwdIntensivdauer() throws SQLException, IOException;

    protected abstract Map<String, Integer> loadDrgAbrechnungsarten() throws SQLException, IOException;

    protected abstract Map<Integer, Vidierstufe> loadVidierstufen() throws SQLException, IOException;

    protected abstract Map<DEPKEYTYPE, KisAbteilungContainer<DEPKEYTYPE>> loadAbteilungen() throws SQLException, IOException;

    protected abstract Map<String, Date> loadEntbindungsdaten() throws SQLException, IOException;

    public Map<CASEKEYTYPE, BigDecimal> getVwdIntensivMap() {
        return new HashMap<>(mVwdIntensivMap);
    }

    protected BigDecimal getVwdIntensiv(final CASEKEYTYPE pFallKey) {
        return mVwdIntensivMap.get(pFallKey);
//        String vwdIntensiv = ";0;";
//        if (mGwiIntensivtypIdent == null) {
//            return vwdIntensiv;
//        } else {
//            Number obj = m_vwdIntensivMap.get(pFallKey);
//            if (obj != null) {
//                return ";" + obj.intValue() + ";";
//            }
//        }
//        return vwdIntensiv;
    }

    public String getAufnahmeanlass(final COREDATATYPE pAufnahmeAnlassOrbisNumber) {
        return mAufnahmeAnlassMap.get(pAufnahmeAnlassOrbisNumber);
    }

    public String getAufnahmeanlassEinwKKH(final Integer pEinwKKH) {
        if (pEinwKKH != null && pEinwKKH > 0) {
            return "5";
        }
        return "1";
    }

    public String getAufnahmeanlassEinwKKH(final String pEinwKKH) {
        if (pEinwKKH != null && !pEinwKKH.trim().isEmpty()) {
            return "5";
        }
        return "1";
    }

    public String getAufnahmeanlass(String behart, String pEinwKKH, String pAufnahmegrund34, String pTransfHospLess24H) {
        if (pEinwKKH != null && !pEinwKKH.isEmpty()) {
            if (pTransfHospLess24H != null && pTransfHospLess24H.equalsIgnoreCase("J")) {
                return "9";
            } else {
                return "5";
            }
        }
        if (behart != null && behart instanceof String) {
            if (behart.equals("BP")) {
                return "8";
            }
            if (behart.equals("NG")) {
                return "7";
            }
        }
        if (pAufnahmegrund34 != null && pAufnahmegrund34.endsWith("7")) {
            return "3";
        }
        return "1";
    }

    public Map<COREDATATYPE, String> getAufnahmeAnlassMap() {
        return new HashMap<>(mAufnahmeAnlassMap);
    }

    public Integer getAufnahmeanlass4CP(final String pAufnahmeAnlassOrbis) {
        return mAufnahmeanlass4CpMap.get(pAufnahmeAnlassOrbis);
    }

    public Map<String, Integer> getAufnahmeanlass4CpMap() {
        return new HashMap<>(mAufnahmeanlass4CpMap);
    }

    private void initAufnahmeanlass4CP() {
        mAufnahmeanlass4CpMap.put("E", 1);
        mAufnahmeanlass4CpMap.put("Z", 2);
        mAufnahmeanlass4CpMap.put("N", 3);
        mAufnahmeanlass4CpMap.put("R", 4);
        mAufnahmeanlass4CpMap.put("V", 5);
        mAufnahmeanlass4CpMap.put("K", 6);
        mAufnahmeanlass4CpMap.put("G", 7);
        mAufnahmeanlass4CpMap.put("B", 8);
        mAufnahmeanlass4CpMap.put("A", 9);
    }

    public String getAufnahmegrund34(final COREDATATYPE pKey) {
        String val = mAufnahmegrund34Map.get(pKey);
        if (val != null) {
            return val;
        } else {
            return "1";
        }
    }

    public Map<COREDATATYPE, String> getAufnahmegrund34Map() {
        return new HashMap<>(mAufnahmegrund34Map);
    }

    public Vidierstufe getVidierstufe(final Integer pFallKey) {
        return mVidierstufeMap.get(pFallKey);
    }

    public Map<Integer, Vidierstufe> getVidierstufeMap() {
        return new HashMap<>(mVidierstufeMap);
    }

    public KisPatientContainer getPatient(final String pPatientenNr) {
        return mPatientenMap.get(pPatientenNr);
    }

    public Map<String, KisPatientContainer> getPatientenMap() {
        return new HashMap<>(mPatientenMap);
    }

    public String getPatientDeb(final String pPatientenNr) {
        KisPatientContainer result = getPatient(pPatientenNr);
        return result == null ? "" : result.getDeb();
    }

    public String getPatientVersNr(final String pPatientenNr) {
        KisPatientContainer result = getPatient(pPatientenNr);
        return result == null ? "" : result.getVersNr();
    }

    public KisAbteilungContainer<DEPKEYTYPE> getAbteilung(final DEPKEYTYPE pAbteilungKey) {
        return mAbteilungenMap.get(pAbteilungKey);
    }

    public Map<DEPKEYTYPE, KisAbteilungContainer<DEPKEYTYPE>> getAbteilungenMap() {
        return new HashMap<>(mAbteilungenMap);
    }

    public String getStation(final String pStationKey) {
        return mStationenMap.get(pStationKey);
    }

    public Map<String, String> getStationenMap() {
        return new HashMap<>(mStationenMap);
    }

    public String getTransferHospLess24Hours(final CASEKEYTYPE pFallKey) {
        return mTransferHospLess24HoursMap.get(pFallKey);
    }

    public Map<CASEKEYTYPE, String> getTransferHospLess24HoursMap() {
        return new HashMap<>(mTransferHospLess24HoursMap);
    }

    public KisInsuranceContainer getInsurance(final CASEKEYTYPE pFallKey) {
        return mInsurancesMap.get(pFallKey);
    }

    public Map<CASEKEYTYPE, KisInsuranceContainer> getInsurancesMap() {
        return new HashMap<>(mInsurancesMap);
    }

    public Integer getDrgBeleg(final Integer pFallKey) {
        return mDrgBelegMap.get(pFallKey);
    }

    public Map<Integer, Integer> getDrgBelegMap() {
        return new HashMap<>(mDrgBelegMap);
    }

    public Integer getDrgAbrechnungsart(final String pDrgAbrechnungsart) {
        return mDrgAbrechnungsartenMap.get(pDrgAbrechnungsart);
    }

    public Map<String, Integer> getDrgAbrechnungsartenMap() {
        return new HashMap<>(mDrgAbrechnungsartenMap);
    }

//    private BigDecimal getVwdIntensiv(final Integer pFallKey) {
//        return m_vwdIntensivMap.get(pFallKey);
////        String vwdIntensiv = ";0;";
////        if (mProperties.mGwiIntensivtypIdent == null) {
////            return vwdIntensiv;
////        } else {
////            Number obj = m_vwdIntensivMap.get(pFallKey);
////            if (obj != null) {
////                return ";" + obj.intValue() + ";";
////            }
////        }
////        return vwdIntensiv;
//    }
    protected static Integer[] getAge(final Date pAufnahmedatum, final Date pGeburtsdatum) {
        if (pAufnahmedatum != null && pGeburtsdatum != null) {
            final Date firstValidDate = new GregorianCalendar(1880, 1, 1, 0, 0, 0).getTime();
            final Date lastValidDate = new GregorianCalendar(2090, 1, 1, 0, 0, 0).getTime();
            if (pAufnahmedatum.after(firstValidDate)
                    && pGeburtsdatum.after(firstValidDate)
                    && pGeburtsdatum.before(lastValidDate)) {
                long aT = pAufnahmedatum.getTime() / (1000 * 60 * 60 * 24);
                long gT = pGeburtsdatum.getTime() / (1000 * 60 * 60 * 24);
                int diff = (int) (aT - gT);
                if (diff >= 0 && diff < 366) {
                    if (diff < 1) {
                        diff = 1;
                    }
                    return new Integer[]{diff, null};
                } else if (diff >= 0) {
                    diff = ((diff + 1) * 4) / (3 * 365 + 366);
                    if (diff < 1) {
                        diff = 1;
                    }
                    return new Integer[]{null, diff};
                }
            }
        }
        return new Integer[]{null, null};
    }

    protected static Integer getAgeInDays(final Date pAufnahmedatum, final Date pGeburtsdatum) {
        Integer[] result = getAge(pAufnahmedatum, pGeburtsdatum);
//        if (result == null) {
//            return null;
//        }
        return result[0];
    }

    protected static Integer getAgeInYears(final Date pAufnahmedatum, final Date pGeburtsdatum) {
        Integer[] result = getAge(pAufnahmedatum, pGeburtsdatum);
//        if (result == null) {
//            return null;
//        }
        return result[1];
    }

//    public static void main(String[] args) {
//        Calendar c1 = Calendar.getInstance();
//        Calendar c2 = Calendar.getInstance();
//        c1.set(Calendar.YEAR, 2005);
//        c1.set(Calendar.DAY_OF_MONTH, 15);
//        c2.set(Calendar.YEAR, 2005);
//        System.out.println(getAge(c2.getTime(), c1.getTime()));
//    }
    public String getDefaultHosIdent() {
        return getInputConfig().getDefaultHosIdent();
    }

    public String getMandant(final String pFallnr, final String pDefaultIK) {
//        if (mProperties.mVecMandanten != null) {
        for (String obj : mProperties.getVecMandanten()) {
            if (obj == null) {
                continue;
            }
            if (pFallnr.startsWith(obj)) {
                String mandant = mProperties.getGwiMandanten().get(obj);
                if (mandant != null) {
                    return mandant;
                }
            }
        }
//        }
        return pDefaultIK;
    }

    protected char getGeschlecht(String pValue) {
        final char def = 'u';
        if (pValue != null && pValue.length() > 0) {
            char c = pValue.charAt(0);
            switch (c) {
                case 'M':
                case 'm':
                    return 'm';
                case 'W':
                case 'w':
                    return 'w';
                default:
                    return def;
            }
        }
        return def;
    }

    protected int getVwd(final Date pAnfang, final Date pEnde) {
        if (pAnfang != null && pEnde != null) {
            Calendar tempCalendar = new GregorianCalendar(TimeZone.getTimeZone("ECT"));
            tempCalendar.setTime(pAnfang);
            tempCalendar.set(Calendar.HOUR_OF_DAY, 0);
            tempCalendar.set(Calendar.MINUTE, 0);
            tempCalendar.set(Calendar.SECOND, 0);
            tempCalendar.set(Calendar.MILLISECOND, 0);
            final Date anfang = tempCalendar.getTime();
            tempCalendar.setTime(pEnde);
            tempCalendar.set(Calendar.HOUR_OF_DAY, 0);
            tempCalendar.set(Calendar.MINUTE, 0);
            tempCalendar.set(Calendar.SECOND, 0);
            tempCalendar.set(Calendar.MILLISECOND, 0);
            final Date ende = tempCalendar.getTime();
            int e = (int) (ende.getTime() / (1000 * 60 * 60 * 24));
            int a = (int) (anfang.getTime() / (1000 * 60 * 60 * 24));
            int diff = e - a;
            if (diff > 0) {
                return diff;
            }
        }
        return 0;
    }

    public boolean isSqlsrv() {
        return mInputConfig.isSqlsrv();
    }

    public boolean isOracle() {
        return mInputConfig.isOracle();
    }

    public boolean isIngres() {
        return mInputConfig.isIngres();
    }

    public String getNullFunction() {
        return mInputConfig.getNullFunction();
    }

    protected boolean checkSameDay(final Date pAnfang, final Date pEnde) {
        if (pAnfang != null && pEnde != null) {
            final Calendar tempCalendar = new GregorianCalendar(TimeZone.getTimeZone("ECT"));
            final Calendar medCalendar = new GregorianCalendar(TimeZone.getTimeZone("ECT"));
            tempCalendar.setTime(pAnfang);
            medCalendar.setTime(pEnde);
            return tempCalendar.get(Calendar.DAY_OF_MONTH) == medCalendar.get(Calendar.DAY_OF_MONTH)
                    && tempCalendar.get(Calendar.MONTH) == medCalendar.get(Calendar.MONTH)
                    && tempCalendar.get(Calendar.YEAR) == medCalendar.get(Calendar.YEAR);
        }
        return false;
    }

    public String getAufnahmedatumVon() {
        return mAufnahmedatumVon;
    }

    public String getAufnahmedatumBis() {
        return mAufnahmedatumBis;
    }

    public String getEntlassungsdatumVon() {
        return mEntlassungsdatumVon;
    }

    public String getEntlassungsdatumBis() {
        return mEntlassungsdatumBis;
    }

    protected static String getLokalisation(final String pSeite) {
        final String def = "0";
        if (pSeite != null && pSeite.length() > 0) {
            char s = pSeite.charAt(0);
            switch (s) {
                case 'r':
                case 'R':
                    return "1";
                case 'l':
                case 'L':
                    return "2";
                case 'b':
                case 'B':
                    return "3";
                default:
                    return def;
            }
        }
        return def;
    }

    protected String getOpsVersion(Date pDate) {
        if (pDate != null) {
            final Calendar tempCalendar = new GregorianCalendar(TimeZone.getTimeZone("ECT"));
            tempCalendar.setTime(pDate);
            int year = tempCalendar.get(Calendar.YEAR);
            if (year <= 2003) {
                return "2.1";
            }
            /*if(year == 2004)
    return "2004";
   if(year == 2005)
    return "2005";
   if(year == 2006)
    return "2006";
   if(year == 2007)
    return "2007";
   if(year == 2008)
    return "2008";*/
            return String.valueOf(year);
        }
        return "";
    }

    protected String getIcdVersion(Date pDate) {
        if (pDate != null) {
            final Calendar tempCalendar = new GregorianCalendar(TimeZone.getTimeZone("ECT"));
            tempCalendar.setTime(pDate);
            int year = tempCalendar.get(Calendar.YEAR);
            if (year <= 2003) {
                return "2.0";
            }
            /*   if(year == 2004)
    return "2004";
   if(year == 2005)
    return "2005";
   if(year == 2006)
    return "2006";
   if(year == 2007)
    return "2007";
   if(year == 2008)
       return "2008";*/
            return String.valueOf(year);
        }
        return "";
    }

    protected void calculateMulticaseLeaveDays(final Collection<CASEKEYTYPE> pCol) {
        if (pCol != null) {
            List<CASEKEYTYPE> v = new ArrayList<>(pCol);
//            Collections.sort(v, new Comparator<Integer>() {
//                @Override
//                public int compare(Integer o1, Integer o2) {
//                    return Integer.compare(o1, o2);
//                }
//            });
            Case f = null;
            CASEKEYTYPE lastCaseID = null;
            for (CASEKEYTYPE obj : v) {
                if (obj == null) {
                    continue;
                }
                if (lastCaseID == null || !obj.equals(lastCaseID)) {
                    f = getFall(obj);
                    lastCaseID = obj;
                    if (f != null) {
                        //this.calculateLeaveDays(f.getBewegungen(), f);
                        this.calculateLeaveDays(new ArrayList<>(f.getDepartments()), f);
                    }
                }
            }
        }
    }

    protected void calculateLeaveDays(final List<Department> pEncounters, final Case pIntCase) {
        Calendar cal = new GregorianCalendar();
        long vwd = 0;
        long episode = 0;
        long sumEpisode = 0;
        long urlaub = 0;
        long shortStay = 0;
        long l1;
        long l = 0;
//        int aufOrentlFab = 0;
        Date fallBeginn = null;
        Date fallEnde = null;
        Date episodeBeginn = null;
        Date episodeEnde = null;
        Date lastEpisodeEnde = null;
        Department maxDurEnc = null;
        for (int i = 0, n = pEncounters.size(); i < n; i++) {
            Department enc = pEncounters.get(i);
//            enc.setAufnehmende((byte) '0');
//            enc.setBehandelnde((byte) '0');
//            enc.setEntlassende((byte) '0');
//            if (i == n - 1) {
//                enc.setEntlassende((byte) '1');
//            }
//            enc.setNummer(i + 1);
            if (!enc.is0001() && !enc.is0002()) {
                if (enc.getEntlassungsdatum() != null && enc.getVerlegungsdatum() != null) {
                    l1 = enc.getEntlassungsdatum().getTime() - enc.getVerlegungsdatum().getTime();
                    if (l1 > l) {
                        l = l1;
                        maxDurEnc = enc;
                    }
                }
                if (fallBeginn == null) {
                    fallBeginn = enc.getVerlegungsdatum();
//                    enc.setAufnehmende((byte) '1');
                }
//                } else {
//                    enc.setAufnehmende((byte) '0');
//                }
                if (enc.getEntlassungsdatum() != null) {
                    fallEnde = enc.getEntlassungsdatum();
                    episodeEnde = enc.getEntlassungsdatum();
                }
                //LOG.info("Anfang: " + enc.anfang +" Ende: " +enc.ende +" Abteilung: " + enc.abteilung301);
                if (episodeBeginn != null) {
                    if (lastEpisodeEnde != null) {
                        String adm = UtlDateTimeConverter.converter().convertDateToExportString(enc.getVerlegungsdatum(), false);
                        String dis = UtlDateTimeConverter.converter().convertDateToExportString(lastEpisodeEnde, false);
                        if (!adm.equals(dis)) {
                            episode = getDaysBetweenDates(episodeBeginn, lastEpisodeEnde);
                            if (i == n - 1) {
                                //letzte Bewegung Kurzaufenthalt?
                                cal.setTime(lastEpisodeEnde);
                                int disDayLast = cal.get(Calendar.DAY_OF_YEAR);
                                cal.setTime(enc.getVerlegungsdatum());
                                int admDayAct = cal.get(Calendar.DAY_OF_YEAR);
                                if (disDayLast != admDayAct) {
                                    shortStay = 1;
                                }
                            }
                            sumEpisode += episode;
                            //LOG.info("Episode Beginn: "+ episodeBeginn +
                            // "Episode Ende: " + lastEpisodeEnde +"Episode Dauer: "+episode);
                            episodeBeginn = enc.getVerlegungsdatum();
                            lastEpisodeEnde = enc.getEntlassungsdatum();
                        } else {
                            lastEpisodeEnde = enc.getEntlassungsdatum();
                        }
                    }
                } else {
                    episodeBeginn = enc.getVerlegungsdatum();
                    lastEpisodeEnde = enc.getEntlassungsdatum();
                }
            }
        }
        //Berechnen der letzten regulären Bewegung
        if (episodeBeginn != null && episodeEnde != null) {
            episode = getDaysBetweenDates(episodeBeginn, episodeEnde);
            if (shortStay == 1) {
                //LOG.info("Episode Dauer: "+ String.valueOf(episodeEnde.getTime()-episodeBeginn.getTime()));
                if ((episodeEnde.getTime() - episodeBeginn.getTime()) >= (24 * 60 * 60 * 1000)) {
                    shortStay = 0;
                }
            }

        }
        //LOG.info("Episode Beginn: "+ episodeBeginn +
        //"Episode Ende: " + lastEpisodeEnde +"Episode Dauer: "+episode);
        sumEpisode += episode;
        if (fallBeginn != null && fallEnde != null) {
            vwd = getDaysBetweenDates(fallBeginn, fallEnde);
        }
        vwd += shortStay;
        urlaub = vwd - sumEpisode;
        if (urlaub < 0) {
            urlaub = 0;
        }
        //LOG.info("S-Episoden: " + sumEpisode + " VWD: " + vwd + " Urlaub: " + urlaub);
        pIntCase.setUrlaubstage((int) urlaub);
//        pIntCase.setUrlaub((int) urlaub);
//        pIntCase.setUrlaubBis((int) vwd - (int) urlaub);
        if (fallEnde != null) {
            pIntCase.setEntlassungsdatum(fallEnde);
        }
//        if (maxDurEnc != null) {
//            maxDurEnc.setBehandelnde((byte) '1');
//        }
    }

    protected int getDaysBetweenDates(final Date pAdmissionDate, final Date pDischargeDate) {
        int alos = 0;
        Calendar admDt = new GregorianCalendar();
        Calendar disDt = new GregorianCalendar();
        Calendar yearDt = new GregorianCalendar();

        admDt.setTime(pAdmissionDate);
        disDt.setTime(pDischargeDate);

        if (admDt.get(Calendar.YEAR) < disDt.get(Calendar.YEAR)) {
            alos = admDt.getMaximum(Calendar.DAY_OF_YEAR) - admDt.get(Calendar.DAY_OF_YEAR);
            for (int beg = admDt.get(Calendar.YEAR) + 1; beg < disDt.get(Calendar.DAY_OF_YEAR); beg++) {
                yearDt.set(Calendar.YEAR, beg);
                alos += yearDt.getMaximum(Calendar.DAY_OF_YEAR);
            }
            alos += disDt.get(Calendar.DAY_OF_YEAR);
        } else {
            alos = disDt.get(Calendar.DAY_OF_YEAR) - admDt.get(Calendar.DAY_OF_YEAR);
        }
        if (alos == 0) {
            alos = 1;
        }
        //ExcLogfile.printClientDebugLine("Episode Beginn: "+ adm +
        //      "Episode Ende: " + dis +"Episode Dauer: "+alos);
        return alos;
    }

    protected void addBeatmungsdauer(final CASEKEYTYPE pFallKey, final int pDauer) {
        ZusatzContainer<BEATMUNGSZEITTYPE> c = mZusatzContainerMap.get(pFallKey);
        if (c == null) {
            c = new ZusatzContainer<>();
            mZusatzContainerMap.put(pFallKey, c);
        }
        c.setBeatmungsdauer(c.getBeatmungsdauer() + pDauer);
    }

    protected void setBeatmungsdauer(final CASEKEYTYPE pFallKey, final int pDauer) {
        ZusatzContainer<BEATMUNGSZEITTYPE> c = mZusatzContainerMap.get(pFallKey);
        if (c == null) {
            c = new ZusatzContainer<>();
            mZusatzContainerMap.put(pFallKey, c);
        }
        c.setBeatmungsdauer(pDauer);
    }

    protected void setBeatmungszeiten(final CASEKEYTYPE pFallKey, final List<BEATMUNGSZEITTYPE> pBeatmungszeiten) {
        ZusatzContainer<BEATMUNGSZEITTYPE> c = mZusatzContainerMap.get(pFallKey);
        if (c == null) {
            c = new ZusatzContainer<>();
            mZusatzContainerMap.put(pFallKey, c);
        }
        c.setBeatmungszeiten(pBeatmungszeiten);
    }

    //GKr 26.11.2013 Zusatz, da in UK Achen diese Feld manchmal länger als die bei uns zulässigen 50 Zeichen
    protected String checkKasseStringLength(String toCheck) {
        int cutLength = 48;
        if (toCheck != null) {
            String text = toCheck;
            try {
                byte[] bytes = text.getBytes("UTF-8");
                if (bytes.length > cutLength) {
                    cutLength -= bytes.length - text.length();
                }
            } catch (UnsupportedEncodingException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
            if (text.length() > cutLength) {
                text = text.substring(0, cutLength);
            }
            //System.out.println("GKr-> text:" +text);
            return text;
        } else {
            return null;
        }
    }

//    protected abstract Map<Integer, Map<String, String>> loadPossibleParams();
//
//    protected Map<String, String> getPossibleImportParams(final int pKisType) {
////        if (m_possibleImportParam == null) {
////            initPossibleIportParams();
////        }
//        Map<String, String> hm = mPossibleParams.get(pKisType);
//        if (hm != null) {
//            return hm;
//        } else {
//            hm = new HashMap<>();
//            mPossibleParams.put(pKisType, hm);
//            return hm;
//        }
//    }
//
////    public boolean containsImportParam(final String pKey) {
////        mImportParam.contains(pKey);
////    }
////
////    public void setImportParameter(final String pParam) {
////        mImportParam.clear();
////        if (pParam != null && pParam.length() > 0) {
////            String[] params = pParam.split(",");
////            for (int i = 0, n = params.length; i < n; i++) {
////                mImportParam.add(params[i].trim());
////            }
////        }
////    }
//
//    public void addImportParam(final int pKisType, final String pKey, final String pDescription) {
//        getPossibleImportParams(pKisType).put(pKey, pDescription);
//    }
    protected abstract String getProzedurenQuery();

    protected abstract String getDiagnosenQuery();

    protected abstract String getEntgelteQuery();

    protected abstract String getBewegungenQuery();

    protected abstract String getStationenQuery();

    protected abstract String getFaelleQuery();

    protected abstract String getLabordatenQuery();

    protected abstract String getPatientenQuery();

    protected abstract String getUrlaubQuery();

    protected abstract String getStornoQuery();

    /**
     * @return the mAufnahmeanlaesse
     */
    public Set<AdmissionCauseEn> getAufnahmeanlaesse() {
        return Collections.unmodifiableSet(mAufnahmeanlaesse);
    }

    /**
     * @return the mAufnahmegruende
     */
    public Set<AdmissionReasonEn> getAufnahmegruende() {
        return Collections.unmodifiableSet(mAufnahmegruende);
    }

    /**
     * @return the mDiagnosearten
     */
    public Set<IcdcTypeEn> getDiagnosearten() {
        return Collections.unmodifiableSet(mDiagnosearten);
    }

    public void createDefaultBewegung(final Case pCase,
            final Date pStartDate, final Date pEndDate) {
        Department b = new Department(pCase);
        if (pStartDate != null) {
            b.setVerlegungsdatum(pStartDate);
        }
        if (pEndDate != null) {
            b.setEntlassungsdatum(pEndDate);
        }
        //getBewegungen().clear();
        //getBewegungen().add(b);
    }

    public static Ward getStation(Date date, Case fall) {
        if (date == null) {
            return null;
        }
        List<Ward> stationen = new ArrayList<>(fall.getWards());
        for (int i = 0, n = stationen.size(); i < n; i++) {
            Ward station = stationen.get(i);
            if (station.getVerlegungsdatum() != null && station.getEntlassungsdatum() != null
                    && date.compareTo(station.getVerlegungsdatum()) >= 0 && date.compareTo(station.getEntlassungsdatum()) <= 0) {
                return station;
            }
            //wenn letzte Station ohne Entlassungsdatum
            if (station != null && station.getVerlegungsdatum() != null && date.compareTo(station.getVerlegungsdatum()) >= 0) {
                return station;
            }
        }
        if (!stationen.isEmpty()) {
            return stationen.get(stationen.size() - 1);
        }
        return null;
    }

//    public Ward getStation(Date pDate, Case pFall) {
//        if (pDate == null) {
//            return null;
//        }
//        Date date = pDate;
//        Set<Ward> wards = pFall.getWards();
//        for (Ward ward : wards) {
//            if (ward.getVerlegungsdatum() != null && ward.getEntlassungsdatum() != null
//                    && (ward.getVerlegungsdatum().before((Date) pDate) && ward.getEntlassungsdatum().after((Date) pDate)
//                    || ward.getVerlegungsdatum().equals(pDate) || ward.getEntlassungsdatum().equals(pDate))) {
//                return ward;
//            } else if (ward.getVerlegungsdatum() != null && (ward.getVerlegungsdatum().before((Date) pDate) || ward.getVerlegungsdatum().equals(pDate))) {
//                return ward;
//            }
//        }
//        for (Ward ward : wards) {
//            //station = (KisHosWardContainer) stationen.get(i);
//            if (ward.getEntlassungsdatum() != null && (ward.getEntlassungsdatum().after((Date) pDate) || ward.getEntlassungsdatum().equals(pDate))) {
//                return ward;
//            }
//        }
//
//        Ward lastWard = null;
//        for (Ward ward : wards) {
//            lastWard = ward;
//        }
//        return lastWard;
//    }
    public Department getBewegung(Date date, Case fall, String p301) {
        List<Department> bewegungen = new ArrayList<>(fall.getDepartments());
        int size = bewegungen.size();

        if (date != null) {
            for (int i = size - 1; i >= 0; i--) {
                Department b = bewegungen.get(i);
                if (b.getVerlegungsdatum() != null && b.getEntlassungsdatum() != null
                        && (b.getVerlegungsdatum().before(date) && b.getEntlassungsdatum().after(date)
                        || b.getVerlegungsdatum().equals(date) || b.getEntlassungsdatum().equals(date))) {
                    if (p301 != null) {
                        if (p301.equalsIgnoreCase(b.getCode())) {
                            return b;
                        } else {
                            for (int j = size - 1; j >= 0; j--) {
                                Department b2 = bewegungen.get(j);
                                if (p301.equalsIgnoreCase(b2.getCode())) {
                                    Calendar tempCalendar = new GregorianCalendar(TimeZone.getTimeZone("ECT"));
                                    tempCalendar.setTime(new Date(b2.getVerlegungsdatum().getTime()));
                                    tempCalendar.set(Calendar.HOUR_OF_DAY, 0);
                                    tempCalendar.set(Calendar.MINUTE, 0);
                                    tempCalendar.set(Calendar.SECOND, 0);
                                    tempCalendar.set(Calendar.MILLISECOND, 0);
                                    Date chkDate = tempCalendar.getTime();
                                    if ((chkDate.before(date) && b.getEntlassungsdatum().after(date)
                                            || chkDate.equals(date) || b.getEntlassungsdatum().equals(date))) {
                                        return b2;
                                    }
                                }
                            }
                            return b;
                        }
                    } else {
                        return b;
                    }
                } else if (b.getVerlegungsdatum() != null && (b.getVerlegungsdatum().before(date) || b.getVerlegungsdatum().equals(date))) {
                    return b;
                }
            }
            for (int i = 0; i < size; i++) {
                Department b = bewegungen.get(i);
                if (b.getEntlassungsdatum() != null && (b.getEntlassungsdatum().after(date) || b.getEntlassungsdatum().equals(date))) {
                    return b;
                }
            }
        }
        if (size >= 1) {
            Department b = bewegungen.get(size - 1);
            return b;
        }
        return null;
    }

    public static Department getBewegung(Date date, Case fall) {
        Department b;
        List<Department> bewegungen = new ArrayList<>(fall.getDepartments());
        int size = bewegungen.size();

        if (date != null) {
            if (size > 0) {
//                if (dateOrAbtID instanceof String) {
//                    for (int i = size - 1; i >= 0; i--) {
//                        b = bewegungen.get(i);
//                        if (b.getTpId().equals(dateOrAbtID)) {
//                            return b;
//                        }
//                    }
//                } else {
                for (int i = size - 1; i >= 0; i--) {
                    b = bewegungen.get(i);
                    if (b.getVerlegungsdatum() != null && b.getEntlassungsdatum() != null
                            && (b.getVerlegungsdatum().before(date) && b.getEntlassungsdatum().after(date)
                            || b.getVerlegungsdatum().equals(date) || b.getEntlassungsdatum().equals(date))) {
                        return b;
                    } else if (b.getVerlegungsdatum() != null && (b.getVerlegungsdatum().before(date) || b.getVerlegungsdatum().equals(date))) {
                        return b;
                    }
                }
                for (int i = 0; i < size; i++) {
                    b = bewegungen.get(i);
                    if (b.getEntlassungsdatum() != null && (b.getEntlassungsdatum().after(date) || b.getEntlassungsdatum().equals(date))) {
                        return b;
                    }
                }
            }
            b = bewegungen.get(size - 1);
            return b;
//            }
        }
        if (size >= 1) {
            b = bewegungen.get(size - 1);
            return b;
        }
        return null;
    }

    public int getKisFallStatus(CASEKEYTYPE fallNr) {
        if (fallNr != null) {
            ZusatzContainer<BEATMUNGSZEITTYPE> c = mZusatzContainerMap.get(fallNr);
            if (c != null) {
                if (c.isMcAnfrage()) {
                    return 3; // RmcCaseAdminMgr_rm.KISSMED_STATUS_OFFSET + RmcCaseAdminMgr_rm.KIS_STATUS_ABRECHNUNGSFREIGABE;
                } else if (c.isAbrechnungsFreigabe()) {
                    return 2; // RmcCaseAdminMgr_rm.KISSMED_STATUS_OFFSET + RmcCaseAdminMgr_rm.KIS_STATUS_ABRECHNUNGSFREIGABE;
                } else if (c.isFallFreigabe()) {
                    return 1; // RmcCaseAdminMgr_rm.KISSMED_STATUS_OFFSET + RmcCaseAdminMgr_rm.KIS_STATUS_FALLFREIGABE;
                }
            }
        }
        return 0; // RmcCaseBase.KISSMED_STATUS_OFFSET + RmcCaseBase.KIS_STATUS_UNBEKANNT
    }

    public abstract void removeStorno() throws SQLException, IOException;

    protected String getAbteilungName(DEPKEYTYPE pAbteilungKey) {
        KisAbteilungContainer<DEPKEYTYPE> dep = getAbteilung(pAbteilungKey);
        if (dep != null) {
            return dep.getName();
        } else {
            return "<" + Constants.CHECKRESULT_TYP_UNKNOWN + ">";
        }
    }

    protected abstract Map<String, String> loadStationen() throws SQLException, IOException;

    protected abstract String getNachfolgerQuery();

    protected abstract Map<CASEKEYTYPE, String> loadTransferHospLess24Hours() throws SQLException, IOException;

    protected abstract Map<CASEKEYTYPE, KisInsuranceContainer> loadInsurances() throws SQLException, IOException;

//    protected int getBeatmungsdauer(final Date pBeginn, final Date pEnde) {
//        long timediff = pEnde.getTime() - pBeginn.getTime();
//        BigDecimal beaDiff = new BigDecimal(timediff);
//        BigDecimal hour = new BigDecimal(1000 * 60 * 60);
//        MathContext m_roundDecimalContext = new MathContext(5, java.math.RoundingMode.HALF_UP);
//        beaDiff = beaDiff.divide(hour, m_roundDecimalContext);
//        if (beaDiff.doubleValue() > 0.001) {
//            //b.addVwdIntensiv(((Number) beatmung).doubleValue());
//        }
//        return beaDiff.intValue();
//    }
}
