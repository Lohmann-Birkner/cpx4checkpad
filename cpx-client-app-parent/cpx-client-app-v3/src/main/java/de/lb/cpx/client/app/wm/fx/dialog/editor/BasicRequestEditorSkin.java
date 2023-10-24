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
package de.lb.cpx.client.app.wm.fx.dialog.editor;

import de.lb.cpx.client.app.wm.util.texttemplate.TextTemplateController;
import de.lb.cpx.client.core.model.fx.labeled.LabeledCheckComboBox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextArea;
import de.lb.cpx.client.core.model.fx.popover.AutoFitPopOver;
import de.lb.cpx.model.enums.TextTemplateTypeEn;
import de.lb.cpx.wm.model.TWmMdkAuditReasons;
import de.lb.cpx.wm.model.TWmRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SkinBase;
import javafx.scene.control.skin.TextAreaSkin;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;
import org.controlsfx.validation.ValidationSupport;

/**
 *
 * @author wilde
 * @param <T> type of request editor
 * @param <Z> type of entity
 */
public abstract class BasicRequestEditorSkin<T extends BasicRequestEditor<Z>, Z extends TWmRequest> extends SkinBase<T> {

    private static final Logger LOG = Logger.getLogger(BasicRequestEditorSkin.class.getName());
    protected static final String FONT_WEIGHT_NORMAL = "-fx-font-weight: normal";
    protected static final String FONT_WEIGHT_BOLD = "-fx-font-weight: bold";
    private final String rootUrl;
    private Parent root;
    private TextTemplateController textTemplateController;

    public BasicRequestEditorSkin(T pSkinnable, String pRootUrl) throws IOException {
        super(pSkinnable);
        rootUrl = pRootUrl;
        loadRoot();
        setUpNodes();
        setUpLanguage();
        getChildren().add(root);

        getSkinnable().validationSupportProperty().addListener(new ChangeListener<ValidationSupport>() {
            @Override
            public void changed(ObservableValue<? extends ValidationSupport> observable, ValidationSupport oldValue, ValidationSupport newValue) {
                setUpValidation();
            }
        });
        pSkinnable.getProperties().addListener(new MapChangeListener<Object, Object>() {
            @Override
            public void onChanged(MapChangeListener.Change<? extends Object, ? extends Object> change) {
                if (change.wasAdded()) {
                    if (InsuranceRequestEditor.UPDATE_REQUEST_DATA.equals(change.getKey())) {
                        updateRequestData();
                        pSkinnable.getProperties().remove(InsuranceRequestEditor.UPDATE_REQUEST_DATA);
                    }
                }
            }
        });
        pSkinnable.getProperties().addListener(new MapChangeListener<Object, Object>() {
            @Override
            public void onChanged(MapChangeListener.Change<? extends Object, ? extends Object> change) {
                if (change.wasAdded()) {
                    if (MdkRequestEditor.UPDATE_REQUEST_DATA.equals(change.getKey())) {
                        updateRequestData();
                        pSkinnable.getProperties().remove(MdkRequestEditor.UPDATE_REQUEST_DATA);
                    }
                }
            }
        });
        pSkinnable.getProperties().addListener(new MapChangeListener<Object, Object>() {
            @Override
            public void onChanged(MapChangeListener.Change<? extends Object, ? extends Object> change) {
                if (change.wasAdded()) {
                    if (ReviewRequestEditor.UPDATE_REQUEST_DATA.equals(change.getKey())) {
                        updateRequestData();
                        pSkinnable.getProperties().remove(ReviewRequestEditor.UPDATE_REQUEST_DATA);
                    }
                }
            }
        });
        initValidation();
//        setUpValidation();
        setUpAutoCompletion();
        setUpdateListeners();
        setDisableControlls();
//        if(getSkinnable().getRequest().getId() != 0L){
//            updateRequestValues(getSkinnable().getRequest());
//            updateCatalogValues(getSkinnable().getInsuranceCatalog().getByCode(getSkinnable().getRequest().getInsuranceIdentifier(), AbstractCpxCatalog.DEFAULT_COUNTRY));
//        }
    }

    protected abstract void setUpNodes();

    protected abstract void setUpLanguage();

    protected abstract void setUpValidation();

    protected abstract void setUpAutoCompletion();

    protected abstract void updateRequestValues(Z pRequest);

    protected abstract void disableControls(boolean armed);

    protected abstract void updateRequestData();
    
    private void createTextTemplateController(){
        if(getSkinnable().getProcessServiceFacade() != null && getSkinnable().getCase() != null){
            textTemplateController = new TextTemplateController(TextTemplateTypeEn.RequestContext, getSkinnable().getProcessServiceFacade(), getWindow(), getSkinnable().getCase());
        }
        
    }
    
    protected void addTextTemplateController(LabeledTextArea pTextArea){
        if(textTemplateController == null){
            createTextTemplateController();
        }
        if(textTemplateController != null){
            TextAreaSkin customContextSkinMC = textTemplateController.customContextSkin(pTextArea.getControl());
            pTextArea.getControl().setSkin(customContextSkinMC);
        }
        
    }

    public void setDisableControlls() {
        disableControls(!getSkinnable().isEditable());
    }

