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
package de.lb.cpx.client.core.model.fx.tableview.column;

import com.sun.javafx.event.RedirectedEvent;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.menu.fx.filterlists.FilterEvent;
import de.lb.cpx.client.core.model.filter.FilterBasicItem;
import de.lb.cpx.client.core.model.fx.button.NotAllowedIcon;
import de.lb.cpx.client.core.model.fx.checked_combobox.CpxCheckComboBox;
import de.lb.cpx.client.core.model.fx.datepicker.FormatedDatePicker;
import de.lb.cpx.client.core.model.fx.label.TooltipLabel;
import de.lb.cpx.client.core.model.fx.labeled.LabeledControl;
import de.lb.cpx.client.core.model.fx.labeled.LabeledControlSkin;
import de.lb.cpx.client.core.model.fx.labeled.LabeledDatePicker;
import de.lb.cpx.client.core.model.fx.labeled.LabeledFilterTextField;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextField;
import de.lb.cpx.client.core.model.fx.popover.AutoFitPopOver;
import de.lb.cpx.client.core.model.fx.tableview.FilterBaseTableView;
import de.lb.cpx.client.core.model.fx.textfield.DoubleConverter;
import de.lb.cpx.client.core.model.fx.textfield.FilterTextField;
import de.lb.cpx.client.core.model.fx.textfield.IntegerConverter;
import de.lb.cpx.client.core.model.fx.tooltip.BasicTooltip;
import de.lb.cpx.client.core.util.OverrunHelper;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.cpx.license.crypter.License;
import de.lb.cpx.model.enums.AdmissionCauseEn;
import de.lb.cpx.model.enums.AdmissionReason2En;
import de.lb.cpx.model.enums.AdmissionReasonEn;
import de.lb.cpx.model.enums.BooleanEn;
import de.lb.cpx.model.enums.CaseStatusEn;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.CpxEnumInterface;
import de.lb.cpx.model.enums.DischargeReasonEn;
import de.lb.cpx.model.enums.GrouperMdcOrSkEn;
import de.lb.cpx.model.enums.PlaceOfRegEn;
import de.lb.cpx.model.enums.RuleTypeEn;
import de.lb.cpx.model.enums.WeekdayEn;
import de.lb.cpx.model.lang.CpxLanguageInterface;
import de.lb.cpx.shared.filter.enums.ActionSubjectMap;
import de.lb.cpx.shared.filter.enums.InsuranceMap;
import de.lb.cpx.shared.filter.enums.MdkAuditReasonsMap;
import de.lb.cpx.shared.filter.enums.MdkStatesMap;
import de.lb.cpx.shared.filter.enums.ProcessResultMap;
import de.lb.cpx.shared.filter.enums.ProcessTopicMap;
import de.lb.cpx.shared.filter.enums.ReminderSubjectMap;
import de.lb.cpx.shared.filter.enums.SearchListAttribute;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;
import de.lb.cpx.wm.model.enums.WmEventTypeEn;
import de.lb.cpx.wm.model.enums.WmRequestTypeEn;
import de.lb.cpx.wm.model.enums.WmWorkflowStateEn;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.NestedTableColumnHeader;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javax.validation.constraints.NotNull;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.CheckModel;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

/**
 * FilterColumn for the working/workflow list display content based on dataType
 * and handle the filtering of one attribute
 *
 * TODO: unify behavior in checkedComboboxes, remove own graphic for column
 * header, better way should be lookup for header container
 *
 * @author wilde
 * @param <S> type of table content
 * @param <T> type
 */
public class FilterColumn<S, T> extends TableColumn<S, T> {

    private static final Logger LOG = Logger.getLogger(FilterColumn.class.getName());
    private static final String NO_DESC_TEXT = "(Keine Beschreibung vorhanden!)";

    private VBox headerRoot;
    private FilterPopOver popOver;
//    protected final SearchListAttribute searchAttribute;
    public final Serializable dataType;
    public final SearchListAttribute.OPERATOR operator;
    public final String dataKey;
    public final Translation translation;

    private Label lblTitle;
//    private ChangeListener<Node> popoverListener = new ChangeListener<>() {
//        @Override
//        public void changed(ObservableValue<? extends Node> observable, Node oldValue, Node newValue) {
//            popOver = new FilterPopOver(newValue);
//        }
//    };
    //todo:listener and popovercontentproperty can be removed .. has no effect - content is always created a new
//    private ChangeListener<Boolean> disableListener = new ChangeListener<Boolean>() {
//        @Override
//        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//            if (newValue) {
//                Label lbl = new Label("Filter für diesen Wert wurde deaktiviert.");
//                lbl.setPadding(new Insets(8));
//                setPopOverContent(lbl);
//            } else {
//                setPopOverContent(createFilterContent());
//            }
//        }
//    };
    private ChangeListener<SortType> sortListener = new ChangeListener<SortType>() {
        @Override
        public void changed(ObservableValue<? extends SortType> observable, SortType oldValue, SortType newValue) {
            if (getOnSortEdit() != null) {
                getOnSortEdit().call(newValue);
            }
        }
    };

    //disable filter property
    private BooleanProperty disableFilterProperty;
    //name property
    private StringProperty nameProperty;
    // allowed aclions
    private ObjectProperty <SearchListAttribute.ACTIONS_ALLOWED>actionAllowedProperty;
    //map for filter values by key, if filter has children (like in a date field if fromDate toDate is definied
    protected ObservableMap<String, FilterBasicItem> filterItemMap;
    //popover content property to react to changes if layout should change
//    private ObjectProperty<Node> popoverContentProperty;
    //callback to called if filter is edited
    private Callback<FilterBasicItem[], Boolean> onFilterEdit;
    private Callback<String, Boolean> onFilterDelete;
    private Callback<SortType, Void> onSortEdit;
    private Map<String, Control> keyControlMap;
    protected StringConverter<String> lockConverter;
    protected StringConverter<String> cancelConverter;
    protected StringConverter<Date> dateConverter;
    protected boolean enableFilter;
    private ChangeListener<SearchListAttribute.ACTIONS_ALLOWED> ACTIONS_ALLOWED_LISTENER = new ChangeListener<SearchListAttribute.ACTIONS_ALLOWED>() {
            @Override
            public void changed(ObservableValue<? extends SearchListAttribute.ACTIONS_ALLOWED> ov, SearchListAttribute.ACTIONS_ALLOWED t, SearchListAttribute.ACTIONS_ALLOWED t1) {
                updateHeader();
                if(SearchListAttribute.ACTIONS_ALLOWED.DO_ALL.equals(t1)){
                    setDisableFilter(false);
                    setSortable(true);
                }
                if(SearchListAttribute.ACTIONS_ALLOWED.DONT_SEARCH.equals(t1)){
                    setDisableFilter(true);
                    setSortable(true);
                }
                if(SearchListAttribute.ACTIONS_ALLOWED.DONT_SORT.equals(t1)){
                    setDisableFilter(false);
                    setSortable(false);
                }
                if(SearchListAttribute.ACTIONS_ALLOWED.DO_NOTHING.equals(t1)){
                    setDisableFilter(true);
                    setSortable(false);
                }
            }
        };
    
