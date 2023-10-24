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
 *    2016  shahin - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.process.section;

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.process.section.details.WmReminderDetails;
import de.lb.cpx.client.app.wm.fx.process.section.operations.WmReminderOperations;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.model.fx.alert.ConfirmDialog;
import de.lb.cpx.client.core.model.fx.contextmenu.CtrlContextMenu;
import de.lb.cpx.client.core.model.fx.label.TooltipLabel;
import de.lb.cpx.client.core.model.fx.tableview.AsyncTableView;
import de.lb.cpx.client.core.util.OverrunHelper;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmReminder;
import de.lb.cpx.wm.model.TWmRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javax.ejb.AsyncResult;

/**
 * Implementation of the Overview of all reminders (Wiedervorlagen) of the
 * Process
 *
 * @author shahin
 */
public class WmReminderSectionForRequest extends WmSectionMulti<TWmReminder> {

    private final TWmRequest request;
    private AsyncTableView<TWmReminder> tvReminder;

//    private final ListChangeListener<TWmReminder> reminderListener = new ListChangeListener<TWmReminder>() {
//        @Override
//        public void onChanged(ListChangeListener.Change<? extends TWmReminder> c) {
//            tvReminder.reload();
//        }
//    };
    /**
     * construct new Reminder section
     *
     * @param pServiceFacade servicefacade to access server services
     * @param pRequest request
     */
    public WmReminderSectionForRequest(ProcessServiceFacade pServiceFacade, TWmRequest pRequest) {
        super("Mit der Anfrage erstellte Wiedervorlagen", pServiceFacade);
        request = pRequest;
        tvReminder.reload();
        tvReminder.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TWmReminder>() {
            @Override
            public void changed(ObservableValue<? extends TWmReminder> observable, TWmReminder oldValue, TWmReminder newValue) {
                if (newValue == null && !tvReminder.isFocused()) {
                    return;
                }
            }
        });
        tvReminder.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    tvReminder.getSelectionModel().select(null);
                }
            }
        });
    }


//    @Override
//    protected void openItem(final TWmReminder pItem) {
//        //open and edit is actually the same for reminders
//        editItem(pItem);
//    }
//    @Override
//    protected EventHandler<Event> editItem(final TWmReminder pItem) {
//        return new EventHandler<Event>() {
//            @Override
//            public void handle(Event evt) {
//                AddReminderDialog dialog = new AddReminderDialog(facade, pItem, getRoot().getScene().getWindow());
//                dialog.showAndWait();
//                reload();
//            }
//        };
//    }
//    public void editReminderAction(final TWmReminder pReminder) {
//        AddReminderDialog dialog = new AddReminderDialog(facade, pReminder, getRoot().getScene().getWindow());
//
//        dialog.showAndWait();
//        reload();
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
        return null;
    }

