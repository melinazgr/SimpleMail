package MailServer;

import Mail.*;

import java.io.*;
import java.net.*;

/**
 * Server class that handles client requests
 *
 * @author Melina Zikou
 *
 */
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

                System.out.println("received " + nextCommand.getType());

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

                //logout
                else if(nextCommand.getType() == Command.CommandType.Logout){
                    System.out.println("Logout user: " + currUsername);
                    currUsername = null;
                }

                // new email
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

                // delete email
                else if(nextCommand.getType() == Command.CommandType.DeleteEmail){
                    DeleteEmailResponse response;
                    if(currUsername == null){
                        response = new DeleteEmailResponse(DeleteEmailResponse.FAIL, "User not logged in.");
                    }
                    else{
                        DeleteEmailRequest deleteEmailRequest = (DeleteEmailRequest) nextCommand;
                        response = handleDeleteEmail(deleteEmailRequest);
                    }
                    out.print(response.createPacket());
                    out.flush();
                }

                // show emails
                else if(nextCommand.getType() == Command.CommandType.ShowEmails){
                    ShowEmailsResponse response;
                    if(currUsername == null){
                        response = new ShowEmailsResponse(ShowEmailsResponse.FAIL, "User not logged in.");
                    }
                    else{
                        ShowEmailsRequest showEmailsRequest = (ShowEmailsRequest) nextCommand;
                        response = handleShowEmails(showEmailsRequest);
                    }

                    out.print(response.createPacket());
                    out.flush();
                }

                // read email
                else if(nextCommand.getType() == Command.CommandType.ReadEmail){
                    ReadEmailResponse response;
                    if(currUsername == null){
                        response = new ReadEmailResponse(ReadEmailResponse.FAIL, "User not logged in.");
                    }
                    else{
                        ReadEmailRequest readEmailRequest = (ReadEmailRequest) nextCommand;
                        response = handleReadEmail(readEmailRequest);
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

    /**
     * Creates response to the Read Email Request
     * @param req read email request
     * @return read email response
     */
    private ReadEmailResponse handleReadEmail(ReadEmailRequest req) {
        // find user with request username
        Account user = AccountManager.getInstance().findAccount(req.getUsername());
        ReadEmailResponse response = new ReadEmailResponse();

        if(user != null){
            // set response email
            response.setEmail(user.findEmail(req.getEmailID()));

            // set email as not new
            if(user.findEmail(req.getEmailID()).getIsNew()){
                user.findEmail(req.getEmailID()).setIsNew(false);
            }

            response.setErrorCode(ReadEmailResponse.SUCCESS);
        }
        else{
            response.setErrorCode(ReadEmailResponse.FAIL);
            response.setErrorMessage("Invalid Receiver Username");
        }

        return response;
    }

    /**
     * Creates response to the Show Emails Request
     * @param req show emails request
     * @return show emails response
     */
    private ShowEmailsResponse handleShowEmails(ShowEmailsRequest req) {
        // find user with request username
        Account user = AccountManager.getInstance().findAccount(req.getUsername());
        ShowEmailsResponse response = new ShowEmailsResponse();

        if(user != null){
            // set response mailbox
            response.setMailbox(user.getMailbox());
            response.setErrorCode(ShowEmailsResponse.SUCCESS);
            System.out.println("Show emails for user: " + req.getUsername() + " #" + user.getMailbox().size());
        }
        else{
            response.setErrorCode(ShowEmailsResponse.FAIL);
            response.setErrorMessage("Invalid Receiver Username");
        }

        return response;
    }

    /**
     * Creates response to the Delete Email Request
     * @param req delete email request
     * @return delete email response
     */
    private DeleteEmailResponse handleDeleteEmail(DeleteEmailRequest req) {
        // find user with request username
        Account user = AccountManager.getInstance().findAccount(req.getUsername());
        DeleteEmailResponse response = new DeleteEmailResponse();

        if(user != null){
            if(user.findEmail(req.getEmailID()) != null){
                // delete email
                user.deleteEmail(req.getEmailID());
                response.setErrorCode(DeleteEmailResponse.SUCCESS);
            }
            else{
                response.setErrorCode(DeleteEmailResponse.FAIL);
                response.setErrorMessage("Email does not exist.");
            }
        }
        else{
            response.setErrorCode(DeleteEmailResponse.FAIL);
            response.setErrorMessage("Invalid Receiver Username");
        }

        return response;
    }


    /**
     * Creates response to the New Email Request
     * @param req new email request
     * @return new email response
     */
    private NewEmailResponse handleNewEmail(NewEmailRequest req) {
        // find user - receiver with request username
        Account receiver = AccountManager.getInstance().findAccount(req.getReceiver());
        NewEmailResponse response = new NewEmailResponse();

        if(receiver != null){

            // set email information for the receiver
            Email email = new Email();
            email.setSender(req.getSender());
            email.setReceiver(receiver.getUsername());
            email.setSubject(req.getSubject());
            email.setMainbody(req.getMainbody());
            email.setIsNew(true);

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

    /**
     * Creates response to the Login Request
     * @param req login request
     * @return login response
     */
    private LoginResponse handleLogin(LoginRequest req) {
        // find user with request username
        Account acc = AccountManager.getInstance().findAccount(req.getUsername());
        LoginResponse response = new LoginResponse();

        // if the user is registered
        // login can happen
        if(acc != null){
            // check if password is correct
            if(acc.getPassword().equals(req.getPassword())){
                response.setErrorCode(LoginResponse.SUCCESS);
                acc.setLogin(true); // user logged in
                System.out.println("User logged in");
            }
            else{
                response.setErrorCode(RegisterResponse.FAIL);
                System.out.println("Invalid Userna me or Password");
            }
        }
        else{
            response.setErrorCode(RegisterResponse.FAIL);
            System.out.println("User log in failed");
        }

        return response;
    }

    /**
     * Creates response to the Register request
     * @param req register request
     * @return register response
     */
    public RegisterResponse handleRegister (RegisterRequest req){
        // find user with request username
        Account acc = AccountManager.getInstance().findAccount(req.getUsername());
        RegisterResponse response = new RegisterResponse();

        // if the account has not been previously created
        // register can happen
        if(acc == null){
            // create new user
            Account newUser = new Account(req.getUsername(), req.getPassword());

            // add user to the account manager
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
