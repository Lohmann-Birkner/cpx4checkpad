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
 *    2018  Your Organisation - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.skin;

import de.lb.cpx.rule.element.model.RulesElement;
import de.lb.cpx.rule.element.model.RulesOperator;
import de.lb.cpx.rule.element.model.RulesValue;
import de.lb.cpx.rule.element.model.Sugg;
import de.lb.cpx.ruleviewer.dragdrop.format.DndFormat;
import de.lb.cpx.ruleviewer.enums.ViewMode;
import de.lb.cpx.ruleviewer.model.Element;
import de.lb.cpx.ruleviewer.model.Link;
import de.lb.cpx.ruleviewer.model.SelectableControl;
import de.lb.cpx.ruleviewer.model.Suggestion;
import de.lb.cpx.ruleviewer.model.Term;
import java.io.IOException;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;

/**
 * Base class for selectable control
 *
 * @author wilde
 * @param <T> control class
 */
public abstract class SelectableControlSkin<T extends SelectableControl> extends SkinBase<T> {
//    private static PseudoClass SELECTED_NODE_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected_node");

    private static final PseudoClass SELECTED_NODE_PARENT_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected_node_parent");
    private static final PseudoClass SELECTED_NODE_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected_node");

    private static final String DEFAULT_BORDER_STYLE = "";
    private static final String ALL_BORDER_STYLE = "-fx-border-style: solid;\n"
            + "    -fx-border-width: 2;\n"
            + "    -fx-border-color: grey; ";
    private static final String TOP_BORDER_STYLE = "-fx-border-style: solid solid solid solid;\n"
            + "    -fx-border-width: 2 2 2 2 ;\n"
            + "    -fx-border-color: grey transparent transparent transparent; ";
    private static final String BOTTOM_BORDER_STYLE = "-fx-border-style: solid solid solid solid;\n"
            + "    -fx-border-width: 2 2 2 2 ;\n"
            + "    -fx-border-color: transparent transparent grey transparent;";

    private final Pane root;

