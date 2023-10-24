/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.tab;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

/**
 * BaseClass of Tabs in Cpx, contains in Header an Image, and two Labels
 *
 * @author wilde
 */
public abstract class TwoLineTab extends Tab {

    private final ImageView imageView;
    private final Label labelTitle;
    private final Label labelDesc;
    private final HBox root;
    private boolean isClosed = true;
    protected static final String DEFAULT_STYLE_CLASS = "two-line-tab";
    private ChangeListener<Image> IMAGE_LISTENER = new ChangeListener<Image>() {
        @Override
        public void changed(ObservableValue<? extends Image> observable, Image oldValue, Image newValue) {
            if(imageView == null){
                //should not happening
                return;
            }
            if (newValue != null) {
                imageView.setFitHeight(16.0);
                imageView.setFitWidth(16.0);
            } else {
                imageView.setFitHeight(0);
                imageView.setFitWidth(0);
            }
        }
    };

    public TwoLineTab() {
        this("/img/Add-16.png", "title", "desc");
    }

    public TwoLineTab(String imgPath, String title, String description) {
        super();
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        root = new HBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(5.0);
        imageView = new ImageView(new Image(getClass().getResourceAsStream(imgPath)));
        imageView.imageProperty().addListener(IMAGE_LISTENER);
        imageView.setFitHeight(0);
        imageView.setFitWidth(0);
        VBox vBox = new VBox();
        labelTitle = new Label(title);
        labelDesc = new Label(description);
        labelDesc.getStyleClass().add("tab-detail-label");
        vBox.getChildren().addAll(labelTitle, labelDesc);
        root.getChildren().addAll(imageView, vBox);
        setGraphic(root);
        isClosed = false;
        setOnClosed(new EventHandler<Event>() {
            @Override
            public void handle(Event t) {
                close();
            }
        });
    }

    public void setImage(Image image) {
        if (root.getChildren().contains(imageView)) {
            imageView.setImage(image);
        }
    }

    public void setImage(String imagePath) {
        setImage(new Image(getClass().getResourceAsStream(imagePath)));
    }

    /**
     * example for String to set f0c9; menu - f013; gear Glyph glyph =
     * fontAwesome.create('\uf013');
     *
     * @param glyph string representation of the glyph in the font
     */
    public final void setGlyph(String glyph) {
        GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");
        //f0c9; menu - f013; gear
        //Glyph glyph = fontAwesome.create('\uf013');//.size(28).color(Color.RED);
        setGlyph(fontAwesome.create(glyph).size(20));
    }

    public final void setGlyph(FontAwesome.Glyph pGlyph) {
        setGlyph(new Glyph("FontAwesome", pGlyph));
    }

    /**
     * @param glyph set glyph as image
     */
    public final void setGlyph(Glyph glyph) {
        glyph.size(20);
        Label glyphLabel = new Label();
        glyphLabel.setGraphic(glyph);
        root.getChildren().remove(imageView);
        root.getChildren().add(0, glyph);
    }

    public final void setTitle(String title) {
        //CPX-1592: Fix issue where title of the tab is not displayed in overrun region of tabpane, when size of tab headers exceeded space of tab header region
        //display could be duplicated, but styleclass two-line-tab should disable this behavior in css, because tab-label is set to display graphic only
        setText(title);
        labelTitle.setText(title);
    }

    public final void setDescription(String description) {
        labelDesc.setText(description);
    }

    public void setContent(Image image, String title, String description) {
        setImage(image);
        setTitle(title);
        setDescription(description);
    }

    public void setContent(String glyph, String title, String description) {
        setGlyph(glyph);
        setTitle(title);
        setDescription(description);
    }

    public void setHeight(double prefHeight) {
        root.setMinHeight(prefHeight);
    }

    public abstract TabType getTabType();

    public void reload() {

    }

    public boolean isClosed() {
        return isClosed;
    }

    public void close() {
        if(imageView != null){
            if(IMAGE_LISTENER != null){
                imageView.imageProperty().removeListener(IMAGE_LISTENER);
            }
            IMAGE_LISTENER = null;
            imageView.setImage(null);
        }
        root.getChildren().clear();
        setOnClosed(null);
        isClosed = true;
    }
    
    public String getTitle() {
        return labelTitle.getText();
    }

    public String getDescription() {
        return labelDesc.getText();
    }

    public enum TabType {                         //ProcessOverview
        DOCUMENT, PATIENT, HISTORY, CASE, ASSIGNMENT, POVERVIEW, PROCESSCOMPLETION, JOBBATCHGROUPING, JOBCASEMERGING_DRG, JOBCASEMERGING_PEPP, CASEMERGING
    }
}
