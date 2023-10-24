/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.core.model.fx.labeled;

import de.lb.cpx.client.core.model.catalog.AbstractCpxNonAnnualCatalog;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextField;
import de.lb.cpx.client.core.model.fx.popover.AutoFitPopOver;
import de.lb.cpx.client.core.util.ResourceLoader;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.validation.ValidationSupport;

/**
 *
 * @author gerschmann
 */
public  class LabeledInfoTextField extends LabeledTextField{
    protected Button searchButton;

    public LabeledInfoTextField(){
        super();
        addSearchButton();
    }
    /**
     * creates a new textField with that label
     *
     * @param pLabel textArea
     * @param maxSize maximum size (maximum amount of characters)
     */
    public LabeledInfoTextField(String pLabel, int maxSize) {
        super(pLabel, new TextField(), maxSize); // by default don't show character counts
        addSearchButton();
    }

    /**
     * creates new LabeledTextField with label and control
     *
     * @param pLabel label text
     * @param pTextField control
     */
    public LabeledInfoTextField(String pLabel, TextField pTextField) {
        super(pLabel, pTextField);
        addSearchButton();
    }
    /**
     * creates new LabeledTextField with label and control
     *
     * @param pLabel label text
     * @param pTextField control
     * @param maxSize maximum size (maximum amount of characters)
     */
    public LabeledInfoTextField(String pLabel, TextField pTextField, int maxSize) {
        super(pLabel, pTextField, maxSize);
        addSearchButton();
    }
    
     protected void addSearchButton(){
        searchButton = new Button();
        searchButton.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.SEARCH_PLUS));
        searchButton.setText("");
        searchButton.getStyleClass().add("cpx-icon-button");
        this.setAdditionalButton(searchButton); 
        setupPopupFields();
//        addPopupListener();
     }
     
    protected  void setupPopupFields(){
    };

    protected  void addPopupListener(){
            
         getAdditionalButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                VBox vBox = new VBox(8.0d);
                vBox.getChildren().addAll(getRelatedControls());
                PopOver popover = showInfoPopover(vBox);
                popover.setOnShowing(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent t) {
                        setShowCaret(false);
                    }
                });
                popover.setOnHiding(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent t) {
                        setShowCaret(true);
                    }
                });
                popover.show(getAdditionalButton());

            }

        });
};
    protected void disableControls(boolean armed){};
    
    public void setUpAutoCompletion() {};

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
    private ObjectProperty<ValidationSupport> validationSupportProperty;

    public ObjectProperty<ValidationSupport> validationSupportProperty() {
        if (validationSupportProperty == null) {
            validationSupportProperty = new SimpleObjectProperty<>();
        }
        return validationSupportProperty;
    }

    public ValidationSupport getValidationSupport() {
        return validationSupportProperty().get();
    }

    public void setValidationSupport(ValidationSupport pSupport) {
        validationSupportProperty().set(pSupport);
    }
    
    private ObjectProperty<AbstractCpxNonAnnualCatalog> catalogProperty;
    
   public ObjectProperty<AbstractCpxNonAnnualCatalog> catalogProperty() {
        if (catalogProperty == null) {
            catalogProperty = new SimpleObjectProperty<>();
        }
        return catalogProperty;
    }
   

    public AbstractCpxNonAnnualCatalog getCatalog() {
        return catalogProperty().get();
    }

    public void setCatalog(AbstractCpxNonAnnualCatalog pCatalog) {
        catalogProperty().set(pCatalog);
    }
    
    private ObjectProperty<List<LabeledTextField>> relatedControlsProperty;
    
    private ObjectProperty<List<LabeledTextField>> relatedControlsProperty() {
        if (relatedControlsProperty == null) {
            relatedControlsProperty = new SimpleObjectProperty<>();
            relatedControlsProperty.set(new ArrayList<>());
        }
        return relatedControlsProperty;
    }

    public List<LabeledTextField> getRelatedControls() {
        return relatedControlsProperty().get();
    }

    public void setRelatedControls(List<LabeledTextField> pList) {
        relatedControlsProperty().set(pList);
    }
    public void addRelatedControl(LabeledTextField pControl) {
       getRelatedControls().add(pControl);
    }
    
    public class ClearAutoCompleteFieldsListener implements ChangeListener<String> {

        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            List<LabeledTextField> controls = getRelatedControls();
            if(controls != null){
                for(LabeledTextField control: controls){
                    control.getControl().clear();
                }
            }

        }
    }

}
