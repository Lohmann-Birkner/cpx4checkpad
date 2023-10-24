/*
 * Copyright (c) 2021 Lohmann & Birkner.
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
 *    2021  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.cm.fx.details;

import de.lb.cpx.client.app.cm.fx.department.DepartmentTitledPane;
import de.lb.cpx.client.app.cm.fx.department.SelectedDepartmentItem;
import de.lb.cpx.client.app.cm.fx.department.SelectedWardItem;
import de.lb.cpx.client.app.cm.fx.department.model.DepartmentWardDetails;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.fx.adapter.WeakPropertyAdapter;
import de.lb.cpx.client.core.model.fx.masterdetail.AccordionMasterDetailPane;
import de.lb.cpx.client.core.model.fx.pane.AsyncPane;
import de.lb.cpx.client.core.model.fx.titledpane.AccordionSelectedItem;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.TCaseWard;
import de.lb.cpx.shared.lang.Lang;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Accordion;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 *
 * @author wilde
 */
public class CmDepartmentFXMLController extends Controller<CmDepartmentScene> {

    private static final String SELECTED_STYLE_CLASS = "accordion-row-selected";

    @FXML
    private ComboBox<TCaseDetails> cbLocals;
    @FXML
    private VBox vbRoot;
    private WeakPropertyAdapter propAdapter;
    
//    @FXML
//    private AccordionMasterDetailPane<TCaseDepartment> mdDepartment;
    private AsyncPane<AccordionMasterDetailPane<TCaseDepartment>> asyncPane;
    
    public CmDepartmentFXMLController(){
        super();
    }

