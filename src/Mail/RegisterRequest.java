package Mail;

import java.io.BufferedReader;
import java.io.IOException;

public class RegisterRequest extends Mail.Command {

    public String username, password;

    final public static String COMMANDNAME = "COMMAND:REGISTER";
    final public static String USERNAME = "USERNAME:";
    final public static String PASSWORD = "PASSWORD:";
    final public static String END = "ENDCOMMAND";

    public RegisterRequest(){}

    public RegisterRequest(String username, String password){
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
        return CommandType.Register;

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