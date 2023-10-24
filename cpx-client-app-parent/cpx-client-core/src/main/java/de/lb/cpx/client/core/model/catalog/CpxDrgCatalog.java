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
import de.lb.cpx.model.enums.DrgPartitionEn;
import de.lb.cpx.model.enums.GrouperMdcOrSkEn;
import de.lb.cpx.server.commonDB.model.CDrgCatalog;
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
public class CpxDrgCatalog extends AbstractCpxCatalog<CpxDrg, CDrgCatalog> {

    private static CpxDrgCatalog instance = null;
    public static final CatalogTypeEn CATALOG = CatalogTypeEn.DRG;

    public static synchronized CpxDrgCatalog instance() {
        if (instance == null) {
            instance = new CpxDrgCatalog();
        }
        return instance;
    }
    //Logger log = Logger.getLogger(getClass().getSimpleName());

    private CpxDrgCatalog() {

    }

    @Override
    public void fillCatalog(final CpxCatalogOverview pCatalogOverview, final List<CDrgCatalog> pList) throws SQLException {
        final String tableName = getTableName(pCatalogOverview);
        String sqlHead = String.format("INSERT INTO %s(ID, CREATION_DATE, MODIFICATION_DATE, COUNTRY_EN, MDC_SK_EN, DRG_DESCRIPTION, DRG_DRG, ", tableName)
                + " DRG_EO_1_DEDUCTION_DAY, DRG_EO_1_SURCHARGE_DAY, DRG_EO_ALOS, DRG_EO_CW, DRG_EO_CW_DEDUCTION, "
                + " DRG_EO_CW_SURCHARGE, DRG_EO_CW_TRANSF_DEDUCT, DRG_EO_IS_READM_FL, DRG_EO_IS_TRANSFER_FL, "
                + " DRG_EOA_CW, DRG_EOAM_CW, DRG_EOM_CW, DRG_IS_DAY_CARE_FL, DRG_IS_NEGOTIATED_FL, "
                + " DRG_MD_1_DEDUCTION_DAY, DRG_MD_1_SURCHARGE_DAY, DRG_MD_ALOS, DRG_MD_CW, DRG_MD_CW_DEDUCTION, "
                + " DRG_MD_CW_SURCHARGE, DRG_MD_CW_TRANSF_DEDUCT, DRG_MD_IS_READM_FL, DRG_MD_IS_TRANSFER_FL, "
                + " DRG_MDM_CW, DRG_PARTITION_EN, DRG_VALID_FROM, DRG_VALID_TO, DRG_YEAR, DRG_HOS_IDENT) ";
        StringBuilder sql = null;
        //String sql = "INSERT INTO DRG_CATALOG(ID, CREATION_DATE, MODIFICATION_DATE, COUNTRY_EN, DRG_CODE, DRG_IS_COMPLETE_FL, DRG_PARENT_ID, DRG_YEAR, DRG_DESCRIPTION, DRG_EXCLUSION, DRG_INCLUSION, DRG_NOTE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int i = 0;
        boolean first = true;
        for (CDrgCatalog drgCatalog : pList) {
            i++;
            if (first) {
                sql = new StringBuilder(sqlHead);
                sql.append(" SELECT ");
                first = false;
            } else {
                sql.append(" UNION ALL SELECT ");
            }
            sql.append(drgCatalog.getId() + ", ");
            sql.append(QStr(drgCatalog.getCreationDate()) + ", ");
            sql.append(QStr(drgCatalog.getModificationDate()) + ", ");
            sql.append(QStr(drgCatalog.getCountryEn().name()) + ", ");
            sql.append(QStr((drgCatalog.getCMdcSkCatalog() == null) ? null : drgCatalog.getCMdcSkCatalog().getViewId()) + ", ");
            sql.append(QStr(drgCatalog.getDrgDescription()) + ", ");
            sql.append(QStr(drgCatalog.getDrgDrg()) + ", ");
            sql.append(QStr(drgCatalog.getDrgEo1DeductionDay()) + ", ");
            sql.append(QStr(drgCatalog.getDrgEo1SurchargeDay()) + ", ");
            sql.append(QStr(drgCatalog.getDrgEoAlos()) + ", ");
            sql.append(QStr(drgCatalog.getDrgEoCw()) + ", ");
            sql.append(QStr(drgCatalog.getDrgEoCwDeduction()) + ", ");
            sql.append(QStr(drgCatalog.getDrgEoCwSurcharge()) + ", ");
            sql.append(QStr(drgCatalog.getDrgEoCwTransfDeduct()) + ", ");
            sql.append(QStr(drgCatalog.getDrgEoIsReadmFl()) + ", ");
            sql.append(QStr(drgCatalog.getDrgEoIsTransferFl()) + ", ");
            sql.append(QStr(drgCatalog.getDrgEoaCw()) + ", ");
            sql.append(QStr(drgCatalog.getDrgEoamCw()) + ", ");
            sql.append(QStr(drgCatalog.getDrgEomCw()) + ", ");
            sql.append(QStr(drgCatalog.getDrgIsDayCareFl()) + ", ");
            sql.append(QStr(drgCatalog.getDrgIsNegotiatedFl()) + ", ");
            sql.append(QStr(drgCatalog.getDrgMd1DeductionDay()) + ", ");
            sql.append(QStr(drgCatalog.getDrgMd1SurchargeDay()) + ", ");
            sql.append(QStr(drgCatalog.getDrgMdAlos()) + ", ");
            sql.append(QStr(drgCatalog.getDrgMdCw()) + ", ");
            sql.append(QStr(drgCatalog.getDrgMdCwDeduction()) + ", ");
            sql.append(QStr(drgCatalog.getDrgMdCwSurcharge()) + ", ");
            sql.append(QStr(drgCatalog.getDrgMdCwTransfDeduct()) + ", ");
            sql.append(QStr(drgCatalog.getDrgMdIsReadmFl()) + ", ");
            sql.append(QStr(drgCatalog.getDrgMdIsTransferFl()) + ", ");
            sql.append(QStr(drgCatalog.getDrgMdMCw()) + ", ");
            sql.append(QStr((drgCatalog.getDrgPartitionEn() == null) ? null : drgCatalog.getDrgPartitionEn().name()) + ", ");
            sql.append(QStr(drgCatalog.getDrgValidFrom()) + ", ");
            sql.append(QStr(drgCatalog.getDrgValidTo()) + ", ");
            sql.append(QStr(drgCatalog.getDrgYear()) + ", ");
            sql.append(QStr(drgCatalog.getDrgHosIdent()));
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
                    + "(ID                      INTEGER    PRIMARY KEY,"
                    + " CREATION_DATE           DATETIME, "
                    + " MODIFICATION_DATE       DATETIME, "
                    + " COUNTRY_EN              VARCHAR(25), "
                    + " MDC_SK_EN               VARCHAR(25), "
                    + " DRG_DESCRIPTION         VARCHAR(1000), "
                    + " DRG_DRG                 VARCHAR(10), "
                    + " DRG_EO_1_DEDUCTION_DAY  DECIMAL(10, 0), "
                    + " DRG_EO_1_SURCHARGE_DAY  DECIMAL(10, 0), "
                    + " DRG_EO_ALOS             DECIMAL(5, 1), "
                    + " DRG_EO_CW               DECIMAL(10, 1), "
                    + " DRG_EO_CW_DEDUCTION     DECIMAL(10, 3), "
                    + " DRG_EO_CW_SURCHARGE     DECIMAL(10, 3), "
                    + " DRG_EO_CW_TRANSF_DEDUCT DECIMAL(10, 3), "
                    + " DRG_EO_IS_READM_FL      TINYINT    NOT NULL, "
                    + " DRG_EO_IS_TRANSFER_FL   TINYINT    NOT NULL, "
                    + " DRG_EOA_CW              DECIMAL(10, 3), "
                    + " DRG_EOAM_CW             DECIMAL(10, 3), "
                    + " DRG_EOM_CW              DECIMAL(10, 3), "
                    + " DRG_IS_DAY_CARE_FL      TINYINT    NOT NULL, "
                    + " DRG_IS_NEGOTIATED_FL    TINYINT    NOT NULL, "
                    + " DRG_MD_1_DEDUCTION_DAY  DECIMAL(10, 0), "
                    + " DRG_MD_1_SURCHARGE_DAY  DECIMAL(10, 0), "
                    + " DRG_MD_ALOS             DECIMAL(5, 1), "
                    + " DRG_MD_CW               DECIMAL(10, 3), "
                    + " DRG_MD_CW_DEDUCTION     DECIMAL(10, 3), "
                    + " DRG_MD_CW_SURCHARGE     DECIMAL(10, 3), "
                    + " DRG_MD_CW_TRANSF_DEDUCT DECIMAL(10, 3), "
                    + " DRG_MD_IS_READM_FL      DECIMAL(10, 0), "
                    + " DRG_MD_IS_TRANSFER_FL   TINYINT    NOT NULL, "
                    + " DRG_MDM_CW              DECIMAL(10, 3), "
                    + " DRG_PARTITION_EN        VARCHAR(25), "
                    + " DRG_VALID_FROM          DATETIME, "
                    + " DRG_VALID_TO            DATETIME, "
                    + " DRG_YEAR                INT, "
                    + " DRG_HOS_IDENT           VARCHAR(10) ) ";
            stmt.executeUpdate(sql);

            sql = String.format("CREATE INDEX IF NOT EXISTS IDX_DRG ON %s (DRG_DRG)", tableName); //COUNTRY_EN, DRG_YEAR
            stmt.executeUpdate(sql);

            stmt.close();
            return tableName;
        } catch (SQLException ex) {
            Logger.getLogger(CpxDrgCatalog.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    @Override
    public CpxDrg toCpxObject(final ResultSet rs) throws SQLException {
        CpxDrg obj = getNewObject();
        if (rs == null) {
            return obj;
        }
        String mdc = rs.getString("MDC_SK_EN");
        GrouperMdcOrSkEn grouperMdcOrSkEn = null;
        if (mdc != null && !mdc.isEmpty()) {
            try {
                //grouperMdcOrSkEn = (GrouperMdcOrSkEn) CpxEnumInterface.findEnum(GrouperMdcOrSkEn.values(), mdc);
                grouperMdcOrSkEn = GrouperMdcOrSkEn.findById(mdc);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(CpxDrgCatalog.class.getName()).log(Level.SEVERE, "Invalid MDC defined!", ex);
            }
        }

        obj.setId(rs.getLong("ID"));
        //obj.setCreationDate(CpxDateParser.parseDateTime(rs.getString("CREATION_DATE")));
        //obj.setModificationDate(CpxDateParser.parseDateTime(rs.getString("MODIFICATION_DATE")));
        //obj.setCountryEn(CountryEn.valueOf(rs.getString("COUNTRY_EN")));
        obj.setCMdcSkCatalog(grouperMdcOrSkEn);
        obj.setDrgDescription(rs.getString("DRG_DESCRIPTION"));
        obj.setDrgDrg(rs.getString("DRG_DRG"));
        obj.setDrgEo1DeductionDay(rs.getInt("DRG_EO_1_DEDUCTION_DAY"));
        obj.setDrgEo1SurchargeDay(rs.getInt("DRG_EO_1_SURCHARGE_DAY"));
        obj.setDrgEoAlos(rs.getBigDecimal("DRG_EO_ALOS"));
        obj.setDrgEoCw(rs.getBigDecimal("DRG_EO_CW"));
        obj.setDrgEoCwDeduction(rs.getBigDecimal("DRG_EO_CW_DEDUCTION"));
        obj.setDrgEoCwSurcharge(rs.getBigDecimal("DRG_EO_CW_SURCHARGE"));
        obj.setDrgEoCwTransfDeduct(rs.getBigDecimal("DRG_EO_CW_TRANSF_DEDUCT"));
        obj.setDrgEoIsReadmFl(rs.getBoolean("DRG_EO_IS_READM_FL"));
        obj.setDrgEoIsTransferFl(rs.getBoolean("DRG_EO_IS_TRANSFER_FL"));
        obj.setDrgEoaCw(rs.getBigDecimal("DRG_EOA_CW"));
        obj.setDrgEoamCw(rs.getBigDecimal("DRG_EOAM_CW"));
        obj.setDrgEomCw(rs.getBigDecimal("DRG_EOM_CW"));
        obj.setDrgIsDayCareFl(rs.getBoolean("DRG_IS_DAY_CARE_FL"));
        obj.setDrgIsNegotiatedFl(rs.getBoolean("DRG_IS_NEGOTIATED_FL"));
        obj.setDrgMd1DeductionDay(rs.getInt("DRG_MD_1_DEDUCTION_DAY"));
        obj.setDrgMd1SurchargeDay(rs.getInt("DRG_MD_1_SURCHARGE_DAY"));
        obj.setDrgMdAlos(rs.getBigDecimal("DRG_MD_ALOS"));
        obj.setDrgMdCw(rs.getBigDecimal("DRG_MD_CW"));
        obj.setDrgMdCwDeduction(rs.getBigDecimal("DRG_MD_CW_DEDUCTION"));
        obj.setDrgMdCwSurcharge(rs.getBigDecimal("DRG_MD_CW_SURCHARGE"));
        obj.setDrgMdCwTransfDeduct(rs.getBigDecimal("DRG_MD_CW_TRANSF_DEDUCT"));
        obj.setDrgMdIsReadmFl(rs.getBoolean("DRG_MD_IS_READM_FL"));
        obj.setDrgMdIsTransferFl(rs.getBoolean("DRG_MD_IS_TRANSFER_FL"));
        obj.setDrgMdMCw(rs.getBigDecimal("DRG_MDM_CW"));
        obj.setDrgPartitionEn(DrgPartitionEn.valueOf(rs.getString("DRG_PARTITION_EN")));
        obj.setDrgValidFrom(CpxDateParser.parseDateTime(rs.getString("DRG_VALID_FROM")));
        obj.setDrgValidTo(CpxDateParser.parseDateTime(rs.getString("DRG_VALID_TO")));
        obj.setDrgYear(rs.getInt("DRG_YEAR"));
        obj.setDrgHosIdent(rs.getString("DRG_HOS_IDENT"));
        return obj;
    }

//    /**
//     * Returns Dummy-Object if DRG Code cannot be found in local SQLite-DB
//     *
//     * @param pCode DRG Code
//     * @param pCountryEn Country ("de", "en")
//     * @param pYear Year
//     * @return DRG
//     */
//    @Override
//    public CpxDrg getByCode(final String pCode, final String pCountryEn, final Integer pYear) {
//        List<CpxDrg> list = findManyByCode(pCode, pCountryEn, pYear);
//        if (list.isEmpty()) {
//            return getNewObject();
//        }
//        return list.get(0);
//    }
//    /**
//     * Returns Dummy-Object if DRG Code cannot be found in local SQLite-DB
//     *
//     * @param pId ID
//     * @param pCountryEn Country ("de", "en")
//     * @param pYear Year
//     * @return DRG
//     */
//    @Override
//    public CpxDrg getById(final Long pId, final String pCountryEn, final Integer pYear) {
//        int year = checkYear(pYear);
//        CpxDrg obj = getNewObject();
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
//            Logger.getLogger(CpxDrgCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return obj;
//    }
    /**
     * Returns Dummy-Object if DRG Code cannot be found in local SQLite-DB
     *
     * @param pCode DRG-Code
     * @param pCountryEn Country ("de", "en")
     * @param pYear Year
     * @return DRG
     */
    @Override
    public List<CpxDrg> findManyByCode(final String pCode, final String pCountryEn, final Integer pYear) {
        final String code = toParam(pCode);
        String sql = "SELECT * FROM %s WHERE DRG_DRG " + operator(code) + " ? ORDER BY DRG_DRG";
        return fetchMany(sql, pCountryEn, pYear, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        int year = checkYear(pYear);
//        List<CpxDrg> objList = new ArrayList<>();
//        final String tableName = getTableName(CATALOG, pCountryEn, year);
//        final String code = toParam(pCode);
//        String sql = String.format("SELECT * FROM %s WHERE DRG_DRG " + operator(code) + " ? ORDER BY DRG_DRG", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(CATALOG, year).prepareStatement(sql)) {
//            pstmt.setString(1, code);
//            try ( ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    CpxDrg obj = toCpxObject(rs);
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
//            Logger.getLogger(CpxDrgCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return objList;
    }

    @Override
    public CatalogTypeEn getCatalogType() {
        return CATALOG;
    }

    /**
     * Search for DRG Catalog entry end creates DRG Description String. Layout:
     * DRG-Code : DRG-Description
     *
     * @param pGrpresCode drg code from the grouper
     * @param pCountryEn country where drg result is valid
     * @param pYear year of validity
     * @return Description String
     */
    public String getDrgDescription(String pGrpresCode, String pCountryEn, Integer pYear) {
        final String code = toParam(pGrpresCode);
        String sql = "SELECT DRG_DRG, DRG_DESCRIPTION FROM %s WHERE DRG_DRG " + operator(code) + " ? ORDER BY DRG_DRG";
        return fetchSingleValue(sql, pCountryEn, pYear, (pstmt) -> {
            pstmt.setString(1, code);
        }, (rs) -> {
            String description = rs.getString("DRG_DESCRIPTION");
            description = description == null ? "----" : description;
            return rs.getString("DRG_DRG") + ": " + description;
        });

//        CpxDrg catalogEntry = getByCode(grpresCode, "de", pYear);
//        String description = catalogEntry.getDrgDescription() == null ? "----" : catalogEntry.getDrgDescription();
//        return grpresCode + ": " + description; // pna-04.07.18, removed space cpx-1073
    }

    /**
     * Search DRG Catalog entry and creates DRG Description String only. Layout:
     * DRG-Description
     *
     * @param pGrpresCode drg code from the grouper
     * @param pCountryEn country where drg result is valid
     * @param pYear year of validity
     * @return Description String
     */
    public String getDrgDecriptionText(String pGrpresCode, String pCountryEn, Integer pYear) {
        final String code = toParam(pGrpresCode);
        String sql = "SELECT DRG_DESCRIPTION FROM %s WHERE DRG_DRG " + operator(code) + " ? ORDER BY DRG_DRG";
        return fetchSingleValue(sql, pCountryEn, pYear, (pstmt) -> {
            pstmt.setString(1, code);
        });

//        CpxDrg catalogEntry = getByCode(grpresCode, "de", year);
//        String description = catalogEntry.getDrgDescription() == null ? "----" : catalogEntry.getDrgDescription();
//        return description;
    }

    @Override
    public CpxDrg getNewObject() {
        return new CpxDrg();
    }

}
