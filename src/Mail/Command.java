package Mail;

import java.io.BufferedReader;
import java.io.IOException;

public abstract class Command {

    public enum CommandType{
        Register,
        RegisterResponse,
        Login,
        Logout,
    }

    public abstract CommandType getType();

    public static Command parse (BufferedReader in) throws IOException {

        String command = (String)in.readLine();

        if(command.startsWith(RegisterRequest.COMMANDNAME)){
            Command c = new RegisterRequest();
            c.parsePacket(in);
            return c;
        }

        else if(command.startsWith(LoginRequest.COMMANDNAME)){
            Command c = new LoginRequest();
            c.parsePacket(in);
            return c;
        }

        else if(command.startsWith(LogoutRequest.COMMANDNAME)){
            Command c = new LogoutRequest();
            c.parsePacket(in);
            return c;
        }


        //TODO ALL PACKETS
        return null;
    }

    public abstract void parsePacket(BufferedReader in) throws IOException;

    public abstract String createPacket();
}

