package Mail;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * handles Read Email response
 *
 * @author Melina Zikou
 *
 */
public class ReadEmailResponse extends Command{
    private String errorCode;
    private String errorMessage;
    private Email email;

    final public static String COMMANDNAME = "RESPONSE:READ";
    final public static String ERROR = "ERRORCODE:";
    final public static String END = "ENDRESPONSE";

    final public static String MAINBODYSIZE = "SIZE:";

    final public static String EMAIL = "EMAILINFO:";

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

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Email getEmail() {
        return email;
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
            else if(line.startsWith(EMAIL)){
                Email e = new Email();

                line = (String) in.readLine();
                e.setId(line);

                line = (String) in.readLine();
                e.setSender(line);

                line = (String) in.readLine();
                e.setSubject(line);

                line = (String) in.readLine();

                if(line.startsWith(MAINBODYSIZE)){
                    int len = Integer.parseInt(line.substring(MAINBODYSIZE.length()));
                    e.setMainbody(readString(in, len)) ;
                }

                this.email = e;
            }

            line = (String) in.readLine();
        }
    }

    @Override
    public String createPacket() {
        StringBuilder sb = new StringBuilder();

        sb.append(COMMANDNAME + '\n');
        sb.append(ERROR + errorCode + "\n");

        sb.append(EMAIL + "\n");
        sb.append(this.email.getId() + "\n");
        sb.append(this.email.getSender() + "\n");
        sb.append(this.email.getSubject() + "\n");

        sb.append(MAINBODYSIZE + this.email.getMainbody().length() + "\n");
        sb.append(this.email.getMainbody());

        sb.append(END + "\n");

        return sb.toString();
    }
}
