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
import de.lb.cpx.server.commonDB.model.CIcdThesaurus;
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
public class CpxIcdThesaurusCatalog extends AbstractCpxCatalog<CpxIcdThesaurus, CIcdThesaurus> {

    private static CpxIcdThesaurusCatalog instance = null;
    public static final CatalogTypeEn CATALOG = CatalogTypeEn.ICD_THESAURUS;
    private static final Logger LOG = Logger.getLogger(CpxIcdThesaurusCatalog.class.getName());

//    private static final boolean IS_ICD_DB_ATTACHED = false;
    public static synchronized CpxIcdThesaurusCatalog instance() {
        if (instance == null) {
            instance = new CpxIcdThesaurusCatalog();
        }
        return instance;
    }
    //Logger log = Logger.getLogger(getClass().getSimpleName());

    private CpxIcdThesaurusCatalog() {

    }

    @Override
    public void fillCatalog(final CpxCatalogOverview pCatalogOverview, final List<CIcdThesaurus> pList) throws SQLException {
        final String tableName = getTableName(pCatalogOverview);
        String sqlHead = String.format("INSERT INTO %s(ID, CREATION_DATE, MODIFICATION_DATE, ICD_YEAR, COUNTRY_EN, ADD_KEY_NO, DESCRIPTION, DIMDI_INTERNAL_NO, PRIM_KEY_NO_1, PRIM_KEY_NO_2, IS_PRINT_FL, REFERENCE, STAR_KEY_NO, TYPE_OF_CODE) ", tableName);
        StringBuilder sql = null;
        int i = 0;
        boolean first = true;
        for (CIcdThesaurus icdThesaurus : pList) {
            i++;
            if (first) {
                sql = new StringBuilder(sqlHead);
                sql.append(" SELECT ");
                first = false;
            } else {
                sql.append(" UNION ALL SELECT ");
            }
            sql.append(icdThesaurus.getId() + ", ");
            sql.append(QStr(icdThesaurus.getCreationDate()) + ", ");
            sql.append(QStr(icdThesaurus.getModificationDate()) + ", ");
            sql.append(QStr(icdThesaurus.getIcdYear()) + ", ");
            sql.append(QStr(icdThesaurus.getCountryEn().name()) + ", ");
            sql.append(QStr(icdThesaurus.getAddKeyNo()) + ", ");
            sql.append(QStr(icdThesaurus.getDescription()) + ", ");
            sql.append(QStr(icdThesaurus.getDimdiInternalNo()) + ", ");
            sql.append(QStr(icdThesaurus.getPrimKeyNo1()) + ", ");
            sql.append(QStr(icdThesaurus.getPrimKeyNo2()) + ", ");
            sql.append(QStr(icdThesaurus.isPrintFl()) + ", ");
            sql.append(QStr(icdThesaurus.getReference()) + ", ");
            sql.append(QStr(icdThesaurus.getStarKeyNo()) + ", ");
            sql.append(QStr(icdThesaurus.getTypeOfCode()));
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
                    + " ICD_YEAR           INT, "
                    + " COUNTRY_EN         VARCHAR(25), "
                    + " ADD_KEY_NO         VARCHAR(10), "
                    + " DESCRIPTION        VARCHAR(255), "
                    + " DIMDI_INTERNAL_NO  INTEGER, "
                    + " PRIM_KEY_NO_1      VARCHAR(10), "
                    + " PRIM_KEY_NO_2      VARCHAR(10), "
                    + " IS_PRINT_FL        TINYINT    NOT NULL, "
                    + " REFERENCE          VARCHAR(255), "
                    + " STAR_KEY_NO        VARCHAR(10), "
                    + " TYPE_OF_CODE       INTEGER ) ";
            stmt.executeUpdate(sql);

            sql = String.format("CREATE INDEX IF NOT EXISTS IDX_ICD_THESAURUS_PRIM_KEY_NO_1 ON %s (PRIM_KEY_NO_1)", tableName); //COUNTRY_EN
            stmt.executeUpdate(sql);
            sql = String.format("CREATE INDEX IF NOT EXISTS IDX_ICD_THESAURUS_PRIM_KEY_NO_2 ON %s (PRIM_KEY_NO_2)", tableName); //COUNTRY_EN
            stmt.executeUpdate(sql);
            sql = String.format("CREATE INDEX IF NOT EXISTS IDX_ICD_THESAURUS_ADD_KEY_NO ON %s (ADD_KEY_NO)", tableName); //COUNTRY_EN
            stmt.executeUpdate(sql);
            sql = String.format("CREATE INDEX IF NOT EXISTS IDX_ICD_THESAURUS_STAR_KEY_NO ON %s (STAR_KEY_NO)", tableName); //COUNTRY_EN
            stmt.executeUpdate(sql);

            stmt.close();
            return tableName;
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return "";
        }
    }

