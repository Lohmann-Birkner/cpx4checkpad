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
import de.lb.cpx.server.commonDB.model.COpsCatalog;
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
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dirk Niemeier
 */
public class CpxOpsCatalog extends AbstractCpxCatalog<CpxOps, COpsCatalog> {

    private static CpxOpsCatalog instance = null;
    public static final CatalogTypeEn CATALOG = CatalogTypeEn.OPS;
    private static final Logger LOG = Logger.getLogger(CpxOpsCatalog.class.getName());

    private static final Map<Integer, Map<Long, CpxOps>> ops4year = new HashMap<>();
    public static synchronized CpxOpsCatalog instance() {
        if (instance == null) {
            instance = new CpxOpsCatalog();
        }
        return instance;
    }
    //Logger log = Logger.getLogger(getClass().getSimpleName());

    private CpxOpsCatalog() {

    }

    @Override
    public void fillCatalog(final CpxCatalogOverview pCatalogOverview, final List<COpsCatalog> pList) throws SQLException {
        final String tableName = getTableName(pCatalogOverview);
        String sqlHead = String.format("INSERT INTO %s(ID, CREATION_DATE, MODIFICATION_DATE, COUNTRY_EN, OPS_CODE, OPS_IS_COMPLETE_FL, OPS_PARENT_ID, OPS_YEAR, OPS_DEPTH, OPS_DESCRIPTION, OPS_EXCLUSION, OPS_INCLUSION, OPS_NOTE) ", tableName);
        StringBuilder sql = null;
        //String sql = "INSERT INTO ICD_CATALOG(ID, CREATION_DATE, MODIFICATION_DATE, COUNTRY_EN, ICD_CODE, ICD_IS_COMPLETE_FL, ICD_PARENT_ID, ICD_YEAR, ICD_DESCRIPTION, ICD_EXCLUSION, ICD_INCLUSION, ICD_NOTE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int i = 0;
        boolean first = true;
        for (COpsCatalog opsCatalog : pList) {
            i++;
            if (first) {
                sql = new StringBuilder(sqlHead);
                sql.append(" SELECT ");
                first = false;
            } else {
                sql.append(" UNION ALL SELECT ");
            }
            sql.append(opsCatalog.getId() + ", ");
            sql.append(QStr(opsCatalog.getCreationDate()) + ", ");
            sql.append(QStr(opsCatalog.getModificationDate()) + ", ");
            sql.append(QStr(opsCatalog.getCountryEn().name()) + ", ");
            sql.append(QStr(opsCatalog.getOpsCode()) + ", ");
            sql.append(QStr(opsCatalog.getOpsIsCompleteFl()) + ", ");
            sql.append(QStr((opsCatalog.getCOpsCatalog() == null) ? null : opsCatalog.getCOpsCatalog().getId()) + ", ");
            sql.append(QStr(opsCatalog.getOpsYear()) + ", ");
            sql.append(QStr(opsCatalog.getOpsDepth()) + ", ");
            sql.append(QStr(opsCatalog.getOpsDescription()) + ", ");
            sql.append(QStr(opsCatalog.getOpsExclusion()) + ", ");
            sql.append(QStr(opsCatalog.getOpsInclusion()) + ", ");
            sql.append(QStr(opsCatalog.getOpsNote()));
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
                    + " COUNTRY_EN         VARCHAR(25), "
                    + " OPS_CODE           VARCHAR(15), "
                    + " OPS_IS_COMPLETE_FL TINYINT    NOT NULL, "
                    + " OPS_PARENT_ID      BIGINT, "
                    + " OPS_YEAR           INT, "
                    + " OPS_DEPTH          TINYINT, "
                    + " OPS_DESCRIPTION    CLOB, "
                    + " OPS_EXCLUSION      CLOB, "
                    + " OPS_INCLUSION      CLOB, "
                    + " OPS_NOTE           CLOB ) ";
            stmt.executeUpdate(sql);

            sql = String.format("CREATE INDEX IF NOT EXISTS IDX_OPS ON %s (OPS_CODE)", tableName); //COUNTRY_EN, OPS_YEAR
            stmt.executeUpdate(sql);

            sql = String.format("CREATE INDEX IF NOT EXISTS IDX_OPS_DEPTH ON %s (OPS_DEPTH)", tableName); //, COUNTRY_EN, ICD_YEAR
            stmt.executeUpdate(sql);

            stmt.close();
            return tableName;
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return "";
        }
    }

