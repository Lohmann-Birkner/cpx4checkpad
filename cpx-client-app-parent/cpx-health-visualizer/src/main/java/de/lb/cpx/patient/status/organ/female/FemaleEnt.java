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
package de.lb.cpx.patient.status.organ.female;

import de.lb.cpx.patient.status.organ.BodyGraphic;
import de.lb.cpx.patient.status.organ.common.Ent10;

/**
 * Ears/Gehörsystem
 *
 * @author niemeier
 */
public class FemaleEnt extends Ent10 {

    public static final String GRAPHIC_FILE_PATH = "female/" + GRAPHIC_FILE_NAME;
    public static final double RELATIVE_SIZE = 19.5d;
    public static final double POSITION_X = -0.2d;
    public static final double POSITION_Y = -77.0d;

    public FemaleEnt(final BodyGraphic pBody) {
        super(pBody, ID, NAME_ENGLISH, NAME_GERMAN, GRAPHIC_FILE_PATH, NUMBER,
                RELATIVE_SIZE, //size relative to the pane (for bodys) or body (for organs) 
                POSITION_X, //horizontal position relative to the middle (0, 0) 
                POSITION_Y); //vertical position relative to the middle (0, 0)
    }

}
