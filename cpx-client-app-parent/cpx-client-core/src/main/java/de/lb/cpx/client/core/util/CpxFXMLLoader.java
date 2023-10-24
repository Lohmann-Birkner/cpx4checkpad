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
package de.lb.cpx.client.core.util;

import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.CpxScene;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Load helper, manages loading fxml and initialisation by controller class
 * of the fxml
 *
 * @author Wilde
 */
public class CpxFXMLLoader {

    private static final Logger LOG = Logger.getLogger(CpxFXMLLoader.class.getName());

    /**
     * instanciation of the scene, neccessary becasue of old drg module, needs
     * to be deleted
     *
     * @param pCpxControllerClass controller class to load fxml from
     * @return loaded cpx scene
     * @throws IOException thrown when fxml is not in resources
     */
    @Deprecated(since = "1.05")
    public static CpxScene getScene(Class<? extends Controller<?>> pCpxControllerClass) throws IOException {
        String ressource = getRessourceToLoad(pCpxControllerClass);
        URL res = pCpxControllerClass.getResource(ressource);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(res);
        Parent root = loader.load();
        CpxScene scene = new CpxScene(root, loader.getController(), res);
        return scene;
    }

    /**
     * get the specific fxml loader from the controller class
     *
     * @param pCpxControllerClass controller class to load fxml from
     * @return inialized loader to create scene from
     * @throws IOException if fxml is not presend in resources
     */
    public static FXMLLoader getLoader(Class<?> pCpxControllerClass) throws IOException {
        String ressource = getRessourceToLoad(pCpxControllerClass);
        URL res = pCpxControllerClass.getResource(ressource);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(res);
        try {
            loader.load();
        } catch (LoadException ex) {
            LOG.log(Level.SEVERE, "Cannot load FXML: " + ressource, ex);
            BasicMainApp.showErrorMessageDialog(ex, "FXML konnte nicht geladen werden: " + ressource);
        }
        return loader;
    }

//    /**
//     * get the specific fxml loader from the controller class
//     *
//     * @param res controller resource
//     * @return inialized loader to create scene from
//     * @throws IOException if fxml is not presend in resources
//     */
//    public static FXMLLoader getLoader(final URL res) throws IOException {
//        final String ressource = res.toString();
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(res);
//        try {
//            loader.load();
//        } catch (LoadException ex) {
//            LOG.log(Level.SEVERE, "Cannot load FXML: " + ressource, ex);
//            BasicMainApp.showErrorMessageDialog(ex, "FXML konnte nicht geladen werden: " + ressource);
//        }
//        return loader;
//    }
    /**
     * get the specific fxml loader from the controller class
     *
     * @param pRessourceLocation loaction of the fxml file
     * @param pCpxControllerClass controller class to load fxml from
     * @return inialized loader to create scene from
     * @throws IOException if fxml is not presend in resources or controller
     * mismatch fxml
     */
    public static FXMLLoader getLoader(String pRessourceLocation, Controller<?> pCpxControllerClass) throws IOException {
        URL res = pCpxControllerClass.getClass().getResource(pRessourceLocation);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(res);
        loader.setController(pCpxControllerClass);
        loader.load();
        return loader;
    }

    /**
     * tries to find specific ressource (fxml) to load by controller name
     *
     * @param pCpxControllerClass controller class to find fxml to
     * @return resource as string
     */
    public static String getRessourceToLoad(Class<?> pCpxControllerClass) {
        String classname = pCpxControllerClass.getSimpleName();
        if (classname.endsWith("Controller")) {
            classname = classname.substring(0, classname.indexOf("Controller"));
        }
        if (!classname.endsWith("FXML")) {
            classname += "FXML";
        }
        String fxmlRessource = "/fxml/" + classname + ".fxml";
        return fxmlRessource;
    }

    /**
     * helper funtion to set Anchors for anchor pane (may be useless if parent
     * from the node is not an anchorpane
     *
     * @param node node to set anchors
     * @return node with anchors
     */
    public static Node setAnchorsInNode(Node node) {
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
        return node;
    }

    /**
     * set specific anchros in the node
     *
     * @param node node to set anchors
     * @param anchors anchorvalue
     * @return node with anchors
     */
    public static Node setAnchorsInNode(Node node, double anchors) {
        AnchorPane.setTopAnchor(node, anchors);
        AnchorPane.setRightAnchor(node, anchors);
        AnchorPane.setLeftAnchor(node, anchors);
        AnchorPane.setBottomAnchor(node, anchors);
        return node;
    }

    /**
     * set specific anchros in the node
     *
     * @param node node to set anchors
     * @param topAnchor top anchor value
     * @param rightAnchor right anchor value
     * @param leftAnchor left anchor value
     * @param bottomAnchor bottom anchor value
     * @return node with anchors
     */
    public static Node setAnchorsInNode(Node node, double topAnchor, double rightAnchor, double leftAnchor, double bottomAnchor) {
        AnchorPane.setTopAnchor(node, topAnchor);
        AnchorPane.setRightAnchor(node, rightAnchor);
        AnchorPane.setLeftAnchor(node, leftAnchor);
        AnchorPane.setBottomAnchor(node, bottomAnchor);
        return node;
    }
}
