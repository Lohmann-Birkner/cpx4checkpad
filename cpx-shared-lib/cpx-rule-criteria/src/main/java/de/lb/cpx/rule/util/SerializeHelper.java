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
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.rule.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 *
 * @author wilde
 */
public class SerializeHelper {

    private SerializeHelper() {
        //utility class needs no public constructor
    }

    public static int getBytes(Object obj) throws IOException {
        return getBytes2(obj).length;
    }

    public static byte[] getBytes2(Object obj) throws IOException {
        ByteArrayOutputStream str2 = new ByteArrayOutputStream();
        byte[] serialized2;
        try ( ObjectOutputStream oos2 = new ObjectOutputStream(str2)) {
            oos2.writeObject(obj);
            serialized2 = str2.toByteArray();
        }
        return serialized2;
    }
}
