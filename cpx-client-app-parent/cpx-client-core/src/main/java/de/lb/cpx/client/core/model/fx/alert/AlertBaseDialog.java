/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.alert;

import de.lb.cpx.client.core.model.fx.dialog.DialogSkin;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.client.core.util.clipboard.ClipboardEnabler;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

/**
 *
 * @author wilde
 */
public class AlertBaseDialog extends Alert {

    private static final Logger LOG = Logger.getLogger(AlertBaseDialog.class.getName());
    public static final double ALERT_ICON_SIZE = 70d;
    private DialogSkin<ButtonType> skin;
    private final Label labelContent;
    private final VBox box;

    /**
     * creates a new AlertDialog instance
     *
     * @param pAlertType type of the Dialog ,Error,Information,Warning
     * @param pHeaderText header text
     * @param pMsg text to be shown in the content area
     * @param pButtonTypes types of buttons to be set
     * @param pException error or throwable
     */
    protected AlertBaseDialog(Alert.AlertType pAlertType, String pHeaderText, String pMsg, final Throwable pException, ButtonType... pButtonTypes) {
        this(pAlertType, pHeaderText, pMsg, "", pException, pButtonTypes);
    }

    /**
     * creates a new AlertDialog instance
     *
     * @param pAlertType type of the Dialog ,Error,Information,Warning
     * @param pHeaderText header text
     * @param pMsg text to be shown in the content area
     * @param pDetails Stacktrace or other details
     * @param pButtonTypes types of buttons to be set
     */
    protected AlertBaseDialog(Alert.AlertType pAlertType, String pHeaderText, String pMsg, String pDetails, ButtonType... pButtonTypes) {
        this(pAlertType, pHeaderText, pMsg, pDetails, null, pButtonTypes);
    }
    /**
     * creates a new AlertDialog instance
     *
     * @param pAlertType type of the Dialog ,Error,Information,Warning
     * @param pHeaderText header text
     * @param pMsg text to be shown in the content area
     * @param pDetails Stacktrace or other details
     * @param pButtonTypes types of buttons to be set
     * @param pException exception or throwable
     */
    protected AlertBaseDialog(Alert.AlertType pAlertType, String pHeaderText, String pMsg, String pDetails, final Throwable pException, ButtonType... pButtonTypes) {
        super(pAlertType, "");
        ClipboardEnabler.installClipboardToScene(this.getDialogPane().getScene());
        String msg = pMsg == null ? "" : pMsg.trim();
        setHeaderText(pHeaderText);
        Glyph g = getAlertIcon(pAlertType);
        setGraphic(g);
        
        labelContent = new Label(pMsg);
        labelContent.setWrapText(true);
        //labelContent.setStyle("-fx-padding: 0 0 10 0");
        labelContent.setPadding(new Insets(0, 0, 10, 0));
        
        box = new VBox(labelContent);
        getDialogPane().setContent(box);
            
        skin = new DialogSkin<>(this);
        skin.setButtonTypes(pButtonTypes);
        
        Node content = createContentNode(msg, pDetails, pException);
        if(content!=null){
            box.getChildren().add(content);
        }
    }
    
    public Node createContentNode(String pMsg, String pDetails, final Throwable pException){
        return null;
    }
    public static String getAlertText(Alert.AlertType pAlertType){
        return AlertHelper.getAlertText(pAlertType);
    }
    public static Glyph getAlertIcon(Alert.AlertType pAlertType){
        return AlertHelper.getAlertIcon(pAlertType, ALERT_ICON_SIZE);
    }
//    public static String getAlertText(Alert.AlertType pAlertType){
//         switch (pAlertType) {
//            case CONFIRMATION:
//                return Lang.getConformation();
//            case INFORMATION:
//                return Lang.getInformation();
//            case WARNING:
//                return Lang.getWarning();
//            case ERROR:
//                return Lang.getError();
//            default:
//                return "Not set";
//        }
//    }
//    public static Glyph getAlertIcon(Alert.AlertType pAlertType) {
//        final Glyph g;
//        switch (pAlertType) {
//            case CONFIRMATION:
//                g = ResourceLoader.getGlyph(FontAwesome.Glyph.QUESTION_CIRCLE);
//                if (g != null) {
//                    g.setStyle("-fx-text-fill: #1569a6;");
//                }
//                break;
//            case INFORMATION:
//                g = ResourceLoader.getGlyph(FontAwesome.Glyph.INFO_CIRCLE);
//                if (g != null) {
//                    g.setStyle("-fx-text-fill: #1569a6;");
//                }
//                break;
//            case WARNING:
//                g = ResourceLoader.getGlyph(FontAwesome.Glyph.EXCLAMATION_TRIANGLE);
//                if (g != null) {
//                    g.setStyle("-fx-text-fill: #f6a117;");
//                }
//                break;
//            case ERROR:
//                g = ResourceLoader.getGlyph(FontAwesome.Glyph.EXCLAMATION_CIRCLE);
//                if (g != null) {
//                    g.setStyle("-fx-text-fill: #ca2424;");
//                }
//                break;
//            default:
//                //Unspecified alert type (NONE or null!)
//                g = ResourceLoader.getGlyph(FontAwesome.Glyph.BELL);
//                if (g != null) {
//                    g.setStyle("-fx-text-fill: #1569a6;");
//                }
//                break;
//        }
//        if (g != null) {
//            g.setFontSize(ALERT_ICON_SIZE);
//        }
//        return g;
//    }

    public static Glyph getLockIcon() {
        Glyph g = ResourceLoader.getGlyph(FontAwesome.Glyph.LOCK);
        //g.setStyle("-fx-text-fill: #f6a117;");
        if (g != null) {
            g.setFontSize(ALERT_ICON_SIZE);
        }
        return g;
    }

    /**
     * Transforms an exception's stacktrace into a string representation
     *
     * @param ex error
     * @return stacktrace as a string
     */
    public static String getStacktrace(final Throwable ex) {
        if (ex == null) {
            return "";
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String sStackTrace = sw.toString(); // stack trace as a string
        return sStackTrace;
    }

    /**
     * get the skin of the Alert dialog
     *
     * @return Skin of the dialog
     */
    public final DialogSkin<ButtonType> getSkin() {
        return skin;
    }
    
    public Label getContentLabel(){
        return labelContent;
    }
    
    public VBox getItemContainer(){
        return box;
    }
    
}
