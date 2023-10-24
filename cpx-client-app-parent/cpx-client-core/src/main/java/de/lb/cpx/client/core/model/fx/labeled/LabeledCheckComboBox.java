/* 
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2017  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.labeled;

import de.lb.cpx.client.core.model.fx.checked_combobox.CpxCheckComboBox;
import de.lb.cpx.client.core.model.fx.checked_combobox.CpxComboBoxListViewSkin;
import de.lb.cpx.client.core.model.fx.tooltip.BasicTooltip;
import impl.org.controlsfx.skin.CheckComboBoxSkin;
import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.ListView;
import javafx.scene.control.Skin;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;
import org.controlsfx.control.IndexedCheckModel;

/**
 * TODO: Implement Focus change
 *
 * @author nandola
 * @param <T> object type to be handled in checked combobox
 */
public class LabeledCheckComboBox<T> extends LabeledControl<CpxCheckComboBox<T>> {

    private static final Logger LOG = Logger.getLogger(LabeledCheckComboBox.class.getName());

//    private boolean selected;
    private ObjectProperty<ComboBox<T>> comboboxProperty = new SimpleObjectProperty<>();
    @SuppressWarnings("unchecked")
    private final ChangeListener<Skin<?>> skinListener = (skinObs, oldVal, newVal) -> {

        if (oldVal == null && newVal != null) {

            CheckComboBoxSkin<T> skin = (CheckComboBoxSkin<T>) newVal;
            comboboxProperty.set((ComboBox) skin.getChildren().get(0));
            initComboBox();
//            comboboxProperty.get().focusedProperty().addListener(new ChangeListener<Boolean>() {
//                @Override
//                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                    if(newValue){
//                        getControl().requestFocus();
//                    }
//                }
//            });
//            comboboxProperty.get().focusedProperty().addListener(new ChangeListener<Boolean>() {
//                @Override
//                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                    if (newValue) {
//                        getControl().requestFocus();
//                    }
//                }
//            });
        }
    };
//    private ObjectProperty<IndexedCheckModel<T>> checkModel
//            = new SimpleObjectProperty<>(this, "checkModel"); //$NON-NLS-1$
//    private ComboBox combobox;

    public LabeledCheckComboBox() {
        this("Label");
    }

    public ListView<T> getListView() {
        return getControl().getCpxComboBoxListViewSkin().getListView();
    }

    public LabeledCheckComboBox(String pLabel, CpxCheckComboBox<T> pCtrl) {
        super(pLabel, pCtrl);
//        setFocusTraversable(false);
        getControl().setFocusTraversable(true);
        //workaround, focus event is not called in checkcombobox impl. so the 
        //focus changed in the skin is not happening, needed to be reviewed in a later stage
        //awi: 20170209
        controlProperty.getValue().skinProperty().addListener(skinListener);
//        getControl().focusedProperty().addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                if(newValue){
//                    getComboBox().requestFocus();
//                }
//            }
//        });
        addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
//                if(getControl().isFocused()){
//                    event.consume();
                handleKeyEvent(event.getCode(), event);
//                    if(KeyCode.DOWN.equals(event.getCode())){
//                        showPopUp();
//                        handleKeyEvent(event.getCode());
//                        event.consume();
//                    }
//                    if(KeyCode.UP.equals(event.getCode())){
//                        showPopUp();
//                        handleKeyEvent(event.getCode());
//                        event.consume();
//                    }
//                    if(KeyCode.ENTER.equals(event.getCode())){
//                        showPopUp();
//                        handleKeyEvent(event.getCode());
//                        event.consume();
//                    }
//                }
            }

            private void handleKeyEvent(KeyCode code, Event event) {
                switch (code) {
                    case ENTER:
                        showPopUp();
                        selectFirst();
                        event.consume();
                        break;
                    case DOWN:
                        showPopUp();
                        selectDown();
                        event.consume();
                        break;
                    case UP:
                        showPopUp();
                        selectUp();
                        event.consume();
                        break;
                    case TAB:
                        getComboBox().hide();
                        break;
//                    case DELETE:
//                        getCheckModel().clearChecks();
//                        break;
                    default:
                        if (getComboBox().isShowing()) {
                            getComboBox().hide();
                        }

                }
            }

