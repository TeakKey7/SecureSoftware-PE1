import java.io.Console;
import java.util.Scanner;

/**
 CEN 4078 Programming Exercise 1
 File Name: LoginApp.java
 Runs a CLI app to auto initialize a user database then allows a user to attempt login
 @author Caleb Metz
 @version 1.0
 */

public class LoginApp {
    public static void main(String[] args) {

        FileUserDB userdb = new FileUserDB("default.txt");
        Validation validator = new Validation();
        AuthService auth = new AuthService(userdb, validator, new SimpleMFA(validator));

        //The MFA is kind of weird, I just hard coded it since it doesn't matter.
        // Let me know if you know what the MFA code I added means.
        System.out.println("Adding user: scientist.");
        auth.saveUser("scientist", "Bl@ckM3sa!", "1119199800");
        System.out.println("Adding user: engineer.");
        auth.saveUser("engineer", "G0rdonFr33", "1119199800");
        System.out.println("Adding user: security.");
        auth.saveUser("security", "B@rn3yCal", "1119199800");
        System.out.println("Adding user: test-.");
        auth.saveUser("test-", "B@rn3yCal", "1119199800");
        System.out.println("Adding user: test. Weak password test");
        auth.saveUser("test", "B@rnyCal", "1119199800");
        System.out.println("Adding user: test. Small MFA code test.");
        auth.saveUser("test-", "B@rn3yCal", "11");
        System.out.println("Adding user: test. Overflow test.");
        auth.saveUser("test-", "B@rn3yCal", "11111111111111111111");

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

        System.out.println("\nAuthenticating...");
        if (auth.authenticate(username,password,mfaString)) {
            System.out.println("Welcome, " + username);
        } else {
            System.out.println("Failed to login.");
        }

    }
}