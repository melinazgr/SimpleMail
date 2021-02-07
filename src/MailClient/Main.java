package MailClient;

import Mail.*;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * Client CLass
 *
 * @author Melina Zikou
 *
 */
public class Main {
    // username of the current user
    private static String currUsername;

    // constant for printing format in the cmd
    final public static String SEPARATOR = "----------\n";

    /**
     * method main to start the program
     * @param argv arguments from System.in
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(String[] argv) throws IOException, InterruptedException {
        Socket s = new Socket();
        String host = "127.0.0.1"; // default IP address
        int port = 5000; // default port


        String helpText = "Usage: java Main [-address <IP address>]\n" +
                        "                 [-port]\n";

        InetAddress localAddress = null;
        InetAddress remoteAddress = null;

        try {
            boolean addressSet = false;
            boolean portSet = false;

            // creation of argument options
            // if not set, the default values are used

            for (int i = 0; i < argv.length; i++) {

                // -address argument / used for the IP address
                if (argv[i].equals("-address")) {
                    i++;
                    if (i >= argv.length) {
                        throw new Exception("Missing address");
                    }
                    host = argv[i];
                    addressSet = true;

                }
                // -port argument / used for the port
                else if (argv[i].equals("-port")) {
                    i++;
                    if (i >= argv.length) {
                        throw new Exception("Missing port");
                    }
                    port = Integer.parseInt(argv[i]);

                    portSet = true;

                }
                // -help argument / used for showing the available arguments
                else if (argv[i].equals("-help")) {
                    System.out.println(helpText);
                    System.exit(1);
                } else{
                    throw new Exception(helpText);
                }
            }

        } catch (Exception e) {
            System.out.println(helpText);
            System.exit(1);
        }

        try {
            // initializing the connection
            s.connect(new InetSocketAddress(host, port));
            localAddress = s.getLocalAddress();
            remoteAddress = s.getInetAddress();

        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + host);
            System.exit(1);
        }

        // information about the connection
        System.out.println("Connected");
        System.out.println("local: " + localAddress + " : " + s.getLocalPort());
        System.out.println("remote: " + remoteAddress);

        handleUserMenuInput(s);

        s.close();
    }

    /**
     * Handles User Input based on the Menu format
     * @param s socket
     * @throws IOException
     * @throws InterruptedException
     */
    private static void handleUserMenuInput(Socket s) throws IOException, InterruptedException {
        boolean isLogin = false;
        int state = 1;

        PrintWriter out = new PrintWriter(s.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

        Scanner scanner = new Scanner(System.in);
        String message = "";

        // if the user types exit or chooses the corresponding numbers
        // the client connection ends
        while(!message.equalsIgnoreCase("exit") && !(message.equals("6") && state == 2) && !(message.equals("3") && state == 1)){

            // state 1 = main menu
            // state 2 = user menu
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
                    state = 1;
                }
            }
        }

