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
package de.lb.cpx.client.app.wm.fx.process.section;

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.process.section.details.WmServiceOverviewDetails;
import de.lb.cpx.client.app.wm.fx.process.section.operations.ItemEventHandler;
import de.lb.cpx.client.app.wm.fx.process.section.operations.WmServiceOverviewOperations;
import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.model.catalog.CpxHospital;
import de.lb.cpx.client.core.model.cell.SimpleCellValueFactory;
import de.lb.cpx.client.core.model.fx.contextmenu.CtrlContextMenu;
import de.lb.cpx.client.core.model.fx.label.TooltipLabel;
import de.lb.cpx.client.core.model.fx.tableview.AsyncTableView;
import de.lb.cpx.client.core.util.OverrunHelper;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmProcessCase;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javax.ejb.AsyncResult;
import org.controlsfx.glyphfont.FontAwesome;

/**
 * Implementation of the Overview of all Services, Cases for taht process
 *
 * @author wilde
 */
public class WmServiceOverviewSection extends WmSectionMulti<TWmProcessCase> {

    private static final Logger LOG = Logger.getLogger(WmServiceOverviewSection.class.getName());
    private AsyncTableView<TWmProcessCase> tvServices;

    private ListChangeListener<TWmProcessCase> processCasesListener = new ListChangeListener<TWmProcessCase>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends TWmProcessCase> c) {
            
            tvServices.reload();
            updateTitle();
        }
    };
    
    public void updateTitle(){
        this.setTitle(Lang.getServiceOverview() + " (" + facade.getObsProcessCases().size() + ")");
    }

    public WmServiceOverviewSection(final ProcessServiceFacade pServiceFacade) {
        super(Lang.getServiceOverview() , pServiceFacade);
        facade.getObsProcessCases().addListener(processCasesListener);
        tvServices.reload();
        updateTitle();
        registerPropertyListener(tvServices.getSelectionModel().selectedItemProperty(),new ChangeListener<TWmProcessCase>() {
            @Override
            public void changed(ObservableValue<? extends TWmProcessCase> observable, TWmProcessCase oldValue, TWmProcessCase newValue) {
                if (newValue == null && !tvServices.isFocused()) {
                    return;
                }
                invalidateRequestDetailProperty();
            }
        });
        registerPropertyListener(tvServices.focusedProperty(),new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    tvServices.getSelectionModel().select(null);
                }
            }
        });
        tvServices.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (KeyCode.DELETE.equals(ke.getCode())) {
                    removeItems();
                    ke.consume();
                    return;
                }
                if (KeyCode.ENTER.equals(ke.getCode())) {
                    openItems();
                    ke.consume();
                    return;
                }
            }
        });
    }

    @Override
    public void dispose() {
        tvServices.getColumns().clear();
        tvServices.setOnRowClick(null);
        tvServices.setRowContextMenu(null);
        tvServices.showContextMenuProperty().unbind();
        tvServices.setOnKeyPressed(null);
        facade.getObsProcessCases().removeListener(processCasesListener);
        getSkin().clearMenu();
        processCasesListener = null;
        super.dispose(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Map<String, WmSectionMenuItem> createMenuItems() {
        Map<String, WmSectionMenuItem> map = super.createMenuItems(); //To change body of generated methods, choose Tools | Templates.
        map.put(Lang.getServiceCreate(), new WmSectionMenuItem(Lang.getServiceCreate(), FontAwesome.Glyph.PLUS, new EventHandler<Event>() {
            @Override
            public void handle(Event t) {
                createItem();
            }
        }));
        return map;
    }
    
//    @Override
//    public void setMenu() {
//        Button createService = new Button(Lang.getServiceCreate());
//        createService.setOnMouseClicked((MouseEvent event) -> {
//            createItem();
//        });
//
//        Button menu = new Button();
//        menu.getStyleClass().add("cpx-icon-button");
//        menu.setGraphic(getSkin().getGlyph("\uf142"));
//        PopOver menuOver = new AutoFitPopOver();
//        menuOver.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
//        menuOver.setContentNode(createService);
//
//        menu.setOnMouseClicked((MouseEvent event) -> {
//            menuOver.show(menu);
//        });
//
//        getSkin().setMenu(menu);
//    }

    @Override
    public void reload() {
        super.reload();
        tvServices.reload();
    }

    @Override
    public TWmProcessCase getSelectedItem() {
        return tvServices.getSelectionModel().getSelectedItem();
    }

    @Override
    public List<TWmProcessCase> getSelectedItems() {
        return new ArrayList<>(tvServices.getSelectionModel().getSelectedItems());
    }
    
    public TWmProcessCase getProcessCaseForCase(final TCase pItem) {
        if (pItem == null) {
            return null;
        }
        TWmProcessCase pc = null;
        for (TWmProcessCase tmp : new ArrayList<>(tvServices.getSelectionModel().getSelectedItems())) {
            if (tmp.getHosCase() == pItem) {
                pc = tmp;
                break;
            }
        }
        return pc;
    }

    @Override
    public WmServiceOverviewDetails getDetails() {
        TWmProcessCase selected = getSelectedItem(); //tvServices.getSelectionModel().getSelectedItem();
        WmServiceOverviewDetails details = new WmServiceOverviewDetails(facade, selected);
        return details;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Parent createContent() {
        long startTime = System.currentTimeMillis();
        VBox contentBox = new VBox();
        VBox.setVgrow(contentBox, Priority.ALWAYS);

        tvServices = new AsyncTableView<TWmProcessCase>() {
            @Override
            public Future<List<TWmProcessCase>> getFuture() {
                return new AsyncResult<>(new ArrayList<>(facade.getObsProcessCases()));
            }
        };
        tvServices.getStyleClass().add("resize-column-table-view");

        TableColumn<TWmProcessCase, TWmProcessCase> col1 = new TableColumn<>("Basisfall");
        col1.prefWidthProperty().bind(Bindings.multiply(tvServices.widthProperty(), (0.5 / 5d)));
        col1.setCellValueFactory(new SimpleCellValueFactory<>());
        col1.setCellFactory(new Callback<TableColumn<TWmProcessCase, TWmProcessCase>, TableCell<TWmProcessCase, TWmProcessCase>>() {
            @Override
            public TableCell<TWmProcessCase, TWmProcessCase> call(TableColumn<TWmProcessCase, TWmProcessCase> param) {
                TableCell<TWmProcessCase, TWmProcessCase> cell = new TableCell<>() {

                    @Override
                    protected void updateItem(TWmProcessCase item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            setText("");
                            return;
                        }
                        setText(item.getMainCase() ? "X" : "");
                    }
                };
                return cell;
            }

        });

        TableColumn<TWmProcessCase, TWmProcessCase> col2 = new TableColumn<>(Lang.getCaseNumber());
        col2.prefWidthProperty().bind(Bindings.multiply(tvServices.widthProperty(), (1 / 5d)));
        col2.setCellValueFactory(new SimpleCellValueFactory<>());
        col2.setCellFactory(new Callback<TableColumn<TWmProcessCase, TWmProcessCase>, TableCell<TWmProcessCase, TWmProcessCase>>() {
            @Override
            public TableCell<TWmProcessCase, TWmProcessCase> call(TableColumn<TWmProcessCase, TWmProcessCase> param) {
                TableCell<TWmProcessCase, TWmProcessCase> cell = new TableCell<>() {

                    @Override
                    protected void updateItem(TWmProcessCase item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            setGraphic(null);
                            return;
                        }
                        String content = item.getHosCase().getCsCaseNumber();
                        Label lblCaseNumber = new Label(content);
                        setGraphic(lblCaseNumber);
                        OverrunHelper.addInfoTooltip(lblCaseNumber);

                    }
                };
                return cell;
            }

        });
        TableColumn<TWmProcessCase, Date> col3 = new TableColumn<>(Lang.getAdmissionDate());
        col3.prefWidthProperty().bind(Bindings.multiply(tvServices.widthProperty(), (1 / 5d)));
        col3.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TWmProcessCase, Date>, ObservableValue<Date>>() {
            @Override
            public ObservableValue<Date> call(TableColumn.CellDataFeatures<TWmProcessCase, Date> param) {
                TCase cse = param.getValue().getHosCase();
                TCaseDetails local = cse != null ? cse.getCurrentLocal() : null;
                return new SimpleObjectProperty<>(local != null ? local.getCsdAdmissionDate() : null);
            }
        });
        col3.setCellFactory(new Callback<TableColumn<TWmProcessCase, Date>, TableCell<TWmProcessCase, Date>>() {
            @Override
            public TableCell<TWmProcessCase, Date> call(TableColumn<TWmProcessCase, Date> param) {
                return new TableCell<TWmProcessCase, Date>() {
                    @Override
                    protected void updateItem(Date item, boolean empty) {
                        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                        if (item == null || empty) {
                            setGraphic(null);
                            return;
                        }
                        String content = Lang.toDate(item);
                        Label lblAdmission = new TooltipLabel(content) {
                            @Override
                            public String fetchTooltipText() {
                                return Lang.toTime(item);
                            }
                        };
                        setGraphic(lblAdmission);
                        OverrunHelper.addInfoTooltip(lblAdmission, lblAdmission.getTooltip() != null ? lblAdmission.getText() + "\n" + ((TooltipLabel) lblAdmission).fetchTooltipText() : lblAdmission.getText());
                    }

                };
            }
        });
        TableColumn<TWmProcessCase, Date> col4 = new TableColumn<>(Lang.getDischargeDate());
        col4.prefWidthProperty().bind(Bindings.multiply(tvServices.widthProperty(), (1 / 5d)));
        col4.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TWmProcessCase, Date>, ObservableValue<Date>>() {
            @Override
            public ObservableValue<Date> call(TableColumn.CellDataFeatures<TWmProcessCase, Date> param) {
                TCase cse = param.getValue().getHosCase();
                TCaseDetails local = cse != null ? cse.getCurrentLocal() : null;
                return new SimpleObjectProperty<>(local != null ? local.getCsdDischargeDate() : null);
            }
        });
        col4.setCellFactory(new Callback<TableColumn<TWmProcessCase, Date>, TableCell<TWmProcessCase, Date>>() {
            @Override
            public TableCell<TWmProcessCase, Date> call(TableColumn<TWmProcessCase, Date> param) {
                return new TableCell<TWmProcessCase, Date>() {
                    @Override
                    protected void updateItem(Date item, boolean empty) {
                        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                        if (item == null || empty) {
                            setGraphic(null);
                            return;
                        }
                        String content = Lang.toDate(item);
                        Label lblDischarge = new TooltipLabel(content) {
                            @Override
                            public String fetchTooltipText() {
                                return Lang.toTime(item);
                            }
                        };
                        setGraphic(lblDischarge);
                        OverrunHelper.addInfoTooltip(lblDischarge, lblDischarge.getTooltip() != null ? lblDischarge.getText() + "\n" + ((TooltipLabel) lblDischarge).fetchTooltipText() : lblDischarge.getText());
                    }

                };
            }
        });

        TableColumn<TWmProcessCase, TWmProcessCase> col5 = new TableColumn<>(Lang.getHospitalName());
        col5.prefWidthProperty().bind(Bindings.multiply(tvServices.widthProperty(), (1 / 5d)));
        col5.setCellValueFactory(new SimpleCellValueFactory<>());
        col5.setCellFactory(new Callback<TableColumn<TWmProcessCase, TWmProcessCase>, TableCell<TWmProcessCase, TWmProcessCase>>() {
            @Override
            public TableCell<TWmProcessCase, TWmProcessCase> call(TableColumn<TWmProcessCase, TWmProcessCase> param) {
                TableCell<TWmProcessCase, TWmProcessCase> cell = new TableCell<>() {

                    @Override
                    protected void updateItem(TWmProcessCase item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            setGraphic(null);
                            return;
                        }
                        CpxHospital hospital = facade.findHospitalByIdent(item.getHosCase().getCsHospitalIdent());
                        String content = hospital.getHosName();
                        Label t = new TooltipLabel(content) {
                            @Override
                            public String fetchTooltipText() {
                                return hospital.toShortDescription();
                            }
                        };
                        setGraphic(t);
                        OverrunHelper.addInfoTooltip(t, t.getTooltip() != null ? t.getText() + "\n" + ((TooltipLabel) t).fetchTooltipText() : t.getText());
                    }
                };
                return cell;
            }

        });
        tvServices.getColumns().addAll(col1, col2, col3, col4, col5);

        tvServices.setOnRowClick(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (MouseButton.PRIMARY.equals(event.getButton()) && event.getClickCount() == 2) {
                    //Open hospital case on double click
                    openItems();
                }
            }
        });

        tvServices.setRowContextMenu(createContextMenu());
        tvServices.showContextMenuProperty().bind(getArmedProperty());

        contentBox.getChildren().add(tvServices);
        LOG.log(Level.FINEST, "create content for workflow number {0} loaded in {1} ms", new Object[]{getProcessNumber(), (System.currentTimeMillis() - startTime)});
        return contentBox;
    }

    private ContextMenu createContextMenu() {
        final ContextMenu contextMenu = new CtrlContextMenu<>();
        final MenuItem openCasemenu = new MenuItem(Lang.getWorkingListContextMenuOpen());
        openCasemenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                openItems();
            }
        });
        final MenuItem unlockCasemenu = new MenuItem(Lang.getWorkingListContextMenuUnlock());
        unlockCasemenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (facade.unlockCase(tvServices.getSelectionModel().getSelectedItem().getHosCase().getId())) {
                    MainApp.showInfoMessageDialog(Lang.getWorkingListContextMenuUnlockSuccess(tvServices.getSelectionModel().getSelectedItem().getHosCase().getCsCaseNumber()), getSkin().getWindow());
                } else {
                    MainApp.showErrorMessageDialog(Lang.getWorkingListContextMenuUnlockError(tvServices.getSelectionModel().getSelectedItem().getHosCase().getCsCaseNumber()));
                }

            }
        });
        final MenuItem deleteServiceMenu = new MenuItem(Lang.getWorkingListContextMenuDelete());
        deleteServiceMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                removeItems();
            }
        });
        contextMenu.getItems().addAll(openCasemenu, unlockCasemenu, deleteServiceMenu);
        contextMenu.setOnShowing(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if (tvServices.getSelectionModel().getSelectedItem() == null) {
                    return;
                }
                boolean isLocked = facade.isCaseLocked(tvServices.getSelectionModel().getSelectedItem().getHosCase().getId());
                unlockCasemenu.setDisable(!isLocked);
            }
        });

        return contextMenu;
    }

    @Override
    public WmServiceOverviewOperations getOperations() {
        return new WmServiceOverviewOperations(facade);
    }

    protected void openItems() {
        for (TWmProcessCase item : getSelectedItems()) {
            openItem(item);
        }
//        reload();
    }

    public void openItem(TWmProcessCase pItem) {
        ItemEventHandler eh = getOperations().openItem(pItem);
        if (eh != null) {
            eh.handle(null);
            reload();
        }
    }

}
