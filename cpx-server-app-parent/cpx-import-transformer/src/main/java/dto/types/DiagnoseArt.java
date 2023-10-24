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
package dto.types;

/**
 *
 * @author Dirk Niemeier
 */
/*
public enum DiagnoseArt {
  AUFNAHMEDIAGNOSE("A"),
  EINWEISUNGSDIAGNOSE("E"),
  PRIMAERDIAGNOSE("P"),
  SEKUNDAERDIAGNOSE("S");

  public final String value;
  private static final Map<String, DiagnoseArt> lookup = new HashMap<>();
  
  static {
    for (DiagnoseArt value : DiagnoseArt.values()) {
      lookup.put(value.getValue().trim().toLowerCase(), value);
    }
  }

  public String getValue() {
    return value;
  }

  DiagnoseArt(final String pValue) {
    value = (pValue == null)?"":pValue.trim().toUpperCase();
  }

  public static DiagnoseArt findByValue(final String pValue) {
    if (pValue == null) {
      throw new CpxIllegalArgumentException("Value is null!");
    }
    return lookup.get(pValue.trim().toLowerCase());
  }
  
  @Override
  public String toString() {
    return getValue();
  }
}
 */
