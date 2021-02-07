package Mail;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * handles delete email request
 *
 * @author Melina Zikou
 *
 */
public class DeleteEmailRequest extends Command {
    private String emailID, username;

    final public static String COMMANDNAME = "COMMAND:DELETE";
    final public static String USERNAME = "USERNAME:";
    final public static String EMAIL = "EMAILID:";
    final public static String END = "ENDCOMMAND";

    public DeleteEmailRequest(){}

    public DeleteEmailRequest(String emailID, String username){
        this.emailID = emailID;
        this.username = username;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public CommandType getType() {
        return CommandType.DeleteEmail;
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
