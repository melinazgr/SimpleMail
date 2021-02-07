import Mail.Command;
import Mail.Email;
import Mail.ReadEmailResponse;
import Mail.ShowEmailsResponse;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ReadEmailResponseTest {
    @Test
    public void parsePacket1() throws IOException, InterruptedException {
        ReadEmailResponse res = new ReadEmailResponse();
        ArrayList<Email> mailbox = new ArrayList<>();

        Email e1 = new Email("1", "foo@foo.bar", "melina@csd.gr", "Hello", "Hello world.\nthe sky is blue.", true);

        mailbox.add(e1);

        res.setEmail(e1);
        res.setErrorCode(ReadEmailResponse.SUCCESS);

        String s = res.createPacket();

        InputStream stream = new ByteArrayInputStream(s.getBytes());
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));

        ReadEmailResponse clientRes = (ReadEmailResponse) Command.parse(br);

        assertEquals(clientRes.getErrorCode(), ReadEmailResponse.SUCCESS);

        Email client1 = clientRes.getEmail();

        assertEquals(client1.getId(), e1.getId());
        assertEquals(client1.getSender(), e1.getSender());
        assertEquals(client1.getSubject(), e1.getSubject());
        assertEquals(client1.getMainbody(), e1.getMainbody());

    }

}