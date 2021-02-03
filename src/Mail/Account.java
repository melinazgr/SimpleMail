package Mail;

import java.util.ArrayList;

public class Account {
    public String username, password;
    ArrayList<Email> mailbox;

    public Account (String username, String password){
        ArrayList<Email> mailbox = new ArrayList<>();

        this.username = username;
        this.password = password;
    }

    public void addEmail(Email e){
        mailbox.add(e);
    }

    public void deleteEmail(String id){
        for (int i = 0; i < mailbox.size(); i++){
            if(mailbox.get(i).id.equals(id)){
                mailbox.remove(i);
                break;
            }
        }
    }
}
