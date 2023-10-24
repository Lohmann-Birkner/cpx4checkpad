/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.core.model;

import de.lb.cpx.client.core.model.Controller;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;
/**
 *
 * @author gerschmann
 * @param <T>
 */
public class TabController  <T extends Scene> extends Controller <T>{
    
    protected void updateHeaderPosition(TabPane tbpOpenContent, double pMenuWidth,double pMetaDataWidth){
        StackPane header = (StackPane) tbpOpenContent.lookup(".tab-header-area");
        if (header != null) {
            //compute new Values translate whole header to the left and than set padding to the right
            //not really nice, but found no other way, should be changed to a more clear way
            //tbMenu is normaly not present in that controller .. can be solved maybe with an lookup on this controller parent?!
            header.setTranslateX(-1.0d * ((int)pMenuWidth));
            header.setPadding(new Insets(0, 0, 0, pMenuWidth + pMetaDataWidth));//tbMenu.getPrefWidth()));
        }
    }
    
    public TabController(){
        super();
    }

}
