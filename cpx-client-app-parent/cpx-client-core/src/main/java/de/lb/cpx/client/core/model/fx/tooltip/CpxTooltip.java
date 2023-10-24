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

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Tooltip implementation to hack tooltip behaviour Should be modifiable in
 * Java9 by default, than class can be refactored Code is copyed from
 * StackOverFlow
 *
 * @author wilde
 */
public class CpxTooltip extends BasicTooltip {

    /**
     * creates default tooltip additional behavior must be added via methode
     * sets opendelay to 200ms, visible duration to 3000ms,close delay to 100ms
     * and hideOnExit to true
     *
     * @param pText tooltip text
     */
    public CpxTooltip(String pText) {
        this(pText, 200, 3000, 100, true);
    }

    /**
     * creates new tooltip instance with updated behavior
     *
     * @param pText tooltip text
     * @param pOpenDelay The open delay, knowing that by default it is set to
     * 1000.
     * @param pVisibleDuration The visible duration, knowing that by default it
     * is set to 5000.
     * @param pCloseDelay The close delay, knowing that by default it is set to
     * 200.
     * @param pHideOnExit Indicates whether the tooltip should be hide on exit,
     * knowing that by default it is set to false.
     */
    public CpxTooltip(String pText, double pOpenDelay, double pVisibleDuration, double pCloseDelay, boolean pHideOnExit) {
        this(null, pText, pOpenDelay, pVisibleDuration, pCloseDelay, pHideOnExit);
    }

    /**
     * creates new tooltip instance with updated behavior
     *
     * @param pTitle tooltip title
     * @param pText tooltip text
     * @param pOpenDelay The open delay, knowing that by default it is set to
     * 1000.
     * @param pVisibleDuration The visible duration, knowing that by default it
     * is set to 5000.
     * @param pCloseDelay The close delay, knowing that by default it is set to
     * 200.
     * @param pHideOnExit Indicates whether the tooltip should be hide on exit,
     * knowing that by default it is set to false.
     */
    public CpxTooltip(String pTitle, String pText, double pOpenDelay, double pVisibleDuration, double pCloseDelay, boolean pHideOnExit) {
        super(pTitle, pText);
        updateTooltipBehavior(
                pOpenDelay,
                Double.doubleToRawLongBits(pVisibleDuration) != Double.doubleToRawLongBits(-1d) ? pVisibleDuration : Double.POSITIVE_INFINITY,
                pCloseDelay,
                pHideOnExit
        );
        if (pTitle == null || pTitle.trim().isEmpty()) {
            removeTitle();
        }
    }

    public VBox getContent() {
        return (VBox) getGraphic();
    }

    public Label getTitleLabel() {
        VBox box = getContent();
        Label title = null;
        if (box != null) {
            for (Node n : box.getChildren()) {
                if (n instanceof Label) {
                    title = (Label) n;
                    break;
                }
            }
        }
        return title;
    }

    public final boolean removeTitle() {
        VBox content = getContent();
        if (content != null) {
            Label title = getTitleLabel();
            if (title != null) {
                return content.getChildren().remove(title);
            }
        }
        return false;
    }

//    /**
//     * @param pFontSize set font size in px , sets value in style class, may or
//     * may not override font settings in the css
//     * @return updated instance
//     */
//    @Override
//    public BasicTooltip setFontSize(int pFontSize) {
//        setFont(new Font(pFontSize));
//        return this;
//    }
    /**
     * Hack allowing to modify the default behavior of the tooltips.
     *
     * @param openDelay The open delay, knowing that by default it is set to
     * 1000.
     * @param visibleDuration The visible duration, knowing that by default it
     * is set to 5000.
     * @param closeDelay The close delay, knowing that by default it is set to
     * 200.
     * @param hideOnExit Indicates whether the tooltip should be hide on exit,
     * knowing that by default it is set to false.
     */
    public final void updateTooltipBehavior(double openDelay, double visibleDuration,
            double closeDelay, boolean hideOnExit) {
        this.setShowDelay(new Duration(openDelay));
        this.setShowDuration(new Duration(visibleDuration));
        this.setHideDelay(new Duration(closeDelay));
        this.setHideOnEscape(hideOnExit);

//        //TooltipBehavior(Duration openDelay, Duration visibleDuration, Duration closeDelay, final boolean hideOnExit)
//        try {
//            // Get the non public field "BEHAVIOR"
//            Field fieldBehavior = Tooltip.class.getDeclaredField("BEHAVIOR");
//            // Make the field accessible to be able to get and set its value
//            fieldBehavior.setAccessible(true);
//            // Get the value of the static field
//            Object objBehavior = fieldBehavior.get(null);
//            // Get the constructor of the private static inner class TooltipBehavior
////            Constructor<?> constructor = objBehavior.getClass().getDeclaredConstructor(
////                    Duration.class, Duration.class, Duration.class, boolean.class
////            );
//            Constructor<?> constructor = objBehavior.getClass().getDeclaredConstructor(
//                    boolean.class
//            );
//            // Make the constructor accessible to be able to invoke it
//            constructor.setAccessible(true);
//            // Create a new instance of the private static inner class TooltipBehavior
////            Object tooltipBehavior = constructor.newInstance(
////                    new Duration(openDelay), new Duration(visibleDuration),
////                    new Duration(closeDelay), hideOnExit
////            );
//            Object tooltipBehavior = constructor.newInstance(
//                    hideOnExit
//            );
//            // Set the new instance of TooltipBehavior
//            fieldBehavior.set(null, tooltipBehavior);
//        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchFieldException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
//            throw new IllegalStateException(e);
//        }
    }
}
