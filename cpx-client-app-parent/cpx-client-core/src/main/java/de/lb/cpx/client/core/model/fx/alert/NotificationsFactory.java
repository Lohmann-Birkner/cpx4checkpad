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
package de.lb.cpx.client.core.model.fx.alert;

import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.shared.lang.Lang;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.Notifications;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

/**
 *
 * @author niemeier
 */
public class NotificationsFactory {

    private static NotificationsFactory instance;

    private static final String ICON_STYLE = "-fx-font-family: 'FontAwesome';"
            + "-fx-font-size: 50;";
    private static final String ICON_STYLE_ERROR = ICON_STYLE.concat("-fx-text-fill:#ca2424;");
    private static final String ICON_STYLE_WARNING = ICON_STYLE.concat("-fx-text-fill:#f6a117;");
    private static final String ICON_STYLE_INFO = ICON_STYLE.concat("-fx-text-fill:#1569a6;");
    private static final String ICON_STYLE_SUCCESS = ICON_STYLE.concat("-fx-text-fill:#4bb543;");

    private NotificationsFactory() {

    }

    public static synchronized NotificationsFactory instance() {
        if (instance == null) {
            instance = new NotificationsFactory();
        }
        return instance;
    }

    public final Notifications createNotification() {
        Notifications notif = Notifications.create();
        notif.position(Pos.TOP_CENTER);
//        notif.darkStyle();
        notif.owner(BasicMainApp.getWindow());
        //20180713 AWi: add own notificationPopOver css to mainApp, if not already present, to possibly change style of the notification
        String css = this.getClass().getResource("/styles/notificationpopup.css").toExternalForm();
        if (!BasicMainApp.getWindow().getScene().getStylesheets().contains(css)) {
            BasicMainApp.getWindow().getScene().getStylesheets().add(css);
        }
        return notif;
    }

    /**
     * @param pGlyphIcon glyph
     * @return creates default dialog with the glyph icon NOTE: Display of icon
     * could be wrong, font family must be added to glyph manually
     */
    public final Notifications createNotification(Glyph pGlyphIcon) {
        Notifications not = createNotification();
        not.position(Pos.BOTTOM_RIGHT);
        not.owner(BasicMainApp.getWindow());
        not.graphic(pGlyphIcon);
        return not;
    }

    /**
     * @return warning notification with EXCLAMATION_CIRCLE as ICON, color is
     * #ca2424
     */
    public final Notifications createErrorNotification() {
        return createNotification(getErrorIcon());
    }

    /**
     * @return success/finish notification with CHECK as ICON, color is #4bb543
     */
    public final Notifications createSuccessNotification() {
        return createNotification(getSuccessIcon());
    }

    /**
     * @return warning notification with EXCLAMATION_TRIANGLE as ICON, color is
     * #f6a117
     */
    public final Notifications createWarningNotification() {
        return createNotification(getWarningIcon());
    }

    /**
     * @return information notification with INFO_CIRCLE as ICON, color is
     * #1569a6
     */
    public final Notifications createInformationNotification() {
        return createNotification(getInformationIcon());
    }
    public final Node createupdateCatalogNotificationLayout(){
        Label text = new Label(Lang.getCatalogUpdateNotification());
        Label desc = new Label("(Einstellungen > Kataloge > Kataloge aktualisieren)");
        Glyph iconNode = ResourceLoader.getGlyph(FontAwesome.Glyph.EXTERNAL_LINK);
        Label icon = new Label("",iconNode);
        iconNode.setStyle("-fx-font-family: 'FontAwesome'; -fx-text-fill:#46a877;");
        desc.setStyle("-fx-font-style: italic;");
        HBox descBox = new HBox(5,desc,icon);
        descBox.setAlignment(Pos.CENTER_LEFT);
        VBox textContent = new VBox(5,text,descBox);
        textContent.setAlignment(Pos.CENTER_LEFT);
        HBox content = new HBox(5,getInformationIcon(),textContent);
        content.setAlignment(Pos.CENTER_LEFT);
//        content.setPrefHeight(65);
        return content;
    }
    public final Notifications createUpdateCatalogNotification(){
        Notifications not = createNotification(null)
                .title("CPX: " + Lang.getCatalogMenuUpdate())
                .graphic(createupdateCatalogNotificationLayout());
        return not;
    }
    /**
     * @return notification as default icon, icon is a bell
     */
    public final Notifications createDefaultNotification() {
        return createNotification(getDefaultIcon());
    }
    
    public static Glyph getInformationIcon(){
        return getIcon(FontAwesome.Glyph.INFO_CIRCLE, ICON_STYLE_INFO);
    }
    
    public static Glyph getWarningIcon(){
        return getIcon(FontAwesome.Glyph.EXCLAMATION_TRIANGLE, ICON_STYLE_WARNING);
    }
    
    public static Glyph getSuccessIcon(){
        return getIcon(FontAwesome.Glyph.CHECK, ICON_STYLE_SUCCESS);
    }
    public static Glyph getErrorIcon(){
        return getIcon(FontAwesome.Glyph.EXCLAMATION_CIRCLE, ICON_STYLE_ERROR);
    }
    
    public static Glyph getDefaultIcon(){
        return getIcon(FontAwesome.Glyph.BELL, ICON_STYLE);
    }
    
    public static Glyph getIcon(FontAwesome.Glyph pGlyph, String pStyle){
        Glyph icon = ResourceLoader.getGlyph(pGlyph);
        icon.setStyle(pStyle);
        return icon;
    }
    
}
