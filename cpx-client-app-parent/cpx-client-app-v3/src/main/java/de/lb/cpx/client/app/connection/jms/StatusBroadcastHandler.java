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
package de.lb.cpx.client.app.connection.jms;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.menu.fx.filterlists.cases.WorkingListScene;
import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.connection.jms.BasicStatusBroadcastHandler;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.model.fx.alert.NotificationsFactory;
import de.lb.cpx.shared.dto.broadcast.BroadcastOriginEn;
import de.lb.cpx.shared.dto.broadcast.StatusBroadcastDTO;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.menucache.MenuCacheEntryEn;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.util.Callback;
import javafx.util.Duration;
import javax.batch.runtime.BatchStatus;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.Notifications;

/**
 *
 * @author wilde
 */
public class StatusBroadcastHandler extends BasicStatusBroadcastHandler<Serializable> {

    private static final Logger LOG = Logger.getLogger(StatusBroadcastHandler.class.getName());

    public StatusBroadcastHandler() {
        super();
    }

    @Override
    public String getFilter() {
        final String filter = super.getFilter();
        return "(" + filter + ") AND (Origin <> '" + BroadcastOriginEn.CASE_TO_COMMON.name() + "')";
    }

    @Override
    protected Callback<StatusBroadcastDTO<Serializable>, Void> createDtoListener() {
        return new Callback<StatusBroadcastDTO<Serializable>, Void>() {
            @Override
            public Void call(StatusBroadcastDTO<Serializable> dto) {
                if (null == dto.getOrigin()) {
                    showNotification(dto, dto.getComment());
                } else {
                    switch (dto.getOrigin()) {
                        case BATCHGROUPING:
                            batchgroupingStatus(dto);
                            break;
                        case CORE_DATA:
                            updateCoreData(dto);
                            break;
                        case DELETE_CASE:
                            deleteCase(dto);
                            break;
                        case DELETE_PROCESS:
                            deleteProcess(dto);
                            break;
                        case ADD_CASE:
                            addCase(dto);
                            break;
                        case ADD_PROCESS:
                            addProcess(dto);
                            break;
                        default:
                            showNotification(dto, dto.getComment());
                            break;
                    }
                }
                return null;
            }
        };
    }

    private void batchgroupingStatus(StatusBroadcastDTO<Serializable> dto) {
        final Notifications not;
        if (BatchStatus.COMPLETED.equals(dto.getBatchStatus())) {
            not = NotificationsFactory.instance().createSuccessNotification();
        } else if (BatchStatus.FAILED.equals(dto.getBatchStatus())) {
            not = NotificationsFactory.instance().createErrorNotification();
        } else {
            not = NotificationsFactory.instance().createInformationNotification();
        }
        not.title(dto.getComment());
        not.text("Datenbank: " + dto.getDatabase() + "\nAnwender: " + dto.getUserName());
        if (BatchStatus.COMPLETED.equals(dto.getBatchStatus())) {
            //does it make sense to offer this action listener when user is not on the same database?!
            not.onAction((t) -> {
                MainApp.getToolbarMenuScene().showWorkingList();
                WorkingListScene workingListScene = MainApp.getToolbarMenuScene().getWorkingList();
                if (workingListScene != null) {
                    workingListScene.reload();
                }
            });
        }
        not.show();
    }

    private void deleteCase(StatusBroadcastDTO<Serializable> dto) {
        Session.instance().resetCaseCount();
        showNotification(dto, dto.getComment());
    }

    private void deleteProcess(StatusBroadcastDTO<Serializable> dto) {
        Session.instance().resetProcessCount();
        showNotification(dto, dto.getComment());
    }

    private void addCase(StatusBroadcastDTO<Serializable> dto) {
        Session.instance().resetCaseCount();
        showNotification(dto, dto.getComment());
    }

    private void addProcess(StatusBroadcastDTO<Serializable> dto) {
        Session.instance().resetProcessCount();
        showNotification(dto, dto.getComment());
    }

