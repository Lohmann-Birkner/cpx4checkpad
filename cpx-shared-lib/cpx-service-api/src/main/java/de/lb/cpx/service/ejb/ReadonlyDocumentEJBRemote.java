/*
 * Copyright (c) 2022 Lohmann & Birkner.
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
 *    2022  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.lb.cpx.shared.enums.ReadonlyDocumentsEn;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import javax.annotation.security.PermitAll;
import javax.ejb.Remote;
import org.jboss.ejb3.annotation.SecurityDomain;

/**
 *
 * @author gerschmann
 */
@Remote
@SecurityDomain("cpx")
@PermitAll
public interface ReadonlyDocumentEJBRemote {
        /**
     * get help file (PDF) from server
     *
     * @param pType
     * @return CPX help document
     * @throws IOException document is missing or corrupted
     */

    byte[] getDocumentContent(ReadonlyDocumentsEn pType, String pYear)throws IOException;

    List<String> getCatalogYears(ReadonlyDocumentsEn pType)  throws RemoteException;

    
}
