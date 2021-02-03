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
            out = new PrintStream(client.getOutputStream());
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            Command nextCommand = null;

            do {

                nextCommand = Command.parse(in);

                // requests
                if(nextCommand.getType() == Command.CommandType.Register){
                    RegisterRequest c = (RegisterRequest) nextCommand;
                    System.out.println("Register user: " + c.username);

                    RegisterResponse response = handleRegister(c);
                    out.print(response);
                    out.flush();
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

        catch(IOException e){
            System.out.println("IOException");
        }
    }


    public RegisterResponse handleRegister (RegisterRequest req){
        Account acc = AccountManager.getInstance().findAccount(req.username);
        RegisterResponse response = new RegisterResponse();

        if(acc == null){
            Account newUser = new Account(req.username, req.password);
            AccountManager.getInstance().addAccount(newUser);

            response.createPacket(RegisterResponse.SUCCESS);
        }
        else{
            response.createPacket(RegisterResponse.FAIL);
        }
        return response;
    }
}
