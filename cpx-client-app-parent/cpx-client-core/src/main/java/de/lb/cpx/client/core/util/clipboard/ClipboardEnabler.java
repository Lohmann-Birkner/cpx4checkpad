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
package de.lb.cpx.client.core.util.clipboard;

import com.sun.javafx.scene.control.ContextMenuContent.MenuItemContainer;
import com.sun.javafx.scene.control.LabeledText;
import de.lb.cpx.client.core.model.fx.alert.NotificationsFactory;
import de.lb.cpx.client.core.util.cpx_handler.BasicCpxHandleManager;
import de.lb.cpx.client.core.util.cpx_handler.CpxHandleLink;
import de.lb.cpx.client.core.util.cpx_handler.CpxHandleMessage;
import de.lb.cpx.client.core.util.shortcut.Shortcut;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.skin.NestedTableColumnHeader;
import javafx.scene.control.skin.TableColumnHeader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.input.TransferMode;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.Notifications;

/**
 *
 * @author niemeier
 */
public class ClipboardEnabler {

    private static final Logger LOG = Logger.getLogger(ClipboardEnabler.class.getName());
    private static final Shortcut SHORTCUT = new Shortcut();

    /**
     * install clipboard functionality via CTRL pressed and left mouse click
     *
     * @param pScene scene to observe
     */
    public static void installClipboardToScene(final Scene pScene) {
        if (pScene == null) {
            return;
        }

        BooleanProperty fired = new SimpleBooleanProperty(false);
        pScene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //LOG.log(Level.INFO, System.currentTimeMillis() + ": MOUSE_PRESSED");
                boolean b = copyClickedNodeToClipboard(event);
                fired.set(b);
            }
        });
        pScene.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //LOG.log(Level.INFO, System.currentTimeMillis() + ": MOUSE_RELEASED");
                if (fired.get()) {
                    event.consume();
                } else {
                    boolean b = copyClickedNodeToClipboard(event);
                    fired.set(b);
                }
            }
        });
        pScene.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //LOG.log(Level.INFO, System.currentTimeMillis() + ": MOUSE_CLICKED");
                if (fired.get()) {
                    event.consume();
                    fired.set(false);
                } else {
                    boolean b = copyClickedNodeToClipboard(event);
                    fired.set(b);
                }
            }
        });

        installDragAndDrop(pScene);
        installShortcut(pScene);
    }

    private static void installShortcut(final Scene pScene) {
        SHORTCUT.setShortcut(pScene);
    }

    private static void installDragAndDrop(final Scene pScene) {
        pScene.setOnDragOver(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {
                if (!BasicCpxHandleManager.instance().isStarted()) {
                    return;
                }
                final String str = StringUtils.trimToEmpty(event.getDragboard().getString());
                final List<File> files = event.getDragboard().getFiles();
                if (!str.isEmpty() || filterFiles(files).length > 0 || urlFiles(files).length > 0) {
                    /* allow for both copying and moving, whatever user chooses */
                    event.acceptTransferModes(TransferMode.COPY);
                }
                event.consume();
            }
        });

        pScene.setOnDragDropped(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {
                if (!BasicCpxHandleManager.instance().isStarted()) {
                    return;
                }
                if (event.getGestureSource() != null) {
                    //don't process self-created stuff
                    return;
                }
                Dragboard db = event.getDragboard();
                boolean success = false;
//                if (db.hasString()) {
//                    dropped.setText(db.getString());
//                    success = true;
//                }
                success = true;
                /* let the source know whether the string was successfully 
                 * transferred and used */
                event.setDropCompleted(success);

                event.consume();

                File[] filterFiles = new File[0];
                File[] urlFiles = new File[0];
                String url = "";
                String str = "";
                String html = "";

                if (db.getFiles() != null && !db.getFiles().isEmpty()) {
                    filterFiles = filterFiles(db.getFiles());
                    urlFiles = urlFiles(db.getFiles());
                } else if (StringUtils.trimToNull(db.getUrl()) != null) {
                    url = StringUtils.trimToEmpty(db.getUrl());
                } else if (StringUtils.trimToNull(db.getString()) != null) {
                    str = StringUtils.trimToEmpty(db.getString());
                } else if (StringUtils.trimToNull(db.getHtml()) != null) {
                    String htmlTmp = StringUtils.trimToEmpty(db.getHtml());
                    //If you need a better implementation to strip html tags then use jsoup
                    html = htmlTmp.replaceAll("\\<.*?\\>", "");
                }

                if (!url.isEmpty() || !str.isEmpty() || !html.isEmpty()) {
                    String message = !url.isEmpty() ? url : !str.isEmpty() ? str : html;
                    if (message.toLowerCase().startsWith("url:")) {
                        //
                    } else if (!message.toLowerCase().startsWith(CpxHandleLink.PROTOCOL_PREFIX)) {
                        message = CpxHandleLink.PROTOCOL_PREFIX + message;
                    }
                    CpxHandleMessage cpxHandleMessage = new CpxHandleMessage(message);
                    BasicCpxHandleManager.instance().performHandler(cpxHandleMessage);
                    return;
                }
                if (filterFiles.length > 0) {
                    for (final File filterFile : filterFiles) {
                        final CpxHandleMessage cpxHandleMessage = new CpxHandleMessage(filterFile.getAbsolutePath());
                        BasicCpxHandleManager.instance().performHandler(cpxHandleMessage);
                    }
                    return;
                }
                if (urlFiles.length > 0) {
                    for (final File urlFile : urlFiles) {
                        final String urlTmp = getUrlFromUrlFile(urlFile);
                        final CpxHandleMessage cpxHandleMessage = new CpxHandleMessage(urlTmp);
                        BasicCpxHandleManager.instance().performHandler(cpxHandleMessage);
                    }
                    return;
                }

//                StringBuilder sb = new StringBuilder();
//                for (File file : files) {
//                    sb.append("\r\n* " + file.getName());
//                };
//                AlertDialog dlg = AlertDialog.createConfirmationDialog("Möchten Sie die nachfolgenden Filterdateien importieren?\r\n" + sb.toString());
//                dlg.initOwner(BasicMainApp.getWindow());
//                dlg.show();
            }
        });
    }

    private static File[] filterFiles(List<File> files) {
        List<File> filterFiles = new ArrayList<>();
        for (File file : files) {
            //cpx://fall_260101865_10339302
            if (file.getName().toLowerCase().endsWith(".cpxf")) {
                filterFiles.add(file);
            }
        }
        File[] tmp = new File[filterFiles.size()];
        filterFiles.toArray(tmp);
        return tmp;
    }

    private static File[] urlFiles(List<File> files) {
        List<File> urlFiles = new ArrayList<>();
        for (File file : files) {
            final String url = getUrlFromUrlFile(file);
            if (!url.isEmpty()) {
                urlFiles.add(file);
            }
        }
        File[] tmp = new File[urlFiles.size()];
        urlFiles.toArray(tmp);
        return tmp;
    }

    private static String getUrlFromUrlFile(final File pFile) {
        if (pFile == null) {
            return "";
        }
        if (!pFile.getName().toLowerCase().endsWith(".url")) {
            return "";
        }
//        int port = CpxHandleManager.instance().getPort();
//        if (pFile.getAbsolutePath().startsWith(System.getProperty("java.io.tmpdir")) {
//            //don't process self-created url links
//            return "";
//        }
        int l = 0;
        try ( BufferedReader reader = new BufferedReader(
                new FileReader(pFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                l++;
                if (l > 50) {
                    //don't read the whole file (it can be very large if someone has changed the extension of a big file!)
                    break;
                }
                // read next line
                line = reader.readLine().trim();
                if (line.startsWith("URL=")) {
                    line = line.substring(4);
                    return line.replace("\\\"", "\"");
                }
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Was not able to read from url file: " + pFile.getAbsolutePath(), ex);
        }
        return "";
    }

    private static boolean copyClickedNodeToClipboard(final MouseEvent pMouseEvent) {
        if (pMouseEvent == null) {
            return false;
        }
        if (!pMouseEvent.isControlDown()) {
            return false;
        }
        if (!pMouseEvent.isShiftDown()) {
            return false;
        }
        if (pMouseEvent.getClickCount() > 1) {
            pMouseEvent.consume();
            return false;
        }
        if (MouseButton.PRIMARY != pMouseEvent.getButton()) {
            return false;
        }
        pMouseEvent.consume(); //stop event bubbling
        PickResult result = pMouseEvent.getPickResult();
        Node n = result.getIntersectedNode();
        ClipboardResult value = getNodeClipboardValue(n);
        copyToClipboard(value);
        return true;
    }

    /**
     * copy value to clipboard and informs user with a notification
     *
     * @param pValue value that is to be shown as notification
     */
    public static void copyToClipboard(final String pValue) {
        final boolean showTip = false;
        final Node node = null;
        copyToClipboard(node, pValue, showTip);
    }

    /**
     * copy value to clipboard and informs user with a notification
     *
     * @param pNode node to highlight (can be null)
     * @param pValue value that is to be shown as notification
     */
    public static void copyToClipboard(final Node pNode, final String pValue) {
        final boolean showTip = false;
        copyToClipboard(pNode, pValue, showTip);
    }

    /**
     * copy value to clipboard and informs user with a notification
     *
     * @param pNode node to highlight (can be null)
     * @param pValue value that is to be shown as notification
     * @param pShowTip show hint to the customer to make him or her aware of
     * clipboard functionality?
     */
    public static void copyToClipboard(final Node pNode, final String pValue, final boolean pShowTip) {
        copyToClipboard(new ClipboardResult(pNode, pValue), pShowTip);
    }

    /**
     * copy html value to clipboard and informs user with a notification
     *
     * @param pNode node to highlight (can be null)
     * @param pValue value that is to be shown as notification
     * @param pShowTip show hint to the customer to make him or her aware of
     * clipboard functionality?
     */
    public static void copyHtmlToClipboard(final Node pNode, final String pValue, final boolean pShowTip) {
        final boolean isHtml = true;
        copyToClipboard(new ClipboardResult(pNode, pValue, isHtml), pShowTip);
    }

    /**
     * copy value to clipboard and informs user with a notification
     *
     * @param pNode node which values has to be copied to clipboard
     */
    public static void copyToClipboard(final Node pNode) {
        if (pNode == null) {
            return;
        }
        ClipboardResult value = getNodeClipboardValue(pNode);
        copyToClipboard(value);
    }

    /**
     * copy value to clipboard and informs user with a notification
     *
     * @param pValue value that is to be shown as notification
     */
    public static void copyToClipboard(final ClipboardResult pValue) {
        final boolean showTip = false;
        copyToClipboard(pValue, showTip);
    }

    /**
     * copy value to clipboard and informs user with a notification
     *
     * @param pValue value that is to be shown as notification
     * @param pShowTip show hint to the customer to make him or her aware of
     * clipboard functionality?
     */
    public static void copyToClipboard(final ClipboardResult pValue, final boolean pShowTip) {
        if (pValue == null) {
            return;
        }
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        String notificationValue;
        if (!pValue.getValue().isEmpty()) {
            if (pValue.isHtml()) {
                //If you need a better implementation to strip html tags then use jsoup
                String noHTMLString = pValue.getValue().replaceAll("\\<.*?\\>", "");
                content.putHtml(pValue.getValue());
                content.putString(noHTMLString);
                notificationValue = "'" + noHTMLString + "'";
            } else {
                content.putString(pValue.getValue());
                notificationValue = "'" + pValue.getValue() + "'";
            }
            //content.putHtml("<b>Gay</b> text");
        } else if (pValue.getImage() != null) {
            content.putImage(pValue.getImage());
            notificationValue = "Bild";
        } else {
            //Cannot paste something to clipboard, no value in result
            return;
        }
        clipboard.setContent(content);

        if (pValue.getNode() != null) {
            FadeTransition ft = new FadeTransition(Duration.millis(1250), pValue.getNode());
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.play();
        }

        final String notVal = notificationValue;

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                final int MAX_LENGTH = 60;
                Notifications not = NotificationsFactory.instance().createInformationNotification();
                not.title("In die Zwischenablage kopiert");
                not.text("" + (notVal.length() > MAX_LENGTH ? notVal.substring(0, MAX_LENGTH).trim() + "..." : notVal) + " wurde in die Zwischenablage gelegt"
                        + (pShowTip ? "\n\nTipp: Halten Sie das nächste mal STRG+SHIFT gedrückt\nund klicken Sie die zu kopierende Information einfach an!" : "")
                );
                not.hideAfter(Duration.seconds(pShowTip ? 6 : 2));
                //not.position(Pos.BOTTOM_RIGHT);
                not.hideCloseButton();
                not.show();
            }
        });
    }

    private static ClipboardResult getNodeClipboardValue(final Node n) {
        return getNodeClipboardValue(n, 0);
    }

    private static String getTextValue(final Text pText) {
        if (pText == null) {
            return "";
        }
        String text;
        try {
            //lt.getClass().getFields();
            Field field = pText.getClass().getDeclaredField("labeled");
            field.setAccessible(true);
            Labeled labeled = (Labeled) field.get(pText);
            text = labeled.getText();
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            LOG.log(Level.FINER, "Was not able to get full text of labeled text", ex);
            text = pText.getText(); //maybe stops with ellipse (...)
        }
        text = getText(pText.getFont(), text);
//        if (text != null) {
//            if (text.equalsIgnoreCase("\\uf129")) {
//                //do not show info symbol (i)
//                text = "";
//            }
//        }
        return text == null ? "" : text.trim();
    }

    private static String getText(final Font pFont, final String pText) {
        if (pText == null) {
            return pText;
        }
        if (pFont == null) {
            return pText;
        }
        boolean fontAwesome = pFont.getName().equalsIgnoreCase("FontAwesome");
        if (fontAwesome && pText.length() > 0) {
            //convert to unicode
            StringBuilder sb = new StringBuilder();
            for (char ch : pText.toCharArray()) {
                //is a fontawesome glyph
                sb.append("\\u" + Integer.toHexString(ch | 0x10000).substring(1));
            }
            return sb.toString();
        }
        return pText;
    }

    private static String getComboBoxValue(final ComboBox<?> pComboBox) {
        String text = pComboBox.getEditor().getText();
        if (!StringUtils.trimToEmpty(text).isEmpty()) {
            return text.trim();
        }
        if (pComboBox.getChildrenUnmodifiable().size() >= 3) {
            Parent parent;
            if (pComboBox.getChildrenUnmodifiable().get(2) instanceof ImageView) {
                parent = (Parent) pComboBox.getChildrenUnmodifiable().get(1);
            } else {
                parent = (Parent) pComboBox.getChildrenUnmodifiable().get(2);
            }
            if (!parent.getChildrenUnmodifiable().isEmpty()) {
                Node item = parent.getChildrenUnmodifiable().get(0);
                if (LabeledText.class.isInstance(item)) {
                    return getTextValue((Text) item);
                }
            }
        }
        Object selItem = pComboBox.getSelectionModel().getSelectedItem();
        text = selItem == null ? "" : selItem.toString();
        if (!StringUtils.trimToEmpty(text).isEmpty()) {
            return text.trim();
        }
        ListCell<?> listCell = pComboBox.getButtonCell();
        text = listCell == null ? "" : listCell.getText();
        if (!StringUtils.trimToEmpty(text).isEmpty()) {
            return text.trim();
        }
        return "";
    }

    private static ClipboardResult getNodeClipboardValue(final Node n, final int iteration) {
        if (n == null) {
            return null;
        }
        if (iteration > 2) {
            return null;
        }
//        if (PasswordField.class.isInstance(n)) {
//            //not allowed
//            return null;
//        }
//        if (LabeledText.class.isInstance(n)) {
//            LabeledText labeledText = (LabeledText) n;
//            final String text = getLabeledTextValue(labeledText);
//            if (!StringUtils.trimToEmpty(text).isEmpty()) {
//                return new ClipboardResult(n, text);
//            }
//        }
        if (Text.class.isInstance(n)) {
            final Text txt = (Text) n;
            final String text = getTextValue(txt);
            if (!StringUtils.trimToEmpty(text).isEmpty()) {
                return new ClipboardResult(n, text);
            }
        }
        if (ProgressIndicator.class.isInstance(n)) {
            final ProgressIndicator progressIndicator = (ProgressIndicator) n;
            final String text = String.valueOf((int) (progressIndicator.getProgress() * 100));
            if (!StringUtils.trimToEmpty(text).isEmpty()) {
                return new ClipboardResult(n, text);
            }
        }
        if (Slider.class.isInstance(n)) {
            final Slider slider = (Slider) n;
            final String text = String.valueOf(slider.getValue());
            if (!StringUtils.trimToEmpty(text).isEmpty()) {
                return new ClipboardResult(n, text);
            }
        }
        if (Label.class.isInstance(n)) {
            final Label label = (Label) n;
            final String text = getText(label.getFont(), label.getText());
            if (!StringUtils.trimToEmpty(text).isEmpty()) {
                return new ClipboardResult(n, text);
            }
        }
        if (MenuItemContainer.class.isInstance(n)) {
            final MenuItemContainer mic = (MenuItemContainer) n;
            final String text = mic.getItem().getText();
            if (!StringUtils.trimToEmpty(text).isEmpty()) {
                return new ClipboardResult(n, text);
            }
        }
        if (Button.class.isInstance(n)) {
            final Button button = (Button) n;
            final String text = getText(button.getFont(), button.getText());
            if (!StringUtils.trimToEmpty(text).isEmpty()) {
                return new ClipboardResult(n, text);
            }
        }
        if (ListCell.class.isInstance(n)) {
            final ListCell<?> listCell = (ListCell<?>) n;
            final String text = getText(listCell.getFont(), listCell.getText());
            if (!StringUtils.trimToEmpty(text).isEmpty()) {
                return new ClipboardResult(n, text);
            }
        }
        if (ComboBox.class.isInstance(n)) {
            final ComboBox<?> comboBox = (ComboBox) n;
            final String text = getComboBoxValue(comboBox);
            if (!StringUtils.trimToEmpty(text).isEmpty()) {
                return new ClipboardResult(n, text);
            }
        }
//        if (CheckComboBox.class.isInstance(n)) {
//            final CheckComboBox checkComboBox = (CheckComboBox) n;
//            final String text = checkComboBox.toString();
//            if (!StringUtils.trimToEmpty(text).isEmpty()) {
//                return new ClipboardResult(n, text);
//            }
//        }
        if (ImageView.class.isInstance(n)) {
            final ImageView imageView = (ImageView) n;
            final Image image = imageView.getImage();
            if (image != null) {
                return new ClipboardResult(n, image);
            }
        }
        if (CheckBox.class.isInstance(n)) {
            final CheckBox checkBox = (CheckBox) n;
            final String text = String.valueOf(checkBox.isSelected());
            if (!StringUtils.trimToEmpty(text).isEmpty()) {
                return new ClipboardResult(n, text);
            }
        }
        if (RadioButton.class.isInstance(n)) {
            final RadioButton radioButton = (RadioButton) n;
            final String text = String.valueOf(radioButton.isSelected());
            if (!StringUtils.trimToEmpty(text).isEmpty()) {
                return new ClipboardResult(n, text);
            }
        }
        if (TableView.class.isInstance(n)) {
            @SuppressWarnings("unchecked")
            final TableView<TableColumn<?, ?>> tableView = (TableView<TableColumn<?, ?>>) n;
            final String text = getTableValue(tableView);
            //if (!StringUtils.trimToEmpty(text).isEmpty()) {
            return new ClipboardResult(n, text);
            //}
        }
        if (TableRow.class.isInstance(n)) {
            final TableRow<?> tableRow = (TableRow<?>) n;
            final String text = getRowValue(tableRow);
            //if (!StringUtils.trimToEmpty(text).isEmpty()) {
            return new ClipboardResult(n, text);
            //}
        }
        if (TableColumnHeader.class.isInstance(n)) {
            final TableColumnHeader tableColumnHeader = (TableColumnHeader) n;
            final String text = getColumnValue(tableColumnHeader);
            //if (!StringUtils.trimToEmpty(text).isEmpty()) {
            return new ClipboardResult(n, text);
            //}
        }
//        if (WebView.class.isInstance(n)) {
//            final String text = (String) ((WebView) n).getEngine().executeScript("window.getSelection().toString()");
//            //if (!StringUtils.trimToEmpty(text).isEmpty()) {
//            return new ClipboardResult(n, text);
//        }
        ClipboardResult value = null;
        if (Parent.class.isInstance(n)) {
            final Parent parent = (Parent) n;
            if (iteration >= 0) {
                ObservableList<Node> children = parent.getChildrenUnmodifiable();
                if (children.size() == 1) {
                    //don't care about data type if there is only 1 children
                    //recursion, truly!
                    value = getNodeClipboardValue(children.get(0), iteration + 1);
                } else {
                    for (Node childNode : children) {
                        if (Text.class.isInstance(childNode)
                                || Group.class.isInstance(childNode)) {
                            value = getNodeClipboardValue(childNode, iteration + 1);
                            if (value != null && value.isSet()) {
                                break;
                            }
                        } else {
                            //process later
                        }
                    }
                    if (value == null && iteration == 0) {
                        for (Node childNode : children) {
                            if (Text.class.isInstance(childNode)
                                    || Group.class.isInstance(childNode)) {
                                //was already processed
                            } else {
                                value = getNodeClipboardValue(childNode, iteration + 1);
                                if (value != null && value.isSet()) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
//                //now check try to get data from nested labels
//                if (value == null && iteration == 0) {
//                    //recursion, truly!
//                    for (Node childNode : labelChildren) {
//                        value = getNodeClipboardValue(childNode, iteration + 1);
//                        if (value != null && value.isSet()) {
//                            break;
//                        }
//                    }
//                }
//        }
        }
        if (value == null && iteration
                <= 0) {
            final Parent parent = n.getParent();
            value = getNodeClipboardValue(parent, iteration - 1);
        }
        if (iteration
                == 0) {
            LOG.log(Level.INFO, "Cannot put value to clipboard of element of type {0}", n.getClass().getName());
        }
        return value;
    }

    private static String getColumnValue(final TableColumnHeader pColumn) {
        if (pColumn == null) {
            return "";
        }
        try {
            //lt.getClass().getFields();
            Field fieldColumnIndex = pColumn.getClass().getDeclaredField("columnIndex");
            fieldColumnIndex.setAccessible(true);
            Integer columnIndex = (Integer) fieldColumnIndex.get(pColumn);
            Field fieldColumn = pColumn.getClass().getDeclaredField("column");
            fieldColumn.setAccessible(true);
            TableColumn<?, ?> column = (TableColumn<?, ?>) fieldColumn.get(pColumn);
            return getTableValue(column.getTableView(), null, columnIndex);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            LOG.log(Level.FINER, "Was not able to get full column index", ex);
            return null;
        }
        //return getTableValue(pColumn.getTableView(), pColumn);
    }

    private static String getRowValue(final TableRow<?> pRow) {
        if (pRow == null) {
            return "";
        }
        return getTableValue(pRow.getTableView(), pRow.getIndex(), null);
    }

    private static String getTableValue(final TableView<TableColumn<?, ?>> pTableView) {
        return getTableValue(pTableView, null, null);
    }

//    private static String getTableValue(final TableView<TableColumn> pTableView, final int pRowIndex) {
//        return getTableValue(pTableView, pRowIndex, null);
//    }
//
//    private static String getTableValue(final TableView<TableColumn> pTableView, final int pColumnIndex) {
//        return getTableValue(pTableView, null, pColumnIndex);
//    }
    private static String getCellText(final Set<Node> pCells) {
        if (pCells == null || pCells.isEmpty()) {
            return "";
        }
        Iterator<Node> it = pCells.iterator();
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
            Node elem = it.next();

            if (LabeledText.class
                    .isInstance(elem)) {
                String text = getTextValue((Text) elem);
                if (text != null && text.startsWith("\\u")) {
                    if (text.equalsIgnoreCase("\\uf129")) {
                        text = ""; //don't show info icons (i) and other unicode glyphs
                    }
                }
                if (!StringUtils.trimToEmpty(text).isEmpty()) {
                    if (sb.length() > 0) {
                        sb.append(" ");
                    }
                    sb.append(text);
                }
            } else {
                //IS NOT A LABELEDTEXT!
            }
        }
        return sb.toString();
    }

    private static String getTableValue(final TableView<?> pTableView, final Integer pRowIndex, final Integer pColumnIndex) {
        if (pTableView == null) {
            return "";
        }

        final StringBuilder sb = new StringBuilder();

        NestedTableColumnHeader header = ((NestedTableColumnHeader) pTableView.lookup("NestedTableColumnHeader"));
        int headIndex = -1;
        int headValues = 0;
        for (TableColumnHeader col : header.getColumnHeaders()) {
            headIndex++;
            if (pColumnIndex != null && pColumnIndex != headIndex) {
                continue;
            }
            Set<Node> cells = col.lookupAll("LabeledText");
            if (headValues > 0) {
                sb.append("\t");
            }
            final String text = getCellText(cells);
//            if ((text == null || text.trim().isEmpty()) && headIndex == 0) {
//                continue;
//            }
            sb.append(text);
            headValues++;
            //System.out.println(col.lookupAll("LabeledText"));
        }

        Set<Node> rows = pTableView.lookupAll("TableRow");
        int rowIndex = -1;
        int rowValues = 0;
        for (Node n : rows) {
            rowIndex++;
            TableRow<?> row = (TableRow<?>) n;
            if (pRowIndex != null && pRowIndex != rowIndex) {
                continue;
            }
            //if (rowValues > 0) {
            sb.append("\r\n");
            //}
            ObservableList<Node> cols = row.getChildrenUnmodifiable();
            int colIndex = -1;
            int colValues = 0;
            for (Node col : cols) {
                colIndex++;
                if (pColumnIndex != null && pColumnIndex != colIndex) {
                    continue;
                }
                Set<Node> cells = col.lookupAll("LabeledText");
                if (colValues > 0) {
                    sb.append("\t");
                }
                final String text = getCellText(cells);
//                if ((text == null || text.trim().isEmpty()) && colIndex == 0) {
//                    continue;
//                }
                sb.append(text);
                colValues++;
            }
            rowValues++;
        }

        return sb.toString();

//        final Set<Integer> rows = new TreeSet<>();
//        for (final TablePosition tablePosition : pTableView.getSelectionModel().getSelectedCells()) {
//            rows.add(tablePosition.getRow());
//        }
//
//        final StringBuilder sb = new StringBuilder();
//        boolean firstRow = true;
//        for (final Integer row : rows) {
//            if (!firstRow) {
//                sb.append('\n');
//            }
//            firstRow = false;
//            boolean firstCol = true;
//            for (final TableColumn<?, ?> column : pTableView.getColumns()) {
//                if (!firstCol) {
//                    sb.append('\t');
//                }
//                firstCol = false;
//                final Object cellData = column.getCellData(row);
//                sb.append(cellData == null ? "" : cellData.toString());
//            }
//        }
//
//        return sb.toString();
    }

}
