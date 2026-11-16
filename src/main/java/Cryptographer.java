/**
 * CEN 4078 Programming Exercise 2
 * File Name: Cryptographer.java
 * Interface to allow a symmetric key cryptography class to be implemented
 * @author Caleb Metz
 * @version 1.0
 */
public interface Cryptographer {
    public String encrypt(String alphaKey, String input);
    public String decrypt(String alphaKey, String input);
    public int encrypt(int numberKey, int input);
    public int decrypt(int numberKey, int input);
}
