/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.menu.fx.list_master_detail;

import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.model.fx.masterdetail.ListViewMasterDetailPane;
import de.lb.cpx.client.core.util.CpxFXMLLoader;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * controller class for the list master detail scene May replaced in an later
 * version
 *
 * @author wilde
 * @param <T> object type of the list
 */
@Deprecated(since = "1.05")
public abstract class ListMasterDetailScene<T> extends CpxScene {

    private final ObjectProperty<EventHandler<ActionEvent>> onOpenItemProperty = new SimpleObjectProperty<>();

    /**
     * creates a new ListMasterDetail scene and initialize the controller class
     *
     * @throws IOException thrown when corresponding fxml is not found
     */
    public ListMasterDetailScene() throws IOException {
        super(CpxFXMLLoader.getLoader(ListMasterDetailFXMLController.class));
    }

//    protected static final URL getResourceForLoader() {
//        final Class<ListMasterDetailFXMLController> clazz = ListMasterDetailFXMLController.class;
//        final String ressource = CpxFXMLLoader.getRessourceToLoad(clazz);
//        return clazz.getResource(ressource);
//    }
    /**
     * sets the master detail in the controller
     *
     * @param pListMdPane master detailPane to set
     */
    public final void setMasterDetail(ListViewMasterDetailPane<T> pListMdPane) {
        getController().setMasterDetailPane(pListMdPane);
        getController().setCellFactory(getCellFactory());
        getController().getMasterDetailPane().getSelectedItemProperty().addListener(new ChangeListener<T>() {
            @Override
            public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
                if (newValue != null) {
                    try {
                        getController().getMasterDetailPane().setDetail(getDetailContent(newValue));
                    } catch (CpxIllegalArgumentException ex) {
                        Logger.getLogger(ListMasterDetailScene.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }

    /**
     * get the currently set master detail pane from the controller
     *
     * @return get the master detail pane
     */
    public final ListViewMasterDetailPane<T> getMasterDetail() {
        return getController().getMasterDetailPane();
    }

    /**
     * get the controller instance
     *
     * @return controller
     */
    @Override
    @SuppressWarnings("unchecked")
    public final ListMasterDetailFXMLController<T> getController() {
        return (ListMasterDetailFXMLController<T>) super.getController();
    }

    /**
     * get the cellfactory, needed to render the list cell in the listview
     *
     * @return callback to perform the cellrendering
     */
    public abstract Callback<ListView<T>, ListCell<T>> getCellFactory();

    /**
     * get the reprensentation of the detail content for the given item
     *
     * @param item item to get detailcontent from
     * @return parent object of the detail layout
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException invalid argument
     */
    public abstract Parent getDetailContent(T item) throws CpxIllegalArgumentException;

    /**
     * open an item stored in the listview, normaly this needed to open a new
     * scene for a detail view of the item for example caseDetailOverview or a
     * processDetailOverview
     *
     * @param pId database id of the item to open
     * @return cpx scene of the item
     */
    public abstract CpxScene openItem(Long pId);

    /**
     * get the selected item property from the list
     *
     * @return selected item proeprty
     */
    public final ReadOnlyObjectProperty<T> getSelectedItemProperty() {
        return getController().getSelectedItemProperty();
    }

    /**
     * get the listview reference of the listview currently placed in the master
     * section of the md pane
     *
     * @return listview of the master section of the md pane
     */
    public ListView<T> getListView() {
        return getController().getListView();
    }

    public EventHandler<ActionEvent> getOnOpenItem() {
        return onOpenItemProperty.getValue();
    }

    public void setOnOpenItem(EventHandler<ActionEvent> pHandler) {
        onOpenItemProperty.setValue(pHandler);
    }

    public final ObjectProperty<EventHandler<ActionEvent>> getOnOpenItemProperty() {
        return onOpenItemProperty;
    }
}
