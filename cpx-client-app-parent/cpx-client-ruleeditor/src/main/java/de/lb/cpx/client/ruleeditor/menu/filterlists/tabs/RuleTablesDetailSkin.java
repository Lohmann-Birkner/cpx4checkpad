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
package de.lb.cpx.client.ruleeditor.menu.filterlists.tabs;

import com.google.common.base.Objects;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.model.fx.button.EditCommentButton;
import de.lb.cpx.client.core.model.fx.button.SaveButton;
import de.lb.cpx.client.core.model.fx.button.TextModeToggleButton;
import de.lb.cpx.client.core.model.fx.section.SectionHeader;
import de.lb.cpx.client.core.model.fx.tableview.FilterBaseTableView;
import de.lb.cpx.client.core.model.fx.tableview.column.StringColumn;
import de.lb.cpx.client.core.model.fx.tooltip.CpxTooltip;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.client.ruleeditor.events.OpenRuleEvent; 
import de.lb.cpx.client.ruleeditor.events.UpdateRulesEvent;
import de.lb.cpx.client.ruleeditor.util.JsonMessageHelper;
import de.lb.cpx.ruleviewer.enums.ViewMode;
import de.lb.cpx.ruleviewer.event.RefreshEvent;
import de.lb.cpx.ruleviewer.model.RuleMessageIndicator;
import de.lb.cpx.ruleviewer.model.ruletable.content.StringContentView; 
import de.lb.cpx.ruleviewer.model.ruletable.content.model.Item;
import de.lb.cpx.server.commonDB.model.enums.RuleTableCategoryEn;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.shared.json.RuleTableMessage;
import de.lb.cpx.shared.json.RuleTableMessageReader;
import de.lb.cpx.shared.lang.Lang;
import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SkinBase;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.SplitPaneSkin;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.controlsfx.control.PopOver;
import org.controlsfx.glyphfont.FontAwesome;

/**
 * Skin class to display detail for CrgRuleTables
 *
 * @author wilde
 */
public class RuleTablesDetailSkin extends SkinBase<RuleTablesDetail> {

    private Label lblNameValue;
    private Label lblCreationDateValue;
    private Label lblCreationUserValue;
    private VBox boxTableContent;
    private VBox boxLinkedRules;
//    private RuleTableSearchPane flowPane;
    private TextField tfNameValue;
    private HBox hbNameValue;
    private SectionHeader shHeader;
    private SaveButton btnSave;
    private StringContentView strPane;
    private EditCommentButton btnEditComment ;
    private Label lblCategoryValue;
    private ComboBox<RuleTableCategoryEn> cbCategoryValue;
    private HBox hbCategoryValue;
    


    public RuleTablesDetailSkin(RuleTablesDetail pSkinnable) throws IOException {
        super(pSkinnable);
        getChildren().add(loadRoot());
        pSkinnable.ruleTablesProperty().addListener(new ChangeListener<CrgRuleTables>() {
            @Override
            public void changed(ObservableValue<? extends CrgRuleTables> observable, CrgRuleTables oldValue, CrgRuleTables newValue) {
                updateData(newValue);
            }
        });
//        updateData(pSkinnable.getRuleTables());
        pSkinnable.viewModeProperty().addListener(new ChangeListener<ViewMode>() {
            @Override
            public void changed(ObservableValue<? extends ViewMode> observable, ViewMode oldValue, ViewMode newValue) {
                enableEditMode(pSkinnable.isEditable());
            }
        });
        enableEditMode(pSkinnable.isEditable());
    }

