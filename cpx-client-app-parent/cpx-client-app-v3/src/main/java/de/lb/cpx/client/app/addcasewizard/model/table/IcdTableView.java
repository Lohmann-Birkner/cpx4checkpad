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
package de.lb.cpx.client.app.addcasewizard.model.table;

import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.catalog.CpxIcdCatalog;
import de.lb.cpx.client.core.model.cell.SimpleCellValueFactory;
import de.lb.cpx.client.core.util.OverrunHelper;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.enums.LocalisationEn;
import de.lb.cpx.server.catalog.service.ejb.CatalogUtil;
import de.lb.cpx.server.commonDB.model.CIcdCatalog;
import de.lb.cpx.shared.lang.Lang;
import java.util.Date;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

/**
 * Basic IcdTableView Supported Columns:
 * MainDiagnosis,GrouperRelevant,ICD-Code,ICD-Text,Secondary Diagnosis ToDo:Add
 * Catalog DataValues to TableItems
 *
 * @author wilde
 */
public class IcdTableView extends TableView<TCaseIcd> {

    private final String[] icdTableHeader = new String[]{"_", Lang.getCaseResolveMd(), Lang.getCaseResolveUsedForGrouping(),
        Lang.getCaseResolveICD(), Lang.getCaseResolveICD_Text(), Lang.getCaseResolveSecundaryICD(), Lang.getCaseResolveLocalisation()};
    protected final TableColumn<TCaseIcd, TCaseIcd> principalDiagnosisColumn = new TableColumn<>(icdTableHeader[1]);
    protected final TableColumn<TCaseIcd, TCaseIcd> useForGroupColumn = new TableColumn<>(icdTableHeader[2]);
    protected final TableColumn<TCaseIcd, String> icdCodeColumn = new TableColumn<>(icdTableHeader[3]);
    protected final TableColumn<TCaseIcd, String> icdTextColumn = new TableColumn<>(icdTableHeader[4]);
    protected final TableColumn<TCaseIcd, TCaseIcd> secIcdCodeColumn = new TableColumn<>(icdTableHeader[5]);
    protected final TableColumn<TCaseIcd, LocalisationEn> icdLocalisationColumn = new TableColumn<>(icdTableHeader[6]);
    private double weight = 1.0;
    private final String lang = CpxClientConfig.instance().getLanguage();
    //Todo: so nicht .. muss jahr übergeben in Konstruktor...
    private int year = CpxClientConfig.instance().getSelectedGrouper().getCatalogYear();
    private Controller<?> controller;

    /**
     * Construct simple ICD Table, hide horizontal Scrollbar
     *
     * @param controller SceneController of the parent class
     * @param weight weight value that should be used for columns
     * @param year year of Validity
     */
    public IcdTableView(Controller<?> controller, double weight, int year) {
        this(controller, year);
        this.weight = weight;

    }

    /**
     * Construct simple ICD Table, hide horizontal Scrollbar
     *
     * @param controller SceneController of the parent class
     * @param year year of Validity
     */
    public IcdTableView(Controller<?> controller, int year) {
        this(year);
        this.controller = controller;
    }

    /**
     * creates simple ICD Table hide horizontal Scrollbar and uses given year
     * for validity
     *
     * @param year year of validity
     */
    public IcdTableView(int year) {
        this();
        this.year = year;
    }

    public IcdTableView() {
//        super(CleanSide.HORIZONTAL);
        setUpTableColumns();
    }

    public Double computeColumnPercentage(Double val1, Double val2) {

        return val1 * val2;
    }

    private void setUpTableColumns() {

        setUpPrincipalColumn(computeColumnPercentage(0.05, weight));
        setUpUseForGroupColumn(computeColumnPercentage(0.05, weight));
        setUpIcdCodeColumn(computeColumnPercentage(0.2, weight));
        setUpIcdTextColumn(computeColumnPercentage(0.5, weight));
        setUpSecIcdCodeColumn(computeColumnPercentage(0.2, weight));
        setUpLocalisationColumn(computeColumnPercentage(0.2, weight));

        resizeColumns();
    }

