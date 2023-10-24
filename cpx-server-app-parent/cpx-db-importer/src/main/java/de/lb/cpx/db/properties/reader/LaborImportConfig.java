/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.db.properties.reader;


import java.io.File;
import java.nio.file.FileSystemException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author gerschmann
 */
public class LaborImportConfig extends ImportConfig{
    
private final static String PROPERTIES_LABOR_NAME = "labor";
        
@Override
public File getXmlConfigFile() {

    return super.getXmlConfigFile(PROPERTIES_LABOR_NAME);
}


    public void createConfigFile() throws FileSystemException {
        createConfigFile(PROPERTIES_LABOR_NAME);
    }

    public Map<String, String> getLaborGroupHash() {
        Map<String, String> result = new HashMap<>();
        Iterator<String> keys = getXmlConfig().getKeys();
        String key = null;
        while (keys.hasNext()) {
            key = keys.next();
            String group = getXmlConfig().getString(key);
            if (group.isEmpty()) {
                group = "Sonstige";
            }
            group = group.replace("_", " ");
            key = key.replace("_", " ");
            result.put(key, group);
        }
        return result;
    }

    
}