    private void enableEditMode(boolean pEnable) {
        if (pEnable) {
            if (hbNameValue.getChildren().contains(lblNameValue)) {
                hbNameValue.getChildren().remove(lblNameValue);
            }
            if (!hbNameValue.getChildren().contains(tfNameValue)) {
                hbNameValue.getChildren().add(tfNameValue);
            }
            
            if (hbCategoryValue.getChildren().contains(lblCategoryValue)) {
                hbCategoryValue.getChildren().remove(lblCategoryValue);
            }
            if (!hbCategoryValue.getChildren().contains(cbCategoryValue)) {
                hbCategoryValue.getChildren().add(cbCategoryValue);
            }
            
            if (!shHeader.getMenuItems().contains(btnSave)) {
                shHeader.getMenuItems().add(btnSave);
            }
        } else {
            if (!hbNameValue.getChildren().contains(lblNameValue)) {
                hbNameValue.getChildren().add(lblNameValue);
            }
            if (hbNameValue.getChildren().contains(tfNameValue)) {
                hbNameValue.getChildren().remove(tfNameValue);
            }
            if (!hbCategoryValue.getChildren().contains(lblCategoryValue)) {
                hbCategoryValue.getChildren().add(lblCategoryValue);
            }
            if (hbCategoryValue.getChildren().contains(cbCategoryValue)) {
                hbCategoryValue.getChildren().remove(cbCategoryValue);
            }
            if (shHeader.getMenuItems().contains(btnSave)) {
                shHeader.getMenuItems().remove(btnSave);
            }
        }
        updateData(getSkinnable().getRuleTables());
    }

    private void updateData(CrgRuleTables pTable) {
//        long start = System.currentTimeMillis();
        if (pTable == null) {
            clearData();
            return;
        }
        if(pTable.getCrgtMessage()!=null){
            RuleMessageIndicator indicator = new RuleMessageIndicator();
            String msg;
            try {
                msg = new RuleTableMessageReader().read(pTable.getCrgtMessage(), "UTF-8").stream().map((t) -> {
                    return t.getDescription();
                }).collect(Collectors.joining("\n"));
            } catch (IOException ex) {
                Logger.getLogger(RulePreviewSkin.class.getName()).log(Level.SEVERE, null, ex);
                msg = new StringBuilder("Reading of JSON failed! Reason:\n").append(ex.getMessage()).toString();
            }
            indicator.setTooltip(new CpxTooltip(msg, 100, 5000, 200, true));
            shHeader.setTitleGraphic(indicator);
        }else{
            shHeader.setTitleGraphic(null);
        }
        lblNameValue.setText(pTable.getCrgtTableName());
        tfNameValue.setText(pTable.getCrgtTableName());
        lblCategoryValue.setText(pTable.getCrgtCategory()!=null?pTable.getCrgtCategory().getTranslation().getValue():"");
        cbCategoryValue.getSelectionModel().select(pTable.getCrgtCategory());
//        LOG.info("set Text " + pTable.getCrgtTableName() + " in " + (System.currentTimeMillis()-start) + " ms");
        lblCreationUserValue.setText(pTable.getCreationUser() != null ? MenuCache.instance().getUserNameForId(pTable.getCreationUser()) : "System");
//        LOG.info("setup user id " + pTable.getCrgtTableName() + " in " + (System.currentTimeMillis()-start) + " ms");
        lblCreationDateValue.setText(pTable.getCreationDate() != null ? Lang.toDate(pTable.getCreationDate()) : "00.00.0000");
        try {
            RuleTableMessage msg = new RuleTableMessageReader().readSingleResultOrNull(pTable.getCrgtMessage(), "UTF-8");
            strPane.setValidationMessage(msg!=null?msg.getCodes():null);
        } catch (IOException ex) {
            Logger.getLogger(RuleTablesDetailSkin.class.getName()).log(Level.SEVERE, null, ex);
        }
        String content = pTable.getCrgtContent();
        if(!java.util.Objects.requireNonNullElse(content, "").equals(strPane.getContent())){
            strPane.setContent(content);
        }else{
            strPane.refresh();
        }
//        LOG.info("setContent " + pTable.getCrgtTableName() + " in " + (System.currentTimeMillis()-start) + " ms");
        boxLinkedRules.getChildren().clear();
        RuleTableView tableview = new RuleTableView();
        boxLinkedRules.getChildren().add(tableview);
        tableview.reload();
//        LOG.info("update view for Table " + pTable.getCrgtTableName() + " in " + (System.currentTimeMillis()-start) + " ms");
    }
    private ObjectProperty<Predicate<Node>> predicateProperty;

