/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.cm.fx.details;

import de.lb.cpx.client.app.wm.fx.dialog.editor.ReadOnlyMdkRequestEditor;
import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.model.fx.button.AddButton;
import de.lb.cpx.client.core.model.fx.button.ChainButton;
import de.lb.cpx.client.core.model.fx.button.LinkButton;
import de.lb.cpx.client.core.model.fx.dialog.TitledDialog;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextArea;
import de.lb.cpx.client.core.model.fx.section.SectionHeader;
import de.lb.cpx.client.core.model.fx.tooltip.RiskTooltip;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.model.enums.VersionRiskTypeEn;
import de.lb.cpx.server.commonDB.model.enums.RiskAreaEn;
import de.lb.cpx.shared.dto.ReadOnlyRequestDTO;
import de.lb.cpx.shared.dto.rules.CpxSimpleRuleDTO;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmRequest;
import de.lb.cpx.wm.model.TWmRequestAudit;
import de.lb.cpx.wm.model.TWmRequestMdk;
import de.lb.cpx.wm.model.TWmRisk;
import de.lb.cpx.wm.model.TWmRiskDetails;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

/**
 *
 * @author gerschmann
 */
public class CmRiskDocumentationViewSkin extends SkinBase<CmRiskDocumentationView> {

    private String title = "Risikobewertung";
    private ListView<TWmRiskDetails> lstSetedRisks;
    private ComboBox<RiskAreaEn> riskAreas;
    private RiskSummary fullRiskSummary;
    private LabeledTextArea txtFullRiskComment;
    private ListView<TWmRiskDetails> lstRulesRisks;
    private RiskTooltip ttRiskValue;
    private RiskTooltip ttRiskPercent;
    private CheckBox chkActual4Reg;
    private SectionHeader shTitle;
    private Label lblRulesRisks;
    private VBox boxReadOnlyContent;
    private VBox boxReadWriteContent;
    private HBox boxOptionalButtons;
    private HBox boxVersionCompare;

    public CmRiskDocumentationViewSkin(CmRiskDocumentationView c) throws IOException {
        super(c);
        getChildren().add(createRoot());
    }

