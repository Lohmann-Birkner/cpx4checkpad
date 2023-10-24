/*
 * Copyright (c) 2021 Lohmann & Birkner.
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
 *    2021  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.Gdv;

import java.util.List;
import javax.ejb.Local;
import org.jboss.ejb3.annotation.SecurityDomain;

/**
 *
 * @author gerschmann
 */
@Local
@SecurityDomain(value = "cpx")
public interface GdvResponseLocal {

    public String createGdvAnswer(String database, String patientNumber, List<String > attachmentPath, String pDestPath) throws Exception;

    public String createGdvBill(String database, String patientNumber,  List<String > attachmentPath, String pDestPath) throws Exception;
    
}
