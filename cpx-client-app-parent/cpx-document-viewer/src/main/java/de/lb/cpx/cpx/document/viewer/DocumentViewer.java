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
 *    2018  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.cpx.document.viewer;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author niemeier
 */
public class DocumentViewer {

    private static final Logger LOG = Logger.getLogger(DocumentViewer.class.getName());

    protected final WebView webView;
    protected final WebEngine webEngine;
    protected ObjectProperty<File> fileProperty = new SimpleObjectProperty<>();
    //protected static final String URL = DocumentViewer.class.getResource("/pdfjs/web/viewer.html").toExternalForm();
    //protected static final String URL = new File("/pdfjs/web/viewer.html").getAbsolutePath();
//    public static final java.net.URL ZIP_FILE = DocumentViewer.class.getResource("/pdfjs.zip");
//    public static final File UNZIP_DIR = new File(System.getProperty("java.io.tmpdir") + "/pdfjs");
//    public static final File URL = new File(UNZIP_DIR.getAbsolutePath() + "/web/viewer.html");
    private final String viewerUrl;

//    public static boolean isUnzipped() {
//        return UNZIP_DIR.exists() && UNZIP_DIR.listFiles().length > 0;
//    }
//
//    protected static synchronized void unzipPdfJs() {
//        if (isUnzipped()) {
//            return;
//        }
//        final UnzipUtility unzipUtil = new UnzipUtility();
//        try {
//            unzipUtil.unzip(ZIP_FILE.openStream(), UNZIP_DIR.getAbsolutePath());
//        } catch (IOException ex) {
//            LOG.log(Level.SEVERE, "Was not able to unzip PDF.js from " + ZIP_FILE, ex);
//        }
//    }
    public DocumentViewer(final File pPdfJsViewerFile) throws MalformedURLException {
        this(null, pPdfJsViewerFile);
    }

    public String getSelectedText() {
        final String value = (String) webView.getEngine().executeScript("window.getSelection().toString()");
        return value == null ? "" : value.trim();
    }

    public String getViewerUrl() {
        return viewerUrl; // + "/pdfjs/web/viewer.html";
    }

