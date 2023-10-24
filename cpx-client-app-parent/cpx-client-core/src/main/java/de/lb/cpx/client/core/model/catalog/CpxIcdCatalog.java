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
import static de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog.checkYear;
import static de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog.getCatalogDb;
import static de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog.getTableName;
import de.lb.cpx.server.commonDB.model.CIcdCatalog;
import de.lb.cpx.service.information.CatalogTypeEn;
import de.lb.cpx.service.information.CpxCatalogOverview;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dirk Niemeier
 */
public class CpxIcdCatalog extends AbstractCpxCatalog<CpxIcd, CIcdCatalog> {

    private static CpxIcdCatalog instance = null;
    public static final CatalogTypeEn CATALOG = CatalogTypeEn.ICD;
    private static final Logger LOG = Logger.getLogger(CpxIcdCatalog.class.getName());
    private static final Map<Integer, Map<Long, CpxIcd>> icd4year = new HashMap<>();
    
    public static synchronized CpxIcdCatalog instance() {
        if (instance == null) {
            instance = new CpxIcdCatalog();
        }
        return instance;
    }
    //Logger log = Logger.getLogger(getClass().getSimpleName());

    private CpxIcdCatalog() {

    }

    @Override
    public void fillCatalog(final CpxCatalogOverview pCatalogOverview, final List<CIcdCatalog> pList) throws SQLException {
        final String tableName = getTableName(pCatalogOverview);
        String sqlHead = String.format("INSERT INTO %s (ID, CREATION_DATE, MODIFICATION_DATE, COUNTRY_EN, ICD_CODE, ICD_IS_COMPLETE_FL, ICD_PARENT_ID, ICD_YEAR, ICD_DEPTH, ICD_DESCRIPTION, ICD_EXCLUSION, ICD_INCLUSION, ICD_NOTE) ", tableName);
        StringBuilder sql = null;
        //String sql = "INSERT INTO ICD_CATALOG(ID, CREATION_DATE, MODIFICATION_DATE, COUNTRY_EN, ICD_CODE, ICD_IS_COMPLETE_FL, ICD_PARENT_ID, ICD_YEAR, ICD_DESCRIPTION, ICD_EXCLUSION, ICD_INCLUSION, ICD_NOTE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int i = 0;
        boolean first = true;
        for (CIcdCatalog icdCatalog : pList) {
            i++;
            if (first) {
                sql = new StringBuilder(sqlHead);
                sql.append(" SELECT ");
                first = false;
            } else {
                sql.append(" UNION ALL SELECT ");
            }
            sql.append(icdCatalog.getId() + ", ");
            sql.append(QStr(icdCatalog.getCreationDate()) + ", ");
            sql.append(QStr(icdCatalog.getModificationDate()) + ", ");
            sql.append(QStr(icdCatalog.getCountryEn().name()) + ", ");
            sql.append(QStr(icdCatalog.getIcdCode()) + ", ");
            sql.append(QStr(icdCatalog.getIcdIsCompleteFl()) + ", ");
            sql.append(QStr((icdCatalog.getCIcdCatalog() == null) ? null : icdCatalog.getCIcdCatalog().getId()) + ", ");
            sql.append(QStr(icdCatalog.getIcdYear()) + ", ");
            sql.append(QStr(icdCatalog.getIcdDepth()) + ", ");
            sql.append(QStr(icdCatalog.getIcdDescription()) + ", ");
            sql.append(QStr(icdCatalog.getIcdExclusion()) + ", ");
            sql.append(QStr(icdCatalog.getIcdInclusion()) + ", ");
            sql.append(QStr(icdCatalog.getIcdNote()));
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
            String sql = String.format("CREATE TABLE IF NOT EXISTS %s", tableName)
                    + "(ID                 INTEGER    PRIMARY KEY,"
                    + " CREATION_DATE      DATETIME, "
                    + " MODIFICATION_DATE  DATETIME, "
                    + " COUNTRY_EN         VARCHAR(25), "
                    + " ICD_CODE           VARCHAR(15), "
                    + " ICD_IS_COMPLETE_FL TINYINT    NOT NULL, "
                    + " ICD_PARENT_ID      BIGINT, "
                    + " ICD_YEAR           INT, "
                    + " ICD_DEPTH          TINYINT, "
                    + " ICD_DESCRIPTION    CLOB, "
                    + " ICD_EXCLUSION      CLOB, "
                    + " ICD_INCLUSION      CLOB, "
                    + " ICD_NOTE           CLOB ) ";
            stmt.executeUpdate(sql);

            sql = String.format("CREATE INDEX IF NOT EXISTS IDX_ICD ON %s (ICD_CODE)", tableName); //, COUNTRY_EN, ICD_YEAR
            stmt.executeUpdate(sql);

            sql = String.format("CREATE INDEX IF NOT EXISTS IDX_ICD_DEPTH ON %s (ICD_DEPTH)", tableName); //, COUNTRY_EN, ICD_YEAR
            stmt.executeUpdate(sql);

            stmt.close();
            return tableName;
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return "";
        }
    }

