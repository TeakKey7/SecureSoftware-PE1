import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CEN 4078 Programming Exercise 1
 * File Name: AuthServiceTest.java
 * Contains JUnit Tests for AuthService class
 * @author Caleb Metz
 * @version 1.0
 */
class AuthServiceTest {
    User alice = new User("alice", "Password123", 2000000000);
    //Mock written by Gemini
    class MockDB implements UserDataStore {
        public boolean dbWasQueried = false;
        @Override
        public Optional<User> findByUsername(String username) {
            dbWasQueried = true;
            if (username.equals("alice")) {
                return Optional.of(alice);
            }
            return Optional.empty();
        }

        @Override
        public void save(User user) {
        }

        @Override
        public void delete(String userName) {
        }
    }
    static class MockMFA implements MFAProvider {
        public boolean verifyWasCalled = false;
        public boolean validateWasCalled = false;
        @Override
        public void enroll(User user, String input) {

        }
        @Override
        public boolean verify(User user, String input) {
            verifyWasCalled = true;
            return true;
        }
        @Override
        public boolean validate(String input) {
            validateWasCalled = true;
            return true;
        }
    }
    static class MockValidation extends Validation {
        public boolean verifySQLInjectionWasCalled = false;
        @Override
        public boolean noSQLInjection(String input) {
            return (!input.equals("Bad"));
        }

    }

    @Test
    void testLogin() {
        MockDB fakeDb = new MockDB();
        MockMFA mockMFA = new MockMFA();
        MockValidation mockValidation = new MockValidation();
        AuthService service = new AuthService(fakeDb, mockValidation, mockMFA);

        assertTrue(service.authenticate("alice", "Password123", "2000000000"), "True on correct login");
        assertFalse(service.authenticate("alice", "Password", "2000000000"), "False on bad password");
        assertFalse(service.authenticate("bob", "Password123", "2000000000"), "False on bad user");
    }


    @Test
    void testSQLInjectionCheckCalledOnAuth() {
        MockDB fakeDb = new MockDB();
        MockMFA mockMFA = new MockMFA();
        MockValidation mockValidation = new MockValidation();
        AuthService service = new AuthService(fakeDb, mockValidation, mockMFA);

        String badInput = "Bad";

        service.authenticate("alice", badInput, "2000000000");
        boolean result = fakeDb.dbWasQueried;
        assertFalse(result, "Ensure bad query was not passed on");

    }
}