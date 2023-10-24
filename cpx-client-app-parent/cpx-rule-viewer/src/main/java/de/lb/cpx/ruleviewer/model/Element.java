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
package de.lb.cpx.ruleviewer.model;

import de.lb.cpx.rule.element.model.RulesElement;
import de.lb.cpx.rule.element.model.RulesOperator;
import de.lb.cpx.rule.element.model.RulesValue;
import de.lb.cpx.ruleviewer.skin.ElementSkin;
import de.lb.cpx.shared.json.RuleMessage;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Skin;

/**
 *
 * @author wilde
 */
public final class Element extends SelectableControl {

    private static final String DEFAULT_STYLE_CLASS = "element";
    private static final String DEFAULT_STYLE_SHEET = "/styles/rule_visualizer.css";
//    private ObservableList<SelectableControl> controls;
//    private StructureTreeItem treeItem;
    private PropertyDescriptor[] propDescriptors;

    public Element() {
        super();
        getStylesheets().add(getClass().getResource(DEFAULT_STYLE_SHEET).toExternalForm());
        getStyleClass().add(DEFAULT_STYLE_CLASS);

        parentElementProperty().addListener(new ChangeListener<Element>() {
            @Override
            public void changed(ObservableValue<? extends Element> observable, Element oldValue, Element newValue) {
                setNested(newValue != null);
            }
        });
        getRuleMessages().addListener(new ListChangeListener<RuleMessage>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends RuleMessage> change) {
                if(change.next()){
                    ObservableList<RuleMessage> list = (ObservableList<RuleMessage>) change.getList();
                    updateLayoutForRuleMessage(list, Element.this);
                }
            }
        });
//        ruleMessageProperty().addListener(new ChangeListener<RuleMessage>() {
//            @Override
//            public void changed(ObservableValue<? extends RuleMessage> ov, RuleMessage t, RuleMessage t1) {
//                updateLayoutForRuleMessage(t1, Element.this);
//            }
//        });
//        getControls().add(new Term());
//        setSelectedNode(getControls().get(0));
    }

    public Element(SelectableControl... pCtrls) {
        super();
        getControls().addAll(pCtrls);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        try {
            return new ElementSkin(this);
        } catch (IOException ex) {
            Logger.getLogger(Element.class.getName()).log(Level.SEVERE, null, ex);
        }
        return super.createDefaultSkin(); //To change body of generated methods, choose Tools | Templates.
    }

    public void setNested(boolean pNested) {
        if (getRulesElement() != null) {
            getRulesElement().setNested(String.valueOf(pNested));
        }
    }

    public void addControls(SelectableControl... pControls) {
        getControls().addAll(pControls);
    }

    public void addControl(int index, SelectableControl pControl) {
        getControls().add(index, pControl);
    }

    public void setControl(int pIndex, SelectableControl pControl) {
        getControls().set(pIndex, pControl);
    }
//    public ObservableList<SelectableControl> getControls(){
//        if(controls == null){
//            controls = FXCollections.observableArrayList();
//        }
//        return controls;
//    }
//    public int getIndex(SelectableControl pCtrl){
//        return controls.indexOf(pCtrl);
//    }

    public SelectableControl getLast() {
        if (getControls().isEmpty()) {
            return null;
        }
        return getControls().get(getControls().size() - 1);
    }

    public SelectableControl getFirst() {
        if (getControls().isEmpty()) {
            return null;
        }
        return getControls().get(0);
    }
    private static final Logger LOG = Logger.getLogger(Element.class.getName());

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
//        if (ViewMode.READ_ONLY.equals(getViewMode())) {
//            return new PropertyDescriptor[0];
//        }
        if (propDescriptors == null) {
            propDescriptors = new PropertyDescriptor[1];
            try {
                propDescriptors[0] = new PropertyDescriptor("invertedProperty", Element.class, "isInverted", "setInverted");
                propDescriptors[0].setDisplayName("Verneint:");
                propDescriptors[0].setPreferred(true);

            } catch (IntrospectionException ex) {
                LOG.log(Level.WARNING, ex.toString(), ex);
            }
        }
        return propDescriptors.clone();
    }

    @Override
    public String getDisplayName() {
        return "Klammer";
    }

    @Override
    public String getDisplayText() {
        return getDisplayName() + ": " + (isInverted() ? "not" : "")
                + " (" + getContent() + ")";
    }

    @Override
    public String toString() {
        return " (" + getContent() + ") ";
    }

    public String getContent() {
        return getControls().stream().map(item -> item.toString()).collect(Collectors.joining(""));
    }

    @Override
    public void refresh() {
        getControls().forEach((SelectableControl t) -> {
            t.refresh();
        });
    }
    
