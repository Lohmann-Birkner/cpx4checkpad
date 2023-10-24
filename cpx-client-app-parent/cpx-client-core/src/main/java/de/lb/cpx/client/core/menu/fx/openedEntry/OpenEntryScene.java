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
package de.lb.cpx.client.core.menu.fx.openedEntry;

import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.model.CpxScreen;
import de.lb.cpx.client.core.util.CpxFXMLLoader;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.io.IOException;
import java.util.logging.Level;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * scene fot the open Entry Object in the list view of the titledpane in the
 * menu handles the opene objects and sets values in the specific controller on
 * close it will unlock its entry in the database and inform potential listeners
 * via the closeRequestedProperty
 *
 * @author wilde
 * @param <T> type of entity to be handled by the scene
 * @param <S> entity type that is handeled in the scene
 */
public class OpenEntryScene<T extends CpxScene, S extends AbstractEntity> extends CpxScreen {

    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(OpenEntryScene.class.getName());

    private final BooleanProperty closeRequestedProperty = new SimpleBooleanProperty(false);
    private T openendScene;

    /**
     * creates new OpenEntry Object with a case to opened case
     *
     * @param pScene scene to store
     * @throws IOException throws exception if fxml file can not be loaded
     */
    public OpenEntryScene(T pScene) throws IOException {
        super(CpxFXMLLoader.getLoader(OpenedEntryFXMLController.class));
        this.openendScene = pScene;
        setValues(pScene.getController());
    }

    @Override
    public OpenedEntryFXMLController getController() {
        return (OpenedEntryFXMLController) super.getController();
    }

    @Override
    public boolean close() {
//        if(openendScene.getController() instanceof CaseDetailsMainViewFXMLController){
//            CaseDetailsMainViewFXMLController ctrl = (CaseDetailsMainViewFXMLController) openendScene.getController();
//            if(ctrl.close()){
//                closeRequestedProperty.setValue(Boolean.TRUE);
//            }else{
//                Logger.getLogger(getClass().getName()).info("Case unlock failed for " + ctrl.getCase().getId() + " is Case Locked by other Client?");
//               BasicMainApp.showErrorMessageDialog(Lang.getWorkingListContextMenuUnlockError(ctrl.getCase().getId()));
//            }
//        }
        super.close();
        S entity = getEntity();
        LOG.log(Level.FINER, "try to close with id " + entity.id + " is of type " + entity.getClassName());
        if (openendScene.close()) {
            LOG.log(Level.INFO, "close for " + entity.getClassName() + " with id " + entity.id + " successful!");
            if (openendScene.getController() != null) {
                openendScene.getController().setScene(null);
            }
            closeRequestedProperty.setValue(Boolean.TRUE);
            openendScene = null;
//            closeRequestedProperty = null;
//            System.gc();
            return true;
        }
        String error = "Close for " + entity.getClassName() + " with id " + entity.id + " failed";
        LOG.log(Level.SEVERE, error);
        BasicMainApp.showErrorMessageDialog(error);
        return false;
    }

    /**
     * get the closeRequestedProperty to get the information if the user wants
     * to close the scene, listeners can by that property react to the action
     *
     * @return get the close request property for bindings
     */
    public BooleanProperty getCloseRequestedProperty() {
        return closeRequestedProperty;
    }

    /**
     * get if close was request by the user
     *
     * @return indicator if the user wants to close the scene
     */
    public boolean isCloseRequested() {
        return closeRequestedProperty.getValue();
    }

    /**
     * get the currently stored Scene
     *
     * @return the current hospital case
     */
    public T getStoredScene() {
        return openendScene;
    }

    protected void setValues(Controller<?> pController) {
//        if (pController instanceof CaseManagementFXMLController) {
//            getController().setEntryHeaderText(((CaseManagementFXMLController) pController).getCase().getCsCaseNumber());
//            TPatient patient = ((CaseManagementFXMLController) pController).getCase().getPatient();
//            getController().setDescription(patient.getPatSecName() != null ? patient.getPatSecName() : "" + (patient.getPatFirstName() != null ? (" ," + patient.getPatFirstName()) : ""));
//            return;
//        }
//        if (pController instanceof WmMainFrameFXMLController) {
//            getController().setEntryHeaderText(String.valueOf(((WmMainFrameFXMLController) pController).getProcess().getWorkflowNumber()));
//            TPatient patient = ((WmMainFrameFXMLController) pController).getProcess().getPatient();
//            getController().setDescription(patient.getPatSecName() != null ? patient.getPatSecName() : "" + (patient.getPatFirstName() != null ? (" ," + patient.getPatFirstName()) : ""));
//            return;
//        }
        getController().setEntryHeaderText("Can not open! Unknown Entity found");
        LOG.log(Level.SEVERE, "Can not open! Unknown Entity found! Entity instance of " + pController.getClass().getSimpleName());
    }

    public S getEntity() {
//        if (openendScene.getController() instanceof CaseManagementFXMLController) {
//            return (S) ((CaseManagementFXMLController) openendScene.getController()).getCase();
//        }
//        if (openendScene.getController() instanceof WmMainFrameFXMLController) {
//            return (S) ((WmMainFrameFXMLController) openendScene.getController()).getProcess();
//        }
        return null;
    }

    public void checkGrouperModel() {
    }
}
