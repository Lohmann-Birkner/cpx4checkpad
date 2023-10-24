/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.menu.fx;

import de.lb.cpx.client.core.easycoder.EasyCoderDialog;
import de.lb.cpx.client.core.menu.model.ToolbarSingleMenuItem;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.model.fx.tooltip.BasicTooltip;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import org.controlsfx.glyphfont.FontAwesome;

/**
 *
 * @author wilde
 */
public class ToolbarMenuScene extends BasicToolbarMenuScene {

    private int selectedYear = 0;

    public ToolbarMenuScene() throws IOException, CpxIllegalArgumentException {
        super();

        final ToolbarSingleMenuItem coder = new ToolbarSingleMenuItem(getController().getToolbar());
        coder.setExtendedTitle("EasyCoder");
        coder.setGlyph(FontAwesome.Glyph.TERMINAL);
        coder.setTooltip(new BasicTooltip("EasyCoder starten", 0, 5000, 200, true));
        coder.setOnAction((arg0) -> {
            final Date today = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(today);
            int currentYear = cal.get(Calendar.YEAR);
            if (selectedYear == 0) {
                selectedYear = currentYear;
            }
            AlertDialog dlg = AlertDialog.createConfirmationDialog("Katalogjahr auswählen");
            final ListView<Integer> yearList = new ListView<>();
            final int minYear = 2004;
            final int maxYear = currentYear + 2;
            for (int year = minYear; year <= maxYear; year++) {
                yearList.getItems().add(year);
            }
            yearList.getSelectionModel().select((Integer) selectedYear);
            yearList.scrollTo((Integer) selectedYear);
            dlg.getDialogPane().setContent(yearList);
            dlg.getDialogPane().getStyleClass().add("stay-selected-list-view");
            Optional<ButtonType> result = dlg.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                selectedYear = yearList.getSelectionModel().getSelectedItem();
                cal.set(Calendar.YEAR, selectedYear);
                EasyCoderDialog easyCoder = new EasyCoderDialog(getController().getScene().getWindow(), Modality.APPLICATION_MODAL, "ICD-, OPS-Bearbeitung", cal.getTime());
                easyCoder.initOwner(getController().getScene().getOwner());
                easyCoder.showAndWait();
            }
            //return easyCoder;
        });
        getController().getToolbar().add(0, coder);
    }

}
