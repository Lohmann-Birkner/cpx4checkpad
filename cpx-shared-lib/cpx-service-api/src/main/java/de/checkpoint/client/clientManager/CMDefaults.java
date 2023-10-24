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
 *    2016  gerschmann - initial API and implementation and/or initial documentation
 */
package de.checkpoint.client.clientManager;

/**
 * This class will be used in CPX instead of the same class of Checkpoint in
 * order to hide or override some not used properties
 *
 * @author gerschmann
 */
public class CMDefaults {

    private static CMDefaults m_defaults;

    public static synchronized CMDefaults defaultsMgr() {
        if (m_defaults == null) {
            m_defaults = new CMDefaults();
        }
        return m_defaults;
    }

    /**
     * later will be read from properties?
     *
     * @return boolean if is Eska-Mode or not
     */
    public boolean isESKA() {
        return false;
    }

}
