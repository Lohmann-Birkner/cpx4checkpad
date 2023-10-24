/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model;

//import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.util.clipboard.ClipboardEnabler;
import de.lb.cpx.client.core.util.shortcut.ShortcutHandler;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.controlsfx.validation.ValidationResult;

/**
 * Basic implementation of an fxml controller class handles stage and basic
 * lifecircle
 *
 * @author Dirk Niemeier
 * @param <T> Scene type
 */
public abstract class Controller<T extends Scene> implements Initializable, ShortcutHandler {

    protected static final Logger LOG = Logger.getLogger(Controller.class.getName());

    private T scene;
    private final StackPane stack = new StackPane();
    private final ObjectProperty<Pane> contentPaneProperty = new SimpleObjectProperty<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * set the scene instance to the controller
     *
     * @param pScene scene of the controller to handle some ui changes if
     * necessary
     */
    public void setScene(T pScene) {
        this.scene = pScene;
        ClipboardEnabler.installClipboardToScene(scene);
    }

    /**
     * get the scene reference of the controller
     *
     * @return scene of the controller
     */
    public T getScene() {
        return scene;
    }

    /**
     * close methode of the controller
     *
     * @return boolean as indicator if closing / cleaning up of the ressources
     * occupied by the controller are successfully unlocked
     */
    public boolean close() {
        return true;
    }

    public void afterInitialisingScene() {
    }

    public void afterInitialisingStage() {
    }

    public void beforeShow() {
    }

    public void afterShow() {
    }

    /**
     * reload values managed in the controller, should result in loading newer
     * content from the server
     */
    public void reload() {

    }

    /**
     * refreshes values managed in the controller, should not loading newer
     * content from the server
     */
    public void refresh() {

    }

    /**
     * performs grouping function, shows error if methode is not overriden with
     * implementation
     */
    public void performGroup() {
        LOG.warning("Grouping not yet implemented! do nothing!");
    }

    /**
     * clear all child elements in Pane
     *
     * @param pane target Pane taht should be cleared
     */
    public void clearPane(Pane pane) {
        if (pane.getChildren() != null) {
            pane.getChildren().clear();
        }
    }

    /**
     * set a Node in Pane, all other Nodes already exists are cleared
     *
     * @param target target Pane
     * @param node Node to be set in Pane
     */
    public void setNodeInPane(Pane target, Node node) {
        clearPane(target);
        appendNodeInPane(target, node);
    }

    /**
     * append Node in target Pane at the end of the displayed children nodes
     *
     * @param target target Pane
     * @param node node that should be appends
     * @return boolean if append was successful
     */
    public boolean appendNodeInPane(Pane target, Node node) {
        if (!target.getChildren().contains(node)) {
            return target.getChildren().add(node);
        }
        return false;
    }

    /**
     * remove specific Node from target Pane
     *
     * @param target target Pane
     * @param node node that should be removed
     * @return boolean if remove was successful
     */
    public boolean removeNodeFromPane(Pane target, Node node) {
        if (target.getChildren().contains(node)) {
            return target.getChildren().remove(node);
        }
        return false;
    }

    /**
     * sets a specific pane as a content Pane
     *
     * @param pPane pane to set as content pane
     */
    public void setContentPane(Pane pPane) {
        contentPaneProperty.setValue(pPane);
        pPane.getChildren().add(stack);
        setAnchorsToZero(stack);
//        scene.setRoot(pPane);
    }

    /**
     * get the current Pane that is set as content Pane
     *
     * @return currently set content pane, null if none is set
     */
    public Pane getContentPane() {
        return contentPaneProperty.getValue();
    }

    public boolean removeContent(Pane pPane) {
        return stack.getChildren().remove(pPane);
    }

