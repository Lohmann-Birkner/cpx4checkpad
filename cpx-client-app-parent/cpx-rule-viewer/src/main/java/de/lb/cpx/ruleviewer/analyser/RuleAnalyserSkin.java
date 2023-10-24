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
package de.lb.cpx.ruleviewer.analyser;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.model.fx.alert.ConfirmDialog;
import de.lb.cpx.client.core.model.fx.button.EditButton;
import de.lb.cpx.client.core.model.fx.button.ReloadButton;
import de.lb.cpx.client.core.model.fx.button.SearchToggleButton;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextField;
import de.lb.cpx.client.core.model.fx.pane.AsyncPane;
import de.lb.cpx.client.core.model.task.CopyCaseToCommonTask;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.grouper.model.transfer.TransferRuleAnalyseResult;
import de.lb.cpx.grouper.model.transfer.TransferRuleResult;
import de.lb.cpx.model.TCase;
import de.lb.cpx.ruleviewer.analyser.attributes.AnalyserAttributes;
import de.lb.cpx.ruleviewer.analyser.editors.CaseChangedEvent;
import de.lb.cpx.ruleviewer.analyser.model.AdditionalAnalyserResult;
import de.lb.cpx.ruleviewer.analyser.tabs.AsyncAnalyserAttributesTab;
import de.lb.cpx.ruleviewer.analyser.tabs.CaseDataTab;
import de.lb.cpx.ruleviewer.analyser.tabs.DepartmentDataTab;
import de.lb.cpx.ruleviewer.analyser.tabs.FeeDataTab;
import de.lb.cpx.ruleviewer.analyser.tabs.IcdDataTab;
import de.lb.cpx.ruleviewer.analyser.tabs.LaboratoryDataTab;
import de.lb.cpx.ruleviewer.analyser.tabs.OpsDataTab;
import de.lb.cpx.ruleviewer.analyser.tabs.PatientDataTab;
import de.lb.cpx.ruleviewer.analyser.task.DeleteAnalyserCaseTask;
import de.lb.cpx.ruleviewer.analyser.task.SaveAnalyserCaseContentTask;
import de.lb.cpx.ruleviewer.util.PreDefinedCases;
import de.lb.cpx.server.commonDB.model.CCase;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.rules.enums.CaseValidationGroupErrList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.controlsfx.control.PopOver;
import org.controlsfx.glyphfont.FontAwesome;

/**
 * Skin Class for rule Analyser handles fxml and ui interaction
 *
 * @author wilde
 */
public class RuleAnalyserSkin extends SkinBase<RuleAnalyser> {

    private static final Logger LOG = Logger.getLogger(RuleAnalyserSkin.class.getName());

    private TreeView<CaseCategoryItem> tvCaseCategories;
    private Label lblCaseName;
    private Button btnCopyAndSaveCase;
    private TabPane tpCaseData;
    private HistoryDataTabPane tpCategoryData = new HistoryDataTabPane();
    private TreeView<TransferRuleResult> tvTestResult;
    private VBox boxTestResult;
    private AsyncPane<TreeView<TransferRuleResult>> resultPane;
    private CaseDataTab tabCaseData;
    private DepartmentDataTab tabDepartmentData;
    private LaboratoryDataTab tabLabData;
    private OpsDataTab tabOpsData;
    private HBox boxCaseDataMenu;
    private SearchToggleButton btnSearch;
    private EditButton btnEdit;
    private IcdDataTab tabIcdData;
    private PatientDataTab tabPatData;
    private Button btnResultExpand;
    private HBox boxResultContainer;
//    private VBox boxAddResultContainer;
    private AdditionalAnalyserResult additionalResults;
    private VBox boxAddResultContent;
    private HBox boxCaseCategoriesMenu;
    private Button btnEditName;
    private FeeDataTab tabFeeData;
    private AnchorPane apAddResultContainer;
    private AnchorPane apCaseDataContainer;
    private AnchorPane apTabDataContainer;
    private Button btnRevert;
    private BorderPane errorMessagePane;
    private Label errorMessage;
    public RuleAnalyserSkin(RuleAnalyser pSkinnable) throws IOException {
        super(pSkinnable);
        getChildren().add(initRoot());

        tvCaseCategories.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<CaseCategoryItem>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<CaseCategoryItem>> observable, TreeItem<CaseCategoryItem> oldValue, 
                    TreeItem<CaseCategoryItem> newValue) {
//                tpCaseData.setDisable(newValue == null);
                if (newValue == null) {
                    setUpCaseDisplay(null);
                    return;
                }
//                if (newValue.getValue().getHospitalCase() != null) {
//                    setUpCaseDisplay(newValue.getValue().getHospitalCase());
//                }
                if (newValue.getValue() != null) {
                    setUpCaseDisplay(newValue);
                    resultPane.reload();
                }
            }
        });
        getSkinnable().getCaseList().addListener((ListChangeListener.Change<? extends CCase> change) -> {
            setUpCaseCategories();
        });
//        setUpAnalyserResult(pSkinnable.getOnAnalyse().call(pSkinnable.getCase()));

