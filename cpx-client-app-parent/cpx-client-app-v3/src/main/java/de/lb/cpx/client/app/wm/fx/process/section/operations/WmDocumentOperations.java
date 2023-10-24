/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.process.section.operations;

import de.FileUtils;
import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.dialog.AddDocByTemplateDialog;
import de.lb.cpx.client.app.wm.fx.dialog.AddDocumentDialog;
import de.lb.cpx.client.app.wm.fx.dialog.SendGdvDocumentsDialog;
import de.lb.cpx.client.app.wm.fx.process.section.WmDocumentSection;
import de.lb.cpx.client.app.wm.util.DocumentManager;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.fx.ToolbarMenuFXMLController;
import de.lb.cpx.client.core.model.fx.alert.ConfirmDialog;
import de.lb.cpx.client.core.model.fx.alert.NotificationsFactory;
import de.lb.cpx.client.core.model.fx.file_chooser.FileChooserFactory;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.service.ejb.ProcessServiceBeanRemote;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmDocument;
import de.lb.cpx.wm.model.TWmProcess;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.glyphfont.FontAwesome;

/**
 *
 * @author niemeier
 */
public class WmDocumentOperations extends WmOperations<TWmDocument> {

    private static final Logger LOG = Logger.getLogger(WmDocumentOperations.class.getName());

    public WmDocumentOperations(ProcessServiceFacade pFacade) {
        super(pFacade);
    }

//    @Override
//    protected Button createMenuItem() {
//        //NOT IMPLEMENTED YET
//        return null;
//    }
    @Override
    protected List<ItemEventHandler> getOtherOperations(final TWmDocument pItem) {
        final List<ItemEventHandler> result = new ArrayList<>();
//        EventHandler<Event> openItem = openItem(pItem);
//        if (openItem != null) {
//            result.add(new ItemEventHandler(openItem, ResourceLoader.getGlyph(FontAwesome.Glyph.FOLDER_OPEN), "Öffnen", true));
//        }
        ItemEventHandler saveItem = saveItem(pItem);
        if (saveItem != null) {
            result.add(saveItem);
        }
//        ItemEventHandler addTemplateItem = addTemplateItem();
//        if (addTemplateItem != null) {
//            result.add(addTemplateItem);
//        }
        return result;
    }

    @Override
    public ItemEventHandler removeItem(final TWmDocument pItem) {
        if (pItem == null) {
            return null;
        }
        return new ItemEventHandler(FontAwesome.Glyph.TRASH, Lang.getEventOperationRemove(), FontAwesome.Glyph.TRASH, Lang.getEventOperationRemoveItem(getItemName()), false) {
            @Override
            public void handle(ActionEvent evt) {
                new ConfirmDialog(MainApp.getWindow(), Lang.getDialogQuestionDelete(pItem.getName())).showAndWait().ifPresent(new Consumer<ButtonType>() {
                    @Override
                    public void accept(ButtonType t) {
                        if (t.equals(ButtonType.YES)) {
                            try {
                                facade.removeDocument(pItem);
                            } catch (IOException ex) {
                                LOG.log(Level.SEVERE, "Was not able to delete document with id " + (pItem == null ? "null" : pItem.id), ex);
                                MainApp.showErrorMessageDialog(ex, "Das Dokument konnte nicht gelöscht werden");
                            }
//                            invalidateRequestDetailProperty();
                        }
//                        tvDocuments.reload();
                    }
                });
            }
        };
    }

    /**
     * shows the File Add dialog with a file chooser at first For supported
     * files see supportedFileTypes list
     *
     * @return handler
     */
    @Override
    public ItemEventHandler createItem() {
        return new ItemEventHandler(FontAwesome.Glyph.FILE, Lang.getEventOperationCreate(), FontAwesome.Glyph.FILE, Lang.getEventOperationCreateItem(getItemName()), false) {
            @Override
            public void handle(ActionEvent evt) {
                final FileChooser fileChooser = FileChooserFactory.instance().createFileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(Lang.getDocument(), WmDocumentSection.getFileFilterExtension()));
                //Window owner = getSkin().getWindow();
                Window owner = MainApp.getWindow();
                File file = fileChooser.showOpenDialog(owner);
                CpxClientConfig.instance().setUserRecentFileChooserPath(file);
                if (file != null) {
                    AddDocumentDialog dialog = WmDocumentSection.getAddDocumentDialog(facade, file); // gives null when same document is already stored.
                    if (dialog != null) {
                        dialog.showAndWait();
                    }
//                    reload();
                }
            }
        };
    }

//    public void openItems(final List<TWmDocument> pItems) {
//        if (pItems == null || pItems.isEmpty()) {
//            return;
//        }
//        for (TWmDocument item : new ArrayList<>(pItems)) {
//            openItem(item);
//        }
//    }
    @Override
    public ItemEventHandler openItem(final TWmDocument pItem) {
        if (pItem == null) {
            return null;
        }
        return new ItemEventHandler(FontAwesome.Glyph.PAPERCLIP, Lang.getEventOperationOpen(), FontAwesome.Glyph.PAPERCLIP, Lang.getEventOperationOpenItem(getItemName()), true) {
            @Override
            public void handle(ActionEvent evt) {
//                EjbProxy<ProcessServiceBeanRemote> processServiceBean = Session.instance().getEjbConnector().connectProcessServiceBean();
                DocumentManager.openDocument(pItem, facade);
            }
        };
    }

