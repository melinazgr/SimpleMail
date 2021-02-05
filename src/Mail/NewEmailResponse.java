package Mail;

import java.io.BufferedReader;
import java.io.IOException;

public class NewEmailResponse extends Command{
    private String errorCode;
    private String errorMessage;

    final public static String COMMANDNAME = "RESPONSE:NEWEMAIL";
    final public static String ERROR = "ERRORCODE:";
    final public static String END = "ENDRESPONSE";

    final public static String SUCCESS = "SUCCESS"; //new email created ok
    final public static String FAIL = "FAIL"; // new email creation failure


    public NewEmailResponse(){}

    public NewEmailResponse(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode(){
        return this.errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public CommandType getType() {
        return CommandType.NewEmailResponse;
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

    public String createPacket(){
        return createPacket(this.errorCode);
    }

    public String createPacket(String errorCode) {
        String s = COMMANDNAME + "\n" +
                ERROR + errorCode + "\n" +
                END +  "\n";

        return s;
    }
}
