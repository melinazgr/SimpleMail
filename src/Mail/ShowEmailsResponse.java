package Mail;

import java.io.BufferedReader;
import java.io.IOException;

public class ShowEmailsResponse extends Command{
    final public static String COMMANDNAME = "RESPONSE:SHOW";
    final public static String ERROR = "ERRORCODE:";
    final public static String END = "ENDRESPONSE";

    final public static String SUCCESS = "SUCCESS"; //show emails ok
    final public static String FAIL = "FAIL"; // show emails failure

    public ShowEmailsResponse(){}

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
