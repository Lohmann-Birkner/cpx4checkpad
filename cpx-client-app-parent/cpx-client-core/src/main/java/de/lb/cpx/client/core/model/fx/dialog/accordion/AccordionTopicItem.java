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
package de.lb.cpx.client.core.model.fx.dialog.accordion;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;

/**
 *
 * @author wilde
 */
public class AccordionTopicItem extends TitledPane{
    
    public AccordionTopicItem(){
        super();
        setAnimated(false);
        TextArea description = new TextArea();
        description.setWrapText(true);
        description.setMinHeight(150.0d);
        description.textProperty().bind(descriptionProperty());
        description.setEditable(false);
        setContent(description);
    }
    public AccordionTopicItem(Node pGraphic, String pTitle, String pDescription){
        this();
        setText(pTitle);
        setGraphic(pGraphic);
        setDescription(pDescription);
    }
    
    private StringProperty descriptionProperty;
    public final StringProperty descriptionProperty(){
        if(descriptionProperty == null){
            descriptionProperty = new SimpleStringProperty();
        }
        return descriptionProperty;
    }
    public final void setDescription(String pDescription){
        descriptionProperty().set(pDescription);
    }
    public String getDescription(){
        return descriptionProperty().get();
    }
}
