/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.db.properties.reader;

import de.lb.cpx.config.AbstractCpxConfig;
import de.lb.cpx.config.ExtendedXMLConfiguration;
import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import java.io.File;
import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.ConfigurationBuilderEvent;
import org.apache.commons.configuration2.builder.ReloadingFileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.event.EventListener;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.tree.xpath.XPathExpressionEngine;

/**
 *
 * @author gerschmann
 */
public abstract class ImportConfig  extends AbstractCpxConfig{
    
    protected ReloadingFileBasedConfigurationBuilder<ExtendedXMLConfiguration> xmlBuilder = null;
    private static final CpxSystemPropertiesInterface CPX_PROPS = CpxSystemProperties.getInstance();
    
    public static final String PROPERTIES_DIR = "dbImport";
    

    public synchronized ExtendedXMLConfiguration getXmlConfig() {
        if (xmlBuilder == null) {
            //CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();
            //File configFile = new File(cpxProps.getCpxServerConfigFile());      
            File configFile = getXmlConfigFile();
//      try {
//        createXmlConfigFile(configFile.getAbsolutePath());
//      } catch (FileSystemException ex) {
//        Logger.getLogger(CpxServerConfig.class.getName()).log(Level.SEVERE, null, ex);
//        return null;
//      }
            Parameters params = new Parameters();
            final ReloadingFileBasedConfigurationBuilder<ExtendedXMLConfiguration> builder
                    = new ReloadingFileBasedConfigurationBuilder<>(ExtendedXMLConfiguration.class)
                            .configure(params.xml()
                                    .setThrowExceptionOnMissing(false)
                                    .setValidating(false)
                                    .setEncoding("UTF-8")
                                    .setFile(configFile)
                                    .setExpressionEngine(new XPathExpressionEngine()));
            builder.setAutoSave(true);

            /*
      PeriodicReloadingTrigger trigger = new PeriodicReloadingTrigger(builder.getReloadingController(),
          null, 1, TimeUnit.MINUTES);
      trigger.start();
             */
            // Register an event listener for triggering reloading checks
            builder.addEventListener(
                    ConfigurationBuilderEvent.CONFIGURATION_REQUEST,
                    new EventListener<ConfigurationBuilderEvent>() {
                @Override
                public void onEvent(ConfigurationBuilderEvent event) {
                    builder.getReloadingController().checkForReloading(null);
                }
            });

            /*
      // Register an event listener for triggering reloading checks
      builder.addEventListener(ConfigurationEvent.SET_PROPERTY,
        new EventListener() {
          @Override
          public void onEvent(Event event) {
            System.out.println("CHANGED!");
          }
        });
             */
            xmlBuilder = builder;
        }
        try {
            return xmlBuilder.getConfiguration();
        } catch (ConfigurationException ex) {
            String msg = "Server configuration file seems to be corrupted: " + (xmlBuilder.getFileHandler() == null || xmlBuilder.getFileHandler().getFile() == null ? "null" : xmlBuilder.getFileHandler().getFile().getAbsolutePath());
            //LOG.log(Level.SEVERE, msg, ex);
            throw new IllegalStateException(msg, ex);
        }
    }

    @Override
    public synchronized List<Configuration> getConfigs() {
        List<Configuration> configs = new ArrayList<>();
        configs.add(getXmlConfig());
//        configs.add(getDbConfig());
        return configs;
    }


    public File getXmlConfigFile(String configName) {
        
        String configPath = CPX_PROPS.getCpxHome() + PROPERTIES_DIR + File.separator + configName + "_properties.xml"; 
        final File configFile = new File(configPath);
        if (!configFile.exists() ) {
            throw new IllegalArgumentException("Property file does not exist: " + configFile.getAbsolutePath());
        }
        if (!configFile.isFile()) {
            throw new IllegalArgumentException("This is a directory and not a property file: " + configFile.getAbsolutePath());
        }
        if (!configFile.canRead()) {
            throw new IllegalArgumentException("No permission to read from property file: " + configFile.getAbsolutePath());
        }


        return configFile;
    }

    public abstract File getXmlConfigFile() ;
    
    public void createConfigFile(String pConfigName) throws FileSystemException {
        String configPath = CPX_PROPS.getCpxHome() + PROPERTIES_DIR + File.separator + pConfigName + "_properties.xml"; 
        createXmlConfigFile(configPath);
    }
}
