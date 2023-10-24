/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.cm.fx.documentation;

import de.lb.cpx.client.core.model.fx.titledpane.AccordionTitledPane;
import de.lb.cpx.model.enums.CommentTypeEn;
import de.lb.cpx.server.commons.dao.AbstractVersionEntity;

/**
 *
 * @author wilde
 * @param <T> entity
 */
public class DocumentationTitledPane<T extends AbstractVersionEntity> extends AccordionTitledPane<CommentTypeEn, T>{
    
    public DocumentationTitledPane(CommentTypeEn pType){
        super();
        setAccordionItem(pType);
        setTitle(pType.getTranslation().getValue());
    }
    
}
