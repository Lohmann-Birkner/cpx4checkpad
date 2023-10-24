/* 
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.filter.enums;

import de.lb.cpx.model.enums.SearchListTypeEn;
import de.lb.cpx.service.properties.SearchListProperties;
import de.lb.cpx.shared.filter.ColumnOption;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author niemeier
 */
public class SearchListTypeFactory {

    private static final Logger LOG = Logger.getLogger(SearchListTypeFactory.class.getName());

    private static SearchListTypeFactory instance;

    public static synchronized SearchListTypeFactory instance() {
        if (instance == null) {
            instance = new SearchListTypeFactory();
        }
        return instance;
    }

    private SearchListTypeFactory() {
        //
    }

    public SearchListAttributes getSearchListAttributes(final SearchListTypeEn pType) {
        if (pType == null) {
            LOG.log(Level.FINEST, "type null passed!");
            return null;
        }
        if (pType.isWorkingList()) {
            return WorkingListAttributes.instance();
        }
        if (pType.isWorkflowList()) {
            return WorkflowListAttributes.instance();
        }
        return RuleListAttributes.instance();
    }

    public SearchListProperties getNewSearchListProperties(final SearchListTypeEn pType) {
        if (pType == null) {
            LOG.log(Level.FINEST, "type null passed!");
            return null;
        }
//        final CSearchList searchList = new CSearchList();
//        searchList.setCreationUser(pUserId);
//        searchList.setSlName("");

        final SearchListProperties searchList = new SearchListProperties();

        boolean isRuleList = pType.isRuleList();
        boolean isQuotaList = pType.isQuotaList();
        boolean isWorkingList = pType.isWorkingList();
        boolean isWorkflowList = pType.isWorkflowList();
        boolean isRuleWorkingList = pType.isRuleWorkingList();
        boolean isLaboratoryDataList = pType.isLaboratoryDataList();

        final SearchListAttributes attributes;
        final List<String> defaultColumns;
        if (isRuleList) {
            attributes = RuleListAttributes.instance();
            defaultColumns = RuleListAttributes.getRuleColumns();
        } else if (isQuotaList) {
            attributes = QuotaListAttributes.instance();
            defaultColumns = QuotaListAttributes.getDefaultColumns();
        } else if (isWorkingList) {
            attributes = WorkingListAttributes.instance();
            defaultColumns = WorkingListAttributes.getDefaultColumns();
        } else if (isWorkflowList) {
            attributes = WorkflowListAttributes.instance();
            defaultColumns = WorkflowListAttributes.getDefaultColumns();
        } else if (isRuleWorkingList) {
            attributes = SearchListRuleAttributes.instance();
            defaultColumns = SearchListRuleAttributes.getDefaultColumns();
        } else if (isLaboratoryDataList) {
            attributes = LaboratoryDataListAttributes.instance();
            defaultColumns = LaboratoryDataListAttributes.getDefaultColumns();
        } else {
            throw new IllegalArgumentException(MessageFormat.format("This list type is unknown and/or not implemented yet: {0}", this));
        }

        //SearchListAttributes attributes = pList == SearchListTypeEn.WORKING ? WorkingListAttributes.instance() : WorkflowListAttributes.instance();
        for (Map.Entry<String, SearchListAttribute> entry : attributes.getAll().entrySet()) {
            SearchListAttribute att = entry.getValue();

            if (!att.isVisible()) {
                continue;
            }

            if (att.getParent() != null) {
                continue;
            }

            if (defaultColumns.contains(att.getKey())) {
//                List<SearchListAttribute> attributeList = new LinkedList<>();
//                if (att.hasChildren()) {
//                    attributeList.addAll(att.getChildren());
//                } else {
//                    attributeList.add(att);
//                }
                boolean selected = false;
                int size = att.getSize();
                //ColumnOption colOption = workingList.getColumn(att.getKey().getName());
                int sort = defaultColumns.indexOf(att.getKey());
                sort++;
                Integer sortNumber = sort;
                String sortType = "";
                Integer number = sort;
                //workinList.addColumn(colOption);
                //String displayName = lang.get(att.getName());
                //ColumnOption option = new ColumnOption(att.getLanguageKey(), att.getKey().getName(), selected);
                ColumnOption option = new ColumnOption(att.getLanguageKey(), att.getKey(), selected);
                //option.setWorkingListAttribute(att);
                option.setSize(size);
                option.setSortNumber(sortNumber);
                option.setSortType(sortType);
                option.setNumber(number);
                option.setShouldShow(true);
                searchList.addColumn(option);
            }
        }
        return searchList;
    }

}
