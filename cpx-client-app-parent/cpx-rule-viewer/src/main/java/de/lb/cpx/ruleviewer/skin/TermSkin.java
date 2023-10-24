/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.skin;

import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.model.fx.dialog.event.MinimizeWindowEvent;
import de.lb.cpx.client.core.model.fx.tooltip.BasicTooltip;
import de.lb.cpx.client.core.model.fx.tooltip.CpxTooltip;
import de.lb.cpx.client.core.util.OverrunHelper;
import de.lb.cpx.rule.criteria.CriteriaHelper;
import de.lb.cpx.rule.criteria.TypesAndOperationsManager;
import de.lb.cpx.rule.criteria.model.CriterionTree;
import de.lb.cpx.rule.criteria.model.TypesAndOperations.OperationGroups.OperationGroup.Operation;
import de.lb.cpx.ruleviewer.cache.RuleMetaDataCache;
import de.lb.cpx.ruleviewer.enums.ViewMode;
import de.lb.cpx.ruleviewer.layout.RuleView;
import de.lb.cpx.ruleviewer.model.RuleMessageIndicator;
import de.lb.cpx.ruleviewer.model.SelectableControl;
import de.lb.cpx.ruleviewer.model.Term;
import de.lb.cpx.ruleviewer.model.ruletable.dialog.TableInfoDialog;
import de.lb.cpx.ruleviewer.util.TooltipHelper;
import de.lb.cpx.shared.json.RuleMessage;
import de.lb.cpx.shared.lang.Lang;
import java.io.IOException;
import java.util.Objects;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * TODO: do selectable control skin
 *
 * @author wilde
 */
public class TermSkin extends SelectableControlSkin<Term> {

//    private static PseudoClass SELECTED_NODE_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected_node");
//    private Parent root;
    private Label lblFirstCondition;
//    private Label lblSecondCondition;
    private Label lblOperatorCondition;
    private HBox boxOptional;
    private Label lblInverted;
    private HBox boxTerm;
    private Hyperlink hlSecondCondition;
    private Label lblInterval;
    private VBox rootBox;
    private Label lblBrackedOpen;
    private Label lblBrackedClose;
    private HBox boxFirstLine;
    private RuleMessageIndicator messageIndicator;