    @Override
    public CpxOps toCpxObject(final ResultSet rs) throws SQLException {
        CpxOps obj = getNewObject();
        if (rs == null) {
            return obj;
        }
        obj.setId(rs.getLong("ID"));
        //Filling datetime fields takes quite a long time, so I don't care here!
        //obj.setCreationDate(CpxDateParser.parseDateTimeExc(rs.getString("CREATION_DATE")));
        //obj.setModificationDate(CpxDateParser.parseDateTimeExc(rs.getString("MODIFICATION_DATE")));
        //obj.setCountryEn(CountryEn.valueOf(rs.getString("COUNTRY_EN")));
        obj.setOpsCode(rs.getString("OPS_CODE"));
        obj.setOpsDescription(rs.getString("OPS_DESCRIPTION"));
        obj.setOpsExclusion(rs.getString("OPS_EXCLUSION"));
        obj.setOpsInclusion(rs.getString("OPS_INCLUSION"));
        obj.setOpsIsCompleteFl(rs.getBoolean("OPS_IS_COMPLETE_FL"));
        obj.setOpsNote(rs.getString("OPS_NOTE"));
        obj.setOpsYear(rs.getInt("OPS_YEAR"));
        obj.setOpsDepth(rs.getInt("OPS_DEPTH"));
        obj.setParentId(rs.getLong("OPS_PARENT_ID"));
        return obj;
    }

//    /**
//     * Returns Dummy-Object if OPS Code cannot be found in local SQLite-DB
//     *
//     * @param pCode OPS Code
//     * @param pCountryEn Country ("de", "en")
//     * @param pYear Year
//     * @return OPS
//     */
//    @Override
//    public CpxOps getByCode(final String pCode, final String pCountryEn, final Integer pYear) {
//        List<CpxOps> list = findManyByCode(pCode, pCountryEn, pYear);
//        if (list.isEmpty()) {
//            return getNewObject();
//        }
//        return list.get(0);
//    }
//    /**
//     * Returns Dummy-Object if OPS Code cannot be found in local SQLite-DB
//     *
//     * @param pId ID
//     * @param pCountryEn Country ("de", "en")
//     * @param pYear Year
//     * @return OPS
//     */
//    @Override
//    public CpxOps getById(final Long pId, final String pCountryEn, final Integer pYear) {
//        int year = checkYear(pYear);
//        CpxOps obj = getNewObject();
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
//            Logger.getLogger(CpxOpsCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return obj;
//    }
    /**
     * Returns Dummy-Object if OPS Code cannot be found in local SQLite-DB
     *
     * @param pParentId Parent-ID
     * @param pCountryEn Country ("de", "en")
     * @param pYear Year
     * @return OPS
     */
    public List<CpxOps> getOpsChildren(final Long pParentId, final String pCountryEn, final Integer pYear) {
        long parentId = (pParentId == null) ? 0L : pParentId;
        String sql = "SELECT * FROM %s WHERE OPS_PARENT_ID " + (parentId == 0L ? "IS" : "=") + " ? ORDER BY OPS_CODE";
        return fetchMany(sql, pCountryEn, pYear, (pstmt) -> {
            if (parentId != 0L) {
                pstmt.setLong(1, parentId);
            } else {
                pstmt.setNull(1, Types.INTEGER);
            }
        });

//        int year = checkYear(pYear);
//        List<CpxOps> objList = new ArrayList<>();
//        long parentId = (pParentId == null) ? 0L : pParentId;
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT * FROM %s WHERE OPS_PARENT_ID " + (parentId == 0L ? "IS" : "=") + " ? ORDER BY OPS_CODE", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            if (parentId != 0L) {
//                pstmt.setLong(1, parentId);
//            } else {
//                pstmt.setNull(1, Types.INTEGER);
//            }
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    CpxOps obj = toCpxObject(rs);
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
//            Logger.getLogger(CpxOpsCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return objList;
    }

