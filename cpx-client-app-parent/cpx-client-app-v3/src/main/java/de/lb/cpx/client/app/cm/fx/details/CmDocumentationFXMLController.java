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
 *    2020  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.cm.fx.details;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.cm.fx.documentation.CaseDocumentation;
import de.lb.cpx.client.app.cm.fx.documentation.DocTitledPaneSelectedItem;
import de.lb.cpx.client.app.cm.fx.documentation.DocTitledPaneVoidItem;
import de.lb.cpx.client.core.model.fx.titledpane.AccordionSelectedItem;
import de.lb.cpx.client.app.cm.fx.documentation.DocumentationTitledPane;
import de.lb.cpx.client.app.cm.fx.documentation.RiskDocumentation;
import de.lb.cpx.client.app.cm.fx.simulation.model.VersionManager;
import de.lb.cpx.client.app.model.events.report.CreateReportEvent;
import de.lb.cpx.client.app.wm.util.texttemplate.TextTemplateController;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.fx.alert.ConfirmDialog;
import de.lb.cpx.client.core.model.fx.alert.NotificationsFactory;
import de.lb.cpx.client.core.model.fx.button.AddTableButton;
import de.lb.cpx.client.core.model.fx.button.TableButton;
import de.lb.cpx.client.core.model.fx.masterdetail.AccordionMasterDetailPane;
import de.lb.cpx.client.core.model.fx.section.SectionHeader;
import de.lb.cpx.client.core.model.fx.titledpane.AccordionTitledPane;
import de.lb.cpx.client.core.model.fx.tooltip.CpxTooltip;
import de.lb.cpx.client.core.util.ContextMenuUtil;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.client.core.util.VersionStringConverter;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseComment;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.enums.CommentTypeEn;
import de.lb.cpx.model.enums.PlaceOfRegEn;
import de.lb.cpx.model.enums.TextTemplateTypeEn;
import de.lb.cpx.model.enums.VersionRiskTypeEn;
import de.lb.cpx.shared.dto.ReadOnlyRequestDTO;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmRisk;
import de.lb.cpx.wm.model.TWmRiskDetails;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Level;
import javafx.application.Platform;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.control.skin.TextAreaSkin;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import javafx.util.Callback;
import org.controlsfx.glyphfont.FontAwesome;

/**
 *
 * @author wilde
 */
public class CmDocumentationFXMLController extends Controller<CmDocumentationScene> {

    private static final Double DIN_A4_WIDTH = 585.0;
    private static final String SELECTED_STYLE_CLASS = "accordion-row-selected";
    
    @FXML
    private AccordionMasterDetailPane<CommentTypeEn> mdDocumentation;

