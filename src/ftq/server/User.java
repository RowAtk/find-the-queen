package ftq.server;

/**
 * Class representing a user/player in the Find the queen game system
 */
public class User {

    String username, password;
    boolean loggedIn;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.loggedIn = false;
    }

    public boolean match(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public boolean isLoggedIn() {
        return this.loggedIn;
    }
}