//        getSkinnable().caseProperty().addListener(new ChangeListener<TCase>() {
//            @Override
//            public void changed(ObservableValue<? extends TCase> ov, TCase t, TCase t1) {
//                setUpAnalyserResult(pSkinnable.getOnAnalyse().call(t1));
//            }
//        });
        getSkinnable().getProperties().addListener(new MapChangeListener<Object, Object>() {
            @Override
            public void onChanged(MapChangeListener.Change<? extends Object, ? extends Object> change) {
                if (change.wasAdded()) {
                    if (RuleAnalyser.ANALYSE_VALUE.equals(change.getKey())) {
                        resultPane.reload();
                        getSkinnable().getProperties().remove(RuleAnalyser.ANALYSE_VALUE);
                    }
                }
            }
        });
        handleAdditionalResults(getSkinnable().isShowAdditionalResults());
        getSkinnable().showAdditionalResultsProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                handleAdditionalResults(newValue);
            }
        });
        getSkinnable().analyserCaseCompareModeProperty().addListener(new ChangeListener<RuleAnalyser.AnalyserCaseCompareMode>() {
            @Override
            public void changed(ObservableValue<? extends RuleAnalyser.AnalyserCaseCompareMode> observable, RuleAnalyser.AnalyserCaseCompareMode oldValue, RuleAnalyser.AnalyserCaseCompareMode newValue) {
                setUpCaseCategories();
            }
        });
        Callback <TCase, Void> cl = new Callback<TCase, Void>(){

                  public Void call(TCase pCase){
                      setUpAnalyserResult(getSkinnable().getOnHistoryAnalyse().call(pCase));
                      return null;
 
                  };

       //            getSkinnable().getOnHistoryAnalyse())
               }   ;    
        tpCategoryData.setOnHistoryAnalyse(cl) ;
        tpCategoryData.setOnPerformGroup(getSkinnable().getOnPerformGroup()) ;
    }

    private CaseCategoryItem getSelectedItem() {
        if (tvCaseCategories == null) {
            return null;
        }
        if (tvCaseCategories.getSelectionModel().getSelectedItem() == null) {
            return null;
        }
        return tvCaseCategories.getSelectionModel().getSelectedItem().getValue();
    }

    private CCase getSelectedCase() {
        CaseCategoryItem item = getSelectedItem();
        if (item == null) {
            return null;
        }
        return item.getAnalyserCase();
    }

    private Parent initRoot() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/RuleAnalyserFXML.fxml"));

        btnCopyAndSaveCase = (Button) root.lookup("#btnCopyAndSaveCase");
        btnCopyAndSaveCase.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.COPY));
        btnCopyAndSaveCase.setText("");

        btnRevert = (Button) root.lookup("#btnRevert");
        btnRevert.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.UNDO));
        btnRevert.setText("");
        btnRevert.setTooltip(new Tooltip("Zurücksetzen"));

        tpCaseData = (TabPane) root.lookup("#tpCaseData");
        tpCaseData.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                if (newValue instanceof AsyncAnalyserAttributesTab) {
                    ((AsyncAnalyserAttributesTab) newValue).reload();
                }
//                setInTab(getSkinnable().getCase());
            }
        });
        boxCaseDataMenu = (HBox) root.lookup("#boxCaseDataMenu");
        boxCaseDataMenu.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                StackPane header = (StackPane) tpCaseData.lookup(".tab-header-area");
                if (header != null) {
                    //compute new Values translate whole header to the left and than set padding to the right
                    //not really nice, but found no other way, should be changed to a more clear way
                    //tbMenu is normaly not present in that controller .. can be solved maybe with an lookup on this controller parent?!
                    header.setTranslateX(-1.0d * newValue.intValue());
                    header.setPadding(new Insets(0, 0, 0, newValue.doubleValue()));//tbMenu.getPrefWidth()));
                }
            }
        });
//        boxCaseDataMenu.disableProperty().bind(tpCaseData.disableProperty());
        btnSearch = new SearchToggleButton();
        btnSearch.getStyleClass().add("analyser-tab-menu-toggle-button");
        btnSearch.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                ((AsyncAnalyserAttributesTab) tpCaseData.getSelectionModel().getSelectedItem()).setShowSearchField(newValue);
            }
        });
        btnEdit = new EditButton();
        btnEdit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AsyncAnalyserAttributesTab<? extends AnalyserAttributes> tab = (AsyncAnalyserAttributesTab<? extends AnalyserAttributes>) tpCaseData.getSelectionModel().getSelectedItem();
                PopOver pop = new PopOver();
                pop.setAnimated(false);
                pop.setArrowLocation(PopOver.ArrowLocation.BOTTOM_RIGHT);
                Node menu = tab.getMenu();
                pop.setContentNode(menu);
                menu.addEventFilter(CaseChangedEvent.caseChangedEvent(), new EventHandler<CaseChangedEvent>() {
                    @Override
                    public void handle(CaseChangedEvent event) {
                        tab.reload();
                        resultPane.reload();
                    }
                });
                pop.show(btnEdit);
            }
        });
        boxCaseDataMenu.getChildren().addAll(btnSearch, btnEdit);
//        tpCaseData.setDisable(true);

        tabCaseData = new CaseDataTab() {
            @Override
            public TCase loadCase() {
                return loadCaseData();
            }

        };
        tabCaseData.getContent().addEventFilter(CaseChangedEvent.caseChangedEvent(), new EventHandler<CaseChangedEvent>() {
            @Override
            public void handle(CaseChangedEvent t) {
                resultPane.reload();
                tabCaseData.reload();
            }
        });

        tabDepartmentData = new DepartmentDataTab() {
            @Override
            public TCase loadCase() {
                return loadCaseData();
            }

        };
        tabDepartmentData.getContent().addEventFilter(CaseChangedEvent.caseChangedEvent(), new EventHandler<CaseChangedEvent>() {
            @Override
            public void handle(CaseChangedEvent t) {
                resultPane.reload();
                tabDepartmentData.reload();
            }
        });

        tabIcdData = new IcdDataTab() {
            @Override
            public TCase loadCase() {
                return loadCaseData();
            }

        };
        tabIcdData.getContent().addEventFilter(CaseChangedEvent.caseChangedEvent(), new EventHandler<CaseChangedEvent>() {
            @Override
            public void handle(CaseChangedEvent t) {
                resultPane.reload();
                tabIcdData.reload();
            }
        });

        tabOpsData = new OpsDataTab() {
            @Override
            public TCase loadCase() {
                return loadCaseData();
            }

        };
        tabOpsData.getContent().addEventFilter(CaseChangedEvent.caseChangedEvent(), new EventHandler<CaseChangedEvent>() {
            @Override
            public void handle(CaseChangedEvent t) {
                resultPane.reload();
                tabOpsData.reload();
            }
        });

        tabLabData = new LaboratoryDataTab() {
            @Override
            public TCase loadCase() {
                return loadCaseData();
            }

        };
        tabLabData.getContent().addEventFilter(CaseChangedEvent.caseChangedEvent(), new EventHandler<CaseChangedEvent>() {
            @Override
            public void handle(CaseChangedEvent t) {
                resultPane.reload();
                tabPatData.reload();
            }
        });

        tabPatData = new PatientDataTab() {
            @Override
            public TCase loadCase() {
                return loadCaseData();
            }

        };
        tabPatData.getContent().addEventFilter(CaseChangedEvent.caseChangedEvent(), new EventHandler<CaseChangedEvent>() {
            @Override
            public void handle(CaseChangedEvent t) {
                resultPane.reload();
                tabPatData.reload();
            }
        });

        tabFeeData = new FeeDataTab() {
            @Override
            public TCase loadCase() {
                return loadCaseData();
            }

        };
        tabFeeData.getContent().addEventFilter(CaseChangedEvent.caseChangedEvent(), new EventHandler<CaseChangedEvent>() {
            @Override
            public void handle(CaseChangedEvent t) {
                resultPane.reload();
                tabFeeData.reload();
            }
        });

        tpCaseData.getTabs().addAll(tabCaseData, tabPatData, tabDepartmentData, tabIcdData, tabOpsData, tabFeeData, tabLabData);
