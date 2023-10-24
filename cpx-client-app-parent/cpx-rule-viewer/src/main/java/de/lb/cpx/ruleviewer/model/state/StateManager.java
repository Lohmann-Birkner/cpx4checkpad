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
package de.lb.cpx.ruleviewer.model.state;

import de.lb.cpx.ruleviewer.editor.state.RuleEditorStateManager;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.apache.commons.lang3.SerializationUtils;

/**
 * handles states of object versions
 *
 * @author wilde
 * @param <T> Object Type
 */
public abstract class StateManager<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;
    private T original;

    public void init(T pOrginalItem) {
        original = copyItem(pOrginalItem);
        setCurrentStateItem(pOrginalItem);
    }

    protected T copyItem(T pItem) {
        T copy = SerializationUtils.clone(pItem);
        return copy;
    }

    public boolean check() {
        return checkDirty(getCurrentStateItem());
    }

    public boolean checkDirty(T pRule) {
        if (original == null) {
            return false;
        }
        try {
            return compare(original, pRule);
        } catch (IOException ex) {
            Logger.getLogger(RuleEditorStateManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public abstract boolean compare(T pItem1, T pItem2) throws IOException;

    public void save() {
        original = copyItem(getCurrentStateItem());
//        LOG.info("save state:\n"+ORIGINAL.toString());
    }

//    private byte[] getBytes(Object obj) throws IOException {
//        ByteArrayOutputStream str2 = new ByteArrayOutputStream();
//        byte[] serialized2;
//        try ( ObjectOutputStream oos2 = new ObjectOutputStream(str2)) {
//            oos2.writeObject(obj);
//            serialized2 = str2.toByteArray();
//        }
//        return serialized2;
//    }
    public void revertToOriginalState() {
        setCurrentStateItem(copyItem(original));
    }
    private transient ObjectProperty<T> currentItemStateProperty;

    public ObjectProperty<T> currentItemStatePorperty() {
        if (currentItemStateProperty == null) {
            currentItemStateProperty = new SimpleObjectProperty<>();
        }
        return currentItemStateProperty;
    }

    public T getCurrentStateItem() {
        return currentItemStatePorperty().get();
    }

    public void setCurrentStateItem(T pITem) {
        currentItemStatePorperty().set(pITem);
    }

    public boolean isInitialized() {
        return original != null;
    }

    public T getOriginalStateItem() {
        return original != null ? copyItem(original) : null;
    }
}
