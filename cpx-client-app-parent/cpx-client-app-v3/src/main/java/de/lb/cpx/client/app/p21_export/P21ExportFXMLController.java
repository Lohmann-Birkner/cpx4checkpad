/* 
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.p21_export;

import de.lb.cpx.client.app.wm.util.DocumentManager;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.menu.fx.ToolbarMenuFXMLController;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.model.fx.file_chooser.FileChooserFactory;
import de.lb.cpx.client.core.model.fx.labeled.LabeledCheckBox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledComboBox;
//import de.lb.cpx.client.core.model.fx.labeled.LabeledDoubleTextField;
import de.lb.cpx.client.core.model.fx.labeled.LabeledDoubleTextField;
//import de.lb.cpx.client.core.model.fx.labeled.LabeledIntegerTextField;
import de.lb.cpx.client.core.model.fx.labeled.LabeledIntegerTextField;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextField;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.shared.p21util.P21CostUnitOfHospitalEn;
import de.lb.cpx.shared.p21util.P21ExportSettings;
import de.lb.cpx.shared.p21util.P21TypeOfHospitalEn;
import de.lb.cpx.shared.p21util.P21Version;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

/**
 * FXML Controller class
 *
 * @author niemeier
 */
public class P21ExportFXMLController extends Controller<CpxScene> {

    private static final Logger LOG = Logger.getLogger(P21ExportFXMLController.class.getName());

    @FXML
    private AnchorPane root;
    @FXML
    private LabeledTextField txtTargetFolder;
    @FXML
    private LabeledComboBox<P21Version> cbP21Version;
    @FXML
    private Glyph glExplorer;
    @FXML
    private LabeledTextField txtEmail;
    @FXML
    private LabeledTextField txtEmail2;
    @FXML
    private LabeledCheckBox chkZip;
    @FXML
    private Button btnSelectFolder;
    @FXML
    private LabeledTextField txtHospitalName;
    @FXML
    private LabeledComboBox<P21TypeOfHospitalEn> cbTypeOfHospital;
    @FXML
    private LabeledComboBox<P21CostUnitOfHospitalEn> cbCostUnitOfHospital;
    @FXML
    private LabeledIntegerTextField txtBedsDrg;
    @FXML
    private LabeledIntegerTextField txtBedsDrgIntensiv;
    @FXML
    private LabeledIntegerTextField txtBedsPepp;
    @FXML
    private LabeledIntegerTextField txtBedsPeppIntensiv;
    @FXML
    private LabeledDoubleTextField txtSurcharges;
    @FXML
    private LabeledCheckBox chkRegionalPensionObligation;
    protected ValidationSupport validationSupport = new ValidationSupport();
    @FXML
    private LabeledIntegerTextField txtIdent;
    @FXML
    private LabeledCheckBox chkAnonymizeHospital;
    @FXML
    private LabeledCheckBox chkAnonymizePatient;
    @FXML
    private LabeledCheckBox chkAnonymizeCase;

