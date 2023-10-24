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

import de.lb.cpx.server.commons.dao.AbstractCatalogEntity;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Dirk Niemeier
 * @param <CpxType> CpxIcd, CpxOps, CpxHospital, CpxInsuranceCompany...
 * @param <EntityType> CIcdCatalog, COpsCatalog, CHospital, CInsuranceCompany
 */
public abstract class AbstractCpxNonAnnualCatalog<CpxType extends AbstractCatalogEntity, EntityType extends AbstractCatalogEntity> extends AbstractCpxCatalog<CpxType, EntityType> {

//    public abstract CpxType getByCode(final String pKey, final String pCountryEn);
    /**
     * Returns Dummy-Object if entry cannot be found in local SQLite-DB
     *
     * @param pKey key
     * @param pCountryEn Country ("de", "en")
     * @return object
     */
    public final CpxType getByCode(final String pKey, final String pCountryEn) {
        List<CpxType> list = findManyByCode(pKey, pCountryEn);
        if (list.isEmpty()) {
            return getNewObject();
        }
        return list.iterator().next();
    }

    //public abstract CpxType getById(final Long pId, final String pCountryEn);
    public final CpxType getById(final Long pId, final String pCountryEn) {
        String sql = "SELECT * FROM %s WHERE ID = ?";
        return fetchSingle(sql, pCountryEn, (pstmt) -> {
            pstmt.setLong(1, pId);
        });
//        int year = 0;
//        CpxType obj = getNewObject();
//        long id = (pId == null) ? 0L : pId;
//        CatalogTypeEn catalog = getCatalogType();
//        final String tableName = getTableName(catalog, pCountryEn, year);
//        String sql = String.format("SELECT * FROM %s WHERE ID = ?", tableName);
//        try ( PreparedStatement pstmt = getCatalogDb(catalog, year).prepareStatement(sql)) {
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
    }

    public abstract List<CpxType> findManyByCode(final String pKey, final String pCountryEn);

    @Override
    public CpxType getByCode(final String pKey, final String pCountryEn, final Integer pYear) {
        return getByCode(pKey, pCountryEn);
    }

    @Override
    public final CpxType getById(final Long pId, final String pCountryEn, final Integer pYear) {
        return getById(pId, pCountryEn);
    }

    public Map<Long, CpxType> getAll(final String pCountryEn) {
        return super.getAll(pCountryEn, 0);
    }

    @Override
    public Map<Long, CpxType> getAll(final String pCountryEn, final Integer pYear) {
        return getAll(pCountryEn);
    }

    @Override
    public List<CpxType> findManyByCode(final String pKey, final String pCountryEn, final Integer pYear) {
        return findManyByCode(pKey, pCountryEn);
    }

    public CpxType getByCode(final String pKey) {
        return getByCode(pKey, DEFAULT_COUNTRY);
    }

    public CpxType getById(final Long pId) {
        return getById(pId, DEFAULT_COUNTRY);
    }

    public List<CpxType> findManyByCode(final String pKey) {
        return findManyByCode(pKey, DEFAULT_COUNTRY);
    }

    @Override
    public boolean isAnnualCatalog() {
        return false;
    }

}