    @Override
    public CpxIcdThesaurus toCpxObject(final ResultSet rs) throws SQLException {
        CpxIcdThesaurus obj = getNewObject();
        if (rs == null) {
            return obj;
        }
        obj.setId(rs.getLong("ID"));
        //obj.setCreationDate(CpxDateParser.parseDateTime(rs.getString("CREATION_DATE")));
        //obj.setModificationDate(CpxDateParser.parseDateTime(rs.getString("MODIFICATION_DATE")));
        //obj.setCountryEn(CountryEn.valueOf(rs.getString("COUNTRY_EN")));
        obj.setAddKeyNo(rs.getString("ADD_KEY_NO"));
        obj.setDescription(rs.getString("DESCRIPTION"));
        obj.setDimdiInternalNo(rs.getInt("DIMDI_INTERNAL_NO"));
        obj.setPrimKeyNo1(rs.getString("PRIM_KEY_NO_1"));
        obj.setPrimKeyNo2(rs.getString("PRIM_KEY_NO_2"));
        obj.setPrintFl(rs.getBoolean("IS_PRINT_FL"));
        obj.setReference(rs.getString("REFERENCE"));
        obj.setStarKeyNo(rs.getString("STAR_KEY_NO"));
        obj.setTypeOfCode(rs.getInt("TYPE_OF_CODE"));
        return obj;
    }

