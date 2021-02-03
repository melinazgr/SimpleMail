package Mail;

import java.util.ArrayList;

public class AccountManager {

    private static AccountManager singleInstance = null;
    private ArrayList<Account> users;


    // private constructor restricted to this class itself
    private AccountManager() {
        ArrayList<Account> users = new ArrayList<>();
    }

    // static method to create instance of AccountManager class
    public static AccountManager getInstance()
    {
        if (singleInstance == null)
            singleInstance = new AccountManager();

        return singleInstance;
    }


    public void addAccount(Account user){
        users.add(user);
    }

//    public void addAccount(String username, String password){
//        users.add(user);
//    }

    public Account findAccount(String username){

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).username.equals(username)) {
                return users.get(i);
            }
        }
        return null;
    }
}
