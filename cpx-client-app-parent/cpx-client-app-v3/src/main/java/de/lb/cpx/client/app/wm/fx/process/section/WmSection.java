/*
 * Copyright (c) 2019 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner.
 * http://www.lohmann-birkner.de/de/index.php
 *
 * Contributors:
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.process.section;

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.process.section.details.WmDetailSection;
import de.lb.cpx.client.app.wm.fx.process.section.details.WmDetails;
import de.lb.cpx.client.app.wm.fx.process.section.operations.ItemEventHandler;
import de.lb.cpx.client.app.wm.fx.process.section.operations.WmOperations;
import de.lb.cpx.client.core.model.fx.popover.AutoFitPopOver;
import de.lb.cpx.client.core.model.fx.sectiontitle.SectionTitle;
import de.lb.cpx.client.core.model.fx.tooltip.CpxTooltip;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.controlsfx.glyphfont.Glyph;

/**
 *
 * @author niemeier
 * @param <E> type
 */
public abstract class WmSection<E extends AbstractEntity> extends SectionTitle {

    protected final ProcessServiceFacade facade;

    public WmSection(ProcessServiceFacade pFacade) {
        this(null, pFacade);
    }

    public WmSection(final String pTitle, final ProcessServiceFacade pFacade) {
        super(pTitle);
        this.facade = pFacade;
        registerPropertyListener(menuItemThreshold(), new ChangeListener<Integer>(){
            @Override
            public void changed(ObservableValue<? extends Integer> ov, Integer t, Integer t1) {
                getSkin().clearMenu();
                setMenu();
            }
        });
    }

    public long getProcessNumber() {
        return facade == null ? 0L : facade.getCurrentProcessNumber();
    }

    public ProcessServiceFacade getFacade() {
        return facade;
    }

    public abstract WmDetails<E> getDetails();

    @Override
    public Parent getDetailContent() {
        WmDetails<E> details = getDetails();
        if (details == null) {
            return null;
        }
        WmDetailSection detailSection = details.getDetailSection();
        return detailSection.getRoot();
    }

    public abstract E getSelectedItem();

    public void createItem() {
        ItemEventHandler eh = getOperations().createItem();
        if (eh != null) {
            eh.handle(null);
            reload();
        }
    }

    public void editItem(E item) {
        ItemEventHandler eh = getOperations().editItem(item);
        if (eh != null) {
            eh.handle(null);
            reload();
        }
    }

    public void removeItem(E item) {
        ItemEventHandler eh = getOperations().removeItem(item);
        if (eh != null) {
            eh.handle(null);
            reload();
        }
    }

    public abstract WmOperations<E> getOperations();
    
    @Override
    public final void setMenu(){
        if(getMenuItems().isEmpty()){
            return;
        }
        Node menu = createMenuStructure(getMenuItems());
        getSkin().setMenu(menu);
    }
    private Map<String,WmSectionMenuItem> menuItems;
    protected Map<String,WmSectionMenuItem> createMenuItems(){
        return new HashMap<>();
    }
    public Map<String,WmSectionMenuItem> getMenuItems(){
        if(menuItems == null){
            menuItems = createMenuItems();
        }
        return menuItems;
    }
    
    protected Pane createMenuStructure(Map<String,WmSectionMenuItem> pItems){
        List<Button> shownItems = new ArrayList<>();
        List<Button> hiddenItems = new ArrayList<>();
        List<String> keys = pItems.keySet().stream().sorted().collect(Collectors.toList());
        ListIterator<String> it = keys.listIterator();
        while (it.hasNext()) {
            int idx = it.nextIndex();
            String next = it.next();
            WmSectionMenuItem item = pItems.get(next);
            if(idx < getMenuItemThreshold()){
                Button btn = createButtonForMenu(item, ContentDisplay.GRAPHIC_ONLY);
                if(btn != null){
                    btn.setTooltip(new CpxTooltip(item.getText()));
                    btn.getStyleClass().add("cpx-icon-button");
                    shownItems.add(btn);
                }
            }else{
                hiddenItems.add(createButtonForMenu(item));
            }
        }
        HBox structure = new HBox();
        structure.setAlignment(Pos.CENTER);
        if(!shownItems.isEmpty()){
            structure.getChildren().addAll(shownItems);
        }
        if(!hiddenItems.isEmpty()){
            VBox menuBox = new VBox();
            menuBox.setAlignment(hiddenItems.size()==1?Pos.CENTER:Pos.TOP_CENTER);
            menuBox.getChildren().addAll(hiddenItems);
            AutoFitPopOver menuOver = new AutoFitPopOver();
            menuOver.setFitOrientation(Orientation.HORIZONTAL);
            menuOver.setArrowLocation(menuOver.getAdjustedLocation());
            menuOver.setContentNode(menuBox);

            Button menu = new Button();
            menu.setTooltip(new CpxTooltip("Weitere Menüpunkte"));
            menu.getStyleClass().add("cpx-icon-button");
            menu.setGraphic(getSkin().getGlyph("\uf142"));
            menu.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    menuOver.show(menu);
                }
            });
            structure.getChildren().add(menu);
        }
        return structure;
    }
    
    private IntegerProperty menuItemThreshold;
    public final IntegerProperty menuItemThreshold(){
        if(menuItemThreshold == null){
            menuItemThreshold = new SimpleIntegerProperty(0);
        }
        return menuItemThreshold;
    }
    public Integer getMenuItemThreshold(){
        return menuItemThreshold().get();
    }
    public void setMenuItemThreshold(Integer pThreshold){
        menuItemThreshold().set(Objects.requireNonNullElse(pThreshold, 0));
    }
    private Button createButtonForMenu(WmSectionMenuItem pItem){
        return createButtonForMenu(pItem, ContentDisplay.TEXT_ONLY);
    }
    private Button createButtonForMenu(WmSectionMenuItem pItem,ContentDisplay pContentDisplay){
        if(pItem == null){
            return null;
        }
        String text = ContentDisplay.GRAPHIC_ONLY.equals(pContentDisplay)?"":pItem.getText();
        Glyph glyph = ContentDisplay.TEXT_ONLY.equals(pContentDisplay)?null:pItem.getIcon();
        Button btn = new Button(text, glyph);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setContentDisplay(pContentDisplay);
        btn.setOnMouseClicked(pItem.getHandler());
        return btn;
    }

}
