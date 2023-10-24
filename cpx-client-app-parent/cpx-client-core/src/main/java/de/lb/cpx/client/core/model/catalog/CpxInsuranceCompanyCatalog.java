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
import de.lb.cpx.server.commonDB.model.CInsuranceCompany;
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
public class CpxInsuranceCompanyCatalog extends AbstractCpxNonAnnualCatalog<CpxInsuranceCompany, CInsuranceCompany> {

    private static CpxInsuranceCompanyCatalog instance = null;
    public static final CatalogTypeEn CATALOG = CatalogTypeEn.INSURANCE_COMPANY;
    private static final Logger LOG = Logger.getLogger(CpxInsuranceCompanyCatalog.class.getName());

    public static synchronized CpxInsuranceCompanyCatalog instance() {
        if (instance == null) {
            instance = new CpxInsuranceCompanyCatalog();
        }
        return instance;
    }
    //Logger log = Logger.getLogger(getClass().getSimpleName());

    private CpxInsuranceCompanyCatalog() {

    }

    @Override
    public void fillCatalog(final CpxCatalogOverview pCatalogOverview, final List<CInsuranceCompany> pList) throws SQLException {
        final String tableName = getTableName(pCatalogOverview);
        String sqlHead = String.format("INSERT INTO %s(ID, CREATION_DATE, MODIFICATION_DATE, COUNTRY_EN, INSC_IDENT, INSC_NAME, INSC_SHORT, ", tableName)
                + " INSC_ADDRESS, INSC_ZIP_CODE, INSC_CITY, INSC_PHONE_PREFIX, INSC_PHONE, INSC_FAX, "
                + " INSC_CHANGE_SERVICE, INSC_CLASS, INSC_REGION, INSC_KBV_INDICATOR, INSC_KBVZ_INDICATOR "
                + " ) ";
        StringBuilder sql = null;
        //String sql = "INSERT INTO ICD_CATALOG(ID, CREATION_DATE, MODIFICATION_DATE, COUNTRY_EN, ICD_CODE, ICD_IS_COMPLETE_FL, ICD_PARENT_ID, ICD_YEAR, ICD_DESCRIPTION, ICD_EXCLUSION, ICD_INCLUSION, ICD_NOTE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int i = 0;
        boolean first = true;
        for (CInsuranceCompany insurance : pList) {
            i++;
            if (first) {
                sql = new StringBuilder(sqlHead);
                sql.append(" SELECT ");
                first = false;
            } else {
                sql.append(" UNION ALL SELECT ");
            }
            sql.append(insurance.getId() + ", ");
            sql.append(QStr(insurance.getCreationDate()) + ", ");
            sql.append(QStr(insurance.getModificationDate()) + ", ");
            sql.append(QStr(insurance.getCountryEn().name()) + ", ");
            sql.append(QStr(insurance.getInscIdent()) + ", ");
            sql.append(QStr(insurance.getInscName()) + ", ");
            //sql.append(QStr((insurance.getInscShort() == null ? null : insurance.getInscShort().name())) + ", ");
            sql.append(QStr((insurance.getInscShort())) + ", ");
            sql.append(QStr(insurance.getInscAddress()) + ", ");
            sql.append(QStr(insurance.getInscZipCode()) + ", ");
            sql.append(QStr(insurance.getInscCity()) + ", ");
            sql.append(QStr(insurance.getInscPhonePrefix()) + ", ");
            sql.append(QStr(insurance.getInscPhone()) + ", ");
            sql.append(QStr(insurance.getInscFax()) + ", ");
            sql.append(QStr(insurance.getInscChangeService()) + ", ");
            sql.append(QStr(insurance.getInscClass()) + ", ");
            sql.append(QStr(insurance.getInscRegion()) + ", ");
            sql.append(QStr(insurance.getInscKbvIndicator()) + ", ");
            sql.append(QStr(insurance.getInscKbvzIndicator()));
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
                    + "(ID                  INTEGER    PRIMARY KEY,"
                    + " CREATION_DATE       DATETIME, "
                    + " MODIFICATION_DATE   DATETIME, "
                    + " COUNTRY_EN          VARCHAR(25), "
                    + " INSC_IDENT          VARCHAR(10), "
                    + " INSC_NAME           VARCHAR(255), "
                    + " INSC_SHORT          VARCHAR(50), "
                    + " INSC_ADDRESS        VARCHAR(255), "
                    + " INSC_ZIP_CODE       VARCHAR(10), "
                    + " INSC_CITY           VARCHAR(50), "
                    + " INSC_PHONE_PREFIX   VARHCAR(50), "
                    + " INSC_PHONE          VARCHAR(50), "
                    + " INSC_FAX            VARCHAR(50), "
                    + " INSC_CHANGE_SERVICE INTEGER, "
                    + " INSC_CLASS          INTEGER, "
                    + " INSC_REGION         INTEGER, "
                    + " INSC_KBV_INDICATOR  INTEGER, "
                    + " INSC_KBVZ_INDICATOR INTEGER ) ";
            stmt.executeUpdate(sql);

            sql = String.format("CREATE INDEX IF NOT EXISTS IDX_INSURANCE_COMPANY ON %s (INSC_IDENT)", tableName); //COUNTRY_EN
            stmt.executeUpdate(sql);

            stmt.close();
            return tableName;
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return "";
        }
    }

