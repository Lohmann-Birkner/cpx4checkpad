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
package de.lb.cpx.service.information;

import de.lb.cpx.shared.lang.Lang;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dirk Niemeier
 */
public class CpxCatalogOverview implements Serializable, Comparable<CpxCatalogOverview> {

    private static final Logger LOG = Logger.getLogger(CpxCatalogOverview.class.getName());

    private static final long serialVersionUID = 1L;

    public final int id;
    public final String catalog;
    public final String countryEn;
    public final int year;
    public int count;
    public final long minId;
    public final long maxId;
    public final Date date;
    private int newCount;
    private long newMinId;
    private long newMaxId;
    private Date newDate;
    public CatalogTodoEn todo = CatalogTodoEn.NONE;

    public CpxCatalogOverview(final Integer pId, final String pCatalog, final String pCountryEn, final Integer pYear, final Integer pCount, final Long pMinId, final Long pMaxId, final Date pDate) {
        id = (pId == null) ? 0 : pId;
        catalog = (pCatalog == null) ? "" : pCatalog.trim().toUpperCase();
        countryEn = (pCountryEn == null) ? "" : pCountryEn.trim().toLowerCase();
        year = (pYear == null) ? 0 : pYear;
        count = (pCount == null) ? 0 : pCount;
        minId = (pMinId == null) ? 0L : pMinId;
        maxId = (pMaxId == null) ? 0L : pMaxId;
        date = pDate;
    }

    public static CatalogTypeEn catalogToType(final String pCatalog) {
        String catalog = (pCatalog == null) ? "" : pCatalog.trim().toUpperCase();
        if (catalog.isEmpty()) {
            return null;
        }
        try {
            return CatalogTypeEn.valueOf(catalog);
        } catch (IllegalArgumentException ex) {
            LOG.log(Level.WARNING, "Catalog does not exist: " + pCatalog, ex);
            // It is perfectly acceptable to not handle "ex" here
            return null;
        }
    }

    public CatalogTypeEn getCatalogType() {
        //return CATALOG_TYPE.valueOf(getCatalog());
        return catalogToType(getCatalog());
    }

    public int getId() {
        return id;
    }

    public String getCatalog() {
        return catalog;
    }

    public String getCatalogWithYear() {
        return year > 0 ? catalog + "_" + year : catalog;
    }

    public String getCountryEn() {
        return countryEn;
    }

    public int getYear() {
        return year;
    }

    public int getCount() {
        return count;
    }

    public CpxCatalogOverview setCount(final int pCount) {
        count = pCount;
        return this;
    }

    public long getMinId() {
        return minId;
    }

    public long getMaxId() {
        return maxId;
    }

    public Date getDate() {
        return date;
    }

    public CpxCatalogOverview setNewCount(final Integer pNewCount) {
        newCount = (pNewCount == null) ? 0 : pNewCount;
        return this;
    }

    public CpxCatalogOverview setNewMinId(final Long pNewMinId) {
        newMinId = (pNewMinId == null) ? 0L : pNewMinId;
        return this;
    }

    public CpxCatalogOverview setNewMaxId(final Long pNewMaxId) {
        newMaxId = (pNewMaxId == null) ? 0L : pNewMaxId;
        return this;
    }

    public CpxCatalogOverview setNewDate(final Date pNewDate) {
        newDate = pNewDate;
        return this;
    }

    public int getNewCount() {
        return newCount;
    }

    public long getNewMinId() {
        return newMinId;
    }

    public long getNewMaxId() {
        return newMaxId;
    }

    public Date getNewDate() {
        return newDate;
    }

    public CatalogTodoEn getTodo() {
        if (todo == null) {
            todo = CatalogTodoEn.NONE;
        }
        return todo;
    }

    public CpxCatalogOverview setTodo(final CatalogTodoEn pTodo) {
        if (pTodo == null) {
            todo = CatalogTodoEn.NONE;
            return this;
        }
        todo = pTodo;
        return this;
    }

    public String getTitleWithYear() {
        String title = getTitle();
        if (getYear() > 0) {
            title += " " + getYear();
        }
        return title;
    }

    public String getTitle() {
        String lCatalog = getCatalog();
        lCatalog = (lCatalog == null) ? "" : lCatalog.trim();
        CatalogTypeEn catalogType = catalogToType(lCatalog);
        if (catalogType != null) {
            switch (catalogType) {
                /*
        case ICD:
          return "ICD";
        case OPS:
          return "OPS";
                 */
                case HOSPITAL:
                    return Lang.getCatalogDownloadHospitals();
                //return "HOSPITAL";
                case INSURANCE_COMPANY:
                    return Lang.getCatalogDownloadInsurance_companys();
                //return "HOSPITAL";
                case DEPARTMENT:
                    return Lang.getCatalogDownloadDepartments();
                //return "HOSPITAL";
                case DOCTOR:
                    return Lang.getCatalogDownloadDoctors();
                //return "HOSPITAL";
                case BASERATE:
                    return Lang.getCatalogDownloadBaserates();
                //return "HOSPITAL";
                case ICD_THESAURUS:
                    return "ICD Thesaurus";
                case OPS_THESAURUS:
                    return "OPS Thesaurus";
                default:
                    return catalogType.name();
            }
            //throw new CpxIllegalArgumentException("Catalog '" + catalog + "' is not valid!");
        }
        return "";
        //return "INVALID_CATALOG";
    }

    @Override
    public int compareTo(CpxCatalogOverview o) {
        if (this == o) {
            return 0;
        }
        int cmp = 0;
        if (this.getYear() > o.getYear()) {
            return -1;
        }
        if (this.getYear() < o.getYear()) {
            return 1;
        }
        cmp = this.getCatalog().compareTo(o.getCatalog());
        if (cmp != 0) {
            return cmp;
        }
        cmp = this.getCountryEn().compareTo(o.getCountryEn());
        if (cmp != 0) {
            return cmp;
        }
        return 0;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.catalog);
        hash = 79 * hash + Objects.hashCode(this.countryEn);
        hash = 79 * hash + this.year;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CpxCatalogOverview other = (CpxCatalogOverview) obj;
        if (this.year != other.year) {
            return false;
        }
        if (!Objects.equals(this.catalog, other.catalog)) {
            return false;
        }
        if (!Objects.equals(this.countryEn, other.countryEn)) {
            return false;
        }
        return true;
    }

    /*
  public int getEntryCount() {
    if (getNewCount() > 0) {
      return getNewCount();
    }
    return getCount();
  }
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ": " + "\n"
                + "catalog:    " + getCatalog() + "\n"
                + "countryEn:  " + getCountryEn() + "\n"
                + "year:       " + getYear() + "\n"
                + "count:      " + getCount() + "\n"
                + "min_id:     " + getMinId() + "\n"
                + "max_id:     " + getMaxId() + "\n"
                + "date:       " + getDate() + "\n"
                + "new_min_id: " + getNewMinId() + "\n"
                + "new_max_id: " + getNewMaxId() + "\n"
                + "new_date:   " + getNewDate() + "\n"
                + "todo:       " + ((getTodo() == null) ? "" : getTodo().name()) + "\n";
    }

}
