/* 
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.serviceutil.export;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author niemeier
 * @param <W> writer type
 * @param <T> search list dto type
 */
public interface ProcessItemCallback<W extends Closeable, T> {

    void call(W pWriter, int pNo, T pDto) throws IOException, InterruptedException;

    void writeRow(W pWriter, List<Object> pValues, final int pRowNum) throws IOException;

    W createWriter(File pTargetFile) throws IOException;

    void closeWriter(W pWriter) throws IOException;
}
