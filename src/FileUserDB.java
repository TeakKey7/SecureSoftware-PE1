import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static java.nio.file.Files.*;

/**
 * CEN 4078 Programming Exercise 1
 * File Name: FileUserDB.java
 * A file storage database for the users array
 * @author Caleb Metz
 * @version 1.1
 */
public class FileUserDB implements UserDataStore{
    private ArrayList<User> users = new ArrayList<User>();
    private final String fileName;

    private int findIndex(String userName) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserName().equals(userName)) {
                return i;
            }
        }
        return -1;
    }

    private void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (User u : users) {
                // Create the CSV line: "username,password,mfa"
                String line = u.getUserName() + "," + u.getPassword() + "," + u.getMfaCode();
                writer.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error writing file.");
        }
    }

    private void loadFromFile() {
        //Started with Gemini snippet
        ArrayList<User> userList = new ArrayList<>();
        File file = new File(fileName);

        //Read the file
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                if (parts.length == 3) {
                    String username = parts[0];
                    String password = parts[1];
                    String mfaCodeStr = parts[2];

                    int mfaCode = 0;
                    try {
                        mfaCode = Integer.parseInt(mfaCodeStr);
                    } catch (NumberFormatException e) {
                        //This could be more verbose, but leaving it vague since server and client are the same in this context.
                        System.out.println("Detected invalid MFA code. Skipping user.");
                        continue;
                    }
                    userList.add(new User(username, password, mfaCode));
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error reading file.");
        }
        users = userList;
    }

    public FileUserDB() {
        users = new ArrayList<User>();
        fileName = "users.txt";
        File file = new File(fileName);
        if (file.exists()) {
            loadFromFile();
        }
    }

    public FileUserDB(String fileName) {
        users = new ArrayList<User>();
        this.fileName = fileName;
        File file = new File(fileName);
        if (file.exists()) {
            loadFromFile();
        }
    }

    @Override
    public Optional<User> findByUsername(String userName) {
        return users.stream()
            .filter(u -> u.getUserName().equals(userName))
            .findFirst();
    }

    @Override
    public void save(User user) {
        int index = findIndex(user.getUserName());
        if (index == -1) {
            users.add(user);
        } else {
            users.set(index, user);
        }
        saveToFile();
    }

    @Override
    public void delete(String userName) {
        int index = findIndex(userName);
        users.remove(index);
        saveToFile();
    }
}
