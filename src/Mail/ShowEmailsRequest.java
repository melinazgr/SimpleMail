package Mail;

import java.io.BufferedReader;
import java.io.IOException;

public class ShowEmailsRequest extends Command {
    final public static String COMMANDNAME = "COMMAND:SHOW";
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