    @Override
    public void refresh() {
        for(TitledPane tp : mdDocumentation.getAccordion().getPanes()){
            if(tp instanceof DocumentationTitledPane){
                if(tp instanceof RiskDocumentation){
                    TCaseDetails selected = ((RiskDocumentation)tp).getSelected();
                    ((RiskDocumentation)tp).setValues(CmDocumentationFXMLController.this.getScene().getFacade().getLocalVersionsFromDb());
                    ((RiskDocumentation)tp).select(selected);
                }
                if(tp.isExpanded()){
                    ((DocumentationTitledPane)tp).refresh();
                }
            }
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        //init master detail listview and set items and cellfactory
        mdDocumentation.setTitledPaneFactory(new Callback<CommentTypeEn, TitledPane>() {
            @Override
            public TitledPane call(CommentTypeEn param) {
                switch(param){
                    case caseReview:
                        CaseDocumentation caseDoc = new CaseDocumentation();
                        caseDoc.setValues(CmDocumentationFXMLController.this.getScene().getFacade().findCaseComments(param));
                        caseDoc.setSelectItemCallback(new Callback<AccordionSelectedItem<TCaseComment>, Boolean>() {
                            @Override
                            public Boolean call(AccordionSelectedItem<TCaseComment> param) {
//                                if(param instanceof DocTitledPaneSelectedItem){
//                                    updateSelectedStyleClass(false,caseDoc,mdDocumentation.getAccordion().getPanes());
//                                }
                                updateSelectedStyleClass(!caseDoc.isExpanded(),caseDoc,mdDocumentation.getAccordion().getPanes());
                                TCaseComment comment = Objects.requireNonNullElse(param.getItem(), getScene().getFacade().findActiveComment(CommentTypeEn.caseReview));
//                                updateSelectedStyleClass(true,caseDoc,mdDocumentation.getAccordion().getPanes());
                                setDetail(createCaseEvaluationLayout(comment));
                                return true;
                            }
                        });
                        caseDoc.expandedProperty().addListener(new ChangeListener<Boolean>() {
                            @Override
                            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                                updateSelectedStyleClass(!t1,caseDoc,mdDocumentation.getAccordion().getPanes());
                            }
                        });
                        caseDoc.setStoreEntityCallback(new Callback<TCaseComment, TCaseComment>() {
                            @Override
                            public TCaseComment call(TCaseComment param) {
                                if(param == null){
                                    return param;
                                }
                                return CmDocumentationFXMLController.this.getScene().getFacade().storeComment(param);
                            }
                        });
                        caseDoc.setRemoveEntityCallback(new Callback<TCaseComment, Boolean>() {
                            @Override
                            public Boolean call(TCaseComment param) {
                                if(param == null){
                                    return false;
                                }
                                return CmDocumentationFXMLController.this.getScene().getFacade().removeComment(param);
                            }
                        });
                        return caseDoc;
                    case riskReview:
                        RiskDocumentation riskDoc = new RiskDocumentation();
                        riskDoc.setSelectItemCallback(new Callback<AccordionSelectedItem<TCaseDetails>, Boolean>() {
                            @Override
                            public Boolean call(AccordionSelectedItem<TCaseDetails> param) {
//                                if(param instanceof DocTitledPaneVoidItem){
//                                    if(AccordionTitledPane.SelectionTarget.ACCORDION_ITEM.equals(((DocTitledPaneVoidItem)param).getTarget())){
//                                        updateSelectedStyleClass(true,riskDoc,mdDocumentation.getAccordion().getPanes()); //accordion item is clicked reset selected style class
//                                        clearSelected(mdDocumentation.getAccordion().getPanes());
//                                    }else{
//                                        updateSelectedStyleClass(false,riskDoc,mdDocumentation.getAccordion().getPanes()); //treeItem is clicked remove selected style class
//                                    }
//                                }
//                                if(param instanceof DocTitledPaneSelectedItem){
//                                    updateSelectedStyleClass(false,riskDoc,mdDocumentation.getAccordion().getPanes()); // risk is selected remove style class
//                                }
                                updateSelectedStyleClass(!riskDoc.isExpanded(),riskDoc,mdDocumentation.getAccordion().getPanes());
                                setDetail(createDetail(param));
                                return true;
                            }

                        });
                        riskDoc.expandedProperty().addListener(new ChangeListener<Boolean>() {
                            @Override
                            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                                updateSelectedStyleClass(!t1,riskDoc,mdDocumentation.getAccordion().getPanes());
//                                setDetail(createDetail(riskDoc.getMostRecentItem(AccordionTitledPane.SelectionTarget.ACCORDION_ITEM)));
                            }
                        });
                        riskDoc.setIsActualRiskCallback(new Callback<TCaseDetails, Boolean>() {
                            @Override
                            public Boolean call(TCaseDetails param) {
                                return getScene().getFacade().check4ActualRisk(param);
                            }
                        });
                        riskDoc.setValues(CmDocumentationFXMLController.this.getScene().getFacade().getLocalVersionsFromDb());
                        return riskDoc;

                    default:
                        return new TitledPane("Unknown Documentation: " + param.getTranslation().getValue(), new Label("No Content set!"));
                }
            }
        });
    }
    private void clearSelected(ObservableList<TitledPane> panes) {
        for(TitledPane pane : panes){
            if(pane instanceof RiskDocumentation){
                ((RiskDocumentation)pane).clearSelection();
            }
        }
    }
    private void updateSelectedStyleClass(boolean pSelected, TitledPane pTitledPane, ObservableList<TitledPane> panes) {
        for (TitledPane pane : panes) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    pane.getStyleClass().remove(SELECTED_STYLE_CLASS);
                    if (pSelected && pane.getText().equals(pTitledPane.getText())) { // weak equals check - should work here with some sort of id? For now should work exactly same department should not exist
                        pTitledPane.getStyleClass().add(0, SELECTED_STYLE_CLASS);
                    }
                }
            });
        }
    }
    
    private Parent createDetail(AccordionSelectedItem<TCaseDetails> pItem){
        if(pItem instanceof DocTitledPaneVoidItem){
            DocTitledPaneVoidItem<TCaseDetails> item = (DocTitledPaneVoidItem)pItem;
            item.setOnLinkAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent t) {
                    TCaseDetails suggestion = item.getSuggestion();
                    TWmRisk risk = getScene().getFacade().getRisk4CaseVersion(suggestion);
                    risk.setRiskActual4Req(true);
                    getScene().getFacade().saveRiskEntity(risk);
                    refresh();
                    RiskDocumentation doc = getRiskDocumentation();
                    
                    if(doc != null){
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                doc.select(suggestion);
                            }
                        });
                    }
                    setDetail(createRiskLayout(risk,suggestion));
                }
            });
            return createPlaceholder(item.getGraphic());
        }
        if(pItem instanceof DocTitledPaneSelectedItem){
            return createRiskLayout(((DocTitledPaneSelectedItem<TCaseDetails>)pItem).getItem());
        }
        return createPlaceholder();
    }
    private void setDetail(Parent pDetail) {
        Parent node = Objects.requireNonNullElse(pDetail, createPlaceholder());
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mdDocumentation.setDetail(node);
            }
        });
    }
    private Parent createPlaceholder(){
        return createPlaceholder("Es wurde nichts ausgewählt!\nWählen Sie einen Eintrag aus dem Menü links aus.");
    }
    private Parent createPlaceholder(String pText){
        Label lbl = new Label(pText);
        return createPlaceholder(lbl);
    }
    private Parent createPlaceholder(Node pNode){
        HBox box = new HBox(pNode);
        box.setAlignment(Pos.CENTER);
        box.setFillHeight(false);
        return box;
    }
    @Override
    public void afterInitialisingScene() {
        super.afterInitialisingScene();
        mdDocumentation.getMenuItems().addAll(CommentTypeEn.values());
    }
    private Parent createRiskLayout(TCaseDetails pDetails){
        TWmRisk risk = getScene().getFacade().getRisk4CaseVersion(pDetails);
        return createRiskLayout(risk, pDetails);
    }
    private Parent createRiskLayout(TWmRisk pRisk,TCaseDetails pDetails){
        if(pDetails == null){
            return null;
        }
        if(pRisk == null){
            return null;
        }
//        TWmRisk risk = getScene().getFacade().getRisk4CaseVersion(pDetails);
        CmRiskDocumentationView riskView = new CmRiskDocumentationView();
        riskView.getMenuItems().setAll(createRiskMenuItems(pDetails));
        riskView.setTitleDescription(VersionStringConverter.convertSimpleWithRiskType(pDetails));
        riskView.setVersionType(pDetails.getCsdVersRiskTypeEn() == null?VersionRiskTypeEn.NOT_SET:pDetails.getCsdVersRiskTypeEn());
//        setSelectedVersion(pDetails);
        riskView.setRisk(pRisk);
        riskView.setProcessedItems(new ArrayList<>(pRisk.getRiskDetails()));
        riskView.setDeltaFee(getScene().getFacade().getDeltaFee4Version(pDetails));
        if(riskView.getVersionType().equals(VersionRiskTypeEn.NOT_SET) || riskView.getVersionType().equals(VersionRiskTypeEn.BEFORE_BILLING)){
            riskView.setRules(getScene().getFacade().getRules4Risks(pDetails));
        }else if(riskView.getVersionType().equals(VersionRiskTypeEn.CASE_FINALISATION)){
            List<TWmRiskDetails> details = new ArrayList<>();
            TWmRisk mdRisk = getScene().getFacade().getActualRisk(PlaceOfRegEn.REQUEST, VersionRiskTypeEn.AUDIT_MD);
// check, whether there is any risk, if not, show this message. If we do have any, but it does not have risk details, probably another message could be good to make a decision

            if(mdRisk == null){
                    MainApp.showWarningMessageDialog("\"Es konnte keine Version ermittelt werden, "
                            + "welche für die Anfrage verwendet wurde! \n"
                            + "Bitte wählen Sie eine entsprechende Version "
                            + "unter dem Menüpunkt \"Anfrage: MD\" aus.");
                    return null;
            } else{
                Set<TWmRiskDetails> det = mdRisk.getRiskDetails();//getScene().getFacade().getRisksFromActualAuditVersion(pDetails);
                if(det != null){
                    details = new ArrayList(det);
                }
            }
            riskView.setSuggestedItems(details); 
        
        }else{
            List<TWmRiskDetails> details = new ArrayList<>();
            TWmRisk billRisk = getScene().getFacade().getActualRisk(PlaceOfRegEn.BEFORE_BILLING, VersionRiskTypeEn.BEFORE_BILLING);
            if(billRisk == null){
                     MainApp.showWarningMessageDialog("\"Es konnte keine Version ermittelt werden, "
                            + "welche für die Abrechnung verwendet wurde! \n"
                            + "Bitte wählen Sie eine entsprechende Version "
                            + "unter dem Menüpunkt \"Bei Abrechnung\" aus.");
                     return null;
            }else{
                Set<TWmRiskDetails> det = billRisk.getRiskDetails();//getScene().getFacade().getRisksFromActualAuditVersion(pDetails);
                if(det != null){
                    details = new ArrayList(det);
                }
            }

            riskView.setSuggestedItems(details); 
        }
        riskView.mergeProcessedWithSuggestedItems();

//        riskView.setSuggestedItems(getScene().getFacade().getRiskDetailsFromRules());   
        riskView.getProcessedItems().addListener(new ListChangeListener<TWmRiskDetails>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends TWmRiskDetails> change) {
                if (change.next()) {
                    if (change.wasAdded()) {
                        addRisks(change.getAddedSubList(), true);
                        riskView.checkMaxValue(change.getAddedSubList(), true);
                    }
                    if (change.wasRemoved()) {
                        removeRisks(change.getRemoved());
                        riskView.checkMaxValue(change.getRemoved(), false);
                    }
                }
            }
        });

        
        Callback<TWmRiskDetails, Boolean> onUpdateRisk = new Callback<TWmRiskDetails, Boolean>() {
            @Override
            public Boolean call(TWmRiskDetails riskDetails) {
                if(checkRiskDetails(riskDetails)){
//                if(result != null){
//                    riskView.setRisk(result);
                    return true;
                }
                return false;
            }
        };
        riskView.setOnUpdateRisk(onUpdateRisk);
        Callback<Double, Boolean> onUpdateFullWaste = new Callback<Double, Boolean>() {
            @Override
            public Boolean call(Double waste) {
                TWmRisk risk = getRisk();
                if (risk == null) {
                    return false;
                }
                risk.setRiskValueTotal(new BigDecimal(waste));
                saveRisk(true);
                return true;
            }

        };

        riskView.setOnUpdateFullWaste(onUpdateFullWaste);

        Callback<Integer, Boolean> onUpdateFullRisk = new Callback<Integer, Boolean>() {
            @Override
            public Boolean call(Integer fullRisk) {
                TWmRisk risk = getRisk();
                if (risk == null) {
                    return false;
                }
                risk.setRiskPercentTotal(fullRisk);
                saveRisk(true);
                return true;
            }

        };

        riskView.setOnUpdateFullRisk(onUpdateFullRisk);

        Callback<String, Boolean> onUpdateFullRiskComment = new Callback<String, Boolean>() {
            @Override
            public Boolean call(String comment) {
                TWmRisk risk = getRisk();
                if (risk == null) {
                    return false;
                }
                risk.setRiskComment(comment);
                saveRisk(true);
                return true;
            }

        };
        
        riskView.setOnUpdateFullRiskComment(onUpdateFullRiskComment);
        
        riskView.setOnUpdateActualRisk(new Callback<Boolean, Boolean>() {
            @Override
            public Boolean call(Boolean param) {
                TWmRisk risk = getRisk();
                if (risk == null) {
                    return false;
                }
                if(!param){
                    risk.setRiskActual4Req(param);
                    saveRisk(true);
                    return true;
                    
                }
                // shell i check, whether risk has its riskdetails?
                // check whether there is already actual risk for for this case type
                List<TCaseDetails> otherActualVersion = check4ActualRisk4Case(pDetails);
                if(otherActualVersion == null || otherActualVersion.isEmpty()){
                    risk.setRiskActual4Req(param);
                    saveRisk(true);
                    return true;
                }else{
//                    final BooleanProperty ret = new SimpleBooleanProperty();
                    // Message 
                    String vers = "";
                    int i = 0;
                    for(TCaseDetails det: otherActualVersion){
                        i++;
                        vers += vers.length() == 0?"":", ";
                        vers += det.getCsdVersion();
                    }
                    final String vers1 =(i == 1?" ":"(en) ") + vers;
                    ConfirmDialog dialog = new ConfirmDialog(MainApp.getWindow(),
                            "Für den Risikotyp \"" + pDetails.getCsdVersRiskTypeEn().getTranslation().value + "\" wurde" + (i == 1?" ":"n ") + "die Version"  + vers1 + " schon als \"Zur Risikoauswertung verwendet\" markiert.\n" + 
                            "Wollen Sie die vorhandene Markierung aufheben und mit dem aktuellen Auswahl ersetzen?") ;
                    dialog.showAndWait().ifPresent(new Consumer<ButtonType>() {
                        @Override
                        public void accept(ButtonType t) {
                            if (ButtonType.YES.equals(t)) {
                                risk.setRiskActual4Req(true);
                                saveRisk(false);
                                getScene().getFacade().resetActual4OtherRisks(risk, pDetails.getCsdVersRiskTypeEn());
                                MainApp.showInfoMessageDialog( 
                                        "Die Markierung \"Zur Risikoauswertung verwendet\" wurde " +
                                        "für den Risikotyp \"" + risk.getRiskPlaceOfReg().getTranslation().value + "\" bei Version" 
//                                        + (i == 1?" ":"(en) ")
                                        + vers1 + " aufgehoben");
                            }else{
                                // reset check
                                riskView.resetActualCheck();
                            }
                            refresh();
                        }
                    });
                    return true;
                }
                 
            }
        });
        riskView.setVersionCompareCallback(new Callback<VersionRiskTypeEn, Boolean>() {
            @Override
            public Boolean call(VersionRiskTypeEn param) {
                param = Objects.requireNonNullElse(param, VersionRiskTypeEn.NOT_SET);
                long start = System.currentTimeMillis();
                Long predecessor  = getScene().getFacade().getRiskTypePredecessorVersionId(param);
                LOG.info("find predecessor in " + (System.currentTimeMillis() - start) + " ms");
                if(predecessor == null){
                    NotificationsFactory.instance().createErrorNotification()
                            .text("Für das Ereignis: "+ param.getTranslation().getValue() + " wurde keine passende Version gefunden!")
                            .show();
                    return false;
                }
                VersionManager manager = getScene().getVersionManager();
                TCaseDetails predecessorVersion = manager.getLocalVersion(predecessor);
                TCaseDetails newVersion = manager.getLocalVersion(pDetails.getId());
                long startKis = System.currentTimeMillis();
                TCaseDetails kisDb = getScene().getFacade().getKisBaseVersion(predecessorVersion);
                LOG.info("find kis-version in " + (System.currentTimeMillis() - startKis) + "ms");
                TCaseDetails kisVersion = kisDb!=null?manager.getExternVersion(kisDb.getId()):null;
                if(predecessorVersion == null){
                    NotificationsFactory.instance().createErrorNotification()
                            .text("Für das Ereignis: "+ param.getTranslation().getValue() + " wurde keine passende Version gefunden!")
                            .show();
                    return false;
                }
                if(kisVersion != null){
                    if (!manager.getManagedVersions().get(0).getContent().equals(kisVersion) && !manager.isDisplayed(kisVersion)) {
                        manager.getManagedVersions().get(0).setContent(kisVersion);
                    }
                }else{
                    NotificationsFactory.instance().createErrorNotification()
                            .text("Es konnte keine Eltern-Version für die " + riskView.getPredecessorDescription(param) + " gefunden werden!")
                            .show();
                }
                if (manager.getManagedVersions().size() > 1) {
                    manager.getManagedVersions().get(1).setContent(predecessorVersion);
                } else {
                    manager.createAndAddVersionContent(predecessorVersion);
                }
                
                if (manager.getManagedVersions().size() > 2) {
                    manager.getManagedVersions().get(2).setContent(newVersion);
                    AddTableButton btn = lookupTableAddButton(riskView);
                    if(btn!=null){
                        btn.setRemoveMode(true);
                    }
                } else {
                    manager.createAndAddVersionContent(newVersion);
                    AddTableButton btn = lookupTableAddButton(riskView);
                    if(btn!=null){
                        btn.setRemoveMode(true);
                    }
                }
                LOG.info("set predecessor versions in " + (start-System.currentTimeMillis()) + " ms");
                return true;
            }
        });
        
        riskView.setRequestCallback(new Callback<VersionRiskTypeEn, ReadOnlyRequestDTO>() {
            @Override
            public ReadOnlyRequestDTO call(VersionRiskTypeEn param) {
                return getScene().getFacade().getLatestRequestForRiskType(param);
            }
        });
        return riskView;
    }
    private AddTableButton lookupTableAddButton(CmRiskDocumentationView pView){
        for(Node menuItem : pView.getMenuItems()){
            if("table-add-btn".equals(menuItem.getId())){
                return (AddTableButton) menuItem;
            }
        }
        return null;
    }
    private List<TCaseDetails> check4ActualRisk4Case(TCaseDetails pDetails){
        return getScene().getFacade().check4ActualRisk4Case(pDetails.getCsdVersRiskTypeEn()); 
    }
    
    private TWmRisk getRisk(){
        if(mdDocumentation.getDetailNode() instanceof CmRiskDocumentationView){
            return ((CmRiskDocumentationView)mdDocumentation.getDetailNode()).getRisk();
        }
        return null;
    }
    private void setRisk(TWmRisk pRisk){
        if(mdDocumentation.getDetailNode() instanceof CmRiskDocumentationView){
            ((CmRiskDocumentationView)mdDocumentation.getDetailNode()).setRisk(pRisk);
        }
    }
    private void addRisks(List<? extends TWmRiskDetails> addedSubList, boolean doAdd) {
        TWmRisk billingRisk = getRisk();
        if(billingRisk==null){
            return;
        }
        Set<TWmRiskDetails> details = billingRisk.getRiskDetails();
        if (details == null) {
            details = new HashSet<>();
            billingRisk.setRiskDetails(details);
        }

        for (TWmRiskDetails added : addedSubList) {
            boolean isAdded = false;
            for (TWmRiskDetails detail : details) {

                if (detail.getRiskArea().equals(added.getRiskArea())) {

                    detail.setRiskPercent(added.getRiskPercent());
                    detail.setRiskValue(added.getRiskValue());
                    detail.setRiskComment(added.getRiskComment());
                    detail.setRiskAuditPercent(added.getRiskAuditPercent() == null?0:added.getRiskAuditPercent());
                    detail.setRiskWastePercent(added.getRiskWastePercent() == null?0:added.getRiskWastePercent());
                    detail.setRiskAuditPercentSugg(added.getRiskAuditPercentSugg() == null?0:added.getRiskAuditPercentSugg());
                    detail.setRiskWastePercentSugg(added.getRiskWastePercentSugg() == null?0:added.getRiskWastePercentSugg());
                    detail.setRiskUsedForAuditFl(added.getRiskUsedForAuditFl());
                    detail.setRiskUsedForFinalFl(added.getRiskUsedForFinalFl());
                    detail.setRiskBaseFee(added.getRiskBaseFee() == null?BigDecimal.ZERO:added.getRiskBaseFee());
                    detail.setRiskNotCalculatedFee(added.getRiskNotCalculatedFee() ==null?BigDecimal.ZERO:added.getRiskNotCalculatedFee());
                    detail.setModificationDate(new Date());
                    detail.setModificationUser(Session.instance().getCpxUserId());
                    isAdded = true;
                    break;
                }
            }
            if (!isAdded && doAdd) {
                added.setRiskAuditPercent(added.getRiskAuditPercent() == null?0:added.getRiskAuditPercent());
                added.setRiskWastePercent(added.getRiskWastePercent() == null?0:added.getRiskWastePercent());
                added.setRiskAuditPercentSugg(added.getRiskAuditPercentSugg() == null?0:added.getRiskAuditPercentSugg());
                added.setRiskWastePercentSugg(added.getRiskWastePercentSugg() == null?0:added.getRiskWastePercentSugg());
                added.setRiskBaseFee(added.getRiskBaseFee() == null?BigDecimal.ZERO:added.getRiskBaseFee());
                added.setRiskNotCalculatedFee(added.getRiskNotCalculatedFee() ==null?BigDecimal.ZERO:added.getRiskNotCalculatedFee());
                added.setRisk(billingRisk);
                added.setCreationDate(new Date());
                added.setCreationUser(Session.instance().getCpxUserId());
                details.add(added);
            }
        }
        saveRisk(true);
    }

    private void saveRisk(boolean pAutoRefresh) {
            if(mdDocumentation.getDetailNode() instanceof CmRiskDocumentationView){
                TWmRisk risk = getRisk();
                risk = getScene().getFacade().saveRiskEntity(risk);
                setRisk(risk);
                if(pAutoRefresh){
                    refresh();
                }
            }
//            pRisk = getScene().getFacade().saveRiskEntity(pRisk);
//            refresh();
    }

    private void removeRisks(List<? extends TWmRiskDetails> pRemovedRisks) {
        TWmRisk billingRisk = getRisk();
        if(billingRisk == null){
            return;
        }
        Set<TWmRiskDetails> details = billingRisk.getRiskDetails();

        if (details == null || details.isEmpty() || pRemovedRisks == null || pRemovedRisks.isEmpty()) {
            LOG.log(Level.INFO, " can't remove any risk");
            return;
        }
//        List<Long> detailsIds = new ArrayList<>();
        for (TWmRiskDetails removed : pRemovedRisks) {

            for (TWmRiskDetails detail : details) {

                if (detail.getRiskArea().equals(removed.getRiskArea())) {
                    details.remove(detail);
                    detail.setRisk(null);
//                    detailsIds.add(detail.getId());
                    break;
                }
            }

        }
        saveRisk(true);
    }

    private boolean checkRiskDetails(TWmRiskDetails pRiskDetails) {
        if (pRiskDetails != null) {
            List<TWmRiskDetails> list = new ArrayList<>();
            list.add(pRiskDetails);
            addRisks(list, false);
            return true;
        }
        return false;
    }
    public CaseDocumentation getCaseDocumentation(){
        for(TitledPane tp : mdDocumentation.getAccordion().getPanes()){
            if(tp instanceof CaseDocumentation){
                return (CaseDocumentation) tp;
            }
        }
        return null;
    }
    public RiskDocumentation getRiskDocumentation(){
        for(TitledPane tp : mdDocumentation.getAccordion().getPanes()){
            if(tp instanceof RiskDocumentation){
                return (RiskDocumentation) tp;
            }
        }
        return null;
    }