    private void setUpPrincipalColumn(double pWidth) {
        ToggleGroup tGroup = new ToggleGroup();

//        validation.registerValidator((RadioButton)tGroup.getSelectedToggle(), Validator.createEmptyValidator("set MainDiagnosis"));
        principalDiagnosisColumn.setCellValueFactory(new SimpleCellValueFactory<>());
        principalDiagnosisColumn.setCellFactory(new Callback<TableColumn<TCaseIcd, TCaseIcd>, TableCell<TCaseIcd, TCaseIcd>>() {
            @Override
            public TableCell<TCaseIcd, TCaseIcd> call(TableColumn<TCaseIcd, TCaseIcd> param) {
                TableCell<TCaseIcd, TCaseIcd> cell = new TableCell<>() {
                    private final RadioButton rb = new RadioButton();

                    @Override
                    protected void updateItem(TCaseIcd item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null) {
                            return;
                        }
                        TCaseIcd icd = item;

                        rb.setUserData(icd);
                        rb.setSelected(icd.getIcdcIsHdxFl());
                        rb.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
//                                if (isArmed()) {
                                if (rb.isFocused()) {
                                    if (!icd.getIcdcIsHdxFl()) {

                                        resetMainDiagnosis();
                                        icd.setIcdcIsHdxFl(true);
                                        if (!icd.getIcdIsToGroupFl()) {
                                            icd.setIcdIsToGroupFl(true);
                                        }
                                        refresh();
//                                    if(controller != null){
//                                        ((CaseDetailsCaseResolveFXMLController)controller).saveIcd(icd);
//                                        ((CaseDetailsCaseResolveFXMLController)controller).performGroup();
//                                    }
                                    }
                                }
//                                } else {
//                                    rb.setSelected(icd.getIcdcIsHdxFl());
//                                    refresh();
//                                }
                            }

                        });
                        setGraphic(rb);
                        rb.setToggleGroup(tGroup);

                    }
                };
                return cell;
            }

        });
        this.getColumns().add(principalDiagnosisColumn);
//        principalDiagnosisColumn.setPercentageWidth(pWidth);
    }

    private void setUpUseForGroupColumn(double pWidth) {
        useForGroupColumn.setCellValueFactory(new SimpleCellValueFactory<>());
        useForGroupColumn.setCellFactory(new Callback<TableColumn<TCaseIcd, TCaseIcd>, TableCell<TCaseIcd, TCaseIcd>>() {
            @Override
            public TableCell<TCaseIcd, TCaseIcd> call(TableColumn<TCaseIcd, TCaseIcd> param) {
                TableCell<TCaseIcd, TCaseIcd> cell = new TableCell<>() {
                    private final CheckBox cb = new CheckBox();

                    @Override
                    protected void updateItem(TCaseIcd item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null) {
                            return;
                        }
                        TCaseIcd icd = item;
                        cb.setUserData(icd);
                        cb.setSelected(icd.getIcdIsToGroupFl());
                        cb.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                boolean selected = cb.isSelected();
//                                if (isArmed()) {
                                if (!icd.getIcdcIsHdxFl()) {
                                    icd.setIcdIsToGroupFl(selected);
                                } else {
                                    cb.setSelected(true);
                                }
//                                ((CaseDetailsCaseResolveFXMLController)controller).saveIcd(icd);
//                                ((CaseDetailsCaseResolveFXMLController)controller).performGroup();
//                                } else {
//                                    cb.setSelected(icd.getIcdIsToGroupFl());
//                                }
                            }
                        });
                        setGraphic(cb);
                    }

                };
                return cell;
            }
        });
        this.getColumns().add(useForGroupColumn);
//        useForGroupColumn.setPercentageWidth(pWidth);
    }

    private void setUpIcdCodeColumn(double pWidth) {
        //CPX-997 sort icdCodeColumn
//        icdCodeColumn.setCellValueFactory(new SimpleCellValueFactory<>());
        icdCodeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TCaseIcd, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TCaseIcd, String> param) {
                return new SimpleObjectProperty<>(param.getValue().getIcdcCode());
            }
        });
        icdCodeColumn.setCellFactory(new Callback<TableColumn<TCaseIcd, String>, TableCell<TCaseIcd, String>>() {
            @Override
            public TableCell<TCaseIcd, String> call(TableColumn<TCaseIcd, String> param) {
                TableCell<TCaseIcd, String> cell = new TableCell<TCaseIcd, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            return;
                        }
//                        TCaseIcd icd = (TCaseIcd) item;
                        setText(item);
                    }
                };
                return cell;
            }
        });
        this.getColumns().add(icdCodeColumn);
