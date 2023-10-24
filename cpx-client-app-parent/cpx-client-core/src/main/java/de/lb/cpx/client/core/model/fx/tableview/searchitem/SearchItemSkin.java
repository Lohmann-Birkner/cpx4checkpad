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
package de.lb.cpx.client.core.model.fx.tableview.searchitem;

import de.lb.cpx.client.core.model.fx.tooltip.BasicTooltip;
import de.lb.cpx.client.core.util.ResourceLoader;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.skin.ButtonSkin;
import org.controlsfx.glyphfont.FontAwesome;

/**
 * Search item skin object Handles layout and user input to close button
 *
 * @author wilde
 */
public class SearchItemSkin extends ButtonSkin {

    private Button btnClose;
    private static final Double FINAL_MAX_WIDTH = 200.0d;

    public SearchItemSkin(SearchItem pSearchItem) {
        super(pSearchItem);
        pSearchItem.getStyleClass().add("filter-button");
        pSearchItem.setMinWidth(Button.USE_PREF_SIZE);
        pSearchItem.setMaxWidth(Button.USE_PREF_SIZE);
        //check on width change if may width is exceeded
        //when it is, tooltip is set and pref size is set to 200
        pSearchItem.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.doubleValue() > FINAL_MAX_WIDTH) {
                    if (Double.doubleToRawLongBits(pSearchItem.getPrefHeight()) != Double.doubleToRawLongBits(FINAL_MAX_WIDTH)) {
                        pSearchItem.setPrefWidth(FINAL_MAX_WIDTH);
                        pSearchItem.setTooltip(new BasicTooltip("", pSearchItem.getText()));
                    }
                }
            }
        });
        btnClose = new Button();
        btnClose.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.CLOSE));
        //fire close event
        btnClose.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (pSearchItem.getOnCloseEvent() != null) {
                    pSearchItem.getOnCloseEvent().handle(event);
                    if (pSearchItem.getTooltip() != null) {
                        pSearchItem.getTooltip().hide();

                    }
                }
            }
        });
        //set close button async to avoid render problems
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                pSearchItem.setGraphic(btnClose);
            }
        });
    }
//
//    @Override
//    protected void updateChildren() {
//        super.updateChildren();
////        btnClose = new Button();
////        btnClose.setOnAction(new EventHandler<ActionEvent>() {
////            @Override
////            public void handle(ActionEvent event) {
////                if(((SearchItem)getSkinnable()).getOnCloseEvent() != null){
////                    ((SearchItem)getSkinnable()).getOnCloseEvent().handle(event);
////                }
////            }
////        });
////        getChildren().add(0, btnClose);
//    }

}
