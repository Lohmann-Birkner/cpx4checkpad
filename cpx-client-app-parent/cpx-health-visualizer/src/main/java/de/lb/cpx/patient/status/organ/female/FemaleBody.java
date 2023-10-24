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

import de.lb.cpx.patient.status.organ.common.Body16;

/**
 * Body/Körper
 *
 * @author niemeier
 */
public class FemaleBody extends Body16 {

    public static final String GRAPHIC_FILE_PATH = "female/" + GRAPHIC_FILE_NAME;
    public static final double RELATIVE_SIZE = 100.0d;
    public static final double POSITION_X = -0.3d;
    public static final double POSITION_Y = 0.0d;

    public FemaleBody() {
        super(ID, NAME_ENGLISH, NAME_GERMAN, GRAPHIC_FILE_PATH, NUMBER,
                RELATIVE_SIZE, //size relative to the pane (for bodys) or body (for organs) 
                POSITION_X, //horizontal position relative to the middle (0, 0) 
                POSITION_Y); //vertical position relative to the middle (0, 0)
    }

}
