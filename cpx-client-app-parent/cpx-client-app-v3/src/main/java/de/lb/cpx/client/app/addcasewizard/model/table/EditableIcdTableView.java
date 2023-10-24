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
package de.lb.cpx.client.app.addcasewizard.model.table;

import de.lb.cpx.client.core.model.fx.contextmenu.IcdRefTypeMenu;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.enums.LocalisationEn;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

/**
 * new Implementaion of a Icd TableView, its editable
 *
 * @author wilde
 */
public class EditableIcdTableView extends IcdTableView {

    private Collection<TCaseIcd> icdSet;
//private final BooleanProperty armedProperty = new SimpleBooleanProperty(true);

    /**
     * create new Editable TableView to edit RefType Column and Localisation,
     * columns identical with basic IcdTableView, except "use for grouping"
     * Column is hidden
     */
    public EditableIcdTableView() {
        super();
//        setArmedPropertyValue(true);
        setUpRows();
        setUpColumns();
    }

    /**
     * create new Editable TableView to edit RefType Column and Localisation,
     * columns identical with basic IcdTableView, except "use for grouping"
     * Column is hidden
     *
     * @param editable should components be editable or not
     */
    public EditableIcdTableView(boolean editable) {
        this();
//        setArmedPropertyValue(editable);
    }

//    /**
//     * set ArmedValue, specify if controls in TableView are changeable or not
//     *
//     * @param value new Armed Value
//     */
//    public final void setArmedPropertyValue(boolean value) {
//        armedProperty.setValue(value);
//    }
//
//    /**
//     * Return if controls are modifiable or not
//     *
//     * @return current armedValue
//     */
//    public boolean isArmed() {
//        return armedProperty.getValue();
//    }
    /**
     * set Set of TCaseIcd entities and show them in the TableView
     *
     * @param icdList List of Icds to show
     */
    public void setItemSet(Collection<TCaseIcd> icdList) {
        this.icdSet = icdList == null ? null : icdList;
        setItems(FXCollections.observableArrayList(icdList));
        refresh();
    }
    //set Up Columns hiddes useForGroupColumn and override default cellFactories if neccessary

