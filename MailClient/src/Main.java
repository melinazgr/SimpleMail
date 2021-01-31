import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Main {
    public static void main (String[] args) throws IOException{
        Socket s = new Socket();
        String host = "127.0.0.1";

        InetAddress localAddress = null;
        InetAddress remoteAddress = null;

        PrintWriter pw = null;
        BufferedReader br = null;

        try {
            s.connect(new InetSocketAddress(host, 5000));
            localAddress = s.getLocalAddress();
            remoteAddress = s.getInetAddress();

            pw = new PrintWriter(s.getOutputStream(), true);
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        }

        catch (UnknownHostException e){
            System.err.println("Unknown host: " + host);
            System.exit(1);

        }

        System.out.println("Connected");
        System.out.println("local: " + localAddress + " : " + s.getLocalPort());
        System.out.println("remote: " + remoteAddress);

        Scanner scanner = new Scanner(System.in);
        String message = null;

        do{
            //        String message = "GET / HTTP/1.1\r\n\r\n";

            message = scanner.next();
            pw.println(message);

            String response;

            response = br.readLine();
            System.out.println("Server : " + response);
        }
        while(!message.startsWith("bye"));

        pw.close();
        br.close();
        s.close();
    }
}
