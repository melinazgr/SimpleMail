package Mail;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * handles Show Emails request
 *
 * @author Melina Zikou
 *
 */
public class ShowEmailsRequest extends Command {
    private String username;

    final public static String COMMANDNAME = "COMMAND:SHOW";
    final public static String USERNAME = "USERNAME:";
    final public static String END = "ENDCOMMAND";

    public ShowEmailsRequest(){}

    public ShowEmailsRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public CommandType getType() {
        return CommandType.ShowEmails;
    }

    @Override
    public void parsePacket(BufferedReader in) throws IOException {
        String line = (String)in.readLine();

        while(!line.startsWith(END)){
            if(line.startsWith(USERNAME)){
                this.username = line.substring(USERNAME.length());
            }

            line = (String)in.readLine();
        }
    }

    @Override
    public String createPacket() {
        String s = COMMANDNAME + "\n" +
                USERNAME + username + "\n" +
                END +  "\n";

        return s;
    }
}
