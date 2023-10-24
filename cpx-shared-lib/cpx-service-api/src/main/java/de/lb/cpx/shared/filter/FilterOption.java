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

import java.io.Serializable;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author wilde
 */
//@XmlRootElement(name = "Filter")
@XmlAccessorType(XmlAccessType.FIELD)
public class FilterOption implements Serializable, Comparable<FilterOption> {

    private static final long serialVersionUID = 1L;
    /*
  public void setWorkingListAttribute(WorkingListAttribute att) {
  this.att = att;
  }
  
  public WorkingListAttribute getWorkingListAttribute() {
  return this.att;
  }
     */

    @XmlAttribute(name = "field")
    public final String field;

    @XmlTransient
    public final String name;
//    public final boolean isShown;

    @XmlAttribute(name = "value")
    protected String value;

//    @XmlAttribute(name = "counter")
//    protected int counter = 0;
    @XmlTransient
    protected String localizedValue = "";

    /*
    @XmlTransient
    private WorkingListAttribute att = null;
     */
    public FilterOption() {
        field = "";
        name = "";
        value = "";
    }

    public FilterOption(String name, String field, String value) {//,boolean shown){
        this.name = name == null ? "" : name.trim();
        this.field = field == null ? "" : field.trim();
        this.value = value == null ? "" : value.trim();
//        this.isShown = shown;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String pValue) {
        this.value = (pValue == null || pValue.trim().isEmpty()) ? "" : pValue.trim();
    }

    public boolean hasValue() {
        return value != null && !value.isEmpty();
    }

    public String getLocalizedValue() {
        return localizedValue;
    }

    public void setLocalizedValue(final String pLocalizedValue) {
        this.localizedValue = (pLocalizedValue == null || pLocalizedValue.trim().isEmpty()) ? "" : pLocalizedValue.trim();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.field);
        hash = 29 * hash + Objects.hashCode(this.name);
//        hash = 29 * hash + this.counter;
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
        final FilterOption other = (FilterOption) obj;
//        if (this.counter != other.counter) {
//            return false;
//        }
        if (!Objects.equals(this.field, other.field)) {
            return false;
        }
        return true;
    }

    /**
     * does this method make sense? no!
     *
     * @param o object
     * @return result
     */
    @Override
    public int compareTo(FilterOption o) {
        if (o == null) {
            return 1;
        }
        return 0;
    }

//    @Override
//    public int compareTo(FilterOption o) {
//        if (o == null) {
//            return 1;
//        }
//        if (this.counter > o.counter) {
//            return 1;
//        }
//        if (this.counter < o.counter) {
//            return -1;
//        }
//        return 0;
//    }
//    /**
//     * counter is greater than 1 if same filter field occurs multiple time
//     *
//     * @return number of same fields
//     * @deprecated don't use this anymore
//     */
//    @Deprecated(since = "1.05")
//    public int getCounter() {
//        return counter;
//    }
//    /**
//     * counter is greater than 1 if same filter field occurs multiple time
//     *
//     * @param pCounter number of same fields
//     * @deprecated don't use this anymore
//     */
//    @Deprecated(since = "1.05")
//    public void setCounter(final int pCounter) {
//        this.counter = pCounter;
//    }
    @Override
    public String toString() {
        return "FilterOption{" + "field=" + field + ", name=" + name + ", value=" + value + ", localizedValue=" + localizedValue + '}';
    }

}
