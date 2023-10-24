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
import de.lb.cpx.server.commonDB.model.CMdk;
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
public class CpxMdkCatalog extends AbstractCpxNonAnnualCatalog<CpxMdk, CMdk> {

    private static CpxMdkCatalog instance = null;
    public static final CatalogTypeEn CATALOG = CatalogTypeEn.MDK;
    private static final Logger LOG = Logger.getLogger(CpxMdkCatalog.class.getName());

    public static synchronized CpxMdkCatalog instance() {
        if (instance == null) {
            instance = new CpxMdkCatalog();
        }
        return instance;
    }
    //Logger log = Logger.getLogger(getClass().getSimpleName());

    private CpxMdkCatalog() {

    }

    @Override
    public void fillCatalog(final CpxCatalogOverview pCatalogOverview, final List<CMdk> pList) throws SQLException {
        final String tableName = getTableName(pCatalogOverview);
        String sqlHead = String.format("INSERT INTO %s(ID, CREATION_DATE, MODIFICATION_DATE, COUNTRY_EN, MDK_INTERNAL_ID, MDK_CITY, MDK_COMMENT, ", tableName)
                + " MDK_DEPARTMENT, MDK_DEPARTMENT_NO, MDK_DISTRICT_NO, MDK_EMAIL, MDK_FAX, MDK_IDENT, MDK_NAME, "
                + " MDK_NOTICE, MDK_PHONE, MDK_PHONE_PREFIX, MDK_STREET, MDK_VALID, MDK_ZIP_CODE) ";
        StringBuilder sql = null;
        //String sql = "INSERT INTO ICD_CATALOG(ID, CREATION_DATE, MODIFICATION_DATE, COUNTRY_EN, ICD_CODE, ICD_IS_COMPLETE_FL, ICD_PARENT_ID, ICD_YEAR, ICD_DESCRIPTION, ICD_EXCLUSION, ICD_INCLUSION, ICD_NOTE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int i = 0;
        boolean first = true;
        for (CMdk mdk : pList) {
            i++;
            if (first) {
                sql = new StringBuilder(sqlHead);
                sql.append(" SELECT ");
                first = false;
            } else {
                sql.append(" UNION ALL SELECT ");
            }
            sql.append(mdk.getId() + ", ");
            sql.append(QStr(mdk.getCreationDate()) + ", ");
            sql.append(QStr(mdk.getModificationDate()) + ", ");
            sql.append(QStr(mdk.getCountryEn().name()) + ", ");
            sql.append(QStr(mdk.getMdkInternalId()) + ", ");
            sql.append(QStr(mdk.getMdkCity()) + ", ");
            sql.append(QStr(mdk.getMdkComment()) + ", ");
            sql.append(QStr(mdk.getMdkDepartment()) + ", ");
            sql.append(QStr(mdk.getMdkDepartmentNo()) + ", ");
            sql.append(QStr(mdk.getMdkDistrictNo()) + ", ");
            sql.append(QStr(mdk.getMdkEmail()) + ", ");
            sql.append(QStr(mdk.getMdkFax()) + ", ");
            sql.append(QStr(mdk.getMdkIdent()) + ", ");
            sql.append(QStr(mdk.getMdkName()) + ", ");
            sql.append(QStr(mdk.getMdkNotice()) + ", ");
            sql.append(QStr(mdk.getMdkPhone()) + ", ");
            sql.append(QStr(mdk.getMdkPhonePrefix()) + ", ");
            sql.append(QStr(mdk.getMdkStreet()) + ", ");
            sql.append(QStr(mdk.getMdkValid()) + ", ");
            sql.append(QStr(mdk.getMdkZipCode()));
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
                    + " MDK_INTERNAL_ID   	INTEGER, "
                    + " MDK_CITY         	VARCHAR(250), "
                    + " MDK_COMMENT	      VARCHAR(250), "
                    + " MDK_DEPARTMENT	    VARCHAR(250), "
                    + " MDK_DEPARTMENT_NO	DECIMAL(10,0), "
                    + " MDK_DISTRICT_NO	  DECIMAL(10,0), "
                    + " MDK_EMAIL	        VARCHAR(50), "
                    + " MDK_FAX	          VARCHAR(50), "
                    + " MDK_IDENT	        VARCHAR(10), "
                    + " MDK_NAME	          VARCHAR(250), "
                    + " MDK_NOTICE	        VARCHAR(250), "
                    + " MDK_PHONE	        VARCHAR(50), "
                    + " MDK_PHONE_PREFIX  	VARCHAR(50), "
                    + " MDK_STREET	        VARCHAR(250), "
                    + " MDK_VALID	        TINYINT    NOT NULL, "
                    + " MDK_ZIP_CODE	      VARCHAR(10) ) ";
            stmt.executeUpdate(sql);

            //sql = "CREATE INDEX IF NOT EXISTS IDX_MDK ON %s (COUNTRY_EN, MDK_ZIP_CODE, MDK_DEPARTMENT_NO)";
            sql = String.format("CREATE INDEX IF NOT EXISTS IDX_MDK ON %s (COUNTRY_EN, MDK_INTERNAL_ID)", tableName);
            stmt.executeUpdate(sql);

            stmt.close();
            return tableName;
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return "";
        }
    }

