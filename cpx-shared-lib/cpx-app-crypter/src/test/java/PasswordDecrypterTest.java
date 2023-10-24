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
import static de.lb.cpx.app.crypter.PasswordDecrypter.getInstance;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author niemeier
 */
public class PasswordDecrypterTest {

    public static final String DECODED_PW = "E2D5Vlub$!";
    public static final String ENCODED_PW = "neQmb7xnOzXbl8HwM+KVn0kGIQ8TkQsC";

    public PasswordDecrypterTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void encrypt() {
        assertEquals(getInstance().encrypt2(DECODED_PW), ENCODED_PW);
        assertEquals(getInstance().encrypt2(getInstance().decrypt2(DECODED_PW)), ENCODED_PW);
    }

    @Test
    public void decrypt() {
        assertEquals(getInstance().decrypt2(DECODED_PW), DECODED_PW);
        assertEquals(getInstance().decrypt2(DECODED_PW), DECODED_PW);
        assertEquals(getInstance().decrypt2(ENCODED_PW), DECODED_PW);
        assertEquals(getInstance().decrypt2(getInstance().encrypt2(DECODED_PW)), DECODED_PW);
    }

    @Test
    public void isEncrypted() {
        assertEquals(getInstance().isEncrypted(DECODED_PW), false);
        assertEquals(getInstance().isEncrypted(ENCODED_PW), true);
    }

    @Test
    public void isDecrypted() {
        assertEquals(getInstance().isDecrypted(DECODED_PW), true);
        assertEquals(getInstance().isDecrypted(ENCODED_PW), false);
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
