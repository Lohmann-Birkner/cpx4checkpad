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
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

/**
 * Property Adapter to manage listeners registered for observable map
 * @author wilde
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class WeakMapAdapter implements IWeakAdapter<ObservableMap<Object,Object>, MapChangeListener<? extends Object,? extends Object>>{

    Map<ObservableMap<Object,Object>, List<MapChangeListener<? extends Object,? extends Object>>> listenerRefs = new HashMap<>();

    @Override
    public void dispose() {
        Iterator<ObservableMap<Object,Object>> it = listenerRefs.keySet().iterator();
        while (it.hasNext()) {
            ObservableMap<Object,Object> next = it.next();
            for (MapChangeListener listener : listenerRefs.get(next)) {
                next.removeListener(listener);
            }
        }
        listenerRefs.clear();
    }

    @Override
    public final void remove(ObservableMap observable, MapChangeListener listener) {
        List<MapChangeListener<? extends Object, ? extends Object>> listeners = listenerRefs.get(observable);
        Iterator<MapChangeListener<? extends Object, ? extends Object>> it = listeners.iterator();
        while (it.hasNext()) {
            MapChangeListener<? extends Object, ? extends Object> next = it.next();
            if (next.equals(listener)) {
                observable.removeListener(listener);
                it.remove();
                return;
            }
        }

    }

    @Override
    public List<MapChangeListener<? extends Object, ? extends Object>> getListeners(ObservableMap<Object,Object> observalbe) {
        if (!listenerRefs.containsKey(observalbe)) {
            listenerRefs.put(observalbe, new ArrayList<>());
        }
        return listenerRefs.get(observalbe);
    }

    @Override
    public final void addChangeListener(final ObservableMap observable, MapChangeListener listener) {
        getListeners(observable).add(listener);
        observable.addListener(listener);
    }
    
}
