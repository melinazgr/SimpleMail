package Mail;

import java.io.BufferedReader;
import java.io.IOException;

public abstract class Command {

    public static Command parse (BufferedReader in) throws IOException {

        String command = (String)in.readLine();

        if(command.startsWith(RegisterCommand.COMMANDNAME)){
            Command c = new RegisterCommand();
            c.parsePacket(in);
            return c;
        }

        else if(command.startsWith(LoginCommand.COMMANDNAME)){
            Command c = new LoginCommand();
            c.parsePacket(in);
            return c;
        }


        //TODO ALL PACKETS
        return null;
    }

    public abstract void parsePacket(BufferedReader in) throws IOException;

    public abstract String createPacket();
}

