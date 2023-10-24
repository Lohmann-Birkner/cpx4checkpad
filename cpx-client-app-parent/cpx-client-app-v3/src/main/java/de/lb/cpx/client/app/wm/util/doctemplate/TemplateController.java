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
 *    2016  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.util.doctemplate;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComFailException;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.DispatchEvents;
import com.jacob.com.InvocationProxy;
import com.jacob.com.LibraryLoader;
import com.jacob.com.Variant;
import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.core.model.fx.alert.NotificationsFactory;
import de.lb.cpx.client.core.model.task.CpxTask;
import de.lb.cpx.reader.util.OS;
import de.lb.cpx.server.commonDB.model.CWmListDocumentType;
import de.lb.cpx.wm.model.TWmDocument;
import de.lb.cpx.wm.model.TWmProcess;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.apache.commons.io.FilenameUtils;
import org.controlsfx.control.Notifications;
//import de.lb.cpx.reader.util;

/**
 * Initialization/Use of Jacob Library and related ActivexComponents,
 * opening/closing generated documents.
 *
 * @author nandola
 */
public class TemplateController {

    private static final Logger LOG = Logger.getLogger(TemplateController.class.getSimpleName());
    private static TemplateController instance;

    private TemplateController() {
        initJacob();
    }
    public static TemplateController instance(){
        if(instance == null){
            instance = new TemplateController();
        }
        return instance;
    }
    public static void destroy(){
        instance = null;
    }
    private void initJacob() {
        String p = OS.getSharedLibraryLoaderPath(); //checking the operating system
        System.setProperty(LibraryLoader.JACOB_DLL_PATH, p);
        System.setProperty("com.jacob.debug", "false");
        LibraryLoader.loadJacobLibrary(); // loading the Jacob library
    }

    private void findBookmarks(final Dispatch pSelection, final Dispatch pFind, final String pToFindText, final String pNewText) {
        if (pToFindText == null || pToFindText.isEmpty()) {
            // System.out.println("************** No Text to find ******");
            return;
        }

        boolean findBool = true; // need it to know the text is written in ms Word or not
        while (findBool) {
            findBool = true;
            Dispatch.call(pSelection, "HomeKey", new Variant(6)); // it will jump to the first line of Ms Word
            Dispatch.put(pFind, "Forward", "True");
            Dispatch.put(pFind, "Format", "True");
            Dispatch.put(pFind, "MatchCase", "True");
            Dispatch.put(pFind, "MatchWholeWord", "True");

            findBool = Dispatch.call(pFind, "Execute").getBoolean(); // if it desn't find the matching text (toFindText), it returns false
            if (findBool) {
                //System.out.println("******** "+"«"+toFindText+"»"+" is found");
                Dispatch.put(pSelection, "Text", pNewText); //replacing the new text (value of the bookmark)
                //System.out.println("******** "+newText+" is written");
            } else {
                //System.out.println("******** "+ toFindText+" is not found");
            }
        }
    }

    /**
     * Extracts text from file (primarly to get text from pdf)
     *
     * @param pInFile (pdf) file
     * @return text
     * @throws IOException cannot process file
     */
    public String getText(final File pInFile) throws IOException {
//        TemplateController templateController = new TemplateController();
        return extractText(pInFile);
    }