    /**
     * Returns Dummy-Object if ICD Thesaurus cannot be found in local SQLite-DB
     *
     * @param pPrimKeyNo1 Code
     * @param pCountryEn Country ("de", "en")
     * @param pYear year
     * @return Doctor
     */
    public List<CpxIcdThesaurus> getByPrimKeyNo1(final String pPrimKeyNo1, final String pCountryEn, final Integer pYear) {
        final String code = toParam(pPrimKeyNo1);
        String sql = "SELECT * FROM %s WHERE PRIM_KEY_NO_1 " + operator(code) + " ?";
        return fetchMany(sql, pCountryEn, pYear, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        List<CpxIcdThesaurus> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, pYear);
//        final String code = toParam(pPrimKeyNo1);
//        String sql = String.format("SELECT * FROM %s WHERE PRIM_KEY_NO_1 " + operator(code) + " ?", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, pYear).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    CpxIcdThesaurus obj = toCpxObject(rs);
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
     * Returns Dummy-Object if ICD Thesaurus cannot be found in local SQLite-DB
     *
     * @param pPrimKeyNo2 Code
     * @param pCountryEn Country ("de", "en")
     * @param pYear year
     * @return Doctor
     */
    public List<CpxIcdThesaurus> getByPrimKeyNo2(final String pPrimKeyNo2, final String pCountryEn, final Integer pYear) {
        final String code = toParam(pPrimKeyNo2);
        String sql = "SELECT * FROM %s WHERE PRIM_KEY_NO_2 " + operator(code) + " ?";
        return fetchMany(sql, pCountryEn, pYear, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        List<CpxIcdThesaurus> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, pYear);
//        final String code = toParam(pPrimKeyNo2);
//        String sql = String.format("SELECT * FROM %s WHERE PRIM_KEY_NO_2 " + operator(code) + " ?", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, pYear).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    CpxIcdThesaurus obj = toCpxObject(rs);
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
     * Returns Dummy-Object if ICD Thesaurus cannot be found in local SQLite-DB
     *
     * @param pAddKeyNo Code
     * @param pCountryEn Country ("de", "en")
     * @param pYear year
     * @return Doctor
     */
    public List<CpxIcdThesaurus> getByAddKeyNo(final String pAddKeyNo, final String pCountryEn, final Integer pYear) {
        final String code = toParam(pAddKeyNo);
        String sql = "SELECT * FROM %s WHERE ADD_KEY_NO " + operator(code) + " ?";
        return fetchMany(sql, pCountryEn, pYear, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        List<CpxIcdThesaurus> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, pYear);
//        final String code = toParam(pAddKeyNo);
//        String sql = String.format("SELECT * FROM %s WHERE ADD_KEY_NO " + operator(code) + " ?", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, pYear).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    CpxIcdThesaurus obj = toCpxObject(rs);
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
     * Returns Dummy-Object if ICD Thesaurus cannot be found in local SQLite-DB
     *
     * @param pStarKeyNo Code
     * @param pCountryEn Country ("de", "en")
     * @param pYear year
     * @return Doctor
     */
    public List<CpxIcdThesaurus> getByStarKeyNo(final String pStarKeyNo, final String pCountryEn, final Integer pYear) {
        final String code = toParam(pStarKeyNo);
        String sql = "SELECT * FROM %s WHERE STAR_KEY_NO " + operator(code) + " ?";
        return fetchMany(sql, pCountryEn, pYear, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        List<CpxIcdThesaurus> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, pYear);
//        final String code = toParam(pStarKeyNo);
//        String sql = String.format("SELECT * FROM %s WHERE STAR_KEY_NO " + operator(code) + " ?", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, pYear).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    CpxIcdThesaurus obj = toCpxObject(rs);
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
//     * Returns Dummy-Object if ICD Thesaurus cannot be found in local SQLite-DB
//     *
//     * @param pId ID
//     * @param pCountryEn Country ("de", "en")
//     * @param pYear year
//     * @return ICD Thesaurus
//     */
//    @Override
//    public CpxIcdThesaurus getById(final Long pId, final String pCountryEn, final Integer pYear) {
//        CpxIcdThesaurus obj = getNewObject();
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
     * Returns map of all ICD Thesaurus in SQLite-DB
     *
     * @param pCountryEn Country ("de", "en")
     * @param pYear year
     * @return List of all ICD Thesaurus
     */
    @Override
    public Map<Long, CpxIcdThesaurus> getAll(final String pCountryEn, final Integer pYear) {
        return getAll(pCountryEn, pYear, null);
    }

    /**
     * Returns map of all ICD Thesaurus in SQLite-DB
     *
     * @param pCountryEn Country ("de", "en")
     * @param pYear year
     * @param pTypeOfCode type of code
     * @return List of all ICD Thesaurus
     */
    public Map<Long, CpxIcdThesaurus> getAll(final String pCountryEn, final Integer pYear, final int[] pTypeOfCode) {
        String sql = "SELECT * FROM %s";
        if (pTypeOfCode != null && pTypeOfCode.length > 0) {
            sql += String.format(" WHERE TYPE_OF_CODE IN (%s)", StringUtils.join(ArrayUtils.toObject(pTypeOfCode), ","));
        }
        return toMap(fetchMany(sql, pCountryEn, pYear, (pstmt) -> {
            //
        }));

//        Map<Long, CpxIcdThesaurus> result = new HashMap<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, pYear);
//        String sql = String.format("SELECT * FROM %s", tableName);
//        if (pTypeOfCode != null && pTypeOfCode.length > 0) {
//            sql += String.format(" WHERE TYPE_OF_CODE IN (%s)", StringUtils.join(ArrayUtils.toObject(pTypeOfCode), ","));
//        }
////        try (PreparedStatement pstmt = getCatalogDb(CATALOG, pYear).prepareStatement(sql)) {
//        try ( PreparedStatement pstmt = prepareStatement(CATALOG, pYear, sql)) {
//            if (pstmt != null) { //prepared statement is null if catalog does not exist!
//                try ( ResultSet rs = pstmt.executeQuery()) {
//                    while (rs.next()) {
//                        CpxIcdThesaurus obj = toCpxObject(rs);
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
//            LOG.log(Level.SEVERE, null, ex);
//        }
//        return result;
    }

//    /**
//     * Returns Dummy-Object if ICD Thesaurus Code cannot be found in local
//     * SQLite-DB
//     *
//     * @param pCode ICD Code
//     * @param pCountryEn Country ("de", "en")
//     * @param pYear year
//     * @return ICD
//     */
//    @Override
//    public CpxIcdThesaurus getByCode(final String pCode, final String pCountryEn, final Integer pYear) {
//        List<CpxIcdThesaurus> list = findManyByCode(pCode, pCountryEn, pYear);
//        if (list.isEmpty()) {
//            return getNewObject();
//        }
//        return list.get(0);
//    }
    /**
     * Returns Dummy-Object if ICD Thesaurus cannot be found in local SQLite-DB
     *
     * @param pCode Identifier
     * @param pCountryEn Country ("de", "en")
     * @param pYear year
     * @return ICD Thesaurus
     */
    @Override
    public List<CpxIcdThesaurus> findManyByCode(final String pCode, final String pCountryEn, final Integer pYear) {
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
//    public String getIcdCatalogDBName(CatalogTypeEn pCatalogType, final Integer year) {
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
     * @param pIcdList icd list
     */
    public void getAllThesaurus(final String pCountryEn, final Integer pYear, Map<Long, CpxIcd> pIcdList) {
        long timeStart = System.currentTimeMillis();
        final Map<String, Long> icdCodeMap = new HashMap<>();
        for (Map.Entry<Long, CpxIcd> entry : pIcdList.entrySet()) {
            icdCodeMap.put(entry.getValue().getCode(), entry.getKey());
        }
        int[] typeOfCode = new int[]{1, 2, 3, 4, 5, 6};
        final Map<Long, CpxIcdThesaurus> thesaurus = getAll(pCountryEn, pYear, typeOfCode);
        for (Map.Entry<Long, CpxIcdThesaurus> entry : thesaurus.entrySet()) {
            final CpxIcdThesaurus icdThesaurus = entry.getValue();
            if (icdThesaurus.getPrimKeyNo1() != null) {
                Long icdId = icdCodeMap.get(icdThesaurus.getPrimKeyNo1().replace("+", ""));
                if (icdId != null) {
                    pIcdList.get(icdId).addThesaurus(icdThesaurus);
                }
            }
            if (icdThesaurus.getPrimKeyNo2() != null) {
                Long icdId = icdCodeMap.get(icdThesaurus.getPrimKeyNo2().replace("+", ""));
                if (icdId != null) {
                    pIcdList.get(icdId).addThesaurus(icdThesaurus);
                }
            }
            if (icdThesaurus.getAddKeyNo() != null) {
                Long icdId = icdCodeMap.get(icdThesaurus.getAddKeyNo().replace("!", ""));
                if (icdId != null) {
                    pIcdList.get(icdId).addThesaurus(icdThesaurus);
                }
            }
            if (icdThesaurus.getStarKeyNo() != null) {
                Long icdId = icdCodeMap.get(icdThesaurus.getStarKeyNo().replace("*", ""));
                if (icdId != null) {
                    pIcdList.get(icdId).addThesaurus(icdThesaurus);
                }
            }
            //LOG.log(Level.FINEST, "Unknown icd in thesaurus with id " + icdThesaurus.id);
        }
        LOG.log(Level.INFO, "Time elapsed while loading ICD Thesaurus: " + (System.currentTimeMillis() - (double) timeStart) / 1000d + "s");
    }

    @Override
    public CpxIcdThesaurus getNewObject() {
        return new CpxIcdThesaurus();
    }

//     
//    /**
//     * Method to match the Thesaurus with the ICD keys Internationale
//     * statistische Klassifikation der Krankheiten und verwandter
//     * Gesundheitsprobleme, 10. Revision Stand der Klassifikation: 04.10.2017
//     *
//     * @param pCountryEn Country ("de", "en")
//     * @param pYear Year
//     * @param icdList icd list
//     */
//    public void getAllThesaurus(final String pCountryEn, final Integer pYear, Map<Long, CpxIcd> icdList) {
//        long timeStartAll = System.currentTimeMillis();
//        long timeStart = System.currentTimeMillis();
//        int year = checkYear(pYear);
//        Map<Integer, Map<Long, CpxIcd>> objList = new HashMap<>();
//        final String icdTableName = getTableName(CatalogTypeEn.ICD, pCountryEn, year);
//        final String thesaurusTableName = getTableName(CATALOG, pCountryEn, year);
//
//        try {
//            if (!isIcdDBAttached) {
//                getCatalogDb(CATALOG, year).prepareStatement("ATTACH DATABASE \"" + CpxSystemProperties.getInstance().getCpxClientCatalogDir() + getIcdCatalogDBName(CatalogTypeEn.ICD, year) + ".db\" AS  ICD_DB").execute();
////                getCatalogDb(CATALOG, year).prepareStatement("ATTACH DATABASE \"" + CpxSystemProperties.getInstance().getCpxClientCatalogDir() + "cpx_catalog_G_Drg_Report_2018.db" + "\" AS  G_Drg_Report").execute();
//                isIcdDBAttached = true;
//            }
//        } catch (SQLException ex) {
//            LOG.log(Level.SEVERE, null, ex);
//        }
//        //TYPE_OF_CODE = 0
////        String sql = String.format("SELECT icd.id,T.* , substr(T.description, 1, pos-1) AS reference, substr(T.description, pos+1) AS newDescription FROM %s as icd ,(SELECT *, instr(description,' - ') AS pos FROM C_ICD_Thesaurus) as T where icd.ICD_DESCRIPTION like '%%' || reference || '%%' and T.typeOfCode = 0  and newDescription NOT like '%%Art der Krankheit%%'", tableName);
////        /*  + " ORDER BY ICD_DEPTH, ICD_CODE */
////        
////        try (PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
////            try (ResultSet rs = pstmt.executeQuery()) {
////                while (rs.next()) {
////                    long l = rs.getLong("ID");
////                    CpxThesaurus obj = CpxThesaurus.toCpxObjectICD(rs);
////                    if (obj != null) {
////                      icdList.get(l).addIcdThesaurusObj(obj);
////                    }
////                }
////                rs.close();
////            }finally{
////                pstmt.close();
////            }
////        } catch (SQLException ex) {
////            //Catalog does not exist, SQLite DB file was deleted?!
////            LOG.log(Level.SEVERE, null, ex);
////        }
////        
////        long timeEnd = System.currentTimeMillis();
////        LOG.log(Level.INFO, "Time elapsed while loading ICD_Thesaurus TYPE_OF_CODE = 0: " + (timeEnd - (double) timeStart) / 1000d + "s");
////        timeStart = System.currentTimeMillis();
////           //TYPE_OF_CODE = 1
//        String sql = String.format("select * from ICD_DB.%s as icd, %s as T where icd.ICD_Code = T.PRIM_KEY_NO_1 and T.TYPE_OF_CODE = 1", icdTableName, thesaurusTableName);
//        /*  + " ORDER BY ICD_DEPTH, ICD_CODE */
////        Connection catalogDb = getCatalogDb(CATALOG, year);
//        try (PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            try (ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    long l = rs.getLong("ID");
//                    CpxIcdThesaurus obj = toCpxObject(rs);
//                    if (obj != null) {
//                        icdList.get(l).addIcdThesaurusObj(obj);
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
//        LOG.log(Level.INFO, "Time elapsed while loading ICD_Thesaurus TYPE_OF_CODE = 1: " + (timeEnd - (double) timeStart) / 1000d + "s");
//        timeStart = System.currentTimeMillis();
//
//        //TYPE_OF_CODE = 2
//        sql = String.format("select Distinct * from ICD_DB.%s as icd, %s as T where (icd.ICD_Code = SUBSTR(T.PRIM_KEY_NO_1 ,1,length(T.PRIM_KEY_NO_1)-1) or icd.ICD_Code = SUBSTR(T.STAR_KEY_NO ,1,length(T.STAR_KEY_NO)-1)) and T.TYPE_OF_CODE = 2", icdTableName, thesaurusTableName);
//        /*  + " ORDER BY ICD_DEPTH, ICD_CODE */
//        try (PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            try (ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    long l = rs.getLong("ID");
//                    CpxIcdThesaurus obj = toCpxObject(rs);
//                    if (obj != null) {
//                        icdList.get(l).addIcdThesaurusObj(obj);
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
//        LOG.log(Level.INFO, "Time elapsed while loading ICD_Thesaurus TYPE_OF_CODE = 2: " + (timeEnd - (double) timeStart) / 1000d + "s");
//        timeStart = System.currentTimeMillis();
//
//        //TYPE_OF_CODE = 3
//        sql = String.format("select Distinct * from ICD_DB.%s as icd, %s as T where (icd.ICD_Code = T.PRIM_KEY_NO_1 or icd.ICD_Code = SUBSTR(T.ADD_KEY_NO ,1,length(T.ADD_KEY_NO)-1)) and T.TYPE_OF_CODE = 3", icdTableName, thesaurusTableName);
//        /*  + " ORDER BY ICD_DEPTH, ICD_CODE */
//        try (PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            try (ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    long l = rs.getLong("ID");
//                    CpxIcdThesaurus obj = toCpxObject(rs);
//                    if (obj != null) {
//                        icdList.get(l).addIcdThesaurusObj(obj);
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
//        LOG.log(Level.INFO, "Time elapsed while loading ICD_Thesaurus TYPE_OF_CODE = 3: " + (timeEnd - (double) timeStart) / 1000d + "s");
//        timeStart = System.currentTimeMillis();
//
//        //TYPE_OF_CODE = 4
//        sql = String.format("select Distinct * from ICD_DB.%s as icd, %s as T where (icd.ICD_Code = SUBSTR(T.PRIM_KEY_NO_1 ,1,length(T.PRIM_KEY_NO_1)-1) or icd.ICD_Code = SUBSTR(T.STAR_KEY_NO ,1,length(T.STAR_KEY_NO)-1) or icd.ICD_Code = SUBSTR(T.ADD_KEY_NO ,1,length(T.ADD_KEY_NO)-1)) and T.TYPE_OF_CODE = 4", icdTableName, thesaurusTableName);
//        /*  + " ORDER BY ICD_DEPTH, ICD_CODE */
//        try (PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            try (ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    long l = rs.getLong("ID");
//                    CpxIcdThesaurus obj = toCpxObject(rs);
//                    if (obj != null) {
//                        icdList.get(l).addIcdThesaurusObj(obj);
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
//        LOG.log(Level.INFO, "Time elapsed while loading ICD_Thesaurus TYPE_OF_CODE = 4: " + (timeEnd - (double) timeStart) / 1000d + "s");
//        timeStart = System.currentTimeMillis();
//
//        //TYPE_OF_CODE = 5
//        sql = String.format("select Distinct * from ICD_DB.%s as icd, %s as T where (icd.ICD_Code = SUBSTR(T.ADD_KEY_NO ,1,length(T.ADD_KEY_NO)-1)) and T.TYPE_OF_CODE = 5", icdTableName, thesaurusTableName);
//        /*  + " ORDER BY ICD_DEPTH, ICD_CODE */
//        try (PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            try (ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    long l = rs.getLong("ID");
//                    CpxIcdThesaurus obj = toCpxObject(rs);
//                    if (obj != null) {
//                        icdList.get(l).addIcdThesaurusObj(obj);
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
//        LOG.log(Level.INFO, "Time elapsed while loading ICD_Thesaurus TYPE_OF_CODE = 5: " + (timeEnd - (double) timeStart) / 1000d + "s");
//        timeStart = System.currentTimeMillis();
//
//        //TYPE_OF_CODE = 6
//        sql = String.format("select Distinct * from ( \n"
//                + "            select Distinct * from ICD_DB.%s as icd, %s as T where (icd.ICD_Code = T.PRIM_KEY_NO_1 or icd.ICD_Code = T.PRIM_KEY_NO_2)  and T.TYPE_OF_CODE = 6  \n"
//                + "            UNION ALL\n"
//                + "            select Distinct * from ICD_DB.%s as icd, %s as T where (icd.ICD_Code = SUBSTR(T.PRIM_KEY_NO_1 ,1,length(T.PRIM_KEY_NO_1)-1) or icd.ICD_Code = SUBSTR(T.STAR_KEY_NO ,1,length(T.STAR_KEY_NO)-1) )  and T.TYPE_OF_CODE = 6  and  SUBSTR(T.STAR_KEY_NO ,-1) = '*'\n"
//                + "            UNION ALL\n"
//                + "            select Distinct * from ICD_DB.%s as icd, %s as T where (icd.ICD_Code = T.PRIM_KEY_NO_1 or icd.ICD_Code = T.PRIM_KEY_NO_2 or  icd.ICD_Code = SUBSTR(T.ADD_KEY_NO ,1,length(T.ADD_KEY_NO)-1) )  and T.TYPE_OF_CODE = 6   and  SUBSTR(T.ADD_KEY_NO ,-1) = '!'\n"
//                + "   ) ", icdTableName, thesaurusTableName, icdTableName, thesaurusTableName, icdTableName, thesaurusTableName);
//        /*  + " ORDER BY ICD_DEPTH, ICD_CODE */
//        try (PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            try (ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    long l = rs.getLong("ID");
//                    CpxIcdThesaurus obj = toCpxObject(rs);
//                    if (obj != null) {
//                        icdList.get(l).addIcdThesaurusObj(obj);
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
//        LOG.log(Level.INFO, "Time elapsed while loading ICD_Thesaurus TYPE_OF_CODE = 6: " + (timeEnd - (double) timeStart) / 1000d + "s");
//        timeStart = System.currentTimeMillis();
//
//        long timeEndAll = System.currentTimeMillis();
//        LOG.log(Level.INFO, "Time elapsed while loading All ICD_Thesaurus catalog: " + (timeEndAll - (double) timeStartAll) / 1000d + "s");
//
//    }
}
