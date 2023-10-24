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
import de.lb.cpx.client.app.wm.fx.process.section.details.WmReminderDetails;
import de.lb.cpx.client.app.wm.fx.process.section.operations.ItemEventHandler;
import de.lb.cpx.client.app.wm.fx.process.section.operations.WmReminderOperations;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.model.fx.contextmenu.CtrlContextMenu;
import de.lb.cpx.client.core.model.fx.label.TooltipLabel;
import de.lb.cpx.client.core.model.fx.tableview.AsyncTableView;
import de.lb.cpx.client.core.util.OverrunHelper;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmReminder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javax.ejb.AsyncResult;
import org.controlsfx.glyphfont.FontAwesome;

/**
 * Implementation of the Overview of all reminders (Wiedervorlagen) of the
 * Process
 *
 * @author wilde
 */
public class WmReminderSection extends WmSectionMulti<TWmReminder> {

    private AsyncTableView<TWmReminder> tvReminder;
    private static final Logger LOG = java.util.logging.Logger.getLogger(WmReminderSection.class.getName());
    private ReminderModeCombobox cbReminderMode;

    private ListChangeListener<TWmReminder> reminderListener = new ListChangeListener<TWmReminder>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends TWmReminder> c) {
            tvReminder.reload();
        }
    };

    /**
     * construct new Reminder section
     *
     * @param pServiceFacade servicefacade to access server services
     */
    public WmReminderSection(final ProcessServiceFacade pServiceFacade) {
        this(pServiceFacade, null);
    }

    /**
     * construct new Reminder section
     *
     * @param pServiceFacade servicefacade to access server services
     * @param pFinishedFl only (un-)finished reminders?
     */
    public WmReminderSection(final ProcessServiceFacade pServiceFacade, final Boolean pFinishedFl) {
        super(Lang.getReminders(), pServiceFacade); //"Wiedervorlagen"
        facade.getObsReminder(pFinishedFl).addListener(reminderListener);
//        tvReminder.setHBarVisible(false);
        tvReminder.reload();
        registerPropertyListener(tvReminder.getSelectionModel().selectedItemProperty(),new ChangeListener<TWmReminder>() {
            @Override
            public void changed(ObservableValue<? extends TWmReminder> observable, TWmReminder oldValue, TWmReminder newValue) {
                if (newValue == null && !tvReminder.isFocused()) {
                    return;
                }
                invalidateRequestDetailProperty();
            }
        });
        registerPropertyListener(tvReminder.focusedProperty(),new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    tvReminder.getSelectionModel().select(null);
                }
            }
        });
        tvReminder.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (KeyCode.DELETE.equals(ke.getCode())) {
                    //handleRemoveEvent();
                    //removeItems(getSelectedItems());
                    removeItems();
                    ke.consume();
                    return;
                }
                if (KeyCode.ENTER.equals(ke.getCode())) {
                    //handleEditEvent();
                    editItems();
                    ke.consume();
                    return;
                }
            }
        });
    }

    @Override
    public void dispose() {
        tvReminder.getColumns().clear();
        tvReminder.setOnRowClick(null);
        tvReminder.setRowContextMenu(null);
        tvReminder.showContextMenuProperty().unbind();
        tvReminder.setOnKeyPressed(null);
        facade.getObsReminder().removeListener(reminderListener);
        getSkin().clearMenu();
        reminderListener = null;
        super.dispose(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Pane createMenuStructure(Map<String, WmSectionMenuItem> pItems) {
        Pane structure = super.createMenuStructure(pItems); //To change body of generated methods, choose Tools | Templates.
        cbReminderMode = new ReminderModeCombobox();
        structure.getChildren().add(0,cbReminderMode);
        return structure;
    }
    
    @Override
    protected Map<String, WmSectionMenuItem> createMenuItems() {
        Map<String, WmSectionMenuItem> map = super.createMenuItems(); //To change body of generated methods, choose Tools | Templates.
        map.put(Lang.getReminderCreate(), new WmSectionMenuItem(Lang.getReminderCreate(), FontAwesome.Glyph.PLUS, new EventHandler<Event>() {
            @Override
            public void handle(Event t) {
                ItemEventHandler eh = getDetails().createItem();
                if (eh != null) {
                    eh.handle(null);
                    reload();
                }
            }
        }));
        return map;
    }
    
//    @Override
//    public void setMenu() {
//        Button createReminder = new Button(Lang.getReminderCreate());
//        createReminder.setMaxWidth(Double.MAX_VALUE);
//        createReminder.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                ItemEventHandler eh = getDetails().createItem();
//                if (eh != null) {
//                    eh.handle(null);
//                    reload();
//                }
//            }
//        });
//
//        Button menu = new Button();
//        menu.getStyleClass().add("cpx-icon-button");
//        menu.setGraphic(getSkin().getGlyph("\uf142"));
//        PopOver menuOver = new AutoFitPopOver();
//        menuOver.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
//        menuOver.setContentNode(createReminder);
//        menu.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                menuOver.show(menu);
//            }
//        });
//        cbReminderMode = new ReminderModeCombobox();
//        getSkin().setMenu(cbReminderMode, menu);
//    }

//    public void createNewReminderAction() {
//        editReminderAction(null);
//    }
//    public void editReminderAction(final TWmReminder pReminder) {
//        AddReminderDialog dialog = new AddReminderDialog(facade, pReminder);
//        dialog.showAndWait();
//        reload();
//    }
//    public static void reloadReminders() {
//        tvReminder.reload();
//    }
    @Override
    public void reload() {
        super.reload();
        tvReminder.reload();
    }

    @Override
    public TWmReminder getSelectedItem() {
        return tvReminder.getSelectionModel().getSelectedItem();
    }

    @Override
    public List<TWmReminder> getSelectedItems() {
        return new ArrayList<>(tvReminder.getSelectionModel().getSelectedItems());
    }

    @Override
    public WmReminderDetails getDetails() {
        TWmReminder selected = getSelectedItem(); //tvReminder.getSelectionModel().getSelectedItem();
        WmReminderDetails details = new WmReminderDetails(facade, selected);
        return details;
    }

//    @Override
//    public Parent getDetailContent() {
////        WmDetailSection detail = new WmDetailSection();
//
//        TWmReminder selected = getSelectedItem(); //tvReminder.getSelectionModel().getSelectedItem();
//        WmDetailSection detailSection = new WmReminderDetails(facade, selected).getDetailSection();
////        TWmReminder selected = (TWmReminder) tvReminder.getFocusModel().getFocusedItem();
////        Parent detailContent = new WmReminderDetails(serviceFacade, selected).getDetailNode();
////
////        detail.setTitle(Lang.getReminders());
////
////        detail.setContent(detailContent);
////        return detail.getRoot();
//        return detailSection.getRoot();
//    }
    @Override
    @SuppressWarnings("unchecked")
    protected Parent createContent() {
        long startTime = System.currentTimeMillis();
        VBox contentBox = new VBox();
//        VBox.setVgrow(contentBox, Priority.ALWAYS);

        tvReminder = new AsyncTableView<TWmReminder>() {
            @Override
            public Future<List<TWmReminder>> getFuture() {

                List<TWmReminder> reminderList = facade.getObsReminder();
                reminderList = filterReminders(reminderList);
//                if(!reminderList.isEmpty())
//                Collections.sort(reminderList, (TWmReminder rem1, TWmReminder rem2) -> rem2.getCreationDate().compareTo(rem1.getCreationDate()));
                return new AsyncResult<>(reminderList);
            }
        };
        tvReminder.getStyleClass().add("resize-column-table-view");
//        tvReminder.setMinHeight(150);
//        tvReminder.setMaxHeight(190);
        TableColumn<TWmReminder, Boolean> col1 = new TableColumn<>("Abg.");
        col1.prefWidthProperty().bind(Bindings.multiply(tvReminder.widthProperty(), (0.35 / 5d)));
        col1.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TWmReminder, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<TWmReminder, Boolean> param) {
                return new SimpleBooleanProperty(param.getValue().isFinished());
            }
        });
        col1.setCellFactory(new Callback<TableColumn<TWmReminder, Boolean>, TableCell<TWmReminder, Boolean>>() {
            @Override
            public TableCell<TWmReminder, Boolean> call(TableColumn<TWmReminder, Boolean> param) {
                return new TableCell<TWmReminder, Boolean>() {
                    @Override
                    protected void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                        if (item == null || empty) {
                            return;
                        }
                        setGraphic(item ? WmReminderSection.this.getSkin().getGlyph("\uf00c") : new Label(""));
                    }
                };
            }
        });
        TableColumn<TWmReminder, Boolean> col2 = new TableColumn<>("Prio.");
        col2.prefWidthProperty().bind(Bindings.multiply(tvReminder.widthProperty(), (0.35 / 5d)));

        col2.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TWmReminder, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<TWmReminder, Boolean> param) {
                return new SimpleBooleanProperty(param.getValue().isHighPrio());
            }
        });
        col2.setCellFactory(new Callback<TableColumn<TWmReminder, Boolean>, TableCell<TWmReminder, Boolean>>() {
            @Override
            public TableCell<TWmReminder, Boolean> call(TableColumn<TWmReminder, Boolean> param) {
                return new TableCell<TWmReminder, Boolean>() {
                    @Override
                    protected void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                        if (item == null || empty) {
                            return;
                        }
                        setGraphic(item ? WmReminderSection.this.getSkin().getGlyph("\uf00c") : new Label(""));
                    }
                };
            }
        });
        TableColumn<TWmReminder, String> col3 = new TableColumn<>(Lang.getReminderSubject()); //"Art der Wiedervorlage"
        col3.prefWidthProperty().bind(Bindings.multiply(tvReminder.widthProperty(), (2 / 5d)));

        col3.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TWmReminder, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TWmReminder, String> param) {
                return new SimpleStringProperty(MenuCache.instance().getReminderSubjectsForInternalId(param.getValue().getSubject()));
            }
        });//new SimpleCellValueFactory<>());

        col3.setCellFactory(new Callback<TableColumn<TWmReminder, String>, TableCell<TWmReminder, String>>() {
            @Override
            public TableCell<TWmReminder, String> call(TableColumn<TWmReminder, String> param) {
                return new TableCell<TWmReminder, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                        if (item == null) {
                            setGraphic(null);
                            return;
                        }
                        Label lblSubject = new Label(item);
                        setGraphic(lblSubject);
                        OverrunHelper.addInfoTooltip(lblSubject);
                    }

                };
            }
        });

        TableColumn<TWmReminder, Date> col4 = new TableColumn<>(Lang.getDurationTo());
        col4.prefWidthProperty().bind(Bindings.multiply(tvReminder.widthProperty(), (1 / 5d)));

        col4.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TWmReminder, Date>, ObservableValue<Date>>() {
            @Override
            public ObservableValue<Date> call(TableColumn.CellDataFeatures<TWmReminder, Date> param) {
                return new SimpleObjectProperty<>(param.getValue().getDueDate());
            }
        });
        col4.setCellFactory(new Callback<TableColumn<TWmReminder, Date>, TableCell<TWmReminder, Date>>() {
            @Override
            public TableCell<TWmReminder, Date> call(TableColumn<TWmReminder, Date> param) {
                return new TableCell<TWmReminder, Date>() {
                    @Override
                    protected void updateItem(Date item, boolean empty) {
                        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                        if (item == null || empty) {
                            setGraphic(null);
                            return;
                        }
                        String content = Lang.toDate(item);
                        Label lblDate = new TooltipLabel(content) {
                            @Override
                            public String fetchTooltipText() {
                                return Lang.toTime(item);
                            }
                        };
                        setGraphic(lblDate);
                        OverrunHelper.addInfoTooltip(lblDate, lblDate.getTooltip() != null ? lblDate.getText() + "\n" + ((TooltipLabel) lblDate).fetchTooltipText() : lblDate.getText());
                    }

                };
            }
        });
        TableColumn<TWmReminder, String> col5 = new TableColumn<>(Lang.getReminderReceiver());