    public ObjectProperty<Predicate<Node>> predicateProperty() {
        if (predicateProperty == null) {
            predicateProperty = new SimpleObjectProperty<>(new Predicate<Node>() {
                @Override
                public boolean test(Node t) {
                    return true;
                }
            });
        }
        return predicateProperty;
    }

    public void setPredicate(Predicate<Node> pPredicate) {
        predicateProperty().set(pPredicate);
    }

    public Predicate<Node> getPredicate() {
        return predicateProperty().get();
    }

    private Parent loadRoot() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/RuleTablesDetail.fxml"));

        shHeader = (SectionHeader) root.lookup("#shHeader");
        btnSave = new SaveButton();
        btnSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                JsonMessageHelper.checkAndShowTransferCatalogError(getSkinnable().getRuleTables(), (t) -> {
                    if (ButtonType.YES.equals(t)) {
                        getSkinnable().saveRuleTable();
                        callUpdateRules();
                        RefreshEvent event2 = new RefreshEvent(RefreshEvent.refreshEvent(), getSkinnable());
                        Event.fireEvent(getSkinnable(), event2);
                        updateData(getSkinnable().getRuleTables());
                    }
                });
            }
        });
        Label lblName = (Label) root.lookup("#lblName");
        lblName.setText("Tabellenname:");
        lblNameValue = (Label) root.lookup("#lblNameValue");
        tfNameValue = (TextField) root.lookup("#tfNameValue");
        tfNameValue.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if(t1){
                    //ignore focus gain
                   return; 
                }
                handleNameUpdate();
            }
        });
        tfNameValue.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if(KeyCode.ENTER.equals(t.getCode())){
                    handleNameUpdate();
                }
            }
        });
        hbNameValue = (HBox) root.lookup("#hbNameValue");
        
        Label lblCategory = (Label) root.lookup("#lblCategory");
        lblCategory.setText("Tabellenkategorie:");
        lblCategoryValue = (Label) root.lookup("#lblCategoryValue");
        cbCategoryValue = (ComboBox) root.lookup("#cbCategoryValue");
        cbCategoryValue.setConverter(new StringConverter<RuleTableCategoryEn>() {
            @Override
            public String toString(RuleTableCategoryEn t) {
                return t!=null?t.getTranslation().getValue():"";
            }

            @Override
            public RuleTableCategoryEn fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        cbCategoryValue.setItems(FXCollections.observableArrayList(RuleTableCategoryEn.values()));
        cbCategoryValue.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<RuleTableCategoryEn>() {
            @Override
            public void changed(ObservableValue<? extends RuleTableCategoryEn> ov, RuleTableCategoryEn t, RuleTableCategoryEn t1) {
                handleCategoryUpdate();
            }
        });
        hbCategoryValue = (HBox) root.lookup("#hbCategoryValue");
        
        Label lblCreationDate = (Label) root.lookup("#lblCreationDate");
        lblCreationDate.setText("Erstellt am:");
        lblCreationDateValue = (Label) root.lookup("#lblCreationDateValue");
        Label lblCreationUser = (Label) root.lookup("#lblCreationUser");
        lblCreationUser.setText("Erstellt von:");
        lblCreationUserValue = (Label) root.lookup("#lblCreationUserValue");

        SplitPane spContent = (SplitPane) root.lookup("#spContent");
        spContent.setSkin(new SplitPaneSkin(spContent));

        SectionHeader shTableContent = (SectionHeader) root.lookup("#shTableContent");
        TextField searchField = new TextField();
        searchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                setPredicate(new Predicate<Node>() {
                    @Override
                    public boolean test(Node t) {
                        if (!(t instanceof Item)) {
                            return false;
                        }
                        return ((Item) t).getText().toLowerCase().contains(newValue.toLowerCase());
                    }
                });
            }
        });
        searchField.disabledProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if (t1) {
                    searchField.setText("");
                }
            }
        });
        strPane = new StringContentView(getSkinnable().isEditable());
        strPane.predicateProperty().bind(predicateProperty());
        strPane.contentProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                CrgRuleTables table = getSkinnable().getRuleTables();
                if(t1.equals(table.getCrgtContent())){
                    return;
                }
                if(getSkinnable().getValidationCalllback()!= null){
//                    byte[] oldMessage = table.getCrgtMessage();
                    table.setCrgtContent(t1);
                    byte[] newMessage = getSkinnable().getValidationCalllback().call(table);
                    table.setCrgtMessage(newMessage);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            updateData(table);
                            strPane.refresh();
                        }
                    });
