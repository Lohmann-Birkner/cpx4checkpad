/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.cm.fx.documentation;

import de.lb.cpx.client.core.model.fx.button.AddButton;
import de.lb.cpx.client.core.model.fx.button.DeleteButton;
import de.lb.cpx.client.core.model.fx.listview.cell.TwoLineCell;
import de.lb.cpx.client.core.model.fx.menu.MenuedControl;
import de.lb.cpx.model.TCaseComment;
import de.lb.cpx.model.enums.CommentTypeEn;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

/**
 *
 * @author wilde
 */
public class CaseDocumentation extends DocumentationTitledPane<TCaseComment> {

    private static final Logger LOG = Logger.getLogger(CaseDocumentation.class.getName());
    private final BooleanProperty activeSelected = new SimpleBooleanProperty(false);
    private final ListView<TCaseComment> listView;
    private boolean clearFlag;
    
    public CaseDocumentation() {
        super(CommentTypeEn.caseReview);
        listView = new ListView<>();
        List<Node> expandedButtons = initExpandedButtons();
//        Button btnLink = initLinkButton();
        setContentFactory(new Callback<CommentTypeEn, Node>() {
            @Override
            public Node call(CommentTypeEn param) {
                ExpandableContent pane = new ExpandableContent(listView);
                pane.getControl().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TCaseComment>() {
                    @Override
                    public void changed(ObservableValue<? extends TCaseComment> observable, TCaseComment oldValue, TCaseComment newValue) {
                        //set new detail content for new selection
                        getSelectItemCallback().call(new DocTitledPaneSelectedItem<>(newValue));
                    }
                });
                return pane;
            }
        });
        expandedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if (t1) {
                    getMenuItems().addAll(0, expandedButtons);
//                    if (getContent() != null && (getContent() instanceof ExpandableContent)) {
//                        getSelectItemCallback().call(getActiveComment());
//                    }
                }else{
                    getMenuItems().removeAll(expandedButtons);
                }
            }
        });
    }
    
    @Override
    public void clearSelection(){
        if(listView.getSelectionModel().getSelectedItem()!=null){
            clearFlag = true;
        }
        listView.getSelectionModel().clearSelection();
    }
    
    @Override
    public DocTitledPaneSelectedItem<TCaseComment> getMostRecentItem(SelectionTarget pTarget) {
        TCaseComment comment = getActiveComment();
        listView.getSelectionModel().select(comment);
        return new DocTitledPaneSelectedItem<>(comment);
    }
    
    //searches list for current active comment
    public TCaseComment getActiveComment() {
        for (TCaseComment comment : getValues()) {
            if (comment.isActive()) {
                return comment;
            }
        }
        return null;
    }
    
    /**
     * TODO:Refactor, number should be set from server after storing?
     *
     * @return max number in list of comments
     */
    public long getMaxNumber() {
        long number = 0;
        for (TCaseComment comment : getValues()) {
            if (comment.getNumber() > number) {
                number = comment.getNumber();
            }
        }
        return number;
    }
    
    private List<Node> initExpandedButtons(){
        List<Node> expandedButtons = new ArrayList<>();
//        expandedButtons.add(new Separator(Orientation.VERTICAL));
        expandedButtons.add(initAddButton());
        expandedButtons.add(initRemoveButton());
        return expandedButtons;
    }
    private Button initAddButton(){
        AddButton btn = new AddButton();
        btn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if(!MouseButton.PRIMARY.equals(t.getButton())){
                    return;
                }
                t.consume();
                TCaseComment oldComment = getActiveComment();
                if (oldComment == null) {
                    LOG.log(Level.WARNING, "old Comment is null!");
                    return;
                }
                oldComment.setActive(Boolean.FALSE);
                store(oldComment);
                TCaseComment newComment = new TCaseComment();
                newComment.setTypeEn(getAccordionItem());
                newComment.setActive(Boolean.TRUE);
                //TODO: generate on server-side?!?
                newComment.setnumber(getMaxNumber() + 1);
                newComment = store(newComment);
                getValues().add(newComment);
                listView.scrollTo(newComment);
                listView.getSelectionModel().select(newComment);
            }
        });
