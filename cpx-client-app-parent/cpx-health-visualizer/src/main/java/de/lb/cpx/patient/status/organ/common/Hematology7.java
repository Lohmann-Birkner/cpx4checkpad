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

import de.lb.cpx.patient.status.organ.AbstractHumanPartGraphic;
import de.lb.cpx.patient.status.organ.AbstractOrganGraphic;
import de.lb.cpx.patient.status.organ.BodyGraphic;
import de.lb.cpx.patient.status.organ.female.FemaleHematology;
import de.lb.cpx.patient.status.organ.male.MaleHematology;

/**
 * Reproductive Organs/Fortpflanzungsorgane
 *
 * @author niemeier
 */
public abstract class Hematology7 extends AbstractOrganGraphic {

    public static final String ID = "hematology";
    public static final String NAME_ENGLISH = "Hematology";
    public static final String NAME_GERMAN = "Hämatologie";
    public static final String GRAPHIC_FILE_NAME = "hematology.svg"; //same graphic as cardiovascula -__-
    public static final int NUMBER = 7;

    public Hematology7(final BodyGraphic pBody, final String pId,
            final String pNameEnglish, final String pNameGerman,
            final String pGraphicFileName, final int pNumber,
            final double pSize, final double pPositionX, final double pPositionY) {
        super(pBody, pId, pNameEnglish, pNameGerman, pGraphicFileName, pNumber, pSize, pPositionX, pPositionY);
    }

    public static Hematology7 instance(final Body16 pBody, final char pGender) {
        if (AbstractHumanPartGraphic.isFemale(pGender)) {
            return new FemaleHematology(pBody);
        }
        if (AbstractHumanPartGraphic.isMale(pGender)) {
            return new MaleHematology(pBody);
        }
        return new MaleHematology(pBody);
    }

}
