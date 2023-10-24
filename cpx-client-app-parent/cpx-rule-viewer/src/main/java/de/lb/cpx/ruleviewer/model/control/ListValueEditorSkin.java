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
package de.lb.cpx.ruleviewer.model.control;

import de.lb.cpx.client.core.model.fx.adapter.WeakListAdapter;
import de.lb.cpx.client.core.model.fx.adapter.WeakPropertyAdapter;
import de.lb.cpx.client.core.model.fx.button.AddButton;
import de.lb.cpx.client.core.model.fx.button.TextModeToggleButton;
import de.lb.cpx.client.core.model.fx.tooltip.CpxTooltip;
import de.lb.cpx.client.core.util.ScrollBarHelper;
import de.lb.cpx.ruleviewer.model.RuleMessageIndicator;
import de.lb.cpx.ruleviewer.model.ruletable.content.model.Item;
import de.lb.cpx.shared.json.enums.MessageReasonEn;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.ScrollPaneSkin;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import org.controlsfx.validation.Severity;

/**
 *
 * @author wilde
 */
public class ListValueEditorSkin extends SkinBase<ListValueEditor>{

    private static final Logger LOG = Logger.getLogger(ListValueEditorSkin.class.getName());
    
    protected static final String BORDER_STYLE = "-fx-border-color:lightgrey";
    private final WeakPropertyAdapter propAdapter;
    private TextModeToggleButton textModeToggle;
    private final WeakListAdapter listAdapter;
    private ScrollPane sp;

    public ListValueEditorSkin(ListValueEditor pSkinnable){
        super(pSkinnable);
        propAdapter = new WeakPropertyAdapter();
        listAdapter = new WeakListAdapter();
        getChildren().add(createLayout());
        refreshEditor();
        pSkinnable.getProperties().addListener(new MapChangeListener<Object, Object>() {
            @Override
            public void onChanged(MapChangeListener.Change<? extends Object, ? extends Object> change) {
                if(change.wasAdded()){
                    if(ListValueEditor.REFRESH_SEVERITY.equals(change.getKey())){
                        updateSeverityAsync();
                        getSkinnable().getProperties().remove(ListValueEditor.REFRESH_SEVERITY);
                    }
                }
            }
        });
        if(pSkinnable.getProperties().containsKey(ListValueEditor.REFRESH_SEVERITY)){
            updateSeverityAsync();
            getSkinnable().getProperties().remove(ListValueEditor.REFRESH_SEVERITY);
        }
    }
    