//        col4.maxWidthProperty().bind(tvReminder.widthProperty().divide(4));
        col5.prefWidthProperty().bind(Bindings.multiply(tvReminder.widthProperty(), (1 / 5d)));

        col5.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TWmReminder, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TWmReminder, String> param) {
                return new SimpleStringProperty(MenuCache.instance().getUserNameForId(param.getValue().getAssignedUserId()));
            }
        });
        col5.setCellFactory(new Callback<TableColumn<TWmReminder, String>, TableCell<TWmReminder, String>>() {
            @Override
            public TableCell<TWmReminder, String> call(TableColumn<TWmReminder, String> param) {
                return new TableCell<TWmReminder, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                        if (item == null || empty) {
                            return;
                        }
                        Label lblUserName = new Label(item);
                        setGraphic(lblUserName);
                        //TODO:Show more user-info?
                        OverrunHelper.addInfoTooltip(lblUserName);
                    }
                };
            }
        });

        tvReminder.getColumns().addAll(col1, col2, col3, col4, col5);
        tvReminder.setOnRowClick(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
//                if (MouseButton.PRIMARY.equals(event.getButton())) {
//                    invalidateRequestDetailProperty();
//                }
                if (MouseButton.PRIMARY.equals(event.getButton()) && event.getClickCount() == 2) {
                    //Edit reminder on double click
                    editItems();
                }
            }
        });
        tvReminder.setRowContextMenu(createContextMenu());
        tvReminder.showContextMenuProperty().bind(getArmedProperty());

        contentBox.getChildren().add(tvReminder);
        LOG.log(Level.FINEST, "create content for workflow number {0} loaded in {1} ms", new Object[]{getProcessNumber(), (System.currentTimeMillis() - startTime)});
        return contentBox;
    }

