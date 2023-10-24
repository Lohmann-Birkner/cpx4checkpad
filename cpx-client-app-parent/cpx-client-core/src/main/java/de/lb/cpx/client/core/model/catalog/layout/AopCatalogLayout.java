/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.core.model.catalog.layout;

import de.lb.cpx.client.core.model.catalog.CpxOpsAop;
import de.lb.cpx.shared.lang.Lang;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author gerschmann
 */
public class AopCatalogLayout extends VBox{

    private final CpxOpsAop aop;
    public AopCatalogLayout(CpxOpsAop pAop){
        super();
       aop = pAop; 
      getChildren().add(createContent());
        //Workaround, add padding.. sometimes for some reason popover do not render top and bottom padding declared in css
       setPadding(new Insets(1));
    }
    
    private Node createContent() {
        GridPane pane = new GridPane();
        pane.setMinWidth(200);
        pane.setMaxWidth(600);
        pane.getStyleClass().add("default-grid");        
        Label text = new Label(Lang.getCatalogAopCatalog() + " " + aop.getCategoryString());
        text.setWrapText(true);
        pane.addRow(0, text);
        return pane;
    }
    
}
