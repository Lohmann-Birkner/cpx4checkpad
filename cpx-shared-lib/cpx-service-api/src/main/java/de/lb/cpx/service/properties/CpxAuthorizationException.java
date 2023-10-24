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
 *    2019  Shahin - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.properties;

/**
 *
 * @author Shahin
 */
public class CpxAuthorizationException extends RuntimeException {

    private static final long serialVersionUID = 1L;
//    private static final Logger LOG = Logger.getLogger(CpxAuthorizationException.class.getName());
    private final long roleId;
    private final String roleName;

//
    public CpxAuthorizationException(final RoleProperties pRoleProperties, final String pReason) {
        super(pReason);
        roleName = pRoleProperties == null ? "" : pRoleProperties.getName();
        roleId = pRoleProperties == null ? 0L : pRoleProperties.getId();
    }

//    private CpxAuthorizationException() {
//        super();
//        roleName = "-";
//    }
//    private CpxAuthorizationException(String message) {
//        super(message);
//        roleName = "-";
//    }
//
//    private CpxAuthorizationException(String message, Throwable cause) {
//        super(message, cause);
//        roleName = "-";
//    }
//
//    private CpxAuthorizationException(Throwable cause) {
//        super(cause);
//        roleName = "-";
//    }
    public String getRoleName() {
        return roleName;
    }

    public long getRoleId() {
        return roleId;
    }

}
