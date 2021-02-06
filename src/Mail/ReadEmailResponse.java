package Mail;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadEmailResponse extends Command{
    private String errorCode;
    private String errorMessage;
    private Email email;


    final public static String COMMANDNAME = "RESPONSE:READ";
    final public static String ERROR = "ERRORCODE:";
    final public static String END = "ENDRESPONSE";

    final public static String ID = "ID:";
    final public static String FROM = "FROM:";
    final public static String RECEIVER = "RECEIVER:";
    final public static String SUBJECT = "SUBJECT:";
    final public static String MAINBODY = "MAINBODY:";


    final public static String SUCCESS = "SUCCESS"; //read email ok
    final public static String FAIL = "FAIL"; // read email failure

    public ReadEmailResponse(){}

    public ReadEmailResponse(String errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }


    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Email getEmail() {
        return email;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    @Override
    public CommandType getType() {
        return CommandType.ReadEmailResponse;
    }

    @Override
    public void parsePacket(BufferedReader in) throws IOException {
        String line = (String) in.readLine();

        while (!line.startsWith(END)) {
            if (line.startsWith(ERROR)) {
                this.errorCode = line.substring(ERROR.length());
            }
            if (line.startsWith(RECEIVER)) {
                this.email.setReceiver(line.substring(RECEIVER.length()));
            }
            if (line.startsWith(ID)) {
                this.email.setId(line.substring(ID.length()));
            }
            if (line.startsWith(FROM)) {
                this.email.setSender( line.substring(FROM.length()));
            }
            if (line.startsWith(SUBJECT)) {
                this.email.setSubject(line.substring(SUBJECT.length()));
            }
            if (line.startsWith(MAINBODY)) {
                this.email.setMainbody(line.substring(MAINBODY.length()));
            }

            line = (String) in.readLine();
        }

    }

    @Override
    public String createPacket() {
        StringBuilder sb = new StringBuilder();

        sb.append(COMMANDNAME + '\n');
        sb.append(ERROR + errorCode + "\n");
        sb.append(ID + email.getId() + "\n");
        sb.append(FROM + email.getSender() + "\n");
        sb.append(SUBJECT + email.getSubject() + "\n");
        sb.append(MAINBODY+ email.getMainbody() + "\n");
        sb.append(END);

        return sb.toString();
    }
}
