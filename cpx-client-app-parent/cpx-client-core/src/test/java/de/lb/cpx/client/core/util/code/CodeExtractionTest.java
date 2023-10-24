/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.util.code;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author wilde
 */
public class CodeExtractionTest {
    @Test
    public void testCode(){
        boolean codeTest = !CodeExtraction.getIcdCode("S7.0").isEmpty();
        Assert.assertEquals(true, codeTest);
        boolean codeTest1 = !CodeExtraction.getIcdCode("S?.2*").isEmpty();
        Assert.assertEquals(true, codeTest1);
    }
}
