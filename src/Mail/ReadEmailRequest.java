package Mail;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadEmailRequest extends Command {
    final public static String COMMANDNAME = "COMMAND:READ";
    final public static String USERNAME = "USERNAME:";
    final public static String PASSWORD = "PASSWORD:";
    final public static String EMAIL = "EMAILID:";
    final public static String END = "ENDCOMMAND";

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
