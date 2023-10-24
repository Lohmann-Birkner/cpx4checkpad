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
package de.lb.cpx.ruleviewer.layout;

import de.lb.cpx.rule.criteria.CriteriaHelper;
import de.lb.cpx.rule.criteria.CriteriaManager;
import de.lb.cpx.rule.criteria.model.Criteria;
import de.lb.cpx.rule.criteria.model.CriterionTree;
import de.lb.cpx.rule.criteria.model.CriterionTree.Supergroup.Group;
import de.lb.cpx.rule.criteria.model.CriterionTree.Supergroup.Group.Criterion;
import de.lb.cpx.shared.lang.Lang;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.skin.ScrollPaneSkin;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 *
 * @author wilde
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class CriteriaViewSkin extends SkinBase<CriteriaView> {

    private TextField tfSearchField;
    private TreeView tvItems;
    private VBox boxTooltips;

//    private static final String FALL_NUMERIC="Fall.Numeric";
//    private static final String FALL_STRING="Fall.String";
    public CriteriaViewSkin(CriteriaView pSkinnable) throws IOException {
        super(pSkinnable);
        getChildren().add(initRoot());
        tvItems.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                updateDetails(newValue);
            }
        });
        setCriteria();
        getSkinnable().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (KeyCode.ESCAPE.equals(event.getCode()) || KeyCode.ENTER.equals(event.getCode())) {
                    TreeItem selected = (TreeItem) tvItems.getSelectionModel().getSelectedItem();
                    if (selected != null) {
                        if (selected.getValue() instanceof Criterion) {
                            handleSelection((Criterion) selected.getValue());
                        }
                    }
                }
            }
        });
        getSkinnable().selectedItemProperty().addListener(new ChangeListener<Criterion>() {
            @Override
            public void changed(ObservableValue<? extends Criterion> observable, Criterion oldValue, Criterion newValue) {
                selectCriterion(newValue);
            }
        });
        selectCriterion(getSkinnable().selectedItemProperty().get());
    }

    private void handleSelection(Criterion crit) {
        if (getSkinnable().getOnSelectedCallback() != null) {
            getSkinnable().getOnSelectedCallback().call(crit);
        }
    }
