package Mail;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * handles New Email request.
 *
 * @author Melina Zikou
 *
 */
public class NewEmailRequest extends Command {
    private String sender, receiver, subject, mainbody;

    final public static String COMMANDNAME = "COMMAND:NEW";
    final public static String SENDER = "SENDER:";
    final public static String RECEIVER = "RECEIVER:";
    final public static String SUBJECT = "SUBJECT:";
    final public static String MAINBODYSIZE = "SIZE:";
    final public static String END = "ENDCOMMAND";

    public NewEmailRequest(){}

    public NewEmailRequest(String sender, String receiver, String subject, String mainbody) {
        this.sender = sender;
        this.receiver = receiver;
        this.subject = subject;
        this.mainbody = mainbody;
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

    @Override
    public CommandType getType() {
        return CommandType.NewEmail;
    }

    @Override
    public void parsePacket(BufferedReader in) throws IOException {
        String line = (String)in.readLine();

        while(!line.startsWith(END)){
            if(line.startsWith(SENDER)){
                this.sender = line.substring(SENDER.length());
            }
            else if(line.startsWith(RECEIVER)){
                this.receiver = line.substring(RECEIVER.length());
            }
            else if(line.startsWith(SUBJECT)){
                this.subject = line.substring(SUBJECT.length());
            }
            else if(line.startsWith(MAINBODYSIZE)){
                int len = Integer.parseInt(line.substring(MAINBODYSIZE.length()));
                this.mainbody = readString(in, len);
            }
            line = (String)in.readLine();
        }
    }

    @Override
    public String createPacket() {
        String s = COMMANDNAME + "\n" +
                    SENDER + sender + "\n" +
                    RECEIVER + receiver + "\n" +
                    SUBJECT + subject + "\n" +
                    MAINBODYSIZE + mainbody.length() + "\n" +
                    mainbody + "\n" +
                    END +  "\n";

        return s;
    }
}
