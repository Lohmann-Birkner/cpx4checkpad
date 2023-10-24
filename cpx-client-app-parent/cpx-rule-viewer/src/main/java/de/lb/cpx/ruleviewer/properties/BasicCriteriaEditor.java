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
package de.lb.cpx.ruleviewer.properties;

import de.lb.cpx.client.core.model.fx.checked_combobox.CpxComboBox;
import de.lb.cpx.client.core.model.fx.checked_combobox.CpxComboBoxListViewSkin;
import de.lb.cpx.client.core.model.fx.popover.AutoFitPopOver;
import de.lb.cpx.client.core.model.fx.tooltip.CpxTooltip;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.rule.criteria.CriteriaHelper;
import de.lb.cpx.rule.criteria.model.Criteria;
import de.lb.cpx.rule.criteria.model.CriterionTree;
import de.lb.cpx.rule.criteria.model.CriterionTree.Supergroup.Group.Criterion;
import de.lb.cpx.ruleviewer.layout.CriteriaView;
import de.lb.cpx.ruleviewer.model.RuleMessageIndicator;
import de.lb.cpx.ruleviewer.model.Term;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.property.BeanProperty;
import org.controlsfx.property.editor.PropertyEditor;

/**
 * Basic Criteria editor to set criterias in Object
 *
 * @author wilde
 */
public abstract class BasicCriteriaEditor implements PropertyEditor<CriterionTree.Supergroup.Group.Criterion> {

    private static final Logger LOG = Logger.getLogger(BasicCriteriaEditor.class.getName());
    
    private final PropertySheet.Item item;
    private ComboBox<CriterionTree.Supergroup.Group.Criterion> cbCriteria;
    private Button btnMenu;
    private HBox box;
    private AutoFitPopOver popover;
    private Term term;
    private final RuleMessageIndicator messageIndicator;

    public BasicCriteriaEditor(PropertySheet.Item property) {
        item = property;
        messageIndicator = new RuleMessageIndicator();
        messageIndicator.setPadding(new Insets(0, 8, 0, 0));
//        setEditorStyle();
    }

    public abstract List<Criteria> getCriteriaList();

    public List<Criterion> getCriterions() {
        List<Criterion> critList = new ArrayList<>();
        for (Criteria criteria : getCriteriaList()) {
            critList.addAll(criteria.criterionMap().values());
        }
        critList.sort(new Comparator<Criterion>() {
            @Override
            public int compare(Criterion o1, Criterion o2) {
                return (getDisplayName(o1).toLowerCase().compareTo(getDisplayName(o2).toLowerCase()));
            }
        });
        return critList;
    }

    @Override
    public final Node getEditor() {
        if (box == null) {
            cbCriteria = new CpxComboBox<>();
            CpxComboBoxListViewSkin<Criterion> sk = new CpxComboBoxListViewSkin<>(cbCriteria);
            sk.setHideOnClick(true);
            cbCriteria.setSkin(sk);
//            cbCriteria.setButtonCell(new ListCell<>(){
//                @Override
//                protected void updateItem(Criterion item, boolean empty) {
//                    super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
//                    if(item == null || empty){
//                        setText("");
//                        return;
//                    }
//                    setText(cbCriteria.getConverter().toString(item));
//                }
//            });
            cbCriteria.setItems(FXCollections.observableArrayList(getCriterions()));
            cbCriteria.setConverter(new StringConverter<Criterion>() {
                @Override
                public String toString(Criterion t) {
                    return t != null ? getDisplayName(t) : "";
                }

                @Override
                public Criterion fromString(String string) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
            cbCriteria.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CriterionTree.Supergroup.Group.Criterion>() {
                @Override
                public void changed(ObservableValue<? extends CriterionTree.Supergroup.Group.Criterion> observable, CriterionTree.Supergroup.Group.Criterion oldValue, CriterionTree.Supergroup.Group.Criterion newValue) {
                    item.setValue(getValue());
                    cbCriteria.getItems().contains(newValue);
                    sk.getListView().scrollTo(newValue);
                }
            });
            btnMenu = new Button();
            btnMenu.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    if (popover == null) {
                        CriteriaView content = new CriteriaView();
                        content.setFocusTraversable(true);
                        content.setOnSelectedCallback(new Callback<Criterion, Void>() {
                            @Override
                            public Void call(Criterion param) {
                                if (param != null) {
                                    cbCriteria.getSelectionModel().select(param);
                                }
                                return null;
                            }
                        });
                        content.getItems().addAll(getCriteriaList());
                        content.setPrefWidth(600);
                        popover = new AutoFitPopOver(content);
                        popover.setFitOrientation(Orientation.HORIZONTAL);
                        popover.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                            @Override
                            public void handle(KeyEvent event) {
                                if (KeyCode.ESCAPE.equals(event.getCode())) {
                                    popover.hide();
                                }
                            }
                        });
                        popover.setDetachable(false);
                        popover.setAutoHide(true);
                        popover.setArrowLocation(popover.getAdjustedLocation(PopOver.ArrowLocation.RIGHT_CENTER));
                        popover.setHideOnEscape(true);
                        popover.setFadeOutDuration(Duration.ZERO);
                        popover.setOnHiding(new EventHandler<WindowEvent>() {
                            @Override
                            public void handle(WindowEvent event) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        cbCriteria.requestFocus();
                                    }
                                });
                            }
                        });
                    }
                    if (!popover.isShowing()) {
                        ((CriteriaView) popover.getContentNode()).selectCriterion(cbCriteria.getSelectionModel().getSelectedItem());
                        popover.show(cbCriteria);
                    }
                }
            });

            btnMenu.getStyleClass().add("cpx-icon-button");
            btnMenu.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.WRENCH));
            HBox.setHgrow(cbCriteria, Priority.ALWAYS);
            box = new HBox(cbCriteria, btnMenu);
            cbCriteria.maxWidthProperty().bind(box.widthProperty().multiply(0.75));
        }
        return box;
    }

    private String getDisplayName(Criterion pCriterion) {
        return CriteriaHelper.getDisplayName(pCriterion);
    }

    @Override
    public CriterionTree.Supergroup.Group.Criterion getValue() {
        if (cbCriteria == null) {
            return null;
        }
        return cbCriteria.getSelectionModel().getSelectedItem();
    }

    @Override
    public void setValue(CriterionTree.Supergroup.Group.Criterion value) {
        getEditor();
//        ComboBox<CriterionTree.Supergroup.Group.Criterion> cb = (ComboBox<CriterionTree.Supergroup.Group.Criterion>) ((Pane) getEditor()).getChildren().get(0);
        cbCriteria.getSelectionModel().select(value);
    }
    
    public Term getTerm() {
        if (term == null) {
            Object bean = ((BeanProperty) item).getBean();
            if (!(bean instanceof Term)) {
                LOG.severe("bean of the editor is not a Term!");
                return null;
            }
            term = (Term) bean;
        }
        return term;
    }

    private void setEditorStyle() {
        Term t = getTerm();
        getEditor();
        if(t == null || t.getMessage() == null){
            cbCriteria.getButtonCell().setStyle("-fx-text-fill:black;");
            box.getChildren().remove(messageIndicator);
            Tooltip.install(messageIndicator, null);
            return;
        }
        cbCriteria.getButtonCell().setStyle("-fx-text-fill:red;");
        if(!box.getChildren().contains(messageIndicator)){
           box.getChildren().add(0, messageIndicator);
        }
        Tooltip.install(messageIndicator, new CpxTooltip(t.getMessage().getDescription(),100, 5000, 100, true));
    }
    
}
