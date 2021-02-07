package Mail;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * handles Read Email request
 *
 * @author Melina Zikou
 *
 */
public class ReadEmailRequest extends Command {
    private String username, emailID;

    final public static String COMMANDNAME = "COMMAND:READ";
    final public static String USERNAME = "USERNAME:";
    final public static String EMAIL = "EMAILID:";
    final public static String END = "ENDCOMMAND";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    @Override
    public CommandType getType() {
        return CommandType.ReadEmail;
    }

    @Override
    public void parsePacket(BufferedReader in) throws IOException {
        String line = (String)in.readLine();

        while(!line.startsWith(END)){
            if(line.startsWith(USERNAME)){
                this.username = line.substring(USERNAME.length());
            }
            else if(line.startsWith(EMAIL)){
                this.emailID = line.substring(EMAIL.length());
            }

            line = (String)in.readLine();
        }
    }

    @Override
    public String createPacket() {
        String s = COMMANDNAME + "\n" +
                USERNAME + username + "\n" +
                EMAIL + emailID + "\n" +
                END +  "\n";

        return s;
    }
}
