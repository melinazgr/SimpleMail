package Mail;

import java.util.ArrayList;

/**
 * Singleton class that stores all the
 * accounts created when a connection with
 * the server is initialized
 *
 * @author Melina Zikou
 *
 */
public class AccountManager {

    private static AccountManager singleInstance = null;
    private ArrayList<Account> users;

    /**
     * private constructor restricted to this class itself
     */
    private AccountManager() {
        this.users = new ArrayList<Account>();
    }

    /**
     * create instance of AccountManager class
     * @return instance of the class
     */
    public static AccountManager getInstance()
    {
        if (singleInstance == null)
            singleInstance = new AccountManager();

        return singleInstance;
    }

    public ArrayList<Account> getUsers() {
        return users;
    }

    /**
     * add account to users
     * @param user to be added
     */
    public void addAccount(Account user){
        users.add(user);
    }

    /**
     * finds an account on the users list based on their username
     * @param username of the user
     * @return true if the account exists
     *         false otherwise
     */
    public Account findAccount(String username){

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username)) {
                return users.get(i);
            }
        }
        return null;
    }
}