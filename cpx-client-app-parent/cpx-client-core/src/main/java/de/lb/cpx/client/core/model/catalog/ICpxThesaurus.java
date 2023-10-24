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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.catalog;

import de.lb.cpx.model.enums.CountryEn;
import java.io.Serializable;

/**
 *
 * @author niemeier
 */
public interface ICpxThesaurus extends Serializable {

    CountryEn getCountryEn();

    int getTypeOfCode();

    int getDimdiInternalNo();

    String getPrimKeyNo1();

    String getPrimKeyNo2();

    String getStarKeyNo();

    String getAddKeyNo();

    String getDescription();

    String getReference();

    int getYear();

}
