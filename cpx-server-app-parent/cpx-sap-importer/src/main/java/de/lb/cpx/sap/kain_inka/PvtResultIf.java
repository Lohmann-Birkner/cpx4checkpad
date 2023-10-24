/* 
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.sap.kain_inka;

/**
 *
 * @author gerschmann
 */
public interface PvtResultIf {

    /**
     *
     * @param text text
     */
    void setText(String text);

    /**
     *
     * @param code code
     * @param loc localisation
     */
    void setPVTPrincipalDiag(String code, String loc);

    /**
     *
     * @param code code
     * @param loc localisation
     */
    void setPVTPrincipalSecondaryDiag(String code, String loc);

    /**
     *
     * @param code code
     * @param loc localisation
     */
    void setPVTAuxDiag(String code, String loc);

    /**
     *
     * @param code code
     * @param loc localisation
     */
    void setPVTSecondaryAuxDiag(String code, String loc);

    /**
     *
     * @param code code
     * @param loc localisation
     */
    void setPVTProcedure(String code, String loc);

    /**
     *
     * @return text
     */
    String getText();

    /**
     *
     * @return icd code
     */
    String getPVTPrincipalDiagCode();

    /**
     *
     * @return icd localisation
     */
    String getPVTPrincipalDiagLoc();

    /**
     *
     * @return icd secondary code
     */
    String getPVTPrincipalDiagSecondaryCode();

    /**
     *
     * @return icd secondary localisation
     */
    String getPVTPrincipalDiagSecondaryLoc();

    /**
     *
     * @return auxiliary icd code
     */
    String getPVTAuxDiagCode();

    /**
     *
     * @return auxiliary icd localisation
     */
    String getPVTAuxDiagLoc();

    /**
     *
     * @return auxiliary icd secondary code
     */
    String getPVTAuxDiagSecondaryCode();

    /**
     *
     * @return auxiliary icd secondary localisation
     */
    String getPVTAuxDiagSecondaryLoc();

    /**
     *
     * @return ops code
     */
    String getPVTProcedureCode();

    /**
     *
     * @return ops localisation
     */
    String getPVTProcedureLoc();

}
