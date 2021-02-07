package Mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Handles all the types of commands
 *
 * @author Melina Zikou
 */
public abstract class Command {

    /**
     * Enum with all the possible command types
     */
    public enum CommandType{
        Register,
        RegisterResponse,
        Login,
        LoginResponse,
        Logout,
        LogoutResponse,
        NewEmail,
        NewEmailResponse,
        ShowEmails,
        ShowEmailsResponse,
        ReadEmail,
        ReadEmailResponse,
        DeleteEmail,
        DeleteEmailResponse
    }

    /**
     * In each class that inherits Command
     * the type of the packet is set according to
     * the enum above
     * @return the type
     */
    public abstract CommandType getType();

    /**
     * Parses the command packets and creates the
     * command object needed according to the keyword
     * the packet starts with.
     *
     * @param in stream to read from
     * @return a command packet
     * @throws IOException
     * @throws InterruptedException
     */
    public static Command parse (BufferedReader in) throws IOException, InterruptedException {

//        while(!in.ready()){
//            Thread.sleep(500);
//            System.out.print(".");
//        }

        String command = (String)in.readLine();

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

        else if(command.startsWith(LoginResponse.COMMANDNAME)){
            Command c = new LoginResponse();
            c.parsePacket(in);
            return c;
        }

        else if(command.startsWith(LogoutRequest.COMMANDNAME)){
            Command c = new LogoutRequest();
            c.parsePacket(in);
            return c;
        }

        else if(command.startsWith(LogoutResponse.COMMANDNAME)){
            Command c = new LogoutResponse();
            c.parsePacket(in);
            return c;
        }

        else if(command.startsWith(NewEmailRequest.COMMANDNAME)){
            Command c = new NewEmailRequest();
            c.parsePacket(in);
            return c;
        }

        else if(command.startsWith(NewEmailResponse.COMMANDNAME)){
            Command c = new NewEmailResponse();
            c.parsePacket(in);
            return c;
        }

        else if(command.startsWith(ReadEmailRequest.COMMANDNAME)){
            Command c = new ReadEmailRequest();
            c.parsePacket(in);
            return c;
        }

        else if(command.startsWith(ReadEmailResponse.COMMANDNAME)){
            Command c = new ReadEmailResponse();
            c.parsePacket(in);
            return c;
        }

        else if(command.startsWith(DeleteEmailRequest.COMMANDNAME)){
            Command c = new DeleteEmailRequest();
            c.parsePacket(in);
            return c;
        }
        else if(command.startsWith(DeleteEmailResponse.COMMANDNAME)){
            Command c = new DeleteEmailResponse();
            c.parsePacket(in);
            return c;
        }
        else if(command.startsWith(ShowEmailsRequest.COMMANDNAME)){
            Command c = new ShowEmailsRequest();
            c.parsePacket(in);
            return c;
        }
        else if(command.startsWith(ShowEmailsResponse.COMMANDNAME)){
            Command c = new ShowEmailsResponse();
            c.parsePacket(in);
            return c;
        }

        else{
            System.out.println("Unknown packet: "+ command);
        }

        return null;
    }

    /**
     * Creates a packet
     * @return String with the commands of the packet
     */
    public abstract String createPacket();

    /**
     * parses the packet created
     * @param in stream to read from
     * @throws IOException
     */
    public abstract void parsePacket(BufferedReader in) throws IOException;

    /**
     * return all characters from a given String, new lines included.
     * Used in parsing the main body part of the email.
     *
     * @param in stream to read from
     * @param size of the mainbody
     * @return String with the right form of the mainbody
     * @throws IOException
     */
    public String readString(BufferedReader in, int size) throws IOException {
        char[] buffer = new char[size];
        int bytesLeft = size;
        int offset = 0;

        while (bytesLeft > 0) {
            int read = in.read(buffer, offset, bytesLeft);
            if (read == -1) {
                throw new IOException("Unexpected end of data");
            }

            offset += read;
            bytesLeft -= read;
        }

        return new String(buffer);
    }
}