//    @Override
//    public Parent getDetailContent() {
//        return getRoot();
//    }
    @Override
    @SuppressWarnings("unchecked")
    protected Parent createContent() {
        VBox contentBox = new VBox();
        VBox.setMargin(contentBox, new Insets(12, 0, 0, 0));
        tvReminder = new AsyncTableView<TWmReminder>() {
            @Override
            public Future<List<TWmReminder>> getFuture() {
                List<TWmReminder> mdkReminders = facade.getRemindersForRequest(request.getId());
                return new AsyncResult<>(mdkReminders);
            }
        };
        tvReminder.getStyleClass().add("resize-column-table-view");
        TableColumn<TWmReminder, Boolean> col1 = new TableColumn<>(Lang.getReminderFinished());
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
                        setGraphic(item ? WmReminderSectionForRequest.this.getSkin().getGlyph("\uf00c") : new Label(""));
                    }
                };
            }
        });

        TableColumn<TWmReminder, String> col2 = new TableColumn<>(Lang.getReminderSubject()); //"Art der Wiedervorlage"
        col2.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TWmReminder, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TWmReminder, String> param) {
                return new SimpleStringProperty(MenuCache.instance().getReminderSubjectsForInternalId(param.getValue().getSubject()));
            }
        });
        col2.setCellFactory(new Callback<TableColumn<TWmReminder, String>, TableCell<TWmReminder, String>>() {
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

        TableColumn<TWmReminder, Date> col3 = new TableColumn<>(Lang.getDurationTo());
        col3.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TWmReminder, Date>, ObservableValue<Date>>() {
            @Override
            public ObservableValue<Date> call(TableColumn.CellDataFeatures<TWmReminder, Date> param) {
                return new SimpleObjectProperty<>(param.getValue().getDueDate());
            }
        });
        col3.setCellFactory(new Callback<TableColumn<TWmReminder, Date>, TableCell<TWmReminder, Date>>() {
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
        TableColumn<TWmReminder, String> col4 = new TableColumn<>(Lang.getReminderReceiver());
        col4.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TWmReminder, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TWmReminder, String> param) {
                return new SimpleStringProperty(MenuCache.instance().getUserNameForId(param.getValue().getAssignedUserId()));
            }
        });
        col4.setCellFactory(new Callback<TableColumn<TWmReminder, String>, TableCell<TWmReminder, String>>() {
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
                        OverrunHelper.addInfoTooltip(lblUserName);
                    }
                };
            }
        });
        col1.setPrefWidth(80);
        col2.setPrefWidth(250);
        col3.setPrefWidth(100);
        col4.setPrefWidth(100);
        tvReminder.getColumns().addAll(col1, col2, col3, col4);
        tvReminder.setOnRowClick(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (MouseButton.PRIMARY.equals(event.getButton()) && event.getClickCount() == 2) {
                    editItems();
                    //handleEditEvent();
                }
            }
        });
        tvReminder.setRowContextMenu(createContextMenu());
        tvReminder.showContextMenuProperty().bind(getArmedProperty());
        Label lbTitle = new Label("Mit der Anfrage erstellte Wiedervorlagen");
        lbTitle.setStyle("-fx-font-size: 24px;");
        lbTitle.setStyle("-fx-font-weight: bold;");
        contentBox.getChildren().addAll(tvReminder);
        return contentBox;
    }

    private ContextMenu createContextMenu() {
        final ContextMenu contextMenu = new CtrlContextMenu<>();
        final MenuItem removeMenuItem = new MenuItem(Lang.getDelete());
        removeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                final TWmReminder item = tvReminder.getSelectionModel().getSelectedItem();//row.getItem();
                new ConfirmDialog(getRoot().getScene().getWindow(), Lang.getDeleteReminder()).showAndWait().ifPresent(new Consumer<ButtonType>() {
                    @Override
                    public void accept(ButtonType t) {
                        if (t.equals(ButtonType.YES)) {
                            facade.removeReminder(item);
                        }
                        tvReminder.reload();
                    }
                });
            }
        });
        contextMenu.getItems().add(removeMenuItem);
        // Set context menu on row, but use a binding to make it only show for non-empty rows:  
        final MenuItem editMenuItem = new MenuItem(Lang.getEdit());
        editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //handleEditEvent();
                editItems();
            }

        });
        contextMenu.getItems().add(editMenuItem);

        return contextMenu;
    }

//    private void handleEditEvent() {
//        //TWmReminder item = tvReminder.getSelectionModel().getSelectedItem();//row.getItem();
//        editItems();
////        editReminderAction(item);
////        tvReminder.reload();
//    }
//
//    @Override
//    protected void editItems() {
//        for (TWmReminder item : getSelectedItems()) {
//            EventHandler<Event> eh = getDetails().editItem(item);
//            if (eh != null) {
//                eh.handle(null);
//            }
//        }
//        reload();
//    }
//    
//    @Override
//    protected void removeItems() {
//        //not implemented yet
//    }
    @Override
    public WmReminderOperations getOperations() {
        return new WmReminderOperations(facade);
    }

}
