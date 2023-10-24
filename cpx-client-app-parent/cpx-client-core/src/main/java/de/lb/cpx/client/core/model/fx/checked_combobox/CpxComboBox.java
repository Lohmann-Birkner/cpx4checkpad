/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.checked_combobox;

import de.lb.cpx.model.enums.CpxEnumInterface;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;
import de.lb.cpx.wm.model.enums.WmEventTypeEn;
import java.util.Arrays;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Skin;
import javafx.util.StringConverter;

/**
 *
 * @author Dirk Niemeier
 * @param <T> type
 */
public class CpxComboBox<T> extends ComboBox<T> {

    @Override
    protected Skin<?> createDefaultSkin() {
        //ObservableList<Node> ch = sk.getChildren();
        //StackPane pane = (StackPane) ch.get(0);
        //ComboBox cb = (ComboBoxListViewSkin) ch.get(1);
        CpxComboBoxListViewSkin<T> sk = new CpxComboBoxListViewSkin<>(this);
        sk.setHideOnClick(true);
        setSkin(sk);
        return sk;
    }

    public static CpxComboBox<Enum<?>> getInstance(final Class<?> pDataType) {
        @SuppressWarnings("unchecked")
        Class<Enum<?>> myClass = (Class<Enum<?>>) pDataType;
        CpxComboBox<Enum<?>> comboBox = new CpxComboBox<>();
        Enum<?>[] items = myClass.getEnumConstants();

        //Translation/localisation of enumerations
        comboBox.setConverter(new StringConverter<Enum<?>>() {
            @Override
            public String toString(Enum<?> element) {
                //System.out.println("TOSTRING");
                if (element == null) {
                    return "";
                }
                if (element instanceof CpxEnumInterface) {
                    return element.toString();
                } else {
                    return Lang.get(element.toString()).value;
                }
                //return element .toString(cpxLanguage);
            }

            @Override
            public Enum<?> fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });

        comboBox.getItems().addAll(Arrays.asList(items));
        //comboBox.getCheckModel().check("test");
        return comboBox;
    }