//
//    //build detail layout
    private Parent createCaseEvaluationLayout(TCaseComment pComment) {
        if (pComment == null) {
            LOG.severe("Can not find active comment for this case and comment type!");
            Label lbl = new Label("Keine Fallbewertung ausgewählt!\nBitte wählen Sie einen Eintrag aus dem linken Menü aus.");
            HBox box = new HBox(lbl);
            box.setAlignment(Pos.CENTER);
            return box;
        }
        //stores number property for current number fo the comment
        LongProperty numberProperty = new SimpleLongProperty();
        numberProperty.set(pComment.getNumber());
        SectionHeader header = new SectionHeader(Lang.getDocumentationCaseEvaluation() + " " + Lang.getNumber(numberProperty.get()));
        numberProperty.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                header.setTitle(Lang.getDocumentationCaseEvaluation() + " " + Lang.getNumber(numberProperty.get()));
            }
        });
        //button to handle creation of the report
        Button btn = new Button("", ResourceLoader.getGlyph(FontAwesome.Glyph.FILE_PDF_ALT));
        btn.setTooltip(new CpxTooltip(Lang.getReportGenerate(), 100, 2000, 500, true));
        btn.setOnAction(new CreateReportAction(getScene().getFacade().getCurrentCase(), Boolean.FALSE, MainApp.getWindow(), pComment));
        //add button to menu
        header.addMenuItems(btn);

        //description text of the comment type
        Label lblDesc = new Label(Lang.getDocumentationCaseEvaluationText());
        VBox wrapper = new VBox();
        //free text to enter in comment
        //save will be delayed to not save very time a key is pressed
        TextArea taEvaluation = new TextArea(pComment.getText() != null ? String.valueOf(pComment.getText()) : "");
        taEvaluation.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    String comment = pComment.getText()!=null?new String(pComment.getText()):null;
                    if(taEvaluation.getText().equals(comment)){
                        LOG.log(Level.FINEST, "Comment Text equals already stored text! Update nothing!");
                        return;
                    }
                    if(taEvaluation.getText().isEmpty() && comment == null){
                        LOG.log(Level.FINEST, "CommentText is Empty and nothing was previously stored - ignore");
                        return;
                    }
                    //TODO: make sure only changed text is stored?!
                    updateCommentInDatabase(pComment, taEvaluation.getText());
                    numberProperty.set(pComment.getNumber());
                    wrapper.getChildren().remove(1);
                    wrapper.getChildren().add(1, getUserInfoGrid(pComment));
                    CaseDocumentation caseDoc = getCaseDocumentation();
                    if(caseDoc != null && caseDoc.getValues().isEmpty()){
//                        if(caseDoc.getValues().isEmpty()){
                        caseDoc.getValues().add(pComment);
//                        }
//                        caseDoc.setValues(CmDocumentationFXMLController.this.getScene().getFacade().findCaseComments(CommentTypeEn.caseReview));
//                        caseDoc.refresh();
                    }
                    LOG.log(Level.FINEST, "save Content!");
                }
            }
        });
        //add context menu option to context of the TextArea to enable generation of the report from there
        MenuItem menuItem = new MenuItem(Lang.getReportGenerate());
        menuItem.setOnAction(new CreateReportAction(getScene().getFacade().getCurrentCase(), Boolean.FALSE, MainApp.getWindow(), pComment));

        //add texttemplate custom MenuItem to the ContextMenu
