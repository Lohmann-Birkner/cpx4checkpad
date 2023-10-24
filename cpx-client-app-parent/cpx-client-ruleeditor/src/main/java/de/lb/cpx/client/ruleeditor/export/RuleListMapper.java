/*
 * Copyright (c) 2021 Lohmann & Birkner.
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
 *    2021  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.ruleeditor.export;

import de.lb.cpx.client.core.util.export.ExportListMapper;
import de.lb.cpx.model.enums.SearchListTypeEn;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.shared.filter.ColumnOption;
import de.lb.cpx.shared.filter.enums.SearchListRuleAttributes;
import de.lb.cpx.shared.lang.Translation;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author wilde
 */
public class RuleListMapper implements ExportListMapper<CrgRules>{

    private final List<ColumnOption> options;
    
    public RuleListMapper(List<ColumnOption> pOptions){
        options = pOptions;
    }
    @Override
    public List<Object> mapValues(CrgRules pDto) {
          List<Object> values = new ArrayList<>();
        if (options == null || pDto == null) {
            return values;
        }
        for (ColumnOption columnOption : options) {
            if (columnOption.attributeName.equals(SearchListRuleAttributes.ruleCaption)) {
                values.add(pDto.getCrgrCaption());
            } else if (columnOption.attributeName.equals(SearchListRuleAttributes.ruleCategory)) {
                values.add(pDto.getCrgrCategory());
            } else if (columnOption.attributeName.equals(SearchListRuleAttributes.ruleIdent)) {
                values.add(pDto.getCrgrIdentifier());
            } else if (columnOption.attributeName.equals(SearchListRuleAttributes.ruleMessage)) {
                values.add(pDto.getCrgrMessage()!=null);
            } else if (columnOption.attributeName.equals(SearchListRuleAttributes.ruleNumber)) {
                values.add(pDto.getCrgrNumber());
            } else if (columnOption.attributeName.equals(SearchListRuleAttributes.ruleStatus)) {
                values.add(pDto.getCrgrRuleErrorType().getTranslation().getValue());
            } else if (columnOption.attributeName.equals(SearchListRuleAttributes.ruleSuggestion)) {
                values.add(pDto.getCrgrSuggText());
            } else if (columnOption.attributeName.equals(SearchListRuleAttributes.ruleType)) {
                values.add(pDto.getCrgRuleTypes() == null?"":pDto.getCrgRuleTypes().getCrgtShortText());
            }
        }
        return values;
    }

    @Override
    public List<String> getTitles() {
        List<String> values = new ArrayList<>();
        if (options == null) {
            return values;
        }
        for (ColumnOption columnOption : options) {
            final String value;
            Translation trans = columnOption.getTranslation(SearchListTypeEn.RULEWL);
            if (trans == null) {
                value = columnOption.getDisplayName();
            } else {
                if (trans.hasAbbreviation()) {
                    value = trans.getAbbreviation();
                } else {
                    value = trans.getValue();
                }
            }
            values.add(value);
        }
        return values;
    }
    
}