    /**
     * When you update Mozilla PDF.js then follow these steps: 1.Step: Set
     * DEFAULT_URL = '' (old version of PDF.js) or defaultUrl: { value = '' }
     * (new version of PDF.js) in viewer.js.2. Step: Add these JavaScript
     * functions to viewer.html (look at
     * https://stackoverflow.com/questions/18207116/displaying-pdf-in-javafx or
     * https://blog.samirhadzic.com/2017/02/09/show-pdf-in-your-application/ for
     * details!): &lt;script src="viewer.js"&gt;&lt;/script&gt; &lt;!-- CUSTOM
     * BLOCK --&gt; &lt;script&gt; var openFileFromBase64 = function(data) { var
     * arr = base64ToArrayBuffer(data); console.log(arr);
     * PDFViewerApplication.open(arr); }
     *
     * function base64ToArrayBuffer(base64) { var binary_string =
     * window.atob(base64); var len = binary_string.length; var bytes = new
     * Uint8Array( len ); for (var i = 0; i &lt; len; i++) { bytes[i] =
     * binary_string.charCodeAt(i); } return bytes.buffer; } &lt;/script&gt;
     *
     * @param pWebView web view
     * @param pPdfJsViewerFile PDF.js viewer url
     * @throws java.net.MalformedURLException viewer url is corrupt/invalid
     */
    public DocumentViewer(final WebView pWebView, final File pPdfJsViewerFile) throws MalformedURLException {
//        unzipPdfJs();
        viewerUrl = pPdfJsViewerFile.toURI().toURL().toExternalForm();
        webView = pWebView == null ? new WebView() : pWebView;
        webView.setStyle("-fx-pref-width: 20px; -fx-padding: 3px;");
        webEngine = webView.getEngine();
        //Change the path according to yours.
        //We add our stylesheet.
        //engine.setUserStyleSheetLocation(getClass().getResource("pdfjs/web.css").toExternalForm());
        webEngine.setJavaScriptEnabled(true);

        //final File file = new File("C:\\workspace\\CheckpointX_Repository\\trunk\\WD_CPX_Client\\pdfjs\\web\\compressed.tracemonkey-pldi-09.pdf");
//        // listener to determine if the page has been loaded
//        engine.setOnStatusChanged((WebEvent<String> status) -> {
//            String s = status.getData();
//            if (s != null && s.equals("done")) {
//
//                //This line should call the open pdf file function in Javascript file 
//                //engine.executeScript("PDFViewerApplication.open('file:///" + file.getAbsolutePath() + "');");
//                //webView.getEngine().executeScript("openFileFromBase64('" + base64 + "')");
//                System.out.println("GAY!");
//            }
//        });
        final ChangeListener<Worker.State> listener = new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                // to debug JS code by showing console.log() calls in IDE console
                //JSObject window = (JSObject) engine.executeScript("window");
                //window.setMember("java", new JSLogListener());
                //engine.executeScript("console.log = function(message){ java.log(message); };");

                // this pdf file will be opened on application startup
                if (newValue == Worker.State.SUCCEEDED) {
                    final File file = fileProperty.get();
                    String base64 = "";
                    if (file == null) {
                        LOG.log(Level.WARNING, "Passed file is null!");
                    } else {
                        try {
                            // readFileToByteArray() comes from commons-io library
                            byte[] data = FileUtils.readFileToByteArray(file);
                            base64 = Base64.getEncoder().encodeToString(data);
                            // call JS function from Java code
                            //webEngine.executeScript("PDFViewerApplication.open('file:///" + file.getAbsolutePath() + "')");
                        } catch (IOException ex) {
                            LOG.log(Level.SEVERE, "Was not able to render file in WebView: " + file.getAbsolutePath(), ex);
                        }
                    }
                    //Disable (hide) some useless buttons. Be careful with enabling print button -> JRE can crash!
                    webEngine.executeScript(
                            "if (document.getElementById('print') != null) { document.getElementById('print').style.display = 'none' };"
                            + "if (document.getElementById('download') != null) { document.getElementById('download').style.display = 'none' };"
                            + "if (document.getElementById('openFile') != null) { document.getElementById('openFile').style.display = 'none' };"
                            + "if (document.getElementById('presentationMode') != null) { document.getElementById('presentationMode').style.display = 'none' };"
                    );
                    webEngine.executeScript("try {"
                            + "  if (PDFViewerApplication != null) { "
                            + "    PDFViewerApplication.preferences.set('showPreviousViewOnLoad', false);" //don't restore page and zoom factor from last session
                            + "    PDFViewerApplication.preferences.set('disablePageMode', true);" //don't show side bar at the beginning
                            + "    PDFViewerApplication.preferences.set('defaultZoomValue', 'auto'); " //use default zoom
                            + "  } "
                            + "} catch(e) { "
                            + "  console.log(e); "
                            + "}"); //use default zoom
                    webEngine.executeScript("try {"
                            + "  openFileFromBase64('" + base64 + "');"
                            + "} catch(e) { "
                            + "  console.log(e); "
                            + "}"); //show document content
                    webEngine.executeScript("try {"
                            + "  if (PDFViewerApplication != null) { "
                            + "    PDFViewerApplication.preferences.set('enableHandToolOnLoad', true);" //use panning instead of textmarker
                            + "  } "
                            + "} catch(e) { "
                            + "  console.log(e); "
                            + "}");
                    //webEngine.executeScript("PDFViewerApplication.preferences.set('enhanceTextSelection', false)"); //
                    //webEngine.executeScript("PDFViewerApplication.page = 1"); //go to first page
                    //webEngine.executeScript("PDFViewerApplication.pdfViewer.currentPageNumber = 1"); //go to first page
                }
            }
        };

        webEngine.getLoadWorker()
                .stateProperty()
                .addListener(listener);
        //apRoot.getChildren().add(webView);
    }

    public void loadDocument(final String pFileName) {
        if (pFileName == null || pFileName.trim().isEmpty()) {
            return;
        }
        loadDocument(new File(pFileName));
    }

    public void loadDocument(final File pFile) {
        if (pFile == null) {
            return;
        }
        checkFile(pFile);
        LOG.log(Level.INFO, "Load " + pFile.getAbsolutePath() + " in WebView");
        fileProperty.set(pFile);
        //webEngine.load(URL.toURI().toString());
        //webEngine.load(URL.toURI().toString() + "#page1"); //go to first page
        final String url = getViewerUrl();
        webEngine.load(url);
        webEngine.load(url + "#page1"); //go to first page
    }

    public WebView getWebView() {
        return webView;
    }

    public WebEngine getWebEngine() {
        return webEngine;
    }

    public File getFile() {
        return fileProperty.get();
    }

    public void showFile(File pFile) {
        if (pFile == null) {
            return;
        }
        checkFile(pFile);
        LOG.log(Level.INFO, "Show " + pFile.getAbsolutePath() + " in WebView");
        fileProperty.set(pFile);
        try {
            webEngine.load(pFile.toURI().toURL().toString());
        } catch (MalformedURLException ex) {
            LOG.log(Level.SEVERE, "illegal file path?: " + pFile.getAbsolutePath(), ex);
        }
    }

    private static void checkFile(File pFile) throws IllegalArgumentException {
        if (!pFile.exists()) {
            throw new IllegalArgumentException("File does not exist: " + pFile.getAbsolutePath());
        }
        if (!pFile.isFile()) {
            throw new IllegalArgumentException("Path is a directory but not a file: " + pFile.getAbsolutePath());
        }
        if (!pFile.canRead()) {
            throw new IllegalArgumentException("No permission to read file: " + pFile.getAbsolutePath());
        }
    }

}
