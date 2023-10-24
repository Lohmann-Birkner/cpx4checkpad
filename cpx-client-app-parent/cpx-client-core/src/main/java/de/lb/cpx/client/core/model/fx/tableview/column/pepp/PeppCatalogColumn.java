/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.tableview.column.pepp;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.client.core.model.catalog.CpxPeppCatalog;
import de.lb.cpx.client.core.model.fx.tableview.column.StringColumn;
import de.lb.cpx.model.TCasePepp;
import de.lb.cpx.shared.lang.Lang;

/**
 * column for Pepp catalog text
 *
 * @author wilde
 * @param <T> type of table
 */
public abstract class PeppCatalogColumn<T> extends StringColumn<T> {

    public PeppCatalogColumn() {
        super(Lang.getPeppText(), true);
    }

    @Override
    public String extractValue(T pValue) {
        TCasePepp pepp = getValue(pValue);
        if (pepp == null) {
            return null;
        }
        if (pepp.getCaseDetails() == null) {
            return null;
        }
        //pepp Text DrgCatalog
        String desc = CpxPeppCatalog.instance().getByCode(pepp.getGrpresCode(), "de", getYear(pepp)).getPeppDescription();
        if (desc != null) {
            desc = desc.replace("\n", "");
        }
        return desc;
    }

    public abstract TCasePepp getValue(T pValue);
    
    public int getYear(TCasePepp pPepp){
        if(GDRGModel.AUTOMATIC.equals(pPepp.getModelIdEn())){
            return Lang.toYear(pPepp.getCaseDetails().getCsdAdmissionDate());
        }
        return pPepp.getModelIdEn().getCatalogYear();
    }
}
