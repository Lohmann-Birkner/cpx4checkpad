/* 
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.patient.status.organ;

import de.lb.cpx.shared.dto.acg.IcdFarbeOrgan;
import java.util.List;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * Describes a human part (body or organ)
 *
 * @author niemeier
 */
public interface HumanPartGraphic {

    /**
     * ImageView that holds an instance the image of an organ or body that is
     * rendered to the user
     *
     * @return image view
     */
    ImageView getImageView();

    /**
     * Loads an SVG image with the given width and height
     *
     * @param pHeight height
     * @param pWidth width
     * @return image of a human body or organ
     */
    Image loadImage(final double pHeight, final double pWidth);

    /**
     * Ratio between height and width of the image that has to be rendered (only
     * for internal use and not really important)
     *
     * @return height/width ratio
     */
    double getRatio();

    /**
     * Returns an adjusted copy of Image View
     *
     * @param pPane Target stack pane
     * @param pZoom Zoom factor
     * @param pIcds information about organ's diseases and their severity
     * @return image view that holds a image of the requested organ
     */
    ImageView addToPane(final StackPane pPane, final double pZoom, final List<IcdFarbeOrgan> pIcds);

    /**
     * File name to the graphic file (SVG)
     *
     * @return file name
     */
    String getGraphicFileName();

    /**
     * Name of the organ or body
     *
     * @return name
     */
    String getName();

    /**
     * Name of the organ or body in English
     *
     * @return name
     */
    String getNameEnglish();

    /**
     * Name of the organ or body in German
     *
     * @return name
     */
    String getNameGerman();

    /**
     * Unique identifier for this organ
     *
     * @return ID
     */
    String getId();

    /**
     * Size relative to the pane (for bodys) or body (for organs)
     *
     * @return size in percent
     */
    double getSize();

    Effect getCurrentEffect();

    Effect setEffect(final Effect pEffect);

    Effect showNoneColored();

    /**
     * Unique number for this organ
     *
     * @return Number
     */
    int getNumber();

    /**
     * Horizontal position relative to the middle (0, 0)
     *
     * @return horizontal position (X)
     */
    double getPositionX();

    /**
     * Vertical position relative to the middle (0, 0)
     *
     * @return vertical position (Y)
     */
    double getPositionY();

    /**
     * Organ is related to this body
     *
     * @return related body
     */
    BodyGraphic getBody();

}
