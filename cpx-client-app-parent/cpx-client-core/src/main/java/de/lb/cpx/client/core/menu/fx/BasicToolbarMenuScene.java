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
package de.lb.cpx.client.core.menu.fx;

import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.settings.SettingsDialog;
import de.lb.cpx.client.core.util.CpxFXMLLoader;
import de.lb.cpx.client.core.util.shortcut.ShortcutHandler;
import de.lb.cpx.client.core.util.shortcut.ShortcutsRedirector;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 *
 * @author wilde
 */
public abstract class BasicToolbarMenuScene extends CpxScene implements ShortcutsRedirector {

    private static final Logger LOG = Logger.getLogger(BasicToolbarMenuScene.class.getName());

//    private final ObjectProperty<Boolean> showMenuProperty = new SimpleObjectProperty<>(null);
    private final ReadOnlyObjectWrapper<CpxScene> displayedSceneProperty = new ReadOnlyObjectWrapper<>(null);

    protected final ReadOnlyObjectProperty<CpxScene> getDisplayedSceneProperty() {
        return displayedSceneProperty.getReadOnlyProperty();
    }

    protected void setDisplayedSceneProperty(final CpxScene pScene) {
        displayedSceneProperty.setValue(pScene);
    }

    public CpxScene getDisplayedScene() {
        return displayedSceneProperty.get();
    }

    public BasicToolbarMenuScene() throws IOException, CpxIllegalArgumentException {
        super(CpxFXMLLoader.getLoader(ToolbarMenuFXMLController.class));
//        getStylesheets().add("/styles/cpx-default.css");
        getDisplayedSceneProperty().addListener(new ChangeListener<CpxScene>() {
            @Override
            public void changed(ObservableValue<? extends CpxScene> observable, CpxScene oldValue, CpxScene newValue) {
                if (newValue == null) {
                    return;
                }
                if (newValue.getSceneTitle() == null) {
                    LOG.log(Level.FINEST, "remove scene title, use default title instead");
                } else {
                    LOG.log(Level.FINEST, "change scene title to: " + newValue.getSceneTitle());
                }
                setSceneTitle(newValue.getSceneTitle());
            }
        });
//        getController().showWorkingList();

        getController().setShowMenu(CpxClientConfig.instance().getExtendToolbar());

        /*      setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ALT || event.getCode() == KeyCode.ALT_GRAPH) {
                    if (getController().getShowMenuProperty().getValue()) {
                        getController().hideMenu();
                    } else {
                        getController().showMenu();
                    }
                }
            }
        });     */
    }

    @Override
    public final ToolbarMenuFXMLController getController() {
        return (ToolbarMenuFXMLController) super.getController();
    }

    @Override
    public ShortcutHandler getShortcuts() {
//        ToolbarMenuFXMLController ctrl = getController();
//        if (ctrl.isInWorkingList()) {
//            CaseList caseList = ctrl.getWorkingList().getController().getCaseList();
//            if (caseList != null) {
//                return caseList;
//            }
//            RuleList ruleList = ctrl.getWorkingList().getController().getRuleList();
//            if (ruleList != null) {
//                return ruleList;
//            }
//        }
//        if (ctrl.isInWorkflowList()) {
//            return ctrl.getWorkflowList().getController().getProcessList();
//        }
        return null;
    }

    //sets the display scene
    protected boolean displayScene(CpxScene pScene) {
        if (pScene == null) {
            HBox box = new HBox(new Label("Keine Ansicht ausgewählt!"));
            box.setAlignment(Pos.CENTER);
            getController().setContent(box);
            return true;
        }
        if (displayedSceneProperty.getValue() != null && displayedSceneProperty.getValue().equals(pScene)) {
            return false;
        }
        displayedSceneProperty.setValue(pScene);
        getController().setContent((Pane) pScene.getRoot());
        return true;
    }

