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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.report.generator.xml;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseComment;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseDrg;
import de.lb.cpx.model.TCaseFee;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseIcdGrouped;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.TCaseOpsGrouped;
import de.lb.cpx.model.TCasePepp;
import de.lb.cpx.model.TCaseSupplFee;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.TInsurance;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.model.TPatientDetails;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.DrgCorrTypeEn;
import de.lb.cpx.model.enums.LocalisationEn;
import de.lb.cpx.model.enums.SupplFeeTypeEn;
import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.server.commonDB.dao.CBaserateDao;
import de.lb.cpx.server.commonDB.dao.CDepartmentDao;
import de.lb.cpx.server.commonDB.dao.CDrgCatalogDao;
import de.lb.cpx.server.commonDB.dao.CHospitalDao;
import de.lb.cpx.server.commonDB.dao.CIcdCatalogDao;
import de.lb.cpx.server.commonDB.dao.CInsuranceCompanyDao;
import de.lb.cpx.server.commonDB.dao.COpsCatalogDao;
import de.lb.cpx.server.commonDB.dao.CPeppCatalogDao;
import de.lb.cpx.server.commonDB.model.CIcdCatalog;
import de.lb.cpx.server.commonDB.model.COpsCatalog;
import de.lb.cpx.server.commonDB.model.CdbUsers;
import de.lb.cpx.server.config.CpxServerConfigLocal;
import de.lb.cpx.server.dao.TCaseCommentDao;
import de.lb.cpx.server.dao.TGroupingResultsDao;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.commons.text.StringEscapeUtils;
import org.jdom2.Attribute;
import org.jdom2.CDATA;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

//import de.lb.cpx.client.app.model.catalog.CpxIcdCatalog;
/**
 * XML-Generator can gather different data about particular case and make xml
 * file with proper elements
 *
 * @author hasse, nandola
 */
//public class XmlGenerator implements IXmlGenerator{
@Stateless
public class XmlGenerator {

    private static File tmpFile;
    private static final Logger LOG = Logger.getLogger(XmlGenerator.class.getSimpleName());

    //private Document m_doc = null;
    private GDRGModel grouperModel = null;
//    private TCaseIcd prim_icd = null;
    private Element completeIcdList = null;
    private Element completeOpsList = null;
    private Date csdAdmissionDate;
    @Inject
    private CIcdCatalogDao icdCatalogDao;
    @Inject
    private COpsCatalogDao opsCatalogDao;
    @Inject
    private CDepartmentDao deptDao;
    @Inject
    private CInsuranceCompanyDao insuCompDao;
    @Inject
    private CHospitalDao hospDao;
    @Inject
    private TGroupingResultsDao resultDao;
    @Inject
    private CDrgCatalogDao drgCatalogDao;
    @Inject
    private CBaserateDao baseRateCatalogDao;
    @Inject
    private TCaseCommentDao caseCommentDao;
//    @EJB(name = "CpxServerConfig")
    @Inject
    private CpxServerConfigLocal cpxServerConfig;
    @Inject
    private CPeppCatalogDao peppCatalogDao;
//    @Inject
//    private TWmProcessCaseDao tWmProcessCaseDao;
    private Collection<TCaseIcd> localCaseIcds;
    private Collection<TCaseOps> localCaseOpses;
    private Collection<TCaseIcd> externCaseIcds;
    private Collection<TCaseOps> externCaseOpses;
    private Map<String, TCaseIcd> hmCaseIcds;
    private Map<String, TCaseOps> hmCaseOpses;
    private Collection<String> localCaseIcdCodes;
    private Collection<String> externCaseIcdCodes;
    private Collection<String> localCaseOpsCodes;
    private Collection<String> externCaseOpsCodes;
    private Collection<TCaseIcd> addedIcdList;
    private Collection<TCaseIcd> removedIcdList;
    private Collection<TCaseOps> addedOpsList;
    private Collection<TCaseOps> removedOpsList;
    private File xmlFile;
//    private ClientManager clientManager;
    private CdbUsers cdbUsers;
    private TCaseComment activeComment;
    private final CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();
    public static final String TMPFILE_NAME = "tmpfile_";

    public XmlGenerator() {
    }

    protected static synchronized File createXmlTempFile() throws IOException {
        CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();
        if (cpxProps != null) {
            File dir = new File(new File(cpxProps.getTmpDir(), "reports"), "tmpfiles");
            if (!dir.exists()) {
                if (dir.mkdirs()) {
                    LOG.log(Level.INFO, "temp directory was created: " + dir.getAbsolutePath());
                }
            }
            tmpFile = File.createTempFile(TMPFILE_NAME, ".xml", dir);
        } else {
            tmpFile = File.createTempFile(TMPFILE_NAME, ".xml");  // for testing, store it to tmp dir.
        }
        return tmpFile;
    }

    protected static File createXmlTempFileTest() throws IOException {
        return File.createTempFile(TMPFILE_NAME, ".xml");
    }

