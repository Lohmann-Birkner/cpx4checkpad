/*
 * Copyright (c) 2022 Lohmann & Birkner.
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
 *    2022  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.serviceutil.export;

/**
 *
 * @author gerschmann
 */
import de.lb.cpx.serviceutil.export.ProcessItemCallback;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.system.properties.CpxSystemProperties;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.Writer;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import org.apache.commons.text.StringEscapeUtils;




public class ProcessXmlItemCallback <T> implements ProcessItemCallback<Writer, T>{
    
    private XMLEventWriter eventWriter;
    private XMLEvent end;
    private XMLEventFactory eventFactory;
    private List<String> nodeNames;
    
private static final Logger LOG = Logger.getLogger(ProcessXmlItemCallback.class.getName());
    @Override
    public void call(Writer pWriter, int pNo, Object pDto) throws IOException, InterruptedException {

    }

    @Override
    public void writeRow(Writer pWriter, List pValues, int pRowNum) throws IOException {
        try{
        if(pRowNum == 0){
            nodeNames = new ArrayList<>(pValues);
        }
        else{
            if(pValues.size() != nodeNames.size()){
                throw new IOException("number of columns data is not equal number of columns");
            }
            try{
                createNodes(pValues);
            }catch(Exception ex){
                LOG.log(Level.SEVERE, " error on creating xml file", ex);
                throw new IOException(" error on creating xml file");
                
            }

        }
        }catch(Exception ex){
                LOG.log(Level.SEVERE, " error on creating xml file", ex);
                throw new IOException(" error on creating xml file");            
        }
    }

    @Override
    public Writer createWriter(File pTargetFile) throws IOException {
       final String charset = CpxSystemProperties.DEFAULT_ENCODING;
       final Writer listWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pTargetFile), "UTF-8"));
//       final Writer listWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pTargetFile), charset));
       createXmlEventWriter(listWriter);
       return listWriter;
  }

    @Override
    public void closeWriter(Writer pWriter) throws IOException {
        try{
            eventWriter.add(eventFactory.createEndElement("", "", "export"));
            eventWriter.add(eventFactory.createCharacters("\n"));
            eventWriter.add(eventFactory.createEndDocument());
            eventWriter.close();    
        try (pWriter) {
            pWriter.flush();
        }
        }catch(XMLStreamException ex){
            LOG.log(Level.SEVERE, " error on closing xml file", ex);
            throw new IOException(" error on closing xml file");
            
        }
    }

    private void createXmlEventWriter(Writer listWriter) throws IOException {
        try{
            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
            eventWriter = outputFactory
                    .createXMLEventWriter(listWriter);
            // create an EventFactory
            eventFactory = XMLEventFactory.newInstance();
            end = eventFactory.createDTD("\n");
            // create and write Start Tag
            StartDocument startDocument = eventFactory.createStartDocument();
//            StartDocument startDocument = eventFactory.createStartDocument(CpxSystemProperties.DEFAULT_ENCODING);
            eventWriter.add(startDocument);
            eventWriter.add(end);
            StartElement configStartElement = eventFactory.createStartElement("", "", "export");
            eventWriter.add(configStartElement);
            eventWriter.add(eventFactory.createAttribute("date", Lang.toDate(new Date())));
            eventWriter.add(eventFactory.createCharacters("\n"));

        }catch(XMLStreamException ex){
            LOG.log(Level.SEVERE, " error on creating xml file", ex);
            throw new IOException(" error on creating xml file");
        }
    }

    private void createNodes(List<Object> pValues) throws XMLStreamException {
        int i = 0;

        eventWriter.add(eventFactory.createStartElement("", "", "line"));
        eventWriter.add(eventFactory.createCharacters("\n"));
        for(String name: nodeNames){
            eventWriter.add(eventFactory.createStartElement("", "", "column"));
            eventWriter.add(eventFactory.createAttribute("header", name));
            eventWriter.add(eventFactory.createAttribute("value", convertValue2String(pValues.get(i))));
            eventWriter.add(eventFactory.createEndElement("", "", "column"));
            eventWriter.add(eventFactory.createCharacters("\n"));
            i++;                 
        }
        eventWriter.add(eventFactory.createEndElement("", "", "line"));        
       
       // create End node

        eventWriter.add(eventFactory.createCharacters("\n"));
    }
    
    private String convertValue2String (Object field){
        if (field instanceof String) {
          return checkXml(field.toString());
       } else if (field instanceof Integer) {
           return String.valueOf(field);
       } else if (field instanceof Boolean) {
           if ((Boolean) field) {
               return"Ja";
           } else {
               return "Nein";
           }
       } else if (field instanceof Date) {
           DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
           Date input = (Date) field;
           LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//                        builder.append(date).append(DEFAULT_SEPARATOR); // gives date format based on zone and system language
           return dateFormatter.format(date);
       } else if (field instanceof Calendar) {
           return field.toString();
       } else if (field instanceof Long) {
           field.toString();
       } else if (field instanceof Double) {
           return Lang.toDecimal((Double)field);
       } else if (field instanceof Float) {
           return field == null?"":Lang.toDecimal(((Float)field).doubleValue());
       }
       return field == null?"":field.toString();         

    }

    private String checkXml(String toString) {
        return StringEscapeUtils.escapeXml11(toString);
    }
    
    private class Column{
        private String name;
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
    
    public static void main(String[]args) throws Exception{
        
         File output = new File("d:\\temp\\test.xml");
        if(output.exists()){
            output.delete();
        } 
        ProcessItemCallback<Writer, Serializable> test = new ProcessXmlItemCallback<>();

        Writer writer = test.createWriter(output);
        List<Object> titles = new ArrayList<>();
        titles.add("ik des Krankenhauses");
        titles.add("Fall/nummer");
        titles.add("Aufnahmedatum");
        titles.add("Aufnahmegrund");
        List<Object> data = new ArrayList<>();     
        data.add("999999999");
        data.add("12345");
        data.add(new Date());
        data.add("01");
        test.writeRow(writer, titles, 0);
        for(int i = 1; i < 100; i++){
            test.writeRow(writer, data, i);
        }

        test.closeWriter(writer);
        System.exit(0);
    }
}
