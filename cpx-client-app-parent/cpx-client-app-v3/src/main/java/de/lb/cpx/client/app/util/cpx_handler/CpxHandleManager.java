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
package de.lb.cpx.client.app.util.cpx_handler;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.menu.fx.filter.tableview.FilterTableView;
import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.client.core.util.cpx_handler.BasicCpxHandleManager;
import de.lb.cpx.client.core.util.cpx_handler.CpxHandleMessage;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.enums.SearchListTypeEn;
import de.lb.cpx.service.ejb.ProcessServiceBeanRemote;
import de.lb.cpx.service.ejb.SingleCaseEJBRemote;
import de.lb.cpx.service.properties.SearchListProperties;
import de.lb.cpx.service.searchlist.SearchListResult;
import de.lb.cpx.wm.model.TWmProcess;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.controlsfx.glyphfont.FontAwesome;

/**
 *
 * @author niemeier
 */
public class CpxHandleManager extends BasicCpxHandleManager {

    private static final Logger LOG = Logger.getLogger(CpxHandleManager.class.getName());

    @Override
    protected void performProtocolCaseHandler(final CpxHandleMessage pMessage) {
        final String[] caseData = pMessage.getCaseDataFromProtocol();
        final String hospital = caseData[0];
        final String caseNumber = caseData[1];
        EjbProxy<SingleCaseEJBRemote> singleCaseEjb = Session.instance().getEjbConnector().connectSingleCaseBean();
        if (hospital.isEmpty()) {
            final List<TCase> cases = singleCaseEjb.get().findCasesForCaseNumber(caseNumber);

            if (cases.isEmpty()) {
                showHandleError("Der Fall " + hospital + "/" + caseNumber + " existiert in der Datenbank " + Session.instance().getCpxDatabase() + " nicht");
                return;
            }

            if (cases.size() == 1) {
                showCaseOpenDialog(cases.get(0));
                return;
            }

            showMultipleSelectionDialog(cases, null);
        } else {
            TCase cs = singleCaseEjb.get().findSingleCaseForCaseNumberAndIdent(caseNumber, hospital);

            if (cs == null) {
                showHandleError("Der Fall " + hospital + "/" + caseNumber + " existiert in der Datenbank " + Session.instance().getCpxDatabase() + " nicht");
                return;
            }

            showCaseOpenDialog(cs);
        }
    }

    protected void showCaseOpenDialog(final TCase pCase) {
        showHandleConfirmation("Möchten Sie den Fall " + pCase.getCsCaseNumber() + " mit der Krankenhaus-IK " + pCase.getCsHospitalIdent() + " wirklich aufrufen?", new Callback<Void, Void>() {
            @Override
            public Void call(Void param) {
                MainApp.getToolbarMenuScene().openCase(pCase.id);
                return null;
            }
        });
    }

    @Override
    protected void performProtocolWorkflowHandler(final CpxHandleMessage pMessage) {
        final String[] workflowData = pMessage.getWorkflowDataFromProtocol();
        final String workflowNumber = workflowData[0];
        EjbProxy<ProcessServiceBeanRemote> processServiceBean = Session.instance().getEjbConnector().connectProcessServiceBean();
        TWmProcess pc = processServiceBean.get().findSingleProcessForWorkflowNumber(Long.valueOf(workflowNumber));

        if (pc == null) {
            showHandleError("Der Vorgang " + workflowNumber + " existiert in der Datenbank " + Session.instance().getCpxDatabase() + " nicht");
            return;
        }

        showWorkflowOpenDialog(pc);
    }

    protected void showWorkflowOpenDialog(final TWmProcess pProcess) {
        showHandleConfirmation("Möchten Sie den Vorgang " + String.valueOf(pProcess.getWorkflowNumber()) + " wirklich aufrufen?", new Callback<Void, Void>() {
            @Override
            public Void call(Void param) {
                MainApp.getToolbarMenuScene().openProcess(pProcess.id);
                return null;
            }
        });
    }

    @Override
    protected boolean performProtocolUnknownHandler(final CpxHandleMessage pMessage) {
        EjbProxy<SingleCaseEJBRemote> singleCaseEjb = Session.instance().getEjbConnector().connectSingleCaseBean();
        EjbProxy<ProcessServiceBeanRemote> processServiceBean = Session.instance().getEjbConnector().connectProcessServiceBean();
        final List<TCase> cases = singleCaseEjb.get().findCasesForCaseNumber(pMessage.message);
        TWmProcess process = null;
        try {
            process = processServiceBean.get().findSingleProcessForWorkflowNumber(Long.parseLong(pMessage.message));
        } catch (NumberFormatException ex) {
            LOG.log(Level.FINEST, "Cannot handle value '" + pMessage.message + "' as long, so this could not be a workflow number", ex);
        }

        if (cases.isEmpty() && process == null) {
            //nothing was found
            return false;
        }

        if (cases.size() == 1 && process == null) {
            showCaseOpenDialog(cases.get(0));
            return true;
        }
        if (cases.isEmpty() && process != null) {
            showWorkflowOpenDialog(process);
            return true;
        }

        //final TWmProcess pc = process;
        showMultipleSelectionDialog(cases, process);

        //result is ambigious (multiple cases or case/process combination was found)
        return true;
    }

