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
import de.lb.cpx.server.commonDB.model.CHospital;
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
public class CpxHospitalCatalog extends AbstractCpxNonAnnualCatalog<CpxHospital, CHospital> {

    private static CpxHospitalCatalog instance = null;
    public static final CatalogTypeEn CATALOG = CatalogTypeEn.HOSPITAL;
    private static final Logger LOG = Logger.getLogger(CpxHospitalCatalog.class.getName());

    public static synchronized CpxHospitalCatalog instance() {
        if (instance == null) {
            instance = new CpxHospitalCatalog();
        }
        return instance;
    }
    //Logger log = Logger.getLogger(getClass().getSimpleName());

    private CpxHospitalCatalog() {

    }

    @Override
    public void fillCatalog(final CpxCatalogOverview pCatalogOverview, final List<CHospital> pList) throws SQLException {
        final String tableName = getTableName(pCatalogOverview);
        String sqlHead = String.format("INSERT INTO %s(ID, CREATION_DATE, MODIFICATION_DATE, COUNTRY_EN, HOS_IDENT, HOS_ADDRESS, HOS_NAME, HOS_ZIP_CODE, HOS_CITY, STATE_EN, HOS_COMMENT) ", tableName);
        StringBuilder sql = null;
        int i = 0;
        boolean first = true;
        for (CHospital hospital : pList) {
            i++;
            if (first) {
                sql = new StringBuilder(sqlHead);
                sql.append(" SELECT ");
                first = false;
            } else {
                sql.append(" UNION ALL SELECT ");
            }
            sql.append(hospital.getId() + ", ");
            sql.append(QStr(hospital.getCreationDate()) + ", ");
            sql.append(QStr(hospital.getModificationDate()) + ", ");
            sql.append(QStr(hospital.getCountryEn().name()) + ", ");
            sql.append(QStr(hospital.getHosIdent()) + ", ");
            sql.append(QStr(hospital.getHosAddress()) + ", ");
            sql.append(QStr(hospital.getHosName()) + ", ");
            sql.append(QStr(hospital.getHosZipCode()) + ", ");
            sql.append(QStr(hospital.getHosCity()) + ", ");
            sql.append(QStr((hospital.getStateEn() == null) ? "" : hospital.getStateEn().getViewId()) + ", ");
            sql.append(QStr(hospital.getHosComment()));
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
                    + " HOS_IDENT          VARCHAR(10), "
                    + " HOS_ADDRESS        VARCHAR(255), "
                    + " HOS_NAME           VARCHAR(255), "
                    + " HOS_ZIP_CODE       VARCHAR(15), "
                    + " HOS_CITY           VARCHAR(50), "
                    + " STATE_EN           VARCHAR(25), "
                    + " HOS_COMMENT        VARCHAR(255))";
            stmt.executeUpdate(sql);

            sql = String.format("CREATE INDEX IF NOT EXISTS IDX_HOSPITAL ON %s (HOS_IDENT)", tableName); //COUNTRY_EN
            stmt.executeUpdate(sql);

            stmt.close();
            return tableName;
        } catch (SQLException ex) {
            Logger.getLogger(CpxHospitalCatalog.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    @Override
    public CpxHospital toCpxObject(final ResultSet rs) throws SQLException {
        CpxHospital obj = getNewObject();
        if (rs == null) {
            return obj;
        }
        obj.setId(rs.getLong("ID"));
        //obj.setCreationDate(CpxDateParser.parseDateTime(rs.getString("CREATION_DATE")));
        //obj.setModificationDate(CpxDateParser.parseDateTime(rs.getString("MODIFICATION_DATE")));
        //obj.setCountryEn(CountryEn.valueOf(rs.getString("COUNTRY_EN")));
        obj.setHosIdent(rs.getString("HOS_IDENT"));
        obj.setHosAddress(rs.getString("HOS_ADDRESS"));
        obj.setHosName(rs.getString("HOS_NAME"));
        obj.setHosZipCode(rs.getString("HOS_ZIP_CODE"));
        obj.setHosCity(rs.getString("HOS_CITY"));
        try {
            obj.setStateEn(rs.getString("STATE_EN"));
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(CpxHospitalCatalog.class.getName()).log(Level.SEVERE, "Invalid state defined!", ex);
        }
        obj.setHosComment(rs.getString("HOS_COMMENT"));
        return obj;
    }

//    /**
//     * Returns Dummy-Object if Hospital Identifier cannot be found in local
//     * SQLite-DB
//     *
//     * @param pHosIdent Hospital Identifier (IKZ)
//     * @param pCountryEn Country ("de", "en")
//     * @return Hospital
//     */
//    @Override
//    public CpxHospital getByCode(final String pHosIdent, final String pCountryEn) {
//        List<CpxHospital> list = findManyByCode(pHosIdent, pCountryEn);
//        if (list.isEmpty()) {
//            return getNewObject();
//        }
//        return list.get(0);
//    }
    // RSH 07092017  CPX-628
    /**
     * Returns Dummy-Object if Hospital Identifier cannot be found in local
     * SQLite-DB
     *
     * @param pHosName Hospital Name
     * @param pCountryEn Country ("de", "en")
     * @return Hospital
     */
    public CpxHospital getByName(final String pHosName, final String pCountryEn) {
        return getSingleObject(findManyByName(pHosName, pCountryEn));
    }

    /**
     * Returns Dummy-Object if Hospital Identifier cannot be found in local
     * SQLite-DB
     *
     * @param pHosAddresse Hospital Addresse
     * @param pCountryEn Country ("de", "en")
     * @return Hospital
     */
    public CpxHospital getByAddresse(final String pHosAddresse, final String pCountryEn) {
        return getSingleObject(findManyByAddresse(pHosAddresse, pCountryEn));
    }

    public CpxHospital getByCity1(final String pHosCity, final String pCountryEn) {
        return getSingleObject(findManyByCity(pHosCity, pCountryEn));
    }

    /**
     * Returns Dummy-Object if Hospital Identifier cannot be found in local
     * SQLite-DB
     *
     * @param pHosCity Hospital City
     * @param pCountryEn Country ("de", "en")
     * @return Hospital
     */
    public CpxHospital getByCity(final String pHosCity, final String pCountryEn) {
        return getSingleObject(findManyByCity(pHosCity, pCountryEn));
    }

    /**
     * Returns Dummy-Object if Hospital Identifier cannot be found in local
     * SQLite-DB
     *
     * @param pHosZipCode Hospital PLZ
     * @param pCountryEn Country ("de", "en")
     * @return Hospital
     */
    public CpxHospital getByZipCode(final String pHosZipCode, final String pCountryEn) {
        return getSingleObject(findManyByZipCode(pHosZipCode, pCountryEn));
    }

//    /**
//     * Returns Dummy-Object if Hospital Identifier cannot be found in local
//     * SQLite-DB
//     *
//     * @param pId ID
//     * @param pCountryEn Country ("de", "en")
//     * @return Hospital
//     */
//    @Override
//    public CpxHospital getById(final Long pId, final String pCountryEn) {
//        int year = 0;
//        CpxHospital obj = getNewObject();
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
     * @param pHosIdent Hospital Identifier (IKZ)
     * @param pCountryEn Country ("de", "en")
     * @return Hospital
     */
    @Override
    public List<CpxHospital> findManyByCode(final String pHosIdent, final String pCountryEn) {
        final String code = toParam(pHosIdent);
        String sql = "SELECT * FROM %s WHERE HOS_IDENT " + operator(code) + " ? ORDER BY HOS_IDENT";
        return fetchMany(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        List<CpxHospital> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam(pHosIdent);
//        String sql = String.format("SELECT * FROM %s WHERE HOS_IDENT " + operator(code) + " ? ORDER BY HOS_IDENT", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    CpxHospital obj = toCpxObject(rs);
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
     * Returns Dummy-Object if Hospital Identifier cannot be found in local
     * SQLite-DB
     *
     * @param pHosCity Hospital City
     * @param pCountryEn Country ("de", "en")
     * @return Hospital
     */
    //RSH 22.09.2017 CPX-628
    public List<CpxHospital> findManyByCity(final String pHosCity, final String pCountryEn) {
        final String code = toParam(pHosCity);
        String sql = "SELECT * FROM %s WHERE HOS_CITY " + operator(code) + " ? ORDER BY HOS_CITY";
        return fetchMany(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        List<CpxHospital> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam(pHosCity);
//        String sql = String.format("SELECT * FROM %s WHERE HOS_CITY " + operator(code) + " ? ORDER BY HOS_CITY", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    CpxHospital obj = toCpxObject(rs);
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
     * Returns Dummy-Object if Hospital Identifier cannot be found in local
     * SQLite-DB
     *
     * @param pHosZipCode Hospital Zip Code
     * @param pCountryEn Country ("de", "en")
     * @return Hospital
     */
    //RSH 22.09.2017 CPX-628
    public List<CpxHospital> findManyByZipCode(final String pHosZipCode, final String pCountryEn) {
        final String code = toParam(pHosZipCode);
        String sql = "SELECT * FROM %s WHERE HOS_ZIP_CODE " + operator(code) + " ? ORDER BY HOS_ZIP_CODE";
        return fetchMany(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        List<CpxHospital> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam(pHosZipCode);
//        String sql = String.format("SELECT * FROM %s WHERE HOS_ZIP_CODE " + operator(code) + " ? ORDER BY HOS_ZIP_CODE", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    CpxHospital obj = toCpxObject(rs);
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
     * Returns Dummy-Object if Hospital Identifier cannot be found in local
     * SQLite-DB
     *
     * @param pHosAddresse Hospital addresse
     * @param pCountryEn Country ("de", "en")
     * @return Hospital
     */
    //RSH 22.09.2017 CPX-628
    public List<CpxHospital> findManyByAddresse(final String pHosAddresse, final String pCountryEn) {
        final String code = toParam(pHosAddresse);
        String sql = "SELECT * FROM %s WHERE HOS_ADDRESS " + operator(code) + " ? ORDER BY HOS_ADDRESS";
        return fetchMany(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        List<CpxHospital> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam(pHosAddresse);
//        String sql = String.format("SELECT * FROM %s WHERE HOS_ADDRESS " + operator(code) + " ? ORDER BY HOS_ADDRESS", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    CpxHospital obj = toCpxObject(rs);
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
     * Returns Dummy-Object if Hospital Name cannot be found in local SQLite-DB
     *
     * @param pHosName Hospital Name
     * @param pCountryEn Country ("de", "en")
     * @return Hospital
     */
    public List<CpxHospital> findManyByName(final String pHosName, final String pCountryEn) {
        final String code = toParam(pHosName);
        String sql = "SELECT * FROM %s WHERE HOS_NAME " + operator(code) + " ? ORDER BY HOS_NAME";
        return fetchMany(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        List<CpxHospital> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam(pHosName);
//        String sql = String.format("SELECT * FROM %s WHERE HOS_NAME " + operator(code) + " ? ORDER BY HOS_NAME", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    CpxHospital obj = toCpxObject(rs);
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

    public Collection<String> getBestMatchForHospital(String pHosIdent, String pCountryEn) {
        final String code = toParam(pHosIdent) + "%";
        String sql = "SELECT HOS_IDENT FROM %s WHERE HOS_IDENT " + operator(code) + " ? ORDER BY HOS_IDENT";
        return fetchManyValues(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam(pHosIdent) + "%";
//        String sql = String.format("SELECT HOS_IDENT FROM %s WHERE HOS_IDENT " + operator(code) + " ? ORDER BY HOS_IDENT", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    String obj = rs.getString("HOS_IDENT");
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
     *
     * @param pHosIdent HosIdent
     * @param pCountryEn CountryEn
     * @return Hospital
     */
    public Collection<String> getBestMatchForHospitalIdent(String pHosIdent, String pCountryEn) {
        final String code = "%" + toParam(pHosIdent) + "%";
        String sql = "SELECT HOS_IDENT, HOS_NAME, HOS_CITY FROM %s WHERE HOS_IDENT " + operator(code) + " ? ORDER BY HOS_IDENT";
        return fetchManyValues(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        }, (rs) -> {
            String name = rs.getString("HOS_NAME");
            String city = rs.getString("HOS_CITY");
            String ident = rs.getString("HOS_IDENT");
            String obj = ident + ", " + name + ", " + city;
            return obj;
        });

//        int year = 0;
//        List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = "%" + toParam(pHosIdent) + "%";
//        String sql = String.format("SELECT HOS_IDENT, HOS_NAME,HOS_CITY FROM %s WHERE HOS_IDENT " + operator(code) + " ? ORDER BY HOS_IDENT", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    String name = rs.getString("HOS_NAME");
//                    String city = rs.getString("HOS_CITY");
//                    String ident = rs.getString("HOS_IDENT");
//
//                    String obj = ident + ", " + name + ", " + city;
//                    //CpxHospital obj = toCpxObject(rs);
//
//                    if (obj != null) {
//                        objList.add(obj);
//                        //  objList.add(obj2);
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

    public Collection<String> getBestMatchForHospitalName(String pHosName, String pCountryEn) {
        final String code = "%" + toParam(pHosName) + "%";
        String sql = "SELECT HOS_NAME, HOS_CITY, HOS_IDENT FROM %s WHERE HOS_NAME " + operator(code) + " ? ORDER BY HOS_NAME";
        return fetchManyValues(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        }, (rs) -> {
            String name = rs.getString("HOS_NAME");
            String city = rs.getString("HOS_CITY");
            String ident = rs.getString("HOS_IDENT");
            String obj = ident + ", " + name + ", " + city;
            return obj;
        });

//        int year = 0;
//        List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = "%" + toParam(pHosName) + "%";
//        String sql = String.format("SELECT HOS_NAME,HOS_CITY ,HOS_IDENT FROM %s WHERE HOS_NAME " + operator(code) + " ? ORDER BY HOS_NAME", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    String name = rs.getString("HOS_NAME");
//                    String city = rs.getString("HOS_CITY");
//                    String ident = rs.getString("HOS_IDENT");
//
//                    String obj = name + ", " + city + ", " + ident;
//                    //CpxHospital obj = toCpxObject(rs);
//
//                    if (obj != null) {
//                        objList.add(obj);
//                        //  objList.add(obj2);
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
     *
     * @param pHosAddress Hospital addresse
     * @param pCountryEn Country ("de", "en")
     * @return Collection of BestMatchForHospitalAddresse
     */
    //RSH 22.09.2017 CPX-628
    public Collection<String> getBestMatchForHospitalAddresse(String pHosAddress, String pCountryEn) {
        final String code = toParam(pHosAddress) + "%";
        String sql = "SELECT HOS_ADDRESS FROM %s WHERE HOS_ADDRESS " + operator(code) + " ? ORDER BY HOS_ADDRESS";
        return fetchManyValues(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam(pHosAddress) + "%";
//        String sql = String.format("SELECT HOS_ADDRESS FROM %s WHERE HOS_ADDRESS " + operator(code) + " ? ORDER BY HOS_ADDRESS", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    String obj = rs.getString("HOS_ADDRESS");
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
     *
     * @param pHosZipCode Hospital zip code
     * @param pCountryEn Country ("de", "en")
     * @return Collection of BestMatchForHospitalAddresse
     */
    //RSH 22.09.2017 CPX-628
    public Collection<String> getBestMatchForHospitalZipCode(String pHosZipCode, String pCountryEn) {
        final String code = toParam(pHosZipCode) + "%";
        String sql = "SELECT HOS_ZIP_CODE, HOS_NAME, HOS_IDENT FROM %s WHERE HOS_ZIP_CODE " + operator(code) + " ? ORDER BY HOS_ZIP_CODE";
        return fetchManyValues(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        }, (rs) -> {
            String zipCode = rs.getString("HOS_ZIP_CODE");
            String name = rs.getString("HOS_NAME");
            String ident = rs.getString("HOS_IDENT");
            String obj = zipCode + ", " + name + ", " + ident;
            return obj;
        });

//        int year = 0;
//        List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam(pHosZipCode) + "%";
//        String sql = String.format("SELECT HOS_ZIP_CODE,HOS_NAME, HOS_IDENT FROM %s WHERE HOS_ZIP_CODE " + operator(code) + " ? ORDER BY HOS_ZIP_CODE", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    String zipCode = rs.getString("HOS_ZIP_CODE");
//                    String name = rs.getString("HOS_NAME");
//                    String ident = rs.getString("HOS_IDENT");
//                    String obj = zipCode + ", " + name + ", " + ident;
//                    // String obj = rs.getString("HOS_ZIP_CODE");
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
     *
     * @param pHosCity Hospital city
     * @param pCountryEn Country ("de", "en")
     * @return Collection of BestMatchForHospitalAddresse
     */
    //RSH 22.09.2017 CPX-628
    public Collection<String> getBestMatchForHospitalCity(String pHosCity, String pCountryEn) {
        final String code = toParam(pHosCity) + "%";
        String sql = "SELECT HOS_CITY, HOS_NAME, HOS_IDENT FROM %s WHERE HOS_CITY " + operator(code) + " ? ORDER BY HOS_CITY";
        return fetchManyValues(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        }, (rs) -> {
            String city = rs.getString("HOS_CITY");
            String name = rs.getString("HOS_NAME");
            String ident = rs.getString("HOS_IDENT");
            String obj = city + ", " + name + ", " + ident;
            return obj;
        });

//        int year = 0;
//        List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam(pHosCity) + "%";
//        String sql = String.format("SELECT HOS_CITY ,HOS_NAME, HOS_IDENT FROM %s WHERE HOS_CITY " + operator(code) + " ? ORDER BY HOS_CITY", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    String city = rs.getString("HOS_CITY");
//                    String name = rs.getString("HOS_NAME");
//                    String ident = rs.getString("HOS_IDENT");
//                    String obj = city + ", " + name + ", " + ident;
//                    //CpxHospital obj = toCpxObject(rs);
//
//                    if (obj != null) {
//                        objList.add(obj);
//                        //  objList.add(obj2);
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
     *
     * @param pHosCityNameIdent Hospital city , Name ,Ident
     * @param pCountryEn Country ("de", "en")
     * @return Collection of BestMatchForHospitalAddresse
     */
    //RSH 22.09.2017 CPX-628
    public CpxHospital getBestMatchForHospitalDetailsByCity(String pHosCityNameIdent, String pCountryEn) {
        final String str[] = pHosCityNameIdent.split(",");
        String sql = "SELECT * FROM %s WHERE HOS_CITY = ? AND HOS_NAME = ? AND HOS_IDENT = ?";
        return fetchSingle(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, str[0].trim());
            pstmt.setString(2, str[1].trim());
            pstmt.setString(3, str[2].trim());
        });

//        int year = 0;
//        CpxHospital obj = getNewObject();
//        //List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String str[] = pHosCityNameIdent.split(",");
//        String sql = String.format("SELECT * FROM %s WHERE HOS_CITY = ? AND HOS_NAME = ? AND HOS_IDENT =?", tableName);
//        if (str.length == 3) {
//            try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//                pstmt.setString(1, str[0].trim());
//                pstmt.setString(2, str[1].trim());
//                pstmt.setString(3, str[2].trim());
//                try ( ResultSet rs = pstmt.executeQuery()) {
//                    while (rs.next()) {
//                        obj = toCpxObject(rs);
//                        break;
//                    }
//                    rs.close();
//                } finally {
//                    if (pstmt != null) {
//                        pstmt.close();
//                    }
//                }
//            } catch (SQLException ex) {
//                //Catalog does not exist, SQLite DB file was deleted?!
//                LOG.log(Level.SEVERE, null, ex);
//            }
//        }
//        return obj;
    }

    public CpxHospital getBestMatchForHospitalDetailsByZipCode(String pHosZipcodeNameIdent, String pCountryEn) {
        final String str[] = pHosZipcodeNameIdent.split(",");
        String sql = "SELECT * FROM %s WHERE HOS_ZIP_CODE = ? AND HOS_NAME = ? AND HOS_IDENT = ?";
        return fetchSingle(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, str[0].trim());
            pstmt.setString(2, str[1].trim());
            pstmt.setString(3, str[2].trim());
        });

//        int year = 0;
//        CpxHospital obj = getNewObject();
//        //List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String str[] = pHosZipcodeNameIdent.split(",");
//        String sql = String.format("SELECT * FROM %s WHERE HOS_ZIP_CODE = ? AND HOS_NAME = ? AND HOS_IDENT =?", tableName);
//        if (str.length == 3) {
//            try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//                pstmt.setString(1, str[0].trim());
//                pstmt.setString(2, str[1].trim());
//                pstmt.setString(3, str[2].trim());
//                try ( ResultSet rs = pstmt.executeQuery()) {
//                    while (rs.next()) {
//                        obj = toCpxObject(rs);
//                        break;
//                    }
//                    rs.close();
//                } finally {
//                    if (pstmt != null) {
//                        pstmt.close();
//                    }
//                }
//            } catch (SQLException ex) {
//                //Catalog does not exist, SQLite DB file was deleted?!
//                LOG.log(Level.SEVERE, null, ex);
//            }
//        }
//        return obj;
    }

    /**
     *
     * @param phosNameFull hosNameFull (hosname,city,ident)
     * @param pCountryEn CountryEn
     * @return Hospital
     */
    public CpxHospital getBestMatchForHospitalDetailsByName(String phosNameFull, String pCountryEn) {
        final String str[] = phosNameFull.split(",");
        String sql = "SELECT * FROM %s WHERE HOS_IDENT = ? AND HOS_NAME = ? AND HOS_CITY = ?";
        return fetchSingle(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, str[0].trim());
            pstmt.setString(2, str[1].trim());
            pstmt.setString(3, str[2].trim());
        });

//        int year = 0;
//        CpxHospital obj = getNewObject();
//        //List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String str[] = phosNameFull.split(",");
//        String sql = String.format("SELECT * FROM %s WHERE HOS_NAME = ? AND HOS_CITY = ?   AND HOS_IDENT =?", tableName);
//        if (str.length == 3) {
//            try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//                pstmt.setString(1, str[0].trim());
//                pstmt.setString(2, str[1].trim());
//                pstmt.setString(3, str[2].trim());
//                try ( ResultSet rs = pstmt.executeQuery()) {
//                    while (rs.next()) {
//                        obj = toCpxObject(rs);
//                        break;
//                    }
//                    rs.close();
//                } finally {
//                    if (pstmt != null) {
//                        pstmt.close();
//                    }
//                }
//            } catch (SQLException ex) {
//                //Catalog does not exist, SQLite DB file was deleted?!
//                LOG.log(Level.SEVERE, null, ex);
//            }
//        }
//        return obj;
    }

    public CpxHospital getBestMatchForHospitalDetailsByCode(String pHosNameFull, String pCountryEn) {
        final String str[] = pHosNameFull.split(",");
        String sql = "SELECT * FROM %s WHERE HOS_IDENT = ? AND HOS_NAME = ? AND HOS_CITY = ?";
        return fetchSingle(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, str[0].trim());
            pstmt.setString(2, str[1].trim());
            pstmt.setString(3, str[2].trim());
        });

//        int year = 0;
//        CpxHospital obj = getNewObject();
//        //List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String str[] = pHosNameFull.split(",");
//        String sql = String.format("SELECT * FROM %s WHERE HOS_IDENT =? AND HOS_NAME = ? AND HOS_CITY = ?   ", tableName);
//        if (str.length == 3) {
//            try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//                pstmt.setString(1, str[0].trim());
//                pstmt.setString(2, str[1].trim());
//                pstmt.setString(3, str[2].trim());
//                try ( ResultSet rs = pstmt.executeQuery()) {
//                    while (rs.next()) {
//                        obj = toCpxObject(rs);
//                        break;
//                    }
//                    rs.close();
//                } finally {
//                    if (pstmt != null) {
//                        pstmt.close();
//                    }
//                }
//            } catch (SQLException ex) {
//                LOG.log(Level.SEVERE, null, ex);
//            }
//        }
//        return obj;
    }

    public boolean hasEntry(String pHosIdent, String pCountryEn) {
        final String code = toParam(pHosIdent);
        String sql = "SELECT * FROM %s WHERE HOS_IDENT = ? ORDER BY HOS_IDENT";
        return super.hasEntry(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT * FROM %s WHERE HOS_IDENT = ? ORDER BY HOS_IDENT", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, toParam(pHosIdent));
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
//            LOG.log(Level.SEVERE, null, ex);
//        }
//        return false;
    }

    public boolean hasEntryName(String pHosName, String pCountryEn) {
        final String code = toParam(pHosName);
        String sql = "SELECT * FROM %s WHERE HOS_NAME = ? ORDER BY HOS_NAME";
        return super.hasEntry(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT * FROM %s WHERE HOS_Name = ? ORDER BY HOS_Name", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, toParam(pHosName));
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
//            LOG.log(Level.SEVERE, null, ex);
//        }
//        return false;
    }

    /**
     *
     * @param pHosIdent Hospital Ident
     * @param pCountryEn Country ("de", "en")
     * @return Hopital name HOS_NAME
     */
    //CPX-677 RSH 26.10.2017 
    public String findHosNameByHosIdent(String pHosIdent, String pCountryEn) {
        final String code = toParam(pHosIdent);
        String sql = "SELECT HOS_NAME FROM %s WHERE HOS_IDENT " + operator(code) + " ?";
        return fetchSingleValue(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        String obj = null;
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam(pHosIdent);
//        String sql = String.format("SELECT HOS_NAME FROM %s WHERE HOS_IDENT " + operator(code) + " ?", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
////            pstmt.setString(2, toParam(insc_type));
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    obj = rs.getString("HOS_NAME");
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
    }

    @Override
    public CpxHospital getNewObject() {
        return new CpxHospital();
    }

    public boolean hasHospitals(String pCountryEn) {
        String sql = "SELECT HOS_NAME FROM %s";
        return super.hasEntry(sql, pCountryEn,null);
    }

}
