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
package de.lb.cpx.client.core.model.fx.spinner;

import com.sun.javafx.scene.control.behavior.FocusTraversalInputMap;
import de.lb.cpx.client.core.model.fx.spinner.InputMap.KeyMapping;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import static javafx.scene.input.KeyCode.*;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

public class SpinnerBehavior<T> extends BehaviorBase<Spinner<T>> {

    // this specifies how long the mouse has to be pressed on a button
    // before the value steps. As the mouse is held down longer, we begin
    // to cut down the duration of subsequent steps (and also increase the
    // step size)
//    private static final double INITIAL_DURATION_MS = 750;
    private final InputMap<Spinner<T>> spinnerInputMap;

    private static final int STEP_AMOUNT = 1;

    private boolean isIncrementing = false;

    private Timeline timeline;

    protected final EventHandler<ActionEvent> spinningKeyFrameEventHandler = event -> {
        final SpinnerValueFactory<T> valueFactory = getNode().getValueFactory();
        if (valueFactory == null) {
            return;
        }

        if (isIncrementing) {
            increment(STEP_AMOUNT);
        } else {
            decrement(STEP_AMOUNT);
        }
    };

    /**
     * *************************************************************************
     *                                                                         *
     * Constructors * * @param spinner spinner
     * ************************************************************************
     * @param spinner spinner
     */
    public SpinnerBehavior(Spinner<T> spinner) {
        super(spinner);

        // create a map for spinner-specific mappings (this reuses the default
        // InputMap installed on the control, if it is non-null, allowing us to pick up any user-specified mappings)
        spinnerInputMap = createInputMap();
        // then spinner-specific mappings for key and mouse input
        addDefaultMapping(spinnerInputMap,
                new KeyMapping(UP, KeyEvent.KEY_PRESSED, e -> {
                    if (arrowsAreVertical()) {
                        increment(1);
                    } else {
                        FocusTraversalInputMap.traverseUp(e);
                    }
                }),
                new KeyMapping(RIGHT, KeyEvent.KEY_PRESSED, e -> {
                    if (!arrowsAreVertical()) {
                        increment(1);
                    } else {
                        FocusTraversalInputMap.traverseRight(e);
                    }
                }),
                new KeyMapping(LEFT, KeyEvent.KEY_PRESSED, e -> {
                    if (!arrowsAreVertical()) {
                        decrement(1);
                    } else {
                        FocusTraversalInputMap.traverseLeft(e);
                    }
                }),
                new KeyMapping(DOWN, KeyEvent.KEY_PRESSED, e -> {
                    if (arrowsAreVertical()) {
                        decrement(1);
                    } else {
                        FocusTraversalInputMap.traverseDown(e);
                    }
                })
        );
    }

    /**
     * *************************************************************************
     *                                                                         *
     * API * *
     * ************************************************************************
     * @return input map
     */
    @Override
    public InputMap<Spinner<T>> getInputMap() {
        return spinnerInputMap;
    }

    public void increment(int steps) {
        getNode().increment(steps);
    }

    public void decrement(int steps) {
        getNode().decrement(steps);
    }

    public void startSpinning(boolean increment) {
        isIncrementing = increment;

        if (timeline != null) {
            timeline.stop();
        }
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setDelay(getNode().getInitialDelay());
        final KeyFrame start = new KeyFrame(Duration.ZERO, spinningKeyFrameEventHandler);
        final KeyFrame repeat = new KeyFrame(getNode().getRepeatDelay());
        timeline.getKeyFrames().setAll(start, repeat);
        timeline.playFromStart();

        spinningKeyFrameEventHandler.handle(null);
    }

    public void stopSpinning() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    /**
     * *************************************************************************
     *                                                                         *
     * Implementation * *
     * ************************************************************************
     */
    private boolean arrowsAreVertical() {
        final List<String> styleClass = getNode().getStyleClass();

        return !(styleClass.contains(Spinner.STYLE_CLASS_ARROWS_ON_LEFT_HORIZONTAL)
                || styleClass.contains(Spinner.STYLE_CLASS_ARROWS_ON_RIGHT_HORIZONTAL)
                || styleClass.contains(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL));
    }
}
