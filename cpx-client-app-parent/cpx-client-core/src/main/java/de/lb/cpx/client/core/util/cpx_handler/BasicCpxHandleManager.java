/* 
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.util.cpx_handler;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.Kernel32;
import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.service.information.CpxDatabase;
import de.lb.cpx.service.properties.SearchListProperties;
import de.lb.cpx.service.searchlist.SearchListResult;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author niemeier
 */
public abstract class BasicCpxHandleManager {

    private static final Logger LOG = Logger.getLogger(BasicCpxHandleManager.class.getName());
    public static final int START_PORT = 11111;
    private final IntegerProperty port = new SimpleIntegerProperty(0);
    public static final String USER_NAME;
    public static final String SESSION_ID;

    static {
//        String userName;
//        try {
//            userName = InetAddress.getLocalHost().getHostName().trim();
//        } catch (UnknownHostException ex) {
//            LOG.log(Level.SEVERE, "Cannot get host name", ex);
//            MainApp.showErrorMessageDialog("Rechnername konnte nicht ausgelesen werden. CPX Handler wird nicht funktionieren!");
//            userName = "";
//        }
//        USER_NAME = userName;
        USER_NAME = System.getProperty("user.name").trim();
        //maybe catch exception, because this function is not available on all systems
        SESSION_ID = getWTSGetActiveConsoleSessionId();
    }

    protected static BasicCpxHandleManager instance;
    protected Thread thread;

    public Thread getThread() {
        return thread;
    }

    protected BasicCpxHandleManager() {
    }

    public void stop() {
        if (thread != null) {
            thread.interrupt();
        }
    }

    public int getPort() {
        return port.get();
    }

    public interface MyKernel extends com.sun.jna.platform.win32.Kernel32 {

        Kernel32 INSTANCE = (Kernel32) Native.loadLibrary("kernel32", Kernel32.class, com.sun.jna.win32.W32APIOptions.DEFAULT_OPTIONS);

        //boolean GetProductInfo(int dwOSMajorVersion, int dwOSMinorVersion, int dwSpMajorVersion, int dwSpMinorVersion, IntByReference pdwReturnedProductType);
        int WTSGetActiveConsoleSessionId();
    }

    public static String getWTSGetActiveConsoleSessionId() {
        MyKernel lib = (MyKernel) Native.loadLibrary("kernel32", MyKernel.class);
        return StringUtils.trimToEmpty(String.valueOf(lib.WTSGetActiveConsoleSessionId()));
//        pSessionId = new IntByReference();
//
//        if (lib.ProcessIdToSessionId(lib.GetCurrentProcessId(), pSessionId)) {
//            consoleSessionId = lib.WTSGetActiveConsoleSessionId();
//            return consoleSessionId;
//        }
//        return 0;

//        if (lib.ProcessIdToSessionId(lib.GetCurrentProcessId(), pSessionId)) {
//          consoleSessionId = lib.WTSGetActiveConsoleSessionId();
//          return (consoleSessionId != 0xFFFFFFFF && consoleSessionId == pSessionId.getValue());
//        } else return false;
    }

