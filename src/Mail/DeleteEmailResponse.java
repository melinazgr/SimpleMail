package Mail;

import java.io.BufferedReader;
import java.io.IOException;

public class DeleteEmailResponse extends Command{
    private String errorCode;

    final public static String COMMANDNAME = "RESPONSE:DELETE";
    final public static String ERROR = "ERRORCODE:";
    final public static String END = "ENDRESPONSE";

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public CommandType getType() {
        return null;
    }

    @Override
    public void parsePacket(BufferedReader in) throws IOException {

    }

    @Override
    public String createPacket() {
        String s = COMMANDNAME + "\n" +
                ERROR + errorCode + "\n" +
                END +  "\n";

        return s;
    }
}