//    public void selectCriterion(Criterion pCriterion){
//        selectedCriterionProperty().set(pCriterion);
//    }

    private Parent initRoot() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/CriteriaView.fxml"));
        tfSearchField = (TextField) root.lookup("#tfSearchField");
        tfSearchField.setPromptText("Hier Suchtext eingeben");
        tvItems = (TreeView) root.lookup("#tvItems");
        tvItems.setFocusTraversable(true);
        tvItems.setRoot(new TreeItem<>());
        tvItems.setShowRoot(false);
        tvItems.setCellFactory(new Callback<>() {
            @Override
            public Object call(Object param) {
                return new TreeCell<Object>() {
                    @Override
                    protected void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText("");
                            return;
                        }
                        if (item instanceof Criterion) {
                            Criterion crit = (Criterion) item;
                            setText(getDisplayName(crit));
                            setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent event) {
                                    if (event.getClickCount() >= 2) {
                                        handleSelection(crit);
                                    }
                                }
                            });
                        }
                        if (item instanceof String) {
                            setText(((String) item).trim());
                        }
                        if (item instanceof Group) {
                            Group group = (Group) item;
                            setText(group.getCpname().trim());
                        }
                        if (item instanceof CriterionTree.Supergroup) {
                            CriterionTree.Supergroup superGroup = (CriterionTree.Supergroup) item;
                            setText(superGroup.getCpname().trim());
                        }
                    }
                };
            }
        });
        ScrollPane spTooltips = (ScrollPane) root.lookup("#spTooltips");
        spTooltips.setFitToWidth(true);
        spTooltips.setSkin(new ScrollPaneSkin(spTooltips));

        boxTooltips = (VBox) root.lookup("#boxTooltips");

        return root;
    }

    private String getDisplayName(Criterion pCrit) {
        return CriteriaHelper.getDisplayName(pCrit);
    }

    private List<Label> updateCriterionDetails(Criterion pItem) {
//        getSkinnable().setSelectedItem(pItem);
        List<Label> labels = new ArrayList<>();
        for (Criterion.Tooltip tip : pItem.getTooltip()) {
//            if(tip.getCpname() != null &&(tip.getCpname().contains(FALL_STRING)||tip.getCpname().contains(FALL_NUMERIC))){
//                continue;
//            }
            String txt = getCriterionTooltipText(tip);
            Label label = new Label(txt);
            label.setWrapText(true);
            labels.add(label);
        }
        return labels;
    }

    private List<Label> updateSuperGroupDetails(CriterionTree.Supergroup pItem) {
        List<Label> labels = new ArrayList<>();
        for (CriterionTree.Supergroup.Tooltip tip : pItem.getTooltip()) {
//            if(tip.getCpname().contains(FALL_STRING)||tip.getCpname().contains(FALL_NUMERIC)){
//                continue;
//            }
            Label label = new Label(tip.getCpname().trim());
            label.setWrapText(true);
            labels.add(label);
        }
        return labels;
    }

    private List<Label> updateGroupDetails(Group pItem) {
        List<Label> labels = new ArrayList<>();
        for (Group.Tooltip tip : pItem.getTooltip()) {
//            if(tip.getCpname().contains(FALL_STRING)||tip.getCpname().contains(FALL_NUMERIC)){
//                continue;
//            }
            Label label = new Label(tip.getCpname().trim());
            label.setWrapText(true);
            labels.add(label);
        }
        return labels;
    }

    private void updateDetails(Object pItem) {
        boxTooltips.getChildren().clear();
        if (pItem == null) {
            return;
        }
        pItem = ((TreeItem) pItem).getValue();
        List<Label> labels = null;
        if (pItem instanceof Criterion) {
            labels = updateCriterionDetails((Criterion) pItem);
        }
        if (pItem instanceof Group) {
            labels = updateGroupDetails((Group) pItem);
        }
        if (pItem instanceof CriterionTree.Supergroup) {
            labels = updateSuperGroupDetails((CriterionTree.Supergroup) pItem);
        }
        if (labels != null) {
            boxTooltips.getChildren().addAll(labels);
        }
    }

    private void setCriteria() {

        for (Criteria crit : getSkinnable().getItems()) {
            tvItems.getRoot().getChildren().add(getTreeItemForCriteria(crit, false));
        }
        tfSearchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                tvItems.setRoot(new TreeItem());
                List<Criteria> filted = CriteriaManager.instance().filterCriteria(getSkinnable().getItems(), newValue/*new ArrayList<>(c.getList())*/);
                for (Criteria crit : filted) {
                    tvItems.getRoot().getChildren().add(getTreeItemForCriteria(crit, !newValue.isEmpty()));
                }
                tvItems.refresh();
            }
        });
    }

    private TreeItem getTreeItemForCriteria(Criteria pCriteria, boolean pExpandAll) {
        CriterionTree.Supergroup sGroup = pCriteria.getSupergroup();
        TreeItem item = new TreeItem<>(sGroup);
        item.setExpanded(pExpandAll);
        List<Group> sortedGroup = new ArrayList<>(sGroup.getGroup());
        sortedGroup.sort(new Comparator<Group>() {
            @Override
            public int compare(Group o1, Group o2) {
                return (o1.getCpname().toLowerCase().compareTo(o2.getCpname().toLowerCase()));
            }
        });
        for (Group group : sortedGroup) {
//            if(group.getName().equals("rules.GKMedicineNode.medicine")){
//                continue;
//            }
//            if(group.getName().equals("rules.txt.group.labor.dis")){
//                continue;
//            }
            TreeItem gItem = new TreeItem<>(group);
            gItem.setExpanded(pExpandAll);
            item.getChildren().add(gItem);
            List<Criterion> sortedCrit = new ArrayList<>(group.getCriterion());
            sortedCrit.sort(new Comparator<Criterion>() {
                @Override
                public int compare(Criterion o1, Criterion o2) {
                    return (getDisplayName(o1).toLowerCase().compareTo(getDisplayName(o2).toLowerCase()));
                }
            });
            for (Criterion crit : sortedCrit) {
//                if(crit.getCpname().contains(FALL_STRING)||crit.getCpname().contains(FALL_NUMERIC)){
//                    continue;
//                }
                TreeItem cItem = new TreeItem<>(crit);
                cItem.setExpanded(pExpandAll);
                gItem.getChildren().add(cItem);
            }
        }

        return item;
    }

    private String getCriterionTooltipText(Criterion.Tooltip tip) {
        //need to differentiate between info tooltip and enum tooltip
        if (tip.getValue() == null) {
            //info tooltip;
            return tip.getCpname().trim();
        }
        if (tip.getCpname() == null) {
            //replace stuff that lang can find entries
            return CriteriaHelper.getTooltipDescription(tip);
        }

        return "";
    }

    private void selectCriterion(Criterion pCriterion) {
        if (pCriterion == null) {
            tvItems.getSelectionModel().select(null);
            tvItems.scrollTo(-1);
            expandTreeView(false);
//            expandTreeView(false);
            return;
        }
        TreeItem item = lookUpItem(tvItems.getRoot(), pCriterion);
        tvItems.getSelectionModel().select(item);
        tvItems.scrollTo(tvItems.getSelectionModel().getSelectedIndex());
//        tvItems.getSelectionModel().sele
        if (item != null) {
            item.setExpanded(true);
        }
    }

    private void expandTreeItem(TreeItem<?> pItem, boolean pExpand) {
        if (tvItems.getRoot() == pItem) {
            LOG.warning("root could not be collapsed, use children instead!");
            return;
        }
        if (pItem != null && !pItem.isLeaf()) {
            pItem.setExpanded(pExpand);
            for (TreeItem<?> child : pItem.getChildren()) {
                expandTreeItem(child, pExpand);
            }
        }
    }

    private void expandTreeView(boolean pExpand) {
        for (Object child : tvItems.getRoot().getChildren()) {
            expandTreeItem((TreeItem<?>) child, pExpand);
        }
    }
    private static final Logger LOG = Logger.getLogger(CriteriaViewSkin.class.getName());

    private TreeItem lookUpItem(TreeItem pItem, Object pValue) {
        if (/*pValue.equals(pItem.getValue())*/isNameEqual(pValue, pItem.getValue())) {
            return pItem;
        }
        if (!pItem.getChildren().isEmpty()) {
            for (Object child : pItem.getChildren()) {
//                if(((TreeItem)child).getValue().equals(pValue)){
//                    return pItem;
//                }
                ((TreeItem) child).getValue();
                TreeItem lookup = lookUpItem((TreeItem) child, pValue);
                if (lookup != null) {
                    return lookup;
                }
            }
        }
        return null;
    }

    private boolean isNameEqual(Object obj, Object obj2) {
        if (obj == null) {
            return false;
        }
        if (obj2 == null) {
            return false;
        }
        if (obj instanceof Criterion && obj2 instanceof Criterion) {
            return ((Criterion) obj).getName().equals(((Criterion) obj2).getName());
        }
        //one is enough for now lol
        return false;
    }
}
