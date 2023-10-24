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
package de.lb.cpx.ruleviewer.properties;

import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.fx.adapter.WeakPropertyAdapter;
import de.lb.cpx.client.core.model.fx.checked_combobox.CpxCheckComboBox;
import de.lb.cpx.client.core.model.fx.datepicker.FormatedDatePicker;
import de.lb.cpx.client.core.model.fx.spinner.CpxTimeSpinner;
import de.lb.cpx.client.core.model.fx.spinner.DoubleSpinner;
import de.lb.cpx.client.core.model.fx.spinner.NumberSpinner;
import de.lb.cpx.client.core.model.fx.tooltip.CpxTooltip;
import de.lb.cpx.client.core.util.code.CodeExtraction;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.rule.criteria.CriteriaHelper;
import de.lb.cpx.rule.criteria.model.CriterionTree.Supergroup.Group.Criterion;
import de.lb.cpx.rule.criteria.model.CriterionTree.Supergroup.Group.Criterion.Tooltip;
import de.lb.cpx.rule.criteria.model.TypesAndOperations.OperationGroups.OperationGroup.Operation;
import de.lb.cpx.ruleviewer.cache.RuleMetaDataCache;
import de.lb.cpx.ruleviewer.event.RefreshEvent;
import de.lb.cpx.ruleviewer.event.RuleChangedEvent;
import de.lb.cpx.ruleviewer.layout.RuleView;
import de.lb.cpx.ruleviewer.model.RuleMessageIndicator;
import de.lb.cpx.ruleviewer.model.Suggestion;
import de.lb.cpx.ruleviewer.model.Term;
import de.lb.cpx.ruleviewer.model.combobox.CatalogSuggestionComboBox;
import de.lb.cpx.ruleviewer.model.control.EditorControl;
import de.lb.cpx.ruleviewer.model.control.ListValueEditor;
import de.lb.cpx.ruleviewer.model.ruletable.content.model.Item;
import de.lb.cpx.ruleviewer.model.search.RuleTableSearchComboBox;
import de.lb.cpx.ruleviewer.util.TooltipHelper;
import de.lb.cpx.server.commonDB.model.enums.RuleTableCategoryEn;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import de.lb.cpx.service.ejb.TransferCatalogBeanRemote;
import de.lb.cpx.shared.json.RuleMessage;
import de.lb.cpx.shared.json.RuleTableMessageReader;
import de.lb.cpx.shared.json.enums.MessageReasonEn;
import de.lb.cpx.shared.lang.Lang;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javax.naming.NamingException;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.BeanProperty;
import org.controlsfx.property.editor.PropertyEditor;
import org.controlsfx.validation.Severity;

/**
 *
 * @author wilde
 */
public abstract class ValueEditor implements PropertyEditor<String> {

    private final PropertySheet.Item item;
    public static final String UPDATE_EDITOR = "update.editor";
//    private Term term;
    private final HBox container;
    private Term term;
    private Suggestion suggestion;
    private final RuleMessageIndicator messageIndicator;
    private final WeakPropertyAdapter adapter;
    private final Map<OperationType,String> typeLastValueMap = new HashMap<>();

    public ValueEditor(final PropertySheet.Item item) {
        this.item = item;
        container = new HBox();
        container.getStyleClass().add("editor-container");
        adapter = new WeakPropertyAdapter();
//        container.setFillWidth(true);
        getProperties().addListener(new MapChangeListener<String, Object>() {
            @Override
            public void onChanged(MapChangeListener.Change<? extends String, ? extends Object> change) {
                if (change.wasAdded()) {
                    if (UPDATE_EDITOR.equals(change.getKey())) {
                        initListeners();
                        createEditor();
                        setEditorStyle();
                        getProperties().remove(UPDATE_EDITOR);
                    }
                }
            }
        });
        container.addEventFilter(RefreshEvent.refreshEvent(), new EventHandler<RefreshEvent>() {
            @Override
            public void handle(RefreshEvent t) {
                getProperties().put(UPDATE_EDITOR, null);
            }
        });
        initListeners();
        createEditor();
        messageIndicator = new RuleMessageIndicator();
        messageIndicator.setPadding(new Insets(0, 8, 0, 0));
        setEditorStyle();
    }
    protected String getLastValueForOperationType(OperationType pType){
        return typeLastValueMap.get(pType);
    }
    protected void saveLastValueForType(OperationType pType, String pValue){
        typeLastValueMap.put(pType, pValue);
    }
    private void initListeners(){
        adapter.dispose();
        if(((BeanProperty) item).getBean() instanceof Term){
            adapter.addChangeListener(getTerm().messageProperty(),new ChangeListener<RuleMessage>() {
                @Override
                public void changed(ObservableValue<? extends RuleMessage> ov, RuleMessage t, RuleMessage t1) {
                    setEditorStyle();
                }
            });
        }
        if(((BeanProperty) item).getBean() instanceof Suggestion){
            adapter.addChangeListener(getSuggestion().messageProperty(),new ChangeListener<RuleMessage>() {
                @Override
                public void changed(ObservableValue<? extends RuleMessage> ov, RuleMessage t, RuleMessage t1) {
                    setEditorStyle();
                }
            });
        }
    }
    private static final Logger LOG = Logger.getLogger(ValueEditor.class.getName());

