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
package de.lb.cpx.client.core.model.fx.combobox;

import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.server.commonDB.model.CWmListDocumentType;
import java.util.List;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.StringConverter;

/**
 * Combobox to display CWMLISTREMINDERSUBJECT
 *
 * @author shahin
 */
public class DocumentTypeCombobox extends ComboBox<CWmListDocumentType> {

    public DocumentTypeCombobox() {
        super();
//                List<CWmListDocumentType> availableDocumentTypes = facade.getAllAvailableDocumentTypes(new Date());
        List<CWmListDocumentType> availableDocumentTypes = MenuCache.getMenuCacheDocumentTypes().values();
//CPX-1193  Dokumentn Typen  are sortes  by wmDtSort 
//                Collections.sort(availableDocumentTypes);
        getItems().addAll(availableDocumentTypes);
        setConverter(new StringConverter<CWmListDocumentType>() {
            @Override
            public String toString(CWmListDocumentType object) {
                return object == null ? "" : object.getWmDtName();
            }

            @Override
            public CWmListDocumentType fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        setCellFactory((ListView<CWmListDocumentType> param) -> new ListCell<CWmListDocumentType>() {
            @Override
            protected void updateItem(CWmListDocumentType item, boolean empty) {
                super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                if (item == null || empty) {
                    setText("");
                    return;
                }
                setText(item.getWmDtName());
            }
        });
    }

    public DocumentTypeCombobox(Long pInternalId) {
        this();
        selectType(pInternalId);
    }

    /**
     * try to select given type by name if nothing found render text only and
     * select nothing
     *
     * @param pInternalId internal id
     */
    public final void selectType(Long pInternalId) {
        for (CWmListDocumentType type : getItems()) {
            //No null check on 20180223 database field is not nullable
            if (pInternalId != null && pInternalId.equals(type.getWmDtInternalId())) {
                //select and break loop
                getSelectionModel().select(type);
                return;
            }
        }
        //if nothing was found show string
        //CWmListDocumentType oldType = new CWmListDocumentType();
        //oldType.setWmDtName(pType);
        CWmListDocumentType oldType = MenuCache.getMenuCacheDocumentTypes().get(pInternalId); //does this make sense?!
        getSelectionModel().select(oldType);
//                getButtonCell().setText(pType);
    }

}
