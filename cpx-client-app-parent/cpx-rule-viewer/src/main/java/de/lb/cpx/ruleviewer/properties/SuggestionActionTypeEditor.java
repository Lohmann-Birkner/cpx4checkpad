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
package de.lb.cpx.ruleviewer.properties;

import de.lb.cpx.rule.criteria.TypesAndOperationsManager;
import de.lb.cpx.rule.criteria.model.CriterionTree;
import de.lb.cpx.rule.criteria.model.TypesAndOperations;
import de.lb.cpx.rule.criteria.model.TypesAndOperations.SuggActions.SuggAction;
import de.lb.cpx.ruleviewer.model.Suggestion;
import de.lb.cpx.shared.lang.Lang;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.BeanProperty;
import org.controlsfx.property.editor.PropertyEditor;

/**
 * Editor to edtit action Type in Suggeston
 *
 * @author wilde
 */
public class SuggestionActionTypeEditor implements PropertyEditor<SuggAction> {

    private static final Logger LOG = Logger.getLogger(SuggestionActionTypeEditor.class.getName());
    private static final String DATA_TYPE_ARRAY_STRING = "DATATYPE_ARRAY_STRING";
    private static final String DATA_TYPE_ARRAY = "DATATYPE_ARRAY";
    private static final String DATA_TYPE_INTEGER = "DATATYPE_INTEGER";
    private static final String DATA_TYPE_DATE = "DATATYPE_DATE";

    private final PropertySheet.Item item;
    private ComboBox<TypesAndOperations.SuggActions.SuggAction> editor;
    private Suggestion suggestion;

    public SuggestionActionTypeEditor(PropertySheet.Item pItem) {
        item = pItem;
        Object bean = ((BeanProperty) item).getBean();
        if (!(bean instanceof Suggestion)) {
            LOG.severe("bean of the editor is not a Suggestion!");
            return;
        }
        suggestion = (Suggestion) bean;
        suggestion.criterionProperty().addListener(new ChangeListener<CriterionTree.Supergroup.Group.Criterion>() {
            @Override
            public void changed(ObservableValue<? extends CriterionTree.Supergroup.Group.Criterion> observable, CriterionTree.Supergroup.Group.Criterion oldValue, CriterionTree.Supergroup.Group.Criterion newValue) {
                setEditorValues(newValue);
            }
        });
//        setValue(suggestion.getActionId());
    }

    @Override
    public Node getEditor() {
        if (editor == null) {
            editor = new ComboBox<>();

            editor.setConverter(new StringConverter<TypesAndOperations.SuggActions.SuggAction>() {
                @Override
                public String toString(TypesAndOperations.SuggActions.SuggAction object) {
                    if (object == null) {
                        return null;
                    }
                    return Lang.get(object.getDisplayName()).getValue();
                }

                @Override
                public TypesAndOperations.SuggActions.SuggAction fromString(String string) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
            editor.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<SuggAction>() {
                @Override
                public void changed(ObservableValue<? extends SuggAction> observable, SuggAction oldValue, SuggAction newValue) {
                    suggestion.setActionId(newValue);
                }
            });
//            editor.disabledProperty().addListener(new ChangeListener<Boolean>() {
//                @Override
//                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                    LOG.info("disable? " +newValue);
//                }
//            });
            setEditorValues(suggestion.getCriterion());
            setValue(suggestion.getActionId());
        }
        return editor;
    }

    @Override
    public SuggAction getValue() {
        return editor.getSelectionModel().getSelectedItem();
    }

    @Override
    public void setValue(SuggAction value) {
        if (editor == null) {
            return;
        }
        if (value == null) {
            editor.getSelectionModel().selectFirst();
            return;
        }
        editor.getSelectionModel().select(value);
    }

    private void setEditorValues(CriterionTree.Supergroup.Group.Criterion crit) {
        if (editor == null) {
            LOG.warning("editor not initialized");
            return;
        }
        TypesAndOperationsManager manager = TypesAndOperationsManager.instance();
        if (crit == null) {
            editor.getItems().setAll(manager.getSuggActions_ActList());
            return;
        }
        //available actions are based on datatype
        String datatype = crit.getCriterionType();
        if (datatype.contains(DATA_TYPE_ARRAY)) {
            if (DATA_TYPE_ARRAY_STRING.equals(datatype)) {
                editor.getItems().setAll(manager.getSuggActions_ActList());
            } else {
                editor.getItems().setAll(manager.getSuggActions_ActListAddDelete());
            }
        } else {
            if (DATA_TYPE_DATE.equals(datatype) || DATA_TYPE_INTEGER.equals(datatype)) {
                editor.getItems().setAll(manager.getSuggActions_ActListChangeOnly());
            } else {
                editor.getItems().setAll(manager.getSuggActions_ActList());
            }
        }
        if (!editor.getItems().contains(suggestion.getActionId())) {
            editor.getSelectionModel().selectFirst();
//            setValue(editor.getItems().);
        }
//        setValue(suggestion.getActionId());
//        if(!editor.getItems().contains(editor.getSelectionModel().getSelectedItem())){
//            editor.getSelectionModel().selectFirst();
//        }
    }
}