    @Override
    public Node getEditor() {
        return container;
    }

    public abstract Criterion getCriterion();

    public abstract Operation getOperation();

    public PropertySheet.Item getItem() {
        return item;
    }

    private String getDisplayText(String pValue) {
        pValue = Objects.requireNonNull(pValue, "");
        return pValue.replace("%", "*");
    }

    private String getValueText(String pValue) {
        pValue = Objects.requireNonNull(pValue, "");
        return pValue.replace("*", "%");
    }

    private TextField createTextfield(boolean pDisabled) {
        final TextField field = new TextField(getValue() != null ? getDisplayText(getValue()) : "----");
        field.setDisable(pDisabled);
        field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                setValue(getValueText(newValue));
            }
        });
        field.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            field.selectAll();
                        }
                    });
                }
            }
        });
        field.disabledProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (item.isEditable()) {
                    field.setText("");
                }
            }
        });
        return field;
    }
    private ListValueEditor createListValueEditor(){
        ListValueEditor editor = new ListValueEditor();
        editor.setValuesAsString(getValue() != null ? getDisplayText(getValue()) : "----");
        editor.setItemFactory(new Callback<String, Item>(){
            @Override
            public Item call(String p) {
                Item item = new Item(p, Boolean.TRUE);
                int idx = editor.getValues().indexOf(p);
                item.setOnContentChanged(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        setValue(getValueText(editor.getValuesAsString()));
                    }
                });
                item.setSingleInputCallback(new Callback<String, Void>() {
                    @Override
                    public Void call(String p) {
                        item.setText(p);
                        String nextVal = Objects.requireNonNullElse(item.getText(),"");
                        String oldVal = editor.getValues().get(idx);
                        if(nextVal.equals(oldVal)){
                            return null;
                        }
                        editor.getValues().set(idx, item.getText());
                        return null;
                    }
                });
                item.setMultiInputCallback(new Callback<String, Void>() {
                    @Override
                    public Void call(String p) {
                        p = Objects.requireNonNullElse(p, "");
                        if(p.isEmpty()){
                            return null;
                        }
                        editor.getValues().addAll(Arrays.asList(p.split(",")).stream().filter((t) -> {
                            return t!=null&&!t.isEmpty();
                        }).map((t) -> {
                            return t.trim();
                        }).collect(Collectors.toList()));
//                        setValue(getValueText(editor.getValuesAsString()));
                        return null;
                    }
                });
                item.setOnDeleteRequested(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        editor.getValues().remove(idx);
//                        setValue(getValueText(editor.getValuesAsString()));
                    }
                });
                item.setEditorFactory(new Callback<String, Control>() {
                    @Override
                    public Control call(String p) {
                        if (!editor.hasValidationMessage()) {
                            return item.createEditTextField();
                        }
                        String validation = editor.getValidationResult(p);
                        if (validation != null && !validation.isEmpty()) {
                            CatalogSuggestionComboBox cb = new CatalogSuggestionComboBox(validation);
                            cb.getSelectionModel().select(item.getText());
                            item.addEditorListeners(cb, true);
                            cb.setValueChangeCallback(new Callback<String, Void>() {
                                @Override
                                public Void call(String t1) {
                                    item.setText(t1);
                                    if (t1 == null) {
                                        if (item.getOnDeleteRequested() != null) {
                                            item.getOnDeleteRequested().handle(new ActionEvent());
                                        }
                                    } else {
                                        String text = t1;
                                        text = text.replace("*", "%");
                                        item.saveText(text);
                                    }
                                    item.setSeverity(Severity.OK);
                                    return null;
                                }
                            });
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    cb.getEditor().requestFocus();
                                }
                            });
                            return cb;
                        }
                        return item.createEditTextField();
                    }
                });
                setValue(getValueText(editor.getValuesAsString()));
                return item;
            }
        });
        editor.setValidationCallback(new Callback<String, String>() {
            @Override
            public String call(String p) {
                return findCodeSuggestionsForCode(p);
            }
        });
        editor.setUpdateCallback(new Callback<String, Void>() {
            @Override
            public Void call(String p) {
                LOG.info("new value: " + p);
                p = Objects.requireNonNullElse(p, "");
                if(p.equals(editor.getValuesAsString())){
                    return null;
                }
                LOG.log(Level.INFO, "Update Values to: {0}", p);
                editor.setValuesAsString(p);
                setValue(getValueText(editor.getValuesAsString()));
                return null;
            }
        });
        editor.setRuleMessage(getMessage());
        return editor;
    }
    private FormatedDatePicker createDatePicker(ChangeListener<LocalDate> pListener, LocalDate pDate) {
        FormatedDatePicker datePicker = new FormatedDatePicker(pDate);
        datePicker.getEditor().setStyle("-fx-font-size:14px;");
        datePicker.valueProperty().addListener(pListener);
        datePicker.disabledProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                datePicker.setValue(null);
            }
        });
        return datePicker;
    }

    private FormatedDatePicker createSingleDatePicker() {
        return createDatePicker(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                setValue(newValue != null ? newValue.format(DateTimeFormatter.BASIC_ISO_DATE) : "");
            }
        }, parseDate(getValue()));
    }
