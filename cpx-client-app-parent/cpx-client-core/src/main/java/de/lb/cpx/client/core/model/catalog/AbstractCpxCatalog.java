/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.catalog;

import de.lb.cpx.client.core.connection.database.CpxDbManager;
import de.lb.cpx.server.commons.dao.AbstractCatalogEntity;
import de.lb.cpx.service.information.CatalogTypeEn;
import de.lb.cpx.service.information.CpxCatalogOverview;
import java.lang.reflect.UndeclaredThrowableException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Dirk Niemeier
 * @param <CpxType> CpxIcd, CpxOps, CpxHospital, CpxInsuranceCompany...
 * @param <EntityType> CIcdCatalog, COpsCatalog, CHospital, CInsuranceCompany
 */
public abstract class AbstractCpxCatalog<CpxType extends AbstractCatalogEntity, EntityType extends AbstractCatalogEntity> {

    private static final Logger LOG = Logger.getLogger(AbstractCpxCatalog.class.getName());
    public static final String DEFAULT_COUNTRY = "de";

    public static AbstractCpxCatalog<? extends AbstractCatalogEntity, ? extends AbstractCatalogEntity> factory(final CatalogTypeEn pCatalogType) {
        if (pCatalogType != null) {
            switch (pCatalogType) {
                case DRG:
                    return CpxDrgCatalog.instance();
                case PEPP:
                    return CpxPeppCatalog.instance();
                case ICD:
                    return CpxIcdCatalog.instance();
                case OPS:
                    return CpxOpsCatalog.instance();
                case OPS_AOP:
                    return CpxOpsAopCatalog.instance();
                case HOSPITAL:
                    return CpxHospitalCatalog.instance();
                case INSURANCE_COMPANY:
                    return CpxInsuranceCompanyCatalog.instance();
                case DEPARTMENT:
                    return CpxDepartmentCatalog.instance();
                case DOCTOR:
                    return CpxDoctorCatalog.instance();
                case ATC:
                    return CpxAtcCatalog.instance();
                case PZN:
                    return CpxPznCatalog.instance();
                case MDK:
                    return CpxMdkCatalog.instance();
                case ZE:
                    return CpxSupplementaryDrgFeeCatalog.instance();
                case ZP:
                    return CpxSupplementaryPeppFeeCatalog.instance();
                case ET:
                    return CpxSupplementaryDailyFeeCatalog.instance();
                case BASERATE:
                    return CpxBaserateCatalog.instance();
                case ICD_THESAURUS:
                    return CpxIcdThesaurusCatalog.instance();
                case OPS_THESAURUS:
                    return CpxOpsThesaurusCatalog.instance();
                default: 
                    LOG.log(Level.SEVERE, "Cannot create CPX Catalog for given Catalog Type ''{0}''", pCatalogType.name());
                    return null;
            }
        }
        LOG.log(Level.SEVERE, "Cannot create CPX Catalog for given Catalog Type ''{0}''", "null");
        return null;
    }

    public static int getActualYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static int checkYear(final Integer pYear) {
        if (pYear == null || pYear <= 0) {
            return getActualYear();
        }
        return pYear;
    }

    public static void vakuumDb(Connection pConnection) throws SQLException {
        if (pConnection == null) {
            return;
        }
        try (Statement stmt = pConnection.createStatement()) {
            stmt.execute("END TRANSACTION");
            stmt.execute("VACUUM");
        }
    }

    /*
  public static Connection getCatalogDb(final Integer pYear) {
    return CpxDbManager.instance().getCatalogDb(pYear); 
  }
     */
    public static Connection getCatalogDb(final CpxCatalogOverview pCatalogOverview) {
        final boolean autoCreateConnection = false;
        return CpxDbManager.instance().getCatalogDb(pCatalogOverview, autoCreateConnection);
    }

    public static Connection getCatalogDb(final CpxCatalogOverview pCatalogOverview, final boolean pAutoCreateConnection) {
        return CpxDbManager.instance().getCatalogDb(pCatalogOverview, pAutoCreateConnection);
    }

    public static Connection getCatalogDb(final CatalogTypeEn pCatalogType, final Integer pYear, final boolean pAutoCreateConnection) {
        return CpxDbManager.instance().getCatalogDb(pCatalogType, pYear, pAutoCreateConnection);
    }

