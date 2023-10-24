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

import de.lb.cpx.client.core.util.VersionStringConverter;
import de.lb.cpx.model.TCaseDetails;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

/**
 * Implementation of the Case Version Tooltip to unify behaviour
 *
 * @author wilde
 */
public class CaseVersionTooltip extends CpxTooltip {

    private final VersionStringConverter converter;

    /**
     * creates new Tooltip instance with default font size 15
     *
     * @param pDetails details for the tooltip
     * @param pOpenDelay open delay in ms
     * @param pVisibleDuration time before fading in ms
     * @param pCloseDelay close delay in ms
     * @param pHideOnExit hide on exit flag
     */
    public CaseVersionTooltip(TCaseDetails pDetails, double pOpenDelay, double pVisibleDuration, double pCloseDelay, boolean pHideOnExit) {
        super(null, pOpenDelay, pVisibleDuration, pCloseDelay, pHideOnExit);
        converter = new VersionStringConverter();
        setGraphic(converter.getTooltipGraphic(pDetails));
        setFontSize(15);
        setOnShowing(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                setGraphic(converter.getTooltipGraphic(pDetails));
            }
        });
    }

    /**
     * creates default tooltip for details with default font size 15 open delay
     * 200ms visibleDuration 5000ms closeDelay 100ms hideOnExit true
     *
     * @param pDetails details for the tooltip
     */
    public CaseVersionTooltip(TCaseDetails pDetails) {
        this(pDetails, 200, 5000, 100, true);
    }

    @Override
    protected void show() {
        if (!getText().isEmpty()) {
            super.show();
        }
    }

}
