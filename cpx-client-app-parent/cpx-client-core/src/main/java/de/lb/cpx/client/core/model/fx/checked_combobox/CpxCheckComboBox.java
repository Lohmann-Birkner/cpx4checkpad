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
import impl.org.controlsfx.skin.CheckComboBoxSkin;
import java.util.Arrays;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;

/**
 *
 * @author Dirk Niemeier
 * @param <T> type
 */
@SuppressWarnings("unchecked")
public class CpxCheckComboBox<T> extends CheckComboBox<T> {

    @Override
    protected Skin<?> createDefaultSkin() {
        CheckComboBoxSkin<T> sk = (CheckComboBoxSkin<T>) super.createDefaultSkin();
        ObservableList<Node> ch = sk.getChildren();
        ComboBox<T> cb = (ComboBox<T>) ch.get(0);
        final CpxComboBoxListViewSkin<T> sk2 = new CpxComboBoxListViewSkin<>(cb);
        sk2.setHideOnClick(false);
        addListener(sk2);
        cb.setSkin(sk2);
        return sk;
    }

    public CpxComboBoxListViewSkin<T> getCpxComboBoxListViewSkin() {
        if (getSkin() == null) {
            setSkin(createDefaultSkin());
        }
        ObservableList<Node> ch = ((SkinBase) getSkin()).getChildren();
        ComboBox<T> cb = (ComboBox<T>) ch.get(0);
        return (CpxComboBoxListViewSkin<T>) cb.getSkin();
    }