    public FilterColumn(String pDataKey, 
            String pLangKey, 
            Serializable pDataType, 
            final SearchListAttribute.OPERATOR pOperator
           
    ) {
        this(pDataKey, pLangKey, pDataType, pOperator,  SearchListAttribute.ACTIONS_ALLOWED.DO_ALL); 
    }
    /**
     * creates new column instance
     *
     * @param pLangKey language key to translate filter to correct language
     * @param pDataKey datakey of the filter
     * @param pDataType type of the filter, used to determine layout of the
     * @param pOperator operator (equal, less than, greater than...) filter and
     * special formatation
     * @param pActionAllowed defines actions to occure on table column aka sorting,filtering,both or nothing at all
     */
    public FilterColumn(String pDataKey, 
            String pLangKey, 
            Serializable pDataType, 
            final SearchListAttribute.OPERATOR pOperator,
            final SearchListAttribute.ACTIONS_ALLOWED pActionAllowed
    ) {//final SearchListAttribute pSearchAttribute){
        super();
//        searchAttribute = pSearchAttribute;
        dataType = pDataType;//pSearchAttribute.getDataType();
        operator = pOperator;
        dataKey = pDataKey;//pSearchAttribute.getKey();
        translation = Lang.get(pLangKey);//pSearchAttribute.getLanguageKey());
        setActionAllowed(pActionAllowed);
        actionAllowedProperty().addListener(ACTIONS_ALLOWED_LISTENER);
     //final SearchListAttribute pSearchAttribute){

        this.dateConverter = new StringConverter<Date>() {
            @Override
            public String toString(Date date) {
                return date == null ? "" : Lang.toIsoDate(date);
            }

            @Override
            public Date fromString(String string) {
                if (string == null || string.isEmpty()) {
                    return null;
                }
                try {
                    return new SimpleDateFormat(CpxLanguageInterface.ISO_DATE).parse(string);
                } catch (ParseException ex) {
                    Logger.getLogger(FilterColumn.class.getName()).log(Level.SEVERE, null, ex);
                }
                return null;
            }
        };
        this.lockConverter = new StringConverter<String>() {
            @Override
            public String toString(String object) {
                if (object == null) {
                    return "";
                }
                switch (object) {
                    case "0":
                        return Lang.getFilterCasesUnlock();
                    case "1":
                        return Lang.getFilterCasesLock();
                    default:
                        return "";
                }
            }

            @Override
            public String fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        this.cancelConverter = new StringConverter<String>() {
            @Override
            public String toString(String object) {
                if (object == null) {
                    return "";
                }
                switch (object) {
                    case "0":
                        return Lang.getFilterCasesCancelNot();
                    case "1":
                        return Lang.getFilterCasesCancel();
                    default:
                        return "";
                }
            }

            @Override
            public String fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        this.keyControlMap = new HashMap<>();
        this.filterItemMap = FXCollections.observableHashMap();

// try to set abbreviation, if empty set whole string in column header
//         setName(translation.getAbbreviation().isEmpty() ? translation.getValue() : translation.getAbbreviation());
        // Needs to find some better way.
        if (isDrg()) {
            setName(getDrgPeppColumnHeaderName());
        } else if (isMdc()) {
            setName(getMdcSkColumnHeaderName());
        } else {
            setName(translation.getAbbreviation().isEmpty() ? translation.getValue() : translation.getAbbreviation());
        }

        setGraphic(createHeader());
        setMinWidth(75d);

//creates new popover if content changes
//        popoverContentProperty().addListener(popoverListener);
//enable cell factory to render cells as Filtercells
        setCellFactory(new Callback<TableColumn<S, T>, TableCell<S, T>>() {
            @Override
            public TableCell<S, T> call(TableColumn<S, T> param) {
                return new FilterCell();
            }
        });
        sortTypeProperty().addListener(sortListener);
//get cell vales by datakey (values set by reflection?)
        setCellValueFactory(new PropertyValueFactory<>(dataKey));
//        disableFilterProperty().addListener(disableListener);

        addEventHandler(FilterEvent.filterEvent(), new EventHandler<FilterEvent>() {
            @Override
            public void handle(FilterEvent t) {
                if (getOnFilterEdit() != null) {
                    getOnFilterEdit().call(filterItemMap.values().toArray(new FilterBasicItem[filterItemMap.values().size()]));
//                    filterItemMap.clear();
                }
                LOG.info("trigger filter");
            }
        });

    }

    public final String getDrgPeppColumnHeaderName() {
        switch (LicenseMode.getCurrentMode()) {
            case DRG:
                return "DRG";
            case PEPP:
                return "PEPP";
            case ALL:
                return "DRG/PEPP";
            default:
                return "";
        }
    }

    public final String getMdcSkColumnHeaderName() {
        switch (LicenseMode.getCurrentMode()) {
            case DRG:
                return "MDC";
            case PEPP:
                return "SK";
            case ALL:
                return "MDC/SK";
            default:
                return "";
        }
    }

    public final String getDrgPeppColumnDescription() {
        switch (LicenseMode.getCurrentMode()) {
            case DRG:
                return "DRG";
            case PEPP:
                return "PEPP";
            case ALL:
                return "DRG/PEPP";
            default:
                return "";
        }
    }

    public final String getMdcSkColumnDescription() {
        switch (LicenseMode.getCurrentMode()) {
            case DRG:
                return translation.getDescription();
            case PEPP:
                return "SK";
            case ALL:
                return translation.getDescription();
            default:
                return "";
        }
    }

    public final boolean isMdc() {
        return isMdc(translation);
    }

    public final boolean isMdc(@NotNull Translation pTranslation) {
        pTranslation = Objects.requireNonNull(pTranslation, "Translation can not be null");
        if (pTranslation.getValue().equals("MDC") || pTranslation.getAbbreviation().equals("MDC")) {
            return true;
        }
        return false;
    }

    public final boolean isDrg() {
        return isDrg(translation);
    }

    public final boolean isDrg(@NotNull Translation pTranslation) {
        pTranslation = Objects.requireNonNull(pTranslation, "Translation can not be null");
        if (pTranslation.getValue().equals("DRG") || pTranslation.getAbbreviation().equals("DRG")) {
            return true;
        }
        return false;
    }

    //creates header area for title of the column
    //TODO:set here style class for label to change fontsize
    private Node createHeader() {
        //fetch abbreviation from lang
        //could result in nullpointer?
        lblTitle = new Label();

        lblTitle.textProperty().bind(nameProperty());
        lblTitle.setMaxHeight(10);
        lblTitle.setWrapText(true);
        lblTitle.setTextAlignment(TextAlignment.CENTER);
        lblTitle.setContentDisplay(ContentDisplay.RIGHT);
        lblTitle.setId("labeled-control-label");
        lblTitle.setFont(Font.font(15));
//        lblTitle.getStyleClass().add("-fx-font-size:15px;");
        //react to changes in filter
        //make title bold if something is filtered 
        //filtered values are stored in filterItemMap, so if map is empty weight is notmal if not bold
        //Could also be solved with pseudostyle class?
        lblTitle.fontProperty().bind(Bindings.when(Bindings.isEmpty(filterItemMap))
                .then(Font.font(lblTitle.getFont().getFamily(), FontWeight.NORMAL, lblTitle.getFont().getSize()))
                .otherwise(Font.font(lblTitle.getFont().getFamily(), FontWeight.BOLD, lblTitle.getFont().getSize())));
        /*Glyph icon = getAllowedActionIcon();      
        if(icon != null){
            icon.getStyleClass().add("glyph-icon");
//            icon.getStyleClass().add("-fx-font-size:20px;-fx-font-family:FontAwesome;");
//            lblTitle.setGraphic(icon);
//            HBox box = new HBox();
            Glyph ban = ResourceLoader.getGlyph(FontAwesome.Glyph.BAN);
            ban.getStyleClass().add("glyph-icon-ban");
//            Label  iconGraph = new Label();
//            iconGraph.getStyleClass().add("column-icon");
//            iconGraph.setAlignment(Pos.CENTER);
//            iconGraph.setGraphic(icon);
//            box.getChildren().addAll(lblTitle, iconGraph);
//            HBox.setHgrow(iconGraph, Priority.ALWAYS);
//            Label ban = new Label("/");
//            
//            ban.setStyle("-fx-background-color: transparent;-fx-text-fill:red;-fx-font-weight:bold");
//            ban.setMaxSize(20, 20);
//            ban.setMinSize(20, 20);
//            ban.setFont(Font.font(50));
            StackPane fullIcon = new StackPane(icon, ban);
            HBox box = new HBox();
            fullIcon.setPadding(new Insets(0, 0, 0, 10));
            box.getChildren().addAll(lblTitle, fullIcon);
////            ban.setPadding(new Insets(0,0,0,2));
//            box.add(lblTitle, 0, 0);
//            box.add(icon, 1, 0);
//            box.add(ban, 1, 0);
////            box.getChildren().addAll(lblTitle, icon);
            headerRoot = new VBox(box);
        }else{

            headerRoot = new VBox(lblTitle);
        }*/
        Node icon = getAllowedActionSymbol();
        if(icon != null){
            HBox box = new HBox();
            box.setAlignment(Pos.CENTER);
            box.getChildren().addAll(lblTitle, icon);
            headerRoot = new VBox(box);
        }else{
            headerRoot = new VBox(lblTitle);
        }
        headerRoot.setAlignment(Pos.CENTER);
        headerRoot.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //reset, drag lock to reenable resize 
                        //workaround for JavaFx-Bug, can be removed if bug is fixed, see CPX-1490
                        resetDragLock();
                        showFilter(event.getScreenX(),event.getScreenY());
                    }
                });
            }
        });
//        tooltip = new BasicTooltip(translation.getValue(), translation.getDescription().isEmpty() ? translation.getTooltip() : translation.getDescription()) {

        setTooltip(headerRoot);
        return headerRoot;
    }
    private void updateHeader(){
        Node icon = getAllowedActionSymbol();
        if(icon != null){
            HBox box = new HBox();
            box.setAlignment(Pos.CENTER);
            box.getChildren().addAll(lblTitle, icon);
            if(headerRoot == null){
                headerRoot = new VBox(box);
            }else{
                headerRoot.getChildren().addAll(box);
            }
        }else{
            if(headerRoot == null){
                headerRoot = new VBox(lblTitle);
            }else{
                headerRoot.getChildren().setAll(lblTitle);
            }
        }
    }
    private FontAwesome.Glyph getAllowedActionGlyph(){
        switch(getActionAllowed()){
            case DO_NOTHING:;
                return FontAwesome.Glyph.BAN;
            case DONT_SORT:;
                return FontAwesome.Glyph.SORT;
            case DONT_SEARCH:;
                return FontAwesome.Glyph.FILTER;
            default:
                return null;
        }
    }
    private Glyph getAllowedActionIcon(){
        FontAwesome.Glyph glyph = getAllowedActionGlyph();
        return glyph!=null?ResourceLoader.getGlyph(glyph):null;
    }
    private Node getAllowedActionSymbol(){
        FontAwesome.Glyph glyph = getAllowedActionGlyph();
        if(glyph == null){
            return null;
        }
        if(FontAwesome.Glyph.BAN.equals(glyph)){
            return new NotAllowedIcon();
        }
        return new NotAllowedIcon(glyph);
    }
    /**
     * @return get disable filter property
     */
    public final BooleanProperty disableFilterProperty() {
        if (disableFilterProperty == null) {
            disableFilterProperty = new SimpleBooleanProperty(false);
        }
        return disableFilterProperty;
    }
