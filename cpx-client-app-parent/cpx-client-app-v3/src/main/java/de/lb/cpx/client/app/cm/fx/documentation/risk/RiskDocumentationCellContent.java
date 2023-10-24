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
package de.lb.cpx.client.app.cm.fx.documentation.risk;

import de.lb.cpx.client.app.cm.fx.documentation.risk.tree.RiskDocumentation4Version;
import de.lb.cpx.client.app.cm.fx.documentation.risk.tree.RiskDocumentationItem;
import de.lb.cpx.client.app.cm.fx.documentation.risk.tree.RiskDocumentionCategory;
import de.lb.cpx.client.core.model.fx.menu.MenuedControl;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.enums.VersionRiskTypeEn;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 *
 * @author wilde
 */
public class RiskDocumentationCellContent extends MenuedControl<TreeView<RiskDocumentationItem>> {

    private static final PseudoClass RISK_EVEN = PseudoClass.getPseudoClass("risk_even");
    //list of comment entites
    private final ObservableList<TCaseDetails> versions;
    //property to monitor state if user currently selected the active comment
    
//    private ObjectProperty<RiskDocumentationItem> selectedItemProperty;
    public RiskDocumentationCellContent(){
        this(new TreeView<>(),FXCollections.observableArrayList());
    }
    public RiskDocumentationCellContent(TreeView<RiskDocumentationItem> pTreeView){
        this(pTreeView,FXCollections.observableArrayList());
    }
    public RiskDocumentationCellContent(TreeView<RiskDocumentationItem> pTreeView,ObservableList<TCaseDetails> pItems) {
        super(pTreeView);
        getControl().getStyleClass().add("risk-tree-view");
        versions = FXCollections.observableArrayList();
        versions.addAll(pItems);
        getControl().setCellFactory(new Callback<TreeView<RiskDocumentationItem>, TreeCell<RiskDocumentationItem>>() {
            @Override
            public TreeCell<RiskDocumentationItem> call(TreeView<RiskDocumentationItem> param) {
                return new RiskDocumentationCell();
            }
        });
        setTreeItems(versions);
        //refresh controll and select actvie comment if size of comment list changes
        versions.addListener(new ListChangeListener<TCaseDetails>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends TCaseDetails> c) {
                if(c.getList().isEmpty()){
                    getControl().getRoot().getChildren().clear();
                    return;
                }
                setTreeItems(new ArrayList<>(c.getList()));
            }
        });
    }
    public void setVersions(ObservableList<TCaseDetails> pVersion){
        versions.setAll(pVersion);
    }
    private Callback<RiskDocumentationItem,Boolean> onSelectItem;
    public Callback<RiskDocumentationItem, Boolean> getOnSelectItem() {
        return onSelectItem;
    }

    public void setOnSelectItem(Callback<RiskDocumentationItem, Boolean> pOnSelectItem) {
        onSelectItem = pOnSelectItem;
    }
    private Callback<TCaseDetails, Boolean> onSelectVersion;

    public Callback<TCaseDetails, Boolean> getOnUpdateRisk() {
        return onSelectVersion;
    }

    public void setOnSelectVersion(Callback< TCaseDetails, Boolean> pOnSelectVersion) {
        onSelectVersion = pOnSelectVersion;
    }

    private Callback<TCaseDetails, Boolean> checkActualRisk;

    public Callback<TCaseDetails, Boolean> getCheckActualRisk() {
        return checkActualRisk;
    }

    public void setCheckActualRisk(Callback< TCaseDetails, Boolean> pCheckActualRisk) {
        checkActualRisk = pCheckActualRisk;
    }

    private boolean isVersionActive(TCaseDetails version) {
        if (checkActualRisk != null) {
            return checkActualRisk.call(version);
        }
        return false;
    }
    
    private List<TreeItem<RiskDocumentationItem>> createTreeItems(List<TCaseDetails> pVersions){
        List<TreeItem<RiskDocumentationItem>> treeItems = new ArrayList<>();
        Map<VersionRiskTypeEn,List<TCaseDetails>> mapTypeToList = transformVersionListInMap(pVersions);
        Iterator<VersionRiskTypeEn> it = mapTypeToList.keySet().iterator();
        while (it.hasNext()) {
            VersionRiskTypeEn next = it.next();
            List<TCaseDetails> details = mapTypeToList.get(next);
            details = Objects.requireNonNullElse(details, new ArrayList<>());
            RiskDocumentionCategory treeCategory = new RiskDocumentionCategory(next,details.size());
            TreeItem<RiskDocumentationItem> rootCategory = new TreeItem<>(treeCategory);
//            rootCategory.setExpanded(true);
            treeItems.add(rootCategory);
            for(int i = 0;i<details.size();i++){
                TCaseDetails detail = details.get(i);
                detail.getCsdVersion();
                RiskDocumentation4Version riskDetail = new RiskDocumentation4Version(detail, isVersionActive(detail));
                boolean isEven = (i % 2 == 0);
                
                riskDetail.setDisplayStyle(isEven?RiskDocumentationItem.DisplayStyle.EVEN:RiskDocumentationItem.DisplayStyle.ODD);
                TreeItem<RiskDocumentationItem> treeDetail = new TreeItem<>(riskDetail);
                rootCategory.getChildren().add(treeDetail);
            }
            
        }
        return treeItems;
    }

    private Map<VersionRiskTypeEn, List<TCaseDetails>> transformVersionListInMap(List<TCaseDetails> pVersions) {
        pVersions = Objects.requireNonNullElse(pVersions, new ArrayList<>());
        Map<VersionRiskTypeEn,List<TCaseDetails>> map = new EnumMap<>(VersionRiskTypeEn.class);
        for(TCaseDetails version : pVersions){
            VersionRiskTypeEn versionRisk = Objects.requireNonNullElse(version.getCsdVersRiskTypeEn(), VersionRiskTypeEn.NOT_SET);
            if(!map.containsKey(versionRisk)){
                map.put(versionRisk, new ArrayList<>());
            }
            map.get(versionRisk).add(version);
        }
        return map;
    }

    private void setTreeItems(List<TCaseDetails> versions) {
        if(getControl().getRoot() != null){
            getControl().getRoot().getChildren().clear();
        }
        if(getControl().getRoot() == null){
            getControl().setShowRoot(false);
            getControl().setRoot(new TreeItem<>());
        }
        getControl().getRoot().getChildren().setAll(createTreeItems(versions));
    }

    public void selectDetail(TCaseDetails tCaseDetails) {
        if (getControl().getRoot() == null) {
            return;
        }

        TreeItem<RiskDocumentationItem> treeItem = lookupDetail(getControl().getRoot(), tCaseDetails);
        if (treeItem == null) {
            return;
        }
        int row = getControl().getRow(treeItem);
        expandTreeView(treeItem);
        
        getControl().getSelectionModel().select(treeItem);
        getControl().scrollTo(row);

    }

    private void expandTreeView(TreeItem<RiskDocumentationItem> selectedItem) {
        if (selectedItem != null) {
            expandTreeView(selectedItem.getParent());

            if (!selectedItem.isLeaf()) {
                selectedItem.setExpanded(true);
            }
        }
    }
    
    public TreeItem<RiskDocumentationItem> lookupDetail(TreeItem<RiskDocumentationItem> pItem,TCaseDetails pDetail){
        if(pDetail == null){
            return null;
        }
        if(pItem.getValue() instanceof RiskDocumentation4Version){
            RiskDocumentation4Version forVersion = (RiskDocumentation4Version) pItem.getValue();
            if(pDetail.equals(forVersion.getDetails())){
                return pItem;
            }
        }
        for(TreeItem<RiskDocumentationItem> item:pItem.getChildren()){
            TreeItem<RiskDocumentationItem> value = lookupDetail(item, pDetail);
            if(value != null){
                return value;
            }
        }
        return null;
    }
    
    private class RiskDocumentationCell extends TreeCell<RiskDocumentationItem> {

        private final VBox root;
        private final Label desc;
        private final Label title;

        public RiskDocumentationCell() {
            super();
            title = new Label();
            title.setMinWidth(USE_COMPUTED_SIZE);
            title.setMaxWidth(Double.MAX_VALUE);

            desc = new Label();
            desc.setMinWidth(USE_COMPUTED_SIZE);
            desc.setMaxWidth(Double.MAX_VALUE);
            
            root = new VBox(5,title);
            root.setFillWidth(true);
            root.setMaxWidth(Double.MAX_VALUE);
            root.setMinWidth(USE_COMPUTED_SIZE);
            setGraphic(root);
            getStyleClass().add(0,"risk-tree-cell");
            setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    if(getOnSelectItem()!=null){
                        getOnSelectItem().call(isSelected()?getItem():null);
                    }
                }
            });
        }

        @Override
        public void updateItem(RiskDocumentationItem item, boolean empty) {
            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
            if (item == null || empty) {
                title.setText("");
                desc.setText("");
                root.setVisible(false);
                getDisclosureNode().setVisible(false);
                setStyleClass(RiskDocumentationItem.DisplayStyle.NONE);
                return;
            }
            root.setVisible(true);
            getDisclosureNode().setVisible(!getTreeItem().isLeaf());
            title.setText(item.getTitle());
            
            if (item instanceof RiskDocumentation4Version) {
                desc.setText(((RiskDocumentation4Version) item).getDescription());
                if(!root.getChildren().contains(desc)){
                    root.getChildren().add(desc);
                }
            }else{
                desc.setText("");
                if(root.getChildren().contains(desc)){
                    root.getChildren().remove(desc);
                }
            }
            setStyleClass(item.getDisplayStyle());
        }

        private void setStyleClass(RiskDocumentationItem.DisplayStyle displayStyle) {
            switch (displayStyle) {
                case EVEN:
                    pseudoClassStateChanged(RISK_EVEN, true);
                    return;
                case ODD:
                    pseudoClassStateChanged(RISK_EVEN, false);
                    return;
                default:
                    pseudoClassStateChanged(RISK_EVEN, false);
            }
        }
    }
}