        out.close();
        in.close();
    }

    /**
     * Read Email option selected
     * @param out
     * @param in
     * @param scanner
     * @throws IOException
     * @throws InterruptedException
     */
    private static void readEmailOption(PrintWriter out, BufferedReader in, Scanner scanner) throws IOException, InterruptedException {
        // email ID of the email to be read
        System.out.println(SEPARATOR + "Email ID: ");
        String emailID = scanner.next();

        // initialize request packet
        ReadEmailRequest req = new ReadEmailRequest();

        // set the information for the request
        req.setUsername(currUsername);
        req.setEmailID(emailID);

        // create and send request packet
        out.print(req.createPacket());
        out.flush();

        // parse response packet from the server
        Command nextCommand = Command.parse(in);

        if(nextCommand.getType() == Command.CommandType.ReadEmailResponse){
            ReadEmailResponse res = (ReadEmailResponse) nextCommand;

            // if the email is read successfully
            // print out the information needed
            if(res.getErrorCode().equals(ReadEmailResponse.SUCCESS)){

                StringBuilder sb = new StringBuilder();

                sb.append(SEPARATOR);
                sb.append("ID: " + res.getEmail().getId());
                sb.append("\nFROM: " + res.getEmail().getSender());
                sb.append("\nSUBJECT: " + res.getEmail().getSubject() + "\n");
                sb.append(SEPARATOR);
                sb.append("\nMAIN BODY\n" + res.getEmail().getMainbody() + "\n");

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

    /**
     * Delete Email option selected
     * @param out
     * @param in
     * @param scanner
     * @throws IOException
     * @throws InterruptedException
     */
    private static void deleteEmailOption(PrintWriter out, BufferedReader in, Scanner scanner) throws IOException, InterruptedException {
        // email ID of the email to be deleted
        System.out.println(SEPARATOR + "Email ID: ");
        String emailID = scanner.next();

        // initialize request packet
        DeleteEmailRequest req = new DeleteEmailRequest();

        req.setUsername(currUsername);
        req.setEmailID(emailID);

        // create and send request packet
        out.print(req.createPacket());
        out.flush();

        // parse response packet from the server
        Command nextCommand = Command.parse(in);

        // if the email is deleted successfully
        // print out message
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

    /**
     * Show Emails option selected
     * @param out
     * @param in
     * @param scanner
     * @throws IOException
     * @throws InterruptedException
     */
    private static void showEmailsOption(PrintWriter out, BufferedReader in, Scanner scanner) throws IOException, InterruptedException {
        // initialize request packet
        ShowEmailsRequest req = new ShowEmailsRequest();

        req.setUsername(currUsername);

        // create and send request packet
        out.print(req.createPacket());
        out.flush();

        // parse response packet from the server
        Command nextCommand = Command.parse(in);

        System.out.println(nextCommand.getType());


        if(nextCommand.getType() == Command.CommandType.ShowEmailsResponse){
            ShowEmailsResponse res = (ShowEmailsResponse) nextCommand;

            System.out.println("User: " + currUsername + " emails: " + res.getMailbox().size());

            // if emails can be shown successfully
            // print out the emails stored in the user mailbox
            if(res.getErrorCode().equals(ShowEmailsResponse.SUCCESS)){

                StringBuilder sb = new StringBuilder();
                String s;

                // format specifiers to align emails
                sb.append("ID %8s FROM %10d% SUBJECT\n");
                System.out.printf("%-10s%-15s%-15s" , "ID" , "FROM", "SUBJECT");
                System.out.println();

                for(Email e : res.getMailbox()){

                    if(e.getIsNew()){
                        System.out.printf("%-10s" , e.getId() + ". [NEW]");
                    }
                    else{
                        System.out.printf("%-10s" , e.getId() , ".");
                    }

                    System.out.printf("%-15s" , e.getSender());
                    System.out.printf("%-15s" , e.getSubject());
                    System.out.println();
                }
            }
            else{
                System.out.println(SEPARATOR + "Show emails Failed");
            }
        }
        else{
            System.out.println(SEPARATOR + "Unexpected Response.");
        }
    }

    /**
     * New Email option selected
     * @param out
     * @param in
     * @param scanner
     * @throws IOException
     * @throws InterruptedException
     */
    private static void newEmailOption(PrintWriter out, BufferedReader in, Scanner scanner) throws IOException, InterruptedException {
        // information for the new email
        scanner.nextLine();

        System.out.println(SEPARATOR + "Receiver: ");
        String receiver = scanner.nextLine();

        System.out.println(SEPARATOR + "Subject: ");
        String subject = scanner.nextLine();

        System.out.println(SEPARATOR + "Main Body:");

        // read multiple lines from System.in
        // when an empty line is given, the scanner terminates
        String mainbody = "";
        String line  = scanner.nextLine();

        while(mainbody.equals("") || !line.equals("")){
            mainbody += line + "\n";
            line  = scanner.nextLine();
        }

        // initialize request packet
        NewEmailRequest req = new NewEmailRequest();

        // set the information for the request
        req.setSender(currUsername);
        req.setReceiver(receiver);
        req.setSubject(subject);
        req.setMainbody(mainbody);

        // create and send request packet
        out.print(req.createPacket());
        out.flush();

        // parse response packet from the server
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

    /**
     * Login option selected
     * @param out
     * @param in
     * @param scanner
     * @return true if the user login is successful
     *         false otherwise
     * @throws IOException
     * @throws InterruptedException
     */
    private static boolean doLogin(PrintWriter out, BufferedReader in, Scanner scanner) throws IOException, InterruptedException {
        // Username and password of the user to be logged in
        System.out.println(SEPARATOR + "Type your username:" );
        String username = scanner.next();

        System.out.println(SEPARATOR + "Type your password:");
        String password = scanner.next();

        // initialize request packet
        LoginRequest req = new LoginRequest();

        // set the information for the request
        req.setUsername(username);
        req.setPassword(password);

        // create and send request packet
        out.print(req.createPacket());
        out.flush();

        // parse response packet from the server
        Command nextCommand = Command.parse(in);

        if(nextCommand.getType() == Command.CommandType.LoginResponse){
            LoginResponse res = (LoginResponse) nextCommand;

            if(res.getErrorCode().equals(LoginResponse.SUCCESS)){
                System.out.println(SEPARATOR + "You are connected as " + username);
                currUsername = username;
                return true;
            }
            else{System.out.println(SEPARATOR + "User Login Failed");}
        }
        else{System.out.println(SEPARATOR + "Unexpected Response.");}

        return false;
    }

    /**
     * Register (Signup) Option selected
     * @param out
     * @param in
     * @param scanner
     * @throws IOException
     * @throws InterruptedException
     */
    private static void registerOption(PrintWriter out, BufferedReader in, Scanner scanner) throws IOException, InterruptedException {
        // Username and password of the user to be registered
        System.out.println(SEPARATOR + "Type your username:");
        String username = scanner.next();

        System.out.println(SEPARATOR + "Type your password");
        String password = scanner.next();

        // validate username ([A-Z]* @ [A-Z]*.[A-Z]*)
        if(validateUser(username)){
            // initialize request packet
            RegisterRequest req = new RegisterRequest();

            // set the information for the request
            req.setUsername(username);
            req.setPassword(password);

            // create and send request packet
            out.print(req.createPacket());
            out.flush();

            // parse response packet from the server
            Command nextCommand = Command.parse(in);

            if(nextCommand.getType() == Command.CommandType.RegisterResponse){
                RegisterResponse res = (RegisterResponse) nextCommand;

                if(res.getErrorCode().equals(RegisterResponse.SUCCESS)){
                    System.out.println(SEPARATOR + "User created Successfully" );
                }
                else{
                    System.out.println(SEPARATOR + "User creation Failed");
                }
            }
            else{
                System.out.println(SEPARATOR + "Unexpected Response.");
            }
        }
    }

    /**
     * Shows menu 1 if the user is not logged in
     *            2 otherwise
     * @param state
     */
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

    /**
     * makes sure the username is of the correct form
     * @param username username of the user
     * @return true if correct
     *         false otherwise
     */
    private static boolean validateUser(String username) {

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

// todo add users
// todo subject correct parsing
// todo comments