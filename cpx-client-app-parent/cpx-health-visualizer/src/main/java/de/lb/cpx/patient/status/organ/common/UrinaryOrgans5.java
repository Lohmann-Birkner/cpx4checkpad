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
import de.lb.cpx.patient.status.organ.female.FemaleUrinaryOrgans;
import de.lb.cpx.patient.status.organ.male.MaleUrinaryOrgans;

/**
 * Reproductive Organs/Fortpflanzungsorgane
 *
 * @author niemeier
 */
public abstract class UrinaryOrgans5 extends AbstractOrganGraphic {

    public static final String ID = "urinary_organs";
    public static final String NAME_ENGLISH = "Urinary Organs";
    public static final String NAME_GERMAN = "Harnorgane";
    public static final String GRAPHIC_FILE_NAME = "urinary_organs.svg";
    public static final int NUMBER = 5;

    public UrinaryOrgans5(final BodyGraphic pBody, final String pId,
            final String pNameEnglish, final String pNameGerman,
            final String pGraphicFileName, final int pNumber,
            final double pSize, final double pPositionX, final double pPositionY) {
        super(pBody, pId, pNameEnglish, pNameGerman, pGraphicFileName, pNumber, pSize, pPositionX, pPositionY);
    }

    public static UrinaryOrgans5 instance(final Body16 pBody, final char pGender) {
        if (AbstractHumanPartGraphic.isFemale(pGender)) {
            return new FemaleUrinaryOrgans(pBody);
        }
        if (AbstractHumanPartGraphic.isMale(pGender)) {
            return new MaleUrinaryOrgans(pBody);
        }
        return new MaleUrinaryOrgans(pBody);
    }

}