//        btn.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent t) {
//                TCaseComment oldComment = getActiveComment();
//                if (oldComment == null) {
//                    LOG.log(Level.WARNING, "old Comment is null!");
//                    return;
//                }
//                oldComment.setActive(Boolean.FALSE);
//                store(oldComment);
//                TCaseComment newComment = new TCaseComment();
//                newComment.setTypeEn(getType());
//                newComment.setActive(Boolean.TRUE);
//                //TODO: generator auf dem server?!?
//                newComment.setnumber(getMaxNumber() + 1);
//                newComment = store(newComment);
//                getValues().add(newComment);
//                listView.scrollTo(newComment);
//                listView.getSelectionModel().select(newComment);
//            }
//        });
        return btn;
    }
    private Button initRemoveButton() {
        DeleteButton btn = new DeleteButton();
        btn.disableProperty().bind(Bindings
                .when(listView.getSelectionModel().selectedItemProperty().isNull().or(activeSelected))
                .then(true)
                .otherwise(false));
//        btn.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent t) {
        btn.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if(!MouseButton.PRIMARY.equals(t.getButton())){
                    return;
                }
                t.consume();
                //remove selected item
                //no validation needed, remove should only be occuring, when remove button is pressed
                //button gets disabled in certain circumstances
                TCaseComment selected = listView.getSelectionModel().getSelectedItem();
                CaseDocumentation.this.remove(selected);
                getValues().remove(selected);
                listView.getSelectionModel().selectNext();
            }
        });
        return btn;
    }
    
    //TODO: make this listview titledpane or somthing like this from ruleeditor
    public class ExpandableContent extends MenuedControl<ListView<TCaseComment>> {

        public ExpandableContent(ListView<TCaseComment> pListView) {
            super(pListView);
            getControl().getStyleClass().add("stay-selected-list-view");
            //if selction changed, change state of active selected
            //could be refactored to work with Bindings-Api
            getControl().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TCaseComment>() {
                @Override
                public void changed(ObservableValue<? extends TCaseComment> observable, TCaseComment oldValue, TCaseComment newValue) {
                    if (newValue == null) {
                        activeSelected.set(false);
                        return;
                    }
                    activeSelected.set(newValue.isActive());
                }
            });

            getControl().getItems().setAll(getValues());
            getControl().getSelectionModel().select(getActiveComment());

            getControl().setCellFactory(new Callback<ListView<TCaseComment>, ListCell<TCaseComment>>() {
                @Override
                public ListCell<TCaseComment> call(ListView<TCaseComment> param) {
                    TwoLineCell<TCaseComment> cell = new TwoLineCell<TCaseComment>() {
                        @Override
                        public void populateContextMenu() {
                            //populate context menu to add menu option to current menu
                            super.populateContextMenu();
                            MenuItem item = new MenuItem(Lang.getDocumentationMenuSetActive());
                            item.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    TCaseComment old = getActiveComment();
                                    if (old == null) {
                                        LOG.log(Level.WARNING, "old comment is null!");
                                        return;
                                    }
                                    old.setActive(Boolean.FALSE);
                                    store(old);
                                    getItem().setActive(Boolean.TRUE);
                                    if(getControl().getSelectionModel().isSelected(getIndex())){
                                        activeSelected.set(getItem().isActive());
                                    }
                                    store(getItem());
                                    getControl().refresh();
                                }
                            });
                            //add in first place
                            getContextMenu().getItems().add(0, item);
                        }

                        @Override
                        protected void updateItem(TCaseComment item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item == null || empty) {
                                setTitle("");
                                setDescription("");
                                return;
                            }
                            //update items
                            setTitle(Lang.getNumber(item.getNumber()));
                            setDescription(Lang.getDocumentationMenuIsActive() + " " + (item.isActive() ? Lang.getConfirmationYes() : Lang.getConfirmationNo()));
                        }
                    };
                    cell.setPrefHeight(50);
                    cell.setStyle("-fx-padding:16;");
                    return cell;
                }
            });
        }

        /**
         * selects current active comment in the ui
         */
        public void selectActive() {
            TCaseComment active = getActiveComment();
            if (active == null) {
                return;
            }
            getControl().getSelectionModel().select(getActiveComment());
        }

    }

}
