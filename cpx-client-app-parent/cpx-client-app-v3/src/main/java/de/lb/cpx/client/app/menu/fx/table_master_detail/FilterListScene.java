/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.menu.fx.table_master_detail;

import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.model.fx.filter_manager.FilterManager;
import de.lb.cpx.client.core.util.CpxFXMLLoader;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.model.enums.CaseDetailsCancelReasonEn;
import de.lb.cpx.model.enums.SearchListTypeEn;
import de.lb.cpx.service.ejb.SearchResult;
import de.lb.cpx.shared.dto.LockException;
import de.lb.cpx.shared.dto.SearchItemDTO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javax.naming.NamingException;

/**
 * Basic Scene for filtered list like Working and Workflow list Implemenation of
 * these list should extend this class and a specific controller to fit there
 * needs
 *
 * @author wilde
 * @param <T> items of the list
 */
public abstract class FilterListScene<T extends SearchItemDTO> extends CpxScene {

    //filter manager property to react to changes in manager
    private ObjectProperty<FilterManager> filterManagerProperty;
    //text to show in spefic area to show status of filtered /loaded items
    private StringProperty filterTextProperty;
    private StringProperty filterTextRowsProperty;
    private ObjectProperty<Parent> detailNodeProperty;
    private static final Integer MAX_ITEM_INDEFINITE = -1;
    protected IntegerProperty maxItemsProperty;
    private ObjectProperty<EventHandler<MouseEvent>> rowClickHandlerProperty;
    private ObjectProperty<T> selectedItemProperty;

    /**
     * contruct new instance with a specific controler matching ressource path
     *
     * @param pRessource path to fxml for the layout definition should be
     * something like /fmxl/dummyFXML.fxml
     * @param pController controller class instance to handle loaded fxml
     * @throws java.io.IOException throws when fxml is not found or controller
     * mismatch with fxml
     *
     * TODO: refactor property of filterTextProperty
     * @throws javax.naming.NamingException thrown if beans are not found
     */
    public FilterListScene(String pRessource, FilterListFXMLController<T> pController) throws IOException, NamingException {
        super(CpxFXMLLoader.getLoader(pRessource, pController));
        connectServiceBeans();
    }

    /**
     * contructs new instance with default fxml FilterListFXML
     *
     * @param pController controller class to handle fxml
     * @throws IOException ,when fxml is not found or controller mismatch
     * @throws javax.naming.NamingException thrown if beans are not found
     */
    public FilterListScene(FilterListFXMLController<T> pController) throws IOException, NamingException {
        this("/fxml/FilterListFXML.fxml", pController);
    }

    /**
     * load items form server to display in tableview
     *
     * @param pStart start index
     * @param pEnd end index
     * @return list of newly loaded data
     */
    public abstract SearchResult<? extends T> loadItems(Integer pStart, Integer pEnd);

    /**
     * connect to service beans to get data from server
     *
     * @throws javax.naming.NamingException thrown if bean was not found
     */
    public abstract void connectServiceBeans() throws NamingException;

    /**
     * @return get filter manager property for bidnings or listen to changes
     */
    public ObjectProperty<FilterManager> filterManagerProperty() {
        if (filterManagerProperty == null) {
            filterManagerProperty = new SimpleObjectProperty<>();
        }
        return filterManagerProperty;
    }

    /**
     * @return filter manager currently set
     */
    public final FilterManager getFilterManager() {
        return filterManagerProperty().get();
    }

    /**
     * @param pManager manager to set
     */
    public void setFilterManager(FilterManager pManager) {
        filterManagerProperty().set(pManager);
    }

    /**
     * @return property to show status of filtered / loaded items
     */
    public StringProperty filterTextProperty() {
        if (filterTextProperty == null) {
            //filterTextProperty = new SimpleStringProperty("gefiltert: ---- / ----");
            filterTextProperty = new SimpleStringProperty();
        }
        return filterTextProperty;
    }

    public StringProperty filterTextRowsProperty() {
        if (filterTextRowsProperty == null) {
            //filterTextRowsProperty = new SimpleStringProperty("gefiltert: ---- / ----");
            filterTextRowsProperty = new SimpleStringProperty();
        }
        return filterTextRowsProperty;
    }

    /**
     * @return status of filtered loaded item
     */
    public String getFilterText() {
        return filterTextProperty().get();
    }

    /**
     * @return status of filtered loaded item
     */
    public String getFilterTextRows() {
        return filterTextRowsProperty().get();
    }

    /**
     * @param pFilterText set new text shown as filter status
     */
    public void setFilterText(String pFilterText) {
        filterTextProperty().set(pFilterText);
    }

