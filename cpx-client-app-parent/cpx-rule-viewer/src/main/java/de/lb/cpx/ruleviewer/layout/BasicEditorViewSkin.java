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
 *    2018  Your Organisation - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.layout;

import javafx.scene.control.SkinBase;

/**
 * Basic editor skin
 *
 * @author wilde
 * @param <T> type of basic editor
 */
public abstract class BasicEditorViewSkin<T extends BasicEditorView> extends SkinBase<T> {

    /**
     * contruct a new skin instance
     *
     * @param pSkinnable skinnable control
     */
    public BasicEditorViewSkin(T pSkinnable) {
        super(pSkinnable);
//        pSkinnable.getProperties().addListener(new MapChangeListener<Object, Object>() {
//            @Override
//            public void onChanged(MapChangeListener.Change<? extends Object, ? extends Object> change) {
//                if (change.wasAdded()) {
//                    if (BasicEditorView.REFRESH.equals(change.getKey())) {
//                        updateUi();
//                        pSkinnable.getProperties().remove(BasicEditorView.REFRESH);
//                    }
//                }
//            }
//        });
        updateUi();
//        getSkinnable().refresh();
    }

    /**
     * update ui, create new content
     */
    public abstract void updateUi();
}
