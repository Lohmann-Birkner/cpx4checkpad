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
package de.lb.cpx.client.ruleeditor.menu;

import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.client.ruleeditor.login.ReLoginScene;
import de.lb.cpx.client.ruleeditor.util.JsonMessageHelper;
import de.lb.cpx.grouper.model.transfer.TransferRuleAnalyseResult;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.ruleviewer.analyser.RuleAnalyser;
import de.lb.cpx.ruleviewer.analyser.RuleAnalyserSkin;
import de.lb.cpx.ruleviewer.editor.RuleEditor;
import de.lb.cpx.ruleviewer.enums.ViewMode;
import de.lb.cpx.ruleviewer.event.RuleChangedEvent;
import de.lb.cpx.ruleviewer.event.RuleTableChangedEvent;
import de.lb.cpx.server.commonDB.model.CCase;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Callback;
import org.controlsfx.glyphfont.FontAwesome;

/**
 * FXML Controller class
 *
 * @author wilde
 */
public class RuleEditorFXMLController extends Controller<RuleEditorScene> {

    @FXML
    private HBox boxContent;
    private RuleEditor editor;
    @FXML
    private Label lblRuleNumber;
    @FXML
    private Label lblPool;
    @FXML
    private Label lblRuleNumberValue;
    @FXML
    private Label lblPoolValue;
    @FXML
    private Button btnSave;
    @FXML
    private HBox boxMenu;
    private RuleAnalyser analyser;
    @FXML
    private SplitPane spContainer;

