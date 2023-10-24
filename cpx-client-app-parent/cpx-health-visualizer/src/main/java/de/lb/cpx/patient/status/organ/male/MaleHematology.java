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
package de.lb.cpx.patient.status.organ.male;

import de.lb.cpx.patient.status.organ.BodyGraphic;
import de.lb.cpx.patient.status.organ.common.Hematology7;

/**
 * Hematology/Hämatologie
 *
 * @author niemeier
 */
public class MaleHematology extends Hematology7 {

    public static final String GRAPHIC_FILE_PATH = "male/" + GRAPHIC_FILE_NAME;
    public static final double RELATIVE_SIZE = 53.0d;
    public static final double POSITION_X = -0.5d;
    public static final double POSITION_Y = -45.5d;

    public MaleHematology(final BodyGraphic pBody) {
        super(pBody, ID, NAME_ENGLISH, NAME_GERMAN, GRAPHIC_FILE_PATH, NUMBER,
                RELATIVE_SIZE, //size relative to the pane (for bodys) or body (for organs) 
                POSITION_X, //horizontal position relative to the middle (0, 0) 
                POSITION_Y); //vertical position relative to the middle (0, 0)
    }

}
