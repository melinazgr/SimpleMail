import Mail.*;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ShowEmailsResponseTest {

    @Test
    public void parsePacket1() throws IOException, InterruptedException {
        ShowEmailsResponse res = new ShowEmailsResponse();
        ArrayList<Email> mailbox = new ArrayList<>();

        Email e1 = new Email("1", "foo@foo.bar", "melina@csd.gr", "Hello", "Hello world. the sky is blue.", true);
        Email e2 = new Email("2", "foo@foo.bar", "melina@csd.gr", "Hello2", "Hello world. the sky is blue.", true);

        mailbox.add(e1);
        mailbox.add(e2);

        res.setMailbox(mailbox);
        res.setErrorCode(ShowEmailsResponse.SUCCESS);

        String s = res.createPacket();

        InputStream stream = new ByteArrayInputStream(s.getBytes());
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));

       ShowEmailsResponse clientRes = (ShowEmailsResponse) Command.parse(br);

        assertEquals(clientRes.getErrorCode(), ShowEmailsResponse.SUCCESS);

        assertEquals(clientRes.getMailbox().size(), 2);

        Email client1 = clientRes.getMailbox().get(0);
        assertEquals(client1.getId(), e1.getId());
        assertEquals(client1.getSender(), e1.getSender());
        assertEquals(client1.getSubject(), e1.getSubject());
        assertEquals(client1.getIsNew(), e1.getIsNew());

    }
}