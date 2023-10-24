/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package dto;

import dto.impl.Case;
import dto.impl.Patient;
import java.io.Serializable;
import java.util.Set;
import line.AbstractLine;
import line.LineI;

/**
 *
 * @author Dirk Niemeier
 */
public interface DtoI extends Serializable {

    LineI toLine();

    String toCsv();

    /*
  public void fromCsv(final String pCsvData);
     */
    //public void toLine(final LineI pLine);
    //public void fromLine(final LineI pLine);
    Class<? extends AbstractLine> getLineClass();

    //public String getCaseKey();
    //public boolean hasValues() throws InstantiationException, IllegalAccessException;
    Set<LineI> getLines();

    Set<DtoI> getDtos();

    String getIkz();

    String getFallNr();

    String getPatNr();

    Case getCase();

    Patient getPatient();

    String getCaseKey();

    String getPatientKey();

}
