/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.rule.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * Loads XML file to Object
 *
 * @author wilde
 */
public class XMLHandler {

    private XMLHandler() {
        //utility class needs no public constructor
    }

    public static Object unmarshalXML(URL pUrl, Class<?> pClazz, Object pFactory) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(pClazz);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        //needed to create som costum extended object due to changes may occure in pojo in the future
        //if pojo remains stable, it could be removed and methodes from criteria object could be moved to pojo
        if (pFactory != null) {
            //unmarshaller.setProperty("com.sun.xml.internal.bind.ObjectFactory", pFactory);
            unmarshaller.setProperty("com.sun.xml.bind.ObjectFactory", pFactory);
        }
//        File xml = new File(getClass().getResource(BASIC_CRITERIA_RESOURCE).toExternalForm());
        return unmarshaller.unmarshal(pUrl);
    }

    public static Object unmarshalXML(URL pURL, Class<?> pClazz) throws JAXBException {
        return XMLHandler.unmarshalXML(pURL, pClazz, null);
    }

    public static Object unmarshalXML(String pContent, Class<?> pClazz, Object pFactory) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(pClazz);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        //needed to create som costum extended object due to changes may occure in pojo in the future
        //if pojo remains stable, it could be removed and methodes from criteria object could be moved to pojo
        if (pFactory != null) {
            unmarshaller.setProperty("com.sun.xml.internal.bind.ObjectFactory", pFactory);
        }
        return unmarshaller.unmarshal(new StringReader(pContent));
    }

    public static Object unmarshalXML(String pContent, Class<?> pClazz) throws JAXBException {
        return XMLHandler.unmarshalXML(pContent, pClazz, null);
    }
    public static String marshalXML(Object pContent, Class<?> pClazz) throws JAXBException {
        return marshalXML(pContent, pClazz, "UTF-16");
    }
    public static String marshalXML(Object pContent, Class<?> pClazz, String pEncoding) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(pClazz);
        Marshaller marshaller = jc.createMarshaller();
        //JAXB Fragment true if header should be removed
        marshaller.setProperty(Marshaller.JAXB_ENCODING, pEncoding);
//        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

        StringWriter writer = new StringWriter();
        marshaller.marshal(pContent, writer);
        String xml = writer.toString();
        //could be replaced if xml header is obsolete?
        xml = xml.replaceAll(" standalone=\"yes\"", "");
        return xml;
    }
}
