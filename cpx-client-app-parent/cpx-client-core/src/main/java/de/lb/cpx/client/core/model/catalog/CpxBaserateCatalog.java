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
import de.lb.cpx.server.commonDB.model.CBaserate;
import de.lb.cpx.service.information.CatalogTypeEn;
import de.lb.cpx.service.information.CpxCatalogOverview;
import de.lb.cpx.serviceutil.parser.CpxDateParser;
import de.lb.cpx.shared.lang.Lang;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dirk Niemeier
 */
public class CpxBaserateCatalog extends AbstractCpxNonAnnualCatalog<CpxBaserate, CBaserate> {

    private static CpxBaserateCatalog instance = null;
    public static final CatalogTypeEn CATALOG = CatalogTypeEn.BASERATE;
    private static final Logger LOG = Logger.getLogger(CpxBaserateCatalog.class.getName());

    public static synchronized CpxBaserateCatalog instance() {
        if (instance == null) {
            instance = new CpxBaserateCatalog();
        }
        return instance;
    }
    //Logger log = Logger.getLogger(getClass().getSimpleName());

    private CpxBaserateCatalog() {

    }

    @Override
    public void fillCatalog(final CpxCatalogOverview pCatalogOverview, final List<CBaserate> pList) throws SQLException {
        final String tableName = getTableName(pCatalogOverview);
        String sqlHead = String.format("INSERT INTO %s(ID, CREATION_DATE, MODIFICATION_DATE, COUNTRY_EN, BASE_HOS_IDENT, BASE_FEE_KEY, BASE_FEE_VALUE, BASE_VALID_FROM, BASE_VALID_TO, BASE_LOS) ", tableName);
        StringBuilder sql = null;
        int i = 0;
        boolean first = true;
        for (CBaserate baserate : pList) {
            i++;
            if (first) {
                sql = new StringBuilder(sqlHead);
                sql.append(" SELECT ");
                first = false;
            } else {
                sql.append(" UNION ALL SELECT ");
            }
            sql.append(baserate.getId() + ", ");
            sql.append(QStr(baserate.getCreationDate()) + ", ");
            sql.append(QStr(baserate.getModificationDate()) + ", ");
            sql.append(QStr(baserate.getCountryEn().name()) + ", ");
            sql.append(QStr(baserate.getBaseHosIdent()) + ", ");
            sql.append(QStr(baserate.getBaseFeeKey()) + ", ");
            sql.append(QStr(baserate.getBaseFeeValue()) + ", ");
            sql.append(QStr(baserate.getBaseValidFrom()) + ", ");
            sql.append(QStr(baserate.getBaseValidTo()) + ", ");
            sql.append(QStr(baserate.getBaseLos()));
            //pstmt.addBbaserateh();
            //break;
            if (i % 500 == 0 || i == pList.size()) {
                try (PreparedStatement pstmt = getCatalogDb(pCatalogOverview).prepareStatement(sql.toString())) {
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
        try (Statement stmt = getCatalogDb(pCatalogOverview).createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS " + tableName
                    + "(ID                 INTEGER    PRIMARY KEY,"
                    + " CREATION_DATE      DATETIME, "
                    + " MODIFICATION_DATE  DATETIME, "
                    + " COUNTRY_EN         VARCHAR(25), "
                    + " BASE_HOS_IDENT     VARCHAR(10), "
                    + " BASE_FEE_KEY       VARCHAR(20), "
                    + " BASE_FEE_VALUE     DOUBLE, "
                    + " BASE_VALID_FROM    DATETIME, "
                    + " BASE_VALID_TO      DATETIME, "
                    + " BASE_LOS           DOUBLE ) ";
            stmt.executeUpdate(sql);

            sql = String.format("CREATE INDEX IF NOT EXISTS IDX_BASERATE ON %s (BASE_HOS_IDENT)", tableName); //COUNTRY_EN
            stmt.executeUpdate(sql);

            stmt.close();
            return tableName;
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return "";
        }
    }

    @Override
    public CpxBaserate toCpxObject(final ResultSet rs) throws SQLException {
        CpxBaserate obj = getNewObject();
        if (rs == null) {
            return obj;
        }
        obj.setId(rs.getLong("ID"));
        //obj.setCreationDate(CpxDateParser.parseDateTime(rs.getString("CREATION_DATE")));
        //obj.setModificationDate(CpxDateParser.parseDateTime(rs.getString("MODIFICATION_DATE")));
        //obj.setCountryEn(CountryEn.valueOf(rs.getString("COUNTRY_EN")));
        obj.setBaseHosIdent(rs.getString("BASE_HOS_IDENT"));
        obj.setBaseFeeKey(rs.getString("BASE_FEE_KEY"));
        obj.setBaseFeeValue(rs.getDouble("BASE_FEE_VALUE"));
        obj.setBaseValidFrom(CpxDateParser.parseDateTime(rs.getString("BASE_VALID_FROM")));
        obj.setBaseValidTo(CpxDateParser.parseDateTime(rs.getString("BASE_VALID_TO")));
        obj.setBaseLos(rs.getDouble("BASE_LOS"));
        return obj;
    }

//    /**
//     * Returns Dummy-Object if Hospital Identifier cannot be found in local
//     * SQLite-DB
//     *
//     * @param pHosIdent Hospital Identifier
//     * @param pCountryEn Country ("de", "en")
//     * @return Baserate
//     */
//    @Override
//    public CpxBaserate getByCode(final String pHosIdent, final String pCountryEn) {
//        throw new UnsupportedOperationException("Not available for baserates");
//        //List<CpxBaserate> list = findManyByCode(pBaserateCode, pCountryEn);
//        //if (list.isEmpty()) {
//        //  return getNewObject();
//        //}
//        //return list.get(0);
//    }
//    /**
//     * Returns Dummy-Object if Baserate Identifier cannot be found in local
//     * SQLite-DB
//     *
//     * @param pId ID
//     * @param pCountryEn Country ("de", "en")
//     * @return Baserate
//     */
//    @Override
//    public CpxBaserate getById(final Long pId, final String pCountryEn) {
//        int year = 0;
//        CpxBaserate obj = getNewObject();
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
    /**
     * Returns Dummy-Object if Hospital Identifier cannot be found in local
     * SQLite-DB
     *
     * @param pHosIdent Hospital Identifier
     * @param pCountryEn Country ("de", "en")
     * @return Baserate
     */
    @Override
    public List<CpxBaserate> findManyByCode(final String pHosIdent, final String pCountryEn) {
        final String code = toParam(pHosIdent);
        String sql = "SELECT * FROM %s WHERE BASE_HOS_IDENT " + operator(code) + " ? ORDER BY BASE_VALID_FROM, BASE_VALID_TO";
        return fetchMany(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        List<CpxBaserate> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam(pHosIdent);
//        String sql = String.format("SELECT * FROM %s WHERE BASE_HOS_IDENT " + operator(code) + " ? ORDER BY BASE_VALID_FROM, BASE_VALID_TO", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    CpxBaserate obj = toCpxObject(rs);
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

    @Override
    public CatalogTypeEn getCatalogType() {
        return CATALOG;
    }

    /**
     * returns Catalog data for the base rate by admDate, hosptial ident and
     * coutnry returns dummie value if no result is found, in that value the
     * baserate is 0.0!
     *
     * @param pHosIdent unqiue hospital identifier
     * @param admDate adm date of the case
     * @param pCountryEn country where the base rate is valid in
     * @return catalog data for the base rate
     */
    public CpxBaserate findDrgBaserate(String pHosIdent, Date admDate, String pCountryEn) {
        final String code = toParam(pHosIdent);
        String sql = "SELECT * FROM %s WHERE BASE_HOS_IDENT " + operator(code) + " ? AND DATETIME(?) between BASE_VALID_FROM and BASE_VALID_TO AND BASE_FEE_KEY = 70000000";
        return fetchSingle(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
            pstmt.setString(2, toParam(Lang.toIsoDate(admDate)));
        });

//        int year = 0;
//        List<CpxBaserate> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam(pHosIdent);
//        String sql = String.format("SELECT * FROM %s WHERE BASE_HOS_IDENT " + operator(code) + " ? AND DATETIME(?) between BASE_VALID_FROM and BASE_VALID_TO AND BASE_FEE_KEY = 70000000", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            pstmt.setString(2, toParam(Lang.toIsoDate(admDate)));
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    CpxBaserate obj = toCpxObject(rs);
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
//        if (objList.size() >= 1) {
//            return objList.get(0);
//        }
//        LOG.log(Level.WARNING, "No drg baserate found for hospital ident " + pHosIdent + " and admission date " + Lang.toDateTime(admDate));
//        CpxBaserate br = getNewObject();
//        br.setBaseFeeValue(0.0);
//        return getNewObject();
    }

    /**
     * returns Catalog data for the base rate by admDate, hosptial ident and
     * coutnry returns dummie value if no result is found, in that value the
     * baserate is 0.0!
     *
     * @param pHosIdent unqiue hospital identifier
     * @param admDate adm date of the case
     * @param pCountryEn country where the base rate is valid in
     * @return catalog data for the base rate
     */
    public CpxBaserate findCareBaserate(String pHosIdent, Date admDate, String pCountryEn) {
        final String code = toParam(pHosIdent);
        String sql = "SELECT * FROM %s WHERE BASE_HOS_IDENT " + operator(code) + " ? AND DATETIME(?) between BASE_VALID_FROM and BASE_VALID_TO AND BASE_FEE_KEY = 74000000";
        return fetchSingle(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
            pstmt.setString(2, toParam(Lang.toIsoDate(admDate)));
        });
    }
    
        /**
     * returns Catalog data for the base rate for DRG Care  by hosptial ident
     * and coutnry
     *
     * @param pHosIdent unqiue hospital identifier
     * @param pCountryEn country where the base rate is valid in
     * @return catalog data for the base rate
     */
    public List<CpxBaserate> findDrgCareBaserates4HosIdent(String pHosIdent, String pCountryEn) {
        return findBaserates4HosIdent(pHosIdent, pCountryEn, "74000000");
    }

    private List<CpxBaserate> findBaserates4HosIdent(String pHosIdent, String pCountryEn, String pFeeKey){
        final String code = toParam(pHosIdent);
        String sql = "SELECT * FROM %s WHERE BASE_HOS_IDENT " + operator(code) + " " + "?  AND BASE_FEE_KEY = '" + pFeeKey + "'";
         List<CpxBaserate> ret =  fetchMany(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });
         if(ret != null){
             Collections.sort(ret);
         }
         return ret;
    }
    /**
     * returns Catalog data for the base rate for Pepp Case by hosptial ident
     * and coutnry
     *
     * @param pHosIdent unqiue hospital identifier
     * @param pCountryEn country where the base rate is valid in
     * @return catalog data for the base rate
     */
    public List<CpxBaserate> findPeppBaserate4HosIdent(String pHosIdent, String pCountryEn) {
        return findBaserates4HosIdent(pHosIdent, pCountryEn, "C1000000");

//        int year = 0;
//        List<CpxBaserate> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam(pHosIdent);
//        String sql = String.format("SELECT * FROM %s WHERE BASE_HOS_IDENT " + operator(code) + " "
//                + "?  AND BASE_FEE_KEY = 'C1000000'", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    CpxBaserate obj = toCpxObject(rs);
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
//            LOG.log(Level.SEVERE, null, ex);
//        }
//
//        return objList;
    }

    public Double findDrgBaserateFeeValue(String pHosIdent, Date admDate, String pCountryEn) {
        final String code = toParam(pHosIdent);
        String sql = "SELECT BASE_FEE_VALUE FROM %s WHERE BASE_HOS_IDENT " + operator(code) + " ? AND DATETIME(?) between BASE_VALID_FROM and BASE_VALID_TO AND BASE_FEE_KEY = 70000000";
        final Double value = fetchSingleValue(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
            pstmt.setString(2, toParam(Lang.toIsoDate(admDate)));
        });

        if (value == null) {
            LOG.log(Level.WARNING, "No drg baserate fee value found for hospital ident {0} and admission date {1}", new Object[]{pHosIdent, Lang.toDateTime(admDate)});
            return 0.0D;
        } else {
            return value;
        }
    

//        int year = 0;
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam(pHosIdent);
//        String sql = String.format("SELECT BASE_FEE_VALUE FROM %s WHERE BASE_HOS_IDENT " + operator(code) + " ? AND DATETIME(?) between BASE_VALID_FROM and BASE_VALID_TO AND BASE_FEE_KEY = 70000000", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            pstmt.setString(2, toParam(Lang.toIsoDate(admDate)));
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    double res = rs.getDouble("BASE_FEE_VALUE");
//                    return res;
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
//        LOG.log(Level.WARNING, "No drg baserate fee value found for hospital ident " + pHosIdent + " and admission date " + Lang.toDateTime(admDate));
//        return 0.0D;
    }



    public Double findCareBaserateFeeValue(String pHosIdent, Date admDate, String pCountryEn) {
        final String code = toParam(pHosIdent);
        String sql = "SELECT BASE_FEE_VALUE FROM %s WHERE BASE_HOS_IDENT " + operator(code) + " ? AND DATETIME(?) between BASE_VALID_FROM and BASE_VALID_TO AND BASE_FEE_KEY = 74000000";
        final Double value = fetchSingleValue(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
            pstmt.setString(2, toParam(Lang.toIsoDate(admDate)));
        });

        if (value == null) {
            LOG.log(Level.WARNING, "No drg baserate fee value found for hospital ident {0} and admission date {1}", new Object[]{pHosIdent, Lang.toDateTime(admDate)});
            return 0.0D;
        } else {
            return value;
        }
    }

