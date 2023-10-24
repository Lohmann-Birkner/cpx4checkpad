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
package de.lb.cpx.ruleviewer.skin;

import de.lb.cpx.ruleviewer.dragdrop.format.DndFormat;
import de.lb.cpx.ruleviewer.model.DragEndArea;
import static de.lb.cpx.ruleviewer.model.DragEndArea.SELECTED_NODE_PSEUDO_CLASS;
import de.lb.cpx.ruleviewer.util.SeverityEn;
import java.io.IOException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author wilde
 */
public class DragEndAreaSkin extends SkinBase<DragEndArea> {

    private static final String DEFAULT_BORDER_STYLE = "";
    private static final String ERROR_BORDER_STYLE = "-fx-border-style: solid solid solid solid;\n"
            + "    -fx-border-width: 2 2 2 2 ;\n"
            + "    -fx-border-color: transparent transparent red transparent;";
    private static final String WARNING_BORDER_STYLE = "-fx-border-style: solid solid solid solid;\n"
            + "    -fx-border-width: 2 2 2 2 ;\n"
            + "    -fx-border-color: transparent transparent yellow transparent;";
    private static final String INFO_BORDER_STYLE = "-fx-border-style: solid solid solid solid;\n"
            + "    -fx-border-width: 2 2 2 2 ;\n"
            + "    -fx-border-color: transparent transparent skyblue transparent;";

    private Parent root;
    private Label placeholder;
    private AnchorPane apContent;

    public DragEndAreaSkin(DragEndArea pSkinnable) throws IOException {
        super(pSkinnable);
        loadRoot();
        getChildren().add(root);
        //not null safe
        registerPane(apContent, pSkinnable.getDataFormat());
        pSkinnable.dataFormatProperty().addListener(new ChangeListener<DataFormat>() {
            @Override
            public void changed(ObservableValue<? extends DataFormat> observable, DataFormat oldValue, DataFormat newValue) {
                registerPane(apContent, newValue);
            }
        });
        pSkinnable.severityLevelProperty().addListener(new ChangeListener<SeverityEn>() {
            @Override
            public void changed(ObservableValue<? extends SeverityEn> observable, SeverityEn oldValue, SeverityEn newValue) {
                changeSeverity(newValue);
            }
        });
        changeSeverity(getSkinnable().getSeverityLevel());
        //placeholder stuff
        //enough for now, maybe cool to set a node as new placeholder and replace old?
        if (placeholder instanceof Label) {
            placeholder.textProperty().bind(pSkinnable.placeholderTextProperty());
        }

        placeholder.textProperty().bind(pSkinnable.placeholderTextProperty());
    }

    private void registerPane(AnchorPane pNode, DataFormat pFormat) {
        //Disabled DnD
        //should be more unified
//        DragDropHelper.deregisterDragDrop(pNode);
//        if(pFormat == null){
//            LOG.warning("No Valid DataFormat!");
//            return;
//        }
//        DragDropHelper.registerDragDropEnd(new Callback<Object, Boolean>() {
//            @Override
//            public Boolean call(Object param) {
//                SelectableControl target = (SelectableControl) param;
//                if (!pNode.getChildren().contains(placeholder)) {
//                    return false;
//                }
//                getSkinnable().getOnDropDetected().call(target);
//
//                return true;
//            }
//        }, pNode, pFormat);
        //        DragDropHelper.deregisterDragDrop(pNode);
//        if(pFormat == null){
//            LOG.warning("No Valid DataFormat!");
//            return;
//        }
        pNode.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                //abort drag and drop if content did no match
                if (!getSkinnable().checkDragEvent(event)) {
//                    event.setDropCompleted(false);
                    event.consume();
                    return;
                }
                Dragboard board = event.getDragboard();
                if (board.hasContent(DndFormat.ADD_FORMAT)) {
                    getSkinnable().getOnDropDetected().call(event);//board.getContent(DndFormat.ADD_FORMAT));
                }
                if (board.hasContent(DndFormat.REORDER_FORMAT)) {
                    getSkinnable().getOnDropDetected().call(event);//board.getContent(DndFormat.REORDER_FORMAT));
                }
            }
        });
        pNode.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                boolean accept = getSkinnable().checkDragEvent(event);
                if (accept) {
                    pNode.pseudoClassStateChanged(SELECTED_NODE_PSEUDO_CLASS, true);
                    event.acceptTransferModes(TransferMode.ANY);
                } else {
//                    event.setDropCompleted(false);
                    event.consume();
                }
            }
        });
        pNode.setOnDragExited(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                pNode.pseudoClassStateChanged(SELECTED_NODE_PSEUDO_CLASS, false);
            }
        });

    }

    public void loadRoot() throws IOException {
        root = FXMLLoader.load(getClass().getResource("/fxml/DragEndArea.fxml"));
        placeholder = (Label) root.lookup("#placeholder");
        apContent = (AnchorPane) root;//root.lookup("apContent");
        apContent.getStyleClass().add("content");
    }

    private void changeSeverity(SeverityEn newValue) {
        if (newValue == null) {
            placeholder.setStyle(DEFAULT_BORDER_STYLE);
            return;
        }
        switch (newValue) {
            case ERROR:
                placeholder.setStyle(ERROR_BORDER_STYLE);
                break;
            case WARNING:
                placeholder.setStyle(WARNING_BORDER_STYLE);
                break;
            case INFORMATION:
                placeholder.setStyle(INFO_BORDER_STYLE);
                break;
            default:
                placeholder.setStyle(DEFAULT_BORDER_STYLE);
        }
    }
}
