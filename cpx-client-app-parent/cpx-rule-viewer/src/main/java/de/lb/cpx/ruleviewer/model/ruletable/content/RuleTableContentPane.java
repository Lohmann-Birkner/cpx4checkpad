/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.model.ruletable.content;

import de.lb.cpx.client.core.model.fx.tooltip.CpxTooltip;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.ruleviewer.model.RuleMessageIndicator;
import de.lb.cpx.ruleviewer.model.combobox.CatalogSuggestionComboBox;
import de.lb.cpx.ruleviewer.model.ruletable.content.model.Item;
import de.lb.cpx.shared.json.enums.MessageReasonEn;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.validation.Severity;

/**
 * FlowPane to show RuleTable Content
 *
 * @author wilde
 */
public class RuleTableContentPane extends FlowPane {

    private static final Logger LOG = Logger.getLogger(RuleTableContentPane.class.getName());

    protected static final String BORDER_STYLE = "-fx-border-color:lightgrey";
    protected static final String REFRESH_SEVERITY = "refreshSeverity";
    private EventHandler<ActionEvent> onContentChanged;
    private Callback<RuleTableContentParam, Item> itemFactory = new Callback<RuleTableContentParam, Item>() {
        @Override
        public Item call(RuleTableContentParam param) {
            return new Item(param.getText(), param.isEditable());
        }
    };

