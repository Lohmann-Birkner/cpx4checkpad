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
import de.lb.cpx.server.commonDB.model.CPzn;
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
public class CpxPznCatalog extends AbstractCpxNonAnnualCatalog<CpxPzn, CPzn> {

    private static CpxPznCatalog instance = null;
    public static final CatalogTypeEn CATALOG = CatalogTypeEn.PZN;

    public static synchronized CpxPznCatalog instance() {
        if (instance == null) {
            instance = new CpxPznCatalog();
        }
        return instance;
    }
    //Logger log = Logger.getLogger(getClass().getSimpleName());

    private CpxPznCatalog() {

    }

    @Override
    public void fillCatalog(final CpxCatalogOverview pCatalogOverview, final List<CPzn> pList) throws SQLException {
        final String tableName = getTableName(pCatalogOverview);
        String sqlHead = String.format("INSERT INTO %s(ID, CREATION_DATE, MODIFICATION_DATE, COUNTRY_EN, PZN_IDENT, PZN_NORMSIZE, PZN_DESC) ", tableName);
        StringBuilder sql = null;
        //String sql = "INSERT INTO ICD_CATALOG(ID, CREATION_DATE, MODIFICATION_DATE, COUNTRY_EN, ICD_CODE, ICD_IS_COMPLETE_FL, ICD_PARENT_ID, ICD_YEAR, ICD_DESCRIPTION, ICD_EXCLUSION, ICD_INCLUSION, ICD_NOTE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int i = 0;
        boolean first = true;
        for (CPzn pzn : pList) {
            i++;
            if (first) {
                sql = new StringBuilder(sqlHead);
                sql.append(" SELECT ");
                first = false;
            } else {
                sql.append(" UNION ALL SELECT ");
            }
            sql.append(pzn.getId() + ", ");
            sql.append(QStr(pzn.getCreationDate()) + ", ");
            sql.append(QStr(pzn.getModificationDate()) + ", ");
            sql.append(QStr(pzn.getCountryEn().name()) + ", ");
            sql.append(QStr(pzn.getPznIdent()) + ", ");
            sql.append(QStr(pzn.getPznNormSize()) + ", ");
            sql.append(QStr(pzn.getPznDesc()));
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
                    + " PZN_IDENT          VARCHAR(20), "
                    + " PZN_NORMSIZE       VARCHAR(20), "
                    + " PZN_DESC           VARCHAR(255) ) ";
            stmt.executeUpdate(sql);

            sql = String.format("CREATE INDEX IF NOT EXISTS IDX_PZN ON %s (PZN_IDENT)", tableName); //COUNTRY_EN
            stmt.executeUpdate(sql);

            stmt.close();
            return tableName;
        } catch (SQLException ex) {
            Logger.getLogger(CpxPznCatalog.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    @Override
    public CpxPzn toCpxObject(final ResultSet rs) throws SQLException {
        CpxPzn obj = getNewObject();
        if (rs == null) {
            return obj;
        }
        obj.setId(rs.getLong("ID"));
        //obj.setCreationDate(CpxDateParser.parseDateTime(rs.getString("CREATION_DATE")));
        //obj.setModificationDate(CpxDateParser.parseDateTime(rs.getString("MODIFICATION_DATE")));
        //obj.setCountryEn(CountryEn.valueOf(rs.getString("COUNTRY_EN")));
        obj.setPznIdent(rs.getString("PZN_IDENT"));
        obj.setPznNormSize(rs.getString("PZN_NORMSIZE"));
        obj.setPznDesc(rs.getString("PZN_DESC"));
        return obj;
    }

//    /**
//     * Returns Dummy-Object if Pzn Identifier cannot be found in local SQLite-DB
//     *
//     * @param pPznIdent Pzn Identifier (IKZ)
//     * @param pCountryEn Country ("de", "en")
//     * @return Pzn
//     */
//    @Override
//    public CpxPzn getByCode(final String pPznIdent, final String pCountryEn) {
//        List<CpxPzn> list = findManyByCode(pPznIdent, pCountryEn);
//        if (list.isEmpty()) {
//            return getNewObject();
//        }
//        return list.get(0);
//    }
//    /**
//     * Returns Dummy-Object if Pzn Identifier cannot be found in local SQLite-DB
//     *
//     * @param pId ID
//     * @param pCountryEn Country ("de", "en")
//     * @return Pzn
//     */
//    @Override
//    public CpxPzn getById(final Long pId, final String pCountryEn) {
//        int year = 0;
//        CpxPzn obj = getNewObject();
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
//            Logger.getLogger(CpxPznCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return obj;
//    }
    /**
     * Returns Dummy-Object if Pzn Identifier cannot be found in local SQLite-DB
     *
     * @param pPznIdent Pzn Identifier
     * @param pCountryEn Country ("de", "en")
     * @return Pzn
     */
    @Override
    public List<CpxPzn> findManyByCode(final String pPznIdent, final String pCountryEn) {
        final String code = toParam(pPznIdent);
        String sql = "SELECT * FROM %s WHERE PZN_IDENT " + operator(code) + " ? ORDER BY PZN_IDENT";
        return fetchMany(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        List<CpxPzn> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam(pPznIdent);
//        String sql = String.format("SELECT * FROM %s WHERE PZN_IDENT " + operator(code) + " ? ORDER BY PZN_IDENT", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    CpxPzn obj = toCpxObject(rs);
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
//            Logger.getLogger(CpxPznCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return objList;
    }

    @Override
    public CatalogTypeEn getCatalogType() {
        return CATALOG;
    }

    @Override
    public CpxPzn getNewObject() {
        return new CpxPzn();
    }

}
