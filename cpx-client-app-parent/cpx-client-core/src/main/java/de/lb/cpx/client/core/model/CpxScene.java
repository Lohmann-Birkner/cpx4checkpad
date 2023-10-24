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
 *    2016  ??? - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model;

import de.lb.cpx.client.core.BasicMainApp;
import java.net.URL;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

/**
 * Scene base class for Cpx Application extends JavaFX scene
 *
 * @author Wilde
 */
public class CpxScene extends Scene {

    @SuppressWarnings("rawtypes")
    protected final Controller controller;
    private URL resource;
    private final StringProperty sceneTitleProperty = new SimpleStringProperty();
    private EventHandler<ActionEvent> onClose;
    private final ChangeListener<String> titleChangeListener = new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            BasicMainApp.setStageTitle(newValue);
        }
    };
    private Stage owner;
//    public ObjectProperty valuePorperty = new SimpleObjectProperty<>();

    /**
     * basic JavaFx Constructor call on Scene Not recommended
     *
     * @param root root object of the scene
     */
    public CpxScene(Parent root) {
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
    public CpxScene(Parent root, Paint fill) {
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
    public CpxScene(Parent root, double width, double height) {
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
    public CpxScene(Parent root, double width, double height, Paint fill) {
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
    public CpxScene(Parent root, double width, double height, boolean depthBuffer) {
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
    public CpxScene(Parent root, double width, double height, boolean depthBuffer, SceneAntialiasing antiAliasing) {
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
    public CpxScene(Parent pRoot, Controller<?> pController, URL pResource) {
        super(pRoot);
//        BasicMainApp.setStageTitleBinding(sceneTitleProperty);
        sceneTitleProperty.addListener(titleChangeListener);
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
    public CpxScene(FXMLLoader pLoader) {
        super(pLoader.getRoot());
//       BasicMainApp.setStageTitleBinding(sceneTitleProperty);
        sceneTitleProperty.addListener(titleChangeListener);
        this.controller = pLoader.getController();
        this.controller.setScene(this);
        this.resource = pLoader.getLocation();
//       valuePorperty.addListener(new ChangeListener() {
//           @Override
//           public void changed(ObservableValue observable, Object oldValue, Object newValue) {
//               if(newValue != null){
//                   new SetUpTask(controller).start();
//               }
//           }
//       });
//       new SetUpTask(controller).start();
    }

    public void initOwner(Stage pOwner) {
        owner = pOwner;
    }

    public Stage getOwner() {
        return owner;
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
//        new ReloadTask(controller).start();
    }

    /**
     * forces scene to refresh (only update its values)
     */
    public void refresh() {
        controller.refresh();
//        new RefreshTask(controller).start();
    }

    /**
     * forces scene to close
     *
     * @return indicates if closing of the scene was successful
     */
    public boolean close() {
        sceneTitleProperty.removeListener(titleChangeListener);
        boolean success = controller.close();
        if (success && onClose != null) {
            onClose.handle(new ActionEvent());
        }
        return success;
    }

    /**
     * sets the title of the scene
     *
     * @param pTitle title of the scene to set
     */
    public final void setSceneTitle(String pTitle) {
        sceneTitleProperty.setValue(pTitle);
    }

    /**
     * get the current value of the scene title
     *
     * @return scene title, null if no title was set
     */
    public String getSceneTitle() {
        return sceneTitleProperty.getValue();
    }

    /**
     * get the scene title property warning: value can be null if no scene title
     * was set
     *
     * @return simple string property
     */
    public StringProperty getSceneTitleProperty() {
        return sceneTitleProperty;
    }

//    public void setUp(){
//        
//    }
//    
//    private class SetUpTask extends CpxTask<Void>{
//        
//        public SetUpTask(Controller pController){
//            super(pController);
//        }
//        @Override
//        protected Void call() throws Exception {
//            Platform.runLater(new Runnable() {
//                @Override
//                public void run() {
//                    setUp();
//                }
//            });
//            return null;
//        }
//        
//    }
//    private class RefreshTask extends CpxTask<Void>{
//        
//        public RefreshTask(Controller pController){
//            super(pController);
//        }
//        @Override
//        protected Void call() throws Exception {
//            controller.refresh();
//            return null;
//        }
//        
//    }
//    private class ReloadTask extends CpxTask<Void>{
//        
//        public ReloadTask(Controller pController){
//            super(pController);
//        }
//        @Override
//        protected Void call() throws Exception {
//            controller.reload();
//            return null;
//        }
//        
//    }
    /**
     * @return alternative root object to display scene without the header
     */
    public Parent getRootWithoutHeader() {
        return getRoot();
    }

    public void setOnClose(EventHandler<ActionEvent> pHandler) {
        onClose = pHandler;
    }

}
