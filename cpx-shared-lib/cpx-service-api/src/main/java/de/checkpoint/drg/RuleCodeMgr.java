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
package de.checkpoint.drg;

import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import java.io.File;

/**
 *
 * @author gerschmann
 */
public class RuleCodeMgr {

    public static String getCatalogPath() {
        CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();
        return new File(cpxProps.getCpxServerCatalogDir()).getAbsolutePath() + File.separatorChar;

//        return "site" + File.separator + "catalog" + File.separator;
    }

    public static String getRulesPath() {
        CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();
        return cpxProps.getCpxServerRulesDir();
    }
}
