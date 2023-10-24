/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.process.section.details;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.job.fx.FileConvert;
import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.process.section.WmDocumentSection;
import de.lb.cpx.client.app.wm.fx.process.section.operations.WmDocumentOperations;
import de.lb.cpx.client.app.wm.util.DocumentManager;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.fx.combobox.DocumentTypeCombobox;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.document.Utils;
import de.lb.cpx.server.commonDB.model.CWmListDocumentType;
import de.lb.cpx.service.ejb.ProcessServiceBeanRemote;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmDocument;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.apache.commons.io.FilenameUtils;
import org.controlsfx.control.action.Action;
import org.controlsfx.glyphfont.Glyph;

/**
 * Detailclass to expose detail pane to the history, avoid doublicate source
 * code
 *
 * @author wilde
 */
public class WmDocumentDetails extends WmDetails<TWmDocument> {

    public WmDocumentDetails(ProcessServiceFacade pFacade, TWmDocument pItem) {
        super(pFacade, pItem);
    }

//    public WmDocumentDetails(ProcessServiceFacade pFacade) {
//        super(pFacade, null);
//    }
    @Override
    public String getDetailTitle() {
        return Lang.getEventNameDocument() + ": " + item.getName();
    }

    /**
     * create from the TWmDocumentEntity the representation of the Document in
     * its detailview
     *
     * @return Parent pane root of the document detail
     */
    @Override
    protected Parent getDetailNode() {
        VBox detailContent = new VBox();
        TitledPane tpGenerelInfos = new TitledPane();
        tpGenerelInfos.setText(Lang.getGeneral());

        GridPane gpInfos = new GridPane();
        gpInfos.setVgap(10.0);
        gpInfos.setHgap(10.0);

        Label documentTypeText = new Label(Lang.getDocumentType());
        documentTypeText.getStyleClass().add("cpx-detail-label");

        Label documentNameText = new Label(Lang.getDocumentName());
        documentNameText.getStyleClass().add("cpx-detail-label");

        Label documentDateText = new Label(Lang.getDocumentDate());
        documentDateText.getStyleClass().add("cpx-detail-label");

        Label documentCreationUserText = new Label("Erstellt durch");
        documentCreationUserText.getStyleClass().add("cpx-detail-label");

        Label documentFileSizeText = new Label(Lang.getDocumentSize());
        documentFileSizeText.getStyleClass().add("cpx-detail-label");

//                Label documentName = new Label(doc.getName());
        TextField documentName = new TextField();
//                TextField documentName = createUnmodifiedPrefixTextField("fixed_");
        String fileExtension = FilenameUtils.getExtension(item.getName());
        String baseName = FilenameUtils.getBaseName(item.getName());
//                documentName.setText(doc.getName());
        documentName.setText(baseName);
        documentName.setMaxHeight(8);
//                documentName.setDisable(true);
//                byte[] content = doc.getContent(); //gives null??
        documentName.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue.booleanValue() == Boolean.FALSE) {
                if (documentName.getText() != null && !documentName.getText().isEmpty() && DocumentManager.isValidFilepath(documentName.getText())) {
                    item.setName(documentName.getText() + "." + fileExtension);
                    getFacade().updateDocument(item);
                    getFacade().getProperties().put(WmDocumentSection.REFRESH_TABLE_VIEW, Boolean.TRUE);
                } else {
                    MainApp.showErrorMessageDialog("Bitte geben Sie einen gültigen Dokumentennamen ein. (Ungültige Zeichen: {'/', '\\n', '\\r', '\\t', '\\0', '\\f', '`', '?', '*', '\\\\', '<', '>', '|', '\\\"', ':'}");
                }
            }
        });
        documentName.setOnAction(new Action((ActionEvent t) -> {
            if (documentName.getText() != null && !documentName.getText().isEmpty() && DocumentManager.isValidFilepath(documentName.getText())) {
                item.setName(documentName.getText() + "." + fileExtension);
                getFacade().updateDocument(item); // this method explicitly set the document content to null (see updateDocument(TWmDocument pDocument) in ProcessServiceBean)
//                            String docNameToUpdate = documentName.getText() + "." + fileExtension;
//                            ProcessServiceBean.updateDocumentName(doc, docNameToUpdate);    //the persistence context is not synchronized with the update result
                getFacade().getProperties().put(WmDocumentSection.REFRESH_TABLE_VIEW, Boolean.TRUE);
//                            tvDocuments.refresh();
            } else {
                MainApp.showErrorMessageDialog("Bitte geben Sie einen gültigen Dokumentennamen ein. (Ungültige Zeichen: {'/', '\\n', '\\r', '\\t', '\\0', '\\f', '`', '?', '*', '\\\\', '<', '>', '|', '\\\"', ':'}");
            }
        }));

