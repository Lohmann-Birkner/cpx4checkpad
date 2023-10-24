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
package de.lb.cpx.ruleviewer.skin;

import com.google.common.collect.Lists;
import de.lb.cpx.rule.element.model.RulesElement;
import de.lb.cpx.ruleviewer.dragdrop.format.DndFormat;
import de.lb.cpx.ruleviewer.enums.ViewMode;
import de.lb.cpx.ruleviewer.event.RefreshEvent;
import de.lb.cpx.ruleviewer.event.RuleChangedEvent;
import de.lb.cpx.ruleviewer.model.Element;
import de.lb.cpx.ruleviewer.model.Link;
import de.lb.cpx.ruleviewer.model.LinkArea;
import de.lb.cpx.ruleviewer.model.SelectableControl;
import de.lb.cpx.ruleviewer.model.Term;
import de.lb.cpx.ruleviewer.model.TermArea;
import de.lb.cpx.ruleviewer.util.SelectableControlFactory;
import de.lb.cpx.ruleviewer.util.SeverityEn;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Callback;

/**
 *
 * @author wilde
 */
public class ElementSkin extends SelectableControlSkin<Element> {

    private static final Logger LOG = Logger.getLogger(ElementSkin.class.getName());

//    private static final PseudoClass SELECTED_NODE_PARENT_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected_node_parent");
//    private static final PseudoClass SELECTED_NODE_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected_node");
//    private Parent root;
    private VBox boxItems;