            private void selectFirst() {
                if (getListView().getSelectionModel().getSelectedItem() == null) {
                    getListView().getSelectionModel().select(0);
                    return;
                }
                getListView().getSelectionModel().select(!getCheckedItems().isEmpty() ? getCheckedItems().get(0) : null);
            }

            private void selectDown() {
                if (getListView().getSelectionModel().getSelectedItem() == null) {
                    getListView().getSelectionModel().select(0);
                    return;
                }
                int firstIndex = !getCheckedItems().isEmpty() ? getItems().indexOf(getCheckedItems().get(0)) : 0;
                for (int i = firstIndex; i <= getItems().size(); i++) {
                    if (!getCheckedItems().contains(getItems().get(i))) {
                        getListView().getSelectionModel().select(getItems().get(i));
                        return;
                    }
                }
                getListView().getSelectionModel().select(0);
            }

            private void selectUp() {
                if (getListView().getSelectionModel().getSelectedItem() == null) {
                    getListView().getSelectionModel().select(0);
                    return;
                }
                int firstIndex = !getCheckedItems().isEmpty() ? getItems().indexOf(getCheckedItems().get(0)) : 0;
                for (int i = firstIndex; i >= 0; i--) {
                    if (!getCheckedItems().contains(getItems().get(i))) {
                        getListView().getSelectionModel().select(getItems().get(i));
                        return;
                    }
                }
                getListView().getSelectionModel().select(0);
            }
        });
    }

    public LabeledCheckComboBox(String pLabel) {
        this(pLabel, new CpxCheckComboBox<T>());
    }

    @SuppressWarnings("unchecked")
    public LabeledCheckComboBox(String pLabel, T... items) {
        this(pLabel);
        setItems(items);
    }

    /**
     * shows popover of the underlaying combobox
     */
    public void showPopUp() {
        ComboBox<T> combo = getComboBox();
        combo.show();
    }

    private void initComboBox() {
        ComboBox<T> cb = comboboxProperty.get();
        cb.setFocusTraversable(false);
        cb.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    requestFocus();
                }
            }
        });
        cb.addEventFilter(ComboBoxBase.ON_SHOWING, new EventHandler<Event>() {
            @Override
            public void handle(Event t) {
                //request focus on showing the popup of the combobox
                //needs to be done to grab focus otherwise header highlighting is not working proberly
                //for process completion
                requestFocus();
            }
        });
        cb.skinProperty().addListener(new ChangeListener<Skin<?>>() {
            @Override
            public void changed(ObservableValue<? extends Skin<?>> observable, Skin<?> oldValue, Skin<?> newValue) {
                cb.skinProperty().removeListener(this);
                getCheckedItems().addListener(new ListChangeListener<T>() {
                    @Override
                    public void onChanged(ListChangeListener.Change<? extends T> c) {
                        if (c.next()) {
                            if (c.wasAdded()) {
                                T added = c.getAddedSubList().get(0);
//                                selectItem(getCheckedItems().indexOf(added));
                                selectItem(added);
                            }
                            if (c.wasRemoved()) {
                                T removed = c.getRemoved().get(0);
                                selectItem(removed);
//                                selectItem(getCheckedItems().indexOf(removed));
                            }
                        }
//                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    private void selectItem(T pItem) {
                        Platform.runLater(new Runnable() {
                            @Override
                            @SuppressWarnings("unchecked")
                            public void run() {
                                ((CpxComboBoxListViewSkin) newValue).getListView().getSelectionModel().select(pItem);
                            }
                        });
                    }
                });
                ((CpxComboBoxListViewSkin) newValue).getListView().getStyleClass().add(0, "stay-selected-list-view");
                ((CpxComboBoxListViewSkin) newValue).getListView().addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
                    private final KeyCombination keyTab = new KeyCodeCombination(KeyCode.TAB);
//                    final KeyCombination keyCtrlTab = new KeyCodeCombination(KeyCode.TAB, KeyCombination.CONTROL_ANY);

                    @Override
                    public void handle(KeyEvent event) {
                        if (keyTab.match(event)) {//||keyCtrlTab.match(event)) {
                            if (cb.isShowing()) {
                                //hide popup
                                //and trigger focus traverse 
                                cb.hide();
                                KeyEvent newEvent = new KeyEvent(
                                        null,
                                        null,
                                        KeyEvent.KEY_PRESSED,
                                        "",
                                        "\t",
                                        KeyCode.TAB,
                                        event.isShiftDown(),
                                        event.isControlDown(),
                                        event.isAltDown(),
                                        event.isMetaDown()
                                );
                                //simulate tab pressed on control to travse the chain to next item
                                Event.fireEvent(getControl(), newEvent);
                            }
                        }
                    }
                });
            }
        });
