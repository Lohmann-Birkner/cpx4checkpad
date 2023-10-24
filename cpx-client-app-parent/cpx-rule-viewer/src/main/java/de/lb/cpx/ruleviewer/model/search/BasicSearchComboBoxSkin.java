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
package de.lb.cpx.ruleviewer.model.search;

import de.lb.cpx.client.core.model.fx.adapter.WeakPropertyAdapter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * Basic SearchComboBox skin to handle apperance the same way
 *
 * @author wilde
 * @param <T> type
 */
public abstract class BasicSearchComboBoxSkin<T> extends SkinBase<BasicSearchComboBox<T>> {

    private final HBox box;
    protected final ComboBox<T> cb;
    protected final Button btn;
    private final WeakPropertyAdapter adapter;
    
    public BasicSearchComboBoxSkin(BasicSearchComboBox<T> pSkinnable) {
        super(pSkinnable);
        adapter = new WeakPropertyAdapter();
        box = new HBox(5);
        getChildren().add(box);
        cb = getComboBox();
        btn = getMenuButton();
        HBox.setHgrow(cb, Priority.ALWAYS);
        cb.maxWidthProperty().bind(box.widthProperty().multiply(0.75));
        cb.getSelectionModel().select(pSkinnable.getSelectedItem());
        initListeners();
//        pSkinnable.selectedItemProperty().bind(cb.getSelectionModel().selectedItemProperty());
        addChild(cb);
        addChild(btn);
    }
    public void initListeners(){
        adapter.addChangeListener(cb.getSelectionModel().selectedItemProperty(),new ChangeListener<T>() {
            @Override
            public void changed(ObservableValue<? extends T> ov, T t, T t1) {
                getSkinnable().selectItem(t1);
            }
        });
        adapter.addChangeListener(getSkinnable().selectedItemProperty(),new ChangeListener<T>() {
            @Override
            public void changed(ObservableValue<? extends T> ov, T t, T t1) {
                cb.getSelectionModel().select(t1);
            }
        });
    }
    public final WeakPropertyAdapter getAdapter(){
        return adapter;
    }
    protected final void addChild(Node pNode) {
        box.getChildren().add(pNode);
    }

    public abstract ComboBox<T> getComboBox();

    public abstract Button getMenuButton();
}
