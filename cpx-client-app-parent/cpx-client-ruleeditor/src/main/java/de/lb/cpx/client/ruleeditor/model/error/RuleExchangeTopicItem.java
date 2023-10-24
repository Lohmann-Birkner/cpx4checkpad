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
package de.lb.cpx.client.ruleeditor.model.error;

import de.lb.cpx.client.core.model.fx.alert.AlertHelper;
import de.lb.cpx.client.core.model.fx.dialog.accordion.AccordionTopicItem;
import de.lb.cpx.client.core.model.fx.tooltip.CpxTooltip;
import de.lb.cpx.server.rule.services.RuleExchangeError;
import de.lb.cpx.shared.rules.util.RuleValidationStatusEn;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import org.controlsfx.glyphfont.Glyph;

/**
 *
 * @author wilde
 */
public class RuleExchangeTopicItem extends AccordionTopicItem{
    private static final double ICON_SIZE = 18.0d;
    public RuleExchangeTopicItem(RuleExchangeError pError){
        super();
        setGraphic(createGraphic(pError));
        setText(createTitle(pError));
        setDescription(createDescription(pError));
    }
    
    public final Node createGraphic(RuleExchangeError pError){
        Glyph glyph;
        RuleValidationStatusEn.ErrPrioEn statusEn = null;
        if(pError == null || pError.getRuleValidationStatusEn() == null){
            glyph = AlertHelper.getAlertIcon(Alert.AlertType.NONE, ICON_SIZE);
        }else{
            glyph = AlertHelper.getAlertIcon(pError.getRuleValidationStatusEn().getErrPrio(), ICON_SIZE);
            statusEn = pError.getRuleValidationStatusEn().getErrPrio();
        }
        glyph.setTooltip(new CpxTooltip(AlertHelper.getAlertText(statusEn), 100, 5000, 100, true));
        return glyph;
    }
    
    public final String createTitle(RuleExchangeError pError){
        if(pError == null){
            return "No error set";
        }
        StringBuilder titleBuilder = new StringBuilder();
        if(pError.getXmlRuleIdent()!= null && !pError.getXmlRuleIdent().isEmpty()){
            titleBuilder.append("Regel-Fehler (");
            titleBuilder.append(pError.getXmlRuleIdent());
            titleBuilder.append(")");
        }else{
            titleBuilder.append("Datei-Fehler");
        }
        titleBuilder.append(" - ");
        titleBuilder.append(pError.getRuleValidationStatusEn()!=null?pError.getRuleValidationStatusEn().getTranslation():"----");
        return titleBuilder.toString();
    }
    
    public final String createDescription(RuleExchangeError pError){
        if(pError == null){
            return "No error set";
        }
        StringBuilder descBuilder = new StringBuilder();
        descBuilder.append("Die Regel: ");
        descBuilder.append(pError.getXmlRuleIdent());
        descBuilder.append(" konnte nicht importiert werden!\n\n");
        descBuilder.append("Der Grund dafür war, dass ");
        descBuilder.append(getValidationDescription(pError.getRuleValidationStatusEn()));
        if(pError.getTextFromXml()!=null && !pError.getTextFromXml().isEmpty()){
            descBuilder.append("\n");
            descBuilder.append("Regeldefinition:\n");
            descBuilder.append(pError.getTextFromXml());
        }
        return descBuilder.toString();
    }

    private String getValidationDescription(RuleValidationStatusEn pStatusEn) {
        if(pStatusEn == null){
            return "ein unbekannter Fehler aufgetreten ist.";
        }
        switch(pStatusEn){
            case RULE_HAS_ERRORS:
                return "die Regel fehlerhaft ist.";
            case SAME_RULE_FOUND:
                return "die Regel bereits in dem ausgewählten Pool existiert.";
            case NO_RULES_4_IMPORT_FOUND:
                return "in der ausgewählten Datei keine Regeln gefunden werden konnten.";
            default:
                return "ein unbekannter Fehler aufgetreten ist (Fehler-Kode: "+pStatusEn.name()+")";
        }
    }
}
