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
package de.lb.cpx.ruleviewer.layout;

import de.lb.cpx.rule.element.model.Rule;
import de.lb.cpx.rule.element.model.Sugg;
import de.lb.cpx.rule.element.model.Suggestions;
import de.lb.cpx.ruleviewer.dragdrop.format.DndFormat;
import de.lb.cpx.ruleviewer.event.RuleChangedEvent;
import de.lb.cpx.ruleviewer.model.SelectableControl;
import de.lb.cpx.ruleviewer.model.Suggestion;
import de.lb.cpx.ruleviewer.model.SuggestionArea;
import de.lb.cpx.ruleviewer.util.SelectableControlFactory;
import de.lb.cpx.shared.json.RuleMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javax.validation.constraints.NotNull;

/**
 * Skin class for the suggestion view
 *
 * @author wilde
 */
public class SuggestionViewSkin extends BasicEditorViewSkin<SuggestionView> {

    private static final Logger LOG = Logger.getLogger(SuggestionViewSkin.class.getName());

    public SuggestionViewSkin(SuggestionView pSkinnable) {
        super(pSkinnable);
//        pSkinnable.suggestionProperty().addListener(new ChangeListener<Suggestions>() {
//            @Override
//            public void changed(ObservableValue<? extends Suggestions> observable, Suggestions oldValue, Suggestions newValue) {
//                updateUi();
//            }
//        });
        pSkinnable.ruleProperty().addListener(new ChangeListener<Rule>() {
            @Override
            public void changed(ObservableValue<? extends Rule> observable, Rule oldValue, Rule newValue) {
                updateUi();
            }
        });
        pSkinnable.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                //reorder suggestions
                if (event.getDragboard().hasContent(DndFormat.REORDER_FORMAT)) {
                    getSkinnable().getControls().size();
                    Suggestion moved = (Suggestion) event.getGestureSource();
                    Suggestion target = (Suggestion) event.getAcceptingObject();
                    int index = getSkinnable().getControls().indexOf(target);

                    javafx.geometry.Point2D sceneCoordinates = target.localToScene(0d, 0d);
                    double height = target.getHeight();
                    // get the y coordinate within the control
                    double mouseY = event.getSceneY() - (sceneCoordinates.getY());
                    remove(moved);
                    if (mouseY <= (height * .25d)) {
                        addAbove(index, moved);
                    } else {
                        addBelow(index, moved);
                    }
                    updateUi();
                    event.setDropCompleted(true);
                    event.consume();
                    RuleChangedEvent saveEvent = new RuleChangedEvent(RuleChangedEvent.ruleChangedEvent(), moved);
                    Event.fireEvent(moved, saveEvent);
                }
                if (event.getDragboard().hasContent(DndFormat.ADD_FORMAT)) {
                    Suggestion target = (Suggestion) event.getAcceptingObject();
                    int index = getSkinnable().getControls().indexOf(target);
                    //should be list
                    List<Sugg> content = (List<Sugg>) event.getDragboard().getContent(DndFormat.ADD_FORMAT);
                    //handle only first item, should not be possible that more than one suggestion is added??
                    //TODO: maybe in future there is a need to add suggestion as rule template if suggestions are always the same
                    //for now ignored
                    Suggestion suggestion = (Suggestion) SelectableControlFactory.instance().createControl(content.get(0));
                    suggestion.fontProperty().bind(getSkinnable().fontProperty());
                    javafx.geometry.Point2D sceneCoordinates = target.localToScene(0d, 0d);
                    double height = target.getHeight();
                    // get the y coordinate within the control
                    double mouseY = event.getSceneY() - (sceneCoordinates.getY());
//                    remove(suggestion);
                    if (mouseY <= (height * .25d)) {
                        addAbove(index, suggestion);
                    } else {
                        addBelow(index, suggestion);
                    }
                    getSkinnable().setSelectedNode(suggestion);
                    updateUi();
                    event.setDropCompleted(true);
                    event.consume();
                    RuleChangedEvent saveEvent = new RuleChangedEvent(RuleChangedEvent.ruleChangedEvent(), getSkinnable());
                    Event.fireEvent(getSkinnable(), saveEvent);
                }
            }
        });
        getSkinnable().getRuleMessages().addListener(new ListChangeListener<RuleMessage>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends RuleMessage> change) {
                if(change.next()){
                    ObservableList<RuleMessage> list = (ObservableList<RuleMessage>) change.getList();
                    updateLayoutForRuleMessage(list);
                }
            }
        });
        pSkinnable.getProperties().addListener(new MapChangeListener<Object, Object>() {
            @Override
            public void onChanged(MapChangeListener.Change<? extends Object, ? extends Object> change) {
                if (change.wasAdded()) {
                    if (BasicEditorView.REFRESH_RULE_MESSAGE.equals(change.getKey())) {
                        LOG.finer("REFRESH RuleMessage");
                        updateLayoutForRuleMessage(getSkinnable().getRuleMessages());
                        pSkinnable.getProperties().remove(BasicEditorView.REFRESH_RULE_MESSAGE);
                    }
                }
            }
        });