//                    updateRules(oldMessage,newMessage);
                }
                RefreshEvent event2 = new RefreshEvent(RefreshEvent.refreshEvent(), getSkinnable());
                Event.fireEvent(getSkinnable(), event2);
            }

        });
//        strPane.setValidationMessage("*.*,1-502.0");
        strPane.contentModeProperty().addListener(new ChangeListener<StringContentView.ContentMode>() {
            @Override
            public void changed(ObservableValue<? extends StringContentView.ContentMode> ov, StringContentView.ContentMode t, StringContentView.ContentMode t1) {
                if (t1 == null) {
                    searchField.setDisable(true);
                    return;
                }
                searchField.setDisable(StringContentView.ContentMode.TEXT.equals(t1) ? true : false);
            }
        });
        strPane.codeSuggestionCallbackProperty().bind(getSkinnable().codeSuggestionCallbackProperty());
//        Button btnSwitchMode = new Button();
//        btnSwitchMode.setTooltip(new Tooltip("Umschalten zwischen Textbearbeitung und Kachelansicht"));
//        btnSwitchMode.getStyleClass().add("cpx-icon-button");
//        btnSwitchMode.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.TOGGLE_OFF));
//        btnSwitchMode.setText("Text-Modus");
//        btnSwitchMode.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
//            @Override
//            public void handle(ActionEvent t) {
//                StringContentView.ContentMode newMode = StringContentView.ContentMode.TEXT.equals(strPane.getContentMode()) ? StringContentView.ContentMode.ITEM : StringContentView.ContentMode.TEXT;
//                btnSwitchMode.setGraphic(StringContentView.ContentMode.TEXT.equals(newMode) ? ResourceLoader.getGlyph(FontAwesome.Glyph.TOGGLE_ON) : ResourceLoader.getGlyph(FontAwesome.Glyph.TOGGLE_OFF));
//                strPane.setContentMode(newMode);
//            }
//        });
        TextModeToggleButton btnSwitchMode = new TextModeToggleButton();
        btnSwitchMode.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                StringContentView.ContentMode newMode = StringContentView.ContentMode.TEXT.equals(strPane.getContentMode()) ? StringContentView.ContentMode.ITEM : StringContentView.ContentMode.TEXT;
                strPane.setContentMode(newMode);
            }
        });
        
        btnEditComment = new EditCommentButton(PopOver.ArrowLocation.BOTTOM_RIGHT);
        btnEditComment.commentProperty().bind(getSkinnable().commentProperty());
        btnEditComment.editableProperty().bind(getSkinnable().editableProperty());
        btnEditComment.setOnUpdateComment(getSkinnable().getOnSaveComment());


        
        Label icon = new Label();
        icon.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.SEARCH));
        icon.getStyleClass().add("icon");
       


        HBox searchMenu = new HBox(5, icon, searchField, btnSwitchMode, btnEditComment/*,btnAllDelete*/);
        searchMenu.setAlignment(Pos.CENTER);
        shTableContent.addMenuItems(searchMenu);
        boxTableContent = (VBox) root.lookup("#boxTableContent");
        ScrollPane scrollPane = new ScrollPane(strPane);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        boxTableContent.getChildren().clear();
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        boxTableContent.getChildren().add(scrollPane);

        boxLinkedRules = (VBox) root.lookup("#boxLinkedRules");

        return root;
    }
    private void updateRules(byte[] oldMessage, byte[] newMessage) {
        if (oldMessage == null && newMessage != null) {
            callUpdateRules();
        }
        if (oldMessage != null && newMessage == null) {
            callUpdateRules();
        }
    }
    private void callUpdateRules() {
        RuleTableView tv = lookUpRuleTableView();
        if (!hasRulesToUpdate(tv)) {
            return; // has no rules to update - do nothing
        }
        UpdateRulesEvent event = new UpdateRulesEvent(UpdateRulesEvent.updateRulesEvent(), getSkinnable().getPool(), tv.getItems(), true);
        Event.fireEvent(getSkinnable(), event);
    }
    
    private RuleTableView lookUpRuleTableView(){
        if(boxLinkedRules == null || boxLinkedRules.getChildren().isEmpty()){
            return null; //if not initialized yet or no items are stored - do nothing
        }
        for(Node child : boxLinkedRules.getChildren()){
            if(child instanceof RuleTableView){
                return (RuleTableView) child;
            }
        }
        return null;
    }
    private boolean hasRulesToUpdate(RuleTableView pTableView){
        if(pTableView == null){
            return false;
        }
        return !pTableView.getItems().isEmpty();
    }
    private void handleNameUpdate() {
        String name = getSkinnable().getRuleTables()!=null?getSkinnable().getRuleTables().getCrgtTableName():"";
        if(name.equals(tfNameValue.getText())){
            //ignore names are equal!
            return;
        }
        getSkinnable().getRuleTables().setCrgtTableName(tfNameValue.getText());
        RefreshEvent event = new RefreshEvent(RefreshEvent.refreshEvent(), getSkinnable());
        Event.fireEvent(getSkinnable(), event);
    }
    
    private void handleCategoryUpdate() {
        RuleTableCategoryEn category = getSkinnable().getRuleTables()!=null?getSkinnable().getRuleTables().getCrgtCategory():null;
        if(Objects.equal(category, cbCategoryValue.getSelectionModel().getSelectedItem())){
            //ignore names are equal!
            return;
        }
        getSkinnable().getRuleTables().setCrgtCategory(cbCategoryValue.getSelectionModel().getSelectedItem());
        RefreshEvent event = new RefreshEvent(RefreshEvent.refreshEvent(), getSkinnable());
        Event.fireEvent(getSkinnable(), event);
    }
    
    private void clearData() {
        lblNameValue.setText("");
        lblCreationUserValue.setText("");
        lblCreationDateValue.setText("");
        lblCategoryValue.setText("");
    }
    private static final Logger LOG = Logger.getLogger(RuleTablesDetailSkin.class.getName());

    /**
     * RuleTableView implementation TODO: ReUse RuleTableview from ruleListTable
     */
    private class RuleTableView extends FilterBaseTableView<CrgRules> {

        public RuleTableView() {
            super();
            setShowMenu(true);
            setTitle("Verknüpfte Regeln");
            getStyleClass().add("stay-selected-table-view");
            setColumnResizePolicy(UNCONSTRAINED_RESIZE_POLICY);

            setOnRowClick(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    if (t.getClickCount() >= 2 && getSelectionModel().getSelectedItem() != null) {
                        CrgRules rule = getSelectionModel().getSelectedItem();
                        if (rule.getCrgrDefinition() == null) {
                            rule.setCrgrDefinition(getSkinnable().fetchRuleContent(rule));
                        }
                        OpenRuleEvent event = new OpenRuleEvent(OpenRuleEvent.openRuleEvent(),
                                getSkinnable().getPool(),
                                rule);
                        Event.fireEvent(RuleTableView.this, event);
                    }
                }
            });
        }

        @Override
        protected void updateColumns() {
            RuleNumberColumn number = new RuleNumberColumn();
            RuleCaptionColumn caption = new RuleCaptionColumn();
            RuleCategoryColumn category = new RuleCategoryColumn();
            RuleIdentColumn ident = new RuleIdentColumn();
//            RuleYearColumn year = new RuleYearColumn();
            RuleSuggestionColumn suggestion = new RuleSuggestionColumn();
            RuleStatusColumn status = new RuleStatusColumn();

            getColumns().addAll(number/*,year*/, ident, caption, category, suggestion, status);

            NumberBinding numberBinding = Bindings.add(number.widthProperty(), 0)
                    .add(category.widthProperty())
                    .add(ident.widthProperty())
                    //                    .add(year.widthProperty())
                    .add(status.widthProperty())
                    //add for scrollbar
                    .add(10);

            caption.prefWidthProperty().bind(widthProperty().subtract(numberBinding).divide(2));
            suggestion.prefWidthProperty().bind(widthProperty().subtract(numberBinding).divide(2));
            caption.setResizable(false);
            suggestion.setResizable(false);

        }

        @Override
        public List<CrgRules> loadItems(int pStartIndex, int pEndIndex) {
            long start = System.currentTimeMillis();
            List<CrgRules> rules = getSkinnable().getRules();//new ArrayList<>();//RuleListFXMLController.this.getScene().loadRules(pStartIndex,pEndIndex);
            LOG.log(Level.INFO, "Load list of rules ({0}) in {1} ms", new Object[]{rules != null ? rules.size() : "null", System.currentTimeMillis() - start});
            return rules;
        }

        private class RuleNumberColumn extends StringColumn<CrgRules> {

            public RuleNumberColumn() {
                super("Nummer");
                setMinWidth(80.0);
                setMaxWidth(80.0);
                setResizable(false);
            }

            @Override
            public String extractValue(CrgRules pValue) {
                return pValue.getCrgrNumber();
            }
        }

