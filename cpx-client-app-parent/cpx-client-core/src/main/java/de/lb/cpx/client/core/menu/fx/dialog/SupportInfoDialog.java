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
package de.lb.cpx.client.core.menu.fx.dialog;

import de.lb.cpx.client.core.MainApp;
import static de.lb.cpx.client.core.menu.fx.ToolbarMenuFXMLController.infoLabel;
import static de.lb.cpx.client.core.menu.fx.ToolbarMenuFXMLController.keyLabel;
import de.lb.cpx.client.core.model.fx.dialog.TitledDialog;
import de.lb.cpx.client.core.util.ResourceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.controlsfx.glyphfont.FontAwesome.Glyph;

/**
 *
 * @author niemeier
 */
public class SupportInfoDialog extends TitledDialog {

    //Distributor & Producer Infos
    public static final String PRODUCER_NAME = "Lohmann & Birkner Health Care Consulting GmbH";
    public static final String PRODUCER_STREET = "Alt-Reinickendorf 25";
    public static final String PRODUCER_ZIPCODE = "13407";
    public static final String PRODUCER_CITY = "Berlin";

    //Support Infos
    public static final String SUPPORT_PHONE = "+49 (30) 40 99 85 - 555";
    public static final String SUPPORT_FAX = "+49 (30) 40 99 85 - 109";
    public static final String SUPPORT_EMAIL = "support@lohmann-birkner.de";
    public static final String SUPPORT_WEBSITE = "www.lohmann-birkner.de";

    public static final String TEAMVIEWER_WEBSITE = "https://www.teamviewer.com/de/download/windows/";

    private static final Logger LOG = Logger.getLogger(SupportInfoDialog.class.getName());
    private static SupportInfoDialog dialogInstance = null;

    public static synchronized void showDialog() {
        if (dialogInstance == null) {
            dialogInstance = new SupportInfoDialog();
        } else {
            dialogInstance.getDialogSkin().centerWindow();
        }
        dialogInstance.show();
    }

    /**
     * construct new instance
     *
     */
    private SupportInfoDialog() {
        super("Support", MainApp.getWindow(), false);
        //getDialogPane().setMaxWidth(200D);
        GridPane gpLic = new GridPane();

        int rowIndex = 0;

        org.controlsfx.glyphfont.Glyph mapIcon = ResourceLoader.getGlyph(Glyph.MAP_MARKER);
        mapIcon.setTooltip(new Tooltip("In Google Maps anzeigen"));
        mapIcon.setCursor(Cursor.HAND);
        mapIcon.setOnMouseClicked((evt) -> {
            MainApp.openUrl("https://www.google.de/maps/place/Lohmann+%26+Birkner+Health+Care+Consulting+GmbH/@52.5761,13.3495413,17z/data=!3m1!4b1!4m5!3m4!1s0x47a853879bc7ff57:0xdaab9d764a76e85f!8m2!3d52.5761!4d13.35173");
        });
        mapIcon.setPadding(new Insets(0, 0, 0, 5));

        //display logo of L&B?
        gpLic.add(keyLabel("Hersteller"), 0, rowIndex);
        gpLic.add(infoLabel(PRODUCER_NAME, null), 1, rowIndex++);
        gpLic.add(new HBox(infoLabel(PRODUCER_STREET, null), mapIcon), 1, rowIndex++);
        gpLic.add(infoLabel(PRODUCER_ZIPCODE + " " + PRODUCER_CITY, null), 1, rowIndex++);
//        gpLic.add(mapIcon, 1, rowIndex++);

        Hyperlink phone = new Hyperlink("Tel. " + SUPPORT_PHONE);
        Hyperlink fax = new Hyperlink("Fax " + SUPPORT_FAX);
        Hyperlink mail = new Hyperlink("Mail " + SUPPORT_EMAIL);
        Hyperlink website = new Hyperlink("WWW " + SUPPORT_WEBSITE);

        phone.setOnAction((evt) -> {
            MainApp.openUrl("tel:" + toProtocol(SUPPORT_PHONE));
        });
        fax.setOnAction((evt) -> {
            MainApp.openUrl("fax:" + toProtocol(SUPPORT_FAX));
        });
        mail.setOnAction((evt) -> {
            MainApp.openUrl("mailto:" + SUPPORT_EMAIL);
        });
        website.setOnAction((evt) -> {
            MainApp.openUrl("https://" + SUPPORT_WEBSITE);
        });

        //display logo of CPX?
        gpLic.add(keyLabel("Support:"), 0, rowIndex);
        gpLic.add(phone, 1, rowIndex++);
        gpLic.add(fax, 1, rowIndex++);
        gpLic.add(mail, 1, rowIndex++);
        gpLic.add(website, 1, rowIndex++);

        //display logo of TeamViewer?
        //gpLic.add(new Hyperlink(TEAMVIEWER_WEBSITE), 1, rowIndex++);
        gpLic.setVgap(10);
        getDialogPane().setContent(gpLic);
        getDialogSkin().setButtonTypes(ButtonType.OK);
//            MainApp.showInfoMessageDialog("Lizenz", gpLic);

        showingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    LOG.log(Level.INFO, "close license info dialog");
                    dialogInstance = null;
                }
            }
        });
    }

    private static String toProtocol(final String pPhone) {
        return pPhone
                .replace("-", "")
                .replace("(", "")
                .replace(")", "")
                .replace(" ", "");
    }

}
