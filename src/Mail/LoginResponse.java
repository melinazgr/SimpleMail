package Mail;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Handles login response.
 *
 * @author Melina Zikou
 *
 */
public class LoginResponse extends Command {
    private String errorCode;

    final public static String COMMANDNAME = "RESPONSE:LOGIN";
    final public static String ERROR = "ERRORCODE:";
    final public static String END = "ENDRESPONSE";
    final public static String SUCCESS = "SUCCESS"; //user loged in ok
    final public static String FAIL = "FAIL"; // login failure

    public LoginResponse(){}

    public LoginResponse (String errorCode){
        this.errorCode = errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode(){
        return this.errorCode;
    }

    public String createPacket(){
        return createPacket(this.errorCode);
    }

    public Command.CommandType getType() {
        return Command.CommandType.LoginResponse;
    }

    public static String createPacket(String errorCode){
        String s = COMMANDNAME + "\n" +
                ERROR + errorCode + "\n" +
                END +  "\n";

        return s;
    }

    public void parsePacket(BufferedReader in) throws IOException {

        String line = (String)in.readLine();

        while(!line.startsWith(END)){
            if(line.startsWith(ERROR)){
                this.errorCode = line.substring(ERROR.length());
                System.out.println("Got errorcode:" + this.errorCode);
            }

            line = (String)in.readLine();
        }
    }
}
