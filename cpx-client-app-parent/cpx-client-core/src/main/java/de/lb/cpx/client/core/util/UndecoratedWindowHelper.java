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
package de.lb.cpx.client.core.util;

import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * test class to refactor ResizableHelper Undecorated windows are not draggable
 * and or resizeable
 *
 * @author wilde
 */
public class UndecoratedWindowHelper {

    private static final Logger LOG = Logger.getLogger(UndecoratedWindowHelper.class.getName());

    /**
     * register listeners to a specific drag region to define dragging
     *
     * @param pDragRegion region to start drag
     * @param pStage stage to drag
     * @param pOffset offset for borders
     */
    public static void enableDragOn(Node pDragRegion, Stage pStage, double pOffset) {
        if (pDragRegion == null || pStage == null) {
            LOG.warning("Can not register drag on region! Stage and/or drag node are null!");
            return;
        }
        DoubleProperty xOffset = new SimpleDoubleProperty(pStage.getX());
        DoubleProperty yOffset = new SimpleDoubleProperty(pStage.getY());
        //set cursor to move when ever mouse is in scene, system handles the events
        pDragRegion.setStyle("-fx-cursor:MOVE;");
        pDragRegion.addEventFilter(MouseEvent.MOUSE_PRESSED, (event) -> {
            xOffset.set(pStage.getX() - event.getScreenX());
            yOffset.set(pStage.getY() - event.getScreenY());
        });
        pDragRegion.addEventFilter(MouseEvent.MOUSE_DRAGGED, (event) -> {
            pStage.setX(event.getScreenX() + xOffset.get());
            pStage.setY(event.getScreenY() + yOffset.get());
        });

    }

    public static void enableDragOn(Node pDragRegion, Stage pStage) {
        enableDragOn(pDragRegion, pStage, 0);
    }

    /**
     * enable resize with border
     *
     * @param pBorderWidth with of resize border
     * @param pStage stage to resize
     */
    public static void enableResize(double pBorderWidth, Stage pStage) {
        if (pStage == null) {
            LOG.warning("Stage is null");
            return;
        }
        if (!pStage.getStyle().equals(StageStyle.UNDECORATED)) {
            LOG.warning("Stage is not undecorated");
            return;
        }
        if (pStage.getScene() == null) {
            LOG.warning("Stage does not contains scene, can not enable resize");
            return;
        }

        pStage.getScene().addEventFilter(MouseEvent.MOUSE_MOVED, (event) -> {
            Cursor cursor = computeResizeCursor(
                    pStage.getScene().getHeight(), pStage.getScene().getWidth(),
                    event.getSceneX(), event.getSceneY(), pBorderWidth);
            if (cursor == null) {
                event.consume();
                return;
            }
            setCursor(pStage.getScene(), cursor);
        });
        pStage.getScene().addEventFilter(MouseEvent.MOUSE_DRAGGED, (event) -> {
            Cursor cursorEvent = pStage.getScene().getCursor();
            double xOffset = pStage.getWidth() - event.getSceneX();
            double yOffset = pStage.getHeight() - event.getSceneY();
            computeResizeScene(cursorEvent, pStage,
                    event.getSceneX(), event.getSceneY(),
                    event.getScreenX(), event.getScreenY(),
                    pStage.getMinHeight(), pStage.getMaxHeight(),
                    pStage.getMinWidth(), pStage.getMaxWidth());
        });
    }

    private static Cursor computeResizeCursor(double pSceneHeight, double pSceneWidth, double pMouseX, double pMouseY, double pBorder) {
        if (pMouseX < pBorder && pMouseY < pBorder) {
            return Cursor.NW_RESIZE;
        }
        if (pMouseX < pBorder && pMouseY > pSceneHeight - pBorder) {
            return Cursor.SW_RESIZE;
        }
        if (pMouseX > pSceneWidth - pBorder && pMouseY < pBorder) {
            return Cursor.NE_RESIZE;
        }
        if (pMouseX > pSceneWidth - pBorder && pMouseY > pSceneHeight - pBorder) {
            return Cursor.SE_RESIZE;
        }
        if (pMouseX < pBorder) {
            return Cursor.W_RESIZE;
        }
        if (pMouseX > pSceneWidth - pBorder) {
            return Cursor.E_RESIZE;
        }
        if (pMouseY < pBorder) {
            return Cursor.N_RESIZE;
        }
        if (pMouseY > pSceneHeight - pBorder) {
            return Cursor.S_RESIZE;
        }
        return Cursor.DEFAULT;
    }

    private static void setCursor(Scene pScene, Cursor pCursor) {
        if (pCursor == null) {
            return;
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                pScene.setCursor(pCursor);
            }
        });
    }

    private static void computeResizeScene(Cursor pCursorEvent, Stage pStage,
            double pMouseSceneX, double pMouseSceneY,
            double pMouseScreenX, double pMouseScreenY,
            double pMinHeight, double pMaxHeight,
            double pMinWidth, double pMaxWidth) {
        if (pCursorEvent == null) {
            return;
        }
        //if cursor is not default start check
        if (Cursor.DEFAULT.equals(pCursorEvent) == false) {

            if (pCursorEvent.equals(Cursor.N_RESIZE) || pCursorEvent.equals(Cursor.NE_RESIZE) || pCursorEvent.equals(Cursor.NW_RESIZE)) {
                double height = pStage.getY() - pMouseScreenY + pStage.getHeight();
                if (height >= pMinHeight && height < pMaxHeight) {
                    pStage.setHeight(height);
                    pStage.setY(pMouseScreenY);
                }
            }
            if (pCursorEvent.equals(Cursor.S_RESIZE) || pCursorEvent.equals(Cursor.SE_RESIZE) || pCursorEvent.equals(Cursor.SW_RESIZE)) {
                if (pMouseSceneY > pMinHeight && pMouseSceneY < pMaxHeight) {
                    pStage.setHeight(pMouseSceneY);
                }
            }
            if (pCursorEvent.equals(Cursor.E_RESIZE) || pCursorEvent.equals(Cursor.NE_RESIZE) || pCursorEvent.equals(Cursor.SE_RESIZE)) {
                if (pMouseSceneX > pMinWidth && pMouseSceneX < pMaxWidth) {
                    pStage.setWidth(pMouseSceneX);
                }

            }
            if (pCursorEvent.equals(Cursor.W_RESIZE) || pCursorEvent.equals(Cursor.NW_RESIZE) || pCursorEvent.equals(Cursor.SW_RESIZE)) {
                double width = pStage.getX() - pMouseScreenX + pStage.getWidth();
                if (width >= pMinWidth && width < pMaxWidth) {
                    pStage.setWidth(width);
                    pStage.setX(pMouseScreenX);
                }
            }
        }
        pStage.getScene().getRoot().requestLayout();
    }
}
