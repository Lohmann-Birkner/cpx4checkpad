/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.server.commonDB.model.rules;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author gerschmann
 */
@Entity
@Table(name = "C_OPS_TRANSFER_CATALOG")
@SuppressWarnings("serial")
public class COpsTransferCatalog extends CTransferCatalog{
    
    private static final long serialVersionUID = 1L;
    
   private boolean hasSrcLocalisationFl;
   private boolean hasDestLocalisationFl;
    
    public COpsTransferCatalog(){
        super();
    }
    

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_ICD_TRANSFER_CATALOG_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    @Override
    public long getId() {
        return this.id;
    }

    @Column(name = "HAS_SRC_LOCALISATION_FL", nullable = false, precision = 1, scale = 0)
    public boolean getHasSrcLocalisationFl() {
        return hasSrcLocalisationFl;
    }

    public void setHasSrcLocalisationFl(boolean hasSrcLocalisationFl) {
        this.hasSrcLocalisationFl = hasSrcLocalisationFl;
    }

    @Column(name = "HAS_DEST_LOCALISATION_FL", nullable = false, precision = 1, scale = 0)
    public boolean getHasDestLocalisationFl() {
        return hasDestLocalisationFl;
    }

    public void setHasDestLocalisationFl(boolean hasDestLocalisationFl) {
        this.hasDestLocalisationFl = hasDestLocalisationFl;
    }



    @Override
    public boolean fillFromLine(String[] line) {
       if(line == null || line.length != 6){
           return false;
       }
       this.setSrcCode(line[0]);
       this.setHasSrcLocalisationFl("J".equalsIgnoreCase(line[1]));
       this.setDestCode(line[2]);
       this.setHasDestLocalisationFl("J".equalsIgnoreCase(line[3]));
       this.setIsAutoForthFl("A".equalsIgnoreCase(line[4]));
       this.setIsAutoBackFl("A".equalsIgnoreCase(line[5]));
       return true;
    }
    
}
