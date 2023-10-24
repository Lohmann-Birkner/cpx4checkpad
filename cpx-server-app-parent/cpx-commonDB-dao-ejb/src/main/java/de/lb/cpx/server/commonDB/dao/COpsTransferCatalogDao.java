/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.commonDB.dao;

import de.lb.cpx.server.commonDB.model.rules.COpsTransferCatalog;
import javax.ejb.Stateless;
import org.jboss.ejb3.annotation.SecurityDomain;



/**
 *
 * @author gerschmann
 */
@Stateless
@SecurityDomain("cpx")
public class COpsTransferCatalogDao  extends CTransferCatalogDao<COpsTransferCatalog>{
    public COpsTransferCatalogDao(){
        super(COpsTransferCatalog.class);
    }

    @Override
    protected String getTableName() {
        return "C_OPS_TRANSFER_CATALOG";
    }
    
}
