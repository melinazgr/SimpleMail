package MailServer;

import Mail.Account;
import Mail.AccountManager;
import Mail.Email;

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

            // create users
            Account user1 = new Account("melina@csd.gr", "12345");
            Account user2 =  new Account("user@csd.gr", "6789");

            // emails sent from user2 to user1
            Email email1 = new Email("1", user2.getUsername(), user1.getUsername(), "Foo Bar", "The terms foobar, foo, bar, and others are used as \n" +
                    "metasyntactic variables and placeholder names in \n" +
                    "computer programming or computer-related documentation.", true);
            Email email2 = new Email("2", user2.getUsername(), user1.getUsername(), "Foo Bar History", "The etymology of foobar could be derived from the military slang from the World War II era FUBAR, which was bowdlerised to foobar.", true);
            Email email3 = new Email("3", user2.getUsername(), user1.getUsername(), "FooBar", "Foo\nBar\nFoo Bar\n Foobar Foobar Foobar", true);

            // emails sent from user1 to user2
            Email email4 = new Email("1", user1.getUsername(), user2.getUsername(), "Lorem Ipsum", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", true);
            Email email5 = new Email("2", user1.getUsername(), user2.getUsername(), "Lorem", " Elit ullamcorper dignissim cras tincidunt lobortis feugiat.", true);
            Email email6 = new Email("3", user1.getUsername(), user2.getUsername(), "lorem ipsum", "Lorem ipsum dolor sit amet, consectetur adipiscing elit,\n" +
                    "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n" +
                    " Ut enim ad minim veniam, quis nostrud exercitation ullamco \n" +
                    "laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure \n" +
                    "dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat \n" +
                    "nulla pariatur. Excepteur sint occaecat cupidatat non proident, \n" +
                    "sunt in culpa qui officia deserunt mollit anim id est laborum.", true);

            user1.addEmail(email1);
            user1.addEmail(email2);
            user1.addEmail(email3);

            user2.addEmail(email4);
            user2.addEmail(email5);
            user2.addEmail(email6);

            AccountManager.getInstance().addAccount(user1);
            AccountManager.getInstance().addAccount(user2);

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