    @SuppressWarnings("unchecked")
    private Parent createRoot() throws IOException {

        getSkinnable().createEditableRiskAreas();
        AnchorPane layout = FXMLLoader.load(getClass().getResource("/fxml/CmRiskDocumentationView.fxml"));

        chkActual4Reg = (CheckBox) layout.lookup("#chkActual4Reg");
        shTitle = (SectionHeader) layout.lookup("#shTitle");
        Bindings.bindContent(shTitle.menuItems(), getSkinnable().getMenuItems());
        title = shTitle.getTitle().isEmpty() ? title : shTitle.getTitle();
        Label lblDescrText1 = (Label) layout.lookup("#lblDescript1Id");
        lblDescrText1.setText(Lang.getDocumentationRiskDescriptionText1());
        Label lblDescrText2 = (Label) layout.lookup("#lblDescript2Id");
        lblDescrText2.setText(Lang.getDocumentationRiskDescriptionText2());
        VBox vbMain = (VBox) layout.lookup("#vBoxMainId");
        VBox vbFullRisk = (VBox) layout.lookup("#vBoxFullRiskId");
        vbFullRisk.prefWidthProperty().bind(vbMain.widthProperty().divide(4));

        Label fullRiskTitle = (Label) layout.lookup("#lblFullRiskTitleId");
        fullRiskTitle.setText(Lang.getDocumentationRiskFullRisk());
        fullRiskSummary = (RiskSummary) layout.lookup("#fullRiskSummaryId");
        fullRiskSummary.enableFields2VersionType(getSkinnable().getVersionType());

        fullRiskSummary.changeCalculatedValuesProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if (t1) {
                    fullRiskSummary.changeCalculatedValues(false);
                    getSkinnable().getRisk().setRiskAuditPercent(fullRiskSummary.getAuditPercentValue());
                    getSkinnable().getRisk().setRiskWastePercent(fullRiskSummary.getWastePercentValue());
                    getSkinnable().getRisk().setRiskNotCalculatedFee(BigDecimal.valueOf(fullRiskSummary.getNotCalculatedFee()));

                    fullRiskSummary.setWasteValue(getSkinnable().calculateWaste4Risk());
                    fullRiskSummary.setCalcWasteValues(getSkinnable().getRisk().getRiskCalcPercentTotal(), getSkinnable().getRisk().getRiskPercentTotal());

                }
            }
        });
        getSkinnable().rule4riskProperty().addListener(new ChangeListener<CpxSimpleRuleDTO>() {
            @Override
            public void changed(ObservableValue<? extends CpxSimpleRuleDTO> ov, CpxSimpleRuleDTO t, CpxSimpleRuleDTO t1) {
                addTooltips4RiskFields(getSkinnable().getRule4Risk());
            }
        });
        boxOptionalButtons = (HBox) layout.lookup("#boxOptionalButtons");
        boxOptionalButtons.getChildren().addAll(getOptinalButtons(getSkinnable().getVersionType()));
        
        txtFullRiskComment = (LabeledTextArea) layout.lookup("#fullRiskCommentId");
        txtFullRiskComment.setTitle(Lang.getComment());
        txtFullRiskComment.setMaxSize(LabeledTextArea.RISK_COMMENT_SIZE);
        txtFullRiskComment.setText(getSkinnable().getRisk() == null || getSkinnable().getRisk().getRiskComment() == null ? "" : getSkinnable().getRisk().getRiskComment());
        fullRiskSummary.getControl().prefWidthProperty().bind(txtFullRiskComment.widthProperty());
        fillFullRiskSummary();
        txtFullRiskComment.getControl().focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    //focus lost -> store value
                    getSkinnable().getOnUpdateFullRiskComment().call(txtFullRiskComment.getText());
                }
            }
        });
        getSkinnable().riskProperty().addListener(new ChangeListener<TWmRisk>() {
            @Override
            public void changed(ObservableValue<? extends TWmRisk> ov, TWmRisk oldVal, TWmRisk newVal) {
                if (newVal != null) {
                    updateRiskValues(newVal);
                }
            }
        });
        getSkinnable().titleDescriptionProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                shTitle.setTitle(createTitleWithDesc(t1));
            }
        });
        shTitle.setTitle(createTitleWithDesc(getSkinnable().getTitleDescription()));

        boxReadOnlyContent = (VBox) layout.lookup("#boxReadOnlyContent");
        boxReadWriteContent = (VBox) layout.lookup("#boxReadWriteContent");
        lstRulesRisks = (ListView<TWmRiskDetails>) layout.lookup("#lstFromRulesId");
        lblRulesRisks = (Label) layout.lookup("#lblRulesRisks");

        lblRulesRisks.setText(getRuleRiskTitle(getSkinnable().getVersionType()));
        lstRulesRisks.setCellFactory(new Callback<ListView<TWmRiskDetails>, ListCell<TWmRiskDetails>>() {
            @Override
            public ListCell<TWmRiskDetails> call(ListView<TWmRiskDetails> p) {
                return new RulesRiskCell(getCheckMode(false,getSkinnable().getVersionType())){
                    @Override
                    protected void updateItem(TWmRiskDetails t, boolean bln) {
                        super.updateItem(t, bln); //To change body of generated methods, choose Tools | Templates.
                        if (t == null || bln) {
                            setGraphic(null);
                            return;
                        }
                        boolean isProcessed = getSkinnable().isProcessed(t);
                        Glyph glyph = ResourceLoader.getGlyph(isProcessed ? FontAwesome.Glyph.CHECK : FontAwesome.Glyph.PLUS);
                        getIconButton().setGlyph(glyph);
                        EventHandler<ActionEvent> handle = new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                if (lstRulesRisks.getSelectionModel().getSelectedItem() != null) {
                                    addSuggestedRisk2Seted(lstRulesRisks.getSelectionModel().getSelectedItem());
                                    lstRulesRisks.refresh();
                                }

                            }
                        };
                        getIconButton().setOnAction(!isProcessed?handle:null);
                    }

                };
            }
        });
        lstRulesRisks.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        lstRulesRisks.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TWmRiskDetails>() {
            @Override
            public void changed(ObservableValue<? extends TWmRiskDetails> ov, TWmRiskDetails oldVal, TWmRiskDetails newVal) {

                Node view = createRiskDetailView(newVal, true);
                setReadOnlyContent(view);
                if(newVal!=null){
//                    view.setDisable(true);
                    lstSetedRisks.getSelectionModel().clearSelection();
                }
            }
        }
        );
        Bindings.bindContent(lstRulesRisks.getItems(), getSkinnable().getSuggestedItems());