    @Override
    public boolean close() {
        LOG.info("close MenuFrame");

//        ObservableList<OpenEntryScene<CpxScene, TCase>> cases = FXCollections.observableArrayList(lvOpenCases.getItems());
//        for (OpenEntryScene entry : cases) {
//            if (!entry.close()) {
//                return false;
//            }
//        }
//        ObservableList<OpenEntryScene<WmMainFrameScene, TWmProcess>> processes = FXCollections.observableArrayList(lvOpenProcesses.getItems());
//        for (OpenEntryScene entry : processes) {
//            if (!entry.close()) {
//                return false;
//            }
//        }
        if (displayedSceneProperty.getValue() != null) {
            boolean result = false;
            try {
                result = displayedSceneProperty.getValue().close();
            } catch (IllegalStateException ex) {
                LOG.log(Level.SEVERE, "Was not able to close scene properly (maybe the connection to server has gone!)", ex);
            }
            return result;
        }
        //clear menu cache if closed
        MenuCache.destroy();
        return true;
    }

    @Override
    public void reload() {
        if (displayedSceneProperty.getValue() != null) {
            displayedSceneProperty.getValue().getController().reload();
        }
    }

    //check if scene is already displayed
    protected boolean isSceneAlreadyShown(final Class<? extends CpxScene> pScene) {
        return displayedSceneProperty.get() != null && pScene.equals(displayedSceneProperty.get().getClass());
    }

    public void cleanUp() {
        if (displayedSceneProperty.get() != null) {
            displayedSceneProperty.get().close();
            displayedSceneProperty.set(null);
        }
        getController().getSceneStack().getChildren().clear();
//        getController().clearSceneStack();
//        removeWorkingList();
//        removeWorkflowList();
//        removeDocumentImport();
    }

    public SettingsDialog getSettingsDialog() {
        return new SettingsDialog(BasicMainApp.getWindow());
    }
    
    public void getHelpDialog() {
        BasicMainApp.showErrorMessageDialog("Not yet Implemented");
    }

    public int closeAllCases(long pCaseId) {
        LOG.log(Level.WARNING, "closing cases is not supported in core version!");
        return 0;
        //throw new UnsupportedOperationException("closing cases is not supported in core version!");
        //BasicMainApp.getToolbarMenuScene().getController().closeAllCases(lockDto.caseId);
    }

    public boolean closeProcess(long pProcessId) {
        LOG.log(Level.WARNING, "closing process is not supported in core version!");
        return false;
        //throw new UnsupportedOperationException("closing process is not supported in core version!");
        //BasicMainApp.getToolbarMenuScene().getController().closeProcess(lockDto.processId);
    }

    public boolean isProcessCaseOpen(Long pCaseId) {
        LOG.log(Level.WARNING, "is process case open is not supported in core version!");
        return false;
        //throw new UnsupportedOperationException("is process case open is not supported in core version!");
    }

    public boolean isCaseOpen(Long pCaseId) {
        LOG.log(Level.WARNING, "is case open is not supported in core version!");
        return false;
        //throw new UnsupportedOperationException("is case open is not supported in core version!");
    }

    public boolean isProcessOpen(Long pProcessId) {
        LOG.log(Level.WARNING, "is process open is not supported in core version!");
        return false;
        //throw new UnsupportedOperationException("is process open is not supported in core version!");
    }

    public void restoreRecentClientScene() {
        LOG.log(Level.WARNING, "restore recent client scene is not supported in core version!");
        MainApp.setStageTitle(null);
        //throw new UnsupportedOperationException("restore recent client scene is not supported in core version!");
    }
    
    public Optional<ButtonType> showSettingsDialog(String pCategory){
        SettingsDialog settings = getSettingsDialog();
        settings.selectCategory(pCategory);
        return settings.showAndWait();
    }
    public Optional<ButtonType> showSettingsDialog(){
        return showSettingsDialog(null);
    } 
}
