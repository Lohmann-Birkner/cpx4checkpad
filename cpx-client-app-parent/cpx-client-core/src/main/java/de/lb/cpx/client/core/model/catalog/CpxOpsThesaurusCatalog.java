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

import static de.lb.cpx.client.core.connection.database.CpxDbManager.QStr;
import static de.lb.cpx.client.core.connection.database.CpxDbManager.toParam;
import static de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog.getCatalogDb;
import static de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog.getTableName;
import de.lb.cpx.server.commonDB.model.COpsThesaurus;
import de.lb.cpx.service.information.CatalogTypeEn;
import de.lb.cpx.service.information.CpxCatalogOverview;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Dirk Niemeier
 */
public class CpxOpsThesaurusCatalog extends AbstractCpxCatalog<CpxOpsThesaurus, COpsThesaurus> {

    private static CpxOpsThesaurusCatalog instance = null;
    public static final CatalogTypeEn CATALOG = CatalogTypeEn.OPS_THESAURUS;
    private static final Logger LOG = Logger.getLogger(CpxOpsThesaurusCatalog.class.getName());

//    private static final boolean IS_OPS_DB_ATTACHED = false;
    public static synchronized CpxOpsThesaurusCatalog instance() {
        if (instance == null) {
            instance = new CpxOpsThesaurusCatalog();
        }
        return instance;
    }
    //Logger log = Logger.getLogger(getClass().getSimpleName());

    private CpxOpsThesaurusCatalog() {

    }

    @Override
    public void fillCatalog(final CpxCatalogOverview pCatalogOverview, final List<COpsThesaurus> pList) throws SQLException {
        final String tableName = getTableName(pCatalogOverview);
        String sqlHead = String.format("INSERT INTO %s(ID, CREATION_DATE, MODIFICATION_DATE, OPS_YEAR, COUNTRY_EN, DESCRIPTION, DIMDI_INTERNAL_NO, KEY_NO_1, KEY_NO_2, REFERENCE, TYPE_OF_CODE) ", tableName);
        StringBuilder sql = null;
        int i = 0;
        boolean first = true;
        for (COpsThesaurus opsThesaurus : pList) {
            i++;
            if (first) {
                sql = new StringBuilder(sqlHead);
                sql.append(" SELECT ");
                first = false;
            } else {
                sql.append(" UNION ALL SELECT ");
            }
            sql.append(opsThesaurus.getId() + ", ");
            sql.append(QStr(opsThesaurus.getCreationDate()) + ", ");
            sql.append(QStr(opsThesaurus.getModificationDate()) + ", ");
            sql.append(QStr(opsThesaurus.getOpsYear()) + ", ");
            sql.append(QStr(opsThesaurus.getCountryEn().name()) + ", ");
            sql.append(QStr(opsThesaurus.getDescription()) + ", ");
            sql.append(QStr(opsThesaurus.getDimdiInternalNo()) + ", ");
            sql.append(QStr(opsThesaurus.getKeyNo1()) + ", ");
            sql.append(QStr(opsThesaurus.getKeyNo2()) + ", ");
            sql.append(QStr(opsThesaurus.getReference()) + ", ");
            sql.append(QStr(opsThesaurus.getTypeOfCode()));
            //pstmt.addBatch();
            //break;
            if (i % 500 == 0 || i == pList.size()) {
                try ( PreparedStatement pstmt = getCatalogDb(pCatalogOverview).prepareStatement(sql.toString())) {
                    pstmt.execute();
                }
                first = true;
            }
        }
        //getCatalogDb(pCatalogOverview.getYear()).commit();
        //System.out.println(sql.toString());
    }

    @Override
    public String createCatalogTable(final CpxCatalogOverview pCatalogOverview) {
        final String tableName = getTableName(pCatalogOverview);
        try ( Statement stmt = getCatalogDb(pCatalogOverview).createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS " + tableName
                    + "(ID                 INTEGER    PRIMARY KEY,"
                    + " CREATION_DATE      DATETIME, "
                    + " MODIFICATION_DATE  DATETIME, "
                    + " OPS_YEAR           INT, "
                    + " COUNTRY_EN         VARCHAR(25), "
                    + " DESCRIPTION        VARCHAR(255), "
                    + " DIMDI_INTERNAL_NO  INTEGER, "
                    + " KEY_NO_1           VARCHAR(10), "
                    + " KEY_NO_2           VARCHAR(10), "
                    + " REFERENCE          VARCHAR(255), "
                    + " TYPE_OF_CODE       INTEGER ) ";
            stmt.executeUpdate(sql);

            sql = String.format("CREATE INDEX IF NOT EXISTS IDX_ICD_THESAURUS_KEY_NO_1 ON %s (KEY_NO_1)", tableName); //COUNTRY_EN
            stmt.executeUpdate(sql);
            sql = String.format("CREATE INDEX IF NOT EXISTS IDX_ICD_THESAURUS_KEY_NO_2 ON %s (KEY_NO_2)", tableName); //COUNTRY_EN
            stmt.executeUpdate(sql);

            stmt.close();
            return tableName;
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return "";
        }
    }

