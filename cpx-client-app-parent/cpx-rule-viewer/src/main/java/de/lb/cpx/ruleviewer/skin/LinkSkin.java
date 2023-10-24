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
package de.lb.cpx.ruleviewer.skin;

import de.lb.cpx.ruleviewer.model.Link;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

/**
 *
 * @author wilde
 */
public class LinkSkin extends SelectableControlSkin<Link> {

    private Label lblOperator;

    public LinkSkin(final Link pSkinnable) throws IOException {
        super(pSkinnable);
        lblOperator.textProperty().bind(pSkinnable.operatorProperty().asString());
    }

    @Override
    protected Pane loadRoot() throws IOException {
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/Link.fxml"));
        lblOperator = (Label) root.lookup("#lblOperator");
        lblOperator.fontProperty().bind(getSkinnable().fontProperty());
        return root;
    }

}
