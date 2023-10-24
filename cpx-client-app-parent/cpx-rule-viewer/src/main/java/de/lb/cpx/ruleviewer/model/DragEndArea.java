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
package de.lb.cpx.ruleviewer.model;

import de.lb.cpx.ruleviewer.skin.DragEndAreaSkin;
import de.lb.cpx.ruleviewer.util.SeverityEn;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.util.Callback;

/**
 *
 * @author wilde
 */
public abstract class DragEndArea extends Control {

    public static final PseudoClass SELECTED_NODE_PSEUDO_CLASS = PseudoClass.getPseudoClass("drag_hover");

    private SimpleObjectProperty<DataFormat> formatProperty;

    private Callback<DragEvent, Void> onDropDetected = new Callback<DragEvent, Void>() {
        @Override
        public Void call(DragEvent param) {
//            getChildren().add(SelectableControlFactory.instance().createControl(param));
            if (param.getGestureSource() instanceof Node) {
                getChildren().add((Node) param.getGestureSource());
            }
            return null;
        }
    };
    private final StringProperty placeholderTextProperty = new SimpleStringProperty("No Item added");
    private final ObjectProperty<Node> contentProperty = new SimpleObjectProperty<>(null);
    private ObjectProperty<SeverityEn> severityLevelProperty;

    public DragEndArea() {
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        try {
            return new DragEndAreaSkin(this);
        } catch (IOException ex) {
            Logger.getLogger(DragEndArea.class.getName()).log(Level.SEVERE, null, ex);
        }

        return super.createDefaultSkin();
    }

    public void setOnDropDetected(Callback<DragEvent, Void> pCallback) {
        onDropDetected = pCallback;
    }

    public Callback<DragEvent, Void> getOnDropDetected() {
        return onDropDetected;
    }

    public final void setPlaceholderText(String pText) {
        placeholderTextProperty().set(pText);
    }

    public String getPlaceholderText() {
        return placeholderTextProperty().get();
    }

    public StringProperty placeholderTextProperty() {
        return placeholderTextProperty;
    }

    public void setContent(Node pContent) {
        contentProperty().set(pContent);
    }

    public Node getContent() {
        return contentProperty().get();
    }

    public ObjectProperty<Node> contentProperty() {
        return contentProperty;
    }

    public ObjectProperty<DataFormat> dataFormatProperty() {
        if (formatProperty == null) {
            formatProperty = new SimpleObjectProperty<>();
        }
        return formatProperty;
    }

    public void setDataFormat(DataFormat pFormat) {
        dataFormatProperty().set(pFormat);
    }

    public DataFormat getDataFormat() {
        return dataFormatProperty().get();
    }

    public abstract boolean checkDragEvent(DragEvent event);

    public ObjectProperty<SeverityEn> severityLevelProperty() {
        if (severityLevelProperty == null) {
            severityLevelProperty = new SimpleObjectProperty<>(SeverityEn.NONE);
        }
        return severityLevelProperty;
    }

    public SeverityEn getSeverityLevel() {
        return severityLevelProperty().get();
    }

    public void setSeverityLevel(SeverityEn pLevel) {
        severityLevelProperty().set(pLevel);
    }

    public boolean isSevLvlError() {
        return SeverityEn.ERROR.equals(getSeverityLevel());
    }

    public boolean isSevLvlWarning() {
        return SeverityEn.WARNING.equals(getSeverityLevel());
    }

    public boolean isSevLvlInfo() {
        return SeverityEn.INFORMATION.equals(getSeverityLevel());
    }

    public boolean isSevLvlNone() {
        return SeverityEn.NONE.equals(getSeverityLevel());
    }
}
