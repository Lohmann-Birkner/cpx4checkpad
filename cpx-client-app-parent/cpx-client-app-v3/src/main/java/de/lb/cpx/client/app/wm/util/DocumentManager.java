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
package de.lb.cpx.client.app.wm.util;

import com.google.common.io.Files;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComFailException;
import com.jacob.com.Dispatch;
import com.jacob.com.DispatchEvents;
import com.jacob.com.InvocationProxy;
import com.jacob.com.LibraryLoader;
import com.jacob.com.Variant;
import de.FileUtils;
import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.fx.ToolbarMenuFXMLController;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.document.Utils;
import de.lb.cpx.reader.DocumentReader;
import de.lb.cpx.reader.util.OS;
import de.lb.cpx.service.ejb.ProcessServiceBeanRemote;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.str.utils.FileNameUtils;
import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.wm.model.TWmDocument;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystemNotFoundException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.HostServices;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.util.Callback;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.util.TempFile;

/**
 * Handles the Documents in the Client, offers Methodes to download a file or
 * get the local copy of the file if its already downloaded
 *
 * @author wilde
 */
public class DocumentManager {

    private static final String DOCUMENTS_PATH = "/Documents/" + Session.instance().getCpxUserName() + "/";
    private static final Logger LOG = Logger.getLogger(DocumentManager.class.getName());

    /**
     * loads the content of the TwmDocument Entity
     *
     * @param document Document Entity
     * @return loaded JavaFile
     * @throws java.nio.file.FileSystemNotFoundException io exception if file
     * can not be accessed
     */
    public static File loadDocument(TWmDocument document) throws FileSystemNotFoundException {
        return loadDocument(document.getId(), document.getName(), document.getModificationDate());
    }