    private void updateCoreData(StatusBroadcastDTO<Serializable> dto) {
        MenuCacheEntryEn cacheEntry = null;
        String command = null;
        String[] ids = null;

        final String[] tmp = StringUtils.trimToEmpty(dto.getComment()).split(";");
        if (tmp.length == 0) {
            LOG.log(Level.SEVERE, "I don''t know how to handle this comment: {0}", dto.getComment());
            return;
        } else {
            cacheEntry = MenuCacheEntryEn.valueOf(tmp[0]);
            if (tmp.length >= 2) {
                command = StringUtils.trimToEmpty(tmp[1]);
            }
            if (tmp.length >= 3) {
                ids = StringUtils.trimToEmpty(tmp[2]).split(",");
//                Set<Long> values = new TreeSet<>();
//                String s = StringUtils.trimToEmpty(tmp[2]);
//                String[] t = s.split(",");
//                for (String v : t) {
//                    v = StringUtils.trimToEmpty(tmp[2]);
//                    if (v.isEmpty()) {
//                        continue;
//                    }
//                    try {
//                        final Long val = Long.parseLong(v);
//                        values.add(val);
//                    } catch (NumberFormatException ex) {
//                        LOG.log(Level.WARNING, "cannot parse this as a long value: {0}", v);
//                        LOG.log(Level.FINEST, "illegal long value: {0}", ex);
//                    }
//                }
//                ids = new Long[values.size()];
//                values.toArray(ids);
            }
        }
        if (cacheEntry == null) {
            LOG.warning("Server send message to update Cache, but Cache is unknown: " + dto.getComment());
        } else {
            if(isCatalog(cacheEntry)){
                Node content = NotificationsFactory.instance().createupdateCatalogNotificationLayout(); //create as content to be able to hide notification
                Notifications notif = NotificationsFactory.instance().createInformationNotification()
                        .owner(BasicMainApp.getWindow())
                        .onAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent t) {
                                content.getScene().getWindow().hide(); // due to lack of hide methode try to hide parent window - notification has no other option to access window 
                                //show Settings
                                MainApp.getToolbarMenuScene().showSettingsDialog(Lang.getCatalogDownloadCatalogs());
                            }
                        })
                        .graphic(content) 
                        .title("CPX: " + Lang.getCatalogMenuUpdate())
                        .text(" ")//necessary workaround - without or empty text position of the notification will be faulty!
                        .hideAfter(Duration.seconds(10));
                        notif.show();
                //no need to go further? catalog 'cacheEntries' could never be initialized
                return;
            }
            if (ids == null) {
                int size = MenuCache.size(cacheEntry);
                if (MenuCache.uninitialize(cacheEntry)) {
                    LOG.log(Level.FINE, "Uninitialize MenuCache for {0}: {1} entries freed from memory", new Object[]{cacheEntry.name(), size});
                }
            } else {
                if (command == null || command.trim().isEmpty()) {
                    LOG.log(Level.WARNING, "I don't know to handle ids {0}, command is null or empty!", ids);
                } else {
                    if (command.equalsIgnoreCase("delete")) {
                        MenuCache.get(cacheEntry).remove(ids);
                    } else if (command.equalsIgnoreCase("update") || command.equalsIgnoreCase("insert")) {
                        MenuCache.get(cacheEntry).refresh(ids);
                    } else {
                        LOG.log(Level.WARNING, "unknown command: {0}", command);
                    }
                }
            }
        }
    }

    private boolean isCatalog(MenuCacheEntryEn cacheEntry) {
        return MenuCacheEntryEn.MD_MASTERDATA.equals(cacheEntry)|| MenuCacheEntryEn.BASERATES.equals(cacheEntry) || MenuCacheEntryEn.DRG.equals(cacheEntry) || MenuCacheEntryEn.PEPP.equals(cacheEntry);
    }
}
