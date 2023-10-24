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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.checkpoint.enums;

import java.io.Serializable;

/**
 * Enum to store Pooltypes
 *
 * @author wilde
 */
public enum AppTypeEn implements Serializable {

    CORE("Core", "CPX Core", true),
    CLIENT("Client", "CPX Client", true),
    RULE_EDITOR("Rule Editor", "CPX Regeleditor", false),
    WEB_APP("WebApp", "CPX WebApp", false),
    ANALYTICS("Analytics", "CPX Analytics", false);

    //DON'T USE LOGGER HERE, OTHERWISE LOG4J2 CANNOT BE INITIATED IN CLIENT, COMMAND setType(AppTypeEn.CORE) BREAKS EVERYTHING!
    //private static final Logger LOG = Logger.getLogger(AppTypeEn.class.getName());

    private final String title;
    private final String description;
    private final boolean needsDatabase;

    private AppTypeEn(String pTitle, String pDescription, final boolean pNeedsDatabase) {
        title = pTitle;
        description = pDescription;
        needsDatabase = pNeedsDatabase;
    }

    public static AppTypeEn findByName(final String pName) {
        final String name = pName == null ? "" : pName.trim();
        if (name.isEmpty()) {
            return null;
        }
        for (AppTypeEn val : values()) {
            if (val.name().equalsIgnoreCase(name)) {
                return val;
            }
        }
        //LOG.log(Level.WARNING, "was not able to detect app type: {0}", pName);
        return null;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCore() {
        return CORE.equals(this);
    }

    public boolean isClient() {
        return CLIENT.equals(this);
    }

    public boolean isRuleEditor() {
        return RULE_EDITOR.equals(this);
    }

    public boolean isWebApp() {
        return WEB_APP.equals(this);
    }

    public boolean getNeedsDatabase() {
        return needsDatabase;
    }

}
