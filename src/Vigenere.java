import jdk.internal.util.xml.impl.Pair;

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

    private char caesar(char key, char input) {

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
        int normalizedInput = input - inputMin;

        int desiredCharIndex = (normalizedInput + offset) % inputAlphabetSize;

        return (char) (desiredCharIndex + inputMin);
    }

    public String encrypt(String alphaKey, String input) {

        int keyLength = alphaKey.length();
        int keyIndex = 0;
        
        StringBuilder result = new StringBuilder();
        
        for (char character: input.toCharArray()){
            result.append(caesar(alphaKey.charAt((keyIndex++ % keyLength)), character));
        }

        return result.toString();
    }
    public String decrypt(String alphaKey, String input) {
        return input;
    }
    public int encrypt(int numberKey, int input) {
        return input;
    }
    public int decrypt(int numberKey, int input) {
        return input;
    }
}
