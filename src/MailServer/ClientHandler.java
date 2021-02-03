package MailServer;

import Mail.Command;
import Mail.LoginCommand;
import Mail.RegisterCommand;

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

                if(nextCommand.getType() == Command.CommandType.Register){
                    RegisterCommand c = (RegisterCommand) nextCommand;
                    System.out.println("Register user: " + c.username);
                }

                else if(nextCommand.getType() == Command.CommandType.Login){
                    LoginCommand c = (LoginCommand) nextCommand;
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
}
