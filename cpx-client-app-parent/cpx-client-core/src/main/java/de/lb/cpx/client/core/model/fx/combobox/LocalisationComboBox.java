/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.combobox;

import de.lb.cpx.model.enums.LocalisationEn;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

/**
 * Admission Reason Combobox
 *
 * @author wilde
 */
public class LocalisationComboBox extends ComboBox<LocalisationEn> {

    public LocalisationComboBox(LocalisationEn initialValue) {
        super(FXCollections.observableArrayList(LocalisationEn.values()));
        getSelectionModel().select(initialValue);
        setConverter(new StringConverter<LocalisationEn>() {
            @Override
            public String toString(LocalisationEn object) {
                return object == null ? "" : object.getTranslation().getValue();
            }

            @Override
            public LocalisationEn fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }

}
