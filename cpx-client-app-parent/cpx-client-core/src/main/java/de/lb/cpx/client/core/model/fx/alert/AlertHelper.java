/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.alert;

import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.rules.util.RuleValidationStatusEn;
import java.util.Objects;
import javafx.scene.control.Alert;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

/**
 *
 * @author wilde
 */
public class AlertHelper {
    
    public static final String getAlertText(Alert.AlertType pAlertType){
         switch (pAlertType) {
            case CONFIRMATION:
                return Lang.getConformation();
            case INFORMATION:
                return Lang.getInformation();
            case WARNING:
                return Lang.getWarning();
            case ERROR:
                return Lang.getError();
            default:
                return "Not set";
        }
    }
    public static final String getAlertText(RuleValidationStatusEn.ErrPrioEn pPrio){
        if(pPrio == null){
            return "No error set";
        }
        switch(pPrio){
            case INFO:
                return getAlertText(Alert.AlertType.INFORMATION);
            case SEVERE:
                return getAlertText(Alert.AlertType.ERROR);
            case WARNING:
                return getAlertText(Alert.AlertType.WARNING);
            default:
                return "Unbekannter Fehler-Code: "+pPrio.name();
        }
    }
    public static final Glyph getAlertIcon(RuleValidationStatusEn.ErrPrioEn pPrio, double pFontSize){
        switch(pPrio){
            case INFO:
                return getAlertIcon(Alert.AlertType.INFORMATION, pFontSize);
            case SEVERE:
                return getAlertIcon(Alert.AlertType.ERROR, pFontSize);
            case WARNING:
                return getAlertIcon(Alert.AlertType.CONFIRMATION, pFontSize);
            default:
                Glyph g =  getDefaultGlyph();
                if(g!= null){
                    g.setFontSize(pFontSize);
                }
                return g;
        }
    }
    
    public static final Glyph getAlertIcon(Alert.AlertType pAlertType, double pFontSize) {
        pAlertType = Objects.requireNonNullElse(pAlertType, Alert.AlertType.NONE);
        final Glyph g;
        switch (pAlertType) {
            case CONFIRMATION:
                g = getConfirmationGlyph();
                break;
            case INFORMATION:
                g = getInformationGlyph();
                break;
            case WARNING:
                g = getWarningGlyph();
                break;
            case ERROR:
                g = getErrorGlyph();
                break;
            default:
                //Unspecified alert type (NONE or null!)
                g = getDefaultGlyph();
                break;
        }
        if (g != null) {
            g.setFontSize(pFontSize);
        }
        return g;
    }
    private static Glyph getConfirmationGlyph(){
        return getGlyph(FontAwesome.Glyph.QUESTION_CIRCLE, "-fx-text-fill: #1569a6;");
    }
    private static Glyph getInformationGlyph(){
        return getGlyph(FontAwesome.Glyph.EXCLAMATION_TRIANGLE, "-fx-text-fill: #f6a117;");
    }
    private static Glyph getWarningGlyph(){
        return getGlyph(FontAwesome.Glyph.EXCLAMATION_TRIANGLE, "-fx-text-fill: #f6a117;");
    }
    private static Glyph getErrorGlyph(){
        return getGlyph(FontAwesome.Glyph.EXCLAMATION_CIRCLE, "-fx-text-fill: #ca2424;");
    }
    private static Glyph getDefaultGlyph(){
        return getGlyph(FontAwesome.Glyph.BELL, "-fx-text-fill: #1569a6;");
    }
    private static Glyph getGlyph(FontAwesome.Glyph pGlyph, String pColor){
        Glyph g = ResourceLoader.getGlyph(pGlyph);
        if (g != null) {
            g.setStyle(pColor);
        }
        return g;
    }
}
