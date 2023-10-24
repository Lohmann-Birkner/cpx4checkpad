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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.alert;

import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.config.Session;
import static de.lb.cpx.client.core.model.fx.alert.AlertDialog.getLockIcon;
import de.lb.cpx.client.core.model.fx.dialog.DialogSkin;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.client.core.util.clipboard.ClipboardEnabler;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.service.ejb.LockService;
import de.lb.cpx.shared.dto.LockDTO;
import de.lb.cpx.shared.dto.LockException;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Window;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

/**
 *
 * @author niemeier
 */
public class LockDialog extends Alert {

    private final DialogSkin<ButtonType> skin;
    private final GridPane grid = new GridPane();
    private final Label labelContent;
    private final TextArea detailsContent;

    /**
     * creates a new none modal error dialog
     *
     * @param pMsg msg to be shown in the content area
     * @return new alert dialog
     */
    public static LockDialog createLockDialog(final String pMsg) {
        String details = "";
        final LockException exception = null;
        return createLockDialog(pMsg, details, exception);
    }

    /**
     * creates a new none modal error dialog
     *
     * @param pMsg msg to be shown in the content area
     * @param pException error or LockException
     * @return new alert dialog
     */
    public static LockDialog createLockDialog(final String pMsg, final LockException pException) {
        String details = "";
        return createLockDialog(pMsg, details, pException);
    }

    /**
     * creates a new none modal error dialog
     *
     * @param pMsg msg to be shown in the content area
     * @param pDetails Strack Trace or other details
     * @param pException error or LockException
     * @return new alert dialog
     */
    public static LockDialog createLockDialog(final String pMsg, final String pDetails,
            final LockException pException) {
        return createLockDialog(pMsg, pDetails, BasicMainApp.getWindow(), pException);
    }

    /**
     * creates a new error Dialog with the msg,specified owner window,modiality
     * and buttons
     *
     * @param pMsg text to be shown in the content area
     * @param pDetails Stack Trace or other details
     * @param pOwner owner window of the alert dialog
     * @param pModality modality mode for the dialog(Apllication modal,Window
     * modal or none)
     * @param pException error or LockException
     * @return new alert dialog
     */
    public static LockDialog createLockDialog(final String pMsg, final String pDetails,
            final Window pOwner, final Modality pModality, final LockException pException) {
        LockDialog dialog = new LockDialog(pMsg, pDetails, pException);
        dialog.initOwner(pOwner);
        dialog.initModality(pModality);
//        dialog.setGraphic(new ImageView(dialog.getClass().getResource("/img/error32.png").toString()));
        return dialog;
    }

    /**
     * creates a new error Dialog with the msg,specified owner window and
     * buttons Dialog is set to Window Modal
     *
     * @param pMsg text to be shown in the content area
     * @param pOwner owner window of the alert dialog
     * @param pException lock exception
     * @return new alert dialog
     */
    public static LockDialog createLockDialog(final String pMsg, final Window pOwner, final LockException pException) {
        String details = "";
        return createLockDialog(pMsg, details, pOwner, Modality.APPLICATION_MODAL, pException);
    }

    /**
     * creates a new error Dialog with the msg,specified owner window and
     * buttons Dialog is set to Window Modal
     *
     * @param pMsg text to be shown in the content area
     * @param pOwner owner window of the alert dialog
     * @return new alert dialog
     */
    public static LockDialog createLockDialog(final String pMsg, final Window pOwner) {
        String details = "";
        final LockException exception = null;
        return createLockDialog(pMsg, details, pOwner, exception);
    }

    /**
     * creates a new error Dialog with the msg,specified owner window and
     * buttons Dialog is set to Window Modal
     *
     * @param pMsg text to be shown in the content area
     * @param pDetails Stack Trace or other details
     * @param pOwner owner window of the alert dialog
     * @param pException error or LockException
     * @return new alert dialog
     */
    public static LockDialog createLockDialog(String pMsg, String pDetails, Window pOwner, final LockException pException) {
        return createLockDialog(pMsg, pDetails, pOwner, Modality.APPLICATION_MODAL, pException);
    }

