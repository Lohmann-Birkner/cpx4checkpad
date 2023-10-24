/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.core.model.fx.combobox;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.server.catalog.service.ejb.CatalogUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 *
 * @author gerschmann
 */
public class GrouperModelsCombobox extends ComboBox<GDRGModel>{
    
    private Callback <GDRGModel, Boolean> onUpdateParent;
    
    public GrouperModelsCombobox(){
        super();
        setItems(FXCollections.observableArrayList(CatalogUtil.getActualGrouperModels(false)));
        getStyleClass().add("combo-box-top-pane");
        getStyleClass().add("header-combo-box");
        setPromptText("Grouper auswählen");

        setConverter(new StringConverter<GDRGModel>() {
            @Override
            public String toString(GDRGModel model) {
                return model == null ? "" : model.toString();
            }

            @Override
            public GDRGModel fromString(String modelName) {
                return GDRGModel.getModel(GDRGModel.getModelID(modelName));
            }
        });
        getSelectionModel().select(CpxClientConfig.instance().getSelectedGrouper());
        getSelectionModel().selectedItemProperty().addListener(new ChangeListener<GDRGModel>() {
            @Override
            public void changed(ObservableValue<? extends GDRGModel> observable, GDRGModel oldValue, GDRGModel newValue) {
                if(oldValue == null && newValue == null
                        || newValue != null && oldValue != null && newValue.equals(oldValue)){
                    return;
                }
                CpxClientConfig.instance().setSelectedGrouper(newValue);
                updateParent();
            }
        });

        setButtonCell(new ListCell<GDRGModel>() {
            @Override
            protected void updateItem(GDRGModel item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText("");
                } else {
                    setText("Grouper " + item.toString());
                }
            }
        });

    }
     public void setOnUpdateParent(Callback <GDRGModel, Boolean>  onUpdate){
         onUpdateParent = onUpdate;
     }
     
     public Callback <GDRGModel, Boolean>  getOnUpateParent(){
         return onUpdateParent;
     }
     
     private void updateParent(){
         if(onUpdateParent != null){
             onUpdateParent.call(CpxClientConfig.instance().getSelectedGrouper());
         }
     }
     
}
