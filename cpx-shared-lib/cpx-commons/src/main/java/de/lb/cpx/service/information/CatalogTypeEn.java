/*
 * Copyright (c) 2018 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner Health Care Consulting GmbH and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner Health Care Consulting GmbH
 * http://www.lohmann-birkner.de
 *
 * Contributors:
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.information;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author niemeier
 */
public enum CatalogTypeEn {

    ET,
    DRG,
    ICD,
    OPS,
    PEPP,
    ZP,
    ZE,
    HOSPITAL,
    INSURANCE_COMPANY(true),
    ATC(true),
    PZN(true),
    DEPARTMENT(true),
    DOCTOR(true),
    MDK,
    BASERATE,
    ICD_THESAURUS(true),
    OPS_THESAURUS(true),
    ICD_TRANSFER(true),
    OPS_TRANSFER(true),
    OPS_AOP(true);
    
    private CatalogTypeEn(boolean pAutoUpdate){
        autoUpdate = pAutoUpdate;
    }
    
    private CatalogTypeEn(){
        autoUpdate = false;
    }
    
    private final boolean autoUpdate;
    
    private static final Logger LOG = Logger.getLogger(CatalogTypeEn.class.getName());
    public static final int MIN_TRANSFER_YEAR = 2016;
    
    public boolean isAutoUpdate(){
        return autoUpdate;
    }
    
    public static CatalogTypeEn detectType(final File pFile) {
        if (pFile == null) {
            return null;
        }
        String name = pFile.getName().toUpperCase().trim();
        if (name.startsWith("ICDTHESAURUS".toUpperCase())) {
            return CatalogTypeEn.ICD_THESAURUS;
        }
        if (name.startsWith("OPSTHESAURUS".toUpperCase())) {
            return CatalogTypeEn.OPS_THESAURUS;
        }
        if (name.startsWith("ET-ZUSATZ".toUpperCase())) {
            return CatalogTypeEn.ET;
        }
        if (name.startsWith("GDRG".toUpperCase())) {
            return CatalogTypeEn.DRG;
        }
        if (name.toUpperCase().startsWith("TRANSFER_ICD".toUpperCase())) {
            return CatalogTypeEn.ICD_TRANSFER;
        }
        if (name.startsWith("ICD".toUpperCase())) {
            return CatalogTypeEn.ICD;
        }
        if (name.toUpperCase().startsWith("TRANSFER_OPS".toUpperCase())) {
            return CatalogTypeEn.OPS_TRANSFER;
        }
        if (name.startsWith("OPS".toUpperCase())) {
            return CatalogTypeEn.OPS;
        }
        if (name.startsWith("PEPP-ZUSATZ".toUpperCase())) {
            return CatalogTypeEn.ZP;
        }
        if (name.startsWith("PEPP".toUpperCase())) {
            return CatalogTypeEn.PEPP;
        }
        if (name.startsWith("ZUSATZ".toUpperCase())) {
            return CatalogTypeEn.ZE;
        }
        if (name.startsWith("KRANKENHAEUSER".toUpperCase())) {
            return CatalogTypeEn.HOSPITAL;
        }
        if (name.startsWith("KRANKENKASSEN".toUpperCase())) {
            return CatalogTypeEn.INSURANCE_COMPANY;
        }
        if (name.startsWith("ABTEILUNGSSCHL".toUpperCase())) {
            return CatalogTypeEn.DEPARTMENT;
        }
        if (name.startsWith("DF_DOCTORS".toUpperCase())) {
            return CatalogTypeEn.DOCTOR;
        }
        if (name.startsWith("ATCCODES".toUpperCase())) {
            return CatalogTypeEn.ATC;
        }
        if (name.startsWith("PZNCODES".toUpperCase())) {
            return CatalogTypeEn.PZN;
        }
        if (name.startsWith("FM_MDKS".toUpperCase())) {
            return CatalogTypeEn.MDK;
        }
        if (name.startsWith("KHBASERATE".toUpperCase())) {
            return CatalogTypeEn.BASERATE;
        
        }
        if(name.startsWith("AOP-Katalog_".toUpperCase())){
            return CatalogTypeEn.OPS_AOP;
        }
        LOG.log(Level.FINE, "Uknown catalog type: " + pFile.getAbsolutePath());
        return null;
    }

    public static int detectYear(final File pFile, CatalogTypeEn pType) {   
        if(pType.equals(CatalogTypeEn.OPS_AOP)){
            String name = pFile.getName();
            String parts[] = name.split("_");
            if(parts.length < 2){
                return 0;
            }
        try{
            return Integer.parseInt(parts[1]);
        }catch(Exception ex){
            return 0;
        }
            
        }else{
            return detectYear(pFile);
        }
    } 
    
    public static int detectYear(final File pFile) {
        int year = 0;
        if (pFile == null) {
            return year;
        }
        String name = pFile.getName().trim();
        int pos;
        if ((pos = name.lastIndexOf('-')) > -1) {
            name = name.substring(pos + 1);
            name = name.substring(0, name.indexOf('.'));
            try {
                year = Integer.valueOf(name);
            } catch (NumberFormatException ex) {
                LOG.log(Level.SEVERE, "Invalid year found in catalog name: " + name, ex);
            }
        }
        return year;
    }
    /**
     * for transfer tables we need both years src and dest, name format transfer_ops_2020_2021
    */
    public static int[] detectSrcAndDestYears(final File pFile){

       int[] ret = new int[2];
       Arrays.fill(ret, 0);
       if(pFile == null){
           return null;
       }
       String name = pFile.getName().trim();
       name = name.substring(0, name.indexOf('.'));
       String[] parts = name.split("_");
       if(parts.length <3 ){
           return null;
       }
       String dest = parts[parts.length - 1];
       String src = parts[parts.length - 2];
        try {
             ret[0] = Integer.valueOf(src);
             if(ret[0] < MIN_TRANSFER_YEAR){
                 ret[0] = 0;
             }
         } catch (NumberFormatException ex) {
             LOG.log(Level.SEVERE, "Invalid src - year found in catalog name: " + name, ex);
         }
        try {
             ret[1] = Integer.valueOf(dest);
             if(ret[1] < MIN_TRANSFER_YEAR + 1){
                 ret[1] = 0;
             }
         } catch (NumberFormatException ex) {
             LOG.log(Level.SEVERE, "Invalid dest - year found in catalog name: " + name, ex);
         }
        if(ret[0] == 0 || ret[1] == 0 ){
            return null;
        }
        return ret;
    }
}
