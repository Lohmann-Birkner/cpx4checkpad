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
package de.lb.cpx.client.app.cm.fx.details;

import de.lb.cpx.client.app.cm.fx.simulation.model.VersionContent;
import de.lb.cpx.client.app.cm.fx.simulation.model.button.AddVersionButton;
import de.lb.cpx.client.app.cm.fx.simulation.model.combobox.VersionComboBox;
import de.lb.cpx.client.app.cm.fx.simulation.tables.CaseDetailsScrollPane;
import de.lb.cpx.client.app.service.facade.CaseServiceFacade;
import de.lb.cpx.client.core.model.Controller;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.util.Callback;

/**
 * FXML Controller class creates and manage the case details in the case
 * management / case simulation scene
 *
 * @author wilde
 */
public class CmCaseDetailsFXMLController extends Controller<CmCaseDetailsScene> {

    @FXML
    private CaseDetailsScrollPane<VersionContent> gpCaseDetails;

    /**
     * Initializes the controller class.
     *
     * @param url url
     * @param rb resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //nothing to do here
        //normally translations are initializied here
    }

    @Override
    public void afterInitialisingScene() {
        super.afterInitialisingScene(); //To change body of generated methods, choose Tools | Templates.
        gpCaseDetails.showMenu();

        gpCaseDetails.setVersionContentWidth(360);
        gpCaseDetails.setComparator(new Comparator<VersionContent>() {
            @Override
            public int compare(VersionContent o1, VersionContent o2) {
                return Boolean.compare(o1.getCaseDetails().getCsdIsLocalFl(), o2.getCaseDetails().getCsdIsLocalFl());
            }
        });
        gpCaseDetails.setMenuButton(new AddVersionButton<>(getScene().getVersionManager()));
        gpCaseDetails.setVersionCtrlFactory(new Callback<VersionContent, Control>() {
            @Override
            public Control call(VersionContent param) {
                return new VersionComboBox(getScene().getVersionManager(), param);
            }
        });
        gpCaseDetails.setOnRemove(new Callback<VersionContent, Boolean>() {
            @Override
            public Boolean call(VersionContent param) {
                if (param.getContent().getCsdIsLocalFl()) {
                    getScene().getVersionManager().removeFromManagedVersions(param);
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * init the view with values
     *
     * @param pListOfVersions list of currently selected versions
     * @param pServiceFacade servicefacade to get access to server functions
     */
    public void init(ObservableList<VersionContent> pListOfVersions, CaseServiceFacade pServiceFacade) {
        //add listener to reflect changes in the list in the ui
        pListOfVersions.addListener(new ListChangeListener<VersionContent>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends VersionContent> c) {
                while (c.next()) {
                    if (c.wasAdded()) {
                        for (VersionContent content : c.getAddedSubList()) {
                            //add new gridpane with version specific content
                            gpCaseDetails.addVersion(content);
                        }
                    }
                    if (c.wasRemoved()) {
                        for (VersionContent content : c.getRemoved()) {
                            //remove gridpane with version specific content
                            gpCaseDetails.removeVersion(content);
                        }
                    }
                }
            }
        });

        //add values for the current list
        for (VersionContent version : pListOfVersions) {
            gpCaseDetails.addVersion(version);
        }

    }

    @Override
    public void refresh() {
        super.refresh(); //To change body of generated methods, choose Tools | Templates.
        gpCaseDetails.refreshMenu();
    }

}
