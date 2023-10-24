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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.process.master_detail;

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.process.listview.entries.HistoryEntry;
import de.lb.cpx.client.app.wm.fx.process.section.WmDocumentSection;
import de.lb.cpx.client.app.wm.fx.process.section.WmHistorySection;
import de.lb.cpx.client.app.wm.fx.process.section.WmPatientSection;
import de.lb.cpx.client.app.wm.fx.process.section.WmReminderSection;
import de.lb.cpx.client.app.wm.fx.process.section.WmServiceOverviewSection;
import de.lb.cpx.client.app.wm.fx.process.section.details.WmDetailSection;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.fx.adapter.WeakPropertyAdapter;
import de.lb.cpx.client.core.model.fx.masterdetail.MasterDetailSplitPane;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import de.lb.cpx.shared.dto.HistoryFilter;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Implementation of the Masterdetail Pattern for the Process View Detailpane
 * occupies 1/3 of the space available resets to default 1/3 if window size
 * changed!
 *
 * @author wilde
 */
public final class ProcessMasterDetailPane extends MasterDetailSplitPane {

    private static final Logger LOG = Logger.getLogger(ProcessMasterDetailPane.class.getName());
    
    private SplitPane contentPane;
    private HBox detailPane;
    private WmHistorySection historySection;
    private WmReminderSection reminderSection;
    private WmPatientSection patientSection;
    private WmServiceOverviewSection serviceSection;
    private WmDocumentSection documentSection;
    private WeakPropertyAdapter adapter;
    private boolean isDisposed;

    public ProcessMasterDetailPane() {
        super();
        //compute size of detail pane
        setContentInMaster();
        setContentInDetail();

        setDividerDefaultPosition(2.0 / 3.0);
    }
    public static final String SHOW_ACTION_DETAILS = "show.action.details";

    public ProcessMasterDetailPane(ProcessServiceFacade serviceFacade) {
        this();
        adapter = new WeakPropertyAdapter();
        setDetailPane(new WmDetailSection().getRoot());

        reminderSection = new WmReminderSection(serviceFacade);
//        reminderSection.requestDetailProperty.addListener(new ChangeListener<Boolean>() {
        adapter.addChangeListener(reminderSection.requestDetailProperty,new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    setDetailPane(reminderSection.getDetailContent());
                }
            }
        });
        addContentInProcessOverview(reminderSection.getRoot());
        
        patientSection = new WmPatientSection(serviceFacade);
        patientSection.minHeightProperty().bind(getProcessOverview().heightProperty().divide(4.0));
        patientSection.setMaxHeight(230.0);
//        patientSection.requestDetailProperty.addListener(new ChangeListener<Boolean>() {
        adapter.addChangeListener(patientSection.requestDetailProperty,new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    setDetailPane(patientSection.getDetailContent());
                }
            }
        });
        addContentInProcessOverview(patientSection.getRoot());

        serviceSection = new WmServiceOverviewSection(serviceFacade);
        serviceSection.setMinHeight(88.0);
        serviceSection.setMaxHeight(88.0);
//        serviceSection.requestDetailProperty.addListener(new ChangeListener<Boolean>() {
        adapter.addChangeListener(serviceSection.requestDetailProperty,new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    setDetailPane(serviceSection.getDetailContent());
                }
            }
        });
        addContentInProcessOverview(serviceSection.getRoot());

        documentSection = new WmDocumentSection(serviceFacade);
        documentSection.setMinHeight(80.0);
//        documentSection.requestDetailProperty.addListener(new ChangeListener<Boolean>() {
        adapter.addChangeListener(documentSection.requestDetailProperty,new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    setDetailPane(documentSection.getDetailContent());
                }
            }
        });
        addContentInProcessOverview(documentSection.getRoot());

        // show filter options
        final HistoryFilter historyFilter = Session.instance().getHistoryFilter();
        historySection = new WmHistorySection(serviceFacade, true);
        if (!historyFilter.getSearchText().isEmpty() || historyFilter.getSelectedEventTypes().length > 0) {
            if (historySection.getFilterButton() != null && historySection.getTextSearchButton() != null) {
                historySection.getFilterButton().setSelected(true);
                historySection.getTextSearchButton().setSelected(true);
            }
        }

        if (historySection.getListView() != null && !historySection.getListView().getItems().isEmpty()) {
            historySection.getListView().setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getClickCount() == 2) {
                        //Use ListView's getSelected Item
                        historySection.getListView().getSelectionModel().getSelectedItem();
                        //use this to do whatever you want to. Open Link etc.
                    }
                }
            });
        }