    public File generateCaseDataXML(TCase caseObj, GDRGModel actGrouperModel) {
        String xmlFileName = "";
        try {
            if (caseObj != null) {
                //complete_icd_list = new HashSet<TCaseIcd>();
                //complete_ops_list = new HashSet<TCaseOps>();
                grouperModel = actGrouperModel;

//                CdbUsers CdbUsers = new CdbUsers();
//                String current_userName = Session.instance().getCpxUserName();
//                String current_userfirstname = CdbUsers.getUserFirstNameById(Session.instance().getCpxUserId());
                Element root = new Element("casedata");

                Element imageElement = generateImageElement();  // "image" element
                if (imageElement.getContent() != null && !imageElement.getContent().isEmpty()) {
                    root.addContent(imageElement);
                }

                root.addContent(generateConfElement());
                root.addContent(generateUserElement());
                root.addContent(generatePatientElement(caseObj.getPatient()));
                root.addContent(generateCaseElement(caseObj));

                Document doc = new Document(root);
                //doc.setRootElement(root);

                XMLOutputter xmlOutput = new XMLOutputter();
                xmlOutput.setFormat(Format.getPrettyFormat());
                xmlOutput.getFormat().setEncoding("UTF-8");

                long caseId = caseObj.getId();
                List<TCaseComment> listOfCaseComments = caseCommentDao.findAllForCase(caseId);
                if (listOfCaseComments != null && !listOfCaseComments.isEmpty()) {
                    listOfCaseComments.forEach(new Consumer<TCaseComment>() {
                        @Override
                        public void accept(TCaseComment t) {
                            if (t == null) {
                                LOG.log(Level.INFO, "Case comment is null for case id {0}", caseId);
                                return;
                            }
                            if (t.isActive()) {
                                activeComment = t;
                                char[] text = activeComment.getText();
                                if (text != null && text.length > 0) {
//                                    String activeCommentText = String.valueOf(text).replaceAll("\\p{C}", "");   // also removes \n, \t, \r
                                    String activeCommentText = String.valueOf(text).replaceAll("\u001e", "").replaceAll("\u001c", "").replaceAll("\u0013", "");
//                                    String activeCommentText = String.valueOf(text).replaceAll("\\P{Print}", "");  // allowes only printable characters but it removes some German language characters like (ö,ä,ü,§,ß, etc.)
                                    if (activeCommentText != null && !activeCommentText.isEmpty()) {
                                        root.addContent(generateActiveCommentElement(activeCommentText));
                                    }
                                }
                            }
                        }
                    });
                }

//                TCaseComment activeComment = caseCommentDao.findActiveComment(case_obj.getId(), CommentType.caseReview);
//                if (activeComment != null) {
//                    activeComment.getText();
//                }
//                Set<TCaseComment> caseComments = case_obj.getCaseComments();
                xmlFile = createXmlTempFile();
//                xmlFile = createXmlTempFileTest();
                xmlFileName = xmlFile.getAbsolutePath();
                try (OutputStream os = new FileOutputStream(new File(xmlFileName))) {
                    xmlOutput.output(doc, os);
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, "Cannot print document to the given output stream under " + xmlFileName, ex);
                }
//                try (FileWriter fw = new FileWriter(xmlFile)) {
//                    xmlOutput.output(doc, fw);
//                }
            } else {
                LOG.log(Level.SEVERE, "Case may be null");
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, null, e);
        }
        return xmlFile;
    }

    private Element generateImageElement() throws MalformedURLException {
        Element image = new Element("image");
        String str = cpxServerConfig.getPdfReportImageFilePath();

        /*        CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();  
        String str = null;
        if (cpxProps != null && !cpxProps.getCpxHome().isEmpty()) {
               String pdfReportType = cpxServerConfig.getPdfReportType();
            switch (pdfReportType.toLowerCase().trim()) {
                case "none":
//                    str = "";
                    break;
                case "bg":
                    str = cpxProps.getCpxHome() + "reports\\CheckpointKliniklogo_BG.gif";
                    break;
                case "huk":
                    str = cpxProps.getCpxHome() + "reports\\CheckpointKliniklogo_HUK.gif";
                    break;
                default:
                    // anything by default?
//                    str = "";
                    break;
            }   
        } else {
            LOG.log(Level.SEVERE, "No CPX_HOME available! Cannot render image");
            return image;
        }  */
        if (str != null && !str.isEmpty()) {
            final File file;
            if (str.startsWith("reports\\")) {
                file = new File(cpxProps.getCpxHome() + cpxProps.getFileSeparator() + str);
            } else {
                file = new File(str);
            }
            if (!file.exists()) {
                LOG.log(Level.SEVERE, "Image file does not exist and cannot be rendered in report: " + file.getAbsolutePath());
                return image;
//                throw new IllegalArgumentException("Bilddatei \"" + str + "\" existiert nicht und kann im Report nicht gerendert werden");
            }
            if (!file.isFile()) {
                LOG.log(Level.SEVERE, "Image file is not a file but a directory and cannot be rendered in report: " + file.getAbsolutePath());
                return image;
//                throw new IllegalArgumentException("Die Bilddatei ist keine Datei, sondern ein Verzeichnis und kann nicht im Report gerendert werden");
            }
            if (!file.canRead()) {
                LOG.log(Level.SEVERE, "Image file is not readable and cannot be rendered in report: " + file.getAbsolutePath());
                return image;
//                throw new IllegalArgumentException("Die Bilddatei ist nicht lesbar und kann nicht im Report gerendert werden");
            }
            URI uri = file.toURI();
//        Attribute attr = new Attribute("src", formatAll("/img/CheckpointKliniklogo.gif"));
            Attribute attr = new Attribute("src", formatAll(uri.toURL()));
            image.setAttribute(attr);
//        image.addContent("/img/CheckpointKliniklogo.gif");
            image.addContent(uri.toURL().toString());
//        Image imageObj = new Image(ResourceLoader.class.getResourceAsStream("/img/cpxLogo2_.JPG"));
        }
        return image;

    }

    private Element generateUserElement() {
        Element user = new Element("user");
        if (ClientManager.isCdiAvailable()) {
            CdbUsers userEntity = ClientManager.getCurrentCpxUser()!=null?ClientManager.getCurrentCpxUser().getUser():new CdbUsers(); //weird test error due to null in currentCpxUser - do not know why cdi is available in that case
            user.addContent(new Element("user_title").addContent(userEntity.getUTitle()));
            user.addContent(new Element("user_fname").addContent(userEntity.getUFirstName()));
            user.addContent(new Element("user_lname").addContent(userEntity.getULastName()));
            user.addContent(new Element("user_title_fname_lname").addContent(formatAll(userEntity.getUTitle()) + " " + formatAll(userEntity.getUFirstName()) + " " + formatAll(userEntity.getULastName())));
            user.addContent(new Element("user_fullname").addContent(userEntity.getUFullName()));
        } else {
            user.addContent(new Element("user_title").addContent(cdbUsers.getUTitle()));
            user.addContent(new Element("user_fname").addContent(cdbUsers.getULastName()));
            user.addContent(new Element("user_lname").addContent(cdbUsers.getUFirstName()));
            user.addContent(new Element("user_title_fname_lname").addContent(formatAll(cdbUsers.getUTitle()) + " " + formatAll(cdbUsers.getUFirstName()) + " " + formatAll(cdbUsers.getULastName())));
        }
        return user;
    }

    private Element generateActiveCommentElement(String activeCommentText) {
//        Element aComment = new Element("activeCommentofCase");
//        if (activeCommentText != null && !activeCommentText.isEmpty()) {
//            aComment.addContent(activeCommentText);
//        }
//        return aComment;

        Element aComment = new Element("activeCommentofCase");
        boolean simpleBr = false;
        // "\r\n" = CR + LF : Used as a new line character in Windows
        // "\n" = LF (Line Feed) // Used as a new line character in Unix/Mac OS X
        while (activeCommentText.contains("\r\n") || activeCommentText.contains("\n")) {
            Element br = new Element("br");
            String substrMl = "";
            if (activeCommentText.contains("\n")) {
                simpleBr = true;
            }
            if (!simpleBr) {
                substrMl = activeCommentText.substring(0, activeCommentText.indexOf("\r\n") + 1);
            } else {
                substrMl = activeCommentText.substring(0, activeCommentText.indexOf('\n') + 1);
            }
            // "\t" : used to indicate horizontal tab characters
//            if (substrMl.indexOf("\t") != -1) {
//                while (substrMl.indexOf("\t") >= 0) {
//                    br.addContent(new Element("tab").addContent(substrMl.substring(0, substrMl.indexOf("\t") + 1)));
//                    substrMl = substrMl.substring(substrMl.indexOf("\t") + 1);
//                }
//                br.addContent(substrMl);
//                aComment.addContent(br);
//            } else {
            //AWi-20180219: check if string is empty 
            //considered as LineBreak
            //with xls LineBreak will be added when in br-element anything other than txt-element is found 
            if (!simpleBr) {
                String srt = activeCommentText.substring(0, activeCommentText.indexOf("\r\n"));
                if (!srt.isEmpty()) {
                    br.addContent(new Element("txt").addContent(srt));
                } else {
                    br.addContent(new Element("br"));
                }
            } else {
                String srt = activeCommentText.substring(0, activeCommentText.indexOf('\n'));
                if (!srt.isEmpty()) {
                    CDATA cdata = new CDATA(srt);
                    br.addContent(new Element("txt").addContent(cdata));
                    //br.addContent(new Element("txt").addContent(new CDATA(srt)));
                } else {
                    br.addContent(new Element("br"));
                }
            }
            aComment.addContent(br);
//            }
            if (!simpleBr) {
                activeCommentText = activeCommentText.substring(activeCommentText.indexOf("\r\n") + 2);
            } else {
                activeCommentText = activeCommentText.substring(activeCommentText.indexOf('\n') + 1);
            }
            simpleBr = false;

        }
        String substrMl = activeCommentText;
        Element br = new Element("br");
//        if (substrMl.indexOf("\t") != -1) {
//            while (substrMl.indexOf("\t") >= 0) {
//                br.addContent(new Element("tab").addContent(substrMl.substring(0, substrMl.indexOf("\t"))));
//                substrMl = substrMl.substring(substrMl.indexOf("\t") + 1);
//            }
//            br.addContent(substrMl);
//            aComment.addContent(br);
//        } else {
        //CDATA cdata = new CDATA(substrMl);
        br.addContent(new Element("txt").addContent(new CDATA(StringEscapeUtils.escapeXml11(substrMl))));
        aComment.addContent(br);
//        }

        return aComment;
    }

    private Element generatePatientElement(TPatient patientObj) {
        Element patient = new Element("patient");
        if (patientObj != null) {
            patient.addContent(new Element("patientno").addContent(patientObj.getPatNumber()));
            patient.addContent(new Element("name").addContent(patientObj.getPatSecName()));
            patient.addContent(new Element("firstname").addContent(patientObj.getPatFirstName()));
            patient.addContent(new Element("title").addContent(patientObj.getPatTitle()));
            patient.addContent(new Element("birthdate").addContent(formatAll(patientObj.getPatDateOfBirth())));
            patient.addContent(new Element("gender").addContent(formatAll(patientObj.getPatGenderEn())));

            patient.addContent(generatePatientDetailsElement(patientObj.getPatDetailsActual()));

            patient.addContent(generateInsuranceElement(patientObj.getPatInsuranceActual()));
        } else {
            LOG.log(Level.WARNING, "There is no Patient or it could be null");
        }
        return patient;
    }

    private Element generatePatientDetailsElement(TPatientDetails patDetailsObj) {
        Element patientDetails = new Element("patient_details");
        if (patDetailsObj != null) {
            Element address = new Element("address");
            address.addContent(new Element("street").addContent(patDetailsObj.getPatdAddress()));
            address.addContent(new Element("postcode").addContent(patDetailsObj.getPatdZipcode()));
            address.addContent(new Element("city").addContent(formatAll(patDetailsObj.getPatdCity())));
//            address.addContent(new Element("city").addContent(formatAll(pat_details_obj.getPatdState())));
            patientDetails.addContent(address);

            Element contact = new Element("contact");
//            contact.addContent(address);
            contact.addContent(new Element("phone").addContent(patDetailsObj.getPatPhoneNumber()));
            contact.addContent(new Element("mobile").addContent(patDetailsObj.getPatCellNumber()));
            patientDetails.addContent(contact);
        } else {
            LOG.log(Level.WARNING, "There is no Patient Details or it could be null");
        }
        return patientDetails;
    }

    private Element generateInsuranceElement(TInsurance insuranceObj) {
        Element insurance = new Element("insurance");
        if (insuranceObj != null) {
            insurance.addContent(new Element("ins_number").addContent(insuranceObj.getInsNumber()));
            insurance.addContent(new Element("ins_company").addContent(insuranceObj.getInsInsuranceCompany())); //IKZ 
            String insuCompanyName = insuCompDao.getInsuCompName(insuranceObj.getInsInsuranceCompany(), "de");
            insurance.addContent(new Element("ins_name").addContent(formatAll(insuCompanyName)));
            insurance.addContent(new Element("valid_from").addContent(formatAll(insuranceObj.getInsValidFrom())));
            insurance.addContent(new Element("valid_to").addContent(formatAll(insuranceObj.getInsValidTo())));
        } else {
            LOG.log(Level.WARNING, "There is no Insurance or it could be null");
        }
        return insurance;
    }

    private Element generateCaseElement(TCase caseObj) {
        Element caseData = new Element("case");
        if (caseObj != null) {
            caseData.addContent(new Element("case_number").addContent(caseObj.getCsCaseNumber()));
            /*          List<TWmProcess> processesOfCase = tWmProcessCaseDao.getProcessesOfCase(caseObj.getId());
            if (processesOfCase != null && !processesOfCase.isEmpty()) {
                processesOfCase.forEach(new Consumer<TWmProcess>() {
                    @Override
                    public void accept(TWmProcess t) {
                        case_data.addContent(new Element("process_number").addContent(String.valueOf(t.getWorkflowNumber())));
                    }
                });
            } */
            caseData.addContent(new Element("case_type").addContent(formatAll(caseObj.getCsCaseTypeEn())));
            caseData.addContent(new Element("feegroup").addContent(formatAll(caseObj.getCsFeeGroupEn())));
            caseData.addContent(new Element("doctor_ikz").addContent(caseObj.getCsDoctorIdent()));
            caseData.addContent(new Element("hospital_ikz").addContent(caseObj.getCsHospitalIdent()));
            caseData.addContent(new Element("ins_number").addContent(caseObj.getInsuranceNumberPatient()));
            caseData.addContent(new Element("ins_company").addContent(caseObj.getInsuranceIdentifier())); //IKZ 
            String insuCompanyName = insuCompDao.getInsuCompName(caseObj.getInsuranceIdentifier(), "de");
            caseData.addContent(new Element("ins_name").addContent(formatAll(insuCompanyName)));
            String hospName = hospDao.getHospitalName(caseObj.getCsHospitalIdent(), "de");
            caseData.addContent(new Element("hospital_name").addContent(formatAll(hospName)));
            caseData.addContent(new Element("case_status").addContent(formatAll(caseObj.getCsStatusEn())));

            TCaseDetails caseDetailsLocal = caseObj.getCurrentLocal();
            Element caseLocal = new Element("case_local");
            caseLocal.addContent(generateCaseDetailsElement(caseObj, caseDetailsLocal));
            caseData.addContent(caseLocal);
//            case_details_local.getComment();

            TCaseDetails caseDetailsExtern = caseObj.getCurrentExtern();
            Element caseExtern = new Element("case_extern");
            caseExtern.addContent(generateCaseDetailsElement(caseObj, caseDetailsExtern));
            caseData.addContent(caseExtern);

            localCaseIcds = new ArrayList<>();
            localCaseOpses = new ArrayList<>();
            externCaseIcds = new ArrayList<>();
            externCaseOpses = new ArrayList<>();
            localCaseIcdCodes = new ArrayList<>();
            externCaseIcdCodes = new ArrayList<>();
            localCaseOpsCodes = new ArrayList<>();
            externCaseOpsCodes = new ArrayList<>();
            hmCaseIcds = new HashMap<>();
            hmCaseOpses = new HashMap<>();
            addedIcdList = new ArrayList<>();
            removedIcdList = new ArrayList<>();
            addedOpsList = new ArrayList<>();
            removedOpsList = new ArrayList<>();

            if (caseDetailsLocal != null) {
                caseDetailsLocal.getCaseDepartments().forEach(new Consumer<TCaseDepartment>() {
                    @Override
                    public void accept(TCaseDepartment t) {
                        localCaseIcds.addAll(t.getCaseIcds());
                        localCaseOpses.addAll(t.getCaseOpses());
                    }
                });
            }

            if (caseDetailsExtern != null) {
                caseDetailsExtern.getCaseDepartments().forEach(new Consumer<TCaseDepartment>() {
                    @Override
                    public void accept(TCaseDepartment t) {
                        externCaseIcds.addAll(t.getCaseIcds());
                        externCaseOpses.addAll(t.getCaseOpses());
                    }
                });
            }

            localCaseIcds.forEach(new Consumer<TCaseIcd>() {
                @Override
                public void accept(TCaseIcd t) {
                    localCaseIcdCodes.add(t.getIcdcCode() + t.getIcdcLocEn());
//                    hmCaseIcds.putIfAbsent(t.getIcdcCode(), t);
                    hmCaseIcds.put(t.getIcdcCode() + t.getIcdcLocEn(), t);
                }
            });

            externCaseIcds.forEach(new Consumer<TCaseIcd>() {
                @Override
                public void accept(TCaseIcd t) {
                    externCaseIcdCodes.add(t.getIcdcCode() + t.getIcdcLocEn());
                    hmCaseIcds.put(t.getIcdcCode() + t.getIcdcLocEn(), t);
                }
            });

            localCaseOpses.forEach(new Consumer<TCaseOps>() {
                @Override
                public void accept(TCaseOps t) {
//                    localCaseOpsCodes.add(t.getOpscCode());
//                    hmCaseOpses.put(t.getOpscCode(), t);
                    localCaseOpsCodes.add(t.getOpscCode() + (t.getOpscDatum() == null ? "" : t.getOpscDatum()) + t.getOpscLocEn());
                    hmCaseOpses.put(t.getOpscCode() + (t.getOpscDatum() == null ? "" : t.getOpscDatum()) + t.getOpscLocEn(), t);
                }
            });

            externCaseOpses.forEach(new Consumer<TCaseOps>() {
                @Override
                public void accept(TCaseOps t) {
                    externCaseOpsCodes.add(t.getOpscCode() + (t.getOpscDatum() == null ? "" : t.getOpscDatum()) + t.getOpscLocEn());
                    hmCaseOpses.put(t.getOpscCode() + (t.getOpscDatum() == null ? "" : t.getOpscDatum()) + t.getOpscLocEn(), t);
                }
            });

            List<String> icdCodeListLocal = new ArrayList<>(localCaseIcdCodes);
            List<String> icdCodeListExtern = new ArrayList<>(externCaseIcdCodes);
            List<String> opsCodeListLocal = new ArrayList<>(localCaseOpsCodes);
            List<String> opsCodeListExtern = new ArrayList<>(externCaseOpsCodes);

            icdCodeListLocal.removeAll(externCaseIcdCodes);
//            icdCode_list_local.size();
            icdCodeListExtern.removeAll(localCaseIcdCodes);
//            icdCode_list_extern.size();
            opsCodeListLocal.removeAll(externCaseOpsCodes);
//            opsCode_list_local.size();
            opsCodeListExtern.removeAll(localCaseOpsCodes);
//            opsCode_list_extern.size();

            if (!icdCodeListLocal.isEmpty()) {
                icdCodeListLocal.forEach(new Consumer<String>() {
                    @Override
                    public void accept(String t) {
                        String icdKey = t;
                        TCaseIcd addedIcd = hmCaseIcds.get(icdKey);
                        addedIcdList.add(addedIcd);
                    }
                });
            }

            if (!icdCodeListExtern.isEmpty()) {
                icdCodeListExtern.forEach(new Consumer<String>() {
                    @Override
                    public void accept(String t) {
                        String icdKey = t;
                        TCaseIcd removedIcd = hmCaseIcds.get(icdKey);
                        removedIcdList.add(removedIcd);
                    }
                });
            }

            if (!opsCodeListLocal.isEmpty()) {
                opsCodeListLocal.forEach(new Consumer<String>() {
                    @Override
                    public void accept(String t) {
                        String opsKey = t;
                        TCaseOps addedOps = hmCaseOpses.get(opsKey);
//                        Set<TCaseOpsGrouped> caseOpsGroupeds = addedOps.getCaseOpsGroupeds();
//                        caseOpsGroupeds.forEach(new Consumer<TCaseOpsGrouped>() {
//                            @Override
//                            public void accept(TCaseOpsGrouped t) {
//                                if (addedOps.getOpscCode() == t.getCaseOps().getOpscCode()) {
//                                    if (t.getCaseSupplFees() != null) {
//                                        String csuplfeeCode = t.getCaseSupplFees().getCsuplfeeCode();
//                                        String ze = String.valueOf(t.getCaseSupplFees().getCsuplValue());
//                                        String currencySymbol = Lang.getCurrencySymbol();
//                                        String toString = (csuplfeeCode + " (" + (ze) + " " + currencySymbol + ")").toString();
//                                    } else {
//                                         String ze = "(0,00" + Lang.getCurrencySymbol() + ")";
//                                    }
//                                }
//                            }
//                        });
                        addedOpsList.add(addedOps);
                    }
                });
            }

            if (!opsCodeListExtern.isEmpty()) {
                opsCodeListExtern.forEach(new Consumer<String>() {
                    @Override
                    public void accept(String t) {
                        String opsKey = t;
                        TCaseOps removedOps = hmCaseOpses.get(opsKey);
                        removedOpsList.add(removedOps);
                    }
                });
            }

            Element changes = new Element("changes");
            changes.addContent(generateAddedIcdElements(addedIcdList));
            changes.addContent(generateRemovedIcdElements(removedIcdList));
            changes.addContent(generateAddedOpsElements(addedOpsList));
            changes.addContent(generateRemovedOpsElements(removedOpsList));
            caseData.addContent(changes);

//            localCaseIcds.size();
//            localCaseIcds.clear();
//            localCaseOpses.size();
//            localCaseOpses.clear();
//            TGroupingResults resultOfLocal = resultDao.findGroupingResult_nativ_lazy(case_details_local.getId(), grouper_model);
//            String drgDescription = drgCatalogDao.getByCode(resultOfLocal.getGrpresCode(), "de", Lang.toYear(case_details_local.getCsdAdmissionDate()));
//            TGroupingResults resultOfExtern = resultDao.findGroupingResult_nativ_lazy(case_details_extern.getId(), grouper_model);
        } else {
            LOG.log(Level.WARNING, "There is no Case or it could be null");
        }
        return caseData;
    }

    private Element generateCaseDetailsElement(TCase caseObj, TCaseDetails caseDetails) {
        completeIcdList = new Element("all_icds");
        completeOpsList = new Element("all_opses");

        Element caseDetail = new Element("case_details");
        if (caseDetails != null) {
            caseDetail.addContent(new Element("case_details_comment").addContent(caseDetails.getComment()));
            caseDetail.addContent(new Element("case_details_leave_days").addContent(formatAll(caseDetails.getCsdLeave())));
            caseDetail.addContent(new Element("case_details_los").addContent(formatAll(caseDetails.getCsdLos())));
            caseDetail.addContent(new Element("case_details_los_alteration_negativ").addContent(formatAll((caseDetails.getCsdLosAlteration()== null?0:caseDetails.getCsdLosAlteration() )< 0)));
            caseDetail.addContent(new Element("case_details_los_alteration").addContent(formatAll(Math.abs(caseDetails.getCsdLosAlteration() == null?0:caseDetails.getCsdLosAlteration()))));
            caseDetail.addContent(new Element("case_details_los_md_alteration").addContent(formatAll(Math.abs(caseDetails.getCsdLosMdAlteration() == null?0:caseDetails.getCsdLosMdAlteration()))));
            caseDetail.addContent(new Element("case_details_los_intensiv").addContent(formatAll(caseDetails.getCsdLosIntensiv())));
            caseDetail.addContent(new Element("case_details_age_days").addContent(formatAll(caseDetails.getCsdAgeDays())));
            caseDetail.addContent(new Element("case_details_age_years").addContent(formatAll(caseDetails.getCsdAgeYears())));
            caseDetail.addContent(new Element("artificial_respiration").addContent(formatAll(caseDetails.getCsdHmv())));
            int tob = (caseDetails.getCsdLeave() == null?0:caseDetails.getCsdLeave()) - (caseDetails.getCsdLosAlteration() == null?0:(caseDetails.getCsdLosAlteration().intValue()));
            caseDetail.addContent(new Element("case_details_leave_with_los_alteration").addContent(formatAll(tob)));
            
            //GKr
            caseDetail.addContent(new Element("csd_los_alteration").addContent(formatAll(caseDetails.getCsdLosAlteration() == null?0:caseDetails.getCsdLosAlteration())));
            caseDetail.addContent(new Element("csd_los_md_alteration").addContent(formatAll(caseDetails.getCsdLosMdAlteration() == null?0:caseDetails.getCsdLosMdAlteration())));

//            case_details.getHdIcdCode();
            //admission
            Element admission = new Element("admission");
            admission.addContent(new Element("adm_cause").addContent(formatAll(caseDetails.getCsdAdmCauseEn())));

//            String admReason12 = AbstractLang.lookupStaticTranslation("de", Lang.ADMISSION_REASON_HOSPITAL_CARE_INPATIENT);
//            admission.addContent(new Element("adm_reason12").addContent(admReason12));
            // Note: the Lang.java class is only used for client to set either DE or EN language. If we use on server, it always gives results in EN??
            admission.addContent(new Element("adm_reason12").addContent(caseDetails.getCsdAdmReason12En().getTranslation().value));

//            String admReason34 = AbstractLang.lookupStaticTranslation("de", Lang.ADMISSION_REASON_2_REGULAR_CASE);
//            admission.addContent(new Element("adm_reason34").addContent(admReason34));
            admission.addContent(new Element("adm_reason34").addContent(caseDetails.getCsdAdmReason34En().getTranslation().value));

            admission.addContent(new Element("adm_date").addContent(formatAll(caseDetails.getCsdAdmissionDate())));
            csdAdmissionDate = caseDetails.getCsdAdmissionDate();
            admission.addContent(new Element("adm_weight").addContent(caseDetails.getCsdAdmissionWeight() == null ? "" : formatAll(caseDetails.getCsdAdmissionWeight())));
            admission.addContent(new Element("adm_mode").addContent(formatAll(caseDetails.getCsdAdmodEn())));
            admission.addContent(new Element("adm_by_law").addContent(formatAll(caseDetails.getCsdAdmLawEn())));
            caseDetail.addContent(admission);
            caseDetail.addContent(new Element("adm_year").addContent(formatAll(caseDetails.getCsdAdmissionYear())));

            //discharge
            Element discharge = new Element("discharge");
//            String disReason12 = AbstractLang.lookupStaticTranslation("de", Lang.DISCHARGE_REASON_COMPLETED_TREATMENT_REGULARLY);
//            discharge.addContent(new Element("dis_reason12").addContent(disReason12));
            discharge.addContent(new Element("dis_reason12").addContent(caseDetails.getCsdDisReason12En() == null ? "" : formatAll(caseDetails.getCsdDisReason12En().getTranslation().value)));
//            String disReason3 = AbstractLang.lookupStaticTranslation("de", Lang.DISCHARGE_REASON_2_ABLE_TO_WORK);
//            discharge.addContent(new Element("dis_reason3").addContent(disReason3));
            discharge.addContent(new Element("dis_reason3").addContent(caseDetails.getCsdDisReason3En() == null ? "" : formatAll(caseDetails.getCsdDisReason3En().getTranslation().value)));

            discharge.addContent(new Element("dis_date").addContent(caseDetails.getCsdDischargeDate() == null ? "" : formatAll(caseDetails.getCsdDischargeDate())));
            caseDetail.addContent(discharge);

            caseDetail.addContent(generateDepartmentElement(caseDetails, caseDetails.getCaseDepartments()));

            caseDetail.addContent(generateFeeElement(caseDetails.getCaseFees()));

            //Ermitteln des Drg Ergebnises
            /*           Set<TGroupingResults> results = case_details.getGroupingResultses();
            Set<TGroupingResults> search_list = new HashSet<>();
            TGroupingResults grouper_result = null;
            for (TGroupingResults result : results) {
                if (result.getCaseIcd().equals(prim_icd)) {
                    search_list.add(result);
                }
            }
            for (TGroupingResults obj : search_list) {
                if (obj.getModelIdEn().name().equals(grouper_model.name())) {
                    grouper_result = obj;
                }
            }   */
//            case_detail.addContent(generateGroupingResultElement(grouper_result));
            caseDetail.addContent(completeIcdList);
            caseDetail.addContent(completeOpsList);

            TGroupingResults groupingResult = resultDao.findGroupingResult_nativ_lazy(caseDetails.getId(), grouperModel);
//            Set<TCaseOpsGrouped> caseOpsGroupeds = groupingResult.getCaseOpsGroupeds();
            if(groupingResult != null && groupingResult instanceof TCaseDrg){
                caseDetail.addContent(new Element("case_details_care_days").addContent(formatAll(groupingResult.getCaseDrg().getDrgcCareDays()))); 
            }else{
                caseDetail.addContent(new Element("case_details_care_days").addContent(formatAll(0))); 
            }
            caseDetail.addContent(generateGroupingResultElement(caseObj, caseDetails, groupingResult));

        } else {
            LOG.log(Level.WARNING, "There is no CaseDetails or it could be null");
        }
        return caseDetail;
    }

    private Element generateDepartmentElement(TCaseDetails caseDetails, Set<TCaseDepartment> departmentList) {
        Element departments = new Element("departments");
        if (departmentList != null) {
            for (TCaseDepartment dep : departmentList) {
                if (dep != null) {
                    Element department = new Element("department");
                    department.addContent(new Element("dep_short_name").addContent(dep.getDepShortName()));
                    department.addContent(new Element("dep_301_key").addContent(dep.getDepKey301()));
                    long deptId = dep.getId();
                    String deptName = deptDao.getDeptName(dep.getDepKey301(), "de");
                    department.addContent(new Element("dep_name").addContent(formatAll(deptName)));
                    department.addContent(new Element("adm_date").addContent(formatAll(dep.getDepcAdmDate())));
                    department.addContent(new Element("adm_mode").addContent(formatAll(dep.getDepcAdmodEn())));
                    department.addContent(new Element("dis_date").addContent(formatAll(dep.getDepcDisDate())));
                    department.addContent(new Element("duration").addContent(formatAll(dep.getDurationLong())));
                    if (dep.getPrincipalCaseIcd() != null) {
                        department.addContent(new Element("prim_icd").addContent(dep.getPrincipalCaseIcd().getIcdcCode()));
//                        prim_icd = dep.getPrincipalCaseIcd();
                    } else {
                        department.addContent(new Element("prim_icd").addContent(""));
                    }
                    department.addContent(new Element("is_adm").addContent(formatAll(dep.getDepcIsAdmissionFl())));
                    department.addContent(new Element("is_dis").addContent(formatAll(dep.getDepcIsDischargeFl())));
                    department.addContent(new Element("is_treat").addContent(formatAll(dep.getDepcIsTreatingFl())));

                    // add ICDs and OPSes elements within a department
                    department.addContent(generateIcdElements(caseDetails, dep.getCaseIcds()));
                    department.addContent(generateOpsElements(dep.getCaseOpses()));

                    departments.addContent(department);

                }
            }
        } else {
            LOG.log(Level.WARNING, "There are no Departments or it could be null");
        }
        return departments;
    }

    private Element generateIcdElements(TCaseDetails caseDetails, Set<TCaseIcd> icdList) {
        // sort Icds based on ICDC Code
        List<TCaseIcd> sortedIcds = icdList.stream().sorted(Comparator.comparing(TCaseIcd::getIcdcCode)).collect(Collectors.toList());
//        SortedSet<TCaseIcd> sortedIcds = new TreeSet<>(Comparator.comparing(TCaseIcd::getIcdcCode));
//        if (sortedIcds != null) {
//            sortedIcds.addAll(icd_list);
//        }

        Element icds = new Element("icds");
        if (sortedIcds != null) {
            int year = Lang.toYear(csdAdmissionDate);
            List<CIcdCatalog> icdDetails = icdCatalogDao.getIcdDetails(sortedIcds, year, "de");
            Map<String, String> hmICDs = new HashMap<>();
            if (icdDetails != null && !icdDetails.isEmpty()) {
                icdDetails.forEach(new Consumer<CIcdCatalog>() {
                    @Override
                    public void accept(CIcdCatalog t) {
                        hmICDs.put(t.getIcdCode(), t.getIcdDescription());
                        if (t.getIcdDescription() == null || t.getIcdDescription().isEmpty()) {
                            LOG.log(Level.INFO, "ICD description is null or empty for the ICD Code {0}", t.getIcdCode());
                        }
                    }
                });
            }
            for (TCaseIcd icd : sortedIcds) {
//                if (icd != null && icd.getIcdIsToGroupFl()) {
                if (icd != null) {
                    Element diagnosis = generateSingleIcdForDeptElement(icd);
                    icds.addContent(diagnosis);
                    if (completeIcdList != null && caseDetails.getCsdIsLocalFl() == false) {
                        Element diagnosis4all = generateSingleIcdElement(icd, hmICDs.get(icd.getIcdcCode()));
                        completeIcdList.addContent(diagnosis4all);
                    } else if (completeIcdList != null && caseDetails.getCsdIsLocalFl() == true) {
                        Element diagnosis4all = generateSingleIcdElementWithAttribute(icd, hmICDs.get(icd.getIcdcCode()));
                        completeIcdList.addContent(diagnosis4all);
                    }
                }
            }
        }
        return icds;
    }

    private Element generateSingleIcdForDeptElement(TCaseIcd icdObj) {
        Element icd = new Element("icd");

        if (icdObj != null) {
            icd.addContent(new Element("is_group").addContent(formatAll(icdObj.getIcdIsToGroupFl())));
            icd.addContent(new Element("icd_code").addContent(icdObj.getIcdcCode()));
            String secIcdCode = "";
            if (icdObj.getRefIcd() != null) {
                secIcdCode = icdObj.getRefIcd().getIcdcCode();
            }
            icd.addContent(new Element("sec_icd_code").addContent(formatAll(secIcdCode)));
            icd.addContent(new Element("is_hdb").addContent(formatAll(icdObj.getIcdcIsHdbFl()))); // main diag of dept flag
            icd.addContent(new Element("is_hdx").addContent(formatAll(icdObj.getIcdcIsHdxFl()))); // main diag of case flag
            icd.addContent(new Element("loc").addContent(formatAll(icdObj.getIcdcLocEn() != LocalisationEn.E ? icdObj.getIcdcLocEn() : "")));
            icd.addContent(new Element("ref_type").addContent(formatAll(icdObj.getIcdcReftypeEn())));
            icd.addContent(new Element("icd_type").addContent(formatAll(icdObj.getIcdcTypeEn())));
        }
        return icd;
    }

    private Element generateSingleIcdElement(TCaseIcd icdObj, String icdDesc) {
        Element icd = new Element("icd");

        if (icdObj != null) {
            icd.addContent(new Element("is_group").addContent(formatAll(icdObj.getIcdIsToGroupFl())));
            icd.addContent(new Element("icd_code").addContent(icdObj.getIcdcCode()));
//            String icdcCode = icd_obj.getIcdcCode();
//            int year = Lang.toYear(csdAdmissionDate);
//            String icdText = icdCatalogDao.getIcdText(icdcCode, "de", year);
//            icd.addContent(new Element("icd_codeText").addContent(icdText));
            icd.addContent(new Element("icd_codeText").addContent(icdDesc));
            String secIcdCode = "";
            if (icdObj.getRefIcd() != null) {
                secIcdCode = icdObj.getRefIcd().getIcdcCode();
            }
            icd.addContent(new Element("sec_icd_code").addContent(formatAll(secIcdCode)));
            icd.addContent(new Element("is_hdb").addContent(formatAll(icdObj.getIcdcIsHdbFl())));
            icd.addContent(new Element("is_hdx").addContent(formatAll(icdObj.getIcdcIsHdxFl())));
            icd.addContent(new Element("loc").addContent(formatAll(icdObj.getIcdcLocEn() != LocalisationEn.E ? icdObj.getIcdcLocEn() : "")));
            icd.addContent(new Element("ref_type").addContent(formatAll(icdObj.getIcdcReftypeEn())));
            icd.addContent(new Element("icd_type").addContent(formatAll(icdObj.getIcdcTypeEn())));
        }
        return icd;
    }

    private Element generateSingleIcdElementWithAttribute(TCaseIcd icdObj, String icdDesc) {
        Element icd = new Element("icd");
        if (icdObj != null) {
            Attribute attr = new Attribute("is_hdb", formatAll(icdObj.getIcdcIsHdbFl()));
            icd.setAttribute(attr);

            icd.addContent(new Element("is_group").addContent(formatAll(icdObj.getIcdIsToGroupFl())));
            icd.addContent(new Element("icd_code").addContent(icdObj.getIcdcCode()));
//            String icdcCode = icd_obj.getIcdcCode();
//            int year = Lang.toYear(csdAdmissionDate);
//            String icdText = icdCatalogDao.getIcdText(icdcCode, "de", year);
//            icd.addContent(new Element("icd_codeText").addContent(icdText));
            icd.addContent(new Element("icd_codeText").addContent(icdDesc));
            String secIcdCode = "";
            if (icdObj.getRefIcd() != null) {
                secIcdCode = icdObj.getRefIcd().getIcdcCode();
            }
            icd.addContent(new Element("sec_icd_code").addContent(formatAll(secIcdCode)));
            icd.addContent(new Element("is_hdb").addContent(formatAll(icdObj.getIcdcIsHdbFl())));
            icd.addContent(new Element("is_hdx").addContent(formatAll(icdObj.getIcdcIsHdxFl())));
            icd.addContent(new Element("loc").addContent(formatAll(icdObj.getIcdcLocEn() != LocalisationEn.E ? icdObj.getIcdcLocEn() : "")));
            icd.addContent(new Element("ref_type").addContent(formatAll(icdObj.getIcdcReftypeEn())));
            icd.addContent(new Element("icd_type").addContent(formatAll(icdObj.getIcdcTypeEn())));
        }
        return icd;
    }

    private Element generateOpsElements(Set<TCaseOps> opsList) {
        // sort Opses based on OPSC Code
        List<TCaseOps> sortedOpses = opsList.stream().sorted(Comparator.comparing(TCaseOps::getOpscCode)).collect(Collectors.toList());
//        SortedSet<TCaseOps> sortedOpses = new TreeSet<>(Comparator.comparing(TCaseOps::getOpscCode));
//        if (sortedOpses != null) {
//            sortedOpses.addAll(ops_list);
//        }
        Element procedures = new Element("opses");
        if (sortedOpses != null) {
            int year = Lang.toYear(csdAdmissionDate);
            List<COpsCatalog> opsDetails = opsCatalogDao.getOpsDetails(sortedOpses, year, "de");
            Map<String, String> hmOPSes = new HashMap<>();
            if (opsDetails != null && !opsDetails.isEmpty()) {
                opsDetails.forEach(new Consumer<COpsCatalog>() {
                    @Override
                    public void accept(COpsCatalog t) {
                        hmOPSes.put(t.getOpsCode(), t.getOpsDescription());
                        if (t.getOpsDescription() == null || t.getOpsDescription().isEmpty()) {
                            LOG.log(Level.INFO, "OPS description is null or empty for the OPS Code {0}", t.getOpsCode());
                        }
                    }
                });
            }
            for (TCaseOps ops : sortedOpses) {
                if (ops != null && ops.getOpsIsToGroupFl()) {
//                if (ops != null) {
                    Element procedur = generateSinlgeOpsForDeptElement(ops);
                    procedures.addContent(procedur);
                    if (completeOpsList != null) {
                        Element prozedur4all = generateSinlgeOpsElement(ops, hmOPSes.get(ops.getOpscCode()));
                        completeOpsList.addContent(prozedur4all);
                    }
                }

                if (ops != null && ops.getOpsIsToGroupFl() == false) {
                    if (completeOpsList != null) {
                        Element prozedur4all = generateSinlgeUngroupedOpsElement(ops, hmOPSes.get(ops.getOpscCode()));
                        completeOpsList.addContent(prozedur4all);
                    }
                }
            }
        }
        return procedures;
    }

    private Element generateSinlgeOpsForDeptElement(TCaseOps opsObj) {
        Element ops = new Element("ops");
        if (opsObj != null) {
            ops.addContent(new Element("is_group").addContent(formatAll(opsObj.getOpsIsToGroupFl())));
            ops.addContent(new Element("ops_code").addContent(opsObj.getOpscCode()));
            ops.addContent(new Element("ops_date").addContent(formatAll(opsObj.getOpscDatum())));
            ops.addContent(new Element("loc").addContent(formatAll(opsObj.getOpscLocEn() != LocalisationEn.E ? opsObj.getOpscLocEn() : "")));
        }
        return ops;
    }

    private Element generateSinlgeOpsElement(TCaseOps opsObj, String opsDesc) {
        Element ops = new Element("ops");
        if (opsObj != null) {
            ops.addContent(new Element("is_group").addContent(formatAll(opsObj.getOpsIsToGroupFl())));
            ops.addContent(new Element("ops_code").addContent(opsObj.getOpscCode()));
//            String opscCode = ops_obj.getOpscCode();
//            int year = Lang.toYear(csdAdmissionDate);
//            String opsText = opsCatalogDao.getOpsText(opscCode, "de", year);
//            ops.addContent(new Element("ops_codeText").addContent(opsText));
            ops.addContent(new Element("ops_codeText").addContent(opsDesc));
            ops.addContent(new Element("ops_date").addContent(formatAll(opsObj.getOpscDatum())));
            ops.addContent(new Element("loc").addContent(formatAll(opsObj.getOpscLocEn() != LocalisationEn.E ? opsObj.getOpscLocEn() : "")));

//            Set<TCaseOpsGrouped> caseOpsGroupeds = ops_obj.getCaseOpsGroupeds();
//            caseOpsGroupeds.forEach(new Consumer<TCaseOpsGrouped>() {
//                private String ze;
//
//                @Override
//                public void accept(TCaseOpsGrouped t) {
//                    if (t.getCaseOps().getOpscCode() != null && ops_obj.getOpscCode() == t.getCaseOps().getOpscCode()) {
//                        if (t.getCaseSupplFees() != null && t.getCaseSupplFees().getCsuplValue() != 0) {
//                            String csuplfeeCode = t.getCaseSupplFees().getCsuplfeeCode();
//                            String csUpl = String.valueOf(t.getCaseSupplFees().getCsuplValue());
//                            String currencySymbol = Lang.getCurrencySymbol();
//                            ze = (csuplfeeCode + " (" + (csUpl) + " " + currencySymbol + ")").toString();
//                            ops.addContent(new Element("ze").addContent(formatAll(ze)));
//                        } else {
//                            ze = "(0,00" + Lang.getCurrencySymbol() + ")";
//                            ops.addContent(new Element("ze").addContent(formatAll(ze)));
//                        }
//                    }
//                }
//            });
        }
        return ops;
    }

    private Element generateSinlgeUngroupedOpsElement(TCaseOps opsObj, String opsDesc) {
        Element ops = new Element("ops");
        if (opsObj != null) {
            ops.addContent(new Element("is_group").addContent(formatAll(opsObj.getOpsIsToGroupFl())));
            ops.addContent(new Element("ops_code").addContent(opsObj.getOpscCode()));
//            String opscCode = ops_obj.getOpscCode();
//            int year = Lang.toYear(csdAdmissionDate);
//            String opsText = opsCatalogDao.getOpsText(opscCode, "de", year);
//            ops.addContent(new Element("ops_codeText").addContent(opsText));
            ops.addContent(new Element("ops_codeText").addContent(opsDesc));
            ops.addContent(new Element("ops_date").addContent(formatAll(opsObj.getOpscDatum())));
            ops.addContent(new Element("loc").addContent(formatAll(opsObj.getOpscLocEn() != LocalisationEn.E ? opsObj.getOpscLocEn() : "")));

            Set<TCaseOpsGrouped> caseOpsGroupeds = opsObj.getCaseOpsGroupeds();    // for not grouped Ops, it has no TCaseOpsGrouped object and its values.
            if (caseOpsGroupeds != null && caseOpsGroupeds.isEmpty() == false) {
                caseOpsGroupeds.forEach(new Consumer<TCaseOpsGrouped>() {
                    private String ze;

                    @Override
                    public void accept(TCaseOpsGrouped t) {
                        if (t.getCaseOps().getOpscCode() != null && (opsObj.getOpscCode() == null ? t.getCaseOps().getOpscCode() == null : opsObj.getOpscCode().equals(t.getCaseOps().getOpscCode()))) {
                            if (t.getCaseSupplFees() != null && Double.doubleToRawLongBits(t.getCaseSupplFees().getCsuplValue()) != Double.doubleToRawLongBits(0.0d)) {
                                String csuplfeeCode = t.getCaseSupplFees().getCsuplfeeCode();
                                String csUpl = createDecimalStringFromDoubleValue(t.getCaseSupplFees().getCsuplValue(), 2);
                                String currencySymbol = Lang.getCurrencySymbol();
                                ze = (csuplfeeCode + " (" + (csUpl) + " " + currencySymbol + ")");
                                ops.addContent(new Element("ze").addContent(formatAll(ze)));
                            } else {
                                ze = "(0,00" + Lang.getCurrencySymbol() + ")";
                                ops.addContent(new Element("ze").addContent(formatAll(ze)));
                            }
                        }
                    }
                });
            } else {    // set the default ze value
                String ze = "(0,00" + Lang.getCurrencySymbol() + ")";
                ops.addContent(new Element("ze").addContent(formatAll(ze)));
            }
        }
        return ops;
    }

    private Element generateFeeElement(Set<TCaseFee> feeList) {
        Element fees = new Element("fees");
        if (feeList != null) {
            for (TCaseFee fee_obj : feeList) {
                if (fee_obj != null) {
                    Element fee = new Element("fee");
                    fee.addContent(new Element("fee_key").addContent(fee_obj.getFeecFeekey()));
                    fee.addContent(new Element("value").addContent(formatAll(fee_obj.getFeecValue())));
                    fee.addContent(new Element("count").addContent(formatAll(fee_obj.getFeecCount())));
                    fee.addContent(new Element("from").addContent(formatAll(fee_obj.getFeecFrom())));
                    fee.addContent(new Element("to").addContent(formatAll(fee_obj.getFeecTo())));
                    fee.addContent(new Element("unbilled_days").addContent(formatAll(fee_obj.getFeecUnbilledDays())));
                    fee.addContent(new Element("is_bill").addContent(formatAll(fee_obj.getFeecIsBillFl())));
                    fees.addContent(fee);
                }
            }
        } else {
            LOG.log(Level.WARNING, "There is no Fee list or it could be null");
        }
        return fees;
    }

