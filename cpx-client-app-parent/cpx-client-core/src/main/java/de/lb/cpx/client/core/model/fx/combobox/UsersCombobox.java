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

import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.model.fx.labeled.LabeledComboBox;
import de.lb.cpx.shared.dto.UserDTO;
import java.util.Objects;
import javafx.util.StringConverter;

/**
 * Combobox to handle user dto objects
 *
 * @author wilde
 */
public class UsersCombobox extends LabeledComboBox<UserDTO> {

    /**
     * no arg contructor for scene builder creates a combobox with default title
     * Label
     */
    public UsersCombobox() {
        this("Label");
    }

    /**
     * creates new user combobox with given title as title of the labeled
     * combobox
     *
     * @param pTitle title of the combobox
     */
    public UsersCombobox(String pTitle) {
        super(pTitle);
//        setCellFactory(new Callback<ListView<UserDTO>, ListCell<UserDTO>>() {
//            @Override
//            public ListCell<UserDTO> call(ListView<UserDTO> param) {
//                return new UserListCell();
//
//            }
//        });
//        setButtonCell(new UserListCell());
        setConverter(new StringConverter<UserDTO>() {
            @Override
            public String toString(UserDTO t) {
//                return t.getName();
                return t == null ? "" : t.getName();
            }

            @Override
            public UserDTO fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }

    /**
     * @param pId select a user by its db id
     */
    public void selectById(Long pId) {
        if (pId == null || pId.equals(-1L)) {
            return;
        }
        for (UserDTO item : getItems()) {
            if (Objects.equals(item.getId(), pId)) {
                select(item);
                return;
            }
        }
        select(MenuCache.instance().getUserMapEntryForId(pId));
    }

    /**
     * @return the id of the currently selected item
     */
    public Long getSelectedId() {
        return getSelectedItem() != null ? getSelectedItem().getId() : null;
    }
//    /**
//     * @param pUserId selects a user by its id 
//     */
//    public void selectUserById(long pUserId){
//        for(UserDTO user : getItems()){
//            if(user.getId() == pUserId){
//                select(user);
//            }
//        }
//    }

    /**
     * tries to fetch current user from the session in the client
     */
    public void selectCurrentUser() {
        selectById(Session.instance().getCpxUserId());
    }

//    private class UserListCell extends ListCell<UserDTO> {
//
//        @Override
//        protected void updateItem(UserDTO item, boolean empty) {
//            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
//            if (item == null || empty) {
//                setText("");
//                return;
//            }
//            setText(item.getUserName() + (item.isInActive() ? " - Inaktiv" : ""));
//        }
//    }
}
