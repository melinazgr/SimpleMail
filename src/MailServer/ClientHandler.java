package MailServer;

import Mail.*;

import java.io.*;
import java.net.*;

public class ClientHandler extends Thread{
    private Socket client;
    public String currUsername = null;
    public static Boolean isLogin = false;

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
                }

                else if(nextCommand.getType() == Command.CommandType.Logout){
                    System.out.println("Logout user: " + currUsername);
                    currUsername = null;
                }

                else if(nextCommand.getType() == Command.CommandType.NewEmail){
                    NewEmailResponse response;
                    if(currUsername == null){
                        response = new NewEmailResponse(NewEmailResponse.FAIL, "User not logged in.");
                    }
                    else{
                        NewEmailRequest newEmailRequest = (NewEmailRequest) nextCommand;
                        response = handleNewEmail(newEmailRequest);
                    }
                    out.print(response.createPacket());
                    out.flush();
                }
            }
            while (nextCommand.getType() != Command.CommandType.Logout);
        }
        catch(IOException | InterruptedException e){
            System.out.println("IOException");
        }
    }

    private NewEmailResponse handleNewEmail(NewEmailRequest req) {
        Account receiver = AccountManager.getInstance().findAccount(req.getReceiver());
        NewEmailResponse response = new NewEmailResponse();

        if(receiver != null){

            Email email = new Email();
            email.setSender(req.getSender());
            email.setReceiver(receiver.getUsername());
            email.setSubject(req.getSubject());
            email.setMainbody(req.getMainbody());
            email.setNew(true);

            receiver.addEmail(email);
            response.setErrorCode(NewEmailResponse.SUCCESS);

            System.out.println("Email sent from: " + email.getSender() + " to: " + email.getReceiver() + " ID: " + email.getId());
        }
        else{
            response.setErrorCode(NewEmailResponse.FAIL);
            response.setErrorMessage("Invalid Receiver Username");
        }
        return response;
    }

    private LoginResponse handleLogin(LoginRequest req) {
        Account acc = AccountManager.getInstance().findAccount(req.getUsername());
        LoginResponse response = new LoginResponse();

        if(acc != null){
            if(acc.getPassword().equals(req.getPassword())){
                response.setErrorCode(LoginResponse.SUCCESS);
                acc.setLogin(true);
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