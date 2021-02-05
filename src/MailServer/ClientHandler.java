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

                // register
                if(nextCommand.getType() == Command.CommandType.Register){
                    RegisterRequest user = (RegisterRequest)nextCommand;
                    System.out.println("Register user: " + user.getUsername());

                    RegisterResponse response = handleRegister(user);
                    out.print(response.createPacket());
                    out.flush();
                    System.out.println("response sent");
                }

                // log in
                else if(nextCommand.getType() == Command.CommandType.Login){
                    LoginRequest user = (LoginRequest) nextCommand;
                    System.out.println("Login user: " + user.getUsername());

                    LoginResponse response = handleLogin(user);
                    out.print(response.createPacket());
                    out.flush();
                    currUsername = user.getUsername();
                    isLogin = true;
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

    private LoginResponse handleLogin(LoginRequest req) {
        Account acc = AccountManager.getInstance().findAccount(req.getUsername());
        LoginResponse response = new LoginResponse();

        if(acc != null){
            if(acc.getPassword().equals(req.getPassword())){
                response.setErrorCode(LoginResponse.SUCCESS);
                System.out.println("User logged in");
            }
            else{
                response.setErrorCode(RegisterResponse.FAIL);
                System.out.println("Invalid Username or Password");
            }
        }
        else{
            response.setErrorCode(RegisterResponse.FAIL);
            System.out.println("User log in failed");
        }
        return response;
    }


    public RegisterResponse handleRegister (RegisterRequest req){
        Account acc = AccountManager.getInstance().findAccount(req.getUsername());
        RegisterResponse response = new RegisterResponse();

        if(acc == null){
            Account newUser = new Account(req.getUsername(), req.getPassword());
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