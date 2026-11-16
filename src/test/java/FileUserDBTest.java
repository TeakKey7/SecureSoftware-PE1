/**
 * CEN 4078 Programming Exercise 1
 * File Name: FileUserDBTest.java
 * JUnit tests for the FileUserDB class
 * @author Caleb Metz
 * @version 1.0
 */
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
//Gemini created tests
class FileUserDBTest {

    private static final String TEST_FILE = "test_users_junk.txt";
    private FileUserDB db;

    @BeforeEach
    void setUp() {
        db = new FileUserDB(TEST_FILE);
    }

    @AfterEach
    void tearDown() {
        // Delete the junk file so the next test starts clean
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testFindByUsername_Success() {
        db.save(new User("alice", "Password123", 2000000000));

        Optional<User> result = db.findByUsername("alice");

        assertTrue(result.isPresent(), "User should be found");
        assertEquals("alice", result.get().getUserName());
        assertEquals("Password123", result.get().getPassword());
    }

    @Test
    void testFindByUsername_NotFound() {
        // Execute on empty DB
        Optional<User> result = db.findByUsername("ghost");

        // Assert
        assertFalse(result.isPresent(), "Should return empty for missing user");
    }

    @Test
    void testSave_CreatesNewUser() {
        User newUser = new User("bob", "Password123", 2000000000);
        db.save(newUser);

        assertTrue(db.findByUsername("bob").isPresent());

        FileUserDB diskCheck = new FileUserDB(TEST_FILE);
        Optional<User> diskUser = diskCheck.findByUsername("bob");

        assertTrue(diskUser.isPresent(), "User must exist on hard drive");
        assertEquals(2000000000, diskUser.get().getMfaCode());
    }

    @Test
    void testSave_UpdatesExistingUser() {
        // 1. Create original user
        db.save(new User("charlie", "oldPass", 2000000000));

        // 2. UPDATE charlie (Change pass and MFA)
        db.save(new User("charlie", "newPass", 2000000000));

        // 3. Verify Memory Update
        User memoryUser = db.findByUsername("charlie").get();
        assertEquals("newPass", memoryUser.getPassword());

        // 4. Verify Disk Update (Did rewriteFile work?)
        FileUserDB diskCheck = new FileUserDB(TEST_FILE);
        User diskUser = diskCheck.findByUsername("charlie").get();

        assertEquals("newPass", diskUser.getPassword(), "Disk should have new password");
        assertEquals(2000000000, diskUser.getMfaCode(), "Disk should have new MFA");
    }

    @Test
    void test_DeleteExistingUser() {

        User charlie = new User("charlie", "oldPass", 2000000000);

        db.save(charlie);

        assertEquals(Optional.of(charlie), db.findByUsername("charlie"), "Confirm user loaded");

        db.delete("charlie");

        assertEquals(Optional.empty(), db.findByUsername("charlie"), "Successfully deletes a user from DB");

    }

    @Test
    void test_DeleteNonExistentUser() {
        FileUserDB diskCheck = new FileUserDB(TEST_FILE);
        assertDoesNotThrow(() -> diskCheck.delete("charlie"),
                "Non existent user should not throw an exception");
        diskCheck.delete("charlie");
    }

    // --- TEST 3: Handling Missing Files ---
    @Test
    void testLoadNonExistentFile() {
        // Ensure we don't crash if the file is missing
        FileUserDB emptyDb = new FileUserDB("does_not_exist.txt");

        Optional<User> result = emptyDb.findByUsername("ghost");
        assertFalse(result.isPresent(), "Should return empty optional, not crash");
    }
}
