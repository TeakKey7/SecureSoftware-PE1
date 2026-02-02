import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static java.nio.file.Files.*;

/**
 * CEN 4078 Programming Exercise 1
 * File Name: null.java
 * {DESCRIPTION}
 *
 * @author Caleb Metz
 * @version 1.0
 */
public class FileUserDB implements UserDataStore{
    private ArrayList<User> users;
    private int currentSize;
    private String fileName;

    private int findIndex(String userName) {
        return -1;
    }

    private void saveToFile() {

    }

    private void loadFromFile() {
        Validation val = new Validation();
        //Started with Gemini snippet
        List<User> userList = new ArrayList<>();
        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println("File " + fileName + " not found.");
        }

        //Read the file
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                if (parts.length == 3) {
                    String username = parts[0];
                    String password = parts[1];
                    String mfaCodeStr = parts[2];

                    try {
                        long mfaCode = Long.parseLong(mfaCodeStr);
                        if (val.noIntOverflow())
                    } catch (syst) {
                        System.out.println("Detected invalid MFA code.");
                    }

                    userList.add(new User(username, password, mfaCode));
                }
            }
            System.out.println("✅ Loaded " + userList.size() + " users.");

        } catch (FileNotFoundException e) {
            System.out.println("❌ Error reading file: " + e.getMessage());
        }

        return userList;
    }

    public FileUserDB() {
        users = new ArrayList<User>();
        currentSize = 0;
        fileName = "users.txt";
    }

    @Override
    public Optional<User> findByUsername(String userName) {
        return Optional.empty();
    }

    @Override
    public void save(User user) {

    }

    @Override
    public void delete(String userName) {

    }
}
