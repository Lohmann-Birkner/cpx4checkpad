/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.server.commonDB.model.rules;


import de.lb.cpx.server.commons.dao.AbstractCatalogEntity;
import de.lb.cpx.service.information.CatalogTypeEn;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;



/**
 *
 * @author gerschmann
 */

@MappedSuperclass
public abstract class CTransferCatalog extends AbstractCatalogEntity implements Comparable<CTransferCatalog>{

    private static final long serialVersionUID = 1L;
    
    private String srcCode;
    private String destCode;
    private int srcYear;
    private int destYear;
    private boolean isAutoForthFl;
    private boolean isAutoBackFl;

    @Column(name = "SRC_CODE", length = 20, nullable = false)
    public String getSrcCode() {
        return srcCode;
    }

    public void setSrcCode(String SrcCode) {
        this.srcCode = SrcCode;
    }

    @Column(name = "DEST_CODE", length = 20, nullable = false)
    public String getDestCode() {
        return destCode;
    }

    public void setDestCode(String destCode) {
        this.destCode = destCode;
    }

    
    @Column(name = "SRC_YEAR", nullable = false, precision = 5, scale = 0)
    public Integer getSrcYear() {
        return srcYear;
    }

    public void setSrcYear(Integer srcYear) {
        this.srcYear = srcYear;
    }

    @Column(name = "DEST_YEAR", nullable = false, precision = 5, scale = 0)
    public Integer getDestYear() {
        return destYear;
    }

    public void setDestYear(Integer destYear) {
        this.destYear = destYear;
    }

    @Column(name = "IS_AUTO_FORTH_FL", nullable = false, precision = 1, scale = 0)
    public boolean getIsAutoForthFl() {
        return isAutoForthFl;
    }

    public void setIsAutoForthFl(Boolean isAutoForthFl) {
        this.isAutoForthFl = isAutoForthFl;
    }

    @Column(name = "IS_AUTO_BACK_FL", nullable = false, precision = 1, scale = 0)
    public boolean getIsAutoBackFl() {
        return isAutoBackFl;
    }

    public void setIsAutoBackFl(Boolean isAutoBackFl) {
        this.isAutoBackFl = isAutoBackFl;
    }


    public static CTransferCatalog getTypeInstance(CatalogTypeEn type){
        switch(type){
           case ICD_TRANSFER:
               return new CIcdTransferCatalog();
           case OPS_TRANSFER:
               return new COpsTransferCatalog();
        }
        return null;
    }

    @Transient
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    


    public boolean fillFromLine(String pChecksum, String[] line, int pSrcYear, int pDestYear)
    {
        
        setChecksum(pChecksum);
        setSrcYear(pSrcYear);
        setDestYear(pDestYear);       
        return fillFromLine(line);
    }
    
    protected abstract boolean fillFromLine(String[] line);
    
    /**
     *
     * @param other
     * @return
     */
    @Override
    public int compareTo(CTransferCatalog other){

        if(Objects.equals(this.getSrcYear(), other.getSrcYear())){
           if( this.getSrcCode().equalsIgnoreCase(other.getSrcCode())){
               return this.getDestCode().compareTo(other.getDestCode());
           }else{
               return this.getSrcCode().compareToIgnoreCase(other.getSrcCode());
           }
        }else{
            return getSrcYear().compareTo(other.getSrcYear());
        }          

    }        
          
}
