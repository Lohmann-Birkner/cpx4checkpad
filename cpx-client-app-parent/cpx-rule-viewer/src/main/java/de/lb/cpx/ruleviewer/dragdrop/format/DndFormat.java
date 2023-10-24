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
package de.lb.cpx.ruleviewer.dragdrop.format;

import javafx.scene.input.DataFormat;

/**
 * Format collection to handle drag and drop modes in application all sorts of
 * drag and drop events could occure, needs information to handle this things
 * differently
 *
 * @author wilde
 */
public class DndFormat {

    public static final DataFormat REORDER_FORMAT = new DataFormat("dataformat.reorder");
    public static final DataFormat ADD_FORMAT = new DataFormat("dataformat.add");

    /**
     * @return get the reorder format, should be used if the user wants to
     * rearrange nodes in the rule structure
     */
    public static DataFormat getReOrderFormat() {
        return REORDER_FORMAT;
    }

    /**
     * @return get the add format, should be used if the user wants to add new
     * values to the rule structure
     */
    public static DataFormat getAddFormat() {
        return ADD_FORMAT;
    }

}
