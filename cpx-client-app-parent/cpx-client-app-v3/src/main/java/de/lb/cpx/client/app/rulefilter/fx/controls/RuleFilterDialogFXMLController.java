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
package de.lb.cpx.client.app.rulefilter.fx.controls;

import de.lb.cpx.client.app.rulefilter.dialog.RuleFilterApplicationUsage;
import de.lb.cpx.client.app.rulefilter.model.table.RuleFilterTableView;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.fx.labeled.LabeledCheckComboBox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextField;
import de.lb.cpx.client.core.model.fx.pane.AsyncPane;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.connector.EjbConnector;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.model.enums.RuleTypeEn;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTypes;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.server.rule.services.RuleEditorBeanRemote;
import de.lb.cpx.shared.dto.RuleFilterDTO;
import de.lb.cpx.shared.lang.Lang;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.controlsfx.glyphfont.FontAwesome;

/**
 * Controller class for the rule filter.
 *
 * @author nandola
 */
//public class RuleFilterDialogFXMLController implements Initializable {
public class RuleFilterDialogFXMLController extends Controller<RuleFilterDialogScene> {

    private static final Logger LOG = Logger.getLogger(RuleFilterDialogFXMLController.class.getName());
    @FXML
    private AnchorPane rootPane;
    @FXML
    private LabeledCheckComboBox<Integer> lccbRulePoolYear;
    @FXML
    private LabeledCheckComboBox<CrgRulePools> lccbRulePool;
    @FXML
    private Button resetButton;
    @FXML
    private LabeledTextField tfRuleNumber;
    @FXML
    private LabeledTextField tfDescription;
    @FXML
    private LabeledTextField tfSuggestion;
    @FXML
    private LabeledTextField tfRuleIdent;
    @FXML
    private LabeledTextField tfRuleCategory;
    @FXML
    private LabeledCheckComboBox<CrgRuleTypes> lccbRuleType;
    @FXML
    private CheckBox cbAllRules;
//    @FXML
//    private ToggleGroup toggleGroup;
//    @FXML
//    private RadioButton cbErrorRules;
//    @FXML
//    private RadioButton cbWarningRules;
//    @FXML
//    private RadioButton cbInformationRules;
    @FXML
    private CheckBox cbErrorRules;
    @FXML
    private CheckBox cbWarningRules;
    @FXML
    private CheckBox cbInformationRules;
    @FXML
    private RuleFilterTableView tvRuleFilter;
    @FXML
    private VBox vbContent;
    private EjbProxy<RuleEditorBeanRemote> connectRuleEditorBean;
//    private final List<CrgRules> allRules = new ArrayList<>();
    private final ObservableList<CrgRules> allRules = FXCollections.observableArrayList();

    // Wrap the ObservableList in a FilteredList (initially display all events)
    private final FilteredList<CrgRules> filteredRules = new FilteredList<>(allRules, s -> true);
    private AsyncPane<Node> asyncPane;

