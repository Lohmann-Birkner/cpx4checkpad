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
 *    2019  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.rulefilter.dialog;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.rulefilter.fx.controls.RuleFilterDialogScene;
import de.lb.cpx.client.app.rulefilter.model.table.RuleFilterTableView;
import de.lb.cpx.client.core.model.fx.dialog.FormularDialog;
import de.lb.cpx.client.core.util.CpxFXMLLoader;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.shared.lang.Lang;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;

/**
 * rulefilter dialog class
 *
 * @author nandola
 */
public class RuleFilterDialog extends FormularDialog<List<CrgRules>> {

    private static final Logger LOG = Logger.getLogger(RuleFilterDialog.class.getName());

    private RuleFilterDialogScene ruleFilterDialogScene;
    private RuleFilterApplicationUsage applicationUsage;

    public RuleFilterDialog(RuleFilterApplicationUsage appUsage) {
        super(MainApp.getStage(), Modality.APPLICATION_MODAL, Lang.getRuleFilterButtonText());

        applicationUsage = appUsage;

//        setValidationSupport(validationSupport);
//        validationSupport = getValidationSupport();
        validationSupport.initInitialDecoration();
        registerValidatorControls();

        try {
            ruleFilterDialogScene = new RuleFilterDialogScene(applicationUsage);
            CpxFXMLLoader.setAnchorsInNode(ruleFilterDialogScene.getRoot());
            addControlGroup(ruleFilterDialogScene.getRoot());
            VBox.setVgrow(ruleFilterDialogScene.getRoot(), Priority.ALWAYS);
//            // restore previously saved ruleFilter..

//            ruleFilterDialogScene.getController().restoreRuleFilter(appUsage);
        } catch (IOException ex) {
            Logger.getLogger(RuleFilterDialog.class.getName()).log(Level.SEVERE, null, ex);
        }

//        RuleFilterDialogFXMLController ruleFilterDialogFXMLController = new RuleFilterDialogFXMLController();
//        addControlGroup(ruleFilterDialogFXMLController.getRootNode(), true);
        getDialogPane().sceneProperty().get().getWindow().centerOnScreen();
    }

    @Override
    public List<CrgRules> onSave() {
        // save rule filter on "OK" button
        ruleFilterDialogScene.getController().saveRuleFilter(applicationUsage);

        return ruleFilterDialogScene.getController().getSelectedRules();
    }

    private ObjectProperty<ValidationSupport> validationSupportProperty;

    private ObjectProperty<ValidationSupport> validationSupportProperty() {
        if (validationSupportProperty == null) {
            validationSupportProperty = new SimpleObjectProperty<>();
        }
        return validationSupportProperty;
    }

    public void setValidationSupport(ValidationSupport validationSupport) {
        validationSupportProperty().set(validationSupport);
    }

    final ValidationSupport getValidationSupport() {
        return validationSupportProperty().get();
    }

    private void registerValidatorControls() {
        Platform.runLater(() -> {
            if (validationSupport != null) {

                RuleFilterTableView ruleFilterTableView = ruleFilterDialogScene.getController().getRuleFilterTableView();

                registerValidator(ruleFilterTableView);

                ruleFilterTableView.getSelectionModel().getSelectedItems().addListener((ListChangeListener.Change<? extends CrgRules> change) -> {
                    registerValidator(ruleFilterTableView);
                });
            }
        });
    }

    private void registerValidator(RuleFilterTableView ruleFilterTableView) {
//        RuleFilterTableView ruleFilterTableView = ruleFilterDialogScene.getController().getRuleFilterTableView();
        if (ruleFilterTableView != null) {
            validationSupport.registerValidator(ruleFilterTableView, true, (Control t, Object u) -> {
                ValidationResult res = new ValidationResult();
                ObservableList<CrgRules> selectedItems = ruleFilterTableView.getSelectionModel().getSelectedItems();

                if (u == null || ruleFilterTableView.getItems().isEmpty()) {
                    res.addErrorIf(t, "die tabelle ist leer!", (u == null || ruleFilterTableView.getItems().isEmpty()));
                } else if (selectedItems.isEmpty()) {
                    res.addErrorIf(t, "bitte selektieren Sie mindestens eine Regel", selectedItems.isEmpty());
                } else if (selectedItems.size() > 999) {
                    res.addErrorIf(t, "es darf nicht mehr als 999 Regeln gleichzeitig selektiert werden", selectedItems.size() > 999);
                }

                /*       
            ruleFilterTableView.getSelectionModel().getSelectedItems().addListener((ListChangeListener.Change<? extends CrgRules> change) -> {
                if (ruleFilterTableView.getSelectionModel().getSelectedItems().isEmpty()) {
                    res.addErrorIf(t, "select at least one rule", ruleFilterTableView.getSelectionModel().getSelectedItems().isEmpty());
                }
                if (ruleFilterTableView.getSelectionModel().getSelectedItems().size() > 1000) {
                    res.addErrorIf(t, "select less than 1000 rules", ruleFilterTableView.getSelectionModel().getSelectedItems().size() > 1000);
                }
            });    */
                return res;
            });
        }
    }

    public void restoreSelection(String pValues) {
        long start = System.currentTimeMillis();
        List<CrgRules> items = ruleFilterDialogScene.getController().getRuleFilterTableView().getItems();
        items = Objects.requireNonNullElse(items, new ArrayList<>());
        if (items.isEmpty()) {
            LOG.warning("List of Rules are null or empty!");
            return;
        }
        List<String> rIds = getListOfRIds(pValues);
        if (rIds.isEmpty()) {
            LOG.finer("Can not restore selection, no rids detected!");
            return;
        }
        for (CrgRules item : items) {
            if (rIds.contains(item.getCrgrRid())) {
                checkItemInTableView(item);
            }
        }
        LOG.log(Level.INFO, "restore selection of {0} in {1} ms", new Object[]{rIds.size(), System.currentTimeMillis() - start});
//        ruleFilterDialogScene.getController().getRuleFilterTableView().getSelectionModel().getSelectedItems();
    }

    protected final void checkItemInTableView(CrgRules pItem) {
        ruleFilterDialogScene.getController().getRuleFilterTableView().getSelectionModel().select(pItem);
    }

    private List<String> getListOfRIds(String pValues) {
        List<String> rIds = new ArrayList<>();
        pValues = Objects.requireNonNullElse(pValues, "");
        if (pValues.isEmpty()) {
            return rIds;
        }
        String[] splitIds = pValues.split(",");
        for (String splitId : splitIds) {
            splitId = splitId.trim();
            //check content of attribute to ensure there are correct values
            //in the returning list, ignore the rest
            try {
                Long.parseLong(splitId);
            } catch (NumberFormatException ex) {
                LOG.log(Level.FINER, "can not parse rId: {0}, is not a number", splitId);
                continue;
            }
            rIds.add(splitId);
        }
        return rIds;
    }

}
