package MailServer;

import Mail.*;

import java.io.*;
import java.net.*;

public class ClientHandler extends Thread{
    private Socket client;
    String currUsername = null;
    Boolean isLogin = false;

    public ClientHandler(Socket client){
        this.client = client;
    }

    public void run(){

        PrintStream out = null;
        BufferedReader in = null;
        String message = "";

        try{
            // input output

            OutputStream os = client.getOutputStream();
            out = new PrintStream(os);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            Command nextCommand = null;
            do {
                nextCommand = Command.parse(in);

                if(nextCommand == null){
                    break;
                }

                // requests
                if(nextCommand.getType() == Command.CommandType.Register){
                    RegisterRequest c = (RegisterRequest)nextCommand;
                    System.out.println("Register user: " + c.username);

                    RegisterResponse response = handleRegister(c);
                    out.print(response.createPacket());
                    out.flush();
                    System.out.println("response sent");
                }

                else if(nextCommand.getType() == Command.CommandType.Login){
                    LoginRequest c = (LoginRequest) nextCommand;
                    System.out.println("Login user: " + c.username);
                    currUsername = c.username;
                    isLogin = true;
                    //todo validate user
                }

                if(nextCommand.getType() == Command.CommandType.Logout){
                    System.out.println("Logout user: " + currUsername);
                }
            }
            while (nextCommand.getType() != Command.CommandType.Logout);
        }
        catch(IOException | InterruptedException e){
            System.out.println("IOException");
        }
    }


    public RegisterResponse handleRegister (RegisterRequest req){
        Account acc = AccountManager.getInstance().findAccount(req.username);
        RegisterResponse response = new RegisterResponse();

        if(acc == null){
            Account newUser = new Account(req.username, req.password);
            AccountManager.getInstance().addAccount(newUser);

            response.setErrorCode(RegisterResponse.SUCCESS);
            System.out.println("User created");

        }
        else{
            response.setErrorCode(RegisterResponse.FAIL);
            System.out.println("User create failed");
        }
        return response;
    }
}