// risks that ware set
        Label lblRiskArea = (Label) layout.lookup(("#lblRiskAreasId"));
        lblRiskArea.setText(Lang.getDocumentationRiskArea());
        riskAreas = (ComboBox) layout.lookup("#riskAreasId");
        riskAreas.getStyleClass().add("risk-combo-box");
        riskAreas.getItems().addAll(getSkinnable().getRiskAreas());
        Bindings.bindContent(riskAreas.getItems(), getSkinnable().getRiskAreas());

        riskAreas.setConverter(new StringConverter<RiskAreaEn>() {
            @Override
            public String toString(RiskAreaEn t) {
                if (t == null) {
                    return "";
                }
                return t.getTranslation().getValue();
            }

            @Override
            public RiskAreaEn fromString(String string) {
                return RiskAreaEn.valueOf(string);
            }
        });
        Button btnAddRisk = (AddButton) layout.lookup("#btnAddRiskId");

        btnAddRisk.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                checkAndAddAreaValue();
                riskAreas.getSelectionModel().clearSelection();
            }
        });

        Label lblRisksSet = (Label) layout.lookup("#lblRiskSetId");
        lblRisksSet.setText("Eigene Einschätzung");
        lstSetedRisks = (ListView<TWmRiskDetails>) layout.lookup("#lvRisks");
        lstSetedRisks.getStyleClass().add("stay-selected-list-view");

        lstSetedRisks.setCellFactory(new Callback<ListView<TWmRiskDetails>, ListCell<TWmRiskDetails>>() {
            @Override
            public ListCell<TWmRiskDetails> call(ListView<TWmRiskDetails> param) {
                SettedRiskCell cell = new SettedRiskCell(getCheckMode(true, getSkinnable().getVersionType()));
                cell.setOnDelete(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        TWmRiskDetails toRemove = lstSetedRisks.getSelectionModel().getSelectedItem();
                        if (toRemove == null) {
                            return;
                        }
                        getSkinnable().getProcessedItems().remove(toRemove);
                        lstSetedRisks.refresh();
                        if (getSkinnable().getRiskItem4SuggestedRisk(toRemove.getRiskArea()) != null) {
                            riskAreas.getSelectionModel().clearSelection();
                            lstRulesRisks.refresh();
                        }

                    }
                });
                return cell;
            }
        });
        lstSetedRisks.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        lstSetedRisks.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TWmRiskDetails>() {
            @Override
            public void changed(ObservableValue<? extends TWmRiskDetails> ov, TWmRiskDetails oldVal, TWmRiskDetails newVal) {
                setReadWriteContent(createRiskDetailView(newVal, false));
                if(newVal!=null){
                    lstRulesRisks.getSelectionModel().clearSelection();
                }
            }
        }
        );
        setReadWriteContent(createRiskDetailView(null, true));
        setReadOnlyContent(createRiskDetailView(null, true));
        Bindings.bindContent(lstSetedRisks.getItems(), getSkinnable().getProcessedItems());
        chkActual4Reg.setSelected(getSkinnable().getRisk().getRiskActual4Req());
        chkActual4Reg.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
//                getSkinnable().getRisk().setRiskActual4Req(t1);
                if (getSkinnable().getOnUpdateActualRisk() == null) {
                    return;
                }
                if (getSkinnable().getRisk().getRiskActual4Req() == t1) {
                    return;
                }
//                getSkinnable().getRisk().setRiskActual4Req(t1);
                getSkinnable().getOnUpdateActualRisk().call(t1);
                }
        });
        
        boxVersionCompare = (HBox) layout.lookup("#boxVersionCompare");
        getSkinnable().versionTypePropperty().addListener(new ChangeListener<VersionRiskTypeEn>() {
            @Override
            public void changed(ObservableValue<? extends VersionRiskTypeEn> ov, VersionRiskTypeEn t, VersionRiskTypeEn t1) {
                handleVersionTypeChange(t1);
            }
        });
        handleVersionTypeChange(getSkinnable().getVersionType());