    public static Connection getCatalogDb(final CatalogTypeEn pCatalogType, final Integer pYear) {
        final boolean autoCreateConnection = false;
        return CpxDbManager.instance().getCatalogDb(pCatalogType, pYear, autoCreateConnection);
    }

//    public static PreparedStatement prepareStatement(final CpxCatalogOverview pCatalogOverview, final String pSql) throws SQLException {
//        final boolean autoCreateConnection = false;        
//        Connection conn = getCatalogDb(pCatalogOverview, autoCreateConnection);
//        if (conn == null) {
//            return null;
//        }
//        return conn.prepareStatement(pSql);
//    }
    public static PreparedStatement prepareStatement(CatalogTypeEn pCatalogType, final Integer pYear, final String pSql) throws SQLException {
        final boolean autoCreateConnection = false;
        Connection conn = getCatalogDb(pCatalogType, pYear, autoCreateConnection);
        if (conn == null) {
            return null;
        }
        return conn.prepareStatement(pSql);
    }

    public static String getTableName(final CpxCatalogOverview pCatalogOverview) {
        return getTableName(pCatalogOverview.getCatalog(), pCatalogOverview.getCountryEn(), pCatalogOverview.getYear());
    }

    public static String getTableName(final CatalogTypeEn pCatalog, final String pCountryEn, final int pYear) {
        String catalog = (pCatalog == null) ? "" : pCatalog.name();
        return getTableName(catalog, pCountryEn, pYear);
    }

    /*
  public String getTableName(final String pCountryEn, final int pYear) {
    CatalogTypeEn catalogType = getCatalogType();
    String catalog = (catalogType == null)?"":catalogType.name();
    return getTableName(catalog, pCountryEn, pYear);
  }
     */
    public static String getTableName(final String pCatalog, final String pCountryEn, final int pYear) {
        //int year = checkYear(pYear);
        int year = pYear;
        String catalog = (pCatalog == null) ? "" : pCatalog.trim().toUpperCase();
        if (catalog.isEmpty()) {
            catalog = "INVALID_CATALOG";
        }
        String country = (pCountryEn == null) ? "" : pCountryEn.trim().toUpperCase();
        if (country.isEmpty()) {
            country = "INVALID_COUNTRY";
        }
        String tableName = catalog + "_" + country;
        if (year > 0) {
            tableName += "_" + year;
        }
        //tableName += "_" + year;
        return tableName;
    }

    public static synchronized int getCatalogCount(final CpxCatalogOverview pCatalogOverview) {
        final boolean autoCreateConnection = true;
        int count = -1;
        final String tableName = getTableName(pCatalogOverview);
        String sql = String.format("SELECT COUNT(*) CNT FROM %s", tableName);
        try (PreparedStatement pstmt = getCatalogDb(pCatalogOverview, autoCreateConnection).prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    count = rs.getInt(1);
                    break;
                }
                rs.close();
            } finally {
                if (pstmt != null) {
                    pstmt.close();
                }
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Failed to get catalog count for catalog '" + pCatalogOverview.getCatalogWithYear() + "': " + ex.getMessage(), ex);
            //Catalog does not exist, SQLite DB file was deleted?!
            //Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    public static synchronized boolean createCatalogOverviewDb() {
        try (Statement stmt = getCatalogDb(null, true).createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS CATALOG_OVERVIEW "
                    + "(ID                 INTEGER    PRIMARY KEY   AUTOINCREMENT, "
                    + " CREATION_DATE      TIMESTAMP  DEFAULT CURRENT_TIMESTAMP, "
                    + " CATALOG            VARCHAR(50), "
                    + " COUNTRY_EN         VARCHAR(25), "
                    + " CYEAR              INT, "
                    + " CNT                INT, "
                    + " MIN_ID             INT, "
                    + " MAX_ID             INT, "
                    + " CDATE              DATETIME ) ";
            stmt.executeUpdate(sql);
            stmt.close();
            return true;
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Failed to create catalog overview db", ex);
            return false;
        }
    }

