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
package de.lb.cpx.ruleviewer.analyser.editors;

import de.lb.cpx.client.core.util.IcdHelper;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.enums.IcdcRefTypeEn;
import java.util.Collection;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.util.StringConverter;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.BeanProperty;
import org.controlsfx.property.editor.PropertyEditor;

/**
 *
 * @author wilde
 */
public class IcdRefTypeEditor implements PropertyEditor<IcdcRefTypeEn> {

    private final PropertySheet.Item item;
    private ComboBox<TCaseIcd> icdEditor;
    private ComboBox<IcdcRefTypeEn> typeEditor;
    private HBox editor;
    private static final Logger LOG = Logger.getLogger(IcdRefTypeEditor.class.getName());
    private TCaseIcd icd;

    public IcdRefTypeEditor(PropertySheet.Item property) {
        item = property;
    }

    @Override
    public Node getEditor() {
        if (editor != null) {
            return editor;
        }
        typeEditor = new ComboBox<>(FXCollections.observableArrayList(IcdcRefTypeEn.values()));
        typeEditor.setConverter(new StringConverter<IcdcRefTypeEn>() {
            @Override
            public String toString(IcdcRefTypeEn object) {
                return object != null ? object.getTranslation().getValue() : null;
            }

            @Override
            public IcdcRefTypeEn fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        typeEditor.setMaxWidth(Double.MAX_VALUE);
        typeEditor.getSelectionModel().select(getIcd().getIcdcReftypeEn());
        typeEditor.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<IcdcRefTypeEn>() {
            @Override
            public void changed(ObservableValue<? extends IcdcRefTypeEn> observable, IcdcRefTypeEn oldValue, IcdcRefTypeEn newValue) {
                clearAndSetReferences();
                CaseChangedEvent saveEvent = new CaseChangedEvent(CaseChangedEvent.caseChangedEvent());
                Event.fireEvent(editor, saveEvent);
            }
        });

        icdEditor = new ComboBox<>();
        icdEditor.setConverter(new StringConverter<TCaseIcd>() {
            @Override
            public String toString(TCaseIcd object) {
                return object != null ? object.getIcdcCode() : null;
            }

            @Override
            public TCaseIcd fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

        icdEditor.setMaxWidth(Double.MAX_VALUE);
        icdEditor.getSelectionModel().select(findSelectedIcd());
        icdEditor.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TCaseIcd>() {
            @Override
            public void changed(ObservableValue<? extends TCaseIcd> observable, TCaseIcd oldValue, TCaseIcd newValue) {
                clearAndSetReferences();
                CaseChangedEvent saveEvent = new CaseChangedEvent(CaseChangedEvent.caseChangedEvent());
                Event.fireEvent(editor, saveEvent);
            }
        });

        editor = new HBox(5.0, typeEditor, icdEditor);
        HBox.setHgrow(typeEditor, Priority.ALWAYS);
        HBox.setHgrow(icdEditor, Priority.ALWAYS);
        return editor;
    }

    @Override
    public IcdcRefTypeEn getValue() {
        return null;
    }

    @Override
    public void setValue(IcdcRefTypeEn value) {

    }

    public TCaseIcd getIcd() {
        if (icd == null) {
            Object bean = ((BeanProperty) item).getBean();
            if (!(bean instanceof TCaseIcd)) {
                LOG.severe("bean of the editor is not a TCaseIcd!");
                return null;
            }
            icd = (TCaseIcd) bean;
        }
        return icd;
    }

    public void setIcds(Collection<TCaseIcd> pIcds) {
        ComboBox<TCaseIcd> cbIcd = (ComboBox<TCaseIcd>) ((Pane) getEditor()).getChildren().get(1);
        cbIcd.getItems().clear();
        cbIcd.getItems().addAll(pIcds);
    }

    private void clearAndSetReferences() {
        IcdHelper.removeSecIcdReference(getIcd());
        TCaseIcd refIcd = getSecIcd();
        IcdcRefTypeEn type = getReferenceType();
        if (refIcd == null || type == null) {
            return;
        }
        IcdHelper.setSecIcdReference(getReferenceType(), getIcd(), getSecIcd());
        getIcd();
        getSecIcd();
    }

    public IcdcRefTypeEn getReferenceType() {
        ComboBox<IcdcRefTypeEn> cb = (ComboBox<IcdcRefTypeEn>) ((Pane) getEditor()).getChildren().get(0);
        return cb.getSelectionModel().getSelectedItem();
    }

    public TCaseIcd getSecIcd() {
        ComboBox<TCaseIcd> cb = (ComboBox<TCaseIcd>) ((Pane) getEditor()).getChildren().get(1);
        return cb.getSelectionModel().getSelectedItem();
    }

    private TCaseIcd findSelectedIcd() {
        if (getIcd().getRefIcd() != null) {
            return getIcd().getRefIcd();
        }
        if (!getIcd().getRefIcds().isEmpty()) {
            return getIcd().getRefIcds().iterator().next();
        }
        return null;
    }
}
