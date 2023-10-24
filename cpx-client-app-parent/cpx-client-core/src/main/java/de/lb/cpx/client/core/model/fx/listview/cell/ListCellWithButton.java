/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.core.model.fx.listview.cell;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import static javafx.scene.layout.Region.USE_PREF_SIZE;

/**
 *
 * @author gerschmann
 * @param <T>
 */
public class ListCellWithButton<T> extends ListCell<T>{
    private static final Double DEFAULT_WIDTH_MENU_BTN = 30.0;
    protected final Label title;
    protected final HBox graphic;
    protected final HBox menu;
    protected final List<Node> menuItems;
    protected final HBox checks;
    
    public ListCellWithButton(){
        super();
        menuItems = new ArrayList<>();
        this.title = new Label();
        title.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(title, Priority.ALWAYS);
        this.menu = new HBox();
        this.checks = new HBox(5);
        checks.setAlignment(Pos.CENTER_LEFT);
        checks.setMinWidth(USE_PREF_SIZE);
        checks.setMaxWidth(USE_PREF_SIZE);
        checks.setFillHeight(true);
        menu.setMinWidth(USE_PREF_SIZE);
        menu.setMaxWidth(USE_PREF_SIZE);
        this.graphic = new HBox(5,title,checks,menu);
        graphic.setAlignment(Pos.CENTER_LEFT);
    }
    
    
    public final void setMenuItems(Node... pItems){
        setMenuItems(Lists.newArrayList(pItems));
    }
    public final void setMenuItems(List<Node> pItems){
        pItems = Objects.requireNonNullElse(pItems, new ArrayList<>());
        menuItems.clear();
        menuItems.addAll(pItems);
        updateMenuItems();
    }
    protected final void updateMenuItems(List<Node> pItems){
        pItems = Objects.requireNonNullElse(pItems, new ArrayList<>());
        if(!menu.getChildren().containsAll(pItems)){
            menu.getChildren().setAll(pItems);
            menu.setPrefWidth(DEFAULT_WIDTH_MENU_BTN*pItems.size());
        }
    }
    protected final void updateMenuItems(){
        updateMenuItems(menuItems);
    }
}
