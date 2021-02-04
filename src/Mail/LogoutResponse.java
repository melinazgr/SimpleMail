package Mail;

import java.io.BufferedReader;
import java.io.IOException;

public class LogoutResponse extends Command {
    final public static String COMMANDNAME = "RESPONSE:LOGOUT";
    final public static String ERROR = "ERRORCODE:";
    final public static String END = "ENDRESPONSE";

    final public static String SUCCESS = "SUCCESS"; //user loged out ok
    final public static String FAIL = "FAIL"; // logout failure

    public LogoutResponse(){}

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
