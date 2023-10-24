/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.core.menu.cache;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author niemeier
 * @param <K> key
 * @param <V> value
 */
public class MenuCacheRefreshResult<K extends Serializable, V extends Serializable> {
    
    private final K[] requestedKeys;
    private final Map<K, V> response;

    public MenuCacheRefreshResult(final K[] pRequestedKeys, final Map<K, V> pResponse) {
        this.requestedKeys = pRequestedKeys;
        this.response = pResponse;
    }

    /**
     * @return the requestedKeys
     */
    public K[] getRequestedKeys() {
        return requestedKeys;
    }

    /**
     * @return the response
     */
    public Map<K, V> getResponse() {
        return response;
    }
    
}
