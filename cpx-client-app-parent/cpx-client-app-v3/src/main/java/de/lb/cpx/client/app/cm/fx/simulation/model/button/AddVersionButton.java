/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.cm.fx.simulation.model.button;

import com.google.common.base.Objects;
import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.cm.fx.simulation.model.VersionContent;
import de.lb.cpx.client.app.cm.fx.simulation.model.VersionManager;
import de.lb.cpx.client.core.model.fx.comparablepane.ComparableContent;
import de.lb.cpx.client.core.model.fx.popover.AutoFitPopOver;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.client.core.util.VersionStringConverter;
import de.lb.cpx.model.TCaseDetails;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.controlsfx.control.PopOver;
import org.controlsfx.glyphfont.FontAwesome;

/**
 * Button to add Version in case simulation
 *
 * @author wilde
 * @param <E> type of comarable content
 */
public class AddVersionButton<E extends ComparableContent<TCaseDetails>> extends Button {

    private static final Logger LOG = Logger.getLogger(AddVersionButton.class.getName());

    private static final boolean OLD_MODE = false;
    private final VersionManager manager;

    /**
     * creates new instance
     *
     * @param pManager version manager to handle db access
     */
    public AddVersionButton(VersionManager pManager) {
        super();
        getStyleClass().add("cpx-icon-button");
        manager = pManager;
        setMode(OLD_MODE);
    }

    public void afterVersionModified() {
    }

    private void setMode(boolean pOldMode) {
        if (pOldMode) {
            setUpOldMode();
        } else {
            setUpNewMode();
        }
    }

    private void setUpOldMode() {
        setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.PLUS));
        setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                long start = System.currentTimeMillis();
                //gets the first unselected/not displayed case details
                TCaseDetails details = manager.getFirstUnSelectedLocal();
                if (details == null) {
                    MainApp.showErrorMessageDialog("Can not show CaseDetails!");
                    return;
                }
                //create and append to the list
                VersionContent content = manager.createAndAddVersionContent(details);
                LOG.log(Level.FINE, "add new content {0} in {1}", new Object[]{content.getVersionName(), System.currentTimeMillis() - start});
            }
        });

//      handle disable of button
        disableProperty().bind(Bindings.or(manager.getAllDisplayedProperty(), Bindings.equal(3, Bindings.size(manager.getManagedVersions()))));
    }
    private AutoFitPopOver popover;

    private void setUpNewMode() {
        setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.CARET_DOWN).size(20));
        setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (popover == null) {
                    popover = new AutoFitPopOver();
                    popover.setDetachable(false);
                    popover.setArrowLocation(PopOver.ArrowLocation.RIGHT_CENTER);
//                    popover.setOnHiding(new EventHandler<WindowEvent>() {
//                        @Override
//                        public void handle(WindowEvent t) {
//                            LOG.info("destroy popover content");
//                            Node node = popover.getContentNode();
//                            if(node instanceof AddVersionButton.PopOverContent){
//                                ((PopOverContent)node).destroy();
//                            }
//                            popover.setContentNode(null);
//                            node = null;
//                            popover.setOnHiding(null);

//                            popover.destory();
//                            popover = null;
//                        }
//                    });
                }
                if (!popover.isShowing()) {
                    popover.setContentNode(getPopOverContent());
                    popover.show(AddVersionButton.this);
                }
            }

            private Node getPopOverContent() {
//                VersionDisplayNode externs = new VersionDisplayNode("KIS-Version(en)", new ArrayList<>(manager.getAllAvailableExterns()),1);
//                VersionDisplayNode locals = new VersionDisplayNode("CP-Version(en)", new ArrayList<>(manager.getAllAvailableLocals()),2);
//                HBox container = new HBox(5.0,externs,locals);
//                return container;
                return new PopOverContent();
            }
        });
    }

    protected class PopOverContent extends HBox {

        public PopOverContent() {
            super(5.0);
            VersionDisplayNode externs = new VersionDisplayNode("KIS-Version(en)", new ArrayList<>(manager.getAllAvailableExterns()), 1);
            VersionDisplayNode locals = new VersionDisplayNode("CP-Version(en)", new ArrayList<>(manager.getAllAvailableLocals()), 2);
            getChildren().addAll(externs, locals);
        }

        @SuppressWarnings("unchecked")
        public void destroy() {
            for (Node child : getChildren()) {
                if (child instanceof AddVersionButton.VersionDisplayNode) {
                    ((VersionDisplayNode) child).destroy();
                }
            }
            getChildren().clear();
        }
    }

    protected class VersionDisplayNode extends VBox {

        private static final double FIXED_CELL_SIZE = 27.0;
        private static final int MAX_LIST_VIEW_HEIGHT = 200;
        private static final double MAX_LIST_VIEW_WIDTH = 150.0d;

        private ListChangeListener<TCaseDetails> SELECTED_INDEX_LISTENER = new ListChangeListener<TCaseDetails>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends TCaseDetails> change) {
                if (change.next()) {
                    LOG.log(Level.FINE, "selected(added {0},removed {1} ): {2}", new Object[]{change.getAddedSize(), change.getRemovedSize(), change.getList().stream().map((t) -> {
                        return String.valueOf(t.getCsdVersion());
                    }).collect(Collectors.joining(", "))});
//                    if(maxItems == 1){
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            handleChange(change);
                        }
                    });
