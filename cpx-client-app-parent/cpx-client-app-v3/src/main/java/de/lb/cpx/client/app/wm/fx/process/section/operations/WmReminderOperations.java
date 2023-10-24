/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.process.section.operations;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.dialog.AddReminderDialog;
import de.lb.cpx.client.app.wm.fx.dialog.ReadonlyReminderDialog;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.fx.alert.ConfirmDialog;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmReminder;
import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.scene.control.ButtonType;
import org.controlsfx.glyphfont.FontAwesome;

/**
 *
 * @author niemeier
 */
public class WmReminderOperations extends WmOperations<TWmReminder> {

    public WmReminderOperations(ProcessServiceFacade pFacade) {
        super(pFacade);
    }

    @Override
    public boolean hasUserRightToEditOther() {
        return Session.instance().getRoleProperties().isEditReminderOfOtherUserAllowed();
    }

    @Override
    public boolean hasUserRightToEdit() {
        return Session.instance().getRoleProperties().isEditReminderAllowed();
    }
    
    @Override
    public ItemEventHandler editItem(final TWmReminder pItem) {
        if (pItem == null) {
            return null;
        }
        //TWmReminder item = tvReminder.getSelectionModel().getSelectedItem();//row.getItem();
        return new ItemEventHandler(FontAwesome.Glyph.PENCIL, Lang.getEventOperationEdit(), FontAwesome.Glyph.BELL, Lang.getEventOperationEditItem(getItemName()), true) {
            @Override
            public void handle(ActionEvent evt) {
                if(!hasUserRightToEdit() && !isItemFromOtherUser(pItem)){
                    ReadonlyReminderDialog dialog = new ReadonlyReminderDialog(facade, pItem);
                    dialog.addReadOnlyReason(Lang.getMsgNoRightBlank(Lang.getEventNameReminder(),"bearbeiten"));
                    dialog.showAndWait();
//                    MainApp.showWarningMessageDialog(Lang.getMsgNoRightBlank("Wiedervorlage","bearbeiten"));
                    //error could not edit and the item is mine
                    return;
                }
                if(!hasUserRightToEditOther() && isItemFromOtherUser(pItem)){
                    ReadonlyReminderDialog dialog = new ReadonlyReminderDialog(facade, pItem);
                    dialog.addReadOnlyReason(Lang.getMsgNoRightOtherBlank(Lang.getEventNameReminder(),"bearbeiten"));
                    dialog.showAndWait();
                    //can not edit reminders from others
//                    MainApp.showWarningMessageDialog(Lang.getMsgNoRightOtherBlank("Wiedervorlage","bearbeiten"));
                    return;
                }
                AddReminderDialog dialog = new AddReminderDialog(facade, pItem);
                dialog.showAndWait();
//                reload();
//                tvReminder.reload();
            }
        };
    }

    @Override
    public ItemEventHandler createItem() {
        return new ItemEventHandler(FontAwesome.Glyph.FILE, Lang.getEventOperationCreate(), FontAwesome.Glyph.FILE, Lang.getEventOperationCreateItem(getItemName()), false) {
            @Override
            public void handle(ActionEvent evt) {
                if(hasUserRightToEdit()){
                    AddReminderDialog dialog = new AddReminderDialog(facade, null);
                    dialog.showAndWait();
                }else{
                    MainApp.showWarningMessageDialog(Lang.getMsgNoRightBlank(Lang.getEventNameReminder(),"erstellen"));
//                    adwd
                }
//                reload();
//                tvReminder.reload();
            }
        };
    }

    
    @Override
    public ItemEventHandler removeItem(final TWmReminder pItem) {
        if (pItem == null) {
            return null;
        }
        return new ItemEventHandler(FontAwesome.Glyph.TRASH, Lang.getEventOperationRemove(), FontAwesome.Glyph.TRASH, Lang.getEventOperationRemoveItem(getItemName()), false) {
            @Override
            public void handle(ActionEvent evt) {
                if(!hasUserRightToEdit() && !isItemFromOtherUser(pItem)){
                    //error could not edit and the item is mine
                    MainApp.showWarningMessageDialog(Lang.getMsgNoRightBlank(Lang.getEventNameReminder(),"löschen"));
                    return;
                }
                if(!hasUserRightToEditOther() && isItemFromOtherUser(pItem)){
                    //can not edit reminders from others
                    MainApp.showWarningMessageDialog(Lang.getMsgNoRightOtherBlank(Lang.getEventNameReminder(),"löschen"));
                    return;
                }
                
                //final TWmReminder item = tvReminder.getSelectionModel().getSelectedItem();//row.getItem();
                new ConfirmDialog(MainApp.getWindow(), Lang.getDeleteReminder()).showAndWait().ifPresent(new Consumer<ButtonType>() {
                    @Override
                    public void accept(ButtonType t) {
                        if (t.equals(ButtonType.YES)) {
                            facade.removeReminder(pItem);
                        }
//                        tvReminder.reload();
                    }
                });
            }
        };
    }
    @Override
    public String getItemName() {
        return Lang.getEventNameReminder();
    }

}
