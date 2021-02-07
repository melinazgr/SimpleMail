package Mail;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * handles delete email response
 *
 * @author Melina Zikou
 *
 */
public class DeleteEmailResponse extends Command{
    private String errorCode;
    private String errorMessage; // in case of error - state why it failed

    final public static String COMMANDNAME = "RESPONSE:DELETE";
    final public static String ERROR = "ERRORCODE:";
    final public static String END = "ENDRESPONSE";

    final public static String SUCCESS = "SUCCESS"; //delete email ok
    final public static String FAIL = "FAIL"; //delete email creation failure

    public DeleteEmailResponse(){}

    public DeleteEmailResponse(String errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
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
        return CommandType.DeleteEmailResponse;
    }

    @Override
    public void parsePacket(BufferedReader in) throws IOException {
        String line = (String)in.readLine();

        while(!line.startsWith(END)){
            if(line.startsWith(ERROR)){
                this.errorCode = line.substring(ERROR.length());
            }
            line = (String)in.readLine();
        }
    }

    @Override
    public String createPacket() {
        String s = COMMANDNAME + "\n" +
                ERROR + errorCode + "\n" +
                END +  "\n";

        return s;
    }
}
