/* 
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.easycoder;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.model.catalog.CpxIcdCatalog;
import de.lb.cpx.client.core.model.catalog.CpxOps;
import de.lb.cpx.client.core.model.catalog.CpxOpsAopCatalog;
import de.lb.cpx.client.core.model.catalog.CpxOpsCatalog;
import de.lb.cpx.client.core.model.catalog.ICpxThesaurus;
import de.lb.cpx.client.core.model.catalog.ICpxTreeItem;
import de.lb.cpx.client.core.model.fx.autocompletion.AutocompletionlTextField;
import de.lb.cpx.client.core.model.fx.combobox.LocalisationComboBox;
import de.lb.cpx.client.core.model.fx.contextmenu.CtrlContextMenu;
import de.lb.cpx.client.core.model.fx.datepicker.FormatedDatePicker;
import de.lb.cpx.client.core.model.fx.pane.AsyncPane;
import de.lb.cpx.client.core.util.clipboard.ClipboardEnabler;
import de.lb.cpx.client.core.util.code.CodeInjector;
import de.lb.cpx.client.core.util.code.TextFlowWithCopyText;
import de.lb.cpx.client.core.util.shortcut.ShortcutHandler;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.enums.LocalisationEn;
import de.lb.cpx.server.commons.dao.AbstractVersionEntity;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Callback;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.search.suggest.analyzing.AnalyzingInfixSuggester;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.NoLockFactory;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

/**
 * FXML Controller class
 *
 * ObservableList selectedDiagnoses, chosen icd ObservableList
 * selectedProcedures, chosen ops
 *
 * @author adameck
 */
public class EasyCoderDialogFXMLController extends Controller<CpxScene>{

    public static final DataFormat ICD_FORMAT = new DataFormat("icdCatalogDragDropItem");
    public static final DataFormat OPS_FORMAT = new DataFormat("opsCatalogDragDropItem");
    private static final String HINT_TEXT = "Hinweis: ";
    private static final String INCL_TEXT = "Incl.: ";
    private static final String EXCL_TEXT = "Excl.: ";
    private static final String THESAURUS_TEXT = "Thesaurus: ";
    private static final String AOP_TEXT = "AOP-Katalog ";
    private static final Logger LOG = Logger.getLogger(EasyCoderDialogFXMLController.class.getName());

    @FXML
    private Tab tabDiagnosis;
    @FXML
    private AutocompletionlTextField diagnosisTxtSearch;
    @FXML
    private TreeView<ICpxTreeItem<? extends ICpxThesaurus>> diagnosisTreeView;
    @FXML
    private ScrollPane diagnosisTxtArea;

    @FXML
    private Tab tabProcedure;
    @FXML
    private AutocompletionlTextField procedureTxtSearch;
    @FXML
    private TreeView<ICpxTreeItem<? extends ICpxThesaurus>> procedureTreeView;
    @FXML
    private ScrollPane procedureTxtArea;

    @FXML
    private Label lblDiagnosis;
    @FXML
    private Label lblProcedure;
    @FXML
    private Button btnRemoveDiagnosis;
    @FXML
    private Button btnRemoveProcedure;

    @FXML
    private ListView<TCaseIcd> tCaseIcdListView;
    @FXML
    private ListView<TCaseOps> tCaseOpsListView;
    @FXML
    private TabPane tpCategory;
    @FXML
    private HBox hbContent;

    private boolean useSpellChecker = true;
    //bool for Temp dictionary
    private final TempDictionary tempD = new TempDictionary();
    //analyzer for autocomplete
    private StandardAnalyzer analyzer;
    private Directory icdDirectory, opsDirectory, icdSpellIndexDir, opsSpellIndexDir;
    private AnalyzingInfixSuggester icdSuggester, opsSuggester;
    private String pathToTempIndex,
            pathToTempDictionary;
    private SpellChecker icdSpellChecker, opsSpellChecker;

    public ShortcutHandler getActiveFlow() {
        if( tpCategory.getSelectionModel().selectedItemProperty().getValue().equals(tabDiagnosis)){
                return (TextFlowWithCopyText )diagnosisTxtArea.getContent();
        }    else{
               return (TextFlowWithCopyText )procedureTxtArea.getContent();
        }
    }


    public enum Diagnose {
        ICD, OPS
    }
    private SpellCheckManagerClass spellCheckerManagerClass;

    private final String lang = CpxClientConfig.instance().getLanguage();
    private int year = 0;

    private final List<ICpxTreeItem<? extends ICpxThesaurus>> icdList = new ArrayList<>();
    private final List<ICpxTreeItem<? extends ICpxThesaurus>> opsList = new ArrayList<>();
    public final ObservableList<TCaseIcd> selectedTCaseIcd
            = FXCollections.observableArrayList();
    public final ObservableList<TCaseOps> selectedTCaseOps
            = FXCollections.observableArrayList();

    private Timer searchTimer = new Timer();

    private static final String[][] chars = new String[][]{{"&&", ",,", "  ", ".", "-", "/"}, {"&", ",", " ", "", "", ""}};
//    private static final String[][] sChars = new String[][]{{".", "-", "/"}, {"", "", ""}};

    private final List<List<String>> nExprList = new ArrayList<>();
    private final List<List<Integer>> fixedExprIndexes = new ArrayList<>();

