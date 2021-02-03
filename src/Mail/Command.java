package Mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;

public abstract class Command {

    public enum CommandType{
        Register,
        RegisterResponse,
        Login,
        Logout,
    }

    public abstract CommandType getType();

    public static Command parse (BufferedReader in) throws IOException, InterruptedException {
        System.out.println("reading input");

        while(!in.ready()){
            Thread.sleep(500);
            System.out.print(".");
        }

        String command = (String)in.readLine();
        System.out.println("command : " +command);

//        System.out.println("length : " + command.length());
//        System.out.println("command : " +command);

        if(command == null || command.length() == 0){
            System.out.println("Null received");
            return null;
        }

        if(command.startsWith(RegisterRequest.COMMANDNAME)){
            Command c = new RegisterRequest();
            c.parsePacket(in);
            return c;
        }

        else if(command.startsWith(RegisterResponse.COMMANDNAME)){
            Command c = new RegisterResponse();
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
        else{
            System.out.println("Unknown packet: "+ command);
        }

        //TODO ALL PACKETS
        return null;
    }

    public abstract void parsePacket(BufferedReader in) throws IOException;

    public abstract String createPacket();
}

