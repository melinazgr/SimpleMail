package Mail;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterCommandTest {


    @Test
    public void parsePacket1() throws IOException{
        String username = "Melina";
        String password = "1234";

        String s = RegisterCommand.createPacket(username, password);

        InputStream stream = new ByteArrayInputStream(s.getBytes());
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));

        RegisterCommand c = (RegisterCommand) Command.parse(br);

        assertEquals(c.username, username);
        assertEquals(c.password, password);

    }
}