package Mail;

import java.io.BufferedReader;
import java.io.IOException;

public abstract class Command {

    public static Command parse (BufferedReader in) throws IOException {

        String command = (String)in.readLine();

        if(command.startsWith("COMMAND:REGISTER")){
            Command c = new RegisterCommand();
            c.parsePacket(in);
            return c;
        }
        //TODO ALL PACKETS
        return null;
    }

    public abstract Command parsePacket(BufferedReader in) throws IOException;

    public abstract String createPacket();
}

