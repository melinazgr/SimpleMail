package Mail;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * handles login request.
 *
 * @author Melina Zikou
 *
 */
public class LoginRequest extends Command{
    final public static String COMMANDNAME = "COMMAND:LOGIN";
    final public static String USERNAME = "USERNAME:";
    final public static String PASSWORD = "PASSWORD:";
    final public static String END = "ENDCOMMAND";

    private String username, password;

    public LoginRequest(){

    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LoginRequest(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String createPacket(){
        return createPacket(this.username, this.password);
    }

    @Override
    public CommandType getType() {
        return CommandType.Login;
    }

    public static String createPacket(String username, String password){
        String s = COMMANDNAME + "\n" +
                USERNAME + username + "\n" +
                PASSWORD + password + "\n" +
                END +  "\n";

        return s;
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
