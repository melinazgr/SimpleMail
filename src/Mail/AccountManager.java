package Mail;

import java.util.ArrayList;

public class AccountManager {

    private static AccountManager singleInstance = null;
    private ArrayList<Account> users;

    // private constructor restricted to this class itself
    private AccountManager() {
        this.users = new ArrayList<Account>();
    }

    // static method to create instance of AccountManager class
    public static AccountManager getInstance()
    {
        if (singleInstance == null)
            singleInstance = new AccountManager();

        return singleInstance;
    }

    public ArrayList<Account> getUsers() {
        return users;
    }

    public void addAccount(Account user){
        users.add(user);
    }

    public Account findAccount(String username){

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username)) {
                return users.get(i);
            }
        }
        return null;
    }
}