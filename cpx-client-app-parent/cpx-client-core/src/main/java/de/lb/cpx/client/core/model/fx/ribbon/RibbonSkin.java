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
package de.lb.cpx.client.core.model.fx.ribbon;

import javafx.beans.binding.Bindings;
import javafx.scene.Parent;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TabPane;

/**
 * Skin impl of the Ribbon Control
 *
 * @author wilde
 */
public class RibbonSkin extends SkinBase<Ribbon> {

    private TabPane pPane;

    /**
     * cobstruct new instance
     *
     * @param pSkinnable object to construct skin for
     */
    public RibbonSkin(Ribbon pSkinnable) {
        super(pSkinnable);
        Parent layout = initLayout();
        getChildren().add(layout);
    }

    //init the layout
    private Parent initLayout() {
        pPane = new TabPane();
        pPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        Bindings.bindContent(pPane.getTabs(), getSkinnable().getItems());
        pPane.sideProperty().bindBidirectional(getSkinnable().sideProperty());
        return pPane;
    }

}
