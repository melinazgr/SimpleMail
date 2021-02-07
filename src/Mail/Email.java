package Mail;

/**
 * Email class holding all the information an email has
 * [Email ID, Sender, Receiver, Subject, Main Body]
 *
 * @author Melina Zikou
 *
 */
public class Email {
    private boolean isNew = false;
    private String id, sender, receiver, subject, mainbody;

    public Email(){}

    public Email(String id, String sender, String receiver, String subject, String mainbody, boolean isNew){
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.subject = subject;
        this.mainbody = mainbody;
        this.isNew = isNew;
    }

    // getters and setters for all the fields of the email

    public boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMainbody() {
        return mainbody;
    }

    public void setMainbody(String mainbody) {
        this.mainbody = mainbody;
    }
}