    /**
     * @param pFilterTextRows set new text shown as filter status
     */
    public void setFilterTextRows(String pFilterTextRows) {
        filterTextRowsProperty().set(pFilterTextRows);
    }

    public ObjectProperty<Parent> detailNodeProperty() {
        if (detailNodeProperty == null) {
            detailNodeProperty = new SimpleObjectProperty<>();
        }
        return detailNodeProperty;
    }

    public Parent getDetailNode() {
        return detailNodeProperty().get();
    }

    public void setDetailNode(Parent pNode) {
        detailNodeProperty().set(pNode);
    }

    /**
     * @return maximum count of items displayed initialy
     */
    public IntegerProperty maxItemProperty() {
        if (maxItemsProperty == null) {
            maxItemsProperty = new SimpleIntegerProperty(MAX_ITEM_INDEFINITE);
        }
        return maxItemsProperty;
    }

    /**
     * @param pMaxItems set new max number
     */
    public void setMaxItems(Integer pMaxItems) {
        maxItemProperty().set(pMaxItems);
    }

    public abstract Integer getMaxItems();

//    /**
//     * @return get current max number
//     */
//    public Integer getMaxItems() {
//        return maxItemProperty().get();
//    }
    /**
     * @return if max number is indefinite (all items should be displayed)
     */
    public boolean isMaxItemIndefinite() {
        return getMaxItems().equals(MAX_ITEM_INDEFINITE);
    }

    public ObjectProperty<EventHandler<MouseEvent>> rowClickHandlerProperty() {
        if (rowClickHandlerProperty == null) {
            rowClickHandlerProperty = new SimpleObjectProperty<>();
        }
        return rowClickHandlerProperty;
    }

    public void setRowClickHandler(EventHandler<MouseEvent> pEventHandler) {
        rowClickHandlerProperty().set(pEventHandler);
    }

    public EventHandler<MouseEvent> getRowClickHandler() {
        return rowClickHandlerProperty().get();
    }

    public final ObjectProperty<T> selectedItemProperty() {
        if (selectedItemProperty == null) {
            selectedItemProperty = new SimpleObjectProperty<>();
        }
        return selectedItemProperty;
    }

    public final T getSelectedItem() {
        return selectedItemProperty().get();
    }
//    public void setSelectedItem(T pItem){
//        selectedItemProperty().set(pItem);
//    }

    /**
     * overrideable methode to show supported sublists in workinglist there are
     * to list tpyes, rule and case, if some more are added it should be placed
     * hear
     *
     * @return list of supported lists,default is empty
     */
    public List<SearchListTypeEn> getSupportedLists() {
        return new ArrayList<>();
    }

    /**
     * update filtermanager values with new searchlist(filter)
     *
     * @param newFilter newfilter to set
     */
    public abstract void updateFilterManager(SearchListTypeEn newFilter);

    /**
     * @param pId database id to open item
     * @return init views to show
     */
    public abstract CpxScene openItem(long pId);

    /**
     * @param pId database id of item
     * @return indicator if item is locked
     */
    public abstract boolean isItemLocked(long pId);

    /**
     *
     * @param pId database id of item
     * @return indicator if item is cancel
     */
    public abstract boolean isItemCanceled(long pId);

    /**
     * @param pId database id of the item
     * @return indicator if unlocking was successful
     */
    public abstract boolean unlockItem(long pId);

    /**
     * @param pIds database ids of items
     * @return number of unlocked items
     */
    public abstract int unlockItems(long[] pIds);

    /**
     * unlock all items
     *
     * @return number of unlocked items
     */
    public abstract int unlockAllItems();

    /**
     * @param pId database id of the item
     * @throws LockException thrown when case is locked
     */
    public abstract void checkLocked(long pId) throws LockException;
    
    public abstract boolean checkExists(long pId);
    
    /**
     * @param pId databse id of item to delete database id is invalid
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException thrown if it
     * does not exist in the database an can therefore not be deleted, or
     * something went wrong while deleting
     */
    public abstract void deleteItem(long pId) throws CpxIllegalArgumentException;

    /**
     *
     * @param pId id of item to cancel
     * @throws CpxIllegalArgumentException cancellation failed
     */
    public abstract void cancelItem(long pId) throws CpxIllegalArgumentException;

    /**
     *
     * @param pId id of item to cancel
     * @throws CpxIllegalArgumentException uncancellation failed
     */
    public abstract void unCancelItem(long pId) throws CpxIllegalArgumentException;
    
     public CaseDetailsCancelReasonEn getCancelReason4Case(long caseId) throws CpxIllegalArgumentException{
         return null;
     }

    public String getCaseNumbers4CancelledCaseByMerge(long caseId) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    return "";
    }

    public void unmergeCases4CancelledCase(long caseId) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