//        btnCopyAndSaveCase.disableProperty().bind(Bindings.isNull(getSkinnable().caseProperty()));

        tvTestResult = new TreeView<>();
        tvTestResult.getStyleClass().add(0, "tree-view-test-result");
        tvTestResult.setRoot(new TreeItem<>());
        tvTestResult.setShowRoot(false);
        tvTestResult.setCellFactory((TreeView<TransferRuleResult> p) -> new AnalyserResultCell());
//        tvTestResult.disableProperty().bind(Bindings.isNull(getSkinnable().caseProperty()));

        boxTestResult = (VBox) root.lookup("#boxTestResult");
        resultPane = new AsyncPane<>() {
            @Override
            public TreeView<TransferRuleResult> loadContent() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
//                        setUpAnalyserResult(getSkinnable().getOnAnalyse().call(getSkinnable().getCase()));

//                        setUpAnalyserResult(getSkinnable().getOnAnalyse().call(getSkinnable().getCase(getSelectedCase())));
                        CaseCategoryItem item = getSelectedItem();
                        if (item == null) {
                            setUpAnalyserResult(null);
                            return;
                        }
                        if (item.isExample()) {
                            setUpAnalyserResult(getSkinnable().getOnAnalyse().call(item.getHospitalCase()));
                            return;
                        }if(item.isContainer()){
                            if(nodeHasCasesOnly(tvCaseCategories.getSelectionModel().getSelectedItem())) {
                                tpCategoryData.setUpAnalyserResult();
                                 return;

                            }
                        }
                        setUpAnalyserResult(getSkinnable().getOnAnalyse().call(getSkinnable().getCase(getSelectedCase())));
                    }
                });
                return tvTestResult;
            }
        };
        VBox.setVgrow(resultPane, Priority.ALWAYS);
        boxTestResult.getChildren().add(0, resultPane);
        errorMessagePane = new BorderPane();
        errorMessage = new Label();
        errorMessage.setWrapText(true);
        errorMessage.prefWidthProperty().bind(errorMessagePane.widthProperty());
        errorMessage.maxWidthProperty().bind(errorMessage.prefWidthProperty());
//        errorMessage.prefHeightProperty().bind(errorMessagePane.heightProperty().multiply(0.8));
        errorMessagePane.setCenter(errorMessage);
//        errorMessage.setTextAlignment(TextAlignment.CENTER);
        tvCaseCategories = (TreeView) root.lookup("#tvCaseCategories");
        tvCaseCategories.setContextMenu(getCaseCategoriesContextMenu(tvCaseCategories));
        setUpCaseCategories();

        lblCaseName = (Label) root.lookup("#lblCaseName");
        btnEditName = (Button) root.lookup("#btnEditName");
        btnEditName.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.PENCIL));
        btnEditName.setVisible(false);

        //opt. additional result values
        btnResultExpand = (Button) root.lookup("#btnResultExpand");
        btnResultExpand.graphicProperty().bind(Bindings
                .when(getSkinnable().showAdditionalResultsProperty())
                .then(ResourceLoader.rotate(FontAwesome.Glyph.SIGN_IN, 0.0))
                .otherwise(ResourceLoader.rotate(FontAwesome.Glyph.SIGN_OUT, -180.0))
        );
        btnResultExpand.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                getSkinnable().setShowAdditionalResults(!getSkinnable().isShowAdditionalResults());
            }
        });
        boxResultContainer = (HBox) root.lookup("#boxResultContainer");
