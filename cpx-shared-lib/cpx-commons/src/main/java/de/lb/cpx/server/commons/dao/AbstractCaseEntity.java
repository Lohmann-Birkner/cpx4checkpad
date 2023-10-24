/*
 * Copyright (c) 2021 Lohmann & Birkner.
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
 *    2021  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.commons.dao;

import java.util.Date;

/**
 *
 * @author gerschmann
 */
public abstract class AbstractCaseEntity extends AbstractVersionEntity{
    
    public Object cloneWithoutIds(Long currentCpxUserId) throws CloneNotSupportedException{
        AbstractCaseEntity entity = (AbstractCaseEntity)super.clone();
        entity.setCreationDate(new Date());
        entity.setCreationUser(currentCpxUserId);
        entity.setModificationDate(null);
        entity.setModificationUser(null);
        return entity;
    }
    
    protected AbstractCaseEntity(Long currentCpxUserId){
       setCreationUser(currentCpxUserId);
       setCreationDate(new Date());
    }
    
    public AbstractCaseEntity(){
        
    }
    
    protected abstract void setIgnoreFields();
}