//        icdCodeColumn.setPercentageWidth(pWidth);
    }

    private void setUpIcdTextColumn(double pWidth) {
//        icdTextColumn.setCellValueFactory(new SimpleCellValueFactory<>());
        icdTextColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TCaseIcd, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TCaseIcd, String> param) {
                return new SimpleObjectProperty<>(CpxIcdCatalog.instance().getByCode(param.getValue().getIcdcCode(), lang, year).getDescription());
            }
        });
        icdTextColumn.setCellFactory(new Callback<TableColumn<TCaseIcd, String>, TableCell<TCaseIcd, String>>() {
            @Override
            public TableCell<TCaseIcd, String> call(TableColumn<TCaseIcd, String> param) {
                TableCell<TCaseIcd, String> cell = new TableCell<TCaseIcd, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            setText("");
                            setGraphic(null);
                            //setValues("No Data for IcdCode in Catalog for language " + lang + " and year " + year);
                            return;
                        }

//                        TCaseIcd icd = (TCaseIcd) cell.getTableRow().getItem();
//                        CIcdCatalog icdCat = CpxIcdCatalog.instance().getByCode(icd.getIcdcCode(), lang, year);
                        //CPX-997 addOverrunInfoButton for Icd Text 
                        HBox hLabel = new HBox();
                        hLabel.setMinWidth(100);
                        hLabel.setMaxWidth(450);
                        hLabel.setAlignment(Pos.CENTER_LEFT);
                        Label label = new Label(item);
                        hLabel.getChildren().add(label);
                        OverrunHelper.addOverrunInfoButton(label);
                        setGraphic(hLabel);

                    }

                };
                return cell;
            }
        });
        this.getColumns().add(icdTextColumn);
//        icdTextColumn.setPercentageWidth(pWidth);
    }

    private void setUpSecIcdCodeColumn(double pWidth) {
        secIcdCodeColumn.setCellValueFactory(new SimpleCellValueFactory<>());
        secIcdCodeColumn.setCellFactory(getSecIcdCellFactory());
        this.getColumns().add(secIcdCodeColumn);
//        secIcdCodeColumn.setPercentageWidth(pWidth);

    }

    /**
     * set new catalogYear refresh table after that
     *
     * @param dateForCatalog date which determines the Catalog to use
     */
    public void setCatalogYear(Date dateForCatalog) {
        this.year = CatalogUtil.getCatalogYearForGrouperModel(dateForCatalog, CpxClientConfig.instance().getSelectedGrouper());
        refresh();
    }

    protected Callback<TableColumn<TCaseIcd, TCaseIcd>, TableCell<TCaseIcd, TCaseIcd>> getSecIcdCellFactory() {
        return new Callback<TableColumn<TCaseIcd, TCaseIcd>, TableCell<TCaseIcd, TCaseIcd>>() {
            @Override
            public TableCell<TCaseIcd, TCaseIcd> call(TableColumn<TCaseIcd, TCaseIcd> param) {
                TableCell<TCaseIcd, TCaseIcd> cell = new TableCell<>() {
                    @Override
                    protected void updateItem(TCaseIcd item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            return;
                        }
                        TCaseIcd icd = item;
                        String secDiag = "";
                        String secDiagTooltipTxt = "";
                        //go through Array of referenced Diagnosis and adds identifier
                        setText("");
                        setGraphic(null);
                        for (TCaseIcd refIcd : icd.getRefIcds()) {
                            CIcdCatalog icdCat = null;
                            //Bugfix CPX-793 RSH -20180411
                            if ((refIcd.getIcdcReftypeEn() != null) && (refIcd.getIcdcCode() != null) && icd.getIcdcCode() != null) {
                                switch (refIcd.getIcdcReftypeEn()) {
                                    case Kreuz:
                                        secDiag = secDiag.concat(refIcd.getIcdcCode() + "+ ");
                                        icdCat = CpxIcdCatalog.instance().getByCode(refIcd.getIcdcCode(), lang, year);
                                        secDiagTooltipTxt = secDiagTooltipTxt.concat(refIcd.getIcdcCode() + ": " + icdCat.getIcdDescription() + "\n");
                                        break;
                                    case Stern:
                                        secDiag = secDiag.concat(refIcd.getIcdcCode() + "* ");
                                        icdCat = CpxIcdCatalog.instance().getByCode(refIcd.getIcdcCode(), lang, year);
                                        secDiagTooltipTxt = secDiagTooltipTxt.concat(secDiagTooltipTxt + refIcd.getIcdcCode() + ": " + icdCat.getIcdDescription() + "\n");
                                        break;
                                    case Zusatz:
                                        secDiag = secDiag.concat(refIcd.getIcdcCode() + "! ");
                                        icdCat = CpxIcdCatalog.instance().getByCode(refIcd.getIcdcCode(), lang, year);
                                        secDiagTooltipTxt = secDiagTooltipTxt.concat(secDiagTooltipTxt + refIcd.getIcdcCode() + ": " + icdCat.getIcdDescription() + "\n");
                                        break;
                                    case ZusatzZu:
                                        secDiag = secDiag.concat(refIcd.getIcdcCode());
                                        icdCat = CpxIcdCatalog.instance().getByCode(refIcd.getIcdcCode(), lang, year);
                                        secDiagTooltipTxt = secDiagTooltipTxt.concat(secDiagTooltipTxt + refIcd.getIcdcCode() + ": " + icdCat.getIcdDescription() + "\n");
                                        break;

                                }
                            }
                        }
                        setText(secDiag);
                        setTooltip(new Tooltip(secDiagTooltipTxt));
                    }
                };
                return cell;
            }
        };
    }

    private void setUpLocalisationColumn(Double pWidth) {
//        icdLocalisationColumn.setCellValueFactory(new SimpleCellValueFactory<>());
//CPX-997 Sort icdLocalisationColumn
        icdLocalisationColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TCaseIcd, LocalisationEn>, ObservableValue<LocalisationEn>>() {
            @Override
            public ObservableValue<LocalisationEn> call(TableColumn.CellDataFeatures<TCaseIcd, LocalisationEn> param) {

                return new SimpleObjectProperty<>(param.getValue().getIcdcLocEn());

            }
        });
        icdLocalisationColumn.setCellFactory(new Callback<TableColumn<TCaseIcd, LocalisationEn>, TableCell<TCaseIcd, LocalisationEn>>() {
            @Override
            public TableCell<TCaseIcd, LocalisationEn> call(TableColumn<TCaseIcd, LocalisationEn> param) {
                TableCell<TCaseIcd, LocalisationEn> cell = new TableCell<TCaseIcd, LocalisationEn>() {
                    @Override
                    protected void updateItem(LocalisationEn item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            return;
                        }
//                        TCaseIcd icd = (TCaseIcd) item;
                        setText(item == LocalisationEn.E ? "" : item.name());
                    }
                };
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });
        this.getColumns().add(icdLocalisationColumn);
