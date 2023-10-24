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
package de.lb.cpx.client.app.wm.fx.dialog;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.job.fx.FileConvert;
import de.lb.cpx.client.app.wm.fx.dialog.editor.CatalogValidationResult;
import de.lb.cpx.client.app.wm.util.DocumentManager;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.menu.cache.MenuCacheOptionsEn;
import de.lb.cpx.client.core.model.enums.CpxErrorTypeEn;
import de.lb.cpx.client.core.model.fx.dialog.FormularDialog;
import de.lb.cpx.client.core.model.fx.labeled.LabeledComboBox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledDatePicker;
import de.lb.cpx.client.core.model.fx.labeled.LabeledLabel;
import de.lb.cpx.document.Utils;
import de.lb.cpx.server.commonDB.model.CWmListDocumentType;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmDocument;
import java.io.File;
import java.util.Date;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Cursor;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.util.StringConverter;
import org.apache.commons.io.FilenameUtils;
import org.controlsfx.glyphfont.Glyph;

/**
 * Add Documentdialog, loads FXML and handels user input, creates new document
 * that can be saved
 *
 * @author wilde
 */
public final class AddDocumentDialog extends FormularDialog<TWmDocument> {

    public BooleanProperty addDocumentProperty = new SimpleBooleanProperty(false);
    public ObjectProperty<TWmDocument> valueProperty = new SimpleObjectProperty<>();

    private byte[] content;
    private File file;
    private final LabeledLabel labelFileName;
    private final LabeledLabel labelFilePath;
    private final LabeledLabel labelTotalSpace;
    private final LabeledComboBox<CWmListDocumentType> cbDocumentType;
    private LabeledDatePicker dpDocumentDate;
    private final CatalogValidationResult catalogValidationResult;

    public AddDocumentDialog(File pFile) {
        super(MainApp.getStage(), Modality.WINDOW_MODAL, Lang.getDocumentAdd());
        this.file = pFile;

        labelFileName = new LabeledLabel(Lang.getDocumentName());
        labelFileName.setText(pFile.getName());

        labelFilePath = new LabeledLabel(Lang.getDocumentPath());
        labelFilePath.setText(pFile.getAbsolutePath());
        labelTotalSpace = new LabeledLabel(Lang.getDocumentSize());
        setTotalSpace(file.length());

        //offer the possibility to open the file directly on click
        final String fileExtension = FilenameUtils.getExtension(pFile.getName());
        final Utils.FILE_TYPES fileType = Utils.getFileType(pFile.getName());
        final Glyph docTypeGlyph = FileConvert.getDocTypeGlyph(fileType);
        labelFileName.getControl().setGraphic(docTypeGlyph);
        labelFileName.getControl().setContentDisplay(ContentDisplay.RIGHT);
        Tooltip.install(labelFileName.getControl(), new Tooltip(fileExtension.toUpperCase() + "-Datei öffnen"));
        labelFileName.getControl().setCursor(Cursor.HAND);
        labelFileName.getControl().setOnMouseClicked((event) -> {
            event.consume();
            DocumentManager.showFile(pFile);
        });

        //offer the possibility to show file directly in Windows Explorer
        Tooltip.install(labelFilePath.getControl(), new Tooltip("Datei im Explorer anzeigen"));
        labelFilePath.getControl().setCursor(Cursor.HAND);
        labelFilePath.getControl().setOnMouseClicked((event) -> {
            event.consume();
            DocumentManager.showInExplorer(file);
        });

        cbDocumentType = new LabeledComboBox<>(Lang.getDocumentType());
        dpDocumentDate = new LabeledDatePicker(Lang.getDocumentDate());
        dpDocumentDate.setDate(new Date());
        cbDocumentType.setItems(MenuCache.getMenuCacheDocumentTypes().values(new Date(), MenuCacheOptionsEn.IGNORE_INACTIVE));
        cbDocumentType.setConverter(new StringConverter<CWmListDocumentType>() {
            @Override
            public String toString(CWmListDocumentType object) {
                return object == null ? "" : object.getWmDtName();
            }

            @Override
            public CWmListDocumentType fromString(String string) {
                return null;
            }
        });
        HBox hbox = new HBox();
        hbox.getChildren().addAll(cbDocumentType, dpDocumentDate);

        addControls(labelFileName, labelFilePath, labelTotalSpace, cbDocumentType, dpDocumentDate);
        
        catalogValidationResult = new CatalogValidationResult();
        catalogValidationResult.add(CpxErrorTypeEn.WARNING, Lang.getValidationMasterdataNoDocumentTypesFound(), (t) -> {
            return MenuCache.getMenuCacheDocumentTypes().values(new Date(), MenuCacheOptionsEn.IGNORE_INACTIVE).isEmpty();
        });
        setMessageText(catalogValidationResult.getValidationMessages());
        setMessageType(catalogValidationResult.getHighestErrorType());
    }


    private TWmDocument getDocument() {
        TWmDocument doc = new TWmDocument();
        doc.setName(file.getName());
        doc.setDocumentType(cbDocumentType.getSelectedItem() != null ? cbDocumentType.getSelectedItem().getWmDtInternalId() : null);
        doc.setDocumentDate(dpDocumentDate.getDate());
        doc.setContent(content);
        return doc;
    }

    public BooleanProperty getAddDocumentProperty() {
        return addDocumentProperty;
    }

    public void setContent(byte[] content) {
        this.content = content == null ? null : content.clone();
    }

    @Override
    public TWmDocument onSave() {
        addDocumentProperty.setValue(Boolean.TRUE);
        valueProperty.setValue(getDocument());
        return valueProperty.getValue();
    }

    public void setTotalSpace(long totalSize) {
        labelTotalSpace.setText(Lang.formatBytes(totalSize, 2));//new DecimalFormat("##.##").format(Lang.toMByte(totalSize)) + " MB");
    }
}
