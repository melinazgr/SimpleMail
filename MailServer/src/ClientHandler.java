import java.io.*;
import java.net.*;

public class ClientHandler extends Thread{
    private Socket client;

    public ClientHandler(Socket client){
        this.client = client;
    }

    public void run(){

        PrintStream out = null;
        BufferedReader in = null;
        String message = null;

        try{
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

    }
}