//    /**
//     * @return translated value for the column description
//     */
//    public final Translation getTranslation(){
//        return translation;
//    }

    /**
     * @return indicator if filter are disabled
     */
    public boolean isDisableFilter() {
        return disableFilterProperty().get();
    }

    /**
     * @param pDisable set if filter is disabled
     */
    public void setDisableFilter(boolean pDisable) {
        disableFilterProperty().set(pDisable);
    }

    /**
     * @return nameproperty of the filter
     */
    private StringProperty nameProperty() {
        if (nameProperty == null) {
            nameProperty = new SimpleStringProperty();
        }
        return nameProperty;
    }
    
    private ObjectProperty<SearchListAttribute.ACTIONS_ALLOWED> actionAllowedProperty(){
        if(actionAllowedProperty == null){
            actionAllowedProperty = new SimpleObjectProperty<SearchListAttribute.ACTIONS_ALLOWED>();
        }
        return actionAllowedProperty;
    }
    
    
    public final SearchListAttribute.ACTIONS_ALLOWED getActionAllowed(){
        return  actionAllowedProperty().get();
    }
    public final void setActionAllowed(SearchListAttribute.ACTIONS_ALLOWED action){
        actionAllowedProperty().set(action);
    }

    /**
     * @return get filter name
     */
    public String getName() {
        return nameProperty().get();
    }

    /**
     * @param pName set filter name
     */
    public final void setName(String pName) {
        nameProperty().set(pName);
    }

    /**
     * @return get filter datakey
     */
    public String getDataKey() {
        return dataKey;
    }

    /**
     * @return get filter translation to current language
     */
    public final Translation getTranslation() {
        return translation;
    }

    //helper methode for reenable resizing of columns
    //due to javafx bug, see CPX-1490
    private void resetDragLock() {
        if (getTableView() == null) {
            return;
        }
        TableHeaderRow headerRow = (TableHeaderRow) getTableView().lookup("TableHeaderRow");
        if (headerRow == null) {
            return;
        }
        for (Node node : headerRow.getChildren()) {
            if (node instanceof NestedTableColumnHeader) {
                try {
                    Field f1 = headerRow.getClass().getDeclaredField("columnDragLock");
                    f1.setAccessible(true);
                    f1.setBoolean(headerRow, false);
                } catch (SecurityException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException ex) {
                    LOG.log(Level.WARNING, "cannot set columnDragLock", ex);
                }
            }
        }
    }
    private Node createDisabledFilterContent(){
        Label lbl = new Label("Filter für diesen Wert wurde deaktiviert.");
        lbl.setPadding(new Insets(8));
        return lbl;
    }
    private Node createFilterContent() {
        Node content = isDisableFilter()?createDisabledFilterContent():createFilterPopOverContentNode(dataType, operator);
        if(content instanceof Region){
            ((Region)content).setMaxWidth(Double.MAX_VALUE);
        }
        Label title = new Label(translation.getValue());
        VBox box = new VBox(title, content);
        box.setFillWidth(true);
        box.setPadding(new Insets(8));
        box.setAlignment(Pos.CENTER);
        box.setSpacing(5);
        //CPX-2286-AWi:
        //prevent box from growing after inital value
        //try to prevent freeze - application will crash here 
        //when in size of checkedComboBoxes exceeds limits of monitor when user is checking items
        box.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                if(t.doubleValue() == 0.0d){
                    box.setMaxWidth(t1.doubleValue());
                    box.widthProperty().removeListener(this);
                }
            }
        });
        return box;
    }

    /**
     * @param pKey remove children from filtermap by key
     */
    public void clearFilterItem(String pKey) {
        FilterBasicItem item = filterItemMap.get(pKey);
        if (item != null) {
            filterItemMap.remove(pKey);
        }
    }

    /**
     * set new filter item in map
     *
     * @param pKey key(datakey) for filter value
     * @param pLangKey translated key
     * @param pValue filter value
     * @param pLangValue lang value
     * @param pDataType datatype
     * @return stored item
     */
    protected final FilterBasicItem setFilterItem(String pKey, String pLangKey, String pValue, String pLangValue, Serializable pDataType) {
        FilterBasicItem item = createFilterItem(pKey, pDataType);
        item.setValue(pValue);
        item.setLocalizedKey(pLangKey);
        item.setLocalizedValue(pLangValue);
        filterItemMap.put(pKey, item);
        return item;
    }

    public FilterBasicItem setFilterItem(SearchListAttribute pAttribute, String pValue) {
        Translation attTranslation = Lang.get(pAttribute.getLanguageKey());
        return setFilterItem(pAttribute.getKey(),
                attTranslation.getAbbreviation().isEmpty() ? attTranslation.getValue() : attTranslation.getAbbreviation(),
                pValue,
                addOperatorValue(pAttribute.getOperator(), transfromFilterValue(pValue, pAttribute.getDataType())),
                pAttribute.getDataType());
    }