//        boxAddResultContainer = (VBox) root.lookup("#boxAddResultContainer");
        apAddResultContainer = (AnchorPane) root.lookup("#apAddResultContainer");
        apCaseDataContainer = (AnchorPane) root.lookup("#apCaseDataContainer");
        apTabDataContainer  = (AnchorPane) root.lookup("#apTabDataContainer");
        boxAddResultContent = (VBox) root.lookup("#boxAddResultContent");
        additionalResults = new AdditionalAnalyserResult();
        VBox.setVgrow(additionalResults, Priority.ALWAYS);
        boxAddResultContent.getChildren().add(additionalResults);

        boxCaseCategoriesMenu = (HBox) root.lookup("#boxCaseCategoriesMenu");
        boxCaseCategoriesMenu.getChildren().add(new CaseCateoryCombobox());
        boxCaseCategoriesMenu.getChildren().add(new CaseCategoryReloadButton());
        
        tpCategoryData.getStyleClass().add("analyser-tab-pane");
        AnchorPane.setBottomAnchor(tpCategoryData, 0.0);
        AnchorPane.setTopAnchor(tpCategoryData, 0.0);
        AnchorPane.setLeftAnchor(tpCategoryData, 0.0);
        AnchorPane.setRightAnchor(tpCategoryData, 0.0);
        setUpCaseDisplay(null);
        return root;
    }

    private TCase loadCaseData() {
        CaseCategoryItem item = getSelectedItem();
        if (item == null) {
            return null;
        }
        if (item.isExample()) {
            return item.getHospitalCase();
        }
        return getSkinnable().getCase(item.getAnalyserCase());
    }

    private void handleAdditionalResults(boolean pShow) {
        if (pShow) {
            showAdditionalResults();
        } else {
            hideAdditionalResults();
        }
    }

    private void showAdditionalResults() {
        btnResultExpand.setTooltip(new Tooltip("Zusätzliche Ergebnisse ausblenden"));
//        if (!boxResultContainer.getChildren().contains(boxAddResultContainer)) {
//            boxResultContainer.getChildren().add(boxAddResultContainer);
//            additionalResults.setAdditionalResult(getSkinnable().getAnalyserResult());
//        }
        if (!boxResultContainer.getChildren().contains(apAddResultContainer)) {
            boxResultContainer.getChildren().add(apAddResultContainer);
            additionalResults.setAdditionalResult(getSkinnable().getAnalyserResult());
        }
    }

    private void hideAdditionalResults() {
        btnResultExpand.setTooltip(new Tooltip("Zusätzliche Ergebnisse einblenden"));
//        if (boxResultContainer.getChildren().contains(boxAddResultContainer)) {
//            boxResultContainer.getChildren().remove(boxAddResultContainer);
//        }
        if (boxResultContainer.getChildren().contains(apAddResultContainer)) {
            boxResultContainer.getChildren().remove(apAddResultContainer);
            additionalResults.setAdditionalResult(null);
        }
    }

    private void reloadSelectedTab() {
        Tab selected = tpCaseData.getSelectionModel().getSelectedItem();
        if (selected instanceof AsyncAnalyserAttributesTab) {
            ((AsyncAnalyserAttributesTab) selected).reload();
        }
    }

    private void setUpCaseDisplay(TreeItem<CaseCategoryItem> pTreeItem) {
        clearCaseDisplay();
        if (pTreeItem == null || pTreeItem.getValue() == null) {
            if(apTabDataContainer.getChildren().contains(apCaseDataContainer)){
                apTabDataContainer.getChildren().remove(apCaseDataContainer);
            }
            if(apTabDataContainer.getChildren().contains(tpCategoryData)){
               apTabDataContainer.getChildren().remove(tpCategoryData);
           }
//            boxCaseDataMenu.setDisable(true);
           return;
        }
        CaseCategoryItem pItem =  pTreeItem.getValue();
        if (!pItem.isContainer()) {
            btnResultExpand.setDisable(false);
//            tpCaseData.setDisable(false);
//           boxCaseDataMenu.setVisible(false);
                if(apTabDataContainer.getChildren().contains(tpCategoryData)){
                    apTabDataContainer.getChildren().remove(tpCategoryData);
                }
                
                if(!apTabDataContainer.getChildren().contains(apCaseDataContainer)){
                    apTabDataContainer.getChildren().add(apCaseDataContainer);
                }
            setName(pItem);
            Tab selected = tpCaseData.getSelectionModel().getSelectedItem();
            if (selected instanceof AsyncAnalyserAttributesTab) {
                ((AsyncAnalyserAttributesTab) selected).reload();
            }
        } else {
//            tpCaseData.setDisable(true);
            btnResultExpand.setDisable(true);
            getSkinnable().setShowAdditionalResults(false);
            if(apTabDataContainer.getChildren().contains(apCaseDataContainer)){
                apTabDataContainer.getChildren().remove(apCaseDataContainer);
            }
//            boxCaseDataMenu.setDisable(true);
            if(nodeHasCasesOnly(pTreeItem)){
                if(!apTabDataContainer.getChildren().contains(tpCategoryData)){
                    apTabDataContainer.getChildren().add(tpCategoryData);
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tpCategoryData.setHistoryCases(getCasesFromTreeItem(pTreeItem)); 
                    }
                });
                
            }else{
                if(apTabDataContainer.getChildren().contains(tpCategoryData)){
                    apTabDataContainer.getChildren().remove(tpCategoryData);
                }
                
            }