    /**
     * Initializes the controller class.
     *
     * @param url url
     * @param rb rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
        LOG.log(Level.FINE, "Regelfilter is starting to initialize..");

        EjbConnector ejbConnector = Session.instance().getEjbConnector();
        connectRuleEditorBean = ejbConnector.connectRuleEditorBean();

        setUpLanguage();
        asyncPane = new AsyncPane<Node>(false) {
            @Override
            public void afterTask(Worker.State pState) {
                super.afterTask(pState); //To change body of generated methods, choose Tools | Templates.
//                searchRulesBasedOnTextSearchAndFilter();
//                restoreRuleFilter(RuleFilterDialogFXMLController.this.getScene().getAppUsage());
            }

            @Override
            public Node loadContent() {
                setUpNodes();
                searchRulesBasedOnTextSearchAndFilter();
                restoreRuleFilter(RuleFilterDialogFXMLController.this.getScene().getAppUsage());
                return vbContent;
            }
        };
        AnchorPane.setTopAnchor(asyncPane, 0.0d);
        AnchorPane.setRightAnchor(asyncPane, 0.0d);
        AnchorPane.setBottomAnchor(asyncPane, 0.0d);
        AnchorPane.setLeftAnchor(asyncPane, 0.0d);
        AnchorPane.setTopAnchor(vbContent, 0.0d);
        AnchorPane.setRightAnchor(vbContent, 0.0d);
        AnchorPane.setBottomAnchor(vbContent, 0.0d);
        AnchorPane.setLeftAnchor(vbContent, 0.0d);
        rootPane.getChildren().clear();
        rootPane.getChildren().add(asyncPane);

        // set tableView
        tvRuleFilter.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tvRuleFilter.getStyleClass().add("stay-selected-table-view");
        tvRuleFilter.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        cbAllRules.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) -> {
            if (cbAllRules.isSelected()) {
                TableColumn<CrgRules, ?> column = tvRuleFilter.getColumns().get(0);
                tvRuleFilter.getSelectionModel().selectAll();
            } else {
                tvRuleFilter.getSelectionModel().clearSelection();
            }
        });
        resetButton.setOnAction((ActionEvent t) -> {
            // clear all controls (clear applied filters)
            resetAllControls();
        });
        lccbRulePool.setConverter(new StringConverter<CrgRulePools>() {
            @Override
            public String toString(CrgRulePools t) {
                return t == null ? "" : String.valueOf(t.getCrgplPoolYear() + "_" + t.getCrgplIdentifier());
            }

            @Override
            public CrgRulePools fromString(String string) {
                return null;
            }
        });
    }

    @Override
    public void afterInitialisingScene() {
        super.afterInitialisingScene();
        asyncPane.reload();
    }

    @Override
    public boolean close() {
        super.close();
        filteredRules.clear();
        allRules.clear();
        RuleFilterDialogFXMLController.this.close();
        return true;

    }

    private void setUpLanguage() {
        lccbRulePoolYear.setTitle(Lang.getRuleFilterDialogPoolYear());
        lccbRulePool.setTitle(Lang.getRuleFilterDialogPool());
        resetButton.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.BACKWARD));
        resetButton.setText(Lang.getRuleFilterDialogResetButton());
        tfRuleNumber.setTitle(Lang.getRuleFilterDialogRuleNumber());
        tfDescription.setTitle(Lang.getRuleFilterDialogRuleDescription());
        tfSuggestion.setTitle(Lang.getRuleFilterDialogRuleSuggestion());
        tfRuleIdent.setTitle(Lang.getRuleFilterDialogRuleIdent());
        tfRuleCategory.setTitle(Lang.getRuleFilterDialogRuleCategory());
        lccbRuleType.setTitle(Lang.getRuleFilterDialogRuleType());
//        cbAllRules.setText("Alle anzeigen");
        cbAllRules.setText(Lang.getRuleFilterDialogAllRulesSelection());
        cbErrorRules.setText(Lang.getRuleFilterDialogErrorRules());
        cbWarningRules.setText(Lang.getRuleFilterDialogWarningRules());
        cbInformationRules.setText(Lang.getRuleFilterDialogInformationRules());
    }

    public Set<Integer> poolYears(final List<CrgRulePools> pools) {
        Set<Integer> years = new HashSet<>();
        pools.forEach((pool) -> {
            years.add(pool.getCrgplPoolYear());
        });
        return years;
    }

    private void setUpNodes() {
//        Session.instance().setRuleOverrideFlag(RuleOverrideFlags.SAVE_OLD);

        // based on settings (configurations), poolType should be used...
//        List<CrgRulePools> crgRulePools = connectRuleEditorBean.get().getCrgRulePools(PoolTypeEn.DEV);
        List<CrgRulePools> crgRulePools = connectRuleEditorBean.get().getRulePools4user();
        sortRulePools(crgRulePools);

        Set<Integer> poolYears = poolYears(crgRulePools);
        ArrayList<Integer> arrayListPoolYears = new ArrayList<>(poolYears);
        sortRulePoolYears(arrayListPoolYears);

//        List<CrgRuleTypes> ruleTypes = connectRuleEditorBean.get().findAllRuleTypes(PoolTypeEn.DEV);
        List<CrgRuleTypes> ruleTypes = connectRuleEditorBean.get().getRuleTypes4user();
        sortRuleTypes(ruleTypes);

        // takes much time to load with rule definations.
//        List<CrgRules> listOfAllRules = connectRuleEditorBean.get().getAllRules(PoolTypeEn.DEV);
        List<CrgRules> listOfAllRules = new ArrayList<>(connectRuleEditorBean.get().getRules4user().values());
        allRules.setAll(listOfAllRules);
        if (allRules.isEmpty()) {
            LOG.log(Level.WARNING, "There are no any rules for the selected pool type... ");
        }

        // set all pool years to the corresponding comboBox.
        lccbRulePoolYear.getControl().getItems().setAll(arrayListPoolYears);

        // set all pools to the corresponding comboBox.
        lccbRulePool.getControl().getItems().setAll(new ArrayList<>(crgRulePools));
//        lccbRulePool.setConverter(new StringConverter<CrgRulePools>() {
//            @Override
//            public String toString(CrgRulePools t) {
//                return t == null ? "" : String.valueOf(t.getCrgplPoolYear() + "_" + t.getCrgplIdentifier());
//            }
//
//            @Override
//            public CrgRulePools fromString(String string) {
//                return null;
//            }
//        });

        // set all rule types to the corresponding comboBox.
        lccbRuleType.getControl().getItems().setAll(new ArrayList<>(ruleTypes));
        lccbRuleType.setConverter(new StringConverter<CrgRuleTypes>() {
            @Override
            public String toString(CrgRuleTypes t) {
                return t == null ? "" : t.getCrgtDisplayText();
            }

            @Override
            public CrgRuleTypes fromString(String string) {
                return null;
            }
        });

//        // set tableView
//        tvRuleFilter.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//        tvRuleFilter.getStyleClass().add("stay-selected-table-view");
//        tvRuleFilter.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//        tvRuleFilter.setCache(true);
        if (allRules != null && !allRules.isEmpty()) {
            tvRuleFilter.setItems(FXCollections.observableArrayList(filteredRules));
        }

        cbErrorRules.setSelected(true);
        cbWarningRules.setSelected(true);
        cbInformationRules.setSelected(true);

//        resetButton.setOnAction((ActionEvent t) -> {
//            // clear all controls (clear applied filters)
//            resetAllControls();
//        });
//        cbAllRules.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) -> {
//            if (cbAllRules.isSelected()) {
//                TableColumn<CrgRules, ?> column = tvRuleFilter.getColumns().get(0);
//                tvRuleFilter.getSelectionModel().selectAll();
//            } else {
//                tvRuleFilter.getSelectionModel().clearSelection();
//            }
//        });
    }

    private void sortRulePoolYears(ArrayList<Integer> arrayList) {
        arrayList.sort(Comparator.comparing(Integer::intValue));
    }

    // sort based on short text
    public void sortRuleTypes(List<CrgRuleTypes> pList) {
        pList.sort(Comparator.comparing(CrgRuleTypes::getCrgtShortText));
    }

    // sort based on pool year
    public void sortRulePools(List<CrgRulePools> pList) {
        pList.sort(Comparator.comparing(CrgRulePools::getCrgplPoolYear));
    }

//    public void selectAllCheckBoxes(ActionEvent e) {
//
//        //Iterate through all items in ObservableList
//        for (CrgRules rule : tvRuleFilter.getItems()) {
//            //And change "selected" boolean
//            rule.setSelected(((CheckBox) e.getSource()).isSelected());
//        }
//
//    }
    // define all change listeners for all controls
//    private void initializeSearchAndFilter() {
//        tfRuleNumber.getControl().textProperty().addListener((ObservableValue<? extends String> ov, String oldValue, String newValue) -> {
//            String textToSearch = tfRuleNumber.getControl().getText();
//            if (textToSearch != null && !textToSearch.isEmpty()) {
//                searchRulesBasedOnRuleNumber(textToSearch);
//            }
//        });
//
//        tfDescription.getControl().textProperty().addListener((ObservableValue<? extends String> ov, String oldValue, String newValue) -> {
//            String textToSearch = tfDescription.getControl().getText();
//            if (textToSearch != null && !textToSearch.isEmpty()) {
//                searchRulesBasedOnRuleDescription(textToSearch);
//            }
//        });
//
//        filteredRules.addListener((ListChangeListener.Change<? extends CrgRules> change) -> {
//            tvRuleFilter.getItems().clear();
//            tvRuleFilter.getItems().addAll(FXCollections.observableArrayList(filteredRules));
//        });
//    }
//
//    private void searchRulesBasedOnRuleNumber(String textToSearch) {
//        filteredRules.setPredicate((CrgRules rule) -> rule.getCrgrNumber().toLowerCase().contains(textToSearch.toLowerCase()));
//    }
//
//    private void searchRulesBasedOnRuleDescription(String textToSearch) {
//        filteredRules.setPredicate((CrgRules rule) -> rule.getCrgrCaption().toLowerCase().contains(textToSearch.toLowerCase()));
//    }
    private void searchRulesBasedOnTextSearchAndFilter() {
        /*       TableFilter tableFilter = new TableFilter(tvRuleFilter);
        tableFilter.setSearchStrategy((input, target) -> {
            try {
                return target.matches(input);
            } catch (Exception e) {
                return false;
            }
        });
         */

