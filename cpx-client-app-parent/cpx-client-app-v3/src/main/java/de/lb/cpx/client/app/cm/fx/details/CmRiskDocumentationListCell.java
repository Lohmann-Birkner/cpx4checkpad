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
package de.lb.cpx.client.app.cm.fx.details;

import com.google.common.collect.Lists;
import de.lb.cpx.client.core.model.fx.button.CheckButton;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.wm.model.TWmRiskDetails;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.controlsfx.glyphfont.FontAwesome;
import de.lb.cpx.client.core.model.fx.listview.cell.ListCellWithButton;

/**
 *
 * @author wilde
 */
public class CmRiskDocumentationListCell extends ListCellWithButton<TWmRiskDetails> {

    private static final Logger LOG = Logger.getLogger(CmRiskDocumentationListCell.class.getName());
//    private static final Double DEFAULT_WIDTH_MENU_BTN = 30.0;
//    private final Label title;
//    private final HBox graphic;
//    private final HBox menu;
//    private final List<Node> menuItems;
//    private final HBox checks;
    private AuditCheckNode auditCheck;
    private FinalCheckNode finalCheck;

    public CmRiskDocumentationListCell(CheckMode pType) {
        super();
//        menuItems = new ArrayList<>();
//        this.title = new Label();
//        title.setMaxWidth(Double.MAX_VALUE);
//        HBox.setHgrow(title, Priority.ALWAYS);
//        this.menu = new HBox();
//        this.checks = new HBox(5);
//        checks.setAlignment(Pos.CENTER_LEFT);
//        checks.setMinWidth(USE_PREF_SIZE);
//        checks.setMaxWidth(USE_PREF_SIZE);
//        checks.setFillHeight(true);
//        updateChecks(pType);
//        menu.setMinWidth(USE_PREF_SIZE);
//        menu.setMaxWidth(USE_PREF_SIZE);
//        this.graphic = new HBox(5,title,checks,menu);
//        graphic.setAlignment(Pos.CENTER_LEFT);
        
//        setGraphic(graphic);
        updateChecks(pType);
    }
//    public final void setMenuItems(Node... pItems){
//        setMenuItems(Lists.newArrayList(pItems));
//    }
//    public final void setMenuItems(List<Node> pItems){
//        pItems = Objects.requireNonNullElse(pItems, new ArrayList<>());
//        menuItems.clear();
//        menuItems.addAll(pItems);
//        updateMenuItems();
//    }
//    protected final void updateMenuItems(List<Node> pItems){
//        pItems = Objects.requireNonNullElse(pItems, new ArrayList<>());
//        if(!menu.getChildren().containsAll(pItems)){
//            menu.getChildren().setAll(pItems);
//            menu.setPrefWidth(DEFAULT_WIDTH_MENU_BTN*pItems.size());
//        }
//    }
//    protected final void updateMenuItems(){
//        updateMenuItems(menuItems);
//    }
    
    @Override
    protected void updateItem(TWmRiskDetails t, boolean bln) {
        super.updateItem(t, bln); //To change body of generated methods, choose Tools | Templates.
        if (t == null || bln) {
            setGraphic(null);
            updateMenuItems(null);
            return;
        }
        title.setText(t.getRiskArea().getTranslation().getValue());
        setAuditCheck(t.getRiskUsedForAuditFl());
        setFinalCheck(t.getRiskUsedForFinalFl());
        updateMenuItems();
        setGraphic(graphic);
    }
    private void setAuditCheck(boolean pCheck){
        if(auditCheck == null){
            return;
        }
        auditCheck.setChecked(pCheck);
    }
    private void setFinalCheck(boolean pCheck){
        if(finalCheck == null){
            return;
        }
        finalCheck.setChecked(pCheck);
    }
    private void updateChecks(CheckMode pType) {
        pType = Objects.requireNonNullElse(pType, CheckMode.NONE);
        checks.getChildren().clear();
        switch(pType){
            case AUDIT_ONLY:
                auditCheck = new AuditCheckNode(Boolean.TRUE);
                checks.getChildren().add(auditCheck);
                checks.setPrefWidth(59.0);
                return;
            case FINAL_ONLY:
                finalCheck = new FinalCheckNode(Boolean.TRUE);
                checks.getChildren().add(finalCheck);
                checks.setPrefWidth(59.0);
                return;
            case ALL:
                auditCheck = new AuditCheckNode(Boolean.TRUE);
                finalCheck = new FinalCheckNode(Boolean.TRUE);
                checks.getChildren().addAll(auditCheck,finalCheck);
                checks.setPrefWidth(59.0);
                return;
            default:
                auditCheck = null;
                finalCheck = null;
                 checks.setPrefWidth(USE_PREF_SIZE);
                LOG.log(Level.FINER, "do not set any checks for versionRiskType: {0}", pType.name());
        }
    }
    public enum CheckMode{
        NONE,AUDIT_ONLY,FINAL_ONLY,ALL;
    }
    
    private class AuditCheckNode extends CheckNode{

        public AuditCheckNode(TWmRiskDetails pDetail) {
            this(pDetail.getRiskUsedForAuditFl());
        }
        public AuditCheckNode(Boolean pChecked) {
            super("A", pChecked);
        }
    }
    private class FinalCheckNode extends CheckNode{

        public FinalCheckNode(TWmRiskDetails pDetail) {
            this(pDetail.getRiskUsedForFinalFl());
        }
        public FinalCheckNode(Boolean pChecked) {
            super("G", pChecked);
        }
    }
    private class CheckLabel extends Label{

        public CheckLabel(boolean pChecked) {
            super();
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setChecked(pChecked);
            handleChecked(pChecked);
            checkedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                    handleChecked(t1);
                }
            });
            setMinWidth(USE_PREF_SIZE);
            setPrefWidth(14.0);
            setMaxWidth(USE_PREF_SIZE);
        }

        private void handleChecked(boolean pChecked) {
            setGraphic(pChecked?ResourceLoader.getGlyph(FontAwesome.Glyph.CHECK):null);
        }
        private BooleanProperty checkedProperty;
        public final BooleanProperty checkedProperty(){
            if(checkedProperty == null){
                checkedProperty = new SimpleBooleanProperty(true);
            }
            return checkedProperty;
        }
        public final void setChecked(boolean pChecked){
            checkedProperty().set(pChecked);
        }
        public boolean isChecked(){
            return checkedProperty().get();
        }
    }
    private class CheckNode extends HBox{

        public CheckNode(String pTitle, boolean pChecked) {
            super(2, new Label(pTitle), new CheckLabel(pChecked));
            setAlignment(Pos.CENTER_LEFT);
        }
        public void setChecked(boolean pChecked){
            CheckLabel btn = getCheckLabel();
            if(btn!=null){
                btn.setChecked(pChecked);
            }
        }
        public boolean isChecked(){
            CheckLabel btn = getCheckLabel();
            return btn!=null?btn.isChecked():false;
        }
        
        protected CheckButton getCheckButton(){
            if(getChildren().size()<2){
                return null;
            }
            Node node = getChildren().get(1);
            if(node instanceof CheckButton){
                return (CheckButton) node;
            }
            return null;
        }
        protected CheckLabel getCheckLabel(){
            if(getChildren().size()<2){
                return null;
            }
            Node node = getChildren().get(1);
            if(node instanceof CheckLabel){
                return (CheckLabel) node;
            }
            return null;
        }
    }
}
