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
package de.lb.cpx.client.app.wm.fx.process.section.operations;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.dialog.AddRequestDialog;
import de.lb.cpx.client.app.wm.fx.dialog.UpdateRequestDialog;
import de.lb.cpx.client.app.wm.fx.process.listview.entries.subject.EventSubject;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmRequest;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.ButtonType;
import org.controlsfx.glyphfont.FontAwesome;

/**
 *
 * @author niemeier
 */
public class WmRequestOperations extends WmOperations<TWmRequest> {

    private static final Logger LOG = Logger.getLogger(WmRequestOperations.class.getName());

    public WmRequestOperations(ProcessServiceFacade pFacade) {
        super(pFacade);
    }

    @Override
    public ItemEventHandler editItem(final TWmRequest pItem) {
        if (pItem == null) {
            return null;
        }
        return new ItemEventHandler(FontAwesome.Glyph.PENCIL, Lang.getEventOperationEdit(), FontAwesome.Glyph.EXCHANGE, Lang.getEventOperationEditItem(getItemName()), true) {
            @Override
            public void handle(ActionEvent evt) {
                UpdateRequestDialog dialog = createUpdateRequestDialog(facade, pItem);
                if (dialog == null) {
                    return;
                }
                dialog.showAndWait();
            }
        };
    }

    @Override
    public ItemEventHandler createItem() {
        Long hCaseId = facade.getCurrentProcess().getMainProcessCase() == null || facade.getCurrentProcess().getMainProcessCase().getHosCase() == null ? null : facade.getCurrentProcess().getMainProcessCase().getHosCase().getId();
        return new ItemEventHandler(FontAwesome.Glyph.FILE, Lang.getEventOperationCreate(), FontAwesome.Glyph.FILE, Lang.getEventOperationCreateItem(getItemName()), false) {
            @Override
            public void handle(ActionEvent evt) {
                if (hCaseId == null || hCaseId == 0) {
                    LOG.log(Level.SEVERE, "MainCase of the process is null or 0");
                    MainApp.showErrorMessageDialog(Lang.getProcessMainCaseError());
                    return;
//            AlertDialog alert = AlertDialog.createErrorDialog("Message", ButtonType.OK, ButtonType.CANCEL);
//            alert.setTitle(Lang.getTemplateErrorTitle());
//            alert.setHeaderText(Lang.getTemplateErrorHeader());
//            alert.setContentText(Lang.getProcessMainCaseError());
//            Optional<ButtonType> buttons = alert.showAndWait();
//            if (buttons.get() == ButtonType.OK) {
////                AddAvailableCaseDialog dialog = new AddAvailableCaseDialog(facade);
////                dialog.showAndWait();
//            } else {
//            }
                }
                AddRequestDialog dialog = null;
                try {
                    dialog = new AddRequestDialog(facade, hCaseId); // If the mainCase of the process is null, then it throws null pointer exception.
                } catch (CpxIllegalArgumentException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                    MainApp.showErrorMessageDialog(ex);
                    return;
                }
                dialog.resultProperty().addListener(new ChangeListener<ButtonType>() {
                    @Override
                    public void changed(ObservableValue<? extends ButtonType> ov, ButtonType t, ButtonType t1) {
                        if(ButtonType.OK.equals(t1)){
                            facade.loadProcess(facade.getCurrentProcess().id, true);
                        }
                    }
                });
                /*
                        .ifPresent(new Consumer<ButtonType>() {
                    @Override
                    public void accept(ButtonType t) {
                        //20191016 AWi: only reload process when user has created new request
                        //is it really neccessary to reload process??
                        if (ButtonType.OK.equals(t)) {
                            //2018-06-21 DNi - Ticket CPX-1048: Solution for this error: 
                            //java.sql.BatchUpdateException: ORA-02292: Integritäts-Constraint (CASEDB.FK_WM_EVENT4REQUEST_ID) verletzt - untergeordneter Datensatz gefunden
                            facade.loadProcess(facade.getCurrentProcess().id, true);
                        }
                    }
                });
                */
                dialog.show();
            }
        };
    }

//    @Override
//    protected void saveItem(final TWmReminder pItem) {
//        throw new UnsupportedOperationException("reminders cannot be stored!");
//    }
    @Override
    public ItemEventHandler removeItem(final TWmRequest pItem) {
        if (pItem == null) {
            return null;
        }
        return new ItemEventHandler(FontAwesome.Glyph.TRASH, Lang.getEventOperationRemove(), FontAwesome.Glyph.TRASH, Lang.getEventOperationRemoveItem(getItemName()), false) {
            @Override
            public void handle(ActionEvent evt) {
                AlertDialog dlg = AlertDialog.createConfirmationDialog("Möchten Sie die Anfrage wirklich löschen?");
                dlg.initOwner(MainApp.getWindow());
                dlg.showAndWait().ifPresent(new Consumer<ButtonType>() {
                    @Override
                    public void accept(ButtonType t) {
                        if (t.equals(ButtonType.OK)) {
                            facade.removeRequest(pItem);
//                                    LOG.log(Level.INFO, "Delete Request");
//                                    pHistoryListView.reload();
                        }
                    }
                });
            }
        };
    }

    private UpdateRequestDialog createUpdateRequestDialog(ProcessServiceFacade pFacade, TWmRequest pRequest) {
        if (pRequest == null) {
            LOG.log(Level.WARNING, "passed request object is null!");
            return null;
        }
        UpdateRequestDialog dialog = new UpdateRequestDialog(pFacade, pRequest, getUpdateRequestDialogTitle(pRequest));
        dialog.clearControls();
        dialog.addControls(dialog.setUpLayoutForRequestType(pRequest.getRequestTypeEnum()));
        return dialog;
    }

    private String getUpdateRequestDialogTitle(TWmRequest pRequest) {
        if (pRequest == null || pRequest.getRequestTypeEnum() == null) {
            return EventSubject.PLACEHOLDER; //"----";
        }
        switch (pRequest.getRequestTypeEnum()) {
            case bege:
                return Lang.getBegeUpdateRequestWindow();
            case mdk: // MDK request
                return Lang.getMdkUpdateRequestWindow();
            case audit: //  Audit request
                return Lang.getAuditUpdateRequestWindow();
            case insurance: //  insurance request
                return "Anfrage aktualisieren (Versicherung)";
            case other: // other request
                return "Anfrage aktualisieren (Sonstige)";
            case review:
                return Lang.getReviewUpdateRequestWindow();
            default:
                return EventSubject.PLACEHOLDER; //"----";
        }
    }
    @Override
    public String getItemName() {
        return Lang.getEventNameRequest();
    }

}
