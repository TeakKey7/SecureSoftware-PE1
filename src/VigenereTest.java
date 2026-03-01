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
    int numberKey = 1963;

    String basicAlpha = "ILikeCheesecake";
    String basicAlphaResult = "ICoywTvgosviocv";

    String fullPassword = "Password12345";
    String fullPasswordResult = "Prygofff12681";


    @Test
    void testBasicAlphaEncrypt() {

        StringBuilder dummyStringBuilder = new StringBuilder();

        for (int i = 0; i < alphaKey.length(); i++) {
            dummyStringBuilder.append("A");
        }
        String dummyString1 = dummyStringBuilder.toString();

        dummyStringBuilder = new StringBuilder();
        for (int i = 0; i < alphaKey.length(); i++) {
            dummyStringBuilder.append("a");
        }
        String dummyString2 = dummyStringBuilder.toString();

        dummyStringBuilder = new StringBuilder();
        for (int i = 0; i < alphaKey.length(); i++) {
            dummyStringBuilder.append("0");
        }
        String dummyString3 = dummyStringBuilder.toString();

        assertEquals(alphaKey, vigenere.encrypt(alphaKey, dummyString1), "String of 'A's should return the key");
        assertEquals(alphaKey, vigenere.encrypt(alphaKey, dummyString1), "String of 'a's should return the key");
        assertEquals(alphaKey, vigenere.encrypt(alphaKey, dummyString1), "String of '0's should return the key");
        assertEquals(basicAlphaResult,vigenere.encrypt(alphaKey, basicAlpha), "Encrypt basic string");


    }

    @Test
    void testFullAlphaEncrypt() {

        assertEquals(fullPasswordResult,vigenere.encrypt(alphaKey, fullPassword), "Encrypt full string");
    }

    @Test
    void testAlphaDecrypt() {

        StringBuilder dummyStringBuilder = new StringBuilder();

        for (int i = 0; i < alphaKey.length(); i++) {
            dummyStringBuilder.append("A");
        }
        String dummyString1 = dummyStringBuilder.toString();

        assertEquals(dummyString1, vigenere.decrypt(alphaKey, alphaKey), "Decrypting the key should return 'A's");
        assertEquals(basicAlpha,vigenere.encrypt(alphaKey, basicAlphaResult), "Decrypt basic string");

    }
    @Test
    void testNumberEncrypt() {

    }
    @Test
    void testNumberDecrypt() {
    }
}
