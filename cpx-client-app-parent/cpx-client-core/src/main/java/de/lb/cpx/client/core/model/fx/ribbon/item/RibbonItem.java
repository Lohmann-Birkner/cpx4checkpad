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
package de.lb.cpx.client.core.model.fx.ribbon.item;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Item to be shown in the Ribbon as Item, implements Javafx tab
 *
 * @author wilde
 */
public class RibbonItem extends Tab {

    private static final String DEFAULT_STYLE_CLASS = "ribbon-item";
    private static final Double DEFAULT_SPACING = 10.0;
    private final ObservableList<RibbonItemGroup> items;
    private Pane contentBox;

    public RibbonItem() {
        super();
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        items = FXCollections.observableArrayList();
        orientationProperty().addListener(new ChangeListener<Orientation>() {
            @Override
            public void changed(ObservableValue<? extends Orientation> observable, Orientation oldValue, Orientation newValue) {
                setContent(initLayout());
            }
        });
        setContent(initLayout());
        items.addListener(new ListChangeListener<RibbonItemGroup>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends RibbonItemGroup> c) {
                if (c.next()) {
                    if (c.wasAdded()) {
                        addContent(new ArrayList<>(c.getAddedSubList()));
//                        contentBox.getChildren().addAll(c.getAddedSubList());
//                        for(RibbonItemGroup item : c.getAddedSubList()){
//                            VBox.setVgrow(item, Priority.ALWAYS);
//                        }
                    }
                    if (c.wasRemoved()) {
                        contentBox.getChildren().removeAll(c.getRemoved());
                    }
                }
            }
        });
//        items.addListener(new ListChangeListener<RibbonItemGroup>() {
//            @Override
//            public void onChanged(ListChangeListener.Change<? extends RibbonItemGroup> c) {
//                if(c.next()){
//                    if(c.wasAdded()){
//                        
//                    }
//                    if(c.wasRemoved()){
//                        
//                    }
//                }
//            }
//        });
//        Bindings.bindContent(contentBox.getChildren(), items);
    }

    /**
     * @return all groups currently stored in item
     */
    public ObservableList<RibbonItemGroup> getItems() {
        return items;
    }

    private VBox getVEdge() {
        VBox box = new VBox();
//        box.setPadding(new Insets(5, 0, 5, 0));
//        box.setSpacing(5.0);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private HBox getHEdge() {
        HBox box = new HBox();
//        box.setPadding(new Insets(0, 5, 0, 5));
//        box.setSpacing(5.0);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private void addContent(List<RibbonItemGroup> groups) {
        for (RibbonItemGroup g : groups) {
            FlowPane pane = new FlowPane();
            pane.getChildren().addAll(g.getItems());
            pane.setAlignment(Pos.CENTER);
            pane.setVgap(5.0);
            pane.setHgap(5.0);
//            pane.setStyle("-fx-background-color:red");

            BorderPane borderPane = new BorderPane(pane, getVEdge(), getHEdge(), getVEdge(), getHEdge());
            switch (getOrientation()) {
                case HORIZONTAL:
                    Separator vsep = new Separator(Orientation.VERTICAL);
                    vsep.setPadding(new Insets(0, 0, 0, 10));
                    ((Pane) borderPane.getRight()).getChildren().add(vsep);
                    break;
                case VERTICAL:
                    Separator hsep = new Separator(Orientation.HORIZONTAL);
                    hsep.setPadding(new Insets(10, 0, 0, 0));
                    ((Pane) borderPane.getBottom()).getChildren().add(hsep);
                    break;
            }
            if (g.isShowTitle()) {
                Label title = new Label(g.getTitle());
                BorderPane.setAlignment(title, Pos.CENTER);
                switch (g.getTitleSide()) {
                    case TOP:
                        ((Pane) borderPane.getTop()).getChildren().add(0, new Group(title));
                        break;
                    case RIGHT:
                        ((Pane) borderPane.getRight()).getChildren().add(0, new Group(title));
                        if (Orientation.VERTICAL.equals(getOrientation())) {
                            title.setRotate(-90.0);
                        }
                        break;
                    case BOTTOM:
                        ((Pane) borderPane.getBottom()).getChildren().add(0, new Group(title));
                        break;
                    case LEFT:
                        ((Pane) borderPane.getLeft()).getChildren().add(0, new Group(title));
                        if (Orientation.VERTICAL.equals(getOrientation())) {
                            title.setRotate(-90.0);
                        }
                        break;
                }
            }
            contentBox.getChildren().add(borderPane);
        }
    }

    private Parent initLayout() {
        contentBox = getLayout();
        addContent(items);
//        contentBox.getChildren().addAll(items);
        return contentBox;
    }

    private Pane getLayout() {
        switch (getOrientation()) {
            case HORIZONTAL:
                return getHorizontalLayout();
            case VERTICAL:
                return getVerticalLayout();
            default:
                HBox box = new HBox(new Label("Inhalte konnten nicht angezeigt werden."));
                box.setAlignment(Pos.CENTER);
                return box;
        }
    }

    private HBox getHorizontalLayout() {
        HBox box = new HBox();
        box.setSpacing(DEFAULT_SPACING);
        box.setFillHeight(true);
        return box;
    }

    private VBox getVerticalLayout() {
        VBox box = new VBox();
//        box.setStyle("-fx-background-color:red");
        box.setSpacing(DEFAULT_SPACING);
        box.setFillWidth(true);
        return box;
    }
    //orientation of the content
    private ObjectProperty<Orientation> orientationProperty;

    /**
     * @return orientation property
     */
    public final ObjectProperty<Orientation> orientationProperty() {
        if (orientationProperty == null) {
            orientationProperty = new SimpleObjectProperty<>(Orientation.HORIZONTAL);
        }
        return orientationProperty;
    }

    /**
     * @return current orientation of the item
     */
    public Orientation getOrientation() {
        return orientationProperty().get();
    }

    /**
     * @param pOrientation set new orientation
     */
    public void setOrientation(Orientation pOrientation) {
        orientationProperty().set(pOrientation);
    }
}
