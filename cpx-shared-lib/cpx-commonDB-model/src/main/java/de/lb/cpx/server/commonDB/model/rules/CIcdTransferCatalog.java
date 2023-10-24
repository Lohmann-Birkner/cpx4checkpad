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
@Table(name = "C_ICD_TRANSFER_CATALOG")
@SuppressWarnings("serial")
public class CIcdTransferCatalog extends CTransferCatalog{
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_ICD_TRANSFER_CATALOG_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    @Override
    public long getId() {
        return this.id;
    }


    @Override
    public boolean fillFromLine(String[] line) {
        if(line == null || line.length != 4){
            return false;
        }

        this.setSrcCode(line[0]);
        this.setDestCode(line[1]);
        this.setIsAutoForthFl("A".equalsIgnoreCase(line[2]));
        this.setIsAutoBackFl("A".equalsIgnoreCase(line[3]));
        return true;
    }
}