//        updateUi();
        LOG.finest("create new Skin");
    }
    
    private void updateLayoutForRuleMessage(ObservableList<RuleMessage> pMessage) {
        for(SelectableControl ctrl : getSkinnable().getControls()){
            ((Suggestion)ctrl).setMessage(null);
        }
        for (RuleMessage message : pMessage) {
            Suggestion sugg = getSkinnable().getSuggestionForText(message.getTerm());
            if(sugg != null){
                sugg.setMessage(message);
            }
//            if (message == null || (message.getTerm() == null || message.getTerm().isEmpty())) {
//                ((Term) pControl).setMessage(null);
//                return;
//            }
//            String term = message.getTerm();
//            if (term.equals(((Term) pControl).toString().trim())) {
//                ((Term) pControl).setMessage(message);
//            }
        }
    }
    
    private void remove(Suggestion pSuggestion) {
        getSkinnable().getControls().remove(pSuggestion);
        getSkinnable().getRule().getSuggestions().getSugg().remove(pSuggestion.getSuggestion());
        pSuggestion.setDeleteCallback(null);
    }

    private void add(Suggestion pSuggestion) {
        add(getSkinnable().getControls().size(), pSuggestion);
    }

    private void add(Integer pIndex, Suggestion pSuggestion) {
        getSkinnable().getControls().add(pIndex, pSuggestion);
        if (getSkinnable().getRule().getSuggestions() == null) {
            getSkinnable().getRule().setSuggestions(new Suggestions());
        }
        getSkinnable().getRule().getSuggestions().getSugg().add(pIndex, pSuggestion.getSuggestion());
    }

    private void addAbove(int pIndex, Suggestion pSuggestion) {
        if (pIndex < 0) {
            return;
        }
        add(pIndex, pSuggestion);
    }

    private void addBelow(int pIndex, Suggestion pSuggestion) {
//        if(pIndex+1 > getSkinnable().getControls().size()){
//            pIndex = getSkinnable().getControls().size();
//        }
        if (pIndex + 1 > getSkinnable().getControls().size()) {
            add(pSuggestion);
            return;
        }
        add(pIndex + 1, pSuggestion);
    }

    @Override
    public void updateUi() {
        int idx = getSkinnable().getControls().indexOf(getSkinnable().getSelectedNode());
        getSkinnable().getControls().clear();
        LOG.fine("update the suggestion skin");
        if (getSkinnable().getRule() == null) {
            return;
        }
        List<Suggestion> suggs = createSuggestions();
        VBox root = new VBox();
//        root.setFillWidth(true);
//        root.prefWidthProperty().bind(getSkinnable().widthProperty());
        root.getChildren().addAll(suggs);
        if (!getSkinnable().isReadOnly()) {
            root.getChildren().add(createDndArea());
        }
        getSkinnable().getControls().addAll(suggs);
        if (idx != -1) {
            getSkinnable().setSelectedNode(getSkinnable().getControls().get(idx));
        }
        getChildren().clear();
        ScrollPane pane = new ScrollPane(root);
//        pane.prefWidthProperty().bind(getSkinnable().widthProperty());
        pane.setFitToHeight(true);
        pane.setFitToWidth(true);
        getChildren().add(pane);
        updateLayoutForRuleMessage(getSkinnable().getRuleMessages());
    }
    private void updateLayoutForRuleMessageAsync(ObservableList<RuleMessage> ruleMessages){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                updateLayoutForRuleMessage(ruleMessages);
            }
        });
    }
    private SuggestionArea createDndArea() {
        SuggestionArea area = new SuggestionArea();
        area.setOnDropDetected(new Callback<DragEvent, Void>() {
            @Override
            public Void call(DragEvent param) {
                if (param.getDragboard().hasContent(DndFormat.ADD_FORMAT)) {
                    List<Sugg> content = (List<Sugg>) param.getDragboard().getContent(DndFormat.ADD_FORMAT);
                    //handle only first item, should not be possible that more than one suggestion is added??
                    //TODO: maybe in future there is a need to add suggestion as rule template if suggestions are always the same
                    //for now ignored
                    Suggestion suggestion = (Suggestion) SelectableControlFactory.instance().createControl(content.get(0));
                    add(suggestion);
                    param.setDropCompleted(true);
                    getSkinnable().setSelectedNode(suggestion);
                }
                if (param.getDragboard().hasContent(DndFormat.REORDER_FORMAT)) {
                    Suggestion moved = (Suggestion) param.getGestureSource();
                    remove(moved);
                    add(moved);
                    param.setDropCompleted(true);
                }
                updateUi();
                param.consume();
                RuleChangedEvent saveEvent = new RuleChangedEvent(RuleChangedEvent.ruleChangedEvent(), getSkinnable());
                Event.fireEvent(getSkinnable(), saveEvent);
                return null;
            }
        });
        return area;
    }

    public Suggestion createSuggestion(Sugg pSugg) {
        Suggestion suggestion = new Suggestion();
        suggestion.setViewMode(getSkinnable().getViewMode());
        suggestion.fontProperty().bind(getSkinnable().fontProperty());
        getSkinnable().selectedNodeProperty().bindBidirectional(suggestion.selectedNodeProperty());
//        suggestion.setViewMode(ViewMode.READ_WRITE);
        suggestion.setDeleteCallback(new Callback<Void, Boolean>() {
            @Override
            public Boolean call(Void param) {
                int idx = getSkinnable().getControls().indexOf(suggestion);
                remove(suggestion);
                updateUi();
//                selectIndex(idx);
                getSkinnable().setSelectedNode(null);
                RuleChangedEvent saveEvent = new RuleChangedEvent(RuleChangedEvent.ruleChangedEvent(), getSkinnable());
                Event.fireEvent(getSkinnable(), saveEvent);
                return true;
            }
        });
        suggestion.setDuplicateCallback(new Callback<Void, Boolean>() {
            @Override
            public Boolean call(Void p) {
//                Suggestion duplicate = duplicateSuggestion(suggestion);
//                addBelow(getSkinnable().getControls().indexOf(suggestion),duplicate);
                Sugg xmlSuggestion = duplicateSuggestion(suggestion);
                int index = getSkinnable().getControls().indexOf(suggestion);
                if (index + 1 > getSkinnable().getControls().size()) {
//                    add(suggestion);
                    getSkinnable().getRule().getSuggestions().getSugg().add(xmlSuggestion);
                } else {
//                    add(index+1,suggestion);
                    getSkinnable().getRule().getSuggestions().getSugg().add(index + 1, xmlSuggestion);
                }
//                getSkinnable().getRule().getSuggestions().getSugg().add(xmlSuggestion);
                updateUi();
//                getSkinnable().setSelectedNode(duplicate);
                RuleChangedEvent saveEvent = new RuleChangedEvent(RuleChangedEvent.ruleChangedEvent(), getSkinnable());
                Event.fireEvent(getSkinnable(), saveEvent);
                return true;
            }
        });
        suggestion.setSuggestion(pSugg);
        return suggestion;
    }