//        checkShowRiskValues();
        return layout;
    }
    
    private void handleVersionTypeChange(VersionRiskTypeEn t1) {
        if(VersionRiskTypeEn.AUDIT_MD.equals(t1) || /*VersionRiskTypeEn.AUDIT_MD.equals(t1) ||*/VersionRiskTypeEn.CASE_FINALISATION.equals(t1)){ //What was meant as second Type? AduitCaseDialog??
                                                                                                                                                 //Disabled second Type for now because it makes no sense   
            if(boxVersionCompare.getChildren().isEmpty()){
                ChainButton btn = new ChainButton();
                btn.setTooltip(new Tooltip("Vergleich der " + getSkinnable().getPredecessorDescription(t1) + " mit " + getSkinnable().getTitleDescription() + " in der DRG-Tabelle."));
                btn.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent t) {
                        getSkinnable().getVersionCompareCallback().call(t1);
                    }
                });
                boxVersionCompare.getChildren().add(btn);
            }
        }else{
            boxVersionCompare.getChildren().clear();
        }
        
    }
    private Node createRiskDetailView(TWmRiskDetails pDetail, boolean isReadonly){
        if(pDetail == null){
            return createNoSelectionPane();
        }
        //alternative, store 2 instances of this view and update via setRiskDetail, should be more resourceful!
        CmRiskDetailDocumentationView view = new CmRiskDetailDocumentationView(isReadonly);
        view.setRiskDetail(pDetail);
        view.setUpdateRiskDetailCalback(new Callback<TWmRiskDetails, Boolean>() {
            @Override
            public Boolean call(TWmRiskDetails param) {
                if(getSkinnable().getOnUpdateRisk() == null){
                    return false;
                }
                if(isReadonly){
                    lstRulesRisks.refresh();
                }else{
                    lstSetedRisks.refresh();
                }
                return getSkinnable().getOnUpdateRisk().call(param);
            }
        });
        return view;
    }
    private CmRiskDocumentationListCell.CheckMode getCheckMode(boolean pIsSettedList, VersionRiskTypeEn pVersionType) {
        pVersionType = Objects.requireNonNullElse(pVersionType, VersionRiskTypeEn.NOT_SET);
        switch(pVersionType){
            case AUDIT_MD:
                if(pIsSettedList){
                    return CmRiskDocumentationListCell.CheckMode.AUDIT_ONLY;
                }
                return CmRiskDocumentationListCell.CheckMode.NONE;
            case CASE_FINALISATION:
                if(pIsSettedList){
                    return CmRiskDocumentationListCell.CheckMode.ALL;
                }
                return CmRiskDocumentationListCell.CheckMode.AUDIT_ONLY;
            default:
                return CmRiskDocumentationListCell.CheckMode.NONE;
        }
    }
    private void setReadOnlyContent(Node pNode){
        Objects.requireNonNull(boxReadOnlyContent, "ReadOnlyContent can not be null!");
        Objects.requireNonNull(pNode, "Node in ReadOnlyContent can not be null!");
        boxReadOnlyContent.getChildren().clear();
        boxReadOnlyContent.getChildren().add(pNode);
        if(pNode instanceof CmRiskDetailDocumentationView){
            ((CmRiskDetailDocumentationView)pNode).setReadonlyContent(true);
         }
    }
    private void setReadWriteContent(Node pNode){
        Objects.requireNonNull(boxReadWriteContent, "ReadWriteContent can not be null!");
        Objects.requireNonNull(pNode, "Node in ReadWriteContent can not be null!");
        boxReadWriteContent.getChildren().clear();
        boxReadWriteContent.getChildren().add(pNode);
        if(pNode instanceof CmRiskDetailDocumentationView){
            ((CmRiskDetailDocumentationView)pNode).setVersionType(getSkinnable().getVersionType());
            ((CmRiskDetailDocumentationView)pNode).setReadonlyContent(false);
        }
    }
    private String createTitleWithDesc(String pDescription) {
        return new StringBuilder(title).append(" ").append(pDescription).toString();
    }

    private void addSuggestedRisk2Seted(TWmRiskDetails pValue) {

        TWmRiskDetails clone = getSkinnable().cloneRiskValue(pValue);
        getSkinnable().getProcessedItems().add(clone);
//        getSkinnable().getRiskAreas().add(pValue.getRiskArea());
        lstSetedRisks.refresh();
        getSkinnable().getOnUpdateRisk().call(clone);
        lstSetedRisks.scrollTo(clone);
        lstSetedRisks.getSelectionModel().select(clone);
//        checkEnabledRiskFields(true);
    }

    private void checkAndAddAreaValue() {
        if (riskAreas.getValue() != null
                && !riskAreas.getValue().toString().isEmpty()) {

            TWmRiskDetails newRisk = getSkinnable().getRiskItem4ProcessedRisk(riskAreas.getValue());
            if (newRisk == null) {
                newRisk = new TWmRiskDetails();
                newRisk.setRiskArea(riskAreas.getValue());
                newRisk.setRiskAuditPercent(0);
                newRisk.setRiskWastePercent(0);
                newRisk.setRiskBaseFee(getSkinnable().getDeltaFee());
                newRisk.setRiskNotCalculatedFee(newRisk.getRiskBaseFee());
                switch(getSkinnable().getVersionType()){
                    case CASE_FINALISATION:
                        newRisk.setRiskWastePercent(100);
                        newRisk.setRiskWastePercentSugg(100);
                        break;
                    case AUDIT_MD:
                    case AUDIT_CASE_DIALOG:
                        newRisk.setRiskAuditPercent(100);
                        newRisk.setRiskAuditPercentSugg(100);
                        break;
                    default:
                        LOG.log(Level.FINER, "can not set AreaValues, unknown versionType found! VersionType is: {0}", getSkinnable().getVersionType().name());
                    
                }
                newRisk.setRiskCalcPercent(newRisk.getRiskAuditPercent() * newRisk.getRiskWastePercent()/100);
                newRisk.setRiskPercent(newRisk.getRiskCalcPercent());
                newRisk.setRiskValue(BigDecimal.valueOf(Lang.round(newRisk.getRiskBaseFee().doubleValue() /100 * newRisk.getRiskPercent(), 2)));
                newRisk.setRiskCalcValue(newRisk.getRiskValue());
                getSkinnable().getProcessedItems().add(newRisk);
//                listView.getItems().add(newRisk);
                lstSetedRisks.refresh();
//                getSkinnable().getOnUpdateRisk().call(newRisk);
            }
            lstSetedRisks.scrollTo(newRisk);
            lstSetedRisks.getSelectionModel().select(newRisk);
//            checkEnabledRiskFields(true);
        } else {
            // do nothing
        }
    }
    private static final Logger LOG = Logger.getLogger(CmRiskDocumentationViewSkin.class.getName());
    
    private BorderPane createNoSelectionPane() {
        Label noListContent = new Label(Lang.getDocumentationNoRiskContent());
        noListContent.setStyle("-fx-font-size:14px;");
        BorderPane noSelectionPane;
        BorderPane.setAlignment(noListContent, Pos.CENTER);
        noSelectionPane = new BorderPane();
        VBox.setVgrow(noSelectionPane, Priority.ALWAYS);
        noSelectionPane.setMaxWidth(Double.MAX_VALUE);
        noSelectionPane.setCenter(noListContent);
        return noSelectionPane;
    }

    public void updateRiskValues(TWmRisk newVal) {
        chkActual4Reg.setSelected(newVal == null ? false : newVal.getRiskActual4Req());
    }

    public void addTooltips4RiskFields(CpxSimpleRuleDTO rule) {
//        addTooltips2DefaultFields(ttFullRiskValue,ttFullRiskPercent, rule);
    }

    public void addToolTips2RiskDetailsFields(CpxSimpleRuleDTO rule) {
        addTooltips2DefaultFields(ttRiskValue, ttRiskPercent, rule);
    }

    private void addTooltips2DefaultFields(RiskTooltip ttValue, RiskTooltip ttPercent, CpxSimpleRuleDTO rule) {

//            ttValue.setValues(rule,  getSkinnable().getRisk().getRiskPlaceOfReg().equals(PlaceOfRegEn.BEFORE_BILLING));
//            ttPercent.setValues(rule ,  getSkinnable().getRisk().getRiskPlaceOfReg().equals(PlaceOfRegEn.BEFORE_BILLING));
    }

    private void fillFullRiskSummary() {
        if (getSkinnable().getRisk() == null) {
            fullRiskSummary.setDFee(0);
            fullRiskSummary.setNotCalculatedFee(0);
            fullRiskSummary.setValues(0, 0, 0, 0, 0.0, 0.0, 0, 0);
        } else {
            fullRiskSummary.setDFee(getSkinnable().getRisk().getRiskBaseFee() == null ? 0 : getSkinnable().getRisk().getRiskBaseFee().doubleValue());
            fullRiskSummary.setNotCalculatedFee(getSkinnable().getRisk().getRiskNotCalculatedFee()== null ? 0 : getSkinnable().getRisk().getRiskNotCalculatedFee().doubleValue());
            fullRiskSummary.setValues(
                    getSkinnable().getRisk().getRiskAuditPercentSugg(),
                    getSkinnable().getRisk().getRiskAuditPercent(),
                    getSkinnable().getRisk().getRiskWastePercentSugg(),
                    getSkinnable().getRisk().getRiskWastePercent(),
                    getSkinnable().getRisk().getRiskCalcValueTotal() == null ? 0.0 : getSkinnable().getRisk().getRiskCalcValueTotal().doubleValue(),
                    getSkinnable().getRisk().getRiskValueTotal() == null ? 0.0 : getSkinnable().getRisk().getRiskValueTotal().doubleValue(),
                    getSkinnable().getRisk().getRiskCalcPercentTotal(),
                    getSkinnable().getRisk().getRiskPercentTotal()
            );
        }
        fullRiskSummary.changeCalculatedValues(false);
//        fullRiskSummary.changeWasteValue(false);
    }

    private String getRuleRiskTitle(VersionRiskTypeEn versionType) {
        if(versionType == null){
            return Lang.getDocumentationRiskFromRules();
        }
        switch(versionType){
            case BEFORE_BILLING:
            case NOT_SET:
                return Lang.getDocumentationRiskFromRules();
            case AUDIT_MD:
            case AUDIT_CASE_DIALOG:
                return "Aus Abrechnungsversion";
            case CASE_FINALISATION:
                return "Aus Anfrageversion";
//                return "Aus Abrechnungsversion";
            default:
                return Lang.getDocumentationRiskFromRules();
                
        }
    }
