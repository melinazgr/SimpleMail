package Mail;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * handles logout request
 *
 * @author Melina Zikou
 *
 */
public class LogoutRequest extends Command{
    final public static String COMMANDNAME = "COMMAND:LOGOUT";
    final public static String END = "ENDCOMMAND";

    public LogoutRequest(){

    }

    @Override
    public CommandType getType() {
        return CommandType.Logout;
    }

    public String createPacket(){
        String s = COMMANDNAME + "\n" +
                    END +  "\n";
        return s;
    }

    public void parsePacket(BufferedReader in) throws IOException {

        String line = (String)in.readLine();


        while(!line.startsWith(END)){

            line = (String)in.readLine();
        }
    }
}