    public String extractText(final File pInFile) throws IOException {
        final long startTime = System.currentTimeMillis();
        CheckDocVersion docVersion = new CheckDocVersion();
        if (pInFile == null) {
            throw new IllegalArgumentException("No file passed for export to txt");
        }
        if (!pInFile.exists()) {
            //MainApp.showErrorMessageDialog("Input draft file doesn't exist, so document can't be generated: " + InFile.getAbsolutePath());
            throw new IllegalArgumentException("Cannot open, file does not exist: " + pInFile.getAbsolutePath());
        }
        if (pInFile.isDirectory()) {
            throw new IllegalArgumentException(MessageFormat.format("This is not a file, but a directory: {0}", pInFile.getAbsolutePath()));
        }
        //checking the installation of Ms Word 
        if (!docVersion.wordInstallation()) {
            //MainApp.showErrorMessageDialog("Microsoft Word wird nicht installiert: ");
            throw new IllegalArgumentException("Microsoft Word is not installed!");
        }
        // to provide full abs path of created input file.
        final String fileName = pInFile.getAbsolutePath();
        final String extension = FilenameUtils.getExtension(fileName).trim().toLowerCase();
        final boolean word;
        final boolean excel;

        if ("xls".equalsIgnoreCase(extension) || "xlsx".equalsIgnoreCase(extension)) {
            excel = true;
            word = false;
        } else {
            excel = false;
            word = true;
        }

        LOG.log(Level.INFO, "Try to extract text from file: " + fileName + "...");

        final ActiveXComponent app;
        if (excel) {
            // create ActiveXComponent for Ms Excel
            app = new ActiveXComponent("Excel.Application"); // type of the application (Here, Ms excel application)
        } else {
            // create ActiveXComponent for Ms Word
            app = new ActiveXComponent("Word.Application"); // type of the application (Here, Ms word application)            
        }
//        tmpGenerationResultProp.get().setWord(word);
        app.setProperty("Visible", new Variant(false));
        final Dispatch documents;
        if (excel) {
            // Instantiate the Documents Property
            documents = app.getProperty("Workbooks").toDispatch();
        } else {
            documents = app.getProperty("Documents").toDispatch();
        }
        final Dispatch document = Dispatch.call(documents, "Open", fileName).toDispatch(); // open the inputFile to read in ms word
        String tmpFileName = System.getProperty("java.io.tmpdir") + "cpx_text_export_" + System.nanoTime() + (word ? ".txt" : ".csv");
        //word.invoke("FileSaveAs", f.getAbsolutePath());
        LOG.log(Level.INFO, "export text to the temporary file: " + tmpFileName);
        Dispatch.call(document, "SaveAs", tmpFileName, word ? new Variant(2) /* 2=txt */ : new Variant(3) /* 3=csv */);
//        Dispatch content = Dispatch.call(document, "Content").toDispatch();
//        int end = Dispatch.call(content, "End").getInt();
//        int start = 1;
//        Dispatch range = Dispatch.call(document, "Range", start, end).toDispatch();
//        Dispatch.call(range, "Select");
//        final Clipboard clipboard = Clipboard.getSystemClipboard();
//        Dispatch.call(range, "Copy");
        closeDocument(document);
        if (excel) {
            closeExcel(app);
        } else {
            closeWord(app);
        }
        byte[] encoded = Files.readAllBytes(Paths.get(tmpFileName));
        final String result = new String(encoded, "Cp1252");
        LOG.log(Level.INFO, "Text with " + result.length() + " characters extracted from file in " + (System.currentTimeMillis() - startTime) + " ms: " + fileName + "...");
        return result.trim();
        //return clipboard.getString();
    }

