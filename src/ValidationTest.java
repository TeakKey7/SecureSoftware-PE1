import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CEN 4078 Programming Exercise 1
 * File Name: ValidationTest.java
 * Ensure the validation class passes all requirements
 * @author Caleb Metz
 * @version 1.0
 */

class ValidationTest {
    char[] sensitiveChars = new char[]{'/', '-', ';', '"'};
    int minLength = 8;
    int maxLength = 12;
    Validation val = new Validation(sensitiveChars, minLength, maxLength);

    @Test
    void noSQLInjection_safe() {
        assertTrue(val.noSQLInjection("hello"), "Safe string should return true");
    }
    @Test
    void noSQLInjection_badChars() {
        for (char character : sensitiveChars) {
            String testString = "username";
            testString += character;
            assertFalse(val.noSQLInjection(testString), "Bad character should return false");
        }
    }

    @Test
    void isValidPassword_badLength() {
        assertFalse(val.isValidPassword("Pass123"), "Short password should return false");
        assertFalse(val.isValidPassword("Password99999"), "Long password should return false");
    }

    @Test
    void isValidPassword_goodLength() {
        assertTrue(val.isValidPassword("Password123"), "Correct password length should return true");
    }

    @Test
    void isValidPassword_badUpper() {
        assertFalse(val.isValidPassword("password123"), "Lowercase password should return false");
    }
    @Test
    void isValidPassword_goodUpper() {
        assertTrue(val.isValidPassword("Password123"), "Good password should return true");
    }

    @Test
    void isValidPassword_badLower() {
        assertFalse(val.isValidPassword("PASSWORD123"), "Lowercase password should return false");
    }
    @Test
    void isValidPassword_goodLower() {
        assertTrue(val.isValidPassword("Password123"), "Good password should return true");
    }

    @Test
    void isValidPassword_goodNumeric() {
        assertTrue(val.isValidPassword("Password123"), "Numeric password should return true");
    }

    @Test
    void isValidPassword_badNumeric() {
        assertFalse(val.isValidPassword("Password"), "Non numeric password should return false");
    }

    @Test
    void noIntOverflow_badTop() {
        assertFalse(val.noIntOverflow("2147483648"), "Out of bounds number should return false");
    }
    @Test
    void noIntOverflow_goodTop() {
        assertTrue(val.noIntOverflow("2147483647"), "In bounds number should return true");
    }
    @Test
    void noIntOverflow_badBottom() {
        assertFalse(val.noIntOverflow("-2147483649"), "Out of bounds number should return false");
    }
    @Test
    void noIntOverflow_goodBottom() {
        assertTrue(val.noIntOverflow("-2147483647"), "In bounds number should return true");
    }
    @Test
    void canChangeLength() {
        Validation val2 = new Validation(sensitiveChars, 15, 20);
        String newShort = "Password12345";
        String newGood = "LongPassword12345";
        String newLong = "ThisIsAVeryVeryVeryLongPassword12345";
        assertTrue(val2.isValidPassword(newGood), "Between 15 and 20 passes");
        assertFalse(val2.isValidPassword(newShort), "Below 15 now fails");
        assertFalse(val2.isValidPassword(newLong), "Above 20 still fails");
    }
}