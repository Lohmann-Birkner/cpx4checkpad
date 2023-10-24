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
import de.lb.cpx.model.enums.SupplFeeTypeEn;
import de.lb.cpx.server.commonDB.model.CSupplementaryFee;
import de.lb.cpx.service.information.CpxCatalogOverview;
import de.lb.cpx.serviceutil.parser.CpxDateParser;
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
public abstract class AbstractCpxSupplementaryFeeCatalog extends AbstractCpxCatalog<CpxSupplementaryFee, CSupplementaryFee> {

    @Override
    public String createCatalogTable(final CpxCatalogOverview pCatalogOverview) {
        final String tableName = getTableName(pCatalogOverview);
        try ( Statement stmt = getCatalogDb(pCatalogOverview).createStatement()) {
            String sql = String.format("CREATE TABLE IF NOT EXISTS %s", tableName)
                    + "(ID                  INTEGER    PRIMARY KEY,"
                    + " CREATION_DATE       DATETIME, "
                    + " MODIFICATION_DATE   DATETIME, "
                    + " COUNTRY_EN          VARCHAR(25), "
                    + " SUPPL_TYPE_EN	     VARCHAR(255), "
                    + " SUPPL_HOS_IDENT	   VARCHAR(255), "
                    + " SUPPL_CW_VALUE	     DOUBLE, "
                    + " SUPPL_DEFINITION	   VARCHAR(1000), "
                    + " SUPPL_KEY	         VARCHAR(10), "
                    + " SUPPL_NEGOTIATED  	 TINYINT NOT NULL, "
                    + " SUPPL_OPS_CODE	     VARCHAR(25), "
                    + " SUPPL_VALID_FROM	   DATETIME, "
                    + " SUPPL_VALID_TO      DATETIME, "
                    + " SUPPL_VALUE	       DOUBLE, "
                    + " SUPPL_YEAR	         INT ) ";
            stmt.executeUpdate(sql);

            sql = String.format("CREATE INDEX IF NOT EXISTS IDX_%s ON %s (SUPPL_KEY)", pCatalogOverview.getCatalog(), tableName); //COUNTRY_EN
            stmt.executeUpdate(sql);

            stmt.close();
            return tableName;
        } catch (SQLException ex) {
            Logger.getLogger(CpxSupplementaryPeppFeeCatalog.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

//    /**
//     * Returns Dummy-Object if Supplementary Fee Key cannot be found in local
//     * SQLite-DB
//     *
//     * @param pCatalogType catalogtype
//     * @param pCode Supplementary Fee Code
//     * @param pCountryEn Country ("de", "en")
//     * @param pYear Year
//     * @return SupplementaryFee
//     */
//    public CpxSupplementaryFee getByCode(final CatalogTypeEn pCatalogType, final String pCode, final String pCountryEn, final Integer pYear) {
//        //throw new CpxIllegalArgumentException("Supplementary Fee is not neccessary unique, use findManyByCode method instead!");
//        List<CpxSupplementaryFee> list = findManyByCode(pCatalogType, pCode, pCountryEn, pYear);
//        if (list.isEmpty()) {
//            return getNewObject();
//        }
//        return list.get(0);
//    }
    @Override
    public void fillCatalog(final CpxCatalogOverview pCatalogOverview, final List<CSupplementaryFee> pList) throws SQLException {
        final String tableName = getTableName(pCatalogOverview);
        String sqlHead = String.format("INSERT INTO %s (ID, CREATION_DATE, MODIFICATION_DATE, COUNTRY_EN, SUPPL_TYPE_EN, SUPPL_HOS_IDENT, ", tableName)
                + " SUPPL_CW_VALUE, SUPPL_DEFINITION, SUPPL_KEY, SUPPL_NEGOTIATED, SUPPL_OPS_CODE, "
                + " SUPPL_VALID_FROM, SUPPL_VALID_TO, SUPPL_VALUE, SUPPL_YEAR) ";
        StringBuilder sql = null;
        int i = 0;
        boolean first = true;
        for (CSupplementaryFee suppFeeCatalog : pList) {
            i++;
            if (first) {
                sql = new StringBuilder(sqlHead);
                sql.append(" SELECT ");
                first = false;
            } else {
                sql.append(" UNION ALL SELECT ");
            }
            sql.append(suppFeeCatalog.getId() + ", ");
            sql.append(QStr(suppFeeCatalog.getCreationDate()) + ", ");
            sql.append(QStr(suppFeeCatalog.getModificationDate()) + ", ");
            sql.append(QStr(suppFeeCatalog.getCountryEn().name()) + ", ");
            sql.append(QStr(suppFeeCatalog.getSupplTypeEn().name()) + ", ");
            sql.append(QStr(suppFeeCatalog.getSupplHosIdent()) + ", ");
            sql.append(QStr(suppFeeCatalog.getSupplCwValue()) + ", ");
            sql.append(QStr(suppFeeCatalog.getSupplDefinition()) + ", ");
            sql.append(QStr(suppFeeCatalog.getSupplKey()) + ", ");
            sql.append(QStr(suppFeeCatalog.getSupplNegotiated()) + ", ");
            sql.append(QStr(suppFeeCatalog.getSupplOpsCode()) + ", ");
            sql.append(QStr(suppFeeCatalog.getSupplValidFrom()) + ", ");
            sql.append(QStr(suppFeeCatalog.getSupplValidTo()) + ", ");
            sql.append(QStr(suppFeeCatalog.getSupplValue()) + ", ");
            sql.append(QStr(suppFeeCatalog.getSupplYear()));
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
    public CpxSupplementaryFee toCpxObject(final ResultSet rs) throws SQLException {
        CpxSupplementaryFee obj = getNewObject();
        if (rs == null) {
            return obj;
        }
        obj.setId(rs.getLong("ID"));
        //obj.setCreationDate(CpxDateParser.parseDateTime(rs.getString("CREATION_DATE")));
        //obj.setModificationDate(CpxDateParser.parseDateTime(rs.getString("MODIFICATION_DATE")));
        //obj.setCountryEn(CountryEn.valueOf(rs.getString("COUNTRY_EN")));
        obj.setSupplTypeEn(SupplFeeTypeEn.valueOf(rs.getString("SUPPL_TYPE_EN")));
        obj.setSupplHosIdent(rs.getString("SUPPL_HOS_IDENT"));
        obj.setSupplCwValue(rs.getDouble("SUPPL_CW_VALUE"));
        obj.setSupplDefinition(rs.getString("SUPPL_DEFINITION"));
        obj.setSupplKey(rs.getString("SUPPL_KEY"));
        obj.setSupplNegotiated(rs.getBoolean("SUPPL_NEGOTIATED"));
        obj.setSupplOpsCode(rs.getString("SUPPL_OPS_CODE"));
        obj.setSupplValidFrom(CpxDateParser.parseDateTime(rs.getString("SUPPL_VALID_FROM")));
        obj.setSupplValidTo(CpxDateParser.parseDateTime(rs.getString("SUPPL_VALID_TO")));
        obj.setSupplValue(rs.getDouble("SUPPL_VALUE"));
        obj.setSupplYear(rs.getInt("SUPPL_YEAR"));
        return obj;
    }

//    /**
//     * Returns Dummy-Object if Supplementary Fee Key cannot be found in local
//     * SQLite-DB
//     *
//     * @param pCatalogType catalogtype
//     * @param pId ID
//     * @param pCountryEn Country ("de", "en")
//     * @param pYear Year
//     * @return SupplementaryFee
//     */
//    public final CpxSupplementaryFee getById(final CatalogTypeEn pCatalogType, final Long pId, final String pCountryEn, final Integer pYear) {
//        String sql = "SELECT * FROM %s WHERE ID = ?";
//        return fetchSingle(sql, pCatalogType, pCountryEn, pYear, (pstmt) -> {
//            pstmt.setLong(1, pId);
//        });
//
////        int year = checkYear(pYear);
////        CpxSupplementaryFee obj = getNewObject();
////        long id = (pId == null) ? 0L : pId;
////        final String tableName = getTableName(pCatalogType, pCountryEn, year);
////        String sql = String.format("SELECT * FROM %s WHERE ID = ?", tableName);
////        try ( PreparedStatement pstmt = getCatalogDb(pCatalogType, year).prepareStatement(sql)) {
////            pstmt.setLong(1, id);
////            try ( ResultSet rs = pstmt.executeQuery()) {
////                while (rs.next()) {
////                    obj = toCpxObject(rs);
////                    break;
////                }
////                rs.close();
////            } finally {
////                if (pstmt != null) {
////                    pstmt.close();
////                }
////            }
////        } catch (SQLException ex) {
////            //Catalog does not exist, SQLite DB file was deleted?!
////            LOG.log(Level.SEVERE, null, ex);
////        }
////        return obj;
//    }
    /**
     * Returns Dummy-Object if Supplementary Fee Key cannot be found in local
     * SQLite-DB
     *
     * @param pCode Supplementary Fee Key
     * @param pCountryEn Country ("de", "en")
     * @param pYear Year
     * @return SupplementaryFee
     */
    @Override
    public final List<CpxSupplementaryFee> findManyByCode(final String pCode, final String pCountryEn, final Integer pYear) {
        final String code = toParam(pCode);
        String sql = "SELECT * FROM %s WHERE SUPPL_KEY " + operator(code) + " ? ORDER BY SUPPL_KEY";
        return fetchMany(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = checkYear(pYear);
//        List<CpxSupplementaryFee> objList = new ArrayList<>();
//        final String tableName = getTableName(pCatalogType, pCountryEn, year);
//        final String code = toParam(pCode);
//        String sql = String.format("SELECT * FROM %s WHERE SUPPL_KEY " + operator(code) + " ? ORDER BY SUPPL_KEY", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(pCatalogType, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    CpxSupplementaryFee obj = toCpxObject(rs);
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
//            Logger.getLogger(CpxSupplementaryPeppFeeCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return objList;
    }

    @Override
    public CpxSupplementaryFee getNewObject() {
        return new CpxSupplementaryFee();
    }

}