    public ElementSkin(final Element pSkinnable) throws IOException {
        super(pSkinnable);
//        loadRoot();
//        getChildren().add(root);
        setControlItems(pSkinnable.getControls());
        for (SelectableControl ctrl : pSkinnable.getControls()) {
            registerCallbacks(ctrl);
        }
        pSkinnable.getControls().addListener(new ListChangeListener<Object>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Object> c) {
                //TODO: redraws whole tree? maybe its only neccessary to update all in current element to shorten time?
                while (c.next()) {
                    if (c.wasRemoved()) {
                        for (Object obj : c.getRemoved()) {
                            remove((SelectableControl) obj);
                            removeCallbacks((SelectableControl) obj);
                        }
                    }
                    if (c.wasAdded()) {
                        for (Object ob : c.getAddedSubList()) {

                            registerCallbacks((SelectableControl) ob);
                        }
                    }
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        setControlItems(pSkinnable.getControls());
                    }
                });
            }
        });
        getSkinnable().fontProperty().addListener(new ChangeListener<Font>() {
            @Override
            public void changed(ObservableValue<? extends Font> observable, Font oldValue, Font newValue) {
                if (newValue == null) {
                    return;
                }
                for (SelectableControl ctrl : pSkinnable.getControls()) {
                    ctrl.setFont(newValue);
                }
            }
        });
        getSkinnable().setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                handleDragDroppedEvent(event);
            }
        });
    }

    private void handleDragDroppedEvent(DragEvent event) {
        //code should be revamped, to be more understandable
        Dragboard board = event.getDragboard();
        event.getGestureSource();
        event.getGestureTarget();
        SelectableControl ctrl = (SelectableControl) ((event.getAcceptingObject() instanceof SelectableControl) ? event.getAcceptingObject() : null);
        boolean success = false;
        if (ctrl != null) {
            if (board.hasContent(DndFormat.ADD_FORMAT)) {
                int index = getSkinnable().getControls().indexOf(ctrl);
                //add item before accecption object if mouse is in the first quarter 
                //otherwise add behind
                javafx.geometry.Point2D sceneCoordinates = ctrl.localToScene(0d, 0d);
                double height = ctrl.getHeight();
                // get the y coordinate within the control
                double mouseY = event.getSceneY() - (sceneCoordinates.getY());
                Object content = board.getContent(DndFormat.ADD_FORMAT);
//                        SelectableControl item = new Term();
//                        item.setViewMode(ViewMode.READ_WRITE);
                if (mouseY <= (height * .25d)) {
                    if (content instanceof List) {
                        List<Object> list = (List<Object>) content;
                        addAllAbove(index, SelectableControlFactory.instance().createControls(list));
                    } else {
                        addAbove(index, SelectableControlFactory.instance().createControl(content));
                    }
                } else {
                    if (content instanceof List) {
                        List<Object> list = (List<Object>) content;
                        addAllBelow(index, SelectableControlFactory.instance().createControls(list));
                    } else {
                        addBelow(index, SelectableControlFactory.instance().createControl(content));
                    }
                }
                success = true;
                RuleChangedEvent saveEvent = new RuleChangedEvent(RuleChangedEvent.ruleChangedEvent(), this);
                Event.fireEvent(ctrl, saveEvent);
            }
            if (board.hasContent(DndFormat.REORDER_FORMAT)) {
                if (!ctrl.getParentElement().equals(getSkinnable())) {
                    return;
                }
                SelectableControl moved = (SelectableControl) event.getGestureSource();
                remove(moved.getParentElement(), moved);
                int index = ctrl.getParentElement().getControls().indexOf(ctrl);
                //add item before accecption object if mouse is in the first quarter 
                //otherwise add behind
                javafx.geometry.Point2D sceneCoordinates = ctrl.localToScene(0d, 0d);
                double height = ctrl.getHeight();
                // get the y coordinate within the control
                double mouseY = event.getSceneY() - (sceneCoordinates.getY());
                if (mouseY <= (height * .25d)) {
                    addAbove(ctrl.getParentElement(), index, moved);
                } else {
                    addBelow(ctrl.getParentElement(), index, moved);
                }
                ctrl.getParentElement().setSelectedNode(moved);
                success = true;
                RuleChangedEvent saveEvent = new RuleChangedEvent(RuleChangedEvent.ruleChangedEvent(), this);
                Event.fireEvent(ctrl, saveEvent);
            }
        } else {
            //check if a simple drop occured in the element 
            //add as last object
            if (event.getGestureTarget() instanceof Element) {

                Element target = (Element) getHighlighted();
                if (target == null) {
                    LOG.warning("Drag and Drop could not be executed! target is null/no target element specified!");
                    event.consume();
                    return;
                }
                //invalid consider as end to last list position
                int lastIdx = 0;
                int size = target.getControls().size();
                if (size > 0) {
                    lastIdx = size;
                }
                if (board.hasContent(DndFormat.ADD_FORMAT)) {
                    Object content = board.getContent(DndFormat.ADD_FORMAT);
                    if (content instanceof List) {
                        List<Object> list = (List<Object>) content;
                        add(target, lastIdx, SelectableControlFactory.instance().createControls(list));
                    } else {
                        add(target, lastIdx, SelectableControlFactory.instance().createControl(content));
                    }
                }
                if (board.hasContent(DndFormat.REORDER_FORMAT)) {
                    SelectableControl moved = (SelectableControl) event.getGestureSource();
                    remove(moved.getParentElement(), moved);
                    add(target, lastIdx, moved);
                    target.setSelectedNode(moved);
                }
                RuleChangedEvent saveEvent = new RuleChangedEvent(RuleChangedEvent.ruleChangedEvent(), this);
                Event.fireEvent(target, saveEvent);
                success = true;
            }
        }
        event.setDropCompleted(success);
        event.consume();
        LOG.finer("drag dropped in element");
    }

    private SelectableControl getHighlighted() {
        return getHighlighted(getSkinnable());
    }

    private SelectableControl getHighlighted(Element pElement) {
        if (pElement.isHighlighted()) {
            return getSkinnable();
        }
        for (SelectableControl ctrl : pElement.getControls()) {
            if (ctrl.isHighlighted()) {
                return ctrl;
            }
            if (ctrl instanceof Element) {
                return getHighlighted((Element) ctrl);
            }
        }
        return null;
    }

