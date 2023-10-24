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
package de.lb.cpx.client.core.menu.fx.openedEntry;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Skin class for openEntry
 *
 * @author wilde
 */
public class OpenEntrySkin extends SkinBase<OpenEntry<?, ?>> {

    private Label title;
    private Button actionBtn;
    private Label desc;
    private Label graphic;
    private Label indicator;
    private HBox boxTitle;

    public OpenEntrySkin(OpenEntry<?, ?> pSkinnable) {
        super(pSkinnable);
        getChildren().add(loadRoot());

        title.textProperty().bind(pSkinnable.titleProperty());
        desc.textProperty().bind(pSkinnable.descProperty());
        graphic.graphicProperty().bind(pSkinnable.graphicProperty());
        actionBtn.onActionProperty().bind(pSkinnable.onCloseProperty());

        pSkinnable.unsavedContentProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                handleUnSavedContent(t1);
            }
        });
        pSkinnable.erroneousProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                handleErroneous(t1);
            }
        });
        pSkinnable.errorGraphicProperty().addListener(new ChangeListener<Node>() {
            @Override
            public void changed(ObservableValue<? extends Node> ov, Node t, Node t1) {
                handleErroneous(getSkinnable().isErroneous());
            }
        });
        handleUnSavedContent(pSkinnable.hasUnSavedContent());
        handleErroneous(pSkinnable.isErroneous());
    }
    private static final PseudoClass UNSAVED_PSEUDO_CLASS = PseudoClass.getPseudoClass("unsaved");

    private void handleUnSavedContent(boolean pUnsaved) {
        if (pUnsaved) {
            if (!boxTitle.getChildren().contains(indicator)) {
                boxTitle.getChildren().add(0, indicator);
            }
        } else {
            boxTitle.getChildren().remove(indicator);
        }
        title.pseudoClassStateChanged(UNSAVED_PSEUDO_CLASS, pUnsaved);
    }
     private void handleErroneous(Boolean pIsErroneous) {
        if(title == null){
            return;
        }
        title.setGraphic(pIsErroneous?getSkinnable().getErrorGraphic():null);
        title.pseudoClassStateChanged(PseudoClass.getPseudoClass("error"), pIsErroneous);
        
    }
    private Parent loadRoot() {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource("/fxml/OpenedEntryFXML.fxml"));
//        title = (Label) parent.lookup("#labelHeader");//new Label();
//        title.getStyleClass().add("open-entry-label");
//        indicator = new Label("*");
//        indicator.getStyleClass().add("open-entry-label");
//        desc = new Label();
//        graphic = new Label();
////        title.setText("test titel " + getSkinnable().getEntity().id);
//        actionBtn = new Button();
//        actionBtn.setGraphic(ResourceLoader.getGlyph("\uf00d"));
//        boxTitle = new HBox(2, title);
////        boxTitle.getStyleClass().add("open-entry-title-container");
//        boxTitle.setFillHeight(true);
//        boxTitle.setAlignment(Pos.CENTER_LEFT);
//
//        VBox boxText = new VBox(5, boxTitle/*title*/, desc);
//        boxText.setAlignment(Pos.CENTER_LEFT);
//        boxText.setPadding(new Insets(0, 0, 0, 5));
//        HBox.setHgrow(boxText, Priority.ALWAYS);
//        HBox content = new HBox(5, graphic, boxText, actionBtn);
//        content.getStyleClass().add("opened-entry");
//        return content;
        } catch (IOException ex) {
            Logger.getLogger(OpenEntrySkin.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(parent == null){
            return new HBox(new Label("could not load parent"));
        }
        title = (Label) parent.lookup("#labelHeader");//new Label();
        indicator = new Label("*");
        indicator.getStyleClass().add("open-entry-label");
        
        desc = (Label) parent.lookup("#labelDescription");
        graphic = (Label) parent.lookup("#labelIcon");
        actionBtn = (Button) parent.lookup("#buttonClose");
        ((HBox)parent.getChildrenUnmodifiable().get(0)).getChildren().get(1);
        boxTitle = (HBox) parent.lookup("#boxTitle");
        VBox boxContent = (VBox) parent.lookup("#boxContent");
        if(boxContent != null){
            boxContent.setPrefWidth(VBox.USE_COMPUTED_SIZE);
        }
        return parent;
    }
}
