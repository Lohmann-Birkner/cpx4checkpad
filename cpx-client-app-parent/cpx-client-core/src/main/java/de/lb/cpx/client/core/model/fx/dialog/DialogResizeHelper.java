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
 *    2016  adameck - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.dialog;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author adameck Checks Stages Max/Min Height and Max/Min Width If the
 * Attributes are set the Dialog can not get smaller then Min Vale or Bigger
 * then Max Value If Min = Max Dialog can not be resized
 *
 *
 */
public class DialogResizeHelper {

    public static void addDialogResizeListener(Stage stage) {
        DialogResizeListener resizeListener = new DialogResizeListener(stage);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_MOVED, resizeListener);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_PRESSED, resizeListener);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_DRAGGED, resizeListener);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_EXITED, resizeListener);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, resizeListener);
        ObservableList<Node> children = stage.getScene().getRoot().getChildrenUnmodifiable();
        for (Node child : children) {
            addListenerDeeply(child, resizeListener);
        }
//        stage.sizeToScene();
    }

    public static void addListenerDeeply(Node node, EventHandler<MouseEvent> listener) {
        node.addEventHandler(MouseEvent.MOUSE_MOVED, listener);
        node.addEventHandler(MouseEvent.MOUSE_PRESSED, listener);
        node.addEventHandler(MouseEvent.MOUSE_DRAGGED, listener);
        node.addEventHandler(MouseEvent.MOUSE_EXITED, listener);
        node.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, listener);
        if (node instanceof Parent) {
            Parent parent = (Parent) node;
            ObservableList<Node> children = parent.getChildrenUnmodifiable();
            for (Node child : children) {
                addListenerDeeply(child, listener);
            }
        }
    }

    static class DialogResizeListener implements EventHandler<MouseEvent> {

        private final Stage stage;
        private Cursor cursorEvent = Cursor.DEFAULT;
        private final int border = 8;
        private double startX = 0;
        private double startY = 0;

        private double initialX = 0;
        private double initialY = 0;

        private double sceneWidth;
        private double sceneHeight;
        private double counter = 0;
        //change initalisation of min and max values in initalisation to access possible changed values after listener is set
        //AWi 04052017
//        private final double minWidth;
//        private final double minHeight;
//        private final double maxWidth;
//        private final double maxHeight;

        DialogResizeListener(Stage stage) {
            this.stage = stage;
//            stage.setWidth(0.0);
//            stage.setHeight(0.0);
//            LOG.info("init with " + stage.getMinHeight() + " " + stage.getMaxHeight() + " " + stage.getMinWidth() + " " + stage.getMaxWidth());
//            stage.widthProperty().addListener(new ChangeListener<Number>() {
//                @Override
//                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                    LOG.info("change width from " +oldValue.doubleValue() + " to " + newValue.doubleValue());
//                }
//            });
//            stage.heightProperty().addListener(new ChangeListener<Number>() {
//                @Override
//                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                    LOG.info("change height from " +oldValue.doubleValue() + " to " + newValue.doubleValue());
//                }
//            });
//            minWidth = stage.getMinWidth() > (border * 2) ? stage.getMinWidth() : (border * 2);
//            minHeight = stage.getMinHeight() > (border * 2) ? stage.getMinHeight() : (border * 2);
//            maxWidth = stage.getMaxWidth();
//            maxHeight = stage.getMaxHeight();
        }

        private double getMinWidth() {
            return stage.getMinWidth() > (border * 2) ? stage.getMinWidth() : (border * 2);
        }

        private double getMaxWidth() {
            return stage.getMaxWidth();
        }

        private double getMinHeight() {
            return stage.getMinHeight() > (border * 2) ? stage.getMinHeight() : (border * 2);
        }

        private double getMaxHeight() {
            return stage.getMaxHeight();
        }

        @Override
        public void handle(MouseEvent mouseEvent) {
            //Awi-20170815
            //workaround to reduce flicker effect on resize
            //only draw in every fourth event
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (Double.doubleToRawLongBits(counter % 6) == Double.doubleToRawLongBits(0)) {
                        computeSizes(mouseEvent);
                    }
                    counter++;
                }
            });
        }

        private void computeSizes(MouseEvent mouseEvent) {
            EventType<? extends MouseEvent> mouseEventType = mouseEvent.getEventType();
            Scene scene = stage.getScene();
            sceneWidth = scene.getWidth();
            sceneHeight = scene.getHeight();
            double mouseEventX = mouseEvent.getSceneX(),
                    mouseEventY = mouseEvent.getSceneY();

            if (MouseEvent.MOUSE_MOVED.equals(mouseEventType) == true) {
                if (mouseEventX < border && mouseEventY < border) {
                    cursorEvent = Cursor.NW_RESIZE;
                } else if (mouseEventX < border && mouseEventY > sceneHeight - border) {
                    cursorEvent = Cursor.SW_RESIZE;
                } else if (mouseEventX > sceneWidth - border && mouseEventY < border) {
                    cursorEvent = Cursor.NE_RESIZE;
                } else if (mouseEventX > sceneWidth - border && mouseEventY > sceneHeight - border) {
                    cursorEvent = Cursor.SE_RESIZE;
                } else if (mouseEventX < border) {
                    cursorEvent = Cursor.W_RESIZE;
                } else if (mouseEventX > sceneWidth - border) {
                    cursorEvent = Cursor.E_RESIZE;
                } else if (mouseEventY < border) {
                    cursorEvent = Cursor.N_RESIZE;
                } else if (mouseEventY > sceneHeight - border) {
                    cursorEvent = Cursor.S_RESIZE;
                } else if ((mouseEventY > border) && (mouseEventY < (50 + border)) && (mouseEventX > border) && (mouseEventX < (sceneWidth - border))) {
                    cursorEvent = Cursor.MOVE;
                } else if ((mouseEventY < (sceneHeight - border)) && (mouseEventY > (sceneHeight - 41 - border)) && (mouseEventX > border) && (mouseEventX < (sceneWidth - border))) {
                    cursorEvent = Cursor.MOVE;
                } else {
                    cursorEvent = Cursor.DEFAULT;
                }
                scene.setCursor(cursorEvent);
            } else if (MouseEvent.MOUSE_EXITED.equals(mouseEventType) || MouseEvent.MOUSE_EXITED_TARGET.equals(mouseEventType)) {
                scene.setCursor(Cursor.DEFAULT);
            } else if (MouseEvent.MOUSE_PRESSED.equals(mouseEventType) == true) {
                startX = stage.getWidth() - mouseEventX;
                startY = stage.getHeight() - mouseEventY;

                initialX = mouseEvent.getSceneX();
                initialY = mouseEvent.getSceneY();

            } else if (MouseEvent.MOUSE_DRAGGED.equals(mouseEventType) == true) {
                if (Cursor.DEFAULT.equals(cursorEvent) == false) {
                    if ((Cursor.W_RESIZE.equals(cursorEvent) == false && Cursor.E_RESIZE.equals(cursorEvent) == false) && Cursor.MOVE.equals(cursorEvent) == false) {
                        if (Cursor.NW_RESIZE.equals(cursorEvent) == true || Cursor.N_RESIZE.equals(cursorEvent) == true || Cursor.NE_RESIZE.equals(cursorEvent) == true) {
                            if ((stage.getHeight() > getMinHeight() || mouseEventY < 0) && getMaxHeight() > stage.getHeight()) {
                                stage.setHeight(stage.getY() - mouseEvent.getScreenY() + stage.getHeight());
                                stage.setY(mouseEvent.getScreenY());
                            }
                        } else if ((stage.getHeight() > getMinHeight() || mouseEventY + startY - stage.getHeight() > 0) && getMaxHeight() > stage.getHeight()) {
                            stage.setHeight(mouseEventY + startY);
                        }
                    }

                    if ((Cursor.N_RESIZE.equals(cursorEvent) == false && Cursor.S_RESIZE.equals(cursorEvent) == false) && Cursor.MOVE.equals(cursorEvent) == false) {

                        if (Cursor.NW_RESIZE.equals(cursorEvent) == true || Cursor.W_RESIZE.equals(cursorEvent) == true || Cursor.SW_RESIZE.equals(cursorEvent) == true) {
                            if ((stage.getWidth() > getMinWidth() || mouseEventX < 0) && getMaxWidth() > stage.getWidth()) {
                                stage.setWidth(stage.getX() - mouseEvent.getScreenX() + stage.getWidth());
                                stage.setX(mouseEvent.getScreenX());
                            }
                        } else if ((stage.getWidth() > getMinWidth() || mouseEventX + startX - stage.getWidth() > 0) && getMaxWidth() >= stage.getWidth()) {
                            stage.setWidth(mouseEventX + startX);
                        }
                    }

                    if (Cursor.MOVE.equals(cursorEvent) == true) {
                        stage.setX(mouseEvent.getScreenX() - initialX);
                        stage.setY(mouseEvent.getScreenY() - initialY);
                    }
                }
            }
        }
    }
}