//    private String getPredecessorDescription(VersionRiskTypeEn pVersionType){
//        pVersionType = Objects.requireNonNullElse(pVersionType, VersionRiskTypeEn.NOT_SET);
//        switch(pVersionType){
//            case AUDIT_MD:
//            case AUDIT_CASE_DIALOG:
//                return "Abrechnungs-Version";
//            case CASE_FINALISATION:
//                return "Anfrage-Version";
////                return "Aus Abrechnungsversion";
//            default:
//                return null;
//        }
//    }
    public void resetActualCheck() {
        chkActual4Reg.setSelected(false);
    }

    private List<Node> getOptinalButtons(VersionRiskTypeEn pVersionType) {
        List<Node> buttons = new ArrayList<>();
        if(boxOptionalButtons == null){
            return buttons;
        }
        pVersionType = Objects.requireNonNullElse(pVersionType, VersionRiskTypeEn.NOT_SET);
        switch(pVersionType){
            case AUDIT_CASE_DIALOG:
            case AUDIT_MD:
                buttons.add(createRequestLinkButton(pVersionType));
                return buttons;
            default:
                return buttons;
                
        }
    }

    private Node createRequestLinkButton(VersionRiskTypeEn pType) {
        LinkButton btn = new LinkButton();
        btn.setStyle("-fx-padding:0;");
        btn.setTooltip(new Tooltip("Anfrage öffnen (lesend)"));
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                ReadOnlyRequestDTO dto = getSkinnable().getRequestCallback().call(pType);
                if(dto == null){
                    MainApp.showErrorMessageDialog("Es konnte keine Anfrage gefunden werden!");
                    return;
                }
                TWmRequest request = dto.getRequest();
                if(request == null){
                    MainApp.showErrorMessageDialog("Es konnte keine Anfrage gefunden werden!");
                    return;
                }
                if(request instanceof TWmRequestMdk){
                    TitledDialog dialog = new TitledDialog("Anfrage (MD)", BasicMainApp.getWindow(), Modality.WINDOW_MODAL);
                    dialog.getDialogPane().getButtonTypes().setAll(ButtonType.CLOSE);
                    ReadOnlyMdkRequestEditor readOnly = new ReadOnlyMdkRequestEditor();
                    readOnly.setDisable(true);
                    readOnly.setRequest((TWmRequestMdk)request);
                    readOnly.setProcessDate(dto.getProcessDate());
                    dialog.getDialogPane().setContent(readOnly);
                    dialog.showAndWait();
                }else if(request instanceof TWmRequestAudit){
                    //add new requests here!
                    BasicMainApp.showWarningMessageDialog("Keine Ansicht für die Anfrageart: "+ pType.name() + " gefunden!");
                }else{
                    BasicMainApp.showWarningMessageDialog("Keine Ansicht für die Anfrageart: "+ pType.name() + " gefunden!");
                }
            }
        });
        return btn;
    }

}