    /**
     * loads an document with its name and modification date
     *
     * @param documentId documentId to find stored entity
     * @param documentName name of the document
     * @param modificationDate date of modification, used to determine if the
     * local file must be replaced or not
     * @return loaded javafile
     * @throws java.nio.file.FileSystemNotFoundException io exception if file
     * can not be accessed
     */
    public static File loadDocument(long documentId, String documentName, Date modificationDate) throws FileSystemNotFoundException {
        String userDir = CpxSystemProperties.getInstance().getUserDir();
        File localDocument = new File(userDir + DOCUMENTS_PATH + documentName);
        //if existing check if modificationstimes match, if so return localcopy else load from server
        //needs to be tested 20161101
        //no modification possible by now 20161102, documentManagement is not yet designed
        //user need to add a new document therefore its only checked if document with that name exists, if true 
        //return localcopy
        if (localDocument.exists()) {
//            if(modificationDate != null){
//                BasicFileAttributes attr = Files.readAttributes(localDocument.toPath(), BasicFileAttributes.class);
//                if(attr.lastModifiedTime().toMillis() == modificationDate.getTime()){
            return localDocument;
//                }
//            }
        }
        //write file and its content to the disk
        EjbProxy<ProcessServiceBeanRemote> processEJB = Session.instance().getEjbConnector().connectProcessServiceBean();
        if (!localDocument.getParentFile().exists()) {
            localDocument.getParentFile().mkdirs();
        }
        try ( FileOutputStream fos = new FileOutputStream(localDocument)) {
            if (processEJB.get().getDocumentContent(documentId) != null) {
                fos.write(processEJB.get().getDocumentContent(documentId));
            } else {
                LOG.log(Level.WARNING, "Document content is null!");
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Was not able to get content from document " + localDocument.getAbsolutePath(), ex);
        }
        if (!localDocument.exists() || localDocument.length() == 0) {
            throw new FileSystemNotFoundException("Can not load File " + documentName + " at " + userDir + DOCUMENTS_PATH);
        }
        return localDocument;
    }

    /**
     * edit file with the default editing program in the system (most likely
     * Notepad, UltraEdit or Notepad++)
     *
     * @param file file to edit
     * @return edit program found?
     */
    public static boolean editFile(File file) {
        return ToolbarMenuFXMLController.editFile(file);
    }

    /**
     * shows file with the default configurated programm in the system
     *
     * @param file file to open
     */
    public static void showFile(File file) {
        if (file == null) {
            return;
        }
        LOG.log(Level.INFO, "show file " + file.getAbsolutePath());

        if (!file.exists()) {
            LOG.log(Level.WARNING, "file to show does not exist: " + file.getAbsolutePath());
        }

        //Desktop desktop = Desktop.getDesktop();
        //desktop.open(file);
        HostServices hostServices = MainApp.instance().getHostServices();
        hostServices.showDocument(file.toURI().toString());

        // should be replaced with some sort of autosave
//        Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK, ButtonType.CANCEL);
//        alert.setHeaderText("No AutoSave active for " + file.getName());
//        alert.setContentText("Changes to the document are not saved in the database!" + "\n"
//                + "To store the changes in the Database you must add the newly corrected File to the Process via Drag&Drop or the FileChoose Dialog.");
//        Optional<ButtonType> btn = alert.showAndWait();
//        if (btn.get().equals(ButtonType.OK)) {
//        }
    }
    public static void showFileAsTempFile(File pFile){
        if(pFile == null){
            LOG.warning("Can not open File As Tempfile, File is null!");
            return;
        }
        if(pFile.length() == 0L){
            LOG.warning("Can not open File As Tempfile, File is empty!");
            return;
        }
        if (!pFile.exists()) {
            LOG.log(Level.WARNING, "file to show does not exist: " + pFile.getAbsolutePath());
            return;
        }
        File tempFile = null;
        try {
            //copy file to temp?
            tempFile = File.createTempFile(pFile.getName(), new StringBuilder(".").append(Files.getFileExtension(pFile.getAbsolutePath())).toString());
        } catch (IOException ex) {
            Logger.getLogger(DocumentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(tempFile == null){
            LOG.warning("Can not open File As Tempfile, TempFile Creation Failed!");
            return;
        }
        if(!FileUtils.copyFile(pFile,tempFile)){
            MainApp.showErrorMessageDialog("Öffnen der Datei: " + pFile.getName() + " konnte nicht ausgeführt werden!");
            tempFile.deleteOnExit();
            return;
        }
        showFile(tempFile);
        tempFile.deleteOnExit();
    }
    public static List<String> getSelectedOutlookAttachments(final Dragboard pDragboard) {
        final List<String> fileNames = new ArrayList<>();
        if (pDragboard == null) {
            return fileNames;
        }
        final Set<DataFormat> pDataFormats = pDragboard.getContentTypes();
        if (pDataFormats == null || pDataFormats.isEmpty()) {
            return fileNames;
        }
        Iterator<DataFormat> it = pDataFormats.iterator();
        while (it.hasNext()) {
            DataFormat item = it.next();
            Iterator<String> it2 = item.getIdentifiers().iterator();
            while (it2.hasNext()) {
                String item2 = it2.next();
                if (item2 != null && item2.contains("external-body")) {
                    String key = "name=";
                    int pos = item2.indexOf(key);
                    if (pos > -1) {
                        String tmp = item2.substring(pos + key.length()).trim();
                        if (tmp.startsWith("\"")) {
                            tmp = tmp.substring(1);
                        }
                        if (tmp.endsWith("\"")) {
                            tmp = tmp.substring(0, tmp.length() - 1);
                        }
                        tmp = tmp.trim();
                        if (!tmp.isEmpty()) {
                            fileNames.add(tmp);
                        }
                    }
                }
            }
        }
        return fileNames;
    }

    /**
     * gets the file content from a specific file extraction handels as task,
     * blocks till its finished
     *
     * @param file file to extract content from
     * @return byte array of the content
     */
    public static byte[] getFileContent(File file) {
        if (file == null) {
            LOG.log(Level.WARNING, "file is null");
            return null;
        }
        try {
            return getFileContentTask(file).get();
        } catch (ExecutionException ex) {
            LOG.log(Level.SEVERE, "Was not able to get file content for " + file.getAbsolutePath(), ex);
        } catch (InterruptedException ex) {
            LOG.log(Level.SEVERE, "Was not able to get file content for " + file.getAbsolutePath(), ex);
            Thread.currentThread().interrupt();
        }
        return null;
    }

    public static void checkFileSize(final File pFile) throws IOException {
        if (pFile == null || !pFile.exists() || !pFile.isFile()) {
            return;
        }
        final int maxKb = Session.instance().getDocumentsSizeMax();
        if (maxKb > 0 && pFile.length() / 1_024D > maxKb) {
            final String actualSize = org.apache.commons.io.FileUtils.byteCountToDisplaySize(pFile.length());
            throw new IOException("Die Datei ist zu groß. Es ist eine maximale Größe von " + Lang.toDecimal(maxKb / 1_024D) + " MB zulässig (die tatsächliche Dateigröße beträgt " + actualSize + "): " + pFile.getAbsolutePath());
            //MainApp.showErrorMessageDialog("Die Datei ist zu groß. Dateien dürfen maximal " + Lang.toDecimal(maxKb / 1_024D) + " MB groß sein (aktuelle Größe ist " + actualSize + "):\r\n" + pFile.getAbsolutePath());
//            return false;
        }
//        return true;
    }

    /**
     * gets the loading task for the file content
     *
     * @param file file to extract content
     * @return future task, it runs in background, result can be obtained by
     * task.get()
     */
    public static FutureTask<byte[]> getFileContentTask(File file) {
        byte[] content = new byte[(int) file.length()];
        FutureTask<byte[]> task = new FutureTask<>(() -> {
            try ( FileInputStream fin = new FileInputStream(file.getAbsolutePath())) {
                int numberOfBytes = fin.read(content);
            }
            return content;
        });
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
        return task;
    }

//    /**
//     * Generate file (e.g. report) on client on specific location like, temp dir
//     * or user specific path
//     *
//     * @param reportFromServer file path from the server.
//     * @param locToCreateFile location where report can generate on Client
//     * @return File generated report on Client
//     * @throws IOException exception if some IO problems
//     */
//    public static File createFileOnSpecificLoc(File reportFromServer, String locToCreateFile) throws IOException {
//        File fileOnClient = new File(locToCreateFile);
//        if (!fileOnClient.getParentFile().exists()) {
//            fileOnClient.getParentFile().mkdirs();
//        }
//
////        FileOutputStream out = new FileOutputStream(filename);
//        try ( FileOutputStream out = new FileOutputStream(fileOnClient);  FileInputStream in = new FileInputStream(reportFromServer)) {
//            //        FileOutputStream out = new FileOutputStream(filename);
//
//            int length;
//
////        byte[] byteArray = locToGetFile.getBytes();
////        byte[] byteArray = reportFromServer.getAbsolutePath().getBytes(); // get bytes of File using abs. path
//            byte[] byteArray = IOUtils.toByteArray(in); // get bytes from File object
//            try ( OutputStream os = new BufferedOutputStream(out)) {
//                os.write(byteArray);
//                os.flush();
//                while ((length = in.read(byteArray)) > -1) {
//                    out.write(byteArray, 0, length);
//                }
//
//                out.flush();
//            }
//        }
//
//        return fileOnClient;
//    }
    /**
     * Is this file name syntactically correct?
     *
     * @param sFile file to check
     * @return is valid?
     */
    public static boolean isValidFilename(String sFile) {
        return FileUtils.isValidFilename(sFile);
    }

    /**
     * Is this file path syntactically correct?
     *
     * @param sFile file to check
     * @return is valid?
     */
    public static boolean isValidFilepath(String sFile) {
        return FileUtils.isValidFilepath(sFile);
    }

    /**
     * replaces illegal characters
     *
     * @param sFile file to validate
     * @return validated file name
     */
    public static String validateFilename(String sFile) {
        return FileUtils.validateFilename(sFile);
    }

    /**
     * Generate file (e.g. report) on client on specific location like, temp dir
     * or user specific path
     *
     * @param reportContent file content from the server.
     * @param locToCreateFile location where report can generate on Client
     * @return File generated report on Client
     * @throws IOException exception if some IO problems
     */
    public static File createFileInTempOrSpecificDir(byte[] reportContent, String locToCreateFile) throws IOException {
        if (locToCreateFile == null || locToCreateFile.trim().isEmpty()) {
            MainApp.showErrorMessageDialog("There was no file name passed (parameter is null or empty)");
            throw new IOException("There was no file name passed (parameter is null or empty)");
        }
        if (!isValidFilepath(locToCreateFile)) {
            MainApp.showErrorMessageDialog(new IOException("This is not a valid file name: " + locToCreateFile), "Please give a valid file name.");
//            MainApp.showErrorMessageDialog("This is not a valid file name: " + locToCreateFile);
            throw new IOException("This is not a valid file name: " + locToCreateFile);
        }
        File fileOnClient = new File(locToCreateFile);
        if (!fileOnClient.getParentFile().exists()) {
            fileOnClient.getParentFile().mkdirs();
        }

        String filename = fileOnClient.getAbsolutePath();
        Pattern PATTERN = Pattern.compile("(.*?)(?:\\((\\d+)\\))?(\\.[^.]*)?");
        if (fileOnClient.exists()) {
            Matcher m = PATTERN.matcher(filename);
            if (m.matches()) {
                String prefix = m.group(1);
                String last = m.group(2);
                String suffix = m.group(3);
                if (suffix == null) {
                    suffix = "";
                }
                int count = last != null ? Integer.parseInt(last) : 0;
                do {
                    count++;
                    filename = prefix + " (" + count + ")" + suffix;
                } while (fileExists(fileOnClient, filename));
            }
//            MainApp.showErrorMessageDialog("Report with same name is already open");
        }

        File newFile = new File(filename);
//        try (FileOutputStream out = new FileOutputStream(fileOnClient)) {
        try ( FileOutputStream out = new FileOutputStream(newFile)) {
            out.write(reportContent);
            out.flush();
        }

        return newFile;
//        return fileOnClient;
    }

    private static boolean fileExists(File fileOnClient, String filename) {
//        String absolutePath = fileOnClient.getParentFile().getAbsolutePath();
//        String filePathString = absolutePath + "\\" + filename;
        File f = new File(filename);
        return f.exists() && !f.isDirectory();
    }

    public static File createFileInSpecificDir(byte[] reportContent, String locToCreateFile) throws IOException {
        File fileOnClient = new File(locToCreateFile);
        if (!fileOnClient.getParentFile().exists()) {
            fileOnClient.getParentFile().mkdirs();
        }

        try ( FileOutputStream out = new FileOutputStream(fileOnClient)) {
            out.write(reportContent);

            out.flush();
        }

        return fileOnClient;
    }

    public static byte[] getDocumentContent(final TWmDocument pWmDoc,
            final ProcessServiceBeanRemote pProcessServiceBean) {
        if (pWmDoc == null) {
            LOG.log(Level.SEVERE, "pWmDoc is null!");
            return null;
        }
        //1st step: retrieve document content from server
        byte[] documentContent;
        try {
            documentContent = pWmDoc.getContent() != null ? pWmDoc.getContent() : pProcessServiceBean.getDocumentContent(pWmDoc.getId());
            if (documentContent == null && pWmDoc.isDatabaseContentDocument()) {
                LOG.log(Level.WARNING, "Content of document {0} is null!", pWmDoc);
                final String message = MessageFormat.format("Das angeforderte Dokument ''{0}'' enthält keine Daten!", pWmDoc.getName());
                MainApp.showErrorMessageDialog(message);
            }
        } catch (FileNotFoundException ex) {
            LOG.log(Level.WARNING, MessageFormat.format("File of document {0} was not found on server", pWmDoc), ex);
            MainApp.showErrorMessageDialog("Die Datei " + pWmDoc.getFilePath() + " konnte auf dem Server nicht gefunden werden!"
                    + "\n\nMöglicherweise trat beim Speichern des Dokuments ein Fehler auf oder die Datei wurde manuell vom Dateisystem gelöscht.");
            return null;
        } catch (IOException | IllegalArgumentException ex) {
            LOG.log(Level.SEVERE, "Was not able to retrieve document content from server", ex);
            MainApp.showErrorMessageDialog(ex, MessageFormat.format("Das Dokument mit der ID {0} konnte nicht vom Server geladen werden", pWmDoc.id));
            return null;
        }
        return documentContent;
    }

    public static void openDocument(final TWmDocument pWmDoc,
            final ProcessServiceFacade pFacade) {
        final byte[] documentContent = getDocumentContent(pWmDoc, pFacade.getProcessServiceBean());
        if (documentContent == null) {
            LOG.log(Level.WARNING, "Content of requested document {0} is null!", pWmDoc);
            return;
        }
        if (documentContent.length == 0) {
            LOG.log(Level.INFO, "The requested document {0} contains no data.", pWmDoc);
            final String message = MessageFormat.format("Das angeforderte Dokument ''{0}'' enthält keine Daten!", pWmDoc.isFileContentDocument() ? pWmDoc.getFilePath() : pWmDoc.getName());
            MainApp.showErrorMessageDialog(message);
            return;
        }
        
        String property = System.getProperty("java.io.tmpdir");  // temp dir on client
        final String locToCreateFile = property + pWmDoc.getName();
        File tempFile = null;
        try {
            //2nd step: write document content to a temporary file
            try {
                tempFile = DocumentManager.createFileInTempOrSpecificDir(documentContent, locToCreateFile);
            } catch (IOException | IllegalArgumentException ex) {
                LOG.log(Level.SEVERE, MessageFormat.format("Was not able to create temporary file on client: {0}", locToCreateFile), ex);
                MainApp.showErrorMessageDialog(ex, MessageFormat.format("Beim Erzeugen der temporären Datei {0} trat ein Fehler auf", locToCreateFile));
                return;
            }
            if(DocumentReader.isOfficeEnabled() && FileNameUtils.isWordDocument(tempFile)){
                final File tempFile1 = tempFile;
                showFileInWord(tempFile, new Callback<ButtonBar.ButtonData, Boolean>() {
                    @Override
                    public Boolean call(ButtonBar.ButtonData param) {
                        if(param.equals(ButtonBar.ButtonData.OK_DONE)){
                            saveTempFile(tempFile1,pFacade);
                        }
                        //delete when CPX Client is exiting
                        if (tempFile1 != null && tempFile1.exists()) {
                            FileUtils.deleteFile(tempFile1);
                        }
                        return true;
                    }

                    private void saveTempFile(File tempFile, ProcessServiceFacade pFacade) {
                        
                        TWmDocument newDoc = new TWmDocument();
                        newDoc.setDocumentDate(new Date());
                        newDoc.setName(createFileName(tempFile,"-","Kopie"));
                        newDoc.setProcess(pWmDoc.getProcess());
                        newDoc.setDocumentType(pWmDoc.getDocumentType());
                        try {
                            final byte[] content = java.nio.file.Files.readAllBytes(tempFile.toPath());
                            final String extension = FilenameUtils.getExtension(tempFile.getName());
                            pFacade.storeDocument(newDoc, content, extension);
                        } catch (IOException ex) {
                            LOG.log(Level.SEVERE, "Was not able to store word document", ex);
                            MainApp.showErrorMessageDialog(ex, "Das Word-Dokument konnte nicht gespeichert werden!");
                        }
                    }

                    private String createFileName(File pFile, String pDelimiter, String pText) {
                        if(pFile == null){
                            return "";
                        }
                        pDelimiter = Objects.requireNonNullElse(pDelimiter, "").trim();
                        pText = Objects.requireNonNullElse(pText, "").trim();
                        if(pDelimiter.isEmpty()){
                            return "";
                        }
                        if(pText.isEmpty()){
                            return "";
                        }
                        String fileName = Objects.requireNonNullElse(pFile.getName(),"").trim();
                        if(fileName.isEmpty()){
                            return "";
                        }
                        if(fileName.contains(pDelimiter)){
                            String extension = FilenameUtils.getExtension(fileName);
                            fileName = fileName.split(pDelimiter)[0].trim();
                            fileName = fileName+"."+extension;
                        }
                        String suffix = new StringBuilder(" "+pDelimiter+" ").append(pText).append(" (").append(Lang.toFileDateTime(new Date())).append(")").toString();
                        return FileNameUtils.addFileSuffix(fileName,suffix);
                    }
                });
            }else{
                //3th step: open that temporary file in an application
                showFile(tempFile);
            }
        } finally {
            //delete when CPX Client is exiting
            if (tempFile != null && tempFile.exists()) {
                tempFile.deleteOnExit();
            }
        }
    }
    private static boolean closeWord(final ActiveXComponent pWord) {
        if (pWord == null) {
            return true;
        }
        try {
            pWord.invoke("Quit", 0);
            //maybe useful? -> ComThread.Release();
        } catch (ComFailException ex) {
            LOG.log(Level.SEVERE, "Was not able to close document", ex);
            return false;
        }
        return true;
    }
    public static boolean deleteFile(final File pFile, final boolean pDeleteOnExit) {
        return FileUtils.deleteFile(pFile, pDeleteOnExit);
    }

    public static boolean deleteFile(final File pFile) {
        return FileUtils.deleteFile(pFile);
    }

    public static boolean isFileLock(final File pFile) {
        return FileUtils.isFileLock(pFile);
    }

//    public static boolean isOpen(File file) {
//        boolean isFileUnlocked = false;
//        try {
////      It creates a new file with size 0 or, if the file exists already, it is opened and
////      closed without modifying it, but updating the file date and time.
//            FileUtils.touch(file);
//            isFileUnlocked = true;
//        } catch (IOException e) {
//            LOG.log(Level.FINEST, "Was not able to create file (maybe already exists): " + file.getAbsolutePath(), e);
//            isFileUnlocked = false;
//        }
//        if (isFileUnlocked) {
//            // Do stuff you need to do with a file that is NOT locked.
//        } else {
//            // Do stuff you need to do with a file that IS locked
//        }
//
//        return isFileUnlocked;
//    }
    /**
     * Get updated filename if same name is already exists on the same path
     *
     * @param locToCreateFile location where file can be created on Client
     * @return String new filename with full abs. path
     * @throws IOException exception if some IO problems
     */
    public static String createFileInTempOrSpecificDir(String locToCreateFile) throws IOException {
        if (locToCreateFile == null || locToCreateFile.trim().isEmpty()) {
            throw new IOException("There was no file name passed (parameter is null or empty)");
        }
        if (!isValidFilepath(locToCreateFile)) {
            throw new IOException("This is not a valid file name: " + locToCreateFile);
        }
        File fileOnClient = new File(locToCreateFile);
        if (!fileOnClient.getParentFile().exists()) {
            fileOnClient.getParentFile().mkdirs();
        }

        String filename = fileOnClient.getAbsolutePath();
        Pattern PATTERN = Pattern.compile("(.*?)(?:\\((\\d+)\\))?(\\.[^.]*)?");
        if (fileOnClient.exists()) {
            Matcher m = PATTERN.matcher(filename);
            if (m.matches()) {
                String prefix = m.group(1);
                String last = m.group(2);
                String suffix = m.group(3);
                if (suffix == null) {
                    suffix = "";
                }
                int count = last != null ? Integer.parseInt(last) : 0;
                do {
                    count++;
                    filename = prefix + " (" + count + ")" + suffix;
                } while (fileExists(fileOnClient, filename));
            }
        }

        return filename;
    }

    public static String createFileInTempOrSpecificDir(File file) throws IOException {
        if (file == null || file.getAbsolutePath() == null || file.getAbsolutePath().trim().isEmpty()) {
            throw new IOException("There was no file passed (parameter is null or empty)");
        }
        if (!isValidFilepath(file.getAbsolutePath())) {
            throw new IOException("This is not a valid file name: " + file.getAbsolutePath());
        }

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        String filename = file.getAbsolutePath();
        Pattern PATTERN = Pattern.compile("(.*?)(?:\\((\\d+)\\))?(\\.[^.]*)?");
        if (file.exists()) {
            Matcher m = PATTERN.matcher(filename);
            if (m.matches()) {
                String prefix = m.group(1);
                String last = m.group(2);
                String suffix = m.group(3);
                if (suffix == null) {
                    suffix = "";
                }
                int count = last != null ? Integer.parseInt(last) : 0;
                do {
                    count++;
                    filename = prefix + " (" + count + ")" + suffix;
                } while (fileExists(file, filename));
            }

//            MainApp.showWarningMessageDialog("Do you want to rename " + file.getAbsolutePath() + " to " + filename + " ?" + "\n"
//                    + "There is already a file with the same name in this location.");
        }

        return filename;
    }

    public static boolean showInExplorer(final File pFile) {
        return ToolbarMenuFXMLController.openInExplorer(pFile);
    }


    public static synchronized boolean archivateFile(final File file, String pArchivePath) {

        Calendar cal = Calendar.getInstance();
        String year = String.valueOf(cal.get(Calendar.YEAR));
        String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
        if (month.length() == 1) {
            month = "0" + month;
        }
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss.SSS");
        final Date now = new Date();
        final String strDate = sdf.format(now);

        boolean archivated = false;
        final File archiveFile = new File(pArchivePath + "//Archiv//" + year + "//" + month + "//" + strDate + "_" + file.getName());
        if (!file.exists()) {
            LOG.log(Level.WARNING, "Was not able to move file ''{0}'' to archive folder, because file does not exist (anymore)", file.getName());
            //MainApp.showErrorMessageDialog("Die Datei '" + file.getName() + "' kann nicht ins Archivverzeichnis verschoben werden, weil die Datei nicht mehr existiert");
        } else {
            if (!archiveFile.getParentFile().exists()) {
                archiveFile.getParentFile().mkdirs();
                if (!archiveFile.getParentFile().exists()) {
                    LOG.log(Level.SEVERE, "was not able to archive/move imported document from {0} to {1} (was not able to create not existing directory)", new Object[]{file.getAbsolutePath(), archiveFile.getAbsolutePath()});
//                    MainApp.showErrorMessageDialog("Die Datei '" + archiveFile.getName() + "' kann nicht ins Archivverzeichnis verschoben werden, weil das Verzeichnis nicht angelegt werden konnte: " + archiveFile.getParentFile().getAbsolutePath());
                    MainApp.showErrorMessageDialog(Lang.getFileArchiveErrorParentNotFound(archiveFile.getName(),archiveFile.getParentFile().getAbsolutePath()));
                }
            }
            if (archiveFile.getParentFile().exists()) {
                if (archiveFile.getParentFile().isFile()) {
                    LOG.log(Level.SEVERE, "was not able to archive/move imported document from {0} to {1} (a file was found under this path)", new Object[]{file.getAbsolutePath(), archiveFile.getAbsolutePath()});
//                    MainApp.showErrorMessageDialog("Die Datei '" + archiveFile.getName() + "' kann nicht ins Archivverzeichnis verschoben werden, weil es bereits eine gleichnamige Datei gibt: " + archiveFile.getParentFile().getAbsolutePath());
                    MainApp.showErrorMessageDialog(Lang.getFileArchiveErrorParentIsFile(archiveFile.getName(),archiveFile.getParentFile().getAbsolutePath()));
                } else {
                    if (!archiveFile.getParentFile().canWrite()) {
                        LOG.log(Level.SEVERE, "was not able to archive/move imported document from {0} to {1} (no permission to write)", new Object[]{file.getAbsolutePath(), archiveFile.getAbsolutePath()});
//                        MainApp.showErrorMessageDialog("Die Datei '" + archiveFile.getName() + "' kann nicht ins Archivverzeichnis verschoben werden, weil die Schreibrechte fehlen: " + archiveFile.getParentFile().getAbsolutePath());
                        MainApp.showErrorMessageDialog(Lang.getFileArchiveErrorCanNotWrite(archiveFile.getName(),archiveFile.getParentFile().getAbsolutePath()));
                    } else {
                        //move file
                        try {
 
                            if (file.renameTo(archiveFile)) {
                                LOG.log(Level.INFO, "file was successfully renamed: {0} -> {1}", new Object[]{file.getAbsolutePath(), archiveFile.getAbsolutePath()});
                                archivated = true;
                            }else{
                                                    
                                LOG.log(Level.INFO, "rename of file for move was not successfull {0} to {1}", new Object[]{file.getAbsolutePath(), archiveFile.getAbsolutePath()});
                                MainApp.showErrorMessageDialog("Die Datei '" + archiveFile.getName() + "' kann nicht ins Archivverzeichnis verschoben werden, weil ein Zugriffsproblem aufgetreten ist: " + archiveFile.getParentFile().getAbsolutePath());
                            }
                        } catch (SecurityException ex) {
                            LOG.log(Level.SEVERE, "was not able to archive/move imported document from " + file.getAbsolutePath() + " to " + archiveFile.getAbsolutePath() + " (maybe file is in access?)", ex);
//                            MainApp.showErrorMessageDialog("Die Datei '" + archiveFile.getName() + "' kann nicht ins Archivverzeichnis verschoben werden, weil ein Zugriffsproblem aufgetreten ist: " + archiveFile.getParentFile().getAbsolutePath());
                            MainApp.showErrorMessageDialog(Lang.getFileArchiveErrorCanNotAccess(archiveFile.getName(),archiveFile.getParentFile().getAbsolutePath()));
                        }
                    }
                }
            }
        }

        return archivated;
    }
    private static void showFileInWord(File tempFile, Callback<ButtonBar.ButtonData,Boolean> fileHook) {
        if(tempFile == null){
            return;
        }
        if(fileHook == null){
            return;
        }
        Utils.initJacob();
        String pid = "Word.Application";
        ActiveXComponent axc = new ActiveXComponent(pid);
        axc.setProperty("Visible", new Variant(true));
        Dispatch oDocuments = axc.getProperty("Documents").toDispatch();
        Dispatch oDocument = Dispatch.call(oDocuments, "Open", tempFile.getAbsolutePath()).toDispatch();
         //wdWindowStateMaximize	1=Maximized, wdWindowStateMinimize 2=Minimized, wdWindowStateNormal	0=Normal.
        try{
            axc.setProperty("WindowState", new Variant(2));  // damit Word in den Vordergrund geholt wird,
            axc.setProperty("WindowState", new Variant(1));  // erstmal klein machen und danach wieder gross
        }catch(ComFailException ex){
            LOG.log(Level.WARNING, "Failed to bring Word-App to foreground! Reason:\n{0}", ex.getMessage());
        }
        SimpleBooleanProperty closeProperty = new SimpleBooleanProperty(false);
        DispatchEvents dispatchEvent = new DispatchEvents(axc.getObject(), new InvocationProxy() {
            @Override
            public Variant invoke(String methodName, Variant[] targetParameters) {
                switch (methodName) {
                    case "DocumentBeforeSave":
                        //Save changes against user's will?
                        //Dispatch.call((Dispatch) wordBasic, "FileSaveAs", fileName);
                        break;
                    case "Quit":
                        // Word is Closed , we should end this method by sending this wait order
                        LOG.log(Level.INFO, "call close");
                        AlertDialog diag = AlertDialog.createWarningDialog("Eine bereits vorhandene Datei wurde potentiell bearbeitet!\nWie soll verfahren werden?"
                                + "\n\n"
                                + "Achtung!\n'Verwerfen' führt dazu, dass sämtliche Änderungen in '"+tempFile.getName()+"' verworfen werden und somit nicht im System gespeichert werden!",
                                new ButtonType("Als neu behalten", ButtonBar.ButtonData.OK_DONE),new ButtonType("Verwerfen", ButtonBar.ButtonData.CANCEL_CLOSE));
                        BasicMainApp.bringToFront();
                        diag.showAndWait().ifPresent(new Consumer<ButtonType>() {
                            @Override
                            public void accept(ButtonType t) {
                                fileHook.call(t.getButtonData());
                            }
                        });
                        closeProperty.set(true);
                        break;
                    default:
                        break;
                }
                return null;
            }
        });
        closeProperty.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if(t1){
                    dispatchEvent.safeRelease();
                    closeWord(axc);
                }
            }
        });
    }
}
