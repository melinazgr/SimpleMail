package MailClient;

import Mail.Command;
import Mail.LogoutRequest;
import Mail.RegisterRequest;
import Mail.RegisterResponse;

import java.io.*;
import java.net.*;

public class Main {
    public static void main(String[] argv) throws IOException {
        Socket s = new Socket();
        String host = "127.0.0.1";
        int port = 5000;

        String helpText = "Usage: java Main [-address <IP address>]\n" +
                "                 [-port]\n";

        InetAddress localAddress = null;
        InetAddress remoteAddress = null;

        PrintWriter out = null;
        BufferedReader in = null;

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

            out = new PrintWriter(s.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + host);
            System.exit(1);

        }

        System.out.println("Connected");
        System.out.println("local: " + localAddress + " : " + s.getLocalPort());
        System.out.println("remote: " + remoteAddress);

//        Scanner scanner = new Scanner(System.in);
//        String message = null;
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

        RegisterRequest c1 = new RegisterRequest();
        LogoutRequest l = new LogoutRequest();

        // todo from scanner
        c1.username = "Melina";
        c1.password = "12345";

        out.print(c1.createPacket());
        out.flush();

        Command nextCommand = Command.parse(in);

        if(nextCommand.getType() == Command.CommandType.RegisterResponse){
            RegisterResponse res = (RegisterResponse) nextCommand;

            if(res.getErrorCode().equals(RegisterResponse.SUCCESS)){
                System.out.println("User Created Successfully");
            }
            else{
                System.out.println("User Creation Failed");
            }
        }



        out.print(l.createPacket());
        out.flush();

        String response;

        do {
            response = in.readLine();
            System.out.println("Server : " + response);
        }
        while (!response.startsWith("exit"));



        out.close();
        in.close();
        s.close();
    }

}