    public static synchronized boolean dropCatalogOverview(final CpxCatalogOverview pCatalogOverview) {
        final boolean autoCreateConnection = false;
        Connection conn = getCatalogDb(null, autoCreateConnection);
        try {
            conn.setAutoCommit(false);
            dropCatalog(pCatalogOverview);
            /*
            switch(pCatalogOverview.getCatalog()) {
              case "ICD_CATALOG":
                dropIcdCatalog(pCatalogOverview.getCountryEn(), pCatalogOverview.getYear());
                break;
              case "OPS_CATALOG":
                dropOpsCatalog(pCatalogOverview.getCountryEn(), pCatalogOverview.getYear());
                break;
              default:
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Catalog '" + pCatalogOverview.getCatalog() + "' is not valid!");
                return false;
            }
             */
            String sql = "DELETE FROM CATALOG_OVERVIEW WHERE CATALOG = ? AND COUNTRY_EN = ? AND CYEAR = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, pCatalogOverview.getCatalog());
                pstmt.setString(2, pCatalogOverview.getCountryEn());
                pstmt.setInt(3, pCatalogOverview.getYear());
                pstmt.execute();
            }
            conn.commit();
        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException ex1) {
                LOG.log(Level.SEVERE, "Failed to drop catalog overview for catalog '" + pCatalogOverview.getCatalogWithYear() + "'", ex1);
            }
            LOG.log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public static synchronized boolean dropCatalog(final CpxCatalogOverview pCatalogOverview) throws SQLException {
        final String tableName = getTableName(pCatalogOverview);
        String sql = String.format("DROP TABLE IF EXISTS %s", tableName);
        try (PreparedStatement pstmt = getCatalogDb(pCatalogOverview).prepareStatement(sql)) {
            pstmt.execute();
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Failed to drop table '" + tableName + "' and catalog '" + pCatalogOverview.getCatalogWithYear() + "': " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public static synchronized boolean insertCatalogOverview(final String pCatalog, final String pCountryEn, final int pYear,
            final int pCount, final long pMinId, final long pMaxId, final Date pDate) {
        Connection conn = getCatalogDb(null);
        boolean ret = false;
        try {
            conn.setAutoCommit(false);
            String sql = "INSERT INTO CATALOG_OVERVIEW (CATALOG, COUNTRY_EN, CYEAR, CNT, MIN_ID, MAX_ID, CDATE) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, pCatalog);
                pstmt.setString(2, pCountryEn);
                pstmt.setInt(3, pYear);
                pstmt.setInt(4, pCount);
                pstmt.setLong(5, pMinId);
                pstmt.setLong(6, pMaxId);
                if (pDate == null) {
                    pstmt.setDate(7, null);
                } else {
                    pstmt.setDate(7, new java.sql.Date(pDate.getTime()));
                }
                pstmt.execute();
                ret = true;
            }
            conn.commit();
        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException ex1) {
                LOG.log(Level.SEVERE, "Failed to insert catalog overview for catalog '" + pCatalog + "/" + pYear + "'", ex1);
            }
            //Logger.getLogger(CpxLanguage.class.getName()).log(Level.SEVERE, null, ex);
            throw new UndeclaredThrowableException(ex, "Was not able to insert catalog overview");
        }
        return ret;
    }

    public static List<CpxCatalogOverview> getCatalogOverviews() {
        return getCatalogOverviews(null, null, null);
    }

    public static synchronized List<CpxCatalogOverview> getCatalogOverviews(final String pCountryEn, final String pCatalog, final Integer pYear) {
        List<CpxCatalogOverview> resultList = new ArrayList<>();
        final boolean autoCreateConnection = true;
        final String catalog = StringUtils.trimToEmpty(pCatalog);
        final String country = StringUtils.trimToEmpty(pCountryEn);
        Connection conn = getCatalogDb(null, autoCreateConnection);
        //try(Statement stmt = conn.createStatement()) {
        String sql = "SELECT ID, CATALOG, COUNTRY_EN, CYEAR, CNT, MIN_ID, MAX_ID, CDATE FROM CATALOG_OVERVIEW ";
        if (!catalog.isEmpty() || !country.isEmpty() || pYear != null) {
            StringBuilder whereSql = new StringBuilder();
            if (!country.isEmpty()) {
                whereSql.append(String.format("COUNTRY_EN = '%s' ", country));
            }
            if (!catalog.isEmpty()) {
                if (whereSql.length() > 0) {
                    whereSql.append(" AND ");
                }
                whereSql.append(String.format("CATALOG = '%s' ", catalog));
            }
            if (pYear != 0) {
                if (whereSql.length() > 0) {
                    whereSql.append(" AND ");
                }
                whereSql.append(String.format("CYEAR = %s ", String.valueOf(pYear)));
            }
            sql += " WHERE " + whereSql.toString();
        }
        sql += "ORDER BY CATALOG, COUNTRY_EN, CYEAR ";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    CpxCatalogOverview entry = new CpxCatalogOverview(
                            rs.getInt("ID"),
                            rs.getString("CATALOG"),
                            rs.getString("COUNTRY_EN"),
                            rs.getInt("CYEAR"),
                            rs.getInt("CNT"),
                            rs.getLong("MIN_ID"),
                            rs.getLong("MAX_ID"),
                            rs.getTimestamp("CDATE")
                    );
                    int count = getCatalogCount(entry);
                    entry.setCount(count);
                    resultList.add(entry);
                }
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Failed to get catalog overview", ex);
        }
        return resultList;
    }

    public static synchronized boolean insertCatalogData(CpxCatalogOverview pCatalogOverview, List<? extends AbstractCatalogEntity> list) {
        if (pCatalogOverview == null) {
            return false;
        }
        final boolean autoCreateConnection = true;
        Connection conn = getCatalogDb(pCatalogOverview, autoCreateConnection);
        boolean success = false;
        try {
            conn.setAutoCommit(false);
            if (!dropCatalogOverview(pCatalogOverview)) {
                //throw new SQLException("Was not able to drop catalog");
                throw new UndeclaredThrowableException(null, "Was not able to drop catalog " + pCatalogOverview.getCatalogWithYear() );
            }
            AbstractCpxCatalog<? extends AbstractCatalogEntity, ? extends AbstractCatalogEntity> cpxCatalog = factory(pCatalogOverview.getCatalogType());
            if (cpxCatalog == null) {
                LOG.log(Level.SEVERE, "cpxCatalog is null!");
                return false;
            }
            cpxCatalog.createCatalogTable(pCatalogOverview);
            cpxCatalog.fillCatalogWithList(pCatalogOverview, list);
            //dropCatalog(pCatalogOverview); //Drop catalog if it was cancelled before! With this trick we can avoid incomplete downloads
            /*
            switch(pCatalogOverview.getCatalog()) {
              case "ICD_CATALOG":
                //dropIcdCatalog(pCatalogOverview.getCountryEn(), pCatalogOverview.getYear());
                CpxIcdCatalog.createIcdCatalogTable(pCatalogOverview);
                CpxIcdCatalog.fillIcdCatalog(pCatalogOverview, (List<CIcdCatalog>) list);
                break;
              case "OPS_CATALOG":
                //dropOpsCatalog(pCatalogOverview.getCountryEn(), pCatalogOverview.getYear());
                CpxOpsCatalog.createOpsCatalogTable(pCatalogOverview);
                CpxOpsCatalog.fillOpsCatalog(pCatalogOverview, (List<COpsCatalog>) list);
                break;
              case "HOSPITAL":
                //dropOpsCatalog(pCatalogOverview.getCountryEn(), pCatalogOverview.getYear());
                CpxHospitalCatalog.createHospitalCatalogTable(pCatalogOverview);
                CpxHospitalCatalog.fillHospitalCatalog(pCatalogOverview, (List<CHospital>) list);
                break;
              case "INSURANCE_COMPANY":
                //dropOpsCatalog(pCatalogOverview.getCountryEn(), pCatalogOverview.getYear());
                CpxInsuranceCompanyCatalog.createInsuranceCompanyCatalogTable(pCatalogOverview);
                CpxInsuranceCompanyCatalog.fillInsuranceCompanyCatalog(pCatalogOverview, (List<CInsuranceCompany>) list);
                break;
              default:
                throw new CpxIllegalArgumentException("Catalog '" + pCatalogOverview.getCatalog() + "' is not valid!");
                //Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Catalog '" + pCatalogOverview.getCatalog() + "' is not valid!");
                //return false;
            }
             */
            insertCatalogOverview(pCatalogOverview.getCatalog(),
                    pCatalogOverview.getCountryEn(),
                    pCatalogOverview.getYear(),
                    pCatalogOverview.getNewCount(),
                    pCatalogOverview.getNewMinId(),
                    pCatalogOverview.getNewMaxId(),
                    pCatalogOverview.getNewDate());
            conn.commit();
            success = true;
// TODO: die DRGM - Datei überschreiben            
        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException ex1) {
                LOG.log(Level.SEVERE, "Failed to insert catalog data for catalog '" + pCatalogOverview.getCatalogWithYear() + "'", ex1);
            }
            LOG.log(Level.SEVERE, null, ex);
        }
        if (success) {
            try {
                vakuumDb(conn);
            } catch (SQLException ex1) {
                LOG.log(Level.SEVERE, "Shrinking (vacuum) of database failed for catalog '" + pCatalogOverview.getCatalogWithYear() + "'", ex1);
            }
        }
        return success;
    }

    public abstract String createCatalogTable(final CpxCatalogOverview pCatalogOverview);

    @SuppressWarnings("unchecked")
    protected void fillCatalogWithList(final CpxCatalogOverview pCatalogOverview, final List<? extends AbstractCatalogEntity> pList) throws SQLException {
        fillCatalog(pCatalogOverview, (List<EntityType>) pList);
    }

    public abstract void fillCatalog(final CpxCatalogOverview pCatalogOverview, final List<EntityType> pList) throws SQLException;

    public abstract CpxType toCpxObject(final ResultSet rs) throws SQLException;

