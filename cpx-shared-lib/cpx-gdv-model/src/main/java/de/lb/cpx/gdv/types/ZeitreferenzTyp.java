//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2021.08.19 um 11:54:00 AM CEST 
//


package de.lb.cpx.gdv.types;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für ZeitreferenzTyp.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="ZeitreferenzTyp">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;maxLength value="4"/>
 *     &lt;minLength value="1"/>
 *     &lt;enumeration value="MESZ"/>
 *     &lt;enumeration value="MEZ"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ZeitreferenzTyp")
@XmlEnum
public enum ZeitreferenzTyp {


    /**
     * Uhrzeitangaben gemäß MESZ
     * 
     */
    MESZ,

    /**
     * Uhrzeitangaben gemäß MEZ
     * 
     */
    MEZ;

    public String value() {
        return name();
    }

    public static ZeitreferenzTyp fromValue(String v) {
        return valueOf(v);
    }

}
