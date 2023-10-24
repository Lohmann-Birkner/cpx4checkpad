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
package de.lb.cpx.service.ejb;

import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.TPatient;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author wilde
 */
@Remote
public interface AddCaseEJBRemote {

    /**
     * get List of Matches for given (uncomplete) patient number
     *
     * @param number (uncomplete) Patientnumber
     * @return List of possible Matches for given number
     */
    List<String> getMatchForPatientNumber(String number);

    /**
     * get Patient by Patientnumber
     *
     * @param patientNumber unique Patientnumber
     * @return Patient Entity for Patientnumber, null if there is no Patient
     * with that Patientnumber
     */
    TPatient getPatient(String patientNumber);

    List<TCaseIcd> getTestIcds(int count);

    List<TCaseOps> getTestOps(int count);

    /**
     * save Patient Entity in the Database
     *
     * @param patient patient Entity to save
     * @return id of the saved Patient
     */
    Long savePatient(TPatient patient);

    /**
     * save HospitalCase Entity in the Database
     *
     * @param hCase hospitalCase to save
     * @return id of the saved Case
     */
    Long saveCase(TCase hCase);

    boolean checkIfCaseExists(String caseNumber, String hospitalIdent);

    /**
     * get Tcase by caseNumber
     *
     * @param caseNumber caseNumber
     * @param hospitalIdent hospitalIdent
     * @return TCase
     */
    TCase findeCase(String caseNumber, String hospitalIdent);

    /**
     *
     * @param hCase merge TCase
     */
    void updateCase(TCase hCase);

    TCase findExistingCase(String hcase, String hospitalIdent);

}