//    public abstract CpxType getByCode(final String pKey, final String pCountryEn, final Integer pYear);
    /**
     * Returns Dummy-Object if entry cannot be found in local SQLite-DB
     *
     * @param pKey key
     * @param pCountryEn Country ("de", "en")
     * @param pYear catalog year
     * @return object
     */
    public CpxType getByCode(final String pKey, final String pCountryEn, final Integer pYear) {
        List<CpxType> list = findManyByCode(pKey, pCountryEn, pYear);
        if (list.isEmpty()) {
            return getNewObject();
        }
        return list.iterator().next();
    }

    public Map<Long, CpxType> getAll(final String pCountryEn, final Integer pYear) {
        String sql = "SELECT * FROM %s";
        return toMap(fetchMany(sql, pCountryEn, pYear, (pstmt) -> {
            //
        }));
    }

    public Map<Long, CpxType> toMap(List<CpxType> pList) {
        final Map<Long, CpxType> result = new LinkedHashMap<>();
        if (pList == null || pList.isEmpty()) {
            return result;
        }
        Iterator<CpxType> it = pList.iterator();
        while (it.hasNext()) {
            CpxType item = it.next();
            if (item == null) {
                continue;
            }
            result.put(item.id, item);
        }
        return result;
    }

    //public abstract CpxType getById(final Long pId, final String pCountryEn, final Integer pYear);
    /**
     * Returns Dummy-Object if ICD Code cannot be found in local SQLite-DB
     *
     * @param pId ID
     * @param pCountryEn Country ("de", "en")
     * @param pYear Year
     * @return ICD
     */
    public CpxType getById(final Long pId, final String pCountryEn, final Integer pYear) {
        String sql = "SELECT * FROM %s WHERE ID = ?";
        return fetchSingle(sql, pCountryEn, pYear, (pstmt) -> {
            pstmt.setLong(1, pId);
        });
//        int year = checkYear(pYear);
//        CpxType obj = getNewObject();
//        long id = (pId == null) ? 0L : pId;
//        CatalogTypeEn catalog = getCatalogType();
//        final String tableName = getTableName(catalog, pCountryEn, year);
//        String sql = String.format("SELECT * FROM %s WHERE ID = ?", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(catalog, year).prepareStatement(sql)) {
//            pstmt.setLong(1, id);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    obj = toCpxObject(rs);
//                    break;
//                }
//                rs.close();
//            } finally {
//                if (pstmt != null) {
//                    pstmt.close();
//                }
//            }
//        } catch (SQLException ex) {
//            //Catalog does not exist, SQLite DB file was deleted?!
//            LOG.log(Level.SEVERE, null, ex);
//        }
//        return obj;
    }

    public abstract List<CpxType> findManyByCode(final String pKey, final String pCountryEn, final Integer pYear);

    public CpxType getByCode(final String pKey, final Integer pYear) {
        return getByCode(pKey, DEFAULT_COUNTRY, pYear);
    }

    public CpxType getById(final Long pId, final Integer pYear) {
        return getById(pId, DEFAULT_COUNTRY, pYear);
    }

    public List<CpxType> findManyByCode(final String pKey, final Integer pYear) {
        return findManyByCode(pKey, DEFAULT_COUNTRY, pYear);
    }

    public abstract CatalogTypeEn getCatalogType();

    public abstract CpxType getNewObject();

    public static String operator(final String pCode) {
        if (pCode == null) {
            return "=";
        }
        if (pCode.contains("%") || pCode.contains("_")) {
            return "LIKE";
        }
        return "=";
    }

    public CpxType getSingleObject(Collection<CpxType> pCollection) {
        if (pCollection == null || pCollection.isEmpty()) {
            return getNewObject();
        } else {
            return pCollection.iterator().next();
        }
    }

    public List<CpxType> fetchMany(final String pSql, final String pCountryEn, final ParamCallback pParamCallback) {
        return fetch(pSql, pCountryEn, 0, pParamCallback, 0);
    }

    public CpxType fetchSingle(final String pSql, final String pCountryEn, final ParamCallback pParamCallback) {
        return getSingleObject(fetch(pSql, pCountryEn, 0, pParamCallback, 1));
    }

