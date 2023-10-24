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

import java.io.Serializable;
import javafx.scene.paint.Color;

/**
 * Describes the severity of an organ's disease. Indirectly controls the color
 * of a rendered organ.
 *
 * @author niemeier
 */
public enum OrganSeverity implements Serializable {

    None(0, "none", "keine", Color.WHITE),
    Minor(1, "minor", "leicht", Color.YELLOW), //Yellow
    Moderate(2, "moderate", "mäßig", Color.ORANGE), //Orange
    Severe(3, "severe", "schwer", Color.RED), //Red
    Severst(4, "severst", "schwerste", Color.INDIGO); //Indigo is somewhat purple

    private OrganSeverity(final int pCcl, final String pNameEnglish, final String pNameGerman, final Color pColor) {
        ccl = pCcl;
        nameEnglish = pNameEnglish;
        nameGerman = pNameGerman;
        color = pColor;
    }

    public final int ccl;
    public final String nameEnglish;
    public final String nameGerman;
    public final Color color;

    public static OrganSeverity getSeverityByCcl(final Integer pCcl) {
        if (pCcl == null) {
            throw new IllegalArgumentException("CCL is null!");
        }
        OrganSeverity severity = null;
        for (OrganSeverity tmp : OrganSeverity.values()) {
            if (tmp.ccl == pCcl) {
                severity = tmp;
            }
        }
        return severity;
    }

    /**
     * Get the highest severity
     *
     * @return severity
     */
    public static OrganSeverity getMaxSeverity() {
        OrganSeverity maxSeverity = null;
        for (OrganSeverity tmp : OrganSeverity.values()) {
            if (maxSeverity == null || tmp.ccl > maxSeverity.ccl) {
                maxSeverity = tmp;
            }
        }
        return maxSeverity;
    }

    /**
     * Get the lowest severity (except from None/0)
     *
     * @return severity
     */
    public static OrganSeverity getMinSeverity() {
        OrganSeverity maxSeverity = null;
        for (OrganSeverity tmp : OrganSeverity.values()) {
            if (None.equals(tmp)) {
                continue;
            }
            if (maxSeverity == null || tmp.ccl < maxSeverity.ccl) {
                maxSeverity = tmp;
            }
        }
        return maxSeverity;
    }

    /**
     * Clinical Complexity Level
     *
     * @return CCL (0-6)
     */
    public int getCcl() {
        return ccl;
    }

    /**
     * Severity name in English
     *
     * @return name
     */
    public String getNameEnglish() {
        return nameEnglish;
    }

    /**
     * Severity name in German
     *
     * @return name
     */
    public String getNameGerman() {
        return nameGerman;
    }

    /**
     * Severity name
     *
     * @return name
     */
    public String getName() {
        return getNameEnglish() + "/" + getNameGerman();
    }

    /**
     * Use this color to visualize organ's diseases
     *
     * @return Color
     */
    public Color getColor() {
        return color;
    }

}
