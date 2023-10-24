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
package de.lb.cpx.ruleviewer.properties;

import de.lb.cpx.client.core.model.fx.spinner.CpxSpinner;
import de.lb.cpx.client.core.model.fx.spinner.NumberSpinner;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.rule.criteria.CriteriaManager;
import de.lb.cpx.rule.criteria.TypesAndOperationsManager;
import de.lb.cpx.rule.criteria.model.CriterionTree;
import de.lb.cpx.rule.criteria.model.TypesAndOperations.IntervalLimits.IntervalLimit;
import de.lb.cpx.ruleviewer.model.Term;
import de.lb.cpx.shared.lang.Lang;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.StringConverter;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.BeanProperty;
import org.controlsfx.property.editor.PropertyEditor;

/**
 * Interval Editor for terms
 *
 * @author wilde
 */
public abstract class BasicIntervallEditor implements PropertyEditor<String> {

    protected Term term;
    private static final Logger LOG = Logger.getLogger(BasicIntervallEditor.class.getName());
    private final PropertySheet.Item item;
    private final ComboBox<IntervalLimit> cbInterval;
    private final HBox container;

    protected static final String INTERVAL_SYSDATE = "sysDate";
    protected static final String INTERVAL_MONTHS = "months";
    public static final String INTERVAL_NOTHING = "nothing";
    protected static final String INTERVAL_DISDATE = "disDate";
    protected static final String INTERVAL_ACTCASE = "actCase";
    protected static final String INTERVAL_DATE = "Date";
    protected static final String INTERVAL_YEARS = "years";
    protected static final String INTERVAL_QUATER = "quater";
    protected static final String INTERVAL_DAYS = "days";
    protected static final String INTERVAL_ADMDATE = "admDate";
    protected static final String INTERVAL_TIMESTAMP1 = "timeStamp1";
    protected static final String INTERVAL_TIMESTAMP2 = "timeStamp2";
    protected static final String INTERVAL_CASE = "case";

    protected static final String SAVE_VALUE = "save.value";

    public BasicIntervallEditor(PropertySheet.Item pItem) {
        item = pItem;
        cbInterval = new ComboBox<>();
        cbInterval.setConverter(new StringConverter<IntervalLimit>() {
            @Override
            public String toString(IntervalLimit object) {
                if (object == null) {
                    return null;
                }
                //replace beginning to find it in set 
                return Lang.get(object.getDisplayName().replace("rules", "Rules")).getValue();
            }

            @Override
            public IntervalLimit fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        cbInterval.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<IntervalLimit>() {
            @Override
            public void changed(ObservableValue<? extends IntervalLimit> observable, IntervalLimit oldValue, IntervalLimit newValue) {
                addEditor(createEditor(newValue));
                getProperties().put(SAVE_VALUE, null);
            }
        });
        HBox.setHgrow(cbInterval, Priority.ALWAYS);
        cbInterval.setMaxHeight(Double.MAX_VALUE);
//        cbInterval.setMaxWidth(Double.MAX_VALUE);
        container = new HBox(cbInterval, createEditor(INTERVAL_NOTHING));
        cbInterval.maxWidthProperty().bind(container.widthProperty().divide(2.0));
        cbInterval.minWidthProperty().bind(container.widthProperty().divide(2.0));
        container.setMaxWidth(Double.MAX_VALUE);
        container.setFillHeight(true);
        container.setSpacing(5);
        disableProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                container.setDisable(newValue);
            }
        });
//        container.disableProperty().bind(disableProperty());
        disableProperty.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