//    public List<CpxType> fetchMany(final String pSql, final CatalogTypeEn pCatalogTypeEn, final String pCountryEn, final ParamCallback pCallback) {
//        return fetch(pSql, pCatalogTypeEn, pCountryEn, 0, pCallback, 0);
//    }
//
//    public CpxType fetchSingle(final String pSql, final CatalogTypeEn pCatalogTypeEn, final String pCountryEn, final ParamCallback pCallback) {
//        return getSingleObject(fetch(pSql, pCatalogTypeEn, pCountryEn, 0, pCallback, 1));
//    }
//
//    public List<CpxType> fetchMany(final String pSql, final CatalogTypeEn pCatalogTypeEn, final String pCountryEn, final Integer pYear, final ParamCallback pCallback) {
//        return fetch(pSql, pCatalogTypeEn, pCountryEn, pYear, pCallback, 0);
//    }
//
//    public CpxType fetchSingle(final String pSql, final CatalogTypeEn pCatalogTypeEn, final String pCountryEn, final Integer pYear, final ParamCallback pCallback) {
//        return getSingleObject(fetch(pSql, pCatalogTypeEn, pCountryEn, pYear, pCallback, 1));
//    }
    public List<CpxType> fetchMany(final String pSql, final String pCountryEn, final Integer pYear, final ParamCallback pParamCallback) {
        return fetch(pSql, pCountryEn, pYear, pParamCallback, 0);
    }

    public CpxType fetchSingle(final String pSql, final String pCountryEn, final Integer pYear, final ParamCallback pParamCallback) {
        return getSingleObject(fetch(pSql, pCountryEn, pYear, pParamCallback, 1));
    }

    public List<CpxType> fetch(final String pSql, final String pCountryEn, final ParamCallback pParamCallback, final int pLimit) {
        return fetch(pSql, pCountryEn, 0, pParamCallback, pLimit);
    }

