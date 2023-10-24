/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.ruleviewer.control;

import de.lb.cpx.client.core.model.fx.button.EditCommentButton;
import de.lb.cpx.client.core.model.fx.pane.AsyncPane;
import java.io.IOException;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import org.controlsfx.control.PopOver;

/**
 *
 * @author gerschmann
 */
public class InfoViewSkin extends SkinBase<InfoView> {

    private static final String DEFAULT_COMMENT_HEADER_TEXT = "Kommentar:";
    private static final String DEFAULT_CONTENT_HEADER_TEXT = "Inhalt:";
    private static final String DEFAULT_NO_COMMENT = "Kein Kommentar";
    private HBox boxComment;

    public InfoViewSkin(InfoView c) throws IOException {
        super(c);
        getChildren().add(createRoot());
    }

    private Parent createRoot() throws IOException {
        AnchorPane layout = FXMLLoader.load(getClass().getResource("/fxml/TableInfoView.fxml"));
        boxComment = (HBox) layout.lookup("#boxComment");
        EditCommentButton commentButton = new EditCommentButton(PopOver.ArrowLocation.TOP_LEFT);
        commentButton.setComment(getSkinnable().getComment());
        commentButton.isEditable(false);
        commentButton.setOnUpdateComment(getSkinnable().getOnSaveComment());
        boxComment.getChildren().add(commentButton);

        ScrollPane spTableContent = (ScrollPane) layout.lookup("#spTableContent");
        Label lblTableNote = (Label) layout.lookup("#lblTableNote");
        lblTableNote.setText(DEFAULT_COMMENT_HEADER_TEXT);
        Label lblTableContent = (Label) layout.lookup("#lblTableContent");
        lblTableContent.setText(DEFAULT_CONTENT_HEADER_TEXT);
        Label lblTableComment = (Label) layout.lookup("#lblTableComment");

        lblTableComment.textProperty().bind(Bindings.when(getSkinnable().commentProperty().isEmpty()).then(DEFAULT_NO_COMMENT).otherwise(getSkinnable().getComment()));
        lblTableNote.textProperty().bind(Bindings.when(getSkinnable().commentHeaderProperty().isEmpty()).then(DEFAULT_COMMENT_HEADER_TEXT).otherwise(getSkinnable().getHeaderComment()));
        lblTableContent.textProperty().bind(Bindings.when(getSkinnable().contentHeaderProperty().isEmpty()).then(DEFAULT_CONTENT_HEADER_TEXT).otherwise(getSkinnable().getHeaderContent()));

//        lblTableComment.textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
////                handleCommentChange(t1);
//            }
//        });
//        handleCommentChange(getSkinnable().getComment());
//        lblTableComment.textProperty().bind(getSkinnable().commentProperty());
//        lblTableComment.setText(getTableComment());
        AsyncPane<FlowPane> pane = getSkinnable().getInfoPane();
        pane.setPrefHeight(100);
        spTableContent.setContent(pane);

        return layout;
    }

//    private void handleCommentChange(String pComment){
//        if(pComment == null || pComment.isEmpty()){
//            if(boxComment.getChildren().contains(boxCommentMenu)){
//                boxComment.getChildren().remove(boxCommentMenu);
//            }
//        }else{
//            if(!boxComment.getChildren().contains(boxCommentMenu)){
//                boxComment.getChildren().add(boxCommentMenu);
//            }
//        }
//    }
}
