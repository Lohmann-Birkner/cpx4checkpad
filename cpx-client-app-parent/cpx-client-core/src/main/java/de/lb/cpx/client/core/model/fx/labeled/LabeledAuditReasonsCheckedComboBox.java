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
package de.lb.cpx.client.core.model.fx.labeled;

import de.lb.cpx.server.commonDB.model.CMdkAuditreason;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.util.StringConverter;

/**
 * Checked combobox to select multiple auditreasons
 *
 * @author wilde
 */
public class LabeledAuditReasonsCheckedComboBox extends LabeledCheckComboBox<CMdkAuditreason> {

    private static final Logger LOG = Logger.getLogger(LabeledAuditReasonsCheckedComboBox.class.getName());
    
    
    private final StringConverter<CMdkAuditreason> converter = new StringConverter<>() {
        @Override
        public String toString(CMdkAuditreason auditReason) {
            return auditReason == null ? "" : auditReason.getMdkArName();
        }

        @Override
        public CMdkAuditreason fromString(String string) {
            return null;
        }
    };

    /**
     * no arg constructor for scene builder
     */
    public LabeledAuditReasonsCheckedComboBox() {
        this("Label");
    }

    /**
     * creates new instance with title as title
     *
     * @param pTitle title to set
     */
    public LabeledAuditReasonsCheckedComboBox(String pTitle) {
        super(pTitle);
        setConverter(converter);
    }

    public void checkByIds(List<Long> pIds) {
        for (Long id : pIds) {
            for (CMdkAuditreason item : getItems()) {
                if (item.getId() == id) {
                    getCheckModel().check(item);
                }
            }
        }
    }

    public void checkByNumbers(List<Long> pNumbers) {
        nextVal:for (Long number : pNumbers) {
            for (CMdkAuditreason item : getItems()) {
                if (number != null && number.equals(item.getMdkArNumber())) {
                    getCheckModel().check(item);
                    continue nextVal;
                }
            }
            LOG.warning("No valid AuditReason found for id: " + number + "!\nDid CaseType was changed, or was AuditReason deleted?");
        }
    }

    public String getCheckedIds() {
//        String ids = "";
//        for(CMdkAuditreason item :getCheckModel().getCheckedItems()){
//            
//        }
//        return ids;
        return getCheckModel().getCheckedItems().stream().map((CMdkAuditreason t) -> String.valueOf(t.getId())).collect(Collectors.joining(","));

    }

    public String getCheckedNumbers() {
//        String ids = "";
//        for(CMdkAuditreason item :getCheckModel().getCheckedItems()){
//            
//        }
//        return ids;
        return getCheckModel().getCheckedItems().stream().map((CMdkAuditreason t) -> String.valueOf(t.getMdkArNumber())).collect(Collectors.joining(","));
    }

}
