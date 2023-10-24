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
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author niemeier
 * @param <E> type
 */
public abstract class WmOperations<E extends AbstractEntity> {

    protected final ProcessServiceFacade facade;

    public WmOperations(ProcessServiceFacade pFacade) {
        this.facade = pFacade;
    }

    /**
     * @return the facade
     */
    public ProcessServiceFacade getFacade() {
        return facade;
    }

    public ItemEventHandler createItem() {
        return null;
    }

    public ItemEventHandler openItem(final E pItem) {
        return null;
    }

    public ItemEventHandler editItem(final E pItem) {
        return null;
    }

    public ItemEventHandler removeItem(final E pItem) {
        return null;
    }

//    public final EventHandler<Event> editItem() {
//        return editItem(getItem());
//    }
//
//    public final EventHandler<Event> removeItem() {
//        return removeItem(getItem());
//    }
    public final List<ItemEventHandler> getOperations(final E pItem) {
        final List<ItemEventHandler> result = new ArrayList<>();

//        EventHandler<Event> ehCreate = createItem();
//        if (ehCreate != null) {
//            result.add(new ItemEventHandler(ehCreate, ResourceLoader.getGlyph(FontAwesome.Glyph.FILE), "Anlegen"));
//            //result.put(ehCreate, ResourceLoader.getGlyph(FontAwesome.Glyph.FILE));
//        }
        ItemEventHandler ehOpen = openItem(pItem);
        if (ehOpen != null) {
            result.add(ehOpen);
            //result.put(ehEdit, ResourceLoader.getGlyph(FontAwesome.Glyph.PENCIL));
        }

        ItemEventHandler ehEdit = editItem(pItem);
        if (ehEdit != null) {
            result.add(ehEdit);
            //result.put(ehEdit, ResourceLoader.getGlyph(FontAwesome.Glyph.PENCIL));
        }

        ItemEventHandler ehRemove = removeItem(pItem);
        if (ehRemove != null) {
            result.add(ehRemove);
            //result.put(ehRemove, ResourceLoader.getGlyph(FontAwesome.Glyph.TRASH));
        }

        final List<ItemEventHandler> otherOps = getOtherOperations(pItem);
        if (otherOps != null && !otherOps.isEmpty()) {
            Iterator<ItemEventHandler> it = otherOps.iterator();
            while(it.hasNext()) {
                ItemEventHandler eh = it.next();
                if (eh != null) {
                    result.add(eh);
                }
            }
            //result.addAll(otherOps);
        }

//        final Iterator<ItemEventHandler> it = result.iterator();
//        while (it.hasNext()) {
//            ItemEventHandler eh = it.next();
//            if (eh == null) {
//                it.remove();
//            }
//        }

        return result;
    }

    protected List<ItemEventHandler> getOtherOperations(final E pItem) {
        return new ArrayList<>();
    }

    public List<ItemEventHandler> getDefaultOperations(final E pItem) {
        final List<ItemEventHandler> result = getOperations(pItem);
//        if (result != null && !result.isEmpty()) {
            Iterator<ItemEventHandler> it = result.iterator();
            while (it.hasNext()) {
                ItemEventHandler op = it.next();
                if (op == null || !op.isHistoryDefaultOperation()) {
                    it.remove();
                }
            }
//        }
        return result;
    }

    public abstract String getItemName();
//
//    protected final Button createMenuItem() {
//        final List<ItemOperation> operations = getOperations();
//        if (operations == null || operations.isEmpty()) {
//            return null;
//        }
//        final Button btnMenu = new Button();
//        btnMenu.setText("");
//        btnMenu.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.ELLIPSIS_V));
//        btnMenu.getStyleClass().add("cpx-icon-button");
//        btnMenu.setOnAction((ActionEvent event) -> {
//            final PopOver menu = new AutoFitPopOver();
//            final List<Button> buttons = new ArrayList<>();
//
//            for (ItemEventHandler op : operations) {
//                final Button button = new Button();
//                button.setGraphic(op.glyph);
//                button.setMaxWidth(Double.MAX_VALUE);
//                button.setText(op.text);
//                button.setOnAction((evt) -> {
//                    op.handler.handle(evt);
//                });
//                buttons.add(button);
//                //            btnAction.setOnAction(new EventHandler<ActionEvent>() {
//                //                @Override
//                //                public void handle(ActionEvent event) {
//                //                    EventHandler<Event> eh = removeItem();
//                //                    if (eh != null) {
//                //                        eh.handle(event);
//                //                    }
//                //                }
//                //            });
//            }
//            VBox menuContent = new VBox();
//            menuContent.getChildren().addAll(buttons);
//            menu.setContentNode(menuContent);
//            menu.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
//            menu.show(btnMenu);
//        });
//        return btnMenu;
////        });
////        return btnMenu;
////        btnMenu.setOnAction((ActionEvent event) -> {
////            final PopOver menu = new AutoFitPopOver();
////            final Button btnDeleteAction = new Button();
////            btnDeleteAction.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.TRASH));
////            btnDeleteAction.setMaxWidth(Double.MAX_VALUE);
////            btnDeleteAction.setText("Aktion löschen");
////            btnDeleteAction.setOnAction(new EventHandler<ActionEvent>() {
////                @Override
////                public void handle(ActionEvent event) {
////                    EventHandler<Event> eh = removeItem();
////                    if (eh != null) {
////                        eh.handle(event);
////                    }
////                }
////            });
////            final Button btnChangeAction = new Button();
////            btnChangeAction.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.PENCIL));
////            btnChangeAction.setMaxWidth(Double.MAX_VALUE);
////            btnChangeAction.setText("Aktion ändern");
////            btnChangeAction.setOnAction(new EventHandler<ActionEvent>() {
////                @Override
////                public void handle(ActionEvent event) {
////                    EventHandler<Event> eh = editItem();
////                    if (eh != null) {
////                        eh.handle(event);
////                    }
////                }
////            });
////            VBox menuContent = new VBox(btnChangeAction, btnDeleteAction);
////            menu.setContentNode(menuContent);
////            menu.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
////            menu.show(btnMenu);
////        });
////        return btnMenu;
//////            getSkin().setMenu(btnMenu);
//    }

    public ItemEventHandler getFirstDefaultOperation(final E pItem) {
//        final E content = HistoryEntry.getEventContent(pEvent);
        final List<ItemEventHandler> operations = getDefaultOperations(pItem);
        if (!operations.isEmpty()) {
            return operations.iterator().next();
        }
        /* else {
            LOG.log(Level.WARNING, "No Default Operation found for Event: {0}", pEvent.getEventType().name());
        } */
        return null;
    }
    
    public boolean hasUserRightToEdit(){
        return true;
    }
    
    public boolean hasUserRightToEditOther(){
        return true;
    }
    public boolean isItemFromOtherUser(final E pItem) {
        return !Long.valueOf(Session.instance().getCpxUserId()).equals(pItem.getCreationUser());
//        if(pItem.)
    }

}
