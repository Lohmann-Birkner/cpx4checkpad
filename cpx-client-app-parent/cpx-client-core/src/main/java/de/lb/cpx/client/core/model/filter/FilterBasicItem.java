/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.filter;

/**
 * Basic filter item used for communication for Filter results in
 * FilterBasicTableView and Columns
 *
 * @author wilde
 */
public class FilterBasicItem {

    private String dataKey;
    private String value;
    private String localizedKey;
    private String localizedValue;
    private String tooltipText;

    public String getTooltipText() {
        return tooltipText;
    }

    public void setTooltipText(String tooltipText) {
        this.tooltipText = tooltipText;
    }

    /**
     * creates new instance for datakey
     *
     * @param pDataKey datakey for filter
     */
    public FilterBasicItem(String pDataKey) {
        dataKey = pDataKey;
    }

    /**
     * creates new instance for datakey of the filter and filtervalue
     *
     * @param pDataKey datakey of the filter
     * @param pValue filter value
     */
    public FilterBasicItem(String pDataKey, String pValue) {
        dataKey = pDataKey;
        value = pValue;
    }

    /**
     * @return localized key, to get correct translation
     */
    public String getLocalizedKey() {
        return localizedKey;
    }

    /**
     * @param pLocKey localized key for translation
     */
    public void setLocalizedKey(String pLocKey) {
        localizedKey = pLocKey;
    }

    /**
     * @return localized(formated value, to display)
     */
    public String getLocalizedValue() {
        return localizedValue;
    }

    /**
     * @param pValue formated/localized value to display
     */
    public void setLocalizedValue(String pValue) {
        localizedValue = pValue;
    }

    /**
     * @return filter datakey
     */
    public String getDataKey() {
        return dataKey;
    }

    /**
     * NOTE: should not be used, after filter is created
     *
     * @param pKey changed datakey
     */
    public void setDataKey(String pKey) {
        dataKey = pKey;
    }

    /**
     * @return get filter value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param pValue updated filter value
     */
    public void setValue(String pValue) {
        value = pValue;
    }

    public boolean hasTooltip() {
        if (tooltipText == null) {
            return false;
        }
        if (tooltipText.isEmpty()) {
            return false;
        }
        return true;
    }

}
