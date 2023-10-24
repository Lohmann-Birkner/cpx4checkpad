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
import de.lb.cpx.model.enums.CpxEnumInterface;
import de.lb.cpx.model.enums.GrouperMdcOrSkEn;
import de.lb.cpx.server.commonDB.model.CPeppCatalog;
import de.lb.cpx.service.information.CatalogTypeEn;
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
public class CpxPeppCatalog extends AbstractCpxCatalog<CpxPepp, CPeppCatalog> {

    private static CpxPeppCatalog instance = null;
    public static final CatalogTypeEn CATALOG = CatalogTypeEn.PEPP;

    public static synchronized CpxPeppCatalog instance() {
        if (instance == null) {
            instance = new CpxPeppCatalog();
        }
        return instance;
    }
    //Logger log = Logger.getLogger(getClass().getSimpleName());

    private CpxPeppCatalog() {

    }

    @Override
    public void fillCatalog(final CpxCatalogOverview pCatalogOverview, final List<CPeppCatalog> pList) throws SQLException {
        final String tableName = getTableName(pCatalogOverview);
        String sqlHead = String.format("INSERT INTO %s(ID, CREATION_DATE, MODIFICATION_DATE, COUNTRY_EN, MDC_SK_EN, PEPP_DESCRIPTION, PEPP_PEPP, ", tableName)
                + " PEPP_HAS_CLASSES_FL, PEPP_IS_DAY_CARE_FL, PEPP_IS_NEGOTIATED_FL, PEPP_RELATION_COST_WEIGHT, "
                + " PEPP_RELATION_FROM, PEPP_RELATION_NUMBER, PEPP_RELATION_TO, PEPP_VALID_FROM, PEPP_VALID_TO, "
                + " PEPP_YEAR, PEPP_HOS_IDENT) ";
        StringBuilder sql = null;
        //String sql = "INSERT INTO PEPP_CATALOG(ID, CREATION_DATE, MODIFICATION_DATE, COUNTRY_EN, PEPP_CODE, PEPP_IS_COMPLETE_FL, PEPP_PARENT_ID, PEPP_YEAR, PEPP_DESCRIPTION, PEPP_EXCLUSION, PEPP_INCLUSION, PEPP_NOTE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int i = 0;
        boolean first = true;
        for (CPeppCatalog peppCatalog : pList) {
            i++;
            if (first) {
                sql = new StringBuilder(sqlHead);
                sql.append(" SELECT ");
                first = false;
            } else {
                sql.append(" UNION ALL SELECT ");
            }
            sql.append(peppCatalog.getId() + ", ");
            sql.append(QStr(peppCatalog.getCreationDate()) + ", ");
            sql.append(QStr(peppCatalog.getModificationDate()) + ", ");
            sql.append(QStr(peppCatalog.getCountryEn().name()) + ", ");
            sql.append(QStr((peppCatalog.getCMdcSkCatalog() == null) ? null : peppCatalog.getCMdcSkCatalog().getViewId()) + ", ");
            sql.append(QStr(peppCatalog.getPeppDescription()) + ", ");
            sql.append(QStr(peppCatalog.getPeppPepp()) + ", ");
            sql.append(QStr(peppCatalog.getPeppHasClassesFl()) + ", ");
            sql.append(QStr(peppCatalog.getPeppIsDayCareFl()) + ", ");
            sql.append(QStr(peppCatalog.getPeppIsNegotiatedFl()) + ", ");
            sql.append(QStr(peppCatalog.getPeppRelationCostWeight()) + ", ");
            sql.append(QStr(peppCatalog.getPeppRelationFrom()) + ", ");
            sql.append(QStr(peppCatalog.getPeppRelationNumber()) + ", ");
            sql.append(QStr(peppCatalog.getPeppRelationTo()) + ", ");
            sql.append(QStr(peppCatalog.getPeppValidFrom()) + ", ");
            sql.append(QStr(peppCatalog.getPeppValidTo()) + ", ");
            sql.append(QStr(peppCatalog.getPeppYear()) + ", ");
            sql.append(QStr(peppCatalog.getPeppHosIdent()));
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
                    + "(ID                         INTEGER    PRIMARY KEY,"
                    + " CREATION_DATE              DATETIME, "
                    + " MODIFICATION_DATE          DATETIME, "
                    + " COUNTRY_EN                 VARCHAR(25), "
                    + " MDC_SK_EN	                VARCHAR(25), "
                    + " PEPP_HOS_IDENT	            VARCHAR(10), "
                    + " PEPP_DESCRIPTION	          VARCHAR(1000), "
                    + " PEPP_HAS_CLASSES_FL        TINYINT NOT NULL, "
                    + " PEPP_IS_DAY_CARE_FL	      TINYINT NOT NULL, "
                    + " PEPP_IS_NEGOTIATED_FL      TINYINT NOT NULL, "
                    + " PEPP_PEPP	                VARCHAR(10), "
                    + " PEPP_RELATION_COST_WEIGHT  DECIMAL(10,4), "
                    + " PEPP_RELATION_FROM	        DECIMAL(10,0), "
                    + " PEPP_RELATION_NUMBER  	    DECIMAL(10,0), "
                    + " PEPP_RELATION_TO	          DECIMAL(10,0), "
                    + " PEPP_VALID_FROM	          DATETIME, "
                    + " PEPP_VALID_TO	            DATETIME, "
                    + " PEPP_YEAR	                INT ) ";
            stmt.executeUpdate(sql);

            sql = String.format("CREATE INDEX IF NOT EXISTS IDX_PEPP ON %s (PEPP_PEPP)", tableName); //COUNTRY_EN, PEPP_YEAR
            stmt.executeUpdate(sql);

            stmt.close();
            return tableName;
        } catch (SQLException ex) {
            Logger.getLogger(CpxPeppCatalog.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    @Override
    public CpxPepp toCpxObject(final ResultSet rs) throws SQLException {
        CpxPepp obj = getNewObject();
        if (rs == null) {
            return obj;
        }
        String mdc = rs.getString("MDC_SK_EN");
        GrouperMdcOrSkEn grouperMdcOrSkEn = null;
        if (mdc != null && !mdc.isEmpty()) {
            try {
                grouperMdcOrSkEn = (GrouperMdcOrSkEn) CpxEnumInterface.findEnum(GrouperMdcOrSkEn.values(), mdc);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(CpxPeppCatalog.class.getName()).log(Level.SEVERE, "Invalid MDC defined!", ex);
            }
        }

        obj.setId(rs.getLong("ID"));
        //obj.setCreationDate(CpxDateParser.parseDateTime(rs.getString("CREATION_DATE")));
        //obj.setModificationDate(CpxDateParser.parseDateTime(rs.getString("MODIFICATION_DATE")));
        //obj.setCountryEn(CountryEn.valueOf(rs.getString("COUNTRY_EN")));
        obj.setCMdcSkCatalog(grouperMdcOrSkEn);
        obj.setPeppDescription(rs.getString("PEPP_DESCRIPTION"));
        obj.setPeppPepp(rs.getString("PEPP_PEPP"));
        obj.setPeppHasClassesFl(rs.getBoolean("PEPP_HAS_CLASSES_FL"));
        obj.setPeppIsDayCareFl(rs.getBoolean("PEPP_IS_DAY_CARE_FL"));
        obj.setPeppIsNegotiatedFl(rs.getBoolean("PEPP_IS_NEGOTIATED_FL"));
        obj.setPeppRelationCostWeight(rs.getBigDecimal("PEPP_RELATION_COST_WEIGHT"));
        obj.setPeppRelationFrom(rs.getInt("PEPP_RELATION_FROM"));
        obj.setPeppRelationNumber(rs.getInt("PEPP_RELATION_NUMBER"));
        obj.setPeppRelationTo(rs.getInt("PEPP_RELATION_TO"));
        obj.setPeppValidFrom(CpxDateParser.parseDateTime(rs.getString("PEPP_VALID_FROM")));
        obj.setPeppValidTo(CpxDateParser.parseDateTime(rs.getString("PEPP_VALID_TO")));
        obj.setPeppYear(rs.getInt("PEPP_YEAR"));
        obj.setPeppHosIdent(rs.getString("PEPP_HOS_IDENT"));
        return obj;
    }

//    /**
//     * Returns Dummy-Object if PEPP Code cannot be found in local SQLite-DB
//     *
//     * @param pCode PEPP Code
//     * @param pCountryEn Country ("de", "en")
//     * @param pYear Year
//     * @return PEPP
//     */
//    @Override
//    public CpxPepp getByCode(final String pCode, final String pCountryEn, final Integer pYear) {
//        List<CpxPepp> list = findManyByCode(pCode, pCountryEn, pYear);
//        if (list.isEmpty()) {
//            return getNewObject();
//        }
//        return list.get(0);
//    }
//    /**
//     * Returns Dummy-Object if PEPP Code cannot be found in local SQLite-DB
//     *
//     * @param pId ID
//     * @param pCountryEn Country ("de", "en")
//     * @param pYear Year
//     * @return PEPP
//     */
//    @Override
//    public CpxPepp getById(final Long pId, final String pCountryEn, final Integer pYear) {
//        int year = checkYear(pYear);
//        CpxPepp obj = getNewObject();
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
//            Logger.getLogger(CpxPeppCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return obj;
//    }
    /**
     * Returns Dummy-Object if PEPP Code cannot be found in local SQLite-DB
     *
     * @param pCode PEPP-Code
     * @param pCountryEn Country ("de", "en")
     * @param pYear Year
     * @return PEPP
     */
    @Override
    public List<CpxPepp> findManyByCode(final String pCode, final String pCountryEn, final Integer pYear) {
        final String code = toParam(pCode);
        String sql = "SELECT * FROM %s WHERE PEPP_PEPP " + operator(code) + " ? ORDER BY PEPP_PEPP";
        return fetchMany(sql, pCountryEn, pYear, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = checkYear(pYear);
//        List<CpxPepp> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam(pCode);
//        String sql = String.format("SELECT * FROM %s WHERE PEPP_PEPP " + operator(code) + " ? ORDER BY PEPP_PEPP", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    CpxPepp obj = toCpxObject(rs);
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
//            Logger.getLogger(CpxPeppCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return objList;
    }

    @Override
    public CatalogTypeEn getCatalogType() {
        return CATALOG;
    }

    /**
     * Search for PEPP Catalog entry end creates Pepp Description String.
     * Layout: PEPP-Code : PEPP-Description
     *
     * @param grpresCode Pepp code from the grouper
     * @param country country where drg result is valid
     * @param year year of validity
     * @return Description String
     */
    public String getPeppDescription(String grpresCode, String country, Integer year) {
        CpxPepp catalogEntry = getByCode(grpresCode, "de", year);
        String description = catalogEntry.getPeppDescription() == null ? "----" : catalogEntry.getPeppDescription();
        return grpresCode + " : " + description;
    }

    /**
     * Search PEPP Catalog entry and creates PEPP Description String only.
     * Layout: PEPP-Description
     *
     * @param grpresCode pepp code from the grouper
     * @param country country where drg result is valid
     * @param year year of validity
     * @return Description String
     */
    public String getPeppDecriptionText(String grpresCode, String country, Integer year) {
        CpxPepp catalogEntry = getByCode(grpresCode, "de", year);
        String description = catalogEntry.getPeppDescription() == null ? "----" : catalogEntry.getPeppDescription();
        return description;
    }

    @Override
    public CpxPepp getNewObject() {
        return new CpxPepp();
    }

}
