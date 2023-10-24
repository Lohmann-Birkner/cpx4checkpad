/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.task;

import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.CpxScene;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.scene.Cursor;
import javafx.scene.Scene;

/**
 *
 * @author Dirk Niemeier
 * @param <V> Rückgabetyp
 */
public abstract class CpxTask<V> extends Task<V> {

    private static final Logger LOG = Logger.getLogger(CpxTask.class.getSimpleName());

    public ObservableValue<? extends Cursor> binding
            = Bindings.when(this.runningProperty())
                    .then(Cursor.WAIT)
                    .otherwise(Cursor.DEFAULT);

    /**
     * creates new task
     */
    public CpxTask() {
        super();
    }

    /**
     * creates new task with controller to manipulate cursor
     *
     * @param cpx_controller controller
     */
    public CpxTask(Controller<? extends CpxScene> cpx_controller) {
        this();
        Scene scene = cpx_controller.getScene();
        if (scene != null) {
            //scene.cursorProperty().
            scene.cursorProperty().bind(binding);
        }
    }

    /**
     * set controller for binding
     *
     * @param cpx_controller controller for cursor binding
     */
    public void setController(Controller<? extends CpxScene> cpx_controller) {
        Scene scene = cpx_controller.getScene();
        if (scene != null) {
            scene.cursorProperty().bind(binding);
        }
    }

    /**
     * start task in extra thread instance
     */
    public void start() {
        if (!isRunning()) {
            Thread thread = new Thread(this);
            thread.start();
        }
    }

    /**
     * stops task
     *
     * @return indicator if stopping was successful
     */
    public boolean stop() {
        if (isRunning()) {
            LOG.info("Will cancel task...");
            cancel();
            while (!isDone()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                    Thread.currentThread().interrupt();
                }
            }
            LOG.log(Level.INFO, "Previous task was cancelled");
            dispose();
            return true;
        }
        return false;
    }

    /**
     * dispose lifecycle methode for clean up
     */
    public void dispose() {
    }

}
