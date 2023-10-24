/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.core.model.fx.tableview.column.rules;

import de.lb.cpx.shared.dto.rules.CpxSimpleRuleDTO;
import de.lb.cpx.shared.lang.Lang;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

/**
 *
 * @author gerschmann
 */
public class RuleSelectColumn extends NodeColumn<CpxSimpleRuleDTO> {

   private BooleanProperty rulesSelectProperty = new SimpleBooleanProperty(false);
   private Callback<CpxSimpleRuleDTO, Boolean> onSelectRule;
    
        public RuleSelectColumn() {
        super("Rel.");
        setMinWidth(40.0);
        setMaxWidth(40.0);
        setResizable(false);
//        widthProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                LOG.info("width " + newValue.doubleValue());
//            }
//        });
        setCellFactory(new Callback<TableColumn<CpxSimpleRuleDTO, Node>, TableCell<CpxSimpleRuleDTO, Node>>() {
            @Override
            public TableCell<CpxSimpleRuleDTO, Node> call(TableColumn<CpxSimpleRuleDTO, Node> param) {
                return new TableCell<CpxSimpleRuleDTO, Node>() {
                    @Override
                    protected void updateItem(Node item, boolean empty) {
                        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                        if (item == null || empty) {
                            setText("");
                            setGraphic(null);
                            return;
                        }
                        setGraphic(item);
//                        ErrorLevel level = ErrorLevel.getBySeverity(item);
//                        RuleTypeEn level = RuleTypeEn.findById(item);
//                        setText(level.getTranslation().getValue());
                    }

                };
            }
        });
        
        
    }

    @Override
    protected Node extractValue(CpxSimpleRuleDTO pValue) {
        CheckBox chkGr =  new CheckBox();
        chkGr.setSelected(pValue.isSelectedRuleFlag());
        chkGr.disableProperty().bind(rulesSelectProperty.not());
        chkGr.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                pValue.setSelectedRuleFl(chkGr.isSelected());
                if(onSelectRule != null){
                    onSelectRule.call(pValue);
                }
            }
        });
       return chkGr;
    }
    

    public void setRulesSelect(boolean b) {
        rulesSelectProperty.set(b);
    }
    
    public boolean isRuleSelect(){
        return rulesSelectProperty.get();
    }
    
    public BooleanProperty rulesSelectProperty(){
        return rulesSelectProperty;
    }

    public void setOnSelectRule(Callback<CpxSimpleRuleDTO, Boolean> pOnSelectRule) {
       onSelectRule = pOnSelectRule;
    }
}