//        cb.setSkin(new CheckComboBoxBehaviorSkin(cb));
    }

    /**
     * @return get ComboBox Warning: this could change in future releases
     */
    protected ComboBox<T> getComboBox() {
//        if(combobox == null){
//            CheckComboBoxSkin obj = (CheckComboBoxSkin)getControl().getSkin();
//            combobox = (ComboBox)obj.getChildren().get(0);
//            combobox.setFocusTraversable(false);
//            ComboBoxListViewSkin cbSkin = (CpxComboBoxListViewSkin) combobox.getSkin();
//            combobox.setSkin(cbSkin);
//            combobox.setOnMouseClicked(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    getControl().requestFocus();
//                }
//            });

//            combobox.focusedProperty().addListener(new ChangeListener<Boolean>() {
//                @Override
//                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                    if(newValue){
//                        getControl().requestFocus();
//                    }
//                }
//            });
//            cbSkin.getListView().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
//                @Override
//                public void handle(KeyEvent event) {
//                    if(KeyCode.ENTER.equals(event.getCode())){
//                        int selectedIndex = cbSkin.getListView().getFocusModel().getFocusedIndex();
//                        if(selectedIndex<0){
//                            event.consume();
//                            return;
//                        }
////                        Platform.runLater(new Runnable() {
////                            @Override
////                            public void run() {
//                                if(getCheckModel().isChecked(selectedIndex)){
//                                    getCheckModel().clearCheck(selectedIndex);
//                                }else{
//                                    getCheckModel().check(selectedIndex);
//                                }
//                                if(!combobox.isShowing()){
//                                    combobox.show();
//                                }
//                                event.consume();
////                            }
////                        });
//                    }
//                    if(KeyCode.TAB.equals(event.getCode())){
//                        if (combobox.isShowing()) {
//                            //hide popup
//                            //and trigger focus traverse 
//                            combobox.hide();
//                            KeyEvent newEvent = new KeyEvent(
//                                    null,
//                                    null,
//                                    KeyEvent.KEY_PRESSED,
//                                    "",
//                                    "\t",
//                                    KeyCode.TAB,
//                                    event.isShiftDown(),
//                                    event.isControlDown(),
//                                    event.isAltDown(),
//                                    event.isMetaDown()
//                            );
//                            //simulate tab pressed on control to travse the chain to next item
//                            Event.fireEvent(getControl(), newEvent);
//                        }
//                    }
//                }
//            });
//        }
        return comboboxProperty.get();//combobox;
    }

    public void setConverter(StringConverter<T> stringConverter) {
        getControl().setConverter(stringConverter);
    }

    @SuppressWarnings("unchecked")
    public final void setItems(T... items) {
        getControl().getItems().addAll(items);
    }

    public void setItems(List<T> items) {
        getControl().getItems().addAll(items);
    }

    public List<T> getItems() {
        return getControl().getItems();
    }

    /**
     * Calls CheckComboBox.getItems.isEmpty and returns it
     *
     * @return Size of Items in the CheckComboBox
     */
    public boolean isEmpty() {
        return getControl().getItems().isEmpty();
    }

    /**
     * @return observable list of currently selected / checked items
     */
    public ObservableList<T> getCheckedItems() {
        return getControl().getCheckModel().getCheckedItems();
    }

    /**
     * @return current text displayed in the combobox
     */
    public String getCurrentDisplayedText() {
        if (comboboxProperty.get() == null) {
            return "";
        }
        return comboboxProperty.get().getButtonCell().getText();
    }

    /**
     * @return current text displayed in the combobox
     */
    public StringProperty getCurrentDisplayedTextProperty() {
        if (comboboxProperty.get() == null) {
            return new SimpleStringProperty("");
        }
        return comboboxProperty.get().getButtonCell().textProperty();
    }

    public IndexedCheckModel<T> getCheckModel() {
        return getControl().getCheckModel();
    }

    /**
     * helper mothod to register tooltip on checkedcombobox keep in mind that
     * tooltips are not shown on disabled controls! should be set in
     * constructor, but in request creation it is set differently and i do not
     * want to mess with that messy because this control have control inside, so
     * tooltip needs to be extended needs refactoring Awi:20170406
     */
    public void registerTooltip() {

        getCheckedItems().addListener(new ListChangeListener<T>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends T> c) {
                if (!c.getList().isEmpty()) {
                    //messy - wait till cb is registered and than set tooltip
                    if (comboboxProperty.get() == null) {
                        comboboxProperty.addListener(new ChangeListener<ComboBox<?>>() {
                            @Override
                            public void changed(ObservableValue<? extends ComboBox<?>> observable, ComboBox<?> oldValue, ComboBox<?> newValue) {
                                BasicTooltip tip = new BasicTooltip(200, 5000, 100, true);
                                tip.textProperty().bindBidirectional(getCurrentDisplayedTextProperty());
                                setTooltip(tip);
                                comboboxProperty.get().setTooltip(tip);
                            }
                        });
                        return;
                    }
                    BasicTooltip tip = new BasicTooltip(200, 5000, 100, true);
                    tip.textProperty().bindBidirectional(getCurrentDisplayedTextProperty());
                    setTooltip(tip);
                    comboboxProperty.get().setTooltip(tip);
                } else {
                    setTooltip(null);
                    comboboxProperty.get().setTooltip(null);
                }
            }
        });
    }

    /**
     * select item by its "id" field
     *
     * @param pId id of the field to select
     * @throws NoSuchFieldException thrown if T has no field with name id
     * @throws IllegalAccessException thrown if field id is private
     */
    public void checkId(long pId) throws NoSuchFieldException, IllegalAccessException {
        for (T item : getItems()) {
            Field field;
            try {
                field = item.getClass().getDeclaredField("id");
                field.setAccessible(true);
            } catch (NoSuchFieldException ex) {
                LOG.log(Level.SEVERE, "Field with this id does not exist: " + pId, ex);
                field = item.getClass().getSuperclass().getDeclaredField("id");
                field.setAccessible(true);
            }
            long id = (long) field.get(item);
            if (id == pId) {
                getCheckModel().check(item);
            }
        }
    }

    /**
     *
     * @param ids to check given Ids.
     * @throws IllegalAccessException IllegalAccessException
     * @throws NoSuchFieldException NoSuchFieldException
     */
    public void checkIds(Long... ids) throws IllegalAccessException, NoSuchFieldException {
        for (Long id : ids) {
            checkId(id);
        }
    }

    /**
     * @return combobox set in the checked combobox
     */
    public ObjectProperty<ComboBox<T>> getComboboxProperty() {
        return comboboxProperty;
    }

