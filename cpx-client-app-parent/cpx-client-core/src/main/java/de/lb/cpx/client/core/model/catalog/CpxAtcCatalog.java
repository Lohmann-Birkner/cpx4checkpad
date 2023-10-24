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
import de.lb.cpx.server.commonDB.model.CAtc;
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
public class CpxAtcCatalog extends AbstractCpxNonAnnualCatalog<CpxAtc, CAtc> {

    private static CpxAtcCatalog instance = null;
    public static final CatalogTypeEn CATALOG = CatalogTypeEn.ATC;

    public static synchronized CpxAtcCatalog instance() {
        if (instance == null) {
            instance = new CpxAtcCatalog();
        }
        return instance;
    }
    //Logger log = Logger.getLogger(getClass().getSimpleName());

    private CpxAtcCatalog() {

    }

    @Override
    public void fillCatalog(final CpxCatalogOverview pCatalogOverview, final List<CAtc> pList) throws SQLException {
        final String tableName = getTableName(pCatalogOverview);
        String sqlHead = String.format("INSERT INTO %s (ID, CREATION_DATE, MODIFICATION_DATE, COUNTRY_EN, ATC_CODE1, ATC_CODE2, ATC_DESC) ", tableName);
        StringBuilder sql = null;
        int i = 0;
        boolean first = true;
        for (CAtc atc : pList) {
            i++;
            if (first) {
                sql = new StringBuilder(sqlHead);
                sql.append(" SELECT ");
                first = false;
            } else {
                sql.append(" UNION ALL SELECT ");
            }
            sql.append(atc.getId() + ", ");
            sql.append(QStr(atc.getCreationDate()) + ", ");
            sql.append(QStr(atc.getModificationDate()) + ", ");
            sql.append(QStr(atc.getCountryEn().name()) + ", ");
            sql.append(QStr(atc.getAtcCode1()) + ", ");
            sql.append(QStr(atc.getAtcCode2()) + ", ");
            sql.append(QStr(atc.getAtcDesc()));
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
                    + " ATC_CODE1          VARCHAR(30), "
                    + " ATC_CODE2          VARCHAR(30), "
                    + " ATC_DESC           VARCHAR(255) ) ";
            stmt.executeUpdate(sql);

            sql = String.format("CREATE INDEX IF NOT EXISTS IDX_ATC ON %s (ATC_CODE1)", tableName); //COUNTRY_EN
            stmt.executeUpdate(sql);

            stmt.close();
            return tableName;
        } catch (SQLException ex) {
            Logger.getLogger(CpxAtcCatalog.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    @Override
    public CpxAtc toCpxObject(final ResultSet rs) throws SQLException {
        CpxAtc obj = getNewObject();
        if (rs == null) {
            return obj;
        }
        obj.setId(rs.getLong("ID"));
        //obj.setCreationDate(CpxDateParser.parseDateTime(rs.getString("CREATION_DATE")));
        //obj.setModificationDate(CpxDateParser.parseDateTime(rs.getString("MODIFICATION_DATE")));
        //obj.setCountryEn(CountryEn.valueOf(rs.getString("COUNTRY_EN")));
        obj.setAtcCode1(rs.getString("ATC_CODE1"));
        obj.setAtcCode2(rs.getString("ATC_CODE2"));
        obj.setAtcDesc(rs.getString("ATC_DESC"));
        return obj;
    }

//    /**
//     * Returns Dummy-Object if Atc Identifier cannot be found in local SQLite-DB
//     *
//     * @param pAtcCode1 Atc Identifier
//     * @param pCountryEn Country ("de", "en")
//     * @return Atc
//     */
//    @Override
//    public CpxAtc getByCode(final String pAtcCode1, final String pCountryEn) {
//        List<CpxAtc> list = findManyByCode(pAtcCode1, pCountryEn);
//        if (list.isEmpty()) {
//            return getNewObject();
//        }
//        return list.get(0);
//    }
//    /**
//     * Returns Dummy-Object if Atc Identifier cannot be found in local SQLite-DB
//     *
//     * @param pId ID
//     * @param pCountryEn Country ("de", "en")
//     * @return Atc
//     */
//    @Override
//    public CpxAtc getById(final Long pId, final String pCountryEn) {
//        int year = 0;
//        CpxAtc obj = getNewObject();
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
//            Logger.getLogger(CpxAtcCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return obj;
//    }
    /**
     * Returns Dummy-Object if Atc Identifier cannot be found in local SQLite-DB
     *
     * @param pAtcCode1 Atc Identifier
     * @param pCountryEn Country ("de", "en")
     * @return Atc
     */
    @Override
    public List<CpxAtc> findManyByCode(final String pAtcCode1, final String pCountryEn) {
        final String code = toParam(pAtcCode1);
        String sql = "SELECT * FROM %s WHERE ATC_CODE1 " + operator(code) + " ? ORDER BY ATC_CODE1";
        return fetchMany(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        List<CpxAtc> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam(pAtcCode1);
//        String sql = String.format("SELECT * FROM %s WHERE ATC_CODE1 " + operator(code) + " ? ORDER BY ATC_CODE1", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    CpxAtc obj = toCpxObject(rs);
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
//
//        } catch (SQLException ex) {
//            //Catalog does not exist, SQLite DB file was deleted?!
//            Logger.getLogger(CpxAtcCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return objList;
    }

    @Override
    public CatalogTypeEn getCatalogType() {
        return CATALOG;
    }

    @Override
    public CpxAtc getNewObject() {
        return new CpxAtc();
    }

}
