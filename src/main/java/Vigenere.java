/**
 * CEN 4078 Programming Exercise 2
 * File Name: Vigenere.java
 * Implements Cryptographer to allow a Vigenere cipher to encrypt string or int
 * @author Caleb Metz
 * @version 1.0
 */
public class Vigenere implements Cryptographer {

    private String findMinMax(char input) {
        char min;
        char max;
        if (Character.isDigit(input)) {
            min = '0'; //48
            max = '9'; //57
        } else if (Character.isUpperCase(input)) {
            min = 'A'; //65
            max = 'Z'; //90
        } else if (Character.isLowerCase(input)) {
            min = 'a'; //97
            max = 'z'; //122
        } else {
            return null;
        }
        return "" + min + max;
    }

    private int vigenere(int numberKey, int input, boolean encryption) {
        String encryptionKey = String.valueOf(numberKey);
        String encryptionInput = String.valueOf(input);
        return Integer.parseInt(vigenere(encryptionKey, encryptionInput, encryption));
    }

    private String vigenere(String alphaKey, String input, boolean encryption) {
        int keyLength = alphaKey.length();
        int keyIndex = 0;

        StringBuilder result = new StringBuilder();

        for (char character: input.toCharArray()){
            result.append(caesar(alphaKey.charAt((keyIndex++ % keyLength)), character, encryption));
        }

        return result.toString();
    }

    private char caesar(char key, char input, boolean encrypting) {

        String keyMinMax = findMinMax(key);
        String inputMinMax = findMinMax(input);

        if (inputMinMax == null || keyMinMax == null) {
            return input;
        }

        char keyMin;
        char keyMax;
        int keyAlphabetSize;
        char inputMin;
        char inputMax;
        int inputAlphabetSize;

        keyMin = keyMinMax.charAt(0);
        keyMax = keyMinMax.charAt(1);

        inputMin = inputMinMax.charAt(0);
        inputMax = inputMinMax.charAt(1);

        inputAlphabetSize = inputMax - inputMin + 1; //Fix 0 based index issue

        int offset = key - keyMin;
        //Gemini provided this solution when I was refactoring for decryption.
        int direction;
        if (Character.isDigit(input)) {
            direction = encrypting ? -1 : 1;
            inputAlphabetSize = 10;
        } else {
            direction = encrypting ? 1 : -1;
        }

        int normalizedInput = input - inputMin;

        int desiredCharIndex = (normalizedInput + (direction * (offset % inputAlphabetSize)) + inputAlphabetSize) % inputAlphabetSize;

        return (char) (desiredCharIndex + inputMin);
    }

    public String encrypt(String alphaKey, String input) {
        return vigenere(alphaKey,input, true); //Call with encryption: true
    }
    public String decrypt(String alphaKey, String input) {
        return vigenere(alphaKey, input, false); //Call with encryption: false
    }
    public int encrypt(int numberKey, int input) {
        return vigenere(numberKey,input, true); //Call with encryption: true
    }
    public int decrypt(int numberKey, int input) {
        return vigenere(numberKey, input, false); //Call with encryption: false
    }
}