    /**
     * Creates a word document from a draft
     *
     * @param pInOutFile template file (input and output file)
     * @param pDocName final document name
     * @param pSelectedDocType document type
     * @param pTemplateValueMap map of values that can be used as bookmarks
     * @param pCurrentProcess current process
     * @param pProcessFacade process service facade
     * @throws IOException yeah, many things can go wrong in this process, you
     * know?
     * @return Result of template generation (properties are used to give late
     * results)
     */
    public TemplateGenerationResult createDocFromTemplate(final File pInOutFile, final String pDocName, final CWmListDocumentType pSelectedDocType, final Map<String, String> pTemplateValueMap, final TWmProcess pCurrentProcess, final ProcessServiceFacade pProcessFacade) throws IOException {
//        TemplateController templateController = new TemplateController();
        return controlTemplate(pInOutFile, pDocName, pSelectedDocType, pTemplateValueMap, pCurrentProcess, pProcessFacade);
    }

//    private TemplateGenerationResult controlTemplate(final File pInOutFile, final String pDocName, final CWmListDocumentType pSelectedDocType, final Map<Object, String> pTemplateValueMap, final TWmProcess pCurrentProcess, final ProcessServiceFacade pProcessFacade) throws IOException {
//        List<CWmListDocumentType> listofDocumentTypeObjects = ProcessServiceBean.get().getAllDocumentTypeObjects();
//        // For now we can set the first document type, but in future we may need to provide all available types to choose one.
//        return controlTemplate(pInOutFile, pDocName, listofDocumentTypeObjects.get(0), pTemplateValueMap, pCurrentProcess, pProcessFacade);
//    }
    private void setDocumentName(final TWmDocument pWmDocument, final String pDocName) {
        String docName = pDocName == null ? "" : pDocName.trim();
        final String currentExtension = FilenameUtils.getExtension(docName).trim();
        final String wordExtension = getWordExtension(docName);
        if (!currentExtension.equalsIgnoreCase(wordExtension)) {
            docName = FilenameUtils.removeExtension(docName);
            docName += "." + wordExtension;
        }
        pWmDocument.setName(docName);
    }

    /**
     * returns "doc" normally, but can also be "docx" if the user manually
     * setted document name to that file extension
     *
     * @param pDocName document name
     * @return word extension (doc, docx)
     */
    public static String getWordExtension(final String pDocName) {
        final String extension = pDocName == null ? "" : FilenameUtils.getExtension(pDocName).trim();
        if ("doc".equalsIgnoreCase(extension)) {
            //ensure that file extension "doc" is written in lower case
            return "doc";
        } else if ("docx".equalsIgnoreCase(extension)) {
            //ensure that file extension "docx" is written in lower case
            return "docx";
        }
        return "doc";
    }

