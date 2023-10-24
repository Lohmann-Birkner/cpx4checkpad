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
package de.lb.cpx.client.ruleeditor.model.dnd;

import com.google.common.collect.Lists;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javafx.scene.input.DataFormat;
import javax.validation.constraints.NotNull;

/**
 *
 * @author wilde
 */
public class DndRulesToRole implements Serializable {

    public static final DataFormat DATA_FORMAT = new DataFormat("dataformat.rules.to.role");
    private static final long serialVersionUID = 1L;

    private final List<CrgRules> rules;

    public DndRulesToRole(@NotNull List<CrgRules> pRules) {
        this.rules = Objects.requireNonNull(pRules, "list of rules can not be null");
    }

    public List<CrgRules> getRules() {
        return Lists.newArrayList(rules);
    }

}
