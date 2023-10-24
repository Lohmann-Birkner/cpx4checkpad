/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.ruleeditor.menu;

import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.fx.BasicToolbarMenuScene;
import de.lb.cpx.client.core.menu.fx.openedEntry.OpenEntry;
import de.lb.cpx.client.core.menu.model.ToolbarListMenuItem;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.settings.SettingsDialog;
import de.lb.cpx.client.ruleeditor.events.OpenRuleEvent;
import de.lb.cpx.client.ruleeditor.events.UpdateRoleEvent;
import de.lb.cpx.client.ruleeditor.events.UpdateRuleEvent;
import de.lb.cpx.client.ruleeditor.events.UpdateRuleTypeEvent;
import de.lb.cpx.client.ruleeditor.menu.dialogs.buttontypes.RuleEditorButtonTypes;
import de.lb.cpx.client.ruleeditor.menu.settings.RuleEditorSettingsScene;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.ruleviewer.event.RuleTableChangedEvent;
import de.lb.cpx.ruleviewer.model.RuleMessageIndicator;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.server.rule.services.RuleLockBeanRemote;
import de.lb.cpx.shared.dto.LockException;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javax.naming.NamingException;
import org.controlsfx.glyphfont.FontAwesome;

/**
 * ToolbarMenuScene for the RuleEditor
 *
 * @author wilde
 */
public class ReToolbarMenuScene extends BasicToolbarMenuScene {

    private static final Logger LOG = Logger.getLogger(ReToolbarMenuScene.class.getName());

    private final ToolbarListMenuItem<RuleOpenEntry> poolMenuItem;
    private PoolOverviewScene pools;
    private RuleLockBeanRemote ruleLockBean;
    private boolean shutdown;

    public ReToolbarMenuScene() throws IOException, CpxIllegalArgumentException {
        super();
        setSceneTitle("Regelsuite");
        try {
            initBeans();
        } catch (NamingException ex) {
            Logger.getLogger(ReToolbarMenuScene.class.getName()).log(Level.SEVERE, null, ex);
        }
        //create new toolbar entries
        poolMenuItem = new ToolbarListMenuItem<>(getController().getToolbar());
        poolMenuItem.disableProperty().bind(getController().disableMenuProperty());
        poolMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showPools();
            }
        });
        poolMenuItem.setGlyph(FontAwesome.Glyph.FILE);
        poolMenuItem.setExtendedTitle("Regelpools");
        poolMenuItem.setOnRemoveCallback(OpenEntry::close);
        getController().getToolbar().add(0, poolMenuItem);

        addEventFilter(UpdateRoleEvent.updateRoleEvent(), new EventHandler<UpdateRoleEvent>() {
            @Override
            public void handle(UpdateRoleEvent event) {
                LOG.info("update Roles: " + event.getRuleIds().stream().map((t) -> {
                    return String.valueOf(t);
                }).collect(Collectors.joining(",")));

                for (RuleOpenEntry item : poolMenuItem.getItems()) {
                    if (item.getStoredScene().getPool().getId() != event.getPool().getId()) {
                        continue;
                    }
                    if (event.getRuleIds().contains(item.getStoredScene().getRule().getId())) {
                        item.getStoredScene().updateRole(event.getRole());
                    }
                }
            }
        });
        showPools();
