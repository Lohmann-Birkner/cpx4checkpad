/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.editor;

import com.google.common.collect.Lists;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextArea;
import de.lb.cpx.client.core.model.fx.pane.AsyncPane;
import de.lb.cpx.client.core.model.fx.ribbon.Ribbon;
import de.lb.cpx.client.core.model.fx.ribbon.item.RibbonItem;
import de.lb.cpx.client.core.model.fx.ribbon.item.RibbonItemGroup;
import de.lb.cpx.client.core.model.fx.section.SectionHeader;
import de.lb.cpx.client.core.model.fx.tooltip.CpxTooltip;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.model.enums.RuleTypeEn;
import de.lb.cpx.rule.element.CaseRuleManager;
import de.lb.cpx.rule.element.model.Risk;
import de.lb.cpx.rule.element.model.RiskArea;
import de.lb.cpx.rule.element.model.Rule;
import de.lb.cpx.rule.element.model.RulesElement;
import de.lb.cpx.rule.element.model.RulesOperator;
import de.lb.cpx.rule.element.model.RulesValue;
import de.lb.cpx.rule.element.model.Sugg;
import de.lb.cpx.ruleviewer.dragdrop.format.DndFormat;
import de.lb.cpx.ruleviewer.event.RefreshEvent;
import de.lb.cpx.ruleviewer.event.RuleChangedEvent;
import de.lb.cpx.ruleviewer.layout.BasicEditorView;
import de.lb.cpx.ruleviewer.layout.RuleView;
import de.lb.cpx.ruleviewer.layout.SuggestionView;
import de.lb.cpx.ruleviewer.model.Element;
import de.lb.cpx.ruleviewer.model.Link;
import de.lb.cpx.ruleviewer.model.SelectableControl;
import de.lb.cpx.ruleviewer.model.Suggestion;
import de.lb.cpx.ruleviewer.model.Term;
import de.lb.cpx.ruleviewer.properties.CaptionEditor;
import de.lb.cpx.ruleviewer.properties.CategoryEditor;
import de.lb.cpx.ruleviewer.properties.ErrorTypeEditor;
import de.lb.cpx.ruleviewer.properties.FromToDateEditor;
import de.lb.cpx.ruleviewer.properties.NumberAndFeeEditor;
import de.lb.cpx.ruleviewer.properties.RoleEditor;
import de.lb.cpx.ruleviewer.properties.RuleTypeChoiceEditor2;
import de.lb.cpx.ruleviewer.properties.risk.RiskAreaEditor;
import de.lb.cpx.ruleviewer.properties.risk.RiskAuditValueEditor;
import de.lb.cpx.ruleviewer.properties.risk.RiskEditorItem;
import de.lb.cpx.ruleviewer.properties.risk.RiskWasteValueEditor;
import de.lb.cpx.ruleviewer.properties.risk.WasteDefaultValueEditor;
import de.lb.cpx.ruleviewer.util.RiskDisplayHelper;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.shared.json.RuleMessage;
import de.lb.cpx.shared.json.RuleMessageReader;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SkinBase;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.skin.ScrollPaneSkin;
import javafx.scene.control.skin.SplitPaneSkin;
import javafx.scene.control.skin.TitledPaneSkin;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.control.PropertySheet.Mode;
import org.controlsfx.control.action.Action;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.property.BeanProperty;

/**
 * rule editor skin sets ui components
 *
 * @author wilde
 */
public class RuleEditorSkin extends SkinBase<RuleEditor> {

    private AsyncPane<SuggestionView> suggestionViewPane;
    private AsyncPane<RuleView> ruleViewPane;
    private PropertySheet psMetaSheet;
//    private Rule rule;
    private PropertySheet psItemSheet;
    private TitledPane tpSelection;
//    private TitledPane tpNotice;
    private Future<Rule> ruleResult;
    private AsyncPane<PropertySheet> apSelection;
    private AsyncPane<PropertySheet> apMetaData;
//    private RuleAnalyser analyser;
    private SplitPane spContainer;
    private TitledPane tpRisk;
    private AsyncPane<Node> apRisk;

