package MailClient;

import Mail.*;
import MailServer.ClientHandler;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Main {
    private static String currUsername;

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

        while(!message.equalsIgnoreCase("exit")){
            //todo main menu


            showMenu(state);

            message = scanner.next();

            if(state == 1){
                if(message.equalsIgnoreCase("signup")){
                    registerOption(out, in, scanner);
                }
                else if(message.equalsIgnoreCase("login")){
                    if(doLogin(out, in, scanner)){
                        state = 2;
                    }
                }
            }
            else if(state == 2){
                if(message.equalsIgnoreCase("newemail")){
                    newEmailOption(out, in, scanner);
                }
                if(message.equalsIgnoreCase("logout")){
                    // todo logout
                    state = 1;
                }
            }
        }

        out.close();
        in.close();
    }

    public static void showMenu(int state){
        if(state == 1){
            System.out.println("==========\n" +
                    "> LogIn\n" +
                    "> SignUp\n" +
                    "> Exit\n" +
                    "==========");
        }
        else{
            System.out.println("===============\n" +
                    "> NewEmail\n" +
                    "> ShowEmails\n" +
                    "> ReadEmail\n" +
                    "> DeleteEmail\n" +
                    "> LogOut\n" +
                    "> Exit\n" +
                    "===============");
        }
    }
    private static void newEmailOption(PrintWriter out, BufferedReader in, Scanner scanner) throws IOException, InterruptedException {
        System.out.println("----------\n" +
                            "Receiver:\n");
        String receiver = scanner.next();

        System.out.println("----------\n" +
                "Subject:\n");
        String subject = scanner.next();

        System.out.println("----------\n" +
                "Main Body:\n");
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
                    System.out.println("----------\n" +
                            "Email sent\n" );
                }
                else{
                    System.out.println("----------\n" +
                            "Email sent Failed\n");
                }
            }
            else{
                System.out.println("----------\n" +
                        "Unexpected Response.\n");
            }
        }


    private static boolean doLogin(PrintWriter out, BufferedReader in, Scanner scanner) throws IOException, InterruptedException {
        System.out.println("----------\n" +
                            "Type your username:\n" );
        String username = scanner.next();

        System.out.println("----------\n" +
                            "Type your password:\n");
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
                    System.out.println("----------\n" +
                                        "Welcome back " + username);
                    currUsername = username;
                    return true;
                }
                else{
                    System.out.println("----------\n" +
                                        "User Login Failed\n");
                }
            }
            else{
                System.out.println("----------\n" +
                                    "Unexpected Response.\n");
            }
        }

        return false;
    }

    private static void registerOption(PrintWriter out, BufferedReader in, Scanner scanner) throws IOException, InterruptedException {
        System.out.println("----------\n" +
                            "Type your username:\n");
        String username = scanner.next();

        System.out.println("----------\n" +
                            "Type your password:\n");
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
                    System.out.println("----------\n" +
                                        "User created Successfully\n" );
                }
                else{
                    System.out.println("----------\n" +
                                        "User creation Failed\n");
                }
            }
            else{
                System.out.println("----------\n" +
                                    "Unexpected Response.\n");
            }
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

        // todo ------ var
        System.out.println("----------\n" +
                            "Invalid Username\n");
        return false;
    }
}