    @Override
    public CpxMdk toCpxObject(final ResultSet rs) throws SQLException {
        CpxMdk obj = getNewObject();
        if (rs == null) {
            return obj;
        }
        obj.setId(rs.getLong("ID"));
        //obj.setCreationDate(CpxDateParser.parseDateTime(rs.getString("CREATION_DATE")));
        //obj.setModificationDate(CpxDateParser.parseDateTime(rs.getString("MODIFICATION_DATE")));
        //obj.setCountryEn(CountryEn.valueOf(rs.getString("COUNTRY_EN")));
        obj.setMdkInternalId(rs.getLong("MDK_INTERNAL_ID"));
        obj.setMdkCity(rs.getString("MDK_CITY"));
        obj.setMdkComment(rs.getString("MDK_COMMENT"));
        obj.setMdkDepartment(rs.getString("MDK_DEPARTMENT"));
        obj.setMdkDepartmentNo(rs.getInt("MDK_DEPARTMENT_NO"));
        obj.setMdkDistrictNo(rs.getInt("MDK_DISTRICT_NO"));
        obj.setMdkEmail(rs.getString("MDK_EMAIL"));
        obj.setMdkFax(rs.getString("MDK_FAX"));
        obj.setMdkIdent(rs.getString("MDK_IDENT"));
        obj.setMdkName(rs.getString("MDK_NAME"));
        obj.setMdkNotice(rs.getString("MDK_NOTICE"));
        obj.setMdkPhone(rs.getString("MDK_PHONE"));
        obj.setMdkPhonePrefix(rs.getString("MDK_PHONE_PREFIX"));
        obj.setMdkStreet(rs.getString("MDK_STREET"));
        obj.setMdkValid(rs.getBoolean("MDK_VALID"));
        obj.setMdkZipCode(rs.getString("MDK_ZIP_CODE"));
        return obj;
    }

//    /**
//     * Returns Dummy-Object if Mdk Internal Id cannot be found in local
//     * SQLite-DB
//     *
//     * @param pMdkInternalId Mdk Internal Id
//     * @param pCountryEn Country ("de", "en")
//     * @return Mdk
//     */
//    @Override
//    public CpxMdk getByCode(final String pMdkInternalId, final String pCountryEn) {
//        List<CpxMdk> list = findManyByCode(pMdkInternalId, pCountryEn);
//        if (list.isEmpty()) {
//            return getNewObject();
//        }
//        return list.get(0);
//    }
//    /**
//     * Returns Dummy-Object if Mdk Identifier cannot be found in local SQLite-DB
//     *
//     * @param pId ID
//     * @param pCountryEn Country ("de", "en")
//     * @return Mdk
//     */
//    @Override
//    public CpxMdk getById(final Long pId, final String pCountryEn) {
//        int year = 0;
//        CpxMdk obj = getNewObject();
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
     * Returns Dummy-Object if Mdk Internal Id cannot be found in local
     * SQLite-DB
     *
     * @param pMdkInternalId Mdk Internal Id
     * @param pCountryEn Country ("de", "en")
     * @return Mdk
     */
    @Override
    public List<CpxMdk> findManyByCode(final String pMdkInternalId, final String pCountryEn) {
        final String code = toParam(pMdkInternalId);
        String sql = "SELECT * FROM %s WHERE MDK_INTERNAL_ID " + operator(code) + " ? ORDER BY MDK_INTERNAL_ID";
        return fetchMany(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        List<CpxMdk> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam(pMdkInternalId);
//        String sql = String.format("SELECT * FROM %s WHERE MDK_INTERNAL_ID " + operator(code) + " ? ORDER BY MDK_INTERNAL_ID", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    CpxMdk obj = toCpxObject(rs);
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
    public boolean hasMdks(){
        return hasMdks(AbstractCpxCatalog.DEFAULT_COUNTRY);
    }
    public boolean hasMdks(final String pCountryEn){
        String sql = "SELECT MDK_NAME FROM %s";
        return super.hasEntry(sql, pCountryEn, null);
    }
    public Collection<String> findMatchesByMdkName(String userText, String pCountryEn) {
        final String code = toParam("%" + userText) + "%";
        String sql = "SELECT MDK_NAME, MDK_DEPARTMENT, MDK_CITY, MDK_VALID FROM %s WHERE MDK_NAME " + operator(code) + " ? OR MDK_DEPARTMENT " + operator(code) + " ? OR MDK_CITY " + operator(code) + " ? AND MDK_VALID = ?" + " ORDER BY MDK_NAME";
        return fetchManyValues(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
            pstmt.setString(2, code);
            pstmt.setString(3, code);
            pstmt.setInt(4, 1); //is valid?
        }, (rs) -> {
            String name = rs.getString("MDK_NAME");
            String dept = rs.getString("MDK_DEPARTMENT");
            String city = rs.getString("MDK_CITY");
//            int valid = rs.getInt(4);
            String name_dept_city = name + ", " + dept + ", " + city;
            return name_dept_city;
        });

//        int year = 0;
//        List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam("%" + userText) + "%";
//        String sql = String.format("SELECT MDK_NAME, MDK_DEPARTMENT, MDK_CITY, MDK_VALID FROM %s WHERE MDK_NAME " + operator(code) + " ? OR MDK_DEPARTMENT " + operator(code) + " ? OR MDK_CITY " + operator(code) + " ? AND MDK_VALID = ?" + " ORDER BY MDK_NAME", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            pstmt.setString(2, code);
//            pstmt.setString(3, code);
//            pstmt.setInt(4, 1);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    String name = rs.getString("MDK_NAME");
//                    String dept = rs.getString("MDK_DEPARTMENT");
//                    String city = rs.getString("MDK_CITY");
//                    int valid = rs.getInt(4);
//                    String name_dept_city = name + ", " + dept + ", " + city;
//                    if (name != null && valid == 1) {
//                        objList.add(name_dept_city);
//                    }
//                }
//                rs.close();
//            } finally {
//                if (pstmt != null) {
//                    pstmt.close();
//                }
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(CpxInsuranceCompanyCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return objList;
    }

    public CpxMdk getByNameDeptCity(String name, String department, String cityname, String pCountryEn) {
        String sql = "SELECT * FROM %s WHERE MDK_NAME = ? AND MDK_DEPARTMENT = ? AND MDK_CITY = ?";
        return fetchSingle(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, name);
            pstmt.setString(2, department);
            pstmt.setString(3, cityname);
        });

//        int year = 0;
//        CpxMdk obj = getNewObject();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
////        String sql = "SELECT * FROM %s WHERE (MDK_NAME + MDK_DEPARTMENT + MDK_CITY) LIKE '%string%'";
//        String sql = String.format("SELECT * FROM %s WHERE MDK_NAME = ? AND MDK_DEPARTMENT = ? AND MDK_CITY = ?", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, name);
//            pstmt.setString(2, department);
//            pstmt.setString(3, cityname);
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
//            Logger.getLogger(CpxInsuranceCompanyCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return obj;
    }