    private TemplateGenerationResult controlTemplate(final File pInOutFile, final String pDocName, final CWmListDocumentType pSelectedDocType, final Map<String, String> pTemplateValueMap, final TWmProcess pCurrentProcess, final ProcessServiceFacade pProcessFacade) throws IOException {
        final long startTime = System.currentTimeMillis();
        CheckDocVersion docVersion = new CheckDocVersion();
        if (pInOutFile == null) {
            throw new IllegalArgumentException("No file passed for template generation");
        }
        if (!pInOutFile.exists()) {
            //MainApp.showErrorMessageDialog("Input draft file doesn't exist, so document can't be generated: " + InFile.getAbsolutePath());
            throw new IllegalArgumentException("Cannot generate document, draft file does not exist: " + pInOutFile.getAbsolutePath());
        }
        if (pInOutFile.isDirectory()) {
            throw new IllegalArgumentException(MessageFormat.format("This is not a file, but a directory: {0}", pInOutFile.getAbsolutePath()));
        }
        //checking the installation of Ms Word 
        if (!docVersion.wordInstallation()) {
            //MainApp.showErrorMessageDialog("Microsoft Word wird nicht installiert: ");
            throw new IllegalArgumentException("Microsoft Word is not installed!");
        }
        if (pDocName == null || pDocName.trim().isEmpty()) {
            throw new IllegalArgumentException("No document name defined!");
        }
        final TWmDocument wmDoc = new TWmDocument();
        wmDoc.setProcess(pCurrentProcess);
//        wmDoc.setName("Generated Document_" + System.currentTimeMillis() + ".doc");
        setDocumentName(wmDoc, pDocName);
        // For now we can set the first document type, but in future we may need to provide all available types to choose one.
        wmDoc.setDocumentType(pSelectedDocType != null ? pSelectedDocType.getWmDtInternalId() : null);
        wmDoc.setDocumentDate(new Date());
        final ObjectProperty<TemplateGenerationResult> tmpGenerationResultProp = new SimpleObjectProperty<>();
        final CpxTask<File> cpxTask = new CpxTask<File>() {
            @Override
            protected File call() throws Exception {
                Object[] array_key = new String[pTemplateValueMap.size()];
                array_key = pTemplateValueMap.keySet().toArray();
                Object[] array_val = new String[pTemplateValueMap.size()];
                array_val = pTemplateValueMap.values().toArray();

                // to provide full abs path of created input file.
                final String fileName = pInOutFile.getAbsolutePath();

                // create ActiveXComponent for Ms Word
                final ActiveXComponent word = new ActiveXComponent("Word.Application"); // type of the application (Here, Ms word application)
                tmpGenerationResultProp.get().setWord(word);
                word.setProperty("Visible", new Variant(false));
                // Instantiate the Documents Property
                final Dispatch documents = word.getProperty("Documents").toDispatch();
                // Open a word document, Current Active Document
                final Dispatch document = Dispatch.call(documents, "Open", fileName).toDispatch(); // open the inputFile to read in ms word
                tmpGenerationResultProp.get().setDocument(document);
                final Dispatch selection = Dispatch.get(word, "Selection").toDispatch();
                final Dispatch bookMarks = Dispatch.call(document, "Bookmarks").toDispatch(); // need it to read and write the bookmarks
                final Dispatch find = Dispatch.call(selection, "Find").toDispatch(); //  need it to find the text in ms word

                LOG.log(Level.INFO, "\n \n ----- Following Bookmarks are filled in a Document ------ \n");
                // for each key-value pair of a HashMap
                for (int i = 0; i < pTemplateValueMap.size(); i++) {
                    String arrayValString = (array_val[i].toString()).trim().replace("[", "").replace("]", ""); // here we are trimming the [] from the array
                    String arrayKeyString = array_key[i].toString();    // key

                    boolean isExist = Dispatch.call(bookMarks, "Exists", arrayKeyString).getBoolean(); // to check bookmark's key is existed in inputFile?

                    if (isExist) {
                        findBookmarks(selection, find, arrayKeyString, arrayValString);
                        Dispatch rangeItem1 = Dispatch.call(bookMarks, "Item", arrayKeyString).toDispatch();
                        Dispatch range1 = Dispatch.call(rangeItem1, "Range").toDispatch(); //Identify current Bookmark range and insert text 
                        Dispatch.put(range1, "Text", new Variant(arrayValString)); // writing the value

                        String bookMarkValue = Dispatch.get(range1, "Text").toString();
                        LOG.log(Level.INFO, "Key is {0} and Value is {1} written.", new Object[]{arrayKeyString, bookMarkValue});
                    }
                } // end of for

                // Now save and close it.
                ActiveXComponent wordBasic = word.getPropertyAsComponent("WordBasic");
                wordBasic.invoke("FileSaveAs", pInOutFile.getAbsolutePath());   // generated doc is saved at this location.

                //Close document without saving changes
                closeDocument(document);
                LOG.log(Level.INFO, "Generating word document ''{0}'' from template took {1} ms", new Object[]{fileName, System.currentTimeMillis() - startTime});

                // After saving the document, now open it
                openDocument(word, this, tmpGenerationResultProp.get());
                tmpGenerationResultProp.set(null);
                return pInOutFile;
            }
        };
        final TemplateGenerationResult tmplGenerationResult = new TemplateGenerationResult(pInOutFile, pSelectedDocType, wmDoc, pCurrentProcess, pProcessFacade, cpxTask);
        tmpGenerationResultProp.set(tmplGenerationResult);
        cpxTask.start();
        return tmplGenerationResult;
    }   // end of controlTemplate

    /**
     * close word document (word application will still run!)
     *
     * @param pDocument word document
     * @return did a ComFailException occur?
     */
    public boolean closeDocument(final Dispatch pDocument) {
        if (pDocument == null) {
            return true;
        }
        try {
            Dispatch.call(pDocument, "Close", new Variant(false));
        } catch (ComFailException ex) {
            LOG.log(Level.SEVERE, "Was not able to close document", ex);
            return false;
        }
        return true;
    }

