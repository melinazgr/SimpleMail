package Mail;

import java.io.BufferedReader;
import java.io.IOException;

public class DeleteEmailRequest extends Command {
    private String emailID;

    final public static String COMMANDNAME = "COMMAND:DELETE";
    final public static String USERNAME = "USERNAME:";
    final public static String PASSWORD = "PASSWORD:";
    final public static String EMAIL = "EMAILID:";
    final public static String END = "ENDCOMMAND";

    public DeleteEmailRequest(){
        this.emailID = emailID;
    }


    @Override
    public CommandType getType() {
        return null;
    }

    @Override
    public void parsePacket(BufferedReader in) throws IOException {

    }

    @Override
    public String createPacket(){

        return createPacket(this.emailID);
    }

    public static String createPacket(String emailID){
        String s = COMMANDNAME + "\n" +
                EMAIL + emailID + "\n" +
                END +  "\n";

        return s;
    }
}