    public CMdk getByFullMdkName(String fullMdkName, String pCountryEn) {
        final String str[] = fullMdkName.split(",");
        if (str.length < 3) {
            LOG.log(Level.FINEST, "Invalid MDK Full Name: {0}", fullMdkName);
            return new CMdk();
        }
        String sql = "SELECT * FROM %s WHERE MDK_NAME = ? AND MDK_DEPARTMENT = ? AND MDK_CITY = ? ";
        return fetchSingle(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, str[0].trim());
            pstmt.setString(2, str[1].trim());
            pstmt.setString(3, str[2].trim());
        });

//        int year = 0;
//        CMdk obj = new CMdk();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String str[] = fullMdkName.split(",");
//        if (str.length == 3) {
//            String sql = String.format("SELECT * FROM %s WHERE MDK_NAME = ? AND MDK_DEPARTMENT = ? AND MDK_CITY = ? ", tableName);
//            try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//                pstmt.setString(1, str[0].trim());
//                pstmt.setString(2, str[1].trim());
//                pstmt.setString(3, str[2].trim());
//
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
//                Logger.getLogger(CpxInsuranceCompanyCatalog.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        } else {
////            System.out.println("Invalid MDK Full Name!!");
//        }
//        return obj;
    }

    public Collection<String> findMatchesByMdkDienstStelle(String userText, String pCountryEn) {
        final String code = toParam("%" + userText) + "%";
        String sql = "SELECT MDK_DEPARTMENT FROM %s WHERE MDK_DEPARTMENT " + operator(code) + " ? ORDER BY MDK_DEPARTMENT";
        return fetchManyValues(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam("%" + userText) + "%";
//        String sql = String.format("SELECT MDK_DEPARTMENT FROM %s WHERE MDK_DEPARTMENT " + operator(code) + " ? ORDER BY MDK_DEPARTMENT", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    String obj = rs.getString("MDK_DEPARTMENT");
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
//            Logger.getLogger(CpxInsuranceCompanyCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return objList;
    }

    public CMdk getByMDKDienststelle(String str, String pCountryEn) {
        String sql = "SELECT * FROM %s WHERE MDK_DEPARTMENT = ?";
        return fetchSingle(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, str);
        });

