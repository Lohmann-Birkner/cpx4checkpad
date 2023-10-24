/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.core.model.fx.button;

import de.lb.cpx.client.core.model.fx.labeled.LabeledTextArea;
import de.lb.cpx.client.core.model.fx.popover.AutoFitPopOver;
import de.lb.cpx.shared.lang.Lang;
import java.util.Objects;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;
import org.controlsfx.glyphfont.FontAwesome;

/**
 *
 * @author gerschmann
 */
public class EditCommentButton extends GlyphIconButton {

    private LabeledTextArea taComment;

    public EditCommentButton() {
        this(PopOver.ArrowLocation.BOTTOM_CENTER);
    }

    public EditCommentButton(PopOver.ArrowLocation pLocation) {
        super(FontAwesome.Glyph.EDIT);
        init(pLocation);
    }

    private void init(PopOver.ArrowLocation pLocation) {

        taComment = new LabeledTextArea(Lang.getComment(), 700);

        taComment.getControl().focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            //ignore focus gain
            if (newValue) {
                return;
            }
            if (!Objects.requireNonNullElse(taComment.getText(),"").equals(getComment()) && getOnUpdateComment() != null) {
                getOnUpdateComment().call(taComment.getText());
            }
        });
        taComment.setPadding(new Insets(12.0, 12.0, 12.0, 12.0));
        PopOver popover = new AutoFitPopOver();
        popover.setHideOnEscape(true);
        popover.setAutoHide(true);
        popover.setArrowLocation(pLocation);
        popover.setDetachable(false);
        popover.setContentNode(taComment);
        popover.getContentNode().setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                popover.hide(Duration.ZERO);

            }
        });

        addEventHandler(ActionEvent.ANY, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (!popover.isShowing()) {
                    taComment.setText(getComment());
                    taComment.setWrapText(true);
                    taComment.setEditable(isEditable());
                    popover.show(EditCommentButton.this);
                } else {
                    popover.hide(Duration.ZERO);
                }
                t.consume();
            }
        });

    }

    private Callback<String, Boolean> onUpdateComment;

    public void setOnUpdateComment(Callback<String, Boolean> pOnUpdate) {
        onUpdateComment = pOnUpdate;
    }

    public Callback<String, Boolean> getOnUpdateComment() {
        return onUpdateComment;
    }

    private StringProperty commentProperty;

    public StringProperty commentProperty() {
        if (commentProperty == null) {
            commentProperty = new SimpleStringProperty();
        }
        return commentProperty;
    }

    public void setComment(String pComment) {
        commentProperty().set(pComment);
    }

    public String getComment() {
        return commentProperty().get();
    }

    private BooleanProperty editableProperty;

    public BooleanProperty editableProperty() {
        if (editableProperty == null) {
            editableProperty = new SimpleBooleanProperty();
            editableProperty.set(false);
        }
        return editableProperty;
    }

    public void isEditable(Boolean isEditable) {
        editableProperty().set(isEditable);
    }

    public boolean isEditable() {
        return editableProperty().get();
    }

}
