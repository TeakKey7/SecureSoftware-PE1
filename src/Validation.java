/**
 * CEN 4078 Programming Exercise 1
 * File Name: Validation.java
 * Ensures proper input sanitization and type safety
 * 8-12 Characters
 * At least one uppercase
 * At least one lowercase
 * At least one numeric character
 * Integer between -2,147,483,648 and 2,147,483,647
 * @author Caleb Metz
 * @version 1.2
 */
public class Validation {
    private final char[] sensitiveChars;
    private final int minLength;
    private final int maxLength;

    public Validation(){
        sensitiveChars = new char[]{'/', '-', ';', '"'};
        minLength = 8;
        maxLength = 12;
    }

    public Validation(char[] sensitiveChars, int minLength, int maxLength) {
        this.sensitiveChars = sensitiveChars;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    public boolean noSQLInjection(String input) {
        for (char character : input.toCharArray()) {
            for (char special : sensitiveChars) {
                if (special == character) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isValidPassword(String input) {
        //Length between 8-12 characters
        if (input.length() < minLength || input.length() > maxLength) {
            return false;
        }
        //Contains at least one upper [Cite: Gemini & IntelliJ]
        if (input.chars().noneMatch(Character::isUpperCase)) {
            return false;
        };
        //Contains at least one lower
        if (input.chars().noneMatch(Character::isLowerCase)) {
            return false;
        }
        //Contains at least one number
        if (input.chars().noneMatch(Character::isDigit)) {
            return false;
        }
        //Meets all requirements
        return true;
    }
    public boolean noIntOverflow(String input) {
        if (input == null || input.isEmpty()) {
            return false; //Confirm input is present
        }
        for (char c : input.toCharArray()) {
            if (!Character.isDigit(c) && c != '-') {
                return false; //Confirm input is only digits or minus sign
            }
        }
        if (input.length() > 15) { return false; } //Don't overflow Long either
        long temp = Long.parseLong(input);
        if (temp <= 2147483647 && temp >= -2147483648) {
            return true; //CHANGE: moved this from SimpleMFA class to here so I could delete Validate
        };
        return false;
    }
    //Added for V2
    public static String getPasswordPolicy() {

        return "Password policy: \n" +
                "1. Must be 8-12 characters\n" +
                "2. Must contain at least one uppercase letter\n" +
                "3. Must contain at least one lowercase letter\n" +
                "4. Must contain at least one digit\n";
    }
}