    public RuleTableContentPane() {
        super();
        setVgap(10);
        setHgap(10);
        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                requestFocus();
            }
        });
        validationMessageProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                validationMap.clear();
                if(t1 == null || t1.isEmpty()){
                    return;
                }
                String[] split = t1.split(",");
                for(String key : split){
                    validationMap.put(key.trim().replace("*", "%"), null);
                }
            }
        });
        
    }
    
    public RuleTableContentPane(String pContent, boolean pEditable) {
        this();
        setContent(pContent, pEditable);
    }
    
    public void refreshSeverity(){
        for(Node child : getChildren()){
            if(child instanceof Item){
                updateSeverity((Item) child);
            }
        }
    }
    public void refreshSeverityAsync(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                refreshSeverity();
            }
        });
    }
    public void setContent(String pContent, boolean pEditable) {
        if (pEditable) {
            getChildren().add(createAddButton());
        }
        addItems(pContent, pEditable);
    }

    public void setOnContentChanged(EventHandler<ActionEvent> pEventHandler) {
        onContentChanged = pEventHandler;
    }

    public final Button createAddButton() {
        Button btn = new Button("Hinzufügen");
        btn.setMaxHeight(Double.MAX_VALUE);
        btn.getStyleClass().add("cpx-icon-button");
        btn.setStyle(BORDER_STYLE);
        btn.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.PLUS_CIRCLE).color(Color.GREEN));
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Item item = createItem("", true);
                item.setEditbale(true);
                add(1, item);
                if (onContentChanged != null) {
                    onContentChanged.handle(new ActionEvent());
                }
            }
        });
        return btn;
    }
    public void add(int index, Item pItem){
        getChildren().add(1, pItem);
    }
    public void remove(Item pItem){
        getChildren().remove(pItem);
    }
    public Callback<RuleTableContentParam, Item> getItemFactory() {
        return itemFactory;
    }

    public void setItemFactory(Callback<RuleTableContentParam, Item> pFactory) {
        itemFactory = pFactory;
    }

    protected Item createItem(String pText, boolean pEditable) {
        Item item = getItemFactory().call(new RuleTableContentParam(pText, pEditable));//new Item(pText, pEditable);
        item.setOnDeleteRequested(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        remove(item);
                        if (onContentChanged != null) {
                            onContentChanged.handle(new ActionEvent());
                        }
                    }
                });
                event.consume();
            }
        });
        item.setMultiInputCallback(new Callback<String, Void>() {
            @Override
            public Void call(String param) {
                addItems(param, pEditable);
                return null;
            }
        });
        item.setOnContentChanged(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                item.getText();
                if (onContentChanged != null) {
                    onContentChanged.handle(t);
                }
            }
        });
        updateSeverity(item);
        item.setEditorFactory(new Callback<String, Control>() {
            @Override
            public Control call(String p) {
                if(!hasValidationMessage()){
                    return item.createEditTextField();
                }
                String validation = getValidationResult(p);
                if(validation != null && !validation.isEmpty()){
                    CatalogSuggestionComboBox cb = new CatalogSuggestionComboBox(validation);
                    cb.getSelectionModel().select(item.getText());
                    item.addEditorListeners(cb, true);
                    cb.setValueChangeCallback(new Callback<String, Void>() {
                        @Override
                        public Void call(String t1) {
                            item.setText(t1);
                            if (t1 == null) {
                                if (item.getOnDeleteRequested() != null) {
                                    item.getOnDeleteRequested().handle(new ActionEvent());
                                }
                            } else {
                                String text = t1;
                                text = text.replace("*", "%");
                                item.saveText(text);
                            }
                            item.setSeverity(Severity.OK);
                            return null;
                        }
                    });
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            cb.getEditor().requestFocus();
                        }
                    });
                    return cb;
                }
                return item.createEditTextField();
            }
        });
        return item;
    }
    //error prone, regrex would be a lot smarter if content has mixed separators

    private String[] splitContent(String pContent) {
        if (pContent == null) {
            return new String[0];
        }
        return pContent.split(" |,|\n");
    }

    protected List<Node> createItems(String pContent, boolean pEditable) {
        long start = System.currentTimeMillis();
        List<Node> items = new ArrayList<>();
        String[] texts = splitContent(pContent);
        LOG.info("split text in " + (System.currentTimeMillis() - start));
        for (String text : texts) {
            items.add(createItem(text, pEditable));
        }
        LOG.info("get list im " + (System.currentTimeMillis() - start));
        return items;
    }
    static <T> List<List<T>> chopped(List<T> list, final int L) {
        List<List<T>> parts = new ArrayList<List<T>>();
        final int N = list.size();
        for (int i = 0; i < N; i += L) {
            parts.add(new ArrayList<T>(
                list.subList(i, Math.min(N, i + L)))
            );
        }
        return parts;
    }
    protected void displayItems(List<Node> pItems) {
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
                long start = System.currentTimeMillis();
                getChildren().addAll(pItems);
//                for(List<Node> chop : chopped(pItems, 100)){
//                    getChildren().setAll(chop);
//                }
//                getChildren().setAll(pItems);
                LOG.info("add all items(" + pItems.size() + ") in " + (System.currentTimeMillis() - start) + " ms");
//            }
//        });
    }

    protected void addItems(String pContent, boolean pEditable) {
        long start = System.currentTimeMillis();
        List<Node> items = createItems(pContent, pEditable);
        LOG.info("create items in " + (System.currentTimeMillis() - start));
        displayItems(items);
        LOG.info("display items in " + (System.currentTimeMillis() - start));
    }
    
    private StringProperty validationMessageProperty;
    public final StringProperty validationMessageProperty() {
        if (validationMessageProperty == null) {
            validationMessageProperty = new SimpleStringProperty();
        }
        return validationMessageProperty;
    }

    public String getValidationMessage() {
        return validationMessageProperty().get();
    }

    public void setValidationMessage(String pValidationMessage) {
        validationMessageProperty().set(pValidationMessage);
    }
    
    private final Callback<String,String> DEFAULT_VALIDATION_CALLBACK = (String p) -> null;
    private Callback<String,String> validationCallback = DEFAULT_VALIDATION_CALLBACK;
    public void setValidationCallback(Callback<String,String> pCallback){
        validationCallback = Objects.requireNonNullElse(pCallback, DEFAULT_VALIDATION_CALLBACK);
    }
    public Callback<String,String> getValidationCallback(){
        return validationCallback;
    }
    private final Map<String,String> validationMap = new HashMap<>();
    
    public String getValidationResult(String pCode){
        String key = validationMapGetKey(pCode);
        if(key == null){
            return null;
        }
        if(validationMap.get(key)==null){
            validationMap.put(key, getValidationCallback().call(pCode));
        }
        return validationMap.get(key);
    }
    
    public boolean hasValidationMessage(){
        return getValidationMessage() != null && !getValidationMessage().isEmpty();
    }
    public String validationMapGetKey(String pCode){
        if(pCode == null){
            return null;
        }
        Iterator<String> it = validationMap.keySet().iterator();
        while (it.hasNext()) {
            String next = it.next();
            String key = next;
            if(key.contains(":")){
                key = key.split(":")[0].trim();
            }
            if(key.equals(pCode)){
                return next;
            }
        }
        return null;
    }
    public boolean valdationMapContainsCode(String pCode){
        return validationMapGetKey(pCode)!=null;
    }    public MessageReasonEn getMessageReasonForCode(String pCode){
        String key = validationMapGetKey(pCode);
        if(key == null){
            return null;        }
        if (key.contains(pCode)) {
            String[] split = key.split(":");
            if (split.length > 1) {
                return MessageReasonEn.valueOfIndex(split[1].trim());
            }
        }
        return null;
    }

    private void updateSeverity(Item item) {
        boolean isMarked = valdationMapContainsCode(item.getText().trim());
        item.setSeverity(isMarked?Severity.ERROR:Severity.OK);
        if(isMarked){
            RuleMessageIndicator indicator = new RuleMessageIndicator();
            MessageReasonEn reason = getMessageReasonForCode(item.getText().trim());
            String msg = reason!=null?reason.toString():"No specific Reason found!";
            indicator.setTooltip(new CpxTooltip(msg, 100, 5000, 100, true));
            item.setGraphic(indicator);
        }else{
            item.setGraphic(null);
        }
    }
    public class RuleTableContentParam {

        private final String text;
        private final boolean editable;

        public RuleTableContentParam(String pText, boolean pEditable) {
            editable = pEditable;
            text = pText;
        }

        public String getText() {
            return text;
        }

        public boolean isEditable() {
            return editable;
        }

    }

}
