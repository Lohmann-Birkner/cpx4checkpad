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
package de.lb.cpx.client.core.menu.fx.openedEntry;

import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.CpxScreen;
import de.lb.cpx.client.core.model.fx.tooltip.CpxTooltip;
import de.lb.cpx.client.core.util.OverrunHelper;
import de.lb.cpx.client.core.util.ResourceLoader;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import org.controlsfx.glyphfont.FontAwesome;

/**
 * FXML Controller class Implements the open entry layout controller for the
 * entry in the titledpane for the main menu shows already open list entries and
 * its values
 *
 * @author wilde
 */
public class OpenedEntryFXMLController extends Controller<CpxScreen> {

    @FXML
    private AnchorPane hboxContent;
    @FXML
    private Label labelIcon;
    @FXML
    private Label labelHeader;
    @FXML
    private Label labelDescription;
    @FXML
    private Button buttonClose;
    //f00d

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttonClose.setGraphic(ResourceLoader.getGlyph("\uf00d"));
        buttonClose.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                closeScene();
            }
        });
        labelIcon.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.FOLDER_OPEN));//"\uf07c"));
        labelHeader.getStyleClass().add("label-open-entry");
        labelDescription.getStyleClass().add("label-open-entry");
//        OverrunHelper.addInfoTooltip(labelDescription);
    }

    /**
     * sets the graphic in the entry, graphic is placed before the text should
     * be of the type glyph
     *
     * @param pGraphic graphic to set
     */
    public void setIconGraphic(Node pGraphic) {
        labelIcon.setGraphic(pGraphic);
    }

    /**
     * sets the text in the header of the entry
     *
     * @param pText text to set
     */
    public void setEntryHeaderText(String pText) {
        labelHeader.setText(pText);
    }

    /**
     * sets the description text in the entry
     *
     * @param pText text to set
     */
    public void setDescription(String pText) {
        labelDescription.setText(pText);
    }
    public void setDescriptionTooltip(String pTooltip){
        OverrunHelper.addOverrunCallback(labelDescription, new Callback<Boolean, Void>() {
            @Override
            public Void call(Boolean p) {
                if(!p){
                    labelDescription.setTooltip(null);
                    return null;
                }
                labelDescription.setTooltip(new CpxTooltip(pTooltip, 200, 5000, 5, true));
                return null;
            }
        });
    }
    private void closeScene(){
        getScene().close();
    }
    
//    @FXML
//    private void onClose(ActionEvent event) {
//        getScene().close();
//    }

    @Override
    public boolean close() {
        buttonClose.setOnAction(null);
        return super.close(); //To change body of generated methods, choose Tools | Templates.
    }
}
