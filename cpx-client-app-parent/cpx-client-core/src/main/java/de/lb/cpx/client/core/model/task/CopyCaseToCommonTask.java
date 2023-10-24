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
package de.lb.cpx.client.core.model.task;

import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextField;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.model.TCase;
import de.lb.cpx.server.rule.services.RuleEditorBeanRemote;
import de.lb.cpx.service.ejb.ProcessServiceBeanRemote;
import de.lb.cpx.shared.lang.Lang;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

/**
 *
 * @author wilde
 */
public class CopyCaseToCommonTask extends TaskService<Integer> {

    private long[] caseIds;
    private BasicCopyCaseDialog dialog;
    private TCase[] cases;

    public CopyCaseToCommonTask(long... pCaseIds) {
        caseIds = Objects.requireNonNull(pCaseIds, "List of cases to copy can not be null!");
    }

    public CopyCaseToCommonTask(TCase... pCases) {
        cases = pCases;
    }

    @Override
    public void start() {
        dialog = getDialog();
        dialog.showAndWait().ifPresent(new Consumer<ButtonType>() {
            @Override
            public void accept(ButtonType t) {
                if (ButtonType.YES.equals(t)) {
                    CopyCaseToCommonTask.super.start();
                }
            }
        });
    }

    @Override
    public Integer call() {
        EjbProxy<ProcessServiceBeanRemote> processServiceBean = Session.instance().getEjbConnector().connectProcessServiceBean();
        if (caseIds != null) {
            return processServiceBean.get().copyTCaseToCommon(caseIds, dialog.getCategoryText());
        }
        if (cases != null) {
            return processServiceBean.get().copyTCaseToCommon(cases, dialog.getCategoryText());
        }
        return null;
    }

    private BasicCopyCaseDialog getDialog() {
        return new CopyCaseDialog();
    }

    private String getInputSize() {
        if (caseIds != null) {
            return String.valueOf(caseIds.length);
        }
        if (cases != null) {
            return String.valueOf(cases.length);
        }
        return "0";
    }

    private class CopyCaseDialog extends BasicCopyCaseDialog {

        public CopyCaseDialog() {
            super("Wollen Sie die ausgewählten Fälle (" + getInputSize() + ") zum Regeleditor kopieren?");
        }

    }

    private class BasicCopyCaseDialog extends AlertDialog {

        private LabeledTextField lblCategory;

        public BasicCopyCaseDialog(String pMsg) {
            super(AlertType.CONFIRMATION, Lang.getConformation(), pMsg, "", ButtonType.YES, ButtonType.NO);
            setOnShowing(new EventHandler<DialogEvent>() {
                @Override
                public void handle(DialogEvent t) {
//                    validationSupport = new ValidationSupport();
                    addContent(createContent());
                }
            });

            getDialogPane().setMinHeight(250);
            initModality(Modality.APPLICATION_MODAL);
            initOwner(MainApp.getWindow());
        }

        private Node createContent() {
            lblCategory = new LabeledTextField("Kategorie", 50);
            lblCategory.getControl().setPromptText("Geben Sie eine Kategorie für die Fallkopien an...");

            AutoCompletionBinding<String> categoryAutoComp = TextFields.bindAutoCompletion(lblCategory.getControl(), (AutoCompletionBinding.ISuggestionRequest param) -> {
                String txt = param.getUserText();
                EjbProxy<RuleEditorBeanRemote> editorBean = Session.instance().getEjbConnector().connectRuleEditorBean();
                Collection<String> listofmatches = editorBean.get().getMatchesAnalyserCaseCategory(txt);//, cbInsuranceTyp.getSelectedItem());
                return listofmatches;
            });
            categoryAutoComp.prefWidthProperty().bind(lblCategory.widthProperty());
            categoryAutoComp.setHideOnEscape(true);
//            LabeledCheckBox lblDuplicates = new LabeledCheckBox("Fälle überschreiben");
//            lblDuplicates.getControl().setText("Fälle überschreiben Ja/Nein");
            VBox box = new VBox(5, lblCategory);//, lblDuplicates);
//            box.getStylesheets().add(this.getClass().getResource("/styles/cpx-default.css").toExternalForm());
            return box;
        }

        private void addContent(Node pNode) {
            if (getDialogPane().getContent() instanceof VBox) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        ((Pane) getDialogPane().getContent()).getChildren().add(pNode);
                    }
                });
            }
        }

        public String getCategoryText() {
            String text = lblCategory.getText();
            if (text == null) {
                //should not happen
                return null;
            }
            if (text.isEmpty()) {
                return null;
            }
            return text;
        }
    }
}