//    private SelectableControl findControl(int code) {
//        return findControl(getSkinnable(), code);
//    }
    private SelectableControl findControl(Element pElement, int code) {
        for (SelectableControl ctrl : pElement.getControls()) {
            if (ctrl.hashCode() == code) {
                return ctrl;
            }
            if (ctrl instanceof Element) {
                return findControl((Element) ctrl, code);
            }
        }
        return null;
    }

    private void addAbove(int pIndex, SelectableControl pItem) {
        addAbove(getSkinnable(), pIndex, pItem);
    }

    private void addBelow(int pIndex, SelectableControl pItem) {
        addBelow(getSkinnable(), pIndex + 1, pItem);
    }

    private void addAbove(Element pParent, int pIndex, SelectableControl pItem) {
        if (pIndex < 0) {
            return;
        }
        add(pParent, pIndex, pItem);
    }

    private void addBelow(Element pParent, int pIndex, SelectableControl pItem) {
        add(pParent, pIndex + 1, pItem);
    }

    private void addAllAbove(int pIndex, List<SelectableControl> pItem) {
        addAllAbove(pIndex, pItem.toArray(new SelectableControl[pItem.size()]));
    }

    private void addAllAbove(int pIndex, SelectableControl... pItem) {
        if (pIndex < 0) {
            return;
        }
        add(getSkinnable(), pIndex, pItem);
    }

    private void addAllBelow(int pIndex, List<SelectableControl> pItem) {
        addAllBelow(pIndex, pItem.toArray(new SelectableControl[pItem.size()]));
    }

    private void addAllBelow(int pIndex, SelectableControl... pItem) {
        add(getSkinnable(), pIndex + 1, pItem);
    }

    private void add(Element pParent, int pIndex, List<SelectableControl> pItem) {
        add(pParent, pIndex, pItem.toArray(new SelectableControl[pItem.size()]));
    }

    private void add(Element pParent, int pIndex, SelectableControl... pItem) {
        if (pIndex > pParent.getControls().size()) {
            pParent.getControls().addAll(pItem);
        } else {
            pParent.getControls().addAll(pIndex, Lists.newArrayList(pItem));
        }
        //add items in element
        int idx = 0;
        for (SelectableControl ctrl : pItem) {
            ctrl.setParentElement(pParent);
            //select first after add
            if (idx == 0) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        getSkinnable().setSelectedNode(ctrl);
                    }
                });
            }
            addToRulesElement(pIndex + idx, pParent, ctrl);
//            if(ctrl instanceof Term){
//                pParent.getRulesElement().getRulesValueOrRulesOperatorOrRulesElement().add(pIndex+idx,((Term)ctrl).getRulesValue());
//            }
//            if(ctrl instanceof Link){
//                pParent.getRulesElement().getRulesValueOrRulesOperatorOrRulesElement().add(pIndex+idx,((Link)ctrl).getRulesOperator());
//            }
//            if(ctrl instanceof Element){
//                pParent.getRulesElement().getRulesValueOrRulesOperatorOrRulesElement().add(pIndex+idx,((Element)ctrl).getRulesElement());
//            }
            idx++;
        }
    }