    @Override
    public CpxIcd toCpxObject(final ResultSet rs) throws SQLException {
        CpxIcd obj = getNewObject();
        if (rs == null) {
            return obj;
        }
        obj.setId(rs.getLong("ID"));
        //Filling datetime fields takes quite a long time, so I don't care here!
        //obj.setCreationDate(CpxDateParser.parseDateTimeExc(rs.getString("CREATION_DATE")));
        //obj.setModificationDate(CpxDateParser.parseDateTimeExc(rs.getString("MODIFICATION_DATE")));
        //obj.setCountryEn(CountryEn.valueOf(rs.getString("COUNTRY_EN")));
        obj.setIcdCode(rs.getString("ICD_CODE"));
        obj.setIcdDescription(rs.getString("ICD_DESCRIPTION"));
        obj.setIcdExclusion(rs.getString("ICD_EXCLUSION"));
        obj.setIcdInclusion(rs.getString("ICD_INCLUSION"));
        obj.setIcdIsCompleteFl(rs.getBoolean("ICD_IS_COMPLETE_FL"));
        obj.setIcdNote(rs.getString("ICD_NOTE"));
        obj.setIcdYear(rs.getInt("ICD_YEAR"));
        obj.setIcdDepth(rs.getInt("ICD_DEPTH"));
        obj.setParentId(rs.getLong("ICD_PARENT_ID"));
        return obj;
    }

//    /**
//     * Returns Dummy-Object if ICD Code cannot be found in local SQLite-DB
//     *
//     * @param pCode ICD Code
//     * @param pCountryEn Country ("de", "en")
//     * @param pYear Year
//     * @return ICD
//     */
//    @Override
//    public CpxIcd getByCode(final String pCode, final String pCountryEn, final Integer pYear) {
//        List<CpxIcd> list = findManyByCode(pCode, pCountryEn, pYear);
//        if (list.isEmpty()) {
//            return getNewObject();
//        }
//        return list.get(0);
//    }
//    /**
//     * Returns Dummy-Object if ICD Code cannot be found in local SQLite-DB
//     *
//     * @param pId ID
//     * @param pCountryEn Country ("de", "en")
//     * @param pYear Year
//     * @return ICD
//     */
//    @Override
//    public CpxIcd getById(final Long pId, final String pCountryEn, final Integer pYear) {
//        int year = checkYear(pYear);
//        CpxIcd obj = getNewObject();
//        long id = (pId == null) ? 0L : pId;
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT * FROM %s WHERE ID = ?", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
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
//    public List<CpxIcd> getParamIndependent(final String param, final String pCountryEn, final Integer pYear) {
//        final String code = "%" + toParam(param) + "%";
//        final String operator = operator(code);
//        String sql = "SELECT * FROM %s WHERE"
//                + " ICD_DESCRIPTION " + operator + " ?"
//                + " OR ICD_CODE " + operator + " ?"
//                + " OR ICD_EXCLUSION " + operator + " ?"
//                + " OR ICD_INCLUSION " + operator + " ?"
//                + " OR ICD_NOTE " + operator + " ?"
//                + " ORDER BY ICD_CODE";
//        return fetchMany(sql, pCountryEn, (pstmt) -> {
//            for (int i = 1; i < 6; i++) {
//                pstmt.setString(i, code);
//            }
//        });
//
////        int year = checkYear(pYear);
////        List<CpxIcd> objList = new ArrayList<>();
////        final String tableName = getTableName(CATALOG, pCountryEn, year);
////        final String code = "%" + toParam(param) + "%";
////        final String operator = operator(code);
////
////        String sql = String.format("SELECT * FROM %s WHERE", tableName)
////                + " ICD_DESCRIPTION " + operator + " ?"
////                + " OR ICD_CODE " + operator + " ?"
////                + " OR ICD_EXCLUSION " + operator + " ?"
////                + " OR ICD_INCLUSION " + operator + " ?"
////                + " OR ICD_NOTE " + operator + " ?"
////                + " ORDER BY ICD_CODE";
////        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
////            for (int i = 1; i < 6; i++) {
////                pstmt.setString(i, code);
////            }
////            try ( ResultSet rs = pstmt.executeQuery()) {
////                while (rs.next()) {
////                    CpxIcd obj = toCpxObject(rs);
////                    if (obj != null) {
////                        objList.add(obj);
////                    }
////                }
////                rs.close();
////            } finally {
////                if (pstmt != null) {
////                    pstmt.close();
////                }
////            }
////        } catch (SQLException ex) {
////            LOG.log(Level.SEVERE, null, ex);
////        }
////
////        return objList;
//    }
    /**
     * Returns Dummy-Object if ICD Code cannot be found in local SQLite-DB
     *
     * @param pParentId Parent-ID
     * @param pCountryEn Country ("de", "en")
     * @param pYear Year
     * @return ICD
     */
    public List<CpxIcd> getIcdChildren(final Long pParentId, final String pCountryEn, final Integer pYear) {
        long parentId = (pParentId == null) ? 0L : pParentId;
        String sql = "SELECT * FROM %s WHERE ICD_PARENT_ID " + (parentId == 0L ? "IS" : "=") + " ? ORDER BY ICD_CODE";
        return fetchMany(sql, pCountryEn, pYear, (pstmt) -> {
            if (parentId != 0L) {
                pstmt.setLong(1, parentId);
            } else {
                pstmt.setNull(1, Types.INTEGER);
            }
        });

//        int year = checkYear(pYear);
//        List<CpxIcd> objList = new ArrayList<>();
//        long parentId = (pParentId == null) ? 0L : pParentId;
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT * FROM %s WHERE ICD_PARENT_ID " + (parentId == 0L ? "IS" : "=") + " ? ORDER BY ICD_CODE", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            if (parentId != 0L) {
//                pstmt.setLong(1, parentId);
//            } else {
//                pstmt.setNull(1, Types.INTEGER);
//            }
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    CpxIcd obj = toCpxObject(rs);
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
//        return objList;
    }

