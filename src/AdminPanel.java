import java.util.Scanner;

/**
 * CEN 4078 Programming Exercise 1
 * File Name: AdminPanel.java
 * Admin panel for making changes to users
 * @author Caleb Metz
 * @version 1.0
 */
public class AdminPanel {

    private User user;
    private AuthService authService;
    private Validation validator = new Validation();
    private Scanner scanner = new Scanner(System.in);
    private DefaultPassword defaultPassword = new DefaultPassword(validator);

    public AdminPanel(AuthService authService, User user) {
        this.authService = authService;
        this.user = user;
    }

    private void printMenu() {
        System.out.println("Please choose from the following options:");
        System.out.println("1) Update password");
        System.out.println("2) Create new user");
        System.out.println("3) Logout");

    }

    private void updatePassword() {
        int numAttempts = 2;
        String password;
        System.out.println(Validation.getPasswordPolicy());
        System.out.println("Please enter a new password: ");
        for (int i = 0; i < numAttempts; i++) {
            password = scanner.nextLine();
            if (validator.isValidPassword(password)) {
                if (authService.saveUser(user.getUserName(), password, String.valueOf(user.getMfaCode()))) {
                    System.out.println("Successfully changed password");
                    return;
                } else {
                    System.out.println("Error occurred when saving user");
                }
            }
            System.out.println("Please try again.");
        }
        authService.saveUser(user.getUserName(), defaultPassword.generateDefaultPassword(), String.valueOf(user.getMfaCode()));
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
                        updatePassword();
                        break;
                    case 2:
                        //newUser();
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