    /**
     * creates a new AlertDialog instance
     *
     * @param pMsg text to be shown in the content area
     * @param pDetails Stacktrace or other details
     * @param pException lock exception
     */
    public LockDialog(final String pMsg, final String pDetails, final LockException pException) {
        super(AlertType.ERROR, "");
        ClipboardEnabler.installClipboardToScene(this.getDialogPane().getScene());
        setHeaderText("Sperre erkannt");
        Glyph g = getLockIcon();
        setGraphic(g);

        VBox box = new VBox();
        GridPane lockGrid = null;

        labelContent = new Label(pMsg);
        labelContent.setWrapText(true);
        labelContent.setStyle("-fx-padding: 0 0 10 0");

        if ((labelContent.getText() != null && !labelContent.getText().trim().isEmpty())) {
            box.getChildren().add(grid);
            grid.addRow(0, labelContent);
        }

        //LockException lockException = (LockException) pException;
        lockGrid = new GridPane();
        lockGrid.setVgap(10d);
        lockGrid.setHgap(5d);
        lockGrid.getColumnConstraints().add(0, new ColumnConstraints());
        lockGrid.getColumnConstraints().add(1, new ColumnConstraints());
        lockGrid.getColumnConstraints().get(0).setHgrow(Priority.ALWAYS);
        lockGrid.getColumnConstraints().get(1).setHgrow(Priority.NEVER);
        final List<Button> unlockButtons = new ArrayList<>();
        final List<Button> closeButtons = new ArrayList<>();
        if (pException != null && pException.getLockDtos() != null && pException.getLockDtos().length > 0) {
            //int k = 0;
            //pMsg = "";
            String text = "";
            int row = 0;
            boolean hasMoreLocks = false;
            final int maxShownLocks = 10;
            for (LockDTO lockDto : pException.getLockDtos()) {
                if (lockDto == null) {
                    continue;
                }
                //if (k > 0) {
//                    if (!pMsg.isEmpty()) {
//                        pMsg += "\r\n";
//                    }
                final boolean lockedByClient = lockDto.getClientId().equalsIgnoreCase(Session.instance().getClientId());
                final boolean canBeClosed = (lockedByClient
                        && ((lockDto.isCaseLock() && (BasicMainApp.getToolbarMenuScene().isCaseOpen(lockDto.caseId) || BasicMainApp.getToolbarMenuScene().isProcessCaseOpen(lockDto.caseId)))
                        || (lockDto.isProcessLock() && BasicMainApp.getToolbarMenuScene().isProcessOpen(lockDto.processId))));

                Button closeBtn = new Button();
                Button unlockBtn = new Button();
                closeBtn.setMnemonicParsing(false);
                unlockBtn.setMnemonicParsing(false);
                closeButtons.add(closeBtn);
                unlockButtons.add(unlockBtn);
                if (!canBeClosed) {
                    closeBtn.setVisible(false);
                }
                unlockBtn.setVisible(true);
                unlockBtn.setText(lockDto.getEntityLabel() + " entsperren");
                unlockBtn.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.UNLOCK));
                closeBtn.setText(lockDto.getEntityLabel() + " schließen");
                //closeBtn.setTooltip(new Tooltip(lockDto.getEntityLabel() + " schließen"));
                closeBtn.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.CLOSE));
                //unlockButtonBox.getChildren().add(unlockCaseBtn);
                //}
                EjbProxy<LockService> lockServiceBean = Session.instance().getEjbConnector().connectLockServiceBean();
                if (lockDto.isCaseLock()) {
                    text = Lang.getLockCaseEntryMessage(lockDto.getCaseNumber(), lockDto.getUserName(), Lang.toDateTime(lockDto.since));
                    unlockBtn.addEventHandler(ActionEvent.ACTION, (event) -> {

//                      
                        if (!lockServiceBean.get().isCaseLocked(lockDto.caseId) || lockServiceBean.get().unlockCase(lockDto.caseId, true)) {
//                                MainApp.showInfoMessageDialog(Lang.getWorkingListContextMenuUnlockSuccess(lockDto.caseNumber));
                            unlockBtn.setDisable(true);
                        }
//                           

                    });
                    closeBtn.addEventHandler(ActionEvent.ACTION, (event) -> {
                        //TODO:comment in, this is not reflected in toolbar
                        int closeCount = BasicMainApp.getToolbarMenuScene().closeAllCases(lockDto.caseId);//BasicMainApp.getToolbarMenuScene().getController().closeAllCases(lockDto.caseId);
                        closeBtn.setDisable(true);
                        if (!unlockBtn.isDisabled() && unlockBtn.isVisible()) {
                            unlockBtn.fire();
                        }
                        if (closeCount > 0) {
                            Notifications.create()
                                    .text("Es wurden " + closeCount + " Tabs geschlossen")
                                    .darkStyle()
                                    .hideAfter(Duration.seconds(3))
                                    .show();
                        }
                    });
                } else if (lockDto.isProcessLock()) {
                    text = Lang.getLockProcessEntryMessage(String.valueOf(lockDto.getWorkflowNumber()), lockDto.getUserName(), Lang.toDateTime(lockDto.since));
                    unlockBtn.addEventHandler(ActionEvent.ACTION, (event) -> {
                        if (!lockServiceBean.get().isProcessLocked(lockDto.processId) || lockServiceBean.get().unlockProcess(lockDto.processId, true)) {
                            //MainApp.showInfoMessageDialog(Lang.getWorkingListContextMenuUnlockSuccess(lockDto.caseNumber));
                            unlockBtn.setDisable(true);
                        } else {
                            BasicMainApp.showErrorMessageDialog(Lang.getWorkingListContextMenuUnlockError(lockDto.workflowNumber));
                        }
                    });
                    closeBtn.addEventHandler(ActionEvent.ACTION, (event) -> {
                        //TODO:comment in, this is not reflected in toolbar
                        BasicMainApp.getToolbarMenuScene().closeProcess(lockDto.processId);
//                        BasicMainApp.getToolbarMenuScene().getController().closeProcess(lockDto.processId);
                        closeBtn.setDisable(true);
                        if (!unlockBtn.isDisabled() && unlockBtn.isVisible()) {
                            unlockBtn.fire();
                        }
                        //closeBtn.setDisable(true);
                    });
                } else {
                    //DB Lock
                    text = Lang.getJobLockErrorDatabase(lockDto.getUserName(), lockDto.getCause(), Lang.toDateTime(lockDto.since));
                    unlockBtn.addEventHandler(ActionEvent.ACTION, (event) -> {
                        lockServiceBean.get().unlockDatabase();
                        //MainApp.showInfoMessageDialog(Lang.getWorkingListContextMenuUnlockSuccess(lockDto.caseNumber));
                        unlockBtn.setDisable(true);
                    });
                }
                Label label = new Label(text);
                label.setWrapText(true);
                lockGrid.addRow(row++, label, unlockBtn, closeBtn);
                //k++;
                if (row >= maxShownLocks) {
                    hasMoreLocks = true;
                    break;
                }
            }
            int numberOfCloseButtons = 0;
            for (Button btn : closeButtons) {
                if (btn != null && btn.isVisible() && !btn.isDisabled()) {
                    numberOfCloseButtons++;
                }
            }
            Button closeAllBtn = new Button();
            if (row > 1) {
                //More than one lock? Offer "unlock all" and "close all" buttons
                Button unlockAllBtn = new Button();
                unlockAllBtn.setVisible(true);
                unlockAllBtn.setText("Alle entsperren");
                unlockAllBtn.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.UNLOCK));
                unlockAllBtn.addEventHandler(ActionEvent.ACTION, (event) -> {
                    for (Button btn : unlockButtons) {
                        if (btn == null) {
                            continue;
                        }
                        if (btn.isDisabled()) {
                            continue;
                        }
                        if (!btn.isVisible()) {
                            continue;
                        }
                        btn.fire();
                    }
                    unlockAllBtn.setDisable(true);
                });
                closeAllBtn.setVisible(false);
                lockGrid.addRow(row++, new Label(""), unlockAllBtn, closeAllBtn);
                if (numberOfCloseButtons > 0) {
                    closeAllBtn.setVisible(true);
                    closeAllBtn.setText("Alle schließen");
                    closeAllBtn.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.UNLOCK));
                    //lockGrid.addRow(row++, new Label(""), closeAllBtn);
                    closeAllBtn.addEventHandler(ActionEvent.ACTION, (event) -> {
                        for (Button btn : closeButtons) {
                            if (btn == null) {
                                continue;
                            }
                            if (btn.isDisabled()) {
                                continue;
                            }
                            if (!btn.isVisible()) {
                                continue;
                            }
                            btn.fire();
                        }
                        closeAllBtn.setDisable(true);
                    });
                }
            }
            if (numberOfCloseButtons == 0) {
                //All locks are acquired by other clients (or they are database locks), so we can remove the whole column
                lockGrid.getChildren().removeAll(closeButtons);
                lockGrid.getChildren().removeAll(closeAllBtn);
            }
            if (hasMoreLocks) {
                lockGrid.addRow(row++, new Label("und " + (pException.getLockDtos().length - maxShownLocks) + " weitere Sperren..."));
            }
        }

        detailsContent = pDetails == null || pDetails.trim().isEmpty() ? null : new TextArea(pDetails);

        grid.getColumnConstraints().add(0, new ColumnConstraints());
        grid.getColumnConstraints().add(1, new ColumnConstraints());
        grid.getColumnConstraints().get(0).setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().get(1).setHgrow(Priority.NEVER);
        grid.getRowConstraints().add(0, new RowConstraints());
        grid.getRowConstraints().get(0).setVgrow(Priority.ALWAYS);

        if (detailsContent != null) {
            detailsContent.setEditable(false);
            //detailsContent.setPrefWidth(Region.USE_COMPUTED_SIZE);
            //detailsContent.setPrefHeight(Region.USE_COMPUTED_SIZE);
            ScrollPane scrollPane = new ScrollPane(detailsContent);
            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(true);
            VBox.setVgrow(scrollPane, Priority.ALWAYS);
            scrollPane.setMinWidth(600d);
            scrollPane.setMinHeight(300d);
            box.getChildren().add(new Label("Details"));
            box.getChildren().add(scrollPane);
        }

        box.getChildren().add(lockGrid);

        getDialogPane().setContent(box);
        skin = new DialogSkin<>(this);
        skin.setMinHeight(50);
        skin.setMinWidth(150);
        //skin.removeButton(ButtonType.OK);
        //skin.addButtonTypes(ButtonType.CANCEL);
    }

}