    /**
     * set content in the content Pane
     *
     * @param pPane pane to add in the content
     */
    public void setContent(Pane pPane) {
        if (contentPaneProperty.getValue() != null) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    setAnchorsToZero(pPane);
                    if (!stack.getChildren().contains(pPane)) {
                        stack.getChildren().add(pPane);
                    } else {
                        pPane.toFront();
                    }
                }
            });
        } else {
            LOG.warning("Can't add pane to the layout! Reason: No ContentPane is set!");
        }
    }

    /**
     * get the validtion result, null by default if not overridden
     *
     * @return gets the validation result, null by default if not overridden
     */
    public ValidationResult getValidationResult() {
        return null;
    }

    private void setAnchorsToZero(Pane pPane) {
        AnchorPane.setTopAnchor(pPane, 0.0);
        AnchorPane.setRightAnchor(pPane, 0.0);
        AnchorPane.setBottomAnchor(pPane, 0.0);
        AnchorPane.setLeftAnchor(pPane, 0.0);
    }

    @Override
    public boolean shortcutF1Help(KeyEvent pEvent) {
        LOG.log(Level.FINEST, "Unhandled F1 shortcut in " + getClass().getSimpleName() + ". Override method to implement shortcut!");
        return false;
    }

    @Override
    public boolean shortcutF2New(KeyEvent pEvent) {
        LOG.log(Level.FINEST, "Unhandled F2 shortcut in " + getClass().getSimpleName() + ". Override method to implement shortcut!");
        return false;
    }

    @Override
    public boolean shortcutF3Find(KeyEvent pEvent) {
        LOG.log(Level.FINEST, "Unhandled F3 shortcut in " + getClass().getSimpleName() + ". Override method to implement shortcut!");
        return false;
    }

    @Override
    public boolean shortcutF4Close(KeyEvent pEvent) {
        LOG.log(Level.FINEST, "Unhandled F4 shortcut in " + getClass().getSimpleName() + ". Override method to implement shortcut!");
        return false;
    }

    @Override
    public boolean shortcutF5Refresh(KeyEvent pEvent) {
        LOG.log(Level.FINEST, "Unhandled F5 shortcut in " + getClass().getSimpleName() + ". Override method to implement shortcut!");
        return false;
    }

    @Override
    public boolean shortcutEnterExecute(KeyEvent pEvent) {
        LOG.log(Level.FINEST, "Unhandled ENTER shortcut in " + getClass().getSimpleName() + ". Override method to implement shortcut!");
        return false;
    }

    @Override
    public boolean shortcutControlPPrint(KeyEvent pEvent) {
        LOG.log(Level.FINEST, "Unhandled Ctrl+P shortcut in " + getClass().getSimpleName() + ". Override method to implement shortcut!");
        return false;
    }

    @Override
    public boolean shortcutControlFFind(KeyEvent pEvent) {
        //LOG.log(Level.FINEST, "Unhandled Ctrl+F shortcut in " + getClass().getSimpleName() + ". Override method to implement shortcut!");
        return shortcutF3Find(pEvent);
    }

    @Override
    public boolean shortcutControlWClose(KeyEvent pEvent) {
        //LOG.log(Level.FINEST, "Unhandled Ctrl+W shortcut in " + getClass().getSimpleName() + ". Override method to implement shortcut!");
        return shortcutF4Close(pEvent);
    }

    @Override
    public boolean shortcutControlNNew(KeyEvent pEvent) {
        //LOG.log(Level.FINEST, "Unhandled Ctrl+N shortcut in " + getClass().getSimpleName() + ". Override method to implement shortcut!");
        return shortcutF2New(pEvent);
    }

    @Override
    public boolean shortcutControlRRefresh(KeyEvent pEvent) {
        //LOG.log(Level.FINEST, "Unhandled Ctrl+R shortcut in " + getClass().getSimpleName() + ". Override method to implement shortcut!");
        return shortcutF5Refresh(pEvent);
    }

    @Override
    public boolean shortcutControlSSave(KeyEvent pEvent) {
        LOG.log(Level.FINEST, "Unhandled Ctrl+S shortcut in " + getClass().getSimpleName() + ". Override method to implement shortcut!");
        return false;
    }

    @Override
    public boolean shortcutAltLeftBack(KeyEvent pEvent) {
        LOG.log(Level.FINEST, "Unhandled Alt+Left shortcut in " + getClass().getSimpleName() + ". Override method to implement shortcut!");
        return false;
    }

    @Override
    public boolean shortcutAltRightForward(KeyEvent pEvent) {
        LOG.log(Level.FINEST, "Unhandled Alt+Right shortcut in " + getClass().getSimpleName() + ". Override method to implement shortcut!");
        return false;
    }

    @Override
    public boolean shortcutControlCCopy(KeyEvent pEvent) {
        LOG.log(Level.FINEST, "Unhandled Ctrl+C shortcut in " + getClass().getSimpleName() + ". Override method to implement shortcut!");
        return false;
    }

    @Override
    public boolean shortcutControlVPaste(KeyEvent pEvent) {
        LOG.log(Level.FINEST, "Unhandled Ctrl+V shortcut in " + getClass().getSimpleName() + ". Override method to implement shortcut!");
        return false;
    }

    @Override
    public boolean shortcutDelRemove(KeyEvent pEvent) {
        LOG.log(Level.FINEST, "Unhandled DEL shortcut in " + getClass().getSimpleName() + ". Override method to implement shortcut!");
        return false;
    }

    @Override
    public boolean shortcutShiftDelRemove(KeyEvent pEvent) {
        LOG.log(Level.FINEST, "Unhandled SHIFT+DEL shortcut in " + getClass().getSimpleName() + ". Override method to implement shortcut!");
        return false;
    }

    public StackPane getSceneStack() {
        return stack;
    }
}
