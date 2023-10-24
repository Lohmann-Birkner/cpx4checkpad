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

/**
 * Abstract implementation of a human organ
 *
 * @author niemeier
 */
public class AbstractOrganGraphic extends AbstractHumanPartGraphic implements OrganGraphic {

    /**
     * Instance represents a part of a human organ
     *
     * @param pBody Related body graphic (e.g. male or female)
     * @param pId Unique identifier (should not be changed)
     * @param pNameEnglish Name in English
     * @param pNameGerman Name in German
     * @param pGraphicFileName Filename for graphical representation (SVG)
     * @param pNumber Unique number for this part of a human organ
     * @param pSize Size relative to the body (for organs)
     * @param pPositionX Horizontal position relative to the middle (0, 0)
     * @param pPositionY Vertical position relative to the middle (0, 0)
     */
    public AbstractOrganGraphic(final BodyGraphic pBody,
            final String pId,
            final String pNameEnglish, final String pNameGerman, final String pGraphicFileName, final int pNumber,
            final double pSize, final double pPositionX, final double pPositionY) {
        super(pBody, pId, pNameEnglish, pNameGerman, pGraphicFileName, pNumber, pSize, pPositionX, pPositionY);
    }

}
