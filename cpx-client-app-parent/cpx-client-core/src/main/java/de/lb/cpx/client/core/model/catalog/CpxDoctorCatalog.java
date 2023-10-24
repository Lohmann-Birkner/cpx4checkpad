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
import de.lb.cpx.server.commonDB.model.CDoctor;
import de.lb.cpx.service.information.CatalogTypeEn;
import de.lb.cpx.service.information.CpxCatalogOverview;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dirk Niemeier
 */
public class CpxDoctorCatalog extends AbstractCpxNonAnnualCatalog<CpxDoctor, CDoctor> {

    private static CpxDoctorCatalog instance = null;
    public static final CatalogTypeEn CATALOG = CatalogTypeEn.DOCTOR;

    public static synchronized CpxDoctorCatalog instance() {
        if (instance == null) {
            instance = new CpxDoctorCatalog();
        }
        return instance;
    }
    //Logger log = Logger.getLogger(getClass().getSimpleName());

    private CpxDoctorCatalog() {

    }

    @Override
    public void fillCatalog(final CpxCatalogOverview pCatalogOverview, final List<CDoctor> pList) throws SQLException {
        final String tableName = getTableName(pCatalogOverview);
        String sqlHead = String.format("INSERT INTO %s(ID, CREATION_DATE, MODIFICATION_DATE, COUNTRY_EN, DOC_IDENT) ", tableName);
        StringBuilder sql = null;
        int i = 0;
        boolean first = true;
        for (CDoctor doctor : pList) {
            i++;
            if (first) {
                sql = new StringBuilder(sqlHead);
                sql.append(" SELECT ");
                first = false;
            } else {
                sql.append(" UNION ALL SELECT ");
            }
            sql.append(doctor.getId() + ", ");
            sql.append(QStr(doctor.getCreationDate()) + ", ");
            sql.append(QStr(doctor.getModificationDate()) + ", ");
            sql.append(QStr(doctor.getCountryEn().name()) + ", ");
            sql.append(QStr(doctor.getDocIdent()));
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
                    + " DOC_IDENT          VARCHAR(10) ) ";
            stmt.executeUpdate(sql);

            sql = String.format("CREATE INDEX IF NOT EXISTS IDX_DOCTOR ON %s (DOC_IDENT)", tableName); //COUNTRY_EN
            stmt.executeUpdate(sql);

            stmt.close();
            return tableName;
        } catch (SQLException ex) {
            Logger.getLogger(CpxDoctorCatalog.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    @Override
    public CpxDoctor toCpxObject(final ResultSet rs) throws SQLException {
        CpxDoctor obj = getNewObject();
        if (rs == null) {
            return obj;
        }
        obj.setId(rs.getLong("ID"));
        //obj.setCreationDate(CpxDateParser.parseDateTime(rs.getString("CREATION_DATE")));
        //obj.setModificationDate(CpxDateParser.parseDateTime(rs.getString("MODIFICATION_DATE")));
        //obj.setCountryEn(CountryEn.valueOf(rs.getString("COUNTRY_EN")));
        obj.setDocIdent(rs.getString("DOC_IDENT"));
        return obj;
    }

//    /**
//     * Returns Dummy-Object if Doctor Identifier cannot be found in local
//     * SQLite-DB
//     *
//     * @param pDocIdent Doctor Identifier (IKZ)
//     * @param pCountryEn Country ("de", "en")
//     * @return Doctor
//     */
//    @Override
//    public CpxDoctor getByCode(final String pDocIdent, final String pCountryEn) {
//        List<CpxDoctor> list = findManyByCode(pDocIdent, pCountryEn);
//        if (list.isEmpty()) {
//            return getNewObject();
//        }
//        return list.get(0);
//    }
//    /**
//     * Returns Dummy-Object if Doctor Identifier cannot be found in local
//     * SQLite-DB
//     *
//     * @param pId ID
//     * @param pCountryEn Country ("de", "en")
//     * @return Doctor
//     */
//    @Override
//    public CpxDoctor getById(final Long pId, final String pCountryEn) {
//        int year = 0;
//        CpxDoctor obj = getNewObject();
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
//            Logger.getLogger(CpxDoctorCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return obj;
//    }
    /**
     * Returns Dummy-Object if Doctor Identifier cannot be found in local
     * SQLite-DB
     *
     * @param pDocIdent Doctor Identifier (IKZ)
     * @param pCountryEn Country ("de", "en")
     * @return Doctor
     */
    @Override
    public List<CpxDoctor> findManyByCode(final String pDocIdent, final String pCountryEn) {
        final String code = toParam(pDocIdent);
        String sql = "SELECT * FROM %s WHERE DOC_IDENT " + operator(code) + " ? ORDER BY DOC_IDENT";
        return fetchMany(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        List<CpxDoctor> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam(pDocIdent);
//        String sql = String.format("SELECT * FROM %s WHERE DOC_IDENT " + operator(code) + " ? ORDER BY DOC_IDENT", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    CpxDoctor obj = toCpxObject(rs);
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
//            Logger.getLogger(CpxDoctorCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return objList;
    }

    @Override
    public CatalogTypeEn getCatalogType() {
        return CATALOG;
    }

    public Collection<String> getBestMatchForDoctor(final String pDocIdent, final String pCountryEn) {
        final String code = toParam(pDocIdent) + "%";
        String sql = "SELECT DOC_IDENT FROM %s WHERE DOC_IDENT " + operator(code) + " ? ORDER BY DOC_IDENT";
        return fetchManyValues(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam(pDocIdent) + "%";
//        String sql = String.format("SELECT DOC_IDENT FROM %s WHERE DOC_IDENT " + operator(code) + " ? ORDER BY DOC_IDENT", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    String obj = rs.getString("DOC_IDENT");
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
//            Logger.getLogger(CpxDoctorCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return objList;
    }

    @Override
    public CpxDoctor getNewObject() {
        return new CpxDoctor();
    }

}
