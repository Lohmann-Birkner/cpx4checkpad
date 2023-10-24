/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.server.commonDB.model;

import java.util.Date;

/**
 *
 * @author gerschmann
 */
public interface CCatalogIF {
    
   public  String getCode();
   public Date getValidFrom(); 
   public Date getValidTo();
   public String getIk();
   public int getCatalogYear();
   public String getAdditionalCode();
}