//        setOnDragOver(new EventHandler<DragEvent>() {
//            @Override
//            public void handle(DragEvent t) {
//                t.acceptTransferModes(TransferMode.ANY);
//                LOG.info("dragover");
//            }
//        });
    }

    private void showPools() {
        if (getController().isDisableMenu()) {
            return;
        }
        if (!isSceneAlreadyShown(PoolOverviewScene.class)) {
            poolMenuItem.clearSelection();
            try {
                initPools();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "Cannot show workflow list!", ex);
            }
            displayScene(pools);
        }
        poolMenuItem.focusTitle();
    }

    @Override
    public void cleanUp() {
        shutdown = true;
        poolMenuItem.clearItems();
        super.cleanUp(); //To change body of generated methods, choose Tools | Templates.
    }
    public boolean isShutdown(){
        return shutdown;
    }
    private void showRule(OpenRuleEvent pEvent) {
        showRule(pEvent.getPool(), pEvent.getRule());
    }

    private void showRule(CrgRulePools pPool, CrgRules pRule) {
        if (getController().isDisableMenu()) {
            return;
        }
        if (!ruleListContainsEntity(pRule.getCrgrRid())) {
            try {
                RuleOpenEntry newEntry = new RuleOpenEntry(pPool, pRule);
                if (displayScene(newEntry.getStoredScene())) {
                    poolMenuItem.getItems().add(newEntry);
                    poolMenuItem.clearSelection();
                    poolMenuItem.select(newEntry);//.getSelectionModel().select(newEntry);
                }
            } catch (IOException | NamingException ex) {
                Logger.getLogger(ReToolbarMenuScene.class.getName()).log(Level.SEVERE, null, ex);
            } catch (LockException ex) {
                Logger.getLogger(ReToolbarMenuScene.class.getName()).log(Level.SEVERE, null, ex);
                MainApp.showErrorMessageDialog("Regel wird bereits von einem anderen Nutzer bearbeitet!");
            }
        } else {
            RuleOpenEntry openEntry = getOpenScene(pRule.getCrgrRid());//getOpenScene(ListType.WORKING_LIST, pId);
            if (openEntry != null) {
                //AWi-20170615:
                //call reload when scene is already openend to update content
                poolMenuItem.select(openEntry);
                openEntry.getStoredScene().refresh();
                displayScene(openEntry.getStoredScene());
            }
        }
    }

    private void initPools() throws IOException {
        if (pools == null) {
            pools = new PoolOverviewScene();
            addEventHandler(OpenRuleEvent.openRuleEvent(), new EventHandler<OpenRuleEvent>() {
                @Override
                public void handle(OpenRuleEvent t) {
                    t.consume();
                    showRule(t);
                }
            });
        }

    }

    @Override
    public SettingsDialog getSettingsDialog() {
        try {
            SettingsDialog diag = new SettingsDialog(MainApp.getWindow(), Modality.APPLICATION_MODAL, "Einstellungen", new RuleEditorSettingsScene());
            diag.getDialogPane().addEventFilter(UpdateRuleTypeEvent.updateRuleTypeEvent(), new EventHandler<UpdateRuleTypeEvent>() {
                @Override
                public void handle(UpdateRuleTypeEvent t) {
                    Event.fireEvent(pools, t);
                    if (!getDisplayedScene().equals(pools)) {
                        getDisplayedScene().refresh();
                    }
                }
            });
            return diag;
        } catch (IOException ex) {
            Logger.getLogger(ReToolbarMenuScene.class.getName()).log(Level.SEVERE, null, ex);
        }
        return super.getSettingsDialog();
    }

    private void initBeans() throws NamingException {
//        ruleEditorBean = Session.instance().getEjbConnector().connectRuleEditorBean().getWithEx();
        ruleLockBean = Session.instance().getEjbConnector().connectRuleLockBean().getWithEx();
    }
    //checks if case exists in one of the open entry scenes

    private boolean ruleListContainsEntity(String pRid) {
        return poolMenuItem.getItems().stream().anyMatch((item) -> (item.getEntity().getCrgrRid() == null ? pRid == null : item.getEntity().getCrgrRid().equals(pRid)));
    }

