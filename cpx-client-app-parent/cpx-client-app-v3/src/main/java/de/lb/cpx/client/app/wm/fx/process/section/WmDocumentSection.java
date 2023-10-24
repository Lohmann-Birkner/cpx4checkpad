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

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.dialog.AddDocumentDialog;
import de.lb.cpx.client.app.wm.fx.process.section.details.WmDocumentDetails;
import de.lb.cpx.client.app.wm.fx.process.section.operations.ItemEventHandler;
import de.lb.cpx.client.app.wm.fx.process.section.operations.WmDocumentOperations;
import de.lb.cpx.client.app.wm.util.DocumentManager;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.model.cell.SimpleCellValueFactory;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.model.fx.alert.ConfirmDialog;
import de.lb.cpx.client.core.model.fx.contextmenu.CtrlContextMenu;
import de.lb.cpx.client.core.model.fx.file_chooser.FileChooserFactory;
import de.lb.cpx.client.core.model.fx.label.TooltipLabel;
import de.lb.cpx.client.core.model.fx.tableview.AsyncTableView;
import de.lb.cpx.client.core.util.OverrunHelper;
import de.lb.cpx.connector.EjbConnector;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.reader.DocumentReader;
import de.lb.cpx.server.commonDB.model.CWmListDocumentType;
import de.lb.cpx.service.ejb.ProcessServiceBeanRemote;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmDocument;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Callback;
import javax.ejb.AsyncResult;
import org.apache.commons.io.FilenameUtils;
import org.controlsfx.glyphfont.FontAwesome;

/**
 * Implementation of the Overview of all Documents for the Process implements
 * drag drop to add documents- supported types are txt doc docx pdf xml TODO:
 * Move static dialogs to extra class
 *
 * @author wilde
 */
public class WmDocumentSection extends WmSectionMulti<TWmDocument> {

    public static final String RELOAD_TABLE_VIEW = "reload.table.view";
    public static final String REFRESH_TABLE_VIEW = "refresh.table.view";
//    private final ProcessServiceFacade facade;
    private AsyncTableView<TWmDocument> tvDocuments;
    //private TWmProcess currentProcess;

    /**
     * supported filetypes to add to the document list
     */
    //private static final List<String> SUPPORTED_FILE_TYPES = java.util.Arrays.asList(new String[]{"*.pdf", "*.doc", "*.docx", "*.xls", "*.xlsx", "*.csv", "*.jpg", "*.jpeg", "*.xml", "*.msg", "*.txt"});
    private static final String[] SUPPORTED_FILE_TYPES = new String[]{"pdf", "xls", "xlsx", "txt", "csv", "doc", "docx", "msg", "jpg", "jpeg", "tiff", "bmp", "png", "gif", "xml"};
    private static final Logger LOG = Logger.getLogger(WmDocumentSection.class.getSimpleName());
    private static EjbProxy<ProcessServiceBeanRemote> processServiceBean;
    //private static EjbProxy<ConfigurationServiceEJBRemote> connectConfigurationServiceBean;

    private synchronized void setProcessServiceBean(final EjbProxy<ProcessServiceBeanRemote> pProcessServiceBean) {
        processServiceBean = pProcessServiceBean;
    }

    public static String[] getSupportedFileTypes() {
        String[] result = new String[SUPPORTED_FILE_TYPES.length];
        System.arraycopy(SUPPORTED_FILE_TYPES, 0, result, 0, SUPPORTED_FILE_TYPES.length);
        return result;
    }

    public static List<String> getFileFilterExtension() {
        List<String> filter = new ArrayList<>();
        for (String type : SUPPORTED_FILE_TYPES) {
            filter.add("*." + type);
        }
        return filter;
    }

