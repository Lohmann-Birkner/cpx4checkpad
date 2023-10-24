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
package de.lb.cpx.service.properties;

import de.lb.cpx.system.properties.CpxSystemProperties;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Dirk Niemeier
 */
public class XmlSerializer {

    private static final Logger LOG = Logger.getLogger(XmlSerializer.class.getName());

    private XmlSerializer() {
        //utility class needs no public constructor
    }

    public static String serialize(Object obj) {
        try {
            if (obj == null) {
                return "";
            }
            StringWriter writer = new StringWriter();
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller m = context.createMarshaller();
            //m.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.TRUE);
            //m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.FALSE);
            m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            //m.setProperty("com.sun.xml.internal.bind.indentString", "  "); //2 instead of 4 spaces for indentation
            m.marshal(obj, writer);
            return writer.toString();
        } catch (JAXBException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static <T> T deserialize(final File pFile, final Class<?> pClass) throws IOException {
        if (pFile == null) {
            throw new IllegalArgumentException("File to deserialize cannot be null!");
        }
        if (!pFile.exists()) {
            throw new IllegalArgumentException("File to deserialize does not exist: " + pFile.getAbsolutePath());
        }
        if (!pFile.canRead()) {
            throw new IllegalArgumentException("Cannot read from file to deserialize: " + pFile.getAbsolutePath());
        }
        if (!pFile.isFile()) {
            throw new IllegalArgumentException("Path to file to deserialize is a directory but not a file: " + pFile.getAbsolutePath());
        }
        byte[] encoded = Files.readAllBytes(pFile.toPath());
        final String content = new String(encoded, CpxSystemProperties.DEFAULT_ENCODING);
        T obj = deserialize(content, pClass);
        if (obj == null) {
            LOG.log(Level.WARNING, "deserialized object is null -> content to deserialize was: {0}", content);
        }
        return obj;
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialize(final String pInput, final Class<?> pClass) {
        try {
            if (pInput == null || pInput.trim().isEmpty()) {
                return null;
            }
            JAXBContext context = JAXBContext.newInstance(pClass);
            Unmarshaller m = context.createUnmarshaller();
            return (T) m.unmarshal(new StringReader(pInput));
        } catch (JAXBException ex) {
            LOG.log(Level.SEVERE, "Unable to deserialize this input of class " + (pClass == null ? "-" : pClass.getName()) + ":\n" + pInput, ex);
        }
        return null;
    }

}
