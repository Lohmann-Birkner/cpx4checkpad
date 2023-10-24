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
package de.lb.cpx.client.ruleeditor.model.titledPane;

import de.lb.cpx.client.core.model.fx.pane.AsyncPane;
import de.lb.cpx.client.core.model.fx.titledpane.MenuTitledPane;
import javafx.concurrent.Worker;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Menued TitledPane thats loads content async to avoid unresponsive ui, if a
 * lot of data is loaded Intended to load pools, could be alot of data
 *
 * @author wilde
 * @param <T> node type
 */
public abstract class AsyncMenuTitledPane<T extends Node> extends MenuTitledPane {

    private final AsyncPane<T> pane;
    private final VBox contentNode;

    public AsyncMenuTitledPane() {
        super();
        pane = new AsyncPane<>() {
            @Override
            public void beforeTask() {
                super.beforeTask(); //To change body of generated methods, choose Tools | Templates.
                AsyncMenuTitledPane.this.beforeTask();
            }
            
            
            @Override
            public T loadContent() {
                return AsyncMenuTitledPane.this.loadContent();
            }

            @Override
            public void afterTask(Worker.State pState) {
                super.afterTask(pState); //To change body of generated methods, choose Tools | Templates.
                AsyncMenuTitledPane.this.afterTask(pState);
            }
            
        };
        contentNode = new VBox(pane);
        contentNode.setSpacing(5);
        contentNode.setFillWidth(true);
        VBox.setVgrow(pane, Priority.ALWAYS);
        setContent(contentNode);
    }

    protected Pane getContentNode() {
        return contentNode;
    }
    public void afterTask(Worker.State pState){
        
    }
    public void beforeTask(){
        
    }
    public abstract T loadContent();

    public void reload() {
        pane.reload();
    }
}