    /**
     * Initializes the controller class.
     *
     * @param url url
     * @param rb rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        glExplorer.setVisible(true);
        glExplorer.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.EXTERNAL_LINK));
        glExplorer.setOnMouseClicked((event) -> {
            if (MouseButton.PRIMARY.equals(event.getButton()) && event.getClickCount() == 1) {
                File file = new File(getTargetFolder());
                while (true) {
                    if (file == null) {
                        break;
                    }
                    if (file.exists()) {
                        ToolbarMenuFXMLController.openInExplorer(file.getAbsolutePath());
                        break;
                    } else {
                        file = file.getParentFile();
                    }
                }
            }
        });

        cbP21Version.getItems().addAll(P21Version.getExportVersions());
        cbP21Version.getSelectedItemProperty().addListener(new ChangeListener<P21Version>() {
            @Override
            public void changed(ObservableValue<? extends P21Version> observable, P21Version oldValue, P21Version newValue) {
                if (newValue == null) {
                    return;
                }
                if (newValue.getYear() >= 2020) {
                    txtBedsDrgIntensiv.setVisible(true);
                    txtBedsPeppIntensiv.setVisible(true);
                    setupValidators(true);
                } else {
                    txtBedsDrgIntensiv.setVisible(false);
                    txtBedsPeppIntensiv.setVisible(false);
                    setupValidators(false);
                }
            }        
        });

        cbTypeOfHospital.getItems().addAll(P21TypeOfHospitalEn.values());
        cbCostUnitOfHospital.getItems().addAll(P21CostUnitOfHospitalEn.values());

        final P21ExportSettings settings = CpxClientConfig.instance().getUserP21ExportSettings();
        setP21ExportSettings(settings);

     }

    private void setupValidators(final boolean withCheckIntensivBeds) {
//        validationSupport.initInitialDecoration();

        Platform.runLater(() -> {
            //final BooleanProperty caseNumberValidProp = new SimpleBooleanProperty();

            validationSupport.registerValidator(cbP21Version.getControl(), new Validator<P21Version>() {
                @Override
                public ValidationResult apply(Control t, P21Version u) {
                    ValidationResult res = new ValidationResult();
                    //if (u != null) {
                    res.addErrorIf(t, "P21-Version nicht ausgewählt", cbP21Version.getSelectedItem() == null);
                    //}
                    return res;
                }
            });

            validationSupport.registerValidator(txtEmail.getControl(), new Validator<String>() {
                @Override
                public ValidationResult apply(Control t, String u) {
                    ValidationResult res = new ValidationResult();
                    //if (u != null) {
                    final String mail = getMail();
                    res.addErrorIf(t, "E-Mail-Adresse fehlt", mail.isEmpty());
                    res.addErrorIf(t, "E-Mail-Adresse ungültig", !mail.isEmpty() && !mail.contains("@"));
                    //}
                    return res;
                }
            });

            validationSupport.registerValidator(txtEmail2.getControl(), new Validator<String>() {
                @Override
                public ValidationResult apply(Control t, String u) {
                    ValidationResult res = new ValidationResult();
                    //if (u != null) {
                    final String mail2 = getMail2();
                    //res.addErrorIf(t, "E-Mail-Adresse fehlt", mail.isEmpty());
                    res.addErrorIf(t, "E-Mail-Adresse ungültig", !mail2.isEmpty() && !mail2.contains("@"));
                    //}
                    return res;
                }
            });

            validationSupport.registerValidator(txtHospitalName.getControl(), new Validator<String>() {
                @Override
                public ValidationResult apply(Control t, String u) {
                    ValidationResult res = new ValidationResult();
                    //if (u != null) {
                    res.addErrorIf(t, "Krankenhausname fehlt", getHosName().isEmpty());
                    //}
                    return res;
                }
            });

            validationSupport.registerValidator(txtIdent.getControl(), new Validator<String>() {
                @Override
                public ValidationResult apply(Control t, String u) {
                    ValidationResult res = new ValidationResult();
                    //if (u != null) {
                    final String ident = getIdent();
                    res.addErrorIf(t, "IK fehlt", ident.isEmpty());
                    res.addErrorIf(t, "IK muss genau 9 Zeichen lang sein", !ident.isEmpty() && ident.length() != 9);
                    //}
                    return res;
                }
            });

            validationSupport.registerValidator(cbTypeOfHospital.getControl(), new Validator<P21TypeOfHospitalEn>() {
                @Override
                public ValidationResult apply(Control t, P21TypeOfHospitalEn u) {
                    ValidationResult res = new ValidationResult();
                    //if (u != null) {
                    res.addErrorIf(t, "Art des Krankenhauses fehlt", cbTypeOfHospital.getSelectedItem() == null);
                    //}
                    return res;
                }
            });

            validationSupport.registerValidator(cbCostUnitOfHospital.getControl(), new Validator<P21CostUnitOfHospitalEn>() {
                @Override
                public ValidationResult apply(Control t, P21CostUnitOfHospitalEn u) {
                    ValidationResult res = new ValidationResult();
                    //if (u != null) {
                    res.addErrorIf(t, "Träger des Krankenhauses fehlt", cbCostUnitOfHospital.getSelectedItem() == null);
                    //}
                    return res;
                }
            });

            validationSupport.registerValidator(txtBedsDrg.getControl(), new Validator<String>() {
                @Override
                public ValidationResult apply(Control t, String u) {
                    ValidationResult res = new ValidationResult();
                    //if (u != null) {
                    res.addErrorIf(t, "Betten-DRG fehlt oder ungültig", getBedsDrg() == null);
                    //}
                    return res;
                }
            });
            validationSupport.registerValidator(txtBedsPepp.getControl(), new Validator<String>() {
                @Override
                public ValidationResult apply(Control t, String u) {
                    ValidationResult res = new ValidationResult();
                    //if (u != null) {
                    res.addErrorIf(t, "Betten-PSY fehlt oder ungültig", getBedsPepp() == null);
                    //}
                    return res;
                }
            });
            if (withCheckIntensivBeds) {
                    validationSupport.registerValidator(txtBedsDrgIntensiv.getControl(), new Validator<String>() {
                        @Override
                        public ValidationResult apply(Control t, String u) {
                            ValidationResult res = new ValidationResult();
                            //if (u != null) {
                            res.addErrorIf(t, "Betten-DRG-Intensiv fehlt oder ist ungültig", getBedsDrgIntensiv() == null);
                            //}
                            return res;
                        }
                    });

                    validationSupport.registerValidator(txtBedsPeppIntensiv.getControl(), new Validator<String>() {
                        @Override
                        public ValidationResult apply(Control t, String u) {
                            ValidationResult res = new ValidationResult();
                            //if (u != null) {
                            res.addErrorIf(t, "Betten-PSY-Intensiv fehlt oder ist ungültig", getBedsPeppIntensiv() == null);
                            //}
                            return res;
                        }
                    });
            }
            validationSupport.registerValidator(txtTargetFolder.getControl(), new Validator<String>() {
                @Override
                public ValidationResult apply(Control t, String u) {
                    ValidationResult res = new ValidationResult();
                    //if (u != null) {
                    final String targetFolder = StringUtils.trimToEmpty(txtTargetFolder.getText());
                    final File file = new File(targetFolder);
                    res.addErrorIf(t, "Zielordner nicht angegeben", targetFolder.isEmpty());
                    res.addErrorIf(t, "Zielordner ist ungültig", !targetFolder.isEmpty() && !DocumentManager.isValidFilepath(targetFolder));
                    res.addErrorIf(t, "Zielordner ist kein Verzeichnis", !targetFolder.isEmpty() && file.exists() && !file.isDirectory());
                    //res.addErrorIf(t, "Zielordner existiert nicht", !targetFolder.isEmpty() && !file.exists());
                    //res.addErrorIf(t, "Zielordner ist kein Verzeichnis", !targetFolder.isEmpty() && file.exists() && !file.isDirectory());
                    //}
                    return res;
                }
            });

            validationSupport.registerValidator(chkRegionalPensionObligation.getControl(), new Validator<Boolean>() {
                @Override
                public ValidationResult apply(Control t, Boolean u) {
                    ValidationResult res = new ValidationResult();
                    //if (u != null) {
                    res.addErrorIf(t, "Reg. Versorgungsverpflichtung wurde nicht angegeben", chkRegionalPensionObligation.getControl().isIndeterminate());
                    //}
                    return res;
                }
            });
        });
    }

    public void init() {
        //
    }

    public String getTargetFolder() {
        return StringUtils.trimToEmpty(txtTargetFolder.getText());
    }

    public boolean isZip() {
        return chkZip.isChecked();
    }

    public boolean isAnonymizeHospital() {
        return chkAnonymizeHospital.isChecked();
    }

    public boolean isAnonymizePatient() {
        return chkAnonymizePatient.isChecked();
    }

    public boolean isAnonymizeCase() {
        return chkAnonymizeCase.isChecked();
    }

    public P21Version getVersion() {
        return cbP21Version.getSelectedItem();
    }

    public String getIdent() {
        return StringUtils.trimToEmpty(txtIdent.getText());
    }

    public String getMail() {
        return StringUtils.trimToEmpty(txtEmail.getText());
    }

    public String getMail2() {
        return StringUtils.trimToEmpty(txtEmail2.getText());
    }

    public String getHosName() {
        return StringUtils.trimToEmpty(txtHospitalName.getText());
    }

    public P21TypeOfHospitalEn getHosType() {
        return cbTypeOfHospital.getSelectedItem();
    }

    public P21CostUnitOfHospitalEn getHosCostUnit() {
        return cbCostUnitOfHospital.getSelectedItem();
    }

    public Integer getBedsDrg() {
        try {
            return Integer.valueOf(txtBedsDrg.getText());
        } catch (NumberFormatException ex) {
            LOG.log(Level.FINEST, null, ex);
            return null;
        }
    }

    public Integer getBedsDrgIntensiv() {
        try {
            return Integer.valueOf(txtBedsDrgIntensiv.getText());
        } catch (NumberFormatException ex) {
            LOG.log(Level.FINEST, null, ex);
            return null;
        }
    }

    public Integer getBedsPepp() {
        try {
            return Integer.valueOf(txtBedsPepp.getText());
        } catch (NumberFormatException ex) {
            LOG.log(Level.FINEST, null, ex);
            return null;
        }
    }

    public Integer getBedsPeppIntensiv() {
        try {
            return Integer.valueOf(txtBedsPeppIntensiv.getText());
        } catch (NumberFormatException ex) {
            LOG.log(Level.FINEST, null, ex);
            return null;
        }
    }

    public Double getSurcharges() {
        try {
            return Double.valueOf(txtSurcharges.getText());
        } catch (NumberFormatException ex) {
            LOG.log(Level.FINEST, null, ex);
            return null;
        }
    }

    public boolean getRegionalPensionObligation() {
        return chkRegionalPensionObligation.isChecked();
    }

    public P21ExportSettings getP21ExportSettings() {
        return new P21ExportSettings(getTargetFolder(),
                isZip(),
                getVersion(),
                getIdent(),
                getMail(),
                getMail2(),
                getHosName(),
                getHosType(),
                getHosCostUnit(),
                getBedsDrg(),
                getBedsDrgIntensiv(),
                getBedsPepp(),
                getBedsPeppIntensiv(),
                getSurcharges(),
                getRegionalPensionObligation(),
                isAnonymizeHospital(),
                isAnonymizePatient(),
                isAnonymizeCase()
        );
    }

    public void setP21ExportSettings(final P21ExportSettings pP21ExportSettings) {
        if (pP21ExportSettings == null) {
            return;
        }
        txtTargetFolder.setText(pP21ExportSettings.getTargetFolder());
        cbP21Version.select(pP21ExportSettings.getVersionEn());
        txtEmail.setText(pP21ExportSettings.getMail());
        txtEmail2.setText(pP21ExportSettings.getMail2());
        txtIdent.setText(pP21ExportSettings.getIdent());
        txtHospitalName.setText(pP21ExportSettings.getHosName());
        cbTypeOfHospital.select(pP21ExportSettings.getHosTypeEn());
        cbCostUnitOfHospital.select(pP21ExportSettings.getHosCostUnitEn());
        txtBedsDrg.setValue(pP21ExportSettings.getBedsDrg());
        txtBedsDrgIntensiv.setValue(pP21ExportSettings.getBedsDrgIntensiv());
        txtBedsDrgIntensiv.getControl().prefWidthProperty().unbind();
        txtBedsDrgIntensiv.getControl().prefWidthProperty().bind(txtBedsDrg.getControl().widthProperty());
        txtBedsPepp.setValue(pP21ExportSettings.getBedsPepp());
        txtBedsPepp.getControl().prefWidthProperty().unbind();
        txtBedsPepp.getControl().prefWidthProperty().bind(txtBedsDrg.getControl().widthProperty());
        txtBedsPeppIntensiv.setValue(pP21ExportSettings.getBedsPeppIntensiv());
        txtBedsPeppIntensiv.getControl().prefWidthProperty().unbind();
        txtBedsPeppIntensiv.getControl().prefWidthProperty().bind(txtBedsDrg.getControl().widthProperty());
        txtSurcharges.setValue(pP21ExportSettings.getSurcharges());
        if (pP21ExportSettings.isRegionalPensionObligation() == null) {
            chkRegionalPensionObligation.getControl().setIndeterminate(true);
        } else {
            chkRegionalPensionObligation.setSelected(pP21ExportSettings.isRegionalPensionObligation());
        }
        chkZip.setSelected(pP21ExportSettings.isZip());
        chkAnonymizeHospital.setSelected(pP21ExportSettings.isAnonymizeHospital());
        chkAnonymizePatient.setSelected(pP21ExportSettings.isAnonymizePatient());
        chkAnonymizeCase.setSelected(pP21ExportSettings.isAnonymizeCase());
    }

    @FXML
    private void chooseFolder(ActionEvent event) {
        DirectoryChooser dirChooser = FileChooserFactory.instance().createDirectoryChooser();
        //fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("pdf", supportedFileTypes));
        dirChooser.setTitle("Verzeichnis auswählen");
        //String defaultReportName = "report_" + pCase.getCsCaseNumber();
        //fileChooser.setInitialFileName(defaultReportName);
        File file = dirChooser.showDialog(getScene().getWindow());
        CpxClientConfig.instance().setUserRecentFileChooserPath(file);
        if (file != null) {
            String dir = file.getAbsolutePath();
            txtTargetFolder.setText(dir);
            //setFolder(dir);
        }
    }

    public ReadOnlyBooleanProperty invalidProperty() {
        return validationSupport.invalidProperty();
    }

}