    @Override
    public CpxInsuranceCompany toCpxObject(final ResultSet rs) throws SQLException {
        CpxInsuranceCompany obj = getNewObject();
        if (rs == null) {
            return obj;
        }
        obj.setId(rs.getLong("ID"));
        //obj.setCreationDate(CpxDateParser.parseDateTime(rs.getString("CREATION_DATE")));
        //obj.setModificationDate(CpxDateParser.parseDateTime(rs.getString("MODIFICATION_DATE")));
        //obj.setCountryEn(CountryEn.valueOf(rs.getString("COUNTRY_EN")));
        obj.setInscIdent(rs.getString("INSC_IDENT"));
        obj.setInscName(rs.getString("INSC_NAME"));
        //obj.setInscShort(InsShortEn.findById(rs.getString("INSC_SHORT")));
        obj.setInscShort(rs.getString("INSC_SHORT"));
        obj.setInscAddress(rs.getString("INSC_ADDRESS"));
        obj.setInscZipCode(rs.getString("INSC_ZIP_CODE"));
        obj.setInscCity(rs.getString("INSC_CITY"));
        obj.setInscPhonePrefix(rs.getString("INSC_PHONE_PREFIX"));
        obj.setInscPhone(rs.getString("INSC_PHONE"));
        obj.setInscFax(rs.getString("INSC_FAX"));
        obj.setInscChangeService(rs.getInt("INSC_CHANGE_SERVICE"));
        obj.setInscClass(rs.getInt("INSC_CLASS"));
        obj.setInscRegion(rs.getInt("INSC_REGION"));
        obj.setInscKbvIndicator(rs.getInt("INSC_KBV_INDICATOR"));
        obj.setInscKbvzIndicator(rs.getInt("INSC_KBVZ_INDICATOR"));
        return obj;
    }

//    /**
//     * Returns Dummy-Object if InsuranceCompany Identifier cannot be found in
//     * local SQLite-DB
//     *
//     * @param pInscIdent InsuranceCompany Identifier (IKZ)
//     * @param pCountryEn Country ("de", "en")
//     * @return InsuranceCompany
//     */
//    @Override
//    public CpxInsuranceCompany getByCode(final String pInscIdent, final String pCountryEn) {
//        List<CpxInsuranceCompany> list = findManyByCode(pInscIdent, pCountryEn);
//        if (list.isEmpty()) {
//            return getNewObject();
//        }
//        return list.get(0);
//    }
//    /**
//     * Returns Dummy-Object if InsuranceCompany Identifier cannot be found in
//     * local SQLite-DB
//     *
//     * @param pId ID
//     * @param pCountryEn Country ("de", "en")
//     * @return InsuranceCompany
//     */
//    @Override
//    public CpxInsuranceCompany getById(final Long pId, final String pCountryEn) {
//        int year = 0;
//        CpxInsuranceCompany obj = getNewObject();
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
     * Returns Dummy-Object if InsuranceCompany Identifier cannot be found in
     * local SQLite-DB
     *
     * @param pInscIdent InsuranceCompany Identifier (IKZ)
     * @param pCountryEn Country ("de", "en")
     * @return InsuranceCompany
     */
    @Override
    public List<CpxInsuranceCompany> findManyByCode(final String pInscIdent, final String pCountryEn) {
        final String code = toParam(pInscIdent);
        String sql = "SELECT * FROM %s WHERE INSC_IDENT " + operator(code) + " ? ORDER BY INSC_IDENT";
        return fetchMany(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        List<CpxInsuranceCompany> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam(pInscIdent);
//        String sql = String.format("SELECT * FROM %s WHERE INSC_IDENT " + operator(code) + " ? ORDER BY INSC_IDENT", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    CpxInsuranceCompany obj = toCpxObject(rs);
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
    //RSH 02112017 CPX-642

//    /**
//     * Returns Dummy-Object if InsuranceCompany Name cannot be found in local
//     * SQLite-DB
//     *
//     * @param pInsName InsuranceCompany Name (KK)
//     * @param pCountryEn Country ("de", "en")
//     * @return InsuranceCompany
//     */
//    public List<CpxInsuranceCompany> findManyByName(String pInsName, String pCountryEn) {
//        int year = 0;
//        List<CpxInsuranceCompany> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam(pInsName);
//        String sql = String.format("SELECT * FROM %s WHERE INSC_NAME " + operator(code) + " ? ORDER BY INSC_NAME", tableName);
//        try (PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try (ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    CpxInsuranceCompany obj = toCpxObject(rs);
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
//    }
    @Override
    public CatalogTypeEn getCatalogType() {
        return CATALOG;
    }

    public Collection<String> findMatchesByIdent(String partialInsuranceNumber, final String pCountryEn) {
        final String code = toParam(partialInsuranceNumber) + "%";
        String sql = "SELECT INSC_IDENT FROM %s WHERE INSC_IDENT " + operator(code) + " ? ORDER BY INSC_IDENT";
        return fetchManyValues(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam(partialInsuranceNumber) + "%";
//        String sql = String.format("SELECT INSC_IDENT FROM %s WHERE INSC_IDENT " + operator(code) + " ? ORDER BY INSC_IDENT", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    String obj = rs.getString("INSC_IDENT");
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

    public Collection<String> findMatchesByInsuranceNumber(String partialInsuranceNumber, final String pCountryEn) {
        final String code = toParam("%" + partialInsuranceNumber) + "%";
        String sql = "SELECT INSC_IDENT, INSC_NAME, INSC_CITY FROM %s WHERE INSC_IDENT " + operator(code) + " ? ORDER BY INSC_IDENT";
        return fetchManyValues(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        }, (rs) -> {
            final String obj;
            if (rs.getString("INSC_CITY") != null && !rs.getString("INSC_CITY").isEmpty()) {
                obj = rs.getString("INSC_IDENT") + " , " + rs.getString("INSC_NAME") + " , " + rs.getString("INSC_CITY");
            } else {
                obj = rs.getString("INSC_IDENT") + " , " + rs.getString("INSC_NAME");
            }
            return obj;
        });

//        int year = 0;
//        List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam("%" + partialInsuranceNumber) + "%";
//        String sql = String.format("SELECT INSC_IDENT ,INSC_NAME,INSC_CITY FROM %s WHERE INSC_IDENT " + operator(code) + " ? ORDER BY INSC_IDENT", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    String obj;
//                    if (rs.getString("INSC_CITY") != null && !rs.getString("INSC_CITY").isEmpty()) {
//                        obj = rs.getString("INSC_IDENT") + " , " + rs.getString("INSC_NAME") + " , " + rs.getString("INSC_CITY");
//                    } else {
//                        obj = rs.getString("INSC_IDENT") + " , " + rs.getString("INSC_NAME");
//                    }
//
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

    public Collection<String> findMatchesByInsuranceNumber(String partialInsuranceNumber, String inscType, final String pCountryEn) {
        final String code1 = toParam(partialInsuranceNumber) + "%";
        final String code2 = toParam(inscType);
        String sql = "SELECT INSC_IDENT FROM %s WHERE INSC_IDENT " + operator(code1) + " ? AND INSC_SHORT " + operator(code2) + " ? ORDER BY INSC_IDENT";
        return fetchManyValues(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code1);
            pstmt.setString(2, code2);
        });

//        int year = 0;
//        List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code1 = toParam(partialInsuranceNumber) + "%";
//        final String code2 = toParam(inscType);
//        String sql = String.format("SELECT INSC_IDENT FROM %s WHERE INSC_IDENT " + operator(code1) + " ? AND INSC_SHORT " + operator(code2) + " ? ORDER BY INSC_IDENT", tableName);
////        LOG.info("pstmt " + sql + " number " + partialInsuranceNumber + " type " + insc_type);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code1);
//            pstmt.setString(2, code2);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    String obj = rs.getString("INSC_IDENT");
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
    public boolean hasEntryOfInscClass(Integer pInscClass, final String pCountryEn){
        String sql = "SELECT INSC_IDENT FROM %s";
        if(pInscClass != null){
            sql = new StringBuilder(sql).append(" WHERE INSC_CLASS = ?").toString();
        }
        return super.hasEntry(sql, pCountryEn, (pstmt) -> {
            if(pInscClass != null){
                pstmt.setInt(1, pInscClass);
            }
        });
    }
    public boolean hasInsurances(){
        return hasInsurances(AbstractCpxCatalog.DEFAULT_COUNTRY);
    }
    public boolean hasInsurances(final String pCountryEn){
        return hasEntryOfInscClass(null, pCountryEn);
    }
    /**
     * find insurances for partial insurance number
     *
     * @param partialInsuranceNumber partial number
     * @param inscClass insurance class e.g. BG = 12, ???
     * @param pCountryEn country for the insurance
     * @return list of available insurances
     */
    public Collection<String> findMatchesByInsuranceNumber(String partialInsuranceNumber, Integer inscClass, final String pCountryEn) {
        final String code1 = toParam(partialInsuranceNumber) + "%";
        final Integer code2 = inscClass;
        String sql = "SELECT INSC_IDENT FROM %s WHERE INSC_IDENT " + operator(code1) + " ? AND INSC_CLASS = ? ORDER BY INSC_IDENT";
        return fetchManyValues(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code1);
            pstmt.setInt(2, code2);
        });
        
//        int year = 0;
//        List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code1 = toParam(partialInsuranceNumber) + "%";
//        final Integer code2 = inscClass;
//        String sql = String.format("SELECT INSC_IDENT FROM %s WHERE INSC_IDENT " + operator(code1) + " ? AND INSC_CLASS = ? ORDER BY INSC_IDENT", tableName);
////        LOG.info("pstmt " + sql + " number " + partialInsuranceNumber + " type " + insc_type);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code1);
//            pstmt.setInt(2, code2);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    String obj = rs.getString("INSC_IDENT");
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

    public String findInsNameByInsuranceNumber(String insuranceNumber, final String pCountryEn) {
        final String code = toParam(insuranceNumber);
        String sql = "SELECT INSC_NAME FROM %s WHERE INSC_IDENT " + operator(code) + " ?";
        return fetchSingleValue(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });
    }

    public String findInsShortNameByInsuranceNumber(String insuranceNumber, final String pCountryEn) {
        final String code = toParam(insuranceNumber);
        String sql = "SELECT INSC_SHORT FROM %s WHERE INSC_IDENT " + operator(code) + " ?";
        return fetchSingleValue(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });
    }

