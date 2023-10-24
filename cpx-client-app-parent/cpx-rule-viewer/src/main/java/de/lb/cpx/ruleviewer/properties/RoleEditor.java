/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.properties;

import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.model.fx.checked_combobox.CpxCheckComboBox;
import de.lb.cpx.ruleviewer.event.RuleChangedEvent;
import de.lb.cpx.server.commonDB.model.CdbUserRoles;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.ListChangeListener;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

/**
 *
 * @author wilde
 */
public class RoleEditor implements PropertyEditor<String> {

    private final PropertySheet.Item item;
    private CpxCheckComboBox<Map.Entry<Long, CdbUserRoles>> chkBox;

    public RoleEditor(PropertySheet.Item pProperty) {
        item = pProperty;
    }
    private static final Logger LOG = Logger.getLogger(RoleEditor.class.getName());

    @Override
    public Node getEditor() {
        if (chkBox == null) {
            chkBox = new CpxCheckComboBox<>();
            try {
                chkBox.getItems().addAll(MenuCache.instance().getRoleMapEntries());
            } catch (NullPointerException ex) {
                LOG.log(Level.WARNING, "Can not load user roles", ex);
            } catch (NoClassDefFoundError ex) {
                LOG.log(Level.WARNING, "Can not load user roles", ex);
            }
            chkBox.setConverter(new StringConverter<Map.Entry<Long, CdbUserRoles>>() {
                @Override
                public String toString(Map.Entry<Long, CdbUserRoles> object) {
                    return object != null ? object.getValue().getCdburName() : null;
                }

                @Override
                public Map.Entry<Long, CdbUserRoles> fromString(String string) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
            chkBox.getCheckModel().getCheckedItems().addListener(new ListChangeListener<Map.Entry<Long, CdbUserRoles>>() {
                @Override
                public void onChanged(ListChangeListener.Change<? extends Map.Entry<Long, CdbUserRoles>> c) {
                    item.setValue(getValue());
                    RuleChangedEvent saveEvent = new RuleChangedEvent(RuleChangedEvent.ruleChangedEvent(), this);
                    Event.fireEvent(chkBox, saveEvent);
                }
            });
        }
        return chkBox;
    }

    @Override
    public String getValue() {
        String obj = chkBox.getCheckModel().getCheckedItems().stream().map((item) -> {
            return String.valueOf(item.getKey());//MenuCache.instance().getUserNameForId(item.getKey());
        }).collect(Collectors.joining(","));
        return obj;
    }

    @Override
    public void setValue(String value) {
        checkItems(value);

    }

    private void checkItems(String filterValue) {
        if (filterValue == null) {
            return;
        }
        if (filterValue.isEmpty()) {
            ((CheckComboBox<Map.Entry<Long, String>>) getEditor()).getCheckModel().check(getItem(Session.instance().getCpxActualRoleId()));
            return;
        }
        String[] split = filterValue.split(",");
        if (split == null) {
            return;
        }
        for (String subString : split) {
            if (subString.isEmpty()) {
                continue;
            }
            Map.Entry<Long, String> itm = getItem(Long.parseLong(subString));
            if (itm != null) {
                ((CheckComboBox<Map.Entry<Long, String>>) getEditor()).getCheckModel().check(itm);
            }
        }
    }

    private Map.Entry<Long, String> getItem(Long pId) {
        for (Map.Entry<Long, String> item : ((CheckComboBox<Map.Entry<Long, String>>) getEditor()).getItems()) {
            if (item.getKey().equals(pId)) {
                return item;
            }
        }
        return null;
    }
}
