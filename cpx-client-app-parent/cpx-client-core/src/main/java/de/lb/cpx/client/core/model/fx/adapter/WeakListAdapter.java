/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.core.model.fx.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 * Property Adapter to manage listeners registered for observable list
 * @author wilde
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class WeakListAdapter implements IWeakAdapter<ObservableList<? extends Object>,ListChangeListener>{

    Map<ObservableList<? extends Object>, List<ListChangeListener>> listenerRefs = new HashMap<>();

    @Override
    public void dispose() {
        Iterator<ObservableList<? extends Object>> it = listenerRefs.keySet().iterator();
        while (it.hasNext()) {
            ObservableList<? extends Object> next = it.next();
            if(next == null){
                continue;
            }
            for (ListChangeListener listener : getListeners(next)) {
                next.removeListener(listener);
            }
        }
        listenerRefs.clear();
    }

    @Override
    public final void remove(ObservableList<? extends Object> observable, ListChangeListener listener) {
        List<ListChangeListener> listeners = listenerRefs.get(observable);
        Iterator<ListChangeListener> it = listeners.iterator();
        while (it.hasNext()) {
            ListChangeListener<? extends Object> next = it.next();
            if (next.equals(listener)) {
                observable.removeListener(listener);
                it.remove();
                return;
            }
        }

    }

    @Override
    public final List<ListChangeListener> getListeners(ObservableList<? extends Object> observalbe) {
        if (!listenerRefs.containsKey(observalbe)) {
            listenerRefs.put(observalbe, new ArrayList<>());
        }
        return Objects.requireNonNullElse(listenerRefs.get(observalbe), new ArrayList<>());
    }

    @Override
    public final void addChangeListener(final ObservableList<? extends Object> observable, ListChangeListener listener) {
        getListeners(observable).add(listener);
        observable.addListener(listener);
    }
    
}
