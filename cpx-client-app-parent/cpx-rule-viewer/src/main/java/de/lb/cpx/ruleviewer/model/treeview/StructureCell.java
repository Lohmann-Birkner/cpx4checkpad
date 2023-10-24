/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.model.treeview;

import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;

/**
 * Structure Cell for display rule in treeview TODO: ENABLE DnD, behavior should
 * be unified in real solution
 *
 * @author wilde
 */
public class StructureCell extends TreeCell<StructureItem> {

//    private static final String DEFAULT_BORDER_STYLE = "";
//    private static final String ALL_BORDER_STYLE = "-fx-border-style: solid;\n"
//            + "    -fx-border-width: 2;\n"
//            + "    -fx-border-color: grey; ";
//    private static final String TOP_BORDER_STYLE = "-fx-border-style: solid solid solid solid;\n"
//            + "    -fx-border-width: 2 2 2 2 ;\n"
//            + "    -fx-border-color: grey transparent transparent transparent; ";
//    private static final String BOTTOM_BORDER_STYLE = "-fx-border-style: solid solid solid solid;\n"
//            + "    -fx-border-width: 2 2 2 2 ;\n"
//            + "    -fx-border-color: transparent transparent grey transparent;";
//    private static final DataFormat REORDER_FORMAT = new DataFormat("dataformat.reorder");
    /**
     * Using a static here, it's just too convenient.
     */
    private TreeItem<StructureItem> draggedTreeItem;
//    private DropLocation dropLocation;
//    private static WorkDropType workDropType;

//    private final FXControlLoader controlLoader;
//    private Object controller;
    public StructureCell() {

//        setOnDragOver(new EventHandler<DragEvent>() {
//            @Override
//            public void handle(DragEvent event) {
//                if(isDisableDnd()){
//                    event.consume();
//                    return;
//                }
//                if(isNotAlreadyChildOfTarget(StructureCell.this.getTreeItem())){// && draggedTreeItem.getParent() != getTreeItem()) {
//                    javafx.geometry.Point2D sceneCoordinates = StructureCell.this.localToScene(0d, 0d);
//                    double height = StructureCell.this.getHeight();
//                    // get the y coordinate within the control
//                    double mouseY = event.getSceneY() - (sceneCoordinates.getY());
//
//                    setDndEffect(mouseY,height);
//                    if(StructureCell.this.getTreeItem().getParent()!=null){
//                            int index = StructureCell.this.getTreeItem().getParent().getChildren().indexOf(StructureCell.this.getTreeItem());
//                            dragOverIndex(index);
//                        }
//                    event.acceptTransferModes(TransferMode.ANY);
//                }
//            }
//
//            private void setDndEffect(double pMouseY,double pHeight) {
//                SelectableControl ctrl = getItem().getControl();
//                if(getItem().getControl() instanceof Element){
//                    //set all border if mouse is in other area of the cell
//                    setStyle(ALL_BORDER_STYLE);
//                    ctrl.setStyle(ALL_BORDER_STYLE);
//                    dropLocation = DropLocation.LAST;
//                    return;
//                }
//                //set top border if mouse in first 25%(top area) of the cell
//                if (pMouseY <= (pHeight * .25d)) {
////                    LOG.info("drag on top " + pMouseY + " - height * .33: " + (pHeight * .33d));
//                    setStyle(TOP_BORDER_STYLE);
//                    ctrl.setStyle(TOP_BORDER_STYLE);
//                    dropLocation = DropLocation.ON_TOP;
//                    return;
//                }
//                //set bottom border if mouse in last 75%(bottom area) of the cell
//                if (pMouseY >= (pHeight * .75d)) {
//                    setStyle(BOTTOM_BORDER_STYLE);
//                    ctrl.setStyle(BOTTOM_BORDER_STYLE);
//                    dropLocation = DropLocation.ON_BENEATH;
////                    LOG.info("drag on bottom " + pMouseY + " - height * .66: " + (pHeight * .66d));
//                    return;
//                }
//            }
//        });
//        
//        setOnDragDetected(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                if(isDisableDnd()){
//                    event.consume();
//                    return;
//                }
//                ClipboardContent content;
//                
//                content = new ClipboardContent();
////                content.putString("");
//                content.put(REORDER_FORMAT, getTreeView().getRow(getTreeItem()));
//                Dragboard dragboard;
//                
//                dragboard = getTreeView().startDragAndDrop(TransferMode.MOVE);
//                dragboard.setContent(content);
//                
//                draggedTreeItem = getTreeItem();
//                
//                event.consume();
//            }});
//        setOnDragDropped(new EventHandler<DragEvent>() {
//            @Override
//            public void handle(DragEvent event) {
//                if(isDisableDnd()){
//                    event.consume();
//                    return;
//                }
//                boolean dropOK = false;
//
//                final Dragboard db = event.getDragboard();
//                SelectableControl ctrl = null;
//                if(db.hasContent(REORDER_FORMAT)){
//                    Integer idx = (Integer) db.getContent(REORDER_FORMAT);
//                    TreeItem<StructureItem> itm = getTreeView().getTreeItem(idx);
//                    ctrl = itm.getValue().getControl();
//                    ((Element)itm.getParent().getValue().getControl()).getControls().remove(ctrl);
//                    itm.getParent().getChildren().remove(itm);
//                }else{
//                    // Only get the first file from the list
//                    Object content = null;
//                    if (db.hasContent(LinkEnum.getDataFormat())) {
//                        content = db.getContent(LinkEnum.getDataFormat());
//                    }
//                    if (db.hasContent(TermEnum.getDataFormat())) {
//                        content = db.getContent(TermEnum.getDataFormat());
//                    }
//                    ctrl = (SelectableControl) DragDropHelper.castToControl(content);
//                }
//                if (ctrl != null) {
//                    if (getItem().getControl() instanceof Element) {
//                        if (dropLocation.equals(DropLocation.LAST)) {
//                            StructureTreeItem it = ctrl.getTreeItem();
//                            Element element = (Element) getItem().getControl();
//                            final SelectableControl drop = ctrl;
////                            Platform.runLater(new Runnable() {
////                                @Override
////                                public void run() {
//                                    Platform.runLater(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            element.getTreeItem().getChildren().add(it);//new StructureTreeItem(ctrl));
//                                        }
//                                    });
////                                    element.getTreeItem().getChildren().add(it);//new StructureTreeItem(ctrl));
//                                    element.getControls().add(drop);
//                                    FXMLController.RefreshEvent refresh = new FXMLController.RefreshEvent(FXMLController.refreshEvent(),element);
//                                    Event.fireEvent(element, refresh);
////                                    getTreeView().refresh();
////                                }
////                            });
//                        }
//                        return;
//                    }
//                    if (getTreeItem().getParent().getValue().getControl() instanceof Element) {
//                        Element parentElement = (Element) getTreeItem().getParent().getValue().getControl();
//                        int indexOf = parentElement.getControls().indexOf(getItem().getControl());
//                        StructureTreeItem newItem = ctrl.getTreeItem();//new StructureTreeItem(ctrl);
//                        switch (dropLocation) {
//                            case LAST:
//                                parentElement.getControls().add(ctrl);
//                                ContextMenu me1 = ctrl.populateContextMenu();
//                                ctrl.setContextMenu(me1);
////                                Platform.runLater(new Runnable() {
////                                    @Override
////                                    public void run() {
//                                        getTreeItem().getParent().getChildren().add(newItem);
////                                        getTreeView().refresh();
//                                        FXMLController.RefreshEvent refresh = new FXMLController.RefreshEvent(FXMLController.refreshEvent(),parentElement);
//                                        Event.fireEvent(parentElement, refresh);
////                                        getTreeView().getSelectionModel().select(newItem);
////                                    }
////                                });
//                                break;
//                            case ON_TOP:
//                                int targetIndex = indexOf - 1 < 0 ? 0 : indexOf - 1;
//                                parentElement.getControls().add(targetIndex, ctrl);
//                                ContextMenu me2 = ctrl.populateContextMenu();
//                                ctrl.setContextMenu(me2);
////                                Platform.runLater(new Runnable() {
////                                    @Override
////                                    public void run() {
//                                        getTreeItem().getParent().getChildren().add(targetIndex, newItem);
//                                        getTreeView().refresh();
//                                        FXMLController.RefreshEvent refresh2 = new FXMLController.RefreshEvent(FXMLController.refreshEvent(),parentElement);
//                                        Event.fireEvent(parentElement, refresh2);
////                                        getTreeView().getSelectionModel().select(newItem);
////                                    }
////                                });
//                                break;
//                            case ON_BENEATH:
//                                parentElement.getControls().add(indexOf + 1, ctrl);
//                                ContextMenu me3 = ctrl.populateContextMenu();
//                                ctrl.setContextMenu(me3);
////                                Platform.runLater(new Runnable() {
////                                    @Override
////                                    public void run() {
//                                        getTreeItem().getParent().getChildren().add(indexOf + 1, newItem);
//                                        FXMLController.RefreshEvent refresh3 = new FXMLController.RefreshEvent(FXMLController.refreshEvent(),parentElement);
//                                        Event.fireEvent(parentElement, refresh3);
////                                        getTreeView().refresh();
////                                        getTreeView().getSelectionModel().select(newItem);
////                                    }
////                                });
//                                break;
//                            default:
//                                LOG.log(Level.WARNING, "unkown drop location! {0} \nDo nothing!", dropLocation);
//                        }
//                    }
//                }
////                    if(pDropCallback != null){
////                         success = pDropCallback.call(castToControl(file));
////                    }
////                    readFileAndShowDialog(file);
////                }
////                tvDocuments.reload();
//                event.setDropCompleted(dropOK);
//                event.consume();
//            }
//        });
//        setOnDragExited(new EventHandler<DragEvent>() {
//            @Override
//            public void handle(DragEvent event) {
//                // remove all dnd effects
//                setStyle(DEFAULT_BORDER_STYLE);
//                dropLocation = DropLocation.NONE;
//                if(getItem() != null){
//                    SelectableControl ctrl = getItem().getControl();
//                    ctrl.setStyle(DEFAULT_BORDER_STYLE);
//                }
//            }});
    }
    private static final Logger LOG = Logger.getLogger(StructureCell.class.getName());

