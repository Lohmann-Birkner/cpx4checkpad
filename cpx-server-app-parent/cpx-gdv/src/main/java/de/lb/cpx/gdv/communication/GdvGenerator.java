/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.gdv.communication;

import de.lb.cpx.gdv.messages.ObjectFactory;

/**
 *
 * @author gerschmann
 */
public class GdvGenerator {
    
    private static ObjectFactory objectFactory;
    
        public static synchronized ObjectFactory getInstance() {
        if (objectFactory == null) {
            objectFactory = new ObjectFactory();
            
        }
        return objectFactory;
    }

    
}
