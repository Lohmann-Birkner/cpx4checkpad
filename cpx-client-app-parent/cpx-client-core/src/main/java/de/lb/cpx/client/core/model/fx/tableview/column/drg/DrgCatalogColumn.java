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
package de.lb.cpx.client.core.model.fx.tableview.column.drg;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.client.core.model.catalog.CpxDrgCatalog;
import de.lb.cpx.client.core.model.fx.tableview.column.StringColumn;
import de.lb.cpx.model.TCaseDrg;
import de.lb.cpx.shared.lang.Lang;
import java.util.logging.Logger;

/**
 * Column to show drg catalog text
 *
 * @author wilde
 * @param <T> type of table item
 */
public abstract class DrgCatalogColumn<T> extends StringColumn<T> {

    private static final Logger LOG = Logger.getLogger(DrgCatalogColumn.class.getName());

    public DrgCatalogColumn() {
        super(Lang.getDrgText(), true);
    }

    @Override
    public String extractValue(T pValue) {
        TCaseDrg drg = getValue(pValue);
        if (drg == null) {
            return null;
        }
        if (drg.getCaseDetails() == null) {
            return null;
        }
        String desc = CpxDrgCatalog.instance().getByCode(drg.getGrpresCode(), "de", getYear(drg)).getDrgDescription();
        if (desc != null) {
            desc = desc.replace("\n", "");
        }
        return desc;
    }

    public abstract TCaseDrg getValue(T pValue);
    
    
    public int getYear(TCaseDrg pDrg){
        if(GDRGModel.AUTOMATIC.equals(pDrg.getModelIdEn())){
            return Lang.toYear(pDrg.getCaseDetails().getCsdAdmissionDate());
        }
        return pDrg.getModelIdEn().getCatalogYear();
    }
}
