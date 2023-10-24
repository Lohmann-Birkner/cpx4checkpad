/* 
 * Copyright (c) 2016 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner.
 * http://www.lohmann-birkner.de/de/index.php
 *
 * Contributors:
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.filter;

import de.lb.cpx.model.enums.SearchListTypeEn;
import de.lb.cpx.shared.filter.enums.LaboratoryDataListAttributes;
import de.lb.cpx.shared.filter.enums.QuotaListAttributes;
import de.lb.cpx.shared.filter.enums.RuleListAttributes;
import de.lb.cpx.shared.filter.enums.SearchListAttribute;
import de.lb.cpx.shared.filter.enums.SearchListTypeFactory;
import de.lb.cpx.shared.filter.enums.WorkflowListAttributes;
import de.lb.cpx.shared.filter.enums.WorkingListAttributes;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author wilde
 */
//@XmlRootElement(name = "Column")
@XmlAccessorType(XmlAccessType.FIELD)
public class ColumnOption implements java.io.Serializable, Comparable<ColumnOption> {

    private static final Logger LOG = Logger.getLogger(ColumnOption.class.getName());

    private static final long serialVersionUID = 1L;
    /*
  public void setWorkingListAttribute(SearchListAttribute att) {
  this.att = att;
  }
  
  public SearchListAttribute getWorkingListAttribute() {
  return this.att;
  }
     */

    @XmlTransient
    protected String displayName;

    @XmlTransient
    protected String toolTipText;

    @XmlAttribute(name = "name")
    public final String attributeName;

    @XmlAttribute(name = "show")
    protected boolean shouldShow;

    @XmlAttribute(name = "size", required = false)
    protected Integer size;

    @XmlAttribute(name = "number", required = false)
    protected Integer number;

    @XmlAttribute(name = "sortType", required = false)
    protected String sortType;

    @XmlAttribute(name = "sortNumber", required = false)
    protected Integer sortNumber;

    /*
    @XmlTransient
    private SearchListAttribute att = null;
     */
    public ColumnOption() {
        displayName = "";
        attributeName = "";
        shouldShow = true;
        toolTipText = "";
        sortType = null;
    }

    public ColumnOption(String displayName, String attributeName, boolean show) {
        this.displayName = displayName == null ? "" : displayName.trim();
        this.attributeName = attributeName == null ? "" : attributeName.trim();
        this.shouldShow = show;
        toolTipText = "";
        sortType = null;
    }

    public Integer getColumnSize() {
        return size;
    }

    public Integer getColumnSize(final SearchListTypeEn pListType) {
        Integer s = size;
        if (s == null && pListType != null) {
            //return WorkingListAttributes.valueOf(attributeName).getDefaultTableSize();
            SearchListAttribute get = SearchListTypeFactory.instance().getSearchListAttributes(pListType).get(attributeName);
            if (get != null) {
                return get.getSize();
            }
        }
        return size;
    }

    public Integer getNumber() {
        return number;
    }

    public Integer getNumber(final SearchListTypeEn pListType) {
        Integer n = number;
        if (n == null && pListType != null) {
            //return WorkingListAttributes.valueOf(attributeName).getNumber();
            SearchListAttribute attr = SearchListTypeFactory.instance().getSearchListAttributes(pListType).get(attributeName);
            if (attr != null) {
                return attr.getSize();
            }
        }
        return number;
    }

    /**
     * @return getNumber,due to some reason getNumber() returns size this helper
     * methodes returns actual value
     */
    public Integer getIndex() {
        return getNumber();
    }

