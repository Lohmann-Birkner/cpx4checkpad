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
 *    2019  yezhov - initial API and implementation and/or initial documentation
 */
import de.lb.cpx.reader.DocumentReader;
import de.lb.cpx.reader.util.Document;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author niemeier
 */
@Ignore
public class InformationExtractionTest {

    private static final Logger LOG = Logger.getLogger(InformationExtractionTest.class.getName());
    public static final String PATH = "C:\\TEMP\\Texte2\\extrahiert\\sub1\\";

    public InformationExtractionTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testDateiTexte1Fall1() {
        String path = "/txt/Anlage19_Klageunterlagen_RA.txt";
        try ( InputStream is = InformationExtractionTest.class.getClass().getResourceAsStream(path)) {
            String result = IOUtils.toString(is, StandardCharsets.UTF_8);
            final String caseNumber = DocumentReader.getCaseNumber(result);
            Assert.assertEquals("case number is unequal in!", "12345", caseNumber);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "couldn't test casenumber", ex);
        }
    }

    @Test
    public void testDateiTexte1Fall1Tabelle1() {
        String path = "/txt/Anlage21_Widerspruch_KH.txt";
        try ( InputStream is = InformationExtractionTest.class.getClass().getResourceAsStream(path)) {
            String result = IOUtils.toString(is, StandardCharsets.UTF_8);
            final String caseNumber = DocumentReader.getCaseNumber(result);
            Assert.assertEquals("case number is unequal in!", "12345xxxxxx", caseNumber);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "couldn't test casenumber", ex);
        }
    }

    @Test
    public void testDateiTexte2Fall1() {
        String path = "/txt/Anlage6_KTR_Anfrage_HanseMerkur.txt";
        try ( InputStream is = InformationExtractionTest.class.getClass().getResourceAsStream(path)) {
            String result = IOUtils.toString(is, StandardCharsets.UTF_8);
            final String caseNumber = DocumentReader.getCaseNumber(result);
            Assert.assertEquals("case number is unequal in!", "123456789", caseNumber);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "couldn't test casenumber", ex);
        }
    }

    @Test
    public void testDateiTexte2FallTabelle1() {
        String path = "/txt/Anlage14_Anschreiben_Fachabteilung_ambPotential.txt";
        try ( InputStream is = InformationExtractionTest.class.getClass().getResourceAsStream(path)) {
            String result = IOUtils.toString(is, StandardCharsets.UTF_8);
            final String caseNumber = DocumentReader.getCaseNumber(result);
            Assert.assertEquals("case number is unequal in!", "12345", caseNumber);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "couldn't test casenumber", ex);
        }
    }