//    private Node createTimeSpinner(ChangeListener<LocalTime> pListener,LocalTime pTime) {
//        try {
//            CpxTimeSpinner spinner = new CpxTimeSpinner();
//            spinner.setLocalTime(pTime);
//            spinner.getTimeProperty().addListener(pListener);
//            spinner.disabledProperty().addListener(new ChangeListener<Boolean>() {
//                @Override
//                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                    if(newValue){
//                        spinner.setLocalTime(LocalTime.parse("00:00", DateTimeFormatter.ISO_LOCAL_TIME));
//                    }
//                }
//            });
//            return spinner;
//        } catch (CpxIllegalArgumentException ex) {
//            Logger.getLogger(ValueEditor.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return createDisabledTextField();
//    }

    private Node createTimeSpinner(ChangeListener<String> pListener, LocalTime pTime) {
        try {
            CpxTimeSpinner spinner = new CpxTimeSpinner();
            spinner.setLocalTime(pTime);
            spinner.textProperty().addListener(pListener);
            spinner.disabledProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (newValue) {
                        spinner.setLocalTime(LocalTime.parse("00:00", DateTimeFormatter.ISO_LOCAL_TIME));
                    }
                }
            });
            return spinner;
        } catch (CpxIllegalArgumentException ex) {
            Logger.getLogger(ValueEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return createDisabledTextField();
    }

    private Node createSingleTimeSpinner() {
//        return createTimeSpinner(new ChangeListener<LocalTime>() {
//            @Override
//            public void changed(ObservableValue<? extends LocalTime> observable, LocalTime oldValue, LocalTime newValue) {
//                setValue(newValue.format(DateTimeFormatter.ISO_LOCAL_TIME));
//            }
//        },parseTime(getValue()));
        Node node = createTimeSpinner(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                LocalTime.parse(newValue,DateTimeFormatter.ISO_LOCAL_TIME);
                item.setValue(newValue);
            }
        }, parseTime(getValue()));
//        if(node instanceof CpxTimeSpinner){
//            ((CpxTimeSpinner)node).valueProperty().addListener(new ChangeListener<LocalTime>() {
//                @Override
//                public void changed(ObservableValue<? extends LocalTime> observable, LocalTime oldValue, LocalTime newValue) {
//                    item.setValue(((CpxTimeSpinner)node).getText());
//                }
//            });
//        }
        return node;
    }

    private Node createDateTimeEditor() {
        String value = getValue();
        String[] split = value.split(" ");
        HBox edit = new HBox(5);
        FormatedDatePicker picker = createDatePicker(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if (newValue == null) {
                    setValue("");
                    return;
                }
                String value = getValue();
                String[] split = value.split(" ");
                if (split.length > 1) {
                    setValue(newValue.format(DateTimeFormatter.BASIC_ISO_DATE) + " " + split[1]);
                } else {
                    setValue(newValue.format(DateTimeFormatter.BASIC_ISO_DATE) + " " + LocalTime.parse("00:00", DateTimeFormatter.ISO_LOCAL_TIME));
                }
            }
        }, parseDate(split[0]));
        if (picker != null) {
            picker.setMaxHeight(Double.MAX_VALUE);
            edit.getChildren().add(picker);
        }
        LocalTime time = null;
        if (split.length > 1) {
            time = parseTime(split[1]);
        } else {
            time = LocalTime.parse("00:00", DateTimeFormatter.ISO_LOCAL_TIME);
        }