//    private void addToRulesElement(int pIndex, SelectableControl pCtrl) {
//        addToRulesElement(pIndex, getSkinnable(), pCtrl);
//    }
    private void addToRulesElement(int pIndex, Element pElement, SelectableControl pCtrl) {
        if (pCtrl instanceof Term) {
            pElement.getRulesElement().getRulesValueOrRulesOperatorOrRulesElement().add(pIndex, ((Term) pCtrl).getRulesValue());
        }
        if (pCtrl instanceof Link) {
            pElement.getRulesElement().getRulesValueOrRulesOperatorOrRulesElement().add(pIndex, ((Link) pCtrl).getRulesOperator());
        }
        if (pCtrl instanceof Element) {
            pElement.getRulesElement().getRulesValueOrRulesOperatorOrRulesElement().add(pIndex, ((Element) pCtrl).getRulesElement());
        }
    }

    private void remove(SelectableControl... pItem) {
        remove(getSkinnable(), pItem);
    }

    private void remove(Element pElement, SelectableControl... pItem) {
        if (pElement == null) {
            LOG.warning("no parent element specified, can not delete");
            return;
        }
//        pElement.getControls().removeAll(pItem);
        for (SelectableControl ctrl : pItem) {
            pElement.getControls().remove(ctrl);
            if (ctrl instanceof Term) {
                pElement.getRulesElement().getRulesValueOrRulesOperatorOrRulesElement().remove(((Term) ctrl).getRulesValue());
            }
            if (ctrl instanceof Link) {
                pElement.getRulesElement().getRulesValueOrRulesOperatorOrRulesElement().remove(((Link) ctrl).getRulesOperator());
            }
            if (ctrl instanceof Element) {
                pElement.getRulesElement().getRulesValueOrRulesOperatorOrRulesElement().remove(((Element) ctrl).getRulesElement());
            }
            pElement.setSelectedNode(null);
        }
    }

    private void removeCallbacks(SelectableControl pControl) {
        pControl.setDeleteCallback(null);
        pControl.fontProperty().unbind();
        if (pControl instanceof Term) {
            Term term = (Term) pControl;
            term.setWrapCallback(null);
            term.setUnWrapCallback(null);
        }
    }

    private void registerCallbacks(final SelectableControl pControl) {
        pControl.fontProperty().bind(getSkinnable().fontProperty());
        pControl.setDeleteCallback(new Callback<Void, Boolean>() {
            @Override
            public Boolean call(Void param) {
                if (handleDeleteCallback(pControl)) {
                    RefreshEvent event = new RefreshEvent(RefreshEvent.refreshEvent(), getSkinnable());
                    Event.fireEvent(getSkinnable(), event);
                    return true;
                }
                return false;
            }
        });
        if (pControl instanceof Term) {
            final Term term = (Term) pControl;
            term.setWrapCallback(new Callback<Void, Boolean>() {
                @Override
                public Boolean call(Void param) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            int idx = getSkinnable().getIndex(term);
//                                getSkinnable().getControls().remove(term);
                            Element element = new Element();
                            getSkinnable().selectedNodeProperty().bindBidirectional(element.selectedNodeProperty());
                            term.setParentElement(element);
                            getSkinnable().getControls().remove(term);
                            element.addControl(0, term);
                            element.setRulesElement(0, new RulesElement());
                            addToRulesElement(0, element, term);
                            element.setParentElement(getSkinnable());
                            element.setViewMode(ViewMode.READ_WRITE);
//                                element.addControls(currCtrl);
                            getSkinnable().addControl(idx, element);
                            addToRulesElement(idx, getSkinnable(), element);
//                            getSkinnable().getControls().set(idx, element);
                            element.setSelectedNode(term);
                            RefreshEvent event = new RefreshEvent(RefreshEvent.refreshEvent(), getSkinnable());
                            Event.fireEvent(element, event);
                            RuleChangedEvent saveEvent = new RuleChangedEvent(RuleChangedEvent.ruleChangedEvent(), element);
                            Event.fireEvent(getSkinnable(), saveEvent);
                        }
                    });
                    return true;
                }
            });
            term.setUnWrapCallback(new Callback<Void, Boolean>() {
                @Override
                public Boolean call(Void param) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Element remove = term.getParentElement();
                            Element parent = remove.getParentElement();
                            if (parent == null) {
                                return;
                            }
                            List<SelectableControl> ctrls = new ArrayList<>(remove.getControls());
                            int index = parent.getIndex(remove);
                            remove.getControls().clear();
                            parent.getControls().remove(remove);
                            remove(parent, remove);
                            add(parent, index, ctrls);

                            RefreshEvent event = new RefreshEvent(RefreshEvent.refreshEvent(), parent);
                            Event.fireEvent(parent, event);
                            RuleChangedEvent saveEvent = new RuleChangedEvent(RuleChangedEvent.ruleChangedEvent(), this);
                            Event.fireEvent(parent, saveEvent);
                            parent.setSelectedNode(term);
                        }
                    });
                    return true;
                }
            });
        }
    }
