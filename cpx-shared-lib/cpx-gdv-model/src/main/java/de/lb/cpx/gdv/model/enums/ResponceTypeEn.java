/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.gdv.model.enums;

/**
 *
 * @author gerschmann
 */
public enum ResponceTypeEn {

    BEAUFTRAGUNG(){
        public String toString(){
            return "Beauftragung";
        }
    },
    RUEKANTWORT(){
        public String toString(){
            return "Rückantwort";
        }
    },
    RECHNUNG(){
        public String toString(){
            return "Rechnung";
        }
    };
    
    private static final ResponceTypeEn[] types4send = new ResponceTypeEn[]{RUEKANTWORT, RECHNUNG};
    
    public static ResponceTypeEn[] getTypes4send (){
        return types4send;
    }
}
    

