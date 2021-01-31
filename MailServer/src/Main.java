import java.io.*;
import java.net.*;
import java.nio.Buffer;

public class Main {
    public static void main (String[] args) throws IOException {
        ServerSocket s = null;
        Socket client = null;
        PrintStream out = null;
        BufferedReader in = null;
        String message = null;

        try{
            // create socket
            s = new ServerSocket(5000, 10);

            // waiting for connection
            client = s.accept();
            System.out.println("connection from " + client.getInetAddress());

            // input output
            out = new PrintStream(client.getOutputStream());
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));


            do {
                // read input from client
                message = (String)in.readLine();
                System.out.println("<client" + message);

                if (message != null){
                    out.println(message);
                    out.flush();

                }
                else {
                    System.out.println("Client has disconnected");
                    break;
                }
            }
            while (!message.equals("bye"));

        }

        catch(IOException e){
            System.out.println("IOException");
        }

        try {
            in.close();
            out.close();
            s.close();
        }

        catch(IOException e){
            System.out.println("Unable to close");
        }

    }
}
