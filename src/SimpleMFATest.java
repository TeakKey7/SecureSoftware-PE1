import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CEN 4078 Programming Exercise 1
 * File Name: SimpleMFATest.java
 * Contains JUnit Tests for SimpleMFA class
 * @author Caleb Metz
 * @version 1.0
 */
class SimpleMFATest {
    static final String badInput = "9999999999999";
    //Mock written by Gemini
    static class MockValidation extends Validation {
        public boolean verifyIntOverflowWasCalled = false;
        @Override
        public boolean noIntOverflow(String input) {
            return (!input.equals(badInput));
        }
    }

    @Test
    void testEnroll() {
        int mfaCode = 1000000000;
        User alice = new User("Alice", "Password123", mfaCode);
        MockValidation mockValidation = new MockValidation();
        SimpleMFA simpleMFA = new SimpleMFA(mockValidation);

        simpleMFA.enroll(alice, "2000000000");
        assertEquals(2000000000, alice.getMfaCode(), "Successfully set MFA code to enroll a user");
        alice.setMfaCode(0);
        assertFalse(simpleMFA.enroll(alice, badInput), "False on bad MFAInput");
        assertEquals(0, alice.getMfaCode(), "Enroll does not change MFA code when invalid");
    }

    @Test
    void testVerify() {
        int mfaCode = 1000000000;
        int weakCode = 999;
        User alice = new User("Alice", "Password123", mfaCode);
        MockValidation mockValidation = new MockValidation();
        SimpleMFA simpleMFA = new SimpleMFA(mockValidation);

        assertTrue(simpleMFA.verify(alice, String.valueOf(mfaCode)), "Successfully verifies code");
        assertFalse(simpleMFA.verify(alice, String.valueOf(mfaCode + 1)), "Successfully denies code");
        User badUser = new User("Alice", "Password123", weakCode);
        assertFalse(simpleMFA.verify(alice, String.valueOf(weakCode)), "Will not verify weak code");
    }

    @Test
    void testValidate() {

    }
}