//       icdLocalisationColumn.setPercentageWidth(pWidth);
    }

    /**
     * forces TableView to resize/recalculate Column-Widths, for all columns
     * ToDo: dynamic resizing, returns to default sizes
     */
    public void resizeColumns() {
//        for(Object colObj : getColumns()){
//            TableColumn col = (TableColumn) colObj;
//            if(col.isVisible()){
//                this.resizeColumn(col, 0.2);
//            }
//        }
//        principalDiagnosisColumn.setPercentageWidth(computeColumnPercentage(0.04, weight));
        /*
        principalDiagnosisColumn.setFixWidth(35);
//        useForGroupColumn.setPercentageWidth(computeColumnPercentage(0.03, weight));
        useForGroupColumn.setFixWidth(35);
        icdCodeColumn.setPercentageWidth(computeColumnPercentage(0.07, weight));
                                                                //useForGroupColumn.isVisible()?0.65:0.05+0.65
        icdTextColumn.setPercentageWidth(computeColumnPercentage(useForGroupColumn.isVisible()?0.48:0.5, weight));
        secIcdCodeColumn.setPercentageWidth(computeColumnPercentage(0.23, weight));
        icdLocalisationColumn.setPercentageWidth(computeColumnPercentage(0.13, weight));
         */

        principalDiagnosisColumn.setMinWidth(45);
        principalDiagnosisColumn.setMaxWidth(45);
        useForGroupColumn.setMinWidth(35);
        useForGroupColumn.setMaxWidth(35);
        icdCodeColumn.setMinWidth(100);
        icdCodeColumn.setMaxWidth(100);

        icdTextColumn.setMinWidth(200);

        secIcdCodeColumn.setMinWidth(150);
        secIcdCodeColumn.setMaxWidth(150);
        icdLocalisationColumn.setMinWidth(100);
        icdLocalisationColumn.setMaxWidth(100);
    }

    public void resetMainDiagnosis() {
        for (TCaseIcd icd : getItems()) {
            if (icd.getIcdcIsHdxFl()) {
                icd.setIcdcIsHdxFl(false);
//                if(controller != null){
//                    ((CaseDetailsCaseResolveFXMLController)controller).saveIcd(icd);
//                }
            }
        }
    }

    public void resetMainDpDiagnosis() {
        for (TCaseIcd icd : getItems()) {
            if (icd.getIcdcIsHdbFl()) {
                icd.setIcdcIsHdbFl(false);
            }
        }
    }
}