    /**
     * Returns Dummy-Object if OPS Code cannot be found in local SQLite-DB
     *
     * @param pCode OPS-Code
     * @param pCountryEn Country ("de", "en")
     * @param pYear Year
     * @return OPS
     */
    @Override
    public List<CpxOps> findManyByCode(final String pCode, final String pCountryEn, final Integer pYear) {
        final String code = toParam(pCode);
        String sql = "SELECT * FROM %s WHERE OPS_CODE " + operator(code) + " ? ORDER BY OPS_CODE";
        return fetchMany(sql, pCountryEn, pYear, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        final long startTime = System.currentTimeMillis();
//        int year = checkYear(pYear);
//        List<CpxOps> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam(pCode);
//        String sql = String.format("SELECT * FROM %s WHERE OPS_CODE " + operator(code) + " ? ORDER BY OPS_CODE", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    CpxOps obj = toCpxObject(rs);
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
//            Logger.getLogger(CpxOpsCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        LOG.log(Level.FINEST, "Search for OPS " + code + " took " + (System.currentTimeMillis() - startTime) + " ms");
//        return objList;
    }

    /**
     * add parent description to the children for better search results
     *
     * @param opsItem ops item
     * @return description
     */
    public static String initOpsChilderenDescription(CpxOps opsItem) {
        String description = "";
        //this Method will only add descriptions to the last child
        if (opsItem.getParent() != null && opsItem.getChildren().isEmpty()) {

            if (opsItem.getOpsInclusion() == null) {
                description = opsItem.getCode() + " - " + opsItem.getDescription() + " ";
            } else {
                description = opsItem.getCode() + " - " + opsItem.getDescription() + " " + opsItem.getOpsInclusion() + " ";
            }

            ICpxTreeItem<CpxOpsThesaurus> ops = opsItem;
            while (ops.getParent() != null) {
                if (ops.getParent().getInclusion() == null) {
                    description = description + " " + ops.getParent().getCode() + " " + ops.getParent().getDescription() + " ";
                } else {
                    description = description + " " + ops.getParent().getCode() + " " + ops.getParent().getDescription() + " " + ops.getParent().getInclusion() + " ";
                }
                ops = ops.getParent();
            }
        }

        return description;
    }

    @Override
    public Map<Long, CpxOps> getAll(final String pCountryEn, final Integer pYear) {
        Map<Long, CpxOps> retOpsMap = ops4year.get(pYear);
        if(retOpsMap != null){
            return retOpsMap;
        }
        long timeStart = System.currentTimeMillis();

        int year = checkYear(pYear);
        Map<Long, CpxOps> opsList = new TreeMap<>();

        Map<Integer, Map<Long, CpxOps>> objList = new HashMap<>();
        //here will be got aop from DB
        Map<String, CpxOpsAop> aops = CpxOpsAopCatalog.instance().getAllAops(pCountryEn, pYear); 
        final String tableName = getTableName(CATALOG, pCountryEn, year);
        String sql = String.format("SELECT * FROM %s", tableName);
        /*  + " ORDER BY ICD_DEPTH, ICD_CODE */
        try ( PreparedStatement pstmt = prepareStatement(CATALOG, year, sql)) {
            if (pstmt != null) { //prepared statement is null if catalog does not exist!
                try ( ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        CpxOps obj = toCpxObject(rs);
                        if (obj != null) {
                            opsList.put(obj.getId(), obj);
//                            if(obj.isCompleteFl() ){
//                                CpxOpsAop aop = aops.get(obj.getCode());
//                                obj.addAop(aop);
//  
//                            }
                            int depth = obj.getOpsDepth();
                            Map<Long, CpxOps> list = objList.get(depth);
                            if (list == null) {
                                list = new HashMap<>();
                                objList.put(depth, list);
                            }
                            if (depth > 0) {
                                CpxOps parent = objList.get(depth - 1).get(obj.getParentId());
                                obj.setParent(parent);
                                parent.addChildren(obj);
                            }
                            objList.get(obj.getOpsDepth()).put(obj.getId(), obj);
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
            Logger.getLogger(CpxIcdCatalog.class.getName()).log(Level.SEVERE, null, ex);

        }

        // loop to fill Descriptions in SearchableDescription
        for (Map.Entry<Long, CpxOps> entry : opsList.entrySet()) {
            CpxOps obj = entry.getValue();

            obj.setSearchableDescription(initOpsChilderenDescription(obj));

        }
        long timeEnd = System.currentTimeMillis();

        Logger.getLogger(CpxOpsCatalog.class.getName()).log(Level.INFO, "Time elapsed while loading OPS catalog: " + (timeEnd - (double) timeStart) / 1000d + "s");

        Map<Long, CpxOps> map = objList.get(0);

        CpxOpsThesaurusCatalog.instance().getAllThesaurus(pCountryEn, pYear, opsList);


        if (map == null) {
            return new HashMap<>();
        }
        
        ops4year.put(year, map);
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
        String sql = "SELECT OPS_DESCRIPTION FROM %s WHERE OPS_CODE " + operator(code) + " ? ORDER BY OPS_CODE";
        return fetchSingleValue(sql, pCountryEn, pYear, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        final String tableName = getTableName(CATALOG, pCountryEn, pYear);
//        final String code = toParam(pCode);
//        String sql = String.format("SELECT OPS_DESCRIPTION FROM %s WHERE OPS_CODE " + operator(code) + " ? ORDER BY OPS_CODE", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, pYear).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    String result = rs.getString("OPS_DESCRIPTION");
//                    return result;
//                }
//                rs.close();
//            } finally {
//                if (pstmt != null) {
//                    pstmt.close();
//                }
//            }
//        } catch (SQLException ex) {
//            //Catalog does not exist, SQLite DB file was deleted?!
//            Logger.getLogger(CpxIcdCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return "";
    }

    @Override
    public CpxOps getNewObject() {
        return new CpxOps();
    }

}