//        int year = 0;
//        CMdk obj = new CMdk();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT * FROM %s WHERE MDK_DEPARTMENT = ?", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, str);
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
//            Logger.getLogger(CpxInsuranceCompanyCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return obj;
    }

    public Collection<String> findMatchesByPostleitzahl(String userText, String pCountryEn) {
        final String code = toParam("%" + userText) + "%";
        String sql = "SELECT MDK_ZIP_CODE FROM %s WHERE MDK_ZIP_CODE " + operator(code) + " ? ORDER BY MDK_ZIP_CODE";
        return fetchManyValues(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam("%" + userText) + "%";
//        String sql = String.format("SELECT MDK_ZIP_CODE FROM %s WHERE MDK_ZIP_CODE " + operator(code) + " ? ORDER BY MDK_ZIP_CODE", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    String obj = rs.getString("MDK_ZIP_CODE");
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
//            Logger.getLogger(CpxInsuranceCompanyCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return objList;
    }

    public CMdk getByMDKPostleitzahl(String str, String pCountryEn) {
        String sql = "SELECT * FROM %s WHERE MDK_ZIP_CODE = ?";
        return fetchSingle(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, str);
        });

//        int year = 0;
//        CMdk obj = new CMdk();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT * FROM %s WHERE MDK_ZIP_CODE = ?", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, str);
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
//            Logger.getLogger(CpxInsuranceCompanyCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return obj;
    }

    public Collection<String> findMatchesByAnschrift(String userText, String pCountryEn) {
        final String code = toParam("%" + userText) + "%";
        String sql = "SELECT MDK_STREET FROM %s WHERE MDK_STREET " + operator(code) + " ? ORDER BY MDK_STREET";
        return fetchManyValues(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam("%" + userText) + "%";
//        String sql = String.format("SELECT MDK_STREET FROM %s WHERE MDK_STREET " + operator(code) + " ? ORDER BY MDK_STREET", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    String obj = rs.getString("MDK_STREET");
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
//            Logger.getLogger(CpxInsuranceCompanyCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return objList;
    }

    public CMdk getByAnschrift(String str, String pCountryEn) {
        String sql = "SELECT * FROM %s WHERE MDK_STREET = ?";
        return fetchSingle(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, str);
        });