//                    disableEditor(true);
                    cbInterval.getSelectionModel().select(null);
                } else {
//                    cbInterval.getSelectionModel().selectFirst();
                }
            }
        });
        getTerm().firstConditionProperty().addListener(new ChangeListener<CriterionTree.Supergroup.Group.Criterion>() {
            @Override
            public void changed(ObservableValue<? extends CriterionTree.Supergroup.Group.Criterion> observable, CriterionTree.Supergroup.Group.Criterion oldValue, CriterionTree.Supergroup.Group.Criterion newValue) {
                setDisable(!CriteriaManager.instance().hasInterval(newValue));
            }
        });
        setDisable(!CriteriaManager.instance().hasInterval(getTerm().getFirstCondition()));
    }

    public void addEditor(Node pNode) {
        if (pNode == null) {
            if (container.getChildren().size() == 2) {
                container.getChildren().remove(1);
            }
            return;
        }
        if (container.getChildren().size() == 1) {
            container.getChildren().add(pNode);
        } else {
            container.getChildren().set(1, pNode);
        }
    }

    public void disableEditor(boolean pDisable) {
        if (container.getChildren().size() == 2) {
            container.getChildren().get(1).setVisible(!pDisable);
            addEditor(createEditor(INTERVAL_NOTHING));
        }
    }

    public ReadOnlyObjectProperty<IntervalLimit> selectedIntervallProperty() {
        return cbInterval.getSelectionModel().selectedItemProperty();
    }

    public IntervalLimit getSelectedInterval() {
        return selectedIntervallProperty().get();
    }

    public void selectInterval(IntervalLimit pInterval) {
        cbInterval.getSelectionModel().select(pInterval);
    }

    public final ObservableList<IntervalLimit> getIntervals() {
        return cbInterval.getItems();
    }

    public PropertySheet.Item getItem() {
        return item;
    }

    public Term getTerm() {
        if (term == null) {
            Object bean = ((BeanProperty) getItem()).getBean();
            if (!(bean instanceof Term)) {
                LOG.severe("bean of the editor is not a Term!");
                return null;
            }
            term = (Term) bean;
        }
        return term;
    }

    @Override
    public Node getEditor() {
        return container;
    }

    private Node createEditor(String pInterval) {
        switch (pInterval) {
            case INTERVAL_ACTCASE:
            case INTERVAL_ADMDATE:
            case INTERVAL_DISDATE:
            case INTERVAL_TIMESTAMP1:
            case INTERVAL_TIMESTAMP2:
            case INTERVAL_SYSDATE:
            case INTERVAL_NOTHING:
                TextField field = new TextField();
                HBox.setHgrow(field, Priority.ALWAYS);
                field.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        getProperties().put(SAVE_VALUE, null);
                    }
                });
                field.setVisible(false);
                return field;
            case INTERVAL_CASE:
            case INTERVAL_MONTHS:
            case INTERVAL_YEARS:
            case INTERVAL_DAYS:
            case INTERVAL_QUATER:
                try {
                    NumberSpinner spinner = new NumberSpinner(getInteger(), -1000, 1000);
                    spinner.setMaxWidth(Double.MAX_VALUE);
                    HBox.setHgrow(spinner, Priority.ALWAYS);
                    spinner.valueProperty().addListener(new ChangeListener<Integer>() {
                        @Override
                        public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                            getProperties().put(SAVE_VALUE, null);
                        }
                    });
                    return spinner;
                } catch (CpxIllegalArgumentException ex) {
                    Logger.getLogger(BasicIntervallEditor.class.getName()).log(Level.SEVERE, null, ex);
                    return null;
                }
            case INTERVAL_DATE:
                DatePicker picker = new DatePicker();
                picker.setMaxWidth(Double.MAX_VALUE);
                picker.setMaxHeight(Double.MAX_VALUE);
                HBox.setHgrow(picker, Priority.ALWAYS);
                picker.valueProperty().addListener(new ChangeListener<LocalDate>() {
                    @Override
                    public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                        getProperties().put(SAVE_VALUE, null);
                    }
                });
                return picker;
            default:
                return null;
        }
    }

    private Node createEditor(IntervalLimit newValue) {
        if (newValue == null) {
            return createEditor(INTERVAL_NOTHING);
        }
        return createEditor(newValue.getName());
    }
    
    public void setEditorValue(String pValue) {
        Node editor = container.getChildren().get(1);
        if (editor instanceof TextField) {
            ((TextInputControl) editor).setText(pValue);
        }
        if (editor instanceof NumberSpinner) {
            try{
                ((NumberSpinner) ((CpxSpinner<Integer>) editor)).setInteger(Integer.parseInt(pValue));
            }catch(NumberFormatException ex){
                LOG.log(Level.INFO, "Can not parse value: {0}, reason: {1}", new Object[]{pValue, ex.getMessage()});
            }
        }
        if (editor instanceof DatePicker) {
            LocalDate date = LocalDate.parse(pValue, DateTimeFormatter.BASIC_ISO_DATE);
            ((ComboBoxBase<LocalDate>) editor).setValue(date);
        }
    }

    public String getEditorValue() {
        if (container.getChildren().size() != 2) {
            return "";
        }
        Node editor = container.getChildren().get(1);
        if (editor instanceof TextField) {
            return ((TextInputControl) editor).getText();
        }
        if (editor instanceof NumberSpinner) {
            return ((CpxSpinner<Integer>) editor).getText();
        }
        if (editor instanceof DatePicker) {
            if (((DatePicker) editor).getValue() != null) {
                return ((DatePicker) editor).getValue().format(DateTimeFormatter.BASIC_ISO_DATE);
            }
        }
        return "";
    }
    public abstract String getIntervall();
    public Integer getInteger(){
        String intervall = Objects.requireNonNullElse(getIntervall(),"");
        String[] parts = intervall.split(":");
        if(parts.length != 2){
            return 0; //something is wrong can not parse further
        }
        switch(parts[0]){
            case INTERVAL_CASE:
            case INTERVAL_MONTHS:
            case INTERVAL_YEARS:
            case INTERVAL_DAYS:
            case INTERVAL_QUATER:
                try{
                    return Integer.parseInt(parts[1].trim());
                }catch(NumberFormatException ex){
                    LOG.log(Level.INFO, "can not parse number: {0} for interval: {1}! Reason:\n{2}", new Object[]{parts[1], parts[0], ex.getMessage()});
                    break;
                }
            default:
                LOG.log(Level.WARNING, "Intervall unknown: {0} for number: {1}", new Object[]{parts[0], parts[1]});
        }
        return 0;
    }
    @Override
    public void setValue(String value) {
        if (value == null) {
            return;
        }
        String[] split = value.split(":");
        IntervalLimit limit = TypesAndOperationsManager.instance().getInterval(split[0]);
        if (limit == null) {
            return;
        }
        selectInterval(limit);
        if (split.length == 2) {
            setEditorValue(split[1]);
        }
    }

    @Override
    public String getValue() {
        if (getSelectedInterval() == null) {
            return "";
        }
        if (getEditorValue().isEmpty()) {
            return getSelectedInterval().getName();
        }
        return getSelectedInterval().getName() + ":" + getEditorValue();
    }

    private ObservableMap<String, Object> properties = FXCollections.observableHashMap();

    public ObservableMap<String, Object> getProperties() {
        return properties;
    }

    private BooleanProperty disableProperty;// = new SimpleBooleanProperty(false);

    public final BooleanProperty disableProperty() {
        if (disableProperty == null) {
            disableProperty = new SimpleBooleanProperty(false);
        }
        return disableProperty;
    }

    public boolean isDisable() {
        return disableProperty().get();
    }

    public void setDisable(Boolean pDisable) {
        disableProperty().set(pDisable);
    }
}