    public void dragOverIndex(int pIndex) {

    }

    protected boolean isDraggableToParent() {
        return false;
//        return draggedTreeItem.getValue().isDraggableTo(getTreeItem().getValue());
    }

    protected boolean isNotAlreadyChildOfTarget(TreeItem<StructureItem> treeItemParent) {
        if (draggedTreeItem == treeItemParent) {
            return false;
        }

        if (treeItemParent.getParent() != null) {
            return isNotAlreadyChildOfTarget(treeItemParent.getParent());
        } else {
            return true;
        }
    }

    @Override
    protected void updateItem(StructureItem item, boolean empty) {
        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
        if (item == null || empty) {
            textProperty().unbind();
            setText("");
            setContextMenu(null);
            return;
        }
//        item.getControl();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                setText(item.getDisplayText());
            }
        });
        setContextMenu(item.getContextMenu());
//        textProperty().unbind();
//        textProperty().bind(item.displayTextProperty());
    }

    private BooleanProperty disableDndProperty;

    public final BooleanProperty disableDndProperty() {
        if (disableDndProperty == null) {
            disableDndProperty = new SimpleBooleanProperty(false);
        }
        return disableDndProperty;
    }

    public final Boolean isDisableDnd() {
        return disableDndProperty().get();
    }

    public final void setDisableDnd(Boolean pDisable) {
        disableDndProperty().set(pDisable);
    }

//    @Override
//    protected void updateItem(StructureItem item, boolean empty) {
//        // if a tree cell is showing the text of another value that was
//        // selected, it may be that the properties are still bound to a form control
//        if (getItem() == null ||empty) {
//            super.updateItem(item, empty);
////            setText("");
//            return;
////            getItem().unbind(this);
//        }
//        setText(item.getControl().getDisplayText());
//        
////        if(item != null) {
////            item.updateTreeCell(this, controlLoader);
////        }
//    }
//    public Object getController() {
//        return controller;
//    }
//
//    public void setController(Object controller) {
//        this.controller = controller;
//    }
    public enum DropLocation {
        ON_TOP, ON_BENEATH, LAST, FIRST, NONE;
    }
}
