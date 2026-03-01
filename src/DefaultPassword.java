import java.util.Random;

/**
 * CEN 4078 Programming Exercise 2
 * File Name: DefaultPassword.java
 * A class to create a default password that meets policy
 * TODO: Send emails and notifications when called
 * @author Caleb Metz
 * @version 1.0
 */
public class DefaultPassword {
    //I relied heavily on Gemini for this. I am bad at using the Random library
    Validation validator;
    Random random = new Random();

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";

    public DefaultPassword(Validation validator) {
        this.validator = new Validation();
    }

    private int[] bucketGenerator() {
        int length = random.nextInt(5) + 8;

        int numUpper = 1;
        int numLower = 1;
        int numDigit = 1;

        int remaining = length - 3;

        for (int i = 0; i < remaining; i++) {
            int bucket = random.nextInt(3);

            switch(bucket) {
                case 0:
                    numUpper++;
                case 1:
                    numLower++;
                default:
                    numDigit++;
            }
        }

        return new int[]{numUpper, numLower, numDigit};
    }

    public String generateDefaultPassword() {

        StringBuilder result = new StringBuilder();

        int[] buckets = bucketGenerator();

        for (int i = 0; i < buckets[0]; i++) {
            result.append(UPPER.charAt(random.nextInt(UPPER.length())));
        }

        for (int i = 0; i < buckets[1]; i++) {
            result.append(LOWER.charAt(random.nextInt(LOWER.length())));
        }

        for (int i = 0; i < buckets[2]; i++) {
            result.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        }

        char[] characters = result.toString().toCharArray();
        for (int i = characters.length - 1; i > 0; i--) {
            int randomIndex = random.nextInt(i + 1);
            // Swap characters
            char temp = characters[randomIndex];
            characters[randomIndex] = characters[i];
            characters[i] = temp;
        }
        String finalString = new String(characters);
        if (!validator.isValidPassword(finalString)) {
         return null;
        }
        return finalString;
    }

    private void sendMessage() {
        System.out.println("This is a placeholder message for you to receive a password over email");
    }

}