    private ListChangeListener<TWmDocument> documentListener = new ListChangeListener<TWmDocument>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends TWmDocument> c) {
            tvDocuments.reload();
        }
    };

    private static boolean isValidExtension(final String pFileName) {
        String ext = FilenameUtils.getExtension(pFileName);
        for (String type : SUPPORTED_FILE_TYPES) {
            //type = type.replace("*", "").trim().toLowerCase();
            if (ext.equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }

    /**
     * construct new instance of the Section with tableview and detail content
     *
     * @param pServiceFacade service facadde to access server services
     */
    public WmDocumentSection(ProcessServiceFacade pServiceFacade) {
        super(Lang.getDocument(), pServiceFacade);
        //facade = pServiceFacade;
        facade.getObsDocuments().addListener(documentListener);
        tvDocuments.reload();

        registerMapListener(facade.getProperties(),new MapChangeListener<Object, Object>() {
            @Override
            public void onChanged(MapChangeListener.Change<? extends Object, ? extends Object> change) {
                if (change.wasAdded() && REFRESH_TABLE_VIEW.equals(change.getKey())) {
                    tvDocuments.getItems();
                    tvDocuments.refresh();
                    facade.getProperties().remove(REFRESH_TABLE_VIEW);
                }
            }
        });
        Session session = Session.instance();
        EjbConnector connector = session.getEjbConnector();
        setProcessServiceBean(connector.connectProcessServiceBean());
        //setConfigurationServiceBean(connector.connectConfigurationServiceBean());

        getRoot().setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(final DragEvent event) {
                mouseDragOver(event);
            }

            private void mouseDragOver(DragEvent e) {
                final Dragboard db = e.getDragboard();
                boolean isAccepted = false;
                final File property = new File(System.getProperty("java.io.tmpdir"));  // temp dir on client
                for (File file : db.getFiles()) {
                    if (file.getParentFile().equals(property)) {
                        continue;
                    }
                    isAccepted = isValidExtension(file.getName());
                    if (isAccepted) {
                        e.acceptTransferModes(TransferMode.COPY);
                        break;
                    }
                }

                if (!isAccepted) {
                    final List<String> fileNames = DocumentManager.getSelectedOutlookAttachments(db);
                    //LOG.log(Level.INFO, "found outlook files: " + fileNames.size());
                    for (String file : fileNames) {
                        isAccepted = isValidExtension(file);
                        if (isAccepted) {
                            e.acceptTransferModes(TransferMode.COPY);
                            break;
                        }
                    }
                }
                e.consume();
            }

        });
        getRoot().setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(final DragEvent event) {
                mouseDragDropped(event);
            }

            private void mouseDragDropped(DragEvent e) {
                final Dragboard db = e.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    success = true;
                    // Only get the first file from the list
                    final File property = new File(System.getProperty("java.io.tmpdir"));  // temp dir on client
                    for (File file : db.getFiles()) {
                        if (file.getParentFile().equals(property)) {
                            continue;
                        }
                        readFileAndShowDialog(file);
                    }
                }
                if (!success) {
                    final List<String> fileNames = DocumentManager.getSelectedOutlookAttachments(db);
                    if (!fileNames.isEmpty()) {
                        //LOG.log(Level.INFO, "found outlook files: " + fileNames.size());
                        success = true;
//                        DocumentReader reader = new DocumentReader();
                        for (File file : DocumentReader.getOutlookAttachments(fileNames)) {
                            readFileAndShowDialog(file);
                        }
                    }
                }
                tvDocuments.reload();
                e.setDropCompleted(success);
                e.consume();
            }
        });
        tvDocuments.setOnDragDetected((final MouseEvent event) -> {
            List<TWmDocument> docs = new ArrayList<>(tvDocuments.getSelectionModel().getSelectedItems());
            if (docs.isEmpty()) {
                return;
            }
            List<File> files = new ArrayList<>();
            final String property = System.getProperty("java.io.tmpdir");  // temp dir on client
            for (TWmDocument doc : docs) {
                final byte[] documentContent = DocumentManager.getDocumentContent(doc, processServiceBean.get());
                if (documentContent == null) {
                    continue;
                }
                final String locToCreateFile = property + doc.getName();
                try {
                    File f = new File(locToCreateFile);
                    DocumentManager.deleteFile(f);
//                        if (f.exists()) {
//                            if (f.delete()) {
//                                LOG.log(Level.FINEST, "file was successfully deleted: " + f.getAbsolutePath());
//                            }
//                        }
                    File tempFile = DocumentManager.createFileInTempOrSpecificDir(documentContent, locToCreateFile);
                    files.add(tempFile);
                    if (tempFile != null && tempFile.exists()) {
                        tempFile.deleteOnExit();
                    }
                } catch (IOException | IllegalArgumentException ex) {
                    LOG.log(Level.SEVERE, "Was not able to create temporary file on client: " + locToCreateFile, ex);
                    MainApp.showErrorMessageDialog(ex, "Beim Erzeugen der temporären Datei " + locToCreateFile + " trat ein Fehler auf");
                    return;
                }
            }
            final Dragboard db = getRoot().startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putFiles(files);
            db.setContent(content);

            event.consume();
        });
        tvDocuments.setOnDragDone(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent me) {
                me.consume();
            }
        });
        registerPropertyListener(tvDocuments.getSelectionModel().selectedItemProperty(),(ObservableValue<? extends TWmDocument> observable, TWmDocument oldValue, TWmDocument newValue) -> {
            if (newValue == null && !tvDocuments.isFocused()) {
                return;
            }
            invalidateRequestDetailProperty();
        });
        registerPropertyListener(tvDocuments.focusedProperty(),(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                Platform.runLater(() -> {
                    tvDocuments.getSelectionModel().select(null);
                });
            }
        });

        tvDocuments.setOnKeyPressed((KeyEvent ke) -> {
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
            final KeyCombination combSave = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
            if (combSave.match(ke)) {
                saveItems();
                ke.consume();
                return;
            }
        });
    }
    @Override
    public void dispose() {
        tvDocuments.getColumns().clear();
        tvDocuments.setOnRowClick(null);
        tvDocuments.setRowContextMenu(null);
        tvDocuments.showContextMenuProperty().unbind();
        tvDocuments.setOnKeyPressed(null);
        tvDocuments.setOnDragDone(null);
        tvDocuments.setOnDragDetected(null);
        getRoot().setOnDragDropped(null);
        getRoot().setOnDragOver(null);
        facade.getObsDocuments().removeListener(documentListener);
        getSkin().clearMenu();
        documentListener = null;
        super.dispose(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Map<String, WmSectionMenuItem> createMenuItems() {
        Map<String, WmSectionMenuItem> map = super.createMenuItems(); //To change body of generated methods, choose Tools | Templates.
        map.put(Lang.getDocumentCreate(), new WmSectionMenuItem(Lang.getDocumentCreate(), FontAwesome.Glyph.DOWNLOAD, new EventHandler<Event>() {
            @Override
            public void handle(Event t) {
                ItemEventHandler eh = getDetails().createItem();
                if (eh != null) {
                    eh.handle(null);
                    reload();
                }
            }
        }));
        map.put(Lang.getTemplateGeneration(), new WmSectionMenuItem(Lang.getTemplateGeneration(), FontAwesome.Glyph.FILE_ALT, new EventHandler<Event>() {
            @Override
            public void handle(Event t) {
                ItemEventHandler eh = new WmDocumentOperations(facade).addTemplateItem();
                if (eh != null) {
                    eh.handle(null);
                    reload();
                }
            }
        }));
        if(CpxClientConfig.instance().getSendGdvResponce() ){
            map.put("Dokumente versenden", new WmSectionMenuItem("Dokumente versenden", FontAwesome.Glyph.MAIL_REPLY, new EventHandler<Event>() {
                @Override
                public void handle(Event t) {
                    ItemEventHandler eh = new WmDocumentOperations(facade).addGdvResponceItem();
                    if (eh != null) {
                        eh.handle(null);
                        reload();
                    }
                }
            }));
            
        }
//        map.put("Dokument scannen", new WmSectionMenuItem("Dokument scannen", FontAwesome.Glyph.SPOON, new EventHandler<Event>() {
//            @Override
//            public void handle(Event t) {
//                    try {
//                        showScanningDialog();
//                        tvDocuments.refresh();
//                    } catch (IOException ex) {
//                        Logger.getLogger(WmDocumentSection.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//            }
//        }));

        return map;
    }
    
//    @Override
//    public void setMenu() {
//        Button open = new Button(Lang.getDocumentCreate());
//        open.setMaxWidth(Double.MAX_VALUE);
//        open.setOnMouseClicked((MouseEvent event) -> {
//            if (event.getButton().equals(MouseButton.PRIMARY)) {
//                //showDialogWithFileChooser(getSkin().getWindow(), facade);
//                ItemEventHandler eh = getDetails().createItem();
//                if (eh != null) {
//                    eh.handle(null);
//                    reload();
//                }
//            }
//        });
//
//        Button template = new Button(Lang.getTemplateGeneration());
//        template.setMaxWidth(Double.MAX_VALUE);
//        template.setOnMouseClicked((MouseEvent event) -> {
//            if (event.getButton().equals(MouseButton.PRIMARY)) {
//                ItemEventHandler eh = new WmDocumentOperations(facade).addTemplateItem();
//                if (eh != null) {
//                    eh.handle(null);
//                    reload();
//                }
//            }
//        });
//
////        Button scannerBtn = new Button("Dokument scannen");
////        scannerBtn.setMaxWidth(Double.MAX_VALUE);
////        scannerBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
////            @Override
////            public void handle(MouseEvent event) {
////                if (event.getButton().equals(MouseButton.PRIMARY)) {
////                    try {
////                        showScanningDialog();
////                        tvDocuments.refresh();
////                    } catch (IOException ex) {
////                        Logger.getLogger(WmDocumentSection.class.getName()).log(Level.SEVERE, null, ex);
////                    }
////                }
////            }
////        });
//        Button menu = new Button();
//        menu.getStyleClass().add("cpx-icon-button");
//        menu.setGraphic(getSkin().getGlyph("\uf142"));
//        AutoFitPopOver menuOver = new AutoFitPopOver();
//        menuOver.setFitOrientation(Orientation.HORIZONTAL);
//        menuOver.setArrowLocation(menuOver.getAdjustedLocation());
//        VBox menuBox = new VBox(open, template);
//        menuOver.setContentNode(menuBox);
//
//        menu.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                menuOver.show(menu);
//            }
//        });
//        getSkin().setMenu(menu);
//    }

    @Override
    public TWmDocument getSelectedItem() {
        return tvDocuments.getSelectionModel().getSelectedItem();
    }

    @Override
    public List<TWmDocument> getSelectedItems() {
        return new ArrayList<>(tvDocuments.getSelectionModel().getSelectedItems());
    }

    @Override
    public WmDocumentDetails getDetails() {
        TWmDocument selected = getSelectedItem(); //tvDocuments.getSelectionModel().getSelectedItem();
        WmDocumentDetails details = new WmDocumentDetails(facade, selected);
        return details;
    }

//    @Override
//    public Parent getDetailContent() {
//        TWmDocument selected = getSelectedItem(); //tvDocuments.getSelectionModel().getSelectedItem();
//        WmDetailSection detailSection = new WmDocumentDetails(facade, selected).getDetailSection();
//        return detailSection.getRoot();
//    }
    @Override
    @SuppressWarnings("unchecked")
    protected Parent createContent() {
        long startTime = System.currentTimeMillis();
        VBox contentBox = new VBox();
        VBox.setVgrow(contentBox, Priority.ALWAYS);

        tvDocuments = new AsyncTableView<TWmDocument>() {
            @Override
            public Future<List<TWmDocument>> getFuture() {

                List<TWmDocument> documents = new ArrayList<>(facade.getObsDocuments());
                if (!documents.isEmpty()) {
                    Collections.sort(documents, (TWmDocument document1, TWmDocument document2) -> document2.getCreationDate().compareTo(document1.getCreationDate()));
                }
                return new AsyncResult<>(documents);
            }
        };
        tvDocuments.getStyleClass().add("resize-column-table-view");
        tvDocuments.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//        tvDocuments.setMinHeight(300);
        TableColumn<TWmDocument, Date> col1 = new TableColumn<>(Lang.getDocumentDate());
        col1.prefWidthProperty().bind(Bindings.multiply(tvDocuments.widthProperty(), (1 / 5d)));
        col1.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TWmDocument, Date>, ObservableValue<Date>>() {
            @Override
            public ObservableValue<Date> call(TableColumn.CellDataFeatures<TWmDocument, Date> param) {
                return new SimpleObjectProperty<>(param.getValue().getDocumentDate());
            }
        });
        col1.setCellFactory(new Callback<TableColumn<TWmDocument, Date>, TableCell<TWmDocument, Date>>() {
            @Override
            public TableCell<TWmDocument, Date> call(TableColumn<TWmDocument, Date> param) {
                return new TableCell<TWmDocument, Date>() {
                    @Override
                    protected void updateItem(Date item, boolean empty) {
                        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                        if (item == null || empty) {
                            setGraphic(null);
                            return;
                        }
//                        String content = Lang.toDate(item);
//                        Label lblDate = new Label(content);
//                        setGraphic(lblDate);
//                        OverrunHelper.addOverrunInfoButton(lblDate, content, true);
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

        TableColumn<TWmDocument, TWmDocument> col2 = new TableColumn<>(Lang.getDocumentName());
        col2.prefWidthProperty().bind(Bindings.multiply(tvDocuments.widthProperty(), (2.5 / 5d)));
        col2.setCellValueFactory(new SimpleCellValueFactory<>());
        col2.setCellFactory(new Callback<TableColumn<TWmDocument, TWmDocument>, TableCell<TWmDocument, TWmDocument>>() {
            @Override
            public TableCell<TWmDocument, TWmDocument> call(TableColumn<TWmDocument, TWmDocument> param) {
                TableCell<TWmDocument, TWmDocument> cell = new TableCell<>() {

                    @Override
                    protected void updateItem(TWmDocument item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            setGraphic(null);
                            return;
                        }
//                        setText(((TWmDocument) item).getName());
                        String content = item.getName();
                        Label lblDocName = new Label(content);
                        setGraphic(lblDocName);
//                        OverrunHelper.addOverrunInfoButton(lblDocName, content, true);
                        OverrunHelper.addInfoTooltip(lblDocName);

                    }
                };
                return cell;
            }

        });
        TableColumn<TWmDocument, TWmDocument> col3 = new TableColumn<>(Lang.getDocumentType());
        col3.prefWidthProperty().bind(Bindings.multiply(tvDocuments.widthProperty(), (1.25 / 5d)));
        col3.setCellValueFactory(new SimpleCellValueFactory<>());
        col3.setCellFactory(new Callback<TableColumn<TWmDocument, TWmDocument>, TableCell<TWmDocument, TWmDocument>>() {
            @Override
            public TableCell<TWmDocument, TWmDocument> call(TableColumn<TWmDocument, TWmDocument> param) {
                TableCell<TWmDocument, TWmDocument> cell = new TableCell<>() {

                    @Override
                    protected void updateItem(TWmDocument item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            setGraphic(null);
                            return;
                        }
//                        setText(((TWmDocument) item).getDocumentType());
                        CWmListDocumentType docType = MenuCache.getMenuCacheDocumentTypes().get(item.getDocumentType());
                        //String content = item.getDocumentType();
                        Label lblDocType = new Label(docType == null ? "" : docType.getWmDtName());
                        setGraphic(lblDocType);
                        OverrunHelper.addInfoTooltip(lblDocType);
//                        OverrunHelper.addOverrunInfoButton(lblDocType, content, true);
                    }
                };
                return cell;
            }

        });
        tvDocuments.getColumns().addAll(col1, col2, col3);

        tvDocuments.setOnRowClick((MouseEvent event) -> {
            //                if (MouseButton.PRIMARY.equals(event.getButton())) {
//                    invalidateRequestDetailProperty();
//                }
            if (MouseButton.PRIMARY.equals(event.getButton()) && event.getClickCount() == 2) {
                //Open document on double click
                openItems();
            }
        });

        tvDocuments.setRowContextMenu(createContextMenu());
        tvDocuments.showContextMenuProperty().bind(getArmedProperty());

        contentBox.getChildren().add(tvDocuments);
        LOG.log(Level.FINEST, "create content for workflow number {0} loaded in {1} ms", new Object[]{getProcessNumber(), (System.currentTimeMillis() - startTime)});
        return contentBox;
    }

//    private void handleDocRemoveEvent() {
//        List<TWmDocument> docs = new ArrayList<>(tvDocuments.getSelectionModel().getSelectedItems());
//        if (docs.isEmpty()) {
//            return;
//        }
//        for (TWmDocument doc : docs) {
//            new ConfirmDialog(WmDocumentSection.this.getRoot().getScene().getWindow(), Lang.getDialogQuestionDelete(doc.getName())).showAndWait().ifPresent(new Consumer<ButtonType>() {
//                @Override
//                public void accept(ButtonType t) {
//                    if (t.equals(ButtonType.YES)) {
//                        try {
//                            facade.removeDocument(doc);
//                            //                                    if (connectConfigurationServiceBean.get().getDocumentDatabaseConfig()) {
//                            //                                        m_serviceFacade.removeDocument(item);
//                            //                                    } else if (connectConfigurationServiceBean.get().getDocumentFileSystemConfig()) {
//                            //                                        String serverRootFolder = connectConfigurationServiceBean.get().getServerRootFolder();
//                            //                                        m_serviceFacade.removeDocumentFromFS(item, serverRootFolder);
//                            //                                    }
//                        } catch (IOException ex) {
//                            LOG.log(Level.SEVERE, "Was not able to delete document with id " + (doc == null ? "null" : doc.id), ex);
//                            MainApp.showErrorMessageDialog(ex, "Das Dokument konnte nicht gelöscht werden");
//                        }
//
//                        invalidateRequestDetailProperty();
//                    }
//                    tvDocuments.reload();
//                }
//            });
//        }
//    }
    private ContextMenu createContextMenu() {
        final ContextMenu contextMenu = new CtrlContextMenu<>();
        final MenuItem removeMenuItem = new MenuItem(Lang.getDelete());
        removeMenuItem.setOnAction((ActionEvent event) -> {
            removeItems();
        });

        final MenuItem openMenuItem = new MenuItem(Lang.getFileOpen());
        openMenuItem.setOnAction((ActionEvent event) -> {
            openItems();
        });

        final MenuItem saveMenuItem = new MenuItem(Lang.getFileSave());
        saveMenuItem.setOnAction((ActionEvent event) -> {
            saveItems();
        });
        contextMenu.getItems().addAll(openMenuItem, saveMenuItem, removeMenuItem);
        return contextMenu;
    }

//    private void handleDocOpeningEvent() {
//        List<TWmDocument> docs = new ArrayList<>(tvDocuments.getSelectionModel().getSelectedItems());
//        if (docs.isEmpty()) {
//            return;
//        }
//        for (TWmDocument doc : docs) {
//            DocumentManager.openDocument(doc, processServiceBean.get());
//        }
//    }
    protected void openItems() {
        for (TWmDocument item : getSelectedItems()) {
            openItem(item);
        }
//        reload();
    }

    protected void saveItems() {
        for (TWmDocument item : getSelectedItems()) {
            saveItem(item);
        }
//        reload();
    }

    public void openItem(TWmDocument pItem) {
        ItemEventHandler eh = getOperations().openItem(pItem);
        if (eh != null) {
            eh.handle(null);
            reload();
        }
    }

    public void saveItem(TWmDocument pItem) {
        ItemEventHandler eh = getOperations().saveItem(pItem);
        if (eh != null) {
            eh.handle(null);
            //reload();
        }
    }

//    private void handleDocSavingEvent() {
//        List<TWmDocument> docs = new ArrayList<>(tvDocuments.getSelectionModel().getSelectedItems());
//        if (docs.isEmpty()) {
//            return;
//        }
//        for (TWmDocument doc : docs) {
//            final byte[] documentContent = DocumentManager.getDocumentContent(doc, processServiceBean.get());
//            if (documentContent == null) {
//                return;
//            }
//            FileChooser fileChooser = FileChooserFactory.instance().createFileChooser();
//            //fileChooser.getExtensionFilters().add(extensionFilter);
//            //fileChooser.setTitle(title);
//            fileChooser.setInitialFileName(doc.getName());
//            Window window = WmDocumentSection.this.getRoot().getScene().getWindow();
//            File file;
//            while (true) {
//                file = fileChooser.showSaveDialog(window);
//                if (file != null && FileUtils.isFileLock(file)) {
//                    MainApp.showErrorMessageDialog("Die Datei " + file.getAbsolutePath() + " ist bereits im Zugriff.\n\nSchließen Sie zunächst die offene Anwendung oder wählen Sie einen anderen Dateinamen aus");
//                } else {
//                    break;
//                }
//            }
//            if (file != null) {
//                CpxClientConfig.instance().setUserRecentFileChooserPath(file);
//                boolean stored = false;
//                try ( FileOutputStream fileOuputStream = new FileOutputStream(file)) {
//                    fileOuputStream.write(documentContent);
//                    stored = true;
//                } catch (IOException ex) {
//                    LOG.log(Level.SEVERE, "Was not able to store document: " + file.getAbsolutePath(), ex);
//                    MainApp.showErrorMessageDialog(ex, "Das Dokument '" + file.getAbsolutePath() + "' konnte nicht gespeichert werden!");
//                }
//                if (stored) {
//                    final File f = file;
//                    Notifications notif = NotificationsFactory.instance().createInformationNotification();
//                    notif.text("Datei wurde gespeichert: " + f.getAbsolutePath());
//                    notif.onAction((t) -> {
//                        ToolbarMenuFXMLController.openInExplorer(f.getAbsolutePath());
//                    });
//                    notif.hideAfter(Duration.seconds(5));
//                    notif.show();
//                }
//            }
//        }
//    }
    private void readFileAndShowDialog(File file) {
        if (!checkFileSize(file)) {
            return;
        }
        AddDocumentDialog dialog = getAddDocumentDialog(facade, file);
        if (dialog == null) {
            LOG.severe("Can not show AddDocumentDialog, is null! Do nothing!");
            return;
        }
        dialog.showAndWait();
    }

    public static boolean checkFileSize(final File pFile) {
        if (pFile == null || !pFile.exists() || !pFile.isFile()) {
            return false;
        }
        try {
            DocumentManager.checkFileSize(pFile);
        } catch (IOException ex) {
            LOG.log(Level.FINEST, null, ex);
            MainApp.showErrorMessageDialog(ex.getMessage());
            return false;
        }
        return true;
//        final int maxKb = processServiceBean.get().getDocumentsSizeMax();
//        if (maxKb > 0 && pFile.length() / 1_024D > maxKb) {
//            final String actualSize = org.apache.commons.io.FileUtils.byteCountToDisplaySize(pFile.length());
//            MainApp.showErrorMessageDialog("Die Datei ist zu groß. Dateien dürfen maximal " + Lang.toDecimal(maxKb / 1_024D) + " MB groß sein (aktuelle Größe ist " + actualSize + "):\r\n" + pFile.getAbsolutePath());
//            return false;
//        }
//        return true;
    }

    /**
     * shows the File Add dialog with a file chooser at first For supported
     * files see supportedFileTypes list
     *
     * @param pOwner owner window to initialize dialog to (only for filechooser)
     * @param pFacade serviceFacade to use to store data
     */
    public void showDialogWithFileChooser(Window pOwner, ProcessServiceFacade pFacade) {
        final FileChooser fileChooser = FileChooserFactory.instance().createFileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(Lang.getDocument(), getFileFilterExtension()));
        File file = fileChooser.showOpenDialog(pOwner);
        CpxClientConfig.instance().setUserRecentFileChooserPath(file);
        if (file != null) {
            AddDocumentDialog dialog = getAddDocumentDialog(pFacade, file); // gives null when same document is already stored.
            if (dialog != null) {
                dialog.showAndWait();
            }
            reload();
        }
    }

//    public static void reloadDocuments() {
//        tvDocuments.reload();
//    }
    @Override
    public void reload() {
        super.reload();
        tvDocuments.reload();
    }
    


    public static AddDocumentDialog getAddDocumentDialog(ProcessServiceFacade facade, File file) {

        if (facade.fileExists(file)) {
//            AlertDialog alert = AlertDialog.createErrorDialog(Lang.getFileAlreadyExists(file.getName()), MainApp.getStage());
//            alert.show();
//            return null;
            AlertDialog alert = ConfirmDialog.createConfirmationDialog(Lang.getFileAlreadyExists(file.getName()) + 
                    "\nWollen Sie trotzdem importieren?"
                    , MainApp.getStage());
            Optional<ButtonType> type = alert.showAndWait();
            if(type.get().equals(ButtonType.CANCEL)){
                return null;
            }
//            alert.showAndWait().ifPresent((btnType) -> {
//                if (btnType == ButtonType.CANCEL) {
//
//                }
//              });
        }
        if (!checkFileSize(file)) {
            return null;
        }
        AddDocumentDialog dialog = new AddDocumentDialog(file);
        dialog.setContent(DocumentManager.getFileContent(file));//new byte[(int)file.length()];
        //String fileExtension = FilenameUtils.getExtension(file.getName());
        dialog.valueProperty.addListener(new ChangeListener<TWmDocument>() {
            @Override
            public void changed(ObservableValue<? extends TWmDocument> observable, TWmDocument oldDocument, TWmDocument newDocument) {
                if (newDocument != null) {
//                    tvDocuments.refresh();
                    final byte[] content = newDocument.getContent();
                    final String fileName = file.getName();
                    dialog.valueProperty.removeListener(this);
                    try {
                        if(facade.storeDocument(newDocument, content, fileName)){
                            //TODO archivate
                            if(processServiceBean.get().isDocumentToArchivateafterImport()) {
                                DocumentManager.archivateFile(file, file.getParent());
                            }
                        }
                    } catch (IOException ex) {
                        LOG.log(Level.SEVERE, "Was not able to store document", ex);
                        MainApp.showErrorMessageDialog(ex, "Das Dokument konnte nicht gespeichert werden!");
                    }
                }
            }
        });
        return dialog;
    }

//    @Override
//    protected void editItems() {
//        for (TWmDocument item : getSelectedItems()) {
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
//        for (TWmDocument item : getSelectedItems()) {
//            EventHandler<Event> eh = getDetails().removeItem(item);
//            if (eh != null) {
//                eh.handle(null);
//            }
//        }
//        reload();
//    }
    @Override
    public WmDocumentOperations getOperations() {
        return new WmDocumentOperations(facade);
    }

}
