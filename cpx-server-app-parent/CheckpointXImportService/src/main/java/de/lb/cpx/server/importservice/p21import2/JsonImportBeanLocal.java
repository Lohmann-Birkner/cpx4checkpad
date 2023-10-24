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
package de.lb.cpx.server.importservice.p21import2;

import de.lb.cpx.shared.dto.LockException;
import java.util.Map;
import javax.ejb.Local;
import javax.json.JsonObject;
import javax.security.auth.login.LoginException;
import org.jboss.ejb3.annotation.SecurityDomain;

/**
 *
 * @author Dirk Niemeier
 */
@Local
@SecurityDomain(value = "cpx")
public interface JsonImportBeanLocal {

    JsonObject startImport(final String pDirectoryName, final String pDatabase, final String pCheckType, final Integer pThreadCount, final boolean pDoGroup, final String pGrouperModel, final String pModule);

    Long startGrouping(final String pDatabase) throws LockException, LoginException;
    
//    Long startBillImport(final String pDatabase, final String pDirectory) throws LockException, LoginException;

    int getNewClientId();

    //boolean login(final String pClientId, final String pUserName, final String pHashedPassword, final CpxSystemPropertiesInterface cpxClientSystemProperties, final AppTypeEn pAppTypeEn) throws LoginException;

    boolean login(final String pClientId, final String pUserName, final String pHashedPassword, final String pDatabase, final String pAppTypeEn) throws LoginException;
    
    String getUserRoleProperties(final String pClientId);
    long getUserId(final String pClientId);
    Map<String,Map<String,String>> getPersistenceUnits(final String pClientId);
}