    /**
     * Initializes the controller class.
     *
     * @param url url
     * @param rb bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    /**
     *
     * @param pEvent
     * @return
     */
    public void init(final int pYear) {
        LOG.log(Level.FINE, "EasyCoder is starting to initialize with year " + pYear + "...");
        if (pYear == 0) {
            throw new IllegalArgumentException("EasyCoder cannot be initialized without a year");
        }
        final int[] grpModelRes = GDRGModel.getAvailableModelsFromYear(pYear, false);
        if (grpModelRes == null || grpModelRes.length == 1) {
            LOG.log(Level.WARNING, "EasyCoder is initialized with an invalid year. There is no grouper available for year " + pYear);
        }
        this.year = pYear;

        AsyncPane<TabPane> asyncPane = new AsyncPane<>() {
            @Override
            public TabPane loadContent() {
                try {
//                    initTexts();
                    initData();
                    initDiagnosisTreeView();
                    initProcedureTreeView();
                    initTCaseIcdListView();
                    initTCaseOpsListView();
                    initSpellChecker();
                    initSearch();
                } catch (IOException ex) {
                    Logger.getLogger(EasyCoderDialogFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }
                return tpCategory;
            }
        };
        asyncPane.setMinWidth(592);
        hbContent.getChildren().remove(tpCategory);
        hbContent.getChildren().add(0, asyncPane);
        initTexts();
//        initData();
//        initDiagnosisTreeView();
//        initProcedureTreeView();
//        initTCaseIcdListView();
//        initTCaseOpsListView();
//        initSpellChecker();
//        initSearch();
    }

    /**
     * Initialize all Texts
     */
    private void initTexts() {
        lblDiagnosis.setText(Lang.getCatalogDialogDiagnoses());
        lblProcedure.setText(Lang.getCatalogDialogProcedures());
        tabDiagnosis.setText(Lang.getCatalogDialogDiagnoses());
        tabProcedure.setText(Lang.getCatalogDialogProcedures());
        diagnosisTxtSearch.setPromptText(Lang.getCatalogDialogSearch());
        procedureTxtSearch.setPromptText(Lang.getCatalogDialogSearch());
        GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");
        btnRemoveProcedure.setGraphic(fontAwesome.create("\uf1f8"));
        btnRemoveProcedure.setText(Lang.getCatalogDialogRemoveAll());
        btnRemoveDiagnosis.setGraphic(fontAwesome.create("\uf1f8"));
        btnRemoveDiagnosis.setText(Lang.getCatalogDialogRemoveAll());
    }

    /**
     * Initialize Data (For the TreeViews)
     */
    private void initData() {
        icdList.clear();
        opsList.clear();
        icdList.addAll(CpxIcdCatalog.instance().getAll(lang, year).values());
        opsList.addAll(CpxOpsCatalog.instance().getAll(lang, year).values());

        if (icdList.isEmpty()) {
            LOG.log(Level.WARNING, "EasyCoder did not find ICD entries for year " + year);
        }
        if (opsList.isEmpty()) {
            LOG.log(Level.WARNING, "EasyCoder did not find OPS entries for year " + year);
        }
        Comparator<ICpxTreeItem<? extends ICpxThesaurus>> sortIcdByCode = new Comparator<>() {
            @Override
            public int compare(ICpxTreeItem<? extends ICpxThesaurus> o1, ICpxTreeItem<? extends ICpxThesaurus> o2) {
                return o1.getCode().compareToIgnoreCase(o2.getCode());
            }
        };
        icdList.sort(sortIcdByCode);
        Comparator<ICpxTreeItem<? extends ICpxThesaurus>> sortOpsByCode = new Comparator<>() {
            @Override
            public int compare(ICpxTreeItem<? extends ICpxThesaurus> o1, ICpxTreeItem<? extends ICpxThesaurus> o2) {
                return o1.getCode().compareToIgnoreCase(o2.getCode());
            }
        };
        opsList.sort(sortOpsByCode);
    }

    private ICpxTreeItem<? extends ICpxThesaurus> findCode(List<? extends ICpxTreeItem<? extends ICpxThesaurus>> pList, final String pCode) {
        if (pList.isEmpty()) {
            return null;
        }
        for (ICpxTreeItem<? extends ICpxThesaurus> item : new ArrayList<>(pList)) {
            if (item.getCode().equalsIgnoreCase(pCode)) {
                return item;
            }
            ICpxTreeItem<? extends ICpxThesaurus> it = findCode(item.getChildren(), pCode);
            if (it != null) {
                return it;
            }
        }
        return null;
    }

    public static TreeItem<ICpxTreeItem<? extends ICpxThesaurus>> getTreeViewItem(TreeItem<ICpxTreeItem<? extends ICpxThesaurus>> item, ICpxTreeItem<? extends ICpxThesaurus> value) {
        if (item == null) {
            return null;
        }
        if (item.getValue() == value) {
            return item;
        }

        for (TreeItem<ICpxTreeItem<? extends ICpxThesaurus>> child : item.getChildren()) {
            TreeItem<ICpxTreeItem<? extends ICpxThesaurus>> s = getTreeViewItem(child, value);
            if (s != null) {
                return s;
            }

        }
        return null;
    }

    public TreeItem<ICpxTreeItem<? extends ICpxThesaurus>> focusCode(List<ICpxTreeItem<? extends ICpxThesaurus>> pList, TreeView<ICpxTreeItem<? extends ICpxThesaurus>> pTree, final String pCode) {
        String code = pCode;
        code = code.replace("*", "");
        code = code.replace("!", "");
        code = code.replace("+", "");
        ICpxTreeItem<? extends ICpxThesaurus> item = findCode(pList, code);
        if (item == null) {
            item = findCode(pList, pCode + ".-");
        }
        if (item == null && code.endsWith("-")) {
            item = findCode(pList, pCode.substring(0, pCode.length() - 1));
        }
        TreeItem<ICpxTreeItem<? extends ICpxThesaurus>> treeIt = null;
        if (item != null) {
            if (pTree == diagnosisTreeView) {
                initDiagnosisTreeView();
            } else if (pTree == procedureTreeView) {
                initProcedureTreeView();
            }

            for (TreeItem<ICpxTreeItem<? extends ICpxThesaurus>> c : pTree.getRoot().getChildren()) {
                TreeItem<ICpxTreeItem<? extends ICpxThesaurus>> it = getTreeViewItem(c, item);
                if (it != null) {
                    treeIt = it;
                    break;
                }
            }
            //item.setVisible(true);
        }

        if (treeIt != null) {
            treeIt.setExpanded(true);
//            pTree.getSelectionModel().s
//                    pTree.getSelectionModel().select(treeIt);
//            int index = pTree.getRow(treeIt);
            pTree.getSelectionModel().select(treeIt);
            pTree.requestFocus();
            int index = pTree.getRow(treeIt);
            pTree.scrollTo(index - (index > 3 ? 3 : index)); //show item before too, so it is index - something
        } else {
            LOG.log(Level.WARNING, "Was not able to find this code: " + pCode);
        }

        //MainApp.showWarningMessageDialog("Der Code '" + pCode + "' konnte nicht gefunden werden!");
        return treeIt;
    }

    /**
     * Initialize Dagnosis TreeView
     */
    private void initDiagnosisTreeView() {
        Controller<CpxScene> redirector = this;

        diagnosisTreeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<? extends ICpxTreeItem<? extends ICpxThesaurus>>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<? extends ICpxTreeItem<? extends ICpxThesaurus>>> observable, TreeItem<? extends ICpxTreeItem<? extends ICpxThesaurus>> oldValue, TreeItem<? extends ICpxTreeItem<? extends ICpxThesaurus>> newValue) {
                TreeItem<? extends ICpxTreeItem<? extends ICpxThesaurus>> icdItem = diagnosisTreeView.getSelectionModel().getSelectedItem();
                if (icdItem != null) {
//                    TextFlow flow = getColoredTextFlow(catalogDetailText(EXCL_TEXT, icdItem.getValue().getExclusion())
//                            + catalogDetailText(INCL_TEXT, icdItem.getValue().getInclusion())
//                            + catalogDetailText(HINT_TEXT, icdItem.getValue().getNote())
//                            + catalogDetailText(THESAURUS_TEXT, icdItem.getValue().getThesaurusString()));
                    TextFlow flow = new TextFlowWithCopyText(catalogDetailText(EXCL_TEXT, icdItem.getValue().getExclusion())
                            + catalogDetailText(INCL_TEXT, icdItem.getValue().getInclusion())
                            + catalogDetailText(HINT_TEXT, icdItem.getValue().getNote())
                            + catalogDetailText(THESAURUS_TEXT, icdItem.getValue().getThesaurusString()),
                            nExprList, fixedExprIndexes);
                    CodeInjector ci = new CodeInjector((pEvent, pCode) -> {
                        focusCode(icdList, diagnosisTreeView, pCode);
                    }, null);
                    ci.addCodeLinks(flow);
                   ( (TextFlowWithCopyText)flow).setRedirector(redirector);
                    diagnosisTxtArea.setFitToWidth(true);
                    diagnosisTxtArea.setFitToHeight(true);
                    diagnosisTxtArea.setContent(flow);
                } else {
                    diagnosisTxtArea.setContent(null);
                }
            }
        });

        diagnosisTreeView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                TreeItem<? extends ICpxTreeItem<? extends ICpxThesaurus>> icdItem = diagnosisTreeView.getSelectionModel().getSelectedItem();
                if (icdItem != null) {
                    if (event.getClickCount() == 2) {
                        if (icdItem.getValue().isCompleteFl()) {
                            setDiagnosis(icdItem.getValue());
                        }
                    }
                }
            }
        });

        diagnosisTreeView.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                TreeItem<? extends ICpxTreeItem<? extends ICpxThesaurus>> icdItem = diagnosisTreeView.getSelectionModel().getSelectedItem();
                if (icdItem != null) {
                    if (event.getCode() == KeyCode.ENTER) {
                        if (icdItem.getValue().isCompleteFl()) {
                            setDiagnosis(icdItem.getValue());
                        }
                        event.consume();
                    }
                }
            }
        });

        diagnosisTreeView.setCellFactory(new Callback<TreeView<ICpxTreeItem<? extends ICpxThesaurus>>, TreeCell<ICpxTreeItem<? extends ICpxThesaurus>>>() {
            @Override
            public TreeCell<ICpxTreeItem<? extends ICpxThesaurus>> call(TreeView<ICpxTreeItem<? extends ICpxThesaurus>> icdTreeView) {
                TreeCell<ICpxTreeItem<? extends ICpxThesaurus>> treeCell = new TreeCell<ICpxTreeItem<? extends ICpxThesaurus>>() {
                    @Override
                    protected void updateItem(ICpxTreeItem<? extends ICpxThesaurus> icdItem, boolean empty) {
                        super.updateItem(icdItem, empty);
                        if (icdItem != null) {
                            String str = icdItem.getCode() + " - " + icdItem.getDescription();
//                            TextFlow flow = getColoredTextFlow(str);
                              TextFlow flow = new TextFlowWithCopyText(str, nExprList, fixedExprIndexes);  
                            // check if the found words are just in Thesaurus and add "*" if so
                            if (flow.getChildren().size() == 1) {
                                TextFlow flowDes =  new TextFlowWithCopyText(catalogDetailText(EXCL_TEXT, icdItem.getExclusion())
                                        + catalogDetailText(INCL_TEXT, icdItem.getInclusion())
                                        + catalogDetailText(HINT_TEXT, icdItem.getNote())
                                        + catalogDetailText(THESAURUS_TEXT, icdItem.getThesaurusString()), nExprList, fixedExprIndexes);  

                                if (flowDes.getChildren().size() > 1 && flow.getChildren().size() == 1) {
                                    Text text2 = new Text("<*>");
                                    text2.setStyle("-fx-wrap-text: true; -fx-font-weight: bold;");
                                    text2.setFill(Color.RED);
                                    flowDes.getChildren().add(0, text2);
                                }
                            }
                            VBox vbox = new VBox();
                            vbox.getChildren().add(flow);
                            Tooltip tooltip = new Tooltip(icdItem.getDescription());
                            Tooltip.install(flow, tooltip);

                            vbox.setPrefWidth(diagnosisTreeView.getWidth() - 40 - ((icdItem.getDepth()) * 15));
                            vbox.setPrefHeight(flow.prefHeight(vbox.getPrefWidth()) + 4);
                            Platform.runLater(() -> {
                                if ((vbox.getHeight() / flow.getHeight()) > 1.6 || (vbox.getHeight() / flow.getHeight()) < -1.6) {
                                    vbox.setPrefHeight(flow.getHeight());
                                }
                            });
                            widthProperty().addListener((obs, oldVal, newVal) -> {
                                vbox.setPrefWidth(diagnosisTreeView.getWidth() - 40 - ((icdItem.getDepth()) * 15));
                                vbox.setPrefHeight(flow.prefHeight(vbox.getPrefWidth()) + 4);
                                Platform.runLater(() -> {
                                    if ((vbox.getHeight() / flow.getHeight()) > 1.6 || (vbox.getHeight() / flow.getHeight()) < -1.6) {
                                        vbox.setPrefHeight(flow.getHeight());
                                    }
                                });
                            });
                            setGraphic(vbox);
                        } else {
                            setText(null);
                            setGraphic(null);
                        }
                    }
                };
                treeCell.setOnDragDetected(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (treeCell.getTreeItem() != null) {
                            if (treeCell.getTreeItem().getValue().isCompleteFl()) {
                                Dragboard dragboard = treeCell.startDragAndDrop(TransferMode.COPY);
                                ClipboardContent content = new ClipboardContent();
                                content.put(ICD_FORMAT, treeCell.getTreeItem().getValue());
                                dragboard.setContent(content);
                                tCaseIcdListView.setStyle("-fx-border-color: #ff3333;");
                                event.consume();
                            }
                        }
                    }
                });
                treeCell.setOnDragDone(new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent event) {
                        if (event.getDragboard().hasContent(ICD_FORMAT)) {
                            tCaseIcdListView.setStyle("-fx-border-color: transparent;");
                        }
                    }
                });
                return treeCell;
            }
        });
        TreeItem<ICpxTreeItem<? extends ICpxThesaurus>> rootItem = new TreeItem<>();
        buildIcdTreeView(rootItem, icdList, true);
        diagnosisTreeView.setRoot(rootItem);
        diagnosisTreeView.setShowRoot(false);
    }
   
    /**
     * Build icd tree view
     *
     * @param pRootItem
     * @param pIcdList
     * @param pCollapseAll
     */
    @SuppressWarnings("unchecked")
    private void buildIcdTreeView(final TreeItem<ICpxTreeItem<? extends ICpxThesaurus>> pRootItem, final List<? extends ICpxTreeItem<? extends ICpxThesaurus>> pIcdList, final boolean pCollapseAll) {
        if (pIcdList == null || pIcdList.isEmpty()) {
            return;
        }
        for (ICpxTreeItem<? extends ICpxThesaurus> cpxIcd : pIcdList) {
            TreeItem<ICpxTreeItem<? extends ICpxThesaurus>> cpxItem = new TreeItem<>(cpxIcd);
            if (!pCollapseAll) {
                if (cpxIcd.isVisible()) {
                    cpxItem.setExpanded(true);
                } else {
                    //Remove elements from tree that don't match the search string
                    cpxItem = null;
                    cpxIcd = null;
                }
            }
            if (cpxItem != null) {
                pRootItem.getChildren().addAll(cpxItem);
                buildIcdTreeView(cpxItem, cpxIcd.getChildren(), pCollapseAll);
            }
        }
    }

    /**
     * build ops tree view
     *
     * @param pRootItem
     * @param pOpsList
     * @param pCollapseAll
     */
    @SuppressWarnings("unchecked")
    private void buildOpsTreeView(final TreeItem<ICpxTreeItem<? extends ICpxThesaurus>> pRootItem, final List<? extends ICpxTreeItem<? extends ICpxThesaurus>> pOpsList, final boolean pCollapseAll) {
        if (pOpsList == null || pOpsList.isEmpty()) {
            return;
        }
        for (ICpxTreeItem<? extends ICpxThesaurus> cpxOps : pOpsList) {
            TreeItem<ICpxTreeItem<? extends ICpxThesaurus>> cpxItem = new TreeItem<>(cpxOps);
            if (!pCollapseAll) {
                if (cpxOps.isVisible()) {
                    cpxItem.setExpanded(true);
                } else {
                    //Remove elements from tree that don't match the search string
                    cpxItem = null;
                    cpxOps = null;
                }
            }
            if (cpxItem != null) {
                pRootItem.getChildren().addAll(cpxItem);
                buildOpsTreeView(cpxItem, cpxOps.getChildren(), pCollapseAll);
            }
        }
    }

    /**
     * Initialize ICD-TCase ListView
     */
    private void initTCaseIcdListView() {
        tCaseIcdListView.setItems(selectedTCaseIcd);
        tCaseIcdListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tCaseIcdListView.setCellFactory(new Callback<ListView<TCaseIcd>, ListCell<TCaseIcd>>() {
            @Override
            public ListCell<TCaseIcd> call(ListView<TCaseIcd> p) {
                ListCell<TCaseIcd> cell = new ListCell<TCaseIcd>() {
                    @Override
                    protected void updateItem(TCaseIcd dbIcd, boolean empty) {
                        super.updateItem(dbIcd, empty);
                        if (dbIcd != null) {

                            HBox hBox = new HBox();
                            hBox.setSpacing(6);

                            Label label = new Label(dbIcd.getIcdcCode());
                            label.setPrefWidth(50);

                            Tooltip tooltip = new Tooltip(dbIcd.getIcdcCode() + " - " + CpxIcdCatalog.instance().getByCode(dbIcd.getIcdcCode(), lang, year).getIcdDescription());
                            label.setTooltip(tooltip);
                            LocalisationComboBox cb = new LocalisationComboBox(dbIcd.getIcdcLocEn());
                            cb.valueProperty().addListener(new ChangeListener<LocalisationEn>() {
                                @Override
                                public void changed(ObservableValue<? extends LocalisationEn> observable, LocalisationEn oldValue, LocalisationEn newValue) {
                                    if (newValue != null) {
                                        dbIcd.setIcdcLocEn(newValue);
                                    }
                                }
                            });

                            hBox.setAlignment(Pos.CENTER_LEFT);
                            hBox.setPadding(new Insets(0, 0, 0, 6));
                            HBox.setHgrow(cb, Priority.ALWAYS);
                            hBox.getChildren().addAll(label, cb);

                            setGraphic(hBox);
                        } else {
                            setText(null);
                            setGraphic(null);
                        }
                    }
                };
                cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (cell.isEmpty()) {
                            tCaseIcdListView.getSelectionModel().clearSelection();
                        } else if (event.getClickCount() == 2) {
                            int index = tCaseIcdListView.getSelectionModel().getSelectedIndex();
                            removeDiagnosis(index);
                            tCaseIcdListView.getSelectionModel().clearSelection();
                        }
                    }
                });

                final ContextMenu contextMenu = new CtrlContextMenu<>();
                MenuItem item1 = new MenuItem(Lang.getCatalogDialogRemove());
                item1.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        int index = tCaseIcdListView.getSelectionModel().getSelectedIndex();
                        boolean isSelectionEmpty = tCaseIcdListView.getSelectionModel().isEmpty();
                        if (!isSelectionEmpty) {
                            removeDiagnosis(index);
                        }
                        tCaseIcdListView.getSelectionModel().clearSelection();
                    }
                });
                MenuItem item2 = new MenuItem(Lang.getCatalogDialogRemoveAll());
                item2.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        clearAndClose(true, false, false);
                    }
                });
                contextMenu.getItems().addAll(item1, item2);
                cell.setContextMenu(contextMenu);

                cell.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                    }
                });
                return cell;
            }
        });

        tCaseIcdListView.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.DELETE) {
                    ObservableList<Integer> indices = tCaseIcdListView.getSelectionModel().getSelectedIndices();
                    for (int index : indices) {
                        removeDiagnosis(index);
                    }
                    event.consume();
                    tCaseIcdListView.getSelectionModel().clearSelection();
                }
            }
        });

        listViewSettings(tCaseIcdListView, ICD_FORMAT);
    }

    /**
     * Initialize Ops-TCase ListView
     */
    private void initTCaseOpsListView() {
        tCaseOpsListView.setItems(selectedTCaseOps);
        tCaseOpsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tCaseOpsListView.getStyleClass().add("remove-h-scroll-bar");
        tCaseOpsListView.setCellFactory(new Callback<ListView<TCaseOps>, ListCell<TCaseOps>>() {
            @Override
            public ListCell<TCaseOps> call(ListView<TCaseOps> p) {
                ListCell<TCaseOps> cell = new ListCell<TCaseOps>() {
                    @Override
                    protected void updateItem(TCaseOps dbOps, boolean empty) {
                        super.updateItem(dbOps, empty);
                        if (dbOps != null) {
                            //CPX-949  RSH 18052018  : Issue 1
                            FormatedDatePicker dp = new FormatedDatePicker();
                            if (dbOps.getOpscDatum() != null) {
                                dp.setValue(Lang.toLocalDate(dbOps.getOpscDatum()));
                            }

                            dp.setPrefWidth(100);

                            dp.valueProperty().addListener(new ChangeListener<LocalDate>() {
                                @Override
                                public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                                    if (newValue != null) {
                                        dbOps.setOpscDatum(Date.from(dp.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                                    } else {
                                        dbOps.setOpscDatum(null);
                                    }
                                }
                            });
                            LocalisationComboBox cb = new LocalisationComboBox(dbOps.getOpscLocEn());
                            cb.setPrefWidth(110);
                            cb.valueProperty().addListener(new ChangeListener<LocalisationEn>() {
                                @Override
                                public void changed(ObservableValue<? extends LocalisationEn> observable, LocalisationEn oldValue, LocalisationEn newValue) {
                                    if (newValue != null) {
                                        dbOps.setOpscLocEn(newValue);
                                    }
                                }
                            });
                            HBox hBox = new HBox();
                            hBox.setSpacing(6);

                            Label label = new Label(dbOps.getOpscCode());
                            Tooltip tooltip = new Tooltip(dbOps.getOpscCode() + " - " + CpxOpsCatalog.instance().getByCode(dbOps.getOpscCode(), lang, year).getOpsDescription());
                            label.setTooltip(tooltip);
                            label.setPrefWidth(50);

                            hBox.setAlignment(Pos.CENTER_LEFT);
                            hBox.setPadding(new Insets(0, 0, 0, 6));
                            HBox.setHgrow(cb, Priority.ALWAYS);
                            hBox.getChildren().addAll(label, dp, cb);

                            setGraphic(hBox);
                        } else {
                            setText(null);
                            setGraphic(null);
                        }
                    }
                };

                cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (cell.isEmpty()) {
                            tCaseOpsListView.getSelectionModel().clearSelection();
                        } else if (event.getClickCount() == 2) {
                            int index = tCaseOpsListView.getSelectionModel().getSelectedIndex();
                            removeProcedure(index);
                            tCaseOpsListView.getSelectionModel().clearSelection();
                        }
                    }
                });

                final ContextMenu contextMenu = new CtrlContextMenu<>();
                MenuItem item1 = new MenuItem(Lang.getCatalogDialogRemove());
                item1.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        int index = tCaseOpsListView.getSelectionModel().getSelectedIndex();
                        boolean isSelectionEmpty = tCaseOpsListView.getSelectionModel().isEmpty();
                        if (!isSelectionEmpty) {
                            removeProcedure(index);
                        }
                        tCaseOpsListView.getSelectionModel().clearSelection();
                    }
                });
                MenuItem item2 = new MenuItem(Lang.getCatalogDialogRemoveAll());
                item2.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        clearAndClose(false, true, false);
                    }
                });
                contextMenu.getItems().addAll(item1, item2);
                cell.setContextMenu(contextMenu);

                cell.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                    }
                });

                return cell;
            }
        });

        tCaseOpsListView.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.DELETE) {
                    ObservableList<Integer> indices = tCaseOpsListView.getSelectionModel().getSelectedIndices();
                    for (int index : indices) {
                        removeProcedure(index);
                    }
                    event.consume();
                    tCaseOpsListView.getSelectionModel().clearSelection();
                }
            }
        });

        listViewSettings(tCaseOpsListView, OPS_FORMAT);
    }

    private String getAopString(CpxOps opsItem){
       return CpxOpsAopCatalog.instance().getAopString( opsItem);
        
    }
    /**
     * Initialize Procedure TreeView
     */
    private void initProcedureTreeView() {
        Controller<CpxScene> redirector = this;
        procedureTreeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<ICpxTreeItem<? extends ICpxThesaurus>>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<ICpxTreeItem<? extends ICpxThesaurus>>> observable, TreeItem<ICpxTreeItem<? extends ICpxThesaurus>> oldValue, TreeItem<ICpxTreeItem<? extends ICpxThesaurus>> newValue) {
                TreeItem<ICpxTreeItem<? extends ICpxThesaurus>> opsItem = procedureTreeView.getSelectionModel().getSelectedItem();
                if (opsItem != null) {
                    String aopStr = getAopString((CpxOps)opsItem.getValue());
                    TextFlow flow = new TextFlowWithCopyText(catalogDetailText(EXCL_TEXT, opsItem.getValue().getExclusion())
                            + catalogDetailText(INCL_TEXT, opsItem.getValue().getInclusion())
                            + catalogDetailText(HINT_TEXT, opsItem.getValue().getNote())
                            + catalogDetailText(THESAURUS_TEXT, opsItem.getValue().getThesaurusString())
                            + (aopStr.length() > 0?catalogDetailText(AOP_TEXT, aopStr)
                                    :"")
 //                                   ((CpxOps)opsItem.getValue()).getAopString()
                            , 
                            nExprList, fixedExprIndexes);
                    CodeInjector ci = new CodeInjector(null, (pEvent, pCode) -> {
                        focusCode(opsList, procedureTreeView, pCode);
                    });
                    ci.addCodeLinks(flow);
                    procedureTxtArea.setFitToWidth(true);
                    procedureTxtArea.setFitToHeight(true);
                    ((TextFlowWithCopyText)flow).setRedirector(redirector);
                    procedureTxtArea.setContent(flow);

                } else {
                    procedureTxtArea.setContent(null);
                }
            }
        });
        procedureTreeView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                TreeItem<ICpxTreeItem<? extends ICpxThesaurus>> opsItem = procedureTreeView.getSelectionModel().getSelectedItem();
                if (opsItem != null) {
                    if (event.getClickCount() == 2) {
                        if (opsItem.getValue().isCompleteFl()) {
                            setProcedure(opsItem.getValue());
                        }
                    }
                }
            }
        });

        procedureTreeView.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                TreeItem<ICpxTreeItem<? extends ICpxThesaurus>> opsItem = procedureTreeView.getSelectionModel().getSelectedItem();
                if (opsItem != null) {
                    if (event.getCode() == KeyCode.ENTER) {
                        if (opsItem.getValue().isCompleteFl()) {
                            setProcedure(opsItem.getValue());
                        }
                        event.consume();
                    }
                }
            }
        });

        //Drag And Drop Feature
        procedureTreeView.setCellFactory(new Callback<TreeView<ICpxTreeItem<? extends ICpxThesaurus>>, TreeCell<ICpxTreeItem<? extends ICpxThesaurus>>>() {
            private final ChangeListener<Boolean> changeListener = new ChangeListener<>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                }
            };

            @Override
            public TreeCell<ICpxTreeItem<? extends ICpxThesaurus>> call(TreeView<ICpxTreeItem<? extends ICpxThesaurus>> opsTreeView) {
                TreeCell<ICpxTreeItem<? extends ICpxThesaurus>> treeCell = new TreeCell<ICpxTreeItem<? extends ICpxThesaurus>>() {
                    @Override
                    protected void updateItem(ICpxTreeItem<? extends ICpxThesaurus> cpxOps, boolean empty) {
                        super.updateItem(cpxOps, empty);
                        if (cpxOps != null) {
                            String str = cpxOps.getCode() + " - " + cpxOps.getDescription();
                            TextFlow flow = new TextFlowWithCopyText(str, nExprList, fixedExprIndexes);
                            if (flow.getChildren().size() == 1) {
                                TextFlow flowDes = new TextFlowWithCopyText(catalogDetailText(EXCL_TEXT, cpxOps.getExclusion())
                                        + catalogDetailText(INCL_TEXT, cpxOps.getInclusion())
                                        + catalogDetailText(HINT_TEXT, cpxOps.getNote())
                                        + catalogDetailText(THESAURUS_TEXT, cpxOps.getThesaurusString()), nExprList, fixedExprIndexes);

                                if (flowDes.getChildren().size() > 1 && flow.getChildren().size() == 1) {
                                    Text text2 = new Text("<*>");
                                    text2.setStyle("-fx-wrap-text: true; -fx-font-weight: bold;");
                                    text2.setFill(Color.RED);
                                    flow.getChildren().add(0, text2);
                                }
                            }
                            VBox vbox = new VBox();
                            vbox.getChildren().add(flow);
                            Tooltip tooltip = new Tooltip(cpxOps.getDescription());
                            Tooltip.install(flow, tooltip);
                            vbox.setPrefWidth(procedureTreeView.getWidth() - 48 - ((cpxOps.getDepth()) * 11));
                            vbox.setPrefHeight(flow.prefHeight(vbox.getPrefWidth()) + 4);

                            Platform.runLater(() -> {
                                if ((vbox.getHeight() / flow.getHeight()) > 1.6 || (vbox.getHeight() / flow.getHeight()) < -1.6) {
                                    vbox.setPrefHeight(flow.getHeight());
                                }
                            });
                            widthProperty().addListener((obs, oldVal, newVal) -> {
                                vbox.setPrefWidth(procedureTreeView.getWidth() - 48 - ((cpxOps.getDepth()) * 11));
                                vbox.setPrefHeight(flow.prefHeight(vbox.getPrefWidth()) + 4);

                                Platform.runLater(() -> {
                                    if ((vbox.getHeight() / flow.getHeight()) > 1.6 || (vbox.getHeight() / flow.getHeight()) < -1.6) {

                                        vbox.setPrefHeight(flow.getHeight());
                                    }
                                });
                            });
                            setGraphic(vbox);
                            treeItemProperty().get().expandedProperty().removeListener(changeListener);
                            treeItemProperty().get().expandedProperty().addListener(changeListener);
                        } else {
                            setText(null);
                            setGraphic(null);
                        }
                    }
                };

                treeCell.setOnDragDetected(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (treeCell.getTreeItem().getValue().isCompleteFl()) {
                            Dragboard dragboard = treeCell.startDragAndDrop(TransferMode.COPY);
                            ClipboardContent content = new ClipboardContent();
                            content.put(OPS_FORMAT, treeCell.getTreeItem().getValue());
                            dragboard.setContent(content);

                            tCaseOpsListView.setStyle("-fx-border-color: #ff3333;");

                            event.consume();
                        }
                    }
                });
                treeCell.setOnDragDone(new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent event) {
                        if (event.getDragboard().hasContent(OPS_FORMAT)) {
                            tCaseOpsListView.setStyle("-fx-border-color: transparent;");
                        }
                    }
                });
                return treeCell;
            }
        });
        TreeItem<ICpxTreeItem<? extends ICpxThesaurus>> rootItem = new TreeItem<>();
        buildOpsTreeView(rootItem, opsList, true);
        procedureTreeView.setRoot(rootItem);
        procedureTreeView.setShowRoot(false);
    }

    /**
     * initialize Search-Function Both: Diagnosis and Procedure
     */
    private void initSearch() {
        diagnosisTxtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchIcds(newValue);
        });
        procedureTxtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchOpses(newValue);
        });
        //default search if text is set outside
        if (!procedureTxtSearch.getText().isEmpty()) {
            searchOpses(procedureTxtSearch.getText());
        }
        if (!diagnosisTxtSearch.getText().isEmpty()) {
            searchIcds(diagnosisTxtSearch.getText());
        }
    }

    private void searchIcds(String pSearchString) {
//        String searchString = diagnosisTxtSearch.getText();
        searchTimer.cancel();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        searchDiagnosis(pSearchString, nExprList, fixedExprIndexes);
                        //spell checker
                        long start = System.currentTimeMillis();
                        if (useSpellChecker) {
                            spellCheckerManagerClass.spellCheckerManager(pSearchString, Diagnose.ICD);
//                                spellCheckerManager(searchString, Diagnose.ICD);
                        }
                        long end = System.currentTimeMillis();
                        java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Time elapsed in ICD SpellChecker Search: " + (end - (double) start) / 1000d + "s");
                    }
                });
            }
        };
        searchTimer = new Timer("searchIcdTimer");
        searchTimer.schedule(task, 60);
    }

    private void searchOpses(String searchString) {
//        String searchString = procedureTxtSearch.getText();
        searchTimer.cancel();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        searchProcedure(searchString, nExprList, fixedExprIndexes);
                        //spell checker
                        long start = System.currentTimeMillis();
                        if (useSpellChecker) {
                            spellCheckerManagerClass.spellCheckerManager(searchString, Diagnose.OPS);
//                                spellCheckerManager(searchString, Diagnose.OPS);
                        }
                        long end = System.currentTimeMillis();
                        java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Time elapsed in OPS SpellChecker Search: " + (end - (double) start) / 1000d + "s");
                    }
                });
            }
        };
        searchTimer = new Timer("searchOpsTimer");
        searchTimer.schedule(task, 150);
    }

    /**
     *
     * initializing spellChecker by declearing the correct Pathes and indexing
     * from autocomplete and correction for both icd and ops note: indexing for
     * ops and icd dictionaries (not temp dicionary) has to be build only once,
     * if the index files are not existed , it would automaticly be generated.
     * if there is any problem with the index files you can delete the indexes
     * directory in "\dictionaries\indexes". these indexes(except temp index)
     * are in readOnly ,which means more than one client can access these files.
     *
     * Halabieh
     *
     * @throws IOException if Directory couldn't be open
     */
    public void initSpellChecker() throws IOException {
        long start = System.currentTimeMillis();

        CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();

        final File icdAutocompleteDir = new File(cpxProps.getCpxClientDictionariesIcdDir() + "icdAutocompleteDir_" + year);
        final File opsAutocompleteDir = new File(cpxProps.getCpxClientDictionariesOpsDir() + "opsAutocompleteDir_" + year);
        final File icdCorrectionDir = new File(cpxProps.getCpxClientDictionariesIcdDir() + "icdCorrectionDir_" + year);
        final File opsCorrectionDir = new File(cpxProps.getCpxClientDictionariesOpsDir() + "opsCorrectionDir_" + year);

        //String outPath = cpxProps.getAppDataLocal()+ "\\dictionaries\\indexes\\Temp\\Temp-Dictionary.txt";
//        pathToTempIndex = cpxProps.getCpxClientDictionariesDir() + "\\temp\\tempIndex";
//        Files.createDirectories(Paths.get(pathToTempIndex));
//        pathToTempDictionary = cpxProps.getCpxClientDictionariesDir() + "\\temp\\Temp-Dictionary.txt";
        pathToTempIndex = cpxProps.getAppDataLocal() + "\\dictionaries\\indexes\\Temp\\tempIndex";
        Files.createDirectories(Paths.get(pathToTempIndex));
        pathToTempDictionary = cpxProps.getAppDataLocal() + "\\dictionaries\\indexes\\Temp\\Temp-Dictionary.txt";

        analyzer = new StandardAnalyzer();

        //icd autocomplete
        LOG.log(Level.INFO, "Initialize ICD Autocompletion from " + icdAutocompleteDir.getAbsolutePath());
        if (!icdAutocompleteDir.exists() || !icdAutocompleteDir.isDirectory() || !icdAutocompleteDir.canRead()) {
            useSpellChecker = false;
            LOG.log(Level.WARNING, "Unable to initialize ICD Autocompletion! Index directory is missing or is unreadable under " + icdAutocompleteDir.getAbsolutePath() + "!");
        } else {
            icdDirectory = FSDirectory.open(icdAutocompleteDir.toPath(), NoLockFactory.INSTANCE);
            if (DirectoryReader.indexExists(icdDirectory)) {
                icdSuggester = new AnalyzingInfixSuggester(icdDirectory, analyzer);
                LOG.log(Level.INFO, "ICD Autocompletion successfully initialized!");
            } else {
                useSpellChecker = false;
                LOG.log(Level.WARNING, "Unable to initialize ICD Autocompletion! Index file is missing under " + icdAutocompleteDir.getAbsolutePath() + "!");
            }
        }

        //ops autocomplete
        LOG.log(Level.INFO, "Initialize OPS Autocompletion from " + opsAutocompleteDir.getAbsolutePath());
        if (!opsAutocompleteDir.exists() || !opsAutocompleteDir.isDirectory() || !opsAutocompleteDir.canRead()) {
            useSpellChecker = false;
            LOG.log(Level.WARNING, "Unable to initialize OPS Autocompletion! Index directory is missing or is unreadable under " + opsAutocompleteDir.getAbsolutePath() + "!");
        } else {
            opsDirectory = FSDirectory.open(opsAutocompleteDir.toPath(), NoLockFactory.INSTANCE);
            if (DirectoryReader.indexExists(opsDirectory)) {
                opsSuggester = new AnalyzingInfixSuggester(opsDirectory, analyzer);
                LOG.log(Level.INFO, "OPS Autocompletion successfully initialized!");
            } else {
                useSpellChecker = false;
                LOG.log(Level.WARNING, "Unable to initialize OPS Autocompletion! Index file is missing under " + opsAutocompleteDir.getAbsolutePath() + "!");
            }
        }

        //icd correction
        LOG.log(Level.INFO, "Initialize ICD Spell Correction from " + icdCorrectionDir.getAbsolutePath());
        if (!icdCorrectionDir.exists() || !icdCorrectionDir.isDirectory() || !icdCorrectionDir.canRead()) {
            useSpellChecker = false;
            LOG.log(Level.WARNING, "Unable to initialize ICD Spell Correction! Index directory is missing or is unreadable under " + icdCorrectionDir.getAbsolutePath() + "!");
        } else {
            icdSpellIndexDir = FSDirectory.open(icdCorrectionDir.toPath(), NoLockFactory.INSTANCE);
            if (DirectoryReader.indexExists(icdSpellIndexDir)) {
                icdSpellChecker = new SpellChecker(icdSpellIndexDir);
                LOG.log(Level.INFO, "ICD Spell Correction successfully initialized!");
            } else {
                useSpellChecker = false;
                LOG.log(Level.WARNING, "Unable to initialize ICD Spell Correction! Index file is missing under " + icdCorrectionDir.getAbsolutePath() + "!");
            }
        }

        //ops correction
        LOG.log(Level.INFO, "Initialize OPS Spell Correction from " + opsCorrectionDir.getAbsolutePath());
        if (!opsCorrectionDir.exists() || !opsCorrectionDir.isDirectory() || !opsCorrectionDir.canRead()) {
            useSpellChecker = false;
            LOG.log(Level.WARNING, "Unable to initialize OPS Spell Correction! Index directory is missing or is unreadable under " + opsCorrectionDir.getAbsolutePath() + "!");
        } else {
            opsSpellIndexDir = FSDirectory.open(opsCorrectionDir.toPath(), NoLockFactory.INSTANCE);
            if (DirectoryReader.indexExists(opsSpellIndexDir)) {
                opsSpellChecker = new SpellChecker(opsSpellIndexDir);
                LOG.log(Level.INFO, "OPS Spell Correction successfully initialized!");
            } else {
                useSpellChecker = false;
                LOG.log(Level.WARNING, "Unable to initialize OPS Spell Correction! Index file is missing under " + opsCorrectionDir.getAbsolutePath() + "!");
            }
        }

        long end = System.currentTimeMillis();
        LOG.log(Level.INFO, "Time elapsed for EasyCoder indexing: " + (end - (double) start) / 1000d + "s");
        spellCheckerManagerClass = new SpellCheckManagerClass(diagnosisTxtSearch, procedureTxtSearch, opsSuggester, icdSuggester, opsSpellChecker, icdSpellChecker, pathToTempIndex, pathToTempDictionary);
    }

    /**
     * Search functions: 1) " " what is written in quotation marks will be
     * considered as a String -if there is no space before or after the
     * quotation the word after or before it would be considered as if its
     * inside the quotations Example: test"word"
     *
     * 2) space between the words would considered as an AND operator
     *
     * 3) , comma would considered as an OR operator
     *
     * @param pExpr expression
     */
    public void textPreparationForSearch(String pExpr) {
        nExprList.clear();
        fixedExprIndexes.clear();
        List<String> qList = new ArrayList<>();

        if (pExpr.contains("javaqarrayelement")) {
            pExpr = pExpr.replace("javaqarrayelement", "");
        }

        //set " " at begain
        if (pExpr.startsWith("\"")) {
            pExpr = " " + pExpr;
        }
        //set " " at end
        if (pExpr.endsWith("\"")) {
            pExpr = pExpr + " ";
        }

        //getting Exception by writing "$"
        pExpr = pExpr.replaceAll("[$]", "");

        //strQ Array and for loop
        String[] strQ = pExpr.split("\"");

        //delete the last " if its alone
        if (strQ.length % 2 == 0) {
            int i = pExpr.lastIndexOf('"');
            pExpr = pExpr.substring(0, i) + pExpr.substring(i + 1);
            strQ = pExpr.split("\"");
        }

        //delete |""| to ||
        pExpr = pExpr.replaceAll("\"\"", "");

        //strQ for loop
        for (int q = 0; q < strQ.length; q++) {
            if (q % 2 != 0) {
                qList.add(strQ[q]);
                strQ[q] = "javaqarrayelement";
            }
        }
        String nExpr = String.join("", strQ).trim();

        nExpr = TextFlowWithCopyText.replaceChars(nExpr.trim().toLowerCase(), chars);

        String[] strOr = nExpr.split(",");
        for (int or = 0; or < strOr.length; or++) {
            List<String> orList = new ArrayList<>();
            boolean match = true;
            //strAnd Array and for loop
            String[] strAnd = strOr[or].split("[& ]");
            for (int and = 0; and < strAnd.length; and++) {
                while (strAnd[and].contains("javaqarrayelement")) {
                    strAnd[and] = strAnd[and].replaceFirst("javaqarrayelement", qList.get(0));
                    qList.remove(0);
                    fixedExprIndexes.add(new ArrayList<>());
                    fixedExprIndexes.get(fixedExprIndexes.size() - 1).add(or);
                    fixedExprIndexes.get(fixedExprIndexes.size() - 1).add(and);
                }
                orList.add(strAnd[and]);
            }
            nExprList.add(orList);
        }
    }