//    public List<CpxType> fetch(final String pSql, final CatalogTypeEn pCatalogTypeEn, final String pCountryEn, final ParamCallback pCallback, final int pLimit) {
//        return fetch(pSql, pCatalogTypeEn, pCountryEn, 0, pCallback, pLimit);
//    }
    public List<CpxType> fetch(final String pSql, final String pCountryEn, final Integer pYear, final ParamCallback pCallback, final int pLimit) {
        List<CpxType> resultList = new ArrayList<>();
        if (pSql == null || pSql.isEmpty()) {
            return resultList;
        }
        final int year = checkYear2(pYear);
        //final String tableName = getTableName(getCatalogType(), pCountryEn, year);
        final String tableName = getTableName2(pCountryEn, year);
        String sql = String.format(pSql, tableName);
        if (pLimit > 0) {
            sql += " LIMIT " + pLimit;
        }
//        final long startTime = System.currentTimeMillis();
//        int year = checkYear(pYear);
//        List<CpxIcd> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam(pCode);
//        String sql = String.format("SELECT * FROM %s WHERE ICD_CODE " + operator(code) + " ? ORDER BY ICD_CODE", tableName);
//        final CatalogTypeEn catalogType = pCatalogTypeEn == null ? getCatalogType() : pCatalogTypeEn;
        final Connection database = getCatalogDb(getCatalogType(), year);
        if (database == null) {
            LOG.log(Level.FINEST, "no database found for type {0} and year {1}", new Object[]{getCatalogType(), year});
            return resultList;
        }
        try (PreparedStatement pstmt = database.prepareStatement(sql)) {
            if (pCallback != null) {
                pCallback.call(pstmt);
            }
            //pstmt.setString(1, code);
            try (ResultSet rs = pstmt.executeQuery()) {
//                int i = 0;
                while (rs.next()) {
                    CpxType obj = toCpxObject(rs);
                    if (obj != null) {
                        resultList.add(obj);
//                        i++;
//                        if (pLimit > 0 && i >= pLimit) {
//                            break;
//                        }
                    }
                }
                rs.close();
            } finally {
                if (pstmt != null) {
                    pstmt.close();
                }
            }
        } catch (SQLException ex) {
            //Catalog does not exist, SQLite DB file was deleted?!
            LOG.log(Level.SEVERE, null, ex);
        }

        //LOG.log(Level.FINEST, "Search for ICD " + code + " took " + (System.currentTimeMillis() - startTime) + " ms");
        return resultList;
    }

    public <T> List<T> fetchManyValues(final String pSql, final String pCountryEn, final ParamCallback pParamCallback) {
        return fetchValues(pSql, pCountryEn, 0, pParamCallback, null, 0);
    }

    public <T> List<T> fetchManyValues(final String pSql, final String pCountryEn, final Integer pYear, final ParamCallback pParamCallback) {
        return fetchValues(pSql, pCountryEn, pYear, pParamCallback, null, 0);
    }

    public <T> T fetchSingleValue(final String pSql, final String pCountryEn, final ParamCallback pParamCallback) {
        return fetchSingleValue(pSql, pCountryEn, 0, pParamCallback, null);
    }

    public <T> T fetchSingleValue(final String pSql, final String pCountryEn, final Integer pYear, final ParamCallback pParamCallback) {
        final List<T> resultList = fetchValues(pSql, pCountryEn, pYear, pParamCallback, null, 1);
        if (resultList.isEmpty()) {
            return null;
        } else {
            return resultList.iterator().next();
        }
    }

    public <T> List<T> fetchManyValues(final String pSql, final String pCountryEn, final ParamCallback pParamCallback, final ResultCallback<T> pResultCallback) {
        return fetchValues(pSql, pCountryEn, 0, pParamCallback, pResultCallback, 0);
    }

    public <T> List<T> fetchManyValues(final String pSql, final String pCountryEn, final Integer pYear, final ParamCallback pParamCallback, final ResultCallback<T> pResultCallback) {
        return fetchValues(pSql, pCountryEn, pYear, pParamCallback, pResultCallback, 0);
    }

    public <T> T fetchSingleValue(final String pSql, final String pCountryEn, final ParamCallback pParamCallback, final ResultCallback<T> pResultCallback) {
        return fetchSingleValue(pSql, pCountryEn, 0, pParamCallback, pResultCallback);
    }

    public <T> T fetchSingleValue(final String pSql, final String pCountryEn, final Integer pYear, final ParamCallback pParamCallback, final ResultCallback<T> pResultCallback) {
        final List<T> resultList = fetchValues(pSql, pCountryEn, pYear, pParamCallback, pResultCallback, 1);
        if (resultList.isEmpty()) {
            return null;
        } else {
            return resultList.iterator().next();
        }
    }

    public <T> List<T> fetchValues(final String pSql, final String pCountryEn, final ParamCallback pParamCallback, final ResultCallback<T> pResultCallback, final int pLimit) {
        return fetchValues(pSql, pCountryEn, 0, pParamCallback, null, pLimit);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> fetchValues(final String pSql, final String pCountryEn, final Integer pYear, final ParamCallback pParamCallback, final ResultCallback<T> pResultCallback, final int pLimit) {
        List<T> resultList = new ArrayList<>();
        if (pSql == null || pSql.isEmpty()) {
            return resultList;
        }
        final int year = checkYear2(pYear);
//        final String tableName = getTableName(getCatalogType(), pCountryEn, year);
        final String tableName = getTableName2(pCountryEn, year);
        String sql = String.format(pSql, tableName);
        if (pLimit > 0) {
            sql += " LIMIT " + pLimit;
        }
//        final long startTime = System.currentTimeMillis();
//        int year = checkYear(pYear);
//        List<CpxIcd> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam(pCode);
//        String sql = String.format("SELECT * FROM %s WHERE ICD_CODE " + operator(code) + " ? ORDER BY ICD_CODE", tableName);
//        final CatalogTypeEn catalogType = pCatalogTypeEn == null ? getCatalogType() : pCatalogTypeEn;
        final Connection database = getCatalogDb(getCatalogType(), year);
        if (database == null) {
            LOG.log(Level.FINEST, "no database found for type {0} and year {1}", new Object[]{getCatalogType(), year});
            return resultList;
        }
        try (PreparedStatement pstmt = database.prepareStatement(sql)) {
            if (pParamCallback != null) {
                pParamCallback.call(pstmt);
            }
            //pstmt.setString(1, code);
            try (ResultSet rs = pstmt.executeQuery()) {
//                int i = 0;
                while (rs.next()) {
                    final T obj;
                    if (pResultCallback != null) {
                        obj = pResultCallback.call(rs);
                    } else {
                        obj = (T) rs.getObject(1);
                    }
                    if (obj != null) {
                        resultList.add(obj);
//                        i++;
//                        if (pLimit > 0 && i >= pLimit) {
//                            break;
//                        }
                    }
                }
                rs.close();
            } finally {
                if (pstmt != null) {
                    pstmt.close();
                }
            }
        } catch (SQLException ex) {
            //Catalog does not exist, SQLite DB file was deleted?!
            LOG.log(Level.SEVERE, null, ex);
        }

        //LOG.log(Level.FINEST, "Search for ICD " + code + " took " + (System.currentTimeMillis() - startTime) + " ms");
        return resultList;
    }

    public boolean hasEntry(final String pSql, final String pCountryEn, final ParamCallback pParamCallback) {
        return hasEntry(pSql, pCountryEn, 0, pParamCallback);
    }

    public boolean hasEntry(final String pSql, final String pCountryEn, final Integer pYear, final ParamCallback pParamCallback) {
        if (pSql == null || pSql.isEmpty()) {
            return false;
        }
        final int year = checkYear2(pYear);
//        final String tableName = getTableName(getCatalogType(), pCountryEn, year);
        final String tableName = getTableName2(pCountryEn, year);
        String sql = String.format(pSql, tableName);
        sql += " LIMIT 1";
//        final long startTime = System.currentTimeMillis();
//        int year = checkYear(pYear);
//        List<CpxIcd> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam(pCode);
//        String sql = String.format("SELECT * FROM %s WHERE ICD_CODE " + operator(code) + " ? ORDER BY ICD_CODE", tableName);
//        final CatalogTypeEn catalogType = pCatalogTypeEn == null ? getCatalogType() : pCatalogTypeEn;
        final Connection database = getCatalogDb(getCatalogType(), year);
        if (database == null) {
            LOG.log(Level.FINEST, "no database found for type {0} and year {1}", new Object[]{getCatalogType(), year});
            return false;
        }
        try (PreparedStatement pstmt = database.prepareStatement(sql)) {
            if (pParamCallback != null) {
                pParamCallback.call(pstmt);
            }
            //pstmt.setString(1, code);
            try (ResultSet rs = pstmt.executeQuery()) {
//                int i = 0;
                while (rs.next()) {
                    Object obj = rs.getObject(1);
                    if (obj != null) {
                        return true;
//                        i++;
//                        if (pLimit > 0 && i >= pLimit) {
//                            break;
//                        }
                    }
                }
                rs.close();
            } finally {
                if (pstmt != null) {
                    pstmt.close();
                }
            }
        } catch (SQLException ex) {
            //Catalog does not exist, SQLite DB file was deleted?!
            LOG.log(Level.SEVERE, null, ex);
        }

        //LOG.log(Level.FINEST, "Search for ICD " + code + " took " + (System.currentTimeMillis() - startTime) + " ms");
        return false;
    }

    public int checkYear2(final Integer pYear) {
        final int year;
        if ((pYear == null || pYear.equals(0)) && !isAnnualCatalog()) {
            year = 0;
        } else {
            //auto detect year (use current year)
            year = checkYear(pYear);
        }
        return year;
    }

    public String getTableName2(final String pCountryEn, final int pYear) {
        //final int year = checkYear2(pYear);
        final String tableName = getTableName(getCatalogType(), pCountryEn, pYear);
        return tableName;
    }

    public boolean isAnnualCatalog() {
        return true;
    }

}