//    private class CheckComboBoxBehaviorSkin extends ComboBoxListViewSkin<T> {
//
//        public CheckComboBoxBehaviorSkin(ComboBox<T> combobox) {
//            super(combobox);
//            final ListView<T> listView = getListView();
//            listView.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
//                @Override
//                public void handle(KeyEvent event) {
//                    if (KeyCode.ENTER.equals(event.getCode())) {
//                        int selectedIndex = listView.getFocusModel().getFocusedIndex();
//                        if (selectedIndex < 0) {
//                            event.consume();
//                            return;
//                        }
////                        Platform.runLater(new Runnable() {
////                            @Override
////                            public void run() {
//                        if (getCheckModel().isChecked(selectedIndex)) {
//                            getCheckModel().clearCheck(selectedIndex);
//                        } else {
//                            getCheckModel().check(selectedIndex);
//                        }
//                        if (!combobox.isShowing()) {
//                            combobox.show();
//                        }
//                        event.consume();
////                            }
////                        });
//                    }
//                    if (KeyCode.TAB.equals(event.getCode())) {
//                        if (combobox.isShowing()) {
//                            //hide popup
//                            //and trigger focus traverse 
//                            combobox.hide();
//                            KeyEvent newEvent = new KeyEvent(
//                                    null,
//                                    null,
//                                    KeyEvent.KEY_PRESSED,
//                                    "",
//                                    "\t",
//                                    KeyCode.TAB,
//                                    event.isShiftDown(),
//                                    event.isControlDown(),
//                                    event.isAltDown(),
//                                    event.isMetaDown()
//                            );
//                            //simulate tab pressed on control to travse the chain to next item
//                            Event.fireEvent(getControl(), newEvent);
//                        }
//                    }
//                }
//            });
//        }
//
//    }
}