    /**
     * Initializes the controller class.
     *
     * @param url url
     * @param rb rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        editor = new RuleEditor();
        HBox.setHgrow(editor, Priority.ALWAYS);
        boxContent.getChildren().add(editor);
        lblPool.setText("Pool:");
        lblRuleNumber.setText("Regel:");
        btnSave.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.SAVE));
        btnSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                onSave();
            }
        });
        editor.setValidationCalllback(new Callback<CrgRules, byte[]>() {
            @Override
            public byte[] call(CrgRules p) {
                return getScene().validateRule(p);
            }
        });
//        btnOpenAnalyser.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.WRENCH));

//        Button btnUndo = new Button();
//        btnUndo.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent t) {
//                if(editor!=null){
//                    editor.getStateManager().undo();
//                }
//            }
//        });
//        btnUndo.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.UNDO));
//        boxMenu.getChildren().add(0, btnUndo);
    }

    private void updateMetaData() {
        lblPoolValue.setText(getScene().getPool() != null ? getScene().getPool().getCrgplIdentifier() : "----");
        lblRuleNumberValue.setText((getScene().isUnsaved() ? "* " : "") + (getScene().getRule() != null ? getScene().getRule().getCrgrNumber() : "----"));
    }
    
    
    private void discardRuleChange() {
        editor.discardRuleChange();
        CrgRules rule = editor.getRule();
        rule.setCrgrMessage(getScene().getRuleMessage(rule)); //get last sved rule message from server -> should still be vaild after all changes are discarded
    }
    
    @Override
    public void afterInitialisingScene() {
        super.afterInitialisingScene(); //To change body of generated methods, choose Tools | Templates.
        btnSave.setDisable(!getScene().isEditable());
        editor.setViewMode(getScene().isEditable() ? ViewMode.READ_WRITE : ViewMode.READ_ONLY);
        editor.ruleProperty().bind(getScene().ruleProperty());
        editor.poolProperty().bind(getScene().poolProperty());
//        lblPoolValue.textProperty().bind(Bindings.createStringBinding(new Callable<String>() {
//            @Override
//            public String call() throws Exception {
//                return getScene().getPool()!=null?getScene().getPool().getCrgplIdentifier():"----";
//            }
//        }, getScene().poolProperty()));
//        lblRuleNumberValue.textProperty().bind(Bindings.createStringBinding(new Callable<String>() {
//            @Override
//            public String call() throws Exception {
//                return getScene().getRule()!=null?getScene().getRule().getCrgrNumber():"----";
//            }
//        }, getScene().ruleProperty()));
        getScene().ruleProperty().addListener(new ChangeListener<CrgRules>() {
            @Override
            public void changed(ObservableValue<? extends CrgRules> ov, CrgRules t, CrgRules t1) {
                updateMetaData();
            }
        });
        getScene().getProperties().addListener(new MapChangeListener<Object, Object>() {
            @Override
            public void onChanged(MapChangeListener.Change<? extends Object, ? extends Object> change) {
                if (change.wasAdded()) {
                    if (RuleEditorScene.UPDATE_METADATA.equals(change.getKey())) {
                        updateMetaData();
                        getScene().getProperties().remove(RuleEditorScene.UPDATE_METADATA);
                    }
                    if (RuleEditorScene.DISCARD_RULE_CHANGE.equals(change.getKey())) {
                        discardRuleChange();
                        getScene().getProperties().remove(RuleEditorScene.DISCARD_RULE_CHANGE);
                    }
                }
            }
        });
        getScene().unsavedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                updateMetaData();
            }
        });
        updateMetaData();
        editor.addEventFilter(RuleChangedEvent.ruleChangedEvent(), new EventHandler<RuleChangedEvent>() {
            @Override
            public void handle(RuleChangedEvent t) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if(editor == null){ //ignore case if editor is null -> this should only happen when rule is closed with unsaved content and the user select in the following dialog "save all changes"
                            return;
                        }
                        editor.refreshRuleMessage(t.isRevalidateRule());
                        if (editor.isShowRuleAnalyser()) {
                            editor.getRuleAnalyser().analyse();
                        }
                        getScene().setUnsaved(!editor.getStateManager().check());
                    }
                });
                t.consume();
            }
        });
        editor.addEventFilter(RuleTableChangedEvent.ruleTableChangedEvent(), new EventHandler<RuleTableChangedEvent>() {
            @Override
            public void handle(RuleTableChangedEvent event) {
                Event.fireEvent(getScene(), event);
                if (editor.isShowRuleAnalyser()) {
                    editor.getRuleAnalyser().analyse();
                }
            }
        });
        RuleAnalyser ruleAnalyser = editor.getRuleAnalyser();
//        ruleAnalyser.addCase(firstTestCase("1"));
//        ruleAnalyser.addCase(secondTestCase("2"));
//        ruleAnalyser.addCase(thirdTestCase("3"));
        ruleAnalyser.setOnAnalyse(new Callback<TCase, TransferRuleAnalyseResult>() {
            @Override
            public TransferRuleAnalyseResult call(TCase p) {
                try {
                    if (p == null) {
                        return null;
                    }
                    Map<String, String> tables = editor.getMapOfRuleTables();
                    return getScene().analyseRule(editor.getRuleXml(), p, tables);
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(RuleEditorFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }
                return null;
            }
        });
        
        ruleAnalyser.setHistoryAnalyse(new Callback<TCase, TransferRuleAnalyseResult>() {
            @Override
            public TransferRuleAnalyseResult call(TCase p) {
                try {
                    if (p == null) {
                        return null;
                    }
                    Map<String, String> tables = editor.getMapOfRuleTables();
                    
                    return getScene().analyseHistoryRule(editor.getRuleXml(), p, tables, ((RuleAnalyserSkin)ruleAnalyser.getSkin()).getHistoryCases() );
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(RuleEditorFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }
                return null;
            }
        });
        
        ruleAnalyser.setOnLoadHospitalCase(new Callback<CCase, TCase>() {
            @Override
            public TCase call(CCase pAnalyserCase) {
                return getScene().loadHospitalCase(pAnalyserCase);
            }
        });
        
        ruleAnalyser.setOnLoadHospitalCaseGrouped(new Callback< TCase, TCase>() {
            @Override
            public TCase call( TCase csCase) {

                if(csCase != null){
                    TGroupingResults res = getScene().getTmpGroupingResults4Case(csCase);
                    if(res != null){
                        Set<TGroupingResults> grRes = new HashSet<>();
                        grRes.add(res);
                        csCase.getCurrentLocal().setGroupingResultses(grRes);
                    }
                }
                return csCase;
            }
        });
        
        ruleAnalyser.setOnPerformGroup(new Callback< TCase, Void>() {
            @Override
            public Void call( TCase csCase) {

                if(csCase != null){
                    TGroupingResults res = getScene().getTmpGroupingResults4Case(csCase);
                    if(res != null){
                        Set<TGroupingResults> grRes = new HashSet<>();
                        grRes.add(res);
                        csCase.getCurrentLocal().setGroupingResultses(grRes);
                    }
                }
                return null;
            }
        });
        
        ruleAnalyser.setOnReloadAnalyserCases(new Callback<Boolean, List<CCase>>() {
            @Override
            public List<CCase> call(Boolean p) {
                return getScene().getAllAnalyserCasesForUser();
            }
        });
        ruleAnalyser.setOnDeleteAnalyserCases(new Callback<List<Long>, Boolean>() {
            @Override
            public Boolean call(List<Long> p) {
                return getScene().deleteAnalyserCases(p);
            }
        });
        ruleAnalyser.setOnSaveAnalyserCase(new Callback<CCase, Boolean>() {
            @Override
            public Boolean call(CCase p) {
                return getScene().saveAnalyserCase(p);
            }
        });
        ruleAnalyser.setOnSaveHospitalCase(new Callback<CCase, Boolean>() {
            @Override
            public Boolean call(CCase p) {
                return getScene().saveHospitalCaseContent(p.getId(), ruleAnalyser.getCase(p));
            }
        });
        ReLoginScene.registerBroadcastResultListener(analyserCaseChangeListener);
        ruleAnalyser.addCases(getScene().getAllAnalyserCasesForUser());

    }
    private final ChangeListener<long[]> analyserCaseChangeListener = new ChangeListener<long[]>() {
        @Override
        public void changed(ObservableValue<? extends long[]> ov, long[] t, long[] t1) {
            if (editor == null || editor.getRuleAnalyser() == null) {
                return;
            }
            List<CCase> cases = editor.getRuleAnalyser().getOnReloadAnalyserCases().call(Boolean.TRUE);
            if (cases == null) {
                return;
            }
            editor.getRuleAnalyser().getCaseList().setAll(cases);
        }
    };

    @Override
    public boolean close() {
        lblPoolValue.textProperty().unbind();
        lblRuleNumberValue.textProperty().unbind();
        editor = null;
        boxContent.getChildren().clear();
        btnSave.setOnAction(null);
        ReLoginScene.deRegisterBroadcastResultListener(analyserCaseChangeListener);
        return super.close(); //To change body of generated methods, choose Tools | Templates.
    }

//    @FXML
    private void onSave() {
        JsonMessageHelper.checkAndShowTransferCatalogError(editor.getRule(), (t) -> {
            if(ButtonType.YES.equals(t)){
                editor.updateRule();
                getScene().saveRule(editor.getRule());
//                editor.getRule().setCrgrMessage(getScene().getRuleMessage(editor.getRule()));
                Event.fireEvent(editor, new RuleChangedEvent(false));
                getScene().setUnsaved(false);
            }
        });
    }

    public void saveRule() {
        onSave();
    }

    @Override
    public void refresh() {
        if (editor != null) {
            editor.refresh();
        }
    }

    public void updateRole(long pRole) {
        if (editor != null) {
            editor.updateRole(pRole);
        }
    }

}
