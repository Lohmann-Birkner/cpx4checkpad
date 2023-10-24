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
 *    2018  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.util.texttemplate;

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.util.bookmarksmapping.CreateBookmarksHashMap;
import de.lb.cpx.client.core.util.ContextMenuUtil;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.enums.TextTemplateTypeEn;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.skin.TextAreaSkin;
import javafx.scene.control.skin.TextFieldSkin;
import javafx.stage.Window;

/**
 * Handling of the text template related bookmarks and corresponding dialog.
 *
 * @author nandola
 */
public class TextTemplateController {

    private final Map<String, String> hm;
    private final TextTemplateTypeEn textTemplateContext;
    private MenuItem textTemplateMenuItem;
    private final Window owner;

    public TextTemplateController(TextTemplateTypeEn pTextTemplateContext, ProcessServiceFacade pFacade, Window pOwner) {
        this(pTextTemplateContext, pFacade, pOwner, null);
    }

    public TextTemplateController(TextTemplateTypeEn pTextTemplateContext, ProcessServiceFacade pFacade, Window pOwner, TCase pCase) {
        this.textTemplateContext = pTextTemplateContext;
        this.owner = pOwner;
        this.hm = new CreateBookmarksHashMap().fillHashMap(pFacade, pCase);
    }

    public TextTemplateController(TextTemplateTypeEn pTextTemplateContext, Window pOwner, TCase pCase) {
        this.textTemplateContext = pTextTemplateContext;
        this.owner = pOwner;
        this.hm = new CreateBookmarksHashMap().fillHashMap(pCase);
    }

    public TextAreaSkin customContextSkin(TextArea textArea) {
        TextAreaSkin customContextSkin = new TextAreaSkin(textArea) {
        };
        final List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new SeparatorMenuItem());
        textTemplateMenuItem = new MenuItem(MENU_ITEM_TEXT);
        textTemplateMenuItem.setOnAction(getCreateFromTemplateEventHandler(textArea));
        menuItems.add(textTemplateMenuItem);
        ContextMenuUtil.install(customContextSkin, menuItems);

        return customContextSkin;
    }
    public static final String MENU_ITEM_TEXT = "Textbausteine";
    public final EventHandler<ActionEvent> getCreateFromTemplateEventHandler(TextInputControl pTextInputControl){
        return (ActionEvent event) -> {
            StringBuilder onSaveResults = TextTemplateDialogCreation();
            
            if (onSaveResults != null && !onSaveResults.toString().isEmpty()) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        pTextInputControl.setText((pTextInputControl.getText() != null && !pTextInputControl.getText().isEmpty()) ? pTextInputControl.getText().concat(onSaveResults.toString()) : onSaveResults.toString());
                    }
                });
            }
        };
    }
    public TextFieldSkin customContextSkin(TextField textField) {
        TextFieldSkin customContextSkin = new TextFieldSkin(textField) {
        };

        final List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new SeparatorMenuItem());
        textTemplateMenuItem = new MenuItem(MENU_ITEM_TEXT);
        textTemplateMenuItem.setOnAction(getCreateFromTemplateEventHandler(textField));
//        textTemplateMenuItem.setOnAction((ActionEvent event) -> {
//
//            StringBuilder onSaveResults = TextTemplateDialogCreation();
//
//            if (onSaveResults != null && !onSaveResults.toString().isEmpty()) {
//                textField.setText((textField.getText() != null && !textField.getText().isEmpty()) ? textField.getText().concat(onSaveResults.toString()) : onSaveResults.toString());
//            }
//        });
        menuItems.add(textTemplateMenuItem);
        ContextMenuUtil.install(customContextSkin, menuItems);

        return customContextSkin;
    }

    private StringBuilder TextTemplateDialogCreation() {
        TextTemplateDialog textTemplateDialog = new TextTemplateDialog(owner, textTemplateContext, hm);
        textTemplateDialog.showAndWait();
        StringBuilder onSaveResults = textTemplateDialog.getResults();
        return onSaveResults;
    }

    public MenuItem getTextTemplateMenuItem() {
        return textTemplateMenuItem;
    }

}
