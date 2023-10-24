/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.app.tabController;

import de.lb.cpx.client.core.model.CpxScene;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TabPane;

/**
 *
 * @author gerschmann
 */
public class MergeParentTabScene extends CpxScene{
    
     private ObjectProperty<TabPane> parentTabPaneProperty = new SimpleObjectProperty();
     
    public MergeParentTabScene(Parent root) {
        super(root);
    }
    
    public MergeParentTabScene(FXMLLoader pLoader) {
        super(pLoader);
    }
    
     
    public void setParentTabPane(TabPane pPane){
        parentTabPaneProperty.set(pPane);
    }
    
    public TabPane getParentTabPane(){
        return parentTabPaneProperty.get();
    }
   
    
}