    /**
     *
     * @param pHosIdent HostIdent
     * @param admDate Admission Date
     * @param pCountryEn Country
     * @return BASE_FEE_VALUE
     */
    public Double findPeppBaserateFeeValue(String pHosIdent, Date admDate, String pCountryEn) {
        final String code = toParam(pHosIdent);
        String sql = "SELECT BASE_FEE_VALUE FROM %s WHERE BASE_HOS_IDENT " + operator(code) + " ? AND DATETIME(?) between BASE_VALID_FROM and BASE_VALID_TO AND BASE_FEE_KEY = 'C1000000'";
        final Double value = fetchSingleValue(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
            pstmt.setString(2, toParam(Lang.toIsoDate(admDate)));
        });

        if (value == null) {
            LOG.log(Level.WARNING, "No pepp baserate fee value found for hospital ident {0} and admission date {1}", new Object[]{pHosIdent, Lang.toDateTime(admDate)});
            return 0.0D;
        } else {
            return value;
        }

//        int year = 0;
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam(pHosIdent);
//        String sql = String.format("SELECT BASE_FEE_VALUE FROM %s WHERE BASE_HOS_IDENT " + operator(code) + " ? AND DATETIME(?) between BASE_VALID_FROM and BASE_VALID_TO AND BASE_FEE_KEY = 'C1000000'", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            pstmt.setString(2, toParam(Lang.toIsoDate(admDate)));
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    double res = rs.getDouble("BASE_FEE_VALUE");
//                    return res;
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
//        LOG.log(Level.WARNING, "No pepp baserate fee value found for hospital ident " + pHosIdent + " and admission date " + Lang.toDateTime(admDate));
//        return 0.0D;
    }

    @Override
    public CpxBaserate getNewObject() {
        return new CpxBaserate();
    }

}