//    private void handleSelectionChange(Node pSelected) {
//        pseudoClassStateChanged(SELECTED_NODE_PSEUDO_CLASS, getSkinnable().equals(pSelected));
//        root.pseudoClassStateChanged(SELECTED_NODE_PARENT_PSEUDO_CLASS, getSkinnable().getControls().contains(pSelected));
//    } 

    private void setControlItems(ObservableList<? extends SelectableControl> list) {
        //maybe complete redraw not necessary
        boxItems.getChildren().clear();
        //add new TermArea if nothing is present
        if (list.isEmpty()) {
            if (ViewMode.READ_WRITE.equals(getSkinnable().getViewMode())) {
                boxItems.getChildren().add(getNewTermArea(SeverityEn.ERROR));
            }
            return;
        }
        for (SelectableControl currCtrl : list) {
            Bindings.bindBidirectional(getSkinnable().selectedNodeProperty(), currCtrl.selectedNodeProperty());
//            getSkinnable().selectedNodeProperty().bindBidirectional(currCtrl.selectedNodeProperty());
            int index = list.indexOf(currCtrl);
            if (index == 0 && currCtrl instanceof Link) {
                appendDndArea(currCtrl, SeverityEn.ERROR);
            }

            boxItems.getChildren().add(currCtrl);
            int next = index + 1;
            if (next < list.size()) {
                SelectableControl nextCtrl = list.get(next);
                if ((currCtrl instanceof Term || currCtrl instanceof Element) && (nextCtrl instanceof Term || nextCtrl instanceof Element)) {
                    appendDndArea(currCtrl, SeverityEn.ERROR);
                    LOG.finest("Add Area cause term on index " + index + " next " + next);
                    continue;
                }
                if ((currCtrl instanceof Link) && (nextCtrl instanceof Link)) {
                    appendDndArea(currCtrl, SeverityEn.ERROR);
                    LOG.finest("Add Area cause link on index " + index + " next " + next);
                    continue;
                }
//                LOG.log(Level.FINER, "CurrItem: {0} - NextItem {1}", new Object[]{currCtrl.getClass().getSimpleName(), nextCtrl.getClass().getSimpleName()});
                LOG.fine("next item seems fine! continue...");
            }
        }
        SelectableControl lastItem = getSkinnable().getLast();
        if (lastItem != null) {
            getSkinnable().selectedNodeProperty().bindBidirectional(lastItem.selectedNodeProperty());
        }
        appendDragDropAtEnd(lastItem);
    }

    private void appendDndArea(SelectableControl pControl, SeverityEn pLevel) {
        if (ViewMode.READ_ONLY.equals(getSkinnable().getViewMode())) {
            return;
        }
        //null is considert as empty list, than only term area is added
        if (pControl == null) {
            boxItems.getChildren().add(getNewTermArea(pLevel));
        }
        if (pControl instanceof Term) {
            boxItems.getChildren().add(getNewLinkArea(pLevel));
        }
        if (pControl instanceof Element) {
            boxItems.getChildren().add(getNewLinkArea(pLevel));
        }
        if (pControl instanceof Link) {
            boxItems.getChildren().add(getNewTermArea(pLevel));
        }
    }

    private LinkArea getNewLinkArea(SeverityEn pLevel) {
        final LinkArea area = new LinkArea();
        area.setSeverityLevel(pLevel);
        area.setOnDropDetected(new Callback<DragEvent, Void>() {
            @Override
            public Void call(DragEvent param) {
                int index = boxItems.getChildren().indexOf(area);
                if (param.getDragboard().hasContent(DndFormat.REORDER_FORMAT)) {
                    //expect hashcode to identify object
                    SelectableControl moved = (SelectableControl) param.getGestureSource();//findControl(code);
                    moved.getParentElement().getControls();
                    remove(moved.getParentElement(), moved);
                    moved.getParentElement().getControls();
                    moved.setParentElement(getSkinnable());
                    add(getSkinnable(), index, moved);
                    getSkinnable().setSelectedNode(moved);
                    param.setDropCompleted(true);
                    param.consume();
                    return null;
                }
                if (param.getDragboard().hasContent(DndFormat.ADD_FORMAT)) {
                    if (param.getDragboard().getContent(DndFormat.ADD_FORMAT) instanceof List) {
                        List<Object> list = (List<Object>) param.getDragboard().getContent(DndFormat.ADD_FORMAT);
                        add(getSkinnable(), index, SelectableControlFactory.instance().createControls(list).toArray(new SelectableControl[list.size()]));
                    } else {
                        add(getSkinnable(), index, SelectableControlFactory.instance().createControl(param));
                    }
                }
                RuleChangedEvent saveEvent = new RuleChangedEvent(RuleChangedEvent.ruleChangedEvent(), this);
                Event.fireEvent(getSkinnable(), saveEvent);
                param.setDropCompleted(true);
                param.consume();
//                handleDragDroppedEvent(param);
                return null;
            }
        });
        return area;
    }

    private TermArea getNewTermArea(SeverityEn pLevel) {
        final TermArea area = new TermArea();
        area.setSeverityLevel(pLevel);
        area.setOnDropDetected((param) -> {
            int index = boxItems.getChildren().indexOf(area);
            if (param.getDragboard().hasContent(DndFormat.REORDER_FORMAT)) {
                //expect hashcode to identify object
                SelectableControl moved = (SelectableControl) param.getGestureSource();//findControl(code);
                moved.getParentElement().getControls();
                remove(moved.getParentElement(), moved);
                moved.getParentElement().getControls();
                moved.setParentElement(getSkinnable());
                add(getSkinnable(), index, moved);
                getSkinnable().setSelectedNode(moved);
                param.setDropCompleted(true);
                param.consume();
                return null;
            }
            if (param.getDragboard().hasContent(DndFormat.ADD_FORMAT)) {
                if (param.getDragboard().getContent(DndFormat.ADD_FORMAT) instanceof List) {
                    List<Object> list = (List<Object>) param.getDragboard().getContent(DndFormat.ADD_FORMAT);
                    add(getSkinnable(), index, SelectableControlFactory.instance().createControls(list).toArray(new SelectableControl[list.size()]));
                } else {
                    add(getSkinnable(), index, SelectableControlFactory.instance().createControl(param));
                }
            }
            RuleChangedEvent saveEvent = new RuleChangedEvent(RuleChangedEvent.ruleChangedEvent(), this);
            Event.fireEvent(getSkinnable(), saveEvent);
            param.setDropCompleted(true);
            param.consume();
//                handleDragDroppedEvent(param);
            return null;
        });
        return area;
    }

    @Override
    protected Pane loadRoot() throws IOException {
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/Element.fxml"));
        boxItems = (VBox) root.lookup("#boxItems");
        Label lbOpenClip = (Label) root.lookup("#lbOpenClip");
        lbOpenClip.fontProperty().bind(getSkinnable().fontProperty());
        Label lbCloseClip = (Label) root.lookup("#lbCloseClip");
        lbCloseClip.fontProperty().bind(getSkinnable().fontProperty());
        Label lblInverted = (Label) root.lookup("#lblInverted");
        lblInverted.fontProperty().bind(getSkinnable().fontProperty());
        lblInverted.textProperty().bind(Bindings.when(getSkinnable().invertedProperty()).then("not").otherwise(""));
        return root;
    }

    private Boolean handleDeleteCallback(SelectableControl currCtrl) {
        if (currCtrl == null) {
            return false;
        }
//        if (currCtrl.getParentElement().getControls().size() == 1) {
//            return false;
//        }
        return currCtrl.getParentElement().getControls().remove(currCtrl);
    }

    private void appendDragDropAtEnd(SelectableControl lastItem) {
        //set error if last item is link
        //is invalid
        if (lastItem instanceof Link) {
            appendDndArea(lastItem, SeverityEn.ERROR);
        } else {
            appendDndArea(lastItem, SeverityEn.NONE);
        }
    }
}
