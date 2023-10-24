/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package dto.types;

/**
 *
 * @author niemeier
 */
public class IcdResult {

    public final String code;
    public final RefIcdType refType;

//    public IcdResult(final String pCode, final RefIcdType pRefType) {
//        this.code = pCode == null ? "" : pCode.trim();
//        this.refType = pRefType;
//    }
    public IcdResult(final String pCode) {
        final String c = pCode == null ? "" : pCode.trim();
        if (c.isEmpty()) {
            this.code = c;
            this.refType = null;
            return;
        }

        char lastChar = c.charAt(c.length() - 1);
        RefIcdType type = null;
        for (RefIcdType val : RefIcdType.values()) {
            if (lastChar == val.getValue()) {
                type = val;
                break;
            }
        }

        if (type == null) {
            this.code = c;
            this.refType = null;
        } else {
            this.refType = type;
            this.code = c.substring(0, c.length() - 1).trim();
        }
    }
}
