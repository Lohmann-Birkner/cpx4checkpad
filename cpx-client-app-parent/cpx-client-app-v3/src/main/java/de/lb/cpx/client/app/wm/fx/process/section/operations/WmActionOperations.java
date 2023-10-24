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

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.dialog.CreateActionDialog;
import de.lb.cpx.client.app.wm.fx.dialog.ReadonlyActionDialog;
import de.lb.cpx.client.app.wm.fx.dialog.UpdateActionDialog;
import de.lb.cpx.client.app.wm.mail.MailCreator;
import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmAction;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.scene.control.ButtonType;
import org.controlsfx.glyphfont.FontAwesome;

/**
 *
 * @author niemeier
 */
public class WmActionOperations extends WmOperations<TWmAction> {

    public WmActionOperations(ProcessServiceFacade pFacade) {
        super(pFacade);
    }

    @Override
    protected List<ItemEventHandler> getOtherOperations(final TWmAction pItem) {
        final List<ItemEventHandler> result = new ArrayList<>();
        ItemEventHandler sendMailItem = sendMailItem(pItem);
        if (sendMailItem != null) {
            result.add(sendMailItem);
        }
        return result;
    }

    @Override
    public ItemEventHandler createItem() {
        return new ItemEventHandler(FontAwesome.Glyph.FILE, Lang.getEventOperationCreate(), FontAwesome.Glyph.FILE, Lang.getEventOperationCreateItem(getItemName()), false) {
            @Override
            public void handle(ActionEvent evt) {
                if(Session.instance().getRoleProperties().isEditActionAllowed()){
                    CreateActionDialog dialog = new CreateActionDialog(facade);
                    dialog.showAndWait();
                }else{
                    MainApp.showWarningMessageDialog(Lang.getActionMsgNoCreateRight());
                }
            }
        };
    }

    @Override
    public ItemEventHandler removeItem(TWmAction pItem) {
        if (pItem == null) {
            return null;
        }
        return new ItemEventHandler(FontAwesome.Glyph.TRASH, Lang.getEventOperationRemove(), FontAwesome.Glyph.TRASH, Lang.getEventOperationRemoveItem(getItemName()), false) {
            @Override
            public void handle(ActionEvent evt) {
                if(!hasUserRightToEdit() && !isItemFromOtherUser(pItem)){
                    //error could not edit and the item is mine
                    MainApp.showWarningMessageDialog(Lang.getMsgNoRightBlank(Lang.getEventNameAction(),"löschen"));
                    return;
                }
                if(!hasUserRightToEditOther() && isItemFromOtherUser(pItem)){
                    //can not edit reminders from others
                    MainApp.showWarningMessageDialog(Lang.getMsgNoRightOtherBlank(Lang.getEventNameAction(),"löschen"));
                    return;
                }
                
                AlertDialog dlg = AlertDialog.createConfirmationDialog("Möchten Sie die Aktion wirklich löschen?");
                dlg.initOwner(MainApp.getWindow());
                dlg.showAndWait().ifPresent(new Consumer<ButtonType>() {
                    @Override
                    public void accept(ButtonType t) {
                        if (t.equals(ButtonType.OK)) {
                            //addCaseDialog();
                            facade.removeAction(pItem);
//                                    LOG.log(Level.INFO, "Delete action");
//                                    pProcessServiceBean.get().removeAction(pEvent.getAction());
//                                    TWmEvent removedEvent = new TWmEvent();
//                                    removedEvent.setEventType(WmEventTypeEn.actionRemoved);
//                                    removedEvent.setProcess(pEvent.getProcess());
//                                    pProcessServiceBean.get().storeEvent(removedEvent);
//                                    pHistoryListView.reload();
                        }
                    }
                });
            }
        };
    }
    
    @Override
    public boolean hasUserRightToEditOther() {
        return Session.instance().getRoleProperties().isEditActionOfOtherUserAllowed();
    }

    @Override
    public boolean hasUserRightToEdit() {
        return Session.instance().getRoleProperties().isEditActionAllowed();
    }
    
    @Override
    public ItemEventHandler editItem(TWmAction pItem) {
        if (pItem == null) {
            return null;
        }
        return new ItemEventHandler(FontAwesome.Glyph.PENCIL, Lang.getEventOperationEdit(), FontAwesome.Glyph.COMMENT, Lang.getEventOperationEditItem(getItemName()), true) {
            @Override
            public void handle(ActionEvent evt) {
//                if (Session.instance().getRoleProperties().isEditActionAllowed()) {
//                    UpdateActionDialog dialog = new UpdateActionDialog(pItem, facade);
//                    dialog.showAndWait();
//                } else {
////                     MainApp.showWarningMessageDialog("Sie haben keine Berechtigung, eine Aktion zu bearbeiten");
//                    ReadonlyActionDialog dialog = new ReadonlyActionDialog(pItem, facade);
//                    dialog.addReadOnlyReason(Lang.getActionMsgNoEditRight());
//                    dialog.showAndWait();
//                }

                if(!hasUserRightToEdit() && !isItemFromOtherUser(pItem)){
                    ReadonlyActionDialog dialog = new ReadonlyActionDialog(pItem, facade);
                    dialog.addReadOnlyReason(Lang.getMsgNoRightBlank(Lang.getEventNameAction(),"bearbeiten"));
                    dialog.showAndWait();
//                    MainApp.showWarningMessageDialog(Lang.getMsgNoRightBlank("Wiedervorlage","bearbeiten"));
                    //error could not edit and the item is mine
                    return;
                }
                if(!hasUserRightToEditOther() && isItemFromOtherUser(pItem)){
                    ReadonlyActionDialog dialog = new ReadonlyActionDialog(pItem, facade);
                    dialog.addReadOnlyReason(Lang.getMsgNoRightOtherBlank(Lang.getEventNameAction(),"bearbeiten"));
                    dialog.showAndWait();
                    //can not edit reminders from others
//                    MainApp.showWarningMessageDialog(Lang.getMsgNoRightOtherBlank("Wiedervorlage","bearbeiten"));
                    return;
                }
                UpdateActionDialog dialog = new UpdateActionDialog(pItem, facade);
                dialog.showAndWait();
            }
        };
    }

    public ItemEventHandler sendMailItem(TWmAction pItem) {
        if (pItem == null) {
            return null;
        }
        return new ItemEventHandler(FontAwesome.Glyph.ENVELOPE, "Versenden", FontAwesome.Glyph.ENVELOPE, "E-Mail-Programm öffnen", true) {
            @Override
            public void handle(ActionEvent evt) {
                MailCreator mailCreator = new MailCreator();
                mailCreator.sendMail(facade.getCurrentProcess(), pItem);
            }
        };
    }

    @Override
    public String getItemName() {
        return Lang.getEventNameAction();
    }

}
