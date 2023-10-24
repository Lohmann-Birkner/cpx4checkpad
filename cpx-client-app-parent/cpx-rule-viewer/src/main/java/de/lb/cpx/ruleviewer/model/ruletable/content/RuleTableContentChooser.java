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
package de.lb.cpx.ruleviewer.model.ruletable.content;

import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.util.Callback;

/**
 *
 * @author wilde
 */
public class RuleTableContentChooser extends Control {

    private static final String DEFAULT_STYLE_CLASS = "rule-table-content-chooser";

    public RuleTableContentChooser() {
        super();
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        getStylesheets().add(getClass().getResource("/styles/Styles.css").toExternalForm());
        setMaxWidth(200);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new RuleTableContentChooserSkin(this);
    }

    private Callback<RuleTableContentEnum, Control> editorFactory = new Callback<RuleTableContentEnum, Control>() {
        @Override
        public Control call(RuleTableContentEnum p) {
            return new Label("Kein Editor konfiguriert für " + p.getTitle());
        }
    };

    public void setEditorFatory(Callback<RuleTableContentEnum, Control> pFactory) {
        editorFactory = pFactory;
    }

    public Callback<RuleTableContentEnum, Control> getEditorFactory() {
        return editorFactory;
    }

}
