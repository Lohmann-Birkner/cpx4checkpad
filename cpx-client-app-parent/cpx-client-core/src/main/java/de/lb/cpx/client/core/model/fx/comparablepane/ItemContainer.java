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
package de.lb.cpx.client.core.model.fx.comparablepane;

import de.lb.cpx.server.commons.dao.AbstractEntity;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * item container for comparable pane
 *
 * @author wilde
 * @param <Z> base control/pane which contains the data gridpane,tableview etc.
 * @param <E> enclosing object of the comparable TODO: auto update
 */
public class ItemContainer<Z extends Region, E extends ComparableContent<? extends AbstractEntity>> extends VBox {

    private VBox header = new VBox();
    private ComparablePaneSkin<Z, E> skin;
    private Z region;
    private Control control;
    private E content;

    ItemContainer(E pContent, Control pControl, Z pRegion, ComparablePaneSkin<Z, E> pSkin) {
        skin = pSkin;
        header.setFillWidth(true);
//            header.setStyle("-fx-background-color:yellow");
        header.setAlignment(Pos.CENTER_RIGHT);
        header.minHeightProperty().bind(getSkinnable().prefHeaderHeightProperty());
        header.prefHeightProperty().bind(getSkinnable().prefHeaderHeightProperty());
        header.maxHeightProperty().bind(getSkinnable().prefHeaderHeightProperty());
        //removed due to not needed
//        header.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                if (event.getClickCount() >= 2) {
//                    //CPX-1151 avoid NPE
//                    if (content == null) {
//                        event.consume();
//                    } else {
//                        if (getSkinnable().getOnRemoveEvent() != null) {
//                            boolean res = getSkinnable().getOnRemoveEvent().call(content);
//                            if (!res) {
//                                BasicMainApp.showErrorMessageDialog(Lang.getErrorCouldNotDelete());
//                            }
////                                ((ComparablePaneSkin)getSkin()).layoutMenuBox();
//                        }
//                    }
//                }
//            }
//        });
        setControl(pControl);
        region = pRegion;
        content = pContent;
        VBox.setVgrow(region, Priority.ALWAYS);
        getChildren().addAll(region);
        minWidthProperty().bind(getSkinnable().versionContentWidthProperty());
        prefWidthProperty().bind(getSkinnable().versionContentWidthProperty());
        maxWidthProperty().bind(getSkinnable().versionContentWidthProperty());
//            version = pVersion;
    }

    public final ComparablePaneSkin<Z, E> getSkin() {
        return skin;
    }

    public final ComparablePane<Z, E> getSkinnable() {
        return skin.getSkinnable();
    }

    public void dispose() {
        header.getChildren().clear();
        header.minHeightProperty().unbind();
        header.prefHeightProperty().unbind();
        header.maxHeightProperty().unbind();
        getChildren().clear();
        header = null;
        region = null;
        control = null;
        content = null;
        skin = null;
    }

    public VBox getHeader() {
        return header;
    }

    public Z getRegion() {
        return region;
    }

    public E getContent() {
        return content;
    }

    public void setControl(Control pControl) {
        if (control != null) {
            if (control instanceof ItemContainerControl) {
                ((ItemContainerControl) control).destroy();
            }
        }
        control = pControl;
        header.getChildren().clear();
        if (header != null && header.prefWidthProperty().isBound()) {
            header.prefWidthProperty().unbind();
        }
        if (pControl == null) {
            return;
        }
        header.getChildren().add(pControl);
    }

    public Control getControl() {
        return control;
    }

    public void setShowMenu(Boolean pShow) {
        if (!pShow) {
            if (getChildren().contains(header)) {
                getChildren().remove(header);
            }
        } else {
            if (!getChildren().contains(header)) {
                getChildren().add(0, header);
            }
        }
    }

}