    @Override
    public CpxOpsThesaurus toCpxObject(final ResultSet rs) throws SQLException {
        CpxOpsThesaurus obj = getNewObject();
        if (rs == null) {
            return obj;
        }
        obj.setId(rs.getLong("ID"));
        //obj.setCreationDate(CpxDateParser.parseDateTime(rs.getString("CREATION_DATE")));
        //obj.setModificationDate(CpxDateParser.parseDateTime(rs.getString("MODIFICATION_DATE")));
        //obj.setCountryEn(CountryEn.valueOf(rs.getString("COUNTRY_EN")));
        obj.setDescription(rs.getString("DESCRIPTION"));
        obj.setDimdiInternalNo(rs.getInt("DIMDI_INTERNAL_NO"));
        obj.setKeyNo1(rs.getString("KEY_NO_1"));
        obj.setKeyNo2(rs.getString("KEY_NO_2"));
        obj.setReference(rs.getString("REFERENCE"));
        obj.setTypeOfCode(rs.getInt("TYPE_OF_CODE"));
        return obj;
    }

    /**
     * Returns Dummy-Object if OPS Thesaurus cannot be found in local SQLite-DB
     *
     * @param pKeyNo1 Code
     * @param pCountryEn Country ("de", "en")
     * @param pYear year
     * @return Doctor
     */
    public List<CpxOpsThesaurus> getByKeyNo1(final String pKeyNo1, final String pCountryEn, final Integer pYear) {
        final String code = toParam(pKeyNo1);
        String sql = "SELECT * FROM %s WHERE KEY_NO_1 " + operator(code) + " ?";
        return fetchMany(sql, pCountryEn, pYear, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        List<CpxOpsThesaurus> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, pYear);
//        final String code = toParam(pKeyNo1);
//        String sql = String.format("SELECT * FROM %s WHERE KEY_NO_1 " + operator(code) + " ?", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, pYear).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    CpxOpsThesaurus obj = toCpxObject(rs);
//                    if (obj != null) {
//                        objList.add(obj);
//                    }
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
//
//        return objList;
    }

    /**
     * Returns Dummy-Object if OPS Thesaurus cannot be found in local SQLite-DB
     *
     * @param pKeyNo2 Code
     * @param pCountryEn Country ("de", "en")
     * @param pYear year
     * @return Doctor
     */
    public List<CpxOpsThesaurus> getByKeyNo2(final String pKeyNo2, final String pCountryEn, final Integer pYear) {
        final String code = toParam(pKeyNo2);
        String sql = "SELECT * FROM %s WHERE KEY_NO_2 " + operator(code) + " ?";
        return fetchMany(sql, pCountryEn, pYear, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        List<CpxOpsThesaurus> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, pYear);
//        final String code = toParam(pKeyNo2);
//        String sql = String.format("SELECT * FROM %s WHERE KEY_NO_2 " + operator(code) + " ?", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, pYear).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    CpxOpsThesaurus obj = toCpxObject(rs);
//                    if (obj != null) {
//                        objList.add(obj);
//                    }
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
//
//        return objList;
    }

//    /**
//     * Returns Dummy-Object if OPS Thesaurus cannot be found in local SQLite-DB
//     *
//     * @param pId ID
//     * @param pCountryEn Country ("de", "en")
//     * @param pYear year
//     * @return OPS Thesaurus
//     */
//    @Override
//    public CpxOpsThesaurus getById(final Long pId, final String pCountryEn, final Integer pYear) {
//        CpxOpsThesaurus obj = getNewObject();
//        long id = (pId == null) ? 0L : pId;
//        final String tableName = getTableName(CATALOG, pCountryEn, pYear);
//        String sql = String.format("SELECT * FROM %s WHERE ID = ?", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, pYear).prepareStatement(sql)) {
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
//    }
    /**
     * Returns map of all OPS Thesaurus in SQLite-DB
     *
     * @param pCountryEn Country ("de", "en")
     * @param pYear year
     * @return List of all OPS Thesaurus
     */
    @Override
    public Map<Long, CpxOpsThesaurus> getAll(final String pCountryEn, final Integer pYear) {
        return getAll(pCountryEn, pYear, null);
    }

