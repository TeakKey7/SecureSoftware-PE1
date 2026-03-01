import java.io.Console;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.Scanner;

/**
 CEN 4078 Programming Exercise 1
 File Name: LoginApp.java
 Runs a CLI app to auto initialize a user database then allows a user to attempt login
 @author Caleb Metz
 @version 2.0
 */

public class LoginApp {
    public static void main(String[] args) {

        FileUserDB userdb = new FileUserDB("default.txt");
        Validation validator = new Validation();
        Cryptographer vigenere = new Vigenere();

        String alphaKey = "ARGOSROCK";

        AuthService auth = new AuthService(
                userdb,
                validator,
                new SimpleMFA(validator)
        );
        //Removed test print statements from project 1. Encypted passwords
        auth.saveUser("scientist", vigenere.encrypt(alphaKey, "BlAckM3sa1"), "1119199800");
        auth.saveUser("engineer", vigenere.encrypt(alphaKey, "G0rdonFr33"), "1119199800");
        auth.saveUser("security", vigenere.encrypt(alphaKey,"Barn3yCal"), "1119199800");

        String username;
        String password;
        String mfaString;

        Console console = System.console();

        //Gemini generated since the password prompting is all Java-ese I wouldn't have learned
        if (console != null) {
            // --- OPTION A: SECURE TERMINAL (Hides Password) ---
            // Note: When typing the password here, the cursor will NOT move.
            // It stays invisible for security (like Linux sudo).

            String userPrompt = "Enter Username: ";
            String passPrompt = "Enter Password: ";
            String mfaPrompt = "Enter MFA OTP: ";

            username = console.readLine(userPrompt);
            char[] passArray = console.readPassword(passPrompt);
            password = new String(passArray); // Convert char[] to String for your validation logic
            mfaString = console.readLine(mfaPrompt);

        } else {
            // --- OPTION B: IDE FALLBACK (Visible Password) ---
            // Use this only for testing in IntelliJ/Eclipse where Console is unavailable.

            Scanner scanner = new Scanner(System.in);

            System.out.println("WARNING: Running in IDE. Password will be visible.");
            System.out.print("Enter Username: ");
            username = scanner.nextLine();

            System.out.print("Enter Password: ");
            password = scanner.nextLine();
            System.out.print("Enter MFA OTP: ");
            mfaString = scanner.nextLine();
        }

        String encryptedPassword = vigenere.encrypt(alphaKey, password);

        if (encryptedPassword.isEmpty()) {
            System.out.println("Login failed.");
            System.exit(1);
        }

        System.out.println("\nAuthenticating...");
        User userLogin = auth.authenticate(username,encryptedPassword,mfaString);
        if (userLogin != null) {
            AdminPanel adminPanel = new AdminPanel(auth, userLogin);
            adminPanel.start();
        } else {
            System.out.println("Failed to login.");
            System.exit(1);
        }

    }
}