//        private class RuleYearColumn extends StringColumn<CrgRules> {
//
//            public RuleYearColumn() {
//                super("Jahr");
//                setMinWidth(80.0);
//                setMaxWidth(80.0);
//                setResizable(false);
//            }
//
//            @Override
//            public String extractValue(CrgRules pValue) {
//                Integer year = pValue.getCrgrValidFromYear();
//                return year == null ? "----" : year + "";
//                //return String.valueOf(pValue.getCrgrValidFrom()!=null?pValue.getCrgrValidFrom().getYear():"----");
//            }
//        }
        private class RuleIdentColumn extends StringColumn<CrgRules> {

            public RuleIdentColumn() {
                super("Identnr");
                setMinWidth(150.0);
                setMaxWidth(150.0);
                setResizable(false);
            }

            @Override
            public String extractValue(CrgRules pValue) {
                return pValue.getCrgrIdentifier();
            }
        }

        private class RuleCaptionColumn extends StringColumn<CrgRules> {

            public RuleCaptionColumn() {
                super("Bezeichnung", true);
            }

            @Override
            public String extractValue(CrgRules pValue) {
                return pValue.getCrgrCaption();
            }
        }

        private class RuleCategoryColumn extends StringColumn<CrgRules> {

            public RuleCategoryColumn() {
                super("Kategorie");
                setMinWidth(100.0);
                setMaxWidth(100.0);
                setResizable(false);
            }

            @Override
            public String extractValue(CrgRules pValue) {
                return pValue.getCrgrCategory();
            }
        }

        private class RuleSuggestionColumn extends StringColumn<CrgRules> {

            public RuleSuggestionColumn() {
                super("Vorschlag", true);
            }

            @Override
            public String extractValue(CrgRules pValue) {
                return pValue.getCrgrSuggText();
            }
        }

        private class RuleStatusColumn extends StringColumn<CrgRules> {

            public RuleStatusColumn() {
                super("Status");
                setMinWidth(80.0);
                setMaxWidth(80.0);
                setResizable(false);
            }

            @Override
            public String extractValue(CrgRules pValue) {
                return pValue.getCrgrRuleErrorType() != null ? pValue.getCrgrRuleErrorType().getTranslation().getValue() : "";
            }
        }
    }
}