//        TextTemplateController textTemplateController = new TextTemplateController(TextTemplateTypeEn.CaseReportContext, getScene().getWindow(), CmDocumentationFXMLController.this.getScene().getFacade().getCurrentCase());
//        TextAreaSkin customContextSkinUC = textTemplateController.customContextSkin(taEvaluation);
//        taEvaluation.setSkin(customContextSkinUC);
//        MenuItem textTemplateMenuItem = textTemplateController.getTextTemplateMenuItem();
        final List<MenuItem> menuItems = new ArrayList<>();
        taEvaluation.setSkin(new TextAreaSkin(taEvaluation));
        menuItems.add(new SeparatorMenuItem());
        MenuItem item = new MenuItem(TextTemplateController.MENU_ITEM_TEXT);
        item.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                TextTemplateController textTemplateController = new TextTemplateController(TextTemplateTypeEn.CaseReportContext, getScene().getWindow(), CmDocumentationFXMLController.this.getScene().getFacade().getCurrentCase());
                textTemplateController.getCreateFromTemplateEventHandler(taEvaluation).handle(t);
            }
        });
        menuItems.add(item);
//        menuItems.add(textTemplateMenuItem);
        menuItems.add(menuItem);
        ContextMenuUtil.install(taEvaluation, menuItems);
        taEvaluation.setMaxWidth(DIN_A4_WIDTH);
        taEvaluation.setWrapText(true);
        //layout work with sizing and spacing
        VBox.setVgrow(taEvaluation, Priority.ALWAYS);
        Pane userInfo = getUserInfoGrid(pComment);
        wrapper.getChildren().addAll(lblDesc, userInfo, taEvaluation);
        VBox.setVgrow(wrapper, Priority.ALWAYS);
        wrapper.setSpacing(12);
        wrapper.setPadding(new Insets(0, 8, 0, 8));
        VBox root = new VBox(header, wrapper);
        root.setFillWidth(true);
        root.setSpacing(12);
        root.setPadding(new Insets(0, 0, 0, 10));
        return root;
    }
