/*
 * Copyright (c) 2022 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner.
 * http://www.lohmann-birkner.de/de/index.php
 *
 * Contributors:
 *    2022  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.enums;

import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.system.properties.CpxSystemProperties;
import java.io.File;
import java.io.Serializable;

/**
 *
 * @author gerschmann
 */
public enum ReadonlyDocumentsEn implements Serializable{
    AOP(Lang.getReadonlyDocItemAop(), Lang.getReadonlyDocItemAopFileName(), CpxSystemProperties.getInstance().getCpxServerHelpDir(), true),
    HELP(Lang.getReadonlyDocItemHelp(), Lang.getReadonlyDocItemHelpFileName(), CpxSystemProperties.getInstance().getCpxServerHelpDir()),
    DKR_DRG(Lang.getReadonlyDocItemDkrDrg(), Lang.getReadonlyDocItemDkrDrgFileName(), CpxSystemProperties.getInstance().getCpxServerHelpDir(), true),
    DKR_PEPP(Lang.getReadonlyDocItemDkrPepp(), Lang.getReadonlyDocItemDkrPeppFileName(), CpxSystemProperties.getInstance().getCpxServerHelpDir(), true),
    MD_RECOMENDATION(Lang.getReadonlyDocItemMdRecomendation(), Lang.getReadonlyDocItemMdRecomendationFileName(), CpxSystemProperties.getInstance().getCpxServerHelpDir()), 
    WARD_COMPENSATING_THERAPY(Lang.getReadonlyDocItemWardCompecatingTherapy(), Lang.getReadonlyDocItemWardCompecatingTherapyFileName(), CpxSystemProperties.getInstance().getCpxServerHelpDir());
    
    private final String menuItem;
    private final String docName;
    private boolean withYear = false;
    private final String serverAbsPath;
    private final String suffix = ".pdf";

    
    private ReadonlyDocumentsEn(String pMenuItem, String pDocName,  String pServerAbsPath){
        menuItem = pMenuItem;
        docName = pDocName;
        serverAbsPath = pServerAbsPath;
    }
    
       private ReadonlyDocumentsEn(String pMenuItem, String pDocName, String pServerAbsPath, boolean pWithYear){
           this(pMenuItem, pDocName, pServerAbsPath);
           withYear = pWithYear;
           
       }

    public String getMenuItem() {
        return menuItem;
    }

    public String getDocName() {
        return docName;
    }
    
    public String getDocName(String pYear) {
        return docName  + (withYear?pYear:"") + suffix;
    }

    public boolean isWithYear() {
        return withYear;
    }

    public String getServerAbsPath() {
        return serverAbsPath + File.separator + docName + suffix;
    }

   public String getServerAbsPath(String pYear) {
        return serverAbsPath + File.separator + docName + (withYear?pYear:"") + suffix;
    }
    
}