//    private void selectIndex(int pIndex) {
//        if (pIndex == -1) {
//            getSkinnable().setSelectedNode(null);
//            return;
//        }
//        if (getSkinnable().getControls().isEmpty()) {
//            getSkinnable().setSelectedNode(null);
//            return;
//        }
//        if (getSkinnable().getControls().size() == 1) {
//            getSkinnable().selectFirst();
//            return;
//        }
//        if (pIndex >= getSkinnable().getControls().size()) {
//            getSkinnable().setSelectedNode(getSkinnable().getControls().get(getSkinnable().getControls().size() - 1));
//        } else {
//            getSkinnable().setSelectedNode(getSkinnable().getControls().get(pIndex));
//        }
//    }
    private List<Suggestion> createSuggestions() {
        List<Suggestion> suggs = new ArrayList<>();
        if (getSkinnable().getRule().getSuggestions() == null) {
            return suggs;
        }
        if (getSkinnable().getRule().getSuggestions() != null) {
            for (Sugg sugg : getSkinnable().getRule().getSuggestions().getSugg()) {
                Suggestion suggestion = createSuggestion(sugg);
                suggs.add(suggestion);
            }
        }
        return suggs;
    }
//    private Suggestion duplicateSuggestion(@NotNull Suggestion pSuggestion){
//        Objects.requireNonNull(pSuggestion, "Suggestion to duplicate can not be null");
//        
//        Sugg xmlSuggestion = new Sugg();
//        xmlSuggestion.setActionid(pSuggestion.getSuggestion().getActionid());
//        xmlSuggestion.setConditionOp(pSuggestion.getSuggestion().getConditionOp());
//        xmlSuggestion.setConditionValue(pSuggestion.getSuggestion().getConditionValue());
//        xmlSuggestion.setCrit(pSuggestion.getSuggestion().getCrit());
//        xmlSuggestion.setOp(pSuggestion.getSuggestion().getOp());
//        xmlSuggestion.setValueAttribute(pSuggestion.getSuggestion().getValueAttribute());
//        
//        return createSuggestion(xmlSuggestion);
//    }

    private Sugg duplicateSuggestion(@NotNull Suggestion pSuggestion) {
        Objects.requireNonNull(pSuggestion, "Suggestion to duplicate can not be null");

        Sugg xmlSuggestion = new Sugg();
        xmlSuggestion.setActionid(pSuggestion.getSuggestion().getActionid());
        xmlSuggestion.setConditionOp(pSuggestion.getSuggestion().getConditionOp());
        xmlSuggestion.setConditionValue(pSuggestion.getSuggestion().getConditionValue());
        xmlSuggestion.setCrit(pSuggestion.getSuggestion().getCrit());
        xmlSuggestion.setOp(pSuggestion.getSuggestion().getOp());
        xmlSuggestion.setValueAttribute(pSuggestion.getSuggestion().getValueAttribute());

        return xmlSuggestion;
    }
}
