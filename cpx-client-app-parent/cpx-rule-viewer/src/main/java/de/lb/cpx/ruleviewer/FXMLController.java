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
package de.lb.cpx.ruleviewer;

import de.lb.cpx.client.core.model.fx.ribbon.Ribbon;
import de.lb.cpx.client.core.model.fx.ribbon.item.RibbonItem;
import de.lb.cpx.client.core.model.fx.ribbon.item.RibbonItemGroup;
import de.lb.cpx.rule.element.CaseRuleManager;
import de.lb.cpx.rule.element.model.Rule;
import de.lb.cpx.ruleviewer.editor.RuleEditor;
import de.lb.cpx.ruleviewer.enums.ViewMode;
import de.lb.cpx.ruleviewer.layout.BasicEditorView;
import de.lb.cpx.ruleviewer.layout.RuleView;
import de.lb.cpx.ruleviewer.util.TestRule;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FXMLController implements Initializable {

    @FXML
    private Label label;
    @FXML
    private AnchorPane hbDisplay;
    @FXML
    private Button button;
    @FXML
    private ComboBox<ViewMode> cbViewMode;
    private TestRule ruleObj;
    private Rule ruleXml;
    @FXML
    private AnchorPane hbStructure;

    private static final Logger LOG = Logger.getLogger(FXMLController.class.getName());
    @FXML
    private VBox contentBox;
    @FXML
    private ComboBox<Side> cbSide;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        LOG.log(Level.INFO, "You clicked me!");
        try {
            //        showTestViews();
            showTestDialog();
        } catch (UnsupportedEncodingException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cbViewMode.getItems().addAll(ViewMode.values());
        cbViewMode.getSelectionModel().select(ViewMode.READ_ONLY);

        cbSide.getItems().addAll(Side.values());

        RibbonItemGroup group = new RibbonItemGroup();
        group.setTitle("test");
        group.setTitleSide(Side.RIGHT);
        group.getItems().add(new Button("1"));
        group.getItems().add(new Button("2"));
        group.getItems().add(new Button("3"));
        group.getItems().add(new Button("4"));
        group.getItems().add(new Button("5"));
        group.getItems().add(new Button("6"));
        RibbonItemGroup group2 = new RibbonItemGroup();
        group2.setTitle("test2");
        group2.setTitleSide(Side.RIGHT);
        group2.getItems().add(new Button("1"));
        group2.getItems().add(new Button("2"));
        group2.getItems().add(new Button("3"));
        group2.getItems().add(new Button("4"));
        group2.getItems().add(new Button("5"));
        group2.getItems().add(new Button("6"));
        RibbonItemGroup group3 = new RibbonItemGroup();
        group3.setTitle("test3");
        group3.setTitleSide(Side.RIGHT);
        group3.getItems().add(new Button("1"));
        group3.getItems().add(new VBox(new ComboBox<>(), new ComboBox<>()));
        group3.getItems().add(new Button("3"));

        RibbonItem item = new RibbonItem();
        item.setText("title");
        item.getItems().addAll(group, group2);
        RibbonItem item2 = new RibbonItem();
        item2.setText("title2");
        item2.getItems().addAll(group3);

        Ribbon ribbon = new Ribbon();
        ribbon.getItems().addAll(item, item2);
        contentBox.getChildren().add(0, ribbon);
        cbSide.getSelectionModel().select(ribbon.getSide());
        ribbon.sideProperty().bind(cbSide.getSelectionModel().selectedItemProperty());
    }

    private BasicEditorView getRuleView() {
        RuleView ruleView = new RuleView(null, null);
        return ruleView;
    }

//    private BasicEditorView getSuggestionView() {
//        SuggestionView suggView = new SuggestionView();
//        return suggView;
//    }
//
//    private StructureView getSuggestionStructure() {
//        return new SuggestionStructure();
//    }
    private Rule getRuleXml() {
        if (ruleXml == null) {
            try {
                ruleObj = new TestRule();
                ruleXml = CaseRuleManager.transfromRule(ruleObj.getCrgrDefinition());
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ruleXml;
    }

    private void showTestViews() {
        hbDisplay.getChildren().clear();
        BasicEditorView view = getRuleView();
//        BasicEditorView view = getSuggestionView();
        view.setRule(getRuleXml());
        hbDisplay.getChildren().add(view);
        AnchorPane.setBottomAnchor(view, 0.0);
        AnchorPane.setTopAnchor(view, 0.0);
        AnchorPane.setRightAnchor(view, 0.0);
        AnchorPane.setLeftAnchor(view, 0.0);
        label.setText("Show RuleView!");

//        RuleStructure structure = new RuleStructure();
//        Bindings.bindContent(structure.getControls(), view.getControls());
//
//        Bindings.bindBidirectional(structure.selectedControlProperty(), view.selectedNodeProperty());
//
//        hbStructure.getChildren().add(structure);
//        AnchorPane.setBottomAnchor(structure, 0.0);
//        AnchorPane.setTopAnchor(structure, 0.0);
//        AnchorPane.setRightAnchor(structure, 0.0);
//        AnchorPane.setLeftAnchor(structure, 0.0);
        label.setText("Show RuleView!");

        view.viewModeProperty().bind(cbViewMode.getSelectionModel().selectedItemProperty());
    }

    private void showTestDialog() throws UnsupportedEncodingException {
        Dialog<ButtonType> dialog = new Dialog<>();
        CrgRuleView view = new CrgRuleView();
        view.setRules(new TestRule());
//        dialog.setOnCloseRequest(new EventHandler<>() {
//            @Override
//            public void handle(Event event) {
//                dialog.close();
//            }
//        });
        dialog.initModality(Modality.NONE);
        dialog.initStyle(StageStyle.DECORATED);
        dialog.getDialogPane().setContent(view);
        dialog.setResizable(true);

        dialog.getDialogPane().getButtonTypes().add(new ButtonType("Schließen", ButtonBar.ButtonData.CANCEL_CLOSE));
        dialog.show();
//        dialog.showAndWait();
    }

    @FXML
    private void onOpenEditor(ActionEvent event) throws UnsupportedEncodingException {
        RuleEditor view = new RuleEditor();
        view.setRule(new TestRule());
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initModality(Modality.NONE);
        dialog.initStyle(StageStyle.DECORATED);
        dialog.getDialogPane().setContent(view);
        dialog.setResizable(true);
        dialog.getDialogPane().getButtonTypes().add(new ButtonType("Schließen", ButtonBar.ButtonData.CANCEL_CLOSE));
        ButtonType saveXmlType = new ButtonType("Speichern als XML", ButtonBar.ButtonData.OTHER);
        dialog.getDialogPane().getButtonTypes().add(saveXmlType);
        ((ButtonBase) dialog.getDialogPane().lookupButton(saveXmlType)).setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String xml = view.getRuleXml();
                    LOG.info("XML:\n" + xml);
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
//        dialog.setX((primScreenBounds.getWidth() - dialog.getWidth()) / 2); 
//        dialog.setY((primScreenBounds.getHeight() - dialog.getHeight()) / 4);
//        dialog.setHeight(primScreenBounds.getHeight());
//        dialog.setWidth(primScreenBounds.getWidth());
        dialog.show();
        ((Stage) dialog.getDialogPane().getScene().getWindow()).setMaximized(true);
    }

}