//    /**
//     * @return popover content Property for bindings and listeners
//     */
//    public final ObjectProperty<Node> popoverContentProperty() {
//        if (popoverContentProperty == null) {
//            popoverContentProperty = new SimpleObjectProperty<>();
//        }
//        return popoverContentProperty;
//    }
//
//    /**
//     * @param pContent new content that should be shown in popover content
//     */
//    public void setPopOverContent(Node pContent) {
//        popoverContentProperty().set(pContent);
//    }
//
//    /**
//     * @return layout currently shown when popover is shown
//     */
//    public Node getPopOverContent() {
//        return popoverContentProperty().get();
//    }

    /**
     * @return callback instance to be called if filter is edited
     */
    public Callback<FilterBasicItem[], Boolean> getOnFilterEdit() {
        return onFilterEdit;
    }

    /**
     * @param pCallback callback to react to changes in the filter
     */
    public void setOnFilterEdit(Callback<FilterBasicItem[], Boolean> pCallback) {
        onFilterEdit = pCallback;
    }

    public Callback<String, Boolean> getOnFilterDelete() {
        return onFilterDelete;
    }

    public void setOnFilterDelete(Callback<String, Boolean> pCallback) {
        onFilterDelete = pCallback;
    }

    public Callback<SortType, Void> getOnSortEdit() {
        return onSortEdit;
    }

    public void setOnSortEdit(Callback<SortType, Void> pCallback) {
        onSortEdit = pCallback;
    }
    public void showFilter(){
        if (headerRoot.localToScreen(headerRoot.getBoundsInLocal()) == null) {
            return;
        }
        //try to simply compute coords based on owner node
        //should be in FilterPopover or Autofit Popover but could be tricky because arrowLocation schould be respected
        //for now it seems ok - if need arises in another case this should be unified as a behavior in AutoFitPopOver
        Bounds bounds = headerRoot.localToScreen(headerRoot.getBoundsInLocal());
        //set vertical Point of Arrow to Bottom of headerRoot minus some Paddign for ui reasons
        double yPos = bounds.getMaxY() - 8;
        double halfCoord = bounds.getMinX() + (bounds.getWidth()/2);
        showFilter(halfCoord, yPos);
    }
    /**
     * show filterpopover on header
     */
    public void showFilter(double x, double y) {
//        if (popOver == null) {
        if(popOver != null){
            popOver.destory();
        }
        if(isDisableFilter()){
            return;
        }
        popOver = new FilterPopOver();//getPopOverContent());
        popOver.setTitle(dataKey);
        popOver.setFitOrientation(Orientation.HORIZONTAL);
        Node content = createFilterContent();
        popOver.setContentNode(content);
        popOver.getScene().getWindow().sizeToScene();
//        }
//        LOG.info("try to show filter, is showing? " + popOver.isShowing());
        if (!popOver.isShowing()) {
            if (headerRoot == null) {
                return;
            }
            //2019-02-15 AWi
            //avoid np in show methode of popover
            //should be replaced with better check
            //somehow localToScreen returns null by valid getBoundsInLocal
            //Netbeans 11 did not support show sources for compiled javafx code
            //so no debugging, check should be sufficent for now, but should be investigated on a later point!
            if (headerRoot.localToScreen(headerRoot.getBoundsInLocal()) == null) {
                return;
            }
            //try to simply compute coords based on owner node
            //should be in FilterPopover or Autofit Popover but could be tricky because arrowLocation schould be respected
            //for now it seems ok - if need arises in another case this should be unified as a behavior in AutoFitPopOver
            Bounds bounds = headerRoot.localToScreen(headerRoot.getBoundsInLocal());
            //set vertical Point of Arrow to Bottom of headerRoot minus some Padding for ui reasons
            double yPos = bounds.getMaxY() - 8;
            double halfCoord = bounds.getMinX() + (bounds.getWidth()/2);
            double windowWidth = headerRoot.getScene().getWindow().getWidth();
            //set horizontal point of Arrow to half of the header root - half of headerroot is beginning 
            double xPos = halfCoord>windowWidth?x:halfCoord;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    LOG.finer("show " + getDataKey());
                    popOver.setXPos(xPos);
                    popOver.setYPos(yPos);
                    popOver.setArrowLocation(popOver.getAdjustedLocation());
                    popOver.show(headerRoot,xPos,yPos,Duration.ONE);
                }
            });
        }
    }

    /**
     * hide filterpopover if it is shown
     */
    public void hideFilter() {
        if (popOver == null) {
            return;
        }
//        LOG.info("try to hide filter, is showing? " + popOver.isShowing());
        if (popOver.isShowing()) {
            popOver.hide();
        }
    }

    /**
     * @return if filterpopover is currently showing
     */
    public boolean isFilterShowing() {
        if (popOver == null) {
            return false;
        }
        return popOver.isShowing();
    }

    /**
     * set current sort type as String supported Strings are asc,ascending,desc
     * and descending
     *
     * @param pSortType sort type to set as string
     */
    public void setSortTypeAsString(String pSortType) {
        if (pSortType == null) {
            return;
        }
        pSortType = pSortType.toLowerCase();
        if (pSortType.equals("asc") || pSortType.equals("ascending")) {
            setSortType(SortType.ASCENDING);
        }
        if (pSortType.equals("desc") || pSortType.equals("descending")) {
            setSortType(SortType.DESCENDING);
        }
    }

    /**
     * get Sorttype as string
     *
     * @param pType sorttype object
     * @return asc or desc , based on given sorttype
     */
    public String getSortType(SortType pType) {
        if (pType == null) {
            return null;
        }
        switch (pType) {
            case ASCENDING:
                return "asc";
            case DESCENDING:
                return "desc";
            default:
                return null;
        }
    }

    /**
     * transforms sorttype to string
     *
     * @return asc or desc, null(is null when sorttype is unknown or no type is
     * set)
     */
    public String getSortTypeAsString() {
        return getSortType(getSortType());
    }

    /**
     * creates new filter item
     *
     * @param pKey datakey of filter
     * @param pDataType datatype of the filter
     * @return filter basic item
     */
    public FilterBasicItem createFilterItem(String pKey, Serializable pDataType) {
        return new FilterBasicItem(pKey);
    }

    protected String transfromCellValue(Object pContent, Object pDataType) {
        //try to avoid null-Strings in listview replace with empty string
        if (pContent == null) {
            return "";
        }
        if (pDataType == String.class) {
            if (pContent.equals("null")) {
                LOG.info("detected string value: null on " + dataKey);
                return "";
            }
            return String.valueOf(pContent);
        }
        if (pDataType == Date.class) {
            return Lang.toDate((Date) pContent);
        }
        if (pDataType == Double.class) {
            return Lang.toDecimal((Double) pContent);
        }
        if (pDataType == Long.class) {
            return String.valueOf(pContent);
        }
        if (pDataType == BigDecimal.class) {
            return ((BigDecimal) pContent).setScale(((BigDecimal) pDataType).scale(), RoundingMode.HALF_UP).toString();
        }
        if (pDataType == Integer.class) {
            return String.valueOf(pContent);
        }
        if (pDataType == WeekdayEn.class) {
            return WeekdayEn.findById((Integer) pContent).getTranslation().getAbbreviation();
        }
        if (pDataType == CaseStatusEn.class) {
            return CaseStatusEn.valueOf(String.valueOf(pContent)).getTranslation().getValue();
        }
        if (pDataType == WmWorkflowStateEn.class) {
            WmWorkflowStateEn state = WmWorkflowStateEn.findById(String.valueOf(pContent));
            return state!=null?state.getTranslation().getValue():"";
        }
        if (pDataType == PlaceOfRegEn.class) {
            return PlaceOfRegEn.findByName(String.valueOf(pContent)).getTranslation().getValue();
        }
        return String.valueOf(pContent);//"Can not process datatype " + pDataType;
    }

    /**
     * transforms filter values to correct localized one example .. dates are
     * stored as iso dates, needs to transformed for display
     *
     * @param pContent string value of the filter
     * @param pDataType datatype
     * @return localized/transformed string
     */
    protected String transfromFilterValue(String pContent, Object pDataType) {
        //cast from iso to localized
        if (pDataType == Date.class) {
            return Lang.toDate(dateConverter.fromString(pContent));
        }
        if (pDataType == WeekdayEn.class) {
            return Arrays.stream(pContent.split(",")).map(item -> WeekdayEn.findById((Integer.parseInt(item))).getTranslation().getValue()).collect(Collectors.joining(","));
        }
        //CPX-994 
        if (pDataType == InsuranceMap.class) {
            String text = Arrays.stream(pContent.split(",")).map(item -> MenuCache.instance().getInsuranceForIkz(item)).collect(Collectors.joining(","));
            return text;
        }
//        if (pDataType == InsShortEn.class) {
//            String text = Arrays.stream(pContent.split(",")).map(item -> MenuCache.instance().getInsuranceShortForIkz(item)).collect(Collectors.joining(","));
//            return text;
//        }
        if (pDataType.equals(RuleTypeEn.class)) {
            return Arrays.stream(pContent.split(",")).map(item -> RuleTypeEn.valueOf(item).toString()).collect(Collectors.joining(","));
        }
       if (pDataType.equals(BooleanEn.class)) {
            return Arrays.stream(pContent.split(",")).map(item -> BooleanEn.valueOf(item).toString()).collect(Collectors.joining(","));
        }
        if (pDataType == Double.class) {
            return pContent.replace(".", Lang.getNumberFormatDecimal());
        }
        return pContent;
    }

    public String getOperatorValue() {
        return getOperatorValue(operator);
    }

    public String getOperatorValue(SearchListAttribute.OPERATOR pOperator) {
        if (pOperator == null) {
            return "";
        }
        return pOperator.getSymbol();
//        switch(pOperator){
//            case GREATER_THAN_OR_EQUAL_TO:
//                return ">=";
//            case LESS_THAN:
//                return "<";
//            case LESS_THAN_OR_EQUAL_TO:
//                return "<=";
//            case EQUAL:
//                //intended fall trough
//            case LIKE:
//                return "";
//            default:
//                return pOperator.name();
//        }
    }

    public String addOperatorValue(SearchListAttribute.OPERATOR pOperator, String pContent) {
        if (pContent == null) {
            return "";
        }
        return new StringBuilder().append(getOperatorValue(pOperator)).append(pContent).toString();
    }

    /**
     * methode to transform content and datatype to cell content renders cell,
     * by datatype should be overriden if default impl. is not enough TODO:
     * refactor handling if code gets messy, additional remove datakey! could
     * result in wrong key to to childen is datakey of column is there to handle
     * boolean values by name, should be refactored
     *
     * @param pKey datakey NOTE: called from cellfactory, will be key of the
     * column
     * @param pContent cell content
     * @param pDataType datatype
     * @return cell content to show
     */
    protected Node transfromCellNode(String pKey, Object pContent, Object pDataType) {
        if (pDataType == WeekdayEn.class) {
            Label lbl = new TooltipLabel(WeekdayEn.findById((Integer) pContent).getTranslation().getAbbreviation()) {
                @Override
                public String fetchTooltipText() {
                    return WeekdayEn.findById((Integer) pContent).getTranslation().getValue();
                }

            };
            return lbl;
        }
        if (pDataType == AdmissionCauseEn.class) {
            return new TooltipLabel(transfromCellValue(pContent, pDataType)) {
                @Override
                public String fetchTooltipText() {
                    return AdmissionCauseEn.valueOf((String) pContent).getTranslation().getValue();
                }

            };
        }
        //default value of datatype could not be processed
        return new Label(transfromCellValue(pContent, pDataType));
    }

    /**
     * methode to transform datatype to filter content renders content of filter
     * based on datatype should be overriden if default impl.is not enough TODO:
     * refactor handling if code gets messy
     *
     * @param pDataType datatype
     * @param pOperator operator for comparison
     * @return filter content to show
     */
    protected Node createFilterPopOverContentNode(Object pDataType, final SearchListAttribute.OPERATOR pOperator) {
        VBox content = new VBox();

        if (isWidthImprov(pDataType)) {
            content.setMaxWidth(500);
        } else {
            content.setMaxWidth(200);
        }
        List<Node> filterNodes = transformFilterNodes(pDataType, pOperator);
        for (Node filterNode : filterNodes) {
            if(filterNode instanceof Region){
                ((Region)filterNode).setPrefWidth(200);
                ((Region)filterNode).setMaxWidth(Double.MAX_VALUE);
            }
            filterNode.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent t) {
                    if (KeyCode.ENTER.equals(t.getCode())) {
                        if (popOver != null && popOver.isShowing()) {
                            popOver.hide();
                        }
//                        Event.fireEvent(FilterColumn.this, new FilterEvent());
                        t.consume();
                    }
                }
            });
        }
        content.getChildren().addAll(filterNodes);
        content.setFillWidth(true);
        content.setSpacing(5);
        content.setAlignment(Pos.CENTER);
        return content;
    }

    protected List<Node> transformFilterNodes(Object pDataType, final SearchListAttribute.OPERATOR pOperator) {
        List<Node> nodes = new ArrayList<>();
        if (pDataType == String.class) {
            nodes.add(getTextField(dataKey, translation.getValue()));
            return nodes;
        }
        if (pDataType == InsuranceMap.class) {
            nodes.add(getTextField(dataKey, translation.getValue()));
            return nodes;
        }
        if (pDataType == Date.class) {
            nodes.add(getDatePicker(dataKey, translation.getValue()));
            return nodes;
        }
        if (pDataType == Double.class) {
            nodes.add(getDoubleTextField(dataKey, translation.getValue()));
            return nodes;
        }
        if (pDataType == Integer.class) {
            nodes.add(getNumberTextField(dataKey, pOperator, translation.getValue()));
            return nodes;
        }
        if (pDataType == Long.class) {
            nodes.add(getNumberTextField(dataKey, pOperator, translation.getValue()));
            return nodes;
        }
//        if (pDataType == SearchListFormatInsurance.class) {
//            nodes.add(getNumberTextField(dataKey,translation.getValue()));
//            return nodes;
//        }
//        if (pDataType == SearchListFormatHospital.class) {
//            nodes.add(getNumberTextField(dataKey,translation.getValue()));
//            return nodes;
//        }
//        if (pDataType == SearchListFormatDepartment.class) {
//            nodes.add(getNumberTextField(dataKey,translation.getValue()));
//            return nodes;
//        }
        if (pDataType == WeekdayEn.class) {
            nodes.add(getWeekdayComboBox(dataKey, translation.getValue()));
            return nodes;
        }
        if (pDataType == CaseTypeEn.class) {
            nodes.add(getCaseTypeComboBox(dataKey, translation.getValue()));
            return nodes;
        }
        if (pDataType == CaseStatusEn.class) {
            nodes.add(getStatusComboBox(dataKey, translation.getValue()));
            return nodes;
        }
        if (pDataType == AdmissionCauseEn.class) {
            nodes.add(getAdmissionCauseComboBox(dataKey, translation.getValue()));
            return nodes;
        }
        //return default
        nodes.add(new Label("Can not process datatype " + pDataType));
        return nodes;
    }

    protected Map<String, Control> getKeyControlMap() {
        return keyControlMap;
    }

    protected FilterTextField getTextField(String pDataKey, String pLangKey, Callback<String, String> pValidateCallback) {
        FilterTextField textField = new FilterTextField();
        textField.setValidateCallback(pValidateCallback);
        keyControlMap.put(pDataKey, textField);
        textField.setText(getFilterValue(pDataKey));
        popOver.addEventHandler(WindowEvent.WINDOW_HIDING, new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                if (textField.getText() != null && !textField.getText().isEmpty()) {
                    textField.validateValue();
                }
                LOG.finer("Popover window hide " + dataKey);
                popOver.removeEventFilter(WindowEvent.WINDOW_HIDING, this);
            }
        });
        textField.filterValueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (textField.isDisabled()) {
                    return;
                }
                FilterBasicItem filterItem = getFilterItem(pDataKey, pLangKey);
                enableFilter = true;
                if (newValue == null) {
                    filterItem.setValue("");
                    LOG.info("clear filter of " + pDataKey + " value was null");
                    return;
                }
                filterItem.setValue(newValue);
                LOG.info("change filter of " + pDataKey + " value to " + filterItem.getValue() + " from " + (oldValue != null ? oldValue : "null"));
            }
        });

        return textField;
    }

    protected FilterTextField getTextField(String pDataKey, String pLangKey) {
        return getTextField(pDataKey, pLangKey, null);
    }

    protected FilterTextField getNumberTextField(String pDataKey, final SearchListAttribute.OPERATOR pOperator, String pLangKey) {
        FilterTextField textField = getTextField(pDataKey, pLangKey);
        final Pattern pattern;
        if (pOperator != null && pOperator == SearchListAttribute.OPERATOR.EQUAL) {
            //2019-01-18 DNi - Ticket CPX-1260: Allow comma for multiple value in this situation
            pattern = Pattern.compile("-?(\\d*(,-?\\d*)*)");
        } else {
            //2019-01-03 DNi - Ticket CPX-1260: Disallow comma for numeric (integer) text fields
            pattern = Pattern.compile("-?(\\d*(-?\\d*)*)");
        }
        TextFormatter<String> formatter = new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        });
        textField.setTextFormatter(formatter);
        return textField;
    }

    protected FilterTextField getDoubleTextField(String pDataKey, String pLangKey) {
        FilterTextField textField = getTextField(pDataKey, pLangKey, new Callback<String, String>() {
            @Override
            public String call(String param) {
                return param != null ? param.replace(Lang.getNumberFormatDecimal(), ".") : null;
            }
        });        
        String val = getFilterValue(pDataKey);
        textField.setText(val != null ? val.replace(".", Lang.getNumberFormatDecimal()) : null);
//        Pattern pattern = Pattern.compile("-?(\\d*(,-?\\d*)*)");
        Pattern pattern = Pattern.compile(String.format("-?(\\d*(%s-?\\d*)*)", Lang.getNumberFormatDecimal()));
        TextFormatter<String> formatter = new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
            return pattern.matcher(change.getText()).matches() ? change : null;
        });
        textField.setTextFormatter(formatter);
        return textField;
    }

    /**
     * Adjust the width of some filter dialogs by data type
     *
     * @param pDataType data type
     * @return true if width should improve
     *
     */
    //CPX-1207
    private boolean isWidthImprov(Object pDataType) {
        if (pDataType == ReminderSubjectMap.class //Art der WV
                || pDataType == InsuranceMap.class //Versicherungsname,
                || pDataType == ProcessResultMap.class//Vorgangsergebnis
                || pDataType == WmRequestTypeEn.class//Anfragetyp
                || pDataType == ProcessTopicMap.class//Vorgangsart
                || pDataType == AdmissionCauseEn.class
                || pDataType == DischargeReasonEn.class
                || pDataType == AdmissionReason2En.class
                || pDataType == AdmissionReasonEn.class
                || pDataType == WmEventTypeEn.class
                || pDataType == MdkAuditReasonsMap.class
                || pDataType == MdkStatesMap.class
                || pDataType == GrouperMdcOrSkEn.class
                || pDataType == ActionSubjectMap.class //AdmissionCauseEn ,DischargeReasonEn ,AdmissionReason2En ,AdmissionReasonEn ,MdkAuditReasonsMap, MdkStatesMap
                //                || pDataType == DepartmentShortNameMap.class
                ) {
            return true;
        }
        return false;
    }

    /**
     * get date picker object to store in filter
     *
     * @param pDataKey datakey of attribute to modify
     * @param pLangKey langkey of attribute for displaying
     * @return picker object with initialized listeners
     * @deprecated use labeled date pickers
     */
    @Deprecated(since = "1.09.0")
    protected DatePicker getDatePicker(String pDataKey, String pLangKey) {
        FormatedDatePicker picker = new FormatedDatePicker();
//        {
//            @Override
//            protected void impl_markDirty(DirtyBits dirtyBit) {
//                getStylesheets().add(0, getClass().getResource("/styles/cpx-default.css").toExternalForm());
//                super.impl_markDirty(dirtyBit); //To change body of generated methods, choose Tools | Templates.
//            }
//        };

        keyControlMap.put(pDataKey, picker);
        picker.addEventFilter(KeyEvent.KEY_PRESSED, new EnterEventHandler() {
            @Override
            public String getFilterValue() {
                return Lang.toIsoDate(picker.getValue());
            }

            @Override
            public String getDataKey() {
                return pDataKey;
            }

            @Override
            public String getLocalizedKey() {
                return pLangKey;
            }

            @Override
            public String getLocalizedValue() {
                return Lang.toDate(picker.getValue());
            }
        });
        picker.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    //force convert of string value in editor to date value
                    if (picker.getConverter() != null) {
                        picker.setValue(picker.getConverter().fromString(picker.getEditor().getText()));
                        event.consume();
                    }
                }
            }
        });