//    /**
//     * remove the chars like {".", "-", "/"} and duplicate {" ", "&amp;&amp;",
//     * ",,"}
//     *
//     * @param pValue value
//     * @param chars characters
//     * @return pValue after replacing the Characters
//     */
//    public static String replaceChars(String pValue, String[][] chars) {
//        for (int i = 0; i < chars[0].length; i++) {
//            while (pValue.contains(chars[0][i])) {
//                pValue = pValue.replace(chars[0][i], chars[1][i]);
//            }
//        }
//        return pValue;
//    }
//
    /**
     * Searching the Diagnoses (ICDs)
     *
     * @param searchString
     */
    private void searchDiagnosis(String searchString,
            final List<List<String>> pExprList, final List<List<Integer>> pFixedExprIndexes) {
        String tempDicText = "";
        textPreparationForSearch(searchString);
        if (searchString.isEmpty()) {
            initDiagnosisTreeView();
        } else {
            List<ICpxTreeItem<? extends ICpxThesaurus>> icdLeafs = new ArrayList<>();
            for (ICpxTreeItem<? extends ICpxThesaurus> pCpxIcd : icdList) {
                pCpxIcd.setVisible(false, false, true);
                icdLeafs.addAll(pCpxIcd.getLastChildren());
            }

            long timeStart = System.currentTimeMillis();
            //set counter to limit the dictionary builder in case of unexpected large dictionary
            // it would take around 0.4 seconds to build a dictionary for 1000 leaf
            //noramlly it should not exceed 300 leafs max
            int counter = 0;
            for (ICpxTreeItem<? extends ICpxThesaurus> pCpxIcd : icdLeafs) {
                pCpxIcd.like(pExprList, pFixedExprIndexes);

                //use temp dic?
                String fullSearchString = searchString;
                if (fullSearchString.contains(",")) {
                    fullSearchString = fullSearchString.substring(fullSearchString.lastIndexOf(',') + 1);
                }
                String[] fullSearchArray = fullSearchString.trim().split(" ");
                //Store all descriptions in tempDicText to write a Temp Dectionary
                if (fullSearchArray.length >= 2) {
                    if (pCpxIcd.isVisible()) {
                        counter++;
                        if (counter < 1000) {
                            tempDicText = tempDicText + pCpxIcd.getSearchableDescription() + " ";

                        }
                    }
                }
            }
            //send to dictionary
            tempD.setTempDictionary(tempDicText);
            long timeEnd = System.currentTimeMillis();
            LOG.log(Level.INFO, "Time elapsed while searching ICD tree: " + (timeEnd - (double) timeStart) / 1000d + "s");
            TreeItem<ICpxTreeItem<? extends ICpxThesaurus>> rootItem = new TreeItem<>();
            buildIcdTreeView(rootItem, icdList, false);
            diagnosisTreeView.setRoot(rootItem);
            diagnosisTreeView.setShowRoot(false);
        }
    }

    /**
     * Search procedure
     *
     * @param searchString
     * @param pExprList
     * @param pFixedExprIndexes
     */
    private void searchProcedure(String searchString,
            final List<List<String>> pExprList, final List<List<Integer>> pFixedExprIndexes) {
        String tempDicText = "";
        textPreparationForSearch(searchString);
        if (searchString.isEmpty()) {
            initProcedureTreeView();
        } else {
            List<ICpxTreeItem<? extends ICpxThesaurus>> opsLeafs = new ArrayList<>();
            for (ICpxTreeItem<? extends ICpxThesaurus> pCpxOps : opsList) {
                pCpxOps.setVisible(false, false, true);
                opsLeafs.addAll(pCpxOps.getLastChildren());
            }
            long timeStart = System.currentTimeMillis();

            //use temp dictionary?
            String fullSearchString = searchString;
            if (fullSearchString.contains(",")) {
                fullSearchString = fullSearchString.substring(fullSearchString.lastIndexOf(',') + 1);
            }
            String[] fullSearchArray = fullSearchString.trim().split(" ");

            //set counter to limit the dictionary builder in case of unexpected large dictionary
            //it would take around 0.4 seconds to build a dictionary for 1000 leaf
            //noramlly it should not exceed 300 leafs max
            int counter = 0;
            for (ICpxTreeItem<? extends ICpxThesaurus> pCpxOps : opsLeafs) {
                pCpxOps.like(pExprList, pFixedExprIndexes);
                //Store all descriptions in tempDicText to write a Temp Dectionary
                if (fullSearchArray.length >= 2) {
                    if (pCpxOps.isVisible()) {
                        counter++;
                        if (counter < 1000) {
                            tempDicText = tempDicText + pCpxOps.getSearchableDescription() + " ";
                        }

                    }
                }
            }
            //send to dictionary
            tempD.setTempDictionary(tempDicText);
            long timeEnd = System.currentTimeMillis();
            LOG.log(Level.INFO, "Time elapsed while searching OPS tree: " + (timeEnd - (double) timeStart) / 1000d + "s");
            TreeItem<ICpxTreeItem<? extends ICpxThesaurus>> rootItem = new TreeItem<>();
            buildOpsTreeView(rootItem, opsList, false);
            procedureTreeView.setRoot(rootItem);
            procedureTreeView.setShowRoot(false);
        }
    }

    /**
     * title + context + linebreak
     *
     * @param title String
     * @param context String
     * @return String
     */
    private String catalogDetailText(String title, String context) {
        String string = new String();
        if (context != null) {
            string = title + context + "\n";
        }
        return string;
    }

    @FXML
    private void clearDiagnosis(ActionEvent event) {
        clearAndClose(true, false, false);
    }

    @FXML
    private void clearProcedure(ActionEvent event) {
        clearAndClose(false, true, false);
    }

    /**
     * clears Selected ArrayLists, closes window
     *
     * @param sIcd Boolean, selectedDiagnoses will be cleared if true
     * @param sOps Boolean, selectedProcedures will be cleared if true
     * @param close Boolean, CatalogDialog will be closed if true
     */
    public void clearAndClose(Boolean sIcd, Boolean sOps, Boolean close) {

        if (sIcd) {
            selectedTCaseIcd.clear();
        }
        if (sOps) {
            selectedTCaseOps.clear();
        }
        if (close) {
            EasyCoderDialogFXMLController.this.close();
        }

    }

    /**
     * closing all directorys after closing EasyCoder
     *
     * @return boolean as indicator if closing of the ressources occupied by the
     * controller are successfully unlocked
     */
    @Override
    public boolean close() {
        //Files can be null if dictionary dosen't exist
        //for example year in the future or deleted files
        try {
            //closing directories
            if (icdSuggester != null) {
                icdSuggester.close();
            }
            if (icdDirectory != null) {
                icdDirectory.close();
            }
            if (opsSuggester != null) {
                opsSuggester.close();
            }
            if (opsDirectory != null) {
                opsDirectory.close();
            }
            //close icd correction
            if (icdSpellIndexDir != null) {
                icdSpellIndexDir.close();
            }
            if (icdSpellChecker != null) {
                icdSpellChecker.close();
            }
            //close ops correction
            if (opsSpellIndexDir != null) {
                opsSpellIndexDir.close();
            }
            if (opsSpellChecker != null) {
                opsSpellChecker.close();
            }

        } catch (IOException ex) {
            Logger.getLogger(EasyCoderDialogFXMLController.class.getName()).log(Level.SEVERE, "Error by closing dirctories from spellChecker", ex);
        }
        return super.close(); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Sets Diagnosis and TCase-Diagnosis
     *
     * @param icd
     */
    private void setDiagnosis(ICpxTreeItem<? extends ICpxThesaurus> icd) {
        TCaseIcd dbIcd = new TCaseIcd();
        dbIcd.setIcdcCode(icd.getCode());
        dbIcd.setCreationDate(new Date());
        dbIcd.setIcdcLocEn(LocalisationEn.E);
        selectedTCaseIcd.add(dbIcd);
    }

    /**
     * Method to remove Diagnoses and TCase-Diagnoses
     *
     * @param index int to locale selected state
     */
    private void removeDiagnosis(int index) {
        selectedTCaseIcd.remove(index);
    }

    /**
     * Sets Procedure and TCase-Procedure
     *
     * @param ops
     */
    private void setProcedure(ICpxTreeItem<? extends ICpxThesaurus> ops) {
        TCaseOps dbOps = new TCaseOps();
        dbOps.setOpscCode(ops.getCode());
        selectedTCaseOps.add(dbOps);
    }

    /**
     * Method to Remove Procedures and TCase-Procedures
     *
     * @param index int to locale selected state
     */
    private void removeProcedure(int index) {
        selectedTCaseOps.remove(index);
    }

    /**
     * Setting ListViews with Drag and Drop-Feature
     *
     * @param lv ListView to get Settings
     * @param df DataFormat to handle differents
     */
    public void listViewSettings(ListView<? extends AbstractVersionEntity> lv, DataFormat df) {
        lv.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard dragboard = event.getDragboard();
                if (dragboard.hasContent(df)) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        });

        lv.setOnDragEntered(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getDragboard().hasContent(df)) {
                    lv.setStyle("-fx-border-color: #8ef362;");
                }
            }
        });

        lv.setOnDragExited(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getDragboard().hasContent(df)) {
                    lv.setStyle("-fx-border-color: #ff3333;");
                }
            }
        });

        lv.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard dragboard = event.getDragboard();
                boolean success = false;
                if (dragboard.hasContent(df)) {
                    if (df.equals(ICD_FORMAT)) {
                        setDiagnosis((ICpxTreeItem<? extends ICpxThesaurus>) dragboard.getContent(ICD_FORMAT));
                        success = true;
                    }
                    if (df.equals(OPS_FORMAT)) {
                        setProcedure((ICpxTreeItem<? extends ICpxThesaurus>) dragboard.getContent(OPS_FORMAT));
                        success = true;
                    }
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });
    }

    /**
     * Set icd list
     *
     * @param icdItems list of diagnosis
     */
    public void setIcdList(ObservableList<TCaseIcd> icdItems) {
        selectedTCaseIcd.addAll(icdItems);
    }

    /**
     * set ops List
     *
     * @param opsItems list of procedures
     */
    public void setOpsList(ObservableList<TCaseOps> opsItems) {
        selectedTCaseOps.addAll(opsItems);
    }

    /**
     * set DiagnosenSearch
     *
     * @param icd
     */
    void setDiagnosenSearch(String icd) {
        diagnosisTxtSearch.setText(icd);
        tabProcedure.setDisable(true);
    }

    /**
     * set ProcedureSearch
     *
     * @param ops
     */
    void setProcedureSearch(String ops) {
        tabDiagnosis.getTabPane().getSelectionModel().select(tabProcedure);
        tabDiagnosis.setDisable(true);
        procedureTxtSearch.setText(ops);
    }

}
