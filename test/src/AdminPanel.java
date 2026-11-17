import java.util.Scanner;

/**
 * CEN 4078 Programming Exercise 1
 * File Name: AdminPanel.java
 * Admin panel for making changes to users
 * @author Caleb Metz
 * @version 2.2
 */
public class AdminPanel {

    private User user;
    private AuthService authService;
    private Validation validator = new Validation();
    private Scanner scanner = new Scanner(System.in);
    private DefaultPassword defaultPassword = new DefaultPassword(validator);
    private UserDataStore userDB;

    public AdminPanel(AuthService authService, UserDataStore userDB, User user) {
        this.authService = authService;
        this.userDB = userDB;
        this.user = user;
    }

    private void printMenu() {
        System.out.println("Please choose from the following options:");
        System.out.println("1) Update password");
        System.out.println("2) Create new user");
        System.out.println("3) Logout");

    }

    private String updatePassword() {
        int numAttempts = 2;
        String password;
        System.out.println(validator.getPasswordPolicy());
        for (int i = 0; i < numAttempts; i++) {
            System.out.print("Please enter a new password: ");
            password = scanner.nextLine();
            if (validator.isValidPassword(password)) {
                return password;
            }
            System.out.println("Please try again.");
        }
        System.out.println("Too many attempts.");
        return defaultPassword.generateDefaultPassword();
    }

    private boolean newUser() {
        System.out.print("Please type the intended username: ");
        String username = scanner.nextLine();
        System.out.print("Please type the intended password: ");
        String password = updatePassword();
        System.out.print("Please type the intended MFACode: ");
        String mfaCode = scanner.nextLine();

        if (userDB.findByUsername(username).isPresent()) {
            System.out.println("Unable to create user.");
            return false;
        }
        return authService.saveUser(username, password, mfaCode);
    }

    public void start() {

        boolean done = false;

        System.out.println("Welcome, " + user.getUserName() + "!");
        while(!done) {
            printMenu();
            String input = scanner.nextLine();
            try {
                int choice = Integer.parseInt(input);
                switch (choice) {
                    case 1:
                        if (authService.saveUser(
                                user.getUserName(),
                                updatePassword(),
                                String.valueOf(user.getMfaCode())
                        )) {
                            System.out.println("Successfully updated password.");
                        } else {
                            System.out.println("Unable to change password.");
                        }
                        break;
                    case 2:
                        if (newUser()) {
                            System.out.println("Successfully created user.");
                        } else {
                            System.out.println("Unable to create user.");
                        }
                        break;
                    case 3:
                        done = true;
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please input a valid integer!");
            }
        }
    }
}