//        int year = 0;
//        CMdk obj = new CMdk();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT * FROM %s WHERE MDK_STREET = ?", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, str);
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
//            Logger.getLogger(CpxInsuranceCompanyCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return obj;
    }

    public Collection<String> findMatchesByMailAdresse(String userText, String pCountryEn) {
        final String code = toParam("%" + userText) + "%";
        String sql = "SELECT MDK_EMAIL FROM %s WHERE MDK_EMAIL " + operator(code) + " ? ORDER BY MDK_EMAIL";
        return fetchManyValues(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam("%" + userText) + "%";
//        String sql = String.format("SELECT MDK_EMAIL FROM %s WHERE MDK_EMAIL " + operator(code) + " ? ORDER BY MDK_EMAIL", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    String obj = rs.getString("MDK_EMAIL");
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
//            Logger.getLogger(CpxInsuranceCompanyCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return objList;
    }

    public CMdk getByMailAdresse(String str, String pCountryEn) {
        String sql = "SELECT * FROM %s WHERE MDK_EMAIL = ?";
        return fetchSingle(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, str);
        });

//        int year = 0;
//        CMdk obj = new CMdk();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT * FROM %s WHERE MDK_EMAIL = ?", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, str);
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
//            Logger.getLogger(CpxInsuranceCompanyCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return obj;
    }

    public Collection<String> findMatchesByVorwahl(String userText, String pCountryEn) {
        final String code = toParam("%" + userText) + "%";
        String sql = "SELECT MDK_PHONE_PREFIX FROM %s WHERE MDK_PHONE_PREFIX " + operator(code) + " ? ORDER BY MDK_PHONE_PREFIX";
        return fetchManyValues(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam("%" + userText) + "%";
//        String sql = String.format("SELECT MDK_PHONE_PREFIX FROM %s WHERE MDK_PHONE_PREFIX " + operator(code) + " ? ORDER BY MDK_PHONE_PREFIX", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    String obj = rs.getString("MDK_PHONE_PREFIX");
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
//            Logger.getLogger(CpxInsuranceCompanyCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return objList;
    }

    public CMdk getByMailVorwahl(String str, String pCountryEn) {
        String sql = "SELECT * FROM %s MDK_PHONE_PREFIX = ?";
        return fetchSingle(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, str);
        });

