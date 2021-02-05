package Mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class ShowEmailsResponse extends Command{
    private String errorCode;
    private String errorMessage;
    private ArrayList<Email> mailbox;


    final public static String COMMANDNAME = "RESPONSE:SHOW";
    final public static String ERROR = "ERRORCODE:";
    final public static String END = "ENDRESPONSE";

    final public static String ID = "ID:";
    final public static String FROM = "FROM:";
    final public static String SUBJECT = "SUBJECT:";

    final public static String SUCCESS = "SUCCESS"; //show emails ok
    final public static String FAIL = "FAIL"; // show emails failure

    public ShowEmailsResponse(){}

    public ShowEmailsResponse(String errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ArrayList<Email> getMailbox() {
        return mailbox;
    }

    public void setMailbox(ArrayList<Email> mailbox) {
        this.mailbox = mailbox;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public CommandType getType() {
        return CommandType.ShowEmailsResponse;
    }

    @Override
    public void parsePacket(BufferedReader in) throws IOException {
        String line = (String) in.readLine();

        while (!line.startsWith(END)) {
            if (line.startsWith(ERROR)) {
                this.errorCode = line.substring(ERROR.length());
            }

            for (Email e : mailbox) {
                if (line.startsWith(ID)) {
                    e.setId(line.substring(ID.length()));
                    line = (String) in.readLine();

                    if (line.startsWith(FROM)) {
                        e.setId(line.substring(FROM.length()));
                        line = (String) in.readLine();

                        if (line.startsWith(SUBJECT)) {
                            e.setSubject(line.substring(SUBJECT.length()));
                        }
                    }
                }

                line = (String) in.readLine();
            }
        }
    }

    @Override
    public String createPacket() {
        StringBuilder sb = new StringBuilder();

        sb.append(COMMANDNAME + '\n');
        sb.append(ERROR + errorCode + "\n");

        for(Email e : mailbox){
            sb.append(ID + e.getId() + "\n");
            sb.append(FROM + e.getSender() + "\n");
            sb.append(SUBJECT + e.getSubject() + "\n");
        }

        sb.append(END);

        return sb.toString();
    }
}