    /*
    public SortType getSortType() {
      if (sortType == null) {WorkingListAttributes.valueOf(attributeName)
        sortType = "";
      }
      if (sortType.equalsIgnoreCase("asc")) {
        return SortType.ASCENDING;
      }
      if (sortType.equalsIgnoreCase("desc")) {
        return SortType.DESCENDING;
      }
      return null;
    }
    
    public void setSortType(final SortType pSortType) {
      if (pSortType != null) {
        if (pSortType.equals(SortType.ASCENDING)) {
          sortType = "asc";
        }
        if (pSortType.equals(SortType.DESCENDING)) {
          sortType = "desc";
        }
      } else {
        sortType = "";
      }
    }
     */
    public String getSortType() {
        if (sortType != null && sortType.trim().isEmpty()) {
            sortType = null;
        }

        return sortType;
    }

    public void setSortType(final String pSortType) {
        String lSortType = (pSortType == null || pSortType.trim().isEmpty()) ? null : pSortType.trim().toLowerCase();

        if (lSortType != null) {
            if (!lSortType.equalsIgnoreCase("asc") && !lSortType.equalsIgnoreCase("desc")) {
                lSortType = null;
            }
        }

        this.sortType = lSortType;
    }

    public Integer getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(final Integer pSortNumber) {
        sortNumber = pSortNumber;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + Objects.hashCode(this.attributeName);
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
        final ColumnOption other = (ColumnOption) obj;
        if (!Objects.equals(this.attributeName, other.attributeName)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(ColumnOption o) {
        if (this == o) {
            return 0;
        }
        //int cmp = ((Integer)this.getNumber()).compareTo((Integer)o.getNumber());
        int cmp = (this.number).compareTo(o.number);
        if (cmp == 0) {
            cmp = this.displayName.compareTo(o.displayName);
        }
        return cmp;
        //return this.displayName.compareTo(o.displayName);
    }

    @Override
    public String toString() {
        return displayName;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(final String pDisplayName) {
        this.displayName = (pDisplayName == null || pDisplayName.trim().isEmpty()) ? "" : pDisplayName.trim();
    }

    public String getDisplayName(final SearchListTypeEn pListType) {
        Translation trans = getTranslation(pListType);
        if (trans == null) {
            return "";
        }
        StringBuilder b = new StringBuilder();
        if (trans.hasAbbreviation()) {
            b.append(trans.abbreviation);
            if (trans.hasValue() && !trans.abbreviation.contains(trans.value)) {
                b.append(" ").append("(").append(trans.value).append(")");
            }
        } else {
            b.append(trans.value);
        }
        return b.toString();
    }

    public String getTooltip(final SearchListTypeEn pListType) {
        Translation trans = getTranslation(pListType);
        if (trans == null) {
            return "";
        }
        return trans.getTooltip();
    }

    public Translation getTranslation(final SearchListTypeEn pListType) {
        if (pListType == null) {
            LOG.log(Level.WARNING, "list type should not be null!");
            return null;
        }
        final Translation trans;
        switch (pListType) {
            case WORKING:
                trans = Lang.get(WorkingListAttributes.instance().get(attributeName).getLanguageKey());
                break;
            case WORKFLOW:
                trans = Lang.get(WorkflowListAttributes.instance().get(attributeName).getLanguageKey());
                break;
            case LABORATORY:
                trans = Lang.get(LaboratoryDataListAttributes.instance().get(attributeName).getLanguageKey());
                break;
            case RULE:
                trans = Lang.get(RuleListAttributes.instance().get(attributeName).getLanguageKey());
                break;
            case QUOTA:
                trans = Lang.get(QuotaListAttributes.instance().get(attributeName).getLanguageKey());
                break;
            default:
                LOG.log(Level.WARNING, "unknown list type: {0}", pListType.name());
                trans = null;
        }
        return trans;
    }

    public boolean isShouldShow() {
        return shouldShow;
    }

    public void setShouldShow(final boolean pShouldShow) {
        this.shouldShow = pShouldShow;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(final Integer pSize) {
        this.size = pSize;
    }

    public String getToolTipText() {
        return toolTipText;
    }

    public void setToolTipText(final String pToolTipText) {
        this.toolTipText = (pToolTipText == null || pToolTipText.trim().isEmpty()) ? "" : pToolTipText.trim();
    }

}
