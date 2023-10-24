/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.core.model.fx.adapter;

import java.util.List;
import javafx.beans.Observable;

/**
 * Interface to handle Observable Adapters
 * @author wilde
 * @param <T> observable class
 * @param <Z> listener class
 */
public interface IWeakAdapter<T extends Observable,Z extends Object> {
    /**
     * dispose object to clear listener references
     */
    public void dispose();
    /**
     * remove listener from observable
     * @param observable object to observe
     * @param listener listener class to remove
     */
    public void remove(T observable, Z listener);
    /**
     * @param observable observable object
     * @return list of all listeners registered on observable
     */
    public List<Z> getListeners(T observable);
    /**
     * register listener on observable
     * @param observable observable object
     * @param listener listener to register
     */
    public void addChangeListener(final T observable, Z listener);
}
