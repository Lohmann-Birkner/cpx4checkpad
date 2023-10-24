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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.commons.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

public final class TestUtil {

    private TestUtil() {
        // Since utility class
    }

    public static File createTestfileFromResources(final String testFilename) throws IOException {
        final InputStream in = TestUtil.class.getClassLoader().getResourceAsStream(testFilename);
        final File file = File.createTempFile("tmpTestFile", ".tmp");
        IOUtils.copy(in, new FileOutputStream(file));
        return file;
    }

}