    protected void showMultipleSelectionDialog(final List<TCase> pCases, final TWmProcess pProcess) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                VBox box = new VBox();
                box.setAlignment(Pos.CENTER);
                box.setSpacing(5d);
                AlertDialog alertDialog = AlertDialog.createConfirmationDialog("Was möchten Sie tun?");
                alertDialog.getButtonTypes().remove(ButtonType.OK);
                for (TCase cs : pCases) {
                    Button btn = new Button("Fall " + cs.getCsHospitalIdent() + "/" + cs.getCsCaseNumber() + " öffnen");
                    btn.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.FILE));
                    btn.addEventHandler(ActionEvent.ACTION, (event) -> {
                        MainApp.getToolbarMenuScene().openCase(cs.id);
                        alertDialog.close();
                    });
                    box.getChildren().add(btn);
                }
                if (pProcess != null) {
                    Button btn = new Button("Vorgang " + pProcess.getWorkflowNumber() + " öffnen");
                    btn.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.FOLDER));
                    btn.addEventHandler(ActionEvent.ACTION, (event) -> {
                        MainApp.getToolbarMenuScene().openProcess(pProcess.id);
                        alertDialog.close();
                    });
                    box.getChildren().add(btn);
                }
                alertDialog.getDialogPane().setContent(box);
                bringDialogToFront(alertDialog);
            }
        });
    }

    @Override
    protected void importFilter(final SearchListResult pExistingSearchList, final SearchListProperties pSearchList) {
        //BasicMainApp.bringToFront();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                VBox box = new VBox();
                box.setAlignment(Pos.CENTER);
                box.setSpacing(5d);

                if (pExistingSearchList != null && !pExistingSearchList.isWriteable(Session.instance().getCpxUserId())) {
                    showHandleError("Sie haben den Filter nicht erstellt und können ihn daher nicht überschreiben!");
                    return;
                }

                AlertDialog alertDialog = AlertDialog.createConfirmationDialog("Was möchten Sie tun?");
                //alertDialog.getButtonTypes().remove(ButtonType.OK);
                box.getChildren().add(new Label("Soll die " + pSearchList.getList().getTranslation().getValue() + " '" + pSearchList.getName() + "' importiert und geöffnet werden?"
                        + (pExistingSearchList != null ? "\n\nDie bereits vorhandene Liste '" + pExistingSearchList.getName() + "' wird ersetzt!" : "")));
                alertDialog.getDialogPane().setContent(box);
                alertDialog.initOwner(BasicMainApp.getStage());
                BasicMainApp.centerWindow(alertDialog.getDialogPane().getScene().getWindow());
//                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
//                double CENTER_ON_SCREEN_X_FRACTION = 1.0f / 2;
//                double CENTER_ON_SCREEN_Y_FRACTION = 1.0f / 3;
//                double centerX = screenBounds.getMinX() + (screenBounds.getWidth() - alertDialog.getDialogPane().getWidth()) * CENTER_ON_SCREEN_X_FRACTION;
//                //centerY will always set the stage a little higher than the center. 
//                double centerY = screenBounds.getMinY() + (screenBounds.getHeight() - alertDialog.getDialogPane().getHeight()) * CENTER_ON_SCREEN_Y_FRACTION;
//                alertDialog.getDialogPane().getScene().getWindow().setX(centerX);
//                alertDialog.getDialogPane().getScene().getWindow().setY(centerY);
//                bringDialogToFront(alertDialog);
//alertDialog.getDialogPane().requestFocus();
                Optional<ButtonType> result = alertDialog.showAndWait();
                if (result.isPresent()) {
                    if (result.get() == ButtonType.OK) {
                        //showHandleMessage("Noch nicht implementiert!");
//                        CSearchList searchList = new CSearchList();
//                        searchList.setCreationDate(new Date());
//                        searchList.setCreationUser(Session.instance().getCpxUserId());
//                        searchList.setSlName(pSearchList.getName());
//                        searchList.setSlType(pSearchList.getList());
                        //searchList.setSlVisibleToUserIds(new Long[0]);
                        //searchList.setSlVisibleToRoleIds(new Long[0]);
                        SearchListResult searchListResult = new SearchListResult(Session.instance().getCpxUserId(), pSearchList.getName(), pSearchList.getList());
                        if (pExistingSearchList != null) {
                            searchListResult.getSearchList().setId(pExistingSearchList.getId());
                        }
                        searchListResult.setSearchListProperties(pSearchList);
                        MenuCache.getMenuCacheSearchLists().saveSearchList(searchListResult, true);
                        if (SearchListTypeEn.WORKFLOW.equals(pSearchList.getList())) {
                            if (MainApp.getToolbarMenuScene().getWorkflowList() == null) {
                                try {
                                    //Workflow List has not been initialized yet
                                    MainApp.getToolbarMenuScene().initWorkflowList();
                                } catch (IOException ex) {
                                    LOG.log(Level.SEVERE, "Was not able to initialize workflow list", ex);
                                    showHandleError("Die Vorgangsliste konnte nicht initialisiert werden", ex);
                                    return;
                                }
                            }
                            MainApp.getToolbarMenuScene().showWorkflowList();
                            MainApp.getToolbarMenuScene()
                                    .getWorkflowList().getProperties()
                                    .put(FilterTableView.ADD_SEARCH_ITEM, searchListResult);
                        } else {
                            MainApp.getToolbarMenuScene().showWorkingList();
                            MainApp.getToolbarMenuScene()
                                    .getWorkingList().getProperties()
                                    .put(FilterTableView.ADD_SEARCH_ITEM, searchListResult);
                        }
                        MainApp.bringToFront();

//                        try {
//                            if (SearchListTypeEn.WORKFLOW.equals(sl.getList())) {
//                                BasicMainApp.getToolbarMenuScene().getController().reloadWorkflowList();
//                            } else {
//                                BasicMainApp.getToolbarMenuScene().getController().reloadWorkingList();
//                            }
//                        } catch (IOException | CpxIllegalArgumentException ex) {
//                            LOG.log(Level.SEVERE, null, ex);
//                        }
                    }
                }
            }
        });
    }

}