    public TermSkin(final Term pSkinnable) throws IOException {
        super(pSkinnable);
        pSkinnable.getProperties().addListener(new MapChangeListener<Object, Object>() {
            @Override
            public void onChanged(MapChangeListener.Change<? extends Object, ? extends Object> change) {
                if (change.wasAdded()) {
                    if (SelectableControl.REFRESH_VALUE.equals(change.getKey())) {
                        updateSecondCondition(getSkinnable().getSecondCondition());
                        pSkinnable.getProperties().remove(SelectableControl.REFRESH_VALUE);
                    }
                }
            }
        });
        pSkinnable.firstConditionProperty().addListener(new ChangeListener<CriterionTree.Supergroup.Group.Criterion>() {
            @Override
            public void changed(ObservableValue<? extends CriterionTree.Supergroup.Group.Criterion> observable, CriterionTree.Supergroup.Group.Criterion oldValue, CriterionTree.Supergroup.Group.Criterion newValue) {
                updateCondition(newValue);
            }

        });
        updateCondition(pSkinnable.getFirstCondition());
        pSkinnable.secondConditionProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updateSecondCondition(newValue);
            }

        });
        updateSecondCondition(getSkinnable().getSecondCondition());
        pSkinnable.operatorProperty().addListener(new ChangeListener<Operation>() {
            @Override
            public void changed(ObservableValue<? extends Operation> observable, Operation oldValue, Operation newValue) {
                updateOperator(newValue);
            }
        });
        updateOperator(pSkinnable.getOperator());
        lblInverted.textProperty().bind(Bindings.when(pSkinnable.invertedProperty()).then("not").otherwise(""));
        pSkinnable.invertedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                updateInverted(t1);
            }
        });
        updateInverted(pSkinnable.isInverted());
        lblFirstCondition.fontProperty().bind(getSkinnable().fontProperty());
        lblOperatorCondition.fontProperty().bind(getSkinnable().fontProperty());
        hlSecondCondition.fontProperty().bind(getSkinnable().fontProperty());
        lblInverted.fontProperty().bind(getSkinnable().fontProperty());
        lblInterval.fontProperty().bind(getSkinnable().fontProperty());

        pSkinnable.intervalFromProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updateInterval();
            }
        });
        pSkinnable.intervalToProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updateInterval();
            }
        });
        updateInterval();
        pSkinnable.messageProperty().addListener(new ChangeListener<RuleMessage>() {
            @Override
            public void changed(ObservableValue<? extends RuleMessage> ov, RuleMessage t, RuleMessage t1) {
                updateMessageIndicator();
            }

        });
        updateMessageIndicator();
        lblInverted.paddingProperty().bind(Bindings.when(pSkinnable.invertedProperty()).then(new Insets(0, 5, 0, 0)).otherwise(Insets.EMPTY));
        OverrunHelper.addInfoTooltip(hlSecondCondition);
    }
    private void updateMessageIndicator() {
        if(messageIndicator == null){
            messageIndicator = new RuleMessageIndicator();
            messageIndicator.setPadding(new Insets(0, 8, 0, 0));
        }
        if(getSkinnable().getMessage() == null || (getSkinnable().getMessage().getDescription() == null || getSkinnable().getMessage().getDescription().isEmpty())){
            boxFirstLine.getChildren().remove(messageIndicator);
            Tooltip.install(messageIndicator, null);
        }else{
            boxFirstLine.getChildren().add(0,messageIndicator);
            Tooltip.install(messageIndicator, new CpxTooltip(getSkinnable().getMessage().getDescription(),100,5000,100,true));
        }
    }
    private void updateInverted(Boolean pInverted) {
        if (pInverted) {
            if (!boxFirstLine.getChildren().contains(lblBrackedClose)) {
                boxFirstLine.getChildren().add(lblBrackedClose);
            }
            if (!boxFirstLine.getChildren().contains(lblBrackedOpen)) {
                boxFirstLine.getChildren().add(1, lblBrackedOpen);
            }
        } else {
            boxFirstLine.getChildren().remove(lblBrackedClose);
            boxFirstLine.getChildren().remove(lblBrackedOpen);
        }
    }

    private void updateSecondCondition(String pNewText) {

        if (getSkinnable().isRuleTable()) {
            hlSecondCondition.setText(RuleMetaDataCache.instance().getTableNameInPool(RuleView.getFacade().getPool(), pNewText));
        } else {
            pNewText = Objects.requireNonNullElse(pNewText, "");
            pNewText = pNewText.replace("%", "*");
            if (getSkinnable().isDouble()) {
                pNewText = pNewText.replace(".", ",");
            }
            hlSecondCondition.setText(pNewText);
        }

        hlSecondCondition.setDisable(false);
        if (pNewText == null || pNewText.isEmpty()) {
            hlSecondCondition.setDisable(true);
            return;
        }
        if (getSkinnable().isUnknown()) {
            hlSecondCondition.setDisable(true);
            return;
        }
        //disable hyperlink 
        if (getSkinnable().isOps()) {
            hlSecondCondition.setDisable(true);
        }
        if (getSkinnable().isIcd()) {
            hlSecondCondition.setDisable(true);
        }
        if (ViewMode.READ_WRITE.equals(getSkinnable().getViewMode())) {
            hlSecondCondition.setDisable(true);
            return;
        }
        hlSecondCondition.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (getSkinnable().isRuleTable()) {
                    TableInfoDialog dialog = new TableInfoDialog(hlSecondCondition.getText(), getSkinnable().getScene().getWindow());
                    dialog.showAndWait();
                    return;
                }
                String code = hlSecondCondition.getText().replace("'", "").replace(" ", "");
                String[] split = code.split("\\.");
                code = code.replace("%", "");
                if (getSkinnable().isIcd()) {
                    if (code.contains(",")) {
                        code = code.replace(",", "%20");
                    }
                    //call web site to access icd info
                    //TODO: Make Catalog view for this data, when available
                    if (split.length > 1) {
                        String url = "http://www.icd-code.de/suche/icd/code/" + split[0] + ".-.html?sp=S" + code;
                        BasicMainApp.openUrl(url);
                    } else {
                        String url = "http://www.icd-code.de/suche/icd/code/" + split[0].replace("%", ".-") + ".html?sp=S" + code;
                        BasicMainApp.openUrl(url);
                    }
                    Event.fireEvent(getSkinnable(), new MinimizeWindowEvent(getSkinnable(), MinimizeWindowEvent.WINDOW_MINIMIZE));
                }
                if (getSkinnable().isOps()) {
                    if (code.contains(",")) {
                        code = code.replace(",", "%20");
                    }
                    //call web site to access ops info
                    //TODO: Make Catalog view for this data, when available
                    String url = "http://www.icd-code.de/suche/ops/code/" + split[0].replace("%", "") + ".html?sp=S" + code;
                    BasicMainApp.openUrl(url);
                    Event.fireEvent(getSkinnable(), new MinimizeWindowEvent(getSkinnable(), MinimizeWindowEvent.WINDOW_MINIMIZE));
                }
            }
        });

    }

    private void updateCondition(CriterionTree.Supergroup.Group.Criterion pCondition) {
        if (pCondition != null) {
            lblFirstCondition.setText(getSkinnable().getCriteriaTranslation(pCondition.getCpname()));//Lang.get(pCondition.getCpname()).getValue());
            if (lblFirstCondition.getText().isEmpty()) {
                boxTerm.setSpacing(0.0);
                boxOptional.setSpacing(0.0);
                lblOperatorCondition.setMinWidth(0);
                lblOperatorCondition.setMaxWidth(0);
            } else {
                boxTerm.setSpacing(5.0);
                boxOptional.setSpacing(5.0);
                lblOperatorCondition.setMinWidth(Label.USE_COMPUTED_SIZE);
                lblOperatorCondition.setMaxWidth(Label.USE_COMPUTED_SIZE);
            }
            BasicTooltip tip = new BasicTooltip(
                    CriteriaHelper.getDisplayName(pCondition)
                            + ":",
                    TooltipHelper.getCriteriaTooltipText(pCondition.getTooltip()));
            tip.setPrefWidth(200);
            lblFirstCondition.setTooltip(tip);
            return;
        }
        lblFirstCondition.setText("----");
        lblFirstCondition.setTooltip(null);
    }

    private void updateOperator(Operation pOperator) {
        if (pOperator != null) {
            lblOperatorCondition.setText(pOperator.toString());
            return;
        }
        lblOperatorCondition.setText(" ");
    }

    private void updateInterval() {
        if (getSkinnable().hasInterval()) {
            lblInterval.setVisible(true);
            if (!rootBox.getChildren().contains(lblInterval)) {
                rootBox.getChildren().add(lblInterval);
            }
            lblInterval.setText("(Zeitinterval:von " + getIntervalTranslation(getSkinnable().getIntervalFrom()) + " bis " + getIntervalTranslation(getSkinnable().getIntervalTo()) + ")");
        } else {
            lblInterval.setVisible(false);
            rootBox.getChildren().remove(lblInterval);
            lblInterval.setText("");
        }
    }

    private String getIntervalTranslation(String pInterval) {
        if (pInterval == null || pInterval.isEmpty()) {
            return "";
        }
        String[] split = pInterval.split(":");
        return pInterval.replace(split[0], Lang.get(TypesAndOperationsManager.instance().getInterval(split[0]).getDisplayName().replace("rules", "Rules")).getValue());
    }

    @Override
    protected Pane loadRoot() throws IOException {
        rootBox = FXMLLoader.load(getClass().getResource("/fxml/Term.fxml"));
        lblFirstCondition = (Label) rootBox.lookup("#lblFirstCondition");
//        lblSecondCondition = (Label) root.lookup("#lblSecondCondition");
        hlSecondCondition = (Hyperlink) rootBox.lookup("#hlSecondCondition");
        lblOperatorCondition = (Label) rootBox.lookup("#lblOperator");
        lblInverted = (Label) rootBox.lookup("#lblInverted");
        boxOptional = (HBox) rootBox.lookup("#optionalData");
        boxTerm = (HBox) rootBox.lookup("#boxTerm");
        lblInterval = (Label) rootBox.lookup("#lblInterval");
        boxFirstLine = (HBox) rootBox.lookup("#boxFirstLine");
        lblBrackedOpen = (Label) rootBox.lookup("#lblBrackedOpen");
        lblBrackedClose = (Label) rootBox.lookup("#lblBrackedClose");
        boxFirstLine.getChildren().removeAll(lblBrackedOpen, lblBrackedClose);
        rootBox.getChildren().remove(lblInterval);
        return rootBox;
    }
    
}
