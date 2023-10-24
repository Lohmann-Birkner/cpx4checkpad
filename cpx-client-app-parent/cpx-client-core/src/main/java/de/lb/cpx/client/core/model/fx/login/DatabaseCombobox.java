/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.login;

import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.fx.labeled.LabeledComboBox;
import de.lb.cpx.service.information.CpxDatabase;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javax.naming.NamingException;

/**
 * Combobox to set up selectable databases
 *
 * @author wilde
 */
public class DatabaseCombobox extends LabeledComboBox<CpxDatabase> {

    private static final Logger LOG = Logger.getLogger(DatabaseCombobox.class.getName());

    private Callback<String, Void> onTruncate;
    private Callback<String, Void> onDrop;

    /**
     * creates new instance with empty selection
     */
    public DatabaseCombobox() {
        this("", "");
    }

    /**
     * creates new instance with database selected
     *
     * @param pTitle title of the labeled item
     * @param pDatabase database name
     */
    public DatabaseCombobox(String pTitle, String pDatabase) {
        if (pTitle != null && !pTitle.isEmpty()) {
            setTitle(pTitle);
        }

        getControl().setCellFactory(new Callback<ListView<CpxDatabase>, ListCell<CpxDatabase>>() {
            @Override
            public ListCell<CpxDatabase> call(ListView<CpxDatabase> param) {
                DatabaseListCell cell = new DatabaseListCell();
                cell.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {

                        CpxDatabase item = cell.getItem();
                        if (item == null) {
                            return;
                        }

                        //PNa: 25.04.19, cpx-1506
                        // disable truncate (ALT + click on db name) feature
                        /* if (event.isPrimaryButtonDown() && event.isAltDown()) {
                            if (getOnTruncate() != null) {
                                getOnTruncate().call(item.getConnectionString());
                                event.consume();
                            }
                        } */
                        // disable drop (SHIFT + click on db name) feature.
                        /*  if (event.isPrimaryButtonDown() && event.isShiftDown()) {
                            if (getOnDrop() != null) {
                                getOnDrop().call(item.getConnectionString());
                                event.consume();
                            }
                        } */
                    }
                });
                return cell;
            }
        });
        getControl().setButtonCell(new DatabaseListCell());
    }

    public void setOnTruncate(Callback<String, Void> pCallback) {
        onTruncate = pCallback;
    }

    public void setOnDrop(Callback<String, Void> pCallback) {
        onDrop = pCallback;
    }

    public Callback<String, Void> getOnTruncate() {
        return onTruncate;
    }

    public Callback<String, Void> getOnDrop() {
        return onDrop;
    }

    @SuppressWarnings("unchecked")
    public void loadDatabases(final String pSelectedDatabase) throws NamingException {
        List<CpxDatabase> list;
        if (getControl().getItems() instanceof FilteredList) {
            list = ((FilteredList) getControl().getItems()).getSource();
        } else {
            list = getControl().getItems();
        }
        list.clear();
        List<CpxDatabase> databaseList = Session.instance().getEjbConnector().connectAuthServiceBean().get().getCpxDatabases();

        CpxDatabase selectedDb = getSelectedDb(pSelectedDatabase, databaseList);
//        if(selectedDb != null){
//            databaseList.add(selectedDb);
//        }

        Collections.sort(databaseList);
        //CpxDatabase emptyDatabaseEntry = new CpxDatabase("", " ", null, "");
        //databaseList.add(0, emptyDatabaseEntry);

        list.addAll(databaseList);
        if (selectedDb != null) {
            getControl().getSelectionModel().select(selectedDb);
        } else {
            getControl().getSelectionModel().selectFirst();
        }
    }

    private CpxDatabase getSelectedDb(String pSelectedDatabase, List<CpxDatabase> pDatabaseList) {
        String lDatabase = (pSelectedDatabase == null) ? "" : pSelectedDatabase.trim();
        if (!lDatabase.isEmpty() && !lDatabase.contains(":")) {
            LOG.log(Level.INFO, "Database ''{0}'' is not associated with a persistence unit! I''ll lookup now if this database is unique...", lDatabase);
            String fallbackDatabase = "";
            int fallbackDatabaseCount = 0;
            for (CpxDatabase cpxDatabase : pDatabaseList) {
                if (cpxDatabase.getName().equalsIgnoreCase(lDatabase)) {
                    fallbackDatabase = cpxDatabase.getConnectionString();
                    fallbackDatabaseCount++;
                }
            }
            if (fallbackDatabaseCount == 1) {
                lDatabase = fallbackDatabase;
                LOG.log(Level.INFO, "I''ll use ''{0}''!", lDatabase);
            } else {
                if (fallbackDatabaseCount == 0) {
                    BasicMainApp.showErrorMessageDialog("Database '" + lDatabase + "' is not associated with a persistence unit and I was not able to auto-detect a suitable one! Was not able to found an existing database with the same name!");
                }
                if (fallbackDatabaseCount > 1) {
                    BasicMainApp.showErrorMessageDialog("Database '" + lDatabase + "' is not associated with a persistence unit and I was not able to auto-detect a suitable one! The given database name is ambigious (" + fallbackDatabaseCount + " matches) cause there are more than one persistence unit that contains this database!");
                }
            }
        }

        CpxDatabase selectedDatabaseEntry = null;
        for (CpxDatabase cpxDatabase : pDatabaseList) {
            if (lDatabase.equalsIgnoreCase(cpxDatabase.getConnectionString())) {
                cpxDatabase.setSelected(true);
                selectedDatabaseEntry = cpxDatabase;
            }
        }

        //Database was specified as a parameter, but it does not seem to exist! So I'll create a dummy entry..
        if (!lDatabase.isEmpty() && selectedDatabaseEntry == null) {
            String persistenceUnit = "???";
            String database = lDatabase;
            String connUrl = "";
            if (lDatabase.contains(":")) {
                String tmp[] = lDatabase.split(":");
                if (tmp.length >= 1) {
                    persistenceUnit = tmp[0].trim();
                }
                if (tmp.length >= 1) {
                    database = tmp[1].trim();
                }
            }
            for (CpxDatabase cpxDatabase : pDatabaseList) {
                if (cpxDatabase.getPersistenceUnit().equalsIgnoreCase(persistenceUnit)) {
                    connUrl = cpxDatabase.getUrl();
                }
            }
            selectedDatabaseEntry = new CpxDatabase(persistenceUnit, database, null, null, connUrl);
            selectedDatabaseEntry.selected = true;
            selectedDatabaseEntry.unknown = true;
        }
        return selectedDatabaseEntry;
    }

}
