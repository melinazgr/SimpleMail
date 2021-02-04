package Mail;

import java.io.BufferedReader;
import java.io.IOException;

public class NewEmailResponse extends Command{
    final public static String COMMANDNAME = "RESPONSE:NEWEMAIL";
    final public static String ERROR = "ERRORCODE:";
    final public static String END = "ENDRESPONSE";

    final public static String SUCCESS = "SUCCESS"; //user loged in ok
    final public static String FAIL = "FAIL"; // login failure

    public NewEmailResponse(){}

    @Override
    public CommandType getType() {
        return null;
    }

    @Override
    public void parsePacket(BufferedReader in) throws IOException {

    }

    @Override
    public String createPacket() {
        return null;
    }
}
