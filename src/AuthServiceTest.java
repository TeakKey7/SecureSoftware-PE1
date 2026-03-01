import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
    public static final String encryptedPassword = "Secure123";
    public static final String decryptedPassword = "Unlocked123";
    public static final int encryptedCode = 9999;
    public static final int decryptedCode = 0000;
    public static String badInput = "Bad";
    User alice = new User("alice", encryptedPassword, 2000000000);
    //Mock written by Gemini
    class MockDB implements UserDataStore {
        public boolean dbWasQueried = false;

        private ArrayList<User> users = new ArrayList<User>();

        public MockDB() {
            users.add(alice);
        }

        @Override
        public Optional<User> findByUsername(String username) {
            dbWasQueried = true;
            for (User user : users) {
                if (username.equals(user.getUserName())) {
                    return Optional.of(user);
                }
            }
            return Optional.empty();
        }

        @Override
        public void save(User user) {
            users.add(user);
        }

        @Override
        public void delete(String userName) {
            if (findByUsername(userName).isPresent()) {
                users.remove(findByUsername(userName).get());
            }
        }
    }
    static class MockMFA implements MFAProvider {
        public boolean verifyWasCalled = false;
        public boolean validateWasCalled = false;
        @Override
        public boolean enroll(User user, String input) {
            return true; //FIXME
        }
        @Override
        public boolean verify(User user, String input) {
            verifyWasCalled = true;
            return true;
        }
        public boolean validate(String input) {
            validateWasCalled = true;
            return true;
        }
    }
    static class MockValidation extends Validation {
        public boolean verifySQLInjectionWasCalled = false;
        @Override
        public boolean noSQLInjection(String input) {
            verifySQLInjectionWasCalled = true;
            return (!input.equals(badInput));
        }

    }
    static class MockCryptographer implements Cryptographer {
        @Override
        public String encrypt(String alphaKey, String input) {
            return encryptedPassword;
        }

        @Override
        public int encrypt(int numberKey, int input) {
            return encryptedCode;
        }

        @Override
        public String decrypt(String alphaKey, String input) {
            return decryptedPassword;
        }

        @Override
        public int decrypt(int numberKey, int input) {
            return decryptedCode;
        }
    }
    @Test
    void testLogin() {
        MockDB fakeDb = new MockDB();
        MockMFA mockMFA = new MockMFA();
        MockValidation mockValidation = new MockValidation();
        MockCryptographer mockCryptographer = new MockCryptographer();
        AuthService service = new AuthService(fakeDb, mockValidation, mockMFA, mockCryptographer);

        assertEquals(alice, service.authenticate("alice", encryptedPassword, "2000000000"), "True on correct login");
        assertNull(service.authenticate("alice", "Password", "2000000000"), "Null on bad password");
        assertNull(service.authenticate("bob", encryptedPassword, "2000000000"), "Null on bad user");
    }

    @Test
    void testSave() {
        MockDB fakeDb = new MockDB();
        MockMFA mockMFA = new MockMFA();
        MockValidation mockValidation = new MockValidation();
        MockCryptographer mockCryptographer = new MockCryptographer();
        AuthService service = new AuthService(fakeDb, mockValidation, mockMFA, mockCryptographer);

        assertTrue(service.saveUser("alice", encryptedPassword, "2000000000"), "True on successful save");
        assertFalse(service.saveUser("alice", badInput, "2000000000"), "Null on bad password");
    }

    @Test
    void testSaveAndLogin() {
        MockDB fakeDb = new MockDB();
        MockMFA mockMFA = new MockMFA();
        MockValidation mockValidation = new MockValidation();
        MockCryptographer mockCryptographer = new MockCryptographer();
        AuthService service = new AuthService(fakeDb, mockValidation, mockMFA, mockCryptographer);

        assertNull(service.authenticate("bob", encryptedPassword, "2000000000"), "Confirm test user does not exist before init");

        User bob = new User("bob", encryptedPassword, 2000000000);
        assertTrue(service.saveUser(bob), "Successfully creates the user");

        assertEquals(bob, service.authenticate("bob", encryptedPassword, "2000000000"), "True on correct login");
    }

    @Test
    void testSQLInjectionCheckCalledOnAuth() {
        MockDB fakeDb = new MockDB();
        MockMFA mockMFA = new MockMFA();
        MockValidation mockValidation = new MockValidation();
        MockCryptographer mockCryptographer = new MockCryptographer();
        AuthService service = new AuthService(fakeDb, mockValidation, mockMFA, mockCryptographer);

        service.authenticate("alice", badInput, "2000000000");
        boolean result = fakeDb.dbWasQueried;
        assertFalse(result, "Ensure bad query was not passed on");
        assertTrue(mockValidation.verifySQLInjectionWasCalled, "SQL injection check was called");
    }

}