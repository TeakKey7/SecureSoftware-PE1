/**
 * CEN 4078 Programming Exercise 1
 * File Name: User.java
 * Provides the instance variables for a user
 * @author Caleb Metz
 * @version 1.0
 */
public class User {
    private String userName;
    private String password;
    private int mfaCode;

    public User(String userName, String password, int mfaCode) {
        this.userName = userName;
        this.password = password;
        this.mfaCode = mfaCode;
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.mfaCode = -1;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMfaCode() {
        return mfaCode;
    }

    public void setMfaCode(int mfaCode) {
        this.mfaCode = mfaCode;
    }
}
