/*
 * Copyright (c) 2021 Lohmann & Birkner.
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
 *    2021  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.cm.fx.department;

import de.lb.cpx.client.app.cm.fx.details.CmDepartmentScene;
import de.lb.cpx.client.core.model.fx.listview.cell.TwoLineCell;
import de.lb.cpx.client.core.model.fx.menu.MenuedControl;
import de.lb.cpx.client.core.model.fx.titledpane.AccordionSelectedItem;
import de.lb.cpx.client.core.model.fx.titledpane.AccordionTitledPane;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseWard;
import de.lb.cpx.shared.lang.Lang;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 *
 * @author wilde
 */
public class DepartmentTitledPane extends AccordionTitledPane<TCaseDepartment, TCaseWard>{
    private ListView<TCaseWard> listView;
    private boolean clearFlag;

   public DepartmentTitledPane(TCaseDepartment pDepartment, String pTitle){
        super();
        setTitle(pTitle);
        LOG.log(Level.FINE, "Department: " + pDepartment.getDepKey301());
        setAccordionItem(pDepartment);

        setContentFactory(new Callback<TCaseDepartment, Node>() {
            @Override
            public Node call(TCaseDepartment param) {
                listView = new ListView<>();
                ExpandableContent content = new ExpandableContent(listView);
                content.getControl().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TCaseWard>() {
                    @Override
                    public void changed(ObservableValue<? extends TCaseWard> observable, TCaseWard oldValue, TCaseWard newValue) {
                        if(clearFlag){ // ignore clear in view update
                            clearFlag = false;
                            return;
                        }
                        //set new detail content for new selection
                        getSelectItemCallback().call(new SelectedWardItem(newValue, pDepartment));
                    }
                });
                return content;
            }
        });
    }
    private static final Logger LOG = Logger.getLogger(DepartmentTitledPane.class.getName());
    
    @Override
    public void clearSelection(){
        if(listView == null){
            return; //no selection to clear!
        }
        if(listView.getSelectionModel().getSelectedItem()!=null){
            clearFlag = true;
        }
        listView.getSelectionModel().clearSelection();
    }
    
    @Override
    public String updateTitle() {
        return getTitle();
    }

    @Override
    public AccordionSelectedItem<TCaseWard> getMostRecentItem(SelectionTarget pTarget) {
        if(pTarget.equals(SelectionTarget.ACCORDION_ITEM)){
            return new SelectedDepartmentItem(getAccordionItem());
        }
        if(listView == null){
            return new SelectedDepartmentItem(getAccordionItem());
        }
        TCaseWard selectedWard = listView.getSelectionModel().getSelectedItem();
        if(selectedWard != null){
            return new SelectedWardItem(selectedWard, getAccordionItem());
        }
        return new SelectedDepartmentItem(getAccordionItem());
    }
    
    
    
    //TODO: make this listview titledpane or somthing like this from ruleeditor
    public class ExpandableContent extends MenuedControl<ListView<TCaseWard>> {

        public ExpandableContent(ListView<TCaseWard> pListView) {
            super(pListView);
            getControl().getStyleClass().add("stay-selected-list-view");

            getControl().getItems().setAll(getValues());

            getControl().setCellFactory(new Callback<ListView<TCaseWard>, ListCell<TCaseWard>>() {
                @Override
                public ListCell<TCaseWard> call(ListView<TCaseWard> param) {
                    TwoLineCell<TCaseWard> cell = new TwoLineCell<TCaseWard>() {

                        @Override
                        protected void updateItem(TCaseWard item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item == null || empty) {
                                setTitle("");
                                setDescription("");
                                return;
                            }
                            //update items
                            setTitle(item.getWardcIdent());
                            setDescription(createWardDescription(item));
//                            setText(ward + "\n" + "von: " + (item.getWardcAdmdate() == null ? "n.A." : formatter.format(item.getWardcAdmdate())) + " bis: " + (item.getWardcDisdate() == null ? "n.A." : formatter.format(item.getWardcDisdate())));
                        }
                    };
                    cell.setPrefHeight(50);
                    cell.setStyle("-fx-padding:16;");
                    return cell;
                }
            });
        }
        
        private String createWardDescription(TCaseWard pWard){
            SimpleDateFormat formatter = new SimpleDateFormat(Lang.getDepartmentsWardDateFormat());
            StringBuilder builder = new StringBuilder();
            //"von: " + (item.getWardcAdmdate() == null ? "n.A." : formatter.format(item.getWardcAdmdate())) + " bis: " + (item.getWardcDisdate() == null ? "n.A." : formatter.format(item.getWardcDisdate()))
            builder.append("von: ");
            builder.append(pWard.getWardcAdmdate() == null ? "n.A." : formatter.format(pWard.getWardcAdmdate()));
            builder.append(" bis: ");
            builder.append(pWard.getWardcDisdate() == null ? "n.A." : formatter.format(pWard.getWardcDisdate()));
            return builder.toString();
        }

    }
    
}
