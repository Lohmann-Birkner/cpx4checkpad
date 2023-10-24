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
import de.lb.cpx.server.commonDB.model.CDepartment;
import de.lb.cpx.service.information.CatalogTypeEn;
import de.lb.cpx.service.information.CpxCatalogOverview;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dirk Niemeier
 */
public class CpxDepartmentCatalog extends AbstractCpxNonAnnualCatalog<CpxDepartment, CDepartment> {

    private static CpxDepartmentCatalog instance = null;
    public static final CatalogTypeEn CATALOG = CatalogTypeEn.DEPARTMENT;

    public static synchronized CpxDepartmentCatalog instance() {
        if (instance == null) {
            instance = new CpxDepartmentCatalog();
        }
        return instance;
    }
    //Logger log = Logger.getLogger(getClass().getSimpleName());

    private CpxDepartmentCatalog() {

    }

    @Override
    public void fillCatalog(final CpxCatalogOverview pCatalogOverview, final List<CDepartment> pList) throws SQLException {
        final String tableName = getTableName(pCatalogOverview);
        String sqlHead = String.format("INSERT INTO %s(ID, CREATION_DATE, MODIFICATION_DATE, COUNTRY_EN, DEP_KEY_301, DEP_DESCRIPTION_301) ", tableName);
        StringBuilder sql = null;
        int i = 0;
        boolean first = true;
        for (CDepartment department : pList) {
            i++;
            if (first) {
                sql = new StringBuilder(sqlHead);
                sql.append(" SELECT ");
                first = false;
            } else {
                sql.append(" UNION ALL SELECT ");
            }
            sql.append(department.getId() + ", ");
            sql.append(QStr(department.getCreationDate()) + ", ");
            sql.append(QStr(department.getModificationDate()) + ", ");
            sql.append(QStr(department.getCountryEn().name()) + ", ");
            sql.append(QStr(department.getDepKey301()) + ", ");
            sql.append(QStr(department.getDepDescription301()));
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
                    + " DEP_KEY_301          VARCHAR(10), "
                    + " DEP_DESCRIPTION_301   VARCHAR(255) ) ";
            stmt.executeUpdate(sql);

            sql = String.format("CREATE INDEX IF NOT EXISTS IDX_DEPARTMENT ON %s (DEP_KEY_301)", tableName); //COUNTRY_EN
            stmt.executeUpdate(sql);

            stmt.close();
            return tableName;
        } catch (SQLException ex) {
            Logger.getLogger(CpxDepartmentCatalog.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    @Override
    public CpxDepartment toCpxObject(final ResultSet rs) throws SQLException {
        CpxDepartment obj = getNewObject();
        if (rs == null) {
            return obj;
        }
        obj.setId(rs.getLong("ID"));
        //obj.setCreationDate(CpxDateParser.parseDateTime(rs.getString("CREATION_DATE")));
        //obj.setModificationDate(CpxDateParser.parseDateTime(rs.getString("MODIFICATION_DATE")));
        //obj.setCountryEn(CountryEn.valueOf(rs.getString("COUNTRY_EN")));
        obj.setDepKey301(rs.getString("DEP_KEY_301"));
        obj.setDepDescription301(rs.getString("DEP_DESCRIPTION_301"));
        return obj;
    }

//    /**
//     * Returns Dummy-Object if Department Identifier cannot be found in local
//     * SQLite-DB
//     *
//     * @param pDepShort Department Identifier (IKZ)
//     * @param pCountryEn Country ("de", "en")
//     * @return Department
//     */
//    @Override
//    public CpxDepartment getByCode(final String pDepShort, final String pCountryEn) {
//        List<CpxDepartment> list = findManyByCode(pDepShort, pCountryEn);
//        if (list.isEmpty()) {
//            return getNewObject();
//        }
//        return list.get(0);
//    }
//    /**
//     * Returns Dummy-Object if Department Identifier cannot be found in local
//     * SQLite-DB
//     *
//     * @param pId ID
//     * @param pCountryEn Country ("de", "en")
//     * @return Department
//     */
//    @Override
//    public CpxDepartment getById(final Long pId, final String pCountryEn) {
//        int year = 0;
//        CpxDepartment obj = getNewObject();
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
//            Logger.getLogger(CpxDepartmentCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return obj;
//    }
    /**
     * Returns Dummy-Object if Department Identifier cannot be found in local
     * SQLite-DB
     *
     * @param pDepKey301 Department Identifier (IKZ)
     * @param pCountryEn Country ("de", "en")
     * @return Department
     */
    @Override
    public List<CpxDepartment> findManyByCode(final String pDepKey301, final String pCountryEn) {
        final String code = toParam(pDepKey301);
        String sql = "SELECT * FROM %s WHERE DEP_KEY_301" + operator(code) + " ? ORDER BY DEP_KEY_301";
        return fetchMany(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        List<CpxDepartment> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam(pDepShort);
//        String sql = String.format("SELECT * FROM %s WHERE DEP_KEY_301" + operator(code) + " ? ORDER BY DEP_KEY_301", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    CpxDepartment obj = toCpxObject(rs);
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
//            Logger.getLogger(CpxDepartmentCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return objList;
    }

    @Override
    public CatalogTypeEn getCatalogType() {
        return CATALOG;
    }

    public List<String> getBestMatches(String userText, String pCountryEn) {
        userText = userText.toLowerCase();
        if (userText.contains(":")) {
            userText = userText.split(":")[0];
        }
        final String code = "%" + toParam(userText) + "%";
        String sql = "SELECT DEP_KEY_301, DEP_DESCRIPTION_301 FROM %s WHERE DEP_KEY_301 " + operator(code) + " ? OR  LOWER(DEP_DESCRIPTION_301) " + operator(code) + " ? ORDER BY DEP_KEY_301";
        return fetchManyValues(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
            pstmt.setString(2, code);
        }, (rs) -> {
            String obj = "";
            String dep_key = rs.getString("DEP_KEY_301");
            String dep_desc = rs.getString("DEP_DESCRIPTION_301");
            obj = obj.concat(dep_key + " : " + dep_desc);
            return obj;
        });

//        userText = userText.toLowerCase();
//        if (userText.contains(":")) {
//            userText = userText.split(":")[0];
//        }
//        int year = 0;
//        List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = "%" + toParam(userText) + "%";
//        String sql = String.format("SELECT DEP_KEY_301, DEP_DESCRIPTION_301 FROM %s WHERE DEP_KEY_301 " + operator(code) + " ? OR  LOWER(DEP_DESCRIPTION_301) " + operator(code) + " ? ORDER BY DEP_KEY_301", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            pstmt.setString(2, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    String obj = "";
//                    String dep_key = rs.getString("DEP_KEY_301");
//                    String dep_desc = rs.getString("DEP_DESCRIPTION_301");
//                    obj = obj.concat(dep_key + " : " + dep_desc);
//                    if (!obj.isEmpty()) {
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
//            Logger.getLogger(CpxDepartmentCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return objList;
    }

    public String getDepartmentNameWithDesc(String pDepKey301, String pCountryEn) {
        final String code = toParam(pDepKey301);
        String sql = "SELECT DEP_KEY_301, DEP_DESCRIPTION_301 FROM %s WHERE DEP_KEY_301 = ? ";
        return fetchSingleValue(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        }, (rs) -> {
            String dep_key = rs.getString("DEP_KEY_301");
            String dep_desc = rs.getString("DEP_DESCRIPTION_301");
            String obj = dep_key + " : " + dep_desc;
            return obj;
        });

//        int year = 0;
//        String obj = "";
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT DEP_KEY_301, DEP_DESCRIPTION_301 FROM %s WHERE DEP_KEY_301 = ? ", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, toParam(pDepKey301));
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//
//                    String dep_key = rs.getString("DEP_KEY_301");
//                    String dep_desc = rs.getString("DEP_DESCRIPTION_301");
//                    obj = obj.concat(dep_key + " : " + dep_desc);
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
//            Logger.getLogger(CpxDepartmentCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return obj;
    }

    public boolean hasEntry(String departmentShort, String pCountryEn) {
        final String code = toParam(departmentShort);
        String sql = "SELECT DEP_KEY_301 FROM %s WHERE DEP_KEY_301 = ? ";
        return super.hasEntry(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT DEP_KEY_301 FROM %s WHERE  DEP_KEY_301 = ? ", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//
//            pstmt.setString(1, toParam(departmentShort));
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                boolean val = rs.next();
//                rs.close();
//                return val;
//            } finally {
//                if (pstmt != null) {
//                    pstmt.close();
//                }
//            }
//        } catch (SQLException ex) {
//            //Catalog does not exist, SQLite DB file was deleted?!
//            Logger.getLogger(CpxHospitalCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return false;
    }

    public boolean hasEntryName(String pDepKey301, String departmentName, String pCountryEn) {
        final String code1 = toParam(pDepKey301);
        final String code2 = toParam(departmentName);
        String sql = "SELECT DEP_KEY_301,DEP_DESCRIPTION_301 FROM %s WHERE DEP_KEY_301=? AND DEP_DESCRIPTION_301=?";
        return super.hasEntry(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code1);
            pstmt.setString(2, code2);
        });

//        int year = 0;
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT DEP_KEY_301,DEP_DESCRIPTION_301 FROM %s WHERE DEP_KEY_301=? AND DEP_DESCRIPTION_301=?", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//
//            pstmt.setString(1, toParam(pDepKey301));
//            pstmt.setString(2, toParam(departmentName));
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                boolean val = rs.next();
//                rs.close();
//                return val;
//            } finally {
//                if (pstmt != null) {
//                    pstmt.close();
//                }
//            }
//        } catch (SQLException ex) {
//            //Catalog does not exist, SQLite DB file was deleted?!
//            Logger.getLogger(CpxHospitalCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return false;
    }

    /**
     *
     * @param pDepKey301 Department key §301.
     * @param pCountryEn Country ("de", "en")
     * @return Department name : DEP_DESCRIPTION_301
     */
    //CPX-677 RSH 26.10.2017 
    public String findDepNameByDepKey301(String pDepKey301, String pCountryEn) {
        final String code = toParam(pDepKey301);
        String sql = "SELECT DEP_DESCRIPTION_301 FROM %s WHERE DEP_KEY_301 " + operator(code) + " ?";
        return fetchSingleValue(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        String obj = null;
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam(pDepKey301);
//        String sql = String.format("SELECT DEP_DESCRIPTION_301 FROM %s WHERE DEP_KEY_301 " + operator(code) + " ?", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
////            pstmt.setString(2, toParam(insc_type));
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    obj = rs.getString("DEP_DESCRIPTION_301");
//                }
//                rs.close();
//            } finally {
//                if (pstmt != null) {
//                    pstmt.close();
//                }
//            }
//        } catch (SQLException ex) {
//            //Catalog does not exist, SQLite DB file was deleted?!
//            Logger.getLogger(CpxInsuranceCompanyCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return obj;
    }

    @Override
    public CpxDepartment getNewObject() {
        return new CpxDepartment();
    }

}