    /**
     * quit excel background process to release memory and file handlers!
     *
     * @param pExcel excel process
     * @return did a ComFailException occur?
     */
    public boolean closeExcel(final ActiveXComponent pExcel) {
        if (pExcel == null) {
            return true;
        }
        try {
            pExcel.invoke("Quit");
            ComThread.Release();
        } catch (ComFailException ex) {
            LOG.log(Level.SEVERE, "Was not able to close document", ex);
            return false;
        }
        return true;
    }

    /**
     * quit word background process to release memory and file handlers!
     *
     * @param pWord word process
     * @return did a ComFailException occur?
     */
    public boolean closeWord(final ActiveXComponent pWord) {
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
    private void showDefaultOpenDocumentWarning(){
        runNotification(NotificationsFactory.instance().createWarningNotification()
                .title("Fehler beim Öffnen des Word-Dokuments!")
                .text("Womöglich stehen ihnen bei diesem Dokument nicht alle Word-Funktionen zur Verfügung!"));
    }
    private void runNotification(Notifications pNotification){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                pNotification.show();
            }
        });
    }
    /**
     * Opens word file and gives the opportunity to save changes to db or file
     * system
     *
     * @param pWord word application process (reuse it from template generation
     * to get faster results!)
     * @param pCpxTask runs in this asynchronous task
     * @param pTmplGenerationResult asynchronous result object that holds
     * necessary information to open a document and to to some actions after
     * closing word
     */
    private void openDocument(final ActiveXComponent pWord, final CpxTask<File> pCpxTask, final TemplateGenerationResult pTmplGenerationResult) {
        final long startTimeOpen = System.currentTimeMillis();
        final String fileName = pTmplGenerationResult.getInOutFile().getAbsolutePath();
        if (pCpxTask.isCancelled()) {
            LOG.log(Level.WARNING, "Opening word document '" + fileName + "' was cancelled by user");
            closeWord(pWord);
            return;
        }
        pWord.setProperty("Visible", true);
        Dispatch documents = pWord.getProperty("Documents").toDispatch();
        Dispatch document = Dispatch.call(documents, "Open", fileName).toDispatch(); // open the inputFile to read in ms word
        //wdWindowStateMaximize	1=Maximized, wdWindowStateMinimize 2=Minimized, wdWindowStateNormal	0=Normal.
        try{
            pWord.setProperty("WindowState", new Variant(2));  // damit Word in den Vordergrund geholt wird,
            pWord.setProperty("WindowState", new Variant(1));  // erstmal klein machen und danach wieder gross
        }catch(ComFailException ex){
            LOG.log(Level.WARNING, "Failed to bring Word-App to foreground! Reason:\n{0}", ex.getMessage());
            showDefaultOpenDocumentWarning();
        }
        try{
            Dispatch.call(document, "Activate");
            Dispatch wordBasic = Dispatch.call(pWord, "WordBasic").getDispatch();
            Dispatch.call(wordBasic, "FileSaveAs", fileName);
        }catch(ComFailException ex){
            LOG.log(Level.WARNING, "Failed to activate Word-Functions! Reason:\n{0}", ex.getMessage());
            showDefaultOpenDocumentWarning();
        }
        pTmplGenerationResult.isDocumentOpened.set(true);
        LOG.log(Level.INFO, "Opening word document '" + fileName + "' took " + (System.currentTimeMillis() - startTimeOpen) + " ms");
        final long startTimeClosed = System.currentTimeMillis();

        InvocationProxy proxy1 = new InvocationProxy() {
            //this method will control the MS word! we use a switch to get the events and take a decision 
            @Override
            public Variant invoke(String methodName, Variant[] targetParameters) {
                switch (methodName) {
                    case "DocumentBeforeSave":
                        //Save changes against user's will?
                        //Dispatch.call((Dispatch) wordBasic, "FileSaveAs", fileName);
                        break;
                    case "Quit":
                        // Word is Closed , we should end this method by sending this wait order
                        pTmplGenerationResult.isWordClosed.set(true); //you can observe this property to do some action after closing!
                        LOG.log(Level.INFO, "Closing word document '" + fileName + "' after " + (System.currentTimeMillis() - startTimeClosed) + " ms");
                        break;
                    default:
                        break;
                }
                return null;
            }
        };
        DispatchEvents dispatchEvent = new DispatchEvents(pWord.getObject(), proxy1);

        while (!pTmplGenerationResult.isWordClosed.get()) {
            if (pCpxTask.isCancelled()) {
                LOG.log(Level.WARNING, "Opening word document '" + fileName + "' was cancelled by user");
                dispatchEvent.safeRelease();
                return;
            }
            final String interruptionMsg = "Thread for handling opened word document '" + fileName + "' received interruption signal!";
            if (Thread.currentThread().isInterrupted()) {
                LOG.log(Level.WARNING, interruptionMsg);
                break;
            }
            try {
                Thread.sleep(100L);
            } catch (InterruptedException ex) {
                LOG.log(Level.SEVERE, interruptionMsg, ex);
                Thread.currentThread().interrupt();
                break;
            }
        }
        dispatchEvent.safeRelease();
    }

