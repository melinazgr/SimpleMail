package Mail;

import org.junit.jupiter.api.Test;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

class CommandTest {

    @Test
    public void parsePacket1() throws IOException {
        String username = "Melina";
        String password = "1234";

        String s1 = RegisterCommand.createPacket(username, password);
        String s2 = LoginCommand.createPacket(username, password);

        StringBuilder sb = new StringBuilder();
        sb.append(s1);
        sb.append(s2);


        InputStream stream = new ByteArrayInputStream(sb.toString().getBytes());
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));

        RegisterCommand c1 = (RegisterCommand) Command.parse(br);

        assertEquals(c1.username, username);
        assertEquals(c1.password, password);

        LoginCommand c2 = (LoginCommand) Command.parse(br);
        assertEquals(c2.username, username);
        assertEquals(c2.password, password);


    }
}

//todo menu
//