//    private ObjectProperty<RuleMessage> ruleMessageProperty;
//    public ObjectProperty<RuleMessage> ruleMessageProperty(){
//        if(ruleMessageProperty == null){
//            ruleMessageProperty = new SimpleObjectProperty<>();
//        }
//        return ruleMessageProperty;
//    }
//    public List<RuleMessage> getRuleMessage(){
//        return ruleMessageProperty().get();
//    }
//    public void setRuleMessage(List<RuleMessage> pMessage){
//        ruleMessageProperty().set(pMessage);
//    }
    private ObservableList<RuleMessage> ruleMessages;
    public ObservableList<RuleMessage> getRuleMessages(){
        if(ruleMessages == null){
            ruleMessages = FXCollections.observableArrayList();
        }
        return ruleMessages;
    }
    public void setRuleMessage(List<RuleMessage> pMessage){
        if(pMessage == null || pMessage.isEmpty()){
            getRuleMessages().clear();
            return;
        }
        getRuleMessages().setAll(pMessage);
    }
    
    private void updateLayoutForRuleMessage(ObservableList<RuleMessage> pMessage,SelectableControl pControl){
        if(pControl instanceof Element){
            for(SelectableControl ctrl : pControl.getControls()){
                updateLayoutForRuleMessage(pMessage, ctrl);
            }
        }
        if(pControl instanceof Term){
            ((Term) pControl).setMessage(null);
            for(RuleMessage message : pMessage){
                if (message == null || (message.getTerm() == null || message.getTerm().isEmpty())) {
                    ((Term) pControl).setMessage(null);
                    return;
                }
                String term = Objects.requireNonNullElse(message.getTerm(),"");
                if (term.trim().equalsIgnoreCase(((Term) pControl).toString().trim())) {
                    ((Term) pControl).setMessage(message);
                }
            }
        }
    }
    public void setRulesElement(int pIndex, RulesElement pElement) {
        List<SelectableControl> ctrls = new ArrayList<>();
        long start = System.currentTimeMillis();
        if (pElement == null) {
            getControls().addAll(ctrls);
            return;
        }
        ruleElementProperty().set(pElement);
        for (Object obj : pElement.getRulesValueOrRulesOperatorOrRulesElement()) {
            pIndex++;
            if (obj instanceof RulesValue) {
                long startTerm = System.currentTimeMillis();
                Term term = new Term();
                term.setParentElement(this);
                long termvalue = System.currentTimeMillis();
                term.setRulesValue((RulesValue) obj);
                LOG.finest("set Term value in " + (System.currentTimeMillis() - termvalue));
                term.setViewMode(getViewMode());
                ctrls.add(term);
                term.setId(String.valueOf(pIndex));
                LOG.finest("create Term in " + (System.currentTimeMillis() - startTerm));
            }
            if (obj instanceof RulesOperator) {
                long startLink = System.currentTimeMillis();
                Link link = new Link();
                link.setParentElement(this);
                link.setRulesOperator((RulesOperator) obj);
                link.setViewMode(getViewMode());
                ctrls.add(link);
                link.setId(String.valueOf(pIndex));
                LOG.finest("create Link in " + (System.currentTimeMillis() - startLink));
            }
            if (obj instanceof RulesElement) {
                long startElement = System.currentTimeMillis();
                Element ele = new Element();
                ele.setViewMode(getViewMode());
                ele.setRulesElement(pIndex, (RulesElement) obj);
                ele.setParentElement(this);
                ele.setId(String.valueOf(pIndex));
                ctrls.add(ele);
                LOG.finest("create element in " + (System.currentTimeMillis() - startElement));
            }
        }
        getControls().addAll(ctrls);
        updateLayoutForRuleMessage(getRuleMessages(), this);
        LOG.finest("creates items in " + (System.currentTimeMillis() - start));
    }
    private ObjectProperty<RulesElement> rulesElementProperty;

    public ObjectProperty<RulesElement> ruleElementProperty() {
        if (rulesElementProperty == null) {
            rulesElementProperty = new SimpleObjectProperty<>();
        }
        return rulesElementProperty;
    }

    public RulesElement getRulesElement() {
        return ruleElementProperty().get();
    }

    private BooleanProperty invertedProperty;// = new SimpleBooleanProperty(false);

    public BooleanProperty invertedProperty() {
        if (invertedProperty == null) {
            invertedProperty = new SimpleBooleanProperty();
            if (getRulesElement() != null) {
                invertedProperty.set(Boolean.parseBoolean(getRulesElement().getNot()));
            }
            invertedProperty.addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                    getProperties().put(UPDATE_CONTROL, null);
                    updateRulesElement();
                }
            });
        }
        return invertedProperty;
    }

    public boolean isInverted() {
        return invertedProperty().get();
    }

    public void setInverted(boolean inverted) {
        invertedProperty().set(inverted);
    }

    private void updateRulesElement() {
        getRulesElement().setNot(String.valueOf(isInverted()));//String.valueOf(isInverted()));
        getProperties().put(UPDATE_CONTROL, null);
    }

    public Node findNodeById(String id) {
        if ("-1".equals(id)) {
            return null;
        }
        for (SelectableControl ctrl : getControls()) {
            if (ctrl.getId().equals(id)) {
                return ctrl;
            }
            if (ctrl instanceof Element) {
                return ((Element) ctrl).findNodeById(id);
            }
        }
        return null;
    }
}
