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
package de.lb.cpx.client.core.model.fx.toggle.skin;

import de.lb.cpx.client.core.model.fx.toggle.ToggleGroupControl;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 *
 * @author wilde
 */
public class ToggleGroupControlSkin extends SkinBase<ToggleGroupControl<? extends ToggleButton>> {

    private static final Logger LOG = Logger.getLogger(ToggleGroupControlSkin.class.getName());

    public ToggleGroupControlSkin(ToggleGroupControl<? extends ToggleButton> pSkinnable) {
        super(pSkinnable);
        pSkinnable.orientationProperty().addListener(new ChangeListener<Orientation>() {
            @Override
            public void changed(ObservableValue<? extends Orientation> observable, Orientation oldValue, Orientation newValue) {
                setUpOrientation(newValue);
            }
        });
        pSkinnable.alignmentProperty().addListener(new ChangeListener<Pos>() {
            @Override
            public void changed(ObservableValue<? extends Pos> ov, Pos t, Pos t1) {
                if (getChildren().isEmpty()) {
                    LOG.warning("alignment can not be set, no children placed in control!");
                    return;
                }
                Node box = getChildren().get(0);
                if (box instanceof VBox) {
                    ((VBox) box).setAlignment(getSkinnable().getAlignment());
                }
                if (box instanceof HBox) {
                    ((HBox) box).setAlignment(getSkinnable().getAlignment());
                }
            }
        });
        setUpOrientation(pSkinnable.getOrientation());
    }

    private void setUpOrientation(Orientation pOrientation) {
        getChildren().clear();
        Pane box = null;
        switch (pOrientation) {
            case HORIZONTAL:
                box = new HBox();
                ((HBox) box).setSpacing(getSkinnable().getSpacing());
                ((HBox) box).setAlignment(getSkinnable().getAlignment());
                box.getChildren().addAll(getSkinnable().getToggleList());
                break;
            case VERTICAL:
                box = new VBox();
                ((VBox) box).setSpacing(getSkinnable().getSpacing());
                ((VBox) box).setAlignment(getSkinnable().getAlignment());
                box.getChildren().addAll(getSkinnable().getToggleList());
                break;
        }
        getChildren().add(box);
    }
}