    public final Node createLayout(){
        
        HBox root = new HBox(5);
        root.setMinHeight(Region.USE_PREF_SIZE);
        root.setMaxHeight(Region.USE_PREF_SIZE);
        root.setAlignment(Pos.CENTER);
        root.setFillHeight(true);
        
        sp = new ScrollPane();
        sp.setSkin(new ScrollPaneSkin(sp));
        sp.setFitToHeight(true);
        sp.setFitToWidth(true);
        sp.setMinHeight(Region.USE_PREF_SIZE);
        sp.setMaxHeight(Region.USE_PREF_SIZE);
        sp.contentProperty().bind(editorProperty());
        sp.setMaxWidth(Double.MAX_VALUE);
        
        textModeToggle = new TextModeToggleButton();
        
        ScrollBar hBar = ScrollBarHelper.getScrollBar(sp, Orientation.HORIZONTAL);
        hBar.visibleProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                setScrollPaneHeight(getComputedHeight(t1));
            }
        });
        setScrollPaneHeight(getComputedHeight(hBar.isVisible()));
        HBox.setHgrow(sp, Priority.ALWAYS);
        
        textModeToggle.toggleProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                refreshEditor();
                setScrollPaneHeight(getComputedHeight(hBar!=null?hBar.isVisible():false));
            }
        });
        
        root.getChildren().addAll(sp,textModeToggle);
        return root;
    }
    private void setScrollPaneHeight(double pHeight){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                sp.setPrefHeight(pHeight);
            }
        });
    }
    public double getComputedHeight(boolean pBarShown){
        if(isTextMode()){
            return Region.USE_COMPUTED_SIZE;
        }
        ScrollBar bar = ScrollBarHelper.getScrollBar(sp, Orientation.HORIZONTAL);
        if(bar != null){
            return 34.0d+(pBarShown?14.0d:0.0d);
        }
        return Region.USE_COMPUTED_SIZE;
    }
    public boolean isTextMode(){
        return textModeToggle.isToggled();
    }
    private ObjectProperty<Node> editorProperty;
    public ObjectProperty<Node> editorProperty(){
        if(editorProperty == null){
            editorProperty = new SimpleObjectProperty<>();
        }
        return editorProperty;
    }
    public Node getEditor(){
        return editorProperty().get();
    }
    public void setEditor(Node pEditor){
        editorProperty().set(pEditor);
    }

    private void refreshEditor() {
        propAdapter.dispose();
        listAdapter.dispose();
        setEditor(isTextMode()?getTextFieldEditor():getItemEditor());
    }
    private Node getItemEditor(){
        HBox box = new HBox(5);
        box.setAlignment(Pos.CENTER_LEFT);
        addItems(box, getSkinnable().getValues());
        listAdapter.addChangeListener(getSkinnable().getValues(), new ListChangeListener() {
            @Override
            public void onChanged(ListChangeListener.Change change) {
                addItems(box, getSkinnable().getValues());
            }
        });
        updateSeverityAsync();
        return box;
    }
    private void addItems(HBox pBox,List<String> pItems){
        Objects.requireNonNull(pBox, "Itemcontainer can not be null!");
        pBox.getChildren().clear();
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
                pBox.getChildren().addAll(createItemNodes(pItems));
//            }
//        });
    }
    private List<Node> createItemNodes(List<String> pItems){
        List<Node> nodes = new ArrayList<>();
        nodes.add(createAddButton());
        nodes.addAll(createItems(pItems));
        return nodes;
    }
    private List<Item> createItems(List<String> pItems){
        pItems = Objects.requireNonNullElse(pItems, new ArrayList<>());
        return pItems.stream().map((t) -> {
            Item item = getSkinnable().getItemFactory().call(t);
            propAdapter.addChangeListener(item.editableProperty(), new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                    ScrollBar hBar = ScrollBarHelper.getScrollBar(sp, Orientation.HORIZONTAL);
                    setScrollPaneHeight(t1?48.0d:getComputedHeight(hBar!=null?hBar.isVisible():false));
                }
            });
            return item;
        }).collect(Collectors.toList());
    }
    private Node getTextFieldEditor(){
        TextField textfield = new TextField(getSkinnable().getValuesAsString());
        propAdapter.addChangeListener(textfield.focusedProperty(), new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if(!t1){
                    getSkinnable().getUpdateCallback().call(textfield.getText());
                }
            }
        });
        textfield.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if(KeyCode.ENTER.equals(t.getCode())){
                    getSkinnable().getUpdateCallback().call(textfield.getText());
                    t.consume();
                }
            }
        });
        listAdapter.addChangeListener(getSkinnable().getValues(), new ListChangeListener() {
            @Override
            public void onChanged(ListChangeListener.Change change) {
                textfield.setText(getSkinnable().getValuesAsString());
            }
        });
        HBox box = new HBox(textfield);
        HBox.setHgrow(textfield, Priority.ALWAYS);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }
    
    public final Button createAddButton() {
//        Button btn = new Button();
//        btn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        AddButton btn = new AddButton();
        btn.setTooltip(new CpxTooltip("Neuen Wert der Liste hinzufügen", 100, 5000, 100, true));
        btn.setMaxHeight(Double.MAX_VALUE);
//        btn.getStyleClass().add("cpx-icon-button");
        btn.setStyle(BORDER_STYLE);
//        btn.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.PLUS_CIRCLE).color(Color.GREEN));
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                getSkinnable().getValues().add(0,"");
//                Item item = getSkinnable().getItemFactory().call("");
//                getChildren().add(1, item);
//                get
            }
        });
        return btn;
    }
    protected final void updateSeverityAsync(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                updateSeverity();
            }
        });
    }
    protected void updateSeverity(){
        Region content = (Region) sp.getContent();
        for(Node child : content.getChildrenUnmodifiable()){
            if(child instanceof Item){
                updateSeverity((Item) child);
            }
        }
    }
    private void updateSeverity(Item item) {
        String code = item.getText().trim().replace("*", "%");
        boolean isMarked = getSkinnable().valdationMapContainsCode(code);
        item.setSeverity(isMarked?Severity.ERROR:Severity.OK);
        if(isMarked){
            RuleMessageIndicator indicator = new RuleMessageIndicator();
            MessageReasonEn reason = getSkinnable().getMessageReasonForCode(item.getText().trim());
            String msg = reason!=null?reason.toString():"No specific Reason found!";
            indicator.setTooltip(new CpxTooltip(msg, 100, 5000, 100, true));
            item.setGraphic(indicator);
        }else{
            item.setGraphic(null);
        }
    }
}