    public static CpxCheckComboBox<Enum<?>> getInstance(final Class<?> pDataType) {
        Class<Enum<?>> myClass = (Class<Enum<?>>) pDataType;
        CpxCheckComboBox<Enum<?>> comboBox = new CpxCheckComboBox<>();
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
//    public static CpxCheckComboBox<WmEventTypeEn> getInstanceEventType() {
//        Class<WmEventTypeEn> myClass = WmEventTypeEn.class;
//        CpxCheckComboBox<WmEventTypeEn> cpxCheckComboBox = new CpxCheckComboBox<>();
//        WmEventTypeEn[] items = myClass.getEnumConstants();
//
//        //Translation/localisation of enumerations
//        cpxCheckComboBox.setConverter(new StringConverter<WmEventTypeEn>() {
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
//        cpxCheckComboBox.getItems().addAll(Arrays.asList(items));
//        return cpxCheckComboBox;
//    }
    public static CpxCheckComboBox<CpxEnumInterface<?>> getInstanceEventType() {
        Class<WmEventTypeEn> myClass = WmEventTypeEn.class;
        CpxCheckComboBox<CpxEnumInterface<?>> cpxCheckComboBox = new CpxCheckComboBox<>();
        WmEventTypeEn[] items = myClass.getEnumConstants();

        // Translation/localisation of enumerations 
        cpxCheckComboBox.setConverter(new StringConverter<CpxEnumInterface<?>>() {
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

//        final CpxEnumInterface<?> selectAllItem = getSelectAllItem();
//        cpxCheckComboBox.getItems().add(selectAllItem);
        cpxCheckComboBox.getItems().addAll(Arrays.asList(items));

        // sort items from the second element.
//        cpxCheckComboBox.getItems().subList(1, cpxCheckComboBox.getItems().size()).sort((o1, o2) -> {
//            return o1.getTranslation().value.compareToIgnoreCase(o2.getTranslation().value);
//        });
        cpxCheckComboBox.getItems().sort((o1, o2) -> {
            return o1.getTranslation().value.compareToIgnoreCase(o2.getTranslation().value);
        });

        /*
        cpxCheckComboBox.getCheckModel().getCheckedItems().addListener(new ListChangeListener<CpxEnumInterface<?>>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends CpxEnumInterface<?>> change) {

                ObservableList<CpxEnumInterface<?>> checkedItems = cpxCheckComboBox.getCheckModel().getCheckedItems();

                while (change.next()) {
                    if (change.wasAdded()) {
                        if (change.getAddedSubList().contains(selectAllItem)) {
                            cpxCheckComboBox.getItems().forEach(new Consumer<CpxEnumInterface<?>>() {

                                @Override
                                public void accept(CpxEnumInterface<?> t) {
                                    if (!cpxCheckComboBox.getCheckModel().isChecked(t)) {
                                        cpxCheckComboBox.getCheckModel().check(t);
                                    }
                                }
                            });
                        } else if (checkedItems.containsAll(cpxCheckComboBox.getItems().subList(1, cpxCheckComboBox.getItems().size())) && !cpxCheckComboBox.getCheckModel().isChecked(selectAllItem)) {
                            cpxCheckComboBox.getCheckModel().check(selectAllItem);
                        }

//                        List<? extends CpxEnumInterface> addedSubList = change.getAddedSubList();
//                        addedSubList.forEach(new Consumer<CpxEnumInterface>() {
//                            @Override
//                            public void accept(CpxEnumInterface t) {
//                                // if "selectAll" checkBox is checked
//                                if (t != null && t.equals(selectAllItem)) {
////                                    cpxCheckComboBox.getCheckModel().checkAll();
////                                    cpxCheckComboBox.getCheckModel().check(itemIndex);
//                                    // check all (remaining) checkboxes
//                                    cpxCheckComboBox.getItems().forEach(new Consumer<CpxEnumInterface>() {
//                                        private int i = 1;
//
//                                        @Override
//                                        public void accept(CpxEnumInterface t) {
//                                            if (cpxCheckComboBox.getItems().get(i).equals(t)) {
//                                                if (!cpxCheckComboBox.getCheckModel().isChecked(t)) {
//                                                    cpxCheckComboBox.getCheckModel().check(t);
//                                                } else {
//                                                    // if already checked, don't do anything!
//                                                }
//                                                i++;
//                                            }
//                                        }
//                                    });
//                                } else if (t != null && !t.equals(selectAllItem)) {
////                                            cpxCheckComboBox.getCheckModel().getCheckedIndices().
//                                    CpxEnumInterface get = cpxCheckComboBox.getItems().get(cpxCheckComboBox.getItems().indexOf(t));
//
//                                    if (checkedItems.containsAll(cpxCheckComboBox.getItems().subList(1, cpxCheckComboBox.getItems().size())) && !cpxCheckComboBox.getCheckModel().isChecked(selectAllItem)) {
//                                        cpxCheckComboBox.getCheckModel().check(selectAllItem);
//                                    }
//
//                                }
//                            }
//                        });
                    }
                    if (change.wasRemoved()) {
//                        if (change.getRemoved().contains(selectAllItem)) {
//                            cpxCheckComboBox.getItems().forEach(new Consumer<CpxEnumInterface>() {
//
//                                @Override
//                                public void accept(CpxEnumInterface t) {
//                                    if (cpxCheckComboBox.getCheckModel().isChecked(t)) {
//                                        cpxCheckComboBox.getCheckModel().clearCheck(t);
//                                    }
//                                }
//                            });
//                        } else if (cpxCheckComboBox.getCheckModel().isChecked(selectAllItem)) {
//                           // clear first one 
//                           cpxCheckComboBox.getCheckModel().clearCheck(selectAllItem);
//                           // then  add remaining
//                            cpxCheckComboBox.getItems().subList(1, cpxCheckComboBox.getItems().size()).forEach(new Consumer<CpxEnumInterface>() {
//                                @Override
//                                public void accept(CpxEnumInterface t1) {
//                                    if (!change.equals(t1)) {
//                                        cpxCheckComboBox.getCheckModel().check(t1);
//                                    }
//                                }
//                            });
//                        }   

                        List<? extends CpxEnumInterface<?>> removedList = change.getRemoved();
                        removedList.forEach(new Consumer<CpxEnumInterface<?>>() {
                            @Override
                            public void accept(CpxEnumInterface<?> t) {
                                // if "selectAll" checkComboBox is unchecked
                                if (t != null && t.equals(selectAllItem)) {
//                                    cpxCheckComboBox.getCheckModel().getCheckedItems().clear();
                                    cpxCheckComboBox.getItems().forEach(new Consumer<CpxEnumInterface<?>>() {
                                        private int i = 1;

                                        @Override
                                        public void accept(CpxEnumInterface<?> t) {
                                            if (cpxCheckComboBox.getItems().get(i).equals(t)) {
                                                if (cpxCheckComboBox.getCheckModel().isChecked(t)) {
                                                    cpxCheckComboBox.getCheckModel().clearCheck(t);
                                                } else {
                                                }
                                                i++;
                                            }
                                        }
                                    });
                                } else if (t != null && !t.equals(selectAllItem)) {
                                    if (cpxCheckComboBox.getCheckModel().isChecked(selectAllItem)) {
                                        cpxCheckComboBox.getCheckModel().clearCheck(selectAllItem);
                                        // this is much time-consuming (any other way?)
                                        cpxCheckComboBox.getItems().subList(1, cpxCheckComboBox.getItems().size()).forEach(new Consumer<CpxEnumInterface<?>>() {
                                            @Override
                                            public void accept(CpxEnumInterface<?> t1) {
                                                if (!t.equals(t1)) {
                                                    cpxCheckComboBox.getCheckModel().check(t1);
                                                }
                                            }
                                        });

                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
         */
        return cpxCheckComboBox;
    }

    public static CpxEnumInterface<?> getSelectAllItem() {
        CpxEnumInterface<?> item = new CpxEnumInterface<>() {
            @Override
            public String getLangKey() {
                return "Alle";
            }

            @Override
            public Translation getTranslation() {
                return new Translation("Alle");
            }

            @Override
            public Translation getTranslation(Object... pParams) {
                return new Translation("Alle");
            }

            @Override
            public String getViewId() {
                return "Alle";
            }

            @Override
            public boolean isViewRelevant() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public String getIdStr() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public int getIdInt() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public Object getId() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        return item;
    }

    public CpxCheckComboBox() {
        super();
//        final BooleanProperty installed = new SimpleBooleanProperty(false);
//        this.addEventHandler(ComboBox.ON_SHOWING, event -> {
//            if (!installed.get()) {
//                installed.set(true);
//                ClipboardEnabler.installClipboardToScene(this.getScene());
//            }
//        });
        //getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void addListener(CpxComboBoxListViewSkin<T> pSkin) {
        final ComboBox<T> cb = ((ComboBox<T>) pSkin.getSkinnable());
        pSkin.getListView().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
//                        if (KeyCode.UP.equals(event.getCode())){
//                            if(((CpxComboBoxListViewSkin) newValue).getListView().getSelectionModel().getSelectedIndex()==-1){
//                                ((CpxComboBoxListViewSkin) newValue).getListView().getSelectionModel().selectFirst();
//                            }
//                        }
//                        if (KeyCode.DOWN.equals(event.getCode())){
//                            if(((CpxComboBoxListViewSkin) newValue).getListView().getSelectionModel().getSelectedIndex()==-1){
//                                ((CpxComboBoxListViewSkin) newValue).getListView().getSelectionModel().selectFirst();
//                            }
//                        }
                if (KeyCode.ENTER.equals(event.getCode())
                        || KeyCode.SPACE.equals(event.getCode())) {
                    //int selectedIndex = pSkin.getListView().getFocusModel().getFocusedIndex();
                    toggleCheck(cb, event);
//                            }
//                        });
                } else if (event.getCode() == KeyCode.DELETE) {
                    getCheckModel().clearChecks();
                    event.consume();
                }
            }
        });
        pSkin.getListView().addEventFilter(MouseEvent.MOUSE_CLICKED, (event) -> {
            if (!(event.getTarget() instanceof Pane)) {
                //select or unselect item from checkcombobox even if user does not click combobox itself but label or cell
                toggleCheck(cb, event);
                pSkin.getListView().requestFocus();
            }
        });
    }

    private void toggleCheck(final ComboBox<T> cb, final Event event) {
        int selIndex = cb.getSelectionModel().getSelectedIndex();
        //int selectedIndex = pSkin.getListView().getSelectionModel().getSelectedIndex();
        if (selIndex < 0) {
            event.consume();
            return;
        }
        Object item = cb.getItems().get(selIndex);
        int selectedIndex = getItems().indexOf(item);
//                        Platform.runLater(new Runnable() {
//                            @Override
//                            public void run() {
        if (getCheckModel().isChecked(selectedIndex)) {
            getCheckModel().clearCheck(selectedIndex);
        } else {
            getCheckModel().check(selectedIndex);
        }
        if (!cb.isShowing()) {
            cb.show();
        }
        event.consume();
    }

}