//
//    //helper methode to update comment object stored in the lists with data from the server
//    //modifaction/creation date, id etc
    private void updateCommentMetaData(TCaseComment pComment, TCaseComment newComment) {
        pComment.setId(newComment.getId());
        pComment.setCreationDate(newComment.getCreationDate());
        pComment.setCreationUser(newComment.getCreationUser());
        pComment.setModificationDate(newComment.getModificationDate());
        pComment.setModificationUser(newComment.getModificationUser());
        pComment.setnumber(newComment.getNumber());
        pComment.setActive(newComment.isActive());
    }

    private long updateCommentInDatabase(TCaseComment pComment, String param) {
        pComment.setText(param.toCharArray());
        if (pComment.getCreationUser() == null) {
            pComment.setCreationUser(Session.instance().getCpxUserId());
        }
        if (pComment.getCreationDate() == null) {
            pComment.setCreationDate(new Date());
        }
        //if comment is new, it will be saved after the user added a comment
        //will be appended in the listview
        if (pComment.getId() == 0) {
            pComment.setnumber(1L);
            pComment.setActive(Boolean.TRUE);
        }
        //store comment
        TCaseComment newComment = CmDocumentationFXMLController.this.getScene().getFacade().storeComment(pComment);
        updateCommentMetaData(pComment, newComment);
        //update infos
        return pComment.getNumber();
    }

    //creates user info grid, creationdate/modification date/user
    private Pane getUserInfoGrid(TCaseComment pComment) {
        GridPane pane = new GridPane();
        pane.setHgap(5);
        Label lblCreationDateTxt = new Label(Lang.getCreatedOn(""));
        Label lblModificationDateTxt = new Label(Lang.getModifiedOn(""));
        Label lblModificationDate = new Label();
        Label lblCreationDate = new Label();
        Label lblCreationUserTxt = new Label(Lang.getCreatedFrom(""));
        Label lblModificationUserTxt = new Label(Lang.getModifiedFrom(""));
        Label lblCreationUser = new Label();
        Label lblModificationUser = new Label();

        pane.addRow(0, lblCreationDateTxt, lblCreationDate, lblCreationUserTxt, lblCreationUser);
        pane.addRow(1, lblModificationDateTxt, lblModificationDate, lblModificationUserTxt, lblModificationUser);
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lblCreationDate.setText(pComment.getCreationDate() != null ? Lang.toDateTime(pComment.getCreationDate()) : "");
                lblCreationUser.setText(pComment.getCreationUser() != null ? getScene().getFacade().findUserName(pComment.getCreationUser()) : "");
                if(pComment.getModificationUser() != null){
                    lblModificationDate.setText(pComment.getModificationDate() != null ? Lang.toDateTime(pComment.getModificationDate()) : "");
                    lblModificationUser.setText(pComment.getModificationUser() != null ? getScene().getFacade().findUserName(pComment.getModificationUser()) : "");
                }
            }
        });
        return pane;
    }

    private List<Node> createRiskMenuItems(TCaseDetails pDetails) {
        TableButton tableButton = new TableButton();
        tableButton.setTooltip(new Tooltip("Fallversion: " + VersionStringConverter.convertSimple(pDetails) + " in der DRG-Übersicht anzeigen"));
        tableButton.setOnAction(new EventHandler() {
            @Override
            public void handle(Event t) {
                VersionManager manager = getScene().getVersionManager();
                TCaseDetails newValue = manager.getLocalVersion(pDetails.getId());
                if (manager.isDisplayed(newValue)) {
                    NotificationsFactory.instance().createInformationNotification()
                                .text("Fall-Version: " + VersionStringConverter.convertSimple(newValue) + " wird bereits angezeigt!")
                                .show();
                    return;
                }
                if (manager.getManagedVersions().size() > 1) {
                    if (!manager.getManagedVersions().get(1).getContent().equals(newValue) && !manager.isDisplayed(newValue)) {
                        manager.getManagedVersions().get(1).setContent(newValue);
                    }
                } else {
                    manager.createAndAddVersionContent(newValue);
                }
            }
        });
        AddTableButton tableAddButton = new AddTableButton();
        tableAddButton.setId("table-add-btn");
        if(getScene().getVersionManager().getDisplayIndexOf(pDetails)>1){
            tableAddButton.setRemoveMode(true);
        }
        tableAddButton.setOnAction(new EventHandler() {
            @Override
            public void handle(Event t) {
                VersionManager manager = getScene().getVersionManager();
                TCaseDetails newValue = manager.getLocalVersion(pDetails.getId());
//                manager.createAndAddVersionContent(newValue);
                if (manager.isDisplayed(newValue)) {
                    if(manager.isAdditionalDisplayVersion(newValue)){
                        manager.removeFromManagedVersions(manager.getManagedVersionForId(newValue));
                        tableAddButton.setRemoveMode(false);
                    }else{
                        NotificationsFactory.instance().createInformationNotification()
                                .text("Fall-Version: " + VersionStringConverter.convertSimple(newValue) + " wird bereits angezeigt!")
                                .show();
                    }
                    return;
                }
                if (manager.getManagedVersions().size() > 2) {
                    if (!manager.getManagedVersions().get(2).getContent().equals(newValue) && !manager.isDisplayed(newValue)) {
                        manager.getManagedVersions().get(2).setContent(newValue);
                        tableAddButton.setRemoveMode(true);
                    }
                } else {
                        manager.createAndAddVersionContent(newValue);
                        tableAddButton.setRemoveMode(true);
                }

            }
        });
        tableAddButton.setTooltip(new Tooltip("Fallversion: " + VersionStringConverter.convertSimple(pDetails) + " als dritte Version in DRG-Übersicht anzeigen"));
        
        List<Node> nodes = new ArrayList<>();
        nodes.add(tableButton);
        nodes.add(tableAddButton);
        return nodes;
    }

    //create report event
    //in task there is some ui work to do after successfully create the report
    private class CreateReportAction extends CreateReportEvent {

        private final TCaseComment comment;

        /**
         * creates new action to invoke report creation
         *
         * @param pCase case to generate the report
         * @param pTempOnly stored in filesystem
         * @param pOwner owner window for Progressdialogs
         * @param pOption option Object
         * @param pComment commet to use
         */
        public CreateReportAction(TCase pCase, Boolean pTempOnly, Window pOwner, TCaseComment pComment) {
            super(pCase, pTempOnly, pOwner);
            this.comment = pComment;
        }

        @Override
        public void afterTask(Worker.State pState) {
            //life cycle calls after task is completted
            super.afterTask(pState);
            //if state was anything but succeeded, abort
            if (!pState.equals(Worker.State.SUCCEEDED)) {
                return;
            }
            //only if succeeded
            if (comment.isActive()) {
                return;
            }
            
            //only if comment is not already active
            TCaseComment old = getCaseDocumentation().getActiveComment();//CmDocumentationFXMLController.this.getScene().getFacade().findActiveComment(CommentTypeEn.caseReview);//option.getActive();
            if (old != null) {
                old.setActive(Boolean.FALSE);
                CmDocumentationFXMLController.this.getScene().getFacade().storeComment(old);
                if (!comment.isActive()) {
                    comment.setActive(Boolean.TRUE);
                }
                CmDocumentationFXMLController.this.getScene().getFacade().storeComment(comment);
                //helper to force refresh in list, to avoid some wrapper shenanigans
                //to inform list of updates in one of its items
                //dirty solution
                refresh();
            }
        }
    }

}
