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
package de.lb.cpx.service.ejb;

import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author gerschmann
 */
@Remote
public interface GdvResponseRemote {

    public String createGdvAnswer(String patientNumber, List<String> attachmentPath, String pDestPath) throws Exception;

    public String createGdvBill(String patientNumber, List<String> attachmentPath, String pDestPath)throws Exception;
    
    public void createSession(String database);
    
}