    public void setUpdateListeners() {
        if (getSkinnable().getRequest() != null) {
            if (getSkinnable().getRequest().getId() != 0L) {
                updateRequestValues(getSkinnable().getRequest());
            }
        }
        getSkinnable().requestProperty().addListener(new ChangeListener<Z>() {
            @Override
            public void changed(ObservableValue<? extends Z> observable, Z oldValue, Z newValue) {
                if (newValue == null) {
                    return;
                }
                updateRequestValues(newValue);
            }
        });
    }

    public PopOver showInfoPopover(Node pNode) {
        VBox vBox = new VBox(5.0d);
        vBox.getChildren().addAll(pNode);
        vBox.setPadding(new Insets(12.0, 12.0, 12.0, 12.0));
        PopOver popover = new AutoFitPopOver();
        popover.setHideOnEscape(true);
        popover.setAutoHide(true);
        popover.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
        popover.setDetachable(false);
        popover.setContentNode(vBox);
        popover.getContentNode().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    popover.hide(Duration.ZERO);
                }
            }
        });
        return popover;
    }

    public Node lookUpInRoot(String pSelector) {
        return root.lookup(pSelector);
    }

    private void loadRoot() throws IOException {
        root = FXMLLoader.load(getClass().getResource(rootUrl));
    }

    private void initValidation() {
        //should newver happen except someone screwed up
        if (getSkinnable().getValidationSupport() == null) {
            return;
        }
        Platform.runLater(() -> {
            setUpValidation();
        });

    }

    public Window getWindow() {
        final Scene scene = getSkinnable().getScene();
        if (scene == null) {
            return null;
        } else {
            return scene.getWindow();
        }
    }
    
    protected void updateAuditReasons(LabeledCheckComboBox<Map.Entry<Long, String>> pCheckBox, TWmRequest pRequest) {
        //request.getAuditReasons().clear();
        //ObservableList<Map.Entry<Long, String>> checked = ckcbAuditReasons.getCheckModel().getCheckedItems();
        List<Map.Entry<Long, String>> items = pCheckBox.getItems();
        for (Map.Entry<Long, String> item : items) {
            final Long key = item.getKey();
            final boolean checked = pCheckBox.getCheckModel().isChecked(item);
            final boolean contains = pRequest.containsAuditReason(key);
            if (checked) {
                if (!contains) {
                    LOG.log(Level.INFO, "add new main audit reason number {0} to request", key);
                    pRequest.addAuditReason(key, false);
                }
            } else {
                if (contains) {
                    LOG.log(Level.INFO, "remove main audit reason number {0} from request", key);
                    pRequest.removeAuditReason(key);
                }
            }
        }
    }

    protected void updateAuditReasonsExtended(LabeledCheckComboBox<Map.Entry<Long, String>> pCheckBox, TWmRequest request) {
        //request.getAuditReasons(true).clear();
        //ObservableList<Map.Entry<Long, String>> extendedChecked = ckcbExtendedAuditReasons.getCheckModel().getCheckedItems();
        List<Map.Entry<Long, String>> extendedItems = pCheckBox.getItems();
        for (Map.Entry<Long, String> item : extendedItems) {
            final Long key = item.getKey();
            final boolean checked = pCheckBox.getCheckModel().isChecked(item);
            final boolean contains = request.containsAuditReasonExtended(key);
            if (checked) {
                if (!contains) {
                    LOG.log(Level.INFO, "add new extended audit reason number {0} to request", key);
                    request.addAuditReason(key, true);
                }
            } else {
                if (contains) {
                    LOG.log(Level.INFO, "remove extended audit reason number {0} from request", key);
                    request.removeAuditReasonExtended(key);
                }
            }
        }
    }

    protected void updateAuditReasonsData(LabeledCheckComboBox<Map.Entry<Long, String>> pCheckBox, TWmRequest request) {
        nextVal:for (TWmMdkAuditReasons reason : request.getAuditReasons(false)) {
            for (Map.Entry<Long, String> item : pCheckBox.getItems()) {
                if (item.getKey().equals(reason.getAuditReasonNumber())) {
                    pCheckBox.getCheckModel().check(item);
                    continue nextVal;
                }
            }
            LOG.warning("Can not find entry for AuditReasonNumber: " + reason.getAuditReasonNumber() + "!\nWas the CaseType changed or was it deleted?");
        }
    }
    
    protected void updateAuditReasonsExtendedData(LabeledCheckComboBox<Map.Entry<Long, String>> pCheckBox, TWmRequest request) {
        nextVal:for (TWmMdkAuditReasons extendedReason : request.getAuditReasons(true)) {
            for (Map.Entry<Long, String> item : pCheckBox.getItems()) {
                if (item.getKey().equals(extendedReason.getAuditReasonNumber())) {
                    pCheckBox.getCheckModel().check(item);
                    continue nextVal;
                }
            }
            LOG.warning("Can not find entry for AuditReasonNumber: " + extendedReason.getAuditReasonNumber() + "!\nWas the CaseType changed or was it deleted?");
        }
    }

    protected void checkMdFields(long mdkInternalId) {
     
    }
    
}
