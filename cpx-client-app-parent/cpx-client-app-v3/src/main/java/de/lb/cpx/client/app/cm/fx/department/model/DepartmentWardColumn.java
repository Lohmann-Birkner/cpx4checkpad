/*
 * Copyright (c) 2021 Lohmann & Birkner.
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
 *    2021  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.cm.fx.department.model;

import de.lb.cpx.server.commons.dao.AbstractEntity;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TableColumn;

/**
 *
 * @author wilde
 * @param <T> entity in row
 * @param <Z> object type in row
 */
public class DepartmentWardColumn<T extends AbstractEntity, Z extends Object> extends TableColumn<T,Z>{
    
    public DepartmentWardColumn(String pTitle){
        super(pTitle);
    }
    
    private BooleanProperty armedProperty;
    public BooleanProperty armedProperty() {
        if (armedProperty == null) {
            armedProperty = new SimpleBooleanProperty(true);
        }
        return armedProperty;
    }

    public boolean isArmed() {
        return armedProperty().get();
    }

    public void setArmed(Boolean pArmed) {
        armedProperty().set(pArmed);
    }
    private BooleanProperty editableValueProperty;
    public BooleanProperty editableValueProperty() {
        if (editableValueProperty == null) {
            editableValueProperty = new SimpleBooleanProperty(true);
        }
        return editableValueProperty;
    }

    public boolean isEditableValue() {
        return editableValueProperty().get();
    }

    public void setEditableValue(Boolean pEditable) {
        editableValueProperty().set(pEditable);
    }
}
