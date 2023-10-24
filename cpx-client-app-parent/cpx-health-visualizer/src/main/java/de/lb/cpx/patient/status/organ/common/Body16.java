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
package de.lb.cpx.patient.status.organ.common;

import de.lb.cpx.patient.status.organ.AbstractBodyGraphic;
import de.lb.cpx.patient.status.organ.AbstractHumanPartGraphic;
import de.lb.cpx.patient.status.organ.female.FemaleBody;
import de.lb.cpx.patient.status.organ.male.MaleBody;

/**
 * Body/Körper
 *
 * @author niemeier
 */
public abstract class Body16 extends AbstractBodyGraphic {

    public static final String ID = "whole_body";
    public static final String NAME_ENGLISH = "whole Body";
    public static final String NAME_GERMAN = "ganzer Körper";
    public static final String GRAPHIC_FILE_NAME = "body.svg";
    public static final int NUMBER = 16;

    public Body16(final String pId,
            final String pNameEnglish, final String pNameGerman,
            final String pGraphicFileName, final int pNumber,
            final double pSize, final double pPositionX, final double pPositionY) {
        super(pId, pNameEnglish, pNameGerman, pGraphicFileName, pNumber, pSize, pPositionX, pPositionY);
    }

    public static Body16 instance(final char pGender) {
        if (AbstractHumanPartGraphic.isFemale(pGender)) {
            return new FemaleBody();
        }
        if (AbstractHumanPartGraphic.isMale(pGender)) {
            return new MaleBody();
        }
        return new MaleBody();
    }

}
