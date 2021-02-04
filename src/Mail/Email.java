package Mail;

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

    public boolean isNew() {
        return isNew;
    }

    public String getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSubject() {
        return subject;
    }

    public String getMainbody() {
        return mainbody;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setMainbody(String mainbody) {
        this.mainbody = mainbody;
    }
}