    /**
     * creates new instance of editor skin, based on ruleEditorFxml
     *
     * @param pSkinnable control class to store values
     * @throws IOException thrown when fxml could not be loaded
     */
    public RuleEditorSkin(RuleEditor pSkinnable) throws IOException {
        super(pSkinnable);
        getChildren().add(initRoot());

        //adds and setup listeners
        pSkinnable.getProperties().addListener(new MapChangeListener<Object, Object>() {
            @Override
            public void onChanged(MapChangeListener.Change<? extends Object, ? extends Object> change) {
                if (change.wasAdded()) {
                    if (change.getKey().equals(RuleEditor.UPDATE_RULE)) {
                        updateRule();
                        getSkinnable().getProperties().remove(RuleEditor.UPDATE_RULE);
                    }
                    if (change.getKey().equals(RuleEditor.REFRESH)) {
                        getSkinnable().getProperties().remove(RuleEditor.REFRESH);
//                        if (pSkinnable.getSelectedControl() == null) {
//                            return;
//                        }
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Long start = System.currentTimeMillis();
                                suggestionViewPane.refresh();
                                ruleViewPane.refresh();
                                getSkinnable().selectControl(pSkinnable.getSelectedControl());
                                LOG.info("refresh in " + (System.currentTimeMillis() - start) + " ms");
                            }
                        });
                        apSelection.reload();
                        apMetaData.reload();
                        apRisk.reload();
//                        ActionShowItemInPropertySheet action = new ActionShowItemInPropertySheet("Itemdaten", pSkinnable.getSelectedControl());
//                        action.handleAction(new ActionEvent());
                    }
                    if(RuleEditor.REFRESH_RULE_MESSAGE.equals(change.getKey())){
                        getSkinnable().getProperties().remove(RuleEditor.REFRESH_RULE_MESSAGE);
//                        if (pSkinnable.getSelectedControl() == null) {
//                            return;
//                        }
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Long start = System.currentTimeMillis();
//                                suggestionViewPane.reload();
//                                ruleViewPane.reload();
                                List<RuleMessage> messages = new RuleMessageReader().read(getSkinnable().getRule());
                                ((SuggestionView)suggestionViewPane.getContent()).setRuleMessages(messages);
                                ((SuggestionView)suggestionViewPane.getContent()).refreshRuleMessage();
                                ((RuleView)ruleViewPane.getContent()).setRuleMessages(messages);
                                ((RuleView)ruleViewPane.getContent()).refreshRuleMessage();
//                                getSkinnable().selectControl(pSkinnable.getSelectedControl());
                                LOG.info("refresh_rule_message in " + (System.currentTimeMillis() - start) + " ms");
                            }
                        });
                        apSelection.refresh();
//                        apSelection.reload();
//                        apMetaData.reload();
//                        apRisk.reload();
//                        ActionShowItemInPropertySheet action = new ActionShowItemInPropertySheet("Itemdaten", pSkinnable.getSelectedControl());
//                        action.handleAction(new ActionEvent());
                    }
                }
            }
        });
        
        pSkinnable.ruleProperty().addListener(new ChangeListener<CrgRules>() {
            @Override
            public void changed(ObservableValue<? extends CrgRules> observable, CrgRules oldValue, CrgRules newValue) {
                //clear rule data, so it will be fetched again
//                rule = null;
                updateRuleData(newValue);
            }
        });

    }

    private void updateSelectedControl(SelectableControl newValue) {

        if (newValue instanceof Suggestion) {
            ((BasicEditorView) ruleViewPane.getContent()).setSelectedNode(null);
        } else if ((newValue instanceof Element) || (newValue instanceof Term) || (newValue instanceof Link)) {
            ((BasicEditorView) suggestionViewPane.getContent()).setSelectedNode(null);
        }
        getSkinnable().selectControl(newValue);
        apSelection.reload();
    }
    private void setUpNotices(Node pRoot){
        LabeledTextArea taRuleNotice = (LabeledTextArea) pRoot.lookup("#taRuleNotice");
        taRuleNotice.setDisable(!getSkinnable().isEditable());
        LabeledTextArea taSuggNotice = (LabeledTextArea) pRoot.lookup("#taSuggNotice");
        taSuggNotice.setDisable(!getSkinnable().isEditable());
        LabeledTextArea taRiskNotice = (LabeledTextArea) pRoot.lookup("#taRiskNotice");
        taRiskNotice.setDisable(!getSkinnable().isEditable());
        
        try {
            taRuleNotice.setText(CaseRuleManager.getDisplayText(getRule().getRulesNotice()));
        } catch (ExecutionException ex) {
            Logger.getLogger(RuleEditorSkin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(RuleEditorSkin.class.getName()).log(Level.SEVERE, null, ex);
            Thread.currentThread().interrupt();
        }
        taRuleNotice.getControl().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    setNotice(newValue);
                    RuleChangedEvent saveEvent = new RuleChangedEvent(RuleChangedEvent.ruleChangedEvent(), this);
                    Event.fireEvent(taRuleNotice, saveEvent);
                } catch (ExecutionException ex) {
                    Logger.getLogger(RuleEditorSkin.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(RuleEditorSkin.class.getName()).log(Level.SEVERE, null, ex);
                    Thread.currentThread().interrupt();
                }
            }

            private void setNotice(String newValue) throws InterruptedException, ExecutionException {
                if (newValue == null || newValue.isEmpty()) {
                    getRule().setRulesNotice(null);
//                        rule.setRulesNotice(null);
                } else {
                    getRule().setRulesNotice(CaseRuleManager.getXMLText(newValue));
//                        rule.setRulesNotice(newValue);
                }
            }
        });
        
        try {
            taSuggNotice.setText(CaseRuleManager.getDisplayText(getRule().getSuggestions().getSuggtext()));
        } catch (ExecutionException ex) {
            Logger.getLogger(RuleEditorSkin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(RuleEditorSkin.class.getName()).log(Level.SEVERE, null, ex);
            Thread.currentThread().interrupt();
        }
        taSuggNotice.getControl().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    getRule().getSuggestions().setSuggtext(CaseRuleManager.getXMLText(newValue));
                    RuleChangedEvent saveEvent = new RuleChangedEvent(RuleChangedEvent.ruleChangedEvent(), this);
                    Event.fireEvent(taSuggNotice, saveEvent);
                } catch (ExecutionException ex) {
                    Logger.getLogger(RuleEditorSkin.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(RuleEditorSkin.class.getName()).log(Level.SEVERE, null, ex);
                    Thread.currentThread().interrupt();
                }
            }
        });
        
        taRiskNotice.setText(CaseRuleManager.getDisplayText(getRiskNotice()));
        taRiskNotice.getControl().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                getRule().getSuggestions().setSuggtext(CaseRuleManager.getXMLText(newValue));
                setRiskNotice(newValue);
                RuleChangedEvent saveEvent = new RuleChangedEvent(RuleChangedEvent.ruleChangedEvent(), this);
                Event.fireEvent(taRiskNotice, saveEvent);
            }
        });
    }

    /**
     * forces update of the rule object, should be called before storing rule
     * TODO: possible to handle with rule change event to autoupdate rule.with
     * every change
     */
    public void updateRule() {
        try {
            //transform rule to text and update the definition in crg rule object
            Rule xmlRule = getSkinnable().getStateManager().getCurrentStateItem();
            byte[] rulebytes = CaseRuleManager.transformObject(getSkinnable().getStateManager().getCurrentStateItem(), "UTF-16");
            getSkinnable().getRule().setCrgrDefinition(rulebytes);
            getSkinnable().getRule().setCrgrNote(xmlRule.getRulesNotice());
            getSkinnable().getRule().setCrgrCaption(xmlRule.getCaption());
            getSkinnable().getRule().setCrgrCategory(xmlRule.getText());//ErrrorType());
            getSkinnable().getRule().setCrgrRuleErrorType(RuleTypeEn.findByInternalKey(xmlRule.getTyp()));
//            getSkinnable().getRule().setCrgRuleTypes(xmlRule.getTyp());
            getSkinnable().getRule().setCrgrValidFrom(parseDate(xmlRule.getFrom()));
            getSkinnable().getRule().setCrgrValidTo(parseDate(xmlRule.getTo()));
            getSkinnable().getRule().setCrgrNumber(xmlRule.getNumber());
            getSkinnable().getRule().setCrgrFeeGroup(xmlRule.getFeegroup());
            if (xmlRule.getSuggestions() != null) {
                getSkinnable().getRule().setCrgrSuggText(xmlRule.getSuggestions().getSuggtext());
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(RuleEditorSkin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Date parseDate(String pDate) {
        if (pDate == null) {
            return null;
        }
        if(pDate.isEmpty()){
            return null;
        }
        return Date.valueOf(LocalDate.parse(pDate, DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }
    private static final Logger LOG = Logger.getLogger(RuleEditorSkin.class.getName());

    //trigger reload and update for new rule, do nothing if rule is null
    private void updateRuleData(CrgRules pRule) {
        if (pRule == null) {
            return;
        }
//        getSkinnable().getStateManager().clear();
        ruleViewPane.reload();
        suggestionViewPane.reload();
    }

    //init root, loads fxml and setup areas in the ui
    private Parent initRoot() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/RuleEditor1.fxml"));
        //pseudo hack, set skin to force loading of children
        //now other items could be looked up.. otherwise methodes should wait till rendering
        //its also messy
        SplitPane spMenu = (SplitPane) root.lookup("#spMenu");
        spMenu.setSkin(new SplitPaneSkin(spMenu));

        spContainer = (SplitPane) root.lookup("#spContainer");
        spContainer.setSkin(new SplitPaneSkin(spContainer));

        //init components in sections
        initAnalyser(root);
        initDrawBoard(root);
        initRibbon(root);
        initSettings(root);
        return root;
    }

    //drawboard, central pane where rule and suggestions are displayed
    private void initDrawBoard(Parent root) {

        SplitPane spDrawBoard = (SplitPane) root.lookup("#spDrawBoard");
        spDrawBoard.setSkin(new SplitPaneSkin(spDrawBoard));
        VBox boxRuleContent = (VBox) root.lookup("#boxRuleContent");
        AsyncPane<RuleView> ruleContent = setUpRuleContent();
        VBox.setVgrow(ruleContent, Priority.ALWAYS);
        boxRuleContent.getChildren().add(ruleContent);

        VBox boxSuggestionContent = (VBox) root.lookup("#boxSuggestionContent");
        AsyncPane<SuggestionView> suggestionContent = setUpSuggestionContent();
        VBox.setVgrow(suggestionContent, Priority.ALWAYS);
        boxSuggestionContent.getChildren().add(suggestionContent);

        SectionHeader shRule = (SectionHeader) root.lookup("#shRule");

    }

    //init settings, displayed on the right, show property sheets and notice texts
    private void initSettings(Parent root) {

        ScrollPane spInfo = (ScrollPane) root.lookup("#spInfo");
        spInfo.setSkin(new ScrollPaneSkin(spInfo));
        spInfo.fitToHeightProperty().bind(getSkinnable().fitToHeightProperty());

        VBox vBoxInfo = (VBox) root.lookup("#vBoxInfo");
        TitledPane tpRuleSettings = (TitledPane) root.lookup("#tpRuleSettings");
        tpRuleSettings.setSkin(new TitledPaneSkin(tpRuleSettings));
        tpRuleSettings.setText("Allgemeine Regeldaten");

        tpSelection = (TitledPane) root.lookup("#tpSelection");
        tpSelection.minWidthProperty().bind(vBoxInfo.widthProperty());
        tpSelection.maxWidthProperty().bind(vBoxInfo.widthProperty());
        tpSelection.setSkin(new TitledPaneSkin(tpSelection));
        tpSelection.setText("Nichts ausgewählt");

        //set propertysheet with updated values, needs not to be done on mainthread
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                setUpRuleContent();
            }
        });
        psItemSheet = new PropertySheet();
        psItemSheet.setMode(Mode.NAME);
        psItemSheet.setSearchBoxVisible(false);
        psItemSheet.setModeSwitcherVisible(false);
        apSelection = new AsyncPane<>() {
            @Override
            public PropertySheet loadContent() {
                SelectableControl newValue = getSkinnable().getSelectedControl();
//                psItemSheet = new PropertySheet();
//                psItemSheet.setMode(Mode.NAME);
//                psItemSheet.setSearchBoxVisible(false);
//                psItemSheet.setModeSwitcherVisible(false);
                if (newValue == null) {
                    psItemSheet.getItems().clear();
                    return psItemSheet;
                }
//                getSkinnable().selectControl(newValue);
                ActionShowItemInPropertySheet action = new ActionShowItemInPropertySheet(psItemSheet, "Itemdaten", newValue);
                action.handleAction(new ActionEvent());
                return psItemSheet;
            }

            @Override
            public void afterTask(Worker.State pState) {
                super.afterTask(pState);
                if (getSkinnable().getSelectedControl() == null) {
                    tpSelection.setText("Nichts ausgewählt");
                }
            }

            @Override
            public void refresh() {
                super.refresh();
                Event.fireEvent(psItemSheet, new RefreshEvent(EventType.ROOT, psItemSheet));
            }
            
        };
        apSelection.setPrefHeight(210.0);
        tpSelection.setContent(apSelection);

        apMetaData = new AsyncPane<>() {
            @Override
            public PropertySheet loadContent() {
                psMetaSheet = new PropertySheet();
//                psMetaSheet.setSkin(new PropertySheetSkin2(psMetaSheet));
                psMetaSheet.setMode(Mode.NAME);
                psMetaSheet.setSearchBoxVisible(false);
                psMetaSheet.setModeSwitcherVisible(false);
                try {
                    ActionShowMetaInPropertySheet action = new ActionShowMetaInPropertySheet("MetaData", getRule());
                    action.handleAction(new ActionEvent());
                } catch (ExecutionException ex) {
                    Logger.getLogger(RuleEditorSkin.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(RuleEditorSkin.class.getName()).log(Level.SEVERE, null, ex);
                    Thread.currentThread().interrupt();
                }
                return psMetaSheet;
            }

        };
//        apMetaData.heightProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                LOG.info("new height " + newValue.doubleValue());
//            }
//        });
        apMetaData.setPrefHeight(242.0);
        tpRuleSettings.setContent(apMetaData);

//        tpNotice = (TitledPane) root.lookup("#tpNotice");
        tpRisk = (TitledPane) root.lookup("#tpRisk");
        tpRisk.setText(getRiskText());
        tpRisk.minWidthProperty().bind(vBoxInfo.widthProperty());
        tpRisk.maxWidthProperty().bind(vBoxInfo.widthProperty());
        tpRisk.expandedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(getSkinnable().isShowRuleAnalyser() && (Objects.equals(newValue, Boolean.FALSE))){
                    return;
                }
                getSkinnable().setFitToHeight(!newValue);
            }
        });
        
        apRisk = new AsyncPane<>() {
            @Override
            public PropertySheet loadContent() {
                PropertySheet psRiskSheet = new PropertySheet();
                psRiskSheet.setMode(Mode.NAME);
                psRiskSheet.setSearchBoxVisible(false);
                psRiskSheet.setModeSwitcherVisible(false);
                ActionShowRiskInPropertySheet action;
                try {
                    action = new ActionShowRiskInPropertySheet(psRiskSheet,"RISK_DATA", new RiskEditorItem(getRule()));
                    action.handleAction(new ActionEvent());
                } catch (InterruptedException ex) {
                    Logger.getLogger(RuleEditorSkin.class.getName()).log(Level.SEVERE, null, ex);
                    //re throw to not lose interrupt? - sonar issue
                    Thread.currentThread().interrupt();
                } catch (ExecutionException ex) {
                    Logger.getLogger(RuleEditorSkin.class.getName()).log(Level.SEVERE, null, ex);
                }
                return psRiskSheet;
            }

        };
        apRisk.setPrefHeight(150.0);
        tpRisk.setContent(apRisk);
        
//        getSkinnable().setFitToHeight(!tpRisk.isExpanded());

        setUpNotices(root);
    }
    private Risk getRuleRisk(){
        try {
            return getRule().getRisks();
        } catch (InterruptedException ex) {
            Logger.getLogger(RuleEditorSkin.class.getName()).log(Level.SEVERE, null, ex);
            //re throw to not lose interrupt? - sonar issue
            Thread.currentThread().interrupt();
        } catch (ExecutionException ex) {
            Logger.getLogger(RuleEditorSkin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    private String getRiskText(){
        return RiskDisplayHelper.getRiskDisplayText(getRuleRisk());
    }
//    private void updateTitlePaneHeight(TitledPane pPane, boolean pIsExtended) {
//        VBox.setVgrow(pPane, pIsExtended ? Priority.ALWAYS : Priority.SOMETIMES);
//    }
    //init rule content
    private AsyncPane<RuleView> setUpRuleContent() {
        cleanUp();
        if (ruleViewPane == null) {
            //make loading async to increase reaction time in ui
            ruleViewPane = new AsyncPane<>() {
                private RuleView ruleView;

                @Override
                public RuleView loadContent() {
                    ruleView = new RuleView(getSkinnable().getPool(), getSkinnable().getRule());
                    ruleView.setViewMode(getSkinnable().getViewMode());
                    ruleView.setFont(Font.font("Courier New", 15));
                    ruleView.setMaxHeight(Double.MAX_VALUE);

                    //try to fetch rule async in future, task should wait if rule is fetched and than use instance
                    try {
                        ruleView.setRule(getRule());
//                        ruleView.saveState(getRule());
//                        ruleView.setCrgRules(getSkinnable().getRule());
                    } catch (ExecutionException ex) {
                        Logger.getLogger(RuleEditorSkin.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(RuleEditorSkin.class.getName()).log(Level.SEVERE, null, ex);
                        Thread.currentThread().interrupt();
                    }

                    //update property sheet if selection changes
                    ruleView.selectedNodeProperty().addListener(new ChangeListener<Node>() {
                        @Override
                        public void changed(ObservableValue<? extends Node> observable, Node oldValue, Node newValue) {
                            updateSelectedControl((SelectableControl) newValue);
                        }
                    });
//                    ruleView.setRuleMessage(new RuleMessageReader().readRuleForDisplay(getSkinnable().getRule()));
                    return ruleView;
                }

                @Override
                public void refresh() {
                    ruleView.refresh();
                    super.refresh();
                }

                @Override
                public void afterTask(Worker.State pState) {
                    super.afterTask(pState); //To change body of generated methods, choose Tools | Templates.
                    ruleView.setRuleMessages(new RuleMessageReader().read(getSkinnable().getRule()));
                }
                
            };
        }
        return ruleViewPane;
    }

    //set up overview over suggestions
    private AsyncPane<SuggestionView> setUpSuggestionContent() {
        cleanUp();
        if (suggestionViewPane == null) {
            suggestionViewPane = new AsyncPane<>() {
                private SuggestionView suggView;

                @Override
                public SuggestionView loadContent() {
                    //load async to decrease loading times
                    suggView = new SuggestionView();
                    suggView.setFont(Font.font("Courier New", 15));
                    suggView.setViewMode(getSkinnable().getViewMode());
                    suggView.setMaxHeight(Double.MAX_VALUE);
                    try {
                        //set rule and wait till fetching is complete, otherwise trouble some if suggestion and rule do not edit the same ruleobject!
                        suggView.setRule(getRule());
                    } catch (ExecutionException ex) {
                        Logger.getLogger(RuleEditorSkin.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(RuleEditorSkin.class.getName()).log(Level.SEVERE, null, ex);
                        Thread.currentThread().interrupt();
                    }
                    suggView.selectedNodeProperty().addListener(new ChangeListener<Node>() {
                        @Override
                        public void changed(ObservableValue<? extends Node> observable, Node oldValue, Node newValue) {
                            updateSelectedControl((SelectableControl) newValue);
                        }
                    });
                    return suggView;
                }

                @Override
                public void refresh() {
                    suggView.refresh();
                    super.refresh();
                }
                
                @Override
                public void afterTask(Worker.State pState) {
                    super.afterTask(pState); //To change body of generated methods, choose Tools | Templates.
                    suggView.setRuleMessages(new RuleMessageReader().read(getSkinnable().getRule()));
                }
            };
        }
        return suggestionViewPane;
    }

    /**
     * @return fetch new rule! or return loaded one
     * @throws InterruptedException loading is interrupted somehow
     * @throws ExecutionException if loading failed
     */
    protected Rule getRule() throws InterruptedException, ExecutionException {
//        if (rule == null) {
//            rule = fetchRule();
        if (getSkinnable().getStateManager().getCurrentStateItem() == null) {
            getSkinnable().getStateManager().init(fetchRule());
//        }
        }
        return getSkinnable().getStateManager().getCurrentStateItem();
    }

    /**
     * @return force to fetch new xml rule form entity
     * @throws InterruptedException loading is interrupted somehow
     * @throws ExecutionException if loading failed
     */
    protected Rule fetchRule() throws InterruptedException, ExecutionException {
        if (ruleResult == null) {
            ExecutorService pool = Executors.newFixedThreadPool(2);
            //trigger loading
            ruleResult = pool.submit(new RuleParser());
            LOG.log(Level.SEVERE, "fetch rule!");
        }
        //wait till future result is finished
        return ruleResult.get();
    }

    //cleanup dummy
    private void cleanUp() {
    }

    /*
    Default object to be added in rule views
     */
    private RulesOperator getDefaultOperator() {
        RulesOperator op = new RulesOperator();
        op.setOpType("&&");
        op.hashCode();
        return op;
    }

    private RulesElement getDefaultElement() {
        RulesElement element = new RulesElement();
        return element;
    }

    private RulesValue getDefaultTerm() {
        RulesValue term = new RulesValue();
        //add default value to wert, due to not trigger updateRule() prematurely
        //after rules value is added in structure in ruleview
        term.setWert("");
        return term;
    }

    private Sugg getDefaultSuggestion() {
        Sugg suggestion = new Sugg();
        return suggestion;
    }

    //init ribbon with children
    private void initRibbon(Parent root) {
        Ribbon ribbonTools = (Ribbon) root.lookup("#ribbonTools");
        ribbonTools.setDisable(!getSkinnable().isEditable());
        //TODO: unify some behavior for other ribbon items

        //link group!
        RibbonItemGroup link = new RibbonItemGroup();
        link.setTitleSide(Side.RIGHT);
        link.setTitle("Verknüpfung");
        //create some simple button to display in ribbon group
        //itemgroup can store all kinds of nodes!
        Button one = new Button();
        //specify what should happen on drag
        one.setOnDragDetected(new DragDetectedHandler(one) {
            @Override
            public List<Object> getControlList() {
                //if this button is dragged, a default/empty operator is created and stored in dragevent to be added in the right place
                return Lists.newArrayList(getDefaultOperator());
            }
        });
        //one.setGraphic(new ImageView(getClass().getResource("/img/toolbar_item_link.png").toExternalForm()));
        one.setGraphic(new ImageView(getClass().getResource("/img/Verk_27x27.png").toExternalForm()));
        one.setTooltip(new CpxTooltip("Einfache Verknüpfung"));

        Button two = new Button();
        two.setOnDragDetected(new DragDetectedHandler(one) {
            @Override
            public List<Object> getControlList() {
                return Lists.newArrayList(getDefaultOperator(), getDefaultElement());
            }
        });
        //two.setGraphic(new ImageView(getClass().getResource("/img/toolbar_item_link_element.png").toExternalForm()));
        two.setGraphic(new ImageView(getClass().getResource("/img/VerkKlammerTerm_27x27.png").toExternalForm()));
        two.setTooltip(new CpxTooltip("Einfache Verknüpfung mit einer Klammer"));

        Button three = new Button();
        three.setOnDragDetected(new DragDetectedHandler(one) {
            @Override
            public List<Object> getControlList() {
                return Lists.newArrayList(getDefaultOperator(), getDefaultTerm());
            }
        });
        three.setTooltip(new CpxTooltip("Einfache Verknüpfung mit einem Term"));
        //three.setGraphic(new ImageView(getClass().getResource("/img/toolbar_item_Link_term.png").toExternalForm()));
        three.setGraphic(new ImageView(getClass().getResource("/img/VerkTerm_27x27.png").toExternalForm()));
        link.getItems().addAll(one, two, three);

        //suggestion group!
        RibbonItemGroup sugg = new RibbonItemGroup();
        sugg.setTitleSide(Side.RIGHT);
        sugg.setTitle("Vorschlag");
        Button four = new Button();
        four.setOnDragDetected(new DragDetectedHandler(four) {
            @Override
            public List<Object> getControlList() {
                return Lists.newArrayList(getDefaultSuggestion());
            }
        });
        four.setTooltip(new CpxTooltip("Vorschlag"));
        four.setGraphic(new ImageView(getClass().getResource("/img/Vorschlag_27x27.png").toExternalForm()));
        sugg.getItems().addAll(four);

        //term group
        RibbonItemGroup term = new RibbonItemGroup();
        term.setTitleSide(Side.RIGHT);
        term.setTitle("Terme");

        Button five = new Button();
        five.setOnDragDetected(new DragDetectedHandler(one) {
            @Override
            public List<Object> getControlList() {
                return Lists.newArrayList(getDefaultElement());
            }
        });
        five.setTooltip(new CpxTooltip("Einfache Klammer"));
        //five.setGraphic(new ImageView(getClass().getResource("/img/toolbar_item_element.png").toExternalForm()));
        five.setGraphic(new ImageView(getClass().getResource("/img/KlammerTerm_27x27.png").toExternalForm()));

        Button six = new Button();
        six.setOnDragDetected(new DragDetectedHandler(one) {
            @Override
            public List<Object> getControlList() {
                return Lists.newArrayList(getDefaultTerm());
            }
        });
        six.setTooltip(new CpxTooltip("Einfacher Term"));
        //six.setGraphic(new ImageView(getClass().getResource("/img/toolbar_item_term.png").toExternalForm()));
        six.setGraphic(new ImageView(getClass().getResource("/img/Term_27x27.png").toExternalForm()));

        Button seven = new Button();
        seven.setOnDragDetected(new DragDetectedHandler(one) {
            @Override
            public List<Object> getControlList() {
                return Lists.newArrayList(getDefaultTerm(), getDefaultOperator());
            }
        });
//        registerDragDetected(seven, getDefaultTerm(),getDefaultOperator());
        seven.setTooltip(new CpxTooltip("Einfacher Term mit einer Verknüpfung"));
        //seven.setGraphic(new ImageView(getClass().getResource("/img/toolbar_item_term_link.png").toExternalForm()));
        seven.setGraphic(new ImageView(getClass().getResource("/img/TermVerk_27x27.png").toExternalForm()));

        Button eight = new Button();
        eight.setOnDragDetected(new DragDetectedHandler(one) {
            @Override
            public List<Object> getControlList() {
                return Lists.newArrayList(getDefaultTerm(), getDefaultOperator(), getDefaultTerm());
            }
        });
        eight.setTooltip(new CpxTooltip("2 Terme mit einer Verknüpfung"));
        //eight.setGraphic(new ImageView(getClass().getResource("/img/toolbar_item_2terms.png").toExternalForm()));
        eight.setGraphic(new ImageView(getClass().getResource("/img/TermVerkTerm_27x27.png").toExternalForm()));
        term.getItems().addAll(five, six, seven, eight);

        //add ribbon item, and add groups to item
        RibbonItem lib = new RibbonItem();
        lib.setText("Bibliothek");
        lib.getItems().addAll(term, link, sugg);
        //here more items could be added
        ribbonTools.getItems().addAll(lib);
    }

    private void handleOnAnalyser() {
//        if(analyser == null){
//            analyser = createAnalyser();
//        }
        if (spContainer.getItems().contains(getSkinnable().getRuleAnalyser())) {
            spContainer.getItems().remove(getSkinnable().getRuleAnalyser());
//            btnOpenAnalyser.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.O);
            if(!tpRisk.isExpanded()){
                getSkinnable().setFitToHeight(Boolean.TRUE);
            }
            getSkinnable().setShowRuleAnalyser(false);
        } else {
            spContainer.getItems().add(getSkinnable().getRuleAnalyser());
            spContainer.getDividers().get(0).setPosition(0.61);
//            btnOpenAnalyser.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.CLOSE));
            getSkinnable().setFitToHeight(Boolean.FALSE);
            getSkinnable().setShowRuleAnalyser(true);
        }
    }
//    private RuleAnalyser createAnalyser(){
//        RuleAnalyser ruleAnalyser = new RuleAnalyser();
//        ruleAnalyser.addCase(firstTestCase("1"));
//        ruleAnalyser.addCase(secondTestCase("2"));
//        ruleAnalyser.addCase(thirdTestCase("3"));
//        ruleAnalyser.setOnAnalyse(new Callback<TCase, TransferRuleResult>() {
//            @Override
//            public TransferRuleResult call(TCase p) {
//                try {
//                    if(p == null){
//                        return null;
//                    }
//                    Map<String, String> tables = editor.getMapOfRuleTables();
//                    return getScene().analyseRule(editor.getRuleXml(),p,tables);
//                } catch (UnsupportedEncodingException ex) {
//                    Logger.getLogger(RuleEditorSkin.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                return null;
//            }
//        });
//        return ruleAnalyser;
//    }

    private void initAnalyser(Parent pRoot) {
        HBox boxAnalyserHeader = (HBox) pRoot.lookup("#boxAnalyserHeader");
        boxAnalyserHeader.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                handleOnAnalyser();
            }
        });
        Button btnOpenAnalyzer = (Button) pRoot.lookup("#btnOpenAnalyzer");
        btnOpenAnalyzer.graphicProperty().bind(Bindings
                .when(getSkinnable().fitToHeightProperty())
                .then(ResourceLoader.rotate(FontAwesome.Glyph.SIGN_OUT, -90.0))
                .otherwise(ResourceLoader.rotate(FontAwesome.Glyph.SIGN_IN, 90.0))
        );
        btnOpenAnalyzer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleOnAnalyser();
            }
        });
    }

    private String getRiskNotice() {
        Risk risk = getRuleRisk();
        if(risk == null || risk.getRiskAreas().isEmpty()){
            return null;
        }
        return risk.getRiskAreas().get(0).getRiskComment();
    }
    
    
    private void setRiskNotice(String newValue) {
        Risk risk = getRuleRisk();
        if(risk == null || risk.getRiskAreas().isEmpty()){
            return;
        }
        for(RiskArea area : risk.getRiskAreas()){
            //make value null if no text is entered
            if(newValue != null && newValue.isEmpty()){
                newValue = null;
            }
            area.setRiskComment(newValue);
        }
    }

    /**
     * drag handler implementation, to not implement stuff 5 or 6 times
     */
    protected abstract class DragDetectedHandler implements EventHandler<MouseEvent> {

        private final Control ctrl;

        /**
         * creaes new drag handler for a control where drag should be
         * overwatched
         *
         * @param pCtrl control where event could occure
         */
        public DragDetectedHandler(Control pCtrl) {
            ctrl = pCtrl;
        }

        @Override
        public void handle(MouseEvent event) {
            /* drag was detected, start a drag-and-drop gesture*/
 /* allow any transfer mode */
            Dragboard db = ctrl.startDragAndDrop(TransferMode.ANY);
            /* Put a string on a dragboard */
            ClipboardContent content = new ClipboardContent();
            //get control list and add the add_format
            content.put(DndFormat.ADD_FORMAT, getControlList());
            db.setContent(content);
        }

        /**
         * @return list of object that should be added
         */
        public abstract List<Object> getControlList();
    }

    //here is the task that displays proeprties of an selected item in the corresponding propertysheet!
    //properties to be shown are specified in the selectable control implementation!
    private class ActionShowItemInPropertySheet extends Action {

        private final Object bean;
        private final PropertySheet sheet;

        /**
         * creates new instance
         *
         * @param title title of the action task
         * @param pBean selectable control to fetch properties for
         */
        public ActionShowItemInPropertySheet(PropertySheet pSheet, String title, Object pBean) {
            super(title);
            long start = System.currentTimeMillis();
            bean = pBean;
            sheet = pSheet;
            setEventHandler(this::handleAction);
            LOG.info("init update proeprty service " + (System.currentTimeMillis() - start));
        }

        private void handleAction(ActionEvent ae) {
            long start = System.currentTimeMillis();
            if (bean == null) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tpSelection.setText("Nichts ausgewählt");
                        sheet.getItems().clear();
                    }
                });
                return;
            }
            ObservableList<PropertySheet.Item> list = FXCollections.observableArrayList();
            //read all properties and add these to the sheet
            for (PropertyDescriptor p : ((SelectableControl) bean).getPropertyDescriptors()) {
                if (p == null) {
                    continue;
                }
                BeanProperty beanProp = new BeanProperty(bean, p);
                beanProp.setEditable(getSkinnable().isEditable());
                list.add(beanProp);
            }
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    sheet.getItems().setAll(list);
                }
            });
            ((Node) bean).getProperties().removeListener(updateSettingListener);
            updateSettingListener.setBean(bean);
            ((Node) bean).getProperties().addListener(updateSettingListener);
            //update display text after loading finished
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    tpSelection.setText(((SelectableControl) bean).getDisplayText());
                }
            });
            LOG.info("time to handle actionevent " + (System.currentTimeMillis() - start));
        }
    }
    private final ItemUpdateListener updateSettingListener = new ItemUpdateListener();

    private class ItemUpdateListener implements MapChangeListener<Object, Object> {

        private Object bean;

        public void setBean(Object pBean) {
            bean = pBean;
        }

        @Override
        public void onChanged(Change<? extends Object, ? extends Object> change) {
            if (change.wasAdded() && SelectableControl.UPDATE_CONTROL.equals(change.getKey())) {
                tpSelection.setText(((SelectableControl) bean).getDisplayText());
                ((Node) bean).getProperties().remove(SelectableControl.UPDATE_CONTROL);
            }
        }

        public void dispose() {
            ((Node) bean).getProperties().removeListener(updateSettingListener);
            bean = null;
        }
    }
    
    //here is the task that displays proeprties of an selected item in the corresponding propertysheet!
    //properties to be shown are specified in the selectable control implementation!
    private class ActionShowRiskInPropertySheet extends Action {

        private final RiskEditorItem bean;
        private final PropertySheet sheet;

        /**
         * creates new instance
         *
         * @param title title of the action task
         * @param pBean selectable control to fetch properties for
         */
        public ActionShowRiskInPropertySheet(PropertySheet pSheet, String title, RiskEditorItem pBean) {
            super(title);
            long start = System.currentTimeMillis();
            bean = pBean;
            sheet = pSheet;
            
            bean.getProperties().addListener(new MapChangeListener<String, Object>() {
                @Override
                public void onChanged(MapChangeListener.Change<? extends String, ? extends Object> change) {
                    if (change.wasAdded() && RiskEditorItem.UPDATE_RISK.equals(change.getKey())) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                tpRisk.setText(getRiskText());
                            }
                        });
                        bean.getProperties().remove(RiskEditorItem.UPDATE_RISK);
                    }
                }
            });
            
            setEventHandler(this::handleAction);
            LOG.log(Level.INFO, "init update proeprty service for risk{0}", System.currentTimeMillis() - start);
        }
        
        private void handleAction(ActionEvent ae) {
            long start = System.currentTimeMillis();
            if (bean == null) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tpRisk.setText(getRiskText());
                        sheet.getItems().clear();
                    }
                });
                return;
            }
            ObservableList<PropertySheet.Item> list = FXCollections.observableArrayList();
            for (PropertyDescriptor p : getPropertyDiscriptiors(bean)) {
                BeanProperty beanProp = new BeanProperty(bean, p);
                beanProp.setEditable(getSkinnable().isEditable());
                list.add(beanProp);
            }
            //add all items to propertysheet
            sheet.getItems().setAll(list);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    tpRisk.setText(getRiskText());
                }
            });
            LOG.log(Level.INFO, "time to handle actionevent for risk{0}", System.currentTimeMillis() - start);
        }
        private PropertyDescriptor[] getRiskPropertyDiscriptors(Object bean) throws IntrospectionException {
            PropertyDescriptor[] props = null;
            if (bean instanceof RiskEditorItem) {
                props = new PropertyDescriptor[4];
                props[0] = new PropertyDescriptor("riskArea", RiskEditorItem.class,"getRiskAreas", "setRiskAreas");
//                props[0].setShortDescription("Prüfbereich: Definiert die Zuordnung des Risikos");
//                props[0].setDisplayName("Prüfbereich:");
                setPropertyTexts(props[0], Lang.getRiskAreaObj());
                props[0].setPropertyEditorClass(RiskAreaEditor.class);
                props[0].setBound(true);

                props[1] = new PropertyDescriptor("riskAuditValue", RiskEditorItem.class,"getRiskAuditValue", "setRiskAuditValue");
//                props[1].setDisplayName("Anfrage-Risiko:");
//                props[1].setShortDescription("Anfrage-Risiko: Prozentualer-Wert für die Wahrscheinlichkeit, dass ein Fall mit diesem Regelanschlag eine Prüfauffälligkeit verursacht");
                setPropertyTexts(props[1], Lang.getRiskEditorRiskAuditValueObj());
                props[1].setPropertyEditorClass(RiskAuditValueEditor.class);//StringEditor.class);
                props[1].setBound(true);
                
                props[2] = new PropertyDescriptor("riskWasteValue", RiskEditorItem.class,"getRiskWasteValue", "setRiskWasteValue");
                setPropertyTexts(props[2], Lang.getRiskEditorRiskWasteValueObj());
//                props[2].setDisplayName("Verlust-Risiko:");
//                props[2].setShortDescription("Verlust-Risiko: Prozentualer-Wert für die Wahrscheinlichkeit, dass es im Zuge einer Anfrage zu einer Rechnungskürzung kommt");
                props[2].setPropertyEditorClass(RiskWasteValueEditor.class);//StringEditor.class);
                props[2].setBound(true);

                props[3] = new PropertyDescriptor("riskDefaultWasteValue", RiskEditorItem.class,"getRiskDefaultWasteValue", "setRiskDefaultWasteValue");
                setPropertyTexts(props[3], Lang.getRiskEditorRiskWasteDefaultValueObj());
//                props[3].setDisplayName("Verlustwert (Optional):");
//                props[3].setShortDescription("Verlustwert (Optional): Definiert ein theoretischen Verlustwert für das Risiko.\n"
//                        + "Wird zur Berechnung des Verlustvolumens benutzt, wenn kein Vorschlag in der Regel definiert ist.");
                props[3].setPropertyEditorClass(WasteDefaultValueEditor.class);

                return props;
            }
            return props;
        }
        
        private PropertyDescriptor[] getPropertyDiscriptiors(Object bean) {
            try {
                return getRiskPropertyDiscriptors(bean);
            } catch (IntrospectionException ex) {
                Logger.getLogger(RuleEditorSkin.class.getName()).log(Level.SEVERE, null, ex);
            }
            return new PropertyDescriptor[0];
        }
        //text stuff should move in new implementation of property descriptor
        private void setPropertyTexts(PropertyDescriptor pPropDesc, Translation pTranslation){
            setPropertyTexts(pPropDesc, pTranslation.getValue(), pTranslation.getTooltip());
        }
        private void setPropertyTexts(PropertyDescriptor pPropDesc, String pTitle, String pDescription){
            pTitle = Objects.requireNonNullElse(pTitle, "n.A.");
            pDescription = Objects.requireNonNullElse(pDescription, "n.A.");
            String title = new StringBuilder().append(pTitle).append(":").toString();
            String description = new StringBuilder().append(title).append("\n").append(pDescription).toString();
            pPropDesc.setDisplayName(title);
            pPropDesc.setShortDescription(description);
        }
    }
    
    //action to show Rule meta data stored in entity
    private class ActionShowMetaInPropertySheet extends Action {

        private final Object bean;

        public ActionShowMetaInPropertySheet(String title, Object pBean) {
            super(title);
            setEventHandler(this::handleAction);
            bean = pBean;
        }

        private void handleAction(ActionEvent ae) {
            // retrieving bean properties may take some time
            // so we have to put it on separate thread to keep UI responsive
            ObservableList<PropertySheet.Item> list = FXCollections.observableArrayList();
            if (bean == null) {
                return;
            }
            for (PropertyDescriptor p : getPropertyDiscriptiors(bean)) {
                BeanProperty beanProp = new BeanProperty(bean, p);
                beanProp.setEditable(getSkinnable().isEditable());
                list.add(beanProp);
            }

            if (psMetaSheet == null) {
                return;
            }
            //add all items to propertysheet
            psMetaSheet.getItems().setAll(list);

        }

        private PropertyDescriptor[] getMetaPropertyDiscriptors(Object bean) throws IntrospectionException {
            PropertyDescriptor[] props = null;
            if (bean instanceof Rule) {
                props = new PropertyDescriptor[7];
                props[0] = new PropertyDescriptor("caption", Rule.class, "getCaption", "setCaption");
                props[0].setDisplayName("Bezeichnung:");
                props[0].setPropertyEditorClass(CaptionEditor.class);
                props[0].setBound(true);

                props[1] = new PropertyDescriptor("number", Rule.class, "getNumber", "setNumber");
                props[1].setDisplayName("Nummer:");
                props[1].setPropertyEditorClass(NumberAndFeeEditor.class);//StringEditor.class);
                props[1].setBound(true);

                props[2] = new PropertyDescriptor("text", Rule.class, "getText", "setText");
                props[2].setDisplayName("Kategorie:");
                props[2].setPropertyEditorClass(CategoryEditor.class);

                props[3] = new PropertyDescriptor("typ", Rule.class, "getTyp", "setTyp");
                props[3].setDisplayName("Fehlerart:");
                props[3].setPropertyEditorClass(RuleTypeChoiceEditor2.class);

                props[4] = new PropertyDescriptor("from", Rule.class);
                props[4].setDisplayName("von:");
                props[4].setPropertyEditorClass(FromToDateEditor.class);

                props[5] = new PropertyDescriptor("errrorType", Rule.class, "getErrrorType", "setErrrorType");
                props[5].setDisplayName("Regeltyp:");
                props[5].setPropertyEditorClass(ErrorTypeEditor.class);

                props[6] = new PropertyDescriptor("role", Rule.class, "getRole", "setRole");
                props[6].setDisplayName("Rollen:");
                props[6].setPropertyEditorClass(RoleEditor.class);
                return props;
            }
            return props;
        }

        private PropertyDescriptor[] getPropertyDiscriptiors(Object bean) {
            try {
                return getMetaPropertyDiscriptors(bean);
            } catch (IntrospectionException ex) {
                Logger.getLogger(RuleEditorSkin.class.getName()).log(Level.SEVERE, null, ex);
            }
            return new PropertyDescriptor[0];
        }
    }

    /**
     * class to parse a rule, tansform byte array(encoded utf-16) from the
     * entity object to xml rule object
     */
    public class RuleParser implements Callable<Rule> {

        /**
         * no-arg constructor
         */
        public RuleParser() {
        }

        @Override
        public Rule call() {
            //get entity
            Rule xmlRule = CaseRuleManager.transformRuleInUTF16(getSkinnable().getRule().getCrgrDefinition());
            if (xmlRule.getRulesNotice() == null || xmlRule.getRulesNotice().isEmpty()) {
                //set rule notice it is not set, should not occure, except in testing with TestRule
                xmlRule.setRulesNotice(getSkinnable().getRule().getCrgrNote());
            }
            return xmlRule;
        }
    }
}
