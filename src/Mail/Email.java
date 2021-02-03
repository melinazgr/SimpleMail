package Mail;

public class Email {
    public boolean isNew = false;
    public String id, sender, receiver, subject, mainbody;

    public Email(){}

    public Email(String id, String sender, String receiver, String subject, String mainbody, boolean isNew){
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.subject = subject;
        this.mainbody = mainbody;
        this.isNew = isNew;
    }
}


// todo setters getters