        filteredRules.predicateProperty().bind(Bindings.createObjectBinding(()
                -> (CrgRules rule) -> (rule.getCrgrNumber() == null ? tfRuleNumber.getText().trim().isEmpty() : rule.getCrgrNumber().toLowerCase().contains(tfRuleNumber.getText().toLowerCase()))
                && (rule.getCrgrCaption() == null ? tfDescription.getText().trim().isEmpty() : rule.getCrgrCaption().toLowerCase().contains(tfDescription.getText().toLowerCase()))
                && (rule.getCrgrSuggText() == null ? tfSuggestion.getText().trim().isEmpty() : rule.getCrgrSuggText().toLowerCase().contains(tfSuggestion.getText().toLowerCase()))
                && (rule.getCrgrIdentifier() == null ? tfRuleIdent.getText().trim().isEmpty() : rule.getCrgrIdentifier().toLowerCase().contains(tfRuleIdent.getText().toLowerCase()))
                && (rule.getCrgrCategory() == null ? tfRuleCategory.getText().trim().isEmpty() : rule.getCrgrCategory().toLowerCase().contains(tfRuleCategory.getText().toLowerCase()))
                && (lccbRuleType.getCheckModel().getCheckedItems().isEmpty() ? true : lccbRuleType.getCheckModel().getCheckedItems().contains(rule.getCrgRuleTypes()))
                && (lccbRulePoolYear.getCheckModel().getCheckedItems().isEmpty() ? true : lccbRulePoolYear.getCheckModel().getCheckedItems().contains(rule.getCrgRulePools().getCrgplPoolYear()))
                && (lccbRulePool.getCheckModel().getCheckedItems().isEmpty() ? true : lccbRulePool.getCheckModel().getCheckedItems().contains(rule.getCrgRulePools()))
                //                && ((!cbErrorRules.isSelected() ? false : rule.getCrgrRuleErrorType().equals(RuleTypeEn.STATE_ERROR))
                //                || (!cbWarningRules.isSelected() ? false : rule.getCrgrRuleErrorType().equals(RuleTypeEn.STATE_WARNING))
                //                || (!cbInformationRules.isSelected() ? false : rule.getCrgrRuleErrorType().equals(RuleTypeEn.STATE_SUGG))),
                //&& ((cbErrorRules.isSelected() || cbWarningRules.isSelected() || cbInformationRules.isSelected() ? ((cbErrorRules.isSelected() ? rule.getCrgrRuleErrorType().equals(RuleTypeEn.STATE_ERROR) : (rule.getCrgrRuleErrorType().equals(RuleTypeEn.STATE_WARNING) && rule.getCrgrRuleErrorType().equals(RuleTypeEn.STATE_SUGG))) || (cbWarningRules.isSelected() ? rule.getCrgrRuleErrorType().equals(RuleTypeEn.STATE_WARNING) : (rule.getCrgrRuleErrorType().equals(RuleTypeEn.STATE_ERROR) && rule.getCrgrRuleErrorType().equals(RuleTypeEn.STATE_SUGG))) || (cbInformationRules.isSelected() ? rule.getCrgrRuleErrorType().equals(RuleTypeEn.STATE_SUGG) : (rule.getCrgrRuleErrorType().equals(RuleTypeEn.STATE_WARNING) && rule.getCrgrRuleErrorType().equals(RuleTypeEn.STATE_ERROR)))) : true)),
                && (cbErrorRules.isSelected() || cbWarningRules.isSelected() || cbInformationRules.isSelected() ? ((cbErrorRules.isSelected() ? rule.getCrgrRuleErrorType().equals(RuleTypeEn.STATE_ERROR) : false) || (cbWarningRules.isSelected() ? rule.getCrgrRuleErrorType().equals(RuleTypeEn.STATE_WARNING) : false) || (cbInformationRules.isSelected() ? rule.getCrgrRuleErrorType().equals(RuleTypeEn.STATE_SUGG) : false)) : true),
                tfRuleNumber.getControl().textProperty(),
                tfDescription.getControl().textProperty(),
                tfSuggestion.getControl().textProperty(),
                tfRuleIdent.getControl().textProperty(),
                tfRuleCategory.getControl().textProperty(),
                lccbRuleType.getCheckModel().getCheckedItems(),
                lccbRulePoolYear.getCheckModel().getCheckedItems(),
                lccbRulePool.getCheckModel().getCheckedItems(),
                cbErrorRules.selectedProperty(),
                cbWarningRules.selectedProperty(),
                cbInformationRules.selectedProperty()
        ));

