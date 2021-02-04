package Mail;

import java.util.ArrayList;

public class Account {
    private String username, password;
    private ArrayList<Email> mailbox;

    public Account (String username, String password){
        ArrayList<Email> mailbox = new ArrayList<>();

        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Email> getMailbox() {
        return mailbox;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addEmail(Email e){
        mailbox.add(e);
    }

    public Email findEmail(String id){
        for (Email e : mailbox){
            if(e.getId().equals(id)){
                return e;
            }
        }
        return null;
    }

    public void deleteEmail(String id){
        for (int i = 0; i < mailbox.size(); i++){
            if(mailbox.get(i).getId().equals(id)){
                mailbox.remove(i);
                break;
            }
        }
    }
}