//                    }else{
//                        handleChange(change);
//                    }
                }
            }

            private void handleChange(ListChangeListener.Change<? extends TCaseDetails> change) {
                if (change.wasAdded()) {
                    for (TCaseDetails details : change.getAddedSubList()) {
                        manager.createAndAddVersionContent(details);
                        manager.markAsDisplayed(details);
                    }
                    if ((change.getList().size() > maxItems)) {
                        int addedSize = change.getAddedSubList().size();
                        for (int i = 0; i < addedSize; i++) {
                            change.getList().remove(i);
                        }
                    }
                }
                if (change.wasRemoved()) {
                    for (TCaseDetails details : change.getRemoved()) {
                        manager.unMarkAsDisplayed(details);
                        VersionContent version = manager.getVersionContentForDetails(details);
                        manager.removeFromManagedVersions(version);
                    }
                }
                afterVersionModified();
            }
        };
        private Callback<ListView<TCaseDetails>, ListCell<TCaseDetails>> DEFAULT_CELL_FACTORY = new Callback<ListView<TCaseDetails>, ListCell<TCaseDetails>>() {
            @Override
            public ListCell<TCaseDetails> call(ListView<TCaseDetails> p) {
                VersionDisplayCell cell = new VersionDisplayCell();
                cells.add(cell);
                return cell;
            }
        };

        private final ObservableList<TCaseDetails> selectedIndexes = FXCollections.observableArrayList();
        private int maxItems;
        private ListView<TCaseDetails> listview;

        public VersionDisplayNode(String pTitle, List<TCaseDetails> pDetails, int pMaxItems) {
            super(5, new Label(pTitle));
            //keep sure that items can be selected
            if (pMaxItems < 1) {
                throw new IllegalArgumentException("MaxItems can not be smaller than 1!");
            }
            maxItems = pMaxItems;
            getStyleClass().add("version-selector");
            setPadding(new Insets(5));
            setFillWidth(true);

            listview = new ListView<>(FXCollections.observableArrayList(pDetails));
            listview.setMinWidth(MAX_LIST_VIEW_WIDTH);
            listview.setMaxWidth(MAX_LIST_VIEW_WIDTH);
            listview.setFixedCellSize(FIXED_CELL_SIZE);
            listview.setMaxHeight(Math.min((listview.getItems().size() * listview.getFixedCellSize()), MAX_LIST_VIEW_HEIGHT));
            listview.getStyleClass().add("no-selection-list-view ");
            listview.setCellFactory(DEFAULT_CELL_FACTORY);
            getChildren().add(listview);
            for (TCaseDetails details : pDetails) {
                if (manager.isDisplayed(details)) {
                    addToSelectedIndexes(details);
                }
            }
            selectedIndexes.addListener(SELECTED_INDEX_LISTENER);
        }
        private List<VersionDisplayCell> cells = new ArrayList<>();

        public void destroy() {
            selectedIndexes.removeListener(SELECTED_INDEX_LISTENER);
            SELECTED_INDEX_LISTENER = null;
            selectedIndexes.clear();
            getChildren().clear();
            listview.getItems().clear();
            listview.setCellFactory(null);
            DEFAULT_CELL_FACTORY = null;
            listview = null;
            for (VersionDisplayCell cell : cells) {
                cell.destroy();
            }
            cells.clear();
        }

        private void removeFromSelectedIndexes(TCaseDetails item) {
            Iterator<TCaseDetails> it = selectedIndexes.iterator();
            while (it.hasNext()) {
                TCaseDetails next = it.next();
                if (Objects.equal(next.getId(), item.getId())) {
                    it.remove();
                    return;
                }
            }
        }

        private boolean hasSelectedIndex(TCaseDetails item) {
            Iterator<TCaseDetails> it = selectedIndexes.iterator();
            while (it.hasNext()) {
                TCaseDetails next = it.next();
                if (Objects.equal(next.getId(), item.getId())) {
                    item.getCsdVersion();
                    next.getCsdVersion();
                    return true;
                }
            }
            return false;
        }

        private void addToSelectedIndexes(TCaseDetails item) {
            if (!selectedIndexes.contains(item)) {
                selectedIndexes.add(item);
            }
        }

        private class VersionDisplayCell extends ListCell<TCaseDetails> {

            CheckBox box = new CheckBox("");
            Label label = new Label();
            HBox content = new HBox(5, box, label);
            VersionStringConverter converter = new VersionStringConverter();
            private ChangeListener<Boolean> selectionChangeListener = new ChangeListener<>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                    if (!t1) {
                        if ((selectedIndexes.size() - 1) <= 0) {
                            if (hasSelectedIndex(getItem())) {
                                box.setSelected(true);
                            }
                            return;
                        }
                    }
                    updateSelection(t1);
                    getListView().refresh();
                }
            };
            private EventHandler<MouseEvent> mousePressedHandler = new EventHandler<>() {
                @Override
                public void handle(MouseEvent t) {
                    t.consume();
                    box.setSelected(!box.isSelected());
                }
            };

            public VersionDisplayCell() {
                super();
                addEventFilter(MouseEvent.MOUSE_PRESSED, mousePressedHandler);
                box.selectedProperty().addListener(selectionChangeListener);
            }

            public void destroy() {
                content.getChildren().clear();
                setGraphic(null);
                box.selectedProperty().removeListener(selectionChangeListener);
                selectionChangeListener = null;
                removeEventFilter(MouseEvent.MOUSE_PRESSED, mousePressedHandler);
                mousePressedHandler = null;
                converter = null;
                setTooltip(null);
            }

            private void updateSelection(boolean pSelected) {
                if (pSelected) {
                    addToSelectedIndexes(getItem());
                } else {
                    removeFromSelectedIndexes(getItem());
                }
            }

            private boolean isChecked(TCaseDetails item) {
                return hasSelectedIndex(item);
            }

            @Override
            protected void updateItem(TCaseDetails t, boolean bln) {
                super.updateItem(t, bln); //To change body of generated methods, choose Tools | Templates.
                if (t == null || bln) {
                    setGraphic(null);
                    setTooltip(null);
                    return;
                }
//                String tooltipText = converter.getTooltipText(t);
//                if (tooltipText != null && !tooltipText.isEmpty()) {
//                    setTooltip(new Tooltip(tooltipText));
//                }
                Tooltip tip = new Tooltip();
                tip.setGraphic(converter.getTooltipGraphic(t));
                setTooltip(tip);
                box.setSelected(isChecked(getItem()));
                label.setText(converter.toString(t));
                setGraphic(content);
            }

        }
    }

}