//            picker.getControl().valueProperty().addListener(new ChangeListener<LocalDate>() {
//                @Override
//                public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
//                    if (picker.isDisabled()) {
//                        return;
//                    }
//                    filter(pDataKey, pLangKey, newValue != null ? Lang.toIsoDate(picker.getControl().getValue()) : ""/*, Lang.toDate(picker.getControl().getValue())*/);
//                    picker.getStylesheets().add(getClass().getResource("/styles/cpx-default.css").toExternalForm());
//                }
//            });
        picker.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if (!t1) {
                    //force convert of string value in editor to date value
                    if (picker.getConverter() != null) {
                        picker.setValue(picker.getConverter().fromString(picker.getEditor().getText()));
                    }
                }
            }
        });
        popOver.addEventHandler(WindowEvent.WINDOW_HIDING, new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                if (!picker.getEditor().getText().isEmpty()) {
                    //force convert of string value in editor to date value
                    if (picker.getConverter() != null) {
                        picker.setValue(picker.getConverter().fromString(picker.getEditor().getText()));
                    }
                }
                popOver.removeEventFilter(WindowEvent.WINDOW_HIDING, this);
            }
        });
        picker.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if (picker.isDisabled()) {
                    return;
                }
                FilterBasicItem filterItem = getFilterItem(pDataKey, pLangKey);
                if (newValue == null) {
                    filterItem.setValue("");
                    LOG.info("clear filter of " + pDataKey + " value was null");
                    return;
                }
                filterItem.setValue(Lang.toIsoDate(picker.getValue()));
                LOG.info("change filter of " + pDataKey + " value to " + filterItem.getValue() + " from " + (oldValue != null ? Lang.toIsoDate(oldValue) : "null"));
//                    filter(pDataKey, pLangKey, newValue != null ? Lang.toIsoDate(picker.getControl().getValue()) : ""/*, Lang.toDate(picker.getControl().getValue())*/);
                picker.getStylesheets().add(getClass().getResource("/styles/cpx-default.css").toExternalForm());
            }
        });
//        picker.valueProperty().addListener(new ChangeListener<LocalDate>() {
//            @Override
//            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
//                LOG.info("value change");
//                filter(pDataKey, pLangKey, Lang.toIsoDate(newValue)/*, Lang.toDate(newValue)*/);

//            }
//        });
        return picker;
    }

    protected Label getFilterLabel(String pDataKey, String pLangKey) {
        Label lbl = new Label(Lang.get(pLangKey).getValue());
        lbl.setUserData(getFilterValue(pDataKey));
        keyControlMap.put(pDataKey, lbl);
        return lbl;
    }

    protected LabeledDatePicker getLabeledDatePicker(String pDataKey, String pLangKey, String pTitle) {
        LabeledDatePicker picker = new LabeledDatePicker(pTitle);
        picker.getStylesheets().add(getClass().getResource("/styles/cpx-default.css").toExternalForm());
        picker.applyCss();
        picker.getControl().setValue(Lang.toLocalDate(dateConverter.fromString(getFilterValue(pDataKey))));//transfromFilterValue(getFilterValue(pDataKey), Date.class));
        if (picker.getControl().getValue() != null) {
            picker.getProperties().put(LabeledControl.FOCUS_REQUESTED, null);
        }
        //TODO:FIXME! Look into css apply error, maybe gets fixed in later javafx version!?
        //needed for weird bug in popover after filtering, font sizes changes
        //due to css apply error 
        ((LabeledControlSkin) picker.getSkin()).setFontToTitle(Font.font(15));
        keyControlMap.put(pDataKey, picker);
        picker.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    picker.convert();
                    event.consume();
                }
            }
        });
//            picker.getControl().valueProperty().addListener(new ChangeListener<LocalDate>() {
//                @Override
//                public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
//                    if (picker.isDisabled()) {
//                        return;
//                    }
//                    filter(pDataKey, pLangKey, newValue != null ? Lang.toIsoDate(picker.getControl().getValue()) : ""/*, Lang.toDate(picker.getControl().getValue())*/);
//                    picker.getStylesheets().add(getClass().getResource("/styles/cpx-default.css").toExternalForm());
//                }
//            });
        picker.getControl().focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if (!t1) {
                    picker.convert();
                }
            }
        });
        popOver.addEventHandler(WindowEvent.WINDOW_HIDING, new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                if (!picker.getControl().getEditor().getText().isEmpty()) {
                    picker.convert();
                }
                popOver.removeEventFilter(WindowEvent.WINDOW_HIDING, this);
            }
        });
        picker.getControl().getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (picker.isDisabled()) {
                    return;
                }
                if (newValue == null || newValue.isEmpty()) {
                    picker.getControl().setValue(null);
                }
            }
        });
        picker.getControl().valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if (picker.isDisabled()) {
                    return;
                }
                FilterBasicItem filterItem = getFilterItem(pDataKey, pLangKey);
                enableFilter = true;
                if (newValue == null) {
                    filterItem.setValue("");
                    LOG.info("clear filter of " + pDataKey + " value was null");
                    return;
                }
                filterItem.setValue(Lang.toIsoDate(picker.getControl().getValue()));
                LOG.info("change filter of " + pDataKey + " value to " + filterItem.getValue() + " from " + (oldValue != null ? Lang.toIsoDate(oldValue) : "null"));