    /**
     * Returns map of all OPS Thesaurus in SQLite-DB
     *
     * @param pCountryEn Country ("de", "en")
     * @param pYear year
     * @param pTypeOfCode type of code
     * @return List of all OPS Thesaurus
     */
    public Map<Long, CpxOpsThesaurus> getAll(final String pCountryEn, final Integer pYear, final int[] pTypeOfCode) {
        String sql = "SELECT * FROM %s";
        if (pTypeOfCode != null && pTypeOfCode.length > 0) {
            sql += String.format(" WHERE TYPE_OF_CODE IN (%s)", StringUtils.join(ArrayUtils.toObject(pTypeOfCode), ","));
        }
        return toMap(fetchMany(sql, pCountryEn, pYear, (pstmt) -> {
            //
        }));

//        Map<Long, CpxOpsThesaurus> result = new HashMap<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, pYear);
//        String sql = String.format("SELECT * FROM %s", tableName);
//        if (pTypeOfCode != null && pTypeOfCode.length > 0) {
//            sql += String.format(" WHERE TYPE_OF_CODE IN (%s)", StringUtils.join(ArrayUtils.toObject(pTypeOfCode), ","));
//        }
//        //        try (PreparedStatement pstmt = getCatalogDb(CATALOG, pYear).prepareStatement(sql)) {
//        try ( PreparedStatement pstmt = prepareStatement(CATALOG, pYear, sql)) {
//            if (pstmt != null) { //prepared statement is null if catalog does not exist!
//                try ( ResultSet rs = pstmt.executeQuery()) {
//                    while (rs.next()) {
//                        CpxOpsThesaurus obj = toCpxObject(rs);
//                        result.put(obj.id, obj);
//                    }
//                    rs.close();
//                } finally {
//                    if (pstmt != null) {
//                        pstmt.close();
//                    }
//                }
//            }
//        } catch (SQLException ex) {
//            //Catalog does not exist, SQLite DB file was deleted?!
//            Logger.getLogger(CpxIcdThesaurusCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return result;
    }

//    /**
//     * Returns Dummy-Object if OPS Thesaurus Code cannot be found in local
//     * SQLite-DB
//     *
//     * @param pCode ICD Code
//     * @param pCountryEn Country ("de", "en")
//     * @param pYear year
//     * @return ICD
//     */
//    @Override
//    public CpxOpsThesaurus getByCode(final String pCode, final String pCountryEn, final Integer pYear) {
//        List<CpxOpsThesaurus> list = findManyByCode(pCode, pCountryEn, pYear);
//        if (list.isEmpty()) {
//            return getNewObject();
//        }
//        return list.get(0);
//    }
    /**
     * Returns Dummy-Object if OPS Thesaurus cannot be found in local SQLite-DB
     *
     * @param pCode Identifier
     * @param pCountryEn Country ("de", "en")
     * @param pYear year
     * @return ICD Thesaurus
     */
    @Override
    public List<CpxOpsThesaurus> findManyByCode(final String pCode, final String pCountryEn, final Integer pYear) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public CatalogTypeEn getCatalogType() {
        return CATALOG;
    }

//    /**
//     * Genrate the Catalog File Name
//     *
//     * @param pCatalogType Catalog Type
//     * @param year Catalog year
//     * @return the Catalog File Name
//     */
//    public String getOpsCatalogDBName(CatalogTypeEn pCatalogType, final Integer year) {
//        String catalog = (pCatalogType == null) ? "" : pCatalogType.name();
//        String name = CpxDbManager.DATABASE.cpx_catalog.name();
//        if (year > 0 || !catalog.isEmpty()) {
//            if (year > 0) {
//                name += "_" + catalog + "_" + year;
//            } else {
//                name += "_" + catalog;
//            }
//        }
//        return name;
//    }
    /**
     * Method to match the Thesaurus with the ICD keys Internationale
     * statistische Klassifikation der Krankheiten und verwandter
     * Gesundheitsprobleme, 10. Revision Stand der Klassifikation: 04.10.2017
     *
     * @param pCountryEn Country ("de", "en")
     * @param pYear Year
     * @param pOpsList ops list
     */
    public void getAllThesaurus(final String pCountryEn, final Integer pYear, Map<Long, CpxOps> pOpsList) {
        long timeStart = System.currentTimeMillis();
        final Map<String, Long> opsCodeMap = new HashMap<>();
        for (Map.Entry<Long, CpxOps> entry : pOpsList.entrySet()) {
            opsCodeMap.put(entry.getValue().getCode(), entry.getKey());
        }
        int[] typeOfCode = new int[]{1, 2};
        final Map<Long, CpxOpsThesaurus> thesaurus = getAll(pCountryEn, pYear, typeOfCode);
        for (Map.Entry<Long, CpxOpsThesaurus> entry : thesaurus.entrySet()) {
            final CpxOpsThesaurus opsThesaurus = entry.getValue();
            if (opsThesaurus.getKeyNo1() != null) {
                Long opsIcd = opsCodeMap.get(opsThesaurus.getKeyNo1());
                if (opsIcd != null) {
                    pOpsList.get(opsIcd).addThesaurus(opsThesaurus);
                }
            }
            if (opsThesaurus.getKeyNo2() != null) {
                Long opsIcd = opsCodeMap.get(opsThesaurus.getKeyNo2().replace("#", ""));
                if (opsIcd != null) {
                    pOpsList.get(opsIcd).addThesaurus(opsThesaurus);
                }
            }
            //LOG.log(Level.FINEST, "Unknown ops in thesaurus with id " + opsThesaurus.id);
        }
        LOG.log(Level.INFO, "Time elapsed while loading OPS Thesaurus: " + (System.currentTimeMillis() - (double) timeStart) / 1000d + "s");
    }

