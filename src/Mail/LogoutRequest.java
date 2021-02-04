package Mail;

import java.io.BufferedReader;
import java.io.IOException;

public class LogoutRequest extends Command{
    final public static String COMMANDNAME = "COMMAND:LOGOUT";
    final public static String END = "ENDCOMMAND";

    public LogoutRequest(){

    }

    public String createPacket(){
        String s = COMMANDNAME + "\n" +
                END +  "\n";
        return s;
    }

    @Override
    public CommandType getType() {
        return CommandType.Logout;
    }

    public void parsePacket(BufferedReader in) throws IOException {

        String line = (String)in.readLine();


        while(!line.startsWith(END)){

            line = (String)in.readLine();
        }

    }
}
