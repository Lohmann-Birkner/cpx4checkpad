/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.ruleeditor.util;

import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;

/**
 *
 * @author wilde
 */
public class RuleListHelper {

    public static final String getSelectedRuleNumbers(@NotNull List<CrgRules> pRules) {
        List<CrgRules> rules = Objects.requireNonNull(pRules, "rules can not be null");
        String numbers = rules.stream()
                .map(n -> n.getCrgrNumber())
                .collect(Collectors.joining(","));
        return numbers;
    }

    public static final List<Long> getSelectedRuleIds(@NotNull List<CrgRules> pRules) {
        List<CrgRules> rules = Objects.requireNonNull(pRules, "rules can not be null");
        List<Long> ids = rules.stream().map(n -> n.getId()).collect(Collectors.toList());
        return ids;
    }
}