    @Override
    public CpxOpsThesaurus getNewObject() {
        return new CpxOpsThesaurus();
    }

//    /**
//     * Method to match the Thesaurus with the ICD keys Internationale
//     * statistische Klassifikation der Krankheiten und verwandter
//     * Gesundheitsprobleme, 10. Revision Stand der Klassifikation: 04.10.2017
//     *
//     * @param pCountryEn Country ("de", "en")
//     * @param pYear Year
//     * @param opsList ops list
//     */
//    public void getAllThesaurus(final String pCountryEn, final Integer pYear, Map<Long, CpxOps> opsList) {
//        long timeStartAll = System.currentTimeMillis();
//        long timeStart = System.currentTimeMillis();
//        int year = checkYear(pYear);
//        Map<Integer, Map<Long, CpxOps>> objList = new HashMap<>();
//        final String opsTableName = getTableName(CatalogTypeEn.OPS, pCountryEn, year);
//        final String thesaurusTableName = getTableName(CATALOG, pCountryEn, year);
//
//        try {
//            if (!isOpsDBAttached) {
//                getCatalogDb(CATALOG, year).prepareStatement("ATTACH DATABASE \"" + CpxSystemProperties.getInstance().getCpxClientCatalogDir() + getOpsCatalogDBName(CatalogTypeEn.OPS, year) + ".db\" AS  OPS_DB").execute();
//                isOpsDBAttached = true;
//            }
//        } catch (SQLException ex) {
//            LOG.log(Level.SEVERE, null, ex);
//        }
//
//        //typeofCode = 1
//        String sql = String.format("select * from OPS_DB.%s as ops, %s as T where ops.OPS_Code = REPLACE(T.KEY_NO_1, '#', '') and T.TYPE_OF_CODE = 1", opsTableName, thesaurusTableName);
//        /*  + " ORDER BY ICD_DEPTH, ICD_CODE */
//        try (PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            try (ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    long l = rs.getLong("ID");
//                    CpxOpsThesaurus obj = toCpxObject(rs);
//                    if (obj != null) {
//                        opsList.get(l).addOpsThesaurusObj(obj);
//                    }
//                }
//                rs.close();
//            } finally {
//                pstmt.close();
//            }
//        } catch (SQLException ex) {
//            //Catalog does not exist, SQLite DB file was deleted?!
//            LOG.log(Level.SEVERE, null, ex);
//        }
//
//        long timeEnd = System.currentTimeMillis();
//        LOG.log(Level.INFO, "Time elapsed while loading OPS_Thesaurus typeofCode = 1: " + (timeEnd - (double) timeStart) / 1000d + "s");
//        timeStart = System.currentTimeMillis();
//
//        timeStart = System.currentTimeMillis();
//
//        //typeofCode = 2
//        sql = String.format("select * from OPS_DB.%s as ops, %s as T where (ops.OPS_Code = REPLACE(T.KEY_NO_1, '#', '') or ops.OPS_Code = REPLACE(T.KEY_NO_2, '#', '')) and T.TYPE_OF_CODE = 2", opsTableName, thesaurusTableName);
//        /*  + " ORDER BY ICD_DEPTH, ICD_CODE */
//        try (PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            try (ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    long l = rs.getLong("ID");
//                    CpxOpsThesaurus obj = toCpxObject(rs);
//                    if (obj != null) {
//                        opsList.get(l).addOpsThesaurusObj(obj);
//                    }
//                }
//                rs.close();
//            } finally {
//                pstmt.close();
//            }
//        } catch (SQLException ex) {
//            //Catalog does not exist, SQLite DB file was deleted?!
//            LOG.log(Level.SEVERE, null, ex);
//        }
//
//        timeEnd = System.currentTimeMillis();
//        LOG.log(Level.INFO, "Time elapsed while loading OPS_Thesaurus typeofCode = 2: " + (timeEnd - (double) timeStart) / 1000d + "s");
//        timeStart = System.currentTimeMillis();
//
//        long timeEndAll = System.currentTimeMillis();
//        LOG.log(Level.INFO, "Time elapsed while loading All OPS_Thesaurus catalog: " + (timeEndAll - (double) timeStartAll) / 1000d + "s");
//
//    }
}