//    private void handleRemoveEvent() {
//        final TWmReminder item = tvReminder.getSelectionModel().getSelectedItem();//row.getItem();
//        new ConfirmDialog(getRoot().getScene().getWindow(), Lang.getDeleteReminder()).showAndWait().ifPresent(new Consumer<ButtonType>() {
//            @Override
//            public void accept(ButtonType t) {
//                if (t.equals(ButtonType.YES)) {
//                    facade.removeReminder(item);
//                }
//                tvReminder.reload();
//            }
//        });
//    }
    private ContextMenu createContextMenu() {
        final ContextMenu contextMenu = new CtrlContextMenu<>();
        final MenuItem removeMenuItem = new MenuItem(Lang.getDelete());
        removeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                removeItems();
            }
        });
        contextMenu.getItems().add(removeMenuItem);
        // Set context menu on row, but use a binding to make it only show for non-empty rows:  
        final MenuItem editMenuItem = new MenuItem(Lang.getEdit());
        editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                editItems();
            }

        });
        contextMenu.getItems().add(editMenuItem);

        return contextMenu;
    }

//    @Override
//    protected void openItem(final TWmReminder pItem) {
//        //open and edit is actually the same for reminders
//        editItem(pItem);
//    }
    private List<TWmReminder> filterReminders(List<TWmReminder> pReminders) {
        if (!isArmed()) {
            return pReminders;
        }
        ReminderMode mode = cbReminderMode.getSelectionModel().getSelectedItem();
        if (mode == null) {
            LOG.warning("Reminder Mode is null! show all Reminders");
            mode = ReminderMode.SHOW_ALL;
        }
        switch (mode) {
            case SHOW_ALL:
                return pReminders;
            case SHOW_CLOSED:
                return pReminders.stream().filter(new Predicate<TWmReminder>() {
                    @Override
                    public boolean test(TWmReminder t) {
                        return t.isFinished();
                    }
                }).collect(Collectors.toList());
            case SHOW_OPEN:
                return pReminders.stream().filter(new Predicate<TWmReminder>() {
                    @Override
                    public boolean test(TWmReminder t) {
                        return !t.isFinished();
                    }
                }).collect(Collectors.toList());
            default:
                return new ArrayList<>();
        }
    }

    protected class ReminderModeCombobox extends ComboBox<ReminderMode> {

        public ReminderModeCombobox() {
            super(FXCollections.observableArrayList(ReminderMode.values()));
            getStyleClass().add("combo-box-menu-pane");
            getSelectionModel().select(ReminderMode.SHOW_OPEN);
            registerPropertyListener(getSelectionModel().selectedItemProperty(),new ChangeListener<ReminderMode>() {
                @Override
                public void changed(ObservableValue<? extends ReminderMode> observable, ReminderMode oldValue, ReminderMode newValue) {
                    tvReminder.reload();
                }
            });
        }

    }

    protected enum ReminderMode {
        SHOW_ALL(Lang.getReminderModeShowAll()), SHOW_OPEN(Lang.getReminderModeShowOpen()), SHOW_CLOSED(Lang.getReminderModeShowClosed());

        private final String displayText;

        private ReminderMode(String pText) {
            displayText = pText;
        }

        public String getDisplayText() {
            return displayText;
        }

        @Override
        public String toString() {
            return getDisplayText();
        }

    }

    @Override
    public WmReminderOperations getOperations() {
        return new WmReminderOperations(facade);
    }

}