//        int year = 0;
//        CMdk obj = new CMdk();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT * FROM %s WHERE MDK_PHONE_PREFIX = ?", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, str);
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
//            Logger.getLogger(CpxInsuranceCompanyCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return obj;
    }

    public Collection<String> findMatchesByTelefon(String userText, String pCountryEn) {
        final String code = toParam("%" + userText) + "%";
        String sql = "SELECT MDK_PHONE FROM %s WHERE MDK_PHONE " + operator(code) + " ? ORDER BY MDK_PHONE";
        return fetchManyValues(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam("%" + userText) + "%";
//        String sql = String.format("SELECT MDK_PHONE FROM %s WHERE MDK_PHONE " + operator(code) + " ? ORDER BY MDK_PHONE", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    String obj = rs.getString("MDK_PHONE");
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
//            Logger.getLogger(CpxInsuranceCompanyCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return objList;
    }

    public CMdk getByTelefon(String str, String pCountryEn) {
        String sql = "SELECT * FROM %s MDK_PHONE = ?";
        return fetchSingle(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, str);
        });

//        int year = 0;
//        CMdk obj = new CMdk();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT * FROM %s WHERE MDK_PHONE = ?", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, str);
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
//            Logger.getLogger(CpxInsuranceCompanyCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return obj;
    }

    public Collection<String> findMatchesByFax(String userText, String pCountryEn) {
        final String code = toParam("%" + userText) + "%";
        String sql = "SELECT MDK_FAX FROM %s WHERE MDK_FAX " + operator(code) + " ? ORDER BY MDK_FAX";
        return fetchManyValues(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam("%" + userText) + "%";
//        String sql = String.format("SELECT MDK_FAX FROM %s WHERE MDK_FAX " + operator(code) + " ? ORDER BY MDK_FAX", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    String obj = rs.getString("MDK_FAX");
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
//            Logger.getLogger(CpxInsuranceCompanyCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return objList;
    }

    public CpxMdk getByFax(String str, String pCountryEn) {
        String sql = "SELECT * FROM %s MDK_FAX = ?";
        return fetchSingle(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, str);
        });

//        int year = 0;
//        CpxMdk obj = getNewObject();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT * FROM %s WHERE MDK_FAX = ?", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, str);
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
//            Logger.getLogger(CpxInsuranceCompanyCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return obj;
    }