//                Label documentType = new Label(doc.getDocumentType());
        ComboBox<CWmListDocumentType> documentType = new DocumentTypeCombobox(item.getDocumentType());
        documentType.getStyleClass().add("header-combo-box");
        documentType.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends CWmListDocumentType> observable, CWmListDocumentType oldValue, CWmListDocumentType newValue) -> {
            if (newValue != null) {
                item.setDocumentType(newValue.getWmDtInternalId());
                getFacade().updateDocument(item);
                getFacade().getProperties().put(WmDocumentSection.REFRESH_TABLE_VIEW, Boolean.TRUE);
            }
        });

        Label documentDate = new Label();
        documentDate.setWrapText(true);
        documentDate.setText(Lang.toDate(item.getDocumentDate()));

        Label documentCreationUser = new Label();
        documentCreationUser.setWrapText(true);
        documentCreationUser.setText(item.getCreationUser() != null ? getFacade().getUserFullName(item.getCreationUser()) : "");

        Label documentFileSize = new Label();
        documentFileSize.setWrapText(true);
        documentFileSize.setText(Lang.formatBytes(item.getFileSize(), 2));//org.apache.commons.io.FileUtils.byteCountToDisplaySize(item.getFileSize()));

        Utils.FILE_TYPES fileType = Utils.getFileType(item.getName());
        Glyph docTypeGlyph = FileConvert.getDocTypeGlyph(fileType);
        if (docTypeGlyph != null) {
            Tooltip.install(docTypeGlyph, new Tooltip(fileExtension.toUpperCase() + "-Datei öffnen"));
            docTypeGlyph.setCursor(Cursor.HAND);
            docTypeGlyph.setOnMouseClicked((event) -> {
                event.consume();
//                EjbProxy<ProcessServiceBeanRemote> processServiceBean = Session.instance().getEjbConnector().connectProcessServiceBean();
                DocumentManager.openDocument(item, facade);
            });
        }

        gpInfos.add(documentNameText, 0, 0);
        gpInfos.add(documentName, 1, 0);
        gpInfos.add(docTypeGlyph, 2, 0);
        gpInfos.add(documentTypeText, 0, 1);
        gpInfos.add(documentType, 1, 1);
        gpInfos.add(documentDateText, 0, 2);
        gpInfos.add(documentDate, 1, 2);
        gpInfos.add(documentCreationUserText, 0, 3);
        gpInfos.add(documentCreationUser, 1, 3);
        gpInfos.add(documentFileSizeText, 0, 4);
        gpInfos.add(documentFileSize, 1, 4);

        ColumnConstraints columnConstraintHalf = new ColumnConstraints();
        columnConstraintHalf.setPercentWidth(50);

        gpInfos.getColumnConstraints().add(columnConstraintHalf);

        tpGenerelInfos.setContent(gpInfos);

        detailContent.getChildren().addAll(tpGenerelInfos);

        detailContent.setDisable(!Session.instance().getRoleProperties().isEditDocumentAllowed()
                && !Session.instance().getRoleProperties().isEditDocumentOfOtherUserAllowed());

        return detailContent;
    }

    @Override
    public WmDocumentOperations getOperations() {
        return new WmDocumentOperations(facade);
    }

}