    public SelectableControlSkin(T pSkinnable) throws IOException {
        super(pSkinnable);
        root = loadRoot();
        getChildren().add(root);
        getSkinnable().selectedNodeProperty().addListener(new ChangeListener<Node>() {
            @Override
            public void changed(ObservableValue<? extends Node> observable, Node oldValue, Node newValue) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        handleSelectionChange(newValue);
                    }
                });
            }
        });
        handleSelectionChange(pSkinnable.getSelectedNode());
        pSkinnable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (MouseButton.PRIMARY.equals(event.getButton())) {
                    pSkinnable.setSelectedNode(pSkinnable);
                } else if (MouseButton.SECONDARY.equals(event.getButton())) {
                    pSkinnable.setSelectedNode(getSkinnable());
                }
                event.consume();
            }
        });
        pSkinnable.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (ViewMode.READ_ONLY.equals(getSkinnable().getViewMode())) {
                    event.consume();
                    return;
                }
                //TODO: avoid highlighting if Object mismatch
                if (!checkEvent(event)) {
                    event.consume();
                    return;
                }
                if (event.getGestureSource() != null && getSkinnable().getParentElement() != null) {
                    if (event.getGestureSource().equals(getSkinnable().getParentElement())) {
                        event.consume();
                        return;
                    }
                    if (hasParent(event.getGestureSource(), getSkinnable().getParentElement())) {
                        event.consume();
                        return;
                    }
//                    if (event.getGestureSource().equals(getSkinnable().getParentElement())) {
//                        event.consume();
//                        return;
//                    }
                }
                //highlight bottom?
                javafx.geometry.Point2D sceneCoordinates = pSkinnable.localToScene(0d, 0d);
                double height = pSkinnable.getHeight();
                // get the y coordinate within the control
                double mouseY = event.getSceneY() - (sceneCoordinates.getY());

                setDndEffect(mouseY, height);
                getSkinnable().setHighlighted(true);
                event.acceptTransferModes(TransferMode.ANY);
            }
        });
        pSkinnable.setOnDragExited(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                // remove all dnd effects
                pSkinnable.setStyle(DEFAULT_BORDER_STYLE);
                getSkinnable().setHighlighted(false);
            }
        });
        pSkinnable.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                //accept the dropped event, needed to fill accepting object, to identitfy where the drop occured
                event.acceptTransferModes(TransferMode.ANY);
            }
        });
        pSkinnable.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (ViewMode.READ_ONLY.equals(getSkinnable().getViewMode())) {
                    event.consume();
                    return;
                }
                if (getSkinnable().getParentElement() == null) {
                    if (!((getSkinnable() instanceof Suggestion))) {
                        event.consume();
                        return;
                    }
                }
                /* drag was detected, start a drag-and-drop gesture*/
 /* allow any transfer mode */
                Dragboard db = pSkinnable.startDragAndDrop(TransferMode.ANY);

                /* Put a string on a dragboard */
                ClipboardContent content = new ClipboardContent();
                //use hashcode of object to identify control in element list 

                content.put(DndFormat.REORDER_FORMAT, pSkinnable.hashCode());
                db.setContent(content);
                pSkinnable.setSelectedNode(pSkinnable);

            }
        });
    }

    private boolean hasParent(Object pSource, SelectableControl pParent) {
        if (pParent == null) {
            return false;
        }
        if (pSource.equals(pParent)) {
            return true;
        }
        SelectableControl newSource = pParent.getParentElement();
        if (newSource == null) {
            return false;
        }
        if (hasParent(newSource, newSource.getParentElement())) {
            return true;
        }
        return false;
    }

    private boolean checkEvent(DragEvent event) {
        //TODO: Maybe categorize term link and element to some kind of rule object to make checking more easy
        if (checkIfRulePart(getSkinnable())) {
            return checkEventForRule(event);
        }
        if (checkIfSuggestionPart(getSkinnable())) {
            return checkEventForSuggestion(event);
        }
        return false;
    }

    public boolean checkIfRulePart(Object pPart) {
        if (pPart instanceof SelectableControl) {
            return pPart instanceof Term || pPart instanceof Link || pPart instanceof Element;
        } else {
            return pPart instanceof RulesValue || pPart instanceof RulesOperator || pPart instanceof RulesElement;
        }
    }

    public boolean checkIfSuggestionPart(Object pPart) {
        if (pPart instanceof SelectableControl) {
            return pPart instanceof Suggestion;
        } else {
            return pPart instanceof Sugg;
        }
    }

    private void setDndEffect(double pMouseY, double pHeight) {
        T ctrl = getSkinnable();
        if (ctrl != null) {
            if (ctrl instanceof Element) {
                //set all border if mouse is in other area of the cell
                ctrl.setStyle(ALL_BORDER_STYLE);
                return;
            }
            //set top border if mouse in first 25%(top area) of the cell
            if (pMouseY <= (pHeight * .25d)) {
                ctrl.setStyle(TOP_BORDER_STYLE);
                return;
            }
            //set bottom border if mouse in last 75%(bottom area) of the cell
            if (pMouseY >= (pHeight * .75d)) {
                ctrl.setStyle(BOTTOM_BORDER_STYLE);
                return;
            }
        }
    }

    private void handleSelectionChange(Node pSelected) {
        pseudoClassStateChanged(SELECTED_NODE_PSEUDO_CLASS, getSkinnable().equals(pSelected));
        root.pseudoClassStateChanged(SELECTED_NODE_PARENT_PSEUDO_CLASS, getSkinnable().getControls().contains(pSelected));
    }

    protected abstract Pane loadRoot() throws IOException;

    public Pane getRoot() {
        return root;
    }

    private boolean checkEventForRule(DragEvent event) {
        //check simple move
        if (checkIfSuggestionPart(event.getGestureSource())) {
            return false;
        }
        //check content if item comes form tool bar
        if (event.getDragboard().hasContent(DndFormat.ADD_FORMAT)) {
            Object content = event.getDragboard().getContent(DndFormat.ADD_FORMAT);
            if (content instanceof List) {
                List<Object> list = (List<Object>) content;
                if (checkIfSuggestionPart(list.get(0))) {
                    return false;
                }
            }
            if (checkIfSuggestionPart(content)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkEventForSuggestion(DragEvent event) {
        //check simple move
        if (checkIfRulePart(event.getGestureSource())) {
            return false;
        }
        //check content if item comes form tool bar
        if (event.getDragboard().hasContent(DndFormat.ADD_FORMAT)) {
            Object content = event.getDragboard().getContent(DndFormat.ADD_FORMAT);
            if (content instanceof List) {
                List<Object> list = (List<Object>) content;
                if (checkIfRulePart(list.get(0))) {
                    return false;
                }
            }
            if (checkIfRulePart(content)) {
                return false;
            }
        }
        return true;
    }
}