    /**
     * Returns Dummy-Object if ICD Code cannot be found in local SQLite-DB
     *
     * @param pCode ICD-Code
     * @param pCountryEn Country ("de", "en")
     * @param pYear Year
     * @return ICD
     */
    @Override
    public List<CpxIcd> findManyByCode(final String pCode, final String pCountryEn, final Integer pYear) {
        final String code = toParam(pCode);
        String sql = "SELECT * FROM %s WHERE ICD_CODE " + operator(code) + " ? ORDER BY ICD_CODE";
        return fetchMany(sql, pCountryEn, pYear, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        final long startTime = System.currentTimeMillis();
//        int year = checkYear(pYear);
//        List<CpxIcd> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam(pCode);
//        String sql = String.format("SELECT * FROM %s WHERE ICD_CODE " + operator(code) + " ? ORDER BY ICD_CODE", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    CpxIcd obj = toCpxObject(rs);
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
//        LOG.log(Level.FINEST, "Search for ICD " + code + " took " + (System.currentTimeMillis() - startTime) + " ms");
//        return objList;
    }

    /**
     * add parent description to the children for better search results
     *
     * @param icdItem icd
     * @return description
     */
    public static String initIcdChildrenDescription(CpxIcd icdItem) {
        String description = "";
        //this Method will only add descriptions to the last child
        if (icdItem.getParent() != null && icdItem.getChildren().isEmpty()) {

            if (icdItem.getIcdInclusion() == null) {
                description = icdItem.getCode() + " - " + icdItem.getDescription() + " ";
            } else {
                description = icdItem.getCode() + " - " + icdItem.getDescription() + " " + icdItem.getIcdInclusion() + " ";
            }

            ICpxTreeItem<CpxIcdThesaurus> icd = icdItem;
            while (icd.getParent() != null) {
                if (icd.getParent().getInclusion() == null) {
                    description = description + " " + icd.getParent().getCode() + " " + icd.getParent().getDescription() + " ";
                } else {
                    description = description + " " + icd.getParent().getCode() + " " + icd.getParent().getDescription() + " " + icd.getParent().getInclusion() + " ";
                }
                icd = icd.getParent();
            }
        }

        return description;
    }

    @Override
    public Map<Long, CpxIcd> getAll(final String pCountryEn, final Integer pYear) {
         Map<Long, CpxIcd> retIcdsMap = icd4year.get(pYear);
         if(retIcdsMap != null){
             return retIcdsMap;
         }
               
        long timeStart = System.currentTimeMillis();

        int year = checkYear(pYear);
        Map<Long, CpxIcd> icdList = new HashMap<>();

        Map<Integer, Map<Long, CpxIcd>> objList = new HashMap<>();
        final String tableName = getTableName(CATALOG, pCountryEn, year);
        String sql = String.format("SELECT * FROM %s", tableName);
        /*  + " ORDER BY ICD_DEPTH, ICD_CODE */
        //try (PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
        try ( PreparedStatement pstmt = prepareStatement(CATALOG, year, sql)) {
            if (pstmt != null) { //prepared statement is null if catalog does not exist!
                try ( ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        CpxIcd obj = toCpxObject(rs);
                        if (obj != null) {
                            icdList.put(obj.getId(), obj);
                            int depth = obj.getIcdDepth();
                            Map<Long, CpxIcd> list = objList.get(depth);
                            if (list == null) {
                                list = new HashMap<>();
                                objList.put(depth, list);
                            }
                            if (depth > 0) {
                                CpxIcd parent = objList.get(depth - 1).get(obj.getParentId());
                                obj.setParent(parent);
                                parent.addChildren(obj);
                            }
                            objList.get(obj.getIcdDepth()).put(obj.getId(), obj);
                        }
                    }
                    rs.close();
                } finally {
                    if (pstmt != null) {
                        pstmt.close();
                    }
                }
            }
        } catch (SQLException ex) {
            //Catalog does not exist, SQLite DB file was deleted?!
            LOG.log(Level.SEVERE, null, ex);
        }
        // loop to fill Descriptions in SearchableDescription
        for (Map.Entry<Long, CpxIcd> entry : icdList.entrySet()) {
            CpxIcd obj = entry.getValue();

            obj.setSearchableDescription(initIcdChildrenDescription(obj));
        }

        long timeEnd = System.currentTimeMillis();

        LOG.log(Level.INFO, "Time elapsed while loading ICD catalog: " + (timeEnd - (double) timeStart) / 1000d + "s");

        Map<Long, CpxIcd> map = objList.get(0);

        CpxIcdThesaurusCatalog.instance().getAllThesaurus(pCountryEn, pYear, icdList);

        if (map == null) {
            return new HashMap<>();
        }
        icd4year.put(year, map);
        return map;
    }

    @Override
    public CatalogTypeEn getCatalogType() {
        return CATALOG;
    }

    /**
     * get the Description for a specific icd code
     *
     * @param pCode icd code
     * @param pCountryEn language
     * @param pYear year of validity
     * @return catalog discription or empty string if nothing was found
     */
    public String getDescriptionByCode(String pCode, String pCountryEn, int pYear) {
        final String code = toParam(pCode);
        String sql = "SELECT ICD_DESCRIPTION FROM %s WHERE ICD_CODE " + operator(code) + " ? ORDER BY ICD_CODE";
        return fetchSingleValue(sql, pCountryEn, pYear, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        final String tableName = getTableName(CATALOG, pCountryEn, pYear);
//        final String code = toParam(pCode);
//        String sql = String.format("SELECT ICD_DESCRIPTION FROM %s WHERE ICD_CODE " + operator(code) + " ? ORDER BY ICD_CODE", tableName);
//        try ( PreparedStatement pstmt = prepareStatement(CATALOG, pYear, sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    return rs.getString("ICD_DESCRIPTION");
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
//        return "";
    }

    @Override
    public CpxIcd getNewObject() {
        return new CpxIcd();
    }

}