//    private Element generateGroupingResultElement(TGroupingResults group_result) {
//        Element grouping_result = new Element("group_result");
//        if (group_result != null) {
//            TCaseDrg drg = (TCaseDrg) group_result;
//            grouping_result.addContent(new Element("code").addContent(drg.getGrpresCode()));
//            grouping_result.addContent(new Element("cw_eff").addContent(formatAll(drg.getDrgcCwEffectiv())));
//            grouping_result.addContent(new Element("pccl").addContent(formatAll(drg.getGrpresPccl())));
//            grouping_result.addContent(new Element("cw_corr").addContent(formatAll(drg.getDrgcCwCorr())));
//            grouping_result.addContent(new Element("days_corr").addContent(formatAll(drg.getDrgcDaysCorr())));
//            grouping_result.addContent(new Element("type_of_corr").addContent(formatAll(drg.getDrgcTypeOfCorrEn())));
//
//        }
//        return grouping_result;
//    }
    
    private String createDecimalStringFromDoubleValue(double pValue, int pAfterComma){
        String ret;
        if (Double.doubleToRawLongBits(pValue) != Double.doubleToRawLongBits(0.0d) ) {
            ret = Lang.toDecimal(pValue, pAfterComma).replace(".", ",");
        } else {
            ret = "0,000";
        }
        return ret;
    }
    private Element generateGroupingResultElement(TCase caseObj, TCaseDetails caseDetails, TGroupingResults groupingResult) {
        Element groupingRes = new Element("grouping_result");
        if (groupingResult != null) {
            GDRGModel grouper = groupingResult.getModelIdEn();
            Set<TCaseOpsGrouped> caseOpsGroupeds = groupingResult.getCaseOpsGroupeds();
            Set<TCaseIcdGrouped> caseIcdGroupeds = groupingResult.getCaseIcdGroupeds();
            TCaseDrg caseDrg = groupingResult.getCaseDrg();
            TCasePepp casePepp = groupingResult.getCasePepp();
            int grpresPccl = groupingResult.getGrpresPccl();
            CaseTypeEn grpresType = groupingResult.getGrpresType();

            String grpresCode = groupingResult.getGrpresCode();
            groupingRes.addContent(new Element("code").addContent(grpresCode));
//            CpxDrgCatalog.instance().getByCode(drg.getGrpresCode(), "de", Lang.toYear(drg.getCaseDetails().getCsdAdmissionDate())).getDrgDescription();
            String drgDescription = drgCatalogDao.getByCode(groupingResult.getGrpresCode(), "de", Lang.toYear(groupingResult.getCaseDetails().getCsdAdmissionDate()));
            groupingRes.addContent(new Element("code_description").addContent(formatAll(drgDescription)));
            groupingRes.addContent(new Element("pccl").addContent(formatAll(groupingResult.getGrpresPccl())));
            if (caseDrg != null) {
                groupingRes.addContent(new Element("catalog_cw").addContent(createDecimalStringFromDoubleValue(caseDrg.getDrgcCwCatalog(), 3)));
                groupingRes.addContent(new Element("eff_cw").addContent(createDecimalStringFromDoubleValue(caseDrg.getDrgcCwEffectiv(), 3)));
                groupingRes.addContent(new Element("care_cw").addContent(createDecimalStringFromDoubleValue(caseDrg.getDrgcCareCw(), 4)));
            }

            if (caseDrg != null && groupingResult.getGrpresType().equals(CaseTypeEn.DRG)) {
                double cwCorr = caseDrg.getDrgcCwCorr();
                double cwCorrDay = caseDrg.getDrgcCwCorrDay();
                DrgCorrTypeEn cwEnum = caseDrg.getDrgcTypeOfCorrEn();
                String zuschlag;
                String zuschlag_tag;
                boolean isZuschlag = false;
                if ( cwEnum != null && cwEnum.equals(DrgCorrTypeEn.Surcharge)) {
                    zuschlag =createDecimalStringFromDoubleValue(cwCorr, 3);
                    zuschlag_tag = createDecimalStringFromDoubleValue(cwCorrDay, 3);
                    isZuschlag = true;
                } else {
                    zuschlag = "0,000";
                    zuschlag_tag = "0,000";
                }
                groupingRes.addContent(new Element("zuschlag").addContent(formatAll(zuschlag)));
                groupingRes.addContent(new Element("zuschlag_tag").addContent(formatAll(zuschlag_tag)));
                groupingRes.addContent(new Element("has_zuschlag").addContent(formatAll(isZuschlag)));
                String abschlag;
                String abschlag_tag;
                boolean isAbschlag = false;
                if (cwEnum != null && (cwEnum.equals(DrgCorrTypeEn.Deduction) || cwEnum.equals(DrgCorrTypeEn.DeductionTransfer))) {
                    abschlag = createDecimalStringFromDoubleValue(cwCorr, 3);
                    abschlag_tag = createDecimalStringFromDoubleValue(cwCorrDay, 3);
                    isAbschlag = true;
                } else {
                    abschlag = "0,000";
                    abschlag_tag = "0,000";
                }
                groupingRes.addContent(new Element("abschlag").addContent(formatAll(abschlag)));
                groupingRes.addContent(new Element("abschlag_tag").addContent(formatAll(abschlag_tag)));
                groupingRes.addContent(new Element("corr_tage").addContent(formatAll(caseDrg.getDrgcDaysCorr())));
                groupingRes.addContent(new Element("has_abschlag").addContent(formatAll(isAbschlag)));
                
                csdAdmissionDate = caseDetails.getCsdAdmissionDate();
                Double findDrgBaserateFeeValue = baseRateCatalogDao.findDrgBaserateFeeValue(caseObj.getCsHospitalIdent(), csdAdmissionDate /*, "de" */);
                Double findCareBaserateFeeValue = baseRateCatalogDao.findCareBaserateFeeValue(caseObj.getCsHospitalIdent(), csdAdmissionDate /*, "de" */);
                groupingRes.addContent(new Element("drg_baserate").addContent(Lang.toDecimal(findDrgBaserateFeeValue, 2) + " " + Lang.getCurrencySymbol()));
                groupingRes.addContent(new Element("pflege_baserate").addContent(Lang.toDecimal(findCareBaserateFeeValue, 2) + " " + Lang.getCurrencySymbol()));
                double revenue = caseDrg.getRevenue(findDrgBaserateFeeValue, findCareBaserateFeeValue);
                String erloesValue = Lang.toDecimal(revenue, 2);
                groupingRes.addContent(new Element("erlös").addContent(formatAll(erloesValue + Lang.getCurrencySymbol())));
                Double supplementaryFee = (Double) resultDao.getSupplementaryValueForIdCalculateOnDatabase(grouper, caseDetails.getId(), SupplFeeTypeEn.ZE);
                if (supplementaryFee != null) {
//                    Lang.toDecimal(supplementaryFee, 2) + " " + Lang.getCurrencySymbol();
                    groupingRes.addContent(new Element("ze_betrag").addContent(formatAll(Lang.toDecimal(supplementaryFee, 2) +  Lang.getCurrencySymbol())));
                } else {
                    groupingRes.addContent(new Element("ze_betrag").addContent(formatAll("0,00" + Lang.getCurrencySymbol())));
                }
                // revenue catalog
                groupingRes.addContent(new Element("catalog_betrag").addContent(Lang.toDecimal(findDrgBaserateFeeValue *caseDrg.getDrgcCwCatalog(), 2 ) + Lang.getCurrencySymbol()));
                //revenue correction
                if(isZuschlag){
                    groupingRes.addContent(new Element("zuschlag_tag_betrag").addContent(Lang.toDecimal(findDrgBaserateFeeValue *caseDrg.getDrgcCwCorrDay(), 2 ) + Lang.getCurrencySymbol()));
                    groupingRes.addContent(new Element("zuschlag_betrag").
                            addContent(Lang.toDecimal(Lang.round(findDrgBaserateFeeValue *caseDrg.getDrgcCwCorrDay(), 2) *caseDrg.getDrgcDaysCorr(), 2 ) + Lang.getCurrencySymbol()));
                    
                }else if(isAbschlag){
                    groupingRes.addContent(new Element("abschlag_tag_betrag").addContent("-" + Lang.toDecimal(findDrgBaserateFeeValue *caseDrg.getDrgcCwCorrDay(), 2 ) + Lang.getCurrencySymbol()));
                    groupingRes.addContent(new Element("abschlag_betrag").addContent("-" + Lang.toDecimal(Lang.round(findDrgBaserateFeeValue *caseDrg.getDrgcCwCorrDay(), 2) *caseDrg.getDrgcDaysCorr(), 2 ) + Lang.getCurrencySymbol()));
                }
                 groupingRes.addContent(new Element("has_korrektur").addContent(formatAll(isZuschlag || isAbschlag)));
                //revenue care
                 groupingRes.addContent(new Element("has_pflege").addContent(formatAll(caseDrg.getDrgcCareDays() > 0)));
                 groupingRes.addContent(new Element("pflege_cw_tag").addContent(Lang.toDecimal(caseDrg.getDrgcCareCwDay(), 4)));
                 groupingRes.addContent(new Element("pflege_tage").addContent(formatAll(caseDrg.getDrgcCareDays())));
                 groupingRes.addContent(new Element("pflege_tag_betrag").addContent(Lang.toDecimal(caseDrg.getDrgcCareCwDay() * findCareBaserateFeeValue, 2)
                    +  Lang.getCurrencySymbol()));
                groupingRes.addContent(new Element("pflege_betrag").addContent(Lang.toDecimal(caseDrg.getCareRevenue(findCareBaserateFeeValue) , 2)
                    +  Lang.getCurrencySymbol()));
            }
            if(casePepp!=null){
                Element pepp = new Element("pepp");
                pepp.addContent(new Element("code").addContent(formatAll(casePepp.getGrpresCode())));
                String peppDescription = peppCatalogDao.getByCode(groupingResult.getGrpresCode(), "de", Lang.toYear(groupingResult.getCaseDetails().getCsdAdmissionDate()));
                pepp.addContent(new Element("code_description").addContent(formatAll(peppDescription)));
                pepp.addContent(new Element("pccl").addContent(formatAll(casePepp.getGrpresPccl())));
                pepp.addContent(new Element("cw_eff").addContent(this.createDecimalStringFromDoubleValue(casePepp.getPeppcCwEffectiv(), 4)));
                pepp.addContent(new Element("days_intensiv").addContent(formatAll(casePepp.getPeppcDaysIntensiv())));
                pepp.addContent(new Element("pers_care_adults").addContent(formatAll(casePepp.getPeppcDaysPerscareAdult())));
                pepp.addContent(new Element("pers_care_inf").addContent(formatAll(casePepp.getPeppcDaysPerscareInf())));
                //double revenue = caseDrg.getRevenue(findDrgBaserateFeeValue, findCareBaserateFeeValue);
                //String erloesValue = Lang.toDecimal(revenue, 2);
                pepp.addContent(new Element("revenue").addContent(createDecimalStringFromDoubleValue(casePepp.getRevenue(), 2) + " " + Lang.getCurrencySymbol()));
                
                Double supplementaryFeePepp = (Double) resultDao.getSupplementaryValueForIdCalculateOnDatabase(grouper, caseDetails.getId(), SupplFeeTypeEn.ZP);
                if (supplementaryFeePepp != null) {
//                    Lang.toDecimal(supplementaryFee, 2) + " " + Lang.getCurrencySymbol();
                    pepp.addContent(new Element("zp_betrag").addContent(createDecimalStringFromDoubleValue(supplementaryFeePepp, 2) + " " + Lang.getCurrencySymbol()));
                } else {
                    pepp.addContent(new Element("zp_betrag").addContent(formatAll("0,00 " + Lang.getCurrencySymbol())));
                }
                
                Double supplementaryFeeET = (Double) resultDao.getSupplementaryValueForIdCalculateOnDatabase(grouper, caseDetails.getId(), SupplFeeTypeEn.ET);
                if (supplementaryFeeET != null) {
//                    Lang.toDecimal(supplementaryFee, 2) + " " + Lang.getCurrencySymbol();
                    pepp.addContent(new Element("et_betrag").addContent(formatAll(Lang.toDecimal(supplementaryFeeET, 2) + " " + Lang.getCurrencySymbol())));
                } else {
                    pepp.addContent(new Element("et_betrag").addContent(formatAll("0,00 " + Lang.getCurrencySymbol())));
                }
                
                groupingRes.addContent(pepp);

            }
            groupingRes.addContent(generateOpsWithZeElements(caseOpsGroupeds));
        } else {
            LOG.log(Level.WARNING, "There is no Grouping results or it could be null");
        }
        return groupingRes;
    }

    private Element generateOpsWithZeElements(Set<TCaseOpsGrouped> caseOpsGroupeds) {
        Element procedures = new Element("opses_with_ze");
        if (caseOpsGroupeds != null) {
            caseOpsGroupeds.forEach(new Consumer<TCaseOpsGrouped>() {
                @Override
                public void accept(TCaseOpsGrouped t) {
                    TCaseSupplFee caseSupplFees = t.getCaseSupplFees();
                    if (caseSupplFees != null) {
                        TCaseOps caseOps = t.getCaseOps();
                        procedures.addContent(generateSinlgeOpsWithZeElement(caseOps, caseSupplFees));
                    }
                }

            });
        }
        return procedures;
    }

    private Element generateSinlgeOpsWithZeElement(TCaseOps caseOps, TCaseSupplFee caseSupplFees) {
        Element ops = new Element("opsWithZe");
        if (caseOps != null) {
            ops.addContent(new Element("is_group").addContent(formatAll(caseOps.getOpsIsToGroupFl())));
            ops.addContent(new Element("ops_code").addContent(caseOps.getOpscCode()));
            String opscCode = caseOps.getOpscCode();
            int year = Lang.toYear(csdAdmissionDate);
            String opsText = opsCatalogDao.getOpsText(opscCode, "de", year);
            ops.addContent(new Element("ops_codeText").addContent(opsText));
            String ze = createDecimalStringFromDoubleValue(caseSupplFees.getCsuplValue(), 2);
            String currencySymbol = Lang.getCurrencySymbol();
            ops.addContent(new Element("ops_ze").addContent(caseSupplFees.getCsuplfeeCode() + " (" + (ze) + " " + currencySymbol + ")"));
            ops.addContent(new Element("ops_ze_one").addContent(caseSupplFees.getCsuplfeeCode() ));
            ops.addContent(new Element("ops_ze_one_betrag").addContent(ze + currencySymbol));
        }
        return ops;
    }

    private Element generateAddedIcdElements(Collection<TCaseIcd> addedIcdList) {
        Element addedIcds = new Element("added_icds");
        if (addedIcdList != null) {
            List<TCaseIcd> sortedAddedIcdList = addedIcdList.stream().sorted(Comparator.comparing(TCaseIcd::getIcdcCode)).collect(Collectors.toList());
            for (TCaseIcd icd : sortedAddedIcdList) {
//                if (icd != null && icd.getIcdIsToGroupFl()) {
                if (icd != null) {
                    addedIcds.addContent(generateAddedOrRemovedIcdElement(icd));
                }
            }
        } else {
            LOG.log(Level.INFO, "There are no added ICDs");
        }
        return addedIcds;
    }

    private Element generateRemovedIcdElements(Collection<TCaseIcd> removedIcdList) {
        Element removedIcds = new Element("removed_icds");
        if (removedIcdList != null) {
            List<TCaseIcd> sortedRemovedIcdList = removedIcdList.stream().sorted(Comparator.comparing(TCaseIcd::getIcdcCode)).collect(Collectors.toList());
            for (TCaseIcd icd : sortedRemovedIcdList) {
//                if (icd != null && icd.getIcdIsToGroupFl()) {
                if (icd != null) {
                    removedIcds.addContent(generateAddedOrRemovedIcdElement(icd));
                }
            }
        } else {
            LOG.log(Level.INFO, "There are no removed ICDs");
        }
        return removedIcds;
    }

    private Element generateAddedOrRemovedIcdElement(TCaseIcd icd) {
        Element addedOrRemovedIcd = new Element("icd");
        if (icd != null) {
            addedOrRemovedIcd.addContent(new Element("is_group").addContent(formatAll(icd.getIcdIsToGroupFl())));
            addedOrRemovedIcd.addContent(new Element("icd_code").addContent(icd.getIcdcCode()));
            String icdcCode = icd.getIcdcCode();
            int year = Lang.toYear(csdAdmissionDate);
            String icdText = icdCatalogDao.getIcdText(icdcCode, "de", year);
            addedOrRemovedIcd.addContent(new Element("icd_codeText").addContent(icdText));
//            addedIcd.addContent(new Element("icd_codeText").addContent(icd_desc));
            String secIcdCode = "";
            if (icd.getRefIcd() != null) {
                secIcdCode = icd.getRefIcd().getIcdcCode();
            }
            addedOrRemovedIcd.addContent(new Element("sec_icd_code").addContent(formatAll(secIcdCode)));
            addedOrRemovedIcd.addContent(new Element("is_hdb").addContent(formatAll(icd.getIcdcIsHdbFl())));
            addedOrRemovedIcd.addContent(new Element("is_hdx").addContent(formatAll(icd.getIcdcIsHdxFl())));
            addedOrRemovedIcd.addContent(new Element("loc").addContent(formatAll(icd.getIcdcLocEn() != LocalisationEn.E ? icd.getIcdcLocEn() : "")));
            addedOrRemovedIcd.addContent(new Element("ref_type").addContent(formatAll(icd.getIcdcReftypeEn())));
            addedOrRemovedIcd.addContent(new Element("icd_type").addContent(formatAll(icd.getIcdcTypeEn())));
        }
        return addedOrRemovedIcd;
    }

    private Element generateAddedOpsElements(Collection<TCaseOps> addedOpsList) {
        Element addedOpses = new Element("added_opses");
        if (addedOpsList != null) {
            List<TCaseOps> sortedAddedOpsList = addedOpsList.stream().sorted(Comparator.comparing(TCaseOps::getOpscCode)).collect(Collectors.toList());
            for (TCaseOps ops : sortedAddedOpsList) {
                if (ops != null) {
                    Set<TCaseOpsGrouped> caseOpsGroupeds = ops.getCaseOpsGroupeds();
                    if (caseOpsGroupeds != null && caseOpsGroupeds.isEmpty() == false) {
                        caseOpsGroupeds.forEach(new Consumer<TCaseOpsGrouped>() {
                            private String ze;

                            @Override
                            public void accept(TCaseOpsGrouped t) {
                                if (ops.getOpscCode() == null ? t.getCaseOps().getOpscCode() == null : ops.getOpscCode().equals(t.getCaseOps().getOpscCode())) {
                                    if (t.getCaseSupplFees() != null) {
                                        String csuplfeeCode = t.getCaseSupplFees().getCsuplfeeCode();
                                        String csUpl = createDecimalStringFromDoubleValue(t.getCaseSupplFees().getCsuplValue(), 2);
                                        String currencySymbol = Lang.getCurrencySymbol();
                                        ze = (csuplfeeCode + " (" + (csUpl) + " " + currencySymbol + ")");
                                        addedOpses.addContent(generateAddedOrRemovedOpsElement(ops, ze));
                                    } else {
                                        ze = "(0,00" + Lang.getCurrencySymbol() + ")";
                                        addedOpses.addContent(generateAddedOrRemovedOpsElement(ops, ze));
                                    }
                                }
                            }
                        });
                    } else {
                        String ze = "(0,00" + Lang.getCurrencySymbol() + ")";
                        addedOpses.addContent(generateAddedOrRemovedOpsElement(ops, ze));
                    }
//                    addedOpses.addContent(generateAddedOrRemovedOpsElement(ops));
                }
            }
        } else {
            LOG.log(Level.INFO, "There are no added OPSes");
        }
        return addedOpses;
    }

    private Element generateRemovedOpsElements(Collection<TCaseOps> removedOpsList) {
        Element removedOpses = new Element("removed_opses");
        if (removedOpsList != null) {
            List<TCaseOps> sortedRemovedOpsList = removedOpsList.stream().sorted(Comparator.comparing(TCaseOps::getOpscCode)).collect(Collectors.toList());
            for (TCaseOps ops : sortedRemovedOpsList) {
                if (ops != null) {
                    Set<TCaseOpsGrouped> caseOpsGroupeds = ops.getCaseOpsGroupeds();
                    caseOpsGroupeds.forEach(new Consumer<TCaseOpsGrouped>() {
                        private String ze;

                        @Override
                        public void accept(TCaseOpsGrouped t) {
                            if (ops.getOpscCode() == null ? t.getCaseOps().getOpscCode() == null : ops.getOpscCode().equals(t.getCaseOps().getOpscCode())) {
                                if (t.getCaseSupplFees() != null) {
                                    String csuplfeeCode = t.getCaseSupplFees().getCsuplfeeCode();
                                    String csUpl = createDecimalStringFromDoubleValue(t.getCaseSupplFees().getCsuplValue(), 2);
                                    String currencySymbol = Lang.getCurrencySymbol();
                                    ze = (csuplfeeCode + " (" + (csUpl) + " " + currencySymbol + ")");
                                    removedOpses.addContent(generateAddedOrRemovedOpsElement(ops, ze));
                                } else {
                                    ze = "(0,00" + Lang.getCurrencySymbol() + ")";
                                    removedOpses.addContent(generateAddedOrRemovedOpsElement(ops, ze));
                                }
                            }

                        }
                    });
//                    removedOpses.addContent(generateAddedOrRemovedOpsElement(ops));
                }
            }
        } else {
            LOG.log(Level.INFO, "There are no removed OPSes");
        }
        return removedOpses;
    }

    private Element generateAddedOrRemovedOpsElement(TCaseOps ops, String ze) {
        Element addedOrRemovedOps = new Element("ops");
        if (ops != null) {
            addedOrRemovedOps.addContent(new Element("is_group").addContent(formatAll(ops.getOpsIsToGroupFl())));
            addedOrRemovedOps.addContent(new Element("ops_code").addContent(ops.getOpscCode()));
            String opscCode = ops.getOpscCode();
            int year = Lang.toYear(csdAdmissionDate);
            String opsText = opsCatalogDao.getOpsText(opscCode, "de", year);
            addedOrRemovedOps.addContent(new Element("ops_codeText").addContent(opsText));
//            addedOrRemovedOps.addContent(new Element("ops_codeText").addContent(ops_desc));
            addedOrRemovedOps.addContent(new Element("ops_date").addContent(formatAll(ops.getOpscDatum())));
            addedOrRemovedOps.addContent(new Element("loc").addContent(formatAll(ops.getOpscLocEn() != LocalisationEn.E ? ops.getOpscLocEn() : "")));
            addedOrRemovedOps.addContent(new Element("ze").addContent(formatAll(ze)));
        }
        return addedOrRemovedOps;
    }

    /**
     * Konfiguration fuer den PDF Report -> erstmal statisch
     *
     * @return
     */
    private Element generateConfElement() {
        Element config = new Element("config");

        config.addContent(new Element("show_patient").addContent("1"));
        config.addContent(new Element("show_contact").addContent("1"));
        config.addContent(new Element("show_insurance").addContent("1"));
        config.addContent(new Element("show_hospital").addContent("1"));
        config.addContent(new Element("show_caseData").addContent("1"));
        config.addContent(new Element("show_case").addContent("1"));
        config.addContent(new Element("show_changes").addContent("1"));

        config.addContent(new Element("show_case_casecompletion").addContent("1"));
        config.addContent(new Element("show_case_main_diag").addContent("1"));
        config.addContent(new Element("show_case_aux_diag").addContent("1"));
        config.addContent(new Element("show_case_procs").addContent("1"));

        config.addContent(new Element("show_insurance").addContent("1"));

        return config;
    }

    /**
     * Wandelt die übergebenen Werte je nach Typ in einen String um (instanceof
     * Number, Date, Boolean, Enum)
     *
     * @param obj value (instanceof Number, Date, Boolean, Enum)
     * @return formatierter Wert als String
     */
    public String formatAll(Object obj) {
        String returnValue = "";

        if (obj != null) {
            if (obj instanceof Number) {
                returnValue = String.valueOf(obj);
            } else if (obj instanceof Date) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                returnValue = dateFormat.format((Date) obj);
            } else if (obj instanceof Boolean) {
                returnValue = Boolean.toString((Boolean) obj);
            } else if (obj instanceof Enum) {
                returnValue = ((Enum) obj).name();
            } else if (obj instanceof String) {
//                 return_value = ((String) obj);
                returnValue = String.valueOf(obj);
            } else if (obj instanceof URL) {
                returnValue = String.valueOf(obj);
            }
        }
        return returnValue;
    }

}
