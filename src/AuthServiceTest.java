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
        public boolean saveWasCalled = false;

        @Override
        public Optional<User> findByUsername(String username) {
            if (username.equals("alice")) {
                return Optional.of(alice);
            }
            return Optional.empty();
        }

        @Override
        public void save(User user) {
            this.saveWasCalled = true;
        }

        @Override
        public void delete(String userName) {
        }
    }
    class MockMFA implements MFAProvider {
        public boolean verifyWasCalled = false;
        @Override
        public void enroll(User user, String input) {

        }
        @Override
        public boolean verify(User user, String input) {
            verifyWasCalled = true;
            return true;
        }
    }

    @Test
    void testLogin() {
        MockDB fakeDb = new MockDB();
        MockMFA mockMFA = new MockMFA();
        AuthService service = new AuthService(fakeDb, new Validation(), mockMFA);

        assertTrue(service.authenticate("alice", "Password123"), "True on correct login");
        assertFalse(service.authenticate("alice", "Password"), "False on bad password");
        assertFalse(service.authenticate("bob", "Password123"), "False on bad user");
    }

    @Test
    void testIfMFAUsed() {
        MockDB fakeDb = new MockDB();
        MockMFA mockMFA = new MockMFA();
        AuthService service = new AuthService(fakeDb, new Validation(), mockMFA);

        service.authenticate("alice", "Password123");

        assertTrue(mockMFA.verifyWasCalled, "True if authentication requires MFA");
    }
}