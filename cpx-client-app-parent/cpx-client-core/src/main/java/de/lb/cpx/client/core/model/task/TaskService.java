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
package de.lb.cpx.client.core.model.task;

import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.model.fx.adapter.WeakPropertyAdapter;
import de.lb.cpx.client.core.model.fx.alert.ProgressWaitingDialog;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;

/**
 * Service class to handle long running tasks in an unified way displays
 * progress waiting dialog as long background task is running supports
 * lifecircle methodes with afterTask and beforeTask
 *
 * @author wilde
 * @param <T> Object type of task
 */
public abstract class TaskService<T> extends Service<T> {

    private ProgressWaitingDialog progressDialog;
    private static final String DESCRIPTION_DEFAULT = "Aktion wird ausgeführt";

    public TaskService() {
        super();
        progressDialog = new ProgressWaitingDialog(this);
        progressDialog.setHeaderText("Aktion wird ausgeführt");
        progressDialog.initOwner(MainApp.getWindow());
        progressDialog.initModality(Modality.APPLICATION_MODAL);
        progressDialog.contentTextProperty().bind(descriptionProperty);
        progressDialog.getDialogPane().getStyleClass().add("dialog-show-all-text");
        VBox box= (VBox)progressDialog.getDialogPane().getContent();
        Label lbl = (Label) box.getChildren().get(0);
        lbl.setMinHeight(Label.USE_PREF_SIZE);
        lbl.setPrefHeight(Label.USE_COMPUTED_SIZE);
        lbl.setMaxHeight(Label.USE_PREF_SIZE);
//        ((Label)((VBox)progressDialog.getDialogPane().getContent()).getChildren().get(0))).setMinHeight(Label.USE_COMPUTED_SIZE);
        progressDialog.showingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    afterTask(getState());
                    progressDialog.showingProperty().removeListener(this);
                }
            }
        });
    }

    public TaskService(String pDescription) {
        this();
        setDescription(pDescription);
    }
    private final StringProperty descriptionProperty = new SimpleStringProperty(DESCRIPTION_DEFAULT);

    public final void setDescription(String pDescription) {
        descriptionProperty.set(pDescription);
    }

    public String getDescription() {
        return descriptionProperty.get();
    }

    @Override
    protected Task<T> createTask() {
        beforeTask();
        return new Task<T>() {
            @Override
            protected T call() throws Exception {
                adapter.addChangeListener(progress, new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                        updateProgress(t1.doubleValue(), 100);
                    }
                });
                return TaskService.this.call();
            }
        };
    }

    public abstract T call();

    public void beforeTask() {
    }

    public void afterTask(Worker.State pState) {
        adapter.dispose();
    }
    
    private final DoubleProperty progress = new SimpleDoubleProperty(-1.0d);
    public void setProgress(Double pDouble){
        progress.set(pDouble);
    }
    private final WeakPropertyAdapter adapter = new WeakPropertyAdapter();
}