        setFilteredRules();
    }

    public void resetAllControls() {
        tfRuleNumber.clear();
        tfDescription.clear();
        tfSuggestion.clear();
        tfRuleIdent.clear();
        tfRuleCategory.clear();
        lccbRuleType.getCheckModel().clearChecks();
        lccbRulePoolYear.getCheckModel().clearChecks();
        lccbRulePool.getCheckModel().clearChecks();
        cbErrorRules.setSelected(true);
        cbWarningRules.setSelected(true);
        cbInformationRules.setSelected(true);
        cbAllRules.setSelected(false);
    }

    private void setFilteredRules() {
        filteredRules.addListener((ListChangeListener.Change<? extends CrgRules> change) -> {
            try {
                cbAllRules.setSelected(false);
                tvRuleFilter.getItems().clear();
            } catch (UnsupportedOperationException ex) {
                //FilteredList does not support clear method
                LOG.log(Level.SEVERE, "Error on clear by update of list " + change.toString(), ex);
            }
            try {
//                tvRuleFilter.getItems().addAll((List<CrgRules>) filteredRules);
                tvRuleFilter.getItems().addAll(FXCollections.observableArrayList(filteredRules));
            } catch (UnsupportedOperationException ex) {
                //FilteredList does not support addAll method
                LOG.log(Level.SEVERE, "Error on addAll by update of list " + change.toString(), ex);
            }
        });
    }

    public void saveRuleFilter(RuleFilterApplicationUsage appUsage) {

        LOG.log(Level.INFO, " save the rule filter now...");

        final String ruleNumber = tfRuleNumber.getText();
        final String ruleDescription = tfDescription.getText();
        final String ruleSuggestion = tfSuggestion.getText();
        final String ruleIdent = tfRuleIdent.getText();
        final String ruleCategory = tfRuleCategory.getText();

//        final List<Integer> selectedYears = lccbRulePoolYear.getCheckModel().getCheckedItems();
        final ObservableList<Integer> selectedYears = lccbRulePoolYear.getCheckModel().getCheckedItems();
        final List<Integer> years = new ArrayList<>();
        for (Integer item : selectedYears) {
            years.add(item);
        }

        final ObservableList<CrgRulePools> selectedRulePools = lccbRulePool.getCheckModel().getCheckedItems();
        final List<CrgRulePools> rulePools = new ArrayList<>();
        for (CrgRulePools pool : selectedRulePools) {
            rulePools.add(pool);
        }

        ObservableList<CrgRuleTypes> selectedRuleTypes = lccbRuleType.getCheckModel().getCheckedItems();
        final List<CrgRuleTypes> ruleTypes = new ArrayList<>();
        for (CrgRuleTypes type : selectedRuleTypes) {
            ruleTypes.add(type);
        }

        boolean errorType = cbErrorRules.isSelected();
        boolean warningType = cbWarningRules.isSelected();
        boolean informationType = cbInformationRules.isSelected();

//        RuleFilterDTO ruleFilterDTO = new RuleFilterDTO(ruleNumber, ruleDescription, ruleSuggestion, ruleIdent, ruleCategory, selectedYears, selectedRulePools, selectedRuleTypes, errorType, warningType, informationType);
        RuleFilterDTO ruleFilterDTO = new RuleFilterDTO(ruleNumber, ruleDescription, ruleSuggestion, ruleIdent, ruleCategory, years, rulePools, ruleTypes, errorType, warningType, informationType);

        if (appUsage.equals(RuleFilterApplicationUsage.CaseList)) {
            Session.instance().setRuleFilterCaseListForUser(ruleFilterDTO);
        } else if (appUsage.equals(RuleFilterApplicationUsage.BatchAdministration)) {
            Session.instance().setRuleFilterBatchAdmForUser(ruleFilterDTO);
        }
    }

    public void restoreRuleFilter(RuleFilterApplicationUsage appUsage) {

        RuleFilterDTO ruleFilterDTO = null;
        if (appUsage.equals(RuleFilterApplicationUsage.CaseList)) {
            ruleFilterDTO = Session.instance().getRuleFilterCaseListForUser();
        } else if (appUsage.equals(RuleFilterApplicationUsage.BatchAdministration)) {
            ruleFilterDTO = Session.instance().getRuleFilterBatchAdmForUser();
        }

        if (ruleFilterDTO != null) {

            // first reset all controls
            resetAllControls();

            // set Text to the all TextFields..
            tfRuleNumber.setText(ruleFilterDTO.getRuleNumber());
            tfDescription.setText(ruleFilterDTO.getRuleDescription());
            tfSuggestion.setText(ruleFilterDTO.getRuleSuggestion());
            tfRuleIdent.setText(ruleFilterDTO.getRuleIdentNumber());
            tfRuleCategory.setText(ruleFilterDTO.getRuleCategorie());

            // check comboBoxes..
            Integer[] years = ruleFilterDTO.getYears();
            if (years != null && years.length > 0) {
                for (Integer year : years) {
                    if (year != null) {
                        lccbRulePoolYear.getCheckModel().check(year);
                    }
                }
            }

            CrgRulePools[] rulePools = ruleFilterDTO.getRulePools();
            if (rulePools != null && rulePools.length > 0) {
                for (CrgRulePools pool : rulePools) {
                    if (pool != null) {
                        for (CrgRulePools item : lccbRulePool.getItems()) {
                            if (item != null && item.id == pool.id) {
                                lccbRulePool.getCheckModel().check(item);
                            }
                        }
                    }
                }
            }

            CrgRuleTypes[] ruleTypes = ruleFilterDTO.getRuleTypes();
            if (ruleTypes != null && ruleTypes.length > 0) {
                for (CrgRuleTypes type : ruleTypes) {
                    if (type != null) {
                        for (CrgRuleTypes item : lccbRuleType.getItems()) {
                            if (item != null && item.id == type.id) {
                                lccbRuleType.getCheckModel().check(item);
                            }
                        }
                    }
                }
            }

            // select/deselect checkBoxes..
            if (ruleFilterDTO.getErrorType() != null) {
                cbErrorRules.setSelected(ruleFilterDTO.getErrorType());
            }
            if (ruleFilterDTO.getWarningType() != null) {
                cbWarningRules.setSelected(ruleFilterDTO.getWarningType());
            }
            if (ruleFilterDTO.getInformationType() != null) {
                cbInformationRules.setSelected(ruleFilterDTO.getInformationType());
            }

        } else {
            LOG.log(Level.INFO, " There is no RuleFilter (either null or empty)...");
        }

    }

    @Override
    public void reload() {
        super.reload();
    }

    @Override
    public void refresh() {
        super.refresh();
        tvRuleFilter.refresh();
    }

    public List<CrgRules> getSelectedRules() {
        return tvRuleFilter.getSelectionModel().getSelectedItems();
    }

    public RuleFilterTableView getRuleFilterTableView() {
        return tvRuleFilter;
    }

}