    public Collection<String> findMatchesByPhonePrefix(String partialPhonePrefixNumber, String inscType, final String pCountryEn) {
        final String code1 = toParam(partialPhonePrefixNumber) + "%";
        final String code2 = toParam(inscType);
        String sql = "SELECT INSC_PHONE_PREFIX FROM %s WHERE INSC_PHONE_PREFIX " + operator(code1) + " ? AND INSC_SHORT " + operator(code2) + " ? ORDER BY INSC_PHONE_PREFIX";
        return fetchManyValues(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code1);
            pstmt.setString(2, code2);
        });

//        int year = 0;
//        List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code1 = toParam(partialPhonePrefixNumber) + "%";
//        final String code2 = toParam(inscType);
//        String sql = String.format("SELECT INSC_PHONE_PREFIX FROM %s WHERE INSC_PHONE_PREFIX " + operator(code1) + " ? AND INSC_SHORT " + operator(code2) + " ? ORDER BY INSC_PHONE_PREFIX", tableName);
////        LOG.info("pstmt " + sql + " number " + partialInsuranceNumber + " type " + insc_type);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code1);
//            pstmt.setString(2, code2);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    String obj = rs.getString("INSC_PHONE_PREFIX");
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

    public Collection<String> findMatchesByPhonePrefix(String partialPhonePrefixNumber, final String pCountryEn) {
        final String code = toParam(partialPhonePrefixNumber) + "%";
        String sql = "SELECT INSC_PHONE_PREFIX, INSC_NAME, INSC_IDENT FROM %s WHERE INSC_PHONE_PREFIX " + operator(code) + " ? ORDER BY INSC_PHONE_PREFIX";
        return fetchManyValues(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        }, (rs) -> {
            String obj = rs.getString("INSC_PHONE_PREFIX") + " , " + rs.getString("INSC_NAME") + " , " + rs.getString("INSC_IDENT");
            return obj;
        });

