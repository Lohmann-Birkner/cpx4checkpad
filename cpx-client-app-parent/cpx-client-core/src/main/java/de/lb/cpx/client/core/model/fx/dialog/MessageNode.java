/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.core.model.fx.dialog;

import de.lb.cpx.client.core.model.enums.CpxErrorTypeEn;
import de.lb.cpx.client.core.util.ResourceLoader;
import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

/**
 *
 * @author gerschmann
 */
public class MessageNode extends VBox {

    private final Glyph icon;

    public MessageNode() {
        super(5);
        setPadding(new Insets(10));
        setAlignment(Pos.CENTER);
        Label msg = new Label();
        icon = ResourceLoader.getGlyph(FontAwesome.Glyph.EXCLAMATION_TRIANGLE);
        msg.textProperty().bind(messageTextProperty());
        updateStyleForType(CpxErrorTypeEn.INFO);
        messageTypeProperty().addListener(new ChangeListener<CpxErrorTypeEn>() {
            @Override
            public void changed(ObservableValue<? extends CpxErrorTypeEn> ov, CpxErrorTypeEn t, CpxErrorTypeEn t1) {
                updateStyleForType(t1);
            }
        });
        HBox container = new HBox(5, icon, msg);
//        container.setAlignment(Pos.CENTER);
        container.alignmentProperty().bind(alignmentProperty());
        container.setMinHeight(HBox.USE_PREF_SIZE);
        getChildren().add(container);
    }

    private void updateStyleForType(CpxErrorTypeEn t1) {
        t1 = Objects.requireNonNullElse(t1, CpxErrorTypeEn.INFO);
        setStyle(new StringBuilder("-fx-background-color:").append(t1.getBackgroundColor()).append(";")
                .append("-fx-background-radius:10;").toString());
        icon.setStyle(new StringBuilder("-fx-text-fill:").append(t1.getIconColor()).append(";").toString());
    }
    private StringProperty messageTextProperty;

    public final StringProperty messageTextProperty() {
        if (messageTextProperty == null) {
            messageTextProperty = new SimpleStringProperty();
        }
        return messageTextProperty;
    }

    public String getMessageText() {
        return messageTextProperty().get();
    }

    public void setMessageText(String pMessage) {
        messageTextProperty().set(pMessage);
    }

    private ObjectProperty<CpxErrorTypeEn> messageTypeProperty;

    public final ObjectProperty<CpxErrorTypeEn> messageTypeProperty() {
        if (messageTypeProperty == null) {
            messageTypeProperty = new SimpleObjectProperty<>(CpxErrorTypeEn.INFO);
        }
        return messageTypeProperty;
    }

    public CpxErrorTypeEn getMessageType() {
        return messageTypeProperty().get();
    }

    public void setMessageType(CpxErrorTypeEn pType) {
        messageTypeProperty().set(Objects.requireNonNullElse(pType, CpxErrorTypeEn.INFO));
    }
}
