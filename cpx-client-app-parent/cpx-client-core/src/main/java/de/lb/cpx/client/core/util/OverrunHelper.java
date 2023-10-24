/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.util;

import de.lb.cpx.client.core.model.fx.popover.AutoFitPopOver;
import java.util.function.Predicate;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TableCell;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import org.controlsfx.control.PopOver;

/**
 * Overrun helper class detects if text is overrun in parent node adds callback
 * to handle overrun adds popover with button to show complete text
 *
 * @author wilde
 */
public class OverrunHelper {

    private static final Logger LOG = Logger.getLogger(OverrunHelper.class.getName());

    /**
     * sets an overrun call back to an node, its fired when width of the node
     * exceeds the parent width Importend Note: parent of the node should be of
     * the type Pane, otherwise overrun calculation with bounds in parent may
     * return a wrong result ToDO: Bugfixes, calculation seems unclean AWI
     * 20170224
     *
     * @param pNode node to add the callback, e.g. a label
     * @param pCallback callback that should be fired if overrun is detected
     * @param offset offset for compuation
     */
    public static void addOverrunCallback(Labeled pNode, Callback<Boolean, Void> pCallback, int offset) {
//        pNode.widthProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                if (pNode.getParent() == null) {
//                    return;
//                }
//
//                double widthInParent = 0.0;
//                if (pNode.getParent() instanceof Pane) {
//                    widthInParent = ((Region) pNode.getParent()).getWidth() - offset;
//                } else {
//                    widthInParent = pNode.getParent().getBoundsInLocal().getWidth() - offset;
//                }
//                double widthInLocal = newValue.doubleValue();
//                boolean isOverrun = widthInLocal >= widthInParent;
////                LOG.info("isOverun : " + isOverrun + " labelWidth " + widthInLocal 
////                        + " parent " + widthInParent);
//                Platform.runLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        pCallback.call(isOverrun);
//                    }
//                });
//            }
//        });
        //TODO: remove wrapping for label
        pNode.needsLayoutProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                //2019-02-15 AWi: Reduce time to determine if text is overrun, only when
                //layouts needs to be updateded, determine anew if text is overrun
                if (!newValue) {
                    return;
                }
                if (!(pNode instanceof Label)) {
                    return;
                }
//                String originalString = ((Label) pNode).getText();
//                Text textNode = (Text) pNode.lookup(".text"); // "text" is the style class of Text
//                String actualString = textNode.getText();

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        boolean isOverrun = isOverrun((Label) pNode);//!actualString.isEmpty() && !originalString.equals(actualString);
                        pCallback.call(isOverrun);
                    }
                });
            }
        });
    }

    public static boolean isOverrun(Label pLabel) {
        if (pLabel == null) {
            return false;
        }
        String originalString = pLabel.getText() != null ? pLabel.getText() : "";
        Text textNode = (Text) pLabel.lookup(".text"); // "text" is the style class of Text
        String actualString = textNode!=null?textNode.getText():"";
        boolean isOverrun = !actualString.isEmpty() && !originalString.equals(actualString);
        LOG.finest("is overrun? " + isOverrun + "\n originalString " + originalString + "\n actuaString " + actualString);
        return isOverrun;
    }

    /**
     * add call back on node with callback 60
     *
     * @param pNode not to add callback
     * @param pCallback callback that occure
     */
    public static void addOverrunCallback(Labeled pNode, Callback<Boolean, Void> pCallback) {
        addOverrunCallback(pNode, pCallback, 60);
    }

    public static void addOverrunInfoButton(Label pNode, String pPopOverContent) {
        addOverrunInfoButton(pNode, pPopOverContent, false);
    }

    public static void addInfoTooltip(Labeled pNode, String pTolltip) {

        addInfoTooltip(pNode, pTolltip, false);
    }

    public static void addOverrunInfoButton(Label pNode, String pPopOverContent, Boolean pUnboundWidth) {
        addOverrunCallback(pNode, new Callback<Boolean, Void>() {
            @Override
            public Void call(Boolean isOverrun) {
                if (!isOverrun) {
                    if (containsOverrunButton(pNode.getParent())) {
                        if (pNode.getParent() instanceof Pane) {
                            ((Pane) pNode.getParent()).getChildren().removeIf(new Predicate<Node>() {
                                @Override
                                public boolean test(Node node) {
                                    return node.getId() != null && node.getId().equals("btnPopover");
                                }
                            });
                        }
                    }
                    return null;
                }
                if (pNode.getParent() instanceof HBox) {
                    if (!pNode.getParent().getStyleClass().contains("cell-hbox")) {
                        pNode.getParent().getStyleClass().add("cell-hbox");
                    }
                    if (!containsOverrunButton(pNode.getParent())) {
                        ((Pane) pNode.getParent()).getChildren().add(getOverrunButton(pPopOverContent, pNode, pUnboundWidth));
                        return null;
                    }
                }

                if (pNode.getParent() instanceof TableCell) {
                    TableCell<?, ?> parent = (TableCell<?, ?>) pNode.getParent();
                    if (parent == null) {
                        return null;
                    }
                    if (!containsOverrunButton(parent)) {
                        HBox box = new HBox(pNode, getOverrunButton(pPopOverContent, pNode, pUnboundWidth));
                        box.setAlignment(Pos.CENTER_LEFT);
                        box.setPrefHeight(HBox.USE_COMPUTED_SIZE);
                        box.setMaxHeight(HBox.USE_PREF_SIZE);
                        box.getStyleClass().add("cell-hbox");
                        parent.setGraphic(box);
                        return null;
                    }
                }
                return null;
            }
        });
    }

    public static void addInfoTooltip(Labeled pNode, String pTooltip, Boolean pUnboundWidth) {
        addOverrunCallback(pNode, new Callback<Boolean, Void>() {
            @Override
            public Void call(Boolean isOverrun) {
                if (!isOverrun) {
                    pNode.setTooltip(null);
                    return null;
                }
                if (pNode.getParent() instanceof HBox) {
                    if (pTooltip != null && !pTooltip.isEmpty()) {
                        pNode.setTooltip(new Tooltip(pTooltip));
                    } else {
                        pNode.setTooltip(null);
                    }
                    return null;
                }
                if (pNode.getParent() instanceof TableCell) {
                    TableCell<?, ?> parent = (TableCell<?, ?>) pNode.getParent();
                    if (parent == null) {
                        return null;
                    }
                    if (!pUnboundWidth) {
                        HBox box = new HBox(pNode);

                        if (pNode.getTooltip() == null) {
                            if (pTooltip != null && !pTooltip.isEmpty()) {
                                pNode.setTooltip(new Tooltip(pTooltip));
                            }
                        }
                        box.setAlignment(Pos.CENTER_LEFT);
                        box.setPrefHeight(HBox.USE_COMPUTED_SIZE);
                        box.setMaxHeight(HBox.USE_PREF_SIZE);
                        box.getStyleClass().add("cell-hbox");
                        parent.setGraphic(box);
                        return null;
                    }

                }
                return null;
            }
        });
    }

    public static void addOverrunInfoButton(Label pLabeled) {
        addOverrunInfoButton(pLabeled, pLabeled.getText());
    }

    public static void addInfoTooltip(Label pLabeled) {
        addInfoTooltip(pLabeled, pLabeled.getText());
    }

    public static void addInfoTooltip(Hyperlink pLabeled) {
        addInfoTooltip(pLabeled, pLabeled.getText());
    }

    private static boolean containsOverrunButton(Parent pParent) {
        if (pParent == null) {
            return false;
        }
        for (Node node : pParent.getChildrenUnmodifiable()) {
            if (node.getId() != null && node.getId().equals("btnPopover")) {
                return true;
            }
        }
        return false;
    }

    private static Button getOverrunButton(String pPopOverContent, Region pNode, Boolean pUnboundWidth) {
        Button btn = new Button();
        btn.setId("btnPopover");
        btn.getStyleClass().add("cpx-icon-button");
        btn.setMinWidth(30);
        btn.setGraphic(ResourceLoader.getGlyph("\uf044"));
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PopOver over = new OverrunPopover(pPopOverContent);
                if (!pUnboundWidth) {
                    over.setMaxWidth(pNode.getBoundsInParent().getWidth());
                }
                over.show(pNode);
            }
        });
        return btn;
    }

    private static class OverrunPopover extends AutoFitPopOver {

        private final String text;

        OverrunPopover(String pText) {
            super();
            this.text = pText;
            initMenu();
        }

        private void initMenu() {

            VBox vBox = new VBox();
            vBox.setSpacing(5);
            Label labelText = new Label(text);
            labelText.maxWidthProperty().bindBidirectional(maxWidthProperty());
            labelText.setWrapText(true);
            labelText.autosize();
            labelText.setStyle("-fx-text-fill: -black01;");
            vBox.getChildren().addAll(labelText);//,getBatchgroupingTitledPane());
            vBox.setPadding(new Insets(10, 10, 10, 10));
            setHideOnEscape(true);
            setDetachable(false);
            setAutoHide(true);
            setDefaultArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
            setContentNode(vBox);
        }

    }
}
