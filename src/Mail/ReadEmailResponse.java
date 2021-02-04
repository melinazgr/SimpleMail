package Mail;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadEmailResponse extends Command{
    final public static String COMMANDNAME = "RESPONSE:READ";
    final public static String ERROR = "ERRORCODE:";
    final public static String END = "ENDRESPONSE";

    final public static String SUCCESS = "SUCCESS"; //read email ok
    final public static String FAIL = "FAIL"; // read email failure

    public ReadEmailResponse(){}

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
