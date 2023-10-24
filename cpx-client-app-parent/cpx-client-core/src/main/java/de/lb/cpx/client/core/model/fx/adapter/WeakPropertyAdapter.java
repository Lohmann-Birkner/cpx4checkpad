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
package de.lb.cpx.client.core.model.fx.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * Property Adapter to manage listeners registered for observable property
 * @author wilde
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class WeakPropertyAdapter implements IWeakAdapter<ObservableValue, ChangeListener<? extends Object>>{

    Map<ObservableValue, List<ChangeListener<? extends Object>>> listenerRefs = new HashMap<>();

    @Override
    public void dispose() {
        Iterator<ObservableValue> it = listenerRefs.keySet().iterator();
        while (it.hasNext()) {
            ObservableValue next = it.next();
            for (ChangeListener<? extends Object> listener : listenerRefs.get(next)) {
                next.removeListener(listener);
            }
        }
        listenerRefs.clear();
    }

    @Override
    public final void remove(ObservableValue observable, ChangeListener<? extends Object> listener) {
        List<ChangeListener<? extends Object>> listeners = listenerRefs.get(observable);
        Iterator<ChangeListener<? extends Object>> it = listeners.iterator();
        while (it.hasNext()) {
            ChangeListener<? extends Object> next = it.next();
            if (next.equals(listener)) {
                observable.removeListener(listener);
                it.remove();
                return;
            }
        }

    }

    @Override
    public final List<ChangeListener<? extends Object>> getListeners(ObservableValue observalbe) {
        if (!listenerRefs.containsKey(observalbe)) {
            listenerRefs.put(observalbe, new ArrayList<>());
        }
        return listenerRefs.get(observalbe);
    }

    @Override
    public final void addChangeListener(final ObservableValue observable, ChangeListener<? extends Object> listener) {
        getListeners(observable).add(listener);
        observable.addListener(listener);
    }

//    public final <T> WeakListChangeListener<T> addListChangeListener(final ObservableValue observable,ListChangeListener<T> listener) {
//        getListeners(ob).add(listener);
//        return new WeakListChangeListener<>(listener);
//    }
//
//    public void addInvalidationListener(final Observable listened, InvalidationListener listener) {
//        getListeners(listened).add(listener);
//        listened.addListener(new WeakInvalidationListener(listener));
//    }
//
//    public final void stringBind(final StringProperty propertyToUpdate, final StringExpression expressionToListen) {
//        ChangeListener<String> listener = new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> ov, String t, String name) {
//                propertyToUpdate.set(name);
//            }
//        };
//        listenerRefs.add(listener);
//        expressionToListen.addListener(new WeakChangeListener<>(listener));
//        listener.changed(null, null, expressionToListen.get());
//    }
//
//    public final void booleanBind(final BooleanProperty propertyToUpdate, final BooleanExpression expressionToListen) {
//        ChangeListener<Boolean> listener = new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean name) {
//                propertyToUpdate.set(name);
//            }
//        };
//        listenerRefs.add(listener);
//        expressionToListen.addListener(new WeakChangeListener<>(listener));
//        propertyToUpdate.set(expressionToListen.get());
//    }

}
