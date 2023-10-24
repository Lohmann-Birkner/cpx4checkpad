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
 *    2017  adameck - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.labeled;

import de.lb.cpx.client.core.model.fx.spinner.CpxTimeSpinner;
import de.lb.cpx.shared.lang.Lang;
import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.StringConverter;

/**
 *
 * Labeled TimeSpinner class handles a CpxTimeSpinner and an corresponting label
 *
 * @author adameck
 */
public class LabeledTimeSpinner extends LabeledControl<CpxTimeSpinner> {

    private static final Logger LOG = Logger.getLogger(LabeledTimeSpinner.class.getName());

    public LabeledTimeSpinner() {
        this("Label");
    }

    /**
     * creates a new timeSpinner with that Label
     *
     * @param pLabel label
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException invalid argument
     */
    public LabeledTimeSpinner(String pLabel) {
        super(pLabel, new CpxTimeSpinner("00:00"));
        controlProperty.getValue().setConverter(new StringConverter<LocalTime>() {
            @Override
            public String toString(LocalTime date) {
                if (date != null) {
                    try {
                        return DateTimeFormatter.ofPattern(Lang.getTimeFormat()).format(date);
                    } catch (DateTimeException ex) {
                        LOG.log(Level.FINER, "Date is not valid!", ex);
//                        showErrorPopOver(ex.getMessage());
                        showErrorPopOver(Lang.getInputTimeError(date));
                    }
                }
                return "";
            }

            @Override
            public LocalTime fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    try {
                        LocalTime time = LocalTime.parse(string, DateTimeFormatter.ofPattern(Lang.getTimeFormat()));
                        getControl().getTimeProperty().set(time);
                        return time;
                    } catch (DateTimeParseException ex) {
                        LOG.log(Level.FINER, "Date is not valid!", ex);
//                         showErrorPopOver(ex.getMessage());
                        showErrorPopOver(Lang.getInputTimeError(string));
                    }
                }
                getControl().setText(toString(getControl().getLocalTime()));
                return getControl().getLocalTime();
            }
        });

    }

}
