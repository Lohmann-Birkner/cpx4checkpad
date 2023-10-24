/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.tooltip;

/**
 * Implementation of the Case Version Tooltip to unify behaviour
 *
 * @author wilde
 */
public class SimpleMathTooltip extends BasicTooltip {

    private final String operator;
    private final String result;

    /**
     * creates default tooltip for details with default font size 15 open delay
     * 200ms visibleDuration 5000ms closeDelay 100ms hideOnExit true
     *
     * @param pOperator operator to be displayed
     * @param pResult result to show
     * @param pObjects list of line objects for math operation
     */
    public SimpleMathTooltip(String pOperator, String pResult, LineObject... pObjects) {
        this(pOperator, pResult);
        setGraphic(new SimpleMathLayout(pOperator, pResult, pObjects));
    }

    /**
     * creates default tooltip for details with default font size 15 open delay
     * 200ms visibleDuration 5000ms closeDelay 100ms hideOnExit true
     *
     * @param pOperator operator to be displayed
     * @param pResult result to show
     */
    public SimpleMathTooltip(String pOperator, String pResult) {
        super(200, 5000, 100, true);
        operator = pOperator;
        result = pResult;
    }

    public final void setLineObjects(LineObject... pObjects) {
        setGraphic(new SimpleMathLayout(operator, result, pObjects));
    }

}