//    /**
//     * Store document in database
//     *
//     * @param pDoc document
//     * @param pFile file to store
//     * @param pFacade process service facade
//     * @throws IOException error
//     */
//    public void addDocumentInDB(final TWmDocument pDoc, final File pFile, final ProcessServiceFacade pFacade) throws IOException {
//        Path path = pFile.toPath();
//        byte[] data = Files.readAllBytes(path);
//
//        pDoc.setContent(data);
//
//        // to set and add an entry in document section and history section
//        pFacade.storeDocument(pDoc);
//
//        LOG.log(Level.INFO, "Document is successfully inserted into the DB");
//
//        deleteFile(pFile);
//    }
//    /**
//     * Store document in file system
//     *
//     * @param pDoc document
//     * @param pFile file to store
//     * @param pServerRootFolder root path on server
//     * @param pFacade process service facade
//     * @param pServiceBean process service bean
//     * @throws IOException error
//     */
//    public void addDocumentInFS(final TWmDocument pDoc, final File pFile, final String pServerRootFolder, final ProcessServiceFacade pFacade, final ProcessServiceBeanRemote pServiceBean) throws IOException {
//        Path path = pFile.toPath();
//        byte[] data = Files.readAllBytes(path);
//        pDoc.setContent(data);
//        String fileExtension = FilenameUtils.getExtension(pFile.getName());
//        String pathToStore = pServiceBean.sendDocument(pDoc, pFacade.getCurrentProcess(), pServerRootFolder, fileExtension);
//        pDoc.setFilePath(pathToStore);
//        pDoc.setContent(null);  //when doc is stored in Server's FS, it's content is null in DB.
//        pFacade.storeDocument(pDoc);
//        LOG.log(Level.INFO, "Document is successfully stored into the file system");
//        deleteFile(pFile);
//    }
//    /**
//     * Deletes file
//     *
//     * @param pFile file to delete
//     * @throws IOException error
//     */
//    public void deleteFile(final File pFile) throws IOException {
//        if (pFile == null || !pFile.exists()) {
//            return;
//        }
//        LOG.log(Level.INFO, "delete (temporary) file '" + pFile.getAbsolutePath() + "'...");
//        if (pFile.isDirectory()) {
//            throw new IllegalArgumentException("This is a directory but not a file: " + pFile.getAbsolutePath());
//        }
//        if (pFile.delete()) {
//            LOG.log(Level.FINEST, "file was successfully deleted: " + pFile.getAbsolutePath());
//        }
//    }
}
