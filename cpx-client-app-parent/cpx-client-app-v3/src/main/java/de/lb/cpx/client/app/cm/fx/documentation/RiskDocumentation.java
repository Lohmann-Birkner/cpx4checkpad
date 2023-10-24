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
package de.lb.cpx.client.app.cm.fx.documentation;

import de.lb.cpx.client.core.model.fx.titledpane.AccordionSelectedItem;
import de.lb.cpx.client.app.cm.fx.documentation.risk.RiskDocumentationCellContent;
import de.lb.cpx.client.app.cm.fx.documentation.risk.tree.RiskDocumentation4Version;
import de.lb.cpx.client.app.cm.fx.documentation.risk.tree.RiskDocumentationItem;
import de.lb.cpx.client.app.cm.fx.documentation.risk.tree.RiskDocumentionCategory;
import de.lb.cpx.client.core.model.fx.menu.MenuedControlSkin;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.enums.CommentTypeEn;
import de.lb.cpx.model.enums.VersionRiskTypeEn;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Callback;

/**
 *
 * @author wilde
 */
public class RiskDocumentation extends DocumentationTitledPane<TCaseDetails> {
    private static final Logger LOG = Logger.getLogger(RiskDocumentation.class.getName());
    private TreeView<RiskDocumentationItem> treeView;
    private TCaseDetails selected;
    private boolean clearFlag;
    public RiskDocumentation() {
        super(CommentTypeEn.riskReview);
        setContentFactory(new Callback<CommentTypeEn, Node>() {
            @Override
            public Node call(CommentTypeEn param) {
                treeView = new TreeView<>();
                RiskDocumentationCellContent pane = new RiskDocumentationCellContent(treeView);
                pane.setSkin(new MenuedControlSkin(pane));
                pane.getControl().getStyleClass().add("stay-selected-tree-view");
                Callback<TCaseDetails, Boolean> onSelectVersion = new Callback< TCaseDetails, Boolean>() {
                    @Override
                    public Boolean call(TCaseDetails version) {
                        selected = version;
                        getSelectItemCallback().call(new DocTitledPaneSelectedItem<>(version));
                        return true;
                    }

                };
//                                    
                pane.setOnSelectVersion(onSelectVersion);
                Callback<TCaseDetails, Boolean> checkActualRisk = new Callback< TCaseDetails, Boolean>() {
                    @Override
                    public Boolean call(TCaseDetails version) {
                        boolean actual = getIsActualRiskCallback().call(version);
                        return actual;
                    }

                };
                pane.setCheckActualRisk(checkActualRisk);
                pane.setOnSelectItem(new Callback<RiskDocumentationItem, Boolean>() {
                    @Override
                    public Boolean call(RiskDocumentationItem param) {
                        
                        TCaseDetails detail = null;
                        AccordionSelectedItem<TCaseDetails> selectedItem = null;
                        if (param instanceof RiskDocumentation4Version) {
                            detail = ((RiskDocumentation4Version) param).getDetails();
                            selectedItem = createSelectedItem(detail);
                        }
                        if (param instanceof RiskDocumentionCategory) {
                            VersionRiskTypeEn type = ((RiskDocumentionCategory) param).getType();
                            detail = getMostRecentCaseVersion(((RiskDocumentionCategory) param).getType());
                            if(detail == null){
                                selectedItem = createVoidItem(SelectionTarget.CONTENT_ITEM,type);
                            }else{
                                selectedItem = createSelectedItem(detail);
                            }
//                            getSelectItemCallback().call(createVoidItem("Es konnte kein aktives Risiko für den Bereich: " + type.getTranslation().getValue() + " gefunden werden!\n"
//                            + "Bitte wählen Sie ein Risiko aus in dem Sie es als 'Zur Risikoauswertung verwenden' markieren."));
                        }
                        selected = detail;
                        getSelectItemCallback().call(selectedItem);
//                        getItem().setSelectedValue(detail);
//                        mdDocumentation.setDetail(detail != null ? createRiskLayout(detail) : getPlaceholder());
                        return true;
                    }
                });
                pane.setVersions(getValues());
                if(selected!=null){
                    pane.selectDetail(selected);
                }
                return pane;
            }
        });
        expandedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if (t1 && getContent() != null && (getContent() instanceof RiskDocumentationCellContent)) {
//                    if (getContent() instanceof RiskDocumentationCellContent) {
                    getSelectItemCallback().call(getMostRecentItem(SelectionTarget.CONTENT_ITEM));
//                    }
                    select(getMostRecentCaseVersion(getRecentRiskType()));
                }
            }
        });

    }
    
    @Override
    public void clearSelection(){
        if(treeView.getSelectionModel().getSelectedItem()!=null){
            clearFlag = true;
        }
        treeView.getSelectionModel().clearSelection();
    }
    
    public TCaseDetails getSelected(){
        if(selected!=null){
            return selected;
        }else{
            try{
            TreeItem<RiskDocumentationItem> treeSelected = treeView.getSelectionModel().getSelectedItem();
            if(treeSelected!=null && (treeSelected.getValue() instanceof RiskDocumentation4Version)){
                return ((RiskDocumentation4Version)treeSelected.getValue()).getDetails();
            }
            }catch(NullPointerException ex){
                LOG.log(Level.SEVERE, "", ex);
            }
        }
        return null;
    }
    public VersionRiskTypeEn getRecentRiskType(){
        RiskDocumentationCellContent content = (RiskDocumentationCellContent) getContent();
        
        VersionRiskTypeEn riskType = VersionRiskTypeEn.NOT_SET;
        if(content != null){
            for(TreeItem<RiskDocumentationItem> item : content.getControl().getRoot().getChildren()){
                if(item.getValue() instanceof RiskDocumentionCategory){
                    VersionRiskTypeEn category = ((RiskDocumentionCategory)item.getValue()).getType();
                    if(riskType.getId()<category.getId()){
                        riskType = category;
                    }
                }
            }
        }
        return riskType;
    }
    public AccordionSelectedItem<TCaseDetails> createSelectedItem(TCaseDetails pItem){
        return createSelectedItem(pItem, null);
    }
    public AccordionSelectedItem<TCaseDetails> createVoidItem(String pText){
        return createSelectedItem(null, pText);
    }
    public AccordionSelectedItem<TCaseDetails> createVoidItem(SelectionTarget pTarget, VersionRiskTypeEn pType){
        String typeText = pType.getTranslation().getValue();
        Text text1 = new Text("Es konnte kein aktives Risiko für den Bereich ");
        Text text2 = new Text(typeText);
        text2.setFont(Font.font(text2.getFont().getFamily(), FontWeight.BOLD, text2.getFont().getSize()));
        Text text3 = new Text(" gefunden werden!\n"
                            + "Bitte wählen Sie ein Risiko aus in dem Sie es als 'Zur Risikoauswertung verwendet' markieren.");
        Text text4 = new Text("\n"
                            + "(Risikobewertung > " + typeText + " > Auswahl einer Version > 'Zur Risikoauswertung verwendet')");
        text4.setFont(Font.font(text4.getFont().getFamily(), FontPosture.ITALIC, text4.getFont().getSize()));
        TCaseDetails max = getNewestCaseVersion(pType);
        return new DocTitledPaneVoidItem<>(pTarget,max,text1,text2,text3,text4);
    }
    public AccordionSelectedItem<TCaseDetails> createSelectedItem(TCaseDetails pItem,String pText){
        if (pItem == null) {
            return new DocTitledPaneVoidItem<>(pText);
        } else {
            return new DocTitledPaneSelectedItem<>(pItem);
        }
    }
    public AccordionSelectedItem<TCaseDetails> getMostRecentItem(SelectionTarget pTarget, VersionRiskTypeEn pType) {
        pType = Objects.requireNonNullElse(pType, VersionRiskTypeEn.NOT_SET);
        pTarget = Objects.requireNonNullElse(pTarget, SelectionTarget.CONTENT_ITEM);
        TCaseDetails item = getMostRecentCaseVersion(pType);
        if (item == null) {
            return createVoidItem(pTarget,pType);
//            return createVoidItem("Es konnte kein aktives Risiko für den Bereich: " + pType.getTranslation().getValue() + " gefunden werden!\n"
//                            + "Bitte wählen Sie ein Risiko aus in dem Sie es als 'Zur Risikoauswertung verwenden' markieren.");
        } else {
            return createSelectedItem(item);
        }
    }
    @Override
    public AccordionSelectedItem<TCaseDetails> getMostRecentItem(SelectionTarget pTarget) {
        return getMostRecentItem(pTarget,getRecentRiskType());
    }

    @Override
    public void select(TCaseDetails pItem) {
        RiskDocumentationCellContent content = (RiskDocumentationCellContent) getContent();
        if(content != null){
            content.selectDetail(pItem);
        }
    }
    
    private Callback<TCaseDetails,Boolean> isActualRiskDefaultCallback = new Callback<TCaseDetails, Boolean>() {
        @Override
        public Boolean call(TCaseDetails param) {
            return false;
        }
    };
    private Callback<TCaseDetails,Boolean> isActualRiskCallback = isActualRiskDefaultCallback;
    public void setIsActualRiskCallback(Callback<TCaseDetails,Boolean> pCallback){
        isActualRiskCallback = Objects.requireNonNullElse(pCallback, isActualRiskDefaultCallback);
    }
    public Callback<TCaseDetails,Boolean> getIsActualRiskCallback(){
        return isActualRiskCallback;
    }
    public TCaseDetails getMostRecentCaseVersion(VersionRiskTypeEn pTypeEn){
        RiskDocumentationCellContent content = (RiskDocumentationCellContent) getContent();
        if(content == null){
            return null;
        }
        return getMostRecentCaseVersion(pTypeEn, content.getControl().getRoot());
    }
    public TCaseDetails getMostRecentCaseVersion(VersionRiskTypeEn pTypeEn, TreeItem<RiskDocumentationItem> pItem){
        if(pItem == null){
            return null;
        }
        for(TreeItem<RiskDocumentationItem> item : pItem.getChildren()){
            TCaseDetails detail = getMostRecentCaseVersion(pTypeEn, item);
            if(detail !=null){
                return detail;
            }
        }
        
        if(pItem.getValue() == null){
            return null;
        }
        if(pItem.getValue() instanceof RiskDocumentation4Version){
            RiskDocumentation4Version version = (RiskDocumentation4Version) pItem.getValue();
            RiskDocumentionCategory category = (RiskDocumentionCategory) pItem.getParent().getValue();
            if(version.isActual() && pTypeEn.equals(category.getType())){
                return version.getDetails();
            }
        }
        if(pItem.getValue() instanceof RiskDocumentionCategory){
            RiskDocumentionCategory version = (RiskDocumentionCategory) pItem.getValue();
            if(!version.getType().equals(pTypeEn)){
                return null;
            }
        }
        return null;
    }
    public TCaseDetails getNewestCaseVersion(VersionRiskTypeEn pTypeEn){
        RiskDocumentationCellContent content = (RiskDocumentationCellContent) getContent();
        if(content == null){
            return null;
        }
        return getNewestCaseVersion(pTypeEn, content.getControl().getRoot());
    }
    public TCaseDetails getNewestCaseVersion(VersionRiskTypeEn pTypeEn,TreeItem<RiskDocumentationItem> pItem){
        if(pItem == null){
            return null;
        }
        for(TreeItem<RiskDocumentationItem> item : pItem.getChildren()){
            TCaseDetails detail = getNewestCaseVersion(pTypeEn, item);
            if(detail != null){
                return detail;
            }
        }
        
        if(pItem.getValue() == null){
            return null;
        }
        if(pItem.getValue() instanceof RiskDocumentionCategory){
            RiskDocumentionCategory version = (RiskDocumentionCategory) pItem.getValue();
            if(!version.getType().equals(pTypeEn)){
                return null;
            }else{
                List<RiskDocumentationItem> items = pItem.getChildren().stream().map((t) -> {
                    return t.getValue();
                }).filter(new Predicate<RiskDocumentationItem>() {
                    @Override
                    public boolean test(RiskDocumentationItem t) {
                        return t instanceof RiskDocumentation4Version;
                    }
                }).collect(Collectors.toList());
                items.sort(new Comparator<RiskDocumentationItem>() {
                    @Override
                    public int compare(RiskDocumentationItem o1, RiskDocumentationItem o2) {
                        if(o1==null || ((RiskDocumentation4Version)o1).getDetails() == null){
                            return -1;
                        }
                        if(o2 == null|| ((RiskDocumentation4Version)o2).getDetails() == null){
                            return -1;
                        }
                        return Integer.compare((((RiskDocumentation4Version)o2).getDetails().getCsdVersion()),(((RiskDocumentation4Version)o1).getDetails().getCsdVersion()));
                    }
                });
                return items.isEmpty()?null:((RiskDocumentation4Version)items.get(0)).getDetails();
            }
        }
        return null;
    }
}
