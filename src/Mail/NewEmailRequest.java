package Mail;

import java.io.BufferedReader;
import java.io.IOException;

public class NewEmailRequest extends Command {
    private Email newEmail;

    final public static String COMMANDNAME = "COMMAND:NEW";
    final public static String SENDER = "SENDER:";
    final public static String RECEIVER = "RECEIVER:";
    final public static String EMAIL = ":";
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
