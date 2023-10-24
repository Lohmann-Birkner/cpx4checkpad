/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.async;

import de.lb.cpx.client.core.model.task.CpxTask;
import javafx.concurrent.Worker;
import javafx.scene.Node;

/**
 * Interface to handle AsyncObject, should be used in
 * AsyncPane,AsyncTableView,AsyncListview
 *
 * @author wilde
 * @param <T> type of task
 */
public interface IAsyncObject<T extends Node> {

    /**
     * trigger after task is complete
     *
     * @param pState state in which the task has been ended
     */
    void afterTask(Worker.State pState);

    /**
     * lifecircle before task is started
     */
    void beforeTask();

    /**
     * @return if task could be stopped via extra button
     */
    boolean isAbortable();

    /**
     * @return get the task currently running, or last task that has been
     * executed, null if nothing was executed
     */
    CpxTask<T> getTask();

    /**
     * trigger whole loading process
     */
    void reload();
}