//    protected void saveItems(final List<TWmDocument> pItems) {
//        if (pItems == null || pItems.isEmpty()) {
//            return;
//        }
//        for (TWmDocument item : new ArrayList<>(pItems)) {
//            saveItem(item);
//        }
//    }
    public ItemEventHandler saveItem(final TWmDocument pItem) {
        if (pItem == null) {
            return null;
        }
        return new ItemEventHandler(FontAwesome.Glyph.SAVE, "Speichern", FontAwesome.Glyph.SAVE, getItemName() + " speichern", false) {
            @Override
            public void handle(ActionEvent evt) {
                EjbProxy<ProcessServiceBeanRemote> processServiceBean = Session.instance().getEjbConnector().connectProcessServiceBean();
                final byte[] documentContent = DocumentManager.getDocumentContent(pItem, processServiceBean.get());
                if (documentContent == null) {
                    return;
                }
                FileChooser fileChooser = FileChooserFactory.instance().createFileChooser();
                //fileChooser.getExtensionFilters().add(extensionFilter);
                //fileChooser.setTitle(title);
                fileChooser.setInitialFileName(pItem.getName());
                Window window = MainApp.getWindow();
                File file;
                while (true) {
                    file = fileChooser.showSaveDialog(window);
                    if (file != null && FileUtils.isFileLock(file)) {
                        MainApp.showErrorMessageDialog("Die Datei " + file.getAbsolutePath() + " ist bereits im Zugriff.\n\nSchließen Sie zunächst die offene Anwendung oder wählen Sie einen anderen Dateinamen aus");
                    } else {
                        break;
                    }
                }
                if (file != null) {
                    CpxClientConfig.instance().setUserRecentFileChooserPath(file);
                    boolean stored = false;
                    try (FileOutputStream fileOuputStream = new FileOutputStream(file)) {
                        fileOuputStream.write(documentContent);
                        stored = true;
                    } catch (IOException ex) {
                        LOG.log(Level.SEVERE, "Was not able to store document: " + file.getAbsolutePath(), ex);
                        MainApp.showErrorMessageDialog(ex, "Das Dokument '" + file.getAbsolutePath() + "' konnte nicht gespeichert werden!");
                    }
                    if (stored) {
                        final File f = file;
                        Notifications notif = NotificationsFactory.instance().createInformationNotification();
                        notif.text("Datei wurde gespeichert: " + f.getAbsolutePath());
                        notif.onAction((t) -> {
                            ToolbarMenuFXMLController.openInExplorer(f.getAbsolutePath());
                        });
                        notif.hideAfter(Duration.seconds(5));
                        notif.show();
                    }
                }
            }
        };
    }

    public ItemEventHandler addTemplateItem() {
        return new ItemEventHandler(FontAwesome.Glyph.FILE_WORD_ALT, "Vorlage", FontAwesome.Glyph.FILE_WORD_ALT, Lang.getTemplateGeneration(), false) {
            @Override
            public void handle(ActionEvent evt) {
                TWmProcess currentProcess = facade.getCurrentProcess();
                //        AddTemplateDialog dialog = new AddTemplateDialog(m_serviceFacade, currentProcess);
                if (currentProcess.getMainCase() == null) {
                    LOG.log(Level.WARNING, "no main case found for process {0}", String.valueOf(currentProcess));
                    MainApp.showErrorMessageDialog(Lang.getProcessMainCaseError());
                    //            MainApp.showInfoMessageDialog("Legen Sie in der Leistungsübersicht zunächst einen Basisfall fest!", getSkin().getWindow());
                    return;
                }
                AddDocByTemplateDialog dialog = new AddDocByTemplateDialog(facade, currentProcess);
                dialog.showAndWait();
            }
        };
    }

    @Override
    public String getItemName() {
        return Lang.getEventNameDocument();
    }

//    public final EventHandler<Event> openItem() {
//        return openItem(getItem());
//    }
//
//    public final EventHandler<Event> saveItem() {
//        return saveItem(getItem());
//    }

    public ItemEventHandler addGdvResponceItem() {
        return new ItemEventHandler(FontAwesome.Glyph.MAIL_REPLY, "Dokumente versenden", FontAwesome.Glyph.MAIL_REPLY,  "Gdv - Dokumente versenden", false) {
            @Override
            public void handle(ActionEvent evt) {
                TWmProcess currentProcess = facade.getCurrentProcess();
                //        AddTemplateDialog dialog = new AddTemplateDialog(m_serviceFacade, currentProcess);
                if (currentProcess.getMainCase() == null) {
                    LOG.log(Level.WARNING, "no main case found for process {0}", String.valueOf(currentProcess));
                    MainApp.showErrorMessageDialog(Lang.getProcessMainCaseError());
                    //            MainApp.showInfoMessageDialog("Legen Sie in der Leistungsübersicht zunächst einen Basisfall fest!", getSkin().getWindow());
                    return;
                }
                SendGdvDocumentsDialog dialog = new SendGdvDocumentsDialog(facade, currentProcess);
                dialog.showAndWait();
            }
        };
    }
}
