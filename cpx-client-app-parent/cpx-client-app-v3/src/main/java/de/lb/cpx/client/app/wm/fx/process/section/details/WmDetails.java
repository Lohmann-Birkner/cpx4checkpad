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
package de.lb.cpx.client.app.wm.fx.process.section.details;

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.process.section.operations.ItemEventHandler;
import de.lb.cpx.client.app.wm.fx.process.section.operations.WmOperations;
import de.lb.cpx.client.core.model.fx.popover.AutoFitPopOver;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;
import org.controlsfx.glyphfont.FontAwesome;

/**
 *
 * @author niemeier
 * @param <E> type
 */
public abstract class WmDetails<E extends AbstractEntity> {

    private static final Logger LOG = Logger.getLogger(WmDetails.class.getName());

    protected final ProcessServiceFacade facade;
    protected final E item;

    public WmDetails(ProcessServiceFacade pFacade, E pItem) {
        this.facade = pFacade;
        this.item = pItem;
    }

    /**
     * create representation of the item in its detailview
     *
     * @return Parent pane root of the item detail
     */
    protected abstract Parent getDetailNode();

    public abstract String getDetailTitle();

//    protected abstract Button createMenuItem();
    public long getProcessNumber() {
        return facade == null ? 0L : facade.getCurrentProcessNumber();
    }

    public WmDetailSection getDetailSection() {
        final WmDetailSection detail = new WmDetailSection();
        if (item != null) {
            long startTime = System.currentTimeMillis();
            detail.setTitle(getDetailTitle());
            detail.setContent(getDetailNode());
            Button menuItems = createMenuItem();
            if (menuItems != null) {
                detail.getSkin().setMenu(menuItems);
            }
            LOG.log(Level.FINEST, "detail content for workflow number {0} loaded in {1} ms", new Object[]{getProcessNumber(), (System.currentTimeMillis() - startTime)});
        } else {
            LOG.log(Level.WARNING, "cannot display detail section, because item is null!");
        }

//        WmDetailSection detail = new WmDetailSection();
////        detail.setTitle(selected != null ? selected.getName() : "");
//        detail.setTitle(item != null ? "Datei" : "");
//        detail.setContent(detailContent);
//        Parent detailContent = new VBox();
//        if (selected != null) {
//            detailContent = new WmDocumentDetails(serviceFacade, selected).getDetailNode();
//        detail.createMenuItems(item, item);
//        }
//        detail.setContent(detailContent);
//        return detail.getRoot();
        return detail;
    }

    /**
     * @return the facade
     */
    public ProcessServiceFacade getFacade() {
        return facade;
    }

    /**
     * @return the item
     */
    public E getItem() {
        return item;
    }

    public abstract WmOperations<E> getOperations();

    public ItemEventHandler createItem() {
        return getOperations().createItem();
    }

    public ItemEventHandler editItem() {
        return getOperations().editItem(getItem());
    }

