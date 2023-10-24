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

import de.lb.cpx.rule.criteria.model.CriterionTree;
import de.lb.cpx.rule.criteria.model.TypesAndOperations;
import de.lb.cpx.ruleviewer.model.Suggestion;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.BeanProperty;

/**
 * ValueEditor for suggestions
 *
 * @author wilde
 */
public abstract class SuggestionValueEditor extends ValueEditor {

    private static final Logger LOG = Logger.getLogger(SuggestionValueEditor.class.getName());

    protected Suggestion suggestion;

    public SuggestionValueEditor(PropertySheet.Item item) {
        super(item);
        getSuggestion().criterionProperty().addListener(new ChangeListener<CriterionTree.Supergroup.Group.Criterion>() {
            @Override
            public void changed(ObservableValue<? extends CriterionTree.Supergroup.Group.Criterion> observable, CriterionTree.Supergroup.Group.Criterion oldValue, CriterionTree.Supergroup.Group.Criterion newValue) {
                refreshEditor();
            }
        });
    }

    @Override
    public CriterionTree.Supergroup.Group.Criterion getCriterion() {
        return getSuggestion() != null ? getSuggestion().getCriterion() : null;
    }

    @Override
    public Suggestion getSuggestion() {
        if (suggestion == null) {
            Object bean = ((BeanProperty) getItem()).getBean();
            if (!(bean instanceof Suggestion)) {
                LOG.severe("bean of the editor is not a Suggestion!");
                return null;
            }
            suggestion = (Suggestion) bean;
        }
        return suggestion;
    }
    
    public boolean isEmptyOperator(TypesAndOperations.OperationGroups.OperationGroup.Operation pOperation){
        if(pOperation == null){
            return true;
        }
        return pOperation.getName().isEmpty();
    }
}
