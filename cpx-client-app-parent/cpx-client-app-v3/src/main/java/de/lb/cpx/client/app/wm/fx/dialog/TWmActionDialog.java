/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.dialog;

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.dialog.editor.CatalogValidationResult;
import de.lb.cpx.client.app.wm.util.texttemplate.TextTemplateController;
import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.menu.cache.MenuCacheOptionsEn;
import de.lb.cpx.client.core.model.enums.CpxErrorTypeEn;
import de.lb.cpx.client.core.model.fx.dialog.FormularDialog;
import de.lb.cpx.client.core.model.fx.labeled.LabeledComboBox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextArea;
import de.lb.cpx.model.enums.TextTemplateTypeEn;
import de.lb.cpx.server.commonDB.model.CWmListActionSubject;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmAction;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.Control;
import javafx.scene.control.ListCell;
import javafx.scene.control.skin.TextAreaSkin;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.Validator;

/**
 *
 * @author wilde
 */
public abstract class TWmActionDialog extends FormularDialog<TWmAction> {

    protected LabeledComboBox<CWmListActionSubject> cbActionType;
    protected LabeledTextArea taComment;
    private final ProcessServiceFacade facade;
    private final CatalogValidationResult catalogValidationResult;

    public TWmActionDialog(String pTitle, @NotNull ProcessServiceFacade pFacade) {
        super(pTitle, MainApp.getStage());
        Objects.requireNonNull(pFacade, "Facade can not be null");
        facade = pFacade;
        setHeight(600D);
        createLayout(pFacade);
        actionEntityProperty().addListener(new ChangeListener<TWmAction>() {
            @Override
            public void changed(ObservableValue<? extends TWmAction> observable, TWmAction oldValue, TWmAction newValue) {
                if (newValue == null) {
                    setActionType(null);
                    setComment("");
                    return;
                }
                setActionType(newValue.getActionType());
                setComment(newValue.getComment() != null ? String.valueOf(newValue.getComment()) : "");
            }
        });
        catalogValidationResult = new CatalogValidationResult();
        catalogValidationResult.add(CpxErrorTypeEn.WARNING, Lang.getValidationMasterdataNoActionTypesFound(), (t) -> {
            return MenuCache.getMenuCacheActionSubjects().values(MenuCacheOptionsEn.IGNORE_INACTIVE).isEmpty();
        });
        setMessageText(catalogValidationResult.getValidationMessages());
        setMessageType(catalogValidationResult.getHighestErrorType());
    }
    public CatalogValidationResult getCatalogValidationResult(){
        return catalogValidationResult;
    }
    private void createLayout(ProcessServiceFacade pFacade) {

        cbActionType = new LabeledComboBox<>("Aktionstyp");
        cbActionType.setMaxWidth(Double.MAX_VALUE);
        List<CWmListActionSubject> subjects = MenuCache.instance().getActionSubjects(new Date());
        cbActionType.setItems(FXCollections.observableArrayList(subjects != null ? subjects : new ArrayList<>()));
        cbActionType.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(CWmListActionSubject item, boolean empty) {
                super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                if (item == null || empty) {
                    setText("");
                    return;
                }
                setText(item.getWmAsName());
            }
        });
        cbActionType.setConverter(new StringConverter<CWmListActionSubject>() {
            @Override
            public String toString(CWmListActionSubject object) {
                return object == null ? "" : object.getWmAsName();
            }

            @Override
            public CWmListActionSubject fromString(String string) {
                return null;
            }
        });

//        if (action.getActionType() != null) {
//            CWmListActionSubject actionSubjectObject = processServiceBean.get().getActionSubjectByInternalId(action.getActionType());
//            cbActionType.getSelectionModel().select(actionSubjectObject);
//        }
        cbActionType.getControl().getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends CWmListActionSubject> observable, CWmListActionSubject oldValue, CWmListActionSubject newValue) -> {
            TWmAction action = getActionEntity();
            if (newValue != null) {
                action.setActionType(newValue.getWmAsInternalId());
            } else {
                de.lb.cpx.client.app.MainApp.showErrorMessageDialog("Der Aktionstyp darf nicht entfernt werden");
                cbActionType.getControl().getSelectionModel().select(oldValue);
            }
//            historyListView.refresh();
//            facade.updateAction(action);
        });
        getValidation().registerValidator(cbActionType.getControl(), new Validator<Object>() {
            @Override
            public ValidationResult apply(Control t, Object u) {
                ValidationResult result = new ValidationResult();
                result.addErrorIf(t, "Geben Sie einen Aktionstyp an!", cbActionType.getControl().getSelectionModel().getSelectedItem() == null);
                return result;
            }

        });

        taComment = new LabeledTextArea(Lang.getComment(), 6000);
//        taComment.setText(action.getComment() != null ? String.valueOf(action.getComment()) : "");
        taComment.setEditable(true); //2017-04-26 DNi - CPX-489: Changed to true
        taComment.setWrapText(true);
        VBox.setVgrow(taComment, Priority.ALWAYS);
        taComment.getControl().focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                return;
            }
            TWmAction action = getActionEntity();
            if (action.getComment() != null && StringUtils.trimToEmpty(taComment.getText()).equals(
                    StringUtils.trimToEmpty(new String(action.getComment())))) {
                return;
            }
            String newComment = StringUtils.trimToEmpty(taComment.getText());
            String oldComment = String.valueOf(action.getComment() != null ? action.getComment() : "");
            if (!oldComment.equals(newComment)) {
                action.setComment(newComment.toCharArray());
            }
        });

        //add custom MenuItem to the ContextMenu
        TextAreaSkin customContextSkin = new TextTemplateController(TextTemplateTypeEn.ActionContext, pFacade, getWindow()).customContextSkin(taComment.getControl());
        taComment.getControl().setSkin(customContextSkin);
        addControls();
    }
    
    protected void addControls(){
        addControls(cbActionType, taComment);
    }

    protected final ProcessServiceFacade getFacade() {
        return facade;
    }

    protected final void setComment(String pComment) {
        taComment.setText(pComment);
    }

    public final String getComment() {
        return taComment.getText();
    }

    protected final void setActionType(long pType) {
        setActionType(MenuCache.instance().getActionSubjectForId(pType));
    }

    public final CWmListActionSubject getActionType() {
        return cbActionType.getControl().getSelectionModel().getSelectedItem();
    }

    protected final void setActionType(CWmListActionSubject pSubject) {
        cbActionType.getControl().getSelectionModel().select(pSubject);
    }
    private ObjectProperty<TWmAction> actionEntityProperty;

    public final ObjectProperty<TWmAction> actionEntityProperty() {
        if (actionEntityProperty == null) {
            actionEntityProperty = new SimpleObjectProperty<>(new TWmAction());
        }
        return actionEntityProperty;
    }

    public final TWmAction getActionEntity() {
        return actionEntityProperty().get();
    }

    public final void setActionEntity(TWmAction pAction) {
        actionEntityProperty().set(pAction);
    }

//    private String castDate(Date pDate){
//        if(pDate == null){
//            return "Unlimitiert";
//        }
//        return Lang.toDate(pDate);
//    }
}