//        historySection.getListView().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<HistoryEntry<? extends AbstractEntity>>() {
        adapter.addChangeListener(historySection.getListView().getSelectionModel().selectedItemProperty(),new ChangeListener<HistoryEntry<? extends AbstractEntity>>() {
            @Override
            public void changed(ObservableValue<? extends HistoryEntry<? extends AbstractEntity>> observable, HistoryEntry<? extends AbstractEntity> oldValue, HistoryEntry<? extends AbstractEntity> newValue) {
                setDetailPane(historySection.getDetailContent());
            }
        });
        addContentInHistory(historySection.getRoot());
    }
    
    /**
     * sets the Historypane in the correct area in the Masterpane replaces old
     * content
     *
     * @param pane history item as Pane
     */
    public void setHistory(Pane pane) {
        pane.prefWidthProperty().bind(getMasterPane().widthProperty().divide(2));
        if (!contentPane.getItems().isEmpty()) {
            contentPane.getItems().set(0, pane);
            return;
        }
        contentPane.getItems().add(pane);
    }

    /**
     * @return history section
     */
    public WmHistorySection getHistorySection() {
        return historySection;
    }

    /**
     * @return service(Leistung) section
     */
    public WmServiceOverviewSection getServiceSection() {
        return serviceSection;
    }

    /**
     * @return document section
     */
    public WmDocumentSection getDocumentSection() {
        return documentSection;
    }

    /**
     * @return patient Section
     */
    public WmPatientSection getPatientSection() {
        return patientSection;
    }

    /**
     * @return reminder section
     */
    public WmReminderSection getReminderSection() {
        return reminderSection;
    }

    /**
     * set Contentarea for Processoveriew replaces old content
     *
     * @param hBox contentarea
     */
    private void setProcessOverview(Pane pane) {
//        pane.setPrefWidth(100);
        pane.prefWidthProperty().bind(getMasterPane().widthProperty().divide(2));
        if (!contentPane.getItems().isEmpty() && contentPane.getItems().size() >= 2) {
            contentPane.getItems().set(1, pane);
            return;
        }
        contentPane.getItems().add(pane);
    }

    /**
     * add content in processOverview, its added below the current content
     * placed there
     *
     * @param content content node to be added
     */
    public void addContentInProcessOverview(Node content) {
        VBox overview = (VBox) contentPane.getItems().get(1);
        overview.getChildren().add(content);
    }

    public VBox getProcessOverview() {
        return (VBox) contentPane.getItems().get(1);
    }

    /**
     * add content in history pane, its added below the current content placed
     * there
     *
     * @param content content node to be added
     */
    public void addContentInHistory(Node content) {
        HBox overview = (HBox) contentPane.getItems().get(0);
        overview.getChildren().add(content);
    }

    //sets up content, needed because base layout in basic class is anchorpane
    private void setContentInMaster() {
        contentPane = new SplitPane();
        contentPane.getStyleClass().add("preview-split-pane");

//        contentPane.setSpacing(10.0);
//        HBox.setMargin(contentPane, new Insets(10, 10, 10, 10));
        //workaround, master expects a pane, but splitpane is controller, needs to be
        //looked up again AWI:20170223
        setMaster(new HBox(contentPane));
        HBox history = new HBox();
        history.setSpacing(10.0);
        HBox.setHgrow(history, Priority.ALWAYS);
//        HBox.setMargin(history, new Insets(10, 10, 10, 10));
        setHistory(history);
        VBox overview = new VBox();
//        overview.setSpacing(5.0);
        HBox.setHgrow(overview, Priority.ALWAYS);
//        HBox.setMargin(overview, new Insets(10, 10, 10, 10));
        setProcessOverview(overview);
    }

    //sets content in detail pane, makes sure that 1/3 of the available space is used for the detailcontent
    private void setContentInDetail() {
//        HBox.setMargin(getDetailPane(), new Insets(10, 10, 10, 10));
        detailPane = new HBox();
        setDetail(detailPane);
//        getDetailPane().prefWidthProperty().bind(this.widthProperty().divide(3));
    }

    /**
     * sets parent in detailpane and removes the old content
     *
     * @param detail detail to set
     */
    public void setDetailPane(Parent detail) {
        detailPane.getChildren().clear();
        detailPane.getChildren().add(detail);
    }

    public void dispose() {
        if(isDisposed){
            LOG.info("ProcessMasterDetailPane is already disposed - do nothing!");
            return;
        }
        getDetailPane().getChildren().clear();
        getMasterPane().getChildren().clear();
        contentPane.getItems().clear();
        patientSection.minHeightProperty().unbind();
        if(historySection.getListView()!=null){
            historySection.getListView().setOnMouseClicked(null);
        }
        adapter.dispose();
        adapter = null;
        reminderSection.dispose();
        serviceSection.dispose();
        documentSection.dispose();
        patientSection.dispose();
        historySection.dispose();
        reminderSection = null;
        serviceSection = null;
        documentSection = null;
        patientSection = null;
        historySection = null;
        isDisposed = true;
    }

}
