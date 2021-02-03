package Mail;

import java.io.BufferedReader;
import java.io.IOException;

public class RegisterResponse extends Mail.Command {

    private String errorCode;

    final public static String COMMANDNAME = "RESPONSE:REGISTER";
    final public static String END = "ENDRESPONSE";
    final public static String ERROR = "ERRORCODE:";

    final public static String SUCCESS = "SUCCESS"; //user created ok
    final public static String FAIL = "FAIL"; // user exists / registration failure

    public RegisterResponse(){}

    public RegisterResponse (String errorCode){
        this.errorCode = errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode(){
        return this.errorCode;
    }

    @Override
    public String createPacket(){
        return createPacket(this.errorCode);
    }

    public static String createPacket(String errorCode){
        String s = COMMANDNAME + "\n" +
                    ERROR + errorCode + "\n" +
                    END +  "\n";

        return s;
    }

    @Override
    public CommandType getType() {
        return CommandType.RegisterResponse;
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
