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
 * Based upon: http://www.hackerav.com/?post=155766
 * Most of the code can maybe removed, importent part is blockign updating visible amount to prevent "jumps" on uneven list cells
 * 
 * Contributors:
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.util;

import java.util.function.ToDoubleFunction;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.util.Duration;

/**
 * Smooth scroll helper, tries to scroll by definied frictions
 *
 * @author wilde
 */
public class SmoothScrollbarHelper {

    private SmoothScrollbarHelper() {
        //
    }

    private static ScrollBar getScrollbarComponent(ListView<?> control, Orientation orientation) {
        Node n = control.lookup(".scroll-bar");
        if (n instanceof ScrollBar) {
            final ScrollBar bar = (ScrollBar) n;
            if (bar.getOrientation().equals(orientation)) {
                return bar;
            }
        }

        return null;
    }

    public static void smoothVScrollingListView(ListView<?> listView, double speed) {
        smoothScrollingListView(listView, speed, Orientation.VERTICAL, Bounds::getHeight);
    }

    public static void smoothHScrollingListView(ListView<?> listView, double speed) {
        smoothScrollingListView(listView, speed, Orientation.HORIZONTAL, Bounds::getHeight);
    }

    private static void smoothScrollingListView(ListView<?> listView, double speed, Orientation orientation, ToDoubleFunction<Bounds> sizeFunc) {
        ScrollBar scrollBar = getScrollbarComponent(listView, orientation);
        if (scrollBar == null) {
            return;
        }
        scrollBar.setUnitIncrement(5);
        final double[] frictions = {0.99, 0.1, 0.05, 0.04, 0.03, 0.02, 0.01, 0.04, 0.01, 0.008, 0.008, 0.008, 0.008, 0.0006, 0.0005, 0.00003, 0.00001};
        final double[] pushes = {speed};
        final double[] derivatives = new double[frictions.length];
        final double[] lastVPos = {0};
        Timeline timeline = new Timeline();
        final EventHandler<MouseEvent> dragHandler = event -> timeline.stop();
        final EventHandler<ScrollEvent> scrollHandler = event -> {
            scrollBar.valueProperty().set(lastVPos[0]);
            if (event.getEventType() == ScrollEvent.SCROLL) {
                double direction = event.getDeltaY() > 0 ? -1 : 1;
                for (int i = 0; i < pushes.length; i++) {
                    derivatives[i] += direction * pushes[i];
                }
                if (timeline.getStatus() == Animation.Status.STOPPED) {
                    timeline.play();
                }

            }
            event.consume();
        };
        if (scrollBar.getParent() != null) {
            scrollBar.getParent().addEventHandler(MouseEvent.DRAG_DETECTED, dragHandler);
            scrollBar.getParent().addEventHandler(ScrollEvent.ANY, scrollHandler);
        }
        scrollBar.parentProperty().addListener((o, oldVal, newVal) -> {
            if (oldVal != null) {
                oldVal.removeEventHandler(MouseEvent.DRAG_DETECTED, dragHandler);
                oldVal.removeEventHandler(ScrollEvent.ANY, scrollHandler);
            }
            if (newVal != null) {
                newVal.addEventHandler(MouseEvent.DRAG_DETECTED, dragHandler);
                newVal.addEventHandler(ScrollEvent.ANY, scrollHandler);
            }
        });

        //guess only importent part here
        scrollBar.visibleAmountProperty().addListener(new ChangeListener<Number>() {
            private Number amount;

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (oldValue == null || oldValue.doubleValue() == 0.0) {
                    amount = newValue;
                    return;
                }
                if (amount != null && amount.doubleValue() != newValue.doubleValue()) {
                    scrollBar.setVisibleAmount(oldValue.doubleValue());
                }
            }
        });
        //end

        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(3), (event) -> {
            for (int i = 0; i < derivatives.length; i++) {
                derivatives[i] *= frictions[i];
            }
            for (int i = 1; i < derivatives.length; i++) {
                derivatives[i] += derivatives[i - 1];
            }
            double dy = derivatives[derivatives.length - 1];
            double size = sizeFunc.applyAsDouble(scrollBar.getLayoutBounds());
            scrollBar.valueProperty().set(Math.min(Math.max(scrollBar.getValue() + dy / size, 0), 1));
            lastVPos[0] = scrollBar.getValue();
            if (Math.abs(dy) < 1 && Math.abs(dy) < 0.001) {
                timeline.stop();
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
    }

}