//                    filter(pDataKey, pLangKey, newValue != null ? Lang.toIsoDate(picker.getControl().getValue()) : ""/*, Lang.toDate(picker.getControl().getValue())*/);
                picker.getStylesheets().add(getClass().getResource("/styles/cpx-default.css").toExternalForm());
            }
        });
        return picker;
    }

    protected LabeledTextField getLabeledTextField(String pDataKey, String pLangKey, String pTitle) {
        LabeledTextField textfield = new LabeledTextField(pTitle, getTextField(pDataKey, pLangKey));
        return textfield;
    }

    protected LabeledTextField getLabeledTextField_Number(String pDataKey, String pLangKey, String pTitle, final SearchListAttribute.OPERATOR pOperator) {
        LabeledTextField textfield = new LabeledTextField(pTitle, getNumberTextField(pDataKey, pOperator, pLangKey));
        return textfield;
    }

    protected Control getLabeledNumberField(String pDataKey, String pLangKey, String pTitle, final SearchListAttribute.OPERATOR pOperator) {
        FilterTextField numberField = getNumberTextField(pDataKey, pOperator, pLangKey);
//        LabeledIntegerTextField textField = new LabeledIntegerTextField(pTitle, numberField);
        LabeledFilterTextField<Integer> textField = new LabeledFilterTextField<>(pTitle, numberField,new IntegerConverter());
//        numberField.filterValueProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                textField.valueProperty().set(textField.getConverter().fromString(textField.getEditor().getText()));
//            }
//        });
//        numberField.setValidateCallback(new Callback<String, String>() {
//            @Override
//            public String call(String param) {
//                Integer value = textField.getConverter().fromString(param);
//                if (value == null) {
//                    return null;
//                }
//                if (value == Integer.MIN_VALUE) {
//                    return "";
//                }
//                return textField.getEditor().getText();
//            }
//        });
        //replace in map - need better way
        keyControlMap.put(pDataKey, textField);
        ((LabeledControlSkin) textField.getSkin()).setFontToTitle(Font.font(15));
//        textfield.setMinWidth(75);
//        textfield.setMaxHeight(TextField.USE_PREF_SIZE);
        return textField;
    }

    protected Control getLabeledDoubleField(String pDataKey, String pLangKey, String pTitle) {
        FilterTextField doubleField = getDoubleTextField(pDataKey, pLangKey);
        LabeledFilterTextField<Double> textField = new LabeledFilterTextField<>(pTitle, doubleField,new DoubleConverter());
//        doubleField.filterValueProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                textField.valueProperty().set(textField.getConverter().fromString(textField.getEditor().getText()));
//            }
//        });
//        doubleField.setValidateCallback(new Callback<String, String>() {
//            @Override
//            public String call(String param) {
//                Double value = textField.getConverter().fromString(param);
//                if (value == null) {
//                    return null;
//                }
//                if (Double.doubleToRawLongBits(value) == Double.doubleToRawLongBits(Double.MIN_VALUE)) {
//                    return "";
//                }
//                return String.valueOf(value);//getEditor().getText();
//            }
//        });
        textField.valueProperty().addListener(new ChangeListener<Double>() {
            @Override
            public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
                doubleField.setFilterValue(newValue!=null?String.valueOf(newValue):null);
            }
        });
        //replace in map - need better way
        keyControlMap.put(pDataKey, textField);
        ((LabeledControlSkin) textField.getSkin()).setFontToTitle(Font.font(15));
        return textField;
    }

    private CpxCheckComboBox<WeekdayEn> getWeekdayComboBox(String pDataKey, String pLangKey) {
        EnumCheckedComboBox<WeekdayEn> box = new EnumCheckedComboBox<WeekdayEn>(pDataKey, pLangKey, WeekdayEn.values()) {
            @Override
            public String getCheckedItems(ObservableList<WeekdayEn> pItems) {
                return pItems.stream().map(item -> String.valueOf(item.getId())).collect(Collectors.joining(","));
            }

            @Override
            public String getLocalizedCheckedItems(ObservableList<WeekdayEn> pItems) {
                return pItems.stream().map(item -> item.getTranslation().getValue()).collect(Collectors.joining(","));
            }

            @Override
            public WeekdayEn getEnum(String pStr) {
                return WeekdayEn.findById(Integer.parseInt(pStr));
            }
        };
        return box;
    }

    private CpxCheckComboBox<CaseTypeEn> getCaseTypeComboBox(String pDataKey, String pLangKey) {
        EnumCheckedComboBox<CaseTypeEn> box = new EnumCheckedComboBox<CaseTypeEn>(pDataKey, pLangKey, CaseTypeEn.values()) {
            @Override
            public String getCheckedItems(ObservableList<CaseTypeEn> pItems) {
                return pItems.stream().map(item -> item.name()).collect(Collectors.joining(","));
            }

            @Override
            public String getLocalizedCheckedItems(ObservableList<CaseTypeEn> pItems) {
                return pItems.stream().map(item -> item.getTranslation().getValue()).collect(Collectors.joining(","));
            }

            @Override
            public CaseTypeEn getEnum(String pStr) {
                return CaseTypeEn.valueOf(pStr);
            }
        };
        return box;
    }

    private CheckComboBox<AdmissionCauseEn> getAdmissionCauseComboBox(String pDataKey, String pLangKey) {
        EnumCheckedComboBox<AdmissionCauseEn> box = new EnumCheckedComboBox<AdmissionCauseEn>(pDataKey, pLangKey, AdmissionCauseEn.values()) {
            @Override
            public String getCheckedItems(ObservableList<AdmissionCauseEn> pItems) {
                return pItems.stream().map(item -> item.name()).collect(Collectors.joining(","));
            }

            @Override
            public String getLocalizedCheckedItems(ObservableList<AdmissionCauseEn> pItems) {
                return pItems.stream().map(item -> item.getTranslation().getValue()).collect(Collectors.joining(","));
            }

            @Override
            public AdmissionCauseEn getEnum(String pStr) {
                return AdmissionCauseEn.valueOf(pStr);
            }
        };
        return box;
    }

    public final void selectFilterControl(String pDataKey) {
        Control ctrl = keyControlMap.get(pDataKey);
        if (ctrl != null) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    ctrl.requestFocus();
                    if (ctrl instanceof LabeledControl) {
                        ctrl.getProperties().put(LabeledControl.FOCUS_REQUESTED, null);
                    }
                }
            });
        }
    }

    protected CheckComboBox<CaseStatusEn> getStatusComboBox(String pDataKey, String pLangKey) {
        EnumCheckedComboBox<CaseStatusEn> box = new EnumCheckedComboBox<CaseStatusEn>(pDataKey, pLangKey, CaseStatusEn.values()) {
            @Override
            public String getCheckedItems(ObservableList<CaseStatusEn> pItems) {
                return pItems.stream().map(item -> item.name()).collect(Collectors.joining(","));
            }

            @Override
            public String getLocalizedCheckedItems(ObservableList<CaseStatusEn> pItems) {
                return pItems.stream().map(item -> item.getTranslation().getValue()).collect(Collectors.joining(","));
            }

            @Override
            public CaseStatusEn getEnum(String pStr) {
                return CaseStatusEn.valueOf(pStr);
            }
        };
        box.setConverter(new StringConverter<CaseStatusEn>() {
            @Override
            public String toString(CaseStatusEn object) {
                return object == null ? "" : object.getTranslation().getValue();
            }

            @Override
            public CaseStatusEn fromString(String string) {
                return null;
            }
        });
        return box;
    }

    public String getFilterValue(String pDataKey) {
        if (filterItemMap.isEmpty()) {
            return null;
        }
        String textFilter = pDataKey;//getKey(pCtrl);
        FilterBasicItem filterValue = filterItemMap.get(textFilter);
        if (filterValue != null && !filterValue.getValue().isEmpty()) {
            return filterValue.getValue();
        } else {
            return null;
        }
    }

    public final void filter(String pDataKey, String pLocKey, String pValue/*,String pLocValue*/) {
//        String key = pDataKey;//getKey(pCtrl);
//        FilterBasicItem filterItem = filterItemMap.get(key);
//        if (filterItem == null) {
//            filterItem = new FilterBasicItem(key);
//            filterItem.setLocalizedKey(pLocKey);
//            filterItemMap.put(key, filterItem);
//        }
        FilterBasicItem filterItem = getFilterItem(pDataKey, pLocKey);
        filterItem.setValue(pValue);
        if (getOnFilterEdit() != null) {
            getOnFilterEdit().call(new FilterBasicItem[]{filterItem});
        }
    }

    public final void filter(FilterBasicItem pItem) {
        if (getOnFilterEdit() != null) {
            getOnFilterEdit().call(new FilterBasicItem[]{pItem});
        }
    }

    public final void filter(FilterBasicItem... pItems) {
        if (getOnFilterEdit() != null) {
            getOnFilterEdit().call(pItems);
        }
    }

    public final FilterBasicItem getFilterItem(String pDataKey) {
        FilterBasicItem filterItem = filterItemMap.get(pDataKey);
        if (filterItem == null) {
            filterItem = new FilterBasicItem(pDataKey);
//            filterItem.setLocalizedKey(pLocKey);
            filterItemMap.put(pDataKey, filterItem);
        }
        return filterItem;
    }

    public final FilterBasicItem getFilterItem(String pDataKey, String pLocKey) {
        FilterBasicItem filterItem = getFilterItem(pDataKey);
        filterItem.setLocalizedKey(pLocKey);
        return filterItem;
    }

    private void setTooltip(Node headerRoot) {

        Tooltip tooltip = createTooltip(translation);
        Tooltip.install(headerRoot, tooltip);
    }

    private Tooltip createTooltip(@NotNull Translation pTranslation) {
        pTranslation = Objects.requireNonNull(pTranslation, "Translation can not be null");
        FontPosture posture = hasValidTooltipDescription(pTranslation) ? FontPosture.REGULAR : FontPosture.ITALIC;
        BasicTooltip tip = new BasicTooltip(getNameForTooltip(pTranslation), getDescriptionForTooltip(pTranslation), posture) {
            @Override
            protected void show() {
                if (getTableView() == null) {
                    return;
                }
                //only show tooltips if no filter is displayed
                if (((FilterBaseTableView) getTableView()).isShowTooltip()) {
                    super.show();
                }
            }
        };
        return tip;
    }

    private String getNameForTooltip(@NotNull Translation pTranslation) {
        pTranslation = Objects.requireNonNull(pTranslation, "Translation can not be null");
        // PNa: CPX-1753 (Lizenzabhängige Spaltennamen in Arbeitsliste)
        // may be a dirty solution, do we have any better way?
        if (isDrg()) {
            return getDrgPeppColumnHeaderName();
        } else if (isMdc()) {
            return getMdcSkColumnHeaderName();
        } else {
            return pTranslation.getValue();
        }
    }

    private String getDescriptionForTooltip(@NotNull Translation pTranslation) {
        pTranslation = Objects.requireNonNull(pTranslation, "Translation can not be null");
        if (isDrg(pTranslation)) {
            return getDrgPeppColumnDescription();
        }
        if (isMdc(pTranslation)) {
            return getMdcSkColumnDescription();
        }
        if (!hasValidTooltipDescription(pTranslation)) {
            return NO_DESC_TEXT;
        }
        return pTranslation.getDescription();
    }

    private boolean hasValidTooltipDescription(@NotNull Translation pTranslation) {
        pTranslation = Objects.requireNonNull(pTranslation, "Translation can not be null");
        if (!pTranslation.hasDescription()) {
            return false;
        }
        if (pTranslation.getDescription() == null) {
            return false;
        }
        if (pTranslation.getDescription().equals(pTranslation.getValue())) {
            return false;
        }
        return true;
    }


    protected abstract class BooleanToggleGroup extends VBox {

        private final ToggleGroup group = new ToggleGroup();
        private ObjectProperty<StringConverter<String>> resultConverterProperty;
        private StringProperty allTextProperty;
        private StringProperty trueTextProperty;
        private StringProperty falseTextProperty;

        public BooleanToggleGroup(String pDataKey, String pLangKey) {
            RadioButton all = new RadioButton();
            all.textProperty().bind(allTextProperty());
            all.setToggleGroup(group);
            all.setUserData("");
//            all.setSelected(true);

            RadioButton trueRb = new RadioButton();
            trueRb.textProperty().bind(trueTextProperty());
            trueRb.setToggleGroup(group);
            trueRb.setUserData("1");

            RadioButton falseRb = new RadioButton();
            falseRb.textProperty().bind(falseTextProperty());
            falseRb.setToggleGroup(group);
            falseRb.setUserData("0");

            String value = getFilterValue(pDataKey);
            if (null == value) {
                all.setSelected(true);
            } else {
                switch (value) {
                    case "0":
                        falseRb.setSelected(true);
                        break;
                    case "1":
                        trueRb.setSelected(true);
                        break;
                    default:
                        break;
                }
            }
            group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
                @Override
                public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                    //try to parse result for translation use result converter to get correct result
//                    String localizedResult = getResultConverter()!=null?getResultConverter().toString((String)newValue.getUserData()):(String) newValue.getUserData();
                    FilterBasicItem filterItem = getFilterItem(pDataKey, pLangKey);
                    enableFilter = true;
                    if (newValue == null) {
                        filterItem.setValue("");
                        return;
                    }
                    filterItem.setValue((String) newValue.getUserData());
//                    filter(pDataKey, pLangKey, (String) newValue.getUserData()/*, localizedResult*/);
                }
            });
            setSpacing(5.0);
            getChildren().addAll(trueRb, falseRb, all);

        }

        public ObjectProperty<StringConverter<String>> resultConverterProperty() {
            if (resultConverterProperty == null) {
                resultConverterProperty = new SimpleObjectProperty<>();
            }
            return resultConverterProperty;
        }

        public void setResultConverter(StringConverter<String> pConverter) {
            resultConverterProperty().set(pConverter);
        }

        public StringConverter<String> getResultConverter() {
            return resultConverterProperty().get();
        }

        public final StringProperty allTextProperty() {
            if (allTextProperty == null) {
                allTextProperty = new SimpleStringProperty("Alle Ergebnisse");
            }
            return allTextProperty;
        }

        public void setAllText(String pText) {
            allTextProperty().set(pText);
        }

        public final StringProperty trueTextProperty() {
            if (trueTextProperty == null) {
                trueTextProperty = new SimpleStringProperty("Wahre Ergebnisse");
            }
            return trueTextProperty;
        }

        public void setTrueText(String pText) {
            trueTextProperty().set(pText);
        }

        public final StringProperty falseTextProperty() {
            if (falseTextProperty == null) {
                falseTextProperty = new SimpleStringProperty("Falsche Ergebnisse");
            }
            return falseTextProperty;
        }

        public void setFalseText(String pText) {
            falseTextProperty().set(pText);
        }
    }

    protected class MapCheckedComboBox extends CpxCheckComboBox<Map.Entry<Long, String>> {

        public MapCheckedComboBox(String pDataKey, String pLangKey, List<Map.Entry<Long, String>> values) {
            super();
            getStylesheets().add(getClass().getResource("/styles/cpx-default.css").toExternalForm());
            //CPX-1207 Adjust the width of some filter dialogs 
            if (isWidthImprov(dataType)) {
                getStyleClass().add("filter-improv-check-box ");
            } else {
                getStyleClass().add("filter-check-box");
            }
            getItems().addAll(values);
            checkItems(getFilterValue(pDataKey));
            setConverter(new StringConverter<Map.Entry<Long, String>>() {
                @Override
                public String toString(Map.Entry<Long, String> object) {
                    return object == null ? "" : object.getValue();
                }

                @Override
                public Map.Entry<Long, String> fromString(String string) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
            addEventHandler(KeyEvent.KEY_PRESSED, new EnterEventHandler() {
                @Override
                public String getFilterValue() {
                    return getCheckedKeys();
                }

                @Override
                public String getDataKey() {
                    return pDataKey;
                }

                @Override
                public String getLocalizedKey() {
                    return pLangKey;
                }

                @Override
                public String getLocalizedValue() {
                    return getCheckedValues();
                }
            });
            getCheckModel().getCheckedItems().addListener(new ComboBoxCheckedChangeListener<Map.Entry<Long, String>>(pDataKey, pLangKey) {
                @Override
                public String getCheckedValues() {
                    return MapCheckedComboBox.this.getCheckedValues();
                }

                @Override
                public CheckModel<Map.Entry<Long, String>> getCheckModel() {
                    return MapCheckedComboBox.this.getCheckModel();
                }

                @Override
                public String getCheckedKeys() {
                    return MapCheckedComboBox.this.getCheckedKeys();
                }
            });
            //CPX-1108 ,CPX-1126
            addEventHandler(ComboBox.ON_HIDDEN, new ComboBoxOnHiddenEventHandler<Map.Entry<Long, String>>(pDataKey, pLangKey) {
                @Override
                public String getCheckedValues() {
                    return MapCheckedComboBox.this.getCheckedValues();
                }

                @Override
                public CheckModel<Map.Entry<Long, String>> getCheckModel() {
                    return MapCheckedComboBox.this.getCheckModel();
                }

                @Override
                public String getCheckedKeys() {
                    return MapCheckedComboBox.this.getCheckedKeys();
                }
            });
        }

        private String getCheckedKeys() {
            return getCheckModel().getCheckedItems().stream().map(item -> String.valueOf(item.getKey())).collect(Collectors.joining(","));
        }

        private String getCheckedValues() {
            return getCheckModel().getCheckedItems().stream().map(item -> item.getValue()).collect(Collectors.joining(","));
        }

        private void checkItems(String filterValue) {
            if (filterValue == null) {
                return;
            }
            String[] split = filterValue.split(",");
            if (split == null) {
                return;
            }
            for (String subString : split) {
                getCheckModel().check(getItem(Long.parseLong(subString)));
            }
        }

        private Map.Entry<Long, String> getItem(Long pId) {
            for (Map.Entry<Long, String> item : getItems()) {
                if (item.getKey().equals(pId)) {
                    return item;
                }
            }
            return null;
        }

    }
    private abstract class ComboBoxCheckedChangeListener<T> implements ListChangeListener<T>{
        private final String dataKey;
        private final String langKey;

        public ComboBoxCheckedChangeListener(String pDataKey, String pLangKey) {
            this.dataKey = pDataKey;
            this.langKey = pLangKey;
        }
        @Override
        public void onChanged(ListChangeListener.Change<? extends T> c) {
            if(c.next()){
                if (!filterItemMap.containsKey(dataKey) && getCheckModel().isEmpty()) {
                    enableFilter = false;
                    return;
                }
                FilterBasicItem filterItem = getFilterItem(dataKey, langKey);
                //data is already selected atleast once, check if selection becomes empty
                //reset values, prevent triggering of filter if not needed
                if (getCheckModel().getCheckedItems().isEmpty()) {
                    if (filterItem.getValue() != null && !filterItem.getValue().isEmpty()) {
                        enableFilter = true;
                    }
                    filterItem.setValue("");
                    return;
                }
                //update filter value with new selection and enable filter
                String value = getCheckedKeys();
                if (filterItem.getValue() == null || filterItem.getValue().isEmpty()) {
                    filterItem.setValue(value);
                    enableFilter = true;
                    return;
                }
                if (!filterItem.getValue().equals(value != null ? value : "")) {
                    filterItem.setValue(value);
                    enableFilter = true;
                }
            }
        }
        
        public abstract String getCheckedValues();

        public abstract CheckModel<T> getCheckModel();

        public abstract String getCheckedKeys();
    }
    private abstract class ComboBoxOnHiddenEventHandler<T> implements EventHandler<Event> {

        private final String dataKey;
        private final String langKey;

        public ComboBoxOnHiddenEventHandler(String pDataKey, String pLangKey) {
            this.dataKey = pDataKey;
            this.langKey = pLangKey;
        }

        @Override
        public void handle(Event event) {
            if (event == null || event.getEventType() == null) {
                return;
            }
            if (event.getEventType().equals(ComboBox.ON_HIDDEN) && getCheckedValues() != null) {
                //check if nothing happend
                //no value selected and no filter item is stored
                //return do nothing
                if (!filterItemMap.containsKey(dataKey) && getCheckModel().isEmpty()) {
                    enableFilter = false;
                    return;
                }
                FilterBasicItem filterItem = getFilterItem(dataKey, langKey);
                //data is already selected atleast once, check if selection becomes empty
                //reset values, prevent triggering of filter if not needed
                if (getCheckModel().getCheckedItems().isEmpty()) {
                    if (filterItem.getValue() != null && !filterItem.getValue().isEmpty()) {
                        enableFilter = true;
                    }
                    filterItem.setValue("");
                    return;
                }
                //update filter value with new selection and enable filter
                String value = getCheckedKeys();
                if (filterItem.getValue() == null || filterItem.getValue().isEmpty()) {
                    filterItem.setValue(value);
                    enableFilter = true;
                    return;
                }
                if (!filterItem.getValue().equals(value != null ? value : "")) {
                    filterItem.setValue(value);
                    enableFilter = true;
                }
//                    filter(pDataKey, pLangKey, getCheckedKeys()/*, getCheckedValues()*/);
            }
        }

        public abstract String getCheckedValues();

        public abstract CheckModel<T> getCheckModel();

        public abstract String getCheckedKeys();
    }

    //CPX-1108 ,CPX-1126
    protected class MapCheckedComboBoxString extends CpxCheckComboBox<Map.Entry<String, String>> {

        public MapCheckedComboBoxString(String pDataKey, String pLangKey, List<Map.Entry<String, String>> values) {
            super();
            getStylesheets().add(getClass().getResource("/styles/cpx-default.css").toExternalForm());
            //CPX-1207 Adjust the width of some filter dialogs 
            getStyleClass().add("filter-improv-check-box ");
            getItems().addAll(values);
            checkItems(getFilterValue(pDataKey));
            setConverter(new StringConverter<Map.Entry<String, String>>() {
                @Override
                public String toString(Map.Entry<String, String> object) {
                    return object == null ? "" : object.getValue();
                }

                @Override
                public Map.Entry<String, String> fromString(String string) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
            addEventHandler(KeyEvent.KEY_PRESSED, new EnterEventHandler() {
                @Override
                public String getFilterValue() {
                    return getCheckedKeys();
                }

                @Override
                public String getDataKey() {
                    return pDataKey;
                }

                @Override
                public String getLocalizedKey() {
                    return pLangKey;
                }

                @Override
                public String getLocalizedValue() {
                    return getCheckedValues();
                }
            });
//            //CPX-1108 ,CPX-1126
            addEventHandler(ComboBox.ON_HIDDEN, new ComboBoxOnHiddenEventHandler<Map.Entry<String, String>>(pDataKey, pLangKey) {
                @Override
                public String getCheckedValues() {
                    return MapCheckedComboBoxString.this.getCheckedValues();
                }

                @Override
                public CheckModel<Map.Entry<String, String>> getCheckModel() {
                    return MapCheckedComboBoxString.this.getCheckModel();
                }

                @Override
                public String getCheckedKeys() {
                    return MapCheckedComboBoxString.this.getCheckedKeys();
                }
            });
            getCheckModel().getCheckedItems().addListener(new ComboBoxCheckedChangeListener<Map.Entry<String, String>>(pDataKey, pLangKey) {
                @Override
                public String getCheckedValues() {
                    return MapCheckedComboBoxString.this.getCheckedValues();
                }

                @Override
                public CheckModel<Map.Entry<String, String>> getCheckModel() {
                    return MapCheckedComboBoxString.this.getCheckModel();
                }

                @Override
                public String getCheckedKeys() {
                    return MapCheckedComboBoxString.this.getCheckedKeys();
                }
            });
        }

        private String getCheckedKeys() {
            return getCheckModel().getCheckedItems().stream().map(item -> String.valueOf(item.getKey())).collect(Collectors.joining(","));
        }

        private String getCheckedValues() {
            return getCheckModel().getCheckedItems().stream().map(item -> item.getValue()).collect(Collectors.joining(","));
        }

        private void checkItems(String filterValue) {
            if (filterValue == null) {
                return;
            }
            String[] split = filterValue.split(",");
            if (split == null) {
                return;
            }
            for (String subString : split) {
                getCheckModel().check(getItem(subString));
            }
        }

        private Map.Entry<String, String> getItem(String pId) {
            for (Map.Entry<String, String> item : getItems()) {
                if (item.getKey().equals(pId)) {
                    return item;
                }
            }
            return null;
        }

    }

    protected abstract class EnumCheckedComboBox<T extends CpxEnumInterface<?>> extends CpxCheckComboBox<T> {

        public EnumCheckedComboBox(String pDataKey, String pLangKey, T[] values) {
            super();
            getStylesheets().add(getClass().getResource("/styles/cpx-default.css").toExternalForm());
            if (isWidthImprov(dataType)) {
                getStyleClass().add("filter-improv-check-box");
            } else {
                getStyleClass().add("filter-check-box");
            }
            getItems().addAll(getViewRelevantValues(values));
            checkItems(getFilterValue(pDataKey));
            addEventHandler(KeyEvent.KEY_PRESSED, new EnterEventHandler() {
                @Override
                public String getFilterValue() {
                    return getCheckedItems(getCheckModel().getCheckedItems());
                }

                @Override
                public String getDataKey() {
                    return pDataKey;
                }

                @Override
                public String getLocalizedKey() {
                    return pLangKey;
                }

                @Override
                public String getLocalizedValue() {
                    return getLocalizedCheckedItems(getCheckModel().getCheckedItems());
                }
            });
            getCheckModel().getCheckedItems().addListener(new ComboBoxCheckedChangeListener<T>(pDataKey, pLangKey) {
                @Override
                public String getCheckedValues() {
                    return getCheckedItems(getCheckModel().getCheckedItems());
                }

                @Override
                public CheckModel<T> getCheckModel() {
                    return EnumCheckedComboBox.this.getCheckModel();
                }

                @Override
                public String getCheckedKeys() {
                    return getCheckedItems(getCheckModel().getCheckedItems());
                }
            });
            addEventHandler(ComboBox.ON_HIDDEN, new ComboBoxOnHiddenEventHandler<T>(pDataKey, pLangKey) {
                @Override
                public String getCheckedValues() {
                    return getCheckedItems(getCheckModel().getCheckedItems());
                }

                @Override
                public CheckModel<T> getCheckModel() {
                    return EnumCheckedComboBox.this.getCheckModel();
                }

                @Override
                public String getCheckedKeys() {
                    return getCheckedItems(getCheckModel().getCheckedItems());
                }
            });

        }

        public abstract String getCheckedItems(ObservableList<T> pItems);

        public abstract String getLocalizedCheckedItems(ObservableList<T> pItems);

        public abstract T getEnum(String pStr);

        private void checkItems(String filterValue) {
            if (filterValue == null) {
                return;
            }
            String[] split = filterValue.split(",");
            if (split == null) {
                return;
            }
            for (String subString : split) {

                getCheckModel().check(getEnum(subString));

            }
        }

        private List<T> getViewRelevantValues(T[] values) {
            List<T> relevant = new ArrayList<>();
            for (T item : values) {
                if (item.isViewRelevant()) {
                    relevant.add(item);
                }
            }
            return relevant;
        }
    }

    protected abstract class EnterEventHandler implements EventHandler<KeyEvent> {

        @Override
        public void handle(KeyEvent event) {
            if (event.getCode().equals(KeyCode.ENTER)) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        event.consume();
                        popOver.requestFocus();
                        filter(getDataKey(), getLocalizedKey(), getFilterValue()/*,getLocalizedValue()*/);
                    }
                });
            }
        }

        public abstract String getFilterValue();

        public abstract String getDataKey();

        public abstract String getLocalizedKey();

        public abstract String getLocalizedValue();
    }

    //popover to show filer values
    private class FilterPopOver extends AutoFitPopOver {

        private TableView<S> tView;
        private boolean destroy = false;
        private final ChangeListener<TableView<S>> TABLE_VIEW_LISTENER = new ChangeListener<>() {
                @Override
                public void changed(ObservableValue<? extends TableView<S>> observable, TableView<S> oldValue, TableView<S> newValue) {
                    if (newValue != null) {
                        tView = newValue;
                    } else {
                        tableViewProperty().removeListener(this);
                        tView = oldValue;
                        destroy = true;
                    }
                }
            };
        
        public FilterPopOver(Node pContentNode) {
            this();
            setContentNode(pContentNode);
//            prefHeightProperty().bind();
        }

        public FilterPopOver() {
            super();
//            setDetachable(false);
            setAutoHide(false);
//            setAutoFix(false);
//            setArrowLocation(ArrowLocation.TOP_CENTER);
            setOnShowing(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    enableTooltipInParent(false);
                    if(onShowingEvent!=null){
                        onShowingEvent.handle(event);
                    }
                }
            });
            setOnHiding(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    enableTooltipInParent(true);
                    //dirty flag hurray ..
                    if (enableFilter) {
                        Event.fireEvent(FilterColumn.this, new FilterEvent());
                    }
                    if (destroy) {
                        tView = null;
                        tableViewProperty().removeListener(TABLE_VIEW_LISTENER);
                    }
                }
            });
            tView = getTableView();
            tableViewProperty().addListener(TABLE_VIEW_LISTENER);
            //needs to hide Popover, and not consume the click. Needs for scrollin in tableview
            addEventFilter(RedirectedEvent.REDIRECTED, new EventHandler<RedirectedEvent>() {
                @Override
                public void handle(RedirectedEvent ev) {
                    if(ev.getOriginalEvent() instanceof MouseEvent){
                        MouseEvent mEv = (MouseEvent) ev.getOriginalEvent();
                        if(MouseEvent.MOUSE_PRESSED.equals(mEv.getEventType())){
                            LOG.finer("hide due to redirect " + getTitle());
                            hide(Duration.ZERO);
                        }
                    }
                }
            });
        }
        
        @Override
        public void destory() {
            tableViewProperty().removeListener(TABLE_VIEW_LISTENER);
            super.destory();
        }
        
        private void enableTooltipInParent(boolean pEnable) {
            if (tView != null) {
                ((FilterBaseTableView) tView).setShowTooltip(pEnable);
            }
        }
        
    }

    protected class FilterCell extends TableCell<S, T> {

        public FilterCell() {
            super();
        }

        @Override
        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
//                if(WorkingListAttributes.csDrg.equals(dataKey)){
//                    Glyph glyph = ResourceLoader.getGlyph(FontAwesome.Glyph.EXCLAMATION).color(Color.RED);
//                    glyph.setStyle("-fx-text-fill:red;");
//                    setGraphic(new Label("----", glyph));
//                    setText(null);
//                    return;
//                }
                setGraphic(null);
                setText(null);
                return;
            }

            Label t = (Label) transfromCellNode(dataKey, item, dataType);

            if (t != null) {
                //  CPX-1314
                if (dataType == String.class && item instanceof String && ((String) item).contains("\n")) {
                    t = (Label) transfromCellNode(dataKey, ((String) item).replace("\n", ""), dataType);
                    OverrunHelper.addInfoTooltip(t, ((String) item));
                } else {
                    // CPX-1201 Tooltip appears when text is cut off
                    OverrunHelper.addInfoTooltip(t, t.getTooltip() != null ? t.getText() + "\n" + ((TooltipLabel) t).fetchTooltipText() : t.getText());
                }
            }
            setGraphic(t);
        }

    }

    protected enum LicenseMode {
        DRG, PEPP, ALL, NONE;

        public static LicenseMode getCurrentMode() {
            License license = Session.instance().getLicense();
            if (license.isDrgModule() && !license.isPeppModule()) {
                return LicenseMode.DRG;
            } else if (license.isPeppModule() && !license.isDrgModule()) {
                return LicenseMode.PEPP;
            } else if (license.isDrgModule() && license.isPeppModule()) {
                return LicenseMode.ALL;
            }
            return LicenseMode.NONE;
        }
    }

}
