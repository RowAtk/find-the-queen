package ftq.server;

import java.io.PrintWriter;

import ftq.utils.FTQUtils;

/**
 * Class representing a user/player in the Find the queen game
 */
public class UserAdmin {

    public static User[] users = new User[]{
        new User("dannyboi", "dre@margh_shelled"),
        new User("matty7", "win&win99")
    };

    public static boolean login(String username, String password, PrintWriter out) {
        for (User user : users) {
            if(user.match(username, password)) {
                // FTQUtils.print(user);
                if(!user.isLoggedIn()) {
                    user.loggedIn = true;
                    out.println("200-User " + username + " successfully logged in");
                    return true;
                }
                out.println("104-User " + username + " already logged in @Try again?(Y|N)");
                return false;
            }
        }
        out.println("104-Incorrect login credentials@Try again?(Y|N)");
        return false;
    }

}