//    @Test
//    public void testDateiTexte2Kv2Kind() {
//        final File file = new File("C:\\TEMP\\Dokumente\\Texte2\\Anlage21_Widerspruch_KH.txt");
//        final Document doc = readDocument(file);
//        final String patientNumber = doc.getPatientNumber();
//        Assert.assertEquals("patient number is unequal in " + file.getName() + "!", "C816641884_K", patientNumber);
//    }
    @Test
    public void testDateiTexte2Kv1() {
        String path = "/txt/Anlage15_Entscheidung_Fachabteilung_PKMS_KonsensGA.txt";
        try ( InputStream is = InformationExtractionTest.class.getClass().getResourceAsStream(path)) {
            String result = IOUtils.toString(is, StandardCharsets.UTF_8);
            final String patientNumber = DocumentReader.getPatientNumber(result);
            Assert.assertEquals("patient number is unequal!", "P123456789", patientNumber);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "couldn't test patientnumber", ex);
        }
    }

    @Test
    public void testDateiTexte2Kv2() {
        String path = "/txt/Anlage2_Kurzbrief_Antwort_DAK.txt";
        try ( InputStream is = InformationExtractionTest.class.getClass().getResourceAsStream(path)) {
            String result = IOUtils.toString(is, StandardCharsets.UTF_8);
            final String patientNumber = DocumentReader.getPatientNumber(result);
            Assert.assertEquals("patient number is unequal!", "C816774198", patientNumber);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "couldn't test patientnumber", ex);
        }
    }

    @Test
    public void testDateiTexte1Kv1() {
        String path = "/txt/Anlage6_KTR_Anfrage_Barmenia.txt";
        try ( InputStream is = InformationExtractionTest.class.getClass().getResourceAsStream(path)) {
            String result = IOUtils.toString(is, StandardCharsets.UTF_8);
            final String patientNumber = DocumentReader.getPatientNumber(result);
            Assert.assertEquals("patient number is unequal!", "A001137046", patientNumber);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "couldn't test patientnumber", ex);
        }
    }

    @Test
    public void testDateiTexte1Kv2() {
        String path = "/txt/Anlage13_Ergebnis_MCo_Beurteilungsbogen.txt";
        try ( InputStream is = InformationExtractionTest.class.getClass().getResourceAsStream(path)) {
            String result = IOUtils.toString(is, StandardCharsets.UTF_8);
            final String patientNumber = DocumentReader.getPatientNumber(result);
            Assert.assertEquals("patient number is unequal!", "Z999790992", patientNumber);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "couldn't test patientnumber", ex);
        }
    }

    @Test
    public void testDateiTexte1MdkAdress1() {
        String path = "/txt/Anlage10_MDK-Folgegutachten.txt";
        try ( InputStream is = InformationExtractionTest.class.getClass().getResourceAsStream(path)) {
            String result = IOUtils.toString(is, StandardCharsets.UTF_8);
            final String mdkAddress = DocumentReader.getMdkAddress(result);
            Assert.assertEquals("mdk address is unequal!", "MDK Berlin-Brandenburg e. V., Lise-Meitner-Str. 1, 10589 Berlin", mdkAddress);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "couldn't test mdkaddress", ex);
        }

    }

    @Test
    public void testDateiTexte2MdkAdress1() {
        String path = "/txt/Anlage4_Prüfanzeige_MDK.txt";
        try ( InputStream is = InformationExtractionTest.class.getClass().getResourceAsStream(path)) {
            String result = IOUtils.toString(is, StandardCharsets.UTF_8);
            final String mdkAddress = DocumentReader.getMdkAddress(result);
            Assert.assertEquals("mdk address is unequal!", "MDK Berlin-Brandenburg e. V., Lise-Meitner-Str. 1, 10589 Berlin", mdkAddress);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "couldn't test mdkaddress", ex);
        }

    }
//    @Test
//    public void testDateiTexte2FallTabelle2() {
//        final File file = new File("C:\\TEMP\\Dokumente\\Texte1\\Anlage8_Benachrichtigung_über_Begehung_BilateralesFallgespräch gedreht.txt");
//        final Document doc = readDocument(file);
//        final String caseNumber = doc.getCaseNumber();
//        Assert.assertEquals("case number is unequal in " + file.getName() + "!", "12345", caseNumber);
//    }

    private Document readDocument(final File pFile) {
        String text = readFile(pFile);
        return new Document(pFile, text);
    }

    private String readFile(final String pFileName) {
        if (pFileName == null || pFileName.trim().isEmpty()) {
            throw new IllegalArgumentException("file name is null or empty!");
        }
        final File file = new File(pFileName);
        return readFile(file);
    }

    private String readFile(final File pFile) {
        if (pFile == null) {
            throw new IllegalArgumentException("file is null!");
        }
        if (!pFile.exists() || !pFile.isFile()) {
            throw new IllegalArgumentException("file does not exist or path is a directory: " + pFile.getAbsolutePath());
        }
        if (!pFile.exists() || !pFile.isFile()) {
            throw new IllegalArgumentException("file does not exist or path is a directory: " + pFile.getAbsolutePath());
        }
        if (!pFile.canRead()) {
            throw new IllegalArgumentException("no permission to read file: " + pFile.getAbsolutePath());
        }
        try ( BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(pFile), "UTF8"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String text = sb.toString().trim();
            if (text.isEmpty()) {
                LOG.log(Level.WARNING, "File is empty: " + pFile.getAbsolutePath());
            } else {
                LOG.log(Level.INFO, text.length() + " characters found in file: " + pFile.getAbsolutePath());
            }
            return DocumentReader.getCorrectedText(text);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new IllegalStateException("Was not able to read file: " + pFile.getAbsolutePath(), ex);
        }
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