    private void setUpColumns() {
        useForGroupColumn.setVisible(false);
//CPX-997 Sort icdLocalisationColumn
        icdLocalisationColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TCaseIcd, LocalisationEn>, ObservableValue<LocalisationEn>>() {
            @Override
            public ObservableValue<LocalisationEn> call(TableColumn.CellDataFeatures<TCaseIcd, LocalisationEn> param) {

                return new SimpleObjectProperty<>(param.getValue().getIcdcLocEn());

            }
        });
        icdLocalisationColumn.setCellFactory(new Callback<TableColumn<TCaseIcd, LocalisationEn>, TableCell<TCaseIcd, LocalisationEn>>() {
            @Override
            public TableCell<TCaseIcd, LocalisationEn> call(TableColumn<TCaseIcd, LocalisationEn> param) {
                TableCell<TCaseIcd, LocalisationEn> cell = new TableCell<TCaseIcd, LocalisationEn>() {
                    @Override
                    protected void updateItem(LocalisationEn item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            return;
                        }
//                           TCaseIcd icd = (TCaseIcd) item;

                        setText(item == LocalisationEn.E ? "" : item.getViewId());
                        if (item != LocalisationEn.E) {
                            setTooltip(new Tooltip(item.getTranslation().toString()));
                        } else {
                            setTooltip(null);
                        }

                    }
                };

                cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {

                        if (cell.isEmpty()) {
                            return;
                        }
                        TCaseIcd icd = cell.getTableRow().getItem();
                        if (icd != null && icd.getIcdcCode() != null) {
                            handleEditEvent(icd);

                        }
                    }

                    private void handleEditEvent(TCaseIcd icd) {
                        TextField icdLoc = new TextField(icd.getIcdcLocEn() == LocalisationEn.E ? "" : icd.getIcdcLocEn().getViewId());
                        icdLoc.setTooltip(new Tooltip("Gültige Werte : R (Rechts) ,L(Links),B(beidseitig)"));
                        cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent e) {
                                refresh();
                            }
                        });
                        icdLoc.textProperty().addListener(new ChangeListener<String>() {
                            @Override
                            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                                if (icdLoc.getText() != null && icdLoc.getText().length() > 1) {
                                    icdLoc.setText(icdLoc.getText().substring(0, 1));
                                }
                            }
                        });
                        icdLoc.focusedProperty().addListener((arg0, oldValue, newValue) -> {
                            if (!newValue) {
                                setIcdLoc(icdLoc.getText(), icd);

                            }

                        });
                        icdLoc.setOnKeyPressed(new EventHandler<KeyEvent>() {
                            @Override
                            public void handle(KeyEvent ke) {
                                if (ke.getCode().equals(KeyCode.ENTER)) {
                                    setIcdLoc(icdLoc.getText(), icd);
                                }
                                if (ke.getCode().equals(KeyCode.ESCAPE)) {

                                    refresh();
                                }

                            }

                        });

                        icdLoc.setPrefWidth(80);
                        cell.setGraphic(icdLoc);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                icdLoc.requestFocus();

                            }
                        });

                    }

                    private void setIcdLoc(String icdLoc, TCaseIcd icd) {
                        if (icdLoc != null) {

                            String icdLocS = icdLoc.toUpperCase();
                            switch (icdLocS) {
                                case "R":
                                    icd.setIcdcLocEn(LocalisationEn.R);
//                                                icdLoc.setText("R");
                                    break;
                                case "B":
                                    icd.setIcdcLocEn(LocalisationEn.B);
//                                                icdLoc.setText("B");
                                    break;
                                case "L":
                                    icd.setIcdcLocEn(LocalisationEn.L);
//                                                icdLoc.setText("L");
                                    break;
                                default:
                                    icd.setIcdcLocEn(LocalisationEn.E);
                                    break;
                            }
                            refresh();
                        } else {
                            refresh();
                        }
                    }
                });
                return cell;
            }

        });

        super.resizeColumns();
    }
    //setUp TableRows (add ContextMenue to it)

    private void setUpRows() {
        setRowFactory(new Callback<TableView<TCaseIcd>, TableRow<TCaseIcd>>() {
            @Override
            public TableRow<TCaseIcd> call(TableView<TCaseIcd> tableView) {
                final TableRow<TCaseIcd> row = new TableRow<>();
//                final ContextMenu contextMenu = new CtrlContextMenu();
                final MenuItem removeMenuItem = new MenuItem(Lang.getDelete());
                setOnKeyPressed(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent ke) {

                        if (ke.getCode().equals(KeyCode.DELETE)) {
                            if (tableView.getSelectionModel().getSelectedItem() != null && tableView.getSelectionModel().getSelectedItem().getIcdcCode() != null) {
                                remove(tableView.getSelectionModel().getSelectedItem());
                            }

                        }
                    }
                });
                //CPX-1061 RSH 2018.08.20
                row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        removeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                if (row.getItem().getIcdcCode() != null) {
                                    remove(row.getItem());
                                }
                            }
                        });
                        //AWi-09022018-CPX-802:
                        //made Context menu class to reuse impementation in simulation
                        final ContextMenu contextMenu = new IcdRefTypeMenu(EditableIcdTableView.this, icdSet) {
                            @Override
                            public TCaseIcd getItem() {
                                return row.getItem();
                            }
                        };
                        contextMenu.getItems().add(0, new SeparatorMenuItem());
                        contextMenu.getItems().add(0, removeMenuItem);
                        // Set context menu on row, but use a binding to make it only show for non-empty rows:  
                        row.contextMenuProperty().bind(
                                Bindings.when(row.emptyProperty())
                                        .then((ContextMenu) null)
                                        .otherwise(contextMenu)
                        );
                    }

                });

                return row;
            }

        });
    }

    public void remove(TCaseIcd pItem) {
        removeIcdObjectFromCollection(getItems(), pItem);
        removeIcdObjectFromCollection(icdSet, pItem);
        refresh();
    }

    public void add(TCaseIcd item) {
        getItems().add(item);
        setItemSet(getItems());
        icdSet.add(item);
        refresh();
    }

    public void removeBlankRows(Collection<TCaseIcd> objCollection) {
        Iterator<TCaseIcd> it = new ArrayList<>(objCollection).iterator();
        while (it.hasNext()) {
            TCaseIcd next = it.next();
            if (next.getIcdcCode() == null) {
                remove(next);
            }
        }
        refresh();
    }

    private void removeIcdObjectFromCollection(Collection<TCaseIcd> objCollection, TCaseIcd icd) {
        Iterator<TCaseIcd> it = objCollection.iterator();
        while (it.hasNext()) {
            TCaseIcd next = it.next();
            if (next.getRefIcds().contains(icd)) {
                next.setIcdcReftypeEn(null);
                next.setRefIcds(new HashSet<>());
            }
        }
        objCollection.remove(icd);
    }
}