//        Node spinner = createTimeSpinner(new ChangeListener<LocalTime>() {
//                @Override
//                public void changed(ObservableValue<? extends LocalTime> observable, LocalTime oldValue, LocalTime newValue) {
//                    String value = getValue();
//                    String[] split = value.split(" ");
//                    setValue(split[0]+" " + newValue.format(DateTimeFormatter.ISO_LOCAL_TIME));
//                }
//            }, time);
        Node spinner = createTimeSpinner(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String value = getValue();
                String[] split = value.split(" ");
                setValue(split[0] + " " + LocalTime.parse(newValue, DateTimeFormatter.ISO_LOCAL_TIME));
            }
        }, time);
        if (spinner != null) {
            edit.getChildren().add(spinner);
        }
        return edit;
    }

    private Node createIntegerSpinner() {
        try {
            NumberSpinner spinner = new NumberSpinner(0, -1000, 10000);
            spinner.setInteger(checkIntegerValue() ? parseInteger(getValue()) : 0);
            spinner.disabledProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (newValue) {
                        spinner.setInteger(0);
                    }
                }
            });
            spinner.valueProperty().addListener(new ChangeListener<Integer>() {
                @Override
                public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                    item.setValue(spinner.getText());
                }
            });
            item.setValue(spinner.getText());
            return spinner;
        } catch (CpxIllegalArgumentException ex) {
            Logger.getLogger(ValueEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return createDisabledTextField();
    }
    private final ObservableMap<String, Object> properties = FXCollections.observableHashMap();

    public ObservableMap<String, Object> getProperties() {
        return properties;
    }

    public void refreshEditor() {
        getProperties().put(UPDATE_EDITOR, null);
    }
    private static final String NO_CRIT = "rules.txt.crit.sole.hpn.tooltip.1.2";

    private void createEditor() {
        container.getChildren().clear();
        if (getCriterion() == null) {
            addEditorInContainer(createDisabledTextField());
//            container.getChildren().add(createDisabledTextField());
            return;
        }
        if (NO_CRIT.equals(getCriterion().getName())) {
            addEditorInContainer(createTextfield(false));
            return;
        }
        OperationType type = OperationType.castOperation(getOperation());
        if (OperationType.NONE.equals(type)) { //set disabled Textfield if no operator is set
            addEditorInContainer(createDisabledTextField());
            return;
        }
        Node editor = createEditorForType(type);
//        if (editor instanceof Control) {
//            //set width to resize, to full with
//            ((Region) editor).prefWidthProperty().bind(container.widthProperty());
//        }
//        container.getChildren().add(editor);
        addEditorInContainer(editor);
    }
    private void addEditorInContainer(Node pNode){
        container.getChildren().add(pNode);
        bindEditorWidth(pNode);
    }
    private void bindEditorWidth(Node pNode){
        if (pNode instanceof Control) {
            //set width to resize, to full with
            ((Region) pNode).prefWidthProperty().bind(container.widthProperty());
        }
    }
    private Node createEditorForType(OperationType pType) {
        switch (pType) {
            case LIST:
                //make somekind of check to help and show
                //catalog data for multiselect?
                if (CriteriaHelper.isEnum(getCriterion())) {
                    return createEnumCheckCombobox();
                }
                return createListValueEditor();//createTextfield();
            case TABLE:
                return createRuleTableCombobox();
//                return createDisabledTextField();
            case SINGLE:
                return createSingleValueEditor();
            default:
                LOG.warning("Unknown Type: " + pType + ", create disabled Field!");
                return createDisabledTextField();
        }
    }

    private TextField createDisabledTextField() {
        TextField field = createTextfield(true);
        return field;
    }
    private RuleMessage getMessage(){
        if(((BeanProperty) item).getBean() instanceof Term){
            return getTerm().getMessage();
        }
        if(((BeanProperty) item).getBean() instanceof Suggestion){
            return getSuggestion().getMessage();
        }
        return null;
    }
    private Node createSingleValueEditor() {
        SingleDataType type = SingleDataType.castDataType(getCriterion());
        if (type == null) {
            return createDisabledTextField();
        }
        getCriterion();
        getItem();
        switch (type) {
            case STRING:
                if(hasNotificationInMessage()){
                    return createSuggestionComboBox();
                }
                return createTextfield(false);
            case DATE:
                return createSingleDatePicker();
            case TIME:
                return createSingleTimeSpinner();
            case INTEGER:
                return createIntegerSpinner();
            case DATETIME:
                return createDateTimeEditor();
            case DOUBLE:
                return createDoubleSpinner();
            case ENUM:
                return createEnumCombobox();
        }
        //should not happend
        return createDisabledTextField();
    }

    private LocalDate parseDate(String value) {
        if (value == null) {
            return null;
        }
        if (value.isEmpty()) {
            return null;
        }
        try {
            value = value.replace("'", "");
            return LocalDate.parse(value, DateTimeFormatter.BASIC_ISO_DATE);
        } catch (DateTimeParseException ex) {
            LOG.log(Level.FINEST, "Cannot parse as date: " + value, ex);
            return null;
        }
    }

    private LocalTime parseTime(String value) {
        if (value == null) {
            return null;
        }
        if (value.isEmpty()) {
            return null;
        }
        try {
            return LocalTime.parse(value, DateTimeFormatter.ISO_LOCAL_TIME);
        } catch (DateTimeParseException ex) {
            LOG.log(Level.FINEST, "Cannot parse as time: " + value, ex);
            return null;
        }
    }

    private Integer parseInteger(String value) {
        if (value == null) {
            return null;
        }
        if (value.isEmpty()) {
            return null;
        }
        return Integer.parseInt(value);
    }

    private Node createDoubleSpinner() {
//        if(isCW(getCriterion())){
//            if(isPepp(getCriterion())){
//                return createDoubleSpinner(4, -1000.0d, 1000.0d);
//            }
//            return createDoubleSpinner(3, -1000.0d, 1000.0d);
//        }
//        if(isCurrency(getCriterion())){
//            return createDoubleSpinner(2, 0, 20000.0d);
//        }

        //default spinner with 2 fractions
        return createDoubleSpinner(getDoublePrecision(getCriterion()));
    }

    private boolean checkValue(String pValue) {
        String val = pValue;
        if (val == null) {
            return false;
        }
        if (val.isEmpty()) {
            return false;
        }
        return true;
    }

    private boolean checkValue() {
        return checkValue(getValue());
    }

    private boolean checkIntegerValue(){
         if(checkValue(getValue())){
            try {  
                Integer.parseInt(getValue());  
                return true;
              } catch(NumberFormatException e){  
                return false;  
              }              
        }else{
            return false;
        }
    }
    
    private boolean checkDoubleValue(){
        if(checkValue(getValue())){
            try {  
                Double.parseDouble(getValue());  
                return true;
              } catch(NumberFormatException e){  
                return false;  
              }              
        }else{
            return false;
        }
    }
    
    private Node createDoubleSpinner(int pPrecision, double pLowerBorder, double pUpperBorder) {
        try {
            DoubleSpinner spinner = new DoubleSpinner(0.0, pLowerBorder, pUpperBorder, pPrecision);
            spinner.getValueFactory().setValue(checkDoubleValue() ? spinner.parse(getValue()) : 0.0);//checkValue()?spinner.getConverter().fromString(getValue()):0.0);
//            spinner.setText(checkValue()?getValue().replace(".", ","):spinner.getText());
            spinner.valueProperty().addListener(new ChangeListener<Double>() {
                @Override
                public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
                    String parsed = DoubleSpinner.parse(newValue, pPrecision);
                    item.setValue(parsed != null ? parsed.replace(",", ".") : parsed);
                }
            });
            String parsed = DoubleSpinner.parse(spinner.getValue(), pPrecision);
            item.setValue(parsed != null ? parsed.replace(",", ".") : parsed);
            return spinner;
        } catch (CpxIllegalArgumentException ex) {
            Logger.getLogger(ValueEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return createDisabledTextField();
    }

    private Node createDoubleSpinner(int pPrecision) {
        return createDoubleSpinner(pPrecision, -10000.0, 20000d);
    }

    private Node createEnumCombobox() {
        ComboBox<Criterion.Tooltip> enumBox = new ComboBox<>();
        enumBox.getItems().addAll(getCriterion().getTooltip());
        Tooltip tip = getTooltip(getValue());
        enumBox.getSelectionModel().select(tip);
        enumBox.setConverter(new StringConverter<Criterion.Tooltip>() {
            @Override
            public String toString(Criterion.Tooltip object) {
                if (object == null) {
                    return null;
                }
                return CriteriaHelper.getTooltipDescription(object);
            }

            @Override
            public Criterion.Tooltip fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        enumBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Criterion.Tooltip>() {
            @Override
            public void changed(ObservableValue<? extends Criterion.Tooltip> observable, Criterion.Tooltip oldValue, Criterion.Tooltip newValue) {
                if (newValue == null) {
                    setValue("");
                    return;
                }
                setValue(newValue.getValue());

            }
        });
        return enumBox;
    }
    private void setEditorStyle() {
        RuleMessage message = getMessage();
        getEditor();
//        if(message == null || (getCode() == null || getCode().isEmpty()) || (message.getDescription()== null || message.getDescription().isEmpty())){
        if(!hasNotificationInMessage(message)){
            container.pseudoClassStateChanged(PseudoClass.getPseudoClass("error"), false);
            container.getChildren().remove(messageIndicator);
            javafx.scene.control.Tooltip.install(messageIndicator, null);
            updateMessageInChildren(container, message);
            return;
        }
        container.pseudoClassStateChanged(PseudoClass.getPseudoClass("error"), true);
//        cbCriteria.getButtonCell().setStyle("-fx-text-fill:red;");
        if(!container.getChildren().contains(messageIndicator)){
           container.getChildren().add(0, messageIndicator);
        }
        String description = getNotificationDescription(message);
        javafx.scene.control.Tooltip.install(messageIndicator, description!=null?new CpxTooltip(description, 100, 5000, 100, true):null);
        updateMessageInChildren(container,message);
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
    public Suggestion getSuggestion() {
        if (suggestion == null) {
            Object bean = ((BeanProperty) item).getBean();
            if (!(bean instanceof Suggestion)) {
                LOG.severe("bean of the editor is not a Term!");
                return null;
            }
            suggestion = (Suggestion) bean;
        }
        return suggestion;
    }
    private Node createEnumCheckCombobox() {
        CpxCheckComboBox<Criterion.Tooltip> enumBox = new CpxCheckComboBox<>();
        enumBox.setMaxWidth(Double.MAX_VALUE);
        enumBox.getItems().addAll(getCriterion().getTooltip());
        List<Tooltip> tips = getTooltips(getValue());
        for (Tooltip tip : tips) {
            enumBox.getCheckModel().check(tip);
        }
//        enumBox.getSelectionModel().select(tips);
        enumBox.setConverter(new StringConverter<Criterion.Tooltip>() {
            @Override
            public String toString(Criterion.Tooltip object) {
                if (object == null) {
                    return null;
                }
                return CriteriaHelper.getTooltipDescription(object);
            }

            @Override
            public Criterion.Tooltip fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        enumBox.getCheckModel().getCheckedItems().addListener(new ListChangeListener<Tooltip>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Tooltip> c) {
                if (c == null) {
                    setValue("");
                    return;
                }
                if (c.getList().isEmpty()) {
                    setValue("");
                    return;
                }
                setValue(c.getList().stream().map((t) -> {
//                    Criterion.Tooltip tip = getTooltip(t.getValue());
//                    if(tip == null){
//                        return "";
//                    }
                    return t.getValue();
                }).collect(Collectors.joining(",")));
            }
        });

//        .selectedItemProperty().addListener(new ChangeListener<Criterion.Tooltip>() {
//            @Override
//            public void changed(ObservableValue<? extends Criterion.Tooltip> observable, Criterion.Tooltip oldValue, Criterion.Tooltip newValue) {
//                if(newValue == null){
//                    setValue("");
//                    return;
//                }
//                setValue(newValue.getValue());
//                
//            }
//        });
        return enumBox;
    }

    private List<Criterion.Tooltip> getTooltips(String pValue) {
        List<Tooltip> tips = new ArrayList<>();
        if(pValue != null){
            for (String value : pValue.split(",")) {
                value = value.trim();
                getCriterion().getTooltip();
                Tooltip tip = getTooltip(value);
                if (tip != null) {
                    tips.add(tip);
                }
            }
        }
        return tips;
    }

    private Criterion.Tooltip getTooltip(String pValue) {
        if(pValue == null){
            return null;
        }
        pValue = pValue.replaceAll("'", "");
        for (Tooltip tip : getCriterion().getTooltip()) {
            if (pValue.equals(tip.getValue())) {
                return tip;
            }
        }
        return null;
    }

    private Node createRuleTableCombobox() {
        RuleTableSearchComboBox box = new RuleTableSearchComboBox();
        box.setValidationCalllback(new Callback<CrgRuleTables, byte[]>() {
            @Override
            public byte[] call(CrgRuleTables param) {
                return validateRuleTable(param);
            }
        });
        box.setCodeSuggestionCallback(new Callback<String, String>(){
            @Override
            public String call(String param) {
                return findCodeSuggestionsForCode(param);
            }
        });
        box.setCriterion(getCriterion());
        CrgRuleTables selected = RuleMetaDataCache.instance().getTableForIdInPool(RuleView.getFacade().getPool(), getValue());
        box.selectItem(selected);//RuleView.getFacade().findRuleTable(getValue()));
        box.selectedItemProperty().addListener(new ChangeListener<CrgRuleTables>() {
            @Override
            public void changed(ObservableValue<? extends CrgRuleTables> observable, CrgRuleTables oldValue, CrgRuleTables newValue) {
                item.setValue(newValue != null ? (String.valueOf(newValue.getId())) : "");
            }
        });
        box.addEventFilter(RuleChangedEvent.ANY, new EventHandler<RuleChangedEvent>() {
            @Override
            public void handle(RuleChangedEvent event) {
                CrgRuleTables selected = box.getSelectedItem();
                item.setValue(selected != null ? (String.valueOf(selected.getId())) : "");
            }
        });
        return box;
    }

    private int getDoublePrecision(Criterion criterion) {
        if (criterion == null || criterion.getDoubleFormat() == null) {
            return 2;
        }
        return Lang.toMaxFractionDigits(criterion.getDoubleFormat());
    }
    public abstract String getCode();
    private Node createSuggestionComboBox() {
        String suggestions = findCodeSuggestionsForCode(Objects.requireNonNullElse(getCode(),"").replace("*", "%"));
        if(suggestions == null || suggestions.isEmpty()){
            return createTextfield(false);
        }
        CatalogSuggestionComboBox box = new CatalogSuggestionComboBox(suggestions);
        box.getSelectionModel().select(getCode());
        box.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String t) {
                return getDisplayText(t);
            }

            @Override
            public String fromString(String string) {
                return getValueText(string);
            }
        });
        box.setValueChangeCallback(new Callback<String, Void>(){
            @Override
            public Void call(String p) {
                setValue(getValueText(p));
                return null;
            }
        });
        return box;
    }
    public String findCodeSuggestionsForCode(String pCatalogCode) {
        try {
            TransferCatalogBeanRemote bean = Session.instance().getEjbConnector().connectTransferCatalogBean().getWithEx();
            RuleMessage msg = getMessage();
            return msg!=null?bean.getCodeSuggestion(pCatalogCode, getCategoryForCode(pCatalogCode), msg.getSrcYear(), msg.getDestYear(), 0L):null;
        } catch (NamingException ex) {
            Logger.getLogger(ValueEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public byte[] validateRuleTable(CrgRuleTables pTable){
        try {
            TransferCatalogBeanRemote bean = Session.instance().getEjbConnector().connectTransferCatalogBean().getWithEx();
            CrgRulePools pool = RuleView.getFacade().getPool();
            pTable.getCrgtContent();
            byte[] msg = bean.validateRuleTable(pool.getCrgplPoolYear(), pTable);
            new RuleTableMessageReader().readUtf8(msg);
            return msg;
        } catch (NamingException ex) {
            Logger.getLogger(ValueEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ValueEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    private RuleTableCategoryEn getCategoryForCode(String pCode) {
        if(pCode == null){
            return RuleTableCategoryEn.NOT_SET;
        }
        pCode = pCode.replace("%", "*");
        if(isOps(pCode)){
            return RuleTableCategoryEn.OPS;
        }
        if(isIcd(pCode)){
            return RuleTableCategoryEn.ICD;
        }
        return RuleTableCategoryEn.OTHER;
    }
    
    public boolean isOps(String pCode) {
        if (pCode != null) {
            return !CodeExtraction.getOpsCode(pCode.replace("'", "")).isEmpty();
        }
        return false;
    }

    public boolean isIcd(String pCode) {
        if (pCode != null) {
            return !CodeExtraction.getIcdCode(pCode.replace("'", "")).isEmpty();
        }
        return false;
    }

    private void updateMessageInChildren(HBox container, RuleMessage message) {
        if(container == null){
            return;
        }
        container.getChildren().stream().forEach((t) -> {
            if(t instanceof EditorControl){
                ((EditorControl)t).setRuleMessage(message);
            }
        });
    }
    private boolean hasNotificationInMessage(){
        return hasNotificationInMessage(getMessage());
    }
    private boolean hasNotificationInMessage(RuleMessage pMessage){
        OperationType opType = OperationType.castOperation(getOperation());
        if(OperationType.LIST.equals(opType)||OperationType.TABLE.equals(opType)){
            return !(pMessage == null /*|| (getCode() == null || getCode().isEmpty())*/ || (pMessage.getDescription()== null || pMessage.getDescription().isEmpty()));
        }
        if(OperationType.SINGLE.equals(opType) && (getCode() == null || getCode().isEmpty()) && (MessageReasonEn.VALIDATION_NO_VALUE.equals(pMessage!=null?pMessage.getReason():null))){
            return true; // no value is found - error state
        }
        return getNotificationFromMessage(pMessage)!=null;
    }
    private MessageReasonEn getNotificationReason(){
        return getNotificationReason(getMessage());
    }
    private MessageReasonEn getNotificationReason(RuleMessage pMessage){
        return getNotificationReason(getNotificationFromMessage(pMessage));
    }
    private MessageReasonEn getNotificationReason(String pNotification){
        if(pNotification == null){
            return null;
        }
        String[] parts = pNotification.split(":");
        if(parts.length!=2){
            return null;
        }
        return MessageReasonEn.valueOfIndex(parts[1].trim());
    }
    private String getNotificationFromMessage(){
        return getNotificationFromMessage(getMessage());
    }
    protected String getNotificationFromMessage(RuleMessage pMessage){
        if(pMessage == null){
            return null;
        }
        return getNotificationFromMessage(getValue(), pMessage.getCodes());
    }
    protected String getNotificationFromMessage(String pCode, String pCodes){
        pCode = Objects.requireNonNullElse(pCode, "");
        pCodes = Objects.requireNonNullElse(pCodes, "");
        
        if(pCode.isEmpty() || pCodes.isEmpty()){
            return null;
        }
        for(String pNotification : pCodes.split(",")){
            if(pNotification.contains(pCode)){
                return pNotification.trim();
            }
        }
        return null;
    }
    private String getNotificationDescription() {
        return getNotificationDescription(getMessage());
    }
    private String getNotificationDescription(RuleMessage message) {
        if(message == null){
            return null;
        }
        return getNotificationDescription(getValue(),message.getDescription());
    }
    private String getNotificationDescription(String pCode,String pDescription){
        pCode = Objects.requireNonNullElse(pCode, "nicht vollständig");
        pDescription = Objects.requireNonNullElse(pDescription, "");
        if(pCode.isEmpty() || pDescription.isEmpty()){
            return null;
        }
        OperationType opType = OperationType.castOperation(getOperation());
//        if(OperationType.LIST.equals(opType)){
//            return pDescription;
//        }
        if(OperationType.TABLE.equals(opType)){
            CrgRuleTables selected = RuleMetaDataCache.instance().getTableForIdInPool(RuleView.getFacade().getPool(), getValue());
            pCode = selected!=null?selected.getCrgtTableName():"nicht vollständig";
        }
        for(String description : pDescription.split("\r\n\n\r\n")){
            if(description.toUpperCase().contains(pCode.toUpperCase())){
                return description.trim();
            }
        }
        return null;
    }
    
    protected enum SingleDataType {
        STRING, INTEGER, DOUBLE, DATE, DATETIME, TIME, ENUM;

        /**
         * cas the criterion to specifix datatype, on which the specific editor
         * is shown
         *
         * @param pCriterion criterion value for the value
         * @return single datatype, base for which editor is created, default
         * datatype string - if criterion was null, null is returned
         */
        public static SingleDataType castDataType(Criterion pCriterion) {
            if (pCriterion == null) {
                return null;
            }
            if (isEnum(pCriterion)) {
                return SingleDataType.ENUM;
            }
            switch (pCriterion.getCriterionType()) {
                case "DATATYPE_STRING":
                case "DATATYPE_ARRAY_STRING":
                    return STRING;
                case "DATATYPE_DATE":
                case "DATATYPE_ARRAY_DATE":
                    //whacky test! but there is not real indicator if the date should be with time or without - in old software its
                    //hardcoded with full name of the criterion
                    if (pCriterion.getName().contains("date.full")) {
                        return DATETIME;
                    } else {
                        return DATE;
                    }
                case "DATATYPE_DOUBLE":
                case "DATATYPE_ARRAY_DOUBLE":
                    return DOUBLE;
                case "DATATYPE_INTEGER":
                case "DATATYPE_ARRAY_INTEGER":
                    return INTEGER;
                case "DATATYPE_DAY_TIME":
                    return TIME;
                default:
                    return STRING;
            }
        }

        private static boolean isEnum(Criterion pCriterion) {
            if (pCriterion.getTooltip().isEmpty()) {
                return false;
            }
            for (Criterion.Tooltip tip : pCriterion.getTooltip()) {
                //strange check but enums are only detectable when in tooltip a value is present
                if (tip.getValue() != null) {
                    return true;
                }
            }
            return false;
        }
    }

    protected enum OperationType {
        NONE, SINGLE, LIST, TABLE;

        /**
         * cast the Operation Object based on the Operation.name attribute to
         * its type
         *
         * @param pOperation currently set operation for the Value
         * @return type, single,list or table ; null if operation is null,
         * default value is single
         */
        public static OperationType castOperation(Operation pOperation) {
            if (pOperation == null) {
                return NONE;
            }
            switch (pOperation.getName()) {
                case "":
                    //if empty, no operator return null to force empty textfield
                    return NONE;
                case "IN":
                case "NOT IN":
                case "!!":
                case "##":
                    return LIST;
                case "@":
                case "NOT IN @":
                case "!!@":
                case "#@":
                    return TABLE;
                default:
                    return SINGLE;
            }
        }
    }
}
