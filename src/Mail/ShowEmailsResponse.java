package Mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * handles Show Emails response
 *
 * @author Melina Zikou
 *
 */
public class ShowEmailsResponse extends Command{
    private String errorCode;
    private String errorMessage;
    private ArrayList<Email> mailbox;

    final public static String COMMANDNAME = "RESPONSE:SHOW";
    final public static String ERROR = "ERRORCODE:";
    final public static String END = "ENDRESPONSE";

    final public static String MAILCOUNT = "MAILCOUNT:";

    final public static String SUCCESS = "SUCCESS"; //show emails ok
    final public static String FAIL = "FAIL"; // show emails failure

    public ShowEmailsResponse(){
        this.mailbox = new ArrayList<Email>();
    }

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

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public CommandType getType() {
        return CommandType.ShowEmailsResponse;
    }

    @Override
    public void parsePacket(BufferedReader in) throws IOException {
        this.mailbox.clear();
        String line = (String) in.readLine();

        while (!line.startsWith(END)) {
            if (line.startsWith(ERROR)) {
                this.errorCode = line.substring(ERROR.length());
            }
            else if (line.startsWith(MAILCOUNT)) {
                int count = Integer.parseInt(line.substring(MAILCOUNT.length()));

                for(int i = 0; i < count; i++){
                    Email email = new Email();

                    line = (String) in.readLine();
                    email.setId(line);

                    line = (String) in.readLine();
                    email.setSender(line);

                    line = (String) in.readLine();
                    email.setSubject(line);

                    line = (String) in.readLine();
                    email.setIsNew( Boolean.parseBoolean(line));

                    this.mailbox.add(email);
                }
            }

            line = (String) in.readLine();
        }
    }

    @Override
    public String createPacket() {
        StringBuilder sb = new StringBuilder();

        sb.append(COMMANDNAME + '\n');
        sb.append(ERROR + errorCode + "\n");

        sb.append(MAILCOUNT + mailbox.size() + "\n");

        for(Email e : mailbox){
            sb.append(e.getId() + "\n");
            sb.append(e.getSender() + "\n");
            sb.append(e.getSubject() + "\n");
            sb.append(e.getIsNew() + "\n");
        }

        sb.append(END + "\n");

        return sb.toString();
    }
}