    // this method is used to get only abbreviations, instead of entire event name sentence
//    public static CpxComboBox<WmEventTypeEn> getInstanceEventType() {
//        Class<WmEventTypeEn> myClass = WmEventTypeEn.class;
//        CpxComboBox<WmEventTypeEn> comboBox = new CpxComboBox<>();
//        WmEventTypeEn[] items = myClass.getEnumConstants();
//
//        //Translation/localisation of enumerations
//        comboBox.setConverter(new StringConverter<WmEventTypeEn>() {
//            @Override
//            public String toString(WmEventTypeEn element) {
//                //System.out.println("TOSTRING");
//                Translation trans = ((CpxEnumInterface) element).getTranslation();
//                if (trans.has_abbreviation) {
//                    return trans.abbreviation;  //gives only abbreviation
//                }
//                return trans.value; // gives entire sentence
//                //return element .toString(cpxLanguage);
//            }
//
//            @Override
//            public WmEventTypeEn fromString(String string) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//        });
//
//        comboBox.getItems().addAll(Arrays.asList(items));
//        return comboBox;
//    }
    public static CpxComboBox<CpxEnumInterface<?>> getInstanceEventType() {
        Class<WmEventTypeEn> myClass = WmEventTypeEn.class;
        CpxComboBox<CpxEnumInterface<?>> comboBox = new CpxComboBox<>();
        WmEventTypeEn[] items = myClass.getEnumConstants();

        final ChangeListener<Skin<?>> changeListener = new ChangeListener<Skin<?>>() {
            @Override
            public void changed(ObservableValue<? extends Skin<?>> ov, Skin<?> t, Skin<?> t1) {
//                CheckComboBoxSkin skin = (CheckComboBoxSkin) t1;
//                ObservableList ch = skin.getChildren();
//                ch.add(0, new TextField("TEST"));
//                Node n = t1.getNode();
//                LOG.log(Level.INFO, String.valueOf(n));
                comboBox.skinProperty().removeListener(this);
            }
        };
        comboBox.skinProperty().addListener(changeListener);

        // Translation/localisation of enumerations 
        comboBox.setConverter(new StringConverter<CpxEnumInterface<?>>() {
            @Override
            public String toString(CpxEnumInterface<?> element) {
                //System.out.println("TOSTRING");
                Translation trans = element.getTranslation();
                if (trans.hasAbbreviation) {
                    return trans.abbreviation;  //gives only abbreviation
                }
                return trans.value; // gives entire sentence
                //return element .toString(cpxLanguage);
            }

            @Override
            public CpxEnumInterface<?> fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });

//        final CpxEnumInterface selectAllItem = CpxEnumInterface.getSelectAllItem();
//        final CpxEnumInterface selectAllItem = getSelectAllItem();
//        comboBox.getItems().add(selectAllItem);
//        comboBox.getItems().add(WmEventTypeEn.values());
//        comboBox.getItems().set(0, WmEventTypeEn.values());
        comboBox.getItems().addAll(Arrays.asList(items));

        // sort items from the second element.
        comboBox.getItems().subList(1, comboBox.getItems().size()).sort((o1, o2) -> {
            return o1.getTranslation().value.compareToIgnoreCase(o2.getTranslation().value);
        });

//        int itemIndex = comboBox.getSelectionModel().getSelectedIndex(); // comboBox.getCheckModel().getItemIndex(selectAllItem);
//
//        comboBox.getCheckModel().getCheckedItems().addListener(new ListChangeListener<CpxEnumInterface>() {
//            @Override
//            public void onChanged(ListChangeListener.Change<? extends CpxEnumInterface> change) {
//                ObservableList<CpxEnumInterface> checkedItems = comboBox.getCheckModel().getCheckedItems();
////                if (checkedItems.containsAll(comboBox.getItems().subList(1, comboBox.getItems().size())) && !comboBox.getCheckModel().isChecked(selectAllItem)) {
////                    comboBox.getCheckModel().check(selectAllItem);
////                }
////                if (comboBox.getCheckModel().isChecked(selectAllItem) && !checkedItems.containsAll(comboBox.getItems().subList(1, comboBox.getItems().size()))) {
////                    comboBox.getCheckModel().clearCheck(selectAllItem);
////                }
//
//                while (change.next()) {
//                    if (change.wasAdded()) {
//                        List<? extends CpxEnumInterface> addedSubList = change.getAddedSubList();
//                        addedSubList.forEach(new Consumer<CpxEnumInterface>() {
//                            @Override
//                            public void accept(CpxEnumInterface t) {
//                                // if "selectAll" checkBox is checked
//                                if (t != null && t.equals(selectAllItem)) {
////                                    comboBox.getCheckModel().checkAll();
////                                    comboBox.getCheckModel().check(itemIndex);
//                                    // check all (remaining) checkboxes
//                                    comboBox.getItems().forEach(new Consumer<CpxEnumInterface>() {
//                                        private int i = 1;
//
//                                        @Override
//                                        public void accept(CpxEnumInterface t) {
//                                            if (comboBox.getItems().get(i).equals(t)) {
//                                                if (!comboBox.getCheckModel().isChecked(t)) {
//                                                    comboBox.getCheckModel().check(t);
//                                                } else {
//                                                    // if already checked, don't do anything!
//                                                }
//                                                i++;
//                                            }
//                                        }
//                                    });
//                                } else if (t != null && !t.equals(selectAllItem)) {
////                                            comboBox.getCheckModel().getCheckedIndices().
//                                    CpxEnumInterface get = comboBox.getItems().get(comboBox.getItems().indexOf(t));
//
//                                    if (checkedItems.containsAll(comboBox.getItems().subList(1, comboBox.getItems().size())) && !comboBox.getCheckModel().isChecked(selectAllItem)) {
//                                        comboBox.getCheckModel().check(selectAllItem);
//                                    }
//
//                                }
//                            }
//                        });
//                    }
//                    if (change.wasRemoved()) {
//                        List<? extends CpxEnumInterface> removedList = change.getRemoved();
//                        removedList.forEach(new Consumer<CpxEnumInterface>() {
//                            @Override
//                            public void accept(CpxEnumInterface t) {
//                                // if "selectAll" checkBox is unchecked
//                                if (t != null && t.equals(selectAllItem)) {
////                                    comboBox.getCheckModel().getCheckedItems().clear();
//                                    comboBox.getItems().forEach(new Consumer<CpxEnumInterface>() {
//                                        private int i = 1;
//
//                                        @Override
//                                        public void accept(CpxEnumInterface t) {
//                                            if (comboBox.getItems().get(i).equals(t)) {
//                                                if (comboBox.getCheckModel().isChecked(t)) {
//                                                    comboBox.getCheckModel().clearCheck(t);
//                                                } else {
//                                                }
//                                                i++;
//                                            }
//                                        }
//                                    });
//                                } else if (t != null && !t.equals(selectAllItem)) {
//                                    if (comboBox.getCheckModel().isChecked(selectAllItem)) {
//                                        comboBox.getCheckModel().clearCheck(selectAllItem);
//                                        comboBox.getItems().subList(1, comboBox.getItems().size()).forEach(new Consumer<CpxEnumInterface>() {
//                                            @Override
//                                            public void accept(CpxEnumInterface t1) {
//                                                if (!t.equals(t1)) {
//                                                    comboBox.getCheckModel().check(t1);
//                                                }
//                                            }
//                                        });
//
//                                    }
//                                }
//                            }
//                        });
//                    }
//                }
//            }
//        });
        return comboBox;
    }

//    public static CpxEnumInterface getSelectAllItem() {
//        CpxEnumInterface item = new CpxEnumInterface() {
//            @Override
//            public String getLangKey() {
//                return "Alle";
//            }
//
//            @Override
//            public Translation getTranslation() {
//                return new Translation("Alle");
//            }
//
//            @Override
//            public Translation getTranslation(Object... pParams) {
//                return new Translation("Alle");
//            }
//
//            @Override
//            public String getViewId() {
//                return "Alle";
//            }
//
//            public CpxEnumInterface getEnum(String value) {
//                return this;
//            }
//
//            @Override
//            public boolean isViewRelevant() {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//
//            @Override
//            public String getIdStr() {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//
//            @Override
//            public int getIdInt() {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//
//            @Override
//            public Object getId() {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//        };
//        return item;
//    }
    private final ObjectProperty<T> previousItem = new SimpleObjectProperty<>();