//        int year = 0;
//        List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam(partialPhonePrefixNumber) + "%";
//        String sql = String.format("SELECT INSC_PHONE_PREFIX, INSC_NAME, INSC_IDENT FROM %s WHERE INSC_PHONE_PREFIX " + operator(code) + " ? ORDER BY INSC_PHONE_PREFIX", tableName);
////        LOG.info("pstmt " + sql + " number " + partialInsuranceNumber + " type " + insc_type);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    String obj = rs.getString("INSC_PHONE_PREFIX") + " , " + rs.getString("INSC_NAME") + " , " + rs.getString("INSC_IDENT");
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

    public Collection<String> findMatchesByPhoneNo(String partialPhoneNumber, final String pCountryEn) {
        final String code = toParam(partialPhoneNumber) + "%";
        String sql = "SELECT INSC_PHONE, INSC_NAME, INSC_IDENT FROM %s WHERE INSC_PHONE " + operator(code) + " ? ORDER BY INSC_PHONE";
        return fetchManyValues(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam(partialPhoneNumber) + "%";
//        String sql = String.format("SELECT INSC_PHONE, INSC_NAME, INSC_IDENT FROM %s WHERE INSC_PHONE " + operator(code) + " ? ORDER BY INSC_PHONE", tableName);
////        LOG.info("pstmt " + sql + " number " + partialInsuranceNumber + " type " + insc_type);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    String obj = rs.getString("INSC_PHONE");
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

    public Collection<String> findMatchesByFaxNo(String partialFaxNo, final String pCountryEn) {
        final String code = toParam(partialFaxNo) + "%";
        String sql = "SELECT INSC_FAX, INSC_NAME, INSC_IDENT FROM %s WHERE INSC_FAX " + operator(code) + " ? ORDER BY INSC_FAX";
        return fetchManyValues(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam(partialFaxNo) + "%";
//        String sql = String.format("SELECT INSC_FAX, INSC_NAME, INSC_IDENT FROM %s WHERE INSC_FAX " + operator(code) + " ? ORDER BY INSC_FAX", tableName);
////        LOG.info("pstmt " + sql + " number " + partialInsuranceNumber + " type " + insc_type);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    String obj = rs.getString("INSC_FAX");
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

    public Collection<String> findMatchesByAddress(String partialAddress, final String pCountryEn) {
        final String code = toParam(partialAddress) + "%";
        String sql = "SELECT INSC_ADDRESS, INSC_NAME, INSC_IDENT FROM %s WHERE INSC_ADDRESS " + operator(code) + " ? ORDER BY INSC_ADDRESS";
        return fetchManyValues(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        }, (rs) -> {
            String obj = rs.getString("INSC_ADDRESS") + " , " + rs.getString("INSC_NAME") + " , " + rs.getString("INSC_IDENT");
            return obj;
        });

//        int year = 0;
//        List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam(partialAddress) + "%";
//        String sql = String.format("SELECT INSC_ADDRESS, INSC_NAME, INSC_IDENT FROM %s WHERE INSC_ADDRESS " + operator(code) + " ? ORDER BY INSC_ADDRESS", tableName);
////        LOG.info("pstmt " + sql + " number " + partialInsuranceNumber + " type " + insc_type);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    String obj = rs.getString("INSC_ADDRESS") + " , " + rs.getString("INSC_NAME") + " , " + rs.getString("INSC_IDENT");
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

    public Collection<String> getInsuranceTypes(final String pCountryEn) {
        String sql = "SELECT DISTINCT INSC_SHORT FROM %s ORDER BY INSC_SHORT";
        return fetchManyValues(sql, pCountryEn, (pstmt) -> {
            //
        });

//        int year = 0;
//        List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT DISTINCT INSC_SHORT FROM %s ORDER BY INSC_SHORT", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    String obj = rs.getString("INSC_SHORT");
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

    public boolean hasEntry(String insuranceIdent, String pCountryEn) {
        final String code = toParam(insuranceIdent);
        String sql = "SELECT INSC_IDENT FROM %s WHERE INSC_IDENT = ?";
        return super.hasEntry(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT INSC_IDENT FROM %s WHERE INSC_IDENT = ? ", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//
//            pstmt.setString(1, toParam(insuranceIdent));
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

    public boolean hasEntryOfInsName(String insuranceName, String pCountryEn) {
        final String code = toParam(insuranceName);
        String sql = "SELECT INSC_NAME FROM %s WHERE INSC_NAME = ?";
        return super.hasEntry(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT INSC_NAME FROM %s WHERE INSC_NAME = ? ", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//
//            pstmt.setString(1, toParam(insuranceName));
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

    public boolean hasEntryOfShortCode(String shortCode, String pCountryEn) {
        final String code = toParam(shortCode);
        String sql = "SELECT INSC_SHORT FROM %s WHERE INSC_SHORT = ?";
        return super.hasEntry(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT INSC_SHORT FROM %s WHERE INSC_SHORT = ? ", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//
//            pstmt.setString(1, toParam(shortCode));
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

    public boolean hasEntryOfInsCity(String insuranceCity, String pCountryEn) {
        final String code = toParam(insuranceCity);
        String sql = "SELECT INSC_CITY FROM %s WHERE INSC_CITY = ?";
        return super.hasEntry(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT INSC_CITY FROM %s WHERE INSC_CITY = ? ", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//
//            pstmt.setString(1, toParam(insuranceCity));
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

    public boolean hasEntryOfInsZipCode(String insuranceZipCode, String pCountryEn) {
        final String code = toParam(insuranceZipCode);
        String sql = "SELECT INSC_ZIP_CODE FROM %s WHERE INSC_ZIP_CODE = ?";
        return super.hasEntry(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT INSC_ZIP_CODE FROM %s WHERE INSC_ZIP_CODE = ? ", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//
//            pstmt.setString(1, toParam(insuranceZipCode));
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

    public boolean hasEntryOfAddress(String insuranceAddress, String pCountryEn) {
        final String code = toParam(insuranceAddress);
        String sql = "SELECT INSC_ADDRESS FROM %s WHERE INSC_ADDRESS = ?";
        return super.hasEntry(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT INSC_ADDRESS FROM %s WHERE INSC_ADDRESS = ? ", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//
//            pstmt.setString(1, toParam(insuranceAddress));
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

    public boolean hasEntryOfAreaCode(String insuranceAreaCode, String pCountryEn) {
        final String code = toParam(insuranceAreaCode);
        String sql = "SELECT INSC_PHONE_PREFIX FROM %s WHERE INSC_PHONE_PREFIX = ?";
        return super.hasEntry(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT INSC_PHONE_PREFIX FROM %s WHERE INSC_PHONE_PREFIX = ? ", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//
//            pstmt.setString(1, toParam(insuranceAreaCode));
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

    public boolean hasEntryOfTelephone(String insuranceTelephone, String pCountryEn) {
        final String code = toParam(insuranceTelephone);
        String sql = "SELECT INSC_PHONE FROM %s WHERE INSC_PHONE = ?";
        return super.hasEntry(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT INSC_PHONE FROM %s WHERE INSC_PHONE = ? ", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//
//            pstmt.setString(1, toParam(insuranceTelephone));
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

    public boolean hasEntryOfFax(String insuranceFax, String pCountryEn) {
        final String code = toParam(insuranceFax);
        String sql = "SELECT INSC_FAX FROM %s WHERE INSC_FAX = ?";
        return super.hasEntry(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT INSC_FAX FROM %s WHERE INSC_FAX = ? ", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//
//            pstmt.setString(1, toParam(insuranceFax));
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
     * find all matches for given name, type of insurance and country
     *
     * @param partialInsuranceName city of the insurance
     * @param inscType insurance type e.g. BG, IKK, AOK,DAK ...
     * @param pCountryEn country of the insurance
     * @return List of matches as Strings, String contains insuranceName
     */
    public Collection<String> findMatchesByInsuranceName(String partialInsuranceName, String inscType, String pCountryEn) {
        final String code1 = toParam("%" + partialInsuranceName) + "%";
        final String code2 = toParam(inscType);
        String sql = "SELECT INSC_NAME FROM %s WHERE INSC_NAME " + operator(code1) + " ? AND INSC_SHORT " + operator(code2) + " ? ORDER BY INSC_NAME";
        return fetchManyValues(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code1);
            pstmt.setString(2, code2);
        });

//        int year = 0;
//        List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code1 = toParam("%" + partialInsuranceName) + "%";
//        final String code2 = toParam(inscType);
//        String sql = String.format("SELECT INSC_NAME FROM %s WHERE INSC_NAME " + operator(code1) + " ? AND INSC_SHORT " + operator(code2) + " ? ORDER BY INSC_NAME", tableName);
////        LOG.info("pstmt " + sql + " number " + partialInsuranceNumber + " type " + insc_type);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code1);
//            pstmt.setString(2, code2);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    String obj = rs.getString("INSC_NAME");
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
     * find all matches for given name, type of insurance and country
     *
     * @param partialInsuranceName city of the insurance
     * @param inscClass insurance class e.g. BG = 12, ???
     * @param pCountryEn country of the insurance
     * @return List of matches as Strings, String contains insuranceName
     */
    public Collection<String> findMatchesByInsuranceName(String partialInsuranceName, Integer inscClass, String pCountryEn) {
        final String code1 = toParam("%" + partialInsuranceName) + "%";
        final Integer code2 = inscClass;
        String sql = "SELECT INSC_NAME FROM %s WHERE INSC_NAME " + operator(code1) + " ? AND INSC_CLASS = ? ORDER BY INSC_NAME";
        return fetchManyValues(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code1);
            pstmt.setInt(2, code2);
        });

//        int year = 0;
//        List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code1 = toParam("%" + partialInsuranceName) + "%";
//        final Integer code2 = inscClass;
//        String sql = String.format("SELECT INSC_NAME FROM %s WHERE INSC_NAME " + operator(code1) + " ? AND INSC_CLASS = ? ORDER BY INSC_NAME", tableName);
////        LOG.info("pstmt " + sql + " number " + partialInsuranceNumber + " type " + insc_type);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code1);
//            pstmt.setInt(2, code2);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    String obj = rs.getString("INSC_NAME");
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

    public Collection<String> findMatchesByInsuranceName(String partialInsuranceName, String pCountryEn) {
        final String code = toParam("%" + partialInsuranceName) + "%";
        String sql = "SELECT INSC_NAME, INSC_IDENT FROM %s WHERE INSC_NAME " + operator(code) + " ? ORDER BY INSC_NAME";
        return fetchManyValues(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        }, (rs) -> {
            String obj = rs.getString("INSC_NAME") + " , " + rs.getString("INSC_IDENT");
            return obj;
        });

//        int year = 0;
//        List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam("%" + partialInsuranceName) + "%";
//        String sql = String.format("SELECT INSC_NAME, INSC_IDENT FROM %s WHERE INSC_NAME " + operator(code) + " ? ORDER BY INSC_NAME", tableName);
////        LOG.info("pstmt " + sql + " number " + partialInsuranceNumber + " type " + insc_type);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    String obj;
////                    if (rs.getString("INSC_IDENT") != null && !rs.getString("INSC_IDENT").isEmpty()) {
////                        obj = rs.getString("INSC_NAME") + " , " + rs.getString("INSC_IDENT");
////                    } else {
//                    obj = rs.getString("INSC_NAME") + " , " + rs.getString("INSC_IDENT");
////                    }
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
    //RSH 02112017 CPX-642

    /**
     * find all matches for given name
     *
     * @param partialInsuranceName name of the insurance
     * @param pCountryEn country of the insurance
     * @return List of matches as Strings, String contains insuranceName ,
     * insuranceIdent, insuranceCity
     */
    public Collection<String> findMatchesByInsName(String partialInsuranceName, String pCountryEn) {
        final String code = toParam("%" + partialInsuranceName) + "%";
        String sql = "SELECT INSC_NAME, INSC_IDENT, INSC_CITY FROM %s WHERE INSC_NAME " + operator(code) + " ? ORDER BY INSC_NAME";
        return fetchManyValues(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        }, (rs) -> {
            final String obj;
            if (rs.getString("INSC_CITY") != null) {
                obj = rs.getString("INSC_NAME") + " , " + rs.getString("INSC_IDENT") + " , " + rs.getString("INSC_CITY");
            } else {
                obj = rs.getString("INSC_NAME") + " , " + rs.getString("INSC_IDENT");
            }
            return obj;
        });

//        int year = 0;
//        List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam("%" + partialInsuranceName) + "%";
//        String sql = String.format("SELECT INSC_NAME, INSC_IDENT ,INSC_CITY FROM %s WHERE INSC_NAME " + operator(code) + " ? ORDER BY INSC_NAME", tableName);
////        LOG.info("pstmt " + sql + " number " + partialInsuranceNumber + " type " + insc_type);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    String obj;
//                    if (rs.getString("INSC_CITY") != null) {
//                        obj = rs.getString("INSC_NAME") + " , " + rs.getString("INSC_IDENT") + " , " + rs.getString("INSC_CITY");
//                    } else {
//                        obj = rs.getString("INSC_NAME") + " , " + rs.getString("INSC_IDENT");
//                    }
//
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
     * find all matches for given City
     *
     * @param partialInsuranceCity city of the insurance
     * @param pCountryEn country of the insurance
     * @return List of matches as Strings, String contains insuranceCity
     * ,insuranceName , insuranceIdent
     */
    public Collection<String> findMatchesByCity(String partialInsuranceCity, String pCountryEn) {
        final String code = toParam("%" + partialInsuranceCity) + "%";
        String sql = "SELECT INSC_CITY, INSC_NAME, INSC_IDENT FROM %s WHERE INSC_CITY " + operator(code) + " ? ORDER BY INSC_CITY";
        return fetchManyValues(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        }, (rs) -> {
            String obj = rs.getString("INSC_CITY") + " , " + rs.getString("INSC_NAME") + " , " + rs.getString("INSC_IDENT");
            return obj;
        });

//        int year = 0;
//        List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam("%" + partialInsuranceCity) + "%";
//        String sql = String.format("SELECT INSC_CITY,INSC_NAME,INSC_IDENT FROM %s WHERE INSC_CITY " + operator(code) + " ? ORDER BY INSC_CITY", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    String obj = rs.getString("INSC_CITY") + " , " + rs.getString("INSC_NAME") + " , " + rs.getString("INSC_IDENT");
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
     * find all matches for given zipcode, type of insurance and country
     *
     * @param partialInsuranceZipCode zipcode of the insurance
     * @param pCountryEn country of the insurance
     * @return List of matches as Strings, String contains Zipcode and Ins Name
     */
    public Collection<String> findMatchesByInsZipCode(String partialInsuranceZipCode, String pCountryEn) {
        final String code = toParam("%" + partialInsuranceZipCode) + "%";
        String sql = "SELECT INSC_ZIP_CODE,INSC_NAME,INSC_IDENT FROM %s WHERE INSC_ZIP_CODE " + operator(code) + " ? ORDER BY INSC_ZIP_CODE";
        return fetchManyValues(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        }, (rs) -> {
            String obj = rs.getString("INSC_ZIP_CODE") + " , " + rs.getString("INSC_NAME") + " , " + rs.getString("INSC_IDENT");
            return obj;
        });

//        int year = 0;
//        List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam("%" + partialInsuranceZipCode) + "%";
//        String sql = String.format("SELECT INSC_ZIP_CODE,INSC_NAME,INSC_IDENT FROM %s WHERE INSC_ZIP_CODE " + operator(code) + " ? ORDER BY INSC_ZIP_CODE", tableName);
////        String sql = "SELECT INSC_ZIP_CODE, INSC_NAME, INSC_CITY, INSC_ADDRESS FROM %s WHERE INSC_ZIP_CODE " + operator(code) + " ? ORDER BY INSC_ZIP_CODE";
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
////                    String obj = rs.getString("INSC_ZIP_CODE") + " , " + rs.getString("INSC_NAME") + " , " + rs.getString("INSC_CITY") + " , " + rs.getString("INSC_ADDRESS");
//                    String obj = rs.getString("INSC_ZIP_CODE") + " , " + rs.getString("INSC_NAME") + " , " + rs.getString("INSC_IDENT");
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

    public Collection<String> findMatchesByInsuranceZipCode(String partialInsuranceZipCode, String inscType, String pCountryEn) {
        final String code1 = toParam("%" + partialInsuranceZipCode) + "%";
        final String code2 = toParam(inscType);
        String sql = "SELECT INSC_ZIP_CODE, INSC_NAME FROM %s WHERE INSC_ZIP_CODE " + operator(code1) + " ? AND INSC_SHORT " + operator(code2) + " ? ORDER BY INSC_NAME";
        return fetchManyValues(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code1);
            pstmt.setString(2, code2);
        }, (rs) -> {
            String obj = rs.getString("INSC_ZIP_CODE") + " - " + rs.getString("INSC_NAME");
            return obj;
        });

//        int year = 0;
//        List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code1 = toParam("%" + partialInsuranceZipCode) + "%";
//        final String code2 = toParam(inscType);
//        String sql = String.format("SELECT INSC_ZIP_CODE,INSC_NAME FROM %s WHERE INSC_ZIP_CODE " + operator(code1) + " ? AND INSC_SHORT " + operator(code2) + " ? ORDER BY INSC_NAME", tableName);
////        LOG.info("pstmt " + sql + " number " + partialInsuranceNumber + " type " + insc_type);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code1);
//            pstmt.setString(2, code2);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    String obj = rs.getString("INSC_ZIP_CODE") + " - " + rs.getString("INSC_NAME");
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

    public Collection<String> findMatchesByInsuranceZipCode(String partialInsuranceZipCode, Integer inscClass, String pCountryEn) {
        final String code1 = toParam("%" + partialInsuranceZipCode) + "%";
        final Integer code2 = inscClass;
        String sql = "SELECT INSC_ZIP_CODE, INSC_NAME FROM %s WHERE INSC_ZIP_CODE " + operator(code1) + " ? AND INSC_CLASS = ? ORDER BY INSC_NAME";
        return fetchManyValues(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code1);
            pstmt.setInt(2, code2);
        }, (rs) -> {
            String obj = rs.getString("INSC_ZIP_CODE") + " - " + rs.getString("INSC_NAME");
            return obj;
        });

//        int year = 0;
//        List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code1 = toParam("%" + partialInsuranceZipCode) + "%";
//        final Integer code2 = inscClass;
//        String sql = String.format("SELECT INSC_ZIP_CODE,INSC_NAME FROM %s WHERE INSC_ZIP_CODE " + operator(code1) + " ? AND INSC_CLASS = ? ORDER BY INSC_NAME", tableName);
////        LOG.info("pstmt " + sql + " number " + partialInsuranceNumber + " type " + insc_type);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code1);
//            pstmt.setInt(2, code2);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    String obj = rs.getString("INSC_ZIP_CODE") + " - " + rs.getString("INSC_NAME");
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
     * find all matches for given city, type of insurance and country
     *
     * @param partialInsuranceCity city of the insurance
     * @param inscType insurance type e.g. BG, IKK, AOK,DAK ...
     * @param pCountryEn country of the insurance
     * @return List of matches as Strings, String contains City and Ins Name
     */
    public Collection<String> findMatchesByInsuranceCity(String partialInsuranceCity, String inscType, String pCountryEn) {
        final String code1 = toParam("%" + partialInsuranceCity) + "%";
        final String code2 = toParam(inscType);
        String sql = "SELECT INSC_CITY, INSC_NAME FROM %s WHERE INSC_CITY " + operator(code1) + " ? AND INSC_SHORT " + operator(code2) + " ? ORDER BY INSC_NAME";
        return fetchManyValues(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code1);
            pstmt.setString(2, code2);
        }, (rs) -> {
            String obj = rs.getString("INSC_CITY") + " - " + rs.getString("INSC_NAME");
            return obj;
        });

//        int year = 0;
////        List<String> objList = new ArrayList<>();
//        List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code1 = toParam("%" + partialInsuranceCity) + "%";
//        final String code2 = toParam(inscType);
//        String sql = String.format("SELECT INSC_CITY,INSC_NAME FROM %s WHERE INSC_CITY " + operator(code1) + " ? AND INSC_SHORT " + operator(code2) + " ? ORDER BY INSC_NAME", tableName);
////        LOG.info("pstmt " + sql + " number " + partialInsuranceNumber + " type " + insc_type);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code1);
//            pstmt.setString(2, code2);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    String obj = rs.getString("INSC_CITY") + " - " + rs.getString("INSC_NAME");
////              CpxInsuranceCompany obj = toCpxObject(rs);
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
     * find all matches for given city, type of insurance and country
     *
     * @param partialInsuranceCity city of the insurance
     * @param inscClass insurance class e.g. 12=BG... rest ???
     * @param pCountryEn country of the insurance
     * @return List of matches as Strings, String contains City and Ins Name
     */
    public Collection<String> findMatchesByInsuranceCity(String partialInsuranceCity, Integer inscClass, String pCountryEn) {
        final String code1 = toParam("%" + partialInsuranceCity) + "%";
        final Integer code2 = inscClass;
        String sql = "SELECT INSC_CITY, INSC_NAME FROM %s WHERE INSC_CITY " + operator(code1) + " ? AND INSC_CLASS = ? ORDER BY INSC_NAME";
        return fetchManyValues(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code1);
            pstmt.setInt(2, code2);
        }, (rs) -> {
            String obj = rs.getString("INSC_CITY") + " - " + rs.getString("INSC_NAME");
            return obj;
        });

//        int year = 0;
////        List<String> objList = new ArrayList<>();
//        List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code1 = toParam("%" + partialInsuranceCity) + "%";
//        final Integer code2 = inscClass;
//        String sql = String.format("SELECT INSC_CITY,INSC_NAME FROM %s WHERE INSC_CITY " + operator(code1) + " ? AND INSC_CLASS = ? ORDER BY INSC_NAME", tableName);
////        LOG.info("pstmt " + sql + " number " + partialInsuranceNumber + " type " + insc_type);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code1);
//            pstmt.setInt(2, code2);//toParam(insc_type));
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    String obj = rs.getString("INSC_CITY") + " - " + rs.getString("INSC_NAME");
////              CpxInsuranceCompany obj = toCpxObject(rs);
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

    public Collection<String> findMatchesByInsuranceCity(String partialInsuranceCity, String pCountryEn) {
        final String code = toParam("%" + partialInsuranceCity) + "%";
        String sql = "SELECT INSC_CITY, INSC_NAME, INSC_IDENT FROM %s WHERE INSC_CITY " + operator(code) + " ? ORDER BY INSC_CITY";
        return fetchManyValues(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        }, (rs) -> {
            String obj = rs.getString("INSC_CITY") + " , " + rs.getString("INSC_NAME") + " , " + rs.getString("INSC_IDENT");
            return obj;
        });

//        int year = 0;
//        List<String> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam("%" + partialInsuranceCity) + "%";
//        String sql = String.format("SELECT INSC_CITY, INSC_NAME, INSC_IDENT FROM %s WHERE INSC_CITY " + operator(code) + " ? ORDER BY INSC_CITY", tableName);
////        String sql = "SELECT INSC_CITY, INSC_NAME, INSC_ADDRESS, INSC_ZIP_CODE FROM %s WHERE INSC_CITY " + operator(code) + " ? ORDER BY INSC_CITY";
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
////                    String obj = rs.getString("INSC_CITY") + " , " + rs.getString("INSC_NAME") + " , " + rs.getString("INSC_ADDRESS") + " , " + rs.getString("INSC_ZIP_CODE");
//                    String obj = rs.getString("INSC_CITY") + " , " + rs.getString("INSC_NAME") + " , " + rs.getString("INSC_IDENT");
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
     * find CpxInsurance by its name, returns first result from the db
     *
     * @param inscName name of the insurance
     * @param pCountryEn country
     * @return first result fetched from the database
     */
    public CpxInsuranceCompany getByName(String inscName, String pCountryEn) {
        final String code = inscName;
        String sql = "SELECT * FROM %s WHERE INSC_NAME = ?";
        return fetchSingle(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = 0;
//        CpxInsuranceCompany obj = getNewObject();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
////        String str[] = inscName.split(",");
//        String sql = String.format("SELECT * FROM %s WHERE INSC_NAME = ?", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
////            pstmt.setString(1, str[0].trim());
//            pstmt.setString(1, inscName);
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

    public CpxInsuranceCompany getByName(String inscName, String inscShort, String pCountryEn) {
        final String code1 = inscName;
        final String code2 = inscShort;
        String sql = "SELECT * FROM %s WHERE INSC_NAME " + operator(code1) + " ? AND INSC_SHORT " + operator(code2) + " ?";
        return fetchSingle(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, code1);
            pstmt.setString(2, code2);
        });

//        int year = 0;
//        CpxInsuranceCompany obj = getNewObject();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code1 = inscName;
//        final String code2 = inscShort;
//        String sql = String.format("SELECT * FROM %s WHERE INSC_NAME " + operator(code1) + " ? AND INSC_SHORT " + operator(code2) + " ?", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code1);
//            pstmt.setString(2, code2);
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

    public CpxInsuranceCompany getByCityInsName(String city, String inscName, String pCountryEn) {
        String sql = "SELECT * FROM %s WHERE INSC_CITY = ? AND INSC_NAME = ?";
        return fetchSingle(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, city);
            pstmt.setString(2, inscName);
        });

//        int year = 0;
//        CpxInsuranceCompany obj = getNewObject();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT * FROM %s WHERE INSC_CITY = ? AND INSC_NAME = ?", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, city);
//            pstmt.setString(2, inscName);
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

    public CpxInsuranceCompany getByCityInsNameIdent(String city, String inscName, String inscIdent, String pCountryEn) {
        String sql = "SELECT * FROM %s WHERE INSC_CITY = ? AND INSC_NAME = ? AND INSC_IDENT = ?";
        return fetchSingle(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, city);
            pstmt.setString(2, inscName);
            pstmt.setString(3, inscIdent);
        });

//        int year = 0;
//        CpxInsuranceCompany obj = getNewObject();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT * FROM %s WHERE INSC_CITY = ? AND INSC_NAME = ? AND INSC_IDENT = ?", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, city);
//            pstmt.setString(2, inscName);
//            pstmt.setString(3, inscIdent);
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

//    public CpxInsuranceCompany getByCityInsNameAddressZip(String city, String inscName, String address, String zip, String pCountryEn) {
//        int year = 0;
//        CpxInsuranceCompany obj = getNewObject();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT * FROM %s WHERE INSC_CITY = ? AND INSC_NAME = ? AND INSC_ADDRESS = ? AND INSC_ZIP_CODE = ?", tableName);
//        try (PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, city);
//            pstmt.setString(2, inscName);
//            pstmt.setString(3, address);
//            pstmt.setString(4, zip);
//            try (ResultSet rs = pstmt.executeQuery()) {
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
    public CpxInsuranceCompany getByZipInsNameIdent(String zip, String inscName, String ident, String pCountryEn) {
        String sql = "SELECT * FROM %s WHERE INSC_ZIP_CODE = ? AND INSC_NAME = ? AND INSC_IDENT = ? ";
        return fetchSingle(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, zip);
            pstmt.setString(2, inscName);
            pstmt.setString(3, ident);
        });

//        int year = 0;
//        CpxInsuranceCompany obj = getNewObject();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT * FROM %s WHERE INSC_ZIP_CODE = ? AND INSC_NAME = ? AND INSC_IDENT = ? ", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, zip);
//            pstmt.setString(2, inscName);
//            pstmt.setString(3, ident);
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

    public CpxInsuranceCompany getByPhonePrefixNameIdent(String prefix, String inscName, String ident, String pCountryEn) {
        String sql = "SELECT * FROM %s WHERE INSC_PHONE_PREFIX = ? AND INSC_NAME = ? AND INSC_IDENT = ? ";
        return fetchSingle(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, prefix);
            pstmt.setString(2, inscName);
            pstmt.setString(3, ident);
        });

//        int year = 0;
//        CpxInsuranceCompany obj = getNewObject();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT * FROM %s WHERE INSC_PHONE_PREFIX = ? AND INSC_NAME = ? AND INSC_IDENT = ? ", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, prefix);
//            pstmt.setString(2, inscName);
//            pstmt.setString(3, ident);
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

    public CpxInsuranceCompany getByAddressNameIdent(String address, String inscName, String ident, String pCountryEn) {
        String sql = "SELECT * FROM %s WHERE INSC_ADDRESS = ? AND INSC_NAME = ? AND INSC_IDENT = ? ";
        return fetchSingle(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, address);
            pstmt.setString(2, inscName);
            pstmt.setString(3, ident);
        });

//        int year = 0;
//        CpxInsuranceCompany obj = getNewObject();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT * FROM %s WHERE INSC_ADDRESS = ? AND INSC_NAME = ? AND INSC_IDENT = ? ", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, address);
//            pstmt.setString(2, inscName);
//            pstmt.setString(3, ident);
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

    public CpxInsuranceCompany getByPhoneNo(String phoneNo, String pCountryEn) {
        String sql = "SELECT * FROM %s WHERE INSC_PHONE = ? ";
        return fetchSingle(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, phoneNo);
        });

//        int year = 0;
//        CpxInsuranceCompany obj = getNewObject();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT * FROM %s WHERE INSC_PHONE = ? ", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, phoneNo);
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

    public CpxInsuranceCompany getByFaxNo(String faxNo, String pCountryEn) {
        String sql = "SELECT * FROM %s WHERE INSC_FAX = ? ";
        return fetchSingle(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, faxNo);
        });

//        int year = 0;
//        CpxInsuranceCompany obj = getNewObject();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT * FROM %s WHERE INSC_FAX = ? ", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, faxNo);
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

    public CpxInsuranceCompany getByPhonePrefix(String phonePrefix, String pCountryEn) {
        String sql = "SELECT * FROM %s WHERE INSC_PHONE_PREFIX = ?";
        return fetchSingle(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, phonePrefix);
        });

//        int year = 0;
//        CpxInsuranceCompany obj = getNewObject();
//
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT * FROM %s WHERE INSC_PHONE_PREFIX = ?", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, phonePrefix);
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

//    public String getTooltipText(String insCompanyIdent, String pCountry) {
//        CpxInsuranceCompany company = getByCode(insCompanyIdent, pCountry);
//        return company.toString();
//    }
    public CInsuranceCompany getByCity(String city, String pCountryEn) {
        String sql = "SELECT * FROM %s WHERE INSC_CITY = ?";
        return fetchSingle(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, city);
        });

//        int year = 0;
//        CpxInsuranceCompany obj = getNewObject();
//
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT * FROM %s WHERE INSC_CITY = ?", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, city);
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

    public CInsuranceCompany getByAddress(String address, String pCountryEn) {
        String sql = "SELECT * FROM %s WHERE INSC_ADDRESS = ?";
        return fetchSingle(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, address);
        });

//        int year = 0;
//        CpxInsuranceCompany obj = getNewObject();
//
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT * FROM %s WHERE INSC_ADDRESS = ?", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, address);
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

    public CpxInsuranceCompany getByIdent(String ident, String pCountryEn) {
        String sql = "SELECT * FROM %s WHERE INSC_IDENT = ?";
        return fetchSingle(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, ident);
        });

//        int year = 0;
//        CpxInsuranceCompany obj = getNewObject();
//
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT * FROM %s WHERE INSC_IDENT = ?", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, ident);
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

    public CInsuranceCompany getByZip(String zipCode, String pCountryEn) {
        String sql = "SELECT * FROM %s WHERE INSC_ZIP_CODE = ?";
        return fetchSingle(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, zipCode);
        });

//        int year = 0;
//        CpxInsuranceCompany obj = getNewObject();
//
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT * FROM %s WHERE INSC_ZIP_CODE = ?", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, zipCode);
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

    public CpxInsuranceCompany getInsuranceDetailByIdent(String pInsIdent, String pCountryEn) {
        final String str[] = pInsIdent.split(",");
        String sql = "SELECT * FROM %s WHERE INSC_IDENT = ? AND INSC_NAME = ? AND INSC_CITY = ?";
        return fetchSingle(sql, pCountryEn, (pstmt) -> {
            pstmt.setString(1, str[0].trim());
            pstmt.setString(2, str[1].trim());
            pstmt.setString(3, str[2].trim());
        });

//        int year = 0;
//        CpxInsuranceCompany obj = getNewObject();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        String sql = String.format("SELECT * FROM %s WHERE INSC_IDENT = ? AND INSC_NAME = ? AND INSC_CITY = ? ", tableName);
//        String str[] = pInsIdent.split(",");
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

    @Override
    public CpxInsuranceCompany getNewObject() {
        return new CpxInsuranceCompany();
    }

}
