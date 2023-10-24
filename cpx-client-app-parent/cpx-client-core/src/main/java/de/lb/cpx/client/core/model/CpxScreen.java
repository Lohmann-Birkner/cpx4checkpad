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
package de.lb.cpx.client.core.model;

import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Paint;

/**
 * Implementation of the screen, screen is definied as an subscene of an parent
 * scene
 *
 * @author wilde
 */
public class CpxScreen extends Scene {

    @SuppressWarnings("rawtypes")
    protected final Controller controller;
    private URL resource;

    /**
     * basic JavaFx Constructor call on Scene Not recommended
     *
     * @param root root object of the scene
     */
    public CpxScreen(Parent root) {
        super(root);
        controller = null;
    }

    /**
     * basic JavaFx Constructor call on Scene Not recommended Controller will
     * not be set in Scene
     *
     * @param root root object of the scene
     * @param fill background color of the scene
     */
    public CpxScreen(Parent root, Paint fill) {
        super(root, fill);
        controller = null;
    }

    /**
     * basic JavaFx Constructor call on Scene Not recommended Controller will
     * not be set in Scene
     *
     * @param root root object of the scene
     * @param width width of the new scene
     * @param height height of the new scene
     */
    public CpxScreen(Parent root, double width, double height) {
        super(root, width, height);
        controller = null;
    }

    /**
     * basic JavaFx Constructor call on Scene Not recommended Controller will
     * not be set in Scene
     *
     * @param root root object of the scene
     * @param width width of the new scene
     * @param height height of the new scene
     * @param fill background color of the scene
     */
    public CpxScreen(Parent root, double width, double height, Paint fill) {
        super(root, width, height, fill);
        controller = null;
    }

    /**
     * basic JavaFx Constructor call on Scene Not recommended Controller will
     * not be set in Scene
     *
     * @param root root object of the scene
     * @param width width of the new scene
     * @param height height of the new scene
     * @param depthBuffer indicator for depthbuffer
     */
    public CpxScreen(Parent root, double width, double height, boolean depthBuffer) {
        super(root, width, height, depthBuffer);
        controller = null;
    }

    /**
     * basic JavaFx Constructor call on Scene Not recommended Controller will
     * not be set in Scene
     *
     * @param root root object of the scene
     * @param width width of the new scene
     * @param height height of the new scene
     * @param depthBuffer indicator for depthbuffer
     * @param antiAliasing indicator for anitaliasing
     */
    public CpxScreen(Parent root, double width, double height, boolean depthBuffer, SceneAntialiasing antiAliasing) {
        super(root, width, height, depthBuffer, antiAliasing);
        controller = null;
    }

    /**
     * creates a new Scene with the root element, the controller and the
     * resource
     *
     * @param pRoot root object of the scene
     * @param pController controller of the scene
     * @param pResource url of the resource that was loaded (fmxl)
     */
    @SuppressWarnings("unchecked")
    public CpxScreen(Parent pRoot, Controller<?> pController, URL pResource) {
        super(pRoot);
        this.controller = pController;
        this.resource = pResource;
        controller.setScene(this);
    }

    /**
     * creates a new Scene with the fxml loader class sets root, controller and
     * resource from loader
     *
     * @param pLoader FXMLLoader class that supports loading process
     */
    @SuppressWarnings("unchecked")
    public CpxScreen(FXMLLoader pLoader) {
        super(pLoader.getRoot());
        this.controller = pLoader.getController();
        this.controller.setScene(this);
        this.resource = pLoader.getLocation();
    }

    /**
     * get the loaded resource
     *
     * @return url of the resource
     */
    public URL getResource() {
        return resource;
    }

    /**
     * get the Controller class from the Scene
     *
     * @return controller Class
     */
    public Controller<?> getController() {
        return controller;
    }

    /**
     * forces the scene to reload (load new values)
     */
    public void reload() {
        controller.reload();
    }

    /**
     * forces scene to refresh (only update its values)
     */
    public void refresh() {
        controller.refresh();
    }

    /**
     * close the screen
     *
     * @return indicator if close was successful
     */
    public boolean close() {
        return controller.close();
    }
}