//    public String getMdkNameById(Long mdkInternalId, final String pCountryEn) {
//        long id = (mdkInternalId == null) ? 0L : mdkInternalId;
//        String sql = "SELECT MDK_NAME FROM %s WHERE MDK_INTERNAL_ID = ?";
//        return fetchSingleValue(sql, pCountryEn, (pstmt) -> {
//            pstmt.setLong(1, id);
//        });
//    }

    public CpxMdk getByInternalId(Long mdkInternalId, String pCountryEn) {
        long id = (mdkInternalId == null) ? 0L : mdkInternalId;
        String sql = "SELECT * FROM %s WHERE MDK_INTERNAL_ID = ?";
        return fetchSingle(sql, pCountryEn, (pstmt) -> {
            pstmt.setLong(1, id);
        });

//        int year = 0;
//        CpxMdk obj = getNewObject();
//        long id = (mdkInternalId == null) ? 0L : mdkInternalId;
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT * FROM %s WHERE MDK_INTERNAL_ID = ?", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setLong(1, id); //mdkInternalId
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
    }

    public boolean hasEntry(String mdkName, String pCountryEn) {
        final String code = toParam(mdkName);
        String sql = "SELECT MDK_NAME FROM %s WHERE MDK_NAME = ?";
        return super.hasEntry(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
////        String str[] = mdkName.split(", ");
////        String sql = String.format("SELECT MDK_NAME, MDK_DEPARTMENT, MDK_CITY FROM %s WHERE MDK_NAME = ? AND MDK_DEPARTMENT = ? AND MDK_CITY = ? ", tableName);
//        String sql = String.format("SELECT MDK_NAME FROM %s WHERE MDK_NAME = ? ", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
////            pstmt.setString(1, toParam(str[0]).trim());
////            pstmt.setString(2, toParam(str[1]).trim());
////            pstmt.setString(3, toParam(str[2]).trim());
//            pstmt.setString(1, toParam(mdkName));
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

    public boolean hasDeptEntry(String dept, String pCountryEn) {
        if (dept == null) {
            return false;
        }
        final String code = toParam(dept);
        String sql = "SELECT MDK_DEPARTMENT FROM %s WHERE MDK_DEPARTMENT = ?";
        return super.hasEntry(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        if (dept == null) {
//            return false;
//        }
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT MDK_DEPARTMENT FROM %s WHERE MDK_DEPARTMENT = ? ", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, toParam(dept.trim()));
//
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

    public boolean hasCityEntry(String city, String pCountryEn) {
        if (city == null) {
            return false;
        }
        final String code = toParam(city);
        String sql = "SELECT MDK_CITY FROM %s WHERE MDK_CITY = ?";
        return super.hasEntry(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        if (city == null) {
//            return false;
//        }
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT MDK_CITY FROM %s WHERE MDK_CITY = ? ", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, toParam(city.trim()));
//
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

    public boolean hasZipCodeEntry(String zipCode, String pCountryEn) {
        if (zipCode == null) {
            return false;
        }
        final String code = toParam(zipCode);
        String sql = "SELECT MDK_ZIP_CODE FROM %s WHERE MDK_ZIP_CODE = ?";
        return super.hasEntry(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        if (zipCode == null) {
//            return false;
//        }
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT MDK_ZIP_CODE FROM %s WHERE MDK_ZIP_CODE = ? ", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, toParam(zipCode));
//
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

    public boolean hasAddressEntry(String address, String pCountryEn) {
        if (address == null) {
            return false;
        }
        final String code = toParam(address);
        String sql = "SELECT MDK_STREET FROM %s WHERE MDK_STREET = ?";
        return super.hasEntry(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        if (address == null) {
//            return false;
//        }
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT MDK_STREET FROM %s WHERE MDK_STREET = ? ", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, toParam(address));
//
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

    public boolean hasEmailAddressEntry(String emailAddress, String pCountryEn) {
        if (emailAddress == null) {
            return false;
        }
        final String code = toParam(emailAddress);
        String sql = "SELECT MDK_EMAIL FROM %s WHERE MDK_EMAIL = ?";
        return super.hasEntry(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        if (emailAddress == null) {
//            return false;
//        }
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT MDK_EMAIL FROM %s WHERE MDK_EMAIL = ? ", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, toParam(emailAddress));
//
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

    public boolean hasAreaCodeEntry(String areaCode, String pCountryEn) {
        if (areaCode == null) {
            return false;
        }
        final String code = toParam(areaCode);
        String sql = "SELECT MDK_PHONE_PREFIX FROM %s WHERE MDK_PHONE_PREFIX = ?";
        return super.hasEntry(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        if (areaCode == null) {
//            return false;
//        }
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT MDK_PHONE_PREFIX FROM %s WHERE MDK_PHONE_PREFIX = ? ", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, toParam(areaCode));
//
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

    public boolean hasTelephoneEntry(String telephone, String pCountryEn) {
        if (telephone == null) {
            return false;
        }
        final String code = toParam(telephone);
        String sql = "SELECT MDK_PHONE FROM %s WHERE MDK_PHONE = ?";
        return super.hasEntry(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        if (telephone == null) {
//            return false;
//        }
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT MDK_PHONE FROM %s WHERE MDK_PHONE = ? ", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, toParam(telephone));
//
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

    public boolean hasFaxEntry(String fax, String pCountryEn) {
        if (fax == null) {
            return false;
        }
        final String code = toParam(fax);
        String sql = "SELECT MDK_FAX FROM %s WHERE MDK_FAX = ?";
        return super.hasEntry(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        if (fax == null) {
//            return false;
//        }
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT MDK_FAX FROM %s WHERE MDK_FAX = ? ", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, toParam(fax));
//
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

    @Override
    public CpxMdk getNewObject() {
        return new CpxMdk();
    }

}
