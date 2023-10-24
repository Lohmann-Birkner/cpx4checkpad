/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.startup_ejb;

//import de.lb.cpx.licenses.ReadWriteLicenseFile;
import de.lb.cpx.cpx.license.crypter.License;
import de.lb.cpx.cpx.license.crypter.LicenseWriter;
import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import java.io.File;
import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Produces;

/**
 * After InitBean, it fetch the License File from the defined license directory.
 * Read different parameters (list of Hospitals, list of insCompanies, list of
 * modules, etc).
 *
 * @author nandola
 */
@Singleton
//@LocalBean
@Startup
@DependsOn("InitBean")
public class LicenseReadBean {

//    private String custName;
//    private Date validTilldate;
//    private File file;
//    private Set<String> hospList = new LinkedHashSet<>();
//    private Set<String> insCompList = new LinkedHashSet<>();
////    private List deptList = new ArrayList<>();
//    private Set<String> moduleList = new LinkedHashSet<>();
    public LicenseReadBean() {
    }

    private static String getLicensePath() {
        CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();
        return cpxProps.getCpxServerLicenseDir();
    }

//    @PostConstruct
//    public synchronized void init() {
//        try {
//            String path = getLicensePath();
//            LOG.log(Level.INFO, " fetching License Key file from the license directory: " + path);
//            readLicenseFile();
//        } catch (Exception e) {
//            LOG.log(Level.SEVERE, "Can't load License File", e);
//        }
//    }
//    public void readLicenseFile() {
//        String path = getLicensePath() + "\\License Key.txt";
//        file = new File(path);
//        if (file != null && file.getAbsolutePath() != null && file.exists()) {
//            LicenseWriter licenseFile = new LicenseWriter();
//            LicenseWriter decryptreadLicenseFile = licenseFile.readLicenseFile(file.getAbsolutePath());
//            custName = decryptreadLicenseFile.getCustName();
//            validTilldate = decryptreadLicenseFile.getValidDate();
//            hospList = decryptreadLicenseFile.getHospList();
//            insCompList = decryptreadLicenseFile.getInsCompList();
//            moduleList = decryptreadLicenseFile.getModuleList();
//        }
//
//    }
    @Produces
    public License getLicense() {
        String path = getLicensePath() + "\\" + LicenseWriter.DEFAULT_LICENSE_FILENAME;
        License lic = null;
        File file = new File(path);
        if (file.getAbsolutePath() != null && file.exists()) {
            lic = License.loadFromLicenseFile(file);
        }
        return lic;
    }

//    public String getCustName() {
//        return custName;
//    }
//
//    public Date getLicExpiryDate() {
//        return validTilldate == null ? null : new Date(validTilldate.getTime());
//    }
//
//    public Set<String> getHospList() {
//        return hospList;
//    }
//
//    public Set<String> getInsCompList() {
//        return insCompList;
//    }
//
//    public Set<String> getModuleList() {
//        return moduleList;
//    }
}