    public ItemEventHandler removeItem() {
        return getOperations().removeItem(getItem());
    }

//    public final EventHandler<Event> editItem() {
//        return editItem(getItem());
//    }
//
//    public final EventHandler<Event> removeItem() {
//        return removeItem(getItem());
//    }
//    protected final List<ItemOperation> getOperations() {
//        final List<ItemOperation> result = new ArrayList<>();
//
//        EventHandler<Event> ehCreate = createItem();
//        if (ehCreate != null) {
//            result.add(new ItemEventHandler(ehCreate, ResourceLoader.getGlyph(FontAwesome.Glyph.FILE), "Anlegen"));
//            //result.put(ehCreate, ResourceLoader.getGlyph(FontAwesome.Glyph.FILE));
//        }
//
//        EventHandler<Event> ehEdit = editItem();
//        if (ehEdit != null) {
//            result.add(new ItemEventHandler(ehEdit, ResourceLoader.getGlyph(FontAwesome.Glyph.PENCIL), "Ändern"));
//            //result.put(ehEdit, ResourceLoader.getGlyph(FontAwesome.Glyph.PENCIL));
//        }
//
//        EventHandler<Event> ehRemove = removeItem();
//        if (ehRemove != null) {
//            result.add(new ItemEventHandler(ehRemove, ResourceLoader.getGlyph(FontAwesome.Glyph.TRASH), "Entfernen"));
//            //result.put(ehRemove, ResourceLoader.getGlyph(FontAwesome.Glyph.TRASH));
//        }
//
//        final List<ItemOperation> otherOps = getOtherOperations();
//        if (otherOps != null && !otherOps.isEmpty()) {
//            result.addAll(otherOps);
//        }
//
//        return result;
//    }
//
//    protected List<ItemOperation> getOtherOperations() {
//        return null;
//    }
    protected final Button createMenuItem() {
        final List<ItemEventHandler> operations = getOperations().getOperations(item);
        if (operations == null || operations.isEmpty()) {
            return null;
        }
        final Button btnMenu = new Button();
        btnMenu.setText("");
        btnMenu.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.ELLIPSIS_V));
        btnMenu.getStyleClass().add("cpx-icon-button");
        btnMenu.setOnAction((ActionEvent event) -> {
            final AutoFitPopOver menu = new AutoFitPopOver();
            menu.setFitOrientation(Orientation.HORIZONTAL);
            final List<Button> buttons = new ArrayList<>();

            for (ItemEventHandler op : operations) {
                final Button button = new Button();
                button.setGraphic(ResourceLoader.getGlyph(op.getMenuItemGlyph()));
                button.setMaxWidth(Double.MAX_VALUE);
                button.setText(op.getMenuItemText());
                //button.setAlignment(Pos.CENTER_LEFT);
                button.setStyle("-fx-alignment: CENTER-LEFT;");
                //button.setTextAlignment(TextAlignment.LEFT);
                button.setOnAction(op);
                buttons.add(button);
                //            btnAction.setOnAction(new EventHandler<ActionEvent>() {
                //                @Override
                //                public void handle(ActionEvent event) {
                //                    EventHandler<Event> eh = removeItem();
                //                    if (eh != null) {
                //                        eh.handle(event);
                //                    }
                //                }
                //            });
            }
            VBox menuContent = new VBox();
            menuContent.getChildren().addAll(buttons);
            menu.setContentNode(menuContent);
            menu.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
            menu.show(btnMenu);
        });
        return btnMenu;
//        });
//        return btnMenu;
//        btnMenu.setOnAction((ActionEvent event) -> {
//            final PopOver menu = new AutoFitPopOver();
//            final Button btnDeleteAction = new Button();
//            btnDeleteAction.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.TRASH));
//            btnDeleteAction.setMaxWidth(Double.MAX_VALUE);
//            btnDeleteAction.setText("Aktion löschen");
//            btnDeleteAction.setOnAction(new EventHandler<ActionEvent>() {
//                @Override
//                public void handle(ActionEvent event) {
//                    EventHandler<Event> eh = removeItem();
//                    if (eh != null) {
//                        eh.handle(event);
//                    }
//                }
//            });
//            final Button btnChangeAction = new Button();
//            btnChangeAction.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.PENCIL));
//            btnChangeAction.setMaxWidth(Double.MAX_VALUE);
//            btnChangeAction.setText("Aktion ändern");
//            btnChangeAction.setOnAction(new EventHandler<ActionEvent>() {
//                @Override
//                public void handle(ActionEvent event) {
//                    EventHandler<Event> eh = editItem();
//                    if (eh != null) {
//                        eh.handle(event);
//                    }
//                }
//            });
//            VBox menuContent = new VBox(btnChangeAction, btnDeleteAction);
//            menu.setContentNode(menuContent);
//            menu.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
//            menu.show(btnMenu);
//        });
//        return btnMenu;
////            getSkin().setMenu(btnMenu);
    }

}
