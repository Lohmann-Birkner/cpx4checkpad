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
package de.lb.cpx.client.ruleeditor.model.error;

import de.lb.cpx.client.core.model.fx.dialog.accordion.AccordionTopicDialog;
import de.lb.cpx.client.core.model.fx.dialog.accordion.AccordionTopicItem;
import de.lb.cpx.server.rule.services.RuleExchangeError;
import javafx.stage.Modality;
import javafx.stage.Window;
import javafx.util.Callback;

/**
 *
 * @author wilde
 */
public class RuleExchangeTopicDialog extends AccordionTopicDialog<RuleExchangeError>{
    
    public RuleExchangeTopicDialog(AlertType pAlertType, String pMessage, Window pOwner, Modality pModality) {
        super(pAlertType, pMessage, pOwner, pModality);
        setAccordionItemFactory(new Callback<RuleExchangeError, AccordionTopicItem>() {
            @Override
            public AccordionTopicItem call(RuleExchangeError p) {
                return new RuleExchangeTopicItem(p);
            }
        });
    }
    
}