    public synchronized void start() {
        if (thread != null) {
            LOG.log(Level.FINER, "Cpx Handle Thread is already running!");
            return;
        }
        LOG.log(Level.INFO, "Starting CPX Handle Thread...");
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                int port = START_PORT;
                while (true) {
                    try (final ServerSocket serverSocket = new ServerSocket(port)) {
                        BasicCpxHandleManager.this.port.set(port);
                        LOG.log(Level.INFO, "Listening on port " + port + " for CPX Handler messages");
                        while (true) {
                            if (Thread.interrupted()) {
                                LOG.log(Level.INFO, "CPX Handle Thread was interrupted!");
                                closeSocket(serverSocket);
                                break;
                            }
                            try {
                                final Socket socket = serverSocket.accept();
                                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                                final int size = 8096;
                                char[] buffer = new char[size];
                                int charCount;
                                try {
                                    charCount = bufferedReader.read(buffer, 0, size - 1);
                                } catch (IndexOutOfBoundsException exc) {
                                    LOG.log(Level.SEVERE, "Cannot read from socket", exc);
                                    continue;
                                }
                                String msg = charCount < 0 ? "" : new String(buffer, 0, charCount).trim();
                                if (msg.isEmpty()) {
                                    LOG.log(Level.FINEST, "Received empty message on port " + port + ", will skip process");
                                    continue;
                                }
                                LOG.log(Level.INFO, "Received incoming socket message on port " + port + ": " + msg);
                                final CpxHandleMessage message = new CpxHandleMessage(msg);
                                LOG.log(Level.INFO, "Message was recognized as " + String.valueOf(msg));

//                        if (message.equalsIgnoreCase("db")) {
//                            final String database = Session.instance().getCpxDatabase();
//                            OutputStream os = socket.getOutputStream();
//                            OutputStreamWriter osw = new OutputStreamWriter(os);
//                            BufferedWriter bw = new BufferedWriter(osw);
//                            bw.write(database);
//                            System.out.println("Requested database was sent to client: " + database);
//                            bw.flush();
//                            continue;
//                        }
                                //if everything is okay and this database can handle that stuff then send "OK", otherweise send "NOK" (NOK = NOT OK)
                                final CpxDatabase db = CpxDatabase.getCpxDatabaseFromString(Session.instance().getCpxDatabase());
                                final boolean ok = (USER_NAME.equalsIgnoreCase(message.getUserName())
                                        && SESSION_ID.equalsIgnoreCase(message.getSessionId()))
                                        && (message.getCpxDatabase() == null
                                        || message.getConnectionString().equalsIgnoreCase(db.getConnectionString())
                                        || (message.getPersistenceUnit().isEmpty() && message.getDatabase().equalsIgnoreCase(db.getName())));
                                final String clientResult = ok ? "OK" : "NOK";
                                //final String database = Session.instance().getCpxDatabase();
                                try (final OutputStream os = socket.getOutputStream(); final OutputStreamWriter osw = new OutputStreamWriter(os); final BufferedWriter bw = new BufferedWriter(osw)) {
                                    bw.write(clientResult);
                                    LOG.log(Level.FINE, "Response to client was: " + clientResult);
                                    bw.flush();
                                } catch (SocketException ex) {
                                    if (!ok) {
                                        LOG.log(Level.SEVERE, "Was not able to send response '" + clientResult + "' to client (maybe timeout reason) -> Result was not okay and CPX Handler EXE does only wait a very short period for your answer. This can cause side effects!", ex);
                                    } else {
                                        LOG.log(Level.WARNING, "Was not able to send response '" + clientResult + "' to client (maybe timeout reason) -> Request is handled by this CPX Client, so this should be no problem!", ex);
                                    }
                                }

                                if (!ok) {
                                    continue;
                                }

                                performHandler(message);
                            } catch (IOException ex) {
                                LOG.log(Level.SEVERE, "An error occured while reading from socket", ex);
                            } catch (UnsupportedOperationException ex) {
                                LOG.log(Level.SEVERE, "This operation is not support in CPX Core", ex);
                                Platform.runLater(() -> {
                                    BasicMainApp.showErrorMessageDialog(ex);
                                });
                            } catch (Exception ex) {
                                LOG.log(Level.SEVERE, "Unknown error occured in cpx handle manager", ex);
                                Platform.runLater(() -> {
                                    BasicMainApp.showErrorMessageDialog(ex);
                                });
                            }
                        }
                        break;
                    } catch (IOException ex) {
                        LOG.log(Level.FINEST, "port " + port + " is already in use, will try next port", ex);
                        port++;
                    }
                }
            }
        });
        th.setName("CPX Handle Listener");
        thread = th;
        thread.start();
        LOG.log(Level.INFO, "CPX Handle Thread was started!");
    }

    protected void closeSocket(final ServerSocket pSocket) {
        if (pSocket == null) {
            return;
        }
        try {
            pSocket.close();
        } catch (IOException ex) {
            LOG.log(Level.FINEST, "Cannot close server socket", ex);
        }
    }

    public void performHandler(final CpxHandleMessage pMessage) {
        if (!isStarted()) {
            //Don't perform anything if method was executed by drag & drop action
            return;
        }
        if (pMessage.isUnknown()) {
            showHandleError("Die Anfrage kann nicht verarbeitet werden, es handelt sich weder um das cpx-Protokoll, noch um eine cpx-Datei:\n" + pMessage);
            return;
        }
        if (pMessage.isProtocol()) {
            performProtocolHandler(pMessage);
            return;
        }
        if (pMessage.isFile()) {
            performFileHandler(pMessage);
            return;
        }
    }

    protected abstract void performProtocolCaseHandler(final CpxHandleMessage pMessage);

    protected abstract void performProtocolWorkflowHandler(final CpxHandleMessage pMessage);

    protected abstract boolean performProtocolUnknownHandler(final CpxHandleMessage pMessage);//protected void 

    protected void performProtocolHandler(final CpxHandleMessage pMessage) {
        if (pMessage.isCaseDataProtocol()) {
            performProtocolCaseHandler(pMessage);
            return;
        }

        if (pMessage.isWorkflowDataProtocol()) {
            performProtocolWorkflowHandler(pMessage);
            return;
        }

        //Okay, everything failed, now I try my very best to guess what this information could be... (case, process or patient?)
        if (performProtocolUnknownHandler(pMessage)) {
            return;
        }

        showHandleError("Die Anfrage konnte nicht als Protokoll verarbeitet werden: " + pMessage);
    }

    protected void performFileHandler(final CpxHandleMessage pMessage) {
        if (pMessage.isCpxfFile()) {
            performCpxfFileHandler(pMessage);
            return;
        }
        showHandleError("Die Anfrage konnte nicht als Datei verarbeitet werden: " + pMessage);
    }

    protected void performCpxfFileHandler(final CpxHandleMessage pMessage) {
        SearchListProperties searchList = null;
        try {
            //showHandleError("Die Anfrage kann nicht verarbeitet werden, weil der Import von cpxf-Dateien noch nicht unterstützt wird:\n" + pMessage);
            searchList = SearchListProperties.deserialize(pMessage.getFileContent());
        } catch (IllegalStateException ex) {
            LOG.log(Level.SEVERE, "Cannot read from cpxf file", ex);
            showHandleError("Der Dateiinhalt konnte nicht ausgelesen werden: " + pMessage.message, ex);
            return;
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Cannot read from cpxf file", ex);
            showHandleError("Die Datei konnte nicht deserialisiert werden. Der Dateiinhalt ist offenbar ungültig: " + pMessage.message, ex);
            return;
//        } catch (JAXBException ex) {
//            LOG.log(Level.SEVERE, "Cannot deserialize cpxf file", ex);
//            showHandleError("Die Datei konnte nicht deserialisiert werden. Der Dateiinhalt ist offenbar ungültig: " + pMessage.message, ex);
//            return;
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, "Cannot deserialize cpxf file", ex);
            showHandleError("Die Datei konnte nicht deserialisiert werden. Es wurde versucht ein Wert in eine Nummer umzuwandeln (vermutlich handelt es sich um eine ungültige oder fehlende Search List ID): " + pMessage.message, ex);
            return;
        }
        final SearchListProperties sl = searchList;
        //final SearchListResult existingSlById = CpxClientConfig.instance().getSearchList(sl.getList(), sl.getId());
        //final SearchListResult existingSlByName = existingSlById != null ? null : CpxClientConfig.instance().getSearchList(sl.getList(), sl.getName());
        final SearchListResult existingSlById = MenuCache.getMenuCacheSearchLists().get(sl.getId(), sl.getList());
        final SearchListResult existingSlByName = existingSlById != null ? null : MenuCache.getMenuCacheSearchLists().get(sl.getName(), sl.getList());
        final SearchListResult existingSl = existingSlById != null ? existingSlById : existingSlByName;
        importFilter(existingSl, sl);
    }

    protected abstract void importFilter(final SearchListResult pExistingSearchList, final SearchListProperties pSearchList);

    public static synchronized void setInstance(final BasicCpxHandleManager pInstance) {
        if (pInstance == null) {
            throw new IllegalArgumentException("instance cannot be null!");
        }
        instance = pInstance;
    }

    public static BasicCpxHandleManager instance() {
        if (instance == null) {
            throw new IllegalStateException("instance is null!");
        }
        return instance;
    }

    protected static void showHandleMessage(final String pMessage) {
        //BasicMainApp.bringToFront();
        Platform.runLater(() -> {
            AlertDialog dlg = AlertDialog.createInformationDialog(pMessage);
            bringDialogToFront(dlg);
        });
    }

    protected static void showHandleConfirmation(final String pMessage, final Callback<?, ?> pCallback) {
        //BasicMainApp.bringToFront();
        Platform.runLater(() -> {
            AlertDialog dlg = AlertDialog.createConfirmationDialog(pMessage);
            BasicMainApp.centerWindow(dlg.getDialogPane().getScene().getWindow());
            //dlg.initOwner(BasicMainApp.getStage());
            //BasicMainApp.getStage().isShowing()??
            Optional<ButtonType> result = bringDialogToFront(dlg, true);
            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                BasicMainApp.bringToFront();
                pCallback.call(null);
            } else {
                BasicMainApp.getStage().setAlwaysOnTop(false);
            }
            //dlg.getDialogPane().requestFocus();
        });
    }

    protected static void showHandleError(final String pMessage) {
        showHandleError(pMessage, null);
    }

    protected static void showHandleError(final String pMessage, final Exception ex) {
        //BasicMainApp.bringToFront();
        Platform.runLater(() -> {
            AlertDialog dlg = AlertDialog.createErrorDialog(pMessage, ex);
            bringDialogToFront(dlg);
        });
    }

    protected static void bringDialogToFront(AlertDialog pDialog) {
        bringDialogToFront(pDialog, false);
    }

    protected static Optional<ButtonType> bringDialogToFront(AlertDialog pDialog, final boolean pShowAndWait) {
        if (pDialog == null) {
            return Optional.empty();
        }
        pDialog.initOwner(BasicMainApp.getStage());
        BasicMainApp.centerWindow(pDialog.getDialogPane().getScene().getWindow());
        //pDialog.show();

        Stage stage = (Stage) pDialog.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        if (pShowAndWait) {
            return pDialog.showAndWait();
        } else {
            pDialog.show();
            pDialog.getDialogPane().requestFocus();
        }
        return Optional.empty();
    }

    public boolean isStarted() {
        return thread != null;
    }

}