//            Tab selected = tpCaseData.getSelectionModel().getSelectedItem();
//            if (selected instanceof AsyncAnalyserAttributesTab) {
//                ((AsyncAnalyserAttributesTab) selected).clearContent();
//            }
        }
    }
    
    private List<TCase> getCasesFromTreeItem(TreeItem<CaseCategoryItem> pTreeItem){
        List<TCase> cases = new ArrayList<>();
        if(pTreeItem == null || pTreeItem.isLeaf()){
            return cases;
        }
        ObservableList<TreeItem<CaseCategoryItem>> children = pTreeItem.getChildren();
        boolean ret = true;
        for(TreeItem<CaseCategoryItem> child:children){
            if(child.getValue().isContainer() || child.getValue().getHospitalCase() == null){
                continue;
            }
            TCase cs = child.getValue().isExample()?child.getValue().getHospitalCase():getSkinnable().getCase(child.getValue().getAnalyserCase());
//            cs = getSkinnable().getOnLoadHospitalCaseGrouped().call(cs);
            cases.add(cs);
        }
        return cases;
        
    }

    private void setName(CaseCategoryItem pItem) {
        if (pItem.isExample()) {
            lblCaseName.setText(pItem.getHospitalCase().getCsCaseNumber());
            btnEditName.setVisible(false);
            btnEditName.setOnAction((ActionEvent event) -> {
            });
            btnCopyAndSaveCase.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.COPY));
            btnCopyAndSaveCase.setOnAction(new CopyExampleCase(pItem.getHospitalCase()));
            btnCopyAndSaveCase.setVisible(true);
            btnRevert.setVisible(true);
            btnRevert.setOnAction(new RevertExampleCase(pItem));
        } else {
            lblCaseName.setText(pItem.getAnalyserCase().getName());
            btnEditName.setVisible(true);
            btnEditName.setOnAction(new EditAnalyserCaseNameHandler(pItem));
            btnCopyAndSaveCase.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.SAVE));
            btnCopyAndSaveCase.setOnAction(new SaveAnalyserCaseContent(pItem.getAnalyserCase()));
            btnCopyAndSaveCase.setVisible(true);
            btnRevert.setVisible(false);
            btnRevert.setOnAction(null);
        }
    }

    private void clearCaseDisplay() {
        lblCaseName.setText("");
        btnEditName.setVisible(false);
        btnCopyAndSaveCase.setVisible(false);
        btnRevert.setVisible(false);
        getSkinnable().setAnalyserResult(null);
//        tpCaseData.setDisable(true);
    }

    private Map<String, TreeItem<CaseCategoryItem>> getExpandedMap() {
        Map<String, TreeItem<CaseCategoryItem>> map = new HashMap<>();
        TreeItem<CaseCategoryItem> root = tvCaseCategories.getRoot();
        if (root == null) {
            return map;
        }
        for (TreeItem<CaseCategoryItem> child : root.getChildren()) {
            map.putAll(getExpandedChildMap(child));
        }
        return map;
    }

    private Map<String, TreeItem<CaseCategoryItem>> getExpandedChildMap(TreeItem<CaseCategoryItem> pItem) {
        Map<String, TreeItem<CaseCategoryItem>> map = new HashMap<>();
        for (TreeItem<CaseCategoryItem> child : pItem.getChildren()) {
            if (!child.getChildren().isEmpty()) {
                map.putAll(getExpandedChildMap(child));
            }
            if (child.isExpanded()) {
                map.put(child.getValue().getText(), child);
            }
        }
        return map;
    }

    private void setUpCaseCategories() {
        Map<String, TreeItem<CaseCategoryItem>> extendedMap = getExpandedMap();
        TreeItem<CaseCategoryItem> selected = tvCaseCategories.getSelectionModel().getSelectedItem();
        if (tvCaseCategories.getRoot() != null) {
            tvCaseCategories.getRoot().getChildren().clear();
        }
//        HashMap<String, TreeItem<CaseCategoryItem>> categoryMap = new HashMap<>();
        TreeItem<CaseCategoryItem> root = new TreeItem<>();
        tvCaseCategories.setRoot(root);
        tvCaseCategories.setCellFactory(new Callback<TreeView<CaseCategoryItem>, TreeCell<CaseCategoryItem>>() {
            @Override
            public TreeCell<CaseCategoryItem> call(TreeView<CaseCategoryItem> param) {
                return new TreeCell<CaseCategoryItem>() {
                    @Override
                    protected void updateItem(CaseCategoryItem item, boolean empty) {
                        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                        if (item == null || empty) {
                            setText("");
                            return;
                        }
                        setText(item.getText());
                        if (item.isContainer()) {
                            int childs = getTreeItem().getChildren().size();
                            setText(getText() + " (" + childs + ")");
                        }
                    }
                };
            }
        });
        TreeItem<CaseCategoryItem> preConstructed = new TreeItem<>(new CaseCategoryItem("Beispiele"));
        preConstructed.getValue().setContainer(Boolean.TRUE);
        preConstructed.getValue().setDeletable(Boolean.FALSE);
        root.getChildren().add(preConstructed);
        for (TCase cse : PreDefinedCases.instance().getPreDefinedCases()) {
            TreeItem<CaseCategoryItem> item = new TreeItem<>();
            item.setValue(new CaseCategoryItem(cse.getCsCaseNumber(), cse));
            item.getValue().setDeletable(Boolean.FALSE);
            preConstructed.getChildren().add(item);
        }
        preConstructed.setExpanded(true);

        List<CCase> caseList = getSkinnable().getCaseList();
        if (caseList.isEmpty()) {
            return;
        }
        TreeItem<CaseCategoryItem> ownCases = new TreeItem<>(new CaseCategoryItem("Kopien"));
        ownCases.getValue().setContainer(Boolean.TRUE);
        ownCases.setExpanded(extendedMap.containsKey(ownCases.getValue().getText()));
//        checkSelected(ownCases, selected);
        root.getChildren().add(ownCases);
        ownCases.getChildren().addAll(getAnalyserCaseTreeItems(caseList, extendedMap));
        ownCases.setExpanded(true);

        restoreSelection(selected);
    }

    private void restoreSelection(TreeItem<CaseCategoryItem> pItem) {
        for (TreeItem<CaseCategoryItem> child : tvCaseCategories.getRoot().getChildren()) {
            restoreSelection(child, pItem);
        }
    }

    private void restoreSelection(TreeItem<CaseCategoryItem> pRoot, TreeItem<CaseCategoryItem> pItem) {
        for (TreeItem<CaseCategoryItem> child : pRoot.getChildren()) {
            if (checkSelected(child, pItem)) {
                return;
            }
            if (!child.getChildren().isEmpty()) {
                restoreSelection(child, pItem);
            }
        }
    }

    private boolean checkSelected(TreeItem<CaseCategoryItem> pItem, TreeItem<CaseCategoryItem> pSelected) {
        if (pItem == null || pSelected == null) {
            return false;
        }
        if (pItem.getValue().getText() == null) {
            return false;
        }
        if (pItem.getValue().getText().equals(pSelected.getValue().getText())) {
            tvCaseCategories.getSelectionModel().select(pItem);
            return true;
        }
        return false;
    }

    private void setUpAnalyserResult(TransferRuleAnalyseResult pAnalyserResult) {
        if (!tvTestResult.getRoot().getChildren().isEmpty()) {
            tvTestResult.getRoot().getChildren().clear();
        }
//        if (getSkinnable().isShowAdditionalResults()) {
        additionalResults.setAdditionalResult(pAnalyserResult);
//        }
        if (pAnalyserResult == null) {
            if(boxTestResult.getChildren().contains(errorMessagePane)){
                boxTestResult.getChildren().remove(errorMessagePane);
            }
            if(!boxTestResult.getChildren().contains(resultPane)){
                boxTestResult.getChildren().add(0, resultPane);
            }
            return;
        }
        getSkinnable().setAnalyserResult(pAnalyserResult);
        LOG.info("update analyser result!");
        if(pAnalyserResult.getCaseValidationGroupErrList() != null && !pAnalyserResult.getCaseValidationGroupErrList().isEmpty()){
            if(boxTestResult.getChildren().contains(resultPane)){
                boxTestResult.getChildren().remove(0);
            }
            if(!boxTestResult.getChildren().contains(errorMessagePane)){
                boxTestResult.getChildren().add(0, errorMessagePane);
            }
            errorMessage.setText(createErrorMessage(pAnalyserResult.getCaseValidationGroupErrList()));

        }else{
            if(boxTestResult.getChildren().contains(errorMessagePane)){
                boxTestResult.getChildren().remove(errorMessagePane);
            }
            if(!boxTestResult.getChildren().contains(resultPane)){
                boxTestResult.getChildren().add(0, resultPane);
            }
            tvTestResult.getRoot().getChildren().add(createAnalyserResultItem(pAnalyserResult.getRuleResult()));
        }
    }

    private TreeItem<TransferRuleResult> createAnalyserResultItem(TransferRuleResult pResult) {
        TreeItem<TransferRuleResult> item = new TreeItem<>(pResult);
        item.setExpanded(true);
        if (pResult == null) {
            return item;
        }
        if (pResult.getChildren() == null) {
            return item;
        }
        for (TransferRuleResult res : pResult.getChildren()) {
//            if(res.getMark() == null){
//                continue;
//            }
            item.getChildren().add(createAnalyserResultItem(res));
        }
        return item;
    }

    private List<TreeItem<CaseCategoryItem>> getAnalyserCaseTreeItems(List<CCase> pAnalyserCases, Map<String, TreeItem<CaseCategoryItem>> pMap) {
        switch (getSkinnable().getAnalyserCaseCompareMode()) {
            case CASENUMBER:
                return getTreeItemsForCaseNumber(pAnalyserCases, pMap);
            case DATE:
                return getTreeItemsForDate(pAnalyserCases, pMap);
            case CATEGORY:
                return getTreeItemsForCategory(pAnalyserCases, pMap);
            default:
                return getTreeItemsForCaseNumber(pAnalyserCases, pMap);
        }
    }

    private List<TreeItem<CaseCategoryItem>> getTreeItemsForCaseNumber(List<CCase> pAnalyserCases, Map<String, TreeItem<CaseCategoryItem>> pMap) {
        ArrayList<CCase> cases = new ArrayList<>(pAnalyserCases);
        cases.sort(Comparator.comparing(CCase::getCsCaseNumber));
        Map<String, TreeItem<CaseCategoryItem>> items = new HashMap<>();

        for (CCase cse : cases) {
            String leadingNumber = String.valueOf(cse.getCsCaseNumber().charAt(0));
            if (!items.containsKey(leadingNumber)) {
                TreeItem<CaseCategoryItem> item = new TreeItem<>();
                item.setValue(new CaseCategoryItem(leadingNumber));
                item.getValue().setContainer(Boolean.TRUE);
                item.setExpanded(pMap.containsKey(item.getValue().getText()));
                items.put(leadingNumber, item);
            }
            TreeItem<CaseCategoryItem> item = new TreeItem<>();
            item.setValue(new CaseCategoryItem(cse.getCsCaseNumber(), cse));
            items.get(leadingNumber).getChildren().add(item);
        }
        return items.values().stream().sorted(new Comparator<TreeItem<CaseCategoryItem>>() {
            @Override
            public int compare(TreeItem<CaseCategoryItem> o1, TreeItem<CaseCategoryItem> o2) {
                return o2.getValue().getText().compareTo(o1.getValue().getText());
            }
        }).collect(Collectors.toList());
    }

    private List<TreeItem<CaseCategoryItem>> getTreeItemsForDate(List<CCase> pAnalyserCases, Map<String, TreeItem<CaseCategoryItem>> pMap) {
        ArrayList<CCase> cases = new ArrayList<>(pAnalyserCases);
        cases.sort(Comparator.comparing(CCase::getCreationDate).reversed());
        Map<String, TreeItem<CaseCategoryItem>> items = new HashMap<>();

        for (CCase cse : cases) {
            String date = Lang.toDate(cse.getCreationDate());
            if (!items.containsKey(date)) {
                TreeItem<CaseCategoryItem> item = new TreeItem<>();
                item.setValue(new CaseCategoryItem(date));
                item.getValue().setContainer(Boolean.TRUE);
                item.setExpanded(pMap.containsKey(item.getValue().getText()));
                items.put(date, item);
            }

            TreeItem<CaseCategoryItem> item = new TreeItem<>();
            item.setValue(new CaseCategoryItem(cse.getName() != null ? cse.getName() : "", cse));
            items.get(date).getChildren().add(item);
        }
        return items.values().stream().sorted(new Comparator<TreeItem<CaseCategoryItem>>() {
            @Override
            public int compare(TreeItem<CaseCategoryItem> o1, TreeItem<CaseCategoryItem> o2) {
                return o2.getValue().getText().compareTo(o1.getValue().getText());
            }
        }).collect(Collectors.toList());
    }

    private List<TreeItem<CaseCategoryItem>> getTreeItemsForCategory(List<CCase> pAnalyserCases, Map<String, TreeItem<CaseCategoryItem>> pMap) {
        ArrayList<CCase> cases = new ArrayList<>(pAnalyserCases);
        cases.sort(Comparator.comparing(CCase::getCreationDate).reversed());
        Map<String, TreeItem<CaseCategoryItem>> items = new HashMap<>();

        for (CCase cse : cases) {
            String category = cse.getCategory() != null ? cse.getCategory().toLowerCase() : Lang.getNoCategory();
            if (!items.containsKey(category)) {
                TreeItem<CaseCategoryItem> item = new TreeItem<>();
                item.setValue(new CaseCategoryItem(cse.getCategory()));
                item.getValue().setContainer(Boolean.TRUE);
                item.setExpanded(pMap.containsKey(item.getValue().getText()));
                items.put(category, item);
            }

            TreeItem<CaseCategoryItem> item = new TreeItem<>();
            item.setValue(new CaseCategoryItem(cse.getName() != null ? cse.getName() : "", cse));
            items.get(category).getChildren().add(item);
        }
        return items.values().stream().sorted(new Comparator<TreeItem<CaseCategoryItem>>() {
            @Override
            public int compare(TreeItem<CaseCategoryItem> o1, TreeItem<CaseCategoryItem> o2) {
                return o2.getValue().getText().compareTo(o1.getValue().getText());
            }
        }).collect(Collectors.toList());
    }

    private ContextMenu getCaseCategoriesContextMenu(TreeView<CaseCategoryItem> pTreeView) {
        ContextMenu menu = new ContextMenu();
        pTreeView.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        menu.getItems().clear();
                        MenuItem delete = new MenuItem();

                        TreeItem<CaseCategoryItem> selected = pTreeView.getSelectionModel().getSelectedItem();
                        if (selected == null) {
                            return;
                        }
                        delete.setText(selected.getValue().isContainer() ? "Alle Fälle löschen" : "Fälle löschen");
                        delete.setDisable(selected.getValue().isDeletable() ? Boolean.FALSE : Boolean.TRUE);
                        delete.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                List<Long> cases = getAnalyserCasesToDelete(selected);
                                DeleteAnalyserCaseTask task = new DeleteAnalyserCaseTask(cases) {
                                    @Override
                                    public Boolean call() {
                                        return getSkinnable().getOnDeleteAnalyserCases().call(cases);
                                    }

                                    @Override
                                    public void afterTask(Worker.State pState) {
                                        super.afterTask(pState);
                                        if (Worker.State.SUCCEEDED.equals(pState)) {
                                            if (getValue()) {
                                                reloadAnalyserCases();
                                            }
                                        }
                                    }

                                };
                                task.start();
                            }
                        });
                        menu.getItems().add(delete);
                        menu.show(pTreeView, event.getScreenX() + 10, event.getScreenY() + 10);
                    }
                });

            }
        });
        return menu;

    }

    private List<Long> getAnalyserCasesToDelete(TreeItem<CaseCategoryItem> pTreeItem) {
        List<Long> cases = new ArrayList<>();
        if (pTreeItem == null || pTreeItem.getValue() == null) {
            return cases;
        }
        CaseCategoryItem item = pTreeItem.getValue();
        if (!item.isContainer()) {
            cases.add(item.getAnalyserCase().getId());
            return cases;
        }
        for (TreeItem<CaseCategoryItem> child : pTreeItem.getChildren()) {
            cases.addAll(getAnalyserCasesToDelete(child));
        }
        return cases;
    }

    public boolean nodeHasCasesOnly(TreeItem<CaseCategoryItem> pTreeItem) {
        if(pTreeItem == null || pTreeItem.isLeaf()){
            return false;
        }
        ObservableList<TreeItem<CaseCategoryItem>> children = pTreeItem.getChildren();
        boolean ret = true;
        for(TreeItem<CaseCategoryItem> child:children){
            if(child.getValue().isContainer()){
                return false;
            }
        }
        return true;
    }
    
    public List<TCase> getHistoryCases(){
        //TODO get list from HistoryDataTabPane when it is visible and selected!!!
         if(apTabDataContainer.getChildren().contains(tpCategoryData)){
            return tpCategoryData.getHistoryCases();
         }
         return new ArrayList<>();

    }

    private String createErrorMessage(List<CaseValidationGroupErrList> caseValidationGroupErrList) {
        if(caseValidationGroupErrList == null || caseValidationGroupErrList.isEmpty()){
            return "";
            
        }
        List<String> errors = new ArrayList<>();
       for(CaseValidationGroupErrList err:caseValidationGroupErrList){
           if(err.equals(CaseValidationGroupErrList.ADMISSION_DATE_FOR_2013)){
              errors.add(err.getTranslation(GDRGModel.getMinModelYear()).getValue() +( err.isSevere()?"; \nGroupen und Regelanwendung wird nicht unterstützt":""));

           }else{
                errors.add(err.getTranslation().getValue());
           }
       }
       return String.join(",\n" , errors);
    }

 
    private class AnalyserResultCell extends TreeCell<TransferRuleResult> {

        //private PseudoClass test_result_pos = PseudoClass.getPseudoClass("test-result_pos");
        private PseudoClass testResultNeg = PseudoClass.getPseudoClass("test-result_neg");

        public AnalyserResultCell() {
            super();
            getStyleClass().add(0, "rule-tester-cell");
        }

        @Override
        protected void updateItem(TransferRuleResult item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setStyle("");
                setText("");
                return;
            }
            boolean validate = item.isResult();
            if (validate) {
                setStyle("-fx-background-color:-green03");
            } else {
                setStyle("-fx-background-color:#f39797;");
//                setStyle("-fx-background-color:lightcoral");
            }
            pseudoClassStateChanged(testResultNeg, validate);
            setText(item.getTerm());
        }
    }

    private class CaseCategoryItem {

        private String text;
        private TCase hospitalCase;
        private CCase analyserCase;
        private Boolean deletable = true;
        private Boolean container = false;
        private Boolean example = false;

        public CaseCategoryItem(String text) {
            this.text = text;
        }

        public CaseCategoryItem(String pText, CCase pAnalyserCase) {
            this.text = pText;
            this.analyserCase = pAnalyserCase;
//            this.value = new ObjectMapper().readValue(value.getContent(), TCase.class);
//            this.value = value;
        }

        public CaseCategoryItem(String text, TCase hospitalCase) {
            this.text = text;
            this.hospitalCase = hospitalCase;
            this.example = true;
        }

        public String getText() {
            return text;
        }

        public TCase getHospitalCase() {
            if (hospitalCase == null) {
                hospitalCase = getSkinnable().getOnLoadHospitalCase().call(analyserCase);
            }
            return hospitalCase;
        }

        public void setHospitalCase(TCase pCase) {
            hospitalCase = pCase;
        }

        public CCase getAnalyserCase() {
            return analyserCase;
        }

        public Boolean isDeletable() {
            return deletable;
        }

        public void setDeletable(Boolean deletable) {
            this.deletable = deletable;
        }

        public Boolean isContainer() {
            return container;
        }

        public void setContainer(Boolean container) {
            this.container = container;
        }

        public Boolean isExample() {
            return this.example;
        }

        private void setText(String text) {
            this.text = text;
        }

    }

    private void reloadAnalyserCases() {
        List<CCase> analyserCase = getSkinnable().getOnReloadAnalyserCases().call(Boolean.TRUE);
        if (analyserCase != null) {
            getSkinnable().getCaseList().setAll(analyserCase);
        }
    }

    private void updateAnalyserCaseName(String pName) {
        lblCaseName.setText(pName);
        tvCaseCategories.refresh();
    }

    private class CaseCategoryReloadButton extends ReloadButton {

        public CaseCategoryReloadButton() {
            super();
            setTooltip(new Tooltip("Fälle neu laden"));
            setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    reloadAnalyserCases();
                }
            });
        }
    }

    private class CaseCateoryCombobox extends ComboBox<RuleAnalyser.AnalyserCaseCompareMode> {

        public CaseCateoryCombobox() {
            super(FXCollections.observableArrayList(RuleAnalyser.AnalyserCaseCompareMode.values()));
            setConverter(new StringConverter<RuleAnalyser.AnalyserCaseCompareMode>() {
                @Override
                public String toString(RuleAnalyser.AnalyserCaseCompareMode object) {
                    if (object == null) {
                        return null;
                    }
                    return object.getText();
                }

                @Override
                public RuleAnalyser.AnalyserCaseCompareMode fromString(String string) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
            getSelectionModel().select(getSkinnable().getAnalyserCaseCompareMode());
            getSkinnable().analyserCaseCompareModeProperty().addListener(new ChangeListener<RuleAnalyser.AnalyserCaseCompareMode>() {
                @Override
                public void changed(ObservableValue<? extends RuleAnalyser.AnalyserCaseCompareMode> observable, RuleAnalyser.AnalyserCaseCompareMode oldValue, RuleAnalyser.AnalyserCaseCompareMode newValue) {
                    getSelectionModel().select(newValue);
                }
            });
            getSelectionModel().selectedItemProperty().addListener(new ChangeListener<RuleAnalyser.AnalyserCaseCompareMode>() {
                @Override
                public void changed(ObservableValue<? extends RuleAnalyser.AnalyserCaseCompareMode> observable, RuleAnalyser.AnalyserCaseCompareMode oldValue, RuleAnalyser.AnalyserCaseCompareMode newValue) {
//                    setUpCaseCategories();
                    getSkinnable().setAnalyserCaseCompareMode(newValue);
                }
            });

        }
    }

    private class CopyExampleCase implements EventHandler<ActionEvent> {

        private final TCase hospitalCase;

        public CopyExampleCase(TCase pCase) {
            hospitalCase = pCase;
        }

        @Override
        public void handle(ActionEvent event) {
            CopyCaseToCommonTask task = new CopyCaseToCommonTask(hospitalCase) {
                @Override
                public void afterTask(Worker.State pState) {
                    super.afterTask(pState); //To change body of generated methods, choose Tools | Templates.
                    if (Worker.State.SUCCEEDED.equals(pState)) {
                        setUpCaseCategories();
                    }
                }

            };
            task.start();
        }
    }

    private class RevertExampleCase implements EventHandler<ActionEvent> {

        private final CaseCategoryItem hospitalCase;

        public RevertExampleCase(CaseCategoryItem pCase) {
            hospitalCase = pCase;
        }

        @Override
        public void handle(ActionEvent event) {
            if (!hospitalCase.isExample()) {
                MainApp.showWarningMessageDialog("Zurücksetzen wird für diesen Eintrag nicht unterstützt!");
            }
            ConfirmDialog dialog = new ConfirmDialog(MainApp.getWindow(), "Soll der Fall mit der Nummer: " + hospitalCase.getText() + " wirklich zurückgesetzt werden?\n"
                    + "Alle Änderungen gehen verloren!");
            dialog.showAndWait().ifPresent(new Consumer<ButtonType>() {
                @Override
                public void accept(ButtonType t) {
                    if (ButtonType.YES.equals(t)) {
                        TCase newCase = PreDefinedCases.instance().getTestCase(hospitalCase.getText());
                        if (newCase == null) {
                            MainApp.showWarningMessageDialog("Fall mit der Nummer: " + hospitalCase.getText() + " konnte nicht zurück gesetzt werden");
                            return;
                        }
                        hospitalCase.setHospitalCase(newCase);
                        tvCaseCategories.refresh();
                        reloadSelectedTab();
                        resultPane.reload();
                    }
                }
            });
        }
    }

    private class SaveAnalyserCaseContent implements EventHandler<ActionEvent> {

        private final CCase analyserCase;

        public SaveAnalyserCaseContent(CCase pCase) {
            analyserCase = pCase;
        }

        @Override
        public void handle(ActionEvent event) {
            getSkinnable().getCase(analyserCase);
            SaveAnalyserCaseContentTask task = new SaveAnalyserCaseContentTask() {
                @Override
                public void afterTask(Worker.State pState) {
                    super.afterTask(pState); //To change body of generated methods, choose Tools | Templates.
                    if (Worker.State.SUCCEEDED.equals(pState)) {
                        if (getValue()) {
                            setUpCaseCategories();
                        } else {
                            MainApp.showErrorMessageDialog("Falldaten konnten nicht gespeichert werden");
                        }
                    }
                }

                @Override
                public Boolean call() {
                    return getSkinnable().getOnSaveHospitalCase().call(analyserCase);
                }

            };
            task.start();
        }

    }

    private class EditAnalyserCaseNameHandler implements EventHandler<ActionEvent> {

        private final CCase analyserCase;
        private PopOver over;
        private final CaseCategoryItem item;

        public EditAnalyserCaseNameHandler(CaseCategoryItem pItem) {
            item = pItem;
            analyserCase = pItem.getAnalyserCase();
        }

        @Override
        public void handle(ActionEvent event) {
            if (over == null) {
                LabeledTextField lblName = new LabeledTextField("Name", 50);
                lblName.getStyleClass().add("attribute-item");
                lblName.setText(analyserCase.getName());
                lblName.focusedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        if (!newValue) {
                            getSkinnable().getOnSaveAnalyserCase().call(analyserCase);
                        }
                    }
                });
                lblName.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        if (KeyCode.ENTER.equals(event.getCode())) {
                            getSkinnable().getOnSaveAnalyserCase().call(analyserCase);
                        }
                    }
                });
                lblName.getControl().textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        analyserCase.setName(newValue);
                        item.setText(newValue);
                        updateAnalyserCaseName(newValue);
                        //setUpCaseCategories();
                    }
                });
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        lblName.getControl().selectAll();
                    }
                });
                VBox box = new VBox(5, lblName);
                box.getStyleClass().add("attribute-menu");
                box.setPadding(new Insets(5.0));
                over = new PopOver(box);
//                over.getStyleClass().add("attribute-menu");
            }
            if (!over.isShowing()) {
                over.show((Node) event.getSource());
            }
        }

    }
}
