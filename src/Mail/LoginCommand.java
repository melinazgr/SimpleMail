package Mail;

import java.io.BufferedReader;
import java.io.IOException;

public class LoginCommand extends Command{
    final public static String COMMANDNAME = "COMMAND:LOGIN";
    final public static String USERNAME = "USERNAME:";
    final public static String PASSWORD = "PASSWORD:";
    final public static String END = "ENDCOMMAND";

    public String username, password;


    public LoginCommand(){

    }

    public LoginCommand(String username, String password){
        this.username = username;
        this.password = password;
    }

    //TODO get set pwd username

    public String createPacket(){

        return createPacket(this.username, this.password);
    }

    public static String createPacket(String username, String password){
        String s = COMMANDNAME + "\n" +
                USERNAME + username + "\n" +
                PASSWORD + password + "\n" +
                END +  "\n";

        return s;
    }


    @Override
    public CommandType getType() {
        return CommandType.Login;
    }

    public void parsePacket(BufferedReader in) throws IOException {

        String line = (String)in.readLine();


        while(!line.startsWith(END)){
            if(line.startsWith(USERNAME)){
                this.username = line.substring(USERNAME.length());
            }
            else if(line.startsWith(PASSWORD)){
                this.password = line.substring(PASSWORD.length());
            }

            line = (String)in.readLine();
        }

    }
}
