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
package de.lb.cpx.client.core.model.fx.pane;

import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.model.async.IAsyncObject;
import de.lb.cpx.client.core.model.task.CpxTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javax.ejb.AsyncResult;

/**
 * Async Pane Class to load content in extra task
 *
 * @author wilde
 * @param <T> node type
 */
public abstract class AsyncPane<T extends Node> extends Control implements IAsyncObject<T> {

    private static final Logger LOG = Logger.getLogger(AsyncPane.class.getName());

    private ObjectProperty<Node> contentProperty;
    private LoadingPane<T> loadingLayout;
    private CpxTask<T> loadingTask;
    private final Boolean autoLoadContent;

    public AsyncPane() {
        this(true);
    }

    public AsyncPane(Boolean pAutoLoadContent) {
        super();
        autoLoadContent = pAutoLoadContent;
    }

    public Boolean getAutoLoadContent() {
        return autoLoadContent;
    }

    /**
     * @return content node to be displayed in the pane, if loading -
     * loadingLayout is shown, otherwise loading result
     */
    public ObjectProperty<Node> contentProperty() {
        if (contentProperty == null) {
            contentProperty = new SimpleObjectProperty<>(getLoadingLayout());
        }
        return contentProperty;
    }

    /**
     * @return currently displayed content
     */
    public Node getContent() {
        return contentProperty().get();
    }

    /**
     * @param pContent content to display
     */
    protected void setContent(Node pContent) {
        contentProperty().set(pContent);
    }

    /**
     * @return content to be loaded async, trigger new computation of the
     * desired content
     */
    public abstract T loadContent();

    /**
     * @return loading pane
     */
    public LoadingPane<T> getLoadingLayout() {
        if (loadingLayout == null) {
            loadingLayout = new LoadingPane<>(this);
        }
        return loadingLayout;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new AsyncPaneSkin<>(this);
    }

    @Override
    public void afterTask(Worker.State pState) {
        loadingTask = null;
    }

    @Override
    public void beforeTask() {
        if (loadingTask != null && loadingTask.isRunning()) {
            LOG.log(Level.INFO, "Will cancel previous started loading now...");
            loadingTask.cancel();
            while (loadingTask != null && !loadingTask.isDone()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                    Thread.currentThread().interrupt();
                }
                loadingTask = null;
            }
            LOG.log(Level.INFO, "Previous started loading was cancelled");
        }
        setContent(loadingLayout);
    }

    @Override
    public boolean isAbortable() {
        return false;
    }

    @Override
    public CpxTask<T> getTask() {
//        if (loadingTask == null) {
        loadingTask = new LoadingTask();
//        }
        return loadingTask;
    }
    public boolean isRunning(){
        if(loadingTask == null){
            return false;
        }
        return loadingTask.isRunning();
    }
    @Override
    public void reload() {
        beforeTask();
        loadingTask = getTask();
        loadingTask.start();
    }

    public void refresh() {

    }

    /*
     * PRIVATE CLASSES 
     */
    private class LoadingTask extends CpxTask<T> {

        private Future<T> futureResult;

        /**
         * creates new instance
         */
        public LoadingTask() {
            setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent t) {
                    onFailed();
                    dispose();
                }
            });
            setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent t) {
                    onCancel();
                    dispose();
                }
            });
            setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    onSucceed();
                    dispose();
                }

            });
        }

        @Override
        protected T call() throws Exception {
            final long startTime2 = System.currentTimeMillis();
            futureResult = new AsyncResult<>(loadContent());
            T result = futureResult.get();
            LOG.log(Level.INFO, "call() took " + (System.currentTimeMillis() - startTime2) + " ms");
            return result;
        }

        @Override
        public void dispose() {
            super.dispose();
            afterTask(getState());
        }

        //on failed show exception 
        private void onFailed() {
            Throwable ex = getException();
            LOG.log(Level.SEVERE, "Failed to load : " + ex.getMessage(), ex);
            BasicMainApp.showErrorMessageDialog(ex, "Error occured when executing loading");
        }

        //on cancel stop future execution
        private void onCancel() {
            if (futureResult != null && !futureResult.isCancelled()) {
                futureResult.cancel(true);
            }
        }

        //on success transform result if neccessary and set items in table 
        private void onSucceed() {
            T result;
            try {
                result = get();
            } catch (ExecutionException ex) {
                LOG.log(Level.SEVERE, null, ex);
                return;
            } catch (InterruptedException ex) {
                LOG.log(Level.SEVERE, null, ex);
                Thread.currentThread().interrupt();
                return;
            }
            if (result != null) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        final long startTime = System.currentTimeMillis();
//                        ((FlowPane)result).getChildren();
                        setContent(result);
                        LOG.log(Level.INFO, "setContent() took " + (System.currentTimeMillis() - startTime) + " ms ");
                    }
                });
            }

        }
    }

}
