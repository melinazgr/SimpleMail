package MailClient;

import Mail.*;
import MailServer.ClientHandler;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Main {
    private static String currUsername;
    final public static String SEPARATOR = "----------\n";


    public static void main(String[] argv) throws IOException, InterruptedException {
        Socket s = new Socket();
        String host = "127.0.0.1";
        int port = 5000;



        String helpText = "Usage: java Main [-address <IP address>]\n" +
                        "                 [-port]\n";

        InetAddress localAddress = null;
        InetAddress remoteAddress = null;

        try {
            boolean addressSet = false;
            boolean portSet = false;

            for (int i = 0; i < argv.length; i++) {
                if (argv[i].equals("-address")) {
                    i++;
                    if (i >= argv.length) {
                        throw new Exception("Missing address");
                    }
                    host = argv[i];
                    addressSet = true;

                } else if (argv[i].equals("-port")) {
                    i++;
                    if (i >= argv.length) {
                        throw new Exception("Missing port");
                    }
                    port = Integer.parseInt(argv[i]);

                    portSet = true;

                } else if (argv[i].equals("-help")) {
                    System.out.println(helpText);
                    System.exit(1);
                }else {
                    throw new Exception(helpText);
                }
            }

        } catch (Exception e) {
            System.out.println(helpText);
            System.exit(1);
        }

        try {
            s.connect(new InetSocketAddress(host, port));
            localAddress = s.getLocalAddress();
            remoteAddress = s.getInetAddress();


        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + host);
            System.exit(1);

        }

        System.out.println("Connected");
        System.out.println("local: " + localAddress + " : " + s.getLocalPort());
        System.out.println("remote: " + remoteAddress);

        handleUserMenuInput(s);

        s.close();
    }

    private static void handleUserMenuInput(Socket s) throws IOException, InterruptedException {
        boolean isLogin = false;
        int state = 1;

        PrintWriter out = new PrintWriter(s.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

        Scanner scanner = new Scanner(System.in);
        String message = "";

        while(!message.equalsIgnoreCase("exit") && !(message.equals("6") && state == 2) && !(message.equals("3") && state == 1)){
            //todo main menu


            showMenu(state);

            message = scanner.next();

            if(state == 1){
                if(message.equalsIgnoreCase("signup") || message.equals("2")){
                    registerOption(out, in, scanner);
                }
                else if(message.equalsIgnoreCase("login") || message.equals("1")){
                    if(doLogin(out, in, scanner)){
                        state = 2;
                    }
                }
            }
            else if(state == 2){
                if(message.equalsIgnoreCase("newemail") || message.equals("1")){
                    newEmailOption(out, in, scanner);
                }
                else if(message.equalsIgnoreCase("showemails") || message.equals("2")){
                    showEmailsOption(out, in, scanner);
                }
                else if(message.equalsIgnoreCase("reademail") || message.equals("3")){
                    readEmailOption(out, in, scanner);
                }
                else if(message.equalsIgnoreCase("deleteemail") || message.equals("4")){
                    deleteEmailOption(out, in, scanner);
                }
                else if(message.equalsIgnoreCase("logout") || message.equals("5")){
                    // todo logout
                    state = 1;
                }
            }
        }

        out.close();
        in.close();
    }

    private static void readEmailOption(PrintWriter out, BufferedReader in, Scanner scanner) throws IOException, InterruptedException {
        ReadEmailRequest req = new ReadEmailRequest();
        System.out.println(SEPARATOR + "Email ID: ");
        String emailID = scanner.next();

        req.setUsername(currUsername);
        req.setEmailID(emailID);

        out.print(req.createPacket());

        out.flush();

        Command nextCommand = Command.parse(in);


        if(nextCommand.getType() == Command.CommandType.ReadEmailResponse){
            ReadEmailResponse res = (ReadEmailResponse) nextCommand;

            if(res.getErrorCode().equals(ReadEmailResponse.SUCCESS)){

                StringBuilder sb = new StringBuilder();

                sb.append("EMAIL ID: " + res.getEmail().getId());
                sb.append("\nFROM: " + res.getEmail().getSender());
                sb.append("\nSUBJECT: " + res.getEmail().getSubject());
                sb.append("\n\n: " + res.getEmail().getMainbody());

                System.out.println(sb.toString());
            }
            else{
                System.out.println(SEPARATOR + "Read email Failed");
            }
        }
        else{
            System.out.println(SEPARATOR + "Unexpected Response.");
        }
    }

    private static void deleteEmailOption(PrintWriter out, BufferedReader in, Scanner scanner) throws IOException, InterruptedException {
        System.out.println(SEPARATOR + "Email ID: ");
        String emailID = scanner.next();

        DeleteEmailRequest req = new DeleteEmailRequest();

        req.setUsername(currUsername);
        req.setEmailID(emailID);

        out.print(req.createPacket());
        out.flush();

        Command nextCommand = Command.parse(in);

        if(nextCommand.getType() == Command.CommandType.DeleteEmailResponse){
            DeleteEmailResponse res = (DeleteEmailResponse) nextCommand;

            if(res.getErrorCode().equals(DeleteEmailResponse.SUCCESS)){
                System.out.println(SEPARATOR +
                        "Email " + req.getEmailID() + " deleted!" );
            }
            else{
                System.out.println(SEPARATOR +
                        "Email Deletion Failed.");
            }
        }
        else{
            System.out.println(SEPARATOR +
                    "Unexpected Response.");
        }
    }


    private static void showEmailsOption(PrintWriter out, BufferedReader in, Scanner scanner) throws IOException, InterruptedException {
        ShowEmailsRequest req = new ShowEmailsRequest();

        req.setUsername(currUsername);

        out.print(req.createPacket());
        out.flush();

        Command nextCommand = Command.parse(in);

        System.out.println(nextCommand.getType());

        if(nextCommand.getType() == Command.CommandType.ShowEmailsResponse){
            ShowEmailsResponse res = (ShowEmailsResponse) nextCommand;

            System.out.println("User: " + currUsername + " emails: " + res.getMailbox().size());

            if(res.getErrorCode().equals(ShowEmailsResponse.SUCCESS)){

                StringBuilder sb = new StringBuilder();

                //todo tabs

                sb.append("ID        FROM                  SUBJECT\n");
                for(Email e : res.getMailbox()){
                    sb.append(e.getId());
                    sb.append(". ");

                    if(e.getIsNew()){
                        sb.append("[NEW]  ");
                    }
                    else{
                        sb.append("       ");
                    }

                    sb.append(e.getSender());
                    sb.append(e.getSubject());
                    sb.append("\n");
                }

                System.out.println(sb.toString());
            }
            else{
                System.out.println(SEPARATOR + "Show emails Failed");
            }
        }
        else{
            System.out.println(SEPARATOR + "Unexpected Response.");
        }
    }

    private static void newEmailOption(PrintWriter out, BufferedReader in, Scanner scanner) throws IOException, InterruptedException {
        System.out.println(SEPARATOR + "Receiver: ");
        String receiver = scanner.next();

        System.out.println(SEPARATOR + "Subject: ");
        String subject = scanner.next();

        System.out.println(SEPARATOR + "Main Body:");
        // todo read multiple line + sysout

        String mainbody = scanner.next();

        NewEmailRequest req = new NewEmailRequest();

        req.setSender(currUsername);
        req.setReceiver(receiver);
        req.setSubject(subject);
        req.setMainbody(mainbody);

        out.print(req.createPacket());
        out.flush();


        Command nextCommand = Command.parse(in);

        if(nextCommand.getType() == Command.CommandType.NewEmailResponse){
            NewEmailResponse res = (NewEmailResponse) nextCommand;

            if(res.getErrorCode().equals(NewEmailResponse.SUCCESS)){
                System.out.println(SEPARATOR +
                        "Email sent!" );
            }
            else{
                System.out.println(SEPARATOR +
                        "Email Send Failed.");
            }
        }
        else{
            System.out.println(SEPARATOR +
                    "Unexpected Response.");
        }
    }


    private static boolean doLogin(PrintWriter out, BufferedReader in, Scanner scanner) throws IOException, InterruptedException {
        System.out.println(SEPARATOR +
                            "Type your username:" );
        String username = scanner.next();

        System.out.println(SEPARATOR +
                            "Type your password:");
        String password = scanner.next();

        if(validateUser(username, password)){
            LoginRequest req = new LoginRequest();

            req.setUsername(username);
            req.setPassword(password);

            out.print(req.createPacket());
            out.flush();

            Command nextCommand = Command.parse(in);

            if(nextCommand.getType() == Command.CommandType.LoginResponse){
                LoginResponse res = (LoginResponse) nextCommand;

                if(res.getErrorCode().equals(LoginResponse.SUCCESS)){
                    System.out.println(SEPARATOR +
                                        "You are connected as " + username);
                    currUsername = username;
                    return true;
                }
                else{
                    System.out.println(SEPARATOR +
                                        "User Login Failed");
                }
            }
            else{
                System.out.println(SEPARATOR +
                                    "Unexpected Response.");
            }
        }

        return false;
    }

    private static void registerOption(PrintWriter out, BufferedReader in, Scanner scanner) throws IOException, InterruptedException {
        System.out.println(SEPARATOR +
                            "Type your username:");
        String username = scanner.next();

        System.out.println(SEPARATOR +
                            "Type your password");
        String password = scanner.next();

        if(validateUser(username, password)){
            RegisterRequest req = new RegisterRequest();

            req.setUsername(username);
            req.setPassword(password);

            out.print(req.createPacket());
            out.flush();

            Command nextCommand = Command.parse(in);

            if(nextCommand.getType() == Command.CommandType.RegisterResponse){
                RegisterResponse res = (RegisterResponse) nextCommand;

                if(res.getErrorCode().equals(RegisterResponse.SUCCESS)){
                    System.out.println(SEPARATOR +
                                        "User created Successfully" );
                }
                else{
                    System.out.println(SEPARATOR +
                                        "User creation Failed");
                }
            }
            else{
                System.out.println(SEPARATOR +
                                    "Unexpected Response.");
            }
        }
    }


    public static void showMenu(int state){
        if(state == 1){
            System.out.println("==========\n" +
                    "1. LogIn\n" +
                    "2. SignUp\n" +
                    "3. Exit\n" +
                    "==========");
        }
        else{
            System.out.println("===============\n" +
                    "1. NewEmail\n" +
                    "2. ShowEmails\n" +
                    "3. ReadEmail\n" +
                    "4. DeleteEmail\n" +
                    "5. LogOut\n" +
                    "6. Exit\n" +
                    "===============");
        }
    }

    private static boolean validateUser(String username, String password) {

        if(username.contains("@")){
            String domain = username.substring(username.indexOf("@"));
            if(domain.contains(".")){
                return true;
                // todo correct domain
            }
        }

        System.out.println(SEPARATOR + "Invalid Username");
        return false;
    }
}