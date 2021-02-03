package MailClient;

import Mail.Command;
import Mail.LogoutRequest;
import Mail.RegisterRequest;
import Mail.RegisterResponse;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Main {
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
        PrintWriter out = new PrintWriter(s.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

        Scanner scanner = new Scanner(System.in);
        String message = "";

        System.out.println("before menu");

        while(!message.equalsIgnoreCase("exit")){
            //todo main menu

            System.out.println("MAIN MENU: SIGNUP / LOGIN / EXIT");
            message = scanner.next();

            if(message.equalsIgnoreCase("signup")){
                System.out.println("Type your username: ");
                String username = scanner.next();

                System.out.println("Type your password: ");
                String password = scanner.next();

                if(validateUser(username, password)){
                    RegisterRequest req = new RegisterRequest();

                    req.username = username;
                    req.password = password;

                    out.print(req.createPacket());
                    out.flush();

                    Command nextCommand = Command.parse(in);
                    System.out.println("got response");

                    if(nextCommand.getType() == Command.CommandType.RegisterResponse){
                        RegisterResponse res = (RegisterResponse) nextCommand;

                        if(res.getErrorCode().equals(RegisterResponse.SUCCESS)){
                            System.out.println("User Created Successfully");
                        }
                        else{
                            System.out.println("User Creation Failed:" + res.getErrorCode());
                        }
                    }
                    else{
                        System.out.println("Unexpected Response.");

                    }
                }


            }

        }





//
//        do {
//
//            message = scanner.next();
//            pw.println(message);
//
//            String response;
//
//            response = br.readLine();
//            System.out.println("Server : " + response);
//        }
//        while (!message.startsWith("bye"));
//
//        LogoutRequest l = new LogoutRequest();
//
//        // todo from scanner
//        c1.username = "Melina";
//        c1.password = "12345";
//
//        out.print(c1.createPacket());
//        out.flush();
//
//
//
//        out.print(l.createPacket());
//        out.flush();
//
//        String response;
//
//        do {
//            response = in.readLine();
//            System.out.println("Server : " + response);
//        }
//        while (!response.startsWith("exit"));

        out.close();
        in.close();
    }

    private static boolean validateUser(String username, String password) {

        return true;
    }


}
