package MailClient;

import Mail.LogoutCommand;
import Mail.RegisterCommand;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] argv) throws IOException {
        Socket s = new Socket();
        String host = "127.0.0.1";
        int port = 5000;

        String helpText = "Usage: java Main [-address <IP address>]\n" +
                "                 [-port]\n";

        InetAddress localAddress = null;
        InetAddress remoteAddress = null;

        PrintWriter pw = null;
        BufferedReader br = null;

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

            pw = new PrintWriter(s.getOutputStream(), true);
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
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

        RegisterCommand c1 = new RegisterCommand();
        LogoutCommand l = new LogoutCommand();

        // todo from scanner
        c1.username = "Melina";
        c1.password = "12345";

        pw.print(c1.createPacket());
        pw.flush();
        pw.print(l.createPacket());
        pw.flush();

        String response;

        do {
            response = br.readLine();
            System.out.println("Server : " + response);
        }
        while (!response.startsWith("exit"));



        pw.close();
        br.close();
        s.close();
    }

}
