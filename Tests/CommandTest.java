import Mail.Command;
import Mail.LoginRequest;
import Mail.RegisterRequest;
import org.junit.jupiter.api.Test;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

class CommandTest {

    @Test
    public void parsePacket1() throws IOException, InterruptedException {
        String username = "Melina";
        String password = "1234";

        String s1 = RegisterRequest.createPacket(username, password);
        String s2 = LoginRequest.createPacket(username, password);

        StringBuilder sb = new StringBuilder();
        sb.append(s1);
        sb.append(s2);


        InputStream stream = new ByteArrayInputStream(sb.toString().getBytes());
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));

        RegisterRequest c1 = (RegisterRequest) Command.parse(br);

        assertEquals(c1.getUsername(), username);
        assertEquals(c1.getPassword(), password);

        LoginRequest c2 = (LoginRequest) Command.parse(br);
        assertEquals(c2.getUsername(), username);
        assertEquals(c2.getPassword(), password);


    }
}

//todo menu
//