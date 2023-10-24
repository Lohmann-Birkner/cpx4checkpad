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
package de.lb.cpx.ruleviewer.model;

import de.lb.cpx.client.core.model.fx.tooltip.CpxTooltip;
import de.lb.cpx.client.core.util.ResourceLoader;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

/**
 *
 * @author wilde
 */
public class RuleMessageIndicator extends HBox{

    public RuleMessageIndicator() {
        super();
        Glyph glyph = ResourceLoader.getGlyph(FontAwesome.Glyph.EXCLAMATION).size(15);
        glyph.tooltipProperty().bind(tooltipProperty());
        glyph.setStyle("-fx-text-fill:orangered;");
        getChildren().add(glyph);
//        setPadding(new Insets(0, 10, 0, 0));
        setAlignment(Pos.CENTER);
        setFillHeight(true);
    }
    public RuleMessageIndicator(String pTooltipText){
        this();
        setTooltip(new CpxTooltip(pTooltipText, 100, 5000, 100, true));
    }
    private ObjectProperty<Tooltip> tooltipProperty;
    public final ObjectProperty<Tooltip> tooltipProperty(){
        if(tooltipProperty == null){
            tooltipProperty = new SimpleObjectProperty<>();
        }
        return tooltipProperty;
    }
    public void setTooltip(Tooltip pTip){
        tooltipProperty().set(pTip);
    }
    public Tooltip getTooltip(){
        return tooltipProperty().get();
    }
    
    
}
