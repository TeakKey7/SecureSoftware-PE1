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
        assertThrows(Validation.InvalidPasswordException.class, () ->
                        val.isValidPassword("Pass123"),
                "Short password should throw InvalidPasswordException");

        assertThrows(Validation.InvalidPasswordException.class, () ->
                        val.isValidPassword("Password99999"),
                "Long password should throw InvalidPasswordException");
    }

    @Test
    void isValidPassword_goodPassword() {
        assertDoesNotThrow(() -> val.isValidPassword("Password123"),
                "Correct password length should not throw an exception");
    }

    @Test
    void isValidPassword_badUpper() {
        assertThrows(Validation.InvalidPasswordException.class, () ->
                        val.isValidPassword("password123"),
                "Lowercase password should throw exception for missing Uppercase");
    }

    @Test
    void isValidPassword_badLower() {
        assertThrows(Validation.InvalidPasswordException.class, () ->
                        val.isValidPassword("PASSWORD123"),
                "Uppercase password should throw exception for missing Lowercase");
    }

    @Test
    void isValidPassword_badNumeric() {
        assertThrows(Validation.InvalidPasswordException.class, () ->
                        val.isValidPassword("Password"),
                "Non-numeric password should throw exception for missing Digit");
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
        assertDoesNotThrow(() -> val2.isValidPassword(newGood),
                "Between 15 and 20 passes");
        assertThrows(Validation.InvalidPasswordException.class, () -> val2.isValidPassword(newShort),
                "Below 15 now fails");
        assertThrows(Validation.InvalidPasswordException.class, () -> val2.isValidPassword(newLong),
                "Above 20 still fails");
    }
}