//    private boolean hasLock(long pPoolId, long pRuleId) {
//        if (ruleLockBean == null) {
//            return true;
//        }
//        return ruleLockBean.hasLock(pPoolId, pRuleId);
//    }
    private RuleOpenEntry getOpenScene(String pRid) {
        for (RuleOpenEntry item : poolMenuItem.getItems()) {
            if (item.getEntity().getCrgrRid().equals(pRid)) {
                return item;
            }
        }
        return null;
    }

    private class RuleOpenEntry extends OpenEntry<RuleEditorScene, CrgRules> {

        public RuleOpenEntry(CrgRulePools pPool, CrgRules pRule) throws IOException, NamingException, LockException {
            super(new RuleEditorScene(pPool, pRule));
            setTitle(pRule.getCrgrNumber());
            setErrorGraphic(new RuleMessageIndicator("Die Regel enthält ein oder mehr Fehler!"));
            setErroneous(pRule.getCrgrMessage()!=null);
            getStoredScene().unsavedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                    setUnSavedContent(t1);
                    setTitle(pRule.getCrgrNumber());
                    setErroneous(pRule.getCrgrMessage()!=null);
                }
            });
            getStoredScene().addEventFilter(UpdateRuleEvent.updateRuleEvent(), new EventHandler<UpdateRuleEvent>() {
                @Override
                public void handle(UpdateRuleEvent t) {
                    setUnSavedContent(getStoredScene().isUnsaved());
                    setTitle(pRule.getCrgrNumber());
                    setErroneous(pRule.getCrgrMessage()!=null);
                    Event.fireEvent(pools, t);
                }
            });
            getStoredScene().addEventFilter(RuleTableChangedEvent.ruleTableChangedEvent(), new EventHandler<RuleTableChangedEvent>() {
                @Override
                public void handle(RuleTableChangedEvent event) {
                    Event.fireEvent(pools, event);
                }
            });
            setDesc(pPool.getCrgplIdentifier() + " - " + pPool.getCrgplPoolYear());
            setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    long start = System.currentTimeMillis();
                    if (getController().isDisableMenu()) {
                        return;
                    }
                    if (event.getClickCount() >= 1) {
                        CpxScene displayedScene = getDisplayedSceneProperty().getValue();
                        //dirty check if scene is already displayed
                        if (displayedScene instanceof RuleEditorScene) {
                            if (((RuleEditorScene) displayedScene).getRule().getCrgrRid() == null ? getEntity().getCrgrRid() == null : ((RuleEditorScene) displayedScene).getRule().getCrgrRid().equals(getEntity().getCrgrRid())) {
                                return;
                            }
                        }
                        showRule(pPool, pRule);
                    }
                    LOG.info("show restore rule in " + (System.currentTimeMillis() - start));
                }
            });
            setOnClose(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    if (getStoredScene().isUnsaved()) {
                        AlertDialog unsavedDialog = getStoredScene().getUnSavedDialog(pRule, pPool,isShutdown());
                        unsavedDialog.showAndWait().ifPresent(new Consumer<ButtonType>() {
                            @Override
                            public void accept(ButtonType t) {
                                //cancel! do nothing
                                if (ButtonType.CANCEL.equals(t)) {
                                    return;
                                }
                                if (RuleEditorButtonTypes.DISCARD.equals(t)) {
                                    getStoredScene().discardRuleChange();
                                    closeEntry(null);
                                }
                                if (RuleEditorButtonTypes.SAVE_AND_CLOSE.equals(t) || RuleEditorButtonTypes.SAVE_ALL.equals(t)) {
                                    getStoredScene().saveRule();
                                    closeEntry(new UpdateRuleEvent(UpdateRuleEvent.updateRuleEvent(), pPool, pRule, false));
                                }
                            }
                        });
                    } else {
                        closeEntry(null);
                    }
                }

                private void closeEntry(Event pEvent) {
                    if (getStoredScene().close()) {
                        poolMenuItem.getItems().remove(RuleOpenEntry.this);
                        clearOpenScene();
                        showPools();
                        if (pEvent != null) {
                            Event.fireEvent(pools, pEvent);
                        }
                        return;
                    }
                    showCloseError();
                }
            });
        }

        @Override
        public CrgRules getEntity() {
            return getStoredScene().getRule();
        }

    }
}
