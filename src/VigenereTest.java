import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * CEN 4078 Programming Exercise 1
 * File Name: null.java
 * {DESCRIPTION}
 *
 * @author Caleb Metz
 * @version 1.0
 */
public class VigenereTest {

    Vigenere vigenere = new Vigenere();

    String alphaKey = "ARGOSROCK";

    @Test
    void testBasicAlphaEncrypt() {
        String basicAlpha = "ILikeCheesecake";
        String basicAlphaResult = "ICoywTvgosviocv";

        StringBuilder dummyStringBuilder = new StringBuilder();

        for (int i = 0; i < alphaKey.length(); i++) {
            dummyStringBuilder.append("A");
        }

        String dummyString = dummyStringBuilder.toString();

        assertEquals(alphaKey, vigenere.encrypt(alphaKey, dummyString), "String of 'A's should return the key");
        assertEquals(basicAlphaResult,vigenere.encrypt(alphaKey, basicAlpha), "Encrypt basic string");


    }

    @Test
    void testFullAlphaEncrypt() {
        String fullPassword = "Password12345";
        String fullPasswordResult = "Prygofff23570";

        assertEquals(fullPasswordResult,vigenere.encrypt(alphaKey, fullPassword), "Encrypt basic string");
    }

    @Test
    void testAlphaDecrypt() {

    }
    @Test
    void testNumberEncrypt() {

    }
    @Test
    void testNumberDecrypt() {

    }
}
