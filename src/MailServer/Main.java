package MailServer;

import java.io.*;
import java.net.*;

/**
 * Server Clalss
 *
 * @author Melina Zikou
 *
 */
public class Main {
    public static void main (String[] argv) throws IOException {
        ServerSocket s = null;
        Socket client = null;
        int port = 5000; // default port

        String helpText = "Usage: java Main [-port]\n";

        // creation of argument options
        // if not set, the default values are used
        try {
            // -port argument / used for the port
            for (int i = 0; i < argv.length; i++) {
                if (argv[i].equals("-port")) {
                    i++;
                    if (i >= argv.length) {
                        throw new Exception("Missing port");
                    }

                    port = Integer.parseInt(argv[i]);
                }
                // -help argument / used for showing the available arguments
                else if (argv[i].equals("-help")) {
                    System.out.println(helpText);
                    System.exit(1);
                }
                else {
                    throw new Exception(helpText);
                }
            }
        }

        catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        try{
            // create socket
            s = new ServerSocket(port, 10);

            while(true){
                // waiting for connection
                client = s.accept();
                System.out.println("connection from " + client.getInetAddress());

                new ClientHandler(client).start();
            }
        }

        catch(IOException e){
            System.out.println("IOException");
        }

        try {
            s.close();
        }
        catch(IOException e){
            System.out.println("Unable to close");
        }
    }
}