    @Override
    public void refresh() {
        Accordion accordion = getAccordion();
//        Accordion accordion = mdDepartment.getAccordion();
        if (accordion == null) {
            return;
        }
        for (TitledPane tp : accordion.getPanes()) {
            if (tp instanceof DepartmentTitledPane) {
                if (tp.isExpanded()) {
                    ((DepartmentTitledPane) tp).refresh();
                }
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        propAdapter = new WeakPropertyAdapter();
        propAdapter.addChangeListener(cbLocals.getSelectionModel().selectedItemProperty(), new ChangeListener<TCaseDetails>() {
            @Override
            public void changed(ObservableValue<? extends TCaseDetails> ov, TCaseDetails t, TCaseDetails t1) {
//                updateAccordionItems(t1);
                asyncPane.reload();
            }
        });
        asyncPane = new AsyncPane<>() {
            @Override
            public AccordionMasterDetailPane<TCaseDepartment> loadContent() {
                AccordionMasterDetailPane<TCaseDepartment> mdDepartment = new AccordionMasterDetailPane<>() {
                    @Override
                    public String getPlaceholderText() {
                        return new StringBuilder("Die Fallversion: ")
                                .append(CmDepartmentFXMLController.this.getScene().getVersionNameConverter().toString(cbLocals.getSelectionModel().getSelectedItem()))
                                .append(" scheint über keine Fachabteilungen zu verfügen!")
                                .toString();
                    }
                };

                //init master detail listview and set items and cellfactory

                mdDepartment.setTitledPaneFactory(new Callback<TCaseDepartment, TitledPane>() {
                    @Override
                    public TitledPane call(TCaseDepartment param) {
                        List<TCaseWard> wards = CmDepartmentFXMLController.this.getScene().findWardsForDepartment(param);
                        DepartmentTitledPane department = new DepartmentTitledPane(param, createDepartmentTitle(param, wards));

                        LOG.log(Level.FINE," call: " +  department.getTitle());
                        department.setValues(wards);
                        department.setSelectItemCallback(new Callback<AccordionSelectedItem<TCaseWard>, Boolean>() {
                            @Override
                            public Boolean call(AccordionSelectedItem<TCaseWard> param) {
                                if (param instanceof SelectedDepartmentItem) {
                                    clearSelectedWards(mdDepartment.getAccordion().getPanes());
                                    updateSelectedStyleClass(true, department, mdDepartment.getAccordion().getPanes());
                                    updateDetailForDepartment(mdDepartment,(SelectedDepartmentItem) param);
                                }
                                if (param instanceof SelectedWardItem) {
                                    updateSelectedStyleClass(false, department, mdDepartment.getAccordion().getPanes());
                                    updateDetailForWard(mdDepartment,(SelectedWardItem) param);
                                }
//                                department.clearSelection();
//                                updateSelectedStyleClass(true,department,mdDepartment.getAccordion().getPanes());
                                return true;
                            }
                        
                            private void clearSelectedWards(ObservableList<TitledPane> panes) {
                                for (TitledPane pane : panes) {
                                    if (pane instanceof DepartmentTitledPane) {
                                        ((DepartmentTitledPane) pane).clearSelection();
                                    }
                                }
                            }
                        });
//                    }
                        return department;
                    }
                });
                List<TCaseDepartment> caseDepartments = sortDepartments(CmDepartmentFXMLController.this.getScene().getDepartmentsForVersion(cbLocals.getSelectionModel().getSelectedItem()));
                for(TCaseDepartment dep: caseDepartments){
                     LOG.log(Level.FINE, "read department list:" + createDepartmentTitle(dep, null));
                }
                mdDepartment.getMenuItems().setAll(caseDepartments);

                return mdDepartment;
            }
        };
        VBox.setVgrow(asyncPane, Priority.ALWAYS);
        vbRoot.getChildren().add(asyncPane);
    }
    
//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//        super.initialize(location, resources);
//        propAdapter = new WeakPropertyAdapter();
//        propAdapter.addChangeListener(cbLocals.getSelectionModel().selectedItemProperty(), new ChangeListener<TCaseDetails>() {
//            @Override
//            public void changed(ObservableValue<? extends TCaseDetails> ov, TCaseDetails t, TCaseDetails t1) {
//                updateAccordionItems(t1);
//
//            }
//        });
//                //init master detail listview and set items and cellfactory
//
//        mdDepartment.setTitledPaneFactory(new Callback<TCaseDepartment, TitledPane>() {
//            @Override
//            public TitledPane call(TCaseDepartment param) {
//                List<TCaseWard> wards = CmDepartmentFXMLController.this.getScene().findWardsForDepartment(param);
//                DepartmentTitledPane department = new DepartmentTitledPane(param, createDepartmentTitle(param, wards));
//
//                LOG.log(Level.FINE," call: " +  department.getTitle());
//                department.setValues(wards);
//                department.setSelectItemCallback(new Callback<AccordionSelectedItem<TCaseWard>, Boolean>() {
//                    @Override
//                    public Boolean call(AccordionSelectedItem<TCaseWard> param) {
//                        if (param instanceof SelectedDepartmentItem) {
//                            clearSelectedWards(mdDepartment.getAccordion().getPanes());
//                            updateSelectedStyleClass(true, department, mdDepartment.getAccordion().getPanes());
//                            updateDetailForDepartment(mdDepartment,(SelectedDepartmentItem) param);
//                        }
//                        if (param instanceof SelectedWardItem) {
//                            updateSelectedStyleClass(false, department, mdDepartment.getAccordion().getPanes());
//                            updateDetailForWard(mdDepartment,(SelectedWardItem) param);
//                        }
////                                department.clearSelection();
////                                updateSelectedStyleClass(true,department,mdDepartment.getAccordion().getPanes());
//                        return true;
//                    }
//
//                    private void clearSelectedWards(ObservableList<TitledPane> panes) {
//                        for (TitledPane pane : panes) {
//                            if (pane instanceof DepartmentTitledPane) {
//                                ((DepartmentTitledPane) pane).clearSelection();
//                            }
//                        }
//                    }
//                });
////                    }
//                return department;
//            }
//        });
////        List<TCaseDepartment> caseDepartments = sortDepartments(CmDepartmentFXMLController.this.getScene().getDepartmentsForVersion(cbLocals.getSelectionModel().getSelectedItem()));
////        for(TCaseDepartment dep: caseDepartments){
////             LOG.log(Level.FINE, "read department list:" + createDepartmentTitle(dep, null));
////        }
////        mdDepartment.getMenuItems().setAll(caseDepartments);
//
//
//    }

    
   
    private List <TCaseDepartment> sortDepartments(List<TCaseDepartment> pDepartments){
        
        return pDepartments.stream().sorted(Comparator.comparing(TCaseDepartment::getDepcAdmDate)
        ).sorted(Comparator.comparing(TCaseDepartment::getDepcDisDate)).collect(Collectors.toList());
    }

    
    private String createDepartmentTitle(TCaseDepartment pDepartment, List<TCaseWard> pWards) {
        StringBuilder builder = new StringBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat(Lang.getDepartmentsWardDateFormat());
        builder.append(getScene().getDepartment301Description(pDepartment));
        builder.append(" (");
        builder.append(pDepartment.getDepCodes());
        builder.append(")");
        builder.append((pDepartment.getDepcIsBedIntensivFl() ? "\nIntensivbett" : ""));
        if (pWards != null && !pWards.isEmpty()) {
            builder.append(" (");
            builder.append(pWards.size());
            builder.append(")");
        }
        builder.append("\n");
        builder.append("von: ").append(pDepartment.getDepcAdmDate() == null ? "n.A." : formatter.format(pDepartment.getDepcAdmDate()));
        builder.append(" bis: ").append(pDepartment.getDepcDisDate() == null ? "n.A." : formatter.format(pDepartment.getDepcDisDate()));
        return builder.toString();
    }

    public TCaseDetails getSelectedDetails(){
        return cbLocals.getValue();
    }
    public int getYearOfSelectedDetails(){
        TCaseDetails details = getSelectedDetails();
        if(details == null){
            return -1;
        }
        return Lang.toYear(details.getCsdAdmissionDate());
    }
    private void updateSelectedStyleClass(boolean selected, DepartmentTitledPane department, ObservableList<TitledPane> panes) {
        AccordionMasterDetailPane<TCaseDepartment> accordion = getAccordionMasterDetail();
        if (accordion == null) {
            return;
        }
        for (TitledPane pane : panes) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    ((DepartmentTitledPane) pane).getStyleClass().remove(SELECTED_STYLE_CLASS);
                    if (selected && ((DepartmentTitledPane) pane).getTitle().equals(department.getTitle())) { // weak equals check - should work here with some sort of id? For now should work exactly same department should not exist
                        department.getStyleClass().add(0, SELECTED_STYLE_CLASS);
                    }
                }
            });
        }

    }


    private Accordion getAccordion() {
        AccordionMasterDetailPane<TCaseDepartment> masterDetail = getAccordionMasterDetail();
        return masterDetail != null ? masterDetail.getAccordion() : null;
    }

    private AccordionMasterDetailPane<TCaseDepartment> getAccordionMasterDetail() {
        if (asyncPane == null) {
            return null;
        }
        if (asyncPane.getContent() instanceof AccordionMasterDetailPane) {
            return ((AccordionMasterDetailPane) asyncPane.getContent());
        }
        return null;
    }
    private void setDetail(Parent pDetail) {
        Parent node = Objects.requireNonNullElse(pDetail, createPlaceholder());
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
                getAccordionMasterDetail().setDetail(node);
//            }
//        });
    }

    private Parent createPlaceholder() {
        return createPlaceholder("Es wurde nichts ausgewählt!\nWählen Sie einen Eintrag aus dem Menü links aus.");
    }

    private Parent createPlaceholder(String pText) {
        Label lbl = new Label(pText);
        return createPlaceholder(lbl);
    }

    private Parent createPlaceholder(Node pNode) {
        HBox box = new HBox(pNode);
        box.setAlignment(Pos.CENTER);
        box.setFillHeight(false);
        return box;
    }

    @Override
    public void afterInitialisingScene() {
        super.afterInitialisingScene();
        cbLocals.setConverter(getScene().getVersionNameConverter());
        cbLocals.getItems().addAll(getScene().getSortedLocalVersions());
        cbLocals.getSelectionModel().select(getScene().getCurrentLocalVersion());
//        updateAccordionItems(cbLocals.getSelectionModel().getSelectedItem());
    }

    @Override
    public boolean close() {
        propAdapter.dispose();
        return true;
    }

    private void updateDetailForDepartment(AccordionMasterDetailPane<TCaseDepartment> pPane, SelectedDepartmentItem selectedDepartmentItem) {
        DepartmentWardDetails<TCaseDepartment> departmentDetail = new DepartmentWardDetails<>(selectedDepartmentItem.getDepartment());
        departmentDetail.setTitle("Fachabteilung: ");
        departmentDetail.setNoItemText("Es wurde keine Fachabteilung ausgewählt!");
        departmentDetail.setItemConverter(getDepartmentConverter());
        departmentDetail.setIcdConverter(getIcdConverter());
        departmentDetail.setOpsConverter(getOpsConverter());
        departmentDetail.setFindIcdsCallback(new Callback<TCaseDepartment, List<TCaseIcd>>() {
            @Override
            public List<TCaseIcd> call(TCaseDepartment param) {
                return getScene().findIcdsForDepartment(param);
            }
        });
        departmentDetail.setFindOpsesCallback(new Callback<TCaseDepartment, List<TCaseOps>>() {
            @Override
            public List<TCaseOps> call(TCaseDepartment param) {
                return getScene().findOpsesForDepartment(param);
            }
        });
        departmentDetail.setSaveIcdCallback(getSaveIcdCallback());
        pPane.setDetail(departmentDetail);
//        setDetail(createDetailForDepartment(selectedDepartmentItem));
    }

    private void updateDetailForWard(AccordionMasterDetailPane<TCaseDepartment> pPane,SelectedWardItem selectedWardItem) {
        DepartmentWardDetails<TCaseWard> wardDetail = new DepartmentWardDetails<>(selectedWardItem.getItem());
        wardDetail.setTitle("Station: ");
        wardDetail.setNoItemText("Es wurde keine Station ausgewählt!");
        wardDetail.setItemConverter(getWardConverter());
        wardDetail.setIcdConverter(getIcdConverter());
        wardDetail.setOpsConverter(getOpsConverter());
        wardDetail.setFindIcdsCallback(new Callback<TCaseWard, List<TCaseIcd>>() {
            @Override
            public List<TCaseIcd> call(TCaseWard param) {
                return getScene().findIcdsForWard(param);
            }
        });
        wardDetail.setFindOpsesCallback(new Callback<TCaseWard, List<TCaseOps>>() {
            @Override
            public List<TCaseOps> call(TCaseWard param) {
                return getScene().findOpsesForWard(param);
            }
        });
        wardDetail.setSaveIcdCallback(getSaveIcdCallback());
        pPane.setDetail(wardDetail);
//        setDetail(createDetailForDepartment(selectedDepartmentItem));
    }
    
    private StringConverter<TCaseWard> wardConverter;
    public StringConverter<TCaseWard> getWardConverter(){
        if(wardConverter == null){
            wardConverter = new StringConverter<TCaseWard>() {
                @Override
                public String toString(TCaseWard t) {
                    if(t == null){
                        return "";
                    }
                    return t.getWardcIdent();
                }

                @Override
                public TCaseWard fromString(String string) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            };
        }
        return wardConverter;
    }
    private StringConverter<TCaseDepartment> departmentConverter;
    public StringConverter<TCaseDepartment> getDepartmentConverter() {
        if (departmentConverter == null) {
            departmentConverter = new StringConverter<TCaseDepartment>() {
                @Override
                public String toString(TCaseDepartment t) {
                    if (t == null) {
                        return null;
                    }
                    return getScene().getDepartment301Description(t) + " (" + t.getDepCodes() + ")"
                            + (t.getDepcIsBedIntensivFl() ? " - Intensivbett" : "");
//                return getScene().getDepartment301Description(t);
                }

                @Override
                public TCaseDepartment fromString(String string) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            };
        }
        return departmentConverter;
    }
    private StringConverter<TCaseIcd> icdConverter;
    public StringConverter<TCaseIcd> getIcdConverter() {
        if (icdConverter == null) {
            icdConverter = new StringConverter<TCaseIcd>() {
                @Override
                public String toString(TCaseIcd t) {
                    if (t == null) {
                        return null;
                    }
                    return getScene().getIcdCodeDescription(t, getYearOfSelectedDetails());
                }

                @Override
                public TCaseIcd fromString(String string) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            };
        }
        return icdConverter;
    }
        
    private StringConverter<TCaseOps> opsConverter;
    public StringConverter<TCaseOps> getOpsConverter() {
        if (opsConverter == null) {
            opsConverter = new StringConverter<TCaseOps>() {
                @Override
                public String toString(TCaseOps t) {
                    return getScene().getOpsCodeDescription(t, getYearOfSelectedDetails());
                }

                @Override
                public TCaseOps fromString(String string) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            };
        }
        return opsConverter;
    }
    private Callback<TCaseIcd,Boolean> saveIcdCallback;
    public Callback<TCaseIcd, Boolean> getSaveIcdCallback() {
        if (saveIcdCallback == null) {
            saveIcdCallback = new Callback<TCaseIcd, Boolean>() {
                @Override
                public Boolean call(TCaseIcd param) {
                    getScene().saveTCaseIcd(param);
                    return true;
                }
            };
        }
        return saveIcdCallback;
    }

//    private void updateAccordionItems(TCaseDetails selectedItem) {
//        List<TCaseDepartment> caseDepartments = sortDepartments(CmDepartmentFXMLController.this.getScene().getDepartmentsForVersion(selectedItem));
//        for(TCaseDepartment dep: caseDepartments){
//             LOG.log(Level.FINE, "read department list:" + createDepartmentTitle(dep, null));
//        }
//        mdDepartment.getMenuItems().setAll(caseDepartments);
//        
//    }
    
}