    public void clear() {
        getSelectionModel().clearSelection();
        previousItem.set(null);
        setValue(null); //https://bugs.openjdk.java.net/browse/JDK-8097244
    }

    public CpxComboBox() {
//        final BooleanProperty installed = new SimpleBooleanProperty(false);
//        this.addEventHandler(ComboBox.ON_SHOWING, event -> {
//            if (!installed.get()) {
//                installed.set(true);
//                ClipboardEnabler.installClipboardToScene(this.getScene());
//            }
//        });
        //getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //store previous selected value before showing combobox
        this.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) {
                @SuppressWarnings("unchecked")
                final CpxComboBoxListViewSkin<T> sk = (CpxComboBoxListViewSkin<T>) getSkin();
                if (newValue) {
                    //store selected value on show
                    previousItem.set(getSelectionModel().getSelectedItem());
                } else {
                    if (getSelectionModel().getSelectedItem() == null
                            && previousItem.get() != null
                            && previousItem.get() != getSelectionModel().getSelectedItem()) {
                        sk.getFilterField().setText(null);
                        getSelectionModel().select(previousItem.get());
                        //updateDisplayNode();
                    }
                }
            }
        });
//        addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent event) {
//                if (KeyCode.ENTER.equals(event.getCode())) {
//                    if (isShowing()) {
//                        //getSelectionModel().select();
//                        event.consume();
//                        hide();
//                    }
//                }
//            }
//        });
    }

}
